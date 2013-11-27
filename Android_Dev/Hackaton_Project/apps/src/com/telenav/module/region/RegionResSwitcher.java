/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * RegionSwitcher.java
 *
 */
package com.telenav.module.region;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.ServerProxyConfig;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.data.serverproxy.impl.IBatchProxy;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.module.UserProfileProvider;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author chrbu
 * @modifier bduan
 * @date 2012-2-13
 */
public class RegionResSwitcher implements IServerProxyListener
{
    public static final int SUCCESS_FROM_SERVER = 0;

    public static final int SUCCESS_FROM_CACHE = 1;
    
    public static final int FAIL = 2;
    

    protected static final int SEND_REQUEST_STEP_1 = 1; // send sync combination

    protected static final int SEND_REQUEST_STEP_2 = 2; // send cache cities

    protected int currentStep = SEND_REQUEST_STEP_1;

    protected UserProfileProvider userProfileProvider = null;

    protected String previousRegion = null;
    
    private IRegionResSwitcherListener regionSwitcherListener;
    
    
    /**
     * constructor
     * @param regionSwitchListener listener for handling switch succ or fail event.
     */
    public RegionResSwitcher(IRegionResSwitcherListener regionSwitchListener)
    {
        this.regionSwitcherListener = regionSwitchListener;
    }

    public void transactionFinished(AbstractServerProxy serverProxy, String jobId)
    {
        if (serverProxy instanceof IBatchProxy)
        {
            int step = 0;
            IBatchProxy batchProxy = (IBatchProxy) serverProxy;
            if (batchProxy.getBatchItems() != null)
            {
                int size = batchProxy.getBatchItems().size();
                for (int i = 0; i < size; i++)
                {
                    Vector batchRequests = (Vector) batchProxy.getBatchItems().elementAt(i);

                    int requestSize = batchRequests.size();
                    boolean isFound = false;
                    for (int m = 0; m < requestSize; m++)
                    {
                        RequestItem requestItem = (RequestItem) batchRequests.elementAt(m);
                        if (requestItem.serverProxy instanceof ISyncCombinationResProxy)
                        {
                            ISyncCombinationResProxy syncResourceProxy = (ISyncCombinationResProxy) requestItem.serverProxy;
                            step = syncResourceProxy.getStep();
                            isFound = true;
                            break;
                        }
                    }

                    if (isFound)
                        break;
                }
            }

            if (step == 1)
            {
                currentStep = SEND_REQUEST_STEP_2;
                syncUpRegionRes();
            }
            else
            {
                updateStatus("", FAIL);
            }
        }
        if (serverProxy instanceof IAddressProxy)
        {
            updateStatus("", SUCCESS_FROM_SERVER);
        }
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {

    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        handleError(proxy);
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        handleError(proxy);
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }

    public int switchRegion(String region)
    {
        userProfileProvider = new UserProfileProvider();
        userProfileProvider.setRegion(region);
        this.previousRegion = region;
        int type = SUCCESS_FROM_SERVER;
        boolean isRegionCached = RegionUtil.getInstance().isRegionCached(region);
        if (isRegionCached)
        {
            type = SUCCESS_FROM_CACHE;
        }
        else
        {
            syncUpRegionRes();
        }
        return type;
    }
    
    /**
     * cancel on-going switching behavior.
     */
    public static void cancelRegionSwitching()
    {
        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE);
        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_CACHE_CITIES);
    }
    
    private void updateStatus(String message, int resultType)
    {
        if (regionSwitcherListener != null)
        {
            if (SUCCESS_FROM_SERVER == resultType)
            {
                appendRegionToDao();
                AbstractDaoManager.getInstance().getResourceBarDao().store();
            }
            regionSwitcherListener.onRegionSwitched(message, resultType);
        }
    }

    private void appendRegionToDao()
    {
        Vector<String> regions = DaoManager.getInstance().getSimpleConfigDao()
                .getVector(SimpleConfigDao.KEY_CACHED_REGION);
        boolean isRegionCached = RegionUtil.getInstance().isRegionCached(getCurrentRegion());
        if (regions.size() > 0 && !isRegionCached)
        {
            regions.addElement(getCurrentRegion());
            DaoManager.getInstance().getSimpleConfigDao().putVector(SimpleConfigDao.KEY_CACHED_REGION, regions);
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
        }
    }

    private void handleError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IBatchProxy || proxy instanceof IAddressProxy)
        {
            proxy.cancel();
            updateStatus(proxy.getErrorMsg(), FAIL);
        }
    }
    
    private IBatchProxy createBatchProxy()
    {
        Host cachecitiesHost = CommManager.getInstance().getComm().getHostProvider()
                .createHost(IServerProxyConstants.ACT_CACHE_CITIES);
        IBatchProxy batchProxy = ServerProxyFactory.getInstance().createBatchProxy(cachecitiesHost,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        return batchProxy;
    }

    private void syncUpRegionRes()
    {
        switch (currentStep)
        {
            case SEND_REQUEST_STEP_1:
            {
                IBatchProxy batchProxy = createBatchProxy();
                sendSyncResourceRequest(batchProxy);
                batchProxy.send(ServerProxyConfig.defaultTimeout * 3);
                break;
            }
            case SEND_REQUEST_STEP_2:
            {
                syncCachedCitiesResource();
                break;
            }
        }
    }

    private void syncCachedCitiesResource()
    {
        TnLocation tnLocation = AbstractDaoManager.getInstance().getResourceBarDao()
                .getRegionAnchor(getCurrentRegion());
        if (tnLocation != null)
        {
            boolean isExisted = AbstractDaoManager.getInstance().getNearCitiesDao()
                    .isNearCityDownloaded(getCurrentRegion());
            if (isExisted)
            {
                updateStatus("", SUCCESS_FROM_SERVER);
            }
            else
            {
                IAddressProxy proxy = ServerProxyFactory.getInstance()
                        .createAddressProxy(null, CommManager.getInstance().getComm(),
                            this, userProfileProvider, false);
                proxy.updateCities(tnLocation.getLatitude(), tnLocation.getLongitude());

            }
        }
        else
        {
            updateStatus("", FAIL);
        }
    }

    private String getCurrentRegion()
    {
        return userProfileProvider == null ? RegionUtil.getInstance().getCurrentRegion()
                : userProfileProvider.getRegion();
    }

    private void sendSyncResourceRequest(IBatchProxy batchProxy)
    {
        ISyncCombinationResProxy combinationResProxy = ServerProxyFactory.getInstance().createSyncCombinationResProxy(
            null, CommManager.getInstance().getComm(), this, userProfileProvider);
        Vector<Integer> combination = new Vector<Integer>();
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_POI_BRAND_NAME));
        combination.addElement(ISyncCombinationResProxy.TYPE_SERVER_DRIVEN_PARAMETERS);
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_CATEGORY_TREE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_HOT_POI_CATEGORY));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_GOBY_EVENT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_REGION_CENTER_POINT));
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE,
                (AbstractServerProxy) combinationResProxy);
        requestItem.params = new Vector();
        requestItem.params.addElement(combination);
        batchProxy.addBatchItem(requestItem);
    }
    
    public interface IRegionResSwitcherListener
    {
        public void onRegionSwitched(String message,int resultType);
    }
}

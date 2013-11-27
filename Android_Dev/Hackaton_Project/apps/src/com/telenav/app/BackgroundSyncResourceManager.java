/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * BackgroundNetworkRequester.java
 *
 */
package com.telenav.app;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.data.serverproxy.impl.IBatchProxy;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.log.mis.MisLogManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.ac.SyncStopsExecutor;
import com.telenav.module.mapdownload.MapDownloadOnBoardDataStatusManager;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.module.sync.apache.Ii18nRequestListener;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author jwchen (jwchen@telenav.cn)
 *@date 2011-4-8
 */
public class BackgroundSyncResourceManager implements IServerProxyListener, Ii18nRequestListener
{
    private static final int QUERY_SYNC_APACHE_RES = 0;
    private static final int QUERY_BATCH_REQUEST = 1;
    private static final int QUERY_MISLOG_REQUEST = 2;
    private static final int QUERY_SYNC_NEW_STOPS = 3;
    
    private static BackgroundSyncResourceManager instance;
    
    private boolean[] requestQueryStatus =
    { 
            false, // QUERY_SYNC_APACHE_RES
            false, // QUERY_BATCH_REQUEST
            false, // QUERY_MISLOG_REQUEST
            false, // QUERY_SYNC_NEW_STOPS
    };
    private IBatchProxy batchProxy;
    
    private int batchGroupSize = 0;
    
    private long syncTimeStamp;
    
    private int timeout;
    
    private BackgroundSyncResourceManager()
    {
        timeout = ((DaoManager)DaoManager.getInstance()).getServerDrivenParamsDao().getIntValue(ServerDrivenParamsDao.APP_BACKGROUND_LIVE_TIME);
        if(timeout <= 0)
        {
            timeout = TeleNavDelegate.DEFAULT_MAX_EXIT_TIME;
        }
    }
    
    public static BackgroundSyncResourceManager getInstance()
    {
        if(instance == null)
        {
            instance = new BackgroundSyncResourceManager();
        }
        
        return instance;
    }
    
    private void resetRequestStatus()
    {
        for(int i = 0; i < requestQueryStatus.length; i++)
        {
            requestQueryStatus[i] = true;
        }
    }
    
    public void syncResourcesInDeactivate()
    {
        resetRequestStatus();

        if (!DaoManager.getInstance().getBillingAccountDao().isLogin())
        {
            return;
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "syncResourcesInDeactivate");
        
        syncTimeStamp = System.currentTimeMillis();
        
        batchProxy = ServerProxyFactory.getInstance().createBatchProxy(null, CommManager.getInstance().getComm(), this, null);
        
        syncPurchaseStatus(batchProxy);
        sendMisLog(null);//mislog use single request in case block by other transaction error.
        syncAllResourceRequest(batchProxy);
        syncSettingData(batchProxy, true);
        
        batchGroupSize = batchProxy.getBatchItems() == null? 0 : batchProxy.getBatchItems().size();
        
        batchProxy.send(timeout);
        requestQueryStatus[QUERY_BATCH_REQUEST] = false;
        
        syncApacheResource();
        
        syncNewStops();
        
        MapDownloadOnBoardDataStatusManager.getInstance().queryMapDownloadStatus();
    }
    
    public void syncApacheResource()
    {
        requestQueryStatus[QUERY_SYNC_APACHE_RES] = false;
        SyncResExecutor.getInstance().syncI18nResource(ResourceManager.getInstance().getCurrentLocale(), this);
    }
    
    public void syncInActivateFromJumb()
    {
        resetRequestStatus();
        
        if (!DaoManager.getInstance().getBillingAccountDao().isLogin())
        {
            return;
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "syncInActivateFromJumb");
        
        syncTimeStamp = System.currentTimeMillis();
        
        batchProxy = ServerProxyFactory.getInstance().createBatchProxy(null, CommManager.getInstance().getComm(), this, null);
        syncPurchaseStatus(batchProxy);
        sendStartUp(batchProxy);
        sendMisLog(null);//mislog use single request in case block by other transaction error.
        syncSetting(batchProxy);
        syncSettingData(batchProxy);
        
        batchGroupSize = batchProxy.getBatchItems() == null? 0 : batchProxy.getBatchItems().size();
        batchProxy.send(timeout);
        
        MapDownloadOnBoardDataStatusManager.getInstance().queryMapDownloadStatus();
    }
    
    private void sendStartUp(IBatchProxy batchProxy)
    {
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_STARTUP, 
            (AbstractServerProxy) ServerProxyFactory.getInstance().createStartupProxy(null, CommManager.getInstance().getComm(), this));
        requestItem.params = new Vector();
        requestItem.params.addElement(PrimitiveTypeCache.valueOf(DaoManager.getInstance().getAddressDao().getSyncTime(false)));
        batchProxy.addBatchItem(requestItem);
    }

    public boolean isSyncFinished()
    {
        long currentTime = System.currentTimeMillis();
        if(currentTime - this.syncTimeStamp > timeout)
        {
            syncTimeStamp = 0;
            
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, DaoManager.class.getName(), " ********* isSyncFinished -- 0 ********* ");
                
                MandatoryProfile mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();
                if(mandatoryNode == null)
                {
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), " mandatoryNode == null ");
                }
                else
                {
                    UserInfo userInfo = mandatoryNode.getUserInfo();
                    if(userInfo == null)
                    {
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " userInfo == null ");
                    }
                    else if (userInfo.phoneNumber == null
                            || userInfo.phoneNumber.length() == 0
                            || userInfo.userId == null || userInfo.userId.length() == 0)
                    {
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                        Logger.log(Logger.ERROR, DaoManager.class.getName(),
                            " missing --> ptn ? ptn = " + userInfo.phoneNumber
                                    + " , or userId? userId = " + userInfo.userId);
                    }
                }
            }
            
            return true;
        }
        
        boolean canExit = requestQueryStatus[QUERY_SYNC_APACHE_RES]
                && requestQueryStatus[QUERY_BATCH_REQUEST]
                && requestQueryStatus[QUERY_MISLOG_REQUEST]
                && requestQueryStatus[QUERY_SYNC_NEW_STOPS];
        
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, this.getClass().getName(),
                "Background Sync : QUERY_SYNC_APACHE_RES : "
                        + requestQueryStatus[QUERY_SYNC_APACHE_RES]
                        + " , QUERY_BATCH_REQUEST : "
                        + requestQueryStatus[QUERY_BATCH_REQUEST]
                        + " , QUERY_MISLOG_REQUEST : "
                        + requestQueryStatus[QUERY_MISLOG_REQUEST]
                        + " , QUERY_SYNC_NEW_STOPS : "
                        + requestQueryStatus[QUERY_SYNC_NEW_STOPS]);
            
            if(canExit)
            {
                Logger.log(Logger.INFO, DaoManager.class.getName(), " ********* isSyncFinished -- 1 ********* ");
                
                MandatoryProfile mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();
                if(mandatoryNode == null)
                {
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), " mandatoryNode == null ");
                }
                else
                {
                    UserInfo userInfo = mandatoryNode.getUserInfo();
                    if(userInfo == null)
                    {
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " userInfo == null ");
                    }
                    else if (userInfo.phoneNumber == null
                            || userInfo.phoneNumber.length() == 0
                            || userInfo.userId == null || userInfo.userId.length() == 0)
                    {
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                        Logger.log(Logger.ERROR, DaoManager.class.getName(),
                            " missing --> ptn ? ptn = " + userInfo.phoneNumber
                                    + " , or userId? userId = " + userInfo.userId);
                    }
                }
            }
        }
        
        return canExit;
    }
    
    private void updateRequestStatus(AbstractServerProxy proxy, int status)
    {
        if(proxy instanceof IBatchProxy)
        {
            batchGroupSize --;
            requestQueryStatus[QUERY_BATCH_REQUEST] =  (batchGroupSize == 0);
        }
        else if(proxy instanceof IToolsProxy)
        {
            if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_SEND_MIS_REPORTS))
            {
                if(status == IServerProxyConstants.FAILED)
                {
                    MisLogManager.getInstance().rollback();
                }
                requestQueryStatus[QUERY_MISLOG_REQUEST] =  true;
            }
        }
        else if(proxy instanceof IAddressProxy)
        {
            requestQueryStatus[QUERY_SYNC_NEW_STOPS] = true;
        }
    }
    
    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        boolean isNetworkFail = false;
        
        try
        {
            isNetworkFail = !NetworkStatusManager.getInstance().isConnected();
        }
        catch (Exception e)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Network is not connected.");
        }
        
        if (isNetworkFail)
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NO_CELL_COVERAGE, IStringCommon.FAMILY_COMMON);
            proxy.setErrorMsg(errorMessage);
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return !isNetworkFail;
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        updateRequestStatus(proxy, IServerProxyConstants.FAILED);
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        updateRequestStatus(proxy, IServerProxyConstants.FAILED);
    }

    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        if(proxy instanceof ISyncPurchaseProxy)
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                DaoManager.getInstance().clearInternalRMS();
                TeleNavDelegate.getInstance().exitApp();
            }
        }
        
        updateRequestStatus(proxy, IServerProxyConstants.SUCCESS);
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
    }
    
    
    protected void syncSettingData(IBatchProxy batchProxy)
    {
        syncSettingData(batchProxy, false);
    }
    
    protected void syncSettingData(IBatchProxy batchProxy, boolean isUpLoad)
    {
        int syncType = isUpLoad ? ISettingDataProxy.SYNC_TYPE_UPLOAD : ISettingDataProxy.SYNC_TYPE_DOWNLOAD;
        
        SyncResExecutor.getInstance().syncSettingData(batchProxy, null, this, syncType);
    }

    protected void syncNewStops()
    {
        if(!MigrationExecutor.getInstance().isSyncEnabled() || MigrationExecutor.getInstance().isInProgress())
        {
            //[DB]set this flag to true, since we do not need check this in isSyncFinished()
            requestQueryStatus[QUERY_SYNC_NEW_STOPS] = true;
        }
        else
        {
            requestQueryStatus[QUERY_SYNC_NEW_STOPS] = false;
        	SyncStopsExecutor.getInstance().syncStop(this, null);
        }
    }

    protected void syncPurchaseStatus(IBatchProxy batchProxy)
    {
        RequestItem requestItem = new RequestItem(
            IServerProxyConstants.ACT_SYNC_PURCHASE, (AbstractServerProxy)ServerProxyFactory.getInstance().createSyncPurchaseProxy(null, CommManager.getInstance().getComm(), this, null));
        requestItem.params = new Vector();
        requestItem.params.addElement(FeaturesManager.APP_CODE);
        batchProxy.addBatchItem(requestItem);
    }
    
    protected void syncAllResourceRequest(IBatchProxy batchProxy)
    {
        Vector combination = new Vector();
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_SERVER_DRIVEN_PARAMETERS));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_SERVICE_LOCATOR_INFO));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_CATEGORY_TREE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AIRPORT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_RESOURCE_FORMAT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_POI_BRAND_NAME));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_HOT_POI_CATEGORY));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_GOBY_EVENT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_MAP_DATA_UPGRADE));
        //always use preload res.
        //combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AUDIO_AND_RULE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_DSR_SDP_NODE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_CAR_MODEL));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING));
        
        syncResourceRequest(combination, batchProxy);

    }
    
    protected void syncSetting(IBatchProxy batchProxy)
    {
        Vector combination = new Vector();
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING));
        syncResourceRequest(combination, batchProxy);
    }
    
    protected void syncResourceRequest(Vector combination, IBatchProxy batchProxy)
    {
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE, 
            (AbstractServerProxy) ServerProxyFactory.getInstance().createSyncCombinationResProxy(null, CommManager.getInstance().getComm(), this, null));
        requestItem.params = new Vector();
        requestItem.params.addElement(combination);
        batchProxy.addBatchItem(requestItem);
    }
    
    protected void sendMisLog(IBatchProxy batchProxy)
    {
        requestQueryStatus[QUERY_MISLOG_REQUEST] = false;
        MisLogManager misLogManager = MisLogManager.getInstance();
        if(!misLogManager.requestSendMisLog(this, batchProxy))//set it to finish if no log to send.
        {
            requestQueryStatus[QUERY_MISLOG_REQUEST] = true;
        }
    }

    public void update(int status, int syncCount)
    {
        requestQueryStatus[QUERY_SYNC_APACHE_RES] = true;
    }

}

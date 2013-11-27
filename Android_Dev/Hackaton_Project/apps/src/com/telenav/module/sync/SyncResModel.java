/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StartUpModel.java
 *
 */
package com.telenav.module.sync;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.map.MapDataUpgradeInfo;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.ServerProxyConfig;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.data.serverproxy.impl.IBatchProxy;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.IStartupProxy;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.json.JsonDimProxy;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.region.RegionUtil;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-12-2
 */
class SyncResModel extends AbstractCommonNetworkModel implements ISyncResConstants
{
    protected boolean isFreshSyncFinish = false;
    
    protected boolean isSendCombinationRes = false;
    
    protected int currentStep = SEND_LOGIN_REQUEST_STEP_1;
    
    protected int INVENTORY_LOAD_TIME_OUT = 20 * 1000;
    
    protected long timeStamp = -1L;
    
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_CHECK_TYPE:
            {  
            	put(KEY_B_SYNCRES_OCCUR_ERROR, false);
                checkSyncType();
                break;
            }
            case ACTION_CHECK_REGION_DETECT_STATUS:
            {
                checkRegionDetectStatus();
                break;
            }
            case ACTION_FRESH_SYNC:
            {
                AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
                    IMisLogConstants.TYPE_INNER_APP_STATUS);
                appStatusMisLog.startSyncRes();
                syncRes(currentStep);
                break;
            }
            case ACTION_HANDLE_ERROR:
            {
                handleError();
				break;
            }
            case ACTION_CLOSE_MENU:
            {
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeOptionsMenu();
                break;
            }
            default:
            {
                break;
            }
        }
    }

    protected void handleRegionDetected(String detectedRegion)
    {
        Logger.log(Logger.INFO, getClass().toString(), "before sync res, detected region is " + detectedRegion);
        if (RegionUtil.getInstance().isLegalRegion(detectedRegion))
        {
            if (RegionUtil.getInstance().isRegionSupported(detectedRegion))
            {
                RegionUtil.getInstance().setCurrentRegion(detectedRegion);
            }
        }
        
        //clearResources();
        Logger.log(Logger.INFO, getClass().toString(), "start sync resource after region detection");
        postModelEvent(EVENT_MODEL_DO_FRESH_SYNC);
    }
    
    protected void clearResources()
    {
        try
        {
            StringMap serverDrivenParam = DaoManager.getInstance().getServerDrivenParamsDao().getServerParams();
            DaoManager.getInstance().getServerDrivenParamsDao().setServerParams(serverDrivenParam, getRegion(), "");
            
            StringMap dsrServerDrivenParam = DaoManager.getInstance().getServerDrivenParamsDao().getServerParams();
            DaoManager.getInstance().getDsrServerDrivenParamsDao().setServerParams(dsrServerDrivenParam, getRegion(), "");
            
            DaoManager.getInstance().getResourceBarDao().setResourceFormatVersion("");
            DaoManager.getInstance().getResourceBarDao().setBrandNameVersion("", getRegion());
            DaoManager.getInstance().getResourceBarDao().setHotPoiVersion("", getRegion());
            DaoManager.getInstance().getResourceBarDao().setCategoryVersion("", getRegion());
            DaoManager.getInstance().getResourceBarDao().setAirportVersion("");
            DaoManager.getInstance().getResourceBarDao().setMapDataUpgradeInfoVersion("");
            DaoManager.getInstance().getResourceBarDao().setPreferenceSettingVersion("", getRegion());
            DaoManager.getInstance().getResourceBarDao().setCenterPoint("", getRegion());
            DaoManager.getInstance().getAddressDao().clear();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    protected void handleError()
    {
        int type = this.getInt(KEY_I_SYNC_TYPE);
        if (type == TYPE_FRESH_SYNC)
        {
            TeleNavDelegate.getInstance().exitApp();
        }
    }

    protected void checkSyncType()
    {
        int type = this.getInt(KEY_I_SYNC_TYPE);
        switch(type)
        {
            case TYPE_FRESH_SYNC:
            {
                this.postModelEvent(EVENT_MODEL_DO_FRESH_SYNC);
                break;
            }
            case TYPE_STARTUP_SYNC:
            {
                this.postModelEvent(EVENT_MODEL_DO_STARTUP_SYNC);
                break;
            }
            case TYPE_EXIT_SYNC:
            {
                this.postModelEvent(EVENT_MODEL_DO_EXIT_SYNC);
                break;
            }
        }
    }
    
    protected void syncRes(int step)
    {
        IBatchProxy batchProxy = null;
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        Logger.log(Logger.INFO, this.getClass().getName(), "-- syncRes -- step: " + step);
        if (step == SEND_LOGIN_REQUEST_STEP_1)
        {
            timeStamp = System.currentTimeMillis();
            boolean needParseInThread = false;
            boolean isSignUp = DaoManager.getInstance().getBillingAccountDao().getLoginType() == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP;
            if(isSignUp)
            {
                userProfileProvider = new UserProfileProvider();
                userProfileProvider.getMandatoryNode().getCredentialInfo().credentialID = "";
                userProfileProvider.getMandatoryNode().getCredentialInfo().credentialPassword = "";
                userProfileProvider.getMandatoryNode().getCredentialInfo().credentialType = "";
                userProfileProvider.getMandatoryNode().getCredentialInfo().credentialValue = "";
            }
            
            sendFetchAllStopsRequest(batchProxy, needParseInThread, userProfileProvider);//COMMON SERVER
            
            if(isSignUp)
            {
                sendDownloadCsrIDRequest(batchProxy); //COMMON SERVER, only need download the csrId.
            }
        }
        else if (step == SEND_LOGIN_REQUEST_STEP_2)
        {
            checkInventoryBeforeRequest();//check whether the inventory is ok , if not , wait for it until time out. 
            Host startUpHost = CommManager.getInstance().getComm().getHostProvider().createHost(IServerProxyConstants.ACT_STARTUP);
            batchProxy = ServerProxyFactory.getInstance().createBatchProxy(startUpHost, CommManager.getInstance().getComm(), this, userProfileProvider);
            sendCachedCities(batchProxy);           //RESOURCE SERVER  
            sendFastStartupRequest(batchProxy);     //RESOURCE SERVER            
            sendSyncResourceRequest(batchProxy);    //RESOURCE SERVER
            if (DaoManager.getInstance().getServerDrivenParamsDao().isI18NSyncEnabled())//nonblocker
            {
                SyncResExecutor.getInstance().syncI18nResource(ResourceManager.getInstance().getCurrentLocale(), null);
            }
        }
        if(batchProxy != null)
        {
            batchProxy.send(ServerProxyConfig.defaultTimeout * 3);
        }
    }
    
    protected void checkInventoryBeforeRequest()
    {
        // because we remove the default value in the .property file , we need to get the inventory string in preload
        // job.
        // here is the protect which will avoid sending the audio fetching request , but the inventroy string is not
        // ready.
        // in normal case , isInventoryLoadSuccessed should be true here
        long currentTime = System.currentTimeMillis();
        ResourceBarDao resourceBarDao =  DaoManager.getInstance().getResourceBarDao();
        if (resourceBarDao != null)
        {
            while (!resourceBarDao.getInventoryLoadSuccessed() && System.currentTimeMillis() - currentTime < INVENTORY_LOAD_TIME_OUT)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    Logger.log(SyncResModel.class.getName(), e);
                }
            }
        }
    }
    
    protected void sendSyncPurchaseRequest(IBatchProxy batchProxy)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance().createSyncPurchaseProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);

        if (batchProxy == null)
        {
            syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
        }
        else
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_PURCHASE, (AbstractServerProxy)syncPurchaseProxy);
            batchProxy.addBatchItem(requestItem);
        }
    }
    
    protected void sendSyncResourceRequest(IBatchProxy batchProxy)
    {
        isSendCombinationRes = false;
        
        Vector combination = new Vector();
        
        String region = getRegion();
        String version = DaoManager.getInstance().getServerDrivenParamsDao().getVersion(region);
        if (version == null || version.trim().length() == 0)
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_SERVER_DRIVEN_PARAMETERS));
        }
        
        version = null;
        if (DaoManager.getInstance().getDsrServerDrivenParamsDao().getServerParams() != null)
        {
            version = DaoManager.getInstance().getDsrServerDrivenParamsDao().getServerParams().get(ServerDrivenParamsDao.SERVER_DRIVEN_VERSION);
        }
        if (version == null || version.trim().length() == 0)
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_DSR_SDP_NODE));
        }
        
        version = DaoManager.getInstance().getResourceBarDao().getCategoryVersion(region);
        if (version == null || version.trim().length() == 0)
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_CATEGORY_TREE));
        }
        
        version = DaoManager.getInstance().getResourceBarDao().getAirportVersion();
        if (version == null || version.trim().length() == 0)
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AIRPORT));
        }
        
        version = DaoManager.getInstance().getResourceBarDao().getBrandNameVersion(region);
        if (version == null || version.trim().length() == 0) 
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_POI_BRAND_NAME));
        }
        
        version = DaoManager.getInstance().getResourceBarDao().getHotPoiVersion(region);
        if (version == null || version.trim().length() == 0) 
        {        
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_HOT_POI_CATEGORY));
        }
        
        if (DaoManager.getInstance().getResourceBarDao().isAudioRuleEmpty())
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AUDIO_AND_RULE));
        }
        
        version = DaoManager.getInstance().getResourceBarDao().getPreferenceSettingVersion(region);
        if (version == null || version.trim().length() == 0) 
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING));
        }
        
        version = DaoManager.getInstance().getResourceBarDao().getLocalEventsVersion(region);
        if (version == null || version.trim().length() == 0) 
        {
            combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_GOBY_EVENT));
        }
        
        if(combination.size() == 0)
        {
            Logger.log(Logger.INFO, getClass().toString(), "DB-Test --> Resource preloaded, Skip combination res. ");
            return;
        }

        DaoManager.getInstance().getResourceBarDao().setIsResourceSyncFinish(false);
        UserProfileProvider userProfileProvider = getUserProfile();
        ISyncCombinationResProxy combinationResProxy = ServerProxyFactory.getInstance().createSyncCombinationResProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        isSendCombinationRes = true;
        
        if (batchProxy == null)
        {
            combinationResProxy.syncCombinationRes(combination, false);
        }
        else
        {
            combinationResProxy.cleanupSyncResourceRequest();
            
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE, (AbstractServerProxy)combinationResProxy);
            requestItem.params = new Vector();
            requestItem.params.addElement(combination);
            batchProxy.addBatchItem(requestItem);
        }
    }

    private UserProfileProvider getUserProfile()
    {
        UserProfileProvider userProfileProvider = new UserProfileProvider();
        return userProfileProvider;
    }
    
    private void sendFastStartupRequest(IBatchProxy batchProxy)
    {
        if (!DaoManager.getInstance().getBillingAccountDao().isLogin())
            return;
        
        sendSyncFlagsRequest(batchProxy); // RESOURCE SERVER
    }
    
    private void sendSyncFlagsRequest(IBatchProxy batchProxy)
    {
        IStartupProxy startupProxy = ServerProxyFactory.getInstance().createStartupProxy(null, CommManager.getInstance().getComm(), this);
        
        long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(false);
        if (batchProxy == null)
        {
            startupProxy.synchronizeStartUp(lastSyncTime);
        }
        else
        {
            RequestItem startupRequestItem = new RequestItem(
                IServerProxyConstants.ACT_STARTUP, (AbstractServerProxy)startupProxy);
            startupRequestItem.params = new Vector();
            startupRequestItem.params.addElement(PrimitiveTypeCache.valueOf(lastSyncTime));
            batchProxy.addBatchItem(startupRequestItem);
        }
    }
    
    protected void sendFetchAllStopsRequest(IBatchProxy batchProxy, boolean needParseInThread, IUserProfileProvider userProfileProvider)
    {
        if (batchProxy == null)
        {
            IAddressProxy proxy = ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider, false);
            proxy.fetchAllStops(needParseInThread);
        }
        else
        {
            RequestItem stopsRequestItem = new RequestItem(
                IServerProxyConstants.ACT_FETCH_ALL_STOPS, (AbstractServerProxy)ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider, false));
            
            stopsRequestItem.params = new Vector();
            stopsRequestItem.params.addElement(PrimitiveTypeCache.valueOf(needParseInThread));
            batchProxy.addBatchItem(stopsRequestItem);
        }
    }
    
    protected void sendDownloadCsrIDRequest(IBatchProxy batchProxy)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        if (batchProxy == null)
        {
            SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_DOWNLOAD, this, userProfileProvider, -1);
        }
        else
        {
            RequestItem downloadPreference = new RequestItem(
                IServerProxyConstants.ACT_SYNC_PREFERENCE, (AbstractServerProxy)ServerProxyFactory.getInstance().createToolsProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider));
            downloadPreference.params = new Vector();
            downloadPreference.params.addElement(PrimitiveTypeCache.valueOf(IToolsProxy.SYNC_TYPE_DOWNLOAD));
            downloadPreference.params.addElement(new Hashtable());
            downloadPreference.params.addElement(PrimitiveTypeCache.valueOf(-1));
            batchProxy.addBatchItem(downloadPreference);
        }
    }
    
    protected void sendCachedCities(IBatchProxy batchProxy)
    {
        TnLocation currentLocation = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if(currentLocation==null)
        {
            return;
        }
        UserProfileProvider userProfileProvider = getUserProfile();

        if (batchProxy == null)
        {
            IAddressProxy proxy = ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider, false);
            proxy.updateCities(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        else
        {
            RequestItem citiesRequestItem = new RequestItem(
                IServerProxyConstants.ACT_CACHE_CITIES, (AbstractServerProxy)ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider, false));
            citiesRequestItem.params = new Vector();
            citiesRequestItem.params.addElement(currentLocation.getLatitude());
            citiesRequestItem.params.addElement(currentLocation.getLongitude());
            batchProxy.addBatchItem(citiesRequestItem);
        }
    }
    
    protected void syncSettingData(IBatchProxy batchProxy)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        SyncResExecutor.getInstance().syncSettingData(null, userProfileProvider, null, ISettingDataProxy.SYNC_TYPE_FORCE_DOWNLOAD);
    }
    
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "------syncRes----updateTransactionStatus() proxy = " + proxy + ", progress = " + progress);
        if(ICommCallback.PROGRESS_CANCEL == progress)
        {
            transactionError(proxy);
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {    	
        Logger.log(Logger.INFO, this.getClass().getName(), "------syncRes----transactionError() proxy = " + proxy);
        
        put(KEY_B_SYNCRES_OCCUR_ERROR, true);
        processTransactionError(proxy);        
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {    	
        Logger.log(Logger.INFO, this.getClass().getName(), "------syncRes----networkError() proxy = " + proxy);
        
        put(KEY_B_SYNCRES_OCCUR_ERROR, true);
        processTransaction(proxy, TYPE_NETWORK_ERROR);
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {    	
        Logger.log(Logger.INFO, this.getClass().getName(), "------syncRes----transactionFinishedDelegate() proxy = " + proxy);
        
        put(KEY_B_SYNCRES_OCCUR_ERROR, false);
        processTransactionFinished(proxy);
    }
    
    public void processTransactionError(AbstractServerProxy proxy)
    {
        if(proxy instanceof ISyncPurchaseProxy)
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                handleAccountFatalError();
            }
        }
        else if(proxy instanceof JsonDimProxy)
        {
            //no need handle this error, won't block user's action. Go ahead.
            checkSyncStep(proxy);
            return;
        }
        
        //error happen ,cacel the current requestJob ,this is Especially for rim 
        //fix bug 14900
        proxy.cancel();
        
        String errMsg = proxy.getErrorMsg();
        
        this.put(KEY_S_ERROR_MESSAGE, errMsg);       

        super.transactionError(proxy);
    }
    
    public void processTransactionFinished(AbstractServerProxy serverProxy)
    {
        AbstractServerProxy proxy = serverProxy;
        
        if (proxy instanceof IStartupProxy)
        {
            DaoManager.getInstance().getStartupDao().store();
        }
        else if(proxy instanceof ISyncPurchaseProxy)
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                handleAccountFatalError();
            }
        }
        else if (proxy instanceof IAddressProxy)
        {
            if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_FETCH_ALL_STOPS))
            {
                DaoManager.getInstance().getAddressDao().sortRecentAddresses();
                int accountEntryType = DaoManager.getInstance().getBillingAccountDao().getLoginType();
                if(accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
                {
                    MigrationExecutor.getInstance().setAddedStatus();
                    SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_UPLOAD, null, null, IToolsProxy.UPLOAD_TYPE_HOME_WORK);
                }
            }
        }
        else if (proxy instanceof ISyncCombinationResProxy)
        {
            updateMapDataUpgradeInfo();
        }
        else if (proxy instanceof IToolsProxy)
        {
            saveCsrId((IToolsProxy) proxy);
        }
        else if (proxy instanceof IBatchProxy)
        {
            //qli: Save user profile in PreferenceDao.
            IBatchProxy batchProxy = (IBatchProxy)proxy;
            Vector batchItems = batchProxy.getBatchItems();
            for( int i=0 ; i<batchItems.size() ; i++ )
            {
                Vector batchRequest = (Vector)batchItems.elementAt(i);
                for( int j=0 ; j< batchRequest.size() ; j++ )
                {
                    RequestItem batchItem = (RequestItem) batchRequest.elementAt(j);
                    AbstractServerProxy itemProxy = batchItem.serverProxy;
                    if(itemProxy instanceof ISyncCombinationResProxy)
                    {
                        updateMapDataUpgradeInfo();
                    }
                    if (itemProxy instanceof IStartupProxy)
                    {
                        String dataSetProvider = DaoManager.getInstance()
                                .getSimpleConfigDao()
                                .getString(SimpleConfigDao.KEY_SWITCHED_DATASET);
                        String oldDataProvider = AbstractDaoManager.getInstance()
                                .getStartupDao().getMapDataset();
                        if ("".equalsIgnoreCase(oldDataProvider))
                        {
                            AbstractDaoManager.getInstance().getStartupDao().setMapDataset(dataSetProvider);
                            DaoManager.getInstance().getSimpleConfigDao().remove(SimpleConfigDao.KEY_SWITCHED_DATASET);
                            AbstractDaoManager.getInstance().getStartupDao().store();
                            DaoManager.getInstance().getSimpleConfigDao().store();
                        }
                        if (isMapCopyrightChanged())
                        {
                            setMapCopyright();
                        }
                    }
                    else if (itemProxy instanceof IToolsProxy)
                    {
                        saveCsrId((IToolsProxy)itemProxy);
                    }
                }
            }
        }
        
        checkSyncStep(proxy);
    }
    
    private void updateMapDataUpgradeInfo()
    {
        if (DaoManager.getInstance().getResourceBarDao().getMapDataUpgradeInfoVersion() != null
                && DaoManager.getInstance().getResourceBarDao().getMapDataUpgradeInfoVersion().trim().length() > 0)
        {
            Vector mapDataUpgradeInfos = DaoManager.getInstance().getResourceBarDao().getMapDataUpgradeInfo();
            
            int size = mapDataUpgradeInfos.size();
            if (size > 0)
            {
                String[] actions = new String[size];
                String[] urls = new String[size];
                boolean isMapDownloadCNEnabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
                    SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_ENABLE);
                String cnURL = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(
                    SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_URL);
                for (int i = 0; i < size; i++)
                {
                    actions[i] = ((MapDataUpgradeInfo) mapDataUpgradeInfos.elementAt(i)).getRegion();
                    urls[i] = ((MapDataUpgradeInfo) mapDataUpgradeInfos.elementAt(i)).getUrl();
                    
                    if (isMapDownloadCNEnabled && cnURL.trim().length() > 0)
                    {
                        urls[i] = NavsdkUserManagementService.getCnURL(urls[i]);
                    }
                }
                NavsdkUserManagementService.getInstance().serviceLocatorUpdate(actions, urls);
            }
        }
    }
    
    private void saveCsrId(IToolsProxy toolsProxy)
    {
        if(toolsProxy == null)
            return;
        
        int syncType = toolsProxy.getPreferenceSyncType();
        if(syncType == IToolsProxy.SYNC_TYPE_DOWNLOAD)
        {
            Hashtable prefers = ((IToolsProxy)toolsProxy).getUserPrefers();
            if(prefers != null)
            {
                int loginType = DaoManager.getInstance().getBillingAccountDao().getLoginType();
                if(loginType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
                {
                    String csrId= (String)prefers.get(IToolsProxy.KEY_CSRID);
                    PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
                    prefDao.setStrValue(Preference.ID_PREFERENCE_CSRID, csrId);
                    prefDao.store(getRegion());
                }
            }
        }
    }
    
    private boolean checkSyncStep(AbstractServerProxy serverProxy)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "------syncRes----checkSyncStep() currentStep = " + currentStep);
        
        if(currentStep == SEND_LOGIN_REQUEST_STEP_1)
        {
            currentStep = SEND_LOGIN_REQUEST_STEP_2;
            this.put(KEY_I_LOGIN_STEP, currentStep);
            this.postModelEvent(EVENT_MODEL_DO_FRESH_SYNC);
            return true;
        }
        else if(currentStep == SEND_LOGIN_REQUEST_STEP_2)
        {
            if (isSendCombinationRes)
            {
                if(serverProxy.getRequestAction().equals(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE))
                {
                    int step = 0;
                    if(serverProxy instanceof ISyncCombinationResProxy)
                    {
                        ISyncCombinationResProxy syncResourceProxy = (ISyncCombinationResProxy)serverProxy;
                        step = syncResourceProxy.getStep();
                    }
                    
                    if(serverProxy instanceof IBatchProxy)
                    {
                        IBatchProxy batchProxy = (IBatchProxy)serverProxy;
                        if(batchProxy.getBatchItems() != null)
                        {
                            int size = batchProxy.getBatchItems().size();
                            for(int i = 0 ; i < size ; i ++)
                            {
                                Vector batchRequests = (Vector)batchProxy.getBatchItems().elementAt(i);
                                
                                int requestSize = batchRequests.size();
                                boolean isFound = false;
                                for(int m = 0; m < requestSize ; m ++)
                                {
                                    RequestItem requestItem = (RequestItem)batchRequests.elementAt(m);
                                    if(requestItem.serverProxy instanceof ISyncCombinationResProxy)
                                    {
                                        ISyncCombinationResProxy syncResourceProxy = (ISyncCombinationResProxy)requestItem.serverProxy;
                                        step = syncResourceProxy.getStep();
                                        isFound = true;
                                        break;
                                    }
                                }
                                
                                if(isFound)
                                    break;
                            }
                        }
                    }
                    if(step == ISyncCombinationResProxy.STEP_SYNC_FINISH)
                    {
                        syncFinished();
                        return true;
                    }
                }
            }
            else
            {
                if(serverProxy.getRequestAction().equals(IServerProxyConstants.ACT_STARTUP))
                {
                    syncFinished();
                    return true;
                }
            }
        }
        return false;
    }

    private void syncFinished()
    {
        isFreshSyncFinish = true;
        Logger.log(Logger.INFO, getClass().toString(), "DB-Test --> sync model duration : " + ( System.currentTimeMillis() - timeStamp ));
       
        //Fix TNANDROID-6251, settingData returns before clientSetting, then the real value from Cserver is replaced by default value by mistake.       
        syncSettingData(null);//nonblocker
        
        if(MigrationExecutor.getInstance().isSyncEnabled())
        {
            DaoManager.getInstance().getAddressDao().setIsMigrationSucc(true);
            DaoManager.getInstance().getAddressDao().setIsMigrationInProgress(false);
            DaoManager.getInstance().getAddressDao().store();
        }
        
        saveRegion();                   
        
        DaoManager.getInstance().getResourceBarDao().setIsResourceSyncFinish(true);
        DaoManager.getInstance().getResourceBarDao().store();
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.finishSyncRes();
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).setCommonBaseLineAnchor(ResourceManager.getInstance()
            .getCurrentBundle().getString(IStringCommon.RES_STANDARD_BASELINE_ANCHOR, IStringCommon.FAMILY_COMMON));
        
        this.postModelEvent(EVENT_MODEL_SYNC_FINISH);
    }
    
    private void saveRegion()
    {
        Vector<String> regions =  DaoManager.getInstance().getSimpleConfigDao().getVector(SimpleConfigDao.KEY_CACHED_REGION);
        regions.addElement(RegionUtil.getInstance().getCurrentRegion());
        DaoManager.getInstance().getSimpleConfigDao().putVector(SimpleConfigDao.KEY_CACHED_REGION, regions);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    private void processTransaction(AbstractServerProxy serverProxy, int transactionType)
    {
        AbstractServerProxy proxy = serverProxy;
        IBatchProxy batchProxy = null;
        if (proxy instanceof IBatchProxy)
        {
            batchProxy = (IBatchProxy) proxy;

            int size = batchProxy.getBatchItems().size();
            for(int m = 0 ; m < size ; m ++)
            {
                if (batchProxy.getBatchItems().elementAt(m) instanceof Vector)
                {
                    Vector requests = (Vector) batchProxy.getBatchItems().elementAt(m);
                    for (int i = 0; i < requests.size(); i++)
                    {
                        RequestItem requestItem = (RequestItem) requests.elementAt(i);
                        
                        if (transactionType == TYPE_TRANS_FINISH)
                            processTransactionFinished(requestItem.serverProxy);
                        else if (transactionType == TYPE_TRANS_ERROR)
                            processTransactionError(requestItem.serverProxy);
                        else if (transactionType == TYPE_NETWORK_ERROR)
                            processNetworkError(requestItem.serverProxy, 0);
                    }
                }
            }
        }
        else
        {
            if (transactionType == TYPE_TRANS_FINISH)
            {
                processTransactionFinished(serverProxy);
            }
            else if (transactionType == TYPE_TRANS_ERROR)
            {
                processTransactionError(serverProxy);
            }
            else if (transactionType == TYPE_NETWORK_ERROR)
            {
                processNetworkError(serverProxy, 0);
            }
        }
    }
    
    public void processNetworkError(AbstractServerProxy proxy, int statusCode)
    {
        if(isFreshSyncFinish)
            return;
        
        if(proxy instanceof JsonDimProxy)
        {
            //no need handle this error, won't block user's action. Go ahead.
            checkSyncStep(proxy);
            return;
        }
        
        int type = this.getInt(KEY_I_SYNC_TYPE);
        if (type == TYPE_FRESH_SYNC)
        {
            super.networkError(proxy, (byte)statusCode, null);//fix bug 51411.
        }
    }

    /**
     * Activate current controller.<br>
     * 
     * For example,resume daemon back end job.<br>
     * 
     * @param isUpdateView
     */
    protected void activateDelegate(boolean isUpdateView)
    {
        MapContainer.getInstance().resume();
    }

    /**
     * Deactivate current model.<br>
     * 
     * For example, pause unnecessary daemon back end job.<br>
     * 
     */
    protected void deactivateDelegate()
    {
        MapContainer.getInstance().pause();
    }
}

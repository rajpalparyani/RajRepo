/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EntryModel.java
 *
 */
package com.telenav.module.entry;

import java.util.Vector;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import com.crashlytics.android.Crashlytics;
import com.telenav.app.CommManager;
import com.telenav.app.INativeAppCallBack;
import com.telenav.app.NavServiceManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.ThreadManager;
import com.telenav.app.android.scout_us.ICrashlyticsConstants;
import com.telenav.data.dao.misc.AndroidBillingDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressBackupDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.dao.serverproxy.StartupDao;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.data.serverproxy.impl.IClientLoggingProxy;
import com.telenav.data.serverproxy.impl.ILoginProxy;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.IStartupProxy;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.htmlsdk.IHtmlSdkServiceListener;
import com.telenav.htmlsdk.ResultGenerator;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.log.mis.GpsStatusLogger;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.RadioStatusLogger;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.log.mis.log.FirstTimeLoginMisLog;
import com.telenav.log.mis.log.HomeScreenTimeMisLog;
import com.telenav.log.mis.log.OnboardStartupInfoMisLog;
import com.telenav.log.mis.log.SessionStartupMisLog;
import com.telenav.log.mis.log.StartupInfoMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.module.marketbilling.MarketBillingHandler;
import com.telenav.module.region.RegionDetectResultHandler;
import com.telenav.module.region.RegionDetector;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractController;
import com.telenav.navservice.NavServiceParameter;
import com.telenav.radio.TnRadioManager;
import com.telenav.res.IStringEntry;
import com.telenav.res.IStringLogin;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.plugin.PluginManager;
import com.telenav.telephony.TnBatteryManager;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.threadpool.ThreadPool;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */
class EntryModel extends AbstractCommonEntryModel implements IEntryConstants, LocationListener,INativeAppCallBack
{
    protected SplashScreenJob splashScreenJob;
    protected TnDeviceInfo tnDeviceInfo = null;
    
    protected int SECRET_KEY_EXEC_TIME = 2000;
    protected int SECRET_KEY_EXEC_COUNT = 3;
    
    protected void doActionDelegate(int actionId)
    {
        super.doActionDelegate(actionId);
        switch (actionId)
        {
            case ACTION_INIT:
            {
                int timeout = ((DaoManager)DaoManager.getInstance()).getServerDrivenParamsDao().getIntValue(ServerDrivenParamsDao.SPLASH_SCREEN_TIMEOUT);
                if(timeout <= 0)
                {
                    timeout = DEFAULT_SPLASH_TIMEOUT;
                }
                
                this.splashScreenJob = new SplashScreenJob(this, timeout);
                
                ThreadPool threadPool = ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION);
                threadPool.addJob(this.splashScreenJob);
                
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_GLOBAL, KontagentLogger.GLOBAL_LOADING_SCREEN_DISPLAYED);
                break;
            }
            case ACTION_EXIT_APP:
            {
                TeleNavDelegate.getInstance().exitApp();
                break;
            }
            case ACTION_CHECK_ACCOUNT_STATUS:
            {
                if(DaoManager.getInstance().getBillingAccountDao().isLogin())
                {
                    postModelEvent(EVENT_MODEL_SYNC_RES_AND_CHECK_UPGRADE);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_LOGIN_FAIL);
                }
                break;
            }
            case ACTION_BG_STARTUP:
            {
                Thread thread = new Thread(new Runnable()
                {
                    public void run()
                    {
                        synchronized (this)
                        {
                            try
                            {
                                this.wait(5000);
                            }
                            catch (Exception e)
                            {
                            }
                        }
                        
                        backgroundStartUp();
                        syncResource();
                        startMisLog();
                        downloadPreference();
                        syncSettingData();
                        backgroundLogin();
                        updateNearCity((IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER));
                        checkMigration();
                        parseAirport();
                    }
                });
                thread.start();
                
                break;
            }
            case ACTION_GOTO_OTA:
            {
                goToOta();
                break;
            }
            case ACTION_CHECK_UPGRADE:
            {
                boolean isFreshLogin = this.getInt(KEY_I_SYNC_TYPE) == TYPE_FRESH_SYNC;
                if (!isFreshLogin)
                {
                    Thread thread = new Thread(new Runnable()
                    {
                        public void run()
                        {
                            synchronizeStartUp();
                        }
                    });
                    thread.start();
                }
                checkUpgrade();
                break;
            }
            case ACTION_DONOT_ASK_FOR_UPGRADE:
            {
                //set flag.
                setIngnoreVersion();
                
                backgroundStartUp();
                startMisLog();
                break;
            }
            case ACTION_LOCATION_SETTING_OK:
            {
                this.put(KEY_B_IS_DISABLE_LOCATION_SETTING, true);

                try
                {
                    TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.LAUNCH_LOCATION_SETTING, null,this);
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
       
                break;
            }
            case ACTION_START_FLOW:
            {
                this.put(KEY_B_IS_DISABLE_LOCATION_SETTING, true);
                startFlow();
                break;
            }
            case ACTION_GO_TO_PURCHASE_NAV:
            {
                this.postModelEvent(EVENT_MODEL_GOTO_PURCHASE_NAV);
                break;
            }
            case ACTION_SYNC_RES_AND_CHECK_UPGRADE:
            {
                boolean isSyncFinish = DaoManager.getInstance().getResourceBarDao()
                        .isResourceSyncFinish();

                if (isSyncFinish)
                {
                    if (isMapDatasetChanged())
                    {
                        this.postModelEvent(EVENT_MODEL_DATASET_SWITCH_RESYNC);
                    }
                    else
                    {
                        this.postModelEvent(EVENT_MODEL_CHECK_UPGRADE);
                    }
                }
                else
                {
                    this.put(KEY_I_SYNC_TYPE, TYPE_FRESH_SYNC);
                    postModelEvent(EVENT_MODEL_SYNC_RES);
                    //Because extra page is removed from Titan Scope.
                    //preloadExtrasWebPage();
                }
                break;
            }
            case ACTION_UPLOAD_LOGIN_INFO:
            {
                try
                {
                    sendLoginInfo();
                }
                catch (Exception e)
                {
                    
                }
                postModelEvent(EVENT_MODEL_CHECK_UPGRADE);
                break;
            }
            case ACTION_DATASET_SWITCH_RESYNC:
            {
                this.put(KEY_B_DATASET_RESYNC, true);
                updateMapDatasetRes();
                break;
            }
        }
    }
    
    protected void updateNearCity( IUserProfileProvider userProfileProvider)
    {
        TnLocation currentLocation = LocationProvider.getInstance().getReliableCurrentLocation();
        
        if (currentLocation == null)
        {
            return;
        }
        
        Vector cityList = DaoManager.getInstance().getNearCitiesDao().getNearCities(this.getRegion());
        
        boolean isCityListEmpty = (cityList == null || cityList.size() == 0);
        boolean isCityFar = false;
        
        if (!isCityListEmpty)
        {
            isCityFar = DaoManager.getInstance().getNearCitiesDao()
                    .isCityFar(currentLocation.getLatitude(), currentLocation.getLongitude(), this.getRegion());
        }
        
        if (isCityListEmpty || isCityFar)
        {
            IAddressProxy proxy = ServerProxyFactory.getInstance().createAddressProxy(null,
                CommManager.getInstance().getComm(), this, userProfileProvider, false);
            if (currentLocation != null)
            {
                proxy.updateCities(currentLocation.getLatitude(), currentLocation.getLongitude());
            }
        }
    }
    
    protected void parseAirport()
    {
        if (!DaoManager.getInstance().getAddressDao().isAirPortListInitialized())
        {
            DaoManager.getInstance().getAddressDao()
                    .parseAirports(DaoManager.getInstance().getResourceBarDao().getAirportNode());
            Logger.log(Logger.INFO, this.getClass().getName(), "==Preload Airport==    done!!!");
        }
    } 
    
    protected void sendLoginInfo()
    {
        String devicePtn = TnTelephonyManager.getInstance().getPhoneNumber();
        String operatorName = TnTelephonyManager.getInstance().getSimCardInfo().operatorName;
        String deviceCarrier = null;
        if (operatorName == null || operatorName.length() == 0)
        {
            deviceCarrier = TnTelephonyManager.getInstance().getSimCardInfo().operator;
        }
        else
        {
            deviceCarrier = operatorName;
        }
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
        IClientLoggingProxy syncPurchaseProxy = ServerProxyFactory.getInstance().createClientLoggingProxy(null,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        syncPurchaseProxy.sendLoginInfo(devicePtn, deviceCarrier);
    }
    
    protected boolean isMapDatasetChanged()
    {
        boolean isDatasetChanged = false;
        String prevDataset = AbstractDaoManager.getInstance().getStartupDao()
                .getMapDataset();

        String latestDataset = DaoManager.getInstance().getSimpleConfigDao()
                .getString(SimpleConfigDao.KEY_SWITCHED_DATASET);

        if (prevDataset != null && latestDataset != null
                && !latestDataset.equalsIgnoreCase(prevDataset))
        {
            isDatasetChanged = true;
        }
        
        return isDatasetChanged;
    }
    
    protected void backgroundLogin()
    {
        ILoginProxy loginProxy = ServerProxyFactory.getInstance().createLoginProxy(null,
            CommManager.getInstance().getComm(), this, null);

        loginProxy.sendLogin(true, "");
    }
    

    private void checkMigration()
    {
        if(MigrationExecutor.getInstance().isSyncEnabled())
        {
            MigrationExecutor.getInstance().doMigration(null);
        }
    }
    
    protected void syncSettingData()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
       
        int value = getLauguageIndexByLocale();
        Preference pref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(Preference.ID_PREFERENCE_LANGUAGE);
        
        if (pref !=null &&pref.getIntValue() != value)
        {
            ((DaoManager) DaoManager.getInstance()).getPreferenceDao().setIntValue(
                Preference.ID_PREFERENCE_LANGUAGE, value);
            SyncResExecutor.getInstance().syncSettingData(null, userProfileProvider, this, ISettingDataProxy.SYNC_TYPE_UPLOAD);
        }
        else
            SyncResExecutor.getInstance().syncSettingData(null, userProfileProvider, this, ISettingDataProxy.SYNC_TYPE_DOWNLOAD);
        
    }

    public int getLauguageIndexByLocale()
    {
        int value = 0;
        Preference pref = ((DaoManager) DaoManager.getInstance()).getPreferenceDao()
                .getPreference(Preference.ID_PREFERENCE_LANGUAGE);
        if (pref != null)
        {
            String[] lauguages = pref.getOptionNames();
            int size = lauguages.length;
            String locale = ResourceManager.getInstance().getCurrentLocale();
            for (int index = 0; index < size; index++)
            {
                if (lauguages[index].lastIndexOf(locale) > 0)
                {
                    value = index;
                    break;
                }
            }
            value = pref.getOptionValues()[value];
        }
        return value;
    }
    
    protected void syncResource()
    {
        Vector combination = new Vector();
        combination.addElement(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING);
        combination.addElement(ISyncCombinationResProxy.TYPE_SERVER_DRIVEN_PARAMETERS);

        //always use preload res.
        //combination.addElement(ISyncCombinationResProxy.TYPE_AUDIO_AND_RULE);
        
        Logger.log(Logger.INFO, this.getClass().getName(), "==EntryModel Airport==    check!!!");
        if("".equals(DaoManager.getInstance().getResourceBarDao().getAirportVersion()) ||
                DaoManager.getInstance().getAddressDao().getAirportsName().size() == 0)
        {
            combination.addElement(ISyncCombinationResProxy.TYPE_AIRPORT);
            Logger.log(Logger.INFO, this.getClass().getName(), "==EntryModel Airport==    Need Sync!!!");
        }
        
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        SyncResExecutor.getInstance().syncCombinationResource(combination, this, userProfileProvider);
    }
    
    protected void downloadPreference()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_DOWNLOAD, this, userProfileProvider, -1);
    }
    
    protected void saveCurrentVersion()
    {
        DaoManager.getInstance().getStartupDao().setCurrentAppVersion(AppConfigHelper.mandatoryClientVersion);
        DaoManager.getInstance().getStartupDao().setBuildNumber(AppConfigHelper.buildNumber);
        DaoManager.getInstance().getStartupDao().store();
    }
    
    protected void goToOta()
    {
        StartupDao startupDao = DaoManager.getInstance().getStartupDao();
        if(startupDao != null)
        {
            String url = startupDao.getUpgradeUrl();
            if(url != null)
            {
                TnTelephonyManager.getInstance().startBrowser(url);
            }
        }
    }

    protected void setIngnoreVersion()
    {
        StartupDao startupDao = DaoManager.getInstance().getStartupDao();
        if(startupDao != null)
        {
            String newVersion = startupDao.getUpgradeAppVersion();
            if(newVersion != null && newVersion.trim().length() > 0)
            {
                startupDao.setIngnoreAppVersion(newVersion);
                startupDao.store();
            }
        }
    }

    protected void checkUpgrade()
    {
        StartupDao startupDao = DaoManager.getInstance().getStartupDao();
        if(startupDao == null)
        {
            postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
            return;
        }

        String currentVersion = startupDao.getCurrentAppVersion();
        String upgradedVersion = startupDao.getUpgradeAppVersion();
        
        boolean isVersionUpgraded = isNeedUpgrade(currentVersion, upgradedVersion);
        String updagradeType = startupDao.getUpgradeType();
        boolean isForceUpgrade = isForceUpgrade(updagradeType);
        boolean isUpdagradeTypeNull = updagradeType == null || updagradeType.trim().length() <= 0;
        
        boolean isIngnoreVersion = false;
        if(!isForceUpgrade)
        {
            String ingnoreVersion = startupDao.getIngnoreVersion();
            if(ingnoreVersion != null && upgradedVersion != null && ingnoreVersion.trim().equalsIgnoreCase(upgradedVersion.trim()))
            {
                isIngnoreVersion = true;
            }
        }
        
        if(isVersionUpgraded && !isIngnoreVersion && !isUpdagradeTypeNull)
        {
            this.put(KEY_B_IS_NEED_UPGRADE, true);
            this.put(KEY_B_IS_FORCE_UPGRADE, isForceUpgrade);
            this.put(KEY_O_IS_UPGRADE_LISTENER, new BrowserEventListener());
            postModelEvent(EVENT_MODEL_GOTO_UPGRADE);
        }
        else
        {
            postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
        }
    }

    protected boolean isForceUpgrade(String updagradeType)
    {
        if(updagradeType == null || updagradeType.trim().length() <= 0)
            return false;
        
        if(updagradeType.trim().equalsIgnoreCase(StartupDao.UPGRADE_TYPE_FORCE))
            return true;
        
        return false;
    }

    protected boolean isNeedUpgrade(String currentVersion, String newVersion)
    {
        StartupDao startupDao = DaoManager.getInstance().getStartupDao();
        String currentBuildNumber = startupDao.getBuildNumber();
        String minOSVersion = startupDao.getMinOSVersion();
        String maxOSVersion = startupDao.getMaxOSVersion();
        String currentOSVersion = TnTelephonyManager.getInstance().getDeviceInfo().platformVersion;
        
        int currentOS = Integer.MIN_VALUE;
        int minOS = Integer.MIN_VALUE;
        int maxOS = Integer.MAX_VALUE;
        if(currentOSVersion != null && currentOSVersion.length() > 0)
        {
            try
            {
                currentOS = Integer.parseInt(currentOSVersion.trim());
            }
            catch(Exception e)
            {
                
            }
        }
        if(minOSVersion != null && minOSVersion.length() > 0)
        {
            try
            {
                minOS = Integer.parseInt(minOSVersion.trim());              
            }
            catch (Exception e)
            {
                
            }
        }
        if(maxOSVersion != null && maxOSVersion.length() > 0)
        {
            try
            {               
                maxOS = Integer.parseInt(maxOSVersion.trim());
            }
            catch (Exception e)
            {
                
            }
        }
        
        if(currentOS >= minOS && currentOS <= maxOS)
        {
            int compareResult = compareVersion(currentVersion, newVersion);
            if(compareResult >0)
            {
                return true;
            }
            else if (compareResult == 0)
            {
                String upgradeBuildNumber = startupDao.getUpgradeBuildNumber();
                if (upgradeBuildNumber != null && upgradeBuildNumber.length() > 0)
                {
                    int intCurrentBuildNumber = 0;
                    int intUpgradeBuildNumber = 0;
                    try
                    {
                        intCurrentBuildNumber = Integer.parseInt(currentBuildNumber.trim());
                        intUpgradeBuildNumber = Integer.parseInt(upgradeBuildNumber.trim());
                    } catch (Exception e)
                    {

                    }
                    return intCurrentBuildNumber < intUpgradeBuildNumber ? true : false;
                }
            }
        }  
        return false;       
    }
    
    /**
     * Compare current & upgrade version
     * 
     * @param currentVersion the running version
     * @param upgradeVersion the version c-server prompt to upgrade to
     * @return 0 --> equal version
     * <br /> 1 --> current < upgrade. need do upgrade
     * <br /> -1 --> current > upgrade. no need to upgrade
     */
    protected int compareVersion(String currentVersion, String upgradeVersion) 
    {
        if( upgradeVersion == null || upgradeVersion.trim().length() <= 0 )
            return -1;
            
        if(currentVersion == null || currentVersion.trim().length() <= 0)
            return 1;
        
        String[] currentNums = currentVersion.split("\\.");
        String[] newNums = upgradeVersion.split("\\.");
        
        if(currentNums.length < newNums.length) 
        {
            for(int i = 0; i < currentNums.length; i ++) 
            {
                int flag = compareEach(currentNums[i], newNums[i]);
                if(flag == 1)
                {
                    return 1;
                }
                else if(flag == -1)
                {
                    return -1;
                }
            }
            for(int i = currentNums.length; i < newNums.length; i ++) 
            {
                int newNum = Integer.parseInt(newNums[i]);
                if(newNum != 0)
                    return 1;
            }
            return -1;
        } 
        else if(currentNums.length > newNums.length)
        {
            for(int i = 0; i < newNums.length; i ++) 
            {
                int flag = compareEach(currentNums[i], newNums[i]);
                if(flag == 1)
                {
                    return 1;
                }
                else if(flag == -1)
                {
                    return -1;
                }
            }
            return -1;
        } 
        else 
        {
            //When the length of currentNums and newNums is the same.
            int length = newNums.length;
            for(int i = 0; i < length - 1; i ++) 
            {
                int flag = compareEach(currentNums[i], newNums[i]);
                if(flag == 1)
                {
                    return 1;
                }
                else if(flag == -1)
                {
                    return -1;
                }
            }
            //Compare the last one directly without adding zeros
            //This is for the case: 7.0.1 = 7.0.01
            int currentNum = Integer.parseInt(currentNums[length - 1]);
            int newNum = Integer.parseInt(newNums[length - 1]);
            if(newNum > currentNum)
            {
                return 1;
            }
            else if(newNum < currentNum)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }
    
    private int compareEach(String currentNum, String newNum)
    {
        int currentNum_length = currentNum.length();
        int newNum_length = newNum.length();
        char[] currentNums = currentNum.toCharArray();
        char[] newNums = newNum.toCharArray();
        //If the length of currentNum and newNum is not the same, then add zeroes to make the length equals.
        //This is for the case: 7.1.1 > 7.01.1, after this option they become 7.10.1 and 7.01.1.
        if(currentNum_length < newNum_length) 
        {
            for(int j = 0; j < currentNum_length; j ++) 
            {
                if(currentNums[j] > newNums[j])
                {
                    return -1;
                }
                else if (currentNums[j] < newNums[j])
                {
                    return 1;
                }
            }
            return 1;
        }
        else if(currentNum_length > newNum_length)
        {
            for(int j = 0; j < newNum_length; j ++) 
            {
                if(currentNums[j] > newNums[j])
                {
                    return -1;
                }
                else if (currentNums[j] < newNums[j])
                {
                    return 1;
                }
            }
            return -1;
        }
        else
        {
            if(currentNum.equals(newNum))
            {
                return  0;
            }
            else
            {
                return (Integer.parseInt(newNum) > Integer.parseInt(currentNum)) ? 1 : -1;
            }
        }
    }
    
    protected void startMisLog()
    {
        MisLogManager misLogManager = MisLogManager.getInstance();

        String ptn = DaoManager.getInstance().getBillingAccountDao().getPtn();
        ServerDrivenParamsDao serverDrivenParamsDao = DaoManager.getInstance().getServerDrivenParamsDao();
        String productType = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().productType;

        misLogManager.initFilter(productType, serverDrivenParamsDao);
        misLogManager.initHelper(serverDrivenParamsDao);
        misLogManager.start(ptn);
        misLogManager.registerMisLogListener();
        
        TnRadioManager.getInstance().addListener(RadioStatusLogger.getInstance());
        LocationProvider.getInstance().addListener(GpsStatusLogger.getInstance());

        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.setAppStartLoc(LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK));
        appStatusMisLog.setAppStartBattery(TnBatteryManager.getInstance().getBatteryLevel());
        appStatusMisLog.setAppStartTime(TeleNavDelegate.getInstance().getStartTime());
        appStatusMisLog.setHomeShowTime(System.currentTimeMillis());

        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_SESSION_STARTUP))
        {
            SessionStartupMisLog log = misLogManager.getFactory().createSessionStartupMisLog();
            log.setTimestamp(System.currentTimeMillis());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }

        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_STARTUP_INFO))
        {
            StartupInfoMisLog log = misLogManager.getFactory().createStartupInfoMisLog();
            log.setTimestamp(System.currentTimeMillis());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }
        
        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ON_BOARD_STARTUP_INFO))
        {
            if (!NetworkStatusManager.getInstance().isConnected()
                    && MapDownloadStatusManager.getInstance().isOnBoardDataAvailable())
            {
                OnboardStartupInfoMisLog log = misLogManager.getFactory().createOnboardStartupInfoMisLog();
                log.setLocation();
                log.setTimestamp(System.currentTimeMillis());
                Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                        { log });
            }
        }
        
        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_FIRST_TIME_LOGIN) && appStatusMisLog.isFirstTimeLogin())
        {
            FirstTimeLoginMisLog log = misLogManager.getFactory().createFirstTimeLoginMisLog();
            log.setTimestamp(System.currentTimeMillis());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }

        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_HOME_SCREEN_TIME))
        {
            HomeScreenTimeMisLog log = misLogManager.getFactory().createHomeScreenTimeMisLog();
            log.setTimestamp(System.currentTimeMillis());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }
    }
    
    protected void synchronizeStartUp()
    {
        IStartupProxy startupProxy = ServerProxyFactory.getInstance().createStartupProxy(null, CommManager.getInstance().getComm(), this);

        long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(false);
        startupProxy.synchronizeStartUp(lastSyncTime);
    }
    
    protected void backgroundStartUp()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance().createSyncPurchaseProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
        backgroundStorePTN();
    }
    
    protected void backgroundStorePTN()
    {
        boolean result = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().load(this.getRegion());
        if(result)
        {
            PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
            String prefPtn = prefDao.getStrValue(Preference.ID_PREFERENCE_PHONE_NUMBER);
            if( prefPtn == null || prefPtn.equals("") )
            {
                String ptn = DaoManager.getInstance().getBillingAccountDao().getPtn();
                prefDao.setStrValue(Preference.ID_PREFERENCE_PHONE_NUMBER, ptn);
                prefDao.store(this.getRegion());
            }
        }
        else
        {
            //TODO AlbertMa do the initialize error handling.
        }
    }
    
    protected void syncServiceLocatorAndSdp()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        ISyncCombinationResProxy combinationResProxy = ServerProxyFactory.getInstance().createSyncCombinationResProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        Vector combination = new Vector();
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_SERVICE_LOCATOR_INFO));
        combinationResProxy.syncCombinationRes(combination);
    }
    
    protected boolean hasServiceLocator()
    {
        String serviceLocatorVersion = (DaoManager.getInstance().getServiceLocatorDao().getVersion());
        
        if(serviceLocatorVersion != null && serviceLocatorVersion.trim().length() > 0)
        {
            return true;
        }
        return false;
    }

    protected boolean hasPtn()
    {
        if (TeleNavDelegate.getInstance().isSimCardChanged())
        {
            // save the mini map dimensions to restore later, to avoid the display bug after inserting / pulling out SIM card without device restart
            int miniMapHeight = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT);
            int miniMapWidth = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH);
            int miniMapHeightLandscape = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND);
            int miniMapWidthLandscape = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND);

            ((DaoManager)DaoManager.getInstance()).clearUserPersonalData();

            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_HEIGHT, miniMapHeight);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_WIDTH, miniMapWidth);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND, miniMapHeightLandscape);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND, miniMapWidthLandscape);
            DaoManager.getInstance().getSimpleConfigDao().store();
            
            saveCurrentVersion();            
            return false;
        }
        
        BillingAccountDao dao = DaoManager.getInstance().getBillingAccountDao();
        String ptn = dao.getPtn();
        
        if(ptn != null && ptn.trim().length() > 0)
        {
            return true;
        }
        
        return false;
    }

    protected void checkBackBillingNotify()
    {
        AndroidBillingDao androidBillingDao =  DaoManager.getInstance().getAndroidBillingDao();
        if(androidBillingDao != null)
        {
            StringList notifys = androidBillingDao.getBackupNotifications();
            
            if(notifys != null)
            {
                int size = notifys.size();
                for(int i = 0; i < size; i++)
                {
                    String notifyId = notifys.elementAt(i);
                    Logger.log(Logger.INFO, this.getClass().getName(),
                        "MarketBilling: checkBackBillingNotify notifyId " + notifyId);
                    MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();                    
                    if(handler != null)
                    {
                        handler.getPurchaseInformation(notifyId);
                    }
                }
            }
        }
    }
    
    void startFlow()
    {
        NavsdkUserManagementService.getInstance().debugSettingUpdate();
        try
        {
            LocationProvider.getInstance().start();
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        boolean isGpsProviderAvialable = TnLocationManager.getInstance().isGpsProviderAvailable(TnLocationManager.GPS_179_PROVIDER);
        if (TnLocationManager.GPS_179_PROVIDER.equals(LocationProvider.getInstance().getGpsServiceType()) && !isGpsProviderAvialable
                && TnDeviceInfo.PLATFORM_ANDROID == TnTelephonyManager.getInstance().getDeviceInfo().platform)
        {
            if (!this.getBool(KEY_B_IS_DISABLE_LOCATION_SETTING))
            {
                this.postModelEvent(EVENT_MODEL_GOTO_LOCATION_SETTING);
                return;
            }
        }
        
        if(hasServiceLocator())
        {
            NavsdkUserManagementService.getInstance().serviceLocatorUpdate();
            
            Logger.log(Logger.INFO, this.getClass().getName(), "startFlow: hasServiceLocator");
            getLocation();
            if(hasPtn())
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "startFlow: hasPtn");
                if (DaoManager.getInstance().getBillingAccountDao().isLogin())
                {
                    /*boolean isExceedTimes = false;
                    
                    int accountStatus = DaoManager.getInstance().getBillingAccountDao().getAccountStatus();
                    
                    boolean isExpired = false;
                    
                    if(accountStatus == BillingAccountDao.ACCOUNT_STATUS_PREM_EXPIRED)
                    {
                        isExpired = true;
                    }
                    
                    if(isExpired)
                    {
                        int maxTimes = DaoManager.getInstance().getServerDrivenParamsDao().getIntValue(ServerDrivenParamsDao.UPSELL_DISPLAY_TIMES);
                        
                        if(maxTimes > 0)
                        {
                            int displayTimes = DaoManager.getInstance().getBillingAccountDao().getUpsellDisplayTimes();
                            if(displayTimes < 0)
                            {
                                displayTimes = 0;
                                DaoManager.getInstance().getBillingAccountDao().setUpsellDisplayTimes(displayTimes);
                                DaoManager.getInstance().getBillingAccountDao().store();
                            }
                            
                            if(displayTimes >= maxTimes)
                            {
                                isExceedTimes = true;
                            }
                        }
                        else
                        {
                            isExceedTimes = true;
                        }
                    }*/
                    
					NavsdkUserManagementService.getInstance().featureListUpdate();
                    NavsdkUserManagementService.getInstance().userProfileUpdate();

                    boolean needShowUpsell = BillingAccountDao.getInstance().isNeedShowUpsell();
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "start flow -- needShowUpsell: " + needShowUpsell);
                    }
                    
                    //1: If account is expired, then trigger the upsell page.
                    //2: Either purchase or not purchase, we always need to sync res and check upgrade.
                    // so refine the code by using State Machine and then we can reuse this action.
                    boolean isMaitaiOrPlungin = MaiTaiManager.getInstance().isFromMaiTai() || PluginManager.getInstance().isFromPlugin();
                    if (needShowUpsell && !isMaitaiOrPlungin)
                    {
                        BillingAccountDao.getInstance().setNeedShowUpsell(false);
                        BillingAccountDao.getInstance().store();
                        this.postModelEvent(EVENT_MODEL_GO_PURCHASE);
                    }
                    else
                    {
                        this.postModelEvent(EVENT_MODEL_SYNC_RES_AND_CHECK_UPGRADE);
                    }
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_LOGIN);
                }
            }
            else
            {
                this.postModelEvent(EVENT_MODEL_LOGIN);
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "startFlow: sync service locator");
            this.postModelEvent(EVENT_MODEL_SYNC_SERVICELOCATOR_SDP);
            syncServiceLocatorAndSdp();
            AppStatusMisLog innerAppStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
                IMisLogConstants.TYPE_INNER_APP_STATUS);
            if (innerAppStatusMisLog != null)
            {
                innerAppStatusMisLog.setIsFirstTimeLogin(true);
            }
        }
    }

    private void getLocation()
    {
        if(AppConfigHelper.isNeedRegionDetection)
        {
            TnLocation tnLocation = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, true);
            if (tnLocation != null)
            {
                detectRegion(tnLocation);
            }
            else
            {
                LocationProvider.getInstance().getCurrentLocation(
                    LocationProvider.GPS_VALID_TIME,
                    LocationProvider.NETWORK_LOCATION_VALID_TIME, 15 * 1000, this,
                    LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1, true);
            }
        }
        else
        {
            RegionDetectResultHandler.getInstance().setRegion("");
        }
    }
    
    private void detectRegion(TnLocation tnLocation)
    {
        RegionDetectResultHandler regionDetectResultHandler = RegionDetectResultHandler.getInstance();
        RegionDetector detector = new RegionDetector(regionDetectResultHandler);
        
        String region = detector.detectRegion(tnLocation);
        if((tnLocation == null || !tnLocation.isValid()) || (region != null && region.trim().length() > 0))
        {
            regionDetectResultHandler.setRegion(region);
        }
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        //only need popup error msg when sync service locator fail.
        if(proxy instanceof ISyncCombinationResProxy)
        {
            handleServiceLocatorResponse();
        }
        else if (proxy instanceof IAddressProxy)
        {
            if (this.fetchBool(KEY_B_DATASET_RESYNC))
            {
                handleSyncMapDatasetResError(proxy);
            }
        }
        else if (proxy instanceof IClientLoggingProxy
                && IServerProxyConstants.ACT_SEND_LOGIN_INFO.equals(proxy.getRequestAction()))
        {
            return;
        }
    }

    private void handleSyncMapDatasetResError(AbstractServerProxy proxy)
    {
        AddressBackupDao backupAddressDao = DaoManager.getInstance().getBackupAddressDao();
        backupAddressDao.cloneToAddressDao();
        this.postModelEvent(EVENT_MODEL_CHECK_UPGRADE);
    }
   
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if(proxy instanceof ISyncCombinationResProxy)
        {
            handleServiceLocatorResponse();
        }
        else if (proxy instanceof IToolsProxy && IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(proxy
                        .getRequestAction()))
        {
            // no need to handle background preference sync action.
            return;
        }
        else if(proxy instanceof ISettingDataProxy)
        {
            //no need to handle settingData sync action.
            return;
        }
        else if(proxy instanceof IStartupProxy)
        {
            //no need to handle startup action.
            return;
        }
        else if(proxy instanceof ILoginProxy)
        {
            ILoginProxy loginProxy = (ILoginProxy)proxy;
            if(loginProxy.getAccountStatus() > 0)
            {
                DaoManager.getInstance().getBillingAccountDao().setAccountStatus(loginProxy.getAccountStatus());
                DaoManager.getInstance().getBillingAccountDao().store();
            }
            return;
        }
        else if(proxy instanceof IAddressProxy)
        {
            if(this.fetchBool(KEY_B_DATASET_RESYNC))
            {
                handleSyncMapDatasetResError(proxy);
            }
            else 
            {
                return;
            }
        }
        else if(proxy instanceof IClientLoggingProxy
               && IServerProxyConstants.ACT_SEND_LOGIN_INFO.equals(proxy.getRequestAction()))
        {
            return;
        }
        else if(proxy instanceof ISyncPurchaseProxy)
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                if(!isActivated())
                {
                    AbstractController currentController = AbstractController.getCurrentController();
                    if (currentController != null)
                    {   
                        currentController.handleModelEvent(EVENT_MODEL_BACK_TO_ENTRY);
                    }
                }
                put(KEY_S_ERROR_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(IStringEntry.RES_LABEL_SYNC_PURCHASE_ERROR_MESSAGE, IStringEntry.FAMILY_ENTRY));
                DaoManager.getInstance().clearInternalRMS();
                postModelEvent(EVENT_MODEL_SYNC_PURCHASE_FAIL);
            }
            else
            {
                return;
            }
        }
        else
        {
            super.transactionError(proxy);
        }
    }
    
    public void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        if(proxy instanceof ISyncCombinationResProxy)
        {
            handleServiceLocatorResponse();
        }
        else if(proxy instanceof ISyncPurchaseProxy)
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                if(!isActivated())
                {
                    AbstractController currentController = AbstractController.getCurrentController();
                    if (currentController != null)
                    {   
                        currentController.handleModelEvent(EVENT_MODEL_BACK_TO_ENTRY);
                    }
                }
                put(KEY_S_ERROR_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(IStringEntry.RES_LABEL_SYNC_PURCHASE_ERROR_MESSAGE, IStringEntry.FAMILY_ENTRY));
                DaoManager.getInstance().clearInternalRMS();
                postModelEvent(EVENT_MODEL_SYNC_PURCHASE_FAIL);
            }
        }
        else if (proxy instanceof IToolsProxy)
        {
            SyncResExecutor.getInstance().handlePreferenceResp((IToolsProxy) proxy);
        }
        else if (proxy instanceof IStartupProxy)
        {
            String dataSetProvider = DaoManager.getInstance().getSimpleConfigDao()
                    .getString(SimpleConfigDao.KEY_SWITCHED_DATASET);
            String oldDataProvider = AbstractDaoManager.getInstance().getStartupDao().getMapDataset();

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
        else if(proxy instanceof ILoginProxy)
        {
            if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_LOGIN)
                    || proxy.getRequestAction().equals(IServerProxyConstants.ACT_ENCRYPT_LOGIN))
            {
                ILoginProxy loginProxy = (ILoginProxy)proxy;
                String ptn = loginProxy.getPtn();
                
                if(AppConfigHelper.isLoggerEnable && ( ptn == null || ptn.trim().length() == 0 ))
                {
                    Logger.log(Logger.ERROR, this.getClass().getName(), "ENTRY BG Login -- Empty ptn : " + ptn);
                }
                
                if(ptn != null && ptn.length() > 0)
                {
                    DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().phoneNumber = ptn;
                    Crashlytics.setString(ICrashlyticsConstants.KEY_PTN, ptn);
                }
                
                DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().pin = loginProxy.getPin();
                
                if(AppConfigHelper.isLoggerEnable && ( loginProxy.getUserId() == null || loginProxy.getUserId().trim().length() == 0 ))
                {
                    Logger.log(Logger.ERROR, this.getClass().getName(), "ENTRY BG Login -- Empty userId : " + loginProxy.getUserId());
                }
                
                DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId = loginProxy.getUserId();
                DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().eqpin = loginProxy.getEqPin() == null ? "" : loginProxy.getEqPin();
                
                DaoManager.getInstance().getBillingAccountDao().setSocType(loginProxy.getSoc_code());
                DaoManager.getInstance().getBillingAccountDao().store();
                
                String credentialId = loginProxy.getCredentialId();
                if(credentialId != null && credentialId.trim().length() > 0)
                {
                    DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo().credentialID = credentialId;
                }
                
                NavServiceParameter param = new NavServiceParameter();
                String userId = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId;
                if(userId != null && userId.trim().length() > 0)
                {
                    param.setUserId(userId);
                }
                NavServiceManager.getNavService().setNavServiceParameters(param);
                NavsdkUserManagementService.getInstance().userProfileUpdate();
            }
        }   
        else if(proxy instanceof IAddressProxy)
        {    
            if(this.fetchBool(KEY_B_DATASET_RESYNC))
            {
                String dataSet =DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_SWITCHED_DATASET);
                String mapCopyright =DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_MAP_COPYRIGHT);
                setMapDataSet(mapCopyright, dataSet);
                Logger.log(Logger.INFO, this.getClass().getName(), "map provider : " + " isResyncFinished finished");
                this.postModelEvent(EVENT_MODEL_CHECK_UPGRADE);        
            }
        }
    }
   
    private void setMapDataSet(String mapCopyright, String dataSetName)
    {
        DaoManager.getInstance().getStartupDao().setMapCopyright(mapCopyright);
        DaoManager.getInstance().getStartupDao().setMapDataset(dataSetName);
        DaoManager.getInstance().getStartupDao().store();
        DaoManager.getInstance().getSimpleConfigDao().remove(SimpleConfigDao.KEY_MAP_COPYRIGHT);
        DaoManager.getInstance().getSimpleConfigDao().remove(SimpleConfigDao.KEY_SWITCHED_DATASET);
        DaoManager.getInstance().getSimpleConfigDao().store();

        DaoManager.getInstance().getAddressDao().createReceivedCatalog();

        DaoManager.getInstance().getBackupAddressDao().clear();
        DaoManager.getInstance().getBackupAddressDao().store();
    }
    
    private void updateMapDatasetRes()
    {
        clearMapDatasetRes();
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);

        IAddressProxy proxy = ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider, false);
        proxy.fetchAllStops(true);
    }
    
    private void clearMapDatasetRes()
    {
        AddressBackupDao backupAddressDao = DaoManager.getInstance().getBackupAddressDao();
        backupAddressDao.copyToBackup();
        DaoManager.getInstance().getNearCitiesDao().clear();
        DaoManager.getInstance().getNearCitiesDao().store();
    }
    
    protected void handleServiceLocatorResponse()
    {
        if (hasServiceLocator())
        {
            postModelEvent(EVENT_MODEL_SYNC_SERVICELOCATOR_SDP_SUCC);
        }
        else // get Service Locator fail.
        {
            put(KEY_S_ERROR_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_SYNC_SERVICE_LOCATOR_ERROR, IStringLogin.FAMILY_LOGIN));
            
            postModelEvent(EVENT_MODEL_SYNC_SERVICELOCATOR_SDP_FAIL);
        }
    }
    
    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        boolean isLocationGot = false;
        TnLocation loc = null;
        if(statusCode == LocationProvider.STATUS_SUCCESS)
        {
            if(locations != null && locations.length > 0)
            {
                isLocationGot = true;
                loc = locations[0];
            }
        }
        
        if(!isLocationGot)
        {
            loc = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            if(loc == null)
            {
                loc = LocationProvider.getInstance().getDefaultLocation();
            }
        }
        detectRegion(loc);
    }

    private void postEvent(int event)
    {
        postModelEvent(event);
    }
    
    private class BrowserEventListener extends BrowserSdkModel
    {
        private static final String REMIND = "remind";

        private static final String DOWNLOAD = "download";

        public BrowserEventListener()
        {
            super();
        }

        public JSONObject doPrivateService(JSONArray args,
                IHtmlSdkServiceListener listener)
        {
            JSONObject retjsonObj = null;
            try
            {
                String serviceName = args.getString(0);
                String name = args.getString(1);
                if (PRIVATE_SERVICE_UPGRADE.equalsIgnoreCase(serviceName) && name != null)
                {
                    if (DOWNLOAD.equalsIgnoreCase(name))
                        postEvent(CMD_UPGRADE_NOW);
                    else if (REMIND.equalsIgnoreCase(name))
                        postEvent(CMD_REMIND_LATER);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return retjsonObj;
        }
        
        public JSONObject doNativeService(JSONObject request, IHtmlSdkServiceListener listener)
        {     
            JSONObject ret = null;
            int serviceId = ResultGenerator.SERVICE_ID_ZERO;
            try
            {
                String serviceName = null; 
                if (request != null) 
                {            
                    serviceId = request.getInt(IHtmlSdkServiceHandler.SERVICE_ID);
                    serviceName = request.getString(IHtmlSdkServiceHandler.SERVICE_NAME);
                }
                else
                {
                    return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
                }
                
                String type = request.getString(IHtmlSdkServiceHandler.SERVICE_DATA);
                if (PRIVATE_SERVICE_UPGRADE.equalsIgnoreCase(serviceName) && type != null)
                {
                    if (DOWNLOAD.equalsIgnoreCase(type))
                        postEvent(CMD_UPGRADE_NOW);
                    else if (REMIND.equalsIgnoreCase(type))
                        postEvent(CMD_REMIND_LATER);
                    
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                }
                else
                {
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }            
            
            return ret;
        }
    }

    protected void activateDelegate(boolean isUpdateView)
    {
        MapContainer.getInstance().resume();
    }
   
    public void nativeAppCallBack(int requestCode, int resultCode)
    {
        if(requestCode == TeleNavDelegate.LAUNCH_LOCATION_SETTING_REQUESTCODE)
        {
            startFlow();
        }       
    }
    
}

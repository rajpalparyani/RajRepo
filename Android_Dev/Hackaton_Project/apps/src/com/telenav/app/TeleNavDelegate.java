/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TeleNavDelegate.java
 *
 */
package com.telenav.app;

import java.util.Vector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;

import com.crashlytics.android.Crashlytics;
import com.telenav.ads.MobileAppConversionTracker;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.RuntimeStatusLogger;
import com.telenav.app.android.jni.NavSdkEngine;
import com.telenav.app.android.scout_us.TeleNav;
import com.telenav.carconnect.host.CarConnectHostManager;
import com.telenav.comm.HostProvider;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.dao.serverproxy.ServiceLocator;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkApplicationLifecycleProxyHelper;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.MainAppStatus;
import com.telenav.location.TnLocation;
import com.telenav.log.TnThreadFileLogger;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AppSessionMisLog;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.log.mis.log.TripSummaryMisLog;
import com.telenav.logger.Logger;
import com.telenav.media.TnMediaManager;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.FriendlyUserRatingUtil;
import com.telenav.module.ModuleFactory;
import com.telenav.module.dashboard.DashboardManager;
import com.telenav.module.location.AbstractLocationEncrypter;
import com.telenav.module.location.EmptyLocationEncrypter;
import com.telenav.module.location.LocationCacheManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.login.LoginController;
import com.telenav.module.login.LoginController.PtnData;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler;
import com.telenav.module.mapdownload.MapDownloadOnBoardDataStatusManager;
import com.telenav.module.marketbilling.MarketBillingHandler;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.preference.PreferenceCoverageListener;
import com.telenav.module.sync.SyncPreferenceCoverageListener;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.radio.TnRadioManager;
import com.telenav.res.IAudioRes;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.telephony.TnBatteryManager;
import com.telenav.telephony.TnSimCardInfo;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.ui.ScoutUiStyleManager;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.citizen.map.MapVehiclePositionService;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public final class TeleNavDelegate
{
    public static final String KEY_C2DM_REGISTRATION_ID = "C2DM_REGISTRATION_ID";
    
    public static final String C2DM_FEATURE_APP_PRELOAD = "C2DM_FEATURE_APP_PRELOAD";
    
    public static final String LAUNCH_LOCATION_SETTING = "LAUNCH_LOCATION_SETTING";
    
    public static final String SWTICH_AIRPLANE_MODE = "SWTICH_AIRPLANE_MODE";
    
    public final static String SWITCH_AIRPLANE_ON = "Start crazy switch";
    
    public final static String SWITCH_AIRPLANE_OFF = "End crazy switch";
    
    public static final int LAUNCH_CONTACT_REQUESTCODE = 0x1;
    
    public static final int LAUNCH_LOCATION_SETTING_REQUESTCODE = 0x2;

    public static final int LAUNCH_SHARE_REQUESTCODE = 0x3;
    
    public static final int LAUNCH_EMAIL_SENDING_REQUESTCODE = 0x4;
    
    public static final String ACTIVIATE_TYPE_RESTART = "ACTIVIATE_TYPE_RESTART";
    
    public static final String ACTIVIATE_TYPE_BACKLIGHT_ON = "ACTIVIATE_TYPE_BACKLIGHT_ON";
    
    
    public static final String DEACTIVIATE_TYPE_STOP = "DEACTIVIATE_TYPE_STOP";
    
    public static final String DEACTIVIATE_TYPE_BACKLIGHT_OFF = "DEACTIVIATE_TYPE_BACKLIGHT_OFF";
    
    public static final String FEATURE_NOTIFY_REFRESH_WIDGET = "REFRESH_WIDGET";
    
    public static final String FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE = "UPDATE_WINDOW_SOFT_INPUT_MODE";
    
    public static final int ORIENTATION_UNSPECIFIED = -1;
    
    public static final int ORIENTATION_LANDSCAPE = 0;
    
    public static final int ORIENTATION_PORTRAIT = 1;
    
    protected long startup;

    protected boolean isExiting = false;
    
    protected boolean isStarted = false;
    
    protected boolean isJumb2Background = false;

    protected static TeleNavDelegate instance = new TeleNavDelegate();

    protected boolean applicationSuspended;
    
    public static boolean IS_START_CRAZY_SWITCH = false;
    
    protected IApplication application;
    
    protected Vector applicationListeners = new Vector();
    
    protected Vector screenStateListeners = new Vector();
    
    protected BackgroundSyncResourceManager backgroundSyncResourceManager; 
    
    public static final int DEFAULT_MAX_EXIT_TIME = 30 * 1000;
    
    private boolean needAppMapResume = true;
    
    private MarketBillingHandler billingHandler;
    
    private long lastSaveTime = -1;
    
    private static final long MIN_SAVE_INTERVAL = 5000;

    private String exitMsg = null;
    
    private boolean isExitWaitingCancelled = false;
    
    private int lastOrientation = -1;
    
    private LoginController.UserLoginData existUserLoginData;
    
    
    private TeleNavDelegate()
    {
        
    }
    
    public static TeleNavDelegate getInstance()
    {
        return instance;
    }
    
    public final void setApplication(IApplication application)
    {
        this.application = application;
    }
    
    /**
     * start application
     */
    public final void startApp(String[] args, IApplication application)
    {
        if (this.application == null)
        {
            this.application = application;
        }
        startup = System.currentTimeMillis();
        isExiting = false;
        isStarted = true;
        
        try
        {
            AppCrashChecker.getInstance().checkCrashState();
            Logger.start();
            
            //TODO will check if so many init methods cause the performance issues.
            AbstractPlatformIniter.getInstance().initBacklight();
            AbstractPlatformIniter.getInstance().initNio();
            AbstractPlatformIniter.getInstance().initLocation();
            AbstractPlatformIniter.getInstance().initMedia();
            AbstractPlatformIniter.getInstance().initRadio();
            AbstractPlatformIniter.getInstance().initSensor();
            AbstractPlatformIniter.getInstance().initTelephony();
            AbstractPlatformIniter.getInstance().initContactPrvoider();
            AbstractPlatformIniter.getInstance().initBattery();
            AbstractPlatformIniter.getInstance().initAudioEncoder();
            AbstractPlatformIniter.getInstance().initSdLogCollector();
            
            Notifier.getInstance().start();
            MapVehiclePositionService.getInstance().start(true);
            
            AppConfigHelper.load();
            
            initByBrand();
            
            boolean isOnBoardMode = !NetworkStatusManager.getInstance().isConnected();
            boolean isNeedUpload = DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SETTING_DATA_NEED_UPLOAD);
            
            if(isOnBoardMode && isNeedUpload)
            {
                NetworkStatusManager.getInstance().addStatusListener(new PreferenceCoverageListener());
            }
            
            boolean isUserProfileNeedUpload = DaoManager.getInstance().getSimpleConfigDao()
                    .getBoolean(SimpleConfigDao.KEY_USER_PROFILE_NEED_UPLOAD);
            boolean isHomeWorkNeedUpload = DaoManager.getInstance().getSimpleConfigDao()
                    .getBoolean(SimpleConfigDao.KEY_HOME_WORK_NEED_UPLOAD);

            isNeedUpload = (isUserProfileNeedUpload | isHomeWorkNeedUpload);
            
            if(isOnBoardMode && isNeedUpload)
            {
                NetworkStatusManager.getInstance().addStatusListener(SyncPreferenceCoverageListener.getInstance());
            }

            AbstractPlatformIniter.getInstance().initNavSdk();
            
            MapDownLoadMessageHandler.getInstance().init();
            
            MediaManager.getInstance().setAudioPlayerFormat(getInternalAudioFormat(AppConfigHelper.audioFamily));
            setDefaultLocale();
            
            NavSdkEngine.getInstance().start();
            NavSdkApplicationLifecycleProxyHelper.getInstance().applicationLifeToForeground();
            
            boolean isLoggerEnabled = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_LOGGER_ENABLE);
            boolean isSdCardOuptEnable = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SDCARD_OUTPUT_LOGGER_ENABLE);
            //Close the log output by default
            boolean isLoggerSetted = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(
                SimpleConfigDao.KEY_LOGGER_ENABLE) != null;
            if(!isLoggerSetted && TeleNav.isDebugVerison)
            {
                //disable it for release.
                isLoggerEnabled = true;
                isSdCardOuptEnable = true;
            }
            Logger.DEBUG = isLoggerEnabled;
            AppConfigHelper.setIsLoggerEnabled(isLoggerEnabled);
            AppConfigHelper.setIsOutputSdCardEnabled(isSdCardOuptEnable);
            boolean isRuntimeStatusLogEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_ENABLE);
            int runtimeStatusLogInterval = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getInteger(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_INTERVAL);
            if(isRuntimeStatusLogEnable)
            {
                RuntimeStatusLogger.getInstance().start(runtimeStatusLogInterval);
            }
            
            boolean isStuckMonitorEnabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SET_STUCK_MONITOR_ENABLE);
            AppConfigHelper.setIsStuckMonitorEnable(isStuckMonitorEnabled);
            
            boolean isMapDiskCacheDisabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SET_MAP_DISK_CACHE_DISABLE);
            AppConfigHelper.setMapDiskCacheDisabled(isMapDiskCacheDisabled);
            
            boolean isDisableRoutesInSatelliteView = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_DISABLE_ROUTE_IN_SATELLITE);
            AppConfigHelper.setDisableRoutesInSatelliteView(isDisableRoutesInSatelliteView);
            
            String alongrouteSpeed = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_ALONG_ROUTE_SPEED);
            if(alongrouteSpeed == null || alongrouteSpeed.trim().length() == 0)
            {
                alongrouteSpeed = "30";
            }
            AppConfigHelper.setAlongrouteSpeed(alongrouteSpeed);
            
            //---------------------------------------------------------------------
            //==================== Logger will work from here =====================
            //---------------------------------------------------------------------
            
            CommManager.getInstance().initHostUrls(HostList.getHostUrls());
            
            
            if(SerializableManager.getInstance() == null)
            {
                SerializableManager.init(new TxNodeSerializableManager());
            }
            
            MobileAppConversionTracker.getInstance().initMobileAppTracking((Context)this.application);
            
            String currentVersion = DaoManager.getInstance().getStartupDao().getCurrentAppVersion();
            
            checkVersionChange();
            
            FriendlyUserRatingUtil.recordHittedCurrentTime();
            
            MobileAppConversionTracker.getInstance().mobileAppTrackInstall((Context)this.application, currentVersion);
            
            CommManager.getInstance().initializeServiceLocator();
            
            if(DwfServiceConnection.getInstance().getConnection() != null)
            {
                HostProvider hostProvider = CommManager.getInstance().getComm().getHostProvider();
                ServiceLocator serviceLocator = (ServiceLocator)hostProvider;
                
                DwfServiceConnection.getInstance().getConnection()
                        .setDwfServiceUrl(serviceLocator.getActionUrl(IServerProxyConstants.ACT_DWF_HTTPS));
                DwfServiceConnection.getInstance().getConnection()
                        .setDwfWebUrl(serviceLocator.getActionUrl(IServerProxyConstants.ACT_DWF_TINY));
            }
            
            registerApplicationListener(TnBacklightManagerImpl.getInstance());

            Logger.log(Logger.INFO, TnThreadFileLogger.LAUNCH_APPLICATION, "");

            Logger.log(Logger.INFO, this.getClass().getName(), "", new Object[]{TnThreadFileLogger.LAUNCH_APPLICATION});

            startAppFlow();
            
            TnUiTimer.getInstance().enable(true);
            
            TnTelephonyManager.getInstance().addListener(MediaManager.getInstance());
            
            backgroundSyncResourceManager = BackgroundSyncResourceManager.getInstance();
            
            NotificationManager.getInstance().cancelNotification();
            NotificationManager.getInstance().cancelMarketBillingNotification();
            
            //--- start added for CarConnect
            CarConnectHostManager.getInstance().startHost(AndroidPersistentContext.getInstance().getContext());
            //--- end added for CarConnect
        }
        catch (Exception e)
        {
            Logger.logCriticalError(this.getClass().getName(), e);
        }
    }
    
    protected void startAppFlow()
    {
        int state = TnPersistentManager.getInstance().getExternalStorageState();
        MapDownloadOnBoardDataStatusManager.getInstance().triggerNavSdkCheckMapDownload(state);
        ModuleFactory.getInstance().startAppFlow();
    }
    
    private void initByBrand()
    {

        ModuleFactory.init(new ModuleFactory());
        AbstractLocationEncrypter.init(new EmptyLocationEncrypter());
        if(AppConfigHelper.BRAND_SPRINT.equals(AppConfigHelper.brandName) ||
                AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName) ||
                AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
        {
            UiStyleManager.init(new ScoutUiStyleManager());
        }
        else
        {
            Throwable t = new Throwable("brandName is invalid");
            Crashlytics.logException(t);
            Logger.log(this.getClass().getName(), t);
        }
//        else if(AppConfigHelper.BRAND_ATT.equals(AppConfigHelper.brandName))
//        {
//            UiStyleManager.init(new AttUiStyleManager());
//        }
//        else
//        {
//            UiStyleManager.init(new TnUiStyleManager());
//        }
    }
    
    private String getInternalAudioFormat(String audioFamily)
    {
        if(IAudioRes.FAMILY_MP3HI.equals(audioFamily) || IAudioRes.FAMILY_MP3.equals(audioFamily))
        {
            return TnMediaManager.FORMAT_MP3;
        }
        else if(IAudioRes.FAMILY_AMR.equals(audioFamily))
        {
            return TnMediaManager.FORMAT_AMR;
        }
        else if(IAudioRes.FAMILY_WAV.equals(audioFamily))
        {
            return TnMediaManager.FORMAT_PCM;
        }
        else
        {
            return TnMediaManager.FORMAT_MP3;
        }
    }
    
    protected void checkVersionChange()
    {
        String currentVersion = DaoManager.getInstance().getStartupDao().getCurrentAppVersion();
        String buildNumber = DaoManager.getInstance().getStartupDao().getBuildNumber();
        boolean isMainVersionChanged = false;
        boolean isBuildNumberChanged = false;
        
        if (currentVersion == null || currentVersion.length() == 0 || !currentVersion.equals(AppConfigHelper.mandatoryClientVersion))
        {
            isMainVersionChanged = true;
        }
        else if(buildNumber == null || buildNumber.length() == 0 || !buildNumber.equals(AppConfigHelper.buildNumber))
        {
            isBuildNumberChanged = true;
        }
        
        if (isMainVersionChanged || isBuildNumberChanged)
        {
            if (currentVersion != null && AppConfigHelper.isNeedReadExistUserLoginInfo (currentVersion) && !isSimCardChanged())
            {
                readExistUserLoginInfo();
            }
            
            int miniMapHeight = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT);
            int miniMapWidth = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH);
            int miniMapHeightLandscape = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND);
            int miniMapWidthLandscape = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND);
            
            boolean isPreviousRated = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_PREVIOUS_RATED);
            boolean isCurrentRated = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_CURRENT_RATED);
            boolean isLoveAppInFeedback = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_LOVE_APP_IN_FEEDBACK);
            int crashTimes = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_CRASH_TIMES);
            
            boolean isSharaed =  DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USED_SHARE_SCOUT);
            boolean isRated =  DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USED_RATE_SCOUT);
            
            DaoManager.getInstance().clearInternalRMS(isMainVersionChanged);
            
            //clear Map Cache.
            FileStoreManager.clearMapCache();
            
            //clear locale and resource bundle cache:
            ResourceManager.getInstance().getCurrentBundle().getPersistentBundle().clear();
            
            //clear opengl resource
            FileStoreManager.clearOpenglResource();
            
            DaoManager.getInstance().getStartupDao().setCurrentAppVersion(AppConfigHelper.mandatoryClientVersion);
            DaoManager.getInstance().getStartupDao().setBuildNumber(AppConfigHelper.buildNumber);
            DaoManager.getInstance().getStartupDao().store();
            
            //keep map parameter
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_HEIGHT, miniMapHeight);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_WIDTH, miniMapWidth);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND, miniMapHeightLandscape);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND, miniMapWidthLandscape);
             
            //keep friendly data for rating
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_PREVIOUS_RATED, isPreviousRated);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_CURRENT_RATED, isCurrentRated);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_LOVE_APP_IN_FEEDBACK, isLoveAppInFeedback);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_CRASH_TIMES, crashTimes);
            
            //keep scout rate and share
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_USED_SHARE_SCOUT, isSharaed);
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_USED_RATE_SCOUT, isRated);
            
            DaoManager.getInstance().getSimpleConfigDao().store();
           
            if (existUserLoginData != null)
            {
                ((DaoManager)DaoManager.getInstance()).getUpgradeDao().setUserLoginData(existUserLoginData);
                ((DaoManager)DaoManager.getInstance()).getUpgradeDao().store();
            }
        }
    }
    
  

    protected void readExistUserLoginInfo()
    {
        if (!BillingAccountDao.getInstance().isLogin())
        {
            return;
        }

        String ptn = BillingAccountDao.getInstance().getPtn();
        if (ptn == null)
        {
            return;
        }

        CredentialInfo credentialInfo = MandatoryNodeDao.getInstance().getMandatoryNode().getCredentialInfo();
        String carrier = MandatoryNodeDao.getInstance().getMandatoryNode().getClientInfo().deviceCarrier;
        String ptn_type = MandatoryNodeDao.getInstance().getMandatoryNode().getUserInfo().ptnType;

        existUserLoginData = new LoginController.UserLoginData();
        
        existUserLoginData.ptnData = new PtnData();
        existUserLoginData.ptnData.ptn = ptn;
        existUserLoginData.ptnData.carrier = carrier;
        existUserLoginData.ptnData.ptn_type = ptn_type;
        existUserLoginData.credentialInfo = credentialInfo;
    }

    public final void registerApplicationListener(IApplicationListener applicationListener)
    {
        if(applicationListener != null && !applicationListeners.contains(applicationListener))
        {
            applicationListeners.addElement(applicationListener);
        }
    }
    
    public final void unregisterApplicationListener(IApplicationListener applicationListener)
    {
        if(applicationListener != null && applicationListeners.contains(applicationListener))
        {
            applicationListeners.removeElement(applicationListener);
        }
    }
    
    public final void registerScreenStateListener(IScreenStateListener screenStateListener)
    {
        if(screenStateListeners != null && !screenStateListeners.contains(screenStateListener))
        {
            screenStateListeners.addElement(screenStateListener);
        }
    }
    
    public final void unregisterScreenStateListener(IScreenStateListener applicationListener)
    {
        if(screenStateListeners != null && screenStateListeners.contains(applicationListener))
        {
            screenStateListeners.removeElement(applicationListener);
        }
    }
    
    public final void exitApp()
    {
        exitApp(false, null);
    }
    
    public final void exitApp(final boolean forceExitQuickly, final Object callbackParams)
    {
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_GLOBAL, KontagentLogger.GLOBAL_APP_EXIT);
        KontagentLogger.getInstance().stop();
        
        Logger.log(Logger.INFO, this.getClass().getName(), "", new Object[]{TnThreadFileLogger.EXIT_APPLICATION});
        setBackgroundDrawable(null);
        
        //--- start added for CarConnect
//        CarConnectHostManager.getInstance().stopHost();
        //--- end added for CarConnect 
        
        if(forceExitQuickly)
        {
            AppCrashChecker.getInstance().exitAppNormally();
            NavRunningStatusProvider.getInstance().setNavRunningEnd();
            application.exit(callbackParams, exitMsg);
            return;
        }
        
        MisLogManager.getInstance().storeToRms();
        
        isExiting = true;
        
        NavSdkNavEngine navEngine = NavSdkNavEngine.getInstance();
        if (navEngine.isRunning())
        {
            navEngine.stop();
        }
        NavSdkEngine navSdkEngine = NavSdkEngine.getInstance();
        if(navSdkEngine.isRunning())
        {
            navSdkEngine.stop();
        }
        MediaManager.getInstance().getAudioPlayer().cancelAll();
        
        DashboardManager.getInstance().stop();
        DashboardManager.getInstance().setDashboardInfoChangeListener(null);
        
        NetworkStatusManager.getInstance().release();
        this.jumpToBackground(callbackParams);
        
        AppCrashChecker.getInstance().exitAppNormally();
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                long time = System.currentTimeMillis();
                
                try
                {
                    MapVehiclePositionService.getInstance().stop();
                    MapContainer.getInstance().onPause();
                    MapContainer.getInstance().destroyMapView();
                    
                    DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_HOME_LAUCHED, false);
                    ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
                    
                    MisLogManager misLogManager = MisLogManager.getInstance();
                    AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
                    appStatusMisLog.setAppEndBattery(TnBatteryManager.getInstance().getBatteryLevel());
                    appStatusMisLog.setAppEndLoc(LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK));

                    TripSummaryMisLog tripSummaryMisLog = (TripSummaryMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_TRIP_SUMMARY);
                    if(tripSummaryMisLog != null)
                    {
                        MisLogManager.getInstance().logExitInfo(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, null, new Object[]
                        { tripSummaryMisLog });
                        MisLogManager.getInstance().removeStoredLog(tripSummaryMisLog);
                    }
                    
                    if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_APP_SESSION_SUMMARY))
                    {
                        AppSessionMisLog log = (AppSessionMisLog) misLogManager.getFactory().creatAppSessionMisLog();
                        MisLogManager.getInstance().logExitInfo(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, null, new Object[]
                        { log });
                    }
                    MisLogManager.getInstance().shutdown();

                    unregisterApplicationListener(TnBacklightManagerImpl.getInstance());
                    TnBacklightManagerImpl.getInstance().stop();

                    TnTelephonyManager.getInstance().removeListener(MediaManager.getInstance());

                    TnBatteryManager.getInstance().finish();
                    


                    long costTime = System.currentTimeMillis() - time;
                    int timeout = ((DaoManager)DaoManager.getInstance()).getServerDrivenParamsDao().getIntValue(ServerDrivenParamsDao.APP_BACKGROUND_LIVE_TIME);
                    if(timeout <= 0)
                    {
                        timeout = DEFAULT_MAX_EXIT_TIME;
                    }
                    
                    if (NetworkStatusManager.getInstance().isConnected())
                    {
                        backgroundSyncResourceManager.syncResourcesInDeactivate();
                        
                        if(!forceExitQuickly)
                        {
                            while(costTime < timeout && !canExit())
                            {
                                if(isExitWaitingCancelled)
                                {
                                    break;
                                }
                                try
                                {
                                    Thread.sleep(100);
                                }
                                catch (InterruptedException e)
                                {
                                    Logger.log(this.getClass().getName(), e);
                                }
                                costTime = System.currentTimeMillis() - time;
                            }
                        }
                    }
                    MisLogManager.getInstance().shutdown();
                    
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                finally
                {
                    isStarted = false;
                    
                    try
                    {
                        Logger.shutdown();
                    }
                    catch(Throwable t)
                    {
                        Logger.log(this.getClass().getName(), t);
                    }
                    
                    try
                    {
                        NotificationManager.getInstance().cancelNotification();
                        NotificationManager.getInstance().cancelMarketBillingNotification();
                    }
                    catch(Throwable t)
                    {
                        Logger.log(this.getClass().getName(), t);
                    }
                    
                    DaoManager.saveInternalRMS();
                    application.exit(callbackParams, exitMsg);
                }
            }
        });
        t.start();
    }
    
    public void cancelExitWaiting()
    {
        isExitWaitingCancelled = true;
    }
    
    private boolean canExit()
    {
        return backgroundSyncResourceManager.isSyncFinished();
    }
    
    public final void jumpToBackground()
    {
        jumpToBackground(true, true, null);
    }
    
    public final void jumpToBackground(Object data)
    {
        jumpToBackground(true, true, data);
    }
    
    public final void jumpToBackground(boolean needSendRequest, boolean needRestartFlow, Object data)
    {
    	Logger.log(Logger.INFO, this.getClass().getName(), "", new Object[]{TnThreadFileLogger.HIDE_TO_BACKGROUND});
    	
        try
        {
            if(!isExiting && needSendRequest && backgroundSyncResourceManager.isSyncFinished())
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            if (System.currentTimeMillis() - lastSaveTime > MIN_SAVE_INTERVAL)
                            {
                                DaoManager.saveInternalRMS();
                                lastSaveTime = System.currentTimeMillis();
                            }
                        }
                        catch (Exception e)
                        {
                            Logger.log(this.getClass().getName(), e);
                        }
                        
                        try
                        {
                            TeleNavDelegate.getInstance().callAppNativeFeature(
                                TeleNavDelegate.C2DM_FEATURE_APP_PRELOAD, null);
                        }
                        catch (Exception e)
                        {
                            Logger.log(this.getClass().getName(), e);
                        }

                        backgroundSyncResourceManager.syncResourcesInDeactivate();
                    }
                }).start();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            isJumb2Background = !isExiting;
            this.application.jumpToBackground(data);
        }
    }
    
    public final void activateApp(String type)
    {
        try
        {
            if(needAppMapResume)
            {
                MapContainer.getInstance().startPreloader();
                try
                {
                    MapVehiclePositionService.getInstance().resume();
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                
                try
                {
                    MapContainer.getInstance().resume();
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
            try
            {
                LocationProvider.getInstance().start();
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            if(applicationSuspended)
            {
                for(int i = 0; i < applicationListeners.size(); i++)
                {
                    Object o = applicationListeners.elementAt(i);
                    if(o instanceof IApplicationListener)
                    {
                        IApplicationListener listener = (IApplicationListener)o;
                        String[] params = new String[1]; 
                        params[0] = type;                       
                        listener.appActivated(params);
                    }
                }
                MapContainer.getInstance().updateMapView();
            }
            
            
            if(isJumb2Background && backgroundSyncResourceManager.isSyncFinished())
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        backgroundSyncResourceManager.syncInActivateFromJumb();
                    }
                }).start();
                
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        try
        {
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

            if (dwfAidl != null)
            {
                try
                {
                    dwfAidl.setMainAppStatus(MainAppStatus.foreground.name());
                    if (dwfAidl.getSharingIntent() != null && !NavRunningStatusProvider.getInstance().isNavRunning())
                    {
                        dwfAidl.enableNotification(true, MainAppStatus.foreground.name());
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        applicationSuspended = false;
        isJumb2Background =false;
        
        //Q: why we invoke LocationProvider.getInstance().start() twice?
        //A: 1) it is no harm to invoke it twice. we start GPS reader earlier to getting GPS earlier.
        //   2) in MovingMapModel.doBackHome(), we will invoke LocationProvider.getInstance().stop() if applicationSuspended.
        //      there is a corner condition that the GPS reader does not start after activateApp(), this is a protection.
        
        try
        {
            LocationProvider.getInstance().start();
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        NavSdkApplicationLifecycleProxyHelper.getInstance().applicationLifeToForeground();
    }
    
    public final boolean isActivate()
    {
        return applicationSuspended == false;
    }
    
    public final boolean isJumb2Background()
    {
        return isJumb2Background;
    }
    
    public final long getStartTime()
    {
        return this.startup;
    }
    
    public final boolean isExiting()
    {
        return this.isExiting;
    }
    
    public final boolean isStarted()
    {
        return this.isStarted;
    }
    
    public final void screenOn()
    {
        for (int i = 0; i < screenStateListeners.size(); i++)
        {
            Object o = screenStateListeners.elementAt(i);
            if (o instanceof IScreenStateListener)
            {
                IScreenStateListener listener = (IScreenStateListener) o;
                listener.onScreenOn();
            }
        }
    }
    
    public final void screenOff()
    {
        for (int i = 0; i < screenStateListeners.size(); i++)
        {
            Object o = screenStateListeners.elementAt(i);
            if (o instanceof IScreenStateListener)
            {
                IScreenStateListener listener = (IScreenStateListener) o;
                listener.onScreenOff();
            }
        }
    }
    
    public final void deactivateApp(String type)
    {
        //fix bug TNANDROID-1324, do not suspend twice, otherwise "needAppMapResume" will be always false.
        if (applicationSuspended)
            return;
        
        try
        {
            //roll back the change made in bug fixing TNANDROID-1324, because it drains battery a lot
//            if (DEACTIVIATE_TYPE_STOP.equals(type)) // only do below work for stop type
            {
            if(MapContainer.getInstance().isPaused())
            {
                needAppMapResume = false;
            }
            else
            {
                MapContainer.getInstance().stopPreloader();
                try
                {
                    MapContainer.getInstance().pause();
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                needAppMapResume = true;
            }
            
            try
            {
                storeLastLocation();
                if(!NavRunningStatusProvider.getInstance().isNavRunning()/* && !CarConnectHostManager.getInstance().isLocationAlwaysOn()*/)
                {
                    try
                    {
                        MapVehiclePositionService.getInstance().pause();
                    }
                    catch(Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    LocationProvider.getInstance().stop();
                }
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            }
            
            if (!applicationSuspended)
            {
                for (int i = 0; i < applicationListeners.size(); i++)
                {
                    Object o = applicationListeners.elementAt(i);
                    if (o instanceof IApplicationListener)
                    {
                        IApplicationListener listener = (IApplicationListener) o;
                        String[] params = new String[1]; 
                        params[0] = type;
                        listener.appDeactivated(params);
                    }
                }
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        try
        {
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

            if (!NavRunningStatusProvider.getInstance().isNavRunning() && dwfAidl != null)
            {
                try
                {
                    dwfAidl.setMainAppStatus(MainAppStatus.background.name());
                    if (dwfAidl.getSharingIntent() != null)
                    {
                        dwfAidl.enableNotification(true, MainAppStatus.background.name());
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        NavSdkApplicationLifecycleProxyHelper.getInstance().applicationLifeToBackground();
        applicationSuspended = true;
    }
    
    protected void storeLastLocation()
    {
        TnLocation gpsFix = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS);
        if(gpsFix != null)
        {
            LocationCacheManager.getInstance().setLastGpsLocation(gpsFix);
        }
        
        TnLocation networkFix = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_NETWORK);
        if(networkFix != null)
        {
            LocationCacheManager.getInstance().setLastCellLocation(networkFix);
        }
        
        LocationCacheManager.getInstance().save();
    }

    public final Object callAppNativeFeature(String feature, Object[] params)
    {
        if(this.application != null)
        {
            return this.application.callAppNativeFeature(feature, params, null);
        }
        
        return null;
    }
    
    public final Object callAppNativeFeature(String feature, Object[] params,INativeAppCallBack nativeAppCallback)
    {
        if(this.application != null)
        {
            return this.application.callAppNativeFeature(feature, params, nativeAppCallback);
        }
        
        return null;
    }
    
    public final Object getDataFromNativeApp(String key)
    {
        if(this.application != null)
        {
            return this.application.getDataFromNativeApp(key);
        }
        
        return null;
    }
    
    public final void closeVirtualKeyBoard(AbstractTnComponent component)
    {
        if(this.application != null)
        {
            this.application.closeVirtualKeyBoard(component);
        }
    }
    
    protected void setDefaultLocale()
    {
        ResourceManager.getInstance().setLocale("en_US");
    }
    
    protected String getDeviceLocale()
    {
        String localeLanguage = TnTelephonyManager.getInstance().getDeviceInfo().systemLocale;
        return getDeviceLocale(localeLanguage);
    }
    
    protected String getDeviceLocale(String localeLanguage)
    {
        if (localeLanguage != null && localeLanguage.length() > 0)
        {
            String localesList = AppConfigHelper.localesList;
            
            int pos = -1;
            while (true)
            {
                pos = localesList.indexOf(',');
                if (pos == -1)
                {
                    pos = localesList.length();
                }
                
                String tmpLocale = localesList.substring(0, pos);
                if (tmpLocale.indexOf(localeLanguage) != -1)
                {
                    return tmpLocale;
                }
                
                if (pos+1 < localesList.length() - 1)
                {
                    localesList = localesList.substring(pos+1);
                }
                else
                {
                    break;
                }
            }
        }
        
        return "";
    }
    
    public boolean isSimCardChanged()
    {
        //current sim card
        TnSimCardInfo simCardInfo = TnTelephonyManager.getInstance().getSimCardInfo();
        
        //login used sim card
        BillingAccountDao dao = DaoManager.getInstance().getBillingAccountDao();
        if(!dao.isLogin())
        {
            return false;
        }
        String loginSimCardId = dao.getSimCardId();
        
        //find if has sim card now.
        boolean hasSimCard = false;
        if(TnRadioManager.getInstance().isAirportMode())
        {
            return false;
        }
        else if(simCardInfo.simCardId != null && simCardInfo.simCardId.trim().length() > 0)
        {
            hasSimCard = true;
        }
        
        //find if user login with sim card.
        boolean hasLoginSimCard = false;
        if(loginSimCardId != null && loginSimCardId.trim().length() > 0)
            hasLoginSimCard = true;
        
        //if currently no sim card but previously login with sim card, return true.
        //if currently has sim card but previously logic without sim card, return true.
        if(hasSimCard != hasLoginSimCard)
        {
            return true;
        }
        
        if(hasSimCard && hasLoginSimCard && !simCardInfo.simCardId.trim().equals(loginSimCardId.trim()))
        {
            return true;
        }
        
        return false;
    }
    
    public final void setOrientation(int orientation)
    {
        if(this.application != null && !AppConfigHelper.isTabletSize())
        {
            // To fix bug TNANDROID-4268, TNANDROID-3730, TNANDROID-4075
            // We find that there bugs only happend on Samsung Galaxy Nexus 4.1.1 (even in 4.2, cannot reproduce), 
            // and cannot anchor the root reason but only confirm one thing that it does
            // be the native system related bug. So we use the below trick code to avoid it. Please FIXME.
            // By Wang, Jianpu at 28/1/2013
            if(lastOrientation != orientation && android.os.Build.VERSION.SDK_INT == 16)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            this.application.setRequestedOrientation(orientation);
            lastOrientation = orientation;
        }
    }
    
    public final int getOrientation()
    {
        if(this.application != null)
        {
            return this.application.getRequestedOrientation();
        }
        return TeleNavDelegate.ORIENTATION_UNSPECIFIED;
    }
    
    public void setBillingHandler(MarketBillingHandler handler)
    {
        this.billingHandler = handler;
    }
    
    public MarketBillingHandler getBillingHandler()
    {
        return this.billingHandler;
    }
    
    public void setExitMsg(String exitMsg)
    {
        this.exitMsg = exitMsg;
    }
    
    public boolean isES2Supported()
    {
        if (this.application != null)
        {
            return this.application.isES2Supported();
        }
        return false;
    }
    
    public void setBackgroundDrawable(Drawable drawable)
    {
        this.application.setBackgroundDrawable(drawable);
    }

    public void setBackgroundDrawableResource(int resid)
    {
        this.application.setBackgroundDrawableResource(resid);        
    }
    
    public void setBrightness(float brightness)
    {
        this.application.setBrightness(brightness);
    }
    
  //move get carrier name to local by using Android API. 
    //You can check SVN for previous implementation which use 3rd service to get carrier name.  
    public void retrieveCarrier()
    {
        boolean hasCarrier = false;
        String carrier = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().deviceCarrier;
        if (carrier != null && carrier.trim().length() > 0)
        {
            hasCarrier = true;
        }
        if (!hasCarrier)
        {
            try
            {
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

                if (deviceCarrier != null && deviceCarrier.trim().length() > 0)
                {
                    deviceCarrier = deviceCarrier.toUpperCase();
                    StringMap carrierMapping = DaoManager.getInstance().getSimpleConfigDao().getCarrierMapping();
                    if (carrierMapping.get(deviceCarrier) != null)
                    {
                        carrier = carrierMapping.get(deviceCarrier);
                    }
                }
            }
            catch (Exception e)
            {
                // send empty carrier name according to agreement with Billing.
                carrier = "";
            }

            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().deviceCarrier = carrier;
            DaoManager.getInstance().getMandatoryNodeDao().store();
        }
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SplashScreenJob.java
 *
 */
package com.telenav.module.entry;

import com.telenav.app.AbstractPlatformIniter;
import com.telenav.app.NavServiceManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.ThreadManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.data.dao.misc.SecretSettingDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.gps.GpsService;
import com.telenav.location.AbstractTnMviewerLocationProvider;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.location.MockLocationProvider;
import com.telenav.module.region.RegionUtil;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.ResourceManager;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;
import com.telenav.ui.citizen.map.GlResManager;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */
public class SplashScreenJob implements IJob
{
    private Object mutex = new Object();
    
    private boolean isCanceled;
    private boolean isRunning;
    
    private EntryModel model;
    private long timeStamp = 0;
    private int timeout;
    
    public static int mapWidth;
    public static int mapHeight;
    public static int mapWidthLandscape;
    public static int mapHeightLandscape;
    
    
    public SplashScreenJob(EntryModel model, int timeout)
    {
        this.model = model;
        timeStamp = System.currentTimeMillis();
        this.timeout = timeout;
    }
    
    public void cancel()
    {
        synchronized (mutex)
        {
            isCanceled = true;
            mutex.notify();
        }
    }

    public void execute(int handlerID)
    {
        isRunning = true;
        try
        {
            synchronized (mutex)
            {
                startGpsService();
                
                AppStatusMisLog appStatusMisLog = MisLogManager.getInstance().getFactory().createAppStatusMisLog();
                MisLogManager.getInstance().storeMisLog(appStatusMisLog);
                
                initMadantoryNode();

                Thread lazyLoader = new Thread(new Runnable()
                {
                    public void run()
                    {
                        initMapContainer();
                    }
                });
                lazyLoader.start();
                
                preload();
                
                this.model.checkBackBillingNotify();
                
                while (!MapContainer.getInstance().isMapReady())
                {
                    synchronized (this)
                    {
                        try
                        {
                            this.wait(50);
                        }catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
                
                if (System.currentTimeMillis() - timeStamp < timeout)
                {
                    mutex.wait(timeout - (System.currentTimeMillis() - timeStamp));
                }
                
                if (!SplashScreenInflater.getInstance().hasMiniMapSize())
                {
                    mapHeight = SplashScreenInflater.getInstance().getMiniMapHeight();
                    mapWidth = SplashScreenInflater.getInstance().getMiniMapWidth();
                    mapHeightLandscape = SplashScreenInflater.getInstance().getLandscapeMiniMapHeight();
                    mapWidthLandscape = SplashScreenInflater.getInstance().getLandscapeMiniMapWidth();
                    
                    Logger.log(Logger.INFO, this.getClass().getName(), "Fake Map-------first time fetch:  mapHeight = "
                            + mapHeight + " ; mapWidth = " + mapWidth);
                    
                    DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_HEIGHT, mapHeight);
                    DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_WIDTH, mapWidth);
                    DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND, mapHeightLandscape);
                    DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND, mapWidthLandscape);
                    DaoManager.getInstance().getSimpleConfigDao().store();
                }
                
                this.model.startFlow();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            isRunning = false;
        }
    }

    public boolean isCancelled()
    {
        return isCanceled;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    private void preload()
    {
        boolean hasLogin = DaoManager.getInstance().getBillingAccountDao().isLogin();
        
        if (!hasLogin)
        {
            preloadCServerRes();
            preload_Carrier_Mapping();
        }
        
        DaoManager.getInstance().getAndroidBillingDao().load();
        
        TeleNavDelegate.getInstance().retrieveCarrier();
        
        if (NavServiceManager.getNavService() != null)
        {
            NavServiceManager.getNavService().setLogEnabled(AppConfigHelper.isLoggerEnable);
            NavServiceManager.getNavService().setLogEnabled(AppConfigHelper.isOutputSdCardEnabled);
        }
        
        ThreadPool threadPool = ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION);
        threadPool.addJob(new PreloadJob(hasLogin));
        threadPool.addJob(new CrashfileCollectingJob());
    }
    
    protected void preloadCServerRes()
    {
        /*preload_SDP();*/
        preload_DSR_SDP();
        preload_CServer_Node();
        preload_audio_rule();
        //checkMethodForDebug();
    }
    
    protected void preload_Carrier_Mapping()
    {
        String pathInAsset = ResourceManager.getInstance().getCurrentBundle().getDataPath("carrierMapping", true) + "TN70_scout_us_carrier_mapping.bin";
        String destPath = "TN70_scout_us_carrier_mapping.bin";
        boolean isSucc = FileStoreManager.copyFileFromAssetToApp(pathInAsset, destPath);
        DaoManager.getInstance().getSimpleConfigDao().load();
        Logger.log(Logger.INFO, this.getClass().getName(), "==preload_Carrier_Mapping== " + isSucc);
    }
    
    protected void checkMethodForDebug()
    {
        String version = DaoManager.getInstance().getServerDrivenParamsDao().getVersion("US");
        
        version = null;
        if (DaoManager.getInstance().getDsrServerDrivenParamsDao().getServerParams() != null)
        {
            version = DaoManager.getInstance().getDsrServerDrivenParamsDao().getServerParams().get(ServerDrivenParamsDao.SERVER_DRIVEN_VERSION);
        }
        
        version = DaoManager.getInstance().getResourceBarDao().getCategoryVersion("US");
        
        version = DaoManager.getInstance().getResourceBarDao().getAirportVersion();
        
        version = DaoManager.getInstance().getResourceBarDao().getBrandNameVersion("US");

        version = DaoManager.getInstance().getResourceBarDao().getHotPoiVersion("US");

        version = DaoManager.getInstance().getResourceBarDao().getPreferenceSettingVersion("US");
    }
    
    protected void preload_audio_rule()
    {
        boolean isSucc = FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_audio_rule_preload.bin");
        if (isSucc)
        {
            ((DaoManager) DaoManager.getInstance()).preloadAudioRule();
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "==audio_rule_preload== " + isSucc);
    }

    protected void preload_CServer_Node()
    {
        boolean isSucc = FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_backup_preference_static_data_US.bin");
//        isSucc = FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_server_driven_US.bin");
        isSucc = FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_cserver_node.bin");
        isSucc = FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_cserver_node_US.bin");
        if(isSucc)
        {
            DaoManager.getInstance().getPreferenceDao().load(RegionUtil.getInstance().getCurrentRegion());
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "==preload_CServer_Node== " + isSucc);
    }

    protected void preload_DSR_SDP()
    {
        boolean isSucc = FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_dsr_dsp.bin");
        Logger.log(Logger.INFO, this.getClass().getName(), "==preload_DSR_SDP== " + isSucc);
    }

    protected void preload_SDP()
    {
        boolean isSucc = FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_server_driven.bin");       
        isSucc |= FileStoreManager.copyFileFromAssetToApp("TN70_scout_us_server_driven_US.bin");       
        if(isSucc)
            DaoManager.getInstance().getServerDrivenParamsDao().reload("US");
        Logger.log(Logger.INFO, this.getClass().getName(), "==preload_SDP== " + isSucc);
    }

    public static void initMadantoryNode()
    {
        String clientVersion = AppConfigHelper.mandatoryClientVersion;
        String productName = AppConfigHelper.mandatoryProductName;
        String programCode = AppConfigHelper.mandatoryProgramCode;
        TnDeviceInfo deviceInfo = TnTelephonyManager.getInstance().getDeviceInfo();
        if(deviceInfo.platform == TnDeviceInfo.PLATFORM_ANDROID)
        {
            AppConfigHelper.platform = AppConfigHelper.PLATFORM_ANDROID;
        }
        else if(deviceInfo.platform == TnDeviceInfo.PLATFORM_RIM)
        {
            AppConfigHelper.platform = AppConfigHelper.PLATFORM_RIM;
        }
        
        if (isNeedClearAccount())
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, SplashScreenJob.class.getName(), " ********* Clear data since we haven't login ********* ");
            }
            
            DaoManager.getInstance().getMandatoryNodeDao().clear();
            DaoManager.getInstance().getBillingAccountDao().clear();
            
            DaoManager.getInstance().getMandatoryNodeDao().store();
            DaoManager.getInstance().getBillingAccountDao().store();
        }
        
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().programCode = programCode;
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().platform = AppConfigHelper.platform;
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().version = clientVersion;
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().buildNumber = AppConfigHelper.buildNumber;
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().device = deviceInfo.deviceName;
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().gpsTpye = AppConfigHelper.GPS_TYPE_AGPS;

        if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region == null
         || DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region.trim().length() == 0)
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region = AppConfigHelper.defaultRegion;
        }
        
        if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().locale == null
                || DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().locale.trim().length() == 0)
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().locale = ResourceManager.getInstance().getCurrentLocale();
        }
        else
        {
            ResourceManager.getInstance().setLocale(DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().locale);
        }

        if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().guideTone == null
                || DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().guideTone.trim().length() == 0)
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().guideTone = AppConfigHelper.GUIDE_TONE_US_FEMAIL;
        }
        
        if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().productType == null
                || DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().productType.trim().length() == 0)
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().productType = productName;
        }
        
        if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().audioFormat == null
                || DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().audioFormat.trim().length() == 0)
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().audioFormat = AppConfigHelper.AUDIO_MP3HI;
        }
        
        
        if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().imageType == null
                || DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().imageType.trim().length() == 0)
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().imageType = AppConfigHelper.IMAGE_PNG;
        }
              
        String currentVersion = DaoManager.getInstance().getStartupDao().getCurrentAppVersion();
        String currentBuildNumber = DaoManager.getInstance().getStartupDao().getBuildNumber();
        if(currentVersion == null || currentVersion.trim().length() <= 0
        		|| currentBuildNumber == null || currentBuildNumber.trim().length() <= 0)
        {
            DaoManager.getInstance().getStartupDao().setCurrentAppVersion(
                DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().version);
			DaoManager.getInstance().getStartupDao().setBuildNumber(DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().buildNumber);
            DaoManager.getInstance().getStartupDao().store();
        }
    }

    protected static boolean isNeedClearAccount()
    {
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, SplashScreenJob.class.getName(), " ********* Account Status :  ********* " + DaoManager.getInstance().getBillingAccountDao().getAccountStatus());
        }
        
        boolean isNeedClearAccount = (
                (!DaoManager.getInstance().getBillingAccountDao().isLogin()
                && DaoManager.getInstance().getBillingAccountDao().getAccountStatus() != BillingAccountDao.ACCOUNT_STATUS_EXPIRED) 
                && !DaoManager.getInstance().getBillingAccountDao().hasHackPtn()
                );
        return isNeedClearAccount;
    }
    
    protected void startGpsService()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "", null, new Object[] {com.telenav.log.ILogConstants.BEFORE_START_GPS_SERVICE}, true);
        
        int source = ((DaoManager) DaoManager.getInstance()).getSecretSettingDao().get(SecretSettingDao.GPS_SOURCE_SELECT_INDEX);
        if(source < 0)
            source = ICommonConstants.GPS_JSR179;
        String provider = null;
        switch(source)
        {
            case ICommonConstants.GPS_JSR179:
            {
                provider = TnLocationManager.GPS_179_PROVIDER;
                break;
            }
            case ICommonConstants.GPS_BLUETOOTH_PUSH:
            {
                provider = TnLocationManager.GPS_179_PROVIDER;
                break;
            }
            case ICommonConstants.GPS_MVIEWER_TOOL:
            {
                provider = TnLocationManager.MVIEWER_PROVIDER;
                break;
            }
            case ICommonConstants.GPS_ALONG_ROUTE:
            {
                provider = TnLocationManager.ALONGROUTE_PROVIDER;
                break;
            }
            case ICommonConstants.GPS_REPLAY:
            {
                provider = TnLocationManager.REPLAY_PROVIDER;
                break;
            }
            default:
            {
                provider = TnLocationManager.GPS_179_PROVIDER;
                break;
            }
        }

        Logger.log(Logger.INFO, this.getClass().getName(), "", null, new Object[] {com.telenav.log.ILogConstants.AFTER_START_GPS_SERVICE}, true);
        
        if(source == ICommonConstants.GPS_MVIEWER_TOOL)
        {
            String host = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_MVIEWER_HOST_IP);
            if(host != null && host.trim().length() > 0)
            {
                AbstractTnMviewerLocationProvider mviewer = (AbstractTnMviewerLocationProvider) TnLocationManager.getInstance().getProvider(
                    TnLocationManager.MVIEWER_PROVIDER);
                
                mviewer.setSocketHost(host, 11159);
            }
        }

        if (TnLocationManager.GPS_179_PROVIDER.equals(provider))
        {
            AbstractPlatformIniter.getInstance().initLocationProvider();
        }
        else
        {
            GpsService gpsService = new GpsService(provider, null);
            TnLocation defaultLocation = LocationProvider.getInstance()
                    .getInitialLocation();
            MockLocationProvider mockLocationProvider = new MockLocationProvider(
                    defaultLocation, gpsService);
            LocationProvider.getInstance().init(mockLocationProvider);
        }
        
        LocationProvider.getInstance().start();
    }
    
    protected void initMapContainer()
    {
        GlResManager.getInstance().initOpenglResource();
        MapContainer.getInstance().initMapEngine();
        MapContainer.getInstance().changeToSpriteVehicleAnnotation();
        MapContainer.getInstance().updateMapColor();
        MapContainer.getInstance().updateMapColorMode();
    }
}

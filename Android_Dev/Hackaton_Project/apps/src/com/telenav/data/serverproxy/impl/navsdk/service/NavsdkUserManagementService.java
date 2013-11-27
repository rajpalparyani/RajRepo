/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NavsdkUserManagementService.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.service;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import android.os.Environment;
import android.os.StatFs;

import com.telenav.app.CommManager;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.ServiceLocator;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.ClientInfo;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserPrefers;
import com.telenav.data.datatypes.map.MapDataUpgradeInfo;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkUserManagementProxyHelper;
import com.telenav.data.serverproxy.impl.protobuf.AbstractProtobufServerProxy;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.UserManagementData;
import com.telenav.navsdk.events.UserManagementData.FeatureStatus;
import com.telenav.navsdk.events.UserManagementData.Service;
import com.telenav.navsdk.events.UserManagementEvents.DebugSettingsRequest;
import com.telenav.navsdk.events.UserManagementEvents.FeatureListUpdateRequest;
import com.telenav.navsdk.events.UserManagementEvents.ServiceLocatorUpdateRequest;
import com.telenav.navsdk.events.UserManagementEvents.UserProfileUpdateRequest;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 * @author pwang
 * @date 2013-1-9
 */
public class NavsdkUserManagementService
{
    private static NavsdkUserManagementService instance;

    private NavSdkUserManagementProxyHelper helper;

    Hashtable updatedActionUrl = new Hashtable();

    private static String KEY_SIMULATOR_NAV = "SimulateNav";

    private static String KEY_ENABLE_LOG = "EnableLog";

    private static String KEY_CN_MAPDOWNLOAD_SERVER_ENABLE = "MapDownloadServer";
    
    private static final String KEY_FILE_CACHE_SIZE_FOR_OFFBOARD_MAP = "FileCacheSizeForOffboardMap";

    private static String VALUE_OPTION_ENABLE = "1";

    private static String VALUE_OPTION_DISABLE = "0";

    private static String VALUE_CN_MAPDOWNLOAD_SERVER_ENABLE = "CN";

    private static String ACTION_SERVICELOCATOR_MAP_DATA_UPGRADE = "MapRegionId:";

    private static String TTS_PATH = "resources/tts/data";
    
    private static String SLP_TTS_PATH = "tts/data";

    private static String NAV_SDK_TTS_ACTION_NAME = "ttsClientRes";
    
    private static String KEY_SEARCH_ALONG_RANGE = "SearchAlongRange";
    
    private static String KEY_DISABLE_ROUTES_IN_SATELLITEVIEW = "DisableRoutesInSatelliteView";
    private static String KEY_ALONGROUTE_SPEED = "SimulateNavSpeed";
    
    public static final int INITIAL_SEARCH_RADIUS_CAT          = 5;    //mile
    public static final int MILE_TO_DM5                        = 1446; //1 mile = 1446 dm5
    public static final int DEFAULT_RADIOUS = INITIAL_SEARCH_RADIUS_CAT * MILE_TO_DM5; //5 miles as init dist to search poi
    
    private static final long MAX_MAP_DISK_CACHE = 10 * 1024 * 1024;    // use 10M per PM

    private NavsdkUserManagementService()
    {
        helper = NavSdkUserManagementProxyHelper.getInstance();
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavsdkUserManagementService();
        }
    }

    public static NavsdkUserManagementService getInstance()
    {
        return instance;
    }

    /**
     * Only update the URL not exist in navsdk or has been changed
     */
    public void serviceLocatorUpdate()
    {
        ServiceLocator serviceLocator = (ServiceLocator) CommManager.getInstance().getComm().getHostProvider();
        Vector actions = serviceLocator.getAllActions();
        ServiceLocatorUpdateRequest.Builder builder = ServiceLocatorUpdateRequest.newBuilder();
        for (int i = 0; i < actions.size(); i++)
        {
            String action = (String) actions.elementAt(i);
            String url = serviceLocator.getActionUrl(action);
            if (url == null || url.trim().length() == 0)
            {
                continue;
            }
            if (updatedActionUrl.get(action) != null && updatedActionUrl.get(action).equals(url))
            {
                continue;
            }
            Service.Builder serviceBuilder = Service.newBuilder();
            serviceBuilder.setName(action);
            serviceBuilder.setUrl(url);
            updatedActionUrl.put(action, url);
            builder.addServices(serviceBuilder.build());
        }
        
        boolean isMapDownloadCNEnabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
            SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_ENABLE);
        String cnURL = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(
            SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_URL);
        Vector mapDataUpgradeInfos = ((DaoManager) DaoManager.getInstance()).getResourceBarDao().getMapDataUpgradeInfo();

        if (mapDataUpgradeInfos != null && mapDataUpgradeInfos.size() > 0)
        {
            for (int i = 0; i < mapDataUpgradeInfos.size(); i++)
            {
                MapDataUpgradeInfo mapDataUpgradeInfo = (MapDataUpgradeInfo) mapDataUpgradeInfos.elementAt(i);
                if (mapDataUpgradeInfo != null && mapDataUpgradeInfo.getUrl() != null
                        && mapDataUpgradeInfo.getUrl().trim().length() > 0 && mapDataUpgradeInfo.getRegion() != null
                        && mapDataUpgradeInfo.getRegion().trim().length() > 0)
                {
                    String action = ACTION_SERVICELOCATOR_MAP_DATA_UPGRADE + mapDataUpgradeInfo.getRegion();
                    String url = mapDataUpgradeInfo.getUrl();
                    
                    if (isMapDownloadCNEnabled && cnURL.trim().length() > 0)
                    {
                        url = getCnURL(url);
                    }
                    if (updatedActionUrl.get(action) == null || !updatedActionUrl.get(action).equals(url))
                    {
                        Service.Builder serviceBuilder = Service.newBuilder();
                        serviceBuilder.setName(ACTION_SERVICELOCATOR_MAP_DATA_UPGRADE + mapDataUpgradeInfo.getRegion());
                        serviceBuilder.setUrl(url);
                        updatedActionUrl.put(ACTION_SERVICELOCATOR_MAP_DATA_UPGRADE + mapDataUpgradeInfo.getRegion(),
                            url);
                        builder.addServices(serviceBuilder.build());
                    }
                }
            }

            addTtsService(builder);
        }

        if (builder.getServicesCount() > 0)
        {
            helper.requestServiceLocatorUpdate(builder.build());
        }
    }
    
    public static String getCnURL(String url)
    {
        String cnURL = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(
            SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_URL);
        
        if(url == null || url.trim().length() == 0)
        {
            return url;
        }
        
        StringBuilder sb = new StringBuilder(url);
        
        int hostIndex = sb.indexOf("://");
        
        if(hostIndex == -1)
        {
            hostIndex = 0;
        }
        else
        {
            hostIndex += 3;
        }
        
        if (hostIndex >= sb.length())
        {
            return url;
        }
        
        int hostEndIndex = sb.indexOf("/", hostIndex);
        
        if (hostEndIndex == -1)
        {
            hostEndIndex = sb.length();
        }
        
        if(hostEndIndex > hostIndex)
        {
            sb.replace(hostIndex, hostEndIndex, cnURL);
        }
        else
        {
            sb.insert(hostIndex, cnURL);
        }
        
        return sb.toString();
    }

    private void addTtsService(ServiceLocatorUpdateRequest.Builder builder)
    {
        String ttsActionName = NAV_SDK_TTS_ACTION_NAME;
        Host ttsHost = CommManager.getInstance().getComm().getHostProvider().createHost(IServerProxyConstants.ACT_GET_TTS_URL);
        Comm comm = CommManager.getInstance().getComm();
        String ttsUrl = "";

        if (ttsHost != null && comm != null)
        {
            ttsUrl = comm.getHostProvider().getUrl(ttsHost) + SLP_TTS_PATH;
        }
        else
        {
            ttsUrl = "http://clientresourcecdn.telenav.com/tts/resources/1.0/tts/data/";
        }

        if (updatedActionUrl.get(ttsActionName) == null || !updatedActionUrl.get(ttsActionName).equals(ttsUrl))
        {
            Service.Builder serviceBuilder = Service.newBuilder();
            serviceBuilder.setName(ttsActionName);
            serviceBuilder.setUrl(ttsUrl);
            builder.addServices(serviceBuilder.build());
            updatedActionUrl.put(ttsActionName, ttsUrl);
        }
    }

    private String createFilePath()
    {
        MandatoryProfile profile = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();
        ClientInfo clientInfo = profile.getClientInfo();
        StringBuilder builder = new StringBuilder("/");
        builder.append(clientInfo.version);
        builder.append("/");
        builder.append(clientInfo.platform);
        builder.append("/");
        builder.append(TTS_PATH);
        builder.append("/");
        return builder.toString();
    }

    /**
     * update the specific action and url
     */
    public void serviceLocatorUpdate(String[] action, String[] url)
    {
        int size = action.length;
        ServiceLocatorUpdateRequest.Builder builder = ServiceLocatorUpdateRequest.newBuilder();
        for (int i = 0; i < size; i++)
        {
            Service.Builder serviceBuilder = Service.newBuilder();
            String name = ACTION_SERVICELOCATOR_MAP_DATA_UPGRADE + action[i];
            String urlValue = url[i];
            if (updatedActionUrl.get(name) == null || !updatedActionUrl.get(name).equals(url))
            {
                serviceBuilder.setName(ACTION_SERVICELOCATOR_MAP_DATA_UPGRADE + action[i]);
                serviceBuilder.setUrl(urlValue);
                builder.addServices(serviceBuilder);
                updatedActionUrl.put(name, urlValue);
            }
        }
        addTtsService(builder);
        if (builder.getServicesCount() > 0)
        {
            helper.requestServiceLocatorUpdate(builder.build());
        }
    }

    public void featureListUpdate()
    {
        FeatureListUpdateRequest.Builder requestBuilder = FeatureListUpdateRequest.newBuilder();
        StringMap featureList = ((DaoManager) DaoManager.getInstance()).getUpsellDao().getFeatureList();

        Enumeration keyEnum = featureList.keys();
        int featureValue = -1;
        while (keyEnum.hasMoreElements())
        {
            UserManagementData.Feature.Builder featureBuilder = UserManagementData.Feature.newBuilder();

            String featureName = (String) keyEnum.nextElement();
            featureValue = FeaturesManager.getInstance().getStatus(featureName);
            featureBuilder.setName(featureName);
            switch (featureValue)
            {
                case FeaturesManager.FE_ENABLED:
                case FeaturesManager.FE_PURCHASED:
                {
                    featureBuilder.setStatus(FeatureStatus.FeatureStatus_Available);
                    break;
                }
                case FeaturesManager.FE_DISABLED:
                {
                    featureBuilder.setStatus(FeatureStatus.FeatureStatus_Unavailable);
                    break;
                }
                case FeaturesManager.FE_UNPURCHASED:
                {
                    featureBuilder.setStatus(FeatureStatus.FeatureStatus_NeedPurchase);
                    break;
                }
                default:
                {
                    break;
                }
            }
            requestBuilder.addFeatures(featureBuilder.build());
        }
        helper.requestFeatureListUpdate(requestBuilder.build());
    }

    public void userProfileUpdate()
    {
        UserProfileUpdateRequest.Builder builder = UserProfileUpdateRequest.newBuilder();
        MandatoryProfile mandatoryProfile = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();
        UserInfo userInfo = mandatoryProfile.getUserInfo();
        ClientInfo clientInfo = mandatoryProfile.getClientInfo();
        UserPrefers userPrefers = mandatoryProfile.getUserPrefers();

        builder.setMin(userInfo.phoneNumber);
        builder.setUserId(userInfo.userId);
        builder.setPassword(userInfo.pin);
        builder.setEqpin(userInfo.eqpin);
        builder.setLocale(userInfo.locale);
        builder.setRegion(userInfo.region);
        builder.setSsoToken(userInfo.ssoToken);
        builder.setGuideTone(userInfo.guideTone);

        builder.setProgramCode(clientInfo.programCode);
        builder.setProduct(clientInfo.productType);
        builder.setPlatform(clientInfo.platform);
        builder.setDevice(clientInfo.device);
        builder.setVersion(clientInfo.version);
        builder.setBuildNumber(clientInfo.buildNumber);
        builder.setGpsType(clientInfo.gpsTpye);

        builder.setAudioFormat(userPrefers.audioFormat);
        builder.setImageType(userPrefers.imageType);
        builder.setAudioLevel(userPrefers.audioLevel);
        builder.setDataProcessType("");
        builder.setScreenWidth("" + ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
        builder.setScreenHeight("" + ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight());

        int ptnType = 2;
        if (userInfo.ptnType != null && userInfo.ptnType.trim().length() > 0)
        {
            try
            {
                ptnType = Integer.valueOf(userInfo.ptnType);
            }
            catch (Exception e)
            {
                Logger.log(AbstractProtobufServerProxy.class.getName(), e);
            }
        }
        builder.setPtnSource(ptnType);
        builder.setDeviceCarrier(clientInfo.deviceCarrier);

        TnDeviceInfo deviceInfo = TnTelephonyManager.getInstance().getDeviceInfo();
        if (deviceInfo != null)
        {
            deviceInfo.deviceId = deviceInfo.deviceId == null ? "" : deviceInfo.deviceId;
            builder.setDeviceId(deviceInfo.deviceId);
        }

        builder.setMacID("");
        builder.setOpenUdid("");// It is for IOS platform
        helper.requestUserProfileUpdateRequest(builder.build());
    }

    public void debugSettingUpdate()
    {
        // set simulator nav enable
        DebugSettingsRequest.Builder builder = DebugSettingsRequest.newBuilder();
        UserManagementData.DebugOption.Builder simulatorNavOptionBuilder = UserManagementData.DebugOption.newBuilder();
        String gpsType = LocationProvider.getInstance().getGpsServiceType();
        boolean isSimulatorEnable = TnLocationManager.ALONGROUTE_PROVIDER.equalsIgnoreCase(gpsType) ? true : false;
        simulatorNavOptionBuilder.setKey(KEY_SIMULATOR_NAV);
        if (isSimulatorEnable)
        {
            simulatorNavOptionBuilder.setValue(VALUE_OPTION_ENABLE);
        }
        else
        {
            simulatorNavOptionBuilder.setValue(VALUE_OPTION_DISABLE);
        }

        // set log enable
        UserManagementData.DebugOption.Builder loggerEnabledOptionBuilder = UserManagementData.DebugOption.newBuilder();
        boolean isLoggerSetted = AppConfigHelper.isLoggerEnable ? true : false;
        loggerEnabledOptionBuilder.setKey(KEY_ENABLE_LOG);
        if (isLoggerSetted)
        {
            loggerEnabledOptionBuilder.setValue(VALUE_OPTION_ENABLE);
        }
        else
        {
            loggerEnabledOptionBuilder.setValue(VALUE_OPTION_DISABLE);
        }

        // set CN map download
        UserManagementData.DebugOption.Builder cnMapDownloadServerEnableBuilder = UserManagementData.DebugOption.newBuilder();
//        boolean isMapDownloadCNEnabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
//            SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_ENABLE);
//        cnMapDownloadServerEnableBuilder.setKey(KEY_CN_MAPDOWNLOAD_SERVER_ENABLE);
//        if (isMapDownloadCNEnabled)
//        {
//            cnMapDownloadServerEnableBuilder.setValue(VALUE_CN_MAPDOWNLOAD_SERVER_ENABLE);
//        }

        if (!AppConfigHelper.isMapDiskCacheDisabled())
        {
            UserManagementData.DebugOption.Builder mapDiskCacheBuilder = UserManagementData.DebugOption.newBuilder();
            mapDiskCacheBuilder.setKey(KEY_FILE_CACHE_SIZE_FOR_OFFBOARD_MAP);
            long cacheSize;
            long available = getAvailableBytes();
            long total = getTotalBytes();
            if (isStorageLow(available, total))
            {
                cacheSize = 0;
            }
            else
            {
                long actualAvailable = available - total / 10;
                // no more than one-third of the available storage size, max size: 10 MB
                cacheSize = Math.min(actualAvailable / 3, MAX_MAP_DISK_CACHE);
            }
            mapDiskCacheBuilder.setValue(String.valueOf(cacheSize));
            builder.addSettings(mapDiskCacheBuilder.build());
        }
        
        builder.addSettings(simulatorNavOptionBuilder.build());
        builder.addSettings(loggerEnabledOptionBuilder.build());
        builder.addSettings(cnMapDownloadServerEnableBuilder.build());

        UserManagementData.DebugOption.Builder searchAlongRadiusBuilder = UserManagementData.DebugOption.newBuilder();
        searchAlongRadiusBuilder.setKey(KEY_SEARCH_ALONG_RANGE);
        searchAlongRadiusBuilder.setValue(String.valueOf(DEFAULT_RADIOUS));      
        builder.addSettings(searchAlongRadiusBuilder.build());

        UserManagementData.DebugOption.Builder disableRoutesInSatelliteView = UserManagementData.DebugOption.newBuilder();
        disableRoutesInSatelliteView.setKey(KEY_DISABLE_ROUTES_IN_SATELLITEVIEW);
        disableRoutesInSatelliteView.setValue(String.valueOf(AppConfigHelper.isDisableRoutesInSatelliteView()?1:0));      
        builder.addSettings(disableRoutesInSatelliteView.build());
        
        UserManagementData.DebugOption.Builder alongrouteSpeed = UserManagementData.DebugOption.newBuilder();
        alongrouteSpeed.setKey(KEY_ALONGROUTE_SPEED);
        alongrouteSpeed.setValue(AppConfigHelper.getAlongrouteSpeed());      
        builder.addSettings(alongrouteSpeed.build());
        
        helper.requestDebugSettingUpdate(builder.build());
    }
    
    private long getAvailableBytes()
    {
        File root = Environment.getDataDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }
    
    private long getTotalBytes()
    {
        File root = Environment.getDataDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long allCount = sf.getBlockCount();
        return allCount * blockSize;
    }

    // if storage available is less than 10% of the total volume, storage is low
    private boolean isStorageLow(long availableBytes, long totalBytes)
    {
        return availableBytes <= totalBytes / 10;
    }

}

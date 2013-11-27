/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AppConfigHelper.java
 *
 */
package com.telenav.module;

import java.io.IOException;
import java.io.InputStream;

import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;
import com.telenav.res.IAudioRes;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public class AppConfigHelper
{
    public final static String BRAND_SCOUT_US = "scout_us";
    
    public final static String BRAND_SCOUT_EU = "scout_eu";
    
    public final static String BRAND_ATT = "cingular";

    public final static String BRAND_USCC = "uscc";
    
    public final static String BRAND_VIVO = "vivo";
    
    public final static String BRAND_TELCEL = "telcel";
    
    public final static String BRAND_BELL = "bell";
    
    public final static String BRAND_ROGERS = "rogers";
    
    public final static String BRAND_SPRINT = "sprint";
    
    public final static String BRAND_TMOBILEUK = "t-mobile-uk";
    
    public final static String BRAND_TMOBILEUS = "t-mobile-us";

    public final static String BRAND_TN = "tn";
    
    public final static String BRAND_MMI = "mmi";
    
    public final static String AUDIO_MP3 = "mp3";
    
    public final static String AUDIO_MP3HI = "mp3hi";
    
    public final static String IMAGE_PNG = "PNG";
    
    public final static String PLATFORM_ANDROID = "ANDROID";
    
    public final static String PLATFORM_RIM = "RIM";
    
    public final static String GUIDE_TONE_US_FEMAIL = "203";
    
    public final static String GPS_TYPE_AGPS = "AGPS";
    
    public static final int MAIN_VERSION = 7;  //7.3's mainVersion is 7
    
    public static final int SUB_VERSION = 3;   //7.3's subVersion is 3
    
    /**
     * Retrieve the brand name of application.
     */
    public static String brandName;

    /**
     * Retrieve the app name of application.
     */
    public static String appName;
    
    /**
     * Retrieve the audio type.
     */
    public static String audioType;
    
    /**
     * Retrieve the application version.
     */
    public static String appVersion;
    
    /**
     * Retrieve the device carrier.
     */
    public static String deviceCarrier;
    
    /**
     * Retrieve the build number.
     */
    public static String buildNumber;
    
    /**
     * Retrieve the audio file's timestamp.
     */
    public static String audioFileTimestamp;
    
    /**
     * Retrieve the audio file's inventory.
     */
    public static String audioFileInventory;
    
    /**
     * Retrieve the default locale
     */
    public static String defaultLocale;
    
    /**
     * region code read for config file.
     */
    public static String defaultRegion; 
    
    /**
     * Retrieve supported locales for config file.
     */
    public static String localesList;
    
    /*
     * android:minSdkVersion
     */
    public static int minSdkVersion = 8;
    
    /*
     * android:speedxEncorderLib version
     */
    public static String speedxEncorderLib;
    
    public static int actionBarHeight = 0;
    
    /**
     * client version read from config file.
     */
    public static String mandatoryClientVersion;
    
    /**
     * client version read from config file.
     */
    public static String mandatoryProductName;
    
    /**
     * client version read from config file.
     */
    public static String mandatoryProgramCode;
    
    /**
     * specific the platform
     */
    public static String platform;
    
    /**
     * specific the apiKey
     */
    public static String apiKey;
    
    /**
     * Device user agent
     */
    public static String deviceUserAgent;
    
    /**
     * isLogger enable
     */
    public static boolean isLoggerEnable;
    
    /**
     * isOutputSdCard enable
     */
    public static boolean isOutputSdCardEnabled;
    
    /**
     * Chunk protocol enable
     */
    public static boolean isChunkEnabled = true;
    
    public static boolean isReadMiniMapFromWebService = false;
    
    
    public static String audioFamily = IAudioRes.FAMILY_MP3HI;
    
    public static boolean isNeedRegionDetection = false;
    
    private static boolean isLoaded = false;
    
    /**
     * Tablet has special UI setting.
     */
    private static boolean isTablet = false;
    
    /**
     * QVGA has special UI setting.
     */
    private static boolean isQVGA = false;
    
    private static String skipSMSPTNDiscovery = "";
    
    private static boolean isNeedPtnVerification = false;
    
    private static int screenWidthForBrowser;
    
    private static int screenHeightForBrowser;
    
    private static boolean isStuckMonitorEnable = false;
    
    private static boolean isMapDiskCacheDisabled = false;
    
    private static boolean isDisableRoutesInSatelliteView = false;
    
    private static String alongrouteSpeed;
    
    static
    {
		/*double xSize = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth()
				/ (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT ? 
						((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getXdpi(): 
							((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getYdpi());
		double ySize = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight()
				/ (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT ?
						((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getYdpi(): 
							((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getXdpi());

		float tabletSize = 7;
		int osVersion = android.os.Build.VERSION.SDK_INT;
        //Android 3.2 or later
        if (osVersion >= 13)
        { 
        	tabletSize = 6.75f;// magic number for 7 inch 3.2 os tablet
        }
        isTablet = Math.sqrt(Math.pow(xSize, 2) + Math.pow(ySize, 2)) >= tabletSize;*/
        
        int maxDisplaySize = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
        
        if(maxDisplaySize <= 320)
        {
            isQVGA = true;
        }
        
        if(maxDisplaySize <= 800)
        {
            isTablet = false; 
        }
        
        screenWidthForBrowser = Math.min(getDisplayWidth(), getScreenHeight());
        screenHeightForBrowser = Math.max(getDisplayWidth(), getScreenHeight());
    }
    
    /**
     * load the config's properties
     */
    public static void load()
    {
        InputStream configInputStream = null;
        try
        {
            if(isLoaded)
                return;
            
            configInputStream = TnIoManager.getInstance().openFileFromAppBundle("appConfig.dat");
            TnProperties configProperties = TnIoManager.getInstance().createProperties();
            configProperties.load(configInputStream);
            
            brandName = configProperties.getProperty("BRAND_NAME");
            if(brandName != null)
                brandName = brandName.trim();
            
            
            appName = configProperties.getProperty("MD_APP_NAME");
            if(appName != null)
                appName = appName.trim();
            
            audioType = configProperties.getProperty("AUDIO_TYPE");
            if(audioType != null)
                audioType = audioType.trim();
            
            buildNumber = configProperties.getProperty("APP_BUILD_NUMBER");
            if(buildNumber != null)
                buildNumber = buildNumber.trim();
            
            audioFileTimestamp = configProperties.getProperty("MD_AUDIO_FILE_TIMESTAMP");
            if(audioFileTimestamp != null)
            {
                audioFileTimestamp = audioFileTimestamp.trim();
                int sepIndex = audioFileTimestamp.indexOf('|');
                if(sepIndex > 0 && sepIndex < audioFileTimestamp.length() - 1)
                {
                    audioFamily = audioFileTimestamp.substring(sepIndex + 1);
                }
            }
            
            audioFileInventory = "";
            if(audioFileInventory != null)
                audioFileInventory = audioFileInventory.trim();
            
            mandatoryClientVersion = configProperties.getProperty("MD_MANDATORY_CLIENT_VERSION");
            if(mandatoryClientVersion != null)
                mandatoryClientVersion = mandatoryClientVersion.trim();
            
            mandatoryProductName = configProperties.getProperty("MD_MANDATORY_PRODUCT_NAME");
            if(mandatoryProductName != null)
                mandatoryProductName = mandatoryProductName.trim();
            
            mandatoryProgramCode = configProperties.getProperty("MD_MANDATORY_PROGRAM_CODE");
            if(mandatoryProgramCode != null)
                mandatoryProgramCode = mandatoryProgramCode.trim();
            
            defaultLocale = configProperties.getProperty("MD_DEFAULT_LOCALE");
            if(defaultLocale != null)
            	defaultLocale = defaultLocale.trim();
            
            defaultRegion = configProperties.getProperty("MD_DEFAULT_REGION");
            if(defaultRegion != null)
                defaultRegion = defaultRegion.trim();
            
            String chunkEnable = configProperties.getProperty("MD_CHUNK_ENABLE");
            if(chunkEnable != null && chunkEnable.equalsIgnoreCase("false"))
                isChunkEnabled = false;
            
            apiKey = configProperties.getProperty("MD_API_KEY");
            if(apiKey != null)
            	apiKey = apiKey.trim();
            
            localesList = configProperties.getProperty("MD_locales-list");
            if(localesList != null)
                localesList = localesList.trim();
            
            appVersion = configProperties.getProperty("MD_APPLICATION_VERSION");
            if(appVersion != null)
                appVersion = appVersion.trim();
            
            deviceCarrier = configProperties.getProperty("MD_DEVICE_CARRIER");
            if(deviceCarrier != null)
                deviceCarrier = deviceCarrier.trim();
            
            speedxEncorderLib= configProperties.getProperty("MD_speedxEncorderLib");
            if(speedxEncorderLib != null)
                speedxEncorderLib = speedxEncorderLib.trim();
            
            String verificationEnable = configProperties.getProperty("MD_IS_NEED_PTN_VERIFICATION");
            if(verificationEnable != null && verificationEnable.equalsIgnoreCase("true"))
                isNeedPtnVerification = true;
            
            String SdkVersion = configProperties.getProperty("MD_minSdkVersion");
            if(SdkVersion != null)
            {
                try
                {
                    minSdkVersion = Integer.parseInt(SdkVersion);
                }
                catch(Exception e)
                {
                    Logger.log(AppConfigHelper.class.getName(), e);
                    minSdkVersion = 8;
                }
                    
            }
            
            skipSMSPTNDiscovery = configProperties.getProperty("MD_SKIP_SMS_PTN_DISCOVERY");
            if(skipSMSPTNDiscovery != null)
                skipSMSPTNDiscovery = skipSMSPTNDiscovery.trim();
            
            String RegionDetectionEnabled = configProperties.getProperty("MD_IS_NEED_DETECT_REGION");
            if(RegionDetectionEnabled != null && RegionDetectionEnabled.trim().equals("true"))
            {
                isNeedRegionDetection = true;
            }
            
            isLoaded = true;
        }
        catch (IOException e)
        {
            Logger.log(AppConfigHelper.class.getName(), e);
            isLoaded = false;
        }
        finally
        {
            if(configInputStream != null)
            {
                try
                {
                    configInputStream.close();
                }
                catch (IOException e)
                {
                    Logger.log(AppConfigHelper.class.getName(), e);
                }
                finally
                {
                    configInputStream = null;
                }
            }
        }
    }
    
    /**
     * return the width of display, in pixels. Note that this should not generally be 
     * used for computing layouts, since a device will typically have screen decoration 
     * (such as a status bar) along the edges of the display that reduce the amount of 
     * application space available from the raw size returned here. This value is adjusted for you 
     * based on the current rotation of the display. 
     * 
     * @return displayWidth
     */
    public static int getDisplayWidth()
    {
        //the regular expression for searching old getDisplayWidth() is
        //-----   \(\(AbstractTnUiHelper\)\s{0,1}AbstractTnUiHel[\.\w\(\)]*(\r\s*){0,10}[\.\w\(\)]*getDisplayWidth\(\)   -------

        return ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth();
    }
    
    /**
     * return the height of display, in pixels. Note that this should not generally be 
     * used for computing layouts, since a device will typically have screen decoration 
     * (such as a status bar) along the edges of the display that reduce the amount of 
     * application space available from the raw size returned here. This value is adjusted for you 
     * based on the current rotation of the display. 
     * 
     * @return displayHeight
     */
    public static int getScreenHeight()
    {
        return ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight();
    }
    
    /**
     * return the height of display, in pixels. Note that this does not include the top bar height. This value is adjusted for you 
     * based on the current rotation of the display. 
     * 
     * @return displayHeight
     */
    public static int getDisplayHeight()
    {
        int osVersion = android.os.Build.VERSION.SDK_INT;
        int statusBarHeight = 0;
        if(osVersion != 13)
        {
            statusBarHeight = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getStatusBarHeight(AbstractTnUiHelper.STATUS_BAR_TOP);
            if (statusBarHeight <= 0)
               statusBarHeight = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getStatusBarHeight(AbstractTnUiHelper.STATUS_BAR_BOTTOM);
        }
        return ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight() - statusBarHeight - actionBarHeight;
    }
    
    /**
     * return smaller edge length of the screen. 
     * 
     * @return MinDisplaySize
     */
    public static int getMinDisplaySize()
    {
        return Math.min(AppConfigHelper.getScreenHeight(), AppConfigHelper.getDisplayWidth());
    }
    
    /**
     * return bigger edge length of the screen. 

     * 
     * @return MaxDisplaySize
     */
    public static int getMaxDisplaySize()
    {
        return Math.max(AppConfigHelper.getScreenHeight(), AppConfigHelper.getDisplayWidth());
    }
    
    /**
     * Retrieve the height of status bar. The status bar contains the time, and Radio signal info etc.
     * @return statusBarHeight
     */
    public static int getStatusBarHeight()
    {
        return 0;
    }
    
    /**
     * Retrieve the height of top bar. The top bar could be status bar or action bar.
     * @return topBarHeight
     */
    public static int getTopBarHeight()
    {
        int topHeight = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getStatusBarHeight(AbstractTnUiHelper.STATUS_BAR_TOP);
        if (topHeight > 0)
            return topHeight;
        return actionBarHeight;
    }
    
    public static int getActionBarHeight()
    {
        return actionBarHeight;
    }
    
    public static void setActionBarHeight(int height)
    {
        actionBarHeight = height;
    }
    
    public static void setIsStuckMonitorEnable(boolean isEnable)
    {
        isStuckMonitorEnable = isEnable; 
    }
    
    public static boolean isStunkMonitorEnable()
    {
        return isStuckMonitorEnable;
    }
    
    public static boolean isMapDiskCacheDisabled()
    {
        return isMapDiskCacheDisabled;
    }

    public static void setMapDiskCacheDisabled(boolean isMapDiskCacheDisabled)
    {
        AppConfigHelper.isMapDiskCacheDisabled = isMapDiskCacheDisabled;
    }

    public static boolean isTabletSize()
    {
        return isTablet;
    }
    
    public static boolean isQVGASize()
    {
        return isQVGA;
    }
    
    public static void setIsLoggerEnabled(boolean b)
    {
        isLoggerEnable = b;
    }
    
    public static void setIsOutputSdCardEnabled(boolean b)
    {
        isOutputSdCardEnabled = b;
    }
    
    public static String getDeviceUserAgent()
    {
        return deviceUserAgent;
    }

    public static void setDeviceUserAgent(String deviceUserAgent)
    {
        AppConfigHelper.deviceUserAgent = deviceUserAgent;
    }
    
    public static boolean isSkipSmsPtnDiscovery()
    {
        return "true".equalsIgnoreCase(skipSMSPTNDiscovery);
    }
    
    public static boolean isGlobalPtn()
    {
        if (AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
        {
            return true;
        }
        return false;
    }
    
    public static boolean isVerificaitonEnable()
    {
        return isNeedPtnVerification;
    }
    
    public static String removePtnGlobalHead(String rawPtn)
    {
        String ptn = "";
        
        if(rawPtn == null || rawPtn.trim().length() == 0)
        {
            return "";
        }
        
        rawPtn = rawPtn.trim();
        
        int length = rawPtn.length();
        
        for(int i = 0; i < length; i++)
        {
            char c = rawPtn.charAt(i);
            if(c != '0' && c != '+')
            {
                ptn = rawPtn.substring(i).trim();
                break;
            }
        }
        
        return ptn;
    }
    
    public static int getScreenWidthForBrowser()
    {
        return screenWidthForBrowser;
    }
    
    public static int getScreenHeightForBrowser()
    {
        return screenHeightForBrowser;
    }
    
    public static int getMaxSizeForBrowser()
    {
        return Math.max(screenWidthForBrowser, screenHeightForBrowser);
    }
    
    public static int getMinSizeForBrowser()
    {
        return Math.min(screenWidthForBrowser, screenHeightForBrowser);
    }
    
    public static boolean isDisableRoutesInSatelliteView()
    {
        return isDisableRoutesInSatelliteView;
    }

    public static void setDisableRoutesInSatelliteView(boolean inputIsDisableRoutesInSatelliteView)
    {
        isDisableRoutesInSatelliteView = inputIsDisableRoutesInSatelliteView;
        
    }

    public static String getAlongrouteSpeed()
    {
        return alongrouteSpeed;
    }

    public static void setAlongrouteSpeed(String alongrouteSpeed)
    {
        AppConfigHelper.alongrouteSpeed = alongrouteSpeed;
    }
    
    public static boolean isNeedReadExistUserLoginInfo(String currentVersion) 
    {
        //return true if currentVersion start with "7.3" or above
        int mainVersion = 0;
        int subVersion = 0;
        
        if (currentVersion == null || currentVersion.trim().length() == 0)
        {
            return false;
        }
        String strMainVersion = currentVersion;
        int firstDotIndex = currentVersion.indexOf(".");
        if (firstDotIndex != -1)
        {
             strMainVersion = currentVersion.substring(0, firstDotIndex);
        }
        try
        {
            mainVersion = Integer.parseInt(strMainVersion);
        }
        catch(Exception e)
        {
            mainVersion = 0;
        }

        if (mainVersion > MAIN_VERSION)
        {
            return true;
        } 
        else if (mainVersion == MAIN_VERSION)
        {
            if (firstDotIndex != -1)
            {
                String restString = currentVersion.substring(firstDotIndex + 1);
                if (restString == null || restString.trim().length() == 0)
                {
                    return false;
                }
                int secondDotIndex = restString.indexOf(".");
                if (secondDotIndex != -1) 
                {
                    restString = restString.substring(0, secondDotIndex);
                }
                try
                {
                    subVersion = Integer.parseInt(restString);
                }
                catch(Exception e)
                {
                    subVersion = 0;
                }
                if (subVersion >= SUB_VERSION) 
                {
                    return true;
                }
            }
        }
        return false;
    }
}

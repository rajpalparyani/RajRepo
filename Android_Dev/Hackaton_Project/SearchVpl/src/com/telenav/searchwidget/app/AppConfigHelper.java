/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AppConfigHelper.java
 *
 */
package com.telenav.searchwidget.app;

import java.io.IOException;
import java.io.InputStream;

import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public class AppConfigHelper
{
    public final static String BRAND_ATT = "cingular";

    public final static String BRAND_SPRINT = "sprint";
    
    public final static String BRAND_VIVO = "vivo";
    
    public final static String BRAND_TMOBILEUK = "tmobileuk";
    
    public final static String BRAND_USCC = "uscc";

    public final static String BRAND_TN = "tn";
    
    public final static String AUDIO_MP3 = "mp3";
    
    public final static String AUDIO_MP3HI = "mp3hi";
    
    public final static String PLATFORM_ANDROID = "ANDROID";
    
    public final static String PLATFORM_RIM = "RIM";
    
    public final static String GUIDE_TONE_US_FEMAIL = "203";
    
    public final static String GPS_TYPE_AGPS = "AGPS";
    
    /**
     * Retrieve the brand name of application.
     */
    public static String brandName;

    /**
     * Retrieve the audio type.
     */
    public static String audioType;

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
     * isLogger enable
     */
    public static boolean isLoggerEnable;
    
    /**
     * Retrieve true if it's a touch device.
     */
    public static boolean isTouchDevice = true;
    
    /**
     * TeleNav developing's apiKey
     */
    public static String apiKey;
    
    private static boolean isLoaded = false;
    
    /**
     * Tablet has special UI setting.
     */
    private static boolean isTablet = false;
    
    /**
     * QVGA has special UI setting.
     */
    private static boolean isQVGA = false;
    
    /* DRAFT:need???
    static
    {
        double xSize = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth() / ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getXdpi();
        double ySize = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() / ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getYdpi();
        isTablet = Math.sqrt(Math.pow(xSize, 2) + Math.pow(ySize, 2)) >= 7;
        
        int maxDisplaySize = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
        if(maxDisplaySize <= 320)
        {
            isQVGA = true;
        }
    }
    */
    
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
            
            audioType = configProperties.getProperty("AUDIO_TYPE");
            if(audioType != null)
                audioType = audioType.trim();
            
            buildNumber = configProperties.getProperty("APP_BUILD_NUMBER");
            if(buildNumber != null)
                buildNumber = buildNumber.trim();
            
            audioFileTimestamp = configProperties.getProperty("MD_AUDIO_FILE_TIMESTAMP");
            if(audioFileTimestamp != null)
                audioFileTimestamp = audioFileTimestamp.trim();
            
            audioFileInventory = configProperties.getProperty("MD_AUDIO_FILE_INVENTORY");
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
            
            localesList = configProperties.getProperty("MD_locales-list");
            if(localesList != null)
                localesList = localesList.trim();
            
            apiKey = configProperties.getProperty("MD_API_KEY");
            if(apiKey != null)
                apiKey = apiKey.trim();
            
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
    public static int getDisplayHeight()
    {
        return ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight();
    }
    
    /**
     * Retrieve the height of status bar. The status bar contains the time, and Radio signal info etc.
     * @return statusBarHeight
     */
    public static int getStatusBarHeight()
    {
        int topHeight = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getStatusBarHeight(AbstractTnUiHelper.STATUS_BAR_TOP);
        if (topHeight > 0)
            return topHeight;

        return ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getStatusBarHeight(AbstractTnUiHelper.STATUS_BAR_BOTTOM);
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
}

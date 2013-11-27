/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ServerDrivenParamsDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Enumeration;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class ServerDrivenParamsDao extends AbstractDao
{
    private static final String SEPERATOR = ".";
    private static final String STORE_SEPERATOR = "~-~";
    
    public static final int FEATURE_DISABLE = 0;
    public static final int FEATURE_ENABLE = 1;
    
    public final static String SERVER_DRIVEN_VERSION = "SERVER_DRIVEN_VERSION_INFO";
    
    public final static String NAV_SPEED_LIMIT_THRESHOLD = "SPEED_LIMIT_THRESHOLD";
    
    /**
     * Enable or disable location service.  This is the master switch of using location service.
     * If it is disabled, the client application MUST NOT use the location service at all.  This must be disabled by default on the client side  
     */
    public final static String CELL_LOCATION_ENABLE = "CELL_LOCATION_ENABLE";
    /**
     * Time to wait to get the first location service fix.  
     * This tells the client the number of second to wait before requesting the first location service fix.
     */
    public final static String CELL_LOCATION_FIRST_WAITTIME = "CELL_LOCATION_FIRST_WAITTIME";
    /**
     * Number of times to retry if the client failed to get a location from location service.
     */
    public final static String CELL_LOCATION_REQUEST_TIMES = "CELL_LOCATION_REQUEST_TIMES";
    /**
     * Retry interval, the time to wait before each retry.
     */
    public final static String CELL_LOCATION_REQUEST_INTERVAL = "CELL_LOCATION_REQUEST_INTERVAL";
    /**
     * Location service fix time to live value.  This is the length of the time that client can cache the location service fix.
     */
    public final static String CELL_LOCATION_LIVE_TIME = "CELL_LOCATION_LIVE_TIME";
    
    public final static String TOS_URL = "TERMS_AND_CONDITIONS_URL";
    
    public final static String SUPPORT_URL = "SUPPORT_INFO_PAGE_URL";
    
    /**
     * the application's live time when is in background.
     */
    public final static String APP_BACKGROUND_LIVE_TIME = "APP_BACKGROUND_LIVE_TIME_WHILE_EXIT";
    
    /**
     * Upsell screen's max show time
     */
    public final static String UPSELL_DISPLAY_TIMES = "UPSELL_DISPLAY_TIMES";
    
    /**
     * Splash screen's timeout
     */
    public final static String SPLASH_SCREEN_TIMEOUT = "SPLASH_SCREEN_TIMEOUT";
    
    /**
     * The time between current time and last abnormal exit times, -1 means disable
     */
    public final static String LAST_ABNORMAL_EXIT_INTERVALS = "LAST_ABNORMAL_EXIT_INTERVALS";
    
    /**
     * NAV_JUNCTION_VIEW
     */
    public final static String NAV_JUNCTION_VIEW = "FE_NAV_JUNCTION_VIEW";
    
    /**
     * c2dm account
     */
    public final static String C2DM_SENDER_GOOGLE_ACCOUNT = "C2DM_SENDER_GOOGLE_ACCOUNT";
    
    
    /**
     * mislog enable types
     */
    public static final String MISLOG_ENABLE_TYPES = "MISREPORTING";

    /**
     * mislog helper prefix
     */
    public static final String MISLOG_HELPER = "MISHELPERS";
    
    //0 -> diable 1 -> enable
    public final static String MAP_DATA_CACHING = "MAP_DATA_CACHING";

    //0 -> diable 1 -> enable
    public final static String ONEBOX_SEARCH_TAB_FEATURE = "ONEBOX_SEARCH_TAB_FEATURE";
    /**
     * the mini map's live time when stored.
     */
    public final static String MINI_MAP_LIVE_TIME = "MINI_MAP_LIVE_TIME";
    
    //*********************************server feature control*******************************//
    
    //******************************** App Store *******************************//
    public final static String DEFAULT_CAR_ICON = "DEFAULT_CAR_ICON";
    public final static String DEFAULT_GUIDE_TONE = "DEFAULT_GUIDE_TONE";
    
    
    //******************************** Region configuration *******************************//
    public final static String ONE_BOX_SUPPORT = "ONEBOX_SEARCH_TAB_FEATURE"; // 1 means one box search, 0 means international search
    public final static String ONE_BOX_TYPE_STANDARD = "1";
    public final static String CENTRAL_POINT = "CENTRAL_POINT";
    public final static String VERSION = "VERSION";
    
    //*****************************************AUTO_COMPLETE_DELAY_TIME*****************************//
    public final static String AUTO_COMPLETE_DELAY_TIME = "AUTO_COMPLETE_DELAY_TIME";
    /**
     * Polling billboard ad interval, Unit seconds, -1 means never polling ad.
     */
    public final static String BILLBOARD_AD_POLLING_INTERVAL = "BILLBOARD_AD_POLLING_INTERVAL";
    
    public final static String GPS_PROBE = "GPS_PROBE";
    
  //******************************** Startup Page *******************************//
    public final static String STARTUP_PAGE = "STARTUP_PAGE";
    
    public final static String STARTUP_MAPS = "maps";
    
    public final static String MAX_FAV_SIZE = "MAX_FAV_SIZE";
    
    public final static String MAX_RECENT_SIZE = "MAX_RECENT_SIZE";
    
    public final static String REGION_DETECTOR_SERVICE_INTERVAL = "REGION_DETECTION_INTERVAL";
    
    public final static String DASHBOARD_POI_QUICK_SEARCH_LIST = "DASHBOARD_POI_QUICK_SEARCH_LIST";
    
    public final static String ADDRESS_SCORE_THRESHOLD = "ADDRESS_SCORE_THRESHOLD";
    
    public final static String I18N_SYNC = "SYNC_I18N_RESOURCE";
    
    public final static String CMS_ABOUT_URL = "CMS_ABOUT_URL";

    /**
     * Client log included in feedback, judge whether to send ClientLogging to logger Csever
     * enable to on
     * disable to off
     */
    public static final String CLIENT_LOG_SWITCH = "CLIENT_LOG_SWITCH";
    
    private final static int SERVER_PARAMS_INDEX = 400001;
    
    private final static int REGION_RELATED_SERVER_PARAMS_INDEX = 400002;
    //this index is for widget to retrieve onebox type, please DO NOT remove.
    private final static int ONEBOX_TYPE_INDEX = 400003; 
    //this index is for widget to retrieve locale type, please DO NOT remove.
    private final static int LOCALE_INDEX = 400004; 
    
    private StringMap params;
    private TnStore serverDrivenStore;
    private TnRegionDependentStoreProvider serverDrivenProvider;
    private boolean isStartupMap = false;
    private boolean isStartupPageLoaded = false;
    private String version;
    
    public ServerDrivenParamsDao(TnStore serverDrivenStore)
    {
        params = new StringMap();
        this.serverDrivenStore = serverDrivenStore; 
        this.serverDrivenProvider = new TnRegionDependentStoreProvider(serverDrivenStore.getName(),serverDrivenStore.getType());
        init();
    }
    
    public String getValue(String key)
    {
        String value = (String)params.get(key);
        if(value != null && value.length() > 0)
            return value;
        
        MandatoryNodeDao mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao();
        String productType = mandatoryNode.getMandatoryNode().getClientInfo().productType;
        String regionIso = mandatoryNode.getMandatoryNode().getUserInfo().region;
        String localeIso = mandatoryNode.getMandatoryNode().getUserInfo().locale;
        
        Enumeration keysEnum = params.keys();
        while(keysEnum.hasMoreElements())
        {
            String tmpKey = (String)keysEnum.nextElement();
            String tmpValue = (String)params.get(tmpKey);
            
            String tempCheckKey = tmpKey + STORE_SEPERATOR;
            
            if(tempCheckKey.indexOf(key + STORE_SEPERATOR) != -1)
            {
                int index = -1;
                int count = 0;
                String tmpProductType = "";
                String tmpRegion = "";
                String tmpLocale = "";
                while((index = tmpKey.indexOf(SEPERATOR)) != -1)
                {
                    String tmpToken = index == 0 ? "" : tmpKey.substring(0, index);
                    if(count == 0)
                        tmpProductType = tmpToken;
                    else if(count == 1)
                    {
                        tmpRegion = tmpToken;
                    }
                    else
                    {
                        tmpLocale = tmpToken;
                    }
                    
                    tmpKey = tmpKey.substring(index + SEPERATOR.length());
                    count++;
                }
                
                if ((productType.equals(tmpProductType) || tmpProductType.length() == 0)
                        && (regionIso.equals(tmpRegion) || tmpRegion.length() == 0)
                        && (localeIso.equals(tmpLocale) || tmpLocale.length() == 0))
                {
                    value = tmpValue;
                    break;
                }
            }
        }
        
        return value;
    }
    
    private String getRegionDependentValue(String key, String region)
    {
        String target = null;
        StringMap params = getRegionDependentServerParams(region);
        if (params != null && params.size() >1)
            target = params.get(key);
        return target;
    }

    public int getIntValue(String key)
    {
        String tmp = getValue(key);
        if(tmp == null || tmp.trim().length() == 0)
            return -1;
        
        try
        {
            return Integer.parseInt(tmp);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            
            return -1;
        }
    }
     
    public String getVersion()
    {
        return this.version;
    }
    
    public void setServerParams(StringMap serverParams)
    {
        if(serverParams != null)
        {
            this.params = serverParams;
            this.version = serverParams.get(SERVER_DRIVEN_VERSION);
            
            if(this.serverDrivenStore != null)
            {
                this.serverDrivenStore.put(SERVER_PARAMS_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(serverParams));
            }
        }
    }
    
    
    public void setServerParams(StringMap serverParams, String region,String version)
    {
        if(serverParams != null)
        {
            this.params = serverParams;
           
            if(this.serverDrivenStore != null)
            {
                this.serverDrivenStore.put(SERVER_PARAMS_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(serverParams));
            }
            
            setRegionDependentServerParams(composeRegionRelatedMap(serverParams, version),region);
        }
    }
    
    public void setRegionDependentServerParams(StringMap serverParams, String region)
    {
        if(serverParams != null)
        {    
            if(this.serverDrivenProvider != null)
            {
                this.serverDrivenProvider.getStore(region).put(REGION_RELATED_SERVER_PARAMS_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(serverParams));
            }
        }
    }
    
    public void updateLocaleForWidget(String locale)
    {
        this.serverDrivenStore.put(LOCALE_INDEX, locale.getBytes());
        this.serverDrivenStore.save();
    }
    
    private StringMap composeRegionRelatedMap(StringMap sdpMap, String  version)
    {
        StringMap regionRelatedMap = new StringMap();
        if (version != null)
            regionRelatedMap.put(ServerDrivenParamsDao.VERSION, version);
        if (sdpMap != null)
        {
            String oneBoxSupport = sdpMap.get(ServerDrivenParamsDao.ONE_BOX_SUPPORT);
            if (oneBoxSupport != null)
            {
                regionRelatedMap.put(ServerDrivenParamsDao.ONE_BOX_SUPPORT, oneBoxSupport);
                this.serverDrivenStore.put(ServerDrivenParamsDao.ONEBOX_TYPE_INDEX, oneBoxSupport.getBytes());
            }
            
            String quickSearchList = sdpMap.get(ServerDrivenParamsDao.DASHBOARD_POI_QUICK_SEARCH_LIST);
            if (quickSearchList != null)
            {
                regionRelatedMap.put(ServerDrivenParamsDao.DASHBOARD_POI_QUICK_SEARCH_LIST, quickSearchList);
            }   
            
        }        
        return regionRelatedMap;

    }
    
    public void reload(String region)
    {
        if (serverDrivenStore != null)
        {
            this.serverDrivenStore.load();
            init();
        }

        if (region != null && serverDrivenProvider != null)
        {
            serverDrivenProvider.getStore(region).load();
        }
    }
    
    public synchronized void store()
    {
        this.serverDrivenStore.save();
        this.serverDrivenProvider.save();
    }
    
    
    public synchronized void clear()
    { 
        isStartupMap = false;
        isStartupPageLoaded = false;
        this.serverDrivenStore.clear();
        this.serverDrivenProvider.clear();
    }
    
    public StringMap getServerParams()
    {
        StringMap node = null;
        if(this.serverDrivenStore != null)
        {
            byte[] data = this.serverDrivenStore.get(SERVER_PARAMS_INDEX);
            if (data != null)
            {
                node = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data); 
            }
        }
        return node;
    }
    
    private StringMap getRegionDependentServerParams(String region)
    {
        StringMap node = null;
        if(this.serverDrivenProvider != null)
        {
            byte[] data = this.serverDrivenProvider.getStore(region).get(REGION_RELATED_SERVER_PARAMS_INDEX);
            if (data != null)
            {
                node = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data); 
            }
        }
        return node;
    }
    
    private void init()
    {
        if(this.serverDrivenStore != null && this.serverDrivenStore.contains(SERVER_PARAMS_INDEX))
        {
            StringMap serverParams = getServerParams();
            
            setServerParams(serverParams);
        }
    }
    
    public boolean isStartupMap()
    {
        if(!isStartupPageLoaded)
        {
            String startupPage = DaoManager.getInstance().getServerDrivenParamsDao().getValue(ServerDrivenParamsDao.STARTUP_PAGE);
            if (ServerDrivenParamsDao.STARTUP_MAPS.equalsIgnoreCase(startupPage))
            {
                this.isStartupMap = true;
            }
            else
            {
                this.isStartupMap = false;
            }
            this.isStartupPageLoaded = true;
        }
        return this.isStartupMap;
    }
    
    
    
    public boolean isI18NSyncEnabled()
    {
        boolean isEnabled = false;
        String i18NSyncEnabled = DaoManager.getInstance().getServerDrivenParamsDao().getValue(ServerDrivenParamsDao.I18N_SYNC);
        if (i18NSyncEnabled != null && "true".equalsIgnoreCase(i18NSyncEnabled))
        {
            isEnabled = true;
        }
        return isEnabled;
    }
    
    public String getCMSAboutUrl()
    {
        return DaoManager.getInstance().getServerDrivenParamsDao().getValue(ServerDrivenParamsDao.CMS_ABOUT_URL);
    }
    
    public String getBoxType(String region)
    {
        return getRegionDependentValue(ONE_BOX_SUPPORT, region);
    }
    
    public String getQuickSearchList(String region)
    {
        return getRegionDependentValue(DASHBOARD_POI_QUICK_SEARCH_LIST, region);
    }
   
    
    public String getVersion(String region)
    {
        return getRegionDependentValue(VERSION, region);
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CommManager.java
 *
 */
package com.telenav.app;

import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.comm.Comm;
import com.telenav.data.dao.misc.CommHostDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.ServiceLocator;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.io.TnIoManager;
import com.telenav.network.TnNetworkManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 11, 2010
 */
public class CommManager implements IServerProxyConstants
{
    public final static int SERVICE_NAME_ROUTING_INDEX = 1;
    public final static int SERVICE_NAME_RESOURCE_INDEX = 2;
    public final static int SERVICE_NAME_COMMON_INDEX = 3;
    public final static int SERVICE_NAME_LOGIN_INDEX = 4;
    public final static int SERVICE_NAME_LOGIN_HTTPS_INDEX = 5;
    public final static int SERVICE_NAME_POI_INDEX = 6;
    public final static int SERVICE_NAME_DSR_INDEX = 7;
    public final static int SERVICE_NAME_DSR_HTTP_INDEX = 8;
    public final static int SERVICE_NAME_OTA_INDEX = 9;
    public final static int SERVICE_NAME_DIM_INDEX = 10;
    public final static int SERVICE_NAME_MAITAI_AUTHENTICATE_INDEX = 11;
    public final static int SERVICE_NAME_MAITAI_LOOKUPURL_INDEX = 12;
    public final static int SERVICE_NAME_SEND_MIS_LOG_INDEX = 13;
    public final static int SERVICE_NAME_VECTOR_MAP_INDEX = 14;
    public final static int SERVICE_NAME_TRAFFIC_TILE_INDEX = 15;
    public final static int SERVICE_NAME_TTS_CLIENT_RES_INDEX = 16;
    public final static int SERVICE_NAME_DWF_HTTP_INDEX = 17;
    public final static int SERVICE_NAME_DWF_TINY_INDEX = 18;
    
    public final static String ABOUT_PAGE_DOMAIN_ALIAS = "aboutpage.http";
    public final static String APP_STORE_DOMAIN_ALIAS = "ecommerce.http";
    public final static String ACE_URL_DOMAIN_ALIAS = "aceTemplate.http";
    public final static String UPGRADE_URL_DOMAIN_ALIAS = "upgradeforhtmlpoi.http";
    public final static String PURCHASE_URL_DOMAIN_ALIAS = "urlforpurchase.http";
    public final static String COMMON_FEEDBACK_DOMAIN_ALIAS = "feedbacksurvey.http";
    public final static String POI_LIST_FEEDBACK_DOMAIN_ALIAS = "feedbacksurveypoilist.http";
    public final static String POI_DETAIL_FEEDBACK_DOMAIN_ALIAS = "feedbacksurveypoidetail.http";
    public final static String AD_JUGGLER_DOMAIN_ALIAS = "addons.http";
    public final static String WEATHER_DOMAIN_ALIAS = "urlforweather.http";
    public final static String POI_DETAIL_DOMAIN_ALIAS = "poidetail.http";
    public final static String FORD_APPLINK_API_SERVER_URL_DOMAIN_ALIAS = "fordapplink.http";
    public final static String TOS_URL_DOMAIN_ALIAS = "tos.http";
    public final static String VBB_URL_DOMAIN_ALIAS = "adsinfo.http";
    public final static String FORGOT_PASSWORD_URL_DOMAIN_ALIAS = "urlforforgotpassword.http";
    public final static String LOCAL_EVENTS_MAIN_DOMAIN_ALIAS = "scoutme.http";

    private final static String[] DOMAIN_ALIAS = 
    {
        "DOMAIN_ALIAS",     //0
        "routing.http",     //1
        "resource.http",    //2
        "common.http",      //3
        "login.http",       //4
        "login.https",      //5
        "poi.http",         //6
        "dsr",              //7
        "dsr.http",         //8
        "clientRes.http",   //9, ota server.
        "dim.http",         //10
        "maitaiAuthenticate.http",//"http://api.telenav.com/tnapi/services/auth/authenticate",//TODO:
        "maitaiLookup.http",//"http://api.telenav.com/tnapi/services/tnurl/lookupUrl",//TODO:
        "mislog.http",      //13, mis log
        "vectorMap.http",   //14, mis log
        "vectorTrafficMap.http",//15
        "ttsClientRes.http",//16
        "dwf.https", //17
        "dwf.tiny.http" //18
    };
    
    //-----------------------------------------------------------//
    private static CommManager commManager = new CommManager();
    
    protected String[][] SERVER_URLS;
    protected Hashtable actionServerIndexMapping;
    
    protected ServiceLocator locator;
    
    protected Comm defaultComm;
    protected Comm apacheComm;
    
    public CommManager()
    {
        locator = new CommServiceLocator();
    }
    
    public static CommManager getInstance()
    {
        return commManager;
    }
    
    public void initHostUrls(String[][] serverUrls)
    {
        this.SERVER_URLS = serverUrls;
    }
    
    public Comm getComm()
    {
        if (this.defaultComm == null)
        {
            this.defaultComm = new Comm(TnNetworkManager.getInstance(), TnIoManager.getInstance(), ThreadManager
                    .getPool(ThreadManager.TYPE_COMM_REQUEST), ThreadManager.getPool(ThreadManager.TYPE_COMM_BACKUP_REQUEST));
            this.defaultComm.setHostProvider(this.locator);
        }

        return this.defaultComm;
    }

    public Comm getApacheComm()
    {
        if (this.apacheComm == null)
        {
            this.apacheComm = new Comm(TnNetworkManager.getInstance(), TnIoManager.getInstance(), ThreadManager
                    .getPool(ThreadManager.TYPE_APP_ACTION), null);
            this.apacheComm.setHostProvider(this.locator);
        }

        return this.apacheComm;
    }
    
    public void initializeServiceLocator()
    {
        AbstractDaoManager.getInstance().getServiceLocatorDao().setServiceLocator(this.locator);
        
        loadDefaultServiceLocator();
        
        loadServerConfigServiceLocator();
    
        loadSecretConfigServiceLocator();
    }
    
    private void loadDefaultServiceLocator()
    {
        StringMap defaultActionUrl = getDefaultActionUrlMap();
        StringMap defaultActionServerMap = getDefaultActionAliasMap();
        locator.setDefaultServiceLocator(defaultActionServerMap, defaultActionUrl);
    }

    private StringMap getDefaultActionUrlMap()
    {
        CommHostDao hostDao = ((DaoManager)AbstractDaoManager.getInstance()).getCommHostDao();
        
        int index = hostDao.getDefaultSelectedIndex();
    
        if(index == -1)
        {
            index = 0;
            hostDao.setDefaultSelectedIndex(index);
        }
        
        StringMap map = new StringMap();
        
        //only for syncRes now.
        map.put(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE, SERVER_URLS[index][HostList.GROUP_RES_URL]);
        
        return map;
    }

	public String[] getUrlGroups()
    {
        String[] groups = new String[SERVER_URLS.length];
    
        for(int i = 0; i < groups.length; i++)
        {
            groups[i] = SERVER_URLS[i][0];
        }
    
        return groups;
    }
    
    public void storeSecretUrlData()
    {
        CommHostDao hostDao = ((DaoManager)AbstractDaoManager.getInstance()).getCommHostDao();
        hostDao.store();
    }
    
    public void removeSecretUrlData()
    {
        CommHostDao hostDao = ((DaoManager)AbstractDaoManager.getInstance()).getCommHostDao();
        hostDao.clear();
    }
    
    private void loadServerConfigServiceLocator()
    {
        StringMap domainUrlMap = DaoManager.getInstance().getServiceLocatorDao().getAliasUrlMap();
        StringMap actionServerMap = DaoManager.getInstance().getServiceLocatorDao().getActionAliasMap();
        StringMap serviceUrlMap = DaoManager.getInstance().getServiceLocatorDao().getAliasSuffixMap();
        
        if (domainUrlMap != null && actionServerMap != null && serviceUrlMap != null)
        {
            locator.setServerConfigServiceLocator(domainUrlMap, serviceUrlMap, actionServerMap);
        }
    }
    
    private void loadSecretConfigServiceLocator()
    {
        StringMap secretAliasUrlMap =  DaoManager.getInstance().getServiceLocatorDao().getSecretAliasUrlMap();
        StringMap secretActionUrlMap =  DaoManager.getInstance().getServiceLocatorDao().getSecretActionUrlMap();
        
        locator.setSetretServiceLocator(secretAliasUrlMap, secretActionUrlMap);
    }
    
    private StringMap getDefaultActionAliasMap()
    {
    	StringMap actionUrlMap = new StringMap();
        Hashtable actionIndexMap = getActionServerIndexMapping();

        if (actionIndexMap != null)
        {
            Enumeration enumeration = actionIndexMap.keys();
            while (enumeration.hasMoreElements())
            {
                String action = (String) enumeration.nextElement();
                int index = ((Integer) actionIndexMap.get(action)).intValue();
                actionUrlMap.put(action, DOMAIN_ALIAS[index]);
            }
        }

        return actionUrlMap;
    }
    
    private Hashtable getActionServerIndexMapping()
    {
        if (actionServerIndexMapping == null)
        {
            actionServerIndexMapping = new Hashtable();

            // RESOURCE SERVER
            actionServerIndexMapping.put(ACT_CACHE_CITIES,                  PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_GET_MISSING_AUDIO,             PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_REQUEST_CELL_LOCATION,         PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_RESOURCE,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_SERVICE_LOCATOR,          PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_SEND_CELLTOWER,                PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_STARTUP,                       PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_COMBINATION_SYNC_RESOURCE,     PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_CLIENT_SETTING_DATA,      PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            // logging
            actionServerIndexMapping.put(ACT_LOGGING,                       PrimitiveTypeCache.valueOf(SERVICE_NAME_RESOURCE_INDEX));
            
            //ota
            actionServerIndexMapping.put(ACT_SYNC_APACHE,                   PrimitiveTypeCache.valueOf(SERVICE_NAME_OTA_INDEX));
            actionServerIndexMapping.put(ACT_GET_TTS_URL,                   PrimitiveTypeCache.valueOf(SERVICE_NAME_TTS_CLIENT_RES_INDEX));

            actionServerIndexMapping.put(ACT_SHARE_ADDRESS,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_ONEBOX_SEARCH,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_VALIDATE_AIRPORT,              PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_POISEARCH,                     PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_VALIDATE_ADDRESS,              PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_BANNER_ADS,                    PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_SENT_ADDRESS,             PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_BILLBOARD_ADS,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            actionServerIndexMapping.put(ACT_BROWSER_POI_DETAIL,            PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            
            
            actionServerIndexMapping.put(ACT_MESSAGE_PROCESS,               PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
            actionServerIndexMapping.put(ACT_PREPARE_PURCHASE,              PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
            actionServerIndexMapping.put(ACT_LOGIN,                         PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
            actionServerIndexMapping.put(ACT_PURCHASE,                      PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
            actionServerIndexMapping.put(ACT_CANCEL_SUBSCRIPTION,           PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
            actionServerIndexMapping.put(ACT_FORGET_PIN,                    PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_PURCHASE,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
            actionServerIndexMapping.put(ACT_REQUEST_VERIFICATION_CODE,     PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_INDEX));
                       
            //for https login with encryption.
            actionServerIndexMapping.put(ACT_ENCRYPT_LOGIN,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_LOGIN_HTTPS_INDEX));

            // COMMON SERVER
            actionServerIndexMapping.put(ACT_FETCH_ALL_STOPS,               PrimitiveTypeCache.valueOf(SERVICE_NAME_COMMON_INDEX));
            actionServerIndexMapping.put(ACT_SEND_FEEDBACK,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_COMMON_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_PREFERENCE,               PrimitiveTypeCache.valueOf(SERVICE_NAME_COMMON_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_STOPS,                    PrimitiveTypeCache.valueOf(SERVICE_NAME_COMMON_INDEX));
            actionServerIndexMapping.put(ACT_SYNC_NEW_STOPS_BY_ID,          PrimitiveTypeCache.valueOf(SERVICE_NAME_COMMON_INDEX));
            actionServerIndexMapping.put(ACT_AUTO_SUGGEST,                  PrimitiveTypeCache.valueOf(SERVICE_NAME_COMMON_INDEX));
            actionServerIndexMapping.put(ACT_C2DM,                          PrimitiveTypeCache.valueOf(SERVICE_NAME_COMMON_INDEX));

            // NAV-MAP SERVER
            actionServerIndexMapping.put(ACT_RGC,                           PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_AVOID_INCIDENTS,               PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_AVOID_SELECT_SEGMENT,          PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_AVOID_SELECT_SEGMENT_DECIMATED,PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_MINIMIZE_DELAY,                PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_MINIMIZE_DELAY_DECIMATED,      PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_AVOID_REROUTE,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_DYNAMIC_ROUTE,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_CHUNKED_DYNAMIC_ROUTE,         PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_REVERSE_ROUTE,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_GET_EDGE,                      PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_GET_SPT,                       PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_STATIC_AVOID_SELECT_SEGMENT,   PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_STATIC_AVOID_SELECT_SEGMENT_DECIMATED, PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_STATIC_MINIMIZE_DELAY,         PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_STATIC_MINIMIZE_DELAY_DECIMATED,PrimitiveTypeCache
                    .valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_STATIC_ROUTE,                  PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_CHECK_DEVIATION,               PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_CHUNKED_ROUTE_SELECTION_CHECK_DEVIATION, PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_TRAFFIC_ALERT_MOVING_MAP,      PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_TRAFFIC_SUMMARY,               PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_DECIMATED_ROUTE,               PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_INCIDENT_REPORT,               PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_COMMUTE_ALERT_DETAIL,          PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_GET_ROUTE_CHOICES,             PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_SET_ROUTE_CHOICES_SELECTION,   PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            actionServerIndexMapping.put(ACT_CHUNKED_SET_ROUTE_CHOICES_SELECTION, PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));

            // DSR SERVER
            actionServerIndexMapping.put(ACT_DSR,                           PrimitiveTypeCache.valueOf(SERVICE_NAME_DSR_INDEX));
            actionServerIndexMapping.put(ACT_DSR_HTTP,                      PrimitiveTypeCache.valueOf(SERVICE_NAME_DSR_HTTP_INDEX));

            // DIM
            actionServerIndexMapping.put(ACT_GET_DIM_TOKEN,                 PrimitiveTypeCache.valueOf(SERVICE_NAME_DIM_INDEX));
            actionServerIndexMapping.put(ACT_GET_DIM_PTN,                   PrimitiveTypeCache.valueOf(SERVICE_NAME_DIM_INDEX));
            /*actionServerIndexMapping.put(ACT_GET_DIM_CARRIER,                   PrimitiveTypeCache.valueOf(SERVICE_NAME_DIM_INDEX));
            actionServerIndexMapping.put(ACT_SEARCH_DIM_CARRIER,                PrimitiveTypeCache.valueOf(SERVICE_NAME_DIM_INDEX));*/

            // MAITAI
            actionServerIndexMapping.put(ACT_MAITAI_AUTHENTICATE,           PrimitiveTypeCache.valueOf(SERVICE_NAME_MAITAI_AUTHENTICATE_INDEX));
            actionServerIndexMapping.put(ACT_MAITAI_LOOKUPURL,              PrimitiveTypeCache.valueOf(SERVICE_NAME_MAITAI_LOOKUPURL_INDEX));

            // mis log server
            actionServerIndexMapping.put(ACT_SEND_MIS_REPORTS,              PrimitiveTypeCache.valueOf(SERVICE_NAME_SEND_MIS_LOG_INDEX));
            
            // Client log
            actionServerIndexMapping.put(ACT_CLIENT_LOGGING,                PrimitiveTypeCache.valueOf(SERVICE_NAME_SEND_MIS_LOG_INDEX));
            
            //vector map
            actionServerIndexMapping.put(ACT_VECTOR_MAP,                    PrimitiveTypeCache.valueOf(SERVICE_NAME_VECTOR_MAP_INDEX));
            
            //traffic Tile
            actionServerIndexMapping.put(ACT_TRAFFIC_TILE,                  PrimitiveTypeCache.valueOf(SERVICE_NAME_TRAFFIC_TILE_INDEX));
            
            actionServerIndexMapping.put(ACT_GET_ETA,                       PrimitiveTypeCache.valueOf(SERVICE_NAME_ROUTING_INDEX));
            
            //weather
            actionServerIndexMapping.put(ACT_GET_WEATHER,                   PrimitiveTypeCache.valueOf(SERVICE_NAME_POI_INDEX));
            //dwf
            actionServerIndexMapping.put(ACT_DWF_HTTPS,                   PrimitiveTypeCache.valueOf(SERVICE_NAME_DWF_HTTP_INDEX));
            //dwf
            actionServerIndexMapping.put(ACT_DWF_TINY,                   PrimitiveTypeCache.valueOf(SERVICE_NAME_DWF_TINY_INDEX));
        }

        return actionServerIndexMapping;
    }
    
    private String[][] initServerMappings(int urlGroupIndex)
    {
        Hashtable actionIndexMap = getActionServerIndexMapping();
        
        String[][] strMapping = null;
        if(actionIndexMap != null)
        {
            int size = actionIndexMap.size();
            strMapping = new String[size][2];
            
            Enumeration enumeration = actionIndexMap.keys();
            int i = 0;
            while(enumeration.hasMoreElements())
            {
                String action = (String)enumeration.nextElement();
                int index = ((Integer)actionIndexMap.get(action)).intValue();
                strMapping[i][0] = action;
                strMapping[i][1] = SERVER_URLS[urlGroupIndex][index];
                i ++;
            }
        }
        
        return strMapping;
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ServiceLocatorTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import junit.framework.TestCase;

import org.powermock.reflect.Whitebox;

import com.telenav.comm.Host;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;

/**
 *@author bduan
 *@date 2011-6-30
 */
public class ServiceLocatorTest extends TestCase
{
	ServiceLocator serviceLocator;
	
	protected void setUp() throws Exception
	{
		serviceLocator = new ServiceLocator();
		super.setUp();
	}
	
	public void testSetSetretServiceLocator()
	{
	    setupSecretConfig();
        
        StringMap internalSecretAliasUrlMap = Whitebox.getInternalState(serviceLocator, "secretAliasUrlMap");
        StringMap internalSecretActionUrlMap = Whitebox.getInternalState(serviceLocator, "secretActionUrlMap");
        
        assertEquals("http://secret.resource.telenav.com:80", internalSecretAliasUrlMap.get("routing.http"));
        assertEquals("http://secret.ad:80", internalSecretAliasUrlMap.get("resource.http"));
        
        assertEquals("http://hqs-certsprint02.resource.secret.telenav.com:80", internalSecretActionUrlMap.get(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
        assertEquals("http://hqs-certsprint02.secret.ad:80", internalSecretActionUrlMap.get(IServerProxyConstants.ACT_AVOID_INCIDENTS));
	}
	
	public void testSetDefaultServiceLocator()
	{
	    setupDefaultConfig();
	    
	    StringMap internalDefaultActionAliasMap = Whitebox.getInternalState(serviceLocator, "defaultActionAliasMap");
	    StringMap internalDefaultActionUrlMap = Whitebox.getInternalState(serviceLocator, "defaultActionUrlMap");
	    
	    assertEquals("routing.http.default", internalDefaultActionAliasMap.get("dynamic_route"));
	    assertEquals("login.http.default", internalDefaultActionAliasMap.get("login"));
	    
	    assertEquals("http://hqs-certsprint02.resource.default.telenav.com:80", internalDefaultActionUrlMap.get(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
	    assertEquals("http://hqs-certsprint02.default.ad:80", internalDefaultActionUrlMap.get(IServerProxyConstants.ACT_AVOID_INCIDENTS));
	}
	
//  FIXME, we will fix this test case later.
//	public void testSetServerConfigServiceLocator()
//	{
//	    setupServerConfig();
//	    
//	    StringMap internalAliasUrlMap = Whitebox.getInternalState(serviceLocator, "aliasUrlMap");
//        StringMap internalAliasSuffixMap = Whitebox.getInternalState(serviceLocator, "aliasSuffixMap");
//        StringMap internalActionAliasMap = Whitebox.getInternalState(serviceLocator, "actionAliasMap");
//        
//        
//        assertEquals(internalAliasUrlMap.get("routing.http"), "http://hqs-certsprint02.routing.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("resource.http"), "http://hqs-certsprint02.resource.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("mislog.http"), "http://hqs-certsprint02.mislog.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("poi.http"), "http://hqs-certsprint02.poi.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("login.http"), "http://hqs-certsprint02.login.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("login.https"), "http://hqs-certsprint02.loginhttps.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("dsr.http"), "http://hqs-certsprint02.dsrhttp.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("dsr"), "http://hqs-certsprint02.dsr.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("common.http"), "http://hqs-certsprint02.common.telenav.com:80");
//        assertEquals(internalAliasUrlMap.get("dim.http"), "http://hqs-tnrim.telenav.com:80/telenavCServer");
//        assertEquals(internalAliasUrlMap.get("ecommerce.http"), "https://cert-ecommerce.telenav.com");
//        assertEquals(internalAliasUrlMap.get("clientRes.http"), "http://stageota.telenav.com/ota");
//        assertEquals(internalAliasUrlMap.get("vectorMap.http"), "http://hqs-vectortilecdn.telenav.com/US_TA_10M9_V2");
//        assertEquals(internalAliasUrlMap.get("addons.http"), "http://telenav.rotator.hadj7.adjuggler.net/servlet/ajrotator/147404/0/vh?z=telenav");
//        assertEquals(internalAliasUrlMap.get("xxddd"), null);
//        
//        
//        assertEquals(internalAliasSuffixMap.get("routing.http"), "/nav-map-cserver/telenav-server-pb");
//        assertEquals(internalAliasSuffixMap.get("resource.http"), "/resource-cserver/telenav-server-pb");
//        assertEquals(internalAliasSuffixMap.get("mislog.http"), "/log-cserver/telenav-server-pb");
//        assertEquals(internalAliasSuffixMap.get("poi.http"), "/poi_service/telenav-server-pb");
//        assertEquals(internalAliasSuffixMap.get("login.http"), "/login_startup_service/telenav-server-pb");
//        assertEquals(internalAliasSuffixMap.get("login.https"), "");
//        assertEquals(internalAliasSuffixMap.get("dsr.http"), "");
//        assertEquals(internalAliasSuffixMap.get("dsr"), "");
//        assertEquals(internalAliasSuffixMap.get("common.http"), "/common-cserver/telenav-server-pb");
//        assertEquals(internalAliasSuffixMap.get("dim.http"), "");
//        assertEquals(internalAliasSuffixMap.get("ecommerce.http"), "");
//        assertEquals(internalAliasSuffixMap.get("clientRes.http"), "");
//        assertEquals(internalAliasSuffixMap.get("vectorMap.http"), "");
//        assertEquals(internalAliasSuffixMap.get("addons.http"), "");
//        assertEquals(internalAliasSuffixMap.get("xxddd"), null);
//        
//        
//        //routing.http
//        assertEquals(internalActionAliasMap.get("Streaming_Map"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Map"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Dynamic_Route"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Get_Extra_Edge"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Check_Deviation"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Static_Route"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Reverse_Route"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Decimated_Route"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Summary"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Alerts_Moving_Map"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Avoid_Selected_Seg_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Min_Delay_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Selected_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Avoid_Incidents_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Static_Avoid_Selected_Seg_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Static_Min_Delay_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Incident_Report"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Commute_Alert_Details"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Reverse_Geocoding"), "routing.http");
//        assertEquals(internalActionAliasMap.get("CheckSelected_Deviation"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Chunked_Dynamic_Route"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Chunk_CheckSelected_Deviation"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Chunk_Selection_Route"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Decimated_MultiRoute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Avoid_Selected_Seg_Deci_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Min_Delay_Deci_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Static_Avoid_Selected_Seg_Deci_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Traffic_Static_Min_Delay_Deci_Reroute"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Get_Extra_Info"), "routing.http");
//        assertEquals(internalActionAliasMap.get("Selection_Route"), "routing.http");
//        assertEquals(internalActionAliasMap.get("TrafficMap"), "routing.http");
//        assertEquals(internalActionAliasMap.get("GET_ETA"), "routing.http");
//        
//        //resource.http
//        assertEquals(internalActionAliasMap.get("GetMissingAudio"), "resource.http");
//        assertEquals(internalActionAliasMap.get("SwitchLocale"), "resource.http");
//        assertEquals(internalActionAliasMap.get("SwitchRegion"), "resource.http");
//        assertEquals(internalActionAliasMap.get("SyncResource"), "resource.http");
//        assertEquals(internalActionAliasMap.get("CellTower"), "resource.http");
//        assertEquals(internalActionAliasMap.get("GetServiceLocator"), "resource.http");
//        assertEquals(internalActionAliasMap.get("Startup"), "resource.http");
//        assertEquals(internalActionAliasMap.get("LocationSearch"), "resource.http");
//        assertEquals(internalActionAliasMap.get("CacheCities"), "resource.http");
//        assertEquals(internalActionAliasMap.get("CombinationSyncResource"), "resource.http");
//        assertEquals(internalActionAliasMap.get("SyncServiceLocator"), "resource.http");
//        assertEquals(internalActionAliasMap.get("SyncClientSettingData"), "resource.http");
//        
//        //poi.http
//        assertEquals(internalActionAliasMap.get("ShareAddress"), "poi.http");
//        assertEquals(internalActionAliasMap.get("OneBox"), "poi.http");
//        assertEquals(internalActionAliasMap.get("SearchPoi"), "poi.http");
//        assertEquals(internalActionAliasMap.get("ValidateAirport"), "poi.http");
//        assertEquals(internalActionAliasMap.get("ValidateAddress"), "poi.http");
//        assertEquals(internalActionAliasMap.get("bannerAds"), "poi.http");
//        assertEquals(internalActionAliasMap.get("SentAddress"), "poi.http");
//        assertEquals(internalActionAliasMap.get("BillBoardAds"), "poi.http");
//        
//        //login.http
//        assertEquals(internalActionAliasMap.get("Login"), "login.http");
//        assertEquals(internalActionAliasMap.get("Purchase"), "login.http");
//        assertEquals(internalActionAliasMap.get("Cancel"), "login.http");
//        assertEquals(internalActionAliasMap.get("forgetPin"), "login.http");
//        assertEquals(internalActionAliasMap.get("Get_Account_Status"), "login.http");
//        assertEquals(internalActionAliasMap.get("SendPTNVerificationCode"), "login.http");
//        
//        //common.http
//        assertEquals(internalActionAliasMap.get("SyncNewStops"), "common.http");
//        assertEquals(internalActionAliasMap.get("SyncPreference"), "common.http");
//        assertEquals(internalActionAliasMap.get("Feedback"), "common.http");
//        assertEquals(internalActionAliasMap.get("FetchAllStops"), "common.http");
//        assertEquals(internalActionAliasMap.get("Logging"), "common.http");
//        assertEquals(internalActionAliasMap.get("AutoSuggestList"), "common.http");
//        assertEquals(internalActionAliasMap.get("Dsm"), "common.http");
//        assertEquals(internalActionAliasMap.get("ConfirmRecentStops"), "common.http");
//        
//        assertEquals(internalActionAliasMap.get("GetToken"), "dim.http");
//        assertEquals(internalActionAliasMap.get("GetPTN"), "common.http");
//        assertEquals(internalActionAliasMap.get("GetEncryptionPtn"), "common.http");
//        assertEquals(internalActionAliasMap.get("xxx"), null);
//        
//	}
	
    public void testClear()
	{
        setupDefaultConfig();
        setupSecretConfig();
//      FIXME, we will fix this test case later.
//        setupServerConfig();
        
        serviceLocator.clear();
        
        StringMap internalAliasUrlMap = Whitebox.getInternalState(serviceLocator, "aliasUrlMap");
        StringMap internalAliasSuffixMap = Whitebox.getInternalState(serviceLocator, "aliasSuffixMap");
        StringMap internalActionAliasMap = Whitebox.getInternalState(serviceLocator, "actionAliasMap");
        
        StringMap internalDefaultActionAliasMap = Whitebox.getInternalState(serviceLocator, "defaultActionAliasMap");
        StringMap internalDefaultActionUrlMap = Whitebox.getInternalState(serviceLocator, "defaultActionUrlMap");
        
        StringMap internalSecretAliasUrlMap = Whitebox.getInternalState(serviceLocator, "secretAliasUrlMap");
        StringMap internalSecretActionUrlMap = Whitebox.getInternalState(serviceLocator, "secretActionUrlMap");
        
        assertEquals(0, internalAliasUrlMap.size());
        assertEquals(0, internalAliasSuffixMap.size());
        assertEquals(0, internalActionAliasMap.size());
        
        assertEquals(0, internalDefaultActionAliasMap.size());
        assertEquals(0, internalDefaultActionUrlMap.size());
        
        assertEquals(0, internalSecretAliasUrlMap.size());
        assertEquals(0, internalSecretActionUrlMap.size());
	}
	
	public void testGetSecretUrl()
	{
	    setupSecretConfig();
	    
	    assertEquals("http://hqs-certsprint02.resource.secret.telenav.com:80", serviceLocator.getSecretUrl(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
	    assertEquals("http://hqs-certsprint02.secret.ad:80", serviceLocator.getSecretUrl(IServerProxyConstants.ACT_AVOID_INCIDENTS));
	}
	
	public void testGetSecretUrlNull()
	{
	    assertEquals(null, serviceLocator.getSecretUrl(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
	    assertEquals(null, serviceLocator.getSecretUrl(IServerProxyConstants.ACT_AVOID_INCIDENTS));
	}
	
	public void testGetDefaultUrl()
	{
	    setupDefaultConfig();
	    
	    assertEquals("http://hqs-certsprint02.resource.default.telenav.com:80", serviceLocator.getDefaultUrl(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
	    assertEquals("http://hqs-certsprint02.default.ad:80", serviceLocator.getDefaultUrl(IServerProxyConstants.ACT_AVOID_INCIDENTS));
	}
	
	public void testGetDefaultUrlNull()
	{
	    assertEquals(null, serviceLocator.getDefaultUrl(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
	    assertEquals(null, serviceLocator.getDefaultUrl(IServerProxyConstants.ACT_AVOID_INCIDENTS));
	}

	public void testGetDomainUrl_Null()
	{
		assertNull(serviceLocator.getDomainUrl("abc"));
	}
	
//  FIXME, we will fix this test case later.
//	public void testGetDomainUrlFromServer()
//	{
//	    setupServerConfig();
//	    
//	    assertEquals("http://hqs-certsprint02.routing.telenav.com:80", serviceLocator.getDomainUrl("routing.http"));
//	    assertEquals("http://hqs-certsprint02.poi.telenav.com:80", serviceLocator.getDomainUrl("poi.http"));
//	    assertEquals("http://hqs-certsprint02.login.telenav.com:80", serviceLocator.getDomainUrl("login.http"));
//	}
	
//  FIXME, we will fix this test case later.
//	public void testGetDomainUrlFromSecret()
//    {
//        setupServerConfig();
//        setupSecretConfig();
//        
//        assertEquals("http://secret.resource.telenav.com:80", serviceLocator.getDomainUrl("routing.http"));
//        assertEquals("http://secret.ad:80", serviceLocator.getDomainUrl("resource.http"));
//        assertEquals("http://hqs-certsprint02.poi.telenav.com:80", serviceLocator.getDomainUrl("poi.http"));
//        assertEquals("http://hqs-certsprint02.login.telenav.com:80", serviceLocator.getDomainUrl("login.http"));
//    }
	
//  FIXME, we will fix this test case later.
//	public void testGetAlias()
//	{
//	    setupServerConfig();
//	    
//	    assertEquals("routing.http", serviceLocator.getAlias("Streaming_Map"));
//	    assertEquals("routing.http", serviceLocator.getAlias("Traffic_Static_Avoid_Selected_Seg_Reroute"));
//	    assertEquals("resource.http", serviceLocator.getAlias("GetMissingAudio"));
//	    assertEquals(null, serviceLocator.getAlias("poi_test"));
//	    
//	    setupDefaultConfig();
//	    
//	    assertEquals("routing.http", serviceLocator.getAlias("Streaming_Map"));
//	    assertEquals("routing.http", serviceLocator.getAlias("Traffic_Static_Avoid_Selected_Seg_Reroute"));
//	    assertEquals("resource.http", serviceLocator.getAlias("GetMissingAudio"));
//	    assertEquals("poi.http.default", serviceLocator.getAlias("poi_test"));
//	    assertEquals("routing.http", serviceLocator.getAlias("Traffic_Incident_Report"));
//	    
//	}
	
	public void testGetActionUrlDefaultUrl()
    {
        setupDefaultConfig();
        
        assertEquals("http://hqs-certsprint02.resource.default.telenav.com:80", serviceLocator.getActionUrl(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
        assertEquals("http://hqs-certsprint02.default.ad:80", serviceLocator.getActionUrl(IServerProxyConstants.ACT_AVOID_INCIDENTS));
        assertEquals(null, serviceLocator.getActionUrl(IServerProxyConstants.ACT_AUTO_SUGGEST));
    }
	
//  FIXME, we will fix this test case later.
//	public void testGetActionUrlDefaultAlias()
//    {
//        setupServerConfig();
//        
//        assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("Streaming_Map"));
//        assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", serviceLocator.getActionUrl("GetMissingAudio"));
//        assertEquals("http://hqs-certsprint02.poi.telenav.com:80/poi_service/telenav-server-pb", serviceLocator.getActionUrl("ShareAddress"));
//        
//        assertEquals(null, serviceLocator.getActionUrl("route_test"));
//        
//        setupDefaultConfig();
//        
//        assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("Streaming_Map"));
//        assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", serviceLocator.getActionUrl("GetMissingAudio"));
//        assertEquals("http://hqs-certsprint02.poi.telenav.com:80/poi_service/telenav-server-pb", serviceLocator.getActionUrl("ShareAddress"));
//        
//        assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("route_test"));
//    }
	
//  FIXME, we will fix this test case later.
//	public void testGetActionUrlSecretAliasUrlConfig()
//    {
//        setupServerConfig();
//        setupDefaultConfig();
//        
//        assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("Streaming_Map"));
//        assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", serviceLocator.getActionUrl("GetMissingAudio"));
//        assertEquals("http://hqs-certsprint02.poi.telenav.com:80/poi_service/telenav-server-pb", serviceLocator.getActionUrl("ShareAddress"));
//        
//        assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("route_test"));
//        
//        setupSecretConfig();
//        assertEquals("http://secret.resource.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("Streaming_Map"));
//        
//        assertEquals("http://secret.resource.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("route_test"));
//    }
	
//  FIXME, we will fix this test case later.
//	public void testGetActionUrlSecretUrl()
//	{
//        setupServerConfig();
//        setupDefaultConfig();
//        
//        assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("Streaming_Map"));
//        assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", serviceLocator.getActionUrl("GetMissingAudio"));
//        assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", serviceLocator.getActionUrl(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
//        assertEquals("http://hqs-certsprint02.poi.telenav.com:80/poi_service/telenav-server-pb", serviceLocator.getActionUrl("ShareAddress"));
//        
//        assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("route_test"));
//        
//        setupSecretConfig();
//        assertEquals("http://hqs-certsprint02.resource.secret.telenav.com:80", serviceLocator.getActionUrl(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE));
//    }
	
//  FIXME, we will fix this test case later.
//	public void testGetActionUrlServerConfig()
//	{
//	    setupServerConfig();
//	    
//	    assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", serviceLocator.getActionUrl("Streaming_Map"));
//	    assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", serviceLocator.getActionUrl("GetMissingAudio"));
//	    assertEquals("http://hqs-certsprint02.poi.telenav.com:80/poi_service/telenav-server-pb", serviceLocator.getActionUrl("ShareAddress"));
//	}
	
	public void testCreateHostByURL()
    {
        String testUrl = "http://hqs-certsprint02.resource.default.telenav.com:80/telenav-common/login";
        Host httpHost = serviceLocator.createHostByURL(testUrl);
        assertEquals(testUrl, getUrl(httpHost));
        
        String url = "https://10.224.74.102:8443/login_startup_service/telenav-server-pb";
        httpHost = serviceLocator.createHostByURL(url);
        assertEquals(url, getUrl(httpHost));
        
        url = "socket://tn62-dsrc.telenav.com:8080";
        httpHost = serviceLocator.createHostByURL(url);
        assertEquals(url, getUrl(httpHost));
        
        url = "UDP://tn62-dsrc.telenav.com:8080";
        httpHost = serviceLocator.createHostByURL(url);
        assertEquals("datagram://tn62-dsrc.telenav.com:8080", getUrl(httpHost));
    }
	
//  FIXME, we will fix this test case later.
//	public void testCreateHostByAction()
//	{
//	    setupServerConfig();
//	    
//	    assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", getUrl(serviceLocator.createHost(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE)));
//	    assertEquals(null, serviceLocator.createHost("route_test"));
//	    
//	    
//	    setupDefaultConfig();
//	    
//	    assertEquals("http://hqs-certsprint02.resource.telenav.com:80/resource-cserver/telenav-server-pb", getUrl(serviceLocator.createHost(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE)));
//	    assertEquals("http://hqs-certsprint02.routing.telenav.com:80/nav-map-cserver/telenav-server-pb", getUrl(serviceLocator.createHost("route_test")));
//	    
//	    
//	    setupSecretConfig();
//	    
//	    assertEquals("http://hqs-certsprint02.poi.telenav.com:80/poi_service/telenav-server-pb", getUrl(serviceLocator.createHost("ValidateAirport")));
//	    assertEquals("http://secret.ad:80/resource-cserver/telenav-server-pb", getUrl(serviceLocator.createHost("SwitchRegion")));
//	    assertEquals("http://hqs-certsprint02.resource.secret.telenav.com:80", getUrl(serviceLocator.createHost(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE)));
//	    assertEquals("http://secret.resource.telenav.com:80/nav-map-cserver/telenav-server-pb", getUrl(serviceLocator.createHost("route_test")));
//	    
//	}
	
	protected void setupSecretConfig()
    {
        StringMap secretActionUrlMap = new StringMap();
        secretActionUrlMap.put(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE, "http://hqs-certsprint02.resource.secret.telenav.com:80");
        secretActionUrlMap.put(IServerProxyConstants.ACT_AVOID_INCIDENTS, "http://hqs-certsprint02.secret.ad:80");
        
        StringMap secretAliasUrlMap = new StringMap();
        secretAliasUrlMap.put("routing.http", "http://secret.resource.telenav.com:80");
        secretAliasUrlMap.put("resource.http", "http://secret.ad:80");
        
        serviceLocator.setSetretServiceLocator(secretAliasUrlMap, secretActionUrlMap);
    }
    
    protected void setupDefaultConfig()
    {
        StringMap defaultActionUrlMap = new StringMap();
        defaultActionUrlMap.put(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE, "http://hqs-certsprint02.resource.default.telenav.com:80");
        defaultActionUrlMap.put(IServerProxyConstants.ACT_AVOID_INCIDENTS, "http://hqs-certsprint02.default.ad:80");
        
        StringMap defaultActionAliasMap = new StringMap();
        defaultActionAliasMap.put("dynamic_route", "routing.http.default");
        defaultActionAliasMap.put("login", "login.http.default");
        defaultActionAliasMap.put("poi_test", "poi.http.default");
        defaultActionAliasMap.put("route_test", "routing.http");
        defaultActionAliasMap.put("Traffic_Incident_Report", "poi.http.default");
        
        serviceLocator.setDefaultServiceLocator(defaultActionAliasMap, defaultActionUrlMap);
    }
    
//    FIXME, we will fix this test case later.
//    protected void setupServerConfig()
//    {
//        StringMap aliasUrlMap = ServiceLocatorDataProvider.getAliasUrlMap();
//        StringMap aliasSuffixMap = ServiceLocatorDataProvider.getAliasSuffixMap();
//        StringMap actionAliasMap = ServiceLocatorDataProvider.getActionAliasMap();
//        
//        serviceLocator.setServerConfigServiceLocator(aliasUrlMap, aliasSuffixMap, actionAliasMap);        
//    }
	
	protected String getUrl(Host httpHost)
	{
		String url = httpHost.protocol + "://" + httpHost.host;
        if(httpHost.port > 0)
        {
            url += ":" + httpHost.port;
        }
        if(httpHost.file != null && httpHost.file.length() > 0)
        {
            url += httpHost.file;
        }
		return url;
	}

}

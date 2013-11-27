/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IOneBoxSearchProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.nav.NavState;
import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IOneBoxSearchProxy
{
    public static final int INITIAL_SEARCH_RADIUS_CAT          = 5;    //mile
    public static final int MAXIMUM_SEARCH_RADIUS_CAT          = 15;   //mile
    public static final int SEARCH_RADIUS_INCREMENT_CAT        = 5;    //mile
    public static final int MILE_TO_DM5                        = 1446; //1 mile = 1446 dm5
    public static final int DEFAULT_RADIOUS = INITIAL_SEARCH_RADIUS_CAT * MILE_TO_DM5; //5 miles as init dist to search poi
    
    //search type
    public static final int TYPE_SEARCH_AROUND_ME = 0;

    public static final int TYPE_SEARCH_RECENT_STOPS = 1;

    public static final int TYPE_SEARCH_AIRPORT = 2;

    public static final int TYPE_SEARCH_CITY = 3;

    public static final int TYPE_SEARCH_ZIP = 4;

    public static final int TYPE_SEARCH_ADDRESS = 5;

    public static final int TYPE_SEARCH_WAYPOINTS = 6;

    public static final int TYPE_SEARCH_ALONGROUTE = 7;
    
    public static final int TYPE_ALONGROUTE_SEARCH_UPPER_HEAD = 0;
    
    public static final int TYPE_ALONGROUTE_SEARCH_NEAR_DEST = 1;
    
    public static final int TYPE_NODE_NAVSTATE = 117;
    
    public static final int TYPE_NODE_ORIN = 118;
    
    public static final int TYPE_NODE_DEST = 119;
    
    //result type
    public static final int TYPE_NO_RESULTS = -1;
    public static final int TYPE_POI_RESULTS = 0;
    public static final int TYPE_SUGGESTS_RESULTS = 1;
    public static final int TYPE_STOPS_RESULTS = 2;
    
    //input type
    public static final int TYPE_INPUTTYPE_ANY = 0;
    public static final int TYPE_INPUTTYPE_CHN = 1;
    public static final int TYPE_INPUTTYPE_PY = 2;
    
    public void oneBoxSearch(String searchUid, String keyword, int pageIndex, int maxNumberPerPage, int distnace, int searchType, int alongRouteType, Stop anchor,TnLocation[] gpsfixs, int fixes, Stop dest, NavState navState,int inputType);
    
    public int getTotalCount();

    public Vector getAddresses();

    public Vector getPois();
    
    public Vector getSponsoredPoi();

    public Vector getSuggestions();
    
    public int getResultType();
    
    public boolean isNeedUserSelection();
    
    public void canceSearchRequest();
    
    
}

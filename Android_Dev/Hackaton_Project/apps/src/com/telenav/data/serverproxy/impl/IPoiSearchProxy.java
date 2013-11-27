/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPoiSearchProxy.java
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
public interface IPoiSearchProxy
{
    
    //search FROM type
    public final static int TYPE_SEARCH_FROM_TYPEIN = 1;
    public final static int TYPE_SEARCH_FROM_SPEAKIN = 2;
    public final static int TYPE_SEARCH_FROM_TYPEIN_ALONG = 3;
    public final static int TYPE_SEARCH_FROM_SPEAKIN_ALONG = 4;
    
    //search type
    public final static int TYPE_SEARCH_ALONG_ROUTE = 7;
    public final static int TYPE_SEARCH_ADDRESS = 5;
    
    //SORT TYPE
    public final static int TYPE_SORT_BY_DISTANCE = 0;
    public final static int TYPE_SORT_BY_RATING = 1;
    public final static int TYPE_SORT_BY_POPULAR = 2;
    public final static int TYPE_SORT_BY_RELEVANCE = 3;
    public final static int TYPE_SORT_BY_PRICE = 4;
    
    //along route  type
    public final static int TYPE_SEARCH_ALONG_UPHEAD = 0;
    public final static int TYPE_SEARCH_AROUND_DESTINATION = 1;
    
    public void requestPoiByCategory(String transactionId, int categoryId, int searchType, 
            int searchFromType, int sortType,  int pageNum, 
            int pageSize, boolean isMostPopular, int distanceUnit, int searchAlongType, String searchString, 
            Stop anchor, TnLocation[] gpsfixs, int fixes, int sponsorNum, NavState navState);
    
    public Vector getPois();
    public Vector getSponsoredPois();
    public int getTotalCount();
    public void cancelSearchRequest();
    
}

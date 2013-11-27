/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkPoiProxyReal.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper.NavSdkSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.location.TnLocation;
import com.telenav.module.poi.PoiSearchArgs;
import com.telenav.navsdk.events.PointOfInterestData.SearchEntryMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchType;
import com.telenav.navsdk.events.PointOfInterestData.SortBy;

/**
 * @author hchai
 * @date 2011-11-29
 */
public class NavSdkPoiProxy extends NavSdkSearchProxy implements IPoiSearchProxy, INavSdkProxyConstants
{

    public NavSdkPoiProxy(IServerProxyListener listener)
    {
        super(listener);
    }

    public void requestPoiByCategory(String transactionId, int categoryId, int searchType, int searchFromType, int sortType,
            int pageNum, int pageSize, boolean isMostPopular, int distanceUnit, int searchAlongType, String searchString,
            Stop anchor, TnLocation[] gpsfixs, int fixes, int sponsorNum, NavState navState)
    {
        SearchMode searchMode = null;
        SortBy sortBy = SortBy.SortBy_Default;
        SearchEntryMode searchEntryMode = null;

        if (searchType == PoiSearchArgs.TYPE_SEARCH_ADDRESS || searchType == PoiSearchArgs.TYPE_SEARCH_ALONG_ROUTE)
        {
            searchMode = SearchMode.SearchMode_Address;
            if (sortType == PoiSearchArgs.TYPE_SORT_BY_DISTANCE)
            {
                sortBy = SortBy.SortBy_Distance;
            }
            else if (sortType == PoiSearchArgs.TYPE_SORT_BY_RATING)
            {
                sortBy = SortBy.SortBy_Rating;
            }
            else if (sortType == PoiSearchArgs.TYPE_SORT_BY_POPULAR)
            {
                sortBy = SortBy.SortBy_Popularity;
            }
            else if (sortType == PoiSearchArgs.TYPE_SORT_BY_RELEVANCE)
            {
                sortBy = SortBy.SortBy_Relevance;
            }
            else if (sortType == PoiSearchArgs.TYPE_SORT_BY_PRICE)
            {
                sortBy = SortBy.SortBy_Price;
            }
        }
        if (searchType == PoiSearchArgs.TYPE_SEARCH_ALONG_ROUTE)
        {
            if (searchAlongType == PoiSearchArgs.TYPE_SEARCH_ALONG_UPHEAD)
            {
                searchMode = SearchMode.SearchMode_AlongRouteAhead;
            }
            else if (searchAlongType == PoiSearchArgs.TYPE_SEARCH_AROUND_DESTINATION)
            {
                searchMode = SearchMode.SearchMode_AlongRouteAroundDestination;
            }
        }

        if (searchFromType == PoiSearchArgs.TYPE_SEARCH_FROM_SPEAKIN
                || searchFromType == PoiSearchArgs.TYPE_SEARCH_FROM_SPEAKIN_ALONG)
        {
            searchEntryMode = SearchEntryMode.SearchEntryMode_Voice;
        }
        else if (searchFromType == PoiSearchArgs.TYPE_SEARCH_FROM_TYPEIN
                || searchFromType == PoiSearchArgs.TYPE_SEARCH_FROM_TYPEIN_ALONG)
        {
            searchEntryMode = SearchEntryMode.SearchEntryMode_Text;
        }

        int firstIndex = pageNum * pageSize;
        Address nearAddress = new Address();
        nearAddress.setStop((Stop)anchor.clone());
        this.requestSearchService(ACT_POI_CATEGORY_SEARCH, transactionId, searchString, firstIndex, pageSize, categoryId,
            searchMode, sortBy, searchEntryMode, SearchType.SearchType_CategorySearch, nearAddress);
    }

    public Vector getPois()
    {
        return this.getAddresses();
    }

    public void cancelSearchRequest()
    {
        this.sendCancelPoiSearchRequest();
    }

}

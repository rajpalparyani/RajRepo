/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkAddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper.NavSdkSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.location.TnLocation;
import com.telenav.module.poi.PoiSearchArgs;
import com.telenav.navsdk.events.PointOfInterestData.SearchEntryMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchType;
import com.telenav.navsdk.events.PointOfInterestData.SortBy;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2011-12-1
 */
public class NavSdkOneboxProxy extends NavSdkSearchProxy implements IOneBoxSearchProxy, INavSdkProxyConstants
{
    public NavSdkOneboxProxy(IServerProxyListener listener)
    {
        super(listener);
    }

    public void oneBoxSearch(String searchUid, String keyword, int pageIndex, int maxNumberPerPage, int distnace,
            int searchType, int alongRouteType, Stop anchor, TnLocation[] gpsfixs, int fixes, Stop dest, NavState navState,
            int inputType)
    {
        SearchMode searchMode = null;
        SortBy sortBy = null;
        SearchEntryMode searchEntryMode = null;

        if (searchType == PoiSearchArgs.TYPE_SEARCH_ADDRESS || searchType == PoiSearchArgs.TYPE_SEARCH_AROUND_ME)
        {
            searchMode = SearchMode.SearchMode_Address;
        }
        else if (searchType == PoiSearchArgs.TYPE_SEARCH_ALONG_ROUTE)
        {
            if (alongRouteType == PoiSearchArgs.TYPE_SEARCH_ALONG_UPHEAD)
            {
                searchMode = SearchMode.SearchMode_AlongRouteAhead;
            }
            else if (alongRouteType == PoiSearchArgs.TYPE_SEARCH_AROUND_DESTINATION)
            {
                searchMode = SearchMode.SearchMode_AlongRouteAroundDestination;
            }
        }

        searchEntryMode = SearchEntryMode.SearchEntryMode_Text;

        int firstIndex = pageIndex * maxNumberPerPage;
        Address nearAddress = new Address();
        nearAddress.setStop(anchor);
        requestSearchService(ACT_POI_ONEBOX_SEARCH, searchUid, keyword, firstIndex, maxNumberPerPage,
            PoiSearchArgs.TYPE_ONE_BOX_SEARCH, searchMode, sortBy, searchEntryMode, SearchType.SearchType_OneBoxSearch,
            nearAddress);
    }

    public Vector getPois()
    {
        return this.getAddresses();
    }

    public Vector getSponsoredPoi()
    {
        return this.getSponsoredPois();
    }

    @Override
    public boolean isNeedUserSelection()
    {
        return this.isFuzzyMatched();
    }

    @Override
    public void canceSearchRequest()
    {
        this.sendCancelPoiSearchRequest();
    }
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkAutoSuggestProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IAutoSuggestProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper.NavSdkSearchProxy;
import com.telenav.module.poi.PoiSearchArgs;
import com.telenav.navsdk.events.PointOfInterestData.SearchEntryMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchType;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2012-1-5
 */
public class NavSdkAutoSuggestProxy extends NavSdkSearchProxy implements IAutoSuggestProxy, INavSdkProxyConstants
{
    private String queryString;

    public NavSdkAutoSuggestProxy(IServerProxyListener listener)
    {
        super(listener);
    }

    public String requestAutoSuggestList(String filter, int size, int lat, int lon, String transactionId)
    {

        String jobId = "";
        this.queryString = filter;

        Address nearPoi = new Address();
        Stop stop = new Stop();
        stop.setLat(lat);
        stop.setLon(lon);
        nearPoi.setStop(stop);
        this.requestSearchService(IServerProxyConstants.ACT_AUTO_SUGGEST, transactionId, filter, 0, size,
            PoiSearchArgs.TYPE_AUTOSUGGEST, null, null, SearchEntryMode.SearchEntryMode_Text,
            SearchType.SearchType_AutoSuggest, nearPoi);
        return jobId;
    }

    public String getQueryString()
    {
        return queryString;
    }

    public Vector getResults()
    {
        return this.getSuggestions();
    }
    
    public boolean hasSuggestionResult()
    {
        return this.hasSuggestionReturn();
    }

}

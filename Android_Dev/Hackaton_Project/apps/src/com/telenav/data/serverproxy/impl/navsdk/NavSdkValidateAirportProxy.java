/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkValidateAirportProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Vector;

import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IValidateAirportProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper.NavSdkSearchProxy;
import com.telenav.module.poi.PoiSearchArgs;
import com.telenav.navsdk.events.PointOfInterestData.SearchEntryMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchType;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2012-1-6
 */
public class NavSdkValidateAirportProxy extends NavSdkSearchProxy implements IValidateAirportProxy
{

    public NavSdkValidateAirportProxy(IServerProxyListener listener)
    {
        super(listener);
    }

    public void validateAirport(String airportName, String region)
    {
        this.requestSearchService(IServerProxyConstants.ACT_VALIDATE_AIRPORT, "", airportName, 0, 0,
            PoiSearchArgs.TYPE_VALIDATE_AIRPORT, SearchMode.SearchMode_Address, null, SearchEntryMode.SearchEntryMode_Text,
            SearchType.SearchType_AirportSearch, null);
    }

    public Vector getSimilarAirports()
    {
        return this.getAddresses();
    }

}

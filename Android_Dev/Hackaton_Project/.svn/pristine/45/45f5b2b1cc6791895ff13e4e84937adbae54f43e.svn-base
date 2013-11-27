/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkRgcProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper.NavSdkSearchProxy;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2011-12-30
 */
public class NavSdkRgcProxy extends NavSdkSearchProxy implements IRgcProxy, INavSdkProxyConstants
{
    private int requestedLat = 0;
    private int requestedLon = 0;
    
    public NavSdkRgcProxy(IServerProxyListener listener)
    {
        super(listener);
    }

    public void requestRgc(int lat, int lon)
    {
        requestedLat = lat;
        requestedLon = lon;
        this.requestRgcService(ACT_POI_REVERSE_GEOCODE, lat, lon);
    }

    public Address getAddress()
    {
        Vector addresses = this.getAddresses();
        if (addresses != null && addresses.size() > 0)
        {
            return (Address) addresses.elementAt(0);
        }
        else
        {
            return null;
        }
    }

    public void requestRgc(int lat, int lon, int retryTimes, int timeout)
    {
        // TODO Auto-generated method stub

    }
    
    public int getRequestedLat()
    {
        return this.requestedLat;
    }
    
    public int getRequestedLon()
    {
        return this.requestedLon;
    }

}

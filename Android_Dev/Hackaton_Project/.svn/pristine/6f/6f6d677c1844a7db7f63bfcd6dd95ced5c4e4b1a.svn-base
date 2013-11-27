/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkLocationProviderProxyHelper.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.helper;

import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.LocationEvents.LocationUpdatedRequest;
import com.telenav.navsdk.services.LocationListener;
import com.telenav.navsdk.services.LocationServiceProxy;

/**
 *@author yren
 *@date 2012-11-23
 */
public class NavSdkLocationProviderProxyHelper implements LocationListener
{
    private static NavSdkLocationProviderProxyHelper instance;
    
    private LocationServiceProxy serverProxy;
    
    public static NavSdkLocationProviderProxyHelper getInstance()
    {
        return instance;
    }
    
    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavSdkLocationProviderProxyHelper();
            instance.setEventBus(bus);
        }
    }
    
    private void setEventBus(EventBus bus)
    {
        serverProxy = new LocationServiceProxy(bus);
        serverProxy.addListener(this);
    }
    
    public void updateLocation(LocationUpdatedRequest event)
    {
        serverProxy.locationUpdated(event);
    }
}

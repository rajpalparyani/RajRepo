/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LocationProvider.java
 *
 */
package com.telenav.carconnect.provider.tnlink;

import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.carconnect.provider.tnlink.module.location.LocationUpdate;

/**
 *@author xiangli
 *@date 2012-3-21
 */
public class LocationProvider extends AbstractProvider
{
    private final String[] events = {"StartLocationUpdateRequest", "StopLocationUpdateRequest","DisconnectedFromCar"};
    
    @Override
    public void handle(String eventType, Object eventData)
    {
        // TODO Auto-generated method stub
        if ("StartLocationUpdateRequest".equals(eventType))
        {
            LocationUpdate update = LocationUpdate.getLocationInstance();
            update.startUpdate();
        }
        
        else if ("StopLocationUpdateRequest".equals(eventType) || "DisconnectedFromCar".equals(eventType))
        {
            LocationUpdate.getLocationInstance().stopUpdate();
        }
    }

    @Override
    public void register()
    {
        // TODO Auto-generated method stub
        for (String event : events)
        {
            getEventBus().subscribe(event, this);
        }
    }

    @Override
    public void unregister()
    {
        // TODO Auto-generated method stub
        for (String event : events)
        {
            getEventBus().unsubscribe(event, this);
        }
    }

}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * HttpProvider.java
 *
 */
package com.telenav.carconnect.provider.tnlink;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.carconnect.provider.tnlink.module.http.HttpProxy;
import com.telenav.navsdk.events.HttpProxyEvents.HttpProxyRequest;

/**
 *@author xiangli
 *@date 2012-3-27
 */
public class HttpProvider extends AbstractProvider
{
    private final String[] events = {CarConnectEvent.HTTP_REQUEST};
    @Override
    public void handle(String eventType, Object eventData)
    {
        // TODO Auto-generated method stub
        MyLog.setLog("handler", eventType);
        if (CarConnectEvent.HTTP_REQUEST.equals(eventType))
        {
            HttpProxy.appendRequest((HttpProxyRequest)eventData);
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

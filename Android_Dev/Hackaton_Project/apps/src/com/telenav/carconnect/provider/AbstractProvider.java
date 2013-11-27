/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AbstractProvider.java
 *
 */
package com.telenav.carconnect.provider;

import com.telenav.carconnect.CarConnectManager;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.eventmanager.EventHandler;

/**
 * Base class for CarConnect Provider using NavSDK EventBus
 * 
 * @author chihlh
 * @date Mar 2, 2012
 */
public abstract class AbstractProvider implements EventHandler
{
    protected final static String TAG = "CarConnect";

    @Override
    /**
     * Handle the event from event bus
     */
    public abstract void handle(String eventType, Object eventData);

    /**
     * register the provider to EventBus
     */
    public abstract void register();

    /**
     * unregister the provider to EventBus
     */
    public abstract void unregister();

    /**
     * Get the EventBusWrapper
     */

    protected EventBus getEventBus()
    {
        return CarConnectManager.getEventBus();
    }

}

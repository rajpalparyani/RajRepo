/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkNavEngine.java
 *
 */
package com.telenav.module.nav;

import java.util.Vector;

import com.telenav.datatypes.nav.NavDataFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.logger.Logger;
import com.telenav.navsdk.nav.event.NavSdkNavEvent;

/**
 *@author yren
 *@date 2012-12-27
 */
public class NavSdkNavEngine
{
    private static NavSdkNavEngine instance = new NavSdkNavEngine();
    private boolean isRunning = false;
    
    protected Vector listeners = new Vector();

    protected Object mutex = new Object();
    
    protected NavState navState = NavDataFactory.getInstance().createNavState(-1);
    
    private int currentRoutId = -1;
    
    private NavSdkNavEngine()
    {
        
    }

    public static NavSdkNavEngine getInstance()
    {
        return instance;
    }
    
    public void start()
    {
        isRunning = true;
    }

    public void addListener(INavEngineListener listener)
    {
        listeners.addElement(listener);
    }
    
    public void removeListener(INavEngineListener listener)
    {
        if (listener == null)
        {
            return;
        }
        
        listeners.removeElement(listener);
    }
    
    public void removeAllListeners()
    {
        listeners.removeAllElements();
    }
    
    public NavState getCurrentNavState()
    {
        return navState;
    }

    public void stop()
    {
        removeAllListeners();
        isRunning = false;
    }

    public boolean isRunning()
    {
        return isRunning;
    }
    
    protected void updateState(NavSdkNavEvent event)
    {
        if(currentRoutId != RouteWrapper.getInstance().getCurrentRouteId())
        {
            currentRoutId = RouteWrapper.getInstance().getCurrentRouteId();
        }
        
        navState.set(event.getNextTurnIndex() - 1);
    }

    public void handleEvent(NavSdkNavEvent event)
    {
        if (event != null
                && (event.getEventType() == NavSdkNavEvent.TYPE_NAVSDK_ON_ROUTE || event.getEventType() == NavSdkNavEvent.TYPE_NAVSDK_OFF_ROUTE))
        {
            this.updateState(event);
        }

        if (listeners != null)
        {
            for (int i = 0; i < listeners.size(); i++)
            {
                try
                {
                    INavEngineListener listener = (INavEngineListener) listeners.elementAt(i);
                    listener.eventUpdate(event);
                }
                catch (Throwable e)
                {
                    Logger.log(this.getClass().getName(), e, "[Navigation] - event update error.");
                }
            }
        }
    }
}

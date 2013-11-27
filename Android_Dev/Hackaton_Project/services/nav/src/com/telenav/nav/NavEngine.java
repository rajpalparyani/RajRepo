/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavEngine.java
 *
 */
package com.telenav.nav;

import java.util.Vector;
import com.telenav.datatypes.nav.NavState;
import com.telenav.logger.Logger;
import com.telenav.nav.event.NavEvent;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class NavEngine
{
    public static int RECURING_DELAY = 1280;

    public static int ADI_GPS_RETRY_TIMES = 12;

    public static int NAV_GPS_RETRY_TIMES = 16;
    
    public static boolean isMergeContinue = true;
    private static NavEngine instance = new NavEngine();
    private boolean isRunning = false;
    protected boolean isArriveDestination;

    protected Vector listeners = new Vector();

    protected INavDataProvider navDataProvider;

    protected INavGpsProvider gpsProvider;
    protected Object mutex = new Object();
    
    private NavEngine()
    {
        
    }

    public static NavEngine getInstance()
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
       //TODO
        return null;
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

    public boolean isArriveDestination()
    {
        return this.isArriveDestination;
    }

    INavGpsProvider getGpsProvider()
    {
        return this.gpsProvider;
    }

    public void handleEvent(NavEvent event)
    {
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
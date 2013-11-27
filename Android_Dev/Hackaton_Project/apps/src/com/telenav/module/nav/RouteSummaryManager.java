/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * RouteSummaryManager.java
 *
 */
package com.telenav.module.nav;

import java.util.Vector;

/**
 * @author yren
 * @date 2013-5-28
 */
public class RouteSummaryManager
{
    private static RouteSummaryManager instance = null;

    private Vector<IRouteSummaryListener> listeners = new Vector<IRouteSummaryListener>();

    public static RouteSummaryManager getInstance()
    {
        if (instance == null)
        {
            instance = new RouteSummaryManager();
        }

        return instance;
    }

    public void registerRouteSummaryListener(IRouteSummaryListener listener)
    {
        if (!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    public void removeRouteSummaryListener(IRouteSummaryListener listener)
    {
        if (listeners.contains(listener))
        {
            listeners.remove(listener);
        }
    }

    public void clear()
    {
        listeners.removeAllElements();
    }

    public void routeSummaryUpdated()
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            IRouteSummaryListener listener = listeners.elementAt(i);
            listener.routeSummaryUpdated();
        }
    }
}

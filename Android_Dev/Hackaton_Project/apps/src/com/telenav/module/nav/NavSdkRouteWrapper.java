/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteWrapper.java
 *
 */
package com.telenav.module.nav;

import java.util.Vector;

import com.telenav.data.datatypes.route.NavSdkRoute;
import com.telenav.datatypes.route.Route;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 12, 2010
 */
public class NavSdkRouteWrapper
{
    public final static int ROUTE_NONE = -1;
    
    protected Vector routes = new Vector();
    protected NavSdkRoute currentRoute;
    protected int routeStyle;
    private Vector listeners = new Vector();
    
    private static NavSdkRouteWrapper instance = new NavSdkRouteWrapper();
    
    public static NavSdkRouteWrapper getInstance()
    {
        return instance;
    }
    
    public synchronized void clear()
    {
        routes.removeAllElements();
        currentRoute = null;
    }
    
    public synchronized void addRoutes(Route[] rs)
    {
        for(int i = 0; i < rs.length; i++)
        {
            routes.addElement(rs[i]);
        }
    }
    
    public synchronized void addRoute(Route rs)
    {
        routes.addElement(rs);
    }
    
    public synchronized Vector getRoutes()
    {
        return this.routes;
    }
    
    public synchronized NavSdkRoute getRoute(int routeId)
    {
        if(this.routes == null)
            return null;
        
        if(currentRoute != null && currentRoute.getRouteID() == routeId)
            return currentRoute;
        
        for(int i = 0; i < this.routes.size(); i++)
        {
            NavSdkRoute route = (NavSdkRoute)this.routes.elementAt(i);
            if(route.getRouteID() == routeId)
            {
                return route;
            }
        }
        
        return null;
    }
    
    public synchronized void setCurrentRouteId(int routeId)
    {
        if(this.currentRoute == null || this.currentRoute.getRouteID() != routeId)
        {
            this.currentRoute = getRoute(routeId);
        }
        
        for (int i = 0; i < listeners.size(); i++)
        {
            IRouteChangeListener listener = (IRouteChangeListener) listeners.elementAt(i);
            listener.onRouteChange(routeId);
        }
    }
    
    public void addRouteChangeListener(IRouteChangeListener listener)
    {
        listeners.add(listener);
    }
    
    public void removeRouteChangeListener(IRouteChangeListener listener)
    {
        listeners.removeElement(listener);
    }
    
    public void removeAllRouteChangeListeners()
    {
        listeners.removeAllElements();
    }
    
    public synchronized int getCurrentRouteId()
    {
        if(this.currentRoute == null)
         return -1;
        
        return this.currentRoute.getRouteID();
    }
    
    public synchronized NavSdkRoute getCurrentRoute()
    {
        if(this.currentRoute == null)
            return null;
        
        return this.currentRoute;
    }
    
    public int getRouteStyle()
    {
        return this.routeStyle;
    }
    
    public void setRouteStyle(int routeStyle)
    {
        this.routeStyle = routeStyle;
    }
}

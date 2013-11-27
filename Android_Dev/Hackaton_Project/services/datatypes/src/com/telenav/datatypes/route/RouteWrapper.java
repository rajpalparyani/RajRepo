/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteWrapper.java
 *
 */
package com.telenav.datatypes.route;

import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 12, 2010
 */
public class RouteWrapper
{
    public final static int ROUTE_NONE = -1;
    
    protected Vector routes = new Vector();
    protected Route currentRoute;
    protected int routeStyle;
    
    private static RouteWrapper instance = new RouteWrapper();
    
    public static RouteWrapper getInstance()
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
    
    public synchronized Route getRoute(int routeId)
    {
        if(this.routes == null)
            return null;
        
        if(currentRoute != null && currentRoute.getRouteID() == routeId)
            return currentRoute;
        
        for(int i = 0; i < this.routes.size(); i++)
        {
            Route route = (Route)this.routes.elementAt(i);
            if(route.getRouteID() == routeId)
            {
                return route;
            }
        }
        
        return null;
    }
    
    public synchronized void setCurrentRouteId(int routeId)
    {
        if(this.currentRoute != null && this.currentRoute.getRouteID() == routeId)
            return;
        
        this.currentRoute = getRoute(routeId);
    }
    
    public synchronized int getCurrentRouteId()
    {
        if(this.currentRoute == null)
         return -1;
        
        return this.currentRoute.getRouteID();
    }
    
    public synchronized Route getCurrentRoute()
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

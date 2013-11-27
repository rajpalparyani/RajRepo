/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavRouteChangeEvent.java
 *
 */
package com.telenav.nav.event;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class NavRouteChangeEvent extends NavEvent
{
    private int newRouteId;
    
    public NavRouteChangeEvent(int newRouteId)
    {
        super(TYPE_ROUTE_CHANGE);
        
        this.newRouteId = newRouteId;
    }

    public int getNewRouteId()
    {
        return this.newRouteId;
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractNavEvent.java
 *
 */
package com.telenav.nav.event;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public abstract class NavEvent
{
    public final static int TYPE_ADI = 5;

    public final static int TYPE_AUDIO = 1;
    
    public final static int TYPE_DEVIATION = 3;
    
    public final static int TYPE_GPS = 3;
    
    public final static int TYPE_INFO = 2;
    
    public final static int TYPE_ROUTE_CHANGE = 6;
    
    public final static int TYPE_START = 11;
    
    public final static int TYPE_END = 12;
    
    public final static int TYPE_ARRIVED = 4;

    protected int eventType;

    public NavEvent(int eventType)
    {
        this.eventType = eventType;
    }

    public int getEventType()
    {
        return this.eventType;
    }
}

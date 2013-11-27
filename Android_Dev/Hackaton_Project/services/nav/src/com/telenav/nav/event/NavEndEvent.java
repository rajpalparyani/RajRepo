/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavEndEvent.java
 *
 */
package com.telenav.nav.event;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class NavEndEvent extends NavEvent
{
    public static final byte STATUS_ON_TRACK = 0;

    public static final byte STATUS_DEVIATED = 1;

    public static final byte STATUS_NO_GPS = 2;

    public static final byte STATUS_GPS_ERROR = 3;

    public static final byte STATUS_ERROR = 4;

    public static final byte STATUS_STOPPED = 5;

    public static final byte STATUS_ADI = 6;

    public static final byte STATUS_EXCEPTION = 7;
    
    protected byte status;
    
    public NavEndEvent(byte status)
    {
        super(TYPE_END);
        
        this.status = status;
    }

    public byte getStatus()
    {
        return this.status;
    }
}

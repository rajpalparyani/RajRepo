/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MapDisplayMisLog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class MapDisplayMisLog extends AbstractMisLog
{
    public MapDisplayMisLog(String id, int priority)
    {
        super(id, TYPE_MAP_DISPLAY_TIME, priority);
    }
    
    public void setMapDisplayTime(long time)
    {
        this.setAttribute(ATTR_MAP_DISPLAY_TIME, String.valueOf(time));
    }
    
}

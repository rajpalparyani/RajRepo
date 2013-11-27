/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MapUpdateMisLog.java
 *
 */
package com.telenav.log.mis.log;

import java.util.Vector;

import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class MapUpdateMisLog extends AbstractMisLog
{
    private Vector mapUpdateTime = new Vector();
    
    public MapUpdateMisLog(String id, int priority)
    {
        super(id, TYPE_MAP_UPDATE_TIME, priority);
    }
    
    public void processLog()
    {
        super.processLog();
        long max = 0;
        long min = Integer.MAX_VALUE;
        long sum = 0;
        long tmp = 0;
        for (int i = 0; i < mapUpdateTime.size(); i++)
        {
            tmp = ((Integer) mapUpdateTime.elementAt(i)).intValue();
            if (tmp > max)
            {
                max = tmp;
            }
            if (tmp < min)
            {
                min = tmp;
            }
            sum += tmp;
        }
        setMapUpdateTimeMax(max);
        setMapUpdateTimeMin(min);
        setMapUpdateTimeAvg(sum / mapUpdateTime.size());
    }
    
    public void addMapUpdateTime(long time)
    {
        this.mapUpdateTime.addElement(PrimitiveTypeCache.valueOf(time));
    }
    

    public void setMapUpdateTimeMin(long minTime)
    {
        this.setAttribute(ATTR_MAP_UPDATE_MIN, String.valueOf(minTime));
    }

    public void setMapUpdateTimeAvg(long avgTime)
    {
        this.setAttribute(ATTR_MAP_UDATE_AVG, String.valueOf(avgTime));
    }

    public void setMapUpdateTimeMax(long maxTime)
    {
        this.setAttribute(ATTR_MAP_UDATE_MAX, String.valueOf(maxTime));
    }

}

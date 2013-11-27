/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DivationEvent.java
 *
 */
package com.telenav.nav.event;

import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class NavDeviationEvent extends NavEvent
{
    private TnLocation deviationPoint;
    
    private int[] deviationCount;
    
    public NavDeviationEvent(TnLocation deviationPoint, int[] deviationCount)
    {
        super(TYPE_DEVIATION);
        
        this.deviationCount = deviationCount;
        
        this.deviationPoint = deviationPoint;
    }

    public TnLocation getDeviationPoint()
    {
        return this.deviationPoint;
    }
    
    public int[] getDeviationCount()
    {
        return this.deviationCount;
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * GpsData.java
 *
 */
package com.telenav.datatypes.nav;

import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class TnNavLocation extends TnLocation
{   
    protected int minUsableSpeed = 30;
    
    /**
     * Constructor the TnNavLocation.
     * 
     * @param provider
     */
    public TnNavLocation(String provider)
    {
        super(provider);
    }
    
    /**
     * Clone location.
     * 
     * @param the location to clone
     */
    public void set(TnNavLocation copy)
    {
        super.set(copy);
        this.minUsableSpeed = copy.getMinUsableSpeed();
    }
    

    /**
     * get min usable speed for heading detect.
     * 
     * @return
     */
    public int getMinUsableSpeed()
    {
        return minUsableSpeed;
    }

    /**
     * set min usable speed for heading detect.
     * @param minSpd
     */
    public void setMinUsableSpeed(int minSpd)
    {
        this.minUsableSpeed = minSpd;
    }
}

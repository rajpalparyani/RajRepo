/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * OnboardStartupInfoMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;

/**
 *@author jxue
 *@date 2013-3-27
 */
public class OnboardStartupInfoMisLog extends AbstractMisLog
{
   
    public OnboardStartupInfoMisLog(String id, int priority)
    {
        super(id, TYPE_ON_BOARD_STARTUP_INFO, priority);
    }
    
    public void setLocation()
    {
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);

        if (location != null)
        {
            this.setAttribute(ATTR_GENERIC_LAT, String.valueOf(location.getLatitude()));
            this.setAttribute(ATTR_GENERIC_LON, String.valueOf(location.getLongitude()));
        }
    }

}

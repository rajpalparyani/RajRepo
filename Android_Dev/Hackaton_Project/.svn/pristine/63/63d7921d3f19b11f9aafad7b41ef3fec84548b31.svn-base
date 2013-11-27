/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * MapDownloadMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;

/**
 *@author yning
 *@date 2013-3-30
 */
public class MapDownloadMisLog extends AbstractMisLog
{
    public MapDownloadMisLog(String id, int eventType, int priority)
    {
        super(id, eventType, priority);
    }

    public void setRegionId(String regionId)
    {
        setAttribute(ATTR_MAP_DOWNLOAD_REGION_ID, regionId);
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

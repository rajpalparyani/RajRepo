/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkRoute.java
 *
 */
package com.telenav.data.datatypes.route;

import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.navsdk.events.PointOfInterestData.Location;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;

/**
 *@author yren
 *@date 2012-11-8
 */
public class NavSdkRoute extends Route
{
    public int[] getOriginLatLon()
    {
        int result[] = new int[2];
        PointOfInterest pointOfInterest = this.wayPoints == null ? null
                : (PointOfInterest) this.wayPoints.get(0);
        if (pointOfInterest == null)
        {
            return null;
        }

        Location location = pointOfInterest.getLocation();
        if (location == null)
        {
            return null;
        }

        result[WAYPOINTS_LAT_INDEX] = (int) (location.getLatitude() * 100000);

        result[WAYPOINTS_LON_INDEX] = (int) (location.getLongitude() * 100000);

        return result;
    }

    public int[] getDestLatLon()
    {
        int result[] = new int[2];
        PointOfInterest pointOfInterest = this.wayPoints == null ? null
                : (PointOfInterest) this.wayPoints.get(wayPoints.size() - 1);
        if (pointOfInterest == null)
        {
            return null;
        }

        Location location = pointOfInterest.getLocation();
        if (location == null)
        {
            return null;
        }

        result[WAYPOINTS_LAT_INDEX] = (int) (location.getLatitude() * 100000);

        result[WAYPOINTS_LON_INDEX] = (int) (location.getLongitude() * 100000);

        return result;
    }
    
    public int calcETA(int startSegIndex)
    {
        int eta = 0;
        
        if (startSegIndex == 0 && this.totalTimeInSeconds > 0)
        {
            return (int) ((totalTimeInSeconds < 60) ? 60 * 1000 : totalTimeInSeconds * 1000); // fix bug 22470
        }
        
        if(startSegIndex > this.segmentsSize() - 1)
        {
            return eta;
        }
        
        for(int i=startSegIndex;i< this.segmentsSize() - 1;i++)
        {
            Segment seg = segmentAt(startSegIndex);
            eta += seg.getSecondsToTurn();
        }
        //calc the start segment
        return (eta < 60) ? 60 * 1000 : eta * 1000; //fix bug 22470
    }
    
    @Override
    public boolean isPartial()
    {
        return !this.isCompeleted;
    }
}

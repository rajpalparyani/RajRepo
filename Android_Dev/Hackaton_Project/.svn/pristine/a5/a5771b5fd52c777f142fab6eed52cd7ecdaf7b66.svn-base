/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AlongRouteLocationProvider.java
 *
 */
package com.telenav.gps;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.Route;
import com.telenav.location.AbstractTnMockLocationProvider;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;

public class AlongRouteLocationProvider extends AbstractTnMockLocationProvider
{
    private static int DEFAULT_SPEED_LIMIT = 270;
    protected int speed = DEFAULT_SPEED_LIMIT; // about 60 mi/h
    // private int speed = 90; // about 20 mi/h

    // company position as default start point, can set outside
    protected int lat = 3737890;

    protected int lon = -12201074;

    protected int originLat = 3737890;

    protected int originLon = -12201074;

    protected int counter = 0;

    protected int SPIRAL_RADIUS_START = 10000; // in DM6

    protected int SPIRAL_HEADING_DELTA_START = 15; // in 1/10 of the degree

    protected int spiralRadius = SPIRAL_RADIUS_START; // in DM6

    protected int spiralHeading = 0; // in 1/10 degrees

    protected int spiralHeadingDelta = SPIRAL_HEADING_DELTA_START; // in 1/10 of the degree

    protected int prevLat = -1;

    protected int prevLon = -1;

    protected long prevTime = -1;
    
    protected boolean isSpiral = false;
    
    protected NavState onRoute;

    protected boolean isNoGps = false;
    
    protected boolean isDeviation = false;

    public AlongRouteLocationProvider(String name)
    {
        super(name);
    }

    protected TnLocation getLocationDelegate(int timeout) throws TnLocationException
    {
        if (isNoGps)
            return null;

        TnLocation location = new TnLocation(this.name);

        long gpsTime = System.currentTimeMillis() / 10;
        // not start
        if (onRoute == null)
        {
            // System.out.println("getPositionData: no onRoute: "+lat+", "+lon);
            this.lat = this.originLat;
            this.lon = this.originLon;

            location.setLatitude(this.lat);
            location.setLongitude(this.lon);
            location.setSatellites(4);
            location.setAccuracy(5);
            location.setTime(gpsTime);
			location.setEncrypt(true);
        }
        else if (isSpiral)
        {
            this.lat = this.originLat + (int) DataUtil.xCosY(spiralRadius / 10, spiralHeading / 10);
            this.lon = this.originLon + (int) DataUtil.xSinY(spiralRadius / 10, spiralHeading / 10);

            int speed = 0;
            int heading = spiralHeading / 10 + 90;
            heading = heading % 360;
            if (prevLat != -1 && prevLon != -1)
            {
                int dLat = this.lat - this.prevLat;
                int dLon = this.lon - this.prevLon;
                int dist = DataUtil.distance(dLat, dLon);
                speed = dist * 10 * 1000 / (int) (System.currentTimeMillis() - this.prevTime);
            }

            location.setLatitude(this.lat);
            location.setLongitude(this.lon);
            location.setSpeed(speed);
            location.setHeading(heading);
            location.setSatellites(4);
            location.setAccuracy(5);
            location.setTime(gpsTime);
            location.setEncrypt(true);

            spiralRadius++;
            spiralHeading += spiralHeadingDelta;
            spiralHeading = spiralHeading % 3600;
            spiralHeadingDelta = SPIRAL_HEADING_DELTA_START * SPIRAL_RADIUS_START / spiralRadius;
            if (spiralHeadingDelta < 1)
                spiralHeadingDelta = 1;
            this.prevLat = lat;
            this.prevLon = lon;
            this.prevTime = System.currentTimeMillis();
        }
        else
        {
            try
            {
                location.setLatitude(this.lat);
                location.setLongitude(this.lon);
                location.setSatellites(4);
                location.setAccuracy(5);
                location.setTime(gpsTime);
				location.setEncrypt(true);

                // at destination
                if (onRoute.isAtTheEndOfRoute())
                {
                    location.setSpeed(0);
                    location.setHeading(0);
                }
                // on road
                else
                {
                    if (counter < 5)
                    {
                        location.setSpeed(0);
                        location.setHeading(0);
                    }
                    else
                    {
                        if (isDeviation)
                        {
                            lat += 500;
                            location.setLatitude(this.lat);
                        }
                        else
                        {
                            int currLat = onRoute.getCurrentShapePointLat();
                            int currLon = onRoute.getCurrentShapePointLon();
                            int nextLat = onRoute.nextLat();
                            int nextLon = onRoute.nextLon();
                            int heading = DataUtil.bearing(currLat, currLon, nextLat, nextLon);

                            location.setLatitude(this.lat);
                            location.setLongitude(this.lon);
                            if (onRoute.getCurrentSegment() != null
                                    && onRoute.getCurrentSegment().getSpeedLimit() > 0)
                            {
                                speed = onRoute.getCurrentSegment().getSpeedLimit();
                            }
                            else
                            {
                                speed = DEFAULT_SPEED_LIMIT;
                            }
                            location.setSpeed(speed);
                            location.setHeading(heading);
                            location.setTime(gpsTime);

                            int mySpeed = speed;
                            
                            onRoute.walkRouteFromCurrentShapePoint(onRoute.getCurrentRange() + mySpeed / 10);
                            lat = onRoute.getPosition().getLatitude();
                            lon = onRoute.getPosition().getLongitude();
                        }
                    }
                    counter++;
                }
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                onRoute = null;
            }
        }

        return location;
    }

    public void setOrigin(int lat, int lon)
    {
        this.lat = lat;
        this.lon = lon;
        this.originLat = lat;
        this.originLon = lon;
    }

    public void setNavState(NavState navState)
    {
        this.onRoute = navState;
    }

    public void setDeviation(boolean isDeviation)
    {
        this.isDeviation = isDeviation;
    }
}

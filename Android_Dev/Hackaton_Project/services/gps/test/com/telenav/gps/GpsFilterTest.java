/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestGpsFilter.java
 *
 */
package com.telenav.gps;

import junit.framework.TestCase;

import com.telenav.location.TnLocation;

/**
 *@author yning
 *@date 2011-6-14
 */
public class GpsFilterTest extends TestCase
{
    public void testEliminateGpsNoise()
    {
        GpsFilter filter = new GpsFilter();
        assertFalse(filter.eliminateGpsNoise(null));
        
        TnLocation gpsData = new TnLocation("");
        gpsData.setAccuracy(50);
        gpsData.setHeading(0);
        gpsData.setLatitude(3737392);
        gpsData.setLongitude(-12201074);
        gpsData.setSatellites(5);
        gpsData.setSpeed(20);
        gpsData.setTime(System.currentTimeMillis());
        gpsData.setLocalTimeStamp(System.currentTimeMillis());
        gpsData.setValid(true);
        assertTrue(filter.eliminateGpsNoise(gpsData));
        
        long time = gpsData.getTime();
        gpsData.setTime(-1);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setTime(time);
        
        int lat = gpsData.getLatitude();
        gpsData.setLatitude(10000000);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setLatitude(lat);
        
        int lon = gpsData.getLongitude();
        gpsData.setLongitude(20000000);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setLongitude(lon);
        
        int speed = gpsData.getSpeed();
        gpsData.setSpeed(-1);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setSpeed(5000);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setSpeed(speed);
        
        int heading = gpsData.getHeading();
        gpsData.setHeading(-1);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setHeading(361);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setHeading(heading);
        
        int accuracy = gpsData.getAccuracy();
        gpsData.setAccuracy(501);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setAccuracy(accuracy);
        
        gpsData.setLatitude(0);
        assertFalse(filter.eliminateGpsNoise(gpsData));
        gpsData.setLatitude(lat);
      
    }
    
    public void testEliminateGpsNoiseWithPreFix()
    {
        GpsFilter filter = new GpsFilter();
        TnLocation gpsData = new TnLocation("");
        for(int i=0; i<40; i++)
        {
            gpsData.setAccuracy(50);
            gpsData.setHeading(0);
            gpsData.setLatitude(3737392+i*10);
            gpsData.setLongitude(-12201074+i*10);
            gpsData.setSatellites(5);
            gpsData.setSpeed(20);
            gpsData.setTime(System.currentTimeMillis()*100);
            gpsData.setLocalTimeStamp(System.currentTimeMillis()*100);
            gpsData.setValid(true);
            assertTrue(filter.eliminateGpsNoise(gpsData));
        }
    }
    
    public void testIsGpsValid()
    {
        GpsFilter filter = new GpsFilter();
        assertFalse(filter.isGpsValid(null));
        TnLocation location = new TnLocation("");
        location.setLatitude(0);
        assertFalse(filter.isGpsValid(location));
        location.setLatitude(3737392);
        location.setLongitude(-12201074);
        assertTrue(filter.isGpsValid(location));
    }
}

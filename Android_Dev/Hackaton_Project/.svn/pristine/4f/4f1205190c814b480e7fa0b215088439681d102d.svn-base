/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnNavLocationTest.java
 *
 */
package com.telenav.datatypes.nav;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-7
 */
public class TnNavLocationTest
{
    TnNavLocation location;
    String provider = "test";
    
    @Before
    public void setup()
    {
        location = new TnNavLocation(provider);
    }
    
    @Test
    public void testMinUsableSpeed()
    {
        //the default value is 30.
        assertEquals(30, location.getMinUsableSpeed());
        int minUsableSpeed = 50;
        location.setMinUsableSpeed(minUsableSpeed);
        assertEquals(minUsableSpeed, location.getMinUsableSpeed());
    }
    
    @Test
    public void testSet()
    {
        String provider = "other";
        int accuracy = 50;
        int altitude = 300;
        int heading = 45;
        int latitude = 3737390;
        int longitude = -12201074;
        int satellites = 8;
        int speed = 60;
        long time = System.currentTimeMillis() / 10;
        long localTime = System.currentTimeMillis();
        boolean isValid = true;
        TnNavLocation other = new TnNavLocation(provider);
        other.setAccuracy(accuracy);
        other.setAltitude(altitude);
        other.setHeading(heading);
        other.setLatitude(latitude);
        other.setLongitude(longitude);
        other.setSatellites(satellites);
        other.setSpeed(speed);
        other.setTime(time);
        other.setValid(isValid);
        other.setLocalTimeStamp(localTime);
        
        location.set(other);
        assertEquals(provider, location.getProvider());
        assertEquals(accuracy, location.getAccuracy());
        assertEquals(altitude, location.getAltitude(), 0.0f);
        assertEquals(heading, location.getHeading());
        assertEquals(latitude, location.getLatitude());
        assertEquals(longitude, location.getLongitude());
        assertEquals(satellites, location.getSatellites());
        assertEquals(speed, location.getSpeed());
        assertEquals(time, location.getTime());
        assertEquals(isValid, location.isValid());
        assertEquals(localTime, location.getLocalTimeStamp());
    }
}

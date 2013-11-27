/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnLocationTest.java
 *
 */
/**
 * 
 */
package com.telenav.location;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-7
 */
public class TnLocationTest
{
    TnLocation tnLocation;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        tnLocation = new TnLocation("mock");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.location.TnLocation#TnLocation(java.lang.String)}.
     */
    @Test
    public void testTnLocation()
    {
        assertEquals("mock", tnLocation.getProvider());
    }

    /**
     * Test method for {@link com.telenav.location.TnLocation#getProvider()}.
     */
    @Test
    public void testGetProvider()
    {
        assertEquals("mock", tnLocation.getProvider());
    }

    /**
     * Test method for {@link com.telenav.location.TnLocation#getTime()}.
     */
    @Test
    public void testGetTime()
    {
        tnLocation.setTime(12342134);
        assertEquals(12342134, tnLocation.getTime());
    }

    

    /**
     * Test method for {@link com.telenav.location.TnLocation#getSpeed()}.
     */
    @Test
    public void testGetSpeed()
    {
        tnLocation.setSpeed(60);
        assertEquals(60, tnLocation.getSpeed());
    }


    /**
     * Test method for {@link com.telenav.location.TnLocation#getAltitude()}.
     */
    @Test
    public void testGetAltitude()
    {
        tnLocation.setAltitude(30f);
        assertTrue(30 == tnLocation.getAltitude());
    }


    /**
     * Test method for {@link com.telenav.location.TnLocation#getSatellites()}.
     */
    @Test
    public void testGetSatellites()
    {
        tnLocation.setSatellites(3);
        assertEquals(3, tnLocation.getSatellites());
    }

   
    /**
     * Test method for {@link com.telenav.location.TnLocation#isValid()}.
     */
    @Test
    public void testIsValid()
    {
        tnLocation.setValid(true);
        assertTrue(tnLocation.isValid());
    }


    /**
     * Test method for {@link com.telenav.location.TnLocation#getHeading()}.
     */
    @Test
    public void testGetHeading()
    {
        tnLocation.setHeading(1);
        assertEquals(1, tnLocation.getHeading());
    }

   

    /**
     * Test method for {@link com.telenav.location.TnLocation#setLatitude(int)}.
     */
    @Test
    public void testSetLatitude()
    {
        tnLocation.setLatitude(1000000);
        assertEquals(1000000, tnLocation.getLatitude());
    }

    /**
     * Test method for {@link com.telenav.location.TnLocation#setLongitude(int)}.
     */
    @Test
    public void testSetLongitude()
    {
        tnLocation.setLongitude(1000000);
        assertEquals(1000000, tnLocation.getLongitude());
    }


    /**
     * Test method for {@link com.telenav.location.TnLocation#getAccuracy()}.
     */
    @Test
    public void testGetAccuracy()
    {
        tnLocation.setAccuracy(100);
        assertEquals(100, tnLocation.getAccuracy());
    }

    /**
     * Test method for {@link com.telenav.location.TnLocation#isEncrypt()}.
     */
    @Test
    public void testIsEncrypt()
    {
        tnLocation.setEncrypt(true);
        assertEquals(true, tnLocation.isEncrypt());
    }


    /**
     * Test method for {@link com.telenav.location.TnLocation#getLocalTimeStamp()}.
     */
    @Test
    public void testGetLocalTimeStamp()
    {
        tnLocation.setLocalTimeStamp(10000000);
        assertEquals(10000000, tnLocation.getLocalTimeStamp());
    }


    /**
     * Test method for {@link com.telenav.location.TnLocation#set(com.telenav.location.TnLocation)}.
     */
    @Test
    public void testSet()
    {
        TnLocation location = new TnLocation("mock_location");
        location.accuracy = 60;
        location.altitude = 30;
        location.hasEnc = true;
        location.heading = 1;
        location.isValid = true;
        location.latitude = 3700000;
        location.localTime = 5000000;
        location.longitude = 12200000;
        location.satellites = 3;
        location.speed = 60;
        location.time = 1000000;
        tnLocation.set(location);
        assertEquals(location.accuracy, location.accuracy);
        assertTrue(location.altitude == location.altitude);
        assertEquals(location.hasEnc, location.hasEnc);
        assertEquals(location.heading, location.heading);
        assertEquals(location.isValid, location.isValid);
        assertEquals(location.latitude, location.latitude);
        assertEquals(location.localTime, location.localTime);
        assertEquals(location.longitude, location.longitude);
        assertEquals(location.satellites, location.satellites);
        assertEquals(location.speed, location.speed);
        assertEquals(location.time, location.time);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocation#toString()}.
     */
    @Test
    public void testToString()
    {
        assertNotNull(tnLocation.toString());
    }

}

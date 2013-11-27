package com.telenav.navservice.location;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.TnLocation;
import com.telenav.navservice.TestUtil;

public class StationaryMonitorTest
{

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testIsMoving() throws Exception
    {
        StationaryMonitor monitor = new StationaryMonitor(2, 2000, 1);
        TnLocation loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(12);
        loc.setValid(true);
        assertTrue(monitor.isMoving(loc));

        loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(10);
        loc.setValid(true);
        assertTrue(monitor.isMoving(loc));

        loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(9);
        loc.setValid(true);
        assertFalse(monitor.isMoving(loc));

        loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(1);
        loc.setValid(true);
        assertFalse(monitor.isMoving(loc));
    }
}

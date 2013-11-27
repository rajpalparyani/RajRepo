package com.telenav.navservice.location;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.TnLocation;
import com.telenav.navservice.TestUtil;

public class StationaryMonitorTestTiming
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

    @Test(timeout = 5000)
    public void testIsStationary() throws Exception
    {
        StationaryMonitor monitor = new StationaryMonitor(3, 20, 1);
        TnLocation loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(100);
        loc.setValid(true);
        assertFalse(monitor.isStationary(loc));

        long time = System.currentTimeMillis();
        loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(5);
        loc.setValid(true);

        while(!monitor.isStationary(loc))
        {
            Thread.sleep(10);
            loc = new TnLocation("gps");
            loc.setLatitude(10);
            loc.setLongitude(10);
            loc.setSpeed(5);
            loc.setValid(true);
        }
        assertTrue(System.currentTimeMillis() - time > 20);
        assertTrue(monitor.isStationary(loc));
    }
    
    @Test(timeout = 5000)
    public void testIsStationary_fixCount() throws Exception
    {
        StationaryMonitor monitor = new StationaryMonitor(3, 20, 1);
        TnLocation loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(100);
        loc.setValid(true);
        assertFalse(monitor.isStationary(loc));
        
        Thread.sleep(20);
        loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(5);
        loc.setValid(true);
        assertFalse(monitor.isStationary(loc));

        Thread.sleep(20);
        loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(5);
        loc.setValid(true);
        assertFalse(monitor.isStationary(loc));

        while(System.currentTimeMillis() - monitor.lastTime <= 20)
            Thread.sleep(5);
        
        loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(5);
        loc.setValid(true);
        assertTrue(monitor.isStationary(loc));
    }
    
    @Test(timeout = 5000)
    public void testIsStationary_duration() throws Exception
    {
        StationaryMonitor monitor = new StationaryMonitor(3, 20, 1);
        for (int i=0; i<10; i++)
        {
            TnLocation loc = new TnLocation("gps");
            loc.setLatitude(10);
            loc.setLongitude(10);
            loc.setSpeed(5);
            loc.setValid(true);
            assertFalse(monitor.isStationary(loc));
        }

        while(System.currentTimeMillis() - monitor.lastTime <= 20)
            Thread.sleep(5);
        
        TnLocation loc = new TnLocation("gps");
        loc.setLatitude(10);
        loc.setLongitude(10);
        loc.setSpeed(5);
        loc.setValid(true);
        assertTrue(monitor.isStationary(loc));
    }
    
    @Test(timeout = 15000)
    public void testNotStationary() throws Exception
    {
        StationaryMonitor monitor = new StationaryMonitor(2, 2000, 1);
        for (int i=0; i<5; i++)
        {
            TnLocation loc = new TnLocation("gps");
            loc.setLatitude(10);
            loc.setLongitude(10);
            loc.setSpeed(12);
            loc.setValid(true);
            assertFalse(monitor.isStationary(loc));
            Thread.sleep(1000);
        }
    }
    
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GlobalCoordinateUtilTest.java
 *
 */
package com.telenav.datatypes;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-7-4
 */
public class GlobalCoordinateUtilTest extends TestCase
{
    public void testComputeLat()
    {
        int x = 33554423;
        int expected = -8505112;
        assertEquals(expected, GlobalCoordinateUtil.computeLat(x));
        
        x = 9;
        expected = 8505112;
        assertEquals(expected, GlobalCoordinateUtil.computeLat(x));
        
        x = 33520872;
        expected = -8501997;
        assertEquals(expected, GlobalCoordinateUtil.computeLat(x));
        
        x = 1351360;
        expected = 8362874;
        assertEquals(expected, GlobalCoordinateUtil.computeLat(x));
    }
    
    public void testComputeGlobal()
    {
        int x = -8505112;
        int expected = 33554423;
        assertEquals(expected, GlobalCoordinateUtil.computeGlobal(x));
        
        x = 8505112;
        expected = 9;
        assertEquals(expected, GlobalCoordinateUtil.computeGlobal(x));
        
        x = -8501997;
        expected = 33520872;
        assertEquals(expected, GlobalCoordinateUtil.computeGlobal(x));
        
        x = 8362874;
        expected = 1351360;
        assertEquals(expected, GlobalCoordinateUtil.computeGlobal(x));
    }
    
    public void testEarthLonToGlobal()
    {
        int earthLon = 0;
        int expected = 16777216;
        assertEquals(expected, GlobalCoordinateUtil.earthLonToGlobal(earthLon));
        
        earthLon = 180;
        expected = 16777383;
        assertEquals(expected, GlobalCoordinateUtil.earthLonToGlobal(earthLon));
        
        earthLon = -180;
        expected = 16777049;
        assertEquals(expected, GlobalCoordinateUtil.earthLonToGlobal(earthLon));
    }
    
    public void testGlobalToEarthLon()
    {
        int global = 16777217;
        int expected = 0;
        assertEquals(expected, GlobalCoordinateUtil.globalToEarthLon(global));
        
        global = 16777385;
        expected = 180;
        assertEquals(expected, GlobalCoordinateUtil.globalToEarthLon(global));
        
        global = 16777049;
        expected = -180;
        assertEquals(expected, GlobalCoordinateUtil.globalToEarthLon(global));
    }
    
    public void testEarthLatToGlobal()
    {
        int earthLat = -8505112;
        int expected = 33554423;
        assertEquals(expected, GlobalCoordinateUtil.earthLatToGlobal(earthLat));
        
        earthLat = 8505112;
        expected = 9;
        assertEquals(expected, GlobalCoordinateUtil.earthLatToGlobal(earthLat));
        
        earthLat = -8501997;
        expected = 33520872;
        assertEquals(expected, GlobalCoordinateUtil.earthLatToGlobal(earthLat));
    }
    
    public void testGlobalToEarthLat()
    {
        int x = 33554423;
        int expected = -8505112;
        assertEquals(expected, GlobalCoordinateUtil.globalToEarthLat(x));
        
        x = 9;
        expected = 8505112;
        assertEquals(expected, GlobalCoordinateUtil.globalToEarthLat(x));
        
        x = 33520872;
        expected = -8501997;
        assertEquals(expected, GlobalCoordinateUtil.globalToEarthLat(x));
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * DecimatedRouteTest.java
 *
 */
package com.telenav.datatypes.route;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-8
 */
public class DecimatedRouteTest
{
    static byte[] data;
    DecimatedRoute route;
    @BeforeClass
    public static void initRoute()
    {
        data = RouteUtil.getDecimateRouteData();
    }
    
    @Before
    public void setup()
    {
        route = new DecimatedRoute(data);
    }
    
    @Test
    public void testConstructor()
    {
        assertEquals(1, route.segmentsSize());
        Segment seg = route.segmentAt(0);
        assertEquals(1, seg.edgesSize());
        RouteEdge edge = seg.getEdge(0);
        assertEquals(-1, edge.getId());
        assertEquals(154, edge.numPoints());
        int[] boundingBox = route.getBoundingBox();
        assertArrayEquals(new int[]{2147483647, 2147483647, -2147483648, -2147483648}, boundingBox);
    }
    
    @Test
    public void testOriginTimeStamp()
    {
        //it always return 0;
        assertEquals(0, route.getOriginTimeStamp());
        route.setOriginTimeStamp(System.currentTimeMillis());
        assertEquals(0, route.getOriginTimeStamp());
    }
    
    @Test
    public void testIsFinalRoute()
    {
        //it is always false.
        assertFalse(route.isFinalRoute());
    }
    
    @Test
    public void testOriginVn()
    {
        //it always return 0;
        assertEquals(0, route.getOriginVn());
        route.setOriginVn(1000);
        assertEquals(0, route.getOriginVn());
    }
    
    @Test
    public void testOriginVe()
    {
        //it always return 0;
        assertEquals(0, route.getOriginVe());
        route.setOriginVe(1000);
        assertEquals(0, route.getOriginVe());
    }
    
    @Test
    public void testGetWalkBackTolerance()
    {
        //it always return 0;
        assertEquals(0, route.getWalkBackTolerance());
    }
    
    @Test
    public void testIsPartial()
    {
        //it is always false.
        assertFalse(route.isPartial());
    }
    
    @Test
    public void testGetLength()
    {
        //it always return 0;
        assertEquals(0, route.getLength());
    }
    
    @Test
    public void testCalcETA()
    {
        //it always return 0;
        assertEquals(0, route.calcETA(true, 20));
        assertEquals(0, route.calcETA(false, 20));
        assertEquals(0, route.calcETA(0, 0, 0, 0, true, 20));
        assertEquals(0, route.calcETA(0, 0, 0, 0, false, 20));
    }
}


/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteDataFactoryTest.java
 *
 */
package com.telenav.datatypes.route;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;
/**
 *@author yning
 *@date 2011-7-8
 */
public class RouteDataFactoryTest
{
    RouteDataFactory factory;
    
    @Before
    public void setup()
    {
        factory = new RouteDataFactory();
    }
    
    @Test
    public void testInit()
    {
        Field field = Whitebox.getField(RouteDataFactory.class, "initCount");
        try
        {
            field.set(factory, 0);
        }
        catch (IllegalArgumentException e)
        {
            fail();
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            fail();
            e.printStackTrace();
        }
        
        assertNotSame(factory, RouteDataFactory.getInstance());
        RouteDataFactory.init(factory);
        assertSame(factory, RouteDataFactory.getInstance());
        try
        {
            assertEquals(1, field.getInt(factory));
        }
        catch (IllegalArgumentException e)
        {
            fail();
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            fail();
            e.printStackTrace();
        }
        
        RouteDataFactory other = new RouteDataFactory();
        RouteDataFactory.init(other);
        
        assertNotSame(other, RouteDataFactory.getInstance());
        assertSame(factory, RouteDataFactory.getInstance());
        try
        {
            assertEquals(1, field.getInt(factory));
        }
        catch (IllegalArgumentException e)
        {
            fail();
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            fail();
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCreateDecimateRoute()
    {
        byte[] data = RouteUtil.getDecimateRouteData();
        DecimatedRoute expected = new DecimatedRoute(data);
        DecimatedRoute actual = factory.createDecimatedRoute(data);
        assertEquals(expected.getRouteID(), actual.getRouteID());
        assertArrayEquals(expected.getBoundingBox(), actual.getBoundingBox());
        Segment[] segs1 = expected.getSegments();
        Segment[] segs2 = actual.getSegments();
        assertEquals(segs1.length, segs2.length);
        Segment seg1 = segs1[0];
        Segment seg2 = segs2[0];
        assertEquals(seg1.edgesSize(), seg2.edgesSize());
        RouteEdge edge1 = seg1.getEdge(0);
        RouteEdge edge2 = seg2.getEdge(0);
        assertEquals(edge1.getId(), edge2.getId());
    }
    
    @Test
    public void testCreateRoute()
    {
        Route route = factory.createRoute();
        assertNotNull(route);
        assertNull(route.getSegments());
    }
    
    @Test
    public void testCreateRouteEdge()
    {
        RouteEdge edge = factory.createRouteEdge();
        assertNotNull(edge);
        assertNull(edge.shapePoints);
        assertNull(edge.shapePointsOffset);
        assertNull(edge.getBoundingBox());
    }
    
    @Test
    public void testCreateSegment()
    {
        Segment seg = factory.createsSegment();
        assertNotNull(seg);
        assertNull(seg.edges);
    }
}

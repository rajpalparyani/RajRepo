/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TrafficDataFactoryTest.java
 *
 */
package com.telenav.datatypes.traffic;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-11
 */
public class TrafficDataFactoryTest
{
    TrafficDataFactory factory;
    
    @Before
    public void setup()
    {
        factory = new TrafficDataFactory();
    }
    
    @Test
    public void testInit()
    {
        Field field = Whitebox.getField(TrafficDataFactory.class, "initCount");
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
        
        assertNotSame(factory, TrafficDataFactory.getInstance());
        TrafficDataFactory.init(factory);
        assertSame(factory, TrafficDataFactory.getInstance());
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
        
        TrafficDataFactory other = new TrafficDataFactory();
        TrafficDataFactory.init(other);
        
        assertNotSame(other, TrafficDataFactory.getInstance());
        assertSame(factory, TrafficDataFactory.getInstance());
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
    public void testCreateTrafficEdge()
    {
        VectorTrafficEdge edge = factory.createTrafficEdge();
        assertNotNull(edge);
    }
    
    @Test
    public void testCreateTrafficIncident()
    {
        TrafficIncident incident = factory.createTrafficIncident();
        assertNotNull(incident);
    }
    
    @Test
    public void testCreateTrafficSegment()
    {
        TrafficSegment seg = factory.createTrafficSegment();
        assertNotNull(seg);
    }
}

package com.telenav.navservice.location;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.TnLocation;
import com.telenav.navservice.TestUtil;

public class LocationBufferTest
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
    public void testAddLocation() throws Exception
    {
        LocationBuffer buffer = new LocationBuffer(100, null);
        buffer.addLocation(new TnLocation(""));
        assertEquals(buffer.size(), 1);
        buffer.addLocation(new TnLocation(""));
        assertEquals(buffer.size(), 2);
        
        buffer = new LocationBuffer(3, null);
        
        TnLocation loc1 = new TnLocation("1");
        buffer.addLocation(loc1);
        assertEquals(buffer.size(), 1);
        
        TnLocation loc2 = new TnLocation("2");
        buffer.addLocation(loc2);
        assertEquals(buffer.size(), 2);
        
        TnLocation loc3 = new TnLocation("3");
        buffer.addLocation(loc3);
        assertEquals(buffer.size(), 3);
        
        TnLocation loc4 = new TnLocation("4");
        buffer.addLocation(loc4);
        assertEquals(buffer.size(), 3);
        
        TnLocation loc5 = new TnLocation("5");
        buffer.addLocation(loc5);
        assertEquals(buffer.size(), 3);
        assertEquals(buffer.buffer.firstElement(), loc3);
        assertEquals(buffer.buffer.lastElement(), loc5);
    }
    
    @Test
    public void testLocationListener() throws Exception
    {
        LocationBuffer buffer = new LocationBuffer(100, null);
        buffer.addLocation(new TnLocation("gps"));
        assertEquals(buffer.size(), 1);

        buffer = new LocationBuffer(100, new LocationBuffer.ILocationListener()
        {
            public boolean locationArrived(TnLocation location)
            {
                return false;
            }
        });
        buffer.addLocation(new TnLocation("gps"));
        buffer.addLocation(new TnLocation("gps"));
        buffer.addLocation(new TnLocation("gps"));
        assertEquals(buffer.size(), 0);

        buffer = new LocationBuffer(100, new LocationBuffer.ILocationListener()
        {
            public boolean locationArrived(TnLocation location)
            {
                return true;
            }
        });
        buffer.addLocation(new TnLocation("gps"));
        buffer.addLocation(new TnLocation("gps"));
        buffer.addLocation(new TnLocation("gps"));
        assertEquals(buffer.size(), 3);
    }
    
    @Test
    public void testExtractAllLocations() throws Exception
    {
        LocationBuffer buffer = new LocationBuffer(100, null);
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        
        Vector v = buffer.extractAllLocations();
        assertEquals(v.size(), 4);
        assertEquals(buffer.buffer.size(), 0);
    }
    
    @Test
    public void testExtractLocations() throws Exception
    {
        LocationBuffer buffer = new LocationBuffer(100, null);
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        buffer.addLocation(new TnLocation(""));
        
        Vector v = buffer.extractLocations(5);
        assertEquals(v.size(), 5);
        assertEquals(buffer.buffer.size(), 3);
        
        v = buffer.extractLocations(8);
        assertEquals(v.size(), 3);
        assertEquals(buffer.buffer.size(), 0);
    }
}

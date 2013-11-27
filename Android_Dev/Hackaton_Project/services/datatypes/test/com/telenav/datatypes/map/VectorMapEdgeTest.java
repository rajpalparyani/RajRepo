/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * VectorMapEdgeTest.java
 *
 */
package com.telenav.datatypes.map;

import org.junit.Before;
import org.junit.Test;

import com.telenav.datatypes.DataUtil;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-6
 */
public class VectorMapEdgeTest
{
    VectorMapEdge edge;
    
    @Before
    public void setup()
    {
        edge = new VectorMapEdge();
    }
    
    @Test
    public void testGetIconType()
    {
        assertEquals(0, edge.getIconType());
        edge.iconType = 1;
        assertEquals(1, edge.getIconType());
    }
    
    @Test
    public void testGetSpeedLimit()
    {
        assertEquals(0, edge.getSpeedLimit());
        edge.speedLimit = 1;
        assertEquals(1, edge.getSpeedLimit());
    }
    
    @Test
    public void testSetGetStreetName()
    {
        assertNull(edge.getStreetName());
        String name = "kifer rd";
        edge.setStreetName(name);
        assertEquals(name, edge.getStreetName());
    }
    
    @Test
    public void testIsIsland()
    {
        assertEquals(false, edge.isIsland());
        boolean isIsland = false;
        edge.setIsIsland(isIsland);
        assertEquals(isIsland, edge.isIsland());
        isIsland = true;
        edge.setIsIsland(isIsland);
        assertEquals(isIsland, edge.isIsland());
    }
    
    @Test
    public void testIsPolygon()
    {
        assertFalse(edge.isPolygon());
        edge.isPolygon = true;
        assertTrue(edge.isPolygon());
    }
    
    @Test
    public void testSetRoadTypeIsPolygon()
    {
        assertEquals(0, edge.getRoadType());
        assertFalse(edge.isPolygon());
        
        edge.setRoadType(DataUtil.PARK);
        assertEquals(DataUtil.PARK, edge.getRoadType());
        assertTrue(edge.isPolygon());
    }
    
    @Test
    public void testSetRoadTypeIsNotPolygon()
    {
        assertEquals(0, edge.getRoadType());
        assertFalse(edge.isPolygon());
        
        edge.setRoadType(DataUtil.LOCAL_STREET);
        assertEquals(DataUtil.LOCAL_STREET, edge.getRoadType());
        assertFalse(edge.isPolygon());
    }
}

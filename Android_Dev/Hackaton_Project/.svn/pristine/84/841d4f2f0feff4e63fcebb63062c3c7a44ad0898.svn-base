/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractVectorEdgeTest.java
 *
 */
package com.telenav.datatypes.map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *@author yning
 *@date 2011-7-4
 */
public class AbstractVectorEdgeTest
{
    AbstractVectorEdge edge;
    
    @Before
    public void setUp()
    {
        edge = new AbstractVectorEdge();
    }
    
    @Test
    public void testAltitudeAt()
    {
        assertEquals(0, edge.altitudeAt(0));
    }
    
    @Test
    public void testId()
    {
        edge.setId(100);
        assertEquals(100, edge.getID());
    }
    
    @Test
    public void testRenderingData()
    {
        Object obj = new Object();
        edge.setRenderingData(obj);
        assertEquals(obj, edge.getRenderingData());
    }
    
    @Test
    public void testRoadType()
    {
        byte roadType = 100;
        edge.setRoadType(roadType);
        assertEquals(roadType, edge.getRoadType());
    }
    
    @Test
    public void testShapePoint()
    {
        assertEquals(0, edge.getShapePointsSize());
        
        int[] shapePoints = new int[]{1,1,2,2,3,3};
        edge.setShapePoints(shapePoints);
        
        assertEquals(3, edge.getShapePointsSize());
    }
    
    @Test
    public void testLatLonAt()
    {
        int[] shapePoints = new int[]{1,1,2,2,3,3};
        edge.setShapePoints(shapePoints);
        
        assertEquals(1, edge.latAt(0));
        assertEquals(1, edge.lonAt(0));
        assertEquals(2, edge.latAt(1));
        assertEquals(2, edge.lonAt(1));
        assertEquals(3, edge.latAt(2));
        assertEquals(3, edge.lonAt(2));
    }
}

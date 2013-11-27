/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * VectorTrafficEdgeTest.java
 *
 */
package com.telenav.datatypes.traffic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-11
 */
public class VectorTrafficEdgeTest
{
    VectorTrafficEdge edge;
    
    @Before
    public void setup()
    {
        edge = new VectorTrafficEdge();
    }
    
    @Test
    public void testBoundingBox()
    {
        int[][] boundingBox = new int[][]{{3737392, -12201074}, {3738580, -12136985}};
        edge.setBoundingBox(boundingBox);
        assertArrayEquals(boundingBox, edge.getBoundingBox());
    }
    
    @Test
    public void testDirectionality()
    {
        byte directionality = 90;
        edge.setDirectionality(directionality);
        assertEquals(directionality, edge.getDirectionality());
    }
    
    @Test
    public void testSpeedLimit()
    {
        short speedLimit = 120;
        edge.setSpeedLimit(speedLimit);
        assertEquals(speedLimit, edge.getSpeedLimit());
    }
    
    @Test
    public void testTmcId()
    {
        String tmcId = "105+04400::78400005548584%78400005548585%78400005521263";
        edge.setTmcId(tmcId);
        assertEquals(tmcId, edge.getTmcId());
    }
    
    @Test
    public void testTrafficSpeed()
    {
        short trafficSpeed = 40;
        edge.setTrafficSpeed(trafficSpeed);
        assertEquals(trafficSpeed, edge.getTrafficSpeed());
    }
}

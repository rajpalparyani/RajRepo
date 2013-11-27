/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TrafficTileTest.java
 *
 */
package com.telenav.datatypes.map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.telenav.datatypes.traffic.TrafficDataFactory;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.datatypes.traffic.VectorTrafficEdge;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-6
 */
public class TrafficTileTest
{
    TrafficTile tile;
    
    @Before
    public void setup()
    {
        tile = new TrafficTile();
    }
    
    @Test
    public void testSetGetPointSize()
    {
        int pointSize = 4;
        tile.setPointSize(pointSize);
        assertEquals(pointSize, tile.getPointSize());
    }
    
    @Test
    public void testSetGetReportTime()
    {
        long reportTime = System.currentTimeMillis();
        tile.setReportTime(reportTime);
        assertEquals(reportTime, tile.getReportTime());
    }
    
    @Test
    public void testSetGetTrafficEdges()
    {
        VectorTrafficEdge e = PowerMock.createMock(VectorTrafficEdge.class);
        VectorTrafficEdge[] edges = new VectorTrafficEdge[]{e};
        tile.setTrafficEdges(edges);
        assertArrayEquals(edges, tile.getTrafficEdges());
    }
    
    @Test
    public void testSetGetTrafficIncident()
    {
        TrafficIncident incident = PowerMock.createMock(TrafficIncident.class);
        TrafficIncident[] incidents = new TrafficIncident[]{incident};
        
        tile.setTrafficIncidents(incidents);
        assertArrayEquals(incidents, tile.getTrafficIncidents());
    }
    
    @Test
    public void testReset()
    {
        VectorTrafficEdge e = PowerMock.createMock(VectorTrafficEdge.class);
        VectorTrafficEdge[] edges = new VectorTrafficEdge[]{e};
        tile.setTrafficEdges(edges);
        
        TrafficIncident incident = PowerMock.createMock(TrafficIncident.class);
        TrafficIncident[] incidents = new TrafficIncident[]{incident};
        tile.setTrafficIncidents(incidents);
        
        tile.reset();
        assertNull(tile.getTrafficEdges());
        assertNull(tile.getTrafficIncidents());
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteEdgeTest.java
 *
 */
package com.telenav.datatypes.route;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-8
 */
public class RouteEdgeTest
{
    
    RouteEdge edge;
    Segment segment;
    @Before
    public void setup()
    {
        edge = new RouteEdge();
        edge.id = 161;
        edge.isLocallyGenerated = false;
        edge.isValid = true;
        edge.length = 6;
        edge.refLat = 3737862;
        edge.refLon = -12201065;
        edge.roadType = 15;
        edge.speedKph = -128;
        edge.speedLimit = 0;
        edge.speedMph = -128;
        edge.boundingBox = new int[]{3737860, -12201066, 3737865, -12201063};
        edge.shapePointsOffset = new short[]{-2, 2, 3, -1};
        segment = PowerMock.createMock(Segment.class);
        edge.setSegment(segment);
    }
    
    @Test
    public void testBoundingBox()
    {
        int[] expected = new int[]{3737860, -12201066, 3737865, -12201063};
        assertArrayEquals(expected, edge.getBoundingBox());
    }
    
    @Test
    public void testGetStreetName()
    {
        String streetName = "test";
        EasyMock.expect(segment.getStreetName()).andReturn(streetName);
        
        PowerMock.replayAll();
        
        assertEquals(streetName, edge.getStreetName());
        
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetStreetAlias()
    {
        String streetNameAlias = "test";
        EasyMock.expect(segment.getStreetAlias()).andReturn(streetNameAlias);
        
        PowerMock.replayAll();
        
        assertEquals(streetNameAlias, edge.getStreetAlias());
        
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetBinsize()
    {
        assertEquals(11, edge.getBinsize(edge));
    }
    
    @Test
    public void testLength()
    {
        int length = 6;
        assertEquals(length, edge.getLength());
        length = 12;
        edge.setLength(length);
        assertEquals(length, edge.getLength());
    }
    
    @Test
    public void testRoadType()
    {
        byte roadType = 15;
        assertEquals(roadType, edge.getRoadType());
        roadType = 100;
        edge.setRoadType(roadType);
        assertEquals(roadType, edge.getRoadType());
    }
    
    @Test
    public void testId()
    {
        int id = 161;
        assertEquals(id, edge.getId());
        assertEquals(id, edge.getID());
        
        id = 80;
        edge.setID(id);
        assertEquals(id, edge.getId());
        assertEquals(id, edge.getID());
    }
    
    @Test
    public void testSpeedLimit()
    {
        int speedLimit = 0;
        assertEquals(speedLimit, edge.getSpeedLimit());
        speedLimit = 12;
        edge.setSpeedLimit(speedLimit);
        assertEquals(speedLimit, edge.getSpeedLimit());
    }
    
    @Test
    public void testSpeedLimitKph()
    {
        int SpeedLimitKph = 0;
        assertEquals(SpeedLimitKph, edge.getSpeedLimitKph());
        SpeedLimitKph = 10;
        edge.setSpeedLimitKph(SpeedLimitKph);
        assertEquals(SpeedLimitKph, edge.getSpeedLimitKph());
    }
    
    @Test
    public void testSpeedLimitMph()
    {
        int SpeedLimitMph = 0;
        assertEquals(SpeedLimitMph, edge.getSpeedLimitMph());
        SpeedLimitMph = 10;
        edge.setSpeedLimitMph(SpeedLimitMph);
        assertEquals(SpeedLimitMph, edge.getSpeedLimitMph());
    }
    
    @Test
    public void testGetDeltaRs()
    {
        int index = 1;
        assertEquals(0, edge.getDeltaRs(index));
        
        index = 0;
        
        assertEquals(6, edge.getDeltaRs(index));
    }
    
    @Test
    public void testSetShapePoints()
    {
        int[] shapePoints = new int[]{-2, 2, -3, 4};
        short[] expected = new short[]{1, -1, 0, 1};
        edge.setShapePoints(shapePoints);
        assertArrayEquals(expected, edge.shapePointsOffset);
    }
    
    @Test
    public void testSetShapePointsBeyondShortRange()
    {
        int[] shapePoints = new int[]{-1, 1, 70000, 70002};
        edge.setShapePoints(shapePoints);
        assertNull(edge.shapePointsOffset);
        assertEquals(shapePoints, edge.shapePoints);
    }
    
    @Test
    public void testSetShapePointsNull()
    {
        assertNotNull(edge.shapePointsOffset);
        assertNotNull(edge.boundingBox);
        assertNull(edge.shapePoints);
        assertEquals(6, edge.getLength());
        assertEquals(3737862, edge.refLat);
        assertEquals(-12201065, edge.refLon);
        edge.setShapePoints(null);
        assertNull(edge.shapePointsOffset);
        assertNull(edge.boundingBox);
        assertNull(edge.shapePoints);
        assertEquals(0, edge.getLength());
        assertEquals(0, edge.refLat);
        assertEquals(0, edge.refLon);
    }
    
    @Test
    public void testNumPoints()
    {
        int[] shapePoints = new int[]{-2, 2, -3, 4};
        edge.setShapePoints(shapePoints);
        assertEquals(2, edge.numPoints());
        edge.shapePointsOffset = null;
        assertEquals(0, edge.numPoints());
        
        shapePoints = new int[]{-1, 1, 70000, 70002};
        edge.setShapePoints(shapePoints);
        assertEquals(2, edge.numPoints());
    }
    
    @Test
    public void testLatAtFromShapePointOffset()
    {
        int[] shapePoints = new int[]{-2, 2, -3, 4};
        edge.setShapePoints(shapePoints);
        
        assertEquals(-2, edge.latAt(0));
        assertEquals(-3, edge.latAt(1));
    }
    
    @Test
    public void testLatAtFromShapePoint()
    {
        int[] shapePoints = new int[]{-1, 1, 70000, 70002};
        edge.setShapePoints(shapePoints);
        
        assertEquals(-1, edge.latAt(0));
        assertEquals(70000, edge.latAt(1));
    }
    
    @Test
    public void testLonAtFromShapePointOffset()
    {
        int[] shapePoints = new int[]{-2, 2, -3, 4};
        edge.setShapePoints(shapePoints);
        
        assertEquals(2, edge.lonAt(0));
        assertEquals(4, edge.lonAt(1));
    }
    
    @Test
    public void testLonAtFromShapePoint()
    {
        int[] shapePoints = new int[]{-1, 1, 70000, 70002};
        edge.setShapePoints(shapePoints);
        
        assertEquals(1, edge.lonAt(0));
        assertEquals(70002, edge.lonAt(1));
    }
    
    @Test
    public void testSegment()
    {
        edge.setSegment(null);
        assertNull(edge.segment);
        edge.setSegment(segment);
        assertEquals(segment, edge.segment);
    }
    
    @Test
    public void testMinBounding()
    {
        //3737860, -12201066, 3737865, -12201063
        assertEquals(3737860, edge.minLat());
        assertEquals(3737865, edge.maxLat());
        assertEquals(-12201066, edge.minLon());
        assertEquals(-12201063, edge.maxLon());
    }

    @Test
    public void testIsValid()
    {
        assertTrue(edge.isValid());
    }
    
    @Test
    public void testIsInClip()
    {
        //3737860, -12201066, 3737865, -12201063
        int clipLatMin = 3737850;
        int clipLonMin = -12201070;
        int clipLatMax = 3737863;
        int clipLonMax = -12201065;
        
        assertTrue(edge.isInClip(clipLatMin, clipLonMin, clipLatMax, clipLonMax));
        
        clipLatMin = 3737830;
        clipLonMin = -12201070;
        clipLatMax = 3737850;
        clipLonMax = -12201065;
        assertFalse(edge.isInClip(clipLatMin, clipLonMin, clipLatMax, clipLonMax));
    }
    
    @Test
    public void testIsLocallyGenerated()
    {
        assertFalse(edge.isLocallyGenerated());
        edge.setIsLocallyGenerated(true);
        assertTrue(edge.isLocallyGenerated());
    }
}

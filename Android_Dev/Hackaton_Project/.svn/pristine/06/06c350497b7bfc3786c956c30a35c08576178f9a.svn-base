/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteTest.java
 *
 */
package com.telenav.datatypes.route;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.telenav.datatypes.audio.AudioDataNode;

import static org.junit.Assert.*;
/**
 *@author yning
 *@date 2011-7-8
 */
public class RouteTest
{
    int routeId;
    Route route;
    
    @Before
    public void setup()
    {
        RouteUtil.prepareRouteData();
        route = RouteWrapper.getInstance().getCurrentRoute();
        routeId = RouteWrapper.getInstance().getCurrentRouteId();
    }
    
    @Test
    public void testSegmentAt() 
    {
        Segment[] segments = route.segments;
        Segment expected = segments[5];
        Segment actual = route.segmentAt(5);
        assertEquals(expected, actual);
        assertNull(route.segmentAt(-1));
        assertNull(route.segmentAt(12));
    }
    
    @Test
    public void testSegmentSize()
    {
        Segment[] segments = route.segments;
        int size = segments.length;
        
        assertEquals(size, route.segmentsSize());
    }
    
    @Test
    public void testRouteId()
    {
        assertEquals(routeId, route.getRouteID());
        int newId = 1234;
        route.setRouteID(newId);
        assertEquals(newId, route.getRouteID());
    }
    
    @Test
    public void testOriginTimeStamp()
    {
        long originTimeStamp = route.getOriginTimeStamp();
        assertEquals(131183056900l, originTimeStamp);
        originTimeStamp = 1234;
        route.setOriginTimeStamp(originTimeStamp);
        assertEquals(originTimeStamp, route.getOriginTimeStamp());
    }
    
    @Test
    public void testOriginVn()
    {
        int originVn = route.getOriginVn();
        assertEquals(0, originVn);
        originVn = 12;
        route.setOriginVn(originVn);
        assertEquals(originVn, route.getOriginVn());
    }
    
    @Test
    public void testOriginVe()
    {
        int originVe = route.getOriginVe();
        assertEquals(0, originVe);
        originVe = 12;
        route.setOriginVe(originVe);
        assertEquals(originVe, route.getOriginVe());
    }
    
    @Test
    public void testGetWalkBackTolerance()
    {
        assertEquals(200, route.getWalkBackTolerance());
    }
    
    @Test
    public void testGetBoundingBox()
    {
        int[] expected = new int[]{3737905, -12239512, 3761604, -12201068};
        assertArrayEquals(expected, route.getBoundingBox());
        
        int[] other = new int[]{1,2,3,4};
        route.setBoundingBox(other);
        assertArrayEquals(other, route.getBoundingBox());
    }
    
    @Test
    public void testGetSegments()
    {
        Segment[] segs = route.segments;
        assertArrayEquals(segs, route.getSegments());
    }
    
    @Test
    public void testCalcETA()
    {
        int expected = 21272000;
        assertEquals(expected, route.calcETA(true, 20));
        expected = 2275000;
        assertEquals(expected, route.calcETA(false, 20));
        expected = 19974000;
        assertEquals(expected, route.calcETA(3, 4, 1, 0, true, 20));
        expected = 1901000;
        assertEquals(expected, route.calcETA(3, 4, 1, 0, false, 20));
    }
    
    @Test
    public void testIsPartial()
    {
        assertFalse(route.isPartial());
        route.segmentAt(route.segmentsSize() - 1).setEdgeResolved(false);
        assertTrue(route.isPartial());
        route.setSegments(null);
        assertTrue(route.isPartial());
    }
    
    @Test
    public void testGetLength()
    {
        long length = 42031;
        assertEquals(length, route.getLength());
        Segment[] segs = route.getSegments();
        route.length = -1;
        segs[2] = null;
        length = 40881;
        assertEquals(length, route.getLength());
    }
    
    @Test
    public void testSetTileRequestIndices()
    {
        int tileRequestSegmentIndex = 0;
        int tileRequestEdgeIndex = 0;
        int tileRequestShapeIndex = 0;
        
        assertEquals(tileRequestSegmentIndex, route.getTileRequestSegmentIndex());
        assertEquals(tileRequestEdgeIndex, route.getTileRequestEdgeIndex());
        assertEquals(tileRequestShapeIndex, route.getTileRequestShapeIndex());
        
        tileRequestSegmentIndex = 1;
        tileRequestEdgeIndex = 1;
        tileRequestShapeIndex = 1;
        route.setTileRequestIndices(tileRequestSegmentIndex, tileRequestEdgeIndex, tileRequestShapeIndex);
        assertEquals(tileRequestSegmentIndex, route.getTileRequestSegmentIndex());
        assertEquals(tileRequestEdgeIndex, route.getTileRequestEdgeIndex());
        assertEquals(tileRequestShapeIndex, route.getTileRequestShapeIndex());
    }
    
    @Test
    public void testIsTileRequestFinished()
    {
        assertFalse(route.isTileRequestFinished());
        route.setIsTileRequestFinished(true);
        assertTrue(route.isTileRequestFinished());
    }
    
    @Test
    public void testAudioRequestSegmentIndex()
    {
        int audioRequestSegmentIndex = -1;
        assertEquals(audioRequestSegmentIndex, route.getAudioRequestSegmentIndex());
        audioRequestSegmentIndex = 11;
        route.setAudioRequestSegmentIndex(audioRequestSegmentIndex);
        assertEquals(audioRequestSegmentIndex, route.getAudioRequestSegmentIndex());
    }
    
    @Test
    public void testDestinationAudio()
    {
        assertNull(route.getDestinationAudio());
        AudioDataNode node = PowerMock.createMock(AudioDataNode.class);
        route.setDestinationAudio(node);
        assertEquals(node, route.getDestinationAudio());
    }
    
    @Test
    public void testTrafficDelayTime()
    {
        int trafficDelayTime = 258;
        assertEquals(trafficDelayTime, route.getTrafficDelayTime());
        trafficDelayTime = 123;
        route.setTrafficDelayTime(trafficDelayTime, 0);
        assertEquals(trafficDelayTime, route.getTrafficDelayTime());
    }
    
    @Test
    public void testMarkedTrafficDelayDistToDest()
    {
        int markedTrafficDelayDistToDest = 42031;
        assertEquals(markedTrafficDelayDistToDest, route.getMarkedTrafficDelayDistToDest());
        markedTrafficDelayDistToDest = 99999;
        route.setTrafficDelayTime(1234, markedTrafficDelayDistToDest);
        assertEquals(markedTrafficDelayDistToDest, route.getMarkedTrafficDelayDistToDest());
    }
    
    @Test
    public void testIsLocallyGenerated()
    {
        assertFalse(route.isLocallyGenerated());
        route.setIsLocallyGenerated(true);
        assertTrue(route.isLocallyGenerated());
    }
}

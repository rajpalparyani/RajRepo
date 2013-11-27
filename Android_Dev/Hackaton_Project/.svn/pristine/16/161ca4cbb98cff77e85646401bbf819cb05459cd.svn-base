/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavStateTest.java
 *
 */
package com.telenav.datatypes.nav;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-6
 */
public class NavStateTest
{
    NavState navState;

    int routeId;

    @BeforeClass
    public static void initBeforeClass()
    {
        RouteUtil.prepareRouteData();
    }

    @Before
    public void setup()
    {
        routeId = 183054400;
        navState = new NavState(routeId);
    }

    @Test
    public void testAdjustNavStateNormal()
    {
        navState.segmentIndex = 2;
        navState.edgeIndex = 1;
        navState.pointIndex = 0;

        navState.adjustNavState();
        assertEquals(2, navState.segmentIndex);
        assertEquals(1, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
        assertEquals(0, navState.range);
    }

    @Test
    public void testAdjustNavStateRouteIdNone()
    {
        navState.segmentIndex = 2;
        navState.edgeIndex = 1;
        navState.pointIndex = 0;
        navState.routeId = RouteWrapper.ROUTE_NONE;

        navState.adjustNavState();
        assertEquals(2, navState.segmentIndex);
        assertEquals(1, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
        assertEquals(0, navState.range);
    }

    @Test
    public void testAdjustNavStateSegIndexInvalid()
    {
        navState.segmentIndex = -1;
        navState.edgeIndex = 1;
        navState.pointIndex = 1;
        navState.range = 5;

        navState.adjustNavState();
        assertEquals(0, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
        assertEquals(0, navState.range);

        navState.segmentIndex = 12;
        navState.edgeIndex = 1;
        navState.pointIndex = 1;
        navState.range = 5;

        navState.adjustNavState();
        assertEquals(10, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(3, navState.pointIndex);
        assertEquals(0, navState.range);
    }

    @Test
    public void testAdjustNavStateEdgeIndexInvalid()
    {
        navState.segmentIndex = 0;
        navState.edgeIndex = -1;
        navState.pointIndex = 1;
        navState.range = 5;

        navState.adjustNavState();
        assertEquals(0, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
        assertEquals(0, navState.range);

        navState.segmentIndex = 9;
        navState.edgeIndex = 1;
        navState.pointIndex = 1;
        navState.range = 5;

        navState.adjustNavState();
        assertEquals(10, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(1, navState.pointIndex);
        assertEquals(5, navState.range);
    }

    @Test
    public void testAdjustNavStatePointIndexInvalid()
    {
        navState.segmentIndex = 1;
        navState.edgeIndex = 0;
        navState.pointIndex = -1;
        navState.range = 5;

        navState.adjustNavState();
        assertEquals(0, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(1, navState.pointIndex);
        assertEquals(5, navState.range);

        navState.segmentIndex = 11;
        navState.edgeIndex = 0;
        navState.pointIndex = 4;
        navState.range = 5;

        navState.adjustNavState();
        assertEquals(10, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(3, navState.pointIndex);
        assertEquals(0, navState.range);
    }

    @Test
    public void testAdjustNavStateRangeNegative()
    {
        navState.segmentIndex = 0;
        navState.edgeIndex = 0;
        navState.pointIndex = 0;
        navState.range = -1;

        navState.adjustNavState();
        assertEquals(0, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
        assertEquals(0, navState.range);

        navState.segmentIndex = 1;
        navState.edgeIndex = 1;
        navState.pointIndex = 0;
        navState.range = -1;

        navState.adjustNavState();
        assertEquals(1, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(1, navState.pointIndex);
        assertEquals(64, navState.range);
    }

    @Test
    public void testAdjustNavStateRangeTooBig()
    {
        navState.segmentIndex = 0;
        navState.edgeIndex = 0;
        navState.pointIndex = 2;
        navState.range = 130;

        // current range is 128
        navState.adjustNavState();
        assertEquals(1, navState.segmentIndex);
        assertEquals(1, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
        assertEquals(15, navState.range);

        navState.segmentIndex = 9;
        navState.edgeIndex = 0;
        navState.pointIndex = 9;
        navState.range = 25;

        navState.adjustNavState();
        assertEquals(10, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(1, navState.pointIndex);
        assertEquals(9, navState.range);
    }

    @Test
    public void testCalcDistanceFromHead()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 10;
        navState.pointIndex = 1;
        navState.range = 10;

        int length = navState.calcDistanceFromHead();
        assertEquals(4577, length);
    }

    @Test
    public void testCalcDistanceFromHeadNoRoute()
    {
        navState.setRoute(12345);
        int length = navState.calcDistanceFromHead();
        assertEquals(0, length);
    }

    @Test
    public void testCalcPositionFromIndexes()
    {
        Route route = RouteWrapper.getInstance().getCurrentRoute();
        assertFalse(navState.calcPositionFromIndexes(null));

        navState.segmentIndex = -1;
        assertFalse(navState.calcPositionFromIndexes(route));

        navState.segmentIndex = 12;
        assertFalse(navState.calcPositionFromIndexes(route));

        navState.segmentIndex = 0;
        navState.edgeIndex = -1;
        assertFalse(navState.calcPositionFromIndexes(route));

        navState.segmentIndex = 0;
        navState.edgeIndex = 1;
        assertFalse(navState.calcPositionFromIndexes(route));

        navState.segmentIndex = 0;
        navState.edgeIndex = 0;
        navState.pointIndex = 5;
        assertFalse(navState.calcPositionFromIndexes(route));

        navState.segmentIndex = 0;
        navState.edgeIndex = 0;
        navState.pointIndex = 0;
        navState.range = -1;
        assertFalse(navState.calcPositionFromIndexes(route));

        navState.segmentIndex = 0;
        navState.edgeIndex = 0;
        navState.pointIndex = 0;
        navState.range = 130;
        assertFalse(navState.calcPositionFromIndexes(route));

        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        assertTrue(navState.calcPositionFromIndexes(route));

        // 3739863,-12202591,284
        assertEquals(3739863, navState.position.getLatitude());
        assertEquals(-12202591, navState.position.getLongitude());
        assertEquals(284, navState.position.getHeading());

        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 0;
        assertTrue(navState.calcPositionFromIndexes(route));

        // 3739863,-12202591,284
        assertEquals(3739861, navState.position.getLatitude());
        assertEquals(-12202579, navState.position.getLongitude());
        assertEquals(284, navState.position.getHeading());
    }

    @Test
    public void testGetCurrentDeltaR()
    {
        // segment 4 has 106 edges.
        // edge 5 has 4 points.
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;

        int deltaR = navState.getCurrDeltaR();
        assertEquals(105, deltaR);

        navState.segmentIndex = 3;
        navState.edgeIndex = 108;
        navState.pointIndex = 1;
        deltaR = navState.getCurrDeltaR();
        assertEquals(0, deltaR);
    }

    @Test
    public void testGetCurrentEdge()
    {

        // segment 4 has 106 edges.
        // edge 5 has 4 points.
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;

        RouteEdge edge = RouteWrapper.getInstance().getRoute(navState.routeId).segmentAt(3).getEdge(5);
        RouteEdge actual = navState.getCurrentEdge();
        assertEquals(edge, actual);

        navState.segmentIndex = 3;
        navState.edgeIndex = 108;
        navState.pointIndex = 1;
        actual = navState.getCurrentEdge();
        assertEquals(null, actual);
    }

    @Test
    public void testGetCurrentRange()
    {
        navState.range = 100;
        assertEquals(100, navState.getRange());
    }

    @Test
    public void testGetCurrentSegment()
    {
        navState.segmentIndex = 3;
        Segment expected = RouteWrapper.getInstance().getRoute(navState.routeId).segmentAt(3);
        Segment actual = navState.getCurrentSegment();
        assertEquals(expected, actual);

        navState.segmentIndex = -1;
        actual = navState.getCurrentSegment();
        assertNull(actual);

        navState.setRoute(123456);
        actual = navState.getCurrentSegment();
        assertNull(actual);
    }

    @Test
    public void testGetCurrentShapePointLat()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;

        long lat = navState.getCurrentShapePointLat();
        assertEquals(3739861, lat);

        navState.edgeIndex = 108;
        lat = navState.getCurrentShapePointLat();
        assertEquals(0, lat);
    }

    @Test
    public void testGetCurrentShapePointLon()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;

        long lon = navState.getCurrentShapePointLon();
        assertEquals(-12202579, lon);

        navState.edgeIndex = 108;
        lon = navState.getCurrentShapePointLon();
        assertEquals(0, lon);
    }

    @Test
    public void testGetDeltaR()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        
        int deltaR = navState.getDeltaR(navState.segmentIndex, navState.edgeIndex, navState.pointIndex);
        assertEquals(105, deltaR);
    }
    
    @Test
    public void testGetDistanceToEdge()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        
        int edgeID = RouteWrapper.getInstance().getRoute(routeId).segmentAt(navState.segmentIndex).getEdgeID(navState.edgeIndex + 20);
        int maxRange = 200000;
        int distance = navState.getDistanceToEdge(edgeID, maxRange);
        assertEquals(4507, distance);
        
        navState.edgeIndex = 108;
        distance = navState.getDistanceToEdge(edgeID, maxRange);
        assertEquals(-1, distance);
        
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        maxRange = 2000;
        distance = navState.getDistanceToEdge(edgeID, maxRange);
        assertEquals(-1, distance);
        
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        edgeID = RouteWrapper.getInstance().getRoute(routeId).segmentAt(navState.segmentIndex).getEdgeID(navState.edgeIndex);
        maxRange = 150;
        distance = navState.getDistanceToEdge(edgeID, maxRange);
        assertEquals(-1, distance);
    }
    
    @Test
    public void testGetDistanceToTurn()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        
        int distToTurn = navState.getDistanceToTurn();
        assertEquals(36830, distToTurn);
    }
    
    @Test
    public void testGetEdgeIndex()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        assertEquals(5, navState.getEdgeIndex());
    }
    
    @Test
    public void testGetPointIndex()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        assertEquals(1, navState.getPointIndex());
    }
    
    @Test
    public void testGetPosition()
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        navState.calcPositionFromIndexes(route);

        TnNavLocation position = navState.getPosition();
        // 3739863,-12202591,284
        assertEquals(3739863, position.getLatitude());
        assertEquals(-12202591, position.getLongitude());
        assertEquals(284, position.getHeading());
    }
    
    @Test
    public void testGetRange()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        
        assertEquals(10, navState.getRange());
    }
    
    @Test
    public void testGetRoute()
    {
        assertEquals(routeId, navState.getRoute());
    }
    
    @Test
    public void testGetSegmentIndex()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        
        assertEquals(3, navState.getSegmentIndex());
    }
    
    @Test
    public void testHaveCurrentData()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        
        assertTrue(navState.haveCurrentData());
    }
    
    @Test
    public void testIsAtTheEndOfRoute()
    {
        navState.segmentIndex = 10;
        navState.edgeIndex = 0;
        navState.pointIndex = 3;
        navState.range = 10;
        
        assertTrue(navState.isAtTheEndOfRoute());
    }
    
    @Test
    public void testIsAtTheStartOfRoute()
    {
        navState.segmentIndex = 0;
        navState.edgeIndex = 0;
        navState.pointIndex = 0;
        navState.range = 10;
        
        assertTrue(navState.isAtTheStartOfRoute());
    }
    
    @Test
    public void testIsWalkingOffEnd()
    {
        navState.walkRouteFromCurrentShapePoint(100);
        //From code, it would always be false.
        assertFalse(navState.isWalkingOffEnd());
    }
    
    @Test
    public void testNextLat() 
    {
        navState.setRoute(12345);
        assertEquals(0, navState.nextLat());
        
        navState.setRoute(routeId);
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        
        int lat = navState.nextLat();
        assertEquals(3739889, lat);
    }
    
    @Test
    public void testNextLon() 
    {
        navState.setRoute(12345);
        assertEquals(0, navState.nextLat());
        
        navState.setRoute(routeId);
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        
        int lon = navState.nextLon();
        assertEquals(-12202705, lon);
    }
    
    @Test
    public void testResetIndexesToHead()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        
        navState.resetIndexesToHead();
        assertEquals(0, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
        assertEquals(0, navState.range);
    }
    
    @Test
    public void testResetIndexesToTail()
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 10;
        
        navState.resetIndexesToTail(route);
        assertEquals(10, navState.segmentIndex);
        assertEquals(0, navState.edgeIndex);
        assertEquals(3, navState.pointIndex);
        assertEquals(0, navState.range);
    }
    
    @Test
    public void testSet()
    {
        //normal
        int segmentIndex = 3;
        int edgeIndex = 5;
        int pointIndex = 1;
        int range = 10;
        
        navState.set(segmentIndex, edgeIndex, pointIndex, range);
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(5, navState.edgeIndex);
        assertEquals(1, navState.pointIndex);
        assertEquals(10, navState.range);
        
        TnNavLocation position = navState.getPosition();
        assertEquals(3739863, position.getLatitude());
        assertEquals(-12202591, position.getLongitude());
        assertEquals(284, position.getHeading());
        
        //special
        segmentIndex = 3;
        edgeIndex = 12;
        pointIndex = 6;
        range = 50;
        
        navState.set(segmentIndex, edgeIndex, pointIndex, range);
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(14, navState.edgeIndex);
        assertEquals(3, navState.pointIndex);
        assertEquals(29, navState.range);
        
        position = navState.getPosition();
        assertEquals(3740433, position.getLatitude());
        assertEquals(-12205174, position.getLongitude());
        assertEquals(288, position.getHeading());
    }
    
    @Test
    public void testSetFromNavState()
    {
        //normal
        int segmentIndex = 3;
        int edgeIndex = 5;
        int pointIndex = 1;
        int range = 10;
        navState = new NavState(routeId, segmentIndex, edgeIndex, pointIndex, range);
        
        NavState other = new NavState(1234);
        other.set(navState);
        
        assertEquals(3, other.segmentIndex);
        assertEquals(5, other.edgeIndex);
        assertEquals(1, other.pointIndex);
        assertEquals(10, other.range);
        assertEquals(routeId, other.getRoute());
        
        TnNavLocation position = other.getPosition();
        assertEquals(3739863, position.getLatitude());
        assertEquals(-12202591, position.getLongitude());
        assertEquals(284, position.getHeading());
    }
    
    @Test
    public void testRange()
    {
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.setRange(300);
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(7, navState.edgeIndex);
        assertEquals(1, navState.pointIndex);
        assertEquals(71, navState.range);
        
        TnNavLocation position = navState.getPosition();
        assertEquals(3739941, position.getLatitude());
        assertEquals(-12202937, position.getLongitude());
        assertEquals(283, position.getHeading());
    }
    
    @Test
    public void testSetRoute()
    {
        assertEquals(routeId, navState.getRoute());
        navState.setRoute(1234);
        assertEquals(1234, navState.getRoute());
    }
    
    @Test
    public void testSetTimeStamp()
    {
        TnNavLocation position = navState.getPosition();
        long newTime = System.currentTimeMillis();
        navState.setTimeStamp(newTime);
        assertEquals(newTime, position.getTime());
    }
    
    @Test
    public void testWalkNextShape()
    {
        // segment 4 has 106 edges.
        // edge 5 has 4 points.
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.walkNextShape();
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(5, navState.edgeIndex);
        assertEquals(2, navState.pointIndex);
        
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 3;
        navState.walkNextShape();
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(6, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
    }
    
    @Test
    public void testWalkPrevShape()
    {
        // segment 4 has 106 edges.
        // edge 5 has 4 points.
        navState.segmentIndex = 3;
        navState.edgeIndex = 6;
        navState.pointIndex = 0;
        navState.walkPrevShape();
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(5, navState.edgeIndex);
        assertEquals(2, navState.pointIndex);
        
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.walkPrevShape();
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(5, navState.edgeIndex);
        assertEquals(0, navState.pointIndex);
    }
    
    @Test
    public void testWalkRouteFromCurrentShapePoint()
    {
        // segment 4 has 106 edges.
        // edge 5 has 4 points.
        navState.segmentIndex = 3;
        navState.edgeIndex = 5;
        navState.pointIndex = 1;
        navState.range = 500;
        navState.walkRouteFromCurrentShapePoint();
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(7, navState.edgeIndex);
        assertEquals(3, navState.pointIndex);
        assertEquals(13, navState.range);
        
        navState.walkRouteFromCurrentShapePoint(500);
        
        assertEquals(3, navState.segmentIndex);
        assertEquals(9, navState.edgeIndex);
        assertEquals(5, navState.pointIndex);
        assertEquals(31, navState.range);
    }
}

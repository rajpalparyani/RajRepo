/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavUtilTest.java
 *
 */
package com.telenav.datatypes.nav;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-7
 */
public class NavUtilTest
{
    NavUtil util;

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
        util = new NavUtil();
    }

    @Test
    public void testAdjustDeviationThresholds()
    {
        int segIndex = 3;
        int edgeIndex = 5;
        int shapePointIndex = 1;
        int range = 20;
        NavState onRoute = new NavState(routeId, segIndex, edgeIndex, shapePointIndex, range);
        Segment seg = onRoute.getCurrentSegment();
        int range_deviation = seg.getThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION);
        int node_deviation = seg.getThreshold(Route.THRESHOLD_INDEX_NODE_DEVIATION);
        int new_route = seg.getThreshold(Route.THRESHOLD_INDEX_NEW_ROUTE);
        int regain = seg.getThreshold(Route.THRESHOLD_INDEX_REGAIN);
        boolean needReAdjust = seg.isNeedReadjustThresholds();
        assertEquals(428, range_deviation);
        assertEquals(417, node_deviation);
        assertEquals(626, new_route);
        assertEquals(90, regain);
        assertEquals(false, needReAdjust);

        TnNavLocation gpsfix = new TnNavLocation("");
        gpsfix.setLatitude(3737356);
        gpsfix.setLongitude(-12201074);
        gpsfix.setHeading(345);
        util.adjustDeviationThresholds(gpsfix, false, onRoute);
        int range_deviation_new = seg.getThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION);
        int node_deviation_new = seg.getThreshold(Route.THRESHOLD_INDEX_NODE_DEVIATION);
        int new_route_new = seg.getThreshold(Route.THRESHOLD_INDEX_NEW_ROUTE);
        int regain_new = seg.getThreshold(Route.THRESHOLD_INDEX_REGAIN);
        boolean needReAdjust_new = seg.isNeedReadjustThresholds();
        assertEquals(70, range_deviation_new);
        assertEquals(68, node_deviation_new);
        assertEquals(102, new_route_new);
        assertEquals(51, regain_new);
        assertEquals(true, needReAdjust_new);
    }

    @Test
    public void testCalcRangeDevThresholdSameFix()
    {
        int roadType = DataUtil.ARTERIAL;
        TnNavLocation gpsfix = new TnNavLocation("");
        gpsfix.setLatitude(3737356);
        gpsfix.setLongitude(-12201074);
        gpsfix.setHeading(345);
        gpsfix.setTime(System.currentTimeMillis() / 10);
        gpsfix.setAccuracy(50);

        int threshold = util.calcRangeDevThreshold(roadType, gpsfix);
        assertEquals(146, threshold);
        util.calcRangeDevThreshold(roadType, gpsfix);
        assertEquals(146, threshold);
    }

    @Test
    public void testCalcRangeDevThresholdDifferentFix()
    {
        long time = System.currentTimeMillis() / 10;
        int roadType = DataUtil.ARTERIAL;
        TnNavLocation gpsfix = new TnNavLocation("");
        gpsfix.setLatitude(3737356);
        gpsfix.setLongitude(-12201074);
        gpsfix.setHeading(345);
        gpsfix.setTime(time);
        gpsfix.setAccuracy(50);

        int threshold = util.calcRangeDevThreshold(roadType, gpsfix);
        assertEquals(146, threshold);

        gpsfix.setTime(time + 10);
        threshold = util.calcRangeDevThreshold(roadType, gpsfix);
        assertEquals(146, threshold);

        gpsfix.setTime(time + 20);
        gpsfix.setAccuracy(146);
        threshold = util.calcRangeDevThreshold(roadType, gpsfix);
        assertEquals(265, threshold);
    }

    @Test
    public void testGetDevTuningRules()
    {
        int[] expected = new int[]
        { 0, 12, 4, -4, 100, 120, 150, 1, 0, 4, 3, 2 };

        assertArrayEquals(expected, util.getDevTuningRules());
    }

    @Test
    public void testGetKTables()
    {
        int[][] expected = new int[][]
        {
        { 1994, 4768, 6712, 7573, 7910, 8050, 8114, 8145 },
        { 2181, 4898, 6761, 7588, 7915, 8052, 8114, 8146 },
        { 6906, 7784, 8001, 8082, 8121, 8143, 8156, 8164 },
        { 6903, 7784, 8001, 8082, 8121, 8143, 8156, 8164 },
        { 119, 42, 13, 4, 2, 1, 0, 0 },
        { 143, 51, 16, 5, 2, 1, 0, 0 },
        { 267, 95, 29, 9, 3, 1, 1, 0 },
        { 213, 75, 23, 7, 2, 0, 0, 0 } };

        assertArrayEquals(expected, util.getKTables());
    }

    @Test
    public void testGetLastDevThreshold()
    {
        assertEquals(-1, util.getLastDevThreshold());

        long time = System.currentTimeMillis() / 10;
        int roadType = DataUtil.ARTERIAL;
        TnNavLocation gpsfix = new TnNavLocation("");
        gpsfix.setLatitude(3737356);
        gpsfix.setLongitude(-12201074);
        gpsfix.setHeading(345);
        gpsfix.setTime(time);
        gpsfix.setAccuracy(50);

        util.calcRangeDevThreshold(roadType, gpsfix);
        assertEquals(131, util.getLastDevThreshold());
    }

    @Test
    public void testGetRegainerRules()
    {
        int[] expected = new int[]
        { 30, 6, 10, 200, 100, 200, 100, 50, 500, 30, 75, 20, 90, 15, 40, 90 };
        assertArrayEquals(expected, util.getRegainerRules());
    }

    @Test
    public void testGetRoadError()
    {
        assertEquals(15, util.getRoadError(DataUtil.ARTERIAL));
        assertEquals(20, util.getRoadError(DataUtil.HIGHWAY));
        assertEquals(10, util.getRoadError(DataUtil.RAMP));
        assertEquals(10, util.getRoadError(DataUtil.HIGHWAYRAMP));
        assertEquals(10, util.getRoadError(DataUtil.SMALLSTREET));
        assertEquals(10, util.getRoadError(DataUtil.LOCAL_STREET));
        assertEquals(10, util.getRoadError(DataUtil.INTERSECTION));
        assertEquals(10, util.getRoadError(DataUtil.TRAFFIC_CIRCLE));
        assertEquals(10, util.getRoadError(DataUtil.ROUND_ABOUT));
        assertEquals(10, util.getRoadError(DataUtil.RAIL));
        assertEquals(10, util.getRoadError(DataUtil.CANAL));
        assertEquals(10, util.getRoadError(DataUtil.STATEBORDER));
        assertEquals(10, util.getRoadError(DataUtil.NATIONBORDER));
        assertEquals(10, util.getRoadError(DataUtil.WATER_LINE));
        assertEquals(10, util.getRoadError(DataUtil.FERRYBOAT_LINE));
        assertEquals(10, util.getRoadError(DataUtil.DATE_LINE));
        assertEquals(10, util.getRoadError(DataUtil.UNKNOWN_CENTER));
        assertEquals(10, util.getRoadError(DataUtil.CAMPUS_CENTER));
        assertEquals(10, util.getRoadError(DataUtil.WATER_CENTER));
        assertEquals(10, util.getRoadError(DataUtil.INTERIOR_CENTER));
        assertEquals(10, util.getRoadError(DataUtil.BUILDING_CENTER));
        assertEquals(10, util.getRoadError(DataUtil.CITY_CENTER));
        assertEquals(10, util.getRoadError(DataUtil.STATE_CENTER));
    }

//    @Test
//    public void testGetThresholdRules()
//    {
//        int[] expected = new int[]
//        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
//                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };
//        assertArrayEquals(expected, util.getThresholdRules());
//    }

    @Test
    public void testLoadNavigationRules()
    {
        //RULE_MIN_GPS_ERR       = 27; change its value from 20 to 30
        //RULE_MAX_GPS_ERR       = 20; change its value from 250 to 220
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 220, 500, 250, 200, 1, 100, 8, 12, 5,
                30, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };
        
        //RULE_MIN_SPEED         = 0;  change its value from 30 to 20
        int[] regainerRules = new int[]
        { 20, 6, 10, 200, 100, 200, 100, 50, 500, 30, 75, 20, 90, 15, 40, 90 };
        //RULE_HEADING_STRICT    = 1;  change its value from 12 to 14
        int[] devTuningRules = new int[]
        { 0, 14, 4, -4, 100, 120, 150, 1, 0, 4, 3, 2 };
        
        //K11 = 0;
        //change kTables[K11] from { 1994, 4768, 6712, 7573, 7910, 8050, 8114, 8145 } to { 2000, 4800, 6750, 7580, 7910, 8050, 8114, 8145 }
        int[][] kTables = new int[][]
        {
        { 2000, 4800, 6750, 7580, 7910, 8050, 8114, 8145 },
        { 2181, 4898, 6761, 7588, 7915, 8052, 8114, 8146 },
        { 6906, 7784, 8001, 8082, 8121, 8143, 8156, 8164 },
        { 6903, 7784, 8001, 8082, 8121, 8143, 8156, 8164 },
        { 119, 42, 13, 4, 2, 1, 0, 0 },
        { 143, 51, 16, 5, 2, 1, 0, 0 },
        { 267, 95, 29, 9, 3, 1, 1, 0 },
        { 213, 75, 23, 7, 2, 0, 0, 0 } };
        
        NavigationRules rules = new NavigationRules();
        rules.thresholdRules = thresholdRules;
        rules.regainerRules = regainerRules;
        rules.devTuningRules = devTuningRules;
        rules.kTables = kTables;
        
        util.loadNavigationRules(rules);
        
        assertArrayEquals(thresholdRules, util.getThresholdRules());
        assertArrayEquals(regainerRules, util.getRegainerRules());
        assertArrayEquals(devTuningRules, util.getDevTuningRules());
        assertArrayEquals(kTables, util.getKTables());
    }
    
    @Test
    public void testResetRangeDevThreshold()
    {
        long time = System.currentTimeMillis() / 10;
        int roadType = DataUtil.ARTERIAL;
        TnNavLocation gpsfix = new TnNavLocation("");
        gpsfix.setLatitude(3737356);
        gpsfix.setLongitude(-12201074);
        gpsfix.setHeading(345);
        gpsfix.setTime(time);
        gpsfix.setAccuracy(50);

        util.calcRangeDevThreshold(roadType, gpsfix);
        assertEquals(131, util.lastDevThreshold);
        assertEquals(time, util.lastGPSErrorTime);
        
        util.resetRangeDevThreshold();
        assertEquals(-1, util.lastDevThreshold);
        assertEquals(-1, util.lastGPSErrorTime);
    }
    
    @Test
    public void testCalcEdgeHeading()
    {
        int segIndex = 10;
        int edgeIndex = 0;
        int pointIndex = 0;
        RouteEdge edge = RouteWrapper.getInstance().getRoute(routeId).segmentAt(segIndex).getEdge(edgeIndex);
        int heading = util.calcEdgeHeading(edge, pointIndex);
        assertEquals(110, heading);
    }
}

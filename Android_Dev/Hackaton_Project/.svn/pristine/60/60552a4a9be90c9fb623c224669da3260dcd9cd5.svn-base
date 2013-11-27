/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SegmentTest.java
 *
 */
package com.telenav.datatypes.route;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.nav.INavEngineConstants;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-8
 */
public class SegmentTest
{
    Segment seg;

    Route route;

    @Before
    public void setup()
    {
        RouteUtil.prepareRouteData();
        route = RouteWrapper.getInstance().getCurrentRoute();
        seg = route.segmentAt(4);
    }

    @Test
    public void testCalcDeviationThresholds()
    {
        seg.length = 1000;
        seg.roadType = DataUtil.RAMP;
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };
        seg.calcDeviationThresholds(false, thresholdRules);
        int[] threshold = new int[]
        { 250, 244, 8, 366, 90 };
        assertArrayEquals(threshold, seg.thresholds);
        seg.calcDeviationThresholds(true, thresholdRules);
        threshold = new int[]
        { 250, 244, 8, 366, 90 };
        assertArrayEquals(threshold, seg.thresholds);
    }

    @Test
    public void testCalculateThresholds()
    {
        seg.roadType = DataUtil.HIGHWAY;
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };
        seg.calculateThresholds(thresholdRules);
        int[] minAudioDistance = new int[]
        { 960, 360, 1000 };
        int[] minAudioTime = new int[]
        { 8, 3, 9 };

        assertArrayEquals(minAudioDistance, seg.minAudioDistance);
        assertArrayEquals(minAudioTime, seg.minAudioTime);
    }

    @Test
    public void testCalculateUILagDist()
    {
        Segment curSeg = new Segment();
        Segment prevSeg = new Segment();
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };
        // 150, 40

        int result = seg.calculateUILagDist(curSeg, prevSeg, null);
        assertEquals(0, result);

        prevSeg.setRoadType(DataUtil.RAMP);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(0, result);

        prevSeg.setRoadType(DataUtil.HIGHWAY);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(0, result);

        prevSeg.setRoadType(DataUtil.BUILDING);
        curSeg.setRoadType(DataUtil.RAMP);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(0, result);

        prevSeg.setRoadType(DataUtil.BUILDING);
        curSeg.setRoadType(DataUtil.HIGHWAY);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(0, result);

        curSeg.setRoadType(DataUtil.INTERSECTION);
        prevSeg.setTurnType(Route.L2L_U_TURN);
        int currSegLength = 100;
        curSeg.setLength(currSegLength);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(currSegLength + 1, result);

        curSeg.setRoadType(DataUtil.PARK);
        currSegLength = thresholdRules[Route.RULE_LAG_MIN_SEG_LEN] + 1;
        curSeg.setLength(currSegLength);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(thresholdRules[Route.RULE_LAG_DEFAULT], result);

        currSegLength = thresholdRules[Route.RULE_LAG_DEFAULT] + 1;
        curSeg.setLength(currSegLength);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(thresholdRules[Route.RULE_LAG_MIN], result);

        currSegLength = thresholdRules[Route.RULE_LAG_DEFAULT] - 1;
        curSeg.setLength(currSegLength);
        result = seg.calculateUILagDist(curSeg, prevSeg, thresholdRules);
        assertEquals(0, result);
    }

    @Test
    public void testDecimatedPointsGlobal()
    {
        int zoom = 1;
        int startEdge = 0;
        int endEdge = 3;
        int startPoint = 0;
        int endPoint = 2;
        int deltaLat = 1;
        int deltaLon = 1;

        int[][] expected = new int[][]
        {
        { 12989384, 5370064 },
        { 12989332, 5370021 },
        { 12989226, 5369908 },
        { 12989197, 5369871 },
        { 12989172, 5369840 },
        { 12989078, 5369727 },
        { 12989028, 5369672 } };
        int[][] decimatedPointsGlobal = seg.getDecimatedPointsGlobal(zoom, startEdge, endEdge, startPoint, endPoint, deltaLat, deltaLon);
        assertArrayEquals(expected, decimatedPointsGlobal);

        expected = new int[][]
        {
        { 12989384, 5370064 },
        { 12989332, 5370021 },
        { 12989226, 5369908 },
        { 12989197, 5369871 },
        { 12989172, 5369840 },
        { 12989078, 5369727 },
        { 12989028, 5369672 } };
        decimatedPointsGlobal = seg.getDecimatedPointsGlobal(zoom, startEdge, endEdge, startPoint, endPoint, 0, 0);
        assertArrayEquals(expected, decimatedPointsGlobal);

        startEdge = 1;
        expected = new int[][]
        {
        { 12989226, 5369908 },
        { 12989197, 5369871 },
        { 12989172, 5369840 },
        { 12989078, 5369727 },
        { 12989028, 5369672 } };
        decimatedPointsGlobal = seg.getDecimatedPointsGlobal(zoom, startEdge, endEdge, startPoint, endPoint, 0, 0);
        assertArrayEquals(expected, decimatedPointsGlobal);

        assertNotNull(seg.decimatedPointsGlobal);
        seg.clearDecimatedPoints();
        assertNull(seg.decimatedPointsGlobal);
    }

    @Test
    public void testEdgesSize()
    {
        assertEquals(4, seg.edgesSize());
        seg.edgesID = null;
        assertEquals(0, seg.edgesSize());
    }

    @Test
    public void testActionAudio()
    {
        byte[] fakeData = new byte[]
        { 1, 2, 3, 4, 5, 6, 7 };

        AudioData audioData = AudioDataFactory.getInstance().createAudioData(fakeData);
        AudioDataNode node = AudioDataFactory.getInstance().createAudioDataNode(audioData);

        seg.setActionAudio(node);
        assertEquals(node, seg.getActionAudio());
    }

    @Test
    public void testGetAudioAssistance()
    {
        SegmentAudioAssistance assistance1 = new SegmentAudioAssistance();
        assistance1.dist2Turn4ConfusingIntersection = 4;
        assistance1.isConfusingIntersectionInfoAvailable = true;
        assistance1.shouldPlayActionAudio = true;
        assistance1.shouldPlayActionAudio4NextSeg = true;
        assistance1.shouldPlayInfoAudio = true;
        assistance1.shouldPlayInfoAudio4NextSeg = true;
        assistance1.shouldPlayPrepareAudio = true;
        assistance1.shouldPlayPrepareAudio4NextSeg = true;
        seg.setAudioAssistance(assistance1);

        SegmentAudioAssistance assistance = seg.getAudioAssistance();
        assertTrue(assistance.shouldPlayActionAudio);
        assertTrue(assistance.isConfusingIntersectionInfoAvailable);
        assertTrue(assistance.shouldPlayActionAudio4NextSeg);
        assertTrue(assistance.shouldPlayInfoAudio);
        assertTrue(assistance.shouldPlayInfoAudio4NextSeg);
        assertTrue(assistance.shouldPlayPrepareAudio);
        assertTrue(assistance.shouldPlayPrepareAudio4NextSeg);
        assertEquals(4, assistance.dist2Turn4ConfusingIntersection);

        assertTrue(assistance.shouldPlayAudio(Route.AUDIO_TYPE_ACTION));
        assertTrue(assistance.shouldPlayAudio(Route.AUDIO_TYPE_PREP));
        assertTrue(assistance.shouldPlayAudio(Route.AUDIO_TYPE_INFO));
        assertFalse(assistance.shouldPlayAudio(Route.AUDIO_TYPE_ADI));

        assertTrue(assistance.shouldPlayAudio4NextSegment(Route.AUDIO_TYPE_ACTION));
        assertTrue(assistance.shouldPlayAudio4NextSegment(Route.AUDIO_TYPE_PREP));
        assertTrue(assistance.shouldPlayAudio4NextSegment(Route.AUDIO_TYPE_INFO));
        assertFalse(assistance.shouldPlayAudio4NextSegment(Route.AUDIO_TYPE_ADI));

        assertTrue(assistance.isConfusingIntersectionInfoAvailable());
        assertEquals(4, assistance.getDist2Turn4ConfusingIntersection());
    }

    @Test
    public void testGetBoundingBox()
    {
        int[] expected = new int[]
        { 3760600, -12238967, 3760902, -12238547 };
        int[] boundingBox = seg.getBoundingBox();
        assertArrayEquals(expected, boundingBox);

        expected = new int[]
        { 3760600, -12238967, 3760905, -12230000 };
        seg.setBoundingBox(expected);
        boundingBox = seg.getBoundingBox();
        assertArrayEquals(expected, boundingBox);
    }

    @Test
    public void testGetCalculatedLength()
    {
        assertEquals(454, seg.getCalculatedLength());
    }

    @Test
    public void testGetCosLat()
    {
        assertEquals(6542, seg.getCosLat());
        seg.setCosLat(5000);
        assertEquals(5000, seg.getCosLat());
    }

    @Test
    public void testGetDistanceToDest()
    {
        assertEquals(2149, seg.getDistanceToDest());
        seg.setDistanceToDest(1000);
        assertEquals(1000, seg.getDistanceToDest());
    }

    @Test
    public void testGetEdge()
    {
        assertNull(seg.getEdge(10));
        RouteEdge[] edge = seg.edges;
        assertEquals(edge[2], seg.getEdge(2));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEdgeID()
    {
        assertEquals(141, seg.getEdgeID(0));
        assertEquals(142, seg.getEdgeID(1));
        assertEquals(143, seg.getEdgeID(2));
        assertEquals(144, seg.getEdgeID(3));
        assertEquals(145, seg.getEdgeID(4));
    }

    @Test
    public void testGetEdgesInClip()
    {
        /*
         * [3760600, -12238714, 3760734, -12238547] [3760734, -12238754, 3760759, -12238714] [3760759, -12238787,
         * 3760780, -12238754] [3760780, -12238967, 3760902, -12238787]
         */
        Vector edgesToAddTo = new Vector();
        int clipLatMin = 3760500;
        int clipLonMin = -12238548;
        int clipLatMax = 3760599;
        int clipLonMax = -12238340;

        seg.getEdgesInClip(clipLatMin, clipLonMin, clipLatMax, clipLonMax, edgesToAddTo);
        assertTrue(edgesToAddTo.isEmpty());

        clipLatMin = 3760600;
        clipLonMin = -12238713;
        clipLatMax = 3760733;
        clipLonMax = -12238547;
        edgesToAddTo.removeAllElements();
        seg.getEdgesInClip(clipLatMin, clipLonMin, clipLatMax, clipLonMax, edgesToAddTo);
        assertEquals(1, edgesToAddTo.size());
        assertEquals(seg.getEdge(0), edgesToAddTo.elementAt(0));

        clipLatMin = 3760735;
        clipLonMin = -12238753;
        clipLatMax = 3760760;
        clipLonMax = -12238715;
        edgesToAddTo.removeAllElements();
        seg.getEdgesInClip(clipLatMin, clipLonMin, clipLatMax, clipLonMax, edgesToAddTo);
        assertEquals(1, edgesToAddTo.size());
        assertEquals(seg.getEdge(1), edgesToAddTo.elementAt(0));
    }

    @Test
    public void testExitName()
    {
        String exitName = "exit";
        seg.setExitName(exitName);
        assertEquals(exitName, seg.getExitName());
    }

    @Test
    public void testExitNumber()
    {
        byte exitNumber = 11;
        seg.setExitNumber(exitNumber);
        assertEquals(exitNumber, seg.getExitNumber());
    }

    @Test
    public void testGetFirstEdgeId()
    {
        assertEquals(new Integer(141), seg.getFirstEdgeId());
    }

    @Test
    public void testInfoAudio()
    {
        byte[] fakeData = new byte[]
        { 1, 2, 3, 4, 5, 6, 7 };

        AudioData audioData = AudioDataFactory.getInstance().createAudioData(fakeData);
        AudioDataNode node = AudioDataFactory.getInstance().createAudioDataNode(audioData);

        seg.setInfoAudio(node);
        assertEquals(node, seg.getInfoAudio());
    }

    @Test
    public void testInstructionLagDist()
    {
        assertEquals(0, seg.getInstructionLagDist());
        seg.setInstructionLagDist(50);
        assertEquals(50, seg.getInstructionLagDist());
    }

    @Test
    public void testLaneInfos()
    {
        int[] laneInfos = new int[]
        { 0, 1 };
        assertArrayEquals(laneInfos, seg.getLaneInfos());
        laneInfos = new int[]
        { 0, 1, 0, 1 };
        seg.setLaneInfos(laneInfos);
        assertArrayEquals(laneInfos, seg.getLaneInfos());
    }

    @Test
    public void testLaneTypes()
    {
        int[] laneTypes = new int[]
        { 0, 0 };
        assertArrayEquals(laneTypes, seg.getLaneTypes());
        laneTypes = new int[]
        { 0, 1, 4, 6, 7 };
        seg.setLaneTypes(laneTypes);
        assertArrayEquals(laneTypes, seg.getLaneTypes());
    }

    @Test
    public void testLength()
    {
        assertEquals(450, seg.getLength());
        seg.setLength(500);
        assertEquals(500, seg.getLength());
    }

    @Test
    public void testGetMinAudioDistance()
    {
        seg.roadType = DataUtil.HIGHWAY;
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };
        seg.calculateThresholds(thresholdRules);

        assertEquals(960, seg.getMinAudioDistance(Route.AUDIO_TYPE_PREP));
        assertEquals(360, seg.getMinAudioDistance(Route.AUDIO_TYPE_ACTION));
        assertEquals(1000, seg.getMinAudioDistance(Route.AUDIO_TYPE_INFO));
    }

    @Test
    public void testGetMinAudioTime()
    {
        seg.roadType = DataUtil.HIGHWAY;
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };
        seg.calculateThresholds(thresholdRules);

        assertEquals(8, seg.getMinAudioTime(Route.AUDIO_TYPE_PREP));
        assertEquals(3, seg.getMinAudioTime(Route.AUDIO_TYPE_ACTION));
        assertEquals(9, seg.getMinAudioTime(Route.AUDIO_TYPE_INFO));
    }

    @Test
    public void testPrepAudio()
    {
        byte[] fakeData = new byte[]
        { 1, 2, 3, 4, 5, 6, 7 };

        AudioData audioData = AudioDataFactory.getInstance().createAudioData(fakeData);
        AudioDataNode node = AudioDataFactory.getInstance().createAudioDataNode(audioData);

        seg.setPrepAudio(node);
        assertEquals(node, seg.getPrepAudio());
    }

    @Test
    public void testRoadType()
    {
        seg.setRoadType(DataUtil.HIGHWAY);
        assertEquals(DataUtil.HIGHWAY, seg.getRoadType());
    }

    @Test
    public void testSegmentAudioNode()
    {
        byte[] fakeData = new byte[]
        { 1, 2, 3, 4, 5, 6, 7 };

        AudioData audioData = AudioDataFactory.getInstance().createAudioData(fakeData);
        AudioDataNode node = AudioDataFactory.getInstance().createAudioDataNode(audioData);

        seg.setSegmentAudioNode(node);
        assertEquals(node, seg.getSegmentAudioNode());
    }

    @Test
    public void testSegmentType()
    {
        seg.setSegmentType(INavEngineConstants.SEGMENT_TIGHT_TURN);
        assertEquals(INavEngineConstants.SEGMENT_TIGHT_TURN, seg.getSegmentType());
    }

    @Test
    public void testSpeedLimit()
    {
        assertEquals(96, seg.getSpeedLimit());
        seg.setSpeedLimit(12);
        assertEquals(12, seg.getSpeedLimit());
    }

    @Test
    public void testStreetAlias()
    {
        assertNull(seg.getStreetAlias());
        seg.setStreetAlias("test");
        assertEquals("test", seg.getStreetAlias());
    }

    @Test
    public void testStreetAliasId()
    {
        assertEquals(-1, seg.getStreetAliasId());
        seg.setStreetAliasId(99);
        assertEquals(99, seg.getStreetAliasId());
    }

    @Test
    public void testStreetName()
    {
        assertEquals("San Francisco Intl Airport", seg.getStreetName());
        seg.setStreetName("kifer rd");
        assertEquals("kifer rd", seg.getStreetName());
    }

    @Test
    public void testStreetNameId()
    {
        assertEquals(5, seg.getStreetNameId());
        seg.setStreetNameId(9);
        assertEquals(9, seg.getStreetNameId());
    }

    @Test
    public void testThreshold()
    {
        // [250, 244, 8, 366, 90]
        assertEquals(250, seg.getThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION));
        assertEquals(244, seg.getThreshold(Route.THRESHOLD_INDEX_NODE_DEVIATION));
        assertEquals(8, seg.getThreshold(Route.THRESHOLD_INDEX_HEADING_DEVIATION));
        assertEquals(366, seg.getThreshold(Route.THRESHOLD_INDEX_NEW_ROUTE));
        assertEquals(90, seg.getThreshold(Route.THRESHOLD_INDEX_REGAIN));

        seg.setThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION, 1);
        seg.setThreshold(Route.THRESHOLD_INDEX_NODE_DEVIATION, 2);
        seg.setThreshold(Route.THRESHOLD_INDEX_HEADING_DEVIATION, 3);
        seg.setThreshold(Route.THRESHOLD_INDEX_NEW_ROUTE, 4);
        seg.setThreshold(Route.THRESHOLD_INDEX_REGAIN, 5);
        assertEquals(1, seg.getThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION));
        assertEquals(2, seg.getThreshold(Route.THRESHOLD_INDEX_NODE_DEVIATION));
        assertEquals(3, seg.getThreshold(Route.THRESHOLD_INDEX_HEADING_DEVIATION));
        assertEquals(4, seg.getThreshold(Route.THRESHOLD_INDEX_NEW_ROUTE));
        assertEquals(5, seg.getThreshold(Route.THRESHOLD_INDEX_REGAIN));
    }

    @Test
    public void testTurnType()
    {
        assertEquals(Route.TURN_TYPE_STAY_RIGHT, seg.getTurnType());
        seg.setTurnType(Route.TURN_TYPE_ROUNDABOUT_ENTER);
        assertEquals(Route.TURN_TYPE_ROUNDABOUT_ENTER, seg.getTurnType());
    }

    @Test
    public void testIsEdgePresent()
    {
        assertTrue(seg.isEdgePresent(0));
        assertTrue(seg.isEdgePresent(1));
        assertTrue(seg.isEdgePresent(2));
        assertTrue(seg.isEdgePresent(3));
        assertFalse(seg.isEdgePresent(4));
    }

    @Test
    public void testIsEdgeResolved()
    {
        assertTrue(seg.isEdgeResolved());
        seg.setEdgeResolved(false);
        assertFalse(seg.isEdgeResolved());
    }

    @Test
    public void testIsNeedReadjustThresholds()
    {
        assertFalse(seg.isNeedReadjustThresholds());
        seg.setNeedReadjustThresholds(true);
        assertTrue(seg.isNeedReadjustThresholds());
    }

    @Test
    public void testIsPlayed()
    {
        assertFalse(seg.isPlayed(Route.AUDIO_TYPE_PREP));
        assertFalse(seg.isPlayed(Route.AUDIO_TYPE_ACTION));
        assertFalse(seg.isPlayed(Route.AUDIO_TYPE_INFO));

        seg.setPlayed(Route.AUDIO_TYPE_PREP, true);
        seg.setPlayed(Route.AUDIO_TYPE_ACTION, true);
        seg.setPlayed(Route.AUDIO_TYPE_INFO, true);

        assertTrue(seg.isPlayed(Route.AUDIO_TYPE_PREP));
        assertTrue(seg.isPlayed(Route.AUDIO_TYPE_ACTION));
        assertTrue(seg.isPlayed(Route.AUDIO_TYPE_INFO));
    }

    @Test
    public void testLatLon()
    {
        // { 3760600, -12238967, 3760902, -12238547 }
        int maxLat = 3760902;
        int maxLon = -12238547;
        int minLat = 3760600;
        int minLon = -12238967;

        assertEquals(maxLat, seg.maxLat());
        assertEquals(maxLon, seg.maxLon());
        assertEquals(minLat, seg.minLat());
        assertEquals(minLon, seg.minLon());
    }

    @Test
    public void testResolveEdgeReferencesDontDoAnything()
    {
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };

        seg.setCosLat(-1);
        seg.calculatedLength = 0;
        seg.setLength(0);
        seg.minAudioDistance = new int[]{-1, -1, -1};
        seg.minAudioTime = new int[]{-1, -1, -1};
        seg.thresholds = new int[]{-1, -1, -1, -1, -1};
        
        // return immediately
        seg.setEdgeResolved(true);
        seg.resolveEdgeReferences(false, thresholdRules, null);
        assertEquals(-1, seg.getCosLat());
        assertEquals(0, seg.getCalculatedLength());
        assertEquals(0, seg.getLength());
        assertArrayEquals(new int[]{-1, -1, -1}, seg.minAudioDistance);
        assertArrayEquals(new int[]{-1, -1, -1}, seg.minAudioTime);
        assertArrayEquals(new int[]{-1, -1, -1, -1, -1}, seg.thresholds);
    }
    
    @Test
    public void testResolveEdgeReferencesNormal()
    {
        int[] thresholdRules = new int[]
        { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5,
                20, 200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, 5, 3, 3, 0, 0 };

        seg.setCosLat(-1);
        seg.calculatedLength = 0;
        seg.setLength(0);
        seg.minAudioDistance = new int[]{-1, -1, -1};
        seg.minAudioTime = new int[]{-1, -1, -1};
        seg.thresholds = new int[]{-1, -1, -1, -1, -1};
        
        // return immediately
        seg.setEdgeResolved(false);
        seg.resolveEdgeReferences(false, thresholdRules, null);
        assertEquals(6542, seg.getCosLat());
        assertEquals(454, seg.getCalculatedLength());
        assertEquals(454, seg.getLength());
        assertArrayEquals(new int[]{960, 360, 1000}, seg.minAudioDistance);
        assertArrayEquals(new int[]{6, 3, 7}, seg.minAudioTime);
        assertArrayEquals(new int[]{250, 244, 8, 366, 90}, seg.thresholds);
    }
}

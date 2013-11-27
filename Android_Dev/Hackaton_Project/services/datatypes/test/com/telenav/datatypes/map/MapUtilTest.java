/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapUtilTest.java
 *
 */
package com.telenav.datatypes.map;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

import com.telenav.datatypes.DataUtil;
/**
 *@author yning
 *@date 2011-7-5
 */
public class MapUtilTest
{
    @Test
    public void testBuildBox()
    {
        int[] points = new int[]{12,34,25,100,56,21};
        int numPoints = 3;
        int[] expected = new int[]{12,21,56,100};
        assertArrayEquals(expected, MapUtil.buildBox(points, numPoints));
    }
    
    @Test
    public void testCalcGlobalCenterX()
    {
        //xIndex = 3793151;
        //yIndex = 3793151;
        //tempZoom = 3;
        long tileID = 908063447572735l;
        int zoom = 3;
        
        int expected = 1942093568;
        
        int actual = MapUtil.calcGlobalCenterX(tileID, zoom);
        assertEquals(expected, actual);
        
        zoom = 2;
        expected = 971046784;
        actual = MapUtil.calcGlobalCenterX(tileID, zoom);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCalcGlobalCenterY()
    {
        //xIndex = 3793151;
        //yIndex = 3793151;
        //tempZoom = 3;
        long tileID = 908063447572735l;
        int zoom = 3;
        
        int expected = 1942093568;
        
        int actual = MapUtil.calcGlobalCenterY(tileID, zoom);
        assertEquals(expected, actual);
        
        zoom = 2;
        expected = 971046784;
        actual = MapUtil.calcGlobalCenterY(tileID, zoom);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCalcGlobalFromIndex()
    {
        int index = 3793151;
        int zoom = 3;
        int expected = 1942093312;
        int actual = MapUtil.calcGlobalFromIndex(index, zoom);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCalcGlobalIndex()
    {
        int global = 1942093312;
        int zoom = 3;
        int expected = 3793151;
        int actual = MapUtil.calcGlobalIndex(global, zoom);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCalcGlobalXIndex()
    {
        long tileID = 908063447572735l;
        int expected = 3793151;
        int actual = MapUtil.calcGlobalXIndex(tileID);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCalcGlobalYIndex()
    {
        long tileID = 908063447572735l;
        int expected = 3793151;
        int actual = MapUtil.calcGlobalYIndex(tileID);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCalcLineIntersectingBoundingSperate()
    {
        int x1 = 0;
        int y1 = 0;
        
        int x2 = 20;
        int y2 = 20;
        
        int x3 = 0;
        int y3 = 40;
        
        int x4 = 20;
        int y4 = 60;
        
        int[] intersectedPos = new int[2];
        assertFalse(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
    }
    
    @Test
    public void testCalcLineIntersectingLinesConnected()
    {
        int x1 = 0;
        int y1 = 0;
        
        int x2 = 20;
        int y2 = 20;
        
        int x3 = 0;
        int y3 = 40;
        
        int x4 = 0;
        int y4 = 0;
        
        
        int[] intersectedPos = new int[2];
        assertTrue(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
        assertArrayEquals(new int[]{0, 0}, intersectedPos);
        
        x4 = 20;
        y4 = 20;
        
        assertTrue(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
        assertArrayEquals(new int[]{20, 20}, intersectedPos);
    }
    
    @Test
    public void testCalcLineIntersectingNotLines()
    {
        int x1 = 0;
        int y1 = 0;
        
        int x2 = 0;
        int y2 = 0;
        
        int x3 = 0;
        int y3 = 40;
        
        int x4 = 0;
        int y4 = 40;
        
        int[] intersectedPos = new int[2];
        assertFalse(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
    }
    
    @Test
    public void testCalcLineIntersectingSameX()
    {
        int x1 = 0;
        int y1 = 0;
        
        int x2 = 0;
        int y2 = 20;
        
        int x3 = -1;
        int y3 = 0;
        
        int x4 = 1;
        int y4 = 20;
        
        int[] intersectedPos = new int[2];
        assertTrue(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
        assertArrayEquals(new int[]{0, 10}, intersectedPos);
        
        x3 = 10;
        y3 = 5;
        x4 = 10;
        y4 = 10;
        
        x1 = 8;
        y1 = 5;
        x2 = 12;
        y2 = 10;
        
        intersectedPos = new int[2];
        assertTrue(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
        assertArrayEquals(new int[]{10, 7}, intersectedPos);
    }
    
    @Test
    public void testCalcLineIntersectingSameSlope()
    {
        int x1 = 0;
        int y1 = 0;
        
        int x2 = 20;
        int y2 = 20;
        
        int x3 = 1;
        int y3 = 0;
        
        int x4 = 21;
        int y4 = 20;
        
        int[] intersectedPos = new int[2];
        assertFalse(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
    }
    
    @Test
    public void testCalcLineIntersectingTrue()
    {
        //y=2x+1;
        //y=x+2;
        //intersect at (1, 3)
        int x1 = 0;
        int y1 = 1;
        
        int x2 = 2;
        int y2 = 5;
        
        int x3 = 0;
        int y3 = 2;
        
        int x4 = 2;
        int y4 = 4;
        
        int[] expected = new int[]{1, 3}; 
        int[] intersectedPos = new int[2];
        assertTrue(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
        assertArrayEquals(expected, intersectedPos);
    }
    
    @Test
    public void testCalcLineIntersectingFalse()
    {
        //y=2x+1;
        //y=x+2;
        //intersect at (1, 3)
        
        int x1 = 0;
        int y1 = 1;
        
        int x2 = -1;
        int y2 = -1;
        
        int x3 = 0;
        int y3 = 2;
        
        int x4 = -1;
        int y4 = 1;
        
        int[] intersectedPos = new int[2];
        assertFalse(MapUtil.calcLineIntersecting(x1, y1, x2, y2, x3, y3, x4, y4, intersectedPos));
    }
    
    @Test
    public void testCalcShiftPoints()
    {
        //start point of along route
        int lat1 = 3737890;
        int lon1 = -12201074;
        
        //'J' point of MViewer
        int lat2 = 3737392;
        int lon2 = -12199934;
        
        int[] points = new int[4];
        int count = 0;
        int shiftWidth = 1;
        
        //expect [59806254, -195217178, 59798286, -195198938]
        int[] expected = new int[]{59806254, -195217178, 59798286, -195198938};
        MapUtil.calcShiftPoints(lat1, lon1, lat2, lon2, points, count, shiftWidth);
        assertArrayEquals(expected, points);
    }
    
    @Test
    public void testComposeID()
    {
        int zoom = 3;
        //xIndex = 3793151;
        //yIndex = 3793151;
        //tempZoom = 3;
        int latIndex = 3793151;
        int lonIndex = 3793151;
        long expected = 908063447572735l;
        long actual = MapUtil.composeID(latIndex, lonIndex, zoom);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeTileArray()
    {
        int lat1 = 3737890;
        int lon1 = -12201074;
        
        int lat2 = 3737890;
        int lon2 = -12199934;
        
        int lat3 = 3737392;
        int lon3 = -12199934;
        
        int lat4 = 3737392;
        int lon4 = -12201074;
        
        int[][] points = new int[][]{{lat1, lon1}, {lat2, lon2}, {lat3, lon3}, {lat4, lon4}};
        int zoom = 3;
        boolean isYaxisPointUp = true;
        
        TileArray tileArray = MapUtil.computeTileArray(points, zoom, isYaxisPointUp);
        //[-399817776260, -399800999044, -399784221828, -399767444612]
        long[] expectedBottomIds = new long[]{-399817776260l, -399800999044l, -399784221828l, -399767444612l};
        long[] bottomIds = tileArray.getBottomIds();
        //[-399817776261, -399800999045, -399784221829, -399767444613]
        long[] expectedTopIds = new long[]{-399817776261l, -399800999045l, -399784221829l, -399767444613l};
        long[] topIds = tileArray.getTopIds();
        //[-399817776261, -399817776260]
        long[] expectedLeftIds = new long[]{-399817776261l, -399817776260l};
        long[] leftIds = tileArray.getLeftIds();
        //[-399767444613, -399767444612]
        long[] expectedRightIds = new long[]{-399767444613l, -399767444612l};
        long[] rightIds = tileArray.getRightIds();
        //-399784221828
        long expectedCenterId = -399784221828l;
        long centerId = tileArray.getCenterId();
        
        int tileZoom = tileArray.getZoom();
        
        assertArrayEquals(expectedBottomIds, bottomIds);
        assertArrayEquals(expectedTopIds, topIds);
        assertArrayEquals(expectedLeftIds, leftIds);
        assertArrayEquals(expectedRightIds, rightIds);
        assertEquals(expectedCenterId, centerId);
        assertEquals(zoom, tileZoom);
    }
    
    @Test
    public void testConvertRoadType()
    {
        assertEquals(DataUtil.LOCAL_STREET, MapUtil.convertRoadType(0));
        assertEquals(DataUtil.BUILDING, MapUtil.convertRoadType(17));
    }
    
    @Test
    public void testCreatePolygonForEdge()
    {
        int lat1 = 3737890;
        int lon1 = -12201074;
        
        int lat2 = 3737890;
        int lon2 = -12199934;
        
        int lat3 = 3737392;
        int lon3 = -12199934;
        
        int lat4 = 3737392;
        int lon4 = -12201074;
        
        int[] points = new int[]{lat1, lon1, lat2, lon2, lat3, lon3, lat4, lon4};
        int numPoints = 4;
        int d = 2;
        boolean needRoundEnd = true;
        int count = MapUtil.createPolygonForEdge(points, numPoints, d, needRoundEnd);
        assertEquals(20, count);
        int[] expectedPolygonPoints = new int[]{59806272, -195217184, 59806272, -195198944, 59806240, -195198912, 59798272, -195198912, 59798240, -195198944, 59798240, -195217184, 59798244, -195217200, 59798256, -195217212, 59798272, -195217216, 59798287, -195217212, 59798299, -195217200, 59798304, -195217184, 59798304, -195198976, 59806208, -195198976, 59806208, -195217184, 59806212, -195217200, 59806224, -195217212, 59806240, -195217216, 59806255, -195217212, 59806267, -195217200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedPolygonPoints, MapUtil.getPolygonPointsForEdge());
    }
    
    @Test
    public void testCreatePolygonForEdgeVectorEdge()
    {
        VectorMapEdge e = new VectorMapEdge();
        
        int lat1 = 3737890;
        int lon1 = -12201074;
        
        int lat2 = 3737890;
        int lon2 = -12199934;
        
        int lat3 = 3737392;
        int lon3 = -12199934;
        
        int lat4 = 3737392;
        int lon4 = -12201074;
        
        int[] points = new int[]{lat1, lon1, lat2, lon2, lat3, lon3, lat4, lon4};
        
        e.setShapePoints(points);
        
        int d = 2;
        boolean needRoundEnd = true;
        
        int count = MapUtil.createPolygonForEdge(e, d, needRoundEnd);
        
        assertEquals(20, count);
        int[] expectedPolygonPoints = new int[]{59806272, -195217184, 59806272, -195198944, 59806240, -195198912, 59798272, -195198912, 59798240, -195198944, 59798240, -195217184, 59798244, -195217200, 59798256, -195217212, 59798272, -195217216, 59798287, -195217212, 59798299, -195217200, 59798304, -195217184, 59798304, -195198976, 59806208, -195198976, 59806208, -195217184, 59806212, -195217200, 59806224, -195217212, 59806240, -195217216, 59806255, -195217212, 59806267, -195217200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedPolygonPoints, MapUtil.getPolygonPointsForEdge());
    }
    
    @Test
    public void testDecimateGlobalPoints()
    {
        int lat1 = 3737890;
        int lon1 = -12201074;
        
        int lat2 = 3737890;
        int lon2 = -12199934;
        
        int lat3 = 3737392;
        int lon3 = -12199934;
        
        int lat4 = 3737392;
        int lon4 = -12201074;
        
        int[] points = new int[]{lat1, lon1, lat2, lon2, lat3, lon3, lat4, lon4};
        int numPoints = 4;
        int zoom = 3;
        
        int distThreshold = MapUtil.VECTOR_MAP_DECIMATION_THRESHOLD[zoom];
        int[] p1 = new int[3];
        int[] p2 = new int[2];
        int count = MapUtil.decimateGlobalPoints(points, numPoints, distThreshold, p1, p2);
        assertEquals(4, count);
        //[3737890, -12201074, 3737890, -12199934, 3737392, -12199934, 3737392, -12201074]
        int[] expectedPoints = new int[]{3737890, -12201074, 3737890, -12199934, 3737392, -12199934, 3737392, -12201074};
        assertArrayEquals(expectedPoints, points);
    }
    
    @Test
    public void testDecimateMapTile()
    {
        VectorMapEdge e = new VectorMapEdge();
        
        int lat1 = 3737890;
        int lon1 = -12201074;
        
        int lat2 = 3737890;
        int lon2 = -12199934;
        
        int lat3 = 3737392;
        int lon3 = -12199934;
        
        int lat4 = 3737392;
        int lon4 = -12201074;
        
        int[] points = new int[]{lat1, lon1, lat2, lon2, lat3, lon3, lat4, lon4};
        
        e.setShapePoints(points);
        
        VectorMapEdge[] mapEdges = new VectorMapEdge[]{e};
        MapTile mapTile = new MapTile();
        mapTile.setMapEdges(mapEdges);
        MapUtil.decimateMapTile(mapTile);
        int count = e.getShapePointsSize();
        assertEquals(4, count);
        //[3737890, -12201074, 3737890, -12199934, 3737392, -12199934, 3737392, -12201074]
        int[] expectedPoints = new int[]{3737890, -12201074, 3737890, -12199934, 3737392, -12199934, 3737392, -12201074};
        assertArrayEquals(expectedPoints, points);
    }
    
    @Test
    public void testDecimatePoints()
    {

        int lat1 = 3737890;
        int lon1 = -12201074;
        
        int lat2 = 3737890;
        int lon2 = -12199934;
        
        int lat3 = 3737392;
        int lon3 = -12199934;
        
        int lat4 = 3737392;
        int lon4 = -12201074;
        
        Vector points = new Vector();
        points.addElement(new int[]{lat1, lon1});
        points.addElement(new int[]{lat2, lon2});
        points.addElement(new int[]{lat3, lon3});
        points.addElement(new int[]{lat4, lon4});
        
        Vector expected = (Vector)points.clone();
        int numPoints = 4;
        int zoom = 3;
        
        int distThreshold = MapUtil.VECTOR_MAP_DECIMATION_THRESHOLD[zoom];
        int[] p1 = new int[3];
        int[] p2 = new int[2];
        MapUtil.decimatePoints(points, distThreshold, p1, p2);
        //[-498, -1140, 1038]
        //[-498, 0]
        
        assertArrayEquals(new int[]{-498, -1140, 1038}, p1);
        assertArrayEquals(new int[]{-498, 0}, p2);
        
        assertEquals(expected, points);
    }
    
    @Test
    public void testGetHigherZoomTileId()
    {
        int lowZoom = 3;
        int highZoom = 4;
        //xIndex = 3793151;
        //yIndex = 3793151;
        //tempZoom = 3;
        long lowerZoomTileId = 908063447572735l;
        long expectedTileId = 1157719157174399l;
        long actual = MapUtil.getHigherZoomTileId(lowerZoomTileId, lowZoom, highZoom);
        assertEquals(expectedTileId, actual);
    }
    
    @Test
    public void testGetMaxZoom()
    {
        assertEquals(19, MapUtil.getMaxZoom());
    }
    
    @Test
    public void testGetPolygonPointsForEdge()
    {

        int lat1 = 3737890;
        int lon1 = -12201074;
        
        int lat2 = 3737890;
        int lon2 = -12199934;
        
        int lat3 = 3737392;
        int lon3 = -12199934;
        
        int lat4 = 3737392;
        int lon4 = -12201074;
        
        int[] points = new int[]{lat1, lon1, lat2, lon2, lat3, lon3, lat4, lon4};
        int numPoints = 4;
        int d = 2;
        boolean needRoundEnd = true;
        MapUtil.createPolygonForEdge(points, numPoints, d, needRoundEnd);
        int[] expectedPolygonPoints = new int[]{59806272, -195217184, 59806272, -195198944, 59806240, -195198912, 59798272, -195198912, 59798240, -195198944, 59798240, -195217184, 59798244, -195217200, 59798256, -195217212, 59798272, -195217216, 59798287, -195217212, 59798299, -195217200, 59798304, -195217184, 59798304, -195198976, 59806208, -195198976, 59806208, -195217184, 59806212, -195217200, 59806224, -195217212, 59806240, -195217216, 59806255, -195217212, 59806267, -195217200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedPolygonPoints, MapUtil.getPolygonPointsForEdge());
    
    }
    
    @Test
    public void testGetTileCount()
    {
        assertEquals(262144, MapUtil.getTileCount(1));
        assertEquals(131072, MapUtil.getTileCount(2));
        assertEquals(65536, MapUtil.getTileCount(3));
        assertEquals(32768, MapUtil.getTileCount(4));
        assertEquals(16384, MapUtil.getTileCount(5));
        assertEquals(8192, MapUtil.getTileCount(6));
        assertEquals(4096, MapUtil.getTileCount(7));
        assertEquals(2048, MapUtil.getTileCount(8));
        assertEquals(1024, MapUtil.getTileCount(9));
        assertEquals(512, MapUtil.getTileCount(10));
        assertEquals(256, MapUtil.getTileCount(11));
        assertEquals(128, MapUtil.getTileCount(12));
        assertEquals(64, MapUtil.getTileCount(13));
        assertEquals(32, MapUtil.getTileCount(14));
        assertEquals(16, MapUtil.getTileCount(15));
    }
    
    @Test
    public void testGetZoomFromID()
    {
        long tileID = 908063447572735l;
        int expected = 3;
        int actual = MapUtil.getZoomFromID(tileID);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsPolygon()
    {
        assertTrue(MapUtil.isPolygon((byte)1));
        assertTrue(MapUtil.isPolygon((byte)8));
        assertFalse(MapUtil.isPolygon((byte)0));
        assertFalse(MapUtil.isPolygon((byte)9));
    }
    
    @Test
    public void testSetParameters()
    {
        int TILE_SIZE_POWER = MapUtil.TILE_SIZE_POWER;
        int TILE_PIXEL_SIZE = MapUtil.TILE_PIXEL_SIZE;
        int TILE_COUNT_ON_SCREEN = MapUtil.TILE_COUNT_ON_SCREEN;
        int MAX_REQUEST_TILE_COUNT = MapUtil.MAX_REQUEST_TILE_COUNT;
        int MAX_TILE_REQUEST_CACHE_SIZE = MapUtil.MAX_TILE_REQUEST_CACHE_SIZE;
        int PNG_MAP_TILE_IMAGE_CACHE_SIZE = MapUtil.PNG_MAP_TILE_IMAGE_CACHE_SIZE;
        int PNG_MAP_TILE_DATA_CACHE_SIZE = MapUtil.PNG_MAP_TILE_DATA_CACHE_SIZE;
        
        MapUtil.setParameters(0, 0, 6);
        assertEquals(6, MapUtil.TILE_SIZE_POWER);
        assertEquals(64, MapUtil.TILE_PIXEL_SIZE);
        assertEquals(81, MapUtil.TILE_COUNT_ON_SCREEN);
        assertEquals(81, MapUtil.MAX_REQUEST_TILE_COUNT);
        assertEquals(324, MapUtil.MAX_TILE_REQUEST_CACHE_SIZE);
        assertEquals(162, MapUtil.PNG_MAP_TILE_IMAGE_CACHE_SIZE);
        assertEquals(405, MapUtil.PNG_MAP_TILE_DATA_CACHE_SIZE);
        
        MapUtil.TILE_SIZE_POWER = TILE_SIZE_POWER;
        MapUtil.TILE_PIXEL_SIZE = TILE_PIXEL_SIZE;
        MapUtil.TILE_COUNT_ON_SCREEN = TILE_COUNT_ON_SCREEN;
        MapUtil.MAX_REQUEST_TILE_COUNT = MAX_REQUEST_TILE_COUNT;
        MapUtil.MAX_TILE_REQUEST_CACHE_SIZE = MAX_TILE_REQUEST_CACHE_SIZE;
        MapUtil.PNG_MAP_TILE_IMAGE_CACHE_SIZE = PNG_MAP_TILE_IMAGE_CACHE_SIZE;
        MapUtil.PNG_MAP_TILE_DATA_CACHE_SIZE = PNG_MAP_TILE_DATA_CACHE_SIZE;
    }
    
    @Test
    public void testShiftRightDefault()
    {
        long positive = 8392705l;
        long negative = -8392705l;
        
        long expected = 1025;
        long actual = MapUtil.shiftRight(positive);
        assertEquals(expected, actual);
        
        expected = -1025;
        actual = MapUtil.shiftRight(negative);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testShiftRightWithShift()
    {
        int shift = 32;
        long positive = 4400193994753l;
        long negative = -4400193994753l;
        
        long expected = 1025;
        long actual = MapUtil.shiftRight(positive, shift);
        assertEquals(expected, actual);
        
        expected = -1025;
        actual = MapUtil.shiftRight(negative, shift);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testShiftRightNoNeedRound()
    {
        int shift = 32;
        long positive = 4400193994753l;
        long negative = -4400193994753l;
        
        long expected = 1024;
        long actual = MapUtil.shiftRight(positive, shift, false);
        assertEquals(expected, actual);
        
        expected = -1024;
        actual = MapUtil.shiftRight(negative, shift, false);
        assertEquals(expected, actual);
    }
}

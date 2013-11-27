/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapDataFactoryTest.java
 *
 */
package com.telenav.datatypes.map;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-4
 */
public class MapDataFactoryTest
{
    @BeforeClass
    public static void beforeClass()
    {
        MapDataFactory.getInstance().init(new MapDataFactory());
    }
    
    @Test
    public void testCreateMapEdge()
    {
        VectorMapEdge mapEdge = MapDataFactory.getInstance().createMapEdge();
        assertNotNull(mapEdge);
    }
    
    @Test
    public void testCreateMapTile()
    {
        MapTile mapTile = MapDataFactory.getInstance().createMapTile();
        assertNotNull(mapTile);
    }
    
    @Test
    public void testCreateTile()
    {
        Tile tile = MapDataFactory.getInstance().createTile();
        assertNotNull(tile);
    }
    
    @Test
    public void testCreateTileArray()
    {
        int[][] columns = new int[][]{};
        TileArray tileArray = MapDataFactory.getInstance().createTileArray(columns, 1);
        assertNotNull(tileArray);
    }
    
    @Test
    public void testCreateTileMark()
    {
        TileMark tileMark = MapDataFactory.getInstance().createTileMark();
        assertNotNull(tileMark);
    }
    
    @Test
    public void testCreateTrafficTile()
    {
        TrafficTile trafficTile = MapDataFactory.getInstance().createTrafficTile();
        assertNotNull(trafficTile);
    }
}

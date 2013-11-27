/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapTileTest.java
 *
 */
package com.telenav.datatypes.map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *@author yning
 *@date 2011-7-5
 */
public class MapTileTest
{
    MapTile mapTile;
    @Before
    public void setup()
    {
        mapTile = new MapTile();
    }
    
    @Test
    public void testMapEdges()
    {
        VectorMapEdge edge = new VectorMapEdge();
        VectorMapEdge[] edges = new VectorMapEdge[]{edge};
        
        mapTile.setMapEdges(edges);
        assertArrayEquals(edges, mapTile.getMapEdges());
    }
    
    @Test
    public void testTileMarks()
    {
        TileMark mark = new TileMark();
        TileMark[] tileMarks = new TileMark[]{mark};
        mapTile.setTileMarks(tileMarks);
        assertArrayEquals(tileMarks, mapTile.getTileMarks());
    }
}

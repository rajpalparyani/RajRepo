/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapTileAssert.java
 *
 */
package com.telenav.data.datatypes.map;

import junit.framework.TestCase;

import com.telenav.datatypes.map.MapTile;

/**
 *@author Leo (lchen@telenavsoftware.com)
 *@date 2011-10-9
 */
public class MapTileAssert
{
    public static void assertMapTile(MapTile expected, MapTile actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        
        if(expected.getMapEdges() == null)
        {
            TestCase.assertNull(actual.getMapEdges());
        }
        else
        {
            TestCase.assertNotNull(actual.getMapEdges());
            TestCase.assertEquals(expected.getMapEdges().length, actual.getMapEdges().length);
            for (int i = 0; i < expected.getMapEdges().length; i++)
            {
                VectorMapEdgeAssert.assertVectorMapEdge(expected.getMapEdges()[i], actual.getMapEdges()[i]);
            }
        }
        
        if(expected.getTileMarks() == null)
        {
            TestCase.assertNull(actual.getTileMarks());
        }
        else
        {
            TestCase.assertNotNull(actual.getTileMarks());
            TestCase.assertEquals(expected.getTileMarks().length, actual.getTileMarks().length);
            for (int i = 0; i < expected.getTileMarks().length; i++)
            {
                TileMarkAssert.assertTileMark(expected.getTileMarks()[i], actual.getTileMarks()[i]);
            }
        }
    }
}

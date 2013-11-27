/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TileArrayTest.java
 *
 */
package com.telenav.datatypes.map;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-6
 */
public class TileArrayTest
{
    TileArray tileArray;

    int zoom;

    // lat shift count [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    // lon shift count [-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    // -1]
    // ids [1688936917245039, 1688936934022255, 1688936950799471, 1688936967576687, 1688936984353903, 1688937001131119,
    // 1688937017908335, 1688937034685551, 1688937051462767, 1688937068239983, 1688937085017199, 1688937101794415,
    // 1688937118571631, 1688937135348847, 1688937152126063, 1688937168903279, 1688937185680495, 1688937202457711,
    // 1688937219234927, 1688937236012143, 1688937252789359, 1688937269566575, 1688937286343791, 1688937303121007,
    // 1688937319898223]
    // column [[-3003, 7279, 7279], [-3002, 7279, 7279], [-3001, 7279, 7279], [-3000, 7279, 7279], [-2999, 7279, 7279],
    // [-2998, 7279, 7279], [-2997, 7279, 7279], [-2996, 7279, 7279], [-2995, 7279, 7279], [-2994, 7279, 7279], [-2993,
    // 7279, 7279], [-2992, 7279, 7279], [-2991, 7279, 7279], [-2990, 7279, 7279], [-2989, 7279, 7279], [-2988, 7279,
    // 7279], [-2987, 7279, 7279], [-2986, 7279, 7279], [-2985, 7279, 7279], [-2984, 7279, 7279], [-2983, 7279, 7279],
    // [-2982, 7279, 7279], [-2981, 7279, 7279], [-2980, 7279, 7279], [-2979, 7279, 7279]]
    @Before
    public void setup()
    {
        int[][] columns = new int[][]
        {
        { -3003, 7279, 7279 },
        { -3002, 7279, 7279 },
        { -3001, 7279, 7279 },
        { -3000, 7279, 7279 },
        { -2999, 7279, 7279 },
        { -2998, 7279, 7279 },
        { -2997, 7279, 7279 },
        { -2996, 7279, 7279 },
        { -2995, 7279, 7279 },
        { -2994, 7279, 7279 },
        { -2993, 7279, 7279 },
        { -2992, 7279, 7279 },
        { -2991, 7279, 7279 },
        { -2990, 7279, 7279 },
        { -2989, 7279, 7279 },
        { -2988, 7279, 7279 },
        { -2987, 7279, 7279 },
        { -2986, 7279, 7279 },
        { -2985, 7279, 7279 },
        { -2984, 7279, 7279 },
        { -2983, 7279, 7279 },
        { -2982, 7279, 7279 },
        { -2981, 7279, 7279 },
        { -2980, 7279, 7279 },
        { -2979, 7279, 7279 } };
        zoom = 6;
        tileArray = new TileArray(columns, zoom);
    }

    @Test
    public void testGetZoom()
    {
        assertEquals(zoom, tileArray.getZoom());
    }

    @Test
    public void testGetLonShift()
    {
        assertEquals(-1, tileArray.getLonShift(0));
    }

    @Test
    public void testGetLatShift()
    {
        assertEquals(0, tileArray.getLatShift(0));
    }

    @Test
    public void testGetTileIdAt()
    {
        assertEquals(1688936917245039l, tileArray.getTileIDAt(0));
        assertEquals(1688936934022255l, tileArray.getTileIDAt(1));
        assertEquals(1688936950799471l, tileArray.getTileIDAt(2));
        assertEquals(1688936967576687l, tileArray.getTileIDAt(3));
        assertEquals(1688936984353903l, tileArray.getTileIDAt(4));
    }

    @Test
    public void testGetTileIDSize()
    {
        assertEquals(25, tileArray.getTileIDSize());
    }

    @Test
    public void testContains()
    {
        assertTrue(tileArray.contains(1688936917245039l));
        assertTrue(tileArray.contains(1688936934022255l));
        assertTrue(tileArray.contains(1688936950799471l));
        assertTrue(tileArray.contains(1688936967576687l));
        assertTrue(tileArray.contains(1688936984353903l));
        assertFalse(tileArray.contains(2008936984353903l));
    }

    // [-50381972369, -50365195153, -50348417937, -50331640721, -50314863505, -50298086289, -50281309073, -50264531857,
    // -50247754641, -50230977425, -50214200209, -50197422993, -50180645777, -50163868561, -50147091345, -50130314129,
    // -50113536913, -50096759697, -50079982481, -50063205265, -50046428049, -50029650833, -50012873617, -49996096401,
    // -49979319185]
    @Test
    public void testGetBottomIds()
    {
        long[] ids = tileArray.getBottomIds();
        long[] expected = new long[]
        { -50381972369l, -50365195153l, -50348417937l, -50331640721l, -50314863505l, -50298086289l, -50281309073l, -50264531857l,
                -50247754641l, -50230977425l, -50214200209l, -50197422993l, -50180645777l, -50163868561l, -50147091345l, -50130314129l,
                -50113536913l, -50096759697l, -50079982481l, -50063205265l, -50046428049l, -50029650833l, -50012873617l, -49996096401l,
                -49979319185l };
        assertArrayEquals(expected, ids);
    }

    @Test
    public void testGetTopIds()
    {
        long[] ids = tileArray.getTopIds();
        long[] expected = new long[]
        { -50381972369l, -50365195153l, -50348417937l, -50331640721l, -50314863505l, -50298086289l, -50281309073l, -50264531857l,
                -50247754641l, -50230977425l, -50214200209l, -50197422993l, -50180645777l, -50163868561l, -50147091345l, -50130314129l,
                -50113536913l, -50096759697l, -50079982481l, -50063205265l, -50046428049l, -50029650833l, -50012873617l, -49996096401l,
                -49979319185l };

        assertArrayEquals(expected, ids);
    }

    @Test
    public void testGetLeftIds()
    {
        long[] ids = tileArray.getLeftIds();
        long[] expected = new long[]
        { -50381972369l };

        assertArrayEquals(expected, ids);
    }

    @Test
    public void testGetRightIds()
    {
        long[] ids = tileArray.getRightIds();
        long[] expected = new long[]
        { -49979319185l };

        assertArrayEquals(expected, ids);
    }

    @Test
    public void testGetCenterIds()
    {
        long[] ids = tileArray.getCenterIds(2);
        long[] expected = new long[]
        { -50197422993l, -50180645777l, -50163868561l, -50147091345l, 0l, 0l, 0l, 0l, 0l, 0l, 0l, 0l, 0l, 0l, 0l, 0l };

        assertArrayEquals(expected, ids);
    }
    
    @Test
    public void testGetCenterId()
    {
        long id = tileArray.getCenterId();
        long expected = -50163868560l;
        
        assertEquals(expected, id);
    }
    
    @Test(expected=NullPointerException.class)
    public void testColumnNull()
    {
        tileArray = new TileArray(null, 3);
    }
    
    @Test
    public void testColumesEmpty()
    {
        int[][] columns = new int[][]{};
        tileArray = new TileArray(columns, 3);
        
        assertNull(tileArray.getTopIds());
        assertNull(tileArray.getBottomIds());
        assertNull(tileArray.getLeftIds());
        assertNull(tileArray.getRightIds());
        assertNull(tileArray.getCenterIds(5));
        assertEquals(0, tileArray.getCenterId());
    }
}

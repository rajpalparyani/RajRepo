/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TileTest.java
 *
 */
package com.telenav.datatypes.map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *@author yning
 *@date 2011-7-6
 */
public class TileTest
{
    Tile tile;
    
    @Before
    public void setup()
    {
        tile = new Tile();
    }
    
    @Test
    public void testSetGetBinary()
    {
        assertNull(tile.getBinary());
        byte[] fakeData = new byte[10];
        for(int i = 0; i < fakeData.length; i++)
        {
            fakeData[i] = (byte)i;
        }
        
        tile.setBinary(fakeData);
        assertArrayEquals(fakeData, tile.getBinary());
    }
    
    @Test
    public void testSetGetExpireTime()
    {
        assertEquals(0, tile.getExpireTime());
        long time = System.currentTimeMillis();
        tile.setExpireTime(time);
        assertEquals(time, tile.getExpireTime());
    }
    
    @Test
    public void testSetGetId()
    {
        assertEquals(0, tile.getId());
        int id = 1234;
        tile.setId(id);
        assertEquals(id, tile.getId());
    }
    
    @Test
    public void testIsEmpty()
    {
        assertEquals(false, tile.isEmpty());
        boolean isEmpty = true;
        tile.setIsEmpty(isEmpty);
        assertEquals(isEmpty, tile.isEmpty());
        isEmpty = false;
        tile.setIsEmpty(isEmpty);
        assertEquals(isEmpty, tile.isEmpty());
    }
    
    @Test
    public void testIsPng()
    {
        assertEquals(false, tile.isPng());
        boolean isPng = true;
        tile.setIsPng(isPng);
        assertEquals(isPng, tile.isPng());
        isPng = false;
        tile.setIsPng(isPng);
        assertEquals(isPng, tile.isPng());
    }
    
    @Test
    public void testSetGetPixel()
    {
        assertEquals(0, tile.getPixel());
        int pixel = 1024;
        tile.setPixel(pixel);
        assertEquals(pixel, tile.getPixel());
    }
    
    @Test
    public void testSetGetRenderingData()
    {
        assertEquals(null, tile.getRenderingData());
        Object renderingData = new Object();
        tile.setRenderingData(renderingData);
        assertEquals(renderingData, tile.getRenderingData());
    }
   
    @Test
    public void testSetGetStatus()
    {
        assertEquals(0, tile.getStatus());
        byte status = 1;
        tile.setStatus(status);
        assertEquals(status, tile.getStatus());
    }
    
    @Test
    public void testSetGetZoom()
    {
        assertEquals(0, tile.getZoom());
        int zoom = 9;
        tile.setZoom(zoom);
        assertEquals(zoom, tile.getZoom());
    }
}

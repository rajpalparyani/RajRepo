/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TileMarkTest.java
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
public class TileMarkTest
{
    TileMark tileMark;
    
    @Before
    public void setup()
    {
        tileMark = new TileMark();
    }
    
    @Test
    public void testSetGetMapEdge()
    {
        VectorMapEdge e = new VectorMapEdge();
        tileMark.setMapEdge(e);
        assertEquals(e, tileMark.getMapEdge());
    }
    
    @Test
    public void testSetGetHeading()
    {
        int heading = 150;
        tileMark.setHeading(heading);
        assertEquals(heading, tileMark.getHeading());
    }
    
    @Test
    public void testSetGetLabelType()
    {
        byte labelType = (byte)TileMark.LABEL_TYPE_AREA_BUILDING;
        tileMark.setLabelType(labelType);
        assertEquals(labelType, tileMark.getLabelType());
    }
    
    @Test
    public void testSetGetMarkType()
    {
        byte markType = (byte)TileMark.MARK_TYPE_BUILDING_CENTER;
        tileMark.setMarkType(markType);
        assertEquals(markType, tileMark.getMarkType());
    }
    
    @Test
    public void testSetGetName()
    {
        String name = "test";
        tileMark.setName(name);
        assertEquals(name, tileMark.getName());
    }
    
    @Test
    public void testSetGetPosition()
    {
        int lat = 3737392;
        int lon = -12201074;
        
        tileMark.setPosition(lat, lon);
        assertEquals(lat, tileMark.getPositionLat());
        assertEquals(lon, tileMark.getPositionLon());
    }
    
    @Test
    public void testSetGetRelatedPoint()
    {
        int x = 1234567;
        int y = 7654321;
        
        tileMark.setRelatedPoint(x, y);
        assertEquals(x, tileMark.getRelatedPointX());
        assertEquals(y, tileMark.getRelatedPointY());
    }
    
    @Test
    public void testSetGetScreenHeading()
    {
        int screenHeading = 150;
        tileMark.setScreenHeading(screenHeading);
        assertEquals(screenHeading, tileMark.getScreenHeading());
    }
    
    @Test
    public void testSetGetScreenPosition()
    {
        int x = 1234567;
        int y = 7654321;
        
        tileMark.setScreenPosition(x, y);
        assertEquals(x, tileMark.getScreenX());
        assertEquals(y, tileMark.getScreenY());
    }
    
    @Test
    public void testSetGetStyle()
    {
        byte style = TileMark.STYLE_ITALIC;
        tileMark.setStyle(style);
        assertEquals(style, tileMark.getStyle());
    }
    
    @Test
    public void testSetGetSize()
    {
        byte size = 121;
        tileMark.setSize(size);
        assertEquals(size, tileMark.getSize());
    }
    
    @Test
    public void testIsAttachedToMapTile()
    {
        boolean isAttachedToMapTile = false;
        tileMark.setIsAttachedToMapTile(isAttachedToMapTile);
        assertEquals(isAttachedToMapTile, tileMark.isAttachedToMapTile());
        isAttachedToMapTile = true;
        tileMark.setIsAttachedToMapTile(isAttachedToMapTile);
        assertEquals(isAttachedToMapTile, tileMark.isAttachedToMapTile());
    }
    
    @Test
    public void testReset()
    {
        VectorMapEdge e = new VectorMapEdge();
        tileMark.setMapEdge(e);
        TileMark leftTile = new TileMark();
        TileMark rightTile = new TileMark();
        tileMark.leftTile = leftTile;
        tileMark.rightTile = rightTile;
        
        assertEquals(e, tileMark.getMapEdge());
        assertEquals(leftTile, tileMark.leftTile);
        assertEquals(rightTile, tileMark.rightTile);
        
        tileMark.reset();
        assertEquals(null, tileMark.getMapEdge());
        assertEquals(null, tileMark.leftTile);
        assertEquals(null, tileMark.rightTile);
    }
}

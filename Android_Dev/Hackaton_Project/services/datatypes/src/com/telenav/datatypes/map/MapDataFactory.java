/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MapDataFactory.java
 *
 */
package com.telenav.datatypes.map;


/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 20, 2010
 */
public class MapDataFactory
{
    protected static MapDataFactory instance = new MapDataFactory();

    private static int initCount;

    protected MapDataFactory()
    {

    }

    public static MapDataFactory getInstance()
    {
        return instance;
    }

    public synchronized static void init(MapDataFactory factory)
    {
        if (initCount >= 1)
            return;

        instance = factory;
        initCount++;
    }
    
    public MapTile createMapTile()
    {
        return new MapTile();
    }
    
    public Tile createTile()
    {
        return new Tile();
    }
    
    public TileArray createTileArray(int[][] columns, int zoom)
    {
        return new TileArray(columns, zoom);
    }
    
    public TileMark createTileMark()
    {
        return new TileMark();
    }
    
    public TrafficTile createTrafficTile()
    {
        return new TrafficTile();
    }
    
    public VectorMapEdge createMapEdge()
    {
        return new VectorMapEdge();
    }
}

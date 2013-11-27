package com.telenav.map.opengl.java.proxy;

import java.util.Vector;

import com.telenav.cache.intcache.LongIntVector;
import com.telenav.datatypes.map.Tile;

abstract public class AbstractTileProvider
{
    public static final int TYPE_VECTOR_MAP             = 1;
    public static final int TYPE_VECTOR_TRAFFIC         = 2;
    public static final int TYPE_PNG_MAP                = 3;
    public static final int TYPE_PNG_TRAFFIC_FLOW       = 4;
    
    public abstract Tile getTile(int type, long id); 
    public abstract void getTile(int type, LongIntVector tileIds, Vector tiles);
    public abstract void flush();
}

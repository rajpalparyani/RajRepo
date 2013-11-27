package com.telenav.mapcontent;

import com.telenav.cache.intcache.LongIntLRUCache;
import com.telenav.datatypes.map.Tile;

public class TileManager
{
    final static protected int RESPONSE_TYPE_ID_MAP         = 1;
    
    public static final int TYPE_PNG_MAP               = 1;
    public static final int TYPE_VECTOR_MAP            = 2;
    public static final int TYPE_PNG_TRAFFIC           = 3;
    public static final int TYPE_VECTOR_TRAFFIC        = 4;
    
    private LongIntLRUCache cacheTiles;
    
    protected Object mutex = new Object();
    
    public TileManager(LongIntLRUCache cache)
    {
        this.cacheTiles = cache;
    }

    public void clear()
    {
        if (cacheTiles != null)
        {
            cacheTiles.clear();
        }
    }

    public void addTile(Tile tile)
    {
        if (tile == null)
            return;
        
        cacheTiles.put(tile.getId(), tile);
    }
    
    public Tile getTile(long id)
    {
        Tile tile = null;
        {
            Object obj = cacheTiles.get(id);
            
            if (obj == null)
            {
                return null;
            }
            else
            {
                tile = (Tile) obj;
            }
        }
                
        return tile;
    }

    public boolean containsTile(long id)
    {
        Tile tile = (Tile) cacheTiles.get(id);
        return tile != null && System.currentTimeMillis() < tile.getExpireTime();
    }

    public static long getTileIdWithZoom(long tileId)
    {
        return (tileId & 0xFFFFFFFFFFFFFFL); 
    }
    
    public void resume()
    {
    }

    public void pause()
    {
        if (cacheTiles != null)
        {
//            cacheTiles.store();
        }
    }
}

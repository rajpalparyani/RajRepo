/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapCache.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.cache.intcache.LongIntLRUCache;
import com.telenav.datatypes.map.Tile;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapCache
{
    LongIntLRUCache mapCache;

    public TnMapCache(int capacity)
    {
        mapCache = new LongIntLRUCache(capacity);
    }


    public void addTile(Tile tile)
    {
        mapCache.put(tile.getId(), tile);
    }


    /*!
    *    Activates a tile that already exists
    */
    public void makeTileActive(Tile tile)
    {
        //do nothing
    }


    /*!
    *    Returns a tile based on its tileID
    */
    public Tile getTile(long tileId)
    {
        Tile it = (Tile)mapCache.get(tileId);

        if (it != null) 
        {
            // Found in active cache.
            return it;
        }

        // Not found. Return null pointer.
        return null;
    }
    
    public void removeTile(Tile tile)
    {
        this.removeTile(tile, true);
    }

    public void removeTile(Tile tile, boolean cache)
    {
        removeTile(tile.getId(), cache);
    }
    
    public void removeTile(long tileId)
    {
        this.removeTile(tileId, true);
    }

    /*!
    *    Removes a tile object based upon tileID, moving it from active list to the inactive list
    */
    public void removeTile(long tileId, boolean cache)
    {
        //Test if tile is in the active cache
        Tile it = (Tile)mapCache.get(tileId);

        if (it != null)
        {
            mapCache.remove(tileId);
        }
    }

    /*!
    *    Return the list of active tiles in the cache
    */
    public LongIntLRUCache getTiRles()
    {
        return mapCache;
    }

    /*!
     *    Clear entire cache
     */
    public void clear()
    {
        mapCache.clear();
    }
}

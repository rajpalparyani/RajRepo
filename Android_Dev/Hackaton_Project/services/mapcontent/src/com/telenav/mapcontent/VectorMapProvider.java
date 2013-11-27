package com.telenav.mapcontent;

import com.telenav.cache.intcache.LongIntLRUCache;
import com.telenav.datatypes.map.Tile;

public class VectorMapProvider extends TileProvider
{
    public VectorMapProvider(AbstractMapConnector connector, 
            ITileListener listener, int tileCache)
    {
        super(new LongIntLRUCache(tileCache), connector, listener);
        super.streamer = new Streamer(AbstractStreamer.ACT_OLD_VECTOR_TILE_REQUEST);
    }

    protected void onTileReceived(Tile[] tiles)
    {
        if(tiles != null)
        {
            for (int i = 0; i < tiles.length; i++)
            {
                if (tiles[i] == null)
                    continue;

                Tile expandedeTile = this.streamer.unmarshal(tiles[i]);
                if (expandedeTile != null)
                    tileManager.addTile(expandedeTile);
            }
        }
        if (tileListener != null)
            tileListener.tileArrived();
    }
}


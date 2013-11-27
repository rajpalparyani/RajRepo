package com.telenav.mapcontent;

import com.telenav.cache.intcache.LongIntLRUCache;
import com.telenav.datatypes.map.MapUtil;

public class PngMapProvider extends TileProvider
{
    public PngMapProvider(AbstractMapConnector connector, ITileListener listener)
    {
        super(new LongIntLRUCache(MapUtil.PNG_MAP_TILE_DATA_CACHE_SIZE), connector,
                listener);
        super.streamer = new Streamer(AbstractStreamer.NEW_NAVMAP_TYPE_ALL_IN_ONE_PNG);
    }
}

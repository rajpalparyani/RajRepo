/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TileRequest.java
 *
 */
package com.telenav.mapcontent;

import com.telenav.datatypes.LongIntArrayList;
import com.telenav.datatypes.map.Tile;
import com.telenav.mapcontent.AbstractMapConnector.IConnectorCallback;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 18, 2010
 */
class TileRequest implements IConnectorCallback
{
    protected TileProvider tileProvider;
    protected LongIntArrayList remainTileIds;
    
    public TileRequest(TileProvider tileProvider)
    {
        this.tileProvider = tileProvider;
        this.remainTileIds = new LongIntArrayList(MapContentConfig.MAX_TILE_REQUEST_CACHE_SIZE + 10);
    }
    
    public LongIntArrayList getRemainTileIds()
    {
        return this.remainTileIds;
    }
    
    public boolean isFetching(long id)
    {
        return remainTileIds.contains(id);
    }
    
    public void send(LongIntArrayList requestTileIds)
    {
        if (requestTileIds.size() > 0)
        {
            while(remainTileIds.size() >= MapContentConfig.MAX_TILE_REQUEST_CACHE_SIZE * 3)
            {
                remainTileIds.removeElementAt(0);
            }
            
            long[] ids = new long[requestTileIds.size()];
            for (int i = 0; i < ids.length; i++)
            {
                ids[i] = requestTileIds.elementAt(i);
                
                remainTileIds.addElement(ids[i]);
            }

            byte[] req = tileProvider.getStreamer().createServerRequest(ids);
            this.tileProvider.getConnector().send(req, this);
        }
    }

    public void onCancelled()
    {
        this.tileProvider.onCancelled();
    }

    public void onError(String error, Throwable exception)
    {
        this.tileProvider.onError(error, exception);
    }

    public void onFinished()
    {
        this.tileProvider.onFinished();
    }

    public void onDataReceived(byte[] response)
    {
        Tile[] tiles = tileProvider.getStreamer().parseServerResponse(response);
        if (tiles != null)
        {
            for (int i=0; i<tiles.length; i++)
            {
                if (tiles[i] == null)
                    continue;
                remainTileIds.removeElement(tiles[i].getId());
            }
        }
        
        this.tileProvider.onTileReceived(tiles);
    }
}

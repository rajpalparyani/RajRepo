package com.telenav.mapcontent;

import com.telenav.cache.intcache.LongIntLRUCache;
import com.telenav.datatypes.LongIntArrayList;
import com.telenav.datatypes.Node;
import com.telenav.datatypes.map.Tile;

public abstract class TileProvider
{
    protected static Node[] mandatoryNodes;
    
    protected TileManager tileManager;
    
    protected LongIntArrayList requestTileIds;
    
    protected LongIntArrayList tempRequestTileIds;
    
    protected AbstractMapConnector connector;
    
    protected AbstractStreamer streamer;
    
    protected ITileListener tileListener;
    
    protected TileRequest tileRequest;
    
    public TileProvider(LongIntLRUCache tileCache, AbstractMapConnector connector, 
            ITileListener listener)
    {
        this.requestTileIds = new LongIntArrayList(MapContentConfig.MAX_TILE_REQUEST_CACHE_SIZE + 10);
        this.tempRequestTileIds = new LongIntArrayList(MapContentConfig.TILE_COUNT_ON_SCREEN + 10);
        tileManager = new TileManager(tileCache);
        this.connector = connector;
        
        this.tileListener = listener;
        
        this.tileRequest = new TileRequest(this);
        this.connector.setConnectorCallback(this.tileRequest);
    }
    
    /**
     * set the mandatory nodes for tile requesting
     * @param n
     */
    public static void setMandatoryNodes(Node[] n)
    {
        mandatoryNodes = n;
    }
    
    public AbstractStreamer getStreamer()
    {
        return streamer;
    }
    
    /**
     * used by map to request tiles from server
     * @return
     * @hide
     */
    public static Node[] getMandatoryNodes()
    {
        return mandatoryNodes;
    }
    
    public Tile getTile(long id)
    {
        return getTile(id, true);
    }
    
    public Tile getTile(long id, boolean requestFromServerIfNeeded)
    {
        if (tileManager.containsTile(id))
        {
            return tileManager.getTile(id);
        }
        else
        {
            if (requestFromServerIfNeeded)
            {
                tempRequestTileIds.addElement(id);
            }
        }
        return null;
    }
    
    protected boolean isFetching(long id)
    {
        return tileRequest != null && tileRequest.isFetching(id);
    }
    
    protected boolean isInArray(long[] array, long id)
    {
        if (array == null)
            return false;
        
        for (int i=0; i<array.length; i++)
            if (id == array[i])
                return true;
        
        return false;
    }

    public void flush(long[] centerIds)
    {
        synchronized(requestTileIds)
        {
            for (int i=tempRequestTileIds.size()-1; i>=0; i--)
            {
                long id = tempRequestTileIds.elementAt(i);
                if (isInArray(centerIds, id))
                    continue;
                
                if (!requestTileIds.contains(id) && !isFetching(id))
                {
                    if (requestTileIds.size() >= MapContentConfig.MAX_TILE_REQUEST_CACHE_SIZE)
                        requestTileIds.removeElementAt(0);
                    requestTileIds.addElement(id);
                }
                tempRequestTileIds.removeElementAt(i);
            }
            
            for (int i=tempRequestTileIds.size()-1; i>=0; i--)
            {
                long id = tempRequestTileIds.elementAt(i);
                if (!requestTileIds.contains(id) && !isFetching(id))
                {
                    if (requestTileIds.size() >= MapContentConfig.MAX_TILE_REQUEST_CACHE_SIZE)
                        requestTileIds.removeElementAt(0);
                    requestTileIds.addElement(id);
                }
                tempRequestTileIds.removeElementAt(i);
            }
            
            this.tileRequest.send(this.requestTileIds);
            this.requestTileIds.removeAllElements();
        }
    }
    
    public void release()
    {
        
    }
    
    public void pause()
    {
    }
    
    public void resume()
    {
    }
    
    public void releaseForMemory()
    {
    }

    public AbstractMapConnector getConnector()
    {
        return this.connector;
    }
    
    protected void onCancelled()
    {
        
    }

    protected void onError(String error, Throwable exception)
    {
        System.out.println("Tile request error: "+error+",  "+exception);
    }

    protected void onFinished()
    {
        synchronized(requestTileIds)
        {
            if(this.tileRequest == null)
                return;
            
            LongIntArrayList remainTileIds = this.tileRequest.getRemainTileIds();
            for (int i=0; remainTileIds!=null && i<remainTileIds.size(); i++)
            {
                tempRequestTileIds.insertElementAt(remainTileIds.elementAt(i), 0);
            }
        }
    }

    protected void onTileReceived(Tile[] tiles)
    {
        if(tiles != null)
        {
            for (int i = 0; i < tiles.length; i++)
            {
                if (tiles[i] == null)
                    continue;

                tileManager.addTile(tiles[i]);
            }
        }
        if (tileListener != null)
            tileListener.tileArrived();
    }
}

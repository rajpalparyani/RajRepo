/*
 *  @file ITileProxy.java    
 *  @copyright (c) 2007 Telenav, Inc.
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;


/**
 * interface to define function of TileProxy
 * @author dhzhao
 * @modificator qli : porting from TN6x
 * @version 1.0 2007-8-3
 */
public interface ITileProxy
{    
    //service type definition
//    public static final int SERVICE_TYPE_GET_MAP_TILE                      = 0;
//    public static final int SERVICE_TYPE_GET_TRAFFIC_TILE                  = 1;
//    public static final int SERVICE_TYPE_GET_PNG_TILE                      = 2;
//    public static final int SERVICE_TYPE_GET_STATIC_VECTOR_MAP             = 3;
//    public static final int SERVICE_TYPE_GET_TILE_MARKS                    = 4;
    
    /**
     * map tiles request
     * @param tileId
     * @param type
     * @param zoom
     * @param pixel
     * @return
     */
    public int requestMapTiles(Vector tileId, int type, int zoom, int pixel);
    
    /**
     * SERVICE_TYPE_GET_MAP_TILE = 0
     * map tiles request
     * @param tileIds
     * @param zooms
     * @param reqNodeAudio
     * @param reqNodeEdges
     * @param isInsertInFront
     * @return
     */
//    public int requestMapTiles(long[] tileIds, long[] zooms, INode reqNodeAudio, INode reqNodeEdges, boolean isInsertInFront);
    
    /**
     * SERVICE_TYPE_GET_TRAFFIC_TILE   = 1
     * traffic tiles request
     * @param tileIds
     * @param zooms
     * @return
     */
//    public int requestTrafficTiles(long[] tileIds, long[] zooms);
    
    /**
     * get vector of map tiles
     * @return
     */
    public Vector getMapTiles();
    
    /**
     * get vactor of traffic tiles
     * @return
     */
    public Vector getTrafficTiles();
    
    /**
     * get node of dynamic audio
     * @return
     */
//    public INode getDynamicAudio();
    
    public abstract Vector getMapId();

    public abstract Vector getTrafficId();

    public abstract int getZoom();

    /**
     * get route edges
     * @return
     */
//    public INode getRouteEdges();
    
    //These interfaces are from TN6.0 
    public static final int SERVICE_TYPE_INVALID  = -1;
    public final static int NODE_TYPE_VECTOR_TILE = 2;
    public final static int NODE_TYPE_VECTOR_TILE_MARKS  = 3;
    public final static int NODE_TYPE_VECTOR_TILE_EDGES  = 4;
    public final static int NODE_TYPE_VECTOR_TRAFFIC_TILE = 1243;
    public final static int NODE_TYPE_UNSHIFTED_VECTOR_TRAFFIC_TILE = 1244;
    public final static int NODE_TYPE_PNG_MAP_TILE = 1260;
    public final static int NODE_TYPE_PNG_TRAFFIC_TILE = 1261;
    
    /**
     * Gets tiles retrieved from the server
     * @return a vector with tiles
     */
//    public Vector getTiles();
    //End TN6.0
    
}

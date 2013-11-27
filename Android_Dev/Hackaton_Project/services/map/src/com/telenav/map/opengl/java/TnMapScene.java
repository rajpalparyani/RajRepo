/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapScene.java
 *
 */
package com.telenav.map.opengl.java;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.cache.intcache.LongIntVector;
import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.map.MapTile;
import com.telenav.datatypes.map.MapUtil;
import com.telenav.datatypes.map.Tile;
import com.telenav.datatypes.map.TileArray;
import com.telenav.datatypes.map.TrafficTile;
import com.telenav.datatypes.map.VectorMapEdge;
import com.telenav.datatypes.traffic.VectorTrafficEdge;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Ray.Rayf;
import com.telenav.map.opengl.java.proxy.AbstractTileProvider;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-1
 */
public class TnMapScene
{
    protected AbstractTileProvider tileProvider;
    
    protected Vector visibleMapTiles = new Vector(64);
    
    protected Vector visibleTrafficTiles = new Vector(64);
    
    protected LongIntVector visibleTileIds = new LongIntVector(64);
    
    protected int prevZoomLevel;
    
    private Rayf touchRay = new Rayf(0, 0, 0, 0, 0, 0);
    private float[] p1 = new float[3];
    private int[][] points = new int[4][2];

    public TnMapScene(AbstractTileProvider tileProvider)
    {
        this.tileProvider = tileProvider;
    }
    
    /*!
     *    Updates the scene with new information
     *
     *    Goes through visible tile list and add/removes tiles.
     *
     *    Algorithm is based on testing nearest neighbors
     *    (top,left,right,bottom) for visibility.  Based upon those tests
     *    tiles are removed or added to the visibility list
     *
     *    @TODO: Improve performance by using TnMapTile objects instead of
     *    tile IDs
     *
     *    @TODO: Only perform testing on edge tiles to reduce number of
     *    tests performed
     */
    public void update(double[] origin, 
            int zoomLevel,
            TnMapCamera camera,
            float screenWidth, float screenHeight)
    {
        //Planef ground = new Planef(0, 0, 1, 0);

        camera.GetTouchRay(0, 0, touchRay); //top left
        boolean b1 = Pe.intersect(0, 0, 1, 0, touchRay, p1);
        if (!b1)
            return;
        points[0][DataUtil.LON] = (int)(p1[0] + origin[0]);
        points[0][DataUtil.LAT] = (int)(p1[1] + origin[1]);

        camera.GetTouchRay(screenWidth, 0, touchRay); //top right
        b1 = Pe.intersect(0, 0, 1, 0, touchRay, p1);
        if (!b1)
            return;
        points[1][DataUtil.LON] = (int)(p1[0] + origin[0]);
        points[1][DataUtil.LAT] = (int)(p1[1] + origin[1]);
        
        camera.GetTouchRay(screenWidth, screenHeight, touchRay); //bottom right
        b1 = Pe.intersect(0, 0, 1, 0, touchRay, p1);
        if (!b1)
            return;
        points[2][DataUtil.LON] = (int)(p1[0] + origin[0]);
        points[2][DataUtil.LAT] = (int)(p1[1] + origin[1]);

        camera.GetTouchRay(0, screenHeight, touchRay); //bottom left;
        b1 = Pe.intersect(0, 0, 1, 0, touchRay, p1);
        if (!b1)
            return;
        points[3][DataUtil.LON] = (int)(p1[0] + origin[0]);
        points[3][DataUtil.LAT] = (int)(p1[1] + origin[1]);

        TileArray tileArray = MapUtil.computeTileArray(points, zoomLevel, true);

        visibleTileIds.removeAllElements();
        for (int i=0; i<tileArray.getTileIDSize(); i++)
        {
            long id = tileArray.getTileIDAt(i);
            visibleTileIds.addElement(id);
        }

        visibleMapTiles.removeAllElements();
        visibleTrafficTiles.removeAllElements();
        tileProvider.getTile(AbstractTileProvider.TYPE_VECTOR_MAP, visibleTileIds, visibleMapTiles);
        
        tileProvider.getTile(AbstractTileProvider.TYPE_VECTOR_TRAFFIC, visibleTileIds, visibleTrafficTiles);

        for (int i=0; i<visibleMapTiles.size(); i++)
        {
            MapTile tile = (MapTile)visibleMapTiles.elementAt(i);
            if (tile.getRenderingData() == null)
            {
                TnMapRenderingData tnmapRenderingData = new TnMapRenderingData(tile);
                VectorMapEdge[] edges = tile.getMapEdges();
                if (edges == null || edges.length == 0)
                    continue;
                
                tnmapRenderingData.setRoadEdgeData(edges);
                tnmapRenderingData.setPolygonData(edges);
                tnmapRenderingData.setText2DData(tile.getTileMarks());
                tile.setRenderingData(tnmapRenderingData);
            }
            TnMapRenderingData tnmapRenderingData = (TnMapRenderingData)tile.getRenderingData();
            // Pre-calculate each tile view matrix
            tnmapRenderingData.setViewMatrix(camera.GetViewMatrix(tnmapRenderingData.getOrigin()));
        }
        
        for (int i=0; i<visibleTrafficTiles.size(); i++)
        {
            TrafficTile tile = (TrafficTile)visibleTrafficTiles.elementAt(i);
            if (tile.getRenderingData() == null)
            {
                TnTrafficRenderingData tntrafficRenderingData = new TnTrafficRenderingData(tile);
                VectorTrafficEdge[] edges = tile.getTrafficEdges();
                if (edges == null || edges.length == 0)
                    continue;
                
                tntrafficRenderingData.setTrafficEdgeData(edges);
                tile.setRenderingData(tntrafficRenderingData);
            }
            TnTrafficRenderingData tntrafficRenderingData = (TnTrafficRenderingData)tile.getRenderingData();
            // Pre-calculate each tile view matrix
            tntrafficRenderingData.setViewMatrix(camera.GetViewMatrix(tntrafficRenderingData.getOrigin()));
        }
        
        tileProvider.flush();
    }


    /*!
     *    Returns the current tiles that are in the cache and visible on screen
     */
    public Vector getMapTiles() 
    {
        return visibleMapTiles;
    }
    
    public Vector getTrafficTiles()
    {
        return visibleTrafficTiles;
    }

    //MultiMap: SortedMap<key, vector>
    public Hashtable getNearest(TnMapCamera camera, int x, int y, boolean onlyEnabled)
    {

        //Detect Touched objects    
        Hashtable touchedItems = new Hashtable();

        // Iterate through each tile
        for(int i = 0; i < visibleTrafficTiles.size(); i++)
        {
            Tile tile = (Tile)visibleTrafficTiles.elementAt(i);
            if (tile == null || tile.getRenderingData() == null)
                continue;
            // Add a tile's worth of pickables
            ((TnTrafficRenderingData)tile.getRenderingData()).addNearest(touchedItems, camera, x, y, onlyEnabled);
        }

        return touchedItems;
    }

    public void clear()
    {
        visibleMapTiles.removeAllElements();
    }
}

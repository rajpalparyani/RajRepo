package com.telenav.datatypes.map;

public class MapTile extends Tile
{
    protected VectorMapEdge[] mapEdges;
    protected TileMark[] tileMarks;
    
    protected MapTile()
    {
        
    }
    
    public VectorMapEdge[] getMapEdges()
    {
        return mapEdges;
    }
    
    public void setMapEdges(VectorMapEdge[] edges)
    {
        this.mapEdges = edges;
    }

    public TileMark[] getTileMarks()
    {
        return tileMarks;
    }
    
    public void setTileMarks(TileMark[] m)
    {
        this.tileMarks = m;
    }

}

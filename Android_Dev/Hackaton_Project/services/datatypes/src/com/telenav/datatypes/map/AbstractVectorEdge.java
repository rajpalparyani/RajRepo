package com.telenav.datatypes.map;

import com.telenav.datatypes.DataUtil;

public class AbstractVectorEdge
{
    protected byte roadType;
    protected int id;
    
    /**
     * the data for map rendering, different map implementation could
     * have different data structure
     */
    protected Object renderingData;
    
    /**
     * the shape points of the edge or polygon
     * it's not shifted left by {@link MapUtil#RENDER_QFACTOR}
     * the unit is 1 global unit 
     */
    protected int[] shapePoints;
    
    protected AbstractVectorEdge()
    {
        
    }

    public int getID()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * return the global lat at index 
     */
    public int latAt(int index)
    {
        return shapePoints[(index << 1) + DataUtil.LAT];
    }
    
    /**
     * return the global lon at index
     * @param index
     * @return
     */
    public int lonAt(int index)
    {
        return shapePoints[(index << 1) + DataUtil.LON];
    }
    
    public int altitudeAt(int index)
    {
        return 0;
    }

    public int getShapePointsSize()
    {
        if (shapePoints == null)
            return 0;
        return shapePoints.length >> 1;
    }

    public void setShapePoints(int[] points)
    {
        this.shapePoints = points;
    }
    
    /**
     * don't change it to public, we want user to use
     * {@link AbstractVectorEdge#latAt(int)} and {@link #lonAt(int)}
     * to get shape points
     * @return
     */
    private int[] getShapePoints()
    {
        return this.shapePoints;
    }

    public byte getRoadType()
    {
        return roadType;
    }
    
    public void setRoadType(byte roadType)
    {
        this.roadType = roadType;
    }
    
    public Object getRenderingData()
    {
        return renderingData;
    }
    
    public void setRenderingData(Object rd)
    {
        this.renderingData = rd;
    }
}

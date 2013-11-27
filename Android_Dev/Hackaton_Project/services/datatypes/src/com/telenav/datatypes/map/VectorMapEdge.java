package com.telenav.datatypes.map;

import com.telenav.datatypes.DataUtil;

public class VectorMapEdge extends AbstractVectorEdge
{
    protected boolean isPolygon;
    protected boolean isIsland;
    protected String streetName;
    protected byte iconType;
    protected short speedLimit;
    
    protected VectorMapEdge()
    {
        
    }

    public byte getIconType()
    {
        return iconType;
    }

    public short getSpeedLimit()
    {
        return speedLimit;
    }

    public String getStreetName()
    {
        return streetName;
    }

    public void setStreetName(String str)
    {
        this.streetName = str;
    }
    
    public boolean isIsland()
    {
        return isIsland;
    }

    public void setIsIsland(boolean value)
    {
        this.isIsland = value;
    }

    public void setRoadType(byte roadType)
    {
        super.setRoadType(roadType);
        this.isPolygon = MapUtil.isPolygon(roadType);
    }
    
    public boolean isPolygon()
    {
        return isPolygon;
    }
}

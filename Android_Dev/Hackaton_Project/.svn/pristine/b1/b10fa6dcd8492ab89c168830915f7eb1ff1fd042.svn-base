package com.telenav.datatypes.map;

public class TileMark
{
    public static final byte STYLE_PLAIN             = 1;
    public static final byte STYLE_ITALIC            = 2;
    
    public static final int MARK_TYPE_MIN = 31; 
    public static final int MARK_TYPE_UNKNOWN_CENTER = 31; 
    public static final int MARK_TYPE_PARK_CENTER = 32;
    public static final int MARK_TYPE_CAMPUS_CENTER = 33;
    public static final int MARK_TYPE_WATER_CENTER = 34;
    public static final int MARK_TYPE_INTERIOR_CENTER = 35;
    public static final int MARK_TYPE_BUILDING_CENTER = 36;
    public static final int MARK_TYPE_CITY_CENTER = 37;
    public static final int MARK_TYPE_STATE_CENTER = 38;     //  38
    public static final int MARK_TYPE_MAX = MARK_TYPE_STATE_CENTER;
        
    public static final int MARK_TYPE_MAP_LABEL = 39;         //  39

    
    public static final int LABEL_TYPE_CHARACTER_MIN = 0;
    public static final int LABEL_TYPE_GEO_COUNTRY = 0;
    public static final int LABEL_TYPE_GEO_STATE   = 1; 
    public static final int LABEL_TYPE_CITY_5      = 2; 
    public static final int LABEL_TYPE_CITY_4      = 3; 
    public static final int LABEL_TYPE_CITY_3      = 4; 
    public static final int LABEL_TYPE_CITY_2      = 5; 
    public static final int LABEL_TYPE_CITY_1      = 6; 
        
    public static final int LABEL_TYPE_AREA_WATER    = 11; 
    public static final int LABEL_TYPE_AREA_PARK     = 12; 
    public static final int LABEL_TYPE_AREA_CAMPUS   = 13; 
    public static final int LABEL_TYPE_AREA_BUILDING = 14; 
        
    public static final int LABEL_TYPE_ROAD_STREET  = 21;
    public static final int LABEL_TYPE_ROAD_ARTERIA = 22; 
    public static final int LABEL_TYPE_ROAD_HIGHWAY = 23; 
        
    public static final int LABEL_TYPE_CHARACTER_MAX = 23; 
        
    public static final int LABEL_TYPE_ROAD_ICON      = 31; 
    public static final int LABEL_TYPE_ARROW_POSITIVE = 32; 
    public static final int LABEL_TYPE_ARROW_NEGATIVE = 33; 
    public static final int LABEL_TYPE_SEGMENT        = 34;
    
    
    protected byte markType;
    protected byte labelType;
    protected String name;
    
    protected int relatedPointX, relatedPointY;
    
    protected short heading;
    protected byte style;
    protected byte size;
    
    protected short screenHeading;
    
    protected boolean isAttachedToTile = true;
    
    protected TileMark leftTile = null;
    protected TileMark rightTile = null;
    protected int index = -1;
    
    protected int lat, lon;
    protected int x0, y0;   //, x1, y1;
    
    protected int xTemp, yTemp, hTemp;
    
    protected VectorMapEdge mapEdge = null;
    
    protected TileMark()
    {
        
    }
    
    public VectorMapEdge getMapEdge()
    {
        return mapEdge;
    }
    
    public void setMapEdge(VectorMapEdge e)
    {
        this.mapEdge = e;
    }
    
    public int getHeading()
    {
        return heading;
    }

    public int getScreenHeading()
    {
        return screenHeading;
    }

    public byte getLabelType()
    {
        return labelType;
    }
    
    public int getPositionLat()
    {
        return this.lat;
    }
    
    public int getPositionLon()
    {
        return this.lon;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setRelatedPoint(int x, int y)
    {
        this.relatedPointX = x;
        this.relatedPointY = y;
    }
    
    public int getRelatedPointX()
    {
        return relatedPointX;
    }

    public int getRelatedPointY()
    {
        return relatedPointY;
    }

    public byte getSize()
    {
        return size;
    }
    
    public byte getStyle()
    {
        return style;
    }
    
    public void setHeading(int heading)
    {
        this.heading = (short)heading;
    }

    public void setScreenHeading(int heading)
    {
        this.screenHeading = (short)heading;
    }

    public void setLabelType(byte labelType)
    {
        this.labelType = labelType;
    }
    
    public void setPosition(int lat, int lon)
    {
        this.lat = lat;
        this.lon = lon;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setSize(byte size)
    {
        this.size = size;
    }
    
    public void setStyle(byte style)
    {
        this.style = style;
    }
    
    public void reset()
    {
        mapEdge = null;
        leftTile = null;
        rightTile = null;
    }

    public int getScreenX()
    {
        return this.x0;
    }

    public int getScreenY()
    {
        return this.y0;
    }

    public void setScreenPosition(int x, int y)
    {
        this.x0 = x;
        this.y0 = y;
    }

    public byte getMarkType()
    {
        return markType;
    }

    public void setMarkType(byte markType)
    {
        this.markType = markType;
    }

    public boolean isAttachedToMapTile() 
    {
        return this.isAttachedToTile;
    }

    public void setIsAttachedToMapTile(boolean value) 
    {
        this.isAttachedToTile = value;
    }
}

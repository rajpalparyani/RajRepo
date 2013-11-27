package com.telenav.datatypes.route;

import com.telenav.datatypes.DataUtil;

public class RouteEdge
{
    protected int[] shapePoints;
    
    protected int refLat, refLon;
    protected short[] shapePointsOffset;
    
    protected boolean isValid;
    protected int id;
    protected short speedLimit;
    protected byte roadType;
    protected int length;
    
    protected Segment segment;

    protected int[] boundingBox;
    
    protected byte speedMph, speedKph;
    
    protected boolean isLocallyGenerated;

    protected RouteEdge()
    {
        
    }
    
    public int[] getBoundingBox()
    {
        return boundingBox;
    }
    
    public String getStreetName()
    {
        return segment.getStreetName();
    }
    public String getStreetAlias()
    {
        return segment.getStreetAlias();
    }
    
    int getBinsize(RouteEdge edge)
    {
        return
            DataUtil.SIZE_SHORT +                              // number of points
            2 * DataUtil.SIZE_INT +                            // id, speedLimit
            DataUtil.SIZE_BYTE;                                // roadType
    }

    public int getLength()
    {
        return length;
    }
    
    public void setLength(int length)
    {
        this.length = length;
    }

    public byte getRoadType()
    {
        return roadType;
    }

    public int getID()
    {
        return id;
    }

    public int getSpeedLimit()
    {
        return this.speedLimit;
    }
    
    public void setSpeedLimit(int speedLimit)
    {
        this.speedLimit = (short)speedLimit;
    }

    public int getSpeedLimitKph()
    {
        return (speedKph + 128);
    }

    public int getSpeedLimitMph()
    {
        return (speedMph + 128);
    }

    public void setSpeedLimitKph(int speedKph)
    {
        if (speedKph > 255)
            speedKph = 255;
        this.speedKph = (byte)(speedKph - 128);
    }

    public void setSpeedLimitMph(int speedMph)
    {
        if (speedMph > 255)
            speedMph = 255;
        
        this.speedMph = (byte)(speedMph - 128);
    }
 
    public void setID(int id)
    {
        this.id = id;
        
    }
    
    public void setRoadType(byte roadType)
    {
        this.roadType = roadType;
        
    }
    public int getDeltaRs(int index)
    {
        if (index >= numPoints() - 1)
            return 0;
        
        int cosLat = DataUtil.getCosLat(latAt(0));
        int pointLat = latAt(index+1);
        int pointLon = lonAt(index+1);
        int prevPointLat = latAt(index);
        int prevPointLon = lonAt(index);
        
        int len = DataUtil.gpsDistance(
                pointLat - prevPointLat,
                pointLon - prevPointLon,
                cosLat);
        
        if (len == 0)
        {
            //QH: the prevPoint is very close to point
            len = Math.abs(prevPointLat - pointLat) +
                                Math.abs(prevPointLon - pointLon);
        }
        
        return len;
    }

    public int getId()
    {
        return id;
    }
    
    public void setShapePoints(int[] shapePoints)
    {
        if (shapePoints != null)
        {
            boolean isWithinShortRange = processShapePoints(shapePoints);
            if (isWithinShortRange)
            {
                this.shapePoints = null;
                this.shapePointsOffset = new short[shapePoints.length];
                int numPoints = shapePoints.length >> 1;
                int centerLat = (shapePoints[DataUtil.LAT] + shapePoints[((numPoints-1)<<1)+DataUtil.LAT]) >> 1; 
                int centerLon = (shapePoints[DataUtil.LON] + shapePoints[((numPoints-1)<<1)+DataUtil.LON]) >> 1; 
                for (int i=0; i<shapePoints.length; i+=2)
                {
                    shapePointsOffset[i+DataUtil.LAT] = (short)(shapePoints[i+DataUtil.LAT] - centerLat);
                    shapePointsOffset[i+DataUtil.LON] = (short)(shapePoints[i+DataUtil.LON] - centerLon);
                }
                refLat = centerLat;
                refLon = centerLon;
            }
            else
            {
                this.shapePoints = shapePoints;
                this.shapePointsOffset = null;
            }
        }
        else
        {
            this.shapePoints = null;
            this.boundingBox = null;
            this.length = 0;
            this.refLat = 0;
            this.refLon = 0;
            this.shapePointsOffset = null;
        }
    }
    
    public int numPoints()
    {
        if (shapePoints == null)
        {
            if (shapePointsOffset == null)
                return 0;
            else
                return shapePointsOffset.length >> 1;
        }
        else
        {
            return shapePoints.length >> 1;
        }
    }
    
    public int latAt(int index)
    {
        if (shapePoints == null)
            return refLat + shapePointsOffset[(index<<1) + DataUtil.LAT];
        else
            return shapePoints[(index<<1) + DataUtil.LAT];
    }
    
    public int lonAt(int index)
    {
        if (shapePoints == null)
            return refLon + shapePointsOffset[(index<<1) + DataUtil.LON];
        else
            return shapePoints[(index<<1) + DataUtil.LON];
    }

    public void setSegment(Segment seg)
    {
        this.segment = seg;
    }

    public void setBoundingBox(int[] boundingBox)
    {
        this.boundingBox = boundingBox;
        isValid = DataUtil.isValidBoundingBox(boundingBox);
    }
    
    //calculate bounding box, edge length, 
    //and check if the distance of all shape points to the center point are inside [-32768, 32767]
    private boolean processShapePoints(int[] shapePoints)
    {
        //if all shape points are within [-32768, 32767] to the center point 
        boolean isWithinShortRange = true;
        
        int minLat = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int minLon = Integer.MAX_VALUE;
        int maxLon = Integer.MIN_VALUE;
        
        int numPoints = shapePoints.length >> 1;
        int length = 0;
        int prevLat = shapePoints[DataUtil.LAT], prevLon = shapePoints[DataUtil.LON];
        int cosLat = DataUtil.getCosLat(prevLat);
        
        int centerLat = (prevLat + shapePoints[((numPoints-1)<<1)+DataUtil.LAT]) >> 1; 
        int centerLon = (prevLon + shapePoints[((numPoints-1)<<1)+DataUtil.LON]) >> 1; 
        
        for (int i = 0; i < numPoints; i++)
        {
            //calculate the bounding box
            int lat = shapePoints[(i<<1)+DataUtil.LAT];
            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;

            int lon = shapePoints[(i<<1)+DataUtil.LON];
            if (lon < minLon) minLon = lon;
            if (lon > maxLon) maxLon = lon;
            
            //calculate the edge length
            if (i > 0)
            {
                int len = DataUtil.gpsDistance(
                        lat - prevLat,
                        lon - prevLon,
                        cosLat);
                
                if (len == 0)
                {
                    //QH: the prevPoint is very close to point
                    len = Math.abs(prevLat - lat) +
                                        Math.abs(prevLon - lon);
                }
                
                length += len;
            }
            prevLat = lat;
            prevLon = lon;
            
            //check if the point is within [-32768, 32767] to the center point
            int diffLat = lat - centerLat;
            int diffLon = lon - centerLon;
            if (diffLat < -32768 || diffLat > 32767
                    || diffLon < -32768 || diffLon > 32767)
            {
                isWithinShortRange = false;
            }
        }

        int[] bb = new int[4];
        bb[0] = minLat;
        bb[1] = minLon; 
        bb[2] = maxLat;
        bb[3] = maxLon;
        isValid = DataUtil.isValidBoundingBox(bb);
        if (isValid)
            boundingBox = bb;
        
        this.length = length;
        
        return isWithinShortRange;
    }
    
    public int minLat()
    {
        return boundingBox[0];
    }
    
    public int maxLat()
    {
        return boundingBox[2];
    }
    
    public int minLon()
    {
        return boundingBox[1];
    }
    
    public int maxLon()
    {
        return boundingBox[3];
    }
    
    public boolean isValid()
    {
        return isValid;
    }

    public boolean isInClip(int clipLatMin, int clipLonMin, int clipLatMax, int clipLonMax)
    {
        return DataUtil.intersect(clipLatMin, clipLonMin, clipLatMax, clipLonMax, getBoundingBox());
    }

    public boolean isLocallyGenerated() 
    {
        return isLocallyGenerated;
    }

    public void setIsLocallyGenerated(boolean isLocallyGenerated) 
    {
        this.isLocallyGenerated = isLocallyGenerated;
    }
    
    public int getClosestShapePointIndex(int lat, int lon)
    {
        int index = -1;
        int minDistance = -1;
        int currentDistance = -1;
        int pointsCount = this.numPoints();
        for (int i = 0; i < pointsCount; i++)
        {
            currentDistance = DataUtil.distance(Math.abs(latAt(i) - lat), Math.abs(lonAt(i) - lon));
            if(currentDistance < minDistance || minDistance == -1)
            {
                minDistance = currentDistance;
                index = i;
            }
        }
        return index;
    }

}

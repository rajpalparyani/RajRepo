package com.telenav.datatypes.route;

import com.telenav.datatypes.DataUtil;

public class DecimatedRoute extends Route
{
    protected int decimatedThreshhold = 2 << 6;

    protected DecimatedRoute(byte[] buff)
    {
        // (lat [int], lon [int]) and threshhold[short] and related
        // (dLat, dLon)[divided by decimatedThreshhold] in byte format
        int numPoints = buff.length / 2 - 4;

        RouteEdge dEdge = new RouteEdge();
        dEdge.setID(-1); // use negative value to avoid duplicated

        // IDataPool dataPool = edgeManager.getDataPool();
        int[] shapePoints = new int[numPoints * 2];

        int lat = DataUtil.readInt(buff, 0);
        int lon = DataUtil.readInt(buff, 4);
        int threshhold = DataUtil.readShort(buff, 8);

        int doubleI = 0;
        for (int i = 0; i < numPoints; i++, doubleI += 2)
        {
            shapePoints[doubleI + DataUtil.LAT] = lat;
            shapePoints[doubleI + DataUtil.LON] = lon;

            if (i != numPoints - 1)
            {
                int dLat = (int) buff[10 + doubleI] * threshhold;
                int dLon = (int) buff[10 + doubleI + 1] * threshhold;

                lat += dLat;
                lon += dLon;
            }
        }

        dEdge.setShapePoints(shapePoints);

        segments = new Segment[1];
        Segment seg = new Segment();
        seg.setEdgesId(new int[]
        { -1 });
        seg.setEdges(new RouteEdge[]
        { dEdge });
        dEdge.segment = seg;
        segments[0] = seg;
        setBoundingBox(seg.getBoundingBox());
    }

//    public int getRouteID()
//    {
//        return 0;
//    }
//
//    public void setRouteID(int routeID)
//    {
//    }

    public long getOriginTimeStamp()
    {
        return 0;
    }

    public void setOriginTimeStamp(long originTimeStamp)
    {
    }

    public boolean isFinalRoute()
    {
        return false;
    }

    public int getOriginVn()
    {
        return 0;
    }

    public void setOriginVn(int originVn)
    {
    }

    public int getOriginVe()
    {
        return 0;
    }

    public void setOriginVe(int originVe)
    {
    }

    public int getWalkBackTolerance()
    {
        return 0;
    }

    public boolean isPartial()
    {
        return false;
    }

    public int getLength()
    {
        return 0;
    }

    public int calcETA(boolean isConstantSpeed, int constantSpeed)
    {
        return 0;
    }

    public int calcETA(int startSegIndex, int startEdgeIndex, int startPointIndex, int range, boolean isConstantSpeed, int constantSpeed)
    {
        return 0;
    }
}

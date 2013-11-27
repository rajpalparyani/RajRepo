package com.telenav.datatypes.map;

import com.telenav.datatypes.traffic.VectorTrafficEdge;
import com.telenav.datatypes.traffic.TrafficIncident;

public class TrafficTile extends Tile
{
    protected int pointSize;
    protected VectorTrafficEdge[] trafficEdges;
    protected TrafficIncident[] trafficIncidents;
    
    protected long reportTime;
    
    protected TrafficTile()
    {
        
    }
    
    public VectorTrafficEdge[] getTrafficEdges()
    {
        return trafficEdges;
    }

    public TrafficIncident[] getTrafficIncidents()
    {
        return trafficIncidents;
    }

    public void setTrafficEdges(VectorTrafficEdge[] trafficEdges)
    {
        this.trafficEdges = trafficEdges;
    }

    public void setTrafficIncidents(TrafficIncident[] trafficIncidents)
    {
        this.trafficIncidents = trafficIncidents;
    }

    public void reset()
    {
        trafficEdges = null;
        trafficIncidents = null;
    }

    public int getPointSize()
    {
        return pointSize;
    }

    public void setPointSize(int size)
    {
        this.pointSize = size;
    }

    public void setReportTime(long time)
    {
        this.reportTime = time;
    }
    
    public long getReportTime()
    {
        return this.reportTime;
    }
    
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavCommonConvert.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.nav;

import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.Segment;
import com.telenav.navsdk.events.PointOfInterestData;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;

/**
 *@author xiangli
 *@date 2012-3-6
 */
public class NavCommonConvert
{
    static public class LastSentSegment 
    {
        public int index;
    }
    
    public static final int CONVERTCONSTANTS    = 100000;
    public static final double LAT_AND_LON_CONVERT_RATE = 100000d;
    public static NavigationData.RouteSummary.Builder convertRouteToRouteSummary(Route route, int startIndex, LastSentSegment lastSeg, boolean firstRoute)
    {
        NavigationData.RouteSummary.Builder builder = NavigationData.RouteSummary.newBuilder();
        int segmentSize = route.getSegments().length;
        
        MyLog.setLog("Route", "segment size: " + segmentSize);
        boolean hasLastSeg = false;
        for (int segIndex = startIndex; segIndex < segmentSize; ++segIndex)
        {
            Segment segment = route.getSegments()[segIndex];
            if (null==segment || !segment.isEdgeResolved())
            {
                if (!hasLastSeg)
                {
                    if (lastSeg != null)
                    {
                        lastSeg.index = segIndex - 1;
                    }
                    hasLastSeg = true;
                }
                if (!firstRoute)
                    break;
                else if (segment == null)
                    continue;
            }

            NavigationData.RouteTurn.Builder turnBuilder = NavigationData.RouteTurn.newBuilder();
            if (firstRoute)
            {
                //only build turn infor if it is firstRoute chunk
                
                if (segment.getStreetName()!=null)
                    turnBuilder.setDisplayStreetName(segment.getStreetName());
                turnBuilder.setRoundaboutExitNumber(segment.getExitNumber());
                turnBuilder.setTurnType(NavigationData.RouteTurnType.valueOf(segment.getTurnType()));
                turnBuilder.setDistanceToTurnInMeters(segment.getLength());
            }
            builder.addTurn(turnBuilder);
            
            int edgeSize = segment.edgesSize();
            
            //MyLog.setLog("Route", "segment " + segIndex);
            //MyLog.setLog("Route", "edge size: " + edgeSize);
            
            for (int edgeIndex = 0; edgeIndex < edgeSize; ++edgeIndex)
            {
                RouteEdge edge = segment.getEdge(edgeIndex);
                if (null==edge)
                    continue;
                
                int pointSize = edge.numPoints();
                
                //MyLog.setLog("Route", "edge " + edgeIndex);
                //MyLog.setLog("Route", "point amount: " + pointSize);
                
                for (int k = 0; k < pointSize; ++k)
                {
                    if(k==0&&edgeIndex!=0) continue;
                    int iLat=edge.latAt(k);
                    int iLon=edge.lonAt(k);
                    double dLat=((double)iLat)/LAT_AND_LON_CONVERT_RATE;
                    double dLon=((double)iLon)/LAT_AND_LON_CONVERT_RATE;
                    
                    if (((k == pointSize -1) && (edgeIndex == (edgeSize-1))) || ((k == 0) && (edgeIndex == 0)))
                    {
                            
                        MyLog.setLog("Route", "seg=" + segIndex +" edge=" + edgeIndex + " point " + k+" iLat:"+iLat+" iLon:"+iLon+" dLat:"+dLat+" dLon:"+dLon);
                    }
                    
                    PointOfInterestData.PointOfInterest.Builder poiBuilder = PointOfInterestData.PointOfInterest
                            .newBuilder();
                    PointOfInterestData.Location.Builder locationBuilder = PointOfInterestData.Location
                            .newBuilder();
                    
                    locationBuilder.setLatitude(dLat);
                    locationBuilder.setLongitude(dLon);
                    poiBuilder.setLocation(locationBuilder);
                    builder.addWaypoints(poiBuilder);
                }
            }
        }
        int wayPointCount=builder.getWaypointsCount();
        int turnCount=builder.getTurnCount();
        /*
        for(int i=0;i<wayPointCount;++i)
        {
            double dLat=builder.getWaypoints(i).getLocation().getLatitude();
            double dLon=builder.getWaypoints(i).getLocation().getLongitude();
            MyLog.setLog("Route", "waypoint "+i+" lat:"+dLat+" lon:"+dLon);
        }
        */
        builder.setRouteName(String.valueOf(route.getRouteID()));
        if (!hasLastSeg && lastSeg != null) lastSeg.index = segmentSize - 1;
        MyLog.setLog("Route","Convert route, startSeg = " + startIndex + " lastSeg=" + ((lastSeg==null)?"null":lastSeg.index) + ", firstChunk:" + firstRoute +" turn count:"+turnCount + ", waypoints = " +wayPointCount);
        
        return builder;
    }
    
    public static NavigationData.RouteSummary.Builder convertRouteToRouteSummaryWithoutPoint(Route route)
    {
        NavigationData.RouteSummary.Builder builder = NavigationData.RouteSummary.newBuilder();
        int segmentSize = route.getSegments().length;
        
        MyLog.setLog("Route", "segment size: " + segmentSize);
        for (int segIndex = 0; segIndex < segmentSize; ++segIndex)
        {
            Segment segment = route.getSegments()[segIndex];
            if (null==segment)
                continue;
            
            NavigationData.RouteTurn.Builder turnBuilder = NavigationData.RouteTurn.newBuilder();
            if (segment.getStreetName()!=null)
                turnBuilder.setDisplayStreetName(segment.getStreetName());
            turnBuilder.setRoundaboutExitNumber(segment.getExitNumber());
            turnBuilder.setTurnType(NavigationData.RouteTurnType.valueOf(segment.getTurnType()));
            turnBuilder.setDistanceToTurnInMeters(segment.getLength());
            builder.addTurn(turnBuilder);
            
            int edgeSize = segment.edgesSize();
            
            //MyLog.setLog("Route", "segment " + segIndex);
            //MyLog.setLog("Route", "edge size: " + edgeSize);
            
        }
        
        builder.setRouteName(String.valueOf(route.getRouteID()));
        
        return builder;
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteUpdater.java
 *
 */
package com.telenav.module;

import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.Segment;
import com.telenav.logger.Logger;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.ui.citizen.map.MapContainer;

/**
 * Update current route common logic.
 * 
 *@author zhdong@telenav.cn
 *@date 2011-2-14
 */
public class RouteUiHelper
{

    private static int lastRouteId = Integer.MIN_VALUE;

    private static int lastSegmentCount = Integer.MIN_VALUE;

    private static int lastTrunArrowSegmentIndex = Integer.MIN_VALUE;

    private static boolean isRouteCompleted;
    
    private RouteUiHelper()
    {

    }

    public static synchronized void resetCurrentRoute()
    {
        lastRouteId = Integer.MIN_VALUE;
        lastSegmentCount = Integer.MIN_VALUE;
        lastTrunArrowSegmentIndex = Integer.MIN_VALUE;
    }

    public static synchronized void updateCurrentRoute(final MapContainer mapContainer, int currentSegment)
    {
        final Route route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (route == null)
        {
            return;
        }
        
        int routeId = route.getRouteID();
        if (lastRouteId == routeId)
        {
            if (lastSegmentCount == currentSegment)
            {
                // we don't need to update route on map
                return;
            }
        }
        else
        {
            lastRouteId = routeId;
        }
        int segmentCount = route.segmentsSize();
        if(currentSegment < 0 || currentSegment >= segmentCount)
        {
            return;
        }
        lastSegmentCount = currentSegment;
        updateRoute(mapContainer, route, lastSegmentCount);
    }
    
    /**
     * Add a route to openGL engine.
     * 
     * NOTE: We are now using route name to indicate route's color,width etc. It should be refactored later.
     * 
     */
    public static synchronized void updateCurrentRoute(final MapContainer mapContainer)
    {
        final Route route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (route == null)
        {
            return;
        }
        
        int routeId = route.getRouteID();        
        if (lastRouteId == routeId && isRouteCompleted == route.isCompeleted())
        {
            return;
        }
        else
        {
            lastRouteId = routeId;
            isRouteCompleted = route.isCompeleted();
        }
//        int segmentCount = route.segmentsSize();
        updateRoute(mapContainer, route, lastSegmentCount);
    }

    private static synchronized void updateRoute(final MapContainer mapContainer, final Route route, final int currentSegIndex)
    {
        if (Logger.DEBUG)
            Logger.log(Logger.INFO, RouteUiHelper.class.getName(), "updating route");
        Vector routes = new Vector();
        routes.add(route.getRouteID());
        mapContainer.showRoutes(routes, "" + route.getRouteID(), false);
        if (currentSegIndex >= 0)
        {
            showTurnArrow(route.getRouteID(),currentSegIndex, mapContainer);
            mapContainer.lookAtPosition(route.getSegments()[currentSegIndex].getTurnLocation());
        }
        if (Logger.DEBUG)
            Logger.log(Logger.INFO, RouteUiHelper.class.getName(), "end updating map engine");
    }

    private static int findLastResolvedSegment(Route route)
    {
        int i = 0;
        for (; i < route.getSegments().length; i++)
        {
            if (!route.getSegments()[i].isEdgeResolved())
            {
                break;
            }
        }

        return i;
    }
    
    public static int findFirstSegment(Route route, int currentSegment)
    {
        if(route == null)
        {
            return -1;
        }
        int firstSegment = currentSegment;
        for(int i = currentSegment; i >= 0; i--)
        {
            Segment seg = route.segmentAt(i);
            if(seg != null && seg.isEdgeResolved())
            {
                firstSegment = i;
            }
            else
            {
                break;
            }
        }
        return firstSegment;
    }
    
    public static int findLastSegment(Route route, int currentSegment)
    {
        if(route == null)
        {
            return -1;
        }
        int lastSegment = currentSegment;
        for(int i = currentSegment; i < route.segmentsSize(); i++)
        {
            Segment seg = route.segmentAt(i);
            if(seg != null && seg.isEdgeResolved())
            {
                lastSegment = i;
            }
            else
            {
                break;
            }
        }
        return lastSegment;
    }
    
    /**
     * Update current turn arrow
     * 
     * @param segmentIndex
     */
    public static synchronized void showTurnArrow(final int routeId,final int segmentIndex, final MapContainer mapContainer)
    {

        if (lastTrunArrowSegmentIndex == segmentIndex && segmentIndex > 0)
        {
            return;
        }
        lastTrunArrowSegmentIndex = segmentIndex;

        mapContainer.disableAllTurnArrows( "" + routeId);
        mapContainer.enableTurnArrows( "" + routeId, segmentIndex, segmentIndex+1);
    }
    
    public static boolean isGetAllData( Route currentRoute)
    {
        if(currentRoute == null)
        {
            return false;
        }
        int segmentNum = currentRoute.segmentsSize();
        if(segmentNum < 1)
        {
            return false;
        }
        Segment lastSegment = currentRoute.segmentAt(segmentNum - 1);
        return lastSegment.isEdgeResolved();
    }
    
    /**
     * shift the destination latitude/longitude
     * @param latlon latlon[0] mean latitude and latlon[1] mean longitude
     * @param turnType the last segment turn type
     * @param lastEdge the last segment's last edge
     */
    public static void shiftDest(int[] latlon, byte turnType, RouteEdge lastEdge)
    {
        int lastLat = latlon[0];
        int lastLon = latlon[1];
        
        if(lastEdge == null)
        {
            return;
        }
            
        if(turnType != Route.TURN_TYPE_DESTINATION_LEFT && turnType != Route.TURN_TYPE_DESTINATION_RIGHT)
        {
            return;
        }
        
        int pointNum = lastEdge.numPoints();
        if(pointNum < 2)
        {
            return;
        }    
        
        lastLat = lastEdge.latAt(lastEdge.numPoints()-1);
        lastLon = lastEdge.lonAt(lastEdge.numPoints()-1);
        
        int preLat = lastEdge.latAt(pointNum - 2);
        int preLon = lastEdge.lonAt(pointNum - 2);
        
        if(pointNum > 2 && DataUtil.distance(preLat - lastLat, preLon - lastLon) < 5)//why 5? porting from 62
        {
            preLat = lastEdge.latAt(pointNum - 3);
            preLon = lastEdge.lonAt(pointNum - 3);
        }
        
        int direction = DataUtil.bearing(preLat,preLon, lastLat, lastLon);
        int shift = 30; // why 30? porting from 62
        if(turnType == Route.TURN_TYPE_DESTINATION_RIGHT)
        {
            lastLat -= DataUtil.xSinY(shift, direction);
            lastLon += DataUtil.xCosY(shift, direction);
        }
        else
        {
            lastLat += DataUtil.xSinY(shift, direction);
            lastLon -= DataUtil.xCosY(shift, direction);
        }
        
        latlon[0] = lastLat;
        latlon[1] = lastLon;
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TurnMapWrapper.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.app.CommManager;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.nav.NavSdkRouteWrapper;

/**
 *@author yning
 *@date 2010-12-20
 */
public class TurnMapWrapper
{
    public final static int LOOKAHEAD_SEGMENTS = 4;

    public final static int LOOKBACK_SEGMENTS = 3;

    private int currentSegmentIndex;
    
    private int lastSegmentIndexWithEdge;

    /**
     * @return the lastSegmentIndexWithEdge
     */
    protected int getLastSegmentIndexWithEdge()
    {
        return lastSegmentIndexWithEdge;
    }

    /**
     * @param lastSegmentIndexWithEdge the lastSegmentIndexWithEdge to set
     */
    protected void setLastSegmentIndexWithEdge(int lastSegmentIndexWithEdge)
    {
        this.lastSegmentIndexWithEdge = lastSegmentIndexWithEdge;
    }

    private Route route = null;

//    private NavSdkNavigationProxy navigationProxy;

    protected long lastEdgeRequestTime = -1;

    public static long MAX_ROUTE_EDGE_REQUEST_LEN = 250L * 5280 * 8192 / 29919;

    public TurnMapWrapper(int currentSegmentIndex, NavSdkNavigationProxy proxy)
    {
        this.currentSegmentIndex = currentSegmentIndex;
        this.route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
//        navigationProxy = proxy;
        this.lastSegmentIndexWithEdge = 0;
        lastSegmentIndexWithEdge = RouteUiHelper.findLastSegment(route, currentSegmentIndex);
    }

    public Route getRoute()
    {
        return route;
    }

    public int getRouteStyle()
    {
        return NavSdkRouteWrapper.getInstance().getRouteStyle();
    }

    protected int getRouteItemSize()
    {
        return route.segmentsSize();
    }

    /**
     * Gets street name for given segment.
     * 
     * @param index segment index
     * @return street name, or null
     */
    protected String getStreetName(int index)
    {
        return index < route.segmentsSize() ? segmentAt(index).getStreetName() : null;
    }

    protected String getStreetName()
    {
        return getStreetName(currentSegmentIndex);
    }

    /**
     * Get distance to run
     * 
     * @param index
     * @return
     */
    protected int getDistance(int index)
    {
        return index < 0 ? 0 : segmentAt(index).getLength();
    }

    protected int getDistance()
    {
        return getDistance(currentSegmentIndex - 1);
    }

    protected int getTurnIndex(int index)
    {
        if (index == 0)
        {
            return 28; // ?!?! - vladp
        }
        else
        {
            return segmentAt(index).getTurnType();
        }
    }

    protected int getTurnIndex()
    {
        return getTurnIndex(currentSegmentIndex);
    }

    protected boolean isCurrentSegment(int index)
    {
        return index == currentSegmentIndex;
    }

    protected int getCurrentSegmentIndex()
    {
        return currentSegmentIndex;
    }

    protected void setCurrentSegmentIndex(int currentSegmentIndex)
    {
        this.currentSegmentIndex = currentSegmentIndex;
    }

    public boolean segmentDataPresent(int segmentNo)
    {
        int shift = segmentNo == route.segmentsSize() ? -1 : 0;
        return isDataPresent(segmentNo + shift, shift);
    }

    public boolean currentSegmentDataPresent()
    {
        int segFrom = Math.max(0, getCurrentSegmentIndex() - LOOKBACK_SEGMENTS);
        int segTo = Math.min(route.segmentsSize() - 1, getCurrentSegmentIndex() + LOOKAHEAD_SEGMENTS);
        for (int segNo = segFrom; segNo < segTo; segNo++)
        {
            if (!segmentDataPresent(segNo))
            {
                return false;
            }
        }

        return true;
    }

//    public long getCurrentSegmentStartPoint()
//    {
//        int shift = currentSegmentIndex == route.segmentsSize() ? -1 : 0;
//        return getPoint(currentSegmentIndex + shift, shift, shift);
//    }

//    protected RouteEdge getCurrentEdge()
//    {
//        Segment seg = segmentAt(currentSegmentIndex == 0 ? 0 : (currentSegmentIndex - 1));
//        return seg == null ? null : seg.getEdge(seg.edgesSize() - 1);
//    }

    protected boolean goToNextTurn()
    {
        if (hasNextTurn())
        {
            currentSegmentIndex++;
        }
        else
        {
            return false;
        }

        provideSegment();

        return true;
    }

    protected boolean hasNextTurn()
    {
        if (currentSegmentIndex < getRouteItemSize())
            return true;

        return false;
    }

    protected boolean goToPreviousTurn()
    {
        if (hasPreviousTurn())
        {
            currentSegmentIndex--;
        }
        else
        {
            return false;
        }

        provideSegment();

        return true;
    }

    protected boolean hasPreviousTurn()
    {
        if (currentSegmentIndex > 0)
            return true;

        return false;
    }

//    protected int getCurrentHeading()
//    {
//        if (currentSegmentIndex == 0)
//        {
//            Segment seg = segmentAt(0);
//            if (seg != null)
//            {
//                RouteEdge edge = seg.getEdge(0);
//                if (edge.numPoints() <= 1)
//                {
//                    return 0;
//                }
//
//                int index = 0;
//                int lat0 = edge.latAt(index);
//                int lon0 = edge.lonAt(index);
//
//                index++;
//                int lat1 = edge.latAt(index);
//                int lon1 = edge.lonAt(index);
//
//                int startIndex = 0;
//                while (startIndex < seg.edgesSize() - 1 && Math.abs(lon1 - lon0) < 20 && Math.abs(lat1 - lat0) < 20)
//                {
//                    startIndex++;
//                    RouteEdge edge1 = seg.getEdge(startIndex);
//                    lat1 = edge1.latAt(0);
//                    lon1 = edge1.lonAt(0);
//                }
//                return DataUtil.bearing(lat0, lon0, lat1, lon1);
//            }
//        }
//        else
//        {
//            Segment seg = segmentAt(currentSegmentIndex - 1);
//            if (seg != null)
//            {
//                RouteEdge edge = seg.getEdge(seg.edgesSize() - 1);
//
//                if (edge == null || edge.numPoints() <= 1)
//                {
//                    return 0;
//                }
//
//                int index = edge.numPoints() - 1;
//                int lat1 = edge.latAt(index);
//                int lon1 = edge.lonAt(index);
//
//                index--;
//                int lat0 = edge.latAt(index);
//                int lon0 = edge.lonAt(index);
//
//                while (index > 0 && Math.abs(lon1 - lon0) < 20 && Math.abs(lat1 - lat0) < 20)
//                {
//                    index--;
//                    lat0 = edge.latAt(index);
//                    lon0 = edge.lonAt(index);
//                }
//                int heading = DataUtil.bearing(lat0, lon0, lat1, lon1);
//
//                return heading;
//            }
//        }
//
//        return 0;
//    }

    /**
     * @return true if data present for destination point
     */
    public boolean destinationDataPresent()
    {
        return isDataPresent(-1, -1);
    }

    /**
     * Returns a position of destination point. May throw an exception.
     * 
     * @return a long consisting of latitude and longitude; use LongPoint to handle it.
     * @throws IllegalStateException if data missing
     */
//    public long getDestinationPoint()
//    {
//        return getPoint(-1, -1, -1);
//    }

    /**
     * Returns a position of destination point. May throw an exception.
     * 
     * @return a long consisting of latitude and longitude; use LongPoint to handle it.
     * @throws IllegalStateException if data missing
     */
//    protected long getOriginPoint()
//    {
//        return getPoint(0, 0, 0);
//    }

    /**
     * Checks if route data are present for given segment and edge numbers.
     * 
     * @param segNo - segment number, 0-based; negative means from the end, e.g. -1: last
     * @param edgeNo - edge number, 0-based; negative means from the end, e.g. -1: last
     * 
     * @return true if data are present in route.
     */
    public boolean isDataPresent(int segNo, int edgeNo)
    {
        // ignore the edgeNo since we have not edge info at client side
        segNo = (segNo + route.segmentsSize()) % route.segmentsSize();
        Segment seg = segmentAt(segNo);
        if (seg == null || !seg.isEdgeResolved())
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a position of an edge in a segment. May throw an exception.
     * 
     * @param segNo - segment number, 0-based; negative means from the end, e.g. -1: last
     * @param edgeNo - edge number, 0-based; negative means from the end, e.g. -1: last
     * @param pointNo - point number, 0-based; negative means from the end, e.g. -1: last
     * 
     * @return a long consisting of latitude and longitude; use LongPoint to handle it.
     * @throws IllegalStateException if data missing
     */
//    public long getPoint(int segNo, int edgeNo, int pointNo)
//    {
//        segNo = (segNo + route.segmentsSize()) % route.segmentsSize();
//        Segment seg = segmentAt(segNo);
//        if (seg == null || seg.edgesSize() == 0)
//        {
//            throw new IllegalStateException("data missing");
//        }
//        edgeNo = (edgeNo + seg.edgesSize()) % seg.edgesSize();
//        RouteEdge edge = seg.getEdge(edgeNo);
//        if (edge == null || edge.numPoints() == 0)
//        {
//            throw new IllegalStateException("data missing");
//        }
//        pointNo = (pointNo + edge.numPoints()) % edge.numPoints();
//
//        return LongPoint.fromInts(edge.latAt(pointNo), edge.lonAt(pointNo));
//    }

    protected Segment segmentAt(int index)
    {
        if (index < 0 || index >= route.segmentsSize())
            return null;
        return route.segmentAt(index);
    }

    // public void provideSegment(int segmentNo, IModuleCallback callback)
    // {
    // if (!segmentDataPresent(segmentNo))
    // {
    // routeProvider.requestRouteEdge(callback, true, segmentNo);
    // }
    // }

    public void provideSegment()
    {
        provideSegment(false, false);
    }

    public void provideSegment(boolean isForce, boolean isCheckInScreen)
    {
        int segFrom = Math.max(0, getCurrentSegmentIndex() - LOOKBACK_SEGMENTS);
        int segTo = Math.min(route.segmentsSize() - 1, getCurrentSegmentIndex() + LOOKAHEAD_SEGMENTS);
//        int segTo = route.segmentsSize() - 1;
        boolean isNeedSend = false;
        if(isCheckInScreen) 
        {
            for (int i = lastSegmentIndexWithEdge; i < this.getRouteItemSize(); i++)
            {
                if (!segmentDataPresent(i))
                {
                    isNeedSend = true;
                    segFrom = Math.max(0, i - LOOKBACK_SEGMENTS);
                    segTo = Math.min(route.segmentsSize() - 1, i + LOOKAHEAD_SEGMENTS);
                    lastSegmentIndexWithEdge = segTo-1;
                    break;
                }
            }
        }
        else
        {
            for (int i = segFrom; i <= segTo; i++)
            {
                if (!segmentDataPresent(i))
                {
                    if(i > lastSegmentIndexWithEdge)
                        lastSegmentIndexWithEdge = i;
                    isNeedSend = true;
                    break;
                }
            }
        }
        if (isNeedSend)
        {
            requestRouteEdge(true, segFrom, segTo);
        }
        else if (isForce)
        {
            requestRouteEdge(true, segFrom);
        }
    }

    public boolean requestRouteEdge(boolean immediately, int startSegId)
    {
        return requestRouteEdge(immediately, startSegId, -1);
    }

    public boolean requestRouteEdge(boolean immediately, int startSegId, int endSegID)
    {
        if (isRouteRequestAllowed())
        {
            // can not use getSelectedRoute() here as it possiblly generated locally
            Route route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
//            int routeId = route.getRouteID();

            int startSegIndex = findStartSegmentIndex(route, startSegId - 1);
            if (startSegIndex >= route.getSegments().length)
            {
                return false;
            }

            long currTime = System.currentTimeMillis();
            if (currTime - lastEdgeRequestTime < 25000 && !immediately)
            {
                return true;
            }
            lastEdgeRequestTime = currTime;

//            int endSegIndex = findLastSegmentIndex(route, startSegIndex + 1);
//            if (endSegID > 0 && endSegID > endSegIndex)
//            {
//                endSegIndex = endSegID;
//            }
            //TODO
//            navigationProxy.requestRouteEdge(routeId, startSegIndex, endSegIndex);
            return true;
        }

        return true;
    }

    private boolean isRouteRequestAllowed()
    {
        if (isOutOfCoverage())
            return false;

        // if (pathTreeManager.isLocalRerouteEnabled())
        // {
        // // check whether currently using stored route data to navigate
        // IRoute[] nominalRoutes = pathTreeManager.getNominalRoutes();
        // return nominalRoutes != null &&
        // nominalRoutes.length > 0 &&
        // nominalRoutes[0]!= null &&
        // ! nominalRoutes[0].isLocallyGenerated();
        // }

        return true;
    }

    private boolean isOutOfCoverage()
    {
        return !NetworkStatusManager.getInstance().isConnected();
    }

    protected int findStartSegmentIndex(Route route, int i)
    {
        if (i < 0)
            i = 0;
        for (; i < route.getSegments().length; i++)
        {
            if (!route.getSegments()[i].isEdgeResolved())
            {
                break;
            }
        }

        return i;
    }

    protected int findLastSegmentIndex(Route route, int startIndex)
    {
        Segment[] segments = route.getSegments();
        int dist = 0;

        int i;
        for (i = startIndex; i < segments.length; i++)
        {
            dist += segments[i].getLength();

            if ((segments[i].isEdgeResolved() && i > startIndex) || dist > MAX_ROUTE_EDGE_REQUEST_LEN)
            {
                break;
            }
        }

        return i;
    }
    
    public void cancelRequest()
    {
        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_GET_EDGE);
    }
}

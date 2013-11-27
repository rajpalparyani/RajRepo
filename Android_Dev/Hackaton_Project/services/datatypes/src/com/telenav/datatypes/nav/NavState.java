package com.telenav.datatypes.nav;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;

public class NavState
{
    /** current position */
    protected TnNavLocation position;
    
    /** current route segment index */
    protected int segmentIndex;
    
    /** edge index within the current segment */
    protected int edgeIndex;
    
    /** shape point index within the current edge */
    protected int pointIndex;
    
    /** distance from the current point */
    protected int range;
    
    /** route reference */
    protected int routeId = RouteWrapper.ROUTE_NONE;
    
    /** see if we're walking off end of the route */
    protected boolean walkingOffEnd = false;
    
    protected NavState(int routeId)
    {
        this.position = new TnNavLocation("");;
        this.routeId = routeId;
    }
    
    protected NavState(int routeId, int segIndex, int edgeIndex, int shapePointIndex, int range)
    {
        this(routeId);
        
        this.segmentIndex = segIndex;
        this.edgeIndex = edgeIndex;
        this.pointIndex = shapePointIndex;
        this.range = range;
        
        adjustNavState();
        
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        calcPositionFromIndexes(route);
    }
    
    protected void adjustNavState()
    {
        if (this.routeId == RouteWrapper.ROUTE_NONE)
            return;
        
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        if (route == null)
        {
            return;
        }
        
        if (segmentIndex < 0)
        {
            resetIndexesToHead();
            return;
        }
        
        if (segmentIndex >= route.segmentsSize())
        {
            resetIndexesToTail(route);
            return;
        }
        
        boolean isReseted = adjustEdgeIndex(route);
        if (isReseted)
            return;
        
        isReseted = adjustPointIndex(route);
        if (isReseted)
            return;
        
        adjustRange(route);
    }
    
    private boolean adjustEdgeIndex(Route route)
    {
        while (edgeIndex < 0)
        {
            segmentIndex --;
            if (segmentIndex < 0)
            {
                resetIndexesToHead();
                return true;
            }
            Segment seg = route.segmentAt(segmentIndex);
            edgeIndex += seg.edgesSize();
        }
        
        Segment currentSegment = route.segmentAt(segmentIndex);
        while(edgeIndex >= currentSegment.edgesSize())
        {
            edgeIndex -= currentSegment.edgesSize();
            segmentIndex ++;
            if (segmentIndex >= route.segmentsSize())
            {
                resetIndexesToTail(route);
                return true;
            }
            currentSegment = route.segmentAt(segmentIndex);
        }
        
        return false;
    }
    
    private boolean adjustPointIndex(Route route)
    {
        while (pointIndex < 0)
        {
            edgeIndex --;
            if (edgeIndex < 0)
            {
                boolean isReseted = adjustEdgeIndex(route);
                if (isReseted)
                    return true;
            }
            RouteEdge edge = getCurrentSegment().getEdge(edgeIndex);
            if (edge == null)
                return false;
            pointIndex += edge.numPoints();
            if (pointIndex == edge.numPoints() - 1)
                pointIndex = edge.numPoints() - 2;
        }
        RouteEdge currentEdge = getCurrentSegment().getEdge(edgeIndex);
        if (currentEdge == null)
            return false;

        while(pointIndex >= currentEdge.numPoints() - 1)
        {
            if (pointIndex == currentEdge.numPoints() - 1)
            {
                pointIndex = 0;
            }
            else
            {
                pointIndex -= currentEdge.numPoints();
            }
            edgeIndex ++;
            if (edgeIndex >= getCurrentSegment().edgesSize())
            {
                boolean isReset = adjustEdgeIndex(route);
                if (isReset)
                    return true;
            }
            currentEdge = getCurrentSegment().getEdge(edgeIndex);
            if (currentEdge == null)
                return false;
        }
        
        return false;
    }
    
    private boolean adjustRange(Route route)
    {
        while (range < 0)
        {
            pointIndex --;
            if (pointIndex < 0)
            {
                boolean isReseted = adjustPointIndex(route);
                if (isReseted)
                    return true;
            }
            RouteEdge edge = getCurrentSegment().getEdge(edgeIndex);
            if (edge == null)
                return false;
            range += edge.getDeltaRs(pointIndex);
        }
        
        RouteEdge currentEdge = getCurrentSegment().getEdge(edgeIndex);
        if (currentEdge == null)
            return false;
        
        int deltaR = currentEdge.getDeltaRs(pointIndex);
        while(range >= deltaR)
        {
            range -= deltaR;
            pointIndex ++;
            if (pointIndex >= currentEdge.numPoints() - 1)
            {
                boolean isReset = adjustPointIndex(route);
                if (isReset)
                    return true;
            }
            currentEdge = getCurrentSegment().getEdge(edgeIndex);
            if (currentEdge == null)
                return false;
            deltaR = currentEdge.getDeltaRs(pointIndex);
        }
        
        return false;
    }
    
    protected void resetIndexesToHead()
    {
        segmentIndex = 0;
        edgeIndex = 0;
        pointIndex = 0;
        range = 0;
    }
    
    protected void resetIndexesToTail(Route route)
    {
        segmentIndex = route.segmentsSize() - 1;
        Segment seg = route.segmentAt(segmentIndex);
        edgeIndex = seg.edgesSize() - 1;
        RouteEdge edge = seg.getEdge(edgeIndex);
        if (edge == null)
            return;
        pointIndex = edge.numPoints() - 1;
        range = 0;
    }
    
    protected boolean calcPositionFromIndexes(Route route)
    {
        if (route == null)
            return false;
        
        if (segmentIndex < 0 || segmentIndex >= route.segmentsSize())
            return false;
        
        Segment currentSeg = route.segmentAt(segmentIndex);
        if (edgeIndex < 0 || edgeIndex >= currentSeg.edgesSize())
            return false;
        
        RouteEdge currentEdge = currentSeg.getEdge(edgeIndex);
        if (pointIndex < 0 || currentEdge == null || pointIndex >= currentEdge.numPoints() - 1)
            return false;

        int currentDeltaR = currentEdge.getDeltaRs(pointIndex);
        if (range < 0 || range >= currentDeltaR)
            return false;
        
        int lat1 = currentEdge.latAt(pointIndex);
        int lon1 = currentEdge.lonAt(pointIndex);
        
        int lat2 = currentEdge.latAt(pointIndex+1);
        int lon2 = currentEdge.lonAt(pointIndex+1);
        
        if (range == 0)
        {
            position.setLatitude(lat1);
            position.setLongitude(lon1);
        }
        else
        {
            int lat = (lat2 - lat1) * range / currentDeltaR + lat1;
            int lon = (lon2 - lon1) * range / currentDeltaR + lon1;
            position.setLatitude(lat);
            position.setLongitude(lon);
        }
        
        int heading = DataUtil.bearing(lat1, lon1, lat2, lon2);
        position.setHeading(heading);
        return true;
    }
    
    public Segment getCurrentSegment()
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        if (route == null)
        {
            return null;
        }
        return route.segmentAt(segmentIndex);
    }

    public boolean haveCurrentData()
    {
        Segment segment = getCurrentSegment();
        return segment != null && segment.isEdgeResolved();
    }

    public RouteEdge getCurrentEdge()
    {
        Segment segment = getCurrentSegment();
        return segment == null ? null : segment.getEdge(edgeIndex);
    }

    public int getCurrentShapePointLat()
    {
        RouteEdge edge = getCurrentEdge();
        return edge == null ? 0 : edge.latAt(pointIndex);
    }

    public int getCurrentShapePointLon()
    {
        RouteEdge edge = getCurrentEdge();
        return edge == null ? 0 : edge.lonAt(pointIndex);
    }

    public int getCurrentRange()
    {
        return range;
    }

    public void set(int segmentIndex, int edgeIndex, int shapeIndex, int range)
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        this.segmentIndex = segmentIndex;
        this.edgeIndex = edgeIndex;
        this.pointIndex = shapeIndex;
        this.range = range;
        
        adjustNavState();
        calcPositionFromIndexes(route);
    }
    
    public void set(int segmentIndex)
    {
        this.segmentIndex = segmentIndex < 0 ? 0 : segmentIndex;
    }
    
    public void setTimeStamp(long timeStamp)
    {
        this.position.setTime(timeStamp);
    }

    public int nextLat()
    {
        return nextP(true);
    }

    public int nextLon()
    {
        return nextP(false);
    }
    
    private int nextP(boolean isLat)
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        if (route == null)
        {
            return 0;
        }
        
        adjustNavState();
        int tempShapeIndex = pointIndex + 1;
        int tempEdgeIndex = edgeIndex;
        int tempRouteIndex = segmentIndex;
        
        RouteEdge currentEdge = getCurrentEdge();
        if (currentEdge == null)
        {
            return 0;
        } 
        
        RouteEdge edge = route.segmentAt(tempRouteIndex).getEdge(tempEdgeIndex);
        if (edge == null) 
            return 0;

        if (tempShapeIndex >= edge.numPoints())
        {
            //there is something wrong, the tempShapeIndex should not be out of bound 
            //because pointIndex will never point to the last shape point of the edge
            return 0;
        }
        
        if (isLat)
        {
            return edge.latAt(tempShapeIndex);
        }
        else
        {
            return edge.lonAt(tempShapeIndex);
        }
    }


    //the prevP has problem, it doesn't adjust the tempShapeIndex if it's < 0
    //fix it first when you want to use these APIs
//    public int prevLat()
//    {
//        return prevP(true);
//    }
//
//    public int prevLon()
//    {
//        return prevP(false);
//    }
//
//    private int prevP(boolean isLat)
//    {
//        Route route = RouteWrapper.getInstance().getRoute(routeId);
//        
//        adjustNavState();
//        int tempShapeIndex = pointIndex - 1;
//        int tempEdgeIndex = edgeIndex;
//        int tempRouteIndex = segmentIndex;
//        
//        RouteEdge edge = route.segmentAt(tempRouteIndex).getEdge(tempEdgeIndex);
//        if (edge == null) 
//            return 0;
//
//        if (isLat)
//        {
//            return edge.latAt(tempShapeIndex);
//        }
//        else
//        {
//            return edge.lonAt(tempShapeIndex);
//        }
//    }
    
    public void walkNextShape()
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        pointIndex++;
        adjustNavState();
        calcPositionFromIndexes(route);
    }

    public void walkPrevShape()
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        pointIndex--;
        adjustNavState();
        calcPositionFromIndexes(route);
    }

    public boolean isAtTheStartOfRoute()
    {
        return (segmentIndex == 0) &&
                (edgeIndex == 0) &&
                (pointIndex == 0);
    }
    
    public boolean isAtTheEndOfRoute()
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        return 
            route != null                    && segmentIndex == route.segmentsSize() - 1 &&
            getCurrentSegment() != null && edgeIndex == getCurrentSegment().edgesSize() - 1 &&
            getCurrentEdge() != null         && pointIndex == getCurrentEdge().numPoints() - 1;
    }

    public boolean walkRouteFromCurrentShapePoint()
    {
        return walkRouteFromCurrentShapePoint(range);
    }
  
    public boolean walkRouteFromCurrentShapePoint(int distance)
    {       
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        if (!haveCurrentData())
        {
            return false;
        }
        walkingOffEnd = false;
        
        this.range = distance;
        adjustNavState();
        calcPositionFromIndexes(route);
        
        return true;
    }
    

    public int getDeltaR(int segIndex, int edgeIndex, int pointIndex)
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        RouteEdge edge = route.segmentAt(segIndex).getEdge(edgeIndex);
        return edge == null ? 0 : edge.getDeltaRs(pointIndex);
    }

    public int getCurrDeltaR()
    {
        RouteEdge edge = getCurrentEdge();
        return edge == null ? 0 : edge.getDeltaRs(pointIndex);
    }

    public int calcDistanceFromHead()
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        if (route == null)
        {
            return 0;
        }
        
        int dist = 0;
        // accumulate segment distances
        for (int i = 0; i < segmentIndex; ++i)
        {
            dist += route.segmentAt(i).getCalculatedLength();
        }
            
        // accumulate point distances
        
        Segment seg = route.segmentAt(segmentIndex); 
        for (int i = 0; i < edgeIndex; ++i)
        {
            RouteEdge edge = seg.getEdge(i);
            if (edge == null) continue;
            
            dist += edge.getLength();
        }
        
        for (int j = 0; j < pointIndex; j++)
        {
            dist += getDeltaR(segmentIndex, edgeIndex, j);
        }
        
        return dist + range;
    }

    public int getDistanceToTurn()
    {
        Segment seg = getCurrentSegment();
        
        int distToTurn = 0;
        
        //for the current edge, iterate from current point to the end of the edge
        for (int i = pointIndex; i < getCurrentEdge().numPoints(); i++)
        {
            int nSegLength = getDeltaR(segmentIndex, edgeIndex, i);
            distToTurn += nSegLength;
        }
        
        //then, iterate through ALL points for the rest of the segments
        //if this is not the last edge
        for (int i = edgeIndex + 1; i < seg.edgesSize(); i++)
        {
            RouteEdge edge = seg.getEdge(i);
            if (edge == null) continue;

            int segPointLen = edge.numPoints();
            for(int j = 0; j < segPointLen; j++)
            {
                int nSegLength = getDeltaR(segmentIndex, i, j);
                distToTurn += nSegLength;
            }
        }
        
        return distToTurn;
    }

    public int getEdgeIndex()
    {
        return edgeIndex;
    }

    public int getPointIndex()
    {
        return pointIndex;
    }

    public TnNavLocation getPosition()
    {
        return this.position;
    }

    public int getRange()
    {
        return this.range;
    }

    public void setRange(int range)
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        this.range = range;
        adjustNavState();
        calcPositionFromIndexes(route);
    }

    public int getRoute()
    {
        return this.routeId;
    }

    public void setRoute(int routeId)
    {
        this.routeId = routeId;
    }

    public int getSegmentIndex()
    {
        return segmentIndex;
    }

    public boolean isWalkingOffEnd()
    {
        return walkingOffEnd;
    }

    public void set(NavState copyFrom) 
    {
        setRoute(copyFrom.getRoute());
        
        set(copyFrom.getSegmentIndex(),
                copyFrom.getEdgeIndex(),
                copyFrom.getPointIndex(),
                copyFrom.getCurrentRange());
        
        setTimeStamp(copyFrom.getPosition().getTime());
        
        if (copyFrom.getPosition() != null)
        {
            position.set(copyFrom.getPosition());
        }
    }
    
    /**
     * Calculate the distance from current pos in onRoute to target point
     * @Param  targetPointIndex the target point index
     * @Param  edgeID the edge ID of the target point
     * @param  maxRange the limitation of the distance
     */
    public int getDistanceToPoint(int targetPointIndex, int edgeID, int maxRange)
    {
        int distance = 0;
        
        boolean isTargetPointPassed = true;

        Route route = RouteWrapper.getInstance().getRoute(routeId);

        distance = -range;

        RouteEdge routeEdge = getCurrentEdge();

        if (routeEdge == null || targetPointIndex < 0)
        {
            return -1;
        }
        
        //calc the distance between current onRoute pos and incident position
        if(routeEdge.getID() == edgeID)
        {
            isTargetPointPassed = false;
            for (int i = pointIndex; i < targetPointIndex; i++)
            {
                int nSegLength = getDeltaR(segmentIndex, edgeIndex, i);
                distance += nSegLength;
            }
            
            return distance <= 0 || distance > maxRange? -1 : distance;
        }
        
        //calc the distance between current onRoute Pos to the end of this edge
        for (int i = pointIndex; i < routeEdge.numPoints(); i++)
        {
            int nSegLength = getDeltaR(segmentIndex, edgeIndex, i);
            distance += nSegLength;
        }
        
        for (int i = segmentIndex; i < route.segmentsSize(); i++)
        {
            Segment seg = route.segmentAt(i);

            int edgeStartIndex = 0;
            if (i == segmentIndex)
            {
                edgeStartIndex = edgeIndex + 1;
            }
            
            for (int j = edgeStartIndex; j < seg.edgesSize(); j++ )
            {
                RouteEdge edge = seg.getEdge(j);
                if (edge == null) continue;

                if (edge.getID() == edgeID)
                {
                    isTargetPointPassed = false;
                    for (int k = 0; k < targetPointIndex; k++)
                    {
                        int nSegLength = getDeltaR(i, j, k);
                        distance += nSegLength;
                    }
                    return distance <= 0 || distance > maxRange ? -1 : distance;
                }
                else
                {
                    distance += edge.getLength();
                }

                if (distance > maxRange)
                    return -1;
            } 
        }
        
        return -1;
    }

    public int getDistanceToEdge(int edgeID, int maxRange) 
    {
        Route route = RouteWrapper.getInstance().getRoute(routeId);
        
        int distance = -range;

        RouteEdge routeEdge = getCurrentEdge();
        
        if (routeEdge == null)
        {
            return -1;
        }
        
        for (int i = pointIndex; i < routeEdge.numPoints(); i++)
        {
            int nSegLength = getDeltaR(segmentIndex, edgeIndex, i);
            distance += nSegLength;
        }

        for (int i = segmentIndex; i < route.segmentsSize(); i++)
        {
            Segment seg = route.segmentAt(i);

            int edgeStartIndex = 0;
            if (i == segmentIndex)
            {
                edgeStartIndex = edgeIndex + 1;
            }
            
            if(routeEdge.getID() == edgeID)//we should check that if the edge id is current edge
            {
                if (distance > maxRange)
                    return -1;
                
                return distance;
            }
            
            for (int j = edgeStartIndex; j < seg.edgesSize(); j++ )
            {
                RouteEdge edge = seg.getEdge(j);
                if (edge == null) continue;


                if (edge.getID() == edgeID)
                {
                    distance += edge.getLength() >> 1;
                    return distance;
                }
                else
                {
                    distance += edge.getLength();
                }

                if (distance > maxRange)
                    return -1;
            } 
        }
        return -1;
    }
}

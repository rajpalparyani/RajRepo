package com.telenav.nav.spt;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.nav.NavUtil;
import com.telenav.datatypes.route.PathTreeEdge;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteDataFactory;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.datatypes.route.SptTree;
import com.telenav.location.TnLocation;

import java.util.Vector;

class SptRouteGenerator
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    static int POINT_CLOSE_THESHOULD          =   12;
    
    static int MULTI_ROUTE_DIST_THRESHOLD     =  100; 
         
    static int SAME_POINT_THRESHOLD           =    6;
    
    // used to fake unique edge ID
    int edgeID  = -10000;
    
    // used to fake unique route ID
    int routeID =  -5000;
    
    private static SptRouteGenerator instance = new SptRouteGenerator();
    
    private SptRouteGenerator()
    {
    }  
    
    static SptRouteGenerator getInstance()
    {
        return instance;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    Route[] generateRoutes(TnLocation[] fixes, PathTreeEdge snappedTreeEdge, 
    		               SptManager pathTreeManager,NavUtil navUtil)
    {
        try
        {
        
            if (snappedTreeEdge == null)
                return null;
            
            if (fixes != null && fixes.length > 0)
            {
                int gpsLat = fixes[fixes.length - 1].getLatitude();
                int gpsLon = fixes[fixes.length - 1].getLongitude();
                
                int[] points = snappedTreeEdge.getShapePoints();
                if (points != null && points.length >= 4)
                {
                    int endLat = points[points.length - 2 + DataUtil.LAT];
                    int endLon = points[points.length - 2 + DataUtil.LON];
                    
                    int cosLat = DataUtil.getCosLat(gpsLat);
                    int dist = DataUtil.gpsDistance(endLat - gpsLat, endLon - gpsLon, cosLat);
            
                    // if distance to next turn is too close, user probably has no time to response
                    if (dist < MULTI_ROUTE_DIST_THRESHOLD)
                    {
                        // FIXME: is this logic always correct or not ?
                        // looking for connecting street with the same street name
                        PathTreeEdge nextEdge = lookForNextConnectingEdge(snappedTreeEdge, pathTreeManager);
                        if (nextEdge != null && nextEdge.getTurnType() == snappedTreeEdge.getTurnType())
                        {
                            byte turnType = snappedTreeEdge.getTurnType();
                            int parentIndex = ((PathTreeEdge)snappedTreeEdge).getParentIndex();  
                            
                            int nextEdgeIndex = -1;
                            Vector pathTrees = pathTreeManager.getSptTrees();
                            for (int k = 0; k < pathTrees.size(); k ++)
                            {
                                SptTree tree = (SptTree)pathTrees.elementAt(k);
                                if (tree == null || tree.getPathTreeEdges(false) == null) 
                                    continue;
                                    
                                nextEdgeIndex = tree.getPathTreeEdges(false).indexOf(nextEdge);
                                if (nextEdgeIndex != -1) break;    
                            } 
                            
                            if (nextEdgeIndex != -1)
                            {
                                snappedTreeEdge.setParentIndex(nextEdgeIndex);
                                snappedTreeEdge.setTurnType(Route.TURN_TYPE_CONTINUE);
                                Route[] routes = generateRoutes(snappedTreeEdge,pathTreeManager,navUtil);
                                
                                snappedTreeEdge.setTurnType(turnType);
                                snappedTreeEdge.setParentIndex(parentIndex);
                                
                                if (routes != null) return routes;  
                            }
                                            
                        }
                    }//end if 
                    
                }// end if 
            }// end if 
            
            return generateRoutes(snappedTreeEdge,pathTreeManager,navUtil);
            
        }
        catch (Exception ex)
        {
            // possiblly null pointer exception somewhere, but don't know the exact place yet
             return null;
        }    
    } 
    
    private Route[] generateRoutes(PathTreeEdge snappedTreeEdge, SptManager pathTreeManager,
                                   NavUtil navUtil)
    {
        Route route = generateRoute(snappedTreeEdge, pathTreeManager,navUtil);
        if (route == null) return null;
        
        Route[] routes = new Route[1];
        routes[0] = route;
             
        return routes;            
    }                  
    
    private PathTreeEdge lookForNextConnectingEdge(PathTreeEdge snappedTreeEdge, 
                                                   SptManager pathTreeManager)
    {
        int[] edgePoints = snappedTreeEdge.getShapePoints();
        int endLat = edgePoints[edgePoints.length - 2 + DataUtil.LAT];
        int endLon = edgePoints[edgePoints.length - 2 + DataUtil.LON];
        int startLat = edgePoints[DataUtil.LAT];
        int startLon = edgePoints[DataUtil.LON];
        int snappedEdgeBearing = DataUtil.bearing(startLat,startLon,endLat,endLon);
        
        PathTreeEdge connectingEdge = null;
        
        Vector pathTrees = pathTreeManager.getSptTrees();
        for (int i = 0; i < pathTrees.size(); i++)
        {
            SptTree pathTree = (SptTree)pathTrees.elementAt(i);
            
            // filter using bounding box of whole path tree
            int[] bb = pathTree.getBoundingBox();
            if (bb == null || 
                bb[DataUtil.LAT]     >= endLat + POINT_CLOSE_THESHOULD || 
                bb[DataUtil.LAT + 2] <= endLat - POINT_CLOSE_THESHOULD || 
                bb[DataUtil.LON]     >= endLon + POINT_CLOSE_THESHOULD || 
                bb[DataUtil.LON + 2] <= endLon - POINT_CLOSE_THESHOULD )
            {    
                continue;
            }   
            
            Vector pathTreeEdges = pathTree.getPathTreeEdges(true);
            if (pathTreeEdges == null) continue;
            
            for (int j = 0; j < pathTreeEdges.size(); j++)
            {
                PathTreeEdge edge = (PathTreeEdge)pathTreeEdges.elementAt(j);
                
                int[] points = edge.getShapePoints();
                if (points == null || points.length < 4) continue;
                
                int firstLat = points[DataUtil.LAT];
                int firstLon = points[DataUtil.LON];
                
                if (isSamePoint(firstLat,firstLon,endLat,endLon))
                {
                    int lastLat = points[points.length - 2 + DataUtil.LAT];
                    int lastLon = points[points.length - 2 + DataUtil.LON];
                    int edgeBearing = DataUtil.bearing(firstLat,firstLon,lastLat,lastLon);
                    
                    // don't need to add the same edge but different direction
                    if ( !isSamePoint(lastLat,lastLon,startLat,startLon) && 
                         edge.getStreetName() != null && 
                         snappedTreeEdge.getStreetName() != null &&
                         edge.getStreetName().equals(snappedTreeEdge.getStreetName()) &&
                         edgeBearing - snappedEdgeBearing <  45 && 
                         snappedEdgeBearing - edgeBearing > - 45)
                    {
                         connectingEdge = edge;
                    } 
                }     
            }//end for   
        }// end for 
        
        // let the same edge in the later SPT tree replace the previous one
        return connectingEdge;
    }
    
    private boolean isSamePoint(int lat1, int lon1, int lat2, int lon2)
    {
        return lat1 - lat2 < SAME_POINT_THRESHOLD && 
               lat2 - lat1 < SAME_POINT_THRESHOLD && 
               lon1 - lon2 < SAME_POINT_THRESHOLD && 
               lon2 - lon1 < SAME_POINT_THRESHOLD ;
    }
    
    private Route generateRoute(PathTreeEdge snappedTreeEdge, SptManager pathTreeManager,
                                NavUtil navUtil)
    {
        if (snappedTreeEdge == null) return null;
        
        Vector pathTrees = pathTreeManager.getSptTrees();
        if (pathTrees == null) return null;
        
        for (int k=0; k< pathTrees.size(); k++)
        {
            SptTree tree = (SptTree)pathTrees.elementAt(k);
            if (tree == null) continue;
                 
            Vector edges = tree.getPathTreeEdges(false);
            if (edges == null) continue;
            
            int index = edges.indexOf(snappedTreeEdge);
            if (index == -1 ) continue; 
            
            Vector routeEdges = tree.generateRouteEdges(index);
            Route route = generateRouteByTreeEdges(routeEdges,pathTreeManager,navUtil);
            
            if (route != null)
            {
                return route;
            }            
        }
        
        return null;
    }
    
    private Route generateRouteByTreeEdges(Vector routeTreeEdges, SptManager pathTreeManager,
                                           NavUtil navUtil)
    {
        if (routeTreeEdges == null || routeTreeEdges.size() <= 0) return null;
        
        int nominalRouteStartEdgeIndex = -1;
        PathTreeEdge nominalRouteStartEdge = null;
        int[] matchResults = null;
        
        for (int i=0; i< routeTreeEdges.size(); i++)
        {
            PathTreeEdge edge = (PathTreeEdge)routeTreeEdges.elementAt(i);
            if (edge.isOnNominalRoute() && nominalRouteStartEdgeIndex == -1)
            {
                // find route index, segment index and edge index of matched route edge
            	Route[] nominalRoutes = getNominalRoutes();
            	
                matchResults = matchTreeEdgeOnNominalRoutes(edge,nominalRoutes);    
                if (matchResults != null)
                {
                	nominalRouteStartEdgeIndex = i;
                    nominalRouteStartEdge = edge;
                    
                    break;
                }    
            }//end if 
        }//end for
        
        if (matchResults == null)
        {
            if (nominalRouteStartEdgeIndex != -1 && nominalRouteStartEdgeIndex != routeTreeEdges.size() - 1)
            {
                return null;
            }
            else
            {
                // destination probably in the middle of the last SPT edge
                // so client need to cut the last SPT edge and make the end point is destination
                Segment lastSeg = getLastSegment(pathTreeManager);
                RouteEdge lastEdge = getLastEdge(pathTreeManager);
                if (lastEdge == null || lastEdge.numPoints() < 2) return null;
                
                PathTreeEdge lastSPTEdge = (PathTreeEdge)routeTreeEdges.elementAt(routeTreeEdges.size() - 1);
                int[] points =  lastSPTEdge.getShapePoints();
                if (points == null || points.length < 4) return null;
                
                int startSPTLat = points[DataUtil.LAT];
                int startSPTLon = points[DataUtil.LON];
                int endSPTLat = points[DataUtil.LAT + points.length - 2];
                int endSPTLon = points[DataUtil.LON + points.length - 2];
                
                int endLat   = lastEdge.latAt(lastEdge.numPoints() - 1);
                int endLon   = lastEdge.lonAt(lastEdge.numPoints() - 1);
                
                int startSPTPointMatchIndex = -1;
                int endSPTPointMatchIndex = - 1;
                for (int i = 0; i < lastEdge.numPoints() ; i++)
                {
                    if (startSPTLat == lastEdge.latAt(i) && startSPTLon == lastEdge.lonAt(i))
                    {
                        startSPTPointMatchIndex = i;
                    } 
                    else if (endSPTLat == lastEdge.latAt(i) && endSPTLon == lastEdge.lonAt(i))
                    {
                        endSPTPointMatchIndex = i;
                    }
                }
                
                if (startSPTPointMatchIndex != -1) 
                {
                    routeTreeEdges.removeElement(lastSPTEdge);
                    PathTreeEdge cloneSPTTreeEdge = cloneSPTTreeEdge(lastSPTEdge);
                    
                    int length = lastEdge.numPoints() - startSPTPointMatchIndex; 
                    int[] shapePoints = new int[length << 1];
                    for (int i = 0; i < length ; i ++)
                    {
                        shapePoints[DataUtil.LAT + (i << 1)] = lastEdge.latAt(i + startSPTPointMatchIndex);
                        shapePoints[DataUtil.LON + (i << 1)] = lastEdge.lonAt(i + startSPTPointMatchIndex);
                    }
                    
                    cloneSPTTreeEdge.setShapePoints(shapePoints);
                    cloneSPTTreeEdge.setTurnType(lastSeg.getTurnType());
                    routeTreeEdges.addElement(cloneSPTTreeEdge);
                }
                else if (endSPTPointMatchIndex != -1)
                {
                    routeTreeEdges.removeElement(lastSPTEdge);
                    if (lastSPTEdge.getPointSize() < lastEdge.numPoints() - endSPTPointMatchIndex) 
                        return null;
                    
                    int i = endSPTPointMatchIndex;
                    for (; i < lastEdge.numPoints() - 1; i ++)
                    {
                        int lat = lastEdge.latAt(i);
                        int lon = lastEdge.lonAt(i);
                        
                        if (lat != points[DataUtil.LAT + points.length - 2 - ( i - endSPTPointMatchIndex << 1)] ||
                            lon != points[DataUtil.LON + points.length - 2 - ( i - endSPTPointMatchIndex << 1)])
                        {
                            break;
                        }    
                    }
                    
                    if ( i == lastEdge.numPoints() - 1)
                    {
                        PathTreeEdge cloneSPTTreeEdge = cloneSPTTreeEdge(lastSPTEdge);
                        
                        int length = lastSPTEdge.getPointSize() - (lastEdge.numPoints() - endSPTPointMatchIndex) + 2;
                        int[] shapePoints = new int [length << 1];
                        for (int k = 0; k < (length - 1) << 1; k ++)
                        {
                            shapePoints[k] = points[k];
                        }
                        
                        shapePoints[DataUtil.LAT + (length << 1) - 2] = endLat;
                        shapePoints[DataUtil.LON + (length << 1) - 2] = endLon;
                        
                        cloneSPTTreeEdge.setShapePoints(shapePoints);
                        
                        // destination should on the other side as coming from opposite direction
                        cloneSPTTreeEdge.setTurnType(lastSeg.getTurnType());
                        if (lastSeg.getTurnType() == Route.TURN_TYPE_DESTINATION_LEFT)
                            cloneSPTTreeEdge.setTurnType(Route.TURN_TYPE_DESTINATION_RIGHT);
                        else if (lastSeg.getTurnType() == Route.TURN_TYPE_DESTINATION_RIGHT)
                            cloneSPTTreeEdge.setTurnType(Route.TURN_TYPE_DESTINATION_LEFT);
                        
                        routeTreeEdges.addElement(cloneSPTTreeEdge);
                    }
                    else
                    {
                        // seems not be able to match
                        // in case More server divided the last edge into four pieces, just let it pass through 
                        if (endSPTLat != endLat || endSPTLon != endLon)
                            return null;
                    }
                }
                
            } // end if   
        }//end if 
        
        check4UTurn(routeTreeEdges,nominalRouteStartEdgeIndex,pathTreeManager);
        
        // some extra edges probably has been added to route edges after checking U-turns
        if (nominalRouteStartEdge != null)
            nominalRouteStartEdgeIndex = routeTreeEdges.indexOf(nominalRouteStartEdge);
        
        // merge SPT edges based on edge names
        Vector segments = mergeSPTEdges(routeTreeEdges,nominalRouteStartEdgeIndex);
        
        Route[] nominalRoutes = getNominalRoutes();
        Route route = buildRoute(segments,nominalRoutes,matchResults,navUtil);
        
        // sanity check
        if (route != null && ( route.getSegments() == null || route.segmentsSize() == 0))
            return null; 
        
        return route;  
    }
    
    private Route[] getNominalRoutes()
    {
    	Vector routes = RouteWrapper.getInstance().getNominalRoutes();
    	if (routes == null || routes.size() == 0) return null;
    	
    	Route[] nominalRoutes = new Route[routes.size()];
    	for (int m = 0; m < nominalRoutes.length; m++)
    	{
    		nominalRoutes[m] = (Route)routes.elementAt(m);
    	}
    	
    	return nominalRoutes;
    }
    
    private PathTreeEdge cloneSPTTreeEdge(PathTreeEdge rawEdge)
    {
        PathTreeEdge edge = new PathTreeEdge();
        edge.setBoundingBox(rawEdge.getBoundingBox());
        edge.setSpeedLimit(rawEdge.getSpeedLimit());
        edge.setEdgeType(rawEdge.getEdgeType());
        edge.setStreetName(rawEdge.getStreetName());
        edge.setTurnType(rawEdge.getTurnType());
        
        return edge;
    }
    
    private Segment getLastSegment(SptManager pathTreeManager)
    {
//        if (pathTreeManager != null && pathTreeManager.getNominalRoutes() != null && 
//               pathTreeManager.getNominalRoutes().length > 0 && 
//                  pathTreeManager.getNominalRoutes()[0] != null)
//        {
//            Route route = pathTreeManager.getNominalRoutes()[0];
//            if (route.segmentsSize() > 0)
//            {
//                return route.segmentAt(route.segmentsSize() - 1);
//            }  
//        }//end if
        
    	//FIXME
    	Route route = RouteWrapper.getInstance().getCurrentRoute();
    	if (route.segmentsSize() > 0)
        {
            return route.segmentAt(route.segmentsSize() - 1);
        }  
    	
        return null;
    }
    
    private RouteEdge getLastEdge(SptManager pathTreeManager)
    {
        Segment lastSeg = getLastSegment(pathTreeManager);
        if (lastSeg != null && lastSeg.edgesSize() > 0)
        {
            return lastSeg.getEdge(lastSeg.edgesSize() - 1);
        }
                 
        return null;
    }
    
    /**
     *  Fake one more segment for U-turn case to avoid false deviation 
     *  Nav star has the similar logic 
     *   
     * @param routeTreeEdges
     */
    private void check4UTurn(Vector routeTreeEdges,int nominalRouteStartEdgeIndex,
                             SptManager pathTreeManager)
    {
        SptUTurnHandler.check4UTurn(routeTreeEdges, nominalRouteStartEdgeIndex, 
                                    pathTreeManager);
    }
    
    private void resolveRouteReferences(Route route, int newSegmentSize,
                                        NavUtil navUtil)
    {
        if (route == null) return;
        
        int[] thresholdRules = navUtil.getThresholdRules();
        
        Segment[] segments = route.getSegments();
        for (int i = 0; i < newSegmentSize; ++i)
        {
            Segment seg = (Segment)segments[i];
            seg.resolveEdgeReferences(i==segments.length-1,thresholdRules, null);
        }
        
        //calc the distance from dest for each of the segments.
        int distToDest = 0;
        for (int i = segments.length - 1; i >= 0; i--)
        {
            distToDest += segments[i].getLength();
            segments[i].setDistanceToDest(distToDest);
        }
        
        //resolve ui lag thresholds
        for (int i = 1; i < newSegmentSize; i++)
        {
            segments[i].setInstructionLagDist(route.calculateUILagDist(segments[i], segments[i-1], thresholdRules));
        }

        //route walk back tolerance     
        route.walkBackTolerance = thresholdRules[Route.RULE_WALK_BACK_TOL];
    }
    
    private RouteEdge createRouteEdge(PathTreeEdge treeEdge)
    {
        RouteEdge edge = RouteDataFactory.getInstance().createRouteEdge();
        edge.setIsLocallyGenerated(true);
       
        // fake unique edge ID
        edge.setID(edgeID --);
        
        int[] bb = treeEdge.getBoundingBox();

        edge.setShapePoints(treeEdge.getShapePoints());
        edge.setSpeedLimit(treeEdge.getSpeedLimit());

        // edge use its segment's street name as street name
        edge.setRoadType(treeEdge.getEdgeType());

        //FIXME: does edge still need ID here
        return edge;   
    }
    
    private Route buildRoute(Vector segments, Route[] nominalRoutes, 
    		                 int[] matchResults, NavUtil navUtil)
    {
        int routeMatchIndex       = -1;
        int segmentMatchIndex     = -1;
        int edgeMatchIndex        = -1;
        int pointMatchStartIndex  = -1;
        
        if (matchResults != null)
        {   
            routeMatchIndex       = matchResults[0];
            segmentMatchIndex     = matchResults[1];
            edgeMatchIndex        = matchResults[2];
            pointMatchStartIndex  = matchResults[3];
        }
         
        int segmentSize = segments.size();
        if (routeMatchIndex != -1)
        {    
            int routeSegSize = nominalRoutes[routeMatchIndex].segmentsSize(); 
            segmentSize = segments.size() - 1 + ( routeSegSize - segmentMatchIndex);   
        }
        
        Route route = RouteDataFactory.getInstance().createRoute();
        route.setIsLocallyGenerated(true);
        
        Segment[] routeSegments = new Segment[segmentSize];
        route.setSegments(routeSegments);
        
        int latRouteMin = Integer.MAX_VALUE;
        int latRouteMax = Integer.MIN_VALUE;
        int lonRouteMin = Integer.MAX_VALUE;
        int lonRouteMax = Integer.MIN_VALUE;
        
        int newSegmentSize = segments.size();
        for ( int segmentIndex = 0; segmentIndex < segmentSize; segmentIndex++)
        {
            if (segmentIndex >= segments.size())
            {
                int index = segmentMatchIndex + (segmentIndex - segments.size()) + 1;
                routeSegments[segmentIndex] = nominalRoutes[routeMatchIndex].segmentAt(index);
                if (routeSegments[segmentIndex].minLat() < latRouteMin) latRouteMin = routeSegments[segmentIndex].minLat();
                if (routeSegments[segmentIndex].maxLat() > latRouteMax) latRouteMax = routeSegments[segmentIndex].maxLat();
                if (routeSegments[segmentIndex].minLon() < lonRouteMin) lonRouteMin = routeSegments[segmentIndex].minLon();
                if (routeSegments[segmentIndex].maxLon() > lonRouteMax) lonRouteMax = routeSegments[segmentIndex].maxLon();
                continue;
            }
            
            Segment seg = RouteDataFactory.getInstance().createsSegment();
            
            int latSegmentMin = Integer.MAX_VALUE;
            int latSegmentMax = Integer.MIN_VALUE;
            int lonSegmentMin = Integer.MAX_VALUE;
            int lonSegmentMax = Integer.MIN_VALUE;
            
            // set necessary segment information
            routeSegments[segmentIndex] = seg;
            
            Vector segEdges  = (Vector)segments.elementAt(segmentIndex);
            if (segmentIndex == segments.size() - 1 && matchResults != null)
            {
                // merge SPT edges and route edges into one segment
                Segment matchedSeg = nominalRoutes[routeMatchIndex].segmentAt(segmentMatchIndex);
                seg.setSpeedLimit(matchedSeg.getSpeedLimit());
                seg.setStreetName(matchedSeg.getStreetName());
                seg.setStreetAlias(matchedSeg.getStreetAlias());
                seg.setTurnType(matchedSeg.getTurnType());
                
                int size = segEdges.size() - 1 + matchedSeg.edgesSize() - edgeMatchIndex;
                RouteEdge[] routeEdges = new RouteEdge[size];
                seg.setEdges(routeEdges);
                
                int [] edgeIDs = new int[routeEdges.length];
                seg.setEdgesId(edgeIDs);
                
                for (int j=0; j < segEdges.size() - 1; j++)
                {
                    PathTreeEdge treeEdge = (PathTreeEdge)segEdges.elementAt(j);
                    RouteEdge edge = createRouteEdge(treeEdge);
                    edge.setSegment(seg);
                    routeEdges[j] = edge;
                    edgeIDs[j] = edge.getID();
            
                    int[] bb = treeEdge.getBoundingBox();
                    if (bb[DataUtil.LAT] < latSegmentMin) latSegmentMin = bb[DataUtil.LAT];
                    if (bb[DataUtil.LAT + 2] > latSegmentMax) latSegmentMax = bb[DataUtil.LAT + 2];
                    if (bb[DataUtil.LON] < lonSegmentMin) lonSegmentMin = bb[DataUtil.LON];
                    if (bb[DataUtil.LON + 2] > lonSegmentMax) lonSegmentMax = bb[DataUtil.LON + 2];
                }
                
                PathTreeEdge treeEdge = (PathTreeEdge)segEdges.elementAt(segEdges.size() - 1);
                for (int j= edgeMatchIndex; j < matchedSeg.edgesSize(); j++)
                {
                    RouteEdge e = matchedSeg.getEdge(j);
                    int[] bb = e.getBoundingBox();
                    
                    if ( j == edgeMatchIndex && pointMatchStartIndex != 0)
                    {
                        RouteEdge edge = RouteDataFactory.getInstance().createRouteEdge();
                        edge.setIsLocallyGenerated(true);
                        edge.setID(edgeID--);
                        edge.setSegment(seg);
                        
                        int pointSize = e.numPoints() - pointMatchStartIndex;
                        
                        int[] shapePoints =  new int[ pointSize << 1];
                        for (int k=0; k < pointSize; k++)
                        {
                            shapePoints[(k<<1) + DataUtil.LAT] = e.latAt(k + pointMatchStartIndex);
                            shapePoints[(k<<1) + DataUtil.LON] = e.lonAt(k + pointMatchStartIndex);
                        }
                                                    
                        edge.setShapePoints(shapePoints);
                        edge.setSpeedLimit(e.getSpeedLimit());
                        edge.setSpeedLimitKph(e.getSpeedLimitKph());
                        edge.setSpeedLimitMph(e.getSpeedLimitMph());
                        edge.setRoadType(e.getRoadType());
                       
                        //FIXME: could be more precise here
                        edge.setBoundingBox(bb);
                        edgeIDs[segEdges.size() - 1 + j - edgeMatchIndex] = edge.getID();
                        routeEdges[segEdges.size() - 1 + j - edgeMatchIndex] = edge;
                    }
                    else
                    {                
                        edgeIDs[segEdges.size() - 1 + j - edgeMatchIndex] = e.getID();
                        routeEdges[segEdges.size() - 1 + j -edgeMatchIndex] = e;
                    }   
                    
                    if (bb != null)
                    {
                        if (bb[DataUtil.LAT] < latSegmentMin) latSegmentMin = bb[DataUtil.LAT];
                        if (bb[DataUtil.LAT] > latSegmentMax) latSegmentMax = bb[DataUtil.LAT];
                        if (bb[DataUtil.LON + 2] < lonSegmentMin) lonSegmentMin = bb[DataUtil.LON];
                        if (bb[DataUtil.LON + 2] > lonSegmentMax) lonSegmentMax = bb[DataUtil.LON];
                    }
                }//end for 
                
                //reuse street name audio
                seg.setSegmentAudioNode(matchedSeg.getSegmentAudioNode());
                     
                // need to change turn type if first edge in this segment is intersection
                if (segEdges.size() > 1)
                {
                    PathTreeEdge firstEdgeInSeg = (PathTreeEdge)segEdges.elementAt(0);
                    if (firstEdgeInSeg.getEdgeType() == DataUtil.INTERSECTION && segmentIndex > 0 &&
                        routeSegments[segmentIndex - 1].getTurnType() == Route.TURN_TYPE_CONTINUE)
                    {
                        routeSegments[segmentIndex - 1].setTurnType(firstEdgeInSeg.getTurnType());
                    }
                }
            }
            else
            {
                // generate a new segment based on SPT edges
                RouteEdge[] routeEdges = new RouteEdge[segEdges.size()];
                seg.setEdges(routeEdges);
                
                int [] edgeIDs = new int[segEdges.size()];
                seg.setEdgesId(edgeIDs);
                
                for (int j=0; j < segEdges.size(); j++)
                {
                    PathTreeEdge treeEdge = (PathTreeEdge)segEdges.elementAt(j);
                    RouteEdge edge = createRouteEdge(treeEdge);
                    edge.setSegment(seg);
                    routeEdges[j] = edge;
                    edgeIDs[j] = edge.getID();
                    
                    int[] bb = treeEdge.getBoundingBox();
                    edge.setBoundingBox(
                        new int[]{ bb[DataUtil.LAT], bb[DataUtil.LON],
                                   bb[DataUtil.LAT + 2], bb[DataUtil.LON + 2]});   
                    
                    if (bb[DataUtil.LAT] < latSegmentMin) latSegmentMin = bb[DataUtil.LAT];
                    if (bb[DataUtil.LAT + 2] > latSegmentMax) latSegmentMax = bb[DataUtil.LAT + 2];
                    if (bb[DataUtil.LON] < lonSegmentMin) lonSegmentMin = bb[DataUtil.LON];
                    if (bb[DataUtil.LON + 2] > lonSegmentMax) lonSegmentMax = bb[DataUtil.LON + 2];
                }
                
                PathTreeEdge lastEdgeInSeg = (PathTreeEdge)segEdges.elementAt(segEdges.size() -1);
                PathTreeEdge firstEdgeInSeg = (PathTreeEdge)segEdges.elementAt(0);

                if (firstEdgeInSeg.getEdgeType() == DataUtil.INTERSECTION && segEdges.size() > 1)
                {
                    PathTreeEdge secondEdge = (PathTreeEdge)segEdges.elementAt(1);
                    seg.setStreetName(secondEdge.getStreetName());
                }
                else
                {
                    seg.setStreetName(firstEdgeInSeg.getStreetName());
                }   
               
                // avoid set street name as null
                if (seg.getStreetName() == null)
                {
                    seg.setStreetName("");
                }
                else
                {
                	Route nominalRoute = nominalRoutes[0]; 
                
                	// try to reuse street name audio
                	if (routeMatchIndex >=0 && routeMatchIndex < nominalRoutes.length)
                        nominalRoute = nominalRoutes[routeMatchIndex];
                	
                    for (int m = 0; m < nominalRoute.segmentsSize(); m ++)
                    {
                        Segment nseg = nominalRoute.segmentAt(m);
                        if (nseg.getStreetName() != null && 
                            seg.getStreetName().equalsIgnoreCase(nseg.getStreetName()))
                        {
                            seg.setSegmentAudioNode(nseg.getSegmentAudioNode());
                            break;
                        }
                    }//end for
                }
                                    
                seg.setTurnType(lastEdgeInSeg.getTurnType());
                seg.setSpeedLimit(lastEdgeInSeg.getSpeedLimit());
               
                // need to change turn type if first edge in this segment is intersection
                if (firstEdgeInSeg.getEdgeType() == DataUtil.INTERSECTION && segmentIndex > 0 &&
                    routeSegments[segmentIndex - 1].getTurnType() == Route.TURN_TYPE_CONTINUE &&
                    segEdges.size() > 1)
                {
                    routeSegments[segmentIndex - 1].setTurnType(firstEdgeInSeg.getTurnType());
                }
            }

            seg.setBoundingBox(
                new int[]{latSegmentMin, lonSegmentMin,latSegmentMax, lonSegmentMax});
            
            if (latSegmentMin < latRouteMin) latRouteMin = latSegmentMin;
            if (latSegmentMax > latRouteMax) latRouteMax = latSegmentMax;
            if (lonSegmentMin < lonRouteMin) lonRouteMin = lonSegmentMin;
            if (lonSegmentMax > lonRouteMax) lonRouteMax = lonSegmentMax;

        }//end for
        
        route.setBoundingBox(
            new int[]{latRouteMin, lonRouteMin, latRouteMax, lonRouteMax});
        
        route.setRouteID(routeID--);
        resolveRouteReferences((Route)route,newSegmentSize,navUtil);
        
        // copy destination audio as destination is not changed
        if (nominalRoutes != null && nominalRoutes.length > 0)
        {
            route.setDestinationAudio(nominalRoutes[0].getDestinationAudio());
        }
        
        return route;
    }
    
    private Vector mergeSPTEdges(Vector routeTreeEdges,int nominalRouteStartEdgeIndex)
    {
        // merge edges into segments
        Vector segments = new Vector();
        PathTreeEdge lastEdge = (PathTreeEdge)routeTreeEdges.elementAt(0);
        
        Vector segmentEdges = new Vector();
        segmentEdges.addElement(lastEdge);
        
        if (routeTreeEdges.size() == 1)
        {
            // only has one tree edge
            segments.addElement(segmentEdges);
        }
        
        for (int i=1; i< routeTreeEdges.size(); i++)
        {
            PathTreeEdge treeEdge = (PathTreeEdge)routeTreeEdges.elementAt(i);
            
            boolean isTheSameStreetName = 
                      (lastEdge.getStreetName() != null && 
                          treeEdge.getStreetName() != null && 
                             lastEdge.getStreetName().equals(treeEdge.getStreetName())); 
            
            boolean isContinue = (lastEdge.getTurnType() == Route.TURN_TYPE_CONTINUE) || 
                                 (lastEdge.getTurnType() == Route.TURN_TYPE_TURN_SLIGHT_LEFT) ||
                                 (lastEdge.getTurnType() == Route.TURN_TYPE_TURN_SLIGHT_RIGHT) ;
                                 
            boolean firstEdgeInSegIsIntersection = (lastEdge.getEdgeType() == DataUtil.INTERSECTION) && 
                                                   (segmentEdges.size() == 1);                     
            
            boolean mergeIntersection = firstEdgeInSegIsIntersection;
            if (firstEdgeInSegIsIntersection && i > 1)
            {
                PathTreeEdge e = (PathTreeEdge)routeTreeEdges.elementAt(i-2);
                if (e.getTurnType() == Route.TURN_TYPE_U_TURN)
                {
                    // don't need to merge intersection with current edge into one segment in case of UTurn
                    mergeIntersection = false;
                }    
            }
            
            // FIXME: what's the right logic to merge edges into segment here 
            if ((isContinue && isTheSameStreetName) || mergeIntersection )
            {
                segmentEdges.addElement(treeEdge);
                if (nominalRouteStartEdgeIndex != -1 && i >= nominalRouteStartEdgeIndex)
                {
                    segments.addElement(segmentEdges);
                    break;
                }
                
            } else
            {
                segments.addElement(segmentEdges);
                
                segmentEdges = new Vector();
                segmentEdges.addElement(treeEdge);
                
                if (nominalRouteStartEdgeIndex != -1 && i >= nominalRouteStartEdgeIndex)
                {
                    segments.addElement(segmentEdges);
                    break;
                }
            }
            
            if (i == routeTreeEdges.size() - 1)
            {
                segments.addElement(segmentEdges);
            }
            
            lastEdge = treeEdge;
        }   
       
        return segments;
    }
    
    private int abs(int value)
    {
        return value > 0 ? value : - value;
    }
    
    private int[] matchTreeEdgeOnNominalRoutes(PathTreeEdge edge, Route[] nominalRoutes)
    {
        int matchRouteIndex         = -1;
        int matchSegmentIndex       = -1;
        int matchEdgeIndex          = -1;
        int matchStartPointIndex    = -1;
        int matchEndPointIndex      = -1;
        
        int[] points = edge.getShapePoints();
        if (points.length < 4) return null;
        
        int lat1 = points[DataUtil.LAT];
        int lon1 = points[DataUtil.LON];
        int lat2 = points[points.length - 2 + DataUtil.LAT];
        int lon2 = points[points.length - 2 + DataUtil.LON];
        
        // because of edge merging in back end, SPT edge could probably 
        // consist of several normal route edges
        int lat3 = lat2;
        int lon3 = lon2;
        int edgeLen = points.length >> 1;
        int distThreshold = 3 * POINT_CLOSE_THESHOULD;
        for (int i = 1; i < edgeLen; i++)
        {
            int lat = points[(i << 1) + DataUtil.LAT];
            int lon = points[(i << 1) + DataUtil.LON];
            
            if (abs(lat - lat1) > distThreshold || abs(lon - lon1) > distThreshold)
            {
                lat3 = lat;
                lon3 = lon;
                break;
            }
        }
        
        for (int i=0; i< nominalRoutes.length; i++)
        {
            Route route = nominalRoutes[i];
            if (route == null || route.getSegments() == null) 
                continue;
            
            Segment[] segs = route.getSegments();
            for (int j=0; j< segs.length; j++)
            {
                Segment seg = segs[j];
                
                int[] bb = seg.getBoundingBox();
                if (bb == null) continue;
                
                int latMinSeg = bb[DataUtil.LAT];
                int lonMinSeg = bb[DataUtil.LON];
                int latMaxSeg = bb[2 + DataUtil.LAT];
                int lonMaxSeg = bb[2 + DataUtil.LON];
                
                if (lat1 - POINT_CLOSE_THESHOULD > latMaxSeg || 
                    lat1 + POINT_CLOSE_THESHOULD < latMinSeg ||
                    lon1 - POINT_CLOSE_THESHOULD > lonMaxSeg || 
                    lon1 + POINT_CLOSE_THESHOULD < lonMinSeg)
                    continue;
                
                if (lat2 - POINT_CLOSE_THESHOULD > latMaxSeg || 
                    lat2 + POINT_CLOSE_THESHOULD < latMinSeg ||
                    lon2 - POINT_CLOSE_THESHOULD > lonMaxSeg || 
                    lon2 + POINT_CLOSE_THESHOULD < lonMinSeg)
                    continue;
                
                int edgeSize = seg.edgesSize();
                for (int k=0; k < edgeSize; k ++)
                {
                    RouteEdge e = seg.getEdge(k);
                    if (e == null || e.getBoundingBox() == null) continue;

                    int[] box = e.getBoundingBox();
                    int latMinEdge = box[DataUtil.LAT];
                    int lonMinEdge = box[DataUtil.LON];
                    int latMaxEdge = box[2 + DataUtil.LAT];
                    int lonMaxEdge = box[2 + DataUtil.LON];
                    
                    if (lat1 - POINT_CLOSE_THESHOULD > latMaxEdge || 
                        lat1 + POINT_CLOSE_THESHOULD < latMinEdge ||
                        lon1 - POINT_CLOSE_THESHOULD > lonMaxEdge || 
                        lon1 + POINT_CLOSE_THESHOULD < lonMinEdge)
                        continue;
                    
                    if ((lat2 - POINT_CLOSE_THESHOULD > latMaxEdge || 
                         lat2 + POINT_CLOSE_THESHOULD < latMinEdge ||
                         lon2 - POINT_CLOSE_THESHOULD > lonMaxEdge || 
                         lon2 + POINT_CLOSE_THESHOULD < lonMinEdge) 
                        && (lat3 - POINT_CLOSE_THESHOULD > latMaxEdge || 
                            lat3 + POINT_CLOSE_THESHOULD < latMinEdge ||
                            lon3 - POINT_CLOSE_THESHOULD > lonMaxEdge || 
                            lon3 + POINT_CLOSE_THESHOULD < lonMinEdge )
                       )
                    {     
                       continue;
                    }  
                    
                    matchStartPointIndex = -1;
                    matchEndPointIndex   = -1;
                    
                    int pointSize = e.numPoints();
                    for (int m =0; m < pointSize; m++)                    
                    {
                        int lat = e.latAt(m);
                        int lon = e.lonAt(m);
                        
                        if (lat1 - POINT_CLOSE_THESHOULD < lat && 
                            lat1 + POINT_CLOSE_THESHOULD > lat &&
                            lon1 - POINT_CLOSE_THESHOULD < lon && 
                            lon1 + POINT_CLOSE_THESHOULD > lon)
                        {
                            matchStartPointIndex = m;
                        }
                        
                        if (lat2 - POINT_CLOSE_THESHOULD < lat && 
                            lat2 + POINT_CLOSE_THESHOULD > lat &&
                            lon2 - POINT_CLOSE_THESHOULD < lon && 
                            lon2 + POINT_CLOSE_THESHOULD > lon)
                        {
                            matchEndPointIndex = m;
                        }
                        
                        if (lat3 - POINT_CLOSE_THESHOULD < lat && 
                            lat3 + POINT_CLOSE_THESHOULD > lat &&
                            lon3 - POINT_CLOSE_THESHOULD < lon && 
                            lon3 + POINT_CLOSE_THESHOULD > lon)
                        {
                            matchEndPointIndex = m;
                        }
                        
                    }//end for
                    
                    if (matchStartPointIndex != -1 && 
                            matchEndPointIndex != -1 && 
                            matchEndPointIndex > matchStartPointIndex)
                    {
                        matchRouteIndex = i;
                        matchSegmentIndex = j;
                        matchEdgeIndex = k;
                        
                        return new int[] {matchRouteIndex,
                                          matchSegmentIndex,
                                          matchEdgeIndex,
                                          matchStartPointIndex,
                                          matchEndPointIndex}; 
                        
                    }
                    else
                    {
                        // snapping as back end possibly merge and decimate shape points
                        // Q: does this case really happen 
                    }
                    
                }//end for
            }//end for
        }//end for 
        
        return null;
    }
    
}



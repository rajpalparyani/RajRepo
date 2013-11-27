package com.telenav.nav.spt;

import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.PathTreeEdge;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.SptTree;

class SptUTurnHandler
{
    // used for filtering by length
    static int INTERSECTION_LENGTH_THRESHOULD           = 50;
    
    // used for filtering by bearing difference 
    static int U_TURN_EDGE_BEARING_DIFFERNCE_THRESHOULD = 45;
    
    // used to find crossed street for U-Turn 
    static int INTERSECTION_DIST_THRESHOULD              = 5;
    
    static int CROSS_EDGE_BEARING_DIFFERNCE_THRESHOULD   = 45;
    
    static void check4UTurn(Vector routeTreeEdges,int nominalRouteStartEdgeIndex,
                            SptManager manager)
    {
        Vector uTurnEdgeIndexes = new Vector();
        Vector intersectionEdgeIndexes = new Vector();

        int endIndex = routeTreeEdges.size();   
        if (nominalRouteStartEdgeIndex > 0)
            endIndex = nominalRouteStartEdgeIndex;
        
        for (int i=0; i < endIndex ; i++)
        {
            PathTreeEdge edge = (PathTreeEdge)routeTreeEdges.elementAt(i);
            if (edge.getTurnType() == Route.TURN_TYPE_U_TURN)
            {
                uTurnEdgeIndexes.addElement(new Integer(i));
            }
            
            if (edge.getEdgeType() == DataUtil.INTERSECTION)
            {
                intersectionEdgeIndexes.addElement(new Integer(i));
            }
        }
        
        for (int i=0; i< intersectionEdgeIndexes.size(); i++)
        {    
            int intersectionEdgeIndex = ((Integer)intersectionEdgeIndexes.elementAt(i)).intValue();
            check4IntersectionUTurn(routeTreeEdges, intersectionEdgeIndex);
        }   
        
        for (int i=0; i< uTurnEdgeIndexes.size(); i++)
        {    
            int uTurnEdgeIndex = ((Integer)uTurnEdgeIndexes.elementAt(i)).intValue();
            check4LocalStreetUTrun(routeTreeEdges, manager, uTurnEdgeIndex);
        }   
    }
    
    static void check4IntersectionUTurn(Vector routeTreeEdges, int index)
    {
        if (index == 0 || index == routeTreeEdges.size() - 1) return;
        
        PathTreeEdge preEdge  = (PathTreeEdge)routeTreeEdges.elementAt(index-1);
        PathTreeEdge curEdge  = (PathTreeEdge)routeTreeEdges.elementAt(index);
        PathTreeEdge nextEdge = (PathTreeEdge)routeTreeEdges.elementAt(index + 1);
        
        if ((preEdge.getTurnType() == Route.TURN_TYPE_CONTINUE || 
               preEdge.getTurnType() == Route.TURN_TYPE_U_TURN ||
                  curEdge.getTurnType() == Route.TURN_TYPE_CONTINUE) && 
            (preEdge.getStreetName() != null && 
                ! preEdge.getStreetName().equalsIgnoreCase(nextEdge.getStreetName())))
            return;

        // check to make sure the intersection edge is short enough
        int[] box = curEdge.getBoundingBox();
        long latDelt = box[DataUtil.LAT + 2] - box[DataUtil.LAT];
        long lonDelt = box[DataUtil.LON + 2] - box[DataUtil.LON];
        
        int cosLat = DataUtil.getCosLat(box[DataUtil.LAT]);
        long latThreshold = INTERSECTION_LENGTH_THRESHOULD;
        long lonThreshold = (latThreshold << DataUtil.SHIFT)/ cosLat;
        if (latDelt > latThreshold || lonDelt > lonThreshold) return;
        
        // check edge bearing differences
        int preEdgeBearing  = getEdgeBearing(preEdge);
        int nextEdgeBearing = getEdgeBearing(nextEdge);
        if (Math.abs(preEdgeBearing - nextEdgeBearing) < 180 - U_TURN_EDGE_BEARING_DIFFERNCE_THRESHOULD ||
            Math.abs(preEdgeBearing - nextEdgeBearing) > 180 + U_TURN_EDGE_BEARING_DIFFERNCE_THRESHOULD )
            return;
        
        PathTreeEdge edge = clonePathTreeEdge(preEdge);
        edge.setTurnType(Route.TURN_TYPE_U_TURN);
        
        routeTreeEdges.insertElementAt(edge, index-1);
        routeTreeEdges.removeElement(preEdge);
    }
    
    static PathTreeEdge clonePathTreeEdge(PathTreeEdge src)
    {
        PathTreeEdge clone = new PathTreeEdge();
        clone.setEdgeType(src.getEdgeType());
        clone.setSpeedLimit(src.getSpeedLimit());
        clone.setShapePoints(src.getShapePoints());
        clone.setStreetName(src.getStreetName());
        clone.setBoundingBox(src.getBoundingBox());
        
        return clone;
    }
    
    
    static void check4LocalStreetUTrun(Vector routeTreeEdges, SptManager manager,
                                       int index)
    {
        if (index == routeTreeEdges.size() - 1) return;
        
        PathTreeEdge curEdge  = (PathTreeEdge)routeTreeEdges.elementAt(index);
        PathTreeEdge nextEdge = (PathTreeEdge)routeTreeEdges.elementAt(index + 1);
        
        if (curEdge.getStreetName() != null && ! curEdge.getStreetName().equals(nextEdge.getStreetName()))
            return;
        
        int curEdgeBearing = getEdgeBearing(curEdge);
        
        int[] points = curEdge.getShapePoints();
        int lat = points[points.length - 2 + DataUtil.LAT];
        int lon = points[points.length - 2 + DataUtil.LON];
        
        int cosLat = DataUtil.getCosLat(lat);
        long latThreshold = INTERSECTION_DIST_THRESHOULD;
        long lonThreshold = (latThreshold << DataUtil.SHIFT)/ cosLat;
        
        int latMin = lat - (int)latThreshold;
        int latMax = lat + (int)latThreshold;
        int lonMin = lon - (int)lonThreshold;
        int lonMax = lon + (int)lonThreshold;
        
        String crossStreetName = null;
        
        // search for cross street name if possible 
        Vector pathTrees = manager.getSptTrees();
        for (int i=0; i < pathTrees.size(); i++)
        {
            SptTree pathTree = (SptTree)pathTrees.elementAt(i);
            
            // filter using bounding box of whole path tree
            int[] bb = pathTree.getBoundingBox();
            if (bb == null || bb[DataUtil.LAT] > latMax || bb[DataUtil.LAT + 2] < latMin || 
                    bb[DataUtil.LON] > lonMax || bb[DataUtil.LON + 2] < lonMin )
                continue;
            
            Vector edges = pathTree.getPathTreeEdges(true);
            if (edges == null) continue;
            
            for (int j=0; j < edges.size(); j++)
            {
                PathTreeEdge edge = (PathTreeEdge)edges.elementAt(j);
                
                if (edge == null || edge.getShapePoints() == null ||
                        edge.getBoundingBox() == null)
                    continue;
                
                // filter using bounding box of path tree edge
                int[] box = edge.getBoundingBox();
                if (box[DataUtil.LAT] > latMax || box[DataUtil.LAT + 2] < latMin || 
                        box[DataUtil.LON] > lonMax || box[DataUtil.LON + 2] < lonMin)
                {    
                    continue;
                }
                else
                {
                    if (edge == curEdge || edge == nextEdge) continue;
                    
                    if (edge.getStreetName() != null && 
                            edge.getStreetName().equals(curEdge.getStreetName()))
                        continue;
                    
                    int edgeBearing = getEdgeBearing(edge);
                    if (Math.abs(edgeBearing - curEdgeBearing) > CROSS_EDGE_BEARING_DIFFERNCE_THRESHOULD)
                    {
                        crossStreetName = edge.getStreetName();
                        break;
                    }
                }
            }//end for
            
            if (crossStreetName != null) break;
            
        }//end for 
        
        int lat1 = points[points.length - 4 + DataUtil.LAT];
        int lon1 = points[points.length - 4 + DataUtil.LON];
        
        int dist = DataUtil.distance(lat-lat1, lon-lon1);
        int lat2 = lat + 1 * (lon - lon1) / dist;  
        int lon2 = lon - 1 * (lat - lat1) / dist;
        int lat3 = lat - (lat2 - lat);
        int lon3 = lon - (lon2 - lon);
        
        PathTreeEdge edge = new PathTreeEdge();
        edge.setStreetName(crossStreetName);
        edge.setSpeedLimit(curEdge.getSpeedLimit());
        edge.setEdgeType(DataUtil.INTERSECTION);
        edge.setTurnType(Route.TURN_TYPE_TURN_LEFT);
        
        int[] shapePoints = new int[] {lat,lon,lat2,lon2,lat3,lon3,lat,lon};
        edge.setShapePoints(shapePoints);
        
        int[] box = new int[]{ Math.min(lat2,lat3), Math.min(lon2, lon3),
                               Math.max(lat2, lat3),Math.max(lon2, lon3)};
        edge.setBoundingBox(box);
        routeTreeEdges.insertElementAt(edge, index+1);
        
    }
    
    static int getEdgeBearing(PathTreeEdge edge)
    {
        int[] points = edge.getShapePoints();
        
        int lat1 = points[DataUtil.LAT];
        int lon1 = points[DataUtil.LON];
        int lat2 = points[points.length - 2 + DataUtil.LAT];
        int lon2 = points[points.length - 2 + DataUtil.LON];
        
        return DataUtil.bearing(lat1, lon1, lat2, lon2);
    }
}

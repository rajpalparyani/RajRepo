/*
 * RouteInfo.java
 *
 * TeleNav, 2003-2008
 * Confidential and proprietary.
 */

package com.telenav.nav.spt;

import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.Segment;
import com.telenav.datatypes.route.SptTree;

/**
 * 
 */
class SptRouteInfo 
{
    int latFrom;
    
    int lonFrom;
    
    Stop dest;
    
    Vector pathTrees = new Vector();
    
    // used for reverse route
    private Route[] routes;      
    
    private boolean isRouteCompleted = false;
    
    // only used for SPT, and only nominal route has this value
    // the reason is edges in client side has been merged 
    // but for SPT request client side should request based on index of origin edges
    private int totalOriginEdgeNumber = 0;  
    
    private boolean hasRouteNode = false;
    
    SptRouteInfo() 
    {  
        
    }
    
    void clear()
    {
    	totalOriginEdgeNumber = 0;
        pathTrees.removeAllElements();    
        dest = null;
        routes = null;  
        
        isRouteCompleted = false;
        hasRouteNode = false;
    }
    
    void setRoutes(Route[] routes)
    {
        this.routes = routes;
        hasRouteNode = true;
        
        isRouteCompleted = false;
    }

    boolean hasRouteNode()
    {
    	return hasRouteNode;
    }
    
    Route[] getRoutes()
    {
        return routes;
    }

    void checkRouteCompletion()
    {
    	if (routes == null) return;
    	
        Route route = routes[0];
        for (int i = 0; i < route.segmentsSize(); i++)
        {
            if (! route.segmentAt(i).isEdgeResolved())
            {
            	android.util.Log.d("[SPT]","route is not completed :: i = " + i);
            	isRouteCompleted = false;
            	
                return;
            }
        }
        
        android.util.Log.d("[SPT]","route is completed ");
	    isRouteCompleted = true;
    }
    
    void releaseMapEdgeMemory4ReverseRoute()
    {
    	// clean shape points in edges for reverse route to reduce memory usage
		if (routes == null) return;
		
		android.util.Log.d("[SPT]", "SptRouteInfo :: release map edge memory for reverse route");
    	
		for (int i = 0; i < routes.length; i++)
		{
			Route route = routes[i];
			if (route == null || route.segmentsSize() == 0) continue;
			
		    for (int j = 0; j < route.segmentsSize(); j++)
		    {
		    	Segment seg = (Segment)route.segmentAt(j);
		    	if (seg == null || seg.edgesSize() == 0) continue;
		    	
		    	for (int k = 0; k < seg.edgesSize(); k++)
		    	{
		    		RouteEdge edge = (RouteEdge)seg.getEdge(k);
		    		if (edge == null) continue;

		    		edge.setShapePoints(null);
		    		///edge.deltaRs = null;
		    	}
		    }//end for
		}//end for 
    }
    
    boolean isRouteCompleted()
    {
        return this.isRouteCompleted;    
    }

    public Vector getPathTrees()
    {
        return pathTrees;    
    }
        
    public void addSptTree(SptTree tree)
    {
        if (tree == null) return;
        
        if (tree.getEndEdgeIndex() <= getSPTEndEdgeIndex())
        {
            return;
        } 
        
        pathTrees.addElement(tree);
    } 
    
    public int getSPTEndEdgeIndex()
    {
        if (pathTrees.size() > 0)
        {
        	SptTree tree = (SptTree)pathTrees.elementAt(pathTrees.size() - 1);
            return tree.getEndEdgeIndex();
        }
        
        return 0;
    }
    
    void setTotalOriginEdgeNumber(int totalOriginEdgeNumber)
    {
    	this.totalOriginEdgeNumber = totalOriginEdgeNumber;
    }

    int getTotalOriginEdgeNumber()
    {
        return this.totalOriginEdgeNumber;
    }
    
    void setDest(Stop dest)
    {
    	this.dest = dest;
    }
    
    Stop getDest()
    {
    	return dest;
    }
} 

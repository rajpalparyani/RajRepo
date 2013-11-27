/*
 * RouteCacheManager.java
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
import com.telenav.logger.Logger;

class SptRouteCacheManager 
{
    private SptRouteInfo forwardRouteInfo;
    private SptRouteInfo reverseRouteInfo;
    
    private SptRouteStoreManager routeStoreManager;
    
    SptRouteCacheManager() 
    {
        forwardRouteInfo = new SptRouteInfo();
        reverseRouteInfo = new SptRouteInfo();  
        
        routeStoreManager = new SptRouteStoreManager();
    }
    
    void clearRouteInfo(boolean isForwardRoute)
    {
    	routeStoreManager.clearRouteStoreInfo(isForwardRoute);
    	getSptRouteInfo(isForwardRoute).clear();
    }
    
    void clearAllData()
    {
        if (forwardRouteInfo != null) forwardRouteInfo.clear();
        if (reverseRouteInfo != null) reverseRouteInfo.clear();
        
        routeStoreManager.clearAllData();
    }
    
    void clearAllRAM()
    {
    	if (forwardRouteInfo != null) forwardRouteInfo.clear();
        if (reverseRouteInfo != null) reverseRouteInfo.clear();
        
       	routeStoreManager.clearAllRAM();
    }
    
    boolean hasRouteData(boolean isForwardRoute)
    {
    	return getSptRouteInfo(isForwardRoute).hasRouteNode();
    }
    
    Vector getSptTrees(boolean isForwardRoute)
    {
    	return getSptRouteInfo(isForwardRoute).getPathTrees();
    }    
    
    void setRouteData(Route[] routes, byte[] routeData, boolean isForwardRoute)
    {
   	    routeStoreManager.saveRouteData(routeData, isForwardRoute); 	
    	getSptRouteInfo(isForwardRoute).setRoutes(routes);
    }
    
    void addMapSectionData(byte[] mapSectionData, boolean isForwardRoute)
    {
   		routeStoreManager.saveMapSectionNode(mapSectionData, isForwardRoute);
   		
   		android.util.Log.d("[SPT]","check route complete :: isForwardRoute = " + isForwardRoute);
   		
        getSptRouteInfo(isForwardRoute).checkRouteCompletion();
   		
   		if (!isForwardRoute && getSptRouteInfo(isForwardRoute).isRouteCompleted())
   		{
   		    getSptRouteInfo(isForwardRoute).releaseMapEdgeMemory4ReverseRoute();	
   		}
    }
    
    void addSptTree(SptTree pathTree, boolean isForwardRoute)
    {
    	if (pathTree == null) return;
    	
   		routeStoreManager.saveSPTTree(pathTree, isForwardRoute);
    	getSptRouteInfo(isForwardRoute).addSptTree(pathTree);
    }
    
    void addLocallyStoredPathTree(SptTree pathTree)
    {
        forwardRouteInfo.addSptTree(pathTree);	
    }
    
    void setTotalOriginEdgeNumber(boolean isForwardRoute, int totalOriginEdgeNumber)
    {
    	getSptRouteInfo(isForwardRoute).setTotalOriginEdgeNumber(totalOriginEdgeNumber);
    }
    
    int getTotalOriginEdgeNumber(boolean isForwardRoute)
    {
    	return getSptRouteInfo(isForwardRoute).getTotalOriginEdgeNumber();
    }
    
    Route[] getRoutes(boolean isForwardRoute)
    {
    	return getSptRouteInfo(isForwardRoute).getRoutes();
    }

    boolean isRouteCompleted(boolean isForwardRoute)
    {
    	return getSptRouteInfo(isForwardRoute).isRouteCompleted();
    }
    
    void setDest(Stop dest, boolean isForwardRoute)
    {
    	getSptRouteInfo(isForwardRoute).setDest(dest);
    }
    
    Stop getDest(boolean isForwardRoute)
    {
    	return getSptRouteInfo(isForwardRoute).getDest();
    }
    
    SptRouteInfo getSptRouteInfo(boolean isForwardRoute)
    {
    	return isForwardRoute ? forwardRouteInfo : reverseRouteInfo;
    }
}   

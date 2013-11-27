package com.telenav.nav.spt;

import java.util.Vector;


import android.util.Log;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.nav.NavUtil;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.ISptDataSerializable;
import com.telenav.datatypes.route.PathTreeEdge;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.SptTree;
import com.telenav.location.TnLocation;
import com.telenav.nav.spt.event.SptNavEvent;

public class SptManager implements ISptDataHandler
{
    private SptRouteCacheManager routeCacheManager;
    
    private ISptDataSerializable sptDataSerializable;

    private SptRequester sptRequester;
    
    private boolean isLocalRerouteEnabled = true;
    
    private static SptManager instance = new SptManager();
    
    private SptManager()
    {
    	routeCacheManager = new SptRouteCacheManager();
    	sptRequester = new SptRequester(routeCacheManager);
    }
    
    public synchronized static SptManager getInstance()
    {
        return instance;  	
    }

    public void setSptDataReqeuster(ISptDataRequester dataRequester)
    {
    	sptRequester.setSptDataRequester(dataRequester);
    }
    
    public void setSptDataSerializable(ISptDataSerializable sptDataSerializable)
    {
    	this.sptDataSerializable = sptDataSerializable;
    }
    
    ISptDataSerializable getSptDataSerializable()
    {
    	return sptDataSerializable;
    }

    public boolean isLocalRerouteEnabled()
    {
        return isLocalRerouteEnabled;
    }
    
    public void disableLocalReroute()
    {
        isLocalRerouteEnabled = false;
    }

    Route[] generateNewRoutes(TnLocation[] fixes, PathTreeEdge snappedTreeEdge, NavUtil navUtil)
    {
        if (! isLocalRerouteEnabled) return null;

        Route[] routes = SptRouteGenerator.getInstance().generateRoutes(fixes,snappedTreeEdge,
                                                                        this,navUtil);
        return routes;
    }
    
    //TODO: add this advanced enhancement later
//    {
//        if (nominalRoutes != null && nominalRoutes.length > 0 && nominalRoutes[0] != null)
//        {
//        	// protection code to avoid destination change in some wrong case
//        	try
//        	{
//        		Segment lastSeg = nominalRoutes[0].segmentAt(nominalRoutes[0].segmentsSize() - 1);
//        		RouteEdge lastEdge = lastSeg.getEdge(lastSeg.edgesSize() - 1);
//        		
//        		Segment lastLocalSeg = routes[0].segmentAt(routes[0].segmentsSize() - 1);
//        		RouteEdge lastLocalEdge = lastLocalSeg.getEdge(lastLocalSeg.edgesSize() - 1);
//        		
//        		if ( lastEdge != null && lastLocalEdge != null && 
//        			 lastEdge.numPoints() > 1 && lastLocalEdge.numPoints() > 1 && 	
//        			(Math.abs(lastEdge.latAt(lastEdge.numPoints() - 1) - 
//        				      lastLocalEdge.latAt(lastLocalEdge.numPoints() - 1)) > 100 ||
//        		     Math.abs(lastEdge.lonAt(lastEdge.numPoints() - 1) - 
//        		    		  lastLocalEdge.lonAt(lastLocalEdge.numPoints() - 1)) > 100))
//        		{
//        			logNavEvent("[FATAL][PathTreeManager] :: destination changed :: "  +  
//        					    lastEdge.latAt(lastEdge.numPoints() - 1) + "," + 
//        					    lastEdge.lonAt(lastEdge.numPoints() - 1));
//        	    	
//        			logRoute(routes[0]);
//        			return null; 	
//        		}
//        	}
//        	catch (Exception ex)
//        	{
//        		logNavEvent("[PathTreeManager] :: check destinaiton caused exeption :: " + ex);
//        	}
//        	
//            // add nominal route to enable route switch back from local generated route to original route
//            this.currentRoutes = new Route[routes.length + 1];
//            for (int i = 0; i < routes.length; i++)
//                 currentRoutes[i] = routes[i];
//            
//            currentRoutes[routes.length] = nominalRoutes[0];
//            
//            // let local generated route has similar delay time as nominal route
//            if (nominalRoutes[0].getTrafficDelayTime() > 0 && 
//            		nominalRoutes[0].getMarkedTrafficDelayDistToDest() > 0)
//            {
//             	for (int i = 0; i < routes.length; i ++)
//            	{
//            		int routeLen = routes[i].getLength();
//            		if (routeLen > nominalRoutes[0].getMarkedTrafficDelayDistToDest())
//            		{	
//            		    routes[i].setTrafficDelayTime(nominalRoutes[0].getTrafficDelayTime(),routeLen);
//            		}    
//            		else
//            		{
//            			routes[i].setTrafficDelayTime(
//            		        nominalRoutes[0].getTrafficDelayTime() * routeLen / 
//            		              nominalRoutes[0].getMarkedTrafficDelayDistToDest(),
//            			    routeLen);
//            		}
//            	}
//            }//end if 
//        }
//        else
//        {
//            this.currentRoutes = routes;
//        }   
//        
//        routes = this.currentRoutes;
//        this.selectedRouteIndex = 0;
//    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    
    public Route[] generateRoutesLocally(TnNavLocation[] fixes,
			                             PathTreeEdge snappedEdge,
			                             NavUtil navUtil) 
	{
	    return generateNewRoutes(fixes, snappedEdge, navUtil);
	}

    public Vector getSptTrees()
    {
        return routeCacheManager.getSptTrees(true);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @see INavEngineListener::eventUpdate(NavEvent navEvent);
     */
    public void eventUpdate(SptNavEvent navEvent)
    {
    	sptRequester.eventUpdate(navEvent);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @see ISptDataHandler::setRouteData()
     */
    public void setRouteData(Route[] routes, byte[] routeData, boolean isForwardRoute)
    {
    	log("[SPT] set route data");
    	
    	routeCacheManager.setRouteData(routes, routeData, isForwardRoute);
    	if (isForwardRoute)
    		sptRequester.onNominalRoutesArrived(routes);
    }
	
    /**
     * @see ISptDataHandler::setTotalOriginEdgeNumber()
     */
    public void setTotalOriginEdgeNumber(int totalOriginEdgeNumber, boolean isForwardRoute)
    {
    	log("[SPT] set total origin edge number :: " + totalOriginEdgeNumber + ", isForwardRoute=?" + isForwardRoute);
    	
    	routeCacheManager.setTotalOriginEdgeNumber(isForwardRoute, totalOriginEdgeNumber);
    }
    
    /**
     * @see ISptDataHandler:: setDest()
     */
	public void setDest(Stop stop, boolean isForwardRoute)
	{
		log("[SPT] set destination :: " + isForwardRoute);
		
		routeCacheManager.setDest(stop, isForwardRoute);
	}
	
	/**
	 * @see ISptDataHandler::addMapSectionData()
	 */
	public void addMapSectionData(byte[] mapSectionData, boolean isForwardRoute)
	{
		log("[SPT] add map section data :: " + isForwardRoute);
		
		routeCacheManager.addMapSectionData(mapSectionData, isForwardRoute);
	}
    
    /**
     * @see ISptDataHandler::handleSptData(..)
     */
    public void handleSptData(SptTree tree, boolean isForwardRoute)
    {
    	sptRequester.handleSptData(tree, isForwardRoute);
    }
	
    /**
     * @see ISptDataHandler::notifySPTTransactionError()
     */
	public void notifySptTransactionError()
	{
		sptRequester.notifySptTransactionError();
	}
    
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void log(String message)
	{
		Log.d("[SPT]", message);
	}
}

package com.telenav.nav.spt;

import java.util.Vector;

import android.util.Log;

import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.datatypes.route.SptTree;
import com.telenav.nav.spt.event.SptNavEvent;
import com.telenav.nav.spt.event.SptNavInfoEvent;

/**
 *  This class is used to schedule SPT request based on current navigation status
 *   
 * @author Sean Xia
 */

class SptRequester 
{
    public static int FIRST_SPT_REQUEST_WAIT_TIME             =   1  *  40 * 1000;  // 40 seconds
    
    public static int SPT_REQUEST_INTERVAL                    =   3  *  30 * 1000;  // 1.5 minutes
    
    public static int NETWORK_ERORR_WAIT_INTERVAL             =         40 * 1000;  // 40 seconds
    
    public static int CHECK_INTERVAL                          =         20 * 1000;  // 20 seconds
    
    public static int REVERSE_ROUTE_EDGE_REQUEST_INTERVER     =   4  *  60 * 1000;  // 4 minutes      
        
    // each SPT request at least cover 20 miles along nominal routes
    public static long SPT_REQUEST_MIN_COVERAGE_DIST          =  15L * 5280 * 8192 / 29919;
    
    public static long FIRST_SPT_REQUEST_MIN_COVERAGE_DIST    =  15L * 5280 * 8192 / 29919;
    
    // stored SPT can cover at most 100 miles 
    public static long MAX_STORED_SPT_COVERAGE_DIST           = 100L * 5280 * 8192 / 29919; 
    
    public static long SPT_ALIVE_DISTANCE_THRESHOLD           =  20L * 5280 * 8192 / 29919;
    
    public static long MAX_REVERSE_ROUTE_EDGE_REQUEST_LEN     = 250L * 5280 * 8192 / 29919;
    
    // don't request for reverse route if route is longer than 1000 miles to save memory usage
    public static long MAX_ROUTE_LENGTH_FOR_REVERSE_ROUTE     = 1000L* 5280 * 8192 / 29919; 
    
    ISptDataRequester sptDataRequester;
    
    SptRouteCacheManager routeCacheManager;
    
    long lastRequestTime;
    
    long navigationStartTime = -1;
    
    long lastCheckTime = -1;
    
    long lastReverseRouteEdgeRequestTime;
    
    int endSPTEdgeIndex = 0;
    
    int totalEdgeCount = 0;
    
    int routeID = Integer.MIN_VALUE;
    
    int continuousSPTTransactionErrorCount = 0;
    
    SptRequester(SptRouteCacheManager manager)
    {
    	routeCacheManager = manager; 
    }
    
    void setSptDataRequester(ISptDataRequester dataRequester)
    {
    	this.sptDataRequester = dataRequester;
    }
    
    /**
     * @see INavEngineListener::eventUpdate(NavEvent navEvent);
     */
    public void eventUpdate(SptNavEvent event)
    {
    	if (event.getEventType() != SptNavEvent.TYPE_INFO) return;
    	
    	long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime < CHECK_INTERVAL)
            return;
        
        SptNavInfoEvent navEvent = (SptNavInfoEvent)event;
        
        lastCheckTime = currentTime;
        Vector nominalRoutes = RouteWrapper.getInstance().getRoutes();
        if (nominalRoutes == null || nominalRoutes.size() == 0)
            return;
        
        Route nominalRoute = (Route)nominalRoutes.elementAt(0);
        checkRouteChange(nominalRoute,currentTime);
        
        if (currentTime - this.lastRequestTime < SPT_REQUEST_INTERVAL)
            return;
        
        // wait for a certain time before sending the first SPT request
        if (currentTime - navigationStartTime < FIRST_SPT_REQUEST_WAIT_TIME)
            return;
        
        // don't need to request any SPT for route generated by stored route data    
        if (nominalRoute.isLocallyGenerated())
            return;    

        // check if already request all SPT along the whole nominal route
        int totalOriginEdgeNum = this.totalEdgeCount;
        if (routeCacheManager.getTotalOriginEdgeNumber(true) > 0)
        {
            totalOriginEdgeNum = routeCacheManager.getTotalOriginEdgeNumber(true);
        }    
        
        if (this.endSPTEdgeIndex >= totalOriginEdgeNum - 1)
        {
            requestReverseRouteData(true);
            return;
        }

        check4PassedSPT(navEvent,nominalRoute);
     
        if (routeCacheManager.getSptTrees(true).size() > 1)
        {
            if (nominalRoute != null && 
            	nominalRoute.getLength() < MAX_ROUTE_LENGTH_FOR_REVERSE_ROUTE &&
            	requestReverseRouteData(false))
            {
                return;
            }
        }

        // check whether stored SPT tree has already covered long enough distance (100 miles here)
        long distToDestSPT = calcSPTDistToDest(this.endSPTEdgeIndex, nominalRoute);
        if (navEvent.getDistanceToDest() - distToDestSPT > MAX_STORED_SPT_COVERAGE_DIST)
        {
        	if (nominalRoute != null && nominalRoute.getLength() < MAX_ROUTE_LENGTH_FOR_REVERSE_ROUTE)
                requestReverseRouteData(false);
        }
        else
        {
            requestSPT(nominalRoute,this.endSPTEdgeIndex,true);
        }
    }
    
    public void notifySptTransactionError()
    {
    	// normally caused by SPT size is too big
        this.lastRequestTime = System.currentTimeMillis() - SPT_REQUEST_INTERVAL + NETWORK_ERORR_WAIT_INTERVAL;
        continuousSPTTransactionErrorCount ++;
        
        log("SPT Requester :: notify SPT transaction error :: continuousSPTTransactionErrorCount = " + continuousSPTTransactionErrorCount);
    }
    
    public void handleSptData(SptTree tree, boolean isForwardRoute)
    {
    	log("[SPT] handle SPT data :: " + tree.getStartEdgeIndex() + "," + tree.getEndEdgeIndex()
    		 + ", isForwardRoute ?" + isForwardRoute );
    	
    	continuousSPTTransactionErrorCount = 0;
        if (isForwardRoute)
        {
        	SptTree newTree = SptTree.deserializeCompressedPathTree(tree.getBinData(),
            tree.getUncompressedLen()); 

			newTree.setRouteId(tree.getRouteID());
			newTree.setStartEdgeIndex(tree.getStartEdgeIndex());
			newTree.setEndEdgeIndex(tree.getEndEdgeIndex());
			newTree.setUncompressedLen(tree.getUncompressedLen());
			newTree.setBinData(tree.getBinData());
			routeCacheManager.addSptTree(newTree, isForwardRoute);
			
			boolean isFirstForwardSPT = false;
			if (routeCacheManager.getSptRouteInfo(isForwardRoute).getPathTrees().size() == 0)
			isFirstForwardSPT = true;
			
			this.endSPTEdgeIndex = tree.getEndEdgeIndex();
			log ("[SPT] handle SPT data :: endSptEdgeIndex = " + endSPTEdgeIndex);
			
			//clean up memory, expand only in case of necessary
			if (! isFirstForwardSPT) newTree.setPathTreeEdges(null);
        }
        else
        {
        	routeCacheManager.addSptTree(tree, isForwardRoute);
            tree.setBinData(null);
        }
    }
    
    boolean requestReverseRouteData(boolean hasAllForwardSPT)
    {
    	log("[SPT] request reverse route data :: hasAllForwardSPT = " + hasAllForwardSPT);
    	
    	boolean isForwardRoute = false;
        if (! routeCacheManager.hasRouteData(isForwardRoute))
        {
        	log("[SPT] request for reverse route ");
            sptDataRequester.requestReverseRoute();
            
            this.lastRequestTime = System.currentTimeMillis() - SPT_REQUEST_INTERVAL/2;
            this.lastReverseRouteEdgeRequestTime = System.currentTimeMillis();
            
            return true;
        }
        else 
        {
            if (! routeCacheManager.getSptRouteInfo(isForwardRoute).isRouteCompleted())
            {
            	return requestReverseRouteEdge();
            	
            }//end if     
        }//end else
       
        if (hasAllForwardSPT)
        {
        	return requestReverseSPT();
        }
        
        return false;   
    }
    
    boolean requestReverseSPT()
    {
    	boolean isForwardRoute = false;
    	Vector v =  routeCacheManager.getSptTrees(isForwardRoute);
        int endEdgeIndex = 0;
        for (int i=0; i< v.size(); i++)
        {
            SptTree tree = (SptTree)v.elementAt(i);
            if (tree.getEndEdgeIndex() > endEdgeIndex)
                endEdgeIndex = tree.getEndEdgeIndex();
        }
        
        Route[] routes = routeCacheManager.getRoutes(isForwardRoute);
        if (routes != null && routes.length > 0 && routes[0] != null)
        {
            int totalOriginEdgeNum = calcTotalEdgeNumber(routes[0]);
            if (routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute) > 0)
            {
                totalOriginEdgeNum = routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute);
            }    
            
            if (endEdgeIndex >= totalOriginEdgeNum - 1)
                return false; 
            
            requestSPT(routes[0],endEdgeIndex,false);
            return true;
        }
        
        return false;
    }
    
    boolean requestReverseRouteEdge()
    {
    	Route[] routes = routeCacheManager.getRoutes(false);
        int startSegIndex = 0;
        if (routes != null && routes.length > 0 && routes[0] != null)
        {
            Route route = routes[0];
            int segSize = route.segmentsSize();
            for (int i=0; i < segSize; i ++)
            {
                Segment seg = route.segmentAt(i);
                if (! seg.isEdgeResolved())
                {
                    if (System.currentTimeMillis() - this.lastReverseRouteEdgeRequestTime < 
                                REVERSE_ROUTE_EDGE_REQUEST_INTERVER)
                    {
                        return false;
                    }            
                    
                    startSegIndex = i;
                    requestReverseRouteEdge(startSegIndex,route);
                    this.lastRequestTime = System.currentTimeMillis() - SPT_REQUEST_INTERVAL/2;
                    this.lastReverseRouteEdgeRequestTime = System.currentTimeMillis();
                    
                    return true;
                }
            } //end for    
        }//end if 
        
        return false;
    }
    
    void requestReverseRouteEdge(int startSegIndex, Route reverseRoute)
    {
    	long requestLen = 0;
        int segSize = reverseRoute.segmentsSize();
        for (int i= startSegIndex; i < segSize; i++)
        {
            Segment seg = reverseRoute.segmentAt(i);
            requestLen += seg.getLength();
            
            if (i == segSize - 1 || requestLen > MAX_REVERSE_ROUTE_EDGE_REQUEST_LEN)   
            {
            	log("[SPT] requestReverseRouteEdge:: " + startSegIndex + "," + i);
            	
            	sptDataRequester.requestReverseRouteEdge(reverseRoute.getRouteID(),startSegIndex,i);
                return;
            }    
        }
    }
    
    void check4PassedSPT(SptNavInfoEvent navEvent,Route nominalRoute)
    {
    	boolean isForwardRoute = true;
        if (routeCacheManager == null || routeCacheManager.getSptTrees(isForwardRoute) == null)
            return;
        
        int SPT_SIZE_CHECK_THRESHOLD = 4;
        int MAX_SPT_SIZE = 10;
        
        // discard SPT for passed route by checking distance to destination
        if (routeCacheManager.getSptTrees(isForwardRoute).size() > SPT_SIZE_CHECK_THRESHOLD)
        {
            long distToDest = navEvent.getDistanceToDest();
            Vector trees = this.routeCacheManager.getSptTrees(isForwardRoute);
            for (int i = trees.size() - 1 ; i >= 0; i--)
            {
                SptTree tree = (SptTree)trees.elementAt(i);
                long distSPT2Dest = calcSPTDistToDest(tree.getEndEdgeIndex(),nominalRoute);
                if (distSPT2Dest - distToDest  >  SPT_ALIVE_DISTANCE_THRESHOLD)
                    trees.removeElementAt(i);
            }    
        }
        
        if (routeCacheManager.getSptTrees(isForwardRoute).size() > MAX_SPT_SIZE)
        {
            // remove the first path tree
            routeCacheManager.getSptTrees(isForwardRoute).removeElementAt(0);
        }
    }
    
    long calcSPTDistToDest(int edgeEndIndex, Route nominalRoute)
    {
    	boolean isForwardRoute = true;
    	
        int totalEdgeNumber = calcTotalEdgeNumber(nominalRoute);
        if (routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute) > 0 && 
            totalEdgeNumber > 0)
        {
        	//FIXME: minus 1 or not
            edgeEndIndex = edgeEndIndex * totalEdgeNumber /
                               routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute) - 1 ; 
            if (edgeEndIndex < 0 ) edgeEndIndex = 0;
        }
        
        // calculate distance from the end point of SPT to destination
        long distToDest = 0;
        int edgeCount = 0;
        int segSize = nominalRoute.segmentsSize();
        
        for (int i= 0 ; i < segSize; i++)
        {
            Segment seg = nominalRoute.segmentAt(i);
            if (edgeCount > edgeEndIndex)
            {
                distToDest += seg.getLength();
            }
            else if (edgeCount < edgeEndIndex && 
                         edgeCount + seg.edgesSize() > edgeEndIndex )
            {
                for (int j = 0; j < seg.edgesSize(); j++)
                {
                    RouteEdge edge = seg.getEdge(j);
                    if (edgeCount + j > edgeEndIndex && edge != null)
                    {
                        distToDest += edge.getLength();
                    }
                }
            }
            
            edgeCount += seg.edgesSize(); 
        }   
        
        return distToDest;
    }
    
    /**
     *  invoked when client gets new route from server to set correct route ID
     */
    void onNominalRoutesArrived(Route[] nominalRoutes)
    {
    	routeID = nominalRoutes[0].getRouteID();
    	totalEdgeCount = calcTotalEdgeNumber(nominalRoutes[0]);
    }
    
    void checkRouteChange(Route nominalRoute, long currentTime)
    {
    	boolean isForwardRoute = true;
        if (routeID != nominalRoute.getRouteID())
        {
        	log ("[SPT] checkRoute change :: " + routeID + "," + nominalRoute.getRouteID());
        	
            // a new route has been generated by server side 
            navigationStartTime = currentTime;
            routeID = nominalRoute.getRouteID();
            lastRequestTime = currentTime - (SPT_REQUEST_INTERVAL - FIRST_SPT_REQUEST_WAIT_TIME);
            
            //client probably already has one SPT back with route 
            endSPTEdgeIndex = 0;
            Vector trees = this.routeCacheManager.getSptTrees(isForwardRoute);
            if (trees != null)
            {
                for (int i=0; i < trees.size(); i++)
                {
                    SptTree tree = (SptTree)trees.elementAt(i);
                    if (tree.getEndEdgeIndex() > this.endSPTEdgeIndex)
                        endSPTEdgeIndex = tree.getEndEdgeIndex();
                }
            }
            
            totalEdgeCount = calcTotalEdgeNumber(nominalRoute);
        }
    }
    
    void requestSPT(Route nominalRoute, int endSPTIndex, boolean isForwardRoute)
    {
        // request for SPT which can cover at least 20 miles along nominal route
        int edgeCount = 0;
        int requestCoveredDist = 0;
        int segSize = nominalRoute.segmentsSize();
        int originSPTEndIndex = endSPTIndex;
        
        int totalEdgeNumber = calcTotalEdgeNumber(nominalRoute);
        if (routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute) > 0 && totalEdgeNumber > 0)
        {
        	log("[SPT] total origin edge number :: " + routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute)
        			+ ", isForwardRoute?" + isForwardRoute);
        	
            // covert back 
            endSPTIndex = endSPTIndex * totalEdgeNumber /
                             routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute) - 1;
            if (endSPTIndex < 0) endSPTIndex = 0;                              
        }
        
        for (int i= 0 ; i < segSize; i++)
        {
            Segment seg = nominalRoute.segmentAt(i);
            if (edgeCount + seg.edgesSize() > endSPTIndex)
            {
                for (int j = 0; j < seg.edgesSize(); j++)
                {
                    if (edgeCount + j < endSPTIndex) continue;
                    
                    RouteEdge edge = seg.getEdge(j);
                    
                    // need to get extra edges first and it should not happen
                    if (edge == null)
                    {
                        return;
                    }    
                    
                    requestCoveredDist += edge.getLength();
                    if (requestCoveredDist > SPT_REQUEST_MIN_COVERAGE_DIST  ||
                           (i == segSize - 1 && j == seg.edgesSize() - 1 && edgeCount + j  > endSPTIndex))
                    {
                        // make SPT requests overlapping each other to solve the SPT boundary issue 
                        boolean isRequestSent =
                            requestShortestPathTree( isForwardRoute,
                                                     nominalRoute, 
                                                     originSPTEndIndex > 0 ? originSPTEndIndex -1 : originSPTEndIndex,
                                                     edgeCount + j + 1); 
                        
                        if (isRequestSent)
                        {
                            this.lastRequestTime = System.currentTimeMillis(); 
                        }    
                                                              
                        return;
                    }
                }// end for
            }//end if

            edgeCount += seg.edgesSize();
        }//end for
    }
    
    private boolean requestShortestPathTree(boolean isForwardRoute, Route nominalRoute,
                                            int startEdgeIndex, int endEdgeIndex)
    {
    	if (nominalRoute == null) return false;
        
    	int totalEdgeNumber = calcTotalEdgeNumber(nominalRoute);
        int totalOriginEdgeNumber = routeCacheManager.getTotalOriginEdgeNumber(isForwardRoute);
        
        if (totalOriginEdgeNumber > 0 && totalEdgeNumber > 0)
        {
            endEdgeIndex = endEdgeIndex * totalOriginEdgeNumber / totalEdgeNumber 
                             +  totalOriginEdgeNumber/ totalEdgeNumber ;      
            
            if (startEdgeIndex >= totalOriginEdgeNumber)
                return false; 
             
            if (endEdgeIndex > totalOriginEdgeNumber)
                endEdgeIndex = totalOriginEdgeNumber;                              
        }
        
        if (continuousSPTTransactionErrorCount > 0)
        {
            // there are two major reasons for transaction failure
            // 1. response is greater than a size limitation (we can decrease the response in this case)
            // 2. server can not generate SPT because of too few edges involved
            if (endEdgeIndex - startEdgeIndex < 10 && continuousSPTTransactionErrorCount % 3 != 0)
            {
            	endEdgeIndex += 5 * Math.min(10, continuousSPTTransactionErrorCount);
            	if (totalOriginEdgeNumber > 0)
            	{
            		endEdgeIndex = Math.min(endEdgeIndex, totalOriginEdgeNumber);
            	}
            	else
            	{
            		endEdgeIndex = Math.min(endEdgeIndex, totalEdgeNumber);
            	}
            }
            else
            {
            	// request less SPT edges if fail to get SPT in latest request
                endEdgeIndex = startEdgeIndex + (endEdgeIndex - startEdgeIndex)/ ( 1 + continuousSPTTransactionErrorCount);
            }
        }
        
        // avoid to send the same SPT request again and again
        // at least send one SPT request for more than 2 edges
        // in some special case, one edge could be more than 20 miles
        if (endEdgeIndex < startEdgeIndex + 2)
        {
            if (totalOriginEdgeNumber > 0)
            {
                if (startEdgeIndex + 2 <= totalOriginEdgeNumber)
                    endEdgeIndex = startEdgeIndex + 2;
            }
            else
            {
                if (startEdgeIndex + 2 <= totalEdgeNumber)
                    endEdgeIndex = startEdgeIndex + 2;
            }
        }
        
        log("[SPT] request SPT :: " + startEdgeIndex + "," + endEdgeIndex + ", isFowradRoute?" + isForwardRoute);
        
        sptDataRequester.requestSptTree(isForwardRoute,
        		                        nominalRoute.getRouteID(), 
                                        startEdgeIndex,
                                        endEdgeIndex); 
       
        return true;                                       
    }
    
    private int calcTotalEdgeNumber(Route route)
    {
        if (route == null) return 0;
        
        int totalEdgeNumber = 0;
        Segment[] segs = route.getSegments();
        if (segs == null) return 0;
        
        for (int i = 0; i < segs.length; i++)
        {
            if (segs[i] == null) return 0;
            
            totalEdgeNumber += segs[i].edgesSize();
        }
        
        return totalEdgeNumber;
    }
    
    private void log(String message)
    {
    	Log.d("[SPT]", message);
    }
    
}
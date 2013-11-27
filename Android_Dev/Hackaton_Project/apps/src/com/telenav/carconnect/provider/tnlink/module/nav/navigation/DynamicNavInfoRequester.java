/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * DynamicAudioRequester.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.nav.navigation;

import android.util.Log;

import com.telenav.data.serverproxy.impl.INavigationProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.nav.NavEngine;

/**
 * DynamicNavInfoRequester will getting audio and extra route timely during navigation.
 * 
 *@author zhdong@telenav.cn
 *@date 2011-1-13
 */
class DynamicNavInfoRequester
{
    
    public final static int LOOKAHEAD_SEGMENTS = 4;
    public final static int LOOKBACK_SEGMENTS = 3;
    
    public static long MAX_ROUTE_EDGE_REQUEST_LEN  = 250L * 5280 * 8192 / 29919;
    
    boolean hasAllEdges = false;
    
    protected long lastAudioRequestTime = -1;
    protected long lastEdgeRequestTime = -1;
    
    private static DynamicNavInfoRequester requester = new DynamicNavInfoRequester();

    private DynamicNavInfoRequester()
    {

    }
    
    public void reset()
    {
        hasAllEdges = false;
        lastAudioRequestTime = -1;
        lastEdgeRequestTime = -1;
    }

    public static DynamicNavInfoRequester getInstance()
    {
        return requester;
    }
    
    public int requestRouteEdge(INavigationProxy proxy)
    {
        if (hasAllEdges || !isRouteRequestAllowed())
        {
            return -1;
        }

        long currTime = System.currentTimeMillis();

        if (currTime > lastEdgeRequestTime + (120 * 1000))
        {
            Route route = RouteWrapper.getInstance().getCurrentRoute();
            int routeId = route.getRouteID();
            int startSegIndex = Math.max(0, NavEngine.getInstance().getCurrentNavState().getSegmentIndex() - LOOKBACK_SEGMENTS);
            startSegIndex = findStartSegmentIndex(route, startSegIndex - 1);
            if (startSegIndex >= route.getSegments().length)
            {
                hasAllEdges = true;
                return -1;
            }

            lastEdgeRequestTime = currTime;

            int endSegIndex = findLastSegmentIndex(route, startSegIndex + 1);
            
            Log.i("Route", "requestRouteEdge start = " + startSegIndex + ", end = " + endSegIndex);

            proxy.requestRouteEdge(routeId, startSegIndex, endSegIndex);
            return startSegIndex;
        }
        return -1;
    }
    
    protected static int findStartSegmentIndex(Route route, int i)
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
    
    protected static int findLastSegmentIndex(Route route, int startIndex)
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

    public void requestDynamicAudio(INavigationProxy proxy)
    {
        if (isRouteRequestAllowed())
        {
            long currTime = System.currentTimeMillis();
            // we should not get the audio too early for that: first, we get the first segment audio already, second it
            // will be canceled.
            if (lastAudioRequestTime == -1)
            {
                lastAudioRequestTime = System.currentTimeMillis() - 10000;
            }
            if (currTime - lastAudioRequestTime < 15000)
            {
                return;
            }
            lastAudioRequestTime = currTime;
            proxy.requestDynamicAudio();
        }
    }
    
    private boolean isRouteRequestAllowed()
    {
        //TODO implement it later
//        if (isOutOfCoverage()) return false;
//        
//        if (pathTreeManager.isLocalRerouteEnabled())
//        {
//            // check whether currently using stored route data to navigate
//            IRoute[] nominalRoutes = pathTreeManager.getNominalRoutes();
//            return nominalRoutes != null && 
//                   nominalRoutes.length > 0 && 
//                   nominalRoutes[0]!= null && 
//                   ! nominalRoutes[0].isLocallyGenerated();
//        }
        return true;
    }
}

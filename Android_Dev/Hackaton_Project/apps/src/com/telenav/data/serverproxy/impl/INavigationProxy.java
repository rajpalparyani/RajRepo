/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * INavigationProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface INavigationProxy
{
    public final static int RESPONSE_CODE_NOT_HANDLED       = -1;
    public final static int RESPONSE_CODE_NEW_ROUTE         = 1;
    public final static int RESPONSE_CODE_STATIC_ROUTE      = 2;
    public final static int RESPONSE_CODE_ON_TRACK          = 3;
    public final static int RESPONSE_CODE_INTERACTIVE       = 4;
    public final static int RESPONSE_CODE_COMM_ERROR        = 5;
    public final static int RESPONSE_CODE_SERVER_ERROR      = 6;
    public final static int RESPONSE_CODE_GPS_ERROR         = 7;
    public final static int RESPONSE_EXTRA_EDGE             = 8;
    public final static int RESPONSE_DYNAMIC_AUDIO          = 9;
    public final static int RESPONSE_ALTERNATE_ROUTE        = 10;
    public final static int RESPONSE_NO_BETTER_ROUTE        = 11;
    public final static int RESPONSE_DECIMATED_ROUTE        = 12;
    public final static int RESPONSE_RTS_FAILED             = 13;
    public final static int RESPONSE_CODE_NEW_ROUTE_STREAM  = 14;
    public final static int RESPONSE_CODE_ROUTE_CHOICES     = 17;
    
    /**
     * flag to distinguish CN audio logic from US
     */
    public static final int AUDIO_ASSEMBLE_TYPE_NORMAL = 0;
    public static final int AUDIO_ASSEMBLE_TYPE_CN = 1;
    
    public String[] getFoundYouOn();
    
    public int getResponseCode();
    
    public Route getDecimateRoute();

    
    public Stop getGeocodedDestination();
    
//    public Node getMapTiles();
    
    public Route[] getChoices();
    
    public int[] getRouteChoiceETA();
    
    public int[] getRouteChoicesTrafficDelay();
    
    public int[] getRouteChoicesLength();
    
	public Vector getStaticEta();
	
	public Vector getDynmicEta();
	
	public Vector getDistance();
    
    public String requestCommuteRoute(long alertId);
    
    public String requestStaticRoute(Stop origStop, Stop destStop, int routeStyle, int avoidSetting);
    
    public String requestDynamicRoute(Stop destStop, int routeStyle, int avoidSetting, boolean isReCalculateRoute, TnLocation[] gpsFix,
            int nFixes, int heading);
    
    public String requestRouteEdge(int routeId, int startSegIndex, int endSegIndex);
    
    public String requestRouteChoices(Stop destStop, int routeSytle, int avoidSetting, boolean needRecalculateRoute, 
            TnLocation[] gpsFixes, int fixCount, int heading);
    
    public String requestRouteChoicesSelection(Stop destStop, int routeID, TnLocation[] gpsFixes,
            int fixCount, boolean isSPTNeeded, 
            boolean isChunkedResponse, boolean isDynamicRoute);
    
    public void requestDynamicAudio();
    
    public String requestDeviation(int routeID, int[] devCount, TnLocation[] gpsFix, int nFixes, Stop dest, int heading,
            int segmentIndex, int edgeIndex, int pointIndex,int range, boolean isChunkEnabled);
    
    public String requestDecimatedRoute(int routeId);
    
    public String requestAvoidIncident(NavState onRoute, String[] edges, TnLocation[] gpsFix, int numFixes,
            Stop dest, int routeStyle, int heading);
    
    /**
     * Request avoid segment with decimated multiple route response.<br>
     * 
     * It is not like the old requestAvoidSegment() whose response are bitmaps.
     * 
     * @param onRoute
     * @param edges
     * @param gpsFix
     * @param numFixes
     * @param routeStyle
     * @param heading
     */
    public String requestAvoidSegment(NavState onRoute, String[] edges, TnLocation[] gpsFix, int numFixes, int routeStyle, int heading);
    
    
    
    /**
     * Request minimized delay with decimated multiple route response.
     * 
     * @param onRoute
     * @param edges
     * @param gpsFix
     * @param numFixes
     * @param routeStyle
     * @param heading
     */
    public String requestMinimizeDelay(NavState onRoute, String[] edges, TnLocation[] gpsFix, int numFixes,
            int routeStyle, int heading);
    

    
    public String requestSelectedReroute(int routeId, boolean isStatic, Stop dest, int heading);
    
    /**
     * Request avoid segment with decimated multiple route response.<br>
     * 
     * It is not like the old requestAvoidSegment() whose response are bitmaps.
     * 
     * @param edges
     * @param orig
     * @param routeStyle
     */
    public String requestStaticAvoidSegment(String[] edges, Stop orig, int routeStyle);
    

    
    /**
     * 
     * Request minimized delay with decimated multiple route response.
     * 
     * @param orig
     * @param routeStyle
     */
    public String requestStaticMinimizeDelay(Stop orig, int routeStyle);
    
   
    /**
     * 
     * Request avoid segment with decimated multiple route response.<br>
     * 
     * It is not like the old requestCommuteAlertAvoidSegment() whose response are bitmaps.
     * 
     * @param edges
     * @param alertId
     */
    public String requestCommuteAlertAvoidSegment(String[] edges, long alertId);
    
  
    /**
     * Request minimized delay with decimated multiple route response.
     * 
     * @param alertId
     */
    public String requestCommuteAlertMinimizeDelay(long alertId);
    

    /**
     *  used for ADI judgment in area near origin location of navigation session   
     */
    public Vector getMapTiles();
    
	/**
     *  used for Drive to screen to get eta   
     */
    public String requestEta(int originLat, int originLon, int[] destLat, int[] destLon, int routeStyle);
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * INavSdkProxyConstants.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;


/**
 * @author hchai
 * @date 2011-12-7
 */
public interface INavSdkProxyConstants
{
    // nav
    public static final String ACT_NAV_AVOID_TRAFFIC_INCIDENT = "AvoidIncident";
    
    public static final String ACT_NAV_VEHICLE_POSITION = "VehiclePosition";

    public static final String ACT_NAV_SPEED_LIMIT_EXCEEDED = "SpeedLimitExceeded";

    public static final String ACT_NAV_CANCEL_LIMIT_EXCEEDED = "CancelLimitExceeded";

    public static final String ACT_NAV_GUIDANCE = "Guidance";

    public static final String ACT_NAV_ROUTE_PROGRESS = "RouteProgress";

    public static final String ACT_NAV_NAVIGATION_STATUS = "NavigationStatus";

    public static final String ACT_NAV_LANE_ASSIST_INFORMATION = "LaneAssistInformation";

    public static final String ACT_NAV_TRAFFIC_DETAIL = "TrafficDetail";

    public static final String ACT_NAV_TRAFFIC_INCIDENTS_AHEAD = "TrafficIncidentsAhead";

    public static final String ACT_NAV_CHECK_FOR_BETTER_ROUTE = "CheckForBetterRoute";

    public static final String ACT_NAV_ROUTE_PLAN = "RoutePlan";

    public static final String ACT_NAV_ROUTE_SUMMARY = "RouteSummary";

    public static final String ACT_NAV_STOP_NAVIGATION = "StopNavigation";

    public static final String ACT_NAV_NAVIGATION_DEVIATION = "NavigationDeviation";

    public static final String ACT_NAV_NAVIGATE_USING_ROUTE = "NavigateUsingRoute";

    public static final String ACT_NAV_ETA = "Eta";

    public static final String ACT_NAV_START_NAVIGATION = "StartNavigation";

    public static final String ACT_NAV_TRAFFIC_INCIDENTS_INFO = "TrafficInfo";

    // poi
    public static final String ACT_POI_CATEGORY_SEARCH = "PoiSearch";

    public static final String ACT_POI_ONEBOX_SEARCH = "OneboxSearch";

    public static final String ACT_POI_CANCEL_POI_SEARCH = "CancelPoiSearch";

    public static final String ACT_POI_REVERSE_GEOCODE = "ReverseGeocode";

    // mapdownload
    public static final String ACT_QUERY_MAP_DOWNLOAD = "queryMapDownload";

    public static final String ACT_START_MAP_DOWNLOAD = "startMapDownload";

    public static final String ACT_CANCEL_MAP_DOWNLOAD = "cancelMapDownload";

    public static final String ACT_PAUSE_MAP_DOWNLOAD = "pauseMapDownload";

    public static final String ACT_DELETE_MAP_DOWNLOAD = "deleteMapDownload";

    public static final String ACT_ON_REGION_DOWNLOAD_PROGRESS = "mapDownloadProgress";
    
    public static final String ACT_GET_BILLBOARD_ADS = "getBillBoardAds";

    public static final String ACT_VOICE_GUIDANCE = "VoiceGuidance";
    
    public static final String STRING_UNKNOWN_ROAD = "Unknown road";
    
    public static final String ONBOARD_ROUTE_PLANNING_ERROR = "NavigationError_ServiceCommunicationProblem";
}

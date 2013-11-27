/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkNavProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.TripsDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.BillboardAd;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkNavigationProxyHelper;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.navsdk.NavsdkFileUtil;
import com.telenav.navsdk.events.NavigationData.BackLightOption;
import com.telenav.navsdk.events.NavigationData.DistanceUnit;
import com.telenav.navsdk.events.NavigationData.NavigationParameters;
import com.telenav.navsdk.events.NavigationData.RouteComputationMode;
import com.telenav.navsdk.events.NavigationData.RouteSummary;
import com.telenav.navsdk.events.NavigationEvents.AvoidIncidentAheadRequest;
import com.telenav.navsdk.events.NavigationEvents.CancelAvoidIncidentAheadRequest;
import com.telenav.navsdk.events.NavigationEvents.CancelRoutePlanRequest;
import com.telenav.navsdk.events.NavigationEvents.CheckForBetterRouteRequest;
import com.telenav.navsdk.events.NavigationEvents.EtaRequest;
import com.telenav.navsdk.events.NavigationEvents.GetBillBoardAdsRequest;
import com.telenav.navsdk.events.NavigationEvents.GuidanceRequest;
import com.telenav.navsdk.events.NavigationEvents.NavigateUsingRouteRequest;
import com.telenav.navsdk.events.NavigationEvents.RoutePlanRequest;
import com.telenav.navsdk.events.NavigationEvents.RouteSummaryRequest;
import com.telenav.navsdk.events.NavigationEvents.StartNavigationRequest;
import com.telenav.navsdk.events.NavigationEvents.StopNavigationRequest;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailRequest;
import com.telenav.navsdk.events.NavigationEvents.TrafficInfoRequest;
import com.telenav.navsdk.events.NavigationEvents.VehiclePosition;
import com.telenav.navsdk.events.NavigationEvents.VoiceGuidancePlayNotification;
import com.telenav.navsdk.nav.event.NavSdkNavEvent;

/**
 * @author hchai
 * @date 2011-12-1
 */
public class NavSdkNavigationProxy extends AbstractNavSdkServerProxy
{
    private NavSdkNavigationProxyHelper helper;

    private String ttsString;

    private byte[] mp3Data;

    private RouteSummary summary;

    private static int MINIUM_GPS_COUNT = 3;

    private Vector staticEta;

    private Vector dynmicEta;

    private int totalDelay;

    private int totalIncident;
    
    private int requestId;

    private TrafficSegment[] segments;

    // for route choices
    private Route[] routeChoices;

    private int[] routeChoicesEta;

    private int[] routeChoicesTrafficDelay;

    private int[] routeChoicesLength;

    private NavSdkNavEvent navEvent;

    private TrafficAlertEvent trafficAlertEvent;
    
    private Vector<BillboardAd> billBoardAds;
    
    private VehiclePosition vehiclePoistion;
    
    private VoiceGuidancePlayNotification voiceGuidance;
    
    public static int currentRoutePlanRequestId = 0;
    
    public NavSdkNavigationProxy(IServerProxyListener listener)
    {
        super(listener);
        helper = NavSdkNavigationProxyHelper.getInstance();
    }

    // TN7.x request
    public void requestRouteChoices(TnLocation[] locations, Address destAddress, int routeStyle, int avoidSetting)
    {
        sendRoutePlanRequest(locations, destAddress, routeStyle, avoidSetting);
    }

    // TN7.x request
    public void requestStaticRouteChoices(Address orgAddress, Address destAddress, int routeStyle, int avoidSetting)
    {
        sendStaticRoutePlanRequest(orgAddress, destAddress, routeStyle, avoidSetting);
    }

    // TN7.x request
    public void requestRouteChoices(Address destAddress, int routeStyle, int avoidSetting)
    {
        requestRouteChoices(null, destAddress, routeStyle, avoidSetting);
    }

    // TN7.x request
    public void requestRouteChoicesSelection(String routeName,  boolean isReroute)
    {
        // used for dynamic nav route
        sendNavigateUsingRouteRequest(routeName, isReroute);
    }

    // TN7.x request
    public void requestEta(Address[] orgAddress, Address[] destAddress, int routeStyle, int avoidSettings, int requestId)
    {
        sendEtaRequest(orgAddress, destAddress, routeStyle, avoidSettings, requestId);
    }

    public void requestDynamicRoute(Address destAddress, int routeStyle, int avoidSetting)
    {
        sendStartNavigationRequest(null, destAddress, routeStyle, avoidSetting);
    }

    // TN7.x request
    public void requestTrafficSummary()
    {
        sendTrafficDetailRequest();
    }

    private boolean isDirectionAudioEnabled()
    {
        TripsDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getTripsDao();
        Preference audioTypePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        int audioPreference = -1;
        if (audioTypePref != null)
        {
            audioPreference = audioTypePref.getIntValue();
        }
        return audioPreference == Preference.AUDIO_DIRECTIONS_TRAFFIC || audioPreference == Preference.AUDIO_DIRECTIONS_ONLY;
    }

    private boolean isIncidentAudioEnabled()
    {
        TripsDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getTripsDao();
        Preference audioTypePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        int audioPreference = -1;
        if (audioTypePref != null)
        {
            audioPreference = audioTypePref.getIntValue();
        }
        return audioPreference == Preference.AUDIO_DIRECTIONS_TRAFFIC || audioPreference == Preference.AUDIO_TRAFFIC_ONLY;
    }

    private NavigationParameters constructNavigationParameters(Address orgAddress, Address destAddress, int routeStyle,
            int avoidSettings, boolean isStaticRoute)
    {
        NavigationParameters.Builder param = NavigationParameters.newBuilder();

        if (isStaticRoute)
        {
            param.setIsStaticNavigation(true);
        }
        else
        {
            param.setIsStaticNavigation(false);
        }

        param.addWaypoints(NavSdkProxyUtil.convertProtoPointOfInterest(orgAddress));
        param.addWaypoints(NavSdkProxyUtil.convertProtoPointOfInterest(destAddress));
        // Route style setting;
        RouteComputationMode routeComputationMode;
        if (routeStyle == Route.ROUTE_FASTEST)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Fastest;
        }
        else if (routeStyle == Route.ROUTE_SHORTEST)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Shortest;
        }
        else if (routeStyle == Route.ROUTE_AVOID_HIGHWAY)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_PreferStreets;
        }
        else if (routeStyle == Route.ROUTE_PEDESTRIAN)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Pedestrian;
        }
        else
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Fastest;// By default;
        }
        
        param.setComputationMode(routeComputationMode);
        
        if(avoidSettings > 0)
        {
            boolean avoidTraffic = (avoidSettings & Preference.AVOID_TRAFFIC_DELAYS) != 0 ;
            param.setAvoidTraffic(avoidTraffic);
            boolean avoidToll = (avoidSettings & Preference.AVOID_TOLLS) != 0 ;
            param.setAvoidTollRoads(avoidToll);
            boolean avoidHovLanes = (avoidSettings & Preference.AVOID_CARPOOL_LANES) != 0 ;
            param.setAvoidHovLanes(avoidHovLanes);
        }
        return param.build();
    }

    private NavigationParameters constructNavigationParameters(TnLocation[] originals, Address destAddress, int routeStyle,
            int avoidSetting)
    {
        NavigationParameters.Builder param = NavigationParameters.newBuilder();

        param.setIsStaticNavigation(false);
        // NavSDK require at least 3 points for navigation.
        if (originals != null && originals.length > 0)
        {
            for (int i = 0; i < MINIUM_GPS_COUNT; i++)
            {
                int index = i > originals.length - 1 ? originals.length - 1 : i;
                param.addRecentPositions(NavSdkProxyUtil.convertProtRecentLocation(originals[index]));
            }
        }

        param.addWaypoints(NavSdkProxyUtil.convertProtoPointOfInterest(destAddress));
        addDestinationType(param,  destAddress);

        // Audio setting;
        param.setAudioFileFormat(AbstractDaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().audioFormat);
        param.setAudioFilePath(NavsdkFileUtil.getNavsdkAudioDirectory());
        param.setAudioRuleFilePath(NavsdkFileUtil.getNavsdkAudioRuleDirectory());
        param.setVoicePromptsForDirectionsEnabled(isDirectionAudioEnabled());
        param.setVoicePromptsForTrafficEnabled(isIncidentAudioEnabled());

        // Distance unit setting
        Preference preference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_DISTANCEUNIT);
        DistanceUnit distanceUnit;
        if (preference != null && preference.getIntValue() == Preference.UNIT_USCUSTOM)
        {
            // currentUnit = "MPH";
            distanceUnit = DistanceUnit.DistanceUnit_Miles;
        }
        else
        {
            // currentUnit = "KPH";
            distanceUnit = DistanceUnit.DistanceUnit_Kilometers;
        }
        param.setDistanceUnit(distanceUnit);

        // Backlight setting;
        int backlightPref = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
            Preference.ID_PREFERENCE_BACKLIGHT);
        BackLightOption backLightOption;
        if (Preference.BACKLIGHT_ON == backlightPref)
        {
            backLightOption = BackLightOption.BackLightOption_AlwaysOn;
        }
        else if (Preference.BACKLIGHT_OFF == backlightPref)
        {
            backLightOption = BackLightOption.BackLightOption_DeviceDefault;
        }
        else if (Preference.BACKLIGHT_TURNS == backlightPref)
        {
            backLightOption = BackLightOption.BackLightOption_OnAtTurns;
        }
        else
        {
            backLightOption = BackLightOption.BackLightOption_DeviceDefault;// By default;
        }
        param.setBacklightOption(backLightOption);

        // Route style setting;
        RouteComputationMode routeComputationMode;
        if (routeStyle == Route.ROUTE_FASTEST)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Fastest;
        }
        else if (routeStyle == Route.ROUTE_SHORTEST)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Shortest;
        }
        else if (routeStyle == Route.ROUTE_AVOID_HIGHWAY)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_PreferStreets;
        }
        else if (routeStyle == Route.ROUTE_PEDESTRIAN)
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Pedestrian;
        }
        else
        {
            routeComputationMode = RouteComputationMode.RouteComputationMode_Fastest;// By default;
        }
        param.setComputationMode(routeComputationMode);

        // Route setting;
        if ((avoidSetting & Preference.AVOID_TRAFFIC_DELAYS) != 0)
        {
            param.setAvoidTraffic(true);
        }
        if ((avoidSetting & Preference.AVOID_TOLLS) != 0)
        {
            param.setAvoidTollRoads(true);
        }
        if ((avoidSetting & Preference.AVOID_CARPOOL_LANES) != 0)
        {
            param.setAvoidHovLanes(true);
        }

        // LaneAssit setting;
        Preference laneAssistPreference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_LANE_ASSIST);
        if (laneAssistPreference != null && laneAssistPreference.getIntValue() == Preference.LANE_ASSIST_ON)
        {
            param.setLaneAssistEnabled(true);
        }
        else
        {
            param.setLaneAssistEnabled(false);
        }

        // SpeedLimits setting;
        Preference speedLimitsPreference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_SPEED_LIMITS);
        if (speedLimitsPreference != null && speedLimitsPreference.getIntValue() == Preference.LANE_ASSIST_ON)
        {
            param.setSpeedLimitAlertEnabled(true);
        }
        else
        {
            param.setSpeedLimitAlertEnabled(false);
        }

        int trafficCameraFeatureValue = FeaturesManager.getInstance()
                .getStatus(FeaturesManager.FEATURE_CODE_NAV_TRAFFIC_CAMERA);
        int speedTrapFeatureValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_NAV_SPEED_TRAP);
        int incidentFeatureValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC_INCIDENT);

        boolean isCameraFeatureEnabled = trafficCameraFeatureValue == FeaturesManager.FE_ENABLED
                || trafficCameraFeatureValue == FeaturesManager.FE_PURCHASED;
        boolean isSpeedTrapFeatureEnabled = speedTrapFeatureValue == FeaturesManager.FE_ENABLED
                || speedTrapFeatureValue == FeaturesManager.FE_PURCHASED;
        boolean isIncidentFeatureEnabled = incidentFeatureValue == FeaturesManager.FE_ENABLED
                || incidentFeatureValue == FeaturesManager.FE_PURCHASED;

        // TrafficCamera setting;
        Preference trafficCameraPreference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_TRAFFIC_CAMERA_ALERT);
        if (trafficCameraPreference != null && trafficCameraPreference.getIntValue() == Preference.TRAFFIC_CAMERA_ALERT_ON
                && isCameraFeatureEnabled)
        {
            param.setTrafficCameraAlertEnabled(true);
        }
        else
        {
            param.setTrafficCameraAlertEnabled(false);
        }

        // SpeedTrap setting;
        Preference speedTrapPreference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_SPEED_TRAP_ALERT);
        if (speedTrapPreference != null && speedTrapPreference.getIntValue() == Preference.SPEED_TRAP_ALERT_ON
                && isSpeedTrapFeatureEnabled)
        {
            param.setSpeedTrapAlertEnabled(true);
        }
        else
        {
            param.setSpeedTrapAlertEnabled(false);
        }

        // Traffic Alert setting;
        Preference trafficAlertPreference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_TRAFFICALERT);
        if (trafficAlertPreference != null && trafficAlertPreference.getIntValue() == Preference.TRAFFIC_ALERT_ON
                && isIncidentFeatureEnabled)
        {
            param.setTrafficIncidentAlertEnabled(true);
        }
        else
        {
            param.setTrafficIncidentAlertEnabled(false);
        }

        return param.build();
    }

    public void sendStartNavigationRequest(TnLocation[] locations, Address destAddress, int routeStyle, int avoidSetting)
    {
        if (locations == null)
        {
            locations = new TnLocation[3];
            TnLocation location = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            locations[0] = location;
            locations[1] = location;
            locations[2] = location;
        }

        StartNavigationRequest.Builder request = StartNavigationRequest.newBuilder();
        request.setNav(constructNavigationParameters(locations, destAddress, routeStyle, avoidSetting));
        helper.startNavigation(request.build(), this);
    }

    // Dynamic route plan
    public void sendRoutePlanRequest(TnLocation[] locations, Address destAddress, int routeStyle, int avoidSetting)
    {
        // A StopNavigationRequest should be sent before each new Navigation(RoutePlanRequest would be considered as the
        // start of new navigation)
        RoutePlanRequest.Builder request = RoutePlanRequest.newBuilder();
        request.setNav(constructNavigationParameters(locations, destAddress, routeStyle, avoidSetting));
        
        currentRoutePlanRequestId = (currentRoutePlanRequestId + 1) % 100;
        
        setRequestId(currentRoutePlanRequestId);
        
        request.setRequestId(currentRoutePlanRequestId);
        
        Logger.log(Logger.INFO, this.getClass().getName(), "ROUTE_PLAN: ==== send route plan request, request id is " + currentRoutePlanRequestId);
        
        helper.routePlan(request.build(), this);
    }

    // Static route plan
    public void sendStaticRoutePlanRequest(Address orgAddress, Address destAddress, int routeStyle, int avoidSetting)
    {
        // A StopNavigationRequest should be sent before each new Navigation(RoutePlanRequest would be considered as the
        // start of new navigation)
        RoutePlanRequest.Builder request = RoutePlanRequest.newBuilder();
        request.setNav(constructNavigationParameters(orgAddress, destAddress, routeStyle, avoidSetting, true));
        
        currentRoutePlanRequestId = (currentRoutePlanRequestId + 1) % 100;
        
        setRequestId(currentRoutePlanRequestId);
        
        request.setRequestId(currentRoutePlanRequestId);
        
        helper.routePlan(request.build(), this);
    }

    public void cancelRoutePlanRequest()
    {
        CancelRoutePlanRequest.Builder builder = CancelRoutePlanRequest.newBuilder();
        
        builder.setRequestId(this.getRequestId());
        
        helper.cancelRoutePlan(builder.build(), this);
    }
    
    public void sendEtaRequest(Address[] orgAddress, Address[] destAddress, int routeSytle, int avoidSettings, int requestId)
    {
        EtaRequest.Builder request = EtaRequest.newBuilder();
        int size = 0;
        int orgSize = orgAddress == null ? 0 : orgAddress.length;
        int destSize = destAddress == null ? 0 : destAddress.length;
        if (orgSize != destSize)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.ERROR, this.getClass().getName(), "Org and Dest for ETA are invalid!!!");
            }
            size = Math.min(orgSize, destSize);
        }
        else
        {
            size = orgSize;
        }

        if (size == 0)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.ERROR, this.getClass().getName(), "Org and Dest for ETA are Empty!!!");
            }
            return;
        }

        for (int i = 0; i < size; i++)
        {
            if (orgAddress != null && orgAddress[i] != null && destAddress[i] != null)
            {
                request.addNav(constructNavigationParameters(orgAddress[i], destAddress[i], routeSytle, avoidSettings, false));
            }
        }
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), " CR send eta!!!");
        }
        request.setRequestId(requestId);
        helper.eta(request.build(), this);
    }

    public void sendStopNavigationRequest()
    {
        StopNavigationRequest.Builder request = StopNavigationRequest.newBuilder();
        helper.stopNavigation(request.build(), this);
    }

    // Used for static nav route
    public void sendRouteSummaryRequest(String str)
    {
        RouteSummaryRequest.Builder request = RouteSummaryRequest.newBuilder();
        request.setRouteName(str);

        helper.routeSummary(request.build(), this);
    }

    public void sendCheckForBetterRouteRequest(int segmentIndex)
    {
        CheckForBetterRouteRequest.Builder request = CheckForBetterRouteRequest.newBuilder();
        request.addSegmentsToAvoid(segmentIndex);
        helper.checkForBetterRoute(request.build(), this);
    }

    public void sendTrafficDetailRequest()
    {
        TrafficDetailRequest.Builder request = TrafficDetailRequest.newBuilder();
        helper.trafficDetail(request.build(), this);
    }

    public void sendGuidanceRequest()
    {
        GuidanceRequest.Builder request = GuidanceRequest.newBuilder();
        helper.guidance(request.build(), this);
    }
    
    /**
     * Play a specific guidance
     * @param routeLeg route leg to play for.
     */
    public void sendGuidanceRequest(int routeLeg)
    {
        GuidanceRequest.Builder request = GuidanceRequest.newBuilder();
        request.setRouteLeg(routeLeg);
        helper.guidance(request.build(), this);
    }

    public void sendTrafficInfoRequest(int segmentIndex)
    {
        TrafficInfoRequest.Builder request = TrafficInfoRequest.newBuilder();
        request.setRouteLeg(segmentIndex);
        helper.trafficInfo(request.build(), this);
    }
    
    public void sendAvoidIncidentRequest(int requestId)
    {
        AvoidIncidentAheadRequest.Builder request = AvoidIncidentAheadRequest.newBuilder();
        request.setRequestId(requestId);
        helper.avoidIncident(request.build(), this);
    }
    
    public void sendCancelAvoidIncidentRequest(int requestId)
    {
        CancelAvoidIncidentAheadRequest.Builder request = CancelAvoidIncidentAheadRequest.newBuilder();
        request.setRequestId(requestId);
        helper.cancelAvoidIncident(request.build(), this);
    }

    public void sendNavigateUsingRouteRequest(String routeName, boolean isReroute)
    {
        NavigateUsingRouteRequest.Builder request = NavigateUsingRouteRequest.newBuilder();
        request.setRouteName(routeName);
        if(isReroute)
        {
            request.setAcceptNewRoute(isReroute);
        }
        helper.navigateUsingRoute(request.build(), this);
    }

    public void requestMinimizeDelay()
    {
        CheckForBetterRouteRequest.Builder request = CheckForBetterRouteRequest.newBuilder();

        helper.checkForBetterRoute(request.build(), this);
    }

    public void requestBillBoardAds(String routeId)
    {
        GetBillBoardAdsRequest.Builder builder = GetBillBoardAdsRequest.newBuilder();
        
        builder.setRouteName(routeId);
        
        helper.getBillBoardAds(builder.build(), this);
    }
    
    public void registerListener(String action)
    {
        helper.registerRequestCallback(action, this);
    }

    public void removeListener(String action)
    {
        helper.removeRequestCallback(action);
    }

    protected void handleResponse(byte[] response)
    {
        // TODO Auto-generated method stub

    }

    public int getRequestId()
    {
        return this.requestId;
    }
    
    public String getTtsString()
    {
        return ttsString;
    }

    public byte[] getMp3Data()
    {
        return mp3Data;
    }

    public void setTtsString(String ttsString)
    {
        this.ttsString = ttsString;
    }

    public void setMp3Data(byte[] mp3Data)
    {
        this.mp3Data = mp3Data;
    }

    public int getTotalDelay()
    {
        return totalDelay;
    }

    public void setTotalDelay(int totalDelay)
    {
        this.totalDelay = totalDelay;
    }

    public int getTotalIncident()
    {
        return totalIncident;
    }

    public void setTotalIncident(int totalIncident)
    {
        this.totalIncident = totalIncident;
    }

    public TrafficSegment[] getTrafficSegments()
    {
        return segments;
    }

    public void setTrafficSegments(TrafficSegment[] segments)
    {
        this.segments = segments;
    }

    public RouteSummary getSummary()
    {
        return summary;
    }

    public void setSummary(RouteSummary summary)
    {
        this.summary = summary;
    }

    public void setStaticEta(Vector staticEta)
    {
        this.staticEta = staticEta;
    }

    public Vector getStaticEta()
    {
        return this.staticEta;
    }

    public Vector getDynmicEta()
    {
        return dynmicEta;
    }

    public void setDynmicEta(Vector dynmicEta)
    {
        this.dynmicEta = dynmicEta;
    }

    public Route[] getChoices()
    {
        return routeChoices;
    }

    public int[] getRouteChoiceETA()
    {
        return routeChoicesEta;
    }

    public int[] getRouteChoicesTrafficDelay()
    {
        return routeChoicesTrafficDelay;
    }

    public int[] getRouteChoicesLength()
    {
        return routeChoicesLength;
    }

    public void setChoices(Route[] routeChoices)
    {
        this.routeChoices = routeChoices;
    }
    
    public void setRequestId(int requestId)
    {
        this.requestId = requestId;
    }

    public void setRouteChoiceETA(int[] routeChoicesEta)
    {
        this.routeChoicesEta = routeChoicesEta;
    }

    public void setRouteChoicesTrafficDelay(int[] routeChoicesTrafficDelay)
    {
        this.routeChoicesTrafficDelay = routeChoicesTrafficDelay;
    }

    public void setRouteChoicesLength(int[] routeChoicesLength)
    {
        this.routeChoicesLength = routeChoicesLength;
    }

    public NavSdkNavEvent getNavEvent()
    {
        return navEvent;
    }

    public void setNavEvent(NavSdkNavEvent navEvent)
    {
        this.navEvent = navEvent;
    }

    public void setTrafficAlertEvent(TrafficAlertEvent event)
    {
        this.trafficAlertEvent = event;
    }

    public TrafficAlertEvent getTrafficAlertEvent()
    {
        return this.trafficAlertEvent;
    }
    
    public void setBillBoardAds(Vector<BillboardAd> billBoardAds)
    {
        this.billBoardAds = billBoardAds;
    }
    
    public Vector<BillboardAd> getBillboardAds()
    {
        return this.billBoardAds;
    }

    public VehiclePosition getVehiclePoistion()
    {
        return vehiclePoistion;
    }

    public void setVehiclePoistion(VehiclePosition vehiclePoistion)
    {
        this.vehiclePoistion = vehiclePoistion;
    }

    public VoiceGuidancePlayNotification getVoiceGuidance()
    {
        return voiceGuidance;
    }

    public void setVoiceGuidance(VoiceGuidancePlayNotification voiceGuidance)
    {
        this.voiceGuidance = voiceGuidance;
    }

    private void addDestinationType(NavigationParameters.Builder param, Address destAddress)
    {
        Stop destStop = destAddress.getStop();
        if (destStop != null && destStop.getType() != 0)
        {
            param.addDestinationType(destStop.getType());
        }
        else
        {
            if(destAddress.getType() == Address.TYPE_FAVORITE_POI || destAddress.getType() == Address.TYPE_FAVORITE_STOP || destAddress.getType() == Address.TYPE_FAVORITE)
            {
                param.addDestinationType(Stop.STOP_FAVORITE);
            }
            else if(destAddress.getType() == Address.TYPE_RECENT_POI || destAddress.getType() == Address.TYPE_RECENT_STOP || destAddress.getType() == Address.TYPE_RECENT)
            {
                param.addDestinationType(Stop.STOP_RECENT);
            }
            else if(destAddress.getType() == Address.TYPE_AIRPORT)
            {
                param.addDestinationType(Stop.STOP_AIRPORT);
            }
            else if(destAddress.getType() == Address.TYPE_HOME || destAddress.getType() == Address.TYPE_RECENT_STOP || destAddress.getType() == Address.TYPE_WORK)
            {
                param.addDestinationType(Stop.STOP_GENERIC);
            }
            else
            {
                param.addDestinationType(Stop.STOP_GENERIC);
            }
        }
    }
}

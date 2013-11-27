/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavigationController.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.nav.navigation;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseModel;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseView;
import com.telenav.carconnect.provider.tnlink.module.AbstractController;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.carconnect.provider.tnlink.module.nav.NavCommonConvert;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.Segment;
import com.telenav.module.nav.NavParameter;
import com.telenav.mvc.Parameter;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.NavigationEvents;
import com.telenav.navsdk.events.PointOfInterestData;
import com.telenav.navsdk.events.NavigationData.RouteSummary;
import com.telenav.navsdk.events.NavigationEvents.CancelLimitExceeded;
import com.telenav.navsdk.events.NavigationEvents.SpeedLimitExceeded;


/**
 *@author xiangli
 *@date 2012-2-22
 */
public class NavigationController extends AbstractController implements INavigationConstants
{
    private Parameter navParameter;
    private boolean isSpeedExceeded=false;
    
    public NavigationController(Parameter navParameter)
    {
        this.navParameter =  navParameter;
    }
    
    @Override
    protected AbstractBaseModel createModel()
    {
        // TODO Auto-generated method stub
        return new NavigationModel();
    }

    @Override
    protected AbstractBaseView createView()
    {
        // TODO Auto-generated method stub
        return new NavigationView();
    }
    
    private int lastRouteId = -1;
    private int lastSentSegment = -1;

    @Override
    public void postModelEvent(int eventType)
    {
        // TODO Auto-generated method stub
        MyLog.setLog("handler", "response event: " + eventType);
        switch (eventType)
        {
            case EVENT_RESULT_ROUTEDETAIL:
            {
                Route route = (Route)model.get(KEY_O_RESULT_ROUTE);
                if (null==route)
                    return;
                int routeId = route.getRouteID();
                navParameter.put(KEY_I_ROUTENAME, routeId);
                
                NavigationEvents.RouteSummaryResponse response = constructRouteDetailResponse(route, model.getBool(KEY_B_ISROUTEFINISED));
                CarConnectManager.getEventBus().broadcast("RouteSummaryResponse", response);
                break;
            }
            case EVENT_RESULT_NAVSTATUS:
            {
                NavParameter parameter = (NavParameter)model.get(KEY_O_RESULT_NAVSTATUS);
                if (null==parameter)
                    return;
                checkSpeed(parameter);
                NavigationEvents.NavigationStatus status = updateNavigationStatus(parameter);
                CarConnectManager.getEventBus().broadcast("NavigationStatus", status);
                break;
            }
            case EVENT_RESULT_ARRIVED:
            {
                MyLog.setLog("navigation", "destnation arrived");
                Segment lastSegment = (Segment)get(KEY_O_LASTSEGMENT);
                NavigationEvents.NavigationStatus status =  destinationArrived(lastSegment);
                CarConnectManager.getEventBus().broadcast("NavigationStatus", status);
                break;
            }
            case EVENT_RESULT_ERROR:
            {
                int errorCode = model.getInt(KEY_I_ERROR_CODE);
                String errorMsg = model.getString(KEY_S_ERROR_MSG);
                NavigationEvents.NavigateUsingRouteError response = constructRouteError(errorCode, errorMsg);
                
                CarConnectManager.getEventBus().broadcast(CarConnectEvent.NAVIGATION_USING_ROUTE_ERROR, response);
                break;
            }
            case EVENT_RESULT_DEVIATION:
            {
                Route route = (Route)model.get(KEY_O_RESULT_ROUTE);
                if (null==route)
                    return;
                NavigationEvents.NavigationDeviation response = constructDeviation(route);
                CarConnectManager.getEventBus().broadcast("NavigationDeviation", response);
                break;
            }
            case EVENT_RESULT_ROUTESUMMARY:
            {
                Route route = (Route)model.get(KEY_O_RESULT_ROUTE);
                if (null==route)
                    return;
                int routeId = model.getInt(KEY_I_ROUTENAME);
                NavigationEvents.RouteSummaryResponse response = constructRouteSummaryResponse(route, routeId);
                CarConnectManager.getEventBus().broadcast(CarConnectEvent.ROUTE_SUMMARY_RESPONSE, response);
                break;
            }
            case EVENT_RESULT_START_NAVIGATION:
            {
                int routeId = model.getInt(KEY_I_ROUTENAME);;
                NavigationEvents.NavigateUsingRouteResponse.Builder builder = NavigationEvents.NavigateUsingRouteResponse.newBuilder();
                builder.setRouteName(String.valueOf(routeId));
                builder.setStatus(NavigationData.NavigationStatusCode.NavigationStatus_OK);
                CarConnectManager.getEventBus().broadcast(
                    CarConnectEvent.NAVIGATION_USING_ROUTE_RESPONSE, builder.build());
                break;
            }
        }
    }
    
    private NavigationEvents.RouteSummaryResponse constructRouteDetailResponse(
            Route route, boolean isRouteFinished)
    {
        NavigationEvents.RouteSummaryResponse.Builder builder = NavigationEvents.RouteSummaryResponse
                .newBuilder();
        int routeId = route.getRouteID();
        boolean firstRoute = false;
        if (lastRouteId != routeId)
        {
            lastSentSegment = -1;
            lastRouteId = routeId;
            firstRoute = true;
        }
        NavCommonConvert.LastSentSegment lastSeg = new NavCommonConvert.LastSentSegment();
        NavigationData.RouteSummary.Builder summaryBuilder = NavCommonConvert
                .convertRouteToRouteSummary(route, lastSentSegment + 1, lastSeg, firstRoute);
        lastSentSegment = lastSeg.index;
        builder.setSummary(summaryBuilder);
        builder.setIsFinished(isRouteFinished);
        return builder.build();
    }
    
    private void checkSpeed(NavParameter parameter)
    {
        if(parameter==null) return;
        int speed=parameter.speed;
        int speedLimit=parameter.speedLimit;
        if(speed >= speedLimit && speedLimit >0 ){
            isSpeedExceeded=true;
            SpeedLimitExceeded.Builder builder=SpeedLimitExceeded.newBuilder();
            builder.setStreetSpeedLimit(parameter.speedLimitKph);
            EventBus bus=CarConnectManager.getEventBus();
            if(bus!=null) bus.broadcast("SpeedLimitExceeded", builder.build());
        }else {
            if(!isSpeedExceeded) return;
            isSpeedExceeded=false;
            CancelLimitExceeded.Builder builder=CancelLimitExceeded.newBuilder();
            EventBus bus=CarConnectManager.getEventBus();
            if(bus!=null) bus.broadcast("CancelLimitExceeded", builder.build());
        }
    }
    
    private NavigationEvents.NavigationStatus updateNavigationStatus(NavParameter parameter)
    {
        int []laneInfor = parameter.laneInfos;
        int []laneTypes = parameter.laneTypes;
        
        NavigationEvents.NavigationStatus.Builder builder = NavigationEvents.NavigationStatus.newBuilder();
        if (parameter.currStreetName != null) builder.setCurrentStreetName(parameter.currStreetName);
        builder.setCurrentSpeedLimit(parameter.speedLimitKph);
        
        NavigationEvents.RouteProgress.Builder progressBuilder = NavigationEvents.RouteProgress.newBuilder();
        progressBuilder.setMetersToDestination(parameter.totalToDest);
        progressBuilder.setMetersToNextManeuver(parameter.distanceToTurn);
        progressBuilder.setNextTurnIndex(parameter.nextSegmentIndex);
        progressBuilder.setSecondsToDestination(parameter.eta);
        builder.setRouteProgress(progressBuilder);

        if (laneInfor != null && laneTypes!=null)
        {
            for (int i = 0; i < laneInfor.length; ++i)
            {
                NavigationData.LaneAssistInformation.Builder laneBuilder = NavigationData.LaneAssistInformation
                        .newBuilder();
                laneBuilder.setLaneIsInRoute(laneInfor[i] == 1);
                laneBuilder.setLaneTurnType(NavigationData.RouteTurnType
                        .valueOf(laneTypes[i]));
                builder.addLaneAssistInfo(laneBuilder);
            }
        }
        
        NavigationData.RouteTurn.Builder turnBuilder = NavigationData.RouteTurn.newBuilder();
        turnBuilder.setCurrentLeg(false);
        turnBuilder.setDisplayStreetName(parameter.nextStreetName);
        turnBuilder.setTurnType(NavigationData.RouteTurnType.valueOf(parameter.turnType));
        builder.setNextTurn(turnBuilder);
        builder.setStatus(NavigationData.NavigationRouteStatusCode.NavigationStatus_OnRoute);
        
        return builder.build();
    }
    
    private NavigationEvents.NavigationStatus destinationArrived(Segment lastSegment)
    {
        NavigationEvents.NavigationStatus.Builder builder = NavigationEvents.NavigationStatus
                .newBuilder();
        NavigationEvents.RouteProgress.Builder progressBuilder = NavigationEvents.RouteProgress
                .newBuilder();
        progressBuilder.setMetersToDestination(0);
        progressBuilder.setMetersToNextManeuver(0);
        progressBuilder.setSecondsToDestination(0);
        builder.setRouteProgress(progressBuilder);
        
        NavigationData.RouteTurn.Builder turnBuilder = NavigationData.RouteTurn.newBuilder();
        turnBuilder.setDistanceToTurnInMeters(0);
        if(lastSegment!=null)
        { 
            String streetName=lastSegment.getStreetName();
            if(streetName!=null)
            {
                turnBuilder.setDisplayStreetName(streetName);
            }
            turnBuilder.setTurnType(NavigationData.RouteTurnType.valueOf(lastSegment.getTurnType()));
        }
        turnBuilder.setSecondsToTurn(0);
        builder.setNextTurn(turnBuilder);
        
        builder.setStatus(NavigationData.NavigationRouteStatusCode.NavigationStatus_Arrived);
        
        return builder.build();
    }
    
    private NavigationEvents.NavigateUsingRouteError constructRouteError(int errorCode, String errorMsg)
    {
        NavigationEvents.NavigateUsingRouteError.Builder builder = NavigationEvents.NavigateUsingRouteError.newBuilder();
        builder.setError(NavigationData.NavigationErrorCode.valueOf(errorCode));
        if (errorMsg!=null)
            builder.setErrorString(errorMsg);
        return builder.build();
    }
    
    private NavigationEvents.NavigationDeviation constructDeviation(Route route)
    {
        NavigationEvents.NavigationDeviation.Builder builder = NavigationEvents.NavigationDeviation.newBuilder();
        NavigationData.RouteSummary.Builder summaryBuilder = NavCommonConvert
        .convertRouteToRouteSummary(route, 0, null, true);
        builder.setNewRoute(summaryBuilder);
        return builder.build();
    }
    
    private NavigationEvents.RouteSummaryResponse constructRouteSummaryResponse(Route route, int routeId)
    {
        NavigationEvents.RouteSummaryResponse.Builder builder = NavigationEvents.RouteSummaryResponse.newBuilder();
        NavigationData.RouteSummary.Builder summaryBuilder = NavCommonConvert
        .convertRouteToRouteSummaryWithoutPoint(route);
        builder.setSummary(summaryBuilder);
        builder.setRouteName(String.valueOf(routeId));
        builder.setError(NavigationData.NavigationStatusCode.NavigationStatus_OK);
        return builder.build();
    }
}

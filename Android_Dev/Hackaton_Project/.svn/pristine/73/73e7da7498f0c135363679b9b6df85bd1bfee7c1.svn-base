/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavigationProvider.java
 *
 */
package com.telenav.carconnect.provider.tnlink;


import com.telenav.app.CommManager;
import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.LogUtil;
import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.carconnect.provider.ProtocolConvertor;
import com.telenav.carconnect.provider.tnlink.module.ModuleFactory;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.carconnect.provider.tnlink.module.nav.navigation.INavigationConstants;
import com.telenav.carconnect.provider.tnlink.module.nav.navigation.NavigationController;
import com.telenav.carconnect.provider.tnlink.module.nav.routeplan.IRoutePlanConstants;
import com.telenav.carconnect.provider.tnlink.module.nav.routeplan.RoutePlanModel;
import com.telenav.carconnect.provider.tnlink.module.traffic.ProtoBufTrafficProxy;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IMissingAudioProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.TrafficMissingAudioProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.Route;
import com.telenav.mvc.Parameter;
import com.telenav.nav.NavEngine;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.NavigationData.NavigationParameters;
import com.telenav.navsdk.events.NavigationData.RouteComputationMode;
import com.telenav.navsdk.events.NavigationEvents;
import com.telenav.navsdk.events.PointOfInterestData;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailError;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.threadpool.Notifier;

/**
 *@author xiangli
 *@date 2012-3-22
 */
public class NavigationProvider extends AbstractProvider implements INavigationConstants, IServerProxyListener
{
    private String[] events =
    { CarConnectEvent.START_NAVIGATION_REQUEST, CarConnectEvent.STOP_NAVIGATION_REQUEST,
            CarConnectEvent.ROUTE_PLAN_REQUEST, CarConnectEvent.ROUTE_SUMMARY_REQUEST,
            CarConnectEvent.NAVIGATION_USING_ROUTE_REQUEST,
            CarConnectEvent.TRAFFIC_DETAIL_REQUEST, CarConnectEvent.GUIDANCE_REQUEST, "TRANSFER_NAV" };
    
    private Parameter navParameter = new Parameter();
    
    @Override
    public void handle(String eventType, Object eventData)
    {
        if (null==eventData)
            return;
        
        MyLog.setLog("Traffic", "event name: " + eventType);
        // TODO Auto-generated method stub
        try
        {
            if (CarConnectEvent.ROUTE_PLAN_REQUEST.equals(eventType))
            {
                 NavigationEvents.RoutePlanRequest request = (NavigationEvents.RoutePlanRequest) eventData;
                NavigationData.NavigationParameters parameters = request.getNav();
                int routeStyle = getRouteStyle(parameters);
                int routeSettings = getRouteSettings(parameters);
                
                if (parameters.getFirstPOIisOrigin())
                {
                    PointOfInterestData.PointOfInterest origPoi = parameters.getWaypoints(0);
                    PointOfInterestData.PointOfInterest destPoi = parameters.getWaypoints(1);
                    Address origAddr = ProtocolConvertor.convertPointOfInterestToAddress(origPoi);
                    Address destAddr = ProtocolConvertor.convertPointOfInterestToAddress(destPoi);
                    ModuleFactory.getInstance().startRoutePlanController(origAddr,
                        destAddr, routeStyle, routeSettings, navParameter);
                }
                else
                {
                    PointOfInterestData.PointOfInterest destPoi = parameters.getWaypoints(0);
                    Address destAddr = ProtocolConvertor.convertPointOfInterestToAddress(destPoi);
                    ModuleFactory.getInstance().startRoutePlanController(null,
                        destAddr, routeStyle, routeSettings, navParameter);
                }
                
                
            }
            else if (eventType.equals(CarConnectEvent.NAVIGATION_USING_ROUTE_REQUEST))
            {
                NavigationEvents.NavigateUsingRouteRequest request = (NavigationEvents.NavigateUsingRouteRequest) eventData;
                Stop destStop = (Stop) navParameter
                        .get(IRoutePlanConstants.KEY_O_DEST_STOP);
                if (null == destStop)
                {
                    return;
                }

                int routeId = -1;
                if (request.hasRouteName())
                {
                    routeId = Integer.valueOf(request.getRouteName());
                }
                ModuleFactory.getInstance().startNavigationController(destStop,
                    routeId, navParameter);
            }
            else if (CarConnectEvent.STOP_NAVIGATION_REQUEST.equals(eventType))
            {
                Object controller = navParameter.get(KEY_O_NAVCONTROLLER);
                if (controller instanceof NavigationController)
                {
                    NavigationController navController = (NavigationController) controller;
                    navController.start(ACTION_NAVIGATION_STOP);
                }
            }
            else if (CarConnectEvent.START_NAVIGATION_REQUEST.equals(eventType))
            {
                Object controller = navParameter.get(KEY_O_NAVCONTROLLER);
                if (controller instanceof NavigationController)
                {
                    NavigationController navController = (NavigationController) controller;
                    NavigationEvents.StartNavigationRequest request = (NavigationEvents.StartNavigationRequest) eventData;
                    NavigationData.NavigationParameters navParameter = request.getNav();
                    Parameter parameter = new Parameter();
                    parameter.put(RoutePlanModel.KEY_I_ROUTE_STYLE, getRouteStyle(navParameter));
                    if (navParameter.getFirstPOIisOrigin())
                    {
                        PointOfInterestData.PointOfInterest origPoi = navParameter.getWaypoints(0);
                        PointOfInterestData.PointOfInterest destPoi = navParameter.getWaypoints(1);
                        Address origAddr = ProtocolConvertor.convertPointOfInterestToAddress(origPoi);
                        Address destAddr = ProtocolConvertor.convertPointOfInterestToAddress(destPoi);

                        parameter.put(INavigationConstants.KEY_O_ADDRESS_ORI, origAddr);
                        parameter.put(INavigationConstants.KEY_O_ADDRESS_DEST, destAddr);
                    }
                    else
                    {
                        PointOfInterestData.PointOfInterest destPoi = navParameter.getWaypoints(1);
                        Address destAddr = ProtocolConvertor.convertPointOfInterestToAddress(destPoi);
                        
                        parameter.put(INavigationConstants.KEY_O_ADDRESS_ORI, null);
                        parameter.put(INavigationConstants.KEY_O_ADDRESS_DEST, destAddr);
                    }

                    navController.init(parameter);
                    navController
                            .start(INavigationConstants.ACTION_NAVIGATION_START_INNAVIGATION);
                }
            }
            else if (CarConnectEvent.TRAFFIC_DETAIL_REQUEST.equals(eventType))
            {
                int routeId = navParameter.getInt(KEY_I_ROUTENAME);
                boolean isCameraTurnOn = true;
                boolean isSpeedTrapOn = true;
                IMissingAudioProxy missingAudioProxy = ServerProxyFactory.getInstance().createMissingAudioProxy(null, CommManager.getInstance().getComm(), this);
                TrafficMissingAudioProxy trafficMissingAudioProxy = ServerProxyFactory.getInstance().createTrafficMissingAudioProxy(Notifier.getInstance(), missingAudioProxy);
                ProtoBufTrafficProxy trafficProxy = new ProtoBufTrafficProxy(null,CommManager.getInstance().getComm(), this, isCameraTurnOn, isSpeedTrapOn, trafficMissingAudioProxy,null);
                NavState currentNavState;
                currentNavState = NavEngine.getInstance().getCurrentNavState();
              int routeSegmentIndex;
              int edgeIndex;
              if(currentNavState != null)
              {
                  routeSegmentIndex = currentNavState.getSegmentIndex();
                  edgeIndex = currentNavState.getEdgeIndex();
              }
              else
              {
                  routeSegmentIndex = 0;
                  edgeIndex = 0;
              }
              trafficProxy.requestTrafficSummary(routeId, routeSegmentIndex, edgeIndex, null);
            
            }
            else if (CarConnectEvent.ROUTE_SUMMARY_REQUEST.equals(eventType))
            {
                Object controller = navParameter.get(KEY_O_NAVCONTROLLER);
                if (controller instanceof NavigationController)
                {
                    NavigationController navController = (NavigationController) controller;
                    navController.start(ACTION_NAVIGATION_ROUTESUMMARY);
                }
            }
            else if (CarConnectEvent.GUIDANCE_REQUEST.equals(eventType))
            {
                Object controller = navParameter.get(KEY_O_NAVCONTROLLER);
                if (controller instanceof NavigationController)
                {
                    NavigationController navController = (NavigationController) controller;
                    navController.start(ACTION_NAVIGATION_GUIDANCE);
                }
            }
            else if ("TRANSFER_NAV".equals(eventType))
            {
                Object[] a = (Object[]) eventData;
                PointOfInterest destPoi = (PointOfInterest)a[0];
                navParameter.put(IRoutePlanConstants.KEY_O_DEST_STOP, ProtocolConvertor.convertPointOfInterestToAddress(destPoi).getStop());
                handle(CarConnectEvent.NAVIGATION_USING_ROUTE_REQUEST, a[1]);
            }
        }
        catch (ClassCastException e)
        {
            MyLog.setLog("error", "class: " + e.getClass() + " " + e.getMessage());
            return;
        }
    }

    @Override
    public void register()
    {
        // TODO Auto-generated method stub
        for (String event : events)
            getEventBus().subscribe(event,this);
    }

    @Override
    public void unregister()
    {
        // TODO Auto-generated method stub
        for (String event : events)
            getEventBus().unsubscribe(event, this);
        //stop the navigation if it is still running
        Object controller = navParameter.get(KEY_O_NAVCONTROLLER);
        if (controller instanceof NavigationController)
        {
            NavigationController navController = (NavigationController) controller;
            navController.start(ACTION_NAVIGATION_STOP);
        }
        
    }

    @Override
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        // TODO Auto-generated method stub
        LogUtil.logInfo("Traffic","transactionFinished");
    }

    @Override
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        // TODO Auto-generated method stub
        LogUtil.logInfo("Traffic","updateTransactionStatus");
    }

    @Override
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        // TODO Auto-generated method stub
        onError();
    }

    @Override
    public void transactionError(AbstractServerProxy proxy)
    {
        // TODO Auto-generated method stub
        onError();
    }

    @Override
    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    private void onError()
    {
        TrafficDetailError.Builder builder=TrafficDetailError.newBuilder();
        CarConnectManager.getEventBus().broadcast("TrafficDetailError", builder.build());
    }

    private int getRouteStyle(NavigationParameters navp)
    {
        int routeStyle = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
        //if (navp.hasComputationMode())
        //{
            int routeMode=navp.getComputationMode().getNumber();
            if(routeMode==RouteComputationMode.RouteComputationMode_Pedestrian_VALUE){
                routeStyle=Route.ROUTE_PEDESTRIAN;
            }else if(routeMode==RouteComputationMode.RouteComputationMode_Shortest_VALUE){
                routeStyle=Route.ROUTE_SHORTEST;
            }else if(routeMode==RouteComputationMode.RouteComputationMode_PreferStreets_VALUE){
                routeStyle=Route.ROUTE_AVOID_HIGHWAY;
            }else{
                routeStyle=Route.ROUTE_FASTEST;
            }
        //}
        return routeStyle;
    }
    
    private int getRouteSettings(NavigationParameters navp)
    {
        int settings = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_ROUTE_SETTING);
        //if (navp.hasAvoidHovLanes() || navp.hasAvoidTollRoads() || navp.hasAvoidTraffic())
        //{
            boolean avoidHovLane = navp.getAvoidHovLanes();
            boolean avoidTollRoads = navp.getAvoidTollRoads();
            boolean avoidTraffic = navp.getAvoidTraffic();
            settings = 0;
            if (avoidHovLane)
                settings += Preference.AVOID_CARPOOL_LANES;
            if (avoidTollRoads)
                settings += Preference.AVOID_TOLLS;
            if (avoidTraffic)
                settings += Preference.AVOID_TRAFFIC_DELAYS;
        //}
        
        return settings;
    }
}

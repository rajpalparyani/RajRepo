/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkNavigationProxyHelper.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.text.TextUtils;

import com.telenav.app.ThreadManager;
import com.telenav.data.datatypes.poi.BillboardAd;
import com.telenav.data.datatypes.poi.GeoFence;
import com.telenav.data.datatypes.route.NavSdkRoute;
import com.telenav.data.datatypes.route.NavSdkRouteDataFactory;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteDataFactory;
import com.telenav.datatypes.route.Segment;
import com.telenav.datatypes.traffic.TrafficDataFactory;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.nav.RouteSummaryManager;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.NavigationData.BillBoardAds;
import com.telenav.navsdk.events.NavigationData.EtaResult;
import com.telenav.navsdk.events.NavigationData.LaneAssistInformation;
import com.telenav.navsdk.events.NavigationData.NavigationErrorCode;
import com.telenav.navsdk.events.NavigationData.RoutePlan;
import com.telenav.navsdk.events.NavigationData.RouteProgress;
import com.telenav.navsdk.events.NavigationData.RouteSummary;
import com.telenav.navsdk.events.NavigationData.RouteTurn;
import com.telenav.navsdk.events.NavigationData.TrafficDetail;
import com.telenav.navsdk.events.NavigationEvents.AvoidIncidentAheadError;
import com.telenav.navsdk.events.NavigationEvents.AvoidIncidentAheadRequest;
import com.telenav.navsdk.events.NavigationEvents.AvoidIncidentAheadResponse;
import com.telenav.navsdk.events.NavigationEvents.CancelAvoidIncidentAheadRequest;
import com.telenav.navsdk.events.NavigationEvents.CancelLimitExceeded;
import com.telenav.navsdk.events.NavigationEvents.CancelRoutePlanRequest;
import com.telenav.navsdk.events.NavigationEvents.CheckForBetterRouteError;
import com.telenav.navsdk.events.NavigationEvents.CheckForBetterRouteRequest;
import com.telenav.navsdk.events.NavigationEvents.CheckForBetterRouteResponse;
import com.telenav.navsdk.events.NavigationEvents.EtaError;
import com.telenav.navsdk.events.NavigationEvents.EtaRequest;
import com.telenav.navsdk.events.NavigationEvents.EtaResponse;
import com.telenav.navsdk.events.NavigationEvents.GetBillBoardAdsError;
import com.telenav.navsdk.events.NavigationEvents.GetBillBoardAdsRequest;
import com.telenav.navsdk.events.NavigationEvents.GetBillBoardAdsResponse;
import com.telenav.navsdk.events.NavigationEvents.Guidance;
import com.telenav.navsdk.events.NavigationEvents.GuidanceRequest;
import com.telenav.navsdk.events.NavigationEvents.NavigateUsingRouteError;
import com.telenav.navsdk.events.NavigationEvents.NavigateUsingRouteRequest;
import com.telenav.navsdk.events.NavigationEvents.NavigateUsingRouteResponse;
import com.telenav.navsdk.events.NavigationEvents.NavigationDeviation;
import com.telenav.navsdk.events.NavigationEvents.NavigationDeviationError;
import com.telenav.navsdk.events.NavigationEvents.NavigationStatus;
import com.telenav.navsdk.events.NavigationEvents.RoutePlanError;
import com.telenav.navsdk.events.NavigationEvents.RoutePlanRequest;
import com.telenav.navsdk.events.NavigationEvents.RoutePlanResponse;
import com.telenav.navsdk.events.NavigationEvents.RouteSummaryError;
import com.telenav.navsdk.events.NavigationEvents.RouteSummaryRequest;
import com.telenav.navsdk.events.NavigationEvents.RouteSummaryResponse;
import com.telenav.navsdk.events.NavigationEvents.SpeedLimitExceeded;
import com.telenav.navsdk.events.NavigationEvents.StartNavigationError;
import com.telenav.navsdk.events.NavigationEvents.StartNavigationRequest;
import com.telenav.navsdk.events.NavigationEvents.StartNavigationResponse;
import com.telenav.navsdk.events.NavigationEvents.StopNavigationError;
import com.telenav.navsdk.events.NavigationEvents.StopNavigationRequest;
import com.telenav.navsdk.events.NavigationEvents.StopNavigationResponse;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailError;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailRequest;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailResponse;
import com.telenav.navsdk.events.NavigationEvents.TrafficIncidentsAhead;
import com.telenav.navsdk.events.NavigationEvents.TrafficInfoRequest;
import com.telenav.navsdk.events.NavigationEvents.VehicleDataUpdate;
import com.telenav.navsdk.events.NavigationEvents.VehiclePosition;
import com.telenav.navsdk.events.NavigationEvents.VoiceGuidancePlayNotification;
import com.telenav.navsdk.nav.event.NavSdkNavEvent;
import com.telenav.navsdk.services.NavigationListener;
import com.telenav.navsdk.services.NavigationServiceProxy;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.threadpool.IJob;

/**
 * @author hchai
 * @date 2011-12-1
 */
public class NavSdkNavigationProxyHelper implements NavigationListener,
        INavSdkProxyConstants
{
    private static NavSdkNavigationProxyHelper instance;

    private NavigationServiceProxy serverProxy;

    private HashMap<String, NavSdkNavigationProxy> listeners = new HashMap<String, NavSdkNavigationProxy>();

    private NavSdkNavigationProxyHelper()
    {
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavSdkNavigationProxyHelper();
            instance.setEventBus(bus);
        }
    }

    public static NavSdkNavigationProxyHelper getInstance()
    {
        return instance;
    }

    public void registerRequestCallback(String action, NavSdkNavigationProxy proxy)
    {
        listeners.put(action, proxy);
    }
    
    public void removeRequestCallback(String action)
    {
        listeners.remove(action);
    }

    private void setEventBus(EventBus bus)
    {
        serverProxy = new NavigationServiceProxy(bus);
        serverProxy.addListener(this);

    }

    public void onStartNavigationResponse(StartNavigationResponse event)
    {
        String action = ACT_NAV_START_NAVIGATION;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            try
            {
                NavSdkRouteWrapper.getInstance().setCurrentRouteId(Integer.parseInt(event.getRouteName()));
            }
            catch (NumberFormatException nfe)
            {
                Logger.log(Logger.ERROR, this.getClass().getName(), "Route ID is invalid!!!!");
            }
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onStartNavigationError(StartNavigationError event)
    {
        String action = ACT_NAV_START_NAVIGATION;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    public void onEtaResponse(EtaResponse event)
    {
        String action = ACT_NAV_ETA;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            Vector staticEta = new Vector();
            Vector dynmicEta = new Vector();
            for(int i = 0; i < event.getEtaResultsCount(); i++)
            {
                EtaResult result = event.getEtaResults(i);
                staticEta.addElement(result.getStaticEta());
                dynmicEta.addElement(result.getDynamicEta());
            }
            proxy.setStaticEta(staticEta);
            proxy.setDynmicEta(dynmicEta);
            proxy.setRequestId(event.getRequestId());
            proxy.transactionFinished(action, proxy);
        }
    }

    @Override
    public void onEtaError(EtaError event)
    {
        String action = ACT_NAV_ETA;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    @Override
    public void onNavigateUsingRouteResponse(NavigateUsingRouteResponse event)
    {
        String action = ACT_NAV_NAVIGATE_USING_ROUTE;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            try
            {
                NavSdkRouteWrapper.getInstance().setCurrentRouteId(Integer.parseInt(event.getRouteName()));
            }
            catch (NumberFormatException nfe)
            {
                Logger.log(Logger.ERROR, this.getClass().getName(), "Route ID is invalid!!!!");
            }
            proxy.transactionFinished(action, proxy);
        }
    }

    @Override
    public void onNavigateUsingRouteError(NavigateUsingRouteError event)
    {
        String action = ACT_NAV_NAVIGATE_USING_ROUTE;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    @Override
    public void onNavigationDeviation(NavigationDeviation event)
    {
        String action = ACT_NAV_NAVIGATION_DEVIATION;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    @Override
    public void onNavigationDeviationError(NavigationDeviationError event)
    {
        String action = ACT_NAV_NAVIGATION_DEVIATION;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    @Override
    public void onStopNavigationResponse(StopNavigationResponse event)
    {
        String action = ACT_NAV_STOP_NAVIGATION;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    @Override
    public void onStopNavigationError(StopNavigationError event)
    {
        String action = ACT_NAV_STOP_NAVIGATION;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    @Override
    public void onRouteSummaryError(RouteSummaryError event)
    {
        String action = ACT_NAV_ROUTE_SUMMARY;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    public void onRouteSummaryResponse(RouteSummaryResponse event)
    {
        String action = ACT_NAV_ROUTE_SUMMARY;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            Route route = NavSdkNavigationUtil.convertRoute(event.getSummary(), true);
            NavSdkRouteWrapper.getInstance().clear();
            NavSdkRouteWrapper.getInstance().addRoute(route);
            route.setCompeleted(event.getIsFinished());
            NavSdkRouteWrapper.getInstance().setCurrentRouteId(route.getRouteID());

            proxy.transactionFinished(action, proxy);

            //Why use thread here?
            //Avoid block event bus main thread, RouteSummaryManager will notify module to update.
            //Maybe app level has heavy process. 
            ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION).addJob(new IJob()
            {
                boolean isRunning = false;

                public boolean isRunning()
                {
                    return isRunning;
                }

                public boolean isCancelled()
                {
                    return false;
                }

                public void execute(int handlerID)
                {
                    isRunning = true;
                    try
                    {
                        RouteSummaryManager.getInstance().routeSummaryUpdated();
                    }
                    catch (Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    isRunning = false;
                }

                public void cancel()
                {

                }
            });
        }
    }

    public void onRoutePlanResponse(RoutePlanResponse event)
    {
        String action = ACT_NAV_ROUTE_PLAN;
        NavSdkNavigationProxy proxy = listeners.get(action);
        Logger.log(Logger.INFO, this.getClass().getName(), "ROUTE_PLAN: ==== route plan response comes, request id: " + event.getRequestId());
        if (proxy != null)
        {
            int requestId = proxy.getRequestId();

            Logger.log(Logger.INFO, this.getClass().getName(), "ROUTE_PLAN: ==== current Proxy request id is: " + requestId);
            
            if (requestId != event.getRequestId())
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "ROUTE_PLAN: ==== id different, drop it");
                return;
            }

            Logger.log(Logger.INFO, this.getClass().getName(), "ROUTE_PLAN: ==== id same!!!!!!");
            listeners.remove(action);

            RoutePlan routePlan = event.getPlan();
            handleRoute(routePlan, proxy, true);

            proxy.transactionFinished(action, proxy);
        }
    }

    @Override
    public void onRoutePlanError(RoutePlanError event)
    {
        String action = ACT_NAV_ROUTE_PLAN;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            int requestId = proxy.getRequestId();
            
            if(requestId != event.getRequestId())
            {
                return;
            }
            
            listeners.remove(action);
            
            proxy.setErrorMsg(event.getErrorString());
            if (event.getErrorString() != null && event.getErrorString().trim().length() == 0 && event.getError() != null && event.getError().toString() != null && event.getError().toString().equals(ONBOARD_ROUTE_PLANNING_ERROR))
            {
                proxy.setErrorMsg(ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ONBOARD_ROUTE_PLANNING_ERROR, IStringNav.FAMILY_NAV));
            }
            proxy.transactionError(action, proxy);
        }
    }

    @Override
    public void onCheckForBetterRouteResponse(CheckForBetterRouteResponse event)
    {
        String action = ACT_NAV_CHECK_FOR_BETTER_ROUTE;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            RoutePlan routePlan = event.getPossibleRoutes();
            int routeCount = routePlan != null ? routePlan.getRoutesCount() : 0;
            if (routeCount > 0)
            {
                NavSdkRoute[] routeChoices = new NavSdkRoute[routeCount];
                int[] routeChoicesEta = new int[routeCount];
                int[] routeChoicesTrafficDelay = new int[routeCount];
                int[] routeChoicesLength = new int[routeCount];
                
                for (int i = 0; i < routeCount; i++)
                {
                    RouteSummary routeSummary = routePlan.getRoutes(i);
                    routeChoices[i] = NavSdkNavigationUtil.convertRoute(routeSummary, false);
                    routeChoicesEta[i] = (int)routeSummary.getTotalTimeInSeconds();//For compatibility, is Int big enough?
                    routeChoicesTrafficDelay[i] = 0;
                    routeChoicesLength[i] = (int)routeSummary.getTotalDistanceInMeters();//For compatibility, is Int big enough?
                }
                proxy.setChoices(routeChoices);
                proxy.setRouteChoiceETA(routeChoicesEta);
                proxy.setRouteChoicesTrafficDelay(routeChoicesTrafficDelay);
                proxy.setRouteChoicesLength(routeChoicesLength);
            }
            proxy.transactionFinished(action, proxy);
        }
    }

    @Override
    public void onCheckForBetterRouteError(CheckForBetterRouteError event)
    {
        String action = ACT_NAV_CHECK_FOR_BETTER_ROUTE;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            if (!TextUtils.isEmpty(event.getErrorString()))
            {
                proxy.setErrorMsg(event.getErrorString());
            }
            else
            {
                proxy.setErrorMsg(NavSdkNavigationUtil.convertError(event.getError()));
            }
            proxy.transactionError(action, proxy);
        }
    }

    public void onTrafficIncidentsAhead(TrafficIncidentsAhead event)
    {
        String action = ACT_NAV_TRAFFIC_INCIDENTS_AHEAD;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            proxy.setTrafficAlertEvent(NavSdkNavigationUtil.convertTrafficAlert(event));
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onTrafficDetailResponse(TrafficDetailResponse event)
    {
        String action = ACT_NAV_TRAFFIC_DETAIL;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            int totalIncidentCounts = 0;
            proxy.setTotalDelay(event.getTrafficDelayInSeconds());
            List<TrafficDetail> list = event.getTrafficDetailsList();
            int size = list.size();
            TrafficSegment[] trafficSegments = new TrafficSegment[size];
            for (int i = 0; i < size; i++)
            {
                TrafficDetail detail = list.get(i);

                TrafficSegment trafficSegment = TrafficDataFactory.getInstance()
                        .createTrafficSegment();
                trafficSegment.setName(convertSteetName(detail.getDisplayStreetName()));
                trafficSegment.setAvgSpeed(detail.getLegSpeed() < 0? -1 : (int)detail.getLegSpeed());
                trafficSegment.setPostedSpeed((int)detail.getLegFreeFlowSpeed());
                trafficSegment.setLength((int)detail.getTotalLegDistanceInMeters());

                int incidentsCount = detail.getIncidentsCount();
                totalIncidentCounts += incidentsCount;
                TrafficIncident[] incidents = new TrafficIncident[incidentsCount];

                for (int j = 0; j < detail.getIncidentsCount(); j++)
                {
                    com.telenav.navsdk.events.NavigationData.TrafficIncident sdkTrafficIncident = (com.telenav.navsdk.events.NavigationData.TrafficIncident) detail
                            .getIncidentsList().get(j);
                    incidents[j] = TrafficDataFactory.getInstance().createTrafficIncident();
                    incidents[j].setId(sdkTrafficIncident.getIncidentId());
                    incidents[j].setMsg(sdkTrafficIncident.getShortSummary());
                    incidents[j].setDisplayInfo(sdkTrafficIncident.getIncidentdetails());
                    incidents[j].setDelay(sdkTrafficIncident.getDelayInSeconds());
                    incidents[j].setIncidentType((byte) sdkTrafficIncident.getIncidentType().getNumber());
//                    nominalIncidents[j].setXXX(sdkTrafficIncident.getImageURL());//DRAFT hchai need mapping
                }
                trafficSegment.setIncidents(incidents);
                trafficSegments[i] = trafficSegment;
            }
            proxy.setTotalIncident(totalIncidentCounts);
            proxy.setTrafficSegments(trafficSegments);
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onTrafficDetailError(TrafficDetailError event)
    {
        String action = ACT_NAV_TRAFFIC_DETAIL;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    public void onNavigationStatus(NavigationStatus event)
    {
        String action = ACT_NAV_NAVIGATION_STATUS;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            NavSdkNavEvent navEvent = NavSdkNavigationUtil.createNavsdkNavEvent(event);
            proxy.setNavEvent(navEvent);
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onRouteProgress(RouteProgress event)
    {
        String action = ACT_NAV_ROUTE_PROGRESS;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onGuidance(Guidance event)
    {
        String action = ACT_NAV_GUIDANCE;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setTtsString(event.getTtsString());
            if (event.getMp3Data() != null)
            {
                proxy.setMp3Data(event.getMp3Data().toByteArray());
            }
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onSpeedLimitExceeded(SpeedLimitExceeded event)
    {
        String action = ACT_NAV_SPEED_LIMIT_EXCEEDED;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onCancelLimitExceeded(CancelLimitExceeded event)
    {
        String action = ACT_NAV_CANCEL_LIMIT_EXCEEDED;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onVehiclePosition(VehiclePosition event)
    {
        String action = ACT_NAV_VEHICLE_POSITION;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            proxy.setVehiclePoistion(event);
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onVehicleDataUpdate(VehicleDataUpdate event)
    {
        String action = ACT_NAV_VEHICLE_POSITION;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    public void startNavigation(StartNavigationRequest request,
            NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_START_NAVIGATION, proxy);
        serverProxy.startNavigation(request);
    }

    public void stopNavigation(StopNavigationRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_STOP_NAVIGATION, proxy);
        serverProxy.stopNavigation(request);
    }

    public void routeSummary(RouteSummaryRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_ROUTE_SUMMARY, proxy);
        serverProxy.routeSummary(request);
    }

    public void checkForBetterRoute(CheckForBetterRouteRequest request,
            NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_CHECK_FOR_BETTER_ROUTE, proxy);
        serverProxy.checkForBetterRoute(request);
    }

    public void trafficDetail(TrafficDetailRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_TRAFFIC_DETAIL, proxy);
        serverProxy.trafficDetail(request);
    }

    public void guidance(GuidanceRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_GUIDANCE, proxy);
        serverProxy.guidance(request);
    }
    
    public void avoidIncident(AvoidIncidentAheadRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_AVOID_TRAFFIC_INCIDENT, proxy);
        serverProxy.avoidIncidentAhead(request);
    }
    
    public void cancelAvoidIncident(CancelAvoidIncidentAheadRequest request, NavSdkNavigationProxy proxy)
    {
        serverProxy.cancelAvoidIncidentAhead(request);
    }
    
    public void trafficInfo(TrafficInfoRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_GUIDANCE, proxy);
        serverProxy.trafficInfo(request);
    }
    
    public void cancelRoutePlan(CancelRoutePlanRequest request, NavSdkNavigationProxy proxy)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "ROUTE_PLAN: ==== cancel route plan request, request id: " + request.getRequestId());
        serverProxy.cancelRoutePlan(request);
    }

    public void routePlan(RoutePlanRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_ROUTE_PLAN, proxy);
        serverProxy.routePlan(request);
    }

    public void eta(EtaRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_ETA, proxy);
        serverProxy.eta(request);
    }

    public void navigateUsingRoute(NavigateUsingRouteRequest request,
            NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_NAV_NAVIGATE_USING_ROUTE, proxy);
        serverProxy.navigateUsingRoute(request);
    }
    
    protected void handleRoute(RoutePlan routePlan, NavSdkNavigationProxy proxy, boolean isSetCurrentRouteId)
    {
        int routeCount = routePlan != null ? routePlan.getRoutesCount() : 0;
        if (routeCount > 0)
        {
            NavSdkRoute[] routeChoices = new NavSdkRoute[routeCount];
            int[] routeChoicesEta = new int[routeCount];
            int[] routeChoicesTrafficDelay = new int[routeCount];
            int[] routeChoicesLength = new int[routeCount];
            
            for (int i = 0; i < routeCount; i++)
            {
                RouteSummary routeSummary = routePlan.getRoutes(i);
                routeChoices[i] = NavSdkNavigationUtil.convertRoute(routeSummary, false);
                routeChoicesEta[i] = (int)routeSummary.getTotalTimeInSeconds();//For compatibility, is Int big enough?
                routeChoicesTrafficDelay[i] = 0;
                routeChoicesLength[i] = (int)routeSummary.getTotalDistanceInMeters();//For compatibility, is Int big enough?
            }
            proxy.setChoices(routeChoices);
            proxy.setRouteChoiceETA(routeChoicesEta);
            proxy.setRouteChoicesTrafficDelay(routeChoicesTrafficDelay);
            proxy.setRouteChoicesLength(routeChoicesLength);
            NavSdkRouteWrapper.getInstance().clear();
            NavSdkRouteWrapper.getInstance().addRoutes(routeChoices);
            if(isSetCurrentRouteId)
            {
                NavSdkRouteWrapper.getInstance().setCurrentRouteId(routeChoices[0].getRouteID());
            }
        }
    }
    
    private static String convertSteetName(String streetName)
    {
        if (streetName != null && streetName.trim().length() > 0)
        {
            return streetName;
        }

        streetName = STRING_UNKNOWN_ROAD;

        return streetName;
    }
    
    public static class NavSdkNavigationUtil
    {
        public static int convertCoordinate(double dCoordinate)
        {
            return (int) (dCoordinate * 100000);
        }
        
        public static NavSdkNavEvent createNavsdkNavEvent(NavigationStatus event)
        {
            NavSdkNavEvent navEvent = new NavSdkNavEvent(event.getStatus().getNumber());
            String currentStreetName = event.getCurrentStreetName();
            navEvent.setCurrentStreetName(convertSteetName(currentStreetName));
            navEvent.setDistanceToDest((long)event.getRouteProgress().getMetersToDestination());
            navEvent.setHasDistantToDest(event.getRouteProgress().hasMetersToDestination());
            navEvent.setDistanceToTurn((int)event.getRouteProgress().getMetersToNextManeuver());
            navEvent.setEstimatedTime((int)event.getTimeToGoInSeconds()*1000);
            navEvent.setNextStreetAlias(event.getNextTurn().getAlternateDisplayStreetName());
            String nextTurnStreetName = event.getNextTurn().getDisplayStreetName();
            navEvent.setNextStreetName(convertSteetName(nextTurnStreetName));
            navEvent.setNextTurnType(event.getNextTurn().getTurnType().getNumber());
            navEvent.setTurnType(event.getNextTurn().getTurnType().getNumber());
            navEvent.setSpeedLimit((int)event.getCurrentSpeedLimit());
//            navEvent.setSpeedLimitMph(DataUtil.kmToMile((int)event.getCurrentSpeedLimit()));
            navEvent.setExitName(event.getNextTurn().getExitName());
            navEvent.setExitNum(event.getNextTurn().getExitNumber());
            navEvent.setNextTurnIndex(event.getRouteProgress().getNextTurnIndex());
            if(event.hasAdiRoadLocation())
            {
                TnNavLocation location = new TnNavLocation("");
                location.setAltitude((float)event.getAdiRoadLocation().getAltitude());
                location.setLatitude((int)event.getAdiRoadLocation().getLatitude());
                location.setLongitude((int)event.getAdiRoadLocation().getLongitude());
                navEvent.setAdiLocation(location);
            }
            List<LaneAssistInformation> infos = event.getLaneAssistInfoList();
            if (infos != null && event.getLaneAssistInfoCount() > 0)
            {
                int size = event.getLaneAssistInfoCount();
                NavigationData.RouteTurnType[] laneTypes = new NavigationData.RouteTurnType[size];
                int[] laneInfos = new int[size];
                for (int i = 0; i < size; i++)
                {
                    laneTypes[i] = infos.get(i).getLaneTurnType();
                    laneInfos[i] = infos.get(i).getLaneIsInRoute() ? 1 : 0;
                }
                navEvent.setLaneTypes(laneTypes);
                navEvent.setLaneInfos(laneInfos);
            }
            return navEvent;
        }

        public static double convertCoordinate(int iCoordinate)
        {
            return iCoordinate / 100000.0;
        }
        
        public static int convertToClientCoordinate(double iCoordinate)
        {
            return (int)(iCoordinate*100000);
        }

        public static NavSdkRoute convertRoute(RouteSummary summary, boolean isFromSummeryResponse)
        {
            if (summary == null)
                return null;

            NavSdkRoute route = NavSdkRouteDataFactory.getInstance().createNavSdkRoute();
            // route.setOriginTimeStamp(summary.getOriginTimeStamp());
            // route.setOriginVe(summary.getOriginVe());
            // route.setOriginVn(summary.getOriginVn());
            // route.setRouteID(summary.getRouteId());
            try
            {
                route.setRouteID(Integer.parseInt(summary.getRouteName()));
            }
            catch (Exception e)
            {
            }
            route.setTotalTimeInSeconds(summary.getTotalTimeInSeconds());
            route.setWayPoints(summary.getWaypointsList());
            List<RouteTurn> routeTurns = summary.getTurnList();
            Segment[] oriSegs = new Segment[routeTurns.size()];
            for (int i = 0; i < routeTurns.size(); i++)
            {
                Segment seg = RouteDataFactory.getInstance().createsSegment();
                oriSegs[i] = seg;
                RouteTurn routeTurn = routeTurns.get(i);

                seg.setLength((int) routeTurn.getDistanceToTurnInMeters());
                seg.setDistanceToDest((int) routeTurn.getDistanceToTurnInMeters());
                // seg.setExitName(routeTurn.getExitName());
                seg.setExitNumber((byte) routeTurn.getExitNumber());
                
                seg.setLength((int)routeTurn.getTotalLegDistanceInMeters());
                // seg.setRoadType((byte)routeTurn.getRoadType());
                // seg.setSegmentType((byte)routeTurn.getSegmentType());
                // seg.setSpeedLimit(routeTurn.getSpeedLimit());
                // seg.setStreetAlias(routeTurn.getStreetAlias());
                // seg.setStreetAliasId(routeTurn.getStreetAliasId());
                seg.setStreetName(convertSteetName(routeTurn.getDisplayStreetName()));
                // seg.setStreetNameId(routeTurn.getStreetNameId());
                seg.setTurnType((byte) routeTurn.getTurnType().getNumber());
                seg.setSecondsToTurn(routeTurn.getSecondsToTurn());
                if (routeTurn.getTurnLocation() != null
                        && routeTurn.getTurnLocation().getLatitude() != 0
                        && routeTurn.getTurnLocation().getLongitude() != 0)
                {
                    seg.setEdgeResolved(true);
                    TnLocation turnLocation = new TnLocation("");
                    turnLocation.setLatitude(NavSdkNavigationUtil
                            .convertToClientCoordinate(routeTurn.getTurnLocation()
                                    .getLatitude()));
                    turnLocation.setLongitude(NavSdkNavigationUtil
                            .convertToClientCoordinate(routeTurn.getTurnLocation()
                                    .getLongitude()));
                    seg.setTurnLocation(turnLocation);
                    
                }
                else
                {
                    seg.setEdgeResolved(false);
                }
                if (routeTurn.hasTurnIsTight())
                {
                    seg.setTightTurn(routeTurn.getTurnIsTight());
                }
            }
            Segment[] desSegs=filterSegment(oriSegs,isFromSummeryResponse);
            route.setSegments(desSegs);
            // route.setTrafficDelayTime((int)summary.getDelayTime(), route.getLength());

            return route;
        }

        
        private static Segment[] filterSegment(Segment[] oriSegs, boolean isFromSummeryResponse)
        {
            if (isFromSummeryResponse)
            {
                int orgSize = oriSegs.length;
                int resolvedIndex=0;
                for (int oriIndex = 0; oriIndex < orgSize; oriIndex++)
                {
                    if (oriSegs[oriIndex].isEdgeResolved())
                    {
                        resolvedIndex = oriIndex;
                    }
                    else
                    {
                        break;
                    }
                }
                int desSize = resolvedIndex+1;
                Segment[] desSegs = new Segment[desSize];
                for (int desIndex = 0; desIndex < desSize; desIndex++)
                {
                    desSegs[desIndex] = (Segment) oriSegs[desIndex];
                }
                return desSegs;
            }
            else
            {
                return oriSegs;
            }
        }
        
        /**
         * convert navigation error code into human readable error messages
         */
        public static String convertError(NavigationErrorCode errorCode)
        {
            if (errorCode == null)
            {
                return "";
            }
            switch (errorCode)
            {
                case NavigationError_NoBetterRouteFound:
                {
                    return ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NO_BETTER_ROUTE,
                        IStringNav.FAMILY_NAV);
                }
                // TODO: add more error code mapping
                default:
                {
                    return errorCode.toString();
                }
            }
        }
        
        public static TrafficAlertEvent convertTrafficAlert(TrafficIncidentsAhead incidentsAhead)
        {
            /*
             * No incident or incident has passed
             */
            if (incidentsAhead == null || incidentsAhead.getIncidentsCount() < 1
                    || incidentsAhead.getIncidentsList().get(0) == null)
            {
                return null;
            }
            
            com.telenav.navsdk.events.NavigationData.TrafficIncident incident = incidentsAhead.getIncidentsList().get(0);
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, NavSdkNavigationProxyHelper.class.getName(),
                    "NavSDK incident detail info : metersToIncident - " + incident.getMetersToIncident() + " type - " + incident.getIncidentType()
                            + " details : " + incident.getIncidentdetails() + " severity : " + incident.getIncidentSeverity()
                            + " totalDely : " + incident.getDelayInSeconds());
            }
            
            TrafficAlertEvent alertEvent = new TrafficAlertEvent();
            //distance
            alertEvent.setDistance((int) incident.getMetersToIncident());
            //type
            if(incident.getIncidentType() == com.telenav.navsdk.events.NavigationData.TrafficIncidentType.TrafficIncidentType_Accident)
            {
                alertEvent.setIncidentType((byte) TrafficIncident.TYPE_ACCIDENT);
            }
            else if(incident.getIncidentType() == com.telenav.navsdk.events.NavigationData.TrafficIncidentType.TrafficIncidentType_Congestion)
            {
                alertEvent.setIncidentType((byte) TrafficIncident.TYPE_CONGESTION);
            }
            else if(incident.getIncidentType() == com.telenav.navsdk.events.NavigationData.TrafficIncidentType.TrafficIncidentType_Construction)
            {
                alertEvent.setIncidentType((byte) TrafficIncident.TYPE_CONSTRUCTION);
            }
            else if(incident.getIncidentType() == com.telenav.navsdk.events.NavigationData.TrafficIncidentType.TrafficIncidentType_SpeedCamera)
            {
                alertEvent.setIncidentType((byte) TrafficIncident.TYPE_CAMERA);
            }
            else if(incident.getIncidentType() == com.telenav.navsdk.events.NavigationData.TrafficIncidentType.TrafficIncidentType_SpeedTrap)
            {
                alertEvent.setIncidentType((byte) TrafficIncident.TYPE_SPEED_TRAP);
            }
            else if(incident.getIncidentType() == com.telenav.navsdk.events.NavigationData.TrafficIncidentType.TrafficIncidentType_Warning)
            {
                alertEvent.setIncidentType((byte) TrafficIncident.TYPE_MISC);
            }
            //by default
            else
            {
                alertEvent.setIncidentType((byte) TrafficIncident.TYPE_ACCIDENT);
            }
            //message
            alertEvent.setMessage(incident.getIncidentdetails());
            //severity
            if(incident.getIncidentSeverity() == com.telenav.navsdk.events.NavigationData.TrafficIncidentSeverity.TrafficIncidentSeverity_Major)
            {
                alertEvent.setSeverity(TrafficIncident.SEVERITY_SEVERE);
            }
            else if(incident.getIncidentSeverity() == com.telenav.navsdk.events.NavigationData.TrafficIncidentSeverity.TrafficIncidentSeverity_Medium)
            {
                alertEvent.setSeverity(TrafficIncident.SEVERITY_MINOR);
            }
            else if(incident.getIncidentSeverity() == com.telenav.navsdk.events.NavigationData.TrafficIncidentSeverity.TrafficIncidentSeverity_Minor)
            {
                alertEvent.setSeverity(TrafficIncident.SEVERITY_MINOR);
            }
            //delay
            alertEvent.setTrafficDelay(incident.getDelayInSeconds());
            
            //incident location
            TnLocation location = new TnLocation("");
            if (incident.getLocation() != null)
            {
                location.setLatitude(NavSdkNavigationUtil.convertToClientCoordinate(incident.getLocation().getLatitude()));
                location.setLongitude(NavSdkNavigationUtil.convertToClientCoordinate(incident.getLocation().getLongitude()));
                alertEvent.setIncidentLocation(location);
            }
            
            alertEvent.setIncidentId(incident.getIncidentId());
            
            
            return alertEvent;
        }
    }

    public void onAvoidIncidentAheadResponse(AvoidIncidentAheadResponse event)
    {
        String action = ACT_NAV_AVOID_TRAFFIC_INCIDENT;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onAvoidIncidentAheadError(AvoidIncidentAheadError event)
    {
        String action = ACT_NAV_AVOID_TRAFFIC_INCIDENT;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }

    @Override
    public void onGetBillBoardAdsResponse(GetBillBoardAdsResponse event)
    {
        String action = ACT_GET_BILLBOARD_ADS;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            int count = event.getAdsCount();
            Vector<BillboardAd> currentAds = new Vector<BillboardAd>();
            for (int i = 0; i < count; i++)
            {
                BillBoardAds tempAd = event.getAds(i);
                BillboardAd ad = new BillboardAd();
                ad.setAdsId(tempAd.getAdsId());
                ad.setAdsSource(tempAd.getAdsSource());
                ad.setDetailViewTime(tempAd.getDetailViewTime());
                ad.setExpirationTime(tempAd.getExpirationTime());
                ad.setInitialViewTime(tempAd.getInitialViewTime());
                ad.setPoiViewTime(tempAd.getPoiViewTime());
                ad.setUrl(tempAd.getAdsUrl());

                GeoFence geoFence = new GeoFence();
                geoFence.setLat(tempAd.getLat());
                geoFence.setLon(tempAd.getLon());
                geoFence.setDistance(tempAd.getDistance());
                ad.setGeoFence(geoFence);
                currentAds.add(ad);
            }

            proxy.setBillBoardAds(currentAds);
            proxy.transactionFinished(action, proxy);
        }
    }

    @Override
    public void onGetBillBoardAdsError(GetBillBoardAdsError event)
    {
        String action = ACT_GET_BILLBOARD_ADS;
        NavSdkNavigationProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getErrorString());
            proxy.transactionError(action, proxy);
        }
    }
    
    public void getBillBoardAds(GetBillBoardAdsRequest request, NavSdkNavigationProxy proxy)
    {
        registerRequestCallback(ACT_GET_BILLBOARD_ADS, proxy);
        
        serverProxy.getBillBoardAds(request);
    }

    @Override
    public void onVoiceGuidancePlayNotification(VoiceGuidancePlayNotification event)
    {
        String action = ACT_VOICE_GUIDANCE;
        NavSdkNavigationProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            proxy.setVoiceGuidance(event);
            proxy.transactionFinished(action, proxy);
        }
    }
}

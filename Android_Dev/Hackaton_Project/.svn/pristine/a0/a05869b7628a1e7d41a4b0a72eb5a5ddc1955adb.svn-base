/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavigationModule.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.nav.navigation;

import java.util.Date;
import java.util.Hashtable;

import com.telenav.app.CommManager;
import com.telenav.app.NavServiceManager;
import com.telenav.app.ThreadManager;
import com.telenav.audio.AudioPlayer;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseModel;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.carconnect.provider.tnlink.module.nav.ErrorCode;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.TripsDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.impl.IMissingAudioProxy;
import com.telenav.data.serverproxy.impl.INavigationProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.nav.NavDataFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.dsr.DsrManager;
import com.telenav.gps.AlongRouteLocationProvider;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;
import com.telenav.log.ILogConstants;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.media.AudioComposer;
import com.telenav.module.media.MediaManager;
import com.telenav.module.media.rule.AudioRuleManager;
import com.telenav.module.media.rule.IRuleParameter;
import com.telenav.module.nav.ITrafficCallback;
import com.telenav.module.nav.NavAdManager;
import com.telenav.module.nav.NavParameter;
import com.telenav.module.nav.NetworkCoverageImpl;
import com.telenav.module.nav.trafficengine.ITrafficAlertEngineListener;
import com.telenav.module.nav.trafficengine.TrafficAlertEngine;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.module.nav.trafficengine.TrafficAudioEvent;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.ICommonConstants;
import com.telenav.nav.INavDataProvider;
import com.telenav.nav.INavEngineListener;
import com.telenav.nav.INavGpsProvider;
import com.telenav.nav.NavEngine;
import com.telenav.nav.event.NavAdiEvent;
import com.telenav.nav.event.NavAudioEvent;
import com.telenav.nav.event.NavDeviationEvent;
import com.telenav.nav.event.NavEvent;
import com.telenav.nav.event.NavGpsEvent;
import com.telenav.nav.event.NavInfoEvent;
import com.telenav.nav.event.NavRouteChangeEvent;
import com.telenav.radio.TnRadioManager;
import com.telenav.res.IAudioRes;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.threadpool.ThreadPool;
import com.telenav.ui.citizen.map.IVechiclePositionCallback;
import com.telenav.ui.citizen.map.MapVehiclePositionService;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author xiangli
 *@date 2012-2-21
 */
public class NavigationModel extends AbstractBaseModel implements
        IMovingMapConstants, INavEngineListener, INavDataProvider, INavGpsProvider,
        LocationListener, ITrafficCallback, ITrafficAlertEngineListener,
        IVechiclePositionCallback, INavigationConstants
{

    public final static int ACTION_INITNAVIGATION = 1;

    public final static int ACTION_STARTNAVIGATION = 2;

    public final static int ACTION_ENDNAVIGATION = 3;

    private TnLocation[] locationData = null;

    private INavigationProxy navigationProxy;

    private Stop destStop = new Stop();

    private boolean deferDeviation;

    private int[] deviationCount;

    boolean shouldSetAlongRoute = true;

    private boolean isCurrentlyNoGPS = false;

    protected boolean isReachEndTrip = false;

    private boolean isFirstTimeToHandleNavEvent = true;

    private long endTripTime = -1L;

    // only notify lost or gain GPS after certain delay time to filter out noise
    private long lastGPSStatusChangeTime;

    private boolean isShownNoGPSIcon = false;

    public static long GPS_STATUS_CHANGE_DELAY_TIME = 30 * 1000; // 30 seconds

    private int routeId = -1;
    
//    private boolean deviationDetected = false;

    private Address adAddress;

    private TrafficAlertEngine trafficEngine;

    private NavAudioEvent lastAudioEvent;

    private MapTileAdiStatusJudger mapTileAdiStatusJudger;

    private final static String ID_NAV_AUDIO = "NAV_AUDIO";

    public boolean isArrivaed = false;

    public boolean isFirstDeviation = true;

    protected boolean isFirstTimeLocated = false;

    private boolean isNavigation = false;

    protected TnLocation lastVehicleLocation = null;

    protected static int MAX_GPS_ERROR_SIZE_4_NAV = 50;

    private int curSegIndex = 0;
    
    

    public NavigationModel()
    {
        IMissingAudioProxy missingAudioProxy = ServerProxyFactory.getInstance()
                .createMissingAudioProxy(null, CommManager.getInstance().getComm(), this);
        navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(null,
            CommManager.getInstance().getComm(), this, missingAudioProxy);
        mapTileAdiStatusJudger = new MapTileAdiStatusJudger();
    }

    public void setDestStop(Stop destStop)
    {
        this.destStop = destStop;
    }

    public boolean initNavigation()
    {
        int gpsCount = 3;
        locationData = new TnLocation[gpsCount];

        for (int i = 0; i < gpsCount; i++)
        {
            // FIXME: why we have to pass in provider?
            locationData[i] = new TnLocation("");
        }

        //boolean isLocationRetrieved = false;
        boolean isCellLocation = false;
        int count = LocationProvider.getInstance()
                .getGpsLocations(locationData, gpsCount);
        if (count <= 0)
        {
            // use last location (must be cell location and must be available)
            TnLocation lastLocation = LocationProvider.getInstance()
                    .getLastKnownLocation(
                        LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);

            if (lastLocation != null)
            {
                isCellLocation = true;
                for (int i = 0; i < gpsCount; i++)
                {
                    locationData[i].set(lastLocation);
                    locationData[i].setAccuracy(MAX_GPS_ERROR_SIZE_4_NAV);
                }

                //isLocationRetrieved = true;
            }
            else
            {
                // this case should never happen at all
                LocationProvider.getInstance().getCurrentLocation(
                    LocationProvider.GPS_VALID_TIME,
                    LocationProvider.NETWORK_LOCATION_VALID_TIME, 5000, this,
                    LocationProvider.TYPE_GPS, 3);
            }
        }
        else if (count < gpsCount)
        {
            for (int i = 1; i < gpsCount; i++)
            {
                locationData[i] = locationData[0];
            }
            //isLocationRetrieved = true;
        }
        else
        {
            //isLocationRetrieved = true;
        }

        if (isCellLocation)
        {
            // show No Gps Icon and play alert directly.
            isCurrentlyNoGPS = true;
            isShownNoGPSIcon = false;
            lastGPSStatusChangeTime = -1;
            NavGpsEvent gpsEvent = new NavGpsEvent(-1);
            handleGpsEvent(gpsEvent);
            isFirstTimeLocated = true;
            MapVehiclePositionService.getInstance().setVehiclePositionCallback(this);
            MapVehiclePositionService.getInstance().start(false);
        }

        return true;
    }

    public void setRouteId(int routeId)
    {
        this.routeId = routeId;
    }

    public void doGetRoute()
    {

        // int routeID = getInt(NavigationConstant.KEY_I_ROUTE_ID);

        // FIXME:currently we don't have SPT feature
        isArrivaed = false;
        isNavigation = false;

        if (this.routeId == -1)
        {
            PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance())
                    .getPreferenceDao();
            int routeStyle = preferenceDao
                    .getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
            int avoidSetting;
            if (routeStyle == Route.ROUTE_PEDESTRIAN)
            {
                avoidSetting = Preference.AVOID_CARPOOL_LANES;
            }
            else
            {
                avoidSetting = preferenceDao
                        .getIntValue(Preference.ID_PREFERENCE_ROUTE_SETTING);
            }
            boolean isReCalculateRoute = false;
            int heading = locationData[0].getHeading();
            navigationProxy.requestDynamicRoute(destStop, routeStyle, avoidSetting,
                isReCalculateRoute, locationData, locationData.length, heading);
        }
        else
        {
            // FIXME:currently we don't have SPT feature
            boolean isSPTNeeded = false;
            // end FIXME
            boolean isChunkedResponse = AppConfigHelper.isChunkEnabled;
            boolean isDynamicRoute = true;
            navigationProxy.requestRouteChoicesSelection(this.destStop, this.routeId,
                locationData, locationData.length, isSPTNeeded, isChunkedResponse,
                isDynamicRoute);
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        this.adAddress = null;
        String action = proxy.getRequestAction();
        if (IServerProxyConstants.ACT_INCIDENT_REPORT.equals(action)
                || IServerProxyConstants.ACT_GET_MISSING_AUDIO.equals(action))
        {
            return;
        }

//        if (action
//                .equalsIgnoreCase(IServerProxyConstants.ACT_SET_ROUTE_CHOICES_SELECTION)
//                || action
//                        .equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_SET_ROUTE_CHOICES_SELECTION)
//                || action
//                        .equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_DYNAMIC_ROUTE))
//        {
//
//        }
        MyLog.setLog("error", proxy.getErrorMsg());

        MapVehiclePositionService.getInstance().setVehiclePositionCallback(null);
        MapVehiclePositionService.getInstance().stop();
        super.transactionError(proxy);
        
        put(KEY_S_ERROR_MSG, proxy.getErrorMsg());
        put(KEY_I_ERROR_CODE, ErrorCode.NavigationError_ServiceGeneratedException);
        controller.postModelEvent(EVENT_RESULT_ERROR);
    }

    @Override
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        // TODO Auto-generated method stub

        String action = proxy.getRequestAction();

        if (action
                .equalsIgnoreCase(IServerProxyConstants.ACT_SET_ROUTE_CHOICES_SELECTION)
                || action
                        .equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_SET_ROUTE_CHOICES_SELECTION)
                || action
                        .equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_DYNAMIC_ROUTE))
        {
            if (action.equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_DYNAMIC_ROUTE)
                    || action
                            .equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_SET_ROUTE_CHOICES_SELECTION))
            {
                int responseCode = ((INavigationProxy) proxy).getResponseCode();
                // For chunk mode dynamic route, we will enter here as soon as route data comes
                // and won't wait for the end of the whole stream transaction finished to speed up.
                // After that responseCode will be set to RESPONSE_CODE_ON_TRACK. So we should ignore this.
                if(AppConfigHelper.isChunkEnabled && responseCode == INavigationProxy.RESPONSE_CODE_ON_TRACK)
                {
                    MyLog.setLog("navigation", "Get sugsequent chunk data - action=" + action);
                    //controller.postModelEvent(EVENT_RESULT_ROUTEDETAIL);
                    return;
                }
            }
            Route route = RouteWrapper.getInstance().getCurrentRoute();
            MyLog.setLog("navigation", "got route");
            
            if (isRouteGetFinished())
            {
                put(KEY_B_ISROUTEFINISED, true);
                MyLog.setLog("navigation", "route is finished");
            }
            else
            {
                MyLog.setLog("navigation", "route is not finished");
                put(KEY_B_ISROUTEFINISED, false);
            }
            put(KEY_O_RESULT_ROUTE, route);
            controller.postModelEvent(EVENT_RESULT_START_NAVIGATION);
            controller.postModelEvent(EVENT_RESULT_ROUTEDETAIL);

            startNav();
        }
        else if (IServerProxyConstants.ACT_CHUNKED_ROUTE_SELECTION_CHECK_DEVIATION.equalsIgnoreCase(action)
                || IServerProxyConstants.ACT_CHECK_DEVIATION.equalsIgnoreCase(action))
        {
            MyLog.setLog("navigation", "deviation");
            int responseCode = ((INavigationProxy) proxy).getResponseCode();
            if (responseCode == INavigationProxy.RESPONSE_CODE_ON_TRACK)
            {
                return;
            }
            Route route = RouteWrapper.getInstance().getCurrentRoute();
            if (null == route)
            {
            }
            else
            {
            }
            
            put(KEY_O_RESULT_ROUTE, route);
            //controller.postModelEvent(EVENT_RESULT_DEVIATION);
            startNav();
        }
        else if (IServerProxyConstants.ACT_AVOID_INCIDENTS.equalsIgnoreCase(action))
        {
            Route route = RouteWrapper.getInstance().getCurrentRoute();
            if (null == route)
            {
            }
            else
            {
            }

            startNav();
            // postModelEvent(EVENT_MODEL_SHOW_NAV_MAP);
        }
        else if (IServerProxyConstants.ACT_GET_EDGE.equalsIgnoreCase(action))
        {
            MyLog.setLog("navigation", "get edge");
            if (isRouteGetFinished())
            {
                put(KEY_B_ISROUTEFINISED, true);
                MyLog.setLog("navigation", "route is finished");
            }
            controller.postModelEvent(EVENT_RESULT_ROUTEDETAIL);
            /*
             * Route route = RouteWrapper.getInstance().getCurrentRoute(); if (null==route) return;
             * 
             * if (this.endSegmentIndex>=route.segmentsSize()) return;
             * 
             * sendRouteSegment(route, this.startSegmentIndex, this.endSegmentIndex);
             * 
             * int startSegIndex = Math.max(0, NavEngine.getInstance() .getCurrentNavState().getSegmentIndex() -
             * DynamicNavInfoRequester.LOOKBACK_SEGMENTS); startSegIndex = DynamicNavInfoRequester.getInstance()
             * .findStartSegmentIndex(route, startSegIndex - 1);
             * 
             * this.startSegmentIndex = startSegIndex; this.endSegmentIndex = DynamicNavInfoRequester.getInstance()
             * .findLastSegmentIndex(route, startSegIndex + 1);
             */

        }
        else if(IServerProxyConstants.ACT_SYNC_PURCHASE.equalsIgnoreCase(action))
        {
            MyLog.setLog("navigation", "need purchase");
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                put(KEY_S_ERROR_MSG, proxy.getErrorMsg());
                put(KEY_I_ERROR_CODE,ErrorCode.NavigationError_ServiceGeneratedException);
                controller.postModelEvent(EVENT_RESULT_ERROR);
                handleAccountFatalError();
            }
            
        }
    }
    
    

    @Override
    protected void doActionDelegate(int actionId)
    {
        // TODO Auto-generated method stub

    }

    protected NavParameter getNavParameter()
    {
        NavParameter navParameter = (NavParameter) this.get(KEY_O_NAV_PARAMETER);
        if (navParameter == null)
        {
            navParameter = new NavParameter();
            this.put(KEY_O_NAV_PARAMETER, navParameter);
        }
        return navParameter;
    }

    private TrafficAlertEngine getTrafficAlertEngine()
    {
        if (trafficEngine == null)
        {
            trafficEngine = new TrafficAlertEngine();
        }
        return trafficEngine;
    }

    private void startNav()
    {
        MyLog.setLog("navigation", "start nav");
        deferDeviation = false;
//        deviationDetected = false;
        shouldSetAlongRoute = true;

        lastAudioEvent = null;
        getNavParameter().alertEvent = null;

        DynamicNavInfoRequester.getInstance().reset();
        mapTileAdiStatusJudger.setMapTiles(navigationProxy.getMapTiles());

        put(KEY_B_IS_ARRIVE_DESTINATION, false);

        RouteWrapper routeWrapper = RouteWrapper.getInstance();
        if (null == routeWrapper.getRoutes())
        {
        }

        if (routeWrapper.getRoutes() != null)
        {
            TrafficAlertEngine trafficEngine = getTrafficAlertEngine();
            trafficEngine.stop();

            ThreadPool navPool = ThreadManager
                    .getPool(ThreadManager.TYPE_NAV_CORE_ENGINE);
            ThreadPool eventPool = ThreadManager
                    .getPool(ThreadManager.TYPE_NAV_CORE_EVENT);
            NavEngine navEngine = NavEngine.getInstance();
            if (navEngine.isRunning())
            {
                navEngine.stop();

                // VERY IMPORTANT: before nav Engine really stopped, we have to wait.
                // otherwise, there will be unexpected errors!!!
                int count = 0;
                while (!navPool.isIdle() || !eventPool.isIdle())
                {
                    if (count++ > 40)
                    {
                        break;
                    }
                    try
                    {
                        Thread.sleep(250);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            Address adAddress = this.adAddress;

            if (adAddress != null)
            {
                TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
                tripsDao.setDetourTrip(adAddress);
                this.adAddress = null;
                this.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, true);
                this.put(ICommonConstants.KEY_I_AC_TYPE,
                    ICommonConstants.TYPE_AC_FROM_NAV);
            }

            AudioPlayer player = MediaManager.getInstance().getAudioPlayer();
            // FIXME: it's a little ugly.
            long timeout = 15000;
            long start = System.currentTimeMillis();
            while (player.isPlaying() && (System.currentTimeMillis() - start) < timeout)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {

                }
            }
            playNewRouteAudio();
            put(KEY_I_ROUTE_ID, routeWrapper.getCurrentRouteId());

            NavServiceManager.getNavService().setRouteId(getInt(KEY_I_ROUTE_ID) + "");
            navEngine.start(navPool, eventPool, this, this);
            navEngine.addListener(this);

            int routeStyle = ((DaoManager) DaoManager.getInstance()).getPreferenceDao()
                    .getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
            if (routeStyle != Route.ROUTE_PEDESTRIAN)// port the fix of bug 22262,22269
            {

                trafficEngine.start(this);
                trafficEngine.updateOnRoute(navEngine.getCurrentNavState());
            }

            NavAdManager.getInstance().start();

            isNavigation = true;

        }

        /*
         * if(getState() != STATE_MOVING_MAP) { postModelEvent(EVENT_MODEL_SHOW_NAV_MAP); }
         */
    }

    protected void stopNav()
    {
        MyLog.setLog("navigation", "stop nav");
        navigationProxy = null;

        NavEngine navEngine = NavEngine.getInstance();
        if (navEngine.isRunning())
        {
            navEngine.stop();
        }
        navEngine.removeListener(this);

        NetworkCoverageImpl.getInstance().removeAllListeners();

        MediaManager.getInstance().getAudioPlayer().cancelAll();

        if (trafficEngine != null)
        {
            trafficEngine.stop();
            trafficEngine = null;
        }

        if (TnLocationManager.ALONGROUTE_PROVIDER.equals(LocationProvider.getInstance().getGpsServiceType()))
        {
            TnLocationProvider provider = TnLocationManager.getInstance().getProvider(TnLocationManager.ALONGROUTE_PROVIDER);

            if(lastVehicleLocation != null)
            {
                int lat = lastVehicleLocation.getLatitude();
                int lon = lastVehicleLocation.getLongitude();
                ((AlongRouteLocationProvider) provider).setOrigin(lat, lon);
            }
            ((AlongRouteLocationProvider) provider).setNavState(null);
        }

        isNavigation = false;
    }

    public String getDestination()
    {
        // TODO Auto-generated method stub
        return this.destStop.getFirstLine();
    }

    public int getFixes(int numFixes, TnLocation[] data)
    {
        // TODO Auto-generated method stub
        if (data == null || data.length < 1)
        {
            return 0;
        }

        return LocationProvider.getInstance().getGpsLocations(data, numFixes);
    }

    public int getLastKnownHeading()
    {
        // TODO Auto-generated method stub
        TnLocation lastKnownData = LocationProvider.getInstance().getLastKnownLocation(
            LocationProvider.TYPE_GPS);
        if (lastKnownData != null)
        {
            return lastKnownData.getHeading();
        }
        return 0;
    }

    public void eventUpdate(NavEvent event)
    {
        // TODO Auto-generated method stub
        int eventType = event.getEventType();
        switch (eventType)
        {
            case NavEvent.TYPE_INFO:
            case NavEvent.TYPE_ADI: // ADI event is also a info event
            {
                NavInfoEvent navInfoEvent = (NavInfoEvent) event;
                handleNavInfoEvent(navInfoEvent);
                break;
            }
            case NavEvent.TYPE_AUDIO:
            {
                NavAudioEvent audioEvent = (NavAudioEvent) event;
                handleAudioEvent(audioEvent);
                break;
            }
            case NavEvent.TYPE_GPS:
            {
                NavGpsEvent gpsEvent = (NavGpsEvent) event;
                handleGpsEvent(gpsEvent);
                break;
            }
            case NavEvent.TYPE_DEVIATION:
            {
                // TODO collect gps points when deviation for feedback.

                NavDeviationEvent navDeviationEvent = (NavDeviationEvent) event;
                deviationCount = navDeviationEvent.getDeviationCount();

//                NavState navState = NavEngine.getInstance().getCurrentNavState();

                if (!deferDeviation)
                {
                    handleDeviation();
                }
//                else
//                {
//                    deviationDetected = true;
//                }
                break;
            }
            case NavEvent.TYPE_ROUTE_CHANGE:
            {
                NavRouteChangeEvent routeChangeEvent = (NavRouteChangeEvent) event;
                handleRouteChangeEvent(routeChangeEvent);
                break;
            }
            default:
            {
                break;
            }
        }
    }

    public void handleTrafficAlert(TrafficAlertEvent alertEvent)
    {
        // TODO Auto-generated method stub

    }

    public void handleTrafficAudio(TrafficAudioEvent audioEvent)
    {
        // TODO Auto-generated method stub

    }

    private boolean checkArriveDest(NavInfoEvent navEvent)
    {
        boolean isArriveDest = this.getBool(KEY_B_IS_ARRIVE_DESTINATION);

        if (navEvent != null && navEvent.getDistanceToDest() > 400)
        {

            if (isArriveDest)
            {
                put(KEY_B_IS_ARRIVE_DESTINATION, false);
                // TODO: it is not necessary in TN70
                // DaoManager daoManager = (DaoManager) AbstractDaoManager.getInstance();
                // int mapType = daoManager.getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_MAPSTYLE);
                // setDimensionality(mapType);
            }
        }
        else
        {

            if (NavEngine.getInstance().isArriveDestination()
                    || (navEvent != null && navEvent.getDistanceToDest() < 100))
            {
                isReachEndTrip = true;
                if (!isArriveDest)
                {
                    int headingAngle = 0;
                    if (navEvent != null)
                    {
                        headingAngle = navEvent.getHeadingAngle();
                    }
                    String sector = ResourceManager.getInstance().getStringConverter()
                            .convertHeading(headingAngle);
                    boolean isContinued = handleEndTripEvent(navEvent, headingAngle,
                        sector);
                    if (!isContinued)
                        return false;
                }
                else
                {
                    TnLocation location = LocationProvider.getInstance()
                            .getCurrentLocation(LocationProvider.TYPE_GPS);

                    if (NavEngine.getInstance().isArriveDestination() && location != null)
                    {
                        // if (!isArrivaed) {
                        isArrivaed = true;
                        Route route = RouteWrapper.getInstance().getCurrentRoute();
                        put(KEY_O_LASTSEGMENT, route.getSegments()[route.segmentsSize()-1]);
                        controller.postModelEvent(EVENT_RESULT_ARRIVED);
                        isNavigation = false;
                        // stopNav();
                        // }
                    }
                }
            }

            TnLocation location = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_GPS);

            if (isArriveDest)
            {
                if (navEvent == null
                        || (navEvent.getRoutePosition() != null && navEvent
                                .getRoutePosition().getPosition() != null))
                {

                    int speed = 0;
                    if (location != null)
                    {
                        speed = location.getSpeed();
                    }

                    speed *= StringConverter.DM6TOSPEED[1]; // US unit
                    speed >>= StringConverter.DM6TOSPEED_SHIFT;

                    // changed from 5 to 20 to fix bug TNSIXTWO-935
                    int speedThreshold = 20; // mile

                    if (endTripTime <= 0 || speed > speedThreshold)
                    {
                        endTripTime = System.currentTimeMillis();
                    }
                    else if (speed <= speedThreshold)
                    {

                    }
                }

                return false;
            }
        }

        return true;
    }

    private boolean handleEndTripEvent(NavInfoEvent navEvent, int headingAngle,
            String sector)
    {
        if (navEvent == null)
            return true;

        // TODO: do we have these logic in TN7.0? currently we impl the simple solution first.
        // if (this.getState() == STATE_EXIT_APPLICATION_CONFIRM || this.getState() == STATE_EXIT_APPLICATION
        // || this.getState() == STATE_BACK_TO_HOME_CONFIRM)
        // return true;

        if (isFirstTimeToHandleNavEvent)
        {
            return false;
        }

        NavParameter navParameter = getNavParameter();

        navParameter.currStreetName = "";
        navParameter.distanceToTurn = 0;
        navParameter.headingAngle = headingAngle;
        navParameter.isAdi = false;
        // navParameter.isArriveDest = this.getBool(KEY_B_IS_ARRIVE_DESTINATION);
        navParameter.nextStreetAlias = navEvent.getNextStreetAlias();
        navParameter.nextStreetName = navEvent.getNextStreetName();
        // navParameter.nextTurnImage = nextTurnImage;
        // navParameter.numSats = navEvent.getNumSats();
        navParameter.sector = sector;
        // navParameter.trafficAlertEvent = null;

        navParameter.tripDist = navEvent.getDistanceToDest();
        navParameter.turnType = navEvent.getTurnType();
        navParameter.totalToDest = navEvent.getDistanceToDest();
        // navParameter.eta = this.tripEta;

        this.put(KEY_B_IS_ARRIVE_DESTINATION, true);

        return true;
    }

    private void handleNavInfoEvent(NavInfoEvent navInfoEvent)
    {
        isReachEndTrip = false;

        boolean isContinue = checkArriveDest(navInfoEvent);
        if (!isContinue)
        {
            return;
        }

        NavParameter navParameter = new NavParameter();// getNavParameter();

        navParameter.distanceToTurn = navInfoEvent.getDistanceToTurn();

        navParameter.nextStreetName = navInfoEvent.getNextStreetName();
        navParameter.currStreetName = navInfoEvent.getCurrStreetName();
        navParameter.turnType = navInfoEvent.getTurnType();
        navParameter.eta = navInfoEvent.getEstimatedTime() / 1000;// + System.currentTimeMillis();
        navParameter.totalToDest = navInfoEvent.getDistanceToDest();
        navParameter.headingAngle = navInfoEvent.getHeadingAngle();

        if (navInfoEvent.getEventType() == NavEvent.TYPE_ADI)
        {
            NavAdiEvent adiEvent = (NavAdiEvent) navInfoEvent;
            if (adiEvent.getRouteEntryPoint() != null)
            {
                TnLocation adiLocation = adiEvent.getRouteEntryPoint().getPosition();
                navParameter.isAdi = true;
                if (adiLocation != null)
                {
                    navParameter.adiLat = adiLocation.getLatitude();
                    navParameter.adiLon = adiLocation.getLongitude();
                }
            }
        }
        else
        {
            navParameter.isAdi = false;
        }

        NavState routePosition = (NavState) navInfoEvent.getRoutePosition();

//        int distanceToDest = 0;
        if (routePosition != null)
        {
            curSegIndex = routePosition.getSegmentIndex();
            TnNavLocation position = routePosition.getPosition();
            if (position != null)
            {
                navParameter.speed = position.getSpeed();
            }

            RouteEdge routeEdge = routePosition.getCurrentEdge();

            if (routeEdge != null)
            {
                navParameter.speedLimit = routeEdge.getSpeedLimit();
                navParameter.speedLimitKph = routeEdge.getSpeedLimitKph();
                navParameter.speedLimitMph = routeEdge.getSpeedLimitMph();
            }

            Segment currentSegment = routePosition.getCurrentSegment();
            if (currentSegment != null)
            {
//                distanceToDest = currentSegment.getDistanceToDest();

                navParameter.exitName = currentSegment.getExitName();
                navParameter.exitNumber = currentSegment.getExitNumber();
            }

            if (currentSegment != null)
            {
                int[] currentLaneInfo = currentSegment.getLaneInfos();
                if (currentSegment.isPlayed(Route.AUDIO_TYPE_PREP)
                        && currentLaneInfo != null && currentLaneInfo.length > 1)
                {
                    navParameter.laneInfos = currentLaneInfo;
                    navParameter.laneTypes = currentSegment.getLaneTypes();
                }
                else
                {
                    navParameter.laneInfos = null;
                    navParameter.laneTypes = null;
                }

                navParameter.nextSegmentIndex = routePosition.getSegmentIndex() + 1;

            }
        }

        TnNavLocation snapPosition = navInfoEvent.getSnapPosition();
        if (snapPosition != null)
        {
            navParameter.vehicalLocation = snapPosition;
            lastVehicleLocation = snapPosition;
        }

        this.put(KEY_O_NAV_PARAMETER, navParameter);
        
        DynamicNavInfoRequester.getInstance().requestRouteEdge(navigationProxy);
        DynamicNavInfoRequester.getInstance().requestDynamicAudio(navigationProxy);

        isFirstTimeToHandleNavEvent = false;
        
        this.put(KEY_O_RESULT_NAVSTATUS, navParameter);
        //Log.i("Hou", "handleNavInfoEvent- before post navupdate navinfo=" + navInfoEvent);
        controller.postModelEvent(EVENT_RESULT_NAVSTATUS);
    }

    private long lastDeviationTime;

    private void handleDeviation()
    {
        put(KEY_B_IS_ARRIVE_DESTINATION, false);

        // TODO: Add this logic later. Currently, it doesn't be refer by other place.
        // this.checkingDeviation = true;

        // TODO cleanup current resources used by route
        if (!isAudioDisabled())
        {
            MediaManager.getInstance().playStaticAudio(IAudioRes.NEW_REROUTING);
        }

        if (System.currentTimeMillis() - lastDeviationTime < 15000)
        {
            // client should not deviate twice inside 15 seconds
            Logger.log(Logger.INFO, ILogConstants.LOG_NAVIGATION, new Date()
                    + " :: [FATAL] found two deviations inside 15 seconds");
        }
        lastDeviationTime = System.currentTimeMillis();

        int gpsCount = 5;
        TnLocation[] locations = new TnLocation[gpsCount];

        for (int i = 0; i < gpsCount; i++)
        {
            // FIXME: why we have to pass in provider?
            locations[i] = new TnLocation("");
        }
        gpsCount = LocationProvider.getInstance().getGpsLocations(locations, gpsCount);
        // clear cached map tiles
        mapTileAdiStatusJudger.setMapTiles(null);
        navigationProxy.requestDeviation(RouteWrapper.getInstance().getCurrentRouteId(),
            deviationCount, locations, gpsCount, this.destStop/* getDestStop() */,
            locations[0].getHeading(), 0, 0, 0, 0, AppConfigHelper.isChunkEnabled);
    }

    // boolean shouldSetAlongRoute = true;
    private void handleGpsEvent(NavGpsEvent gpsEvent)
    {
        if (shouldSetAlongRoute)
        {
            if (TnLocationManager.ALONGROUTE_PROVIDER.equals(LocationProvider
                    .getInstance().getGpsServiceType()))
            {
                TnLocationProvider provider = TnLocationManager.getInstance()
                        .getProvider(TnLocationManager.ALONGROUTE_PROVIDER);

                NavState state = NavEngine.getInstance().getCurrentNavState();

                if (state != null)
                {
                    ((AlongRouteLocationProvider) provider).setNavState(state);
                    shouldSetAlongRoute = false;
                }
            }
            else
            {
                shouldSetAlongRoute = false;
            }
        }

        if (gpsEvent.getSatelliteCount() >= 0)
        {
            if (this.isCurrentlyNoGPS)
            {
                // set last status change time
                lastGPSStatusChangeTime = System.currentTimeMillis();
            }

            this.isCurrentlyNoGPS = false;
            if (isShownNoGPSIcon
                    && System.currentTimeMillis() - this.lastGPSStatusChangeTime > GPS_STATUS_CHANGE_DELAY_TIME)
            {
                isShownNoGPSIcon = false;

                NavParameter param = getNavParameter();

                param.isNoSatellite = false;
                // this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                // play audio "You now have GPS signal."
                MediaManager.getInstance().playStaticAudio(
                    IAudioRes.JINGLES_FRIENDLY_BEEP);
                MediaManager.getInstance().playStaticAudio(IAudioRes.NEW_GPS_SIGNAL);
            }
        }
        else
        {
            if (!this.isCurrentlyNoGPS)
            {
                // set last status change time
                lastGPSStatusChangeTime = System.currentTimeMillis();
            }

            this.isCurrentlyNoGPS = true;
            if (!isShownNoGPSIcon
                    && System.currentTimeMillis() - this.lastGPSStatusChangeTime > GPS_STATUS_CHANGE_DELAY_TIME)
            {
                isShownNoGPSIcon = true;

                NavParameter param = getNavParameter();

                param.isNoSatellite = true;

            }
        }

        checkArriveDest(null);

    }

    private void handleAudioEvent(NavAudioEvent audioEvent)
    {
        DaoManager daoManager = (DaoManager) DaoManager.getInstance();
        PreferenceDao preferenceDao = daoManager.getPreferenceDao();
        Preference audioPreference = preferenceDao
                .getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        if (audioPreference != null
                && (audioPreference.getIntValue() == Preference.AUDIO_TRAFFIC_ONLY || audioPreference
                        .getIntValue() == Preference.AUDIO_NONE))
        {
            return;
        }
        // FIXME: ingnore current audio when dsr is working.
        // let's find right stategy later.
        if (DsrManager.getInstance().isRunning())
        {
            return;
        }

        int currSegIndex = audioEvent.getCurrentSegmentIndex();
        int turnType = audioEvent.getTurnType();
        boolean isSpecialUturnAudio = false;
        Route currentRoute = RouteWrapper.getInstance().getCurrentRoute();
        if (turnType == Route.L2L_U_TURN)
        {
            int nextNextSegIndex = currSegIndex + 2;
            if (nextNextSegIndex <= currentRoute.segmentsSize())
            {
                Segment nextNextSegment = currentRoute.segmentAt(nextNextSegIndex);
                if (audioEvent.getDistanceToTurn() < nextNextSegment.getLength())
                {
                    isSpecialUturnAudio = true;
                }
            }
        }

        AudioData[] playList;
        if (isSpecialUturnAudio)
        {
            playList = new AudioData[]
            { AudioDataFactory.getInstance().createAudioData("") };
            playList[0].setAudio(IAudioRes.NEW_UTURN_WHEN_POSSIBLE);
        }
        else
        {
            playList = AudioComposer.getInstance().createPrompt(audioEvent);
        }

        boolean isRecording = MediaManager.getInstance().getRecordPlayer().isRecording();
        boolean isPlaying = MediaManager.getInstance().getAudioPlayer().isPlaying();
        if (isRecording || isPlaying)
        {
            // The DSR is working now
            if (isRecording)// We only cancel when is recording. For that cancel() will cancel the whole network
            // request. fix bug 52114. fqming
            {
                MediaManager.getInstance().getRecordPlayer().cancel();
            }

        }

        if (audioEvent.isPlayImmediately())
        {
            MediaManager.getInstance().getAudioPlayer().cancelAll();
        }
        MediaManager.getInstance().getAudioPlayer().play(ID_NAV_AUDIO, playList, -1);

        lastAudioEvent = audioEvent;
    }

    private void handleRouteChangeEvent(NavRouteChangeEvent routeChangeEvent)
    {
        RouteWrapper.getInstance().setCurrentRouteId(routeChangeEvent.getNewRouteId());
        Route route = RouteWrapper.getInstance().getCurrentRoute();
        if (null == route)
        {
        }
        else
        {
        }

        // cancel all audio before playing "new route" audio
        MediaManager.getInstance().getAudioPlayer().cancelAll();
        playNewRouteAudio();
        getNavParameter().alertEvent = null;

        // FIXME: not sure how to do this now.
        NavServiceManager.getNavService().setRouteId(
            routeChangeEvent.getNewRouteId() + "");
    }

    protected void doPlayAudio()
    {
        if (null == lastAudioEvent)
        {
        }
        if (lastAudioEvent != null
                && !MediaManager.getInstance().getAudioPlayer().isPlaying()
                && !isAudioDisabled())
        {
            NavState navState = NavEngine.getInstance().getCurrentNavState();
            PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance())
                    .getPreferenceDao();
            Preference audioTypePref = preferenceDao
                    .getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);

            int audioPreference = -1;
            if (audioTypePref != null)
            {
                audioPreference = audioTypePref.getIntValue();
            }
            else
            {
            }
            if (navState != null)
            {
                if (audioPreference == Preference.AUDIO_DIRECTIONS_ONLY
                        || audioPreference == Preference.AUDIO_DIRECTIONS_TRAFFIC)
                {
                    int distToTurn = navState.getDistanceToTurn()
                            - navState.getCurrentRange();
                    if (navState.getPosition() != null)
                    {
                        // add 3 seconds off for audio playing delay
                        int audioDelayOffset = navState.getPosition().getSpeed() / 3;
                        if (audioDelayOffset < distToTurn)
                            distToTurn -= audioDelayOffset;
                    }

                    NavAudioEvent newAudioEvent = new NavAudioEvent(lastAudioEvent
                            .getRoute(), lastAudioEvent.getCurrentSegmentIndex(),
                            distToTurn, lastAudioEvent.getAudioType(), lastAudioEvent
                                    .getAudioType(), lastAudioEvent.getHeadingType(),
                            lastAudioEvent.isPlayImmediately());
                    AudioData[] playList = AudioComposer.getInstance().createPrompt(
                        newAudioEvent);
                    MediaManager.getInstance().getAudioPlayer().play(
                        IMovingMapConstants.ID_NAV_AUDIO, playList, -1);
                }

                if (audioPreference == Preference.AUDIO_TRAFFIC_ONLY
                        || audioPreference == Preference.AUDIO_DIRECTIONS_TRAFFIC)
                {
                    TrafficAlertEngine engine = getTrafficAlertEngine();
                    Preference trafficAlertPref = ((DaoManager) DaoManager.getInstance())
                            .getPreferenceDao().getPreference(
                                Preference.ID_PREFERENCE_TRAFFICALERT);
                    if (engine != null
                            && trafficAlertPref != null
                            && trafficAlertPref.getIntValue() == Preference.TRAFFIC_ALERT_ON)
                    {
                        TrafficIncident incident = engine.getCurrentTrafficIncident();
                        NavParameter navParameter = getNavParameter();

                        // no need to replay audio for speed trap and traffic camera
                        if (incident != null
                                && navParameter.alertEvent != null
                                && incident.getIncidentType() != TrafficIncident.TYPE_CAMERA
                                && incident.getIncidentType() != TrafficIncident.TYPE_SPEED_TRAP)
                        {
                            playIncidentAudio(incident, null);
                        }
                    }
                }
            }
        }

    }

    private void playIncidentAudio(TrafficIncident incident,
            TrafficAudioEvent trafficAudioEvent)
    {
        if (incident == null)
            return;

        switch (incident.getIncidentType())
        {
            case TrafficIncident.TYPE_CAMERA:
            {

                int trafficCameraValue = FeaturesManager.getInstance().getStatus(
                    FeaturesManager.FEATURE_CODE_NAV_TRAFFIC_CAMERA);
                boolean isTrafficCameraOn = trafficCameraValue == FeaturesManager.FE_ENABLED
                        || trafficCameraValue == FeaturesManager.FE_PURCHASED;

                int trafficCameraPreference = ((DaoManager) DaoManager.getInstance())
                        .getPreferenceDao().getIntValue(
                            Preference.ID_PREFERENCE_TRAFFIC_CAMERA_ALERT);
                boolean isNeedCamera = (trafficCameraPreference == Preference.TRAFFIC_CAMERA_ALERT_ON)
                        && isTrafficCameraOn;
                if (!isNeedCamera || trafficAudioEvent == null)
                {
                    break;
                }
                if (!MediaManager.getInstance().getAudioPlayer().isPlaying()
                        && !isAudioDisabled())
                {
                    MediaManager.getInstance().playStaticAudio(new int[]
                    { IAudioRes.JINGLES_WARNING_BEEP, IAudioRes.TRAFFIC_CAMERA_AHEAD });
                }
                break;
            }
            case TrafficIncident.TYPE_SPEED_TRAP:
            {

                int speedTrapValue = FeaturesManager.getInstance().getStatus(
                    FeaturesManager.FEATURE_CODE_NAV_SPEED_TRAP);
                boolean isSpeedTrapOn = speedTrapValue == FeaturesManager.FE_ENABLED
                        || speedTrapValue == FeaturesManager.FE_PURCHASED;

                int speedTrapPreference = ((DaoManager) DaoManager.getInstance())
                        .getPreferenceDao().getIntValue(
                            Preference.ID_PREFERENCE_SPEED_TRAP_ALERT);
                boolean isNeedSpeedTrap = (speedTrapPreference == Preference.SPEED_TRAP_ALERT_ON)
                        && isSpeedTrapOn;
                if (!isNeedSpeedTrap || trafficAudioEvent == null)
                {
                    break;
                }

                if (MediaManager.getInstance().getAudioPlayer().isPlaying()
                        || isAudioDisabled())
                {
                    break;
                }
                DaoManager daoManager = (DaoManager) DaoManager.getInstance();
                PreferenceDao preferenceDao = daoManager.getPreferenceDao();
                int sysOfUnits = preferenceDao.getPreference(
                    Preference.ID_PREFERENCE_DISTANCEUNIT).getIntValue();

                AudioRuleManager ruleManager = ((DaoManager) AbstractDaoManager
                        .getInstance()).getRuleManager();
                AbstractRule speedTrapRule = (AbstractRule) ruleManager
                        .getAudioRule(IRuleParameter.RULE_TYPE_SPEED_TRAP_WARNING_AUDIO);

                Hashtable param = new Hashtable();
                param.put(IRuleParameter.DISTANCE, PrimitiveTypeCache
                        .valueOf(trafficAudioEvent.getDistance()));
                param.put(IRuleParameter.DIST_UNIT, PrimitiveTypeCache
                        .valueOf(sysOfUnits));
                AudioData[] audioData = speedTrapRule.createAudioData(param);

                MediaManager.getInstance().getAudioPlayer().play("", audioData, -1);

                break;
            }
            default:
            {
                NavState navState = NavEngine.getInstance().getCurrentNavState();
                if (navState != null)
                {
                    int distance = navState.getDistanceToEdge(incident.getEdgeID(),
                        200000);

                    // no need to play traffic audio if too far away or already pass the incident
                    if (distance < 0)
                        return;

                    AudioData[] playList = AudioComposer.getInstance()
                            .createIncidentPrompt(distance, incident.getIncidentType(),
                                incident.getStreetAudio(), incident.getLocationAudio(),
                                incident.getLaneClosed());
                    MediaManager.getInstance().getAudioPlayer().play("", playList, -1);

                }
            }
        }
    }

    private void playNewRouteAudio()
    {
        AudioPlayer player = MediaManager.getInstance().getAudioPlayer();
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance())
                .getPreferenceDao();
        Preference routeStylePref = preferenceDao
                .getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        Preference routeSettingPref = preferenceDao
                .getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);

        int routeStyle = -1;
        int routeSetting = -1;
        if (routeStylePref != null)
        {
            routeStyle = routeStylePref.getIntValue();
        }
        if (routeSettingPref != null)
        {
            routeSetting = routeSettingPref.getIntValue();
        }
        // play new route audio
        if ((routeStyle & Route.ROUTE_PEDESTRIAN) != Route.ROUTE_PEDESTRIAN
                && !isAudioDisabled())
        {
            int trafficEnableValue = FeaturesManager.getInstance().getStatus(
                FeaturesManager.FEATURE_CODE_TRAFFIC);
            boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                    || trafficEnableValue == FeaturesManager.FE_PURCHASED;
            if ((routeSetting & Preference.AVOID_TRAFFIC_DELAYS) == Preference.AVOID_TRAFFIC_DELAYS
                    && isTrafficEnabled) // Guoyuan for bug 21774 - do not play traffic
            // if the region does not support it.
            {
                if (!player.isPlaying())
                {
                    MediaManager.getInstance().playStaticAudio(
                        IAudioRes.TRAFFIC_NEW_TRAFFIC_ROUTE_V1);
                }
            }
            else
            {
                if (!player.isPlaying())
                {
                    MediaManager.getInstance().playStaticAudio(IAudioRes.NEW_NEW_ROUTE);
                }
            }
        }
    }

    private boolean isAudioDisabled()
    {
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance())
                .getPreferenceDao();
        Preference audioTypePref = preferenceDao
                .getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        int audioPreference = -1;
        if (audioTypePref != null)
        {
            audioPreference = audioTypePref.getIntValue();
        }

        return audioPreference == Preference.AUDIO_NONE;

    }

    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        // TODO Auto-generated method stub
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            locationData = locations;
            // postModelEvent(EVENT_MODEL_LOCATION_DATA_RETRIEVED);
        }
        else
        {
            // postModelEvent(EVENT_MODEL_LOCATION_DATA_FAILED);
        }
    }

    public void noGpsTimeout()
    {
        // TODO Auto-generated method stub

    }

    public void updateVechiclePostion(TnLocation location)
    {
        // TODO Auto-generated method stub
        boolean isGpsAvailable = true;
        if (location == null)
        {
            isGpsAvailable = false;
        }
        else if (location.getProvider().equals(TnLocationManager.NETWORK_PROVIDER)
                || location.getProvider().equals(TnLocationManager.TN_NETWORK_PROVIDER))
        {
            isGpsAvailable = false;
        }

        int gpsCount = -1;
        if (isGpsAvailable)
        {
            gpsCount = location.getSatellites();
        }

        NavGpsEvent gpsEvent = new NavGpsEvent(gpsCount);
        handleGpsEvent(gpsEvent);
    }

    public boolean isNavigation()
    {
        return isNavigation;
    }

    public boolean isArrived()
    {
        return isArrivaed;
    }

    public int getCurSegIndex()
    {
        return curSegIndex;
    }

    public NavState getCurrentNavState()
    {
        // TODO Auto-generated method stub
        NavState navState = NavEngine.getInstance().getCurrentNavState();
        if (navState != null)
        {
            return navState;
        }
        else
        {
            navState = NavDataFactory.getInstance().createNavState(
                RouteWrapper.getInstance().getCurrentRouteId());
        }
        return navState;
    }

    public void resumeDeviation()
    {
        // TODO Auto-generated method stub

    }

    public void suspendDeviation()
    {
        // TODO Auto-generated method stub

    }

    /*
    private void updateRoute(int currentSegment)
    {
        final Route route = RouteWrapper.getInstance().getCurrentRoute();
        if (route == null)
        {
            return;
        }

        int segmentCount = route.segmentsSize();
        if (currentSegment < 0 || currentSegment >= segmentCount)
        {
            return;
        }

        int firstSegmentIndex = findFirstSegment(route, currentSegment);
        int lastSegmentIndex = findLastSegment(route, currentSegment);
        if (firstSegmentIndex == -1 || lastSegmentIndex == -1)
        {
            return;
        }

        
          MyLog.setLog("Test", "update route");
          TNNavController.getControllerInstance().setSegmentIndex(lastSegmentIndex);
          TNNavController.getControllerInstance().updateRoute(route, currentSegment);
         
    }
*/

/*
    private static int findLastResolvedSegment(Route route)
    {
        int i = 0;
        for (; i < route.getSegments().length; i++)
        {
            if (!route.getSegments()[i].isEdgeResolved())
            {
                break;
            }
        }

        return i;
    }
*/
    public static int findFirstSegment(Route route, int currentSegment)
    {
        if (route == null)
        {
            return -1;
        }
        int firstSegment = currentSegment;
        for (int i = currentSegment; i >= 0; i--)
        {
            Segment seg = route.segmentAt(i);
            if (seg != null && seg.isEdgeResolved())
            {
                firstSegment = i;
            }
            else
            {
                break;
            }
        }
        return firstSegment;
    }

    public static int findLastSegment(Route route, int currentSegment)
    {
        if (route == null)
        {
            return -1;
        }
        int lastSegment = currentSegment;
        for (int i = currentSegment; i < route.segmentsSize(); i++)
        {
            Segment seg = route.segmentAt(i);
            if (seg != null && seg.isEdgeResolved())
            {
                lastSegment = i;
            }
            else
            {
                break;
            }
        }
        return lastSegment;
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        this.adAddress = null;
        String action = proxy.getRequestAction();
        if (IServerProxyConstants.ACT_INCIDENT_REPORT.equals(action)
                || IServerProxyConstants.ACT_GET_MISSING_AUDIO.equals(action))
        {
            return;
        }
        MapVehiclePositionService.getInstance().setVehiclePositionCallback(null);
        MapVehiclePositionService.getInstance().stop();
        super.networkError(proxy, statusCode, jobId);
        
        MyLog.setLog("error", proxy.getErrorMsg());
        put(KEY_I_ERROR_CODE, ErrorCode.NavigationError_ServiceTimeout);
        put(KEY_S_ERROR_MSG, proxy.getErrorMsg());
        controller.postModelEvent(EVENT_RESULT_ERROR);
    }

    @Override
    /**
     *  @see INavDataProvider::isOnRoad()
     */
    public boolean isOnRoad(TnNavLocation gpsFix, int[] thresholds, boolean ignoreHeading)
    {
        // TODO: change to use OpenGL engine API to judge when it is ready
        // return MapEngineManager.getInstance().getMapEngine().getClientSupport().isOnRoad(latitude, longitude,
        // threshold);

        // even if OpenGL engine API work as expected, still safer to use map tiles
        // to make ADI status judgment first
        switch (mapTileAdiStatusJudger.isOnRoad(gpsFix, thresholds, ignoreHeading))
        {
            case MapTileAdiStatusJudger.STATUS_ADI:
            {
                return false;
            }
                // case MapTileAdiStatusJudger.STATUS_ON_ROAD:
                // case MapTileAdiStatusJudger.STATUS_NO_DATA:
            default:
            {
                // hold on in ADI mode in case of out of network coverage
                if (!TnRadioManager.getInstance().isNetworkConnected()
                        && !TnRadioManager.getInstance().isWifiConnected())
                {
                    return false;
                }

                return true;
            }
        }
    }

    @Override
    protected void handleEvent(int actionId)
    {
        // TODO Auto-generated method stub
        switch (actionId)
        {
            case ACTION_NAVIGATION_START:
            {
                if (!initNavigation())
                    return;
                this.routeId = getInt(KEY_I_ROUTENAME);
                controller.put(INavigationConstants.KEY_I_ROUTENAME, this.routeId);
                this.destStop = (Stop)get(KEY_O_DEST_STOP);
                doGetRoute();
                break;
            }
            case ACTION_NAVIGATION_STOP:
            {
                stopNav();
                break;
            }
            case ACTION_NAVIGATION_START_INNAVIGATION:
            {
                this.routeId = -1;
                if (!initNavigation())
                    return;
                Address destAddr = (Address)(get(INavigationConstants.KEY_O_ADDRESS_DEST));
                if (null==destAddr)
                    return;
                destStop = destAddr.getStop();
                doGetRoute();
                break;
            }
            case ACTION_NAVIGATION_GUIDANCE:
            {
                doPlayAudio();
                break;
            }
            case ACTION_NAVIGATION_ROUTESUMMARY:
            {
                Route route = RouteWrapper.getInstance().getCurrentRoute();
                put(KEY_O_RESULT_ROUTE, route);
                put(KEY_I_ROUTENAME, this.routeId);
                controller.postModelEvent(EVENT_RESULT_ROUTESUMMARY);
                break;
            }
        }
    }
    
    private boolean isRouteGetFinished()
    {
        Route route = RouteWrapper.getInstance().getCurrentRoute();
        int startSegIndex = 0;
        if (NavEngine.getInstance().isRunning())
        {
            startSegIndex = Math.max(0, NavEngine.getInstance().getCurrentNavState().getSegmentIndex() - DynamicNavInfoRequester.LOOKBACK_SEGMENTS);
        }
        
        startSegIndex = DynamicNavInfoRequester.findStartSegmentIndex(route, startSegIndex - 1);
        if (startSegIndex >= route.getSegments().length)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    
}

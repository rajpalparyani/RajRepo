/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TrafficModel.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.traffic;

import android.util.Log;

import com.telenav.app.CommManager;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseModel;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.carconnect.provider.tnlink.module.nav.ErrorCode;
import com.telenav.carconnect.provider.tnlink.module.nav.navigation.INavigationConstants;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IMissingAudioProxy;
import com.telenav.data.serverproxy.impl.INavigationProxy;
import com.telenav.data.serverproxy.impl.ITrafficProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.TrafficMissingAudioProxy;
import com.telenav.datatypes.nav.NavDataFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.location.TnLocation;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.ITrafficCallback;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.mvc.ICommonConstants;
import com.telenav.nav.NavEngine;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.threadpool.Notifier;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author xiangli
 *@date 2012-3-1
 */
public class TrafficModel extends AbstractBaseModel implements ITrafficConstants, IServerProxyListener
{
    private long alertId;
    
    private boolean isMinimizeAllDelay = false;
    private boolean isAvoidSelectedSegemnt = false;
    
    private NavState trafficSummaryMarker = null;
    
    protected INavigationProxy navigationProxy;

    protected ITrafficProxy trafficProxy;
    
    private int routeId = -1;
//    private ITrafficCallback callBack;
    @Override
    protected void handleEvent(int actionId)
    {
        // TODO Auto-generated method stub
        this.routeId = getInt(INavigationConstants.KEY_I_ROUTENAME); 
        doGetTrafficSummary();
    }

    TrafficModel()
    {
        boolean isCameraTurnOn = true;
        boolean isSpeedTrapOn = true;
        IMissingAudioProxy missingAudioProxy = ServerProxyFactory.getInstance().createMissingAudioProxy(null, CommManager.getInstance().getComm(), this);
        TrafficMissingAudioProxy trafficMissingAudioProxy = ServerProxyFactory.getInstance().createTrafficMissingAudioProxy(Notifier.getInstance(), missingAudioProxy);
        trafficProxy = ServerProxyFactory.getInstance().createTrafficProxy(null, CommManager.getInstance().getComm(), this, isCameraTurnOn, isSpeedTrapOn, trafficMissingAudioProxy);
        trafficSummaryMarker = NavDataFactory.getInstance().createNavState(-1);
        navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(null, CommManager.getInstance().getComm(), this, missingAudioProxy);
    }
    
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
    
    public void setTrafficCallback(ITrafficCallback callBack) {
        
    }
    
    protected void doActionDelegate(int actionId)
    {
        Log.i("Traffic","doActionDelegate");
        switch(actionId)
        {
            case ACTION_GET_TRAFFIC_SUMMARY:
            {
                doGetTrafficSummary();
                break;
            }
            case ACTION_AVOID_SELECTED:
            {
                doAvoidSelectedSegment();
                break;
            }
            case ACTION_REROUTE:
            {
                doReroute();
                break;
            }
        }

    }
    
    private void doReroute()
    {
        if (!isMinimizeAllDelay && isAvoidSelectedSegemnt)
        {
            int index = this.getInt(KEY_I_INDEX);
            TrafficSegment[] segments = (TrafficSegment[]) get(KEY_A_SEGMENTS);
            if (index >= 0 && index < segments.length)
            {
                boolean canAvoidSegment = index > getCurrentTrafficSegmentIndex();
                if (!canAvoidSegment)
                {
                    this.put(KEY_S_ERROR_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(
                        IStringNav.RES_NO_BETTER_ROUTE, IStringNav.FAMILY_NAV));
                    this.postModelEvent(EVENT_MODEL_POST_ERROR);
                    return;
                }
            }
        }

        Address destAddress = (Address) get(ICommonConstants.KEY_O_ADDRESS_DEST);
        Stop destStop = destAddress.getStop();

        navigationProxy.requestSelectedReroute(0, true, destStop, 0);
    }
    
    private void doGetTrafficSummary()
    {
        Route route = RouteWrapper.getInstance().getCurrentRoute();
        int routeId = -1;
        if (route != null)
        {
            routeId = route.getRouteID();
        }
        else
        {
            routeId = this.routeId;
        }
//        ITrafficCallback trafficCallback = this.callBack;//(ITrafficCallback) this.get(ICommonConstants.KEY_O_TRAFFIC_CALLBACK);
        NavState currentNavState;
//        if(this.callBack != null)
//        {
//            currentNavState = this.callBack.getCurrentNavState();
//        }
//        else
//        {
            currentNavState = NavEngine.getInstance().getCurrentNavState();
//        }
        int routeSegmentIndex;
        int edgeIndex;
        if(currentNavState != null)
        {
            routeSegmentIndex = currentNavState.getSegmentIndex();
            edgeIndex = currentNavState.getEdgeIndex();
            trafficSummaryMarker.set(currentNavState);
        }
        else
        {
            routeSegmentIndex = 0;
            edgeIndex = 0;
        }
        trafficProxy.requestTrafficSummary(routeId, routeSegmentIndex, edgeIndex, null);
    }
    
//    private int getCurrentDistance()
//    {
//        ITrafficCallback trafficCallback = (ITrafficCallback) this.get(ICommonConstants.KEY_O_TRAFFIC_CALLBACK);
//
//        if (trafficCallback == null)
//        {
//            return 0;
//        }
//
//        NavState navState = trafficCallback.getCurrentNavState();
//
//        if (navState == null)
//        {
//            return 0;
//        }
//        int routeId = navState.getRoute();
//
//        Route route = RouteWrapper.getInstance().getRoute(routeId);
//
//        if (route != null)
//        {
//            int distToDest = 0;
//            for (int i = 0; i <= navState.getSegmentIndex(); i++)
//            {
//                distToDest += route.getSegments()[i].getLength();
//            }
//            if (navState.getDistanceToTurn() > 0)
//            {
//                distToDest -= navState.getDistanceToTurn();
//            }
//
//            return distToDest;
//        }
//
//        return 0;
//    }
    
    private void doAvoidSelectedSegment()
    {
        ITrafficCallback trafficCallback = (ITrafficCallback) this.get(ICommonConstants.KEY_O_TRAFFIC_CALLBACK);

        NavState currentNavState;
        if (trafficCallback != null)
        {
            currentNavState = trafficCallback.getCurrentNavState();
        }
        else
        {
            currentNavState = NavEngine.getInstance().getCurrentNavState();
        }
        
        isMinimizeAllDelay = false;
        isAvoidSelectedSegemnt = true;

        int index = this.getInt(KEY_I_INDEX);

        TrafficSegment[] segments = (TrafficSegment[]) get(KEY_A_SEGMENTS);
        
        if (segments == null)
        {
            return;
        }

        if (index >= 0 && index < segments.length)
        {
            boolean canAvoidSegment = index > getCurrentTrafficSegmentIndex();
            if (!canAvoidSegment)
            {
                return;
            }
        }

        String[] tmcIDs = segments[index].getTmcIDs();

        boolean isComingFromCommute = this.optBool(KEY_B_IS_FROM_COMMUTE_ALERT_MAP, false);

        if (isComingFromCommute)
        {
            navigationProxy.requestCommuteAlertAvoidSegment(tmcIDs, alertId);
        }
        else
        {
            if (trafficCallback != null)
                trafficCallback.suspendDeviation();

            boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
            if (isDynamicRoute)
            {
                int gpsCount = 5;
                TnLocation[] locations = new TnLocation[gpsCount];

                for (int i = 0; i < gpsCount; i++)
                {
                    // FIXME: why we have to pass in provider?
                    locations[i] = new TnLocation("");
                }

                gpsCount = LocationProvider.getInstance().getGpsLocations(locations, gpsCount);
                navigationProxy.requestAvoidSegment(currentNavState, tmcIDs, locations, gpsCount, getRouteStyle(), locations[0]
                        .getHeading());
            }
            else
            {
                Address orignalAddress = (Address) get(ICommonConstants.KEY_O_ADDRESS_ORI);
                Stop orignalStop = null;

                if (orignalAddress != null)
                {
                    orignalStop = orignalAddress.getStop();
                }

                navigationProxy.requestStaticAvoidSegment(tmcIDs, orignalStop, getRouteStyle());
            }
        }
    }
    
    protected int getRouteStyle()
    {
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        return prefDao.getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
    }
    
    public int getCurrentTrafficSegmentIndex()
    {
        int distanceFromRequest = distanceFromTrafficSummaryMarker();
        int distance = 0;
        TrafficSegment[] segments = (TrafficSegment[]) get(KEY_A_SEGMENTS);

        for (int i = 0; i < segments.length; i++)
        {
            TrafficSegment segment = segments[i];
            if (segment == null)
                continue;
            distance += segment.getLength();
            if (distance > distanceFromRequest)
            {
                return i;
            }
        }
        return 0;
    }

    private int distanceFromTrafficSummaryMarker()
    {
        ITrafficCallback trafficCallback = (ITrafficCallback) this.get(ICommonConstants.KEY_O_TRAFFIC_CALLBACK);

        if (trafficCallback == null)
        {
            return 0;
        }

        NavState onRoute = trafficCallback.getCurrentNavState();
        RouteEdge currentEdge = onRoute.getCurrentEdge();
        int edgeID = currentEdge.getID();
        return trafficSummaryMarker.getDistanceToEdge(edgeID, 10000);

    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        if(IServerProxyConstants.ACT_TRAFFIC_SUMMARY.equals(action))
        {
//            long lastUpdateDurationTime = trafficProxy.getLastUpdateDurationTime();
//            int incidentNumber = trafficProxy.getTotalIncident();
            int incidentDelay = trafficProxy.getTotalDelay();
            
            TrafficSegment[] segments = trafficProxy.getSegments();
            put(KEY_O_TRAFFICINFOR, segments);
            put(KEY_I_TOTAL_DELAY, incidentDelay);
            controller.postModelEvent(EVENT_MODEL_RESPONSE_TRAFFIC_SEGMENT);
        }
        else if (IServerProxyConstants.ACT_AVOID_REROUTE.equals(action))
        {
            int status = navigationProxy.getResponseCode();
            switch(status)
            {
                case INavigationProxy.RESPONSE_CODE_NEW_ROUTE:
                {
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
        else if (IServerProxyConstants.ACT_AVOID_SELECT_SEGMENT_DECIMATED.equals(action)
                || IServerProxyConstants.ACT_STATIC_AVOID_SELECT_SEGMENT_DECIMATED.equals(action)
                || IServerProxyConstants.ACT_MINIMIZE_DELAY_DECIMATED.equals(action)
                || IServerProxyConstants.ACT_STATIC_MINIMIZE_DELAY_DECIMATED.equals(action))
        {
            // avoid segment response
            int status = navigationProxy.getResponseCode();
            
            switch(status)
            {
                case INavigationProxy.RESPONSE_CODE_ROUTE_CHOICES:
                {
//                    Route[] choices = navigationProxy.getChoices();
//
//                    int[] etas = navigationProxy.getRouteChoiceETA();
//
//                    int[] trafficDelays = navigationProxy.getRouteChoicesTrafficDelay();
//
//                    int[] lengths = navigationProxy.getRouteChoicesLength();
                    
                    break;
                }
                default:
                {
                    break;
                }
            }           
  
        }
    }
    
    protected void deactivateDelegate()
    {
        MapContainer.getInstance().pause();
        RouteUiHelper.resetCurrentRoute();
    }

    /**
     * Activate current controller.<br>
     * 
     * For example,resume daemon back end job.<br>
     * 
     * @param isUpdateView
     */
    protected void activateDelegate(boolean isUpdateView)
    {
        MapContainer.getInstance().resume();
    }
    
    //TODO NEED TO SHOW MESSAGE? JUST COMMET OUT TO FIX BUGS FOUND BY FINDBUGS
    public void networkError(AbstractServerProxy proxy, byte statusCode)
    {
//        String errorMessage = proxy.getErrorMsg();
//        if (errorMessage == null || errorMessage.length() == 0)
//        {
//            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
//        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        MyLog.setLog("error", proxy.getErrorMsg());
        put(KEY_I_ERROR_CODE, ErrorCode.NavigationError_ServiceGeneratedException);
        put(KEY_S_ERROR_MSG, errorMessage);
        controller.postModelEvent(EVENT_MODEL_TRAFFIC_SEGMENT_ERROR);
        //postErrorMessage(errorMessage);
    }

}

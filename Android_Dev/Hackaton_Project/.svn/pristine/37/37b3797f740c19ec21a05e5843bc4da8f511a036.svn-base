/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficModel.java
 *
 */
package com.telenav.module.nav.traffic;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.nav.NavSdkNavState;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.ITrafficCallback;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-10-26
 */
class TrafficModel extends AbstractCommonNetworkModel implements ITrafficConstants, IServerProxyListener, INetworkStatusListener
{
    protected NavSdkNavigationProxy navigationProxy;

    TrafficModel()
    {
        navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(this);
        NetworkStatusManager.getInstance().addStatusListener(this);
    }
    
    protected void releaseDelegate()
    {
        super.releaseDelegate();
        NetworkStatusManager.getInstance().removeStatusListener(this);
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_GET_TRAFFIC_SUMMARY:
            {
                doGetTrafficSummary();
                break;
            }
            case ACTION_CHECK_TRAFFIC_SEGMENT:
            {
                doCheckTrafficSegment();
                break;
            }
            case ACTION_AVOID_SELECTED:
            {
                doAvoidSelectedSegment();
                break;
            }
            case ACTION_MINIMIZE_ALL_DELAY:
            {
                doMinimizeAllDelay();
                break;
            }
            case ACTION_REROUTE:
            {
                doReroute();
                break;
            }
            case ACTION_PLAY_AUDIO:
            {
                doPlayAudio();
                break;
            }
            case ACTION_RESUME_CALLBACK_DEVIATION:
            {
                CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_AVOID_SELECT_SEGMENT_DECIMATED);
                CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_STATIC_AVOID_SELECT_SEGMENT_DECIMATED);
                CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_MINIMIZE_DELAY_DECIMATED);
                CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_STATIC_MINIMIZE_DELAY_DECIMATED);
                break;
            }
        }
    }
    
    private void doPlayAudio()
    {
        if (!MediaManager.getInstance().getAudioPlayer().isPlaying())
        {
            int selectedIndex = this.getInt(KEY_I_INDEX);
            TrafficSegment[] segments = (TrafficSegment[]) get(KEY_A_SEGMENTS);
            
            boolean canPlayAudio = false;
            if (segments != null && selectedIndex >= 0 && selectedIndex < segments.length)
            {
                int distance = 0;
                for (int i = 0; i <= selectedIndex; i++)
                {
                    distance += segments[i].getLength();
                }
                canPlayAudio = distance > getCurrentDistance(); // TNANDROID-6656: If pass by, cannot play the traffic incident audio.
            }
            if (canPlayAudio)
            {
                playTrafficSegmentAudio(selectedIndex);
            }
        }
    }
    
    
    protected boolean isAudioDataReady(AudioDataNode audioNodes)
    {
        if(audioNodes == null || audioNodes.getChildrenSize() == 0)
            return false;
        
        for (int i = 0; i < audioNodes.getChildrenSize(); i++)
        {
            AudioDataNode audioNode = audioNodes.getChild(i);
            if (audioNode.getAudioData() != null)
            {
                int audioId = (int) audioNode.getAudioData().getResourceId();
                if (audioNode.getAudioData().getData() == null
                        && AbstractDaoManager.getInstance().getResourceBarDao().getAudio(audioId) == null)
                {
                    return false;
                }
            }
        }
        return true;
    }
   
    private void playTrafficSegmentAudio(int segmentIndex)
    {
//        if(seg != null && seg.getIncidents() != null && seg.getIncidents().length > 0 )
//        {
//            for( int i = 0; i < seg.getIncidents().length; i++ )
//            {
//                if(seg.getIncidents()[i] == null)
//                    continue;
//                TrafficIncident incident = seg.getIncidents()[i];
//                
//                NavState navState= NavSdkNavEngine.getInstance().getCurrentNavState();
//                if(navState != null)
//                {
//                    int distance = navState.getDistanceToEdge(incident.getEdgeID(), 200000);
//
//                    if (isAudioDataReady(incident.getStreetAudio()))
//                    {
//                        AudioData[] playList = AudioComposer.getInstance().createIncidentPrompt(distance, incident.getIncidentType(),
//                            incident.getStreetAudio(), incident.getLocationAudio(), incident.getLaneClosed());
//                        MediaManager.getInstance().getAudioPlayer().play(ID_TRAFFIC_AUDIO, playList, -1);
//                        this.put(KEY_B_IS_TRAFFIC_AUDIO_PLAY, true);
//                    }
//                }
//            }
//        }
//        else
//        {
//            if (this.getState() != STATE_SHOW_SUMMARY)
//            {
//                MediaManager.getInstance().playStaticAudio(IAudioRes.TRAFFIC_NO_INCIDENT_SEGMENT);
//                this.put(KEY_B_IS_TRAFFIC_AUDIO_PLAY, true);
//            }
//        }
        
        navigationProxy.sendTrafficInfoRequest(segmentIndex);
    }
    
    private void doReroute()
    {
        Route[] routes = (Route[]) get(KEY_A_ROUTE_CHOICES);
        int[] lengths = (int[]) get(KEY_A_ROUTE_CHOICES_LENGTH);
        for(int i=0; i<routes.length; i++)
        {
            int routeId = routes[i].getRouteID();
            if(routeId == getInt(KEY_I_NEW_ROUTE_ID))
            {
                int distance = lengths[i];
                KontagentAssistLogger.ktLogNavigationDistance(distance);
                break;
            }
        }
        navigationProxy.requestRouteChoicesSelection("" + this.getInt(KEY_I_NEW_ROUTE_ID), true);
    }
    
    private void doGetTrafficSummary()
    {
        navigationProxy.sendTrafficDetailRequest();
    }
    
    private void doCheckTrafficSegment()
    {
        int index = this.getInt(KEY_I_INDEX);

        TrafficSegment[] segments = (TrafficSegment[]) get(KEY_A_SEGMENTS);

        if (index >= 0 && index < segments.length)
        {
            TrafficSegment segment = segments[index];
            this.put(KEY_O_SEGMENT, segment);
            int distance = 0;
            for (int i = 0; i < index; i++)
            {
                distance += segments[i].getLength();
            }
            boolean canAvoidSegment = false;
            if (index != 0)
            {
                canAvoidSegment = distance > getCurrentDistance();
            }
            this.put(KEY_B_CAN_AVOID_SEGMENT, canAvoidSegment);
            this.postModelEvent(EVENT_MODEL_SHOW_TRAFFIC_SEGMENT);
        }
        else
        {

            this.postModelEvent(EVENT_MODEL_TRAFFIC_SEGMENT_ERROR);
        }
    }
    
    public int getCurrentDistance()
    {
        ITrafficCallback trafficCallback = (ITrafficCallback) this.get(ICommonConstants.KEY_O_TRAFFIC_CALLBACK);

        if (trafficCallback == null)
        {
            return 0;
        }

        NavSdkNavState navState = trafficCallback.getCurrentNavState();

        if (navState == null)
        {
            return 0;
        }
        int routeId = NavSdkRouteWrapper.getInstance().getCurrentRouteId();

//        Route route = RouteWrapper.getInstance().getRoute(routeId);
        Route route = NavSdkRouteWrapper.getInstance().getRoute(routeId);

        if (route != null)
        {
            int distToDest = 0;
            for (int i = 0; i <= navState.getSegmentIndex(); i++)
            {
                distToDest += route.getSegments()[i].getLength();
            }
            if (navState.getDistanceToTurn() > 0)
            {
                distToDest -= navState.getDistanceToTurn();
            }

            return distToDest;
        }

        return 0;
    }
    
    private void doAvoidSelectedSegment()
    {
//        isMinimizeAllDelay = false;
//        isAvoidSelectedSegemnt = true;

        int index = this.getInt(KEY_I_INDEX);

        TrafficSegment[] segments = (TrafficSegment[]) get(KEY_A_SEGMENTS);
        
        if (segments == null)
        {
            this.put(KEY_S_COMMON_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NO_BETTER_ROUTE,
                IStringNav.FAMILY_NAV));
            this.postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
            return;
        }

        if (index >= 0 && index < segments.length)
        {
            boolean canAvoidSegment = index > getCurrentOnRouteSegmentIndex();
            if (!canAvoidSegment)
            {
                this.put(KEY_S_COMMON_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NO_BETTER_ROUTE,
                    IStringNav.FAMILY_NAV));
                this.postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                return;
            }
        }

//        String[] tmcIDs = segments[index].getTmcIDs();

        boolean isComingFromCommute = this.optBool(KEY_B_IS_FROM_COMMUTE_ALERT_MAP, false);

        if (isComingFromCommute)
        {
            //TODO
//            navigationProxy.requestCommuteAlertAvoidSegment(tmcIDs, alertId);
        }
        else
        {
//            boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
//            if (isDynamicRoute)
//            {

//                int gpsCount = 5;
//                TnLocation[] locations = new TnLocation[gpsCount];
//
//                for (int i = 0; i < gpsCount; i++)
//                {
//                    // FIXME: why we have to pass in provider?
//                    locations[i] = new TnLocation("");
//                }
//
//                gpsCount = LocationProvider.getInstance().getGpsLocations(locations, gpsCount);
//                navigationProxy.requestAvoidSegment(currentNavState, tmcIDs, locations, gpsCount, getRouteStyle(), locations[0]
//                        .getHeading());
                navigationProxy.sendCheckForBetterRouteRequest(index);
//            }
//            else
//            {
//                Address orignalAddress = (Address) get(ICommonConstants.KEY_O_ADDRESS_ORI);
//                Stop orignalStop = null;
//
//                if (orignalAddress != null)
//                {
//                    orignalStop = orignalAddress.getStop();
//                }
//                
                //TODO
//                navigationProxy.requestStaticAvoidSegment(tmcIDs, orignalStop, getRouteStyle());
//            }
        }
    }
    
    private void doMinimizeAllDelay()
    {
        ITrafficCallback trafficCallback = (ITrafficCallback) this.get(ICommonConstants.KEY_O_TRAFFIC_CALLBACK);
        
//        isMinimizeAllDelay = true;
//        isAvoidSelectedSegemnt = false;
        
        boolean isComingFromCommute = this.optBool(KEY_B_IS_FROM_COMMUTE_ALERT_MAP, false);
        
        if (isComingFromCommute)
        {
            //TODO
//            navigationProxy.requestCommuteAlertMinimizeDelay(alertId);
        }
        else
        {
            // no extra parameter is required by navsdk
            navigationProxy.requestMinimizeDelay();
//            boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
//            if (isDynamicRoute)
//            {
//                
//                int gpsCount = 5;
//                TnLocation[] locations = new TnLocation[gpsCount];
//
//                for (int i = 0; i < gpsCount; i++)
//                {
//                    // FIXME: why we have to pass in provider?
//                    locations[i] = new TnLocation("");
//                }
//                
//                gpsCount = LocationProvider.getInstance().getGpsLocations(locations, gpsCount);
//                //TODO
////                navigationProxy.requestMinimizeDelay(trafficCallback.getCurrentNavState(), null, locations, gpsCount, getRouteStyle(),
////                    locations[0].getHeading());
//            }
//            else
//            {
//
//                Address orignalAddress = (Address) get(ICommonConstants.KEY_O_ADDRESS_ORI);
//                Stop orignalStop = null;
//
//                if (orignalAddress != null)
//                {
//                    orignalStop = orignalAddress.getStop();
//                }
//                
//                //TODO
////                navigationProxy.requestStaticMinimizeDelay(orignalStop, getRouteStyle());
//            }
        }
        
    }
    
    protected int getRouteStyle()
    {
        return DaoManager.getInstance().getTripsDao().getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
    }

    public int getCurrentOnRouteSegmentIndex()
    {
        ITrafficCallback trafficCallback = (ITrafficCallback) this.get(ICommonConstants.KEY_O_TRAFFIC_CALLBACK);
        if (trafficCallback == null)
        {
            return 0;
        }
        NavSdkNavState navState = trafficCallback.getCurrentNavState();
        if (navState == null)
        {
            return 0;
        }
        return navState.getSegmentIndex();
    }

    private void avoidSelectedSegmentError()
    {
        this.put(KEY_S_COMMON_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NO_BETTER_ROUTE,
            IStringNav.FAMILY_NAV));
        this.postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        if (INavSdkProxyConstants.ACT_NAV_TRAFFIC_DETAIL.equals(action))
        {
            int incidentNumber = navigationProxy.getTotalIncident();
            int incidentDelay = navigationProxy.getTotalDelay();
            TrafficSegment[] segments = navigationProxy.getTrafficSegments();
            put(KEY_A_SEGMENTS, segments);
            put(KEY_I_INCIDENT_NUMBER, incidentNumber);
            put(KEY_I_TOTAL_DELAY, incidentDelay);
            postModelEvent(EVENT_MODEL_SHOW_SUMMARY);
        }
        // else if (IServerProxyConstants.ACT_AVOID_REROUTE.equals(action))
        // {
        // // int status = navigationProxy.getResponseCode();
        // int status = 0;
        // switch(status)
        // {
        // case INavigationProxy.RESPONSE_CODE_NEW_ROUTE:
        // {
        // boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() ==
        // NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
        // if(isDynamicRoute)
        // {
        // postModelEvent(EVENT_MODEL_RENEW_DYNAMIC_ROUTE);
        // }
        // else
        // {
        // postModelEvent(EVENT_MODEL_RENEW_STATIC_ROUTE);
        // }
        // break;
        // }
        // default:
        // {
        // avoidSelectedSegmentError();
        // break;
        // }
        // }
        // }
        else if (INavSdkProxyConstants.ACT_NAV_CHECK_FOR_BETTER_ROUTE
                .equalsIgnoreCase(action))
        {
            Route[] choices = navigationProxy.getChoices();
            put(KEY_A_ROUTE_CHOICES, choices);

            int[] etas = navigationProxy.getRouteChoiceETA();
            put(KEY_A_ROUTE_CHOICES_ETA, etas);

            int[] trafficDelays = navigationProxy.getRouteChoicesTrafficDelay();
            put(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY, trafficDelays);

            int[] lengths = navigationProxy.getRouteChoicesLength();
            put(KEY_A_ROUTE_CHOICES_LENGTH, lengths);
            this.postModelEvent(EVENT_MODEL_ALTERNATE_ROUTE);
        }
        else if (INavSdkProxyConstants.ACT_NAV_NAVIGATE_USING_ROUTE.equals(action))
        {
            boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
            if (isDynamicRoute)
            {
                postModelEvent(EVENT_MODEL_RENEW_DYNAMIC_ROUTE);
            }
            else
            {
                postModelEvent(EVENT_MODEL_RENEW_STATIC_ROUTE);
            }
        }
        else if (INavSdkProxyConstants.ACT_NAV_TRAFFIC_INCIDENTS_INFO
                .equalsIgnoreCase(action))
        {
            avoidSelectedSegmentError();
        }

    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        String noBetterRoute = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NO_BETTER_ROUTE,
            IStringNav.FAMILY_NAV);
        if(noBetterRoute.equals(proxy.getErrorMsg()))
        {
            this.put(ICommonConstants.KEY_S_COMMON_MESSAGE, proxy.getErrorMsg());
            this.postModelEvent(ICommonConstants.EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
        }
        else
        {
            super.transactionError(proxy);
        }
    }
    
    protected void deactivateDelegate()
    {
        MapContainer.getInstance().pause();
        RouteUiHelper.resetCurrentRoute();
        MediaManager.getInstance().getAudioPlayer().cancelJob(ID_TRAFFIC_AUDIO);
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

    @Override
    public void statusUpdate(boolean isConnected)
    {
        if (isConnected)
        {
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
        else
        {
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TurnMapModel.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.app.CommManager;
import com.telenav.app.ThreadManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.threadpool.ThreadPool;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author yning
 *@date 2010-12-13
 */
public class TurnMapModel extends AbstractCommonNetworkModel implements ITurnMapConstants, INotifierListener, INetworkStatusListener
{
    protected NavSdkNavigationProxy navigationProxy = null; 
    protected int audioSegIndex;
    protected final int INTERVAL =  3000;
    protected long lastNotifyTimestamp = -1L;
    protected TurnMapWrapper turnMapWrapper = null;
    protected boolean isNavStarted = false;
    
    protected TurnMapModel()
    {
//        IMissingAudioProxy missingAudioProxy = ServerProxyFactory.getInstance().createMissingAudioProxy(null, CommManager.getInstance().getComm(), this);
        navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(this);
        NetworkStatusManager.getInstance().addStatusListener(this);
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_INIT:
            {
                boolean isStaticRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
                if (isStaticRoute)
                {
                    postModelEvent(EVENT_MODEL_GET_TURN_MAP);
                }
                else
                {
                    startStaticNav();
                }
                break;
            }
            case ACTION_SHOW_TURN_MAP:
            {
                startStaticNav();
                break;
            }
            case ACTION_GETTING_TURN_MAP:
            {
                doGetTurnMaps();
                break;
            }
            case ACTION_CANVAS_NEXT:
            {
                doHandleCanvasNext();
                break;
            }
            case ACTION_CANVAS_PREVIOUS:
            {
                doHandleCanvasPrevious();
                break;
            }
            case ACTION_GET_ROUTE_EDGE:
            {
                doGetRouteEdge();
                break;
            }
            case ACTION_GET_EXTRA_ROUTE_EDGE:
            {
                doGetExtraRouteEdge();
                break;
            }
            case ACTION_CANCEL_NETWORK:
            {
                doCancelNetwork();
                break;
            }
            case ACTION_SYNC_PURCHASE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance()
                        .createSyncPurchaseProxy(null,
                            CommManager.getInstance().getComm(), this, userProfileProvider);
                syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
                break;
            }
        }
    }

    protected int getPreference(int id)
    {
        PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
        return prefDao.getIntValue(id);
    }
    
    protected void doGetTurnMaps()
    {
        int routeID = getInt(KEY_I_ROUTE_ID);
        navigationProxy.sendRouteSummaryRequest(routeID+"");
    }
    
    private void doGetRouteEdge()
    {
        turnMapWrapper.provideSegment(true, false);
    }
    
    private void doGetExtraRouteEdge()
    {
//Need NavSDK to confirm whether client to get the turn map segment        
//        if(getBool(KEY_B_IS_GETTING_EXTRA))
//            return;
//        MapContainer.getInstance().postRenderEvent(new Runnable() {
//        
//
//            public void run()
//            {
//                if (turnMapWrapper == null)
//                    return;
//                IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//                IMapEngine.AnnotationSearchResult[] startAnnotationsSearchResults = mapEngine.getNearestAnnotations(MapContainer.getInstance().getViewId(), -100, -100, false);
//                IMapEngine.AnnotationSearchResult[] endAnnotationsSearchResults = mapEngine.getNearestAnnotations(MapContainer.getInstance().getViewId(), 
//                    PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth()) + 100, 
//                    PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight()) + 100, false);
//                if(startAnnotationsSearchResults != null && endAnnotationsSearchResults != null
//                        && startAnnotationsSearchResults.length > 0 && endAnnotationsSearchResults.length > 0)
//                {
//                    double[] beginPoint = new double[2];
//                    double[] endPoint = new double[2];
//                    IMapEngine.AnnotationSearchResult.LatLonPickable startLatlon =
//                    null, endLatlon =
//                    null;
//                    for(int i=0;i < startAnnotationsSearchResults.length;i++)
//                    {
//                        if(startAnnotationsSearchResults[i].pickable instanceof IMapEngine.AnnotationSearchResult.LatLonPickable)
//                        {
//                            startLatlon = (IMapEngine.AnnotationSearchResult.LatLonPickable)startAnnotationsSearchResults[i].pickable;
//                            break;
//                        }
//                    }
//                    
//                    for(int i=0;i < endAnnotationsSearchResults.length;i++)
//                    {
//                        if(endAnnotationsSearchResults[i].pickable instanceof IMapEngine.AnnotationSearchResult.LatLonPickable)
//                        {
//                            endLatlon = (IMapEngine.AnnotationSearchResult.LatLonPickable)endAnnotationsSearchResults[i].pickable;
//                            break;
//                        }
//                    }
//                    
//                    if(startLatlon != null && endLatlon != null)
//                    {
//                        beginPoint[0] = startLatlon.latitude;
//                        beginPoint[1] = startLatlon.longitude;
//                        endPoint[0] = endLatlon.latitude;
//                        endPoint[1] = endLatlon.longitude;
//                        Segment lastSegmentWithEdge = turnMapWrapper.segmentAt(turnMapWrapper.getLastSegmentIndexWithEdge());
//                        if(lastSegmentWithEdge == null ||  lastSegmentWithEdge.getEdge(lastSegmentWithEdge.edgesSize() -1) == null)
//                            return;
//                        RouteEdge lastEdgeWithPoint = lastSegmentWithEdge.getEdge(lastSegmentWithEdge.edgesSize() -1);
//                        double lastPointLat = lastEdgeWithPoint.latAt(lastEdgeWithPoint.numPoints() - 1) / 100000.0d;;
//                        double lastPointLon = lastEdgeWithPoint.lonAt(lastEdgeWithPoint.numPoints() - 1) / 100000.0d;;
//                        if( ((lastPointLat > beginPoint[0] && lastPointLat < endPoint[0]) || (lastPointLat < beginPoint[0] && lastPointLat > endPoint[0]))
//                                && ((lastPointLon > beginPoint[1] && lastPointLon < endPoint[1]) || (lastPointLon < beginPoint[1] && lastPointLon > endPoint[1])) )
//                        {
//                            turnMapWrapper.provideSegment(true, true);
//                            put(KEY_B_IS_GETTING_EXTRA, true);
//                        }
//                    }
//                }
//            }
//        });
//        
    }
    
    protected void startStaticNav()
    {
        int currentSegmentIndex = this.getInt(KEY_I_CURRENT_SEGMENT_INDEX);
        if(currentSegmentIndex < 0)
            currentSegmentIndex = 0;
        
        boolean isStatic = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;

        audioSegIndex = 0;
        turnMapWrapper = new TurnMapWrapper(currentSegmentIndex, navigationProxy);
        turnMapWrapper.provideSegment(true, false);
        if (isStatic)
        {
            Notifier.getInstance().addListener(this);
        }

//        //put(KEY_B_UPDATE_TURN_MAP, true);
//        if (!turnMapWrapper.currentSegmentDataPresent())
//        {
//            postModelEvent(EVENT_MODEL_REQUEST_ROUTE_EDGE);
//        }
//        else
//        {
            postModelEvent(EVENT_MODEL_SHOW_TURN_MAP);
//        }
    }
    
    private void doHandleCanvasNext()
    {
        if(turnMapWrapper.segmentDataPresent(turnMapWrapper.getCurrentSegmentIndex() + 1))
        {
            if(turnMapWrapper.goToNextTurn())
            {
                put(KEY_I_CURRENT_SEGMENT_INDEX, turnMapWrapper.getCurrentSegmentIndex());
                put(KEY_B_UPDATE_TURN_ARROW, true);
                if (isPlayAudioEnabled())
                {
                    playCurrentSegment(turnMapWrapper.getCurrentSegmentIndex());
                }
            }
            else
            {
                //put(KEY_B_UPDATE_TURN_MAP, false);
            }
        }
//        else
//        {
//            postModelEvent(EVENT_MODEL_REQUEST_ROUTE_EDGE);
//        }
    }
    
    private void doHandleCanvasPrevious()
    {
        if(turnMapWrapper.segmentDataPresent(turnMapWrapper.getCurrentSegmentIndex() - 1))
        {
            if(turnMapWrapper.goToPreviousTurn())
            {
                put(KEY_I_CURRENT_SEGMENT_INDEX, turnMapWrapper.getCurrentSegmentIndex());
                put(KEY_B_UPDATE_TURN_ARROW, true);
                if (isPlayAudioEnabled())
                {
                    playCurrentSegment(turnMapWrapper.getCurrentSegmentIndex());
                }
            }
            else
            {
                //put(KEY_B_UPDATE_TURN_MAP, false);
            }
        }
//        else
//        {
//            postModelEvent(EVENT_MODEL_REQUEST_ROUTE_EDGE);
//        }
    }
    
    private void playCurrentSegment(int curSegmentIndex)
    {
        if (!MediaManager.getInstance().getAudioPlayer().isPlaying())
        {
            navigationProxy.sendGuidanceRequest(curSegmentIndex - 1);
        }
    }
    
    private void playAudio(int index)
    {
        //Fix bug 70408. Per PM, doesn't play any audio in RouteSummary.
        
        // don't play the first segment        
//        if (index >= 0)
//        {
////            Route route = RouteWrapper.getInstance().getCurrentRoute();
//            Route route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
//            Segment[] segments = route.getSegments();
//            if (index >= segments.length)
//            {
//                index = segments.length - 1;
//            }
//            Segment seg = segments[index];
//            
//            if (!MediaManager.getInstance().getAudioPlayer().isPlaying())
//            {
//                byte audioType;
//                if(seg.getAudioAssembleType() == INavigationProxy.AUDIO_ASSEMBLE_TYPE_CN)
//                {
//                    if(seg.getPrepAudio() != null)
//                    {
//                        audioType = Route.AUDIO_TYPE_PREP;
//                    }
//                    else if(seg.getInfoAudio() != null)
//                    {
//                        audioType = Route.AUDIO_TYPE_INFO;
//                    }
//                    else
//                    {
//                        audioType = Route.AUDIO_TYPE_ACTION;
//                    }
//                }
//                else
//                {
//                    audioType = Route.AUDIO_TYPE_INFO;
//                }
//                NavAudioEvent audioEvent = new NavAudioEvent(route.getRouteID(), index, seg.getLength(), audioType, -1, -1, true);
//                AudioData[] playList = AudioComposer.getInstance().createPrompt(audioEvent);
//                MediaManager.getInstance().getAudioPlayer().play(ID_TURN_MAP_TURN_MAP_AUDIO, playList, -1);
//            }
//        }
    }
    
    private void doCancelNetwork()
    {
        turnMapWrapper.cancelRequest();
    }
    
    TurnMapWrapper getTurnMapWrapper()
    {
        return turnMapWrapper;
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        String requestAction = proxy.getRequestAction();
        if(INavSdkProxyConstants.ACT_NAV_ROUTE_SUMMARY.equalsIgnoreCase(requestAction))
        {
            put(KEY_B_UPDATE_TURN_ARROW, true);
            if (!isNavStarted)
            {
                startStaticNav();
            }
        }
        else if(IServerProxyConstants.ACT_CHUNKED_SET_ROUTE_CHOICES_SELECTION.equalsIgnoreCase(requestAction)
                || IServerProxyConstants.ACT_SET_ROUTE_CHOICES_SELECTION.equalsIgnoreCase(requestAction))
        {
            if (!isNavStarted)
            {
                startStaticNav();
            }
        }
        else if(IServerProxyConstants.ACT_GET_EDGE.equalsIgnoreCase(requestAction))
        {
            put(KEY_B_UPDATE_TURN_ARROW, true);
            if(fetchBool(KEY_B_IS_GETTING_EXTRA))
            {
                put(KEY_B_IS_GETTING_EXTRA, false);
                put(KEY_B_FROM_GETTING_EXTRA, true);
            }
            if(getState() != STATE_TURN_MAP) 
            {
                postModelEvent(EVENT_MODEL_SHOW_TURN_MAP);
                playAudio(turnMapWrapper.getCurrentSegmentIndex() - 1);
            }
        }
        else if(IServerProxyConstants.ACT_SYNC_PURCHASE.equalsIgnoreCase(requestAction))
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                handleAccountFatalError();
            }
            this.postModelEvent(EVENT_MODEL_GET_TURN_MAP);
        }
    }
    
    protected void releaseDelegate()
    {
        Notifier.getInstance().removeListener(this);
        NetworkStatusManager.getInstance().removeStatusListener(this);
    }
    
    //Implement interface INotifierListener
    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    public long getNotifyInterval()
    {
        return INTERVAL;
    }

    //TODO: This logic need deeply discuss.
    //e.g. user select the middle segment in route summary, however we still request the audio sequentially.
    public void notify(long timestamp)
    {
        ThreadPool pool = ThreadManager.getPool(ThreadManager.TYPE_COMM_REQUEST);

        if (!pool.isIdle())
        {
            return;
        }

//        Route selectedRoute = RouteWrapper.getInstance().getCurrentRoute();
        Route selectedRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (selectedRoute == null)
            return;

        if (audioSegIndex > selectedRoute.segmentsSize())
        {
            Notifier.getInstance().removeListener(this);
            return;
        }

        //TODO
//        navigationProxy.requestDynamicAudio();
        audioSegIndex++;
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestamp = timestamp;
    }

    //*******************************************************
    private Stop getDestStop()
    {
        Address address = (Address)get(KEY_O_ADDRESS_DEST);
        Stop destStop = address.getStop();
        if(destStop == null)
        {
            throw new IllegalArgumentException(getClass().toString() + ": destStop == null!!!!");
        }
        else
        {
            return destStop;
        }
    }
    
    private Stop getOriginStop()
    {
        Stop originStop = null;
        Address originAddress = (Address)get(KEY_O_ADDRESS_ORI);
        originStop = originAddress.getStop();
        if(originStop == null)
        {
            throw new IllegalArgumentException(getClass().toString() + ": originStop == null!!!!");
        }
        else
        {
            return originStop;
        }
    }
    
    private TnLocation[] getStopLocation(Stop stop)
    {
        if (stop == null)
        {
            return null;
        }
        TnLocation[] data = new TnLocation[3];
        for (int i = 0; i < 3; i++)
        {
            data[i] = new TnLocation("");
        }
        data[0].setTime(System.currentTimeMillis());
        data[0].setLatitude(stop.getLat());
        data[0].setLongitude(stop.getLon());
        data[0].setSpeed(0);
        data[0].setHeading(0);
        data[0].setAltitude(5);
        data[0].setAccuracy(0);
        data[0].setValid(true);

        // just copy to make fixes the same
        data[2] = data[1] = data[0];
        return data;
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

    /**
     * Deactivate current model.<br>
     * 
     * For example, pause unnecessary daemon back end job.<br>
     * 
     */
    protected void deactivateDelegate()
    {
        isNavStarted = false;
        RouteUiHelper.resetCurrentRoute();
        MapContainer.getInstance().pause();
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if(proxy.getStatus() == IServerProxyConstants.INVALID_IDENTITY)
        {
            this.postModelEvent(EVENT_MODEL_INVALID_IDENTITY); 
            Logger.log(Logger.INFO, this.getClass().getName(), "INVALID_IDENTITY");
            return;
        }
        
        //fix CANNON-125
        if(IServerProxyConstants.ACT_GET_MISSING_AUDIO.equals(proxy.getRequestAction()))
        {
            //TODO show something.
            return;
        }
        else if(IServerProxyConstants.ACT_SYNC_PURCHASE.equalsIgnoreCase(proxy.getRequestAction()))
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                handleAccountFatalError();
            }
            else
            {
                super.transactionError(proxy);
            }
            return;
        }
        super.transactionError(proxy);
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if(IServerProxyConstants.ACT_GET_MISSING_AUDIO.equals(proxy.getRequestAction()))
        {
            //TODO show something.
            return;
        }
        super.networkError(proxy, statusCode, jobId);
    }
    
    protected boolean isPlayAudioEnabled()
    {
        Preference audioPreference = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        boolean audioDisable = audioPreference != null && (audioPreference.getIntValue() == Preference.AUDIO_TRAFFIC_ONLY || audioPreference.getIntValue() == Preference.AUDIO_NONE );
        return !audioDisable;
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        put(KEY_B_UPDATE_MAP_LAYER, true);
        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }
}

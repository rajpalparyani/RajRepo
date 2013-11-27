/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EntryModel.java
 *
 */
package com.telenav.module.map;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.INavigationProxy;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkRgcProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.IRouteChangeListener;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.result.PoiResultController;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.HomeScreenManager;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author JY Xu
 *@date Aug 21, 2010
 */

class MapModel extends BrowserSdkModel implements IMapConstants, PoiDataListener, INetworkStatusListener, IRouteChangeListener
{
    private PoiDataRequester poiDataRequester;
    
    public MapModel()
    {
        NetworkStatusManager.getInstance().addStatusListener(this);
    }

    protected void releaseDelegate()
    {
        super.releaseDelegate();
        NetworkStatusManager.getInstance().removeStatusListener(this);
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_MAP_INIT:
            {
                break;
            }
            case ACTION_BACK_CHECK:
            {
                int mapFromType = getInt(KEY_I_TYPE_MAP_FROM);
                if (mapFromType == TYPE_MAP_FROM_ENTRY || mapFromType == -1)
                {
                    this.put(KEY_B_IS_GOTO_BACKGROUND, true);
                    
                    if (!MapContainer.getInstance().hasCleanAll())
                    {
                        MapContainer.getInstance().removePoiAnnotations();
                        this.put(KEY_B_IS_CLEAR_ONEBOX, true);
                    }

                    if (MapContainer.getInstance().getInteractionMode() == IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
                    {
                        TeleNavDelegate.getInstance().jumpToBackground();
                    }
                    else
                    {
                        TnLocation loc = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
                        if (loc != null)
                        {
                            this.put(KEY_I_TYPE_MAP_FROM, HomeScreenManager.getHomeMapFromType());
                            AbstractTnComponent currentLocation = MapContainer.getInstance().getFeature(ID_CURRENT_LOCATION);
                            if(currentLocation != null)
                            {
                                TnCommandEvent commandEvent = new TnCommandEvent(CMD_CURRENT_LOCATION);
                                TnUiEvent event = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, currentLocation);
                                event.setCommandEvent(commandEvent);
                                currentLocation.dispatchUiEvent(event);
                            }
                            else
                            {
                                TeleNavDelegate.getInstance().jumpToBackground();
                            }
                        }
                        else
                        {
                            TeleNavDelegate.getInstance().jumpToBackground();
                        }
                    }
                }
                else  if(mapFromType == TYPE_MAP_FROM_BROWSER)
                {
                    this.postModelEvent(EVENT_MODEL_COMMON_BACK);
                }
                else
                {
                    if (isControllerInStack(PoiResultController.class.getName()))
                        this.postModelEvent(EVENT_MODEL_POI_LIST);
                    else
                        this.postModelEvent(EVENT_MODEL_COMMON_BACK);
                }
                break;
            }
            case ACTION_CLEAR:
            {
                TnLocation loc = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
                if (loc != null)
                {
                    AbstractTnComponent currentLocation = MapContainer.getInstance().getFeature(ID_CURRENT_LOCATION);
                    if(currentLocation != null)
                    {
                        TnCommandEvent commandEvent = new TnCommandEvent(CMD_CURRENT_LOCATION);
                        TnUiEvent event = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, currentLocation);
                        event.setCommandEvent(commandEvent);
                        currentLocation.dispatchUiEvent(event);
                    }
                }
                postModelEvent(EVENT_MODEL_CLEAR);
                break;
            }
            case ACTION_INIT_MAP_SUMMARY:
            {
                initMapSummary();
                break;
            }
            case ACTION_CHECK_LAYER:
            {
                int layerSetting = this.getInt(KEY_I_MAP_LAYER_SETTING);
                int layerSelect = this.fetchInt(KEY_I_MAP_LAYER);
                boolean isTraffic = (0x01 & layerSetting) != 0;
                boolean isShowSatelliteOverlay = ((layerSetting & 0x02) != 0);  
                boolean isShowTrafficCameraOverlay = ((layerSetting & 0x04) != 0);  
                
                // Fix bug TN-1461,TN-2936
                if (isTraffic && layerSelect == MAP_LAYER_TRAFFIC)
                {
                    if (needResetFeature(FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW))
                    {
                        layerSetting ^= 0x01;
                        this.put(KEY_S_FEATURE_CODE, FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW);
                        this.postModelEvent(EVENT_MODEL_GOTO_TRAFFIC_UPSELL);
                    }
                }
                else if (isShowTrafficCameraOverlay && layerSelect == MAP_LAYER_CAMERA)
                {
                    if (needResetFeature(FeaturesManager.FEATURE_CODE_MAP_LAYER_CAMERA))
                    {
                        layerSetting ^= 0x04;;
                        this.put(KEY_S_FEATURE_CODE, FeaturesManager.FEATURE_CODE_MAP_LAYER_CAMERA);
                        this.postModelEvent(EVENT_MODEL_GOTO_TRAFFIC_UPSELL);
                    }
                }
                else if (isShowSatelliteOverlay && layerSelect == MAP_LAYER_SATELLITE)
                {
                    if (needResetFeature(FeaturesManager.FEATURE_CODE_MAP_LAYER_SATELLITE))
                    {
                        layerSetting ^= 0x02;;
                        this.put(KEY_S_FEATURE_CODE, FeaturesManager.FEATURE_CODE_MAP_LAYER_SATELLITE);
                        this.postModelEvent(EVENT_MODEL_GOTO_TRAFFIC_UPSELL);
                    }
                }
                
                ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_MAP_LAYER_SETTING, layerSetting);
                MapContainer.getInstance().showMapLayer(layerSetting);
                break;
            }
            case ACTION_CHECK_UPSELL_SUCCESS:
            {
                int layerSetting = this.getInt(KEY_I_MAP_LAYER_SETTING);
                boolean isTraffic = (0x01 & layerSetting) != 0;
                boolean isShowSatelliteOverlay = ((layerSetting & 0x02) != 0);  
                boolean isShowTrafficCameraOverlay = ((layerSetting & 0x04) != 0);  

                if (isTraffic && needResetFeature(FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW))
                {
                    layerSetting  ^=  0x01;
                }

                if (isShowSatelliteOverlay && needResetFeature(FeaturesManager.FEATURE_CODE_MAP_LAYER_SATELLITE))
                {
                    layerSetting  ^=  0x02;
                }
              
                if (isShowTrafficCameraOverlay  && needResetFeature(FeaturesManager.FEATURE_CODE_MAP_LAYER_CAMERA))
                {
                    layerSetting  ^=  0x04;
                }
                
                ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_MAP_LAYER_SETTING, layerSetting);
                MapContainer.getInstance().showMapLayer(layerSetting);
                break;
            }
            case ACTION_CALL:
            {
                PoiDataWrapper poiWrapper = (PoiDataWrapper) get(KEY_O_POI_DATA_WRAPPER);
                if (poiWrapper != null)
                {
                    PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_CALL_TO, poiWrapper.getSelectedIndex());
                }
                String phoneNumber = this.getString(KEY_S_POI_PHONENUMBER);
                TnTelephonyManager.getInstance().startCall(phoneNumber);
                break;
            }
            case ACTION_ADDRESS_SELECTED:
            {
                if(get(KEY_O_SELECTED_ADDRESS) != null)
                {
                    put(KEY_I_TYPE_MAP_FROM, -1);
                }
                break;
            }
            case ACTION_GETTING_MORE_POIS:
            {
                requestMorePois();
                break;
            }
            case ACTION_CANCEL_GETTING_MORE_POIS:
            {
                cancelMorePoisRequest();
                break;
            }
            case ACTION_MAP_ADDRESS:
            {
                moveToAddress();
                break;
            }
            case ACTION_GET_RGC:
            {
                doGetRgc();
                break;
            }
            case ACTION_CHECK_ROUTE_INTEGRITY:
            {
                if (NavSdkRouteWrapper.getInstance().getCurrentRoute().isPartial())
                {
                    NavSdkRouteWrapper.getInstance().addRouteChangeListener(this);
                    postModelEvent(EVENT_MODEL_GET_MORE_ROUTE);
                }
                break;
            }
        }
    }

    private void cancelMorePoisRequest()
    {
        if (poiDataRequester != null)
        {
            poiDataRequester.cancelSearchRequest();
        }
        
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        if (poiDataWrapper.isHasMorePoi())
        {
            poiDataWrapper.setIsDoingRequest(false);
        }

    }

    private boolean isControllerInStack(String className)
    {
        boolean isInStack = false;
        Vector matches = getControllerInStack(className);
        if (matches != null && matches.size() > 0)
            isInStack = true;
        return isInStack;
    }
    
   
    private boolean needResetFeature(String featureId)
    {
        int status = FeaturesManager.getInstance().getStatus(featureId);
        return status == FeaturesManager.FE_UNPURCHASED ||  status == FeaturesManager.FE_DISABLED;
    }
    
    protected void initMapSummary()
    {
//        Route route = RouteWrapper.getInstance().getCurrentRoute();
        Route route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (route != null && route.isPartial() /*|| route.getLength() > MAX_FULL_ROUTE_LENGTH)*/)
        {
////            navigationProxy.requestDecimatedRoute(RouteWrapper.getInstance().getCurrentRouteId());
//            navigationProxy.requestDecimatedRoute(NavSdkRouteWrapper.getInstance().getCurrentRouteId());
//            postModelEvent(EVENT_MODEL_GET_DECIMATE_ROUTE);
            //TODO need set the decimated flag
        }
        postModelEvent(EVENT_MODEL_SHOW_SUMMARY);
    }

    /**
     * 
     */
    protected void requestMorePois()
    {
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        if (poiDataWrapper.isHasMorePoi())
        {
            if (!poiDataWrapper.isDoingRequest())
            {
                int size = poiDataWrapper.getNormalAddressSize();
                poiDataWrapper.setNormalPoiStartIndex(size);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
                poiDataRequester = new PoiDataRequester(userProfileProvider);
                poiDataRequester.doRequestPoi(poiDataWrapper, this);
            }
        }
        else
        {
            postModelEvent(EVENT_MODEL_BACK_TO_MAIN);
        }
    }
    
    public void poiResultUpdate(int resultValue, int resultType, String msg, PoiDataWrapper poiDataWrapper)
    {
        switch (resultValue)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, false);
                if (msg == null || msg.trim().length() == 0)
                {
                    msg = ResourceManager.getInstance()
                            .getCurrentBundle().getString(
                                    IStringCommon.RES_SERVER_ERROR,
                                    IStringCommon.FAMILY_COMMON);
                }
                postErrorMessage(msg);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, false);
                String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name = getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if (name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }

                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]
                { name });
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_BAD_ARGS:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, false);
                String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name = getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if (name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }

                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]
                { name });
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, true);
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                this.postModelEvent(EVENT_MODEL_GET_MORE_POIS);
                break;
            }
            
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IRgcProxy)
        {
            if (this.getState() == STATE_GETTING_RGC)
            {
                Address addr = new Address();
                Stop stop = new Stop();
                NavSdkRgcProxy rgcProxy = (NavSdkRgcProxy) proxy;
                stop.setLat(rgcProxy.getRequestedLat());
                stop.setLon(rgcProxy.getRequestedLon());
                String staticStrDroppedPin = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_DROPPED_PIN, IStringCommon.FAMILY_COMMON);
                stop.setLabel(staticStrDroppedPin == null ? "" : staticStrDroppedPin);
                addr.setStop(stop);
                addDroppedPin(addr);
            }
        }
        else
        {
            super.transactionError(proxy);
        }

    }
    
    private void addDroppedPin(Address addr)
    {
//        MapContainer.getInstance().cleanAll(false);
        addr.setSource(Address.SOURCE_MAP);
        put(KEY_O_SELECTED_ADDRESS, addr);
        put(KEY_I_TYPE_MAP_FROM, TYPE_MAP_FROM_ADDRESS);
        remove(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        postModelEvent(EVENT_MODEL_GOT_RGC);
        put(KEY_B_IS_FROM_RGC, true);
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        String action = proxy.getRequestAction();
        if(proxy instanceof IRgcProxy)
        {
            if (this.getState() == STATE_GETTING_RGC)
            {
                Address addr = ((IRgcProxy) proxy).getAddress();
                addDroppedPin(addr);
                KontagentAssistLogger.endLogMapDisplayTime();
            }
        }
        else if(proxy instanceof INavigationProxy )
        {
            if (IServerProxyConstants.ACT_DECIMATED_ROUTE.equals(action))
            {
                INavigationProxy navProxy = (INavigationProxy)proxy;
                put(KEY_O_MAP_SUMMARY_DECIMATED_ROUTE, navProxy.getDecimateRoute());
                
                postModelEvent(EVENT_MODEL_SHOW_SUMMARY);
            }
        }
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
        RouteUiHelper.resetCurrentRoute();
        NavSdkRouteWrapper.getInstance().removeRouteChangeListener(this);
        if(this.getState() != STATE_GOTO_POI_DETAIL&&this.getState() != STATE_GOTO_SHARE )
        {
            MapContainer.getInstance().pause();
        }
    }
    
    private void moveToAddress()
    {
        Address address = (Address) fetch(KEY_O_SELECTED_ADDRESS);
        if(address == null || !address.isValid())
        {
            return;
        }
        
        if (address != null)
        {
            double lat = address.getStop().getLat() / 100000.0d;
            double lon = address.getStop().getLon() / 100000.0d;
            MapContainer.getInstance().moveMapTo(lat, lon, 0, 0.0f);
        }
    }
    
    private void doGetRgc()
    {
        int latitude = this.getInt(KEY_I_RGC_LAT);
        int longitude = this.getInt(KEY_I_RGC_LON);
        IRgcProxy rgcProxy = ServerProxyFactory.getInstance().createRgcProxy(null, CommManager.getInstance().getComm(), this);
        rgcProxy.requestRgc(latitude, longitude);
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        if (isConnected)
        {
            this.put(KEY_B_IS_ONBOARD, false);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
        else
        {
            this.put(KEY_B_IS_ONBOARD, true);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }

    public void onRouteChange(int routeId)
    {
        if (!NavSdkRouteWrapper.getInstance().getCurrentRoute().isPartial())
        {
            MapContainer.getInstance().cleanAll(false);
            NavSdkRouteWrapper.getInstance().removeRouteChangeListener(this);
            postModelEvent(EVENT_MODEL_GET_MORE_ROUTE_FINISHED);
        }
    }
}

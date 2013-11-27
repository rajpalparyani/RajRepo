/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EntryView.java
 *
 */
package com.telenav.module.map;

import java.util.Vector;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.map.MapConfig;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.HomeScreenManager;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewData.SelectedTrafficIncident;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringMap;
import com.telenav.res.IStringNav;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenSlidableContainer;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.citizen.map.AbstractAnnotation;
import com.telenav.ui.citizen.map.AddressAnnotation;
import com.telenav.ui.citizen.map.DayNightService;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.IMapUIEventListener;
import com.telenav.ui.citizen.map.IVechiclePositionCallback;
import com.telenav.ui.citizen.map.ImageAnnotation;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.citizen.map.MapPoiIndicator;
import com.telenav.ui.citizen.map.MapVehiclePositionService;
import com.telenav.ui.citizen.map.MapViewListComponent;
import com.telenav.ui.citizen.map.PoiAnnotation;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author jyxu (jyxu@telenav.cn)
 * @date Jul 21, 2010
 */
class MapViewTouch extends AbstractCommonMapView implements IMapConstants, PoiDataListener, IVechiclePositionCallback, INotifierListener,IApplicationListener
{
    protected int updateStep = 1;
    protected static final int MAX_UPDATE_STEPS = 4;
    protected static final int NOTIFY_INTERVAL = 500;
    protected long lastNotifyTimeStamp = -1L;
    protected static String DOT_CHAR = ".";
    
    public static final int POI_FORWARD = 0;
    public static final int POI_BACK = 1;

    private Vector poiAnnotationsInMap = new Vector();
    private PoiAnnotation adPoiAnnotationInMap;
    private int selectedPoiIndex = -1;
    private int currentPage = -1;
    private boolean needSyncZoomLevel;

    private Vector addressAnnotationsInMap = new Vector();
    private AddressAnnotation selectedAddressAnnotation;
    private int selectedAddressIndex = -1;
    private boolean isCurrentLocationPressed = false;
    private boolean isFirstTimeDisplayMap = true; //fix bug TN-1729
    protected CitizenSlidableContainer gpsNotAvailableNotification;
    protected CitizenSlidableContainer noGpsAnimationNotificaton;
    protected static final int GPS_NOTIFICATION_TIMEOUT = 3000;
   
    ImageAnnotation origFlagAnnotation;
    ImageAnnotation destFlagAnnotation;
    View titlebarView;
    View searchbarView;
    
    boolean isLastTimeConnected = false;
     
    public MapViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popupContainer = null;
        switch (state)
        {
            case STATE_GETTING_DECIMATE_ROUTE:
            {
                popupContainer = createLoadingProgressBar(state);
                break;
            }
            case STATE_GETTING_MORE_POIS:
            {
                popupContainer = createSearchProgressPopup();
                break;
            }
            case STATE_GOTO_SAVE_FAVORITE:
            {
            	Address anchorAddr = (Address) model.get(KEY_O_SELECTED_ADDRESS); 
            	if(anchorAddr != null)
                {
                	boolean isFavorite = DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(anchorAddr, true);
                	ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
                	String notifaction = "";
                	if(!isFavorite)
                	{                		
                		notifaction = bundle.getString(IStringMap.RES_SAVE_ADDRESS_SUCCESS, IStringMap.FAMILY_MAP);                        
                	}
                	else
                	{
                		notifaction = bundle.getString(IStringMap.RES_DELETE_ADDRESS_SUCCESS, IStringMap.FAMILY_MAP);
                	}
                	popupContainer = createNotifactionBox(state, FrogTextHelper.WHITE_COLOR_START + notifaction + FrogTextHelper.WHITE_COLOR_END, 2);
                }
            	break;
            } 
            case STATE_SHOW_TRAFFIC_DETAIL:
            {
                popupContainer = createTrafficAlertDetail(state);
                break;
            }
            case STATE_GETTING_RGC:
            {
                String strGetAddr = ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_GETTING_ADDR, IStringMap.FAMILY_MAP);
                FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, strGetAddr);
                return progressBox;
            }
            case STATE_GETTING_MORE_ROUTE:
            {
                String strGetRoute = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_GETTING_ROUTE, IStringNav.FAMILY_NAV);
                FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, strGetRoute);
                return progressBox;
            }
            default:
                break;
        }
        return popupContainer;
    }

    protected TnScreen createScreen(int state)
    {

        switch (state)
        {
            case STATE_MAIN:
            case STATE_MAP_SUMMARY:
            {
                return createMapScreen(state);
            }
            case STATE_GENERAL_FEEDBACK:
            {
                int screenBackgroundColor = UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR);
        		return createGeneralFeedbackScreen(state, screenBackgroundColor);
            }
        }

        return null;
    }

    protected boolean isShownTransientView(int state)
    {
        switch (state)
        {

        }
        return super.isShownTransientView(state);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        if(STATE_MAIN == state)
        {
            if(tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT )
            {
                TnKeyEvent keyEvent = tnUiEvent.getKeyEvent();
                if(keyEvent.getCode() == TnKeyEvent.KEYCODE_SEARCH && keyEvent.getAction() == TnKeyEvent.ACTION_UP )
                {
                    cmd = CMD_COMMON_GOTO_ONEBOX;
                }
            }
        }
        return cmd;
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        if(commandId >=  CMD_MAP_POI_CHANGE_TO_FOCUSED_START && commandId <= CMD_MAP_POI_CHANGE_TO_FOCUSED_END)
        {
            int index = commandId - CMD_MAP_POI_CHANGE_TO_FOCUSED_START;
            tranferSelectedPoi(index, PoiMisLogHelper.SHOW_TYPE_BY_CLICK);
            return true;
        }
        else if(commandId >=  CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED_START && commandId <= CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED_END)
        {
            int index = commandId - CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED_START;
            tranferSelectedAddress(index);
            return true;
        }
        switch (commandId)
        {
            case CMD_ZOOM_IN:
            {
                MapContainer.getInstance().zoomInMap();
                return true;
            }
            case CMD_ZOOM_OUT:
            {
                MapContainer.getInstance().zoomOutMap();
                return true;
            }
            case CMD_CURRENT_LOCATION:
            {
                TnLocation loc = LocationProvider.getInstance().getCurrentLocation(
                    LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
                if (loc != null)
                {
                    MapContainer.getInstance().setMapVerticalOffset(0.0f);
                    MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getFasterTransitionTime());
                    MapContainer.getInstance().followVehicle();
                }
                return true;
            }
            case CMD_COMMON_CHANGE_LOCATION:
            {
                boolean needCurrentLocationItem = MapContainer.getInstance().isAnchorAtCurrentLocation();
                model.put(KEY_B_IS_CURRENT_LOCATION, needCurrentLocationItem);
                return true;
            }
            case CMD_MAP_POI_GOTO_NAV:
            {
                Address address = getSelectedPoi();
                model.put(KEY_O_SELECTED_ADDRESS, address);
                model.put(KEY_I_POI_SELECTED_INDEX, getSelectedPoiIndex());
                return true;
            }
            case CMD_MAP_POI_GOTO_POI_DETAIL:
            {
                Address address = getSelectedPoi();
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                if (poiDataWrapper != null)
                {
                    int addressIndex = poiDataWrapper.getIndexOfMixedList(address);
                    if(addressIndex != -1)
                    {
                        poiDataWrapper.setSelectedIndex(addressIndex);
                    }
                }
                return true;
            }
            case CMD_MAP_POI_GOTO_SHARE:
            {
                Address address = getSelectedPoi();
                model.put(KEY_O_SELECTED_ADDRESS, address);
                return true;
            }
            case CMD_MAP_POI_GOTO_CALL:
            {
                Address address = getSelectedPoi();
                model.put(KEY_I_POI_SELECTED_INDEX, getSelectedPoiIndex());
                String phoneNumber = address.getPhoneNumber();
                model.put(KEY_S_POI_PHONENUMBER, phoneNumber);
                return true;
            }
            case CMD_COMMON_LINK_TO_SEARCH:
            {
               Address centerAnchor;
               if (state == STATE_MAIN) 
               {
                   centerAnchor = null;
               }
               else 
               {
                   centerAnchor = MapContainer.getInstance().getMapCenter();
               }
                model.put(KEY_O_SELECTED_ADDRESS, centerAnchor);
                return true;
            }
            case CMD_MAP_POI_NEXT:
            {
                transferPoi(POI_FORWARD);
                return true;
            }
            case CMD_MAP_POI_PREV:
            {
                transferPoi(POI_BACK);
                return true;
            }
            case CMD_MAP_POI_PAGE_NEXT:
            {
                this.currentPage ++;
                setCurrentPage(currentPage);
                transferPage(POI_FORWARD);
                return true;
            }
            case CMD_MAP_POI_PAGE_NEXT_NETWORK:
            {
                return true;
            }
            case CMD_MAP_POI_PAGE_PREV:
            {
                this.currentPage --;
                setCurrentPage(currentPage);
                transferPage(POI_BACK);
                return true;
            }
            case CMD_MAP_POI_CHANGE_TO_UNFOCUSED:
            {
                setSelectedPoiIndex(-1);
                return true;
            }
            case CMD_MAP_ADDRESS_CHANGE_TO_UNFOCUSED:
            {
                setSelectedAddressIndex(-1);
                return true;
            }
            case CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED:
            {
                return true;
            }
            case CMD_MAP_ADDRESS_GOTO_SHARE:
            case CMD_MAP_ADDRESS_GOTO_SAVE_FAVORITE:
            case CMD_MAP_ADDRESS_GOTO_ONE_SEARCH_BOX:
            case CMD_MAP_ADDRESS_GOTO_NAV:
            {
                if(selectedAddressIndex >=0 && selectedAddressIndex < addressAnnotationsInMap.size())
                {
                    AddressAnnotation selectedAnnotation = (AddressAnnotation)this.addressAnnotationsInMap.elementAt(selectedAddressIndex);
                    model.put(KEY_O_SELECTED_ADDRESS, selectedAnnotation.getAddress());
                }
                
                if(commandId == CMD_MAP_ADDRESS_GOTO_ONE_SEARCH_BOX)
                {
                    model.put(KEY_B_IS_SEARCH_NEAR_BY_CLICKED, true);
                }
                return true;
            }
            case CMD_COMMON_OK:
            {
                // Update address anotation save favorite status
                if (selectedAddressAnnotation != null)
                {
                    selectedAddressAnnotation.setFocused(true);
                }
                return true;
            }
            case CMD_CLEAR:
            {
                if (!MapContainer.getInstance().hasCleanAll())
                {
                    MapContainer.getInstance().removePoiAnnotations();
                    AbstractTnComponent component = MapContainer.getInstance().getFeature(ID_MAP_POI_INDICATOR);
                    if (component != null && component instanceof MapPoiIndicator)
                    {
                        MapContainer.getInstance().removeFeature(component);
                    }
                }
                poiAnnotationsInMap.removeAllElements();
                adPoiAnnotationInMap = null;
                selectedPoiIndex = -1;
                currentPage = -1;

                addressAnnotationsInMap.removeAllElements();
                selectedAddressAnnotation = null;
                selectedAddressIndex = -1;
                model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, "");
                return true;
            }
        }
        return super.prepareModelData(state, commandId);
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {

        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_MAP_SUMMARY:
            {
                if(!model.fetchBool(KEY_B_IS_GOTO_BACKGROUND))
                {
                    createSummaryMapContainer((CitizenScreen) screen);
                    updateTrafficTab((CitizenScreen) screen);
                    updatePoisFromNetwork();
                }
                break;
            }
            case STATE_MAIN:
            {
                if(NetworkStatusManager.getInstance().isConnected() != isLastTimeConnected)
                {
                    initMapLayer();
                }
                if(!model.fetchBool(KEY_B_IS_GOTO_BACKGROUND))
                {
                    createMapContainer((CitizenScreen) screen);
                    updatePoisFromNetwork();
                    this.model.remove(KEY_B_IS_FROM_RGC);
                }
                break;
            }
        }
        return false;
    }

    private void updatePoisFromNetwork()
    {
        boolean isUpdateFromGetMorePois = model.fetchBool(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS);
        if(isUpdateFromGetMorePois)
        {
            this.currentPage ++;
            setCurrentPage(currentPage);
            AbstractTnComponent component = MapContainer.getInstance().getFeature(ID_MAP_POI_INDICATOR);
            if (component != null && component instanceof MapPoiIndicator)
            {
                PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                if (poiDataManager != null && poiDataManager.getAddressSize() > 0)
                {
                    ((MapPoiIndicator)component).increaseCurrentPage();
                }
            }
            transferPage(POI_FORWARD);
        }
    }
    
    private TnPopupContainer createLoadingProgressBar(int state)
    {
        String loading = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_LABEL_LOADING,
            IStringCommon.FAMILY_COMMON);
        if (loading == null)
        {
            loading = "";
        }
        return UiFactory.getInstance().createProgressBox(state, loading);
    }
    
    protected AbstractTnComponent createSummaryMapContainer(CitizenScreen screen)
    {
        if (!MapContainer.getInstance().hasCleanAll())
        {
            return MapContainer.getInstance();
        }
        
//TODO Need API to show or hide car icon in navSDK
//        boolean isStaticRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
//        if (isStaticRoute)
//        {
//            MapVehiclePositionService.getInstance().stop();
//            MapContainer.getInstance().postRenderEvent(new Runnable()
//            {
//                public void run()
//                {
//                    IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//                    mapEngine.setBool(MapContainer.getInstance().getViewId(), IMapEngine.PARAMETER_BOOL_SHOW_CAR, false);
//                }
//            });
//        }
//        else
//        {
//            startVehicalePositionService();
//        }
        MapContainer mapContainer = UiFactory.getInstance().getCleanMapContainer(screen, ID_MAP_CONTAINER);

        TnUiArgAdapter mapX = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        });

        TnUiArgAdapter mapWidth = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
        });

        TnUiArgAdapter mapHeight = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int height;
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    // exclude the transparent part of bottom bar
                    height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                            - ((MapUiDecorator) uiDecorator).TAB_OUTER_CONTAINER_HEIGHT.getInt()
                            - uiDecorator.BOTTOM_BAR_HEIGHT.getInt() * 43 / 50;
                }
                else
                {
                    height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                            - ((MapUiDecorator) uiDecorator).TAB_OUTER_CONTAINER_HEIGHT.getInt()
                            - AppConfigHelper.getMinDisplaySize() * 125 / 1000;
                }
                return PrimitiveTypeCache.valueOf(height);
            }
        });

        TnUiArgAdapter mapY = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int y = ((MapUiDecorator) uiDecorator).TAB_OUTER_CONTAINER_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(y);
            }
        });

        mapContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, mapHeight);
        mapContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, mapY);
        mapContainer.setMapRect(mapX, mapY, mapWidth, mapHeight);
        
        int layerSetting = 0x01;
        mapContainer.showMapLayer(layerSetting);
        
        mapContainer.addFeature(getTabContainer());

        mapContainer.addFeature(createMapCompanyLogo());
        mapContainer.addFeature(createMapProvider());
        
        AbstractTnContainer bottomContainer = createBottomContainer(null, createBottomBarArgs(true));
        bottomContainer.setId(ID_BOTTOM_BAR);
        mapContainer.addFeature(bottomContainer);
        MapContainer.getInstance().setMapUIEventListener(this);
        updateMapContainerEvent(MapContainer.getInstance(), IMapUIEventListener.EVENT_CREATED);

        return mapContainer;
    }
    
    private int getSelectedIndexFromPoiAnnotation(Address selectedAddress)
    {
        int selectedIndex = 0;

        Poi selectedPoi = selectedAddress.getPoi();
        
        if (selectedPoi == null || selectedPoi.getBizPoi() == null)
        {
            return selectedIndex;
        }
        
        String selectedPoiId = selectedPoi.getBizPoi().getPoiId();
        
        try
        {
            for (int i = 0; i < poiAnnotationsInMap.size(); i++)
            {
                PoiAnnotation tempAnnotation = (PoiAnnotation) poiAnnotationsInMap.elementAt(i);
                Address addressPoiAnnotation = tempAnnotation.getAddress();
                String tempPoiId = addressPoiAnnotation.getPoi().getBizPoi().getPoiId();
                if(selectedPoiId.equalsIgnoreCase(tempPoiId))
                {
                    selectedIndex = i;
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            return selectedIndex;
        }

        return selectedIndex;
    }

    protected AbstractTnComponent createMapContainer(CitizenScreen screen)
    {
        boolean needRestorePoiAnnotation = model.fetchBool(KEY_B_NEED_RESTORE_POI_ANNOTATION);
        boolean needUpdateFlagsAndRoute = model.fetchBool(KEY_B_NEED_UPDATE_FLAGS);
        PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
        boolean isNotOneBoxSearch =  (poiDataManager != null && (poiDataManager.getCategoryId() == PoiDataRequester.TYPE_NO_CATEGORY_ID || poiDataManager
                .getCategoryId() > 0));
        model.put(KEY_B_IS_SEARCH_FROM_ONEBOX, !isNotOneBoxSearch);
        boolean isSearchFromOnebox = model.getBool(KEY_B_IS_SEARCH_FROM_ONEBOX);
        if (!MapContainer.getInstance().hasCleanAll())
        {
            boolean isDSRFromPoiMap = model.fetchBool(KEY_B_IS_DSR_FROM_POI_MAP);
            
            TnLinearContainer searchBarContainer = (TnLinearContainer) MapContainer.getInstance().getFeature(ID_SEARCH_BAR);
            if (searchBarContainer != null && searchBarContainer.isVisible())
            {
                updateOneBoxSearch(isSearchFromOnebox);
            }

            if (needRestorePoiAnnotation)
            {
                poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                Address selectedAddress = poiDataManager.getSelectedAddress();

                MapContainer.getInstance().removePoiAnnotations();
                MapContainer.getInstance().addFeature(this.poiAnnotationsInMap, true);
//                MapContainer.getInstance().moveMapTo(selectedAddress.getStop().getLat() / 100000.0d, selectedAddress.getStop().getLon() / 100000.0d, 0, 0.0f);
                AbstractAnnotation selectedAnnotation = null;
                if(poiDataManager.getSelectedIndex() > -1 && poiDataManager.getSelectedIndex() < poiAnnotationsInMap.size())
                {
                    selectedAnnotation = (AbstractAnnotation) poiAnnotationsInMap.get(poiDataManager.getSelectedIndex());
                }
                showMapAnnotationRegion(poiAnnotationsInMap,
                    selectedAnnotation == null ? null : selectedAnnotation.getAnnotationId());
                setSelectedPoiIndex(getSelectedIndexFromPoiAnnotation(selectedAddress));
            }
            
            if(isDSRFromPoiMap)
            {
                MapContainer.getInstance().removePoiAnnotations();
                poiAnnotationsInMap.removeAllElements();
                AbstractTnComponent component = MapContainer.getInstance().getFeature(ID_MAP_POI_INDICATOR);
                if (component != null && component instanceof MapPoiIndicator)
                {
                    MapContainer.getInstance().removeFeature(component);
                }
                
                if (searchBarContainer != null && searchBarContainer.isVisible())
                {
                    updateOneBoxSearch(isSearchFromOnebox);
                }
                updateMapContainerEvent(MapContainer.getInstance(), IMapUIEventListener.EVENT_CREATED);
            }
            
            if (model.getBool(KEY_B_IS_FROM_RGC))
            {
                MapContainer.getInstance().removeAllAnnotations();
                AbstractTnComponent component = MapContainer.getInstance().getFeature(ID_MAP_POI_INDICATOR);
                if (component != null && component instanceof MapPoiIndicator)
                {
                    MapContainer.getInstance().removeFeature(component);
                }
                hideMapViewList();
                updateMapContainerEvent(MapContainer.getInstance(), IMapUIEventListener.EVENT_CREATED);
            }
            
            if (needUpdateFlagsAndRoute && isSearchAlongRoute())
            {
                RouteUiHelper.updateCurrentRoute(MapContainer.getInstance());
                updateFlags();
            }
            boolean isOnboard = this.model.getBool(KEY_B_IS_ONBOARD);
            if (isOnboard)
            {
                closeMapLayerPopup();
            }
            if (MapContainer.getInstance().getFeature(ID_MAP_VIEW_LIST_COMPONENT) == null)
            {
                boolean isDayColor = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? false : true;
                int trafficFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW);
                int cameraFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_CAMERA);
                int satelliteFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_SATELLITE);
                if (trafficFeature != FeaturesManager.FE_DISABLED || cameraFeature != FeaturesManager.FE_DISABLED || satelliteFeature != FeaturesManager.FE_DISABLED)
                {
                    MapViewListComponent mapViewListComponent = new MapViewListComponent(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT,
                        trafficFeature != FeaturesManager.FE_DISABLED, cameraFeature != FeaturesManager.FE_DISABLED, satelliteFeature != FeaturesManager.FE_DISABLED);
                    mapViewListComponent.setMapColor(isDayColor);
                    mapViewListComponent.setCommandEventListener(this);
                    mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).MAP_ICON_WIDTH);
                    mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).MAP_ICON_HEIGHT);
                    mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_X);
                    mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y);
                    MapContainer.getInstance().addFeature(mapViewListComponent);
                }
            }
            else
            {
                MapViewListComponent mapViewListComponent = (MapViewListComponent) MapContainer.getInstance().getFeature(ID_MAP_VIEW_LIST_COMPONENT);
                mapViewListComponent.requestPaint();
            }

            //reset zoom level
            if(model.fetchBool(KEY_B_NEED_RESET_ZOOMLEVEL))
            {
                MapContainer.getInstance().setZoomLevel(MapConfig.MAP_DEFAULT_ZOOM_LEVEL);
            }
            return MapContainer.getInstance();
        }
        
        boolean isDayColor = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? false : true;
        
        startVehicalePositionService();
        MapContainer mapContainer;
        if (model.getBool(KEY_B_IS_FROM_RGC))
        {
            mapContainer = UiFactory.getInstance().getCleanMapContainer(screen, ID_MAP_CONTAINER,false);
        }
        else
        {
            mapContainer = UiFactory.getInstance().getCleanMapContainer(screen, ID_MAP_CONTAINER);
        }
//        mapContainer.setMapRect(((MapUiDecorator) uiDecorator).MAP_X, ((MapUiDecorator) uiDecorator).MAP_Y, ((MapUiDecorator) uiDecorator).MAP_WIDTH, ((MapUiDecorator) uiDecorator).MAP_HEIGHT);
        updateMapSize();
        int shadowHeight = 6;
        FrogButton zoomInButton = new FrogButton(ICommonConstants.ID_ZOOM_IN, "");
        zoomInButton.setPadding(0, shadowHeight, 0, 0);
        zoomInButton.setIcon(ImageDecorator.IMG_MAP_ZOOM_IN_ICON_FOCUSED.getImage(), isDayColor ? ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_UNFOCUSED.getImage(): 
            ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);    
        zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).ZOOM_ICON_WIDTH);
        zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).ZOOM_ICON_HEIGHT);
        zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MAP_ZOOM_TOP_BG_FOCUSED);
        zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, isDayColor ? NinePatchImageDecorator.MAP_ZOOM_TOP_BG_DAY_UNFOCUSED : NinePatchImageDecorator.MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED);
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_ZOOM_IN);
        zoomInButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        zoomInButton.setCommandEventListener(this);
        zoomInButton.setKeyEventListener(this);
        String zoomIn=ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_ZOOM_IN, IStringMap.FAMILY_MAP);
        zoomInButton.setContentDescription(zoomIn);
        
        FrogButton zoomOutButton = new FrogButton(ICommonConstants.ID_ZOOM_OUT, "");
        zoomOutButton.setPadding(0, 0, 0, shadowHeight);
        zoomOutButton.setIcon(ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage(), isDayColor ? ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_UNFOCUSED.getImage() :
            ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).ZOOM_ICON_WIDTH);
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).ZOOM_ICON_HEIGHT);
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_FOCUSED);
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, isDayColor ? NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED : NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED);
    
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_ZOOM_OUT);
        zoomOutButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        zoomOutButton.setCommandEventListener(this);
        zoomOutButton.setKeyEventListener(this);
        String zoomOut=ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_ZOOM_OUT, IStringMap.FAMILY_MAP);
        zoomOutButton.setContentDescription(zoomOut);
        
        TnLinearContainer zoomContainer = UiFactory.getInstance().createLinearContainer(ID_MAP_ZOOM_CONTAINER, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        zoomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator) uiDecorator).ZOOM_IN_X);
        zoomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).ZOOM_IN_Y);
        zoomContainer.add(zoomInButton);
        zoomContainer.add(zoomOutButton);
        mapContainer.addFeature(zoomContainer);
        
        FrogButton currentLocation = new FrogButton(ICommonConstants.ID_CURRENT_LOCATION, "");
		currentLocation.setPadding(0, 0, 0, 0);	
        currentLocation.setIcon(ImageDecorator.IMG_CURRENT_LOCATION_FOCUSED.getImage(), isDayColor ? ImageDecorator.IMG_CURRENT_LOCATION_UNFOCUSED.getImage() : 
            ImageDecorator.IMG_CURRENT_LOCATION_NIGHT_UNFOCUSED.getImage(), AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        currentLocation.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator) uiDecorator).CURRENT_LOCATION_X);
        currentLocation.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).CURRENT_LOCATION_Y);
        currentLocation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).MAP_ICON_WIDTH);
        currentLocation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).MAP_ICON_HEIGHT);
        currentLocation.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MAP_BUTTON_FOCUSED);
        currentLocation.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, isDayColor ? NinePatchImageDecorator.MAP_BUTTON_UNFOCUSED : NinePatchImageDecorator.MAP_BUTTON_NIGHT_UNFOCUSED);

        mapContainer.addFeature(currentLocation);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CURRENT_LOCATION);
        currentLocation.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        currentLocation.setCommandEventListener(this);
        String currentLocationStr=ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_CURRENT_LOCATION, IStringMap.FAMILY_MAP);
        currentLocation.setContentDescription(currentLocationStr);

        int trafficFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW);
        int cameraFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_CAMERA);
        int satelliteFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_SATELLITE);
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        this.model.put(KEY_B_IS_ONBOARD, isOnboard);
        if(isOnboard)
        {
            closeMapLayerPopup();
        }
        if (trafficFeature != FeaturesManager.FE_DISABLED || cameraFeature != FeaturesManager.FE_DISABLED || satelliteFeature != FeaturesManager.FE_DISABLED)
        {
            MapViewListComponent mapViewListComponent = new MapViewListComponent(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT,
                    trafficFeature != FeaturesManager.FE_DISABLED, cameraFeature != FeaturesManager.FE_DISABLED,
                    satelliteFeature != FeaturesManager.FE_DISABLED);
            mapViewListComponent.setMapColor(isDayColor);
            mapViewListComponent.setCommandEventListener(this);
            mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).MAP_ICON_WIDTH);
            mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).MAP_ICON_HEIGHT);
            mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_X);
            mapViewListComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y);
            String layerStr=ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_LAYER, IStringMap.FAMILY_MAP);
            mapViewListComponent.setContentDescription(layerStr);
            mapContainer.addFeature(mapViewListComponent);
        }
        
        BottomContainerArgs bottomContainerArgs = null;
        poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
        if (isMapDuringNavigation())
        {
            bottomContainerArgs = createBottomBarArgs(false);
            AbstractTnContainer bottomContainer = createBottomContainer(null, bottomContainerArgs);
            bottomContainer.setId(ID_BOTTOM_BAR);
            mapContainer.addFeature(bottomContainer);
            mapContainer.addFeature(createMapCompanyLogo());
            mapContainer.addFeature(createMapProvider());
        }
        else
        {
//            bottomContainerArgs = getDefaultBottomBarArgs(CMD_COMMON_LINK_TO_MAP);
            mapContainer.addFeature(createMapCompanyLogo(this.uiDecorator.MAP_LOGO_Y));
            mapContainer.addFeature(createMapProvider(this.uiDecorator.MAP_LOGO_Y));
        }
        
        addTitlebar(screen.getTitleContainer());
        AbstractTnContainer oneboxSearchBar = createOneboxSearchBar(isSearchFromOnebox);
        mapContainer.addFeature(oneboxSearchBar);
        
        int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
        if (mapFromType == TYPE_MAP_FROM_BILLBOARD)
        {
            addFlags();
        }
        
        MapContainer.getInstance().setMapUIEventListener(this);
        updateMapContainerEvent(MapContainer.getInstance(), IMapUIEventListener.EVENT_CREATED);
        return mapContainer;
    }

    private AbstractTnContainer createOneboxSearchBar(boolean isSearchFromOnebox)
    {
        AbstractTnContainer searchBar = UiFactory.getInstance().createLinearContainer(ID_SEARCH_BAR, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        searchBar.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.COMMON_ONEBOX_HEIGHT);
        searchBar.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        searchBar.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        searchBar.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        searchBar.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator) uiDecorator).SEARCH_BAR_X);
        searchBar.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).SEARCH_BAR_Y);
        searchbarView = AndroidCitizenUiHelper.addContentView(searchBar, R.layout.common_onebox);            
        int horizentalPadding = (int) searchbarView.getResources().getDimension(R.dimen.common_side_margin);
        int verticalPadding = (int) searchbarView.getResources().getDimension(R.dimen.placeList0BottomButtonMarginTopBottom);
        searchBar.setPadding(horizentalPadding, verticalPadding, horizentalPadding, verticalPadding);
        updateOneBoxSearch(isSearchFromOnebox);
        return searchBar;
    }
    
    private void addTitlebar(AbstractTnContainer titleBarContainer)
    {
        if (titleBarContainer != null)
        {
            AbstractTnComponent titleContainer = MapContainer.getInstance().getFeature(ID_TITLE_CONTAINER);
            if (titleContainer == null)
            {
                titleBarContainer.setId(ID_TITLE_CONTAINER);
                titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.COMMON_TITLE_BUTTON_HEIGHT);
                titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
                titlebarView = AndroidCitizenUiHelper.addContentView(titleBarContainer, R.layout.common_title);
                MapContainer.getInstance().addFeature(titleBarContainer);
            }
        }
    }
    
    private void updateOneBoxSearch(boolean isSearchFromOnebox)
    {
        if (searchbarView != null)
        {
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            TextView searchBoxHint = (TextView) searchbarView.findViewById(R.id.commonOneboxTextView);
            ImageView DSRimage = (ImageView) searchbarView.findViewById(R.id.commonOneboxDsrButton);
            View oneBoxDSR = (View) searchbarView.findViewById(R.id.commonOneboxDsrButton);
            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
            this.model.put(KEY_B_IS_ONBOARD, isOnboard);
            int DSRImageId = isOnboard ? R.drawable.inputbox_mic_icon_disabled : R.drawable.inputbox_mic_icon;
            DSRimage.setImageResource(DSRImageId);
            String searchText = model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
            
            if(isSearchFromOnebox)
            {
                if (isOnboard)
                {
                    searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
                }
                else
                {
                    if (searchText != null && searchText.length() > 0)
                    {
                        searchBoxHint.setText(searchText);
                    }
                    else
                    {
                        searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
                    }
                    
                }
            }
            else
            {
                if (isOnboard)
                {
                    searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
                }
                else
                {
                    searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
                }
            }
            
            if (model.fetchBool(KEY_B_IS_CLEAR_ONEBOX))
            {
                searchBoxHint.setText("");
            }

            AndroidCitizenUiHelper.setOnClickCommand(this, oneBoxDSR, isOnboard ? CMD_NONE : CMD_COMMON_DSR);
            AndroidCitizenUiHelper.setOnClickCommand(this, searchBoxHint, CMD_COMMON_GOTO_ONEBOX);
        }
    }
    
    private TnScreen createMapScreen(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                CitizenScreen mainScreen = UiFactory.getInstance().createScreen(state);
                isFirstTimeDisplayMap = true; // fix bug TN-1729, let the notify() to trigger the zoom level calculation
                createMapContainer(mainScreen);
                // based on error size
                return mainScreen;
            }
            case STATE_MAP_SUMMARY:
            {
                CitizenScreen mainScreen = UiFactory.getInstance().createScreen(state);
                createSummaryMapContainer(mainScreen);
                return mainScreen;
            }
        }
        return null;
    }
    
    private void updateTrafficTab(CitizenScreen screen)
    {
        
        AbstractTnComponent container = MapContainer.getInstance().getFeature(ID_TAB_CONTAINER);
        AbstractTnComponent comp = null;
        if (container instanceof AbstractTnContainer)
        {
            comp = ((AbstractTnContainer) container).getComponentById(ID_TAB_TRAFFIC_BUTTON);
        }
        if (comp instanceof FrogButton)
        {
            FrogButton trafficButton = (FrogButton) comp;
            int tabTextUnfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_UNFOCUSED);
            int trafficDisabledColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_DISABLED);
            int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
            boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                    || trafficEnableValue == FeaturesManager.FE_PURCHASED;
            PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
            Preference routeTypePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
            boolean isOnboard = model.getBool(KEY_B_IS_ONBOARD)|| NavRunningStatusProvider.getInstance().isOnBoardRoute();
            if (!isOnboard && isTrafficEnabled
                    && (routeTypePref == null || routeTypePref.getIntValue() != Route.ROUTE_PEDESTRIAN))
            {
                trafficButton.setEnabled(true);
                trafficButton.setForegroundColor(tabTextUnfocusedColor, tabTextUnfocusedColor);
            }
            else
            {
                trafficButton.setEnabled(false);
                trafficButton.setForegroundColor(trafficDisabledColor, trafficDisabledColor);
            }
        }
    }
    
    protected AbstractTnContainer getTabContainer()
    {
        TnLinearContainer outerContainer = UiFactory.getInstance().createLinearContainer(ID_TAB_CONTAINER, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        int tabTextUnfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_UNFOCUSED);
        int tabTextFocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_FOCUSED);
        int trafficDisabledColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_DISABLED); 
        outerContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TITLE_CONTAINER_COLOR));
        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator)uiDecorator).TAB_OUTER_CONTAINER_HEIGHT);
        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator)uiDecorator).SCREEN_WIDTH);
        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator)uiDecorator).TAB_OUTER_CONTAINER_X);
        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator)uiDecorator).TAB_OUTER_CONTAINER_Y);
        
        TnLinearContainer innerContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        
        String turns = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_TURNS, IStringNav.FAMILY_NAV);
        FrogButton turnButton = UiFactory.getInstance().createButton(ID_TAB_TURN_BUTTON, turns);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_LEFT_BUTTON_UNFOCUSED);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_LEFT_BUTTON_UNFOCUSED);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        TnMenu turnMenu = new TnMenu();
        turnMenu.add("", CMD_ROUTE_SUMMARY);
        turnButton.setMenu(turnMenu, AbstractTnComponent.TYPE_CLICK);
        turnButton.setCommandEventListener(this);
        turnButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));
        turnButton.setForegroundColor(tabTextFocusedColor, tabTextUnfocusedColor);
        turnButton.setFocusable(true);
        innerContainer.add(turnButton);
        
        String traffic = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_TRAFFIC, IStringNav.FAMILY_NAV);
        FrogButton trafficButton = UiFactory.getInstance().createButton(ID_TAB_TRAFFIC_BUTTON, traffic);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_MIDDLE_BUTTON_UNFOCUSED);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_MIDDLE_BUTTON_UNFOCUSED);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        TnMenu trafficMenu = new TnMenu();
        trafficMenu.add("", CMD_TRAFFIC_SUMMARY);
        trafficButton.setMenu(trafficMenu, AbstractTnComponent.TYPE_CLICK);
        trafficButton.setCommandEventListener(this);
        trafficButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));        
       
        int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
        boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED || trafficEnableValue == FeaturesManager.FE_PURCHASED;
        Preference routeTypePref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        boolean isOnboard = NavRunningStatusProvider.getInstance().isOnBoardRoute() 
                || !NetworkStatusManager.getInstance().isConnected();
        this.model.put(KEY_B_IS_ONBOARD, isOnboard);
        if (!isOnboard && isTrafficEnabled && (routeTypePref == null || routeTypePref.getIntValue() != Route.ROUTE_PEDESTRIAN))
        {
            trafficButton.setEnabled(true);
            trafficButton.setForegroundColor(tabTextFocusedColor, tabTextUnfocusedColor);
        }
        else
        {
            trafficButton.setEnabled(false);
            trafficButton.setForegroundColor(trafficDisabledColor, trafficDisabledColor);
        }
        trafficButton.setFocusable(true);
        innerContainer.add(trafficButton);
        
        String map = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_MAP, IStringNav.FAMILY_NAV);
        FrogButton mapButton = UiFactory.getInstance().createButton(0, map);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_RIGHT_BUTTON_FOCUSED);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_RIGHT_BUTTON_FOCUSED);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        mapButton.setForegroundColor(tabTextFocusedColor, tabTextUnfocusedColor);
        mapButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));
        mapButton.setFocusable(true);
        
        innerContainer.add(mapButton);
        
        outerContainer.add(innerContainer);
        return outerContainer;
    }
    
   public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUIEvent(tnUiEvent);
        return super.handleUiEvent(tnUiEvent);
    }
    
   private void logKtUIEvent(TnUiEvent tnUiEvent)
   {
       if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
       {
           int command = tnUiEvent.getCommandEvent().getCommand();
           switch (command)
           {
               case CMD_MAP_ADDRESS_GOTO_NAV:
               case CMD_MAP_POI_GOTO_NAV:
               {
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_MAP,
                       KontagentLogger.POIMAP_DRIVE_CLICKED);
                   break;
               }
               case CMD_MAP_POI_GOTO_CALL :
               {
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_MAP,
                       KontagentLogger.POIMAP_CALL_CLICKED);
                   break;
               }
               case CMD_MAP_ADDRESS_GOTO_SHARE:
               case CMD_MAP_POI_GOTO_SHARE:
               {
                  KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_MAP,
                       KontagentLogger.POIMAP_SHARE_CLICKED);
                   break;
               }
               case CMD_NAVIGATION:
               case CMD_DIRECTIONS:
               {
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                       KontagentLogger.NAVIGATION_NAVIGATION_CLICKED);
                   break;
               }
               case CMD_TRAFFIC_SUMMARY:
               {
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                       KontagentLogger.NAVIGATION_TRAFFIC_CLICKED);
                   break;
               }
               case CMD_ROUTE_SUMMARY:
               {
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                       KontagentLogger.NAVIGATION_ROUTE_CLICKED);
                   break;
               }
               case CMD_COMMON_LINK_TO_SEARCH:
               {
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                       KontagentLogger.NAVIGATION_NEARBY_CLICKED);
                   break;
               }
               case CMD_POI_LIST:
               {
                   //Level 2: When the display is switched from map view to list view
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                       KontagentLogger.SEARCHRESULTS_DISPLAY_CHANGED, 2);
                   break;
               }
               case CMD_MAP_ADDRESS_GOTO_SAVE_FAVORITE:
               {   
                   KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_MAP,
                       KontagentLogger.POIMAP_SAVE_CLICKED);
                   break;
               }
               case CMD_COMMON_EXIT:
               {
                   int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
                   if (mapFromType == TYPE_MAP_FROM_ENTRY || mapFromType == TYPE_MAP_FROM_AC || mapFromType == -1)
                   {
                       KontagentAssistLogger.endLogMapDisplayTime();
                   }
                   break;
               }
           }
       }
   }
   
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
        {
            AbstractTnComponent mapContainer  =  MapContainer.getInstance();
            int id = tnUiEvent.getCommandEvent().getCommand();
            switch (id)
            {
                case CMD_POI_LIST:
                {
                    Address centerAnchor = ((MapContainer)mapContainer).getMapCenter();
                    model.put(KEY_O_ADDRESS_ORI, centerAnchor);
                    break;
                }
                case CMD_COMMON_GOTO_ONEBOX:
                {
                    if (tnUiEvent.getComponent() != null && tnUiEvent.getComponent() instanceof FrogTextField)
                    {
                        FrogTextField textField = (FrogTextField) tnUiEvent.getComponent();
                        String text = textField.getText();
                        this.model.put(KEY_S_COMMON_SEARCH_TEXT, text);
                        textField.setText("");
                    }
                    boolean isIgnoreTextChange = model.fetchBool(KEY_B_IGNORE_TEXT_CHANGE);
                    if (isIgnoreTextChange)
                        return true;

                    break;
                }
                case CMD_COMMON_LINK_TO_SEARCH:
                {
                    this.model.remove(KEY_S_COMMON_SEARCH_TEXT);
                    break;
                }
                case CMD_CURRENT_LOCATION:
                {
                    MapVehiclePositionService.getInstance().restart(true);
                    isCurrentLocationPressed = true;
                    break;
                }
                case CMD_GPS_NOT_AVAILABLE_NOTIFICATION_CLOSE:
                {
                    gpsNotAvailableNotification = null;
                    return true;
                }
            }
            
            if(id >= IMapConstants.CMD_SHOW_MAP_LAYER_START && id <= IMapConstants.CMD_SHOW_MAP_LAYER_END)
            {
                tnUiEvent.setCommandEvent(new TnCommandEvent(IMapConstants.CMD_SHOW_MAP_LAYER));
                int layerSetting = 0;
                if(id >= IMapConstants.CMD_SHOW_MAP_TRAFFIC_LAYER_START && id <= IMapConstants.CMD_SHOW_MAP_TRAFFIC_LAYER_END)
                {
                    layerSetting = id - IMapConstants.CMD_SHOW_MAP_TRAFFIC_LAYER_START;
                    model.put(KEY_I_MAP_LAYER, MAP_LAYER_TRAFFIC);
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MAP,
                        KontagentLogger.MAP_TRAFFIC_CLICKED );
                }
                else if(id >= IMapConstants.CMD_SHOW_MAP_CAMERA_LAYER_START && id <= IMapConstants.CMD_SHOW_MAP_CAMERA_LAYER_END)
                {
                    layerSetting = id - IMapConstants.CMD_SHOW_MAP_CAMERA_LAYER_START;
                    model.put(KEY_I_MAP_LAYER, MAP_LAYER_CAMERA);
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MAP,
                        KontagentLogger.MAP_CAMERA_CLICKED );
                }
                else
                {
                    layerSetting = id - IMapConstants.CMD_SHOW_MAP_SATELLITE_LAYER_START;
                    model.put(KEY_I_MAP_LAYER, MAP_LAYER_SATELLITE);
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MAP,
                        KontagentLogger.MAP_SATELLITE_CLICKED );
                }
                model.put(KEY_I_MAP_LAYER_SETTING, layerSetting);
            }
        }
        return super.preProcessUIEvent(tnUiEvent);

    }

    protected boolean isFollowMeMap()
    {
        int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
        if(mapFromType == HomeScreenManager.getHomeMapFromType())
        {
            return true;
        }
        return false;
    }
    
    private boolean isSearchAlongRoute()
    {
        PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
        if (poiDataManager != null && poiDataManager.getAddressSize() > 0)
        {
            int searchType = poiDataManager.getSearchType();
            
            int acType = model.getInt(KEY_I_AC_TYPE);
            if(searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE || acType == TYPE_AC_FROM_TURN_MAP)
            {
               return true;
            }
        }
        return false;
    }
    
    public void updateMapContainerEvent(final MapContainer mapContainer, int event)
    {
        if (event == IMapUIEventListener.EVENT_CREATED)
        {
            MapContainer.getInstance().enableGPSCoarse(true);
            if (!model.getBool(KEY_B_IS_FROM_RGC))
            {
                MapContainer.getInstance().setZoomLevel(MapConfig.MAP_DEFAULT_ZOOM_LEVEL);
            }
            int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
            int detailFromType = model.getInt(KEY_I_TYPE_DETAIL_FROM);
            if (isFollowMeMap())
            {
                mapContainer.clearRoute(); // there is no route in main map screen at all.
                mapContainer.initMap();
                startFollowVehicleMode(mapContainer);
            }
            else if (mapFromType == TYPE_MAP_FROM_BROWSER && detailFromType > 0)
            {
                PoiDataWrapper poiDataManager = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                if (NavRunningStatusProvider.getInstance().isNavRunning())
                {
                    RouteUiHelper.updateCurrentRoute(mapContainer);
                    updateFlags();
                }
                else
                {
                    mapContainer.clearRoute();
                }
                Address selectedAddress = (Address) (detailFromType > 0 ? model.get(KEY_O_SELECTED_ADDRESS) : poiDataManager
                        .getSelectedAddress());
                mapContainer.clearRoute(); // there is no route in main map screen at all.
                addAndGotoSingleAddress(selectedAddress);
            }
            else if (mapFromType == TYPE_MAP_FROM_POI || mapFromType == TYPE_MAP_FROM_SPECIFIC_POI ||mapFromType == TYPE_MAP_FROM_ONEBOX_POI || mapFromType == TYPE_MAP_FROM_BROWSER)
            {
                boolean isAdPoiSelected = false;
                boolean hasAdPoiInMap = false;
                PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                if (poiDataManager != null && poiDataManager.getAddressSize() > 0)
                {
                    int acType = model.getInt(KEY_I_AC_TYPE);
                    // check if it is from search poi along route
                    if (isSearchAlongRoute())
                    {
                        RouteUiHelper.updateCurrentRoute(mapContainer);
                        updateFlags();
                    }
                    else
                    {
                        mapContainer.clearRoute();
                    }
                    poiAnnotationsInMap.removeAllElements();
                    byte showMode = PoiMisLogHelper.getInstance().isMapResultsMode() ? PoiMisLogHelper.SHOW_TYPE_BY_DEFAULT : PoiMisLogHelper.SHOW_TYPE_BY_INTENTION;
                    Address selectedAddress = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                    if ((selectedAddress != null && mapFromType == TYPE_MAP_FROM_BROWSER) || poiDataManager.getOriginSelectedIndex() >= 0 || poiDataManager.getAddressSize() == 1)
                    {
                        if (selectedAddress == null)
                        {
                            selectedAddress = poiDataManager.getSelectedAddress();
                        }
                        PoiMisLogHelper.getInstance().showSelectedPoiMap(poiDataManager.getIndexOfNormalList(selectedAddress), true, showMode);
                        PoiAnnotation poiAnnotation = new PoiAnnotation(selectedAddress, 0, true, false, 0, false);
                        poiAnnotation.setCommandEventListener(this);
                        this.poiAnnotationsInMap.addElement(poiAnnotation);
                        MapContainer.getInstance().addFeature(this.poiAnnotationsInMap, true);
                        mapContainer.moveMapTo(selectedAddress.getStop().getLat() / 100000.0d, selectedAddress.getStop().getLon() / 100000.0d, 0, 0.0f);
                        this.selectedPoiIndex = 0;
                    }
                    else
                    {
                        selectedAddress = poiDataManager.getSelectedAddress();
                        
                        //this is to avoid null selectedAddress from poi module
                        if(selectedAddress == null)
                        {
                            selectedAddress = poiDataManager.getAddress(0);
                        }
                        
                        if (selectedAddress != null && selectedAddress.getPoi() != null && selectedAddress.getPoi().getType() == Poi.TYPE_SPONSOR_POI)
                        {
                            isAdPoiSelected = true;
                            this.currentPage = poiDataManager.getIndexOfSponsoredList(selectedAddress);
                        }
                        else
                        {
                            isAdPoiSelected = false;
                            this.currentPage = poiDataManager.getIndexOfNormalList(selectedAddress)/PoiDataRequester.DEFAULT_PAGE_SIZE;
                        }
                        
                        //if ad poi select and can map, it means adPoi has valid stop
                        if(isAdPoiSelected)
                        {
                            this.adPoiAnnotationInMap  = new PoiAnnotation(selectedAddress, 0, true, true,  poiDataManager.getTotalNormalCount());
                            adPoiAnnotationInMap.setCommandEventListener(this);
                            PoiMisLogHelper.getInstance().showSelectedPoiMap(currentPage * (PoiDataRequester.DEFAULT_PAGE_SIZE + 1), false,
                                showMode);
                            poiAnnotationsInMap.addElement(adPoiAnnotationInMap);
                            hasAdPoiInMap = true;
                        }
                        else
                        {
                            Address adPoi = poiDataManager.getSponsoredAddress(currentPage); //adPoi per page 
                            if(adPoi != null && adPoi.isValid() && adPoi.getPoi()!= null && adPoi.getPoi().getType() != Poi.TYPE_DUMMY_POI)
                            {
                                this.adPoiAnnotationInMap  = new PoiAnnotation(adPoi, 0, false, true, poiDataManager.getTotalNormalCount());
                                adPoiAnnotationInMap.setCommandEventListener(this);
                                poiAnnotationsInMap.addElement(adPoiAnnotationInMap);
                                hasAdPoiInMap = true;
                            }
                        }
                        
                        int selectNormalIndexInMap = (poiDataManager.getIndexOfNormalList(selectedAddress)) % PoiDataRequester.DEFAULT_PAGE_SIZE;
                        
                        int endIndex = (currentPage + 1) * PoiDataRequester.DEFAULT_PAGE_SIZE;
                        if(poiDataManager.getNormalAddressSize() < endIndex)
                        {
                            endIndex = poiDataManager.getNormalAddressSize();
                        }
                        
                        PoiAnnotation focusedPoiannotation = null;
                        for (int i = currentPage * PoiDataRequester.DEFAULT_PAGE_SIZE; i < endIndex; i++)
                        {
                            Address address = (Address)poiDataManager.getNormalAddress(i);
                            if (address != null)
                            {
                                PoiAnnotation poiAnnotation;
                                if(!isAdPoiSelected && selectNormalIndexInMap == i %  PoiDataRequester.DEFAULT_PAGE_SIZE)  //if which is selected poi
                                {
                                    selectedAddress = address;
                                    PoiMisLogHelper.getInstance().showSelectedPoiMap(i, true, showMode);
                                    poiAnnotation = new PoiAnnotation(address, i, true, hasAdPoiInMap, poiDataManager.getTotalNormalCount());
                                    focusedPoiannotation = poiAnnotation;
                                }
                                else
                                {
                                    poiAnnotation = new PoiAnnotation(address, i, false, hasAdPoiInMap, poiDataManager.getTotalNormalCount());
                                }
                                poiAnnotation.setCommandEventListener(this);
                                this.poiAnnotationsInMap.addElement(poiAnnotation);
                            }
                        }
                        
                        PoiMisLogHelper.getInstance().showPoiPage(currentPage, endIndex - currentPage * PoiDataRequester.DEFAULT_PAGE_SIZE,
                            hasAdPoiInMap);
                        
                        if(isAdPoiSelected)
                        {
                            this.selectedPoiIndex = 0;
                        }
                        else
                        {
                            if(hasAdPoiInMap)
                                this.selectedPoiIndex = ((poiDataManager.getIndexOfNormalList(selectedAddress)) % PoiDataRequester.DEFAULT_PAGE_SIZE) + 1;
                            else
                                this.selectedPoiIndex = (poiDataManager.getIndexOfNormalList(selectedAddress)) % PoiDataRequester.DEFAULT_PAGE_SIZE;
                        }
                        
                        MapContainer.getInstance().addFeature(this.poiAnnotationsInMap, true);
                        
                        //Only select non-poi should show 1- 10 poi, this is to avoid ad-poi is far away from other pois
                        if(!isAdPoiSelected && mapFromType != TYPE_MAP_FROM_BROWSER && mapFromType != TYPE_MAP_FROM_SPECIFIC_POI)
                        {
                            showMapAnnotationRegion(poiAnnotationsInMap, focusedPoiannotation == null ? null : focusedPoiannotation.getAnnotationId());
                        }
                        
                        addPoiPagination(poiDataManager.getNormalAddressSize(), poiDataManager.getTotalNormalCount());
                        //TN-2155    [Moto Atrix] App stuck and then exits(crashes) when user is using DSR in dynamic and static routes
                        if(this.isMapDuringNavigation() && acType!= TYPE_AC_FROM_TURN_MAP)
                        {
                            model.put(ICommonConstants.KEY_I_AC_TYPE, ICommonConstants.TYPE_AC_FROM_NAV);
                        }
                        
                    }
                }
            }
            else if (mapFromType == TYPE_MAP_FROM_BILLBOARD)
            {
                Address destAddress = (Address) model.get(KEY_O_ADDRESS_DEST);
                if(destAddress != null)
                {
                    addDestinationFlag(destAddress.getStop());
                }
                Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                this.addAndGotoBillboard(address);
            }
            else if(mapFromType == TYPE_FROM_MAITAI)
            {
                mapContainer.clearRoute(); // there is no route in main map screen at all.
                Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                addAndGotoSingleAddress(address);
            }
            else if(mapFromType == TYPE_MAP_FROM_MAITAI_POI)
            {
                mapContainer.clearRoute(); // there is no route in main map screen at all.
                Object address = model.get(KEY_O_OPERATED_ADDRESS_LIST);
                addressAnnotationsInMap.removeAllElements();
                if (address instanceof Address[])
                {
                    Address[] addresses = (Address[])address;
                    AddressAnnotation addressAnnotation = null;
                    for(int i = 0; i< addresses.length; i++)
                    {
                        if(i == 0)
                        {
                            addressAnnotation = new AddressAnnotation(addresses[i], i, true);
                            selectedAddressAnnotation = addressAnnotation;
                            this.selectedAddressIndex = 0;
                        }
                        else
                        {
                            addressAnnotation = new AddressAnnotation(addresses[i], i, false);
                        }
                        addressAnnotation.setCommandEventListener(this);
                        addressAnnotationsInMap.addElement(addressAnnotation);
                    }
                                    
                    mapContainer.addFeature(addressAnnotationsInMap, true);
                    showMapAnnotationRegion(addressAnnotationsInMap, selectedAddressAnnotation.getAnnotationId());
                }
            }
            else if (mapFromType == TYPE_MAP_FROM_SUMMARY)
            {
                initMapSummary(mapContainer);
            }
            else if (mapFromType == TYPE_MAP_FROM_ADDRESS)
            {
                if (NavRunningStatusProvider.getInstance().isNavRunning())
                {
                    RouteUiHelper.updateCurrentRoute(mapContainer);
                    updateFlags();
                }
                else
                {
                    mapContainer.clearRoute();
                }
                Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                addAndGotoSingleAddress(address);
            }
            else
            {
                mapContainer.clearRoute(); // there is no route in main map screen at all.
                Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                if (address != null && address.isValid())
                {
                    if(address.getStop().getType() != Stop.STOP_CURRENT_LOCATION)
                    {
                        addAndGotoSingleAddress(address);
                    }
                    else
                    {
                        double lat = address.getStop().getLat() / 100000.0d;
                        double lon = address.getStop().getLon() / 100000.0d;
                        MapContainer.getInstance().moveMapTo(lat, lon, 0, 0.0f);
                    }
                }
                else
                {
                    mapContainer.initMap();
                }
            }
            updateMapUI();           
        }
    }

    private void addPoiPagination(int currentPoiCount, int totalCount)
    {
        AbstractTnComponent component = MapContainer.getInstance().getFeature(ID_MAP_POI_INDICATOR);
        if (component == null )
        {
            MapPoiIndicator indicator = new MapPoiIndicator(ID_MAP_POI_INDICATOR, false, currentPage, currentPoiCount, totalCount);
            indicator.setCommandEventListener(this);
            indicator.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator) uiDecorator).MAP_POI_INDICATOR_X);
            indicator.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_POI_INDICATOR_Y);
            indicator.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).MAP_POI_INDICATOR_WIDTH);
            indicator.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).MAP_POI_INDICATOR_HEIGHT);
            MapContainer.getInstance().addFeature(indicator);
        }                
    }
    
    private void updateYPosition()
    {
        AbstractTnComponent component = MapContainer.getInstance().getFeature(ID_MAP_POI_INDICATOR);
        //handle the case of pagination view exists
        if (component != null && component instanceof MapPoiIndicator)
        {
            if (titlebarView != null)
            {
                TextView tosTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);
                String searchText = model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                boolean isSearchFromOnebox = model.getBool(KEY_B_IS_SEARCH_FROM_ONEBOX);
                if (searchText == null || searchText.trim().length() == 0 || isSearchFromOnebox)
                {
                    searchText = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringPoi.LABEL_SEARCH, IStringPoi.FAMILY_POI);
                }
                tosTextView.setText(searchText);
                ImageView editView = (ImageView) titlebarView.findViewById(R.id.commonTitle0IconButton);
                Drawable bgImage = new AssetsImageDrawable(ImageDecorator.IMG_SEARCHBOX_POILIST_FOCUS);
                editView.setImageDrawable(bgImage);
                editView.setVisibility(View.VISIBLE);
                AndroidCitizenUiHelper.setOnClickCommand(this, editView, CMD_POI_LIST);
            }

            AbstractTnComponent mapViewList = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
            if (mapViewList instanceof MapViewListComponent)
            {
                mapViewList.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y_WITH_POI_PAGINATION);
                ((MapViewListComponent) mapViewList).requestPaint();
            }

            AbstractTnComponent currentLocation = MapContainer.getInstance().getFeature(ICommonConstants.ID_CURRENT_LOCATION);
            if (currentLocation instanceof FrogButton)
            {
                currentLocation.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y_WITH_POI_PAGINATION);
                currentLocation.requestPaint();
            }

            AbstractTnComponent zoomContainer = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_ZOOM_CONTAINER);
            if (zoomContainer instanceof TnLinearContainer)
            {
                zoomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,((MapUiDecorator) uiDecorator).ZOOM_IN_Y_WITH_POI_PAGINATION);
                zoomContainer.requestPaint();
            }

            AbstractTnComponent bottomContainer = MapContainer.getInstance().getFeature(ID_BOTTOM_BAR);
            if (bottomContainer instanceof TnLinearContainer)
            {
                MapContainer.getInstance().removeFeature(bottomContainer);
            }          
            relocateMapLogoProvider(((MapUiDecorator) uiDecorator).MAP_LOGO_Y_WITH_POI_PAGINATION);

        }
        else
        {
            if (titlebarView != null)
            {
                TextView tosTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);

                String searchText = null;
             
                Address anchorAddr = (Address)model.get(KEY_O_SELECTED_ADDRESS) ;
                if(anchorAddr ==null)
                {
                    PoiDataWrapper poiDataManager = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                    if (poiDataManager != null)
                        anchorAddr = (Address) poiDataManager.getSelectedAddress();
                }
                if (anchorAddr != null && anchorAddr.getLabel() != null)
                {
                    searchText = anchorAddr.getLabel();
                }

                boolean isSearchFromOnebox = model.getBool(KEY_B_IS_SEARCH_FROM_ONEBOX);
                
                if (searchText == null || searchText.trim().length() == 0 || isSearchFromOnebox)
                {
                    ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
                    searchText = bundle.getString(IStringMap.RES_DEFAULT_MAP_TITLE, IStringMap.FAMILY_MAP);
                }

                tosTextView.setText(searchText);

                ImageView editView = (ImageView) titlebarView.findViewById(R.id.commonTitle0IconButton);
                editView.setVisibility(View.INVISIBLE);
            }
            
            AbstractTnComponent mapViewList = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
            if (mapViewList instanceof MapViewListComponent)
            {
                mapViewList.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y);
                ((MapViewListComponent) mapViewList).requestPaint();
            }
            
            if (this.addressAnnotationsInMap.size() > 0 || poiAnnotationsInMap.size()>0)
            {
                AbstractTnComponent bottomContainer = MapContainer.getInstance().getFeature(ID_BOTTOM_BAR);
               
                if (bottomContainer instanceof TnLinearContainer)
                {
                    AbstractTnComponent currentLocation = MapContainer.getInstance().getFeature(ICommonConstants.ID_CURRENT_LOCATION);
                    if (currentLocation instanceof FrogButton)
                    {
                        currentLocation.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y_WITH_BOTTOM_BAR);
                        currentLocation.requestPaint();
                    }
                    
                    mapViewList = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
                    if (mapViewList instanceof MapViewListComponent)
                    {
                        mapViewList.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y_WITH_BOTTOM_BAR);
                        ((MapViewListComponent) mapViewList).requestPaint();
                    }
                    
                    AbstractTnComponent zoomContainer = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_ZOOM_CONTAINER);
                    if (zoomContainer instanceof TnLinearContainer)
                    {
                        zoomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,((MapUiDecorator) uiDecorator).ZOOM_IN_Y_WITH_WITH_BOTTOM_BAR);
                        zoomContainer.requestPaint();
                    }
                }
            }
        }
        if (this.model.getBool(KEY_B_IS_FROM_RGC))
        {
            AbstractTnComponent mapViewList = MapContainer.getInstance()
                    .getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
            if (mapViewList instanceof MapViewListComponent)
            {
                mapViewList.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y);
                ((MapViewListComponent) mapViewList).requestPaint();
            }

            AbstractTnComponent currentLocation = MapContainer.getInstance().getFeature(ICommonConstants.ID_CURRENT_LOCATION);
            if (currentLocation instanceof FrogButton)
            {
                currentLocation.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).MAP_VIEW_LIST_Y);
                currentLocation.requestPaint();
            }

            AbstractTnComponent zoomContainer = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_ZOOM_CONTAINER);
            if (zoomContainer instanceof TnLinearContainer)
            {
                zoomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator) uiDecorator).ZOOM_IN_Y);
                zoomContainer.requestPaint();
            }

            AbstractTnComponent bottomContainer = MapContainer.getInstance().getFeature(ID_BOTTOM_BAR);
            if (bottomContainer instanceof TnLinearContainer)
            {
                MapContainer.getInstance().removeFeature(bottomContainer);
            }
            relocateMapLogoProvider(((MapUiDecorator) uiDecorator).MAP_LOGO_Y);
        }
    }
    
    private boolean isSearchBarNeeded()
    {
        boolean isPortrait = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
        return isPortrait || (this.addressAnnotationsInMap.size() < 1 && poiAnnotationsInMap.size() < 1);
    }

    private void updateSeachbar()
    {
        AbstractTnComponent searchBar = MapContainer.getInstance().getFeature(ID_SEARCH_BAR);
        if (searchBar instanceof AbstractTnContainer)
        {
            searchBar.setVisible(isSearchBarNeeded());
        }
    }
    
    private void removeFlags()
    {
        if(origFlagAnnotation != null)
        {
            MapContainer.getInstance().removeFeature(origFlagAnnotation);
            origFlagAnnotation = null;
        }
        
        if(destFlagAnnotation != null)
        {
            MapContainer.getInstance().removeFeature(destFlagAnnotation);
            destFlagAnnotation = null;
        }
    }
    
    protected void updateSelectedAnnotation()
    {
        if (selectedPoiIndex >= 0 && selectedPoiIndex < poiAnnotationsInMap.size())
        {
            PoiAnnotation oldSelectedpoi = (PoiAnnotation) poiAnnotationsInMap.elementAt(selectedPoiIndex);
            if (oldSelectedpoi.getId() != 0 && oldSelectedpoi.isSetFocused())
            {
                oldSelectedpoi.update(oldSelectedpoi.getId());
            }
        }
    }
    
    protected void addFlags()
    {
        AbstractTnImage startFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
        if (startFlag != null)
        {
            double startLatD = 0;
            double startLonD = 0;
            Address originAddress = (Address) model.get(KEY_O_ADDRESS_ORI);
            if (originAddress != null && originAddress.getStop() != null)
            {
                Stop originStop = originAddress.getStop();
                startLatD = originStop.getLat() / 100000.0d;
                startLonD = originStop.getLon() / 100000.0d;
            }
            else
            {
                Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
                Segment startSeg = currentRoute == null ? null : currentRoute.segmentAt(0);
                if (startSeg != null)
                {
                    int[] originPointLatLon = currentRoute.getOriginLatLon();
                    if(startSeg.isEdgeResolved() && originPointLatLon.length > 1)
                    {
                        startLatD = originPointLatLon[Route.WAYPOINTS_LAT_INDEX] / 100000.0d;
                        startLonD = originPointLatLon[Route.WAYPOINTS_LON_INDEX] / 100000.0d;
                    }
                }
            }
            if (startLatD != 0 && startLonD != 0)
            {
                float pivotX = 0.5f;
                float pivotY = 0;
                origFlagAnnotation = new ImageAnnotation(startFlag, startLatD, startLonD, pivotX, pivotY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                MapContainer.getInstance().addFeature(origFlagAnnotation);
            }
        }

        // Add end flag here.
        double endLatD = 0;
        double endLonD = 0;
        AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED
                .getImage();
        if (destFlag != null)
        {
            Address destAddress = (Address) model.get(KEY_O_ADDRESS_DEST);
            if (destAddress != null && destAddress.getStop() != null)
            {
                Stop destStop = destAddress.getStop();
                if (destStop != null)
                {
                    endLatD = destStop.getLat() / 100000.0d;
                    endLonD = destStop.getLon() / 100000.0d;
                }
            }
            else
            {
//                Route currentRoute = RouteWrapper.getInstance().getCurrentRoute();
                Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
                int segMentSize = -1;
                if(currentRoute != null && currentRoute.getSegments() != null)
                {
                    segMentSize = currentRoute.getSegments().length;
                }
                if (segMentSize > 0)
                {
                    Segment endSeg = currentRoute.segmentAt(segMentSize - 1);
                    int[] destPointLatLon = currentRoute.getDestLatLon();
                    if (endSeg != null)
                    {
                        if(endSeg.isEdgeResolved() && destPointLatLon.length > 1)
                        {
                            endLatD = destPointLatLon[Route.WAYPOINTS_LAT_INDEX] / 100000.0d;
                            endLonD = destPointLatLon[Route.WAYPOINTS_LON_INDEX] / 100000.0d;;
                        }
                    }
                }
            }

            if (endLatD != 0 && endLonD != 0)
            {
                float pivotX = 0.5f;
                float pivotY = 0;
                destFlagAnnotation = new ImageAnnotation(destFlag, endLatD, endLonD, pivotX, pivotY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                MapContainer.getInstance().addFeature(destFlagAnnotation);
            }
        }
    }
    
    protected void updateFlags()
    {
        removeFlags();
        addFlags();
        updateSelectedAnnotation();//foreground PopBubble
    }

    protected void startFollowVehicleMode(MapContainer mapContainer)
    {
        // map center follow with car.
        MapContainer.getInstance().followVehicle();
        MapContainer.getInstance().setRenderingMode(NavSdkMapProxy.RENDERING_MODE_2D_NORTH_UP);
    }
    
    protected void startVehicalePositionService()
    {
        // status bar update.
        MapVehiclePositionService.getInstance().setVehiclePositionCallback(this);
        // let car move.
        MapVehiclePositionService.getInstance().resume(true);
    }
    
    protected void stopVehiclePositionService()
    {
        // stop car move.
        MapVehiclePositionService.getInstance().pause();
        // stop status bar update.
        MapVehiclePositionService.getInstance().setVehiclePositionCallback(null);
        closeNotifications();
    }
    
    private void initMapSummary(MapContainer mapContainer)
    {
        if (mapContainer != null)
        {
//            final long viewId = mapContainer.getViewId();
            final AbstractTnImage startFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
            final AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();

            boolean isDecimatedRoute = true;
            Route currentRoute = (Route) model.get(KEY_O_MAP_SUMMARY_DECIMATED_ROUTE);

            if (currentRoute == null)
            {
//                currentRoute = RouteWrapper.getInstance().getCurrentRoute();
                currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
                isDecimatedRoute = false;
            }
            if (currentRoute != null)
            {
                final Route route = currentRoute;
                Vector routes = new Vector();
                routes.add(route.getRouteID());
                mapContainer.showRoutes(routes, "" + route.getRouteID(), false);
                mapContainer.lookAtRoute(route.getRouteID(), false);
            }

            // Add start flag here.
            if (startFlag != null)
            {
                if (currentRoute.segmentsSize() > 0)
                {
                    Segment startSeg = currentRoute.segmentAt(0);
                    if (startSeg != null)
                    {
                        if(isDecimatedRoute || startSeg.isEdgeResolved())
                        {
                            int[] originPointLatLon = currentRoute.getOriginLatLon();
                            if (originPointLatLon.length > 1)
                            {
                                double degreesLatitude = originPointLatLon[Route.WAYPOINTS_LAT_INDEX] / 100000.0d;
                                double degreesLongitude = originPointLatLon[Route.WAYPOINTS_LON_INDEX] / 100000.0d;
                                float pivotX = 0.5f;
                                float pivotY = 0;
                                origFlagAnnotation = new ImageAnnotation(startFlag,
                                        degreesLatitude, degreesLongitude, pivotX,
                                        pivotY,
                                        ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                                MapContainer.getInstance().addFeature(origFlagAnnotation);
                            }
                        }
                    }
                }
            }

            // Add end flag here.
            if (destFlag != null)
            {
                if (currentRoute.segmentsSize() > 0)
                {
                    Segment destSeg = currentRoute.segmentAt(currentRoute.segmentsSize() - 1);
                    if (destSeg != null)
                    {
                        if (isDecimatedRoute
                                || destSeg.isEdgeResolved())
                        {
                            int[] destPointLatLon = currentRoute.getDestLatLon();
                            if (destPointLatLon.length > 1)
                            {
                                double degreesLatitude = destPointLatLon[Route.WAYPOINTS_LAT_INDEX] / 100000.0d;
                                double degreesLongitude = destPointLatLon[Route.WAYPOINTS_LON_INDEX] / 100000.0d;
                                float pivotX = 0.5f;
                                float pivotY = 0;
                                destFlagAnnotation = new ImageAnnotation(destFlag,
                                        degreesLatitude, degreesLongitude, pivotX,
                                        pivotY,
                                        ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                                MapContainer.getInstance().addFeature(destFlagAnnotation);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isMapDuringNavigation()
    {
        boolean isMapDuringNavigation = NavRunningStatusProvider.getInstance().isNavRunning();
        return isMapDuringNavigation;
    }

    private BottomContainerArgs createBottomBarArgs(boolean isSummaryMap)
    {
        BottomContainerArgs args = new BottomContainerArgs(CMD_NONE);
        
        boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
        args.cmdIds = new int[5];
        if(isDynamicRoute)
        {
            args.cmdIds[0] = CMD_NAVIGATION;
        }
        else
        {
            args.cmdIds[0] = CMD_DIRECTIONS;
        }
        
        if (isSummaryMap)
        {
            args.cmdIds[1] = CMD_NONE;
        }
        else
        {
            args.cmdIds[1] = CMD_ROUTE_SUMMARY;
        }
        args.cmdIds[2] = CMD_COMMON_DSR;

        if (isSummaryMap)
        {
            boolean isOnboard = NavRunningStatusProvider.getInstance().isOnBoardRoute()
                    || !NetworkStatusManager.getInstance().isConnected();
            args.enableIcons[3] = isOnboard ? false : true;
            args.cmdIds[3] = CMD_COMMON_LINK_TO_SEARCH;
        }
        else
        {
            args.cmdIds[3] = CMD_NONE;
        }
        args.cmdIds[4] = CMD_COMMON_END_TRIP;
        
        args.displayStrResIds = new int[5];
        if(isDynamicRoute)
        {
            args.displayStrResIds[0] = IStringCommon.RES_NAVIGATION;
        }
        else
        {
            args.displayStrResIds[0] = IStringCommon.RES_DIRECTIONS;
        }
        args.displayStrResIds[1] = IStringCommon.RES_ROUTE;
        //args.displayStrResIds[2] = IStringCommon.RES_BTTN_DSR; For dsr btn, there's no string
        args.displayStrResIds[3] = IStringCommon.RES_NEARBY;
        args.displayStrResIds[4] = IStringCommon.RES_BTTN_EXIT;
        
        args.unfocusImageAdapters = new TnUiArgAdapter[5];
        if(isDynamicRoute)
        {
            args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_UNFOCUS;
            
        }
        else
        {
            args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_ICON_DIRECTIONS_UNFOCUSED;
        }
        args.unfocusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_UNFOCUS;
        args.unfocusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.unfocusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_UNFOCUS;
        args.unfocusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_UNFOCUS;
        
        args.disableImageAdapters = new TnUiArgAdapter[5];
        args.disableImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_DISABLE;
        
        args.focusImageAdapters = new TnUiArgAdapter[5];
        if(isDynamicRoute)
        {
            args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_FOCUS;
        }
        else
        {
            args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_ICON_DIRECTIONS_FOCUSED;
        }
        args.focusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_FOCUS;
        args.focusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;

        args.focusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_FOCUS;

        args.focusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_FOCUS;
        
        
        return args;
    }

    protected AbstractTnContainer createBottomContainer(final AbstractTnContainer titleContainer, final BottomContainerArgs bottomBarArgs)
    {
        AbstractTnContainer container = super.createBottomContainer(titleContainer, bottomBarArgs);
        boolean isMapDuringNavigation = this.isMapDuringNavigation();
        
        if(isMapDuringNavigation)
        {
            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);
            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);
        }
        return container;
    }
    
    public void poiResultUpdate(int status, int resultType, String msg, PoiDataWrapper poiDataWrapper)
    {
        // TODO Auto-generated method stub

    }

    public void handleMapRgc(double latitude, double longitude)
    {
        //[NY]: protect here. For RGC will clear all things on map.
        //[CR]: Comment it our to keep the same behavior with IOS
//        if(this.isMapDuringNavigation())
//        {
//            return;
//        }
        
        float zoomLevel = MapContainer.getInstance().getZoomLevel();
        if (zoomLevel > MapConfig.MAP_DEFAULT_ZOOM_LEVEL)//TODO should define the mini zoom Level by PM. 
        {
            return;
        }
        int lat = (int) (latitude * 100000);
        int lon = (int) (longitude * 100000);

        this.model.put(KEY_I_RGC_LAT, lat);
        this.model.put(KEY_I_RGC_LON, lon);
        this.handleViewEvent(CMD_MAP_RGC);
        return;
    }
    
    public void handleTouchEventOnMap(MapContainer container, TnUiEvent uiEvent)
    {
    }
    
    public void updateVechiclePostion(final TnLocation location)
    {
        final MapContainer mapContainer = MapContainer.getInstance();
        int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
        if(mapFromType == TYPE_MAP_FROM_SUMMARY)
        {
            return;
        }
        if(location != null)
        {
        	String provider = location.getProvider();
        	if ((provider.equalsIgnoreCase(TnLocationManager.NETWORK_PROVIDER)
                    || provider.equalsIgnoreCase(TnLocationManager.TN_NETWORK_PROVIDER)))
        	{
        		if(!MapVehiclePositionService.getInstance().isNetworkLocationAvailable())
        		{
        			addStatusInfo();
        		}
        	}
        	else
        	{
        	    closeNotifications();
        	}
        	
            if (isFollowMeMap())
            {
                boolean isNeedMove = false;
                boolean isNeedZoom = false;
                if (isCurrentLocationPressed)
                {
                    isNeedMove = true;
                }
                if ((MapContainer.getInstance().getInteractionMode() == IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE && !MapVehiclePositionService
                        .getInstance().isLocationAvailable()) || isFirstTimeDisplayMap)
                {
                    isNeedMove = true;
                    isNeedZoom = true;
                }
                else if(isCurrentLocationPressed)
                {
                    if ((provider.equalsIgnoreCase(TnLocationManager.NETWORK_PROVIDER)
                            || provider.equalsIgnoreCase(TnLocationManager.TN_NETWORK_PROVIDER)))
                    {
                        checkParlaySize(location);
                    }
                }
                
                if(isNeedMove)
                {
                    startFollowVehicleMode(mapContainer);
                }
                if(isNeedZoom)
                {
                    
                    if (provider.equalsIgnoreCase(TnLocationManager.NETWORK_PROVIDER)
                            || provider.equalsIgnoreCase(TnLocationManager.TN_NETWORK_PROVIDER))
                    {
                        updateZoomForCellLocation(location);
                    }
                    else
                    {
                        updateZoomForGpsLocation();
                    }
                }
                if(isNeedMove || isNeedZoom)
                {
                    isCurrentLocationPressed = false;
                }
                
                isFirstTimeDisplayMap = false;
            }
            else
            {
                if (isCurrentLocationPressed)
                {
                    final double latitude = location.getLatitude() / 100000.0d;
                    final double longitude = location.getLongitude() / 100000.0d;
//                    final float heading = location.getHeading();
                    mapContainer.setMapTransitionTime(0.0f);
                    mapContainer.lookAt(latitude, longitude);
                    isCurrentLocationPressed = false;
                }
            }
        }
        else
        {
            if(!isFollowMeMap() && !isCurrentLocationPressed)
            {
                return;
            }
            addStatusInfo();
        }
    }

    public void checkParlaySize(final TnLocation location)
    {
        calcProperZoomLevel(location);
    }
    
    public void noGpsTimeout()
    {
        if(!isFollowMeMap() && !isCurrentLocationPressed)
        {
            return;
        }
        showGpsNotAvailableInfo();
        isCurrentLocationPressed = false;
    }
    
    protected void updateZoomForCellLocation(final TnLocation location)
    {
        calcProperZoomLevel(location);
    }
    
//TODO need API in NavSDK to calc the zoomlevel 
    protected void calcProperZoomLevel(final TnLocation location)
    {
//        Runnable runnable = new Runnable()
//        {
//            public void run()
//            {
//                IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//                long viewId = MapContainer.getInstance().getViewId();
//
//                int lat = location.getLatitude();
//                int lon = location.getLongitude();
//                int meterAccuracy = location.getAccuracy();
//                double minLat = (lat - meterAccuracy) / 100000.0d;
//                double maxLat = (lat + meterAccuracy) / 100000.0d;
//                double minLon = (lon - meterAccuracy) / 100000.0d;
//                double maxLon = (lon + meterAccuracy) / 100000.0d;
//
//                int x = 0;
//                int y = 0;
//                
//                int width = uiDecorator.SCREEN_WIDTH.getInt();
//                
//                int bottomHeight = uiDecorator.BOTTOM_BAR_HEIGHT.getInt();      
//                if (!isMapDuringNavigation())
//                {
//                    bottomHeight = 0;
//                }
//                int height = uiDecorator.SCREEN_HEIGHT.getInt()
//                        - AppConfigHelper.getStatusBarHeight()
//                        - bottomHeight;
//
//                float[] zoomArray = new float[1];
//                double[] latArray = new double[1];
//                double[] lonArray = new double[1];
//                mapEngine.calcRegion(viewId, maxLat, minLon, minLat, maxLon, x, y, width,
//                    height, zoomArray, latArray, lonArray);
//
//                float currentZoomLevel = mapEngine.getZoomLevel(viewId);
//                if (currentZoomLevel < zoomArray[0])
//                {
//                    mapEngine.setZoomLevel(viewId, zoomArray[0]);
//                    MapContainer.getInstance().updateZoomBtnState((int)zoomArray[0], false);
//                }
//            }
//        };
//        
//        MapContainer.getInstance().postRenderEvent(runnable);
    }
    
    protected void updateZoomForGpsLocation()
    {
//        Runnable runnable = new Runnable()
//        {
//            public void run()
//            {
//                IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//                long viewId = MapContainer.getInstance().getViewId();
//                mapEngine.setZoomLevel(viewId, MAP_DEFAULT_ZOOM_LEVEL);
//                MapContainer.getInstance().updateZoomBtnState((int)MAP_DEFAULT_ZOOM_LEVEL, false);
//            }
//        };
//
//        MapContainer.getInstance().postRenderEvent(runnable);
        MapContainer.getInstance().setZoomLevel( MapConfig.MAP_DEFAULT_ZOOM_LEVEL);
        MapContainer.getInstance().updateZoomBtnState(MapConfig.MAP_DEFAULT_ZOOM_LEVEL, false);
    }
    
    protected void removeStatusInfo()
    {
        Notifier.getInstance().removeListener(this);
        if(noGpsAnimationNotificaton != null)
        {
            noGpsAnimationNotificaton.hideImmediately();
            noGpsAnimationNotificaton = null;
        }
    }
    
    protected void addStatusInfo()
    {
        closeNotifications();
        
        FrogLabel label = createStatusInfo();
        if(label != null)
        {
            noGpsAnimationNotificaton = UiFactory.getInstance().createSlidableContainer(ID_GPS_NOT_AVAILABLE_NOTIFICATION);
            noGpsAnimationNotificaton.setContent(label);
            noGpsAnimationNotificaton.setTimeout(GETTING_GPS_DISPLAY_TIMEOUT, CMD_NONE);
            noGpsAnimationNotificaton.setAnimationDuration(0, 0);
            int x = ((MapUiDecorator)uiDecorator).STATUS_X.getInt();
            int y = ((MapUiDecorator)uiDecorator).STATUS_Y.getInt() + AppConfigHelper.getTopBarHeight();
            int w = ((MapUiDecorator)uiDecorator).STATUS_WIDTH.getInt();
            int h = ((MapUiDecorator)uiDecorator).STATUS_HEIGHT.getInt();
            noGpsAnimationNotificaton.showAt(MapContainer.getInstance(), x, y, w, h, false);
            noGpsAnimationNotificaton.setSizeChangeListener(this);
        }
        Notifier.getInstance().addListener(this);
    }
    
    protected FrogLabel createStatusInfo()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String status = bundle.getString(IStringNav.RES_STATUS_GETTING_GPS, IStringNav.FAMILY_NAV);
        FrogLabel label = createNotification(status, true);
        return label;
    }
    
    protected void showGpsNotAvailableInfo()
    {
        closeNotifications();
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String status = bundle.getString(IStringCommon.RES_GPS_NOT_AVAILABLE, IStringCommon.FAMILY_COMMON);
        FrogLabel label = createNotification(status, false);
        
        if(label != null)
        {
            gpsNotAvailableNotification = UiFactory.getInstance().createSlidableContainer(ID_GPS_NOT_AVAILABLE_NOTIFICATION);
            gpsNotAvailableNotification.setContent(label);
            gpsNotAvailableNotification.setTimeout(GPS_NOTIFICATION_TIMEOUT, CMD_GPS_NOT_AVAILABLE_NOTIFICATION_CLOSE);
            gpsNotAvailableNotification.setAnimationDuration(0, 0);
            int x = ((MapUiDecorator)uiDecorator).STATUS_X.getInt();
            int y = ((MapUiDecorator)uiDecorator).STATUS_Y.getInt() + AppConfigHelper.getTopBarHeight();
            int w = ((MapUiDecorator)uiDecorator).STATUS_WIDTH.getInt();
            int h = ((MapUiDecorator)uiDecorator).STATUS_HEIGHT.getInt();
            gpsNotAvailableNotification.showAt(MapContainer.getInstance(), x, y, w, h, false);
            gpsNotAvailableNotification.setSizeChangeListener(this);
        }
    }
    
    protected void closeNotifications()
    {
        closeGpsNotAvailableInfo();
        removeStatusInfo();
    }
    
    protected void closeGpsNotAvailableInfo()
    {
        if(gpsNotAvailableNotification != null)
        {
            gpsNotAvailableNotification.hideImmediately();
            gpsNotAvailableNotification = null;
        }
    }
    
    protected FrogLabel createNotification(String status, boolean hasDot)
    {
        FrogLabel label = null;
        if (status != null)
        {
            int whiteColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
            AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR);
            label = UiFactory.getInstance().createLabel(ID_STATUS_INFO, status);
            label.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
            label.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
            label.setForegroundColor(whiteColor, whiteColor);
            label.setFont(font);
            label.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MapUiDecorator)uiDecorator).STATUS_X);
            label.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MapUiDecorator)uiDecorator).STATUS_Y);
            label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator)uiDecorator).STATUS_WIDTH);
            label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator)uiDecorator).STATUS_HEIGHT);
            StringBuffer sb = new StringBuffer(status);
            if(hasDot)
            {
                for(int i = 1; i < MAX_UPDATE_STEPS; i++)
                {
                    sb.append(DOT_CHAR);
                }
            }
            int hPadding = (((MapUiDecorator)uiDecorator).STATUS_WIDTH.getInt() - font.stringWidth(sb.toString())) / 2;
            label.setPadding(hPadding, 0, hPadding, 0);
        }

        return label;
    }
    
    protected void updateStatusInfo(final boolean isChangePosition)
    {
        int state = model.getState();
        if(state == STATE_MAIN || state == STATE_MAP_SUMMARY)
        {
            ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
                    String status = bundle.getString(IStringNav.RES_STATUS_GETTING_GPS, IStringNav.FAMILY_NAV);
                    
                    if(noGpsAnimationNotificaton != null && noGpsAnimationNotificaton.isShown())
                    {
                        FrogLabel statusInfo = (FrogLabel) noGpsAnimationNotificaton.getContent();
                        if (statusInfo != null)
                        {
                            if (updateStep % MAX_UPDATE_STEPS == 0)
                            {
                                updateStep = 1;
                                statusInfo.setText(status);
                            }
                            else
                            {
                                String labelText = statusInfo.getText();
                                labelText += DOT_CHAR;
                                statusInfo.setText(labelText);
                                updateStep++;
                            }
                        }
                        
                        int x = ((MapUiDecorator)uiDecorator).STATUS_X.getInt();
                        int y = ((MapUiDecorator)uiDecorator).STATUS_Y.getInt() + AppConfigHelper.getTopBarHeight();
                        int w = ((MapUiDecorator)uiDecorator).STATUS_WIDTH.getInt();
                        int h = ((MapUiDecorator)uiDecorator).STATUS_HEIGHT.getInt();
                        
                        if(isChangePosition)
                        {
                            noGpsAnimationNotificaton.showAt(MapContainer.getInstance(), x, y, w, h, false);
                        }
                        else
                        {
                            noGpsAnimationNotificaton.update(null, x, y, w, h);
                        }
                    }
                }
            });
        }
    }
    
    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimeStamp;
    }

    public long getNotifyInterval()
    {
        return NOTIFY_INTERVAL;
    }

    public void notify(long timestamp)
    {
        updateStatusInfo(false);
    }
    
    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimeStamp = timestamp;
    }
    
    protected void popAllViews()
    {
        super.popAllViews();
    }
    
    protected void activate()
    {
        super.activate();
		TeleNavDelegate.getInstance().registerApplicationListener(this);
        // If now status is in navigation, don't need to start GPS service because the GPS service is started already
        if (!NavSdkNavEngine.getInstance().isRunning())
        {
            startVehicalePositionService();
        }
        MapContainer.getInstance().enableGPSCoarse(true);
        initMapLayer();
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);

        int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
        if (mapFromType == TYPE_MAP_FROM_ENTRY || mapFromType == TYPE_MAP_FROM_AC || mapFromType == -1)
        {
            KontagentAssistLogger.startKtLogMapDisplay();
        }

    }
    
    protected void deactivateDelegate()
    {
        super.deactivateDelegate();
		TeleNavDelegate.getInstance().unregisterApplicationListener(this);
        if (!NavSdkNavEngine.getInstance().isRunning())
        {
            stopVehiclePositionService();
        }
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
        if (mapFromType == TYPE_MAP_FROM_ENTRY || mapFromType == TYPE_MAP_FROM_AC || mapFromType == -1)
        {
            KontagentAssistLogger.endLogMapDisplayTime();
        }
    }

    public void cleanPoiData()
    {
        poiAnnotationsInMap.removeAllElements();
        selectedPoiIndex = -1;
        currentPage = -1;
    }

    public void transferPoi(int direction)
    {
        if (direction == POI_FORWARD)
        {
            if (selectedPoiIndex + 1 < poiAnnotationsInMap.size())
            {
                tranferSelectedPoi(selectedPoiIndex + 1, PoiMisLogHelper.SHOW_TYPE_BY_ARROW);
            }
        }
        else if (direction == POI_BACK)
        {
            if ((selectedPoiIndex - 1) >= 0)
            {
                tranferSelectedPoi(selectedPoiIndex - 1, PoiMisLogHelper.SHOW_TYPE_BY_ARROW);
            }
        }
    }
    
    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public void transferPage(int direction)
    {
        // remove old 10 pois and add new 10 pois
        PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
        MapContainer.getInstance().removePoiAnnotations();
        poiAnnotationsInMap.removeAllElements();
        Address selectedAddress = null;
        boolean hasAdPoiInMap = false;
        
        if (poiDataManager.getSearchType() == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            addDestinationFlag(poiDataManager.getDestStop());
            
            Stop origStop = null;
            if (model.get(KEY_O_ADDRESS_ORI) instanceof Address)
            {
                Address origAddress = (Address) model.get(KEY_O_ADDRESS_ORI);
                origStop = origAddress.getStop();
            }
            else
            {
//                Route currentRoute = RouteWrapper.getInstance().getCurrentRoute();
                Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
                Segment startSeg = currentRoute == null ? null : currentRoute.segmentAt(0);
                if (startSeg != null)
                {
                    if(startSeg.isEdgeResolved())
                    {
                        int[] originPointLatLon = currentRoute.getOriginLatLon();
                        if (originPointLatLon.length > 1)
                        {
                            int degreesLatitude = originPointLatLon[Route.WAYPOINTS_LAT_INDEX];
                            int degreesLongitude = originPointLatLon[Route.WAYPOINTS_LON_INDEX];
                            origStop = new Stop();
                            origStop.setLat(degreesLatitude);
                            origStop.setLon(degreesLongitude);
                        }
                    }
                }
            }
            
            if (origStop != null)
            {
                addOriginFlag(origStop);
            }
        }
        else
        {
            if (model.get(KEY_O_ADDRESS_ORI) instanceof Address && model.get(KEY_O_ADDRESS_DEST) instanceof Address)
            {
                Address destAddress = (Address) model.get(KEY_O_ADDRESS_DEST);
                Address origAddress = (Address) model.get(KEY_O_ADDRESS_ORI);
                
                addDestinationFlag(destAddress.getStop());
                addOriginFlag(origAddress.getStop());
            }
        }
        
        Address adPoi = (Address)poiDataManager.getSponsoredAddress(this.currentPage);
        if(adPoi != null && adPoi.isValid() && adPoi.getPoi()!= null && adPoi.getPoi().getType() != Poi.TYPE_DUMMY_POI)
        {
            this.adPoiAnnotationInMap  = new PoiAnnotation(adPoi, 0, false, true, poiDataManager.getTotalNormalCount());
            adPoiAnnotationInMap.setCommandEventListener(this);
            poiAnnotationsInMap.addElement(adPoiAnnotationInMap);
            hasAdPoiInMap = true;
        }

        int endIndex = (currentPage + 1) * PoiDataRequester.DEFAULT_PAGE_SIZE;
        if(direction == POI_FORWARD && poiDataManager.getNormalAddressSize() < endIndex)
        {
            endIndex = poiDataManager.getNormalAddressSize();
        }
        PoiMisLogHelper.getInstance().showPoiPage(this.currentPage, endIndex - currentPage * PoiDataRequester.DEFAULT_PAGE_SIZE,
            hasAdPoiInMap);
        
        AbstractAnnotation focusedAnnotation = null;
        for (int i = currentPage * PoiDataRequester.DEFAULT_PAGE_SIZE; i < endIndex; i++)
        {
            Address address = poiDataManager.getNormalAddress(i);
            PoiAnnotation poiAnnotation;
            if (i % PoiDataRequester.DEFAULT_PAGE_SIZE == 0)// focused
            {
                PoiMisLogHelper.getInstance().showSelectedPoiMap(i, true, PoiMisLogHelper.SHOW_TYPE_BY_DEFAULT);
                poiAnnotation = new PoiAnnotation(address, i, true, hasAdPoiInMap, poiDataManager.getTotalNormalCount());
                selectedAddress = address;
                focusedAnnotation = poiAnnotation;
            }
            else
            {
                poiAnnotation = new PoiAnnotation(address, i, false, hasAdPoiInMap, poiDataManager.getTotalNormalCount());
            }
            poiAnnotation.setCommandEventListener(this);
            poiAnnotationsInMap.addElement(poiAnnotation);
        }
        if(hasAdPoiInMap)
            this.selectedPoiIndex = 1;
        else
            this.selectedPoiIndex = 0;

        int selectIndex = poiDataManager.getIndexOfMixedList(selectedAddress);
        poiDataManager.setSelectedIndex(selectIndex);
        MapContainer.getInstance().addFeature(this.poiAnnotationsInMap, true);
        showMapAnnotationRegion(poiAnnotationsInMap, focusedAnnotation == null ? null : focusedAnnotation.getAnnotationId());
        setMapOffset();
    }
    
    public void tranferSelectedPoi(final int newSelectedIndex, byte showType)
    {
        if (selectedPoiIndex >= 0 && selectedPoiIndex < poiAnnotationsInMap.size())
        {
            final PoiAnnotation oldSelectedpoi = (PoiAnnotation) poiAnnotationsInMap.elementAt(selectedPoiIndex);
            if (oldSelectedpoi.isSetFocused())
            {
                oldSelectedpoi.setFocused(false);
            }
        }

        else if (selectedPoiIndex == -1)
        {
            for (int i = 0; i < poiAnnotationsInMap.size(); i++)
            {
                final PoiAnnotation tempPoi = (PoiAnnotation) poiAnnotationsInMap.elementAt(i);
                if (tempPoi.isSetFocused())
                {
                    tempPoi.setFocused(false);
                    break;
                }
            }
        }
        
        if (newSelectedIndex >= 0 && newSelectedIndex < poiAnnotationsInMap.size())
        {
            final PoiAnnotation newSelectedpoi = (PoiAnnotation) poiAnnotationsInMap.elementAt(newSelectedIndex);
            MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getPoiTransitionTime());
            setMapOffset();            
            newSelectedpoi.setFocused(true);
            MapContainer.getInstance().lookAt(newSelectedpoi.lat, newSelectedpoi.lon);
            PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
            if (poiDataManager != null)
            {
                int selectIndex = poiDataManager.getIndexOfMixedList(newSelectedpoi.getAddress());
                poiDataManager.setSelectedIndex(selectIndex);

                PoiMisLogHelper.getInstance().showSelectedPoiMap(selectIndex, false, showType);
            }
        }
        
        this.selectedPoiIndex = newSelectedIndex; // -1 will close selected poi
    }
  
    public void tranferSelectedAddress(final int newSelectedIndex)
    {
        if (selectedAddressIndex >= 0 && selectedAddressIndex < addressAnnotationsInMap.size())
        {
            AddressAnnotation oldSelectedAddress = (AddressAnnotation) addressAnnotationsInMap.elementAt(selectedAddressIndex);
            if(oldSelectedAddress.isSetFocused())
            {
                oldSelectedAddress.setFocused(false);
            }
        }
        else if(selectedAddressIndex == -1)
        {
            for(int i=0; i< addressAnnotationsInMap.size(); i++)
            {
                AddressAnnotation tempAddress = (AddressAnnotation) addressAnnotationsInMap.elementAt(i);
                if(tempAddress.isSetFocused())
                {
                    tempAddress.setFocused(false);
                    break;
                }
            }
            
        }
        if (newSelectedIndex >= 0 && newSelectedIndex < addressAnnotationsInMap.size())
        {
            MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getPoiTransitionTime());
            AddressAnnotation newSelectedAddress = (AddressAnnotation) addressAnnotationsInMap.elementAt(newSelectedIndex);
            MapContainer.getInstance().lookAt(newSelectedAddress.lat, newSelectedAddress.lon);
            newSelectedAddress.setFocused(true);
            selectedAddressAnnotation = (AddressAnnotation) addressAnnotationsInMap.elementAt(newSelectedIndex);
        }
        
        if (newSelectedIndex >= 0)
        {
//            PoiMisLogHelper poiMisLogHelper = MisLogManager.getInstance().getFactory().getCurrentMisLogHelper();
//            if (poiMisLogHelper != null)
//            {
//                poiMisLogHelper.recordNormalPoiMislog(IMisLogConstants.TYPE_POI_VIEW_MAP, currentPage * PoiDataRequester.DEFAULT_PAGE_SIZE
//                        + newSelectedIndex);
//            }
        }

        this.selectedAddressIndex = newSelectedIndex; // -1 will close selected poi
    }
    
  public int getSelectedPoiIndex()
  {
      return selectedPoiIndex;
  }

  public int getSelectedAddressIndex()
  {
      return selectedAddressIndex;
  }
  
  public void setSelectedPoiIndex(int newSelectedPoiIndex)
  {
      tranferSelectedPoi(newSelectedPoiIndex, PoiMisLogHelper.SHOW_TYPE_UNDEFINED);
  }

  public void setSelectedAddressIndex(int newSelectedAddressIndex)
  {
      tranferSelectedAddress(newSelectedAddressIndex);
  }
  
  public Address getSelectedPoi()
  {
      if (selectedPoiIndex >= 0 && selectedPoiIndex < poiAnnotationsInMap.size())
      {
          PoiAnnotation annotation = (PoiAnnotation) poiAnnotationsInMap.elementAt(selectedPoiIndex);
          if (annotation != null)
          {
              return annotation.getAddress();
          }
      }
      return null;
  }
  
  public void tapNoAnnotation()
  {
      closePoiBubble();
      
      //http://jira.telenav.com:8080/browse/TNANDROID-2941
      //closeMapLayerPopup();
  }
  
    protected void closePoiBubble()
    {
        if (poiAnnotationsInMap != null && poiAnnotationsInMap.size() > 0 && selectedPoiIndex != -1)
        {
            setSelectedPoiIndex(-1);
        }

        if (addressAnnotationsInMap != null && addressAnnotationsInMap.size() > 0 && selectedAddressIndex != -1)
        {
            setSelectedAddressIndex(-1);
        }
    }
  
  protected void closeMapLayerPopup()
  {
      //For map view list to close popup
      AbstractTnComponent component = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
      if (component != null && component instanceof MapViewListComponent)
      {
          if (((MapViewListComponent) component).isListvisible())
          {
              ((MapViewListComponent) component).hideList();
          }
      }
  }
  
  private void addAndGotoSingleAddress(Address address)
  {
      if(address == null || !address.isValid())
          return;
      addressAnnotationsInMap.removeAllElements();
      if (address != null)
      {
          double lat = address.getStop().getLat() / 100000.0d;
          double lon = address.getStop().getLon() / 100000.0d;
          AddressAnnotation addressAnnotation = new AddressAnnotation(address, 0, true);
          addressAnnotation.setCommandEventListener(this);
          this.selectedAddressIndex = 0;
          this.selectedAddressAnnotation = addressAnnotation;
          addressAnnotationsInMap.addElement(addressAnnotation);
          MapContainer.getInstance().addFeature(addressAnnotation);
          MapContainer.getInstance().moveMapTo(lat, lon, 0, MapContainer.getInstance().getPoiTransitionTime());
      }
  }
  
    private void addAndGotoBillboard(Address address)
    {
        if (address == null || !address.isValid())
            return;

        poiAnnotationsInMap.removeAllElements();
        if (address != null)
        {
            double lat = address.getStop().getLat() / 100000.0d;
            double lon = address.getStop().getLon() / 100000.0d;
            adPoiAnnotationInMap = new PoiAnnotation(address, 0, true, false, 0);
            adPoiAnnotationInMap.setCommandEventListener(this);
            this.selectedPoiIndex = 0;
            poiAnnotationsInMap.addElement(adPoiAnnotationInMap);
            MapContainer.getInstance().addFeature(adPoiAnnotationInMap);
            MapContainer.getInstance().moveMapTo(lat, lon, 0, 0.0f);
        }
    }
  
    protected void resizeMapSummary()
    {
//        MapContainer.getInstance().postRenderEvent(new Runnable()
//        {
//            public void run()
//            {
//                String routeName = "default";
//                IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
                
//                final AbstractTnImage startFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
//                final AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();
//                
//                int startFlagWidth = 0;
//                int startFlagHeight = 0;
//                if(startFlag != null)
//                {
//                    startFlagWidth = startFlag.getWidth();
//                    startFlagHeight = startFlag.getHeight();
//                }
//                int destFlagWidth = 0;
//                int destFlagHeight = 0;
//                if(destFlag != null)
//                {
//                    destFlagWidth = destFlag.getWidth();
//                    destFlagHeight = destFlag.getHeight();
//                }
//                
//                int hPadding = (startFlagWidth + destFlagWidth) / 2;
//                int vPadding = (startFlagHeight + destFlagHeight) / 2;
//                int x = hPadding;
//                int y = ((MapUiDecorator) uiDecorator).TAB_OUTER_CONTAINER_HEIGHT.getInt() + vPadding;
//                int width = ((MapUiDecorator) uiDecorator).SCREEN_WIDTH.getInt() - hPadding * 2;
//                int height = ((MapUiDecorator) uiDecorator).SCREEN_HEIGHT.getInt() - AppConfigHelper.getStatusBarHeight()
//                        - ((MapUiDecorator) uiDecorator).TAB_OUTER_CONTAINER_HEIGHT.getInt()
//                        - ((MapUiDecorator) uiDecorator).BOTTOM_BAR_HEIGHT.getInt()
//                        - vPadding;
//                
//                mapEngine.showRegionForRoutes(MapContainer.getInstance().getViewId(), new String[]
//                { routeName }, x, y, width, height);
//            }
//        });
//        Route route = RouteWrapper.getInstance().getCurrentRoute();
        Route route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if(route!=null)
        {
            MapContainer.getInstance().lookAtRoute(route.getRouteID(), false);
        }
    }

    public void handleClickTrafficIncident(SelectedTrafficIncident incident)
    {
        model.put(KEY_O_TRAFFIC_INCIDENT_DETAIL, incident);
        TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, MapContainer.getInstance());
        tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_SHOW_TRAFFIC_DETAIL));
        handleUiEvent(tnUiEvent);
    }
    
    protected TnPopupContainer createTrafficAlertDetail(int state)
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();

        
        TnPopupContainer popup = UiFactory.getInstance().createPopupContainer(state);
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

        TnLinearContainer topContainer = createTrafficAlertDetailTopContainer();
        TnLinearContainer bottomContainer = createBottomButtonContainer(
            CMD_TRAFFIC_DETAIL_OK, bundle.getString(IStringCommon.RES_BTTN_DONE,
                IStringCommon.FAMILY_COMMON));

        container.add(topContainer);
        container.add(bottomContainer);

        popup.setContent(container);
        return popup;
    }
    
    public void eglSizeChanged()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                hideMapViewList();
                updateMapUI();
            }
        });
    }

    private void hideMapViewList()
    {
        AbstractTnComponent component = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
        if (component != null && component instanceof MapViewListComponent)
        {
            if (((MapViewListComponent) component).isListvisible())
            {
                ((MapViewListComponent) component).hideList();
            }
        }
    }
    
    
    public void mapViewSizeChanged()
    {
      final int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
      if(mapFromType == TYPE_MAP_FROM_SUMMARY)
      {
          ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
          {
              public void run()
              {
                  resizeMapSummary();
              }
          });
      }
  }
  
    private void updateMapUI()
    {
        if (this.model.getState() != STATE_MAP_SUMMARY && this.model.getPreState() != STATE_MAP_SUMMARY)
        {
            updateYPosition();
            updateSeachbar();
            updateMapSize();
            resetMapOffset();
        }
    }

    private void resetMapOffset()
    {
        if ((this.poiAnnotationsInMap.size() > 0 && selectedPoiIndex > -1)
                || (this.addressAnnotationsInMap.size() > 0 && selectedAddressIndex > -1))
        {
            setMapOffset();
        }
        else
        {
            MapContainer.getInstance().setMapVerticalOffset(0f);
        }
    }

    private void updateMapSize()
    {
        TnUiArgAdapter mapHeight = isSearchBarNeeded() ? ((MapUiDecorator) uiDecorator).MAP_HEIGHT
                : ((MapUiDecorator) uiDecorator).MAP_HEIGHT_WITHOUT_SEARCHBAR;
        TnUiArgAdapter mapY = isSearchBarNeeded() ? ((MapUiDecorator) uiDecorator).MAP_Y
                : ((MapUiDecorator) uiDecorator).MAP_Y_WITHOUT_SEARCHBAR;

        MapContainer.getInstance().setMapRect(((MapUiDecorator) uiDecorator).MAP_X, mapY,
            ((MapUiDecorator) uiDecorator).MAP_WIDTH, mapHeight);
    }

    private void setMapOffset()
    {
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
        {
            MapContainer.getInstance().setMapVerticalOffset(-0.45f);
        }
        else
        {
            MapContainer.getInstance().setMapVerticalOffset(0f);
        }
    }
  
  protected TnPopupContainer createSearchProgressPopup()
  {
      FrogProgressBox progressBox  = UiFactory.getInstance().createProgressBox(0, "");
      return progressBox;
  }
  

  private TnPopupContainer createNotifactionBox(int state, String notification, int timeout)
  {

      FrogMessageBox notificationBox = UiFactory.getInstance().createNotificationMessageBox(
          state,
          notification, timeout);
      notificationBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
      notificationBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
      
      final FrogMultiLine text = UiFactory.getInstance().createMultiLine(0, notification);
      text.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).NOTIFY_BOX_WIDTH);
      text.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
      text.setPadding(0, 0, 0, 0);
      text.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MESSAGE_BOX));
      text.sublayout(0, 0);

      
      notificationBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).NOTIFY_BOX_WIDTH);
      notificationBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
      {
          public Object decorate(TnUiArgAdapter args)
          {
              int height = text.getPreferredHeight() + 6 * 2;
              return PrimitiveTypeCache.valueOf(height);
          }
      }));
      
      notificationBox.setCommandEventListener(this);
      return notificationBox;
  }

  public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
  {
      if(gpsNotAvailableNotification != null && gpsNotAvailableNotification.isShown())
      {
          showGpsNotAvailableInfo();
      }
      
      if(noGpsAnimationNotificaton != null && noGpsAnimationNotificaton.isShown())
      {
          updateStatusInfo(true);
      }
      ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
  }
  
    private void showMapAnnotationRegion(Vector annotations, long selectedAnnotationId)
    {
        MapContainer.getInstance().lookAtAnnotations(annotations, selectedAnnotationId);
        MapContainer.getInstance().updateZoomBtnState(MapContainer.getInstance().getZoomLevel(), false);
    }
    
    private TnLinearContainer createTrafficAlertDetailTopContainer()
    {
        SelectedTrafficIncident incident = (SelectedTrafficIncident) model.fetch(KEY_O_TRAFFIC_INCIDENT_DETAIL);
        
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_WIDTH);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_TOP_HEIGHT);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);

        FrogNullField nullFieldTop = UiFactory.getInstance().createNullField(0);
        nullFieldTop.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).POPUP_TOP_PADDING); 
        container.add(nullFieldTop);

        String titleStr  = "";
        if(TYPE_SPEED_TRAP.equals(incident.getType()))
        {
            titleStr  = ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_SPEED_TRAP, IStringMap.FAMILY_MAP);
        }
        else if(TYPE_TRAFFIC_CAMERA.equals(incident.getType()))
        {
            titleStr  = ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_TRAFFIC_CAMERA, IStringMap.FAMILY_MAP);
        }
        else
        {
            titleStr  = ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_TRFFIC_INCIDENT, IStringMap.FAMILY_MAP);
        }
        FrogLabel title = UiFactory.getInstance().createLabel(0,titleStr);
        title.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        container.add(title);

        FrogNullField nullFieldTitle = UiFactory.getInstance().createNullField(0);
        nullFieldTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).POPUP_TITLE_PADDING); 
        container.add(nullFieldTitle);

        String crossStreetStr = incident.getCrossStreet();
        if (crossStreetStr != null && crossStreetStr.length() > 0)
        {
            FrogLabel crossStreet = UiFactory.getInstance().createLabel(0, crossStreetStr);
            crossStreet.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
            container.add(crossStreet);
        }
        
        TnLinearContainer contentContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);

        if (isComponentNeeded(INinePatchImageRes.FAVORITE_PURE_WHITE_BG  + INinePatchImageRes.ID_UNFOCUSED))
        {
            contentContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
            contentContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
        }
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_CONTENT_WIDTH);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT);

        TnLinearContainer contentInnerContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);

        TnScrollPanel scrollPanel = UiFactory.getInstance().createScrollPanel(0, true);

        scrollPanel.set(contentInnerContainer);

        contentContainer.add(scrollPanel);

        String alertMessage = incident.getMessage();
        FrogMultiLine messageMultiLine = UiFactory.getInstance().createMultiLine(0, alertMessage);
        messageMultiLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_CONTENT_WIDTH);
        int multilineGap = ((MapUiDecorator) uiDecorator).TRAFFIC_ALERT_MULTILINE_GAP;
        messageMultiLine.setPadding(multilineGap, multilineGap, multilineGap, multilineGap);
        messageMultiLine.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
        contentInnerContainer.add(messageMultiLine);

        container.add(contentContainer);

        return container;
    }

    private TnLinearContainer createBottomButtonContainer(int btnCmd, String btnString)
    {
        TnLinearContainer bottomContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_WIDTH);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).POPUP_BOTTOM_PART_HEIGHT);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);

        FrogNullField nullFieldTop = UiFactory.getInstance().createNullField(0);
        nullFieldTop.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).POPUP_BUTTON_PADDING); 
        bottomContainer.add(nullFieldTop);
        
        FrogButton rightButton = UiFactory.getInstance().createButton(0, btnString);
        rightButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MapUiDecorator) uiDecorator).POPUP_BOTTOM_BUTTON_WIDTH);
        rightButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MapUiDecorator) uiDecorator).POPUP_BOTTOM_BUTTON_HEIGHT);
        TnMenu menu = new TnMenu();
        menu.add("", btnCmd);
        rightButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        rightButton.setCommandEventListener(this);

        bottomContainer.add(rightButton);

        return bottomContainer;
    }
    
    public void onPinchEnd()
    {
        super.onPinchEnd();
        MapContainer.getInstance().updateZoomBtnState(MapContainer.getInstance().getZoomLevel(), false);
    }
    
    private void addDestinationFlag(Stop destStop)
    {
        AbstractTnImage imageFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();
        addStopFlag(destStop, imageFlag);
    }
    
    private void addOriginFlag(Stop stop)
    {
        AbstractTnImage imageFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
        addStopFlag(stop, imageFlag);
    }

    protected void addStopFlag(Stop destStop, AbstractTnImage imageFlag)
    {
        if (destStop != null)
        {
            int destLat = destStop.getLat();
            int destLon = destStop.getLon();
            if (destLat != 0 && destLon != 0)
            {
                double latD = destLat / 100000.0d;
                double lonD = destLon / 100000.0d;
                if (imageFlag != null)
                {
                    float pivotX = 0.5f;
                    float pivotY = 0;
                    ImageAnnotation image = new ImageAnnotation(imageFlag, latD, lonD, pivotX, pivotY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                    MapContainer.getInstance().addFeature(image);
                }
            }
        }
    }
    
    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
    }


    @Override
    public void appActivated(String[] params)
    {
        if (model.isActivated() && NavRunningStatusProvider.getInstance().isNavRunning())
        {
            TnScreen screen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
            if (screen != null && (screen.getId() == STATE_MAP_SUMMARY || screen.getId() == STATE_MAIN))
            {
                relocateMapLogoProvider(uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR);
            }
        }
    }

    @Override
    public void appDeactivated(String[] params)
    {
    
    }

    @Override
    public void gpsWeak()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void initMapLayer()
    {
        super.initMapLayer();
        
        isLastTimeConnected = NetworkStatusManager.getInstance().isConnected();
    }

}


/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TurnMapView.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.map.MapConfig;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.nav.movingmap.MovingMapHelper;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.map.DayNightService;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.IMapUIEventListener;
import com.telenav.ui.citizen.map.ImageAnnotation;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2010-12-13
 */
public class TurnMapViewTouch extends AbstractCommonMapView implements ITurnMapConstants
{
    protected int oldOrientation;
    protected static float TURN_MAP_DEFAULT_ZOOM = 1.0f;
    final static int[] textIdForType = new int[] {
        IStringNav.RES_CONTINUE_ON, 
        IStringNav.RES_TURN_SLIGHT_RIGHT, 
        IStringNav.RES_TURN_RIGHT,
        IStringNav.RES_TURN_HARD_RIGHT, 
        IStringNav.RES_U_TURN,
        IStringNav.RES_TURN_HARD_LEFT,
        IStringNav.RES_TURN_LEFT,
        IStringNav.RES_TURN_SLIGHT_LEFT,
        -1, -1,
        IStringNav.RES_ENTER,
        IStringNav.RES_ENTER,
        IStringNav.RES_EXIT,
        IStringNav.RES_EXIT,
        IStringNav.RES_MERGE,
        IStringNav.RES_MERGE,
        IStringNav.RES_CONTINUE_ON,
        IStringNav.RES_ARRIVE,
        IStringNav.RES_ARRIVE,
        IStringNav.RES_ARRIVE,
        IStringNav.RES_ROUND_ABOUT,
        IStringNav.RES_EXIT_ROUND_ABOUT,
        -1, -1, -1, -1,
        IStringNav.RES_BEAR_LEFT,
        IStringNav.RES_BEAR_RIGHT,
    };
    
    protected double currentSegLat;
    protected double currentSegLon;
    protected float currentSegZoom = TURN_MAP_DEFAULT_ZOOM;
    
    ImageAnnotation origFlagAnnotation;
    ImageAnnotation destFlagAnnotation;
    
    protected boolean isLastConnected = false;
    
    public TurnMapViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
      //Fix bug http://jira.telenav.com:8080/browse/TN-12222
        MapContainer.getInstance().removeMapUIEventListener();
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_GETTING_TURN_MAP:
            {
                popup = createGettingRouteProgressBox(STATE_GETTING_TURN_MAP);
                break;
            }
            case STATE_GET_ROUTE_EDGE:
            {
                popup = createLoadingProgressBox(STATE_GET_ROUTE_EDGE);
                break;
            }
            case STATE_GET_TURN_MAP_ERROR:
            {
                String errorMsg = model.getString(KEY_S_ERROR_MESSAGE);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
                    IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
                messageBox.setCommandEventListener(this);
                return messageBox;
            }
        }
        return popup;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch(state)
        {
            case STATE_TURN_MAP:
            {
                screen = createTurnMapScreen(state);
                break;
            }
        }
        return screen;
    }

    protected TnScreen createTurnMapScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        createMapContainer(screen);
        boolean isStaticRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
        if(isStaticRoute)
        {
            TnMenu screenMenu = screen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);
            if(screenMenu == null)
            {
                screenMenu = new TnMenu();
                screen.getRootContainer().setMenu(screenMenu, AbstractTnComponent.TYPE_MENU);
            }
            screenMenu.remove(ICommonConstants.CMD_COMMON_HOME);
            String navigate = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NAVIGATE, IStringNav.FAMILY_NAV);
            screenMenu.add(navigate, CMD_NAVIGATE, 0);
            
            String endTrip = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_END_TRIP_BTTN, IStringNav.FAMILY_NAV);
            screenMenu.add(endTrip, CMD_COMMON_EXIT, 1);
        }
        return screen;
    }
    
    protected AbstractTnComponent createTurnMapInfoComponent()
    {
        TurnMapInfoComponent turnMapInfoComponent = new TurnMapInfoComponent(ID_TURN_MAP_INFO_COMPONENT);
        turnMapInfoComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INFO_WIDTH);
        turnMapInfoComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INFO_HEIGHT);
        turnMapInfoComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INFO_X);
        turnMapInfoComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INFO_Y);
        turnMapInfoComponent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MAP_SEMI_TRANSPARENT_BANNER);
        turnMapInfoComponent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MAP_SEMI_TRANSPARENT_BANNER);
        turnMapInfoComponent.setIconCommand(CMD_CANVAS_PREVIOUS, CMD_CANVAS_NEXT);
        turnMapInfoComponent.setCommandEventListener(this);
        return turnMapInfoComponent;
    }
    
    protected AbstractTnComponent createTurnIndexComponent()
    {
        TurnMapIndexComponent turnIndexComponent = new TurnMapIndexComponent(ID_TURN_MAP_TURN_INDEX_COMPONENT);
        turnIndexComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INDEX_WIDTH);
        turnIndexComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INDEX_HEIGHT);
        turnIndexComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INDEX_X);
        turnIndexComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((TurnMapUiDecorator)uiDecorator).TURN_MAP_INDEX_Y);
        turnIndexComponent.setAlpha(0x9E);
        return turnIndexComponent;
    }
    
    protected void addControlButtons(final MapContainer mapContainer)
    {
    	boolean isDayColor = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? false : true;    	
    	FrogButton zoomInButton = new FrogButton(ID_ZOOM_IN, "");    	
    	if (isDayColor)
        {
            zoomInButton.setIcon(
                ImageDecorator.IMG_MAP_ZOOM_IN_ICON_FOCUSED.getImage(),
                ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_UNFOCUSED.getImage(),
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);    
            zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.MAP_ZOOM_TOP_BG_DAY_UNFOCUSED);
        }
        else
        {
            zoomInButton.setIcon(
                ImageDecorator.IMG_MAP_ZOOM_IN_ICON_FOCUSED.getImage(),
                ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_UNFOCUSED.getImage(),
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED);
        }
    	zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.MAP_ZOOM_TOP_BG_FOCUSED);
    	zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((TurnMapUiDecorator) uiDecorator).TURN_MAP_ZOOM_ICON_WIDTH);
        zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((TurnMapUiDecorator) uiDecorator).TURN_MAP_ZOOM_ICON_HEIGHT);   
        int shadowHeight = 6;
        zoomInButton.setPadding(0, shadowHeight, 0, 0);

        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_ZOOM_IN);
        zoomInButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        zoomInButton.setCommandEventListener(this);
        
        FrogButton zoomOutButton = new FrogButton(ID_TURN_MAP_ZOOM_OUT_COMPONENT, "");
        zoomOutButton.setPadding(0, 0, 0, 0);
        if (isDayColor)
        {
            zoomOutButton.setIcon(
                ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage(),
                ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_UNFOCUSED.getImage(),
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            
            zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED);
        }
        else
        {
            zoomOutButton.setIcon(
                ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage(),
                ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_UNFOCUSED.getImage(),
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED);
        }   
        
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
            NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_FOCUSED);
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((TurnMapUiDecorator) uiDecorator).TURN_MAP_ZOOM_ICON_WIDTH);
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((TurnMapUiDecorator) uiDecorator).TURN_MAP_ZOOM_ICON_HEIGHT);        

        zoomOutButton.setPadding(0, 0, 0, shadowHeight);

        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_ZOOM_OUT);
        zoomOutButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        zoomOutButton.setCommandEventListener(this);
        
        TnLinearContainer zoomContainer = UiFactory.getInstance()
        .createLinearContainer(ID_TURN_MAP_ZOOM_CONTAINER, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        zoomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((TurnMapUiDecorator) uiDecorator).TURN_MAP_ZOOM_IN_X);
        zoomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((TurnMapUiDecorator) uiDecorator).TURN_MAP_ZOOM_IN_Y);
        zoomContainer.add(zoomInButton);
        zoomContainer.add(zoomOutButton);
        mapContainer.addFeature(zoomContainer);

        FrogButton fitMap = UiFactory.getInstance().createButton(ID_TURN_MAP_FIT_MAP_COMPONENT, "");
        if (isDayColor)
        {
            fitMap.setIcon(ImageDecorator.IMG_MAP_FIT_ICON_FOCUSED.getImage(),
                ImageDecorator.IMG_MAP_FIT_ICON_UNFOCUSED.getImage(),
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            fitMap.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.MAP_BUTTON_UNFOCUSED);
       }
        else
        {
            fitMap.setIcon(ImageDecorator.IMG_MAP_FIT_ICON_FOCUSED.getImage(),
                ImageDecorator.IMG_MAP_FIT_ICON_NIGHT_UNFOCUSED.getImage(),
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            fitMap.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.MAP_BUTTON_NIGHT_UNFOCUSED);
        }
        fitMap.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MAP_BUTTON_FOCUSED);
        fitMap.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((TurnMapUiDecorator) uiDecorator).TURN_MAP_ZOOM_IN_X);
        
        fitMap.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                boolean isStaticRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
                int baseY = isStaticRoute ? uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR.getInt() : uiDecorator.MAP_LOGO_Y.getInt();
                return PrimitiveTypeCache.valueOf(baseY - ((TurnMapUiDecorator) uiDecorator).TURN_MAP_FIT_MAP_SIZE.getInt() - uiDecorator.MAP_BUTTON_BOTTOM_GAP.getInt());
            }
        }));
        fitMap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TurnMapUiDecorator) uiDecorator).TURN_MAP_FIT_MAP_SIZE);
        fitMap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TurnMapUiDecorator) uiDecorator).TURN_MAP_FIT_MAP_SIZE);
        fitMap.setPadding(0, 0, 0, 0);

        mapContainer.addFeature(fitMap);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_FIT_MAP);
        fitMap.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        fitMap.setCommandEventListener(this);
        
        float zoomLevelF = mapContainer.getZoomLevel();
        int zoomLevel = (int) (zoomLevelF + 0.5);
        updateZoomButtonStatus(zoomLevel);
    }
    
    /**
     * @author jpwang 
     * @see com.telenav.mvc.AbstractCommonMapView#handleZoomOut(com.telenav.ui.citizen.map.MapContainer)
     */
    public void handleZoomOut()
    {
        super.handleZoomOut(); 
        model.put(KEY_B_FROM_GETTING_EXTRA, true);
        TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, MapContainer.getInstance());
        tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_REQUEST_EXTRA_ROUTE));
        handleUiEvent(tnUiEvent);
    }
    
    /**
     * @author jpwang
     * @see com.telenav.mvc.AbstractCommonMapView#handleTouchEventOnMap(com.telenav.ui.citizen.map.MapContainer, com.telenav.tnui.core.TnUiEvent)
     */
    public void handleTouchEventOnMap(MapContainer container, TnUiEvent uiEvent)
    {
        super.handleTouchEventOnMap(container, uiEvent);
        model.put(KEY_B_FROM_GETTING_EXTRA, true);
        TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, container);
        tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_REQUEST_EXTRA_ROUTE));
        handleUiEvent(tnUiEvent);
    }
    
    protected AbstractTnComponent createMapContainer(CitizenScreen screen)
    {
    	MapContainer mapContainer = UiFactory.getInstance().getCleanMapContainer(screen, ID_MAP_CONTAINER);
    	MapContainer.getInstance().enableGPSCoarse(true);
        mapContainer.addFeature(createTurnMapInfoComponent());
        mapContainer.addFeature(createTurnIndexComponent());
        addControlButtons(mapContainer);
        boolean isStaticRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
        AbstractTnComponent mapCompanyLogo = isStaticRoute ? createMapCompanyLogo() : createMapCompanyLogo(this.uiDecorator.MAP_LOGO_Y);
        AbstractTnComponent mapProviderLabel = isStaticRoute ? createMapProvider() : createMapProvider(this.uiDecorator.MAP_LOGO_Y);

        mapContainer.addFeature(mapCompanyLogo);
        mapContainer.addFeature(mapProviderLabel);

        mapContainer.setMapUIEventListener(this);
        updateMapContainerEvent(mapContainer, IMapUIEventListener.EVENT_CREATED);
        
        if(isStaticRoute)
        {
            AbstractTnContainer bottomContainer = createBottomContainer(null, createBottomBarArgs());
            bottomContainer.setId(ID_TURN_MAP_BOTTOM_CONTAINER);
            mapContainer.addFeature(bottomContainer);
        }
        updateTurnMapInfo(screen);
        oldOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();        
        return mapContainer;
    }
    
    protected void updateTurnMapInfo(TnScreen screen)
    {
        TurnMapWrapper wrapper = ((TurnMapModel)model).getTurnMapWrapper();
        if(wrapper != null)
        {
            MapContainer mapContainer = MapContainer.getInstance();
            if(mapContainer != null)
            {
                TurnMapInfoComponent infoComponent = (TurnMapInfoComponent)mapContainer.getFeature(ID_TURN_MAP_INFO_COMPONENT);

                if(infoComponent != null)
                {
                    infoComponent.updateValues(wrapper.getRouteItemSize(), wrapper.getCurrentSegmentIndex(), MovingMapHelper.getTurnDescription(wrapper.getTurnIndex(), textIdForType), getDistToTurnString(wrapper.getDistance()), getStreetName(), wrapper.getDistance());
                }
                
                TurnMapIndexComponent turnIndexComponent = (TurnMapIndexComponent)mapContainer.getFeature(ID_TURN_MAP_TURN_INDEX_COMPONENT);
                if(turnIndexComponent != null)
                {
                    turnIndexComponent.update(getTurnIndexString());
                }
            }
        }
    }
    
    protected String getStreetName()
    {
        StringBuffer sb = new StringBuffer();
        TurnMapWrapper wrapper = ((TurnMapModel)model).getTurnMapWrapper();
        String streetName = "";
        if (wrapper != null)
        {
            streetName = wrapper.getStreetName();
        }
        sb.append(FrogTextHelper.BOLD_START).append(streetName).append(FrogTextHelper.BOLD_END);
        return sb.toString();
    }
    
    protected String getDistToTurnString(int distance)
    {
        StringBuffer sb = new StringBuffer();
        PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        String go = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_GO, IStringCommon.FAMILY_COMMON);
        String distToTurn = ResourceManager.getInstance().getStringConverter().convertDistanceMeterToMile(distance, systemUnits);
        sb.append(go).append(" ").append(FrogTextHelper.BOLD_START).append(distToTurn).append(FrogTextHelper.BOLD_END);
        return sb.toString();
    }
    
    public String getStopDisplayLabel(Stop stop)
    {
        if(stop==null)
        {
            return "";
        }
        return ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
    }
    
    protected String getTurnIndexString()
    {
        StringBuffer sb = new StringBuffer();
        TurnMapWrapper wrapper = ((TurnMapModel)model).getTurnMapWrapper();
        if(wrapper != null)
        {
            String of = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_OF, IStringNav.FAMILY_NAV);
            int currentIndex = wrapper.getCurrentSegmentIndex() + 1;
            int totalSize = wrapper.getRouteItemSize();
            sb.append(currentIndex).append(" ").append(of).append(" ").append(totalSize);
        }
        return sb.toString();
    }
    
    boolean isFirstTime = true;
    
    protected void updateMapComponent()
    {
        final MapContainer mapContainer = MapContainer.getInstance();
        if (mapContainer != null)
        {
            TurnMapModel turnMapModel = (TurnMapModel) model;
            TurnMapWrapper wrapper = turnMapModel.getTurnMapWrapper();
            Route route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
            int currentSegIndex = wrapper.getCurrentSegmentIndex();
            int lat = -1;
            int lon = -1;
            if (currentSegIndex < route.segmentsSize())
            {
                Segment seg = route.segmentAt(currentSegIndex);
                if (seg.isEdgeResolved())
                {
                    lat = seg.getTurnLocation().getLatitude();
                    lon = seg.getTurnLocation().getLongitude();
                }
            }
            else
            {
                Segment lastSeg = route.segmentAt(currentSegIndex - 1);
                if (lastSeg.isEdgeResolved())
                {
                    lat = lastSeg.getTurnLocation().getLatitude();
                    lon = lastSeg.getTurnLocation().getLongitude();
                }
            }
            boolean isStatic = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
            if (isStatic && isFirstTime)
            {
                isFirstTime = false;
            }
            mapContainer.setRenderingMode(NavSdkMapProxy.RENDERING_MODE_2D_NORTH_UP);
            mapContainer.setZoomLevel((int) TURN_MAP_DEFAULT_ZOOM);
            updateZoomButtonStatus((int) TURN_MAP_DEFAULT_ZOOM);
            mapContainer.disableAllTurnArrows("" + NavSdkRouteWrapper.getInstance().getCurrentRouteId());
            updateZoomButtonStatus((int) TURN_MAP_DEFAULT_ZOOM);
            if (currentSegIndex > 0)
            {
                mapContainer.enableTurnArrows("" + NavSdkRouteWrapper.getInstance().getCurrentRouteId(), currentSegIndex,
                    currentSegIndex + 1);
            }
            if (lat != -1 && lon != -1)
            {
                double latitude = lat / 100000.0d;
                double longitude = lon / 100000.0d;
                mapContainer.lookAt(latitude, longitude);
                currentSegLat = latitude;
                currentSegLon = longitude;
                currentSegZoom = TURN_MAP_DEFAULT_ZOOM;
            }
        }
    }
    
    protected void updateRoute()
    {
        MapContainer mapContainer = MapContainer.getInstance();
        if (mapContainer != null)
        {
            Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
            if (currentRoute == null)
            {
                return;
            }
            TurnMapWrapper turnMapWrapper = ((TurnMapModel) model).getTurnMapWrapper();
            int currentSegIndex = turnMapWrapper.getCurrentSegmentIndex();
            if (currentSegIndex == currentRoute.segmentsSize())
            {
                currentSegIndex--;
            }
            RouteUiHelper.updateCurrentRoute(mapContainer, currentSegIndex);
        }
    }
    
    protected void updateTurnArrow()
    {
        MapContainer mapContainer = MapContainer.getInstance();
        if (mapContainer != null)
        {
            Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
            if (currentRoute == null)
            {
                return;
            }
            TurnMapWrapper turnMapWrapper = ((TurnMapModel) model).getTurnMapWrapper();
            int currentSegIndex = turnMapWrapper.getCurrentSegmentIndex();
            if (currentSegIndex == currentRoute.segmentsSize())
            {
                currentSegIndex--;
            }
            RouteUiHelper.showTurnArrow(currentRoute.getRouteID(), currentSegIndex, mapContainer);
        }
    }
    
    private void removeFlags()
    {
        if (origFlagAnnotation != null)
        {
            MapContainer.getInstance().removeFeature(origFlagAnnotation);
            origFlagAnnotation = null;
        }

        if (destFlagAnnotation != null)
        {
            MapContainer.getInstance().removeFeature(destFlagAnnotation);
            destFlagAnnotation = null;
        }
    }
    
    protected void addFlags()
    {
     // Add start flag here.
        AbstractTnImage startFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
//        Route currentRoute = RouteWrapper.getInstance().getCurrentRoute();
        Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        Segment[] segments = null;
        if(currentRoute != null)
        {
            segments = currentRoute.getSegments();
        }
        if (startFlag != null)
        {
            boolean isFirstSegmentReady = false;
            
            if(segments != null && segments.length > 0)
            {
                isFirstSegmentReady = segments[0].isEdgeResolved();
            }
            
            int startLat = 0;
            int startLon = 0;
            
            if(isFirstSegmentReady)
            {
                startLat = currentRoute.getOriginLatLon()[Route.WAYPOINTS_LAT_INDEX];
                startLon = currentRoute.getOriginLatLon()[Route.WAYPOINTS_LON_INDEX];
            }
            else
            {
                Address originAddress = (Address)model.get(KEY_O_ADDRESS_ORI);
                if (originAddress != null && originAddress.getStop() != null)
                {
                    Stop originStop = originAddress.getStop();
                    if(originStop != null)
                    {
                        startLat = originStop.getLat();
                        startLon = originStop.getLon();
                    }
                }
            }
            
            if (startLat != 0 && startLon != 0)
            {
                double latD = startLat / 100000.0d;
                double lonD = startLon / 100000.0d;

                if (startFlag != null)
                {
                    float pivotX = 0.5f;
                    float pivotY = 0;
                    origFlagAnnotation = new ImageAnnotation(startFlag, latD, lonD, pivotX, pivotY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                    MapContainer.getInstance().addFeature(origFlagAnnotation);
                }
            }        
        }

        // Add end flag here.
        AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();
        if (destFlag != null)
        {
            boolean isLastSegmentReady = false;
            Segment lastSeg = null;
            
            if(segments != null && segments.length > 0)
            {
                lastSeg = segments[segments.length - 1];
                isLastSegmentReady = lastSeg.isEdgeResolved();
            }
            
            int destLat = 0;
            int destLon = 0;
            
            if(isLastSegmentReady)
            {
                destLat = currentRoute.getDestLatLon()[Route.WAYPOINTS_LAT_INDEX];
                destLon = currentRoute.getDestLatLon()[Route.WAYPOINTS_LON_INDEX];
            }
            else
            {
                Address destAddress = (Address)model.get(KEY_O_ADDRESS_DEST);
                if (destAddress != null && destAddress.getStop() != null)
                {
                    Stop destStop = destAddress.getStop();
                    if(destStop != null)
                    {
                        destLat = destStop.getLat();
                        destLon = destStop.getLon();
                    }
                }
            }
            
            if (destLat != 0 && destLon != 0)
            {
                double latD = destLat / 100000.0d;
                double lonD = destLon / 100000.0d;

                if (destFlag != null)
                {
                    float pivotX = 0.5f;
                    float pivotY = 0;
                    destFlagAnnotation = new ImageAnnotation(destFlag, latD, lonD, pivotX, pivotY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                    MapContainer.getInstance().addFeature(destFlagAnnotation);
                }
            }
        }
    }
    
    protected void updateFlags()
    {
        removeFlags();
        addFlags();
    }
    
    protected TnPopupContainer createGettingRouteProgressBox(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createProgressBox(state, ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_GETTING_TURN_MAPS, IStringNav.FAMILY_NAV));
        return popup;
    }
    
    protected TnPopupContainer createLoadingProgressBox(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createProgressBox(state, ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_LABEL_LOADING, IStringCommon.FAMILY_COMMON));
        return popup;
    }
    
    private BottomContainerArgs createBottomBarArgs()
    {
        BottomContainerArgs args = new BottomContainerArgs(CMD_NONE);
        
        args.cmdIds = new int[5];
        args.cmdIds[0] = CMD_NONE;
        args.cmdIds[1] = CMD_ROUTE_SUMMARY;
        args.cmdIds[2] = CMD_COMMON_DSR;
        args.cmdIds[3] = CMD_COMMON_LINK_TO_SEARCH;
        args.cmdIds[4] = CMD_COMMON_END_TRIP;
        
        args.displayStrResIds = new int[5];
        args.displayStrResIds[0] = IStringCommon.RES_DIRECTIONS;
        args.displayStrResIds[1] = IStringCommon.RES_ROUTE;
        //args.displayStrResIds[2] = IStringCommon.RES_BTTN_DSR; For dsr btn, there's no string
        args.displayStrResIds[3] = IStringCommon.RES_NEARBY;
        args.displayStrResIds[4] = IStringCommon.RES_BTTN_EXIT;
        
        args.unfocusImageAdapters = new TnUiArgAdapter[5];
        args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_ICON_DIRECTIONS_UNFOCUSED;
        args.unfocusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_UNFOCUS;
        args.unfocusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.unfocusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_UNFOCUS;
        args.unfocusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_UNFOCUS;
        
        args.focusImageAdapters = new TnUiArgAdapter[5];
        args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_ICON_DIRECTIONS_FOCUSED;
        args.focusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_FOCUS;
        args.focusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.focusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_FOCUS;
        args.focusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_FOCUS;
        
        args.disableImageAdapters = new TnUiArgAdapter[5];
        args.disableImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_DISABLE;

        return args;
    }
    
    protected AbstractTnContainer createBottomContainer(final AbstractTnContainer titleContainer, final BottomContainerArgs bottomBarArgs)
    {
        AbstractTnContainer container = super.createBottomContainer(titleContainer, bottomBarArgs);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);
        return container;
    }
    
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmdId = CMD_NONE;
        if(state == STATE_TURN_MAP)
        {
            if(tnUiEvent.getKeyEvent() != null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN && tnUiEvent.getKeyEvent().getCode()== TnKeyEvent.KEYCODE_BACK)
            {
                boolean isStatic = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
                if(isStatic)
                {
                    cmdId = CMD_COMMON_EXIT;
                }
                else
                {
                    cmdId = CMD_COMMON_BACK;
                }
            }
        }
        return cmdId;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch(state)
        {
            case STATE_TURN_MAP:
            {
                if(model.fetchBool(KEY_B_UPDATE_MAP_LAYER))
                {
                    if(isLastConnected != NetworkStatusManager.getInstance().isConnected())
                    {
                        initMapLayer();
                    }
                }
                else
                {
                    MapContainer mapContainer = MapContainer.getInstance();
                    TurnMapInfoComponent infoComponent = (TurnMapInfoComponent)mapContainer.getFeature(ID_TURN_MAP_INFO_COMPONENT);
                    if (infoComponent == null)
                    {
                        createMapContainer((CitizenScreen) screen);
                    }
                    else
                    {
                        if (model.fetchBool(KEY_B_RELOAD_ROUTE))
                        {
                            updateFlags();
                            updateRoute();
                        }
                        else if (model.fetchBool(KEY_B_UPDATE_TURN_ARROW))
                        {
                            updateTurnArrow();
                        }
                        updateTurnMapInfo(screen);
                        if(!model.fetchBool(KEY_B_FROM_GETTING_EXTRA))
                        {
                            updateMapComponent();
                            model.put(KEY_B_FROM_GETTING_EXTRA, false);
                        }
                    }
                }
                
                return true;
            }
        }
        return false;
    }

    public void updateMapContainerEvent(MapContainer container, int event)
    {
        if (event == IMapUIEventListener.EVENT_CREATED)
        {
            if (model.getState() == STATE_TURN_MAP)
            {
                initMapLayer();
                updateRoute();
                updateMapComponent();
                updateFlags();
            }
        }
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h,
            int oldw, int oldh)
    {
        switch (model.getState())
        {
            case STATE_TURN_MAP:
            {
                int orintation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                if (orintation == oldOrientation)
                    return;

                oldOrientation = orintation;
                
                if(orintation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    showBottomContainer();
                }
                else
                {
                    hideBottomContainer();
                }
                break;
            }
        }
    }

    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        if(type == TnUiEvent.TYPE_COMMAND_EVENT)
        {
            TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
            int commandId = commandEvent.getCommand();
            switch(commandId)
            {
                case CMD_ZOOM_IN:
                {
                    MapContainer mapContainer = MapContainer.getInstance();
                    if(mapContainer != null)
                    {
                        float zoomLevelF = MapContainer.getInstance().getZoomLevel();
                        int zoomLevel = (int) (zoomLevelF + 0.5);
                        updateZoomButtonStatus(zoomLevel - 1);
                        mapContainer.zoomInMap();
                    }
                    return true;
                }
                case CMD_ZOOM_OUT:
                {
                    MapContainer mapContainer = MapContainer.getInstance();
                    if(mapContainer != null)
                    {
                        float zoomLevelF = MapContainer.getInstance().getZoomLevel();
                        int zoomLevel = (int) (zoomLevelF + 0.5);
                        updateZoomButtonStatus(zoomLevel + 1);
                        mapContainer.zoomOutMap();
                        handleZoomOut();
                    }
                    return true;
                }
                case CMD_FIT_MAP:
                {
                    final MapContainer mapContainer = MapContainer.getInstance();
                    mapContainer.lookAt(currentSegLat, currentSegLon);
                    mapContainer.setZoomLevel((int) currentSegZoom);
                    updateZoomButtonStatus((int)currentSegZoom);
                    return true;
                }
            }
        }
        return false;
    }
    
    protected void updateZoomButtonStatus(int zoomLevel)
    {
        MapContainer mapContainer = MapContainer.getInstance();
        if(mapContainer != null)
        {
            TnLinearContainer zoomContainer = (TnLinearContainer)mapContainer.getFeature(ID_TURN_MAP_ZOOM_CONTAINER);
            if(zoomContainer != null)
            {
                boolean isDayColor = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? false : true;
                FrogButton zoomInButton = (FrogButton)zoomContainer.getComponentById(ID_ZOOM_IN);
                if(zoomInButton != null)
                {
                    if (zoomLevel <= MapConfig.MAP_MIN_ZOOM_LEVEL)
                    {
                        if (isDayColor)
                        {
                            zoomInButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_DISABLE.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_DISABLE.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);    
                            zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_TOP_BG_DAY_UNFOCUSED);
                        }
                        else
                        {
                            zoomInButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_DISABLE.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_DISABLE.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
                            zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED);
                        }
                    }
                    else
                    {
                        if (isDayColor)
                        {
                            zoomInButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_IN_ICON_FOCUSED.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_UNFOCUSED.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);    
                            zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_TOP_BG_DAY_UNFOCUSED);
                        }
                        else
                        {
                            zoomInButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_IN_ICON_FOCUSED.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_UNFOCUSED.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
                            zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED);
                        }
                    }
                }
                FrogButton zoomOutButton = (FrogButton)zoomContainer.getComponentById(ID_TURN_MAP_ZOOM_OUT_COMPONENT);
                if(zoomOutButton != null)
                {
                    if (zoomLevel >= MapConfig.MAP_MAX_ZOOM_LEVEL)
                    {
                        if (isDayColor)
                        {
                            zoomOutButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_DISABLE.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_DISABLE.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);    
                            zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED);
                        }
                        else
                        {
                            zoomOutButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_DISABLE.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_DISABLE.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
                            zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED);
                        }
                    }
                    else
                    {
                        if (isDayColor)
                        {
                            zoomOutButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_UNFOCUSED.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);    
                            zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED);
                        }
                        else
                        {
                            zoomOutButton.setIcon(
                                ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage(),
                                ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_UNFOCUSED.getImage(),
                                AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
                            zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                                NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED);
                        }
                    }
                }
            }
        }
    }
    
    public void onPinchEnd()
    {
        super.onPinchEnd();
        updateZoomButtonStatus((int) MapContainer.getInstance().getZoomLevel());
    }
    
    @Override
    protected void initMapLayer()
    {
        super.initMapLayer();
        
        isLastConnected = NetworkStatusManager.getInstance().isConnected();
    }
}

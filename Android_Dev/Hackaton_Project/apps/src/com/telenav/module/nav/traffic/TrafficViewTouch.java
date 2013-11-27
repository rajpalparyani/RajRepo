/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficViewTouch.java
 *
 */
package com.telenav.module.nav.traffic;

import java.util.Vector;

import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.route.NavSdkRoute;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTrafficItem;
import com.telenav.ui.citizen.map.IMapUIEventListener;
import com.telenav.ui.citizen.map.ImageAnnotation;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.FrogScreen;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-10-26
 */
class TrafficViewTouch extends AbstractCommonMapView implements ITrafficConstants,IApplicationListener
{
    protected int[] speedColorArray = new int[] {TnColor.RED, UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_OR), TnColor.YELLOW, TnColor.GREEN};
    
    private ImageAnnotation origAnnotation;
    private ImageAnnotation destAnnotation;
    
    protected int oldOrientation = 0;
    
    private static final String ROUTE_NAME_CURRENT_ROUTE = "routeB";
    private static final String ROUTE_NAME_ALTERNATE_ROUTE = "trafficAlternateRoute";

    private boolean isLastTimeConnected = false;
    
    public TrafficViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_GET_SUMMARY:
            {
                popup = UiFactory.getInstance().createProgressBox(state, ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_GETTING_TRAFFIC, IStringNav.FAMILY_NAV));
                break;
            }
            case STATE_SEND_AVOID:
            case STATE_SEND_REROUTE:
            {
                TnUiArgAdapter xMapAdapter = new TnUiArgAdapter(0, new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(0);
                    }
                });
                TnUiArgAdapter yMapAdapter = new TnUiArgAdapter(0, new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(0);
                    }
                });
                MapContainer.getInstance().setMapRect(xMapAdapter, yMapAdapter, uiDecorator.SCREEN_WIDTH,
                    uiDecorator.SCREEN_HEIGHT);
                popup = UiFactory.getInstance().createProgressBox(state,
                    ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_GETTING_ROUTE, IStringNav.FAMILY_NAV));
                break;
            }
        }
        return popup;
    }

    @Override
    protected void activate()
    {
        TeleNavDelegate.getInstance().registerApplicationListener(this);
        super.activate();
    }

    @Override
    protected void deactivateDelegate()
    {
        TeleNavDelegate.getInstance().unregisterApplicationListener(this);
        super.deactivateDelegate();
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch (state)
        {
            case STATE_SHOW_SUMMARY:
            {
                screen = createTrafficSummary(state);
                break;
            }
            case STATE_SHOW_TRAFFIC_SEGMENT:
            {
                screen = createTrafficSegment(state);
                break;
            }
            case STATE_ALTERNATE_ROUTE:
            {
                screen = createAlternateRouteScreen(state);
                break;
            }
        }
        return screen;
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_SHOW_SUMMARY:
            {
                updateSummaryScreen((CitizenScreen) screen);
                break;
            }
            case STATE_ALTERNATE_ROUTE:
            {
                boolean isCurrentConnected = NetworkStatusManager.getInstance().isConnected();
                if(isLastTimeConnected != isCurrentConnected)
                {
                    initMapLayer();
                }
                createMapContainer((CitizenScreen) screen);
                break;
            }
            case STATE_SHOW_TRAFFIC_SEGMENT:
            {
                AbstractTnContainer container = ((CitizenScreen) screen).getContentContainer();
                AbstractTnComponent avoidSegmentBtn = (AbstractTnComponent) container.getComponentById(ID_AVOID_BUTTON);
                if (avoidSegmentBtn instanceof FrogButton)
                {
                    updateBtn((FrogButton) avoidSegmentBtn);
                }
                break;
            }
        }
        return false;
    }
    
    private void updateSummaryScreen(CitizenScreen screen)
    {
        AbstractTnContainer container = ((CitizenScreen) screen).getContentContainer();
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        if (oldOrientation == orientation)
        {
            AbstractTnComponent minimizeAllDelayBtn = (AbstractTnComponent) container.getComponentById(ID_MINIMIZE_ALL_DELAY);
            if (minimizeAllDelayBtn instanceof FrogButton)
            {
                updateBtn((FrogButton) minimizeAllDelayBtn);
            }
        }
        else
        {
            AbstractTnContainer summaryContainer = (AbstractTnContainer) container
                    .getComponentById(ID_TRAFFIC_SUMMARY_MIDDLE_CONTAINER);
            if (summaryContainer != null)
            {
                AbstractTnContainer parent = (AbstractTnContainer) summaryContainer.getParent();
                if (parent != null)
                {
                    int index = parent.indexOf(summaryContainer);
                    parent.remove(summaryContainer);
                    parent.add(createMiddleContainer(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT), index);
                }
            }
            oldOrientation = orientation;
        }
    }

    protected FrogScreen createTrafficSummary(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        AbstractTnContainer container = screen.getContentContainer();   
        container.add(getTabContainer());
        
        oldOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        
        container.add(createMiddleContainer(oldOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT));
        
        
        addBottomContainer(screen, createBottomBarArgs());
        return screen;
    }
    
    protected AbstractTnContainer createMiddleContainer(boolean isPortrait)
    {
        TnLinearContainer middleContainer = null;
        if(isPortrait)
        {
            middleContainer = UiFactory.getInstance().createLinearContainer(ID_TRAFFIC_SUMMARY_MIDDLE_CONTAINER, true);
            middleContainer.add(getSummaryInfoContainer(isPortrait));
            middleContainer.add(createSummaryTitle(isPortrait));
            AbstractTnContainer summaryContainer = createSummaryContainer(isPortrait);
            TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, true);
            panel.set(summaryContainer);
            panel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).TRAFFIC_SUMMARY_CONTAINER_HEIGHT);
            panel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).SCREEN_WIDTH);
            panel.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
            middleContainer.add(panel);
        }
        else
        {
            middleContainer = UiFactory.getInstance().createLinearContainer(ID_TRAFFIC_SUMMARY_MIDDLE_CONTAINER, false);
            TnLinearContainer middleRightContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
            AbstractTnContainer summaryContainer = createSummaryContainer(isPortrait);
            TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, true);
            panel.set(summaryContainer);
            panel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).TRAFFIC_SUMMARY_CONTAINER_HEIGHT_LANDSCAPE);
            panel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).TRAFFIC_SUMMARY_CONTAINER_WIDTH);
            panel.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
            middleRightContainer.add(createSummaryTitle(isPortrait));
            middleRightContainer.add(panel);
            middleContainer.add(getSummaryInfoContainer(isPortrait));
            middleContainer.add(middleRightContainer);
        }
        
        return middleContainer;
    }
    
    protected AbstractTnContainer getTabContainer()
    {
        TnLinearContainer outerContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        int tabTextUnfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_UNFOCUSED);
        int tabTextFocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_FOCUSED);

        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).TAB_OUTER_CONTAINER_HEIGHT);
        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).SCREEN_WIDTH);
        
        outerContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TITLE_CONTAINER_COLOR));
        
        TnLinearContainer innerContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        
        String turns = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_TURNS, IStringNav.FAMILY_NAV);
        FrogButton turnButton = UiFactory.getInstance().createButton(ID_TAB_TURNS_BUTTON, turns);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_LEFT_BUTTON_UNFOCUSED);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_LEFT_BUTTON_UNFOCUSED);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        TnMenu turnMenu = new TnMenu();
        turnMenu.add("", CMD_ROUTE_SUMMARY);
        turnButton.setMenu(turnMenu, AbstractTnComponent.TYPE_CLICK);
        turnButton.setCommandEventListener(this);
        turnButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));
        turnButton.setForegroundColor(tabTextUnfocusedColor, tabTextFocusedColor);
        turnButton.setFocusable(true);
        
        String traffic = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_TRAFFIC, IStringNav.FAMILY_NAV);
        FrogButton trafficButton = UiFactory.getInstance().createButton(0, traffic);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_MIDDLE_BUTTON_FOCUSED);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_MIDDLE_BUTTON_FOCUSED);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        trafficButton.setForegroundColor(tabTextUnfocusedColor, tabTextFocusedColor);
        trafficButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));
        trafficButton.setFocusable(true);
        
        String map = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_MAP, IStringNav.FAMILY_NAV);
        FrogButton mapButton = UiFactory.getInstance().createButton(ID_TAB_MAP_BUTTON, map);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_RIGHT_BUTTON_UNFOCUSED);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_RIGHT_BUTTON_UNFOCUSED);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        TnMenu mapMenu = new TnMenu();
        mapMenu.add("", CMD_MAP_SUMMARY);
        mapButton.setMenu(mapMenu, AbstractTnComponent.TYPE_CLICK);
        mapButton.setCommandEventListener(this);
        mapButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));
        mapButton.setForegroundColor(tabTextUnfocusedColor, tabTextFocusedColor);
        mapButton.setFocusable(true);
        
        innerContainer.add(turnButton);
        innerContainer.add(trafficButton);
        innerContainer.add(mapButton);
        
        outerContainer.add(innerContainer);
        return outerContainer;
    }
    
    protected FrogScreen createAlternateRouteScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);        
        createMapContainer(screen);
        return screen;

    }
    
    private AbstractTnContainer createSummaryTitle(boolean isVertical)
    {
        TnLinearContainer summaryTitle = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator) uiDecorator).SUMMARY_TITLE_HEIGHT);
        if(isVertical)
        {
            summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator) uiDecorator).SCREEN_WIDTH);
        }
        else
        {
            summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator) uiDecorator).SUMMARY_TITLE_WIDTH);
        }
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.STREET_TABLE_BG_UNFOCUSED);
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.STREET_TABLE_BG_UNFOCUSED);
        
        AbstractTnFont titleFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_SUMMARY_TITLE);
        int systemUnits = DaoManager.getInstance().getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        String trafficUnitTitle = "";
        if(systemUnits == Preference.UNIT_METRIC)
        {
            trafficUnitTitle = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_KPH, IStringNav.FAMILY_NAV);
        }
        else if(systemUnits == Preference.UNIT_USCUSTOM)
        {
            trafficUnitTitle = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_MPH, IStringNav.FAMILY_NAV);
        }
        
        FrogLabel trafficUnits = UiFactory.getInstance().createLabel(0, trafficUnitTitle);
        trafficUnits.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR));
        trafficUnits.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator) uiDecorator).TRAFFIC_ITEM_FIRST_PART_WIDTH);
        trafficUnits.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        trafficUnits.setFont(titleFont);
        
        FrogLabel street = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_STREET, IStringNav.FAMILY_NAV));
        street.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR));
        street.setStyle(AbstractTnGraphics.LEFT);
        street.setFont(titleFont);
        
        summaryTitle.add(trafficUnits);
        summaryTitle.add(street);
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
        if(!isVertical)
        {
            summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_LEFT_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_LEFT_UNFOCUSED);
        }
        return summaryTitle;
    }
    
    protected AbstractTnContainer createSummaryContainer(boolean isVertical)
    {
        TnLinearContainer summaryContainer = UiFactory.getInstance().createLinearContainer(ID_SUMMARY_CONTAINER, true, AbstractTnGraphics.RIGHT);
        summaryContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        TrafficSegment[] segments = (TrafficSegment[])model.get(KEY_A_SEGMENTS);
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        
        int greyColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
        
        int currentDistance = ((TrafficModel)model).getCurrentDistance();
        int trafficDistance = 0;
        boolean hasAddedCar = false;
        
        //static route don't need the car icon.
        if (!NavSdkNavEngine.getInstance().isRunning())
        {
            hasAddedCar = true;
        }
        
        for (int i = 0; segments != null && i < segments.length; i++)
        {
            TrafficSegment segment = segments[i];
            if(segment == null)
            {
                continue;
            }
            String length = converter.convertDistanceMeterToMile(segment.getLength(), systemUnits);
            String streetName = segment.getName();
            String speed = converter.convertSpeed(segment.getAvgSpeed(), systemUnits);
            trafficDistance += segment.getLength();
            
            //if the last segment without any traffic info, we should hide it
            if( segment.getAvgSpeed() == 0 &&  (i == segments.length -1 ) && segment.getLength() == 0)
            {
                continue;
            }
            boolean isNA = false;
            int speedColor = 0xFFFFFF;
            if (segment.getAvgSpeed() < 0)
            {
                isNA = true;
            }
            else
            {
                if( segment.getPostedSpeed() > 0 )
                {
                    int quarterSpeed = segment.getPostedSpeed() >> 2;
                    speedColor = speedColorArray[ Math.min(speedColorArray.length-1,(segment.getAvgSpeed()/quarterSpeed)) ];
                }
            }
            
            TrafficIncident[] incidents = segment.getIncidents();
            int incidentNum = incidents == null ? 0 : incidents.length;
            
            FrogButton triangleButton = null;
            if( incidentNum > 0 )
            {
                int severity = getSeverity(incidents);
                int incidentType = getIncidentType(incidents);
                triangleButton = getIncidentIcon(severity, incidentType, incidentNum);
            }
            
            CitizenTrafficItem item = UiFactory.getInstance().createCitizenTrafficItem(ID_TRAFFIC_ITEM_BASE + i, length, streetName, speed, speedColor, isNA, ((TrafficUiDecorator)uiDecorator).TRAFFIC_ITEM_ICON_SIZE, ((TrafficUiDecorator)uiDecorator).TRAFFIC_ITEM_FIRST_PART_WIDTH, ((TrafficUiDecorator)uiDecorator).TRAFFIC_ITEM_FIRST_PART_LEFT_PADDING, ((TrafficUiDecorator)uiDecorator).TRAFFIC_ITEM_SECOND_PART_PADDING, triangleButton);
            item.setForegroundColor(greyColor, greyColor);
            item.setFocusable(true);
            item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).TRAFFIC_ITEM_HEIGHT);
            if(isVertical)
            {
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).SCREEN_WIDTH);
            }
            else
            {
                item.getTnUiArgs().put(TnUiArgs.KEY_LEFT_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_LEFT_UNFOCUSED);
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).TRAFFIC_SUMMARY_CONTAINER_WIDTH);
            }
            
            if (!hasAddedCar)
            {
                if (trafficDistance >= currentDistance || i == segments.length - 1)
                {
                    item.setIsShowCar(true);
                    hasAddedCar = true;
                }
            }
            
            item.setCommand(CMD_PLAY_AUDIO, CMD_VIEW_DETAIL);
            item.setCommandEventListener(this);
            summaryContainer.add(item);
        }
        
        return summaryContainer;
    }
    
    private int getSeverity( TrafficIncident[] incidents )
    {
        // The larger the severity number, the less severity the incident.
        int severity = 99;

        if( incidents != null )
        {
            for( int i = 0; i < incidents.length; i++ )
            {
                if(incidents[i] == null)
                    continue;
                severity = Math.min( severity, incidents[i].getSeverity() );
            }
        }
        return severity;
    }
    
    private int getIncidentType( TrafficIncident[] incidents )
    {
        // The larger the severity number, the less severity the incident.
        int incidentType = 99;

        if( incidents != null )
        {
            for( int i = 0; i < incidents.length; i++ )
            {
                if(incidents[i] == null)
                    continue;
                incidentType = Math.min( incidentType, incidents[i].getIncidentType() );
            }
        }
        return incidentType;
    }
    
    protected FrogButton getIncidentIcon( int severity, int incidentType, int incidentNum)
    {
        if (severity <= 0)
        {
            severity = 1;
        }
        
        TnUiArgAdapter image = null;
        
        if(incidentType == TrafficIncident.TYPE_CAMERA)
        {
            return null;
        }
        int foregroundColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        switch(severity)
        {
            case TrafficIncident.SEVERITY_SEVERE:
            {
                image = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_RED_EMPTY_ICON_UNFOCUSED;
                foregroundColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
                break;
            }
            case TrafficIncident.SEVERITY_MAJOR:
            {
                image = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_ORANGE_EMPTY_ICON_UNFOCUSED;
                break;
            }
            case TrafficIncident.SEVERITY_MINOR:
            {
                image = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_YELLOW_EMPTY_ICON_UNFOCUSED;
                break;
            }
        }

        FrogButton incidentButton = null;
        if(image != null)
        {
            AbstractTnImage iconImage = image.getImage();
            if(iconImage != null)
            {
                incidentButton = UiFactory.getInstance().createButton(0, "");
                incidentButton.setText("" + incidentNum);
                incidentButton.setForegroundColor(foregroundColor, foregroundColor);
                incidentButton.setFocusable(false);
                incidentButton.setIcon(iconImage, iconImage, AbstractTnGraphics.LEFT);
            }
        }
        
        return incidentButton;
    }
    
    protected AbstractTnComponent createMapContainer(CitizenScreen screen)
    {
        if (!MapContainer.getInstance().hasCleanAll())
        {
            return MapContainer.getInstance();
        }
        initMapLayer();
        Route[] routes = (Route[]) model.get(KEY_A_ROUTE_CHOICES);

        int[] etas = (int[]) model.get(KEY_A_ROUTE_CHOICES_ETA);

        int[] lengths = (int[]) model.get(KEY_A_ROUTE_CHOICES_LENGTH);

        int newEta;
        int deltaDistance;

//        if (RouteWrapper.getInstance().getCurrentRouteId() == routes[0].getRouteID())
        if (NavSdkRouteWrapper.getInstance().getCurrentRouteId() == routes[0].getRouteID())
        {
            // the 1st route is current route
            newEta = etas[1];
            deltaDistance = lengths[1] - lengths[0];
            model.put(KEY_I_NEW_ROUTE_ID, routes[1].getRouteID());
        }
        else
        {
            // the 2nd route is current route
            newEta = etas[0];
            deltaDistance = lengths[0] - lengths[1];
            model.put(KEY_I_NEW_ROUTE_ID, routes[0].getRouteID());
        }

        MapContainer mapContainer = UiFactory.getInstance().getCleanMapContainer(screen, ID_MAP_CONTAINER);

        TrafficAlternateRouteTitile title = new TrafficAlternateRouteTitile(ID_ALTERNATE_ROUTE_TITLE, (Address) model
                .get(KEY_O_ADDRESS_DEST), newEta, deltaDistance);
        mapContainer.addFeature(title);
        mapContainer.addFeature(createMapCompanyLogo(((TrafficUiDecorator) uiDecorator).MAP_LOGO_Y_WITH_BOTTOM_CONTAINER));
        mapContainer.addFeature(createMapProvider(((TrafficUiDecorator) uiDecorator).MAP_LOGO_Y_WITH_BOTTOM_CONTAINER));

        TnLinearContainer bottomContainer = UiFactory.getInstance().createLinearContainer(ID_BUTTON_CONTAINER, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        bottomContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.NAV_ALTERNATE_ROUTE_BOTTOM_COLOR));
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((TrafficUiDecorator) this.uiDecorator).BOTTOM_CONTAINER_Y);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        
        FrogButton acceptButton = UiFactory.getInstance().createButton(ID_ACCEPT_BUTTON,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ACCEPT_NEW_ROUTE, IStringNav.FAMILY_NAV));
        acceptButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)this.uiDecorator).BUTTON_WIDTH);

        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_ACCEPT_REROUTE);
        acceptButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        acceptButton.setCommandEventListener(this);
        acceptButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BOTTOM_BUTTON_SMALL));
        acceptButton.setIcon(ImageDecorator.ROUTE_NEW_ICON_UNFOCUSED.getImage(), ImageDecorator.ROUTE_NEW_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.HCENTER);
        acceptButton.setGap(ImageDecorator.ROUTE_NEW_ICON_UNFOCUSED.getImage().getWidth() / 2);
        bottomContainer.add(acceptButton);
        
        FrogNullField nullFieldGap = UiFactory.getInstance().createNullField(0);
        nullFieldGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)this.uiDecorator).NULL_FIELD_WIDTH);
        nullFieldGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)this.uiDecorator).BOTTOM_CONTAINER_HEIGHT);
        bottomContainer.add(nullFieldGap);

        FrogButton cancelButton = UiFactory.getInstance().createButton(ID_CANCEL_BUTTON,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_USE_CURRENT_ROUTE, IStringNav.FAMILY_NAV));
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)this.uiDecorator).BUTTON_WIDTH);

        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_BACK);
        cancelButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        cancelButton.setCommandEventListener(this);
        cancelButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BOTTOM_BUTTON_SMALL));
        cancelButton.setIcon(ImageDecorator.ROUTE_CURRENT_ICON_UNFOCUSED.getImage(), ImageDecorator.ROUTE_CURRENT_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.HCENTER);
        cancelButton.setGap(ImageDecorator.ROUTE_NEW_ICON_UNFOCUSED.getImage().getWidth() / 2);
        bottomContainer.add(cancelButton);
        mapContainer.addFeature(bottomContainer);

        this.updateMapContainerEvent(mapContainer, IMapUIEventListener.EVENT_CREATED);
        return mapContainer;
    }
    
    protected void initMapLayer()
    {
        boolean isConnected = NetworkStatusManager.getInstance().isConnected();
        
        int layerSetting = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_MAP_LAYER_SETTING);
        
        if(!isConnected)
        {
            layerSetting = 0;
        }
        
        if (layerSetting < 0)
        {
            layerSetting = 0;
        }

        boolean isShowSatelliteOverlay = ((layerSetting & 0x02) != 0);
        if (isShowSatelliteOverlay)
        {
            layerSetting = layerSetting ^ 0x02;
        }

        MapContainer.getInstance().showMapLayer(layerSetting);
        MapContainer.getInstance().changeToSpriteVehicleAnnotation();
        
        isLastTimeConnected = isConnected;
    }
    
    private String getTafficSegInfo(TrafficSegment seg)
    {
        int segLength = seg.getLength();
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        String segDist = converter.convertDistanceMeterToMile(segLength, systemUnits);
        return segDist;
    }
    
    protected AbstractTnImage getIncidentImage(int incidentType)
    {
        TnUiArgAdapter imageID = ImageDecorator.IMG_INCIDENT_DEFAULT;
        switch (incidentType)
        {
            case TrafficIncident.TYPE_ACCIDENT:
            {
                imageID = ImageDecorator.IMG_INCIDENT_ACCIDENT;
                break;
            }
            case TrafficIncident.TYPE_CONGESTION:
            {
                imageID = ImageDecorator.IMG_INCIDENT_CONGESTION;
                break;
            }
            case TrafficIncident.TYPE_CONSTRUCTION:
            {
                imageID = ImageDecorator.IMG_INCIDENT_CONSTRUCTION;
                break;
            }
            case TrafficIncident.TYPE_SPEED_TRAP:
            {
                imageID = ImageDecorator.IMG_SPEED_TRAP_ICON_UNFOCUSED;
                break;
            }
            case TrafficIncident.TYPE_CAMERA:
            {
                imageID = ImageDecorator.IMG_SPEED_CAMERA_ICON_UNFOCUSED;
                break;
            }
        }
        return imageID.getImage();
    }
    
    protected FrogScreen createTrafficSegment(int state)
    {
        boolean canAvoidSegment = this.model.getBool(KEY_B_CAN_AVOID_SEGMENT);
        TrafficSegment segment = (TrafficSegment) model.get(KEY_O_SEGMENT);
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        AbstractTnContainer incidentContainer = UiFactory.getInstance().createLinearContainer(0, true);
        incidentContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        TnScrollPanel scrollPanel = UiFactory.getInstance().createScrollPanel(0, true);
        scrollPanel.set(incidentContainer);
        
        //TODO: we could have common title later
        AbstractTnContainer titleContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_SUBTITLE_BAR));
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        int titlePadding = ((TrafficUiDecorator) uiDecorator).TRAFFIC_TITLE_GAP.getInt();
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, segment.getName());
        titleLabel.setPadding(0, titlePadding, 0, 0);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_INCIDENT_BOLD));
        titleContainer.add(titleLabel);
        
        String infoStr = getTafficSegInfo(segment);
        FrogLabel updateLabel = UiFactory.getInstance().createLabel(0, infoStr);
        updateLabel.setPadding(0, titlePadding / 2, 0, titlePadding);
        updateLabel.setForegroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_DA_GR));
        updateLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_UPDATE_TITLE));
        titleContainer.add(updateLabel);
        
        screen.getContentContainer().add(titleContainer);
        screen.getContentContainer().add(scrollPanel);
        
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator) uiDecorator).TRAFFIC_INFO_GAP);
        incidentContainer.add(nullField);
        
        if (segment.getAvgSpeed() >= 0 && segment.getPostedSpeed() > 0)
        {
            TrafficSpeedBar speedBar = new TrafficSpeedBar(0, segment);
            speedBar.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
            incidentContainer.add(speedBar);
        }

        nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator) uiDecorator).TRAFFIC_INFO_GAP);
        incidentContainer.add(nullField);
        
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        
        if (segment.getIncidents() != null && segment.getIncidents().length > 0)
        {
            for (int i = 0; i < segment.getIncidents().length; i++)
            {
                if(segment.getIncidents()[i] == null)
                    continue;
                TrafficIncident incident = segment.getIncidents()[i];
                String xStreet = incident.getCrossSt();
                
                FrogImageComponent alertImageComponent = UiFactory.getInstance().createFrogImageComponent(0,
                    getIncidentImage(incident.getIncidentType()));                

                StringBuffer msgSb = new StringBuffer();
                if (xStreet != null && xStreet.length() > 0)
                {
                    // [SX] fix for bug# 4059
                    // Insert "Near" before cross street [refer to Addendum7.4.2]
                    
                    String xStreetInfo = converter.convert(ResourceManager.getInstance().getCurrentBundle().getString(
                        IStringNav.RES_XSTREET_INFO, IStringNav.FAMILY_NAV), new String[]
                    { xStreet });
                    msgSb.append(xStreetInfo + " ");
                }
                if (incident.getMsg() != null && incident.getMsg().length() > 0)
                {
                    msgSb.append(incident.getMsg() + " ");
                }
                if (incident.getLaneClosed() > 0)
                {
                    String tmpFormat = null;

                    if (incident.getLaneClosed() == 1)
                    {
                        tmpFormat = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_LANE_CLOSE,
                            IStringNav.FAMILY_NAV);
                    }
                    else if (incident.getLaneClosed() > 99)
                    {
                        tmpFormat = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROAD_CLOSE,
                            IStringNav.FAMILY_NAV);
                    }
                    else
                    {
                        tmpFormat = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_LANES_CLOSE,
                            IStringNav.FAMILY_NAV);
                    }
                    String laneStr = converter.convert(tmpFormat, new String[]
                    { "" + incident.getLaneClosed() });
                    msgSb.append(laneStr);
                }
                String message = msgSb.toString();
                
                FrogMultiLine incidentMsg = UiFactory.getInstance().createMultiLine(0, message);                
                incidentMsg.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator) uiDecorator).TRAFFIC_DETAIL_MULTILINE_WIDTH);
//                incidentMsg.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator) uiDecorator).TRAFFIC_DETAIL_MULTILINE_HEIGHT);
                incidentMsg.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
                int meGreyColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
                incidentMsg.setForegroundColor(meGreyColor);
                
                FrogNullField iconTextPadding = UiFactory.getInstance().createNullField(0);
                iconTextPadding.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).TRAFFIC_DETAIL_ICON_TEXT_PADDING);
                
                TnLinearContainer linearContainer = UiFactory.getInstance().createLinearContainer(0, false,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                linearContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
                linearContainer.add(alertImageComponent);
                linearContainer.add(iconTextPadding);
                linearContainer.add(incidentMsg);
                incidentContainer.add(linearContainer);
                
                FrogNullField incidentPadding = UiFactory.getInstance().createNullField(0);
                incidentPadding.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).INCIDENT_PADDING);
                incidentContainer.add(incidentPadding);
            }
        }
        else if (segment.getAvgSpeed() < 0)
        {
            FrogMultiLine incidentMsg = UiFactory.getInstance().createMultiLine(0, ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringNav.RES_TRAFFIC_INFO_NOT_AVAILABLE, IStringNav.FAMILY_NAV));
            incidentMsg.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator) uiDecorator).TRAFFIC_DETAIL_MULTILINE_WIDTH);            
            incidentMsg.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
            int meGreyColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
            incidentMsg.setForegroundColor(meGreyColor);
            incidentMsg.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
            
            TnLinearContainer linearContainer = UiFactory.getInstance().createLinearContainer(0, false,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            linearContainer.add(incidentMsg);
            linearContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
            incidentContainer.add(linearContainer);
            nullField = UiFactory.getInstance().createNullField(0);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator) uiDecorator).TRAFFIC_INFO_GAP);
            incidentContainer.add(nullField);
        }
          
        if (canAvoidSegment)
        {
            FrogButton button = UiFactory.getInstance().createButton(ID_AVOID_BUTTON,
                ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TRAFFIC_AVOID_SEGMENT, IStringNav.FAMILY_NAV));
            updateBtn(button);
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).AVOID_SEGMENT_BUTTON_WIDTH);
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", CMD_AVOID_SELECTED);
            button.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            button.setCommandEventListener(this);

            TnLinearContainer linearContainer = UiFactory.getInstance().createLinearContainer(0, false,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            linearContainer.add(button);
            linearContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
            incidentContainer.add(linearContainer);
            nullField = UiFactory.getInstance().createNullField(0);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator) uiDecorator).TRAFFIC_INFO_GAP);
            incidentContainer.add(nullField);
        }

        return screen;
    }

    private void updateBtn(FrogButton button)
    {
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        if (!isOnboard)
        {
            button.setEnabled(true);
            int trafficAvailableColor = UiStyleManager.getInstance().getColor(UiStyleManager.TRAFFIC_SUMMERY_BUTTON_ENABLED_COLOR);
            button.setForegroundColor(trafficAvailableColor,trafficAvailableColor);
        }
        else
        {
            button.setEnabled(false);
            int trafficDisabledColor = UiStyleManager.getInstance().getColor(UiStyleManager.TRAFFIC_SUMMERY_BUTTON_DISABLED_COLOR);
            button.setForegroundColor(trafficDisabledColor,trafficDisabledColor);
        }
    }

    private AbstractTnContainer getSummaryInfoContainer(boolean isVertical)
    {
        int totalIncidents = model.getInt(KEY_I_INCIDENT_NUMBER);
        int totalDelay = model.getInt(KEY_I_TOTAL_DELAY);
        
        final int infoHPadding = ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_H_PADDING.getInt();
        
        AbstractTnContainer infoContainer = null;
        int infoVPadding;
        if(isVertical)
        {
            infoContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            infoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).SCREEN_WIDTH);
            infoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_HEIGHT);
            infoVPadding = ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_V_PADDING.getInt();
            infoContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_SUMMARY_TIME_INFO));
        }
        else
        {
            infoContainer = UiFactory.getInstance().createLinearContainer(0, true);
            infoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_WIDTH);
            infoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_HEIGHT_LANDSCAPE);
            infoVPadding = ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_V_PADDING_LANDSCAPE.getInt();
            infoContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_SUMMARY_TIME_INFO));
        }
        infoContainer.setPadding(infoHPadding, infoVPadding, infoHPadding, infoVPadding);
        infoContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TITLE_CONTAINER_COLOR));
        AbstractTnContainer incidentContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.TOP | 
            AbstractTnGraphics.LEFT);  
        String NA = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TRAFFIC_NA, IStringNav.FAMILY_NAV);
        String totalIncidentString = (totalIncidents == 0 ? NA : ("" + totalIncidents));
        String incidents = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TRAFFIC_INCIDENT_PREFIX, IStringNav.FAMILY_NAV);
        String incidentText = FrogTextHelper.BOLD_START + incidents + FrogTextHelper.BOLD_END + totalIncidentString;
        final FrogLabel incidentNum = UiFactory.getInstance().createLabel(0, incidentText);
        incidentNum.setPadding(0, 0, 0, 0);
        incidentNum.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_INCIDENT));
        incidentNum.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_INCIDENT_BOLD));
        incidentNum.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_LABEL_HEIGHT);
        incidentNum.sublayout(0, 0);
        incidentNum.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUMMARY_TIME_INFO),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUMMARY_TIME_INFO));
        incidentContainer.add(incidentNum);
        
        String delayTimeString = ResourceManager.getInstance().getStringConverter().convertCostTime(totalDelay * 1000);
        String delayTimePrefix = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TRAFFIC_DELAY_TIME, IStringNav.FAMILY_NAV);
        String delayText = FrogTextHelper.BOLD_START + delayTimePrefix + FrogTextHelper.BOLD_END + delayTimeString;
        final FrogLabel incidentDelay = UiFactory.getInstance().createLabel(0, delayText);
        incidentDelay.setPadding(0, 0, 0, 0);
        incidentDelay.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_INCIDENT));
        incidentDelay.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_INCIDENT_BOLD));
        incidentDelay.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).SUMMARY_INFO_LABEL_HEIGHT);
        incidentDelay.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUMMARY_TIME_INFO),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUMMARY_TIME_INFO));
        incidentContainer.add(incidentDelay);
        
        TnLinearContainer minimizeDelayContainer = null;
        if(isVertical)
        {
            minimizeDelayContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.RIGHT | AbstractTnGraphics.VCENTER);
        }
        else
        {
            minimizeDelayContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
        
        final FrogButton minimizeDelayButton = UiFactory.getInstance().createButton(ID_MINIMIZE_ALL_DELAY,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_MINIMIZE_ALL_DELAYS, IStringNav.FAMILY_NAV));
        minimizeDelayButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_MINIMIZE_BUTTON));
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_MINIMIZE_ALL_DELAY);
        minimizeDelayButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        minimizeDelayButton.setCommandEventListener(this);
        minimizeDelayButton.getTnUiArgs().remove(TnUiArgs.KEY_PREFER_WIDTH);
        int hPadding = ((TrafficUiDecorator)uiDecorator).MINIMIZE_DELAY_BUTTON_HPADDING.getInt();
        minimizeDelayButton.setPadding(hPadding, 0, hPadding, 0);
        updateBtn((FrogButton) minimizeDelayButton);
        if(isVertical)
        {
            incidentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int infoWidth = incidentNum.getPreferredWidth() > incidentDelay.getPreferredWidth() ? incidentNum.getPreferredWidth()
                            : incidentDelay.getPreferredWidth();
                    return PrimitiveTypeCache.valueOf(infoWidth);
                }
            }));
            
            minimizeDelayContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int infoWidth = incidentNum.getPreferredWidth() > incidentDelay.getPreferredWidth() ? incidentNum.getPreferredWidth()
                            : incidentDelay.getPreferredWidth();
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - infoHPadding * 2 - infoWidth);
                }
            }));
//            infoContainer.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
        }
        else
        {
            incidentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TrafficUiDecorator)uiDecorator).INCIDENT_CONTAINER_HEIGHT);
            minimizeDelayContainer.setPadding(0, infoVPadding, 0, 0);
            infoContainer.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
        }
        
        minimizeDelayContainer.add(minimizeDelayButton);
        infoContainer.add(incidentContainer);
        infoContainer.add(minimizeDelayContainer);
        return infoContainer;
    }
    
    protected String[] titleFormat()
    {
        String[] title = new String[]
        { ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_MPH, IStringNav.FAMILY_NAV),
                ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_STREET, IStringNav.FAMILY_NAV) };
        return title;
    }

    private BottomContainerArgs createBottomBarArgs()
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
        args.cmdIds[1] = CMD_NONE;
        args.cmdIds[2] = CMD_COMMON_DSR;
       
        args.cmdIds[3] = CMD_COMMON_LINK_TO_SEARCH;
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
        args.focusImageAdapters[3] =  ImageDecorator.IMG_BOTTOM_BAR_PLACES_FOCUS;
      	boolean isOnboard = NavRunningStatusProvider.getInstance().isOnBoardRoute()
                || !NetworkStatusManager.getInstance().isConnected();  
	     args.enableIcons[3] = isOnboard ? false : true;
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
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        return super.handleUiEvent(tnUiEvent);
    }

    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
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
            }
        }
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        switch (type)
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
                if (tnUiEvent.getComponent() instanceof CitizenTrafficItem)
                {
                    CitizenTrafficItem item = ((CitizenTrafficItem) tnUiEvent.getComponent());
                    model.put(KEY_I_INDEX, item.getId() - ID_TRAFFIC_ITEM_BASE);

                }
                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    private void addFlags(final MapContainer container)
    {
        Address dest = (Address) model.get(KEY_O_ADDRESS_DEST);
        if (dest == null || dest.getStop() == null)
        {
            return;
        }
        
        int origLat = 0;

        int origLon = 0;
        
        NavSdkRoute currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        int[] oriLatLon = currentRoute.getOriginLatLon();
        
        if (oriLatLon != null && oriLatLon.length > 1)
        {
            origLat = oriLatLon[Route.WAYPOINTS_LAT_INDEX];
            origLon = oriLatLon[Route.WAYPOINTS_LON_INDEX];
        }

        if (origLat != 0 && origLon != 0)
        {
            double latD = origLat / 100000.0d;
            double lonD = origLon / 100000.0d;

            AbstractTnImage startFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
            if (startFlag != null)
            {
                float pivotPointX = 0.5f;
                float pivotPointY = 0;
                origAnnotation = new ImageAnnotation(startFlag, latD, lonD, pivotPointX, pivotPointY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                MapContainer.getInstance().addFeature(origAnnotation);
            }
        }

        Stop destStop = dest.getStop();
        if (destStop != null)
        {
            int destLat = destStop.getLat();
            int destLon = destStop.getLon();
            if (destLat != -1 && destLon != -1)
            {
                double latD = destLat / 100000.0d;
                double lonD = destLon / 100000.0d;

                AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();
                if (destFlag != null)
                {
                    float pivotPointX = 0.5f;
                    float pivotPointY = 0;
                    destAnnotation = new ImageAnnotation(destFlag, latD, lonD, pivotPointX, pivotPointY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                    MapContainer.getInstance().addFeature(destAnnotation);
                }
            }
        }
    }

    public void updateMapContainerEvent(final MapContainer container, int event)
    {
        if (event == IMapUIEventListener.EVENT_CREATED)
        {
            if (model.getState() == STATE_ALTERNATE_ROUTE)
            {
                initMapLayer();
                MapContainer.getInstance().setMapTransitionTime(0);
                MapContainer.getInstance().setRenderingMode(NavSdkMapProxy.RENDERING_MODE_2D_NORTH_UP);
                addFlags(container);
                resizeRoute();
            }
        }
    }
    
//    public void mapViewSizeChanged()
//    {
//        if (model.getState() == STATE_ALTERNATE_ROUTE)
//        {
//            resizeRoute();
//        }
//    }

    public void eglSizeChanged()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (model.getState() == STATE_ALTERNATE_ROUTE)
                {
                    resizeRoute();
                }
            }
        });
    }
    protected void resizeRoute()
    {
        Object obj = model.get(KEY_A_ROUTE_CHOICES);
        if (obj != null)
        {
            Route[] routes = (Route[]) model.get(KEY_A_ROUTE_CHOICES);

            if (routes == null)
            {
                return;
            }
            Vector routeNames = new Vector();
            for (int i = 0; i < routes.length; i++)
            {
                Route route = (Route) routes[i];
                routeNames.add(route.getRouteID());
            }
            
            TnUiArgAdapter xMapAdapter = new TnUiArgAdapter(0, new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            });
            MapContainer.getInstance().setMapRect(xMapAdapter, ((TrafficUiDecorator) uiDecorator).ROUTE_TRAFFIC_MAP_SUMMARY_Y,
                uiDecorator.SCREEN_WIDTH, ((TrafficUiDecorator) uiDecorator).ROUTE_TRAFFIC_MAP_SUMMARY_HEIGHT);
            
            // use different methods to add special suffix to the route names before sending navSDK request 
            String newRoute = String.valueOf(model.getInt(KEY_I_NEW_ROUTE_ID));
            MapContainer.getInstance().showBetterRoutes(routeNames, newRoute);
            MapContainer.getInstance().lookAtBetterRoutes(routeNames, newRoute);
        }
    }
    
    protected void popAllViews()
    {
        if(origAnnotation != null)
        {
            MapContainer.getInstance().removeFeature(origAnnotation);
            origAnnotation = null;
        }
        if(destAnnotation != null)
        {
            MapContainer.getInstance().removeFeature(destAnnotation);
            destAnnotation = null;
        }
        super.popAllViews();
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        TnScreen screen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        if (screen != null && screen.getId() == STATE_SHOW_SUMMARY)
        {
            updateSummaryScreen((CitizenScreen) tnComponent.getRoot());
        }
    }

    @Override
    public void appActivated(String[] params)
    {
        if (NavRunningStatusProvider.getInstance().isNavRunning())
        {
            TnScreen screen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
            if (screen != null && (screen.getId() ==STATE_ALTERNATE_ROUTE))
            {
                relocateMapLogoProvider(uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR);
            }
        }
    }

    @Override
    public void appDeactivated(String[] params)
    {
        
    }
}

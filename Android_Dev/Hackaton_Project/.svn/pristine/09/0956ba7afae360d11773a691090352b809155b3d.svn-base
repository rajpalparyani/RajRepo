/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteSummaryViewTouch.java
 *
 */
package com.telenav.module.nav.routesummary;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.route.NavSdkRoute;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.nav.NavParameter;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCircleAnimation;
import com.telenav.ui.citizen.CitizenRouteSummaryItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-4
 */
class RouteSummaryViewTouch extends AbstractCommonView implements IRouteSummaryConstants
{
    protected int lastCarIndex = -1;
    protected boolean shouldShowCarNextSeg = false;
    public RouteSummaryViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        return null;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch(state)
        {
            case STATE_ROUTE_SUMMARY:
            {
                screen = createRouteSummary(state);
                break;
            }
        }
        
        return screen;
    }

    private TnScreen createRouteSummary(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        addTabContainer(screen);
        updateTimeInfo(screen,this.model.get(KEY_O_NAV_PARAMETER));
        addSummaryTitle(screen.getContentContainer());
        
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        
        addRouteSummary(screen.getContentContainer());
        
        addBottomContainer(screen, createBottomBarArgs());
        
        return screen;
    }

    private void updateTrafficTab(CitizenScreen screen)
    {
        AbstractTnComponent comp = screen.getComponentById(ID_TAB_TRAFFIC_SUMMARY);
        if (comp != null && comp instanceof FrogButton)
        {
            FrogButton trafficButton = (FrogButton) comp;
            int tabTextUnfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_UNFOCUSED);
            int trafficDisabledColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_DISABLED);
            int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
            boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                    || trafficEnableValue == FeaturesManager.FE_PURCHASED;
            PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
            Preference routeTypePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
            boolean isOnboard = model.getBool(KEY_B_IS_ONBOARD);
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
    
    protected void addTabContainer(CitizenScreen screen)
    {
        TnLinearContainer outerContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        int darkGrayColor = UiStyleManager.getInstance().getColor(UiStyleManager.BG_SUMMARY_TIME_INFO);
        
        int tabTextFocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_FOCUSED);
        int tabTextUnfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_UNFOCUSED);
        int trafficDisabledColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_DISABLED); 
        	
        outerContainer.setBackgroundColor(darkGrayColor);
        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator)uiDecorator).TAB_OUTER_CONTAINER_HEIGHT);
        outerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator)uiDecorator).SCREEN_WIDTH);
        
        TnLinearContainer innerContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        
        String turns = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_TURNS, IStringNav.FAMILY_NAV);
        FrogButton turnButton = UiFactory.getInstance().createButton(0, turns);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_LLEFT_BUTTON_FOCUSED);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_LLEFT_BUTTON_FOCUSED);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        turnButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        turnButton.setForegroundColor(tabTextFocusedColor, tabTextUnfocusedColor);
        turnButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));
        turnButton.setFocusable(true);
        innerContainer.add(turnButton);
        
        String traffic = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_TRAFFIC, IStringNav.FAMILY_NAV);
        FrogButton trafficButton = UiFactory.getInstance().createButton(ID_TAB_TRAFFIC_SUMMARY, traffic);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_MIDDLE_BUTTON_UNFOCUSED);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_MIDDLE_BUTTON_UNFOCUSED);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
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
        FrogButton mapButton = UiFactory.getInstance().createButton(ID_TAB_MAP_SUMMARY, map);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TAB_CONTAINER_RIGHT_BUTTON_UNFOCUSED);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TAB_CONTAINER_RIGHT_BUTTON_UNFOCUSED);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        mapButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        TnMenu mapMenu = new TnMenu();
        mapMenu.add("", CMD_MAP_SUMMARY);
        mapButton.setMenu(mapMenu, AbstractTnComponent.TYPE_CLICK);
        mapButton.setCommandEventListener(this);
        mapButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_TRAFFIC_SUMMARY));
        mapButton.setForegroundColor(tabTextFocusedColor, tabTextUnfocusedColor);
        mapButton.setFocusable(true);
        
        innerContainer.add(mapButton);
        
        outerContainer.add(innerContainer);
        
        screen.getContentContainer().add(outerContainer);
    }
    
    
    private void addRouteSummary(AbstractTnContainer contentContainer)
    {
        TnLinearContainer summaryContainer = UiFactory.getInstance().createLinearContainer(ID_SUMMARY_CONTAINER, true,
            AbstractTnGraphics.HCENTER);
        Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (currentRoute != null)
        {
            Segment[] segments = currentRoute.getSegments();
            if (segments != null)
            {
                createRouteSummaryItem(summaryContainer, 0, segments.length);
                if (!currentRoute.isCompeleted() || currentRoute.isPartial())
                {
                    CitizenCircleAnimation progressAnimation = UiFactory.getInstance().createCircleAnimation(
                        ID_LOADING_ANIMATION, false);
                    progressAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                        new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                        {
                            public Object decorate(TnUiArgAdapter args)
                            {
                                return PrimitiveTypeCache
                                        .valueOf(((RouteSummaryUiDecorator) RouteSummaryViewTouch.this.uiDecorator).ROUTE_ITEM_HEIGHT
                                                .getInt());
                            }
                        }));
                    progressAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                        new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                        {
                            public Object decorate(TnUiArgAdapter args)
                            {
                                return PrimitiveTypeCache
                                        .valueOf(((RouteSummaryUiDecorator) RouteSummaryViewTouch.this.uiDecorator).ROUTE_ITEM_HEIGHT
                                                .getInt());
                            }
                        }));
                    summaryContainer.add(progressAnimation);
                    this.model.put(KEY_I_SHOWN_SEGMENT_LENGTH, segments.length);
                }
                else
                {
                    this.model.put(KEY_I_SHOWN_SEGMENT_LENGTH, Integer.MAX_VALUE);
                }
            }

            TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, true);
            panel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((RouteSummaryUiDecorator) uiDecorator).ROUTE_SUMMARY_CONTAINER_HEIGHT);
            panel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator) uiDecorator).SCREEN_WIDTH);
            panel.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
            panel.set(summaryContainer);
            contentContainer.add(panel);
        }
    }
    
    private void createRouteSummaryItem(AbstractTnContainer parent, int startIndex, int endIndex)
    {
        Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
        int currentSegment = 0;
        if (NavSdkNavEngine.getInstance().isRunning())
        {
            NavState navState = NavSdkNavEngine.getInstance().getCurrentNavState();
            if (navState != null)
            {
                currentSegment = navState.getSegmentIndex();
            }
        }
        if (currentSegment < 0)
        {
            currentSegment = 0;
        }
        Segment[] segments = currentRoute.getSegments();
        // Address destAddress = (Address)model.get(KEY_O_ADDRESS_DEST);
        int mediumGrayColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
        if (segments != null && segments.length > 0)
        {
            shouldShowCarNextSeg = false;
            for (int i = startIndex; i < endIndex; i++)
            {
                int turnType;
                String streetName;
                String dist;

                if (i == 0)
                {
                    turnType = Route.TURN_TYPE_START;
                }
                else
                {
                    turnType = segments[i].getTurnType();
                }

                // the last one is destination.
                // if (i == segments.length)
                // {
                // streetName = getStopDisplayLabel(destStop);
                // dist = "";
                // }
                // else
                // {
                if (!isSegmentDisplayed(i))
                {
                    // don't show the turn next to U-turn segment because it has no street name and very short

                    if (isDynamicRoute && currentSegment == i)
                    {
                        shouldShowCarNextSeg = true;
                    }
                }

                streetName = segments[i].getStreetName();
                int distance = segments[i].getLength();
                if (isDestinationTurnType(turnType))
                {
                    dist = "";
                }
                else
                {
                    dist = ResourceManager.getInstance().getStringConverter().convertDistanceMeterToMile(distance, systemUnits);
                }

//                if (i > 1 && segments[i - 1].getTurnType() == Route.L2L_U_TURN && i != (segments.length - 1))
//                {
//                    streetName = segments[i + 1].getStreetName();
//                }

                CitizenRouteSummaryItem routeSummaryItem = UiFactory.getInstance().createCitizenRouteItem(
                    ID_SUMMARY_ITEM_BASE + i, turnType, streetName, dist,
                    ((RouteSummaryUiDecorator) uiDecorator).TURN_AREA_WIDTH,
                    ((RouteSummaryUiDecorator) uiDecorator).TURN_ICON_SIZE,
                    ((RouteSummaryUiDecorator) uiDecorator).ROUTE_ITEM_DIST_SIZE,
                    ((RouteSummaryUiDecorator) uiDecorator).ROUTE_ITEM_STREET_PADDING);
                routeSummaryItem.setForegroundColor(mediumGrayColor, mediumGrayColor);
                routeSummaryItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                    ((RouteSummaryUiDecorator) uiDecorator).SCREEN_WIDTH);
                routeSummaryItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                    ((RouteSummaryUiDecorator) uiDecorator).ROUTE_ITEM_HEIGHT);
                if (isDynamicRoute && (shouldShowCarNextSeg || currentSegment == i))
                {
                    lastCarIndex = i;
                    routeSummaryItem.setIsShowCar(true);

                    shouldShowCarNextSeg = false;
                }
                routeSummaryItem.setFocusable(true);
                TnMenu menu = new TnMenu();
                menu.add("", CMD_ROUTE_ITEM_SELECTED);
                routeSummaryItem.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                parent.add(routeSummaryItem);
            }
        }
    }
    
    private boolean isDestinationTurnType(int turnType)
    {
        return turnType == Route.TURN_TYPE_DESTINATION_AHEAD || turnType == Route.TURN_TYPE_DESTINATION_LEFT
                || turnType == Route.TURN_TYPE_DESTINATION_RIGHT;
    }

    protected boolean isSegmentDisplayed(int index)
    {
//        Route currentRoute = RouteWrapper.getInstance().getCurrentRoute();
        Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if(currentRoute != null)
        {
            Segment[] segments = currentRoute.getSegments();
            if (index > 1 && segments[index - 2].getTurnType() == Route.L2L_U_TURN)
            {
                return false;
            }
        }
        return true;
    }
    
    public String getStopDisplayLabel(Stop stop)
    {
        if(stop == null)
        {
            return "";
        }
        String  displayString = "";
        displayString = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
        return displayString;
    }
    
    private void addSummaryTitle(AbstractTnContainer outerContainer)
    {
        TnLinearContainer summaryTitle = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        summaryTitle.setPadding(0, 0, 0, 0);
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator) uiDecorator).SCREEN_WIDTH);
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator) uiDecorator).SUMMARY_TITLE_HEIGHT);
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.STREET_TABLE_BG_UNFOCUSED);
        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.STREET_TABLE_BG_UNFOCUSED);
        
        AbstractTnFont titleFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_SUMMARY_TITLE);
        FrogLabel turn = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TURN, IStringNav.FAMILY_NAV));
        turn.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR));
        turn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator) uiDecorator).TURN_AREA_WIDTH);
        turn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator) uiDecorator).SUMMARY_TITLE_HEIGHT);
        turn.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        turn.setPadding(0, 0, 0, 0);
        turn.setFont(titleFont);
        
        int streetPadding = ((RouteSummaryUiDecorator) uiDecorator).ROUTE_ITEM_STREET_PADDING.getInt();
        FrogLabel street = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_STREET, IStringNav.FAMILY_NAV));
        street.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR));
        street.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator) uiDecorator).ROUTE_ITEM_STREET_SIZE);
        street.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator) uiDecorator).SUMMARY_TITLE_HEIGHT);
        street.setStyle(AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        street.setPadding(streetPadding, 0, 0, 0);
        street.setFont(titleFont);
        
        FrogLabel dist = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_DIST, IStringNav.FAMILY_NAV));
        dist.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_TABLE_TITLE_COLOR));
        dist.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator) uiDecorator).ROUTE_ITEM_DIST_SIZE);
        dist.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSummaryUiDecorator) uiDecorator).SUMMARY_TITLE_HEIGHT);
        dist.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        dist.setPadding(0, 0, 0, 0);
        dist.setFont(titleFont);

        summaryTitle.add(turn);
        summaryTitle.add(street);
        summaryTitle.add(dist);

        summaryTitle.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
        outerContainer.add(summaryTitle);
    }

    private void updateTimeInfo(CitizenScreen screen, Object navParameter)
    {
        // add Time Remaining title
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        String timeRemainString = bundle.getString(IStringNav.RES_TIME_REMAINING, IStringNav.FAMILY_NAV);
        long eta = 0;
        NavSdkRoute route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (NavSdkNavEngine.getInstance().isRunning())
        {
            if (navParameter instanceof NavParameter)
            {
                eta = (int) ((NavParameter)navParameter).eta;
            }
        }
        else
        {
            eta = route.calcETA(0);
        }
        String timeStr = converter.convertCostTime(eta);
        int bgColor = UiStyleManager.getInstance().getColor(UiStyleManager.BG_SUMMARY_TIME_INFO);

        FrogLabel timeInfo = null;
        if (screen.getComponentById(IRouteSummaryConstants.ID_SUMMARY_ETA) == null)
        {
            timeInfo = UiFactory.getInstance().createLabel(IRouteSummaryConstants.ID_SUMMARY_ETA, timeRemainString + timeStr);
            timeInfo.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUMMARY_TIME_INFO),
                UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUMMARY_TIME_INFO));
            timeInfo.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSummaryUiDecorator) uiDecorator).SCREEN_WIDTH);
            timeInfo.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((RouteSummaryUiDecorator) uiDecorator).ROUTE_TIME_INFO_HEIGHT);
            timeInfo.setBackgroundColor(bgColor);
            timeInfo.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            timeInfo.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_SUMMARY_TIME_INFO));
            screen.getContentContainer().add(timeInfo);
        }
        else
        {
            timeInfo = (FrogLabel) screen.getComponentById(IRouteSummaryConstants.ID_SUMMARY_ETA);
            timeInfo.setText(timeRemainString + timeStr);
        }
    }
    
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        
        return cmd;
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
                int command = commandEvent.getCommand();
                if (command == CMD_ROUTE_ITEM_SELECTED)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    if (component != null)
                    {
                        int id = component.getId();
                        if (id >= ID_SUMMARY_ITEM_BASE && id < ID_SUMMARY_ITEM_MAX)
                        {
                            int index = id - ID_SUMMARY_ITEM_BASE;
                            model.put(KEY_I_CURRENT_SEGMENT_INDEX, index);
                        }
                    }
                }
                break;
            }
        }
        return false;
    }
    
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch(state)
        {
            case STATE_ROUTE_SUMMARY:
            {
                boolean isUpdate = false;
                Object obj = model.fetch(KEY_O_NAV_PARAMETER);
                if (obj != null)
                {
                    if (screen instanceof CitizenScreen)
                    {
                        updateTrafficTab((CitizenScreen) screen);
                        updateTimeInfo((CitizenScreen) screen, obj);
                        NavState navState = NavSdkNavEngine.getInstance().getCurrentNavState();
                        if(navState != null)
                        {
                            int currentSegIndex = navState.getSegmentIndex();
                            boolean notDisplayCurrent = !isSegmentDisplayed(currentSegIndex);
                            if (lastCarIndex == -1)
                            {
                                isUpdate = false;
                            }
                            else if (currentSegIndex == lastCarIndex)
                            {
                                isUpdate = false;
                            }
                            else if (notDisplayCurrent && lastCarIndex > currentSegIndex)
                            {
                                isUpdate = false;
                            }
                            else
                            {
                                AbstractTnComponent comp = screen.getComponentById(ID_SUMMARY_CONTAINER);
                                if (comp != null)
                                {
                                    TnLinearContainer linearContainer = (TnLinearContainer) comp;
                                    if (linearContainer.getChildrenSize() > 0)
                                    {
                                        int lastCarItemId = ID_SUMMARY_ITEM_BASE + lastCarIndex;
                                        int newCarItemId = ID_SUMMARY_ITEM_BASE + currentSegIndex;
                                        
                                        if(notDisplayCurrent)
                                        {
                                            newCarItemId++;
                                        }
                                        
                                        CitizenRouteSummaryItem lastCarItem = (CitizenRouteSummaryItem)linearContainer.getComponentById(lastCarItemId);
                                        CitizenRouteSummaryItem newCarItem = (CitizenRouteSummaryItem)linearContainer.getComponentById(newCarItemId);
                                        if (lastCarItem != null)
                                        {
                                            lastCarItem.setIsShowCar(false);
                                            isUpdate = true;
                                        }
                                        
                                        if (newCarItem != null)
                                        {
                                            newCarItem.setIsShowCar(true);
                                            isUpdate = true;
                                            
                                            lastCarIndex = currentSegIndex;
                                        }
                                    }
                                }
                            }
                            
                            NavSdkRoute currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
                            Segment[] segments = currentRoute.getSegments();
                            int lastSegmentsSize = this.model.getInt(KEY_I_SHOWN_SEGMENT_LENGTH);
                            if (segments.length > lastSegmentsSize)
                            {
                                CitizenCircleAnimation circleAnimation = (CitizenCircleAnimation) screen
                                        .getComponentById(ID_LOADING_ANIMATION);
                                TnLinearContainer routeSummaryContainer = (TnLinearContainer) screen
                                        .getComponentById(ID_SUMMARY_CONTAINER);
                                routeSummaryContainer.remove(circleAnimation);
                                createRouteSummaryItem(routeSummaryContainer, lastSegmentsSize, segments.length);
                                if (!currentRoute.isCompeleted() || currentRoute.isPartial())
                                {
                                    routeSummaryContainer.add(circleAnimation);
                                }
                                this.model.put(KEY_I_SHOWN_SEGMENT_LENGTH, segments.length);
                            }
                        }
                    }
                }
                return isUpdate;
            }
        }
        return false;
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
        
        boolean isOnboard = NavRunningStatusProvider.getInstance().isOnBoardRoute()
                || !NetworkStatusManager.getInstance().isConnected();
        args.cmdIds[3] =  CMD_COMMON_LINK_TO_SEARCH;
        args.enableIcons[3] = isOnboard ? false : true;
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
        args.focusImageAdapters[3] =  ImageDecorator.IMG_BOTTOM_BAR_PLACES_FOCUS;
        args.focusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_FOCUS;
        
        return args;
    }
    
    protected AbstractTnContainer createBottomContainer(final AbstractTnContainer titleContainer, final BottomContainerArgs bottomBarArgs)
    {
        AbstractTnContainer container = super.createBottomContainer(titleContainer, bottomBarArgs);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);
        return container;
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MovingMapViewTouch.java
 *
 */
package com.telenav.module.nav.movingmap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import android.os.RemoteException;
import android.view.View;

import com.telenav.app.CommManager;
import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.scout_us.TeleNav;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.poi.BillboardAd;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.route.NavSdkRoute;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.DwfUpdateListener;
import com.telenav.dwf.aidl.Friend;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.logger.Logger;
import com.telenav.map.InteractionModeChangeListener;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.browsersdk.IBrowserSdkConstants;
import com.telenav.module.dwf.DwfUtil;
import com.telenav.module.dwf.DwfSliderPopup;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.CurrentStreetNameComponent;
import com.telenav.module.nav.IAdiModeHelper;
import com.telenav.module.nav.NavAdManager;
import com.telenav.module.nav.NavBackToNavButton;
import com.telenav.module.nav.NavBottomStatusBarHelper;
import com.telenav.module.nav.NavCompassComponent;
import com.telenav.module.nav.NavParameter;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.nav.NavSpeedLimitComponent;
import com.telenav.module.nav.NavTrafficAlertContainer;
import com.telenav.module.nav.NavTrafficButton;
import com.telenav.module.nav.NavZoomButton;
import com.telenav.module.nav.NavZoomButton.IZoomButtonListener;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.preference.carmodel.CarModelManager;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewData.AnnotationType;
import com.telenav.navsdk.events.NavigationData.VoiceGuidanceType;
import com.telenav.navsdk.events.NavigationEvents.VehiclePosition;
import com.telenav.navsdk.events.NavigationEvents.VoiceGuidancePlayNotification;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.tnui.widget.TnWebBrowserField.WebBrowserEventListener;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenButton;
import com.telenav.ui.citizen.CitizenMessageBox;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenSlidableContainer;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.map.AbstractAnnotation;
import com.telenav.ui.citizen.map.FriendAnnotation;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.IMapUIEventListener;
import com.telenav.ui.citizen.map.ImageAnnotation;
import com.telenav.ui.citizen.map.IncidentAnnotation;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author yning (yning@telenav.cn)
 * @date 2010-11-5
 */
class MovingMapViewTouch extends AbstractCommonMapView implements IMovingMapConstants, IApplicationListener, IAdiModeHelper,
        InteractionModeChangeListener, IBillboardModeChangeListener, INetworkStatusListener, IZoomButtonListener
{
    protected static final int NO_ACTION_TIMEOUT = 7000;
    
    protected static final int NOTIFICATION_TIMEOUT = 3000;
    
    // 70 mi/h = 112 km/h = 31.1 meter/second
    // 40 mi/h = 64 km/h = 17.78 meter/second
    protected static final float MIN_SPEED_THRESHOLD_IN_METER_PER_SECOND = 17.78f;

    // Routing engine defines tight turn and showing tight turn in 30 seconds
    protected static final float THRESHOLD_FOR_SHOWING_TIGHT_TURN_IN_SECONDS = 30.0f;

    protected boolean isLastGpsCellLocation = false;

    protected boolean isLastNotShowOnboard;

    protected boolean isLastNoGpsSignal = false;
    
    protected boolean isAppPaused;
    
    protected boolean hasGpsSignal = false;
    
    protected boolean isInitialPopupShown = false;
    
    protected boolean hasAdjustFlag = false;
    
    protected boolean isDoublePointTouch = false;
    
    protected boolean isReFollowVechicle = false;
    
    protected int oldOrientation = 0;
    
    protected int lastOrientation = -1;

    protected BillboardAd currentAd;

    protected ImageAnnotation destFlag = null;

    protected CitizenSlidableContainer notification;

    protected CitizenSlidableContainer bottomPopup;
    
    protected CitizenSlidableContainer zoomPopup;
    
    protected CitizenSlidableContainer trafficSummaryButtonPopup;
    
    protected CitizenSlidableContainer trafficAlertButtonPopup;
    
    protected CitizenSlidableContainer backToNavPopup;
    
    protected TrafficAlertEvent lastAlertEvent = null;
    
    private boolean isNextNextTurnShown = false;
    
    private boolean isKeyBack= false;
    
    private boolean hasLogMapEvent = false;
    
    private Vector<FriendAnnotation> currentFriendAnnotations = new Vector<FriendAnnotation>();
    
    private MovingMapFriendsAnnotationHandler friendsAnnotationHandler = new MovingMapFriendsAnnotationHandler();
    
    private final static int DWF_DESTINATION_THREASHOLD  = 100;
    
    public MovingMapViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
        TeleNavDelegate.getInstance().registerApplicationListener(this);
        NetworkStatusManager.getInstance().addStatusListener(this);
    }

    protected TnPopupContainer createPopup(int state)
    {
        if (isAppPaused || !this.model.isActivated())
        {
            return null;
        }
        TnPopupContainer popup = null;
        switch (state)
        {
            case STATE_EXIT_CONFIRM:
            case STATE_COMMON_EXIT:
            {
                popup = createExitConfirm(state);
                break;
            }
            case STATE_RTS_FAILED:
            {
                popup = createRtsFailed(state);
                break;
            }
            case STATE_END_TRIP_MODE:
            {
                // we don't use MVC TnPopupContainer, instead we treat endtrip board part of map container.
                createEndTripComponent();
                NavParameter param = (NavParameter) model.get(KEY_O_NAV_PARAMETER);
                if (param != null)
                {
                    // fix CANNON-429,update nav info in end trip mode
                    // no real popup return,so update event will always go create block.
                    updateNavInfo(param, ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen());
                }
                break;
            }
            case STATE_TRAFFIC_ALERT_DETAIL:
            {
                popup = createTrafficAlertDetail(state);
                break;
            }
            case STATE_AVOID_INCIDENT:
            {
                popup = createAvoidIncidentProgressBar(state);
                break;
            }
            case STATE_REQUESTING_DEVIATION:
            {
                popup = createRequestDeviationProgressBox(state);
                break;
            }
            case STATE_DRIVING_TO_AD:
            {
                removeFlag();
                popup = createRequestAdRouteProgressBox(state);
                break;
            }
            case STATE_DEVIATION_FAIL:
            {
                popup = createDeviationFailPopup(state);
                break;
            }
            case STATE_DETOUR_CONFIRM:
            {
                popup = createDetourConfirmPopup(state);
                break;
            }
            case STATE_ROUTING_ERROR:
            case STATE_ACCOUNT_ERROR:
            case STATE_ACCOUNT_ERROR_FATAL:
            {
                popup = super.createPopupDelegate(STATE_COMMON_ERROR);
                popup.setId(state);
                break;
            }
        }
        return popup;
    }

    protected TnPopupContainer createDetourConfirmPopup(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON),
            CMD_RESUME_TRIP);
        menu.add(
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON),
            CMD_COMMON_BACK);

        Address lastTrip = DaoManager.getInstance().getTripsDao().getLastTrip();

        String lastTripLabel = lastTrip.getLabel();
        if (lastTripLabel == null || lastTripLabel.length() == 0)
        {
            lastTripLabel = ResourceManager.getInstance().getStringConverter().convertAddress(lastTrip.getStop(), false);
        }

        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, lastTripLabel, menu);
        FrogLabel title = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_RESUME_TRIP_TO, IStringNav.FAMILY_NAV));

        messageBox.setTitle(title);
        return messageBox;
    }

    private void createEndTripComponent()
    {
        AbstractTnComponent endTripComponent = MapContainer.getInstance().getFeature(ID_END_TRIP_CONTAINER);

        if (endTripComponent == null)
        {
            TnLinearContainer container = UiFactory.getInstance().createLinearContainer(ID_END_TRIP_CONTAINER, false,
                AbstractTnGraphics.VCENTER);

            container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).SCREEN_WIDTH);

            container.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MovingMapUiDecorator) uiDecorator).NAV_END_TRIP_X);
            container.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).NAV_END_TRIP_Y);

            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.NAV_END_TRIP_BG);
            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.NAV_END_TRIP_BG);

            FrogNullField leftNullField = UiFactory.getInstance().createNullField(0);
            leftNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).END_TRIP_HORIZONTAL_PADDING);
            container.add(leftNullField);
            
            TnLinearContainer leftContainer = UiFactory.getInstance()
                    .createLinearContainer(0, true, AbstractTnGraphics.VCENTER);
            int color = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
            AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE);

            String firstLine = this.getDestinationFirstLine();
            if (firstLine != null && firstLine.length() > 0)
            {
                FrogLabel firstLineLabel = UiFactory.getInstance().createLabel(0, firstLine);
                firstLineLabel.setForegroundColor(color, color);
                firstLineLabel.setFont(font);
                leftContainer.add(firstLineLabel);
            }

            String secondLine = this.getDestinationSecondLine();
            if (secondLine != null && secondLine.length() > 0)
            {
                FrogLabel secondLineLabel = UiFactory.getInstance().createLabel(0, secondLine);
                secondLineLabel.setForegroundColor(color, color);
                secondLineLabel.setFont(font);
                leftContainer.add(secondLineLabel);
            }

            leftContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((MovingMapUiDecorator) uiDecorator).NAV_END_TRIP_LEFT_WIDTH);

            container.add(leftContainer);

            FrogButton addToFavButton = UiFactory.getInstance().createButton(ID_END_TRIP_ADD_FAV_BUTTON, "");

            Address destAddress = (Address) model.get(KEY_O_ADDRESS_DEST);
            byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(destAddress);
            Address newAddress = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
            if (newAddress.getPoi() == null)
            {
                newAddress.setType(Address.TYPE_FAVORITE_STOP);
            }
            else
            {
                newAddress.setType(Address.TYPE_FAVORITE_POI);
            }
            newAddress.setSource(Address.SOURCE_FAVORITES);
            if (DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(newAddress, true))
            {
                addToFavButton.setIcon(ImageDecorator.IMG_FAVORITES_ADD_ICON_FOCUSED.getImage(),
                    ImageDecorator.IMG_FAVORITES_ADD_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.LEFT);
            }
            else
            {
                addToFavButton.setIcon(ImageDecorator.IMG_FAVORITES_ICON_FOCUSED.getImage(),
                    ImageDecorator.IMG_FAVORITES_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.LEFT);
            }

            TnMenu menu = new TnMenu();
            menu.add("", CMD_ADD_TO_FAVORITE);
            addToFavButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            addToFavButton.setPadding(0, 0, 0, 0);
            addToFavButton.setCommandEventListener(this);
            addToFavButton.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
            addToFavButton.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);

            TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false,
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.RIGHT);

            buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((MovingMapUiDecorator) uiDecorator).NAV_END_TRIP_HEIGHT);
            buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((MovingMapUiDecorator) uiDecorator).NAV_END_TRIP_HEIGHT);

            buttonContainer.add(addToFavButton);
            container.add(buttonContainer);
            FrogNullField rightNullField = UiFactory.getInstance().createNullField(0);
            rightNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).END_TRIP_HORIZONTAL_PADDING);
            container.add(rightNullField);

            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeOptionsMenu();
            handlePopupWhenArrival();
            relocateMapLogoProvider(uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR);
            MapContainer.getInstance().addFeature(container);
        }
    }

    private boolean isEndTripShown()
    {
        return MapContainer.getInstance().getFeature(ID_END_TRIP_CONTAINER) != null;
    }

    protected TnPopupContainer createAvoidIncidentProgressBar(int state)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(
            state,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_LABEL_LOADING, IStringCommon.FAMILY_COMMON));
        return progressBox;
    }

    protected TnPopupContainer createTrafficAlertDetail(int state)
    {
        String avoid = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_AVOID, IStringNav.FAMILY_NAV);
        String ignore = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_AVOID_POPUP_IGNORE, IStringNav.FAMILY_NAV);
        TnMenu menu = UiFactory.getInstance().createMenu();
        avoid = (avoid == null ? "" : avoid);
        ignore = (ignore == null ? "" : ignore);
        menu.add(avoid, CMD_AVOID);
        menu.add(ignore, CMD_COMMON_BACK);
        final CitizenMessageBox popup = (CitizenMessageBox) UiFactory.getInstance().createMessageBox(state, "", menu);
        constructTrafficAlertDetailTopContainer(popup);
        return popup;
    }

    private void constructTrafficAlertDetailTopContainer(final CitizenMessageBox popup)
    {
        popup.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_TOP_HEIGHT,
            ((MovingMapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_TOP_HEIGHT);
        final AbstractTnContainer topContainer = popup.getTopContainer();

        final int padding = topContainer.getLeftPadding() + topContainer.getRightPadding();
        final TnUiArgAdapter widthAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(popup.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt() - padding);
            }
        });
        topContainer.setPadding(0, ((MovingMapUiDecorator) uiDecorator).TRAFFIC_ALERT_POPUP_SHADOW_PADDING.getInt(), 0,
            topContainer.getBottomPadding());

        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, popup.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH));
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((MovingMapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_TOP_HEIGHT);

        final FrogLabel title = UiFactory.getInstance().createLabel(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringNav.RES_TRAFFIC_DELAY_DETECTED, IStringNav.FAMILY_NAV));
        title.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));

        FrogNullField gapField = UiFactory.getInstance().createNullField(0);
        gapField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(widthAdapter.getInt() - title.getPreferredWidth()
                            - ImageDecorator.MAP_TRAFFIC_ICON_FOCUSED.getImage().getWidth());
                }
            }));

        CitizenButton button = UiFactory.getInstance().createCitizenButton(0, "",
            ImageDecorator.MAP_TRAFFIC_ICON_FOCUSED.getImage(), ImageDecorator.MAP_TRAFFIC_ICON_UNFOCUSED.getImage());
        button.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
        button.setPadding(0, 0, 0, 0);

        TnMenu menu = new TnMenu();
        menu.add("", CMD_TRAFFIC_SUMMARY);
        button.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        button.setCommandEventListener(this);

        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((MovingMapUiDecorator) uiDecorator).TRAFFIC_ALERT_TITEL_HEIGHT);
        titleContainer.add(title);
        titleContainer.add(gapField);
        titleContainer.add(button);

        container.add(titleContainer);

        if (isComponentNeeded(INinePatchImageRes.POPUP_SPLIT_LINE_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED))
        {
            TnLinearContainer gapContainer = UiFactory.getInstance().createLinearContainer(0, true,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
            gapContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((MovingMapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_SPACE_BELOW_SPLIT_LINE);
            FrogLabel gapLine = UiFactory.getInstance().createLabel(0, "");
            gapLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, popup.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH));
            gapLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).POPUP_SPLIT_LINE_HEIGHT);
            gapLine.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.POPUP_SPLIT_LINE_UNFOCUSED_UNFOCUSED);
            gapLine.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.POPUP_SPLIT_LINE_UNFOCUSED_UNFOCUSED);
            gapContainer.add(gapLine);
            container.add(gapContainer);
        }

        TnLinearContainer contentContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdapter);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((MovingMapUiDecorator) uiDecorator).TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT);
        if (isComponentNeeded(INinePatchImageRes.FAVORITE_PURE_WHITE_BG + INinePatchImageRes.ID_UNFOCUSED))
        {
            contentContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
            contentContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
        }
        NavParameter param = (NavParameter) model.get(KEY_O_NAV_PARAMETER);
        if (param != null)
        {

            TnScrollPanel scrollPanel = UiFactory.getInstance().createScrollPanel(0, true);
            contentContainer.add(scrollPanel);

            TnLinearContainer contentInnerContainer = UiFactory.getInstance().createLinearContainer(0, false);
            contentInnerContainer.setPadding(10, 10, 10, 10);
            scrollPanel.set(contentInnerContainer);

            TrafficAlertEvent event = param.alertEvent;
            if (event != null)
            {
                FrogImageComponent incidentIcon = UiFactory.getInstance().createFrogImageComponent(0,
                    getIncidentIcon(event.getSeverity()));
                contentInnerContainer.add(incidentIcon);

                FrogNullField gap = UiFactory.getInstance().createNullField(0);
                gap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                    new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                    {

                        public Object decorate(TnUiArgAdapter args)
                        {
                            return PrimitiveTypeCache.valueOf(10);
                        }
                    }));
                contentInnerContainer.add(gap);
                
                final int iconWidth = getIncidentIcon(event.getSeverity()).getWidth();
                String message = event.getMessage();
                FrogMultiLine messageMultiLine = UiFactory.getInstance().createMultiLine(0, message);
                messageMultiLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,  new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {

                    public Object decorate(TnUiArgAdapter args)
                    {
                        //10 innergap 20 leftMargin+rightMargin 
                        return PrimitiveTypeCache.valueOf(widthAdapter.getInt() - 10 - iconWidth - 20);
                    }
                }));
                messageMultiLine.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
                contentInnerContainer.add(messageMultiLine);
            }
        }
        container.add(contentContainer);
        popup.setTitle(container);
    }

    protected AbstractTnImage getIncidentIcon(int severity)
    {
        if (severity <= 0)
        {
            severity = 1;
        }

        TnUiArgAdapter image;

        switch (severity)
        {
            case TrafficIncident.SEVERITY_SEVERE:
            {
                image = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_RED_ICON_UNFOCUSED;
                break;
            }
            case TrafficIncident.SEVERITY_MAJOR:
            {
                image = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_ORANGE_ICON_UNFOCUSED;
                break;
            }
            case TrafficIncident.SEVERITY_MINOR:
            default:
            {
                image = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_YELLOW_ICON_UNFOCUSED;
                break;
            }
        }

        return image.getImage();
    }

    protected String getDestinationFirstLine()
    {
        Address address = (Address) model.get(KEY_O_ADDRESS_DEST);

        StringBuffer firstLine = new StringBuffer();
        if (address != null)
        {
            if (address.getPoi() != null && address.getPoi().getBizPoi() != null)
            {
                Poi poi = address.getPoi();
                BizPoi bizPoi = poi.getBizPoi();

                firstLine.append(bizPoi.getBrand());
            }
            else
            {
                Stop destStop = address.getStop();
                if (destStop != null)
                {
                    String strLine = destStop.getFirstLine();
                    if (strLine != null && strLine.trim().length() > 0)
                        firstLine.append(strLine);
                    else
                        firstLine.append(ResourceManager.getInstance().getStringConverter().convertAddress(destStop, false));
                }
            }
        }
        return firstLine.toString().trim();
    }

    final static private boolean isStringEmpty(String str)
    {
        return (str == null) || (str.trim().length() == 0);
    }

    protected String getDestinationSecondLine()
    {
        Address address = (Address) model.get(KEY_O_ADDRESS_DEST);
        StringBuffer secondLine = new StringBuffer();
        if (address != null)
        {
            if (address.getPoi() != null && address.getPoi().getBizPoi() != null)
            {
                Stop destStop = address.getStop();
                if (destStop != null)
                {
                    if (!isStringEmpty(destStop.getFirstLine()))
                    {
                        secondLine.append(destStop.getFirstLine());
                    }
                    if (!isStringEmpty(destStop.getCity()))
                    {
                        if (secondLine.length() > 0)
                        {
                            secondLine.append(", ");
                        }
                        secondLine.append(destStop.getCity().trim());
                        if (!isStringEmpty(destStop.getProvince()))
                        {
                            secondLine.append(", ").append(destStop.getProvince().trim());
                        }
                    }
                }
            }
            else
            {
                Stop destStop = address.getStop();
                if (destStop != null)
                {
                    if (!isStringEmpty(destStop.getCity()))
                    {
                        secondLine.append(destStop.getCity());
                        if (!isStringEmpty(destStop.getProvince()))
                        {
                            secondLine.append(", ").append(destStop.getProvince().trim());
                        }
                    }
                }
            }
        }
        return secondLine.toString().trim();
    }

    protected TnPopupContainer createRtsFailed(int state)
    {
        String msg = this.model.fetchString(KEY_S_ERROR_MESSAGE);
        int commandId = CMD_COMMON_BACK;
        int buttonId = IStringCommon.RES_BTTN_OK;
        boolean isValidService = model.getBool(KEY_B_IS_VALID_SERVICE);
        if (isValidService)
        {
            commandId = CMD_GENERAL_FEEDBACK;
            buttonId = IStringCommon.RES_FEEDBACK_MENU;
        }
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String buttonName = bundle.getString(buttonId, IStringCommon.FAMILY_COMMON);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(buttonName, commandId);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, msg, menu);
        return messageBox;
    }

    protected TnPopupContainer createExitConfirm(int state)
    {
        if (model.getBool(KEY_B_IS_FROM_SEARCH_ALONG))
        {
            return createDetourConfirm(state);
        }

        String message = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_MOVING_MAP_END_TRIP_MSG, IStringNav.FAMILY_NAV);
        String endTrip = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON);
        String cancel = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
        TnMenu menu = UiFactory.getInstance().createMenu();
        message = (message == null ? "" : message);
        endTrip = (endTrip == null ? "" : endTrip);
        cancel = (cancel == null ? "" : cancel);
        menu.add(endTrip, CMD_END_TRIP);
        menu.add(cancel, CMD_COMMON_BACK);
        TnPopupContainer popup = UiFactory.getInstance().createMessageBox(state, message, menu);
        return popup;
    }

    protected TnPopupContainer createDetourConfirm(int state)
    {
        String message = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_END_DETOUR_DETAIL, IStringNav.FAMILY_NAV);
        String endDetour = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_END_DETOUR_BTTN, IStringNav.FAMILY_NAV);
        String endTrip = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_END_TRIP_BTTN, IStringNav.FAMILY_NAV);
        String cancel = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON);
        TnMenu menu = UiFactory.getInstance().createMenu();
        message = (message == null ? "" : message);
        endTrip = (endTrip == null ? "" : endTrip);
        cancel = (cancel == null ? "" : cancel);
        menu.add(endDetour, CMD_RESUME_TRIP);
        menu.add(endTrip, CMD_END_TRIP);
        menu.add(cancel, CMD_COMMON_BACK);
        FrogMessageBox popup = UiFactory.getInstance().createMessageBox(state, message, menu);
        TnUiArgAdapter widthAdapter = null;
        AbstractTnContainer content = popup.getContent();
        if (content != null)
        {
            widthAdapter = content.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH);
        }

        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_DETOUR, IStringNav.FAMILY_NAV);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        if (widthAdapter != null)
        {
            titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdapter);
        }

        popup.setTitle(titleLabel);
        return popup;
    }

    protected TnPopupContainer createRequestAdRouteProgressBox(int state)
    {
        hideVBB();

        MapContainer mapContainer = (MapContainer) model.get(KEY_O_MAP_CONTAINER);
        if (mapContainer != null)
        {
            RouteUiHelper.resetCurrentRoute();
        }

        String loading = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_GETTING_ROUTE, IStringNav.FAMILY_NAV);
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(state, loading);
        return progressBox;
    }

    protected TnPopupContainer createRequestDeviationProgressBox(int state)
    {
        MapContainer mapContainer = (MapContainer) model.get(KEY_O_MAP_CONTAINER);
        if (mapContainer != null)
        {
            RouteUiHelper.resetCurrentRoute();
        }

        String reRouting = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_REROUTING, IStringNav.FAMILY_NAV);
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(state, reRouting);
        return progressBox;
    }

    protected TnPopupContainer createDeviationFailPopup(int state)
    {
        String errorMsg = model.getString(KEY_S_ERROR_MESSAGE);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON),
            CMD_COMMON_OK);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
        messageBox.setCommandEventListener(this);
        return messageBox;
    }

    protected TnScreen createScreen(int state)
    {
        if (isAppPaused)
        {
            return null;
        }

        TnScreen screen = null;

        switch (state)
        {
            case STATE_MOVING_MAP:
            {
                screen = createMovingMap(state);
                break;
            }
            case STATE_GENERAL_FEEDBACK:
            {
                screen = createGeneralFeedbackScreen(state);
                break;
            }
        }

        return screen;
    }

    private TnScreen createGeneralFeedbackScreen(int state)
    {
        hideAllPopup();
        int screenBackgroundColor = UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR);
        return createGeneralFeedbackScreen(state, screenBackgroundColor);
    }

    private TnScreen createMovingMap(int state)
    {
        CitizenScreen movingMapScreen = UiFactory.getInstance().createScreen(state);

        createMapContainer(movingMapScreen);

        int index = 0;
        TnMenu menu = movingMapScreen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);
        if (menu == null)
        {
            menu = new TnMenu();
        }
        menu.remove(ICommonConstants.CMD_COMMON_EXIT);

        boolean isOnBoard = !NetworkStatusManager.getInstance().isConnected();
        if (!isOnBoard)
        {
            int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
            boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                    || trafficEnableValue == FeaturesManager.FE_PURCHASED;

            if (isTrafficEnabled)
            {
                int trafficCameraValue = FeaturesManager.getInstance().getStatus(
                    FeaturesManager.FEATURE_CODE_NAV_TRAFFIC_CAMERA);
                boolean isTrafficCameraOn = trafficCameraValue == FeaturesManager.FE_ENABLED
                        || trafficCameraValue == FeaturesManager.FE_PURCHASED;
                if (isTrafficCameraOn)
                {
                    menu.add(
                        ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringNav.RES_REPORT_SPEED_TRAP, IStringNav.FAMILY_NAV),
                        CMD_REPORT_SPEED_TRAP, index++);
                }
            }
        }

        menu.add(
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_END_TRIP, IStringCommon.FAMILY_COMMON),
            CMD_END_TRIP, index++);

        movingMapScreen.getRootContainer().setMenu(menu, AbstractTnComponent.TYPE_MENU);

        // FIXME: a hack solution
        if (model instanceof MovingMapModel)
        {
            MovingMapModel movingMapModel = (MovingMapModel) model;
            movingMapModel.setBillboardModeChangeListener(this);
        }

        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        this.isLastNoGpsSignal = location == null;

        addSliderMenuPopup(movingMapScreen);
        
        return movingMapScreen;
    }
    
    private NavSpeedLimitComponent addSpeedLimitContainer(MapContainer mapContainer)
    {
        NavSpeedLimitComponent speedLimit = new NavSpeedLimitComponent(ID_SPEED_LIMIT_CONTAINER);
        speedLimit.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_X);
        speedLimit.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_Y);
        speedLimit.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_WIDTH);
        speedLimit.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_HEIGHT);

        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_X);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_Y);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_WIDTH);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).SPEED_LIMIT_HEIGHT);

        mapContainer.addFeature(nullField);
        mapContainer.addFeature(speedLimit);
        return speedLimit;
    }

    private CurrentStreetNameComponent addStreetName(MapContainer mapContainer)
    {
        CurrentStreetNameComponent currentStreetName = new CurrentStreetNameComponent(ID_CURRENT_STREET_NAME);

        currentStreetName.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MovingMapUiDecorator) uiDecorator).STREET_NAME_X);
        currentStreetName.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).STREET_NAME_Y);

        currentStreetName.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).STREET_NAME_WIDTH);
        currentStreetName.getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).STREET_NAME_HEIGHT);

        mapContainer.addFeature(currentStreetName);
        return currentStreetName;
    }

    private void addCompassContainer(MapContainer mapContainer)
    {
        NavCompassComponent compass = new NavCompassComponent(ID_COMPASS_CONTAINER, mapContainer, this);
        compass.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MovingMapUiDecorator) uiDecorator).COMPASS_X);
        compass.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).COMPASS_Y);
        compass.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).COMPASS_WIDTH);
        compass.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).COMPASS_HEIGHT);
        mapContainer.addFeature(compass);
    }
    
    private NavNextNextTurnComponent addNextNextTurnContainer(MapContainer mapContainer)
    {
        NavNextNextTurnComponent nextNextTurn = new NavNextNextTurnComponent(ID_NEXT_NEXT_TURN_COMPONENT);
        nextNextTurn.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MovingMapUiDecorator) uiDecorator).NEXT_NEXT_TURN_X);
        nextNextTurn.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).NEXT_NEXT_TURN_Y);
        mapContainer.addFeature(nextNextTurn);
        return nextNextTurn;
    }

    private AbstractTnComponent createMapContainer(CitizenScreen screen)
    {
        lastOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        MapContainer mapContainer = UiFactory.getInstance().getCleanMapContainer(screen, ID_MAP_CONTAINER);
        mapContainer.setInteractionModeChangeListener(this);

        NavTopComponent navTopComponent = new NavTopComponent(ID_NAV_TITLE);
        navTopComponent.setNextStreetCmdId(CMD_PLAY_AUDIO, this);
        navTopComponent.setCommandEventListener(this);
        mapContainer.addFeature(navTopComponent);

        model.put(KEY_O_MAP_CONTAINER, mapContainer);

        addSpeedLimitContainer(mapContainer);
        addStreetName(mapContainer);
        addCompassContainer(mapContainer);
        mapContainer.addFeature(createMapCompanyLogo(((MovingMapUiDecorator) uiDecorator).MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR));
        mapContainer.addFeature(createMapProvider(((MovingMapUiDecorator) uiDecorator).MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR));
        mapContainer.setTouchEventListener(this);
        mapContainer.setMapUIEventListener(this);
        mapContainer.enableGPSCoarse(false);
        mapContainer.setLowFPSAllowed(true);

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
                int height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                        - ((MovingMapUiDecorator) uiDecorator).NAV_TITLE_HEIGHT.getInt()
                        - ((MovingMapUiDecorator) uiDecorator).STREET_NAME_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(height);
            }
        });

        TnUiArgAdapter mapY = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int y = ((MovingMapUiDecorator) uiDecorator).NAV_TITLE_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(y);
            }
        });

        mapContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, mapHeight);
        mapContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, mapY);

        mapContainer.setMapRect(mapX, mapY, mapWidth, mapHeight);

        this.updateMapContainerEvent(mapContainer, IMapUIEventListener.EVENT_CREATED);

        return mapContainer;
    }
    
    private boolean isSameDestination(Address original, Address target, int threshold)
    {
        if (original == null || target == null)
        {
            return false;
        }
        int oriLat = original.getStop().getLat();
        int oriLon = original.getStop().getLon();
        
        int currentLat = target.getStop().getLat();
        int currentLon = target.getStop().getLon();
        long distMeter = DataUtil.gpsDistance(currentLat - oriLat, currentLon - oriLon, DataUtil.getCosLat(currentLat));
        if (distMeter < threshold)
        {
            return true;
        }
        return false;
    }
    
    private void addSliderMenuPopup(TnScreen movingMapScreen)
    {
        View parent = (View)movingMapScreen.getRootContainer().getNativeUiComponent();
        
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
        if (dwfAidl != null)
        {
            try
            {
                if (dwfAidl.getSharingIntent() == null)
                {
                    if (DwfSliderPopup.getInstance().isShowing())
                    {
                        DwfSliderPopup.getInstance().hide();
                        dwfAidl.removeUpdateListener(friendsAnnotationHandler.toString());
                    }
                    return;
                }
                else
                {
                    String addressDt = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
                    if (DwfSliderPopup.getInstance().isShowing())
                    {
                        if (addressDt != null)
                        {
                            Address address = DwfUtil.jsonToAddress(addressDt);

                            Address dest = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                            if (!isSameDestination(address, dest, DWF_DESTINATION_THREASHOLD))
                            {
                                dwfAidl.setShareLocationInterval(10000);
                                DwfSliderPopup.getInstance().hide();
                                dwfAidl.removeUpdateListener(friendsAnnotationHandler.toString());
                            }
                        }
                    }
                    else
                    {
                        if (addressDt != null)
                        {
                            Address address = DwfUtil.jsonToAddress(addressDt);

                            Address dest = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                            if (isSameDestination(address, dest, DWF_DESTINATION_THREASHOLD))
                            {
                                dwfAidl.updateStatus(-1, Friend.FriendStatus.DRIVING.name(), 0, 0);
                                TnUiArgAdapter mapHeight = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0),
                                        new ITnUiArgsDecorator()
                                        {
                                            public Object decorate(TnUiArgAdapter args)
                                            {
                                                int height = AppConfigHelper.getDisplayHeight()
                                                        - AppConfigHelper.getStatusBarHeight()
                                                        - ((MovingMapUiDecorator) uiDecorator).NAV_TITLE_HEIGHT.getInt()
                                                        - ((MovingMapUiDecorator) uiDecorator).STREET_NAME_HEIGHT.getInt();
                                                return PrimitiveTypeCache.valueOf(height);
                                            }
                                        });

                                TnUiArgAdapter popupYLocation = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0),
                                        new ITnUiArgsDecorator()
                                        {
                                            public Object decorate(TnUiArgAdapter args)
                                            {
                                                int height = (((AndroidUiHelper) AndroidUiHelper.getInstance())
                                                        .getStatusBarHeight(0)
                                                        + ((MovingMapUiDecorator) uiDecorator).NAV_TITLE_HEIGHT.getInt() - ((MovingMapUiDecorator) uiDecorator).STREET_NAME_HEIGHT
                                                        .getInt());
                                                int locationY = height % 2 == 0 ? height / 2 : (height + 1) / 2;
                                                return PrimitiveTypeCache.valueOf(locationY);
                                            }
                                        });

                                DwfSliderPopup.getInstance().show(parent, mapHeight, popupYLocation);
                                dwfAidl.setShareLocationInterval(10000);
                                dwfAidl.addUpdateListener(friendsAnnotationHandler, friendsAnnotationHandler.toString());
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                return;
            }
        }
    }

    private BottomContainerArgs createBottomBarArgs()
    {
        return createBottomBarArgs(false);
    }

    private BottomContainerArgs createBottomBarArgs(boolean isArrivalDestinationMode)
    {
        BottomContainerArgs args = new BottomContainerArgs(CMD_NONE);

        args.cmdIds = new int[5];
        args.cmdIds[0] = CMD_NONE;
        args.cmdIds[1] = CMD_ROUTE_SUMMARY;
        args.cmdIds[2] = CMD_COMMON_DSR;
        boolean isOnboard = NavRunningStatusProvider.getInstance().isOnBoardRoute()
                || !NetworkStatusManager.getInstance().isConnected();
        args.cmdIds[3] = CMD_COMMON_LINK_TO_SEARCH;
        args.enableIcons[3] = isOnboard ? false : true;
        args.cmdIds[4] = CMD_COMMON_BACK;

        if (isArrivalDestinationMode)
        {
            args.enableIcons[0] = false;
            args.enableIcons[1] = false;
            args.enableIcons[2] = false;
            args.enableIcons[3] = false;
        }

        args.displayStrResIds = new int[5];
        args.displayStrResIds[0] = IStringCommon.RES_NAVIGATION;
        args.displayStrResIds[1] = IStringCommon.RES_ROUTE;
        // args.displayStrResIds[2] = IStringCommon.RES_BTTN_DSR; For dsr btn, there's no string
        args.displayStrResIds[3] = IStringCommon.RES_NEARBY;
        args.displayStrResIds[4] = IStringCommon.RES_BTTN_EXIT;

        args.unfocusImageAdapters = new TnUiArgAdapter[5];
        args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_UNFOCUS;
        args.unfocusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_UNFOCUS;
        args.unfocusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.unfocusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_UNFOCUS;
        args.unfocusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_UNFOCUS;

        args.focusImageAdapters = new TnUiArgAdapter[5];
        args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_FOCUS;
        args.focusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_FOCUS;
        args.focusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.focusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_FOCUS;
        args.focusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_FOCUS;
        
        args.disableImageAdapters = new TnUiArgAdapter[5];
        args.disableImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_DISABLE;
        if (isArrivalDestinationMode && !isOnboard)
        {
            args.disableImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_UNFOCUS;
        }
        return args;
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        switch (model.getState())
        {
            case STATE_MOVING_MAP:
            {
                int type = tnUiEvent.getType();
                if (type == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    TnMotionEvent event = tnUiEvent.getMotionEvent();
                    if (event != null && event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        if (tnUiEvent.getComponent() instanceof NavZoomButton)
                        {
                            updateMapPopupTimeStamp();
                        }
                        else if (tnUiEvent.getComponent() != null && tnUiEvent.getComponent().getRoot() != null)
                        {
                            handleTapAction(tnUiEvent.getComponent().getRoot().getRootContainer());
                        }
                    }
                }
                break;
            }
            case STATE_END_TRIP_MODE:
            {
                int type = tnUiEvent.getType();
                if (type == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
                    if (commandEvent != null)
                    {
                        int command = commandEvent.getCommand();
                        if (command == CMD_FAVORITE_NOTIFACTION_HIDE)
                        {
                            notification = null;
                            return true;
                        }
                    }
                }
                break;
            }
            case STATE_COMMON_EXIT:
            {
                int type = tnUiEvent.getType();
                if (type == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
                    if (commandEvent != null)
                    {
                        int command = commandEvent.getCommand();
                        if (command == CMD_COMMON_EXIT)
                        {
                            return true;
                        }
                    }
                }
                break;
            }
        }

        CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                .getCurrentScreen();
        if (currentScreen.getId() != STATE_MOVING_MAP)
        {
            return true;
        }

        if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
        {
            TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
            if (commandEvent != null)
            {
                int command = commandEvent.getCommand();
                if (command == CMD_BOTTOM_HIDE)
                {
                    if (!isEndTripShown())
                    {
                        if (bottomPopup != null)
                        {
                            bottomPopup.hideImmediately();
                            bottomPopup = null;
                        }
                    }
                    zoomPopup = null;
                    trafficSummaryButtonPopup = null;
                    isLastNotShowOnboard = false;
                    relocateMapLogoProvider(((MovingMapUiDecorator) uiDecorator).MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR);
                    return true;
                }
            }
        }

        return false;
    }

    long lastActionTime;

    protected boolean isMapPopupExist()
    {
        int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
        boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                || trafficEnableValue == FeaturesManager.FE_PURCHASED;
        Preference routeTypePref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        if (isLastNotShowOnboard || !isTrafficEnabled
                || (routeTypePref != null && routeTypePref.getIntValue() == Route.ROUTE_PEDESTRIAN))
        {
            return bottomPopup != null && zoomPopup != null;
        }
        else
        {
            return bottomPopup != null && zoomPopup != null && trafficSummaryButtonPopup != null;
        }
    }

    protected void handleTapAction(final AbstractTnComponent rootContainer)
    {
        long now = System.currentTimeMillis();

        if (now - lastActionTime < 1000)
        {
            return;
        }
        lastActionTime = now;

        Runnable run = new Runnable()
        {
            public void run()
            {
                if (isMapPopupExist())
                {
                    if (bottomPopup.isInAnimation())
                    {
                        return;
                    }
                    if (bottomPopup.isShown())
                    {
                        updateMapPopupTimeStamp();
                    }
                    else
                    {
                        showMapPopup(rootContainer, NO_ACTION_TIMEOUT);
                    }
                }
                else if (bottomPopup == null)
                {
                    showMapPopup(rootContainer, NO_ACTION_TIMEOUT);
                }
            }
        };

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_MOVING_MAP:
            {
                switch (commandId)
                {
                    case CMD_TRAFFIC_SUMMARY:
                    {
                        NavState navState = NavSdkNavEngine.getInstance().getCurrentNavState();
                        if (navState == null)
                        {
                            return false;
                        }
                        break;
                    }
                    case CMD_COMMON_BACK:
                    {
                        if (MapContainer.getInstance().getFeature(ID_AD_CONTAINER) != null)
                        {
                            if (!isKeyBack)
                                model.put(KEY_B_GOTO_EXIT_CONFIRM, true);
                            hideVbbManually();
                        }
                        else
                        {
                            model.put(KEY_B_GOTO_EXIT_CONFIRM, true);
                        }
                        break;
                    }
                }
                break;
            }
            case STATE_END_TRIP_MODE:
            {
                switch (commandId)
                {
                    case CMD_ADD_TO_FAVORITE:
                    {
                        Address destAddress = (Address) model.get(KEY_O_ADDRESS_DEST);
                        if (destAddress == null)
                        {
                            break;
                        }

                        byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(destAddress);
                        Address newAddress = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
                        if (newAddress.getPoi() == null)
                        {
                            newAddress.setType(Address.TYPE_FAVORITE_STOP);
                        }
                        else
                        {
                            newAddress.setType(Address.TYPE_FAVORITE_POI);
                        }
                        newAddress.setSource(Address.SOURCE_FAVORITES);

                        DaoManager.getInstance().getAddressDao().checkAddressLabel(newAddress);

                        TnLinearContainer endTripContainer = (TnLinearContainer) MapContainer.getInstance().getFeature(
                            ID_END_TRIP_CONTAINER);

                        if (endTripContainer == null)
                        {
                            break;
                        }

                        Address favoriteAddr = DaoManager.getInstance().getAddressDao()
                                .getMatchedFavoriteAddress(newAddress, true);
                        String notificationString = null;
                        if (favoriteAddr != null)
                        {
                            DaoManager.getInstance().getAddressDao().deleteAddress(favoriteAddr);
                            FrogButton addToFavButton = (FrogButton) endTripContainer
                                    .getComponentById(ID_END_TRIP_ADD_FAV_BUTTON);

                            notificationString = ResourceManager.getInstance().getCurrentBundle()
                                    .getString(IStringNav.RES_DEST_DELETED_FROM_FAV, IStringNav.FAMILY_NAV);
                            if (addToFavButton != null)
                            {

                                addToFavButton.setIcon(ImageDecorator.IMG_FAVORITES_ICON_FOCUSED.getImage(),
                                    ImageDecorator.IMG_FAVORITES_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.LEFT);
                            }
                        }
                        else
                        {
                            newAddress.setCreateTime(System.currentTimeMillis());
                            DaoManager.getInstance().getAddressDao().addAddress(newAddress, false);

                            notificationString = ResourceManager.getInstance().getCurrentBundle()
                                    .getString(IStringNav.RES_DEST_ADDED_TO_FAV, IStringNav.FAMILY_NAV);

                            FrogButton addToFavButton = (FrogButton) endTripContainer
                                    .getComponentById(ID_END_TRIP_ADD_FAV_BUTTON);
                            if (addToFavButton != null)
                            {

                                addToFavButton.setIcon(ImageDecorator.IMG_FAVORITES_ADD_ICON_FOCUSED.getImage(),
                                    ImageDecorator.IMG_FAVORITES_ADD_ICON_UNFOCUSED.getImage(), AbstractTnGraphics.LEFT);
                            }

                        }
                        showFavoriteNotification(notificationString);
                        DaoManager.getInstance().getAddressDao().store();
                        if (favoriteAddr == null)
                        {
                            logAddPlaceMisLog(newAddress);
                        }

                        break;
                    }
                    case CMD_COMMON_BACK:
                    {
                        if (notification != null)
                        {
                            notification.hideImmediately();
                            notification = null;
                            return false;
                        }
                        break;
                    }
                }
                break;
            }
        }
        return super.prepareModelData(state, commandId);
    }
    
    private void logAddPlaceMisLog(Address address)
    {
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
        if(address.getPoi() != null)
        {
            int index = -1;
            if(address.getPoi().getBizPoi() != null && poiDataWrapper != null)
            {
                index= poiDataWrapper.getIndexOfMixedListByPoiId(address.getPoi().getBizPoi().getPoiId());
            }
            if(index >= 0)
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_ADD_PLACE, index);
            }
            else
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_ADD_PLACE, address.getPoi());
            }
        }        
    }

    protected void showFavoriteNotification(String notificationString)
    {
        if (notification != null)
        {
            notification.hideImmediately();
            notification = null;
        }

        if (notificationString != null)
        {
            notification = UiFactory.getInstance().createSlidableContainer(0);
            notification.setTimeout(NOTIFICATION_TIMEOUT, CMD_FAVORITE_NOTIFACTION_HIDE);
            notification.setAnimationDuration(0, 0);
            notification.setCommandEventListener(this);
            notification.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((MovingMapUiDecorator) uiDecorator).END_TRIP_FAVORITE_NOTIFICATION_WIDTH);

            TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, false,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((MovingMapUiDecorator) uiDecorator).END_TRIP_FAVORITE_NOTIFICATION_WIDTH);
            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);

            final FrogMultiLine text = UiFactory.getInstance().createMultiLine(0, notificationString);
            text.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((MovingMapUiDecorator) uiDecorator).END_TRIP_FAVORITE_NOTIFICATION_TEXT_WIDTH);
            text.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
            int whiteColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
            text.setForegroundColor(whiteColor);
            text.setPadding(0, 0, 0, 0);
            text.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MESSAGE_BOX));
            text.sublayout(0, 0);
            container.add(text);

            final int containerPadding = ((MovingMapUiDecorator) uiDecorator).END_TRIP_FAVORITE_NOTIFICATION_PADDING.getInt();
            container.setPadding(containerPadding, containerPadding, containerPadding, containerPadding);

            notification.setContent(container);
            notification.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        int height = text.getPreferredHeight() + containerPadding * 2;
                        return PrimitiveTypeCache.valueOf(height);
                    }
                }));

            int x = ((MovingMapUiDecorator) uiDecorator).END_TRIP_FAVORITE_NOTIFICATION_X.getInt();
            int y;

            int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
            int screen_height = AppConfigHelper.getDisplayHeight();
            if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            {
                y = (screen_height - notification.getPreferredHeight()) / 2 + AppConfigHelper.getTopBarHeight() / 2;
            }
            else
            {
                int navTitleHeight = ((MovingMapUiDecorator) uiDecorator).NAV_TITLE_HEIGHT.getInt();
                int endTripHeight = ((MovingMapUiDecorator) uiDecorator).NAV_END_TRIP_HEIGHT.getInt();
                int spareSpaceHeight = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                        - navTitleHeight - endTripHeight - uiDecorator.BOTTOM_BAR_HEIGHT.getInt();
                int spareSpaceY = AppConfigHelper.getStatusBarHeight() + navTitleHeight + endTripHeight;
                int padding = (spareSpaceHeight - notification.getPreferredHeight()) / 2;
                if (padding < 0)
                {
                    padding = 0;
                }
                y = spareSpaceY + padding;
            }

            int width = ((MovingMapUiDecorator) uiDecorator).END_TRIP_FAVORITE_NOTIFICATION_WIDTH.getInt();
            int height = notification.getPreferredHeight();
            notification.showAt(MapContainer.getInstance(), x, y, width, height, false);
        }
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    boolean isAdiMode;

    public boolean isAdiMode()
    {
        return isAdiMode;
    }

    public void setAdi(final MapContainer mapContainer, final int adiLat, final int adiLon)
    {
        if (isAdiMode)
        {
            return;
        }

        mapContainer.followVehicle();
        mapContainer.enableAdi(true);
        mapContainer.setRenderingMode(NavSdkMapProxy.RENDERING_MODE_2D_NORTH_UP);
        isAdiMode = true;
    }
    
    private void setZoomButtonNeedSync()
    {
        CitizenSlidableContainer zoomPopup = this.zoomPopup;
        if (zoomPopup != null)
        {
            TnLinearContainer innerContainer = (TnLinearContainer) zoomPopup.getContent();
            NavZoomButton zoomInBtn = (NavZoomButton) innerContainer.getComponentById(ID_ZOOM_IN_BUTTON);
            if (zoomInBtn != null)
            {
                zoomInBtn.setNeedSync(true);
            }
            NavZoomButton zoomOutBtn = (NavZoomButton) innerContainer.getComponentById(ID_ZOOM_OUT_BUTTON);
            if (zoomOutBtn != null)
            {
                zoomOutBtn.setNeedSync(true);
            }
        }
    }

    public void removeAdi(final MapContainer mapContainer)
    {
        if (!isAdiMode)
        {
            return;
        }

        mapContainer.enableAdi(false);
        mapContainer.setRenderingMode(mapContainer.getRenderingModeFromTripDao());
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
        {
            mapContainer.setZoomLevel(ZOOM_LEVEL_DEFAULT_NAV_LANDSCAPE);
        }
        else
        {
            mapContainer.setZoomLevel(ZOOM_LEVEL_DEFAULT_NAV);
        }
        mapContainer.followVehicle();
        isAdiMode = false;
    }

    private void showTrafficAlert(TrafficAlertEvent event)
    {
        if (trafficSummaryButtonPopup != null)
        {
            trafficSummaryButtonPopup.hideImmediately();
            trafficSummaryButtonPopup = null;
        }

        if (trafficAlertButtonPopup == null)
        {
            trafficAlertButtonPopup = UiFactory.getInstance().createSlidableContainer(ID_TRAFFIC_BUTTON_CONTAINER);
            NavTrafficAlertContainer navTrafficAlertContainer = new NavTrafficAlertContainer(ID_TRAFFIC_ALERT_CONTAINER);
            navTrafficAlertContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.NAV_ALERT_BG);
            navTrafficAlertContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.NAV_ALERT_BG);
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", CMD_SHOW_INCIDENT_DETAIL);
            navTrafficAlertContainer.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            navTrafficAlertContainer.setCommandEventListener(this);
            trafficAlertButtonPopup.setContent(navTrafficAlertContainer);
        }
        NavTrafficAlertContainer navTrafficAlertContainer = (NavTrafficAlertContainer) trafficAlertButtonPopup.getContent();
        navTrafficAlertContainer.update(event);

        navTrafficAlertContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_WIDTH);
        navTrafficAlertContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_HEIGHT);
        int trafficX = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_X.getInt();
        int trafficY = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_Y.getInt();
        if (isNextNextTurnShown)
        {
            trafficY += ((MovingMapUiDecorator) uiDecorator).NEXT_NEXT_TURN_HEIGHT.getInt();
        }
        int trafficWidth = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_WIDTH.getInt();
        int trafficHeight = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_HEIGHT.getInt();
        if (model.isActivated())
        {
            if (trafficAlertButtonPopup.isShown())
            {
                trafficAlertButtonPopup.update(null, trafficX, trafficY, trafficWidth, trafficHeight);
            }
            else
            {
                trafficAlertButtonPopup.showAt(MapContainer.getInstance(), trafficX, trafficY, trafficWidth, trafficHeight,
                    false);
            }
        }

        updateTrafficIncident(event);
    }

    private void hideTrafficAlert(NavParameter param)
    {
        if (trafficAlertButtonPopup != null)
        {
            trafficAlertButtonPopup.hideImmediately();
            trafficAlertButtonPopup = null;
        }
        
        //avoid cleanning annotation too frequently
        if (param.alertEvent == null && lastAlertEvent == null)
        {
            return;
        }
        MapContainer.getInstance().removeIncidentAnnotations();
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {

        if (isAppPaused || !this.model.isActivated())
        {
            return false;
        }

        switch (state)
        {
            case STATE_MOVING_MAP:
            {
                addSliderMenuPopup(screen);
                
                if (Logger.DEBUG)
                    Logger.log(Logger.INFO, this.getClass().getName(), "updating moving map");

                if (!MapContainer.getInstance().isDisplayingModelVehicle() && this.model.isActivated())
                {
                    MapContainer.getInstance().changeToModelVehicleAnnotation();
                }

                if (model.fetchBool(KEY_B_IS_BACK_FROM_VBB_DETAIL))
                {
                    BillboardAd currentAd = this.currentAd;
                    if (currentAd != null && !currentAd.isEnded())
                    {
                        NavAdManager.getInstance().logImpressionEnd(currentAd,
                            IMisLogConstants.VALUE_BILLBOARD_EXIT_TYPE_FORCED);
                        currentAd.updateState(BillboardAd.STATE_DETAIL_VIEW);
                    }
                }

                NavParameter param = (NavParameter) model.get(KEY_O_NAV_PARAMETER);
                if (param != null)
                {
                    AbstractTnComponent comp = MapContainer.getInstance().getFeature(ID_NAV_TITLE);

                    if (comp == null)
                    {
                        createMapContainer((CitizenScreen) screen);
                        comp = MapContainer.getInstance().getFeature(ID_NAV_TITLE);
                    }

                    if (comp != null)
                    {
                        ((NavTopComponent) comp).updateNavStatus(param);
                    }
                    
                    updateNextNextTurn();

                    NavSpeedLimitComponent speedLimit = (NavSpeedLimitComponent) MapContainer.getInstance().getFeature(
                        ID_SPEED_LIMIT_CONTAINER);

                    if (speedLimit != null)
                    {
                        speedLimit.updateSpeedLimit(param.isSpeedLimitExceeded, param.speedLimit);
                    }

                    showTrafficIncident(param);

                    NavCompassComponent compass = (NavCompassComponent) MapContainer.getInstance().getFeature(
                        ID_COMPASS_CONTAINER);

                    if (compass != null)
                    {
                        VehiclePosition vp = (VehiclePosition) this.model.get(KEY_O_VEHICLE_POSITION);
                        if (vp != null)
                        {
                            compass.update((int) vp.getHeading());
                        }
                    }

                    if (NavSdkNavEngine.getInstance().isRunning())
                    {
                        RouteUiHelper.updateCurrentRoute(MapContainer.getInstance());
                        RouteUiHelper.showTurnArrow(NavSdkRouteWrapper.getInstance().getCurrentRouteId(),
                            param.nextSegmentIndex, MapContainer.getInstance());
                        if (!this.hasAdjustFlag)// need adjust the destination flag
                        {
                            checkFlags();
                        }
                    }
                    if (param.isAdi)
                    {
                        if (MapContainer.getInstance().getInteractionMode() == IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
                        {
                            MovingmapSyncZoomLevelTask task = new MovingmapSyncZoomLevelTask();
                            syncZoomLevel(100, task);
                        }
                        setAdi(MapContainer.getInstance(), param.adiLat, param.adiLon);
                    }
                    else
                    {
                        removeAdi(MapContainer.getInstance());
                    }
                    NavBottomStatusBarHelper.getInstance().setIsNoGps(param.isNoSatellite);
                    NavBottomStatusBarHelper.getInstance().setIsOutofCoverage(param.isOutOfCoverage);

                    if (isLastNoGpsSignal != param.isNoSatellite)
                    {
                        isLastNoGpsSignal = param.isNoSatellite;
                        if (param.isNoSatellite)
                        {
                            CarModelManager.getInstance().loadGreyCarModel();
                        }
                        else
                        {
                            CarModelManager.getInstance().loadCarModel();
                        }
                    }

                    CurrentStreetNameComponent currentStreetNameComponent = (CurrentStreetNameComponent) MapContainer
                            .getInstance().getFeature(ID_CURRENT_STREET_NAME);

                    if (currentStreetNameComponent != null)
                    {
                        currentStreetNameComponent.update(param.currStreetName, param.laneInfos, param.laneTypes, param.isAdi);
                    }

                    if (param.hideVBB)
                    {
                        hideVbbManually();
                        param.hideVBB = false;
                    }
                    else if (this.currentAd == null || this.currentAd.isEnded() || this.currentAd.isDisplayTimeout())
                    {
                        BillboardAd currentAd = NavAdManager.getInstance().getCurrentAd();
                        if (currentAd == null || currentAd.isEnded() || currentAd.isDisplayTimeout())
                        {
                            if (MapContainer.getInstance().getFeature(ID_AD_CONTAINER) != null)
                            {
                                this.hideVBB();
                                if (currentAd != null && currentAd.isDisplayTimeout())
                                {
                                    NavAdManager.getInstance().logImpressionEnd(currentAd,
                                        IMisLogConstants.VALUE_BILLBOARD_EXIT_TYPE_NATURAL);
                                }
                            }
                        }
                        else
                        {
                            if (currentAd != this.currentAd)
                            {
                                this.hideVBB();
                                isLoadingAd = false;
                                this.currentAd = currentAd;
                                model.put(KEY_O_BILLBOARD_AD, currentAd);
                                this.showAd();
                            }
                            else if (MapContainer.getInstance().getFeature(ID_AD_CONTAINER) == null && !isLoadingAd)
                            {
                                this.showAd();
                            }
                        }
                    }

                    if (isReFollowVechicle || MapContainer.getInstance().getInteractionMode() != IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
                    {
                        if (isReFollowVechicle || (System.currentTimeMillis() - MapContainer.getInstance().getLastControlEventTime()) > 15000)
                        {
                            MapContainer.getInstance().followVehicle();
                            if (!isReFollowVechicle)
                            {
                                MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getTransitionTime());
                            }

                            isReFollowVechicle = false;
                        }
                    }

                    if (!isInitialPopupShown)
                    {
                        // Fix bug http://jira.telenav.com:8080/browse/TNANDROID-1507
                        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeOptionsMenu();
                        this.handleTapAction(MapContainer.getInstance());
                        isInitialPopupShown = true;
                    }
                }

                if (MapContainer.getInstance().getFeature(ID_END_TRIP_CONTAINER) != null)
                {
                    MapContainer.getInstance().removeFeature(ID_END_TRIP_CONTAINER);
                    if (bottomPopup != null)
                    {
                        bottomPopup.hideImmediately();
                        this.bottomPopup = null;
                    }
                }

                return false;
            }
        }
        return false;
    }

    private void updateNavInfo(NavParameter param, TnScreen screen)
    {
        if (!MapContainer.getInstance().isDisplayingModelVehicle() && this.model.isActivated())
        {
            MapContainer.getInstance().changeToModelVehicleAnnotation();
        }

        if (param != null)
        {
            AbstractTnComponent comp = MapContainer.getInstance().getFeature(ID_NAV_TITLE);

            if (comp == null)
            {
                createMapContainer((CitizenScreen) screen);
                comp = MapContainer.getInstance().getFeature(ID_NAV_TITLE);
            }

            if (comp != null)
            {
                ((NavTopComponent) comp).updateNavStatus(param);
            }
            
            updateNextNextTurn();

            NavSpeedLimitComponent speedLimit = (NavSpeedLimitComponent) MapContainer.getInstance().getFeature(
                ID_SPEED_LIMIT_CONTAINER);

            if (speedLimit != null)
            {
                speedLimit.updateSpeedLimit(param.isSpeedLimitExceeded, param.speedLimit);
            }

            showTrafficIncident(param);

            NavCompassComponent compass = (NavCompassComponent) MapContainer.getInstance().getFeature(ID_COMPASS_CONTAINER);

            if (compass != null)
            {
                VehiclePosition vp = (VehiclePosition) this.model.get(KEY_O_VEHICLE_POSITION);
                if (vp != null)
                {
                    compass.update((int) vp.getHeading());
                }
            }

            if (NavSdkNavEngine.getInstance().isRunning())
            {
                RouteUiHelper.updateCurrentRoute(MapContainer.getInstance());
                RouteUiHelper.showTurnArrow(NavSdkRouteWrapper.getInstance().getCurrentRouteId(), param.nextSegmentIndex,
                    MapContainer.getInstance());
                if (!this.hasAdjustFlag)// need adjust the destination flag
                {
                    checkFlags();
                }
            }
            if (param.isAdi)
            {
                setAdi(MapContainer.getInstance(), param.adiLat, param.adiLon);
            }
            else
            {
                removeAdi(MapContainer.getInstance());
            }

            NavBottomStatusBarHelper.getInstance().setIsNoGps(param.isNoSatellite);
            NavBottomStatusBarHelper.getInstance().setIsOutofCoverage(param.isOutOfCoverage);

            CurrentStreetNameComponent currentStreetNameComponent = (CurrentStreetNameComponent) MapContainer.getInstance()
                    .getFeature(ID_CURRENT_STREET_NAME);

            if (currentStreetNameComponent != null)
            {
                currentStreetNameComponent.update(param.currStreetName, param.laneInfos, param.laneTypes, param.isAdi);
            }

            if (MapContainer.getInstance().getInteractionMode() != IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
            {
                if (!isAppPaused && ((System.currentTimeMillis() - MapContainer.getInstance().getLastControlEventTime()) > 15000))
                {
                    MapContainer.getInstance().followVehicle();
                    MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getTransitionTime());
                }
            }

            if (param.hideVBB)
            {
                hideVbbManually();
                param.hideVBB = false;
            }
        }
    }

    private void showTrafficIncident(NavParameter param)
    {
        TrafficAlertEvent event = param.alertEvent;
        if (event != null && event.getDistance() > 0 /* && event.getTrafficIncident() != null */)
        {
            int incidentType = event.getIncidentType();
            // for camera and speed trap , just show it on map , but not show the icon on screen
            if (incidentType != TrafficIncident.TYPE_CAMERA && incidentType != TrafficIncident.TYPE_SPEED_TRAP)
            {
                showTrafficAlert(event);
            }
            else
            {
                updateTrafficIncident(event);
            }
        }
        else
        {
            hideTrafficAlert(param);
        }
        lastAlertEvent = event;
    }

    private void checkFlags()
    {
        Route currentRoute = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        int[] latlon =
        { Integer.MIN_VALUE, Integer.MIN_VALUE };
        if (NavSdkNavEngine.getInstance().isRunning() && RouteUiHelper.isGetAllData(currentRoute))
        {
            latlon = currentRoute.getDestLatLon();
            hasAdjustFlag = true;
        }
        else
        {
            if (destFlag == null)
            {
                Address dest = (Address) model.get(KEY_O_ADDRESS_DEST);
                if (dest.getStop() != null)
                {
                    Stop destStop = dest.getStop();
                    if (destStop != null)
                    {
                        latlon[0] = destStop.getLat();
                        latlon[1] = destStop.getLon();
                    }
                }
            }
        }

        if (latlon[0] != Integer.MIN_VALUE && latlon[1] != Integer.MIN_VALUE)
        {
            double latD = latlon[0] / 100000.0d;
            double lonD = latlon[1] / 100000.0d;

            AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();
            if (destFlag != null)
            {
                MapContainer.getInstance().removeFeature(this.destFlag);
                float pivotX = 0.5f;
                float pivotY = 0;
                ImageAnnotation image = new ImageAnnotation(destFlag, latD, lonD, pivotX, pivotY,
                        ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION, IMapContainerConstants.ANNOTATION_SPRITE);
                this.destFlag = image;
                MapContainer.getInstance().addFeature(image);
            }
        }
    }

    private void removeFlag()
    {
        if (destFlag != null)
        {
            MapContainer.getInstance().removeFeature(destFlag);
            destFlag = null;
            hasAdjustFlag = false;
        }
    }

    private String getAdUrl(BillboardAd ad)
    {
        if (ad == null)
        {
            return null;
        }

        String url = DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_BILLBOARD_HOST);

        if (url != null && url.trim().length() > 0)
        {
            url += IBrowserSdkConstants.BROWSER_VBB_URL_DIR_STR;
        }
        else
        {
            BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.VBB_URL_DOMAIN_ALIAS);
            url = sessionArgs.getUrl();
        }

        url = BrowserSdkModel.addEncodeTnInfo(url, "");
        url = BrowserSdkModel.appendWidthHeightToUrl(url);

        return url;
    }

    private void changeCameraDeclination()
    {
        NavParameter param = (NavParameter) model.get(KEY_O_NAV_PARAMETER);
        if (Logger.DEBUG)
        {
            if(param == null)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "changeCameraDeclination: param is null");
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "changeCameraDeclination: param.isAdi --- " + param.isAdi);
            }
        }
        if (!this.model.getBool(KEY_B_IS_JUNCTION_VIEW) && param != null && !param.isAdi)
        {
            boolean isPortrait = (((AbstractTnUiHelper) (AbstractTnUiHelper.getInstance())).getOrientation()) == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
            final Vector cameraDeclination = this.model.getVector(KEY_V_DEFAULT_DECLINATION_BEFORE_JUNCTION_VIEW);
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "changeCameraDeclination: isPortrait --- " + isPortrait);
            }
            if (cameraDeclination.size() > 1)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "changeCameraDeclination: portrait Declination--- "
                            + cameraDeclination.elementAt(0));
                    Logger.log(Logger.INFO, this.getClass().getName(), "changeCameraDeclination: landscap Declination --- "
                            + cameraDeclination.elementAt(1));
                }
                final int index = isPortrait ? 0 : 1;
                Float declinationValue = (Float) cameraDeclination.elementAt(index);
                MapContainer.getInstance().setCameraDeclination(declinationValue.floatValue());
            }
        }
    }

    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        switch (model.getState())
        {
            case STATE_MOVING_MAP:
            {
                hideMapPopup();
                changeZoomLevelOnMode();
                relocateMapLogoProvider(((MovingMapUiDecorator) uiDecorator).MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR);
                
                if (DwfSliderPopup.getInstance().isShowing())
                {
                    DwfSliderPopup.getInstance().update();
                }
                break;
            }
            case STATE_END_TRIP_MODE:
            {
                final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();

                if (oldOrientation != orientation)
                {
                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            CitizenScreen screen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                                    .getCurrentScreen();
                            createMapContainer(screen);
                        }
                    });
                }

                oldOrientation = orientation;
                super.onSizeChanged(tnComponent, w, h, oldw, oldh);
                handlePopupWhenArrival();
                relocateMapLogoProvider(uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR);
                if (notification != null)
                {
                    notification.hideImmediately();
                    notification = null;
                }
                break;
            }
            case STATE_TRAFFIC_ALERT_DETAIL:
            {
                if (zoomPopup != null && zoomPopup.isShown())
                    showMapPopup(MapContainer.getInstance(), NO_ACTION_TIMEOUT);
                break;
            }
            case STATE_DETOUR_CONFIRM:
            {
                showMapPopup(MapContainer.getInstance(), NO_ACTION_TIMEOUT);
                break;
            }
            default:
            {
                hideMapPopup();
                relocateMapLogoProvider(((MovingMapUiDecorator) uiDecorator).MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR);
                break;
            }
        }

        if (this.trafficAlertButtonPopup != null && trafficAlertButtonPopup.isShown())
        {
            trafficAlertButtonPopup.hideImmediately();
            trafficAlertButtonPopup = null;
        }
        if (backToNavPopup != null && backToNavPopup.isShown())
        {
            showBackToNavPopup();
        }
        changeCameraDeclination();
    }

    protected CitizenSlidableContainer createBottomPopup(final BottomContainerArgs bottomBarArgs)
    {
        int timeout = this.getBottomPopupTimeout();
        CitizenSlidableContainer container = UiFactory.getInstance().createSlidableContainer(ID_BOTTOM_CONTAINER);
        container.setTimeout(timeout, CMD_BOTTOM_HIDE);
        container.setCommandEventListener(this);

        AbstractTnContainer innerContainer = createBottomContainer(null, bottomBarArgs, false, false);
        innerContainer.getTnUiArgs()
                .put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);
        innerContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
            NinePatchImageDecorator.BOTTOM_NAVIGATION_NAV_BAR);

        TnUiArgs containerArgs = container.getTnUiArgs();
        containerArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.BOTTOM_BAR_HEIGHT);
        containerArgs.put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        container.setContent(innerContainer);
        return container;
    }

    private boolean isPortarit()
    {
        return ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
    }

    protected CitizenSlidableContainer createTrafficSummaryButtonPopup()
    {
        CitizenSlidableContainer container = UiFactory.getInstance().createSlidableContainer(ID_TRAFFIC_BUTTON_CONTAINER);
        container.setTimeout(NO_ACTION_TIMEOUT, CMD_BOTTOM_HIDE);
        container.setCommandEventListener(this);

        NavTrafficButton trafficButton = new NavTrafficButton(ID_TRAFFIC_BUTTON_CONTAINER);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_TRAFFIC_SUMMARY);
        trafficButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        trafficButton.setCommandEventListener(this);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.NAV_CONTROLS_BG_FOCUSED);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
            NinePatchImageDecorator.NAV_CONTROLS_BG_UNFOCUSED);

        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_WIDTH);
        trafficButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_HEIGHT);

        container.setContent(trafficButton);

        return container;
    }

    public void billboardModeChanged(int mode)
    {
        switch (mode)
        {
            case MODE_TITLE:
            {
                AbstractTnComponent billboard = MapContainer.getInstance().getFeature(ID_AD_CONTAINER);
                if (billboard != null)
                {
                    BillboardAd ad = currentAd;
                    if (ad != null)
                    {
                        NavAdManager.getInstance().logImpressionEnd(ad, IMisLogConstants.VALUE_BILLBOARD_EXIT_TYPE_MINIMIZE);
                        ad.updateState(BillboardAd.STATE_INITIAL_VIEW);
                    }
                    billboard.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).AD_HEIGHT);
                    billboard.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).AD_Y);
                    billboard.requestLayout();
                }
                break;

            }
            case MODE_FULL:
            {
                AbstractTnComponent billboard = MapContainer.getInstance().getFeature(ID_AD_CONTAINER);
                if (billboard != null)
                {
                    BillboardAd ad = currentAd;
                    if (ad != null)
                    {
                        NavAdManager.getInstance().logImpressionEnd(ad, IMisLogConstants.VALUE_BILLBOARD_EXIT_TYPE_FORCED);
                        ad.updateState(BillboardAd.STATE_DETAIL_VIEW);
                        NavAdManager.getInstance().logImpressionStart(ad);
                    }
                    billboard.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).AD_HEIGHT_EXT);
                    billboard.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).AD_Y_EXT);
                    billboard.requestLayout();
                }
                break;
            }
        }
    }

    boolean isLoadingAd;

    protected void showAd()
    {

        if (isLoadingAd)
        {
            return;
        }

        isLoadingAd = true;

        String currentAdUrl = this.getAdUrl(currentAd);

        if (currentAdUrl == null)
        {
            return;
        }
        
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        if (isOnboard)
        {
            return;
        }
        
        currentAd.setInitHeight(((MovingMapUiDecorator) uiDecorator).AD_HEIGHT);
        currentAd.setContentHeight(((MovingMapUiDecorator) uiDecorator).AD_HEIGHT_EXT);
        final CitizenWebComponent billboard = UiFactory.getInstance().createCitizenWebComponent(this, ID_AD_CONTAINER);

        billboard.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((MovingMapUiDecorator) uiDecorator).AD_X);
        billboard.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((MovingMapUiDecorator) uiDecorator).AD_Y);
        billboard.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).SCREEN_WIDTH);
        billboard.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).AD_HEIGHT);
        billboard.setKeyEventListener(this);
        billboard.setScrollable(false);
        billboard.setBackgroundColor(0);
        // billboard.setTouchEventListener(this);
        billboard.setWebBrowserEventListener(new WebBrowserEventListener()
        {
            boolean isErrorOccurred = false;
            public void onPageError(TnWebBrowserField browserField, String errorMsg)
            {
                isErrorOccurred = true;
            }

            public void onPageFinished(TnWebBrowserField browserField, String url)
            {
                if (isErrorOccurred)
                {
                    currentAd = null;
                }
                else
                {
                    if (model.getState() == STATE_MOVING_MAP)
                    {
                        showVBB(billboard);
                        BillboardAd ad = currentAd;
                        if (ad != null)
                        {
                            ad.updateState(BillboardAd.STATE_INITIAL_VIEW);
                            NavAdManager.getInstance().logImpressionStart(ad);
                        }
                    }
                }
                
                isLoadingAd = false;
            }
        });
        billboard.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler) model);
        billboard.setLayerType(CitizenWebComponent.LAYER_TYPE_SOFTWARE);
        billboard.loadUrl(currentAdUrl);
    }

    /**
     * Show virtual billboard
     */
    private void showVBB(CitizenWebComponent billboard)
    {
        if (MapContainer.getInstance().getFeature(ID_AD_CONTAINER) == null)
        {
            MapContainer.getInstance().addFeature(billboard);
        }
        if (backToNavPopup != null)
        {
            backToNavPopup.hideImmediately();
        }
    }

    /**
     * Hide virtual billboard
     */
    private void hideVBB()
    {
        if (MapContainer.getInstance().getFeature(ID_AD_CONTAINER) != null)
        {
            if (currentAd != null)
            {
                currentAd.setEnded(true);
            }
            MapContainer.getInstance().removeFeature(ID_AD_CONTAINER);
        }

        if (MapContainer.getInstance().getInteractionMode() != IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
        {
            CitizenSlidableContainer backToNavPopup = this.backToNavPopup;
            if (backToNavPopup == null)
            {
                backToNavPopup = createBackToNavPopup();
                this.backToNavPopup = backToNavPopup;
            }
            if (!backToNavPopup.isShown())
            {
                showBackToNavPopup();
            }
        }
    }

    private boolean shouldSetNeedSync()
    {
        return isAdiMode() && IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE == MapContainer.getInstance().getInteractionMode();
    }
    
    protected CitizenSlidableContainer createZoomPopup()
    {
        CitizenSlidableContainer container = UiFactory.getInstance().createSlidableContainer(ID_ZOOM_CONTAINER);
        container.setTimeout(NO_ACTION_TIMEOUT, CMD_BOTTOM_HIDE);
        container.setCommandEventListener(this);

        NavZoomButton zoomOutButton = new NavZoomButton(ID_ZOOM_OUT_BUTTON, false);
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_WIDTH);
        zoomOutButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_HEIGHT);
        zoomOutButton.setTouchEventListener(this);
        zoomOutButton.setIsHorizonLayout(isZoomHorizonLayout());
        zoomOutButton.setManualZoomLevelChangeListener((NavCompassComponent) MapContainer.getInstance().getFeature(
            ID_COMPASS_CONTAINER));
        zoomOutButton.setZoomButtonListener(this);
        if (shouldSetNeedSync())
        {
            zoomOutButton.setNeedSync(true);
        }

        NavZoomButton zoomInButton = new NavZoomButton(ID_ZOOM_IN_BUTTON, true);
        zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((MovingMapUiDecorator) uiDecorator).ZOOM_IN_WIDTH);
        zoomInButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((MovingMapUiDecorator) uiDecorator).ZOOM_IN_HEIGHT);
        zoomInButton.setTouchEventListener(this);
        zoomInButton.setIsHorizonLayout(isZoomHorizonLayout());
        zoomInButton.setManualZoomLevelChangeListener((NavCompassComponent) MapContainer.getInstance().getFeature(
            ID_COMPASS_CONTAINER));
        zoomInButton.setZoomButtonListener(this);
        if (shouldSetNeedSync())
        {
            zoomInButton.setNeedSync(true);
        }

        boolean isPortarit = isPortarit();

        if (isZoomHorizonLayout() || isPortarit || AppConfigHelper.isTabletSize())
        {
            TnLinearContainer innerContainer = UiFactory.getInstance().createLinearContainer(0, false,
                AbstractTnGraphics.VCENTER);
            innerContainer.add(zoomOutButton);
            innerContainer.add(zoomInButton);
            container.setContent(innerContainer);
        }
        else
        {
            TnLinearContainer innerContainer = UiFactory.getInstance().createLinearContainer(0, true,
                AbstractTnGraphics.VCENTER);
            innerContainer.add(zoomInButton);
            innerContainer.add(zoomOutButton);
            container.setContent(innerContainer);
        }

        // To fix http://jira.telenav.com:8080/browse/TNANDROID-4162
        // Since the native method mapEngine.setZoomlevel() is invoked as multiple thread in OpenGL,
        // the immediate getZoomlevel method will return an unexpected value. So we hardcode the
        // initial zoom level while in ADI mode.
        NavParameter param = (NavParameter) model.get(KEY_O_NAV_PARAMETER);
        if (param != null && param.isAdi)
        {
            zoomInButton.updateZoomBtnState((int) (MapContainer.getInstance().getZoomLevel() + 0.5));
        }

        return container;
    }

    protected CitizenSlidableContainer createBackToNavPopup()
    {
        CitizenSlidableContainer container = UiFactory.getInstance().createSlidableContainer(ID_BACK_TO_NAV_BUTTON);
        NavBackToNavButton navBackToNavButton = new NavBackToNavButton(ID_BACK_TO_NAV_BUTTON, MapContainer.getInstance());

        navBackToNavButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((MovingMapUiDecorator) uiDecorator).BACK_TO_NAV_BUTTON_WIDTH);
        navBackToNavButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((MovingMapUiDecorator) uiDecorator).BACK_TO_NAV_BUTTON_HEIGHT);
        navBackToNavButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
            NinePatchImageDecorator.NAV_CONTROLS_BG_UNFOCUSED);
        navBackToNavButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
            NinePatchImageDecorator.NAV_CONTROLS_BG_FOCUSED);

        navBackToNavButton.setTouchEventListener(this);

        container.setContent(navBackToNavButton);
        container.setAnimationDuration(0, 0);
        return container;
    }

    private int getBottomPopupTimeout()
    {
        if (this.isEndTripShown())
        {
            return CitizenSlidableContainer.TIMEOUT_NEVER_HIDE;
        }
        else
        {
            return NO_ACTION_TIMEOUT;
        }
    }

    protected void updateMapPopupTimeStamp()
    {
        Runnable run = new Runnable()
        {
            public void run()
            {
                if (bottomPopup != null && bottomPopup.isInAnimation())
                {
                    return;
                }

                if (bottomPopup != null && !bottomPopup.isShown())
                {
                    return;
                }

                if (bottomPopup != null)
                {
                    bottomPopup.updateTimeStamp();
                    bottomPopup.setTimeout(getBottomPopupTimeout(), CMD_BOTTOM_HIDE);
                }
                if (zoomPopup != null)
                {
                    zoomPopup.updateTimeStamp();
                    zoomPopup.setTimeout(NO_ACTION_TIMEOUT, CMD_BOTTOM_HIDE);
                }
                if (trafficSummaryButtonPopup != null)
                {
                    trafficSummaryButtonPopup.updateTimeStamp();
                    trafficSummaryButtonPopup.setTimeout(NO_ACTION_TIMEOUT, CMD_BOTTOM_HIDE);
                }
            }
        };

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }

    private void updateNextNextTurn()
    {
        if (!NavSdkNavEngine.getInstance().isRunning())
        {
            return;
        }
        Runnable run = new Runnable()
        {
            
            @Override
            public void run()
            {
                NavParameter navParam = (NavParameter) model.get(KEY_O_NAV_PARAMETER);
                if (navParam != null && navParam.isNextNextTurnValid() && isTightTurn(navParam.nextSegmentIndex, navParam.distanceToTurn) && !isEndTripShown())
                {
                    showNextNextTurn(navParam);
                }
                else
                {
                    hideNextNextTurn();
                }
            }
        };

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }
    
    private void showNextNextTurn(NavParameter navParam)
    {
        if (!isNextNextTurnShown)
        {
            isNextNextTurnShown = true;
            if (isMapPopupExist())
            {
                showMapPopup(MapContainer.getInstance(), NO_ACTION_TIMEOUT);
            }
        }
        NavNextNextTurnComponent nextNextTurn = (NavNextNextTurnComponent) MapContainer.getInstance().getFeature(ID_NEXT_NEXT_TURN_COMPONENT);
        if (nextNextTurn == null)
        {
            nextNextTurn = addNextNextTurnContainer(MapContainer.getInstance());
        }
        nextNextTurn.updateStatus(navParam);
    }
    
    private void hideNextNextTurn()
    {
        NavNextNextTurnComponent nextNextTurn = (NavNextNextTurnComponent) MapContainer.getInstance().getFeature(ID_NEXT_NEXT_TURN_COMPONENT);
        if (nextNextTurn != null)
        {
            MapContainer.getInstance().removeFeature(ID_NEXT_NEXT_TURN_COMPONENT);
        }
        if (isNextNextTurnShown)
        {
            isNextNextTurnShown = false;
            if (isMapPopupExist() && bottomPopup.isShown())
            {
                showMapPopup(MapContainer.getInstance(), NO_ACTION_TIMEOUT);
            }
        }
    }
        
    private boolean isTightTurn(int nextTurnIndex, int distanceToTurn)
    {
        NavSdkRoute route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (route != null)
        {
            Segment[] segments = route.getSegments();
            return necessity(nextTurnIndex, segments, distanceToTurn);
        }
        return false;
    }
    
    // Rules
    // #1 - if the turn after current turn is less then distance like 1mi, show the turn assist
    // #2 - If #1 is true, check with current turn distance
    private boolean necessity(int turnIndex, Segment[] turns, int distanceToTurn)
    {
        if (0 == turnIndex)
            return false;
        turnIndex--;
        if (turnIndex < turns.length)
        {
            Segment segment = turns[turnIndex];
            boolean isTightTurn = isTightTurnForRouteTurn(segment);
            if (isTightTurn)
            {
                boolean[] audioExist = new boolean[]
                { false };
                boolean ready = isPrepareActionGuidancePlayed(audioExist);
                if (ready)
                {
                    return true;
                }
                if (!audioExist[0])
                {
                    return isGoodTimeToShowTightTurn(distanceToTurn);
                }
                return false;
            }
        }
        return false;
    }

    boolean isPrepareActionGuidancePlayed(boolean[] audioExist)
    {
        VoiceGuidancePlayNotification voiceGuidance = (VoiceGuidancePlayNotification) model.get(KEY_O_VOICE_GUIDANCE);
        if (voiceGuidance == null || !voiceGuidance.hasGuidanceType())
        {
            audioExist[0] = false;
            return false;
        }

        audioExist[0] = true;
        int type = voiceGuidance.getGuidanceType().getNumber();
        if (VoiceGuidanceType.VoiceGuidanceType_Action_VALUE == type
                || VoiceGuidanceType.VoiceGuidanceType_Prepare_VALUE == type)
        {
            return true;
        }

        return false;
    }

    private boolean isTightTurnForRouteTurn(Segment turn)
    {
        if (turn != null)
        {
            return turn.isTightTurn();
        }
        return false;
    }

    private boolean isGoodTimeToShowTightTurn(float nextTurnDistance)
    {
        float seconds = estimatedTimeInSeconds(nextTurnDistance);
        return (seconds <= THRESHOLD_FOR_SHOWING_TIGHT_TURN_IN_SECONDS);
    }

    private float estimatedTimeInSeconds(float distance)
    {
        float speed = 1;
        VehiclePosition vehiclePos = (VehiclePosition) model.get(KEY_O_VEHICLE_POSITION);
        if (vehiclePos != null)
        {
            speed = vehiclePos.getSpeed();
        }
        if (speed < MIN_SPEED_THRESHOLD_IN_METER_PER_SECOND)
        {
            speed = MIN_SPEED_THRESHOLD_IN_METER_PER_SECOND;
        }

        float seconds = distance / speed;
        return seconds;
    }

    
    protected void showMapPopup(final AbstractTnComponent rootContainer, final int timeout)
    {
        // fix bug TN-1207
        if (!NavSdkNavEngine.getInstance().isRunning())
        {
            return;
        }

        Runnable run = new Runnable()
        {
            public void run()
            {
                hideMapPopup();

                CitizenSlidableContainer bottomPopup = createBottomPopup(createBottomBarArgs());
                CitizenSlidableContainer zoomPopup = createZoomPopup();
                zoomPopup.setTimeout(timeout, CMD_BOTTOM_HIDE);
                zoomPopup.setAnimationDuration(0, 0);

                int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
                boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                        || trafficEnableValue == FeaturesManager.FE_PURCHASED;
                // isTrafficEnabled = false;

                CitizenSlidableContainer trafficSummaryButtonPopup = null;
                Preference routeTypePref = DaoManager.getInstance().getTripsDao()
                        .getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
                boolean isOnboard = NavRunningStatusProvider.getInstance().isOnBoardRoute()
                        || !NetworkStatusManager.getInstance().isConnected();
                
                if (isTrafficEnabled && !isOnboard)
                {
                    if (routeTypePref == null || routeTypePref.getIntValue() != Route.ROUTE_PEDESTRIAN)
                    {
                        trafficSummaryButtonPopup = createTrafficSummaryButtonPopup();
                        trafficSummaryButtonPopup.setTimeout(timeout, CMD_BOTTOM_HIDE);
                        trafficSummaryButtonPopup.setAnimationDuration(0, 0);
                    }
                }
                isLastNotShowOnboard = isOnboard;

                int x = 0;
                int y = uiDecorator.BOTTOM_BAR_Y_POS.getInt() + AppConfigHelper.getTopBarHeight();
                int width = uiDecorator.SCREEN_WIDTH.getInt();
                int height = uiDecorator.BOTTOM_BAR_HEIGHT.getInt();

                bottomPopup.showAt(rootContainer, x, y, width, height, false);

                int zoomX, zoomY, zoomWidth, zoomHeight;
                boolean isPortrait = isPortarit();
                if (isZoomHorizonLayout() || isPortrait || AppConfigHelper.isTabletSize())
                {
                    zoomX = ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_X.getInt();
                    zoomY = ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_Y.getInt();
                    zoomWidth = ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_WIDTH.getInt()
                            + ((MovingMapUiDecorator) uiDecorator).ZOOM_IN_WIDTH.getInt();
                    zoomHeight = ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_HEIGHT.getInt();
                }
                else
                {
                    zoomWidth = ((MovingMapUiDecorator) uiDecorator).ZOOM_IN_WIDTH.getInt();
                    zoomHeight = ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_HEIGHT.getInt() * 2;

                    zoomX = ((MovingMapUiDecorator) uiDecorator).ZOOM_OUT_X.getInt();
                    zoomY = ((MovingMapUiDecorator) uiDecorator).ZOOM_IN_Y.getInt();
                }
                if (isNextNextTurnShown)
                {
                    zoomY += ((MovingMapUiDecorator) uiDecorator).NEXT_NEXT_TURN_HEIGHT.getInt();
                }
                if (!isEndTripShown())
                {
                    zoomPopup.showAt(rootContainer, zoomX, zoomY, zoomWidth, zoomHeight, false);
                }

                if (trafficSummaryButtonPopup != null)
                {
                    int trafficX = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_X.getInt();
                    int trafficY = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_Y.getInt();
                    if (isNextNextTurnShown)
                    {
                        trafficY += ((MovingMapUiDecorator) uiDecorator).NEXT_NEXT_TURN_HEIGHT.getInt();
                    }
                    int trafficWidth = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_WIDTH.getInt();
                    int trafficHeight = ((MovingMapUiDecorator) uiDecorator).TRAFFIC_BUTTON_HEIGHT.getInt();
                    if (trafficAlertButtonPopup == null || !trafficAlertButtonPopup.isShown())
                    {
                        if (!isEndTripShown())
                        {
                            trafficSummaryButtonPopup.showAt(rootContainer, trafficX, trafficY, trafficWidth, trafficHeight,
                                false);
                        }
                    }
                }

                MovingMapViewTouch.this.zoomPopup = zoomPopup;
                MovingMapViewTouch.this.trafficSummaryButtonPopup = trafficSummaryButtonPopup;
                MovingMapViewTouch.this.bottomPopup = bottomPopup;
                relocateMapLogoProvider(uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR);
            }
        };

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }

    protected void hideMapPopup()
    {
        Runnable run = new Runnable()
        {
            public void run()
            {
                if (zoomPopup != null)
                {
                    zoomPopup.hideImmediately();
                    zoomPopup = null;
                }
                if (trafficSummaryButtonPopup != null)
                {
                    trafficSummaryButtonPopup.hideImmediately();
                    trafficSummaryButtonPopup = null;
                    isLastNotShowOnboard = false;
                }
                if (bottomPopup != null)
                {
                    bottomPopup.hideImmediately();
                    bottomPopup = null;
                }
            }
        };
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }

    protected void changeZoomLevelOnMode()
    {
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        if (lastOrientation != orientation)
        {
            int zoomLevel = (int) MapContainer.getInstance().getZoomLevel();
            if (lastOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            {
                if (zoomLevel == ZOOM_LEVEL_DEFAULT_NAV)
                {
                    MapContainer.getInstance().setZoomLevel(ZOOM_LEVEL_DEFAULT_NAV_LANDSCAPE);
                }
            }
            else
            {
                if (zoomLevel == ZOOM_LEVEL_DEFAULT_NAV_LANDSCAPE)
                {
                    MapContainer.getInstance().setZoomLevel(ZOOM_LEVEL_DEFAULT_NAV);
                }
            }
            lastOrientation = orientation;
        }
    }

    protected void handlePopupWhenArrival()
    {
        Runnable run = new Runnable()
        {
            public void run()
            {
                hideMapPopup();

                if (trafficAlertButtonPopup != null)
                {
                    trafficAlertButtonPopup.hideImmediately();
                }

                bottomPopup = createBottomPopup(createBottomBarArgs(true));
                bottomPopup.setTimeout(CitizenSlidableContainer.TIMEOUT_NEVER_HIDE, CMD_BOTTOM_HIDE);
                int x = 0;
                int y = uiDecorator.BOTTOM_BAR_Y_POS.getInt() + AppConfigHelper.getTopBarHeight();
                int width = uiDecorator.SCREEN_WIDTH.getInt();
                int height = uiDecorator.BOTTOM_BAR_HEIGHT.getInt();
                bottomPopup.showAt(MapContainer.getInstance(), x, y, width, height, false);
            }
        };
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }

    public void updateMapContainerEvent(MapContainer container, int event)
    {
        if (event == IMapUIEventListener.EVENT_CREATED)
        {
            initMovingMap();
        }
    }

    protected void initMovingMap()
    {
        final MapContainer mapContainer = (MapContainer) model.get(KEY_O_MAP_CONTAINER);
        if (mapContainer != null)
        {
            mapContainer.enableCar();
            
            initMapLayer();
            
            boolean isAdiInitial = false;
            
            NavParameter navParam = (NavParameter)model.get(KEY_O_NAV_PARAMETER);
            
            if(getScreenByState(STATE_MOVING_MAP) != null)
            {
                //this means it's not the first time we create the screen.
                
                if(navParam != null && navParam.isAdi)
                {
                    isAdiInitial = true;
                }
            }
            
            if(isAdiInitial)
            {
                mapContainer.setMapTransitionTime(0);
                isAdiMode = false;
                
                
                setAdi(mapContainer, -1, -1);
            }
            else
            {
                Float zoomLevel = (Float)model.get(KEY_F_MOVIING_MAP_ZOOM_LEVEL);
                if(zoomLevel == null)
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
                    {
                        mapContainer.setZoomLevel(ZOOM_LEVEL_DEFAULT_NAV_LANDSCAPE);
                    }
                    else
                    {
                        int renderingMode = mapContainer.getRenderingModeFromTripDao();
                        if(renderingMode == IMapContainerConstants.RENDERING_MODE_3D_HEADING_UP  || renderingMode == IMapContainerConstants.RENDERING_MODE_3D_NORTH_UP)
                        {
                            mapContainer.setZoomLevel(MapContainer.NAV_3D_DEFAULT_ZOOM_LEVEL);
                        }
                        else if (renderingMode == IMapContainerConstants.RENDERING_MODE_2D_HEADING_UP || renderingMode == IMapContainerConstants.RENDERING_MODE_2D_NORTH_UP)
                        {
                            mapContainer.setZoomLevel(MapContainer.NAV_2D_DEFAULT_ZOOM_LEVEL);
                        }
                    }
                }
                else
                {
                    mapContainer.setZoomLevel(zoomLevel.floatValue());
                }
                mapContainer.setMapTransitionTime(0);
                mapContainer.setRenderingMode(mapContainer.getRenderingModeFromTripDao());
                mapContainer.followVehicle();
                
                removeAdi(mapContainer);
            }
            
            checkFlags();
            model.put(KEY_B_IS_MAP_INITIALIZED, true);
            mapContainer.setEnableGLEngineUIEvent(true);

        }
    }
    
    protected void setVehiclePosition(TnLocation gpsData)
    {
        if (gpsData != null)
        {
            final MapContainer mapContainer = (MapContainer) model.get(KEY_O_MAP_CONTAINER);
            if (mapContainer != null)
            {
                mapContainer.setInteractionModeChangeListener(this);
                // final int latI = gpsData.getLatitude();
                // final int lonI = gpsData.getLongitude();
                // int headingI = gpsData.getHeading();
                // final double degreesLat = latI / 100000d;
                // final double degreesLon = lonI / 100000d;
                // final float heading = (float) headingI;
                //
                // final int meterAccuracy;
                String provider = gpsData.getProvider();
                // final boolean needResetZoomLevel;

                final boolean isCellLocation = TnLocationManager.NETWORK_PROVIDER.equalsIgnoreCase(provider)
                        || TnLocationManager.TN_NETWORK_PROVIDER.equalsIgnoreCase(provider);
                boolean isInJunctionMode = this.model.getBool(KEY_B_IS_JUNCTION_VIEW);

                if (isCellLocation)
                {
                    // needResetZoomLevel = !isLastGpsCellLocation && !isInJunctionMode && !isAdiMode();
                    // meterAccuracy = gpsData.getAccuracy();
                    if (!isInJunctionMode && !isAdiMode())
                    {
                        isLastGpsCellLocation = true;
                    }
                }
                else
                {
                    // needResetZoomLevel = isLastGpsCellLocation && !isInJunctionMode && !isAdiMode();
                    // meterAccuracy = 0;
                    if (!isInJunctionMode && !isAdiMode())
                    {
                        isLastGpsCellLocation = false;
                    }
                }

                // Runnable runnable = new Runnable()
                // {
                // public void run()
                // {
                // try
                // {
                // IMapEngine engine = MapEngineManager.getInstance()
                // .getMapEngine();
                // if (engine != null)
                // {
                // if (Logger.DEBUG)
                // Logger.log(Logger.INFO, this.getClass().getName(),
                // "start setting position, lat/lon=" + degreesLat
                // + "," + degreesLon + ", " + heading);
                // engine.setVehiclePosition(degreesLat, degreesLon,
                // heading, meterAccuracy);
                // if(needResetZoomLevel)
                // {
                // if(isCellLocation)
                // {
                // double minLat = (latI - meterAccuracy) / 100000.0d;
                // double maxLat = (latI + meterAccuracy) / 100000.0d;
                // double minLon = (lonI - meterAccuracy) / 100000.0d;
                // double maxLon = (lonI + meterAccuracy) / 100000.0d;
                //
                // int x = 0;
                // int y = 0;
                // int width = uiDecorator.SCREEN_WIDTH.getInt();
                // int height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                // - ((MovingMapUiDecorator) uiDecorator).NAV_TITLE_HEIGHT.getInt()
                // - ((MovingMapUiDecorator) uiDecorator).STREET_NAME_HEIGHT.getInt();
                //
                //
                // float[] zoomArray = new float[1];
                // double[] latArray = new double[1];
                // double[] lonArray = new double[1];
                // engine.calcRegion(engine.getViewId(), maxLat, minLon, minLat, maxLon, x, y, width,
                // height, zoomArray, latArray, lonArray);
                //
                // engine.setZoomLevel(engine.getViewId(), zoomArray[0]);
                // }
                // else
                // {
                // engine.setZoomLevel(engine.getViewId(), ZOOM_LEVEL_DEFAULT_NAV);
                // }
                // }
                // if (Logger.DEBUG)
                // {
                // EngineState engineState = engine.getViewState(engine
                // .getViewId());
                // if (engineState != null)
                // Logger.log(Logger.INFO,
                // this.getClass().getName(),
                // "end setting position, engine lat/lon="
                // + engineState.cameraLatitude + ","
                // + engineState.cameraLongitude + ", "
                // + engineState.cameraHeading);
                // else
                // Logger.log(Logger.INFO,
                // this.getClass().getName(),
                // "engineState is null");
                // }
                // }
                // else
                // {
                // if (Logger.DEBUG)
                // Logger.log(Logger.INFO, this.getClass().getName(),
                // "mapEngine is null");
                // }
                // }
                // catch (Throwable t)
                // {
                // Logger.log(this.getClass().getName(), t);
                // }
                // }
                // };
                //
                // mapContainer.postRenderEvent(runnable);
            }
            else
            {
                if (Logger.DEBUG)
                    Logger.log(Logger.INFO, this.getClass().getName(), "mapContainer is null!");
            }
        }
    }

    protected void popAllViews()
    {
        removeDwfFeature();
        TeleNavDelegate.getInstance().unregisterApplicationListener(this);
        releaseMapContainer();
        hideMapPopup();
        if (backToNavPopup != null)
        {
            backToNavPopup.hideImmediately();
            backToNavPopup = null;
        }
        if (notification != null)
        {
            notification.hideImmediately();
            notification = null;
        }
        if (trafficAlertButtonPopup != null)
        {
            trafficAlertButtonPopup.hideImmediately();
            trafficAlertButtonPopup = null;
        }
        
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
        if (dwfAidl != null)
        {
            try
            {
                dwfAidl.removeUpdateListener(friendsAnnotationHandler.toString());
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        super.popAllViews();
    }

    private void releaseMapContainer()
    {
        MapContainer.getInstance().setInteractionModeChangeListener(null);
    }

    private boolean hideVbbManually()
    {
        BillboardAd currentAd = this.currentAd;

        if (currentAd != null && !currentAd.isEnded())
        {
            NavAdManager.getInstance().logImpressionEnd(currentAd, IMisLogConstants.VALUE_BILLBOARD_EXIT_TYPE_FORCED);
            hideVBB();
            return true;
        }
        return false;
    }

    public void handleTouchEventOnMap(final MapContainer container, TnUiEvent uiEvent)
    {
        handleTapAction(container);
        if(!hasLogMapEvent)
        {
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                KontagentLogger.NAVIGATION_MAP_CLICKED);
            hasLogMapEvent = true;
        }
        //disable the dimming of the screen per Matt. Enable it once get a better idea.
        /*TnBacklightManagerImpl.getInstance().setDefaultBrightness(15);*/
        
        final TnMotionEvent motionEvent = uiEvent.getMotionEvent();
        int action = motionEvent.getAction();
        if (action == TnMotionEvent.ACTION_DOWN)
        {
            isDoublePointTouch = motionEvent.getPointerCount() > 1;
            hideVbbManually();
        }
        else if (action == TnMotionEvent.ACTION_MOVE)
        {
            if (IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE == container.getInteractionMode())
            {
                isDoublePointTouch = motionEvent.getPointerCount() > 1;
                container.setEnableMapUI(true);
                MapContainer.getInstance().setMapPanAndZoom();
            }
            if (!isDoublePointTouch)
            {
                if (backToNavPopup == null)
                {
                    backToNavPopup = createBackToNavPopup();
                }

                if (backToNavPopup != null && !backToNavPopup.isShown())
                {
                    showBackToNavPopup();
                }
            }
        }
        else if (action == TnMotionEvent.ACTION_UP)
        {
            if (isDoublePointTouch)
            {
                isDoublePointTouch = false;
            }
        }
    }

    public void appActivated(String[] params)
    {
        isAppPaused = false;
        MapContainer.getInstance().setInteractionModeChangeListener(this);
    }

    public void appDeactivated(String[] params)
    {
        isAppPaused = true;
        // this.setDisableScreenAnimationTemporarily(true);
        // fix bug TN-472. TN-473.
        // refresh the whole moving map screen when back to background.
        releaseMapContainer();
        hideMapPopup();
        TnScreen screen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        if (screen != null && (screen.getId() == STATE_MOVING_MAP))
        {
            relocateMapLogoProvider(((MovingMapUiDecorator) uiDecorator).MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR);
        }
    }

    public void interactionModeChanged(int newInteractionMode)
    {
        if (model.isActivated())
        {
            switch (newInteractionMode)
            {
                case IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE:
                {
                    Runnable run = new Runnable()
                    {
                        public void run()
                        {
                            if (backToNavPopup != null)
                            {
                                backToNavPopup.hideImmediately();
                                backToNavPopup = null;

                                // When click on center location button, it will bring back the real time navigation
                                // and center location button will disappear, all other buttons remain on the screen for
                                // 7 seconds then disappear.
                                if (isMapPopupExist() && bottomPopup.isShown())
                                {
                                    handleTapAction(MapContainer.getInstance());
                                }
                            }
                        }
                    };

                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
                    if (shouldSetNeedSync())
                    {
                        setZoomButtonNeedSync();
                    }

                    break;
                }
                // default:
                // {
                // if (backToNavPopup == null)
                // {
                // backToNavPopup = createBackToNavPopup();
                // }
                // showBackToNavPopup();
                // break;
                // }
            }
        }
    }

    private void showBackToNavPopup()
    {
        CitizenSlidableContainer backToNavPopup = this.backToNavPopup;
        if (backToNavPopup != null)
        {
            int x = ((MovingMapUiDecorator) uiDecorator).BACK_TO_NAV_BUTTON_X.getInt();
            int y = ((MovingMapUiDecorator) uiDecorator).BACK_TO_NAV_BUTTON_Y.getInt();
            int width = ((MovingMapUiDecorator) uiDecorator).BACK_TO_NAV_BUTTON_WIDTH.getInt();
            int height = ((MovingMapUiDecorator) uiDecorator).BACK_TO_NAV_BUTTON_HEIGHT.getInt();
            backToNavPopup.showAt(MapContainer.getInstance(), x, y, width, height, false);
        }
    }

    protected void deactivateDelegate()
    {
        removeDwfFeature();
        
        MapContainer.getInstance().enableAnnotationLayer(IMapContainerConstants.NAV_SDK_Z_ORDER_TRAFFIC_INCIDENT_CUSTOM, false);
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeOptionsMenu();
        MapContainer.getInstance().removeMapUIEventListener();
        hideAllPopup();
        TnScreen screen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        if (screen != null && (screen.getId() == STATE_MOVING_MAP))
        {
            relocateMapLogoProvider(((MovingMapUiDecorator) uiDecorator).MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR);
            float zoomLevel = MapContainer.getInstance().getZoomLevel();
            model.put(KEY_F_MOVIING_MAP_ZOOM_LEVEL, new Float(zoomLevel));
        }
        removeFlag();
        NetworkStatusManager.getInstance().removeStatusListener(this);
        isReFollowVechicle = true;
        lastAlertEvent = null;
        super.deactivateDelegate();
    }

    private void removeDwfFeature()
    {
        DwfAidl aidl = DwfServiceConnection.getInstance().getConnection();
        try
        {
            if (aidl != null)
            {
                aidl.removeUpdateListener(friendsAnnotationHandler.toString());
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        DwfSliderPopup.getInstance().hide();
    }

    private void hideAllPopup()
    {
        Runnable run = new Runnable()
        {
            public void run()
            {
                CitizenSlidableContainer backToNavPopup = MovingMapViewTouch.this.backToNavPopup;
                if (backToNavPopup != null)
                {
                    backToNavPopup.hideImmediately();
                    MovingMapViewTouch.this.backToNavPopup = null;
                }
                if (trafficAlertButtonPopup != null)
                {
                    trafficAlertButtonPopup.hideImmediately();
                    trafficAlertButtonPopup = null;
                }
                hideMapPopup();
            }
        };
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }

    protected void activate()
    {
        initMapLayer();
        MapContainer.getInstance().setMapUIEventListener(this);
        if (MapContainer.getInstance().getInteractionMode() != IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
        {
            // if first time enter this module, backToNavPopup is NULL.
            // if we leave this page to other controller that don't have screen,
            // e.g, DSR and route setting, which only have popup,
            // when back to nav, we should show backToNavPopup again.
            CitizenSlidableContainer backToNavPopup = this.backToNavPopup;
            if (backToNavPopup != null && !backToNavPopup.isShown())
            {
                showBackToNavPopup();
            }
        }

        if (!MapContainer.getInstance().isDisplayingModelVehicle() && this.model.isActivated())
        {
            MapContainer.getInstance().changeToModelVehicleAnnotation();
        }
        if (NavSdkNavEngine.getInstance().isRunning())
        {
            RouteUiHelper.updateCurrentRoute(MapContainer.getInstance());
        }

        this.changeCameraDeclination();

        MapContainer.getInstance().setMapTransitionTime(
            this.model.fetchBool(KEY_B_IS_FROM_DWF) ? 0 : MapContainer.getInstance().getFasterTransitionTime());
        MapContainer.getInstance().followVehicle();
        MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getTransitionTime());
        MapContainer.getInstance().enableGPSCoarse(false);
        MapContainer.getInstance().setLowFPSAllowed(true);

        super.activate();
    }

    protected void initMapLayer()
    {
        int layerSetting = 0;
        if(AppConfigHelper.isDisableRoutesInSatelliteView() && TeleNav.isDebugVerison)
        {
            //debug and set disable routes in satellite
            layerSetting = layerSetting | 0x02;
        }
        
        int trafficFlowFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_LAYER_TRAFFIC_INCIDENT);
        if ((trafficFlowFeature == FeaturesManager.FE_ENABLED || trafficFlowFeature == FeaturesManager.FE_PURCHASED))
        {
            layerSetting = layerSetting | 0x01;
        }
        MapContainer.getInstance().showMapLayer(layerSetting, false);
        MapContainer.getInstance().enableAnnotationLayer(IMapContainerConstants.NAV_SDK_Z_ORDER_TRAFFIC_INCIDENT_CUSTOM, true);

        MapContainer.getInstance().changeToModelVehicleAnnotation();
        // update day/night/satellite configuration
        MapContainer.getInstance().updateMapColor();
        MapContainer.getInstance().updateMapColorMode();
    }

    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (tnUiEvent.getCommandEvent() != null)
                {
                    int command = tnUiEvent.getCommandEvent().getCommand();
                    if (command == ICommonConstants.CMD_COMMON_EXIT)
                    {
                        this.handleViewEvent(ICommonConstants.CMD_COMMON_EXIT);
                    }
                    else if (command == ICommonConstants.CMD_COMMON_BACK)
                    {
                        isKeyBack = false;
                    }
                }
                break;
            }
            case TnUiEvent.TYPE_KEY_EVENT:
            {
                if (tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
                {
                    isKeyBack = true;
                }
                break;
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }

    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
                case CMD_ADD_TO_FAVORITE:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                        KontagentLogger.NAVIGATION_ARRIVAL_ADD_CLICKED);
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
                case CMD_PLAY_AUDIO:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                        KontagentLogger.NAVIGATION_REPLAY_GUIDANCE_CLICKED);
                    break;
                }
                case CMD_COMMON_LINK_TO_SEARCH:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                        KontagentLogger.NAVIGATION_NEARBY_CLICKED);
                    break;
                }
                case CMD_END_TRIP:
                {
                    KontagentAssistLogger.endLogNavigationTimeWhenExit();
                    break;
                }
            }
        }
        else if(tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
        {
            if (tnUiEvent.getKeyEvent()!= null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
                    && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK && this.model.getState() == STATE_MOVING_MAP)
            {
//                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MYPROFILE, KontagentLogger.MYPROFILE_BACK_CLICKED);
            }
        } 
    }
    
    private void updateTrafficIncident(TrafficAlertEvent event)
    {
        IncidentAnnotation incidentAnnotation = null;
        NavParameter param = (NavParameter) this.model.get(KEY_O_NAV_PARAMETER);
        {
            if (param.alertEvent != null
                    && (lastAlertEvent == null || param.alertEvent.getIncidentId() != lastAlertEvent.getIncidentId()))
            {
                incidentAnnotation = new IncidentAnnotation(event.getIncidentId(), param.alertEvent);
            }
        }
        if (incidentAnnotation == null)
        {
            return;
        }

        MapContainer.getInstance().removeIncidentAnnotations();
        incidentAnnotation.setCommandEventListener(this);
        MapContainer.getInstance().addFeature(incidentAnnotation);

    }

    private boolean isZoomHorizonLayout()
    {
        return true;
    }

    public void onPinchEnd()
    {
        super.onPinchEnd();
        updateZoomInButton();
    }

    public void onDoubleTap()
    {
        if (backToNavPopup == null)
        {
            backToNavPopup = createBackToNavPopup();
        }

        if (backToNavPopup != null && !backToNavPopup.isShown())
        {
            showBackToNavPopup();
        }
    }

    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null)
        {
            if (attached == ITnScreenAttachedListener.AFTER_ATTACHED && screen.getId() == STATE_MOVING_MAP)
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
                TeleNavDelegate.getInstance().callAppNativeFeature(
                    TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, new Object[]{PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(false),PrimitiveTypeCache.valueOf(true)});

            }
        }
    }

    protected void addReportTrafficCameraMenu()
    {
        TnScreen screen = getScreenByState(STATE_MOVING_MAP);

        if (screen == null)
        {
            return;
        }

        TnMenu menu = screen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);

        if (menu == null)
        {
            return;
        }

        if (menu.findItem(CMD_REPORT_SPEED_TRAP) != null)
        {
            return;
        }

        int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
        boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                || trafficEnableValue == FeaturesManager.FE_PURCHASED;

        int index = 0;
        if (isTrafficEnabled)
        {
            int trafficCameraValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_NAV_TRAFFIC_CAMERA);
            boolean isTrafficCameraOn = trafficCameraValue == FeaturesManager.FE_ENABLED
                    || trafficCameraValue == FeaturesManager.FE_PURCHASED;
            if (isTrafficCameraOn)
            {
                menu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringNav.RES_REPORT_SPEED_TRAP, IStringNav.FAMILY_NAV), CMD_REPORT_SPEED_TRAP,
                    index);
            }
        }
    }

    protected void removeReportTrafficCameraMenu()
    {
        TnScreen screen = getScreenByState(STATE_MOVING_MAP);

        if (screen == null)
        {
            return;
        }

        TnMenu menu = screen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);

        if (menu == null)
        {
            return;
        }

        menu.remove(CMD_REPORT_SPEED_TRAP);

        TnScreen currentScreen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        if (currentScreen != null && currentScreen.getId() == STATE_MOVING_MAP)
        {
            // when the status changed, we should close the menu if it's opened.
            // next time it is opened, it will show updated content.
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeOptionsMenu();
        }
    }

    public void statusUpdate(boolean isConnected)
    {
        if(isConnected)
        {
            addReportTrafficCameraMenu();
        }
        else
        {
            removeReportTrafficCameraMenu();
        }
    }

    @Override
    public void onZoomIn()
    {
        if (isAdiMode())
        {
            MapContainer.getInstance().interactionModeChanged(IMapContainerConstants.INTERACTION_MODE_PAN_AND_ZOOM);
            MapContainer.getInstance().setMapPanAndZoom();
            MapContainer.getInstance().setLastControlEventTime(System.currentTimeMillis());
        }
    }

    @Override
    public void onZoomOut()
    {
        if (isAdiMode())
        {
            MapContainer.getInstance().interactionModeChanged(IMapContainerConstants.INTERACTION_MODE_PAN_AND_ZOOM);
            MapContainer.getInstance().setMapPanAndZoom();
            MapContainer.getInstance().setLastControlEventTime(System.currentTimeMillis());
        }
    }
    
    protected void updateZoomInButton()
    {
        CitizenSlidableContainer zoomPopup = this.zoomPopup;
        if (zoomPopup != null)
        {
            TnLinearContainer innerContainer = (TnLinearContainer) zoomPopup.getContent();
            NavZoomButton zoomInBtn = (NavZoomButton) innerContainer.getComponentById(ID_ZOOM_IN_BUTTON);
            if (zoomInBtn != null)
            {
                zoomInBtn.updateZoomButtonStatus();
            }
        }
    }
    
    protected void updateZoomOutButton()
    {
        CitizenSlidableContainer zoomPopup = this.zoomPopup;
        if (zoomPopup != null)
        {
            TnLinearContainer innerContainer = (TnLinearContainer) zoomPopup.getContent();
            NavZoomButton zoomOutBtn = (NavZoomButton) innerContainer.getComponentById(ID_ZOOM_OUT_BUTTON);
            if (zoomOutBtn != null)
            {
                zoomOutBtn.updateZoomButtonStatus();
            }
        }
    }
    
    private void addFriends(ArrayList<Friend> friends, boolean isClearBeforeAdd, String userKey)
    {
        if(isClearBeforeAdd)
        {
            currentFriendAnnotations.clear();
        }
        
        if (friends == null)
            return;

        for (Friend f : friends)
        {
            if ((f.getKey() != null && f.getKey().equals(userKey)) || (f.getLat() == 0 && f.getLon() == 0))
            {
                continue;
            }

            FriendAnnotation friendAnnotation = new FriendAnnotation(f, AnnotationType.AnnotationType_Sprite, FriendAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
            MapContainer.getInstance().addFeature(friendAnnotation);

            currentFriendAnnotations.addElement(friendAnnotation);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void updateFriends(List<Friend> friends)
    {
        if (isAppPaused || !this.model.isActivated())
        {
            return;
        }
        
        String userKey = null;
        
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
        if (dwfAidl != null)
        {
            try
            {
                userKey = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_KEY);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        
        if (currentFriendAnnotations == null)
        {
            currentFriendAnnotations = new Vector<FriendAnnotation>();
        }

        ArrayList<Friend> newFriends = new ArrayList<Friend>();
        Hashtable<FriendAnnotation, Friend> updatedAnnotations = new Hashtable<FriendAnnotation, Friend>();

        // There are 3 types of friends:
        // 1. new added ones (exists in friends list but not in annotation vector).
        // 2. updated ones (exists both in friends list and annotation vector).
        // 3. deleted ones (only exist in annotation vector).
        for (int i = 0; i < friends.size(); i++)
        {
            Friend friend = friends.get(i);
            String key = friend.getKey();

            if ((friend.getKey() != null && friend.getKey().equals(userKey)) || (friend.getLat() == 0 && friend.getLon() == 0))
            {
                continue;
            }
            
            boolean isFound = false;
            for (int j = 0; j < currentFriendAnnotations.size(); j++)
            {
                FriendAnnotation friendAnnotation = (FriendAnnotation) currentFriendAnnotations.elementAt(j);
                String addedKey = friendAnnotation.getUserKey();

                if (key.equals(addedKey))
                {
                    isFound = true;
                    updatedAnnotations.put(friendAnnotation, friend);
                    currentFriendAnnotations.removeElement(friendAnnotation);
                    break;
                }
            }

            if (!isFound)
            {
                newFriends.add(friend);
            }
        }

        // The rest annotations don't exist in latest friend list.
        // Delete them.
        if (!currentFriendAnnotations.isEmpty())
        {
            for (int i = 0; i < currentFriendAnnotations.size(); i++)
            {
                FriendAnnotation friendAnnotation = currentFriendAnnotations.elementAt(i);

                MapContainer.getInstance().removeFeature(friendAnnotation);
            }

            currentFriendAnnotations.clear();
        }

        // add new friends
        if (!newFriends.isEmpty())
        {
            addFriends(newFriends, false, userKey);
        }

        // update existing friends
        if (!updatedAnnotations.isEmpty())
        {
            Hashtable<AbstractAnnotation, TnLocation> table = new Hashtable<AbstractAnnotation, TnLocation>();
            Enumeration<FriendAnnotation> keys = updatedAnnotations.keys();
            while (keys.hasMoreElements())
            {
                FriendAnnotation annotation = keys.nextElement();
                Friend newFriend = updatedAnnotations.get(annotation);

                if (newFriend.getLat() == 0 && newFriend.getLon() == 0)
                {
                    continue;
                }
                TnLocation location = new TnLocation("");
                int lat = (int) (newFriend.getLat() * 100000);
                int lon = (int) (newFriend.getLon() * 100000);
                location.setLatitude(lat);
                location.setLongitude(lon);

                table.put(annotation, location);

                currentFriendAnnotations.add(annotation);
            }

            if (!table.isEmpty())
            {
                MapContainer.getInstance().moveAnnotations(table, MapContainer.getInstance().getViewId() + "");
            }
        }
    }
    
    class MovingmapSyncZoomLevelTask extends SyncZoomLevelTask
    {
        public void run()
        {
            MapContainer.getInstance().updateZoomLevel();
            updateZoomInButton();
            updateZoomOutButton();
        }
    }
    
    class MovingMapFriendsAnnotationHandler extends DwfUpdateListener.Stub
    {
        @Override
        public void updateDwf(List<Friend> friends) throws RemoteException
        {
            updateFriends(friends);
        }
    }

}
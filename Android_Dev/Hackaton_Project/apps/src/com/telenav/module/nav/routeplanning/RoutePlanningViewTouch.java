/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RoutePlanningViewTouch.java
 *
 */
package com.telenav.module.nav.routeplanning;

import java.util.Vector;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.map.MapConfig;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
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
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenRouteSummaryItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenShareEtaCheckItem;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.ImageAnnotation;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-2
 */
class RoutePlanningViewTouch extends AbstractCommonMapView implements IRoutePlanningConstants, INotifierListener
{
    protected int updateStep = 1;
    protected static final int MAX_UPDATE_STEPS = 4;
    protected static final int NOTIFY_INTERVAL = 500;
    protected long lastNotifyTimeStamp = -1L;
    protected static String DOT_CHAR = ".";
    protected static String APPEND_CHAR = "+";
    protected int lastOrientationForConfirmPanel = -1;
    
    ImageAnnotation origFlagAnnotation;
    ImageAnnotation destFlagAnnotation;
    
    boolean needUpdateMap = false;

    protected boolean isLastConnected = false;
    boolean hasLogMapEvent = false;
    
    public RoutePlanningViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popupContainer = null;
        switch (state)
        {
            case STATE_NO_GPS_TIMEOUT:
            {
                popupContainer =  createNoGpsPopup(state);
                break;
            }
            default:
                break;
        }
        
        return popupContainer;
    }

    protected TnPopupContainer createNoGpsPopup(int state)
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String noGps = bundle.getString(IStringCommon.RES_GPS_ERROR, IStringCommon.FAMILY_COMMON);
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        String retry = bundle.getString(IStringCommon.RES_BTTN_RETRY, IStringCommon.FAMILY_COMMON);
        menu.add(retry, CMD_RETRY_GPS);
        
        String cancel = bundle.getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON);
        menu.add(cancel, CMD_COMMON_BACK);
        
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, noGps, menu);
        return messageBox;
    }
    
    protected TnScreen createScreen(int state)
    {
        switch(state)
        {
           case STATE_ROUTE_PLANNING:
           {
               return createRoutePlanningScreen(STATE_ROUTE_PLANNING);
           }
           case STATE_CHANGE_SETTING_SCREEN:
           {
               return createSettingScreen(STATE_CHANGE_SETTING_SCREEN);
           }
        }
        
        return null;
    }

    //--------------------------------------------------------
    //  New Implementation
    //--------------------------------------------------------
    protected TnScreen createRoutePlanningScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        screen.getRootContainer().setSizeChangeListener(this);
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        createMapContainer(screen, orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT, true, true);
        boolean enableShareEta = model.getBool(KEY_B_IS_SHARE_ETA_SELECTED);
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_ROUTE_PLAN, enableShareEta 
            ? KontagentLogger.ROUTEPLAN_SHAREETA_INITIALLYON : KontagentLogger.ROUTEPLAN_SHAREETA_INITIALLYOFF);
        return screen;
    }
    
    protected void createConfirmPanel(final boolean isPortrait)
    {
        TnLinearContainer oldContainer = (TnLinearContainer) MapContainer.getInstance().getFeature(ID_CONFIRM_PANEL);
        
        TnLinearContainer confirmPanel = new TnLinearContainer(ID_CONFIRM_PANEL, true, AbstractTnGraphics.VCENTER);
        confirmPanel.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.ROUTE_PLAN_INFO_COLOR));
        
        int hPadding = ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_H_PADDING.getInt();
        int topPadding = ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_TOP_PADDING.getInt();
        int bottomPadding = ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_BOTTOM_PADDING.getInt();
        
        if(oldContainer == null)
        {
            TnLinearContainer addressInfoContainer;
            confirmPanel.setPadding(hPadding, topPadding, hPadding, bottomPadding);
            confirmPanel.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((RoutePlanningUiDecorator)uiDecorator).CONFIRM_PANEL_X);
            confirmPanel.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((RoutePlanningUiDecorator)uiDecorator).CONFIRM_PANEL_Y);
            TnLinearContainer originContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.VCENTER);
            TnLinearContainer destContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.VCENTER);
            addressInfoContainer = UiFactory.getInstance().createLinearContainer(0, true);
            
            addressInfoContainer.add(originContainer);
            addressInfoContainer.add(destContainer);
            
            //add content to origin container
            AbstractTnImage originPinSmallImage = ImageDecorator.ORIGIN_ICON_SMALL_UNFOCUSED.getImage();
            FrogImageComponent originPin = UiFactory.getInstance().createFrogImageComponent(0, originPinSmallImage);
            originPin.setPadding(0, 0, 0, 0);
            originContainer.add(originPin);
            
            FrogNullField originNullField = UiFactory.getInstance().createNullField(0);
            originNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_PADDING_BETWEEN_PIN_AND_LABEL);
            originContainer.add(originNullField);
            
            String originString = getOriginStopStr();
            FrogLabel originLabel = UiFactory.getInstance().createLabel(ID_ORIGIN_LABEL, originString);
            AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_ROUTE_INFO);
            originLabel.setFont(font);
            int textColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR);
            originLabel.setForegroundColor(textColor, textColor);
            originLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator)uiDecorator).CONFIRM_PANEL_ADDRESS_LABEL_WIDTH);
            originContainer.add(originLabel);
            
            //add content to dest container
            AbstractTnImage destPinSmallImage = ImageDecorator.DESTINATION_ICON_SMALL_UNFOCUSED.getImage();
            FrogImageComponent destPin = UiFactory.getInstance().createFrogImageComponent(0, destPinSmallImage);
            destPin.setPadding(0, 0, 0, 0);
            destContainer.add(destPin);
            
            FrogNullField destNullField = UiFactory.getInstance().createNullField(0);
            destNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_PADDING_BETWEEN_PIN_AND_LABEL);
            destContainer.add(destNullField);

            String destString = getDestStopStr();
            FrogLabel destLabel = UiFactory.getInstance().createLabel(ID_DEST_LABEL, destString);
            destLabel.setFont(font);
            destLabel.setForegroundColor(textColor, textColor);
            destLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator)uiDecorator).CONFIRM_PANEL_ADDRESS_LABEL_WIDTH);
            destContainer.add(destLabel);
            
            originContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator)uiDecorator).ADDRESS_CONTAINER__WIDTH);
            destContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator)uiDecorator).ADDRESS_CONTAINER__WIDTH);
            confirmPanel.add(addressInfoContainer);
            MapContainer.getInstance().addFeature(confirmPanel);
        }
        else
        {
            oldContainer.setPadding(hPadding, topPadding, hPadding, bottomPadding);
            oldContainer.requestLayout();
        }
    }
    
    protected AbstractTnComponent createMapContainer(CitizenScreen screen, final boolean isPortrait, boolean needClean, boolean showRoute)
    {
        MapContainer mapContainer;
        if(needClean)
        {
            mapContainer = UiFactory.getInstance().getCleanMapContainer(screen, ID_MAP_CONTAINER);
            origFlagAnnotation = null;
            destFlagAnnotation = null;
        }
        else
        {
            mapContainer = MapContainer.getInstance();
        }
        mapContainer.enableGPSCoarse(true);
        mapContainer.setMapRect(((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_X,
            ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_Y,
            ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_WIDTH,
            ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_HEIGHT);
        
        MapContainer.getInstance().setEnableGLEngineUIEvent(true);
        if(mapContainer.getFeature(ID_TITLE_CONTAINER) == null)
        {
            this.addTitleContainer();
        }
       
        createConfirmPanel(isPortrait);
        
        if(mapContainer.getFeature(ID_MAP_COMPANY_LOGO) == null)
        {
            AbstractTnComponent mapCompanyLogo = createMapCompanyLogo(((RoutePlanningUiDecorator) this.uiDecorator).ROUTE_MAP_LOGO_Y);
            final TnUiArgAdapter xAdapter = mapCompanyLogo.getTnUiArgs().get(TnUiArgs.KEY_POSITION_X);
            mapCompanyLogo.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {         
                    int x = xAdapter.getInt();
                    if(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        return PrimitiveTypeCache.valueOf(x); 
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(x + ((RoutePlanningUiDecorator)RoutePlanningViewTouch.this.uiDecorator).BOTTOM_CONTAINER_WIDTH.getInt()); 
                    }
                }
            }));
            mapContainer.addFeature(mapCompanyLogo);
        }
        if(mapContainer.getFeature(ID_MAP_PROVIDER) == null)
        {
            AbstractTnComponent mapProviderLabel = createMapProvider(((RoutePlanningUiDecorator) this.uiDecorator).ROUTE_MAP_LOGO_Y);
            mapContainer.addFeature(mapProviderLabel);
        }
        if(mapContainer.getFeature(ID_STATUS_INFO) == null)
        {
            AbstractTnComponent statusComponent = createStatusInfo();
            if(statusComponent != null)
            {
                mapContainer.addFeature(statusComponent);
                showStatusInfo(statusComponent);
            }
        }
        
        updateRoutePlanningMap(isPortrait, showRoute, false);

        return mapContainer;
    }
    
    protected FrogButton createNavigateButton()
    {
        String driveText = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_DRIVE, IStringNav.FAMILY_NAV);
        FrogButton navigateBtn = UiFactory.getInstance().createButton(ID_NAVIGATION_BUTTON, driveText);
        navigateBtn = UiFactory.getInstance().createButton(ID_NAVIGATION_BUTTON, driveText);
        navigateBtn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_BUTTON_HEIGHT);
        
        boolean needShareEta = this.model.getBool(KEY_B_NEED_SHARE_ETA);
        if(needShareEta)
        {
            navigateBtn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_NAVIGATION_WIDTH_WITH_ETA);
            navigateBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.ROUTE_PLANNING_NAVIGATE_BG_UNFOCUSED);
            navigateBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.ROUTE_PLANNING_NAVIGATE_BG_FOCUSED);
        }
        else
        {
            navigateBtn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_NAVIGATION_WIDTH);
            navigateBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.BIG_RADIAN_YELLOW_BUTTON_UNFOCUSED);
            navigateBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.BIG_RADIAN_YELLOW_BUTTON_FOCUSED);
        }
        navigateBtn.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_NAVIAGATION));
        int driveColor = UiStyleManager.getInstance().getColor(UiStyleManager.DRIVE_TEXT_COLOR);
        navigateBtn.setForegroundColor(driveColor, driveColor);

        // if no route choices, we need to disable drive button.
        Object choices = model.get(KEY_A_ROUTE_CHOICES);
        if (choices != null)
        {
            navigateBtn.setEnabled(true);
        }
        else
        {
            navigateBtn.setEnabled(false);
        }
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_NAVIGATE);
        navigateBtn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        navigateBtn.setCommandEventListener(this);
        
        return navigateBtn;
    }
    
    protected CitizenShareEtaCheckItem createCheckItem()
    {
        CitizenShareEtaCheckItem checkItem = new CitizenShareEtaCheckItem(ID_SHARE_ETA_CHECK_ITEM);
        checkItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_SHARE_ETA_WIDTH);
        checkItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_BUTTON_HEIGHT);
        AbstractTnFont shareTextFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_SHARE_ETA);
        checkItem.setFont(shareTextFont);
        checkItem.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SHARE_ETA_COLOR), UiStyleManager
                .getInstance().getColor(UiStyleManager.SHARE_ETA_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SHARE_ETA_DISABLED));
        boolean enableShareEta = model.getBool(KEY_B_IS_SHARE_ETA_SELECTED);
        checkItem.setChecked(enableShareEta);
        String shareText = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_SHARE, IStringNav.FAMILY_NAV);
        String etaText = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ETA, IStringNav.FAMILY_NAV);
        checkItem.setTexts(new String[]
        { shareText, etaText });
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_NONE);
        checkItem.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        checkItem.setCommandEventListener(this);
        checkItem.setTouchEventListener(this);
        checkItem.setEnabled(NetworkStatusManager.getInstance().isConnected());
        return checkItem;
    }
    
    protected AbstractTnContainer createBottomContainer()
    {
        AbstractTnContainer bottomContainer = (AbstractTnContainer) MapContainer.getInstance().getFeature(ID_BOTTOM_CONTAINER);
        FrogButton navigateBtn = null;
        CitizenShareEtaCheckItem checkItem = null;
        
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        boolean isPortrait = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;

        if (bottomContainer == null)
        {
            bottomContainer = new TnLinearContainer(ID_BOTTOM_CONTAINER, false, AbstractTnGraphics.VCENTER
                    | AbstractTnGraphics.HCENTER);
            SimpleConfigDao simpleConfigDao = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao();
            bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_CONTAINER_WIDTH);
            bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_CONTAINER_HEIGHT);
            bottomContainer.getTnUiArgs()
                    .put(TnUiArgs.KEY_POSITION_X, ((RoutePlanningUiDecorator) this.uiDecorator).ZERO_VALUE);
            bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,
                ((RoutePlanningUiDecorator) this.uiDecorator).BOTTOM_CONTAINER_Y);
            bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_BOTTOMBAR_TOP_UNFOCUSED);

            Address dest = (Address) this.model.get(KEY_O_ADDRESS_DEST);
            Stop destStop = dest.getStop();
            int destType = -1;
            if (destStop != null)
            {
                Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();

                if (DaoManager.getInstance().getAddressDao().isSameStop(destStop, work))
                {
                    destType = TYPE_WORK_DEST;
                }
                else if (DaoManager.getInstance().getAddressDao().isSameStop(destStop, home))
                {
                    destType = TYPE_HOME_DEST;
                }
                else
                {
                    destType = TYPE_OTHER_DEST;
                }
            }
            this.model.put(KEY_I_SHARE_ETA_ADDRESS_TYPE, destType);

            if (this.model.getBool(KEY_B_IS_FROM_DSR))
            {
                this.model.put(KEY_B_IS_SHARE_ETA_SELECTED, false);
            }
            else
            {
                // HOME receiver configured, drive home
                if (simpleConfigDao.getBoolean(SimpleConfigDao.KEY_IS_HOME_AUTO_SHARE_ENABLED)
                        && this.model.getInt(KEY_I_SHARE_ETA_ADDRESS_TYPE) == TYPE_HOME_DEST)
                {
                    this.model.put(KEY_B_IS_SHARE_ETA_SELECTED, true);
                }
                // WORK receiver configured, drive to work
                if (simpleConfigDao.getBoolean(SimpleConfigDao.KEY_IS_WORK_AUTO_SHARE_ENABLED)
                        && this.model.getInt(KEY_I_SHARE_ETA_ADDRESS_TYPE) == TYPE_WORK_DEST)
                {
                    this.model.put(KEY_B_IS_SHARE_ETA_SELECTED, true);
                }
            }
            MapContainer.getInstance().addFeature(bottomContainer);
        }
        else
        {
            navigateBtn = (FrogButton) bottomContainer.getComponentById(ID_NAVIGATION_BUTTON);
            checkItem = (CitizenShareEtaCheckItem) bottomContainer.getComponentById(ID_SHARE_ETA_CHECK_ITEM);
        }
        
        int bgColor = UiStyleManager.getInstance().getColor(
            isPortrait ? UiStyleManager.ROUTE_PLAN_BOTTOM_CONTAINER_COLOR_PORTRAIT
                    : UiStyleManager.ROUTE_PLAN_BOTTOM_CONTAINER_COLOR_LANDSCAPE);
        bottomContainer.setBackgroundColor(bgColor);
        
        boolean needShareEta = this.model.getBool(KEY_B_NEED_SHARE_ETA);
        
        if (checkItem == null)
        {
            checkItem = createCheckItem();
            bottomContainer.add(checkItem);
        }
        
        if(!needShareEta)
        {
            checkItem.setVisible(false);
        }
        
        if(navigateBtn == null)
        {
            navigateBtn = createNavigateButton();
            bottomContainer.add(navigateBtn);
        }
        else
        {
            Object choices = model.get(KEY_A_ROUTE_CHOICES);
            if (choices != null)
            {
                navigateBtn.setEnabled(true);
            }
            else
            {
                navigateBtn.setEnabled(false);
            }
        }
        
        if (isPortrait)
        {
            int iconTextGap = ((RoutePlanningUiDecorator) this.uiDecorator).NAV_BUTTON_ICON_TEXT_GAP.getInt();
            navigateBtn.setIcon(ImageDecorator.ROUTE_PLANNING_NAVIGAGE_UNFOCUSED.getImage(),
                ImageDecorator.ROUTE_PLANNING_NAVIGAGE_UNFOCUSED.getImage(), AbstractTnGraphics.HCENTER
                | AbstractTnGraphics.VCENTER);
            navigateBtn.setGap(iconTextGap);
        }
        else
        {
            navigateBtn.setIcon(null, null, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            navigateBtn.setGap(0);
        }
        
        return bottomContainer;
    }
    
    protected AbstractTnComponent createStatusInfo()
    {
        FrogLabel label = null;
        String status = getStatusString();

        if (status != null)
        {
            int whiteColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
            AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR);
            label = UiFactory.getInstance().createLabel(ID_STATUS_INFO, status);
            label.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
            label.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
            label.setForegroundColor(whiteColor, whiteColor);
            label.setFont(font);
            label.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((RoutePlanningUiDecorator)uiDecorator).ROUTE_PLANNING_STATUS_X);
            label.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((RoutePlanningUiDecorator)uiDecorator).ROUTE_PLANNING_STATUS_Y);
            label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator)uiDecorator).ROUTE_PLANNING_STATUS_WIDTH);
            label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator)uiDecorator).ROUTE_PLANNING_STATUS_HEIGHT);
            StringBuffer sb = new StringBuffer(status);
            for(int i = 1; i < MAX_UPDATE_STEPS; i++)
            {
                sb.append(DOT_CHAR);
            }
            int hPadding = (((RoutePlanningUiDecorator)uiDecorator).ROUTE_PLANNING_STATUS_WIDTH.getInt() - font.stringWidth(sb.toString())) / 2;
            label.setPadding(hPadding, 0, hPadding, 0);
        }
        else
        {
            label = null;
        }

        return label;
    }
    
    protected TnScreen createSettingScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        int whiteColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
        int MediumGrayColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
        AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM);
        AbstractTnFont boldFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP);
        AbstractTnFont buttonFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BUTTON);
        AbstractTnFont titleFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE);
        
        String screenTitle = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING_SETTING_ROUTE, IStringNav.FAMILY_NAV);
        FrogLabel title = UiFactory.getInstance().createLabel(0, screenTitle);
        title.setStyle(AbstractTnGraphics.HCENTER);
        title.setForegroundColor(whiteColor, whiteColor);
        title.setFont(titleFont);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator)uiDecorator).SETTING_TITLE_HEIGHT);
        title.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        title.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        
        TnMenu menu = new TnMenu();
        CitizenAddressListItem routeSettingItem = UiFactory.getInstance().createCitizenAddressListItem(ID_SETTING_ROUTE_SETTING_FIELD);
        routeSettingItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator)uiDecorator).SETTING_SCREEN_ITEM_HEIGHT);
        String routeSettingTitle = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING_SETTING_ROUTE_SETTING, IStringNav.FAMILY_NAV);
        routeSettingItem.setTitle(routeSettingTitle);
        routeSettingItem.setTitleColor(MediumGrayColor, MediumGrayColor);
        routeSettingItem.setAddress(getRouteSettingString(true));
        routeSettingItem.setForegroundColor(MediumGrayColor, MediumGrayColor);
        routeSettingItem.setFont(font);
        routeSettingItem.setBoldFont(boldFont);
        menu = new TnMenu();
        menu.add("", CMD_CHANGE_ROUTE_SETTING);
        routeSettingItem.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        routeSettingItem.setCommandEventListener(this);
        
        CitizenAddressListItem voiceSettingItem = UiFactory.getInstance().createCitizenAddressListItem(ID_SETTING_VOICE_SETTING_FIELD);
        voiceSettingItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator)uiDecorator).SETTING_SCREEN_ITEM_HEIGHT);
        String voiceSettingTitle = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING_SETTING_VOICE_SETTING, IStringNav.FAMILY_NAV);
        voiceSettingItem.setTitle(voiceSettingTitle);
        voiceSettingItem.setTitleColor(MediumGrayColor, MediumGrayColor);
        voiceSettingItem.setAddress(getVoiceSettingString());
        voiceSettingItem.setForegroundColor(MediumGrayColor, MediumGrayColor);
        voiceSettingItem.setFont(font);
        voiceSettingItem.setBoldFont(boldFont);
        menu = new TnMenu();
        menu.add("", CMD_CHANGE_VOICE_SETTING);
        voiceSettingItem.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        voiceSettingItem.setCommandEventListener(this);
        
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator)uiDecorator).SETTING_BUTTON_GAP);
        
        FrogNullField bottomField = UiFactory.getInstance().createNullField(0);
        bottomField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator)uiDecorator).SETTING_BOTTOM_GAP);
        
        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(ID_SETTING_GET_ROUTE_BUTTON, false, AbstractTnGraphics.HCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator)uiDecorator).SCREEN_WIDTH);
        String buttonText = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING_SETTING_GET_ROUTE, IStringNav.FAMILY_NAV);
        FrogButton getRouteButton = UiFactory.getInstance().createButton(0, buttonText);
        getRouteButton.setFont(buttonFont);
        getRouteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator)uiDecorator).SETTING_BUTTON_WIDTH);
        getRouteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator)uiDecorator).SETTING_BUTTON_HEIGHT);
        menu = new TnMenu();
        menu.add("", CMD_GET_ROUTE);
        getRouteButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        getRouteButton.setCommandEventListener(this);
        buttonContainer.add(getRouteButton);

        screen.getContentContainer().add(title);

        TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, true);
        TnLinearContainer contentContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        contentContainer.add(routeSettingItem);
        contentContainer.add(voiceSettingItem);
        contentContainer.add(nullField);
        contentContainer.add(buttonContainer);
        contentContainer.add(bottomField);

        panel.set(contentContainer);
        screen.getContentContainer().add(panel);

        return screen;
    }
    
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch(state)
        {
            case STATE_ROUTE_PLANNING:
            {
                return updateRoutePlanningScreen(screen);
            }
            case STATE_CHANGE_SETTING_SCREEN:
            {
                return updateSettingScreen(screen);
            }
        }
        return false;
    }

    private boolean updateRoutePlanningScreen(TnScreen prefScreen)
    {
        boolean isNeedRefresh = false;
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        boolean isPortrait = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
        
        MapContainer.getInstance().setEnableGLEngineUIEvent(true);
        
        boolean isBackFromRouteSetting = model.fetchBool(KEY_B_IS_BACK_FROM_ROUTE_SETTING);
        boolean isOrientationChanged = model.fetchBool(KEY_B_ORIENTATION_CHANGED);
        boolean isUpdatePlan = model.fetchBool(KEY_B_IS_UPDATE_PLAN);
        boolean isUpdateShowRoute = model.fetchBool(KEY_B_IS_UPDATE_SHOW_ROUTE);
        if (isBackFromRouteSetting || isOrientationChanged || isUpdatePlan || isUpdateShowRoute)
        {
            updateRoutePlanningMap(isPortrait, true, false);
            isNeedRefresh = true;
        }
        else
        {
            if (MapContainer.getInstance().getFeature(ID_CONFIRM_PANEL) == null)
            {
                createMapContainer((CitizenScreen) prefScreen, isPortrait, true, true);
                model.put(KEY_B_IS_UPDATE_FLAGS, true);
                isNeedRefresh = true;
            }
        }

        boolean isCurrentConnected = NetworkStatusManager.getInstance().isConnected();
        
        if(isCurrentConnected != isLastConnected)
        {
            initMapLayer();
        }
        
        return isNeedRefresh;
    }
    
    protected void removeSummaryPanel()
    {
        AbstractTnComponent routeSummaryPanel = MapContainer.getInstance().getFeature(ID_SUMMARY_CONTAINER);
        if (routeSummaryPanel != null)
        {
            ((AbstractTnContainer) routeSummaryPanel).removeAll();
            MapContainer.getInstance().removeFeature(ID_SUMMARY_CONTAINER);
        }
    }
    
    protected void updateRoutePlanningMap(final boolean isPortrait, boolean showRoute, boolean lazyUpdateMap)
    {
        MapContainer mapContainer = MapContainer.getInstance();//(MapContainer)contentContainer.getComponentById(ID_MAP_CONTAINER);
        if(mapContainer != null)
        {
            removeSummaryPanel();
            
            if(mapContainer.getFeature(ID_TITLE_CONTAINER) == null)
            {
                this.addTitleContainer();
            }
           
            createConfirmPanel(isPortrait);
            
            createBottomContainer();
            
            if(model.get(KEY_A_ROUTE_CHOICES) != null)
            {
                AbstractTnContainer confirmPanel = (AbstractTnContainer)mapContainer.getFeature(ID_CONFIRM_PANEL);
                if(confirmPanel != null)
                {
                    confirmPanel.getTnUiArgs().remove(TnUiArgs.KEY_BOTTOM_SHADOW_IMAGE);
                }
                
                //add planning info(Route A, Route B....)
                addRouteSelectorInfo(isPortrait);
                
                //hide status info label
                AbstractTnComponent statusInfo = mapContainer.getFeature(ID_STATUS_INFO);//contentContainer.getComponentById(ID_STATUS_INFO);
                hideStatusInfo(statusInfo);
            }
            else 
            {
                if(isPortrait)
                {
                    MapContainer.getInstance().removeFeature(ID_ROUTE_PLANNING_INFO_CONTAINER);
                }
                else //Keep space in landscape mode
                {
                    AbstractTnContainer routeCompareContainer = new TnLinearContainer(ID_ROUTE_PLANNING_INFO_CONTAINER, false, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    routeCompareContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_SUBTITLE_BAR));
                    routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((RoutePlanningUiDecorator) uiDecorator).ZERO_VALUE);
                    routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_Y);
                    routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).BOTTOM_CONTAINER_WIDTH);
                    routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_HEIGHT);
                    routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_PLANNING_PANEL_BG_UNFOCUSED);
                    routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_PLANNING_PANEL_BG_UNFOCUSED);
                    MapContainer.getInstance().addFeature(routeCompareContainer);
                }
                if(mapContainer.getFeature(ID_STATUS_INFO) == null)
                {
                    AbstractTnComponent statusComponent = createStatusInfo();
                    if(statusComponent != null)
                    {
                        mapContainer.addFeature(statusComponent);
                        showStatusInfo(statusComponent);
                    }
                }
                else 
                {
                    AbstractTnComponent statusInfo = mapContainer.getFeature(ID_STATUS_INFO);//contentContainer.getComponentById(ID_STATUS_INFO);
                    showStatusInfo(statusInfo);
                }
            }
            if(showRoute)
            {
                updateRouteMapOrList(lazyUpdateMap);
            }
        }
    }
    
    protected void updateConfirmPanel(TnScreen prefScreen)
    {
        String from = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING_FROM, IStringNav.FAMILY_NAV);
        String to = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING_TO, IStringNav.FAMILY_NAV);
        
        String originString = from + getOriginStopStr();
        String destString = to + getDestStopStr();
        FrogLabel originLabel = (FrogLabel)prefScreen.getComponentById(ID_ORIGIN_LABEL);
        if(originLabel != null)
        {
            originLabel.setText(originString);
        }
        
        FrogLabel destLabel = (FrogLabel)prefScreen.getComponentById(ID_DEST_LABEL);
        if(destLabel != null)
        {
            destLabel.setText(destString);
        }
        
    }

    protected void addRouteSelectorInfo(final boolean isPortrait)
    {
        TnLinearContainer oldContainer = (TnLinearContainer) MapContainer.getInstance().getFeature(ID_ROUTE_PLANNING_INFO_CONTAINER);
        MapContainer.getInstance().removeFeature(oldContainer);

        AbstractTnContainer routeCompareContainer = new TnLinearContainer(ID_ROUTE_PLANNING_INFO_CONTAINER, !isPortrait, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        int selectedPlan = model.getInt(KEY_I_SELECTED_PLAN);
        
        int[] routeLengths = null;
        int[] nzRouteEtas = null;
        int[] trafficDelays = null;
        Object obj = model.get(KEY_A_ROUTE_CHOICES_LENGTH);
        if (obj != null)
        {
            routeLengths = (int[]) obj;
        }

        obj = model.get(KEY_A_ROUTE_CHOICES_ETA);
        if (obj != null)
        {
            nzRouteEtas = (int[]) obj;
        }

        obj = model.get(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY);
        if (obj != null)
        {
            trafficDelays = (int[]) obj;
        }

        if (routeLengths == null || nzRouteEtas == null || routeLengths.length < 1
                || nzRouteEtas.length < 1)
        {
            return;
        }

        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        String[] routeLen = new String[routeLengths.length];
        String[] costTime = new String[routeLengths.length];
        for (int i = 0; i < nzRouteEtas.length; i++)
        {
           routeLen[i] = converter.convertDistanceMeterToMile(routeLengths[i], systemUnits);
           costTime[i] = ((RoutePlanningModel)model).parseEtaDisplayTime((nzRouteEtas[i] + trafficDelays[i]));
        }
        
        RoutePlanSelecter routePlanSelecter = (RoutePlanSelecter)MapContainer.getInstance().getFeature(ID_ROUTE_PLAN_SELECTOR);
        if(routePlanSelecter == null)
        {
            boolean needShowRouteList = model.getBool(KEY_B_SHOW_ROUTE_LIST);
            int status = needShowRouteList ? RoutePlanSelecter.STATUS_CHOICE_FOCUSED_LIST : RoutePlanSelecter.STATUS_CHOICE_FOCUSED_MAP;
            routePlanSelecter = new RoutePlanSelecter(ID_ROUTE_PLAN_SELECTOR, selectedPlan, routeLengths.length, routeLen, costTime, status);
            routePlanSelecter.setCommandEventListener(this);
        }
        else
        {
            routePlanSelecter.updateImageArray();
            MapContainer.getInstance().removeFeature(ID_ROUTE_PLAN_SELECTOR);
        }
        
        final int routePlanSelecterWidth = routePlanSelecter.getWidth();
        final int routePlanSelecterHeight = routePlanSelecter.getHeight();
        final int routePlanMiniSelecterMiniHeight = routePlanSelecter.getMiniHeight();
        final int routePlanMiniSelecterMiniWidth = routePlanSelecter.getMiniWidth();
       
        routePlanSelecter.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int gap = (((RoutePlanningUiDecorator)uiDecorator).SCREEN_WIDTH.getInt() - routePlanSelecterWidth)/2;
                    return PrimitiveTypeCache.valueOf(gap);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(((RoutePlanningUiDecorator)uiDecorator).BOTTOM_CONTAINER_WIDTH.getInt() - routePlanMiniSelecterMiniWidth);
                }
            }
        }));
      routePlanSelecter.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
      {
          public Object decorate(TnUiArgAdapter args)
          {
              int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
              if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
              {
                  return PrimitiveTypeCache.valueOf(((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_Y.getInt());
              }
              else
              {
                  return PrimitiveTypeCache.valueOf(((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_HEIGHT.getInt() - routePlanSelecterHeight + ((RoutePlanningUiDecorator) uiDecorator).BOTTOM_CONTAINER_VERTICAL_GAP.getInt())/2 + (((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_Y.getInt());
              }
          }
          }));

        routePlanSelecter.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(routePlanSelecterWidth);
            }
        }));
        routePlanSelecter.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(routePlanSelecterHeight);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(routePlanSelecterHeight);
                }
            }
        }));
        routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((RoutePlanningUiDecorator) uiDecorator).ZERO_VALUE);
        routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_Y);
        routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_WIDTH);
        routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(routePlanMiniSelecterMiniHeight);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(((RoutePlanningUiDecorator) uiDecorator).PLANNING_INFO_HEIGHT.getInt());
                }
            }
        }));

        routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_PLANNING_PANEL_BG_UNFOCUSED);
        routeCompareContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_PLANNING_PANEL_BG_UNFOCUSED);
        MapContainer.getInstance().setMapRect(((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_X,
            ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_Y_WITH_ROUTE,
            ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_WIDTH,
            ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_HEIGHT);
        MapContainer.getInstance().addFeature(routeCompareContainer);
        MapContainer.getInstance().addFeature(routePlanSelecter);
    }
    
    protected boolean updateSettingScreen(TnScreen screen)
    {
        CitizenAddressListItem originItem = (CitizenAddressListItem)screen.getComponentById(ID_SETTING_ORIGIN_FIELD);
        CitizenAddressListItem destItem = (CitizenAddressListItem)screen.getComponentById(ID_SETTING_DEST_FIELD);
        CitizenAddressListItem routeSettingItem = (CitizenAddressListItem)screen.getComponentById(ID_SETTING_ROUTE_SETTING_FIELD);
        CitizenAddressListItem voiceSettingItem = (CitizenAddressListItem)screen.getComponentById(ID_SETTING_VOICE_SETTING_FIELD);
        
        if(originItem != null)
        {
            originItem.setAddress(getOriginStopStr());
        }
        
        if(destItem != null)
        {
            destItem.setAddress(getDestStopStr());
        }
        
        if(routeSettingItem != null)
        {
            routeSettingItem.setAddress(getRouteSettingString(true));
        }
        
        if(voiceSettingItem != null)
        {
            voiceSettingItem.setAddress(getVoiceSettingString());
        }
        
        return true;
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch(state)
        {
            case STATE_CHANGE_SETTING_SCREEN:
            {
                if(commandId == CMD_CHANGE_ORIGIN)
                {
                    model.put(KEY_B_IS_EDITING_ORIGIN, true);
                }
                else if(commandId == CMD_CHANGE_DESTINATION)
                {
                    model.put(KEY_B_IS_EDITING_ORIGIN, false);
                }
                break;
            }
            case STATE_ROUTE_PLANNING:
            {
                if(commandId == CMD_CHANGE_SETTING)
                {
                    model.put(KEY_B_IS_UPDATE_FLAGS, true);
                }
				break;
            }
        }
        return true;
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        switch (model.getState())
        {
            case STATE_ROUTE_PLANNING:
            {
                if(tnUiEvent.getComponent() != null && (tnUiEvent.getComponent() instanceof CitizenShareEtaCheckItem) && tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT
                         && tnUiEvent.getMotionEvent() != null && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_UP)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    CitizenShareEtaCheckItem checkItem = (CitizenShareEtaCheckItem)component;
                    if (checkItem != null && checkItem.isEnabled())
                    {
                        checkItem.setChecked(!checkItem.isChecked());
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_ROUTE_PLAN, checkItem.isChecked() 
                            ? KontagentLogger.ROUTEPLAN_SHAREETA_ENABLED : KontagentLogger.ROUTEPLAN_SHAREETA_DISABLED);
                        model.put(KEY_B_IS_SHARE_ETA_SELECTED, checkItem.isChecked());
                        checkItem.requestPaint();
                        return true;
                    }
                }
                int type = tnUiEvent.getType();
                if (type == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
                    if (commandEvent != null)
                    {
                        int command = commandEvent.getCommand();
                        if (command == CMD_NAVIGATE)
                        {
                            MapContainer.getInstance().setEnableGLEngineUIEvent(false);
                            AbstractTnContainer bottomContainer = (AbstractTnContainer) MapContainer.getInstance().getFeature(ID_BOTTOM_CONTAINER);
                            if (bottomContainer != null)
                            {
                                CitizenShareEtaCheckItem shareETAItem = (CitizenShareEtaCheckItem) bottomContainer.getComponentById(ID_SHARE_ETA_CHECK_ITEM);
                                if (shareETAItem != null)
                                {
                                    model.put(KEY_B_IS_SHARE_ETA_DISABLED, !shareETAItem.isEnabled());
                                }
                            }
                        }
                        else if(command >= CMD_CHANGE_ROUTE_CHOICE_START && command < CMD_CHANGE_ROUTE_CHOICE_START + 3)
                        {
                            model.put(KEY_B_IS_UPDATE_PLAN, true);
                            model.put(KEY_B_SHOW_ROUTE_LIST, false);
                            model.put(KEY_I_SELECTED_PLAN, command - CMD_CHANGE_ROUTE_CHOICE_START);
                            tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_UPDATE_PLAN_INFO));
                        }
                        else if(command == CMD_SHOW_ROUTE_LIST)
                        {
                            model.put(KEY_B_IS_UPDATE_PLAN, true);
                            model.put(KEY_B_SHOW_ROUTE_LIST, true);
                            tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_UPDATE_PLAN_INFO));
                        }
                        else if(command == CMD_SHOW_ROUTE_MAP)
                        {
                            model.put(KEY_B_IS_UPDATE_PLAN, true);
                            model.put(KEY_B_SHOW_ROUTE_LIST, false);
                            tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_UPDATE_PLAN_INFO));
                        }
                    }
                }
                break;
            }
        }
        return false;
    }
    
    //---start---notifier listener interface
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
        updateStatusInfo();
    }
    
    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimeStamp = timestamp;
    }
    //---end---notifier listener interface
    
    protected void addToNotifier()
    {
        Notifier.getInstance().addListener(this);
    }
    
    protected void removeNotifierListener()
    {
        Notifier.getInstance().removeListener(this);
    }
    
    protected void popAllViews()
    {
        removeNotifierListener();
        super.popAllViews();
    }
    
    public void tapNoAnnotation()
    {
        // TODO Auto-generated method stub
        
    }

    public void handleTouchEventOnMap(MapContainer container, TnUiEvent uiEvent)
    {
        if(!hasLogMapEvent)
        {
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_ROUTE_PLAN,
                KontagentLogger.ROUTEPLAN_MAP_CLICKED);
            hasLogMapEvent = true;
        }
    }
    
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        
        int type = tnUiEvent.getType();
        
        switch(type)
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                if(tnUiEvent.getComponent() != null)
                {
                    TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();
                    if(motionEvent.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        if(tnUiEvent.getComponent() instanceof FrogLabel && tnUiEvent.getComponent().getParent() != null)
                        {
                            int index = tnUiEvent.getComponent().getParent().getId() - ID_PLANNING_INFO_LEFT_ROUTE_CHOICE;
                            if (index >= 0 && index < 3)
                            {
                                model.put(KEY_B_IS_UPDATE_PLAN, true);
                                model.put(KEY_I_SELECTED_PLAN, index);
                                cmd = CMD_UPDATE_PLAN_INFO;
                            }
                        }
                    }
                }
                break;
            }
        }
        
        return cmd;
    }

    
    private boolean isDynamicNav()
    {
        if (model.get(KEY_O_ADDRESS_ORI) == null)
        {
            return true;
        }
        
        return isCurrentLocation(true);
    }
    
    private boolean isCurrentLocation(boolean isOrigin)
    {
        Object obj = null;
        if(isOrigin)
        {
            obj = model.get(KEY_O_ADDRESS_ORI);
        }
        else
        {
            obj = model.get(KEY_O_ADDRESS_DEST);
        }
        
        Stop stop = ((Address)obj).getStop();
        if (stop != null && stop.getType() == Stop.STOP_CURRENT_LOCATION)
        {
            return true;
        }   
        return false;
    }
    
    protected String getDestStopStr()
    {
        Address dest = (Address) model.get(KEY_O_ADDRESS_DEST);
        Stop destStop = dest.getStop();
        if (destStop != null)
        {
            return getStopStr(destStop);
        }
        return "";
    }
    
    protected String getOriginStopStr()
    {
        Object obj = model.get(KEY_O_ADDRESS_ORI);
        if (obj instanceof Address)
        {
            Address origAddr = (Address) obj;
            Stop orig = origAddr.getStop();
            if(orig != null)
            {
                return getStopStr(orig);
            }
        }
        return "";
    }
    
    protected String getCurrentLocationString()
    {
        String currentLocation = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_CURRENT_LOCATION,
            IStringCommon.FAMILY_COMMON);
        if (currentLocation != null)
        {
            return currentLocation;
        }
        else
        {
            return "";
        }
    }
    
    protected String getStopStr(Stop stop)
    {
        if (stop != null)
        {
            if(stop.getType() == Stop.STOP_CURRENT_LOCATION)
            {
                return getCurrentLocationString();
            }
            else
            {
                return ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
            }
        }

        return "";
    }

    protected String getRouteSettingString(boolean isFullString)
    {
        StringBuffer result = new StringBuffer();
        Preference routeTypePref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        Preference routeSettingPref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
        
        if (routeTypePref != null)
        {
            result.append(getPreferenceString(routeTypePref));
        }
        if (routeSettingPref != null)
        {
        	if (routeTypePref.getIntValue() == Route.ROUTE_PEDESTRIAN)
        	{
        		result.append("");
        	}
        	else
        	{
        		String[] choices = routeSettingPref.getOptionNames();
        		boolean[] selected = getMultiChoices(routeSettingPref);
        		
        		for (int i = 0; i < selected.length; i++)
        		{
        			if (isFullString)
        			{
        				if (choices != null && i < choices.length)
        				{
        				    if (selected[i])
        					{
        						result.append(", ").append(choices[i]);
        					}
        				}
        			}
        			else
        			{
        				if (selected[i])
        				{
        					result.append(APPEND_CHAR);
        					break;
        				}
        			}
        		}        		
        	}
            
        }
        return result.toString();
    }
    
    protected String getVoiceSettingString()
    {
        String result = "";
        Preference audioPref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);

        result = getPreferenceString(audioPref);
        
        return result;
    }
    
    protected String getPreferenceString(Preference pref)
    {
        if (pref == null)
        {
            return "";
        }
        String result = "";

        int choice = pref.getIntValue();
        int[] choices = pref.getOptionValues();
        String[] choiceStrings = pref.getOptionNames();
        if (choices != null && choiceStrings != null)
        {
            for (int i = 0; i < choices.length && i < choiceStrings.length; i++)
            {
                if (choice == choices[i])
                {
                    result = choiceStrings[i];
                    break;
                }
            }
        }

        return result;
    }
    
    protected boolean[] getMultiChoices(Preference pref)
    {
        if (pref != null && pref.getOptionNames() != null)
        {
            boolean[] bSelected = new boolean[pref.getOptionNames().length];
            int remainValue = pref.getIntValue();
            if (pref.getId() == Preference.ID_PREFERENCE_ROUTE_SETTING)
            {
                int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
                boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED || trafficEnableValue == FeaturesManager.FE_PURCHASED;
                if (!isTrafficEnabled)
                {
                    if ((pref.getIntValue() & Preference.AVOID_TRAFFIC_DELAYS) != 0)
                    {
                        remainValue = remainValue - Preference.AVOID_TRAFFIC_DELAYS;
                    }
                }
            }
            Preference routeSettingPref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
            if(routeSettingPref != null)
            {
                int[] optionVals = routeSettingPref.getOptionValues();
                int index = -1;            
                int len = optionVals.length;
                for (int i = 0; i < len; i++)
                {
                    if (optionVals[i] == Preference.USE_CARPOOL_LANES)
                    {
                        index = i;
                        break;
                    }
                }
                if (index >= 0)
                {
                    bSelected[index] = ((remainValue & Preference.AVOID_CARPOOL_LANES) == 0) ? true : false;
                }
                for (int j = 0; j < optionVals.length; j++)
                {
                    if ((optionVals[j] & remainValue) <= 0 || j == index)
                        continue;
                    bSelected[j] = true;
                }
            }
            return bSelected;
        }
        return null;
    }    
    
    protected void hideStatusInfo(AbstractTnComponent statusInfo)
    {
        if(statusInfo != null)
        {
            statusInfo.setVisible(false);
        }
        
        removeNotifierListener();
    }
    
    protected void showStatusInfo(AbstractTnComponent statusInfo)
    {
        if(statusInfo != null)
        {
            statusInfo.setVisible(true);
        }
        
        addToNotifier();
    }
    
    protected void updateStatusInfo()
    {
        int state = model.getState();
        if (state == STATE_ROUTE_PLANNING)
        {
            FrogLabel label = (FrogLabel) MapContainer.getInstance().getFeature(ID_STATUS_INFO);
            if (label != null)
            {
                String labelText = label.getText();
                String statusString = getStatusString();
                if (statusString == null)
                {
                    return;
                }
                else if (labelText != null && labelText.startsWith(statusString))
                {
                    if (updateStep % MAX_UPDATE_STEPS == 0)
                    {
                        updateStep = 1;
                        label.setText(statusString);
                    }
                    else
                    {
                        labelText += DOT_CHAR;
                        label.setText(labelText);
                        updateStep++;
                    }
                    label.requestPaint();
                }
                else
                {
                    updateStep = 1;
                    label.setText(statusString);
                    StringBuffer sb = new StringBuffer(statusString);
                    for(int i = 1; i < MAX_UPDATE_STEPS; i++)
                    {
                        sb.append(DOT_CHAR);
                    }
                    int hPadding = (((RoutePlanningUiDecorator)uiDecorator).ROUTE_PLANNING_STATUS_WIDTH.getInt() - label.getFont().stringWidth(sb.toString())) / 2;
                    label.setPadding(hPadding, 0, hPadding, 0);
                    label.requestPaint();
                }
            }
        }
    }
    
    protected String getStatusString()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String status = null;
        Object currentLocation = model.get(KEY_O_CURRENT_LOCATION);
        Object routes = model.get(KEY_A_ROUTE_CHOICES);
        boolean isCurrentLocationExist = isCurrentLocation(true) | isCurrentLocation(false);
        if(isCurrentLocationExist && currentLocation == null && routes == null)
        {
            status = bundle.getString(IStringNav.RES_STATUS_GETTING_GPS, IStringNav.FAMILY_NAV);
        }
        else if(routes == null)
        {
            status = bundle.getString(IStringNav.RES_STATUS_GETTING_ROUTE, IStringNav.FAMILY_NAV);
        }
        return status;
    }
    
    //the method to update planning map.
    protected void updateRouteMapOrList(boolean lazyUpdateMap)
    {
        MapContainer mapContainer = MapContainer.getInstance();
        
        final AbstractTnComponent routePlanSelecter = mapContainer.getFeature(ID_ROUTE_PLAN_SELECTOR);
        int tempHeight = 0;
        if (routePlanSelecter != null)
        {            
            tempHeight = ((RoutePlanSelecter) routePlanSelecter).getMiniHeight();
        }
        if (!model.getBool(KEY_B_SHOW_ROUTE_LIST)) // show route on map
        {
            //for onsizeChanged, we should update map until surface change is done.
            if (lazyUpdateMap)
            {
                needUpdateMap = true;
            }
            else
            {
                updateRouteMap();
            }
        }
        else
        {
            // add route list
            this.updateRouteList(tempHeight, routePlanSelecter);
        }
    }

    private void updateRouteMap()
    {
        MapContainer mapContainer = MapContainer.getInstance();
        initMapLayer();
        //http://jira.telenav.com:8080/browse/TNANDROID-2225
//        MapContainer.getInstance().setEnableMapUI(false);
       
        mapContainer.setMapTransitionTime(0);
        mapContainer.setRenderingMode(IMapContainerConstants.RENDERING_MODE_2D_NORTH_UP);
        // TODO: Set interaction mode
        // mapContainer.setInteractionMode(IMapContainerConstants.INTERACTION_MODE_PAN_AND_ZOOM);
        int originLat = Integer.MIN_VALUE;
        int originLon = Integer.MIN_VALUE;
        int destLat = Integer.MIN_VALUE;
        int destLon = Integer.MIN_VALUE;

        boolean isOriginAvailable = false;
        boolean isDestAvailable = false;
        boolean isHandled = false;
        Address oriAddress = (Address) model.get(KEY_O_ADDRESS_ORI);
        if (oriAddress != null)
        {
            Stop originStop = oriAddress.getStop();
            if (originStop != null && originStop.isValid())
            {
                originLat = originStop.getLat();
                originLon = originStop.getLon();
            }
        }

        Address destAddress = (Address) model.get(KEY_O_ADDRESS_DEST);
        if (destAddress != null)
        {
            Stop destStop = destAddress.getStop();

            if (destStop != null && destStop.isValid())
            {
                destLat = destStop.getLat();
                destLon = destStop.getLon();
            }
        }

        if (originLat != Integer.MIN_VALUE && originLon != Integer.MIN_VALUE)
        {
            isOriginAvailable = true;
        }

        if (destLat != Integer.MIN_VALUE && destLon != Integer.MIN_VALUE)
        {
            isDestAvailable = true;
        }

        Object obj = model.get(KEY_A_ROUTE_CHOICES);
        if (obj != null)
        {
            Route[] routes = (Route[]) model.get(KEY_A_ROUTE_CHOICES);
            int selection = model.getInt(KEY_I_SELECTED_PLAN);

            if (routes == null)
            {
                return;
            }
            Vector routeNames = new Vector();
            int highlightRoute = 0;
            for (int i = 0; i < routes.length; i++)
            {
                Route route = (Route) routes[i];
                routeNames.add(route.getRouteID());
                if (selection == i || i == 0)
                {
                    highlightRoute = route.getRouteID();
                }
            }
            MapContainer.getInstance().showRoutes(routeNames, highlightRoute + "", true);
            MapContainer.getInstance().lookAtRoutes(routeNames, true);
            isHandled = true;
        }
        else
        {
            if (isOriginAvailable && isDestAvailable)
            {
                // clearRoute();
                int minLat = Math.min(originLat, destLat);
                int minLon = Math.min(originLon, destLon);
                int maxLat = Math.max(originLat, destLat);
                int maxLon = Math.max(originLon, destLon);

                if (DataUtil.isValidBoundingBox(new int[]
                { minLat, minLon, maxLat, maxLon }))
                {
//                    double minLatD = minLat / 100000.0d;
//                    double minLonD = minLon / 100000.0d;
//                    double maxLatD = maxLat / 100000.0d;
//                    double maxLonD = maxLon / 100000.0d;

                    // int[] padding = getFlagPadding();
                    // mapContainer.setMapRect(
                    // padding[0],
                    // padding[1],
                    // ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_WIDTH
                    // .getInt() - 2 * padding[0],
                    // ((RoutePlanningUiDecorator) uiDecorator).ROUTE_CHOICE_MAP_HEIGHT
                    // .getInt() - 2 * padding[1]);
                    isHandled = true;
                }
            }
            else if (isDestAvailable)
            {
                // if origin address is not ready now. e.g. current location have not been retrieved.
                // show destination address. Note: zoom level should be default.
                // clearRoute();
                double destLatD = destLat / 100000.0d;
                double destLonD = destLon / 100000.0d;
                mapContainer
                        .setRenderingMode(IMapContainerConstants.RENDERING_MODE_2D_NORTH_UP);
                mapContainer.setZoomLevel(MapConfig.MAP_DEFAULT_ZOOM_LEVEL);
                mapContainer.lookAt(destLatD, destLonD);
                isHandled = true;
            }

        }

        if (!isHandled)
        {
             clearRoute();
             showDefaultLocation();
        }

        Route[] routes = (Route[]) model.get(KEY_A_ROUTE_CHOICES);
        if (routes != null && routes.length > 0)
        {
            updateFlags(true);
        }
        else
        {
            updateFlags(false);
        }
    }
    
    private void updateRouteList(final int routePlanSelectorMiniHeight, AbstractTnComponent routePlanSelecter)
    {
        MapContainer.getInstance().removeFeature(routePlanSelecter);
        AbstractTnContainer contentContainer = new TnLinearContainer(ID_SUMMARY_CONTAINER, true, AbstractTnGraphics.TOP);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X,
            ((RoutePlanningUiDecorator) uiDecorator).ROUTE_SUMMARY_CONTAINER_X);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                    if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        return PrimitiveTypeCache.valueOf(((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_Y.getInt()
                                + ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_HEIGHT.getInt()
                                + routePlanSelectorMiniHeight);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(((RoutePlanningUiDecorator) uiDecorator).LABEL_TITLE_HEIGHT.getInt());
                    }
                }
            }));
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                    if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        return PrimitiveTypeCache.valueOf(uiDecorator.SCREEN_WIDTH.getInt());
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(uiDecorator.SCREEN_WIDTH.getInt()
                                - ((RoutePlanningUiDecorator) uiDecorator).BOTTOM_CONTAINER_WIDTH.getInt());
                    }
                }
            }));
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                    if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight()
                                - ((RoutePlanningUiDecorator) uiDecorator).BOTTOM_CONTAINER_HEIGHT.getInt()
                                - routePlanSelectorMiniHeight
                                - ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_Y.getInt()
                                - ((RoutePlanningUiDecorator) uiDecorator).CONFIRM_PANEL_HEIGHT.getInt());
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight()
                                - ((RoutePlanningUiDecorator) uiDecorator).LABEL_TITLE_HEIGHT.getInt());
                    }
                }
            }));
        contentContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        addRouteSummary(contentContainer);
        MapContainer.getInstance().addFeature(contentContainer);
        MapContainer.getInstance().addFeature(routePlanSelecter);
    }
    
    protected void updateFlags(final boolean hasRoute)
    {
        if (hasRoute)
        {
            // if origin stop is current location, we don't show origin Pin.
            // so it won't overlap with car icon.
            boolean lackOrigin = (origFlagAnnotation == null);
            boolean lackDest = (destFlagAnnotation == null);
            boolean forceUpdate = model.fetchBool(KEY_B_IS_UPDATE_FLAGS);
            if (lackOrigin || lackDest || forceUpdate)
            {
                removeFlags();
                addFlags(hasRoute);
                updateCarLocation();
                model.put(KEY_B_IS_UPDATE_FLAGS, false);
            }
        }
        else
        {
            removeFlags();
            addFlags(hasRoute);
        }
    }

    private void updateCarLocation()
    {
//        TnLocation currentLocation;
//        if (isCurrentLocation(true) || isCurrentLocation(false))
//        {
//            currentLocation = (TnLocation) model.get(KEY_O_CURRENT_LOCATION);
//        }
//        else
//        {
//            currentLocation = LocationProvider.getInstance().getCurrentLocation(
//                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
//        }
//
//        if (currentLocation != null)
//        {
//            double degreesLatitude = currentLocation.getLatitude() / 100000d;
//            double degreesLongitude = currentLocation.getLongitude() / 100000d;
//            float heading = (float) currentLocation.getHeading();
//            int meterAccuracy = currentLocation.getAccuracy();
//            MapContainer.getInstance().
//        }
    }

    private ImageAnnotation addFlag(final MapContainer container, Address address,
            AbstractTnImage flageImage)
    {
        Stop addressStop = address.getStop();
        if (addressStop != null)
        {
            int addressLat = addressStop.getLat();
            int addressLon = addressStop.getLon();
            if (addressLat != -1 && addressLon != -1)
            {
                double latD = addressLat / 100000.0d;
                double lonD = addressLon / 100000.0d;

                if (flageImage != null)
                {
                    float pivotX = 0.5f;
                    float pivotY = 0f;
                    ImageAnnotation annotation = new ImageAnnotation(flageImage, latD,
                            lonD, pivotX, pivotY,
                            ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
                    return annotation;
                }
            }
        }

        return null;
    }

    static boolean isRouteRequested = false;
    
    private void addFlags(boolean hasRoute)
    {
        Address origin = (Address) model.get(KEY_O_ADDRESS_ORI);
        Address dest = (Address) model.get(KEY_O_ADDRESS_DEST);
        if (origin == null || dest == null || origin.getStop() == null
                || dest.getStop() == null)
        {
            return;
        }

        if (hasRoute || !isCurrentLocation(true))
        {
            AbstractTnImage startFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
            origFlagAnnotation = addFlag(MapContainer.getInstance(), origin, startFlag);
            MapContainer.getInstance().addFeature(origFlagAnnotation);
        }

        AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();
        destFlagAnnotation = addFlag(MapContainer.getInstance(), dest, destFlag);
        MapContainer.getInstance().addFeature(destFlagAnnotation);
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

    protected void showDefaultLocation()
    {
        TnLocation loc = LocationProvider.getInstance().getDefaultLocation();
        double lat = loc.getLatitude() / 100000.0d;
        double lon = loc.getLongitude() / 100000.0d;
        MapContainer.getInstance().setMapTransitionTime(0);
        MapContainer.getInstance().lookAt(lat, lon);
        MapContainer.getInstance().setZoomLevel(MapConfig.MAP_MAX_ZOOM_LEVEL);
    }
   
    protected void clearRoute()
    {
        MapContainer.getInstance().clearRoute();
    }
    
    public int[] getFlagPadding()
    {
        AbstractTnImage startFlag = ImageDecorator.ORIGIN_ICON_UNFOCUSED.getImage();
        AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();
        int startFlagWidth = 0;
        int startFlagHeight = 0;
        if(startFlag != null)
        {
            startFlagWidth = startFlag.getWidth();
            startFlagHeight = startFlag.getHeight();
        }
        int destFlagWidth = 0;
        int destFlagHeight = 0;
        if(destFlag != null)
        {
            destFlagWidth = destFlag.getWidth();
            destFlagHeight = destFlag.getHeight();
        }
        
        int hPadding = (startFlagWidth + destFlagWidth) / 2;
        int vPadding = (startFlagHeight + destFlagHeight) / 2;
        return new int[]{hPadding, vPadding};
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        if (model.getState() == STATE_ROUTE_PLANNING)
        {
            final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
            final boolean isPortrait = (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT);
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    updateRoutePlanningMap(isPortrait, true, true);
                }
            });
        }
    }
    
    public void eglSizeChanged()
    {
        if (model.getState() == STATE_ROUTE_PLANNING)
        {
            if (needUpdateMap)
            {
                needUpdateMap = false;
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        updateRouteMap();
                    }
                });
            }
        }
    }

    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
            
            if(screen.getId() == STATE_ROUTE_PLANNING)
            {
                NetworkStatusManager.getInstance().addStatusListener((RoutePlanningModel)model);
            }
        }
        else if(screen != null && attached == ITnScreenAttachedListener.DETTACHED)
        {
            if(screen.getId() == STATE_ROUTE_PLANNING)
            {
                NetworkStatusManager.getInstance().removeStatusListener((RoutePlanningModel)model);
            }
        }
        
    }
    
   
    
    private void addRouteSummary(AbstractTnContainer contentContainer)
    {
        TnLinearContainer summaryContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        Route[] routes = (Route[]) model.get(KEY_A_ROUTE_CHOICES);
        int selection = model.getInt(KEY_I_SELECTED_PLAN);
        Route currentRoute = NavSdkRouteWrapper.getInstance().getRoute(routes[selection].getRouteID());
        PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        if(currentRoute != null)
        {
            Segment[] segments = currentRoute.getSegments();
            int mediumGrayColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
            int whiteColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
            if(segments != null && segments.length > 0)
            {
                for(int i = 0;  i < segments.length; i++)
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
//                    if (!isSegmentDisplayed(i))
//                    {
//                        // don't show the turn next to U-turn segment because it has no street name and very short
//                        continue;
//                    }

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

//                    if (i > 1 && segments[i - 1].getTurnType() == Route.L2L_U_TURN && i != (segments.length - 1))
//                    {
//                        streetName = segments[i + 1].getStreetName();
//                    }

                    CitizenRouteSummaryItem routeSummaryItem = UiFactory.getInstance().createCitizenRouteItem(0,
                        turnType, streetName, dist, ((RoutePlanningUiDecorator) uiDecorator).TURN_AREA_WIDTH,
                        ((RoutePlanningUiDecorator) uiDecorator).TURN_ICON_SIZE,
                        ((RoutePlanningUiDecorator) uiDecorator).ROUTE_ITEM_DIST_SIZE,
                        ((RoutePlanningUiDecorator) uiDecorator).ROUTE_ITEM_STREET_PADDING);
                    routeSummaryItem.setForegroundColor(whiteColor, mediumGrayColor);
                    routeSummaryItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).ROUTE_SUMMARY_CONTAINER_WIDTH);
                    routeSummaryItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator) uiDecorator).ROUTE_ITEM_HEIGHT);
                    routeSummaryItem.setEnabled(false);
                    summaryContainer.add(routeSummaryItem);
                }
            }
            
            TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, true);
            panel.getTnUiArgs().put(TnUiArgs.KEY_TOP_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_TOP_UNFOCUSED);
            panel.set(summaryContainer);
            contentContainer.add(panel);
        }
    }
    
    private boolean isDestinationTurnType(int turnType)
    {
        return turnType == Route.TURN_TYPE_DESTINATION_AHEAD || turnType == Route.TURN_TYPE_DESTINATION_LEFT
                || turnType == Route.TURN_TYPE_DESTINATION_RIGHT;
    }
    
    protected boolean isSegmentDisplayed(int index)
    {
        Route[] routes = (Route[]) model.get(KEY_A_ROUTE_CHOICES);
        int selection = model.getInt(KEY_I_SELECTED_PLAN);
        Route currentRoute = NavSdkRouteWrapper.getInstance().getRoute(routes[selection].getRouteID());
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
    
    private void addTitleContainer()
    {
        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(ID_TITLE_CONTAINER, false, AbstractTnGraphics.VCENTER);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) this.uiDecorator).SCREEN_WIDTH);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((RoutePlanningUiDecorator)uiDecorator).ZERO_VALUE);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((RoutePlanningUiDecorator) uiDecorator).ZERO_VALUE);
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING, IStringNav.FAMILY_NAV);

        FrogNullField titleLeftGapField = UiFactory.getInstance().createNullField(0);
        titleLeftGapField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).TITLE_LEFT_GAP);
        titleContainer.add(titleLeftGapField);
        
        
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) this.uiDecorator).LABEL_TITLE_WIDTH);

        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);
        
        FrogNullField titleRightGapField = UiFactory.getInstance().createNullField(0);
        titleRightGapField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).TITLE_RIGHT_GAP);
        titleContainer.add(titleRightGapField);
        
        FrogButton routeSettingButton = UiFactory.getInstance().createButton(0, "");
        routeSettingButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.TITLE_BUTTON_BG_FOCUSED);
        routeSettingButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.TITLE_BUTTON_BG_UNFOCUSED);
        routeSettingButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RoutePlanningUiDecorator) uiDecorator).TITLE_ICON_WIDTH);
        routeSettingButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RoutePlanningUiDecorator) uiDecorator).TITLE_ICON_HEIGHT);
        routeSettingButton.setIcon(ImageDecorator.ROUTE_PLANNING_STYLE_SETTING.getImage(),
            ImageDecorator.ROUTE_PLANNING_STYLE_SETTING.getImage(), AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        TnMenu menu = new TnMenu();
        menu.add("", CMD_CHANGE_SETTING);
        routeSettingButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        routeSettingButton.setCommandEventListener(this);
        titleContainer.add(routeSettingButton);
        
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        MapContainer.getInstance().addFeature(titleContainer);
    }
    
    protected void initMapLayer()
    {
        //we will disable aerial overlay, traffic flow, cameras or speed traps for route plan
        int layerSetting = 0; 
        boolean isConnected = NetworkStatusManager.getInstance().isConnected();
        int trafficIncidentFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_LAYER_TRAFFIC_INCIDENT);
        boolean isTrafficIncidentEnabled = false;
        if ((trafficIncidentFeature == FeaturesManager.FE_ENABLED || trafficIncidentFeature == FeaturesManager.FE_PURCHASED))
        {
            isTrafficIncidentEnabled = true;
        }
        
        MapContainer.getInstance().showMapLayer(layerSetting,isTrafficIncidentEnabled);
        MapContainer.getInstance().changeToSpriteVehicleAnnotation();
        // update day/night/satellite configuration
        MapContainer.getInstance().updateMapColor();
        MapContainer.getInstance().updateMapColorMode();
        
        isLastConnected = isConnected;
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
                case CMD_NAVIGATE:
                {
                    Object obj = model.get(KEY_A_ROUTE_CHOICES);
                    if(obj != null)
                    {
                        Route[] choices = (Route[])obj;
                        int choiceIndex = model.getInt(KEY_I_SELECTED_PLAN);
                        if(choiceIndex >= 0 && choiceIndex < choices.length)
                        {
                            // Level 1: Route 1 chosen
                            // Level 2: Route 2 chosen
                            // Level 3: Route 3 chosen
                            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_ROUTE_PLAN,
                                KontagentLogger.ROUTEPLAN_DRIVE_CLICKED, (choiceIndex + 1));
                        }
                    }
                    break;
                }
                case CMD_CHANGE_SETTING:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_ROUTE_PLAN,
                        KontagentLogger.ROUTEPLAN_OPTIONS_CLICKED);
                    break;
                }
                case CMD_SHOW_ROUTE_LIST:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_ROUTE_PLAN,
                        KontagentLogger.ROUTEPLAN_TURNLIST_DISPLAYED);
                    break;
                }
            }
        }
//        else if(tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
//        {
//            if (tnUiEvent.getKeyEvent()!= null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
//                    && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK && model.getState() == STATE_ROUTE_PLANNING)
//            {
//                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_ROUTE_PLAN, KontagentLogger.ROUTEPLAN_BACK_CLICKED);
//            }
//        }
    }
}



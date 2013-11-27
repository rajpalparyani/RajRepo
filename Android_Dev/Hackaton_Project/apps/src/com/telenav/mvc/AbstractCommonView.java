/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractCommonView.java
 *
 */
package com.telenav.mvc;

import java.util.Vector;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.view.WindowManager;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.i18n.Locale;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.res.IStringAc;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDsr;
import com.telenav.res.IStringMapDownload;
import com.telenav.res.IStringNav;
import com.telenav.res.IStringPreference;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.IWebViewContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenButtonWithBadge;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenSlidableContainer;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author fqming (fqming@telenav.cn)
 * @date Jul 20, 2010
 */
public abstract class AbstractCommonView extends AbstractBaseView implements IWebViewContainer, ITnScreenAttachedListener
{
    protected Vector webComponents;

    protected AbstractTnContainer buttonContainer;

    protected AbstractTnContainer absoluteContainer;

    public AbstractCommonView(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected final TnPopupContainer createPopupDelegate(int state)
    {
        TnPopupContainer popup = createPopup(state);

        if (popup == null)
        {
            switch (state)
            {
                case STATE_COMMON_ERROR:
                {
                    String errorMsg = model.getString(KEY_S_ERROR_MESSAGE);
                    TnMenu menu = UiFactory.getInstance().createMenu();
                    menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
                        IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                    FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
                    messageBox.setCommandEventListener(this);
                    popup = messageBox;
                    break;
                }
                case STATE_COMMON_TIMEOUT_MESSAGEBOX:
                {
                    FrogMessageBox messageBox = UiFactory.getInstance().createTimeoutMessageBox(state, model.getString(KEY_S_COMMON_MESSAGE), CMD_COMMON_OK);
                    popup = messageBox;
                    break;
                }
                case STATE_SWITCHING_REGION:
                {
                    popup = createRegionSwitchingProgressBar();
                    break;
                }
                case STATE_SWITCHING_FAIL:
                {
                    popup = createRegionSwitchFailPopup();
                    break;
                }
                case STATE_DETECTING_REGION:
                {
                    popup = createRegionDetectingProgressBar(STATE_DETECTING_REGION);
                    break;
                }
                case STATE_COMMON_EXIT_NAV_CONFIRM:
                {
                    popup = createEndNavPopup(state);
                    break;
                }
                case STATE_MAP_DOWNLOADED_STATUS_CHANGED:
                {
                    popup = createMapDownloadedStatusChangePopup(state);
                    break;
                }
                case STATE_COMMON_MAP_DOWNLOAD_ERROR:
                {
                    popup = createMapDownloadErrorMessage(state);
                    break;
                }
                case STATE_COMMON_EXIT:
                {
                    popup = createExitConfirmMessage(state);
                    break; 
                }
                case STATE_COMMON_DWF_RESOLVE_TINY_URL:
                {
                    popup = createResolveTinyUrlProgressBar(state);
                    break;
                }
                case STATE_COMMON_DWF_PARAMETER_ERROR:
                {
                    popup = createDwfErrorPrompt(state);
                    break;
                }
            }
        }
        
        setWindowsFlag(popup);

        return popup;
    }

    protected TnPopupContainer createDwfErrorPrompt(int state)
    {
        String errorMsg = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
            IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
        messageBox.setCommandEventListener(this);
        return messageBox;
    }

    private void setWindowsFlag(final TnPopupContainer popup)
    {
        if (popup == null)
        {
            return;
        }
        
        if (NavSdkNavEngine.getInstance().isRunning())
        {
            INativeUiComponent nativeComponent = popup.getNativeUiComponent();
            if (nativeComponent instanceof Dialog)
            {
                Dialog dialog = (Dialog)nativeComponent;
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            }
        }
    }

    protected TnPopupContainer createExitConfirmMessage(int state)
    {
        String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_EXIT_APP,
            IStringCommon.FAMILY_COMMON);
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        message = (message == null ? "" : message);
        menu.add(ResourceManager.getInstance().getCurrentBundle()
            .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        menu.add(ResourceManager.getInstance().getCurrentBundle()
            .getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON), CMD_COMMON_BACK);
        TnPopupContainer popup = UiFactory.getInstance().createMessageBox(state, message, menu);
        return popup;
    }

    protected TnPopupContainer createMapDownloadedStatusChangePopup(int state)
    {
        String message = MapDownLoadMessageHandler.getInstance().mapIncompatibleMessage;
        MapDownLoadMessageHandler.getInstance().setMapIncompatibleMessageDone();
        TnMenu comfirmMenu = UiFactory.getInstance().createMenu();
        comfirmMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_TAKE_ME_THERE, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        comfirmMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_NOT_RIGHT_NOW, IStringCommon.FAMILY_COMMON), CMD_COMMON_CANCEL);
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_TITLE, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
        FrogMessageBox popup = UiFactory.getInstance().createMessageBox(0, message, title, comfirmMenu, false);
        return popup;
    }
    
    protected TnPopupContainer createMapDownloadErrorMessage(int state)
    {
        String message = model.getString(KEY_S_ERROR_MESSAGE);
        
        TnMenu comfirmMenu = UiFactory.getInstance().createMenu();
        comfirmMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
            .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_TITLE, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
        FrogMessageBox popup = UiFactory.getInstance().createMessageBox(0, message, title, comfirmMenu, false);
        return popup;
    }
    
    protected TnPopupContainer createRegionDetectingProgressBar(int state)
    {
        String name = ResourceManager.getInstance().getCurrentBundle()
            .getString(IStringAc.RES_REGION_DETECTING, IStringAc.FAMILY_AC);
        return UiFactory.getInstance().createProgressBox(state, name);
    }
    
    protected TnPopupContainer createResolveTinyUrlProgressBar(int state)
    {
        String name = ResourceManager.getInstance().getCurrentBundle()
            .getString(IStringCommon.RES_LABEL_LOADING, IStringCommon.FAMILY_COMMON);
        return UiFactory.getInstance().createProgressBox(state, name);
    }
    
    private TnPopupContainer createRegionSwitchFailPopup()
    {
        String message = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringAc.RES_REGION_SWITCH_FAIL, IStringAc.FAMILY_AC);

        TnMenu commands = UiFactory.getInstance().createMenu();
        commands.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON),
            CMD_COMMON_BACK);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(0,
            message, commands);
        messageBox.setCommandEventListener(this);
        return messageBox;
    }

    private TnPopupContainer createEndNavPopup(int state)
    {
        boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
        if (isDynamicRoute && !model.fetchBool(KEY_B_IS_IGNORE_END_DETOUR))
        {
            boolean isDetour = false;
            if(DaoManager.getInstance().getTripsDao().isDetourTripExist() && NavSdkNavEngine.getInstance().isRunning())
            {
                isDetour = true;
            }
            if (isDetour)
            {
                return createDetourConfirm(state);
            }
        }
        String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_MOVING_MAP_END_TRIP_MSG,
            IStringNav.FAMILY_NAV);
        
        String endTrip = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON);
        String cancel = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
        TnMenu menu = UiFactory.getInstance().createMenu();
        message = (message == null ? "" : message);
        endTrip = (endTrip == null ? "" : endTrip);
        cancel = (cancel == null ? "" : cancel);
        menu.add(endTrip, CMD_COMMON_OK);
        menu.add(cancel, CMD_COMMON_BACK);
        TnPopupContainer popup = UiFactory.getInstance().createMessageBox(state, message, menu);
        return popup;
    }
    
    protected TnPopupContainer createDetourConfirm(int state)
    {
        String message = ResourceManager.getInstance().getCurrentBundle()
            .getString(IStringNav.RES_END_DETOUR_DETAIL, IStringNav.FAMILY_NAV);
        String endDetour = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_END_DETOUR_BTTN, IStringNav.FAMILY_NAV);
        String endTrip = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_END_TRIP_BTTN, IStringNav.FAMILY_NAV);
        String cancel = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON);
        TnMenu menu = UiFactory.getInstance().createMenu();
        message = (message == null ? "" : message);
        endTrip = (endTrip == null ? "" : endTrip);
        cancel = (cancel == null ? "" : cancel);
        menu.add(endDetour, CMD_COMMON_END_DETOUR);
        menu.add(endTrip, CMD_COMMON_OK);
        menu.add(cancel, CMD_COMMON_BACK);
        FrogMessageBox popup = UiFactory.getInstance().createMessageBox(state, message, menu);
        AbstractTnContainer content = popup.getContent();
        TnUiArgAdapter widthAdapter = null;
        if(content != null)
        {
            widthAdapter = content.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH);
        }
        
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_DETOUR, IStringNav.FAMILY_NAV);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        if(widthAdapter != null)
        {
            titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdapter);
        }
        
        popup.setTitle(titleLabel);
        return popup;
    }
    
    /*private TnPopupContainer createRegionSwitchSuccessPopup()
    {
        String countryCode=RegionUtil.getInstance().getCurrentRegion();
        StringMap supportedRegions = DaoManager.getInstance().getBillingAccountDao().getSupportedRegion();
        String displayName = supportedRegions.get(countryCode)==null?"":supportedRegions.get(countryCode);
        String name = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringAc.RES_REGION_SWITCH_SUCCESS, IStringAc.FAMILY_AC);
        String message = name+displayName;

        TnMenu commands = UiFactory.getInstance().createMenu();
        commands.add(ResourceManager.getInstance().getCurrentBundle().getString(
            IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_BACK);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(0,
            message, commands);
        messageBox.setCommandEventListener(this);
        return messageBox;
    }*/
    
    protected TnPopupContainer createRegionSwitchingProgressBar()
    {
        String name = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringAc.RES_REGION_SWITCHING, IStringAc.FAMILY_AC);
        return UiFactory.getInstance().createProgressBox(0, name);
    }
    
    protected TnPopupContainer createSearchProgressPopup()
    {
        String search = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SEARCHING, IStringCommon.FAMILY_COMMON);
        String name =  model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        if(name == null)
        {
            name = model.getString(KEY_S_COMMON_SEARCH_TEXT);
        }
        
        if (name == null)
        {
            search = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_SEARCHING, IStringDsr.FAMILY_DSR);            
        }
        else
        {
            StringConverter converter = ResourceManager.getInstance().getStringConverter();
            search = converter.convert(search, new String[]{name});
        }
        
        FrogProgressBox progressBox  = UiFactory.getInstance().createProgressBox(0, search);
        
        return progressBox;
    }
    
    protected boolean updateScreenDelegate(int state, TnScreen screen)
    {
        if(AppConfigHelper.BRAND_ATT.equals(AppConfigHelper.brandName))
        {
            AbstractTnComponent component = screen.getComponentById(CMD_COMMON_LINK_TO_AC);
            if(component != null)
            {
                CitizenButtonWithBadge buttonDriveTo = (CitizenButtonWithBadge) component;
                buttonDriveTo.setBadge(DaoManager.getInstance().getAddressDao().getUnreviewedAddressSize());
            }
        }
        return updateScreen(state, screen);
    }

    protected abstract boolean updateScreen(int state, TnScreen screen);

    protected int transformBaseCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = transformCommandDelegate(state, tnUiEvent);
        if(cmd == CMD_NONE)
        {
            switch (state)
            {
                case STATE_COMMON_TIMEOUT_MESSAGEBOX:
                {
                    cmd = CMD_COMMON_OK;
                    break;
                }
            }
            
            //handle search key event
            if(cmd == CMD_NONE && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT )
            {
                TnKeyEvent keyEvent = tnUiEvent.getKeyEvent();
                if(keyEvent.getAction() == TnKeyEvent.ACTION_UP && keyEvent.getCode() == TnKeyEvent.KEYCODE_SEARCH && hasQuickSearchBar(tnUiEvent.getComponent()))
                {
                    cmd = CMD_COMMON_GOTO_ONEBOX;
                }
            }
        }
        return cmd;
    }
    
    protected boolean hasQuickSearchBar(AbstractTnComponent abstractTnComponent)
    {
        if (abstractTnComponent != null && abstractTnComponent.getRoot() != null)
        {
            AbstractTnContainer rootContainer = abstractTnComponent.getRoot().getRootContainer();
            if(rootContainer != null)
            {
                AbstractTnComponent quickSearchBar = rootContainer.getComponentById(ID_QUICK_SEARCH_BAR);
                return quickSearchBar != null;
            }

        }
        return false;
    }

    protected abstract int transformCommandDelegate(int state, TnUiEvent tnUiEvent);

    protected abstract TnPopupContainer createPopup(int state);

    protected final TnScreen createScreenDelegate(int state)
    {
        TnScreen screen = createScreen(state);

        if(screen != null)
        {
            screen.addScreenAttachedListener(this);
        }
        
        return screen;
    }

    protected abstract TnScreen createScreen(int state);

    public static class BottomContainerArgs
    {
        /**
         * create the BottomContainerArgs.
         * 
         * @param moduleCmdId the cmd id of current module 
         * whose's icon will be highlight if has multi state.
         */
        public BottomContainerArgs(int moduleCmdId)
        {
            this.moduleCmdId = moduleCmdId;
            this.enableIcons = new boolean[5];
            for (int i = 0; i < 5; i++)
            {
                this.enableIcons[i] = true;
            }
        }
        
        public int[] cmdIds;

        public String[] displayStrs;
        
        public boolean[] enableIcons;
        
        public int[] displayStrResIds;

        public TnUiArgAdapter[] unfocusImageAdapters;

        public TnUiArgAdapter[] focusImageAdapters;
        
        public TnUiArgAdapter[] disableImageAdapters;
        
        public int moduleCmdId = -1;//for current module's cmd id.
        
        public int micIconIndex = 2;//mic icon is special, we'd better declare here.
        
        public boolean isMicShow =
            ((ResourceManager.getInstance().getCurrentLocale().equals(Locale.en_US) || ResourceManager.getInstance()
                .getCurrentLocale().equals(Locale.en_GB))
                && FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_DSR) != FeaturesManager.FE_DISABLED)
                && NetworkStatusManager.getInstance().isConnected();
    }

    protected BottomContainerArgs getDefaultBottomBarArgs(int moduleCmdId)
    {
        BottomContainerArgs args = new BottomContainerArgs(moduleCmdId);
        args.cmdIds = new int[5];
        args.displayStrResIds = new int[5];
        args.displayStrs = new String[5];
        args.unfocusImageAdapters = new TnUiArgAdapter[5];
        args.focusImageAdapters = new TnUiArgAdapter[5];
        args.disableImageAdapters = new TnUiArgAdapter[5];
        HomeScreenManager.initBottomContainerArgs(args);
       
        args.cmdIds[2] = CMD_COMMON_DSR;
        args.cmdIds[3] = CMD_COMMON_LINK_TO_SEARCH;
        args.cmdIds[4] = CMD_COMMON_LINK_TO_EXTRAS;

        // args.displayStrResIds[2] = IStringCommon.RES_BTTN_DSR; For dsr btn, there's no string
        args.displayStrResIds[3] = IStringCommon.RES_NEARBY;
        args.displayStrResIds[4] = IStringCommon.RES_BTTN_EXTRAS;
        // args.displayStrResIds[4] = IStringCommon.RES_BTTN_APP;

        args.unfocusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.unfocusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_UNFOCUS;
        args.unfocusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_EXTRA_UNFOCUS;

        args.focusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.focusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_FOCUS;
        args.focusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_EXTRA_FOCUS;
        
        args.disableImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_DISABLE;

        
        for(int i = 0; i < 5; i++)
        {
            String str = ResourceManager.getInstance().getCurrentBundle()
            .getString(args.displayStrResIds[i], IStringCommon.FAMILY_COMMON);
            args.displayStrs[i] = str;
        }
        return args;
    }
    

    private boolean checkBottomBarArgs(BottomContainerArgs bottomBarArgs)
    {
        // check available.
        if (bottomBarArgs == null || bottomBarArgs.cmdIds == null || bottomBarArgs.unfocusImageAdapters == null)
            return false;

        // check available.
        if (bottomBarArgs.cmdIds.length != bottomBarArgs.unfocusImageAdapters.length)
            return false;

        return true;
    }

    protected void addBottomContainer(CitizenScreen screen, BottomContainerArgs bottomBarArgs)
    {
        if (screen == null || !checkBottomBarArgs(bottomBarArgs))
            return;
        buttonContainer = createBottomContainer(screen.getTitleContainer(), bottomBarArgs);
        
        addAbsoluteContainerToScreen(screen, buttonContainer);
    }
    
    protected void applyBottomBarPos(final AbstractTnContainer titleContainer, AbstractTnContainer bottomButtonContainer)
    {
        TnUiArgs containerArgs = bottomButtonContainer.getTnUiArgs();
        containerArgs.put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        }));
        if (titleContainer != null && titleContainer.getChildrenSize() > 0)
        {
            containerArgs.put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(uiDecorator.BOTTOM_BAR_Y_POS.getInt()
                            - titleContainer.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt());
                }
            }));
        }
        else
        {
            containerArgs.put(TnUiArgs.KEY_POSITION_Y, uiDecorator.BOTTOM_BAR_Y_POS);
        }
    }

    protected AbstractTnContainer createBottomContainer(final AbstractTnContainer titleContainer, final BottomContainerArgs bottomBarArgs)
    {
        return createBottomContainer(titleContainer, bottomBarArgs, true, true);
    }
    
    protected AbstractTnContainer createBottomContainer(final AbstractTnContainer titleContainer, final BottomContainerArgs bottomBarArgs, final boolean isNeedBdage, final boolean isNeedSetPos)
    {
        int size = bottomBarArgs.cmdIds.length;
        final boolean isMicShow = bottomBarArgs.isMicShow;
        
        //make dsr btn a square
        final TnUiArgs dsrBtnArgs = new TnUiArgs();
        dsrBtnArgs.put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.MIC_ICON_HEIGHT);
        dsrBtnArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.MIC_ICON_HEIGHT);
        
        final TnUiArgs buttonArgs = new TnUiArgs();
        buttonArgs.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                if (AppConfigHelper.isTabletSize())
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 780 / 10000);
                }
                else
                {
                    int dsrWidth = isMicShow ? dsrBtnArgs.get(TnUiArgs.KEY_PREFER_WIDTH).getInt() : 0;
                    return PrimitiveTypeCache.valueOf((AppConfigHelper.getDisplayWidth() - dsrWidth) / (bottomBarArgs.cmdIds.length - 1));
                }
            }
        }));
        buttonArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.BOTTOM_BAR_HEIGHT);
        

        AbstractTnContainer container;
        if(AppConfigHelper.isTabletSize())
        {
            container = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        }
        else
        {
            container = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.VCENTER);
        }
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.BOTTOM_BAR_HEIGHT);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BOTTOM_NAV_BAR);
        container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_NAV_BAR);
        AbstractTnFont btnFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LINK_ICON_LABEL);

        for (int i = 0; i < size; i++)
        {
            boolean isCurrentModule = bottomBarArgs.cmdIds[i] == bottomBarArgs.moduleCmdId;
            if (bottomBarArgs.micIconIndex == i && !isMicShow)
            {
                continue;
            }

            FrogButton button = null;
            if (bottomBarArgs.displayStrs != null && bottomBarArgs.displayStrs.length > i && bottomBarArgs.displayStrs[i] != null)
            {
                if( AppConfigHelper.BRAND_ATT.equals(AppConfigHelper.brandName) && bottomBarArgs.cmdIds[i] == CMD_COMMON_LINK_TO_AC && isNeedBdage) 
                {
                    button = new CitizenButtonWithBadge(bottomBarArgs.cmdIds[i], bottomBarArgs.displayStrs[i], 
                        bottomBarArgs.focusImageAdapters[i].getImage(), bottomBarArgs.unfocusImageAdapters[i].getImage(), null);
                   ((CitizenButtonWithBadge)button).setBadge(DaoManager.getInstance().getAddressDao().getUnreviewedAddressSize());
                    ((CitizenButtonWithBadge)button).setBadgePosition(AbstractTnGraphics.TOP);
                    ((CitizenButtonWithBadge)button).setBadgeColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), 
                        UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
                    ((CitizenButtonWithBadge)button).setBadgeFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BADGE));
                }
               else
                {
                    button = new FrogButton(bottomBarArgs.cmdIds[i], bottomBarArgs.displayStrs[i]);
                }
            }
            else if(bottomBarArgs.displayStrResIds[i] > 0)
            {
                String str = ResourceManager.getInstance().getCurrentBundle()
                .getString(bottomBarArgs.displayStrResIds[i], IStringCommon.FAMILY_COMMON);
                button = new FrogButton(bottomBarArgs.cmdIds[i], str);
                button.setPadding(0, button.getTopPadding(), 0, button.getBottomPadding());
            }
            else
            {
                button = new FrogButton(bottomBarArgs.cmdIds[i]);
            }

            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", isCurrentModule ? CMD_NONE : bottomBarArgs.cmdIds[i]);
            button.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            button.setCommandEventListener(this);
            button.setFont(btnFont);
            button.setEnabled(bottomBarArgs.enableIcons[i]);
            
            /**
             * FIXME:qli, set bold font for default bold font is too big
             * fix bug http://jira.telenav.com:8080/browse/TN-3898
             */
            button.setBoldFont(btnFont);

            if (bottomBarArgs.focusImageAdapters != null
                    && bottomBarArgs.focusImageAdapters.length == bottomBarArgs.unfocusImageAdapters.length)
            {
                button.setIcon(bottomBarArgs.focusImageAdapters[i].getImage(), isCurrentModule ? bottomBarArgs.focusImageAdapters[i].getImage() 
                        : bottomBarArgs.unfocusImageAdapters[i].getImage(), AbstractTnGraphics.TOP);
                if(!button.isEnabled() && (bottomBarArgs.disableImageAdapters != null) && bottomBarArgs.disableImageAdapters[i] != null)
                    button.setIcon(bottomBarArgs.disableImageAdapters[i].getImage(), bottomBarArgs.disableImageAdapters[i].getImage() 
                        , AbstractTnGraphics.TOP);
            }
            else
            {
                button.setIcon(bottomBarArgs.unfocusImageAdapters[i].getImage(), bottomBarArgs.unfocusImageAdapters[i].getImage(),
                    AbstractTnGraphics.TOP);
                if(!button.isEnabled() && (bottomBarArgs.disableImageAdapters != null) && bottomBarArgs.unfocusImageAdapters[i] != null)
                    button.setIcon(bottomBarArgs.disableImageAdapters[i].getImage(), bottomBarArgs.disableImageAdapters[i].getImage() 
                        , AbstractTnGraphics.TOP);
            }
            
            
            
            int color = isCurrentModule ? UiStyleManager.getInstance().getColor(UiStyleManager.BOTTOM_BAR_COLOR_FOCUS)
                    : UiStyleManager.getInstance().getColor(UiStyleManager.BOTTOM_BAR_COLOR_UNFOCUS);

            button.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BOTTOM_BAR_COLOR_FOCUS),
                color);
            

            if (bottomBarArgs.micIconIndex == i)
            {
                button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MIC_BUTTON_BG_FOCUS);
                button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MIC_BUTTON_BG_UNFOCUS);
                button.getTnUiArgs().copy(dsrBtnArgs);
                
                TnLinearContainer micContainer;
                if(AppConfigHelper.isTabletSize())
                {
                    micContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
                    micContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                        new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                        {
                            public Object decorate(TnUiArgAdapter args)
                            {
                                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 1544 / 10000);
                            }
                        }));
                }
                else
                {
                    micContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.TOP);
                    micContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.MIC_ICON_HEIGHT);
                }
                micContainer.add(button);
                micContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.BOTTOM_BAR_HEIGHT);
                
                container.add(micContainer);
            }
            else
            {
                int topPad = this.uiDecorator.BOTTOM_BAR_HEIGHT.getInt() - this.uiDecorator.BOTTOM_BAR_REAL_HEIGHT.getInt();
                /**
                 * FIXME:qli, 
                 * set bottom padding zero for let button to be center in Bottom Container.
                 * fix bug http://jira.telenav.com:8080/browse/TN-3898
                 * 
                 * set left and right padding zero for avoid cut off long string.
                 */
                button.setPadding(0, topPad, 0, 0);
                if(AppConfigHelper.isTabletSize())
                {
                    final TnUiArgs tabletArgs = new TnUiArgs();
                    tabletArgs.copy(buttonArgs);
                    tabletArgs.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                    {
                        public Object decorate(TnUiArgAdapter args)
                        {
                            if (isMicShow)
                            {
                                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 1578 / 10000);
                            }
                            else
                            {
                                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 1969 / 10000);
                            }
                        }
                    }));
                    button.getTnUiArgs().copy(tabletArgs);
                }
                else
                {
                    button.getTnUiArgs().copy(buttonArgs);
                }
                container.add(button);
            }

        }
        
        if(isNeedSetPos)
        {
            applyBottomBarPos(titleContainer, container);
        }

        return container;
    }

    protected void hideBottomContainer()
    {
        if (absoluteContainer != null && buttonContainer != null)
        {
            absoluteContainer.remove(buttonContainer);
        }
    }

    protected void showBottomContainer()
    {
        if (absoluteContainer != null && buttonContainer != null)
        {
            if (buttonContainer.getParent() != null)
            {
                ((AbstractTnContainer) buttonContainer.getParent()).remove(buttonContainer);
            }
            absoluteContainer.add(buttonContainer);
        }
    }

    private void addAbsoluteContainerToScreen(CitizenScreen screen, AbstractTnContainer container)
    {
        TnLinearContainer contentContainer = (TnLinearContainer) screen.getContentContainer();
        TnLinearContainer linearContainer = new TnLinearContainer(0, true);
        if (contentContainer.getChildrenSize() > 0)
        {
            int size = contentContainer.getChildrenSize();
            for (int i = 0; i < size; i++)
            {
                AbstractTnComponent component = contentContainer.get(0);
                contentContainer.remove(component);
                linearContainer.add(component);
            }
        }

        absoluteContainer = new TnAbsoluteContainer(0);
        linearContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            }));
        linearContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            }));
        absoluteContainer.add(linearContainer);
        absoluteContainer.add(container);

        contentContainer.add(absoluteContainer);
    }
    
    protected int createContextMenuId(int componentId, int commandId)
    {
        int index = componentId << 24;
        int menuId = index + commandId;
        return menuId;
    }
    
    protected int retrieveComponentId(int menuId)
    {
        int index = menuId >> 24;
        return index;
    }
    
    protected int retrieveCommandId(int menuId)
    {
        int commandId = menuId & 0x00FFFFFF;
        return commandId;
    }
    
    protected boolean isComponentNeeded(int backGroundImageId)
    {
       return UiStyleManager.getInstance().isComponentNeeded(backGroundImageId);
    }
    
    protected String getRegion()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) this.model.get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
        MandatoryProfile profile;
        if (userProfileProvider != null)
        {
            profile = userProfileProvider.getMandatoryNode();
        }
        else
        {
            MandatoryNodeDao mandatoryNode = DaoManager.getInstance()
                    .getMandatoryNodeDao();
            profile = mandatoryNode.getMandatoryNode();
        }
        return profile.getUserInfo().region;        
    }
    
    public void addWebView(CitizenWebComponent webComponent)
    {
        if(webComponents == null)
        {
            webComponents = new Vector();
        }
        webComponents.add(webComponent);
    }
    
    protected void popAllViews()
    {
        super.popAllViews();
        
        if(isNeedDelayClearWebView())
        {
            Thread t = new Thread()
            {
                Object o = new Object();
                public void run()
                {
                    try
                    {
                        synchronized(o)
                        {
                            o.wait(1000);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    
                    clearWebView();
                }
            };
            t.start();
        }
        else
        {
            clearWebView();
        }
    }
    
    protected boolean isNeedDelayClearWebView()
    {
        return false;
    }
    
    protected void clearWebView()
    {
        if(webComponents != null && webComponents.size() > 0)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if(webComponents != null)
                    {
                        try
                        {
                            for (int i = 0; i < webComponents.size(); i++)
                            {
                                CitizenWebComponent webComponent=((CitizenWebComponent) webComponents.elementAt(i));
                                if(webComponent.getParent() != null)
                                {
                                    ((AbstractTnContainer)webComponent.getParent()).removeNativeComponentInLayout(webComponent);
                                }
                                webComponent.freeMemory();
                                webComponent.destroy();
                            }
                            
                        }
                        catch (Exception e)
                        {
                            
                        }
                        finally
                        {
                            webComponents = null;
                        }
                    }
                }
            });
        }
    }
    
    protected TnScreen createGeneralFeedbackScreen(int state, int screenBackgroundColor)
    {
        BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.COMMON_FEEDBACK_DOMAIN_ALIAS);
        CitizenScreen feedbackScreen = UiFactory.getInstance().createScreen(state);
        feedbackScreen.getRootContainer().setBackgroundColor(screenBackgroundColor);
        AbstractTnContainer container = feedbackScreen.getContentContainer();
        CitizenWebComponent feedbackWebComponent = UiFactory.getInstance().createCitizenWebComponent(this, 0, null, false);
        feedbackWebComponent.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler)model);
        String url = sessionArgs.getUrl();
        
        url = BrowserSdkModel.addEncodeTnInfo(url, "");
        
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        
        String clientLogSwitche = DaoManager.getInstance().getServerDrivenParamsDao().getValue(ServerDrivenParamsDao.CLIENT_LOG_SWITCH);
        
        if("enable".equals(clientLogSwitche))
        {
            url = url + "&needSdCardLog=true";
        }
        else
        {
            url = url + "&needSdCardLog=false";
        }
        
        feedbackWebComponent.loadUrl(url);
        container.add(feedbackWebComponent);
        return feedbackScreen;
    }
    
    protected boolean isCredentialInfoExisted()
    {
        if (model.fetchBool(KEY_B_IS_CREATE_ACCOUNT))
        {
            CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
            if(credentialInfo != null && credentialInfo.credentialValue != null && credentialInfo.credentialValue.trim().length() > 0)
            {
                return true;
            }
        }
        return false;
    }
    
    protected CitizenSlidableContainer notificationContainer;
    protected void showNotificationContainer(int screenId)
    {
        if(notificationContainer != null)
        {
            notificationContainer.hideImmediately();
            notificationContainer = null;
        }
        String toastStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_SIGN_UP_SUCCESS, IStringPreference.FAMILY_PREFERENCE);
        int whiteColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
        AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR);
        FrogLabel label = UiFactory.getInstance().createLabel(0, toastStr);
        label.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
        label.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
        label.setForegroundColor(whiteColor, whiteColor);
        label.setFont(font);
        label.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, this.uiDecorator.NOTIFICATION_STATUS_X);
        label.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, this.uiDecorator.NOTIFICATION_STATUS_Y);
        label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.NOTIFICATION_STATUS_WIDTH);
        label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.uiDecorator.NOTIFICATION_STATUS_HEIGHT);
        int hPadding = (this.uiDecorator.NOTIFICATION_STATUS_WIDTH.getInt() - font.stringWidth(toastStr)) / 2;
        label.setPadding(hPadding, 0, hPadding, 0);
        
        notificationContainer = UiFactory.getInstance().createSlidableContainer(ID_TOAST_SLIDERBAL_CONTAINER);
        notificationContainer.setContent(label);
        notificationContainer.setTimeout(3000, CMD_HIDE_NOTIFICATION);
        notificationContainer.setAnimationDuration(0, 0);
        int x = this.uiDecorator.NOTIFICATION_STATUS_X.getInt();
        int y = this.uiDecorator.NOTIFICATION_STATUS_Y.getInt();
        int w = this.uiDecorator.NOTIFICATION_STATUS_WIDTH.getInt();
        int h = this.uiDecorator.NOTIFICATION_STATUS_HEIGHT.getInt();
        TnScreen screen = this.getScreenByState(screenId);
        notificationContainer.showAt(screen.getRootContainer(), x, y, w, h, false);
    }
    
    public void onScreenUiEngineAttached(TnScreen screen, int attached)
    {
        
    }
    
    protected ColorStateList getColorState(int id)
    {
        ColorStateList colorList = null;
        XmlResourceParser xrp = AndroidCitizenUiHelper.getResources().getXml(id);
        try
        {
            colorList = ColorStateList.createFromXml(AndroidCitizenUiHelper.getResources(), xrp);
        }
        catch (Exception e)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "ColorStateList.createFromXml get error!");
        }
        return colorList;
    }
    
    protected void removeMenuById(TnScreen screen, int id)
    {
        if (screen == null || id < 0)
            return;
        TnMenu screenMenu = screen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);
        screenMenu.remove(id);
    }
}

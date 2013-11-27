/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AcView.java
 *
 */
package com.telenav.module.ac;

import java.util.Enumeration;
import java.util.Vector;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.IStringAc;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenButton;
import com.telenav.ui.citizen.CitizenButtonWithBadge;
import com.telenav.ui.citizen.CitizenCheckBoxPopup;
import com.telenav.ui.citizen.CitizenDropDownComboBox;
import com.telenav.ui.citizen.CitizenQuickSearchBar;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-13
 */
class AcViewTouch extends AbstractCommonView implements IAcConstants
{
    protected int lastOrientation = 0;
    String[] regions;
    String[] regionCodes;
    public AcViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        
        int eventType = tnUiEvent.getType();
        AbstractTnComponent component = tnUiEvent.getComponent();
        switch(eventType)
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                if((component instanceof FrogButton || component instanceof FrogLabel)
                        && component.getId() == ID_CHANGE_REGION_DROP_DOWN
                        && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_UP)
                {
                    cmd = CMD_CHANGE_REGION;
                }
                break;
            }
        }
        
        return cmd;
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popupContainer = null;
        switch (state)
        {
            case STATE_GET_CURRENT_LOCATION:
            {
                return createGetCurrentLocationPopup(state);
            }
            case STATE_RGC:
            {
                return createRGCPopup(state);
            }
            case STATE_CHANGE_REGION:
            {
                return createChangeRegionPopup(state);
            }
            case STATE_REGION_SWITCH_FAILED:
            {
                return createRegionSwitchFailPopup(state);
            }
            case STATE_CHECK_RETURNED_ADDRESS_REGION:
            {
                return createCheckRegionProgressBox(state);
            }
        }
        return popupContainer;
    }

    private TnPopupContainer createRegionSwitchFailPopup(int state)
    {
        String message = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringAc.RES_REGION_SWITCH_FAIL, IStringAc.FAMILY_AC);

        TnMenu commands = UiFactory.getInstance().createMenu();
        commands.add(ResourceManager.getInstance().getCurrentBundle().getString(
            IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state,
            message, commands);
        messageBox.setCommandEventListener(this);
        return messageBox;
    }
    
    private TnPopupContainer createNonCancelableProgressBar(int state, String message)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(state,
            message);
        progressBox.setCancelMenu(null);
        return progressBox;
    }
    
    private TnPopupContainer createCheckRegionProgressBox(int state)
    {
        String name = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringAc.RES_REGION_DETECTING, IStringAc.FAMILY_AC);

        return createNonCancelableProgressBar(state, name);
    }
    
    private TnPopupContainer createRGCPopup(int state)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(
            state,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAc.RES_LABEL_PARSING_LOCATION, IStringAc.FAMILY_AC));
        return progressBox;
    }

    private TnPopupContainer createGetCurrentLocationPopup(int state)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(
            state, ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAc.RES_GET_CURRENT_LOCATION, IStringAc.FAMILY_AC));
        return progressBox;
    }

    private TnPopupContainer createChangeRegionPopup(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_SAVE, IStringCommon.FAMILY_COMMON),
            CMD_CHANGE_REGION_SAVE);
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON),
            CMD_COMMON_BACK);
        
        int selectedIndex = 0;
        if(regions != null)
        {
            String currentRegion = getRegion();
            for (int i = 0; i < regions.length; i++)
            {
                if (currentRegion.equals(regionCodes[i]))
                {
                    selectedIndex = i;
                }
            }
        }
        
        CitizenCheckBoxPopup popup = UiFactory.getInstance().createCitizenCheckBoxPopup(state, "", menu);
        popup.addCheckBox(regions, false, selectedIndex);
        popup.setCommandEventListener(this);
        
        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) this.uiDecorator).REGION_POPUP_TITLE_CONTAINER_HEIGHT);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_CHANGE_REGION, IStringAc.FAMILY_AC));
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor( UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setFont(UiStyleManager.getInstance().getFont( UiStyleManager.FONT_POPUP_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);     
        popup.setTitle(titleContainer);
        
        return popup;
    }
    
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                return createAcMainScreen(state);
            }
        }
        return null;
    }

    private TnScreen createAcMainScreen(int state)
    {
        CitizenScreen mainScreen = UiFactory.getInstance().createScreen(state);

        addTitleContainer(mainScreen);
        
        TnLinearContainer contentContainer = (TnLinearContainer) mainScreen.getContentContainer();
        contentContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.AC_BUTTON_BACKGROUND));
        
        final TnUiArgAdapter searchbarHeight;
        if(!model.getBool(KEY_B_IS_CHOOSING_LOCATION))
        {
            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
            this.model.put(KEY_B_IS_ONBOARD, isOnboard);
            String hint = isOnboard ? ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON)
                    :ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON);
            AbstractTnImage micImage = isOnboard ? ImageDecorator.SEARCHBOX_MIC_DISABLED.getImage() : ImageDecorator.SEARCHBOX_MIC_UNFOCUSED.getImage();
            CitizenQuickSearchBar citizenQuickSearchBar = UiFactory.getInstance().createQuickSearchBar(ID_QUICK_SEARCH_BAR, CMD_COMMON_GOTO_ONEBOX, FrogTextField.UNEDITABLE, hint,
                micImage, ImageDecorator.IMG_AC_BACKSPACE.getImage(), this, false);
            citizenQuickSearchBar.setTextFieldLeftCommandId(isOnboard ? CMD_NONE : CMD_COMMON_DSR);
            citizenQuickSearchBar.setTextFieldCommandEventListener(this);
            citizenQuickSearchBar.setCommandEventListener(this);
            citizenQuickSearchBar.getTnUiArgs().put(CitizenQuickSearchBar.KEY_TEXT_FIELD_WIDTH, ((AcUiDecorator)uiDecorator).ONE_BOX_EDIT_TEXT_WIDTH);
            searchbarHeight = citizenQuickSearchBar.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT);
            TnLinearContainer container = UiFactory.getInstance().createLinearContainer(ID_SEARCH_BAR_CONTAINER, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
            container.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SCREEN_FILTER_BG_COLOR));
            contentContainer.add(container);
            container.add(citizenQuickSearchBar);
        }
        else
        {
            searchbarHeight = null;
        }
        
        final boolean needSpliteLine = isComponentNeeded(INinePatchImageRes.SPLITE_LINE + INinePatchImageRes.ID_UNFOCUSED);
        if (needSpliteLine)
        {
            FrogLabel splitLine = UiFactory.getInstance().createLabel(0, "");
            splitLine.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED);
            splitLine.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED);
            splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.SCREEN_SPLIT_LINE_HEIGHT);
            splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_SPLIT_LINE_WIDTH);
            contentContainer.add(splitLine);
        }
        
        StringMap regions=DaoManager.getInstance().getBillingAccountDao().getSupportedRegion();
        boolean isMoreThanOneSupportedRegion= regions !=null && regions.size()>1?true:false;
        final boolean needRegion = ((AcModel)model).isInternationalAddressCapture() 
        && ! NavRunningStatusProvider.getInstance().isNavRunning()
        && isMoreThanOneSupportedRegion;  
        if(needRegion)
        {
            prepareSupportedRegions();
            TnLinearContainer changeRegionContainer = UiFactory.getInstance().createLinearContainer(ID_CHANGE_REGION_CONTAINER, false,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            changeRegionContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) this.uiDecorator).REGION_CONTAINER_WIDTH);
            changeRegionContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,((AcUiDecorator) this.uiDecorator).REGION_CONTAINER_HEIGHT);

            AbstractTnImage iconImage = ImageDecorator.IMG_AC_MAIN_REGION_UNFOCUSED.getImage();
            FrogImageComponent regionIcon = UiFactory.getInstance().createFrogImageComponent(0, iconImage);
            regionIcon.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_REGION_ICON_WIDTH);
            regionIcon.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_REGION_ICON_WIDTH);
            regionIcon.setAnchor(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

            FrogNullField nullField = UiFactory.getInstance().createNullField(0);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_BUTTON_HORIZONTAL_GAP);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_BUTTON_HEIGHT);
            nullField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.AC_BUTTON_BACKGROUND));

            CitizenDropDownComboBox comboBox = createChangeRegionComboBox();

            FrogNullField nullField2 = UiFactory.getInstance().createNullField(0);
            nullField2.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_BUTTON_HORIZONTAL_GAP);
            nullField2.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_BUTTON_HEIGHT);
            nullField2.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.AC_BUTTON_BACKGROUND));

            changeRegionContainer.add(nullField);
            changeRegionContainer.add(regionIcon);
            changeRegionContainer.add(comboBox);
            changeRegionContainer.add(nullField2);
            
            FrogLabel splitLine = new FrogLabel(0, null);
            splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).SCREEN_WIDTH);
            splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).CHOOSELOCATION_SPLIT_LINE);
            splitLine.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_SPLIT_LINE_COLOR));
            
            contentContainer.add(changeRegionContainer);
            contentContainer.add(splitLine);
        }
        
        TnLinearContainer panelContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        contentContainer.add(panelContainer);
        TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, true);
        panelContainer.add(panel);
        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(ID_AC_BUTTON_CONTAINER, true, AbstractTnGraphics.HCENTER);
        panel.set(buttonContainer);
        fillACButtonContainer(buttonContainer);
        
        panelContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,  new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int height = 0;
                if(searchbarHeight != null)
                {
                    height += searchbarHeight.getInt();
                }
                if(needSpliteLine)
                {
                    height += AcViewTouch.this.uiDecorator.SCREEN_SPLIT_LINE_HEIGHT.getInt();
                }
                if(needRegion)
                {
                    height += ((AcUiDecorator)AcViewTouch.this.uiDecorator).REGION_CONTAINER_HEIGHT.getInt();
                    height += ((AcUiDecorator)AcViewTouch.this.uiDecorator).CHOOSELOCATION_SPLIT_LINE.getInt();
                }
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - height);
            }
        }));
        return mainScreen;
    }
    
    private void addTitleContainer(CitizenScreen mainScreen)
    {
        TnLinearContainer titleContainer = (TnLinearContainer) mainScreen.getTitleContainer();
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        int acType = model.getInt(KEY_I_AC_TYPE);
        String title = "";
        switch (acType)
        {
            case TYPE_AC_FROM_FAV:
            {
                title = FrogTextHelper.BOLD_START
                        + ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_ADD_FAV_TITLE, IStringAc.FAMILY_AC)
                        + FrogTextHelper.BOLD_END;
                break;
            }
            case TYPE_AC_FROM_NAV:
            case TYPE_AC_FROM_DASHBOARD:
            {
                title = FrogTextHelper.BOLD_START
                + ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_DRIVE, IStringCommon.FAMILY_COMMON) + FrogTextHelper.BOLD_END;
                break;
            }
            default:
            {
                title = FrogTextHelper.BOLD_START
                        + ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_CHOOSE_LOCATION, IStringAc.FAMILY_AC)
                        + FrogTextHelper.BOLD_END;
                break;
            }
        }

        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleContainer.add(titleLabel);
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                switch (commandId)
                {
                    case CMD_COMMON_GOTO_ONEBOX:
                    {
                        CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                                .getCurrentScreen();
                        if (currentScreen != null)
                        {
                            CitizenQuickSearchBar citizenQuickSearchBar = (CitizenQuickSearchBar) currentScreen
                                    .getComponentById(ID_QUICK_SEARCH_BAR);
                            citizenQuickSearchBar.setTextFieldText("");
                        }
                        break;
                    }

                }
                break;
            }
            case STATE_CHANGE_REGION:
            {
                switch(commandId)
                {
                    case CMD_CHANGE_REGION_SAVE:
                    {
                        TnPopupContainer popup = getCurrentPopup();
                        if(popup != null && popup instanceof CitizenCheckBoxPopup)
                        {
                            int index = ((CitizenCheckBoxPopup) popup).getSelectedIndex();
                            if (index >= 0 && regionCodes != null)
                            {
                                model.put(KEY_S_CURRENT_REGION, regionCodes[index]);
                            }
                        }
						break;
                    }
                }
				break;
            }
        }
        return super.prepareModelData(state, commandId);
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();

        switch (state)
        {
            case STATE_MAIN:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                        && tnUiEvent.getCommandEvent().getCommand() == CMD_COMMON_GOTO_ONEBOX)
                {
                    if (tnUiEvent.getComponent() instanceof FrogTextField)
                    {
                        FrogTextField textField = (FrogTextField) tnUiEvent.getComponent();
                        String text = textField.getText();
                        this.model.put(KEY_S_COMMON_SEARCH_TEXT, text);
                        textField.setText("");
                    }
                }
                break;
            }
            default:
                break;

        }
        return super.preProcessUIEvent(tnUiEvent);

    }

    private void fillACButtonContainer(TnLinearContainer contentContainer)
    {
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        Vector buttons = initButtonList();
        initAcMainButton(contentContainer, buttons);
    }

    private Vector initButtonList()
    {
        boolean hasCurrentLoc=false;
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        Vector buttons = new Vector();
        
        if (model.getBool(KEY_B_IS_CHOOSING_LOCATION) && model.getBool(KEY_B_IS_CURRENT_LOCATION_NEEDED))
        {
            FrogButton button = createButton(IAcConstants.ID_CURRENT_LOCATION, bundle.getString(IStringAc.RES_BTN_CURRENT_LOCATION, IStringAc.FAMILY_AC),ImageDecorator.IMG_AC_ICON_CURRENT_LOCATION_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_CURRENT_LOCATION_UNFOCUSED.getImage(), CMD_CURRENT_LOACTION, true);
            buttons.addElement(button);
            hasCurrentLoc = true;
        }

        if(hasCurrentLoc)
        {
            buttons.addElement(createButton(ID_ADDRESS_BUTTON, bundle.getString(IStringAc.RES_BTN_ADDRESS, IStringAc.FAMILY_AC),
                ImageDecorator.IMG_AC_ICON_ADDRESS_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_ADDRESS_UNFOCUSED.getImage(), CMD_ADDRESS,
                true));
        }
        
        if (model.getInt(KEY_I_AC_TYPE) != TYPE_AC_FROM_FAV)
        {
            CitizenButton homeButton = createButton(ID_HOME_BUTTON, bundle.getString(IStringAc.RES_BTTN_HOME, IStringAc.FAMILY_AC),
                ImageDecorator.IMG_AC_ICON_HOME_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_HOME_UNFOCUS.getImage(), CMD_HOME, true);
            buttons.addElement(homeButton);

            CitizenButton workButton = createButton(ID_WORK_BUTTON, bundle.getString(IStringAc.RES_BTN_WORK, IStringAc.FAMILY_AC),
                ImageDecorator.IMG_AC_ICON_BUSSINESSES_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_BUSSINESSES_UNFOCUS.getImage(), CMD_WORK,
                true);
            buttons.addElement(workButton);
        }

        String recentLabel = bundle.getString(IStringAc.RES_BTTN_RECENT, IStringAc.FAMILY_AC);
         buttons.addElement(createButton(ID_RECENT_BUTTON,  recentLabel, ImageDecorator.IMG_AC_ICON_RECNET_FOCUSED.getImage(),
            ImageDecorator.IMG_AC_ICON_RECNET_UNFOCUS.getImage(), CMD_RECENT, true));
        
        if(!hasCurrentLoc)
        {
        buttons.addElement(createButton(ID_ADDRESS_BUTTON, bundle.getString(IStringAc.RES_BTN_ADDRESS, IStringAc.FAMILY_AC),
            ImageDecorator.IMG_AC_ICON_ADDRESS_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_ADDRESS_UNFOCUSED.getImage(), CMD_ADDRESS,
            true));
        }
        
        if (model.getInt(KEY_I_AC_TYPE) != TYPE_AC_FROM_FAV)
        {         
            CitizenButton favButton = createBadgeButton(ID_FAVORITE_BUTTON, bundle.getString(IStringAc.RES_BTTN_FAVORITES, IStringAc.FAMILY_AC));
            buttons.addElement(favButton);
        }

		if(!model.getBool(KEY_B_IS_CHOOSING_LOCATION) || model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_FAV)
        {
            buttons.addElement(createButton(ID_PLACE_BUTTON, bundle.getString(IStringCommon.RES_NEARBY, IStringCommon.FAMILY_COMMON),
                ImageDecorator.IMG_AC_ICON_PLACES_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_PLACES_UNFOCUSED.getImage(), CMD_PLACES, true));
        }
        
        buttons.addElement(createButton(ID_CONTACT_BUTTON, bundle.getString(IStringAc.RES_BTTN_CONTACTS, IStringAc.FAMILY_AC),
            ImageDecorator.IMG_AC_ICON_CONTACTS_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_CONTACTS_UNFOCUS.getImage(), CMD_CONTACTS,
            true));
        buttons.addElement(createButton(ID_AIRPORT_BUTTON, bundle.getString(IStringAc.RES_BTTN_AIRPORTS, IStringAc.FAMILY_AC),
            ImageDecorator.IMG_AC_ICON_AIRPORTS_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_AIRPORTS_UNFOCUS.getImage(), CMD_AIRPORTS,
            true));    
        
        return buttons;
    }

    protected CitizenDropDownComboBox createChangeRegionComboBox()
    {
        int foregroundColor = UiStyleManager.getInstance().getColor(UiStyleManager.BUTTON_DEFAULT_UNFOCUSED_COLOR);
        
        CitizenDropDownComboBox changeRegionComboBox = UiFactory.getInstance().createCitizenDropDownComboBox(ID_CHANGE_REGION_DROP_DOWN);
        changeRegionComboBox.setTouchEventListener(this);
        changeRegionComboBox.setForegroundColor(foregroundColor, foregroundColor);
        changeRegionComboBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_AC_SWITCH_REGION_COMBO_FONT));
        changeRegionComboBox.setIndicateIcon(ImageDecorator.POI_LIST_DROP_DOWN_COMBOBOX_INDICATOR_FOCUSED.getImage(), ImageDecorator.POI_LIST_DROP_DOWN_COMBOBOX_INDICATOR_FOCUSED.getImage());

        int index = 0;
        String currentRegion = this.getRegion();
        if(regionCodes != null)
        {
            for(int i = 0; i < regionCodes.length; i++)
            {
                if(currentRegion.equals(regionCodes[i]))
                {
                    index = i;
                    break;
                }
            }
        }
        
        if(regions != null)
        {
            changeRegionComboBox.setItemTitles(regions);
            changeRegionComboBox.setSelectedIndex(index);
        }
        
        TnUiArgs tnUiArgs = changeRegionComboBox.getTnUiArgs();
        tnUiArgs.put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_REGION_COMBOBOX_WIDTH);
        tnUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_REGION_COMBOBOX_HEIGHT);
        
        TnUiArgs titlePartArgs = changeRegionComboBox.getTitlePartUiArgs();
        titlePartArgs.put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_REGION_TITLE_WIDTH);
        titlePartArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_REGION_COMBOBOX_HEIGHT);

        TnUiArgs titleIconArgs = changeRegionComboBox.getIconPartUiArgs();
        titleIconArgs.put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).CHANGE_REGION_DOWN_ARROW_WIDTH);
        titleIconArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_REGION_COMBOBOX_HEIGHT);
        
        TnUiArgs SeperatorLineArgs =changeRegionComboBox.getSeperatorLineArgs();
        SeperatorLineArgs.put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_REGION_SEPARATOR_WIDTH);
        SeperatorLineArgs.put(TnUiArgs.KEY_PREFER_HEIGHT,  ((AcUiDecorator) uiDecorator).AC_REGION_COMBOBOX_HEIGHT);
        SeperatorLineArgs.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,  NinePatchImageDecorator.SORT_BY_SEPERATOR_LINE_UNFOCUS);
        SeperatorLineArgs.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,   NinePatchImageDecorator.SORT_BY_SEPERATOR_LINE_UNFOCUS);

        return changeRegionComboBox;
    }
    
    protected CitizenButton createBadgeButton(int id, String label)
    {
        CitizenButtonWithBadge badgeButton = new CitizenButtonWithBadge(id, label,
            ImageDecorator.IMG_AC_ICON_FAVORITES_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_FAVORITES_UNFOCUS.getImage(), null);
        badgeButton.setBadge(DaoManager.getInstance().getAddressDao().getUnreviewedAddressSize());
        int rPadding = ((AcUiDecorator) uiDecorator).BUTTON_RIGHT_PADDING.getInt();
        badgeButton.setPadding(badgeButton.getLeftPadding(), badgeButton.getTopPadding(), rPadding, badgeButton.getBottomPadding());
        badgeButton.setRightIcon(ImageDecorator.IMG_AC_ICON_ARROW_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_ARROW_UNFOCUS.getImage());
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_FAVORITES);
        badgeButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        badgeButton.setCommandEventListener(this);
        badgeButton.setGap(5);
        badgeButton.setBadgePosition(CitizenButtonWithBadge.SAME_Y_AS_ICON);
        badgeButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_BUTTON_WIDTH);
        badgeButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_BUTTON_HEIGHT);
        badgeButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.AC_DRIVE_TO_BUTTON_UNFOCUSED);
        badgeButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.AC_DRIVE_TO_BUTTON_FOCUSED);
        badgeButton.setForegroundColor(
            UiStyleManager.getInstance().getColor(
                UiStyleManager.AC_BUTTON_FOCUSED_FOREGROUND), UiStyleManager
                    .getInstance()
                    .getColor(UiStyleManager.AC_BUTTON_UNFOCUSED_FOREGROUND));
        ((CitizenButtonWithBadge) badgeButton).setBadgeColor(UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        badgeButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_AC_BUTTON));
        return badgeButton;
    }

    private void initAcMainButton(TnLinearContainer contentContainer, Vector buttons)
    {
        TnLinearContainer container = null;
        int count;
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        lastOrientation = orientation;
        if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            count = 2;
        }
        else
        {
            count = 3;
        }
        for (int i = 0; i < buttons.size(); i++)
        {
            if (i % count == 0)
            {
                container = UiFactory.getInstance().createLinearContainer(i, false, AbstractTnGraphics.VCENTER);
                container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
                container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) this.uiDecorator).BUTTON_CONTAINER_HEIGHT);
                contentContainer.add(container);
            }
            CitizenButton button = (CitizenButton) buttons.elementAt(i);
            FrogNullField nullField = UiFactory.getInstance().createNullField(0);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_BUTTON_HORIZONTAL_GAP);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_BUTTON_HEIGHT);
            container.add(nullField);
            container.add(button);
        }
    }

    private CitizenButton createButton(int id, String name, AbstractTnImage focusedImages, AbstractTnImage unfocusImage, int cmd,
            boolean hasRightIcon)
    {
        CitizenButton button = UiFactory.getInstance().createCitizenButton(id, name, focusedImages, unfocusImage);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", cmd);
        button.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        button.setCommandEventListener(this);
        int rPadding = ((AcUiDecorator) uiDecorator).BUTTON_RIGHT_PADDING.getInt();
        if (hasRightIcon)
        {
            button.setPadding(button.getLeftPadding(), button.getTopPadding(), rPadding, button.getBottomPadding());
            button.setRightIcon(ImageDecorator.IMG_AC_ICON_ARROW_FOCUSED.getImage(), ImageDecorator.IMG_AC_ICON_ARROW_UNFOCUS.getImage());
        }
        button.setGap(5);
        button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AcUiDecorator) uiDecorator).AC_BUTTON_WIDTH);
        button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AcUiDecorator) uiDecorator).AC_BUTTON_HEIGHT);
        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.AC_DRIVE_TO_BUTTON_UNFOCUSED);
        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.AC_DRIVE_TO_BUTTON_FOCUSED);
        button.setForegroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.AC_BUTTON_FOCUSED_FOREGROUND),
            UiStyleManager.getInstance().getColor(
                UiStyleManager.AC_BUTTON_UNFOCUSED_FOREGROUND));
        button.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_AC_BUTTON));
        return button;
    }
    
    protected boolean refreshButtonContainer(TnScreen screen)
    {
        TnLinearContainer buttonContainer = (TnLinearContainer) screen.getComponentById(ID_AC_BUTTON_CONTAINER);
        if (buttonContainer != null)
        {
            buttonContainer.removeAll();
            fillACButtonContainer(buttonContainer);
            return true;
        }

        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                boolean isNeedRelayout = false;
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper
                        .getInstance()).getOrientation();
                if (lastOrientation != orientation)
                {
                    isNeedRelayout = true;
                }
                else if (((AcModel)model).isInternationalAddressCapture())
                {
                    CitizenDropDownComboBox dropDown = (CitizenDropDownComboBox)((CitizenScreen)screen).getComponentById(ID_CHANGE_REGION_DROP_DOWN);
                    if(dropDown != null)
                    {
                        if(regionCodes != null)
                        {
                            String currentRegion = getRegion();
                            for(int i = 0; i < regionCodes.length; i++)
                            {
                                if(currentRegion.equals(regionCodes[i]))
                                {
                                    dropDown.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    }
                    
                    AbstractTnComponent currentLocationButton = screen
                            .getComponentById(IAcConstants.ID_CURRENT_LOCATION);
                    if (currentLocationButton == null
                            && model.getBool(KEY_B_IS_CURRENT_LOCATION_NEEDED))
                    {
                        isNeedRelayout = true;
                    }
                    else if (currentLocationButton != null
                            && !model.getBool(KEY_B_IS_CURRENT_LOCATION_NEEDED))
                    {
                        isNeedRelayout = true;
                    }
                }
                if (isNeedRelayout)
                {
                    return refreshButtonContainer(screen);
                }
                
                CitizenButton homeButton = (CitizenButton) ((CitizenScreen) screen).getComponentById(ID_HOME_BUTTON);
                if (homeButton != null)
                {
                    if (DaoManager.getInstance().getAddressDao().getHomeAddress() != null)
                    {
                        homeButton.setText(ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTTN_HOME, IStringAc.FAMILY_AC));
                    }
                    else
                    {
                        homeButton.setText(ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTTN_HOME, IStringAc.FAMILY_AC));
                        homeButton.setMenu(null, AbstractTnComponent.TYPE_CONTEXT);
                    }
                }

                CitizenButton workButton = (CitizenButton) ((CitizenScreen) screen).getComponentById(ID_WORK_BUTTON);
                if (workButton != null)
                {
                    if (DaoManager.getInstance().getAddressDao().getOfficeAddress() != null)
                    {
                        workButton.setText(ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTN_WORK, IStringAc.FAMILY_AC));
                    }
                    else
                    {
                        workButton.setText(ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTN_WORK, IStringAc.FAMILY_AC));
                        workButton.setMenu(null, AbstractTnComponent.TYPE_CONTEXT);
                    }
                }

                CitizenButton favButton = (CitizenButton) ((CitizenScreen) screen).getComponentById(ID_FAVORITE_BUTTON);
                if(favButton != null)
                {
                    ((CitizenButtonWithBadge)favButton).setBadge(DaoManager.getInstance().getAddressDao().getUnreviewedAddressSize());
                }
                
                CitizenQuickSearchBar quickSearchBar = (CitizenQuickSearchBar) screen.getComponentById(ID_QUICK_SEARCH_BAR);
                if (quickSearchBar != null)
                {
                    boolean isOnboard = this.model.getBool(KEY_B_IS_ONBOARD);
                    String hint = isOnboard ? ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON)
                            :ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON);
                    if (isOnboard)
                    {
                        if (quickSearchBar.isLeftButtonEnabled())
                        {
                            quickSearchBar.disableLeftButton(ImageDecorator.SEARCHBOX_MIC_DISABLED.getImage());
                            quickSearchBar.setHintText(hint);
                        }
                    }
                    else
                    {
                        if (!quickSearchBar.isLeftButtonEnabled())
                        {
                            quickSearchBar.enableLeftButton(ImageDecorator.SEARCHBOX_MIC_UNFOCUSED.getImage());
                            quickSearchBar.setHintText(hint);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void prepareSupportedRegions()
    {
        if (regions == null || regionCodes == null)
        {
            StringMap regionMaps =    DaoManager.getInstance().getBillingAccountDao().getSupportedRegion();
            if (regionMaps != null)
            {
                int size = regionMaps.size();
                regions = new String[size];
                regionCodes = new String[size];
                Enumeration keys = regionMaps.keys();
                int i = 0;
                while (keys.hasMoreElements())
                {
                    String key = (String) keys.nextElement();
                    String value = regionMaps.get(key);
                    regionCodes[i] = key;
                    regions[i] = value;
                    i++;
                }
            }
        }
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw,
            int oldh)
    {
        if (tnComponent != null && tnComponent.getRoot() instanceof TnScreen)
        {
            TnScreen screen = tnComponent.getRoot();
            refreshButtonContainer(screen);
        }
    }
    
    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
//        logKtUiEvent(tnUiEvent);
        return super.handleUiEvent(tnUiEvent);
    }

//    private void logKtUiEvent(TnUiEvent tnUiEvent)
//    {
//        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
//        {
//            int command = tnUiEvent.getCommandEvent().getCommand();
//            switch (command)
//            {
//                case CMD_CURRENT_LOACTION:
//                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_CHOOSE_PLACE,
//                        KontagentLogger.CHOOSEPLACE_CURRENT_LOCATION_CLICKED);
//                    break;
//                case CMD_WORK:
//                case CMD_HOME:
//                case CMD_RECENT:
//                case CMD_FAVORITES:
//                case CMD_ADDRESS:
//                case CMD_PLACES:
//                case CMD_CONTACTS:
//                case CMD_AIRPORTS:
//                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_CHOOSE_PLACE,
//                        KontagentLogger.CHOOSEPLACE_OTHER_PLACE_CLICKED);
//                    break;
//            }
//        }
//        else if(tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
//        {
//            if (tnUiEvent.getKeyEvent()!= null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
//                    && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK && this.model.getState() == STATE_MAIN)
//            {
//                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_CHOOSE_PLACE, KontagentLogger.CHOOSEPLACE_BACK_CLICKED);
//            }
//        } 
//    }
}

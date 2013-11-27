/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteSettingViewTouch.java
 *
 */
package com.telenav.module.preference.routesetting;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.route.Route;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCheckBox;
import com.telenav.ui.citizen.CitizenCheckBoxPopup;
import com.telenav.ui.citizen.CitizenMessageBox;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMultiLineButton;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 *@author yning
 *@date 2011-3-3
 */
public class RouteSettingViewTouch extends AbstractCommonView implements IRouteSettingConstants
{
    protected int lastOrientation = -1;  
    
    public RouteSettingViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_ROUTE_SETTING:
            {
                popup = createRouteSettingPopup(state);
                break;
            }
            case STATE_VOICE_SETTING:
            {
                popup = createVoiceSettingPopup(state);
                break;
            }
        }
        return popup;
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        switch(model.getState())
        {
            case STATE_ROUTE_SETTING:
            {
                int type = tnUiEvent.getType();
                if(type == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    TnMotionEvent event = tnUiEvent.getMotionEvent();
                    if (component != null && event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        int id = component.getId();
                        if (id >= ID_ROUTE_SETTING_BUTTON_BASE  && id < ID_MAX_ROUTE_SETTING_ID)
                        {
                            handleRouteTypeChange(tnUiEvent.getComponent());
                        }
                    }
                }
                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch(state)
        {
            case STATE_VOICE_SETTING:
            {
                if(commandId == CMD_SAVE)
                {
                    TnPopupContainer popup = getCurrentPopup();
                    if(popup != null && popup instanceof CitizenCheckBoxPopup)
                    {
                        int index = ((CitizenCheckBoxPopup) popup).getSelectedIndex();
                        if (index >= 0)
                        {
                            model.put(KEY_I_SELECTED_VOICE_CHOICE, index);
                        }
                    }
                    break;
                }
            }
            case STATE_ROUTE_SETTING:
            {
                if(commandId == CMD_SAVE)
                {
                    TnPopupContainer popup = getCurrentPopup();
                    if(popup != null && popup instanceof CitizenCheckBoxPopup)
                    {
                        boolean[] selectedIndex = ((CitizenCheckBoxPopup)popup).getCheckBox().getItemStatus();
                        model.put(KEY_A_SELECTED_AVOID, selectedIndex);
                    }
                }
                break;
            }
        }
        return true;
    }
    
    protected TnPopupContainer createRouteSettingPopup(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_SAVE, IStringCommon.FAMILY_COMMON),
            CMD_SAVE);
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON),
            CMD_COMMON_BACK);
        
        Preference avoidPref = getTripPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
        String[] avoidOptions = avoidPref.getOptionNames();
        boolean[] selected = getMultiChoices(avoidPref);
        
        CitizenCheckBoxPopup popup = UiFactory.getInstance().createCitizenCheckBoxPopup(state, "", menu);
        popup.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_TOP_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_AREA_HEIGHT);
        popup.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_POPUP_WIDTH);
        popup.getTnUiArgs().put(CitizenCheckBoxPopup.KEY_CHECK_ITEM_WIDTH, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_CHECKBOX_ITEM_WIDTH);
        popup.getTnUiArgs().put(CitizenCheckBoxPopup.KEY_CHECK_BOX_CONTAINER_WIDTH, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_SELECTION_CONTAINER_WIDTH);
        popup.addCheckBox(avoidOptions, true, 0);
        popup.setCommandEventListener(this);
        popup.setSizeChangeListener(this);
        
        CitizenCheckBox checkbox = popup.getCheckBox();
        for(int i = 0; i < selected.length; i++)
        {
            if(selected[i])
            {
                checkbox.setSelectedIndex(i);
            }
        }
        
        updateCheckItem(popup, getSelectedRouteTypeIndex());
        
        TnLinearContainer titleContainer = createRouteSettingPopupTitle();
        popup.setTitle(titleContainer);
        TnLinearContainer topContainer = popup.getTopContainer();
        if(topContainer != null)
        {
            topContainer.setPadding(topContainer.getLeftPadding(), ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_AREA_TOP_PADDING.getInt(), 
                topContainer.getRightPadding(),  ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING.getInt());
        }
        return popup;
    }
    
    protected TnLinearContainer createRouteSettingPopupTitle()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();

        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TITLE_CONTAINER_HEIGHT);
        // title label
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, bundle.getString(IStringNav.RES_ROUTE_SETTINGS, IStringNav.FAMILY_NAV));
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);
        
        final TnLinearContainer contentContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,  ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_COMPONENT_HEIGHT);
//        final int shadowHeight = 12;
//        contentContainer.setPadding(0, shadowHeight, 0, 0);
        contentContainer.add(titleContainer);
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).TITLE_NULL_FIELD_GAP);
        contentContainer.add(nullField);
        contentContainer.add(createSelectionContainer());

        return contentContainer;
    }
    

    protected AbstractTnContainer createSelectionContainer()
    {
        TnLinearContainer selectionContainer = UiFactory.getInstance().createLinearContainer(ID_ROUTE_TYPE_CONTAINER, true , AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        selectionContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_SELECTION_CONTAINER_WIDTH);
        
        Preference routeTypePref = getTripPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        if(routeTypePref != null)
        {
            int selectedIndex = model.getInt(KEY_I_SELECTED_ROUTE_TYPE_CHOICE);
            if(selectedIndex == -1)
            {
                selectedIndex = getSelectedRouteTypeIndex();
            }
            
            model.put(KEY_I_SELECTED_ROUTE_TYPE_CHOICE, selectedIndex);

            String[] routeTypeOptions = routeTypePref.getOptionNames();
            int[] routeTypeValues = routeTypePref.getOptionValues();
            int length = Math.min(routeTypeOptions.length, routeTypeValues.length);
            
            int maxOneRowNumber = ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_SELECTION_CONTAINER_WIDTH.getInt() / ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_SELECTION_BUTTON_WIDTH.getInt();
            if(maxOneRowNumber < 1)
            {
                maxOneRowNumber = 1;
            }
            model.put(KEY_I_TOTAL_SELECTION_NUMBER, length);
            model.put(KEY_I_MAX_BUTTON_COUNT_IN_ONE_ROW, maxOneRowNumber);
            TnLinearContainer oneRowContainer = null;
            for(int i = 0; i < length; i++)
            {
                if(i % maxOneRowNumber == 0)
                {
                    if(oneRowContainer != null)
                    {
                        selectionContainer.add(oneRowContainer);
                    }
                    oneRowContainer = UiFactory.getInstance().createLinearContainer(0, false , AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
                    oneRowContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_SELECTION_CONTAINER_WIDTH);
                }
                
                AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_FONT_BOLD);
                FrogMultiLineButton button = UiFactory.getInstance().createMultiLineButton(ID_ROUTE_SETTING_BUTTON_BASE + i, routeTypeOptions[i]);
                button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_SELECTION_BUTTON_WIDTH);
                button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_SELECTION_BUTTON_HEIGHT);
                button.setFocusable(true);
                button.setFont(font);
                routeStyleButtonStatusChange(button, selectedIndex == i);
                oneRowContainer.add(button);
                button.setTouchEventListener(this);
            }
            
            if(oneRowContainer != null && oneRowContainer.getChildrenSize() > 0)
            {
                selectionContainer.add(oneRowContainer);
            }
        }
        
        return selectionContainer;
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
            Preference routeSettingPref = getTripPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
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
                if(index != -1)
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
    
    protected int getSelectedRouteTypeIndex()
    {
        Preference routeTypePref = getTripPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        int value = routeTypePref.getIntValue();
        int[] values = routeTypePref.getOptionValues();
        for(int i = 0; i < values.length; i++)
        {
            if(values[i] == value)
            {
                return i;
            }
        }
        return -1;
    }
    
    protected void routeStyleButtonStatusChange(FrogMultiLineButton button, boolean isFocus)
    {
        int index = button.getId() - ID_ROUTE_SETTING_BUTTON_BASE;
        int maxOneRowNumber = model.getInt(KEY_I_MAX_BUTTON_COUNT_IN_ONE_ROW);
        int totalNumber = model.getInt(KEY_I_TOTAL_SELECTION_NUMBER); 
        int rowCount = (totalNumber +  maxOneRowNumber  - 1) / maxOneRowNumber;
        int xIndex = index % maxOneRowNumber;
        int yIndex = index / maxOneRowNumber;
        int greyColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
        button.setForegroundColor(greyColor, greyColor);
        
        if((yIndex == 0 || yIndex == rowCount - 1) && (xIndex == 0 || xIndex == maxOneRowNumber - 1))
        {
            if(xIndex == 0)
            {
                if(rowCount == 1)
                {
                    if(isFocus)
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BUTTON_FOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BUTTON_FOCUSED);
                    }
                    else
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BUTTON_UNFOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BUTTON_UNFOCUSED);
                    }
                }
                else if(yIndex == 0)
                {
                    if(isFocus)
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_TOP_BUTTON_FOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_TOP_BUTTON_FOCUSED);
                    }
                    else
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_TOP_BUTTON_UNFOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_TOP_BUTTON_UNFOCUSED);
                    }
                    
                }
                else if(yIndex == rowCount - 1)
                {
                    if(isFocus)
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BOTTOM_BUTTON_FOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BOTTOM_BUTTON_FOCUSED);
                    }
                    else
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BOTTOM_BUTTON_UNFOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_LEFT_BOTTOM_BUTTON_UNFOCUSED);
                    }
                    
                }
            }
            else if(xIndex == maxOneRowNumber - 1)
            {
                if(rowCount == 1)
                {
                    if(isFocus)
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BUTTON_FOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BUTTON_FOCUSED);
                    }
                    else
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BUTTON_UNFOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BUTTON_UNFOCUSED);
                    }
                }
                else if(yIndex == 0)
                {
                    if(isFocus)
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_TOP_BUTTON_FOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_TOP_BUTTON_FOCUSED);
                    }
                    else
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_TOP_BUTTON_UNFOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_TOP_BUTTON_UNFOCUSED);
                    }
                    
                }
                else if(yIndex == rowCount - 1)
                {
                    if(isFocus)
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BOTTOM_BUTTON_FOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BOTTOM_BUTTON_FOCUSED);
                    }
                    else
                    {
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BOTTOM_BUTTON_UNFOCUSED);
                        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_RIGHT_BOTTOM_BUTTON_UNFOCUSED);
                    }
                }
            }
        }
        else
        {
            if(isFocus)
            {
                button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_MIDDLE_BUTTON_FOCUSED);
                button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_MIDDLE_BUTTON_FOCUSED);
            }
            else
            {
                button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ROUTE_STYLE_MIDDLE_BUTTON_UNFOCUSED);
                button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ROUTE_STYLE_MIDDLE_BUTTON_UNFOCUSED);
            }
        }
        button.requestPaint();
    }
    
    
    private TnPopupContainer createVoiceSettingPopup(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_SAVE, IStringCommon.FAMILY_COMMON),
            CMD_SAVE);
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON),
            CMD_COMMON_BACK);
        
        int selectedIndex = 0;
        final Preference audioPref = getTripPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        
        //set disable values when onboard
        boolean isOnboard = model.getBool(KEY_B_IS_ONBOARD);
        
        int value = audioPref.getIntValue();
        if(value == Preference.AUDIO_DIRECTIONS_TRAFFIC || value == Preference.AUDIO_TRAFFIC_ONLY)
        {
            if(isOnboard)
            {
                value = Preference.AUDIO_DIRECTIONS_ONLY;
            }
        }
        int[] values = audioPref.getOptionValues();
        for(int i = 0; i < values.length; i++)
        {
            if(values[i] == value)
            {
                selectedIndex = i;
                break;
            }
        }
        
        String[] text = audioPref.getOptionNames();
        boolean[] disables = null;
        if (isOnboard)
        {
            disables = new boolean[text.length];
            for (int i = 0; i < disables.length; i++)
            {
                if(values[i] == Preference.AUDIO_DIRECTIONS_TRAFFIC || values[i] == Preference.AUDIO_TRAFFIC_ONLY)
                {
                    disables[i] = true;
                }
                else
                {
                    disables[i] = false;
                }
            }
        }
        
        CitizenCheckBoxPopup popup = UiFactory.getInstance().createCitizenCheckBoxPopup(state, "", menu);
        popup.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_TOP_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).PREFERENCE_POP_UP_HEIGHT);
        popup.addCheckBox(text, false, selectedIndex, disables);
        popup.setCommandEventListener(this);
        
        TnLinearContainer titleContainer = createVoicePopupTitle();
        popup.setTitle(titleContainer);
        TnLinearContainer topContainer = popup.getTopContainer();
        if(topContainer != null)
        {
            topContainer.setPadding(topContainer.getLeftPadding(), ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_AREA_TOP_PADDING.getInt(), 
                topContainer.getRightPadding(),  ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING.getInt());
        }
        
        return popup;
    }
    
    
    protected TnLinearContainer createVoicePopupTitle()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();

        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.setPadding(0, 0, 0, 0);
//        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TITLE_CONTAINER_HEIGHT);

        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, bundle.getString(IStringNav.RES_VOICE, IStringNav.FAMILY_NAV));
//        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RouteSettingUiDecorator)this.uiDecorator).ROUTE_SETTING_OPTIONS_CONTAINER_WIDTH);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        titleContainer.add(titleLabel);        

        FrogLabel titleAnnotationLabel = UiFactory.getInstance().createLabel(0, " " + bundle.getString(IStringNav.RES_VOICE_POPUP_TIP, IStringNav.FAMILY_NAV));
        titleAnnotationLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        titleAnnotationLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_POPUP_TITLE_SMALL));
        titleAnnotationLabel.setAnchorFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        titleContainer.add(titleAnnotationLabel);
        
        TnLinearContainer outerTitleContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        outerTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).ROUTE_SETTING_TITLE_CONTAINER_WITH_NULL_FIELD_HEIGHT);
        
        outerTitleContainer.add(titleContainer);
        
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RouteSettingUiDecorator) this.uiDecorator).TITLE_NULL_FIELD_GAP);
        outerTitleContainer.add(nullField);

        return outerTitleContainer;
    }
    
    
    protected boolean handleRouteTypeChange(AbstractTnComponent component)
    {
        int currentSelectedIndex = component.getId() - ID_ROUTE_SETTING_BUTTON_BASE;
        int lastSelectedIndex = model.getInt(KEY_I_SELECTED_ROUTE_TYPE_CHOICE);
        this.model.put(KEY_I_SELECTED_ROUTE_TYPE_CHOICE, currentSelectedIndex);
        if (currentSelectedIndex == lastSelectedIndex)
        {
            return false;
        }

        TnPopupContainer popup = getCurrentPopup();
        if(popup != null && popup instanceof CitizenCheckBoxPopup)
        {
            updateCheckItem((CitizenCheckBoxPopup) popup, currentSelectedIndex);

            FrogMultiLineButton newFocusButton = (FrogMultiLineButton) component;
            routeStyleButtonStatusChange(newFocusButton, true);
            
            FrogMultiLineButton lastFocusedButton = (FrogMultiLineButton) popup.getContent().getComponentById(ID_ROUTE_SETTING_BUTTON_BASE + lastSelectedIndex);
            if (lastFocusedButton != null)
            {
                routeStyleButtonStatusChange(lastFocusedButton, false);
            }
        }
        return false;
    }
    
    protected void updateCheckItem(CitizenCheckBoxPopup popup, int electedRouteTypeIndex)
    {
        CitizenCheckBox checkbox = popup.getCheckBox();

        Preference routeTypePref = getTripPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        int routeTypeValue = -1;
        if (routeTypePref != null)
        {
            int[] routeTypeValues = routeTypePref.getOptionValues();
            if (routeTypeValues != null && electedRouteTypeIndex >= 0 && electedRouteTypeIndex < routeTypeValues.length)
            {
                routeTypeValue = routeTypeValues[electedRouteTypeIndex];
            }
        }
        
        boolean isEnabled = routeTypeValue != Route.ROUTE_PEDESTRIAN;
        if (checkbox!=null)
        {
            for (int i = 0; i < checkbox.getItemsCount(); i++)
            {
                checkbox.setItemEnabled(i, isEnabled);
            }   
            
            boolean isOnboard = model.getBool(KEY_B_IS_ONBOARD);
            int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
            boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED || trafficEnableValue == FeaturesManager.FE_PURCHASED;
            if (!isTrafficEnabled || isOnboard)
            {
                Preference avoidPref = getTripPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
                int[] avoidOptions = avoidPref.getOptionValues();
                for (int i = 0; i < avoidOptions.length; i++)
                {
                    if(avoidOptions[i] == Preference.AVOID_TRAFFIC_DELAYS)
                    {
                        checkbox.setItemEnabled(i, false);
                        break;
                    }
                }
            }
        }
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h,  int oldw, int oldh)
    {
        switch(model.getState())
        {
            case STATE_ROUTE_SETTING:
            {
                int orintation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                if (orintation != lastOrientation)
                {
                    lastOrientation = orintation;
                    TnPopupContainer currentPopup = getCurrentPopup();
                    if (currentPopup != null && currentPopup instanceof CitizenCheckBoxPopup)
                    {
                        AbstractTnContainer contentContainer = currentPopup.getContent();
                        if (contentContainer != null)
                        {
                            AbstractTnContainer routeStyleContainer = (AbstractTnContainer) contentContainer.getComponentById(ID_ROUTE_TYPE_CONTAINER);
                            if(routeStyleContainer != null)
                            {
                                AbstractTnContainer parent = (AbstractTnContainer)routeStyleContainer.getParent();
                                if(parent != null)
                                {
                                    int index = parent.indexOf(routeStyleContainer);
                                    parent.remove(routeStyleContainer);
                                    parent.add(createSelectionContainer(), index);
                                }
                            }
                            ((CitizenCheckBoxPopup)currentPopup).getCheckBox().layoutItems(true);
                            ((CitizenCheckBoxPopup)currentPopup).getCheckBox().requestLayout();
                            ((CitizenCheckBoxPopup)currentPopup).getCheckBox().requestPaint();
                        }
                    }
                }
                break;
            }
        }
    }
    
    
    protected TnScreen createScreen(int state)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
    }
    
    protected Preference getTripPreference(int id)
    {
        boolean isRouteSession = model.getBool(KEY_B_IS_NAV_SESSION);
        Preference tripPref;
        
        if(isRouteSession)
        {
            tripPref = DaoManager.getInstance().getTripsDao().getPreference(id);
        }
        else
        {
            tripPref = DaoManager.getInstance().getPreferenceDao().getPreference(id);
        }
        
        return tripPref;
    }
}

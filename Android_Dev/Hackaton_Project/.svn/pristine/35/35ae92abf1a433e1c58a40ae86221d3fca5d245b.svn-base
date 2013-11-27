/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PreferenceComboBox.java
 *
 */
package com.telenav.module.preference;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCheckBoxPopup;
import com.telenav.ui.citizen.CitizenMessageBox;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogComboBox;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 *@author qli
 *@date 2011-2-17
 */
public class PreferenceComboBox extends FrogComboBox implements IPreferenceConstants, ITnUiEventListener
{
    protected AbstractCommonUiDecorator uiDecorator;
    protected String[] options;

    public PreferenceComboBox(int id, String text)
    {
        super(id, text);
    }
    
    protected void setUiDecorator(AbstractCommonUiDecorator uiDecorator)
    {
        this.uiDecorator = uiDecorator;
    }
    
    protected void showDialog()
    {
        if (popup == null)
        {
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON),
                CMD_SAVE_AND_BACK);
            menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON),
                CMD_COMMON_BACK);
            
            popup = UiFactory.getInstance().createCitizenCheckBoxPopup(0, "", menu);
            popup.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_TOP_HEIGHT, ((PreferenceUiDecorator) this.uiDecorator).PREFERENCE_POP_UP_HEIGHT);
                        
            TnLinearContainer titleContainer = createTitle();
            ((CitizenCheckBoxPopup)popup).setTitle(titleContainer);
            TnLinearContainer topContainer = ((CitizenCheckBoxPopup)popup).getTopContainer();
            if(topContainer != null)
            {
                topContainer.setPadding(topContainer.getLeftPadding(), ((PreferenceUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_AREA_TOP_PADDING.getInt(), 
                    topContainer.getRightPadding(),  ((PreferenceUiDecorator) this.uiDecorator).ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING.getInt());
            }
            
            ((CitizenCheckBoxPopup)popup).addCheckBox(options, false, selectedIndex);
            popup.setCommandEventListener(this);
            popup.setKeyEventListener(new ITnUiEventListener(){
                public boolean handleUiEvent(TnUiEvent tnUiEvent)
                {
                    if( tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK )
                    {
                        if( popup != null )
                        {
                            popup.hide();
                            ((CitizenCheckBoxPopup)popup).setSelectedIndex(selectedIndex);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        popup.show();
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                && tnUiEvent.getCommandEvent() != null
                && tnUiEvent.getComponent() instanceof FrogButton)
        {
            switch (tnUiEvent.getCommandEvent().getCommand() )
            {
                case CMD_SAVE_AND_BACK:
                {
                    if (popup != null)
                    {
                        popup.hide();
                        int index = (((CitizenCheckBoxPopup) popup)).getSelectedIndex();
                        if (index == selectedIndex || index == -1)
                        {
                            return true;
                        }
                        selectedIndex = index;
                    }
                    PreferenceComboBox.this.setSelectedIndex(selectedIndex);
                    tnUiEvent.setComponent(PreferenceComboBox.this);
                    if (PreferenceComboBox.this.commandListener != null)
                    {
                        PreferenceComboBox.this.commandListener.handleUiEvent(tnUiEvent);
                    }
                    return false;
                }
                case CMD_COMMON_BACK:
                {
                    if (popup != null)
                    {
                        popup.hide();
                        (((CitizenCheckBoxPopup) popup)).setSelectedIndex(selectedIndex);
                    }
                    return false;
                }
            }

        }
        return super.handleUiEvent(tnUiEvent);
    }
    
    
    protected void setOptions(String[] options)
    {
        this.options = options;
    }
    
    public void setSelectedIndex(int index)
    {
        this.selectedIndex = index;
        if( options != null && options.length > 0 )
        {
            this.requestPaint();
        }
    }

    protected TnLinearContainer createTitle()
    {
        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).ROUTE_SETTING_TITLE_CONTAINER_WITH_NULL_FIELD_HEIGHT);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, getText());
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).TITLE_NULL_FIELD_GAP);
        titleContainer.add(nullField);
        return titleContainer;
    }
    
    //Override, it's used by paint method in FrogComboBox.
    protected FrogListItem getListItem(int position)
    {
        FrogListItem label = UiFactory.getInstance().createListItem(position);
        label.setText(options[position]);
        return label;
    }

}

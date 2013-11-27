/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PluginViewTouch.java
 *
 */
package com.telenav.sdk.plugin.module;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.plugin.IStringPlugin;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenCheckBoxPopup;
import com.telenav.ui.citizen.CitizenMessageBox;

/**
 *@author qli
 *@date 2011-2-24
 */
class PluginViewTouch extends AbstractCommonView implements IPluginConstants
{
    
    protected String[] actions;

    public PluginViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_PLUGIN_CHOICES:
            {
                return createPluginChoicesPopup(state);
            }
            case STATE_START_PLUGIN:
            case STATE_DO_ONEBOX_RGC:
            {
                return createProgressPopup(state);
            }
            
        }
        return null;
    }
    
    protected TnPopupContainer createPluginChoicesPopup(int state)
    {
        int count = IStringPlugin.RES_SHARE - IStringPlugin.PLUGIN_STR_BASE;
        String[] options = new String[count];
        actions = new String[count];
        for( int i = count ; i > 0 ; i -- )
        {
            options[i-1] = ResourceManager.getInstance().getCurrentBundle().getString(IStringPlugin.PLUGIN_STR_BASE + i, IStringPlugin.FAMILY_PLUGIN);
            actions[i-1] = getAction(IStringPlugin.PLUGIN_STR_BASE + i);
        }
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_DONE, IStringCommon.FAMILY_COMMON),
            CMD_START_PLUGIN);
        
        CitizenCheckBoxPopup popup = UiFactory.getInstance().createCitizenCheckBoxPopup(state, "", menu);
        popup.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_TOP_HEIGHT, ((PluginUiDecorator)uiDecorator).POPUP_TOP_HEIGHT);
        TnLinearContainer topContainer = ((CitizenCheckBoxPopup)popup).getTopContainer();
        if(topContainer != null)
        {
            topContainer.setPadding(topContainer.getLeftPadding(), topContainer.getTopPadding() + 12,//transparent edge.
                topContainer.getRightPadding(), topContainer.getBottomPadding());
        }
        popup.setCommandEventListener(this);
        popup.addCheckBox(options, false, 0);
        
        return popup;
    }
    
    protected String getAction(int index)
    {
        if(index == IStringPlugin.RES_DRIVETO)
        {
            return IMaiTai.MENU_ITEM_DRIVE_TO;
        }
        else if(index == IStringPlugin.RES_VIEWMAP)
        {
            return IMaiTai.MENU_ITEM_MAP_VALUE;
        }
        else if(index == IStringPlugin.RES_SEARCH)
        {
            return IMaiTai.MENU_ITEM_BIZ_VALUE;
        }
        else if(index == IStringPlugin.RES_SHARE)
        {
            return IMaiTai.MENU_ITEM_SHARE_ADDRESS;
        }
        return null;
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_PLUGIN_CHOICES:
            {
                switch(commandId)
                {
                    case CMD_START_PLUGIN:
                    {
                        TnPopupContainer popup = getCurrentPopup();
                        if(popup != null && popup instanceof CitizenCheckBoxPopup)
                        {
                            int index = ((CitizenCheckBoxPopup) popup).getSelectedIndex();
                            if (actions != null && index >= 0 && index < actions.length)
                            {
                                model.put(KEY_S_PLUGIN_ACTION, actions[index]);
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
    
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }
    
    protected TnPopupContainer createProgressPopup(int state)
    {
        return UiFactory.getInstance().createProgressBox(state, "");
    }

    protected TnScreen createScreen(int state)
    {
        return null;
    }
    
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }


}

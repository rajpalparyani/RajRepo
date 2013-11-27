/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SwitchLangViewTouch.java
 *
 */
package com.telenav.module.preference.language;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringPreference;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.frogui.widget.FrogProgressBox;

/**
 *@author qli
 *@modifier wzhu
 *@date 2011-3-9
 */
class SwitchLanguageViewTouch extends AbstractCommonView implements ISwitchLanguageConstants
{

    public SwitchLanguageViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        return cmd;
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch(state)
        {
            case STATE_SWITCHLANG_SYNC:
            {
                return createHandlePopup(state);
            }
        }
        return null;
    }
    
    protected TnPopupContainer createHandlePopup(int state)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(state, 
            ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_STR_HANDLING, IStringPreference.FAMILY_PREFERENCE));
        return progressBox;
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

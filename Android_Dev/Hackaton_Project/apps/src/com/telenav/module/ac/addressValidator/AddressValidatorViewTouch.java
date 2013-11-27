/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentViewTouch.java
 *
 */
package com.telenav.module.ac.addressValidator;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringSetHome;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.frogui.widget.FrogProgressBox;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-26
 */
class AddressValidatorViewTouch extends AbstractCommonView implements IAddressValidatorConstants
{

    public AddressValidatorViewTouch(AbstractCommonUiDecorator uiDecorator)
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
        switch (state)
        {
            case STATE_MAIN:
            {
                return createValidateAddressPopup(state);
            }
        }
        return null;
    }

    protected boolean isShownTransientView(int state)
    {
        switch (state)
        {
            case STATE_CHOOSE_ADDRESS:
            {
                return false;
            }
        }
        return super.isShownTransientView(state);
    }

    private TnPopupContainer createValidateAddressPopup(int state)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(
            state,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringSetHome.RES_LABEL_VALIDATING_HOME, IStringSetHome.FAMILY_SET_HOME));
        return progressBox;
    }


    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        return null;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

}

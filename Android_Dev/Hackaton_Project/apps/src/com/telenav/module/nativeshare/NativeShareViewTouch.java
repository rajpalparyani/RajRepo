/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NativeShareViewTouch.java
 *
 */
package com.telenav.module.nativeshare;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringShare;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.frogui.widget.FrogProgressBox;

/**
 * @author wchshao
 * @date Mar 18, 2013
 */
class NativeShareViewTouch extends AbstractCommonView implements INativeShareConstants
{

    public NativeShareViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    @Override
    protected boolean updateScreen(int state, TnScreen screen)
    {
        // no screen
        return false;
    }

    @Override
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return ICommonConstants.CMD_NONE;
    }

    @Override
    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_NATIVE_SHARE_REQUEST_TINY_URL:
            {
                return createRequestingTinyUrlPopup(state);
            }
        }
        return null;
    }

    @Override
    protected TnScreen createScreen(int state)
    {
        // no screen
        return null;
    }

    @Override
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        // no need to update
        return false;
    }

    protected TnPopupContainer createRequestingTinyUrlPopup(int state)
    {
        String hint = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringShare.RES_REQUESTING_TINY_URL, IStringShare.FAMILY_SHARE);
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, hint);
        return progressBox;
    }

    @Override
    protected void afterShowTransientView(int state)
    {
        super.afterShowTransientView(state);
        if (state == STATE_NATIVE_SHARE)
        {
            // hide the scouting popup to avoid the bug popup flashes after back from share popup
            // FIXME: use popAllViews since hidePopup() is private and there are no screens in this module.
            popAllViews();
        }
    }

}

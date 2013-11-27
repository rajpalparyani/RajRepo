/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PluginUiDecorator.java
 *
 */
package com.telenav.sdk.plugin.module;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2011-2-24
 */
public class PluginUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_POPUP_TOP_HEIGHT = 1;
    private final static int ID_POPUP_RADIO_BOX_MAX_HEIGHT = 2;
    
    TnUiArgAdapter POPUP_TOP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_TOP_HEIGHT), this);
    TnUiArgAdapter POPUP_RADIO_BOX_MAX_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_RADIO_BOX_MAX_HEIGHT), this);

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        int keyValue = ((Integer)args.getKey()).intValue();
        switch(keyValue)
        {
            case ID_POPUP_TOP_HEIGHT:
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                int top = 20 * max / 480 + 12;//transparent edge.
                int bottom = 10 * max / 480;
                return PrimitiveTypeCache.valueOf(top + bottom);
            }
            case ID_POPUP_RADIO_BOX_MAX_HEIGHT:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 40 / 100);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 53 / 100);
                }
            }
        }
        return null;
    }

}

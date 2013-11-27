/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SecretKeyUiDecorator.java
 *
 */
package com.telenav.module.entry.secretkey;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author jshchen
 *@date 2010-8-19
 */
class SecretKeyUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_OPTIONS_ITEM_WIDTH = 17;
    private final static int ID_OPTIONS_ITEM_HEIGHT = 18;
    private final static int ID_OPTIONS_HALF_SCREEN_WIDTH = 19;

    public TnUiArgAdapter SETTING_POPUP_COMBO_ITEM_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_OPTIONS_ITEM_WIDTH), this);
    public TnUiArgAdapter SETTING_POPUP_COMBO_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_OPTIONS_ITEM_HEIGHT), this);
    public TnUiArgAdapter HALF_SCREEN_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_OPTIONS_HALF_SCREEN_WIDTH), this);

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_OPTIONS_ITEM_WIDTH:
            {
                return AppConfigHelper.getDisplayWidth();
            }
            case ID_OPTIONS_ITEM_HEIGHT:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(maxHeight * 83 / 1000);
            }
            case ID_OPTIONS_HALF_SCREEN_WIDTH:
            {
                return AppConfigHelper.getDisplayWidth() / 2;
            }
        }

        return null;
    }

}

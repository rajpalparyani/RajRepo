/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentUiDecorator.java
 *
 */
package com.telenav.module.ac.stopsselection;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-26
 */
class StopsSelectionUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_CHOOSE_ADDRESS_TITLE_HEIGHT = 1;
    
    public static final int SCALE = 10000;

    public TnUiArgAdapter CHOOSE_ADDRESS_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_ADDRESS_TITLE_HEIGHT), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_CHOOSE_ADDRESS_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 850 / SCALE);
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / SCALE);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1270 / SCALE);
                    }
                }
            }
        }
        return null;
    }

}

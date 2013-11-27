/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StartUpDecorator.java
 *
 */
package com.telenav.module.sync;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-12-2
 */
class SyncResUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_PROGRESS_BAR_HEIGHT = 1;
    private final static int ID_PROGRESS_BAR_WIDTH = 2;
    private final static int ID_TITLE_LABEL_HEIGHT = 3;
    private final static int ID_BOTTOM_PROGRESS_CONTAINER_HEIGHT = 4;
    private final static int ID_BOTTOM_PROGRESS_CONTAINER_Y = 5;
    private final static int ID_MAP_LOGO_WITH_BOTTOM_CONTAINER_Y = 6;
    
    
    public TnUiArgAdapter PROGRESS_BAR_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROGRESS_BAR_HEIGHT), this);
    public TnUiArgAdapter PROGRESS_BAR_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROGRESS_BAR_WIDTH), this);
    
    public TnUiArgAdapter TITLE_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_LABEL_HEIGHT), this);
    public TnUiArgAdapter BOTTOM_PROGRESS_CONTAINER_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_PROGRESS_CONTAINER_Y), this);
    public TnUiArgAdapter BOTTOM_PROGRESS_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_PROGRESS_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter MAP_LOGO_WITH_BOTTOM_CONTAINER_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_LOGO_WITH_BOTTOM_CONTAINER_Y), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_PROGRESS_BAR_HEIGHT:
            {
                int max  = AppConfigHelper.getMaxDisplaySize();
                if (max < 480)
                {
                	max = 420;
                }
                return PrimitiveTypeCache.valueOf(max / 12);
            }
            case ID_PROGRESS_BAR_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_TITLE_LABEL_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 850 / 10000);
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1270 / 10000);
                    }
                }
            }
            case ID_BOTTOM_PROGRESS_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf( 2 * PROGRESS_BAR_HEIGHT.getInt());
            }
            case ID_BOTTOM_PROGRESS_CONTAINER_Y:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - BOTTOM_PROGRESS_CONTAINER_HEIGHT.getInt());
            }
            case ID_MAP_LOGO_WITH_BOTTOM_CONTAINER_Y:
            {
                return PrimitiveTypeCache.valueOf(MAP_LOGO_Y.getInt() - BOTTOM_PROGRESS_CONTAINER_HEIGHT.getInt());
            }
        }
        return null;
    }

}

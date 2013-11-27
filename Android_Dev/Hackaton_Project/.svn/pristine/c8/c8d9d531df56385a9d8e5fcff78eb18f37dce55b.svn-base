/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BrowserSdkUiDecorator.java
 *
 */
package com.telenav.module.browsersdk;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2010-12-29
 */
public class BrowserSdkUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_TITLE_CONTAINER_HEIGHT = 1;

    private final static int ID_LIST_ITEM_HEIGHT = 2;
    
    public TnUiArgAdapter LIST_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LIST_ITEM_HEIGHT), this);
    
    public TnUiArgAdapter TITLE_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_CONTAINER_HEIGHT), this);
    
    public static final int SCALE = 100; 

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_TITLE_CONTAINER_HEIGHT:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 6 / SCALE);
                else 
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 8 / SCALE);
            }
            case ID_LIST_ITEM_HEIGHT:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 9 / SCALE);
                else 
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 12/ SCALE);
            }
        }
        return null;
    }

}

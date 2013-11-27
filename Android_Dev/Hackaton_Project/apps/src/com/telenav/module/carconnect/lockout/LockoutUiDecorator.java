/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LockoutUiDecorator.java
 *
 */
package com.telenav.module.carconnect.lockout;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author chihlh
 *@date Mar 15, 2012
 */
class LockoutUiDecorator extends AbstractCommonUiDecorator
{
    
    
    private static final int ID_TITLE_HEIGHT = 0;
    private static final int ID_TEXT_GAP = 1;
    private static final int ID_LOGO_GAP_X = 2;
    private static final int ID_LOGO_GAP_Y = 3;
    
    final TnUiArgAdapter LOCKOUT_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_HEIGHT), this);
    final TnUiArgAdapter LOCKOUT_TEXT_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXT_GAP), this);
    final TnUiArgAdapter LOCKOUT_LOGO_GAP_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LOGO_GAP_X), this);
    final TnUiArgAdapter LOCKOUT_LOGO_GAP_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LOGO_GAP_Y), this);
    
    
    

    @Override
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer)args.getKey()).intValue();
        boolean isLandscape = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_LANDSCAPE;
        switch (key)
        {
            case ID_TITLE_HEIGHT:
            {
                if (isLandscape)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 175 / 1000);
                else   
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 3385 / 10000);
            }
            case ID_TEXT_GAP:
            {
                if (isLandscape)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 10 / 100);
                else   
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 702 / 10000);
            }
            case ID_LOGO_GAP_X:
            {
                if (isLandscape)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 574 / 10000);
                else   
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 574 / 10000);
            }
            case ID_LOGO_GAP_Y:
            {
                if (isLandscape)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 750 / 10000);
                else   
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 2300 / 10000);
            }
        }
        
        return null;
    }

}

/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AddPlaceUiDecorator.java
 *
 */
package com.telenav.module.ac.place.addplace;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper(pwang@telenav.cn)
 *@date 2013-2-21
 */
public class AddPlaceUiDecorator extends AbstractCommonUiDecorator
{

    private final static int ID_CHOOSE_ADDRESS_TITLE_HEIGHT = 1;
    private final static int ID_TEXTFIELD_WIDTH = 2;
    private final static int ID_TEXTFIELD_HEIGHT = 3;
     public TnUiArgAdapter TEXTFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_WIDTH), this);
     public TnUiArgAdapter TEXTFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_HEIGHT), this);
    
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
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1400 / SCALE);
            }
            case ID_TEXTFIELD_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - AndroidCitizenUiHelper.getPixelsByDip(95));
            }
            case ID_TEXTFIELD_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(35));
            }
        }
        return null;
    }


}

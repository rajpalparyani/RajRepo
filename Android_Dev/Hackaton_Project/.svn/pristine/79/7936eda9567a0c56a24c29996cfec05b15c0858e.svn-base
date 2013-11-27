/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LoginUiDecorator.java
 *
 */
package com.telenav.module.login;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-11-26
 */
class LoginUiDecorator extends AbstractCommonUiDecorator implements ILoginConstants
{
    private final static int ID_BUTTON_WIDTH = 1;
    private final static int ID_GAP_HEIGHT = 2;
    private final static int ID_TITLE_CONTAINER_HEIGHT = 3;
    private final static int ID_BOTTOM_CONTAINER_HEIGHT = 4;
    private final static int ID_TOC_BUTTON_WIDTH = 5;
    private final static int ID_NULL_FIELD_WIDTH = 6;
    private final static int ID_PTN_INPUT_L_PADDING = 7;
    private final static int ID_CONTAINER_LR_PADDING = 8;
    private final static int ID_TEXT_FIELD_WIDTH = 9;
    private final static int ID_TEXT_FIELD_HEIGHT = 10;
    private final static int ID_LABEL_TITLE_HEIGHT = 11;
    private final static int ID_TOC_BUTTON_HEIGHT = 12;
    
    public TnUiArgAdapter BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_WIDTH), this);
    public TnUiArgAdapter GAP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_GAP_HEIGHT), this);
    public TnUiArgAdapter TITLE_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter TOC_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOC_BUTTON_WIDTH), this);
    public TnUiArgAdapter NULL_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULL_FIELD_WIDTH), this);
    public TnUiArgAdapter PTN_INPUT_L_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PTN_INPUT_L_PADDING), this);
    public TnUiArgAdapter CONTAINER_LR_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTAINER_LR_PADDING), this);
    public TnUiArgAdapter TEXT_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXT_FIELD_WIDTH), this);
    public TnUiArgAdapter TEXT_FIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXT_FIELD_HEIGHT), this);
    public TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);
    public TnUiArgAdapter TOC_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOC_BUTTON_HEIGHT), this);
    
    private final static int SCALE = 1000;
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer)args.getKey()).intValue();
        switch(key)
        {
            case ID_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(TEXT_FIELD_WIDTH.getInt());
            }
            case ID_GAP_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 40);
            }
            case ID_TITLE_CONTAINER_HEIGHT:
            {
                if (AppConfigHelper.isTabletSize())
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / 10000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 11 );
                }
            }
            case ID_BOTTOM_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 7 );
            }
            case ID_TOC_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 18 / 40 );
            }
            case ID_TOC_BUTTON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(BOTTOM_CONTAINER_HEIGHT.getInt() * 60 / 100);
            }
            case ID_NULL_FIELD_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() / 40 );
            }  
            case ID_PTN_INPUT_L_PADDING:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() / 40 );
            }
            case ID_CONTAINER_LR_PADDING:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 8 / 100 );
            }
            case ID_TEXT_FIELD_WIDTH:
            {
            	 return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 55 / 100);
            }
            case ID_TEXT_FIELD_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 62 / 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 55 / 1000);
                }
            }
            case ID_LABEL_TITLE_HEIGHT:
            {
                if(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                  //unify the title height with native xml screen title height
                    return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(53)); 
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 127 / SCALE);
                    }
                }
            }
        }
        return null;
    }

}

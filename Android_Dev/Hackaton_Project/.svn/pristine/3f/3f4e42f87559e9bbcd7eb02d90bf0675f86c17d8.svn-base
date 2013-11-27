/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceConstants.java
 *
 */
package com.telenav.module.preference;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;
/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-18
 */
public class PreferenceUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_GAP_HEIGHT_ON_SAVE = 1;
    private final static int ID_BUTTON_HEIGHT = 2;
    private final static int ID_BUTTON_WIDTH = 3;
    private final static int ID_LABEL_TITLE_HEIGHT = 4;
    
    private final static int ID_PROFILE_ITEM_TITLE_HEIGHT = 6;
    private final static int ID_PROFILE_ITEM_CONTENT_HEIGHT =7;
    
    private final static int ID_PROFILE_CONTAINER_PADDING =8;
    private final static int ID_PROFILE_TEXT_PADDING =9;
    
    private final static int ID_PROFILE_TITLE_SEPERATOR =10;
    private final static int ID_PROFILE_ITEM_SEPERATOR =11;
    
    private final static int ID_ITEM_CONTAINER_WIDTH = 12;
    private final static int ID_ITEM_BUTTON_HEIGHT = 13;
    private final static int ID_ITEM_BUTTON_WIDTH = 14;

    TnUiArgAdapter GAP_HEIGHT_ON_SAVE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_GAP_HEIGHT_ON_SAVE), this);
    TnUiArgAdapter BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_HEIGHT), this);
    TnUiArgAdapter BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_WIDTH), this);
    TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);;
    TnUiArgAdapter PROFILE_ITEM_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROFILE_ITEM_TITLE_HEIGHT), this);
    TnUiArgAdapter PROFILE_ITEM_CONTENT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROFILE_ITEM_CONTENT_HEIGHT), this);
    TnUiArgAdapter PROFILE_CONTAINER_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROFILE_CONTAINER_PADDING), this);
    TnUiArgAdapter PROFILE_TEXT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROFILE_TEXT_PADDING), this);
    TnUiArgAdapter PROFILE_TITLE_SEPERATOR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROFILE_TITLE_SEPERATOR), this);
    TnUiArgAdapter PROFILE_ITEM_SEPERATOR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PROFILE_ITEM_SEPERATOR), this);
    TnUiArgAdapter ITEM_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ITEM_CONTAINER_WIDTH), this);
    TnUiArgAdapter ITEM_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ITEM_BUTTON_HEIGHT), this);
    TnUiArgAdapter ITEM_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ITEM_BUTTON_WIDTH), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        int width = AppConfigHelper.getDisplayWidth();
        int height = AppConfigHelper.getDisplayHeight();
        int keyValue = ((Integer)args.getKey()).intValue();
        switch(keyValue)
        {
            case ID_GAP_HEIGHT_ON_SAVE:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(height * 8 / 100);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(height * 12 / 100);
                }
            }
                
            case ID_BUTTON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 65 / 1000);
            }
            case ID_BUTTON_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 6250 / 10000);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 6000 / 10000);
                }
            }
            case ID_LABEL_TITLE_HEIGHT:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
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
                        return PrimitiveTypeCache.valueOf(height * 1270 / 10000);
                    }
                }
            }
            case ID_PROFILE_ITEM_TITLE_HEIGHT:
            {
                int h = AppConfigHelper.getMaxDisplaySize() * 28 / 1280;
                return PrimitiveTypeCache.valueOf(h);
            }
            case ID_PROFILE_ITEM_CONTENT_HEIGHT:
            {
                int h = AppConfigHelper.getMaxDisplaySize() * 132 / 1280;
                return PrimitiveTypeCache.valueOf(h);
            }
            case ID_PROFILE_CONTAINER_PADDING:
            {
                int w = AppConfigHelper.getMinDisplaySize() * 32 / 720;
                return PrimitiveTypeCache.valueOf(w);
            }
            case ID_PROFILE_TEXT_PADDING:
            {
                int w = AppConfigHelper.getMinDisplaySize() * 16 / 720;
                return PrimitiveTypeCache.valueOf(w);
            }
            case ID_PROFILE_TITLE_SEPERATOR:
            {
                if(AppConfigHelper.getMaxDisplaySize() >= 1000)
                {
                    return PrimitiveTypeCache.valueOf(3);//if big screen
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(2);//if small screen
                }
            }
            case ID_PROFILE_ITEM_SEPERATOR:
            {
                return PrimitiveTypeCache.valueOf(1);
            }
            case ID_ITEM_CONTAINER_WIDTH:
            {
                int w = width - 2* PROFILE_CONTAINER_PADDING.getInt();
                return PrimitiveTypeCache.valueOf(w);
            }
            case ID_ITEM_BUTTON_HEIGHT:
            {
                int h = AndroidCitizenUiHelper.getPixelsByDip(50);
                return PrimitiveTypeCache.valueOf(h);
            }
            case ID_ITEM_BUTTON_WIDTH:
            {
                int w = width - 3* PROFILE_CONTAINER_PADDING.getInt();
                return PrimitiveTypeCache.valueOf(w);
            }
        }
        return null;
    }

}

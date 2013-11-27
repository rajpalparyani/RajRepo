/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentUiDecorator.java
 *
 */
package com.telenav.module.ac.home;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-3
 */
public class HomeUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_HOME_POPUP_WIDTH = 1;

    private final static int ID_HOME_POPUP_HEIGHT = 2;
    
    private final static int ID_TEXTFIELD_HOME_HEIGHT = 3;
    
    private final static int ID_NULLFIELD_HOME_HEIGHT = 4;
    
    private final static int ID_POPUP_BOTTOM_HEIGHT = 5;
    
    private final static int ID_POPUP_TOP_HEIGHT = 6;
    
    private final static int ID_CHOOSE_HOME_TITLE_HEIGHT = 7;
    
    private final static int ID_HOME_POPUP_TITLE_HEIGHT = 8;
    
    private final static int ID_HOME_POPUP_HINT_LABEL_HEIGHT = 9;
    
    private static final int ID_LABEL_TITLE_HEIGHT = 10;
    
    private static final int ID_ADDRESS_ITEM_HEIGHT = 11;
    
    private static final int ID_ADDRESS_FIELD_WIDTH = 12;
    
    private static final int ID_SUBMIT_BUTTON_WIDTH = 13;
    
    private final static int ID_DROPDOWN_Y_POS = 14;
    
    private final static int ID_CHOOSE_AIRPORT_LIST_HEIGHT = 15;
    
    private final static int ID_DROPDOWN_LIST_WIDTH = 16;
    
    private final static int ID_DROPDOWN_LIST_HEIGHT = 17;
    
    public static final int SCALE = 10000;

    public TnUiArgAdapter HOME_POPUP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_HOME_POPUP_WIDTH), this);

    public TnUiArgAdapter HOME_POPUP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_HOME_POPUP_HEIGHT), this);
    
    public TnUiArgAdapter TEXTFIELD_HOME_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_HOME_HEIGHT), this);
    
    public TnUiArgAdapter NULLFIELD_HOME_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_HOME_HEIGHT), this);
    
    public TnUiArgAdapter POPUP_BOTTOM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BOTTOM_HEIGHT), this);
    
    public TnUiArgAdapter POPUP_TOP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_TOP_HEIGHT), this);
    
    public TnUiArgAdapter CHOOSE_HOME_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_HOME_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter HOME_POPUP_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_HOME_POPUP_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter HOME_POPUP_HINT_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_HOME_POPUP_HINT_LABEL_HEIGHT), this);
    
    public TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter ADDRESS_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ADDRESS_ITEM_HEIGHT), this);
    
    public TnUiArgAdapter ADDRESS_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ADDRESS_FIELD_WIDTH), this);
    
    public TnUiArgAdapter SUBMIT_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUBMIT_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter DROPDOWN_Y_POS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWN_Y_POS), this);
    
    public TnUiArgAdapter CHOOSE_AIRPORT_LIST_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_AIRPORT_LIST_HEIGHT), this);
    
    public TnUiArgAdapter DROPDOWN_LIST_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWN_LIST_WIDTH), this);
    
    public TnUiArgAdapter DROPDOWN_LIST_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWN_LIST_HEIGHT), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_HOME_POPUP_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9600 / SCALE);
            }
            case ID_HOME_POPUP_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 8000 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 8000 / SCALE);
            }
            case ID_TEXTFIELD_HOME_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 480 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 720 / SCALE);
                }

            }
            case ID_NULLFIELD_HOME_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 500 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 800 / SCALE);
            }
            case ID_POPUP_BOTTOM_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1300 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1800 / SCALE);
            }
            case ID_POPUP_TOP_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 3300 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 4800 / SCALE);
            }
            case ID_CHOOSE_HOME_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1400 / SCALE);
            }
            case ID_HOME_POPUP_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 800 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1100 / SCALE);
            }
            case ID_HOME_POPUP_HINT_LABEL_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 500 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 700 / SCALE);
            }
            case ID_LABEL_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    //unify the title height with native xml screen title height
                    return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(53));
                }
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
            case ID_ADDRESS_ITEM_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1050 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1500 / SCALE);
                }
            }
            case ID_ADDRESS_FIELD_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9400 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9400 / SCALE);
            }
            case ID_SUBMIT_BUTTON_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 6250 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 6000 / SCALE);
                }
            }
            case ID_DROPDOWN_Y_POS:
            {
                return PrimitiveTypeCache.valueOf(4);
            }
            case ID_CHOOSE_AIRPORT_LIST_HEIGHT:
            {
                int listTopBottomMargin = 15;
                int itemCount = 5;
                return PrimitiveTypeCache.valueOf(DROPDOWN_LIST_HEIGHT.getInt() * itemCount + listTopBottomMargin); 
            } 
            case ID_DROPDOWN_LIST_WIDTH:
            {
                
                int maxDisplaySize = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
                if(maxDisplaySize <= 480)
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9210 / SCALE);
                    else
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9280 / SCALE);
                }
                else
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9280 / SCALE);
                    else
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9320 / SCALE);
                }
            }
            case ID_DROPDOWN_LIST_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 800 / SCALE);
            }
        }
        return null;
    }

}

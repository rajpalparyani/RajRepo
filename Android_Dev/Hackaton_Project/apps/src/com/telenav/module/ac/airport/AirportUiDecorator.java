/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteUiDecorator.java
 *
 */
package com.telenav.module.ac.airport;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
class AirportUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_DROPDOWNFIELD_WIDTH = 1;

    private final static int ID_DROPDOWNFIELD_HEIGHT = 2;

    private final static int ID_CONTAINER_DROPDOWN_HEIGHT = 3;

    private final static int ID_BUTTON_SUBMIT_WIDTH = 4;

    private final static int ID_NULLFIELD_AIRPORT_INTEVAL_HEIGHT = 5;

    private final static int ID_NULLFIELD_AIRPORT_HEIGHT = 6;

    private final static int ID_CONTAINER_DROPDOWN_WIDTH = 7;
    
    private final static int ID_CHOOSE_AIRPORT_TITLE_HEIGHT = 8;
    
    private final static int ID_CHOOSE_AIRPORT_LIST_CONTAINER_HEIGHT = 9;
    
    private final static int ID_CHOOSE_AIRPORT_LIST_ITEM_HEIGHT = 10;
    
    private final static int ID_CHOOSE_AIRPORT_LIST_HEIGHT = 11;
    
    private final static int ID_DROPDOWN_LIST_WIDTH = 12;
    
    private final static int ID_DROPDOWN_Y_POS = 13;
    
    private final static int ID_SEARCH_BUTTON_WIDTH = 14;
    
    private final static int ID_SEARCH_FIELD_HEIGHT = 15;
    
    private final static int ID_SEARCH_FIELD_NULLFIELD_WIDTH = 16;
    
    private final static int SCALE = 10000;

    public TnUiArgAdapter DROPDOWNFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWNFIELD_WIDTH), this);

    public TnUiArgAdapter DROPDOWNFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWNFIELD_HEIGHT), this);

    public TnUiArgAdapter CONTAINER_DROPDOWN_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTAINER_DROPDOWN_HEIGHT), this);

    public TnUiArgAdapter BUTTON_SUBMIT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_SUBMIT_WIDTH), this);

    public TnUiArgAdapter NULLFIELD_AIRPORT_INTEVAL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_AIRPORT_INTEVAL_HEIGHT), this);

    public TnUiArgAdapter NULLFIELD_AIRPORT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_AIRPORT_HEIGHT), this);

    public TnUiArgAdapter CONTAINER_DROPDOWN_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTAINER_DROPDOWN_WIDTH), this);

    public TnUiArgAdapter CHOOSE_AIRPORT_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_AIRPORT_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter CHOOSE_AIRPORT_LIST_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_AIRPORT_LIST_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter CHOOSE_AIRPORT_LIST_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_AIRPORT_LIST_ITEM_HEIGHT), this);
    
    public TnUiArgAdapter CHOOSE_AIRPORT_LIST_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_AIRPORT_LIST_HEIGHT), this);
    
    public TnUiArgAdapter DROPDOWN_LIST_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWN_LIST_WIDTH), this);
    
    public TnUiArgAdapter DROPDOWN_Y_POS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWN_Y_POS), this);
    
    public TnUiArgAdapter SEARCH_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter SEARCH_FIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_FIELD_HEIGHT), this);
    
    public TnUiArgAdapter SEARCH_FIELD_NULLFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_FIELD_NULLFIELD_WIDTH), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_DROPDOWNFIELD_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 8390 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 8880 / SCALE);
            }
            case ID_DROPDOWNFIELD_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 480 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 720 / SCALE);
            }
            case ID_CONTAINER_DROPDOWN_HEIGHT:
            {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 10);
            }
            case ID_BUTTON_SUBMIT_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 6250 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 6000 / SCALE);
            }
            case ID_NULLFIELD_AIRPORT_INTEVAL_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 300 / SCALE);
            }
            case ID_NULLFIELD_AIRPORT_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 7500 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 6000 / SCALE);
            }
            case ID_CONTAINER_DROPDOWN_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9800 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9800 / SCALE);
            }
            case ID_CHOOSE_AIRPORT_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1400 / SCALE);
            }
            case ID_CHOOSE_AIRPORT_LIST_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 8800 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 8600 / SCALE);
            }
            case ID_CHOOSE_AIRPORT_LIST_ITEM_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1000 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1200 / SCALE);
            }
            case ID_CHOOSE_AIRPORT_LIST_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 3000 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 4200 / SCALE);
            } 
            case ID_DROPDOWN_LIST_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 7450 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 8000 / SCALE);
            }
            case ID_DROPDOWN_Y_POS:
            {
                return PrimitiveTypeCache.valueOf(4);
            }
            case ID_SEARCH_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / 10000);
            }
            case ID_SEARCH_FIELD_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / 10000);
            }
            case ID_SEARCH_FIELD_NULLFIELD_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 150 / SCALE);
            }
        }
        return null;
    }

}

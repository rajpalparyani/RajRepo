/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteUiDecorator.java
 *
 */
package com.telenav.module.ac.favorite.editcategory;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
class EditCategoryUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_POPUP_WIDTH = 1;

    private final static int ID_BUTTON_DELETE_WIDTH = 3;

    private final static int ID_NULLFIELD_EDIT_CATEGORY_WIDTH = 4;

    private final static int ID_NULLFIELD_EDIT_CATEGORY_HEIGHT = 5;

    private final static int ID_BUTTON_EDIT_CATEGORY_WIDTH = 6;

    private final static int ID_NULLFIELD_DELETE_CATEGORY_POPUP_WIDTH = 7;

    private final static int ID_TOP_CONTAINER_HEIGHT = 8;
    
    private final static int ID_BUTON_HEIGHT = 9;
    
    private final static int ID_BUTTON_CONTAINER_HEIGHT = 10;
    
    private final static int ID_ALERT_POPUP_BUTTON_CONTAINER_HEIGHT = 11;
    
    private final static int ID_ALERT_POPUP_MULTILINE_WIDTH = 12;
    
    private final static int ID_ALERT_POPUP_MULTILINE_HEIGHT = 13;
    
    private final static int ID_TEXT_FIELD_HEIGHT = 14;
    
    private final static int ID_POPUP_TITLE_HEIGHT = 15;
    
    private final static int ID_POPUP_HINT_LABEL_HEIGHT = 16;
    
    private final static int ID_NULLFIELD_HEIGHT = 17;
    
    private final static int ID_MULTILINE_WIDTH = 18;
    
    private final static int ID_MULTILINE_HEIGHT = 19;
    
    private final static int ID_TEXT_FIELD_WIDTH = 20;
    
    public TnUiArgAdapter NULLFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_HEIGHT), this);

    public TnUiArgAdapter POPUP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_WIDTH), this);

    public TnUiArgAdapter BUTTON_DELETE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_DELETE_WIDTH), this);

    public TnUiArgAdapter NULLFIELD_EDIT_CATEGORY_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_EDIT_CATEGORY_WIDTH), this);

    public TnUiArgAdapter NULLFIELD_EDIT_CATEGORY_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_EDIT_CATEGORY_HEIGHT), this);

    public TnUiArgAdapter BUTTON_EDIT_CATEGORY_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_EDIT_CATEGORY_WIDTH), this);

    public TnUiArgAdapter NULLFIELD_DELETE_CATEGORY_POPUP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_DELETE_CATEGORY_POPUP_WIDTH),
            this);

    public TnUiArgAdapter TOP_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOP_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter BUTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTON_HEIGHT), this);
    
    public TnUiArgAdapter BUTTON_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter ALERT_POPUP_BUTTON_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ALERT_POPUP_BUTTON_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter ALERT_POPUP_MULTILINE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ALERT_POPUP_MULTILINE_WIDTH), this);
    
    public TnUiArgAdapter ALERT_POPUP_MULTILINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ALERT_POPUP_MULTILINE_HEIGHT), this);
    
    public TnUiArgAdapter TEXT_FIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXT_FIELD_HEIGHT), this);
    
    public TnUiArgAdapter TEXT_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXT_FIELD_WIDTH), this);
    
    public TnUiArgAdapter POPUP_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter POPUP_HINT_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_HINT_LABEL_HEIGHT), this);
    
    public TnUiArgAdapter MULTILINE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MULTILINE_WIDTH), this);
    
    public TnUiArgAdapter MULTILINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MULTILINE_HEIGHT), this);

    private final static int SCALE = 1000;

    public EditCategoryUiDecorator()
    {
    }

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_POPUP_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 950 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 850 / SCALE);
                }
            }
            case ID_BUTTON_DELETE_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 450 / SCALE);
            }
            case ID_NULLFIELD_EDIT_CATEGORY_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 50 / SCALE);
            }
            case ID_NULLFIELD_EDIT_CATEGORY_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 20 / SCALE);
            }
            case ID_BUTTON_EDIT_CATEGORY_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 375 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 350 / SCALE);
                }
            }
            case ID_NULLFIELD_DELETE_CATEGORY_POPUP_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 80 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 50 / SCALE);
            }
            case ID_TOP_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 270 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 380 / SCALE);
            }
            case ID_BUTON_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 65 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 105 / SCALE);
                }
            }
            case ID_BUTTON_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 190 / SCALE);
            }
            case ID_ALERT_POPUP_BUTTON_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 190 / SCALE);
            }
            case ID_ALERT_POPUP_MULTILINE_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 700 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 600/ SCALE);
            }
            case ID_ALERT_POPUP_MULTILINE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 200 / SCALE);
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
            case ID_TEXT_FIELD_WIDTH:
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                int padding = 40 * max / 480;
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 950 / SCALE - padding);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 850 / SCALE - padding);
                }
            }
            case ID_POPUP_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 70 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 91 / SCALE);
                }
            }
            case ID_POPUP_HINT_LABEL_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 50 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 70 / SCALE);
            }
            case ID_NULLFIELD_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 35 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 45 / SCALE);
            }
            case ID_MULTILINE_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 650 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 700/ SCALE);
            }
            case ID_MULTILINE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / SCALE);
            }
        }
        return null;
    }

}

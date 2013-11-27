/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * OneBoxSearchUiDecorator.java
 *
 */
package com.telenav.module.oneboxsearch;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-3
 */
public class OneBoxSearchUiDecorator extends AbstractCommonUiDecorator implements IOneBoxSearchConstants
{
//Add up from 100 to avoid collision with super home module.
    private final static int SCALE = 10000;
    private static final int ONEBOX_SEARCH_CONTAINER_WIDTH_INDEX = 100;
    private static final int ONEBOX_SEARCH_CONTAINER_HEIGHT_INDEX = 101;
    private static final int ONEBOX_SEARCH_TEXTBOX_WIDTH_INDEX = 102;
    private static final int ONEBOX_SEARCH_TEXTBOX_HEIGHT_INDEX = 103;
    private static final int ONEBOX_SUGGEST_LISTITEM_HEIGHT_INDEX = 104;
    private static final int ONEBOX_SUGGESTION_TITLE_HEIGHT_INDEX = 105;
    private static final int ONEBOX_SUGGESTION_LIST_ITEM_HEIGHT_INDEX = 106;
    private static final int ONEBOX_BUTTON_GAP_WIDTH_INDEX = 107;
    private static final int ONEBOX_SEARCH_BUTTON_WIDTH_INDEX = 108;
    private static final int ONEBOX_SEARCH_BUTTON_HEIGHT_INDEX = 109;
    private static final int ONEBOX_SEARCH_AREA_HEIGHT_INDEX = 110;
    private static final int ONEBOX_SEARCH_PLACE_HOLDER_WIDTH_INDEX = 111;
    private static final int ONEBOX_TITLE_BAR_HEIGHT_INDEX = 112;
    private static final int ONEBOX_SEARCH_AND_TAB_AREA_HEIGHT_INDEX = 113;
    private static final int TAB_BUTTON_WIDTH_INDEX = 114;
    private static final int TAB_BUTTON_HEIGHT_INDEX = 115;
    private static final int TAB_HEIGHT_INDEX = 116;
    private static final int TAB_BUTTON_GAP_WIDTH_INDEX = 117;
    private static final int TAB_BUTTON_ARROW_HEIGHT_INDEX = 118;
    private static final int SEARCH_BUTTON_CONTAINER_HEIGHT_INDEX = 119;
    private static final int ONEBOX_BUSINESS_SEARCH_TEXTBOX_WIDTH_INDEX = 120;
    private static final int ONEBOX_BUSINESS_SEARCH_CONTAINER_HEIGHT_INDEX = 121;
    private static final int ID_FILTER_LIST_HEIGHT = 122;
    private static final int ID_FILTER_LIST_WIDTH = 123;
    private static final int ONEBOX_TAB_ADDRESS_TEXTBOX_WIDTH_INDEX = 124;
    private static final int ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_INDEX = 125;
    
    private static final int ONEBOX_CITY_BUTTON_WIDTH_INDEX = 128;
    private static final int ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_SHORT_INDEX = 129;
    private static final int ONEBOX_CITY_BUTTON_WIDTH_LONG_INDEX = 130;
    private static final int ID_ADDRESS_TEMPLATE_HEIGHT = 133;
    private static final int TOP_CONTAINER_HORIZONTAL_PADDING_INDEX = 134;
    private static final int ID_ADDRESS_ITEM_HEIGHT = 135;
    private static final int ID_ADDRESS_FIELD_WIDTH = 136;
    private static final int ID_LABEL_TITLE_HEIGHT = 137;
    private final static int ID_TEXTFIELD_HOME_HEIGHT = 138;
    private final static int ID_DROPDOWN_LIST_HEIGHT = 139;
    
    public TnUiArgAdapter ADDRESS_TEMPLATE_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ID_ADDRESS_TEMPLATE_HEIGHT), this);
    
    public TnUiArgAdapter TOP_CONTAINER_HORIZONTAL_PADDING = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(TOP_CONTAINER_HORIZONTAL_PADDING_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SEARCH_CONTAINER_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_CONTAINER_WIDTH_INDEX), this);

    public TnUiArgAdapter ONEBOX_SEARCH_CONTAINER_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_CONTAINER_HEIGHT_INDEX), this);

    public TnUiArgAdapter ONEBOX_SEARCH_TEXTBOX_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_TEXTBOX_WIDTH_INDEX), this);

    public TnUiArgAdapter ONEBOX_SEARCH_TEXTBOX_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_TEXTBOX_HEIGHT_INDEX), this);

    public TnUiArgAdapter ONEBOX_SUGGEST_LISTITEM_HEIGHT = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ONEBOX_SUGGEST_LISTITEM_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SUGGESTION_TITLE_HEIGHT = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ONEBOX_SUGGESTION_TITLE_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SUGGESTION_LIST_ITEM_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SUGGESTION_LIST_ITEM_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_BUTTON_GAP_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_BUTTON_GAP_WIDTH_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SEARCH_BUTTON_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_BUTTON_WIDTH_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SEARCH_BUTTON_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_BUTTON_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SEARCH_AREA_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_AREA_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SEARCH_PLACE_HOLDER_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_PLACE_HOLDER_WIDTH_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_TITLE_BAR_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_TITLE_BAR_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_SEARCH_AND_TAB_AREA_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_AND_TAB_AREA_HEIGHT_INDEX), this);

    public TnUiArgAdapter TAB_BUTTON_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(TAB_BUTTON_WIDTH_INDEX), this);
    public TnUiArgAdapter TAB_BUTTON_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(TAB_BUTTON_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter TAB_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(TAB_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter TAB_BUTTON_GAP_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(TAB_BUTTON_GAP_WIDTH_INDEX), this);
    
    public TnUiArgAdapter TAB_BUTTON_ARROW_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(TAB_BUTTON_ARROW_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter SEARCH_BUTTON_CONTAINER_HEIGT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(SEARCH_BUTTON_CONTAINER_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_BUSINESS_SEARCH_TEXTBOX_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_BUSINESS_SEARCH_TEXTBOX_WIDTH_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_BUSINESS_SEARCH_CONTAINER_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_BUSINESS_SEARCH_CONTAINER_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter FILTER_LIST_HEIGHT = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ID_FILTER_LIST_HEIGHT), this);
    
    public TnUiArgAdapter FILTER_LIST_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ID_FILTER_LIST_WIDTH), this);
    
    public TnUiArgAdapter ONEBOX_TAB_ADDRESS_TEXTBOX_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_TAB_ADDRESS_TEXTBOX_WIDTH_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_CITY_BUTTON_WIDTH = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ONEBOX_CITY_BUTTON_WIDTH_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_CITY_BUTTON_WIDTH_LONG = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(ONEBOX_CITY_BUTTON_WIDTH_LONG_INDEX), this);
    
    public TnUiArgAdapter ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_SHORT = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_SHORT_INDEX), this);
        
    public TnUiArgAdapter ADDRESS_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ADDRESS_ITEM_HEIGHT), this);
    public TnUiArgAdapter ADDRESS_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ADDRESS_FIELD_WIDTH), this);
    public TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);
    public TnUiArgAdapter TEXTFIELD_HOME_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_HOME_HEIGHT), this);
    public TnUiArgAdapter DROPDOWN_LIST_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROPDOWN_LIST_HEIGHT), this);

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case TOP_CONTAINER_HORIZONTAL_PADDING_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 3125 / 100000);            
            }
            case ONEBOX_SEARCH_CONTAINER_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 10);
            }
            case ONEBOX_SEARCH_CONTAINER_WIDTH_INDEX:
            {
                return (AppConfigHelper.getDisplayWidth()- TOP_CONTAINER_HORIZONTAL_PADDING.getInt() * 2);
            }
            case ONEBOX_SEARCH_TEXTBOX_WIDTH_INDEX:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 7900 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 8000 / SCALE);
                }
            }
            case ONEBOX_SEARCH_TEXTBOX_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / SCALE);
            }
            case ONEBOX_SUGGEST_LISTITEM_HEIGHT_INDEX:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 830 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1230 / SCALE);
            }
            case ONEBOX_SUGGESTION_TITLE_HEIGHT_INDEX:
            {
                if(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 800 / SCALE);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 950 / SCALE);
                }
            }
            case ONEBOX_SUGGESTION_LIST_ITEM_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 830 / SCALE);
            }
            case ONEBOX_BUTTON_GAP_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 100);
            }
            case ONEBOX_SEARCH_BUTTON_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / 10000);
            }
            case ONEBOX_SEARCH_BUTTON_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / 10000);
            }
            case ONEBOX_SEARCH_AREA_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1700 / SCALE);
            }
            case ONEBOX_SEARCH_PLACE_HOLDER_WIDTH_INDEX:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(maxHeight * 660 / 10000 + maxHeight / 100);
            }
            case ONEBOX_TITLE_BAR_HEIGHT_INDEX:
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
            case ONEBOX_SEARCH_AND_TAB_AREA_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 2500 / SCALE);
            }
            case TAB_BUTTON_GAP_WIDTH_INDEX:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return AppConfigHelper.getDisplayWidth()  * 188 / SCALE;
                }
                else
                {
                    return AppConfigHelper.getDisplayWidth()  * 222 / SCALE;
                }
            }            
            case TAB_BUTTON_ARROW_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(9);
            }    
            case TAB_BUTTON_WIDTH_INDEX:
            {
                int width = ONEBOX_TAB_ADDRESS_TEXTBOX_WIDTH.getInt() + ONEBOX_BUTTON_GAP_WIDTH.getInt() + ONEBOX_SEARCH_BUTTON_WIDTH.getInt();
                return PrimitiveTypeCache.valueOf(width - ONEBOX_SEARCH_BUTTON_WIDTH.getInt() - TAB_BUTTON_GAP_WIDTH.getInt() * 2) / 2;
            }
            case TAB_BUTTON_HEIGHT_INDEX:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 6875 / 100000;
                return PrimitiveTypeCache.valueOf(height + TAB_BUTTON_ARROW_HEIGHT.getInt());
            }
            case TAB_HEIGHT_INDEX:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 937 / SCALE;
                return PrimitiveTypeCache.valueOf(height);
            }
            case SEARCH_BUTTON_CONTAINER_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_BUTTON_HEIGHT.getInt() + TAB_BUTTON_ARROW_HEIGHT.getInt());
            }
            
            case ONEBOX_BUSINESS_SEARCH_TEXTBOX_WIDTH_INDEX:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 7200 / SCALE);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 7300 / SCALE);
                }
            }
            case ONEBOX_BUSINESS_SEARCH_CONTAINER_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 990 / SCALE);
            }
            case ID_FILTER_LIST_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 3000 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 4200 / SCALE);
            }
            case ID_FILTER_LIST_WIDTH:
            {
                int maxDisplaySize = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
                if(maxDisplaySize <= 480)
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 8290 / SCALE);
                    else
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 9180 / SCALE);
                }
                else
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 8290 / SCALE);
                    else
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 9220 / SCALE);
                }
            }
            case ONEBOX_TAB_ADDRESS_TEXTBOX_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_CONTAINER_WIDTH.getInt() - ONEBOX_SEARCH_BUTTON_WIDTH.getInt() - ONEBOX_BUTTON_GAP_WIDTH.getInt());
//                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
//                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 8390 / SCALE);
//                else
//                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 8880 / SCALE);
            }
            case ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_CONTAINER_WIDTH.getInt() - ONEBOX_SEARCH_BUTTON_WIDTH.getInt() - ONEBOX_BUTTON_GAP_WIDTH.getInt());
//                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
//                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 8390 / SCALE);
//                else
//                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 8880 / SCALE);
            }
            case ONEBOX_CITY_BUTTON_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1100 / 10000); 
            }
            case ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_SHORT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(ONEBOX_SEARCH_CONTAINER_WIDTH.getInt() - ONEBOX_SEARCH_BUTTON_WIDTH.getInt() - ONEBOX_SEARCH_BUTTON_WIDTH.getInt() - 2 * ONEBOX_BUTTON_GAP_WIDTH.getInt()); 
//                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
//                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 7258 / SCALE);
//                else
//                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 8222 / SCALE);
            }
            case ONEBOX_CITY_BUTTON_WIDTH_LONG_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1650 / 10000);
            }
            case ID_ADDRESS_TEMPLATE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - TAB_HEIGHT.getInt());
            }
            case ID_ADDRESS_ITEM_HEIGHT:
            {
                int h = AppConfigHelper.getMaxDisplaySize() * 132 / 1280;
                return PrimitiveTypeCache.valueOf(h);
            }
            case ID_ADDRESS_FIELD_WIDTH:
            {
                int width = AppConfigHelper.getDisplayWidth();
                int w = width - 2* AppConfigHelper.getMinDisplaySize() * 32 / 720;
                return PrimitiveTypeCache.valueOf(w);
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
            case ID_TEXTFIELD_HOME_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 480 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 720 / SCALE);
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

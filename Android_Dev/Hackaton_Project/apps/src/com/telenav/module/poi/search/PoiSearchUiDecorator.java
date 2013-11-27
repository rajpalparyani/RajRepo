/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiSearchUiDecorator.java
 *
 */
package com.telenav.module.poi.search;

import com.telenav.app.android.scout_us.R;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-18
 */
class PoiSearchUiDecorator extends AbstractCommonUiDecorator
{
    public static int QUICK_SEARCHING_LINE_ICON_NUM_P = 4;
    public static int QUICK_SEARCHING_LINE_ICON_NUM_L = 6;
    
    private static final int TOP_MARGIN_INDEX = 1;
    private static final int ICON_BTN_WIDTH_INDEX = 2;
    private static final int TEXTFIELD_WIDTH_INDEX = 3;
    private static final int TOPMARGIN_HEIGHT_INDEX = 4;
    private static final int TEXTFIELD_HEIGHT_INDEX = 5;
    private static final int TITLE_BAR_HEIGHT_INDEX = 6;
    private static final int SEARCH_INPUTBOX_CONTAINER_WIDTH_INDEX = 7;
    private static final int SEARCH_INPUTBOX_CONTAINER_HEIGHT_INDEX = 8;
    private static final int PROGRESS_BAR_WIDTH_INDEX = 9;
    private static final int PROGRESS_BAR_HEIGHT_INDEX = 10;
    private static final int QUICK_SEARCH_BAR_CONTAINER_HEIGHT_INDEX = 11;
    private static final int TOP_CONTAINER_WIDTH_INDEX = 15;
    private static final int PLACE_TITLE_BAR_HEIGHT_INDEX = 16;
    private static final int TITLE_LABEL_WIDTH_INDEX = 17;
    private static final int HEADER_HOME_ICON_WIDTH_INDEX = 18;
    private static final int POI_LIST_PADDING_INDEX = 19;
    private static final int ID_POI_LIST_SEPERATE_LINE_HEIGHT = 20;
    private static final int ID_TEXTFIELD_HEIGHT = 21;
    private static final int ID_PANEL_TOP_NULLFIELD_HEIGHT = 22;
    private static final int ID_TEXTFIELD_WIDTH = 23;
    
    public TnUiArgAdapter QUICK_FIND_TOP_MARGIN = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_MARGIN_INDEX), this);
    public TnUiArgAdapter QUICK_FIND_ICON_BTN_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ICON_BTN_WIDTH_INDEX), this);
    public TnUiArgAdapter QUICK_FIND_TEXTFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TEXTFIELD_WIDTH_INDEX), this);
    public TnUiArgAdapter QUICK_FIND_TOPMARGIN_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOPMARGIN_HEIGHT_INDEX), this);
    public TnUiArgAdapter QUICK_FIND_TEXTFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TEXTFIELD_HEIGHT_INDEX), this);
    public TnUiArgAdapter TITLEBAR_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TITLE_BAR_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter SEARCH_INPUTBOX_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(SEARCH_INPUTBOX_CONTAINER_WIDTH_INDEX), this);
    public TnUiArgAdapter SEARCH_INPUTBOX_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(SEARCH_INPUTBOX_CONTAINER_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter PROGRESS_BAR_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PROGRESS_BAR_WIDTH_INDEX), this);
    public TnUiArgAdapter PROGRESS_BAR_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PROGRESS_BAR_HEIGHT_INDEX), this);
    
    public TnUiArgAdapter QUICK_SEARCH_BAR_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(QUICK_SEARCH_BAR_CONTAINER_HEIGHT_INDEX), this);
   
    public TnUiArgAdapter TOP_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_WIDTH_INDEX), this);
    
    public TnUiArgAdapter PLACE_TITLE_BAR_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PLACE_TITLE_BAR_HEIGHT_INDEX), this);
	
    public TnUiArgAdapter TITLE_LABEL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TITLE_LABEL_WIDTH_INDEX), this);

    public TnUiArgAdapter HEADER_HOME_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(HEADER_HOME_ICON_WIDTH_INDEX), this);
    
    public TnUiArgAdapter POI_LIST_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(POI_LIST_PADDING_INDEX), this);
    public TnUiArgAdapter POI_LIST_SEPERATE_LINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POI_LIST_SEPERATE_LINE_HEIGHT), this);
    public TnUiArgAdapter TEXTFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_HEIGHT), this);
    
    public TnUiArgAdapter PANEL_TOP_NULLFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PANEL_TOP_NULLFIELD_HEIGHT), this);
    public TnUiArgAdapter TEXTFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_WIDTH), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        int width = AppConfigHelper.getDisplayWidth();
        int height = AppConfigHelper.getDisplayHeight();
        int nullFullHeight = height - AppConfigHelper.getStatusBarHeight();
        int key = ((Integer)args.getKey()).intValue();
        switch(key)
        {
            case TOP_MARGIN_INDEX:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
                {
                    return PrimitiveTypeCache.valueOf(nullFullHeight * 90 / 10000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(nullFullHeight * 50 / 10000);
                }
            }
            case ICON_BTN_WIDTH_INDEX:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(width / QUICK_SEARCHING_LINE_ICON_NUM_P);
                    
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(width / QUICK_SEARCHING_LINE_ICON_NUM_L);
                }
            }
            case TOPMARGIN_HEIGHT_INDEX:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
                {
                    return PrimitiveTypeCache.valueOf(nullFullHeight * 90/10000 );
                }else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / 10000);
                }
            }
            case TEXTFIELD_WIDTH_INDEX:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9400 / 10000);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9500 / 10000);
                }
            }
            case TEXTFIELD_HEIGHT_INDEX:
            {
                int maxLength = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(maxLength * 677 / 10000);
            }
            case SEARCH_INPUTBOX_CONTAINER_WIDTH_INDEX:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case SEARCH_INPUTBOX_CONTAINER_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 12);
            }
            case TITLE_BAR_HEIGHT_INDEX:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 840 / 10000);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1270 / 10000);
                }
            }
            case PROGRESS_BAR_WIDTH_INDEX:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(width * 60 /100);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(height * 60 /100);
                }
            }
            case PROGRESS_BAR_HEIGHT_INDEX:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(height * 30 /100);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(width * 30 /100);
                }
            }
            case QUICK_SEARCH_BAR_CONTAINER_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 10);

            }
            case TOP_CONTAINER_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth()/* - UiFactory.getInstance().getCharWidth() * 2*/);
            }
            case PLACE_TITLE_BAR_HEIGHT_INDEX:
            {
            	return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1146 / 10000);
            }
            case TITLE_LABEL_WIDTH_INDEX:
            {
                //TOP_CONTAINER_WIDTH_INDEX - 2 * HEADER_HOME_ICON_WIDTH_INDEX
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() /* * (10000 - 2 * 1813) / 10000*/ - UiFactory.getInstance().getCharWidth() * 2);
            }
            case HEADER_HOME_ICON_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 1813 / 10000);
            }
            case POI_LIST_PADDING_INDEX:
            {
                return PrimitiveTypeCache.valueOf((SCREEN_WIDTH.getInt() - QUICK_FIND_TEXTFIELD_WIDTH.getInt()) / 2);
            }
            case ID_POI_LIST_SEPERATE_LINE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(4);
            }
            case ID_TEXTFIELD_HEIGHT:
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
            case ID_PANEL_TOP_NULLFIELD_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 2343 / 100000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 700 / 100000);
                }
            }
            case ID_TEXTFIELD_WIDTH:
            {
                int margin = (int)AndroidCitizenUiHelper.getResources().getDimension(R.dimen.common_side_margin);
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - margin*2);
            }
          }
            
        return null;
    }

}

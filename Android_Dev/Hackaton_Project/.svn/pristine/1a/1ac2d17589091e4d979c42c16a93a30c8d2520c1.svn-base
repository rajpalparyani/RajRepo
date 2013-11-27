/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiResultUiDecorator.java
 *
 */
package com.telenav.module.poi.result;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2012-4-5
 */
class PoiResultUiDecorator extends AbstractCommonUiDecorator
{
    private static final int TOP_CONTAINER_HORIZONTAL_PADDING_INDEX = 1;
    private static final int TOP_CONTAINER_VERTICAL_PADDING_INDEX = 2;
    private static final int TOP_CONTAINER_HORIZONTAL_GAP_INDEX = 3;
    private static final int TOP_CONTAINER_VERTICAL_GAP_INDEX = 4;
    
    private static final int TOP_NARROW_SEARCH_TEXTFIELD_WIDTH_INDEX = 5;
    private static final int TOP_WIDE_SEARCH_TEXTFIELD_WIDTH_INDEX = 6;
    private static final int TOP_SEARCH_TEXTFIELD_HEIGHT_INDEX = 7;
    private static final int TOP_CONTAINER_WIDTH_INDEX = 8;
    private static final int TOP_CONTAINER_ONELINE_HEIGHT_INDEX = 9;
    private static final int TOP_CONTAINER_TWOLINE_HEIGHT_INDEX = 10;

    private static final int LIST_TITLE_LABEL_WIDTH_INDEX = 11;
    private static final int LIST_TITLE_HEIGHT_INDEX = 12;
    private static final int LIST_TITLE_ALONGROUTE_HEIGHT_INDEX = 13;
    private static final int LIST_TITLE_VERTICAL_PADDING_INDEX = 14;

    private static final int TOP_CONTAINER_BUTTON_WIDTH_INDEX = 15;
    private static final int TOP_CONTAINER_BUTTON_HEIGHT_INDEX = 16;
    private static final int ALONGROUTE_BUTTON_WIDTH_INDEX = 17;
    private final static int ALONGROUTE_BUTTON_HEIGHT_INDEX = 18;
    private final static int ALONGROUTE_BUTTON_CONTAINER_WIDTH_INDEX = 19;
    
    private static final int SORT_COMPONENT_WIDTH_INDEX = 20;
    private static final int SORT_TITLE_WIDTH_INDEX = 21;
    private static final int SORT_ICON_WIDTH_INDEX = 22;
    private static final int SORT_SPERATOR_WIDTH_INDEX = 23;
    private static final int SORT_COMPONENT_HEIGHT_INDEX = 24;
    private final static int SORT_POPUP_TITLE_HEIGHT_INDEX = 25;
    
    private static final int BANNER_ADS_CONTAINER_HEIGHT_INDEX = 26;
    private static final int BANNER_ADS_IMAGE_WIDTH_INDEX = 27;
    private static final int BANNER_ADS_IMAGE_HEIGHT_INDEX = 28;
    
    private static final int FEEDBACK_CONTAINER_HEIGHT_INDEX = 29;
    private static final int FEEDBACK_CONTAINER_POS_Y_INDEX = 30;
    private static final int POI_LIST_ITEM_INDEX_ICON_PADDING_INDEX = 31;
   
  
    public TnUiArgAdapter TOP_CONTAINER_HORIZONTAL_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_HORIZONTAL_GAP_INDEX), this);
    public TnUiArgAdapter TOP_CONTAINER_VERTICAL_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PrimitiveTypeCache.valueOf(TOP_CONTAINER_VERTICAL_GAP_INDEX)), this);
    public TnUiArgAdapter TOP_CONTAINER_HORIZONTAL_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_HORIZONTAL_PADDING_INDEX), this);
    public TnUiArgAdapter TOP_CONTAINER_VERTICAL_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_VERTICAL_PADDING_INDEX), this);
    
    public TnUiArgAdapter TOP_NARROW_SEARCH_TEXTFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PrimitiveTypeCache.valueOf(TOP_NARROW_SEARCH_TEXTFIELD_WIDTH_INDEX)), this);
    public TnUiArgAdapter TOP_WIDE_SEARCH_TEXTFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_WIDE_SEARCH_TEXTFIELD_WIDTH_INDEX), this);
    public TnUiArgAdapter TOP_SEARCH_TEXTFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_SEARCH_TEXTFIELD_HEIGHT_INDEX), this);
    public TnUiArgAdapter TOP_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_WIDTH_INDEX), this);
    public TnUiArgAdapter TOP_CONTAINER_ONELINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_ONELINE_HEIGHT_INDEX), this);
    public TnUiArgAdapter TOP_CONTAINER_TWOLINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_TWOLINE_HEIGHT_INDEX), this);

    public TnUiArgAdapter LIST_TITLE_LABEL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(LIST_TITLE_LABEL_WIDTH_INDEX), this);
    public TnUiArgAdapter LIST_TITLE_HEIGHT= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(LIST_TITLE_HEIGHT_INDEX), this);
    public TnUiArgAdapter LIST_TITLE_ALONGROUTE_HEIGHT= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(LIST_TITLE_ALONGROUTE_HEIGHT_INDEX), this);
    public TnUiArgAdapter LIST_TITLE_VERTICAL_PADDING= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(LIST_TITLE_VERTICAL_PADDING_INDEX), this);
  
    public TnUiArgAdapter TOP_CONTAINER_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_BUTTON_WIDTH_INDEX), this);
    public TnUiArgAdapter TOP_CONTAINER_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TOP_CONTAINER_BUTTON_HEIGHT_INDEX), this);
    public TnUiArgAdapter ALONGROUTE_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ALONGROUTE_BUTTON_WIDTH_INDEX), this);
    public TnUiArgAdapter ALONGROUTE_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ALONGROUTE_BUTTON_HEIGHT_INDEX), this);
    public TnUiArgAdapter ALONGROUTE_BUTTON_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ALONGROUTE_BUTTON_CONTAINER_WIDTH_INDEX), this);
    
    public TnUiArgAdapter SORT_COMPONENT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(SORT_COMPONENT_WIDTH_INDEX), this);
    public TnUiArgAdapter SORT_TITLE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(SORT_TITLE_WIDTH_INDEX), this);
    public TnUiArgAdapter SORT_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(SORT_ICON_WIDTH_INDEX), this);
    public TnUiArgAdapter SORT_SPERATOR_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(SORT_SPERATOR_WIDTH_INDEX), this);
    public TnUiArgAdapter SORT_COMPONENT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(SORT_COMPONENT_HEIGHT_INDEX), this);
    public TnUiArgAdapter SORT_POPUP_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PrimitiveTypeCache.valueOf(SORT_POPUP_TITLE_HEIGHT_INDEX)), this);

    public TnUiArgAdapter BANNER_ADS_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(BANNER_ADS_CONTAINER_HEIGHT_INDEX), this);
    public TnUiArgAdapter BANNER_ADS_IMAGE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(BANNER_ADS_IMAGE_WIDTH_INDEX), this);
    public TnUiArgAdapter BANNER_ADS_IMAGE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(BANNER_ADS_IMAGE_HEIGHT_INDEX), this);
  
    public TnUiArgAdapter FEEDBACK_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PrimitiveTypeCache.valueOf(FEEDBACK_CONTAINER_HEIGHT_INDEX)), this);
    public TnUiArgAdapter FEEDBACK_CONTAINER_POS_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PrimitiveTypeCache.valueOf(FEEDBACK_CONTAINER_POS_Y_INDEX)), this);
    public TnUiArgAdapter POI_LIST_ITEM_INDEX_ICON_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PrimitiveTypeCache.valueOf(POI_LIST_ITEM_INDEX_ICON_PADDING_INDEX)), this);
 
   
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int value = ((Integer)args.getKey()).intValue();
        int width = AppConfigHelper.getDisplayWidth();
        int height = AppConfigHelper.getDisplayHeight();
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        
        switch(value)
        {

            case TOP_CONTAINER_HORIZONTAL_PADDING_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 3125 / 100000);            
            }
            case TOP_CONTAINER_VERTICAL_PADDING_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize()* 1875 / 100000);
            }
            case TOP_CONTAINER_HORIZONTAL_GAP_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 100);
            }
            case TOP_CONTAINER_VERTICAL_GAP_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize()* 1875 / 100000);
            }
            case TOP_NARROW_SEARCH_TEXTFIELD_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_WIDE_SEARCH_TEXTFIELD_WIDTH.getInt() - TOP_CONTAINER_BUTTON_WIDTH.getInt() - TOP_CONTAINER_HORIZONTAL_GAP.getInt());
            }
            case TOP_WIDE_SEARCH_TEXTFIELD_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_CONTAINER_WIDTH.getInt() - TOP_CONTAINER_BUTTON_WIDTH.getInt() - TOP_CONTAINER_HORIZONTAL_GAP.getInt());
            }
            case TOP_SEARCH_TEXTFIELD_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / 10000);
            }
            case TOP_CONTAINER_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt() - TOP_CONTAINER_HORIZONTAL_PADDING.getInt() * 2);            
            }
            case TOP_CONTAINER_ONELINE_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_SEARCH_TEXTFIELD_HEIGHT.getInt() + 2 * TOP_CONTAINER_VERTICAL_PADDING.getInt());
            }
            case TOP_CONTAINER_TWOLINE_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_CONTAINER_ONELINE_HEIGHT.getInt() + TOP_SEARCH_TEXTFIELD_HEIGHT.getInt() + TOP_CONTAINER_VERTICAL_GAP.getInt());
            }
            
            case LIST_TITLE_LABEL_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_CONTAINER_WIDTH.getInt()  - TOP_CONTAINER_BUTTON_WIDTH.getInt() - TOP_CONTAINER_HORIZONTAL_GAP.getInt() - SORT_COMPONENT_WIDTH.getInt() - TOP_CONTAINER_HORIZONTAL_GAP.getInt());            
            }
            case LIST_TITLE_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_CONTAINER_BUTTON_HEIGHT.getInt() + 2 * LIST_TITLE_VERTICAL_PADDING.getInt());            
            }
            case LIST_TITLE_ALONGROUTE_HEIGHT_INDEX:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(TOP_CONTAINER_BUTTON_HEIGHT.getInt() + 2 * LIST_TITLE_VERTICAL_PADDING.getInt());            
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(ALONGROUTE_BUTTON_HEIGHT.getInt() + 2 * LIST_TITLE_VERTICAL_PADDING.getInt());
                }
            }
            case LIST_TITLE_VERTICAL_PADDING_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_CONTAINER_VERTICAL_PADDING.getInt() * 50 / 100);
            }

            
            case TOP_CONTAINER_BUTTON_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / 10000);
            }
            case TOP_CONTAINER_BUTTON_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 660 / 10000);
            }
            case ALONGROUTE_BUTTON_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(ALONGROUTE_BUTTON_CONTAINER_WIDTH.getInt() / 2);
            }
            case ALONGROUTE_BUTTON_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_SEARCH_TEXTFIELD_HEIGHT.getInt());
            }
            case ALONGROUTE_BUTTON_CONTAINER_WIDTH_INDEX:
            {
                int totalWidth;
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    totalWidth = TOP_CONTAINER_WIDTH.getInt();
                }
                else
                {
                    totalWidth = LIST_TITLE_LABEL_WIDTH.getInt(); 
                }
                return PrimitiveTypeCache.valueOf(totalWidth);
            }

            case SORT_COMPONENT_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 3625 /10000);
            }
            case SORT_TITLE_WIDTH_INDEX:
            {
                int totalWidth = SORT_COMPONENT_WIDTH.getInt() - SORT_SPERATOR_WIDTH.getInt();
                return PrimitiveTypeCache.valueOf(totalWidth * 78 / 100);
            }
            case SORT_ICON_WIDTH_INDEX:
            {
                int totalWidth = SORT_COMPONENT_WIDTH.getInt() - SORT_SPERATOR_WIDTH.getInt();
                return PrimitiveTypeCache.valueOf(totalWidth * 22 / 100);
            }
            case SORT_SPERATOR_WIDTH_INDEX:
            {
                return PrimitiveTypeCache.valueOf(3);
            }
            case SORT_COMPONENT_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(TOP_CONTAINER_BUTTON_HEIGHT.getInt() - AppConfigHelper.getMaxDisplaySize() * 50 /10000);
            }
            case SORT_POPUP_TITLE_HEIGHT_INDEX:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();
                int gapHeight = maxHeight * 1 / 100;
                int fontHeight = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE).getHeight();
                return PrimitiveTypeCache.valueOf(gapHeight*2 + fontHeight);            
            }
            
            case BANNER_ADS_CONTAINER_HEIGHT_INDEX:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(height * 13 / 100);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(height * 15 / 100);
                }
            }
            case BANNER_ADS_IMAGE_WIDTH_INDEX:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(width * 96 / 100);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(height * 96 / 100);
                }
            }
            case BANNER_ADS_IMAGE_HEIGHT_INDEX:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(height * 10 / 100);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(height * 12 / 100);
                }
            }
            
            case FEEDBACK_CONTAINER_HEIGHT_INDEX:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 40 / 1000);
            }
            case FEEDBACK_CONTAINER_POS_Y_INDEX:
            {
                int y = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() - FEEDBACK_CONTAINER_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(y);
            }
            case POI_LIST_ITEM_INDEX_ICON_PADDING_INDEX:
            {
                int padding = AppConfigHelper.getMinDisplaySize() * 315 / 10000;
                return PrimitiveTypeCache.valueOf(padding);
            }
        }
        return null;
    }

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficUiDecorator.java
 *
 */
package com.telenav.module.nav.traffic;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-10-26
 */
class TrafficUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_TRAFFIC_ITEM_HEIGHT = 1;

    private final static int ID_TRAFFIC_ITEM_ICON_SIZE = 2;

    private final static int ID_TRAFFIC_ITEM_SECOND_PART_PADDING = 3;

    private final static int ID_TRAFFIC_TITLE_GAP = 5;
    
    private final static int ID_TRAFFIC_INFO_GAP = 6;
    
    private final static int ID_TRAFFIC_SUMMARY_CONTAINER_HEIGHT = 11;
    
    private final static int ID_TRAFFIC_SUMMARY_CONTAINER_WIDTH = 12;
    
    private final static int ID_TRAFFIC_DETAIL_MULTILINE_WIDTH = 13;

    private final static int ID_TRAFFIC_DETAIL_MULTILINE_HEIGHT = 14;
    
    private final static int ID_TRAFFIC_ALTERNATE_TITLE_ADDRESS_BOX_WIDTH = 15;
    
    private final static int ID_TRAFFIC_ALTERNATE_TITLE_LEFT_WIDTH = 16;
    
    private final static int ID_TAB_BUTTON_WIDTH = 17;
    
    private final static int ID_TAB_BUTTON_HEIGHT = 18;
    
    private final static int ID_TAB_OUTER_CONTAINER_HEIGHT = 19;
    
    private final static int ID_SUMMARY_TITLE_WIDTH = 20;
    
    private final static int ID_SUMMARY_TITLE_HEIGHT = 21;
    
    private final static int ID_SUMMARY_INFO_HEIGHT = 22;
    
    private final static int ID_SUMMARY_INFO_WIDTH = 23;
    
    private final static int ID_SUMMARY_INFO_H_PADDING = 24;
    
    private final static int ID_SUMMARY_INFO_V_PADDING = 25;
    
    private final static int ID_SUMMARY_INFO_LABEL_HEIGHT = 26;
    
    private final static int ID_TRAFFIC_ITEM_FIRST_PART_WIDTH = 28;
    
    private final static int ID_TRAFFIC_ITEM_FIRST_PART_LEFT_PADDING = 29;

    private final static int ID_ALTERNATE_ROUTE_TITLE_HEIGHT = 30;
    
    private final static int ID_NULL_FIELD_WIDTH = 31;

    private final static int ID_BOTTOM_CONTAINER_HEIGHT = 32;

    private final static int ID_BOTTOM_CONTAINER_Y = 33;

    private final static int ID_BUTTON_WIDTH = 34;
    
    private final static int ID_MINIMIZE_DELAY_BUTTON_HPADDING = 35;
    
    private final static int ID_MINIMIZE_DELAY_BUTTON_VPADDING = 36;
    
    private final static int ID_AVOID_SEGMENT_BUTTON_WIDTH = 37;
    
    private final static int ID_INCIDENT_PADDING = 38;
    
    private final static int ID_TRAFFIC_DETAIL_ICON_TEXT_PADDING = 39;
    
    private final static int ID_SUGGEST_ROUTE_HEADER_ARC = 40;
    
    private final static int ID_SUGGEST_ROUTE_HEADER_ADDR_WIDTH = 41;
    
    private final static int ID_INCIDENT_CONTAINER_HEIGHT = 42;

    private final static int ID_SUMMARY_INFO_HEIGHT_LANDSCAPE = 43;
    
    private final static int ID_SUMMARY_INFO_V_PADDING_LANDSCAPE = 44;
    
    private final static int ID_TRAFFIC_SUMMARY_CONTAINER_HEIGHT_LANDSCAPE = 45;
    
    private final static int ID_MAP_LOGO_Y_WITH_BOTTOM_CONTAINER = 46;

    private final static int ID_TRAFFIC_MAP_SUMMARY_HEIGHT = 47;
    
    private final static int ID_TRAFFIC_MAP_SUMMARY_Y = 48;
    
    public TnUiArgAdapter TRAFFIC_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ITEM_HEIGHT), this);

    public TnUiArgAdapter TRAFFIC_ITEM_ICON_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ITEM_ICON_SIZE), this);

    public TnUiArgAdapter TRAFFIC_ITEM_SECOND_PART_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ITEM_SECOND_PART_PADDING), this);

    public TnUiArgAdapter TRAFFIC_TITLE_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_TITLE_GAP), this);
    
    public TnUiArgAdapter TRAFFIC_INFO_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_INFO_GAP), this);
    
    public TnUiArgAdapter TRAFFIC_SUMMARY_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_SUMMARY_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter TRAFFIC_SUMMARY_CONTAINER_HEIGHT_LANDSCAPE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_SUMMARY_CONTAINER_HEIGHT_LANDSCAPE), this);
    
    public TnUiArgAdapter TRAFFIC_SUMMARY_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_SUMMARY_CONTAINER_WIDTH), this);
    
    public TnUiArgAdapter TRAFFIC_DETAIL_MULTILINE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_DETAIL_MULTILINE_WIDTH), this);

    public TnUiArgAdapter TRAFFIC_DETAIL_MULTILINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_DETAIL_MULTILINE_HEIGHT), this);
    
    public TnUiArgAdapter TRAFFIC_ALTERNATE_TITLE_ADDRESS_BOX_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALTERNATE_TITLE_ADDRESS_BOX_WIDTH), this);
    public TnUiArgAdapter TRAFFIC_ALTERNATE_TITLE_LEFT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALTERNATE_TITLE_LEFT_WIDTH), this);
    
    public TnUiArgAdapter TAB_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_BUTTON_WIDTH), this);
    public TnUiArgAdapter TAB_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_BUTTON_HEIGHT), this);
    public TnUiArgAdapter TAB_OUTER_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_OUTER_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter SUMMARY_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_TITLE_HEIGHT), this);
    public TnUiArgAdapter SUMMARY_TITLE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_TITLE_WIDTH), this);
    public TnUiArgAdapter SUMMARY_INFO_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_INFO_WIDTH), this);
    public TnUiArgAdapter SUMMARY_INFO_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_INFO_HEIGHT), this);
    public TnUiArgAdapter SUMMARY_INFO_HEIGHT_LANDSCAPE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_INFO_HEIGHT_LANDSCAPE), this);
    public TnUiArgAdapter SUMMARY_INFO_H_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_INFO_H_PADDING), this);
    public TnUiArgAdapter SUMMARY_INFO_V_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_INFO_V_PADDING), this);
    public TnUiArgAdapter SUMMARY_INFO_V_PADDING_LANDSCAPE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_INFO_V_PADDING_LANDSCAPE), this);
    public TnUiArgAdapter SUMMARY_INFO_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_INFO_LABEL_HEIGHT), this);
    public TnUiArgAdapter TRAFFIC_ITEM_FIRST_PART_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ITEM_FIRST_PART_WIDTH), this);
    public TnUiArgAdapter TRAFFIC_ITEM_FIRST_PART_LEFT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ITEM_FIRST_PART_LEFT_PADDING), this);
    public TnUiArgAdapter ALTERNATE_ROUTE_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ALTERNATE_ROUTE_TITLE_HEIGHT), this);
    public TnUiArgAdapter NULL_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULL_FIELD_WIDTH), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_Y), this);
    public TnUiArgAdapter BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_WIDTH), this);
    public TnUiArgAdapter MINIMIZE_DELAY_BUTTON_HPADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MINIMIZE_DELAY_BUTTON_HPADDING), this);
    public TnUiArgAdapter MINIMIZE_DELAY_BUTTON_VPADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MINIMIZE_DELAY_BUTTON_VPADDING), this);
    public TnUiArgAdapter AVOID_SEGMENT_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AVOID_SEGMENT_BUTTON_WIDTH), this);
    public TnUiArgAdapter INCIDENT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_INCIDENT_PADDING), this);
    public TnUiArgAdapter TRAFFIC_DETAIL_ICON_TEXT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_DETAIL_ICON_TEXT_PADDING), this);
    public TnUiArgAdapter INCIDENT_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_INCIDENT_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter SUGGEST_ROUTE_HEADER_ARC = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUGGEST_ROUTE_HEADER_ARC), this);
    public TnUiArgAdapter SUGGEST_ROUTE_HEADER_ADDR_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUGGEST_ROUTE_HEADER_ADDR_WIDTH), this);

    public TnUiArgAdapter MAP_LOGO_Y_WITH_BOTTOM_CONTAINER = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_LOGO_Y_WITH_BOTTOM_CONTAINER), this);
    
    public TnUiArgAdapter ROUTE_TRAFFIC_MAP_SUMMARY_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_MAP_SUMMARY_HEIGHT), this);
    
    public TnUiArgAdapter ROUTE_TRAFFIC_MAP_SUMMARY_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_MAP_SUMMARY_Y), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int value = ((Integer) args.getKey()).intValue();
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        switch (value)
        {
            case ID_TRAFFIC_ITEM_HEIGHT:
            {
                int maxSize = AppConfigHelper.getMaxDisplaySize();

                int size = maxSize * 104 / 1000;
                return PrimitiveTypeCache.valueOf(size);
            }
            case ID_MAP_LOGO_Y_WITH_BOTTOM_CONTAINER:
            {
                return PrimitiveTypeCache.valueOf(MAP_LOGO_Y.getInt() - BOTTOM_CONTAINER_HEIGHT.getInt());
            }
            case ID_TRAFFIC_ITEM_ICON_SIZE:
            {
                int size = AppConfigHelper.getMinDisplaySize() * 128 / 1000;
                return PrimitiveTypeCache.valueOf(size);
            }
            case ID_TRAFFIC_ITEM_SECOND_PART_PADDING:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 37 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TRAFFIC_TITLE_GAP:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 19 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_INFO_GAP:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 29 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_SUMMARY_CONTAINER_WIDTH:
            {
                int width = AppConfigHelper.getDisplayWidth() - SUMMARY_INFO_WIDTH.getInt();
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TRAFFIC_SUMMARY_CONTAINER_HEIGHT:
            {
                int height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                        - TAB_OUTER_CONTAINER_HEIGHT.getInt() - SUMMARY_INFO_HEIGHT.getInt() - SUMMARY_TITLE_HEIGHT.getInt()
                        - BOTTOM_BAR_REAL_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_SUMMARY_CONTAINER_HEIGHT_LANDSCAPE:
            {
                int height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                        - TAB_OUTER_CONTAINER_HEIGHT.getInt()- BOTTOM_BAR_REAL_HEIGHT.getInt()- SUMMARY_TITLE_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_DETAIL_MULTILINE_WIDTH:
            {
                int width;
                AbstractTnImage image;
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    image = ImageDecorator.IMG_TRAFFIC_COLOR_BAR.getImage();
                }
                else
                {
                    image = ImageDecorator.IMG_TRAFFIC_COLOR_BAR_LANDSCAPE.getImage();
                }
                int padding = (SCREEN_WIDTH.getInt() - image.getWidth()) / 2;
                if(padding < 0)
                {
                    width = AppConfigHelper.getDisplayWidth() * 80 / 100;
                }
                else
                {
                    width = image.getWidth() - ImageDecorator.IMG_INCIDENT_DEFAULT.getImage().getWidth() - TRAFFIC_DETAIL_ICON_TEXT_PADDING.getInt();
                }
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TRAFFIC_DETAIL_MULTILINE_HEIGHT:
            {
                int height = AppConfigHelper.getDisplayHeight() * 10 / 100;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_ALTERNATE_TITLE_LEFT_WIDTH:
            {
                int width = AppConfigHelper.getDisplayWidth() * 50 / 100;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TRAFFIC_ALTERNATE_TITLE_ADDRESS_BOX_WIDTH:
            {
                int width = AppConfigHelper.getDisplayWidth() * 40 / 100;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TAB_BUTTON_WIDTH:
            {
                int width = SCREEN_WIDTH.getInt() * 2632 / 10000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TAB_BUTTON_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 60 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TAB_OUTER_CONTAINER_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 90 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_SUMMARY_TITLE_WIDTH:
            {
                int width = AppConfigHelper.getDisplayWidth() - SUMMARY_INFO_WIDTH.getInt();
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_SUMMARY_TITLE_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 45 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_SUMMARY_INFO_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 116 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_SUMMARY_INFO_HEIGHT_LANDSCAPE:
            {
                int height = BOTTOM_BAR_Y_POS.getInt() - TAB_OUTER_CONTAINER_HEIGHT.getInt() + AppConfigHelper.getMaxDisplaySize() * 25 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_SUMMARY_INFO_WIDTH:
            {
                int width = AppConfigHelper.getMaxDisplaySize() * 3479 / 10000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_SUMMARY_INFO_H_PADDING:
            {
                int padding = AppConfigHelper.getMinDisplaySize() * 31 / 1000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_SUMMARY_INFO_V_PADDING:
            {
                int padding = AppConfigHelper.getMaxDisplaySize() * 83 / 10000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_SUMMARY_INFO_V_PADDING_LANDSCAPE:
            {
                int padding = AppConfigHelper.getDisplayHeight() * 293 / 10000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_SUMMARY_INFO_LABEL_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 52 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_ITEM_FIRST_PART_WIDTH:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 200 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TRAFFIC_ITEM_FIRST_PART_LEFT_PADDING:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 31 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_ALTERNATE_ROUTE_TITLE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 148 / 1000);
            }
            case ID_NULL_FIELD_WIDTH:
            {
                int width = AppConfigHelper.getDisplayWidth() * 313 / 10000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_BOTTOM_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 10);
            }
            case ID_BOTTOM_CONTAINER_Y:
            {
                int y = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - BOTTOM_CONTAINER_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(y);
            }
            case ID_BUTTON_WIDTH:
            {
                int width = AppConfigHelper.getDisplayWidth() * 4667 / 10000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_MINIMIZE_DELAY_BUTTON_HPADDING:
            {
                int hpadding = AppConfigHelper.getMinDisplaySize() * 47 / 1000;
                return PrimitiveTypeCache.valueOf(hpadding);
            }
            case ID_MINIMIZE_DELAY_BUTTON_VPADDING:
            {
                int hpadding = AppConfigHelper.getMaxDisplaySize() * 19 / 1000;
                return PrimitiveTypeCache.valueOf(hpadding);
            }
            case ID_AVOID_SEGMENT_BUTTON_WIDTH:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 625 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_INCIDENT_PADDING:
            {
                int padding = AppConfigHelper.getMaxDisplaySize() * 42 / 1000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_TRAFFIC_DETAIL_ICON_TEXT_PADDING:
            {
                int padding = AppConfigHelper.getMaxDisplaySize() * 19 / 1000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_SUGGEST_ROUTE_HEADER_ARC:
            {
                int padding = AppConfigHelper.getMinDisplaySize() * 188 / 10000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_SUGGEST_ROUTE_HEADER_ADDR_WIDTH:
            {
                int padding = AppConfigHelper.getDisplayWidth() * 4531 / 10000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_INCIDENT_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(SUMMARY_INFO_LABEL_HEIGHT.getInt() * 2);
            }
            case ID_TRAFFIC_MAP_SUMMARY_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - ALTERNATE_ROUTE_TITLE_HEIGHT.getInt() - BOTTOM_CONTAINER_HEIGHT.getInt());
            }
            case ID_TRAFFIC_MAP_SUMMARY_Y:
            {
                return PrimitiveTypeCache.valueOf(ALTERNATE_ROUTE_TITLE_HEIGHT.getInt());
            }
        }

        return null;
    }

}

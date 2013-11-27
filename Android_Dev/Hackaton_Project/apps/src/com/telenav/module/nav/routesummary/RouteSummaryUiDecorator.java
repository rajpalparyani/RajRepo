/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteSummaryUiDecorator.java
 *
 */
package com.telenav.module.nav.routesummary;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-4
 */
class RouteSummaryUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_TURN_ICON_SIZE = 1;
    private final static int ID_ROUTE_ITEM_HEIGHT = 4;
    private final static int ID_ROUTE_ITEM_DIST_SIZE = 5;
    private final static int ID_ROUTE_SUMMARY_CONTAINER_HEIGHT = 13;
    private final static int ID_TAB_BUTTON_WIDTH = 16;
    private final static int ID_TAB_BUTTON_HEIGHT = 17;
    private final static int ID_TAB_OUTER_CONTAINER_HEIGHT = 18;
    private final static int ID_ROUTE_TIME_INFO_HEIGHT = 19;
    private final static int ID_SUMMARY_TITLE_HEIGHT = 20;
    private final static int ID_TURN_AREA_WIDTH = 21;
    private final static int ID_ROUTE_ITEM_STREET_PADDING = 22;
    private final static int ID_ROUTE_ITEM_STREET_SIZE = 23;
    
    public TnUiArgAdapter TURN_ICON_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_ICON_SIZE), this);
    public TnUiArgAdapter ROUTE_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_HEIGHT), this);
    public TnUiArgAdapter ROUTE_ITEM_DIST_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_DIST_SIZE), this);
    public TnUiArgAdapter ROUTE_SUMMARY_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SUMMARY_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter TAB_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_BUTTON_WIDTH), this);
    public TnUiArgAdapter TAB_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_BUTTON_HEIGHT), this);
    public TnUiArgAdapter TAB_OUTER_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_OUTER_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter ROUTE_TIME_INFO_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_TIME_INFO_HEIGHT), this);
    public TnUiArgAdapter SUMMARY_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUMMARY_TITLE_HEIGHT), this);
    public TnUiArgAdapter TURN_AREA_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_AREA_WIDTH), this);
    public TnUiArgAdapter ROUTE_ITEM_STREET_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_STREET_PADDING), this);
    public TnUiArgAdapter ROUTE_ITEM_STREET_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_STREET_SIZE), this);
    
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_TURN_ICON_SIZE:
            {
                int width = AppConfigHelper.getMinDisplaySize();

                return PrimitiveTypeCache.valueOf(width * 13 / 100);
            }
            case ID_ROUTE_ITEM_HEIGHT:
            {
                int max_width = AppConfigHelper.getMaxDisplaySize();

                int size = max_width * 104 / 1000;
                return PrimitiveTypeCache.valueOf(size);
            }
            case ID_ROUTE_ITEM_DIST_SIZE:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 203 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_ROUTE_SUMMARY_CONTAINER_HEIGHT:
            {
                int height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - TAB_OUTER_CONTAINER_HEIGHT.getInt()
                        - ROUTE_TIME_INFO_HEIGHT.getInt() - SUMMARY_TITLE_HEIGHT.getInt() - BOTTOM_BAR_REAL_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(height);
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
            case ID_ROUTE_TIME_INFO_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 81 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_SUMMARY_TITLE_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 45 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TURN_AREA_WIDTH:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 187 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_ROUTE_ITEM_STREET_PADDING:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 43 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_ROUTE_ITEM_STREET_SIZE:
            {
                int width = SCREEN_WIDTH.getInt() - TURN_AREA_WIDTH.getInt() - ROUTE_ITEM_DIST_SIZE.getInt();
                return PrimitiveTypeCache.valueOf(width);
            }
        }
        return null;
    }

}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DashboardUiDecorator.java
 *
 */
package com.telenav.module.drivingshare;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2012-2-3
 */
public class DrivingShareUiDecorator extends AbstractCommonUiDecorator
{
    protected final static int ID_TOP_CONTAINER_WIDTH = 1; 
    
    protected final static int ID_DASHBOARD_LOCATION_WEATHER_HEIGHT = 2;
    
    protected final static int ID_SCOUT_WEATHER_ICON_WIDTH = 3;
    
    protected final static int ID_SCOUT_WEATHER_ICON_HEIGHT = 4;
    
    protected final static int ID_SCOUT_WEATHER_WIDTH = 5;
    
    protected final static int ID_SCOUT_ONEBOX_CONTAINER_HEIGHT = 7;
    
    protected final static int ID_SCOUT_ONEBOX_SEARCH_HEIGHT = 8;
    
    protected final static int ID_DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT = 9;
    
    protected final static int ID_SCOUT_HOME_WORK_LABEL_HEIGHT = 11;
    
    protected final static int ID_SCOUT_HOME_WORK_TIME_HEIGHT = 12;
    
    protected final static int ID_SCOUT_WEBVIEW_HEIGHT = 13;
    
    protected final static int ID_MAP_BOTTOM_SPLIT_LINE_HEIGHT = 14;
    
    protected final static int ID_CIRCLE_RADIUS = 15;
    
    protected final static int ID_SCOUT_VECTOR_MAP_X = 16;
    
    protected final static int ID_SCOUT_VECTOR_MAP_Y = 17;
    
    protected final static int ID_SCOUT_VECTOR_MAP_WIDTH = 18;
    
    protected final static int ID_SCOUT_VECTOR_MAP_HEIGHT = 19;

    protected final static int ID_SCOUT_CLOSABLE_BOTTON_WIDTH = 20;
    
    protected final static int ID_SCOUT_CLOSABLE_BOTTON_HEIGHT = 21;

    protected final static int ID_SCOUT_POI_HEIGHT = 23;
    
    protected final static int ID_DASHBOARD_HORIZONTAL_PADDING = 24;
    
    protected final static int ID_DASHBOARD_BADGE_GAP = 25;
    
    protected final static int ID_RESUME_BUTTON_LEFT_PADDING = 26;
    
    protected final static int ID_LOCATION_STREET_LABEL_HEIGHT = 27;
    
    protected final static int ID_FAVORITE_IMG_WIDTH = 28;
    
    protected final static int ID_FAVORITE_IMG_HEIGHT = 29;
    
    protected final static int ID_TOP_SEARCHBOX_WIDTH = 30;
    
    protected final static int ID_MYPROFILE_CONTAINER_HEIGHT = 31;
    
    protected final static int ID_DASHBOARD_LOCATION_TOP = 32;
    
    protected final static int ID_RESUME_TRIP_CONTAINER_HEIGHT = 33;
    
    
    public TnUiArgAdapter TOP_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOP_CONTAINER_WIDTH), this);

    public TnUiArgAdapter DASHBOARD_LOCATION_WEATHER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DASHBOARD_LOCATION_WEATHER_HEIGHT), this);

    public TnUiArgAdapter SCOUT_WEATHER_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_WEATHER_ICON_WIDTH), this);

    public TnUiArgAdapter SCOUT_WEATHER_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_WEATHER_ICON_HEIGHT), this);

    public TnUiArgAdapter SCOUT_WEATHER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_WEATHER_WIDTH), this);

    public TnUiArgAdapter SCOUT_ONEBOX_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_ONEBOX_CONTAINER_HEIGHT), this);

    public TnUiArgAdapter SCOUT_ONEBOX_SEARCH_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_ONEBOX_SEARCH_HEIGHT), this);

    public TnUiArgAdapter DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT), this);

    public TnUiArgAdapter SCOUT_HOME_WORK_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_HOME_WORK_LABEL_HEIGHT), this);

    public TnUiArgAdapter SCOUT_HOME_WORK_TIME_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_HOME_WORK_TIME_HEIGHT), this);

    public TnUiArgAdapter SCOUT_WEBVIEW_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_WEBVIEW_HEIGHT), this);

    public TnUiArgAdapter MAP_BOTTOM_SPLIT_LINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_BOTTOM_SPLIT_LINE_HEIGHT), this);
    
    public TnUiArgAdapter SCOUT_VECTOR_MAP_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_VECTOR_MAP_X), this);
    
    public TnUiArgAdapter SCOUT_VECTOR_MAP_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_VECTOR_MAP_Y), this);
    
    public TnUiArgAdapter SCOUT_VECTOR_MAP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_VECTOR_MAP_WIDTH), this);
    
    public TnUiArgAdapter SCOUT_VECTOR_MAP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_VECTOR_MAP_HEIGHT), this);
    
    public TnUiArgAdapter CIRCLE_RADIUS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CIRCLE_RADIUS), this);

    public TnUiArgAdapter SCOUT_CLOSABLE_BOTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_CLOSABLE_BOTTON_WIDTH), this);

    public TnUiArgAdapter SCOUT_CLOSABLE_BOTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_CLOSABLE_BOTTON_HEIGHT), this);

    public TnUiArgAdapter SCOUT_POI_POI_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCOUT_POI_HEIGHT), this);
    
    public TnUiArgAdapter DASHBOARD_HORIZONTAL_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DASHBOARD_HORIZONTAL_PADDING), this);
    
    public TnUiArgAdapter DASHBOARD_BADGE_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DASHBOARD_BADGE_GAP), this);
    
    public TnUiArgAdapter RESUME_BUTTON_LEFT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_RESUME_BUTTON_LEFT_PADDING), this);
 
    public TnUiArgAdapter LOCATION_STREET_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LOCATION_STREET_LABEL_HEIGHT), this);
    
    public TnUiArgAdapter FAVORITE_IMG_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_FAVORITE_IMG_WIDTH), this);
    
    public TnUiArgAdapter FAVORITE_IMG_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_FAVORITE_IMG_HEIGHT), this);
    
    public TnUiArgAdapter TOP_SEARCHBOX_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOP_SEARCHBOX_WIDTH), this);
    
    public TnUiArgAdapter DASHBOARD_LOCATION_TOP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DASHBOARD_LOCATION_TOP), this);
    
    public TnUiArgAdapter MYPROFILE_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MYPROFILE_CONTAINER_HEIGHT), this);
   
    public TnUiArgAdapter RESUME_TRIP_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_RESUME_TRIP_CONTAINER_HEIGHT), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_DASHBOARD_HORIZONTAL_PADDING:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 2 / 100);
            }
            case ID_TOP_CONTAINER_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - DASHBOARD_HORIZONTAL_PADDING.getInt() * 2);
            }
            case ID_DASHBOARD_LOCATION_WEATHER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1229 / 10000);
            }
            case ID_SCOUT_WEATHER_ICON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 1741 / 10000);
            }
            case ID_SCOUT_WEATHER_ICON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getScreenHeight() * 729 / 10000);
            }
            case ID_SCOUT_WEATHER_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 2000 / 10000);
            }
            case ID_SCOUT_ONEBOX_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 896 / 10000);
            }
            case ID_SCOUT_ONEBOX_SEARCH_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 479 / 10000);
            }
            case ID_DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT:
            {
                int height = Math.max(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DASHBOARD_LOCATION_CITY_LABEL).getHeight(), ImageDecorator.DASHBOARD_ARROW_ICON_FOCUSED.getImage().getHeight());
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(height + 2 * AppConfigHelper.getDisplayHeight() * 211 / 10000);

                }
                else
                {
                    return PrimitiveTypeCache.valueOf(height + 2 * AppConfigHelper.getDisplayHeight() * 234 / 10000);
                }
            }
            case ID_SCOUT_HOME_WORK_LABEL_HEIGHT:
            {
                int height = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DASHBOARD_HOME_WORK_TIME_TITLE).getHeight();
                return PrimitiveTypeCache.valueOf(height + 2);
            }
            case ID_SCOUT_HOME_WORK_TIME_HEIGHT:
            {
                int height = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DASHBOARD_HOME_WORK_TIME).getHeight();
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(height);

                }
                else
                {
                    return PrimitiveTypeCache.valueOf(height + 2 * AppConfigHelper.getDisplayHeight() * 102 / 10000);
                }
            }
            case ID_SCOUT_WEBVIEW_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1400 / 10000);
            }
            case ID_MAP_BOTTOM_SPLIT_LINE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(1);
            }
            case ID_CIRCLE_RADIUS:
            {
                return SCOUT_VECTOR_MAP_HEIGHT.getInt() / 2;
            }
            case ID_SCOUT_VECTOR_MAP_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_SCOUT_VECTOR_MAP_Y:
            {
                return PrimitiveTypeCache.valueOf(-1);
            }
            case ID_SCOUT_VECTOR_MAP_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt());
            }
            case ID_SCOUT_VECTOR_MAP_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 6000 / 10000);
            }
            case ID_SCOUT_CLOSABLE_BOTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(ImageDecorator.DASHBOARD_RESUME_TRIP_CLOSE_ICON_UNFOCUSED.getImage().getWidth());
            }
            case ID_SCOUT_CLOSABLE_BOTTON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(ImageDecorator.DASHBOARD_RESUME_TRIP_CLOSE_ICON_UNFOCUSED.getImage().getHeight());
            }
            case ID_SCOUT_POI_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1104 / 10000 );
            }
            case ID_DASHBOARD_BADGE_GAP:
            {
                int gap = AppConfigHelper.getMinDisplaySize() / 48;
                return PrimitiveTypeCache.valueOf(gap);
            }
            case ID_RESUME_BUTTON_LEFT_PADDING:
            {
                int padding = (SCREEN_WIDTH.getInt() - TOP_CONTAINER_WIDTH.getInt()) / 2 - 3;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_LOCATION_STREET_LABEL_HEIGHT:
            {
                //For we use the ascent part to anchor center, so we should add extra descent to calculate the height.
                AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DASHBOARD_LOCATION_STREET_LABEL);
                return PrimitiveTypeCache.valueOf(font.getHeight() + font.getDescent());
            }
            case ID_FAVORITE_IMG_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(ImageDecorator.DASHBOARD_FAVORITE_ICON_FOCUSED.getImage().getWidth());
            }
            case ID_FAVORITE_IMG_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(ImageDecorator.DASHBOARD_FAVORITE_ICON_FOCUSED.getImage().getHeight());
            }
            case ID_TOP_SEARCHBOX_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(TOP_CONTAINER_WIDTH.getInt() - DASHBOARD_HORIZONTAL_PADDING.getInt() -  FAVORITE_IMG_WIDTH.getInt());
            }
            case ID_MYPROFILE_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 130 / 1280);
            }
            case ID_DASHBOARD_LOCATION_TOP:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 750 / 10000);
            }
            case ID_RESUME_TRIP_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(SCOUT_HOME_WORK_TIME_HEIGHT.getInt() + 4*2);
            }    
        }
        return null;
    }

}

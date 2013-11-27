/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RoutePlanningUiDecorator.java
 *
 */
package com.telenav.module.nav.routeplanning;

import com.telenav.app.android.scout_us.R;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-2
 */
class RoutePlanningUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_CONFIRM_PANEL_HEIGHT = 1;

    private final static int ID_CONFIRM_PANEL_TOP_PADDING = 2;

    private final static int ID_CONFIRM_PANEL_H_PADDING = 3;

    private final static int ID_PLANNING_INFO_WIDTH = 4;
    
    private final static int ID_PLANNING_INFO_HEIGHT = 5;
    
    private final static int ID_PLANNING_INFO_X = 6;

    private final static int ID_PLANNING_INFO_Y = 7;
     
    private final static int ID_ROUTE_PLANNING_STATUS_X = 8;

    private final static int ID_ROUTE_PLANNING_STATUS_Y = 9;

    private final static int ID_ROUTE_PLANNING_STATUS_WIDTH = 10;

    private final static int ID_ROUTE_PLANNING_STATUS_HEIGHT = 11;

    private final static int ID_INFO_CONTAINER_WIDTH = 12;

    private final static int ID_SETTING_BUTTON_GAP = 13;
    
    private final static int ID_SETTING_TITLE_HEIGHT = 14;

    private final static int ID_SETTING_BUTTON_WIDTH = 15;

    private final static int ID_NAV_BUTTON_ICON_TEXT_GAP = 16;

    private final static int ID_CONFIRM_PANEL_BOTTOM_PADDING = 17;

    private final static int ID_CONFIRM_PANEL_ADDRESS_LABEL_WIDTH = 18;

    private final static int ID_CONFIRM_PANEL_X = 19;

    private final static int ID_CONFIRM_PANEL_Y = 20;

    private final static int ID_SETTING_SCREEN_ITEM_HEIGHT = 21;

    private final static int ID_SETTING_BUTTON_HEIGHT = 22;
    
    private final static int ID_CONFIRM_PANEL_PADDING_BETWEEN_PIN_AND_LABEL = 23;

    private final static int ID_SETTING_BOTTOM_GAP = 24;
    
    private final static int ID_ROUTE_CHOICE_MAP_WIDTH = 25;
    
    private final static int ID_ROUTE_CHOICE_MAP_HEIGHT = 26;
    
    private final static int ID_ROUTE_CHOICE_MAP_X = 27;
    
    private final static int ID_ROUTE_CHOICE_MAP_Y = 28;
    
    private final static int ID_ROUTE_CHOICE_MAP_Y_WITH_ROUTE = 29;
    private final static int ID_TURN_ICON_SIZE = 30;
    private final static int ID_ROUTE_ITEM_HEIGHT = 31;
    private final static int ID_ROUTE_ITEM_DIST_SIZE = 32;
    private final static int ID_ROUTE_SUMMARY_CONTAINER_HEIGHT = 33;

    private final static int ID_TURN_AREA_WIDTH = 34;
    private final static int ID_ROUTE_ITEM_STREET_PADDING = 35;
    private final static int ID_ROUTE_ITEM_STREET_SIZE = 36;
    private final static int ID_LABEL_TITLE_HEIGHT = 37;
    
    //ID
    private static final int ID_BOTTOM_HORIZONTAL_GAP = 38;
    private static final int ID_BOTTOM_CONTAINER_HEIGHT = 39;
    private static final int ID_BOTTOM_BUTTON_HEIGHT = 40;
    private static final int ID_BOTTOM_SHARE_ETA_WIDTH = 41;
    private static final int ID_BOTTOM_NAVIGATION_WIDTH_WITH_ETA = 42;
    private static final int ID_BOTTOM_NAVIGATION_WIDTH = 43;
    
    private static final int ID_ZERO_VALUE = 44;
    private static final int ID_BOTTOM_CONTAINER_X = 45;
    private static final int ID_BOTTOM_CONTAINER_Y = 46;
    
    private static final int ID_LABEL_TITLE_WIDTH = 47;
    private static final int ID_TITLE_LEFT_GAP = 48;
    private static final int ID_TITLE_RIGHT_GAP = 49;
    
    private final static int ID_ROUTE_MAP_LOGO_Y = 50;
    private final static int ID_BOTTOM_CONTAINER_WIDTH = 51;
    private final static int ID_ROUTE_SUMMARY_CONTAINER_WIDTH = 52;
    private final static int ID_ROUTE_SUMMARY_CONTAINER_X = 53;
    private final static int ID_ROUTE_SUMMARY_CONTAINER_Y = 54;
    private final static int ID_BOTTOM_CONTAINER_VERTICAL_GAP = 55;
    private final static int ID_ADDRESS_CONTAINER__WIDTH = 56;
    
    private final static int ID_TITLE_ICON_WIDTH = 57;
    private final static int ID_TITLE_ICON_HEIGHT = 58;
    
    public TnUiArgAdapter TURN_ICON_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_ICON_SIZE), this);
    public TnUiArgAdapter ROUTE_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_HEIGHT), this);
    public TnUiArgAdapter ROUTE_ITEM_DIST_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_DIST_SIZE), this);
    public TnUiArgAdapter ROUTE_SUMMARY_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SUMMARY_CONTAINER_WIDTH), this);
    public TnUiArgAdapter ROUTE_SUMMARY_CONTAINER_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SUMMARY_CONTAINER_X), this);
    public TnUiArgAdapter ROUTE_SUMMARY_CONTAINER_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SUMMARY_CONTAINER_Y), this);
    public TnUiArgAdapter TURN_AREA_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_AREA_WIDTH), this);
    public TnUiArgAdapter ROUTE_ITEM_STREET_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_STREET_PADDING), this);
    public TnUiArgAdapter ROUTE_ITEM_STREET_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_ITEM_STREET_SIZE), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_VERTICAL_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_VERTICAL_GAP), this);
    
    public TnUiArgAdapter CONFIRM_PANEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_HEIGHT), this);

    public TnUiArgAdapter CONFIRM_PANEL_TOP_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_TOP_PADDING), this);

    public TnUiArgAdapter CONFIRM_PANEL_BOTTOM_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_BOTTOM_PADDING), this);

    public TnUiArgAdapter CONFIRM_PANEL_H_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_H_PADDING), this);
    
    public TnUiArgAdapter PLANNING_INFO_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PLANNING_INFO_WIDTH), this);
    
    public TnUiArgAdapter PLANNING_INFO_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PLANNING_INFO_HEIGHT), this);
    
    
    public TnUiArgAdapter PLANNING_INFO_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PLANNING_INFO_X), this);
    
    public TnUiArgAdapter PLANNING_INFO_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PLANNING_INFO_Y), this);

    public TnUiArgAdapter ROUTE_PLANNING_STATUS_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_PLANNING_STATUS_X), this);

    public TnUiArgAdapter ROUTE_PLANNING_STATUS_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_PLANNING_STATUS_Y), this);

    public TnUiArgAdapter ROUTE_PLANNING_STATUS_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_PLANNING_STATUS_WIDTH), this);

    public TnUiArgAdapter ROUTE_PLANNING_STATUS_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_PLANNING_STATUS_HEIGHT), this);
    public TnUiArgAdapter INFO_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_INFO_CONTAINER_WIDTH), this);

    public TnUiArgAdapter SETTING_BUTTON_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SETTING_BUTTON_GAP), this);

    public TnUiArgAdapter SETTING_BOTTOM_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SETTING_BOTTOM_GAP), this);
    
    public TnUiArgAdapter SETTING_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SETTING_TITLE_HEIGHT), this);

    public TnUiArgAdapter SETTING_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SETTING_BUTTON_WIDTH), this);

    public TnUiArgAdapter NAV_BUTTON_ICON_TEXT_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_BUTTON_ICON_TEXT_GAP), this);

    public TnUiArgAdapter CONFIRM_PANEL_ADDRESS_LABEL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_ADDRESS_LABEL_WIDTH), this);

    public TnUiArgAdapter CONFIRM_PANEL_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_X), this);

    public TnUiArgAdapter CONFIRM_PANEL_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_Y), this);

    public TnUiArgAdapter SETTING_SCREEN_ITEM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SETTING_SCREEN_ITEM_HEIGHT), this);

    public TnUiArgAdapter SETTING_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SETTING_BUTTON_HEIGHT), this);
    
    public TnUiArgAdapter CONFIRM_PANEL_PADDING_BETWEEN_PIN_AND_LABEL = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONFIRM_PANEL_PADDING_BETWEEN_PIN_AND_LABEL), this);

    public TnUiArgAdapter ROUTE_CHOICE_MAP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_CHOICE_MAP_WIDTH), this);
    
    public TnUiArgAdapter ROUTE_CHOICE_MAP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_CHOICE_MAP_HEIGHT), this);
    
    public TnUiArgAdapter ROUTE_CHOICE_MAP_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_CHOICE_MAP_X), this);
    
    public TnUiArgAdapter ROUTE_CHOICE_MAP_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_CHOICE_MAP_Y), this);
    
    public TnUiArgAdapter ROUTE_CHOICE_MAP_Y_WITH_ROUTE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_CHOICE_MAP_Y_WITH_ROUTE), this);
    
    public TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);
    public TnUiArgAdapter LABEL_TITLE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_WIDTH), this);
    
    public TnUiArgAdapter BOTTOM_HORIZONTAL_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_HORIZONTAL_GAP), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter BOTTOM_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_BUTTON_HEIGHT), this);
    public TnUiArgAdapter BOTTOM_SHARE_ETA_WIDTH  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_SHARE_ETA_WIDTH), this);
    public TnUiArgAdapter BOTTOM_NAVIGATION_WIDTH_WITH_ETA  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_NAVIGATION_WIDTH_WITH_ETA), this);
    public TnUiArgAdapter BOTTOM_NAVIGATION_WIDTH  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_NAVIGATION_WIDTH), this);
    
    public TnUiArgAdapter ZERO_VALUE  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZERO_VALUE), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_X  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_X), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_Y  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_Y), this);
    
    public TnUiArgAdapter TITLE_LEFT_GAP  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_LEFT_GAP), this);

    public TnUiArgAdapter TITLE_RIGHT_GAP  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_RIGHT_GAP), this);
    
    public TnUiArgAdapter ROUTE_MAP_LOGO_Y  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_MAP_LOGO_Y), this);
    public TnUiArgAdapter BOTTOM_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_WIDTH), this);
    public TnUiArgAdapter ADDRESS_CONTAINER__WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ADDRESS_CONTAINER__WIDTH), this);
  
    public TnUiArgAdapter TITLE_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_ICON_WIDTH), this);
    public TnUiArgAdapter TITLE_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_ICON_HEIGHT), this);

    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        switch (key)
        {
            case ID_CONFIRM_PANEL_HEIGHT:
            {
                AbstractTnImage originPinImage = ImageDecorator.ORIGIN_ICON_SMALL_UNFOCUSED.getImage();
                AbstractTnImage destPinImage = ImageDecorator.DESTINATION_ICON_SMALL_UNFOCUSED.getImage();
                int height = originPinImage.getHeight() + destPinImage.getHeight() + CONFIRM_PANEL_TOP_PADDING.getInt() + CONFIRM_PANEL_BOTTOM_PADDING.getInt();
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_CONFIRM_PANEL_TOP_PADDING:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int vPadding = AppConfigHelper.getMaxDisplaySize() * 20 / 1000;
                    return PrimitiveTypeCache.valueOf(vPadding);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(10 * AppConfigHelper.getMinDisplaySize() /720);
                }
            }
            case ID_CONFIRM_PANEL_BOTTOM_PADDING:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int vPadding = AppConfigHelper.getMaxDisplaySize() * 10 / 1000;
                    return PrimitiveTypeCache.valueOf(vPadding);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            }
            case ID_CONFIRM_PANEL_H_PADDING:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int padding = (SCREEN_WIDTH.getInt() - 3*ImageDecorator.ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_FOCUSED.getImage().getWidth())>>1;
                    return PrimitiveTypeCache.valueOf(padding);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 177 / 10000);
                }
            }
            case ID_PLANNING_INFO_WIDTH:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                }
                else
                {
                    int imageLandscapeWidth = ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_LANDSCAPE_UNFOCUSED.getImage().getWidth();
                    return PrimitiveTypeCache.valueOf(imageLandscapeWidth + CONFIRM_PANEL_H_PADDING.getInt());
                }
            }
            case ID_PLANNING_INFO_HEIGHT: //dynamic to get
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - CONFIRM_PANEL_HEIGHT.getInt() - CONFIRM_PANEL_Y.getInt() - BOTTOM_CONTAINER_HEIGHT.getInt());
                }
            }
            case ID_PLANNING_INFO_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_PLANNING_INFO_Y:
            {
                int y = CONFIRM_PANEL_Y.getInt() + CONFIRM_PANEL_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(y);
            }
            case ID_ROUTE_PLANNING_STATUS_WIDTH:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(maxHeight * 266 / 1000);
            }
            case ID_ROUTE_PLANNING_STATUS_HEIGHT:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();

                return PrimitiveTypeCache.valueOf(maxHeight * 62 / 1000);
            }
            case ID_ROUTE_PLANNING_STATUS_X:
            {
                int x = (AppConfigHelper.getDisplayWidth() - ROUTE_PLANNING_STATUS_WIDTH.getInt()) / 2;
                if (orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
                {
                    x += PLANNING_INFO_WIDTH.getInt() / 2;
                }
                return PrimitiveTypeCache.valueOf(x);
            }
            case ID_ROUTE_PLANNING_STATUS_Y:
            {
                return PrimitiveTypeCache.valueOf(ROUTE_MAP_LOGO_Y.getInt() - ROUTE_PLANNING_STATUS_HEIGHT.getInt());
            }
            case ID_INFO_CONTAINER_WIDTH:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int width = SCREEN_WIDTH.getInt() - CONFIRM_PANEL_H_PADDING.getInt() * 2;
                    return PrimitiveTypeCache.valueOf(width);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(PLANNING_INFO_WIDTH.getInt() - CONFIRM_PANEL_H_PADDING.getInt() * 2);
                }
            }
            case ID_SETTING_BUTTON_GAP:
            {
                int height = 0;
                int blankHeight = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - SETTING_TITLE_HEIGHT.getInt()
                        - SETTING_SCREEN_ITEM_HEIGHT.getInt() * 4 - SETTING_BUTTON_HEIGHT.getInt();
                if (blankHeight < 0)
                {
                    blankHeight = 0;
                }
                int gap = AppConfigHelper.getMaxDisplaySize() * 52 / 1000;
                int minGap = AppConfigHelper.getMaxDisplaySize() * 20 /1000;         
                if (blankHeight <= minGap)
                {
                    height = gap;                
                }
                else if(blankHeight > minGap && blankHeight < 2 * minGap )
                {
                    height = minGap;
                }
                else if (blankHeight >= 2* minGap && blankHeight < 2 * gap)
                {
                    height = blankHeight/2;
                }
                else
                {
                    height = gap;   
                }
                return PrimitiveTypeCache.valueOf(height);
            }
            
            case ID_SETTING_BOTTOM_GAP:
            {
                int bottomHeight = 0;
                int blankHeight = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - SETTING_TITLE_HEIGHT.getInt()
                        - SETTING_SCREEN_ITEM_HEIGHT.getInt() * 4 - SETTING_BUTTON_HEIGHT.getInt();
                if (blankHeight < 0)
                {
                    blankHeight = 0;
                }
                int gap = AppConfigHelper.getMaxDisplaySize() * 52 / 1000;
                int minGap = AppConfigHelper.getMaxDisplaySize() * 20 /1000;         
                if (blankHeight <= minGap)
                {
                    bottomHeight = gap;                
                }
                else if(blankHeight > minGap && blankHeight < 2 * minGap )
                {
                    bottomHeight = minGap;
                }                
                return PrimitiveTypeCache.valueOf(bottomHeight);
            }
            
            case ID_SETTING_TITLE_HEIGHT:
            {
                if (AppConfigHelper.isTabletSize())
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / 10000);
                }
                else
                {
                    int height = AppConfigHelper.getMaxDisplaySize() * 85 / 1000;
                    return PrimitiveTypeCache.valueOf(height);
                }
            }
            case ID_SETTING_BUTTON_WIDTH:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 418 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_NAV_BUTTON_ICON_TEXT_GAP:
            {
                int minHeight = AppConfigHelper.getMinDisplaySize();
                int gap = minHeight * 25 / 1000;
                return PrimitiveTypeCache.valueOf(gap);
            }
            case ID_CONFIRM_PANEL_ADDRESS_LABEL_WIDTH:
            {
                int width = INFO_CONTAINER_WIDTH.getInt();
                AbstractTnImage originPin = ImageDecorator.ORIGIN_ICON_SMALL_UNFOCUSED.getImage();
                if(originPin != null)
                {
                    width -= (originPin.getWidth() + CONFIRM_PANEL_PADDING_BETWEEN_PIN_AND_LABEL.getInt());
                }
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_CONFIRM_PANEL_X:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                 return PrimitiveTypeCache.valueOf(0);
                }
                else
                {
                     return PrimitiveTypeCache.valueOf(0);
                }
            }
            case ID_CONFIRM_PANEL_Y:
            {
                return PrimitiveTypeCache.valueOf(LABEL_TITLE_HEIGHT.getInt());
            }
            case ID_SETTING_SCREEN_ITEM_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 104 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_SETTING_BUTTON_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 69 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_CONFIRM_PANEL_PADDING_BETWEEN_PIN_AND_LABEL:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int padding = AppConfigHelper.getMinDisplaySize() * 14 / 1000;
                    return PrimitiveTypeCache.valueOf(padding);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth()  * 125 / 10000);
                }
            }
            case ID_ROUTE_CHOICE_MAP_WIDTH:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - BOTTOM_CONTAINER_WIDTH.getInt());
                }
            }
            case ID_ROUTE_CHOICE_MAP_HEIGHT:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - CONFIRM_PANEL_HEIGHT.getInt() - CONFIRM_PANEL_Y.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - LABEL_TITLE_HEIGHT.getInt());
                }
            }
            case ID_ROUTE_CHOICE_MAP_X:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(BOTTOM_CONTAINER_WIDTH.getInt());
                }
            }
            case ID_ROUTE_CHOICE_MAP_Y:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(CONFIRM_PANEL_Y.getInt() + CONFIRM_PANEL_HEIGHT.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(LABEL_TITLE_HEIGHT.getInt());
                }
            }
            case ID_ROUTE_CHOICE_MAP_Y_WITH_ROUTE:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(CONFIRM_PANEL_Y.getInt() + CONFIRM_PANEL_HEIGHT.getInt() + PLANNING_INFO_HEIGHT.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(LABEL_TITLE_HEIGHT.getInt());
                }
            }
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
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(0);
                    
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - LABEL_TITLE_HEIGHT.getInt());
                   
                }
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
                        return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(38));
                    }
                }
            }
            case ID_BOTTOM_HORIZONTAL_GAP:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(22*AppConfigHelper.getDisplayWidth()/720);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            }
            case ID_BOTTOM_CONTAINER_HEIGHT:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf((int)AndroidCitizenUiHelper.getResources().getDimension(R.dimen.commonBottom0ContainerPortraitLayoutHeight));
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(142* AppConfigHelper.getMinDisplaySize() /720);
                }
            }
            case ID_BOTTOM_BUTTON_HEIGHT:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(BOTTOM_CONTAINER_HEIGHT.getInt()-AndroidCitizenUiHelper.getPixelsByDip(18));
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(102 * AppConfigHelper.getMinDisplaySize() /720);
                }              
            }
            case ID_BOTTOM_CONTAINER_VERTICAL_GAP:
            {
                return PrimitiveTypeCache.valueOf(20 * AppConfigHelper.getMinDisplaySize() /720);
            }
            case ID_BOTTOM_SHARE_ETA_WIDTH:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(200 * AppConfigHelper.getMinDisplaySize() /720);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(189 * AppConfigHelper.getMinDisplaySize() /720);
                }
            }
            case ID_BOTTOM_NAVIGATION_WIDTH:
            {
                return BOTTOM_NAVIGATION_WIDTH_WITH_ETA.getInt() + BOTTOM_SHARE_ETA_WIDTH.getInt();
            }
            case ID_BOTTOM_NAVIGATION_WIDTH_WITH_ETA:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(476 * AppConfigHelper.getMinDisplaySize() /720);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(BOTTOM_CONTAINER_WIDTH.getInt() - 2*CONFIRM_PANEL_H_PADDING.getInt() - BOTTOM_SHARE_ETA_WIDTH.getInt());
                }
            }
            case ID_ZERO_VALUE:
            {
                return  PrimitiveTypeCache.valueOf(0);
            }
            case ID_BOTTOM_CONTAINER_X:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(22 * AppConfigHelper.getMinDisplaySize() /720);
                }
            }
            case ID_BOTTOM_CONTAINER_Y:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - BOTTOM_CONTAINER_HEIGHT.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - BOTTOM_CONTAINER_HEIGHT.getInt());
                }
            }
            case ID_LABEL_TITLE_WIDTH:
            {
                String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ROUTE_PLANNING, IStringNav.FAMILY_NAV);
                AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE);
                return PrimitiveTypeCache.valueOf(font.stringWidth(title) + 16);
            }
            case ID_TITLE_LEFT_GAP:
            {
               return PrimitiveTypeCache.valueOf((AppConfigHelper.getDisplayWidth() - LABEL_TITLE_WIDTH.getInt())/2);
            }
            case ID_TITLE_RIGHT_GAP:
            {
               return PrimitiveTypeCache.valueOf(TITLE_LEFT_GAP.getInt() - 20 * AppConfigHelper.getMinDisplaySize() /720 - TITLE_BUTTON_WIDTH.getInt());
            }
            case ID_ROUTE_MAP_LOGO_Y:
            {
                int padding = 0;
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    padding = AppConfigHelper.getDisplayHeight() * 73 / 10000;
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - BOTTOM_CONTAINER_HEIGHT.getInt()  - AppConfigHelper.getStatusBarHeight() - ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage().getHeight() - padding);
                }
                else
                {
                    padding = AppConfigHelper.getDisplayHeight() * 37 / 10000;
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage().getHeight() - padding);
                }
            }
            case ID_BOTTOM_CONTAINER_WIDTH:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(PLANNING_INFO_WIDTH.getInt());
                }
            }
            case ID_ROUTE_SUMMARY_CONTAINER_WIDTH:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt() - BOTTOM_CONTAINER_WIDTH.getInt());
                }
            }
            case ID_ROUTE_SUMMARY_CONTAINER_X:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(CONFIRM_PANEL_X.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(BOTTOM_CONTAINER_WIDTH.getInt());
                }
            }
            case ID_ROUTE_SUMMARY_CONTAINER_Y:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt() - BOTTOM_CONTAINER_WIDTH.getInt());
                }
            }
            case ID_ADDRESS_CONTAINER__WIDTH:
            {
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(SCREEN_WIDTH.getInt() - 2*CONFIRM_PANEL_H_PADDING.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(PLANNING_INFO_WIDTH.getInt() - 2*CONFIRM_PANEL_H_PADDING.getInt());
                }
            }
            case ID_TITLE_ICON_WIDTH:
            {
               return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(43));
            }
            case ID_TITLE_ICON_HEIGHT:
            {
               return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(32));
            }
        }
        return null;
    }
}


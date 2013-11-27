/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractCommonUiDecorator.java
 *
 */
package com.telenav.mvc;

import com.telenav.app.android.scout_us.R;
import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 16, 2010
 */
public abstract class AbstractCommonUiDecorator implements ITnUiArgsDecorator
{
    private final static int ID_SCREEN_WIDTH = -1;

    private static final int ID_SCREEN_HEIGHT = -2;

    private static final int ID_MIC_ICON_HEIGHT = -3;

    private static final int ID_BOTTOM_BAR_HEIGHT = -4;

    private static final int ID_BOTTOM_BAR_REAL_HEIGHT = -5;
    
    private static final int ID_BOTTOM_BAR_Y_POS = -6;
    
    private static final int ID_SCREEN_SPLIT_LINE_WIDTH = -7;
    
    private static final int ID_SCREEN_SPLIT_LINE_HEIGHT = -8;
    
    private static final int ID_TEXT_FIELD_PADDING = -9;
    
    private static final int ID_MAP_LOGO_Y = -10;
    
    private static final int ID_MAP_LOGO_Y_WITH_BOTTOM_BAR = -11;
    
    private static final int ID_MAP_BUTTON_BOTTOM_GAP = -12;
    
    private static final int ID_NOTIFICATION_STATUS_X = -13;
    
    private static final int ID_NOTIFICATION_STATUS_Y = -14;
    
    private static final int ID_NOTIFICATION_STATUS_WIDTH = -15;
    
    private static final int ID_NOTIFICATION_STATUS_HEIGHT = -16;
    
    private static final int ID_TITLE_BUTTON_WIDTH = -17;
    
    private static final int ID_TITLE_BUTTON_HEIGHT = -18;
    
    private static final int ID_COMMON_TITLE_HEIGHT = -19;
    
    private static final int ID_COMMON_ONEBOX_HEIGHT = -20;
    
    private static final int ID_TITLE_NULL_FIELD_GAP = -21;

    private static final int ID_ROUTE_SETTING_TOP_AREA_TOP_PADDING = -22;

    private static final int ID_ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING = -23;

    private static final int ID_ROUTE_SETTING_TITLE_STYLE_GAP = -24;

    private static final int ID_ROUTE_SETTING_TITLE_CONTAINER_HEIGHT = -25;

    private static final int ID_ROUTE_SETTING_TITLE_CONTAINER_WITH_NULL_FIELD_HEIGHT = -26;
    
    private static final int ID_PREFERENCE_POP_UP_HEIGHT = -27;
    
    
    public TnUiArgAdapter SCREEN_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCREEN_WIDTH), this);
    
    public TnUiArgAdapter SCREEN_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCREEN_HEIGHT), this);
    
    public TnUiArgAdapter MIC_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MIC_ICON_HEIGHT), this);
    
    public TnUiArgAdapter BOTTOM_BAR_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_BAR_HEIGHT), this);
    
    public TnUiArgAdapter BOTTOM_BAR_REAL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_BAR_REAL_HEIGHT), this);
    
    public TnUiArgAdapter BOTTOM_BAR_Y_POS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_BAR_Y_POS), this);
    
    public TnUiArgAdapter SCREEN_SPLIT_LINE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCREEN_SPLIT_LINE_WIDTH), this);
    
    public TnUiArgAdapter SCREEN_SPLIT_LINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SCREEN_SPLIT_LINE_HEIGHT), this);
    
    public TnUiArgAdapter TEXT_FIELD_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXT_FIELD_PADDING), this);
    
    public TnUiArgAdapter MAP_LOGO_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_LOGO_Y), this);
    
    public TnUiArgAdapter MAP_LOGO_Y_WITH_BOTTOM_BAR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_LOGO_Y_WITH_BOTTOM_BAR), this);
    
    public TnUiArgAdapter MAP_BUTTON_BOTTOM_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_BUTTON_BOTTOM_GAP), this);
    
    public TnUiArgAdapter NOTIFICATION_STATUS_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTIFICATION_STATUS_X), this);
    
    public TnUiArgAdapter NOTIFICATION_STATUS_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTIFICATION_STATUS_Y), this);
    
    public TnUiArgAdapter NOTIFICATION_STATUS_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTIFICATION_STATUS_WIDTH), this);
    
    public TnUiArgAdapter NOTIFICATION_STATUS_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTIFICATION_STATUS_HEIGHT), this);
    
    public TnUiArgAdapter TITLE_BUTTON_WIDTH  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter TITLE_BUTTON_HEIGHT  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_BUTTON_HEIGHT), this);
    
    public TnUiArgAdapter COMMON_TITLE_BUTTON_HEIGHT  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_COMMON_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter COMMON_ONEBOX_HEIGHT  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_COMMON_ONEBOX_HEIGHT), this);
    
    public TnUiArgAdapter TITLE_NULL_FIELD_GAP  = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_NULL_FIELD_GAP), this);
    
    public TnUiArgAdapter ROUTE_SETTING_TOP_AREA_TOP_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_TOP_AREA_TOP_PADDING), this);
    
    public TnUiArgAdapter ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING), this);
   
    public TnUiArgAdapter ROUTE_SETTING_TITLE_STYLE_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_TITLE_STYLE_GAP), this);
    
    public TnUiArgAdapter ROUTE_SETTING_TITLE_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_TITLE_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter ROUTE_SETTING_TITLE_CONTAINER_WITH_NULL_FIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_TITLE_CONTAINER_WITH_NULL_FIELD_HEIGHT), this);
    
    public TnUiArgAdapter PREFERENCE_POP_UP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PREFERENCE_POP_UP_HEIGHT), this);
    
    
    public Object decorate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_SCREEN_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_MIC_ICON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(BOTTOM_BAR_HEIGHT.getInt() * 93 / 100);
            }
            case ID_SCREEN_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight());
            }
            case ID_BOTTOM_BAR_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf((int) (ImageDecorator.IMG_BOTTOM_BAR_DRIVE_FOCUS.getImage().getHeight() * 2.24));
            }
            case ID_BOTTOM_BAR_REAL_HEIGHT:
            {
                // The original image is 52 pixels height with 8 pixels transparent
                return BOTTOM_BAR_HEIGHT.getInt() - BOTTOM_BAR_HEIGHT.getInt() * 8 / 52;
                // All 96, transparent 9
                // 40*2.24 real 8
            }
            case ID_BOTTOM_BAR_Y_POS:
            {
                int height = AppConfigHelper.getDisplayHeight();
                int y = height - AppConfigHelper.getStatusBarHeight() - BOTTOM_BAR_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(y);
            }
            case ID_SCREEN_SPLIT_LINE_WIDTH:
            {
                return SCREEN_WIDTH.getInt();
            }
            case ID_SCREEN_SPLIT_LINE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(225 * AppConfigHelper.getDisplayHeight() / 10000);
            }
            case ID_TEXT_FIELD_PADDING:
            {
                return AppConfigHelper.getMinDisplaySize() * 100 / 6000;
            }
            case ID_MAP_LOGO_Y:
            {
                int padding = 0;
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    padding = AppConfigHelper.getDisplayHeight() * 73 / 10000;
                }
                else
                {
                    padding = AppConfigHelper.getDisplayHeight() * 37 / 10000;
                }
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage().getHeight() - padding);
            }
            case ID_MAP_LOGO_Y_WITH_BOTTOM_BAR:
            {
                return PrimitiveTypeCache.valueOf(MAP_LOGO_Y.getInt() - BOTTOM_BAR_REAL_HEIGHT.getInt());
            }
            case ID_MAP_BUTTON_BOTTOM_GAP:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 104 / 10000);
            }
            case ID_NOTIFICATION_STATUS_WIDTH:
            {
                int min = AppConfigHelper.getMinDisplaySize();
                return PrimitiveTypeCache.valueOf(min * 900 / 1000);
            }
            case ID_NOTIFICATION_STATUS_HEIGHT:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(maxHeight * 62 / 1000);
            }
            case ID_NOTIFICATION_STATUS_X:
            {
                int screenWidth = AppConfigHelper.getDisplayWidth();
                int x = (screenWidth - NOTIFICATION_STATUS_WIDTH.getInt()) / 2;
                return PrimitiveTypeCache.valueOf(x);
            }
            case ID_NOTIFICATION_STATUS_Y:
            {
                int screenHeight = AppConfigHelper.getDisplayHeight();
                int y = (screenHeight - NOTIFICATION_STATUS_HEIGHT.getInt() - 15);
                return PrimitiveTypeCache.valueOf(y);
            }
            case ID_TITLE_BUTTON_WIDTH:
            {
               return PrimitiveTypeCache.valueOf(85 * AppConfigHelper.getMinDisplaySize() /720);
            }
            case ID_TITLE_BUTTON_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(64 * AppConfigHelper.getMinDisplaySize() /720);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(58 * AppConfigHelper.getMinDisplaySize() /720);
                }
            }
            case ID_COMMON_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                 
                    return PrimitiveTypeCache.valueOf((int)AndroidCitizenUiHelper.getResources().getDimension(R.dimen.commonTitle0ContainerLayoutHeight));
                }
                else
                {
                    return PrimitiveTypeCache.valueOf((int)AndroidCitizenUiHelper.getResources().getDimension(R.dimen.commonTitle0ContainerLayoutHorizontalHeight));
                }
            }
            case ID_COMMON_ONEBOX_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(PrimitiveTypeCache.valueOf((int)AndroidCitizenUiHelper.getResources().getDimension(R.dimen.commonTitle0ContainerLayoutHeight)));
            }
            case ID_TITLE_NULL_FIELD_GAP:
            {
                int gap = 8 * AppConfigHelper.getMaxDisplaySize() / 480;
                return PrimitiveTypeCache.valueOf(gap);
            }
            case ID_ROUTE_SETTING_TOP_AREA_TOP_PADDING:
            {
                return PrimitiveTypeCache.valueOf(ROUTE_SETTING_TITLE_STYLE_GAP.getInt() * 2 - TITLE_NULL_FIELD_GAP.getInt());
            }
            case ID_ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING:
            {
                return PrimitiveTypeCache.valueOf(ROUTE_SETTING_TITLE_STYLE_GAP.getInt());
            }
            case ID_ROUTE_SETTING_TITLE_STYLE_GAP://The same as CitizenMessageBox line 110
            {
                int gap = 10 * AppConfigHelper.getMaxDisplaySize() / 480;
                return PrimitiveTypeCache.valueOf(gap);
            }
            case ID_ROUTE_SETTING_TITLE_CONTAINER_HEIGHT:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();
                int gapHeight = maxHeight * 1 / 100;
                int fontHeight = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE).getHeight();
                return PrimitiveTypeCache.valueOf(gapHeight*2 + fontHeight);
            }
            case ID_ROUTE_SETTING_TITLE_CONTAINER_WITH_NULL_FIELD_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(ROUTE_SETTING_TITLE_CONTAINER_HEIGHT.getInt() + TITLE_NULL_FIELD_GAP.getInt());
            }
            case ID_PREFERENCE_POP_UP_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(ROUTE_SETTING_TITLE_CONTAINER_WITH_NULL_FIELD_HEIGHT.getInt() + ROUTE_SETTING_TOP_AREA_TOP_PADDING.getInt() + ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING.getInt());
            }
            default:
            {
                return decorateDelegate(args);
            }
        }
    }


    
    protected abstract Object decorateDelegate(TnUiArgAdapter args);
}

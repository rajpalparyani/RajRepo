/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MovingMapUiDecorator.java
 *
 */
package com.telenav.module.nav.movingmap;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.ISpecialImageRes;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author yning (yning@telenav.cn)
 * @date 2010-11-5
 */
class MovingMapUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_NAV_TITLE_WIDTH = 1;

    private final static int ID_NAV_TITLE_HEIGHT = 2;

    private final static int ID_NAV_TITLE_LEFT_PADDING = 3;

    private final static int ID_TURN_ICON_WIDTH = 4;

    private final static int ID_TURN_ICON_HEIGHT = 5;

    private final static int ID_NAV_BUTTON_HORIZONTAL_PADDING = 6;

    private static final int ID_ZOOM_OUT_X = 7;

    private static final int ID_ZOOM_OUT_Y = 8;

    private static final int ID_ZOOM_OUT_WIDTH = 9;

    private static final int ID_ZOOM_OUT_HEIGHT = 10;

    private static final int ID_ZOOM_IN_X = 11;

    private static final int ID_ZOOM_IN_Y = 12;

    private static final int ID_ZOOM_IN_WIDTH = 13;

    private static final int ID_ZOOM_IN_HEIGHT = 14;

    private static final int ID_BACK_TO_NAV_BUTTON_X = 15;

    private static final int ID_BACK_TO_NAV_BUTTON_Y = 16;

    private static final int ID_BACK_TO_NAV_BUTTON_WIDTH = 17;

    private static final int ID_BACK_TO_NAV_BUTTON_HEIGHT = 18;

    private static final int ID_TRAFFIC_BUTTON_X = 19;

    private static final int ID_TRAFFIC_BUTTON_Y = 20;

    private static final int ID_TRAFFIC_BUTTON_WIDTH = 21;

    private static final int ID_TRAFFIC_BUTTON_HEIGHT = 22;

    private static final int ID_STREET_NAME_X = 23;

    private static final int ID_STREET_NAME_Y = 24;

    private static final int ID_STREET_NAME_WIDTH = 25;

    private static final int ID_STREET_NAME_HEIGHT = 26;

    private static final int ID_SPEED_LIMIT_X = 27;

    private static final int ID_SPEED_LIMIT_Y = 28;

    private static final int ID_SPEED_LIMIT_WIDTH = 29;

    private static final int ID_SPEED_LIMIT_HEIGHT = 30;

    private static final int ID_COMPASS_X = 31;

    private static final int ID_COMPASS_Y = 32;

    private static final int ID_COMPASS_WIDTH = 33;

    private static final int ID_COMPASS_HEIGHT = 34;

    private final static int ID_AD_X = 35;

    private final static int ID_AD_Y = 36;

    private final static int ID_AD_HEIGHT = 37;

    private final static int ID_AD_Y_EXT = 38;

    private final static int ID_AD_HEIGHT_EXT = 39;

    private static final int ID_END_TRIP_FAVORITE_NOTIFICATION_WIDTH = 40;

    private static final int ID_END_TRIP_FAVORITE_NOTIFICATION_X = 41;

    private static final int ID_END_TRIP_FAVORITE_NOTIFICATION_TEXT_WIDTH = 42;

    private static final int ID_END_TRIP_FAVORITE_NOTIFICATION_PADDING = 43;

    private static final int ID_TRAFFIC_ALERT_POPUP_SHADOW_PADDING = 44;

    private static final int ID_TRAFFIC_ALERT_TITLE_HEIGHT = 45;

    private final static int ID_TRAFFIC_ALERT_DETAIL_TOP_HEIGHT = 47;

    private final static int ID_TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT = 48;

    private final static int ID_TRAFFIC_ALERT_DETAIL_SPACE_BELOW_SPLIT_LINE = 49;

    private final static int ID_NAV_END_TRIP_X = 50;

    private final static int ID_NAV_END_TRIP_Y = 51;

    private final static int ID_NAV_END_TRIP_HEIGHT = 52;

    private final static int ID_NAV_END_TRIP_LEFT_WIDTH = 53;

    private static final int ID_POPUP_SPLIT_LINE_HEIGHT = 54;

    private static final int ID_MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR = 55;

    private static final int ID_AD_INITIAL_HEIGHT_WITHOUT_EAR = 56;

    private static final int ID_AD_FULL_HEIGHT_WITHOUT_EAR = 57;
    
    private static final int ID_NEXT_NEXT_TURN_WIDTH = 58;
    
    private static final int ID_NEXT_NEXT_TURN_HEIGHT = 59;
    
    private static final int ID_NEXT_NEXT_TURN_X = 60;
    
    private static final int ID_NEXT_NEXT_TURN_Y = 61;
    
    private static final int ID_END_TRIP_HORIZONTAL_PADDING = 62;

    public TnUiArgAdapter NAV_TITLE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_TITLE_WIDTH), this);

    public TnUiArgAdapter NAV_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_TITLE_HEIGHT), this);

    public TnUiArgAdapter NAV_TITLE_LEFT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_TITLE_LEFT_PADDING),
            this);

    public TnUiArgAdapter TURN_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_ICON_WIDTH), this);

    public TnUiArgAdapter TURN_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_ICON_HEIGHT), this);

    public TnUiArgAdapter ZOOM_OUT_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_OUT_X), this);

    public TnUiArgAdapter ZOOM_OUT_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_OUT_Y), this);

    public TnUiArgAdapter ZOOM_OUT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_OUT_WIDTH), this);

    public TnUiArgAdapter ZOOM_OUT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_OUT_HEIGHT), this);

    public TnUiArgAdapter ZOOM_IN_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_X), this);

    public TnUiArgAdapter ZOOM_IN_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_Y), this);

    public TnUiArgAdapter ZOOM_IN_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_WIDTH), this);

    public TnUiArgAdapter ZOOM_IN_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_HEIGHT), this);

    public TnUiArgAdapter BACK_TO_NAV_BUTTON_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BACK_TO_NAV_BUTTON_X), this);

    public TnUiArgAdapter BACK_TO_NAV_BUTTON_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BACK_TO_NAV_BUTTON_Y), this);

    public TnUiArgAdapter BACK_TO_NAV_BUTTON_WIDTH = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_BACK_TO_NAV_BUTTON_WIDTH), this);

    public TnUiArgAdapter BACK_TO_NAV_BUTTON_HEIGHT = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_BACK_TO_NAV_BUTTON_HEIGHT), this);

    public TnUiArgAdapter TRAFFIC_BUTTON_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_BUTTON_X), this);

    public TnUiArgAdapter TRAFFIC_BUTTON_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_BUTTON_Y), this);

    public TnUiArgAdapter TRAFFIC_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_BUTTON_WIDTH), this);

    public TnUiArgAdapter TRAFFIC_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_BUTTON_HEIGHT), this);

    public TnUiArgAdapter STREET_NAME_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STREET_NAME_X), this);

    public TnUiArgAdapter STREET_NAME_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STREET_NAME_Y), this);

    public TnUiArgAdapter STREET_NAME_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STREET_NAME_WIDTH), this);

    public TnUiArgAdapter STREET_NAME_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STREET_NAME_HEIGHT), this);

    public TnUiArgAdapter SPEED_LIMIT_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SPEED_LIMIT_X), this);

    public TnUiArgAdapter SPEED_LIMIT_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SPEED_LIMIT_Y), this);

    public TnUiArgAdapter SPEED_LIMIT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SPEED_LIMIT_WIDTH), this);

    public TnUiArgAdapter SPEED_LIMIT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SPEED_LIMIT_HEIGHT), this);

    public TnUiArgAdapter COMPASS_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_COMPASS_X), this);

    public TnUiArgAdapter COMPASS_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_COMPASS_Y), this);

    public TnUiArgAdapter COMPASS_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_COMPASS_WIDTH), this);

    public TnUiArgAdapter COMPASS_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_COMPASS_HEIGHT), this);

    public TnUiArgAdapter END_TRIP_FAVORITE_NOTIFICATION_WIDTH = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_END_TRIP_FAVORITE_NOTIFICATION_WIDTH), this);

    public TnUiArgAdapter END_TRIP_FAVORITE_NOTIFICATION_X = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_END_TRIP_FAVORITE_NOTIFICATION_X), this);

    public TnUiArgAdapter END_TRIP_FAVORITE_NOTIFICATION_TEXT_WIDTH = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_END_TRIP_FAVORITE_NOTIFICATION_TEXT_WIDTH), this);

    public TnUiArgAdapter END_TRIP_FAVORITE_NOTIFICATION_PADDING = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_END_TRIP_FAVORITE_NOTIFICATION_PADDING), this);

    public TnUiArgAdapter AD_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AD_X), this);

    public TnUiArgAdapter AD_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AD_Y), this);

    public TnUiArgAdapter AD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AD_HEIGHT), this);

    public TnUiArgAdapter AD_Y_EXT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AD_Y_EXT), this);

    public TnUiArgAdapter AD_HEIGHT_EXT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AD_HEIGHT_EXT), this);

    public TnUiArgAdapter TRAFFIC_ALERT_POPUP_SHADOW_PADDING = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALERT_POPUP_SHADOW_PADDING), this);

    public TnUiArgAdapter TRAFFIC_ALERT_TITEL_HEIGHT = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALERT_TITLE_HEIGHT), this);

    public TnUiArgAdapter TRAFFIC_ALERT_DETAIL_TOP_HEIGHT = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALERT_DETAIL_TOP_HEIGHT), this);

    public TnUiArgAdapter TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT), this);

    public TnUiArgAdapter TRAFFIC_ALERT_DETAIL_SPACE_BELOW_SPLIT_LINE = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALERT_DETAIL_SPACE_BELOW_SPLIT_LINE), this);

    public TnUiArgAdapter NAV_END_TRIP_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_END_TRIP_X), this);

    public TnUiArgAdapter NAV_END_TRIP_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_END_TRIP_Y), this);

    public TnUiArgAdapter NAV_END_TRIP_LEFT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_END_TRIP_LEFT_WIDTH),
            this);

    public TnUiArgAdapter NAV_END_TRIP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NAV_END_TRIP_HEIGHT), this);

    public TnUiArgAdapter POPUP_SPLIT_LINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_SPLIT_LINE_HEIGHT),
            this);

    public TnUiArgAdapter NAV_BUTTON_HORIZONTAL_PADDING = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_NAV_BUTTON_HORIZONTAL_PADDING), this);

    public TnUiArgAdapter MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR), this);

    public TnUiArgAdapter AD_INITIAL_HEIGHT_WITHOUT_EAR = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_AD_INITIAL_HEIGHT_WITHOUT_EAR), this);

    public TnUiArgAdapter AD_FULL_HEIGHT_WITHOUT_EAR = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_AD_FULL_HEIGHT_WITHOUT_EAR), this);

    public TnUiArgAdapter NEXT_NEXT_TURN_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NEXT_NEXT_TURN_WIDTH), this);

    public TnUiArgAdapter NEXT_NEXT_TURN_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NEXT_NEXT_TURN_HEIGHT), this);
    
    public TnUiArgAdapter NEXT_NEXT_TURN_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NEXT_NEXT_TURN_X), this);
    public TnUiArgAdapter NEXT_NEXT_TURN_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NEXT_NEXT_TURN_Y), this);
    
    public TnUiArgAdapter END_TRIP_HORIZONTAL_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_END_TRIP_HORIZONTAL_PADDING), this);

    public static final int SCALE = 1000;

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_NAV_TITLE_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_NAV_TITLE_HEIGHT:
            {
                boolean isPortarit = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if (isPortarit)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1458 / 10000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 1100 / 10000);
                }
            }
            case ID_NAV_TITLE_LEFT_PADDING:
            {
                if (AppConfigHelper.BRAND_SPRINT.equals(AppConfigHelper.brandName)
                        || AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName)
                        || AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
                else
                {
                    return NAV_BUTTON_HORIZONTAL_PADDING.getInt();
                }
            }
            case ID_TURN_ICON_WIDTH:
            {
                if (AppConfigHelper.BRAND_SPRINT.equals(AppConfigHelper.brandName)
                        || AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName)
                        || AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
                {
                    if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        return PrimitiveTypeCache.valueOf(TURN_ICON_HEIGHT.getInt());
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 1479 / 10000);
                    }
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(TURN_ICON_HEIGHT.getInt());
                }
            }
            case ID_TURN_ICON_HEIGHT:
            {
                if (AppConfigHelper.BRAND_SPRINT.equals(AppConfigHelper.brandName)
                        || AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName)
                        || AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
                {
                    return PrimitiveTypeCache.valueOf(NAV_TITLE_HEIGHT.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1021 / 10000);
                }
            }
            case ID_NAV_BUTTON_HORIZONTAL_PADDING:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 400 / 10000);
            }
            case ID_ZOOM_OUT_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - NAV_BUTTON_HORIZONTAL_PADDING.getInt()
                        - ZOOM_OUT_WIDTH.getInt() - ZOOM_IN_WIDTH.getInt());
            }
            case ID_ZOOM_OUT_Y:
            {
                boolean isPortarit = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if (isPortarit)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 260 / 10000
                            + NAV_TITLE_HEIGHT.getInt() + AppConfigHelper.getTopBarHeight());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 185 / 10000
                            + NAV_TITLE_HEIGHT.getInt() + AppConfigHelper.getTopBarHeight());
                }
            }
            case ID_ZOOM_OUT_WIDTH:
            {
                boolean isPortarit = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if (isPortarit || isZoomHorizonLayout())
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 78 / 1000);
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 750 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 100 / 1000);
                    }
                }
            }
            case ID_ZOOM_OUT_HEIGHT:
            {
                boolean isPortarit = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if (isPortarit || isZoomHorizonLayout())
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 102 / 1000);
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1325 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 133 / 1000);
                    }
                }
            }
            case ID_ZOOM_IN_X:
            {
                return PrimitiveTypeCache.valueOf(ZOOM_OUT_X.getInt() - ZOOM_OUT_WIDTH.getInt());
            }
            case ID_ZOOM_IN_Y:
            {
                return PrimitiveTypeCache.valueOf(ZOOM_OUT_Y.getInt());
            }
            case ID_ZOOM_IN_WIDTH:
            {
                boolean isPortarit = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if (isPortarit || isZoomHorizonLayout())
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 78 / 1000);
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 750 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 100 / 1000);
                    }
                }
            }
            case ID_ZOOM_IN_HEIGHT:
            {
                boolean isPortarit = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if (isPortarit || isZoomHorizonLayout())
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 102 / 1000);
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1325 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 133 / 1000);
                    }
                }
            }
            case ID_BACK_TO_NAV_BUTTON_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - NAV_BUTTON_HORIZONTAL_PADDING.getInt()
                        - BACK_TO_NAV_BUTTON_WIDTH.getInt());
            }
            case ID_BACK_TO_NAV_BUTTON_Y:
            {
                int gapToBottom;
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    gapToBottom = AppConfigHelper.getDisplayHeight() * 690 / 10000;
                }
                else
                {
                    gapToBottom = AppConfigHelper.getDisplayHeight() * 350 / 10000;
                    AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MAP_PROVIDER);
                    gapToBottom += font.getHeight();
                }

                int apiVersion = android.os.Build.VERSION.SDK_INT;
                if (AppConfigHelper.isTabletSize() && apiVersion > 10)
                {
                    int y = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - getBottomBarHeight()
                            - BACK_TO_NAV_BUTTON_HEIGHT.getInt() - gapToBottom;
                    return PrimitiveTypeCache.valueOf(y + AppConfigHelper.getTopBarHeight());
                }
                else
                {
                    int y = AppConfigHelper.getDisplayHeight() - getBottomBarHeight() - BACK_TO_NAV_BUTTON_HEIGHT.getInt()
                            - gapToBottom;
                    return PrimitiveTypeCache.valueOf(y + AppConfigHelper.getTopBarHeight());
                }
            }
            case ID_BACK_TO_NAV_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(TRAFFIC_BUTTON_WIDTH.getInt());
            }
            case ID_BACK_TO_NAV_BUTTON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(BACK_TO_NAV_BUTTON_WIDTH.getInt());
            }
            case ID_TRAFFIC_BUTTON_X:
            {
                return PrimitiveTypeCache.valueOf(NAV_BUTTON_HORIZONTAL_PADDING.getInt());
            }
            case ID_TRAFFIC_BUTTON_Y:
            {
                return PrimitiveTypeCache.valueOf(ZOOM_OUT_Y.getInt());
            }
            case ID_TRAFFIC_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(ZOOM_OUT_HEIGHT.getInt());
            }
            case ID_TRAFFIC_BUTTON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(TRAFFIC_BUTTON_WIDTH.getInt());
            }

            case ID_STREET_NAME_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 200 / SCALE);
            }
            case ID_STREET_NAME_Y:
            {
                return PrimitiveTypeCache.valueOf(getCurrentStreetCompY());
            }
            case ID_STREET_NAME_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 610 / SCALE);
            }
            case ID_STREET_NAME_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(getCurrentStreetCompHeight());
            }
            case ID_SPEED_LIMIT_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 800 / SCALE);
            }
            case ID_SPEED_LIMIT_Y:
            {
                return PrimitiveTypeCache.valueOf(getCurrentStreetCompY());
            }
            case ID_SPEED_LIMIT_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 210 / SCALE);
            }
            case ID_SPEED_LIMIT_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(getCurrentStreetCompHeight());
            }
            case ID_COMPASS_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_COMPASS_Y:
            {
                return PrimitiveTypeCache.valueOf(getCurrentStreetCompY());
            }
            case ID_COMPASS_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 200 / SCALE);
            }
            case ID_COMPASS_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(getCurrentStreetCompHeight());
            }
            case ID_AD_HEIGHT:
            {
                int height;

                if (AppConfigHelper.isTabletSize())
                {
                    height = AppConfigHelper.getMaxSizeForBrowser() * 70 / 1000;
                }
                else
                {
                    height = AppConfigHelper.getMaxSizeForBrowser() / 15;
                }

                height += getVBBSelectorOffset();

                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_AD_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_AD_Y:
            {
                return PrimitiveTypeCache.valueOf(getAdY());
            }
            case ID_AD_HEIGHT_EXT:
            {
                int height;

                if (AppConfigHelper.isTabletSize())
                {
                    height = AppConfigHelper.getMaxSizeForBrowser() * 225 / 1000;
                }
                else
                {
                    height = AppConfigHelper.getMaxSizeForBrowser() * 300 / 1000;
                }

                height += getVBBSelectorOffset();

                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_AD_Y_EXT:
            {
                return PrimitiveTypeCache.valueOf(getAdYExt());
            }
            case ID_END_TRIP_FAVORITE_NOTIFICATION_WIDTH:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 625 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_END_TRIP_FAVORITE_NOTIFICATION_X:
            {
                int x = (SCREEN_WIDTH.getInt() - END_TRIP_FAVORITE_NOTIFICATION_WIDTH.getInt()) / 2;
                return PrimitiveTypeCache.valueOf(x);
            }
            case ID_END_TRIP_FAVORITE_NOTIFICATION_TEXT_WIDTH:
            {
                int hPadding = AppConfigHelper.getMinDisplaySize() * 31 / 1000;
                int width = END_TRIP_FAVORITE_NOTIFICATION_WIDTH.getInt() - hPadding * 2;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TRAFFIC_ALERT_POPUP_SHADOW_PADDING:
            {
                int shadowHeight = 12;
                return PrimitiveTypeCache.valueOf(shadowHeight);
            }
            case ID_TRAFFIC_ALERT_TITLE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1175 / 10000);
            }
            case ID_TRAFFIC_ALERT_DETAIL_TOP_HEIGHT:
            {
                int height = TRAFFIC_ALERT_TITEL_HEIGHT.getInt() + TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT.getInt();
                if (UiStyleManager.getInstance().isComponentNeeded(
                    INinePatchImageRes.POPUP_SPLIT_LINE_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED))
                {
                    height += TRAFFIC_ALERT_DETAIL_SPACE_BELOW_SPLIT_LINE.getInt();
                }
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT:
            {
                int maxDisplaySize = AppConfigHelper.getMaxDisplaySize();
                int height = maxDisplaySize * 250 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TRAFFIC_ALERT_DETAIL_SPACE_BELOW_SPLIT_LINE:
            {
                int height = (TRAFFIC_ALERT_TITEL_HEIGHT.getInt() - ImageDecorator.MAP_TRAFFIC_ICON_FOCUSED.getImage()
                        .getHeight()) / 2 - 10;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_NAV_END_TRIP_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(getBottomBarHeight());
            }
            case ID_NAV_END_TRIP_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_NAV_END_TRIP_Y:
            {
                return PrimitiveTypeCache.valueOf(NAV_TITLE_HEIGHT.getInt());
            }
            case ID_NAV_END_TRIP_LEFT_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - NAV_END_TRIP_HEIGHT.getInt() - 2 * END_TRIP_HORIZONTAL_PADDING.getInt());
            }
            case ID_END_TRIP_FAVORITE_NOTIFICATION_PADDING:
            {
                int padding = AppConfigHelper.getMaxDisplaySize() * 19 / 1000;
                return PrimitiveTypeCache.valueOf(padding);
            }
            case ID_POPUP_SPLIT_LINE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(1);
            }
            case ID_MOVING_MAP_LOGO_Y_WITH_BOTTOM_BAR:
            {
                return PrimitiveTypeCache.valueOf(MAP_LOGO_Y.getInt() - getCurrentStreetCompHeight());
            }
            case ID_AD_INITIAL_HEIGHT_WITHOUT_EAR:
            {
                return PrimitiveTypeCache.valueOf(AD_HEIGHT.getInt() - getVBBSelectorOffset());
            }
            case ID_AD_FULL_HEIGHT_WITHOUT_EAR:
            {
                return PrimitiveTypeCache.valueOf(AD_HEIGHT_EXT.getInt() - getVBBSelectorOffset());
            }
            case ID_NEXT_NEXT_TURN_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_NEXT_NEXT_TURN_HEIGHT:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 86 / 1230);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 78 / 670);
                }
            }
            case ID_NEXT_NEXT_TURN_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_NEXT_NEXT_TURN_Y:
            {
                return PrimitiveTypeCache.valueOf(NAV_TITLE_HEIGHT.getInt());
            }
            case ID_END_TRIP_HORIZONTAL_PADDING:
            {
                int padding = AppConfigHelper.getMaxDisplaySize() * 19 / SCALE;
                return PrimitiveTypeCache.valueOf(padding);
            }
        }
        return null;
    }

    private int getAdY()
    {
        return AppConfigHelper.getDisplayHeight() - getCurrentStreetCompHeight() - AD_HEIGHT.getInt();
    }

    private int getAdYExt()
    {
        return AppConfigHelper.getDisplayHeight() - getCurrentStreetCompHeight() - AD_HEIGHT_EXT.getInt();
    }

    private boolean isZoomHorizonLayout()
    {
        return true;
    }

    private int getBottomBarHeight()
    {
        return PrimitiveTypeCache.valueOf(BOTTOM_BAR_REAL_HEIGHT.getInt());
    }

    private int getCurrentStreetCompHeight()
    {
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            return PrimitiveTypeCache.valueOf(getBottomBarHeight());
        }
        else
        {
            return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 125 / 1000);
        }
    }

    private int getCurrentStreetCompY()
    {
        return AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - getCurrentStreetCompHeight();
    }

    // Because now browser page can't fill the page automatically, I need to set the related selector height for this
    // feature.
    // I have suggested browser team can change the page to be expanded automatically according to the screen.
    // then we only need to set a max height for this.
    private int getVBBSelectorOffset()
    {
        int offset = 0;
        if (ISpecialImageRes.getSpecialImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
        {
            offset = 17;
        }
        else if (ISpecialImageRes.getSpecialImageFamily() == ISpecialImageRes.FAMILY_VAST)
        {
            if (AppConfigHelper.getMaxDisplaySize() < 960)
            {
                offset = 27;
            }
            else
            {
                offset = 30;
            }
        }

        return offset;
    }
}

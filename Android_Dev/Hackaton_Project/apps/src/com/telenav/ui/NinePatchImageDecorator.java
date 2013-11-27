/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NinePatchImageDecorator.java
 *
 */
package com.telenav.ui;

import com.telenav.data.cache.ImageCacheManager;
import com.telenav.datatypes.DataUtil;
import com.telenav.i18n.ResourceBundle;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author fqming (fqming@telenav.cn)
 * @date Aug 16, 2010
 */
public class NinePatchImageDecorator implements ITnUiArgsDecorator
{
    public static NinePatchImageDecorator instance = new NinePatchImageDecorator();

    public static TnUiArgAdapter BOTTOM_NAV_BAR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.BOTTOM_NAVIGATION_BAR_ID
            + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter BOTTOM_NAVIGATION_NAV_BAR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.BOTTOM_NAVIGATION_NAV_BAR_UNFOCUSED
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter BIG_RADIAN_BUTTON_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.BIG_RADIAN_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter BIG_RADIAN_BUTTON_FOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.BIG_RADIAN_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter SMALL_RADIAN_BUTTON_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.SMALL_RADIAN_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter SMALL_RADIAN_BUTTON_FOCUSED = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.SMALL_RADIAN_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter MIC_BUTTON_BG_UNFOCUS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MIC_BUTTON_BG
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MIC_BUTTON_BG_FOCUS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MIC_BUTTON_BG
            + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter SEARCH_BOX_BG_UNFOCUS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.SEARCH_BOX_BG
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter SEARCH_BOX_INPUT_BG_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.SEARCH_BOX_INPUT_BG + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter SEARCH_BOX_INPUT_BG_FOCUSED = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.SEARCH_BOX_INPUT_BG + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter SEARCH_BOX_BUTTON_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.SEARCH_BOX_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter SEARCH_BOX_BUTTON_FOCUSED = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.SEARCH_BOX_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter DROPDOWN_NORMAL_ITEM_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.DROPDOWN_NORMAL_ITEM + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter DROPDOWN_NORMAL_ITEM_FOCUSED = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.DROPDOWN_NORMAL_ITEM + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter DROPDOWN_BOTTOM_ITEM_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.DROPDOWN_BOTTOM_ITEM + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter DROPDOWN_BOTTOM_ITEM_FOCUSED = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.DROPDOWN_BOTTOM_ITEM + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter LIST_ITEM_UNFOCUS = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.LIST_ITEM
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter LIST_ITEM_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.LIST_ITEM
            + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_BG = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_BG
            + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter POI_BUBBLE_POI_TITLE_UNFOCUS = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_POI_TITLE + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter POI_BUBBLE_POI_TITLE_FOCUSED = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_POI_TITLE + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_ADDRESS_TITLE_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_ADDRESS_TITLE + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_ADDRESS_TITLE_FOCUSED = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_ADDRESS_TITLE + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_LEFT_BUTTON_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_LEFT_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_LEFT_BUTTON_FOCUSED = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_LEFT_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_MIDDLE_BUTTON_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_MIDDLE_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_MIDDLE_BUTTON_FOCUSED = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_MIDDLE_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_RIGHT_BUTTON_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_RIGHT_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter POI_BUBBLE_RIGHT_BUTTON_FOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_BUBBLE_RIGHT_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);

    //the three small buttons in the second line of the poi/address bubble.
    public static TnUiArgAdapter POI_BUBBLE_DRIVETO_LEFT_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.POI_BUBBLE_DRIVETO_LEFT_BUTTON_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);
   
    public static TnUiArgAdapter MESSAGE_BOX_TOP_BG = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.MESSAGE_BOX_TOP_BACKGROUND + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MESSAGE_BOX_BOTTOM_BG = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.MESSAGE_BOX_BOTTOM_BACKGROUND + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MESSAGE_BOX_BG = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MESSAGE_BOX_BACKGROUND
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter POI_DETAIL_BACKGROUND = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_DETAIL_BG
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BACKGROUND + INinePatchImageRes.ID_UNFOCUSED),
            instance);

    public static TnUiArgAdapter POI_DETAIL_TITLE_BG = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.POI_DETAIL_TITLE_BG + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter COMBO_BOX_BG_UNFOCUSED_TOP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_TOP
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_UNFOCUSED_MIDDLE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_MIDDLE
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_UNFOCUSED_BOTTOM = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_BOTTOM
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_FOCUSED_TOP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_TOP
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_FOCUSED_MIDDLE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_MIDDLE
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_FOCUSED_BOTTOM = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_BOTTOM
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_LEFT_TOP_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_LEFT_TOP
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_LEFT_BOTTOM_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_LEFT_BOTTOM
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_RIGHT_TOP_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_RIGHT_TOP
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_RIGHT_BOTTOM_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_RIGHT_BOTTOM
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_LEFT_TOP_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_LEFT_TOP
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_LEFT_BOTTOM_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_LEFT_BOTTOM
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_RIGHT_TOP_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_RIGHT_TOP
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter COMBO_BOX_BG_RIGHT_BOTTOM_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.COMBO_BOX_BG_RIGHT_BOTTOM
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_LEFT_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_LEFT_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_MIDDLE_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_MIDDLE_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_RIGHT_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_RIGHT_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_LEFT_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_LEFT_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_MIDDLE_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_MIDDLE_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_RIGHT_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_RIGHT_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_LEFT_TOP_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_LEFT_TOP_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_LEFT_BOTTOM_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_LEFT_BOTTOM_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_RIGHT_TOP_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_RIGHT_TOP_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_RIGHT_BOTTOM_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_RIGHT_BOTTOM_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_LEFT_TOP_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_LEFT_TOP_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_LEFT_BOTTOM_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_LEFT_BOTTOM_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_RIGHT_TOP_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_RIGHT_TOP_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_STYLE_RIGHT_BOTTOM_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_STYLE_RIGHT_BOTTOM_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter MAP_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_BUTTON
            + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter MAP_BUTTON_NIGHT_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_BUTTON_NIGHT
        + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MAP_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_BUTTON
            + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter NAV_ALERT_BG = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.NAV_ALERT_BG
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter NAV_TIGHT_TURN_BG = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.NAV_TIGHT_TURN_BG + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_FOCUSED_LEFT = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_FOCUSED_LEFT + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_FOCUSED_RIGHT = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_FOCUSED_RIGHT + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter NAV_CONTROLS_BG_FOCUSED_TOP = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_FOCUSED_TOP + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_FOCUSED_BOTTOM = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_FOCUSED_BOTTOM+ INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_UNFOCUSED_LEFT = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_UNFOCUSED_LEFT + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_UNFOCUSED_RIGHT = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_UNFOCUSED_RIGHT + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter NAV_CONTROLS_BG_UNFOCUSED_TOP = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_UNFOCUSED_TOP + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_UNFOCUSED_BOTTOM = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_UNFOCUSED_BOTTOM + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter NAV_TURN_BG = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.NAV_TURN_BG
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MAP_SHADOW_BUTTON_UNFOCUS = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_SHADOW_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter SUMMARY_TRAFFIC_GRAY = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.TRAFFIC_ITEM_SPEED_BG_GRAY + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter SUMMARY_TRAFFIC_GREEN = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.TRAFFIC_ITEM_SPEED_BG_GREEN + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter SUMMARY_TRAFFIC_YELLOW = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.TRAFFIC_ITEM_SPEED_BG_YELLOW + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter SUMMARY_TRAFFIC_ORANGE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.TRAFFIC_ITEM_SPEED_BG_ORANGE + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter SUMMARY_TRAFFIC_RED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.TRAFFIC_ITEM_SPEED_BG_RED + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_ITEM_TURN_BG = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_ITEM_TURN_BG + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter ROUTE_ITEM_TURN_CURRENT_BG = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_ITEM_TURN_CURRENT_BG + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MAP_LAYER_ICON_BG_UNFOCUSED  = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_LAYER_ICON_BG  + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter MAP_LAYER_ICON_BG_FOCUSED  = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_LAYER_ICON_BG  + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter MAP_LAYER_ICON_DISABLED_BG_UNFOCUSED  = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_LAYER_ICON_DISABLED_BG  + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MAP_SMALL_BUTTON_ICON_BG  = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_SMALL_BUTTON_ICON_BG  + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter PROGRESS_BAR_BG_FOCUS  = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.PROGRESS_BAR_BG  + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter PROGRESS_BAR_BG_FOCUS_END  = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.PROGRESS_BAR_BG_END  + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter PROGRESS_BAR_BG_UNFOCUS  = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.PROGRESS_BAR_BG  + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter SORT_BY_BUTTON_ICON_UNFOCUS = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.SORT_BY_BUTTON_ICON_BG + INinePatchImageRes.ID_UNFOCUSED), instance);
    public static TnUiArgAdapter SORT_BY_BUTTON_ICON_FOCUS = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.SORT_BY_BUTTON_ICON_BG + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter SORT_BY_SEPERATOR_LINE_UNFOCUS = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.SORT_BY_SEPERATOR_LINE + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter MAP_SEMI_TRANSPARENT_BANNER = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_SEMI_TRANSPARENT_BANNER + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter STREET_TABLE_BG_UNFOCUSED = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.STREET_TABLE_BG_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);
    
//    public static TnUiArgAdapter TURN_MAP_SEMI_TRANSPARENT_BANNER = new TnUiArgAdapter(
//        PrimitiveTypeCache.valueOf(INinePatchImageRes.TURN_MAP_SEMI_TRANSPARENT_BANNER + INinePatchImageRes.ID_UNFOCUSED), instance);
    
//    public static TnUiArgAdapter TURN_MAP_SEMI_TRANSPARENT_BANNER_LINE = new TnUiArgAdapter(
//        PrimitiveTypeCache.valueOf(INinePatchImageRes.TURN_MAP_SEMI_TRANSPARENT_BANNER_LINE + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter FAVORITE_PURE_WHITE_BG = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.FAVORITE_PURE_WHITE_BG + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter SEMI_TRANSPARENT_BG_UNFOCUSED = new TnUiArgAdapter(
        PrimitiveTypeCache.valueOf(INinePatchImageRes.SEMI_TRANSPARENT_BG_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter MAP_SHADOW_GREEN_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_SHADOW_GREEN_BUTTON_UNFOCUSED
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter MAP_SHADOW_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_SHADOW_BUTTON_FOCUSED
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
//    public static TnUiArgAdapter NAV_HEADER_PROGRESS_BAR_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
//            .valueOf(INinePatchImageRes.NAV_HEADER_PROGRESS_BAR_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter SPONSORED_LIST_ITEM_BG_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.SPONSORED_POI_LIST_ITEM_BG_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_FOCUSED + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter NAV_CONTROLS_BG_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(INinePatchImageRes.NAV_CONTROLS_BG_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter QUICK_FIND_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.QUICK_FIND_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter INPUT_DISABLE_BG = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.INPUT_DISABLE_BG_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance); 
    
    public static TnUiArgAdapter TAB_CONTAINER_LEFT_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.TAB_CONTAINER_LEFT_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter TAB_CONTAINER_LLEFT_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.TAB_CONTAINER_LEFT_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter TAB_CONTAINER_MIDDLE_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.TAB_CONTAINER_MIDDLE_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter TAB_CONTAINER_MIDDLE_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.TAB_CONTAINER_MIDDLE_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter TAB_CONTAINER_RIGHT_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.TAB_CONTAINER_RIGHT_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter TAB_CONTAINER_RIGHT_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.TAB_CONTAINER_RIGHT_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);
    
    
    public static TnUiArgAdapter MAP_ZOOM_TOP_BG_DAY_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.MAP_ZOOM_TOP_BTN_BG_DAY + INinePatchImageRes.ID_UNFOCUSED), instance);
    public static TnUiArgAdapter MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.MAP_ZOOM_TOP_BTN_BG_NIGHT + INinePatchImageRes.ID_UNFOCUSED), instance);
    public static TnUiArgAdapter MAP_ZOOM_TOP_BG_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.MAP_ZOOM_TOP_BTN_BG_DAY + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.MAP_ZOOM_BOTTOM_BTN_BG_DAY + INinePatchImageRes.ID_UNFOCUSED), instance);
    public static TnUiArgAdapter MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.MAP_ZOOM_BOTTOM_BTN_BG_NIGHT + INinePatchImageRes.ID_UNFOCUSED), instance);
    public static TnUiArgAdapter MAP_ZOOM_BOTTOM_BG_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.MAP_ZOOM_BOTTOM_BTN_BG_DAY + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ONEBOX_TAB_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.ONEBOX_TAB_BUTTON_FOCUSED + INinePatchImageRes.ID_FOCUSED), instance);
    public static TnUiArgAdapter ONEBOX_TAB_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.ONEBOX_TAB_BUTTON_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    
    public static TnUiArgAdapter NAV_END_TRIP_BG = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.NAV_END_TRIP_BG + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter QUICK_FIND_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.QUICK_FIND_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);

    public static TnUiArgAdapter SCREEN_SPLITE_LINE_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.SPLITE_LINE + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter DASHBOARD_RESUME_TRIP_BG_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.DASHBOARD_RESUME_TRIP_BG + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter POPUP_SPLIT_LINE_UNFOCUSED_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.POPUP_SPLIT_LINE_UNFOCUSED + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter AC_DRIVE_TO_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.AC_DRIVE_TO_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter AC_DRIVE_TO_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.AC_DRIVE_TO_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter FAVORITE_EDIT_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.FAVORITE_EDIT_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter FAVORITE_EDIT_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.FAVORITE_EDIT_BUTTON + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter MAP_DROPUP_BG_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_DROPUP_BG
        + INinePatchImageRes.ID_UNFOCUSED), instance);
   
    public static TnUiArgAdapter FTUE_TEXT_VIEW_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.FTUE_TEXT_VIEW_UNFOCUSED
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter FTUE_BG_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.FTUE_BG_UNFOCUSED
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter FTUE_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.FTUE_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter FTUE_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.FTUE_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter PROFILE_LIST_ITEM = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.PROFILE_LIST_ITEM
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter PROFILE_LOGIN_ITEM_FOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.PROFILE_LOGIN_ITEM
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter PROFILE_LOGIN_ITEM_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.PROFILE_LOGIN_ITEM
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter MAP_DOWNLOAD_TAB_BUTTON_LEFT_FOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_DOWNLOAD_TAB_BUTTON_LEFT
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter MAP_DOWNLOAD_TAB_BUTTON_LEFT_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_DOWNLOAD_TAB_BUTTON_LEFT
        + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MAP_DOWNLOAD_TAB_BUTTON_MIDDLE_FOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_DOWNLOAD_TAB_BUTTON_MIDDLE
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter MAP_DOWNLOAD_TAB_BUTTON_MIDDLE_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_DOWNLOAD_TAB_BUTTON_MIDDLE
        + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter MAP_DOWNLOAD_TAB_BUTTON_RIGHT_FOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_DOWNLOAD_TAB_BUTTON_RIGHT
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter MAP_DOWNLOAD_TAB_BUTTON_RIGHT_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.MAP_DOWNLOAD_TAB_BUTTON_RIGHT
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    ////
    public static TnUiArgAdapter ROUTE_PLANNING_SHARE_ETA_BG_CHECKED_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_SHARE_ETA_BG_CHECKED
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_PLANNING_SHARE_ETA_BG_CHECKED_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_SHARE_ETA_BG_CHECKED
        + INinePatchImageRes.ID_UNFOCUSED), instance);

    public static TnUiArgAdapter ROUTE_PLANNING_SHARE_ETA_BG_UNCHECKED_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_SHARE_ETA_BG_UNCHECKED
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_PLANNING_SHARE_ETA_BG_UNCHECKED_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_SHARE_ETA_BG_UNCHECKED
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_PLANNING_NAVIGATE_BG_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_NAVIGATE_BG
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_PLANNING_NAVIGATE_BG_FOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_NAVIGATE_BG
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_PLANNING_CONFIRM_PANEL_INFO_BG_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_CONFIRM_PANEL_INFO_BG
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter TITLE_BUTTON_BG_UNFOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.TITLE_BUTTON_BG
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter TITLE_BUTTON_BG_FOCUSED= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.TITLE_BUTTON_BG
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter ROUTE_PLANNING_PANEL_BG_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.ROUTE_PLANNING_PANEL_BG
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter BOTTOM_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.BOTTOM_BUTTON
        + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter BOTTOM_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.BOTTOM_BUTTON
        + INinePatchImageRes.ID_FOCUSED), instance);
    
    public static TnUiArgAdapter POI_CATEGORY_SEPERATE_LINE_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.POI_CATEGORY_SEPERATE_LINE + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter POI_GOBY_LOCAL_EVENT_SEPERATE_LINE_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.POI_GOBY_LOCAL_EVENT_SEPERATE_LINE + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter PROFILE_RED_BADGE_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.PROFILE_RED_BADGE + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter BIG_RADIAN_YELLOW_BUTTON_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.BIG_RADIAN_YELLOW_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    public static TnUiArgAdapter BIG_RADIAN_YELLOW_BUTTON_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(INinePatchImageRes.BIG_RADIAN_YELLOW_BUTTON + INinePatchImageRes.ID_UNFOCUSED), instance);
    
    private final static String[] FILTER =
    { "_unfocused", "_focused" };
    
    private final static String STRETCH_IMAGE_CARVEL_NAME_EXTENSION = ".carvel.9.png";

    private final static String WRADIANT = "wradiant";

    private final static String STRETCH_IMAGE_NAME_EXTENSION = ".9.png";
    private Object decorateByOriginalNinePatch(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            AbstractTnImage[] ninepatchImages = null;
            AbstractTnImage[] radiantImages = null;
            String lastPrefix = null;
            byte[] lastImageData = null;
            for (int i = 0; i < 18; i++)
            {
                int id = key + i;
                // such as button_unfocused_wradiant_top.png
                String pngName = (String) INinePatchImageRes.NINE_PATCH_IMAGES.get(PrimitiveTypeCache.valueOf(id));
                if (pngName == null || pngName.equals(""))
                    continue;
                
                int index = pngName.indexOf(FILTER[0]);
                if (index != -1)
                {
                    index += FILTER[0].length();
                }
                else
                {
                    index = pngName.indexOf(FILTER[1]);
                    index += FILTER[1].length();
                }
                String prefix = pngName.substring(0, index);

                if (lastPrefix == null || !prefix.equals(lastPrefix))
                {
                    byte[] imageData = bundle.getGenericImage(prefix + ".image", INinePatchImageRes.FAMILY_NINE_PATCH);
                    // byte[] imageData = bundle.getBinary(key, prefix + ".bin", null);
                    lastImageData = imageData;
                    lastPrefix = prefix;
                }

                String positionStr = pngName.substring(pngName.indexOf('_', index) + 1, pngName.lastIndexOf('.'));
                int wrandiantIndex = positionStr.indexOf(WRADIANT);
                if(wrandiantIndex != -1)
                {
                    positionStr = positionStr.substring(positionStr.indexOf('_', wrandiantIndex) + 1);
                }
                int position = ((Integer)INinePatchImageRes.NINE_PATCH_POSITIONS.get(positionStr)).intValue();
                int tag = i % 9;
                int wradiant = 0;
                if (i <= TnNinePatchImage.LEFT_BOTTOM)
                {
                    if (ninepatchImages == null)
                    {
                        ninepatchImages = new AbstractTnImage[9];
                    }
                    
                    AbstractTnImage image = createNinePatchImageFromBinary(position, wradiant, lastImageData);
                    
                    if(NinePatchImageDecorator.QUICK_FIND_BUTTON_UNFOCUSED.equals(args) ||
                            NinePatchImageDecorator.QUICK_FIND_BUTTON_FOCUSED.equals(args))
                    {
                        image = ImageDecorator.checkStretchImage(image);
                    }
                    
                    ninepatchImages[tag] = image;
                }
                else
                {
                    wradiant = 1;
                    if (radiantImages == null)
                    {
                        radiantImages = new AbstractTnImage[9];
                    }
                    
                    AbstractTnImage image = createNinePatchImageFromBinary(position, wradiant, lastImageData);
                    
                    if(NinePatchImageDecorator.QUICK_FIND_BUTTON_UNFOCUSED.equals(args) ||
                            NinePatchImageDecorator.QUICK_FIND_BUTTON_FOCUSED.equals(args))
                    {
                        image = ImageDecorator.checkStretchImage(image);
                    }
                    
                    radiantImages[tag] = image;
                }
            }
            int drawMode = TnNinePatchImage.DRAW_SCALE;
            if (ninepatchImages[TnNinePatchImage.LEFT] != null || ninepatchImages[TnNinePatchImage.LEFT_TOP] != null
                    || ninepatchImages[TnNinePatchImage.LEFT_BOTTOM] != null || ninepatchImages[TnNinePatchImage.RIGHT] != null
                    || ninepatchImages[TnNinePatchImage.RIGHT_BOTTOM] != null || ninepatchImages[TnNinePatchImage.RIGHT_TOP] != null)
                drawMode = drawMode | TnNinePatchImage.DRAW_ROUNDCORNER;
            TnNinePatchImage tnNinePatchImage = new TnNinePatchImage(ninepatchImages[TnNinePatchImage.LEFT],
                    ninepatchImages[TnNinePatchImage.TOP], ninepatchImages[TnNinePatchImage.RIGHT],
                    ninepatchImages[TnNinePatchImage.BOTTOM], ninepatchImages[TnNinePatchImage.CENTER],
                    ninepatchImages[TnNinePatchImage.LEFT_TOP], ninepatchImages[TnNinePatchImage.RIGHT_TOP],
                    ninepatchImages[TnNinePatchImage.RIGHT_BOTTOM], ninepatchImages[TnNinePatchImage.LEFT_BOTTOM]);
            if (radiantImages != null)
            {
                for (int i = 0; i < radiantImages.length; i++)
                {
                    tnNinePatchImage.setRadiant(i, radiantImages[i]);
                }
            }
            tnNinePatchImage.setDrawMode(drawMode);
            ImageCacheManager.getInstance().getNinePatchImageCache().put(PrimitiveTypeCache.valueOf(key - 1), tnNinePatchImage);
            return tnNinePatchImage;
    }

    public Object decorateByStretchedNinePatch(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String pngName = (String) INinePatchImageRes.NINE_PATCH_IMAGES.get(PrimitiveTypeCache.valueOf(key));
        if (pngName == null || pngName.equals(""))
        {
            return null;
        }

        byte[] imageData = bundle.getGenericImage(pngName, INinePatchImageRes.FAMILY_NINE_PATCH);
        TnNinePatchImage image = new TnNinePatchImage(AbstractTnGraphicsHelper.getInstance().createImage(imageData));
        if (pngName.contains(STRETCH_IMAGE_CARVEL_NAME_EXTENSION))
        {
            image.setDrawMode(TnNinePatchImage.DRAW_CARVEL);
        }
        ImageCacheManager.getInstance().getNinePatchImageCache().put(PrimitiveTypeCache.valueOf(key - 1), image);
        return image;
    }

    public Object decorate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        AbstractTnImage wholeImage = (AbstractTnImage) ImageCacheManager.getInstance().getNinePatchImageCache().get(
            PrimitiveTypeCache.valueOf(key - 1));
        if (wholeImage == null)
        {
            String pngName = (String) INinePatchImageRes.NINE_PATCH_IMAGES.get(PrimitiveTypeCache.valueOf(key));

            if (pngName == null || pngName.equals(""))
            {
                return decorateByOriginalNinePatch(args);
            }
            else if (pngName.contains(STRETCH_IMAGE_NAME_EXTENSION))
            {
                return decorateByStretchedNinePatch(args);
            }
            else
            {
                return decorateByOriginalNinePatch(args);
            }
        }
        else
            return wholeImage;

    }

    private AbstractTnImage createNinePatchImageFromBinary(int tag, int wradiant, byte[] imageData)
    {
        if (imageData == null)
        {
            return null;
        }
        int index = 0;
        while (index < imageData.length && imageData[index] != (byte) 0xff)
        {
            int position = imageData[index++];
            if (position == tag)
            {
                int wradiantTag = imageData[index++];
                if (wradiantTag == wradiant)
                {
                    long offset = DataUtil.readLong(imageData, index);
                    index += 8;
                    long length = DataUtil.readLong(imageData, index);
                    // System.out.println(length);
                    byte[] data = new byte[(int) length];
                    System.arraycopy(imageData, (int) offset, data, 0, (int) length);
                    AbstractTnImage image = AbstractTnGraphicsHelper.getInstance().createImage(data);
                    return image;
                }
                index--;
            }
            
            
            
            
            
            
            
            
            index += 17;
        }
        return null;
    }

}

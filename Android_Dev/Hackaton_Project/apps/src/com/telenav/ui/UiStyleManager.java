/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * UiStyleManager.java
 *
 */
package com.telenav.ui;

import java.util.HashMap;
import java.util.Hashtable;

import com.telenav.res.INinePatchImageRes;
import com.telenav.res.ResourceUtil;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public abstract class UiStyleManager
{
    //---------------------------------------color style id----------------------------------------------//
    public final static int TEXT_COLOR_WH = 0;

    public final static int TEXT_COLOR_DA_BL = 1;

    public final static int TEXT_COLOR_OR = 2;

    public final static int TEXT_COLOR_AT_GR = 3;

    public final static int TEXT_COLOR_SU_LI_GR = 4;

    public final static int TEXT_COLOR_BL = 5;

    public final static int TEXT_COLOR_LI_GR = 6;

    public final static int TEXT_COLOR_ME_GR = 7;

    public final static int TEXT_COLOR_DA_GR = 8;

    public final static int BG_COLOR_LI_FO = 9;

    public final static int BG_COLOR_WH = 10;
    
    public final static int BG_COLOR_TRANSPARENT = 11;
    
    public final static int BG_SUBTITLE_BAR = 12;
    
    public final static int BG_NAV_TITLE = 13;
    
    public final static int BG_NAV_PROGRESS = 14;
    
    public final static int BG_SUMMARY_TIME_INFO = 15;
    
    public final static int BG_SUMMARY_ITEM_TITLE_TOP = 16;
    
    public final static int BG_SUMMARY_ITEM_TITLE_BOTTOM = 17;
    
    public final static int TEXT_COLOR_BLUE = 18;
    
    public final static int TEXT_COLOR_BUBBLE_TITLE_FOCUSED = 19;
    
    public final static int TEXT_COLOR_BUBBLE_TITLE_UNFOCUSED = 20;
    
    public final static int TEXT_COLOR_BUBBLE_ADDRESS_TITLE_FOCUSED = 21;
    
    public final static int TEXT_COLOR_BUBBLE_ADDRESS_TITLE_UNFOCUSED = 22;
    
    public final static int TEXT_COLOR_BUBBLE_CONTENT_FOCUSED = 23;
    
    public final static int TEXT_COLOR_BUBBLE_CONTENT_UNFOCUSED = 24;
    
    public final static int TEXT_COLOR_BUBBLE_BUTTON_FOCUSED = 25;
    
    public final static int TEXT_COLOR_BUBBLE_BUTTON_UNFOCUSED = 26;
    
    public final static int TEXT_COLOR_BUBBLE_ADDRESS_BUTTON_FOCUSED = 27;
    
    public final static int TEXT_COLOR_BUBBLE_ADDRESS_BUTTON_UNFOCUSED = 28;
    
    public final static int TEXT_COLOR_FILTER = 29;
    
    public final static int MAP_PROVIDER_TEXT_DAY_COLOR = 30;
    
    public final static int MAP_PROVIDER_TEXT_NIGHT_COLOR = 31;
    
    public final static int TEXT_COLOR_LABEL = 32;
    
    public final static int TEXT_COLOR_RED = 33;
    
    public final static int BOTTOM_BAR_COLOR_FOCUS = 34;
    
    public final static int BOTTOM_BAR_COLOR_UNFOCUS = 35;
    
    public final static int POI_ICON_PANEL_COLOR = 36;
    
    public final static int ICON_BUTTON_TEXT_COLOR = 37;
    
    public final static int DASHBOARD_TITLE_BACKGROUND_COLOR = 38;
    
    public final static int DASHBOARD_SPLIT_LINE_COLOR = 39;
    
    public final static int DASHBOARD_HIGHLIGHT_UNFOCUSED_COLOR = 40;
    
    public final static int DASHBOARD_HIGHLIGHT_FOCUSED_COLOR = 41;
    
    public final static int DASHBOARD_LOCATION_CITY_COLOR = 42;
    
    public final static int DASHBOARD_LOCATION_STREET_COLOR = 43;
    
    public final static int DASHBOARD_NORMAL_STRING_COLOR = 44;
    
    public final static int DASHBOARD_NEARBY_TIME_COLOR = 45;
    
    public final static int DASHBOARD_GREEN_TIME_COLOR = 46;
    
    public final static int DASHBOARD_ORANGE_TIME_COLOR = 47;
    
    public final static int DASHBOARD_RED_TIME_COLOR = 48;
    
    public final static int SCREEN_TITLE_BG_COLOR = 49;
    
    public final static int SCREEN_FILTER_BG_COLOR = 50;
    
    public final static int SCREEN_BOTTOM_BG_COLOR = 51;
    
    public final static int LIST_ITEM_TITLE_UNFOCUS_COLOR = 52;
    
    public final static int LIST_ITEM_TITLE_FOCUSED_COLOR = 53;
    
    public final static int NAV_ALTERNATE_ROUTE_TITLE_COLOR = 54;

    public final static int NAV_ALTERNATE_ROUTE_BOTTOM_COLOR = 55;
    
    public final static int TEXT_SUGGESTED_ROUTE_DEST_ABBR_COLOR = 56;
    
    public final static int TEXT_SUGGESTED_ROUTE_DEST_DETAIL_COLOR = 57;
    
    public final static int CLOCK_ANIMATION_DEFAULT_COLOR = 58;
    
    public final static int TEXT_POI_RESULT_INDEX_NUM_UNFOCUSED_COLOR = 59;
    
    public final static int TEXT_POI_RESULT_INDEX_NUM_FOCUSED_COLOR = 60;
    
    public final static int TEXT_POI_RESULT_ITEM_TITLE_UNFOCUSED_COLOR = 61;
    
    public final static int TEXT_POI_RESULT_ITEM_TITLE_FOCUSED_COLOR = 62;
    
    public final static int TEXT_POI_RESULT_AD_ITEM_SHORT_MESSAGE_FOCUSED_COLOR = 63;
    
    public final static int TEXT_POI_RESULT_AD_ITEM_SHORT_MESSAGE_UNFOCUSED_COLOR = 64;
    
    public final static int TEXT_POI_RESULT_COMBOX_UNFOCUSED_COLOR = 65;
    
    public final static int TEXT_POI_RESULT_COMBOX_FOCUSED_COLOR = 66;
    
    public final static int ROUTE_PLANING_CHOICE_ITEM_A_COLOR = 67;
    
    public final static int ROUTE_PLANING_CHOICE_ITEM_B_COLOR = 68;
    
    public final static int ROUTE_PLANING_CHOICE_ITEM_C_COLOR = 69;
    
    public final static int BUTTON_DEFAULT_UNFOCUSED_COLOR = 70;
    
    public final static int BUTTON_DEFAULT_FOCUSED_COLOR = 71;
    
    public final static int NAV_TRAFFIC_BUTTON_COLOR = 72;
    
    public final static int TOC_BOTTOM_BUTTON_TEXT_UNFOCUSED_COLOR = 73;
    
    public final static int TOC_BOTTOM_BUTTON_TEXT_FOCUSED_COLOR = 74;
    
    public final static int TOC_BOTTOM_ACCEPT_BUTTON_TEXT_UNFOCUSED_COLOR = 75;
    
    public final static int TEXT_COLOR_BUTTON_DISABLED = 76;
    
    public final static int NAV_TOP_SEPARATER_LINE_COLOR = 77;
    
    public final static int TOC_BOTTOM_BUTTON_CONTAINER_BACK_GROUND_COLOR = 78;
    
    public final static int POI_RESULTS_SEARCH_TITLE_LABEL_COLOR = 79;
    
    public final static int POI_RESULTS_LOCATION_LABEL_COLOR = 80;
    
    public final static int TEXT_COLOR_TAB_BUTTON_FOCUSED = 81;
    
    public final static int TEXT_COLOR_TAB_BUTTON_UNFOCUSED = 82;
	
	public final static int EDIT_FAV_SUB_TITLE_COLOR = 83;
	
	public final static int EDIT_FAV_SUB_TITLE_TEXT_COLOR = 84;
    
    public final static int TEXT_MAP_POI_ANNOTATION_COLOR = 85;
    
    public final static int TEXT_SUMMARY_TIME_INFO = 86;
    
    public final static int POI_CATEGORY_TITLE_BACKGROUND = 87;
    
    public final static int POI_CATEGORY_TEXTFIELD_BACKGROUND = 88;
    
    public final static int AC_BUTTON_BACKGROUND = 89;
    
    public final static int AC_BUTTON_FOCUSED_FOREGROUND = 90;
    
    public final static int AC_BUTTON_UNFOCUSED_FOREGROUND = 91;
    
    public final static int SYNCRES_PROGRESS_CONTAINER_BACKGROUND = 92;

    public final static int SEARCH_RESULT_TITLE_BG_COLOR = 93;
    
    public final static int TEXT_COLOR_FEEDBACK_LINK = 94;
    
    public final static int ONEBOX_TAB_CONTAINER_BG_COLOR = 95;
    
    public final static int ONEBOX_TAB_CONTENT_BG_COLOR = 96;
    
    public final static int TEXT_COLOR_SETTINGS_ITEM_VALUE = 97;
    
    public final static int TEXT_COLOR_AC_TAB_BUTTON_FOCUSED = 98;
    
    public final static int TEXT_COLOR_AC_TAB_BUTTON_UNFOCUSED = 99;
    
    public final static int MOVING_MAP_BUTTOM_BAR_BG_COLOR = 100;

    public final static int MOVING_MAP_BUTTOM_BAR_LINE_COLOR = 101;

    public final static int LOCKOUT_BACKGROUND_COLOR = 102;
    
    public final static int LOCKOUT_TEXT_COLOR = 103;
    
    public final static int TEXT_COLOR_TEXTFIELD = 104;
    
    public final static int TEXT_COLOR_TRAFFIC_DISABLED = 105;
    
    public final static int TURN_MAP_INFO_COLOR = 106;
    
    public final static int POI_RESULT_FEEDBACK_BACKGROUND_COLOR = 107;
    
    public final static int MY_PROFILE_ITEM_TITLE_COLOR = 108;
    
    public final static int MY_PROFILE_TITLE_SEPERATOR_COLOR = 109;
    
    public final static int MY_PROFILE_ITEM_SEPERATOR_COLOR = 110;
    
    public final static int MY_PROFILE_ITEM_FOCUSED_BG_COLOR = 111;
    
    public final static int TEXT_ITEM_VALUE_COLOR = 113;
    
    public final static int TEXT_ITEM_DISABLE_COLOR = 114;
    
    public final static int MY_PROFILE_SUBSCRIPTIONINFO_COLOR = 115; 
    
    public final static int DASHBOARD_DRIVE_SETUP_COLOR = 116;
    
    public final static int UPSELL_LEARNING_ITEM_UNFOCUSED_COLOR = 117;
    
    public final static int UPSELL_LEARNING_ITEM_FOCUSED_COLOR = 118;
    
    public final static int SHARE_ETA_COLOR= 119;
    
    public final static int DRIVE_TEXT_COLOR = 120;
    
    public final static int ROUTE_PLAN_INFO_COLOR = 121;
    
    public final static int ROUTE_PLAN_BOTTOM_CONTAINER_COLOR_LANDSCAPE = 122;
    
    public final static int ROUTE_PLAN_BOTTOM_CONTAINER_COLOR_PORTRAIT = 123;
    
    public final static int SUMMARY_PROFILE_CONTAINER_BG_COLOR = 124;
    
    public final static int TRAFFIC_SUMMERY_BUTTON_ENABLED_COLOR = 125;
    
    public final static int TRAFFIC_SUMMERY_BUTTON_DISABLED_COLOR = 126;
    
    public final static int POI_CATEGORY_TEXT_COLOR = 127;
    
    public final static int POI_CATEGORY_BG_COLOR = 128;
    
    public final static int SUMMARY_TITLE_CONTAINER_COLOR = 129;

    public final static int TEXT_COLOR_TIGHT_STREET_NAME = 130;

    public final static int DASHBOARD_ADDRESS_BUTTON_UNFOCUSED_COLOR = 131;
    
    public final static int SUMMARY_TABLE_TITLE_COLOR = 132;
    
    public final static int TEXT_COLOR_TAB_BUTTON_DISABLED = 133;

    public final static int DASHBOARD_RESUME_TRIP_ADDRESS_COLOR = 134;
    
    public final static int BG_COLOR_TIGHT_TURN = 135;
    
    public final static int TEXT_COLOR_BUBBLE_BUTTON_DISABLED = 136;
    
    public final static int TEXT_SHARE_ETA_DISABLED = 137;
    
    public final static int BG_LOCAL_EVENT_PANEL = 138;
    
    public final static int BG_CATEGORY_PANEL = 139;
    
    public final static int POI_LOCAL_EVENT_TEXT_COLOR = 140;
    
    //---------------------------------------font style id----------------------------------------------//
    /*******************************************************************************/
    /*******************************************************************************/
    /***********************    common font case ***********************************/
    /*******************************************************************************/
    /*******************************************************************************/
    
    public final static int FONT_COMMON_BASE = 1000;
    
    public final static int FONT_SCREEN_TITLE = FONT_COMMON_BASE + 0;
    
    public final static int FONT_BOTTOM_BAR = FONT_COMMON_BASE + 1;

    public final static int FONT_BUTTON = FONT_COMMON_BASE + 2;
    
    public final static int FONT_LABEL = FONT_COMMON_BASE + 3;

    public final static int FONT_LINK_ICON_LABEL = FONT_COMMON_BASE + 4;
    
    public final static int FONT_LIST_SINGLE = FONT_COMMON_BASE + 5;
    
    
    public final static int FONT_LIST_DUAL_LINE_TOP = FONT_COMMON_BASE + 6;
    
    public final static int FONT_LIST_DUAL_LINE_BOTTOM = FONT_COMMON_BASE + 7;
    
    
    public final static int FONT_LINK_TRI_LINE_FIRST = FONT_COMMON_BASE + 8;
    
    public final static int FONT_LINK_TRI_LINE_SECOND = FONT_COMMON_BASE + 9;
    
    public final static int FONT_LINK_TRI_LINE_RIGHT_TOP = FONT_COMMON_BASE + 10;
    
    
    public final static int FONT_TEXT_FIELD = FONT_COMMON_BASE + 11;
    
    
    public final static int FONT_RESULT_BAR_LABEL = FONT_COMMON_BASE + 12;
    
    public final static int FONT_RESULT_BAR_BTN = FONT_COMMON_BASE + 13;
    
    public final static int FONT_NAV_INFO_BAR = FONT_COMMON_BASE + 14;
    
    public final static int FONT_NAV_INFO_BAR_BOLD = FONT_COMMON_BASE + 15;
    
    public final static int FONT_NAV_INFO_BAR_BTN = FONT_COMMON_BASE + 17;
    
    public final static int FONT_TAB_FONT = FONT_COMMON_BASE + 18;
    
    public final static int FONT_MAP_POI_INFO_BOLD = FONT_COMMON_BASE + 19;
    
    public final static int FONT_MAP_POI_INFO = FONT_COMMON_BASE + 20;
    
    public final static int FONT_MAP_NAV_TURN_INFO_BOLD = FONT_COMMON_BASE + 21;
    
    public final static int FONT_MAP_NAV_TURN_INFO = FONT_COMMON_BASE + 22;
    
    public final static int FONT_POPUP_TITLE = FONT_COMMON_BASE + 24;
    
    public final static int FONT_DSR_EXAMPLE_TITLE = FONT_COMMON_BASE + 25;
    
    public final static int FONT_DSR_EXAMPLE = FONT_COMMON_BASE + 26;
    
    public final static int FONT_CHECK_BOX = FONT_COMMON_BASE + 27;
    
    public final static int FONT_MESSAGE_BOX = FONT_COMMON_BASE + 28;
    
    public final static int FONT_DETOUR_CONFIRM_CONTENT = FONT_COMMON_BASE + 29; 
    
    /*******************************************************************************/
    /*******************************************************************************/
    /***********************    special font case **********************************/
    /*******************************************************************************/
    /*******************************************************************************/
    private final static int FONT_SPECIAL_BASE = 2000;

    public final static int FONT_POI_INFO_PANEL_TITLE = FONT_SPECIAL_BASE + 1;
    
    public final static int FONT_POI_QUICK_FIND_BUTTON_TEXT = FONT_SPECIAL_BASE + 2;
    
    public final static int FONT_TRAFFIC_UPDATE_TITLE = FONT_SPECIAL_BASE + 4;
    
    public final static int FONT_TRAFFIC_INCIDENT = FONT_SPECIAL_BASE + 5;
    
    public final static int FONT_TRAFFIC_MINIMIZE_BUTTON = FONT_SPECIAL_BASE + 6;
    
    public final static int FONT_NAV_STREET_NAME = FONT_SPECIAL_BASE + 9;
    
    public final static int FONT_NAV_TURN_DIST_DIGIT = FONT_SPECIAL_BASE + 10;
    
    public final static int FONT_NAV_TRUN_DIST_UNIT = FONT_SPECIAL_BASE + 11;
    
    public final static int FONT_NAV_DIST_DIGIT = FONT_SPECIAL_BASE + 12;
    
    public final static int FONT_SUMMARY_SPEED_DIGIT = FONT_SPECIAL_BASE + 13;
    
    public final static int FONT_SUMMARY_SPEED_UNIT = FONT_SPECIAL_BASE + 14;
    
    public final static int FONT_SUMMARY_SPEED_NA = FONT_SPECIAL_BASE + 15;
    
    public final static int FONT_FAVORITE_ADDRESS_ITEM = FONT_SPECIAL_BASE + 16;
    
    public final static int FONT_TRAFFIC_SUMMARY_STREET_LENGTH = FONT_SPECIAL_BASE + 17;
    
    public final static int FONT_SPLASH_SCREEN = FONT_SPECIAL_BASE + 18;
    
    public final static int FONT_ROUTE_SUMMARY_STREET_NAME = FONT_SPECIAL_BASE + 19;
    
    public final static int FONT_ROUTE_SUMMARY_STREET_LENGTH = FONT_SPECIAL_BASE + 20;
    
    public final static int FONT_ROUTE_SUMMARY_TIME_INFO = FONT_SPECIAL_BASE + 21;
    
    public final static int FONT_ROUTE_SUMMARY_TITLE = FONT_SPECIAL_BASE + 22;

    public final static int FONT_TRAFFIC_AVERAGE_SPEED = FONT_SPECIAL_BASE + 23;

    public final static int FONT_ALTERNATE_ROUTE_TITLE_INFO = FONT_SPECIAL_BASE + 24;
    
    public final static int FONT_ALTERNATE_ROUTE_ADDRESS_LABEL = FONT_SPECIAL_BASE + 25;
    
    public final static int FONT_ROUTE_PLANNING_ROUTE_INFO = FONT_SPECIAL_BASE + 26;
    
    public final static int FONT_TRAFFIC_INCIDENT_NUMBER = FONT_SPECIAL_BASE + 28;
    
    public final static int FONT_SPEED_LIMIT = FONT_SPECIAL_BASE + 29;
    
    public final static int FONT_SPEED_LIMIT_NUMBER = FONT_SPECIAL_BASE + 30;
    
    public final static int FONT_ROUTE_PLANNING_COMPARE_TITLE_BOLD = FONT_SPECIAL_BASE + 31;
    
    public final static int FONT_ROUTE_PLANNING_COMPARE_BOTTOM_HUGE_UNFOCUSED = FONT_SPECIAL_BASE + 32;
    
    public final static int FONT_ROUTE_PLANNING_COMPARE_BOTTOM_MEDIUM_UNFOCUSED = FONT_SPECIAL_BASE + 33;
    
    public final static int FONT_ROUTE_PLANNING_COMPARE_BOTTOM_HUGE_FOCUSED = FONT_SPECIAL_BASE + 34;
    
    public final static int FONT_ROUTE_PLANNING_COMPARE_BOTTOM_MEDIUM_FOCUSED = FONT_SPECIAL_BASE + 35;

    public final static int FONT_BADGE = FONT_SPECIAL_BASE + 36;
    
    public final static int FONT_ROUTE_PLANNING_POPUP_TITLE_SMALL = FONT_SPECIAL_BASE + 37;
    
    public final static int FONT_TRAFFIC_INCIDENT_BOLD = FONT_SPECIAL_BASE + 38;
    
    public final static int FONT_TRAFFIC_SUMMARY_STREET_NAME = FONT_SPECIAL_BASE + 39;
    
    public final static int FONT_BOTTOM_BUTTON_SMALL = FONT_SPECIAL_BASE + 40;
    
    public final static int FONT_NAV_SCREEN_BOTTOM_STREET_NAME = FONT_SPECIAL_BASE + 43;

    public final static int FONT_NAV_SCREEN_BOTTOM_STREET_NAME_EXTRA_INFO = FONT_SPECIAL_BASE + 44;
    
    public final static int FONT_NAV_SCREEN_TRAFFIC_BUTTON = FONT_SPECIAL_BASE + 45;
    
    public final static int FONT_POI_LIST_ITEM_INDEX = FONT_SPECIAL_BASE + 46;
    
    public final static int FONT_POI_ICON_NUM = FONT_SPECIAL_BASE + 47;
    
    public final static int FONT_TAB_FONT_BOLD = FONT_SPECIAL_BASE + 49;
    
    public final static int FONT_FAV_CATEGORY_NUMBER = FONT_SPECIAL_BASE + 50;
    
    public final static int FONT_MAP_PROVIDER = FONT_SPECIAL_BASE + 51;
    
    public final static int FONT_ROUTE_SETTING_BUTTON = FONT_SPECIAL_BASE + 52;
    
    public final static int FONT_POI_SEARCH_SINGLE_CATEGORY = FONT_SPECIAL_BASE + 53;
    
    public final static int FONT_POI_SEARCH_SUB_CATEGORY_TITLE = FONT_SPECIAL_BASE + 54;

    public final static int FONT_ALTERNATE_ROUTE_ADDRESS_INFO = FONT_SPECIAL_BASE + 55;

    public final static int FONT_SPLASH_SCREEN_SMALL = FONT_SPECIAL_BASE + 56;
    
    public final static int FONT_POI_RESULT_LOCATION_LABEL = FONT_SPECIAL_BASE + 57;
    
    public final static int FONT_AC_BUTTON = FONT_SPECIAL_BASE + 58;
    
    public final static int FONT_AC_SWITCH_REGION_COMBO_FONT = FONT_SPECIAL_BASE + 59;
    
    public final static int FONT_TAB_TRAFFIC_SUMMARY = FONT_SPECIAL_BASE + 60;
    
    public final static int FONT_LOCKOUT_TEXT = FONT_SPECIAL_BASE + 61;
    
    public final static int FONT_SECRET_KEY_LABEL = FONT_SPECIAL_BASE + 62;
    
    public final static int FONT_SECRET_KEY_INFO_LABEL = FONT_SPECIAL_BASE + 63;
    
    public final static int FONT_SECRET_KEY_INFO_LABEL_SELECTED = FONT_SPECIAL_BASE + 64;
    
    public final static int FONT_DASHBOARD_LOCATING_LABEL = FONT_SPECIAL_BASE + 65;
    
    public final static int FONT_DASHBOARD_LOCATION_CITY_LABEL = FONT_SPECIAL_BASE + 66;
    
    public final static int FONT_DASHBOARD_LOCATION_STREET_LABEL = FONT_SPECIAL_BASE + 67;
    
    public final static int FONT_DASHBOARD_WEATHER_FONT = FONT_SPECIAL_BASE + 68;
    
    public final static int FONT_DASHBOARD_HOME_WORK_TIME_TITLE = FONT_SPECIAL_BASE + 69;
    
    public final static int FONT_DASHBOARD_HOME_WORK_TIME = FONT_SPECIAL_BASE + 70;

    public final static int FONT_DASHBOARD_HOME_WORK_LABEL = FONT_SPECIAL_BASE + 71;
 
    public final static int FONT_DASHBOARD_CALCULATING = FONT_SPECIAL_BASE + 72;
    
    public final static int FONT_DASHBOARD_RESUMETRIP_TIME = FONT_SPECIAL_BASE + 73;
    
    public final static int FONT_DASHBOARD_RESUMETRIP_BIG_STRING = FONT_SPECIAL_BASE + 74;
    
    public final static int FONT_DASHBOARD_RESUMETRIP_SMALL_STRING = FONT_SPECIAL_BASE + 75;
    
    public final static int FONT_DASHBOARD_RESUMETRIP_CALCULATING = FONT_SPECIAL_BASE + 76;
    
    public final static int FONT_DASHBOARD_FEEDBACK_BIG_STRING = FONT_SPECIAL_BASE + 77;
    
    public final static int FONT_DASHBOARD_FEEDBACK_SMALL_STRING = FONT_SPECIAL_BASE + 78;
    
    public final static int FONT_DASHBOARD_TITLE = FONT_SPECIAL_BASE + 79;
    
    public final static int FONT_DASHBOARD_BOLD_TITLE = FONT_SPECIAL_BASE + 80;
    
    public final static int FONT_ADDRESS_BUTTON_UNFOCUSED = FONT_SPECIAL_BASE + 81;
    
    public final static int FONT_FTUE_UNDERLINE_STRING = FONT_SPECIAL_BASE + 82;

    public final static int FONT_MY_PROFILE_ITEM_TITLE = FONT_SPECIAL_BASE + 83;
    
    public final static int FONT_MY_PROFILE_ITEM = FONT_SPECIAL_BASE + 84;

    public final static int FONT_MY_PROFILE_VALUE = FONT_SPECIAL_BASE + 85;
    
    public final static int FONT_MY_PROFILE_BUTTON = FONT_SPECIAL_BASE + 86;

    public final static int FONT_UPSELL_ITEM_TITLE = FONT_SPECIAL_BASE + 87;
    
    public final static int FONT_UPSELL_ITEM_CONTENT = FONT_SPECIAL_BASE + 88;
    
    public final static int FONT_ROUTE_PLANNING_DISTANCE = FONT_SPECIAL_BASE + 89;
    
    public final static int FONT_ROUTE_PLANNING_ETA = FONT_SPECIAL_BASE + 90;
    
    public final static int FONT_ROUTE_PLANNING_SHARE_ETA = FONT_SPECIAL_BASE + 91;
    
    public final static int FONT_ROUTE_PLANNING_NAVIAGATION = FONT_SPECIAL_BASE + 92;
    
    public final static int FONT_POI_CATEGORY_BUTTON_TEXT =  FONT_SPECIAL_BASE + 93;

    public final static int FONT_NAV_DIST_UNIT = FONT_SPECIAL_BASE + 94;
    
    public final static int FONT_SMALL_BADGE = FONT_SPECIAL_BASE + 95;

    public final static int FONT_TIGHT_TURN_NAME = FONT_SPECIAL_BASE + 96;
    
    public final static int FONT_POI_CATEGORY_TITLE = FONT_SPECIAL_BASE + 97;
    
    public final static int FONT_POI_LOCAL_EVENT_BUTTON_TEXT =  FONT_SPECIAL_BASE + 98;
    
    public final static int FONT_PROFILE_BADGE =  FONT_SPECIAL_BASE + 99;
    
    public final static int FONT_HOME_RECIPIENT_HINT_TEXT =  FONT_SPECIAL_BASE + 100;
    
    public final static int FONT_FRIEND_BUBBLE_TEXT =  FONT_SPECIAL_BASE + 101;
    
    public final static int FONT_FRIEND_BUBBLE_TEXT_MOVING_MAP =  FONT_SPECIAL_BASE + 102;
    
    protected static UiStyleManager instance;
    protected Hashtable fontTable;
    
    protected UiStyleManager()
    {

    }
    
    public static void init(UiStyleManager uiStyleMngr)
    {
        instance = uiStyleMngr;
    }

    public static UiStyleManager getInstance()
    {
        return instance;
    }
    
    protected AbstractTnFont createFont(int family, int fontStyle, int fontSize)
    {
        if(fontTable == null)
        {
            fontTable = new Hashtable();
        }
        int realSize = this.calcFontSizeByDensity(fontSize);
        /*
         *|00|family|style|size| 
         *31 24     15    7    0
         */
        int key = 0x00000000 | (0xFF&family)<<16 | (0xFF&fontStyle)<<8 | 0xFF&realSize;
        AbstractTnFont font= null;
        Integer keyInt = PrimitiveTypeCache.valueOf(key);
        if(fontTable != null)
        {
            font = (AbstractTnFont)this.fontTable.get(keyInt);
        }
        if(font == null)
        {
            font = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).createFont(family, fontStyle, realSize);
            this.fontTable.put(keyInt, font);
        }
        return font;
    }
    
    protected int calcFontSizeByDensity(int hugeDensityFontSize)
    {
        float densityRatio =  ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDensity();
        if(densityRatio <= 0)
        {
            densityRatio = 1;
        }
        
        int targetFontSize = (int)(hugeDensityFontSize * ((AndroidUiHelper)AbstractTnUiHelper.getInstance()).getVisionRatio() / 1.5);
        
        // example: for Xoom, visionRatio = 2.5 but density = 1.0 
        if (((AndroidUiHelper)AbstractTnUiHelper.getInstance()).getVisionRatio() > 2 * densityRatio)
        {
            targetFontSize *= 1 - (((AndroidUiHelper)AbstractTnUiHelper.getInstance()).getVisionRatio() - densityRatio) / 10;
        }
        
        return targetFontSize;   
    }
    
    public abstract int getColor(int styleId);
    
    public abstract AbstractTnFont getFont(int styleId);
    
    HashMap componentNeeded = new HashMap<Integer, Boolean>();
    public boolean isComponentNeeded(int backGroundImageId)
    {
        boolean result = false;
        if(!componentNeeded.containsKey(PrimitiveTypeCache.valueOf(backGroundImageId)))
        {
            String name = (String) INinePatchImageRes.NINE_PATCH_IMAGES.get(
                PrimitiveTypeCache.valueOf(backGroundImageId));
            result = ResourceUtil.isImageFileExist(name,
                INinePatchImageRes.FAMILY_NINE_PATCH, "generic");
            componentNeeded.put(PrimitiveTypeCache.valueOf(backGroundImageId), result);
            
        }
        else
        {
            result = ((Boolean) componentNeeded.get(PrimitiveTypeCache.valueOf(backGroundImageId))).booleanValue();
        }
        return result;
    }
}

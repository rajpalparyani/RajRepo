/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ScoutUiStyleManager.java
 *
 */
package com.telenav.ui;

import com.telenav.module.AppConfigHelper;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.TnColor;

/**
 *@author qli
 *@date 2012-2-13
 */
public class ScoutUiStyleManager extends UiStyleManager
{
    protected TnDeviceInfo deviceInfo;
    
    protected static final String MOTO_ATRIX = "olympus";

    public ScoutUiStyleManager()
    {
        
    }
    
    public int getColor(int styleId)
    {
        switch(styleId)
        {

            case TEXT_COLOR_WH:
            {
                return 0xFFFFFFFF;
            }
            case TEXT_COLOR_DA_BL:
            {
                return 0xFF067AB4;
            }
            case TEXT_COLOR_OR:
            {
                return 0xFFFF7200;
            }
            case TEXT_COLOR_AT_GR:
            {
                return 0xFF6EBB1F;
            }
            case TEXT_COLOR_RED:
            {
                return 0xFFFF0000;
            }
            case TEXT_COLOR_SU_LI_GR:
            {
                return 0xFFCCCCCC;
            }
            case TEXT_COLOR_BL:
            {
                return 0xFF000000;
            }
            case TEXT_COLOR_LI_GR:
            {
                return 0xFF999999;
            }
            case TEXT_COLOR_TRAFFIC_DISABLED:
            {
                return 0xFFbdbfbf;
            }
            case TEXT_COLOR_ME_GR:
            {
                return 0xFF241F21;
            }
            case TEXT_COLOR_DA_GR:
            {
                return 0xFF333333;
            }
            case BG_COLOR_LI_FO:
            {
                return 0xFFDCDCDC;
            }
            case BG_COLOR_WH:
            {
                return 0xFFFFFFFF;
            }
            case BG_COLOR_TRANSPARENT:
            {
                return 0x00FFFFFF;
            }
            case EDIT_FAV_SUB_TITLE_COLOR:
            {
                return 0xFF6e6e70;
            }
            case EDIT_FAV_SUB_TITLE_TEXT_COLOR:
            {
                return this.getColor(TEXT_COLOR_WH);
            }
			case POI_ICON_PANEL_COLOR:
            {
                return 0xFF241F21;
            }
            case ICON_BUTTON_TEXT_COLOR:
            {
                return 0xFFBDBFBF;
            }
            case BOTTOM_BAR_COLOR_UNFOCUS:
            {
                return 0xFF9a9a9a;
            }
            case BOTTOM_BAR_COLOR_FOCUS:
            {
                return 0xFFFFFFFF;
            }
            case BG_SUBTITLE_BAR:
            {
                return 0xFFDDDDDD;
            }
            case BG_NAV_TITLE:
            {
                return 0xFF161e24;
            }
            case NAV_ALTERNATE_ROUTE_TITLE_COLOR:
            {
                return 0xFF333333;
            }
            case NAV_ALTERNATE_ROUTE_BOTTOM_COLOR:
            {
                return 0xFF241f21;
            }
            case TEXT_SUGGESTED_ROUTE_DEST_ABBR_COLOR:
            {
                return 0xFFFFCD15;
            }
            case TEXT_SUGGESTED_ROUTE_DEST_DETAIL_COLOR:
            {
                return 0xFFFFCD15;//Per UI, it is the same as the color of the title old is 0xFFFFA134
            }
            case BG_NAV_PROGRESS:
            {
                return 0xFF247B08;
            }
            case BG_SUMMARY_ITEM_TITLE_TOP:
            {
                return 0xFFE0E0E0;
            }
            case BG_SUMMARY_ITEM_TITLE_BOTTOM:
            {
                return 0xFF6E6E70;
            }
            case BG_SUMMARY_TIME_INFO:
            {
                return 0xFF161e24;
            }
            case TEXT_SUMMARY_TIME_INFO:
            {
                return 0xFFFFFFFF;
            }
            case TEXT_COLOR_BLUE:
            {
                return 0xFF006699;
            }
            case TEXT_COLOR_BUBBLE_TITLE_FOCUSED:
            {
                return 0xFFFFA134;
            }
            case TEXT_COLOR_BUBBLE_TITLE_UNFOCUSED:
            {
                return 0xFFFFCD15;
            }
            case TEXT_COLOR_BUBBLE_ADDRESS_TITLE_FOCUSED:
            {
                return 0xFFFFA134;
            }
            case TEXT_COLOR_BUBBLE_ADDRESS_TITLE_UNFOCUSED:
            {
                return 0xFFFFCD15;
            }
            case TEXT_COLOR_BUBBLE_CONTENT_FOCUSED:
            {
                return 0xFFFFA134;
            }
            case TEXT_COLOR_BUBBLE_CONTENT_UNFOCUSED:
            {
                return 0xFFFFCD15;
            }
            case TEXT_COLOR_BUBBLE_BUTTON_FOCUSED:
            {
                return 0xFEDD00;
            }
            case TEXT_COLOR_BUBBLE_BUTTON_UNFOCUSED:
            {
                return 0xFFFFFFFF;
            }
            case TEXT_COLOR_BUBBLE_BUTTON_DISABLED:
            {
                return 0xFF666666;
            }
            case TEXT_COLOR_BUBBLE_ADDRESS_BUTTON_FOCUSED:
            {
                return 0xFEDD00;
            }
            case TEXT_COLOR_BUBBLE_ADDRESS_BUTTON_UNFOCUSED:
            {
                return 0xFFFFFFFF;
            }
            case TEXT_COLOR_FILTER:
            {
                return 0xFF999999;
            }
            case MAP_PROVIDER_TEXT_DAY_COLOR:
            {
                return 0x676767;
            }
            case MAP_PROVIDER_TEXT_NIGHT_COLOR:
            {
                return 0x676767;
            }
            case TEXT_COLOR_LABEL:
            {
                if (AppConfigHelper.isTabletSize())
                {
                    return 0xFF666666;
                }
                else
                {
                    return 0xFF000000;
                } 
            }
            case DASHBOARD_TITLE_BACKGROUND_COLOR:
            {
                return TnColor.TRANSPARENT;
            }
            case DASHBOARD_SPLIT_LINE_COLOR:
            {
                return 0xFF464646;
            }
            case DASHBOARD_HIGHLIGHT_UNFOCUSED_COLOR:
            {
                return 0xFFFFCD15;
            }
            case DASHBOARD_HIGHLIGHT_FOCUSED_COLOR:
            {
                return 0xFFFFA134;
            }
            case DASHBOARD_LOCATION_CITY_COLOR:
            {
                return 0xFFE5E4E4;
            }
            case DASHBOARD_LOCATION_STREET_COLOR:
            {
                return 0xFF8a8a8a;
            }
            case DASHBOARD_NORMAL_STRING_COLOR:
            {
                return 0xFFE5E4E4;
            }
            case DASHBOARD_DRIVE_SETUP_COLOR:
            {
                return 0xFF858585;
            }
            case DASHBOARD_NEARBY_TIME_COLOR:
            {
                return 0xFF7b7b7b;
            }
            case DASHBOARD_GREEN_TIME_COLOR:
            {
                return this.getColor(DASHBOARD_NORMAL_STRING_COLOR);
            }
            case DASHBOARD_ORANGE_TIME_COLOR:
            {
                return this.getColor(DASHBOARD_NORMAL_STRING_COLOR);
            }
            case DASHBOARD_RED_TIME_COLOR:
            {
                return this.getColor(DASHBOARD_NORMAL_STRING_COLOR);
            }
            case CLOCK_ANIMATION_DEFAULT_COLOR:
            {
                return 0x00999999;
            }
            case SCREEN_TITLE_BG_COLOR:
            {
                return this.getColor(TEXT_COLOR_ME_GR);
            }
            case SCREEN_FILTER_BG_COLOR:
            case SEARCH_RESULT_TITLE_BG_COLOR:
            {
                return this.getColor(TEXT_COLOR_ME_GR);
            }
            case SCREEN_BOTTOM_BG_COLOR:
            {
                return this.getColor(TEXT_COLOR_ME_GR);
            }
            case AC_BUTTON_BACKGROUND:
            {
                return 0xFF241F21;
            }
            case AC_BUTTON_FOCUSED_FOREGROUND:
            {
                return 0xFFFFA134;
            }
            case AC_BUTTON_UNFOCUSED_FOREGROUND:
            {
                return 0xFF8D8D8D;
            }
            case LIST_ITEM_TITLE_UNFOCUS_COLOR:
            {
                return 0xFF241F21;
            }
            case LIST_ITEM_TITLE_FOCUSED_COLOR:
            {
                return 0xFFFFFFFF;
            }
            case TEXT_POI_RESULT_INDEX_NUM_UNFOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_BL);
            }
            case TEXT_POI_RESULT_INDEX_NUM_FOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_BL);
            }
            case TEXT_POI_RESULT_ITEM_TITLE_UNFOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_BL);
            }
            case TEXT_POI_RESULT_ITEM_TITLE_FOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_WH);
            }
            case TEXT_POI_RESULT_AD_ITEM_SHORT_MESSAGE_UNFOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_BL);
            }
            case TEXT_POI_RESULT_AD_ITEM_SHORT_MESSAGE_FOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_WH);
            }
            case TEXT_POI_RESULT_COMBOX_UNFOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_WH);
            }
            case TEXT_POI_RESULT_COMBOX_FOCUSED_COLOR:
            {
                return this.getColor(TEXT_COLOR_WH);
            }
            case ROUTE_PLANING_CHOICE_ITEM_A_COLOR:
            {
                return 0xFF67BC4C;
            }
            case ROUTE_PLANING_CHOICE_ITEM_B_COLOR:
            {
                return 0xFF79A4D6;
            }
            case ROUTE_PLANING_CHOICE_ITEM_C_COLOR:
            {
                return 0xFFCA5376;
            }
            case NAV_TOP_SEPARATER_LINE_COLOR:
            {
                return 0xFF928F90;
            }
            case BUTTON_DEFAULT_UNFOCUSED_COLOR:
            {
                return 0XFF505C67;
            }
            case BUTTON_DEFAULT_FOCUSED_COLOR:
            {
                return 0XFF505C67;
            }
            case NAV_TRAFFIC_BUTTON_COLOR:
            {
                return 0xFFFFFFFF;
            }
            case TOC_BOTTOM_BUTTON_TEXT_FOCUSED_COLOR:
            {
                return 0xFFFFFFFF;
            }
            case TOC_BOTTOM_BUTTON_TEXT_UNFOCUSED_COLOR:
            {
                return 0xFFFFFFFF;
            }
            case TOC_BOTTOM_ACCEPT_BUTTON_TEXT_UNFOCUSED_COLOR:
            {
                return 0xFF241F21;
            }
            case TEXT_COLOR_BUTTON_DISABLED:
            {
                return 0xFF999999;
            }
            case TOC_BOTTOM_BUTTON_CONTAINER_BACK_GROUND_COLOR:
            {
                return 0xFF241F21;
            }
            case TEXT_MAP_POI_ANNOTATION_COLOR:
            {
                return 0xFF000000;
            }
            case POI_RESULTS_SEARCH_TITLE_LABEL_COLOR:
            {
                return 0xFFFFFFFF;
            }
            case POI_RESULTS_LOCATION_LABEL_COLOR:
            {
                return 0xFFFFFFFF;
            }
            case TEXT_COLOR_TAB_BUTTON_FOCUSED:
            {
                return 0xFF4a657c;
            }
            case TEXT_COLOR_TAB_BUTTON_UNFOCUSED:
            {
                return 0xFF4a657c;
            }
            case TEXT_COLOR_TAB_BUTTON_DISABLED:
            {
                return 0XFF768da1;
            }
            case POI_CATEGORY_TITLE_BACKGROUND:
            {
                return 0xFF241F21;
            }
            case POI_CATEGORY_TEXTFIELD_BACKGROUND:
            {
                return 0xFF241F21;
            }
            case SYNCRES_PROGRESS_CONTAINER_BACKGROUND:
            {
                return 0xFF161e24;
            }
            case TEXT_COLOR_FEEDBACK_LINK:
            {
                return 0xFFffcd15;
            }
            case ONEBOX_TAB_CONTAINER_BG_COLOR:
            {
                return 0xFF241f21;
            }
            case ONEBOX_TAB_CONTENT_BG_COLOR:
            {
                return 0xFF241f21;
            }
            case TEXT_COLOR_SETTINGS_ITEM_VALUE:
            {
                return 0xFF666666;
            }
            case TEXT_COLOR_AC_TAB_BUTTON_FOCUSED:
            {
                return 0xFF241f21;
            }
            case TEXT_COLOR_AC_TAB_BUTTON_UNFOCUSED:
            {
                return 0xFFFFFFFF;
            }
            case MOVING_MAP_BUTTOM_BAR_BG_COLOR:
            {
                return 0xFF161e24;
            }
            case MOVING_MAP_BUTTOM_BAR_LINE_COLOR:
            {
                return 0xFF666666;
            }
            case LOCKOUT_BACKGROUND_COLOR:
            {
                return 0xFF241f21;
            }
            case LOCKOUT_TEXT_COLOR:
            {
                return 0xFFFFFFFF;
            }
            case TEXT_COLOR_TEXTFIELD:
            {
                return 0xFF666666;
            }
            case TURN_MAP_INFO_COLOR:
            {
                return this.getColor(TEXT_COLOR_WH);
            }
            case POI_RESULT_FEEDBACK_BACKGROUND_COLOR:
            {
                return 0xFF6E6E70;
            }
            case MY_PROFILE_ITEM_TITLE_COLOR:
            {
                return 0xFF717171;
            }
            case MY_PROFILE_TITLE_SEPERATOR_COLOR:
            {
                return 0xFFC8C8C8;
            }
            case MY_PROFILE_ITEM_SEPERATOR_COLOR:
            {
                return 0xFFDFDFDF;
            }
            case MY_PROFILE_ITEM_FOCUSED_BG_COLOR:
            {
                return 0xFF249EC7;
            }
            case MY_PROFILE_SUBSCRIPTIONINFO_COLOR:
            {
                return 0xFF505C67;
            }
            case TEXT_ITEM_VALUE_COLOR:
            {
                return 0xFF343434;
            }
            case TEXT_ITEM_DISABLE_COLOR:
            {
                return 0XFFB2B2B2;
            }
            case UPSELL_LEARNING_ITEM_UNFOCUSED_COLOR :
            {
                return 0XFF999999;
            }
            case UPSELL_LEARNING_ITEM_FOCUSED_COLOR :
            {
                return 0xFFFFA134;
            }
            case SHARE_ETA_COLOR :
            {
                return 0XFF505C67;
            }
            case DRIVE_TEXT_COLOR :
            {
                return 0xFF1A2228;
            }
            case ROUTE_PLAN_INFO_COLOR:
            {
                return 0XF1F0F0;
            }
            case ROUTE_PLAN_BOTTOM_CONTAINER_COLOR_LANDSCAPE:
            {
                return 0XFFC3C5C7;
            }
            case ROUTE_PLAN_BOTTOM_CONTAINER_COLOR_PORTRAIT:
            {
                return 0XFFDDDDDD;
            }
            case SUMMARY_PROFILE_CONTAINER_BG_COLOR:
            {
                return 0xFFF8F7F7;
            }
            case TRAFFIC_SUMMERY_BUTTON_ENABLED_COLOR:
            {
                return 0xFF4a657c;
            }
            case TRAFFIC_SUMMERY_BUTTON_DISABLED_COLOR:
            {
                return 0xFF768da1;
            }
            case POI_CATEGORY_TEXT_COLOR:
            {
                return 0xFFffd200;
            }
            case POI_CATEGORY_BG_COLOR:
            {
                return 0xFF161e24;
            }
            case SUMMARY_TITLE_CONTAINER_COLOR:
            {
                return 0xFF161e24;
            }
            case SUMMARY_TABLE_TITLE_COLOR:
            {
                return 0XFF788A9A;
            }
            case TEXT_COLOR_TIGHT_STREET_NAME:
            {
                return 0xFFC9CDD0;
            }
            case BG_COLOR_TIGHT_TURN:
            {
                return 0xFF3D4046;
            }
            case TEXT_SHARE_ETA_DISABLED:
            {
                return 0xFF939FAB;
            }
            case BG_LOCAL_EVENT_PANEL:
            {
                return 0xffffd200;
            }
            case BG_CATEGORY_PANEL:
            {
                return 0xff161e24;
            }
            case POI_LOCAL_EVENT_TEXT_COLOR:
            {
                return 0xff141b22;
            }
        }
        
        return 0xFFFF0000;
    }

    public AbstractTnFont getFont(int styleId)
    {
        switch(styleId)
        {
            /*******************************************************************************/
            /*******************************************************************************/
            /***********************    common font case ***********************************/
            /*******************************************************************************/
            /*******************************************************************************/
            case FONT_SCREEN_TITLE:
            {
                    return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 27);
            }
            case FONT_BOTTOM_BAR:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_BUTTON:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_LINK_ICON_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 16);
            }
            case FONT_LIST_SINGLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            
            case FONT_LIST_DUAL_LINE_TOP:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_LIST_DUAL_LINE_BOTTOM:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_LINK_TRI_LINE_FIRST:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_LINK_TRI_LINE_SECOND:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_LINK_TRI_LINE_RIGHT_TOP:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_TEXT_FIELD:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_RESULT_BAR_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_RESULT_BAR_BTN:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 18);
            }
            case FONT_NAV_INFO_BAR:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_NAV_INFO_BAR_BOLD:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_NAV_INFO_BAR_BTN:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_TAB_FONT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 26);
            }
            case FONT_MAP_POI_INFO_BOLD:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_MAP_POI_INFO:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_MAP_NAV_TURN_INFO_BOLD:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_MAP_NAV_TURN_INFO:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_POPUP_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 27);
            }
            case FONT_DSR_EXAMPLE_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_DSR_EXAMPLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_CHECK_BOX:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case  FONT_MESSAGE_BOX:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            
            
            
            
            
            
            
            
            
            
            
            
            
            /*******************************************************************************/
            /*******************************************************************************/
            /***********************    special font case **********************************/
            /*******************************************************************************/
            /*******************************************************************************/

            case FONT_POI_INFO_PANEL_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 21);
            }
            case FONT_POI_QUICK_FIND_BUTTON_TEXT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 17);
            }
            case FONT_TRAFFIC_UPDATE_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_NAV_STREET_NAME:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 40);
            }
            case FONT_NAV_TURN_DIST_DIGIT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 50);
            }
            case FONT_NAV_TRUN_DIST_UNIT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 28);
            }
            case FONT_NAV_DIST_DIGIT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 34);
            }
            case FONT_NAV_DIST_UNIT:
            {
                 return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 20);
            }
            case FONT_TRAFFIC_INCIDENT:
            {
                 return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_TRAFFIC_INCIDENT_BOLD:
            {
                 return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_TRAFFIC_MINIMIZE_BUTTON:
            {
                 return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 21);
            }
            case FONT_SUMMARY_SPEED_DIGIT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 30);
            }
            case FONT_SUMMARY_SPEED_UNIT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 18);
            }
            case FONT_SUMMARY_SPEED_NA:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 33);
            }
            case FONT_FAVORITE_ADDRESS_ITEM:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 16);
            }
            case FONT_TRAFFIC_SUMMARY_STREET_NAME:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 27);
            }
            case FONT_TRAFFIC_SUMMARY_STREET_LENGTH:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_SPLASH_SCREEN:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 13);
            }
            case FONT_SPLASH_SCREEN_SMALL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 12);
            }
            case FONT_ROUTE_SUMMARY_STREET_NAME:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_ROUTE_SUMMARY_STREET_LENGTH:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 21);
            }
            case FONT_ROUTE_SUMMARY_TIME_INFO:
            {
                if (AppConfigHelper.isTabletSize())
                {
                    return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 32);
                }
                else
                {
                    return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
                }
            }
            case FONT_ROUTE_SUMMARY_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 20);
            }
            case FONT_TRAFFIC_AVERAGE_SPEED:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_ALTERNATE_ROUTE_TITLE_INFO:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 18);
            }
            case FONT_ALTERNATE_ROUTE_ADDRESS_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 28);
            }
            case FONT_ALTERNATE_ROUTE_ADDRESS_INFO:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 22);
            }
            //-------------------------------------------------
            case FONT_ROUTE_PLANNING_ROUTE_INFO:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 21);
            }
            case FONT_TRAFFIC_INCIDENT_NUMBER:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 21);
            }
            case FONT_SPEED_LIMIT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 15);
            }
            case FONT_SPEED_LIMIT_NUMBER:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 34);
            }
            case FONT_ROUTE_PLANNING_COMPARE_TITLE_BOLD:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 22);
            }
            case FONT_ROUTE_PLANNING_COMPARE_BOTTOM_HUGE_UNFOCUSED:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 32);
            }
            case FONT_ROUTE_PLANNING_COMPARE_BOTTOM_MEDIUM_UNFOCUSED:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 28);
            }
            case FONT_ROUTE_PLANNING_COMPARE_BOTTOM_HUGE_FOCUSED:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 32);
            }
            case FONT_ROUTE_PLANNING_COMPARE_BOTTOM_MEDIUM_FOCUSED:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 28);
            }
            case FONT_BADGE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 18);
            }
            case FONT_SMALL_BADGE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 14);
            }
            case FONT_ROUTE_PLANNING_POPUP_TITLE_SMALL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_BOTTOM_BUTTON_SMALL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 18);
            }
            case FONT_NAV_SCREEN_BOTTOM_STREET_NAME:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 28);
            }
            case FONT_NAV_SCREEN_BOTTOM_STREET_NAME_EXTRA_INFO:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 15);
            }
            case FONT_POI_LIST_ITEM_INDEX:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 15);
            }
            case FONT_NAV_SCREEN_TRAFFIC_BUTTON:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 18);
            }
            case FONT_POI_ICON_NUM:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 18);
            }
            case FONT_TAB_FONT_BOLD:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 21);
            }
            case FONT_FAV_CATEGORY_NUMBER:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_MAP_PROVIDER:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 14);
            }
            case FONT_ROUTE_SETTING_BUTTON:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 18);
            }
            case FONT_POI_SEARCH_SINGLE_CATEGORY:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_POI_SEARCH_SUB_CATEGORY_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_POI_RESULT_LOCATION_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 26);
            }
            case FONT_AC_BUTTON:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_AC_SWITCH_REGION_COMBO_FONT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_TAB_TRAFFIC_SUMMARY:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 26);
            }
            case FONT_LOCKOUT_TEXT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 38);
            }
            case FONT_SECRET_KEY_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_SECRET_KEY_INFO_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 16);
            }
            case FONT_SECRET_KEY_INFO_LABEL_SELECTED:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 16);
            }
            case FONT_DETOUR_CONFIRM_CONTENT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_DASHBOARD_LOCATING_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 33);
            }
            case FONT_DASHBOARD_LOCATION_CITY_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 46);
            }
            case FONT_DASHBOARD_LOCATION_STREET_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 24);
            }
            case FONT_DASHBOARD_WEATHER_FONT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 20);
            }
            case FONT_DASHBOARD_HOME_WORK_TIME_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 22);
            }
            case FONT_DASHBOARD_HOME_WORK_TIME:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 60);
            }
            case FONT_DASHBOARD_HOME_WORK_LABEL:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 46);
            }
            case FONT_DASHBOARD_CALCULATING:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 33);
            }
            case FONT_DASHBOARD_RESUMETRIP_TIME:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 50);
            }
            case FONT_DASHBOARD_RESUMETRIP_BIG_STRING:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 29);
            }
            case FONT_DASHBOARD_RESUMETRIP_SMALL_STRING:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 17);
            }
            case FONT_DASHBOARD_RESUMETRIP_CALCULATING:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 26);
            }
            case FONT_DASHBOARD_FEEDBACK_BIG_STRING:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 30);
            }
            case FONT_DASHBOARD_FEEDBACK_SMALL_STRING:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 21);
            }
            case FONT_FTUE_UNDERLINE_STRING:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_UNDERLINED, 27);
            }
            case FONT_MY_PROFILE_ITEM_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 18);
            }
            case FONT_MY_PROFILE_ITEM:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 26);
            }
            case FONT_MY_PROFILE_VALUE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 18);
            }
            case FONT_MY_PROFILE_BUTTON:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 26);
            }
            case FONT_UPSELL_ITEM_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_UPSELL_ITEM_CONTENT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 20);
            }
            
            case FONT_ROUTE_PLANNING_DISTANCE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 23);
            }
            case FONT_ROUTE_PLANNING_ETA:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 31);
            }
            case FONT_ROUTE_PLANNING_SHARE_ETA:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 24);
            }
            case FONT_ROUTE_PLANNING_NAVIAGATION:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 28);
            }
            case FONT_POI_CATEGORY_BUTTON_TEXT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 18);
            }
            case FONT_TIGHT_TURN_NAME:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 23);
            }
            case FONT_POI_CATEGORY_TITLE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 20);
            }
            case FONT_POI_LOCAL_EVENT_BUTTON_TEXT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 18);
            }
            case FONT_PROFILE_BADGE:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 14);
            }
            case FONT_HOME_RECIPIENT_HINT_TEXT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 23); 
            }
            case FONT_FRIEND_BUBBLE_TEXT:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 17); 
            }
            case FONT_FRIEND_BUBBLE_TEXT_MOVING_MAP:
            {
                return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_BOLD, 8); 
            }
        }
        
        return createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, 36);
    }
}

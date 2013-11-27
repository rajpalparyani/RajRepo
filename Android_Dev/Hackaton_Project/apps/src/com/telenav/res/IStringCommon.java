/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStringCommon.java
 *
 */
package com.telenav.res;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public interface IStringCommon
{
    //=====================================common family=====================================//
    public final static String FAMILY_COMMON = "common";
    public final static int COMMON_STR_BASE     = 5000;

    //common string res id:    
    public final static int RES_BTTN_OK = COMMON_STR_BASE + 1;
    public final static int RES_BTTN_BACK = COMMON_STR_BASE + 2;
    public final static int RES_BTTN_CANCEL = COMMON_STR_BASE + 3;
    public final static int RES_BTTN_YES = COMMON_STR_BASE + 4;
    public final static int RES_BTTN_NO = COMMON_STR_BASE + 5;
    public final static int RES_BTTN_DELETE = COMMON_STR_BASE + 6;
    public final static int RES_BTTN_SAVE = COMMON_STR_BASE + 7;
    public final static int RES_BTTN_EXIT = COMMON_STR_BASE + 8;
    public final static int RES_BTTN_DONE = COMMON_STR_BASE + 9;
    public final static int RES_BTTN_GO = COMMON_STR_BASE + 10;
    public final static int RES_BTTN_ACCEPT = COMMON_STR_BASE + 11;
    public final static int RES_LABEL_LOADING = COMMON_STR_BASE + 12;
    public final static int RES_BTTN_RETRY = COMMON_STR_BASE + 13;
    public final static int RES_BTTN_EDIT = COMMON_STR_BASE + 14;
    public final static int RES_MENU_CALL = COMMON_STR_BASE + 15;
    public final static int RES_MENU_RATE = COMMON_STR_BASE + 16;
    
    //This string is used to locate the base line we draw most text.
    //Wee align these common letters vertical center in the given rect, and calculate the base line level. 
    //We suggest only use Digital and Capital letters.
    //This means we should not include those have part below the base line.
    //That means we use this common baseline to draw text with part bellow base line('p', 'q', 'Q'), the bottom part will not be centered as part of the whole
    public final static int RES_STANDARD_BASELINE_ANCHOR = COMMON_STR_BASE + 17;
    
    public final static int RES_SERVER_ERROR = COMMON_STR_BASE + 20;
    public final static int RES_GPS_ERROR = COMMON_STR_BASE + 21;
    public final static int RES_GETTING_GPS = COMMON_STR_BASE + 22;
    public final static int RES_NO_CELL_COVERAGE = COMMON_STR_BASE + 23;
    public final static int RES_GPS_NOT_AVAILABLE = COMMON_STR_BASE + 24;
    public final static int RES_DWF_SESSION_EXPIRED = COMMON_STR_BASE + 25;
    
    public final static int RES_KPH = COMMON_STR_BASE + 50;
    public final static int RES_MPH = COMMON_STR_BASE + 51;
    public final static int RES_KILOMETER = COMMON_STR_BASE + 52;
    public final static int RES_MILE = COMMON_STR_BASE + 53;
    public final static int RES_METER = COMMON_STR_BASE + 54;
    public final static int RES_FEET = COMMON_STR_BASE + 55;
    public final static int RES_NORTH = COMMON_STR_BASE + 56;
    public final static int RES_NORTH_EAST = COMMON_STR_BASE + 57;
    public final static int RES_EAST = COMMON_STR_BASE + 58;
    public final static int RES_SOUTH_EAST = COMMON_STR_BASE + 59;
    public final static int RES_SOUTH = COMMON_STR_BASE + 60;
    public final static int RES_SOUTH_WEST = COMMON_STR_BASE + 61;
    public final static int RES_WEST = COMMON_STR_BASE + 62;
    public final static int RES_NORTH_WEST = COMMON_STR_BASE + 63;
    
    public final static int RES_LAT = COMMON_STR_BASE + 70;
    public final static int RES_LON = COMMON_STR_BASE + 71;
    
    public final static int RES_BTTN_MAP = COMMON_STR_BASE + 100;
    public final static int RES_BTTN_DRIVETO = COMMON_STR_BASE + 101;
    public final static int RES_MENU_MAP = COMMON_STR_BASE + 102;
    public final static int RES_BTTN_SEARCH = COMMON_STR_BASE + 103;
    public final static int RES_BTTN_APP = COMMON_STR_BASE + 104;
    public final static int RES_BTTN_LIST = COMMON_STR_BASE + 105;
    public final static int RES_BTTN_TRAFFIC = COMMON_STR_BASE + 106;
    public final static int RES_BTTN_EXTRAS = COMMON_STR_BASE + 107;    
    public final static int RES_CURRENT_LOCATION = COMMON_STR_BASE + 108;
    public final static int RES_SETTING_MENU = COMMON_STR_BASE + 109;

    public final static int RES_FEEDBACK_MENU = COMMON_STR_BASE + 110;

    public final static int RES_INPUT_HINT = COMMON_STR_BASE + 111;
    public final static int RES_SEARCHING = COMMON_STR_BASE + 112;
    public final static int RES_RECEIVED_CATEGORY = COMMON_STR_BASE + 113;
    public final static int RES_SHARE = COMMON_STR_BASE + 114;
    
    public final static int RES_NO_BT_SUPPORT = COMMON_STR_BASE + 115;
    public final static int RES_BT_ON = COMMON_STR_BASE + 116;
    public final static int RES_BT_OFF = COMMON_STR_BASE + 117;
    
    public final static int RES_NAVIGATION = COMMON_STR_BASE + 118;
    public final static int RES_ROUTE = COMMON_STR_BASE + 119;
    public final static int RES_PLACES = COMMON_STR_BASE + 120;
    public final static int RES_END_TRIP = COMMON_STR_BASE + 121;
    public final static int RES_DIRECTIONS = COMMON_STR_BASE + 122;
    
    public final static int RES_ADD_TO_FAVORITE = COMMON_STR_BASE + 123;
    
    public final static int RES_MENU_CHANGE_LOCATION = COMMON_STR_BASE + 124;
    
    public final static int RES_MENU_ABOUT = COMMON_STR_BASE + 125;
    
    public final static int RES_ONEBOX_TAB_BUSINESS = COMMON_STR_BASE + 126;
    
    public final static int RES_ONEBOX_TAB_ADDRESS = COMMON_STR_BASE + 127;
    
    public final static int RES_ONEBOX_TAB_BUSINESS_HINT = COMMON_STR_BASE + 128;
    public final static int RES_ONEBOX_TAB_BUSINESS_PY_HINT = COMMON_STR_BASE + 129;
    
    public final static int RES_BTTN_DASHBOARD = COMMON_STR_BASE + 130;
    public final static int RES_MARKET_BILLING_NOTIFICATION = COMMON_STR_BASE + 131;
    public final static int RES_MARKET_PURCHASE_SUCC = COMMON_STR_BASE + 132;
    
    public final static int RES_TIMEUNIT_HOUR = COMMON_STR_BASE + 150;
    public final static int RES_TIMEUNIT_MINUTE = COMMON_STR_BASE + 151;
    
    public final static int RES_MSG_GETTING_STOP = COMMON_STR_BASE + 152;
    
    public final static int RES_MSG_APPLICATION_EXITING = COMMON_STR_BASE + 153;
    
    public final static int RES_PRODUCT_TITLE = COMMON_STR_BASE + 154;
    
    public final static int RES_DRIVE = COMMON_STR_BASE + 155;
    
    public final static int RES_NEARBY = COMMON_STR_BASE + 156;

    public final static int RES_ADDRESS_SEARCH_ONLY = COMMON_STR_BASE + 157;
    
    public final static int RES_NO_INTERNET_CONNECTION = COMMON_STR_BASE + 158;
    
    public final static int RES_ERROR_START_MAITAI = COMMON_STR_BASE + 159;
    
    public final static int RES_SORRY = COMMON_STR_BASE + 160;
    
    public final static int RES_INPUT_HINT_OFFLINE = COMMON_STR_BASE + 161;
    
    public final static int RES_BTTN_RATE_APP = COMMON_STR_BASE + 162;
    
    public final static int RES_BTTN_REMIDER = COMMON_STR_BASE + 163;
    
    public final static int RES_BTTN_NO_THANKS = COMMON_STR_BASE + 164;
    
    public final static int RES_RATE_MESSAGE = COMMON_STR_BASE + 165;
    
    public final static int RES_RATE_TITLE = COMMON_STR_BASE + 166;
    
    public final static int RES_TAKE_ME_THERE = COMMON_STR_BASE + 167;
        
    public final static int RES_NOT_RIGHT_NOW = COMMON_STR_BASE + 168;
    
    public final static int RES_DROPPED_PIN = COMMON_STR_BASE + 169;
    
    public final static int RES_SHARE_ETA_NOT_STARTED = COMMON_STR_BASE + 170;
    
    public final static int RES_SHARE_ETA_DRIVING = COMMON_STR_BASE + 171;
    
    public final static int RES_SHARE_ETA_ARRIVED = COMMON_STR_BASE + 172;
    
    public final static int RES_SHARE_ETA_FRIEND = COMMON_STR_BASE + 173;
    
    public final static int RES_EXIT_APP = COMMON_STR_BASE + 174;
    
    public final static int RES_STRING_FOOD = COMMON_STR_BASE + 175;
    
    public final static int RES_STRING_GAS_REGULAR = COMMON_STR_BASE + 176;
    
    public final static int RES_SETUP_HOME = COMMON_STR_BASE + 177;
    
    public final static int RES_SETUP_WORK = COMMON_STR_BASE + 178;
}


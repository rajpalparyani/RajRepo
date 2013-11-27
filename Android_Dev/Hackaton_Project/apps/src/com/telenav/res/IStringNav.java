/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStringNav.java
 *
 */
package com.telenav.res;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public interface IStringNav
{
    //=====================================nav family=====================================//
    public final static String FAMILY_NAV = "nav";
    
    public final static int NAV_STR_BASE        = 30000;
    
    public final static int RES_TURN = NAV_STR_BASE + 1;
    
    public final static int RES_STREET = NAV_STR_BASE + 2;
    
    public final static int RES_DIST = NAV_STR_BASE + 3;
    
    public final static int RES_TRAFFIC_SUMMARY = NAV_STR_BASE + 4;
    
    public final static int RES_MINIMIZE_ALL_DELAYS = NAV_STR_BASE + 5;
    
    public final static int RES_MPH = NAV_STR_BASE + 6;
    
    public final static int RES_GETTING_ROUTE = NAV_STR_BASE + 7;
    
    public final static int RES_VOICE = NAV_STR_BASE + 8;
    
    public final static int RES_DIRECETIONS_TRAFFIC = NAV_STR_BASE + 9;
    
    public final static int RES_TRAFFIC_ONLY = NAV_STR_BASE + 10;
    
    public final static int RES_DIRECTIONS_ONLY = NAV_STR_BASE + 11;
    
    public final static int RES_NONE = NAV_STR_BASE + 12;
    
    public final static int RES_ROUTE_SETTINGS = NAV_STR_BASE + 13;
    
    public final static int RES_NAVIGATE = NAV_STR_BASE + 14;
    
    public final static int RES_DIRECTIONS = NAV_STR_BASE + 15;
    
    public final static int RES_DIST_PREFIX = NAV_STR_BASE + 16;
    
    public final static int RES_ETA_PREFIX = NAV_STR_BASE + 17;
    
    public final static int RES_ROUTE_A = NAV_STR_BASE + 18;
    
    public final static int RES_ROUTE_B = NAV_STR_BASE + 19;
    
    public final static int RES_ROUTE_C = NAV_STR_BASE + 20;
    
    public final static int RES_TIME_REMAINING = NAV_STR_BASE + 21;
    
    public final static int RES_GETTING_TRAFFIC = NAV_STR_BASE + 22;
    
    public final static int RES_AVOID = NAV_STR_BASE + 23;
    
    public final static int RES_VOICE_POPUP_TIP = NAV_STR_BASE + 24;
    
    public final static int RES_MOVING_MAP_END_TRIP_MSG = NAV_STR_BASE + 25;
    
    public final static int RES_END_TRIP_BTTN = NAV_STR_BASE + 26;
    
    public final static int RES_GETTING_TURN_MAPS = NAV_STR_BASE + 27;
    
    public final static int RES_STATUS_GETTING_GPS = NAV_STR_BASE + 28;
    
    public final static int RES_STATUS_GETTING_ROUTE = NAV_STR_BASE + 29;
    
    public final static int RES_ORIG_DEST_TOO_CLOSE = NAV_STR_BASE + 30;
    
    public final static int RES_START = NAV_STR_BASE + 31;
    
    public final static int RES_EXIT_TURN_MAP_CONFIRM_MESSAGE = NAV_STR_BASE + 32;
    
    public final static int RES_SEARCH_UPHEAD = NAV_STR_BASE + 33;
    
    public final static int RES_SEARCH_NEAR_DEST = NAV_STR_BASE + 34;
    
    public final static int RES_REPORT_SPEED_TRAP = NAV_STR_BASE + 35;
    
    public final static int RES_END_DETOUR_DETAIL = NAV_STR_BASE + 37;
    
    public final static int RES_END_DETOUR_BTTN = NAV_STR_BASE + 38;
    
    public final static int RES_DETOUR = NAV_STR_BASE + 39;
    
    public final static int RES_RESUME_TRIP_TO = NAV_STR_BASE + 41;
    
    public final static int RES_XSTREET_INFO = NAV_STR_BASE + 42;
    
    public final static int RES_LANE_CLOSE = NAV_STR_BASE + 43;
    public final static int RES_LANES_CLOSE = NAV_STR_BASE + 44;
    public final static int RES_ROAD_CLOSE = NAV_STR_BASE + 45;
    public final static int RES_TRAFFIC_INFO_NOT_AVAILABLE = NAV_STR_BASE + 46;
    
    public final static int RES_REROUTING = NAV_STR_BASE + 47;
    public final static int RES_NO_BETTER_ROUTE = NAV_STR_BASE + 48;
    
    //---------------------------------------------
    public final static int RES_ROUTE_PLANNING_FROM = NAV_STR_BASE + 49;
    public final static int RES_ROUTE_PLANNING_TO = NAV_STR_BASE + 50;
    public final static int RES_ROUTE_PLANNING_SETTING_ROUTE = NAV_STR_BASE + 51;
    public final static int RES_ROUTE_PLANNING_SETTING_ORIGIN = NAV_STR_BASE + 52;
    public final static int RES_ROUTE_PLANNING_SETTING_DESTINATION = NAV_STR_BASE + 53;
    public final static int RES_ROUTE_PLANNING_SETTING_ROUTE_SETTING = NAV_STR_BASE + 54;
    public final static int RES_ROUTE_PLANNING_SETTING_VOICE_SETTING = NAV_STR_BASE + 55;
    public final static int RES_ROUTE_PLANNING_SETTING_GET_ROUTE = NAV_STR_BASE + 56;
    public final static int RES_TAB_TURNS = NAV_STR_BASE + 57;
    public final static int RES_TAB_TRAFFIC = NAV_STR_BASE + 58;
    public final static int RES_TAB_MAP = NAV_STR_BASE + 59;
    
    public final static int RES_TRAFFIC_NA = NAV_STR_BASE + 60;
    public final static int RES_TRAFFIC_INCIDENT_PREFIX = NAV_STR_BASE + 61;
    public final static int RES_TRAFFIC_DELAY_TIME = NAV_STR_BASE + 62;
    public final static int RES_TRAFFIC_LAST_UPDATE_TIME_PREFIX = NAV_STR_BASE + 63;
    public final static int RES_TRAFFIC_LAST_UPDATE_TIME_SUFFIX = NAV_STR_BASE + 64;
    
    final static public int RES_ARRIVE                 = NAV_STR_BASE + 65;
    final static public int RES_BEAR_LEFT              = NAV_STR_BASE + 66;
    final static public int RES_BEAR_RIGHT             = NAV_STR_BASE + 67;
    final static public int RES_CONTINUE_ON               = NAV_STR_BASE + 68;
    final static public int RES_ENTER                  = NAV_STR_BASE + 69;
    final static public int RES_EXIT                   = NAV_STR_BASE + 70;
    final static public int RES_EXIT_ROUND_ABOUT       = NAV_STR_BASE + 71;
    final static public int RES_MERGE                  = NAV_STR_BASE + 72;
    final static public int RES_ROUND_ABOUT            = NAV_STR_BASE + 73;
    final static public int RES_START_AT               = NAV_STR_BASE + 74;
    final static public int RES_TURN_HARD_LEFT         = NAV_STR_BASE + 75;
    final static public int RES_TURN_HARD_RIGHT        = NAV_STR_BASE + 76;
    final static public int RES_TURN_LEFT              = NAV_STR_BASE + 77;
    final static public int RES_TURN_RIGHT             = NAV_STR_BASE + 78;
    final static public int RES_TURN_SLIGHT_LEFT       = NAV_STR_BASE + 79;
    final static public int RES_TURN_SLIGHT_RIGHT      = NAV_STR_BASE + 80;
    final static public int RES_U_TURN                 = NAV_STR_BASE + 81;
    
    final static public int RES_OF                 = NAV_STR_BASE + 82;
    final static public int RES_AND                 = NAV_STR_BASE + 83;
    
    final static public int RES_ACCEPT_NEW_ROUTE    = NAV_STR_BASE + 84;
    final static public int RES_USE_CURRENT_ROUTE   = NAV_STR_BASE + 85;
    final static public int RES_NEW_ROUTE           = NAV_STR_BASE + 86;
    
    final static public int RES_ADDED_DISTANCE      = NAV_STR_BASE + 87;
    final static public int RES_REDUCED_DISTANCE    = NAV_STR_BASE + 88;
    final static public int RES_DETOUR_ETA          = NAV_STR_BASE + 89;

    final static public int RES_APPROACHING_DESTINATION                 = NAV_STR_BASE + 90;
    final static public int RES_BTTN_CONTINUE                 = NAV_STR_BASE + 91;
    final static public int RES_DEST_ADDED_TO_FAV                 = NAV_STR_BASE + 92;
    final static public int RES_DEST_DELETED_FROM_FAV                 = NAV_STR_BASE + 93;
    
    final static public int RES_WEAK_GPS                              = NAV_STR_BASE + 94;
    final static public int RES_OVER_SPEED_LIMIT                      = NAV_STR_BASE + 95;
    
    //--------- NOTIFICATION STRING --------------------
    final static public int RES_TEXT_ARRIVE                 = NAV_STR_BASE + 96;
    final static public int RES_TEXT_BEAR_LEFT              = NAV_STR_BASE + 97;
    final static public int RES_TEXT_BEAR_RIGHT             = NAV_STR_BASE + 98;
    final static public int RES_TEXT_CONTINUE               = NAV_STR_BASE + 99;
    final static public int RES_TEXT_ENTER                  = NAV_STR_BASE + 100;
    final static public int RES_TEXT_EXIT                   = NAV_STR_BASE + 101;
    final static public int RES_TEXT_EXIT_ROUND_ABOUT       = NAV_STR_BASE + 102;
    final static public int RES_TEXT_MERGE                  = NAV_STR_BASE + 103;
    final static public int RES_TEXT_ROUND_ABOUT            = NAV_STR_BASE + 104;
    final static public int RES_TEXT_START                  = NAV_STR_BASE + 105;
    final static public int RES_TEXT_TURN_HARD_LEFT         = NAV_STR_BASE + 106;
    final static public int RES_TEXT_TURN_HARD_RIGHT        = NAV_STR_BASE + 107;
    final static public int RES_TEXT_TURN_LEFT              = NAV_STR_BASE + 108;
    final static public int RES_TEXT_TURN_RIGHT             = NAV_STR_BASE + 109;
    final static public int RES_TEXT_TURN_SLIGHT_LEFT       = NAV_STR_BASE + 110;
    final static public int RES_TEXT_TURN_SLIGHT_RIGHT      = NAV_STR_BASE + 111;
    final static public int RES_TEXT_U_TURN                 = NAV_STR_BASE + 112;
    final static public int RES_TEXT_NEXT                   = NAV_STR_BASE + 113;
    final static public int RES_TEXT_PREVIOUS               = NAV_STR_BASE + 114;    
    final static public int RES_BACK_TO_NAV                 = NAV_STR_BASE + 115;
    final static public int RES_SPEED_LIMIT                 = NAV_STR_BASE + 116;
    final static public int RES_ADVERTISEMENT               = NAV_STR_BASE + 117;
    final static public int RES_TRAFFIC_DELAY_DETECTED      = NAV_STR_BASE + 118;
    final static public int RES_DELAY                       = NAV_STR_BASE + 119;
    final static public int RES_ROUTE_PLANNING_SETTING_BUTTON           = NAV_STR_BASE + 120;
    final static public int RES_TRAFFIC_AVOID_SEGMENT       = NAV_STR_BASE + 121;
    
    final static public int RES_FERRY_ENTER = NAV_STR_BASE + 122;
    final static public int RES_FERRY_EXIT = NAV_STR_BASE + 123;
    final static public int RES_STAY_MIDDLE = NAV_STR_BASE + 124;
    final static public int RES_F2Z_ENTER_LEFT = NAV_STR_BASE + 125;
    final static public int RES_Z2F_EXIT_LEFT = NAV_STR_BASE + 126;
    final static public int RES_MULIT_FORK_STAY_RIGHT = NAV_STR_BASE + 127;
    final static public int RES_MULIT_FORK_STAY_LEFT = NAV_STR_BASE + 128;
    final static public int RES_H2R_EXIT_MIDDLE = NAV_STR_BASE + 129;
    final static public int RES_H2H_EXIT = NAV_STR_BASE + 130;
//    final static public int RES_H2H_EXIT_RIGHT = NAV_STR_BASE + 123;
//    final static public int RES_L2H_ENTER = NAV_STR_BASE + 131;
    final static public int RES_F2Z_ENTER_RIGHT = NAV_STR_BASE + 131;
    
    final static public int RES_Z2F_EXIT_RIGHT = NAV_STR_BASE + 132;
    final static public int RES_WAYPOINT = NAV_STR_BASE + 133;
    
    public final static int RES_KPH = NAV_STR_BASE + 134;
    
    public final static int RES_AVOID_POPUP_IGNORE = NAV_STR_BASE + 135;

    public final static int RES_DESTINATION_ON = NAV_STR_BASE + 136;
    public final static int RES_LEFT = NAV_STR_BASE + 137;
    public final static int RES_RIGHT = NAV_STR_BASE + 138;
    public final static int RES_AHEAD = NAV_STR_BASE + 139;
    
    public final static int RES_ORDINAL_NUMBERS = NAV_STR_BASE + 141;
    public final static int RES_EXIT_TO_EXIT_NUMBER = NAV_STR_BASE + 142;
    public final static int RES_EXIT_TO_EXIT_NAME = NAV_STR_BASE + 143;
    public final static int RES_NO_NETWORK = NAV_STR_BASE + 144;
    public final static int RES_END_NAVIGATION_SESSION = NAV_STR_BASE + 145;
    
    public final static int RES_ROUTE_PLANNING = NAV_STR_BASE + 146;
    public final static int RES_SHARE = NAV_STR_BASE + 147;
    public final static int RES_ETA = NAV_STR_BASE + 148;
    public final static int RES_DRIVE = NAV_STR_BASE + 149;
    public final static int RES_ONBOARD_ROUTE_PLANNING_ERROR= NAV_STR_BASE + 150;
}

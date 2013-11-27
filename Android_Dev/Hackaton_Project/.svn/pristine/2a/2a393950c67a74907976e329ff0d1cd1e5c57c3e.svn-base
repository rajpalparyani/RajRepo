/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMovingMapConstants.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.nav.navigation;

import com.telenav.map.MapConfig;
import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-5
 */
interface IMovingMapConstants extends ICommonConstants
{
    //-------------------------------------------------------------
    // State definition
    //-------------------------------------------------------------
	public static final int STATE_MOVING_MAP_BASE = STATE_USER_BASE + USER_BASE_MOVING_MAP;
    public static final int STATE_MOVING_MAP = (STATE_USER_BASE + USER_BASE_MOVING_MAP + 1);
    public static final int STATE_GOTO_ROUTE_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 2);
    public static final int STATE_GOTO_TRAFFIC_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 3);
    public static final int STATE_GOTO_DSR = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 4);
    public static final int STATE_GOTO_POI = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 5);
    public static final int STATE_GOTO_MAP_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 6);
    public static final int STATE_EXIT_CONFIRM = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 7);
    public static final int STATE_RTS_FAILED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 8);
    
    public static final int STATE_END_TRIP_MODE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 9);
    public static final int STATE_RESUME_TRIP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 11);
    

    /**
     * When user clicks traffic alert box at right bottom, we show traffic alert detail first.
     */
    public static final int STATE_TRAFFIC_ALERT_DETAIL = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 12);
    
    public static final int STATE_AVOID_INCIDENT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 13);
    
    public static final int STATE_EXIT_APPLICATION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 14);
    
    public static final int STATE_POI_RETURN = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 15);
    
    public static final int STATE_REQUESTING_DEVIATION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 16);
    
    public static final int STATE_DEVIATION_FAIL = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 17);
    
    public static final int STATE_START_MAIN = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 18);
    
    public static final int STATE_DRIVING_TO_AD = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 19);

	public static final int STATE_GO_TO_ROUTE_SETTING = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 20);
	
	public static final int STATE_UPDATE_ROUTE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 21);
	
    public static final int STATE_AD_DETAIL = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 22);
    
    public static final int STATE_MAPPING_AD = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 23);

    public static final int STATE_DETOUR_CONFIRM = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 24);

    public static final int STATE_CHECKING_BACK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 25);
    
    public static final int STATE_SYNC_PURCHASE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 26);
    
    public static final int STATE_START_MAIN_GO_PURCHASE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 27);

    //TODO: do we have these logic in TN7.0? currently we impl the simple solution first.
    //public static final int STATE_EXIT_APPLICATION_CONFIRM = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 8);
    //public static final int STATE_BACK_TO_HOME_CONFIRM = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 10);
    //public static final int STATE_BACK_TO_HOME_CONFIRM_FEEDBACK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_MOVING_MAP + 11);
        
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_NAVIGATE = CMD_USER_BASE + USER_BASE_MOVING_MAP + 1;
    public static final int CMD_ROUTE_SUMMARY = CMD_USER_BASE + USER_BASE_MOVING_MAP + 2;
    public static final int CMD_TRAFFIC_SUMMARY = CMD_USER_BASE + USER_BASE_MOVING_MAP + 3;
    public static final int CMD_END_TRIP = CMD_USER_BASE + USER_BASE_MOVING_MAP + 4;
    public static final int CMD_EXIT = CMD_USER_BASE + USER_BASE_MOVING_MAP + 6;
    /**
     * Show traffic detail when user click traffic alert box at right bottom
     */
    public static final int CMD_SHOW_TRAFFIC_DETAIL = CMD_USER_BASE + USER_BASE_MOVING_MAP + 7;
    
    public static final int CMD_AVOID = CMD_USER_BASE + USER_BASE_MOVING_MAP + 8;
    
    public static final int CMD_MAP_SUMMARY = CMD_USER_BASE + USER_BASE_MOVING_MAP + 9;
    
    public static final int CMD_REPORT_TRAFFIC_CAMERA = CMD_USER_BASE + USER_BASE_MOVING_MAP + 10;
    
    public static final int CMD_REPORT_SPEED_TRAP = CMD_USER_BASE + USER_BASE_MOVING_MAP + 11;
    
    public static final int CMD_RESUME_TRIP = CMD_USER_BASE + USER_BASE_MOVING_MAP + 12;
    
    public static final int CMD_BOTTOM_HIDE = CMD_USER_BASE + USER_BASE_MOVING_MAP + 13;
    
    public static final int CMD_PLAY_AUDIO = CMD_USER_BASE + USER_BASE_MOVING_MAP + 14;
    
    public static final int CMD_ADD_TO_FAVORITE = CMD_USER_BASE + USER_BASE_MOVING_MAP + 16;
    
    public static final int CMD_FAVORITE_NOTIFACTION_HIDE = CMD_USER_BASE + USER_BASE_MOVING_MAP + 17;
    
    public static final int CMD_GO_TO_ROUTE_SETTING = CMD_USER_BASE + USER_BASE_MOVING_MAP + 18;
        
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT_MAP = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 1;
    public static final int ACTION_GET_ROUTE = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 2;
    public static final int ACTION_AVOID_INCIDENT = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 3;    
    public static final int ACTION_CANCEL_AVOID_INCIDENT = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 4;
    public static final int ACTION_REPORT_TRAFFIC_CAMERA = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 5;
    public static final int ACTION_REPORT_SPEED_TRAP = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 6;
    public static final int ACTION_RENEW_DYNAMIC_ROUTE = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 7;
    public static final int ACTION_RESUME_TRIP = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 8;
    public static final int ACTION_PLAY_AUDIO = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 9;
    public static final int ACTION_SHOW_END_TRIP = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 10;
    public static final int ACTION_GET_AD_ROUTE = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 11;
    public static final int ACTION_END_TRIP_CHECK = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 12;
    public static final int ACTION_CHECK_BACK = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 13;
    public static final int ACTION_SYNC_PURCHASE = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 14;
    public static final int ACTION_CONTINUE_GET_ROUTE = ACTION_USER_BASE + USER_BASE_MOVING_MAP + 15;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LOCATION_DATA_RETRIEVED = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 1;
    public static final int EVENT_MODEL_LOCATION_DATA_FAILED = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 2;
    public static final int EVENT_MODEL_RTS_FAILED = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 3;
    public static final int EVENT_MODEL_SHOW_END_TRIP = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 4;    
    public static final int EVENT_MODEL_SHOW_NAV_MAP = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 5;
    
    //EVENT_MODEL_BACK_TO_HOME is exactly the same as EVENT_MODEL_EXIT, so remove it.
    //public static final int EVENT_MODEL_BACK_TO_HOME = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 5;
    public static final int EVENT_MODEL_EXIT_NAV = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 6;
    
    public static final int EVENT_MODEL_REQUEST_DEVIATION = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 7;
    
    public static final int EVENT_MODEL_DRIVE_TO_AD = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 8;
    
    public static final int EVENT_MODEL_AD_DETAIL = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 9;
    
    public static final int EVENT_MODEL_MAP_AD = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 10;
    
    public static final int EVENT_MODEL_DETOUR_CONFIRM = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 11;
    
    public static final int EVENT_MODEL_EXIT_CONFIRM = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 12;
    
    public static final int EVENT_MODEL_INVALID_IDENTITY = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 13;
    
    public static final int EVENT_MODEL_EXIT_AND_PURCHASE = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 14;
	
	public static final int EVENT_MODEL_CONTINUE_GET_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_MOVING_MAP + 15;
    
    //-----------------------------------------------------------
    // key value between and in modules.
    //-----------------------------------------------------------
    public static final Integer KEY_O_MAP_CONTAINER = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 1);
    public static final Integer KEY_O_NAV_PARAMETER = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 2);
    public static final Integer KEY_B_IS_ARRIVE_DESTINATION = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 3);
        
    public static final Integer KEY_B_IS_VALID_SERVICE = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 5);
    public static final Integer KEY_B_IS_CRITICAL_ERROR = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 6);
    public static final Integer KEY_S_AD_DETAIL_URL = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 7);
    public static final Integer KEY_O_AD_ADDRESS = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 8);
    public static final Integer KEY_O_BILLBOARD_AD = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 9);
    public static final Integer KEY_B_GOTO_EXIT_CONFIRM = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 10);
    public static final Integer KEY_I_GET_ROUTE_TYPE = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 11);
    public static final Integer KEY_B_IS_MAP_INITIALIZED = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 12);
    public static final Integer KEY_O_NAVINFO = PrimitiveTypeCache.valueOf(STATE_MOVING_MAP_BASE + 13);
        
    //------------------------------------------------------------
    //
    //------------------------------------------------------------
    final static String ID_NAV_AUDIO = "NAV_AUDIO";

    public static final int ID_NAV_TITLE = 1001;
    public static final int ID_SPEED_LIMIT_CONTAINER = 1003;
    public static final int ID_TRAFFIC_ALERT_CONTAINER = 1005;
    public static final int ID_COMPASS_CONTAINER = 1006;
    public static final int ID_LANE_ASSIST_COMPONENT = 1007;
    public static final int ID_BOTTOM_CONTAINER = 1009;
    public static final int ID_BACK_TO_NAV_BUTTON = 1010;
    public static final int ID_CURRENT_STREET_NAME = 1011;
    public static final int ID_ZOOM_IN_BUTTON = 1012;
    public static final int ID_ZOOM_OUT_BUTTON = 1013;
    public static final int ID_ZOOM_CONTAINER = 1014;
    public static final int ID_TRAFFIC_BUTTON_CONTAINER = 1015;
    public static final int ID_END_TRIP_ADD_FAV_BUTTON = 1017;
    public static final int ID_AD_CONTAINER = 1018;
    public static final int ID_END_TRIP_CONTAINER = 1019;
    
    public static final int ID_TYPE_GET_ROUTE = 10000;
    public static final int ID_TYPE_GET_AD_ROUTE = 10001;
    public static final int ID_TYPE_OTHER = 10002;
    
    public static final int ZOOM_LEVEL_JUNCTION_VIEW = MapConfig.MAP_MIN_ZOOM_LEVEL;
    public static final int ZOOM_LEVEL_DEFAULT_NAV = MapConfig.MAP_MIN_ZOOM_LEVEL;
    public static final int ZOOM_LEVEL_DEFAULT_NAV_LANDSCAPE = MapConfig.MAP_MIN_ZOOM_LEVEL + 1;
    public static final float ZOOM_LEVEL_ADI = 2.0f; //fix bug TN-2388
    
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITrafficConstants.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.traffic;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-10-26
 */
interface ITrafficConstants extends ICommonConstants
{
    // **********************************************************************
    // *********************** State id definition ************************
    // **********************************************************************
	public static final int STATE_TRAFFIC_BASE = STATE_USER_BASE + USER_BASE_TRAFFIC;
	
    public static final int STATE_GET_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 1);
    
    public static final int STATE_SHOW_SUMMARY = STATE_USER_BASE + USER_BASE_TRAFFIC + 2;
    
    public static final int STATE_GO_TO_ROUTE_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 3);
    
    public static final int STATE_CHECK_SEGMENT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 4);

    public static final int STATE_SHOW_TRAFFIC_SEGMENT = STATE_USER_BASE + USER_BASE_TRAFFIC + 5;
    
    public static final int STATE_SEND_AVOID = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 6);
    
    public static final int STATE_ALTERNATE_ROUTE = STATE_USER_BASE + USER_BASE_TRAFFIC + 7;
    
    public static final int STATE_GO_TO_POI = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 8);
    
    public static final int STATE_GO_TO_MAP_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 9);
    
    /**
     * Back to SummaryControlController and let SummaryControlController determine the to coming logic
     */
    public static final int STATE_BACK_TO_SUMMARY_CONTROLLER = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 10);
    
    public static final int STATE_RENEW_DYNAMIC_ROUTE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 11);
        
    /**
     * Back to moving map screen.
     */
    public static final int STATE_GO_BACK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 12);
    
    public static final int STATE_EXIT_CONFIRM = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 13);
    
    public static final int STATE_EXIT_NAV = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 14);
    
    public static final int STATE_RESUME_TRIP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 15);
    
    public static final int STATE_RENEW_STATIC_ROUTE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TRAFFIC + 16);
    
    // **********************************************************************
    // *********************** Action id definition ***********************
    // **********************************************************************
    public static final int ACTION_GET_TRAFFIC_SUMMARY = 1;

    public static final int ACTION_CANCEL_GET_SUMMARY = 2;

    public static final int ACTION_SHOW_TRAFFIC_SEGMENT = 3;

//    public static final int ACTION_AVOID = 4;

    public static final int ACTION_AVOID_SELECTED = 5;

    public static final int ACTION_CANCEL_REROUTE = 6;

    public static final int ACTION_MINIMIZE_ALL_DELAY = 7;

    public static final int ACTION_CHECK_NAV = 8;

    public static final int ACTION_REROUTE = 9;

    public static final int ACTION_COMMUTE_SUMMARY = 10;

    public static final int ACTION_SWITCH_AUDIO_PATH = 11;

    public static final int ACTION_PLAY_AUDIO = 12;

    public static final int ACTION_HANDLE_SEARCH_ALONG = 13;

    public static final int ACTION_SHOW_SUMMARY_TIP = 14;

    public static final int ACTION_STOP_TRAFFIC_AUDIO = 15;
    
    public static final int ACTION_CHECK_TRAFFIC_SEGMENT = 17;
    
    // **********************************************************************
    // ********************** Command id definition ***********************
    // **********************************************************************
    public static final int CMD_CANCEL = CMD_USER_BASE + USER_BASE_TRAFFIC + 1;

    public static final int CMD_VIEW_DETAIL = CMD_USER_BASE + USER_BASE_TRAFFIC + 2;

    public static final int CMD_AVOID = CMD_USER_BASE + USER_BASE_TRAFFIC + 3;

    public static final int CMD_AVOID_SELECTED = CMD_USER_BASE + USER_BASE_TRAFFIC + 4;

    public static final int CMD_CANCEL_REROUTE = CMD_USER_BASE + USER_BASE_TRAFFIC + 5;

    public static final int CMD_MINIMIZE_ALL_DELAY = CMD_USER_BASE + USER_BASE_TRAFFIC + 6;

    public static final int CMD_NAVIGATION = CMD_USER_BASE + USER_BASE_TRAFFIC + 7;

    public static final int CMD_ACCEPT_REROUTE = CMD_USER_BASE + USER_BASE_TRAFFIC + 8;

    public static final int CMD_SWITCH_AUDIO_PATH = CMD_USER_BASE + USER_BASE_TRAFFIC + 9;

    public static final int CMD_PLAY_AUDIO = CMD_USER_BASE + USER_BASE_TRAFFIC + 10;

    public static final int CMD_SEARCH_ALONG_HOT_KEY = CMD_USER_BASE + USER_BASE_TRAFFIC + 11;
    
    public static final int CMD_ROUTE_SUMMARY = CMD_USER_BASE + USER_BASE_TRAFFIC + 12;
    
    public static final int CMD_MAP_SUMMARY = CMD_USER_BASE + USER_BASE_TRAFFIC + 13;

    public static final int CMD_TRAFFIC_SUMMARY = CMD_USER_BASE + USER_BASE_TRAFFIC + 14;
    
    //6.1
    public static final int CMD_COMMON_BACK_TO_NAV_FROM_SEG = CMD_USER_BASE + USER_BASE_TRAFFIC + 16;
    
    public static final int CMD_DIRECTIONS = CMD_USER_BASE + USER_BASE_TRAFFIC + 17;
    
    public static final int CMD_END_TRIP = CMD_USER_BASE + USER_BASE_TRAFFIC + 18;
    
    public static final int CMD_END_TRIP_CONFIRM = CMD_USER_BASE + USER_BASE_TRAFFIC + 19;
    
    public static final int CMD_RESUME_TRIP = CMD_USER_BASE + USER_BASE_TRAFFIC + 20;
    
    // **********************************************************************
    // ********************* model event definition ***********************
    // **********************************************************************
    public static final int EVENT_MODEL_SHOW_SUMMARY = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 1;

    public static final int EVENT_MODEL_START_NAV = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 2;

    public static final int EVENT_MODEL_BACK_TO_NAV = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 3;

    public static final int EVENT_MODEL_ALTERNATE_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 4;

    public static final int EVENT_MODEL_BACK_TO_STATIC_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 5;

    public static final int EVENT_MODEL_LAUNCH_STATIC_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 6;

    public static final int EVENT_MODEL_NO_BETTER_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 7;

    public static final int EVENT_MODEL_GPS_ERROR = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 8;

    public static final int EVENT_MODEL_RTS_FAILED = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 9;

    public static final int EVENT_MODEL_NO_BT_AUDIO = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 10;

    public static final int EVENT_MODEL_SHOW_TIP = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 11;

    public static final int EVENT_MODEL_RENEW_DYNAMIC_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 12;
    
    public static final int EVENT_MODEL_EXIT = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 13;
    
    public static final int EVENT_MODEL_ALTERNATE_ERROR = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 14;
    
    public static final int EVENT_MODEL_SHOW_TRAFFIC_SEGMENT = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 15;
    
    public static final int EVENT_MODEL_TRAFFIC_SEGMENT_ERROR = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 16;
    
    public static final int EVENT_MODEL_RENEW_STATIC_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 17;
    
    public static final int EVENT_MODEL_RESPONSE_TRAFFIC_SEGMENT = EVENT_MODEL_USER_BASE + USER_BASE_TRAFFIC + 18;

    // **********************************************************************
    // *********************** key id definition **************************
    // **********************************************************************

    public static final Integer KEY_B_CAN_AVOID_SEGMENT = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 1);

    public static final Integer KEY_I_FAKE_TITLE_HEIGHT = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 4);

    public static final Integer KEY_I_INDEX             = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 5);

    public static final Integer KEY_B_IS_TRAFFIC_AUDIO_PLAY = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 6);
    
    public static final Integer KEY_B_IS_FROM_COMMUTE_ALERT_MAP = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 7);
    
    public static final Integer KEY_B_REFRESH_ALTERNATE_SCREEN  = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 8);
    
    public static final Integer KEY_O_SEGMENT  = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 9);
    
    public static final Integer KEY_A_SEGMENTS = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 10);
    
    public static final Integer KEY_I_INCIDENT_NUMBER  = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 12);
    
    public static final Integer KEY_I_TOTAL_DELAY      = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 13);
        
    public static final Integer KEY_A_ROUTE_CHOICES = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 14);
    
    public static final Integer KEY_A_ROUTE_CHOICES_ETA = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 15);
    
    public static final Integer KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 16);
    
    public static final Integer KEY_A_ROUTE_CHOICES_LENGTH = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 17);
    
    public static final Integer KEY_O_TRAFFICINFOR  = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 18);
    
    public static final Integer KEY_I_ERROR_CODE    = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 19);
    
    public static final Integer KEY_S_ERROR_MSG    = PrimitiveTypeCache.valueOf(STATE_TRAFFIC_BASE + 20);
    
  //-----------------------------------------------------------
    // component id
    //-----------------------------------------------------------
    public static final int ID_SUMMARY_CONTAINER = 1001;
    public static final int ID_ALTERNATE_ROUTE_TITLE = 1002;
    public static final int ID_BUTTON_CONTAINER = 1004;
    public static final int ID_ACCEPT_BUTTON = 1005;
    public static final int ID_CANCEL_BUTTON = 1006;
    public static final int ID_TAB_TURNS_BUTTON = 1007;
    public static final int ID_TAB_MAP_BUTTON = 1008;
    public static final int ID_AVOID_BUTTON = 1009;
    public static final int ID_TRAFFIC_ITEM_BASE = 3000;
    
    final static String ID_TRAFFIC_AUDIO = "TRAFFIC_AUDIO";
//    public static final int ID_ALTERNATE_ROUTE_MAP_CONTAINER = 1002;

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRoutePlanningConstants.java
 *
 */
package com.telenav.module.nav.routeplanning;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-2
 */
public interface IRoutePlanningConstants extends ICommonConstants
{
    //-------------------------------------------------------------
    // State definition
    //-------------------------------------------------------------
	public static final int STATE_ROUTE_PLANNING_BASE = STATE_USER_BASE + USER_BASE_ROUTE_PLANNING;
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 1);
    public static final int STATE_ROUTE_PLANNING = STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 2;
	public static final int STATE_GOTO_NAVIGATE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 3);
	public static final int STATE_GOTO_DIRECTIONS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 4);
    public static final int STATE_NO_GPS_TIMEOUT = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 5);
    public static final int STATE_CHANGE_SETTING_SCREEN = STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 6;
    public static final int STATE_GO_TO_AC = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 7);
    public static final int STATE_GO_TO_ROUTE_SETTING = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 10);
    public static final int STATE_GO_TO_VOICE_SETTING = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 11);
    public static final int STATE_CHECK_ACCOUNT_STATUS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 12);
    public static final int STATE_GOTO_PURCHASE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 13);
    public static final int STATE_CHECK_MULTIROUTE_SERVER_DRIVEN_VALUE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 14);
    public static final int STATE_START_GETTING_ROUTE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 15);
    public static final int STATE_RETURN_TO_DASHBOARD = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 16);
    public static final int STATE_CHECK_SHARE_ETA = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 17);
    public static final int STATE_COMMOM_ERROR_BACK_TO_DASHBOARD = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 18);
    public static final int STATE_REQUEST_TINY_URL = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 19);
    public static final int STATE_CHECK_TINY_URL_QEQUIRED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_PLANNING + 20);

    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_NAVIGATE = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 1;
    public static final int CMD_DIRECTIONS = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 2;
    public static final int CMD_UPDATE_PLAN_INFO = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 3;
    public static final int CMD_RETRY_GPS = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 4;
    public static final int CMD_CHANGE_SETTING = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 6;
    public static final int CMD_GET_ROUTE = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 7;
    public static final int CMD_CHANGE_ORIGIN = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 8;
    public static final int CMD_CHANGE_DESTINATION = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 9;
    public static final int CMD_CHANGE_ROUTE_SETTING = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 10;
    public static final int CMD_CHANGE_VOICE_SETTING = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 11;
    
    public static final int CMD_SHOW_ROUTE_LIST = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 12;
    public static final int CMD_SHOW_ROUTE_MAP = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 13;
    
    //NOTICE: keep 14-17
    public static final int CMD_CHANGE_ROUTE_CHOICE_START = CMD_USER_BASE + USER_BASE_ROUTE_PLANNING + 14;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 1;
    public static final int ACTION_ROUTE_PLANNING_RETRY_GPS = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 2;
    public static final int ACTION_CHECK_RETURN_ADDRESS = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 3;
    public static final int ACTION_GET_ROUTE = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 4;
    public static final int ACTION_CANCEL_CURRENT_REQUEST = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 5;
    public static final int ACTION_PREFENCE_RESET = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 6;
    public static final int ACTION_CHECK_ACCOUNT_STATUS = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 7;
    public static final int ACTION_INIT_ROUTE_SETTING = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 8;
    public static final int ACTION_CHECK_MULTI_ROUTE_VALUE = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 9;
    public static final int ACTION_BACK_TO_ROUTE_PLANNING = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 10;
    public static final int ACTION_CHECK_SHARE_ETA = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 11;
    public static final int ACTION_ETA_SHARE = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 12;
    public static final int ACTION_REQUEST_TINY_URL = ACTION_USER_BASE + USER_BASE_ROUTE_PLANNING + 13;
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_SHOW_ROUTE_PLANNING = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 1;
    public static final int EVENT_MODEL_NO_GPS_TIMEOUT = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 2;
    public static final int EVENT_MODEL_GO_PURCHASE = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 3;
    public static final int EVENT_MODEL_GO_NAVIGATE = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 4;
    public static final int EVENT_MODEL_START_ROUTE_PLANNING = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 5;
    public static final int EVENT_MODEL_START_GET_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 6;
    public static final int EVENT_MODEL_SHARE_ETA_FINISH = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 7;
    public static final int EVENT_MODEL_SHARE_GOT_TINY_URL = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 8;
    public static final int EVENT_MODEL_SHARE_GOT_TINY_URL_FAIL = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 9;
    public static final int EVENT_MODEL_NEED_SHARE = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 10;
    public static final int EVENT_MODEL_NO_SHARE = EVENT_MODEL_USER_BASE + USER_BASE_ROUTE_PLANNING + 11;
    //-----------------------------------------------------------
    // key value between and in modules.
    //-----------------------------------------------------------
    public static final Integer KEY_I_SELECTED_PLAN = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 1);
    public static final Integer KEY_A_ROUTE_CHOICES = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 2);
    public static final Integer KEY_A_ROUTE_CHOICES_ETA = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 3);
    public static final Integer KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 4);
    public static final Integer KEY_A_ROUTE_CHOICES_LENGTH = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 5);
    public static final Integer KEY_B_IS_UPDATE_PLAN   = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 7);
    public static final Integer KEY_B_IS_UPDATE_SHOW_ROUTE = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 8);
    public static final Integer KEY_B_IS_EDITING_ORIGIN  = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 9);
    public static final Integer KEY_B_IS_REQUEST_CANCELLED      = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 10);
    public static final Integer KEY_B_IS_UPDATE_FLAGS      = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 11);
    public static final Integer KEY_B_SHOW_ROUTE_LIST      = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 12);
    public static final Integer KEY_B_ORIENTATION_CHANGED      = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 13);
    public static final Integer KEY_B_IS_BACK_FROM_ROUTE_SETTING      = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 14);
    public static final Integer KEY_B_IS_SHARE_ETA_SELECTED      = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 15);
    public static final Integer KEY_B_IS_SHARE_ETA_DISABLED    = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 16);
    public static final Integer KEY_I_SHARE_ETA_ADDRESS_TYPE    = PrimitiveTypeCache.valueOf(STATE_ROUTE_PLANNING_BASE + 17);
    //------------------------------------------------------------
    //
    //------------------------------------------------------------
     final static int ID_ROUTE_PLANNING_INFO_CONTAINER = 101;
     final static int ID_PLANNING_INFO_LEFT_ROUTE_CHOICE = 102;
     final static int ID_PLANNING_INFO_CENTER_ROUTE_CHOICE = 103;
     final static int ID_PLANNING_INFO_RIGHT_ROUTE_CHOICE = 104;
     final static int ID_NAV_BUTTON_CONTAINER = 117;
     final static int ID_STATUS_INFO = 118;
     final static int ID_ORIGIN_LABEL = 119;
     final static int ID_DEST_LABEL = 120;
     final static int ID_SETTING_LABEL = 121;
     final static int ID_NAVIGATION_BUTTON = 122;
     final static int ID_DIRECTION_BUTTON = 123;
     final static int ID_NAV_BUTTON_GAP = 124;
     final static int ID_SETTING_ORIGIN_FIELD = 125;
     final static int ID_SETTING_DEST_FIELD = 126;
     final static int ID_SETTING_ROUTE_SETTING_FIELD = 127;
     final static int ID_SETTING_VOICE_SETTING_FIELD = 128;
     final static int ID_CONFIRM_PANEL = 129;
     final static int ID_ROUTE_SETTINGS_BUTTON = 130;
     final static int ID_SETTING_GET_ROUTE_BUTTON = 131;
     final static int ID_SUMMARY_CONTAINER = 133;
     final static int ID_ROUTE_PLAN_SELECTOR = 134;
     final static int ID_TITLE_CONTAINER = 135;
     final static int ID_BOTTOM_CONTAINER = 136;
     final static int ID_SHARE_ETA_CHECK_CONTAINER = 137;
     final static int ID_SHARE_ETA_CHECK_ITEM = 138;
     
     final static int TYPE_HOME_DEST = 1;
     final static int TYPE_WORK_DEST = 2;
     final static int TYPE_OTHER_DEST = 0;
     
}

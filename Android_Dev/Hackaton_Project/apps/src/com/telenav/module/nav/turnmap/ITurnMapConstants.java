/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITurnMapConstants.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2010-12-13
 */
public interface ITurnMapConstants extends ICommonConstants
{
    //-------------------------------------------------------------
    // State definition
    //-------------------------------------------------------------
	public static final int STATE_TURN_MAP_BASE = STATE_USER_BASE + USER_BASE_TURN_MAP;
    
	public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 1);
    public static final int STATE_GETTING_TURN_MAP = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 2);
    public static final int STATE_TURN_MAP = STATE_TURN_MAP_BASE + 3;
    public static final int STATE_GET_ROUTE_EDGE = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 4);
    public static final int STATE_GO_TO_ROUTE_SUMMARY = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 5);
    public static final int STATE_GO_TO_TRAFFIC_SUMMARY = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 6);
    public static final int STATE_GO_TO_POI = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 7);
    public static final int STATE_GO_TO_NAV = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 9);
    public static final int STATE_GO_TO_MAP_SUMMARY = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 10);
    public static final int STATE_POI_RETURN = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 11);
    public static final int STATE_START_MAIN = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 12);
    public static final int STATE_GET_TURN_MAP_ERROR = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 13);
    public static final int STATE_GET_EXTRA_ROUTE_EDGE = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 14);
    public static final int STATE_SYNC_PURCHASE = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 15);
    public static final int STATE_CONTINUE_GETTING_TURN_MAP = MASK_STATE_TRANSIENT | (STATE_TURN_MAP_BASE + 16);
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_ROUTE_SUMMARY = CMD_USER_BASE + USER_BASE_TURN_MAP + 1;
    public static final int CMD_TRAFFIC_SUMMARY = CMD_USER_BASE + USER_BASE_TURN_MAP + 2;
    public static final int CMD_CANVAS_NEXT = CMD_USER_BASE + USER_BASE_TURN_MAP + 3;
    public static final int CMD_CANVAS_PREVIOUS = CMD_USER_BASE + USER_BASE_TURN_MAP + 4;
    public static final int CMD_NAVIGATE = CMD_USER_BASE + USER_BASE_TURN_MAP + 7;
    public static final int CMD_MAP_SUMMARY = CMD_USER_BASE + USER_BASE_TURN_MAP + 8;
    public static final int CMD_ZOOM_IN = CMD_USER_BASE + USER_BASE_TURN_MAP + 9;
    public static final int CMD_ZOOM_OUT = CMD_USER_BASE + USER_BASE_TURN_MAP + 10;
    public static final int CMD_FIT_MAP = CMD_USER_BASE + USER_BASE_TURN_MAP + 11;
    public static final int CMD_REQUEST_EXTRA_ROUTE = CMD_USER_BASE + USER_BASE_TURN_MAP + 12;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_TURN_MAP + 1;
    public static final int ACTION_GETTING_TURN_MAP = ACTION_USER_BASE + USER_BASE_TURN_MAP + 2;
    public static final int ACTION_CANVAS_NEXT = ACTION_USER_BASE + USER_BASE_TURN_MAP + 3;
    public static final int ACTION_CANVAS_PREVIOUS = ACTION_USER_BASE + USER_BASE_TURN_MAP + 4;
    public static final int ACTION_GET_ROUTE_EDGE = ACTION_USER_BASE + USER_BASE_TURN_MAP + 5;
    public static final int ACTION_CANCEL_NETWORK = ACTION_USER_BASE + USER_BASE_TURN_MAP + 6;
    public static final int ACTION_SHOW_TURN_MAP = ACTION_USER_BASE + USER_BASE_TURN_MAP + 7;
    public static final int ACTION_GET_EXTRA_ROUTE_EDGE = ACTION_USER_BASE + USER_BASE_TURN_MAP + 8;
    public static final int ACTION_SYNC_PURCHASE = ACTION_USER_BASE + USER_BASE_TURN_MAP + 9;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_GET_TURN_MAP = EVENT_MODEL_USER_BASE + USER_BASE_TURN_MAP + 1;
    public static final int EVENT_MODEL_SHOW_TURN_MAP = EVENT_MODEL_USER_BASE + USER_BASE_TURN_MAP + 2;
    public static final int EVENT_MODEL_REQUEST_ROUTE_EDGE = EVENT_MODEL_USER_BASE + USER_BASE_TURN_MAP + 3;
    public static final int EVENT_MODEL_INVALID_IDENTITY = EVENT_MODEL_USER_BASE + USER_BASE_TURN_MAP + 4;
    
    
    //-----------------------------------------------------------
    // key value between and in modules.
    //-----------------------------------------------------------
    public static final Integer KEY_B_RELOAD_ROUTE = PrimitiveTypeCache.valueOf(STATE_TURN_MAP_BASE + 1);
    public static final Integer KEY_B_FROM_GETTING_EXTRA = PrimitiveTypeCache.valueOf(STATE_TURN_MAP_BASE + 2);
    public static final Integer KEY_B_IS_GETTING_EXTRA = PrimitiveTypeCache.valueOf(STATE_TURN_MAP_BASE + 3);
    public static final Integer KEY_B_UPDATE_TURN_ARROW = PrimitiveTypeCache.valueOf(STATE_TURN_MAP_BASE + 4);
    public static final Integer KEY_B_UPDATE_MAP_LAYER = PrimitiveTypeCache.valueOf(STATE_TURN_MAP_BASE + 5);
    
    //------------------------------------------------------------
    // ID values definition
    //------------------------------------------------------------
    public static final int ID_TURN_MAP_PREV_TURN_ICON = 1003;
    public static final int ID_TURN_MAP_NEXT_TURN_ICON = 1004;
    public static final int ID_TURN_MAP_LIST_VIEW_ICON = 1005;
    public static final int ID_TURN_MAP_INFO_COMPONENT = 1006;
    public static final int ID_TURN_MAP_TURN_INDEX_COMPONENT = 1007;
    public static final int ID_TURN_MAP_ZOOM_IN_COMPONENT = 1008;
    public static final int ID_TURN_MAP_ZOOM_OUT_COMPONENT = 1009;
    public static final int ID_TURN_MAP_FIT_MAP_COMPONENT = 1010;
    public static final int ID_TURN_MAP_BOTTOM_CONTAINER = 1011;
    public static final int ID_TURN_MAP_ZOOM_CONTAINER = 1012;
    public static final String ID_TURN_MAP_TURN_MAP_AUDIO = "TURN_MAP_AUDIO";
}

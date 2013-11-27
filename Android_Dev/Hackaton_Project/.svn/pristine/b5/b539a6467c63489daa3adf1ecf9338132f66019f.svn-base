/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRouteSummaryConstants.java
 *
 */
package com.telenav.module.nav.routesummary;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-4
 */
interface IRouteSummaryConstants extends ICommonConstants
{
    //-------------------------------------------------------------
    // State definition
    //-------------------------------------------------------------
	public static final int STATE_ROUTE_SUMMARY_BASE = STATE_USER_BASE + USER_BASE_ROUTE_SUMMARY;
    public static final int STATE_ROUTE_SUMMARY = (STATE_USER_BASE + USER_BASE_ROUTE_SUMMARY + 1);
    public static final int STATE_GO_TO_TRAFFIC_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_SUMMARY + 2);
    public static final int STATE_GO_TO_TURN_MAP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_SUMMARY + 3);
    public static final int STATE_GO_TO_POI = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_SUMMARY + 4);
    public static final int STATE_GO_TO_MAP_SUMMARY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_SUMMARY + 5);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_ROUTE_SUMMARY = CMD_USER_BASE + USER_BASE_ROUTE_SUMMARY + 1;
    public static final int CMD_TRAFFIC_SUMMARY = CMD_USER_BASE + USER_BASE_ROUTE_SUMMARY + 2;
    public static final int CMD_ROUTE_ITEM_SELECTED = CMD_USER_BASE + USER_BASE_ROUTE_SUMMARY + 3;
    public static final int CMD_MAP_SUMMARY = CMD_USER_BASE + USER_BASE_ROUTE_SUMMARY + 4;
    public static final int CMD_NAVIGATION = CMD_USER_BASE + USER_BASE_ROUTE_SUMMARY + 5;
    public static final int CMD_DIRECTIONS = CMD_USER_BASE + USER_BASE_ROUTE_SUMMARY + 6;
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_ROUTE_SUMMARY + 1;
    public static final int ACTION_GO_TO_TRAFFIC_SUMMARY = ACTION_USER_BASE + USER_BASE_ROUTE_SUMMARY + 2;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    //-----------------------------------------------------------
    // key value between and in modules.
    //-----------------------------------------------------------
    public static final Integer KEY_O_NAV_PARAMETER = PrimitiveTypeCache.valueOf(STATE_ROUTE_SUMMARY_BASE + 1);
    
    public static final Integer KEY_I_SHOWN_SEGMENT_LENGTH = PrimitiveTypeCache.valueOf(STATE_ROUTE_SUMMARY_BASE + 2);
    
    //-----------------------------------------------------------
    // component id
    //-----------------------------------------------------------
    public static final int ID_SUMMARY_CONTAINER = 1002;
    public static final int ID_TAB_MAP_SUMMARY = 1003;
    public static final int ID_TAB_TRAFFIC_SUMMARY = 1004;
    public static final int ID_SUMMARY_ITEM_BASE = 2300;
    public static final int ID_SUMMARY_ITEM_MAX = 3000;
    public static final int ID_SUMMARY_ETA = 4000;
    public static final int ID_LOADING_ANIMATION = 5000;
    
    
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ISummaryConstants.java
 *
 */
package com.telenav.module.nav.summarycontrol;

import com.telenav.mvc.ICommonConstants;

/**
 *@author yning
 *@date 2010-12-29
 */
interface ISummaryControlConstants extends ICommonConstants
{
    //-------------------------------------------------------------
    // State definition
    //-------------------------------------------------------------
    public static final int STATE_SUMMARY_CONTROL_BASE = STATE_USER_BASE + USER_BASE_SUMMARY_CONTROL;
    public static final int STATE_GO_TO_ROUTE_SUMMARY = MASK_STATE_TRANSIENT | (STATE_SUMMARY_CONTROL_BASE + 1);
    public static final int STATE_GO_TO_MAP_SUMMARY = MASK_STATE_TRANSIENT | (STATE_SUMMARY_CONTROL_BASE + 2);
    public static final int STATE_GO_TO_TRAFFIC_SUMMARY = MASK_STATE_TRANSIENT | (STATE_SUMMARY_CONTROL_BASE + 3);
    public static final int STATE_GO_TO_POI = MASK_STATE_TRANSIENT | (STATE_SUMMARY_CONTROL_BASE + 4);
    public static final int STATE_CHECK_BACK_STATUS = MASK_STATE_TRANSIENT | (STATE_SUMMARY_CONTROL_BASE + 5);
    public static final int STATE_BACK_TO_LAST_CONTROLLER = MASK_STATE_TRANSIENT | (STATE_SUMMARY_CONTROL_BASE + 6);
    public static final int STATE_GO_TO_TURN_MAP = MASK_STATE_TRANSIENT | (STATE_SUMMARY_CONTROL_BASE + 7);
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_HANDLE_BACK = ACTION_USER_BASE + USER_BASE_SUMMARY_CONTROL + 1;
    public static final int ACTION_START_CONTROLLER = ACTION_USER_BASE + USER_BASE_SUMMARY_CONTROL + 2;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    /**
     * why we have this?
     * Because in current tree structure, the summaries are in the same layer.
     * If we go from one summary to another, as soon as new module is started, the old one is released.
     * But when entering map summary or traffic summary, they can have a transient state for network, which can be canceled.
     * In this case, we need to back to last summary, because it's still displayed as the background of the transient state.
     * That's why we need to store the last available summary module and use this event to handle it.
     */
    public static final int EVENT_MODEL_BACK_BETWEEN_SUMMARY = EVENT_MODEL_USER_BASE + USER_BASE_SUMMARY_CONTROL + 1;
    public static final int EVENT_MODEL_EXIT_SUMMARYS = EVENT_MODEL_USER_BASE + USER_BASE_SUMMARY_CONTROL + 2;
    
    //-----------------------------------------------------------
    // key value between and in modules.
    //-----------------------------------------------------------
}

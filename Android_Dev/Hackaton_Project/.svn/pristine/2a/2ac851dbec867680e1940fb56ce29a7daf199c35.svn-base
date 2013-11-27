/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStartUpConstants.java
 *
 */
package com.telenav.module.sync;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-12-2
 */
interface ISyncResConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = (STATE_USER_BASE + USER_BASE_SYNC_RES + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_FRESH_SYNC = (STATE_USER_BASE + USER_BASE_SYNC_RES + 2);
    public static final int STATE_STARTUP_SYNC = (STATE_USER_BASE + USER_BASE_SYNC_RES + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_EXIT_SYNC = (STATE_USER_BASE + USER_BASE_SYNC_RES + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_BACK_TO_MAIN = (STATE_USER_BASE + USER_BASE_SYNC_RES + 5) | MASK_STATE_TRANSIENT;
    public static final int STATE_SCOUT_GO = STATE_USER_BASE + USER_BASE_SYNC_RES + 6;
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_SCOUT_GO = STATE_COMMON_BASE + USER_BASE_SYNC_RES + 1;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_CHECK_TYPE = ACTION_USER_BASE + USER_BASE_SYNC_RES + 1;
    public static final int ACTION_FRESH_SYNC = ACTION_USER_BASE + USER_BASE_SYNC_RES + 2;
    public static final int ACTION_STARTUP_SYNC = ACTION_USER_BASE + USER_BASE_SYNC_RES + 3;
    public static final int ACTION_EXIT_SYNC = ACTION_USER_BASE + USER_BASE_SYNC_RES + 4;
    public static final int ACTION_HANDLE_ERROR = ACTION_USER_BASE + USER_BASE_SYNC_RES + 5;
    public static final int ACTION_CLOSE_MENU = ACTION_USER_BASE + USER_BASE_SYNC_RES + 6;
    public static final int ACTION_CHECK_REGION_DETECT_STATUS = ACTION_USER_BASE + USER_BASE_SYNC_RES + 7;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_DO_FRESH_SYNC = EVENT_MODEL_USER_BASE + USER_BASE_SYNC_RES + 1;
    public static final int EVENT_MODEL_DO_STARTUP_SYNC = EVENT_MODEL_USER_BASE + USER_BASE_SYNC_RES + 2;
    public static final int EVENT_MODEL_DO_EXIT_SYNC = EVENT_MODEL_USER_BASE + USER_BASE_SYNC_RES + 3;
    public static final int EVENT_MODEL_SYNC_FINISH = EVENT_MODEL_USER_BASE + USER_BASE_SYNC_RES + 4;
    
    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------
    public static final int ID_PROGRESS_BAR = USER_BASE_SYNC_RES + 1; 
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static final Integer KEY_B_LOGIN_STEP1_FINISHED = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_SYNC_RES + 1);
    public static final Integer KEY_B_LOGIN_STEP2_FINISHED = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_SYNC_RES + 2);
    public static final Integer KEY_I_LOGIN_STEP = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_SYNC_RES + 3);
    public static final Integer KEY_B_SYNCRES_OCCUR_ERROR = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_SYNC_RES + 4);
    
    public static final int SEND_LOGIN_REQUEST_STEP_1 = 1; // send COMMON SERVER
    public static final int SEND_LOGIN_REQUEST_STEP_2 = 2; // send RESOURCE SERVER
    //public static final int SEND_LOGIN_REQUEST_STEP_3 = 3; // get carrier from DIM.
    
    public static final int TYPE_TRANS_FINISH = 0; 
    public static final int TYPE_TRANS_ERROR = 1;
    public static final int TYPE_NETWORK_ERROR = 2;
    
    // ------------------------------------------------------------
    // The Component id definition
    // ------------------------------------------------------------
    
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IDsrConstants.java
 *
 */
package com.telenav.module.dsr;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date Aug 23, 2010
 */
interface IDsrConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 1);
    public static final int STATE_TEST_THINKING = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 2);
    public static final int STATE_UNIT_TEST = (STATE_USER_BASE + USER_BASE_DSR + 3);
    
    public static final int STATE_GO_TO_MAP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 4);
    public static final int STATE_GO_TO_NAV = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 5);
    public static final int STATE_DO_SEARCH = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 6);
    public static final int STATE_GO_TO_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 7);
    public static final int STATE_GO_TO_POI_LIST = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 8);
    public static final int STATE_GO_RESUME_TRIP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 9);
    public static final int STATE_RETURN_ADDRESSS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 10);
    public static final int STATE_GO_TO_MULTI_MATCH = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 11);
    public static final int STATE_STOP_SELECTED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 12);
    public static final int STATE_GO_TO_OFFICE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 13);
    public static final int STATE_GO_TO_POI_MAP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 14);
    public static final int STATE_CHECK_NETWORK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DSR + 15);

    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_DO_FINISH = STATE_COMMON_BASE + USER_BASE_DSR + 1;
    public static final int CMD_DO_TEST = STATE_COMMON_BASE + USER_BASE_DSR + 2;

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_DSR + 1;

    public static final int ACTION_MANNUAL_STOP = ACTION_USER_BASE + USER_BASE_DSR + 2;
    
    public static final int ACTION_SEND_DATA = ACTION_USER_BASE + USER_BASE_DSR + 3;
    
    public static final int ACTION_AUTO_STOP = ACTION_USER_BASE + USER_BASE_DSR + 4;
    
    public static final int ACTION_DO_SEARCH = ACTION_USER_BASE + USER_BASE_DSR + 5;
    
    public static final int ACTION_PREPARE_AUDIO = ACTION_USER_BASE + USER_BASE_DSR + 6;
    
    public static final int ACTION_PLAY_ERROR_AUDIO = ACTION_USER_BASE + USER_BASE_DSR + 7;
    
    public static final int ACTION_CHECK_NETWORK = ACTION_USER_BASE + USER_BASE_DSR + 8;

    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_GO_TO_MAP = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 1;
    public static final int EVENT_MODEL_GO_TO_NAV = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 2;
    public static final int EVENT_MODEL_GO_TO_SEARCH = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 3;
    public static final int EVENT_MODEL_DO_THINKING = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 4;
    public static final int EVENT_MODEL_RECO_ERROR = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 5;
    public static final int EVENT_MODEL_ERROR_TIMES_EXCEED = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 6;
    public static final int EVENT_MODEL_SET_HOME = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 7;
    public static final int EVENT_MODEL_GO_TO_AC = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 8;
    public static final int EVENT_MODEL_GOTO_POI_LIST = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 9;
    public static final int EVENT_MODEL_RESUME_TRIP = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 10;
    public static final int EVENT_MODEL_ADDRESS_GOT = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 11;
    public static final int EVENT_MODEL_MULTI_MATCH = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 12;
    public static final int EVENT_MODEL_PLAY_ERROR_FINISH = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 13;
    public static final int EVENT_MODEL_SET_OFFICE = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 14;
    public static final int EVENT_MODEL_MAP_POI_LIST = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 15;
    public static final int EVENT_MODEL_DO_INIT = EVENT_MODEL_USER_BASE + USER_BASE_DSR + 16;
    
    // ------------------------------------------------------------
    // The Controller event id definition
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static Integer KEY_B_IS_DO_RECOGNIZE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DSR + 1);
    
    public static Integer KEY_I_RES_ID = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DSR + 2);
    
    public static Integer KEY_V_MULTI_MATCH_AUDIOS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DSR + 4);
    
    public static Integer KEY_B_IS_PLAY_ERROR_AUDIO = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DSR + 5);
    
    public static Integer KEY_B_IS_MAP_POI = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DSR + 7);
    
    public static Integer KEY_S_DSR_SILENCE_MASSAGE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DSR + 8);

    // ------------------------------------------------------------
    // The Component id definition
    // ------------------------------------------------------------
    
    public static final int ID_INFO_CONTAINER = 1233;
    public static final int ID_INFO_ACTION_LABEL = 1234;
    public static final int ID_BUTTON_FINISH = 1235;
    public static final int ID_INSTRUCTION_CONTAINER = 1236;
    public static final int ID_MIC_CONTAINER = 1237;
    public static final int ID_DASHBOARD_CIRCLE_ANNIMATION = 1238;
    public static final int ID_THINKING_CONTAINER = 1239;
    
}

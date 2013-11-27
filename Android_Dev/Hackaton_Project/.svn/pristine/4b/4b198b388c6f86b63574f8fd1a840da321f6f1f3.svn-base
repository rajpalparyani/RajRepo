/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IEntryConstants.java
 *
 */
package com.telenav.module.entry;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */
interface IEntryConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = (STATE_USER_BASE + USER_BASE_ENTRY + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_ACCOUNT_STATUS = (STATE_USER_BASE + USER_BASE_ENTRY + 2)| MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_LOGIN = (STATE_USER_BASE + USER_BASE_ENTRY + 3) | MASK_STATE_TRANSIENT;
 
    // as move it into ICommonContant, no need to declare it in this class
    // public static final int STATE_LAUNCH_SECRET_FUNCTIONS = (STATE_USER_BASE + USER_BASE_ENTRY + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_MAIN = (STATE_USER_BASE + USER_BASE_ENTRY + 5);
    public static final int STATE_SYNC_SERVICELOCATOR_SDP = (STATE_USER_BASE + USER_BASE_ENTRY + 6) | MASK_STATE_TRANSIENT;
    public static final int STATE_SYNC_SERVICELOCATOR_SDP_ERROR_MSG = (STATE_USER_BASE + USER_BASE_ENTRY + 7) | MASK_STATE_TRANSIENT;
    public static final int STATE_EXIT_APP = (STATE_USER_BASE + USER_BASE_ENTRY + 8)| MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_SYNC_RES = (STATE_USER_BASE + USER_BASE_ENTRY + 9) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_UPGRADE = (STATE_USER_BASE + USER_BASE_ENTRY + 10) | MASK_STATE_TRANSIENT;
    public static final int STATE_UPGRADE_SELECTION = (STATE_USER_BASE + USER_BASE_ENTRY + 11);
    public static final int STATE_SYNC_PURCHASE_FAIL_MSG = (STATE_USER_BASE + USER_BASE_ENTRY + 12) | MASK_STATE_TRANSIENT;
    public static final int STATE_SYNC_PURCHASE = (STATE_USER_BASE + USER_BASE_ENTRY + 13) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_AC_TYPE = (STATE_USER_BASE + USER_BASE_ENTRY + 14) | MASK_STATE_TRANSIENT;
    
    //plugin
    public static final int STATE_START_PLUGIN = (STATE_USER_BASE + USER_BASE_ENTRY + 15) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_GOTO_LOCATION_SETTING = (STATE_USER_BASE + USER_BASE_ENTRY + 16) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_LOCATION_SETTING_OK = (STATE_USER_BASE + USER_BASE_ENTRY + 17) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_LOCATION_SETTING_CANCEL = (STATE_USER_BASE + USER_BASE_ENTRY + 18) | MASK_STATE_TRANSIENT;
    
    //upsell
    public static final int STATE_GOTO_PURCHASE_NAV = (STATE_USER_BASE + USER_BASE_ENTRY + 19) | MASK_STATE_TRANSIENT;
    public static final int STATE_UP_SELL = (STATE_USER_BASE + USER_BASE_ENTRY + 20) | MASK_STATE_TRANSIENT;
    public static final int STATE_PURCHASE_FINISH = (STATE_USER_BASE + USER_BASE_ENTRY + 21) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_STARTUP_TYPE = (STATE_USER_BASE + USER_BASE_ENTRY + 22) | MASK_STATE_TRANSIENT;
    public static final int STATE_SYNC_RES_AND_CHECK_UPGRADE = (STATE_USER_BASE + USER_BASE_ENTRY + 23) | MASK_STATE_TRANSIENT;
    public static final int STATE_UPLOAD_PREFERENCE = (STATE_USER_BASE + USER_BASE_ENTRY + 24) | MASK_STATE_TRANSIENT;
    public static final int STATE_DATASET_SWITCH_RESYNC = (STATE_USER_BASE + USER_BASE_ENTRY + 25) | MASK_STATE_TRANSIENT;
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    // as move it into ICommonContant, no need to declare it in this class
//    public static final int CMD_SECRET_KEY = STATE_USER_BASE + USER_BASE_ENTRY + 1;
    public static final int CMD_UPGRADE_NOW = STATE_USER_BASE + USER_BASE_ENTRY + 2;
    public static final int CMD_REMIND_LATER = STATE_USER_BASE + USER_BASE_ENTRY + 3;
    public static final int CMD_DONOT_ASK = STATE_USER_BASE + USER_BASE_ENTRY + 4;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_ENTRY + 1;
    public static final int ACTION_LOGIN = ACTION_USER_BASE + USER_BASE_ENTRY + 2;
    public static final int ACTION_CHECK_ACCOUNT_STATUS = ACTION_USER_BASE + USER_BASE_ENTRY + 3;
    public static final int ACTION_EXIT_APP = ACTION_USER_BASE + USER_BASE_ENTRY + 4;
    public static final int ACTION_BG_STARTUP = ACTION_USER_BASE + USER_BASE_ENTRY + 5;
    public static final int ACTION_CHECK_UPGRADE = ACTION_USER_BASE + USER_BASE_ENTRY + 6;
    public static final int ACTION_DONOT_ASK_FOR_UPGRADE = ACTION_USER_BASE + USER_BASE_ENTRY + 7;
    public static final int ACTION_GOTO_OTA = ACTION_USER_BASE + USER_BASE_ENTRY + 8;
    public static final int ACTION_LOCATION_SETTING_OK = ACTION_USER_BASE + USER_BASE_ENTRY + 9;
    public static final int ACTION_START_FLOW = ACTION_USER_BASE + USER_BASE_ENTRY + 10;
    public static final int ACTION_GO_TO_PURCHASE_NAV = ACTION_USER_BASE + USER_BASE_ENTRY + 11;
    public static final int ACTION_SYNC_RES_AND_CHECK_UPGRADE = ACTION_USER_BASE + USER_BASE_ENTRY + 12;
    public static final int ACTION_UPLOAD_LOGIN_INFO = ACTION_USER_BASE + USER_BASE_ENTRY + 13;
    public static final int ACTION_DATASET_SWITCH_RESYNC = ACTION_USER_BASE + USER_BASE_ENTRY + 14;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_SYNC_SERVICELOCATOR_SDP = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 1;
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 2;
    public static final int EVENT_MODEL_SYNC_SERVICELOCATOR_SDP_SUCC = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 3;
    public static final int EVENT_MODEL_SYNC_SERVICELOCATOR_SDP_FAIL = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 4;
    public static final int EVENT_MODEL_LOGIN = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 5;
    public static final int EVENT_MODEL_SYNC_RES = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 6;
    public static final int EVENT_MODEL_CHECK_PTN = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 7;
    public static final int EVENT_MODEL_CHECK_CONTEXT = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 8;
    public static final int EVENT_MODEL_LOGIN_FAIL = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 9;
    public static final int EVENT_MODEL_CHECK_UPGRADE = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 10;
    public static final int EVENT_MODEL_GOTO_UPGRADE = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 11;
    public static final int EVENT_MODEL_GOTO_LOCATION_SETTING = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 12;
    public static final int EVENT_MODEL_GOTO_PURCHASE_NAV = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 13;
    public static final int EVENT_MODEL_GO_PURCHASE = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 14;
    public static final int EVENT_MODEL_SYNC_RES_AND_CHECK_UPGRADE = EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 15;
    public static final int EVENT_MODEL_SYNC_PURCHASE_FAIL= EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 16;
    public static final int EVENT_MODEL_DATASET_SWITCH_RESYNC= EVENT_MODEL_USER_BASE + USER_BASE_ENTRY + 17;

    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static Integer KEY_B_IS_NEED_UPGRADE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_ENTRY + 1);
    public static Integer KEY_B_IS_FORCE_UPGRADE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_ENTRY + 2);
    public static Integer KEY_B_IS_SECRET_STARTED = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_ENTRY + 3);
    public static Integer KEY_B_IS_DISABLE_LOCATION_SETTING = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_ENTRY + 4);
    public static Integer KEY_O_IS_UPGRADE_LISTENER = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_ENTRY + 5);
    public static Integer KEY_B_DATASET_RESYNC = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_ENTRY + 6);
    
    // ------------------------------------------------------------
    // The Component id definition
    // ------------------------------------------------------------
    public static final int DEFAULT_SPLASH_TIMEOUT = 1000;
    public static final int ID_BOTTOM_CONTAINER = USER_BASE_ENTRY + 100;
    public static final int ID_BUTTON_UPGRADE = USER_BASE_ENTRY   + 101;
}

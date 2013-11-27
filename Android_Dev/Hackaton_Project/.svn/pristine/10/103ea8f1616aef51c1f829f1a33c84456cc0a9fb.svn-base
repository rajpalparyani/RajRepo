/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IDashbaordConstants.java
 *
 */
package com.telenav.module.drivingshare;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2012-2-3
 */
public interface IDrivingShareConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_DRIVING_SHARE_SETTING_BASE = STATE_USER_BASE + USER_BASE_DRIVING_SHARE_SETTING;
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_DRIVING_SHARE_SETTING_BASE + 1);

    public static final int STATE_MAIN = STATE_DRIVING_SHARE_SETTING_BASE + 2;
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_DRIVING_SHARE_SETTING + 1;
    public static final int ACTION_SAVE = ACTION_USER_BASE + USER_BASE_DRIVING_SHARE_SETTING + 2;
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_DRIVING_SHARE_SETTING_BASE = EVENT_MODEL_USER_BASE + USER_BASE_DRIVING_SHARE_SETTING;
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_DRIVING_SHARE_SETTING_BASE + 1;
    //------------------------------------------------------------
    // component id
    //------------------------------------------------------------
    public static final int ID_DRIVING_SHARE_SWITCHER = 10001;
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static final Integer KEY_B_SHARE_REAL_TIME_SET = PrimitiveTypeCache.valueOf(STATE_DRIVING_SHARE_SETTING_BASE + 1);
    public static final Integer KEY_B_SHARE_REAL_TIME_ENABLE = PrimitiveTypeCache.valueOf(STATE_DRIVING_SHARE_SETTING_BASE + 2);
    // A string to save the address inputed by user
    
    //public static boolean isOnboard = AndroidConnectivityListener.isOnboardMode(true);
    public final static int[][] UI_COMMAND_TABLE = new int[][]
            {
                //portrait command
                //{R.id.commonOneboxDsrButton,                isOnboard ? CMD_NONE : CMD_COMMON_DSR},
//                {R.id.commonOneboxTextView,                 CMD_COMMON_GOTO_ONEBOX},
            };
}

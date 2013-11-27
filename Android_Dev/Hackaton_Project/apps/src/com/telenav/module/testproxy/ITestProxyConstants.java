/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ITestProxyConstants.java
 *
 */
package com.telenav.module.testproxy;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2012-11-21
 */
public interface ITestProxyConstants extends ICommonConstants
{
    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_TEST_PROXY + 1;
    public static final int STATE_REQUESTING = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_TEST_PROXY + 2);
    public static final int STATE_RESULT = STATE_USER_BASE + USER_BASE_TEST_PROXY + 3;
    public static final int STATE_UPSELL_MAIN = STATE_USER_BASE + USER_BASE_TEST_PROXY + 4;
    public static final int STATE_UPSELL_SHOW_OPTIONS = STATE_USER_BASE + USER_BASE_TEST_PROXY + 5;
    
    public static final int CMD_LOGIN = CMD_USER_BASE + USER_BASE_TEST_PROXY + 1;
    public static final int CMD_SYNC_PURCHASE = CMD_USER_BASE + USER_BASE_TEST_PROXY + 2;
    public static final int CMD_SYNC_PREFERENCE = CMD_USER_BASE + USER_BASE_TEST_PROXY + 3;
    public static final int CMD_SYNC_SETTING_DATA = CMD_USER_BASE + USER_BASE_TEST_PROXY + 4;
    public static final int CMD_SHOW_UPSELL_MAIN = CMD_USER_BASE + USER_BASE_TEST_PROXY + 5;
    public static final int CMD_UPSELL_SUBMIT = CMD_USER_BASE + USER_BASE_TEST_PROXY + 8;
    public static final int CMD_UPSELL_PURCHASE = CMD_USER_BASE + USER_BASE_TEST_PROXY + 9;
    
    public static final int ACTION_DO_LOGIN = ACTION_USER_BASE + USER_BASE_TEST_PROXY + 1;
    public static final int ACTION_DO_SYNC_PURCHASED = ACTION_USER_BASE + USER_BASE_TEST_PROXY + 2;
    public static final int ACTION_DO_SYNC_PREFERENCE = ACTION_USER_BASE + USER_BASE_TEST_PROXY + 3;
    public static final int ACTION_DO_SYNC_SETTING_DATA = ACTION_USER_BASE + USER_BASE_TEST_PROXY + 4;
    public static final int ACTION_DO_GET_UPSELL_INFO = ACTION_USER_BASE + USER_BASE_TEST_PROXY + 5;
    public static final int ACTION_DO_PURCHASE = ACTION_USER_BASE + USER_BASE_TEST_PROXY + 6;
    
    public static final int EVENT_MODEL_SHOW_RESULT = EVENT_MODEL_USER_BASE + USER_BASE_TEST_PROXY + 1;
    public static final int EVENT_MODEL_SHOW_UPSELL_OPTIONS = EVENT_MODEL_USER_BASE + USER_BASE_TEST_PROXY + 2;
    
    public static final Integer KEY_V_REQUEST_RESULT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_TEST_PROXY + 1);
    
    public static final Integer KEY_A_STRING_OPTIONS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_TEST_PROXY + 2);
    
    public static final Integer KEY_A_INT_CMDS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_TEST_PROXY + 3);
    
    public static final Integer KEY_S_PTN = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_TEST_PROXY + 4);
    
    public static final Integer KEY_V_UPSELL_OPTIONS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_TEST_PROXY + 5);
    
    public static final Integer KEY_B_IS_IN_UPSELL_SESSION = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_TEST_PROXY + 6);
    
    public static final Integer KEY_I_OPTION_INDEX = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_TEST_PROXY + 7);
    
    public static final int ID_PTN_INPUT = STATE_USER_BASE + USER_BASE_TEST_PROXY + 1;
    
    public static final int ID_OPTIONS_BASE = STATE_USER_BASE + USER_BASE_TEST_PROXY + 200;
}
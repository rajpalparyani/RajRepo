/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * INativeShareConstants.java
 *
 */
package com.telenav.module.nativeshare;

import com.telenav.mvc.ICommonConstants;

/**
 *@author wchshao
 *@date Mar 18, 2013
 */
interface INativeShareConstants extends ICommonConstants
{

    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_NATIVE_SHARE =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_NATIVE_SHARE + 1);
    public static final int STATE_INIT =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_NATIVE_SHARE + 2);
    public static final int STATE_NATIVE_SHARE_FINISH =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_NATIVE_SHARE + 3);
    public static final int STATE_NATIVE_SHARE_CANCEL =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_NATIVE_SHARE + 4);
    public static final int STATE_NATIVE_SHARE_REQUEST_TINY_URL =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_NATIVE_SHARE + 5);

    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_NATIVE_SHARE + 1;
    
    public static final int ACTION_NATIVE_SHARE = ACTION_USER_BASE + USER_BASE_NATIVE_SHARE + 2;

    public static final int ACTION_NATIVE_SHARE_REQUEST_TINY_URL = ACTION_USER_BASE + USER_BASE_NATIVE_SHARE + 3;
    

    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_NATIVE_SHARE_FINISH = EVENT_MODEL_USER_BASE + USER_BASE_NATIVE_SHARE + 1;
    
    public static final int EVENT_MODEL_NATIVE_SHARE_CANCEL = EVENT_MODEL_USER_BASE + USER_BASE_NATIVE_SHARE + 2;

    public static final int EVENT_MODEL_NATIVE_SHARE_REQUEST_TINY_URL = EVENT_MODEL_USER_BASE + USER_BASE_NATIVE_SHARE + 3;
    
    public static final int EVENT_MODEL_START_NATIVE_SHARE = EVENT_MODEL_USER_BASE + USER_BASE_NATIVE_SHARE + 4;
    
    // ------------------------------------------------------------
    // The key id definition
    // ------------------------------------------------------------

}

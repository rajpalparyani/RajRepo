/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IVbbDetailConstants.java
 *
 */
package com.telenav.module.vbb;

import com.telenav.mvc.ICommonConstants;

/**
 *@author yning
 *@date 2013-1-6
 */
public interface IVbbDetailConstants extends ICommonConstants
{
    // -------------------------------------------------------------
    // State definition
    // -------------------------------------------------------------
    public static final int STATE_VBB_DETAIL_BASE = STATE_USER_BASE + USER_BASE_VBB_DETAIL;
    public static final int STATE_VBB_DETAIL = STATE_VBB_DETAIL_BASE + 1;
    public static final int STATE_GOTO_MAP_AD = (STATE_VBB_DETAIL_BASE + 2) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_DRIVE_TO_AD = (STATE_VBB_DETAIL_BASE + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_BACK_TO_INVOKER = (STATE_VBB_DETAIL_BASE + 4) | MASK_STATE_TRANSIENT;
    
    public static final int EVENT_MODEL_MAP_AD = EVENT_MODEL_USER_BASE + USER_BASE_VBB_DETAIL + 1;
    public static final int EVENT_MODEL_DRIVE_TO_AD = EVENT_MODEL_USER_BASE + USER_BASE_VBB_DETAIL + 2;
    
    public static final String KEY_ADDETAIL_WEBVIEW = "addetail_webview";
}

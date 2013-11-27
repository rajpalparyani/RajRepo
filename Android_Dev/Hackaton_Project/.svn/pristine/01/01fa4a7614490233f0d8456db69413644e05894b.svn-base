/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IBrowserSdkConstants.java
 *
 */
package com.telenav.module.browsersdk;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2010-12-29
 */
public interface IBrowserSdkConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_BROWSER_BASE                  = (STATE_USER_BASE + USER_BASE_BROWSERSDK );
    public static final int STATE_BROWSER_INIT                  = (STATE_BROWSER_BASE + 1)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_MAIN                  = (STATE_BROWSER_BASE + 2);
    public static final int STATE_BROWSER_GOTO_AC               = (STATE_BROWSER_BASE + 3)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_GOTO_MAP              = (STATE_BROWSER_BASE + 4)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_GOTO_NAV              = (STATE_BROWSER_BASE + 5)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_GOTO_SHARE_ADDR       = (STATE_BROWSER_BASE + 6)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_GOTO_SEARCH_NEARBY    = (STATE_BROWSER_BASE + 7)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_SYNC_PURCHASE_STATUS  = (STATE_BROWSER_BASE + 8)  | MASK_STATE_TRANSIENT;
//    public static final int STATE_BROWSER_REGISTRATE_FINISH     = (STATE_BROWSER_BASE + 9)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_GOTO_LAUNCH_LOCALAPP  = (STATE_BROWSER_BASE + 10)  | MASK_STATE_TRANSIENT;
    public static final int STATE_BROWSER_GOTO_LAUNCH_SETTING  = (STATE_BROWSER_BASE + 11)  | MASK_STATE_TRANSIENT;

    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------

    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT                 = ACTION_USER_BASE + USER_BASE_BROWSERSDK + 1;
    public static final int ACTION_AC_ADDRESS           = ACTION_USER_BASE + USER_BASE_BROWSERSDK + 2;

    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_BROWSER       = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 1;
    public static final int EVENT_MODEL_GOTO_AC              = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 2;
    public static final int EVENT_MODEL_GOTO_MAP             = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 3;
    public static final int EVENT_MODEL_GOTO_NAV             = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 4;
    public static final int EVENT_MODEL_UPDATE_MAIN          = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 5;
    public static final int EVENT_MODEL_GOTO_SHARE_ADDR      = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 6;
    public static final int EVENT_MODEL_GOTO_SEARCH_NEARBY   = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 7;
    public static final int EVENT_MODEL_SYNC_PURCHASE_STATUS = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 8;
    public static final int EVENT_MODEL_SYNC_PURCHASE_STATUS_DONE = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 9;
//    public static final int EVENT_MODEL_EXIT_BROWSER         = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 10;
    public static final int EVENT_MODEL_UPSELL_FINISH    = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 11;
    public static final int EVENT_MODEL_LAUNCH_LOCALAPP      = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 12;
    public static final int EVENT_MODEL_LAUNCH_SETTING      = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 13;
    public static final int EVENT_MODEL_REGISTRATE_CANCEL      = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 14;
    public static final int EVENT_MODEL_DO_VALIDATE_ADDRESS      = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 15;
    public static final int EVENT_MODEL_SELECT_ADDRESS      = EVENT_MODEL_USER_BASE + USER_BASE_BROWSERSDK + 16;
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static final Integer KEY_S_WINDOW_MODE       = PrimitiveTypeCache.valueOf(STATE_BROWSER_BASE + 1);
    public static final Integer KEY_O_PRIVATE_ADDRESS   = PrimitiveTypeCache.valueOf(STATE_BROWSER_BASE + 2);
    public static final Integer KEY_O_NEW_WINDOW_URL    = PrimitiveTypeCache.valueOf(STATE_BROWSER_BASE + 3);
    public static final Integer KEY_B_FROM_APPSTORE    = PrimitiveTypeCache.valueOf(STATE_BROWSER_BASE + 4);
    public static final Integer KEY_O_FLIPPING_TIME_STAMP = PrimitiveTypeCache.valueOf(STATE_BROWSER_BASE + 5);
    
    public static final String BROWSER_EXTRA_URL_DIR_STR = "/appstore/extra/myExtras.do";
	public static final String BROWSER_VBB_URL_DIR_STR = "/htmlpoi/html/adsinfo.do";
    public final String KEY_EXTRAS = "Extras";
}

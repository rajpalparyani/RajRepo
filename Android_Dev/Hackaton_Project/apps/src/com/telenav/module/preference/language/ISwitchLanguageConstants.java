/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ISwitchLangConstants.java
 *
 */
package com.telenav.module.preference.language;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@modifier wzhu
 *@date 2011-3-9
 */
interface ISwitchLanguageConstants extends ICommonConstants
{

    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_SWITCHLANG_BASE           = STATE_USER_BASE + USER_BASE_SWITCHLANG;
    public static final int STATE_SWITCHLANG_SYNC         = (STATE_SWITCHLANG_BASE + 2) | MASK_STATE_TRANSIENT;
    public static final int STATE_SWITCHLANG_SUCCESS        = (STATE_SWITCHLANG_BASE + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_SWITCHLANG_FAIL           = (STATE_SWITCHLANG_BASE + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_SWITCHLANG_FINISHED           = (STATE_SWITCHLANG_BASE + 5) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_NAV_IS_RUNNING                       = (STATE_SWITCHLANG_BASE + 6) | MASK_STATE_TRANSIENT;
    public static final int STATE_GO_TO_NAV                       = (STATE_SWITCHLANG_BASE + 7) | MASK_STATE_TRANSIENT;
    
    //-----------------------------------------------------------
    // model event definition
    //-----------------------------------------------------------
    public static final int EVENT_MODEL_SWITCHLANG_BASE     = EVENT_MODEL_USER_BASE + USER_BASE_SWITCHLANG;
    public static final int EVENT_MODEL_SWITCHLANG_FINISHED = (EVENT_MODEL_SWITCHLANG_BASE + 4) ;
    public static final int EVENT_MODEL_NAV_IS_RUNNING = (EVENT_MODEL_SWITCHLANG_BASE + 5) ;
    public static final int EVENT_MODEL_NAV_IS_NOT_RUNNING = (EVENT_MODEL_SWITCHLANG_BASE + 6) ;
    
    //-----------------------------------------------------------
    // action id definition
    //-----------------------------------------------------------
    public static final int ACTION_SWITCHLANG_BASE          = ACTION_USER_BASE + USER_BASE_SWITCHLANG;
    public static final int ACTION_SWITCHLANG_SYNC          = (ACTION_SWITCHLANG_BASE + 1) ;
    public static final int ACTION_SWITCHLANG_CANCEL         = (ACTION_SWITCHLANG_BASE + 2) ;
    public static final int ACTION_CHECK_NAV_IS_RUNNING         = (ACTION_SWITCHLANG_BASE + 3) ;
    
    public static Integer KEY_S_LAST_LOCALE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_SWITCHLANG + 1);
    public static Integer KEY_S_LAST_LAUGUAGECODE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_SWITCHLANG + 2);
}

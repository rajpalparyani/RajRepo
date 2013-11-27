/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IShareConstants.java
 *
 */
package com.telenav.module.feedback;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
interface IFeedbackConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FEEDBACK + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_FEEDBACK + 2;
    
    public static final int STATE_SUBMIT_FEEDBACK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FEEDBACK + 3);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------

    public static final int CMD_SUMBIT = CMD_USER_BASE + USER_BASE_FEEDBACK + 1;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_FEEDBACK + 1;
    
    public static final int ACTION_SUBMIT = ACTION_USER_BASE + USER_BASE_FEEDBACK + 2;
    

    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_FEEDBACK + 1;
    
    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------
    public static final int ID_FEEDBACK_TEXTAREA = USER_BASE_FEEDBACK + 1;
    
    public static final int ID_FEEDBACK_SUBMIT_BUTTON = USER_BASE_FEEDBACK + 2;
    
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    // A string to save the feedback
    public static Integer KEY_S_FEEDBACK = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FEEDBACK + 1);


    
}

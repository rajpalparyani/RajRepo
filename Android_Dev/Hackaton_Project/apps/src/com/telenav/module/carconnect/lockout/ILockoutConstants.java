/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ILockoutConstants.java
 *
 */
package com.telenav.module.carconnect.lockout;

import com.telenav.mvc.ICommonConstants;

/**
 *@author chihlh
 *@date Mar 8, 2012
 */
interface ILockoutConstants extends ICommonConstants
{
    public static final int STATE_INIT = (STATE_USER_BASE + USER_BASE_LOCKOUT + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_SCREEN_UNLOCKED = (STATE_USER_BASE + USER_BASE_LOCKOUT + 2) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_SCREEN_LOCKED = STATE_USER_BASE + USER_BASE_LOCKOUT + 3;
    
    // ------------------------------------------------------------
    // Model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_UNLOCK_SCREEN = EVENT_MODEL_USER_BASE + USER_BASE_LOCKOUT + 1;
    public static final int EVENT_MODEL_LOCK_SCREEN = EVENT_MODEL_USER_BASE + USER_BASE_LOCKOUT + 2;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_LOCKOUT + 1;

}

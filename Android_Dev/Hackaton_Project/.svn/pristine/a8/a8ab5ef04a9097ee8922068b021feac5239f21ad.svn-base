/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAddFavoriteConstants.java
 *
 */
package com.telenav.module.ac.contacts;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
interface IContactsConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_MAIN = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_CONTACTS + 1);

    public static final int STATE_RETURN_CONTACT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_CONTACTS + 2);

    public static final int STATE_VALIDATE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_CONTACTS + 3);
    
    public static final int STATE_ONE_RESULT_RETURNED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_CONTACTS + 4);
    
    public static final int STATE_RETURN_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_CONTACTS + 6);
    
    public static final int STATE_CHOOSE_NATIVE_CONTACT = STATE_USER_BASE + USER_BASE_CONTACTS + 7;

    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_SUBMIT = CMD_USER_BASE + USER_BASE_CONTACTS + 1;
    
    public static final int CMD_SELECT_ADDRESS = CMD_USER_BASE + USER_BASE_CONTACTS + 2;
    
    public static final int CMD_SELECT_PHONE = CMD_USER_BASE + USER_BASE_CONTACTS + 3;

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_CONTACTS + 1;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_RETURN_CONTACT = EVENT_MODEL_USER_BASE + USER_BASE_CONTACTS + 1;

    public static final int EVENT_MODEL_VALIDATE_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_CONTACTS + 2;
    
    public static final int EVENT_MODEL_CHOOSE_NATIVE_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_CONTACTS + 4;
    
    public static final int EVENT_MODEL_CHOOSE_NATIVE_PHONE = EVENT_MODEL_USER_BASE + USER_BASE_CONTACTS + 5;

    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    
    //Vector to save the alternative choices
    public static Integer KEY_V_ALTERNATIVE_CHOICES = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_CONTACTS + 1);
    
    //Index of the selected contact
    public static Integer KEY_I_INDEX = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_CONTACTS + 2);
    
}
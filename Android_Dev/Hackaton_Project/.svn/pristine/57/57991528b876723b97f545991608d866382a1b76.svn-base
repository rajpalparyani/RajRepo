/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRecentConstants.java
 *
 */
package com.telenav.module.ac.addressValidator;

import com.telenav.mvc.ICommonConstants;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-26
 */
interface IAddressValidatorConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
	public static final int STATE_ADDRESS_VALIDATOR_BASE = STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR;
	
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 1);

    public static final int STATE_MAIN = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 2);

    public static final int STATE_ERROR_ADDRESS_NOT_FOUND = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 3);

    public static final int STATE_CHOOSE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 4);
    
    public static final int STATE_RETURN_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 5);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 1;

    public static final int ACTION_VALIDATE_HOME = ACTION_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 2;
    
    public static final int ACTION_CANCEL_VALIDATING = ACTION_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 3;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 1;

    public static final int EVENT_MODEL_RETURN_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 2;
    
    public static final int EVENT_MODEL_CHOOSE_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 3;

    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    
}

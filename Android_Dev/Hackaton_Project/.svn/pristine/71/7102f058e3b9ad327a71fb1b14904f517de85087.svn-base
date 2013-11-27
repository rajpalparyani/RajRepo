/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRecentConstants.java
 *
 */
package com.telenav.module.ac.stopsselection;

import com.telenav.mvc.ICommonConstants;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-26
 */
interface IStopsSelectionConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
	public static final int STATE_ADDRESS_VALIDATOR_BASE = STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR;

    public static final int STATE_CHOOSE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 1);
    
    public static final int STATE_RETURN_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 2);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_SELECT_ADDRESS = CMD_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 1;

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_RETURN_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 1;
    
    public static final int EVENT_MODEL_CHOOSE_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 2;

    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    
}

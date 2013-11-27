/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAddFavoriteConstants.java
 *
 */
package com.telenav.module.ac.airport;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
interface IAirportConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AIRPORT + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_AIRPORT + 2;
    
    public static final int STATE_VALIDATE_AIRPORT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AIRPORT + 3);
    
    public static final int STATE_RETURN_AIRPORT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AIRPORT + 4);
    
    public static final int STATE_CHOOSE_AIRPORT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AIRPORT + 5);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------

    public static final int CMD_BACKSPACE = CMD_USER_BASE + USER_BASE_AIRPORT + 1;
    
    public static final int CMD_SUBMIT = CMD_USER_BASE + USER_BASE_AIRPORT + 2;
    
    public static final int CMD_SELECT_AIRPORT = CMD_USER_BASE + USER_BASE_AIRPORT + 3;
    
    public static final int CMD_SEARCH_AIRPORT = CMD_USER_BASE + USER_BASE_AIRPORT + 4;
    

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_AIRPORT + 1;
    
    public static final int ACTION_VALIDATE_AIRPORT = ACTION_USER_BASE + USER_BASE_AIRPORT + 2;
    
    public static final int ACTION_HIDE_DROPDOWN = ACTION_USER_BASE + USER_BASE_AIRPORT + 3;
    
    public static final int ACTION_CANCEL_VALIDATING_AIRPORT = ACTION_USER_BASE + USER_BASE_AIRPORT + 4;
    
    public static final int ACTION_SEARCH_AIRPORT = ACTION_USER_BASE + USER_BASE_AIRPORT + 5;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_AIRPORT + 1;
    
    public static final int EVENT_MODEL_RETURN_AIRPORT = EVENT_MODEL_USER_BASE + USER_BASE_AIRPORT + 2;
    
    public static final int EVENT_MODEL_CHOOSE_AIRPORT = EVENT_MODEL_USER_BASE + USER_BASE_AIRPORT + 3;
    
    public static final int EVENT_MODEL_HIDE_DROPDOWN = EVENT_MODEL_USER_BASE + USER_BASE_AIRPORT + 4;
    

    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------
    public static final int ID_AIRPORT_TEXTFIELD = USER_BASE_AIRPORT + 1;
    
    public static final int ID_AIRPORT_LIST = USER_BASE_AIRPORT + 2;
    
    public static final int ID_AIRPORT_BUTTON_CONTAINER = USER_BASE_AIRPORT + 3;
    
    public static final int ID_SEARCH_BUTTON = USER_BASE_AIRPORT + 4;
    
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    // A String variable to store alert message
    public static Integer KEY_S_AIRPORT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AIRPORT + 1);
    
    //Vector to save the hint of airport
    public static Integer KEY_V_DROPDOWN_HINT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AIRPORT + 2);
    
    //Airports searched by native data
    public static Integer KEY_V_MATCHED_STOPS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AIRPORT + 6);

    public static Integer KEY_O_TNLOCATION = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AIRPORT + 7);

}

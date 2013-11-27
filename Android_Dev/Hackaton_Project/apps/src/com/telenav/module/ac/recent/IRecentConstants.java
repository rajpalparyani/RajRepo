/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRecentConstants.java
 *
 */
package com.telenav.module.ac.recent;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2010-10-27
 */
interface IRecentConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
	public static final int STATE_RECENT_BASE = STATE_USER_BASE + USER_BASE_RECENT;
	
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_RECENT + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_RECENT + 2;
    
    public static final int STATE_ADDRESS_SELECTED = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 3);
    
    public static final int STATE_SYNC_FAILED = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 4);
    
    public static final int STATE_DELETE_ALL_CONFIRM = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 5);
    
    public static final int STATE_GO_TO_MAP = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 6);
    
    public static final int STATE_GO_TO_NAV = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 7);
    
    public static final int STATE_SAVE_RECENT = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 8);
    
    public static final int STATE_DELETE_RECENT = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 9);
    
    public static final int STATE_RATE_RECENT = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 10);
    
    public static final int STATE_DELETE_CHECK = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_RECENT + 11);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_SEARCH_ADDRESS = CMD_USER_BASE + USER_BASE_RECENT + 1;
    
    public static final int CMD_SELECT_ADDRESS = CMD_USER_BASE + USER_BASE_RECENT + 2;
    
    public static final int CMD_SYNC_RECENT = CMD_USER_BASE + USER_BASE_RECENT + 3;
    
    public static final int CMD_TIME_OUT = CMD_USER_BASE + USER_BASE_RECENT + 4;
    
    public static final int CMD_DELETE_RECENT = CMD_USER_BASE + USER_BASE_RECENT + 5;
    
    public static final int CMD_DRIVETO_ADDRESS = CMD_USER_BASE + USER_BASE_RECENT + 6;
    
    public static final int CMD_MAP_ADDRESS = CMD_USER_BASE + USER_BASE_RECENT + 7;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_RECENT + 1;
    
    public static final int ACTION_SEARCH_RECENT = ACTION_USER_BASE + USER_BASE_RECENT + 2;
    
    public static final int ACTION_SYN_RECENT = ACTION_USER_BASE + USER_BASE_RECENT + 3;
    
    public static final int ACTION_DELETE_ALL_RECENT = ACTION_USER_BASE + USER_BASE_RECENT + 4;
    
    public static final int ACTION_DELETE_RECENT = ACTION_USER_BASE + USER_BASE_RECENT + 5;
    
    public static final int ACTION_CANCEL_VALIDATING = ACTION_USER_BASE + USER_BASE_RECENT + 6;
    
    public static final int ACTION_CALL = ACTION_USER_BASE + USER_BASE_RECENT + 7;
    
    public static final int ACTION_BACKGROUND_SYNC = ACTION_USER_BASE + USER_BASE_RECENT + 8;
    
    public static final int ACTION_CHECK_MIGRATION = ACTION_USER_BASE + USER_BASE_RECENT + 9;
    
    public static final int ACTION_DELETE_CHECK = ACTION_USER_BASE + USER_BASE_RECENT + 10;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_RECENT + 1;
    
    public static final int EVENT_MODEL_SYNC_FINISHED = EVENT_MODEL_USER_BASE + USER_BASE_RECENT + 2;
    
    public static final int EVENT_MODEL_DELETE_SINGLE_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_RECENT + 3;
    
    public static final int EVENT_MODEL_DELETE_ALL_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_RECENT + 4;
    
    
    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------
    public static final int ID_RECENT_LIST = USER_BASE_RECENT + 1;
    
    public static final int ID_EMPTY_LABEL = USER_BASE_RECENT + 2;
    
    public static final int ID_DELETE_ALL_BUTTON = USER_BASE_RECENT + 3;
    
    public static final int ID_LEFT_BOTTOM_CONTAINER = USER_BASE_RECENT + 4;
    
    public static final int ID_BOTTOM_CONTAINER = USER_BASE_RECENT + 5;
    
    public static final int ID_LIST_CONTAINER = USER_BASE_RECENT + 6;
    
    public static final int ID_RIGHT_BOTTOM_CONTAINER = USER_BASE_RECENT + 7;
    
    public static final int ID_DELETE_RECENT_ITEM = USER_BASE_RECENT + 8;
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    
    // A string object to save the searching text
    public static final Integer KEY_S_SEARCH_TEXT = PrimitiveTypeCache.valueOf(STATE_RECENT_BASE + 1);
    
    // Vector to save the result of searching
    public static final Integer KEY_V_CURRENT_RECENT_ADDRESSES = PrimitiveTypeCache.valueOf(STATE_RECENT_BASE + 2);
    
    // Int value to save the index of the selected address in list
    public static final Integer KEY_I_INDEX = PrimitiveTypeCache.valueOf(STATE_RECENT_BASE + 3);
    
    //The phone number of POI
    public static final Integer KEY_S_POI_PHONENUMBER = PrimitiveTypeCache.valueOf(STATE_RECENT_BASE+ 4);
    
    public static final Integer KEY_B_IS_NEED_RECREATE = PrimitiveTypeCache.valueOf(STATE_RECENT_BASE + 5);
    
    public static Integer KEY_B_KEEP_MINI_BAR = PrimitiveTypeCache.valueOf(STATE_RECENT_BASE + 6);
}

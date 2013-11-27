/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRecentConstants.java
 *
 */
package com.telenav.module.ac.home;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-3
 */
public interface IHomeConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
	public static final int STATE_SET_HOME_BASE = STATE_USER_BASE + USER_BASE_SET_HOME;
	
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_SET_HOME + 2;

    public static final int STATE_VALIDATE_INPUT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 3);

    public static final int STATE_HOME_SET = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 4);
    
    public static final int STATE_RETURN_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 5);
    
    public static final int STATE_VALIDATE_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 6);
    
    public static final int STATE_CHECK_ADDRESS_TYPE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 7);
    
    public static final int STATE_SAVE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 8);
    
    public static final int STATE_ADDRESS_SELECTED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 9);
    
    public static final int STATE_RETURN_TO_OTHERS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 10);
        
    public static final int STATE_GOTO_ONEBOX = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 11);
    
    public static final int STATE_CHECK_CITY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_SET_HOME + 12);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_SAVE = CMD_USER_BASE + USER_BASE_SET_HOME + 1;
    
    public static final int CMD_BACKSPACE = CMD_USER_BASE + USER_BASE_SET_HOME + 2;
    
    public static final int CMD_SELECT_ADDRESS = CMD_USER_BASE + USER_BASE_SET_HOME + 3;
    
    public static final int CMD_SELECT_CITY = CMD_USER_BASE + USER_BASE_SET_HOME + 4;

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_SET_HOME + 1;

    public static final int ACTION_VALIDATE_INPUT = ACTION_USER_BASE + USER_BASE_SET_HOME + 2;

    public static final int ACTION_SAVE_HOME = ACTION_USER_BASE + USER_BASE_SET_HOME + 3;
    
    public static final int ACTION_CHECK_ADDRESS_TYPE = ACTION_USER_BASE + USER_BASE_SET_HOME + 4;
    
    public static final int ACTION_SAVE_WORK = ACTION_USER_BASE + USER_BASE_SET_HOME + 5;
    
    public static final int ACTION_SAVE_HOME_FROM_ONEBOX = ACTION_USER_BASE + USER_BASE_SET_HOME + 6;
    
    public static final int ACTION_SAVE_WORK_FROM_ONEBOX = ACTION_USER_BASE + USER_BASE_SET_HOME + 7;
    
    public static final int ACTION_CHECK_CITY = ACTION_USER_BASE + USER_BASE_SET_HOME + 8;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME + 1;

    public static final int EVENT_MODEL_SAVE_HOME = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME + 2;

    public static final int EVENT_MODEL_SAVE_WORK = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME + 3;
    
    public static final int EVENT_MODEL_VALIDATE_HOME = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME + 4;
    
    public static final int EVENT_RETURN_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME + 5;
    
    public static final int EVENT_MODEL_VALIDATE_LIST_HOME = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME + 6;
    
    public static final int EVENT_MODEL_GO_TO_VALIDATE_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME +  7;
    
    public static final int EVENT_MODEL_RETURN_CITY_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_SET_HOME +  8;
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------

    // A string to save the district inputed by user
    public static Integer KEY_S_CITY = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 1);
    
    public static Integer KEY_V_ALTERNATIVE_ADDRESSES = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 2);
    
    public static Integer KEY_I_INDEX = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 3);
    
    public static Integer KEY_O_HOME = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 4);
    
    public static Integer KEY_V_SEARCH_LIST = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 5);
    
    public static Integer KEY_V_NEAR_CITIES = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 6);
    
    public static Integer KEY_V_FILTER_SEARCH_LIST = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 7);
    
    public static Integer KEY_B_FROM_LIST = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 8);
    
    public static Integer KEY_S_TITLE = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 9);
    
    public static Integer KEY_S_STREET_INIT_TEXT = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 10);
    
    public static Integer KEY_S_CITY_INIT_TEXT = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 11);
    
    public static Integer KEY_B_NEED_PREFERENCE_MENU = PrimitiveTypeCache.valueOf(STATE_SET_HOME_BASE + 12);
    
    //------------------------------------------------------------
    // component id
    //------------------------------------------------------------
    public final static int ID_ADDRESS_INPUT_FIELD = 10001;
    public final static int ID_CITY_INPUT_FIELD = 10002;
    public final static int ID_ADDRESS_LIST = 10003;
    public final static int ID_DISTRICT_LIST = 10004;
    public final static int ID_SUBMIT_BUTTON = 10005;
    public final static int ID_CONTENT_CONTAINER = 10006;
    public final static int ID_SEARCH_LIST = 10007;
}

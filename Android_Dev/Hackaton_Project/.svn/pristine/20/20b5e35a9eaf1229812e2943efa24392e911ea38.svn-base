/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IAddPlaceConstants.java
 *
 */
package com.telenav.module.ac.place.addplace;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper(pwang@telenav.cn)
 *@date 2013-2-21
 */
interface IAddPlaceConstants extends ICommonConstants
{

    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_ADD_PLACE_BASE = STATE_USER_BASE + USER_BASE_ADD_PLACE;
    
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_ADD_PLACE_BASE + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + STATE_ADD_PLACE_BASE + 2;
    
    public static final int STATE_NO_GPS_WARNING = MASK_STATE_TRANSIENT | (STATE_ADD_PLACE_BASE + 4);
    
    public static final int STATE_ITEM_CHECK_SELECTED = MASK_STATE_TRANSIENT | (STATE_ADD_PLACE_BASE + 5);
    
    public static final int STATE_CUSTOM_PLACE = STATE_USER_BASE + STATE_ADD_PLACE_BASE + 6;
    
    public static final int STATE_CHECK_PLACE_LABEL = MASK_STATE_TRANSIENT | (STATE_USER_BASE + STATE_ADD_PLACE_BASE + 7);
    
    public static final int STATE_SAVE_PLACE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + STATE_ADD_PLACE_BASE + 8);
    
    public static final int STATE_PLACE_ADDED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + STATE_ADD_PLACE_BASE + 9);
    
    public static final int STATE_GOTO_NATIVE_SHARE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + STATE_ADD_PLACE_BASE + 10);
    
    public static final int STATE_ADD_CATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + STATE_ADD_PLACE_BASE + 11);

    
//    public static final int STATE_ERROR_ADDRESS_NOT_FOUND = MASK_STATE_TRANSIENT | (STATE_USER_BASE + STATE_ADD_PLACE_BASE + 3);

//    public static final int STATE_CHOOSE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ADDRESS_VALIDATOR + 4);
    
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_ADD_PLACE_BASE     = CMD_COMMON_BASE + USER_BASE_ADD_PLACE;

    public static final int CMD_GETTING_MORE          = CMD_ADD_PLACE_BASE + 1;
    
    public static final int CMD_SELECT_ITEM          = CMD_ADD_PLACE_BASE + 2;
    
    public static final int CMD_CUSTOM_PLACE_DONE          = CMD_ADD_PLACE_BASE + 3;
    
    public static final int CMD_ADD_CATEGORY          = CMD_ADD_PLACE_BASE + 4;

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_ADD_PLACE_BASE = ACTION_USER_BASE + USER_BASE_ADD_PLACE;
    public static final int ACTION_INIT = ACTION_ADD_PLACE_BASE + 1;
    public static final int ACTION_RETRIEVE_PLACES_LIST = ACTION_ADD_PLACE_BASE + 2;
    public static final int ACTION_GETING_MORE = ACTION_ADD_PLACE_BASE + 3;
    public static final int ACTION_CHECK_SELECT = ACTION_ADD_PLACE_BASE + 4;
    public static final int ACTION_CHECK_PLACE_LABEL = ACTION_ADD_PLACE_BASE + 5;
    public static final int ACTION_SAVE_PLACE = ACTION_ADD_PLACE_BASE + 6;
    public static final int ACTION_CATEGORY_UPDATE = ACTION_ADD_PLACE_BASE + 7;

    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_ADD_PLACE_BASE = EVENT_MODEL_USER_BASE + USER_BASE_ADD_PLACE;

    public static final int EVENT_MODEL_START_MAIN = EVENT_MODEL_ADD_PLACE_BASE + 1;

    public static final int EVENT_MODEL_REFRESH_PLACES_LIST = EVENT_MODEL_ADD_PLACE_BASE + 3;
    
    public static final int EVENT_MODEL_NO_GPS_WARNING = EVENT_MODEL_ADD_PLACE_BASE + 4;
    
    public static final int EVENT_MODEL_PLACE_ADDED = EVENT_MODEL_ADD_PLACE_BASE + 5;
    
    public static final int EVENT_MODEL_CUSTOM_PLACE = EVENT_MODEL_ADD_PLACE_BASE + 6;
    
    public static final int EVENT_MODEL_SAVE_PLACE = EVENT_MODEL_ADD_PLACE_BASE + 7;
    
    public static final int EVENT_MODEL_NO_NEED_SAVE_AGAIN = EVENT_MODEL_ADD_PLACE_BASE + 8;
    
    public static final int EVENT_MODEL_SHARE_PLACE = EVENT_MODEL_ADD_PLACE_BASE + 9;
    
    

    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static final Integer KEY_S_PLACE_LABEL = PrimitiveTypeCache.valueOf(STATE_ADD_PLACE_BASE + 1);
    public static final Integer KEY_I_SELECTED_CATEGORY_INDEX = PrimitiveTypeCache.valueOf(STATE_ADD_PLACE_BASE + 2);
    public static final Integer KEY_S_OLD_CATEGORY = PrimitiveTypeCache.valueOf(STATE_ADD_PLACE_BASE + 3);
    public static final Integer KEY_V_ALL_CATEGORIES = PrimitiveTypeCache.valueOf(STATE_ADD_PLACE_BASE + 5);
    public static final Integer KEY_B_CATEGORY_CHANGED = PrimitiveTypeCache.valueOf(STATE_ADD_PLACE_BASE + 6);
    public static final Integer KEY_B_LABEL_CHANGED = PrimitiveTypeCache.valueOf(STATE_ADD_PLACE_BASE + 7);

}

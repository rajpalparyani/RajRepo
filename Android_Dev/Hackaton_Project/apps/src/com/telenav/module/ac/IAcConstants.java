/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAcConstants.java
 *
 */
package com.telenav.module.ac;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-13
 */
interface IAcConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------

    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_AC + 2;

    public static final int STATE_CHECK_HOME_EXIST = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 3);

    public static final int STATE_SET_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 4);

    public static final int STATE_RETURN_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 5);
    
    public static final int STATE_GOTO_NAV = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 6);
    
    public static final int STATE_GOTO_RECENT_MAIN = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 7);

    public static final int STATE_SEARCH_MAIN = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 8);

    public static final int STATE_GOTO_POI = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 9);

    public static final int STATE_GOTO_FAVORITE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 10);

    public static final int STATE_AIRPORT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 11);

    public static final int STATE_GOTO_UPSELL = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 12);

    public static final int STATE_GOTO_CONTACTS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 13);
    
    public static final int STATE_GOTO_DSR = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 15);

    public static final int STATE_EDIT_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 16);
    
    public static final int STATE_RETURN_CURRENT_LOCATION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 17);
    
    public static final int STATE_CHECK_WORK_EXIST = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 18);
    
    public static final int STATE_SET_WORK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 19);
    
    public static final int STATE_RETURN_WORK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 20);
    
    public static final int STATE_EDIT_WORK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 21);
    
    public static final int STATE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 22);
    
    public static final int STATE_FIND_LOCATION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 24);
    
    public static final int STATE_GET_CURRENT_LOCATION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 25);
    
    public static final int STATE_DSR_GET_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 26);
    
    public static final int STATE_MAP_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 27);
    
    public static final int STATE_MAP_WORK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 28);
    
    public static final int STATE_DRIVE_HOME = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 29);
    
    public static final int STATE_DRIVE_WORK = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 30);
    
    public static final int STATE_RGC = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 31);
    
    public static final int STATE_INTERSECTION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 32);
    
    public static final int STATE_RETURN_ADDRESS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 33);
    
    public static final int STATE_CHANGE_REGION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 34);
    
    public static final int STATE_CHECK_REGION_CHANGE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 35);
    
    public static final int STATE_REGION_SWITCH_FAILED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 36);
    
    public static final int STATE_CHECK_RETURNED_ADDRESS_REGION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_AC + 37);
       
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_HOME = CMD_USER_BASE + USER_BASE_AC + 1;

//    public static final int CMD_CONTACTS = CMD_USER_BASE + USER_BASE_AC + 2;

    public static final int CMD_FAVORITES = CMD_USER_BASE + USER_BASE_AC + 3;

    public static final int CMD_RECENT = CMD_USER_BASE + USER_BASE_AC + 4;

    public static final int CMD_PLACES = CMD_USER_BASE + USER_BASE_AC + 5;

    public static final int CMD_AIRPORTS = CMD_USER_BASE + USER_BASE_AC + 6;
    
    public static final int CMD_EDIT_HOME = CMD_USER_BASE + USER_BASE_AC + 7;
    
    public static final int CMD_CURRENT_LOACTION = CMD_USER_BASE + USER_BASE_AC + 9;
    
    public static final int CMD_WORK = CMD_USER_BASE + USER_BASE_AC + 10;
    
    public static final int CMD_ADDRESS = CMD_USER_BASE + USER_BASE_AC + 11;
    
    public static final int CMD_EDIT_WORK = CMD_USER_BASE + USER_BASE_AC + 12;
    
    public static final int CMD_MAP_WORK = CMD_USER_BASE + USER_BASE_AC + 13;
    
    public static final int CMD_DRIVETO_HOME = CMD_USER_BASE + USER_BASE_AC + 14;
    
    public static final int CMD_MAP_HOME = CMD_USER_BASE + USER_BASE_AC + 15;
    
    public static final int CMD_DRIVETO_WORK = CMD_USER_BASE + USER_BASE_AC + 17;
    
    public static final int CMD_INTERSECTION = CMD_USER_BASE + USER_BASE_AC + 18;
    
    public static final int CMD_CHANGE_REGION = CMD_USER_BASE + USER_BASE_AC + 19;
    
    public static final int CMD_CHANGE_REGION_SAVE = CMD_USER_BASE + USER_BASE_AC + 20;

    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_AC + 1;

    public static final int ACTION_CHECK_HOME_EXIST = ACTION_USER_BASE + USER_BASE_AC + 2;

    public static final int ACTION_CHECK_CURRENT_LOCATION = ACTION_USER_BASE + USER_BASE_AC + 3;
    
    public static final int ACTION_CHECK_WORK_EXIST = ACTION_USER_BASE + USER_BASE_AC + 4;

    public static final int ACTION_GET_CURRENT_LOCATION = ACTION_USER_BASE + USER_BASE_AC + 6;
    
    public static final int ACTION_RGC = ACTION_USER_BASE + USER_BASE_AC + 7;
    
    public static final int ACTION_BACK_CHECK = ACTION_USER_BASE + USER_BASE_AC + 8;
    
    public static final int ACTION_CHECK_REGION_CHANGE = ACTION_USER_BASE + USER_BASE_AC + 9;
    
    public static final int ACTION_CHECK_RETURNED_ADDRESS_REGION = ACTION_USER_BASE + USER_BASE_AC + 10;
    
    public static final int ACTION_REGION_SWITCH_CANCELLED = ACTION_USER_BASE + USER_BASE_AC + 11;
    
    public static final int ACTION_REGION_DETECT_CANCELLED = ACTION_USER_BASE + USER_BASE_AC + 12;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_AC + 1;

    public static final int EVENT_MODEL_SET_HOME = EVENT_MODEL_USER_BASE + USER_BASE_AC + 2;

    public static final int EVENT_MODEL_GOTO_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_AC + 3;

    public static final int EVENT_MODEL_RESET_HOME = EVENT_MODEL_USER_BASE + USER_BASE_AC + 4;

    public static final int EVENT_MODEL_RETURN_HOME = EVENT_MODEL_USER_BASE + USER_BASE_AC + 5;

    public static final int EVENT_MODEL_SET_WORK = EVENT_MODEL_USER_BASE + USER_BASE_AC + 7;
    
    public static final int EVENT_MODEL_RETURN_WORK = EVENT_MODEL_USER_BASE + USER_BASE_AC + 8;
    
    public static final int EVENT_MODEL_LOCATION_GOT = EVENT_MODEL_USER_BASE + USER_BASE_AC + 9;
    
    public static final int EVENT_MODEL_FIND_CURRENT_LOCATION = EVENT_MODEL_USER_BASE + USER_BASE_AC + 10;
    
    public static final int EVENT_MODEL_GETTING_CURRENT_LOCATION = EVENT_MODEL_USER_BASE + USER_BASE_AC + 11;
    
    public static final int EVENT_MODEL_RGC = EVENT_MODEL_USER_BASE + USER_BASE_AC + 12;
    
    public static final int EVENT_MODEL_RGC_FINISHED = EVENT_MODEL_USER_BASE + USER_BASE_AC + 13;
    
    public static final int EVENT_MODEL_REFRESH = EVENT_MODEL_USER_BASE + USER_BASE_AC + 14;
    
    public static final int EVENT_MODEL_REGION_SWITCH_FAILED = EVENT_MODEL_USER_BASE + USER_BASE_AC + 15;
    
    public static final int EVENT_MODEL_RETURN_ADDRESS = EVENT_MODEL_USER_BASE + USER_BASE_AC + 16;

    public static final int EVENT_MODEL_BACK_TO_DASHBOARD = EVENT_MODEL_USER_BASE + USER_BASE_AC + 17;
    
    // ------------------------------------------------------------
    // The controller event id definition
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------
    
    //replaces by ID_QUICK_SEARCH_BAR
    
    public static final int ID_RECENT_BUTTON = USER_BASE_AC + 1;
    
    public static final int ID_HOME_BUTTON = USER_BASE_AC + 2;
    
    public static final int ID_SEARCH_BAR_CONTAINER = USER_BASE_AC + 3;
    
    public static final int ID_WORK_BUTTON = USER_BASE_AC + 4;
    
    public static final int ID_CURRENT_LOCATION = USER_BASE_AC + 5;
    
    public static final int ID_FAVORITE_BUTTON = USER_BASE_AC + 6;
    
    public static final int ID_PLACE_BUTTON = USER_BASE_AC + 7;
    
    public static final int ID_ADDRESS_BUTTON = USER_BASE_AC + 8;
    
    public static final int ID_CONTACT_BUTTON = USER_BASE_AC + 9;
    
    public static final int ID_AIRPORT_BUTTON = USER_BASE_AC + 10;
    
    public static final int ID_RESUME_BUTTON = USER_BASE_AC + 11;
    
    public static final int ID_TEXTFIELD_FILTER = USER_BASE_AC + 12;
    
    public static final int ID_MAP_TILE = USER_BASE_AC + 13;
    
    public static final int ID_DRIVE_TO_CONTAINER = USER_BASE_AC + 14;
    
    public static final int ID_ADJUGGLER_WEBCOMPONENT = USER_BASE_AC + 15;
    
    public static final int ID_INTERSECTION_BUTTON = USER_BASE_AC + 16;
    
    public static final int ID_CHANGE_REGION_DROP_DOWN = USER_BASE_AC + 17;
    
    public static final int ID_CHANGE_REGION_CONTAINER = USER_BASE_AC + 18;
    
    public static final int ID_CHANGE_REGION_RADIO_BOX = USER_BASE_AC + 19;
    
    public static final int ID_AC_BUTTON_CONTAINER = USER_BASE_AC + 20;
		
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------

    //Object to save the current location
    public static Integer KEY_O_CURRENT_LOCATION = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 1);
    
    public static Integer KEY_I_ORIENTATION = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 2);
    
    public static Integer KEY_B_SHOW_LAST_TRIP = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 3);
    
    public static Integer KEY_I_TOP_CONTAINER_HEIGHT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 4);
    
    public static Integer KEY_I_CURRENT_LOCATION_LAT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 5);

    public static Integer KEY_I_CURRENT_LOCATION_LON = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 6);
    
    public static Integer KEY_S_HOME_STATIC_ETA = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 7);
    
    public static Integer KEY_S_HOME_DYNAMIC_ETA = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 8);
    
    public static Integer KEY_S_OFFICE_STATIC_ETA = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 9);
    
    public static Integer KEY_S_OFFICE_DYNAMIC_ETA = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 10);
    
    public static Integer KEY_O_MAP_TILE_ID_VECTOR = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 11);
    
    public static Integer KEY_I_MAP_ZOOM_LEVEL = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 12);
    
    public static Integer KEY_I_MAP_PIXEL_SIZE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 13);
    
    public static Integer KEY_O_STATIC_MAP_TILE_IMAGE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 14);
    
    public static Integer KEY_O_STATIC_TRAFFIC_TILE_IMAGE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 15);
    
    public static Integer KEY_I_MAP_CURRENT_X = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 16);
    
    public static Integer KEY_I_MAP_CURRENT_Y = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 17);
    
    public static Integer KEY_B_IS_CLOSE_TO_HOME = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 18);
    
    public static Integer KEY_B_IS_CLOSE_TO_OFFICE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 19);

    public static Integer KEY_B_IS_NEW_DRIVE_TO_PAGE_NEED_RELOAD = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 20);
    
    public static Integer KEY_O_ADJUGGLER_COMPONENT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 21);
    
    public static Integer KEY_B_IS_NEED_PURCHASE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 22);
    
    public static Integer KEY_B_IS_GETTING_LOCATION_FOR_MINI_MAP = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 23);
    
    public static Integer KEY_O_MAP_TILE_COMPONENT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 25);

    public static Integer KEY_O_BANNER_CONTAINER_PORTRAIT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 26);

    public static Integer KEY_O_BANNER_CONTAINER_LANDSCAPE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 27);

    public static Integer KEY_O_HOME_COMPONENT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 28);

    public static Integer KEY_O_WORK_COMPONENT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 29);

    public static Integer KEY_B_WEB_COMPONENT_DESTROY = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 30);
    
    public static Integer KEY_O_MAP_DRIVETO = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 31);
    
    public static Integer KEY_O_TRAFFIC_DRIVETO = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 32);
    
    public static Integer KEY_I_SWITCH_REGION_TYPE  = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 35);
    
    public static Integer KEY_B_IS_REGION_SWITCHED_DIRECTLY  = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 36);
    
    public static Integer KEY_B_IS_REGION_OPERATION_CANCELLED  = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_AC + 37);
    
    
    
    //SWITCH REGION TYPE CONSTANTS
    public static final int SWITCH_REGION_TYPE_SELECT_REGION_FROM_LIST = 1;
    
    public static final int SWITCH_REGION_TYPE_SELECT_ADDRESS = 2;
}

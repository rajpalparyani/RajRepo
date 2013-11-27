/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IEntryConstants.java
 *
 */
package com.telenav.module.map;

import com.telenav.module.poi.PoiDataRequester;
import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author JY Xu
 *@date Aug 21, 2010
 */

public interface IMapConstants extends ICommonConstants
{
    public static final int STATE_MAP_BASE = STATE_USER_BASE + USER_BASE_STATIC_MAP;

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_STATIC_MAP + 1;

    public static final int STATE_GOTO_DSR = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 3) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_SEARCH = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 4) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_NAV = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 5) | MASK_STATE_TRANSIENT;

    public static final int STATE_MAP_SUMMARY = STATE_USER_BASE + USER_BASE_STATIC_MAP + 6;

    public static final int STATE_GOTO_ROUTE_SUMMARY = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 7) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_TRAFFIC_SUMMARY = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 8) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_POI_DETAIL = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 9) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_SHARE = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 10) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_CALL = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 11) | MASK_STATE_TRANSIENT;

    public static final int STATE_GETTING_DECIMATE_ROUTE = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 12) | MASK_STATE_TRANSIENT;

    public static final int STATE_BACK_TO_SUMMARY_CONTROLLER = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 13) | MASK_STATE_TRANSIENT;

    public static final int STATE_INIT_MAP_SUMMARY = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 14) | MASK_STATE_TRANSIENT;

    public static final int STATE_CHANGE_LOCATION = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 15) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_POI_LIST = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 16) | MASK_STATE_TRANSIENT;

    public static final int STATE_RELEASE_ALL_GO_TO_MAP = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 17) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_UPSELL = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 21) | MASK_STATE_TRANSIENT;

    public static final int STATE_GETTING_MORE_POIS = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 23) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_ONE_SEARCH_BOX  = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 25) | MASK_STATE_TRANSIENT;

    public static final int STATE_GOTO_SAVE_FAVORITE = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 27) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_LINK_TO_NAVIGATION = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 28) | MASK_STATE_TRANSIENT;
     
    public static final int STATE_LINK_TO_DIRECTIONS = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 29) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_SHOW_TRAFFIC_DETAIL = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 30) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_CLEAR = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 32) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_BACK_TO_POI_DETAIL = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 33) | MASK_STATE_TRANSIENT;
    public static final int STATE_GETTING_RGC  = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 34) | MASK_STATE_TRANSIENT;
    
    public static final int STATE_GETTING_MORE_ROUTE  = (STATE_USER_BASE + USER_BASE_STATIC_MAP + 35) | MASK_STATE_TRANSIENT;
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------

    public static final int CMD_ROUTE_SUMMARY = CMD_USER_BASE + USER_BASE_STATIC_MAP + 1;

    public static final int CMD_TRAFFIC_SUMMARY = CMD_USER_BASE + USER_BASE_STATIC_MAP + 2;

    public static final int CMD_MAP_SUMMARY = CMD_USER_BASE + USER_BASE_STATIC_MAP + 3;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int CMD_ZOOM_IN = CMD_USER_BASE + USER_BASE_STATIC_MAP + 102;

    public static final int CMD_ZOOM_OUT = CMD_USER_BASE + USER_BASE_STATIC_MAP + 103;

    public static final int CMD_CURRENT_LOCATION = CMD_USER_BASE + USER_BASE_STATIC_MAP + 104;

    public static final int CMD_POI_LIST = CMD_USER_BASE + USER_BASE_STATIC_MAP + 107;
    
    public static final int CMD_NAVIGATION = CMD_USER_BASE + USER_BASE_STATIC_MAP + 108;
    
    public static final int CMD_DIRECTIONS = CMD_USER_BASE + USER_BASE_STATIC_MAP + 109;
    
    
    public static final int CMD_MAP_POI_GOTO_POI_DETAIL = CMD_USER_BASE + USER_BASE_STATIC_MAP + 113;
    
    public static final int CMD_MAP_POI_GOTO_SHARE = CMD_USER_BASE + USER_BASE_STATIC_MAP + 114;
    
    public static final int CMD_MAP_POI_GOTO_CALL = CMD_USER_BASE + USER_BASE_STATIC_MAP + 115;
    
    public static final int CMD_MAP_POI_GOTO_NAV = CMD_USER_BASE + USER_BASE_STATIC_MAP + 116;
    
    public static final int CMD_MAP_POI_NEXT = CMD_USER_BASE + USER_BASE_STATIC_MAP + 117;
    
    public static final int CMD_MAP_POI_PREV = CMD_USER_BASE + USER_BASE_STATIC_MAP + 118;
    
    public static final int CMD_MAP_POI_PAGE_NEXT = CMD_USER_BASE + USER_BASE_STATIC_MAP + 119;
    
    public static final int CMD_MAP_POI_PAGE_PREV = CMD_USER_BASE + USER_BASE_STATIC_MAP + 120;
    
    public static final int CMD_MAP_POI_PAGE_NEXT_NETWORK = CMD_USER_BASE + USER_BASE_STATIC_MAP + 122;
    
    public static final int CMD_SHOW_TRAFFIC_DETAIL = CMD_USER_BASE + USER_BASE_STATIC_MAP + 123;

    public static final int CMD_TRAFFIC_DETAIL_OK = CMD_USER_BASE + USER_BASE_STATIC_MAP + 124;

    public static final int CMD_MAP_ADDRESS_GOTO_ONE_SEARCH_BOX = CMD_USER_BASE + USER_BASE_STATIC_MAP + 125;

    public static final int CMD_MAP_ADDRESS_GOTO_SAVE_FAVORITE = CMD_USER_BASE + USER_BASE_STATIC_MAP + 127;

    public static final int CMD_MAP_ADDRESS_GOTO_NAV = CMD_USER_BASE + USER_BASE_STATIC_MAP + 128;
    
    public static final int CMD_MAP_ADDRESS_GOTO_SHARE = CMD_USER_BASE + USER_BASE_STATIC_MAP + 130;
    
    public static final int CMD_CLEAR  = CMD_USER_BASE + USER_BASE_STATIC_MAP +  131;
    
    public static final int CMD_GPS_NOT_AVAILABLE_NOTIFICATION_CLOSE = CMD_USER_BASE + USER_BASE_STATIC_MAP + 132;
    
    public static final int CMD_MAP_RGC = CMD_USER_BASE + USER_BASE_STATIC_MAP + 133;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //NOTE: please don't use 200 - 220 
    
    /*************************** CMD WITH SELECTED INDEX FROM POI ANNOTATION *******************************/
    public static final int CMD_MAP_POI_CHANGE_TO_UNFOCUSED = CMD_USER_BASE + USER_BASE_STATIC_MAP + 200;
    
    public static final int CMD_MAP_POI_CHANGE_TO_FOCUSED = CMD_USER_BASE + USER_BASE_STATIC_MAP + 201;
    
    public static final int CMD_MAP_POI_CHANGE_TO_FOCUSED_START = CMD_MAP_POI_CHANGE_TO_FOCUSED + 0;
    
    public static final int CMD_MAP_POI_CHANGE_TO_FOCUSED_END = CMD_MAP_POI_CHANGE_TO_FOCUSED_START + 2*PoiDataRequester.DEFAULT_PAGE_SIZE;
    
    /*************************** CMD WITH LAYER SETTING FROM POI MAP VIEW LIST ************************************************************/
    
    //NOTE: please don't use 230 - 290 

    public static final int CMD_SHOW_MAP_LAYER = CMD_USER_BASE + USER_BASE_STATIC_MAP + 230;
    
    public static final int CMD_SHOW_MAP_LAYER_START = CMD_SHOW_MAP_LAYER;
    
    public static final int CMD_SHOW_MAP_TRAFFIC_LAYER_START = CMD_SHOW_MAP_LAYER + 1;
    
    public static final int CMD_SHOW_MAP_TRAFFIC_LAYER_END = CMD_SHOW_MAP_LAYER + 20;
    
    public static final int CMD_SHOW_MAP_CAMERA_LAYER_START = CMD_SHOW_MAP_LAYER + 21 ;
    
    public static final int CMD_SHOW_MAP_CAMERA_LAYER_END = CMD_SHOW_MAP_LAYER + 40;

    public static final int CMD_SHOW_MAP_SATELLITE_LAYER_START = CMD_SHOW_MAP_LAYER + 41 ;
    
    public static final int CMD_SHOW_MAP_SATELLITE_LAYER_END = CMD_SHOW_MAP_LAYER + 60;
    
    public static final int CMD_SHOW_MAP_LAYER_END = CMD_SHOW_MAP_LAYER + 61;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //NOTE: please don't use 300-350
    
    public static final int CMD_MAP_ADDRESS_CHANGE_TO_UNFOCUSED = CMD_USER_BASE + USER_BASE_STATIC_MAP + 300;

    public static final int CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED = CMD_USER_BASE + USER_BASE_STATIC_MAP + 301;
    
    public static final int CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED_START = CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED + 0;
    
    public static final int CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED_END = CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED_START + 30;

    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_MAP_INIT = ACTION_USER_BASE + USER_BASE_STATIC_MAP + 0;

    public static final int ACTION_BACK_CHECK = ACTION_USER_BASE + USER_BASE_STATIC_MAP +1;

    public static final int ACTION_SHOW_MORE = ACTION_USER_BASE + USER_BASE_STATIC_MAP +2;

    public static final int ACTION_INIT_MAP_SUMMARY = ACTION_USER_BASE + USER_BASE_STATIC_MAP +3;

    public static final int ACTION_CALL = ACTION_USER_BASE + USER_BASE_STATIC_MAP +4;

    public static final int ACTION_ADDRESS_SELECTED = ACTION_USER_BASE + USER_BASE_STATIC_MAP +5;
    
    public static final int ACTION_CHECK_LAYER = ACTION_USER_BASE + USER_BASE_STATIC_MAP +6;
    
    public static final int ACTION_GETTING_MORE_POIS  = ACTION_USER_BASE + USER_BASE_STATIC_MAP +8;
    
    public static final int ACTION_MAP_ADDRESS = ACTION_USER_BASE + USER_BASE_STATIC_MAP +9;
    
    public static final int ACTION_CLEAR = ACTION_USER_BASE + USER_BASE_STATIC_MAP +10;
    
    public static final int ACTION_GET_RGC = ACTION_USER_BASE + USER_BASE_STATIC_MAP +11;
    
    public static final int ACTION_CHECK_UPSELL_SUCCESS = ACTION_USER_BASE + USER_BASE_STATIC_MAP +12;
    
    public static final int ACTION_CHECK_ROUTE_INTEGRITY = ACTION_USER_BASE + USER_BASE_STATIC_MAP +13;
    
    public static final int ACTION_CANCEL_GETTING_MORE_POIS = ACTION_USER_BASE + USER_BASE_STATIC_MAP +14;
    
    
    // ------------------------------------------------------------
    // Model id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_UPDATE = EVENT_MODEL_USER_BASE + USER_BASE_MAP + 1;

    public static final int EVENT_MODEL_SHOW_SUMMARY = EVENT_MODEL_USER_BASE + USER_BASE_MAP + 2;

    public static final int EVENT_MODEL_GET_DECIMATE_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_MAP + 3;

    public static final int EVENT_MODEL_GOTO_NAV = EVENT_MODEL_USER_BASE + USER_BASE_MAP + 4;

    public static final int EVENT_MODEL_GOTO_MAP = EVENT_MODEL_USER_BASE + USER_BASE_MAP + 5;

    public static final int EVENT_MODEL_GET_MORE_POIS = EVENT_MODEL_USER_BASE + USER_BASE_MAP + 8;
    
    public static final int EVENT_MODEL_GOTO_TRAFFIC_UPSELL = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 10;
    
    public static final int EVENT_MODEL_CLEAR = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 11;
    
    public static final int EVENT_MODEL_POI_MAP_BACK = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 12;
    
    public static final int EVENT_MODEL_GOT_RGC = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 13;
    
    public static final int EVENT_MODEL_POI_LIST = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 14;
    
    public static final int EVENT_MODEL_GET_MORE_ROUTE = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 15;
    
    public static final int EVENT_MODEL_GET_MORE_ROUTE_FINISHED = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 16;
        
    public static final int EVENT_MODEL_BACK_TO_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_STATIC_MAP + 17;
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final Integer KEY_B_IGNORE_TEXT_CHANGE = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 1);

    // A string to save the address inputed by user
    public static final Integer KEY_S_ADDRESS = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 3);

    public static final Integer KEY_S_POI_PHONENUMBER = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 6);
    
    public static final Integer KEY_O_MAP_SUMMARY_DECIMATED_ROUTE = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 7);
    
    public static final Integer KEY_I_MAP_LAYER_SETTING = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 8);
    
    public static final Integer KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 9);
    
    public static final Integer KEY_B_IS_CURRENT_LOCATION = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 11);
    
    public static final Integer KEY_O_TRAFFIC_INCIDENT_DETAIL = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 13);
    
    public static final Integer KEY_B_IS_MAP_RESULTS_MODE = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 14);
    
    public final static Integer KEY_B_IS_GOTO_BACKGROUND = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 15);
    
    public final static Integer KEY_I_RGC_LAT = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 16);
    public final static Integer KEY_I_RGC_LON = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 17);
    
    public final static Integer KDY_O_RGC_STOP = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 18);
    public final static Integer KEY_B_IS_CLEAR_ONEBOX = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 19);
    public final static Integer KEY_B_IS_FROM_RGC = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 20);
    
    public final static Integer KEY_B_NEED_RESTORE_POI_ANNOTATION = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 21);
    public final static Integer KEY_B_NEED_UPDATE_FLAGS = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 22);
    
    public final static Integer KEY_B_IS_SEARCH_NEAR_BY_CLICKED = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 23);
    
    public final static Integer KEY_I_MAP_LAYER = PrimitiveTypeCache.valueOf(STATE_MAP_BASE + 24);
    
    public static final int MAP_LAYER_TRAFFIC = 0;

    public static final int MAP_LAYER_CAMERA = 1;

    public static final int MAP_LAYER_SATELLITE = 2;
    
    
    // TODO: do we need base here?
    public static final int ID_MAP_BUTTON = 1000;

    public static final int ID_DRIVE_TO_BUTTON = 1001;

    public static final int ID_DSR_BUTTON = 1002;

    public static final int ID_SEARCH_BUTTON = 1003;

    public static final int ID_APP_BUTTON = 1004;
    
    public static final int ID_STATUS_INFO = 1005;
    
    public static final int ID_BOTTOM_BAR = 1006;
    
    public static final int ID_TAB_CONTAINER = 1007;


    public static final int ID_NULL_FIELD = 1013;

    public static final int ID_MAP_POI_INDICATOR = 1014;

    public static final int ID_SEARCH_BAR = 1015;

    public static final int ID_PROGRESS_BOX = 1017;
    
    public static final int ID_GPS_NOT_AVAILABLE_NOTIFICATION = 1018;
    
    public static final int ID_TAB_TURN_BUTTON = 1019;
    
    public static final int ID_TAB_TRAFFIC_BUTTON = 1020;
    
    public static final int ID_TITLE_CONTAINER = 1021;
    
    public static final String TYPE_SPEED_TRAP = "speed trap";
    
    public static final String TYPE_TRAFFIC_CAMERA = "traffic camera";
}

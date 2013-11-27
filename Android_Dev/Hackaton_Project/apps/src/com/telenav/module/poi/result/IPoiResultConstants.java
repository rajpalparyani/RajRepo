/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPoiResultConstants.java
 *
 */
package com.telenav.module.poi.result;

import com.telenav.app.android.scout_us.R;
import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-9-25
 */
interface IPoiResultConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_POI_RESULT_BASE = STATE_USER_BASE + USER_BASE_POI_RESULT;
    public static final int STATE_POI_RESULT_STARTING = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 1);
    public static final int STATE_POI_RESULT_LIST = STATE_POI_RESULT_BASE + 2;
    public static final int STATE_GOTO_POI_DETAIL = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 3);
    public static final int STATE_POI_REDO_SEARCHING = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 4);
    public static final int STATE_GOT_ADDRESS = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 5);
    public static final int STATE_POI_SORT_BY = MASK_STATE_TRANSIENT |(STATE_POI_RESULT_BASE + 6);
    public static final int STATE_POI_ENTRY_CHECK = MASK_STATE_TRANSIENT |(STATE_POI_RESULT_BASE + 7);
    public static final int STATE_POI_GOTO_MAP = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 8);
    public static final int STATE_POI_GOTO_NAV = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 10);
    public static final int STATE_POI_GOTO_RATING = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 11);
    public static final int STATE_POI_GOTO_SHARE = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 12);
    public static final int STATE_POI_CHECK_ALONGTYPE_CHANGE = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 13);
    public static final int STATE_POI_GOTO_CHANGE_LOCATION = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 14);
    public static final int STATE_POI_CHECK_ADDRESS_GOT = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 15);
    public static final int STATE_POI_CHECK_INPUT_TEXT = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 16);
    public static final int STATE_POI_GOTO_ONE_BOX = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 17);
    public static final int STATE_POI_GOTO_FAV_EDIT = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 18);
    public static final int STATE_POI_SEARCH_FEEDBACK = STATE_POI_RESULT_BASE + 19;    
    public static final int STATE_NO_GPS_WARNING =  MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 20);
    public static final int STATE_SEARCH_ANCHOR_SELECT = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 24);
    public static final int STATE_POI_RESULT_BACK_CHECKING = MASK_STATE_TRANSIENT | (STATE_POI_RESULT_BASE + 25);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_POI_RESULT_BASE     = CMD_COMMON_BASE + USER_BASE_POI_RESULT;
    public static final int CMD_POI_RESULT_SELECT   = CMD_POI_RESULT_BASE + 1;
    public static final int CMD_GOTO_POI_MAP        = CMD_POI_RESULT_BASE + 2;
    public static final int CMD_POI_SUGGEST_SELECT  = CMD_POI_RESULT_BASE + 3;
    public static final int CMD_HIT_BOTTOM          = CMD_POI_RESULT_BASE + 5;
    public static final int CMD_SEARCH_NEAR_DEST    = CMD_POI_RESULT_BASE + 7;
    public static final int CMD_SEARCH_NEAR_ROUTE   = CMD_POI_RESULT_BASE + 8;
    public static final int CMD_DRIVE_TO            = CMD_POI_RESULT_BASE + 9;
    public static final int CMD_CALL                = CMD_POI_RESULT_BASE + 10;
    public static final int CMD_MAP_IT              = CMD_POI_RESULT_BASE + 11;
    public static final int CMD_SAVE_TO_FAV         = CMD_POI_RESULT_BASE + 12;
    public static final int CMD_RATE_IT             = CMD_POI_RESULT_BASE + 13;
    public static final int CMD_SHARE_IT            = CMD_POI_RESULT_BASE + 14;
    public static final int CMD_GOTO_CHANGE_SORT    = CMD_POI_RESULT_BASE + 15;
    public static final int CMD_SEARCH_BY_BEST_MATCH   = CMD_POI_RESULT_BASE + 16;
    public static final int CMD_SEARCH_BY_DISTANCE = CMD_POI_RESULT_BASE + 17;
    public static final int CMD_IMPRESS_LISTITEM    = CMD_POI_RESULT_BASE + 20;
    public static final int CMD_GOTO_CHANGE_LOCATION = CMD_POI_RESULT_BASE + 21;
    public static final int CMD_CHECK_SEARCH   = CMD_POI_RESULT_BASE + 22;
    public static final int CMD_REMOVE_TEXT    = CMD_POI_RESULT_BASE + 23;
    public static final int CMD_SHOW_FEEDBACK    = CMD_POI_RESULT_BASE + 24;
    public static final int CMD_HIDE_FEEDBACK    = CMD_POI_RESULT_BASE + 25;
    public static final int CMD_FEEDBACK    = CMD_POI_RESULT_BASE + 26;
    public static final int CMD_SEARCH_NEAR_ME   = CMD_POI_RESULT_BASE + 27;
    
    public static final int CMD_REDO_SEARCH = CMD_POI_RESULT_BASE + 30;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_POI_RESULT_BASE = ACTION_USER_BASE + USER_BASE_POI_RESULT;
    public static final int ACTION_POI_RESULT_START_INIT = ACTION_POI_RESULT_BASE + 1;
    public static final int ACTION_ENTRY_CHECKING = ACTION_POI_RESULT_BASE + 2;
    public static final int ACTION_SORT_CHANGE_SEARCH = ACTION_POI_RESULT_BASE + 3;
    public static final int ACTION_GETING_MORE = ACTION_POI_RESULT_BASE + 4;
    public static final int ACTION_SEARCH_ALONG = ACTION_POI_RESULT_BASE + 7;
    public static final int ACTION_CALL_POI = ACTION_POI_RESULT_BASE + 9;
    public static final int ACTION_CHECK_SEARCHALONG = ACTION_POI_RESULT_BASE + 15;
    public static final int ACTION_CHANGE_LOCATION = ACTION_POI_RESULT_BASE + 16;
    public static final int ACTION_CHECK_ADDRESS_GOT = ACTION_POI_RESULT_BASE + 17;
    public static final int ACTION_UPDATE_SEARCH_CENTER = ACTION_POI_RESULT_BASE + 18;
    public static final int ACTION_SEARCH_BASE_NEW_LOCATION = ACTION_POI_RESULT_BASE + 19;
    public static final int ACTION_CHECK_TEXT_INPUT = ACTION_POI_RESULT_BASE + 20;
    public static final int ACTION_MAP_RESULTS = ACTION_POI_RESULT_BASE + 21;
    public static final int ACTION_MAP_IT = ACTION_POI_RESULT_BASE + 22;
    public static final int ACTION_CANCEL_SEARCH = ACTION_POI_RESULT_BASE + 23;
    public static final int ACTION_BACK_CHECKING = ACTION_POI_RESULT_BASE + 24;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_POI_RESULT_BASE = EVENT_MODEL_USER_BASE + USER_BASE_POI_RESULT;
    public static final int EVENT_MODEL_POI_RESULT_LIST = EVENT_MODEL_POI_RESULT_BASE + 1;
    public static final int EVENT_MODEL_GOTO_POI_MAP    = EVENT_MODEL_POI_RESULT_BASE + 2;
    public static final int EVENT_MODEL_REDO_SEARCH     = EVENT_MODEL_POI_RESULT_BASE + 3;
    public static final int EVENT_MODEL_RETURN_POIS     = EVENT_MODEL_POI_RESULT_BASE + 4;
    public static final int EVENT_MODEL_SHOW_RESEARCH_LIST = EVENT_MODEL_POI_RESULT_BASE + 6;
    public static final int EVENT_MODEL_GOTO_DETAIL = EVENT_MODEL_POI_RESULT_BASE + 7;
    public static final int EVENT_MODEL_CHANGE_ALONGROUTE_TYPE = EVENT_MODEL_POI_RESULT_BASE + 8;
    public static final int EVENT_MODEL_ALONGROUTE_NO_CHANGE = EVENT_MODEL_POI_RESULT_BASE + 9;
    public static final int EVENT_MODEL_POST_ADDRESS_TO_SUPER = EVENT_MODEL_POI_RESULT_BASE + 10;
    public static final int EVENT_MODEL_GOTO_ONE_BOX = EVENT_MODEL_POI_RESULT_BASE + 11;
    public static final int EVENT_MODEL_GOTO_NO_GPS_WARNING = EVENT_MODEL_POI_RESULT_BASE + 12;
    public static final int EVENT_MODEL_POST_SEARCH_ANCHOR = EVENT_MODEL_POI_RESULT_BASE + 13;
    // -------------------------------------------------------------
    // Key constants definition
    // -------------------------------------------------------------
    public static final Integer KEY_I_LAST_SHOWN_INDEX = PrimitiveTypeCache.valueOf(STATE_POI_RESULT_BASE + 1);
    public static final Integer KEY_I_CHANGETO_ALONGROUTE_TYPE  = PrimitiveTypeCache.valueOf(STATE_POI_RESULT_BASE + 2);
    public static final Integer KEY_I_CHANGETO_SORT_TYPE = PrimitiveTypeCache.valueOf(STATE_POI_RESULT_BASE + 3);
    public static final Integer KEY_B_IS_GET_MORE = PrimitiveTypeCache.valueOf(STATE_POI_RESULT_BASE + 4);
    public static final Integer KEY_S_INPUT_TEXT = PrimitiveTypeCache.valueOf(STATE_POI_RESULT_BASE + 5);
    
    //--------------------------------------------------------------
    // 
    //--------------------------------------------------------------
    public static final int SORT_BY_COMOBOX_COMPONENT_ID = 10000;
    public static final int POI_LIST_ID = 10001;
    public static final int ID_ONEBOX_INPUT_FIELD = 10003;
    public static final int CATEGORY_ALL = -1;
    public static final int ID_SEARCH_LOCATION_COMP = 10004;
    public static final int ID_SEARCH_BUTTON_COMP = 10005;
    public static final int ID_TITLE_CONTAINER = 10006;
    public static final int ID_DUMMY_SORTBY = 10007;
    public static final int ID_DROPDOWN_CONTAINER = 10009;
    public static final int ID_POI_TOP_CONTAINER = 10010;
    public static final int ID_ALONG_ROUTE_TOP_CONTAINER = 10011;
    public static final int ID_ALONG_ROUTE_BOTTOM_CONTAINER = 10012;
    public static final int ID_RESULT_TITLE_LABEL = 10013;
    public static final int ID_TOP_CONTAINER_VERTICAL_GAP = 10014;
    public static final int POI_RESULT_FEEDBACK_LABEL = 10015;
    public static final int POI_RESULT_FEEDBACK_CONTAINER = 10016;
    public static final int ID_POI_RESULT_MAP_BUTTON = 10017;
    public static final int ID_POI_RESULT_CONTENT_CONTAINER = 10018;
    
    
    public static final int SEARCH_BY_PRICE_ANY = 50500;
    public static final int SEARCH_BY_PRICE_PLUS = 703;
    public static final int SEARCH_BY_PRICE_PREMIUM = 704;
    public static final int SEARCH_BY_PRICE_DIESEL = 705;
    public static final int SEARCH_BY_PRICE_REGULAR = 702;
    
    public final static int[][] UI_COMMAND_TABLE = new int[][]
            {
                {R.id.commonOneboxDsrButton,                    CMD_COMMON_DSR},
                {R.id.commonOneboxTextView,                     CMD_COMMON_GOTO_ONEBOX},
                {R.id.commonTitle0IconButton,                   CMD_GOTO_POI_MAP},
                {R.id.placeList0SearchAlongUpAheadView,         CMD_SEARCH_NEAR_ROUTE},
                {R.id.placeList0SearchAlongUpDestinationView,   CMD_SEARCH_NEAR_DEST},
                {R.id.placeList0SortByBestMatchView,            CMD_SEARCH_BY_BEST_MATCH},
                {R.id.placeList0SortByDistanceView,             CMD_SEARCH_BY_DISTANCE},
            };
}

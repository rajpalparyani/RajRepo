/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceConstants.java
 *
 */
package com.telenav.module.poi.search;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;
/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-18
 */
interface IPoiSearchConstants extends ICommonConstants
{
    //-------------------------------------------------------------
    // State definition
    //-------------------------------------------------------------
    public static final int STATE_POI_SEARCH_BASE = STATE_USER_BASE + USER_BASE_POI_SEARCH;
    public static final int STATE_SEARCH_QUICK_FIND = STATE_POI_SEARCH_BASE + 1;
    public static final int STATE_DOING_POI_SEARCH = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 2);
    public static final int STATE_SEARCH_CATEGORIES = STATE_POI_SEARCH_BASE + 3;
    public static final int STATE_DOING_SEARCH_CHECKING = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 4);
    public static final int STATE_POI_SEARCHING = MASK_STATE_TRANSIENT | (STATE_POI_SEARCH_BASE + 5);
    public static final int STATE_GOTO_RESULT_LIST = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 6);
    public static final int STATE_GOTO_ONEBOX_SEARCH    = MASK_STATE_TRANSIENT | (STATE_POI_SEARCH_BASE + 9);
    public static final int STATE_NO_RESULT = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 10);
    public static final int STATE_GOTO_DSR = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 13);
    public static final int STATE_SUB_CATELOG = STATE_POI_SEARCH_BASE + 15;
    public static final int STATE_GOTO_ROUTE_SUMMARY = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 18);
    public static final int STATE_NO_GPS_WARNING = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 20);
    public static final int STATE_GO_TO_MOVING_MAP = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 21);
    public static final int STATE_GO_TO_TURN_MAP = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 22);
    public static final int STATE_CHECK_ANCHOR_BEFORE_SEARCH_EVENT = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 25);
    public static final int STATE_GO_TO_LOCAL_EVENT = MASK_STATE_TRANSIENT |(STATE_POI_SEARCH_BASE + 26);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_POI_SEARCH_BASE = CMD_USER_BASE + USER_BASE_POI_SEARCH;
    public static final int CMD_QUICK_BUTTON_SELECTED =  CMD_POI_SEARCH_BASE + 2;
    public static final int CMD_POISEARCH_REMOVE_TEXT = CMD_POI_SEARCH_BASE + 3;
    public static final int CMD_GOTO_SEARCH_CATEGORIES = CMD_POI_SEARCH_BASE + 4;
    public static final int CMD_GOTO_NEXT_POI = CMD_POI_SEARCH_BASE + 5;
    public static final int CMD_GOTO_PREVIOUS_POI = CMD_POI_SEARCH_BASE + 6;
    public static final int CMD_SELECT_CATEGORY_LIST = CMD_POI_SEARCH_BASE + 8;
    public static final int CMD_GOTO_SUB_CATELOG = CMD_POI_SEARCH_BASE + 9;
    public static final int CMD_GOTO_ROUTE_SUMMARY = CMD_POI_SEARCH_BASE + 12;
    public static final int CMD_GOTO_NAV         = CMD_POI_SEARCH_BASE + 13;
    public static final int CMD_STAY_IN_SEARCH   = CMD_POI_SEARCH_BASE + 14;
    public static final int CMD_GOTO_TURN_MAP = CMD_POI_SEARCH_BASE + 16;
    public static final int CMD_CLEAR_TEXT = CMD_POI_SEARCH_BASE + 17;
    public static final int CMD_HOME = CMD_POI_SEARCH_BASE + 18;
    public static final int CMD_EVENT_SELECTED = CMD_POI_SEARCH_BASE + 20;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_POI_SEARCH_BASE = ACTION_USER_BASE + USER_BASE_POI_SEARCH;
    public static final int ACTION_DOING_SEARCH = ACTION_POI_SEARCH_BASE + 1;
    public static final int ACTION_CHECK_CATEGORY = ACTION_POI_SEARCH_BASE + 2;
    public static final int ACTION_CANCEL_SEARCH = ACTION_POI_SEARCH_BASE + 3;
    public static final int ACTION_CHECK_ANCHOR = ACTION_POI_SEARCH_BASE + 4;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_POISEARCH_BASE = EVENT_MODEL_USER_BASE + USER_BASE_POI_SEARCH;
    public static final int EVENT_MODEL_POISEARCH_REMOVE = EVENT_MODEL_POISEARCH_BASE + 1;
    public static final int EVENT_MODEL_GOTO_SEARCH_CATEGORIES = EVENT_MODEL_POISEARCH_BASE + 2;
    public static final int EVENT_MODEL_GOTO_POI_LIST = EVENT_MODEL_POISEARCH_BASE + 3;
    public static final int EVENT_MODEL_GOTO_SEARCH_QUICK_FIND = EVENT_MODEL_POISEARCH_BASE + 5;
    public static final int EVENT_MODEL_BACK          = EVENT_MODEL_POISEARCH_BASE + 6;
    public static final int EVENT_MODEL_GOTO_NO_GPS_WARNING = EVENT_MODEL_POISEARCH_BASE + 7;
    public static final int EVENT_MODEL_SHOW_PROGRESS = EVENT_MODEL_POISEARCH_BASE + 8;
    public static final int EVENT_MODEL_GO_TO_LOCAL_EVENT = EVENT_MODEL_POISEARCH_BASE + 9;
    //-----------------------------------------------------------
    // key value between and in modules.
    //-----------------------------------------------------------
    public static final Object KEY_I_ID_SELECTED_QUICK_FIND = PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 1);
    public static final Object KEY_S_CATEGORY_LIST_ID       = PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 2);
    public static final Object KEY_B_SHOWRECENT_FLAG        = PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 3);
    public static final Object KEY_I_VIEW_MIX_INDEX         = PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 4);
    public static final Object KEY_I_VIEW_POI_INDEX         = PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 5);
    public static final Object KEY_I_VIEW_SPONSORED_INDEX   = PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 6);
    public static final Object KEY_I_REQUEST_INDEX          = PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 7);
    public static final Integer KEY_B_IS_MOST_PUPOLAR_SEARCH= PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 8);
    public static final Integer KEY_S_MORE_TITLE_NAME= PrimitiveTypeCache.valueOf(STATE_POI_SEARCH_BASE + 9);
    
    //------------------------------------------------------------
    //
    //------------------------------------------------------------
    public final static int ID_MORE_CMD = 0;
    public final static int ID_CATEGORY_LIST = 10051;
    public final static int ID_TEXTFIELD_FILTER     = 10052;
    public final static int ID_SUB_CATEGORY_LIST = 10053;
    public final static int ID_LABEL_TITLE = 10054;
    public final static int ID_HOME_BUTTON = 10055;
    
    public final static int QUICK_SEARCHING_PREFER_ROW_NUM_P = -1; //driven by icon, no setting.
    public final static int QUICK_SEARCHING_PREFER_ROW_NUM_L = 2; //when landscape, VDD prefer to 2 rows.
    public final static int DIRECTORY_ID = -2;
    public final static int COMPLETE_LIST_ID = -1;
}

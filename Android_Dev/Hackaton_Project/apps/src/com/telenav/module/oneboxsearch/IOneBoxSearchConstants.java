/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IOneBoxSearchConstants.java
 *
 */
package com.telenav.module.oneboxsearch;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-3
 */
public interface IOneBoxSearchConstants extends ICommonConstants
{
    // -------------------------------------------------------------
    // State definition
    // -------------------------------------------------------------
    public final static int STATE_ONE_BOX_BASE = STATE_USER_BASE + USER_BASE_ONE_BOX_SEARCH;

    public final static int STATE_ONE_BOX_MAIN = STATE_ONE_BOX_BASE + 1; 
    public final static int STATE_GOT_RECENT_FAV = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 2);
    public final static int STATE_DOING_ONEBOX_SEARCH = MASK_STATE_TRANSIENT |(STATE_ONE_BOX_BASE + 3);
    public final static int STATE_DID_U_MEAN = STATE_ONE_BOX_BASE + 4;
    public final static int STATE_GOT_STOPS = MASK_STATE_TRANSIENT |(STATE_ONE_BOX_BASE + 5);
    public final static int STATE_CHECK_IS_SEARCH_DIRECTLY = MASK_STATE_TRANSIENT |(STATE_ONE_BOX_BASE + 7);
    public final static int STATE_DID_U_MEAN_MULTISTOP = STATE_ONE_BOX_BASE + 8;
    public final static int STATE_GOTO_CHANGE_LOCATION = MASK_STATE_TRANSIENT|(STATE_ONE_BOX_BASE + 9);
    public final static int STATE_CHECK_ADDRESS_GOT = MASK_STATE_TRANSIENT|(STATE_ONE_BOX_BASE + 10);
    public final static int STATE_POST_ADDRESS_TO_SUPER = MASK_STATE_TRANSIENT|(STATE_ONE_BOX_BASE + 11);
    public final static int STATE_QUERY_CLEAR_HISTORY = MASK_STATE_TRANSIENT|(STATE_ONE_BOX_BASE + 12);
    public final static int STATE_NO_GPS_WARNING = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 14);
    public final static int STATE_VALIDATE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 15);
    public final static int STATE_CHECK_VALIDATE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 16);
    public final static int STATE_POIS_GOT = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 17);
    public final static int STATE_CHECK_CITY = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 18);
    public final static int STATE_VALIDATE_INPUT = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 19);
    public final static int STATE_GOT_ONE_ADDRESS = MASK_STATE_TRANSIENT |(STATE_ONE_BOX_BASE + 20);
    public final static int STATE_ONEBOX_GO_TO_SET_HOME = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 21);
    public final static int STATE_ONEBOX_GO_TO_SET_WORK = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 22);
    public final static int STATE_VALIDATED_ADDRESS_RETURNED = MASK_STATE_TRANSIENT | (STATE_ONE_BOX_BASE + 23);

    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public final static int CMD_ONE_BOX_BASE = CMD_USER_BASE + USER_BASE_ONE_BOX_SEARCH;
    public final static int CMD_SELECT_RECENT_FAV = CMD_ONE_BOX_BASE + 1;
    public final static int CMD_DO_ONEBOX_SEARCH = CMD_ONE_BOX_BASE + 2;
    public final static int CMD_DO_CATELOG_SEARCH = CMD_ONE_BOX_BASE + 3;
    public final static int CMD_POI_SUGGEST_SELECT = CMD_ONE_BOX_BASE + 4;
    public final static int CMD_STOP_SELECT = CMD_ONE_BOX_BASE + 5;
    public final static int CMD_REMOVE_ALL = CMD_ONE_BOX_BASE + 6;
    public final static int CMD_GOTO_CHANGE_LOCATION = CMD_ONE_BOX_BASE + 7;
    public final static int CMD_CLEAR_SEARCH_HISTORY = CMD_ONE_BOX_BASE + 8;
    public final static int CMD_GOTO_BUSINESS = CMD_ONE_BOX_BASE + 9;
    public final static int CMD_GOTO_ADDRESS = CMD_ONE_BOX_BASE + 10;
    
    public final static int CMD_SAVE = CMD_ONE_BOX_BASE +11;
    public final static int CMD_BACKSPACE = CMD_ONE_BOX_BASE + 12;
    public final static int CMD_SELECT_CITY = CMD_ONE_BOX_BASE + 13;
    public final static int CMD_SELECT_ADDRESS = CMD_ONE_BOX_BASE + 14;
    public final static int CMD_VALIDATE_CONTACT_ADDRESS = CMD_ONE_BOX_BASE + 15;
    public final static int CMD_ONEBOX_GO_TO_SET_HOME = CMD_ONE_BOX_BASE + 16;
    public final static int CMD_ONEBOX_GO_TO_SET_WORK = CMD_ONE_BOX_BASE + 17;
    
    //Use Home constants
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public final static int EVENT_ONE_BOX_BASE = EVENT_CONTROLLER_USER_BASE + USER_BASE_ONE_BOX_SEARCH;
    public final static int EVENT_SHOW_SUGGESTIONS = EVENT_ONE_BOX_BASE + 1;
    public final static int EVENT_MODEL_RETURN_STOPS = EVENT_ONE_BOX_BASE + 2;
    public final static int EVENT_SEARCHING_DIRECTLY = EVENT_ONE_BOX_BASE + 3;
    public final static int EVENT_GOTO_ONE_BOX_MAIN = EVENT_ONE_BOX_BASE + 4;
    public final static int EVENT_SHOW_MULISTOPS = EVENT_ONE_BOX_BASE + 5;
    public final static int EVENT_MODEL_RETURN_ADDRESS_TO_SUPER = EVENT_ONE_BOX_BASE + 6;
    public final static int EVENT_MODEL_GOTO_NO_GPS_WARNING = EVENT_ONE_BOX_BASE + 7;
    public final static int EVENT_MODEL_POIS_GOT = EVENT_ONE_BOX_BASE + 8;

    public final static int EVENT_MODEL_RETURN_CITY_ADDRESS = EVENT_ONE_BOX_BASE + 9;
    public final static int EVENT_MODEL_GO_TO_VALIDATE_ADDRESS = EVENT_ONE_BOX_BASE + 10;
    public final static int EVENT_MODEL_VALIDATE_LIST_HOME = EVENT_ONE_BOX_BASE + 11;
    public final static int EVENT_MODEL_VALIDATE_HOME = EVENT_ONE_BOX_BASE + 12;
    public final static int EVENT_MODEL_ONE_ADDRESS = EVENT_ONE_BOX_BASE + 13;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public final static int ACTION_ONE_BOX_BASE = ACTION_USER_BASE + USER_BASE_ONE_BOX_SEARCH;
    public final static int ACTION_ONEBOX_SEARCH =  ACTION_ONE_BOX_BASE + 1;
    public final static int ACTION_INIT = ACTION_ONE_BOX_BASE + 2;
    public final static int ACTION_PREPARE_SUGGESTION = ACTION_ONE_BOX_BASE + 3;
    public final static int ACTION_SET_LOCATION_CHANGE = ACTION_ONE_BOX_BASE + 4;
    public final static int ACTION_CHECK_ADDRESS_GOT = ACTION_ONE_BOX_BASE + 5;
    public final static int ACTION_CLEAR_HISTORY = ACTION_ONE_BOX_BASE + 6;
    public final static int ACTION_CLEAR_MULTI_RESULTS = ACTION_ONE_BOX_BASE + 7;
    public final static int ACTION_CHECK_CITY = ACTION_ONE_BOX_BASE + 8;
    public final static int ACTION_VALIDATE_INPUT = ACTION_ONE_BOX_BASE + 9;
    public final static int ACTION_REFRESH_AUTO_SUGGESTION = ACTION_ONE_BOX_BASE + 10;
    public final static int ACTION_VALIDATED_ADDRESS_RETURNED = ACTION_ONE_BOX_BASE + 11;
    
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public final static Integer KEY_I_DATA_TYPE    = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 1);
    public final static Integer KEY_O_LIST_ADAPTER = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 2);
    public final static Integer KEY_V_RESULT_SUGGESTIONS = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 3);
    public final static Integer KEY_B_CLEAR_SEARCH_HISTORY = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 4);
    public final static Integer KEY_B_IS_SAVE_TEXT_RECENT_SEARCH = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 5);
    public final static Integer KEY_B_ONEBOX_SEARCH = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 6);
    //Use Home constants
//    public final static Integer KEY_V_SEARCH_LIST   = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 9);
//    public final static Integer KEY_V_FILTER_SEARCH_LIST = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 10);
    public final static Integer KEY_O_TEXTFIELD_FILTER = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 11);
    public static final Integer KEY_B_IS_FROM_SUGGEST_SELECTION = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 12);    
    public static final Integer KEY_S_LAST_INTERNATIONAL_SEARCH_REGION = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 13);  
    
    public final static Integer KEY_S_CITY = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 14);
    public static final Integer KEY_S_STREET_INIT_TEXT = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 15);    
    public static final Integer KEY_S_CITY_INIT_TEXT = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 16);    
    public final static Integer KEY_S_TITLE = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 17);
    public static final Integer KEY_V_NEAR_CITIES = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 18);    
    public static final Integer KEY_V_FILTER_SEARCH_LIST = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 19);    
    public static final Integer KEY_B_FROM_LIST = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 20);    
    public static final Integer KEY_V_SEARCH_LIST = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 21);
    public static final Integer KEY_B_DETAIL_FROM_CONTACT = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 22); 
    public final static Integer KEY_S_LABEL_FROM_CONTACT = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 23);
    public final static Integer KEY_S_PTN_FROM_CONTACT = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 24);
    public final static Integer KEY_B_NEED_REFRESH_AUTO_SUGGESTION = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 25);
    public final static Integer KEY_O_ANCHOR_CURRENT_ADDRESS = PrimitiveTypeCache.valueOf(STATE_ONE_BOX_BASE + 26);
    
    //------------------------------------------------------------
    // component id
    //------------------------------------------------------------
	//Start with 2 for difference between super home module.
    public final static int ID_ONEBOX_INPUT_FIELD = 20001;
    public final static int ID_CHANGE_LOCATION_COMP = 20002;
    public final static int ID_SUGGEST_LIST_COMP = 20003;
    public final static int ID_TAB_BUTTON_BUSINESS = 20004;
    public final static int ID_TAB_BUTTON_ADDRESS = 20005;
    public final static int ID_TAB_CONTAINER = 20006;
    public final static int ID_ONEBOX_SEARCH_CONTAINER = 20007;
    public final static int ID_ADDRESS_TEXTFIELD = 20008;
    public final static int ID_DISTRICT_TEXTFIELD = 20009;
    
    public final static int ID_TAB_INPUTTYPE_CHN = 20010;
    public final static int ID_TAB_INPUTTYPE_PY = 20011;
    public final static int ID_BUTTON_CITY = 20012;
    public final static int ID_ADDESS_WEBVIEW_CONTAINER = 20013;
    public final static int ID_SEARCH_CONTAINER = 20014;
    public final static int ID_ADDRESS_WEB_COMPONENT = 20015;

    public final static int ID_CONTENT_CONTAINER = 20016;
    public final static int ID_ADDRESS_INPUT_FIELD = 20017;
    public final static int ID_CITY_INPUT_FIELD = 20018;
    public final static int ID_INPUT_AREA_CONTAINER = 20019;
    
}

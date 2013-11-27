/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ICommonConstants.java
 *
 */
package com.telenav.mvc;

import com.telenav.util.PrimitiveTypeCache;

/**
 * 
 * The common constants which will be used in sub-module.
 * 
 * It contains(not limited):
 * 
 * 1) Common command id - must start with "CMD_"
 * 
 * 2) Common model event - must start with "EVENT_MODEL_"
 * 
 * 3) Common controller event - must start with "EVENT_CONTROLLER_"
 * 
 * 4) Common action (optional) - must start with "ACTION_"
 * 
 * 5) Common state table - the global STATE_TABLE_COMMON will be used for all sub-modules.
 * 
 * 6) Common key - must start with "KEY_"
 * 
 * For Integer value "KEY_I_"
 * 
 * For Long value "KEY_L_"
 * 
 * For String value "KEY_S_"
 * 
 * For Vector value "KEY_V_"
 * 
 * For boolean value "KEY_B_"
 * 
 * For array objects such as IStop[], value "KEY_A_"
 * 
 * Other object should use "KEY_O_"
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */
public interface ICommonConstants extends ITriggerConstants, IActionConstants, IStateConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_COMMON_ERROR = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 1);
    public static final int STATE_COMMON_GOTO_SETTINGS = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 2);
    public static final int STATE_COMMON_GOTO_FEEDBACK = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 3);
    public static final int STATE_COMMON_GOTO_ONEBOX = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 4);
    public static final int STATE_COMMON_TIMEOUT_MESSAGEBOX = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 6);
    public static final int STATE_COMMON_SHARE_ADDRESS = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 7);
    public static final int STATE_COMMON_BACK_TO_MOVING_MAP = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 8);
    
    //common dsr flow
    public static final int STATE_COMMON_DSR_SEARCH = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 9);
    public static final int STATE_COMMON_DSR_MAP = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 10);
    public static final int STATE_COMMON_DSR_NAV = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 11);
    
    //for link jump
    public static final int STATE_COMMON_LINK_TO_MAP = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 12);
    public static final int STATE_COMMON_LINK_TO_AC = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 13);
    public static final int STATE_COMMON_LINK_TO_SEARCH = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 14);
    public static final int STATE_COMMON_LINK_TO_EXTRA = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 15);
    public static final int STATE_COMMON_DSR = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 16);
    
    public static final int STATE_COMMON_NO_BT_MSG_BOX = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 17);
    public static final int STATE_COMMON_GOTO_ABOUT = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 18);
    
    public static final int STATE_COMMON_GOTO_HOME = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 19);
    public static final int STATE_COMMON_EXIT = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 20);
    public static final int STATE_COMMON_EXIT_NAV_FROM_PLUGIN = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 21);
    //for region switch
    public static final int STATE_SWITCHING_REGION = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 22);
    public static final int STATE_SWITCHING_SUCC = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 23);
    public static final int STATE_SWITCHING_FAIL = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 24);
    public static final int STATE_DETECTING_REGION = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 25);
    //for back to Entry
    public static final int STATE_COMMON_BACK_TO_ENTRY = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 26);
    public static final int STATE_GENERAL_FEEDBACK  = STATE_COMMON_BASE + 27;
    public static final int STATE_COMMON_EXIT_NAV_FROM_SHORTCUT = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 28);
    public static final int STATE_LAUNCH_SECRET_FUNCTIONS = STATE_COMMON_BASE + 29;
    public static final int STATE_GO_TO_SIGN_IN = STATE_COMMON_BASE + 30;
    public static final int STATE_GO_TO_SIGN_UP = STATE_COMMON_BASE + 31;
    public static final int STATE_COMMON_EXIT_NAV_CONFIRM= MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 32);
    //for friendly user
    public static final int STATE_SHOW_FRIEND_USER_POPUP = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 33);

    public static final int STATE_MAP_DOWNLOADED_STATUS_CHANGED = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 34);
    
    public static final int STATE_COMMON_MAP_DOWNLOAD_ERROR = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 35);
    
    public static final int STATE_EXIT_NAV = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 36);
    
    public static final int STATE_END_DETOUR = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 37);
    
    public static final int STATE_EXIT_APPLICATION = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 38);
    
    public static final int STATE_GOTO_CONTACTS = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 39);
    
    public static final int STATE_COMMON_DWF_FROM_PLUGIN = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 40);
    
    public static final int STATE_CHECK_EXIT_NAV = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 41);
    
    public static final int STATE_COMMON_DWF_CHECK_DATA = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 42);
    
    public static final int STATE_COMMON_DWF_RESOLVE_TINY_URL = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 43);
    
    public static final int STATE_COMMON_DWF_PARAMETER_ERROR = MASK_STATE_TRANSIENT | (STATE_COMMON_BASE + 44);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_COMMON_OK = CMD_COMMON_BASE + 1;

    public static final int CMD_COMMON_BACK = CMD_COMMON_BASE + 2;

    public static final int CMD_COMMON_CANCEL = CMD_COMMON_BASE + 3;

    public static final int CMD_COMMON_EXIT = CMD_COMMON_BASE + 4;
    
    public static final int CMD_COMMON_HOME = CMD_COMMON_BASE + 5;
    
    //for bottom bar
    public static final int CMD_COMMON_LINK_TO_MAP = CMD_COMMON_BASE + 6;
    public static final int CMD_COMMON_LINK_TO_AC = CMD_COMMON_BASE + 7;
    public static final int CMD_COMMON_DSR = CMD_COMMON_BASE + 8;
    public static final int CMD_COMMON_LINK_TO_SEARCH = CMD_COMMON_BASE + 9;
    public static final int CMD_COMMON_LINK_TO_EXTRAS = CMD_COMMON_BASE + 10;
    
    public static final int CMD_COMMON_PREFERENCE = CMD_COMMON_BASE + 11;
    
    public static final int CMD_COMMON_PREFERENCE_ICON_ON_DASHBOARD = CMD_COMMON_BASE + 12;
    
    public static final int CMD_COMMON_GOTO_ONEBOX = CMD_COMMON_BASE + 13;
    
    public static final int CMD_COMMON_SHARE = CMD_COMMON_BASE + 14;
    
    public static final int CMD_COMMON_DELETE = CMD_COMMON_BASE + 15;
    
    public static final int CMD_COMMON_ADD_TO_FAVORITES = CMD_COMMON_BASE + 16;
    
    public static final int CMD_COMMON_CHANGE_LOCATION = CMD_COMMON_BASE + 17;
    
    public static final int CMD_COMMON_ABOUT = CMD_COMMON_BASE + 18;
    
    public static final int CMD_COMMON_ONE_KEY_CALL = CMD_COMMON_BASE + 19;
    
    public static final int CMD_SHOW_INCIDENT_DETAIL = CMD_COMMON_BASE + 20;
    
    public static final int CMD_GENERAL_FEEDBACK = CMD_COMMON_BASE + 21;
    
    public static final int CMD_GENERAL_MY_PROFILE = CMD_COMMON_BASE + 22;

    public static final int CMD_CHANGE_SWITCH = CMD_COMMON_BASE + 24;
    
    public static final int CMD_COMMON_EDIT = CMD_COMMON_BASE + 25;
    
    public static final int CMD_COMMON_CALL = CMD_COMMON_BASE + 26;
    
    public static final int CMD_COMMON_SAVE = CMD_COMMON_BASE + 27;
    
    public static final int CMD_COMMON_SWITCH_AUDIO = CMD_COMMON_BASE + 28;

    public static final int CMD_SECRET_KEY = CMD_COMMON_BASE + 29;
    
    public static final int CMD_COMMON_LOGIN = CMD_COMMON_BASE + 30;
    
    public static final int CMD_COMMON_CREATE_ACCOUNT = CMD_COMMON_BASE + 31;
    
    //Friendly user
    
    public static final int CMD_RATE_APP = CMD_COMMON_BASE + 32;

    public static final int CMD_REMIND_ME_LATER = CMD_COMMON_BASE + 33;

    public static final int CMD_NO_THANKS = CMD_COMMON_BASE + 34;
    
//    public static final int CMD_SHOW_FRIEND_USER_POPUP = CMD_COMMON_BASE + 35;

    public static final int CMD_COMMON_REPORT_AN_ISSUE = CMD_COMMON_BASE + 36;
    
    public static final int CMD_CHECK_SHARE_ETA = CMD_COMMON_BASE + 37;
    
    public static final int CMD_COMMON_SWITCH_AIRPLANE = CMD_COMMON_BASE + 38;
    
    public static final int CMD_HIDE_NOTIFICATION = CMD_COMMON_BASE + 39;
    
    public static final int CMD_COMMON_END_DETOUR = CMD_COMMON_BASE + 40;
    
    public static final int CMD_COMMON_END_TRIP = CMD_COMMON_BASE + 41;
    
    public static final int CMD_CONTACTS = CMD_COMMON_BASE + 42;
    
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_EXIT = ACTION_COMMON_BASE + 1;
    public static final int ACTION_SWITCH_AUDIO = ACTION_COMMON_BASE + 2;
    public static final int ACTION_LOG_EXIT = ACTION_COMMON_BASE + 3;  
    
    public static final int ACTION_RATE_APP = ACTION_COMMON_BASE + 4;
    
    public static final int ACTION_REMIND_LATER = ACTION_COMMON_BASE + 5;
    
    public static final int ACTION_NO_THANKS = ACTION_COMMON_BASE + 6;
    
    public static final int ACTION_CANCEL_POPUP = ACTION_COMMON_BASE + 7;

    public static final int ACTION_EXIT_NAV = ACTION_COMMON_BASE + 8;  

    public static final int ACTION_RESUME_NAV = ACTION_COMMON_BASE + 9;  

    public static final int ACTION_CHECK_MODULE_READY = ACTION_COMMON_BASE + 10; 
    
    public static final int ACTION_SHOW_TIMEOUT_MESSAGE = ACTION_COMMON_BASE + 11;

    public static final int ACTION_EDIT_PLACE_CHECK = ACTION_COMMON_BASE + 12;
    
    public static final int ACTION_IGNORE_END_DETOUR = ACTION_COMMON_BASE + 13;
    
    public static final int ACTION_DWF_CHECK_DATA = ACTION_COMMON_BASE + 14;
    
    public static final int ACTION_DWF_RESOLVE_URL = ACTION_COMMON_BASE + 15;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_POST_ERROR = EVENT_MODEL_COMMON_BASE + 1;
    public static final int EVENT_MODEL_SHOW_TIMEOUT_MESSAGE = EVENT_MODEL_COMMON_BASE + 2;
    public static final int EVENT_MODEL_BACK_TO_MOVING_MAP = EVENT_MODEL_COMMON_BASE + 3;
    
    /**
     * Q: What is the difference between CMD_COMMON_BACK and EVENT_MODEL_COMMON_BACK
     * A: They are mostly the same except that CMD_COMMON_BACK normally triggered by initiative UI event
     *    while EVENT_MODEL_COMMON_BACK is not.
     */
    
    public static final int EVENT_MODEL_COMMON_BACK = EVENT_MODEL_COMMON_BASE + 4;
    
    public static final int EVENT_MODEL_EXIT_NAV_FROM_PLUGIN = EVENT_MODEL_COMMON_BASE + 5;
    //for region switch
    public static final int EVENT_MODEL_SWITCHING_REGION = EVENT_MODEL_COMMON_BASE + 6;
    public static final int EVENT_MODEL_SWITCHING_FAIL = EVENT_MODEL_COMMON_BASE + 7;
    public static final int EVENT_MODEL_SWITCHING_SUCC_FROM_SERVER = EVENT_MODEL_COMMON_BASE + 8;
    public static final int EVENT_MODEL_CONTINUE = EVENT_MODEL_COMMON_BASE + 9;
    public static final int EVENT_MODEL_BACK_TO_PRE = EVENT_MODEL_COMMON_BASE + 10;
    public static final int EVENT_MODEL_DETECTING_REGION = EVENT_MODEL_COMMON_BASE + 11;
    public static final int EVENT_MODEL_CHECK_REGION = EVENT_MODEL_COMMON_BASE + 12;
    
    //back to entry
    public static final int EVENT_MODEL_BACK_TO_ENTRY = EVENT_MODEL_COMMON_BASE + 13;
    public static final int EVENT_MODEL_EXIT_BROWSER = EVENT_MODEL_COMMON_BASE + 14;
    
    //shortcut
    public static final int EVENT_MODEL_SHORTCUT_TO_PLACE = EVENT_MODEL_COMMON_BASE + 15;
    public static final int EVENT_MODEL_EXIT_NAV_FROM_SHORTCUT = EVENT_MODEL_COMMON_BASE + 16;
    public static final int EVENT_MODEL_SHORTCUT_TO_MAP = EVENT_MODEL_COMMON_BASE + 17;
    
    //map download status changed
    public static final int EVENT_MODEL_MAP_DOWNLOADED_STATUS_CHANGED = EVENT_MODEL_COMMON_BASE + 18;
    public static final int EVENT_MODEL_SHOW_MAP_DOWNLOADED_STATUS_CHANGED_NOTIFICATION = EVENT_MODEL_COMMON_BASE + 19;
    
    public static final int EVENT_MODEL_MAP_DOWNLOAD_ERROR = EVENT_MODEL_COMMON_BASE + 20;
    
    public static final int EVENT_MODEL_DWF_FROM_PLUGIN = EVENT_MODEL_COMMON_BASE + 21;
    public static final int EVENT_MODEL_SHOW_FRIEND_USER_POPUP = EVENT_MODEL_COMMON_BASE + 22;
    public static final int EVENT_MODEL_EXIT_NAV_CONFIRM = EVENT_MODEL_COMMON_BASE + 23;    
    public static final int EVENT_MODEL_HIDE_NAV_CONFIRM = EVENT_MODEL_COMMON_BASE + 24;   
    public static final int EVENT_MODEL_COMMON_EXIT_NAV = EVENT_MODEL_COMMON_BASE + 25;
    public static final int EVENT_MODEL_DWF_LAUNCH_SESSION = EVENT_MODEL_COMMON_BASE + 26;
    public static final int EVENT_MODEL_DWF_RESOLVE_TINY_URL = EVENT_MODEL_COMMON_BASE + 27;
    public static final int EVENT_MODEL_DWF_PARAMETER_ERROR = EVENT_MODEL_COMMON_BASE + 28;

    // ------------------------------------------------------------
    // The Controller event id definition
    // ------------------------------------------------------------
    public static final int EVENT_CONTROLLER_CATEGORY_DELETED = EVENT_CONTROLLER_COMMON_BASE + 1;
    public static final int EVENT_CONTROLLER_CATEGORY_RENAMED = EVENT_CONTROLLER_COMMON_BASE + 2;
    public static final int EVENT_CONTROLLER_CATEGORY_ADDED = EVENT_CONTROLLER_COMMON_BASE + 3;
    public static final int EVENT_CONTROLLER_GOTO_POI = EVENT_CONTROLLER_COMMON_BASE + 4;
    public static final int EVENT_CONTROLLER_POI_REENTER = EVENT_CONTROLLER_COMMON_BASE + 5;
    public static final int EVENT_CONTROLLER_LOGIN_FINISH = EVENT_CONTROLLER_COMMON_BASE + 6;
    public static final int EVENT_CONTROLLER_SYNC_FINISH = EVENT_CONTROLLER_COMMON_BASE + 7;
    public static final int EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY = EVENT_CONTROLLER_COMMON_BASE + 8;
    public static final int EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY = EVENT_CONTROLLER_COMMON_BASE + 9;
    public static final int EVENT_CONTROLLER_GO_TO_MAP_SUMMARY = EVENT_CONTROLLER_COMMON_BASE + 10;
    public static final int EVENT_CONTROLLER_CONTACT_SELECTED = EVENT_CONTROLLER_COMMON_BASE + 11;
    public static final int EVENT_CONTROLLER_GO_TO_MOVING_MAP = EVENT_CONTROLLER_COMMON_BASE + 12;
    public static final int EVENT_CONTROLLER_GO_TO_TURN_MAP = EVENT_CONTROLLER_COMMON_BASE + 13;
    public static final int EVENT_CONTROLLER_ADDRESS_VALIDATED = EVENT_CONTROLLER_COMMON_BASE + 14;
    
    /**
     * Back to SummaryControlController and let SummaryControlController determine the to coming logic
     */
    public static final int EVENT_CONTROLLER_BACK_TO_SUMMARY_CONTROLLER = EVENT_CONTROLLER_COMMON_BASE + 15;
    public static final int EVENT_CONTROLLER_REQUEST_ROUTE = EVENT_CONTROLLER_COMMON_BASE + 16;
	public static final int EVENT_CONTROLLER_CONTEXT_MENU_CLICKED = EVENT_CONTROLLER_COMMON_BASE + 17;
	public static final int EVENT_CONTROLLER_BACK_TO_MOVING_MAP = EVENT_CONTROLLER_COMMON_BASE + 18;
	public static final int EVENT_CONTROLLER_DELETE_FAVORITE = EVENT_CONTROLLER_COMMON_BASE + 19;
	public static final int EVENT_CONTROLLER_DELETE_CATEGORY = EVENT_CONTROLLER_COMMON_BASE + 20;
	public static final int EVENT_CONTROLLER_RENAME_CATEGORY = EVENT_CONTROLLER_COMMON_BASE + 21;
	public static final int EVENT_CONTROLLER_MAP_TO_POI_LIST = EVENT_CONTROLLER_COMMON_BASE + 22;
	public static final int EVENT_CONTROLLER_MAP_TO_POI_DETAIL = EVENT_CONTROLLER_COMMON_BASE + 23;
	public static final int EVENT_CONTROLLER_DSR_SEARCH = EVENT_CONTROLLER_COMMON_BASE + 24;
	public static final int EVENT_CONTROLLER_DSR_MAP = EVENT_CONTROLLER_COMMON_BASE + 25;
	public static final int EVENT_CONTROLLER_DSR_NAV = EVENT_CONTROLLER_COMMON_BASE + 26;
	public static final int EVENT_CONTROLLER_SHOW_POI_LIST = EVENT_CONTROLLER_COMMON_BASE + 27;
	
	//for common address selected
	public static final int EVENT_CONTROLLER_ADDRESS_SELECTED = EVENT_CONTROLLER_COMMON_BASE + 28;
	
	//for link event.
	public static final int EVENT_CONTROLLER_LINK_TO_MAP = EVENT_CONTROLLER_COMMON_BASE + 29;
	public static final int EVENT_CONTROLLER_LINK_TO_AC = EVENT_CONTROLLER_COMMON_BASE + 30;
	public static final int EVENT_CONTROLLER_LINK_TO_POI = EVENT_CONTROLLER_COMMON_BASE + 31;
	public static final int EVENT_CONTROLLER_LINK_TO_EXTRA = EVENT_CONTROLLER_COMMON_BASE + 32;
	/**
	 * Renew dynamic route. This event is thrown when we choose alternate route from traffic model.
	 */
	public static final int EVENT_CONTROLLER_RENEW_DYNAMIC_ROUTE = EVENT_CONTROLLER_COMMON_BASE + 33;
	
	/**
	 * use this event to start main screen.
	 */
	public static final int EVENT_CONTROLLER_GO_TO_MAIN = EVENT_CONTROLLER_COMMON_BASE + 34;
	
	/**
     * use this event go back and exit Maitai.
     */
    public static final int EVENT_CONTROLLER_EXIT_MAITAI = EVENT_CONTROLLER_COMMON_BASE + 35;
    
    public static final int EVENT_CONTROLLER_EXIT_NAV = EVENT_CONTROLLER_COMMON_BASE + 36;
    
    public static final int EVENT_CONTROLLER_RESUME_TRIP = EVENT_CONTROLLER_COMMON_BASE + 37;
    
    public static final int EVENT_CONTROLLER_STOP_SELECTED = EVENT_CONTROLLER_COMMON_BASE + 38;
    
    public static final int EVENT_CONTROLLER_FAVORITE_EDITED = EVENT_CONTROLLER_COMMON_BASE + 39;
    
    public static final int EVENT_CONTROLLER_GO_TO_ROUTE_SETTING = EVENT_CONTROLLER_COMMON_BASE + 40;
    
    public static final int EVENT_CONTROLLER_GO_TO_VOICE_SETTING = EVENT_CONTROLLER_COMMON_BASE + 41;
    
    public static final int EVENT_CONTROLLER_PREFERENCE_SAVED = EVENT_CONTROLLER_COMMON_BASE + 42;
    
    public static final int EVENT_CONTROLLER_UPSELL_PURCHASE_FINISH = EVENT_CONTROLLER_COMMON_BASE + 43;
  
    public static final int EVENT_CONTROLLER_RENEW_STATIC_ROUTE = EVENT_CONTROLLER_COMMON_BASE + 44;

    public static final int EVENT_CONTROLLER_BACK_TO_TURN_MAP = EVENT_CONTROLLER_COMMON_BASE + 45;
    
    //plugin
    public static final int EVENT_CONTROLLER_GOTO_PLUGIN = EVENT_CONTROLLER_COMMON_BASE + 46;
    
    public static final int EVENT_CONTROLLER_START_ROUTE_PLANNING_SETTING = EVENT_CONTROLLER_COMMON_BASE + 47;
    
    //for map back to detail
    public static final int EVENT_CONTROLLER_SHOW_POI_DETAIL = EVENT_CONTROLLER_COMMON_BASE + 48;
    
    public static final int EVENT_CONTROLLER_GO_TO_MAIN_PURCHASE_NAV = EVENT_CONTROLLER_COMMON_BASE + 49;
    
    public static final int EVENT_CONTROLLER_GO_TO_NAV = EVENT_CONTROLLER_COMMON_BASE + 50;
    
    public static final int EVENT_CONTROLLER_GO_TO_POI_MAP = EVENT_CONTROLLER_COMMON_BASE + 51;
    
    public static final int EVENT_CONTROLLER_FROM_PLUGIN_REQUEST = EVENT_CONTROLLER_COMMON_BASE + 52;
    
    // From DSR back to poi result but not start new poi result controller because there is already one in the controller chain
    public static final int EVENT_CONTROLLER_GO_BACK_TO_POI_RESULT = EVENT_CONTROLLER_COMMON_BASE + 53;
    
    //for back to Entry
    public static final int EVENT_CONTROLLER_BACK_TO_ENTRY = EVENT_CONTROLLER_COMMON_BASE + 54;
    
    public static final int EVENT_CONTROLLER_BACK_TO_DASHBOARD = EVENT_CONTROLLER_COMMON_BASE + 55;
    
    public static final int EVENT_CONTROLLER_GOTO_FTUE_SIGNUP = EVENT_CONTROLLER_COMMON_BASE + 56;
    
    public static final int EVENT_CONTROLLER_GOTO_FTUE_SIGNIN = EVENT_CONTROLLER_COMMON_BASE + 57;

    public static final int EVENT_CONTROLLER_BACK_TO_PREV = EVENT_CONTROLLER_COMMON_BASE + 58;
    
    public static final int EVENT_CONTROLLER_GOTO_FTUE_EDIT_ACCOUNT = EVENT_CONTROLLER_COMMON_BASE + 59;
    
    public static final int EVENT_CONTROLLER_GOTO_DASHBOARD_FROM_WIDGET_HOMEWORK = EVENT_CONTROLLER_COMMON_BASE + 60;
    
    //for About
    public static final int EVENT_CONTROLLER_GOTO_ABOUT = EVENT_CONTROLLER_COMMON_BASE + 61;

    public static final int EVENT_CONTROLLER_GOTO_SUPPORT = EVENT_CONTROLLER_COMMON_BASE + 62;
    
    public static final int EVENT_CONTROLLER_GOTO_TOS = EVENT_CONTROLLER_COMMON_BASE + 63;
    
    public static final int EVENT_CONTROLLER_GOTO_FORGET_PASSWORD = EVENT_CONTROLLER_COMMON_BASE + 64;

    public static final int EVENT_CONTROLLER_GOTO_UPSELL = EVENT_CONTROLLER_COMMON_BASE + 65;
   
    //for cancel subscripton
    public static final int EVENT_CONTROLLER_GOTO_UPSELL_CANCEL_SUBSCRIPTION = EVENT_CONTROLLER_COMMON_BASE + 66;
    public static final int EVENT_CONTROLLER_CANCEL_SUBSCRIPTION_SUCCESS = EVENT_CONTROLLER_COMMON_BASE + 67;

    public static final int EVENT_CONTROLLER_MAP_VBB = EVENT_CONTROLLER_COMMON_BASE + 68;
    public static final int EVENT_CONTROLLER_DRIVE_TO_VBB = EVENT_CONTROLLER_COMMON_BASE + 69;
    public static final int EVENT_CONTROLLER_BACK_FROM_VBB_DETAIL = EVENT_CONTROLLER_COMMON_BASE + 70;
    
    public static final int EVENT_CONTROLLER_GOTO_FTUE_MAIN = EVENT_CONTROLLER_COMMON_BASE + 71;
    
    public static final int EVENT_CONTROLLER_NATIVE_SHARE_FINISH = EVENT_CONTROLLER_COMMON_BASE + 74;
    
    public static final int EVENT_CONTROLLER_NATIVE_SHARE_CANCEL = EVENT_CONTROLLER_COMMON_BASE + 75;
    
    public static final int EVENT_CONTROLLER_PLACE_ADDED = EVENT_CONTROLLER_COMMON_BASE + 76;
    
    public static final int EVENT_CONTROLLER_PLACE_EDIT = EVENT_CONTROLLER_COMMON_BASE + 77;
    
    public static final int EVENT_CONTROLLER_BACK_TO_ONEBOX_SEARCH = EVENT_CONTROLLER_COMMON_BASE + 78;

    public static final int EVENT_CONTROLLER_BACK_TO_PREFERENCE = EVENT_CONTROLLER_COMMON_BASE + 79;

    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    
    // An int variable to store the type
    public static final Integer KEY_I_AC_TYPE            = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 1);
    
    // Selected address
    public static final Integer KEY_O_SELECTED_ADDRESS   = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 2);
    
    // Operated addresses list
    public static final Integer KEY_O_OPERATED_ADDRESS_LIST   = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 4);
    
    //Search along Type
    public static final Integer KEY_I_SEARCH_ALONG_TYPE  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 5);
    
    //Searching directly, add for maitai
    public static final Integer KEY_B_IS_SEARCHING_DIRECTLY = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 6);
    
    // Search center gps Point, store the lat, lon values
    public static final Integer KEY_O_SEARCH_CENTER  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 7);
    
    // Selected Index
    public static final Integer KEY_I_POI_SELECTED_INDEX = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 8);
    
    // Search type
    public static final Integer KEY_I_SEARCH_TYPE       = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 9);
    
    // Search from type
    public static final Integer KEY_I_SEARCH_FROM_TYPE  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 10);
    
    // Sort type
    public static final Integer KEY_I_SEARCH_SORT_TYPE  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 11);
    
    // Search Text 
    public static final Integer KEY_S_COMMON_SEARCH_TEXT = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 12);
    
    // Search ShowString
    public static final Integer KEY_S_COMMON_SHOW_SEARCH_TEXT = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 13);
    
    // Search ORI
    public static final Integer KEY_O_ADDRESS_ORI = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 14);
    
    // Search Dest
    public static final Integer KEY_O_ADDRESS_DEST = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 15);
    
    // Search category id
    public static final Integer KEY_I_CATEGORY_ID = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 16);
    
    // Search navState
    public static final Integer KEY_O_NAVSTATE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 17);
    
    public static final Integer KEY_I_ROUTE_ID = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 18);
    
    //Home stop
    public static final Integer KEY_O_HOME_STOP = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 19);
    
    //Common error message
    public static final Integer KEY_S_ERROR_MESSAGE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 21);
    
    //Common messagebox message
    public static final Integer KEY_S_COMMON_MESSAGE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 22);
    
    //Contact selected
    public static final Integer KEY_O_CONTACT = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 23);
    
    //Category operated
    public static final Integer KEY_O_CATEGORY = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 24);
    
    //sync type
    public static final Integer KEY_I_SYNC_TYPE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 25);
    
    // The ITrafficCallback object
    public static final Integer KEY_O_TRAFFIC_CALLBACK = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 27);
    
    // The route style used for navigation
    public static final Integer KEY_I_ROUTE_STYLE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 28);
    
    // The stop audio used for navigation
    public static final Integer KEY_V_STOP_AUDIOS = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 29);

    public static final Integer KEY_I_CURRENT_SEGMENT_INDEX = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 30);
    
    //First line of address to be validate
    public static final Integer KEY_S_ADDRESS_LINE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 31);
    
    //Second line of address to be validate
    public static final Integer KEY_S_SECOND_LINE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 32);
    
    //Validated address
    public static final Integer KEY_O_VALIDATED_ADDRESS = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 33);
    
    //Shared address
    public static final Integer KEY_O_SHARED_ADDRESS = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 34);
    
    public static final Integer KEY_O_LAST_CONTROLLER = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 35);
    
    public static final Integer KEY_O_CURRENT_CONTROLLER = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 36);
    
    public static final Integer KEY_B_IS_FROM_SEARCH_ALONG = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 37);
    
    public static final Integer KEY_I_TYPE_MAP_FROM = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 38);
    
    public static final Integer KEY_I_TYPE_ADDRESS_VALIDATOR_FROM = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 39);
    
    public static final Integer KEY_O_POI_DATA_WRAPPER = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 42);
    
    //Browser sdk
    public static final Integer KEY_O_BROWSER_SESSION_ARGS = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 43);
    
    public static final Integer KEY_I_ADDRESS_TYPE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 44);
    //mark the flow is choosing location flow. only use for choosing location. 
    public static final Integer KEY_B_IS_CHOOSING_LOCATION = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 45);
    
    public static final Integer KEY_S_ADDED_CATEGORY = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 46);
    
    public static final Integer KEY_V_ALTERNATIVE_ADDRESSES = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 47);
    
    //selected stop
    public static final Integer KEY_O_SELECTED_STOP = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 48);
    
    //Is current location needed
    public static final Integer KEY_B_IS_CURRENT_LOCATION_NEEDED = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 49);
    
    public static final Integer KEY_S_FEATURE_CODE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 50);
    
    public static final Integer KEY_S_APP_CODE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 51);
    
    public static final Integer KEY_S_OFFER_CODE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 52);

    //Plugin sdk
    public static final Integer KEY_O_PLUGIN_REQUEST = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 53);
    
    public static final Integer KEY_O_CURRENT_LOCATION = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 54);
    
    public static final Integer KEY_B_IS_REGISTRATE_SUCC  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 55);
    
    
    public static final Integer KEY_B_IS_START_UP  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 56);
    
    public static final Integer KEY_B_IS_RESUME_TRIP = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 57);
    
    /**
     * Ignore route planning go to moving map screen directly or not.
     */
    public static final Integer KEY_B_IS_IGNORE_ROUTE_PLANNING = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 58);

    //Transaction id for auto suggest and onebox search
    public final static Integer KEY_S_TRANSACTION_ID = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 59);
    
    public final static Integer KEY_S_CHANGED_LANGUAGE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 60);
    
    public final static Integer KEY_I_CHANGED_LANGUAGE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 61);
    
    public static final Integer KEY_B_NO_NEED_UPDATE_SCROLL = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 62);
    
    public static final Integer KEY_B_IS_INTERSECTION = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 63);
    
    public static final Integer KEY_I_INPUT_TYPE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 64);
    
    public static final Integer KEY_B_IS_NEED_REFRESH = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 65);
    
    public static final Integer KEY_B_IS_NEED_PAUSE_LATER = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 66);
    
    public static final Integer KEY_S_CURRENT_REGION = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 67);
    
    public final static Integer KEY_S_MESSAGE_ADDRESS  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 70);
    
    public final static Integer KEY_O_USER_PROFILE_PROVIDER  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 71);
    
    //added for Ford Applink
    public final static Integer KEY_B_IS_CAR_CONNECT_MODE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 72);
    
    public final static Integer KEY_B_IS_FROM_ONE_BOX = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 74);
    
    //added for Ford Applink
    public final static Integer KEY_B_IS_NAV_PAUSED = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 75);
    
    public final static Integer KEY_S_ADDRESS_VALIDATE_COUNTRY = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 76);
        
    public static final Integer KEY_B_ROUTE_SETTING_CHANGED  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 77);
    
    public static final Integer KEY_B_IS_SEARCH_NEAR_BY  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 78);

    public static final Integer KEY_B_IS_DSR_FROM_POI_MAP = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 79);
   
    //added for feedback
    public static final Integer KEY_I_FEEDBACK_TYPE  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 80);
    
    public static final Integer KEY_S_FEEDBACK_ABOUT  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 81);
       
    public static final Integer KEY_B_IS_START_BY_OTHER_CONTROLLER = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 82);
    
    public static final Integer KEY_I_NEW_ROUTE_ID = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 83);
    
    public static final Integer KEY_B_IS_SYNCING    = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 84);
    
    // onboard/offboard switch
    public static final Integer KEY_B_IS_ONBOARD    = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 85);
	
    public static final Integer KEY_B_NEED_RESET_ZOOMLEVEL  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 86);

    
    public static final Integer KEY_B_IS_CANCEL_SUBSCRIPTION_SUCC  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 87);
    
    public static final Integer KEY_O_AD_ADDRESS = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 88);
    
    public static final Integer KEY_S_AD_DETAIL_URL = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 89);
    
    public static final Integer KEY_B_IS_NAV_SESSION = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 90);
    
    public static final Integer KEY_B_IS_CREATE_ACCOUNT = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 91);
    
    public static final Integer KEY_B_REFRESH_LIST = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 92);
    
    public static final Integer KEY_B_IS_MODULE_READY_TO_SHOW_MAP_NOTIFICATION = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 93);
    
    public static final Integer KEY_I_PLACE_OPERATION_TYPE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 94);
    
    public static final Integer KEY_I_TYPE_DETAIL_FROM = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 95);
    
    public static final Integer KEY_B_FORBID_LIST_HITBOTTOM = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 96);
    
    public static final Integer KEY_B_IS_SEARCH_FROM_ONEBOX = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 97);
    
    public static final Integer KEY_IS_START_APP = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 98);
    
    public static final Integer KEY_O_CURRENT_ADDRESS = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 99);
    
    public static final Integer KEY_S_LOCAL_EVENT_ID = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 100);
    
    public static final Integer KEY_S_DEST_TYPE = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 101);
    
    public static final Integer KEY_S_TINY_URL = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 102);
    
    public static final Integer KEY_S_TINY_URL_ETA = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 103);

    public static final Integer KEY_B_IS_FROM_DSR = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 104);
    
    public static final Integer KEY_I_LOGIN_FROM = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 105);
    
    public static final Integer KEY_S_LOCAL_EVENT_NAME = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 106);
    
    public static final Integer KEY_B_IS_IGNORE_END_DETOUR = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 107);
    
    public static final Integer KEY_B_NEED_CURRENT_LOCATION = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 108);
    
    public static final Integer KEY_S_DWF_SESSION_ID = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 109);
    
    public static final Integer KEY_S_DWF_USER_KEY = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 110);
    
    public static final Integer KEY_S_DWF_USER_ID = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 111);
    
    public static final Integer KEY_B_DWF_LAUNCH_APP = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 112);
    
    public static final Integer KEY_B_NEED_SHARE_ETA = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 113);

    public static final Integer KEY_B_IS_FROM_MAP  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 114);
    
    public static final Integer KEY_B_IS_FROM_DWF  = PrimitiveTypeCache.valueOf(STATE_COMMON_BASE + 115);
    
    // select place type
    public static final int PLACE_OPERATION_TYPE_ADD = 1;
    
    public static final int PLACE_OPERATION_TYPE_ADD_TO_FOLDER = 2;
    
    public static final int PLACE_OPERATION_TYPE_EDIT = 3;
    
    public static final int PLACE_OPERATION_TYPE_SHARE = 4;

    
    // feedback type
    public static final int TYPE_FEEDBACK_GENERAL = 0;

    public static final int TYPE_FEEDBACK_SEARCH = 1;

    public static final int TYPE_FEEDBACK_POI = 2;
    
    // ------------------------------------------------------------
    // Ac type definition
    // ------------------------------------------------------------
    public static final int TYPE_AC_FROM_NAV = 0;
    public static final int TYPE_AC_FROM_POI = 1;
    public static final int TYPE_AC_FROM_MAP = 2;
    public static final int TYPE_AC_FROM_SHARE = 3;
    public static final int TYPE_AC_FROM_EDIT = 4;
    public static final int TYPE_AC_FROM_FAV = 5;
    public static final int TYPE_AC_FROM_CONTACT = 6;
    public static final int TYPE_AC_FROM_TRIP_CONFIRMATION = 7;
    public static final int TYPE_AC_FROM_DSR = 8;
    public static final int TYPE_AC_FROM_BROWSER = 9;
    public static final int TYPE_AC_FROM_SHARE_AND_MORE = 10;
    public static final int TYPE_AC_FROM_ADDRESS_SEARCH = 11;
    public static final int TYPE_AC_FROM_POI_LIST = 12;
    public static final int TYPE_AC_FROM_TURN_MAP = 13;
    public static final int TYPE_AC_FROM_ROUTE_PLAN = 14;
    public static final int TYPE_AC_FROM_ONE_BOX = 15;
    public static final int TYPE_AC_FROM_CHANGE_LOCATION = 16;
    public static final int TYPE_AC_FROM_AC_MAIN = 17;
    public static final int TYPE_AC_FROM_MAP_ONEBOX_SEARCH = 18;
    public static final int TYPE_AC_FROM_ENTRY = 19;
    public static final int TYPE_AC_FROM_DASHBOARD = 20;
    
    // ------------------------------------------------------------
    // Login type definition
    // ------------------------------------------------------------
    public static final int TYPE_LOGIN_FROM_ENTRY = 0;
    public static final int TYPE_LOGIN_FROM_DASHBOARD = 1;
    public static final int TYPE_LOGIN_FROM_PREFERENCE = 2;
    public static final int TYPE_LOGIN_FROM_FAVORITE = 3;
    public static final int TYPE_LOGIN_FROM_RECENT = 4;
    
    // ------------------------------------------------------------
    // Address type definition
    // ------------------------------------------------------------
    public static final int TYPE_HOME_ADDRESS = 0;
    public static final int TYPE_WORK_ADDRESS = 1;
    public static final int TYPE_OTHER_ADDRESS = 2;
    
    //Map Type From
    public static final int TYPE_MAP_FROM_ENTRY = 0;
    public static final int TYPE_MAP_FROM_POI = 1;
    public static final int TYPE_MAP_FROM_MAITAI_POI = 2;
    public static final int TYPE_MAP_FROM_BROWSER = 3;
    public static final int TYPE_MAP_FROM_FAVORITE = 4;
    public static final int TYPE_MAP_FROM_RECENT = 5;
    public static final int TYPE_MAP_FROM_SUMMARY = 6;
    public static final int TYPE_MAP_FROM_AC = 7;
    public static final int TYPE_MAP_FROM_ADDRESS = 8;
    public static final int TYPE_MAP_FROM_ONEBOX_POI = 9;
    public static final int TYPE_MAP_FROM_BILLBOARD = 10;
    public static final int TYPE_MAP_FROM_DSR = 11;
    public static final int TYPE_MAP_FROM_SPECIFIC_POI = 12;
    
    //Detail Type From
    public static final int TYPE_DETAIL_FROM_POI = 0;
    public static final int TYPE_DETAIL_FROM_RECENT = 1;
    public static final int TYPE_DETAIL_FROM_FAVORITE = 2;
    public static final int TYPE_DETAIL_FROM_CONTACT = 3;
    
    //sync res type
    public static final int TYPE_FRESH_SYNC = 0;
    public static final int TYPE_STARTUP_SYNC = 1;
    public static final int TYPE_EXIT_SYNC = 2;
    public static final int TYPE_SYNC_FINISH = 3;
    //one box using type
    public static final int TYPE_FETCH_STOP = 0;
    public static final int TYPE_SHOW_DETAIL = 1;
    
    //from Maitai
    public static final int TYPE_FROM_MAITAI = 100001;
    public static final int TYPE_FROM_CONTACTS = 100002;
    public static final int TYPE_FROM_POI = 100003;
    //-----------------------------------------------------------
    // constants path string value of poi 
    //-----------------------------------------------------------
    public static final int PATH_SEARCH = 1;
    public static final int PATH_LIST   = 2;
    public static final int PATH_REVIEW = 3;
    
    //user ptn length
    public static final int PTN_TYPEIN_LENGTH = 10;
    public static final int PTN_TYPEIN_LENGTH_CN = 11;
    
    public static final int PASSWORD_MAX_LENGTH = 128;
    public static final int PASSWORD_MIN_LENGTH = 4;
    
    //-------------------------------------------------------------
    // Search result type
    //-------------------------------------------------------------
    public static final int TYPE_POI_RESULT = 0;
    public static final int TYPE_ADDRESS_RESULT = 1;
    public static final int TYPE_SUGGEST_RESULT = 2;
    
    //Gps Select Index
    public static final int GPS_BLUETOOTH_PUSH = 0;
    public static final int GPS_JSR179   = 1;
    public static final int GPS_ALONG_ROUTE = 2;
    public static final int GPS_MVIEWER_TOOL = 3;
    public static final int GPS_REPLAY = 4;
    
    //Onboard Mode Select Index
    public static final int DATASOURCE_MODE_AUTO = 0;
    public static final int DATASOURCE_MODE_ONBOARD = 1;
    public static final int DATASOURCE_MODE_OFFBOARD = 2;
    
    
    //FUTE launch type
    public static final int FUTE_LAUNCH_BY_MY_PROFILE_SIGNUP   = 1;
    public static final int FUTE_LAUNCH_BY_MY_PROFILE_SIGNIN = 2;
    
    // ------------------------------------------------------------
    // Module Base id definition
    // ------------------------------------------------------------
    public static final int USER_BASE_ENTRY             = 1000;
    public static final int USER_BASE_DSR               = 2000;
    public static final int USER_BASE_AC                = 3000;
    public static final int USER_BASE_PREFERENCE        = 4000;
    public static final int USER_BASE_SECRET_KEY        = 5000;
    public static final int USER_BASE_STATIC_MAP        = 6000;
    public static final int USER_BASE_FAV               = 7000;
    public static final int USER_BASE_POI_SEARCH        = 8000;
    public static final int USER_BASE_POI_RESULT        = 9000;
    public static final int USER_BASE_POI_REIVEW        = 10000;
    public static final int USER_BASE_UPSELL            = 11000;
    public static final int USER_BASE_SHARE             = 12000;
    public static final int USER_BASE_RECENT            = 13000;
    public static final int USER_BASE_ADD_CATEGORY      = 14000;
    public static final int USER_BASE_EDIT_CATEGORY     = 15000;
    public static final int USER_BASE_EDIT_FAVORITE     = 16000;
    public static final int USER_BASE_NAV               = 17000;
    public static final int USER_BASE_MOVING_MAP        = 18000;
    public static final int USER_BASE_ROUTE_CONFIRM     = 19000;
    public static final int USER_BASE_ROUTE_SUMMARY     = 20000;
    public static final int USER_BASE_TRAFFIC           = 21000;
    public static final int USER_BASE_POI               = 22000;
    public static final int USER_BASE_ROUTE_PLANNING    = 23000;
    public static final int USER_BASE_BROWSERSDK        = 24000;
    public static final int USER_BASE_LOGIN             = 25000;
    public static final int USER_BASE_MAP               = 26000;
    public static final int USER_BASE_FEEDBACK          = 27000;
    public static final int USER_BASE_SYNC_RES          = 28000;
    
    public static final int USER_BASE_AIRPORT           = 29000;
    public static final int USER_BASE_SET_HOME          = 30000;
    public static final int USER_BASE_ONE_BOX_SEARCH    = 31000;
    public static final int USER_BASE_CONTACTS          = 32000;
    public static final int USER_BASE_TURN_MAP          = 33000;
    public static final int USER_BASE_POI_DETAIL        = 34000;
    public static final int USER_BASE_ADDRESS_VALIDATOR = 35000;
    public static final int USER_BASE_SUMMARY_CONTROL   = 36000;
    public static final int USER_BASE_MAITAI            = 37000;
    public static final int USER_BASE_COMMON_ENTRY      = 38000;
    public static final int USER_BASE_ABOUT             = 39000;
    public static final int USER_BASE_ROUTE_SETTING     = 40000;
    public static final int USER_BASE_PLUGIN            = 41000;
    
    public static final int USER_BASE_SWITCHLANG        = 42000;
    
    public static final int USER_BASE_MAITAI_VIEW        = 43000;
	public static final int USER_BASE_CITIES        = 44000;
    public static final int USER_BASE_ADDRESS        = 45000;
    public static final int USER_BASE_ONEKEYCALL        = 46000;
    
    public static final int USER_BASE_DASHBOARD        = 47000;
    
    //added for CarConnect (FordApplink)
    public static final int USER_BASE_LOCKOUT = 48000;
    public static final int USER_BASE_CARCONNECTUPSELL = 49000;
    
    public static final int USER_BASE_ATT_DASHBOARD        = 50000;

    public static final int USER_BASE_TEST_PROXY        = 51000;
    
    public static final int USER_BASE_VBB_DETAIL        = 52000;
    
    public static final int USER_BASE_MAP_DOWNLOAD        = 53000;
    
    public static final int USER_BASE_ADD_PLACE        = 54000;

    public static final int USER_BASE_NATIVE_SHARE     = 55000;
    
    public static final int USER_BASE_LOCAL_EVENT     = 56000;
    
    public static final int USER_BASE_DRIVING_SHARE_SETTING     = 57000;

    public static final int USER_BASE_DRIVE_WITH_FRIENDS     = 58000;
    
    // ------------------------------------------------------------
    // COMMON COMPONENT ID (used for search)
    // ------------------------------------------------------------
	public static final int ID_MAP_COMPONENT = 11111;
	public static final int ID_MAP_CONTAINER = 11112;
	public static final int ID_MAP_VIEW_LIST_COMPONENT = 11113;
	public static final int ID_QUICK_SEARCH_BAR = 11114;
	public static final int ID_MAP_PROVIDER = 11115;
	public static final int ID_MAP_COMPANY_LOGO = 11116;
    public static final int ID_ZOOM_IN = 11117;
    public static final int ID_ZOOM_OUT = 11118;
    public static final int ID_CURRENT_LOCATION = 11119;
    public static final int ID_MAP_ZOOM_CONTAINER = 11120;
    public static final int ID_TOAST_SLIDERBAL_CONTAINER = 11121;
    
	public static final long ANOTATION_COMPONENT_TRANSLATE = 100000;
	//default poi results number per page.
	public static final int MAX_NUM_PER_PAGE = 10;
	
	public static final int GETTING_GPS_DISPLAY_TIMEOUT = 8000; //Per suggest display 8s is enough
	
	public static final int CATEGORY_ID_GAS_REGULAR = 702;
	
	public static final int CATEGORY_ID_FOOD = 2041;
	
    // ------------------------------------------------------------
    // COMMON STATE TABLE definition
    // ------------------------------------------------------------
    public static int[][] STATE_TABLE_COMMON = new int[][]
    {
        { STATE_ANY, CMD_COMMON_BACK, STATE_PREV, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_COMMON_BACK, STATE_PREV, ACTION_NONE },
        { STATE_ANY, CMD_COMMON_PREFERENCE, STATE_COMMON_GOTO_SETTINGS, ACTION_NONE},
        { STATE_ANY, CMD_COMMON_PREFERENCE_ICON_ON_DASHBOARD, STATE_COMMON_GOTO_SETTINGS, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_POST_ERROR, STATE_COMMON_ERROR, ACTION_NONE },
        { STATE_ANY, CMD_COMMON_SWITCH_AUDIO, STATE_ANY, ACTION_SWITCH_AUDIO},
        { STATE_ANY, CMD_COMMON_GOTO_ONEBOX, STATE_COMMON_GOTO_ONEBOX, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_SHOW_TIMEOUT_MESSAGE, STATE_COMMON_TIMEOUT_MESSAGEBOX, ACTION_NONE},
        { STATE_ANY, CMD_COMMON_ABOUT, STATE_COMMON_GOTO_ABOUT, ACTION_NONE},
        { STATE_ANY, CMD_COMMON_HOME, STATE_COMMON_GOTO_HOME, ACTION_NONE},
        
        { STATE_ANY, CMD_GENERAL_FEEDBACK, STATE_GENERAL_FEEDBACK, ACTION_NONE },
        { STATE_GENERAL_FEEDBACK, EVENT_MODEL_EXIT_BROWSER, STATE_PREV, ACTION_NONE },
        { STATE_ANY, CMD_COMMON_EXIT, STATE_COMMON_EXIT_NAV_CONFIRM, ACTION_NONE},
        { STATE_ANY, CMD_COMMON_SHARE, STATE_COMMON_SHARE_ADDRESS, ACTION_NONE},
        { STATE_COMMON_ERROR, CMD_COMMON_OK, STATE_PREV, ACTION_NONE},
        { STATE_COMMON_TIMEOUT_MESSAGEBOX, CMD_COMMON_OK, STATE_PREV, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_BACK_TO_MOVING_MAP, STATE_COMMON_BACK_TO_MOVING_MAP, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_BACK_TO_ENTRY, STATE_COMMON_BACK_TO_ENTRY, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_EXIT_NAV_FROM_PLUGIN, STATE_COMMON_EXIT_NAV_FROM_PLUGIN, ACTION_NONE},
        
        
        //Drive with Friends.
        { STATE_ANY, EVENT_MODEL_DWF_FROM_PLUGIN, STATE_COMMON_DWF_CHECK_DATA, ACTION_DWF_CHECK_DATA},
        { STATE_COMMON_DWF_CHECK_DATA, EVENT_MODEL_DWF_LAUNCH_SESSION, STATE_COMMON_DWF_FROM_PLUGIN, ACTION_NONE},
        { STATE_COMMON_DWF_CHECK_DATA, EVENT_MODEL_DWF_RESOLVE_TINY_URL, STATE_COMMON_DWF_RESOLVE_TINY_URL, ACTION_DWF_RESOLVE_URL},
        { STATE_COMMON_DWF_RESOLVE_TINY_URL, EVENT_MODEL_DWF_LAUNCH_SESSION, STATE_COMMON_DWF_FROM_PLUGIN, ACTION_NONE},
        { STATE_COMMON_DWF_RESOLVE_TINY_URL, EVENT_MODEL_DWF_PARAMETER_ERROR, STATE_COMMON_DWF_PARAMETER_ERROR, ACTION_NONE},
        { STATE_COMMON_DWF_PARAMETER_ERROR, CMD_COMMON_OK, STATE_COMMON_EXIT_NAV_FROM_SHORTCUT, ACTION_NONE},
        { STATE_COMMON_DWF_PARAMETER_ERROR, CMD_COMMON_BACK, STATE_COMMON_EXIT_NAV_FROM_SHORTCUT, ACTION_NONE},
        
        { STATE_ANY, EVENT_MODEL_EXIT_NAV_FROM_SHORTCUT, STATE_COMMON_EXIT_NAV_FROM_SHORTCUT, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_MOVING_MAP, STATE_COMMON_BACK_TO_MOVING_MAP, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_ENTRY, STATE_COMMON_BACK_TO_ENTRY, ACTION_NONE},
        
        //TODO: for dsr handle, should add more common event.
        //{ STATE_ANY, EVENT_CONTROLLER_DSR_MAP, STATE_COMMON_DSR_MAP, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_DSR_NAV, STATE_COMMON_DSR_NAV, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_DSR_SEARCH, STATE_COMMON_DSR_SEARCH, ACTION_NONE},
        
        //for link jump
        { STATE_ANY, CMD_COMMON_LINK_TO_MAP, STATE_COMMON_LINK_TO_MAP, ACTION_NONE},
        { STATE_ANY, CMD_COMMON_LINK_TO_AC, STATE_COMMON_LINK_TO_AC, ACTION_NONE},
        { STATE_ANY, CMD_COMMON_LINK_TO_SEARCH, STATE_COMMON_LINK_TO_SEARCH, ACTION_NONE},
        //disable it, beacuse extra page is going to be replaced by 'My Profile'
        //{ STATE_ANY, CMD_COMMON_LINK_TO_EXTRAS, STATE_COMMON_LINK_TO_EXTRA, ACTION_NONE},
        { STATE_ANY, CMD_COMMON_DSR, STATE_COMMON_DSR, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_UPSELL_PURCHASE_FINISH, STATE_PREV, ACTION_NONE},
        
        //for shortcut icon
        { STATE_ANY, EVENT_MODEL_SHORTCUT_TO_MAP, STATE_COMMON_LINK_TO_MAP, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_SHORTCUT_TO_PLACE, STATE_COMMON_LINK_TO_SEARCH, ACTION_NONE},
        
         //for region switch
        { STATE_ANY,                EVENT_MODEL_SWITCHING_REGION,       STATE_SWITCHING_REGION,     ACTION_NONE },   
        { STATE_SWITCHING_REGION,   CMD_COMMON_BACK,                    STATE_SWITCHING_REGION,     ACTION_NONE },     
        { STATE_ANY,   EVENT_MODEL_DETECTING_REGION,                    STATE_DETECTING_REGION,     ACTION_NONE },     
        { STATE_DETECTING_REGION,   CMD_COMMON_BACK,                    STATE_DETECTING_REGION,     ACTION_NONE },   
        { STATE_ANY, CMD_SECRET_KEY, STATE_LAUNCH_SECRET_FUNCTIONS, ACTION_NONE },
        
        { STATE_ANY, CMD_COMMON_LOGIN, STATE_GO_TO_SIGN_IN, ACTION_NONE},
        
        { STATE_ANY, CMD_COMMON_CREATE_ACCOUNT, STATE_GO_TO_SIGN_UP, ACTION_NONE},
        
        { STATE_ANY, CMD_COMMON_END_TRIP, STATE_COMMON_EXIT_NAV_CONFIRM, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_EXIT_NAV_CONFIRM, STATE_COMMON_EXIT_NAV_CONFIRM, ACTION_IGNORE_END_DETOUR},
        { STATE_COMMON_EXIT_NAV_CONFIRM, CMD_COMMON_OK, STATE_CHECK_EXIT_NAV, ACTION_EXIT_NAV},
        { STATE_CHECK_EXIT_NAV, EVENT_MODEL_COMMON_EXIT_NAV , STATE_EXIT_NAV, ACTION_NONE},
        
        { STATE_COMMON_EXIT_NAV_CONFIRM, CMD_COMMON_BACK, STATE_PREV, ACTION_RESUME_NAV},
        { STATE_COMMON_EXIT_NAV_CONFIRM, CMD_COMMON_END_DETOUR, STATE_END_DETOUR, ACTION_NONE},
        { STATE_COMMON_EXIT_NAV_CONFIRM, EVENT_MODEL_HIDE_NAV_CONFIRM, STATE_PREV, ACTION_RESUME_NAV},
        
        { STATE_ANY, EVENT_MODEL_MAP_DOWNLOADED_STATUS_CHANGED, STATE_ANY, ACTION_CHECK_MODULE_READY },
        { STATE_ANY, EVENT_MODEL_SHOW_MAP_DOWNLOADED_STATUS_CHANGED_NOTIFICATION, STATE_MAP_DOWNLOADED_STATUS_CHANGED, ACTION_NONE },
        
        { STATE_ANY, EVENT_CONTROLLER_PLACE_ADDED, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
        { STATE_ANY, EVENT_CONTROLLER_PLACE_EDIT, STATE_ANY, ACTION_EDIT_PLACE_CHECK },
        
        { STATE_ANY, EVENT_MODEL_MAP_DOWNLOAD_ERROR, STATE_COMMON_MAP_DOWNLOAD_ERROR, ACTION_NONE },
        { STATE_COMMON_MAP_DOWNLOAD_ERROR, CMD_COMMON_OK, STATE_PREV, ACTION_NONE },
        { STATE_ANY, CMD_CONTACTS, STATE_GOTO_CONTACTS, ACTION_NONE },
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_PREV, STATE_PREV, ACTION_NONE },
    };
}

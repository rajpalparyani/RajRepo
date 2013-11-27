/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * INavConstants.java
 *
 */
package com.telenav.module.nav;

import com.telenav.mvc.ICommonConstants;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-2
 */
interface INavConstants extends ICommonConstants
{
    public static final int STATE_GO_TO_ROUTE_PLANNING = (STATE_USER_BASE + USER_BASE_NAV + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_GO_TO_MOVING_MAP = (STATE_USER_BASE + USER_BASE_NAV + 2) | MASK_STATE_TRANSIENT;
    public static final int STATE_GO_TO_TURN_MAP = (STATE_USER_BASE + USER_BASE_NAV + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_INIT = (STATE_USER_BASE + USER_BASE_NAV + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_GO_HOME = (STATE_USER_BASE + USER_BASE_NAV + 5) | MASK_STATE_TRANSIENT;
    public static final int STATE_GO_TO_ROUTE_PLANNING_SETTING = (STATE_USER_BASE + USER_BASE_NAV + 6) | MASK_STATE_TRANSIENT;
    
    //plugin
    public static final int STATE_GO_TO_PLUGIN = (STATE_USER_BASE + USER_BASE_NAV + 7) | MASK_STATE_TRANSIENT;
    
//    public static final int STATE_MAP_SCREEN = STATE_USER_BASE + USER_BASE_NAV + 2;

    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    //public static final int COMMAND_DRIVETO = STATE_COMMON_BASE + USER_BASE_NAV + 34;
    
    // [Heads Up] please don't use 100 - 120, it is reserved for dynamic menu items
    public static final int CMD_DYNAMIC_MENU_ITEM = CMD_USER_BASE + USER_BASE_NAV + 100;

    // ------------------------------------------------------------
    // Model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_START_MOVING_MAP = EVENT_MODEL_USER_BASE + USER_BASE_NAV + 1;
    public static final int EVENT_MODEL_START_ROUTE_PLANNING = EVENT_MODEL_USER_BASE + USER_BASE_NAV + 2;
    public static final int EVENT_MODEL_START_ROUTE_PLANNING_SETTING = EVENT_MODEL_USER_BASE + USER_BASE_NAV + 3;
    //Added for Ford Applink (CarConnect)
    public static final int EVENT_MODEL_START_CAR_CONNECT_NAV = EVENT_MODEL_USER_BASE + USER_BASE_NAV + 4;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_START_MOVING_MAP = ACTION_USER_BASE + USER_BASE_NAV + 1;
    public static final int ACTION_START_TURN_MAP = ACTION_USER_BASE + USER_BASE_NAV + 2;;
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_NAV + 3;
    //Added for Ford Applink (CarConnect)
    public static final int ACTION_LAUNCH_CAR_CONNECT_NAV = ACTION_USER_BASE + USER_BASE_NAV + 4;
    
    //TODO: do we need base here?
//    public static final int ID_MAP_BUTTON = 1000;
//    public static final int ID_DRIVE_TO_BUTTON = 1001;
//    public static final int ID_DSR_BUTTON = 1002;
//    public static final int ID_SEARCH_BUTTON = 1003;
//    public static final int ID_APP_BUTTON = 1004;

}

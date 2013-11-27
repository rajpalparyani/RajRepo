/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IDriveWithFriendsConstants.java
 *
 */
package com.telenav.module.dwf;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author fangquanm
 * @date Jul 1, 2013
 */
interface IDriveWithFriendsConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 1);

    public static final int STATE_NEW_DWF = STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 2;

    public static final int STATE_SELECT_FROM_CONTACT = MASK_STATE_TRANSIENT
            | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 3);

    public static final int STATE_CHECK_DRIVE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 4);

    public static final int STATE_REQUEST_SESSION_LIST = MASK_STATE_TRANSIENT
            | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 5);

    public static final int STATE_LAUNCH_ROUTE_PLAN = MASK_STATE_TRANSIENT
            | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 6);

    public static final int STATE_SESSION_LIST = STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 7;

    public static final int STATE_LEAVE_GROUP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 8);
    
    public static final int STATE_BACK_TO_MOVING_MAP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 9);
    
    public static final int STATE_CHECK_EXPIRATION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 10);
    
    public static final int STATE_START_MAIN = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 11);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------

    public static final int CMD_SELECT_FROM_CONTACTS = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 1;

    public static final int CMD_INVITE = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 2;

    public static final int CMD_SHOW_MENU = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 3;

    public static final int CMD_DROP_MAP = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 4;

    public static final int CMD_DRIVE = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 5;

    public static final int CMD_LEAVE_GROUP = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 6;

    public static final int CMD_INVITE_NEW = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 7;

    public static final int CMD_SHOW_TERMS = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 8;
    
    public static final int CMD_BACK_TO_MOVING_MAP = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 9;
    
    public static final int CMD_OK_EXPIRATION = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 10;
    
    public static final int CMD_START_MAIN = CMD_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 11;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 1;

    public static final int ACTION_SELECT_ADDRESS = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 2;

    public static final int ACTION_REQUEST_SESSION = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 3;

    public static final int ACTION_LAUNCH_SESSION_LIST = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 4;

    public static final int ACTION_REQUEST_SESSION_DETAIL = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 5;

    public static final int ACTION_LEAVE_GROUP = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 6;

    public static final int ACTION_INVITE_NEW = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 7;
    
    public static final int ACTION_FROM_PLUGIN = ACTION_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 8;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 1;

    public static final int EVENT_MODEL_LAUNCH_SESSION_LIST = EVENT_MODEL_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 3;

    public static final int EVENT_MODEL_UPDATE_SESSION_LIST = EVENT_MODEL_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 4;

    public static final int EVENT_MODEL_GET_SESSION_DETAIL = EVENT_MODEL_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 5;

    public static final int EVENT_MODEL_LAUNCH_ROUTE_PLAN = EVENT_MODEL_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 6;
    
    public static final int EVENT_CHECK_EXPIRATION = EVENT_MODEL_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 7;
    
    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // The key id definition
    // ------------------------------------------------------------
    public static Integer KEY_V_CONTACTS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 2);

    public static Integer KEY_S_SMS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 3);

    public static Integer KEY_V_FRIENDS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 4);

    public static Integer KEY_B_IS_FULL_MAP = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 5);

    public static Integer KEY_O_HOST = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 7);

    public static Integer KEY_B_INVITE_NEW = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 8);
    
    public static Integer KEY_B_FRIENDS_UPDATED = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 9);
    
    public static Integer KEY_B_DESTINATION_UPDATED = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_DRIVE_WITH_FRIENDS + 10);

    // ------------------------------------------------------------
    //
    // ------------------------------------------------------------
    final static int ID_TITLE_CONTAINER = 10;

    final static int ID_DRIVE_INFO_CONTAINER = 20;

    final static int ID_LIST_INFO_CONTAINER = 30;

    final static int ID_DROP_BUTTON = 40;
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAddFavoriteConstants.java
 *
 */
package com.telenav.module.ac.favorite.editcategory;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
interface IEditCategoryConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 2;

    public static final int STATE_DELETE_CATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 3);
    
    public static final int STATE_DELETE_CATEGORY_NOTIFICATION = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 4);
    
    public static final int STATE_CATEGORY_UPDATED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 5);
    
    public static final int STATE_UPDATE_CATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 6);
    
    public static final int STATE_CATEGORY_DELETED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 7);
    
    public static final int STATE_NEW_CATEGORY = STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 8;
    
    public static final int STATE_CHECK_CATEGORY_EXIST = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 9);
    
    public static final int STATE_ADD_CATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 10);

    public static final int STATE_CATEGORY_ADDED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 11);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_RENAME_CATEGORY = CMD_USER_BASE + USER_BASE_EDIT_CATEGORY + 1;

    public static final int CMD_DELETE_CATEGORY = CMD_USER_BASE + USER_BASE_EDIT_CATEGORY + 2;
    
    public static final int CMD_ADD_CATEGORY = CMD_USER_BASE + USER_BASE_EDIT_CATEGORY + 3;

    public static final int CMD_BACK_TO_ADD_CATEGORY = CMD_USER_BASE + USER_BASE_EDIT_CATEGORY + 4;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_EDIT_CATEGORY + 1;

    public static final int ACTION_RENAME_CATEGORY = ACTION_USER_BASE + USER_BASE_EDIT_CATEGORY + 2;

    public static final int ACTION_DELETE_CATEGORY = ACTION_USER_BASE + USER_BASE_EDIT_CATEGORY + 3;
    
    public static final int ACTION_CHECK_CATEGORY = ACTION_USER_BASE + USER_BASE_EDIT_CATEGORY + 4;
    
    public static final int ACTION_CHECK_CATEGORY_EXIST = ACTION_USER_BASE + USER_BASE_EDIT_CATEGORY + 5;

    public static final int ACTION_ADD_CATEGORY = ACTION_USER_BASE + USER_BASE_EDIT_CATEGORY + 6;

    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_EDIT_CATEGORY = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_CATEGORY + 1;
    
    public static final int EVENT_MODEL_RENAME_CATEGORY = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_CATEGORY + 2;
    
    public static final int EVENT_MODEL_ADD_CATEGORY = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_CATEGORY + 3;

    public static final int EVENT_MODEL_RETRY_ADD_CATEGORY = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_CATEGORY + 4;
    
    public static final int EVENT_MODEL_LAUNCH_NEW_CATEGORY = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_CATEGORY + 5;
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static Integer KEY_S_RENAMED_CATEGORY = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 1);
    
    // A String variable to store alert message
    public static Integer KEY_S_ALERT_MESSAGE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 2);

    // A String to save the name of a new category
    public static Integer KEY_S_NEW_CATEGROY_NAME = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_CATEGORY + 3);
    
    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------
    public static final int ID_EDIT_CATEGORY_TEXTFIELD = USER_BASE_EDIT_CATEGORY + 1;
    
    public static final int ID_ADD_CATEGORY_TEXTFIELD = USER_BASE_ADD_CATEGORY + 2;
    
    public static final int ID_ADD_CATEGORY_BUTTON = USER_BASE_ADD_CATEGORY + 3;
    
    public static final int ID_ADD_CATEGORY_CANCEL_BUTTON = USER_BASE_ADD_CATEGORY + 4;
    
}

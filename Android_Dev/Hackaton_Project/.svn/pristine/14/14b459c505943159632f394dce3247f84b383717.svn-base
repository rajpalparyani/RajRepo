/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAddFavoriteConstants.java
 *
 */
package com.telenav.module.ac.favorite.favoriteeditor;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
interface IFavoriteEditorConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
	public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 2;

    public static final int STATE_ADD_CATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 3);

    public static final int STATE_DELETE_FAVORITE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 4);
    
    public static final int STATE_FAVORITE_UPDATED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 5);
    
    public static final int STATE_CHECK_LABEL = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 6);
    
    public static final int STATE_FAVORITE_DELETED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 8);
    
    public static final int STATE_FAVORITE_EDITED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 9);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_NEW_CATEGORY = CMD_USER_BASE + USER_BASE_EDIT_FAVORITE + 1;

    public static final int CMD_DELETE_FAVORITE = CMD_USER_BASE + USER_BASE_EDIT_FAVORITE + 2;
    
    public static final int CMD_UPDATE_FAVORITE = CMD_USER_BASE + USER_BASE_EDIT_FAVORITE + 3;
    
    public static final int CMD_FAVORITE_EDITED = CMD_USER_BASE + USER_BASE_EDIT_FAVORITE + 4;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_EDIT_FAVORITE + 1;

    public static final int ACTION_CHECK_NAME_EXIST = ACTION_USER_BASE + USER_BASE_EDIT_FAVORITE + 2;

    public static final int ACTION_SAVE_FAVORITE = ACTION_USER_BASE + USER_BASE_EDIT_FAVORITE + 3;
    
    public static final int ACTION_DELETE_FAVORITE = ACTION_USER_BASE + USER_BASE_EDIT_FAVORITE + 4;
    
    public static final int ACTION_SET_SELECTED_CATEGORY = ACTION_USER_BASE + USER_BASE_EDIT_FAVORITE + 5;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_FAVORITE + 1;

    public static final int EVENT_MODEL_SAVE_FAVORITE = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_FAVORITE + 2;
    
    public static final int EVENT_MODEL_SET_SELECTED_CATEGORY = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_FAVORITE + 4;
    
    public static final int EVENT_MODEL_NOT_SAVE_FAVORITE = EVENT_MODEL_USER_BASE + USER_BASE_EDIT_FAVORITE + 7;
    
    // ------------------------------------------------------------
    // Component id definition
    // ------------------------------------------------------------
    public static final int ID_EDIT_FAVORITE_TEXTFIELD = USER_BASE_EDIT_FAVORITE + 1;
    
    public static final int ID_EDIT_FAVORITE_MULTIBOX = USER_BASE_EDIT_FAVORITE + 2;
    
    public static final int ID_EDIT_FAVORITE_PANEL = USER_BASE_EDIT_FAVORITE + 3;
    
    public static final int ID_NEW_FAVORITE_TEXTFIELD = USER_BASE_EDIT_FAVORITE + 4;
    
    public static final int ID_ADD_FAVORITE_ITEM = USER_BASE_EDIT_FAVORITE + 5;
    
    public static final int ID_DELETE_FAVORITE_ITEM = USER_BASE_EDIT_FAVORITE + 6;
    
    public static final int ID_ADD_BUTTON = USER_BASE_EDIT_FAVORITE + 7;
    
    public static final int ID_EDIT_FAVORITE_ADDRESS_CONTAINER = USER_BASE_EDIT_FAVORITE + 8;
    
    public static final int ID_CATEGORIZE_FAVORITE_PANEL_CONTAINER = USER_BASE_EDIT_FAVORITE + 9;
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    // A vector to save the categories selected by user
    public static Integer KEY_V_SAVED_CATEGORIES = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 1);

    // A string to save the filter text
    public static Integer KEY_S_FAV_NAME = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 2);
    
    public static Integer KEY_S_NEW_CATEGORY = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 3);
    
    public static Integer KEY_V_CATEGORIES = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 4);
    
    public static Integer KEY_S_OLD_CATEGORY = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_EDIT_FAVORITE + 6);
    
}

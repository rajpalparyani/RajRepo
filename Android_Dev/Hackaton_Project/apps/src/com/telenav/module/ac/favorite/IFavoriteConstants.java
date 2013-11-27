/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IFavConstants.java
 *
 */
package com.telenav.module.ac.favorite;

import com.telenav.app.android.scout_us.R;
import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-26
 */
interface IFavoriteConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------

    public static final int STATE_INIT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 1);

    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_FAV + 2;
    
    public static final int STATE_GOTO_NAV = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 3);

    public static final int STATE_EDIT_CATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 4);

    public static final int STATE_EDIT_FAVORITE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 5);

    public static final int STATE_DELETE_FAVORITE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 6);

    public static final int STATE_ADD_CATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 7);

    public static final int STATE_CHECK_NAME_EXIST = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 8);

    public static final int STATE_SUBCATEGORY = STATE_USER_BASE + USER_BASE_FAV + 9;

    public static final int STATE_EDIT_SUBCATEGORY = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 11);

    public static final int STATE_LABEL_ALREADY_EXISTS = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 12);
    
    public static final int STATE_ADD_FAVORITE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 13);
    
    public static final int STATE_GOTO_MAP = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 14);
    
    public static final int STATE_DELETE_CATEGORY_BY_MENU = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 15);
    
    public static final int STATE_RENAME_CATEGORY_BY_MENU = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 16);
    
    public static final int STATE_ADDRESSES_SELECTED = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 17);
    
    public static final int STATE_MAIN_EDIT_MODE = STATE_USER_BASE + USER_BASE_FAV + 18;
    
    public static final int STATE_SUBCATEGORY_EDIT_MODE = STATE_USER_BASE + USER_BASE_FAV + 19;
    
    public static final int STATE_CALL_FAVORITE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 20);
    
    public static final int STATE_SORT_SELECT = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_FAV + 21);
    
    public static final int STATE_ADDRESS_SELECTED = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_FAV + 22);
    
    public static final int STATE_ADD_TO_FOLDER = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_FAV + 23);
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_NEXT_CATEGORY = CMD_USER_BASE + USER_BASE_FAV + 1;

    public static final int CMD_FAVORITE_SELECTED = CMD_USER_BASE + USER_BASE_FAV + 2;

    public static final int CMD_DELETE_FAVORITE = CMD_USER_BASE + USER_BASE_FAV + 3;

    public static final int CMD_SAVE_FAVORITE = CMD_USER_BASE + USER_BASE_FAV + 4;

    public static final int CMD_NEW_CATEGORY = CMD_USER_BASE + USER_BASE_FAV + 6;

    public static final int CMD_EDIT_CATEGORY = CMD_USER_BASE + USER_BASE_FAV + 8;

    public static final int CMD_SYNC = CMD_USER_BASE + USER_BASE_FAV + 9;

    public static final int CMD_SEARCH_FAVORITE = CMD_USER_BASE + USER_BASE_FAV + 10;

    public static final int CMD_ADD_FAVORITE = CMD_USER_BASE + USER_BASE_FAV + 11;

    public static final int CMD_BACK_TO_ADD_CATEGORY = CMD_USER_BASE + USER_BASE_FAV + 12;
    
    public static final int CMD_RENAME_CATEGORY = CMD_USER_BASE + USER_BASE_FAV + 13;
    
    public static final int CMD_DELETE_CATEGORY = CMD_USER_BASE + USER_BASE_FAV + 14;
    
    public static final int CMD_DRIVETO_ADDRESS = CMD_USER_BASE + USER_BASE_FAV + 15;
    
    public static final int CMD_MAP_ADDRESS = CMD_USER_BASE + USER_BASE_FAV + 16;
    
    public static final int CMD_ENTER_EDIT_MODE = CMD_USER_BASE + USER_BASE_FAV + 17;
    
    public static final int CMD_QUIT_EDIT_MODE = CMD_USER_BASE + USER_BASE_FAV + 18;
    
    public static final int CMD_SORT = CMD_USER_BASE + USER_BASE_FAV + 19;
    
    public static final int CMD_SORT_BY_ALPHABET = CMD_USER_BASE + USER_BASE_FAV + 20;
    
    public static final int CMD_SORT_BY_DATE = CMD_USER_BASE + USER_BASE_FAV + 21;
    
    public static final int CMD_SORT_BY_DISTANCE = CMD_USER_BASE + USER_BASE_FAV + 22;
    
    public static final int CMD_SELECT_ADDRESS = CMD_USER_BASE + USER_BASE_FAV + 23;
    
    public static final int CMD_ADD_TO_FOLDER = CMD_USER_BASE + USER_BASE_FAV + 24;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_FAV + 1;

    public static final int ACTION_CHECK_NAME_EXIST = ACTION_USER_BASE + USER_BASE_FAV + 2;

    public static final int ACTION_DELETE_CATEGORY = ACTION_USER_BASE + USER_BASE_FAV + 3;

    public static final int ACTION_DELETE_FAVORITE = ACTION_USER_BASE + USER_BASE_FAV + 4;

    public static final int ACTION_SAVE_FAVORITE = ACTION_USER_BASE + USER_BASE_FAV + 5;

    public static final int ACTION_SEARCH_FAVORITE = ACTION_USER_BASE + USER_BASE_FAV + 6;

    public static final int ACTION_SEARCH_SUBCATEGORY = ACTION_USER_BASE + USER_BASE_FAV + 7;

    public static final int ACTION_CLEAR_DATA = ACTION_USER_BASE + USER_BASE_FAV + 8;

    public static final int ACTION_UPDATE_DATA = ACTION_USER_BASE + USER_BASE_FAV + 9;
    
    public static final int ACTION_INIT_ADDING_FAVORITE = ACTION_USER_BASE + USER_BASE_FAV + 10;
    
    public static final int ACTION_SYN_FAVORITE = ACTION_USER_BASE + USER_BASE_FAV + 11;
    
    public static final int ACTION_CATEGORY_DELETED = ACTION_USER_BASE + USER_BASE_FAV + 12;
    
    public static final int ACTION_PREPARE_DATA = ACTION_USER_BASE + USER_BASE_FAV + 17;
    
    public static final int ACTION_BACKGROUND_SYNC = ACTION_USER_BASE + USER_BASE_FAV + 18;
    
    public static final int ACTION_NEXT_CATEGORY = ACTION_USER_BASE + USER_BASE_FAV + 19;
    
    public static final int ACTION_CALL = ACTION_USER_BASE + USER_BASE_FAV + 20;
    
    public static final int ACTION_CHECK_MIGRATION = ACTION_USER_BASE + USER_BASE_FAV + 21;
    
    public static final int ACTION_SORT_BY_ALPHABET = ACTION_USER_BASE + USER_BASE_FAV + 22;
    
    public static final int ACTION_SORT_BY_DATE = ACTION_USER_BASE + USER_BASE_FAV + 23;
    
    public static final int ACTION_SORT_BY_DISTANCE = ACTION_USER_BASE + USER_BASE_FAV + 24;
    
    public static final int ACTION_BACK_FROM_SUBCATEGORY = ACTION_USER_BASE + USER_BASE_FAV + 25;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LAUNCH_MAIN = EVENT_MODEL_USER_BASE + USER_BASE_FAV + 1;

    public static final int EVENT_MODEL_SAVE_FAVORITE = EVENT_MODEL_USER_BASE + USER_BASE_FAV + 2;

    public static final int EVENT_MODEL_ALERT = EVENT_MODEL_USER_BASE + USER_BASE_FAV + 3;
    
    public static final int EVENT_MODEL_SYNC_FINISHED = EVENT_MODEL_USER_BASE + USER_BASE_FAV + 4;
    
    // ------------------------------------------------------------
    // Component id
    // --------------------------------
    public static final int ID_FAVORITE_MAIN_SCREEN_TEXTFIELD = USER_BASE_FAV + 1;
    
    public static final int ID_FAVORITE_SUBCATEORY_MAIN_SCREEN_TEXTFIELD =  USER_BASE_FAV + 2;
    
    public static final int ID_FAVORITE_MAIN_SCREEN_ITEM_LIST = USER_BASE_FAV + 3;
    
    public static final int ID_FAVORITE_SUBCATEORY_MAIN_SCREEN_FAVORITE_LIST = USER_BASE_FAV + 4;
    
    public static final int ID_FAVORITE_SUBCATEORY_MAIN_SCREEN_LABEL = USER_BASE_FAV + 5;
    
    public static final int ID_FAVORITE_MAIN_BOTTOM_CONTAINER = USER_BASE_FAV + 6;
    
    public static final int ID_NEW_FAVORITE_TEXTFIELD = USER_BASE_FAV + 7;
    
    public static final int ID_FAVORITE_MAIN_SCREEN_LIST = USER_BASE_FAV + 8;   
    
    public static final int ID_FAVORITE_MAIN_SCREEN_LIST_EMPTY = USER_BASE_FAV + 9; 
    
    public static final int ID_EDIT_BUTTON = USER_BASE_FAV + 10; 
    
    public static final int ID_SYNC_BUTTON = USER_BASE_FAV + 11; 
    
    public static final int ID_NEWCAT_BUTTON = USER_BASE_FAV + 12; 
    
    public static final int ID_NEWFAV_BUTTON = USER_BASE_FAV + 13; 
    
    public static final int ID_FAVORITE_SUBCATEORY_SCREEN_LIST = USER_BASE_FAV + 14;   
    
    public static final int ID_FAVORITE_SUBCATEORY_SCREEN_LIST_EMPTY = USER_BASE_FAV + 15;   
       
    // ------------------------------------------------------------
    // The controller event id definition
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------

    // A string to save the filter text
    public static Integer KEY_S_SEARCH_TEXT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 1);

    // Vector to save the current favorites
    public static Integer KEY_V_SEARCH_FAVORITE_RESULT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 2);

    // Vector to save the current categories
    public static Integer KEY_V_SEARCH_CATEGORY_RESULT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 3);

    // Favorite index
    public static Integer KEY_I_INDEX = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 4);
    
    //phonenumbe of a poi
    public static final Integer KEY_S_POI_PHONENUMBER = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 5);
    
    //Whether user is editing the favorite
    public static final Integer KEY_B_IS_IN_EDIT_MODE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 6);
    
    public static final Integer KEY_S_NEW_FAVORITE_LABEL = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 7);
    
    // Vector to save the current favorites
    public static Integer KEY_V_ALL_FAVORITES = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 8);

    // Vector to save the current categories
    public static Integer KEY_V_ALL_CATEGORIES = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 9);
    
    //Vector to save the favorites in sub category
    public static Integer KEY_V_SUB_CATEGORY_FAVORITES = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 10);
    
    public static Integer KEY_I_SORT_TYPE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 11);
    
    public static Integer KEY_B_KEEP_MINI_BAR = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 12);
    
    public static Integer KEY_B_NEED_REFRESH_LIST = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 13);
    
    public static Integer KEY_B_REFRESH_FROM_NETWORK = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 14);
    
    public static Integer KEY_B_IS_SUBCATEGORY = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 15);
    
    public static Integer KEY_B_NEED_RESORT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 16);
    
    public static Integer KEY_B_IS_LAST_TIME_DATA_AVAILABLE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_FAV + 17);
    
    public final static int[][] BOTTOM_BUTTON_COMMAND_TABLE = new int[][]
            {
                {R.id.myplaceList0AddButton,                CMD_ADD_FAVORITE},
                {R.id.myplaceList0EditButton,                 CMD_ENTER_EDIT_MODE},
                {R.id.myplaceList0SortButton,                CMD_SORT},
            };

    public final static int[] BOTTOM_BUTTON_DISATBLED_IMAGE_RES_TABLE = new int[]
    { 0, R.drawable.my_places_icon_edit_disabled, R.drawable.my_places_icon_sort_disabled };

    public final static int[] BOTTOM_BUTTON_ENABLED_IMAGE_RES_TABLE = new int[]
    { 0, R.drawable.my_places_icon_edit_unfocused, R.drawable.my_places_icon_sort_unfocused };
    
    public static final int SORT_TYPE_CURRENT_ORDER = -999;
    
    public static final int SORT_TYPE_ALPHABET = 1;
    
    public static final int SORT_TYPE_DISTANCE = 2;
    
    public static final int SORT_TYPE_DATE = 3;
}

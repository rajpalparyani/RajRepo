/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IPoiDetailConstants.java
 *
 */
package com.telenav.module.poi.detail;

import com.telenav.module.browsersdk.IBrowserSdkConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-24
 */
interface IPoiDetailConstants extends IBrowserSdkConstants
{
    // -------------------------------------------------------------
    // State definition
    // -------------------------------------------------------------
    public static final int STATE_POI_DETAIL_BASE = (STATE_USER_BASE + USER_BASE_POI_DETAIL);

    public static final int STATE_POI_DETAIL = STATE_POI_DETAIL_BASE + 1;
    
    public static final int STATE_POI_DETAIL_FEEDBACK = STATE_POI_DETAIL_BASE + 2;
    
    public static final int STATE_GETTING_MORE_POIS = STATE_POI_DETAIL_BASE + 3 | MASK_STATE_TRANSIENT;
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_POI_DETAIL_FEEDBACK = CMD_USER_BASE + USER_BASE_POI_DETAIL + 1;
    public static final int CMD_MAP_POI_PAGE_NEXT_NETWORK = CMD_USER_BASE + USER_BASE_POI_DETAIL + 2;
    
    
    public static final int EVENT_MODEL_GET_MORE_POIS = EVENT_MODEL_USER_BASE + USER_BASE_POI_DETAIL + 1;
    public static final int EVENT_MODEL_BACK_TO_MAIN= EVENT_MODEL_USER_BASE + USER_BASE_POI_DETAIL + 2;

    
    
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_RELOAD_GALLERY = ACTION_USER_BASE + USER_BASE_POI_DETAIL + 1;
    public static final int ACTION_GETTING_MORE_POIS = ACTION_USER_BASE + USER_BASE_POI_DETAIL + 2;
    public static final int ACTION_CANCEL_GETTING_MORE_POIS = ACTION_USER_BASE + USER_BASE_POI_DETAIL + 3;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------

    // -----------------------------------------------------------
    // key value between and in modules.
    // -----------------------------------------------------------
    
    // -------------------------------------------------------------
    // Key constants definition
    // -------------------------------------------------------------
    public static final Integer KEY_I_SHOWN_POI_SIZE = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 1);
    
    public static final Integer KEY_B_IS_POPUP_SHOW = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 2);
    
    public static final Integer KEY_IS_NEED_CLEAR_HISTORY = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 3);
    
    public static final Integer KEY_POI_INDEX = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 4);
    
    public static final Integer KEY_B_FIRST_FLUSH_SUCCESS = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 5);
    
    public static final Integer KEY_IS_NEED_RELOAD_GALLERY = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 6);
    
    public static final Integer KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 7);
    
    public static final Integer KEY_B_IS_FIRST_TIME_SHOW = PrimitiveTypeCache.valueOf(STATE_POI_DETAIL_BASE + 8);
    
    
    //-------------------------------------------------------------
    // constants id of component
    //-------------------------------------------------------------
    public static final int ID_WEB_VIEW_COMPONENT = STATE_POI_DETAIL_BASE + 1;
    public static final int ID_WEB_VIEW_FEEDBACK_COMPONENT = STATE_POI_DETAIL_BASE + 2;
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IRouteSettingConstants.java
 *
 */
package com.telenav.module.preference.routesetting;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2011-3-3
 */
public interface IRouteSettingConstants extends ICommonConstants
{
    public static final int STATE_ROUTE_SETTING_BASE = STATE_USER_BASE + USER_BASE_ROUTE_SETTING;
    public static final int STATE_ROUTE_SETTING = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_SETTING + 1);
    public static final int STATE_VOICE_SETTING = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_SETTING + 2);
    public static final int STATE_RETURN_VALUE = MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_ROUTE_SETTING + 3);
    
    //CMD
    public static final int CMD_SAVE = CMD_USER_BASE + USER_BASE_ROUTE_SETTING + 1;
    
    //Action
    public static final int ACTION_SAVE_PREFERENCE = ACTION_USER_BASE + USER_BASE_ROUTE_SETTING + 1;
    
    //Key value for model
    public static final Integer KEY_I_MAX_BUTTON_COUNT_IN_ONE_ROW  = PrimitiveTypeCache.valueOf(STATE_ROUTE_SETTING_BASE + 1);
    public static final Integer KEY_I_TOTAL_SELECTION_NUMBER  = PrimitiveTypeCache.valueOf(STATE_ROUTE_SETTING_BASE + 2);
    public static final Integer KEY_I_SELECTED_ROUTE_TYPE_CHOICE  = PrimitiveTypeCache.valueOf(STATE_ROUTE_SETTING_BASE + 3);
    public static final Integer KEY_A_SELECTED_AVOID     = PrimitiveTypeCache.valueOf(STATE_ROUTE_SETTING_BASE + 4);
    public static final Integer KEY_I_SELECTED_VOICE_CHOICE  = PrimitiveTypeCache.valueOf(STATE_ROUTE_SETTING_BASE + 5);
    
    //ID for components
    final static int ID_ROUTE_SETTING_BUTTON_BASE = 500;
    final static int ID_MAX_ROUTE_SETTING_ID = 510;
    final static int ID_ROUTE_TYPE_CONTAINER = 111;
    
}

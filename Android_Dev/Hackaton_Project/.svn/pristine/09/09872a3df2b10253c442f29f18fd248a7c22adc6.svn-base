/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IPluginConstants.java
 *
 */
package com.telenav.sdk.plugin.module;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2011-2-24
 */
public interface IPluginConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_PLUGIN_BASE           = (STATE_USER_BASE + USER_BASE_PLUGIN) ;
    public static final int STATE_INIT                  = (STATE_PLUGIN_BASE + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_PLUGIN_CHOICES          = (STATE_PLUGIN_BASE + 2) | MASK_STATE_TRANSIENT;
    public static final int STATE_START_PLUGIN          = (STATE_PLUGIN_BASE + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_HANDLE_PLUGIN         = (STATE_PLUGIN_BASE + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_PLUGIN_GOTO_MODULE    = (STATE_PLUGIN_BASE + 5) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_VALIDATE         = (STATE_PLUGIN_BASE + 6) | MASK_STATE_TRANSIENT;
    public static final int STATE_PLUGIN_RGC_ADDRESS    = (STATE_PLUGIN_BASE + 7) | MASK_STATE_TRANSIENT;
    public static final int STATE_DO_ONEBOX_RGC    = (STATE_PLUGIN_BASE + 8) | MASK_STATE_TRANSIENT;
    public static final int STATE_SHARE_SUCCESS         = (STATE_PLUGIN_BASE + 9) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_ONEBOX_SEARCH         = (STATE_PLUGIN_BASE + 10) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_ONE_BOX_RETURN         = (STATE_PLUGIN_BASE + 11) | MASK_STATE_TRANSIENT;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_INIT_PLUGIN     = EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 1;
    public static final int EVENT_MODEL_START_PLUGIN    = EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 2;
    public static final int EVENT_MODEL_HANDLE_PLUGIN   = EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 3;
    public static final int EVENT_MODEL_VALIDATE_ADDRESS= EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 4;
    public static final int EVENT_MODEL_DONE_PLUGIN     = EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 5;
    public static final int EVENT_MODEL_RGC_ADDRESS     = EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 6;
    public static final int EVENT_MODEL_DO_ONEBOX_RGC     = EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 7;
    public static final int EVENT_MODEL_DO_ONEBOX_SEARCH     = EVENT_MODEL_USER_BASE + USER_BASE_PLUGIN + 8;
    //-----------------------------------------------------------
    // cmd id deifinition
    //------------------------------------------------------------
    public static final int CMD_START_PLUGIN            = CMD_USER_BASE + USER_BASE_PLUGIN + 1;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_CHECK_REGION_CHANGE  = ACTION_USER_BASE + USER_BASE_PLUGIN + 1;
    public static final int ACTION_START_PLUGIN         = ACTION_USER_BASE + USER_BASE_PLUGIN + 2;
    public static final int ACTION_GOTO_MODULE          = ACTION_USER_BASE + USER_BASE_PLUGIN + 3;
    public static final int ACTION_JUMP_BACKGROUND      = ACTION_USER_BASE + USER_BASE_PLUGIN + 4;
    public static final int ACTION_RGC_ADDRESS          = ACTION_USER_BASE + USER_BASE_PLUGIN + 5;
    public static final int ACTION_INIT                 = ACTION_USER_BASE + USER_BASE_PLUGIN + 6;
    public static final int ACTION_CHECK_REGION_DETECTION_STATUS = ACTION_USER_BASE + USER_BASE_PLUGIN + 7;
    public static final int ACTION_DO_ONEBOX_RGC = ACTION_USER_BASE + USER_BASE_PLUGIN + 8;
    public static final int ACTION_CHECK_ONE_BOX_RETURN = ACTION_USER_BASE + USER_BASE_PLUGIN + 9;
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static final Integer KEY_S_PLUGIN_ACTION             = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 1);
    public static final Integer KEY_B_PLUGIN_GOTO_MODULE        = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 2);
    public static final Integer KEY_S_VALIDATE_ADDRESS          = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 3);
    public static final Integer KEY_S_VALIDATE_DIRECTIONS_DEST  = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 4);
    public static final Integer KEY_B_VALIDATE_TWO_ADDRESSES    = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 5);
    public static final Integer KEY_B_VALIDATE_DIRECTIONS_ORIG  = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 6);
    public static final Integer KEY_O_PLUGIN_ADDRESS            = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 7);
    public static final Integer KEY_O_PLUGIN_RGC_ADDRESS        = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 8);
    public static final Integer KEY_S_VALIDATE_COUNTRY          = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 9);
    public static final Integer KEY_O_ONEBOX_ADDRESS          = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 10);
    public static final Integer KEY_B_IS_ONEBOX_FAILED             = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 11);
    public static final Integer KEY_B_IS_RGC_FINISHED                = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 12);
    public static final Integer KEY_B_IS_ONEBOX_RGC          = PrimitiveTypeCache.valueOf(STATE_PLUGIN_BASE + 13);

}

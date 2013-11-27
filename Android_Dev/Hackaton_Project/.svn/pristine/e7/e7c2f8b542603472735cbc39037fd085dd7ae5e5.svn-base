/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMaiTaiConstants.java
 *
 */
package com.telenav.sdk.maitai.module;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author qli
 * @date 2010-12-2
 */
public interface IMaiTaiConstants extends ICommonConstants
{
    
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_MAITAI_BASE           = (STATE_USER_BASE + USER_BASE_MAITAI) ;
    public static final int STATE_INIT                  = (STATE_MAITAI_BASE + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_START_MAITAI          = (STATE_MAITAI_BASE + 2) | MASK_STATE_TRANSIENT;
    public static final int STATE_HANDLE_MAITAI         = (STATE_MAITAI_BASE + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_MAITAI_GOTO_MODULE    = (STATE_MAITAI_BASE + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_VALIDATE         = (STATE_MAITAI_BASE + 5) | MASK_STATE_TRANSIENT;
    public static final int STATE_MAITAI_RGC_ADDRESS    = (STATE_MAITAI_BASE + 6) | MASK_STATE_TRANSIENT;
    public static final int STATE_START_MAITAI_ERROR    = (STATE_MAITAI_BASE + 7) | MASK_STATE_TRANSIENT;
    
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_INIT_MAITAI     = EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 1;
    public static final int EVENT_MODEL_START_MAITAI    = EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 2;
    public static final int EVENT_MODEL_HANDLE_MAITAI   = EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 3;
    public static final int EVENT_MODEL_VALIDATE_ADDRESS= EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 4;
    public static final int EVENT_MODEL_VIRGIN_MAITAI   = EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 5;
    public static final int EVENT_MODEL_RGC_ADDRESS     = EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 6;
    public static final int EVENT_MODEL_GET_ADDRESS_BY_ID= EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 7;
    public static final int EVENT_MODEL_START_MAITAI_ERROR = EVENT_MODEL_USER_BASE + USER_BASE_MAITAI + 8;
    
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_START_MAITAI         = ACTION_USER_BASE + USER_BASE_MAITAI + 1;
    public static final int ACTION_HANDLE_MAITAI        = ACTION_USER_BASE + USER_BASE_MAITAI + 2;
    public static final int ACTION_GOTO_MODULE          = ACTION_USER_BASE + USER_BASE_MAITAI + 3;
    public static final int ACTION_JUMP_BACKGROUND      = ACTION_USER_BASE + USER_BASE_MAITAI + 4;
    public static final int ACTION_CHECK_REGION_DETECTION_STATUS        = ACTION_USER_BASE + USER_BASE_MAITAI + 5;
    public static final int ACTION_RGC_ADDRESS          = ACTION_USER_BASE + USER_BASE_MAITAI + 6;
    public static final int ACTION_CHECK_REGION_CHANGE          = ACTION_USER_BASE + USER_BASE_MAITAI + 7;
    public static final int ACTION_GOTO_GET_ADDRESS_BY_ID          = ACTION_USER_BASE + USER_BASE_MAITAI + 8;
    
     
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------
    public static final Integer KEY_S_MAITAI_ACTION             = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 1);
    public static final Integer KEY_B_MAITAI_GOTO_MODULE        = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 2);
    public static final Integer KEY_S_VALIDATE_ADDRESS          = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 3);
    public static final Integer KEY_S_VALIDATE_DIRECTIONS_DEST  = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 4);
    public static final Integer KEY_B_VALIDATE_TWO_ADDRESSES    = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 5);
    public static final Integer KEY_B_VALIDATE_DIRECTIONS_ORIG  = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 6);
    public static final Integer KEY_B_IS_SET_HOME               = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 7);
    public static final Integer KEY_S_NOTIFICATION_ADDRESS_ID   = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 8);
    public static final Integer KEY_O_MAITAI_RGC_ADDRESS   = PrimitiveTypeCache.valueOf(STATE_MAITAI_BASE + 9);
}

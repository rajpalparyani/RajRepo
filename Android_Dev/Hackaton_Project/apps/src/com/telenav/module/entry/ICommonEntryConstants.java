/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ICommonEntryConstants.java
 *
 */
package com.telenav.module.entry;

import com.telenav.mvc.ICommonConstants;

/**
 *@author qli
 *@date 2011-1-27
 */
public interface ICommonEntryConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_COMMON_ENTRY      = STATE_USER_BASE + USER_BASE_COMMON_ENTRY;
    public static final int STATE_LINK_TO_AC        = (STATE_COMMON_ENTRY + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_LINK_TO_POI       = (STATE_COMMON_ENTRY + 2) | MASK_STATE_TRANSIENT;
    public static final int STATE_LINK_TO_EXTRA     = (STATE_COMMON_ENTRY + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_LINK_TO_MAP       = (STATE_COMMON_ENTRY + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_DO_LINK           = (STATE_COMMON_ENTRY + 5) | MASK_STATE_TRANSIENT;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_LINK_TO_AC       = ACTION_USER_BASE + USER_BASE_COMMON_ENTRY + 1;
    public static final int ACTION_LINK_TO_MAP      = ACTION_USER_BASE + USER_BASE_COMMON_ENTRY + 2;
    public static final int ACTION_LINK_TO_POI      = ACTION_USER_BASE + USER_BASE_COMMON_ENTRY + 3;
    public static final int ACTION_LINK_TO_EXTRA    = ACTION_USER_BASE + USER_BASE_COMMON_ENTRY + 4;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_LINK_TO_AC  = EVENT_MODEL_USER_BASE +  USER_BASE_COMMON_ENTRY + 1;
    public static final int EVENT_MODEL_LINK_TO_MAP = EVENT_MODEL_USER_BASE +  USER_BASE_COMMON_ENTRY + 2;
    public static final int EVENT_MODEL_LINK_TO_POI = EVENT_MODEL_USER_BASE +  USER_BASE_COMMON_ENTRY + 3;
    public static final int EVENT_MODEL_LINK_TO_EXTRA = EVENT_MODEL_USER_BASE +  USER_BASE_COMMON_ENTRY + 4;
}

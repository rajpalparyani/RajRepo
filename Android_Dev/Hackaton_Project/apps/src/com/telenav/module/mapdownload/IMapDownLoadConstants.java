/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IMapDownLoadConstants.java
 *
 */
package com.telenav.module.mapdownload;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yren
 *@date 2012-12-11
 */
public interface IMapDownLoadConstants extends ICommonConstants
{
    public static final int STATE_MAP_DOWNLOAD_BASE = STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD;
    public static final int STATE_REQUEST_DOWNLOADABLE_REGION = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_MAIN = STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 2;
    public static final int STATE_CANCEL_MAP_DOWNLOAD_CONFIRM = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_DOWNLOADING = STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 4;
    public static final int STATE_ACTIVE_FEATURE_LIST = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 5) | MASK_STATE_TRANSIENT;
    public static final int STATE_DELETE_MAP_DOWNLOAD_CONFIRM = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 6) | MASK_STATE_TRANSIENT;
    public static final int STATE_REPLACE_MAP_DOWNLOAD_CONFIRM = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 7) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_WIFI = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 8) | MASK_STATE_TRANSIENT;
    public static final int STATE_WIFI_DISCONNECTED = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 9) | MASK_STATE_TRANSIENT;
    public static final int STATE_NOT_INITED_ERROR = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 10) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHAGING_BUTTON = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 11) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_DELETING = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 12) | MASK_STATE_TRANSIENT;
    public static final int STATE_STILL_DELETING = (STATE_USER_BASE + USER_BASE_MAP_DOWNLOAD + 13) | MASK_STATE_TRANSIENT;
    
    public static final int EVENT_MODEL_SHOW_DOWNLOADABLE_REGION = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 1;
    public static final int EVENT_MODEL_DOWNLOAD_COMPLETE = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 2;
    public static final int EVENT_MODEL_SHOW_DOWNLOADING = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 3;
    public static final int EVENT_MODEL_START_DOWNLOAD = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 4;
    public static final int EVENT_MODEL_UPGRADE = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 5;
    public static final int EVENT_MODEL_REPLACE_DOWNLOAD = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 6;
    public static final int EVENT_MODEL_WIFI_DISCONNECTED = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 7;
    public static final int EVENT_MODEL_DO_ACTION_AFTER_UP_SELL = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 8;
    public static final int EVENT_MODEL_DO_NOTHING_AFTER_UP_SELL = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 9;
    public static final int EVENT_MODEL_SET_BACK_BUTTON = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 10;
    public static final int EVENT_MODEL_STILL_DELETING = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 11;
    public static final int EVENT_MODEL_NOT_DELETING = EVENT_MODEL_USER_BASE + USER_BASE_MAP_DOWNLOAD + 12;
    
    public static final int CMD_DOWNLOADABLE_REGION_SELECTED_BASE = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 800;
    public static final int CMD_START_DOWNLOAD = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 1;
    public static final int CMD_CANCEL_DOWNLOAD = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 2;
    public static final int CMD_PAUSE_DOWNLOAD = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 3;
    public static final int CMD_DELETE_DOWNLOAD = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 4;
    public static final int CMD_RESUME_DOWNLOAD = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 5;
    public static final int CMD_UPGRADE = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 6;
    public static final int CMD_ACTIVATED = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 7;
    public static final int CMD_REPLACE_DOWNLOAD = CMD_USER_BASE + USER_BASE_MAP_DOWNLOAD + 8;
    
    public static final int ACTION_INIT = 0;
    public static final int ACTION_START_DOWNLOAD = 1;
    public static final int ACTION_CANCEL_DOWNLOAD = 2;
    public static final int ACTION_PAUSE_DOWNLOAD = 3;
    public static final int ACTION_DELETE_DOWNLOAD = 4;
    public static final int ACTION_CHECK_REGION_STATUS = 5;
    public static final int ACTION_UPGRADE = 6;
    public static final int ACTION_REACTIVATED = 7;
    public static final int ACTION_REPLACE = 8;
    public static final int ACTION_CHECK_WIFI = 9;
    public static final int ACTION_CHECK_STATUS_CHANGED = 10;
    public static final int ACTION_CHECK_UPSELL_RETURN = 11;
    public static final int ACTION_CHECK_DELETING = 12;
    
    public static final int ID_START_BUTTON = 1000;
    public static final int ID_CANCEL_BUTTON = 1001;
    public static final int ID_DELETE_BUTTON = 1002;
    public static final int ID_REGION_SELECTED_INFO_CONTAINER = 1003;
    public static final int ID_REGION_DOWNLOAD_PERCENT_LABEL = 1004;
    public static final int ID_REGION_DOWNLOAD_SPEED_LABEL = 1005;
    public static final int ID_PAUSE_BUTTON = 1006;
    public static final int ID_BUTTON_CONTAINER = 1007;
    public static final int ID_RESUME_BUTTON = 1008;
    public static final int ID_REGION_SELECTED_INFO = 8000;
    
    public static final Integer KEY_O_MAP_DOWNLOAD_MANAGER = PrimitiveTypeCache.valueOf(STATE_MAP_DOWNLOAD_BASE + 1);
    public static final Integer KEY_I_MAP_REGION_TO_DOWNLOAD = PrimitiveTypeCache.valueOf(STATE_MAP_DOWNLOAD_BASE + 2);
    public static final Integer KEY_I_CMD_TO_CHECK_WIFI = PrimitiveTypeCache.valueOf(STATE_MAP_DOWNLOAD_BASE + 3);
    public static final Integer KEY_I_UPSELL_OPERATION_TYPE = PrimitiveTypeCache.valueOf(STATE_MAP_DOWNLOAD_BASE + 4);
    public static final Integer KEY_I_CMD_TO_TRIGGER_UPSELL = PrimitiveTypeCache.valueOf(STATE_MAP_DOWNLOAD_BASE + 5);
    public static final Integer KEY_B_IS_NEED_SET_BACK_BUTTON = PrimitiveTypeCache.valueOf(STATE_MAP_DOWNLOAD_BASE + 6);
    
    public static final int BTN_TYPE_DELETE = 1;
    public static final int BTN_TYPE_DOWNLOAD = 2;
    public static final int BTN_TYPE_REACTIVE = 3;
}

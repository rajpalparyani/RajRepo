/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ILoginConstants.java
 *
 */
package com.telenav.module.login;

import java.util.regex.Pattern;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-11-26
 */
public interface ILoginConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_INIT = (STATE_USER_BASE + USER_BASE_LOGIN + 1) | MASK_STATE_TRANSIENT;
    public static final int STATE_GOTO_TOS =  (STATE_USER_BASE + USER_BASE_LOGIN + 2) | MASK_STATE_TRANSIENT;
    public static final int STATE_BACK_TO_MAIN = (STATE_USER_BASE + USER_BASE_LOGIN + 3) | MASK_STATE_TRANSIENT;
    public static final int STATE_EXIT_APP = (STATE_USER_BASE + USER_BASE_LOGIN + 4) | MASK_STATE_TRANSIENT;
    public static final int STATE_TYPEIN_PTN = (STATE_USER_BASE + USER_BASE_LOGIN + 5);
    public static final int STATE_CHECK_TYPE_PTN = (STATE_USER_BASE + USER_BASE_LOGIN + 6) | MASK_STATE_TRANSIENT;
    public static final int STATE_SENDING_FORGET_REQUESET = (STATE_USER_BASE + USER_BASE_LOGIN + 7) | MASK_STATE_TRANSIENT;
    public static final int STATE_PIN_SENT = (STATE_USER_BASE + USER_BASE_LOGIN + 8) | MASK_STATE_TRANSIENT;
    public static final int STATE_WAIT_DIM_PROCESS = (STATE_USER_BASE + USER_BASE_LOGIN + 9) | MASK_STATE_TRANSIENT;
    public static final int STATE_REQUEST_VERIFICATION_CODE = (STATE_USER_BASE + USER_BASE_LOGIN + 10) | MASK_STATE_TRANSIENT;;
    public static final int STATE_INPUT_VERIFICATION_CODE = (STATE_USER_BASE + USER_BASE_LOGIN + 11);
    public static final int STATE_CHECK_VERIFICATION_CODE = (STATE_USER_BASE + USER_BASE_LOGIN + 12) | MASK_STATE_TRANSIENT;
    public static final int STATE_CHECK_DIM_PTN = (STATE_USER_BASE + USER_BASE_LOGIN + 13) | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_WELCOME = STATE_USER_BASE + USER_BASE_LOGIN + 14;
    public static final int STATE_FTUE_SHOW_SIGN_UP = STATE_USER_BASE + USER_BASE_LOGIN + 15;
    public static final int STATE_FTUE_SHOW_SIGN_IN = STATE_USER_BASE + USER_BASE_LOGIN + 16;
    public static final int STATE_FTUE_MAYBE_LATER=STATE_USER_BASE + USER_BASE_LOGIN + 17 | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_SHOW_TOS = STATE_USER_BASE + USER_BASE_LOGIN + 18;
    public static final int STATE_FTUE_SIGN_UP_SUBMIT = STATE_USER_BASE + USER_BASE_LOGIN + 19 | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_SIGN_IN_SUBMIT = STATE_USER_BASE + USER_BASE_LOGIN + 20 | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_CHECK_EMAIL_ERROR = (STATE_USER_BASE + USER_BASE_LOGIN + 21) | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_VALIDATION_PASSWORD_ERROR = (STATE_USER_BASE + USER_BASE_LOGIN + 22) | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_LORDING = (STATE_USER_BASE + USER_BASE_LOGIN + 23) | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_SUCCESS = (STATE_USER_BASE + USER_BASE_LOGIN + 24) | MASK_STATE_TRANSIENT;
    public static final int STATE_PTN_CONFIRMATION = (STATE_USER_BASE + USER_BASE_LOGIN + 25) | MASK_STATE_TRANSIENT;
    public static final int STATE_PTN_VERIFY_FAIL = (STATE_USER_BASE + USER_BASE_LOGIN + 26) | MASK_STATE_TRANSIENT;
    public static final int STATE_BACK_TO_LAUNCH = (STATE_USER_BASE + USER_BASE_LOGIN + 27) | MASK_STATE_TRANSIENT;
    public static final int STATE_FTUE_SHOW_EDIT_ACCOUNT = STATE_USER_BASE + USER_BASE_LOGIN + 28;     
    public static final int STATE_FTUE_EDIT_ACCOUNT = STATE_USER_BASE + USER_BASE_LOGIN + 29| MASK_STATE_TRANSIENT;     
    public static final int STATE_FTUE_ACCOUNT_BACK = STATE_USER_BASE + USER_BASE_LOGIN + 30| MASK_STATE_TRANSIENT;     
    public static final int STATE_FTUE_LOGIN_EMAIL_NOT_REGISTERED = STATE_USER_BASE + USER_BASE_LOGIN + 31| MASK_STATE_TRANSIENT;     
    public static final int STATE_FTUE_LOGIN_PASSWORD_INCORRECT = STATE_USER_BASE + USER_BASE_LOGIN + 32| MASK_STATE_TRANSIENT;     
    public static final int STATE_FORGET_PASSWORD = STATE_USER_BASE + USER_BASE_LOGIN + 33;    
    public static final int STATE_FTUE_LOGIN_EMAIL_ALREADY_REGISTERED = (STATE_USER_BASE + USER_BASE_LOGIN + 34) | MASK_STATE_TRANSIENT;
    public static final int STATE_UPGRADE_LOGIN_FAILED = (STATE_USER_BASE + USER_BASE_LOGIN + 35) | MASK_STATE_TRANSIENT;
    
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    public static final int CMD_FORGET_PIN = STATE_USER_BASE + USER_BASE_LOGIN + 1;
    public static final int CMD_TYPEIN_CONTINUE = STATE_USER_BASE + USER_BASE_LOGIN + 2;
    public static final int CMD_FTUE_SIGN_UP = STATE_USER_BASE + USER_BASE_LOGIN + 3;
    public static final int CMD_FTUE_SIGN_IN = STATE_USER_BASE + USER_BASE_LOGIN + 4;
    public static final int CMD_FTUE_MAYBE_LATER = STATE_USER_BASE + USER_BASE_LOGIN + 5;
    public static final int CMD_FTUE_TOS = STATE_USER_BASE + USER_BASE_LOGIN + 6;
    public static final int CMD_FTUE_SIGN_UP_SUBMIT = STATE_USER_BASE + USER_BASE_LOGIN + 7;
    public static final int CMD_FTUE_LOGIN_FORGET_PASSWD = STATE_USER_BASE + USER_BASE_LOGIN + 8;
    public static final int CMD_FTUE_SIGN_IN_SUBMIT = STATE_USER_BASE + USER_BASE_LOGIN + 9;
    public static final int CMD_FTUE_DONE = STATE_USER_BASE + USER_BASE_LOGIN + 10;
    public static final int CMD_FTUE_EDIT_ACCOUNT = STATE_USER_BASE + USER_BASE_LOGIN + 11;
    public static final int CMD_UPGRADE_BACK_TO_FTUE = STATE_USER_BASE + USER_BASE_LOGIN + 12;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    public static final int ACTION_CHECK_DIM_PTN = ACTION_USER_BASE + USER_BASE_LOGIN + 1;
    public static final int ACTION_FORGET_PIN = ACTION_USER_BASE + USER_BASE_LOGIN + 2;
    public static final int ACTION_CHECK_DIM_PROCESS = ACTION_USER_BASE + USER_BASE_LOGIN + 3;
    public static final int ACTION_REQUEST_VERIFICATION_CODE = ACTION_USER_BASE + USER_BASE_LOGIN + 4;
    public static final int ACTION_CHECK_VERIFICATION_CODE = ACTION_USER_BASE + USER_BASE_LOGIN + 5;
    public static final int ACTION_LOGIN_WITH_VERIFICATION_CODE = ACTION_USER_BASE + USER_BASE_LOGIN + 6;
    public static final int ACTION_CHECK_CONTEXT = ACTION_USER_BASE + USER_BASE_LOGIN + 7;
    public static final int ACTION_CHECK_TYPE_IN_PTN = ACTION_USER_BASE + USER_BASE_LOGIN + 8;
    public static final int ACTION_SIGN_UP_PREPARE = ACTION_USER_BASE + USER_BASE_LOGIN + 9;
    public static final int ACTION_SIGN_IN_PREPARE = ACTION_USER_BASE + USER_BASE_LOGIN + 10;
    public static final int ACTION_JUMP_TO_BACKGROUND = ACTION_USER_BASE + USER_BASE_LOGIN + 11;
    public static final int ACTION_FTUE_SUCCESS_GO = ACTION_USER_BASE + USER_BASE_LOGIN + 12;
    public static final int ACTION_FTUE_RESTORE_PROCESS = ACTION_USER_BASE + USER_BASE_LOGIN + 13;
    public static final int ACTION_MAYBE_LATER = ACTION_USER_BASE + USER_BASE_LOGIN + 14;
    public static final int ACTION_SIGN_UP_SUMMIT = ACTION_USER_BASE + USER_BASE_LOGIN + 15;
    public static final int ACTION_SIGN_IN_SUMMIT = ACTION_USER_BASE + USER_BASE_LOGIN + 16;
    public static final int ACTION_EDIT_ACCOUNT = ACTION_USER_BASE + USER_BASE_LOGIN + 17;
    public static final int ACTION_ACCOUNT_BACK_CHECK = ACTION_USER_BASE + USER_BASE_LOGIN + 18;
    public static final int ACTION_SET_MAYBELAYTER_DISABLE = ACTION_USER_BASE + USER_BASE_LOGIN + 19;
    public static final int ACTION_CLEAR_TYPE_IN = ACTION_USER_BASE + USER_BASE_LOGIN + 20;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    
    public static final int EVENT_MODEL_LOGIN_FINISH = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 1;
    public static final int EVENT_MODEL_TYPE_IN = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 2;
    public static final int EVENT_MODEL_PIN_SENT = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 3;
    public static final int EVENT_MODEL_DIM_FINISH = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 4;
    public static final int EVENT_MODEL_SEND_VERIFICATION_CODE = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 5;
    public static final int EVENT_MODEL_VERI_CODE_AVAILABLE = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 6;
    public static final int EVENT_MODEL_VERI_CODE_UNAVAILABLE = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 7;
    public static final int EVENT_MODEL_VERI_CODE_SENT = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 8;
    public static final int EVENT_MODEL_LAUNCH_FTUE = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 9;
    public static final int EVENT_MODEL_CHECK_EMAIL_ERROR = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 10;
    public static final int EVENT_MODEL_CHECK_PASSWORD_ERROR = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 11;
    public static final int EVENT_MODEL_FTUE_LOADING = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 12;
    public static final int EVENT_MODEL_FTUE_SUCCESS = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 13;
    public static final int EVENT_MODEL_WAIT_DIM_PROCESS = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 14;
    public static final int EVENT_MODEL_FTUE_SIGN_UP = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 15;
    public static final int EVENT_MODEL_FTUE_SIGN_IN = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 16;
    public static final int EVENT_MODEL_RESTORE_PROCESS = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 17;
    public static final int EVENT_MODEL_VERIFY_PTN_FAIL = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 18;
    public static final int EVENT_MODEL_BACK_TO_LAUNCH = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 19;
    public static final int EVENT_MODEL_EDIT_ACCOUNT_DONE = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 20;
    public static final int EVENT_MODEL_BACK_TO_PREV = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 21;
    public static final int EVENT_MODEL_EMAIL_NOT_REGISTERED = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 22;
    public static final int EVENT_MODEL_PASSWARD_INCORRECT = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 23;
    public static final int EVENT_MODEL_EMAIL_ALREADY_REGISTERED = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 24;
    public static final int EVENT_MODEL_UPGRADE_LOGIN_FAILED = EVENT_MODEL_USER_BASE + USER_BASE_LOGIN + 25;
    
    // ------------------------------------------------------------
    // The key definition
    // ------------------------------------------------------------

    public static Integer KEY_S_PRODUCT_TYPE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 1);
    public static Integer KEY_S_INPUT_STR = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 2);
    public static Integer KEY_S_FIRSTNAME = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 3);
    public static Integer KEY_S_LASTNAME = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 4);
    public static Integer KEY_S_EMAILADDRESS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 5);
    public static Integer KEY_S_PASSWORD = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 6);
    public static Integer KEY_S_VERIFY_CODE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 7);
    public static Integer KEY_I_LOGIN_TYPE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 8);
    public static Integer KEY_B_TRIGGER_BY_KEY_BACK = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 9);
    public static Integer KEY_B_MEYBELATER_DISABLE = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 10);
    public static Integer KEY_S_PREFETCH_FIRSTNAME = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 11);
    public static Integer KEY_S_PREFETCH_LASTNAME = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 12);
    public static Integer KEY_S_PREFETCH_EMAILADDRESS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 13);
    public static Integer KEY_B_UPGRADE_LOGIN = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_LOGIN + 14);
    
    // ------------------------------------------------------------
    // The Component id definition
    // ------------------------------------------------------------
    public static final int ID_TYPEIN_TEXTFIELD = 1000;
    
    public static final int TYPE_EMAIL = 1000;
    public static final int TYPE_PASSWORD = 1001;
    public static final int TYPE_PTN = 1002;
    public static final int TYPE_VERIFY_CODE = 1003;
    public static final int TYPE_FIRSTNAME = 1004;
    public static final int TYPE_LASTNAME = 1005;
    
    public static final int STATUS_VERICATION_CODE_ERROR = 3000;
    
    public static final int BTN_ENABLED_COLOR = 0xff241f21;
    public static final int BTN_DISABLE_COLOR = 0x66241f21;

    public static final int LINK_TOUCH_UP_COLOR = 0xffffffff;
    public static final int LINK_TOUCH_DOWN_COLOR = 0xffffa134;
   
    public static final int BTN_TOUCH_UP_COLOR = 0xffffffff;
    public static final int BTN_TOUCH_DOWN_COLOR = 0xFF241F21;
    
    public static final String KEY_FORGET_PASSWORD_WEBVIEW = "Forget_password";
            
    public static final Pattern EMAIL_ADDRESS_PATTERN
    = Pattern.compile("^[a-zA-Z0-9]+([\\_|\\-|\\.]?[a-zA-Z0-9])*\\@[a-zA-Z0-9]+([\\_|\\-|\\.]?[a-zA-Z0-9])*\\.[a-zA-Z]{2,3}$"
    );
    
}

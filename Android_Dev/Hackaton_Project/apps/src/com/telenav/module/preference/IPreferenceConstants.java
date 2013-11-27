/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceConstants.java
 *
 */
package com.telenav.module.preference;

import com.telenav.mvc.ICommonConstants;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.ImageDecorator;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-11
 */
interface IPreferenceConstants extends ICommonConstants
{
    // ------------------------------------------------------------
    // State id definition
    // ------------------------------------------------------------
    public static final int STATE_PREFERENCE_BASE           = STATE_USER_BASE + USER_BASE_PREFERENCE;

    public static final int STATE_PREFERENCE_ROOT           = STATE_PREFERENCE_BASE + 1;

    public static final int STATE_PREFERENCE_INIT           = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 2);
    
    public static final int STATE_PREFERENCE_LAYER1         = STATE_PREFERENCE_BASE + 3;
    
    public static final int STATE_PREFERENCE_CHANGING_CHECK = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 4);
    
    public static final int STATE_SAVING_UPLOAD_PREFENCE    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 6);
    
    public static final int STATE_GO_TO_ROUTE_SETTING    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 7);
    
    public static final int STATE_PREFERENCE_VALID_FAIL     = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 8);
    
    public static final int STATE_GO_TO_SET_HOME     = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 9);
    
    public static final int STATE_GO_TO_SET_WORK     = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 10);
    

    
    public static final int STATE_PREFERENCE_CHANGING_LANGUAGE  = STATE_PREFERENCE_BASE + 11;
    
    public static final int STATE_CHECK_LANGUAGE    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 12);
   
    public static final int STATE_GOTO_ABOUT_PAGE  = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 13);
    
    public static final int STATE_GOTO_SUPPORT_INFO    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 14);
    
    public static final int STATE_GOTO_FTUE   =  MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 15);
    
    public static final int STATE_GOTO_TOS    =  MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 16);
    
    public static final int STATE_MY_CAR    = STATE_PREFERENCE_BASE + 17;
    
    public static final int STATE_GOTO_MAP_DOWNLOAD    =  MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 18);
    
    public static final int STATE_FTUE_EDIT_ACCOUNT   = STATE_PREFERENCE_BASE + 19;
    
    public static final int STATE_CAR_CHANGING_CHECK    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 20);
    
    public static final int STATE_PREFERENCE_CHANGING_CAR    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 21);
    
    public static final int STATE_SHOW_SUBSCRIPTION    = STATE_PREFERENCE_BASE + 22;

    public static final int STATE_GOTO_UPSELL_CANCEL_SUBSCRIPTION  = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 23);
    
    public static final int STATE_CANCEL_SUBSCRIPTION_CONFIRM    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 24);

    public static final int STATE_GOTO_UPSELL    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 25);
    
    public static final int STATE_BACK_TO_DASHBOARD    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 26);
    
    public static final int STATE_SHOW_CANCEL_SUBSCRIPTION_SUCCESS    = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 27);

    public static final int STATE_GOTO_SHARE_SCOUT  = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 28);
    
    public static final int STATE_GOTO_DRIVING_SHARE_SETTING  = MASK_STATE_TRANSIENT | (STATE_PREFERENCE_BASE + 29);
    
    //-----------------------------------------------------------
    // model event definition
    //-----------------------------------------------------------
    public static final int EVENT_MODEL_PREFERENCE_BASE     = EVENT_MODEL_USER_BASE + USER_BASE_PREFERENCE;
    
    public static final int EVENT_MODEL_PREFERENCE_GOTO_ROOT = EVENT_MODEL_PREFERENCE_BASE + 1;
    
    public static final int EVENT_MODEL_PREFERENCE_CHANGED  = EVENT_MODEL_PREFERENCE_BASE + 2;
    
    public static final int EVENT_MODEL_BACK_TO_ROOT        = EVENT_MODEL_PREFERENCE_BASE + 3;
    
    public static final int EVENT_MODEL_VALID_FAIL          = EVENT_MODEL_PREFERENCE_BASE + 4;
    public static final int EVENT_MODEL_LANGUAGE_CHANGE        = EVENT_MODEL_PREFERENCE_BASE + 5;
    public static final int EVENT_MODEL_CAR_CHANGED        = EVENT_MODEL_PREFERENCE_BASE + 6;
    
    public static final int EVENT_MODDEL_CAR_CHANGED_FINISH        = EVENT_MODEL_PREFERENCE_BASE + 7;
    public static final int EVENT_MODEL_SUBSCRIPTION_CANCEL_SUCCESS        = EVENT_MODEL_PREFERENCE_BASE + 8;
    public static final int EVENT_MODEL_SUBSCRIPTION_CANCEL_FAIL        = EVENT_MODEL_PREFERENCE_BASE + 9;
    
    //-----------------------------------------------------------
    // cmd id deifinition
    //------------------------------------------------------------
    public static final int CMD_PREFERENCE_BASE             = CMD_USER_BASE + USER_BASE_PREFERENCE;
    
    public static final int CMD_PREFERENCE_COMBOX_CHANGE    = CMD_PREFERENCE_BASE + 1;
    
    public static final int CMD_SAVE_UPLOAD_PREFERENCE      = CMD_PREFERENCE_BASE + 2;
    
    public static final int CMD_BACK_TO_ROOT                = CMD_PREFERENCE_BASE + 3;
    
    public static final int CMD_SAVE_AND_BACK               = CMD_PREFERENCE_BASE + 4;
    
    public static final int CMD_GO_TO_ROUTE_SETTING         = CMD_PREFERENCE_BASE + 5;
    
    public static final int CMD_GO_TO_SET_HOME              = CMD_PREFERENCE_BASE + 6;
    
    public static final int CMD_GO_TO_SET_WORK              = CMD_PREFERENCE_BASE + 7;
    
    public static final int CMD_SWITCH_LANGUAGE             = CMD_PREFERENCE_BASE + 8;
    
    public static final int CMD_ABOUT_PAGE                  = CMD_PREFERENCE_BASE + 9;
    
    public static final int CMD_SUPPORT_INFO                = CMD_PREFERENCE_BASE +10;

    public static final int CMD_AUDIO                       = CMD_PREFERENCE_BASE + 11;
    
    public static final int CMD_MAP_NAV_SETTING             = CMD_PREFERENCE_BASE + 12;
    
    public static final int CMD_MY_CAR                      = CMD_PREFERENCE_BASE + 13;
    
    public static final int CMD_TERMS_CONDITIONS            = CMD_PREFERENCE_BASE + 14;
    
    public static final int CMD_CREATE_ACCOUNT              = CMD_PREFERENCE_BASE + 15;
    
    public static final int CMD_LOGIN                       = CMD_PREFERENCE_BASE + 16;
    
    public static final int CMD_EDIT_ACCOUNT                = CMD_PREFERENCE_BASE + 17;
    
    public static final int CMD_SHOW_SUBSCRIPTION           = CMD_PREFERENCE_BASE + 18;  
    
    public static final int CMD_MAP_DOWNLOAD                = CMD_PREFERENCE_BASE + 19;
        
    public static final int CMD_SHARE_SCOUT                 = CMD_PREFERENCE_BASE + 20;
    
    public static final int CMD_RATE_SCOUT                  = CMD_PREFERENCE_BASE + 21;
    

    //NOTE: please don't use 19 - 39 
    public static final int CMD_SELECT_CAR = CMD_PREFERENCE_BASE + 19;
    public static final int CMD_SELECT_CAR_START = CMD_PREFERENCE_BASE + 20;
    public static final int CMD_SELECT_CAR_END = CMD_PREFERENCE_BASE + 39;
    
    public static final int CMD_CANCEL_SUBSCRIPTION_CONFIRM = CMD_PREFERENCE_BASE + 40;
    public static final int CMD_CANCEL_SUBSCRIPTION = CMD_PREFERENCE_BASE + 41;
    public static final int CMD_GOTO_UPSELL = CMD_PREFERENCE_BASE + 42;
    
    public static final int CMD_DELETE_HOME = CMD_PREFERENCE_BASE + 43;
    
    public static final int CMD_DELETE_WORK = CMD_PREFERENCE_BASE + 44;
    
    public static final int CMD_DRIVING_SHARE_SETTING = CMD_PREFERENCE_BASE + 45;
    
    //-----------------------------------------------------------
    // action id definition
    //-----------------------------------------------------------
    public static final int ACTION_PREFERENCE_BASE          = ACTION_USER_BASE + USER_BASE_PREFERENCE;
    
    public static final int ACTION_PREFERENCE_INIT          = ACTION_PREFERENCE_BASE + 1;
    
    public static final int ACTION_PREFERENCE_CHANGING_CHECK = ACTION_PREFERENCE_BASE + 2;
    
    public static final int ACTION_SAVING_UPLOAD            = ACTION_PREFERENCE_BASE + 3;
    
    public static final int ACTION_PREFENCE_RESET           = ACTION_PREFERENCE_BASE + 4;
    
    public static final int ACTION_CHECK_RETURNED_PREFERENCE_VALUE           = ACTION_PREFERENCE_BASE + 5;
    
    public static final int ACTION_CHECK_LANGUAGE_CHANGED           = ACTION_PREFERENCE_BASE + 6;
    
    public static final int ACTION_CAR_CHANGING_CHECK           = ACTION_PREFERENCE_BASE + 7;
    
    public static final int ACTION_CHANGE_CAR           = ACTION_PREFERENCE_BASE + 8;
    
    public static final int ACTION_CHECK_MIGRATION          = ACTION_PREFERENCE_BASE + 9;
    
    public static final int ACTION_SET_CANCEL_SUBSCRIPTION_FLAG          = ACTION_PREFERENCE_BASE + 10;
    
    public static final int ACTION_DELETE_HOME          = ACTION_PREFERENCE_BASE + 11;
    
    public static final int ACTION_DELETE_WORK          = ACTION_PREFERENCE_BASE + 12;
    
    //-----------------------------------------------------------
    // key value between and in modules.
    //-----------------------------------------------------------
    public static final Integer KEY_I_SELECTED_PREFERENCE_GROUP_ID = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 1);
    
    public static final Integer KEY_S_SELECTED_PREFERENCE_GROUP_NAME = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 2);
    
    public static final Integer KEY_B_IS_PREFERENCE_CHANGE  = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 3);
    
    public static final Integer KEY_B_IS_NEED_UPLOAD        = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 4);
    
    public static final Integer KEY_I_GUIDE_TONE_VALUE      = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 5);
    
    public static final Integer KEY_I_CAR_MODEL_VALUE      = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 6);
    
    public static final Integer KEY_I_OLD_ROUTE_STYLE_VALUE      = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 7);
    
    public static final Integer KEY_I_OLD_ROUTE_SETTING_VALUE      = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 8);
    
    public static final Integer KEY_O_USER_INFO             = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 9);
    
    public static final Integer KEY_S_VALID_FAIL            = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 10);
    
    public static final Integer KEY_B_NEED_REVERT_USER_INFO = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 11);
    
    public static final Integer KEY_B_IS_LANGUAGE_CHANGE    = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 12);
    
    public static final Integer KEY_I_FTUE_LAUNCH_EVENT    = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 13);
    
    public static final Integer KEY_B_IS_FROM_SUBSCRIPTION    = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 14);

    public static final Integer KEY_B_IS_ROUTE_STYLE_CHANGE  = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 15);
    
    public static final Integer KEY_S_ACCOUNT_TYPE  = PrimitiveTypeCache.valueOf(STATE_PREFERENCE_BASE + 16);
    
    //ids
    public static final int ID_PREFERENCE_ITEM_BASE =STATE_PREFERENCE_BASE+ 500;
    public static final int ID_PREFERENCE_ITEM_MAX = STATE_PREFERENCE_BASE+600;
    
    public static final int ID_PREFERENCE_GROUP_BASE = STATE_PREFERENCE_BASE+700;
    public static final int ID_PREFERENCE_GROUP_MAX = STATE_PREFERENCE_BASE+800;
    
    public static final int ID_PREFERENCE_HOME_ADDRESS = STATE_PREFERENCE_BASE+5;
    public static final int ID_PREFERENCE_WORK_ADDRESS =STATE_PREFERENCE_BASE+ 6;
    
    public static final int ID_BODY_CONTAINER = STATE_PREFERENCE_BASE+1000;
    public static final int ID_MYACCOUNT_CONTAINER = STATE_PREFERENCE_BASE+1001;
    
    public static final int ID_COMPONENT_CAR_LIST =STATE_PREFERENCE_BASE+100001;
    public static final int ID_COMPONENT_MY_CAR =STATE_PREFERENCE_BASE+100002;
    public static final int ID_COMPONENT_HOMEWORK_CONTAINER =STATE_PREFERENCE_BASE+100003;
    public static final int ID_CANCEL_SUBSCRIPTION_BUTTON =STATE_PREFERENCE_BASE+100004;
    
    public static final String[] carNames = new String[]
    {
           "Arrow", 
           "Sports Car",
           "Classic Car",
           "Compact Car", 
           "Convertible",
           "Main Battle Tank",
           "Minivan", 
           "Monster Truck",
           "Motorcycle",
           "Muscle Car", 
           "Old School",
           "SUV",
           "Space Fighter",
   };
    
    public static final TnUiArgAdapter[] carImageAdapters = new TnUiArgAdapter[]
    {
        ImageDecorator.CAR_ARROW, 
        ImageDecorator.CAR_SPORTS,
        ImageDecorator.CAR_CLASSIC,
        ImageDecorator.CAR_COMPACT, 
        ImageDecorator.CAR_CONVERTIBLE,
        ImageDecorator.CAR_BATTLE_TANK,
        ImageDecorator.CAR_MINIVAN, 
        ImageDecorator.CAR_MONSTER_TRUCK,
        ImageDecorator.CAR_MOTORCYCLE,
        ImageDecorator.CAR_MUSCLE, 
        ImageDecorator.CAR_OLD_SCHOOL,
        ImageDecorator.CAR_SUV,
        ImageDecorator.CAR_SPACE_FIGHTER,
   };
    
    public static final String[] carModFileNames = new String[]
    {
           "arrow", 
           "sportsCar",
           "classic",
           "smallCar", 
           "convertable",
           "tank",
           "miniVan", 
           "monsterTruck",
           "motorCycle",
           "muscleCar", 
           "oldSchool",
           "SUV",
           "spaceShip",
   };
}

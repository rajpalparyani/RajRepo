/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * UpSellConstants.java
 *
 */
package com.telenav.module.upsell;

import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringTouring;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.ImageDecorator;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
interface IUpSellConstants extends ICommonConstants
{
    // -------------------------------------------------------------
    // State definition
    // -------------------------------------------------------------
    public static final int STATE_UP_SELL = STATE_USER_BASE + USER_BASE_UPSELL + 1;    
    public static final int STATE_INIT =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_UPSELL + 2);    
    public static final int STATE_UPSELL_OPTION_SUBMIT =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_UPSELL + 3);    
    public static final int STATE_UPSELL_LEARN_MORE =  STATE_USER_BASE + USER_BASE_UPSELL + 4;
    public static final int STATE_UPSELL_FINISH =  MASK_STATE_TRANSIENT | (STATE_USER_BASE + USER_BASE_UPSELL + 5);    
    public static final int STATE_LEARN_LIST = STATE_USER_BASE + USER_BASE_UPSELL + 6;
    public static final int STATE_LEARN_GALERY = STATE_USER_BASE + USER_BASE_UPSELL + 7;
    public static final int STATE_UPGRADE = STATE_USER_BASE + USER_BASE_UPSELL + 8;
    public static final int STATE_NON_SCOUT_UPSELL_PURCHASE_SUCCESS =MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_UPSELL + 9);
    public static final int STATE_CANCELLING_SUBSCRIPTION = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_UPSELL + 10);
    public static final int STATE_CANCEL_SUBSCRIPTION_FINISH = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_UPSELL + 11);
    public static final int STATE_CANCEL_EXCEPTION = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_UPSELL + 12);
    public static final int STATE_GET_UPSELL_OPTION_ERROR = MASK_STATE_TRANSIENT |(STATE_USER_BASE + USER_BASE_UPSELL + 13);
       
    // ------------------------------------------------------------
    // Command id definition
    // ------------------------------------------------------------
    
    //Please don't use 1-11 because it is for select learn item id
    public static final int CMD_SELECT_LEARN_LIST = CMD_USER_BASE + USER_BASE_UPSELL + 1;
    
    public static final int CMD_UPGRADE = CMD_USER_BASE + USER_BASE_UPSELL + 12;
    public static final int CMD_LEARN_MORE = CMD_USER_BASE + USER_BASE_UPSELL + 13;
    
    
    public static final int CMD_FIRST_OPTION_SELECT = CMD_USER_BASE + USER_BASE_UPSELL + 14;
    public static final int CMD_SECOND_OPTION_SELECT = CMD_USER_BASE + USER_BASE_UPSELL + 15;
    public static final int CMD_THIRD_OPTION_SELECT = CMD_USER_BASE + USER_BASE_UPSELL + 16;
    public static final int CMD_UPSELL_LEARN_MORE = CMD_USER_BASE + USER_BASE_UPSELL + 17;
    public static final int CMD_UPSELL_OPTION_CONFIRM = CMD_USER_BASE + USER_BASE_UPSELL + 18;
    public static final int CMD_NON_SCOUT_UPSELL_SUCCESS= EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 19;
    public static final int CMD_UPSELL_OPTION_SUBMIT = CMD_USER_BASE + USER_BASE_UPSELL + 20;
    
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------

    public static final int ACTION_EXIT_CHECK = ACTION_USER_BASE + USER_BASE_UPSELL + 1;
    public static final int ACTION_UPSELL_OPTION_SUBMIT = ACTION_USER_BASE + USER_BASE_UPSELL + 2;
    public static final int ACTION_CANCEL_SUBSCRIPTION = ACTION_USER_BASE + USER_BASE_UPSELL + 3; 
    public static final int ACTION_INIT = ACTION_USER_BASE + USER_BASE_UPSELL + 4;
    public static final int ACTION_CHECK_AMP_PROMOTE_TOAST = ACTION_USER_BASE + USER_BASE_UPSELL + 5;
    
    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------
    public static final int EVENT_MODEL_GOTO_PREV      = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 1;
    public static final int EVENT_MODEL_UPSELL_OPTIONS = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 2;
    public static final int EVENT_PURCHASE_SUCCESS = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 4;
    public static final int EVENT_MODEL_REQUEST_CARRIER_FINISH = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 5;
    public static final int EVENT_MODEL_NON_SCOUT_UPSELL_PURCHASE_SUCCESS = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 6;
    public static final int EVENT_MODEL_SCOUT_UPSELL_PURCHASE_SUCCESS = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 7;
    
    public static final int EVENT_MODEL_CANCEL_SUBSCRIPTION_SUCCESS  = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 8;
    public static final int EVENT_MODEL_REGISTRATE_CANCEL      = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 9;
    public static final int EVENT_MODEL_CANCEL_EXCEPTION      = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 10;
    public static final int EVENT_MODEL_GET_UPSELL_OPTION_ERROR      = EVENT_MODEL_USER_BASE + USER_BASE_UPSELL + 11;

    
    // -----------------------------------------------------------
    // key value between and in modules.
    // -----------------------------------------------------------
    public static final Integer KEY_V_UPSELL_OPTIONS = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_UPSELL + 1);
    public static final Integer KEY_O_UPSELL_OPTIONS_SELECT = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_UPSELL + 2);
    public static final Integer KEY_I_LEARN_ITEM_INDEX = PrimitiveTypeCache.valueOf(STATE_USER_BASE + USER_BASE_UPSELL + 3);

    public static final int[] LEARN_ITEM_TITLE_IDS = new int[]
    {
        IStringTouring.RES_NO_ADS,
        
        IStringTouring.RES_REAL_TIME_TRAFFIC,
        
        IStringTouring.RES_SPEED_CAMERA_ALERTS,
        
        IStringTouring.RES_ADVANCED_GUIDANCE,
        
        IStringTouring.RES_ALWAYS_THERE_NAVIGATION,
        
        IStringTouring.RES_CAR_CONNECT
     };
    
     public static final int[] LEARN_ITEM_DESC_IDS = new int[]
     {
         IStringTouring.RES_NO_ADS_DESC,
         
         IStringTouring.RES_REAL_TIME_TRAFFIC_DESC,
         
         IStringTouring.RES_SPEED_CAMERA_ALERTS_DESC,
         
         IStringTouring.RES_ADVANCED_GUIDANCE_DESC,
         
         IStringTouring.RES_ALWAYS_THERE_NAVIGATION_DESC,
         
         IStringTouring.RES_CAR_CONNECT_DESC
      };
     
     public static final int[] LEARN_ITEM_DESC_ON_GALLERY_IDS = new int[]
     {
         IStringTouring.RES_NO_ADS_DESC_ON_GALLERY,

         IStringTouring.RES_REAL_TIME_TRAFFIC_DESC_ON_GALLERY,
         
         IStringTouring.RES_SPEED_CAMERA_ALERTS_DESC_ON_GALLERY,
         
         IStringTouring.RES_ADVANCED_GUIDANCE_DESC_ON_GALLERY,
         
         IStringTouring.RES_ALWAYS_THERE_NAVIGATION_DESC_ON_GALLERY,
         
         IStringTouring.RES_CAR_CONNECT_DESC_ON_GALLERY
      };
     
     public static final TnUiArgAdapter[] LEARN_ITEM_FOCUSED_ICON_ADAPTERS = new TnUiArgAdapter[]
     {
         ImageDecorator.ICON_NO_ADS_FOCUSED,
         ImageDecorator.ICON_REAL_TIME_TRAFFIC_FOCUSED, 
         ImageDecorator.ICON_SPEED_CAMERA_ALERTS_FOCUSED,
         ImageDecorator.ICON_LANE_ASSIST_FOCUSED,
         ImageDecorator.ICON_ALWAYS_THERE_FOCUSED,
         ImageDecorator.ICON_CAR_CONNECT_FOCUSED,
      };
     
     public static final TnUiArgAdapter[] LEARN_ITEM_UNFOCUSED_ICON_ADAPTERS = new TnUiArgAdapter[]
     {
         ImageDecorator.ICON_NO_ADS_UNFOCUSED,
         ImageDecorator.ICON_REAL_TIME_TRAFFIC_UNFOCUSED, 
         ImageDecorator.ICON_SPEED_CAMERA_ALERTS_UNFOCUSED,
         ImageDecorator.ICON_LANE_ASSIST_UNFOCUSED,
         ImageDecorator.ICON_ALWAYS_THERE_UNFOCUSED,
         ImageDecorator.ICON_CAR_CONNECT_UNFOCUSED,
      };
     
     public static final TnUiArgAdapter[] LEARN_ITEM_GALERY_ADAPTERS = new TnUiArgAdapter[]
     {
         ImageDecorator.GALERY_NO_ADS, 
         ImageDecorator.GALERY_REAL_TIME_TRAFFIC, 
         ImageDecorator.GALERY_SPEED_CAMERA_ALERTS,
         ImageDecorator.GALERY_LANE_ASSIST,
         ImageDecorator.GALERY_ALWAYS_THERE,
         ImageDecorator.GALERY_CAR_CONNECT,
      };
     
     public static final String[] CAR_CONNECT_DESC = new String[]
     {
         "The following vehicles are Scout-compatible.",
         "More coming soon!",
         "      ",
         "Ford SYNC AppLink vehicles:",
         "Expedition E-Series",
         "F-150",
         "F-150 SVT Raptor",
         "Fiesta",
         "Fusion",
         "Fusion Hybrid",
         "Shelby GT500"
     };
     
     public static final int BTN_TOUCH_UP_COLOR = 0xffffffff;
     public static final int BTN_TOUCH_DOWN_COLOR = 0xFF241F21;
     public static final int UPSELL_TWO_OPTIONS = 2;
     public static final int UPSELL_THREE_OPTIONS = 3;
     public static final String DOLLAR_SIGN = "$";
     public static final String DOLLAR_ABBR = "USD";
     
} 

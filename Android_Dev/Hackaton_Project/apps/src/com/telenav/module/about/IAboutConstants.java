/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAboutConstants.java
 *
 */

package com.telenav.module.about;

import com.telenav.mvc.ICommonConstants;
import com.telenav.util.PrimitiveTypeCache;


/**
 *@author dfqin (dfqin@telenav.cn)
 *@date 2011-1-20
 */
interface IAboutConstants extends ICommonConstants
{
    //component ID
    public static final short ID_TOOLS_CONTENT_CONTAINER = 50;
    
	//state definition
	public static final int STATE_ABOUT_BASE = STATE_USER_BASE + USER_BASE_ABOUT;
	public static final int STATE_ROOT = STATE_ABOUT_BASE + 1;
	public static final int STATE_TOOLS = STATE_ABOUT_BASE + 2;
	public static final int STATE_INFO_COLLECTING = MASK_STATE_TRANSIENT | (STATE_ABOUT_BASE + 3);
	public static final int STATE_ABOUT_PAGE = STATE_ABOUT_BASE + 4;
	public static final int STATE_SUPPORT_INFO = STATE_ABOUT_BASE + 5;
	public static final int STATE_GETTING_PIN = MASK_STATE_TRANSIENT | (STATE_ABOUT_BASE + 6);
	public static final int STATE_PIN_GOT = MASK_STATE_TRANSIENT | (STATE_ABOUT_BASE + 7);
	public static final int STATE_TOOLS_REFRESH = MASK_STATE_TRANSIENT | (STATE_ABOUT_BASE + 8);
	public static final int STATE_TOS = STATE_ABOUT_BASE + 9;
	
	//action definition
	public static final int ACTION_ABOUT_BASE = ACTION_USER_BASE + USER_BASE_ABOUT;
	public static final int ACTION_ABOUT_INIT = ACTION_ABOUT_BASE + 1;
	public static final int ACTION_GETINFO = ACTION_ABOUT_BASE + 2;
	public static final int ACTION_FORGET_PIN = ACTION_ABOUT_BASE + 3;
	public static final int ACTION_REFRESH = ACTION_ABOUT_BASE + 4;
	
	//model event definition
	public static final int EVENT_MODEL_ABOUT_BASE = EVENT_MODEL_USER_BASE + USER_BASE_ABOUT;
	public static final int EVENT_MODEL_GOTO_TOOLS = EVENT_MODEL_ABOUT_BASE + 1; 
	public static final int EVENT_MODEL_FORGET_PIN = EVENT_MODEL_ABOUT_BASE + 2;
	
	//key value
	public static final Integer KEY_V_DIAGNOSTIC_VECTOR = PrimitiveTypeCache.valueOf(STATE_ABOUT_BASE +1);
	
	//cmd id definition
	public static final int CMD_PREFERENCE_BASE = CMD_USER_BASE + USER_BASE_PREFERENCE;
	public static final int CMD_MENU_ABOUT_ATT = CMD_PREFERENCE_BASE + 1;
	public static final int CMD_MENU_SUPPORT_INFO = CMD_PREFERENCE_BASE + 2;
	public static final int CMD_MENU_PIN_NUM = CMD_PREFERENCE_BASE + 3;
	public static final int CMD_MENU_DIAGNOSTIC = CMD_PREFERENCE_BASE + 4;
	public static final int CMD_MENU_REFRESH = CMD_PREFERENCE_BASE + 5;
	
	
	public static final String KEY_ABOUT_WEBVIEW = "about_webview";
	public static final String KEY_SUPPORTINFO_WEBVIEW = "supportinfo_webview";
	
	public static final String DEFAULT_SUPPORT_URL = "http://m.telenav.com/7.x/7.2.0/scout_us/en_US/support.html";;
	public static final String DEFAULT_TOS_URL ="http://m.telenav.com/7.x/7.2.0/scout_us/en_US/legal.html";
	
	
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 *
 */
package com.telenav.res;

/**
 *@author dfqin (dfqin@telenav.cn)
 *@date 2011-1-21
 */
public interface IStringAbout 
{
	public final static String FAMILY_ABOUT = "about";
	
	public final static int ABOUT_STR_BASE = 6000;
	public final static int DIAGNOSTIC_STR_BASE = 6100;
	
	//about related
	public final static int RES_UNRECOGNIZE = ABOUT_STR_BASE + 1;
	public final static int RES_ABOUT_TITLE_ABOUT = ABOUT_STR_BASE + 2;
	public final static int RES_ABOUT_ABOUT_ATT = ABOUT_STR_BASE + 3;
	public final static int RES_ABOUT_SUPPORT = ABOUT_STR_BASE + 4;
	public final static int RES_ABOUT_PIN_NUMBER = ABOUT_STR_BASE + 5;
	public final static int RES_ABOUT_DIAGNOSTIC_TOOLS = ABOUT_STR_BASE + 6;
	
	//diagnostic related
	public final static int RES_DIAG_PHONE_NUM = DIAGNOSTIC_STR_BASE + 1;
	public final static int RES_DIAG_SERVICE_CODE = DIAGNOSTIC_STR_BASE + 2;
	public final static int RES_DIAG_GPS_STATUS = DIAGNOSTIC_STR_BASE + 3;
	public final static int RES_DIAG_NET_STATUS = DIAGNOSTIC_STR_BASE + 4;
	public final static int RES_DIAG_LOCATION_PERMIT = DIAGNOSTIC_STR_BASE + 5;
	public final static int RES_DIAG_NETGUARD_SET = DIAGNOSTIC_STR_BASE + 6;
	public final static int RES_DIAG_DATA_ROAM_STATUS = DIAGNOSTIC_STR_BASE + 7;
	public final static int RES_DIAG_AIRPLANE_MODE = DIAGNOSTIC_STR_BASE + 8;
	public final static int RES_DIAG_DATA_SERVICE = DIAGNOSTIC_STR_BASE + 9;
	public final static int RES_DIAG_BATTERY_LEVEL = DIAGNOSTIC_STR_BASE + 10;
	
	public final static int RES_DIAG_GPS_LAT = DIAGNOSTIC_STR_BASE + 11;
	public final static int RES_DIAG_GPS_LON = DIAGNOSTIC_STR_BASE + 12;
	public final static int RES_DIAG_GPS_FIXTIME = DIAGNOSTIC_STR_BASE + 13;
	public final static int RES_DIAG_GPS_SATELLITENUM = DIAGNOSTIC_STR_BASE + 14;
	public final static int RES_DIAG_GPS_ACCURACY = DIAGNOSTIC_STR_BASE + 15;
	public final static int RES_DIAG_METERS = DIAGNOSTIC_STR_BASE + 16;
	public final static int RES_ENABLE = DIAGNOSTIC_STR_BASE + 17;
	public final static int RES_UNABLE = DIAGNOSTIC_STR_BASE + 18;
	public final static int RES_MENU_HOME = DIAGNOSTIC_STR_BASE + 19;
	public final static int RES_MENU_REFRESH = DIAGNOSTIC_STR_BASE + 20;
	public final static int RES_GETTING_GPS = DIAGNOSTIC_STR_BASE + 21;
	public final static int RES_REFRESHING = DIAGNOSTIC_STR_BASE + 22;
	public final static int RES_SENDING_REQ = DIAGNOSTIC_STR_BASE + 23;
	public final static int RES_SEND_PIN = DIAGNOSTIC_STR_BASE + 24;
	public final static int RES_PIN_ERROR = DIAGNOSTIC_STR_BASE + 25;
	public final static int RES_ABOUT_VERSION = DIAGNOSTIC_STR_BASE + 26;
	public final static int RES_ABOUT_MAP_DATA_VERSION = DIAGNOSTIC_STR_BASE + 27;
	
	
}

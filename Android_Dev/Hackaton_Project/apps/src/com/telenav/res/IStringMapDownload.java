/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IStringMapDownload.java
 *
 */
/**
 * 
 */
package com.telenav.res;

/**
 *@author wchshao
 *@date Dec 18, 2012
 */
public interface IStringMapDownload
{
    //=====================================map download family=====================================//
    public final static String FAMILY_MAP_DOWNLOAD = "mapdownload";
    
    public final static int MAP_DOWNLOAD_STR_BASE        = 130000;
    
    public final static int RES_HINT = MAP_DOWNLOAD_STR_BASE + 1;
    public final static int RES_DESCRIPTION_PREFIX = MAP_DOWNLOAD_STR_BASE + 2;
    public final static int RES_DOWNLOAD_SIZE_PREFIX = MAP_DOWNLOAD_STR_BASE + 3;
    public final static int RES_REACTIVATE = MAP_DOWNLOAD_STR_BASE + 4;
    public final static int RES_DELETE = MAP_DOWNLOAD_STR_BASE + 5;
    public final static int RES_START = MAP_DOWNLOAD_STR_BASE + 6;
    public final static int RES_PAUSE = MAP_DOWNLOAD_STR_BASE + 7;
    public final static int RES_CONTINUE = MAP_DOWNLOAD_STR_BASE + 8;
    public final static int RES_UPDATE = MAP_DOWNLOAD_STR_BASE + 9;
    public final static int RES_TITLE = MAP_DOWNLOAD_STR_BASE + 10;
    public final static int RES_DOWNLOADING_HINT = MAP_DOWNLOAD_STR_BASE + 11;
    public final static int RES_REPLACE = MAP_DOWNLOAD_STR_BASE + 12;
    public final static int RES_ERROR_UNKNOWN = MAP_DOWNLOAD_STR_BASE + 13;
    public final static int RES_ERROR_NO_WIFI = MAP_DOWNLOAD_STR_BASE + 14;
    public final static int RES_ERROR_NOT_ENOUGH_SPACE = MAP_DOWNLOAD_STR_BASE + 15;
    public final static int RES_ERROR_NO_SD_CARD = MAP_DOWNLOAD_STR_BASE + 16;
    public final static int RES_IN_PROGRESS = MAP_DOWNLOAD_STR_BASE + 17;
    public final static int RES_PLEASE_RESUME = MAP_DOWNLOAD_STR_BASE + 18;
    public final static int RES_CANCEL_CONFIRM = MAP_DOWNLOAD_STR_BASE + 19;
    public final static int RES_ERROR_NOT_INITED = MAP_DOWNLOAD_STR_BASE + 20;
    public final static int RES_DOWNLOADED_DATA_NOT_VALID = MAP_DOWNLOAD_STR_BASE + 21;
    public final static int RES_DOWNLOADED_DATA_UPDATE_AVAILABLE = MAP_DOWNLOAD_STR_BASE + 22;
    public final static int RES_RETRY_DOWNLOADING = MAP_DOWNLOAD_STR_BASE + 23;
    public final static int RES_REPLACE_CONFIRM = MAP_DOWNLOAD_STR_BASE + 24;
    public final static int RES_ERROR_NOT_ENOUGH_SPACE_DETAILED = MAP_DOWNLOAD_STR_BASE + 25;
    public final static int RES_ERROR_CHECK_YOUR_NETWORK = MAP_DOWNLOAD_STR_BASE + 26;
    public final static int RES_ERROR_INSTALL_FAIL = MAP_DOWNLOAD_STR_BASE + 27;
    public final static int RES_ERROR_STILL_DELETING = MAP_DOWNLOAD_STR_BASE + 28;
    public final static int RES_MAP_DOWNLOAD_SUCC = MAP_DOWNLOAD_STR_BASE + 29;
    public final static int RES_ERROR_NO_WIFI_START = MAP_DOWNLOAD_STR_BASE + 30;
}

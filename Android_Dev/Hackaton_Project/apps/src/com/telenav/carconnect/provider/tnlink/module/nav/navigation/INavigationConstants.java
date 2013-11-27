/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * INavigationConstants.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.nav.navigation;

/**
 *@author xiangli
 *@date 2012-2-24
 */
public interface INavigationConstants
{
    public static final Integer KEY_BASE    = 10000;
    public static final Integer KEY_I_ROUTENAME = KEY_BASE +1;
    public static final Integer KEY_O_RESULT_ROUTE  = KEY_BASE + 2;
    public static final Integer KEY_O_RESULT_NAVSTATUS  = KEY_BASE + 3;
    public static final Integer KEY_O_DEST_STOP         = KEY_BASE + 4;
    public static final Integer KEY_O_ADDRESS_ORI       = KEY_BASE + 5;
    public static final Integer KEY_O_ADDRESS_DEST      = KEY_BASE + 6;
    public static final Integer KEY_I_ERROR_CODE        = KEY_BASE + 7;
    public static final Integer KEY_S_ERROR_MSG       = KEY_BASE + 8;
    public static final Integer KEY_O_NAVCONTROLLER     = KEY_BASE + 9;
    public static final Integer KEY_O_LASTSEGMENT       = KEY_BASE + 10;
    public static final Integer KEY_B_ISROUTEFINISED    = KEY_BASE + 11;
    
    public static final int EVENT_BASE  = 20000;
    public static final int EVENT_NAVIGATION_START  = EVENT_BASE + 1;
    public static final int EVENT_RESULT_ROUTEDETAIL    = EVENT_BASE + 2;
    public static final int EVENT_RESULT_NAVSTATUS  = EVENT_BASE + 3;
    public static final int EVENT_RESULT_ARRIVED    = EVENT_BASE + 4;
    public static final int EVENT_RESULT_ERROR      = EVENT_BASE + 5;
    public static final int EVENT_RESULT_DEVIATION  = EVENT_BASE + 6;
    public static final int EVENT_RESULT_ROUTESUMMARY  = EVENT_BASE + 7;
    public static final int EVENT_RESULT_START_NAVIGATION   = EVENT_BASE + 8;
    
    public static final int ACTION_BASE  = 30000;
    public static final int ACTION_NAVIGATION_START = ACTION_BASE + 1;
    public static final int ACTION_NAVIGATION_STOP  = ACTION_BASE + 2;
    public static final int ACTION_NAVIGATION_START_INNAVIGATION    = ACTION_BASE + 3;
    public static final int ACTION_NAVIGATION_GUIDANCE              = ACTION_BASE + 4;
    public static final int ACTION_NAVIGATION_ROUTESUMMARY          = ACTION_BASE + 5;
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStringMaiTai.java
 *
 */
package com.telenav.sdk.plugin;

/**
 *@author qli
 *@date 2010-11-25
 */
public interface IStringPlugin
{
    // =====================================Plugin family=====================================//
    public final static String FAMILY_PLUGIN = "plugin";

    public final static int PLUGIN_STR_BASE = 10000;

    // Plugin res id:
    public final static int RES_DRIVETO         = PLUGIN_STR_BASE + 1;

    public final static int RES_VIEWMAP         = PLUGIN_STR_BASE + 2;

    public final static int RES_SEARCH          = PLUGIN_STR_BASE + 3;

    public final static int RES_SHARE           = PLUGIN_STR_BASE + 4;
    
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ParametersProxy.java
 *
 */
package com.telenav.app;

import com.telenav.navservice.NavServiceApi;


/**
 *@author qli
 *@date 2011-3-22
 */
public abstract class NavServiceManager
{
    public static Object mutex = new Object();
    
    protected static NavServiceApi navService;
    
    
    public static void init(NavServiceApi instance)
    {
        NavServiceManager.navService = instance;
    }
    
    public static NavServiceApi getNavService()
    {
        return navService;
    }
}

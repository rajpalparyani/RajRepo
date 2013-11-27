/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkRouteDataFactory.java
 *
 */
package com.telenav.data.datatypes.route;

import com.telenav.datatypes.route.RouteDataFactory;

/**
 *@author yren
 *@date 2012-11-8
 */
public class NavSdkRouteDataFactory extends RouteDataFactory
{
    protected static NavSdkRouteDataFactory instance = new NavSdkRouteDataFactory();

    public static NavSdkRouteDataFactory getInstance()
    {
        return instance;
    }

    public NavSdkRoute createNavSdkRoute()
    {
        return new NavSdkRoute();
    }
}

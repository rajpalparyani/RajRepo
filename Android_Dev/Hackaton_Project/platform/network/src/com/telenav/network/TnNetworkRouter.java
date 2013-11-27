/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnNetworkRouter.java
 *
 */
package com.telenav.network;

/**
 *@author yxyao
 *@date 2011-10-21
 */
public abstract class TnNetworkRouter
{
    /**
     * Get proxy for specified url.
     * @param url target url
     * @return
     */
    public abstract TnProxy getProxy(String url);
}

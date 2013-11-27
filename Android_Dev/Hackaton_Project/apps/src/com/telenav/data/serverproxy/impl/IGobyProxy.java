/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IGobyProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Hashtable;

/**
 *@author yning
 *@date 2013-5-17
 */
public interface IGobyProxy
{
    public void requestGobyEventDetail(Hashtable favorite, Hashtable recent);
}

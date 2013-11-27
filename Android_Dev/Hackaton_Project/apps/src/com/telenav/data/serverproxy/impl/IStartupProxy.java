/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStartupProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IStartupProxy
{
    public String synchronizeStartUp(long lastSyncTime);
}

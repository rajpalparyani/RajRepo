/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IBatchProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.data.serverproxy.RequestItem;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IBatchProxy
{
    public Vector getBatchItems();
    
    public void addBatchItem(RequestItem requestItem);
    
    public void send(int timeout);
}

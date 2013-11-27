/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ISyncSentAddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-1-21
 */
public interface ISyncSentAddressProxy
{
    public String syncSentAddress();
    
    public Vector getSentAddresses();
}

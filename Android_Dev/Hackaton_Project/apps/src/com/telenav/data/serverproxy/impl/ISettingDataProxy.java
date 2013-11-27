/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ISettingDateProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Hashtable;


/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-3-31
 */
public interface ISettingDataProxy
{
    public static final int SYNC_TYPE_UPLOAD = 1;
    public static final int SYNC_TYPE_DOWNLOAD = 2;
    public static final int SYNC_TYPE_FORCE_DOWNLOAD = 3;
    
    public String syncSettingData(String version, long timeStamp);
    
    public String syncSettingData(String version, long timeStamp, Hashtable settings);
    
}

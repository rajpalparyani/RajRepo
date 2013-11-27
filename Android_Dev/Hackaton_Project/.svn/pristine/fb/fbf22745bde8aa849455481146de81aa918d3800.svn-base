/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidNioManager.java
 *
 */
package com.telenav.nio.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.telenav.nio.TnNioManager;

/**
 * Provides access to the device's new IO operation at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Sep 2, 2010
 */
public class AndroidNioManager extends TnNioManager
{
    /**
     * construct a AndroidNioManager.
     */
    public AndroidNioManager()
    {
        
    }
    
    public ByteBuffer allocateDirect(int capacity)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
        buffer.order(ByteOrder.nativeOrder());
        
        return buffer;
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidNioManager.java
 *
 */
package com.telenav.nio.j2se;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.telenav.nio.TnNioManager;

/**
 * Provides access to the device's new IO operation at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Sep 2, 2010
 */
public class J2seNioManager extends TnNioManager
{
    /**
     * construct a J2seNioManager.
     */
    public J2seNioManager()
    {

    }

    public ByteBuffer allocateDirect(int capacity)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
        buffer.order(ByteOrder.nativeOrder());

        return buffer;
    }
}
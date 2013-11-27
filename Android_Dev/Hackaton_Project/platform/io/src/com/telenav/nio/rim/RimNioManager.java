/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimNioManager.java
 *
 */
package com.telenav.nio.rim;

import java.nio.ByteBuffer;

import com.telenav.nio.TnNioManager;

/**
 * Provides access to the device's new IO operation at rim platform. <br />
 * JSR 239 does not support the ByteOrder class or the order methods. The inital order of a byte buffer is the platform
 * byte order.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-8
 */
public class RimNioManager extends TnNioManager
{
    /**
     * construct a RimNioManager.
     */
    public RimNioManager()
    {

    }

    public ByteBuffer allocateDirect(int capacity)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);

        return buffer;
    }
}

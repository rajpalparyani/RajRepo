/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnNioManager.java
 *
 */
package com.telenav.nio;

import java.nio.ByteBuffer;

/**
 * Provides access to the device's new IO operation.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Sep 2, 2010
 */
public abstract class TnNioManager
{
    private static TnNioManager nioManager;
    private static int initCount;
    
    /**
     * Retrieve the instance of io manager.
     * 
     * @return {@link TnNioManager}
     */
    public static TnNioManager getInstance()
    {
        return nioManager;
    }
    
    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param ioMngr This manager is native manager of platforms. Such as {@link AndroidNioManager} etc.
     */
    public synchronized static void init(TnNioManager nioMngr)
    {
        if(initCount >= 1)
            return;
        
        nioManager = nioMngr;
        initCount++;
    }
    
    public abstract ByteBuffer allocateDirect(int capacity);
}

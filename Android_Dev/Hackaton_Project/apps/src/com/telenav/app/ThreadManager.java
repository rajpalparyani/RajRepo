/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ThreadManager.java
 *
 */
package com.telenav.app;

import com.telenav.threadpool.ThreadPool;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class ThreadManager
{
    public final static int TYPE_COMM_REQUEST = 0;

    public final static int TYPE_COMM_BACKUP_REQUEST = 1;

    public final static int TYPE_APP_ACTION = 2;

    public final static int TYPE_AUDIO_PLAYER = 3;
    
    public final static int TYPE_RECORD_PLAYER = 4;
    
    public final static int TYPE_NAV_CORE_ENGINE = 5;
    
    public final static int TYPE_NAV_CORE_EVENT = 6;
    
    /**
     * Thread used for TrafficAlertEngine
     */
    public final static int TYPE_TRAFFIC = 7;
    
    public final static int TYPE_OPENGL_BUILD_THREAD = 8;
    
    public final static int TYPE_BROWSER_PRELOAD = 9;
    
    public final static int TYPE_NETWORK_STATUS_NOTIFIER = 10;
    
    
    private final static int TYPE_SIZE = 11;
    
    private static ThreadPool[] pools = new ThreadPool[TYPE_SIZE];

    public static ThreadPool getPool(int type)
    {
        if (pools[type] == null)
        {
            int threadCount = 1;
            if(type == TYPE_APP_ACTION)
            {
                threadCount = 3;
            }
            pools[type] = new ThreadPool(threadCount, false, type);
            pools[type].start();
        }

        return pools[type];
    }

    public static void stopAll()
    {
        for (int i = 0; i < pools.length; i++)
        {
            if (pools[i] != null)
            {
                pools[i].stop();
                pools[i] = null;
            }
        }
    }

    public static void cancelAll()
    {
        for (int i = 0; i < pools.length; i++)
        {
            if (pools[i] != null)
            {
                pools[i].cancelAll();
            }
        }
    }
}

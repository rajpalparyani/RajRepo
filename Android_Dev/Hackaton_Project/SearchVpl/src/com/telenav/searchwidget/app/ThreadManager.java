/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ThreadManager.java
 *
 */
package com.telenav.searchwidget.app;

import com.telenav.threadpool.ThreadPool;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class ThreadManager
{
    public final static int TYPE_COMM_REQUEST = 0;

    public final static int TYPE_COMM_BACKUP_REQUEST = 1;

    private final static int TYPE_SIZE = 2;
    
    private static ThreadPool[] pools = new ThreadPool[TYPE_SIZE];

    public static ThreadPool getPool(int type)
    {
        if (pools[type] == null)
        {
            int threadCount = 1;
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

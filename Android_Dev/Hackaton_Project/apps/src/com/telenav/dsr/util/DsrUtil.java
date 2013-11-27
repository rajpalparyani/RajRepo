/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * DsrUtil.java
 *
 */
package com.telenav.dsr.util;

import java.util.Vector;

import com.telenav.dsr.DsrManager;
import com.telenav.dsr.IRecordEventListener;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 14, 2011
 */

public class DsrUtil
{
    public static void recordStatusUpdate(ThreadPool threadPool, final int event, final Object eventData)
    {
        final Vector listeners = DsrManager.getInstance().getRecordStatusListeners();
        if(listeners == null || listeners.size() == 0)
            return;
        
        IJob job = new IJob()
        {
            public boolean isRunning()
            {
                return true;
            }
            
            public boolean isCancelled()
            {
                return false;
            }
            
            public void execute(int handlerID)
            {
                int size = listeners.size();
                
                for(int i = 0 ; i < size ; i ++)
                {
                    IRecordEventListener listener = (IRecordEventListener)listeners.elementAt(i);
                    listener.recordStatusUpdate(event, eventData);
                }
            }
            
            public void cancel()
            {
                
            }
        };
        threadPool.addJob(job);
        threadPool.start();

    }
    
    public static void notifyVolumeChanged(ThreadPool threadPool, final int volumeLevel)
    {
        IJob job = new IJob()
        {
            public boolean isRunning()
            {
                return true;
            }
            
            public boolean isCancelled()
            {
                return false;
            }
            
            public void execute(int handlerID)
            {
                DsrManager.getInstance().notifyVolumeChanged(volumeLevel);
            }
            
            public void cancel()
            {
                
            }
        };
        threadPool.addJob(job);
        threadPool.start();

    }
}

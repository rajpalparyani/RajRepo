/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NetworkStatusHandler.java
 *
 */
package com.telenav.data.serverproxy;

import java.util.Vector;

import com.telenav.app.ThreadManager;
import com.telenav.logger.Logger;
import com.telenav.threadpool.IJob;

/**
 *@author bduan
 *@date 2013-3-12
 */
public class NetworkStatusManager
{
    private static final String TAG = "NetworkStatusManager";
    
    protected Vector<INetworkStatusListener> listeners = new Vector<INetworkStatusListener>();
    
    protected boolean isConnected = true;
    
    private static class InnerNetworkStatusHandler
    {
        private static NetworkStatusManager instance = new NetworkStatusManager();
    }
    
    public static NetworkStatusManager getInstance()
    {
        return InnerNetworkStatusHandler.instance;
    }

    private NetworkStatusManager()
    {
        
    }
    
    public synchronized boolean isListenerExist(INetworkStatusListener listener)
    {
        if(listeners != null && listeners.contains(listener))
        {
            return true;
        }
        
        return false;
    }
    
    public synchronized void addStatusListener(INetworkStatusListener listener)
    {
        if(listeners != null &&!listeners.contains(listener) )
        {
            listeners.add(listener);
        }
    }
    
    public synchronized void removeStatusListener(INetworkStatusListener listener)
    {
        if(listeners != null)
        {
            listeners.remove(listener);
        }
    }
    
    public boolean isConnected()
    {
        return isConnected;
    }
    
    public void networkStatusUpdate(final boolean isConnected)
    {
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, TAG, " isConnected : " + isConnected);
        }
        
        this.isConnected = isConnected;
        
        ThreadManager.getPool(ThreadManager.TYPE_NETWORK_STATUS_NOTIFIER).addJob(new IJob()
        {
            private boolean isRunning = false;
            
            public boolean isRunning()
            {
                return isRunning;
            }
            
            public boolean isCancelled()
            {
                return false;
            }
            
            public void execute(int handlerID)
            {
                isRunning = true;
                if (listeners != null && listeners.size() > 0)
                {
                    for (int i = 0 ; i < listeners.size() ; i ++)
                    {
                        INetworkStatusListener handler = listeners.elementAt(i);
                        handler.statusUpdate(isConnected);
                    }
                }
                isRunning = false;
            }
            
            public void cancel()
            {
            }
        });
    }
    
    public void release()
    {
        if (listeners != null)
        {
            listeners.removeAllElements();
            listeners = null;
        }
    }
}

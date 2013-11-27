/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * StuckMonitorService.java
 *
 */
package com.telenav.ui.citizen.map;

import java.util.Vector;

import com.telenav.logger.Logger;

/**
 * @author wchshao
 * @date May 8, 2013
 */
public class StuckMonitorService implements Runnable
{
    private static StuckMonitorService stuckMonitorService;
    private long checkInterval = 5 * 1000;
    private boolean isStarted;
    protected Object serviceMutex = new Object();
    protected Thread callbackThread;
    protected Vector stuckListeners;
    
    public synchronized static StuckMonitorService getInstance()
    {
        if (stuckMonitorService == null)
        {
            stuckMonitorService = new StuckMonitorService();
        }
        return stuckMonitorService;
    }
    
    
    public void start()
    {
        synchronized (serviceMutex)
        {
            if (isStarted)
            {
                return;
            }

            isStarted = true;

            if (this.callbackThread == null || !this.callbackThread.isAlive())
            {
                this.callbackThread = new Thread(this, "StuckMonitorService-Thread");
                this.callbackThread.start();
            }
        }
    }
    
    public void stop()
    {
        synchronized (serviceMutex)
        {
            if(!isStarted)
            {
                return;
            }
            
            isStarted = false;
            serviceMutex.notifyAll();
            
            this.removeAllListeners();
        }
    }

    protected void restart(boolean quickly)
    {
        this.stop();

        if(!quickly)
        {
            try
            {
                Thread.sleep(1500);
            }
            catch (InterruptedException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }

        this.start();
    }
    
    public void removeAllListeners()
    {
        if(this.stuckListeners == null)
            return;
        
        this.stuckListeners.removeAllElements();
    }
    
    private void notifyListeners()
    {
        if (stuckListeners != null)
        {
            for (int i = 0; i < stuckListeners.size(); i++)
            {
                try
                {
                    IStuckListener dayNightListener = (IStuckListener) stuckListeners.elementAt(i);
                    dayNightListener.checkStuckStatus();
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
        }
    }
    
    public void addListener(IStuckListener stuckListener)
    {
        if (this.stuckListeners == null)
        {
            this.stuckListeners = new Vector();
        }

        this.stuckListeners.addElement(stuckListener);
    }
    
    public void removeListener(IStuckListener stuckListener)
    {
        if (this.stuckListeners == null)
            return;

        this.stuckListeners.removeElement(stuckListener);
    }

    @Override
    public void run()
    {
        while (isStarted)
        {
            try
            {
                if (!isStarted)
                {
                    break;
                }
                notifyListeners();

                if (!isStarted)
                {
                    break;
                }
                synchronized (serviceMutex)
                {
                    serviceMutex.wait(checkInterval);
                }
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }
    
    public interface IStuckListener
    {
        public void checkStuckStatus();
    }
}

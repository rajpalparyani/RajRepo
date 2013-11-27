/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnUiTimer.java
 *
 */
package com.telenav.tnui.core;

import java.util.Vector;

import com.telenav.logger.Logger;

/**
 * Register ui timer event here.
 * <br />
 * if the receiver is not null, will send out the TnPrivateEvent.EVENT_TIMER every 0.5 second, and you can handle kinds of logic
 * such as repaint, update progress by listening the EVENT_TIMER event.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public class TnUiTimer implements Runnable
{
    public final static int DEFAULT_INTERVAL = 200;
    public final static int TIMER_OBJECT_MAX_COUNT = 5;
    private static TnUiTimer instance;

    private Vector timerObjects;
    
    private Object mutex = new Object();

    private boolean isEnabled = false;

    private TnUiTimer()
    {
        timerObjects = new Vector();
    }

    /**
     * Retrieve the ui timer instance.
     * 
     * @return {@link TnUiTimer}
     */
    public synchronized static TnUiTimer getInstance()
    {
        if(instance == null)
        {
            instance = new TnUiTimer();
            Thread t = new Thread(instance);
            t.start();
        }
        
        return instance;
    }

    /**
     * add the receiver of ui time event.
     * 
     * use default interval 500.
     * 
     * @param tnComponent the receiver of time event.
     */
    public void addReceiver(AbstractTnComponent tnComponent)
    {
        addReceiver(tnComponent, DEFAULT_INTERVAL);
    }
    
    /**
     * add the receiver of ui time event.
     * 
     * add the interval for timer, it should > 50 millisecond. 
     * 
     * @param tnComponent the receiver of time event.
     */
    public synchronized void addReceiver(AbstractTnComponent tnComponent, int interval)
    {
        if(interval < 20)
            throw new RuntimeException("The interval should >= 20 millisecond");
        
        for(int i = 0; i < this.timerObjects.size(); i++)
        {
            UiTimerObject timerObject = (UiTimerObject)this.timerObjects.elementAt(i);
            if(timerObject.tnComponent.equals(tnComponent))
            {
                return;
            }
        }
        
        if(this.timerObjects.size() > TIMER_OBJECT_MAX_COUNT)
        {
            this.timerObjects.removeElementAt(0);
        }
        
        UiTimerObject uiTimerObject = new UiTimerObject();
        uiTimerObject.tnComponent = tnComponent;
        uiTimerObject.interval = interval;
        
        this.timerObjects.addElement(uiTimerObject);

        synchronized (mutex)
        {
            mutex.notifyAll();
        }
    }
    
    /**
     * remove the receiver from ui timer.
     * 
     * @param tnComponent the receiver of time event.
     */
    public synchronized void removeReceiver(AbstractTnComponent tnComponent)
    {
        for(int i = 0; i < this.timerObjects.size(); i++)
        {
            UiTimerObject timerObject = (UiTimerObject)this.timerObjects.elementAt(i);
            if(timerObject.tnComponent.equals(tnComponent))
            {
                this.timerObjects.removeElementAt(i);
                break;
            }
        }
    }

    /**
     * enable/disable the ui timer event.
     * 
     * @param isEnabled true is enabled, and false is disabled.
     */
    public void enable(boolean isEnabled)
    {
        this.isEnabled = isEnabled;

        synchronized (mutex)
        {
            mutex.notifyAll();
        }
    }

    public void run()
    {
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_PRIVATE_EVENT, null);
        uiEvent.setPrivateEvent(TnPrivateEvent.EVENT_TIMER);

        long minInterval = 100;
        
        while (true)
        {
            try
            {
                if (!isEnabled)
                {
                    synchronized (mutex)
                    {
                        mutex.wait(6 * 60000);
                    }
                }

                if (this.timerObjects.size() > 0)
                {
                    if (isEnabled)
                    {
                        for(int i = 0; i < this.timerObjects.size(); i++)
                        {
                            UiTimerObject timerObject = (UiTimerObject)this.timerObjects.elementAt(i);
                            if((System.currentTimeMillis() - timerObject.lastTime) >= timerObject.interval)
                            {
                                uiEvent.setComponent(timerObject.tnComponent);
                                timerObject.tnComponent.handleUiEvent(uiEvent);
                                timerObject.lastTime = System.currentTimeMillis();
                            }
                            if(minInterval > timerObject.interval)
                            {
                                minInterval = timerObject.interval;
                            }
                        }
                    }
                    
                    if(minInterval <= 50)
                    {
                        minInterval = 50;
                    }
                    
                    synchronized (mutex)
                    {
                        mutex.wait(minInterval);
                    }
                }
                else
                {
                    synchronized (mutex)
                    {
                        mutex.wait(30000);
                    }
                }
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }
    
    static class UiTimerObject
    {
        AbstractTnComponent tnComponent;
        int interval;
        long lastTime;
    }
}

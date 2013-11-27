/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavEventJob.java
 *
 */
package com.telenav.nav;

import com.telenav.logger.Logger;
import com.telenav.nav.event.NavEndEvent;
import com.telenav.nav.event.NavEvent;
import com.telenav.threadpool.IJob;
import com.telenav.util.Queue;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-14
 */
public class NavEventJob implements IJob
{
    protected Queue eventQueue;
    protected boolean isCancelled;
    protected boolean isRunning;
    protected boolean isNavEngineStoped = false;
    
    public NavEventJob()
    {
        this.eventQueue = new Queue();
    }
    
    protected void postEvent(NavEvent event)
    {
        eventQueue.push(event);
    }
    
    public void cancel()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "NYTest --> NavEventJob is cancelled ! " + this);
        isCancelled = true;
    }

    public void execute(int handlerID)
    {
        isRunning = true;
        
        while (!isCancelled())
        {
            NavEvent event = sendEvent();
            if(event instanceof NavEndEvent)
            {
                break;
            }
        }
        
        if(!isNavEngineStoped)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "NYTest --> NavEventJob stop NavEngine ! " + this);
            NavEngine.getInstance().stop();
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "NYTest --> NavEventJob finished finally ! " + this);
        isRunning = false;
    }
    
    public void setIsNavEngineStoped(boolean isNavEngineStoped)
    {
        this.isNavEngineStoped = isNavEngineStoped;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        return this.isRunning;
    }
    
    NavEvent sendEvent()
    {
        NavEvent event = null;
        try
        {
            event = (NavEvent) eventQueue.pop(1000);
            if(event == null)
                return event;
            
            NavEngine.getInstance().handleEvent(event);
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e, "[Navigation] - Just ignore this error.");
        }
        
        return event;
    }
}

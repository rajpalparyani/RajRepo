/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Notifier.java
 *
 */
package com.telenav.threadpool;

import java.util.Vector;

/**
 * @author alexg
 *
 */
public class Notifier implements IJob
{
	private boolean isCancelled = false;
	private boolean isRunning = false;
	private Object mutex = new Object();
	
	private int interval = 400;
	
	private Vector listeners = new Vector(); 
	private ThreadPool threadPool;
	
	private static Notifier notifier = new Notifier();
	
	private boolean isForcePause;
	
	private Notifier()
	{
	    
	}
	
	public static Notifier getInstance()
	{
	    return notifier;
	}
	
	public void start()
    {
        synchronized (mutex)
        {
            if (threadPool == null)
            {
                threadPool = new ThreadPool(1, false, (byte) 10);
                threadPool.start();

                threadPool.addJob(this);
            }
        }
    }
	
	public void stop()
    {
        synchronized (mutex)
        {
            if (threadPool != null)
            {
                threadPool.cancelAll();
                threadPool.stop();

                this.removeAllListener();
            }
        }
    }
	
	public void pause()
    {
        this.isForcePause = true;
    }

    public void resume()
    {
        synchronized (mutex)
        {
            this.isForcePause = false;
            mutex.notifyAll();
        }
    }
	
	public void addListener(INotifierListener listener)
	{
		synchronized (mutex)
		{
			if (!listeners.contains(listener))
			{
				listeners.addElement(listener);
			}
			
			mutex.notify();
		}
	}
	
	public void removeListener(INotifierListener listener)
	{
		synchronized (mutex)
		{
			listeners.removeElement(listener);
		}
	}
	
	public void removeAllListener()
	{
	    synchronized (mutex)
	    {
	        listeners.removeAllElements();
	    }
	}
	
	/* (non-Javadoc)
	 * @see com.telenav.framework.thread.IJob#execute(int)
	 */
	public void execute(int handlerID)
	{
		this.isRunning = true;

        Vector listeners = new Vector();
		while (!this.isCancelled)
		{
            long minInterval = interval;
            
            listeners.removeAllElements();            
			synchronized (mutex)
			{
				for (int i = 0; i < this.listeners.size(); i++)
				{
					listeners.addElement(this.listeners.elementAt(i));
				}
			}

			checkForPause();
			
			for (int i = 0; i < listeners.size(); i++)
			{
				INotifierListener listener = (INotifierListener) listeners.elementAt(i);
				long timestamp = System.currentTimeMillis();
	            try
	            {
	            	long lastTimestamp = listener.getLastNotifyTimestamp();
	            	if (timestamp > lastTimestamp + listener.getNotifyInterval())
	            	{
	            		listener.notify(timestamp);
	            		listener.setLastNotifyTimestamp(timestamp);
	            	}
                    
                    if (minInterval > listener.getNotifyInterval())
                        minInterval = listener.getNotifyInterval();
	            }
	            catch (Throwable e)
	            {
	            	e.printStackTrace();
	            }
			}
			
            try
            {
                synchronized (mutex)
                {
                	if (!this.isCancelled)
                	{
                		if (listeners.size() == 0)
                		{
                			mutex.wait(30000); // safety net - never wait forever          			
                		}
                		else
                		{
                		    if(minInterval <= 0)
                		        minInterval = 100;
                		    
                			mutex.wait(minInterval);
                		}
                	}
                }
            }
            catch (Throwable e)
            {
            	
            }
		}
		
		this.isRunning = false;
	}

	/* (non-Javadoc)
	 * @see com.telenav.framework.thread.IJob#cancel()
	 */
	public void cancel()
	{
		synchronized (mutex)
		{
			this.isCancelled = true;
			mutex.notify();
		}
	}

	/* (non-Javadoc)
	 * @see com.telenav.framework.thread.IJob#isCancelled()
	 */
	public boolean isCancelled()
	{
		return isCancelled;
	}

	/* (non-Javadoc)
	 * @see com.telenav.framework.thread.IJob#isRunning()
	 */
	public boolean isRunning()
	{
		return this.isRunning;
	}
	
	public void setNotifierInterval(int interval)
	{
		this.interval = interval;
	}

    private void checkForPause()
    {
        if (isForcePause)
        {
            try
            {
                synchronized (mutex)
                {
                    mutex.wait(0);
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }

    public Vector getAllListeners()
    {        
        return this.listeners;
    }

}

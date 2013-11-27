/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Handler.java
 *
 */
package com.telenav.threadpool;

import com.telenav.logger.Logger;

/**
 * @author alexg
 *
 */
class Handler implements Runnable
{
    final static public long WAIT_TIMEOUT = 600000; // 10 minutes

//  static private int threadCounter = 0;

    protected Object handlerMutex = new Object();
    protected Object currJobMutex = new Object();

    private             ThreadPool pool;
//  protected           int id;
    private boolean     isRunning = true;
    private boolean     isAlive = false;

    public int handlerID = 0;
    
    protected IJob currJob;

    protected Handler(ThreadPool pool, int handlerID, int poolID)
    {
        this.pool = pool;
//      threadCounter++;
//      this.id = threadCounter;
        this.handlerID = handlerID;
    }

    protected void stop()
    {
        isRunning = false;
        cancelJob();        
        synchronized(handlerMutex)
        {
            handlerMutex.notify();
        }
    }


    protected void cancelJob()
    {
        if (this.currJob != null)
        {
            synchronized (currJobMutex)
            {
                if (this.currJob != null)
                {
                    this.currJob.cancel();
                }
            }
        }
    }

    public void run()
    {
        isAlive = true;
        while (isRunning)
        {
            try
            {
                IJob job = pool.getNextJob();

                synchronized (currJobMutex)
                {
                    currJob = job;
                }

                if (currJob == null)
                {
                    synchronized (handlerMutex)
                    {
                        pool.addToWaitQueue(this);

                        long tooLate = System.currentTimeMillis() + WAIT_TIMEOUT;
                        try
                        {
                            handlerMutex.wait(WAIT_TIMEOUT);    // safety net - don't wait forever
                        }
                        catch (Throwable ie)
                        {
                            Logger.log(this.getClass().getName(), ie);
                            pool.removeFromWaitQueue(this);
                        }
                        if (!isRunning)
                            break;

                        if (System.currentTimeMillis() > tooLate)
                        {
                            pool.removeFromWaitQueue(this);
                        }
                    }
                }

                if (isRunning)
                {
                    if (currJob == null)
                    {
                        job = pool.getNextJob();
                        synchronized (currJobMutex)
                        {
                            currJob = job;
                        }
                    }

                    if (currJob != null && !currJob.isCancelled())
                    {
                        currJob.execute(handlerID);
                    }
                }
            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e, "handler id is: " + handlerID + ", job: " + currJob);
            }
        }
        isAlive = false;
    }
    
    protected boolean isAlive()
    {
        return isAlive;
    }
        
    protected void start()
    {
        isRunning = true;
    }
    
}

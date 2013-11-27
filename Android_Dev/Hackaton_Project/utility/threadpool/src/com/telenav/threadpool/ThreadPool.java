/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ThreadPool.java
 *
 */
package com.telenav.threadpool;

import java.util.Vector;

/**
 * @author alexg
 *
 */
public class ThreadPool
{
    protected Vector jobQueue = new Vector();
    protected Vector waitPool = new Vector();

    private Object jobMutex = new Object();
    private Object poolMutex = new Object();

    // this vector is added later to support cancel functionality
    private Vector handlers = new Vector();

    private int poolCapacity;
    private boolean skipPending;
    
    private boolean isStarted = false;
    
    private int poolID;
    
    public ThreadPool(int poolSize, boolean skipPending, int poolID)
    {
        this.poolCapacity = poolSize;
        this.skipPending = skipPending;
        
        this.poolID = poolID;
        
        for (int i = 0; i < poolSize; i++)
        {
            Handler handler = new Handler(this, i, poolID);
            this.handlers.addElement(handler);
        }
    }
    
    private static String NAME_PREFIX = "---Handler---";
    private static String DOT = ".";

    public int getPoolSize()
    {
        return poolCapacity;
    }
    
    public void start()
    {
        synchronized (jobMutex)
        {
            if (!isStarted)
            {
                for (int i = 0; i < handlers.size(); i++)
                {
                    Handler handler = (Handler) handlers.elementAt(i);
                    handler.start();
                    String name = NAME_PREFIX + poolID + DOT + handler.handlerID;
                    Thread t = new Thread(handler, name);
                    t.start();
                }
                isStarted = true;
            }
        }
    }

    public void stop()
    {
        synchronized (jobMutex)
        {
            if (isStarted)
            {
                for (int i = 0; i < this.handlers.size(); i++)
                {
                    Handler handler = (Handler) this.handlers.elementAt(i);
                    handler.stop();
                }
            }
            isStarted = false;
        }
    }
    
    public boolean isIdle()
    {
        boolean isIdle = true;

        if (!jobQueue.isEmpty() || waitPool.isEmpty() || waitPool.size() < this.poolCapacity) isIdle = false;

            return isIdle;
    }

    public void cancelAll()
    {
        // don't allow new jobs until we cancel all current jobs
        synchronized (jobMutex)
        {
            this.jobQueue.removeAllElements();
            for (int i = 0; i < this.handlers.size(); i++)
            {
                Handler handler = (Handler) this.handlers.elementAt(i);
                handler.cancelJob();
            }
        }
    }

    public int addJob(IJob job)
    {
        return insertJob(job, false);
    }

    public int insertJobInFront(IJob job)
    {
        return insertJob(job, true);        
    }

    private int insertJob(IJob job, boolean isInFront)
    {
        int poolSize;
        synchronized (jobMutex)
        {
            if (isInFront)
            {
                this.jobQueue.insertElementAt(job, 0);              
            }
            else
            {
                this.jobQueue.addElement(job);
            }
            poolSize = this.jobQueue.size();
        }

        Handler handler = null;
        synchronized (poolMutex)
        {
            if (this.waitPool.size() > 0)
            {
                handler = (Handler) this.waitPool.elementAt(0);
            }
        }

        if (handler != null)
        {
            synchronized (handler.handlerMutex)
            {
                handler.handlerMutex.notify();
                removeFromWaitQueue(handler);
            }
        }

        return poolSize;
    }

    protected void addToWaitQueue(Handler handler)
    {
        synchronized (this.poolMutex)
        {
            if (!this.waitPool.contains(handler))
            {
                this.waitPool.addElement(handler);
            }
        }
    }

    protected void removeFromWaitQueue(Handler handler)
    {
        synchronized (this.poolMutex)
        {
            this.waitPool.removeElement(handler);
        }
    }

    public Vector getCurrentJobs()
    {
        Vector currJobs = new Vector();
        
        for(int i = 0; i < this.handlers.size(); i++)
        {
            Handler handler = (Handler)this.handlers.elementAt(i);
            if(handler.currJob != null)
            {
                currJobs.addElement(handler.currJob);
            }
        }
        
        return currJobs;
    }
    
    public IJob getNextJob()
    {
        IJob job = null;
        synchronized (jobMutex)
        {
            if (this.jobQueue.size() > 0)
            {
                if (this.skipPending)
                {
                    job = (IJob) this.jobQueue.lastElement();
                    this.jobQueue.removeAllElements();
                }
                else
                {
                    job = (IJob) this.jobQueue.firstElement();
                    this.jobQueue.removeElementAt(0);
                }
            }
        }

        return job;
    }
    
    public void setSkipPending(boolean skipPending)
    {
        this.skipPending = skipPending;
    }
    
    public int getQueueSize()
    {
        return this.jobQueue.size();
    }
    
    public Vector getPendingJobs()
    {
        Vector v = new Vector();
        
        synchronized (jobMutex)
        {
            for (int i = 0; i < jobQueue.size(); i++)
            {
                v.addElement(jobQueue.elementAt(i));
            }
        }       
        return v;
    }

    public void removePendingJob(IJob job)
    {
        synchronized (jobMutex)
        {
            for (int i = 0; i < jobQueue.size(); i++)
            {
                jobQueue.removeElement(job);
            }
        }
    }
    
    public boolean isAlive()
    {
        synchronized (jobMutex)
        {
            for (int i = 0; i < this.handlers.size(); i++)
            {
                Handler handler = (Handler) this.handlers.elementAt(i);
                if (handler.isAlive()) return true;
            }
        }

        return false;
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * HandlerTest.java
 *
 */
package com.telenav.threadpool;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-29
 */
public class HandlerTest extends TestCase
{
    Handler handler;
    ThreadPool pool;
    int handlerId;
    protected void setUp() throws Exception
    {
        pool = new ThreadPool(1, false, 1);
        handlerId = 5;
        handler = new Handler(pool, handlerId, 0);
        super.setUp();
    }
    
    public void testStop()
    {
        Thread t = new Thread(handler);
        t.start();
        while(!(t.getState() == Thread.State.TIMED_WAITING))
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        assertTrue(handler.isAlive());
        handler.stop();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertFalse(handler.isAlive());
    }
    
    public void testCancelJob()
    {
        IJob testJob = new TestJob();
        handler.currJob = testJob;
        assertFalse(testJob.isCancelled());
        
        handler.stop();
        assertTrue(testJob.isCancelled());
    }
    
    public void testRun_jobNull()
    {
        Thread t = new Thread(handler);
        t.start();
        while(!(t.getState() == Thread.State.TIMED_WAITING))
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        assertTrue(handler.isAlive());
        synchronized(handler.handlerMutex)
        {
            handler.handlerMutex.notify();
        }
        
        while(!(t.getState() == Thread.State.TIMED_WAITING))
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        handler.stop();
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertFalse(handler.isAlive());
    }
    
    public void testRun_WithJob()
    {
        TestJob job = new TestJob();
        assertEquals(-1, job.handlerId);
        pool.addJob(job);
        Thread t = new Thread(handler);
        t.start();
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertEquals(handlerId, job.handlerId);
        
        while(!(t.getState() == Thread.State.TIMED_WAITING))
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        handler.stop();
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertFalse(handler.isAlive());
    }
    
    public void testIsAlive()
    {
        Thread t = new Thread(handler);
        t.start();
        while(!(t.getState() == Thread.State.TIMED_WAITING))
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        assertTrue(handler.isAlive());
        handler.stop();
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertFalse(handler.isAlive());
    }
    
    public void testStart()
    {
        handler.stop();
        TestJob job = new TestJob();
        assertEquals(-1, job.handlerId);
        pool.addJob(job);
        
        Thread t = new Thread(handler);
        t.start();
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        //althrough we run handler, it has been stopped and won't execute job.
        assertEquals(-1, job.handlerId);
        
        handler.start();
        
        job = new TestJob();
        assertEquals(-1, job.handlerId);
        pool.addJob(job);
        
        t = new Thread(handler);
        t.start();
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertEquals(handlerId, job.handlerId);
        
        while(!(t.getState() == Thread.State.TIMED_WAITING))
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        handler.stop();
    }
    
    class TestJob implements IJob
    {
        public boolean isCancelled = false;
        public boolean isRunning = false;
        
        int handlerId = -1;
        public void cancel()
        {
            isCancelled = true;
        }

        public void execute(int handlerID)
        {
            handlerId = handlerID;
        }

        public boolean isCancelled()
        {
            return isCancelled;
        }

        public boolean isRunning()
        {
            return isRunning;
        }
    }
}

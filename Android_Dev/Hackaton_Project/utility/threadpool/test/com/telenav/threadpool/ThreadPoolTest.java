/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestThreadPool.java
 *
 */
package com.telenav.threadpool;

import java.util.Vector;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-29
 */
public class ThreadPoolTest extends TestCase
{
    ThreadPool pool;
    int poolSize = 3;
    Object mutex = new Object();
    
    protected void setUp() throws Exception
    {
        pool = new ThreadPool(poolSize, false, 1);
        super.setUp();
    }
    
    public void testGetPoolSize()
    {
        assertEquals(poolSize, pool.getPoolSize());
    }
    
    public void testStart()
    {
        assertEquals(0, pool.waitPool.size());
        pool.start();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertEquals(poolSize, pool.waitPool.size());
        pool.stop();
    }
    
    public void testStop()
    {
        assertEquals(0, pool.waitPool.size());
        pool.start();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertEquals(poolSize, pool.waitPool.size());
        
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertEquals(0, pool.waitPool.size());
        assertEquals(0, pool.getQueueSize());
        pool.stop();
        
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertTrue(job1.isCancelled());
        assertTrue(job2.isCancelled());
        assertTrue(job3.isCancelled());
    }
    
    public void testIsIdle()
    {
        pool.start();
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertTrue(pool.isIdle());
        
        //wait pool size is less than pool size.
        TestJob job1 = new TestJob();
        pool.addJob(job1);
        
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertFalse(pool.isIdle());
        
        //wait pool is empty.
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        pool.addJob(job2);
        pool.addJob(job3);
        
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertFalse(pool.isIdle());
        
        //jobQueue is not empty
        TestJob job4 = new TestJob();
        pool.addJob(job4);
        
        assertFalse(pool.isIdle());
    }
    
    public void testCancelAll()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        pool.start();
        
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertFalse(job1.isCancelled());
        assertFalse(job2.isCancelled());
        assertFalse(job3.isCancelled());
        
        pool.cancelAll();
        assertTrue(job1.isCancelled());
        assertTrue(job2.isCancelled());
        assertTrue(job3.isCancelled());
    }
    
    public void testAddJob()
    {
        TestJob job1 = new TestJob();
        assertEquals(1, pool.addJob(job1));
        assertEquals(1, pool.getQueueSize());
        pool.removePendingJob(job1);
        
        pool.start();
        assertEquals(1, pool.addJob(job1));
        
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertTrue(job1.handlerId != -1);
        assertEquals(0, pool.getQueueSize());
    }
    
    public void testInsertJobInFront()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        assertEquals(1, pool.addJob(job1));
        assertEquals(2, pool.addJob(job2));
        assertEquals(job1, pool.getNextJob());
        
        pool.removePendingJob(job1);
        pool.removePendingJob(job2);
        
        job1 = new TestJob();
        job2 = new TestJob();
        assertEquals(1, pool.addJob(job1));
        assertEquals(2, pool.insertJobInFront(job2));
        assertEquals(job2, pool.getNextJob());
        
        pool.removePendingJob(job1);
        pool.removePendingJob(job2);
    }
    
    public void testAddToWaitQueue()
    {
        int size = pool.waitPool.size();
        Handler handler1 = new Handler(pool, 0, 1);
        pool.addToWaitQueue(handler1);
        assertEquals(size + 1, pool.waitPool.size());
    }
    
    public void testRemoveFromWaitQueue()
    {
        int size = pool.waitPool.size();
        Handler handler1 = new Handler(pool, 0, 1);
        pool.addToWaitQueue(handler1);
        int newSize = pool.waitPool.size();
        assertEquals(size + 1, newSize);
        
        size = newSize;
        pool.removeFromWaitQueue(handler1);
        newSize = pool.waitPool.size();
        assertEquals(size - 1, newSize);
    }
    
    public void testGetCurrentJobs()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        pool.start();
        
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        Vector currentJobs = pool.getCurrentJobs();
        assertTrue(currentJobs.contains(job1));
        assertTrue(currentJobs.contains(job2));
        assertTrue(currentJobs.contains(job3));
        assertEquals(3, currentJobs.size());
    }
    
    public void testGetNextJob()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        
        assertEquals(job1, pool.getNextJob());
        assertEquals(2, pool.getQueueSize());
        
        pool = new ThreadPool(3, true, 1);
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        
        assertEquals(job3, pool.getNextJob());
        assertEquals(0, pool.getQueueSize());
    }
    
    public void testSetSkipPending()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        
        assertEquals(job1, pool.getNextJob());
        assertEquals(2, pool.getQueueSize());
        
        pool.removePendingJob(job2);
        pool.removePendingJob(job3);
        
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        pool.setSkipPending(true);
        
        assertEquals(job3, pool.getNextJob());
        assertEquals(0, pool.getQueueSize());
    }
    
    public void testGetQueueSize()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        assertEquals(0, pool.getQueueSize());
        pool.addJob(job1);
        assertEquals(1, pool.getQueueSize());
        pool.addJob(job2);
        assertEquals(2, pool.getQueueSize());
        pool.addJob(job3);
        assertEquals(3, pool.getQueueSize());
    }
    
    public void testGetPendingJobs()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        
        Vector pendingJobs = pool.getPendingJobs();
        assertEquals(3, pendingJobs.size());
        assertTrue(pendingJobs.contains(job1));
        assertTrue(pendingJobs.contains(job2));
        assertTrue(pendingJobs.contains(job3));
    }
    
    public void testRemovePendingJob()
    {
        TestJob job1 = new TestJob();
        TestJob job2 = new TestJob();
        TestJob job3 = new TestJob();
        
        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);
        
        Vector pendingJobs = pool.getPendingJobs();
        assertEquals(3, pendingJobs.size());
        assertTrue(pendingJobs.contains(job1));
        assertTrue(pendingJobs.contains(job2));
        assertTrue(pendingJobs.contains(job3));
        
        pool.removePendingJob(job1);
        pendingJobs = pool.getPendingJobs();
        assertEquals(2, pendingJobs.size());
        assertFalse(pendingJobs.contains(job1));
        assertTrue(pendingJobs.contains(job2));
        assertTrue(pendingJobs.contains(job3));
    }
    
    public void testIsAlive()
    {
        pool.start();
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertTrue(pool.isAlive());
        pool.stop();
        
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertFalse(pool.isAlive());
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
            synchronized(mutex)
            {
                try
                {
                    mutex.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
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
    
    protected void tearDown() throws Exception
    {
        synchronized(mutex)
        {
            mutex.notifyAll();
        }
        super.tearDown();
    }
}

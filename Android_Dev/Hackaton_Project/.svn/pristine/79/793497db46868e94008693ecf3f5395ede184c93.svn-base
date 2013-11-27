/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NotifierTest.java
 *
 */
package com.telenav.threadpool;

import java.util.Vector;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *@author yning
 *@date 2011-6-29
 */
public class NotifierTest
{
    static NotifierListenerMock listener1;
    static NotifierListenerMock listener2;
    
    public static final int INTERVAL = 800;
    @BeforeClass
    public static void startNotifier()
    {
        Notifier.getInstance().setNotifierInterval(INTERVAL);
        Notifier.getInstance().start();
    }
    
    @Before
    public void setUp() throws Exception
    {
        Notifier.getInstance().removeAllListener();
        listener1 = new NotifierListenerMock();
        listener2 = new NotifierListenerMock();
        Notifier.getInstance().addListener(listener1);
        Notifier.getInstance().addListener(listener2);
    }
    
    @Test
    public void testPause()
    {
        Notifier.getInstance().pause();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        long lastNotifyTime1 = listener1.getLastNotifyTimestamp();
        long lastNotifyTime2 = listener2.getLastNotifyTimestamp();
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        long lastNotifyTime11 = listener1.getLastNotifyTimestamp();
        long lastNotifyTime22 = listener2.getLastNotifyTimestamp();
        
        Assert.assertTrue(lastNotifyTime1 == lastNotifyTime11);
        Assert.assertTrue(lastNotifyTime2 == lastNotifyTime22);
        
        Notifier.getInstance().resume();
    }
    /*
    @Test
    public void testResume()
    {
        Notifier.getInstance().pause();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        long lastNotifyTime1 = listener1.getLastNotifyTimestamp();
        long lastNotifyTime2 = listener2.getLastNotifyTimestamp();
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        long lastNotifyTime11 = listener1.getLastNotifyTimestamp();
        long lastNotifyTime22 = listener2.getLastNotifyTimestamp();
        
        Assert.assertTrue(lastNotifyTime1 == lastNotifyTime11);
        Assert.assertTrue(lastNotifyTime2 == lastNotifyTime22);
        
        Notifier.getInstance().resume();
        
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        long lastNotifyTime111 = listener1.getLastNotifyTimestamp();
        long lastNotifyTime222 = listener2.getLastNotifyTimestamp();
        
        Assert.assertTrue(lastNotifyTime111 > lastNotifyTime11);
        Assert.assertTrue(lastNotifyTime222 > lastNotifyTime22);
    }
    */

    @Test
    public void testAddListener()
    {
        Notifier.getInstance().removeAllListener();
        Vector listeners = Notifier.getInstance().getAllListeners();
        Assert.assertFalse(listeners.contains(listener1));
        Assert.assertFalse(listeners.contains(listener2));
        Assert.assertEquals(0, listeners.size());
        
        Notifier.getInstance().addListener(listener1);
        Notifier.getInstance().addListener(listener2);
        Assert.assertTrue(listeners.contains(listener1));
        Assert.assertTrue(listeners.contains(listener2));
        Assert.assertEquals(2, listeners.size());
    }
    
    @Test
    public void testRemoveListener()
    {
        Vector listeners = Notifier.getInstance().getAllListeners();
        Assert.assertTrue(listeners.contains(listener1));
        Assert.assertTrue(listeners.contains(listener2));
        Assert.assertEquals(2, listeners.size());
        
        Notifier.getInstance().removeListener(listener1);
        Assert.assertFalse(listeners.contains(listener1));
        Assert.assertTrue(listeners.contains(listener2));
        Assert.assertEquals(1, listeners.size());
        
        Notifier.getInstance().removeListener(listener2);
        Assert.assertFalse(listeners.contains(listener1));
        Assert.assertFalse(listeners.contains(listener2));
        Assert.assertEquals(0, listeners.size());
    }
    
    @Test
    public void testRemoveAllListeners()
    {
        Vector listeners = Notifier.getInstance().getAllListeners();
        Assert.assertTrue(listeners.contains(listener1));
        Assert.assertTrue(listeners.contains(listener2));
        Assert.assertEquals(2, listeners.size());
        
        Notifier.getInstance().removeAllListener();
        Assert.assertFalse(listeners.contains(listener1));
        Assert.assertFalse(listeners.contains(listener2));
        Assert.assertEquals(0, listeners.size());
    }
    
    @Test
    public void testGetAllListeners()
    {
        Vector listeners = Notifier.getInstance().getAllListeners();
        Assert.assertTrue(listeners.contains(listener1));
        Assert.assertTrue(listeners.contains(listener2));
        Assert.assertEquals(2, listeners.size());
    }
    /*
    @Test
    public void testIsRunning()
    {
        Assert.assertTrue(Notifier.getInstance().isRunning());
    }
    */

    /*
    @Test
    public void testSetNotifierInterval_A()
    {
        Notifier.getInstance().setNotifierInterval(INTERVAL);
        long checkTimes1 = listener1.checkTimes;
        try
        {
            Thread.sleep(INTERVAL * 2);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        long checkTimes2 = listener1.checkTimes;
        Assert.assertTrue(checkTimes2 - checkTimes1 < 4);
        
        Notifier.getInstance().pause();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        Notifier.getInstance().setNotifierInterval(-1);
        checkTimes1 = listener1.checkTimes;
        Notifier.getInstance().resume();
        try
        {
            Thread.sleep(INTERVAL * 2);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        checkTimes2 = listener1.checkTimes;
        
        System.out.println(checkTimes2 - checkTimes1);
        Assert.assertTrue(checkTimes2 - checkTimes1 > 6);
        
        Notifier.getInstance().setNotifierInterval(INTERVAL);
    }
    */

	/*
    @Test
    public void testSetNotifierInterval_B()
    {
        Notifier.getInstance().setNotifierInterval(INTERVAL);
        long checkTimes1 = listener1.checkTimes;
        long interval = listener1.getNotifyInterval();
        try
        {
            Thread.sleep(interval);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        long checkTimes2 = listener1.checkTimes;
        Assert.assertTrue(checkTimes2 - checkTimes1 < 3);
        
        Notifier.getInstance().pause();
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        Notifier.getInstance().resume();
        
        Notifier.getInstance().setNotifierInterval((int)interval * 4);
        checkTimes1 = listener1.checkTimes;
        try
        {
            Thread.sleep((int)interval * 4);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        checkTimes2 = listener1.checkTimes;
        
        Assert.assertTrue(checkTimes2 - checkTimes1 > 2);
        
        Notifier.getInstance().setNotifierInterval(INTERVAL);
     } 
	*/

    class NotifierListenerMock implements INotifierListener
    {
        long lastNotifyTimeStamp = -1;
        long notifyInterval = INTERVAL;
        public long checkTimes = 0;
        public long getLastNotifyTimestamp()
        {
            checkTimes++;
            return lastNotifyTimeStamp;
        }

        public long getNotifyInterval()
        {
            return notifyInterval;
        }

        public void notify(long timestamp)
        {
            lastNotifyTimeStamp = System.currentTimeMillis();
        }

        public void setLastNotifyTimestamp(long timestamp)
        {
            lastNotifyTimeStamp = timestamp;
        }
    }
    
    @AfterClass
    public static void stopNotifier()
    {
        Notifier.getInstance().stop();
    }
}

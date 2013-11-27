/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidBatteryManagerTest.java
 *
 */
package com.telenav.telephony.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.telenav.logger.Logger;
import com.telenav.telephony.ITnBatteryListener;


/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidBatteryManager.class,BroadcastReceiver.class,Vector.class,IntentFilter.class,System.class, Logger.class})
public class AndroidBatteryManagerTest
{
    int batteryLevel = -1;
    int batteryLevel2 = -1;
    Context mockContext;
    AndroidBatteryManager androidBatteryManager;
    BroadcastReceiver mockbroadcastReceiver;
    Field listenerfield;
    
    @Before
    public void setUp()
    {
        mockContext = PowerMock.createMock(Context.class);
        
        Class anonymousClass = null;
        try
        {
            anonymousClass = Class.forName("com.telenav.telephony.android.AndroidBatteryManager$1");
        }
        catch (ClassNotFoundException e)
        {
            fail(e.toString());
        }
         
        mockbroadcastReceiver = PowerMock.createMock(anonymousClass);
        
        IntentFilter mockIntentFilter1 = PowerMock.createMock(IntentFilter.class);
        IntentFilter mockIntentFilter2 = PowerMock.createMock(IntentFilter.class);
        IntentFilter mockIntentFilter3 = PowerMock.createMock(IntentFilter.class);
        try
        {
            PowerMock.expectNew(anonymousClass).andReturn(mockbroadcastReceiver);
            PowerMock.expectNew(IntentFilter.class, Intent.ACTION_BATTERY_CHANGED).andReturn(mockIntentFilter1);
            PowerMock.expectNew(IntentFilter.class, Intent.ACTION_BATTERY_LOW).andReturn(mockIntentFilter2);
            PowerMock.expectNew(IntentFilter.class, Intent.ACTION_BATTERY_OKAY).andReturn(mockIntentFilter3);
            
            listenerfield = PowerMock.field(AndroidBatteryManager.class, "listenerReceiver");
            listenerfield.set(Whitebox.newInstance(AndroidBatteryManager.class), null);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        EasyMock.expect(mockContext.registerReceiver(EasyMock.anyObject(BroadcastReceiver.class), EasyMock.anyObject(IntentFilter.class))).andReturn(null).times(3);
    }
    
    @Test
    public void testConstructor()
    {
        Vector mockVector = PowerMock.createMock(Vector.class);
        Vector mockVector2 = PowerMock.createMock(Vector.class);
        Context mockContext2 = PowerMock.createMock(Context.class);

        try
        {
            PowerMock.expectNew(Vector.class).andReturn(mockVector).andReturn(mockVector2);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        PowerMock.replayAll();
        

        assertNull(AndroidBatteryManager.listenerReceiver);
        
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        assertEquals(Whitebox.getInternalState(androidBatteryManager, "context"), mockContext);
        assertEquals(androidBatteryManager.listeners, mockVector);
        assertEquals(AndroidBatteryManager.listenerReceiver, mockbroadcastReceiver);
        
        androidBatteryManager = new AndroidBatteryManager(mockContext2);
        assertEquals(Whitebox.getInternalState(androidBatteryManager, "context"), mockContext2);
        assertEquals(androidBatteryManager.listeners, mockVector2);
        assertEquals(AndroidBatteryManager.listenerReceiver, mockbroadcastReceiver);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testAddListener()
    {
        ITnBatteryListener mockBatteryListener = PowerMock.createMock(ITnBatteryListener.class);
        PowerMock.replayAll();
        
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        androidBatteryManager.addListener(mockBatteryListener);
        
        assertTrue(androidBatteryManager.listeners.contains(mockBatteryListener));
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetBatteryLevelSuccess1()
    {
        PowerMock.replayAll();
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        assertEquals(-1, androidBatteryManager.getBatteryLevel());
        androidBatteryManager.batteryLevel = 20;
        assertEquals(20, androidBatteryManager.getBatteryLevel());
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetBatteryLevelSuccess2()
    {
        PowerMock.replayAll();
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                batteryLevel = androidBatteryManager.getBatteryLevel();
            }
        });
        
        Thread t2 = new Thread(new Runnable()
        {
            public void run()
            {
                batteryLevel2 = androidBatteryManager.getBatteryLevel();
            }
        });
        t.start();
        t2.start();
        t.yield();
        
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < 3000 && t.getState() != Thread.State.TIMED_WAITING)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                fail(e.getMessage());
            }
        }
        androidBatteryManager.batteryLevel = 30;
        time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < 3000 && t2.getState() != Thread.State.TERMINATED)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                fail(e.getMessage());
            }
        }
        assertEquals(30, batteryLevel2);
        assertEquals(30, batteryLevel2);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetBatteryLevelException1()
    { 
        PowerMock.replayAll();
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                batteryLevel = androidBatteryManager.getBatteryLevel();
            }
        });
        t.start();
        
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < 3000 && t.getState() != Thread.State.TIMED_WAITING)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                fail(e.getMessage());
            }
        }
        t.interrupt();
        androidBatteryManager.batteryLevel = 20;
        time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < 3000 && t.getState() != Thread.State.TERMINATED)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                fail(e.getMessage());
            }
        }
        assertEquals(20, batteryLevel);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetBatteryLevelException2()
    {
        PowerMock.mockStatic(System.class);
        PowerMock.mockStatic(Logger.class);
        EasyMock.expect(System.currentTimeMillis()).andThrow(new RuntimeException());
        Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
        PowerMock.replayAll();
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                batteryLevel = androidBatteryManager.getBatteryLevel();
            }
        });
        t.start();
        
        int count = 0;
        while (count < 20 && t.getState() != Thread.State.TERMINATED)
        {
            try
            {
                count ++;
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                fail(e.getMessage());
            }
        }
        assertEquals(-1, batteryLevel);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testRemoveListener()
    {
        ITnBatteryListener mockBatteryListener = PowerMock.createMock(ITnBatteryListener.class);
        PowerMock.replayAll();
        
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        androidBatteryManager.listeners.add(mockBatteryListener);
        androidBatteryManager.removeListener(mockBatteryListener);
        assertFalse(androidBatteryManager.listeners.contains(mockBatteryListener));
        PowerMock.verifyAll();
    }
    
//    @Test
//    public void testRegisterReceiver()
//    {
//        mockContext.unregisterReceiver(mockbroadcastReceiver);
//        PowerMock.replayAll();
//        PowerMock.verifyAll();
//    }
    
    @Test
    public void testFinish()
    {
        mockContext.unregisterReceiver(mockbroadcastReceiver);
        PowerMock.replayAll();
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        assertNotNull(AndroidBatteryManager.listenerReceiver);
        androidBatteryManager.finish();
        assertNull(AndroidBatteryManager.listenerReceiver);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testFinalize()
    {
        mockContext.unregisterReceiver(mockbroadcastReceiver);
        PowerMock.replayAll();
        androidBatteryManager = new AndroidBatteryManager(mockContext);
        assertNotNull(AndroidBatteryManager.listenerReceiver);
        try
        {
            androidBatteryManager.finalize();
        }
        catch (Throwable e)
        {
            fail(e.getMessage());
        }
        assertNull(AndroidBatteryManager.listenerReceiver);
        PowerMock.verifyAll();
    }
    
}

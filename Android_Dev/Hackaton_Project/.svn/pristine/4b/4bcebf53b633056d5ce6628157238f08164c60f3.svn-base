/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidBacklightManagerTest.java
 *
 */
/**
 * 
 */
package com.telenav.backlight.android;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-1
 */
/**
 * @author byma
 *
 */
public class AndroidBacklightManagerTest
{

    private AndroidBacklightManager backlightManager;
    private Context mockContext;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        mockContext = PowerMock.createMock(Context.class);
        backlightManager = new AndroidBacklightManager(mockContext);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.backlight.android.AndroidBacklightManager#turnOnDelegate(long)}.
     */
    @Test
    public void testTurnOnDelegate()
    {
        PowerManager powerManager = PowerMock.createMock(PowerManager.class);
        EasyMock.expect(mockContext.getSystemService(Context.POWER_SERVICE)).andReturn(
            powerManager);
        WakeLock mockWakeLock = EasyMock.createMock(PowerManager.WakeLock.class);
        EasyMock.expect(
            powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TeleNavApp-backlight"))
                .andReturn(mockWakeLock);
        PowerMock.replayAll();
        backlightManager.turnOnDelegate(10000);
        PowerMock.verifyAll();
    }

    @Test
    public void testTurnOnDelegateException()
    {
        PowerManager powerManager = PowerMock.createMock(PowerManager.class);
        EasyMock.expect(mockContext.getSystemService(Context.POWER_SERVICE)).andReturn(
            powerManager);
        WakeLock mockWakeLock = PowerMock.createMock(PowerManager.WakeLock.class);
        EasyMock.expect(
            powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TeleNavApp-backlight"))
                .andReturn(mockWakeLock);
        PowerMock.replayAll();
        backlightManager.turnOnDelegate(10000);
        PowerMock.verifyAll();
    }
    
    /**
     * Test method for {@link com.telenav.backlight.android.AndroidBacklightManager#turnOff()}.
     */
    @Test
    public void testTurnOff()
    {
        backlightManager.wackLock = PowerMock.createMock(PowerManager.WakeLock.class);
        EasyMock.expect(backlightManager.wackLock.isHeld()).andReturn(
            Boolean.valueOf(true));
        backlightManager.wackLock.release();
        PowerMock.replayAll();
        backlightManager.turnOff();
        PowerMock.verifyAll();

    }
    
    @Test
    public void testTurnOffException()
    {
        backlightManager.wackLock = PowerMock.createMock(PowerManager.WakeLock.class);
        EasyMock.expect(backlightManager.wackLock.isHeld()).andThrow(new RuntimeException());
        PowerMock.replayAll();
        backlightManager.turnOff();
        PowerMock.verifyAll();

    }

    /**
     * Test method for {@link com.telenav.backlight.android.AndroidBacklightManager#isOn()}.
     */
    @Test
    public void testIsOn()
    {
        PowerMock.resetAll();
        PowerManager powerMgr = PowerMock.createMock(PowerManager.class);
        EasyMock.expect(mockContext.getSystemService(Context.POWER_SERVICE)).andReturn(
            powerMgr);
        PowerMock.replayAll();
        try
        {
            backlightManager.isOn();
        }
        catch (NumberFormatException e)
        {

        }
        PowerMock.verifyAll();
    }
    
    /**
     * Test method for {@link com.telenav.backlight.android.AndroidBacklightManager#enableKeyguard(boolean)}.
     */
    @Test
    public void testEnableKeyguardFalse()
    {
        KeyguardManager keyGuardMgr = PowerMock.createMock(KeyguardManager.class);
        EasyMock.expect(mockContext.getSystemService(Context.KEYGUARD_SERVICE)).andReturn(keyGuardMgr);
        KeyguardLock kLock = PowerMock.createMock(KeyguardLock.class);
        EasyMock.expect(keyGuardMgr.newKeyguardLock("TeleNavApp-keyguard")).andReturn(kLock);
        kLock.disableKeyguard();
        PowerMock.replayAll();
        backlightManager.enableKeyguard(false);
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PowerMock.verifyAll();
        
    }

    @Test
    public void testEnableKeyguardTrue()
    {
        KeyguardManager keyGuardMgr = PowerMock.createMock(KeyguardManager.class);
        EasyMock.expect(mockContext.getSystemService(Context.KEYGUARD_SERVICE)).andReturn(keyGuardMgr);
        KeyguardLock kLock = PowerMock.createMock(KeyguardLock.class);
        EasyMock.expect(keyGuardMgr.newKeyguardLock("TeleNavApp-keyguard")).andReturn(kLock);
        kLock.reenableKeyguard();
        PowerMock.replayAll();
        backlightManager.enableKeyguard(true);
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PowerMock.verifyAll();
        
    }
    /**
     * Test method for {@link com.telenav.backlight.android.AndroidBacklightManager#AndroidBacklightManager(android.content.Context)}.
     */
    @Test
    public void testAndroidBacklightManager()
    {
        assertNotNull(backlightManager);
    }

}

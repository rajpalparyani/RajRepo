/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnBatteryManagerTest.java
 *
 */
package com.telenav.telephony;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnBatteryManager.class})
public class TnBatteryManagerTest
{
    @Before
    public void setUp()
    {
        
        Field field = PowerMock.field(TnBatteryManager.class, "tnbatteryManager");
        try
        {
            field.set(Whitebox.newInstance(TestBatteryManager.class), null);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        
        field = PowerMock.field(TnBatteryManager.class, "initCount");
        try
        {
            field.set(Whitebox.newInstance(TestBatteryManager.class), 0);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }
    
    @Test
    public void testGetInstance()
    {
        TnBatteryManager mockTnBatteryManager = PowerMock.createMock(TnBatteryManager.class);

        assertNull(TnBatteryManager.getInstance());
        TnBatteryManager.init(mockTnBatteryManager);
        assertEquals(TnBatteryManager.getInstance(), mockTnBatteryManager);
    }
    
    @Test
    public void testInit()
    {
        TnBatteryManager mockTnRadioManager = PowerMock.createMock(TnBatteryManager.class);
        TnBatteryManager mockTnRadioManager2 = PowerMock.createMock(TnBatteryManager.class);

        assertNull(TnBatteryManager.getInstance());
        TnBatteryManager.init(mockTnRadioManager);
        assertEquals(TnBatteryManager.getInstance(), mockTnRadioManager);
        TnBatteryManager.init(mockTnRadioManager2);
        assertEquals(TnBatteryManager.getInstance(), mockTnRadioManager);
    }
    
    @Test
    public void testFinish()
    {
        TnBatteryManager mockTnRadioManager = PowerMock.createMock(TnBatteryManager.class);
        
        assertNull(TnBatteryManager.getInstance());
        TnBatteryManager.init(mockTnRadioManager);
        TnBatteryManager.getInstance().finish();
        assertEquals(TnBatteryManager.getInstance(), mockTnRadioManager);
    }
    
    class TestBatteryManager extends TnBatteryManager
    {
        public void addListener(ITnBatteryListener batteryListener)
        {
        }

        public int getBatteryLevel()
        {
            return 0;
        }

        public void removeListener(ITnBatteryListener batteryListener)
        {
        }
    }
}

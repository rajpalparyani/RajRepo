/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnRadioManagerTest.java
 *
 */
package com.telenav.radio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-6
 */
public class TnRadioManagerTest
{
    @Before
    public void setUp()
    {
        
        Field field = PowerMock.field(TnRadioManager.class, "radioManager");
        try
        {
            field.set(Whitebox.newInstance(TestTnRadioManager.class), null);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        
        field = PowerMock.field(TnRadioManager.class, "initCount");
        try
        {
            field.set(Whitebox.newInstance(TestTnRadioManager.class), 0);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }
    
    @Test
    public void testGetInstance()
    {
        TnRadioManager mockTnRadioManager = PowerMock.createMock(TnRadioManager.class);

        assertNull(TnRadioManager.getInstance());
        TnRadioManager.init(mockTnRadioManager);
        assertEquals(TnRadioManager.getInstance(), mockTnRadioManager);
    }
    
    @Test
    public void testInit()
    {
        TnRadioManager mockTnRadioManager = PowerMock.createMock(TnRadioManager.class);
        TnRadioManager mockTnRadioManager2 = PowerMock.createMock(TnRadioManager.class);

        assertNull(TnRadioManager.getInstance());
        TnRadioManager.init(mockTnRadioManager);
        assertEquals(TnRadioManager.getInstance(), mockTnRadioManager);
        TnRadioManager.init(mockTnRadioManager2);
        assertEquals(TnRadioManager.getInstance(), mockTnRadioManager);
    }
    
    class TestTnRadioManager extends TnRadioManager
    {
        public void addListener(ITnCoverageListener coverageListener)
        {
        }

        public TnCellInfo getCellInfo()
        {
            return null;
        }

        public TnWifiInfo getWifiInfo()
        {
            return null;
        }

        public boolean isAirportMode()
        {
            return false;
        }

        public boolean isNetworkConnected()
        {
            return false;
        }

        public boolean isRoaming()
        {
            return false;
        }

        public boolean isWifiConnected()
        {
            return false;
        }

        public void removeListener(ITnCoverageListener coverageListener)
        {
        }
    }
}

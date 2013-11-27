/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnNioManagerTest.java
 *
 */
package com.telenav.nio;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

import com.telenav.nio.TnNioManager;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-6
 */
public class TnNioManagerTest
{

    private TnNioManager mockNioManger;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        PowerMock.mockStatic(TnNioManager.class);
        mockNioManger = PowerMock.createMock(TnNioManager.class);
        Whitebox.setInternalState(TnNioManager.class, "initCount", 0);
        TnNioManager.init(mockNioManger);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.nio.TnNioManager#getInstance()}.
     */
    @Test
    public void testGetInstance()
    {
       
        assertEquals(mockNioManger, TnNioManager.getInstance());
    }

    /**
     * Test method for {@link com.telenav.nio.TnNioManager#init(com.telenav.nio.TnNioManager)}.
     */
    @Test
    public void testInit()
    {
        assertEquals(mockNioManger, TnNioManager.getInstance());
    }

}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavServiceManagerTest.java
 *
 */
package com.telenav.app;

import java.lang.reflect.Field;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.navservice.NavServiceApi;

import junit.framework.TestCase;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-9-29
 */
@RunWith(PowerMockRunner.class)
public class NavServiceManagerTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testInit()
    {
        PowerMock.mockStatic(NavServiceManager.class);
        NavServiceManager mockManager = PowerMock.createMock(NavServiceManager.class);
        NavServiceApi mockNavServiceApi = PowerMock.createMock(NavServiceApi.class);
        mockManager.init(mockNavServiceApi);
        assertEquals(mockNavServiceApi, mockManager.navService);
    }
    
    public void testGetNavService()
    {
        PowerMock.mockStatic(NavServiceManager.class);
        NavServiceManager mockManager = PowerMock.createMock(NavServiceManager.class);
        Field navService = PowerMock.field(NavServiceManager.class, "navService");
        NavServiceApi mockNavServiceApi = PowerMock.createMock(NavServiceApi.class);
        try
        {
            navService.set(mockManager, mockNavServiceApi);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(mockNavServiceApi, mockManager.getNavService());
    }

}

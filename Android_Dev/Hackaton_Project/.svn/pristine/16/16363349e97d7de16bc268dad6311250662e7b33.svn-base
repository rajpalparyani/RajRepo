/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidBacklightUtilTest.java
 *
 */
/**
 * 
 */
package com.telenav.backlight.android;


import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import android.os.PowerManager;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-6-28
 */
public class AndroidBacklightUtilTest
{

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.backlight.android.AndroidBacklightUtil#isOn(android.os.PowerManager)}.
     */
    @Test
    public void testIsOn()
    {
        IMocksControl mockControl = EasyMock.createControl();
        PowerManager mockPowerManager = mockControl.createMock(PowerManager.class);
        EasyMock.expect(mockPowerManager.isScreenOn()).andReturn(true);
        mockControl.replay();
        assertTrue(AndroidBacklightUtil.isOn(mockPowerManager));
        mockControl.verify();
    }
    
    @Test
    public void testAndroidBacklightUtil()
    {
        AndroidBacklightUtil androidUtil = new AndroidBacklightUtil();
        assertNotNull(androidUtil);
    }

}

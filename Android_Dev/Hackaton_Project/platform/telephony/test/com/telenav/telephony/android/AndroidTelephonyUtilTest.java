/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidTelephonyUtilTest.java
 *
 */
package com.telenav.telephony.android;

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

import android.os.Build;

import com.telenav.telephony.TnBatteryManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Build.class})
public class AndroidTelephonyUtilTest
{
    AndroidTelephonyUtil androidTelephonyUtil;
    
    @Before
    public void setUp()
    {
        androidTelephonyUtil = new AndroidTelephonyUtil();
    }
    
    @Test
    public void testGetManufacturerName()
    {
        Field field = PowerMock.field(Build.class, "MANUFACTURER");
        try
        {
            field.set(Whitebox.newInstance(Build.class), "MOTO");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        PowerMock.replayAll();

        assertEquals(AndroidTelephonyUtil.getManufacturerName(), "MOTO");
        PowerMock.verifyAll();
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnTelephonyManagerTest.java
 *
 */
package com.telenav.telephony;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Hashtable;

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
@PrepareForTest({TnTelephonyManager.class})
public class TnTelephonyManagerTest
{
    @Before
    public void setUp()
    {
        
        Field field = PowerMock.field(TnTelephonyManager.class, "telephonyManager");
        try
        {
            field.set(Whitebox.newInstance(TestTelephonyManager.class), null);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        
        field = PowerMock.field(TnTelephonyManager.class, "initCount");
        try
        {
            field.set(Whitebox.newInstance(TestTelephonyManager.class), 0);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }
    
    @Test
    public void testGetInstance()
    {
        TnTelephonyManager mockTnTelephonyManager = PowerMock.createMock(TnTelephonyManager.class);

        assertNull(TnTelephonyManager.getInstance());
        TnTelephonyManager.init(mockTnTelephonyManager);
        assertEquals(TnTelephonyManager.getInstance(), mockTnTelephonyManager);
    }
    
    @Test
    public void testInit()
    {
        TnTelephonyManager mockTnTelephonyManager = PowerMock.createMock(TnTelephonyManager.class);
        TnTelephonyManager mockTnTelephonyManager2 = PowerMock.createMock(TnTelephonyManager.class);

        assertNull(TnTelephonyManager.getInstance());
        TnTelephonyManager.init(mockTnTelephonyManager);
        assertEquals(TnTelephonyManager.getInstance(), mockTnTelephonyManager);
        TnTelephonyManager.init(mockTnTelephonyManager2);
        assertEquals(TnTelephonyManager.getInstance(), mockTnTelephonyManager);
    }
    class TestTelephonyManager extends TnTelephonyManager
    {
        public void addListener(ITnPhoneListener phoneListener)
        {
        }

        public TnDeviceInfo getDeviceInfo()
        {
            return null;
        }

        public String getPhoneNumber()
        {
            return null;
        }

        public TnSimCardInfo getSimCardInfo()
        {
            return null;
        }

        public void removeListener(ITnPhoneListener phoneListener)
        {
        }

        public void startBrowser(String url)
        {
        }

        public void startCall(String phoneNumber)
        {
        }

        public void startEmail(String sentTo, String subject, String content)
        {
        }

        public void startMMS(String sentTo, String message)
        {
        }

        public void startMMSAtBackground(String sentTo, String message)
        {
        }

        public void vibrate(int durationTime)
        {
        }

        public void addApnListener(ITnApnListener listener)
        {
        }

        public void removeApnListener(ITnApnListener listener)
        {
        }
        
        public String insertApn(TnApnInfo apn, Hashtable extras)
        {
            return null;
        }

        public void setPrefered(String apnId)
        {
        }

        protected TnApnInfo[] getMatchedApn(String apn, String type, String mcc, String mnc)
        {
            return null;
        }

        protected String getSimOperator()
        {
            return null;
        }

        protected TnApnInfo getPreferredApn(String apn, String type, String mcc, String mnc)
        {
            return null;
        }

        @Override
        public String getIpAddress()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}


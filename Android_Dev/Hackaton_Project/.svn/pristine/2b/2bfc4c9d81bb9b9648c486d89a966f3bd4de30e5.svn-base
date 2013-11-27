/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndoidPhoneListenerTest.java
 *
 */
package com.telenav.telephony.android;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.telenav.telephony.ITnPhoneListener;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AndoidPhoneListener.class})
public class AndoidPhoneListenerTest
{
    AndoidPhoneListener andoidPhoneListener;
    ITnPhoneListener mockTnPhoneListener;
    
    @Before
    public void setUp()
    {
        PowerMock.suppress(PowerMock.constructor(PhoneStateListener.class)); 
        mockTnPhoneListener = PowerMock.createMock(ITnPhoneListener.class);
        andoidPhoneListener = new AndoidPhoneListener(mockTnPhoneListener);
    }
    
    @Test
    public void testConstructor()
    {
        PowerMock.replayAll();
        
        AndoidPhoneListener andoidPhoneListener1 = new AndoidPhoneListener(mockTnPhoneListener);
        assertEquals(Whitebox.getInternalState(andoidPhoneListener1, "tnPhoneListener"), mockTnPhoneListener);
        PowerMock.verifyAll();
    }
    
    
    @Test
    public void testOnCallStateChanged()
    {
        String ptn = "3788933621";
        mockTnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, ptn, "declined.");
        mockTnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_CONNECTED, ptn, null);
        mockTnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, ptn, "end by user.");
        mockTnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_RINGING, ptn, null);
        PowerMock.replayAll();

        assertFalse((Boolean)Whitebox.getInternalState(andoidPhoneListener, "isPhoneConnected"));
        andoidPhoneListener.onCallStateChanged(TelephonyManager.CALL_STATE_IDLE, ptn);
        assertFalse((Boolean)Whitebox.getInternalState(andoidPhoneListener, "isPhoneConnected"));
        andoidPhoneListener.onCallStateChanged(TelephonyManager.CALL_STATE_OFFHOOK, ptn);
        assertTrue((Boolean)Whitebox.getInternalState(andoidPhoneListener, "isPhoneConnected"));
        andoidPhoneListener.onCallStateChanged(TelephonyManager.CALL_STATE_OFFHOOK, ptn);
        assertTrue((Boolean)Whitebox.getInternalState(andoidPhoneListener, "isPhoneConnected"));
        andoidPhoneListener.onCallStateChanged(TelephonyManager.CALL_STATE_IDLE, ptn);
        assertFalse((Boolean)Whitebox.getInternalState(andoidPhoneListener, "isPhoneConnected"));
        andoidPhoneListener.onCallStateChanged(TelephonyManager.CALL_STATE_RINGING, ptn);
        assertFalse((Boolean)Whitebox.getInternalState(andoidPhoneListener, "isPhoneConnected"));
        andoidPhoneListener.onCallStateChanged(3, ptn);
        assertFalse((Boolean)Whitebox.getInternalState(andoidPhoneListener, "isPhoneConnected"));
        PowerMock.verifyAll();
    }
}

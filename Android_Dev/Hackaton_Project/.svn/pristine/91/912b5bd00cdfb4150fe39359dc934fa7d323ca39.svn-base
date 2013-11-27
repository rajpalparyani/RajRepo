/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidRadioStatusListenerTest.java
 *
 */
package com.telenav.radio.android;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;

import com.telenav.radio.ITnCoverageListener;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-6
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidRadioStatusListener.class})
public class AndroidRadioStatusListenerTest
{
    int state = -1;
    int signal = -1;
    AndroidRadioStatusListener mockRadioStatusListenerTest;
         
    @Before
    public void setUp()
    {
        ITnCoverageListener mockTnCoverageListener = new ITnCoverageListener()
        {
            public void onSignalStrengthChanged(int strength)
            {
                signal = strength;
            }
            
            public void onServiceStateChanged(int serviceState)
            {
                state = serviceState;
            }
        };
        PowerMock.suppress(PowerMock.constructor(PhoneStateListener.class)); 
        mockRadioStatusListenerTest = new AndroidRadioStatusListener(mockTnCoverageListener);
    }
    
    
    @Test
    public void testOnServiceStateChanged()
    {
        ServiceState mockServiceState = PowerMock.createMock(ServiceState.class);
        EasyMock.expect(mockServiceState.getState()).andReturn(3).andReturn(2).andReturn(1).andReturn(0);
        PowerMock.replayAll();
        
        mockRadioStatusListenerTest.onServiceStateChanged(mockServiceState);
        assertEquals(state, 0);
        mockRadioStatusListenerTest.onServiceStateChanged(mockServiceState);
        assertEquals(state, 2);
        mockRadioStatusListenerTest.onServiceStateChanged(mockServiceState);
        assertEquals(state, 1);
        mockRadioStatusListenerTest.onServiceStateChanged(mockServiceState);
        assertEquals(state, 3);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testOnSignalStrengthChanged()
    {
        mockRadioStatusListenerTest.onSignalStrengthChanged(10);
        assertEquals(-93, signal);
    }

}

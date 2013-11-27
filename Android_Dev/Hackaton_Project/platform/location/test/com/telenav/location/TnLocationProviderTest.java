/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnLocationProviderTest.java
 *
 */
package com.telenav.location;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-7
 */
public class TnLocationProviderTest
{

    TnLocationProvider tnLocationProvider;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        tnLocationProvider = new TnLocationProvider("mock")
        {

            @Override
            protected void reset()
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected TnLocation getLastKnownLocation() throws TnLocationException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            protected TnLocation getLocation(int timeout) throws TnLocationException
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            protected void requestLocationUpdates(long minTime, float minDistance,
                    int timeout, int maxAge, ITnLocationListener listener)
            {
                // TODO Auto-generated method stub
                
            }
            
        };
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationProvider#TnLocationProvider(java.lang.String)}.
     */
    @Test
    public void testTnLocationProvider()
    {
        
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationProvider#getName()}.
     */
    @Test
    public void testGetName()
    {
        assertEquals("mock",tnLocationProvider.getName());
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationProvider#meetsCriteria(com.telenav.location.TnCriteria)}.
     */
    @Test
    public void testMeetsCriteria()
    {
        assertFalse(tnLocationProvider.meetsCriteria(null));
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationProvider#sendExtraCommand(java.lang.String, java.util.Hashtable)}.
     */
    @Test
    public void testSendExtraCommand()
    {
        assertFalse(tnLocationProvider.sendExtraCommand("", null));
    }

}

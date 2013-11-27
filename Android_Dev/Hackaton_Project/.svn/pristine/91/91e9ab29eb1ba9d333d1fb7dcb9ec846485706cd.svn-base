/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractTnMviewerLocationProviderTest.java
 *
 */
/**
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
public class AbstractTnMviewerLocationProviderTest
{
    AbstractTnMviewerLocationProvider tnMviewerLocationProvider;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        tnMviewerLocationProvider = new AbstractTnMviewerLocationProvider("MviewerMock")
        {
            
            @Override
            protected String getMviewerData(int timeout) throws TnLocationException
            {
               
                return new String("1212132112,3700000,12200000,12,1,60,3");
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
     * Test method for {@link com.telenav.location.AbstractTnMviewerLocationProvider#getLocationDelegate(int)}.
     */
    @Test
    public void testGetLocationDelegate()
    {
        TnLocation location = null;
        try
        {
            location = tnMviewerLocationProvider.getLocationDelegate(10000);
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(121213211, location.getTime());
        assertEquals(3700000, location.getLatitude());
        assertEquals(12200000, location.getLongitude());
        assertEquals(12, location.getSpeed());
        assertEquals(1, location.getHeading());
        assertEquals(60, location.getAccuracy());
        assertEquals(3, location.getSatellites());
        assertTrue(location.isValid());
    }

    /**
     * Test method for {@link com.telenav.location.AbstractTnMviewerLocationProvider#AbstractTnMviewerLocationProvider(java.lang.String)}.
     */
    @Test
    public void testAbstractTnMviewerLocationProvider()
    {
        assertEquals("MviewerMock", tnMviewerLocationProvider.getName());
    }

    /**
     * Test method for {@link com.telenav.location.AbstractTnMviewerLocationProvider#setSocketHost(java.lang.String, int)}.
     */
    @Test
    public void testSetSocketHost()
    {
        tnMviewerLocationProvider.setSocketHost("http://www.telenav.com", 8080);
        assertEquals("http://www.telenav.com", tnMviewerLocationProvider.socketHost);
        assertEquals(8080, tnMviewerLocationProvider.socketPort);
    }

}

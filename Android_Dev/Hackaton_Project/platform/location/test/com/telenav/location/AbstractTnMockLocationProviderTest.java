/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractTnMockLocationProviderTest.java
 *
 */
package com.telenav.location;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-7
 */
public class AbstractTnMockLocationProviderTest implements ITnLocationListener
{

    AbstractTnMockLocationProvider mockLocationProvider;
    TnLocation location;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        mockLocationProvider = new AbstractTnMockLocationProvider("mock")
        {
            @Override
            protected TnLocation getLocationDelegate(int timeout) throws TnLocationException
            {
                location = new TnLocation("mock");
                location.accuracy = 60;
                location.altitude = 40;
                location.latitude = 37422006;
                location.hasEnc = false;
                location.heading = 0;
                location.localTime = System.currentTimeMillis();
                location.longitude = -122084095;
                location.satellites = 3;
                location.speed = 3;
                location.time = System.currentTimeMillis();
                return location;
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
     * Test method for {@link com.telenav.location.AbstractTnMockLocationProvider#reset()}.
     */
    @Test
    public void testReset()
    {
        mockLocationProvider.reset();
        assertTrue(mockLocationProvider.isCancelled);
        assertNull(mockLocationProvider.listener);
    }

    /**
     * Test method for {@link com.telenav.location.AbstractTnMockLocationProvider#getLastKnownLocation()}.
     */
    @Test
    public void testGetLastKnownLocationNull()
    {
        mockLocationProvider.lastKnownLocation = null;
        TnLocation location = null;
        try
        {
            location = mockLocationProvider.getLastKnownLocation();
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(this.location, location);
    }

    /**
     * Test method for {@link com.telenav.location.AbstractTnMockLocationProvider#getLastKnownLocation()}.
     */
    @Test
    public void testGetLastKnownLocationOverDue()
    {
        mockLocationProvider.lastKnownLocation = new TnLocation("mock");
        mockLocationProvider.lastKnownLocation.time = System.currentTimeMillis() - 30 * 60 * 1001;
        TnLocation location = null;
        try
        {
            location = mockLocationProvider.getLastKnownLocation();
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(this.location, location);
    }
    
    /**
     * Test method for {@link com.telenav.location.AbstractTnMockLocationProvider#getLastKnownLocation()}.
     */
    @Test
    public void testGetLastKnownLocationDirectly()
    {
        mockLocationProvider.lastKnownLocation = new TnLocation("mock");
        mockLocationProvider.lastKnownLocation.time = System.currentTimeMillis();
        TnLocation tnLocation = null;
        try
        {
            tnLocation = mockLocationProvider.getLastKnownLocation();
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(mockLocationProvider.lastKnownLocation, tnLocation);
    }
    
    /**
     * Test method for {@link com.telenav.location.AbstractTnMockLocationProvider#getLocation(int)}.
     */
    @Test
    public void testGetLocation()
    {
        TnLocation currentlocation = null;
        try
        {
            currentlocation = mockLocationProvider.getLocation(100000);
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(this.location, currentlocation);
    }

    /**
     * Test method for {@link com.telenav.location.AbstractTnMockLocationProvider#requestLocationUpdates(long, float, int, int, com.telenav.location.ITnLocationListener)}.
     */
    @Test
    public void testRequestLocationUpdates()
    {
        mockLocationProvider.requestLocationUpdates(500, 1, 100000, 100000, this);
    }

    /**
     * Test method for {@link com.telenav.location.AbstractTnMockLocationProvider#AbstractTnMockLocationProvider(java.lang.String)}.
     */
    @Test
    public void testAbstractTnMockLocationProvider()
    {
        assertNotNull(mockLocationProvider);
    }


    /* (non-Javadoc)
     * @see com.telenav.location.ITnLocationListener#onLocationChanged(com.telenav.location.TnLocationProvider, com.telenav.location.TnLocation)
     */
    public void onLocationChanged(TnLocationProvider provider, TnLocation location)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.telenav.location.ITnLocationListener#onStatusChanged(com.telenav.location.TnLocationProvider, int)
     */
    public void onStatusChanged(TnLocationProvider provider, int status)
    {
        // TODO Auto-generated method stub
        
    }

}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnLocationManagerTest.java
 *
 */
/**
 * 
 */
package com.telenav.location;

import static org.junit.Assert.*;

import java.util.Hashtable;

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
public class TnLocationManagerTest
{
    TnLocationManager mockLocationMgr;
    TnLocationProvider mockLocationProvider;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        mockLocationProvider = EasyMock.createMock(TnLocationProvider.class);
        mockLocationMgr = new TnLocationManager()
        {
            
            @Override
            public boolean isGpsProviderAvailable(String provider)
            {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            protected TnLocationProvider getProviderDelegate(String provider)
            {
                // TODO Auto-generated method stub
                return mockLocationProvider;
            }
            
            @Override
            protected TnLocationProvider getProvider(TnCriteria criteria)
            {
                // TODO Auto-generated method stub
                return mockLocationProvider;
            }
        };
        Whitebox.setInternalState(TnLocationManager.class, "initCount", 0);
        TnLocationManager.init(mockLocationMgr);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#getInstance()}.
     */
    @Test
    public void testGetInstance()
    {
        assertEquals(mockLocationMgr, TnLocationManager.getInstance());
    }

   
    /**
     * Test method for {@link com.telenav.location.TnLocationManager#addProvider(java.lang.String, com.telenav.location.TnLocationProvider)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testAddProviderIllegalParameters()
    {
        TnLocationManager.getInstance().addProvider("", null);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#addProvider(java.lang.String, com.telenav.location.TnLocationProvider)}.
     */
    @Test
    public void testAddProvider()
    {
        TnLocationProvider tnLocationProvider = PowerMock.createMock(TnLocationProvider.class);
        TnLocationManager.getInstance().addProvider("mockprovider", tnLocationProvider);
        Object locationProvider = TnLocationManager.getInstance().getProvider("mockprovider");
        assertEquals(tnLocationProvider, locationProvider);
    }
    
    /**
     * Test method for {@link com.telenav.location.TnLocationManager#getProvider(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetProviderStringIllegalException()
    {
        TnLocationManager.getInstance().getProvider("");
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#getLastKnownLocation(com.telenav.location.TnCriteria)}.
     */
    @Test
    public void testGetLastKnownLocationTnCriteria()
    {
        TnCriteria mockCritera = EasyMock.createMock(TnCriteria.class);
        TnLocation lastKnownMockLocation = EasyMock.createMock(TnLocation.class);
        TnLocation location = null;
        try
        {
            EasyMock.expect(mockLocationProvider.getLastKnownLocation()).andReturn(lastKnownMockLocation);
            EasyMock.replay(mockLocationProvider);
            location = TnLocationManager.getInstance().getLastKnownLocation(mockCritera);
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(lastKnownMockLocation, location);
        EasyMock.verify(mockLocationProvider);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#getLastKnownLocation(java.lang.String)}.
     */
    @Test
    public void testGetLastKnownLocationString()
    {
        TnLocation lastKnownMockLocation = EasyMock.createMock(TnLocation.class);
        TnLocation location = null;
        try
        {
            EasyMock.expect(mockLocationProvider.getLastKnownLocation()).andReturn(lastKnownMockLocation);
            EasyMock.replay(mockLocationProvider);
            location = TnLocationManager.getInstance().getLastKnownLocation("mock");
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(lastKnownMockLocation, location);
        EasyMock.verify(mockLocationProvider);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#getLocation(com.telenav.location.TnCriteria, int)}.
     */
    @Test
    public void testGetLocation()
    {
        TnCriteria mockCriteria = EasyMock.createMock(TnCriteria.class);
        TnLocation mockTnLocation = EasyMock.createMock(TnLocation.class);
        TnLocation location = null;
        try
        {
            EasyMock.expect(mockLocationProvider.getLocation(100000)).andReturn(mockTnLocation);
            EasyMock.replay(mockLocationProvider);
            location = TnLocationManager.getInstance().getLocation(mockCriteria, 100000);
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(mockTnLocation, location);
        EasyMock.verify(mockLocationProvider);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#getLocation(com.telenav.location.TnCriteria, int)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetLocationIllegalParameters()
    {
        try
        {
            TnLocationManager.getInstance().getLocation(null, -1);
        }
        catch (TnLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Test method for {@link com.telenav.location.TnLocationManager#requestLocationUpdates(com.telenav.location.TnCriteria, long, float, int, int, com.telenav.location.ITnLocationListener)}.
     */
    @Test
    public void testRequestLocationUpdatesTnCriteriaLongFloatIntIntITnLocationListener()
    {
        TnCriteria critera = EasyMock.createMock(TnCriteria.class);
        mockLocationProvider.requestLocationUpdates(0, 0, 0, 0, null);
        EasyMock.expect(mockLocationProvider.getName()).andReturn("mock");
        EasyMock.replay(mockLocationProvider);
        String name = TnLocationManager.getInstance().requestLocationUpdates(critera, 0, 0, 0, 0, null);
        assertEquals("mock", name);
        EasyMock.verify(mockLocationProvider);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#requestLocationUpdates(java.lang.String, long, float, int, int, com.telenav.location.ITnLocationListener)}.
     */
    @Test
    public void testRequestLocationUpdatesStringLongFloatIntIntITnLocationListener()
    {
        mockLocationProvider.requestLocationUpdates(1, 1, 1, 1, null);
        EasyMock.replay(mockLocationProvider);
        TnLocationManager.getInstance().requestLocationUpdates("critea", 1, 1, 1, 1, null);
        EasyMock.verify(mockLocationProvider);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#sendExtraCommand(java.lang.String, java.lang.String, java.util.Hashtable)}.
     */
    @Test
    public void testSendExtraCommand()
    {
        EasyMock.expect(mockLocationProvider.sendExtraCommand("command", null)).andReturn(Boolean.valueOf(true));
        EasyMock.replay(mockLocationProvider);
        boolean isSend = TnLocationManager.getInstance().sendExtraCommand("mock", "command", null);
        assertTrue(isSend);
    }

    /**
     * Test method for {@link com.telenav.location.TnLocationManager#reset(java.lang.String)}.
     */
    @Test
    public void testReset()
    {
        Hashtable mockproviders = EasyMock.createMock(Hashtable.class);
        Whitebox.setInternalState(TnLocationManager.getInstance(), "providers", mockproviders);
        EasyMock.expect(mockproviders.remove("mock")).andReturn(mockLocationProvider);
        mockLocationProvider.reset();
        EasyMock.replay(mockproviders);
        EasyMock.replay(mockLocationProvider);
        TnLocationManager.getInstance().reset("mock");
        EasyMock.verify(mockproviders);
        EasyMock.verify(mockLocationProvider);
    }

}

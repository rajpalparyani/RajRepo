/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AlongRouteLocationProvider.java
 *
 */
package com.telenav.gps;

/**
 *@author jyxu
 *@date 2011-7-1
 */

import org.easymock.EasyMock;

import junit.framework.TestCase;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;

public class AlongRouteLocationProviderTest extends TestCase
{
    AlongRouteLocationProvider provider;
    
    public void setUp()throws Exception
    {
        provider = new AlongRouteLocationProvider("test");
    }
    
    public void testGetLocationDelegateWithNoGps() throws TnLocationException
    {
        provider.isNoGps = true;
        assertNull(provider.getLocationDelegate(100));
    }
    
    public void testGetLocationDelegateWithGpsAndNotOnRoute()throws TnLocationException
    {
        provider.isNoGps = false;
        provider.onRoute = null;
        TnLocation location = provider.getLocationDelegate(100);
        assertEquals(provider.originLat, location.getLatitude());
        assertEquals(provider.originLon, location.getLongitude());
        assertEquals(4, location.getSatellites());
        assertEquals(5, location.getAccuracy());
    }
    
    public void testGetLocationDelegateWithGpsAndOnRouteAndSpiral()throws TnLocationException
    {
        provider.isNoGps = false;
        NavState navState = EasyMock.createMock("NavState", NavState.class);
        provider.onRoute = navState;
        provider.isSpiral = true;
        TnLocation location = provider.getLocationDelegate(100);
        
        //recover to default value
        provider.spiralRadius = 10000;
        provider.spiralHeading = 0;
        int expLat = provider.originLat + (int) DataUtil.xCosY(provider.spiralRadius / 10, provider.spiralHeading / 10);
        int expLon = provider.originLon + (int) DataUtil.xSinY(provider.spiralRadius / 10, provider.spiralHeading / 10);
        assertEquals(expLat, location.getLatitude());
        assertEquals(expLon, location.getLongitude());
        

        int expHeading = provider.spiralHeading / 10 + 90;
        expHeading = expHeading % 360;
        assertEquals(expHeading, location.getHeading());
        
        assertEquals(4, location.getSatellites());
        assertEquals(5, location.getAccuracy());
     }  
    
    public void testGetLocationDelegateWithGpsAndOnRouteAndNotSpiral_1()throws TnLocationException
    {  

        NavState navState = EasyMock.createMock("NavState", NavState.class);
        EasyMock.expect(navState.isAtTheEndOfRoute()).andReturn(true);
        EasyMock.replay(navState);
        provider.isNoGps = false;
        provider.onRoute = navState;
        provider.isSpiral = false;
        TnLocation location = provider.getLocationDelegate(100);
        assertEquals(provider.lat, location.getLatitude());
        assertEquals(provider.lon, location.getLongitude());
        assertEquals(4, location.getSatellites());
        assertEquals(5, location.getAccuracy());
        assertEquals(0, location.getHeading());
        assertEquals(0, location.getSpeed());
        EasyMock.verify(navState);
    }  
    
    public void testGetLocationDelegateWithGpsAndOnRouteAndNotSpiral_2()throws TnLocationException
    {  

        NavState navState = EasyMock.createMock("NavState", NavState.class);
        EasyMock.expect(navState.isAtTheEndOfRoute()).andReturn(false);
        EasyMock.replay(navState);
        provider.isNoGps = false;
        provider.onRoute = navState;
        provider.isSpiral = false;
        provider.counter = 3; //test counter < 5
        TnLocation location = provider.getLocationDelegate(100);
        assertEquals(provider.lat, location.getLatitude());
        assertEquals(provider.lon, location.getLongitude());
        assertEquals(4, location.getSatellites());
        assertEquals(5, location.getAccuracy());
        assertEquals(0, location.getHeading());
        assertEquals(0, location.getSpeed());
        EasyMock.verify(navState);
    }  
    
    public void testGetLocationDelegateWithGpsAndOnRouteAndNotSpiral_3() throws TnLocationException
    {  

        NavState navState = EasyMock.createMock("NavState", NavState.class);
        EasyMock.expect(navState.isAtTheEndOfRoute()).andReturn(false);
        EasyMock.replay(navState);
        provider.isNoGps = false;
        provider.onRoute = navState;
        provider.isSpiral = false;
        provider.counter = 6; //test counter > 5
        provider.isDeviation = true;
        TnLocation location = provider.getLocationDelegate(100);
        assertEquals(provider.lat, location.getLatitude());
        assertEquals(provider.lon, location.getLongitude());
        assertEquals(4, location.getSatellites());
        assertEquals(5, location.getAccuracy());
        assertEquals(0, location.getHeading());
        assertEquals(0, location.getSpeed());
        EasyMock.verify(navState);
    }  
    
    public void testGetLocationDelegateWithGpsAndOnRouteAndNotSpiral_4() throws TnLocationException
    {  

        //faked nav location
        TnNavLocation navLocation = new TnNavLocation("test");
        navLocation.setLatitude(3737890);
        navLocation.setLongitude(-12201074);
        NavState navState = EasyMock.createMock("NavState", NavState.class);
        EasyMock.expect(navState.isAtTheEndOfRoute()).andReturn(false);
        EasyMock.expect(navState.getCurrentShapePointLat()).andReturn(100000);
        EasyMock.expect(navState.getCurrentShapePointLon()).andReturn(50000);
        EasyMock.expect(navState.nextLat()).andReturn(100010);
        EasyMock.expect(navState.nextLon()).andReturn(50010);
        
        
        EasyMock.replay(navState);
        provider.isNoGps = false;
        provider.onRoute = navState;
        provider.isSpiral = false;
        provider.counter = 6; //test counter > 5
        provider.isDeviation = false;
        TnLocation location = provider.getLocationDelegate(100);
        assertEquals(provider.lat, location.getLatitude());
        assertEquals(provider.lon, location.getLongitude());
        assertEquals(4, location.getSatellites());
        assertEquals(5, location.getAccuracy());
        
        EasyMock.verify(navState);
    }  

    public void testSetOrigin()
    {
        int expectLat = 12345678;
        int expectLon = 87654321;
        provider.setOrigin(expectLat, expectLon);
        assertEquals(expectLat, provider.lat);
        assertEquals(expectLon, provider.lon);
        assertEquals(expectLat, provider.originLat);
        assertEquals(expectLon, provider.originLon);
    }

    public void testSetNavState()
    {
        NavState navState = EasyMock.createMock("NavState", NavState.class);
        provider.setNavState(navState);
        assertSame(navState, provider.onRoute);
    }

    public void testSetDeviation()
    {
        provider.setDeviation(false);
        assertFalse(provider.isDeviation);
        provider.setDeviation(true);
        assertTrue(provider.isDeviation);
    }
}

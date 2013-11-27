/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidLocationUtilTest.java
 *
 */
/**
 * 
 */
package com.telenav.location.android;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Criteria.class, AndroidLocationUtil.class})
public class AndroidLocationUtilTest
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
     * Test method for {@link com.telenav.location.android.AndroidLocationUtil#convertNativeProvider(java.lang.String)}.
     */
    @Test
    public void testConvertNativeProvider()
    {
        assertEquals(LocationManager.GPS_PROVIDER, AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER));
    }

    @Test
    public void testConvertNativeProviderNull()
    {
        assertNull(null, AndroidLocationUtil.convertNativeProvider(null));
    }
    @Test
    public void testConvertNativeProviderNetwork()
    {
        assertEquals(LocationManager.NETWORK_PROVIDER, AndroidLocationUtil.convertNativeProvider(TnLocationManager.NETWORK_PROVIDER));
    }
    
    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationUtil#convert(com.telenav.location.TnCriteria)}.
     * @throws Exception 
     */
    @Test
    public void testConvertTnCriteria() throws Exception
    {
        PowerMock.resetAll();
        TnCriteria testCriteria = new TnCriteria();
        testCriteria.setAccuracy(TnCriteria.ACCURACY_COARSE);
        testCriteria.setAltitudeRequired(true);
        testCriteria.setBearingRequired(true);
        testCriteria.setCostAllowed(true);
        testCriteria.setHorizontalAccuracy(20);
        testCriteria.setPowerRequirement(TnCriteria.POWER_MEDIUM);
        testCriteria.setSpeedRequired(true);
        testCriteria.setVerticalAccuracy(20);
        Criteria mockNativeCriteria = PowerMock.createMock(Criteria.class);
        PowerMock.expectNew(Criteria.class).andReturn(mockNativeCriteria);
        mockNativeCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        mockNativeCriteria.setAltitudeRequired(true);
        mockNativeCriteria.setBearingRequired(true);
        mockNativeCriteria.setCostAllowed(true);
        mockNativeCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        mockNativeCriteria.setSpeedRequired(true);
        PowerMock.replayAll();
        Criteria nativeCriteria = AndroidLocationUtil.convert(testCriteria);
        assertEquals(mockNativeCriteria, nativeCriteria);
        PowerMock.verifyAll();
    }

    @Test
    public void testConvertTnCriteriaA() throws Exception
    {
        PowerMock.resetAll();
        TnCriteria testCriteria = new TnCriteria();
        testCriteria.setAccuracy(TnCriteria.NO_REQUIREMENT);
        testCriteria.setAltitudeRequired(true);
        testCriteria.setBearingRequired(true);
        testCriteria.setCostAllowed(true);
        testCriteria.setHorizontalAccuracy(20);
        testCriteria.setPowerRequirement(TnCriteria.POWER_HIGH);
        testCriteria.setSpeedRequired(true);
        testCriteria.setVerticalAccuracy(20);
        Criteria mockNativeCriteria = PowerMock.createMock(Criteria.class);
        PowerMock.expectNew(Criteria.class).andReturn(mockNativeCriteria);
        mockNativeCriteria.setAccuracy(Criteria.NO_REQUIREMENT);
        mockNativeCriteria.setAltitudeRequired(true);
        mockNativeCriteria.setBearingRequired(true);
        mockNativeCriteria.setCostAllowed(true);
        mockNativeCriteria.setPowerRequirement(Criteria.POWER_HIGH);
        mockNativeCriteria.setSpeedRequired(true);
        PowerMock.replayAll();
        Criteria nativeCriteria = AndroidLocationUtil.convert(testCriteria);
        assertEquals(mockNativeCriteria, nativeCriteria);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testConvertTnCriteriaB() throws Exception
    {
        PowerMock.resetAll();
        TnCriteria testCriteria = new TnCriteria();
        testCriteria.setAccuracy(TnCriteria.ACCURACY_FINE);
        testCriteria.setAltitudeRequired(true);
        testCriteria.setBearingRequired(true);
        testCriteria.setCostAllowed(true);
        testCriteria.setHorizontalAccuracy(20);
        testCriteria.setPowerRequirement(TnCriteria.POWER_LOW);
        testCriteria.setSpeedRequired(true);
        testCriteria.setVerticalAccuracy(20);
        Criteria mockNativeCriteria = PowerMock.createMock(Criteria.class);
        PowerMock.expectNew(Criteria.class).andReturn(mockNativeCriteria);
        mockNativeCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mockNativeCriteria.setAltitudeRequired(true);
        mockNativeCriteria.setBearingRequired(true);
        mockNativeCriteria.setCostAllowed(true);
        mockNativeCriteria.setPowerRequirement(Criteria.POWER_LOW);
        mockNativeCriteria.setSpeedRequired(true);
        PowerMock.replayAll();
        Criteria nativeCriteria = AndroidLocationUtil.convert(testCriteria);
        assertEquals(mockNativeCriteria, nativeCriteria);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testConvertTnCriteriaC() throws Exception
    {
        PowerMock.resetAll();
        TnCriteria testCriteria = new TnCriteria();
        testCriteria.setAccuracy(TnCriteria.NO_REQUIREMENT);
        testCriteria.setAltitudeRequired(true);
        testCriteria.setBearingRequired(true);
        testCriteria.setCostAllowed(true);
        testCriteria.setHorizontalAccuracy(20);
        testCriteria.setPowerRequirement(TnCriteria.NO_REQUIREMENT);
        testCriteria.setSpeedRequired(true);
        testCriteria.setVerticalAccuracy(20);
        Criteria mockNativeCriteria = PowerMock.createMock(Criteria.class);
        PowerMock.expectNew(Criteria.class).andReturn(mockNativeCriteria);
        mockNativeCriteria.setAccuracy(Criteria.NO_REQUIREMENT);
        mockNativeCriteria.setAltitudeRequired(true);
        mockNativeCriteria.setBearingRequired(true);
        mockNativeCriteria.setCostAllowed(true);
        mockNativeCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        mockNativeCriteria.setSpeedRequired(true);
        PowerMock.replayAll();
        Criteria nativeCriteria = AndroidLocationUtil.convert(testCriteria);
        assertEquals(mockNativeCriteria, nativeCriteria);
        PowerMock.verifyAll();
    }
    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationUtil#convert(android.location.Location)}.
     */
    @Test
    public void testConvertLocation()
    {
        PowerMock.resetAll();
        Location location = PowerMock.createMock(Location.class);
        EasyMock.expect(location.getProvider()).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(location.getAccuracy()).andReturn(Float.valueOf(60));
        EasyMock.expect(location.getAltitude()).andReturn(Double.valueOf(60));
        EasyMock.expect(location.getBearing()).andReturn(Float.valueOf(20));
        EasyMock.expect(location.getLatitude()).andReturn(Double.valueOf(-122.43243));
        EasyMock.expect(location.getLongitude()).andReturn(Double.valueOf(21.43243));
        EasyMock.expect(location.getSpeed()).andReturn(Float.valueOf(60.0f));
        EasyMock.expect(location.getTime()).andReturn(Long.valueOf(System.currentTimeMillis()));
        PowerMock.replayAll();
        AndroidLocationUtil.convert(location);
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationUtil#convertStatus(int)}.
     */
    @Test
    public void testConvertStatusA()
    {
        assertEquals(TnLocationProvider.AVAILABLE, AndroidLocationUtil.convertStatus(LocationProvider.AVAILABLE));
    }
    
    @Test
    public void testConvertStatusB()
    {
        assertEquals(TnLocationProvider.OUT_OF_SERVICE, AndroidLocationUtil.convertStatus(LocationProvider.OUT_OF_SERVICE));
    }
    
    @Test
    public void testConvertStatusC()
    {
        assertEquals(TnLocationProvider.TEMPORARILY_UNAVAILABLE, AndroidLocationUtil.convertStatus(LocationProvider.TEMPORARILY_UNAVAILABLE));
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationUtil#convertProvider(java.lang.String)}.
     */
    @Test
    public void testConvertProviderA()
    {
        assertEquals(TnLocationManager.GPS_179_PROVIDER, AndroidLocationUtil.convertProvider(LocationManager.GPS_PROVIDER));
    }

    @Test
    public void testConvertProviderB()
    {
        assertEquals(TnLocationManager.NETWORK_PROVIDER, AndroidLocationUtil.convertProvider(LocationManager.NETWORK_PROVIDER));
    }
    
    @Test
    public void testConvertProviderC()
    {
        assertEquals(TnLocationManager.PASSIVE_PROVIDER, AndroidLocationUtil.convertProvider(LocationManager.PASSIVE_PROVIDER));
    }
}

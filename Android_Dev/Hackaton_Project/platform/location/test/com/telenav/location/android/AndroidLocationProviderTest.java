/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidLocationProviderTest.java
 *
 */
/**
 * 
 */
package com.telenav.location.android;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidLocationProvider.class, AndroidLocationUtil.class, Bundle.class, Looper.class})
public class AndroidLocationProviderTest
{
    AndroidLocationProvider androidLocationProvider;
    LocationManager mockLocMgr;
    LocationProvider locProvider;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        mockLocMgr = PowerMock.createMock(LocationManager.class);
        locProvider = PowerMock.createMock(LocationProvider.class);
        androidLocationProvider = new AndroidLocationProvider(TnLocationManager.GPS_179_PROVIDER,mockLocMgr, locProvider);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#meetsCriteria(com.telenav.location.TnCriteria)}.
     */
    @Test
    public void testMeetsCriteria()
    {
        TnCriteria tnCriteria = PowerMock.createMock(TnCriteria.class);
        Criteria mockCriteria = PowerMock.createMock(Criteria.class);
        PowerMock.mockStatic(AndroidLocationUtil.class);
        EasyMock.expect(AndroidLocationUtil.convert(tnCriteria)).andReturn(mockCriteria);
        EasyMock.expect(locProvider.meetsCriteria(mockCriteria)).andReturn(Boolean.valueOf(true));
        PowerMock.replayAll();
        boolean isTrue = androidLocationProvider.meetsCriteria(tnCriteria);
        assertTrue(isTrue);
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#reset()}.
     */
    @Test
    public void testReset()
    {
        mockLocMgr.removeUpdates(EasyMock.anyObject(AndroidLocationProvider.class));
        androidLocationProvider.looper =  PowerMock.createMock(Looper.class);
        androidLocationProvider.looper.quit();
        PowerMock.replayAll();
        androidLocationProvider.reset();
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#getLastKnownLocation()}.
     * @throws TnLocationException 
     */
    @Test
    public void testGetLastKnownLocation() throws TnLocationException
    {
        PowerMock.resetAll();
        PowerMock.mockStatic(AndroidLocationUtil.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        Location location = PowerMock.createMock(Location.class);
        TnLocation tnMockLocation = PowerMock.createMock(TnLocation.class);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(mockLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)).andReturn(location);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(tnMockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(3));
        tnMockLocation.setSatellites(3);
        PowerMock.replayAll();
        TnLocation currentLoc = androidLocationProvider.getLastKnownLocation();
        assertEquals(tnMockLocation, currentLoc);
        PowerMock.verifyAll();
    }

    @Test
    public void testGetLastKnownLocationB() throws TnLocationException
    {
        PowerMock.resetAll();
        PowerMock.mockStatic(AndroidLocationUtil.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        Location location = PowerMock.createMock(Location.class);
        TnLocation tnMockLocation = PowerMock.createMock(TnLocation.class);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(mockLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)).andReturn(location);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(tnMockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(location.getAccuracy()).andReturn(Float.valueOf(60f));
        tnMockLocation.setSatellites(0);
        PowerMock.replayAll();
        TnLocation currentLoc = androidLocationProvider.getLastKnownLocation();
        assertEquals(tnMockLocation, currentLoc);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetLastKnownLocationC() throws TnLocationException
    {
        PowerMock.resetAll();
        PowerMock.mockStatic(AndroidLocationUtil.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        Location location = PowerMock.createMock(Location.class);
        TnLocation tnMockLocation = PowerMock.createMock(TnLocation.class);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(mockLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)).andReturn(location);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(tnMockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(location.getAccuracy()).andReturn(Float.valueOf(50f));
        tnMockLocation.setSatellites(1);
        PowerMock.replayAll();
        TnLocation currentLoc = androidLocationProvider.getLastKnownLocation();
        assertEquals(tnMockLocation, currentLoc);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetLastKnownLocationD() throws TnLocationException
    {
        PowerMock.resetAll();
        PowerMock.mockStatic(AndroidLocationUtil.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        Location location = PowerMock.createMock(Location.class);
        TnLocation tnMockLocation = PowerMock.createMock(TnLocation.class);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(mockLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)).andReturn(location);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(tnMockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(location.getAccuracy()).andReturn(Float.valueOf(40f));
        tnMockLocation.setSatellites(2);
        PowerMock.replayAll();
        TnLocation currentLoc = androidLocationProvider.getLastKnownLocation();
        assertEquals(tnMockLocation, currentLoc);
        PowerMock.verifyAll();
    }
    @Test
    public void testGetLastKnownLocationE() throws TnLocationException
    {
        PowerMock.resetAll();
        PowerMock.mockStatic(AndroidLocationUtil.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        Location location = PowerMock.createMock(Location.class);
        TnLocation tnMockLocation = PowerMock.createMock(TnLocation.class);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(mockLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)).andReturn(location);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(tnMockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(location.getAccuracy()).andReturn(Float.valueOf(10f));
        tnMockLocation.setSatellites(4);
        PowerMock.replayAll();
        TnLocation currentLoc = androidLocationProvider.getLastKnownLocation();
        assertEquals(tnMockLocation, currentLoc);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetLastKnownLocationF() throws TnLocationException
    {
        PowerMock.resetAll();
        PowerMock.mockStatic(AndroidLocationUtil.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        Location location = PowerMock.createMock(Location.class);
        TnLocation tnMockLocation = PowerMock.createMock(TnLocation.class);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(mockLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)).andReturn(location);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(tnMockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(location.getAccuracy()).andReturn(Float.valueOf(5f));
        tnMockLocation.setSatellites(5);
        PowerMock.replayAll();
        TnLocation currentLoc = androidLocationProvider.getLastKnownLocation();
        assertEquals(tnMockLocation, currentLoc);
        PowerMock.verifyAll();
    }
    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#getLocation(int)}.
     * @throws TnLocationException 
     */
    @Test
    public void testGetLocation() throws TnLocationException
    {
        PowerMock.resetAll();
        PowerMock.mockStatic(AndroidLocationUtil.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        Location location = PowerMock.createMock(Location.class);
        TnLocation tnMockLocation = PowerMock.createMock(TnLocation.class);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(mockLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER)).andReturn(location);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(tnMockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(3));
        tnMockLocation.setSatellites(3);
        PowerMock.replayAll();
        TnLocation currentLoc = androidLocationProvider.getLocation(1000);
        assertEquals(tnMockLocation, currentLoc);
        PowerMock.verifyAll();
    }

//    /**
//     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#requestLocationUpdates(long, float, int, int, com.telenav.location.ITnLocationListener)}.
//     */
//    @Test
//    public void testRequestLocationUpdates()
//    {
//        PowerMock.mockStatic(Looper.class);
//        this.androidLocationProvider.looper = PowerMock.createMock(Looper.class);
//        this.mockLocMgr.removeUpdates(androidLocationProvider);
//        this.androidLocationProvider.looper.quit();
//        PowerMock.mockStatic(AndroidLocationUtil.class);
//        Looper.prepare();
//        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
//        mockLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//            10, 10, androidLocationProvider, this.androidLocationProvider.looper);
//        Looper.loop();
//        PowerMock.replayAll();
//        androidLocationProvider.requestLocationUpdates(10L, 10L, 0, 0, null);
//        try
//        {
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        PowerMock.verifyAll();
//    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#sendExtraCommand(java.lang.String, java.util.Hashtable)}.
     * @throws Exception 
     */
    @Test
    public void testSendExtraCommand() throws Exception
    {
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        PowerMock.expectNew(Bundle.class).andReturn(mockBundle);
        Hashtable extras = new Hashtable();
        String intKey = "int";
        String boolKey = "boolean";
        String stringKey = "string";
        String stringValue = "abc";
        extras.put(intKey, Integer.valueOf(10));
        extras.put(boolKey, Boolean.valueOf(true));
        extras.put(stringKey, stringValue);
        EasyMock.expect(
            this.mockLocMgr.sendExtraCommand(EasyMock.anyObject(String.class),
                EasyMock.anyObject(String.class), EasyMock.anyObject(Bundle.class)))
                .andReturn(Boolean.valueOf(true));
        mockBundle.putInt(intKey, 10);
        mockBundle.putBoolean(boolKey, true);
        mockBundle.putString(stringKey, stringValue);
        PowerMock.replayAll();
        boolean isTrue = this.androidLocationProvider.sendExtraCommand("abc", extras);
        assertTrue(isTrue);
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#AndroidLocationProvider(java.lang.String, android.location.LocationManager, android.location.LocationProvider)}.
     */
    @Test
    public void testAndroidLocationProvider()
    {
        assertNotNull(androidLocationProvider);
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#onLocationChanged(android.location.Location)}.
     * @throws Exception 
     */
    @Test
    public void testOnLocationChanged() throws Exception
    {
        PowerMock.resetAll();
        androidLocationProvider.listener = PowerMock.createMock(ITnLocationListener.class);
        TnLocation mockLocation = PowerMock.createMock(TnLocation.class);
        Location location = PowerMock.createMock(Location.class);
        PowerMock.mockStatic(Bundle.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        PowerMock.mockStatic(AndroidLocationUtil.class);
        EasyMock.expect(AndroidLocationUtil.convert(location)).andReturn(mockLocation);
        EasyMock.expect(location.getExtras()).andReturn(mockBundle).anyTimes();
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockBundle.getInt(EasyMock.anyObject(String.class))).andReturn(Integer.valueOf(3));
        mockLocation.setSatellites(3);
        androidLocationProvider.listener.onLocationChanged(androidLocationProvider, mockLocation);
        EasyMock.expect(mockLocation.getAccuracy()).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockLocation.getHeading()).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockLocation.getLatitude()).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockLocation.getLocalTimeStamp()).andReturn(Long.valueOf(0));
        EasyMock.expect(mockLocation.getLongitude()).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockLocation.getSpeed()).andReturn(Integer.valueOf(0));
        EasyMock.expect(mockLocation.getTime()).andReturn(Long.valueOf(0));
        EasyMock.expect(mockLocation.getSatellites()).andReturn(Integer.valueOf(3));
        PowerMock.replayAll();
        androidLocationProvider.onLocationChanged(location);
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationProvider#onStatusChanged(java.lang.String, int, android.os.Bundle)}.
     */
    @Test
    public void testOnStatusChanged()
    {
        PowerMock.mockStatic(Bundle.class);
        PowerMock.mockStatic(AndroidLocationUtil.class);
        Bundle mockBundle = PowerMock.createMock(Bundle.class);
        EasyMock.expect(AndroidLocationUtil.convertStatus(LocationProvider.AVAILABLE)).andReturn(TnLocationProvider.AVAILABLE);
        androidLocationProvider.listener = PowerMock.createMock(ITnLocationListener.class);
        androidLocationProvider.listener.onStatusChanged(androidLocationProvider, LocationProvider.AVAILABLE);
        EasyMock.expect(mockBundle.getInt("satellites")).andReturn(Integer.valueOf(1));
        PowerMock.replayAll();
        androidLocationProvider.onStatusChanged(TnLocationManager.GPS_179_PROVIDER, LocationProvider.AVAILABLE, mockBundle);
        PowerMock.verifyAll();
    }
}

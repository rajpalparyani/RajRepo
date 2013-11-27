/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidLocationManagerTest.java
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

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidLocationManager.class, AndroidLocationUtil.class})
public class AndroidLocationManagerTest
{

    AndroidLocationManager androidLocationMgr;
    Context context;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        context = PowerMock.createMock(Context.class);
        androidLocationMgr = new AndroidLocationManager(context);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationManager#getProviderDelegate(java.lang.String)}.
     */
    @Test
    public void testGetProviderDelegate()
    {
        LocationManager locationMgr = PowerMock.createMock(LocationManager.class);
        LocationProvider locationProvider = PowerMock.createMock(LocationProvider.class);
        EasyMock.expect(context.getSystemService(Context.LOCATION_SERVICE)).andReturn(locationMgr);
        EasyMock.expect(locationMgr.getProvider(LocationManager.GPS_PROVIDER)).andReturn(locationProvider);
        PowerMock.replayAll(context, locationMgr);
        TnLocationProvider tnLocationProvider = androidLocationMgr.getProviderDelegate(TnLocationManager.GPS_179_PROVIDER);
        assertNotNull(tnLocationProvider);
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationManager#getProvider(com.telenav.location.TnCriteria)}.
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     */
    @Test
    public void testGetProviderTnCriteria() throws SecurityException, NoSuchMethodException
    {
        PowerMock.resetAll();
        TnCriteria criteria = PowerMock.createMock(TnCriteria.class);
        Criteria mockcriteria = PowerMock.createMock(Criteria.class);
        LocationManager locationMgr = PowerMock.createMock(LocationManager.class);
        LocationProvider nativeMocklocationProvider = PowerMock.createMock(LocationProvider.class);
//        TnLocationProvider locationProvider = PowerMock.createMock(TnLocationProvider.class);
        EasyMock.expect(context.getSystemService(Context.LOCATION_SERVICE)).andReturn(locationMgr).anyTimes();
        EasyMock.expect(locationMgr.getBestProvider(EasyMock.anyObject(Criteria.class), EasyMock.eq(true))).andReturn(LocationManager.GPS_PROVIDER);
        PowerMock.mockStatic(AndroidLocationUtil.class);
        EasyMock.expect(AndroidLocationUtil.convert(criteria)).andReturn(mockcriteria);
        EasyMock.expect(AndroidLocationUtil.convertProvider(LocationManager.GPS_PROVIDER)).andReturn(TnLocationManager.GPS_179_PROVIDER);
        EasyMock.expect(AndroidLocationUtil.convertNativeProvider(TnLocationManager.GPS_179_PROVIDER)).andReturn(LocationManager.GPS_PROVIDER);
        EasyMock.expect(locationMgr.getProvider(LocationManager.GPS_PROVIDER)).andReturn(nativeMocklocationProvider);
        PowerMock.replayAll(context, locationMgr);
        TnLocationProvider tnLocationProvider = androidLocationMgr.getProvider(criteria);
        assertNotNull(tnLocationProvider);
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationManager#isGpsProviderAvailable(java.lang.String)}.
     */
    @Test
    public void testIsGpsProviderAvailable()
    {
        PowerMock.resetAll();
        LocationManager locationMgr = PowerMock.createMock(LocationManager.class);
        LocationProvider locProvider = PowerMock.createMock(LocationProvider.class);
        EasyMock.expect(context.getSystemService(Context.LOCATION_SERVICE)).andReturn(locationMgr);
        EasyMock.expect(locationMgr.getProvider(LocationManager.GPS_PROVIDER)).andReturn(locProvider);
        EasyMock.expect(locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)).andReturn(Boolean.valueOf(true));
        PowerMock.replayAll(context, locationMgr, locProvider);
        boolean isAvailable = androidLocationMgr.isGpsProviderAvailable(TnLocationManager.GPS_179_PROVIDER);
        assertTrue(isAvailable);
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidLocationManager#AndroidLocationManager(android.content.Context)}.
     */
    @Test
    public void testAndroidLocationManager()
    {
        assertNotNull(androidLocationMgr);
    }

}

/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DashboardManagerTest.java
 *
 */
package com.telenav.module.dashboard;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.location.TnLocation;
import com.telenav.module.dashboard.DashboardManager.RequestInfo;

/**
 *@author fqming
 *@date 2013-5-29
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.telenav.module.dashboard.DashboardManager"})
public class DashboardManagerTest
{
    DashboardManager dashboardManager;
    
    @BeforeClass
    public static void beforeClass()
    {
        
    }
    
    @Before
    public void beforeTest() throws Exception
    {
        dashboardManager = PowerMock.createPartialMock(DashboardManager.class, "getMinMapWidthHeight");
    }
    
    @Test
    public void testCheckRequestInfo_CurentLocationNull()
    {
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testCheckRequestInfo_RequestInfoNull()
    {
        dashboardManager.currentLocation = new TnLocation("");
        boolean result = dashboardManager.checkRequestInfo(null, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testCheckRequestInfo_Requesting_Timeout_Distance_beyond()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //requesting & timeout
        requestInfo.lastRequestTimestamp = System.currentTimeMillis() - DashboardManager.REQUEST_TIMEOUT - 1000;
        
        requestInfo.responseTimestamp = -1;
        
        //San Francisco, very far from current location
        TnLocation requestLocation = prepareLocation(3777885, -12242134);
        
        requestInfo.location = requestLocation;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertTrue(result);
        
        //requestInfo will be reset.
        Assert.assertEquals(0, requestInfo.lastRequestTimestamp);
        Assert.assertEquals(0, requestInfo.responseTimestamp);
        Assert.assertEquals(null, requestInfo.location);
    }
    
    @Test
    public void testCheckRequestInfo_Requesting_Timeout_Retry_beyond()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //requesting & timeout
        requestInfo.lastRequestTimestamp = System.currentTimeMillis() - DashboardManager.REQUEST_TIMEOUT - 1000;
        
        requestInfo.responseTimestamp = -1;
        
        //it's near current location.
        TnLocation requestLocation = prepareLocation(3737890, -12202500);
        
        requestInfo.location = requestLocation;
        
        //retry time exceeds max value.
        requestInfo.retryTimes = 2;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testCheckRequestInfo_Requesting_Timeout_Retry()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //requesting & timeout
        requestInfo.lastRequestTimestamp = System.currentTimeMillis() - DashboardManager.REQUEST_TIMEOUT - 1000;
        
        requestInfo.responseTimestamp = -1;
        
        //it's near current location
        TnLocation requestLocation = prepareLocation(3737890, -12202500);
        
        requestInfo.location = requestLocation;
        
        //max retry time is 2
        requestInfo.retryTimes = 1;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertTrue(result);
        
        //It will add 1
        Assert.assertEquals(requestInfo.retryTimes, 2);
    }
    
    @Test
    public void testCheckRequestInfo_Requesting_No_Timeout()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //requesting BUT no timeout
        requestInfo.lastRequestTimestamp = System.currentTimeMillis();
        
        requestInfo.responseTimestamp = -1;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertFalse(result);
        
        //It will do nothing but return.
    }
    
    @Test
    public void testCheckRequestInfo_Not_Requesting_Distance_beyond()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //Not requesting yet.
        requestInfo.lastRequestTimestamp = 0;
        
        requestInfo.responseTimestamp = 0;
        
        //San Francisco, very far from current location
        TnLocation requestLocation = prepareLocation(3777885, -12242134);
        
        requestInfo.location = requestLocation;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertTrue(result);
        
        //requestInfo will be reset.
        Assert.assertEquals(0, requestInfo.lastRequestTimestamp);
        Assert.assertEquals(0, requestInfo.responseTimestamp);
        Assert.assertEquals(null, requestInfo.location);
    }
    
    @Test
    public void testCheckRequestInfo_Not_Requesting_Within_Distance_Last_Success_Last_Response_Timeout()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //Not requesting yet.
        requestInfo.lastRequestTimestamp = 0;
        
        //last request succesful
        requestInfo.isSucc = true;
        
        //last response no longer valid
        requestInfo.responseTimestamp = System.currentTimeMillis() - DashboardManager.WEATHER_VALID_TIME - 1000;
        
        //it's near current location
        TnLocation requestLocation = prepareLocation(3737890, -12202500);
        
        requestInfo.location = requestLocation;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertTrue(result);
        
        //requestInfo will be reset.
        Assert.assertEquals(0, requestInfo.lastRequestTimestamp);
        Assert.assertEquals(0, requestInfo.responseTimestamp);
        Assert.assertEquals(false, requestInfo.isSucc);
        Assert.assertEquals(null, requestInfo.location);
    }
    
    @Test
    public void testCheckRequestInfo_Not_Requesting_Within_Distance_Last_Success_Last_Response_Valid()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //Not requesting yet.
        requestInfo.lastRequestTimestamp = 0;
        
        //last request succeeded
        requestInfo.isSucc = true;
        
        //last response no longer valid
        requestInfo.responseTimestamp = System.currentTimeMillis();
        
        //it's near current location
        TnLocation requestLocation = prepareLocation(3737890, -12202500);
        
        requestInfo.location = requestLocation;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertFalse(result);
        
        //Do nothing but return.
    }
    
    @Test
    public void testCheckRequestInfo_Not_Requesting_Within_Distance_Last_Fail_Retry()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //Not requesting yet.
        requestInfo.lastRequestTimestamp = 0;
        
        //last request failed
        requestInfo.isSucc = false;
        
        //last response no longer valid
        requestInfo.responseTimestamp = System.currentTimeMillis();
        
        //still can retry.
        requestInfo.retryTimes = 0;
        
        //it's near current location
        TnLocation requestLocation = prepareLocation(3737890, -12202500);
        
        requestInfo.location = requestLocation;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertTrue(result);
        
        //It will add 1.
        Assert.assertEquals(1, requestInfo.retryTimes);
    }
    
    @Test
    public void testCheckRequestInfo_Not_Requesting_Within_Distance_Last_Fail_Cant_Retry()
    {
        TnLocation currentLocation = prepareLocation(3737392, -12201074);
        
        RequestInfo requestInfo = dashboardManager.new RequestInfo();
        
        dashboardManager.currentLocation = currentLocation;
        
        //Not requesting yet.
        requestInfo.lastRequestTimestamp = 0;
        
        //last request failed
        requestInfo.isSucc = false;
        
        //last response no longer valid
        requestInfo.responseTimestamp = System.currentTimeMillis();
        
        //Can't retry
        requestInfo.retryTimes = 2;
        
        //it's near current location
        TnLocation requestLocation = prepareLocation(3737890, -12202500);
        
        requestInfo.location = requestLocation;
        
        boolean result = dashboardManager.checkRequestInfo(requestInfo, DashboardManager.WEATHER_VALID_TIME, DashboardManager.WEATHER_GPS_VALID_RANGE);
        Assert.assertFalse(result);
        
        //Do nothing but return.
    }
    
    @After
    public void tearDown()
    {
        
    }
    
    public TnLocation prepareLocation(int lat, int lon)
    {
        TnLocation location = new TnLocation("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        location.setSatellites(4);
        location.setTime(System.currentTimeMillis());
        
        return location;
    }
}

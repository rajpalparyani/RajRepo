/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestGpsService.java
 *
 */
package com.telenav.gps;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnLocationManager.class,TnLocationManager.class,IGpsListener.class})

public class GpsServiceTest extends TestCase implements IGpsCallback
{
    int testType;
    Object mutex = new Object();
    boolean isSuccess = false;
    
    public void testGpsServiceCallback()
    {
        PowerMock.mockStatic(TnLocationManager.class);
        TnLocationManager mockLocationManager = PowerMock.createMock(TnLocationManager.class);
        
        //record
        EasyMock.expect(TnLocationManager.getInstance()).andReturn(mockLocationManager).anyTimes();
        mockLocationManager.requestLocationUpdates(EasyMock.anyObject(String.class), EasyMock.anyLong(), EasyMock.anyFloat(), EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyObject(ITnLocationListener.class));
        
        //replay
        PowerMock.replayAll();
        
        TnLocation[] data = new TnLocation[1];
        data[0] = new TnLocation("");
        GpsService gpsService = new GpsService("Unit Test", null);
        gpsService.start();
        
        //timeout
        isSuccess = false;
        testType = GpsService.STATUS_TIMEOUT;
        gpsService.getFixes(1, data, 3000, this);
        synchronized(mutex)
        {
            try
            {
                mutex.wait(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        assertTrue(isSuccess);
        
        //success
        TnLocation location = new TnLocation("");
        location.setLatitude(3737392);
        location.setLongitude(-12201074);
        location.setAccuracy(50);
        location.setHeading(0);
        location.setSatellites(5);
        location.setSpeed(10);
        location.setValid(true);
        location.setTime(System.currentTimeMillis() / 10);
        
        String expect = location.toString();
        
        gpsService.addGpsData(location);
      
        isSuccess = false;
        testType = GpsService.STATUS_SUCCESS;
        gpsService.getFixes(1, data, 3000, this);
        synchronized(mutex)
        {
            try
            {
                mutex.wait(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        assertTrue(isSuccess);
        String actual = data[0].toString();
        assertEquals(expect, actual);
        
        PowerMock.verifyAll();
    }
    

    public void requestCompleted(TnLocation[] data, int nFixes, int status)
    {
        if(status == GpsService.STATUS_SUCCESS)
        {
            if(testType == status)
            {
                isSuccess = true;
                synchronized(mutex)
                {
                    mutex.notify();
                }
            }
        }
        else
        {
            if(testType == status)
            {
                isSuccess = true;
                synchronized(mutex)
                {
                    mutex.notify();
                }
            }
        }
    }

    public void testGetFixesNoneBlocking()
    {
        GpsService gpsService = new GpsService("unit test", null);
        
        long time = System.currentTimeMillis() / 10;
        TnLocation location = new TnLocation("");
        location.setLatitude(3737392);
        location.setLongitude(-12201074);
        location.setAccuracy(50);
        location.setHeading(0);
        location.setSatellites(5);
        location.setSpeed(15);
        location.setValid(true);
        location.setTime(time);
        
        TnLocation[] gpsData = new TnLocation[3];
        for(int i = 0; i < gpsData.length; i++)
        {
            gpsData[i] = new TnLocation("");
        }
        
        int num = gpsService.getFixes(3, gpsData);
        assertEquals(0, num);
        
        gpsService.addGpsData(location);
        num = gpsService.getFixes(3, gpsData);
        assertEquals(1, num);
        String str = location.toString();
        assertEquals(str, gpsData[0].toString());
        
        //must fill the gps buffer first.
        location.setTime(time + 1000);
        gpsService.addGpsData(location);
        location.setTime(time + 2000);
        gpsService.addGpsData(location);
        location.setTime(time + 3000);
        gpsService.addGpsData(location);
        location.setTime(time + 4000);
        gpsService.addGpsData(location);
        String str1 = location.toString();
        
        num = gpsService.getFixes(3, gpsData);
        assertEquals(2, num);
        assertEquals(str1, gpsData[0].toString());
        assertEquals(str, gpsData[1].toString());
        
        location.setTime(time + 5000);
        gpsService.addGpsData(location);
        num = gpsService.getFixes(3, gpsData);
        assertEquals(3, num);
        String str2 = location.toString();
        assertEquals(str2, gpsData[0].toString());
        assertEquals(str1, gpsData[1].toString());
        assertEquals(str, gpsData[2].toString());
    }
    
    public void testAddGpsData()
    {
        GpsService gpsService = new GpsService("unit test", null);
        IGpsListener gpsListenerMock = PowerMock.createMock(IGpsListener.class);
        gpsService.addListener(gpsListenerMock);
        gpsListenerMock.gpsDataArrived(EasyMock.anyObject(TnLocation.class));
        EasyMock.expectLastCall().anyTimes();
        
        long time = System.currentTimeMillis() / 10;
        TnLocation location = new TnLocation("");
        location.setLatitude(3737392);
        location.setLongitude(-12201074);
        location.setAccuracy(50);
        location.setHeading(0);
        location.setSatellites(5);
        location.setSpeed(15);
        location.setValid(true);
        location.setTime(time);
        
        gpsService.addGpsData(location);
        assertEquals(1, gpsService.latestFixIndex);
        int currentIndex = gpsService.latestFixIndex - 1;
        if(currentIndex < 0)
        {
            currentIndex = gpsService.positionData.length - 1;
        }
        TnLocation loc = gpsService.positionData[currentIndex];
        String expected = location.toString();
        assertEquals(expected, loc.toString());
        
        //must fill the gps buffer first.
        location.setTime(time + 1000);
        gpsService.addGpsData(location);
        assertEquals(1, gpsService.latestFixIndex);
        location.setTime(time + 2000);
        gpsService.addGpsData(location);
        assertEquals(1, gpsService.latestFixIndex);
        location.setTime(time + 3000);
        gpsService.addGpsData(location);
        assertEquals(1, gpsService.latestFixIndex);
        location.setTime(time + 4000);
        gpsService.addGpsData(location);
        assertEquals(2, gpsService.latestFixIndex);
        expected = location.toString();
        
        currentIndex = gpsService.latestFixIndex - 1;
        if(currentIndex < 0)
        {
            currentIndex = gpsService.positionData.length - 1;
        }
        loc = gpsService.positionData[currentIndex];
        assertEquals(expected, loc.toString());
        
        location.setTime(time + 5000);
        gpsService.addGpsData(location);
        assertEquals(3, gpsService.latestFixIndex);
        expected = location.toString();
        
        currentIndex = gpsService.latestFixIndex - 1;
        if(currentIndex < 0)
        {
            currentIndex = gpsService.positionData.length - 1;
        }
        loc = gpsService.positionData[currentIndex];
        assertEquals(expected, loc.toString());
    }
    
    public void testSetCriteria()
    {
        GpsService gpsService = new GpsService("Unit Test", null);
        TnCriteria criteria = new TnCriteria();
        String expectSourceType = "Unit Test";
        TnLocationManager locManagerMock = PowerMock.createMock(TnLocationManager.class);
        EasyMock.expect(locManagerMock.requestLocationUpdates(criteria, (long) GpsService.MIN_TIME, (float) GpsService.MIN_DISTANCE, GpsService.DEFAULT_TIMEOUT, GpsService.MAX_AGE, gpsService)).andReturn(expectSourceType);
        TnLocationManager.init(locManagerMock);
//        PowerMock.mockStatic(TnLocationManager.class);
//        EasyMock.expect(TnLocationManager.getInstance()).andReturn(locManagerMock);
        PowerMock.replayAll();

        gpsService.setCriteria(criteria);
        assertSame(criteria, gpsService.getCriteria());
        assertEquals(expectSourceType, gpsService.sourceType);
        
        PowerMock.replayAll();
    }
    
    public void testAddListener()
    {
        IGpsListener gpsListenerMock = PowerMock.createMock(IGpsListener.class);
        IGpsListener gpsListenerMock2 = PowerMock.createMock(IGpsListener.class);
        GpsService gpsService = new GpsService("Unit Test", null);
        gpsService.addListener(gpsListenerMock);
        gpsService.addListener(gpsListenerMock2);
        assertEquals(2, gpsService.gpsListeners.size());
    }
    
    public void testRemoveListener()
    {
        IGpsListener gpsListenerMock = PowerMock.createMock(IGpsListener.class);
        IGpsListener gpsListenerMock2 = PowerMock.createMock(IGpsListener.class);
        GpsService gpsService = new GpsService("Unit Test", null);
        gpsService.addListener(gpsListenerMock);
        gpsService.addListener(gpsListenerMock2);
        assertEquals(2, gpsService.gpsListeners.size());
        gpsService.removeListener(gpsListenerMock2);
        assertEquals(1, gpsService.gpsListeners.size());
    }
    
    public void testRemoveAllListeners()
    {
        IGpsListener gpsListenerMock = PowerMock.createMock(IGpsListener.class);
        IGpsListener gpsListenerMock2 = PowerMock.createMock(IGpsListener.class);
        GpsService gpsService = new GpsService("Unit Test", null);
        gpsService.addListener(gpsListenerMock);
        gpsService.addListener(gpsListenerMock2);
        assertEquals(2, gpsService.gpsListeners.size());
        gpsService.removeAllListeners();
        assertEquals(0, gpsService.gpsListeners.size());
    }
    
    public void testStop()
    {
        GpsService gpsService = new GpsService("Unit Test", null);
        gpsService.isStarted = true;
        
        TnCriteria criteria = new TnCriteria();
        TnLocationManager locManagerMock = PowerMock.createMock(TnLocationManager.class);
        TnLocationManager.init(locManagerMock);
        locManagerMock.reset("Unit Test");
        EasyMock.expectLastCall().anyTimes();
        PowerMock.replayAll();
        gpsService.stop();
        PowerMock.verifyAll();
    }
}

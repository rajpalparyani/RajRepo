package com.telenav.navservice.location;


import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.navservice.TestUtil;
import com.telenav.navservice.model.App;
import com.telenav.navservice.network.TnNetwork;

public class LocationSenderTestTiming
{
    private TnNetwork network;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
        network = createMock(TnNetwork.class);
        expect(network.sendUdp((String)anyObject(), (byte[])anyObject())).andReturn(true).anyTimes();
        
        replay(network);
    }

    @After
    public void tearDown() throws Exception
    {
        network = null;
    }

    @Test(timeout = 5000)
    public void testStop() throws Exception
    {
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        LocationBuffer cellBuffer = new LocationBuffer(100, null);
        cellBuffer.addLocation(new TnCellLocation(new TnLocation("")));
        
        LocationSender sender = new LocationSender(gpsBuffer, cellBuffer, null);
        sender.app = new App();
        sender.network = network;
        sender.isAttachCell = true;
        sender.start();
        sender.stop();

        while(gpsBuffer.size() > 0)
            Thread.sleep(5);
        
        assertEquals(gpsBuffer.size(), 0);
        assertEquals(cellBuffer.size(), 0);
        assertFalse(sender.isRunning);
    }
    
    @Test(timeout = 5000)
    public void testFlush() throws Exception
    {
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        LocationBuffer cellBuffer = new LocationBuffer(100, null);
        cellBuffer.addLocation(new TnCellLocation(new TnLocation("")));
        
        LocationSender sender = new LocationSender(gpsBuffer, cellBuffer, null);
        sender.app = new App();
        sender.network = network;
        sender.isAttachCell = true;
        sender.start();
        sender.flush();

        while(gpsBuffer.size() > 0)
            Thread.sleep(5);
        
        assertEquals(gpsBuffer.size(), 0);
        assertEquals(cellBuffer.size(), 0);
    }
    
    @Test(timeout = 10000)
    public void testGpsSendingFrequency() throws Exception
    {
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        LocationBuffer cellBuffer = new LocationBuffer(100, null);
        cellBuffer.addLocation(new TnCellLocation(new TnLocation("")));
        
        LocationSender sender = new LocationSender(gpsBuffer, cellBuffer, null);
        sender.app = new App();
        sender.network = network;
        sender.setGpsSendingInterval(3, false);
        sender.setCellSendingInterval(30);
        
        long time = System.currentTimeMillis();
        sender.start();
        
        Thread.sleep(200);
        if (System.currentTimeMillis() - time < 2000)
        {
            assertEquals(gpsBuffer.size(), 3);
            assertEquals(cellBuffer.size(), 1);
        }
        
        while(gpsBuffer.size() > 0)
            Thread.sleep(10);
        
        assertTrue(System.currentTimeMillis() - time > 2000);
        assertEquals(gpsBuffer.size(), 0);
        assertEquals(cellBuffer.size(), 1);
        sender.stop();
    }
    
    @Test(timeout = 10000)
    public void testCellSendingFrequency() throws Exception
    {
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        gpsBuffer.addLocation(new TnLocation(""));
        LocationBuffer cellBuffer = new LocationBuffer(100, null);
        cellBuffer.addLocation(new TnCellLocation(new TnLocation("")));
        
        LocationSender sender = new LocationSender(gpsBuffer, cellBuffer, null);
        sender.app = new App();
        sender.network = network;
        sender.setGpsSendingInterval(30, true);
        sender.setCellSendingInterval(3);
        
        long time = System.currentTimeMillis();
        sender.start();
        
        Thread.sleep(200);
        if (System.currentTimeMillis() - time < 2000)
        {
            assertEquals(gpsBuffer.size(), 3);
            assertEquals(cellBuffer.size(), 1);
        }
        
        while(cellBuffer.size() > 0)
            Thread.sleep(10);
        
        assertTrue(System.currentTimeMillis() - time > 2000);
        assertEquals(gpsBuffer.size(), 3);
        assertEquals(cellBuffer.size(), 0);
        sender.stop();
    }
    
    @Test(timeout = 10000)
    public void testSendGpsData() throws Exception
    {
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        LocationBuffer cellBuffer = new LocationBuffer(100, null);
        LocationSender sender = new LocationSender(gpsBuffer, cellBuffer, null);
        sender.app = new App();
        sender.network = network;
        sender.setGpsSendingInterval(3, false);
        
        long time = System.currentTimeMillis();
        sender.start();
        
        for (int i=0; i<5; i++)
        {
            gpsBuffer.addLocation(new TnLocation(TnLocationManager.GPS_179_PROVIDER));
        }

        while(gpsBuffer.size() > 0)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time > 2000);
        assertEquals(gpsBuffer.size(), 0);
        
        for (int i=0; i<2; i++)
        {
            gpsBuffer.addLocation(new TnLocation(TnLocationManager.GPS_179_PROVIDER));
        }
        while(gpsBuffer.size() > 0)
            Thread.sleep(100);

        assertTrue(System.currentTimeMillis() - time > 5000);
        assertEquals(gpsBuffer.size(), 0);
        
        sender.stop();
    }
    
    @Test(timeout = 10000)
    public void testSendCellData() throws Exception
    {
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        LocationBuffer cellBuffer = new LocationBuffer(100, null);
        LocationSender sender = new LocationSender(gpsBuffer, cellBuffer, null);
        sender.app = new App();
        sender.network = network;
        sender.setCellSendingInterval(3);
        
        long time = System.currentTimeMillis();
        sender.start();
        
        for (int i=0; i<2; i++)
        {
            cellBuffer.addLocation(new TnCellLocation(new TnLocation(TnLocationManager.GPS_179_PROVIDER)));
        }
        while(cellBuffer.size() > 0)
            Thread.sleep(100);
        
        assertTrue(System.currentTimeMillis() - time > 2000);
        assertEquals(cellBuffer.size(), 0);
        
        for (int i=0; i<1; i++)
        {
            cellBuffer.addLocation(new TnCellLocation(new TnLocation(TnLocationManager.GPS_179_PROVIDER)));
        }
        while(cellBuffer.size() > 0)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time > 5000);
        assertEquals(cellBuffer.size(), 0);
        
        sender.stop();
    }
    
    @Test(timeout = 15000)
    public void testSendDataWhenOverflow() throws Exception
    {
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        LocationBuffer cellBuffer = new LocationBuffer(100, null);
        LocationSender sender = new LocationSender(gpsBuffer, cellBuffer, null);
        sender.app = new App();
        sender.network = network;
        sender.setGpsSendingInterval(100, true);
        sender.setCellSendingInterval(100);
        sender.start();
        
        gpsBuffer.extractAllLocations();
        cellBuffer.extractAllLocations();
        for (int i=0; i<39; i++)
        {
            gpsBuffer.addLocation(new TnLocation(TnLocationManager.GPS_179_PROVIDER));
        }
        synchronized(sender.mutex)
        {
            sender.mutex.notify();
        }
        Thread.sleep(100);
        assertEquals(gpsBuffer.size(), 39);

        gpsBuffer.extractAllLocations();
        cellBuffer.extractAllLocations();
        for (int i=0; i<40; i++)
        {
            gpsBuffer.addLocation(new TnLocation(TnLocationManager.GPS_179_PROVIDER));
        }
        synchronized(sender.mutex)
        {
            sender.mutex.notify();
        }
        long time = System.currentTimeMillis();
        while(gpsBuffer.size() > 0)
            Thread.sleep(50);
        assertTrue(System.currentTimeMillis() - time < 2000);
        assertEquals(gpsBuffer.size(), 0);
        
        gpsBuffer.extractAllLocations();
        cellBuffer.extractAllLocations();
        for (int i=0; i<39; i++)
        {
            TnCellLocation loc = new TnCellLocation(new TnLocation(TnLocationManager.NETWORK_PROVIDER));
            cellBuffer.addLocation(loc);
        }
        synchronized(sender.mutex)
        {
            sender.mutex.notify();
        }
        Thread.sleep(100);
        assertEquals(cellBuffer.size(), 39);
        
        gpsBuffer.extractAllLocations();
        cellBuffer.extractAllLocations();
        for (int i=0; i<40; i++)
        {
            TnCellLocation loc = new TnCellLocation(new TnLocation(TnLocationManager.NETWORK_PROVIDER));
            cellBuffer.addLocation(loc);
        }
        synchronized(sender.mutex)
        {
            sender.mutex.notify();
        }
        time = System.currentTimeMillis();
        while(cellBuffer.size() > 0)
            Thread.sleep(50);
        assertTrue(System.currentTimeMillis() - time < 2000);
        assertEquals(cellBuffer.size(), 0);
        
        gpsBuffer.extractAllLocations();
        cellBuffer.extractAllLocations();
        for (int i=0; i<30; i++)
        {
            gpsBuffer.addLocation(new TnLocation(TnLocationManager.GPS_179_PROVIDER));
        }
        for (int i=0; i<10; i++)
        {
            TnCellLocation loc = new TnCellLocation(new TnLocation(TnLocationManager.NETWORK_PROVIDER));
            cellBuffer.addLocation(loc);
        }
        synchronized(sender.mutex)
        {
            sender.mutex.notify();
        }
        time = System.currentTimeMillis();
        while(gpsBuffer.size() > 0)
            Thread.sleep(50);
        assertTrue(System.currentTimeMillis() - time < 2000);
        assertEquals(gpsBuffer.size(), 0);
        assertEquals(cellBuffer.size(), 0);
        sender.stop();
    }
    
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TripDaoTest.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.AddressAssert;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 *@author jxue
 *@date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SerializableManager.class,Address.class})

public class TripsDaoTest extends TestCase
{
    private TnStore storeTripNull;
    private TnStore storeTripNotNull;
    private TnStore detourTripNull;
    byte[] tripData = {48, -99, -101, 58, 7, 20, 29, 3, 6, 0, -125, 56, 1, 4, 0, 0, 12, 24, 115, 116, 111, 112, 95, 97, 100, 100, 114, 101, 115, 115, 0, 16, 56, 55, 54, 53, 52, 51, 50, 49, 18, 117, 110, 105, 116, 95, 116, 101, 115, 116, 18, 102, 97, 107, 101, 95, 100, 97, 116, 97, 16, 49, 50, 51, 52, 53, 54, 55, 56, 2, 85, 1, 8, 28, 56, 7, 115, -112, 3, -25, 56, 93, -12, 4, 2, 0, 0, 0, 14, 26, 49, 49, 51, 48, 32, 107, 105, 102, 101, 114, 32, 114, 100, 26, 49, 49, 51, 48, 32, 107, 105, 102, 101, 114, 32, 114, 100, 18, 115, 117, 110, 110, 121, 118, 97, 108, 101, 4, 67, 65, 8, 50, 49, 48, 48, 10, 57, 52, 48, 56, 54, 26, 85, 110, 105, 116, 101, 100, 32, 83, 116, 97, 116, 101, 115, 0, 0, 0};
    byte[] lastTripTime = "111111".getBytes();
    private Address trip;
    private Address detourTrip;
    long tripTime = -1;
    
    protected void setUp() throws Exception
    {
        if(SerializableManager.getInstance() == null)
        {
            SerializableManager.init(new TxNodeSerializableManager());
        }
        storeTripNull = new TnStoreMock();
        storeTripNull.load();
        storeTripNull.remove(TripsDao.KEY_NORMAL_TRIP);
        storeTripNull.remove(TripsDao.KEY_DETOUR_TRIP);
        storeTripNull.put(TripsDao.KEY_LAST_TRIP_TIME, lastTripTime);
        
        storeTripNotNull = new TnStoreMock();
        storeTripNotNull.load();
        storeTripNotNull.put(TripsDao.KEY_NORMAL_TRIP, tripData);
        storeTripNotNull.put(TripsDao.KEY_DETOUR_TRIP, tripData);
        storeTripNotNull.put(TripsDao.KEY_LAST_TRIP_TIME, lastTripTime);
        
        detourTripNull = new TnStoreMock();
        detourTripNull.load();
        detourTripNull.put(TripsDao.KEY_NORMAL_TRIP, tripData);
        detourTripNull.remove(TripsDao.KEY_DETOUR_TRIP);
        detourTripNull.put(TripsDao.KEY_LAST_TRIP_TIME, lastTripTime);
        
        trip = new Address();
        Vector categories = new Vector();
        categories.addElement("unit_test");
        categories.addElement("fake_data");
        trip.setCatagories(categories);
        trip.setCursorAddress(false);
        trip.setExistedInFavorite(false);
        trip.setId(10000);
        trip.setLabel("stop_address");
        trip.setPhoneNumber("12345678");
        trip.setSharedFromPTN("87654321");
        trip.setStatus(Address.UNCHANGED);
        trip.setType(Address.TYPE_RECENT_STOP);
        
        Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setCountry("United States");
        stop.setFirstLine("1130 kifer rd");
        stop.setId(2100);
        stop.setIsGeocoded(false);
        stop.setLabel("1130 kifer rd");
        stop.setLat(3737392);
        stop.setLon(-12201074);
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        stop.setStatus(Stop.ADDED);
        stop.setType(Stop.STOP_RECENT);
        trip.setStop(stop);
       
        super.setUp();
    }
    
    public void testSetNormalTrip()
    {
        TripsDao tripsDao = new TripsDao(storeTripNotNull);
        tripsDao.setNormalTrip(trip);
        
        AddressAssert.assertAddress(trip, tripsDao.getLastTrip());
        assertTrue (tripsDao.tripTime() != -1);
        assertNull (detourTrip);
    }
    
    public void testSetDetourTrip()
    {
        TripsDao tripsDao = new TripsDao(storeTripNotNull);
        tripsDao.setDetourTrip(trip);
        
        AddressAssert.assertAddress(trip, tripsDao.getLastTrip());
        assertTrue (tripsDao.tripTime() != -1);
    }
    
    public void testGetLastTripDetourNotNull()
    {
        TripsDao tripsDao = new TripsDao(storeTripNotNull);
        
        assertEquals(tripsDao.getLastTrip().getSource(), Address.SOURCE_RESUME_TRIP);
        assertNotNull(tripsDao.getLastTrip());
//        AddressAssert.assertAddress(trip, tripsDao.getLastTrip());
    }
    
    public void testGetLastTripDetourNull()
    {
        TripsDao tripsDao = new TripsDao(detourTripNull);
        
        assertEquals(tripsDao.getLastTrip().getSource(), Address.SOURCE_RESUME_TRIP);
//        AddressAssert.assertAddress(trip, tripsDao.getLastTrip());
    }
    
    public void testEndTripDetourNull()
    {
        TripsDao tripsDao = new TripsDao(detourTripNull);
        tripsDao.endTrip();
        assertNull (tripsDao.getLastTrip());
    }
    
    public void testEndTripDetourNotNull()
    {
        TripsDao tripsDao = new TripsDao(storeTripNotNull);
        tripsDao.endTrip();
        assertNotNull (tripsDao.getLastTrip());
    }
    
    public void testClear()
    {
        TripsDao tripsDao = new TripsDao(storeTripNotNull);
        tripsDao.clear();
        
        assertNull(tripsDao.getLastTrip());
        assertEquals(tripsDao.tripTime(), -1);
    }
    
    public void testStore()
    {
       //TODO
        
    }
    
    public void testIsDetourTripExistTrue()
    {
        TripsDao tripsDao = new TripsDao(storeTripNotNull);
        assertTrue(tripsDao.isDetourTripExist());
    }
    
    public void testIsDetourTripExistFalse()
    {
        TripsDao tripsDao = new TripsDao(detourTripNull);
        assertFalse(tripsDao.isDetourTripExist());
    }
    
    public void testTripTime()
    {
        TripsDao tripsDao = new TripsDao(storeTripNotNull);
        assertTrue(tripsDao.tripTime() != -1);
    }
    

}

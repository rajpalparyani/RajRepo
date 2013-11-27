/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AddressTest.java
 *
 */
package com.telenav.data.datatypes.address;

import java.util.Vector;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStoreMock;

/**
 *@author jxue
 *@date 2011-6-24
 */
public class AddressTest extends TestCase
{
	//test getDisplayedText();
	public void testGetDisplayedTextLable()
    {
        Address address = new Address();
        address.setLabel("test for label not null");
        assertEquals("test for label not null", address.getDisplayedText());
    }
    
    public void testGetDisplayedTextTextPoi()
    {
        Address address = new Address();
        address.setLabel(null);
        Poi poi = new Poi();
        poi.setType(Poi.TYPE_BUSINESS_DETAIL);
        BizPoi bizPoi = new BizPoi();
        bizPoi.setBrand("Starbucks");
        poi.setBizPoi(bizPoi);
        address.setPoi(poi);
        assertEquals("Starbucks", address.getDisplayedText());
    }
    
    public void testGetDisplayedTextTextStop()
    {
        Address address = new Address();
        address.setLabel(null);
        Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setCountry("United States");
        stop.setFirstLine("1130 kifer rd");
        stop.setId(2100);
        stop.setIsGeocoded(false);
        stop.setLabel("1130 kifer");
        stop.setLat(3737392);
        stop.setLon(-12201074);
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        stop.setStatus(Stop.ADDED);
        stop.setType(Stop.STOP_RECENT);
        address.setStop(stop);
        assertEquals("1130 kifer", address.getDisplayedText());
    }
    
    public void testGetDisplayedTextFirstLine()
    {
        Address address = new Address();
        address.setLabel(null);
        Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setCountry("United States");
        stop.setFirstLine("1130 kifer rd");
        stop.setId(2100);
        stop.setIsGeocoded(false);
        stop.setLat(3737392);
        stop.setLon(-12201074);
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        stop.setStatus(Stop.ADDED);
        stop.setType(Stop.STOP_RECENT);
        address.setStop(stop);
        assertEquals("1130 kifer rd", address.getDisplayedText());
    }
    
    public void testGetDisplayedTextCity()
    {
        Address address = new Address();
        address.setLabel(null);
        Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setCountry("United States");
        stop.setId(2100);
        stop.setIsGeocoded(false);
        stop.setLat(3737392);
        stop.setLon(-12201074);
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        stop.setStatus(Stop.ADDED);
        stop.setType(Stop.STOP_RECENT);
        address.setStop(stop);
        assertEquals("sunnyvale", address.getDisplayedText());
    }
    
    public void testGetDisplayedTextProvince()
    {
        Address address = new Address();
        address.setLabel(null);
        Stop stop = new Stop();
        stop.setProvince("CA");
        stop.setCountry("United States");
        stop.setId(2100);
        stop.setIsGeocoded(false);
        stop.setLat(3737392);
        stop.setLon(-12201074);
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        stop.setStatus(Stop.ADDED);
        stop.setType(Stop.STOP_RECENT);
        address.setStop(stop);
        assertEquals("CA", address.getDisplayedText());
    }
    
    public void testGetDisplayedTextLatLon()
    {
        Address address = new Address();
        address.setLabel(null);
        Stop stop = new Stop();
        stop.setLat(9999999);
        stop.setLon(1111111);
        address.setStop(stop);
        assertEquals("9999999, 1111111", address.getDisplayedText());
    }
    
   //test isSameAddress();
    public void testIsSameAddressTypeNotSame()
    {
       Address address = new Address();
       address.setType(Address.TYPE_RECENT_STOP);
       Address destAddress = new Address();
       destAddress.setType(Address.TYPE_STOP_POI_WRAPPER);
       assertFalse(address.isSameAddress(destAddress));
    }
    
    public void testIsSameAddressRecentTrue()
    {
       Address address = new Address();
       address.setType(Address.TYPE_RECENT_POI);
       Poi poi = new Poi();
       address.setPoi(poi);
       BizPoi bizPoi = new BizPoi();
       bizPoi.setPoiId("111111");
       poi.setBizPoi(bizPoi);
       Address destAddress = new Address();
       destAddress.setType(Address.TYPE_RECENT_POI);
       destAddress.setPoi(poi);
       assertTrue(address.isSameAddress(destAddress));
    }
    
    public void testIsSameAddressFavoriteFalse()
    {
       Address address = new Address();
       address.setType(Address.TYPE_FAVORITE_POI);
       Poi poi = new Poi();
       address.setPoi(poi);
       BizPoi bizPoi = new BizPoi();
       bizPoi.setPoiId("111111");
       poi.setBizPoi(bizPoi);
       
       Address destAddress = new Address();
       destAddress.setType(Address.TYPE_FAVORITE_POI);
       Poi desPoi = new Poi();
       destAddress.setPoi(desPoi);
       BizPoi desBizPoi = new BizPoi();
       desBizPoi.setPoiId("22222");
       desPoi.setBizPoi(desBizPoi);
       assertFalse(address.isSameAddress(destAddress));
    }
    
    public void testIsSameAddressLabelNull()
    {
       Address address = new Address();
       address.setType(Address.TYPE_RECENT_STOP);
       Stop stop = new Stop();
       stop.setLabel("");
       address.setStop(stop);
       
       Address destAddress = new Address();
       destAddress.setType(Address.TYPE_RECENT_STOP);
       destAddress.setStop(stop);
       assertTrue(address.isSameAddress(destAddress));
    }
    
    public void testIsSameAddressLabelSame()
    {
       Address address = new Address();
       address.setLabel("IAMOK");
       address.setType(Address.TYPE_RECENT_STOP);
       Stop stop = new Stop();
       address.setStop(stop);
       
       Address destAddress = new Address();
       destAddress.setLabel("iamok");
       destAddress.setType(Address.TYPE_RECENT_STOP);
       destAddress.setStop(stop);
       assertTrue(address.isSameAddress(destAddress));
    }
    
    public void testIsSameAddressLabelNotSame()
    {
       Address address = new Address();
       address.setLabel("IAMOK");
       address.setType(Address.TYPE_RECENT_STOP);
       Stop stop = new Stop();
       address.setStop(stop);
       
       Address destAddress = new Address();
       destAddress.setLabel("iam");
       destAddress.setType(Address.TYPE_RECENT_STOP);
       destAddress.setStop(stop);
       assertFalse(address.isSameAddress(destAddress));
    }
    
    public void testIsSameAddressStopNotSame()
    {
       Address address = new Address();
       address.setType(Address.TYPE_RECENT_STOP);
       Stop stop = new Stop();
       stop.setFirstLine("1130 kifer");
       address.setStop(stop);
       
       Address destAddress = new Address();
       destAddress.setType(Address.TYPE_RECENT_STOP);
       Stop desStop = new Stop();
       desStop.setFirstLine("San Francisco");
       destAddress.setStop(desStop);
       assertFalse(address.isSameAddress(destAddress));
    }
    
  //test getPhoneNumber();
    public void testGetPhoneNumber()
    {
    	Address address = new Address();
    	address.setPhoneNumber("4083687541");
    	assertEquals("4083687541", address.getPhoneNumber());
    }
    
    public void testGetPhoneNumberBizPoi()
    {
    	Address address = new Address();
    	address.setPhoneNumber("");
    	Poi poi = new Poi();
    	BizPoi bizPoi = new BizPoi();
    	bizPoi.setPhoneNumber("4083333333");
    	poi.setBizPoi(bizPoi);
    	address.setPoi(poi);
    	assertEquals("4083333333", address.getPhoneNumber());
    }
    
   
   //test getValidStop(); 
    public void testGetValidStop()
    {
    	Address address = new Address();
    	Stop stop = new Stop();
    	address.setStop(stop);
    	stop.setLat(11111);
    	stop.setLon(99999);
    	assertEquals (stop, address.getValidStop());
    }
   
    public void testGetValidStopPoi()
    {
    	Address address = new Address();
    	Poi poi = new Poi();
    	address.setPoi(poi);
    	Stop stop = new Stop();
    	poi.setStop(stop);
    	stop.setLat(11111);
    	stop.setLon(99999);
    	assertEquals (stop, address.getValidStop());
    }
    
    public void testGetValidStopBizPoi()
    {
    	Address address = new Address();
    	Poi poi = new Poi();
    	Stop stopPoi = new Stop();
    	stopPoi.setLat(0);
    	stopPoi.setLon(0);
    	address.setStop(stopPoi);
    	address.setPoi(poi);
    	BizPoi bizPoi = new BizPoi();
    	poi.setBizPoi(bizPoi);
    	Stop stop = new Stop();
    	bizPoi.setStop(stop);
    	stop.setLat(11111);
    	stop.setLon(99999);
    	assertEquals (stop, address.getValidStop());
    }
    
    public void testGetValidStopNull()
    {
    	Address address = new Address();
    	Stop stop = new Stop();
    	address.setStop(stop);
    	assertNull(address.getValidStop());
    }
    
    //test isValid();
    public void testIsValidFalse()
    {
    	Address address = new Address();
    	Stop stop = new Stop();
    	stop.setLat(0);
    	stop.setLon(0);
    	address.setStop(stop);
    	PowerMock.replayAll();
    	assertFalse(address.isValid());
    	PowerMock.verifyAll();
    }
    
    public void testIsValidTrue()
    {
    	Address address = new Address();
    	Stop stop = new Stop();
    	stop.setLat(111111);
    	stop.setLon(999999);
    	address.setStop(stop);
    	PowerMock.replayAll();
    	assertTrue(address.isValid());
    	PowerMock.verifyAll();
    }

}

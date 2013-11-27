/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ResUtilTest.java
 *
 */
package com.telenav.searchwidget.res;

import junit.framework.TestCase;

import com.telenav.searchwidget.data.datatypes.address.Stop;
import com.telenav.searchwidget.serverproxy.data.GeocodeBean;
import com.telenav.searchwidget.serverproxy.data.ReverseGeocodeBean;


/**
 *@author xinrongl
 *@date 2011-10-06
 */
public class ResUtilTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testGetApproximateAddress()
    {
    	ReverseGeocodeBean bean1 = new ReverseGeocodeBean();
    	bean1.city = "sunnyvale";
    	bean1.state = "ca";
    	
    	String result = ResUtil.getapproximateAddress(bean1);
    	
    	assertEquals("sunnyvale, ca", result);
	}    
    
    public void testGetOnelineAddress()
    {
    	GeocodeBean.MatchedAddress addr = new GeocodeBean.MatchedAddress();    	
    	addr.streetNumber = "1130";
    	addr.streetName = "kifer rd";
    	addr.streetAddress = "1130 kifer road";
    	addr.crossStreetName = "lawrence expy";
    	addr.city = "sunnyvale";
    	addr.state = "ca";
    	addr.postalCode = "94086";
    	addr.country = "US";
    	addr.lat = 3737365;
    	addr.lon = -12199896;    	
    	String result = ResUtil.getOnelineAddress(addr);    	
    	assertEquals("1130 kifer road, sunnyvale, ca, 94086", result);
    	
    	addr = new GeocodeBean.MatchedAddress();    	
    	addr.streetNumber = "1130";
    	addr.streetName = "kifer rd";
    	addr.postalCode = "94086";
    	addr.country = "US";
    	addr.lat = 3737365;
    	addr.lon = -12199896;    	
    	result = ResUtil.getOnelineAddress(addr);
    	assertEquals("1130, kifer rd, 94086", result);

    	addr = new GeocodeBean.MatchedAddress();    	
    	addr.streetName = "kifer rd";
    	addr.postalCode = "94086";
    	addr.country = "US";
    	addr.lat = 3737365;
    	addr.lon = -12199896;    	
    	result = ResUtil.getOnelineAddress(addr);
    	assertEquals("kifer rd, 94086", result);
    	
    	addr = new GeocodeBean.MatchedAddress();    	
    	addr.streetName = "kifer rd";
    	addr.city = "sunnyvale";
    	addr.state = "ca";
    	addr.country = "US";
    	addr.lat = 3737365;
    	addr.lon = -12199896;    	
    	result = ResUtil.getOnelineAddress(addr);    	
    	assertEquals("kifer rd, sunnyvale, ca", result);

    }
    
    public void testGetOnelineAddressFromStop()
    {
    	Stop stop = new Stop();    	
    	stop.setStreetNumber("1130");
    	stop.setStreetName("kifer rd");
    	stop.setFirstLine("1130 kifer road");
    	stop.setCrossStreetName("lawrence expy");
    	stop.setCity("sunnyvale");
    	stop.setProvince("ca");
    	stop.setPostalCode("94086");
    	stop.setCountry("US");
    	stop.setLat(3737365);
    	stop.setLon(-12199896);    	
    	String result = ResUtil.getOnelineAddress(stop);  
    	assertEquals("1130 kifer road, sunnyvale, ca, 94086", result);
    	
    	stop = new Stop();    	
    	stop.setStreetNumber("1130");
    	stop.setStreetName("kifer rd");
    	stop.setCity("sunnyvale");
    	stop.setProvince("ca");
    	stop.setCountry("US");
    	stop.setLat(3737365);
    	stop.setLon(-12199896);    	
    	result = ResUtil.getOnelineAddress(stop);  
    	assertEquals("1130 kifer rd, sunnyvale, ca", result);
    	
    	stop = new Stop();    	
    	stop.setStreetNumber("1130");
    	stop.setStreetName("kifer rd");
    	stop.setPostalCode("94086");
    	stop.setCountry("US");
    	stop.setLat(3737365);
    	stop.setLon(-12199896);    	
    	result = ResUtil.getOnelineAddress(stop);  
    	assertEquals("1130 kifer rd, 94086", result);

    	stop = new Stop();    	
    	stop.setStreetName("kifer rd");
    	stop.setPostalCode("94086");
    	stop.setCountry("US");
    	stop.setLat(3737365);
    	stop.setLon(-12199896);    	
    	result = ResUtil.getOnelineAddress(stop);  
    	assertEquals("kifer rd, 94086", result);
    }
}

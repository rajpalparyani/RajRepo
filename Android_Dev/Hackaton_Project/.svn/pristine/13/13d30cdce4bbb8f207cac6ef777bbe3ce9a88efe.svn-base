/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GeocodeProxyTest.java
 *
 */

package com.telenav.searchwidget.serverproxy;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.junit.Assert;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.comm.ICommCallback.CommResponse;
import com.telenav.searchwidget.serverproxy.data.EtaBean;
import com.telenav.searchwidget.serverproxy.data.GeocodeBean;
import com.telenav.searchwidget.serverproxy.data.MapBean;
import com.telenav.searchwidget.serverproxy.data.ReverseGeocodeBean;

/**
 * @author xinrongl
 * @date Oct 7, 2011
 */

public class GeocodeProxyTest extends TestCase
{
	private static final String GEOCODE_RESP = "<ns3:GeocodeResponse xmlns:ns3=\"http://telenav.com/tnapi/services/geocoding/v10/\"><status><code>FOUND_EXACT</code></status><matchFound><address><streetNumber>1130</streetNumber><streetName>KIFER RD</streetName><streetAddress>1130 KIFER RD</streetAddress><city>SUNNYVALE</city><state>CA</state><postalCode>94086</postalCode><country>US</country><geoCode lat=\"37.373905\" lon=\"-121.999644\" /></address><matchType>exact</matchType></matchFound></ns3:GeocodeResponse>";
	
	private static final String RGC_RESP = "<ns3:ReverseGeocodeResponse xmlns:ns3=\"http://telenav.com/tnapi/services/geocoding/v10/\"><status><code>OK</code></status><matchFound><address><streetNumber>1090</streetNumber><streetName>Kifer Rd</streetName><crossStreetName>Semiconductor Dr</crossStreetName><streetAddress>1090 Kifer Rd</streetAddress><city>Sunnyvale</city><state>CA</state><postalCode>94086</postalCode><country>US</country><geoCode lat=\"37.37389\" lon=\"-122.00163\" /></address><distance unit=\"mi\">0.105757</distance></matchFound></ns3:ReverseGeocodeResponse>";
	
	private static final String MAP_RESP = "<ns3:GetMapResponse xmlns:ns3=\"http://telenav.com/tnapi/datatypes/map/v10/\"><status><code>OK</code></status><image>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</image></ns3:ReverseGeocodeResponse>";
	
	private static final String ETA_RESP = "<ns4:ETAResponse xmlns:ns4=\"http://telenav.com/tnapi/datatypes/eta/v10/\"><status><code>OK</code></status><results><startEnd>37.37295,-122.00162 37.3739,-121.99964</startEnd><distance>0.204</distance><travelTime>14</travelTime><trafficTime>14</trafficTime></results></ns4:ETAResponse>";
	
	private Host host;
	private Comm comm;
	private IServerProxyListener listener;
	
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
    
    public void testGeocode()
    {
    	GeocodeProxy proxy = initProxy();    	
    	Whitebox.setInternalState(proxy, "actionId", IServerProxyConstants.ACTION_GEOCODE);
    	
    	PowerMock.replayAll();
    	
    	try
    	{
        	ByteArrayInputStream is = new ByteArrayInputStream(GEOCODE_RESP.getBytes());
        	ICommCallback.CommResponse response = new CommResponse("", (byte)0);
        	
    		proxy.receive(is, GEOCODE_RESP.length(), response, null);
    	}
    	catch(Exception e)
    	{
    		Assert.fail();
    	}
    	
    	PowerMock.verifyAll();    	
    	PowerMock.resetAll();
    	
    	GeocodeBean bean = proxy.getGeocodeBean();
    	GeocodeBean.MatchedAddress addr = (GeocodeBean.MatchedAddress)bean.addresses.elementAt(0);
    	assertEquals("FOUND_EXACT", bean.code);
    	assertEquals("1130", addr.streetNumber);
    	assertEquals("KIFER RD", addr.streetName);
    	assertEquals("1130 KIFER RD", addr.streetAddress);
    	assertEquals("SUNNYVALE", addr.city);
    	assertEquals("CA", addr.state);
    	assertEquals("94086", addr.postalCode);
    	assertEquals("US", addr.country);
    	assertEquals(3737390, addr.lat);
    	assertEquals(-12199964, addr.lon);    	    
    }
    
    public void testRgc()
    {
    	GeocodeProxy proxy = initProxy();    	
    	Whitebox.setInternalState(proxy, "actionId", IServerProxyConstants.ACTION_REVERSE_GEOCODE);
    	
    	PowerMock.replayAll();
    	
    	try
    	{
        	ByteArrayInputStream is = new ByteArrayInputStream(RGC_RESP.getBytes());
        	ICommCallback.CommResponse response = new CommResponse("", (byte)0);
        	
    		proxy.receive(is, RGC_RESP.length(), response, null);
    	}
    	catch(Exception e)
    	{
    		Assert.fail();
    	}
    	
    	PowerMock.verifyAll();    	
    	PowerMock.resetAll();
    	
    	ReverseGeocodeBean bean = proxy.getReverseGeocodeBean();
    	
    	assertEquals("OK", bean.code);
    	assertEquals(1090, bean.streetNumber);
    	assertEquals("Kifer Rd", bean.streetName);
    	assertEquals("Semiconductor Dr", bean.crossStreetName);
    	assertEquals("1090 Kifer Rd", bean.streetAddress);
    	assertEquals("Sunnyvale", bean.city);
    	assertEquals("CA", bean.state);
    	assertEquals("94086", bean.postalCode);
    	assertEquals("US", bean.country);
    	assertEquals(0.105757F, bean.distance);
    	assertEquals("mi", bean.unitString);
    	
    }
    
    public void testEta()
    {
    	GeocodeProxy proxy = initProxy();    	
    	Whitebox.setInternalState(proxy, "actionId", IServerProxyConstants.ACTION_GET_ETA);
    	
    	PowerMock.replayAll();
    	
    	try
    	{
        	ByteArrayInputStream is = new ByteArrayInputStream(ETA_RESP.getBytes());
        	ICommCallback.CommResponse response = new CommResponse("", (byte)0);
        	
    		proxy.receive(is, ETA_RESP.length(), response, null);
    	}
    	catch(Exception e)
    	{
    		Assert.fail();
    	}
    	
    	PowerMock.verifyAll();    	
    	PowerMock.resetAll();
    	
    	EtaBean bean = proxy.getEtaBean();
    	
    	assertEquals("OK", bean.code);
    	assertEquals(0.204F, bean.distance);
    	assertEquals(14, bean.travelTime);
    	assertEquals(14, bean.trafficTime);
    }
    
    public void testMap()
    {
    	GeocodeProxy proxy = initProxy();    	
    	Whitebox.setInternalState(proxy, "actionId", IServerProxyConstants.ACTION_GET_MAP);
    	
    	PowerMock.replayAll();
    	
    	try
    	{
        	ByteArrayInputStream is = new ByteArrayInputStream(MAP_RESP.getBytes());
        	ICommCallback.CommResponse response = new CommResponse("", (byte)0);
        	
    		proxy.receive(is, MAP_RESP.length(), response, null);
    	}
    	catch(Exception e)
    	{
    		Assert.fail();
    	}
    	
    	PowerMock.verifyAll();    	
    	PowerMock.resetAll();
    	
    	MapBean bean = proxy.getMapBean();
    	System.out.println(bean);
    	assertEquals("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", bean.imageString.toString());
    }
    
    private GeocodeProxy initProxy()
    {
    	host = PowerMock.createMock(Host.class);
    	comm = PowerMock.createMock(Comm.class);
    	listener = PowerMock.createMock(IServerProxyListener.class);

    	GeocodeProxy proxy = PowerMock.createPartialMock(
    			GeocodeProxy.class, 
    			new String[] {"addJobToList"},
    			host, comm, listener);

    	return proxy;
    }

}


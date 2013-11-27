/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AddressDaoTest.java
 *
 */

package com.telenav.searchwidget.data;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.searchwidget.data.datatypes.address.Stop;

/**
 * @author xinrongl
 * @date Oct 10, 2011
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({AddressDao.class})
public class AddressDaoTest extends TestCase
{
	public void testHomeAddr()
	{
    	byte[] homeBuf = new byte[] {
    			48, -99, -101, 58, 8, 28, 56, 87, 113, -112, 3, -121, -126, 
    			93, -12, 0, 0, 0, 0, 0, 14, 2, -1, 26, 49, 49, 51, 48, 32, 
    			107, 105, 102, 101, 114, 32, 114, 100, 18, 115, 117, 110, 
    			110, 121, 118, 97, 108, 101, 4, 99, 97, 2, 48, 10, 57, 52, 
    			48, 56, 54, 4, 85, 83, 0, 0, 
    	};
    	
    	byte[] homeIndex = "1,".getBytes();
    	
    	AppConfigHelper.brandName = "cingular";
    	
		AddressDao dao = PowerMock.createPartialMock(AddressDao.class, new String[] {"readData", "writeData"});
		try
		{
			PowerMock.expectPrivate(dao, "readData", "TN70_cingular_express_address/1.bin").andReturn(homeBuf).anyTimes();
			PowerMock.expectPrivate(dao, "readData", "TN70_cingular_express_address.index").andReturn(homeIndex).anyTimes();
			PowerMock.expectPrivate(dao, "writeData", EasyMock.anyObject(String.class), EasyMock.anyObject(byte[].class));
			PowerMock.expectLastCall().anyTimes();
		}
		catch (Exception e)
		{
			assertEquals(true, false);
		}
		
		PowerMock.replayAll();
		
		Stop home = new Stop();
		home.setStreetNumber("1130");
		home.setStreetName("kifer rd");
		home.setCrossStreetName("lawrence expressway");
		home.setCity("sunnyvale");
		home.setProvince("ca");
		home.setPostalCode("94086");
		home.setCountry("US");
		home.setLat(3737365);
		home.setLon(-12199896);
		
		dao.setHomeStop(home);

		Stop stop = dao.getHomeStop();
		System.out.println("stop = " + stop);
		assertEquals("1130", stop.getStreetNumber());
		assertEquals("kifer rd", stop.getStreetName());
		assertEquals("sunnyvale", stop.getCity());
		assertEquals("ca", stop.getProvince());
		assertEquals("94086", stop.getPostalCode());
		assertEquals("US", stop.getCountry());
		assertEquals(3737365, stop.getLat());
		assertEquals(-12199896, stop.getLon());
		
		PowerMock.verifyAll();
		PowerMock.resetAll();
	}
	
	public void testOfficeAddr()
	{
    	byte[] officeBuf = new byte[] {
    			48, -99, -101, 58, 8, 28, 56, 87, 113, -112, 3, -121, -126, 
    			93, -12, 0, 0, 0, 0, 0, 14, 2, -1, 26, 49, 49, 51, 48, 32, 
    			107, 105, 102, 101, 114, 32, 114, 100, 18, 115, 117, 110, 
    			110, 121, 118, 97, 108, 101, 4, 99, 97, 2, 48, 10, 57, 52, 
    			48, 56, 54, 4, 85, 83, 0, 0, 
    	};
    	
    	byte[] officeIndex = "2,".getBytes();
    	
    	AppConfigHelper.brandName = "cingular";
    	
		AddressDao dao = PowerMock.createPartialMock(AddressDao.class, new String[] {"readData", "writeData"});
		try
		{
			PowerMock.expectPrivate(dao, "readData", "TN70_cingular_express_address/2.bin").andReturn(officeBuf).anyTimes();
			PowerMock.expectPrivate(dao, "readData", "TN70_cingular_express_address.index").andReturn(officeIndex).anyTimes();
			PowerMock.expectPrivate(dao, "writeData", EasyMock.anyObject(String.class), EasyMock.anyObject(byte[].class));
			PowerMock.expectLastCall().anyTimes();
		}
		catch (Exception e)
		{
			assertEquals(true, false);
		}
		
		PowerMock.replayAll();
		
		Stop office = new Stop();
		office.setStreetNumber("1130");
		office.setStreetName("kifer rd");
		office.setCrossStreetName("lawrence expressway");
		office.setCity("sunnyvale");
		office.setProvince("ca");
		office.setPostalCode("94086");
		office.setCountry("US");
		office.setLat(3737365);
		office.setLon(-12199896);
		
		dao.setOfficeStop(office);

		Stop stop = dao.getOfficeStop();
		System.out.println("stop = " + stop);
		assertEquals("1130", stop.getStreetNumber());
		assertEquals("kifer rd", stop.getStreetName());
		assertEquals("sunnyvale", stop.getCity());
		assertEquals("ca", stop.getProvince());
		assertEquals("94086", stop.getPostalCode());
		assertEquals("US", stop.getCountry());
		assertEquals(3737365, stop.getLat());
		assertEquals(-12199896, stop.getLon());
		
		PowerMock.verifyAll();
		PowerMock.resetAll();
	}

}


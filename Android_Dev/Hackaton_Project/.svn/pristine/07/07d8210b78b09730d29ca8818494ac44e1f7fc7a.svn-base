/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LocationDaoTest.java
 *
 */

package com.telenav.searchwidget.gps.android;

import javassist.bytecode.ByteArray;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;

/**
 * @author xinrongl
 * @date Oct 7, 2011
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocationDao.class})
public class LocationDaoTest extends TestCase 
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

	public void testSearchWidgetLastKnownLocation()
	{
    	byte[] baseBuf = new byte[] {
    			48, -99, -101, 58, 8, 52, -97, -78, -111, -34, -74, 76, 87, 113, 
    			-112, 3, -121, -126, 93, -12, -111, 1, 118, 0, -95, 15, -97, -78, 
    			-111, -34, -74, 76, 0, 0, 0, 
    	};

		PowerMock.mockStatic(LocationDao.class);
		LocationDao dao = PowerMock.createPartialMock(LocationDao.class, new String[] {"readData", "writeData"});		
		EasyMock.expect(LocationDao.getInstance()).andReturn(dao).anyTimes();
		try
		{
			PowerMock.expectPrivate(dao, "writeData", EasyMock.anyObject(String.class), EasyMock.anyObject(byte[].class));
			PowerMock.expectLastCall();
			PowerMock.expectPrivate(dao, "readData", EasyMock.anyObject(String.class)).andReturn(baseBuf).anyTimes();
		}
		catch(Exception e)
		{
			assertEquals(true, false);
		}
		
		PowerMock.replayAll();
		
    	TnLocation location = new TnLocation(TnLocationManager.GPS_179_PROVIDER);
        location.setTime(1317942216394L);
        location.setLatitude(3737365);
        location.setLongitude(-12199896);
        location.setSpeed(100);
        location.setHeading(59);
        location.setAccuracy(1000);
        location.setLocalTimeStamp(1317942216394L);
        
        LocationDao.getInstance().setRootPath("./searchwidget");
        
		LocationDao.getInstance().setSearchWidgetLastKnownLocation(location);
		
		TnLocation location2 = LocationDao.getInstance().getSearchWidgetLastKnownLocation();
		
    	assertNotNull(location);    	
    	assertEquals(1317942216394L, location2.getTime());
    	assertEquals(3737365, location2.getLatitude());
    	assertEquals(-12199896, location2.getLongitude());
    	assertEquals(100, location2.getSpeed());
    	assertEquals(59, location2.getHeading());
    	assertEquals(1000, location2.getAccuracy());
    	assertEquals(1317942216394L, location2.getLocalTimeStamp());
		
		PowerMock.verifyAll();
		PowerMock.resetAll();
		
	}
}


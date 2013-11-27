/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidLocationUtilTest.java
 *
 */

package com.telenav.searchwidget.gps.android;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.location.Location;
import android.location.LocationManager;

import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;

/**
 * @author xinrongl
 * @date Oct 6, 2011
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Location.class, AndroidLocationUtil.class})
public class AndroidLocationUtilTest extends TestCase
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
    
    public void testToBytes()
    {
    	byte[] baseBuf = new byte[] {
    			48, -99, -101, 58, 8, 52, -97, -78, -111, -34, -74, 76, 87, 113, 
    			-112, 3, -121, -126, 93, -12, -111, 1, 118, 0, -95, 15, -97, -78, 
    			-111, -34, -74, 76, 0, 0, 0, 
    	};
    	
    	TnLocation location = new TnLocation(TnLocationManager.GPS_179_PROVIDER);
        location.setTime(1317942216394L);
        location.setLatitude(3737365);
        location.setLongitude(-12199896);
        location.setSpeed(100);
        location.setHeading(59);
        location.setAccuracy(1000);
        location.setLocalTimeStamp(1317942216394L);

    	byte[] buf = AndroidLocationUtil.toBytes(location);
    	assertNotNull(buf);
    	assertEquals(baseBuf.length, buf.length);
    	Assert.assertArrayEquals(baseBuf, buf);
    }
    
    public void testFromBytes()
    {
    	byte[] buf = new byte[] {
    			48, -99, -101, 58, 8, 52, -97, -78, -111, -34, -74, 76, 87, 113, 
    			-112, 3, -121, -126, 93, -12, -111, 1, 118, 0, -95, 15, -97, -78, 
    			-111, -34, -74, 76, 0, 0, 0, 
    	};
    	
    	TnLocation location = AndroidLocationUtil.fromBytes(buf);
    	
    	assertNotNull(location);    	
    	assertEquals(1317942216394L, location.getTime());
    	assertEquals(3737365, location.getLatitude());
    	assertEquals(-12199896, location.getLongitude());
    	assertEquals(100, location.getSpeed());
    	assertEquals(59, location.getHeading());
    	assertEquals(1000, location.getAccuracy());
    	assertEquals(1317942216394L, location.getLocalTimeStamp());
    }
    
    public void testConvert()
    {
    	Location loc = PowerMock.createMock(Location.class);
    	EasyMock.expect(loc.getAccuracy()).andReturn(23.43F).anyTimes();
    	EasyMock.expect(loc.getAltitude()).andReturn(131.21).anyTimes();
    	EasyMock.expect(loc.getBearing()).andReturn(20.0F).anyTimes();
    	EasyMock.expect(loc.getLatitude()).andReturn(32.32012).anyTimes();
    	EasyMock.expect(loc.getLongitude()).andReturn(-121.34222).anyTimes();
    	EasyMock.expect(loc.getSpeed()).andReturn(100F).anyTimes();
    	EasyMock.expect(loc.getTime()).andReturn(1317942216394L).anyTimes();
    	EasyMock.expect(loc.getProvider()).andReturn(LocationManager.GPS_PROVIDER).anyTimes();
    	
    	PowerMock.replayAll();    	

    	TnLocation tnLoc = AndroidLocationUtil.convert(loc);
    	
    	assertEquals(20, tnLoc.getAccuracy());
    	assertEquals(131.0F, tnLoc.getAltitude());
    	assertEquals(20, tnLoc.getHeading());
    	assertEquals(3232012, tnLoc.getLatitude());
    	assertEquals(-12134222, tnLoc.getLongitude());
    	assertEquals(900, tnLoc.getSpeed());
    	assertEquals(131794221639L, tnLoc.getTime());
    	assertEquals(TnLocationManager.GPS_179_PROVIDER, tnLoc.getProvider());
    	
    	PowerMock.verifyAll();
    	
    	PowerMock.resetAll();
    }

}


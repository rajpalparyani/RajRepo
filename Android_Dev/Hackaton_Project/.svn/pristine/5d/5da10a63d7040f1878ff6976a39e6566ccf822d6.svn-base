/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NearCitiesDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.module.region.RegionUtil;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 * @author htzheng
 * @date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnStore.class, RegionUtil.class})
public class NearCitiesDaoTest
{
	private final static int NEAR_CITIY_INDEX = 200001;

	private final static int CENTER_CITIY_ARGS_INDEX = 200002;

	private final static int CENTER_CITIY_ARGS_CHANGED_INDEX = 200003;
	private TnStoreMock nearCitiesStore;
	private NearCitiesDao nearCitiesDao;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		SerializableManager.init(new TxNodeSerializableManager());
		nearCitiesStore = new TnStoreMock();
		nearCitiesStore.load();
		nearCitiesDao = new NearCitiesDao(nearCitiesStore);
		
		PowerMock.mockStatic(RegionUtil.class);
		RegionUtil reginUtil = PowerMock.createMock(RegionUtil.class);
		EasyMock.expect(RegionUtil.getInstance()).andReturn(reginUtil).anyTimes();
		EasyMock.expect(reginUtil.getCurrentRegion()).andReturn("").anyTimes();
		EasyMock.expect(reginUtil.isLegalRegion("")).andReturn(true).anyTimes();
		
		TnRegionDependentStoreProvider nearCitiesProvider = PowerMock.createMock(TnRegionDependentStoreProvider.class);
		Whitebox.setInternalState(nearCitiesDao, "nearCitiesProvider", nearCitiesProvider);
		EasyMock.expect(nearCitiesProvider.getStore("")).andReturn(nearCitiesStore).anyTimes();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		nearCitiesStore = null;
	}
	
	@Test
	public void testGetCities_NullData()
	{
		PowerMock.replayAll();
		
		assertEquals(nearCitiesDao.getNearCities(""), null);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetCities_HasCityData()
	{
		nearCitiesStore.put(CENTER_CITIY_ARGS_CHANGED_INDEX, new byte[]{0});
		PowerMock.replayAll();
		
		nearCitiesDao.getNearCities("");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetNearCityChanged_HasChange()
	{
		nearCitiesStore.put(CENTER_CITIY_ARGS_CHANGED_INDEX, new byte[]{1});
		PowerMock.replayAll();
		
		assertEquals(nearCitiesDao.getNearCityChanged(""), true);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetNearCityChanged_NoChange()
	{
		PowerMock.replayAll();
		
		assertEquals(nearCitiesDao.getNearCityChanged(""), false);
		PowerMock.verifyAll();
	}

	@Test
	public void testPutNearCities()
	{
		Stop stop = new Stop();
		stop.setCity("Sunnyvale");
		stop.setCountry("United States");
		stop.setCrossStreetName("Wolf Rd");
		stop.setFirstLine("1130 Kifer Rd");
		stop.setIsGeocoded(false);
		stop.setLat(3737392);
		stop.setLon(-12201074);
		stop.setPostalCode("94086");
		stop.setProvince("CA");
		stop.setSharedAddress(false);
		stop.setType(Stop.STOP_RECENT);
		
		Vector cities = new Vector();
		cities.add(stop);
		
		
	}
}

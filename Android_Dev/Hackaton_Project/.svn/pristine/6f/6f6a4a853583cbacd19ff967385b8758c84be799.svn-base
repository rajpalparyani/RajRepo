/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ServerDrivenParamsDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.datatypes.primitive.StringMapAssert;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 *@author bmyang
 *@date 2011-6-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractDaoManager.class)
public class ServerDrivenParamsDaoTest extends TestCase 
{
	TnStore serverDrivenStore;
	ServerDrivenParamsDao serverDrivenParamsDao;
	private StringMap params;
	private DaoManager daoManager;

	protected void setUp() throws Exception 
	{
		SerializableManager.init(new TxNodeSerializableManager());
		serverDrivenStore = new TnStoreMock();
		serverDrivenStore.load();
		serverDrivenParamsDao = new ServerDrivenParamsDao(serverDrivenStore);
		params = new StringMap();
		params.put("test_key", "test_value");
		params.put("a", "1");
		params.put("ATT_NAV...BILLBOARD_AD_POLLING_INTERVAL", "300");
		params.put("ATT_NAV...AAA", "400");
		params.put("ATT_NAV..en_US.BBB", "500");
		params.put("ATT_NAV..en_FR.CCC", "600");
		params.put("FE_NAV_JUNCTION_VIEW", "1");
		params.put("ROUTE_PLANNING_MULTI_ROUTE", "1");
		params.put(ServerDrivenParamsDao.SERVER_DRIVEN_VERSION, "7.0.1.1001");
		serverDrivenParamsDao.setServerParams(params);
		
		MandatoryProfile profile = new MandatoryProfile();
		profile.getClientInfo().productType = "ATT_NAV";
		profile.getUserInfo().region = "";
		profile.getUserInfo().locale = "en_US";
		MandatoryNodeDao mandatoryNodeDao = PowerMock.createMock(MandatoryNodeDao.class);
		
		daoManager = PowerMock.createMock(DaoManager.class);
		PowerMock.mockStatic(AbstractDaoManager.class);
		EasyMock.expect(DaoManager.getInstance()).andReturn(daoManager).anyTimes();
		EasyMock.expect(daoManager.getMandatoryNodeDao()).andReturn(mandatoryNodeDao).anyTimes();
		EasyMock.expect(mandatoryNodeDao.getMandatoryNode()).andReturn(profile).anyTimes();
		
		super.setUp();
	}

	public void testGetValue() 
	{
		PowerMock.replayAll();
		assertEquals("test_value", serverDrivenParamsDao.getValue("test_key"));
		assertEquals("300", serverDrivenParamsDao.getValue("BILLBOARD_AD_POLLING_INTERVAL"));
		assertEquals("400", serverDrivenParamsDao.getValue("AAA"));
		assertEquals("500", serverDrivenParamsDao.getValue("BBB"));
		assertEquals("500", serverDrivenParamsDao.getValue("BB"));//this is weird, null should be return in this case.
		assertEquals("500", serverDrivenParamsDao.getValue("B"));//this is weird, null should be return in this case.
		assertNull(serverDrivenParamsDao.getValue("CCC"));
		PowerMock.verifyAll();
	}

	public void testGetIntValue() 
	{
		PowerMock.replayAll();
		assertEquals(-1, serverDrivenParamsDao.getIntValue("ab"));
		assertEquals(1, serverDrivenParamsDao.getIntValue("a"));
		assertEquals(300, serverDrivenParamsDao.getIntValue("BILLBOARD_AD_POLLING_INTERVAL"));
		assertEquals(400, serverDrivenParamsDao.getIntValue("AAA"));
		assertEquals(500, serverDrivenParamsDao.getIntValue("BBB"));
		assertEquals(500, serverDrivenParamsDao.getIntValue("BB"));//this is weird, -1 should be return in this case.
		assertEquals(500, serverDrivenParamsDao.getIntValue("B")); //this is weird, -1 should be return in this case.
		assertEquals(-1, serverDrivenParamsDao.getIntValue("CCC"));
		assertEquals(1, serverDrivenParamsDao.getIntValue("FE_NAV_JUNCTION_VIEW"));
		assertEquals(1, serverDrivenParamsDao.getIntValue("ROUTE_PLANNING_MULTI_ROUTE"));
		PowerMock.verifyAll();
	}

	public void testSetServerParams() 
	{
		PowerMock.replayAll();
		StringMap serverParams = new StringMap();
		serverParams.put("a", "1");
		String version = "7.0.1.1001";
		serverParams.put(ServerDrivenParamsDao.VERSION, version);
		serverDrivenParamsDao.setServerParams(serverParams);
		byte[] expected = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(params);
		serverDrivenParamsDao.setServerParams(params);
		assertEquals(version, serverDrivenParamsDao.getVersion());
		//private final static int SERVER_PARAMS_INDEX = 400001;
		Assert.assertArrayEquals(expected, serverDrivenStore.get(400001));
	}

	public void testGetServerParams() 
	{
		PowerMock.replayAll();
		StringMapAssert.assertStringMap(params, serverDrivenParamsDao.getServerParams());
	}

	public void testGetVersion()
	{
		PowerMock.replayAll();
		StringMap serverParams = new StringMap();
		serverParams.put("a", "1");
		serverParams.put(ServerDrivenParamsDao.SERVER_DRIVEN_VERSION, "7.0.1.1001");
		serverDrivenParamsDao.setServerParams(serverParams);
		assertEquals("7.0.1.1001", serverDrivenParamsDao.getVersion());
	}
	
	public void testClear()
	{
		SimpleConfigDao simpleConfigDao = PowerMock.createMock(SimpleConfigDao.class);
		EasyMock.expect(daoManager.getSimpleConfigDao()).andReturn(simpleConfigDao);
		EasyMock.expect(simpleConfigDao.getVector(SimpleConfigDao.KEY_CACHED_REGION)).andReturn(new Vector());
		PowerMock.replayAll();
		serverDrivenParamsDao.clear();
		assertEquals(0, serverDrivenStore.size());
	}
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ServiceLocatorDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;


import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

import junit.framework.TestCase;

import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.datatypes.primitive.StringMapAssert;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 *@author bmyang
 *@date 2011-6-21
 */
public class ServiceLocatorDaoTest extends TestCase
{

	TnStore serverMappingStore;
	ServiceLocatorDao serviceLocatorDao;
	private StringMap serverMapping;
	private StringMap actionMapping;
	private String version;
	private TnStore regionStore;
	
	public void setUp() throws Exception 
	{
		SerializableManager.init(new TxNodeSerializableManager());
		serverMappingStore = new TnStoreMock();
		serverMappingStore.load();
		serviceLocatorDao = new ServiceLocatorDao(serverMappingStore);
		serverMapping = new StringMap();
		serverMapping.put("a", "1");
		serverMapping.put("b", "2");
		 
		actionMapping = new StringMap();
		actionMapping.put("c", "3");
		actionMapping.put("d", "4");
		actionMapping.put("e", "5");
		 
		version = "7.0.1.1001";
		
	    regionStore = new TnStoreMock();
		regionStore.load();
		
	    super.setUp();
	}

//  FIXME, we will fix this test case later.
//	public void testGetVersion() 
//	{
//		assertNull(serviceLocatorDao.getVersion());
//        serviceLocatorDao.setServerMappings(null, null, version);
//        assertEquals(version, serviceLocatorDao.getVersion());
//	}
	
	public void testGetVersion_Empty() 
	{
	    PowerMock.replayAll();
		assertEquals("", serviceLocatorDao.getVersion());
		PowerMock.verifyAll();
	}
	
	public void testGetVersion_From_Store() 
	{
		//private final static int SERVER_MAPPINGS_VERSION_INDEX = 500002;
//        regionStore.put(500002, version.getBytes());
        
        PowerMock.replayAll();
        assertEquals("", serviceLocatorDao.getVersion());
        PowerMock.verifyAll();
	}

//  FIXME, we will fix this test case later.
//	public void testGetDomainMap() 
//	{
//		//private final static int DOMAIN_MAPPINGS_INDEX = 500001;
//		byte[] data = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(serverMapping);
//        serverMappingStore.put(500001, data);
//        StringMapAssert.assertStringMap(serverMapping, serviceLocatorDao.getDomainMap());
//	}
	
//  FIXME, we will fix this test case later.
//	public void testGetDomainMap_Null() 
//	{
//        assertNull(serviceLocatorDao.getDomainMap());
//	}
	
//  FIXME, we will fix this test case later.
//	public void testGetActionMap() 
//	{
//		//private final static int ACTION_MAPPINGS_INDEX = 500003;
//		byte[] data = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(actionMapping);
//        serverMappingStore.put(500003, data);
//        StringMapAssert.assertStringMap(actionMapping, serviceLocatorDao.getActionMap());
//	}

//  FIXME, we will fix this test case later.
//	public void testGetActionMap_Null() 
//	{
//        assertNull(serviceLocatorDao.getActionMap());
//	}
	
//  FIXME, we will fix this test case later.
//	public void testSetServerMappings() 
//	{
//		ServiceLocator serviceLocator = PowerMock.createMock(ServiceLocator.class);
//		serviceLocator.loadServiceLocatorInfo(serverMapping, actionMapping);
//        PowerMock.replayAll();
//		serviceLocatorDao.setServiceLocator(serviceLocator);
//        serviceLocatorDao.setServerMappings(serverMapping, actionMapping, version);
//        StringMapAssert.assertStringMap(actionMapping, serviceLocatorDao.getActionMap());
//        StringMapAssert.assertStringMap(serverMapping, serviceLocatorDao.getDomainMap());
//        assertEquals(version, serviceLocatorDao.getVersion());
//        PowerMock.verifyAll();
//	}
	
//  FIXME, we will fix this test case later.
//	public void testSetServerMappings_Null() 
//	{
//        serviceLocatorDao.setServerMappings(null, null, null);
//        assertEquals(0, serverMappingStore.size());
//        assertNull(serviceLocatorDao.getActionMap());
//        assertNull(serviceLocatorDao.getDomainMap());
//        assertNull(serviceLocatorDao.getVersion());
//	}
	
//	FIXME, we will fix this test case later.
//	public void testClear()
//	{
//		serviceLocatorDao.setServerMappings(serverMapping, actionMapping, version);
//		assertEquals(3, serverMappingStore.size());
//		assertEquals(version, serviceLocatorDao.getVersion());
//		serviceLocatorDao.clear();
//		assertEquals(0, serverMappingStore.size());
//		assertEquals("", serviceLocatorDao.getVersion());
//	}
}

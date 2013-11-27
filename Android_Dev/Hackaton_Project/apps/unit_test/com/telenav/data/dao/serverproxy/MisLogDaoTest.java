/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MisLogDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.log.mis.MisLogFactory;
import com.telenav.log.mis.log.DefaultMisLog;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 * @author htzheng
 * @date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnStore.class})
public class MisLogDaoTest
{
	private static final int SESSION_ID = 700001;
	private String sessionId = "12345";
	private TnStore mislogDataWrapper = new TnStoreMock();
	private MisLogDao misLogDao = null;
	private MisLogFactory misLogFactory;
	private DefaultMisLog misLog;
	@Before
	public void setUp()
	{
		mislogDataWrapper = new TnStoreMock();
		mislogDataWrapper.load();
		
		misLogDao = new MisLogDao(mislogDataWrapper);
		misLogFactory = new MisLogFactory();
		SerializableManager.init(new TxNodeSerializableManager());
		
		misLog = misLogFactory.createDefaultMislog(613);
	}
	
	@After
	public void tearDown()
	{
		
	}
	
	@Test
	public void testGetSessionId_NullMislogDataWapper()
	{
		misLogDao = new MisLogDao(null);
		Assert.assertEquals("", misLogDao.getSessionId());
	}
	
	@Test 
	public void testtestGetSessionId_HasData()
	{
		
		mislogDataWrapper.put(SESSION_ID, sessionId.getBytes());
		
		Assert.assertEquals(sessionId, misLogDao.getSessionId());
	}
	
	@Test
	public void testSetSessionId_NULLSessionId()
	{
		misLogDao.setSessionId(null);
		Assert.assertEquals("", misLogDao.getSessionId());
	}
	

	@Test
	public void testSetSessionId_SetData()
	{
		misLogDao.setSessionId(sessionId);
		Assert.assertEquals(sessionId, misLogDao.getSessionId());
	}

//	@Test
//	public void testAddMisLog()
//	{
//		misLogDao.setSessionId(sessionId);
//		
//		misLog = misLogFactory.createDefaultMislog(613);
//		misLogDao.addMisLog(misLog);
//		
////		AbstractMisLog[] logs = misLogDao.getMislog();
////		Assert.assertTrue(misLog.compareTo(logs[0]) == 0);
//	}
	
	@Test
	public void testRemoveMisLog_NOMisLog()
	{
		Assert.assertFalse(misLogDao.removeMisLog(misLog));	
	}

//	@Test
//	public void testRemoveMisLog_HasMisLog()
//	{
//		misLogDao.addMisLog(misLog);
//		Assert.assertEquals(true, misLogDao.removeMisLog(misLog));
//	}
	
	@Test
	public void testClear()
	{
		misLogDao.clear();
		Assert.assertTrue(mislogDataWrapper.size() == 0);
	}
	
}

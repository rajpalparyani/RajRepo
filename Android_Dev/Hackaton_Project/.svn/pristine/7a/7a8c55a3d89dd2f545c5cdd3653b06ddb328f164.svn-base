/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TxNodeMisLogSerializableTest.java
 *
 */
package com.telenav.data.serializable.txnode;

import java.util.Enumeration;

import org.junit.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;


import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.log.mis.log.PoiMisLog;
import com.telenav.module.AppConfigHelper;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStoreMock;

/**
 *@author bmyang
 *@date 2011-6-23
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TnPersistentManager.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class TxNodeMisLogSerializableTest extends TestCase
{
	byte[] poiMislogData = {48, -99, -101, 58, 14, 84, -117, 56, 1, -109, 56, 1, -101, 56, 1, -37, -117, 8, -45, -117, 8, -53, -117, 8, -61, -117, 8, -69, -117, 8, -77, -117, 8, -85, -117, 8, -93, -117, 8, -117, -117, 8, -61, 62, 1, -29, -117, 8, 28, 6, 55, 48, 48, 36, 55, 48, 48, 95, 49, 51, 48, 56, 55, 57, 49, 51, 55, 51, 53, 49, 55, 48, 26, 49, 51, 48, 56, 55, 57, 49, 51, 55, 51, 53, 49, 54, 4, 49, 49, 2, 49, 18, 114, 101, 108, 101, 118, 97, 110, 99, 101, 12, 48, 46, 52, 32, 109, 105, 2, 48, 2, 49, 2, 49, 26, 49, 51, 48, 56, 55, 57, 49, 51, 53, 55, 57, 48, 50, 20, 51, 52, 49, 56, 55, 54, 52, 50, 48, 53, 2, 49, 2, 50, 0, 0};
	byte[] poiMislogData_2 = {48, -99, -101, 58, 13, 78, -117, 56, 1, -101, 56, 1, -37, -117, 8, -45, -117, 8, -53, -117, 8, -61, -117, 8, -69, -117, 8, -77, -117, 8, -85, -117, 8, -93, -117, 8, -117, -117, 8, -61, 62, 1, -29, -117, 8, 26, 6, 55, 48, 48, 26, 49, 51, 48, 56, 55, 57, 49, 51, 55, 51, 53, 49, 54, 4, 49, 49, 2, 49, 18, 114, 101, 108, 101, 118, 97, 110, 99, 101, 12, 48, 46, 52, 32, 109, 105, 2, 48, 2, 49, 2, 49, 26, 49, 51, 48, 56, 55, 57, 49, 51, 53, 55, 57, 48, 50, 20, 51, 52, 49, 56, 55, 54, 52, 50, 48, 53, 2, 49, 2, 50, 0, 0};
	PoiMisLog poiMisLog;
	TxNodeMisLogSerializable txNodeMisLogSerializable;
	
	public void setUp() throws Exception 
	{
		AbstractDaoManager.init(new DaoManager());
		txNodeMisLogSerializable = new TxNodeMisLogSerializable();
		poiMisLog = new PoiMisLog("700_13087913735170", IMisLogConstants.TYPE_POI_IMPRESSION, IMisLogConstants.PRIORITY_1, null); 
		poiMisLog.setTimestamp(1308791373516L);
		poiMisLog.setPoiId("3418764205");
		poiMisLog.setSearchUid("1308791357902");
		poiMisLog.setPageName(IMisLogConstants.VALUE_POI_PAGE_NAME_DETAIL);
		poiMisLog.setPageIndex(1);
		poiMisLog.setPoiRating(0);
		poiMisLog.setPoiDistance("0.4 mi");
		poiMisLog.setPoiSorting(IMisLogConstants.VALUE_POI_SORT_BY_RELEVANCE);
		poiMisLog.setPageNumber(1);
		poiMisLog.setPageSize(11);
		poiMisLog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_NORMAL);
		poiMisLog.setSessionId("1");
		super.setUp();
	}

	public void testCreateMisLog() 
	{
		TnStoreMock mockStore = new TnStoreMock(); 
    	mockStore.load();
    	PowerMock.mockStatic(TnPersistentManager.class);    	
    	TnPersistentManager tnPersistentManager = PowerMock.createMock(TnPersistentManager.class);
    	EasyMock.expect(TnPersistentManager.getInstance()).andReturn(tnPersistentManager);
    	EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "mislog")).andReturn(mockStore);
    	
    	AppConfigHelper.isLoggerEnable = false;
		PowerMock.replayAll();
		assertAbstractMisLog(poiMisLog, txNodeMisLogSerializable.createMisLog(poiMislogData));
		PowerMock.verifyAll();
	}
	
	public void testCreateMisLogNull()
	{
		assertNull(txNodeMisLogSerializable.createMisLog(null));
	}

	public void testToBytes() 
	{
	    Assert.assertArrayEquals(poiMislogData, txNodeMisLogSerializable.toBytes(poiMisLog, true, "1"));
		Assert.assertArrayEquals(poiMislogData_2, txNodeMisLogSerializable.toBytes(poiMisLog, false, "1"));
	}
	
	public void testToBytesNull() 
	{
		assertNull(txNodeMisLogSerializable.toBytes(null, true, "1"));
	}
	
	private void assertAbstractMisLog(AbstractMisLog expected, AbstractMisLog actual)
	{
		if (null == expected)
		{
			assertNull(actual);
			return ;
		}
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getPriority(), actual.getPriority());
		assertEquals(expected.getTimestamp(), actual.getTimestamp());
		assertEquals(expected.getType(), actual.getType());
		Enumeration keys = expected.getAttributeKeys();
		while (keys.hasMoreElements())
		{
			Long key = (Long) keys.nextElement();
			assertEquals(expected.getAttribute(key), actual.getAttribute(key));
		}
		keys = actual.getAttributeKeys();
		while (keys.hasMoreElements())
		{
			Long key = (Long) keys.nextElement();
			assertEquals(expected.getAttribute(key), actual.getAttribute(key));
		}
	}

}

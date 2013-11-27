/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * InventoryUtilTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStore;

/**
 * @author htzheng
 * @date 2011-6-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnStore.class})
public class InventoryUtilTest
{
	private TnStore store;
	
	@Before
	public void setUp()
	{
		SerializableManager.init(new TxNodeSerializableManager());
		store = PowerMock.createMock(TnStore.class);
	}
	
	@After
	public void tearDown()
	{
		store = null;
		SerializableManager.init(null);
	}
	
	/*Test case for method getInventory()*/
	
	//case 1:
	//description: null parameter
	//Expected: "0"
	@Test
	public void testGetInventory_NullParam()
	{
		assertEquals("0", InventoryUtil.getInventory(null));
	}
	
	//case 2:
	//description: empty store
	//Expected:"0"
	@Test
	public void testGetInventory_EmptyStore()
	{
		
		expect(store.size()).andReturn(new Integer(0));
		expect(store.keys()).andReturn(null);
		PowerMock.replayAll();
		
		assertEquals("0", InventoryUtil.getInventory(store));
		PowerMock.verifyAll();
	}
	
	//case 3:
	//description: store has items
	//Expected: can output the inventory string.
	//we can't know the compress info, so now use "0" to check if it is right.
	@Test
	public void testGetInventory_StoreHasItems()
	{
		expect(store.size()).andReturn(1);
		Vector ids = new Vector();
		ids.add("11");
		expect(store.keys()).andReturn(ids.elements());
		byte[] value = {48, -99, -101, 58, 0, 0, 4, 26, 49, 51, 48, 56, 50, 56, 57, 57, 52, 50, 55, 48, 50, 2, 48, 0, 0};
		expect(store.get(11)).andReturn(value);
		PowerMock.replayAll();
		
		String inventory = InventoryUtil.getInventory(store);
		assertTrue( inventory != null 
				&& inventory.trim().length() > 0
				&& !inventory.equals("0"));
		
		PowerMock.verifyAll();
	}
}

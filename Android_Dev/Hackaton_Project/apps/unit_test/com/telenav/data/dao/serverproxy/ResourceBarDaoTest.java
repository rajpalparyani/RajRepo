/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ResourceBarDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.io.ByteArrayInputStream;
import java.util.Vector;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cache.AbstractCache;
import com.telenav.cache.LRUCache;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.address.StopAssert;
import com.telenav.data.datatypes.map.MapDataUpgradeInfo;
import com.telenav.data.datatypes.map.MapDataUpgradeInfoAssert;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.RuleNode;
import com.telenav.io.TnIoManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author bmyang
 * @date 2011-7-4
 */
@RunWith(PowerMockRunner.class)
public class ResourceBarDaoTest extends TestCase
{
	private ResourceBarDao resourceBarDao;
	private AbstractCache audioRuleCache;
	private TnStore audioRuleStore;
	private AbstractCache dynamicAudioCache;
	private TnStore resourceBarVersionStore;
	private AbstractCache navAudiosCache;
	private TnStore persistableAudioStore;
	private AbstractCache trafficAudiosCache;
	private TnStore cserverNodeStore;
	private TnStore preferenceBackupStore;
	private static final byte[] data = new byte[]{1,2,3,4};
	private static final byte[] audioData = new byte[]{0, 0, 1, -53, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 10, 0, 0, 0, 21, 0, 0, 0, 32, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 15, 0, 0, 0, 18, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 26, 0, 0, 0, 29, 0, 0, 0, 26, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 38, 0, 0, 0, 65, 0, 0, 0, 108, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 43, 0, 0, 0, 54, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 48, 0, 0, 0, 51, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 59, 0, 0, 0, 62, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 69, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 75, 0, 0, 0, 82, 0, 0, 0, 97, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 79, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 86, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 91, 0, 0, 0, 94, 0, 0, 0, 24, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 101, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 105, 0, 0, 0, 24, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 114, 0, 0, 0, -99, 0, 0, 0, -56, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 119, 0, 0, 0, -126, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 124, 0, 0, 0, 127, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -121, 0, 0, 0, -110, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -116, 0, 0, 0, -113, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -105, 0, 0, 0, -102, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, -95, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, -89, 0, 0, 0, -82, 0, 0, 0, -67, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, -85, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, -78, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -73, 0, 0, 0, -70, 0, 0, 0, 24, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, -63, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, -59, 0, 0, 0, 24, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, -50, 0, 0, 0, -23, 0, 0, 1, 68, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -45, 0, 0, 0, -34, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -40, 0, 0, 0, -37, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 66, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -29, 0, 0, 0, -26, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -18, 0, 0, 1, 45, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, -12, 0, 0, 0, -1, 0, 0, 1, 22, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, -7, 0, 0, 0, -4, 0, 0, 0, 26, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 3, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 8, 0, 0, 1, 11, 0, 0, 0, 24, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 16, 0, 0, 1, 19, 0, 0, 0, 26, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 26, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 31, 0, 0, 1, 34, 0, 0, 0, 24, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 39, 0, 0, 1, 42, 0, 0, 0, 26, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 1, 114, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 50, 0, 0, 1, 57, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 54, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 61, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 65, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 74, 0, 0, 1, 85, 0, 0, 1, -88, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 79, 0, 0, 1, 82, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 90, 0, 0, 1, -111, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 96, 0, 0, 1, 107, 0, 0, 1, 126, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 101, 0, 0, 1, 104, 0, 0, 0, 26, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 111, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 115, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 120, 0, 0, 1, 123, 0, 0, 0, 26, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -126, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -122, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, -117, 0, 0, 1, -114, 0, 0, 0, 26, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 1, 114, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, -106, 0, 0, 1, -99, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -102, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -95, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -91, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, -83, 0, 0, 1, -76, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -79, 0, 0, 0, 26, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, -71, 0, 0, 1, -64, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -67, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -60, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, -56, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0};
	private TnIoManager ioManager;
	private TnRegionDependentStoreProvider cserverNodeStoreProvider;
	private TnRegionDependentStoreProvider backupPreferenceStoreProvider;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		ioManager = PowerMock.createMock(TnIoManager.class);
		if(SerializableManager.getInstance() == null)
        {
            SerializableManager.init(new TxNodeSerializableManager());
        }
		persistableAudioStore = new TnStoreMock();
		persistableAudioStore.load();
		audioRuleStore = new TnStoreMock();
		audioRuleStore.load();
		resourceBarVersionStore = new TnStoreMock();
		resourceBarVersionStore.load();
		cserverNodeStore = new TnStoreMock();
		cserverNodeStore.load();
        preferenceBackupStore = new TnStoreMock();
        preferenceBackupStore.load();
		dynamicAudioCache = new LRUCache(10);
		audioRuleCache = new LRUCache(10);
        navAudiosCache = new LRUCache(10);
        trafficAudiosCache = new LRUCache(10);
        cserverNodeStoreProvider = PowerMock.createMock(TnRegionDependentStoreProvider.class);
        backupPreferenceStoreProvider = PowerMock.createMock(TnRegionDependentStoreProvider.class);
        resourceBarDao = new ResourceBarDao(persistableAudioStore, audioRuleStore, resourceBarVersionStore, cserverNodeStore,
                preferenceBackupStore, dynamicAudioCache, audioRuleCache, navAudiosCache, trafficAudiosCache, ioManager);
        Whitebox.setInternalState(resourceBarDao, "cserverNodeStoreProvider", cserverNodeStoreProvider);
        Whitebox.setInternalState(resourceBarDao, "backupPreferenceStoreProvider", backupPreferenceStoreProvider);
        
        super.setUp();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#clear()}.
	 */
	public void testClear()
	{
		byte[] data = "test".getBytes();
		persistableAudioStore.put("a", data);
		audioRuleStore.put("a", data);
		resourceBarVersionStore.put("a", data);
		cserverNodeStore.put("a", data);
		dynamicAudioCache.put("a", data);
		audioRuleCache.put("a", data);
		navAudiosCache.put("a", data);
		trafficAudiosCache.put("a", data);
		
		cserverNodeStoreProvider.clear();
		backupPreferenceStoreProvider.clear();
		PowerMock.replayAll();
		
		resourceBarDao.clear();
		assertEquals(0, persistableAudioStore.size());
		assertEquals(0, audioRuleStore.size());
		assertEquals(0, resourceBarVersionStore.size());
		assertEquals(0, cserverNodeStore.size());
		assertEquals(0, dynamicAudioCache.size());
		assertEquals(0, audioRuleCache.size());
		assertEquals(0, navAudiosCache.size());
		assertEquals(0, trafficAudiosCache.size());
		assertEquals("0", resourceBarDao.getAudioInventory());
		assertEquals("0", resourceBarDao.getAudioRuleInventory());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#copyVersions(com.telenav.data.dao.serverproxy.ResourceBarDao, com.telenav.data.dao.serverproxy.ResourceBarDao)}.
	 */
	public void testCopyVersions()
	{
		ResourceBarDao srcDao = PowerMock.createMock(ResourceBarDao.class);
		String version = "7.0.1.101";
		EasyMock.expect(srcDao.getAirportVersion()).andReturn(version);
		EasyMock.expect(srcDao.getAudioInventory()).andReturn(version);
		String timeStamp = "2011-07-04:00";
		EasyMock.expect(srcDao.getAudioRuleTimestamp()).andReturn(timeStamp);
		EasyMock.expect(srcDao.getAudioTimestamp()).andReturn(timeStamp);
		EasyMock.expect(srcDao.getBrandNameVersion(null)).andReturn(version);
		EasyMock.expect(srcDao.getCategoryVersion(null)).andReturn(version);
		EasyMock.expect(srcDao.getHotPoiVersion(null)).andReturn(version);
		EasyMock.expect(srcDao.getMapDataUpgradeInfoVersion()).andReturn(version);
		EasyMock.expect(srcDao.getPreferenceSettingVersion(null)).andReturn(version);
		EasyMock.expect(srcDao.getResourceFormatVersion()).andReturn(version);
		
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.copyVersions(srcDao, resourceBarDao, null);
		assertEquals(version, resourceBarDao.getAirportVersion());
		assertEquals(version, resourceBarDao.getAudioInventory());
		assertEquals(version, resourceBarDao.getBrandNameVersion(null));
		assertEquals(version, resourceBarDao.getCategoryVersion(null));
		assertEquals(version, resourceBarDao.getHotPoiVersion(null));
		assertEquals(version, resourceBarDao.getMapDataUpgradeInfoVersion());
		assertEquals(version, resourceBarDao.getPreferenceSettingVersion(null));
		assertEquals(version, resourceBarDao.getResourceFormatVersion());
		assertEquals(timeStamp, resourceBarDao.getAudioRuleTimestamp());
		assertEquals(timeStamp, resourceBarDao.getAudioTimestamp());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#putStaticAudio(int, byte[])}.
	 */
	public void testPutStaticAudio()
	{
		resourceBarDao.putStaticAudio(1111, data);
		Assert.assertArrayEquals(data, resourceBarDao.getAudio(1111));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#removeStaticAudio(int)}.
	 */
	public void testRemoveStaticAudio()
	{
		resourceBarDao.putStaticAudio(1111, data);
		Assert.assertArrayEquals(data, resourceBarDao.getAudio(1111));
		resourceBarDao.removeStaticAudio(1111);
		assertNull(resourceBarDao.getAudio(1111));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#clearStaticAudio()}.
	 */
	public void testClearStaticAudio()
	{
		resourceBarDao.putStaticAudio(1111, data);
		resourceBarDao.putStaticAudio(111, data);
		assertEquals(2, persistableAudioStore.size());
		resourceBarDao.clearStaticAudio();
		assertEquals(0, persistableAudioStore.size());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#clearDynamicAudio()}.
	 */
	public void testClearDynamicAudio()
	{
		resourceBarDao.putDynamicAudio(111, data);
		assertEquals(1, dynamicAudioCache.size());
		resourceBarDao.clearDynamicAudio();
		assertEquals(0, dynamicAudioCache.size());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#clearStaticAudioRule()}.
	 */
	public void testClearStaticAudioRule()
	{
		resourceBarDao.putAudioRule(1111, data);
		assertEquals(1, audioRuleStore.size());
		resourceBarDao.clearStaticAudioRule();
		assertEquals(0, audioRuleStore.size());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudio(int)}.
	 */
	public void testGetAudio()
	{
		resourceBarDao.putStaticAudio(111, data);
		Assert.assertArrayEquals(data, resourceBarDao.getAudio(111));
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudio(int)}.
	 */
	public void testGetAudio_dynamic()
	{
		resourceBarDao.putDynamicAudio(111111111, data);
		Assert.assertArrayEquals(data, resourceBarDao.getAudio(111111111));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#putDynamicAudio(int, byte[], int)}.
	 */
	public void testPutDynamicAudioIntByteArrayInt()
	{
		resourceBarDao.putDynamicAudio(111111111, data, 1001);
		assertEquals(1, dynamicAudioCache.size());
		Assert.assertArrayEquals(data, (byte[]) dynamicAudioCache.get(111111111));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#putDynamicAudio(int, byte[])}.
	 */
	public void testPutDynamicAudioIntByteArray()
	{
		resourceBarDao.putDynamicAudio(111111111, data);
		assertEquals(1, dynamicAudioCache.size());
		Assert.assertArrayEquals(data, (byte[]) dynamicAudioCache.get(111111111));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#putAudioRule(int, byte[])}.
	 */
	public void testPutAudioRule()
	{
		resourceBarDao.putAudioRule(1111, data);
		assertEquals(1, audioRuleStore.size());
		Assert.assertArrayEquals(data, (byte[]) audioRuleStore.get(1111));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#removeAudioRule(int)}.
	 */
	public void testRemoveAudioRule()
	{
		int ruleId = 1111;
		resourceBarDao.putAudioRule(ruleId, data);
		assertEquals(1, audioRuleStore.size());
		Assert.assertArrayEquals(data, (byte[]) audioRuleStore.get(ruleId));
		resourceBarDao.removeAudioRule(ruleId);
		assertNull(audioRuleStore.get(ruleId));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioRule(int)}.
	 */
	public void testGetAudioRule_From_Cache()
	{
		int key = 111;
		ByteArrayInputStream is = new ByteArrayInputStream(audioData);
        RuleNode rule = null;
		try
		{
			rule = AudioDataFactory.getInstance().createRuleNode(is);
		} catch (Exception e)
		{
			fail("fail to create audio data factory");
		}
		audioRuleCache.put(key, rule);
		assertEquals(rule, resourceBarDao.getAudioRule(key));
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioRule(int)}.
	 */
	public void testGetAudioRule_From_Store()
	{
		int id = 111;
		audioRuleStore.put(id, audioData);
		RuleNode audioRule = resourceBarDao.getAudioRule(id);
		assertNotNull(audioRule);
		assertEquals(audioRuleCache.get(id), audioRule);
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioRule(int)}.
	 */
	public void testGetAudioRule_Null()
	{
		assertNull(resourceBarDao.getAudioRule(111));
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setAudioTimestamp(java.lang.String)}.
	 */
	public void testSetAudioTimestamp()
	{
		resourceBarDao.setAudioTimestamp("2011-07-04:00");
		assertEquals("2011-07-04:00", resourceBarDao.getAudioTimestamp());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioTimestamp()}.
	 */
	public void testGetAudioTimestamp()
	{
		resourceBarDao.setAudioTimestamp("2011-07-04:00");
		assertEquals("2011-07-04:00", resourceBarDao.getAudioTimestamp());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioTimestamp()}.
	 */
	public void testGetAudioTimestamp_default()
	{
		assertEquals("0", resourceBarDao.getAudioTimestamp());
		resourceBarDao.setDefaultAudioTimestamp("2011-07-04:00");
		assertEquals("2011-07-04:00", resourceBarDao.getAudioTimestamp());
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioInventory()}.
	 */
	public void testGetAudioInventory()
	{
		assertEquals("0", resourceBarDao.getAudioInventory());
		resourceBarDao.setAudioInventory("test");
		assertEquals("test", resourceBarDao.getAudioInventory());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setAudioInventory(java.lang.String)}.
	 */
	public void testSetAudioInventory()
	{
		resourceBarDao.setAudioInventory("test");
		assertEquals("test", resourceBarDao.getAudioInventory());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setAudioRuleTimestamp(java.lang.String)}.
	 */
	public void testSetAudioRuleTimestamp()
	{
		String timestamp = "2011-07-04:00";
		resourceBarDao.setAudioRuleTimestamp(timestamp);
		assertEquals(timestamp, resourceBarDao.getAudioRuleTimestamp());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioRuleTimestamp()}.
	 */
	public void testGetAudioRuleTimestamp()
	{
		assertEquals("0", resourceBarDao.getAudioRuleTimestamp());
		String timestamp = "2011-07-04:00";
		resourceBarDao.setAudioRuleTimestamp(timestamp);
		assertEquals(timestamp, resourceBarDao.getAudioRuleTimestamp());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAudioRuleInventory()}.
	 */
	public void testGetAudioRuleInventory()
	{
		assertEquals("0", resourceBarDao.getAudioRuleInventory());
		audioRuleStore.put(111, data);
		audioRuleStore.put(11, audioData);
		String inventory = InventoryUtil.getInventory(audioRuleStore);
		resourceBarDao.refreshAudioRuleInventory();
		assertEquals(inventory, resourceBarDao.getAudioRuleInventory());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#refreshAudioInventory()}.
	 */
	public void testRefreshAudioInventory()
	{
		persistableAudioStore.put(111, data);
		persistableAudioStore.put(11, audioData);
		String inventory = InventoryUtil.getInventory(persistableAudioStore);
		resourceBarDao.refreshAudioInventory();
		assertEquals(inventory, resourceBarDao.getAudioInventory());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#refreshAudioRuleInventory()}.
	 */
	public void testRefreshAudioRuleInventory()
	{
		audioRuleStore.put(111, data);
		audioRuleStore.put(11, audioData);
		String inventory = InventoryUtil.getInventory(audioRuleStore);
		resourceBarDao.refreshAudioRuleInventory();
		assertEquals(inventory, resourceBarDao.getAudioRuleInventory());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setDefaultAudioTimestamp(java.lang.String)}.
	 */
	public void testSetDefaultAudioTimestamp()
	{
		assertEquals("0", resourceBarDao.getAudioTimestamp());
		String timestamp = "2011-07-04:00";
		resourceBarDao.setDefaultAudioTimestamp(timestamp);
		assertEquals(timestamp, resourceBarDao.getAudioTimestamp());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setAirportVersion(java.lang.String)}.
	 */
	public void testSetAirportVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setAirportVersion(version);
		assertEquals(version, resourceBarDao.getAirportVersion());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAirportVersion()}.
	 */
	public void testGetAirportVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertEquals("", resourceBarDao.getAirportVersion());
		resourceBarDao.setAirportVersion(version);
		assertEquals(version, resourceBarDao.getAirportVersion());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setResourceFormatVersion(java.lang.String)}.
	 */
	public void testSetResourceFormatVersion()
	{
		String version = "7.0.1.1001";
		PowerMock.replayAll();
		
		resourceBarDao.setResourceFormatVersion(version );
		assertEquals(version, resourceBarDao.getResourceFormatVersion());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getResourceFormatVersion()}.
	 */
	public void testGetResourceFormatVersion()
	{
		String version = "7.0.1.1001";
		PowerMock.replayAll();
		
		assertEquals("", resourceBarDao.getResourceFormatVersion());
		resourceBarDao.setResourceFormatVersion(version );
		assertEquals(version, resourceBarDao.getResourceFormatVersion());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setHotPoiVersion(java.lang.String)}.
	 */
	public void testSetHotPoiVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setHotPoiVersion(version, null);
		assertEquals(version, resourceBarDao.getHotPoiVersion(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getHotPoiVersion()}.
	 */
	public void testGetHotPoiVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertEquals("", resourceBarDao.getHotPoiVersion(null));
		resourceBarDao.setHotPoiVersion(version, null);
		assertEquals(version, resourceBarDao.getHotPoiVersion(null));
		PowerMock.verifyAll();
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setHotPoiNode(com.telenav.data.datatypes.poi.PoiCategory)}.
	 */
	public void testSetHotPoiNode()
	{
		PoiCategory expected = createPoiCategory();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setHotPoiNode(expected, null);
		assertPoicategory(expected, resourceBarDao.getHotPoiNode(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getHotPoiNode()}.
	 */
	public void testGetHotPoiNode()
	{
		PoiCategory expected = createPoiCategory();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertNull(resourceBarDao.getHotPoiNode(null));
		resourceBarDao.setHotPoiNode(expected, null);
		assertPoicategory(expected, resourceBarDao.getHotPoiNode(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setBrandNameVersion(java.lang.String)}.
	 */
	public void testSetBrandNameVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setBrandNameVersion(version, null);
		assertEquals(version, resourceBarDao.getBrandNameVersion(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getBrandNameVersion()}.
	 */
	public void testGetBrandNameVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertEquals("", resourceBarDao.getBrandNameVersion(null));
		resourceBarDao.setBrandNameVersion(version, null);
		assertEquals(version, resourceBarDao.getBrandNameVersion(null));
		PowerMock.verifyAll();
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setBrandNameNode(com.telenav.data.datatypes.primitive.StringList)}.
	 */
	public void testSetBrandNameNode()
	{
		StringList expected = createBrandName();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setBrandNameNode(expected, null);
		assertStringList(expected, resourceBarDao.getBrandNameNode(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getBrandNameNode()}.
	 */
	public void testGetBrandNameNode()
	{
		StringList expected = createBrandName();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertNull(resourceBarDao.getBrandNameNode(null));
		resourceBarDao.setBrandNameNode(expected, null);
		assertStringList(expected, resourceBarDao.getBrandNameNode(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setAirportNode(java.util.Vector)}.
	 */
	public void testSetAirportNode()
	{
		Vector stops = createStops();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
        resourceBarDao.setAirportNode(stops);
        Vector actual = resourceBarDao.getAirportNode();
        assertEquals(stops.size(), actual.size());
        for (int i =0; i < stops.size(); i++)
        {
        	StopAssert.assertStop((Stop)stops.get(i), (Stop) actual.get(i));
        }
        PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getAirportNode()}.
	 */
	public void testGetAirportNode()
	{
		Vector stops = createStops();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertNull(resourceBarDao.getAirportNode());
        resourceBarDao.setAirportNode(stops);
        Vector actual = resourceBarDao.getAirportNode();
        assertEquals(stops.size(), actual.size());
        for (int i =0; i < stops.size(); i++)
        {
        	StopAssert.assertStop((Stop)stops.get(i), (Stop) actual.get(i));
        }
        PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setCategoryNode(com.telenav.data.datatypes.poi.PoiCategory)}.
	 */
	public void testSetCategoryNode()
	{
        PoiCategory expected = createPoiCategory();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setCategoryNode(expected, null);
		assertPoicategory(expected, resourceBarDao.getCategoryNode(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getCategoryNode()}.
	 */
	public void testGetCategoryNode()
	{
        PoiCategory expected = createPoiCategory();
        EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertNull(resourceBarDao.getCategoryNode(null));
		resourceBarDao.setCategoryNode(expected, null);
		assertPoicategory(expected, resourceBarDao.getCategoryNode(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setCategoryVersion(java.lang.String)}.
	 */
	public void testSetCategoryVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
			
		resourceBarDao.setCategoryVersion(version, null);
		assertEquals(version, resourceBarDao.getCategoryVersion(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getCategoryVersion()}.
	 */
	public void testGetCategoryVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertEquals("", resourceBarDao.getCategoryVersion(null));
		resourceBarDao.setCategoryVersion(version, null);
		assertEquals(version, resourceBarDao.getCategoryVersion(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getMapDataUpgradeInfo()}.
	 */
	public void testGetMapDataUpgradeInfo()
	{
		MapDataUpgradeInfo expected = createMapDataUpgradeInfo();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertNull(resourceBarDao.getMapDataUpgradeInfo());
//		resourceBarDao.setMapDataUpgradeInfo(expected);
//		MapDataUpgradeInfoAssert.assertMapDataUpgradeInfo(expected, resourceBarDao.getMapDataUpgradeInfo());
		PowerMock.verifyAll();
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setMapDataUpgradeInfo(com.telenav.data.datatypes.map.MapDataUpgradeInfo)}.
	 */
	public void testSetMapDataUpgradeInfo()
	{
		MapDataUpgradeInfo expected = createMapDataUpgradeInfo();
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
//		resourceBarDao.setMapDataUpgradeInfo(expected);
//		MapDataUpgradeInfoAssert.assertMapDataUpgradeInfo(expected, resourceBarDao.getMapDataUpgradeInfo());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setMapDataUpgradeInfoVersion(java.lang.String)}.
	 */
	public void testSetMapDataUpgradeInfoVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setMapDataUpgradeInfoVersion(version);
		assertEquals(version, resourceBarDao.getMapDataUpgradeInfoVersion());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getMapDataUpgradeInfoVersion()}.
	 */
	public void testGetMapDataUpgradeInfoVersion()
	{

		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertEquals("", resourceBarDao.getMapDataUpgradeInfoVersion());
		resourceBarDao.setMapDataUpgradeInfoVersion(version);
		assertEquals(version, resourceBarDao.getMapDataUpgradeInfoVersion());
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getPreferenceSetting()}.
	 */
	public void testGetPreferenceSetting()
	{
		String expected = "this is a test!!! ";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertNull(resourceBarDao.getPreferenceSetting(null));
		resourceBarDao.setPreferenceSetting(expected.getBytes(), null);
		Assert.assertArrayEquals(expected.getBytes(), resourceBarDao.getPreferenceSetting(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setPreferenceSetting(byte[])}.
	 */
	public void testSetPreferenceSetting()
	{
        String expected = "this is a test!!! ";
        EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setPreferenceSetting(expected.getBytes(), null);
		Assert.assertArrayEquals(expected.getBytes(), resourceBarDao.getPreferenceSetting(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setPreferenceSettingVersion(java.lang.String)}.
	 */
	public void testSetPreferenceSettingVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		resourceBarDao.setPreferenceSettingVersion(version, null);
		assertEquals(version, resourceBarDao.getPreferenceSettingVersion(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getPreferenceSettingVersion()}.
	 */
	public void testGetPreferenceSettingVersion()
	{
		String version = "7.0.1.1001";
		EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(cserverNodeStore).anyTimes();
		PowerMock.replayAll();
		
		assertEquals("", resourceBarDao.getPreferenceSettingVersion(null));
		resourceBarDao.setPreferenceSettingVersion(version, null);
		assertEquals(version, resourceBarDao.getPreferenceSettingVersion(null));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#setIsResourceSyncFinish(boolean)}.
	 */
	public void testSetIsResourceSyncFinish()
	{
		boolean isResourceSyncFinish = true;
		resourceBarDao.setIsResourceSyncFinish(isResourceSyncFinish);
		assertEquals(isResourceSyncFinish, resourceBarDao.isResourceSyncFinish);
		assertEquals(isResourceSyncFinish, resourceBarDao.isResourceSyncFinish());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#isResourceSyncFinish()}.
	 */
	public void testIsResourceSyncFinish()
	{
		assertEquals(false, resourceBarDao.isResourceSyncFinish());
		boolean isResourceSyncFinish = true;
		resourceBarDao.setIsResourceSyncFinish(isResourceSyncFinish);
		assertEquals(isResourceSyncFinish, resourceBarDao.isResourceSyncFinish);
		assertEquals(isResourceSyncFinish, resourceBarDao.isResourceSyncFinish());
	}

	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	public void testGetData_Null()
	{
		try
		{
			assertNull(resourceBarDao.getData(null));
			
			AudioData data = AudioDataFactory.getInstance().createAudioData(-1);
			assertNull(resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	public void testGetData_URL()
	{
		AudioData data = AudioDataFactory.getInstance().createAudioData("en_us/audio/test.mp3");
		EasyMock.expect(ioManager.openFileBytesFromAppBundle(data.getResourceUri())).andReturn(audioData);
		PowerMock.replayAll();
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
			PowerMock.verifyAll();
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	public void testGetData_static_ID()
	{
		AudioData data = AudioDataFactory.getInstance().createAudioData(111);
		resourceBarDao.putStaticAudio(111, audioData);
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	public void testGetData_NavCache_ID()
	{
		int resourceId = 111 + resourceBarDao.MAX_STATIC_AUDIO_ID;
		AudioData data = AudioDataFactory.getInstance().createAudioData(resourceId);
		data.setCategory(resourceBarDao.AUDIO_WHAT_NAVIGATION);
		navAudiosCache.put(PrimitiveTypeCache.valueOf(resourceId), audioData);
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	@Test(expected=AssertionFailedError.class)
	public void testGetData_NavCache_ID_PersistableStore()
	{
		int resourceId = 111 + resourceBarDao.MAX_STATIC_AUDIO_ID;
		AudioData data = AudioDataFactory.getInstance().createAudioData(resourceId);
		data.setCategory(resourceBarDao.AUDIO_WHAT_NAVIGATION);
		navAudiosCache.put(PrimitiveTypeCache.valueOf(resourceId), "test".getBytes());
		persistableAudioStore.put(PrimitiveTypeCache.valueOf(resourceId), audioData);
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	public void testGetData_TrafficCache_ID()
	{
		int resourceId = 111 + resourceBarDao.MAX_STATIC_AUDIO_ID;
		AudioData data = AudioDataFactory.getInstance().createAudioData(resourceId);
		data.setCategory(resourceBarDao.AUDIO_WHAT_TRAFFIC);
		trafficAudiosCache.put(PrimitiveTypeCache.valueOf(resourceId), audioData);
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	@Test(expected=AssertionFailedError.class)
	public void testGetData_TrafficCache_ID_PersistableStore()
	{
		int resourceId = 111 + resourceBarDao.MAX_STATIC_AUDIO_ID;
		AudioData data = AudioDataFactory.getInstance().createAudioData(resourceId);
		data.setCategory(resourceBarDao.AUDIO_WHAT_TRAFFIC);
		trafficAudiosCache.put(PrimitiveTypeCache.valueOf(resourceId), "test".getBytes());
		persistableAudioStore.put(PrimitiveTypeCache.valueOf(resourceId), audioData);
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	public void testGetData_DynamicCache_ID()
	{
		int resourceId = 111 + resourceBarDao.MAX_STATIC_AUDIO_ID;
		AudioData data = AudioDataFactory.getInstance().createAudioData(resourceId);
		data.setCategory(resourceBarDao.AUDIO_WHAT_DEFAULT);
		dynamicAudioCache.put(PrimitiveTypeCache.valueOf(resourceId), audioData);
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
	
	/**
	 * Test method for {@link com.telenav.data.dao.serverproxy.ResourceBarDao#getData(com.telenav.datatypes.audio.AudioData)}.
	 */
	@Test(expected=AssertionFailedError.class)
	public void testGetData_DynamicCache_ID_PersistableStore()
	{
		int resourceId = 111 + resourceBarDao.MAX_STATIC_AUDIO_ID;
		AudioData data = AudioDataFactory.getInstance().createAudioData(resourceId);
		data.setCategory(resourceBarDao.AUDIO_WHAT_DEFAULT);
		dynamicAudioCache.put(PrimitiveTypeCache.valueOf(resourceId), "test".getBytes());
		persistableAudioStore.put(PrimitiveTypeCache.valueOf(resourceId), audioData);
		try
		{
			Assert.assertArrayEquals(audioData,resourceBarDao.getData(data));
		} catch (Throwable e)
		{
			fail("exception is thrown");
		}
	}
// --------------------------------------------------------------------------------utility methods --------------------------------------------------
	private void assertPoicategory(PoiCategory expected, PoiCategory actual)
	{
		if (null == expected)
		{
			assertNull(actual);
			return;
		}
		assertNotNull(actual);
		assertEquals(expected.getChildrenSize(), actual.getChildrenSize());
		assertEquals(expected.getCategoryId(), actual.getCategoryId());
		assertEquals(expected.getFocusedImagePath(), actual.getFocusedImagePath());
		assertEquals(expected.getUnfocusedImagePath(), actual.getUnfocusedImagePath());
		assertEquals(expected.getName(), actual.getName());
		for (int index=0; index< expected.getChildrenSize(); index++)
		{
			assertPoicategory(expected.getChildAt(index), actual.getChildAt(index));
		}
	}
	
	private PoiCategory createPoiCategory()
	{
		PoiCategory expected = new PoiCategory();
		expected.setCategoryId(1);
		expected.setFocusedImagePath("test\\image");
		expected.setUnfocusedImagePath("test\\image");
		expected.setName("node_1");
		PoiCategory child = new PoiCategory();
		child.setCategoryId(2);
		child.setFocusedImagePath("test\\image\\child");
		child.setUnfocusedImagePath("test\\image\\child");
		child.setName("child");
		expected.addChild(child);
		return expected;
	}
	
	private StringList createBrandName()
	{
		StringList stringlist = new StringList();
		stringlist.add("att");
		stringlist.add("sprint");
		stringlist.add("rogers");
		stringlist.add("bell");
		return stringlist;
	}
	
	private void assertStringList(StringList expected, StringList actual)
	{
		if (null == expected)
		{
			assertNull(actual);
			return;
		}
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		for (int index=0; index < expected.size(); index++)
		{
			assertEquals(expected.elementAt(index), actual.elementAt(index));
		}
	}
	
	private Vector createStops()
	{
		Vector stops = new Vector();
		Stop sfoStop = new Stop();
        sfoStop.setId(2100);
        sfoStop.setIsGeocoded(false);
        sfoStop.setLabel("SFO Air port");
        sfoStop.setLat(3737392);
        sfoStop.setLon(-12201074);
        
        Stop wmStop = new Stop();
        sfoStop.setLabel("wma Air port");
        wmStop.setLat(3737392);
        wmStop.setLon(-12209075);
        stops.add(sfoStop);
        stops.add(wmStop);
		return stops;
	}
	
	private MapDataUpgradeInfo createMapDataUpgradeInfo()
	{
		MapDataUpgradeInfo mapData = new MapDataUpgradeInfo();
		mapData = new MapDataUpgradeInfo();
        mapData.setUpgradeMode(1);
        mapData.setName("mapdata_name");
        mapData.setVersion("mapdata_version");
        mapData.setBuildNumber("mapdata_buildNumber");
        mapData.setRegion("mapdata_region");
        mapData.setState("mapdata_state");
        mapData.setUrl("mapdata_url");
        mapData.setSummary("mapdata_summary");
        mapData.setMapDataSize("mapdata_size");
        return mapData;
	}

}

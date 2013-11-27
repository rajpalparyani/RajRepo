/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ResourceBackupDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import static org.junit.Assert.assertArrayEquals;

import java.util.Enumeration;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.module.region.RegionUtil;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;
/**
 *@author bmyang
 *@date 2011-6-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractDaoManager.class, RegionUtil.class})
public class ResourceBackupDaoTest extends TestCase
{
	private ResourceBackupDao resourceBackupDao;
	private TnStore audioRuleStore;
	private TnStore cserverNodeStore;
	private TnStore resourceBarVersionStore;
	private TnStore persistableAudioStore;
	private TnStore backupPreferenceStore;
	private TnRegionDependentStoreProvider cserverNodeStoreProvider;
	private TnRegionDependentStoreProvider backupPreferenceStoreProvider;

	public void setUp() throws Exception 
	{
		audioRuleStore = new TnStoreMock();
		audioRuleStore.load();
		audioRuleStore.put("1", "audio test".getBytes());
		cserverNodeStore = new TnStoreMock();
		cserverNodeStore.load();
		cserverNodeStore.put("2", "cserver node test".getBytes());
		resourceBarVersionStore = new TnStoreMock();
		resourceBarVersionStore.load();
		resourceBarVersionStore.put("3", "resource bar store test".getBytes());
		persistableAudioStore = new TnStoreMock();
		persistableAudioStore.load();
        persistableAudioStore.put("4", "persistable audio store test".getBytes());
        backupPreferenceStore = new TnStoreMock();
        backupPreferenceStore.load();
        backupPreferenceStore.put("5", "backup preference Store test".getBytes());
        resourceBackupDao = new ResourceBackupDao(persistableAudioStore, audioRuleStore, resourceBarVersionStore, cserverNodeStore,
                backupPreferenceStore);
        cserverNodeStoreProvider = PowerMock.createMock(TnRegionDependentStoreProvider.class);
        backupPreferenceStoreProvider = PowerMock.createMock(TnRegionDependentStoreProvider.class);
        Whitebox.setInternalState(resourceBackupDao, "cserverNodeStoreProvider", cserverNodeStoreProvider);
        Whitebox.setInternalState(resourceBackupDao, "backupPreferenceStoreProvider", backupPreferenceStoreProvider);
    }

	/*public void testBackupDone() 
	{
		TnStore resourceBarVersionStore = new TnStoreMock();
		resourceBarVersionStore.load();
		
		TnStore cserverNodeStore = new TnStoreMock();
		cserverNodeStore.load();
		
		TnStore persistableAudioStore = new TnStoreMock();
		persistableAudioStore.load();
		
		TnStore audioRuleStore = new TnStoreMock();
		audioRuleStore.load();
		//the snapshot of resourceBarVersionStore
		TnStore resourceTemp = new TnStoreMock();
		resourceTemp.load();
		resourceBackupDao.copyStore(this.resourceBarVersionStore, resourceTemp);
		resourceTemp.put(ResourceBarDao.KEY_B_IS_SYNC_FINISH, "1".getBytes());
		//the snapshot of cserverNodeStore
		
		PowerMock.mockStatic(RegionUtil.class);
		RegionUtil reginUtil = PowerMock.createMock(RegionUtil.class);
		EasyMock.expect(RegionUtil.getInstance()).andReturn(reginUtil).anyTimes();
		EasyMock.expect(reginUtil.getCurrentRegion()).andReturn("").anyTimes();
		
		TnStore cserverTemp = new TnStoreMock();
		cserverTemp.load();
		resourceBackupDao.copyStore(this.cserverNodeStore, cserverTemp);
		//the snapshot of persistableAudioStore
		TnStore persistableTemp = new TnStoreMock();
		persistableTemp.load();
		resourceBackupDao.copyStore(this.persistableAudioStore, persistableTemp);
		//the snapshot of audioRuleStore
		TnStore audioRuleTemp = new TnStoreMock();
		audioRuleTemp.load();
		resourceBackupDao.copyStore(this.audioRuleStore, audioRuleTemp);
	    //the snapshot of preference backup store
        TnStore preferenceBackupTemp = new TnStoreMock();
        preferenceBackupTemp.load();
        resourceBackupDao.copyStore(this.backupPreferenceStore, preferenceBackupTemp);

        ResourceBarDao resourceBarDao = new ResourceBarDao(persistableAudioStore, audioRuleStore, resourceBarVersionStore,
                cserverNodeStore, preferenceBackupTemp, null, null, null, null, null);
        TnRegionDependentStoreProvider cserverNodeStoreProvider1 = PowerMock.createMock(TnRegionDependentStoreProvider.class);
        TnRegionDependentStoreProvider backupPreferenceStoreProvider1 = PowerMock.createMock(TnRegionDependentStoreProvider.class);
        Whitebox.setInternalState(resourceBarDao, "cserverNodeStoreProvider", cserverNodeStoreProvider1);
        Whitebox.setInternalState(resourceBarDao, "backupPreferenceStoreProvider", backupPreferenceStoreProvider1);
        
        PowerMock.mockStatic(AbstractDaoManager.class);
        AbstractDaoManager manager = PowerMock.createMock(AbstractDaoManager.class);
        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(manager);
        EasyMock.expect(manager.getResourceBarDao()).andReturn(resourceBarDao);
        
        TnStoreMock store1 = new TnStoreMock();
        TnStoreMock store2 = new TnStoreMock();
        TnStoreMock store3 = new TnStoreMock();
        TnStoreMock store4 = new TnStoreMock();
        store1.load();
        store2.load();
        store3.load();
        store4.load();
        
        cserverNodeStoreProvider1.clear();
        backupPreferenceStoreProvider1.clear();
        EasyMock.expect(cserverNodeStoreProvider.getStore(null)).andReturn(store1);
        EasyMock.expect(backupPreferenceStoreProvider.getStore(null)).andReturn(store2);
        EasyMock.expect(cserverNodeStoreProvider1.getStore(null)).andReturn(store3);
        EasyMock.expect(backupPreferenceStoreProvider1.getStore(null)).andReturn(store4);
        
        cserverNodeStoreProvider1.save();
        backupPreferenceStoreProvider1.save();
        
        cserverNodeStoreProvider.clear();
        backupPreferenceStoreProvider.clear();
        
		PowerMock.replayAll();
		resourceBackupDao.backupDone(null);
		
		assertTnStore(audioRuleTemp, audioRuleStore);
		assertTnStore(persistableTemp, persistableAudioStore);
		assertTnStore(cserverTemp, cserverNodeStore);
//		assertTnStore(resourceTemp, resourceBarVersionStore);
		//test clear
		assertEquals(0, this.audioRuleStore.size());
		assertEquals(0, this.cserverNodeStore.size());
		assertEquals(0, this.persistableAudioStore.size());
		assertEquals(0, this.resourceBarVersionStore.size());
		PowerMock.verifyAll();
	}*/

	public void testCopyStore() 
	{
		TnStoreMock store = new TnStoreMock();
		store.load();
		TnStoreMock dest = new TnStoreMock();
		dest.load();
		store.put(1, "a".getBytes());
		store.put("2", "b".getBytes());
		resourceBackupDao.copyStore(store, dest);
		assertTnStore(store, dest);
		
	}
	
	public void testCopyStoreNull() 
	{
		TnStoreMock store = new TnStoreMock();
		store.load();
		//test null parameters, no exception is thrown.
		resourceBackupDao.copyStore(null, null); 
		resourceBackupDao.copyStore(store, null);
		resourceBackupDao.copyStore(null, store);
	}
	
	void assertTnStore(TnStore expected, TnStore actual)
	{
		if (null == expected)
		{
			assertNull(actual);
			return ;
		}
		assertNotNull(actual);
		containStore(expected, actual);
		containStore(actual, expected);
	}

	private void containStore(TnStore child, TnStore parent)
	{
		Enumeration keys = child.keys();
	     while( keys.hasMoreElements() )
	        {
	            String key = (String)keys.nextElement();
	            assertArrayEquals(child.get(key), parent.get(key));
	        }
	}
	
	

}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * StartupDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 *@author hchai
 *@date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TnPersistentManager.class)
public class StartupDaoTest extends TestCase
{
    protected TnStore tnStore;

    protected StartupDao startupDao;
    
    StringList list;

    byte[] expected;
    
    protected void setUp() throws Exception
    {
    	TnPersistentManager manager = PowerMock.createMock(TnPersistentManager.class);
    	PowerMock.mockStatic(TnPersistentManager.class);
    	EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager);
    	tnStore = new TnStoreMock();
    	tnStore.load();
    	EasyMock.expect(manager.createStore(TnPersistentManager.DATABASE, "startup_data")).andReturn(tnStore);
    	PowerMock.replayAll();
    	startupDao = StartupDao.getInstance();
        SerializableManager.init(new TxNodeSerializableManager());
        tnStore.clear();
        startupDao.setStore(tnStore);
        list = new StringList();
        list.add("OPTION");
        list.add("SN3.0");
        list.add("6.0.0");
        list.add("http://telenav.com");
        list.add("TN6.0");
        list.add("1010");
        list.add("5.0.0");
        list.add("6.0.0");

        expected = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(list);
        startupDao.setUpgradeInfo(list);

        super.setUp();
    }

    public void testGetUpgradeUrl()
    {
        assertEquals("http://telenav.com", startupDao.getUpgradeUrl());
    }
    
    public void testGetUpgradeSummary()
    {
        assertEquals("TN6.0", startupDao.getUpgradeSummary());
    }
    
    public void testGetUpgradeBuildNumber()
    {
        assertEquals("1010", startupDao.getUpgradeBuildNumber());
    }
    
    public void testGetMapDataset()
    {
        Whitebox.setInternalState(startupDao, "mapDataset", (String)null);
        assertEquals("", startupDao.getMapDataset());
        
        Whitebox.setInternalState(startupDao, "mapDataset", "Navteq");
        assertEquals("Navteq", startupDao.getMapDataset());
    }
    
}

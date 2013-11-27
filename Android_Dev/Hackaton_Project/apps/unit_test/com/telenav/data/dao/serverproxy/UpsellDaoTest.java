/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * UpsellDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 *@author hchai
 *@date 2011-6-21
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SerializableManager.class,TnStore.class})
public class UpsellDaoTest extends TestCase
{
    protected UpsellDao upsellDao;

    protected TnStore upsellStore;

    @Override
    protected void setUp() throws Exception
    {
        upsellStore = PowerMock.createMock(TnStore.class);
        EasyMock.expect(upsellStore.getName()).andReturn("upsell");
        EasyMock.expect(upsellStore.getType()).andReturn(TnPersistentManager.DATABASE);
        
        super.setUp();
    }

    public void testSetUpsellData()
    {
        StringMap stringMap = new StringMap();
        stringMap.put("carrier", "Sprint");
        stringMap.put("product", "TN_NAV");

        SerializableManager.init(new TxNodeSerializableManager());
        byte[] expected = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(stringMap);
        EasyMock.expect(upsellStore.get(UpsellDao.UPSELL_INFO_INDEX)).andReturn(null);
        upsellStore.load();
        upsellStore.put(EasyMock.eq(UpsellDao.UPSELL_INFO_INDEX), EasyMock.aryEq(expected));
        PowerMock.replayAll();
        
        upsellDao = new UpsellDao(upsellStore);
        upsellStore.load();
        upsellDao.setUpsellData(stringMap);
        
        PowerMock.verifyAll();
        
    }
    
    public void testGetValue()
    {
        StringMap stringMap = new StringMap();
        stringMap.put("carrier", "Sprint");

        SerializableManager.init(new TxNodeSerializableManager());
        byte[] expected = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(stringMap);
        EasyMock.expect(upsellStore.get(UpsellDao.UPSELL_INFO_INDEX)).andReturn(null).anyTimes();
        upsellStore.load();
        upsellStore.put(EasyMock.eq(UpsellDao.UPSELL_INFO_INDEX), EasyMock.aryEq(expected));
        PowerMock.replayAll();
        
        upsellDao = new UpsellDao(upsellStore);
        upsellStore.load();
        upsellDao.setUpsellData(stringMap);
        
        PowerMock.verifyAll();
        
        assertEquals("Sprint", upsellDao.getValue("carrier"));
    }
}

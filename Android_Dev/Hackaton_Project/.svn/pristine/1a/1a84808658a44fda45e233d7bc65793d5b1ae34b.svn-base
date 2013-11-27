/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SecretSettingDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.dao.misc.CommHostDao;
import com.telenav.data.dao.misc.SecretSettingDao;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStore;

import junit.framework.TestCase;

/**
 *@author bduan
 *@date 2011-8-19
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnStore.class})
public class SecretSettingDaoTest extends TestCase
{
    protected TnStore tnStore;

    protected SecretSettingDao secretSettingDao;
    
    StringList list;

    byte[] expected;
    
    protected void setUp() throws Exception
    {
        tnStore = PowerMock.createMock(TnStore.class);
        
        secretSettingDao = new SecretSettingDao(tnStore);
        
        super.setUp();
    }

    public void testGetInt()
    {
        EasyMock.expect(tnStore.get(1001)).andReturn("1".getBytes());
        
        EasyMock.expect(tnStore.get(EasyMock.anyInt())).andReturn(null);
        
        PowerMock.replayAll();
        
        assertEquals(1, secretSettingDao.get(1001));
        
        assertEquals(-1, secretSettingDao.get(1000));
        
        PowerMock.verifyAll();
    }
    
}

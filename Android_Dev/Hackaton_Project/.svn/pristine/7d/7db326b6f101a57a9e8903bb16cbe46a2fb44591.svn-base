/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AppStoreDaoTest.java
 *
 */
package com.telenav.data.dao.misc;


import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

import junit.framework.TestCase;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AppStoreDao.class, TnStore.class})
public class AppStoreDaoTest extends TestCase
{
    byte[] data = {6, 5, 4, 3, 2, 1};
    String id_test = "test_data";
    TnStore mock = null;
    
    protected void setUp() throws Exception
    {
        mock = new TnStoreMock();
        mock.load();
        mock.put(id_test, data);
        super.setUp();
    }

    public void testStore()
    {
        PowerMock.resetAll();
        TnStore mockStore = PowerMock.createMock(TnStore.class);

        mockStore.save();
        PowerMock.replayAll();
        AppStoreDao appDao = new AppStoreDao(mockStore);
        appDao.store();
        PowerMock.verifyAll();
    }
    
    public void testClear()
    {
        assertTrue(mock.size() == 1);
        assertEquals(data, mock.get(id_test));
        AppStoreDao appDao = new AppStoreDao(mock);
        appDao.clear();
        assertNull(appDao.get(id_test));
    }
    
    public void testSet()
    {
        byte[] test = {1, 2, 3, 4, 5, 6};
        assertEquals(data, mock.get(id_test));
        AppStoreDao appDao = new AppStoreDao(mock);
        appDao.set(id_test, test);
        assertEquals(test, appDao.get(id_test));
    }
    
    public void testGet()
    {
        PowerMock.resetAll();
        TnStore mockStore = PowerMock.createMock(TnStore.class);

        EasyMock.expect(mockStore.get(id_test)).andReturn(data);
        PowerMock.replayAll();
        AppStoreDao appDao = new AppStoreDao(mockStore);
        Assert.assertArrayEquals(data, appDao.get(id_test));
        PowerMock.verifyAll();
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ApacheResouceDaoTest.java
 *
 */
package com.telenav.data.dao.misc;


import java.util.Enumeration;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

import junit.framework.TestCase;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-6-20
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ApacheResouceDao.class, TnStore.class})
public class ApacheResouceDaoTest extends TestCase
{
    TnStore store = null;
    Enumeration expected = null;
    byte[] data = {6, 5, 4, 3, 2, 1};
    String id_one = "test";
    String id_two = "testSize";
    String id_three = "testKeys";
    
    @Override
    protected void setUp() throws Exception
    {
        store = new TnStoreMock();
        store.load();
        store.put(id_one, new byte[0]);
        store.put(id_two, new byte[0]);
        store.put(id_three, new byte[0]);
        expected = store.keys();
        super.setUp();
    }
    
    public void testClear()
    {
        ApacheResouceDao apacheDao = new ApacheResouceDao(store);
        assertEquals(3, apacheDao.size());
        apacheDao.clear();
        assertEquals(0, apacheDao.size());
        assertFalse(apacheDao.contains(id_one));
        assertFalse(apacheDao.contains(id_two));
        assertFalse(apacheDao.contains(id_three));
    }
    
    public void testStore()
    {
        PowerMock.resetAll();
        TnStore mockStore = PowerMock.createMock(TnStore.class);

        mockStore.save();
        PowerMock.replayAll();
        ApacheResouceDao apacheDao = new ApacheResouceDao(mockStore);
        apacheDao.store();
        PowerMock.verifyAll();
    }
    
    public void testContains()
    {
        ApacheResouceDao apacheDao = new ApacheResouceDao(store);
        assertTrue(apacheDao.contains(id_one));
        assertTrue(apacheDao.contains(id_two));
        assertTrue(apacheDao.contains(id_three));
        apacheDao.clear();
        assertFalse(apacheDao.contains(id_one));
        assertFalse(apacheDao.contains(id_two));
        assertFalse(apacheDao.contains(id_three));
    }
    
    public void testSize()
    {
        ApacheResouceDao apacheDao = new ApacheResouceDao(store);
        assertEquals(3, apacheDao.size());
    }
    
    public void testPut()
    {
        ApacheResouceDao apacheDao = new ApacheResouceDao(store);
        apacheDao.put("testPut", data);
        assertEquals(4, apacheDao.size());
        assertTrue(apacheDao.contains("testPut"));
        assertEquals(data, apacheDao.get("testPut"));
    }
    
    public void testGet()
    {
        store.put(id_one, data);
        ApacheResouceDao apacheDao = new ApacheResouceDao(store);
        assertEquals(3, apacheDao.size());
        assertTrue(apacheDao.contains(id_one));
        assertEquals(data, apacheDao.get(id_one));
    }
    
    public void testKeys()
    {
        ApacheResouceDao apacheDao = new ApacheResouceDao(store);
        assertEquals(3, apacheDao.size());
        assertEnumeration(expected, apacheDao.keys());
    }
    
    protected void assertEnumeration(Enumeration expected, Enumeration actual)
    {
        if ( expected == null )
        {
            assertNull(actual);
            return;
        }
        assertNotNull(expected);
        assertNotNull(actual);
        while( expected.hasMoreElements() && actual.hasMoreElements() )
        {
            assertEquals(expected.nextElement(), actual.nextElement());
        }
        assertEquals(expected.hasMoreElements(), actual.hasMoreElements());
    }

}

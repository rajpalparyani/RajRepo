/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SecretSettingDaoTest.java
 *
 */
package com.telenav.data.dao.misc;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 * @author yhzhou
 * @date 2011-9-23
 */
@RunWith(PowerMockRunner.class)
public class SecretSettingDaoTest extends TestCase
{
    TnStore store = null;

    SecretSettingDao secretSettingDao = null;

    int test_int = 100;

    Boolean test_boolean = true;

    String test_str = "110";

    public SecretSettingDaoTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        store = new TnStoreMock();
        store.load();
        secretSettingDao = new SecretSettingDao(store);
        secretSettingDao.put(1, test_int);
        secretSettingDao.put(2, test_boolean);
        secretSettingDao.put(3, test_str);
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        secretSettingDao.clear();
        super.tearDown();
    }

    public void testStore()
    {
        PowerMock.resetAll();
        store.save();
        PowerMock.replayAll();
        SecretSettingDao secretSettingsDao = new SecretSettingDao(store);
        secretSettingsDao.store();
        PowerMock.verifyAll();
    }

    public void testClear()
    {
        assertEquals(test_int, secretSettingDao.get(1));
        assertEquals(1, secretSettingDao.get(2));
        assertEquals(110, secretSettingDao.get(3));

        secretSettingDao.clear();

        assertEquals(-1, secretSettingDao.get(1));
        assertEquals(-1, secretSettingDao.get(2));
        assertEquals(-1, secretSettingDao.get(3));

    }

    public void testRemove()
    {
        assertEquals(100, secretSettingDao.get(1));
        secretSettingDao.remove(1);
        assertEquals(-1, secretSettingDao.get(1));
    }

    public void testPutIntInt()
    {
        secretSettingDao.put(4, 400);
        assertEquals(400, secretSettingDao.get(4));
    }

    public void testGet()
    {
        assertEquals(100, secretSettingDao.get(1));
        assertEquals(-1, secretSettingDao.get(4));
    }

    public void testPutIntString()
    {
        secretSettingDao.put(4, "110");
        assertEquals(110, secretSettingDao.get(4));
    }

    public void testPutIntBoolean()
    {
        secretSettingDao.put(4, true);
        assertEquals(1, secretSettingDao.get(4));
    }

    public void testGetBoolean()
    {
        secretSettingDao.put(5, "1");
        assertEquals(true, secretSettingDao.getBoolean(5));
        assertEquals(false, secretSettingDao.getBoolean(3));
        assertEquals(false, secretSettingDao.getBoolean(4));
    }

    public void testGetString()
    {
        assertEquals(null, secretSettingDao.getString(4));
        assertEquals("110", secretSettingDao.getString(3));
    }

}

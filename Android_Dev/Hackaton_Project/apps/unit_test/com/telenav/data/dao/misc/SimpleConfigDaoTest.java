/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SimpleConfigDaoTest.java
 *
 */
package com.telenav.data.dao.misc;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.serializable.SerializableManager;
import com.telenav.persistent.TnStore;

/**
 *@author jxue
 *@date 2011-6-21
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SerializableManager.class,TnStore.class})

public class SimpleConfigDaoTest extends TestCase
{
    TnStore store;

    SimpleConfigDao simpleConfigDao;

    @Override
    protected void setUp() throws Exception
    {
        store = PowerMock.createMock(TnStore.class);

        simpleConfigDao = new SimpleConfigDao(store, null);

        super.setUp();
    }
    
    @Test
    public void testRemove()
    {
        //record
        int key = EasyMock.anyInt();
        store.remove(key);
       
        //replay
        PowerMock.replayAll();
        
        simpleConfigDao.remove(key);
        
        PowerMock.verifyAll();
    }
    
    @Test
    public void testPutFromInt()
    {
        // record
        int key = EasyMock.anyInt();
        int value = EasyMock.anyInt();
        store.put(key, ("" + value).getBytes());;

        // replay
        PowerMock.replayAll();
        
        simpleConfigDao.put(key,value);

        PowerMock.verifyAll();
    }
    
    @Test
    public void testGet()
    {
        // record
        int key = EasyMock.anyInt();
        String string = "1111";
        byte[] data = string.getBytes();
       
        EasyMock.expect(store.get(key)).andReturn(data);
        EasyMock.expect(store.get(key)).andReturn(null);

        // replay
        PowerMock.replayAll();

        assertEquals(Integer.parseInt(string), simpleConfigDao.get(key));
        assertEquals(-1, simpleConfigDao.get(key));
        
        PowerMock.verifyAll();
    }
    
    
    @Test
    public void testPutFromString()
    {
        // record
        int key = EasyMock.anyInt();
        String value = "abcd";
        store.put(key, EasyMock.aryEq(value.getBytes()));

        // replay
        PowerMock.replayAll();

        simpleConfigDao.put(key,value);

        PowerMock.verifyAll();
        
    }
    
    @Test
    public void testPutFromBoolean()
    {
        // record
        int key1 = 1;
        int key2 = 2;
        boolean value1 = true;
        boolean value2 = false;
        store.put(EasyMock.eq(key1), EasyMock.aryEq("1".getBytes()));
        store.put(EasyMock.eq(key2), EasyMock.aryEq("0".getBytes()));
        
        // replay
        PowerMock.replayAll();
        simpleConfigDao.put(key1, value1);
        simpleConfigDao.put(key2, value2);

        PowerMock.verifyAll();
    }
    
   @Test
    public void testGetString()
    {
        // record
        int key = EasyMock.anyInt();
        String string = "abcd";
        byte[] data = string.getBytes();
       
        EasyMock.expect(store.get(key)).andReturn(data);
        EasyMock.expect(store.get(key)).andReturn(null);
        
        // replay
        PowerMock.replayAll();

        assertEquals(string, simpleConfigDao.getString(key));
        assertEquals(null, simpleConfigDao.getString(key));

        PowerMock.verifyAll();
    }
   
    @Test
    public void testGetBoolean()
    { 
        int key = EasyMock.anyInt();
        String string1 = "1";
        String string2 = "111";
        byte[] data1 = string1.getBytes();
        byte[] data2 = string2.getBytes();        
        
        EasyMock.expect(store.get(key)).andReturn(data1);
        EasyMock.expect(store.get(key)).andReturn(data2);
               
        // replay
        PowerMock.replayAll();
        
        assertEquals(true, simpleConfigDao.getBoolean(key));
        assertEquals(false, simpleConfigDao.getBoolean(key));
        
        PowerMock.verifyAll();
    }
    
    @Test
    public void testStore()
    {
        // record
        store.save();

        // replay
        PowerMock.replayAll();

        simpleConfigDao.store();

        PowerMock.verifyAll();
    }

    @Test
    public void testClear()
    {
        // record
        store.clear();

        // replay
        PowerMock.replayAll();

        simpleConfigDao.clear();

        PowerMock.verifyAll();
        
    }
}

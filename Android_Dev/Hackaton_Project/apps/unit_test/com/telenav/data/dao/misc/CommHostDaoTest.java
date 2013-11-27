/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestCommHostDao.java
 *
 */
package com.telenav.data.dao.misc;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.persistent.TnStore;

/**
 *@author bduan
 *@date 2011-6-21
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnStore.class})
public class CommHostDaoTest extends TestCase
{
    String url = "www.telenav.com";
    
    TnStore mockCommHostStore;
    
    CommHostDao commHostDao;
    
    @Override
    protected void setUp() throws Exception
    {
        mockCommHostStore = PowerMock.createMock(TnStore.class);
        
        commHostDao = new CommHostDao(mockCommHostStore);
        
        super.setUp();
    }
    
    public void testPutHost()
    {
        mockCommHostStore.put(EasyMock.eq(1111), EasyMock.aryEq(url.getBytes()));
        
        PowerMock.replayAll();
        
        commHostDao.putHost(1111, url);
        
        PowerMock.verifyAll();
    }
    
    public void testGetHost()
    {
        EasyMock.expect(mockCommHostStore.get(1111)).andReturn(url.getBytes());
        
        EasyMock.expect(mockCommHostStore.get(EasyMock.anyInt())).andReturn(null);
        
        PowerMock.replayAll();
        
        assertEquals(url, commHostDao.getHost(1111));
        
        assertEquals(null, commHostDao.getHost(1000));
        
        PowerMock.verifyAll();
    }
    
    public void testSetDefaultSelectedIndex()
    {
        //private final static int URL_GROUP_SELECT_INDEX = 100000;
        mockCommHostStore.put(EasyMock.eq(100000), EasyMock.aryEq("1".getBytes()));
        
        PowerMock.replayAll();
        
        commHostDao.setDefaultSelectedIndex(1);
        
        PowerMock.verifyAll();
    }
    
    public void testGetDefaultSelectIndex()
    {
        //private final static int URL_GROUP_SELECT_INDEX = 100000;
        EasyMock.expect(mockCommHostStore.get(100000)).andReturn("1".getBytes());
        
        PowerMock.replayAll();
        
        assertEquals(1, commHostDao.getDefaultSelectedIndex());
        
        PowerMock.verifyAll();
        
    }
    
    public void testGetHostSize()
    {
        //private final static int URL_GROUP_SELECT_INDEX = 100000;
        EasyMock.expect(mockCommHostStore.size()).andReturn(100);
        
        PowerMock.replayAll();
        
        assertEquals(100, commHostDao.getHostSize());
        
        PowerMock.verifyAll();
    }
    
}

package com.telenav.data.dao.misc;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;

import com.telenav.persistent.TnStoreMock;

/**
 *@author gbwang
 *@date 2011-6-22
 */

public class BrowserSdkServiceDaoTest extends TestCase
{ 
    TnStoreMock mockBrowserStore; 
    BrowserSdkServiceDao browserSdkServiceDao = null;
    
    @Override
    protected void setUp() throws Exception
    {
    	mockBrowserStore = new TnStoreMock(); 
    	mockBrowserStore.load();
    	browserSdkServiceDao = new BrowserSdkServiceDao(mockBrowserStore);
        super.setUp();
    }
    
    public void testSet()
    {
    	String value = "test";
    	String key = "10000";
        
        browserSdkServiceDao.set(key, value.getBytes());    
        browserSdkServiceDao.store();
        byte[] actualValue = (byte[]) browserSdkServiceDao.get(key);
        Assert.assertArrayEquals(value.getBytes(), actualValue); 
    }
    
    public void testClear()
    {
    	String key = EasyMock.anyObject();
        
        browserSdkServiceDao.clear();
        Assert.assertNull(browserSdkServiceDao.get(key));
    }
    
    public void testDelete()
    {
    	String key = "1000";
        
        browserSdkServiceDao.delete(key);
        Assert.assertNull(browserSdkServiceDao.get(key));
    }
    
}

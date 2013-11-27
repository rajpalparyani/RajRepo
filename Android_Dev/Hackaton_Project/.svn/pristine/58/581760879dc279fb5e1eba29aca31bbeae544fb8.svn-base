/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ExpressAddressDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStoreMock;

import junit.framework.TestCase;

/**
 *@author hchai
 *@date 2011-7-26
 */
@RunWith(PowerMockRunner.class)
public class ExpressAddressDaoTest extends TestCase
{
    protected TnStoreMock store;
    
    
    protected ExpressAddressDao dao;
    protected void setUp() throws Exception
    {
        SerializableManager.init(new TxNodeSerializableManager());
        store = new TnStoreMock();
        store.load();
        dao = new ExpressAddressDao(store);
        
        super.setUp();
    }


    public void testSetHome()
    {
        Stop stop = new Stop();
        stop.setCity("Las Vegas");
        dao.setHomeAddress(stop);
        dao.store();
        
        Stop rmsStop = dao.getHomeAddress();
        assertEquals(stop.getCity(), rmsStop.getCity());
    }
    
    public void testSetHomeAsNull()
    {
        Stop stop = new Stop();
        stop.setCity("Las Vegas");
        dao.setHomeAddress(stop);
        dao.store();
        
        dao.setHomeAddress(null);
        dao.store();
        
        Stop rmsStop = dao.getHomeAddress();
        assertEquals(rmsStop, null);
    }
    
    public void testSetOffice()
    {
        Stop stop = new Stop();
        stop.setCity("Las Vegas");
        dao.setOfficeAddress(stop);
        dao.store();
        
        Stop rmsStop = dao.getOfficeAddress();
        assertEquals(stop.getCity(), rmsStop.getCity());
    }
    
    public void testSetOfficeAsNull()
    {
        Stop stop = new Stop();
        stop.setCity("Las Vegas");
        dao.setOfficeAddress(stop);
        dao.store();
        
        dao.setOfficeAddress(null);
        dao.store();
        
        Stop rmsStop = dao.getOfficeAddress();
        assertEquals(rmsStop, null);
    }
    
    public void testClear()
    {
        Stop stop = new Stop();
        stop.setCity("Las Vegas");
        dao.setOfficeAddress(stop);
        dao.store();
        
        dao.clear();
        dao.store();
        
        Stop rmsStop = dao.getOfficeAddress();
        assertEquals(rmsStop, null);
    }
}

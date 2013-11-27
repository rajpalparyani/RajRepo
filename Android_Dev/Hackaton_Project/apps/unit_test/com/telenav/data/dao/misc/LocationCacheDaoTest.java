/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LocationCacheDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationCacheManager;
import com.telenav.persistent.TnStoreMock;

/**
 *@author hchai
 *@date 2011-6-23
 */
@RunWith(PowerMockRunner.class)
public class LocationCacheDaoTest extends TestCase
{
    protected LocationCacheDao locationCacheDao;
    
    protected TnStoreMock store;

    @Override
    protected void setUp() throws Exception
    {
        // TODO Auto-generated method stub
        SerializableManager.init(new TxNodeSerializableManager());
        store = new TnStoreMock();
        store.load();
        
        locationCacheDao = new LocationCacheDao(store);
        
        super.setUp();
    }
    
    public void testSetLastGpsLocation()
    {
        TnLocation location = new TnLocation("gps-179");
        location.setLatitude(3700000);
        location.setLongitude(-12100000);
        locationCacheDao.setLastGpsLocation(location);
        
        TnLocation rmsLocation = SerializableManager.getInstance().getLocationSerializable().createTnLocation(store.get(1)); 
        
        assertEquals(rmsLocation.getLatitude(), 3700000);
        assertEquals(rmsLocation.getLongitude(), -12100000);
    }
    
    public void testSetLastCellLocation()
    {
        TnLocation location = new TnLocation("network");
        location.setLatitude(3800000);
        location.setLongitude(-12100000);
        locationCacheDao.setLastCellLocation(location);
        
        TnLocation rmsLocation = SerializableManager.getInstance().getLocationSerializable().createTnLocation(store.get(3)); 
        
        assertEquals(rmsLocation.getProvider(), "network");
        assertEquals(rmsLocation.getLatitude(), 3800000);
    }
    
    public void testSetCellTowerCache()
    {
        Vector cellTowerCache = new Vector();
        cellTowerCache.add(new LocationCacheManager.CellTowerMapping());
        locationCacheDao.setCellTowerCache(cellTowerCache);
        assertEquals(locationCacheDao.getCellTowerCache().size(), cellTowerCache.size());
    }
    
    public void testClear()
    {
        testSetCellTowerCache();
        assert(store.size() > 0);
        locationCacheDao.clear();
        assertEquals(store.size(), 0);
    }
    
    public void testStore()
    {
        testSetCellTowerCache();
        locationCacheDao.store();
        assert(store.size() > 0);
    }
    
    public void testInit()
    {
        locationCacheDao.init();
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LocationCacheDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Vector;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationCacheManager;
import com.telenav.persistent.TnStore;
import com.telenav.radio.TnCellInfo;

/**
 *@author bduan
 *@date 2010-11-24
 */
public class LocationCacheDao extends AbstractDao
{
    protected TnStore cache;
    
    protected final int LAST_KNOWN_LOCATION = 1;
    protected final int LAST_VIEW_LOCATION = 2;
    protected final int LAST_CELL_TOWER_LOCATION = 3;
    protected final int CELL_TOWER_CACHE = 4;
    
    public LocationCacheDao(TnStore cache)
    {
        this.cache = cache;
        
        init();
    }
    
    public void setLastGpsLocation(TnLocation location)
    {
        if(location == null)
            return;
        
        byte[] locationNode = SerializableManager.getInstance().getLocationSerializable().toBytes(location);
        cache.put(LAST_KNOWN_LOCATION, locationNode);
    }
    
    public TnLocation getLastGpsLocation()
    {
        byte[] datas = cache.get(LAST_KNOWN_LOCATION);
        if(datas == null)
            return null;
        
        TnLocation location = SerializableManager.getInstance().getLocationSerializable().createTnLocation(datas);
        return location;
    }
    
    public void setLastCellLocation(TnLocation location)
    {
        if(location == null)
            return;
        
        byte[] locationNode = SerializableManager.getInstance().getLocationSerializable().toBytes(location);
        cache.put(LAST_CELL_TOWER_LOCATION, locationNode);
    }
    
    public TnLocation getLastCellLocation()
    {
        byte[] datas = cache.get(LAST_CELL_TOWER_LOCATION);
        if(datas == null)
            return null;
        
        TnLocation location = SerializableManager.getInstance().getLocationSerializable().createTnLocation(datas);
        return location;
    }
    
    public void setCellTowerCache(Vector cellTowerCache)
    {
        if (cellTowerCache == null || cellTowerCache.size() == 0)
            return;
        
        int size = cellTowerCache.size();
        BytesList list = new BytesList();
        for (int i = 0; i < size; i++)
        {
            LocationCacheManager.CellTowerMapping mapping = (LocationCacheManager.CellTowerMapping)cellTowerCache.elementAt(i);
            
            list.add(SerializableManager.getInstance().getLocationSerializable().toBytes(mapping.cellInfo));
            list.add(SerializableManager.getInstance().getLocationSerializable().toBytes(mapping.location));
        }
        
        cache.put(CELL_TOWER_CACHE, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(list));
    }
    
    public Vector getCellTowerCache()
    {
        byte[] datas = cache.get(CELL_TOWER_CACHE);
        if(datas == null)
            return null;
        
        BytesList list = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(datas);
        if(list == null || list.size() == 0)
            return null;
        
        Vector cellTowerCache = null;
        for(int i = 0 ; i < list.size() ; i++)
        {
            TnCellInfo cellInfo = SerializableManager.getInstance().getLocationSerializable().createTnCellInfo(list.elementAt(i));
            i++;
            TnLocation location = SerializableManager.getInstance().getLocationSerializable().createTnLocation(list.elementAt(i));

            if (cellTowerCache == null)
                cellTowerCache = new Vector();

            LocationCacheManager.CellTowerMapping mapping = new LocationCacheManager.CellTowerMapping();
            mapping.cellInfo = cellInfo;
            mapping.location = location;
            cellTowerCache.addElement(mapping);
        }
        
        return cellTowerCache;
    }
    
    public void clear()
    {
        cache.clear();
    }
    
    public void store()
    {
        cache.save();
    }
    
    protected void init()
    {
        
    }

}

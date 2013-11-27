/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * HomeDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.persistent.TnStore;


/**
 *@author hchai
 *@date 2011-7-26
 */
public class ExpressAddressDao extends AbstractDao
{
    final static private int KEY_HOME_ADDRESS = 1;

    final static private int KEY_OFFICE_ADDRESS = 2;
    
    private TnStore cache;
    
    protected Stop home;
    protected Stop work;
    
    
    public ExpressAddressDao(TnStore homeStore)
    {
        this.cache = homeStore;
    }

    public void setHomeAddress(Stop stop)
    {
        home = stop;
        
        if (stop == null)
        {
            this.cache.remove(KEY_HOME_ADDRESS);
            
            return;
        }
        
        byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(stop);

        this.cache.put(KEY_HOME_ADDRESS, data);
        
        this.store();
    }

    public Stop getHomeAddress()
    {
        if (home != null)
        {
            return home;
        }
        
        byte[] data = this.cache.get(KEY_HOME_ADDRESS);
        if (data == null)
            return null;
        
        Stop stop = SerializableManager.getInstance().getAddressSerializable().createStop(data);
        
        home = stop;
        return stop;
    }

    public void setOfficeAddress(Stop stop)
    {
        work = stop;
        
        if (stop == null)
        {
            this.cache.remove(KEY_OFFICE_ADDRESS);
            
            return;
        }

        byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(stop);

        this.cache.put(KEY_OFFICE_ADDRESS, data);
        
        this.store();
    }

    public Stop getOfficeAddress()
    {
        if (work != null)
        {
            return work;
        }
        
        byte[] data = this.cache.get(KEY_OFFICE_ADDRESS);
        if (data == null)
            return null;

        Stop stop = SerializableManager.getInstance().getAddressSerializable().createStop(data);
        
        work = stop;
        return stop;
    }
    
    public synchronized void clearOffice()
    {
        this.cache.remove(KEY_OFFICE_ADDRESS);
        work = null;
    }
    
    public synchronized void clearHome()
    {
        this.cache.remove(KEY_HOME_ADDRESS);
        home = null;
    }
    
    public synchronized void clear()
    {
        this.cache.clear();
        home = null;
        work = null;
    }

    public synchronized void store()
    {
        this.cache.save();
    }

}

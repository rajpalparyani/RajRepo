/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GuideToneDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Enumeration;

import com.telenav.data.dao.AbstractDao;
import com.telenav.persistent.TnStore;

/**
 * Resources which from AppStore will be stored here.
 * include GuideTone, CarModel ...
 * 
 *@author qli
 *@date 2011-1-21
 */
public class AppStoreDao extends AbstractDao
{
    
    private TnStore appStore;

    public AppStoreDao(TnStore store)
    {
        this.appStore = store;
    }
    
    public void store()
    {
        appStore.save();
    }

    public void clear()
    {
        appStore.clear();
    }
    
    public void set(String key, byte[] value)
    {
        appStore.put(key, value);
    }
    
    public byte[] get(String key)
    {
        return appStore.get(key);
    }
    
    public Enumeration keys()
    {
        return appStore.keys();
    }

}

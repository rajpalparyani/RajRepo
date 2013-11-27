/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MiniMapDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Enumeration;

import com.telenav.data.dao.AbstractDao;
import com.telenav.persistent.TnStore;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-9-21
 */
public class MiniMapDao extends AbstractDao
{
    private TnStore miniMapStore;
    
    
    public MiniMapDao(TnStore store)
    {
        this.miniMapStore = store;
    }

    public void store()
    {
        miniMapStore.save();
    }

    public void clear()
    {
        miniMapStore.clear();
    }
    
    public void put(String key, byte[] value)
    {
        miniMapStore.put(key, value);
    }
    
    public byte[] get(String key)
    {
        return miniMapStore.get(key);
    }
    
    public void put(String key, String value)
    {
        miniMapStore.put(key, value.getBytes());
    }
    
    public String getString(String key)
    {
        byte[] data = miniMapStore.get(key);
        if (data != null)
        {
            return new String(data);
        }
        return null;
    }
    
    public void remove(String key)
    {
        miniMapStore.remove(key);
    }
    
    public boolean contains(String key)
    {
        return miniMapStore.contains(key);
    }
    
    public Enumeration keys()
    {
        return miniMapStore.keys();
    }
    
    public int size()
    {
        return miniMapStore.size();
    }

}

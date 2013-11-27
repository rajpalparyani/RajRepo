/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ApacheResouceDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Enumeration;

import com.telenav.data.dao.AbstractDao;
import com.telenav.persistent.TnStore;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-4-14
 */
public class ApacheResouceDao extends AbstractDao
{

    private TnStore apacheResourceStore;
    
    public ApacheResouceDao(TnStore apacheResourceStore)
    {
        this.apacheResourceStore = apacheResourceStore;
    }

    public void clear()
    {
        this.apacheResourceStore.clear();
    }

    public void store()
    {
        this.apacheResourceStore.save();
    }

    public byte[] get(String id)
    {
        return apacheResourceStore.get(id);
    }
    
    public Enumeration keys()
    {
        return apacheResourceStore.keys();
    }
    
    public void put(String id, byte[] data)
    {
        this.apacheResourceStore.put(id, data);
    }
    
    public boolean contains(String id)
    {
        return this.apacheResourceStore.contains(id);
    }
    
    public int size()
    {
        return this.apacheResourceStore.size();
    }
}

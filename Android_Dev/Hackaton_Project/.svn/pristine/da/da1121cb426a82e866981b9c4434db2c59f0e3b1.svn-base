/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BrowserSdkServiceDao.java
 *
 */
package com.telenav.data.dao.misc;

import com.telenav.data.dao.AbstractDao;
import com.telenav.persistent.TnStore;

/**
 *@author qli
 *@date 2010-12-28
 */
public class BrowserSdkServiceDao extends AbstractDao
{
    private TnStore browserSdkServiceStore;
    
    public BrowserSdkServiceDao(TnStore browserStore)
    {
        this.browserSdkServiceStore = browserStore;
    }

    public void store()
    {
        browserSdkServiceStore.save();
    }

    public void clear()
    {
        browserSdkServiceStore.clear();
    }
    
    public Object get(String key)
    {
        return browserSdkServiceStore.get(key);
    }
    
    public void set(String key, Object value)
    {
        if( key == null || value == null )
        {
            return;
        }
        if( value instanceof byte[] )
        {
            browserSdkServiceStore.put(key, (byte[])value);
        }
    }
    
    public void delete(String key)
    {
        browserSdkServiceStore.remove(key);
    }

}

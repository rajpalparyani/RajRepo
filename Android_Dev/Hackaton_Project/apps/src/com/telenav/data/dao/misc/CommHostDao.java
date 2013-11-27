/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CommHostDao.java
 *
 */
package com.telenav.data.dao.misc;

import com.telenav.data.dao.AbstractDao;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 11, 2010
 */
public class CommHostDao extends AbstractDao
{
    private final static int URL_GROUP_SELECT_INDEX = 100000;
    
    private TnStore commHostStore;

    public CommHostDao(TnStore commHostStore)
    {
        this.commHostStore = commHostStore;
    }
    
    public void putHost(int actionIndex, String url)
    {
        this.commHostStore.put(actionIndex, url.getBytes());
    }
    
    public String getHost(int actionIndex)
    {
        byte[] data = this.commHostStore.get(actionIndex);
        if (data == null)
            return null;

        return new String(data);
    }
    
    public void setDefaultSelectedIndex(int index)
    {
        putHost(URL_GROUP_SELECT_INDEX, index + "");
    }
    
    public int getDefaultSelectedIndex()
    {
        String str = getHost(URL_GROUP_SELECT_INDEX);
        if(str == null || str.length() == 0)
            return -1;
        
        return Integer.parseInt(str);
    }
    
    public int getHostSize()
    {
        return this.commHostStore.size();
    }
    
    public void clear()
    {
        this.commHostStore.clear();
    }

    public void store()
    {
        this.commHostStore.save();
    }

}

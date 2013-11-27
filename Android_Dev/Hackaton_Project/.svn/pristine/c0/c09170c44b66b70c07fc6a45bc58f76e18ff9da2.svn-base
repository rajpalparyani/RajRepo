/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MockStore.java
 *
 */
package com.telenav.persistent;

import java.util.Hashtable;


/**
 *@author yning
 *@date 2011-6-21
 */
/**
 * remove over write
 */
public class TnStoreMock extends TnStore
{
    protected Hashtable cache = new Hashtable();
    public TnStoreMock()
    {
        super("unit test", 1);
    }

    protected void clearDelegate()
    {
        cache.clear();
    }

    protected byte[] getDelegate(String id)
    {
        byte[] content = (byte[])cache.get(id);
        return content;
    }

    protected void loadDelegate()
    {
        
    }

    protected void putDelegate(String id, byte[] data)
    {
        cache.put(id, data);
    }

    protected void removeDelegate(String id)
    {
        cache.remove(id);
    }

    protected void saveDelegate()
    {
        
    }

}

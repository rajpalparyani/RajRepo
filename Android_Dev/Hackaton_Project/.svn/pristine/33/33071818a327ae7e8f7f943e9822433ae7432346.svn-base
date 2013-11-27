/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StringMap.java
 *
 */
package com.telenav.data.datatypes.primitive;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public class StringMap
{
    private Hashtable<String, String> table = new Hashtable<String, String>();
    
    public void put(String key, String value)
    {
        table.put(key, value);
    }
    
    public String get(String key)
    {
        return (String)table.get(key);
    }
    
    public void remove(String key)
    {
        table.remove(key);
    }
    
    public Enumeration<String> keys()
    {
        return table.keys();
    }
    
    public int size()
    {
        return table.size();
    }
    
    public void clear()
    {
        table.clear();
    }
    
    public void copy(StringMap oriMap)
    {
        if(oriMap == null || oriMap.size() == 0)
        {
            return;
        }
        
        Enumeration<String> enumeration = oriMap.keys();
        while (enumeration.hasMoreElements())
        {
            String key = enumeration.nextElement();
            this.put(key, oriMap.get(key));
        }
    }
}

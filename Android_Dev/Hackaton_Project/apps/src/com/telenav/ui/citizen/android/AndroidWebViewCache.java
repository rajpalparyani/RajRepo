/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidWebViewCache.java
 *
 */
package com.telenav.ui.citizen.android;

import java.util.Hashtable;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-3-26
 */
public class AndroidWebViewCache extends Hashtable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2848990367973757290L;

    public AndroidWebViewCache()
    {
    }
    
    public String getCacheValue(String key)
    {
        return (String) super.get(key);
    }
}

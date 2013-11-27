/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MutableImageCache.java
 *
 */
package com.telenav.searchwidget.data.cache;

import com.telenav.cache.LRUCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-2-21
 */
class MutableImageCache extends LRUCache
{

    public MutableImageCache(int maxSize)
    {
        super(maxSize);
    }
}

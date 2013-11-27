/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ImageCacheManager.java
 *
 */
package com.telenav.searchwidget.data.cache;

import com.telenav.cache.AbstractCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 23, 2010
 */
public class ImageCacheManager
{
    private static ImageCacheManager instance = new ImageCacheManager();
    
    private AbstractCache imageCache = new NormalImageCache(200);
    private AbstractCache ninePatchImageCache = new NormalImageCache(200);
    private AbstractCache mutableImageCache = new MutableImageCache(20);
    
    private ImageCacheManager()
    {
        
    }
    
    public static ImageCacheManager getInstance()
    {
        return instance;
    }
    
    public AbstractCache getImageCache()
    {
        return imageCache;
    }
    
    public AbstractCache getNinePatchImageCache()
    {
        return ninePatchImageCache;
    }
    
    public AbstractCache getMutableImageCache()
    {
        return mutableImageCache;
    }
    
    public void clear()
    {
        imageCache.clear();
        ninePatchImageCache.clear();
    }
}

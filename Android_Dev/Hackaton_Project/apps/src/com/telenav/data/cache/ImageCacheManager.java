/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ImageCacheManager.java
 *
 */
package com.telenav.data.cache;

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
    private AbstractCache miniMapCache = new NormalImageCache(20);
    
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
    
    public AbstractCache getMiniMapCache()
    {
        return miniMapCache;
    }
    
    public AbstractCache expandMiniMapCache(int maxSize)
    {
        AbstractCache old = miniMapCache;
        miniMapCache = new NormalImageCache(maxSize);
        miniMapCache.putAll(old);
        old.clear();
        return miniMapCache;
    }
    
    public void clear()
    {
        imageCache.clear();
        ninePatchImageCache.clear();
        miniMapCache.clear();
    }
    
    public void clearI18nImage()
    {
        imageCache.clear();
        ninePatchImageCache.clear();
    }
}

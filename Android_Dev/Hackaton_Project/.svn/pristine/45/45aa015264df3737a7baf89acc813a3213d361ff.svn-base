/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnMapResourceProxy.java
 *
 */
package com.telenav.map.opengl.java.proxy;

import com.telenav.tnui.graphics.AbstractTnImage;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public interface ITnMapResourceProxy
{
    public abstract class AbstractTextureImage
    {
        public int internalFormat;
        public abstract AbstractTnImage getImage(String name);
    }
    
    public void requestResource(TnMapResourceData data);
    public void cancelRequest(TnMapResourceData data);
    public AbstractTextureImage getTextureResource(String fileName);
}

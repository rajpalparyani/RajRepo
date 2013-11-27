/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapTextureLoader.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.map.opengl.java.proxy.ITnMapResourceProxy;
import com.telenav.map.opengl.java.proxy.ITnMapResourceProxy.AbstractTextureImage;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapTextureLoader
{
    TnGL10 gl10;
    
    ITnMapResourceProxy m_ResourceProxy;
    
    public TnMapTextureLoader(TnGL10 gl10, ITnMapResourceProxy resourceProxy)
    {
        this.gl10 = gl10;
        m_ResourceProxy = resourceProxy;
    }
    
    public TnMapTexture syncLoad(String fileName)
    {
        AbstractTextureImage r = m_ResourceProxy.getTextureResource(fileName);
        if (r == null)
        {
            return new TnMapTexture(gl10);
        }
        AbstractTnImage image = r.getImage(fileName);
        if (image == null)
            return null;
        return syncLoad(image, r.internalFormat);
    }
    
    public TnMapTexture syncLoad(AbstractTnImage bitmap, int internalFormat)
    {
        TnMapTexture texture = new TnMapTexture(gl10, false);
        texture.load(bitmap, bitmap.getWidth(), bitmap.getHeight(), internalFormat);
        
        return texture;
    }
    
}

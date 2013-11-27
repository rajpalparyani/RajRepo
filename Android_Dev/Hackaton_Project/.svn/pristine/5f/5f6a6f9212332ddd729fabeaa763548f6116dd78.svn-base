/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapTextureData.java
 *
 */
package com.telenav.map.opengl.java.proxy;

import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapTextureData extends TnMapResourceData
{
    TnMapTexture m_texture;

    // Internal format to use when (eventually) loading to GPU
    int m_internalFormat;

    // Texture loader
    TnMapTextureLoader m_textureLoader;
    
    public TnMapTextureData(TnMapTextureLoader loader, TnMapTexture texture, String resourceName, int internalFormat)
    {
        super(resourceName);
        m_texture = texture;
        m_internalFormat = internalFormat;
        m_textureLoader = loader;
    }

    public void setRequestResult(int result)
    {
        // If our destination texture is invalid, we have major logic problems.
        if(m_texture == null)
        {
            return;
        }

        // Request Complete
        if(result == TN_MAP_REQUEST_RESULT_COMPLETE)
        {
            // Is it a BMP?
            if((m_resource[0x00] == 0x42) && (m_resource[0x01] == 0x4d))
            {
                // Verify 1 color plane, 32bpp, no compression
                if((le_uint16(m_resource, 0x1a) == 1) &&
                   (le_uint16(m_resource, 0x1c) == 32) &&
                   ((le_uint32(m_resource, 0x1e) == 0) || (le_uint32(m_resource, 0x1e) == 3)))
                {
                    // Bitmap width and height
//                    int width = le_uint32(m_resource, 0x12);
//                    int height = le_uint32(m_resource, 0x16);
                    int offset = le_uint32(m_resource, 0x0a);
                    int size = le_uint32(m_resource, 0x22);

                    // New resource to hold texture data
                    byte[] data = new byte[size];
                    if(data != null)
                    {
                        // Swizzle and store 32 bpp
                        for(int i=0; i<size; i+=4)
                        {
                            data[i+0] = m_resource[offset+i+2];
                            data[i+1] = m_resource[offset+i+1];
                            data[i+2] = m_resource[offset+i+0];
                            data[i+3] = m_resource[offset+i+3];
                        }

                        // Enqueue texture load
//                        m_textureLoader.EnqueueLoad(m_texture, data, width, height, GL11.GL_RGBA, m_internalFormat);
                    }
                    else
                    {
                        // @TODO: Failed to allocate raw data buffer
                    }
                }
                else
                {
                    // @TODO: BMP good, but parameters we don't support
                }
            }
            else
            {
                // @TODO: Not a BMP
            }
        }
    }
    
    int le_uint16(byte[] buffer, int startIndex)
    {
        return Math.abs((int)(buffer[startIndex])) + Math.abs((int)((buffer[startIndex + 1]) << 8));
    }

    int le_uint32(byte[] buffer, int startIndex)
    {
        return Math.abs((int) (buffer[startIndex])) + Math.abs(((int) (buffer[startIndex + 1]) << 8))
                + Math.abs(((int) (buffer[startIndex + 2]) << 16)) + Math.abs(((int) (buffer[startIndex + 3]) << 24));
    }
}

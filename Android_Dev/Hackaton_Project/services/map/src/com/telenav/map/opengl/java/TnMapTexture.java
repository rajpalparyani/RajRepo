/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapTexture.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.telenav.nio.TnNioManager;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL10;
import com.telenav.tnui.opengles.TnGLUtils;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapTexture
{
    private int m_Size;

    // Whether or not we need to generate mipmap
    private boolean m_GenerateMip;

    // The texture name from OpenGL
    private int[] m_Texture = new int[1];

    // The texture target, usually GL_TEXTURE_2D
    private int m_Target;

    // Alternate texture matrix for non-power-of-two textures
//    private float[] m_Matrix;
    
    private TnGL10 gl10;
    
    public TnMapTexture(TnGL10 gl10)
    {
        this(gl10, false, TnGL10.GL_TEXTURE_2D);
    }
    
    public TnMapTexture(TnGL10 gl10, boolean mipmap)
    {
        this(gl10, mipmap, TnGL10.GL_TEXTURE_2D);
    }
    
    public TnMapTexture(TnGL10 gl10, boolean mipmap, int target)
    {
        this.gl10 = gl10;
        this.m_Size = 0;
        this.m_GenerateMip = mipmap;
        this.m_Texture[0] = 0;
        this.m_Target = target;
    }
    
    public void load(AbstractTnImage bitmap, int width, int height, int internalFormat)
    {
        // Generate the texture handle
        this.gl10.glGenTextures(1, m_Texture, 0);

        // Bind to initialize
        this.gl10.glBindTexture(this.m_Target, m_Texture[0]);
        this.gl10.glEnable(m_Target);

        gl10.glTexParameterf(m_Target, TnGL10.GL_TEXTURE_WRAP_S, TnGL10.GL_CLAMP_TO_EDGE);
        gl10.glTexParameterf(m_Target, TnGL10.GL_TEXTURE_WRAP_T, TnGL10.GL_CLAMP_TO_EDGE);
        gl10.glTexParameterf(m_Target, TnGL10.GL_TEXTURE_MIN_FILTER, m_GenerateMip ? TnGL10.GL_LINEAR_MIPMAP_LINEAR : TnGL10.GL_LINEAR);
        gl10.glTexParameterf(m_Target, TnGL10.GL_TEXTURE_MAG_FILTER, TnGL10.GL_LINEAR);
        
        // Actual texture width/height
        int actualWidth = nextpo2(width);
        int actualHeight = nextpo2(height);

        // Force power of two texture
        AbstractTnImage oldImg = bitmap;
        
        bitmap = TnGLUtils.getInstance().scaleBitmap(bitmap, actualWidth, actualHeight, internalFormat);
        
        if (oldImg != bitmap)
            oldImg.release();
        
        // Upload texture
        TnGLUtils.getInstance().texImage2D(this.gl10, m_Target, 0, bitmap, internalFormat, 0);
    
        this.gl10.glDisable(m_Target);
        this.gl10.glBindTexture(m_Target, 0);

//        if((width != actualWidth) || (height != actualHeight))
//        {
//            // Build up alternate texture matrix
//            m_Matrix = new float[16];
//            m_Matrix[0] = (float)width / (float) actualWidth;
//            m_Matrix[5] = (float)height / (float) actualHeight;
//            m_Matrix[10] = 1.0f;
//            m_Matrix[15] = 1.0f;
//        }

        // Calculate size: mipmaps add 1/3 to level 0 size
        m_Size = sizeOf(actualWidth, actualHeight, internalFormat, m_GenerateMip);
    }
    
    // Return texture memory footprint
    public int size()
    {
        return m_Size;
    }

    // Returns whether or not any data has been loaded into the texture
    public boolean isLoaded()
    {
        return m_Size > 0;
    }

    public void startUsing()
    {
        // Use the texture: Bind it and enable the target
        this.gl10.glBindTexture(m_Target, m_Texture[0]);
        this.gl10.glEnable(m_Target);

        // For non power of two textures
//        if(m_Matrix != null)
//        {
//            this.gl10.glMatrixMode(TnGL10.GL_TEXTURE);
//            this.gl10.glPushMatrix();
//            this.gl10.glLoadMatrixf(m_Matrix, 0);
//            this.gl10.glMatrixMode(TnGL10.GL_MODELVIEW);
//        }
    }

    public void stopUsing()
    {
        // For non power of two textures
//        if(m_Matrix != null)
//        {
//            this.gl10.glMatrixMode(TnGL10.GL_TEXTURE);
//            this.gl10.glPopMatrix();
//            this.gl10.glMatrixMode(TnGL10.GL_MODELVIEW);
//        }

        // Don't use the texture: Unbind it and disable the target
        this.gl10.glDisable(m_Target);
        this.gl10.glBindTexture(m_Target, 0);
    }

    public void destroy()
    {
//        System.out.println("*****TnMapTexture destroy-----");
        this.gl10.glDeleteTextures(1, m_Texture, 0);
    }
    
    public int sizeOf(int width, int height, int internalFormat, boolean mipmap)
    {
        // Bits per pixel
        int bpp = 32;

        switch(internalFormat)
        {
        case 1:
        case TnGL10.GL_ALPHA:
        case TnGL10.GL_LUMINANCE:
            bpp = 8;
            break;
        case 2:
        case TnGL10.GL_LUMINANCE_ALPHA:
            bpp = 16;
            break;
        case 3:
        case TnGL10.GL_RGB:
            bpp = 24;
            break;
        case 4:
        case TnGL10.GL_RGBA:
            bpp = 32;
            break;
        default:
            // @TODO: Handle unknown internalFormat
            break;
        }

        if(mipmap)
        {
            // Size of mipmap ~ size of base texture * 4/3
            return width * height * bpp * 4 / 24;
        }
        else
        {
            return width * height * bpp / 8;
        }
    }
    
    private static int nextpo2(int k)
    {
        //TODO sizeof
        k--;
        for (int i = 1; i < 4 * 8; i <<= 1)
        {
            k = k | k >> i;
        }
        return k + 1;
    }
}

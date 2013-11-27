/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimImage.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.util.Arrays;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * Encapsulates an image graphic usable for display on the device at rim platform. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-10
 */
class RimImage extends AbstractTnImage
{
    protected Bitmap bitmap;
    protected RimGraphics androidGraphics;
    
    public RimImage(int[] pixels, int width, int height)
    {
        bitmap = createBitmap(null, null, pixels, width, height, -1, -1);
    }

    public RimImage(int width, int height)
    {
        bitmap = createBitmap(null, null, null, width, height, -1, -1);
    }
    
    public RimImage(Object nativeImage)
    {
        bitmap = (Bitmap)nativeImage;
    }

    public RimImage(byte[] data)
    {
        bitmap = createBitmap(null, data, null, -1, -1, -1, -1);
    }
    
    public RimImage(AbstractTnImage image, int x, int y, int width, int height)
    {
        if(image.getNativeImage() != null)
        {
            bitmap = createBitmap((Bitmap)image.getNativeImage(), null, null, width, height, x, y);
        }
    }
    
    public AbstractTnGraphics getGraphics()
    {
        if(androidGraphics == null)
        {
            androidGraphics = new RimGraphics();
            androidGraphics.setGraphics(Graphics.create(bitmap));
        }
        
        return androidGraphics;
    }

    public int getHeight()
    {
        return bitmap == null ? 0 : bitmap.getHeight();
    }

    public Object getNativeImage()
    {
        return bitmap;
    }

    public int getWidth()
    {
        return bitmap == null ? 0 : bitmap.getWidth();
    }

    public void clear(int color)
    {
        if(bitmap != null)
        {
            int[] argb = new int[bitmap.getWidth() * bitmap.getHeight()];
            Arrays.fill(argb, 0x00FFFFFF);//use RIM native API by fqming
            bitmap.setARGB(argb, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
    }
    
    public boolean isMutable()
    {
        return bitmap == null ? false : bitmap.isWritable();
    }
    
    public void getARGB(int[] argbData, int offset, int scanLength, int x, int y, int width, int height)
    {
        if(bitmap == null)
            return;
        
        bitmap.getARGB(argbData, offset, scanLength, x, y, width, height);
    }
    
    private Bitmap createBitmap(Bitmap srcBitmap, byte[] data, int[] pixels, int width, int height, int x, int y)
    {
        Bitmap bitmap = null;

        if(srcBitmap != null)
        {
            bitmap = new Bitmap(width, height);
            int[] argb = new int[bitmap.getWidth() * bitmap.getHeight()];
            srcBitmap.getARGB(argb, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            bitmap.setARGB(argb, 0, width, 0, 0, width, height);
        }
        else if (data != null)
        {
            bitmap = Bitmap.createBitmapFromPNG(data, 0, data.length);
        }
        else if (pixels != null)
        {
            bitmap = new Bitmap(width, height);
            bitmap.setARGB(pixels, 0, width, 0, 0, width, height);
        }
        else if(width > 0 && height > 0)
        {
            bitmap = new Bitmap(width, height);
        }

        return bitmap;
    }
    
    public AbstractTnImage createScaledImage(int dstWidth, int dstHeight)
    {
        Bitmap dstBitmap = new Bitmap(dstWidth, dstHeight);
        int[] alphaC = new int[dstWidth * dstHeight];
        dstBitmap.setARGB(alphaC, 0, dstWidth, 0, 0, dstWidth, dstHeight);
        
        bitmap.scaleInto(dstBitmap, Bitmap.FILTER_BOX);
        
        return new RimImage(dstBitmap);
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seImage.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.telenav.logger.Logger;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 3, 2010
 */
class J2seImage extends AbstractTnImage
{
    private BufferedImage bitmap;
    private J2seGraphics j2seGraphics;
    
    public J2seImage(int[] pixels, int width, int height)
    {
        bitmap = createBitmap(null, null, pixels, width, height, -1, -1);
    }

    public J2seImage(int width, int height)
    {
        bitmap = createBitmap(null, null, null, width, height, -1, -1);
    }
    
    public J2seImage(Object nativeImage)
    {
        bitmap = (BufferedImage)nativeImage;
    }

    public J2seImage(byte[] data)
    {
        bitmap = createBitmap(null, data, null, -1, -1, -1, -1);
    }
    
    public J2seImage(AbstractTnImage image, int x, int y, int width, int height)
    {
        if(image.getNativeImage() != null)
        {
            bitmap = createBitmap((BufferedImage)image.getNativeImage(), null, null, width, height, x, y);
        }
    }
    
    public void clear(int color)
    {
        this.getGraphics().setColor(color);
        this.getGraphics().fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public AbstractTnGraphics getGraphics()
    {
        if(j2seGraphics == null)
        {
            j2seGraphics = new J2seGraphics((Graphics2D)this.bitmap.getGraphics());
        }
        
        return j2seGraphics;
    }

    public int getHeight()
    {
        return bitmap == null ? 0 : bitmap.getHeight();
    }

    public int getWidth()
    {
        return bitmap == null ? 0 : bitmap.getWidth();
    }
    
    public Object getNativeImage()
    {
        return this.bitmap;
    }

    public boolean isMutable()
    {
        return true;
    }

    private BufferedImage createBitmap(BufferedImage srcBitmap, byte[] data, int[] pixels, int width, int height, int x, int y)
    {
        BufferedImage image = null;

        if(srcBitmap != null)
        {
            image = srcBitmap.getSubimage(x, y, width, height);
        }
        else if (data != null)
        {
            InputStream in = null;
            try
            {
                in = new ByteArrayInputStream(data);
                image = ImageIO.read(in);
            }
            catch (IOException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            finally
            {
                try
                {
                    if(in != null)
                    {
                        in.close();
                    }
                }
                catch (IOException e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                finally
                {
                    in = null;
                }
            }
        }
        else if (pixels != null)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image.setRGB(0, 0, width, height, pixels, 0, width);
        }
        else if(width > 0 && height > 0)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        return image;
    }

    public void getARGB(int[] argbData, int offset, int scanLength, int x, int y, int width, int height)
    {
        if(bitmap == null)
            return;
        
        bitmap.getRGB(x, y, width, height, argbData, offset, scanLength);
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidImage.java
 *
 */
package com.telenav.tnui.core.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * Encapsulates an image graphic usable for display on the device at android platform. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
class AndroidImage extends AbstractTnImage
{
    protected Bitmap bitmap;
    protected AndroidGraphics androidGraphics;
    
    public AndroidImage(int[] pixels, int width, int height)
    {
        bitmap = createBitmap(null, null, pixels, width, height, -1, -1);
    }

    public AndroidImage(int width, int height)
    {
        bitmap = createBitmap(null, null, null, width, height, -1, -1);
    }

    public AndroidImage(Object nativeBitmap)
    {
        bitmap = (Bitmap)nativeBitmap;
    }
    
    public AndroidImage(byte[] data)
    {
        bitmap = createBitmap(null, data, null, -1, -1, -1, -1);
    }
    
    public AndroidImage(AbstractTnImage image, int x, int y, int width, int height)
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
            androidGraphics = new AndroidGraphics();
            androidGraphics.setGraphics(new Canvas(bitmap));
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
            bitmap.eraseColor(color);
        }
    }
    
    public boolean isMutable()
    {
        return bitmap == null ? false : bitmap.isMutable();
    }
    
    public void getARGB(int[] argbData, int offset, int scanLength, int x, int y, int width, int height)
    {
        if(bitmap == null)
            return;
        
        bitmap.getPixels(argbData, offset, scanLength, x, y, width, height);
    }
    
    private Bitmap createBitmap(Bitmap srcBitmap, byte[] data, int[] pixels, int width, int height, int x, int y)
    {
        Bitmap bitmap = null;

        if(srcBitmap != null)
        {
            bitmap = Bitmap.createBitmap(srcBitmap, x, y, width, height);
        }
        else if (data != null)
        {
            if (Integer.parseInt(Build.VERSION.SDK) >= 4)
            {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, AndroidBitmapOptions.getOptions());
            }
            else
            {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        }
        else if (pixels != null)
        {
            bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        }
        else if(width > 0 && height > 0)
        {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        return bitmap;
    }
    
    public AbstractTnImage createScaledImage(int dstWidth, int dstHeight)
    {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.bitmap, dstWidth, dstHeight, true);
        
        return new AndroidImage(scaledBitmap);
    }

	public void release()
	{
		bitmap.recycle();
	}
	
    /**
     * Retrieve if the image is released.
     * 
     * @return boolean
     */
    public boolean isRelease()
    {
        if(bitmap == null)
        {
            return true;
        }
        
        return bitmap.isRecycled();
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimGLUtils.java
 *
 */
package com.telenav.tnui.widget.rim;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import net.rim.device.api.opengles.GLUtils;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.util.MathUtilities;

import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL;
import com.telenav.tnui.opengles.TnGL10;
import com.telenav.tnui.opengles.TnGLUtils;

class RimGLUtils extends TnGLUtils
{
    public RimGLUtils()
    {
        
    }
    
    public void texImage2D(TnGL gl, int target, int level, AbstractTnImage image, int internalFormat, int border)
    {
        GLUtils.glTexImage2D((GL)gl.getNativeGL(), level, internalFormat, GL10.GL_UNSIGNED_SHORT_4_4_4_4, (Bitmap)image.getNativeImage(), null);
    }

    public double acos(double d)
    {
        return MathUtilities.acos(d);
    }

    public double atan(double d)
    {
        return MathUtilities.atan(d);
    }

    public double atan2(double d1, double d2)
    {
        return MathUtilities.atan2(d1, d2);
    }

    public double exp(double d)
    {
        return MathUtilities.exp(d);
    }

    public double log(double d)
    {
        return MathUtilities.log(d);
    }

    public double log10(double d)
    {
        return MathUtilities.log(d)/MathUtilities.log(10);
    }

    public double pow(double d1, double d2)
    {
        return MathUtilities.pow(d1, d2);
    }

    public AbstractTnImage scaleBitmap(AbstractTnImage image, int width, int height, int internalFormat)
    {
        switch(internalFormat)
        {
            case TnGL10.GL_ALPHA:
                break;
            case TnGL10.GL_RGB:
                break;
            case TnGL10.GL_RGBA:
                break;
            default:
        }
        Bitmap bitmap2 = new Bitmap(width, height);
        Bitmap bitmap = (Bitmap)image.getNativeImage();
        bitmap.scaleInto(0, 0, bitmap.getWidth(), bitmap.getHeight(), bitmap2, 0, 0, width, height, Bitmap.FILTER_BILINEAR);
        
        return AbstractTnGraphicsHelper.getInstance().createImage(bitmap2);
    }

}


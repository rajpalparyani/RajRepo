package com.telenav.tnui.widget.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLUtils;

import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL;
import com.telenav.tnui.opengles.TnGL10;
import com.telenav.tnui.opengles.TnGLUtils;

class AndroidGLUtils extends TnGLUtils
{
    public AndroidGLUtils()
    {
    }
    
    public void texImage2D(TnGL gl, int target, int level, AbstractTnImage image, int internalFormat, int border)
    {
        GLUtils.texImage2D(target, level, internalFormat, (Bitmap)image.getNativeImage(), border);
    }

    public void recycleBitmap(Object bitmap)
    {
        ((Bitmap)bitmap).recycle();
    }

    public double acos(double d)
    {
        return Math.acos(d);
    }

    public double atan(double d)
    {
        return Math.atan(d);
    }

    public double atan2(double d1, double d2)
    {
        return Math.atan2(d1, d2);
    }

    public double exp(double d)
    {
        return Math.exp(d);
    }

    public double log(double d)
    {
        return Math.log(d);
    }

    public double log10(double d)
    {
        return Math.log10(d);
    }

    public double pow(double d1, double d2)
    {
        return Math.pow(d1, d2);
    }

    public AbstractTnImage scaleBitmap(AbstractTnImage image, int width, int height, int internalFormat)
    {
        Bitmap.Config config;
        switch(internalFormat)
        {
            case TnGL10.GL_ALPHA:
                config = Bitmap.Config.ALPHA_8;
                break;
            case TnGL10.GL_RGB:
                config = Bitmap.Config.RGB_565;
                break;
            case TnGL10.GL_RGBA:
                config = Bitmap.Config.ARGB_8888;
                break;
            default:
                config = Bitmap.Config.ARGB_8888;
        }
        Bitmap bitmap2 = Bitmap.createBitmap(width, height, config);
        Canvas c = new Canvas(bitmap2);
        Bitmap bitmap = (Bitmap)image.getNativeImage();
        c.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(0, 0, width, height), null);
        
        return AbstractTnGraphicsHelper.getInstance().createImage(bitmap2);
    }

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidBitmapDrawable.java
 *
 */
package com.telenav.tnui.core.android;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class AndroidBitmapDrawable extends BitmapDrawable
{
    protected AbstractTnImage image = null;

    protected AndroidGraphics graphics = null;
    
    public AndroidBitmapDrawable(AbstractTnImage image)
    {
        super();
        
        this.graphics = new AndroidGraphics();
        
        this.image = image;
    }

    public void draw(Canvas canvas)
    {
        graphics.setGraphics(canvas);

        if(image.getDrawable() == null)
        {
            Rect bound = this.getBounds();
            TnRect oriRect = new TnRect(0, 0, image.getWidth(), image.getHeight());
            TnRect dstRect = new TnRect(bound.left, bound.top, bound.right, bound.bottom);
            graphics.drawImage(image, oriRect, dstRect);
        }
        else
        {
            image.setWidth(this.getBounds().width());
            image.setHeight(this.getBounds().height());
            image.getDrawable().draw(graphics);
        }
    }

    public int getOpacity()
    {
        return 0;
    }

    public void setAlpha(int alpha)
    {

    }

    public void setColorFilter(ColorFilter cf)
    {

    }

    public void setBounds(int left, int top, int right, int bottom)
    {
        super.setBounds(left, top, right, bottom);
        if (image != null && image.getDrawable() != null)
        {
            image.getDrawable().setBounds(new TnRect(left, top, right, bottom));
        }
    }

    public void setBounds(Rect bounds)
    {
        setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }
}

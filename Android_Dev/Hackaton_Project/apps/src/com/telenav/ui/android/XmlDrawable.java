/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * XmlDrawable.java
 *
 */
package com.telenav.ui.android;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnDrawable;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 *@author bduan
 *@date Oct 31, 2012
 */
public class XmlDrawable extends TnDrawable
{
    private Drawable nativeDrawable;
    
    public XmlDrawable(int resID)
    {
        nativeDrawable = AndroidCitizenUiHelper.inflateDrawable(resID);
    }

    public void setBounds(TnRect bounds)
    {
        if(bounds != null)
        {
            nativeDrawable.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
        }
    }
    
    @Override
    public void draw(AbstractTnGraphics g)
    {
        if(nativeDrawable != null)
        {
            Canvas canvas = (Canvas)g.getNativeGraphics();
            if(canvas != null)
            {
                nativeDrawable.draw(canvas);
            }
        }
    }
    
    public Drawable getNativeDrawable()
    {
        return nativeDrawable;
    }

}

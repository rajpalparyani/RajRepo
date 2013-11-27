/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ImageDrawable.java
 *
 */
package com.telenav.ui.android;

import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.android.AndroidBitmapDrawable;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * @author bduan
 * @date Oct 23, 2012
 */
public class AssetsImageDrawable extends AndroidBitmapDrawable
{
    public AssetsImageDrawable(TnUiArgAdapter adapter)
    {
        super(adapter == null ? null : adapter.getImage());
    }

    public AssetsImageDrawable(AbstractTnImage image)
    {
        super(image);
    }
    
    public int getIntrinsicWidth()
    {
        return this.image == null ? 0 : this.image.getWidth();
    }
    
    public int getIntrinsicHeight()
    {
        return this.image == null ? 0 : this.image.getHeight();
    }
    
    public Object getNativeBitmap()
    {
        if (image != null)
            return image.getNativeImage();
        else
            return null;
    }
}

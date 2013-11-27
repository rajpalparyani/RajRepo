/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WidgetUtil.java
 *
 */
package com.telenav.searchwidget.util.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Aug 18, 2011
 */

public class WidgetUtil
{
    public static Bitmap combineBitmap(Bitmap mapImage, Bitmap trafficImage)
    {
        if (mapImage == null || trafficImage == null)
            return mapImage;
        
        int w = mapImage.getWidth();
        int h = mapImage.getHeight();
        
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint paint = new Paint();
        c.drawBitmap(mapImage, 0, 0, paint);
        c.drawBitmap(trafficImage, 0, 0, paint);
        
        return bm;
    }

}

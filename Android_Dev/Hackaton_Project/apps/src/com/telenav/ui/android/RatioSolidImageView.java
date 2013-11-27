/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * SquareImageView.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *@author bduan
 *@date 2012-11-13
 */
public class RatioSolidImageView extends ImageView
{
    protected int width = 0;
    protected int height = 0;
    
    protected boolean height_based = true;

    public RatioSolidImageView(Context context)
    {
        super(context);
    }
    
    public RatioSolidImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public void setIsHeightBased(boolean height_based)
    {
        this.height_based = height_based;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mh = this.getMeasuredHeight();
        int mw = this.getMeasuredWidth();
        
        if(width > 0 && height > 0)
        {
            if(height_based)
            {
                mw = mh * width / height;
            }
            else
            {
                mh = mw * height / width;
            }
        }
        else //if no ratio designated, use square.
        {
            if(mh > mw)
            { 
                mh = mw;
            }
            else
            {
                mw = mh;
            }
        }
        
        setMeasuredDimension(mw, mh);
    }
    
    public void setBackgroundDrawable(Drawable drawable)
    {
        super.setBackgroundDrawable(drawable);
        this.width = drawable.getIntrinsicWidth();
        this.height = drawable.getIntrinsicHeight();
    }
    
}

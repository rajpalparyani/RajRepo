package com.telenav.ui.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

public class ImageIndicator extends LinearLayout
{
    private Drawable unfocusId;
    private Drawable focusId;
    private boolean isInit = false;

    public ImageIndicator(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void initView(int length, Drawable unfocusImageResId, Drawable focusImageResId)
    {
        initImageViews(length, unfocusImageResId);
        this.unfocusId = unfocusImageResId;
        this.focusId = focusImageResId;
        isInit = true;
    }

    public void setSelection(int index)
    {
        if(!isInit)
        {
            throw new RuntimeException("Image indicator has not been init, please call initView firstly");
        }
        
        int size = this.getChildCount();
        for (int start = 0; start < size; start++)
        {
            if (start != index)
            {
                ((ImageView) this.getChildAt(start)).setBackgroundDrawable(unfocusId);
            }
            else
            {
                ((ImageView) this.getChildAt(start)).setBackgroundDrawable(focusId);
            }
        }
    }
    
    private void initImageViews(int length, Drawable resId)
    {
        int hpixel = AndroidCitizenUiHelper.getPixelsByDip(10);
        int wpixel = AndroidCitizenUiHelper.getPixelsByDip(10);
        for (int i = 0; i < length; i++)
        {
            ImageView imageView = new ImageView(this.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setBackgroundDrawable(resId);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wpixel, hpixel);
            params.setMargins(wpixel/2, 0, wpixel/2, 0);
            this.addView(imageView, params);
        }
    }
    
    public void destroy()
    {
        if (unfocusId instanceof AssetsImageDrawable)
        {
            recycle(unfocusId);
        }
        
        if (focusId instanceof AssetsImageDrawable)
        {
            recycle(focusId);
        }
    }

    private void recycle(Drawable drawble)
    {
        AssetsImageDrawable bitmapDrawable = (AssetsImageDrawable) drawble;
        Object bitmap = bitmapDrawable.getNativeBitmap();
        if (bitmap instanceof Bitmap)
            ((Bitmap) bitmap).recycle();
    }
}
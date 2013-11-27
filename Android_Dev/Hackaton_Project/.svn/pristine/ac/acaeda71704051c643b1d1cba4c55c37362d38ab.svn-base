/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * TnTextView.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;

/**
 * @author pwang
 * @date 2013-3-30
 */
public class TnTextView extends TextView
{
    public static final float MIN_TEXT_SIZE = 10;

    private static final float SPACING_MULT = 1.0f;
    
    private static final float SPACING_ADD = 0.0f;
    
    private boolean needsResize = false;

    private float textSize;

    private float minTextSize = MIN_TEXT_SIZE;

    private boolean autoResizeEnable = false;

    public TnTextView(Context context)
    {
        this(context, null);
    }

    public TnTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TnTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        // Parse internal attributes;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TnTextView);
        boolean b = a.getBoolean(R.styleable.TnTextView_autoResize, false);
        setAutoResizeEnable(b);
        int min = a.getInt(R.styleable.TnTextView_minTextSize, 0);
        setMinTextSize(min);
        a.recycle();

        textSize = getTextSize();
    }

    private void setAutoResizeEnable(boolean autoResizeEnable)
    {
        this.autoResizeEnable = autoResizeEnable;
    }

    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after)
    {
        needsResize = true;
        resetTextSize();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (w != oldw || h != oldh)
        {
            needsResize = true;
        }
    }

    @Override
    public void setTextSize(float size)
    {
        super.setTextSize(size);
        textSize = getTextSize();
    }

    @Override
    public void setTextSize(int unit, float size)
    {
        super.setTextSize(unit, size);
        textSize = getTextSize();
    }



    public void setMinTextSize(float minTextSize)
    {
        this.minTextSize = minTextSize;
        requestLayout();
        invalidate();
    }

    public float getMinTextSize()
    {
        return minTextSize;
    }

    public void resetTextSize()
    {
        if (textSize > 0)
        {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if (autoResizeEnable && (changed || needsResize))
        {
            int widthLimit = (right - left) - getCompoundPaddingLeft() - getCompoundPaddingRight();
            int heightLimit = (bottom - top) - getCompoundPaddingBottom() - getCompoundPaddingTop();
            resizeText(widthLimit, heightLimit);
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    public void resizeText(int width, int height)
    {
        String text = getText().toString();
        if (text == null || text.length() == 0 || height <= 0 || width <= 0 || textSize == 0)
        {
            return;
        }

        TextPaint textPaint = getPaint();

        float oldTextSize = textSize;
        textPaint.setTextSize(oldTextSize);
        int size = (int)oldTextSize;
        float w = textPaint.measureText(text);
        while(w > width && size > minTextSize)
        {
            size --;
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            w = textPaint.measureText(text);
        }
        setLineSpacing(SPACING_ADD, SPACING_MULT);
        
        needsResize = false;
    }

    

}

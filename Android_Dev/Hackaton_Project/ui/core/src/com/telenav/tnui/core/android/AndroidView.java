/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidView.java
 *
 */
package com.telenav.tnui.core.android;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiEvent;

/**
 * This class represents the basic building block for user interface components. A View
 * occupies a rectangular area on the screen and is responsible for drawing and
 * event handling. View is the base class for <em>widgets</em>, which are
 * used to create interactive UI components (buttons, text fields, etc.). The
 * {@link android.view.ViewGroup} subclass is the base class for <em>layouts</em>, which
 * are invisible containers that hold other Views (or other ViewGroups) and define
 * their layout properties.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-2
 */
public class AndroidView extends View implements INativeUiComponent, View.OnClickListener, View.OnLongClickListener
{
    protected AbstractTnComponent tnComponent;
    
    public AndroidView(Context context, AbstractTnComponent tnComponent)
    {
        super(context);
        
        this.tnComponent = tnComponent;
        
        this.setOnLongClickListener(this);
        this.setOnClickListener(this);
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        this.tnComponent.sublayout(widthSize, heightSize);
        
        int prefWidth = this.tnComponent.getPreferredWidth();
        int prefHeight = this.tnComponent.getPreferredHeight();
        
        if (prefWidth < 0 || (prefWidth > widthSize && (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY)))
        {
            prefWidth = widthSize;
        }
        
        if (prefHeight < 0  || (prefHeight > heightSize && (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY)))
        {
            prefHeight = heightSize;
        }
        
        setMeasuredDimension(prefWidth, prefHeight);
    }
    
    public void setSelected(boolean selected)
    {
        super.setSelected(selected);
        dispatchSetSelected(selected);
    }
    
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        
        boolean gainFocus = this.isFocused() || this.isPressed();
        if(this.tnComponent.isFocused() != gainFocus)
        {
            this.tnComponent.dispatchFocusChanged(gainFocus);
            invalidate();
        }
    }
    
    protected void dispatchSetSelected(boolean selected) 
    {
        super.dispatchSetSelected(selected);
        
        if(selected != tnComponent.isFocused())
            this.tnComponent.dispatchFocusChanged(selected);
    }
    
    protected void onDraw(Canvas canvas)
    {
        AndroidGraphics.getInstance().setGraphics(canvas);
        
        canvas.save();
        
        tnComponent.draw(AndroidGraphics.getInstance());
        
        canvas.restore();
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;
        
        return null;
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean isNativeVisible()
    {
        return this.getVisibility() == VISIBLE;
    }

    public boolean requestNativeFocus()
    {
        return this.requestFocus();
    }

    public void requestNativePaint()
    {
        this.postInvalidate();
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public void setNativeVisible(boolean isVisible)
    {
        this.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public int getNativeHeight()
    {
        return this.getHeight();
    }

    public int getNativeWidth()
    {
        return this.getWidth();
    }

    public int getNativeX()
    {
        return this.getLeft();
    }

    public int getNativeY()
    {
        return this.getTop();
    }
    
    public AbstractTnComponent getTnUiComponent()
    {
        return this.tnComponent;
    }

    public void onClick(View v)
    {
        if(this.tnComponent.isFocused() != false)
        {
            postDelayed(new Runnable()
            {
              
                public void run()
                {
                    tnComponent.dispatchFocusChanged(false);
                    invalidate();
                }
            } , 100);
        }
        
        AndroidUiEventHandler.onClick(this.tnComponent);
    }
    
    public boolean onLongClick(View v)
    {
        return AndroidUiEventHandler.onLongClick(this.tnComponent);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyDown(this.tnComponent, keyCode, event);
        
        return isHandled ? true : super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyUp(this.tnComponent, keyCode, event);
        
        return isHandled ? true : super.onKeyUp(keyCode, event);
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTouch(this.tnComponent, event);
        
        return isHandled ? true : super.onTouchEvent(event);
    }
    
    public boolean onTrackballEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTrackball(this.tnComponent, event);
        
        return isHandled ? true : super.onTrackballEvent(event);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        this.tnComponent.dispatchSizeChanged(w, h, oldw, oldh);
    }
    
    public void createContextMenu(ContextMenu menu)
    {
        super.createContextMenu(menu);
        
        AndroidUiEventHandler.onCreateContextMenu(this.tnComponent, menu);
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        
        this.tnComponent.dispatchDisplayChanged(true);
    }
    
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        
        this.tnComponent.dispatchDisplayChanged(false);
    }
}

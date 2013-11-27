/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidHorizontalScrollPanel.java
 *
 */
package com.telenav.tnui.widget.android;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnScrollPanel;

/**
 * Layout container for a view hierarchy that can be scrolled by the user,
 * allowing it to be larger than the physical display.  A HorizontalScrollView
 * is a {@link FrameLayout}, meaning you should place one child in it
 * containing the entire contents to scroll; this child may itself be a layout
 * manager with a complex hierarchy of objects.  A child that is often used
 * is a {@link LinearLayout} in a horizontal orientation, presenting a horizontal
 * array of top-level items that the user can scroll through.
 * 
 *@author fqming
 *@date 2010-6-19
 */
class AndroidHorizontalScrollPanel extends HorizontalScrollView implements INativeUiComponent
{
    protected TnScrollPanel tnComponent;
    public AndroidHorizontalScrollPanel(Context context, TnScrollPanel tnComponent)
    {
        super(context);
        
        this.tnComponent = tnComponent;
        this.setSmoothScrollingEnabled(true);
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int prefWidth = this.tnComponent.getPreferredWidth();
        int prefHeight = this.tnComponent.getPreferredHeight();
        
        if (prefWidth <= 0 && prefHeight <= 0)
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else
        {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(prefWidth, widthMode), MeasureSpec.makeMeasureSpec(prefHeight, heightMode));
            setMeasuredDimension(prefWidth, prefHeight);
        }
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                AndroidUiEventHandler.onTouch(this.tnComponent, event);
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                AndroidUiEventHandler.onTouch(this.tnComponent, event);
               
                break;
            }
        }
        return super.onTouchEvent(event);
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        this.tnComponent.dispatchSizeChanged(w, h, oldw, oldh);
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
    
    protected void onDraw(Canvas canvas)
    {
        AbstractTnGraphics.getInstance().setGraphics(canvas);
        
        canvas.save();
        
        tnComponent.draw(AbstractTnGraphics.getInstance());
        
        canvas.restore();
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;

        switch (eventMethod)
        {
            case TnScrollPanel.METHOD_GET:
            {
                View view = this.getChildAt(0);
                if (view instanceof INativeUiComponent)
                {
                    return ((INativeUiComponent) view).getTnUiComponent();
                }
                break;
            }
            case TnScrollPanel.METHOD_SET:
            {
                if (this.getChildCount() > 0)
                {
                    this.removeAllViews();
                }
                AbstractTnComponent uiComponent = (AbstractTnComponent) args[0];
                this.addView((View) uiComponent.getNativeUiComponent(), new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
                break;
            }
            case TnScrollPanel.METHOD_REMOVE:
            {
                this.removeAllViews();
                break;
            }
            case TnScrollPanel.METHOD_SET_SCROLLBAR_VISIBLE:
            {
                if(args[0] instanceof Boolean)
                {
                    boolean isVisible = ((Boolean)args[0]).booleanValue();
                    this.setHorizontalScrollBarEnabled(isVisible);
                }
                break;
            }
            case TnScrollPanel.METHOD_SCROLL_TO:
            {
                this.scrollTo(((Integer)args[0]).intValue(), ((Integer)args[1]).intValue());
                break;
            }
            case TnScrollPanel.METHOD_SMOOTH_SCROLL_TO:
            {
                this.smoothScrollTo(((Integer)args[0]).intValue(), ((Integer)args[1]).intValue());
                break;
            }
            case TnScrollPanel.METHOD_SET_HORIZONTAL_FADING_EDGE_ENABLED:
            {
                if(args[0] instanceof Boolean)
                {
                    boolean horizontalFadingEdgeEnabled = ((Boolean)args[0]).booleanValue();
                    this.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
                }
                break;
            }
        }

        return null;
    }
   
    public void fling(int velocityX) 
    {
        TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_TOUCH_EVENT, tnComponent);
        TnMotionEvent motionEvent = new TnMotionEvent(TnMotionEvent.ACTION_MOVE);
        tnUiEvent.setMotionEvent(motionEvent);
        motionEvent.setLocation(velocityX, 0);
        boolean isHandled = tnComponent.dispatchUiEvent(tnUiEvent);
        if(!isHandled) 
        {
            super.fling(velocityX);
        }
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
    
}

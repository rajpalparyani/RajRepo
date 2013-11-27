/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidVerticalScrollPanel.java
 *
 */
package com.telenav.tnui.widget.android;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnScrollPanel;

/**
 * Layout container for a view hierarchy that can be scrolled by the user,
 * allowing it to be larger than the physical display.  A ScrollView
 * is a {@link FrameLayout}, meaning you should place one child in it
 * containing the entire contents to scroll; this child may itself be a layout
 * manager with a complex hierarchy of objects.  A child that is often used
 * is a {@link LinearLayout} in a vertical orientation, presenting a vertical
 * array of top-level items that the user can scroll through.
 * 
 *@author fqming
 *@date 2010-6-19
 */
class AndroidVerticalScrollPanel extends ScrollView implements INativeUiComponent
{
    protected TnScrollPanel tnComponent;
    
    protected boolean isHitBottom = false;
    
    protected boolean isHitTop = false;
    
    protected int SDK_INT = 0;
    
    protected float downX, downY;
    
    /**
     * Constructor for VerticalScrollPanel.
     * 
     * @param context the context
     * @param tnComponent the outer Component.
     */
    public AndroidVerticalScrollPanel(Context context, TnScrollPanel tnComponent)
    {
        super(context);
        
        this.tnComponent = tnComponent;
        
        SDK_INT = Build.VERSION.SDK_INT;
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
    
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        boolean isIntercept = super.onInterceptTouchEvent(ev);
        
        if(SDK_INT > 8)
        {
            return isIntercept;
        }
        
        if (isHitBottom && isIntercept)
        {
            if (ev.getAction() == MotionEvent.ACTION_DOWN)
            {
                downX = ev.getX();
                downY = ev.getY();
                return false;
            }
            else if (ev.getAction() == MotionEvent.ACTION_UP)
            {
                isHitBottom = false;
                return false;
            }
            else if(ev.getAction() == MotionEvent.ACTION_MOVE)
            {
                if(Math.abs(downX - ev.getX()) < 2.0f && Math.abs(downY - ev.getY()) < 2.0f)
                {
                    return false;
                }
                else
                {
                    isHitBottom = false;
                }
            }
            else
            {
                isHitBottom = false;
            }
        }
        
        if (isHitTop && isIntercept)
        {
            if (ev.getAction() == MotionEvent.ACTION_DOWN)
            {
                downX = ev.getX();
                downY = ev.getY();
                return false;
            }
            else if (ev.getAction() == MotionEvent.ACTION_UP)
            {
                isHitTop = false;
                return false;
            }
            else if(ev.getAction() == MotionEvent.ACTION_MOVE)
            {
                if(Math.abs(downX - ev.getX()) < 2.0f && Math.abs(downY - ev.getY()) < 2.0f)
                {
                    return false;
                }
                else
                {
                    isHitTop = false;
                }
            }
            else
            {
                isHitTop = false;
            }
        }

        return isIntercept;
    }
    
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "onScrollEvent", null, 
            new Object[]{TnUiEvent.LOG_UI_EVENT, Integer.valueOf(l), Integer.valueOf(t), Integer.valueOf(oldl), Integer.valueOf(oldt)}, true);
        if(SDK_INT <= 8 && this.getChildCount() == 1)
        {
            if( this.getHeight() >= this.getChildAt(0).getHeight() - t) 
            {
                isHitBottom = true;
            }
            else
            {
                isHitBottom = false;
            }
            
            if(t <= 0)
            {
                isHitTop = true;
            }
            else
            {
                isHitTop = false;
            }
        }
        
        this.tnComponent.dispatchScrollChanged(l, t, oldl, oldt);
    }
    
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch(ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                AndroidUiEventHandler.onTouch(this.tnComponent, ev);
                break;
            }
        }
        boolean result = super.onTouchEvent(ev);
        if (this.tnComponent.getInternalScrollPanel() != null)
        {
            return false;
        }
        return result;
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
            case TnScrollPanel.METHOD_SCROLL_TO:
            {
                this.scrollTo(((Integer)args[0]).intValue(), ((Integer)args[1]).intValue());
                break;
            }
            case TnScrollPanel.METHOD_SET_SCROLLBAR_VISIBLE:
            {
                if(args[0] instanceof Boolean)
                {
                    boolean isVisible = ((Boolean)args[0]).booleanValue();
                    this.setVerticalScrollBarEnabled(isVisible);
                }
                break;
            }
            case TnScrollPanel.METHOD_SET_VERTICAL_FADING_EDGE_ENABLED:
            {
                if (args[0] instanceof Boolean)
                {
                    boolean verticalFadingEdgeEnabled = ((Boolean) args[0]).booleanValue();
                    this.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
                }
                break;
            }
        }

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
    
}

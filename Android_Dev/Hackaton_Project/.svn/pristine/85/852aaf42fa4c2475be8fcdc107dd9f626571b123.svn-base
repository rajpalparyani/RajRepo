/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidLinearLayout.java
 *
 */
package com.telenav.tnui.widget.android;

import android.content.Context;
import android.graphics.Canvas;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.INativeUiContainer;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;

/**
 * A Layout that arranges its children in a single column or a single row. The direction of 
 * the row can be set by calling {@link #setOrientation(int) setOrientation()}. 
 * You can also specify gravity, which specifies the alignment of all the child elements by
 * calling {@link #setGravity(int) setGravity()} or specify that specific children 
 * grow to fill up any remaining space in the layout by setting the <em>weight</em> member of
 * {@link android.widget.LinearLayout.LayoutParams LinearLayout.LayoutParams}.
 * The default orientation is horizontal.
 *
 * <p>
 * Also see {@link LinearLayout.LayoutParams android.widget.LinearLayout.LayoutParams}
 * for layout attributes </p>
 *
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class AndroidLinearLayout extends LinearLayout implements INativeUiContainer, OnTouchListener
{
    protected TnLinearContainer tnContainer;
    
    /**
     * The constructor of this class.
     * 
     * @param context
     * @param tnContainer
     */
    public AndroidLinearLayout(Context context, TnLinearContainer tnContainer)
    {
        super(context);
        
        this.tnContainer = tnContainer;
        
        this.setOnTouchListener(this);
        
        this.setDescendantFocusability(LinearLayout.FOCUS_AFTER_DESCENDANTS);
        
        this.setClickable(true);
    }

    public void addNativeComponent(AbstractTnComponent uiComponent)
    {
        android.view.ViewGroup.LayoutParams layoutParams = ((View)uiComponent.getNativeUiComponent()).getLayoutParams();
        if(layoutParams == null)
        {
            layoutParams = getDefaultLayoutParams();
        }
        
        this.addView((View)uiComponent.getNativeUiComponent(), layoutParams);
    }

    public void addNativeComponent(AbstractTnComponent uiComponent, int index)
    {
        android.view.ViewGroup.LayoutParams layoutParams = ((View)uiComponent.getNativeUiComponent()).getLayoutParams();
        if(layoutParams == null)
        {
            layoutParams = getDefaultLayoutParams();
        }
        
        this.addView((View)uiComponent.getNativeUiComponent(), index, layoutParams);
    }

    public AbstractTnComponent getNativeComponent(int index)
    {
        View view = this.getChildAt(index);
        if(view instanceof INativeUiComponent)
        {
            return ((INativeUiComponent)view).getTnUiComponent();
        }
        
        return null;
    }

    public int indexOfComponent(AbstractTnComponent uiComponent)
    {
        return this.indexOfChild((View)uiComponent.getNativeUiComponent());
    }

    public void removeAllNativeComponents()
    {
        this.removeAllViews();
    }

    public void removeNativeComponent(AbstractTnComponent uiComponent)
    {
        this.removeView((View)uiComponent.getNativeUiComponent());
    }
    
    public void removeNativeComponentInLayout(AbstractTnComponent uiComponent)
    {
        this.removeViewInLayout((View)uiComponent.getNativeUiComponent());
    }

    public void removeNativeComponent(int index)
    {
        this.removeViewAt(index);
    }
    
    
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTouch(this.tnContainer, event);
        
        return isHandled ? true : super.onTouchEvent(event);
    }
    
    private LinearLayout.LayoutParams getDefaultLayoutParams()
    {
        LinearLayout.LayoutParams layoutParams = null;
        
        switch(this.getOrientation())
        {
            case VERTICAL:
            {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                break;
            }
            case HORIZONTAL:
            {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                break;
            }
        }
        
        return layoutParams;
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        this.tnContainer.dispatchSizeChanged(w, h, oldw, oldh);
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        
        this.tnContainer.dispatchDisplayChanged(true);
    }
    
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        
        this.tnContainer.dispatchDisplayChanged(false);
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int prefWidth = this.tnContainer.getPreferredWidth();
        int prefHeight = this.tnContainer.getPreferredHeight();

        
        if(prefWidth > 0 && ( widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY ) )
        {
            if(widthSize > 0 && prefWidth > widthSize)
                prefWidth = widthSize;

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(prefWidth, MeasureSpec.EXACTLY);
        }
        
        if(prefHeight > 0 &&  ( heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY ) )
        {
            if(heightSize > 0 && prefHeight > heightSize)
                prefHeight = heightSize;
            
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(prefHeight, MeasureSpec.EXACTLY);
        }
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        
        boolean gainFocus = this.isFocused() || this.isPressed();
        if(this.tnContainer.isFocused() != gainFocus)
        {
            this.tnContainer.dispatchFocusChanged(gainFocus);
            invalidate();
        }
    }
    
    protected void dispatchDraw(Canvas canvas) 
    {
        if(AbstractTnGraphics.getInstance() != null)
        {
            AbstractTnGraphics.getInstance().setGraphics(canvas);
            
            canvas.save();
            
            tnContainer.draw(AbstractTnGraphics.getInstance());
            
            canvas.restore();
        }
        
        super.dispatchDraw(canvas);
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(tnContainer, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;

        switch (eventMethod)
        {
            case TnLinearContainer.METHOD_SCROLLABLE:
            {
                this.setScrollContainer(((Boolean)args[0]).booleanValue());
                break;
            }
            case TnLinearContainer.METHOD_ORIENTATION:
            {
                int orientation = HORIZONTAL;
                if (tnContainer.isVertical())
                {
                    orientation = VERTICAL;
                }
                this.setOrientation(orientation);
                break;
            }
            case TnLinearContainer.METHOD_SET_PADDING:
            {
                if(args != null && args.length > 3)
                {
                    int left = ((Integer)args[0]).intValue();
                    int top = ((Integer)args[1]).intValue();
                    int right = ((Integer)args[2]).intValue();
                    int bottom = ((Integer)args[3]).intValue();
                    this.setPadding(left, top, right, bottom);
                }
                break;
            }
            case TnLinearContainer.METHOD_SET_ANCHOR:
            {
                int gravity = Gravity.NO_GRAVITY;
                int arg = tnContainer.getAnchor();
                if( (arg & AbstractTnGraphics.LEFT) != 0)
                {
                    gravity |= Gravity.LEFT;
                }
                
                if( (arg & AbstractTnGraphics.RIGHT) != 0)
                {
                    gravity |= Gravity.RIGHT;
                }
                
                if( (arg & AbstractTnGraphics.TOP) != 0)
                {
                    gravity |= Gravity.TOP;
                }
                
                if( (arg & AbstractTnGraphics.BOTTOM) != 0)
                {
                    gravity |= Gravity.BOTTOM;
                }
                
                if( (arg & AbstractTnGraphics.HCENTER) != 0)
                {
                    gravity |= Gravity.CENTER_HORIZONTAL;
                }
                
                if( (arg & AbstractTnGraphics.VCENTER) != 0)
                {
                    gravity |= Gravity.CENTER_VERTICAL;
                }
                
                this.setGravity(gravity);
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
        return this.tnContainer;
    }

    public int getChildrenSize()
    {
        return this.getChildCount();
    }
    
    public AbstractTnComponent getFocusedComponent()
    {
        View focusView = this.getFocusedChild();
        if(focusView instanceof INativeUiComponent)
        {
            return ((INativeUiComponent)focusView).getTnUiComponent();
        }
        
        return null;
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTouch(this.tnContainer, event);
        
        return isHandled ? true : super.onTouchEvent(event);
    }
}

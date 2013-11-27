/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidAbsoluteLayout.java
 *
 */
package com.telenav.tnui.widget.android;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.INativeUiContainer;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnAbsoluteContainer;

/**
 *@author bduan
 *@date 2010-11-1
 */
class AndroidAbsoluteLayout extends AbsoluteLayout implements INativeUiContainer
{
    TnAbsoluteContainer tnAbsoluteContainer;

    public AndroidAbsoluteLayout(Context context, TnAbsoluteContainer tnAbsoluteContainer)
    {
        super(context);
        this.tnAbsoluteContainer = tnAbsoluteContainer;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        this.tnAbsoluteContainer.dispatchSizeChanged(w, h, oldw, oldh);
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        
        this.tnAbsoluteContainer.dispatchDisplayChanged(true);
    }
    
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        
        this.tnAbsoluteContainer.dispatchDisplayChanged(false);
    }
    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if (child instanceof INativeUiComponent)
            {
                AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) child.getLayoutParams();

                lp.x = ((INativeUiComponent) child).getTnUiComponent().getX();
                lp.y = ((INativeUiComponent) child).getTnUiComponent().getY();
            }
        }
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int prefWidth = this.tnAbsoluteContainer.getPreferredWidth();
        int prefHeight = this.tnAbsoluteContainer.getPreferredHeight();

        
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
	
    protected void dispatchDraw(Canvas canvas) 
    {
        if(AbstractTnGraphics.getInstance() != null)
        {
            AbstractTnGraphics.getInstance().setGraphics(canvas);
            
            canvas.save();
            
            tnAbsoluteContainer.draw(AbstractTnGraphics.getInstance());
            
            canvas.restore();
        }
        
        super.dispatchDraw(canvas);
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(tnAbsoluteContainer, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;

        return null;
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
        return this.tnAbsoluteContainer;
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

    public void addNativeComponent(AbstractTnComponent uiComponent)
    {
        android.view.ViewGroup.LayoutParams layoutParams = getChildLayoutParams(uiComponent);
        
        this.addView((View)uiComponent.getNativeUiComponent(), layoutParams);
    }

    private LayoutParams getChildLayoutParams(AbstractTnComponent uiComponent)
    {
        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, uiComponent.getX(), uiComponent.getY());

        return layoutParams;
    }

    public void addNativeComponent(AbstractTnComponent uiComponent, int index)
    {
        android.view.ViewGroup.LayoutParams layoutParams = ((View)uiComponent.getNativeUiComponent()).getLayoutParams();
        if(layoutParams == null)
        {
            layoutParams = getChildLayoutParams(uiComponent);
        }
        
        this.addView((View)uiComponent.getNativeUiComponent(), index, layoutParams);
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

}

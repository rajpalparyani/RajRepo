/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimField.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;

/**
 * Provides fundamental functionality for all field components. <br />
 * <br />
 * A field represents a rectangular region contained by a manager. The field sizes itself according to its needs in
 * layout. The manager, rather than the contained fields, completely handles scrolling.<br />
 * <br />
 * You can't instantiate Field directly, rather, use one of the provided specialized field components in
 * net.rim.device.api.ui.component, or extend Field to create your own, custom field type.<br />
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-8
 */
public class RimField extends Field implements INativeUiComponent
{
    protected AbstractTnComponent tnComponent;

    protected boolean isVisible;

    protected boolean isFocusable;

    public RimField(AbstractTnComponent tnComponent)
    {
        this.tnComponent = tnComponent;
        isVisible = true;
        isFocusable = false;
    }

    protected void onFocus(int direction)
    {
        super.onFocus(direction);
        this.tnComponent.dispatchFocusChanged(true);
    }

    protected void onUnfocus()
    {
        super.onUnfocus();
        this.tnComponent.dispatchFocusChanged(false);
    }
    
    protected void onDisplay()
    {
        super.onDisplay();
        
        this.tnComponent.dispatchDisplayChanged(true);
    }
    
    protected void onUndisplay()
    {
        super.onUndisplay();
        
        this.tnComponent.dispatchDisplayChanged(false);
    }

    protected void drawFocus(Graphics graphics, boolean on)
    {
        return;// if we used the system focus style, it will draw a black rectangle.
    }

    protected void layout(int width, int height)
    {
        this.tnComponent.sublayout(width, height);

        this.setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public int getPreferredWidth()
    {
        if (!isVisible)
            return 0;

        return this.tnComponent.getPreferredWidth();
    }

    public int getPreferredHeight()
    {
        if (!isVisible)
            return 0;

        return this.tnComponent.getPreferredHeight();
    }

    protected void setExtent(int width, int height)
    {
        if (this.getExtent().width != width || this.getExtent().height != height)
        {
            onSizeChanged(width, height, this.getExtent().width, this.getExtent().height);
        }

        super.setExtent(width, height);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        this.tnComponent.dispatchSizeChanged(w, h, oldw, oldh);
    }

    protected void paint(Graphics g)
    {
        RimGraphics.getInstance().setGraphics(g);

        tnComponent.draw(RimGraphics.getInstance());
    }

    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = RimUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!RimUiMethodHandler.NO_HANDLED.equals(obj))
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
        return this.tnComponent;
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean isNativeVisible()
    {
        return this.isVisible;
    }

    public boolean requestNativeFocus()
    {
        this.setFocus();

        return true;
    }

    public void requestNativePaint()
    {
        this.invalidate();
    }

    public boolean isFocusable()
    {
        return this.isFocusable;
    }

    public void setNativeVisible(boolean isVisible)
    {
        if (this.isVisible != isVisible)
        {
            this.updateLayout();
        }
        this.isVisible = isVisible;
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        if (this.isFocusable != isFocusable)
        {
            this.invalidate();
        }
        this.isFocusable = isFocusable;
    }
}

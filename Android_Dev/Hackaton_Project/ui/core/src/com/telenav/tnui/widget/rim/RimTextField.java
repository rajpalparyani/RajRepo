/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimTextField.java
 *
 */
package com.telenav.tnui.widget.rim;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.ScrollChangeListener;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.rim.RimUiMethodHandler;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-11
 */
public class RimTextField extends HorizontalFieldManager implements INativeUiComponent
{
    protected AbstractTnComponent tnComponent;

    protected boolean isVisible;

    protected boolean isFocusable;
    
    protected RimInnerTextField innerTextField;
    protected RimInnerTextFieldScroll innerTextFieldScroll;
    
    public RimTextField(AbstractTnComponent tnComponent)
    {
        this.tnComponent = tnComponent;
        isVisible = true;
        isFocusable = false;
        
        innerTextField = new RimInnerTextField("", EditField.NO_NEWLINE);
        innerTextFieldScroll = new RimInnerTextFieldScroll();
        innerTextFieldScroll.add(innerTextField);
        this.add(innerTextFieldScroll);
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

//    protected void drawFocus(Graphics graphics, boolean on)
//    {
//        return;// if we used the system focus style, it will draw a black rectangle.
//    }

//    protected void layout(int width, int height)
//    {
//        this.tnComponent.sublayout(width, height);
//
//        this.setExtent(getPreferredWidth(), getPreferredHeight());
//    }

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

//    protected void paint(Graphics g)
//    {
//        RimGraphics.getInstance().setGraphics(g);
//
//        tnComponent.draw(RimGraphics.getInstance());
//    }

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
    
    protected class RimInnerTextFieldScroll extends HorizontalFieldManager
    {
        
        RimInnerTextFieldScroll()
        {
            super(HORIZONTAL_SCROLL);
            
            this.setScrollListener(new ScrollChangeListener()
            {
                
                public void scrollChanged(Manager arg0, int arg1, int arg2)
                {
//                    innerTextField
                }
            });
        }
        
//        public void sublayout(int width, int height)
//        {
//            super.sublayout(getPreferredWidth(), getPreferredHeight());
//            setExtent(getPreferredWidth(), getPreferredHeight());
//        }
//        
//        public int getPreferredHeight()
//        {
//            return inputBoxHeight;
//        }
//        
//        public int getPreferredWidth()
//        {
//            return inputBoxWidth - 4 - inputBoxHintWidth;
//        }
    }
    
    protected static class RimInnerTextField extends EditField
    {
        public RimInnerTextField(String initText, long style)
        {
            super("", initText, 1000, style);
        }
    }
}

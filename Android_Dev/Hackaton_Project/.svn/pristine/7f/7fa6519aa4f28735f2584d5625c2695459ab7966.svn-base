/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimAbsoluteFieldManager.java
 *
 */
package com.telenav.tnui.widget.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.AbsoluteFieldManager;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.INativeUiContainer;
import com.telenav.tnui.core.rim.RimUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.tnui.widget.TnLinearContainer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-11
 */
public class RimAbsoluteFieldManager extends AbsoluteFieldManager implements INativeUiContainer
{
    protected TnAbsoluteContainer tnContainer;

    protected Manager linearManager;

    protected boolean isVisible;

    protected boolean isFocusable;
    
    public RimAbsoluteFieldManager(TnAbsoluteContainer tnContainer)
    {
        super();
        
        this.tnContainer = tnContainer;
    }

    public void addNativeComponent(AbstractTnComponent uiComponent)
    {
        this.add((Field)uiComponent.getNativeUiComponent());
    }

    public void addNativeComponent(AbstractTnComponent uiComponent, int index)
    {
        this.insert((Field)uiComponent.getNativeUiComponent(), index);
    }

    public int getChildrenSize()
    {
        return this.getFieldCount();
    }

    public AbstractTnComponent getFocusedComponent()
    {
        Field focusField = this.getFieldWithFocus();
        if(focusField instanceof INativeUiComponent)
        {
            return ((INativeUiComponent)focusField).getTnUiComponent();
        }
        
        return null;
    }

    public AbstractTnComponent getNativeComponent(int index)
    {
        Field field = this.getField(index);
        if(field instanceof INativeUiComponent)
        {
            return ((INativeUiComponent)field).getTnUiComponent();
        }
        
        return null;
    }

    public int indexOfComponent(AbstractTnComponent uiComponent)
    {
        for(int i = 0; i < this.getFieldCount(); i++)
        {
            if(uiComponent.getNativeUiComponent().equals(this.getField(i)))
            {
                return i;
            }
        }
        
        return -1;
    }

    public void removeAllNativeComponents()
    {
        this.deleteAll();
    }

    public void removeNativeComponent(AbstractTnComponent uiComponent)
    {
        this.delete((Field)uiComponent.getNativeUiComponent());
    }

	public void removeNativeComponentInLayout(AbstractTnComponent uiComponent)
    {

    }

    public void removeNativeComponent(int index)
    {
        Field field = this.getField(index);
        this.delete(field);
    }

    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = RimUiMethodHandler.callUiMethod(tnContainer, this, eventMethod, args);

        if (!RimUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;

        switch (eventMethod)
        {
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
        }

        return null;
    }
    
    protected void paint(Graphics g)
    {
        AbstractTnGraphics.getInstance().setGraphics(g);

        tnContainer.draw(AbstractTnGraphics.getInstance());
        
        super.paint(g);
    }
    
    public int getPreferredWidth()
    {
        if (!isVisible)
            return 0;

        return this.tnContainer.getPreferredWidth();
    }

    public int getPreferredHeight()
    {
        if (!isVisible)
            return 0;

        return this.tnContainer.getPreferredHeight();
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
        this.tnContainer.dispatchSizeChanged(w, h, oldw, oldh);
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

//    public boolean isFocusable()
//    {
//        return this.isFocusable;
//    }

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
    
    protected void onDisplay()
    {
        super.onDisplay();
        
        this.tnContainer.dispatchDisplayChanged(true);
    }
    
    protected void onUndisplay()
    {
        super.onUndisplay();
        
        this.tnContainer.dispatchDisplayChanged(false);
    }
}

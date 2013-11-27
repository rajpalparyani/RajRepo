/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimVerticalFieldManager.java
 *
 */
package com.telenav.tnui.widget.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.INativeUiContainer;
import com.telenav.tnui.core.rim.RimUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnScrollPanel;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-11
 */
public class RimVerticalFieldManager extends VerticalFieldManager implements INativeUiContainer
{
    protected AbstractTnComponent tnContainer;

    protected Manager linearManager;

    protected boolean isVisible;

    protected boolean isFocusable;
    
    public RimVerticalFieldManager(AbstractTnComponent tnContainer, long style)
    {
        super(style);
        
        this.isVisible = true;
        this.isFocusable = true;
        
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
            case TnScrollPanel.METHOD_GET:
            {
                Field field = this.getField(0);
                if (field instanceof INativeUiComponent)
                {
                    return ((INativeUiComponent) field).getTnUiComponent();
                }
                break;
            }
            case TnScrollPanel.METHOD_SET:
            {
                if (this.getFieldCount() > 0)
                {
                    this.deleteAll();
                }
                AbstractTnComponent uiComponent = (AbstractTnComponent) args[0];
                this.add((Field) uiComponent.getNativeUiComponent());
                break;
            }
            case TnScrollPanel.METHOD_REMOVE:
            {
                this.deleteAll();
                break;
            }
            case TnScrollPanel.METHOD_SCROLL_TO:
            {
                this.setVerticalScroll(((Integer)args[0]).intValue());
                break;
            }
            case TnScrollPanel.METHOD_SMOOTH_SCROLL_TO:
            {
                this.setVerticalScroll(((Integer)args[0]).intValue(), true);
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
//        super.isFocusable();
//        
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

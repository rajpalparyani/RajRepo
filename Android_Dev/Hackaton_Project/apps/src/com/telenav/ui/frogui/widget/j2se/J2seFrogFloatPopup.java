/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seFrogFloatPopup.java
 *
 */
package com.telenav.ui.frogui.widget.j2se;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import com.telenav.frogui.widget.FrogFloatPopup;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.j2se.J2seRootPaneContainer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 5, 2010
 */
public class J2seFrogFloatPopup implements INativeUiComponent
{
    /**
     * 
     */
    private static final long serialVersionUID = -401497358748090634L;

    protected Component content;
    protected boolean isShown;
    protected Popup popup;
    protected J2seRootPaneContainer rootPaneContainer;
    
    public J2seFrogFloatPopup(J2seRootPaneContainer rootPaneContainer)
    {
        super();
        
        this.rootPaneContainer = rootPaneContainer;
    }
    
    public void setContent(AbstractTnComponent component)
    {
        if(component != null && component.getNativeUiComponent() != null)
        {
            this.content = (Component) component.getNativeUiComponent();
            Container parent = this.content.getParent();
            if (parent != null)
            {
                parent.remove(this.content);
            }
        }
        
    }
    
    private void showFloatPopup(AbstractTnComponent anchor, int x, int y, int w, int h)
    {
        if(this.isShown)
            return;
        
        isShown = true;
        
        x = this.rootPaneContainer.getRootPane().getX() + x;
        y = this.rootPaneContainer.getRootPane().getY() + y;
        
        content.setPreferredSize(new Dimension(w, h));
        content.setMinimumSize(content.getPreferredSize());
        content.setMaximumSize(content.getPreferredSize());
        this.popup = PopupFactory.getSharedInstance().getPopup((Component)anchor.getNativeUiComponent(), content, x, y);
        this.popup.show();
    }
    
    private void updateFloatPopup(AbstractTnComponent anchor, int w, int h)
    {
        
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        switch (eventMethod)
        {
            case FrogFloatPopup.METHOD_SET:
            {
                if(args != null && args.length > 0)
                {
                    setContent((AbstractTnComponent)args[0]);
                    if(args.length > 2)
                    {
                        this.content.setSize(((Integer)args[1]).intValue(), ((Integer)args[2]).intValue());
                    }
                }
                break;
            }
            case FrogFloatPopup.METHOD_SHOW:
            {
                if(args != null && args.length > 2)
                {
                    int x = ((Integer)args[1]).intValue();
                    int y = ((Integer)args[2]).intValue();
                    
                    int w = ((Integer)args[3]).intValue();
                    int h = ((Integer)args[4]).intValue();
                    showFloatPopup((AbstractTnComponent)args[0], x, y, w, h);
                }
                break;
            }
            case FrogFloatPopup.METHOD_HIDE:
            {
                if(this.isShown)
                {
                    this.popup.hide();
                }
                break;
            }
            case FrogFloatPopup.METHOD_UPDATE:
            {
                if(args != null && args.length > 2)
                {
                    int w = ((Integer)args[1]).intValue();
                    int h = ((Integer)args[2]).intValue();
                    
                    updateFloatPopup((AbstractTnComponent)args[0], w, h);
                }
                break;
            }
            case FrogFloatPopup.METHOD_SET_WDITH:
            {
                if(args != null && args.length > 0)
                {
                    this.content.setSize(((Integer)args[0]).intValue(), this.content.getHeight());
                }
                break;
            }
            case FrogFloatPopup.METHOD_SET_HEIGHT:
            {
                if(args != null && args.length > 0)
                {
                    this.content.setSize(this.content.getWidth(), ((Integer)args[0]).intValue());
                }
                break;
            }
        }
        
        return null;
    }

    public int getNativeHeight()
    {
        return this.content.getHeight();
    }

    public int getNativeWidth()
    {
        return this.content.getWidth();
    }

    public int getNativeX()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getNativeY()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return null;
    }

    public boolean isNativeFocusable()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isNativeVisible()
    {
        return this.isShown;
    }

    public boolean requestNativeFocus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void requestNativePaint()
    {
        // TODO Auto-generated method stub
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        // TODO Auto-generated method stub
    }

    public void setNativeVisible(boolean isVisible)
    {
        // TODO Auto-generated method stub
        
    }
}

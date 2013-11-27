/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seComponent.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 4, 2010
 */
public class J2seComponent extends JComponent implements INativeUiComponent
{
    /**
     * 
     */
    private static final long serialVersionUID = -3383817279123000999L;
    
    protected AbstractTnComponent tnComponent;

    public J2seComponent(AbstractTnComponent tnComponent)
    {
        this.tnComponent = tnComponent;
        
        J2seBasicListener listener = new J2seBasicListener(this.tnComponent);
        this.addFocusListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addKeyListener(listener);
    }
    
    public Dimension getPreferredSize()
    {
        int parentWidth = this.getRootPane().getWidth() - 3;
        int parentHeight = this.getRootPane().getHeight() - 3;
        this.tnComponent.sublayout(parentWidth, parentHeight);
        
        int prefWidth = this.tnComponent.getPreferredWidth();
        int prefHeight = this.tnComponent.getPreferredHeight();
        
        if(prefWidth > 0 || prefHeight > 0)
        {
            if(prefWidth <= 0)
            {
                prefWidth = parentWidth;
            }
            if(prefHeight <= 0)
            {
                prefHeight = parentHeight;
            }
        }
        
        return new Dimension(prefWidth, prefHeight);
    }
    
    public Dimension getMinimumSize()
    {
        return this.getPreferredSize();
    }
    
    public Dimension getMaximumSize()
    {
        return this.getPreferredSize();
    }
    
    public void setBounds(int x, int y, int width, int height)
    {
        if(this.getWidth() != width || this.getHeight() != height)
        {
            this.tnComponent.dispatchSizeChanged(width, height, this.getWidth(), this.getHeight());
        }
        
        super.setBounds(x, y, width, height);
    }
    
    public void paint(Graphics g)
    {
        Font font = g.getFont();
        Color color = g.getColor();
        
        J2seGraphics.getInstance().setGraphics(g);
        tnComponent.draw(AbstractTnGraphics.getInstance());
        
        g.setFont(font);
        g.setColor(color);
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = J2seUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!J2seUiMethodHandler.NO_HANDLED.equals(obj))
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
        return this.getX();
    }

    public int getNativeY()
    {
        return this.getY();
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
        return this.isVisible();
    }

    public boolean requestNativeFocus()
    {
        return this.requestFocusInWindow();
    }

    public void requestNativePaint()
    {
        this.repaint();
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public void setNativeVisible(boolean isVisible)
    {
        this.setVisible(isVisible);
    }
}

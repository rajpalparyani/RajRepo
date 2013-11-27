package com.telenav.tnui.widget.j2se;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import com.telenav.tnui.core.INativeUiComponent;

public class AbsoluteLayoutManager implements LayoutManager
{
    public void addLayoutComponent(String name, Component comp)
    {
    }

    public void layoutContainer(Container parent) 
    {
        Component[] comps = parent.getComponents();
        for (int i = 0; i < comps.length; i++)
        {
            Dimension d = comps[i].getPreferredSize();
            INativeUiComponent uiComp = (INativeUiComponent)comps[i];
            
            int x = uiComp.getTnUiComponent().getX();
            int y = uiComp.getTnUiComponent().getY();
            
            comps[i].setBounds(x, y, d.width, d.height);
        }
    }

    public Dimension minimumLayoutSize(Container parent) 
    {
        return preferredLayoutSize(parent);
    }

    public Dimension preferredLayoutSize(Container parent) 
    {
        int prefWidth  = 0;
        int prefHeight = 0;
        
        Component[] comps = parent.getComponents();
        for (int i = 0; i < comps.length; i++)
        {
            if (! comps[i].isVisible()) continue;
            
            Dimension d = comps[i].getPreferredSize();
            INativeUiComponent uiComp = (INativeUiComponent)comps[i];
            
            int x = uiComp.getTnUiComponent().getX();
            int y = uiComp.getTnUiComponent().getY();
            
            prefWidth = Math.max(prefWidth, x + d.width);
            prefHeight = Math.max(prefHeight, y + d.height);
              
        }
        
        return new Dimension(prefWidth, prefHeight);
    }

    public void removeLayoutComponent(Component comp) 
    {
    }

}

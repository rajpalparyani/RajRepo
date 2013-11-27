/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seApplet.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;

import com.telenav.tnui.core.TnScreen;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 3, 2010
 */
public class J2seRootPaneContainer implements RootPaneContainer
{
    /**
     * 
     */
    private static final long serialVersionUID = 7556807093739738808L;
    
    /**
     * The top screen of this activity.
     */
    protected TnScreen currentScreen;
    protected RootPaneContainer rootPaneContainer;
    
    public J2seRootPaneContainer(RootPaneContainer rootPaneContainer)
    {
        this.rootPaneContainer = rootPaneContainer;
    }
    
    /**
     * Set the screen content to an explicit screen. This screen is placed directly into the activity's view hierarchy.
     * It can itself be a complex view hierarchy.
     * 
     * @param screen
     */
    public final void showScreen(TnScreen screen)
    {
        if (screen == null)
            return;

        Component component = (Component) screen.getRootContainer().getNativeUiComponent();
        component.setSize(this.getRootPane().getSize());
        
        if (screen.equals(this.currentScreen))
        {
            component.repaint();
        }
        else
        {
            this.currentScreen = screen;
            this.setContentPane((Container)component);
            this.getRootPane().setVisible(true);
            component.requestFocus();
        }
    }

    public Container getContentPane()
    {
        return this.rootPaneContainer.getContentPane();
    }

    public Component getGlassPane()
    {
        return this.rootPaneContainer.getGlassPane();
    }

    public JLayeredPane getLayeredPane()
    {
        return this.rootPaneContainer.getLayeredPane();
    }

    public JRootPane getRootPane()
    {
        return this.rootPaneContainer.getRootPane();
    }

    public void setContentPane(Container contentPane)
    {
        this.rootPaneContainer.setContentPane(contentPane);
    }

    public void setGlassPane(Component glassPane)
    {
        this.rootPaneContainer.setGlassPane(glassPane);
    }

    public void setLayeredPane(JLayeredPane layeredPane)
    {
        this.rootPaneContainer.setLayeredPane(layeredPane);
    }
    
    public RootPaneContainer getRootContainer()
    {
    	return rootPaneContainer;
    }
}

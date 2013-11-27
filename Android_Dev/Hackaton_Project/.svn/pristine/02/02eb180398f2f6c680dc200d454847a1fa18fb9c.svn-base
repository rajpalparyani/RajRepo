/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Popup.java
 *
 */
package com.telenav.ui.frogui.widget.j2se;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import sun.awt.ModalExclude;

import com.telenav.tnui.core.j2se.J2seComponent;
import com.telenav.tnui.graphics.TnColor;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 6, 2010
 */
public class Popup
{
    /**
     * The Component representing the Popup.
     */
    private Component component;

    /**
     * Creates a <code>Popup</code> for the Component <code>owner</code>
     * containing the Component <code>contents</code>. <code>owner</code>
     * is used to determine which <code>Window</code> the new
     * <code>Popup</code> will parent the <code>Component</code> the
     * <code>Popup</code> creates to.
     * A null <code>owner</code> implies there is no valid parent.
     * <code>x</code> and
     * <code>y</code> specify the preferred initial location to place
     * the <code>Popup</code> at. Based on screen size, or other paramaters,
     * the <code>Popup</code> may not display at <code>x</code> and
     * <code>y</code>.
     *
     * @param owner    Component mouse coordinates are relative to, may be null
     * @param contents Contents of the Popup
     * @param x        Initial x screen coordinate
     * @param y        Initial y screen coordinate
     * @exception IllegalArgumentException if contents is null
     */
    protected Popup(Component owner, Component contents, int x, int y) {
        this();
        if (contents == null) {
            throw new IllegalArgumentException("Contents must be non-null");
        }
        reset(owner, contents, x, y);
    }

    /**
     * Creates a <code>Popup</code>. This is provided for subclasses.
     */
    protected Popup() {
    }

    /**
     * Makes the <code>Popup</code> visible. If the <code>Popup</code> is
     * currently visible, this has no effect.
     */
    public void show() {
        Component component = getComponent();

        if (component != null) {
            component.show();
        }
    }

    /**
     * Hides and disposes of the <code>Popup</code>. Once a <code>Popup</code>
     * has been disposed you should no longer invoke methods on it. A
     * <code>dispose</code>d <code>Popup</code> may be reclaimed and later used
     * based on the <code>PopupFactory</code>. As such, if you invoke methods
     * on a <code>disposed</code> <code>Popup</code>, indeterminate
     * behavior will result.
     */
    public void hide() {
        Component component = getComponent();

        if (component instanceof JWindow) {
            component.hide();
            ((JWindow)component).getContentPane().removeAll();
        }
        dispose();
    }

    /**
     * Frees any resources the <code>Popup</code> may be holding onto.
     */
    void dispose() {
        Component component = getComponent();
        Window window = SwingUtilities.getWindowAncestor(component);

        if (component instanceof JWindow) {
            ((Window)component).dispose();
            component = null;
        }
        // If our parent is a DefaultFrame, we need to dispose it, too.
        if (window instanceof DefaultFrame) {
            window.dispose();
        }
    }

    /**
     * Resets the <code>Popup</code> to an initial state.
     */
    void reset(Component owner, Component contents, int ownerX, int ownerY) {
        if (getComponent() == null) {
            component = createComponent(owner);
        }

        Component c = getComponent();

        if (c instanceof JWindow) {
            JWindow component = (JWindow)getComponent();

            component.setLocation(ownerX, ownerY);
            component.getContentPane().add(contents, BorderLayout.CENTER);
            contents.invalidate();
            if(component.isVisible()) {
                // Do not call pack() if window is not visible to
                // avoid early native peer creation
                pack();
            }
        }
    }


    /**
     * Causes the <code>Popup</code> to be sized to fit the preferred size
     * of the <code>Component</code> it contains.
     */
    void pack() {
        Component component = getComponent();

        if (component instanceof Window) {
            ((Window)component).pack();
        }
    }

    /**
     * Returns the <code>Window</code> to use as the parent of the
     * <code>Window</code> created for the <code>Popup</code>. This creates
     * a new <code>DefaultFrame</code>, if necessary.
     */
    private Window getParentWindow(Component owner) {
        Window window = null;

        if (owner instanceof Window) {
            window = (Window)owner;
        }
        else if (owner != null) {
            window = SwingUtilities.getWindowAncestor(owner);
        }
        if (window == null) {
            window = new DefaultFrame();
        }
        return window;
    }

    /**
     * Creates the Component to use as the parent of the <code>Popup</code>.
     * The default implementation creates a <code>Window</code>, subclasses
     * should override.
     */
    Component createComponent(Component owner) {
        if (GraphicsEnvironment.isHeadless()) {
            // Generally not useful, bail.
            return null;
        }
        return new HeavyWeightWindow(getParentWindow(owner));
    }

    /**
     * Returns the <code>Component</code> returned from
     * <code>createComponent</code> that will hold the <code>Popup</code>.
     */
    Component getComponent() {
        if(component != null)
        {
            if(component instanceof JComponent)
            {
                ((J2seComponent) component).setOpaque(false);
            }
            else if(component instanceof JWindow)
            {
                ((JWindow) component).getParent().setBackground(new Color(TnColor.TRANSPARENT, true));
                JRootPane rootPane = ((JWindow) component).getRootPane();
                rootPane.setOpaque(false);
                rootPane.setBackground(new Color(TnColor.TRANSPARENT, true));
                ((JComponent)((JWindow) component).getRootPane().getContentPane()).setOpaque(false);
                ((JWindow) component).getRootPane().getContentPane().setBackground(new Color(TnColor.TRANSPARENT, true));
                setColor(((JWindow) component).getRootPane());
                setColor(((JWindow) component).getRootPane().getContentPane());
                ((JComponent)((JWindow) component).getRootPane().getContentPane()).setBorder(new EmptyBorder(0, 0, 0, 0));
                rootPane.setBorder(new EmptyBorder(0, 0, 0, 0));
                ((JComponent)((JWindow) component).getContentPane()).setBackground(new Color(TnColor.TRANSPARENT, true));
            }
            component.setBackground(new Color(TnColor.TRANSPARENT, true));
        }
        return component;
    }

    private void setColor(Container container)
    {
        for(int i = 0; i < container.getComponentCount(); i++)
        {
//            ((JComponent)container.getComponent(i)).setOpaque(false);
            ((JComponent)container.getComponent(i)).setBackground(new Color(TnColor.TRANSPARENT, true));
            ((JComponent)container.getComponent(i)).setBorder(new EmptyBorder(0, 0, 0, 0));
        }
    }
    
    /**
     * Component used to house window.
     */
    static class HeavyWeightWindow extends JWindow implements ModalExclude {
        HeavyWeightWindow(Window parent) {
            super(parent);
            setFocusableWindowState(false);
            setName("###overrideRedirect###");
            // Popups are typically transient and most likely won't benefit
            // from true double buffering.  Turn it off here.
//            getRootPane().setUseTrueDoubleBuffering(false);
            // Try to set "always-on-top" for the popup window.
            // Applets usually don't have sufficient permissions to do it.
            // In this case simply ignore the exception.
            try {
                setAlwaysOnTop(true);
            } catch (SecurityException se) {
                // setAlwaysOnTop is restricted,
                // the exception is ignored
            }
        }

        public void update(Graphics g) {
            paint(g);
        }

    public void show() {
        this.pack();
        super.show();
    }
    }


    /**
     * Used if no valid Window ancestor of the supplied owner is found.
     * <p>
     * PopupFactory uses this as a way to know when the Popup shouldn't
     * be cached based on the Window.
     */
    static class DefaultFrame extends Frame {
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seBasicListener.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.telenav.tnui.core.AbstractTnComponent;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 6, 2010
 */
public class J2seBasicListener implements MouseListener, MouseMotionListener, FocusListener, KeyListener
{
    protected AbstractTnComponent tnComponent;
    
    public J2seBasicListener(AbstractTnComponent tnComponent)
    {
        this.tnComponent = tnComponent;
    }
    
    public void setEventSource(AbstractTnComponent tnComponent)
    {
        this.tnComponent = tnComponent;
    }
    
    public void mouseClicked(MouseEvent e)
    {
        J2seUiEventHandler.onClick(this.tnComponent);
    }

    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent e)
    {
        tnComponent.requestFocus();
        
        if (tnComponent.getRoot() != null && tnComponent.getRoot().getRootContainer() != null)
            tnComponent.getRoot().getRootContainer().requestPaint();
    }

    public void mouseReleased(MouseEvent e)
    {

    }

    public void mouseDragged(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void mouseMoved(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void focusGained(FocusEvent e)
    {
        this.tnComponent.dispatchFocusChanged(e.getID() == FocusEvent.FOCUS_GAINED);
    }

    public void focusLost(FocusEvent e)
    {
        this.tnComponent.dispatchFocusChanged(e.getID() == FocusEvent.FOCUS_GAINED);
    }

    public void keyPressed(KeyEvent e)
    {
        J2seUiEventHandler.onKeyDown(this.tnComponent, e);
    }

    public void keyReleased(KeyEvent e)
    {
        J2seUiEventHandler.onKeyUp(this.tnComponent, e);
    }

    public void keyTyped(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnSizeChangListener.java
 *
 */
package com.telenav.tnui.core;

/**
 * Interface definition for a callback to be invoked when the size of a component changed. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 25, 2010
 */
public interface ITnSizeChangListener
{
    /**
     * This is called during layout when the size of this component has changed. If you were just added to the component
     * hierarchy, you're called with the old values of 0.
     * 
     * @param tnComponent the component which received this event.
     * @param w Current width of this component.
     * @param h Current height of this component.
     * @param oldw Old width of this component.
     * @param oldh Old height of this component. 
     */
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh);
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnTextChangeListener.java
 *
 */
package com.telenav.tnui.core;

/**
 * This listener receives notifications of changes in text field
 * 
 *@author wzhu (wzhu@telenav.cn)
 *@date 2010-8-23
 */
public interface ITnTextChangeListener
{
    /**
     * Callback invokes when the text changes
     * 
     * @param component {@link AbstractTnComponent} the component which receives the text change event.
     * @param text the current text of the component 
     */
    public void onTextChange(AbstractTnComponent component,String text);
}

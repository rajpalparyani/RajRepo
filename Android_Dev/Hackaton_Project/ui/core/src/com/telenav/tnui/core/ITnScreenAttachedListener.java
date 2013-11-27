/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnScreenAttachedListener.java
 *
 */
package com.telenav.tnui.core;

/**
 * Defines a listener for screen attach events.
 * <br />
 * Implementations of this interface may be registered and unregistered with Screen objects to be notified of UI engine
 * attached and detached events using
 * TnScreen.addScreenAttachedListener(ITnScreenAttachedListener) and
 * TnScreen.removeScreenAttachedListener(ITnScreenAttachedListener), respectively.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-30
 */
public interface ITnScreenAttachedListener
{
    public final static int DETTACHED = 0;
    
    public final static int BEFORE_ATTACHED = 2;
    
    public final static int AFTER_ATTACHED = 3;
    
    /**
     * Callback indicating a screen has been attached or detached from a UiEngine.
     * 
     * @param screen The screen that has changed state.
     * @param attached Whether it was attached (true) or detached (false).
     */
    public void onScreenUiEngineAttached(TnScreen screen, int attached);
}

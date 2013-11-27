/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IMapResizeViewListener.java
 *
 */
package com.telenav.ui.citizen.map;

/**
 *@author yning
 *@date 2012-12-26
 *Be used to notify app when map engine resize finished
 */
public interface IMapResizeViewListener
{
    //Notice that, this interface is call back for resize view, do not invoke resize view here!!!!!!!!!
    public void mapViewSizeChanged();
    
    //This interface is call back for egl surface changed, when it invoked, whether map engine has been resized can't be guaranteed.
    public void eglSizeChanged();
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractMapEngine.java
 *
 */
package com.telenav.map;

/**
 *@author jyxu(jyxu@telenav.cn)
 *@date 2010-11-17
 */
public abstract class AbstractMapEngine implements IMapEngine
{
    protected InteractionModeChangeListener listener;
    
    public abstract void setMapView(IMapView mapView);
    
    public abstract IMapView getMapView();
    
    public void setInteractionModeChangeListener(InteractionModeChangeListener listener)
    {
        this.listener = listener;
    }
}

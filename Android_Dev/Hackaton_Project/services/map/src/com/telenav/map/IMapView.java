/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMapView.java
 *
 */
package com.telenav.map;

/**
 * Represents the view seen in a mapping application (latitude and longitude for the center of the map, and the zoom and
 * rotation values).
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-9
 */
public interface IMapView
{
    /**
     * post the event to UI thread or OpenGl thread.
     * 
     * @param runnable
     */
    public void postMapUiEvent(Runnable runnable);

    /**
     * Request repaint the whole map.
     */
    public void requestRepaint();
}
 
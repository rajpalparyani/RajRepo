/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * INavSDKMapEventListener.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.List;

import com.telenav.navsdk.events.MapViewData.SelectedAnnotation;
import com.telenav.navsdk.events.MapViewData.SelectedTrafficIncident;

/**
 * @author yren
 * @date Oct 3, 2012
 */
public interface INavSdkMapEventListener
{
    // annotation click event
    public void handleClickAnnotation(String viewName, List<SelectedAnnotation> annotations);

    // annotation cancel click event
    public void handleTapNoAnnotation(String viewName);

    // annotation up event
    public void handleUpAnnotation(String viewName, List<SelectedAnnotation> annotations);

    // annotation down event
    public void handleDownAnnotation(String viewName, List<SelectedAnnotation> annotations);

    public void handleMapViewProjection(String viewName, double lat, double lon);

    public void handleClickIncident(String viewName, SelectedTrafficIncident incidents);

    public void handleGetZoomLevel(String viewName, float zoomLevel);
    
    public void handleMapViewResizeResponse(String viewName);
}

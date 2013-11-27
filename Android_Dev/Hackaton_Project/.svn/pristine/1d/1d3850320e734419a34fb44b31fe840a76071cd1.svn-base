package com.telenav.ui.citizen.map;

import com.telenav.navsdk.events.MapViewData.SelectedTrafficIncident;
import com.telenav.tnui.core.TnUiEvent;

public interface IMapUIEventListener extends IMapResizeViewListener
{
//    public void updateMapContainerEvent(MapContainer container, int event);
    public void tapNoAnnotation();
    public void handleTouchEventOnMap(MapContainer container, TnUiEvent uiEvent);
    public void handleMapRgc(double latitude, double longitude);
    public void onPinchEvent();
    public void onPinchEnd();
    public void handleZoomOut();
    public void onDoubleTap();
    public void handleClickTrafficIncident(SelectedTrafficIncident incident);
    
    public static final int EVENT_CREATED = 1;
    public static final int EVENT_RESIZED = 2;
}

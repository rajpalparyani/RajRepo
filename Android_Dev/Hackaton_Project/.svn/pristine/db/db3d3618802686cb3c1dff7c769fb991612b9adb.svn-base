/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkMapProxyHelper.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.helper;

import java.util.Vector;

import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.logger.Logger;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewAnnotationsTouched;
import com.telenav.navsdk.events.MapViewEvents.MapViewChangeConfigRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewCloseRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewDisableAllTurnArrowsRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewDisableTurnArrowsRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewDisplayRoutesRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewEnableAllTurnArrowsRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewEnableTurnArrowsRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewFollowVehicleRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewGetPropertiesRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewGetPropertiesResponse;
import com.telenav.navsdk.events.MapViewEvents.MapViewLoadAssetRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewLookAtAnnotationsRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewLookAtRegionRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewLookAtRoutesRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewLookAtViewpointRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewMoveAnnotationsRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewOpenRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewOperateAnnotationsRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewProjectionRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewProjectionResponse;
import com.telenav.navsdk.events.MapViewEvents.MapViewResizeRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewResizeResponse;
import com.telenav.navsdk.events.MapViewEvents.MapViewScreenshot;
import com.telenav.navsdk.events.MapViewEvents.MapViewScreenshotRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewSetPropertiesRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUnloadAssetRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewZoomInRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewZoomOutRequest;
import com.telenav.navsdk.services.MapViewListener;
import com.telenav.navsdk.services.MapViewServiceProxy;

/**
 * @author hchai
 * @date 2012-4-9
 */
public class NavSdkMapProxyHelper implements MapViewListener, INavSdkProxyConstants
{
    private static NavSdkMapProxyHelper instance;

    private MapViewServiceProxy serverProxy;

    private Vector<NavSdkMapProxy> listeners = new Vector<NavSdkMapProxy>();

    private NavSdkMapProxyHelper()
    {
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavSdkMapProxyHelper();
            instance.setEventBus(bus);
        }
    }

    public static NavSdkMapProxyHelper getInstance()
    {
        return instance;
    }

    public void registerRequestCallback(NavSdkMapProxy proxy)
    {
        if (!listeners.contains(proxy))
        {
            listeners.add(proxy);
        }
    }

    public void unregisterRequestCallback(NavSdkMapProxy proxy)
    {
        listeners.remove(proxy);
    }

    private void setEventBus(EventBus bus)
    {
        serverProxy = new MapViewServiceProxy(bus);
        serverProxy.addListener(this);

    }

    public void onMapViewScreenshot(MapViewScreenshot event)
    {
        // TODO Auto-generated method stub

    }

    public void addListener(MapViewListener listener)
    {
        this.serverProxy.addListener(listener);
    }

    public void mapViewOpen(MapViewOpenRequest event)
    {
        this.serverProxy.mapViewOpen(event);
    }

    public void mapViewClose(MapViewCloseRequest event)
    {
        this.serverProxy.mapViewClose(event);
    }

    public void mapViewLookAtViewpoint(MapViewLookAtViewpointRequest event)
    {
        this.serverProxy.mapViewLookAtViewpoint(event);
    }
    
    public void mapViewLookAtRegion(MapViewLookAtRegionRequest event)
    {
        this.serverProxy.mapViewLookAtRegion(event);
    }

    public void mapViewSetProperties(MapViewSetPropertiesRequest event)
    {
        this.serverProxy.mapViewSetProperties(event);
    }

    public void mapViewFollowVehicle(MapViewFollowVehicleRequest event)
    {
        this.serverProxy.mapViewFollowVehicle(event);
    }

    public void mapViewLookAtRoutes(MapViewLookAtRoutesRequest event)
    {
        this.serverProxy.mapViewLookAtRoutes(event);
    }

    public void mapViewDisplayRoutes(MapViewDisplayRoutesRequest event)
    {
        this.serverProxy.mapViewDisplayRoutes(event);
    }

    public void mapViewZoomIn(MapViewZoomInRequest event)
    {
        this.serverProxy.mapViewZoomIn(event);
    }

    public void mapViewZoomOut(MapViewZoomOutRequest event)
    {
        this.serverProxy.mapViewZoomOut(event);
    }

    public void mapViewLookAtAnnotations(MapViewLookAtAnnotationsRequest event)
    {
        this.serverProxy.mapViewLookAtAnnotations(event);
    }

    public void mapViewEnableTurnArrows(MapViewEnableTurnArrowsRequest event)
    {
        this.serverProxy.mapViewEnableTurnArrows(event);
    }

    public void mapViewDisableTurnArrows(MapViewDisableTurnArrowsRequest event)
    {
        this.serverProxy.mapViewDisableTurnArrows(event);        
    }

    public void mapViewEnableAllTurnArrows(MapViewEnableAllTurnArrowsRequest event)
    {
        this.serverProxy.mapViewEnableAllTurnArrows(event);        
    }

    public void mapViewDisableAllTurnArrows(MapViewDisableAllTurnArrowsRequest event)
    {
        this.serverProxy.mapViewDisableAllTurnArrows(event);        
    }

    public void mapViewLoadAsset(MapViewLoadAssetRequest event)
    {
        this.serverProxy.mapViewLoadAsset(event);        
    }

    public void mapViewUnloadAsset(MapViewUnloadAssetRequest event)
    {
        this.serverProxy.mapViewUnloadAsset(event);        
    }

    public void mapViewAddAnnotation(MapViewAddAnnotationRequest event)
    {
        this.serverProxy.mapViewAddAnnotation(event);        
    }

    public void mapViewOperateAnnotations(MapViewOperateAnnotationsRequest event)
    {
        this.serverProxy.mapViewOperateAnnotations(event);        
    }
    
    public void mapViewOperateAllAnnotations(MapViewOperateAnnotationsRequest[] events)
    {
        for (int i = 0; i < events.length; i++)
        {
            this.serverProxy.mapViewOperateAnnotations(events[i]);
        }
    }

    public void mapViewScreenshot(MapViewScreenshotRequest event)
    {
        this.serverProxy.mapViewScreenshot(event);        
    }

    public void mapViewChangeConfiguration(MapViewChangeConfigRequest event)
    {
        this.serverProxy.mapViewChangeConfig(event);        
    }
    
    public void mapViewResize(MapViewResizeRequest event)
    {
        this.serverProxy.mapViewResize(event);
    }

    public void mapViewProjection(MapViewProjectionRequest event)
    {
        this.serverProxy.mapViewProjection(event);
    }
    
    public void mapViewGetPropertiesRequest(MapViewGetPropertiesRequest event)
    {
        serverProxy.mapViewGetProperties(event);
    }
    
    public void onMapViewProjectionResponse(MapViewProjectionResponse event)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            NavSdkMapProxy proxy = listeners.get(i);
            if (proxy != null)
            {
                proxy.handleMapViewProjection(event);
            }
        }
    }

    public void onMapViewGetPropertiesResponse(MapViewGetPropertiesResponse event)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            NavSdkMapProxy proxy = listeners.get(i);
            if (proxy != null)
            {
                proxy.handleGetZoomLevel(event.getViewName(), event.getZoomLevel());
            }
        }
    }

    public void onMapViewAnnotationsTouched(MapViewAnnotationsTouched event)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            NavSdkMapProxy proxy = listeners.get(i);
            if (proxy != null)
            {
                proxy.handleAnnotationTouched(event);
            }
        }
    }
    
    public void updateAnnotation(MapViewUpdateAnnotationRequest event)
    {
        this.serverProxy.mapViewUpdateAnnotation(event);
    }
    
    public void moveAnnotations(MapViewMoveAnnotationsRequest event)
    {
        this.serverProxy.mapViewMoveAnnotations(event);
    }

    public void onMapViewResizeResponse(MapViewResizeResponse event)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "OGLMapService-GetSnapshot --- onMapViewResizeResponse x: " + event.getX()
                    + " y: " + event.getY() + " width: " + event.getWidth() + " height: " + event.getHeight());
        }
        for (int i = 0; i < listeners.size(); i++)
        {
            NavSdkMapProxy proxy = listeners.get(i);
            if (proxy != null)
            {
                proxy.handleResizeResponse("");
            }
        }
    }
}

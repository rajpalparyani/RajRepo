/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkMapProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.google.protobuf.ByteString;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkMapProxyHelper;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.navsdk.events.MapViewData;
import com.telenav.navsdk.events.MapViewData.AnnotationLocation;
import com.telenav.navsdk.events.MapViewData.AnnotationOperation;
import com.telenav.navsdk.events.MapViewData.AnnotationTouchState;
import com.telenav.navsdk.events.MapViewData.MapViewConfigObject;
import com.telenav.navsdk.events.MapViewData.RenderingMode;
import com.telenav.navsdk.events.MapViewData.SelectedTrafficIncident;
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
import com.telenav.navsdk.events.MapViewEvents.MapViewScreenshotRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewSetPropertiesRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUnloadAssetRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewZoomInRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewZoomOutRequest;
import com.telenav.navsdk.events.PointOfInterestData.Location;
import com.telenav.navsdk.services.MapViewListener;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.citizen.map.AbstractAnnotation;
import com.telenav.ui.citizen.map.IMapContainerConstants;

/**
 * @author hchai
 * @date 2012-4-9
 */
public class NavSdkMapProxy
{

    private NavSdkMapProxyHelper helper;

    private Vector<INavSdkMapEventListener> listeners = new Vector<INavSdkMapEventListener>();

    private String[] configFiles;

    public NavSdkMapProxy()
    {
        helper = NavSdkMapProxyHelper.getInstance();

        helper.registerRequestCallback(this);
    }

    public NavSdkMapProxy(INavSdkMapEventListener listener)
    {
        this();
        listeners.add(listener);
    }

    public static final int RENDERING_MODE_2D_NORTH_UP = RenderingMode.RenderingMode_2DNorthUp_VALUE;

    public static final int RENDERING_MODE_2D_HEAD_UP = RenderingMode.RenderingMode_2DHeadUp_VALUE;

    public static final int RENDERING_MODE_3D_NORTH_UP = RenderingMode.RenderingMode_3DNorthUp_VALUE;

    public static final int RENDERING_MODE_3D_HEAD_UP = RenderingMode.RenderingMode_3DHeadUp_VALUE;

    public void addMapViewListener(MapViewListener listener)
    {
        helper.addListener(listener);
    }

    public void initMapConfig(String[] configFiles)
    {
        this.configFiles = configFiles;
    }

    public void open(String viewName, int x, int y, int width, int height)
    {
        MapViewOpenRequest.Builder builder = MapViewOpenRequest.newBuilder();
        
        float dpi = 0.0f;
        AbstractTnUiHelper uiHelper = (AbstractTnUiHelper)AbstractTnUiHelper.getInstance(); 
        dpi = uiHelper.getDensity() * 160;
        if (dpi <= 0)
            dpi = 160.0f;
        
        Logger.log(Logger.INFO, this.getClass().getName(), "createView for dpi = " + dpi);
        
        builder.setViewName(viewName).setX(x).setY(y).setWidth(width).setHeight(height).setDotsPerInch(dpi);
        for (int i = 0; i < configFiles.length; i++)
        {
            if (configFiles[i].trim().length() == 0)
            {
                continue;
            }
            MapViewConfigObject.Builder configBuilder = MapViewConfigObject.newBuilder();
            configBuilder.setFile(configFiles[i]);
            builder.addConfigurations(configBuilder);
        }

        helper.mapViewOpen(builder.build());
    }

    public void close(String viewName)
    {
        MapViewCloseRequest.Builder builder = MapViewCloseRequest.newBuilder();
        builder.setViewName(viewName);
        helper.mapViewClose(builder.build());
    }

    public void lookAt(String viewName, double lat, double lon)
    {
        MapViewLookAtViewpointRequest.Builder builder = MapViewLookAtViewpointRequest.newBuilder();
        builder.setViewName(viewName).setLocation(Location.newBuilder().setLatitude(lat).setLongitude(lon));
        helper.mapViewLookAtViewpoint(builder.build());
    }

    public void setMapPanAndZoom(String viewName)
    {
        MapViewLookAtViewpointRequest.Builder builder = MapViewLookAtViewpointRequest.newBuilder();
        builder.setViewName(viewName);
        helper.mapViewLookAtViewpoint(builder.build());
    }

    public void lookAt(String viewName, double lat, double lon, double altitude)
    {
        MapViewLookAtViewpointRequest.Builder lookAtReq = MapViewLookAtViewpointRequest.newBuilder();
        lookAtReq.setViewName(viewName).setLocation(
            Location.newBuilder().setLatitude(lat).setLongitude(lon).setAltitude(altitude));

        helper.mapViewLookAtViewpoint(lookAtReq.build());
    }

    public void setRenderingMode(String viewName, int mode)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setRenderingMode(RenderingMode.valueOf(mode));
        if (mode == NavSdkMapProxy.RENDERING_MODE_3D_HEAD_UP)
        {
            builder.setVerticalOffset(-0.6f);
        }
        else
        {
            builder.setVerticalOffset(0.0f);
        }
        helper.mapViewSetProperties(builder.build());
    }

    public void setMapVerticalOffset(String viewName, float vertical)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setVerticalOffset(vertical);
        helper.mapViewSetProperties(builder.build());
    }

    public void setZoom(String viewName, float zoomLevel)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setZoomLevel(zoomLevel);
        helper.mapViewSetProperties(builder.build());
    }

    public void setSateliteEnabled(String viewName, boolean enable)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setSatelliteImagesEnabled(enable);
        helper.mapViewSetProperties(builder.build());
    }

    public void setTrafficEnabled(String viewName, boolean enable)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setTrafficEnabled(enable);
        helper.mapViewSetProperties(builder.build());
    }

    public void setTrafficCamerasEnabled(String viewName, boolean enable)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setTrafficCamerasEnabled(enable);
        helper.mapViewSetProperties(builder.build());
    }

    public void showMapLayer(String viewName, boolean isShowTrafficOverlay, boolean isShowSatelliteOverlay,
            boolean isShowTrafficCameraOverlay, boolean isShowIncident)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setSatelliteImagesEnabled(isShowSatelliteOverlay);
        builder.setTrafficEnabled(isShowTrafficOverlay);
        builder.setTrafficCamerasEnabled(isShowTrafficCameraOverlay);
        builder.setSpeedTrapEnabled(isShowTrafficCameraOverlay);
        builder.setTrafficIncidentsEnabled(isShowIncident);
        helper.mapViewSetProperties(builder.build());
    }

    public void setAdiEnabled(String viewName, boolean enable)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setShowAutoDeviationIndicator(enable);
        helper.mapViewSetProperties(builder.build());
    }

    public void followVehicle(String viewName)
    {
        MapViewFollowVehicleRequest.Builder builder = MapViewFollowVehicleRequest.newBuilder();
        builder.setViewName(viewName).setMargin(0.1f);
        helper.mapViewFollowVehicle(builder.build());
    }

    public void lookAtRoutes(String viewName, Vector routeNames, boolean isDecimatedRoute)
    {
        MapViewLookAtRoutesRequest.Builder builder = MapViewLookAtRoutesRequest.newBuilder();
        builder.setViewName(viewName).setMargin(0.1f);
        for (int i = 0; i < routeNames.size(); ++i)
        {
            if (isDecimatedRoute)
            {
                builder.addRouteNames(routeNames.get(i).toString() + (i == 0 ? "Selected" : "Unselected"));
            }
            else
            {
                builder.addRouteNames(routeNames.get(i).toString());
            }
        }
        // builder.addRouteNames(routeNames.get(0).toString()).addRouteNames(routeNames.get(1).toString()).addRouteNames(routeNames.get(2).toString());
        // builder.addAllRouteNames(routeNames);
        helper.mapViewLookAtRoutes(builder.build());
    }

    public void lookAtBetterRoutes(String viewName, Vector routeNames, String newRoute)
    {
        MapViewLookAtRoutesRequest.Builder builder = MapViewLookAtRoutesRequest.newBuilder();
        builder.setViewName(viewName).setMargin(0.1f);
        for (int i = 0; i < routeNames.size(); ++i)
        {
            builder.addRouteNames(routeNames.get(i).toString()
                    + (routeNames.get(i).toString().equals(newRoute) ? "" : "Decimated"));
        }
        helper.mapViewLookAtRoutes(builder.build());
    }

    public void displayRoutes(String viewName, Vector routeNames, String highlightRoute, boolean isDecimatedRoute)
    {
        MapViewDisplayRoutesRequest.Builder builder = MapViewDisplayRoutesRequest.newBuilder();
        builder.setViewName(viewName);
        if (routeNames != null)
        {
            for (int i = 0; i < routeNames.size(); ++i)
            {
                if (isDecimatedRoute)
                {
                    builder.addRouteNames(routeNames.get(i).toString()
                            + (routeNames.get(i).toString().equals(highlightRoute) ? "Selected" : "Unselected"));
                }
                else
                {
                    builder.addRouteNames(routeNames.get(i).toString());
                }
            }
        }
        // builder.addAllRouteNames(routeNames);
        if (highlightRoute != null && highlightRoute.trim().length() > 0 && isDecimatedRoute)
        {
            builder.setHighlightRoute(highlightRoute + "Selected");
        }
        helper.mapViewDisplayRoutes(builder.build());
    }

    /** display the result after check for better routes */
    public void displayBetterRoutes(String viewName, Vector routeNames, String newRoute)
    {
        MapViewDisplayRoutesRequest.Builder builder = MapViewDisplayRoutesRequest.newBuilder();
        builder.setViewName(viewName);
        if (routeNames != null)
        {
            for (int i = 0; i < routeNames.size(); ++i)
            {
                builder.addRouteNames(routeNames.get(i).toString()
                        + (routeNames.get(i).toString().equals(newRoute) ? "" : "Decimated"));
            }
        }
        helper.mapViewDisplayRoutes(builder.build());
    }

    public void clearRoutes(String viewName)
    {
        displayRoutes(viewName, null, "", false);
    }

    public void zoomIn(String viewName)
    {
        MapViewZoomInRequest.Builder builder = MapViewZoomInRequest.newBuilder();
        builder.setViewName(viewName);
        helper.mapViewZoomIn(builder.build());
    }

    public void zoomOut(String viewName)
    {
        MapViewZoomOutRequest.Builder builder = MapViewZoomOutRequest.newBuilder();
        builder.setViewName(viewName);
        helper.mapViewZoomOut(builder.build());
    }

    public void lookAtAnnotations(String viewName, Vector names, String centerAnnotationName)
    {
        MapViewLookAtAnnotationsRequest.Builder builder = MapViewLookAtAnnotationsRequest.newBuilder();
        builder.setViewName(viewName);
        if (centerAnnotationName != null && centerAnnotationName.trim().length() > 0)
        {
            builder.setCenterAnnotationName(centerAnnotationName);
        }
        
        for (int i = 0; i < names.size(); i++)
        {
            builder.addAnnotationNames((String) (names.elementAt(i)));
        }
        // builder.setAnnotationNames(index, name);
        builder.setMargin(0.07f);
        helper.mapViewLookAtAnnotations(builder.build());
    }

    public void enableTurnArrows(String viewName, String routeName, int segmentIndex, int segmentValue)
    {
        MapViewEnableTurnArrowsRequest.Builder builder = MapViewEnableTurnArrowsRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setRouteName(routeName);
        builder.addSegmentIndexes(segmentIndex);
        helper.mapViewEnableTurnArrows(builder.build());
    }

    public void disableTurnArrows(String viewName, String routeName, int segmentIndex, int segmentValue)
    {
        MapViewDisableTurnArrowsRequest.Builder builder = MapViewDisableTurnArrowsRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setRouteName(routeName);
        builder.addSegmentIndexes(segmentIndex);
        helper.mapViewDisableTurnArrows(builder.build());
    }

    public void enableAllTurnArrows(String viewName, String routeName)
    {
        MapViewEnableAllTurnArrowsRequest.Builder builder = MapViewEnableAllTurnArrowsRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setRouteName(routeName);
        helper.mapViewEnableAllTurnArrows(builder.build());
    }

    public void disableAllTurnArrows(String viewName, String routeName)
    {
        MapViewDisableAllTurnArrowsRequest.Builder builder = MapViewDisableAllTurnArrowsRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setRouteName(routeName);
        helper.mapViewDisableAllTurnArrows(builder.build());
    }

    public void loadAsset(String viewName, int width, int height, ByteString data, String assetName)
    {
        MapViewLoadAssetRequest.Builder builder = MapViewLoadAssetRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setImageWidth(width);
        builder.setImageHeight(height);
        builder.setAssetData(data);
        builder.setAssetName(assetName);
        helper.mapViewLoadAsset(builder.build());
    }

    public void loadAsset(String viewName, int width, int height, String fileName, String assetName)
    {
        MapViewLoadAssetRequest.Builder builder = MapViewLoadAssetRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setImageWidth(width);
        builder.setImageHeight(height);
        builder.setAssetFileName(fileName);
        builder.setAssetName(assetName);
        helper.mapViewLoadAsset(builder.build());
    }

    public void unloadAsset(String viewName, String assetName)
    {
        MapViewUnloadAssetRequest.Builder builder = MapViewUnloadAssetRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setAssetName(assetName);
        helper.mapViewUnloadAsset(builder.build());
    }

    public void addAnnotation(String viewName)
    {
        MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest.newBuilder();
        builder.setViewName(viewName);
        // builder.setAnnotationName(arg0);
        // builder.setAssetName(arg0);
        // builder.setClickable(arg0);
        // builder.setLocation(arg0);
        // builder.setLocation(arg0);
        // builder.setPivotX(arg0);
        // builder.setPivotY(arg0);
        // builder.setPositionBottom(arg0);
        // builder.setPositionLeft(arg0);
        // builder.setPositionRight(arg0);
        // builder.setStyle(arg0);
        // builder.setVisible(arg0);
        // builder.setZorder(arg0);
        helper.mapViewAddAnnotation(builder.build());
    }

    public void addAnnotation(MapViewAddAnnotationRequest.Builder builder)
    {
        helper.mapViewAddAnnotation(builder.build());
    }

    public void removeAnnotationByName(String viewName, String annotationName)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Remove);
        builder.setViewName(viewName);
        builder.addAnnotationNames(annotationName);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void removeAnnotationsByLayer(String viewName, int zOder)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Remove);
        builder.setViewName(viewName);
        builder.setZorder(zOder);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void removeAllAnnotations(String viewName)
    {
        MapViewOperateAnnotationsRequest.Builder poiLayerBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        poiLayerBuilder.setViewName(viewName);
        poiLayerBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER);
        poiLayerBuilder.setOperation(AnnotationOperation.AnnotationOperation_Remove);

        MapViewOperateAnnotationsRequest.Builder userDefinedBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        userDefinedBuilder.setViewName(viewName);
        userDefinedBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        userDefinedBuilder.setOperation(AnnotationOperation.AnnotationOperation_Remove);

        MapViewOperateAnnotationsRequest.Builder incidentCustomBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        incidentCustomBuilder.setViewName(viewName);
        incidentCustomBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_TRAFFIC_INCIDENT_CUSTOM);
        incidentCustomBuilder.setOperation(AnnotationOperation.AnnotationOperation_Remove);

        helper.mapViewOperateAllAnnotations(new MapViewOperateAnnotationsRequest[]
        { poiLayerBuilder.build(), userDefinedBuilder.build(), incidentCustomBuilder.build() });
    }

    public void enableAnnotationByName(String viewName, String annotationName)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Enable);
        builder.setViewName(viewName);
        builder.addAnnotationNames(annotationName);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void enableAnnotationsByLayer(String viewName, int zOder)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Enable);
        builder.setViewName(viewName);
        builder.setZorder(zOder);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void enableAllAnnotations(String viewName)
    {
        MapViewOperateAnnotationsRequest.Builder poiLayerBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        poiLayerBuilder.setViewName(viewName);
        poiLayerBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER);
        poiLayerBuilder.setOperation(AnnotationOperation.AnnotationOperation_Enable);

        MapViewOperateAnnotationsRequest.Builder userDefinedBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        userDefinedBuilder.setViewName(viewName);
        userDefinedBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        userDefinedBuilder.setOperation(AnnotationOperation.AnnotationOperation_Enable);
        helper.mapViewOperateAllAnnotations(new MapViewOperateAnnotationsRequest[]
        { poiLayerBuilder.build(), userDefinedBuilder.build() });
    }

    public void disableAnnotationByName(String viewName, String annotationName)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Disable);
        builder.setViewName(viewName);
        builder.addAnnotationNames(annotationName);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void disableAnnotationsByLayer(String viewName, int zOder)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Disable);
        builder.setViewName(viewName);
        builder.setZorder(zOder);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void disableAllAnnotations(String viewName)
    {
        MapViewOperateAnnotationsRequest.Builder poiLayerBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        poiLayerBuilder.setViewName(viewName);
        poiLayerBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER);
        poiLayerBuilder.setOperation(AnnotationOperation.AnnotationOperation_Disable);

        MapViewOperateAnnotationsRequest.Builder userDefinedBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        userDefinedBuilder.setViewName(viewName);
        userDefinedBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        userDefinedBuilder.setOperation(AnnotationOperation.AnnotationOperation_Disable);
        helper.mapViewOperateAllAnnotations(new MapViewOperateAnnotationsRequest[]
        { poiLayerBuilder.build(), userDefinedBuilder.build() });
    }

    public void hideAnnotationByName(String viewName, String annotationName)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Hide);
        builder.setViewName(viewName);
        builder.addAnnotationNames(annotationName);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void hideAnnotationsByLayer(String viewName, int zOder)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Hide);
        builder.setViewName(viewName);
        builder.setZorder(zOder);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void hideAllAnnotations(String viewName)
    {
        MapViewOperateAnnotationsRequest.Builder poiLayerBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        poiLayerBuilder.setViewName(viewName);
        poiLayerBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER);
        poiLayerBuilder.setOperation(AnnotationOperation.AnnotationOperation_Hide);

        MapViewOperateAnnotationsRequest.Builder userDefinedBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        userDefinedBuilder.setViewName(viewName);
        userDefinedBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        userDefinedBuilder.setOperation(AnnotationOperation.AnnotationOperation_Hide);
        helper.mapViewOperateAllAnnotations(new MapViewOperateAnnotationsRequest[]
        { poiLayerBuilder.build(), userDefinedBuilder.build() });
    }

    public void displayAnnotationByName(String viewName, String annotationName)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Unhide);
        builder.setViewName(viewName);
        builder.addAnnotationNames(annotationName);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void displayAnnotationsByLayer(String viewName, int zOder)
    {
        MapViewOperateAnnotationsRequest.Builder builder = MapViewOperateAnnotationsRequest.newBuilder();
        builder.setOperation(AnnotationOperation.AnnotationOperation_Unhide);
        builder.setViewName(viewName);
        builder.setZorder(zOder);

        helper.mapViewOperateAnnotations(builder.build());
    }

    public void displayAllAnnotations(String viewName)
    {
        MapViewOperateAnnotationsRequest.Builder poiLayerBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        poiLayerBuilder.setViewName(viewName);
        poiLayerBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER);
        poiLayerBuilder.setOperation(AnnotationOperation.AnnotationOperation_Unhide);

        MapViewOperateAnnotationsRequest.Builder userDefinedBuilder = MapViewOperateAnnotationsRequest.newBuilder();
        userDefinedBuilder.setViewName(viewName);
        userDefinedBuilder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        userDefinedBuilder.setOperation(AnnotationOperation.AnnotationOperation_Unhide);
        helper.mapViewOperateAllAnnotations(new MapViewOperateAnnotationsRequest[]
        { poiLayerBuilder.build(), userDefinedBuilder.build() });
    }

    public void screenshot(String viewName, Location location, float zoom, int width, int height)
    {
        MapViewScreenshotRequest.Builder builder = MapViewScreenshotRequest.newBuilder();
        builder.setViewName(viewName);
        if (location != null)
        {
            builder.setLocation(location);
        }
        builder.setZoomLevel(zoom).setWidth(width).setHeight(height);
        helper.mapViewScreenshot(builder.build());
    }

    // TODO YRen, need to implement later by the latest navsdk interface
    // public void handleAnnotationClicked(MapViewAnnotationClicked event)
    // {
    // if(event == null)
    // {
    // return;
    // }
    // for (int i = 0; i < this.listeners.size(); i++)
    // {
    // INavSDKMapEventListener tmpListener = listeners.elementAt(i);
    // tmpListener.handleClickOnAnnotation(event.getViewName(), event.getAnnotationName(), event.getX() , event.getY());
    // }
    // }

    public void handleMapViewProjection(MapViewProjectionResponse event)
    {
        if (event == null || event.getLocationsCount() < 1)
        {
            return;
        }

        com.telenav.navsdk.events.PointOfInterestData.Location location = event.getLocations(0);

        double lat = location.getLatitude();
        double lon = location.getLongitude();
        for (int i = 0; i < this.listeners.size(); i++)
        {
            INavSdkMapEventListener tmpListener = listeners.elementAt(i);
            tmpListener.handleMapViewProjection(event.getViewName(), lat, lon);
        }
    }

    public void handleAnnotationTouched(MapViewAnnotationsTouched event)
    {
        if (event == null)
        {
            return;
        }

        boolean isTapNoAnnotation = false;

        /**
         * The touch event state touch state, press down/up/clicked
         */
        int action = event.getState().getNumber();
        String viewName = event.getViewName();
        if (event.getAnnotationsCount() > 0)
        {
            isTapNoAnnotation = false;
        }
        else
        {
            isTapNoAnnotation = true;
        }
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "handleNavSdkAnnotationTouched getAnnotationsCount : " + event.getAnnotationsCount());
            Logger.log(Logger.INFO, this.getClass().getName(), "handleNavSdkAnnotationTouched isTapNoAnnotation : " + isTapNoAnnotation);
        }
        
        for (int i = 0; i < this.listeners.size(); i++)
        {
            INavSdkMapEventListener tmpListener = listeners.elementAt(i);
            if (isTapNoAnnotation)
            {
               
                tmpListener.handleTapNoAnnotation(event.getViewName());
                if (action == AnnotationTouchState.AnnotationTouchState_Clicked_VALUE)
                {
                    int incidentCount = event.getIncidentsCount();
                    if (incidentCount > 0)
                    {
                        SelectedTrafficIncident tempIncident = event.getIncidentsList().get(incidentCount - 1);
                        tmpListener.handleClickIncident(viewName, tempIncident);
                    }
                }
            }
            else
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "handleNavSdkAnnotationTouched action : " + action);
                }
                
                switch (action)
                {
                    case AnnotationTouchState.AnnotationTouchState_TouchDown_VALUE:
                    {
                        if (event.getAnnotationsCount() > 0)
                        {
                            tmpListener.handleDownAnnotation(viewName, event.getAnnotationsList());
                        }
                        break;
                    }
                    case AnnotationTouchState.AnnotationTouchState_TouchUp_VALUE:
                    {
                        if (event.getAnnotationsCount() > 0)
                        {
                            tmpListener.handleUpAnnotation(viewName, event.getAnnotationsList());
                        }
                        break;
                    }
                    case AnnotationTouchState.AnnotationTouchState_Clicked_VALUE:
                    {
                        if (event.getAnnotationsCount() > 0)
                        {
                            tmpListener.handleClickAnnotation(viewName, event.getAnnotationsList());
                        }
                        break;
                    }
                }
            }
        }
    }

    public void handleGetZoomLevel(String viewName, float zoomLevel)
    {
        for (int i = 0; i < this.listeners.size(); i++)
        {
            INavSdkMapEventListener tmpListener = listeners.elementAt(i);
            tmpListener.handleGetZoomLevel(viewName, zoomLevel);
        }
    }

    public void mapViewResize(String viewName, int x, int y, int width, int height)
    {
        MapViewResizeRequest.Builder builder = MapViewResizeRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setX(x);
        builder.setY(y);
        builder.setWidth(width);
        builder.setHeight(height);
        helper.mapViewResize(builder.build());
    }

    public void mapViewProjection(String viewName, int x, int y)
    {
        MapViewProjectionRequest.Builder projectionRequestBuilder = MapViewProjectionRequest.newBuilder();
        MapViewData.MapViewScreenPoint.Builder screenPointBuilder = MapViewData.MapViewScreenPoint.newBuilder();
        screenPointBuilder.setX(x);
        screenPointBuilder.setY(y);
        MapViewData.MapViewScreenPoint screenPoint = screenPointBuilder.build();
        projectionRequestBuilder.setViewName(viewName);
        projectionRequestBuilder.addPoints(screenPoint);
        helper.mapViewProjection(projectionRequestBuilder.build());
    }

    public void removeListener(INavSdkMapEventListener listener)
    {
        this.listeners.remove(listener);
    }

    public void clearListeners()
    {
        this.listeners.clear();
    }

    public void addListener(INavSdkMapEventListener listener)
    {
        this.listeners.add(listener);
    }

    public void changeConfiguration(String viewName, String fileName)
    {
        MapViewChangeConfigRequest.Builder builder = MapViewChangeConfigRequest.newBuilder();
        MapViewConfigObject.Builder configBuilder = MapViewConfigObject.newBuilder();
        configBuilder.setFile(fileName);
        builder.addConfigFiles(configBuilder.build());
        builder.setViewName(viewName);
        this.helper.mapViewChangeConfiguration(builder.build());
    }

    public void changedConfiguration(String viewName, byte[] content)
    {
        MapViewChangeConfigRequest.Builder builder = MapViewChangeConfigRequest.newBuilder();
        MapViewConfigObject.Builder configBuilder = MapViewConfigObject.newBuilder();
        configBuilder.setContent(ByteString.copyFrom(content));
        builder.addConfigFiles(configBuilder.build());
        builder.setViewName(viewName);
        this.helper.mapViewChangeConfiguration(builder.build());
    }

    public void seTransitionTime(String viewName, float time)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setTransitionTime(time);
        helper.mapViewSetProperties(builder.build());
    }

    public void getZoomLevel(String viewName)
    {
        MapViewGetPropertiesRequest.Builder builder = MapViewGetPropertiesRequest.newBuilder();
        builder.setViewName(viewName);
        helper.mapViewGetPropertiesRequest(builder.build());
    }

    public void updateAnnotation(MapViewUpdateAnnotationRequest.Builder builder)
    {
        helper.updateAnnotation(builder.build());
    }
    
    public void setMapDeclination(String viewName, float declination)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName).setDeclination(declination);
        helper.mapViewSetProperties(builder.build());
    }
    
    public void lookAtRegion(String viewName, double centerLat, double denterLon, double deltaLat, double deltaLon)
    {
        MapViewLookAtRegionRequest.Builder builder = MapViewLookAtRegionRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setLatitudeDelta(deltaLat);
        builder.setLongitudeDelta(deltaLon);
        builder.setPreferHighZoom(true);
        Location.Builder locationBuilder = Location.newBuilder();
        locationBuilder.setLatitude(centerLat);
        locationBuilder.setLongitude(denterLon);
        builder.setLocation(locationBuilder.build());
        helper.mapViewLookAtRegion(builder.build());
    }
    
    public void enableRadius(String viewName, boolean isEnable)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setShowBlueCircle(isEnable);
        helper.mapViewSetProperties(builder.build());
    }

    public void enableCoarseAnimation(String viewName, boolean isEnable)
    {
        MapViewSetPropertiesRequest.Builder builder = MapViewSetPropertiesRequest.newBuilder();
        builder.setViewName(viewName);
        builder.setEnableCoarseAnimation(isEnable);
        helper.mapViewSetProperties(builder.build());
    }
    
    public void handleResizeResponse(String viewName)
    {
        for (int i = 0; i < this.listeners.size(); i++)
        {
            INavSdkMapEventListener tmpListener = listeners.elementAt(i);
            tmpListener.handleMapViewResizeResponse(viewName);
        }
    }
    
    public void moveAnnotations(Hashtable<AbstractAnnotation, TnLocation> annotationLocationMapping, String viewName)
    {
        if(annotationLocationMapping.isEmpty())
        {
            return;
        }
        
        MapViewMoveAnnotationsRequest.Builder builder = MapViewMoveAnnotationsRequest.newBuilder();
        
        Enumeration<AbstractAnnotation> keys = annotationLocationMapping.keys();
        
        builder.setViewName(viewName);
        Vector<AnnotationLocation> annotationLocations = new Vector<AnnotationLocation>();
        while(keys.hasMoreElements())
        {
            AbstractAnnotation annotation = keys.nextElement();
            TnLocation location = annotationLocationMapping.get(annotation);
            
            if(location == null)
            {
                continue;
            }
            
            AnnotationLocation.Builder annotationLocationBuilder = AnnotationLocation.newBuilder();
            Location.Builder locationBuilder = Location.newBuilder();
            locationBuilder.setLatitude((double)location.getLatitude() / 100000.0d);
            locationBuilder.setLongitude((double)location.getLongitude() / 100000.0d);
            annotationLocationBuilder.setAnnotationName(annotation.getAnnotationId() + "");
            annotationLocationBuilder.setLocation(locationBuilder.build());
            annotationLocations.addElement(annotationLocationBuilder.build());
        }
        
        if(annotationLocations.isEmpty())
        {
            return;
        }
        
        builder.addAllAnnotations(annotationLocations);
        
        MapViewMoveAnnotationsRequest request = builder.build();
        helper.moveAnnotations(request);
    }
}

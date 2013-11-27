/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenMapComponent.java
 *
 */
package com.telenav.ui.citizen.map;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.google.protobuf.ByteString;
import com.telenav.app.TeleNavDelegate;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkMapEventListener;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.map.IMapView;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.NavigationSDK;
import com.telenav.navsdk.events.MapViewData.SelectedAnnotation;
import com.telenav.navsdk.events.MapViewData.SelectedTrafficIncident;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.res.ISpecialImageRes;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnGLSurfaceField;
import com.telenav.util.PrimitiveTypeCache;


/**
 *@author JY Xu (jyxu@telenav.cn)
 *@date Nov 10, 2010
 */
public class GlMapComponent extends TnGLSurfaceField implements IMapView,INavSdkMapEventListener, INotifierListener,IMapEGLEventListener
{
    private static final int DOUBLE_TAP_TIMEOUT = 350;
    private static final int DOUBLE_TAP_SLOP = 35 * 35;
    private static final int SINGLE_TAG_SLOP = 35 * 35;
    private static final int DISTANCE_THRESHOLD = 20; // pixel
    
    boolean isLowFPSAllowed = true;
    
    boolean isRadiusEnabled = true;
    
    boolean isGpsCoarseEnabled = true;
    
    boolean isRequestingResizeView = false;    
    
    boolean isEGLSizeChanging = false;
    
    private int lastTouchCount = 0;
    
    private boolean enableMapUIEvent = true;
    
    private boolean enableGLEngineUIEvent = true;

    private TnMotionEvent lastDownEvent, lastUpEvent, currentDownEvent, currentUpEvent,lastScaleGestureEvent;
    
    int lastTouchDownX;
    
    int lastTouchDownY;

    private IMapUIEventListener listener;
    
    private long lastDownTime = -1;
    
    private long lastNotifyTimestamp = -1;
    
    private static final int MAX_CLICK_INTERVAL = 800;
    
    private NavSdkMapProxy mapProxy;
    
    Object snapShotMutex = new Object();

    boolean isLongTouched = false;
    boolean isAnnotationTouched = false;
    boolean needHandleTapNoAnnotation = true;
    
    boolean snapShotCanceled = false;
    
    private int lastX;
    
    private int lastY;    
    
    private boolean hasEnterScaleGesture;
    
    private boolean is2FingerDown;
    
    private long lastControlEventTime;
    
    private boolean isEngineTouchEventBegin;
    
    private int singleFingerX,singleFingerY;
//    String[] commonImageFiles = new String[]
//    { "ArialBlack.png", "ArialGlow.png", };
    
    private String[] configFiles =
    { "", "", ""};

    long viewId = 0;

    GlMapComponent(int id, int version)
    {
        super(id, version);
        if (TeleNavDelegate.getInstance().isES2Supported())
        {
            this.setEGLClientVersion(2);
        }
        else
        {
            this.setEGLClientVersion(1);
        }
        glRenderer = new GLMapRenderer();
        setRenderer(glRenderer);
        
        if (glRenderer instanceof GLMapRenderer)
        {
            ((GLMapRenderer) glRenderer).setMapEGLEventListener(this);
        }
        
        Notifier.getInstance().addListener(this);
        initPoints();
    }
    
    void initRender()
    {
        String esFileName = "es1_config.json";
        if (TeleNavDelegate.getInstance().isES2Supported())
        {
            esFileName = "es2_config.json";
        }
        ((GLMapRenderer) glRenderer).init(esFileName);
        mapProxy = new NavSdkMapProxy();
        mapProxy.addListener(this);
        openMapView();
        enableRadius(getViewId(), false);
    }
    
    private void initPoints()
    {
        singleFingerX = -1;
        singleFingerY = -1;
    }
    
    void openMapView()
    {
        if (mapProxy != null)
        {
            mapProxy.initMapConfig(configFiles);
            mapProxy.open("" + viewId, 0, 0, AppConfigHelper.getDisplayWidth(), AppConfigHelper.getDisplayHeight()
                    - AppConfigHelper.getStatusBarHeight());
            ((GLMapRenderer) glRenderer).setViewId(this.viewId);
            //TODO need to record opengl log here, please refer to http://jira.telenav.com:8080/browse/TNANDROID-4396 
        }
    }
    
    void destroyMapView(String viewName)
    {
        mapProxy.close(viewName);
    }
    
    void createMapView(final int mapX, final int mapY, final int mapWidth, final int mapHeight, final long viewId)
    {
        this.viewId = viewId;
    }
    
    protected void initConfig()
    {
        configFiles[0] = "config_default.json";
        configFiles[1] = "config_map_view.json";
        if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_TINY)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "add config_tiny.json");
            configFiles[2] = "config_tiny.json";
        }
        else if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "add config_small.json");
            configFiles[2] = "config_small.json";
        }
        else if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "add config_medium.json");
            configFiles[2] = "config_medium.json";
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "add config_large.json");
            configFiles[2] = "config_large.json";
        }
    }
    
    protected void initMapEngine()
    {
        initConfig();
        initRender();
        ((GLMapRenderer) glRenderer).setViewManuallyInitialized(true);
    }
    
    public void followVehicle(String viewName)
    {
        mapProxy.followVehicle(viewName);
    }
    
    public void disableAllTurnArrows(String viewName, String routeName)
    {
        mapProxy.disableAllTurnArrows(viewName, routeName);
    }

    void showMapLayer(final boolean isShowTrafficOverlay, final boolean isShowSatelliteOverlay, 
            final boolean isShowTrafficCameraOverlay, final boolean isShowLandMarkOverlay, final boolean isShowIncident, String viewName)
    {
        mapProxy.showMapLayer(viewName, isShowTrafficOverlay, isShowSatelliteOverlay, isShowTrafficCameraOverlay, isShowIncident);
    }
    
    void setCameraDeclination(String viewName, float declination)
    {
        mapProxy.setMapDeclination(viewName, declination);
    }
    
    void setMapPanAndZoom(String viewName)
    {
        mapProxy.setMapPanAndZoom(viewName);
    }
    
    void lookAtRegion(String viewName, double centerLat, double denterLon, double deltaLat, double deltaLon)
    {
        mapProxy.lookAtRegion(viewName, centerLat, denterLon, deltaLat, deltaLon);
    }
    
    void displayAnnotationLayer(String viewName, int annotationLayer, boolean isDisplay)
    {
        if (isDisplay)
        {
            mapProxy.displayAnnotationsByLayer(viewName, annotationLayer);
        }
        else
        {
            mapProxy.hideAnnotationsByLayer(viewName, annotationLayer);
        }
    }

    void displayAnnotationByName(String viewName, String annotationName, boolean isDisplay)
    {
        if (isDisplay)
        {
            mapProxy.displayAnnotationByName(viewName, annotationName);
        }
        else
        {
            mapProxy.hideAnnotationByName(viewName, annotationName);
        }
    }
    
    void enableAnnotationLayer(String viewName, int annotationLayer, boolean isEnable)
    {
        if (isEnable)
        {
            mapProxy.enableAnnotationsByLayer(viewName, annotationLayer);
        }
        else
        {
            mapProxy.disableAnnotationsByLayer(viewName, annotationLayer);
        }
    }
    
    void enableAnnotationByName(String viewName, String annotationName, boolean isEnable)
    {
        if (isEnable)
        {
            mapProxy.enableAnnotationByName(viewName, annotationName);
        }
        else
        {
            mapProxy.disableAnnotationByName(viewName, annotationName);
        }
    }
    
    
    void removeAnnotationByLayer(String viewName, int zOder)
    {
        mapProxy.removeAnnotationsByLayer(viewName, zOder);
    }
    
    void removeAllAnnotations(String viewName)
    {
        mapProxy.removeAllAnnotations(viewName);
    }
    
    void removeAnnotationByName(String viewName, String annotationName)
    {
        mapProxy.removeAnnotationByName(viewName, annotationName);   
    }

    public void setMapUIEventListener(IMapUIEventListener listener)
    {
        this.listener = listener;
    }

    public void removeMapUIEventListener()
    {
        this.listener = null;
    }

    public boolean hasMapUIEventListener()
    {
        return this.listener != null;
    }
    
    public void pause()
    {
        if(glRenderer == null)
        {
            return;
        }
        ((GLMapRenderer) glRenderer).pause();
    }

    public void resume()
    {
        if(glRenderer == null)
        {
            return;
        }
        ((GLMapRenderer) glRenderer).resume();
    }

    protected void initDefaultStyle()
    {

    }

    protected void paint(AbstractTnGraphics graphics)
    {

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if(viewId < 1)
        {
            return;
        }
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "OGLMapService-GetSnapshot onSizeChanged");
        }
        isEGLSizeChanging = true;
    }
    
    public long getConfId()
    {
        if (glRenderer == null)
            return 0;

        return ((GLMapRenderer) glRenderer).getConfId();
    }

    /*
     * check whether the array items is annotation layer, if all items are not annotation layer, return false, and
     * search latlonPicable and return it's lat lon. if at least one item is annotaion layer, return true.  
     */
    private void handleLongTouch(final TnMotionEvent motionEvent)
    {
        int screenX = motionEvent.getX();
        int screenY = motionEvent.getY();
        this.mapProxy.mapViewProjection(getViewId(screenX,screenY), screenX, screenY);
    }
    
    protected String getViewId(int x, int y)
    {
        return "" + this.viewId;
    }
    
    protected String getViewId()
    {
        return this.viewId == 0 ? "" : "" + this.viewId;
    }

    void showRoutes(String viewName, Vector routeNames, String highlightRoute, boolean isDecimatedRoute)
    {
        mapProxy.displayRoutes(viewName, routeNames, highlightRoute, isDecimatedRoute);
    }
    
    void showBetterRoutes(String viewName, Vector routeNames, String newRoute)
    {
        mapProxy.displayBetterRoutes(viewName, routeNames, newRoute);
    }
    
    void clearRoutes(String viewName)
    {
        mapProxy.clearRoutes(viewName);
    }
    
    void setRenderingMode(String viewName, int mode)
    {
        mapProxy.setRenderingMode(viewName, mode);
    }
    
    void setMapVerticalOffset(String viewName, float vertical)
    {
        mapProxy.setMapVerticalOffset(viewName, vertical);
    }
    
    public void requestRepaint()
    {

    }

    // ================================= ui event ==========================================================================//
   
    protected long getLastControlEventTime()
    {
        return lastControlEventTime;
    }
    
    protected void setLastControlEventTime(long timeStamp)
    {
        lastControlEventTime = timeStamp;
    }
    
    void looAtRoutes(String viewName, Vector routeNames, boolean isDecimatedRoute)
    {
        mapProxy.lookAtRoutes(viewName, routeNames, isDecimatedRoute);
    }
    
    void lookAtBetterRoutes(String viewName, Vector routeNames, String newRoute)
    {
        mapProxy.lookAtBetterRoutes(viewName, routeNames, newRoute);
    }
    
    void enableTurnArrows(String viewName, String routeName, int segmentIndex,
            int segmentValue)
    {
        mapProxy.enableTurnArrows(viewName, routeName, segmentIndex, segmentValue);
    }
    
    void setZoomLevel(String viewName, float zoomLevel)
    {
        mapProxy.setZoom(viewName, zoomLevel);
    }
    
    void lookAt(String viewName, double lat, double lon)
    {
        if(!DataUtil.isLatLonValid(lat, lon))
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "invalid lat lon!!!");
            return;
        }
        mapProxy.lookAt(viewName, lat, lon);
    }

    void lookAt(String viewName, double lat, double lon, double altitude)
    {
        if(!DataUtil.isLatLonValid(lat, lon))
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "invalid lat lon!!!");
            return;
        }
        mapProxy.lookAt(viewName, lat, lon, altitude);
    }
    
    void lookAtAnnotations(String viewName, Vector annotationNames, String centerAnnotationName)
    {
        mapProxy.lookAtAnnotations(viewName, annotationNames, centerAnnotationName);
    }
    
    void zoomInMap(String viewName)
    {
        mapProxy.zoomIn(viewName);
    }
    
    void zoomOutMap(String viewName)
    {
        mapProxy.zoomOut(viewName);
    }
    
    void loadAsset(String viewName, int width, int height, byte[] data,
            String assetName)
    {
        mapProxy.loadAsset(viewName, width, height, ByteString.copyFrom(data), assetName);
    }
    
    void loadAsset(String viewName, int width, int height, AbstractTnImage image,
            String assetName)
    {
        byte[] data = this.generateAnnotationBinary(image);
        this.loadAsset(viewName, width, height, data, assetName);
    }

    void loadAsset(String viewName, int width, int height, String fileName,
            String assetName)
    {
        mapProxy.loadAsset(viewName, width, height, fileName, assetName);
    }

    void unloadAsset(String viewName, String assetName)
    {
        mapProxy.unloadAsset(viewName, assetName);
    }
    
    void addAnnotation(String viewName, String annotationName ,MapViewAddAnnotationRequest.Builder builder)
    {
        builder.setAnnotationName(annotationName);
        builder.setViewName(viewName);
        mapProxy.addAnnotation(builder);
    }
    
    void moveAnnotations(Hashtable<AbstractAnnotation, TnLocation> annotationLocationMapping, String viewName)
    {
        mapProxy.moveAnnotations(annotationLocationMapping, viewName);
    }
    
    void updateAnnotation(MapViewUpdateAnnotationRequest.Builder builder)
    {
        mapProxy.updateAnnotation(builder);
    }
    
    void loadConfiguration(String viewName, String config)
    {
        mapProxy.changeConfiguration(viewName, config);
    }
    
    void setTransitionTime(String viewName,float time)
    {
        mapProxy.seTransitionTime(viewName, time);
    }

    void getZoomLevel(String viewName)
    {
        mapProxy.getZoomLevel(viewName);
    }

    void enableAdi(String viewName, boolean enable)
    {
        mapProxy.setAdiEnabled(viewName, enable);
    }

    private byte[] generateAnnotationBinary(AbstractTnImage image)
    {
        int bitmapWidth = image.getWidth();
        int bitmapHeight = image.getHeight();
        int[] argbData = new int[bitmapWidth * bitmapHeight];
        image.getARGB(argbData, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        byte[] imageData = new byte[argbData.length * 4];

        // image should be top-down
        for (int i = bitmapHeight - 1; i >= 0; i--)
        {
            for (int j = 0; j < bitmapWidth; j++)
            {
                int srcPixelIndex = bitmapWidth * (bitmapHeight - 1 - i) + j;
                int destPixelIndex = bitmapWidth * i + j;
                imageData[4 * destPixelIndex + 2] = (byte) ((argbData[srcPixelIndex] & 0xFF) >> 0);
                imageData[4 * destPixelIndex + 1] = (byte) ((argbData[srcPixelIndex] & 0xFF00) >> 8);
                imageData[4 * destPixelIndex + 0] = (byte) ((argbData[srcPixelIndex] & 0xFF0000) >> 16);
                imageData[4 * destPixelIndex + 3] = (byte) ((argbData[srcPixelIndex] & 0xFF000000) >> 24);
            }
        }
        return imageData;
    }
    
    private final int translateTouchAction(TnMotionEvent event)
    {
        switch (event.getAction())
        {
            case TnMotionEvent.ACTION_DOWN:
            case TnMotionEvent.ACTION_BEGIN_SCALE:
            {
                return IMapContainerConstants.TOUCH_EVENT_BEGIN;
            }
            case TnMotionEvent.ACTION_MOVE:
            case TnMotionEvent.ACTION_ON_SCALE:
            {
                return IMapContainerConstants.TOUCH_EVENT_MOVE;
            }
            case TnMotionEvent.ACTION_UP:
            case TnMotionEvent.ACTION_END_SCALE:
            {
                return IMapContainerConstants.TOUCH_EVENT_END;
            }
            case TnMotionEvent.ACTION_OUTSIDE:
            case TnMotionEvent.ACTION_CANCEL:
            {
                if (isEngineTouchEventBegin)
                {
                    return IMapContainerConstants.TOUCH_EVENT_END;
                }
                return -1;
            }
            case TnMotionEvent.ACTION_LONG_TOUCH:
            {
                return IMapContainerConstants.TOUCH_LONG_TOUCH;
            }
            default:
                return -1;
        }
    }
    
    private void handleNavsdkTouchEvent(int action, float x, float y)
    {
        final String mapViewName = "1";
        final NavigationSDK navSdk = NavigationSDK.getInstance(null);
        if (Logger.DEBUG)
        {
            if (action != IMapContainerConstants.TOUCH_EVENT_MOVE)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "handleNavSdkTouchEvent: action--- " + action + " x--- " + x
                        + " y--- " + y);
            }
        }
        // pinch action
        switch (action)
        {
            case IMapContainerConstants.TOUCH_EVENT_BEGIN:
            {
                navSdk.HandleTouchBegin(mapViewName, x, y);
                this.isEngineTouchEventBegin = true;
                break;
            }
            case IMapContainerConstants.TOUCH_EVENT_MOVE:
            {
                navSdk.HandleTouchMove(mapViewName, x, y);
                break;
            }
            case IMapContainerConstants.TOUCH_EVENT_END:
            {
                navSdk.HandleTouchEnd(mapViewName, x, y);
                this.isEngineTouchEventBegin = false;
                break;
            }
            case IMapContainerConstants.TOUCH_EVENT_TAP:
            {
                navSdk.HandleTap(mapViewName, x, y);
                break;
            }
            default:
            {
                break;
            }
        }
    }
    
    protected boolean handleNavsdkScaleGestureEvent(final int action, final int distance, final int x, final int y)
    {
        final String mapViewName = "1";
        final NavigationSDK navSdk = NavigationSDK.getInstance(null);
        switch (action)
        {
            case IMapContainerConstants.TOUCH_EVENT_BEGIN:
            {
                navSdk.HandlePinchBegin(mapViewName, x, y, distance);
                this.isEngineTouchEventBegin = true;
                break;
            }
            case IMapContainerConstants.TOUCH_EVENT_MOVE:
            {
                navSdk.HandlePinchMove(mapViewName, x, y, distance);
                break;
            }
            case IMapContainerConstants.TOUCH_EVENT_END:
            {
                navSdk.HandlePinchEnd(mapViewName, x, y, distance);
                this.isEngineTouchEventBegin = false;
                if (listener != null)
                {
                    listener.onPinchEnd();
                }
                break;
            }
            default:
            {
                break;
            }
        }
        return false;
    }
    
    private boolean isPointsValid()
    {
        return  singleFingerX != -1 && singleFingerY != -1;
    }

    protected boolean handleUiEvent(final TnUiEvent tnUiEvent)
    {
        long mTimestamp = System.currentTimeMillis();
        if (tnUiEvent.getPrivateEvent() != null && tnUiEvent.getPrivateEvent().getAction() == TnPrivateEvent.ACTION_TIMER)
        {
            if (listener != null)
            {
                listener.onPinchEvent();
            }
            TnUiTimer.getInstance().removeReceiver(this);
            return false;
        }
        
        lastNotifyTimestamp = System.currentTimeMillis();
        setMaxFps(GLMapRenderer.MAX_FPS);
        
        final TnMotionEvent event = tnUiEvent.getMotionEvent();
        
        if (event == null)
        {
            return false;
        }

        // int mapYPosition = mapY == null ? 0 : mapY.getInt();
        // event.setLocation(event.getX(), event.getY() - mapYPosition <= 0 ? 0 : event.getY() - mapYPosition);
        int mapYPosition = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - event.getY();
        event.setLocation(event.getX(), mapYPosition > 0 ? mapYPosition : 0);

        MapUiEventDelegate.handleTouchEventOnMap(listener, MapContainer.getInstance(), tnUiEvent);
        /*
         * if ui event for map engine is disabled, doesn't pass any event to map engine
         */
        if(!enableGLEngineUIEvent)
        {
            return false;
        }
        
        /*
         * if ui event for view(MVC) is disabled, just pass the event to map engine and never let MVC to receive the event
         */
        if (!enableMapUIEvent)
        {
            // http://jira.telenav.com:8080/browse/GLMAP-1970
            // anyway, we need to post the touch event to map engine.
            int action = translateTouchAction(event);
            int touchX = event.getX();
            int touchY = event.getY();
            if (tnUiEvent.getType() == TnUiEvent.TYPE_GESTURE_SCALE)
            {
                handleNavsdkScaleGestureEvent(action, event.getDistance(), touchX, touchY);
            }
            else if (event.getPointerCount() == 1)
            {
                handleNavsdkTouchEvent(action, touchX, touchY);
            }
            return false;
        }
        final int action = translateTouchAction(event);
        /*
         * Scale Begin
         */
        if (tnUiEvent.getType() == TnUiEvent.TYPE_GESTURE_SCALE && tnUiEvent.getMotionEvent() != null
                && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_BEGIN_SCALE)
        {
            TnUiTimer.getInstance().addReceiver(this, 500);
            needHandleTapNoAnnotation = false;
            /*
             * When scale begin, we need to reset first touch down event to map engine
             */
            if (lastDownEvent != null)
            {
                handleNavsdkTouchEvent(IMapContainerConstants.TOUCH_EVENT_END, lastX, lastY);
                lastDownEvent = null;
            }
            handleNavsdkScaleGestureEvent(action, event.getDistance(), event.getX(), event.getY());
            hasEnterScaleGesture = true;
            initPoints();
            lastScaleGestureEvent = event;
        }
        /*
         * In Scale, pinching to zoom
         */
        else if (tnUiEvent.getType() == TnUiEvent.TYPE_GESTURE_SCALE && tnUiEvent.getMotionEvent() != null
                && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_ON_SCALE)
        {
            TnUiTimer.getInstance().addReceiver(this, 500);
            needHandleTapNoAnnotation = false;
            handleNavsdkScaleGestureEvent(action, event.getDistance(), event.getX(), event.getY());
            lastScaleGestureEvent = event;
        }
        /*
         * Scale End
         */
        else if (tnUiEvent.getType() == TnUiEvent.TYPE_GESTURE_SCALE && tnUiEvent.getMotionEvent() != null
                && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_END_SCALE)
        {
            TnUiTimer.getInstance().addReceiver(this, 500);
            needHandleTapNoAnnotation = false;
            if (lastScaleGestureEvent != null)
            {
                handleNavsdkScaleGestureEvent(action, lastScaleGestureEvent.getDistance(), lastScaleGestureEvent.getX(),
                    lastScaleGestureEvent.getY());
            }
            else
            {
                handleNavsdkScaleGestureEvent(action, event.getDistance(), event.getX(), event.getY());
            }
        }
        /*
         * handle the long click for one finger action
         */
        else if(event.getPointerCount() == 0 && event.getAction() == TnMotionEvent.ACTION_LONG_TOUCH)
        {
            if(lastTouchCount <= 1
                    && lastTouchDownX > 0
                    && lastTouchDownY > 0
                    && DataUtil.distance(event.getX() - lastTouchDownX, event.getY() - lastTouchDownY) < DISTANCE_THRESHOLD && !isAnnotationTouched)
            {
                handleLongTouch(event);
                isLongTouched = true;
            }
        }
        /*
         * not handle event, just record the finger coordinator
         * Please notice, distance pass to map engine is 0, so here we will not handle pinch!!!
         */
        else if (event.getPointerCount() > 1)
        {
            if (event.getAction() == TnMotionEvent.ACTION_DOWN)
            {
                is2FingerDown = true;
            }
        }
        /*
         * handle one finger event
         */
        else if (event.getPointerCount() == 1)
        {
            if (event.getAction() == TnMotionEvent.ACTION_DOWN)
            {
                lastDownEvent = currentDownEvent;
                currentDownEvent = event;
                lastDownTime = mTimestamp;
                lastTouchDownX = event.getX();
                lastTouchDownY = event.getY();
            }
            else if (event.getAction() == TnMotionEvent.ACTION_UP)
            {
                isAnnotationTouched = false;
                lastUpEvent = currentUpEvent;
                currentUpEvent = event;
            }

            boolean isHandled = false;
            
            final int touchX = (int) event.getX();
            final int touchY = (int) event.getY();
            if (event.getAction() == TnMotionEvent.ACTION_UP)
            {
                if (DataUtil.distance(touchX - lastTouchDownX, touchY - lastTouchDownY) < DISTANCE_THRESHOLD
                        && (mTimestamp - lastDownTime < MAX_CLICK_INTERVAL) && !isLongTouched)
                {
                    needHandleTapNoAnnotation = true;
                }
                else
                {
                    if (isLongTouched)
                    {
                        isLongTouched = false;
                    }
                    needHandleTapNoAnnotation = false;
                }
                
                //handle tap event, will receive onclick call back from NavSDK
                if (listener != null && lastTouchDownX >= 0 && lastTouchDownY >= 0
                        && DataUtil.distance(touchX -  lastTouchDownX, touchY - lastTouchDownY) < DISTANCE_THRESHOLD
                        && (mTimestamp - lastDownTime < MAX_CLICK_INTERVAL))
                {
                    handleNavsdkTouchEvent(IMapContainerConstants.TOUCH_EVENT_END, touchX, touchY);
                    handleNavsdkTouchEvent(IMapContainerConstants.TOUCH_EVENT_TAP, touchX, touchY);
                    isHandled = true; 
                }
                
                if (isConsideredDoubleTap(lastDownEvent, lastUpEvent, currentDownEvent, currentUpEvent))
                {
                    handleNavsdkTouchEvent(action, event.getX(), event.getY());
                    MapContainer.getInstance().zoomInMap();
                    isHandled = true;
                }
            }
            
            if (!isHandled)
            {
                if (event.getAction() == TnMotionEvent.ACTION_MOVE
                        && IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE == MapContainer.getInstance()
                                .getInteractionMode())
                {
                    // This is a hack
                    // The interaction mode will be changed to INTERACTION_MODE_PAN_AND_ZOOM in
                    // listener.handleTouchEventOnMap()
                    // when user is trying to pan the map.
                    MapUiEventDelegate.handleTouchEventOnMap(listener, MapContainer.getInstance(), tnUiEvent);
                }

                /*
                 * When scale has completed, we need to mock a begin map touch event to update current single finger
                 * coordinator
                 */
                if (!hasEnterScaleGesture && is2FingerDown && isPointsValid())
                {
                    if (event.getAction() == TnMotionEvent.ACTION_MOVE)
                    {
                        handleNavsdkTouchEvent(IMapContainerConstants.TOUCH_EVENT_END, singleFingerX, singleFingerY);
                        handleNavsdkTouchEvent(IMapContainerConstants.TOUCH_EVENT_BEGIN, event.getX(), event.getY());
                        hasEnterScaleGesture = false;
                        is2FingerDown = false;
                    }
                    else if (event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        handleNavsdkTouchEvent(IMapContainerConstants.TOUCH_EVENT_END, singleFingerX, singleFingerY);
                        hasEnterScaleGesture = false;
                        is2FingerDown = false;
                    }
                }
                else
                {
                    if (hasEnterScaleGesture)
                    {
                        handleNavsdkTouchEvent(IMapContainerConstants.TOUCH_EVENT_BEGIN, event.getX(), event.getY());
                        hasEnterScaleGesture = false;
                    }

                    handleNavsdkTouchEvent(translateTouchAction(event), touchX, touchY);
                    if (event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        hasEnterScaleGesture = false;
                        is2FingerDown = false;
                    }
                }
                singleFingerX = event.getX();
                singleFingerY = event.getY();
            }
        }
        
        lastTouchCount = event.getPointerCount();
        lastX = event.getX();
        lastY = event.getY();
        lastControlEventTime = System.currentTimeMillis();
        return false;
    }
    
    
    private boolean isConsideredDoubleTap(TnMotionEvent firstDown, TnMotionEvent firstUp, TnMotionEvent secondDown, TnMotionEvent secondUp)
    {
        
        if (firstDown == null || firstUp == null || secondDown == null || secondUp == null)
            return false;
        
        if (secondUp.getEventTime() - firstUp.getEventTime() > DOUBLE_TAP_TIMEOUT)
        {
            return false;
        }

        int dx = firstDown.getX() - firstUp.getX();
        int dy = firstDown.getY() - firstUp.getY();
        if (dx * dx + dy * dy > SINGLE_TAG_SLOP * ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDensity())
            return false;

        dx = secondDown.getX() - secondUp.getX();
        dy = secondDown.getY() - secondUp.getY();
        if (dx * dx + dy * dy > SINGLE_TAG_SLOP * ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDensity())
            return false;

        int deltaX = (int) firstUp.getX() - (int) secondUp.getX();
        int deltaY = (int) firstUp.getY() - (int) secondUp.getY();
        boolean isHandled = (deltaX * deltaX + deltaY * deltaY < DOUBLE_TAP_SLOP
                * ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDensity());
        if (isHandled)
        {
            return true;
        }

        return false;
    }

    // ================================= end ui event===========================================//

    public void setEnableMapUIEvent(boolean enableMapUIEvent)
    {
        this.enableMapUIEvent = enableMapUIEvent;
    }

    public void setEnableGLEngineUIEvent(boolean enableGLEngineUIEvent)
    {
        this.enableGLEngineUIEvent = enableGLEngineUIEvent;
    }

    public void postMapUiEvent(Runnable runnable)
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(runnable);
    }
    
    TnUiArgAdapter mapX;
    TnUiArgAdapter mapY;
    TnUiArgAdapter mapWidth;
    TnUiArgAdapter mapHeight;

    public void setMapRect(TnUiArgAdapter x, TnUiArgAdapter y, TnUiArgAdapter width, TnUiArgAdapter height)
    {
        mapX = x;
        mapY = y;
        mapWidth = width;
        mapHeight = height;

        if (mapX != null && mapY != null && mapWidth != null && mapHeight != null)
        {
            this.resizeView("" + MapContainer.getInstance().getViewId(), mapX.getInt(), mapY.getInt(), mapWidth.getInt(),
                mapHeight.getInt());
        }
        else
        {
            this.resizeView("" + MapContainer.getInstance().getViewId(), 0, 0, AppConfigHelper.getDisplayWidth(),
                AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight());
        }
    }
    
    public void setMapRect(final int x, final int y, final int width, final int height)
    {
        mapX = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(x);
            }
        });
        mapY = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(y);
            }
        });
        mapWidth = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0),
                new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(width);
                    }
                });
        mapHeight = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0),
                new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(height);
                    }
                });
        this.resizeView("" + MapContainer.getInstance().getViewId(), mapX.getInt(),
            mapY.getInt(), mapWidth.getInt(), mapHeight.getInt());
    }

    public void sublayout(int width, int height)
    {
        super.sublayout(width, height);
    }
    
    private void resizeView(String viewName, int x, int y, int width, int height)
    {
        int surfaceWidth = ((GLMapRenderer)glRenderer).getSurfaceWidth();
        int surfaceHeight = ((GLMapRenderer)glRenderer).getSurfaceHeight();
       
        if(y < 0)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.WARNING, this.getClass().getName(), "OGLMapService resize view: y is invalid!!!");
            }
            y = 0;
        }
        
        int transformedY = 0;
        
        transformedY = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - height - y;
        
        if (transformedY < 0)
        {
            transformedY = 0;
        }
        
        if (mapProxy == null || x + width > surfaceWidth || transformedY + height > surfaceHeight)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.WARNING, this.getClass().getName(),
                    "OGLMapService resize view width and height are invalid, igorne it!!! ");
                Logger.log(Logger.WARNING, this.getClass().getName(), "OGLMapService resize view: x--- " + x + "y--- "
                        + transformedY + "width--- " + width + "height--- " + height);
            }
            return;
        }
        
        isRequestingResizeView = true;

        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "OGLMapService resize view: x--- " + x + "y--- "
                    + transformedY + "width--- " + width + "height--- " + height);
        }

        this.mapProxy.mapViewResize("" + MapContainer.getInstance().getViewId(), x, transformedY, width, height);
    }

    private int parseString2Int(String str)
    {
        int result = -1;
        
        try
        {
            result = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            
        }
        
        return result;
    }
    
    public void handleMapViewProjection(String viewName, double lat, double lon)
    {
        listener.handleMapRgc(lat, lon);
    }

    public void handleTapNoAnnotation(String viewName)
    {
        if (listener != null && needHandleTapNoAnnotation)
        {
            listener.tapNoAnnotation();
        }
    }
    
    public void handleClickAnnotation(String viewName, List<SelectedAnnotation> annotations)
    {
        AbstractAnnotation unfocusedAnnotation = null;
        int unfocusedAnnotationX = 0;
        int unfocusedAnnotationY = 0;
        for (int i = 0; i < annotations.size(); i++)
        {
            SelectedAnnotation annotation = annotations.get(i);
            int viewId = parseString2Int(viewName);
            int annotationId = parseString2Int(annotation.getAnnotationName());
            int x = annotation.getX();
            int y = annotation.getY();

            if (viewId == -1 || annotationId == -1)
            {
                return;
            }

            AbstractAnnotation tmpAnnotation = (AbstractAnnotation) (MapContainer.getInstance()
                    .getFeature(ICommonConstants.ANOTATION_COMPONENT_TRANSLATE + annotationId));

            if(tmpAnnotation != null)
            {
                if (tmpAnnotation.isSetFocused())
                {

                    if (tmpAnnotation.handleClickEvent(x, y))
                    {
                        return;
                    }
                    else
                    {
                        unfocusedAnnotationX = annotation.getX();
                        unfocusedAnnotationY = annotation.getY();
                        unfocusedAnnotation = tmpAnnotation;
                    }
                }
                else
                {
                    unfocusedAnnotationX = annotation.getX();
                    unfocusedAnnotationY = annotation.getY();
                    unfocusedAnnotation = tmpAnnotation;
                }
            }

            if (i == annotations.size() - 1 && unfocusedAnnotation != null)
            {
                unfocusedAnnotation.handleClickEvent(unfocusedAnnotationX, unfocusedAnnotationY);
            }
        }
    }

    public void handleUpAnnotation(String viewName, List<SelectedAnnotation> annotations)
    {
        for (int i = 0; i < annotations.size(); i++)
        {
            SelectedAnnotation annotation = annotations.get(i);
            int viewId = parseString2Int(viewName);
            int annotationId = parseString2Int(annotation.getAnnotationName());

            if (viewId == -1 || annotationId == -1)
            {
                return;
            }

            AbstractAnnotation tmpAnnotation = (AbstractAnnotation) (MapContainer.getInstance()
                    .getFeature(ICommonConstants.ANOTATION_COMPONENT_TRANSLATE + annotationId));

            if (tmpAnnotation.isSetFocused())
            {
                tmpAnnotation.handleUpEvent(annotation.getX(), annotation.getY());
                return;
            }
        }
    }

    public void handleDownAnnotation(String viewName, List<SelectedAnnotation> annotations)
    {
        for (int i = 0; i < annotations.size(); i++)
        {
            SelectedAnnotation annotation = annotations.get(i);
            int viewId = parseString2Int(viewName);
            int annotationId = parseString2Int(annotation.getAnnotationName());

            if (viewId == -1 || annotationId == -1)
            {
                return;
            }

            AbstractAnnotation tmpAnnotation = (AbstractAnnotation) (MapContainer.getInstance()
                    .getFeature(ICommonConstants.ANOTATION_COMPONENT_TRANSLATE + annotationId));

            if (tmpAnnotation != null)
            {
                isAnnotationTouched = true;
                if (tmpAnnotation.isSetFocused())
                {
                    tmpAnnotation.handleDownEvent(annotation.getX(), annotation.getY());
                    return;
                }
            }
        }
    }

    public void handleClickIncident(String viewName, SelectedTrafficIncident incident)
    {
        if (listener == null)
        {
            return;
        }
        listener.handleClickTrafficIncident(incident);
    }

    public void handleGetZoomLevel(String viewName, float zoomLevel)
    {
        MapContainer.getInstance().updateZoomLevel(zoomLevel);
    }
    
    public boolean isMapReady()
    {
        boolean isMapReady = this.viewId > 0;
        if(isMapReady)
        {
            if(glRenderer instanceof GLMapRenderer)
            {
                isMapReady = ((GLMapRenderer)glRenderer).isMapReady();
            }
            else
            {
                isMapReady = false;
            }
        }
        
        return isMapReady;
    }
    
    protected void updateMapView(String viewName, int width, int height)
    {
        if (this.mapX == null || this.mapY == null || this.mapHeight == null || this.mapWidth == null)
        {
            if (width == -1 || height == -1)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "OGLMapService-GetSnapshot updateMapView: x --- "
                            + "at least one of following value is null" + "mapX,mapY,mapHeight,mapWidth" + "and width or height is -1");
                }
            }
            else
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(),
                        "OGLMapService-GetSnapshot updateMapView: x --- " + "at least one of following value is null"+"mapX,mapY,mapHeight,mapWidth");
                }                
                this.resizeView(viewName, 0, 0, width, height);
            }
        }
        else
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "OGLMapService-GetSnapshot updateMapView: x --- " + mapX.getInt() + " y --- " + mapY.getInt()
                            + " width --- " + mapWidth.getInt() + " height --- " + mapHeight.getInt());
            }
            this.resizeView(viewName, mapX.getInt(), mapY.getInt(), mapWidth.getInt(), mapHeight.getInt());
        }
    }
    
    public void setMaxFps(int fps)
    {
        if (glRenderer != null)
        {
            ((GLMapRenderer) glRenderer).setMaxFps(fps);
        }
    }
    
    @Override
    public long getNotifyInterval()
    {
        return 10 * 1000;
    }

    @Override
    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    @Override
    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestamp = timestamp;
    }

    @Override
    public void notify(long timestamp)
    {
        long curr = System.currentTimeMillis();
        
        if (curr - lastNotifyTimestamp > 10 * 1000 && isLowFPSAllowed)
        {
            setMaxFps(GLMapRenderer.MIN_FPS);
        }
    }
    
    void setLowFPSAllowed(boolean isAllowed)
    {
        if (this.isLowFPSAllowed == isAllowed)
        {
            return;
        }
        
        this.isLowFPSAllowed = isAllowed;
        if(!isAllowed)
        {
            setMaxFps(GLMapRenderer.MAX_FPS);
        }
    }
    
    // For current location blue circle
    void enableRadius(String viewName, boolean isEnable)
    {
        if (this.isRadiusEnabled == isEnable)
        {
            return;
        }

        mapProxy.enableRadius(viewName, isEnable);
        this.isRadiusEnabled = isEnable;
    }

    // For current location Coarse animation
    void enableGPSCoarse(String viewName, boolean isEnable)
    {
        if (this.isGpsCoarseEnabled == isEnable)
        {
            return;
        }

        mapProxy.enableCoarseAnimation(viewName, isEnable);
        this.isGpsCoarseEnabled = isEnable;
    }
    
    boolean isGPSCoarseEnabled()
    {
        return this.isGpsCoarseEnabled;
    }

    public void handleMapViewResizeResponse(String viewName)
    {
        this.isRequestingResizeView = false;
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (GlMapComponent.this.listener != null)
                {
                    GlMapComponent.this.listener.mapViewSizeChanged();
                }
            }
        });
    }

    //call back will be invoked when map engine resize successfully
    public void eglSizeChanged()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "OGLMapService-GetSnapshot eglSizeChanged");
        }
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (GlMapComponent.this.listener != null)
                {
                    GlMapComponent.this.listener.eglSizeChanged();
                }
            }
        });
        this.isEGLSizeChanging = false;
    }
}

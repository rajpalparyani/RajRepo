package com.telenav.ui.citizen.map;

/**
 *@author jyxu (jyxu@telenav.cn)
 *@date Jul 21, 2010
 */

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.misc.TripsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkNavigationProxyHelper.NavSdkNavigationUtil;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.map.InteractionModeChangeListener;
import com.telenav.map.MapConfig;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.preference.carmodel.CarModelManager;
import com.telenav.module.region.RegionUtil;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewData.AnnotationType;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.tnui.widget.TnGLSurfaceField;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.android.AndroidGLRenderer;
import com.telenav.tnui.widget.android.AndroidGLRenderer16;
import com.telenav.tnui.widget.android.IGLSnapBitmapCallBack;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.map.DayNightService.IDayNightListener;
import com.telenav.ui.citizen.map.StuckMonitorService.IStuckListener;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

public class MapContainer extends AbstractTnComponent implements InteractionModeChangeListener, INotifierListener, IDayNightListener, IStuckListener
{
    protected static final long SYNC_ZOOM_LEVEL_DELAY = 500;
    
    private static final int GPS_NETWORK_INTERVAL = 15 * 60 * 1000; // 15min

    private static final int GPS_SATELLIET_INTERVAL = 5 * 60 * 1000; // 5min
    
    private GlMapComponent mapComponent;

    private int version;
    
    private int renderMode;

    Hashtable addedComponents = new Hashtable();
    
    private TnAbsoluteContainer contentContainer = new TnAbsoluteContainer(0);
   
    private FrogNullField bgContainer = new FrogNullField(0);
    
    private TnAbsoluteContainer container = new TnAbsoluteContainer(0);
   
    private static MapContainer instance = new MapContainer(ICommonConstants.ID_MAP_CONTAINER, TnGLSurfaceField.VERSION_2_0);
    
    private float transitionTime = 2.0f;
    
    int interactionMode = IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE;
    
    InteractionModeChangeListener interactionModeChangeListener;
    
    private int currentDayNightColor = 0; //0 means undefined
    private boolean isMapColorAuto = false;
    
    private boolean isPaused = true;
    
    private boolean isInitialized = false;
    
    private boolean currentIsShowSatellite = false;
    
    private boolean isDisplayingModelVehicle = false;
    
    private boolean isNavConfig = false;
    
    private boolean zoomInBtnEnabled = true;//zoomIn button's state 
    
    private boolean zoomOutBtnEnabled= true;//zoomOut button's State
 
//    private double lat[] = new double[1]; //  as paramter return the lat by openGl
//    private double lon[] = new double[1]; //  as paramter return the lon by openGl
                    
//    IMapUIEventListener mapUiEventListener;
    
    protected long lastNotifiedTimeStamp = -1L;
    protected long lastPauseTime = -1L;
    protected Object resumeMutex = new Object();
    
//    Hashtable annotationAssetName = new Hashtable(); 
    
	public static final String SPRITE_VEHICLE_ANNOTATION_NAME = "SPRITE_VEHICLE_ANNOTATION_NAME";
    public static final String MODEL_VEHICLE_ANNOTATION_NAME = "MODEL_VEHICLE_ANNOTATION_NAME";
    
    String spriteVehicleAnnotationName = "";
    String modelVehicleAnnotationName = "";
//    boolean isRenderStarted = false;

    public final static int NAV_3D_DEFAULT_ZOOM_LEVEL = 0;
    public final static int NAV_2D_DEFAULT_ZOOM_LEVEL = 1;	
    
    private static final int CRASH_NOTIFICATION_ID = 1000000 + 1;
    
    private boolean isCarEnabled = true;
    
    public static MapContainer getInstance()
    {
        return instance;
    }
    
    public void pause()
    {
        if (isPaused)
        {
            return;
        }
        
        synchronized (resumeMutex)
        {
            isPaused = true;
            
            lastPauseTime = System.currentTimeMillis();
            
            stopStuckMonitor();
            Notifier.getInstance().addListener(this);
        }
        
    }

    private void pauseInternally()
    {
        if (mapComponent != null)
        {
            mapComponent.pause();
            mapComponent.onPause();

            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    bgContainer.setBackgroundColor(0xFFFFFFFF);
                    bgContainer.setVisible(true);
                }
            });

            Thread t = new Thread()
            {
                public void run()
                {
                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if(isPaused)
                            {
                                mapComponent.setVisible(false);
                            }
                        }
                    });
                }
            };
            
            t.start();
        }
    }
    
    public void resume()
    {
        if(!isPaused || !isInitialized)
        {
            return;
        }
        
        synchronized (resumeMutex)
        {
            if (mapComponent != null)
            {
                isPaused = false;
                mapComponent.onResume();
                mapComponent.resume();
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        bgContainer.setVisible(false);
                        mapComponent.setVisible(true);
                    }
                });
                startStuckMonitor();
    
            }        
        }
    }
    
    /**
     * This is for the application is moving to background
     */
    public void onPause()
    {
        if(!isInitialized)
            return;
        
        if (mapComponent != null)
        {
            mapComponent.pause();
            mapComponent.onPause();
        }
    }
    
    /**
     * This is for the application is bringing to foreground
     */
    public void onResume()
    {
        if (mapComponent != null)
        {
            mapComponent.resume();
            mapComponent.onResume();
        }        
    }
    
    private MapContainer(int id, int version)
    {
        super(id);
        this.version = version;
        nativeUiComponent = container.getNativeUiComponent();
        if(mapComponent == null)
        {
            mapComponent = new GlMapComponent(ICommonConstants.ID_MAP_COMPONENT, this.version);
           
            int width = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth();
            int height = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight();
            mapComponent.createMapView(0, 0, width, height, 1);
            
            contentContainer.add(mapComponent);
            container.add(contentContainer, 0);
            
            bgContainer.setVisible(false);
            bgContainer.setBackgroundColor(0xFFFFFFFF);
            bgContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(0, new ITnUiArgsDecorator()
            {
                
                public Object decorate(TnUiArgAdapter args)
                {
                    return AppConfigHelper.getDisplayWidth();
                }
            }));
            bgContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(0, new ITnUiArgsDecorator()
            {
                
                public Object decorate(TnUiArgAdapter args)
                {
                    return AppConfigHelper.getDisplayHeight();
                }
            }));
            container.add(bgContainer, 1);
        }
    }

    public void followVehicle()
    {
        mapComponent.followVehicle(this.getViewId() + "");
        this.interactionModeChanged(IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE);
    }
    
    public void destroyMapView()
    {
        DayNightService.getInstance().stop();
        mapComponent.destroyMapView("" + this.getViewId());
    }
    
    public void disableAllTurnArrows(String routeName)
    {
        mapComponent.disableAllTurnArrows("" + this.getViewId(), routeName);
    }
    
    public void enableTurnArrows(String routeName, int segmentIndex,
            int segmentValue)
    {
        mapComponent.enableTurnArrows("" + this.getViewId(), routeName, segmentIndex, segmentValue);
    }
    
    public void lookAt(double lat, double lon)
    {
        mapComponent.lookAt("" + this.getViewId(), lat, lon);
    }

    public void lookAt(double lat, double lon, double altitude)
    {
        mapComponent.lookAt("" + this.getViewId(), lat, lon, altitude);
    }
    
    public void lookAtRoutes(Vector routeNames, boolean isDecimatedRoute)
    {
        setMapTransitionTime(0);
        mapComponent.looAtRoutes("" + this.getViewId(), routeNames, isDecimatedRoute);
        updateZoomLevel();
    }
    
    public void lookAtBetterRoutes(Vector routeNames, String newRoute)
    {
        setMapTransitionTime(0);
        mapComponent.lookAtBetterRoutes("" + this.getViewId(), routeNames, newRoute);
        updateZoomLevel();
    }
    
    public void lookAtRoute(int routeID, boolean isDecimatedRoute)
    {
        Vector routeNames = new Vector();
        routeNames.add("" + routeID);
        lookAtRoutes(routeNames, isDecimatedRoute);
    }
    
    public void lookAtPosition(TnLocation location)
    {
        mapComponent.lookAt("" + this.getViewId(), NavSdkNavigationUtil.convertCoordinate(location.getLatitude()),
            NavSdkNavigationUtil.convertCoordinate(location.getLongitude()));
    }
    
    public void lookAtAnnotations(Vector annotations, long centerAnnotationId)
    {
        Vector annotationNames = new Vector();
        for (int i = 0; i < annotations.size(); i++)
        {
            AbstractAnnotation annotation = (AbstractAnnotation) annotations.elementAt(i);
            annotationNames.add("" + annotation.getAnnotationId());
        }
        setMapTransitionTime(0);
        mapComponent.lookAtAnnotations("" + this.getViewId(), annotationNames, "" + centerAnnotationId);
        updateZoomLevel();
    }
    
    public void lookAtAnnotationsByName(Vector annotationNames)
    {
        setMapTransitionTime(0);
        mapComponent.lookAtAnnotations("" + this.getViewId(), annotationNames, null);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                updateZoomLevel();
            }
        }).start();
    }
    
    public void enableAnnotationLayer(int layer, boolean enable)
    {
        mapComponent.enableAnnotationLayer("" + this.getViewId(), layer, enable);
    }

    public void enableAnnotationByName(String annotationName, boolean enable)
    {
        mapComponent
                .enableAnnotationByName("" + this.getViewId(), annotationName, enable);
    }

    public void displayAnnotationLayer(int layer, boolean isDisplay)
    {
        mapComponent.displayAnnotationLayer("" + this.getViewId(), layer, isDisplay);
    }

    public void displayAnnotationByName(String annotationName, boolean isDisplay)
    {
        mapComponent.displayAnnotationByName("" + this.getViewId(), annotationName,
            isDisplay);
    }
	
	//This is to avoid the duplicate add during fix TNANDROID-3065.
    public void initMapEngine()
    {
        if(mapComponent != null)
        {
            mapComponent.initMapEngine();
        }
        isInitialized = true;
    }
    
    //This is to avoid the duplicate add during fix TNANDROID-3065.
    public void removeComponetFromFormerParent(AbstractTnComponent component)
    {
        if (component != null)
        {
            AbstractTnComponent parent = component.getParent();
            if(parent instanceof AbstractTnContainer)
            {
                ((AbstractTnContainer) parent).remove(component);
            }
            else if(parent instanceof TnScrollPanel)
            {
                ((TnScrollPanel) parent).removeAllViews();
            }
        }
    }

    /**** features should be in the same type, annotation or component ***********************/
    public void addFeature(final Vector features, boolean isAnnotation)
    {
        if (isAnnotation)
        {
            for (int i = 0; i < features.size(); i++)
            {
                AbstractTnComponent component = (AbstractTnComponent) features
                        .elementAt(i);
                AbstractAnnotation anntotaion = (AbstractAnnotation) component;
                long annotationId = anntotaion.addToMap();
                component.setCookie(PrimitiveTypeCache
                        .valueOf(annotationId2ComponentId(annotationId)));
                component.setId((int) annotationId2ComponentId(annotationId));
                addedComponents.put(component.getCookie(), component);
            }
        }
        else
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    for (int i = 0; i < features.size(); i++)
                    {
                        AbstractTnComponent component = (AbstractTnComponent) features.elementAt(i);
                        removeComponetFromFormerParent(component);
                        contentContainer.add(component);
                        component.setCookie(PrimitiveTypeCache.valueOf(component.getId()));
                        addedComponents.put(component.getCookie(), component);
                    }
                }
            });
        }
    }

    public void addFeature(final AbstractTnComponent component)
    {
        if (component instanceof AbstractAnnotation)
        {
            AbstractAnnotation anntotaion = (AbstractAnnotation) component;
            long annotationId = anntotaion.addToMap();
            component.setCookie(PrimitiveTypeCache
                    .valueOf(annotationId2ComponentId(annotationId)));
            component.setId((int) annotationId2ComponentId(annotationId));
            addedComponents.put(component.getCookie(), component);
        }
        else if(component != null)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    long componentId = component.getId();
                    removeComponetFromFormerParent(component);
                    contentContainer.add(component);
                    component.setCookie(PrimitiveTypeCache.valueOf(componentId));
                    addedComponents.put(component.getCookie(), component);
                }
            });
        }
    }
    
    public void showRoutes(Vector routeNames, String highlightRoute, boolean isDecimatedRoute)
    {
        mapComponent.showRoutes("" + this.getViewId(), routeNames, highlightRoute, isDecimatedRoute);
    }
    
    public void showBetterRoutes(Vector routeNames, String newRoute)
    {
        mapComponent.showBetterRoutes("" + this.getViewId(), routeNames, newRoute);
    }
    
    public void setRenderingMode(int mode)
    {
        mapComponent.setRenderingMode("" + this.getViewId(), mode);
        this.renderMode = mode;
    }
    
    public void setMapVerticalOffset(float verticalOffset)
    {
        mapComponent.setMapVerticalOffset("" + this.getViewId(), verticalOffset);
    }
    
    public int getRenderingMode()
    {
        return this.renderMode;
    }
    
    public boolean hasCleanAll()
    {
        return addedComponents.size() == 0;
    }
    
    public void setZoomLevel(float zoomLevel)
    {
        mapComponent.setZoomLevel("" + this.getViewId(), zoomLevel);
        this.updateZoomBtnState(zoomLevel, false);
        updateZoomLevel(zoomLevel);
    }
    
    public void setZoomLevel(float zoomLevel, boolean needAnimation)
    {
        if (needAnimation)
        {
            this.setMapTransitionTime(MapContainer.getInstance().getTransitionTime());
        }
        setZoomLevel(zoomLevel);
    }
    
    public void removeFeature(final AbstractTnComponent component)
    {
        if(component == null || component.getCookie() == null)
        {
            return;
        }
        if(component instanceof AbstractAnnotation)
        {
            Long key = (Long) component.getCookie();
            long anntotationId = componentId2AnnotationId(key.longValue());
            long graphicId = ((AbstractAnnotation) component).getGraphicId();
            this.unloadAsset("" + this.getViewId(), "" + graphicId);
            this.removeAnnotationByName("" + anntotationId);
            if (addedComponents.get(key) != null)
            {
                addedComponents.remove(key);
            }
        }
        else
        {
            ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    contentContainer.remove(component);
                    addedComponents.remove(component.getCookie());
                }
            });
        }
    }
    
    public void updateFeature(final AbstractTnComponent component)
    {
        if (component instanceof AbstractAnnotation)
        {
            if (MapContainer.this.hasCleanAll())
            {
                return;
            }

            AbstractAnnotation newAnnotation = (AbstractAnnotation) component;
            MapViewUpdateAnnotationRequest.Builder updateBuilder = newAnnotation.updateGraphic();
            if(updateBuilder == null)
            {
                return;
            }
            this.updateAnnotation(updateBuilder);
        }
        else
        {
            if(component == null)
            {
                return;
            }
            ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if(MapContainer.this.hasCleanAll())
                    {
                        return;
                    }
                    
                    int componentId = component.getId();
                    AbstractTnComponent oldComponent = (AbstractTnComponent)addedComponents.get(PrimitiveTypeCache.valueOf(componentId));
                    
                    if(oldComponent != null)
                    {
                        contentContainer.remove(oldComponent);
                        addedComponents.remove(oldComponent.getCookie()); 
                    }
                    else
                    {
                        Logger.log(Logger.ERROR, this.getClass().getName(), "Old component " + componentId + " can not be found." );
                    }
                    
                    removeComponetFromFormerParent(component);
                    contentContainer.add(component);
                    component.setCookie(PrimitiveTypeCache.valueOf(component.getId()));
                    addedComponents.put(component.getCookie(), component);
                }
            });
        }
    }
    
    public void removeFeature(long componentId)
    {
        AbstractTnComponent component = this.getComponentById((int) componentId);
        this.removeFeature(component);
    }
    
    public AbstractTnComponent getComponentById(int id)
    {
        Long longId = PrimitiveTypeCache.valueOf((long) id);

        AbstractTnComponent tempCom = (AbstractTnComponent) addedComponents.get(longId);
        
        if (tempCom != null && tempCom.getId() == id)
        {
            return tempCom;
        }

        if (tempCom instanceof TnScrollPanel)
        {
            TnScrollPanel scrollPanel = (TnScrollPanel) tempCom;
            tempCom = scrollPanel.get();
        }


        if (tempCom instanceof AbstractTnContainer)
        {
            tempCom = ((AbstractTnContainer) tempCom).getComponentById(id);
        }

        if (tempCom != null && tempCom.getId() == id)
        {
            return tempCom;
        }

        return null;
    }
    
    public AbstractTnComponent getFeature(long componentId)
    {
        AbstractTnComponent component = (AbstractTnComponent)addedComponents.get(PrimitiveTypeCache.valueOf(componentId));
        return component;
    }
    
    private long componentId2AnnotationId(long componentId)
    {
        if(componentId > ICommonConstants.ANOTATION_COMPONENT_TRANSLATE)
            return (componentId - ICommonConstants.ANOTATION_COMPONENT_TRANSLATE);
        else
            return -1;
    }
    
    private long annotationId2ComponentId(long annotationId)
    {
        return ICommonConstants.ANOTATION_COMPONENT_TRANSLATE  + annotationId;
    }
    
    public float getTransitionTime()
    {
        return transitionTime;
    }
    
    public float getFasterTransitionTime()
    {
        return transitionTime / 4;
    }
    
    /**
     * Q: Why not using getFasterTransitionTime()?
     * 
     * A: There is performance issue when switching between POI annotation. 
     *    If the animation is too fast, we will see screen flash.
     * 
     * @return
     */
    public float getPoiTransitionTime()
    {
        return transitionTime / 2;
    }

    public void setMapTransitionTime(float transitionTime)
    {
        if(transitionTime > 0.0f)
        {
            mapComponent.setMaxFps(GLMapRenderer.MAX_FPS);
        }
        mapComponent.setTransitionTime("" + getViewId(), transitionTime);
    }
    
    public void cleanAll(final boolean needResetZoomLevel)
    {
//        final Object cleanMutex = new Object();
//        this.setMapRect(null, null, null, null, new IMapResizeViewListener()
//        {
//            
//            public void mapViewSizeChanged()
//            {
//                synchronized (cleanMutex)
//                {
//                    cleanMutex.notifyAll();
//                }
//            }
//        });
        
//        try
//        {
//            synchronized (cleanMutex)
//            {
//                cleanMutex.wait(700);
//            }
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
        this.setMapRect(null, null, null, null);
        
        //clean all components exclude mapComponent
        ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                //we should keep map component which index is 0
                for(int i = contentContainer.getChildrenSize() - 1; i >= 1; i--)
                {
                    contentContainer.remove(i);
                }
            }
        });
        
        setEnableMapUI(true);
        // clean up map rect to full screen

        zoomOutBtnEnabled = true;
        zoomInBtnEnabled = true;

        setRenderingMode(NavSdkMapProxy.RENDERING_MODE_2D_NORTH_UP);
        removeAllAnnotations(); 
        addedComponents.clear();
        enableAdi(false);
        setMapPanAndZoom();
    }
    
    public void cleanAll()
    {
        cleanAll(true);
    }
    
    public void clearRoute()
    {
        mapComponent.clearRoutes("" + this.getViewId());
    }
    
    public void setMapPanAndZoom()
    {
        mapComponent.setMapPanAndZoom("" + this.getViewId());
    }
    
    public void removeIncidentAnnotations()
    {
        //Improve efficiency
        mapComponent.removeAnnotationByLayer("" + this.getViewId(), IMapContainerConstants.NAV_SDK_Z_ORDER_TRAFFIC_INCIDENT_CUSTOM);
        Enumeration keys = addedComponents.keys();
        while (keys.hasMoreElements())
        {
            Long key = (Long) keys.nextElement();
            if (key.longValue() > ICommonConstants.ANOTATION_COMPONENT_TRANSLATE)
            {
                final Object annotation = addedComponents.get(key);
                if (annotation instanceof IncidentAnnotation)
                {
                    addedComponents.remove(key);
                }
            }
        }
    }
    
    
    public void removeAddressAnnotations()
    {
        mapComponent.removeAnnotationByLayer("" + this.getViewId(),
            IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        mapComponent.removeAnnotationByLayer("" + this.getViewId(),
            IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER);
        Enumeration keys = addedComponents.keys();
        while (keys.hasMoreElements())
        {
            Long key = (Long) keys.nextElement();
            if (key.longValue() > ICommonConstants.ANOTATION_COMPONENT_TRANSLATE)
            {
                final Object annotation = addedComponents.get(key);
                if (annotation instanceof AddressAnnotation)
                {
                    addedComponents.remove(key);
                }
            }
        }
    }

    public void removePoiAnnotations()
    {
        mapComponent.removeAnnotationByLayer("" + this.getViewId(),
            IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        mapComponent.removeAnnotationByLayer("" + this.getViewId(),
            IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER);

        Enumeration keys = addedComponents.keys();
        while (keys.hasMoreElements())
        {
            Long key = (Long) keys.nextElement();
            if (key.longValue() > ICommonConstants.ANOTATION_COMPONENT_TRANSLATE)
            {
                final Object annotation = addedComponents.get(key);
                if (annotation instanceof PoiAnnotation)
                {
                    addedComponents.remove(key);
                }
            }
        }

    }
    
    public void removeAllAnnotations()
    {
        mapComponent.removeAllAnnotations("" + this.getViewId());
    }
    
    public void zoomInMap()
    {
        int zoomLevel = (int) (getZoomLevel() + 0.5) - 1;
        if (zoomLevel >= (NavRunningStatusProvider.getInstance().isNavRunning() ? MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL
                : MapConfig.MAP_MIN_ZOOM_LEVEL))
        {
            this.mapComponent.setZoomLevel("" + this.getViewId(), zoomLevel);
            updateZoomBtnState(zoomLevel, false);
            updateZoomLevel(zoomLevel);
        }
        else
        {
            updateZoomBtnState(MapConfig.MAP_MIN_ZOOM_LEVEL, false);
        }
    }

    public void zoomOutMap()
    {
        int zoomLevel = (int) (getZoomLevel() + 0.5) + 1;
        if (zoomLevel <= (isNavConfig ? MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL
                : MapConfig.MAP_MAX_ZOOM_LEVEL))
        {
            this.mapComponent.setZoomLevel("" + this.getViewId(), zoomLevel);
            updateZoomBtnState(zoomLevel, false);
            updateZoomLevel(zoomLevel);
        }
        else
        {
            updateZoomBtnState(MapConfig.MAP_MAX_ZOOM_LEVEL, false);
        }
    }
    
    private void enableZoomButton(AbstractTnComponent zoomBtn, boolean isZoomIn,
            boolean isEnabled)
    {
        boolean isDayColor = true;
        try
        {
            isDayColor = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? false
                    : true;
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        if (isZoomIn)
        {
            if (isEnabled)
            {
                AbstractTnImage unfocusedImage = isDayColor ? ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_UNFOCUSED
                        .getImage() : ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_UNFOCUSED
                        .getImage();
                TnUiArgAdapter bgImageAdapter = isDayColor ? NinePatchImageDecorator.MAP_ZOOM_TOP_BG_DAY_UNFOCUSED
                        : NinePatchImageDecorator.MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED;

                ((FrogButton) zoomBtn).setIcon(
                    ImageDecorator.IMG_MAP_ZOOM_IN_ICON_FOCUSED.getImage(),
                    unfocusedImage, AbstractTnGraphics.VCENTER
                            | AbstractTnGraphics.HCENTER);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, bgImageAdapter);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                    NinePatchImageDecorator.MAP_ZOOM_TOP_BG_FOCUSED);
                this.zoomInBtnEnabled = true;
            }
            else
            {
                AbstractTnImage disableIcon = isDayColor ? ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_DISABLE
                        .getImage() : ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_DISABLE
                        .getImage();
                TnUiArgAdapter bgImageAdapter = isDayColor ? NinePatchImageDecorator.MAP_ZOOM_TOP_BG_DAY_UNFOCUSED
                        : NinePatchImageDecorator.MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED;

                ((FrogButton) zoomBtn).setIcon(disableIcon, disableIcon,
                    AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, bgImageAdapter);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, bgImageAdapter);
                this.zoomInBtnEnabled = false;
            }
        }
        else
        {
            if (isEnabled)
            {

                AbstractTnImage unfocusedImage = isDayColor ? ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_UNFOCUSED
                        .getImage()
                        : ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_UNFOCUSED.getImage();
                TnUiArgAdapter bgImageAdapter = isDayColor ? NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED
                        : NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED;

                ((FrogButton) zoomBtn).setIcon(
                    ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage(),
                    unfocusedImage, AbstractTnGraphics.VCENTER
                            | AbstractTnGraphics.HCENTER);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, bgImageAdapter);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                    NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_FOCUSED);
                this.zoomOutBtnEnabled = true;
            }
            else
            {
                AbstractTnImage disableIcon = isDayColor ? ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_DISABLE
                        .getImage() : ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_DISABLE
                        .getImage();
                TnUiArgAdapter bgImageAdapter = isDayColor ? NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED
                        : NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED;
                ((FrogButton) zoomBtn).setIcon(disableIcon, disableIcon,
                    AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, bgImageAdapter);
                ((FrogButton) zoomBtn).getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, bgImageAdapter);
                this.zoomOutBtnEnabled = false;
            }
        }
    }
        
    /**
     * when at the max or min level, zoom in or zoom out button should disable
     * @param zoomLevel the current zoom level, -1 mean not set
     * @param needRedo if true, it update once more after 1 sec (the setlevel and getlevel are asych, sometime you can't get the newest zoomlevel)
     **/
    public void updateZoomBtnState(final float zoomLevel, final boolean needRedo)
    {
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                float tempZoomLevel = zoomLevel;
                if (needRedo)
                {
                    TnUiTimer.getInstance().addReceiver(mapComponent, 2000);// 1000 not must, only a magic number
                }

                if (tempZoomLevel == -1)
                {
                    tempZoomLevel = (int) (MapContainer.this.getZoomLevel() + 0.5);
                }

                if ((tempZoomLevel < MapConfig.MAP_MIN_ZOOM_LEVEL) || (tempZoomLevel > MapConfig.MAP_MAX_ZOOM_LEVEL))
                {
                    if (tempZoomLevel > MapConfig.MAP_MAX_ZOOM_LEVEL)
                    {
                        tempZoomLevel = MapConfig.MAP_MAX_ZOOM_LEVEL;
                        MapContainer.this.setZoomLevel(MapConfig.MAP_MAX_ZOOM_LEVEL);
                    }
                }
                if (tempZoomLevel != MapConfig.MAP_MIN_ZOOM_LEVEL && tempZoomLevel != MapConfig.MAP_MAX_ZOOM_LEVEL
                        && MapContainer.this.zoomInBtnEnabled == true && MapContainer.this.zoomOutBtnEnabled == true) // the most situations
                                                                                            // needn't update state
                {
                    return;
                }

                if (tempZoomLevel == MapConfig.MAP_MIN_ZOOM_LEVEL && MapContainer.this.zoomInBtnEnabled == true)// disable zoomIn
                {
                    AbstractTnComponent zoomInBtn = MapContainer.this.contentContainer.getComponentById(ICommonConstants.ID_ZOOM_IN);
                    if (zoomInBtn == null)
                    {
                        return;
                    }
                    // disable zoomIn button
                    enableZoomButton(zoomInBtn, true, false);
                    return;
                }

                if (tempZoomLevel == MapConfig.MAP_MAX_ZOOM_LEVEL && MapContainer.this.zoomOutBtnEnabled == true)// disable zoomOut
                {
                    AbstractTnComponent zoomOutBtn = MapContainer.this.contentContainer.getComponentById(ICommonConstants.ID_ZOOM_OUT);
                    if (zoomOutBtn == null)
                    {
                        return;
                    }
                    // disable zoomOut button
                    enableZoomButton(zoomOutBtn, false, false);
                    return;
                }

                if (tempZoomLevel != MapConfig.MAP_MIN_ZOOM_LEVEL && MapContainer.this.zoomInBtnEnabled == false)// enable zoomIn
                {
                    AbstractTnComponent zoomInBtn = MapContainer.this.contentContainer.getComponentById(ICommonConstants.ID_ZOOM_IN);
                    if (zoomInBtn == null)
                    {
                        return;
                    }
                    // enable zoomIn button
                    enableZoomButton(zoomInBtn, true, true);
                    return;
                }

                if (tempZoomLevel != MapConfig.MAP_MAX_ZOOM_LEVEL && MapContainer.this.zoomOutBtnEnabled == false)// enable zoomOut
                {
                    AbstractTnComponent zoomOutBtn = MapContainer.this.contentContainer.getComponentById(ICommonConstants.ID_ZOOM_OUT);
                    if (zoomOutBtn == null)
                    {
                        return;
                    }
                    // enable zoomOut button
                    enableZoomButton(zoomOutBtn, false, true);
                    return;
                }
            }
        });
        t.start();
    }
    
    public void moveMapTo(final double lat, final double lon, final float heading, final float seconds)
    {
        MapContainer.getInstance().setMapTransitionTime(seconds);
        this.lookAt(lat, lon);
    }

    public void showMapLayer(int layerSetting)
    {
        boolean isShowTrafficOverlay = ((layerSetting & 0x01) != 0);
        int trafficIncidentFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_LAYER_TRAFFIC_INCIDENT);
        boolean isTrafficIncidentEnabled = false;
        if ((trafficIncidentFeature == FeaturesManager.FE_ENABLED || trafficIncidentFeature == FeaturesManager.FE_PURCHASED)
                && isShowTrafficOverlay)
        {
            isTrafficIncidentEnabled = true;
        }
        showMapLayer(layerSetting, isTrafficIncidentEnabled);
    }
    
    public void showMapLayer(int layerSetting, boolean trafficIncidentEnabled)
    {
        boolean isShowTrafficOverlay = ((layerSetting & 0x01) != 0); 
        boolean isShowSatelliteOverlay = ((layerSetting & 0x02) != 0);  
        boolean isShowTrafficCameraOverlay = ((layerSetting & 0x04) != 0);  
        boolean isShowLandMarkOverlay = ((layerSetting & 0x08) != 0); 
        
        AbstractTnComponent component = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
        if (component != null && component instanceof MapViewListComponent)
        {
            MapViewListComponent mapViewListComponent = (MapViewListComponent)component;
            mapViewListComponent.setLayerSetting(layerSetting);
        }
        
        showMapLayer(isShowTrafficOverlay, isShowSatelliteOverlay, isShowTrafficCameraOverlay, isShowLandMarkOverlay, trafficIncidentEnabled);
    }
    
    private void showMapLayer(final boolean isShowTrafficOverlay, final boolean isShowSatelliteOverlay, 
            final boolean isShowTrafficCameraOverlay, final boolean isShowLandMarkOverlay, final boolean isShowIncident)
    {
        mapComponent.showMapLayer(isShowTrafficOverlay, isShowSatelliteOverlay, isShowTrafficCameraOverlay, isShowLandMarkOverlay, isShowIncident, "" + this.getViewId());
        if(isShowSatelliteOverlay != currentIsShowSatellite)
        {
            currentIsShowSatellite = isShowSatelliteOverlay;
            
            final String colorConfig = getMapColorConfig(DayNightService.getInstance().getMapColor(currentIsShowSatellite));
            this.loadConfiguration(colorConfig);
            changeMapFeatureStyle();
        }
    }
    
    public void loadConfiguration(String config)
    {
        this.mapComponent.loadConfiguration("" + this.getViewId(), config);
    }
    
    public void setMapUIEventListener(IMapUIEventListener mapUIEventListener)
    {
        mapComponent.setMapUIEventListener(mapUIEventListener);
    }

    public void removeMapUIEventListener()
    {
        mapComponent.removeMapUIEventListener();
    }

    public void postRenderEvent(Runnable runnable)
    {
        mapComponent.postRenderEvent(runnable);
    }
    
    public void updateMapColor()
    {
        int mapColor = DayNightService.getInstance().getMapColor(currentIsShowSatellite);
        if (currentDayNightColor != mapColor)
        {
            final String colorConfig = getMapColorConfig(mapColor);
            
            currentDayNightColor = mapColor;
            this.loadConfiguration(colorConfig);
            RouteUiHelper.resetCurrentRoute();
            changeMapFeatureStyle();
        }
    }
    
    public void updateMapColorMode()
    {
        boolean isAuto = DayNightService.getInstance().isMapColorAutoMode();
        if (isAuto != isMapColorAuto)
        {
            isMapColorAuto = isAuto;
            if (isMapColorAuto)
            {
                DayNightService.getInstance().start();
                DayNightService.getInstance().addListener(this);
            }
            else
            {
                DayNightService.getInstance().stop();
                DayNightService.getInstance().removeListener(this);
            }
        }
    }
    
    private void changeMapFeatureStyle()
    {
        //change night or day
        boolean isNightColor = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR;
        
        AbstractTnComponent mapViewList = this.getFeature(ICommonConstants.ID_MAP_VIEW_LIST_COMPONENT);
        if(mapViewList instanceof MapViewListComponent)
        {
            ((MapViewListComponent)mapViewList).setMapColor(!isNightColor);
        }
       
        AbstractTnComponent currentLocation = this.getFeature(ICommonConstants.ID_CURRENT_LOCATION);
        if(currentLocation instanceof FrogButton)
        {
            AbstractTnImage unfocusedImage = isNightColor ? ImageDecorator.IMG_CURRENT_LOCATION_NIGHT_UNFOCUSED.getImage():ImageDecorator.IMG_CURRENT_LOCATION_UNFOCUSED.getImage();
            TnUiArgAdapter bgImageAdapter = isNightColor ? NinePatchImageDecorator.MAP_BUTTON_NIGHT_UNFOCUSED : NinePatchImageDecorator.MAP_BUTTON_UNFOCUSED;
            ((FrogButton)currentLocation).setIcon(ImageDecorator.IMG_CURRENT_LOCATION_FOCUSED.getImage(), unfocusedImage, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER, false);
            ((FrogButton)currentLocation).getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, bgImageAdapter);
        }
        
        //only when zoomIn button is enable, set the zoomIn button by the default image 
        if(this.zoomInBtnEnabled)
        {
            AbstractTnComponent zoomInButton = this.contentContainer.getComponentById(ICommonConstants.ID_ZOOM_IN);
            if (zoomInButton instanceof FrogButton)
            {
                AbstractTnImage unfocusedImage = isNightColor ? ImageDecorator.IMG_MAP_ZOOM_IN_NIGHT_ICON_UNFOCUSED.getImage() : ImageDecorator.IMG_MAP_ZOOM_IN_DAY_ICON_UNFOCUSED.getImage();
                TnUiArgAdapter bgImageAdapter = isNightColor ? NinePatchImageDecorator.MAP_ZOOM_TOP_BG_NIGHT_UNFOCUSED : NinePatchImageDecorator.MAP_ZOOM_TOP_BG_DAY_UNFOCUSED;
                ((FrogButton) zoomInButton).setIcon(ImageDecorator.IMG_MAP_ZOOM_IN_ICON_FOCUSED.getImage(), unfocusedImage, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER, false);
                ((FrogButton) zoomInButton).getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, bgImageAdapter);
            }
        }

        //only when zoomOut button is enable, set the zoomOut button by the default image
        if(this.zoomOutBtnEnabled)
        {
            AbstractTnComponent zoomOutButton = this.contentContainer.getComponentById(ICommonConstants.ID_ZOOM_OUT);
            if (zoomOutButton instanceof FrogButton)
            {
                AbstractTnImage unfocusedImage = isNightColor ? ImageDecorator.IMG_MAP_ZOOM_OUT_NIGHT_ICON_UNFOCUSED.getImage() : ImageDecorator.IMG_MAP_ZOOM_OUT_DAY_ICON_UNFOCUSED.getImage();
                TnUiArgAdapter bgImageAdapter = isNightColor ? NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_NIGHT_UNFOCUSED : NinePatchImageDecorator.MAP_ZOOM_BOTTOM_BG_DAY_UNFOCUSED;
                ((FrogButton) zoomOutButton).setIcon(ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage(), unfocusedImage, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER, false);
                ((FrogButton) zoomOutButton).getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, bgImageAdapter);
            }
        }

        
        AbstractTnComponent mapProvider = this.getFeature(ICommonConstants.ID_MAP_PROVIDER);
        if(mapProvider instanceof FrogLabel)
        {
            int color = isNightColor ? UiStyleManager.getInstance().getColor(UiStyleManager.MAP_PROVIDER_TEXT_NIGHT_COLOR): UiStyleManager.getInstance().getColor(UiStyleManager.MAP_PROVIDER_TEXT_DAY_COLOR);
            ((FrogLabel)mapProvider).setForegroundColor(color, color);
        }

        AbstractTnComponent mapCompanyLog = this.getFeature(ICommonConstants.ID_MAP_COMPANY_LOGO);
        if(mapCompanyLog instanceof FrogImageComponent)
        {
            AbstractTnImage image = isNightColor ? ImageDecorator.IMG_NIGHT_LOGO_ON_MAP.getImage():ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage();
            ((FrogImageComponent)mapCompanyLog).setImage(image);
        }
        
        contentContainer.requestPaint();
    }
    
    public String getMapColorConfig(int mapColor)
    {
        if (mapColor == IMapContainerConstants.MAP_DAY_COLOR)
            return "config_day.json";
        else if (mapColor == IMapContainerConstants.MAP_NIGHT_COLOR)
            return "config_night.json";
        else if (mapColor == IMapContainerConstants.MAP_SAT_COLOR)
            return "config_SAT.json";
        else
            return null;
    }

    public long getViewId()
    {
//        IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//        return mapEngine.getViewId();
        return 1;
    }

    
    
    public void setEnableMapUI(boolean enableMapUIEvent)
    {
            mapComponent.setEnableMapUIEvent(enableMapUIEvent);
    }
    
    public void setEnableGLEngineUIEvent(boolean enableEngineUIEvent)
    {
            mapComponent.setEnableGLEngineUIEvent(enableEngineUIEvent);
    }

    public Address getMapCenter()
    {
//        IMapEngine.EngineState state = getViewState();
        Address centerAddress = null;
//        if (state != null)
//        {
//            centerAddress = new Address(Address.SOURCE_MAP);
//            Stop stop = new Stop();
//            stop.setLat((int) (state.cameraLatitude * 100000));
//            stop.setLon((int) (state.cameraLongitude * 100000));
//            stop.setType(Stop.STOP_CURRENT_LOCATION);
//            centerAddress.setStop(stop);
//        }
        return centerAddress;
    }
    
    double cameraDeclination;
    
    //TODO
    Object cameraDeclinationMutex = new Object();
    public double getCameraDeclination()
    {
//        synchronized(cameraDeclinationMutex)
//        {
//            Runnable run = new Runnable()
//            {
//                public void run()
//                {
//                    getCameraDeclinationImpl();
//                }
//            };
//            this.postRenderEvent(run);
//            try
//            {
//                cameraDeclinationMutex.wait(500);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
        
        return cameraDeclination;
    }
    
//    private void getCameraDeclinationImpl()
//    {
////need NavSdk to confirm whether need client to handle the camera declination
////        synchronized (cameraDeclinationMutex)
////        {
////            IMapEngine mapEngine = (AbstractMapEngine) MapEngineManager.getInstance()
////                    .getMapEngine();
////            cameraDeclination = mapEngine.getDouble(mapEngine.getViewId(),
////                IMapEngine.PARAMETER_DOUBLE_CAMERA_DECLINATION);
////            cameraDeclinationMutex.notifyAll();
////        }
//    }
    
    float zoomLevel;

    public float getZoomLevel()
    {
        return getZoomLevel(false);
    }
    
    Object zoomLevelMutex = new Object();
    public float getZoomLevel(boolean needSync)
    {
        if(!needSync)
            return zoomLevel;
        
        synchronized(zoomLevelMutex)
        {
            getZoomLevelImpl();
            try
            {
                zoomLevelMutex.wait(800);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return zoomLevel;
    }

    private void getZoomLevelImpl()
    {
        Timer syncZoomLevelTimer = new Timer();

        GetZoomLevelTask task = new GetZoomLevelTask();

        syncZoomLevelTimer.schedule(task, SYNC_ZOOM_LEVEL_DELAY);
    }
    
    protected void updateZoomLevel(float zoomLevel)
    {
        synchronized (zoomLevelMutex)
        {
            this.zoomLevel = zoomLevel;
            zoomLevelMutex.notifyAll();
        }
    }
    
    public void updateZoomLevel()
    {
        float zoomLevel = this.getZoomLevel(true);
        updateZoomLevel(zoomLevel);
    }
    
/*  public double[] getLatLon(int screenX, int screenY)
    {
        double arrLatLon[] = new double[2];
        final int x = screenX;
        final int y = screenY;
        
//      final double arrLat[] = new double[1];
//      final double arrLon[] = new double[1];
        synchronized (this)
        {
            if ((screenX >= 0) && (screenY >= 0))
            {
                Runnable run = new Runnable()
                {
                    public void run()
                    {
                        getLatLonImpl(x, y);
                    }
                };
                this.postRenderEvent(run);
            }
            try
            {
                this.wait(500);
            }
            catch (Exception e)
            {

            }
        }
        arrLatLon[0] = lat[0];
        arrLatLon[1] = lon[0];
        
        return arrLatLon; 
    }
    
    public void getLatLonImpl(int x, int y)
    {
        synchronized(this)
        {
            IMapEngine mapEngine = (AbstractMapEngine) MapEngineManager.getInstance().getMapEngine();
            float zoomLevel = mapEngine.getZoomLevel(mapEngine.getViewId());
            mapEngine.setZoomLevel(mapEngine.getViewId(), zoomLevel, x, y, lat, lon);
            System.out.println("DF-lat"+lat[0]+"lon:"+lon[0]);

        }
    }
    */
//    public void setZoomLevel(int iZoomLevel, int screenX, int screenY)
//    {
//        setZoomLevel(iZoomLevel, screenX, screenY, true);
//    }
    
//    public void setZoomLevel(int iZoomLevel, int screenX, int screenY, final boolean needAnnimation)
//    {
//        if( (iZoomLevel < MapConfig.MAP_MIN_ZOOM_LEVEL) || (iZoomLevel > MapConfig.MAP_MAX_ZOOM_LEVEL) )
//        {
//            return;
//        }
//      
//
//      final float zoom = (float)iZoomLevel;
//      final int x = screenX;
//      final int y = screenY;
//      
//      if( (screenX >= 0) && (screenY >= 0) )// change zoom at (x,y)
//      {
//          MapContainer.getInstance().postRenderEvent(new Runnable()
//              {
//              public void run()
//                  {
//
//                  IMapEngine mapEngine = (AbstractMapEngine) MapEngineManager.getInstance().getMapEngine();
//                  mapEngine.setZoomLevel(mapEngine.getViewId(), zoom, x, y, lat, lon);
//                  if(Math.abs(lat[0]) >= Double.MIN_VALUE && Math.abs(lon[0]) >= Double.MIN_VALUE)
//                  {
//                      mapEngine.setInteractionMode(mapEngine.getViewId(), IMapEngine.INTERACTION_MODE_PAN_AND_ZOOM);
//                        if (needAnnimation)
//                        {
//                            mapEngine.setTransitionTime(mapEngine.getViewId(),
//                                MapContainer.getInstance().getTransitionTime());
//                        }
//                        mapEngine.lookAt(mapEngine.getViewId(), lat[0], lon[0], 0.0f);
//                        if(mapUiEventListener != null)
//                        {
//                            mapUiEventListener.onDoubleTap();
//                        }
//                  }
//                  
//                  }
//               
//              });
//      }
//      else
//      {
//          MapContainer.getInstance().postRenderEvent(new Runnable()
//              {
//              public void run()
//                  {
//                  IMapEngine mapEngine = (AbstractMapEngine) MapEngineManager.getInstance().getMapEngine();
//                  mapEngine.setZoomLevel(mapEngine.getViewId(),zoom);
//                  }
//               
//              });
//      }
//      this.zoomLevel = zoom;  
//      this.updateZoomBtnState(iZoomLevel, false);
//    }
    
    public int getRenderingModeFromPreference()
    {
        int renderingMode =  IMapContainerConstants.RENDERING_MODE_3D_HEADING_UP;
        PreferenceDao preferenceDao = DaoManager.getInstance().getPreferenceDao();
        Preference mapStylePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_MAPSTYLE);
        if (mapStylePref != null && mapStylePref.getIntValue()== Preference.MAP_TYPE_2D)
        {
            renderingMode = IMapContainerConstants.RENDERING_MODE_2D_HEADING_UP;
        }
        return renderingMode;
    }
    
    public int getRenderingModeFromTripDao()
    {
        int renderingMode =  IMapContainerConstants.RENDERING_MODE_3D_HEADING_UP;
        TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
        Preference mapStylePref = tripsDao.getPreference(Preference.ID_PREFERENCE_MAPSTYLE);
        if (mapStylePref != null && mapStylePref.getIntValue()== Preference.MAP_TYPE_2D)
        {
            renderingMode = IMapContainerConstants.RENDERING_MODE_2D_HEADING_UP;
        }
        return renderingMode;
        
    }
    
    public void saveRenderingModeInPreference(int renderingMode)
    {
        int style = Preference.MAP_TYPE_3D;
        if (renderingMode == IMapContainerConstants.RENDERING_MODE_2D_NORTH_UP
                || renderingMode == IMapContainerConstants.RENDERING_MODE_2D_HEADING_UP)
        {
            style = Preference.MAP_TYPE_2D;
        }
        
        PreferenceDao preferenceDao = DaoManager.getInstance().getPreferenceDao();
        Preference mapStylePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_MAPSTYLE);
        if(mapStylePref != null)
        {
            mapStylePref.setIntValue(style);
            preferenceDao.store(RegionUtil.getInstance().getCurrentRegion());
        }
    }
    
    public void saveRenderingModeInTripsDao(int renderingMode)
    {
        int style = Preference.MAP_TYPE_3D;
        if (renderingMode == IMapContainerConstants.RENDERING_MODE_2D_NORTH_UP
                || renderingMode == IMapContainerConstants.RENDERING_MODE_2D_HEADING_UP)
        {
            style = Preference.MAP_TYPE_2D;
        }
        
        TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
        Preference mapStylePref = tripsDao.getPreference(Preference.ID_PREFERENCE_MAPSTYLE);
        if(mapStylePref != null)
        {
            mapStylePref.setIntValue(style);
        }        
    }
    
    public void initMap()
    {
        int layerSetting = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_MAP_LAYER_SETTING);
        if(layerSetting < 0)
        {
            layerSetting = 0;
        }
        
        showMapLayer(layerSetting);
        
        changeToSpriteVehicleAnnotation();
        
        TnLocation loc = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        
        boolean isValidLastKnownGpsTemp = false;

        if (loc != null)
        {
            // location from network
            if (TnLocationManager.NETWORK_PROVIDER.equalsIgnoreCase(loc.getProvider())
                    || TnLocationManager.TN_NETWORK_PROVIDER.equalsIgnoreCase(loc.getProvider()))
            {                
                isValidLastKnownGpsTemp = System.currentTimeMillis() - loc.getLocalTimeStamp() < GPS_NETWORK_INTERVAL;          
            }
            else
            {
                isValidLastKnownGpsTemp = System.currentTimeMillis() - loc.getLocalTimeStamp() < GPS_SATELLIET_INTERVAL;             
            }
            final boolean isValidLastKnownGps = isValidLastKnownGpsTemp;
            
            final double lat = loc.getLatitude() / 100000.0d;
            final double lon = loc.getLongitude() / 100000.0d;

            MapContainer.getInstance().setMapTransitionTime(0);
            MapContainer.getInstance().lookAt(lat, lon);
            if (isValidLastKnownGps)
            {
                MapContainer.getInstance().enableVehicleAnnotation();
            }
            else
            {
                MapContainer.getInstance().disableCar();
            }
        }
        else
        {
            loc = LocationProvider.getInstance().getDefaultLocation();
            final double lat = loc.getLatitude() / 100000.0d;
            final double lon = loc.getLongitude() / 100000.0d;
            if (spriteVehicleAnnotationName.trim().length() > 0)
            {
                this.enableAnnotationByName(spriteVehicleAnnotationName, false);
            }
            this.setMapTransitionTime(0);
            this.interactionModeChanged(IMapContainerConstants.INTERACTION_MODE_PAN_AND_ZOOM);
            this.lookAt(lat, lon);
            this.setZoomLevel(MapConfig.MAP_MAX_ZOOM_LEVEL - 1);
            updateZoomBtnState(MapConfig.MAP_MAX_ZOOM_LEVEL - 1, false);
        }
    }

    public boolean isAnchorAtCurrentLocation()
    {
        
//TODO need nav sdk to confirm the API        
        //JY: if you want to test on emulator please set time is 500
//        IMapEngine.EngineState engineState = this.getViewState();
//        TnLocation currentLocation = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
//        if (currentLocation == null)
//        {   
//            return false;
//        } 
//        else
//        {
//            int currLat = currentLocation.getLatitude();
//            int currLon = currentLocation.getLongitude();
//            if(engineState != null)
//            {
//                double lat =  engineState.cameraLatitude;
//                double lon =  engineState.cameraLongitude;
//                int dx = Math.abs(((int)(lat*100000)) - currLat);
//                int dy = Math.abs(((int)(lon*100000)) - currLon);
//                return DataUtil.distance(dx, dy) < GPS_MIN_DISTANCE; //10 means 10m
//            }
//            else
//            {
//                return false;
//            }
//        }
        return false;
     }
    
    protected void paint(AbstractTnGraphics graphics)
    {
    }

    
    public void interactionModeChanged(int newInteractionMode)
    {
        this.interactionMode = newInteractionMode;
        InteractionModeChangeListener listener = this.interactionModeChangeListener;
        if (listener != null)
        {
            listener.interactionModeChanged(newInteractionMode);
        }
    }
    
    public void setInteractionModeChangeListener(InteractionModeChangeListener listener)
    {
        this.interactionModeChangeListener = listener;
    }
    
    public int getInteractionMode()
    {
        return this.interactionMode;
    }
    
    public void addCarModel(byte[] skin, final byte[] model)
    {
        if(model == null || model.length == 0)
            return;
        if (this.modelVehicleAnnotationName.length() > 0)
        {
            this.removeAnnotationByName(modelVehicleAnnotationName);
        }
        this.loadAsset("" + this.getViewId(), 0, 0, model, MODEL_VEHICLE_ANNOTATION_NAME);
        modelVehicleAnnotationName = MODEL_VEHICLE_ANNOTATION_NAME;
        MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest
                .newBuilder();
        builder.setAssetName(modelVehicleAnnotationName);
        builder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_VEHICLE);
        this.addAnnotation("" + this.getViewId(), modelVehicleAnnotationName, builder);
    }
    
    public void addCarModel(byte[] skin, final String modelFile)
    {
        if (this.modelVehicleAnnotationName.length() > 0)
        {
            this.removeAnnotationByName(modelVehicleAnnotationName);
        }
        this.loadAsset("" + this.getViewId(), 0, 0, modelFile,
            MODEL_VEHICLE_ANNOTATION_NAME);
        modelVehicleAnnotationName = MODEL_VEHICLE_ANNOTATION_NAME;
        MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest
                .newBuilder();
        com.telenav.navsdk.events.PointOfInterestData.Location.Builder locationBuilder = com.telenav.navsdk.events.PointOfInterestData.Location
                .newBuilder();
        locationBuilder.setLatitude(1d);
        locationBuilder.setLongitude(1d);
        builder.setType(AnnotationType.AnnotationType_Screen);
        builder.setAssetName(modelVehicleAnnotationName);
        builder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_VEHICLE);
        builder.setLocation(locationBuilder.build());
        this.addAnnotation("" + this.getViewId(), modelVehicleAnnotationName, builder);
    }
    
    public void setMapRect(TnUiArgAdapter x, TnUiArgAdapter y, TnUiArgAdapter width, TnUiArgAdapter height)
    {
        mapComponent.setMapRect(x, y, width, height);
    }
    
    public void setMapRect(int x, int y, int width, int height)
    {
        mapComponent.setMapRect(x, y, width, height);
    }
    
    public void startPreloader()
    {
//        postRenderEvent(new Runnable()
//        {
//            public void run()
//            {
//                IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//                mapEngine.startPreloader();
//            }
//        });
        
    }
    
    public void stopPreloader()
    {
//        postRenderEvent(new Runnable()
//        {
//            public void run()
//            {
//                IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//                mapEngine.stopPreloader();
//            }
//        });
    }
    
    public void openMapView()
    {
        mapComponent.openMapView();
    }
    
    public long getLastControlEventTime()
    {
        if (mapComponent != null)
        {
            return mapComponent.getLastControlEventTime();
        }
        return 0;
    }
    
    public void setLastControlEventTime(long eventTime)
    {
        if (mapComponent != null)
        {
            mapComponent.setLastControlEventTime(eventTime);
        }
    }
    
    public boolean isSpriteVehicleAnnotationExisted()
    {
        return this.spriteVehicleAnnotationName.length() > 0;
    }
    
    public void changeToSpriteVehicleAnnotation()
    {
        if (isNavConfig)
        {
            this.loadConfiguration("config_map_view.json");
            isNavConfig = false;
        }
        
        if(!this.isCarEnabled)
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "car is disabled changeToSpriteVehicleAnnotation" );   
            }
            return;
        }
        
        isDisplayingModelVehicle = false;
        if (this.modelVehicleAnnotationName.length() > 0)
        {
            this.enableAnnotationByName(modelVehicleAnnotationName, false);
        }

        if (this.spriteVehicleAnnotationName.length() == 0)
        {
            // need to check the param of width and height of loadAssert

            AbstractTnImage dotImage = ImageDecorator.IMG_MAP_2D_DOT_UNFOCUSED.getImage();
            this.mapComponent.loadAsset("" + this.getViewId(), dotImage.getWidth(),
                dotImage.getHeight(), dotImage, SPRITE_VEHICLE_ANNOTATION_NAME);
            spriteVehicleAnnotationName = SPRITE_VEHICLE_ANNOTATION_NAME;
            String style = "screen_annotations.dot_screen_annotation";
            // **************************************************************************************************************************************
            // Here is a nav sdk bug, if you want add annotation which is not 'mod' into vehicle layer, you must set
            // the fake lat & lon
            // http://jira.telenav.com:8080/browse/CNSDK-3003
            com.telenav.navsdk.events.PointOfInterestData.Location.Builder locationBuilder = com.telenav.navsdk.events.PointOfInterestData.Location
                    .newBuilder();
            locationBuilder.setLatitude(1d);
            locationBuilder.setLongitude(1d);
            // **************************************************************************************************************************************
            MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest
                    .newBuilder();
            builder.setAssetName(spriteVehicleAnnotationName);
            builder.setLocation(locationBuilder.build());
            builder.setType(AnnotationType.AnnotationType_Sprite);
            builder.setStyle(style);
            builder.setPivotX(0.5f);
            builder.setPivotY(0.5f);
            builder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_VEHICLE);
            builder.setVisible(true);
            this.addAnnotation("" + this.getViewId(), spriteVehicleAnnotationName,
                builder);
        }
        else
        {
            this.enableAnnotationByName(SPRITE_VEHICLE_ANNOTATION_NAME, true);
        }
    }

    public void loadAsset(String viewName, int width, int height, byte[] data,
            String assetName)
    {
        this.mapComponent.loadAsset(viewName, width, height, data, assetName);
    }
    
    public void loadAsset(String viewName, int width, int height, AbstractTnImage image,
            String assetName)
    {
        this.mapComponent.loadAsset(viewName, width, height, image, assetName);
    }

    public void loadAsset(String viewName, int width, int height, String fileName,
            String assetName)
    {
        this.mapComponent.loadAsset(viewName, width, height, fileName, assetName);
    }

    public void unloadAsset(String viewName, String assetName)
    {
        this.mapComponent.unloadAsset(viewName, assetName);
    }
    
    public void addAnnotation(String viewName, String annotationName, MapViewAddAnnotationRequest.Builder builder)
    {
        this.mapComponent.addAnnotation(viewName, annotationName, builder);
    }
    
    public void updateAnnotation(MapViewUpdateAnnotationRequest.Builder builder)
    {
        builder.setViewName("" + getViewId());
        this.mapComponent.updateAnnotation(builder);
    }
    
    public void disableCar()
    {
        if(isCarEnabled)
        {
            this.isCarEnabled = false;
            disableVehicleAnnotation();
        }
    }

    public void enableCar()
    {
        if(!isCarEnabled)
        {
            this.isCarEnabled = true;
            enableVehicleAnnotation();
        }
    }
    
    public void disableVehicleAnnotation()
    {
        if (isDisplayingModelVehicle)
        {
            isDisplayingModelVehicle = false;
            this.mapComponent.enableAnnotationByName("" + this.getViewId(), MODEL_VEHICLE_ANNOTATION_NAME, false);
        }
        else
        {
            this.mapComponent.enableAnnotationByName("" + this.getViewId(), SPRITE_VEHICLE_ANNOTATION_NAME, false);
        }
    }
    
    public void enableVehicleAnnotation()
    {
        if(!this.isCarEnabled)
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "car is disabled enableVehicleAnnotation" );   
            }
            return;
        }
        
        if (NavSdkNavEngine.getInstance().isRunning())
        {
            isDisplayingModelVehicle = true;
            this.mapComponent.enableAnnotationByName("" + this.getViewId(), MODEL_VEHICLE_ANNOTATION_NAME, true);
        }
        else
        {
            this.mapComponent.enableAnnotationByName("" + this.getViewId(), SPRITE_VEHICLE_ANNOTATION_NAME, true);
        }
    }
    
    public void enableAdi(boolean enable)
    {
        this.mapComponent.enableAdi("" + this.getViewId(), enable);
    }
    
    void removeAnnotationByName(String annotationName)
    {
        this.mapComponent.removeAnnotationByName("" + this.getViewId(), annotationName);   
    }
    
    public void changeToModelVehicleAnnotation()
    {
        if(!isNavConfig)
        {
            this.loadConfiguration("config_nav_view.json");
            isNavConfig = true;
        }
        
        if(!this.isCarEnabled)
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "car is disabled changeToModelVehicleAnnotation" );   
            }
            return;
        }
        
        if (!isDisplayingModelVehicle)
        {
            if (this.spriteVehicleAnnotationName.length() > 0)
            {
                this.enableAnnotationByName(spriteVehicleAnnotationName, false);
            }

            if (!isDisplayingModelVehicle)
            {
                if (this.modelVehicleAnnotationName.length() > 0)
                {
                    this.enableAnnotationByName(modelVehicleAnnotationName, true);
                }
                else
                {
                    
                    CarModelManager.getInstance().loadCarModel();
                }
            }

            isDisplayingModelVehicle = true;
        }
    }
    
    public boolean isDisplayingModelVehicle()
    {
        return isDisplayingModelVehicle;
    }
    
    public boolean isPaused()
    {
        return isPaused;
    }
    
    public long getNotifyInterval()
    {
        return 1000;
    }

    public long getLastNotifyTimestamp()
    {
        return this.lastNotifiedTimeStamp;
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        this.lastNotifiedTimeStamp = timestamp;
    }

    public void notify(long timestamp)
    {
        if(isPaused)
        {
            if(timestamp - lastPauseTime > 15 * 1000)
            {
                Thread thread = new Thread(new Runnable()
                {
                    public void run()
                    {
                        synchronized (resumeMutex)
                        {
                            Notifier.getInstance().removeListener(MapContainer.this);
                            if(!isPaused)
                            {
                                return;
                            }
                            
                            pauseInternally();
                            
                            try
                            {
                                resumeMutex.wait(500);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }                        
                    }
                });
                thread.start();
            }
            else
            {
            }   
        }
        else
        {
            Notifier.getInstance().removeListener(this);
        }
    }

    public void reLayoutContentContainer()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                .runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        contentContainer.requestLayout();
                    }
                });
    }

    public void updateDayNightStatus()
    {
        updateMapColor();
    }
    
    public boolean isNight()
    {
        
        int mapColor = DayNightService.getInstance().getMapColor(currentIsShowSatellite);
        if(mapColor != IMapContainerConstants.MAP_DAY_COLOR)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isMapReady()
    {
        if(mapComponent != null && mapComponent.isMapReady())
        {
            return true;
        }

        return false;
    }
    
    public void resetMap()
    {
        spriteVehicleAnnotationName = "";

        if (isDisplayingModelVehicle)
        {
            changeToModelVehicleAnnotation();
        }
        else
        {
            changeToSpriteVehicleAnnotation();
        }

        if (IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE == interactionMode)
        {
            this.followVehicle();
        }
    }
    
    public void requestOpenGLScreenShot(IGLSnapBitmapCallBack callback)
    {
        requestOpenGLScreenShot(0, 0, this.getWidth(), this.getHeight(), callback, System.currentTimeMillis());
    }
    
    public void requestOpenGLScreenShot(int x, int y, int width, int height, IGLSnapBitmapCallBack callback, long transactionId)
    {
        if (x < 0 || y < 0 || width > this.getWidth() || height > this.getHeight())
        {
            if (callback != null)
            {
                callback.snapBitmapCanceled(transactionId);
            }
            return;
        }

        Renderer renderer = (Renderer) mapComponent.getRenderer().getNativeRenderer();
        if (renderer instanceof AndroidGLRenderer)
        {
            AndroidGLRenderer androidGLRenderer = (AndroidGLRenderer) renderer;
            androidGLRenderer.requestScreenShot(x, y, width, height, callback, transactionId);
        }

        else if (renderer instanceof AndroidGLRenderer16)
        {
            AndroidGLRenderer16 androidGLRenderer = (AndroidGLRenderer16) renderer;
            androidGLRenderer.requestScreenShot(x, y, width, height, callback, transactionId);
        }
    }
    
    public boolean hasMapUIEventListener()
    {
        return mapComponent.hasMapUIEventListener();
    }
    
    protected void updateMapView(int width, int height)
    {
        if (!mapComponent.isMapReady())
        {
            return;
        }
        mapComponent.updateMapView("" + this.getViewId(), width, height);
    }
    
    
    public void updateMapView()
    {
        if (!mapComponent.isMapReady())
        {
            return;
        }
        mapComponent.updateMapView("" + this.getViewId(), -1, -1);
    }


    
    public void setCameraDeclination(float declination)
    {
        mapComponent.setCameraDeclination("" + this.getViewId(), declination);
    }
    
    public void lookAtRegion(double centerLat, double centerLon, double deltaLat, double deltaLon)
    {
        setMapTransitionTime(0);
        mapComponent.lookAtRegion("" + this.getViewId(), centerLat, centerLon, deltaLat, deltaLon);
        updateZoomLevel();
    }
    
    //For current location blue circle
    public void enableRadius(boolean isEnable)
    {
        mapComponent.enableRadius("" + this.getViewId(), isEnable);
    }

    // For current location Coarse animation
    public void enableGPSCoarse(boolean isEnable)
    {
        mapComponent.enableGPSCoarse("" + this.getViewId(), isEnable);
    }
    
    public void setLowFPSAllowed(boolean isAllowed)
    {
        mapComponent.setLowFPSAllowed(isAllowed);
    }
    
    public boolean isGPSCoarseEnabled()
    {
        return mapComponent.isGPSCoarseEnabled();
    }

    private static final long ALERT_STUCK_INTERVAL = 2 * 60 * 1000;
    @Override
    public void checkStuckStatus()
    {
        long idleTime = ((GLMapRenderer)mapComponent.getRenderer()).getIdleTime();
        Log.d("DebugCrash", "idleTime=" + idleTime);
        if (idleTime > ALERT_STUCK_INTERVAL)
        {
            // ALERT and crash
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    showCrashNotification();
                    ((GLMapRenderer)mapComponent.getRenderer()).debugCrash();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
        }
    }
    
    private void showCrashNotification()
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String tickerText = "Crashed for stuck issue";
        Notification crashNotification = new Notification(R.drawable.app_notification, tickerText, System.currentTimeMillis());
        crashNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;

        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
        String msg = tickerText;
        crashNotification.setLatestEventInfo(context, "Stucked", msg, pendIntent);
        crashNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(CRASH_NOTIFICATION_ID, crashNotification);
    }
    
    public void startStuckMonitor()
    {
        boolean isStuckMonitorEnabled = AppConfigHelper.isStunkMonitorEnable();
        if (!isStuckMonitorEnabled)
        {
            // monitor not enabled, need not do that
            return;
        }
        StuckMonitorService.getInstance().start();
        StuckMonitorService.getInstance().addListener(this);
    }

    public void stopStuckMonitor()
    {
        boolean isStuckMonitorEnabled = AppConfigHelper.isStunkMonitorEnable();
        if (!isStuckMonitorEnabled)
        {
            // monitor not enabled, need not do that
            return;
        }
        StuckMonitorService.getInstance().stop();
        StuckMonitorService.getInstance().removeListener(this);
    }
    
    private class GetZoomLevelTask extends TimerTask
    {
        public void run()
        {
            MapContainer.this.mapComponent.getZoomLevel("" + MapContainer.this.getViewId());
        }
    }
    
    public void moveAnnotations(Hashtable<AbstractAnnotation, TnLocation> annotationLocationMapping, String viewName)
    {
        mapComponent.moveAnnotations(annotationLocationMapping, viewName);
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapCanvas.java
 *
 */
package com.telenav.map.opengl.java;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.json.tnme.JSONObject;

import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.map.EngineState;
import com.telenav.map.IMapEngine;
import com.telenav.map.IMapEngine.AnnotationSearchResult;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapRect;
import com.telenav.map.opengl.java.TnMapAnnotation.TnMapAnnotation2D;
import com.telenav.map.opengl.java.TnMapAnnotation.TnMapAnnotation3D;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.proxy.AbstractTileProvider;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapCanvas
{
    public static class TnMapLatch
    {
        public TnMapLatch()
        {
            m_updated = true;
        }

        public boolean update(){
            if(m_updated) 
            {
                m_outVal = m_inVal;
                m_updated = false; 
                return true;
            } 
            return false;
        }

        public void setVal(float val)
        {
            m_inVal = val; 
            m_updated = true;
        }
        public  float getVal() 
        { 
            return m_outVal; 
        }

        public float m_inVal;
        public float m_outVal;
        public boolean m_updated;
    }

    TnGL10 gl10;
    
    TnMapRect m_rcView;

    TnMapCameraManager       m_cameraManager;
    TnMapRenderManager       m_renderManager;
    TnMapBuildManager        m_buildManager;
    TnMapScene               m_curScene;
    TnMapRoute               m_route;
    TnMapVehicle             m_vehicle;
    TnMapScale               m_mapScale;
    TnMapSkyDome             m_skyDome;

    Vector m_Annotation2DLayers = new Vector();
    Vector m_Annotation3DLayers = new Vector();

    TnMapLatch m_point;
    TnMapLatch m_event;

    //! Start and end route icon graphics
    int m_StartGraphicId;
    int m_EndGraphicId;

    //! Start and end route icon annotations
    int m_StartAnnotationId;
    int m_EndAnnotationId;

    //! Collections for each type of setting
    boolean[] m_boolSettings = new boolean[IMapEngine.PARAMETER_BOOL_SIZE];
    int[] m_intSettings = new int[IMapEngine.PARAMETER_INT_SIZE];
    double[] m_doubleSettings = new double[IMapEngine.PARAMETER_DOUBLE_SIZE];

    /* These are not members of TnMapCanvas or TnMapEngine. Sharing the
     * id-to-annotation map amongst multiple engine instances insures that
     * an id created from one engine can't be mistaken for an id created
     * from the other engine. @TODO: Make this thread-safe. */

    //        typedef std::map<IMapEngine::AnnotationId, TnMapAnnotation2D::wptr> Annotation2DIdMap;
    //        typedef std::pair<IMapEngine::AnnotationId, TnMapAnnotation2D::wptr> Annotation2DIdMapEntry;

    //        typedef std::map<IMapEngine::AnnotationId, TnMapAnnotation3D::wptr> Annotation3DIdMap;
    //        typedef std::pair<IMapEngine::AnnotationId, TnMapAnnotation3D::wptr> Annotation3DIdMapEntry;

    static Hashtable annotation2DIdMap = new Hashtable(); //IMapEngine::AnnotationId, TnMapAnnotation2D::wptr
    static Hashtable annotation3DIdMap = new Hashtable();

    static int annotation_id_counter = 0;

    public TnMapCanvas(TnGL10 gl10,
            AbstractTileProvider tileProvider,
            TnMapTextureLoader loader,
            TnMapBuildManager builderManager,
            TnMapRect rc)
    {
        this.gl10 = gl10;
        this.m_rcView = rc;
        this.m_cameraManager = new TnMapCameraManager();
        this.m_renderManager = new TnMapRenderManager(gl10, loader);
        this.m_buildManager = builderManager;
        this.m_curScene = new TnMapScene(tileProvider);
        this.m_vehicle = new TnMapVehicle();
        this.m_mapScale = new TnMapScale(gl10);
        this.m_skyDome = new TnMapSkyDome(loader);
        this.m_StartGraphicId = addAnnotationGraphic(loader.syncLoad("StartFlag"));
        this.m_EndGraphicId = addAnnotationGraphic(loader.syncLoad("EndFlag"));
        this.m_StartAnnotationId = 0;
        this.m_EndAnnotationId = 0;

        this.m_cameraManager.setScreenSize(rc.dx, rc.dy);
        this.m_cameraManager.setPosition(start_origin);
        this.m_buildManager.getVehicleBuilder().buildVehicle(m_vehicle);
        this.m_cameraManager.update(0.0f, m_vehicle);
        this.m_vehicle.setPosition(new double[]{m_cameraManager.getPosition()[0],
                m_cameraManager.getPosition()[1],
                0});


        // Zero out parameters so there are no surprises when someone
        // forgets to provide an initial value.

        for (int i = 0; i<IMapEngine.PARAMETER_BOOL_SIZE; ++i)
        {
            m_boolSettings[i] = false;
        }

        for (int i = 0; i<IMapEngine.PARAMETER_INT_SIZE; ++i)
        {
            m_intSettings[i] = 0;
        }

        for (int i = 0; i<IMapEngine.PARAMETER_DOUBLE_SIZE; ++i)
        {
            m_doubleSettings[i] = 0.0;
        }


        // default settings
        m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_CAR] = true;
        m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_ADI] = true;
        m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_ROUTE] = true;
//        m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_ORIGIN_FLAG] = true;
//        m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_DESTINATION_FLAG] = true;
//        m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_TRAFFIC] = true;
        m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_MAP_SCALE] = true;
        m_boolSettings[IMapEngine.PARAMETER_BOOL_ENGLISH_UNITS] = true;
        m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_CAR_SCALE] = 1;
        m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_ADI_LATITUDE] = 10.0;
        m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_ADI_LONGITUDE] = 10.0;
        m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_VERTICAL_OFFSET] = 0.0;
        m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_LOD_BIAS] = 0.0;
        m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_CAMERA_DECLINATION] = TnMapConf.DEFAULT_CAMERA_DECLINATION_3D;
    }

    /*! This generates a unique ViewId. It should only be called once per
     *  creation of an annotation. */
    public static int createAnnotationId(TnMapAnnotation2D annotation)
    {
        int annotationId = ++annotation_id_counter;
        //            TnMapAnnotation2D::wptr ptr = TnMapAnnotation2D::wptr(annotation);

        annotation2DIdMap.put(new Integer(annotationId), annotation);

        return annotationId;
    }


    public static int createAnnotationId(TnMapAnnotation3D annotation) {

        int annotationId = ++annotation_id_counter;
        //            TnMapAnnotation3D::wptr ptr = TnMapAnnotation3D::wptr(annotation);

        annotation3DIdMap.put(new Integer(annotationId), annotation);

        return annotationId;
    }


    public static  boolean destroyAnnotationId(int annotationId)
    {
        boolean found = false;
        {
            // Search 2D annotations.
            if (annotation2DIdMap.containsKey(new Integer(annotationId)))
            {
                found = true;
                annotation2DIdMap.remove(new Integer(annotationId));
            }
        }

        {
            // Search 3D annotations. In theory if it was found in 2D, it
            // won't be here.
            if (annotation3DIdMap.containsKey(new Integer(annotationId)))
            {
                found = true;
                annotation3DIdMap.remove(new Integer(annotationId));
            }
        }

        return found;
    }

    /*! Returns a shared_ptr to an annotation, or a null shared pointer if the
     *  annotation no longer exists. */

    public static TnMapAnnotation2D idToAnnotation2D(int annotationId) {

        TnMapAnnotation2D other = (TnMapAnnotation2D)annotation2DIdMap.get(new Integer(annotationId));
        return other;
    }

    public static TnMapAnnotation3D idToAnnotation3D(int annotationId) {

        TnMapAnnotation3D other = (TnMapAnnotation3D)annotation3DIdMap.get(new Integer(annotationId));
        return other;
    }


    public static int annotation2DToId(TnMapAnnotation2D annotation) 
    {
        Enumeration e = annotation2DIdMap.keys();
        while(e.hasMoreElements())
        {
            Integer key = (Integer)e.nextElement();
            if(annotation2DIdMap.get(key) == annotation)
            {
                return key.intValue();
            }
        }

        return 0;
    }

    public static int annotation3DToId(TnMapAnnotation3D annotation) 
    {
        Enumeration e = annotation3DIdMap.keys();
        while(e.hasMoreElements())
        {
            Integer key = (Integer)e.nextElement();
            if(annotation3DIdMap.get(key) == annotation)
            {
                return key.intValue();
            }
        }

        return 0;
    }

    static int counter = 0;
    int getFreshGraphicId()
    {

        return ++counter;
    }



    public static boolean enableAnnotation(int annotationId)
    {
        boolean result = false;

        TnMapAnnotation2D annotation2D = idToAnnotation2D(annotationId);
        if (annotation2D != null)
        {
            result = result || annotation2D.action(TnMapAnnotation.TN_MAP_ANNOTATION_ACTION_ENABLE);
        }

        TnMapAnnotation3D annotation3D = idToAnnotation3D(annotationId);
        if (annotation3D != null)
        {
            result = result || annotation3D.action(TnMapAnnotation.TN_MAP_ANNOTATION_ACTION_ENABLE);
        }

        return result;
    }

    public static boolean disableAnnotation(int annotationId)
    {
        boolean result = false;

        TnMapAnnotation2D annotation2D = idToAnnotation2D(annotationId);
        if (annotation2D != null)
        {
            result = result || annotation2D.action(TnMapAnnotation.TN_MAP_ANNOTATION_ACTION_DISABLE);
        }

        TnMapAnnotation3D annotation3D = idToAnnotation3D(annotationId);
        if (annotation3D != null)
        {
            result = result || annotation3D.action(TnMapAnnotation.TN_MAP_ANNOTATION_ACTION_DISABLE);
        }

        return result;
    }


    //        namespace {

    //        #if 1
    // Telenav
    public static int start_x = 5406091;
    public static int start_y = 20537781;
    public static int start_z = 0;
    double[] start_origin = new double[]{start_x, start_y, start_z};
    //        #endif

    //        #if 0
    //        // Treasure Island
    //        double start_lat = 37.82444444444;
    //        double start_lon = -122.370277777777;
    //        tngm::Point3d start_origin = tnmm::latLonToGlobal(start_lat, start_lon, 0.0);
    //        #endif
    //
    //
    //        #if 0
    //        // Bay Bridge
    //        double start_lat = 37.798572;
    //        double start_lon = -122.377547;
    //        tngm::Point3d start_origin = tnmm::latLonToGlobal(start_lat, start_lon, 0.0);
    //        #endif
    //
    //
    //        #if 0
    //        // Bay Bridge North
    //        double start_lat = 37.817083;
    //        double start_lon = -122.355664;
    //        tngm::Point3d start_origin = tnmm::latLonToGlobal(start_lat, start_lon, 0.0);
    //        #endif
    //
    //
    //        }


    public void getCanvasState(EngineState state)
    {
        TnMapCamera camera = m_cameraManager.getCurrentCamera();

        //            state.cameraLatitude = TnMapMagic.globalToLat(camera.GetOrigin());
        //            state.cameraLongitude = TnMapMagic.globalToLon(camera.GetOrigin());
        //            state.cameraHeading = camera.GetHeading();
        //            state.cameraHeight = (float)TnMapMagic.globalToAlt(camera.GetOrigin());
        //            state.cameraFieldOfView = camera.GetFov();
        //            state.cameraDeclination = camera.GetDeclination();        
        state.carLatitude = TnMapMagic.globalToLat(m_vehicle.getPosition()[1]);
        state.carLongitude = TnMapMagic.globalToLon(m_vehicle.getPosition()[0]);
        state.carHeading = m_vehicle.getHeading();
        state.zoomLevel = camera.getZoom();
        state.interactionMode = m_cameraManager.getInteractionMode();
        state.renderMode = m_cameraManager.getRenderingMode();
        //            state.updated = true;
    }


    public boolean setBool(int param, boolean value)
    {
        if(param < IMapEngine.PARAMETER_BOOL_SIZE)
        {
            m_boolSettings[param] = value;
            return true;
        }

        // Invalid parameter
        return false;
    }

    public boolean setInt(int param, int value)
    {
        if(param < IMapEngine.PARAMETER_INT_SIZE)
        {
            m_intSettings[param] = value;
            return true;
        }

        // Invalid parameter
        return false;
    }

    public boolean setDouble(int param, double value)
    {
        if(param < IMapEngine.PARAMETER_DOUBLE_SIZE)
        {
            m_doubleSettings[param] = value;
            return true;
        }

        // Invalid parameter
        return false;
    }

    public boolean getBool(int param, boolean[] value)
    {
        if(param < IMapEngine.PARAMETER_BOOL_SIZE)
        {
            value[0] = m_boolSettings[param];
            return true;
        }

        // Invalid parameter
        return false;
    }

    public boolean getInt(int param, int[] value)
    {
        if(param < IMapEngine.PARAMETER_INT_SIZE)
        {
            value[0] = m_intSettings[param];
            return true;
        }

        // Invalid parameter
        return false;
    }

    public boolean getDouble(int param, double[] value)
    {
        if(param < IMapEngine.PARAMETER_DOUBLE_SIZE)
        {
            value[0] = m_doubleSettings[param];
            return true;
        }

        // Invalid parameter
        return false;
    }


    public void resize(TnMapRect rc)
    {
        m_cameraManager.setScreenSize(rc.dx, rc.dy);
        m_rcView = rc;

    }

    public void update(float dt)
    {
        m_mapScale.englishUnits(m_boolSettings[IMapEngine.PARAMETER_BOOL_ENGLISH_UNITS]);
        if (m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_MAP_SCALE])
        {
            m_mapScale.enable();
        } else {
            m_mapScale.disable();
        }
        TnMapCamera camera = m_cameraManager.getCurrentCamera();
        m_mapScale.build(-10, -10, 140, 6, camera);

        //Update Vehicle Position
        m_vehicle.update(dt);

        // Set camera declination according to our setting
        float declination3d = (float)m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_CAMERA_DECLINATION];
        m_cameraManager.setTargetDeclination(IMapEngine.RENDERING_MODE_3D, declination3d);
        m_cameraManager.setTargetDeclination(IMapEngine.RENDERING_MODE_3D_NORTH_UP, declination3d);
        m_cameraManager.setTargetDeclination(IMapEngine.RENDERING_MODE_3D_HEADING_UP, declination3d);

        // Set vertical offset according to our setting
        m_cameraManager.setVerticalOffset(m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_VERTICAL_OFFSET]);

        // Update camera
        m_cameraManager.update(dt, m_vehicle);

        double[] origin = m_cameraManager.getPosition();

        float bias = (float)m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_LOD_BIAS];
        m_cameraManager.setZoomBias(bias);
        int zoomLevel = m_cameraManager.getZoomLevel();

        // Update scene based on zoom level and bias from client
        m_curScene.update(origin, zoomLevel, camera, camera.GetScreenWidth(), camera.GetScreenHeight());

        // Update ADI state
        double[] position = new double[3];
        position[0] = m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_ADI_LONGITUDE];
        position[1] = m_doubleSettings[IMapEngine.PARAMETER_DOUBLE_ADI_LATITUDE];
        TnMapMagic.latLonToGlobal(position);
        m_vehicle.setADIEndpoint(position);
        m_vehicle.setADIState(m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_ADI]);

        // Update origin flag state
//        if(m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_ORIGIN_FLAG])
//        {
//            enableAnnotation(m_StartAnnotationId);
//        }
//        else
//        {
//            disableAnnotation(m_StartAnnotationId);
//        }
//
//        // Update destination flag state
//        if(m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_DESTINATION_FLAG])
//        {
//            enableAnnotation(m_EndAnnotationId);
//        }
//        else
//        {
//            disableAnnotation(m_EndAnnotationId);
//        }
    }


    public void render()
    {
        m_renderManager.render(this,m_cameraManager);
    }

    public void setTransitionTime(float seconds)
    {
        m_cameraManager.setTransitionTime(seconds);
    }

    public void setInteractionMode(int interactionMode)
    {
        m_cameraManager.setInteractionMode(interactionMode);
    }


    public int getInteractionMode()
    {
        return m_cameraManager.getInteractionMode();
    }


    public void setRenderingMode(int mode)
    {
        m_cameraManager.setRenderingMode(mode);
    }


    public int getRenderingMode()
    {
        return m_cameraManager.getRenderingMode();
    }


    public void setOrientation(int orientation)
    {
        m_cameraManager.setOrientation(orientation);
    }

    public int getOrientation()
    {
        return m_cameraManager.getOrientation();
    }

    public void lookAt(double latitude, double longitude, float heading)
    {
        double[] position = new double[]{longitude, latitude, 0};
        TnMapMagic.latLonToGlobal(position);

        m_cameraManager.setLookAtPoint(position,heading);
    }

    public TnMapScene getCurrentScene()
    {
        return m_curScene;
    }

    public Vector getAnnotation2DLayers()
    {
        return m_Annotation2DLayers;
    }

    public Vector getAnnotation3DLayers()
    {
        return m_Annotation3DLayers;
    }

    public TnMapRect getScreenRect()
    {
        return m_rcView;
    }

    public TnMapVehicle getVehicle()
    {
        boolean[]  show = new boolean[1];
        getBool(IMapEngine.PARAMETER_BOOL_SHOW_CAR, show);

        return show[0] ? m_vehicle : null;
    }


    public TnMapRoute getRoute()
    {
        boolean[]  show = new boolean[1];
        getBool(IMapEngine.PARAMETER_BOOL_SHOW_ROUTE, show);

        return show[0] ? m_route : null;
    }


    public TnMapScale getMapScale()
    {
        boolean[]  show = new boolean[1];
        getBool(IMapEngine.PARAMETER_BOOL_SHOW_MAP_SCALE, show);

        return show[0] ? m_mapScale : null;
    }


    public TnMapSkyDome getSkyDome()
    {
        return m_skyDome;
    }
    
    public AnnotationSearchResult[] getNearestAnnotations(int x, int y, boolean onlyEnabledAnnotations)
    {
        TnMapCamera camera = m_cameraManager.getCurrentCamera();

        Vector v = new Vector();
        
        for(int i = 0; i < m_Annotation2DLayers.size(); i++)
        {
            Vector objectSet = (Vector)m_Annotation2DLayers.elementAt(i);
            for (int j=0; j<objectSet.size(); j++)
            {
                TnMapAnnotation2D a = (TnMapAnnotation2D)objectSet.elementAt(j);
                if(!onlyEnabledAnnotations || a.isEnabled())
                {
                    int annotationId = annotation2DToId(a);
                    float distance = a.hit(x, y, camera);
                    AnnotationSearchResult r = new AnnotationSearchResult(distance, 0, 0,0,0,0, i, annotationId, null);
                    
                    int t;
                    for (t=0; t<v.size(); t++)
                    {
                        AnnotationSearchResult temp = (AnnotationSearchResult)v.elementAt(t);
                        if (temp.distance > distance)
                        {
                            break;
                        }
                    }
                    v.insertElementAt(r, t);
                }
            }
        }

        //Detect Touched 3D Annotations
        for(int i = 0; i < m_Annotation3DLayers.size(); i++)
        {
            Vector objectSet = (Vector)m_Annotation3DLayers.elementAt(i);
            for (int j=0; j<objectSet.size(); j++)
            {
                TnMapAnnotation3D a = (TnMapAnnotation3D)objectSet.elementAt(j);
                if(!onlyEnabledAnnotations || a.isEnabled())
                {
                    int annotationId = annotation3DToId(a);
                    float distance = a.hit(x, y, camera);
                    
                    AnnotationSearchResult r = new AnnotationSearchResult(distance, 0, 0,0,0,0,  i, annotationId, null);
                    
                    int t;
                    for (t=0; t<v.size(); t++)
                    {
                        AnnotationSearchResult temp = (AnnotationSearchResult)v.elementAt(t);
                        if (temp.distance > distance)
                        {
                            break;
                        }
                    }
                    v.insertElementAt(r, t);
                }
            }
        }

        AnnotationSearchResult[] results = new AnnotationSearchResult[v.size()];
        for (int i=0; i<v.size(); i++)
            results[i] = (AnnotationSearchResult)v.elementAt(i);
        
        return results;
    }

    //MultiMap: SortedMap<key, vector>
    public Hashtable getNearest(int x, int y, boolean onlyEnabled)
    {
        // Since all we can really pick from are traffic incidents, this works.
        // Eventually we'll have to have finer grained control.
//        if(onlyEnabled && (m_boolSettings[IMapEngine.PARAMETER_BOOL_SHOW_TRAFFIC] == false))
//        {
//            // return empty map
//            return new Hashtable();
//        }

        // For now, just go right to the scene
        return m_curScene.getNearest(m_cameraManager.getCurrentCamera(), x, y, onlyEnabled);
    }

    public void handleTouchEvent(int event, 
            int touchX, int touchY, 
            float pinch)
    {
        //Have camera interact with touch
        m_cameraManager.handleTouchEvent(event, touchX, touchY, pinch);
    }

    private TnMapRoute convertRoute(Route route)
    {
        double[] startPoint = new double[3], endPoint = new double[3];
        
        Vector edges = new Vector();
        for (int i=0; i<route.segmentsSize(); i++)
        {
            Segment seg = route.segmentAt(i);
            for (int j=0; j<seg.edgesSize(); j++)
            {
                RouteEdge e = seg.getEdge(j);
                Vector v = new Vector();
                edges.addElement(v);
                for (int k=0; k<e.numPoints(); k++)
                {
                    if (i == 0 && j == 0 && k == 0)
                    {
                        startPoint = new double[]{e.lonAt(0) / 100000.0, e.latAt(0) / 100000.0, 0.0};
                        TnMapMagic.latLonToGlobal(startPoint);
                    }
                    else if (i == route.segmentsSize()-1 && j == seg.edgesSize()-1 && k == e.numPoints()-1)
                    {
                        endPoint = new double[]{e.lonAt(k) / 100000.0, e.latAt(k) / 100000.0, 0.0};
                        TnMapMagic.latLonToGlobal(endPoint);
                    }

                    double[] p = new double[]{e.lonAt(k) / 100000.0, e.latAt(k) / 100000.0, 0.0};
                    TnMapMagic.latLonToGlobal(p);
                    v.addElement(p);
                }                   
            }
        }
        
        TnMapRoute mapRoute = new TnMapRoute(gl10, edges, new Vector(), startPoint, endPoint);
        return mapRoute;
    }
    
    public void setRoute(RouteWrapper route)
    {
        TnMapRoute mapRoute = convertRoute(route.getCurrentRoute());
        
        // Retain arrow enabled/disabled state from previous route
        if(m_route != null)
        {
            int arrows = (int)Pe.tnmin(mapRoute.arrowsCount(), m_route.arrowsCount());
            for(int i=0; i<arrows; i++)
            {
                if(m_route.getArrow(i).isEnabled())
                {
                    mapRoute.getArrow(i).enable();
                }
            }
        }

        // Clear any existing route
        clearRoute();

        // Assign the new route
        m_route = mapRoute;

        // Add start and end annotations
        m_StartAnnotationId = addSpriteAnnotation(
                IMapEngine.ANNOTATION_LAYER_ROUTE_WAYPOINT, m_StartGraphicId,
                m_route.getStartPoint(),
                53, 42,
                26, 0);
        m_EndAnnotationId = addSpriteAnnotation(
                IMapEngine.ANNOTATION_LAYER_ROUTE_WAYPOINT, m_EndGraphicId,
                m_route.getFinishPoint(),
                53, 42,
                26, 0);
    }


    public void clearRoute()
    {
        // Clear current route
        m_route = null;

        // Remove any old annotations
        removeAnnotation(m_StartAnnotationId);
        removeAnnotation(m_EndAnnotationId);
    }


    public int addSpriteAnnotation(int layer,
            int graphicId,
            double[] pos,
            int pixelWidth, int pixelHeight,
            int pivotPointX, int pivotPointY)
    {
        if(layer != IMapEngine.ANNOTATION_LAYER_NO_LAYER)
        {
            //Build Annotation
            TnMapAnnotation3D annotation
            = m_buildManager.getAnnotationBuilder().build3DAnnotation(graphicId,
                    pos,
                    pixelWidth, pixelHeight,
                    pivotPointX, pivotPointY);
            int annotationId = createAnnotationId(annotation);
            if(layer < m_Annotation3DLayers.size())
            {
                Vector objectSet = (Vector)m_Annotation3DLayers.elementAt(layer);
                objectSet.addElement(annotation);
            }
            else
            {
                Vector objectSet = new Vector();
                objectSet.addElement(annotation);
                m_Annotation3DLayers.addElement(objectSet);
            }
            return annotationId;
        }

        return 0;
    }


    public int addFixedAnnotation(int layer, 
                                    int graphicId,
                                    double[] pos, 
                                    int pixelWidth, 
                                    int pixelHeight,
                                    int pivotPointX, 
                                    int pivotPointY,
                                    float[] faceVector, 
                                    float[] upVector)
    {
        if(layer != IMapEngine.ANNOTATION_LAYER_NO_LAYER && graphicId > 0)
        {
            //Build Annotation
            TnMapAnnotation3D annotation = m_buildManager.getAnnotationBuilder().build3DAnnotation(graphicId,
                                                                        pos,
                                                                        pixelWidth, pixelHeight,
                                                                        pivotPointX, pivotPointY,
                                                                        faceVector, upVector,
                                                                        layer);
            int annotationId = createAnnotationId(annotation);
            if(layer < m_Annotation3DLayers.size())
            {
                Vector objectSet = (Vector)m_Annotation3DLayers.elementAt(layer);
                objectSet.addElement(annotation);
            }
            else
            {
                Vector objectSet = new Vector();
                objectSet.addElement(annotation);
                m_Annotation3DLayers.addElement(objectSet);
            }
            return annotationId;
        }
        
        return 0;
    }
    
    public int addBillboardAnnotation(int layer,
            int graphicId,
            double[] pos,
            int pixelWidth, int pixelHeight,
            int pivotPointX, int pivotPointY)
    {
        // @TODO: Implement AddBillboardAnnotation
        return 0;
    }



    public int add2DAnnotation(int layer,
            int x, int y,
            int graphicId,
            int pixelWidth, int pixelHeight)
    {
        if(layer != IMapEngine.ANNOTATION_LAYER_NO_LAYER)
        {
            //Build Annotation

            TnMapAnnotation2D annotation = m_buildManager.getAnnotationBuilder().build2DAnnotation(graphicId, x, y, pixelWidth, pixelHeight);
            int annotationId = createAnnotationId(annotation);

            if(layer < m_Annotation2DLayers.size())
            {
                Vector objectSet = (Vector)m_Annotation2DLayers.elementAt(layer);
                objectSet.addElement(annotation);
            }
            else
            {
                Vector objectSet = new Vector();
                objectSet.addElement(annotation);
                m_Annotation2DLayers.addElement(objectSet);
            }
            return annotationId;
        }

        return 0;
    }


    public boolean removeAnnotation(int annotationId)
    {
        boolean found = false;

        // In theory, only one of these happens.

        TnMapAnnotation2D ptr_2d = idToAnnotation2D(annotationId);

        if (ptr_2d != null)
        {
            found = true;
            destroyAnnotationId(annotationId);

            for (int i = 0; i < m_Annotation2DLayers.size(); i++) {

                Vector objectSet = (Vector)m_Annotation2DLayers.elementAt(i);
                objectSet.removeElement(ptr_2d);
            }
        }

        TnMapAnnotation3D ptr_3d = idToAnnotation3D(annotationId);
        if (ptr_3d != null)
        {
            found = true;
            destroyAnnotationId(annotationId);

            for (int i = 0; i < m_Annotation3DLayers.size(); i++) {

                Vector objectSet = (Vector)m_Annotation3DLayers.elementAt(i);
                objectSet.removeElement(ptr_2d);
            }
        }

        return found;
    }


    public void clearAnnotationLayer(int layer /* layer */)
    {

    }


    public int addAnnotationGraphic(TnMapTexture texture)
    {
        int graphicId = getFreshGraphicId();
        m_buildManager.getAnnotationBuilder().addGraphic(graphicId,texture);
        return graphicId;
    }

    public int addAnnotationGraphic(byte[] bytes, int width, int height)
    {
        int graphicId = getFreshGraphicId();
        m_buildManager.getAnnotationBuilder().buildGraphic(graphicId,bytes,width,height);
        return graphicId;
    }

    public boolean removeAnnotationGraphic(int graphicId)
    {
        return m_buildManager.getAnnotationBuilder().removeGraphic(graphicId);
    }


    public void setVehiclePosition(double latitude, double longitude, double height , float heading, int meterAccuracy)
    {
        double[] position = new double[]{longitude, latitude, height}; 
        TnMapMagic.latLonToGlobal(position);

        m_vehicle.setPosition(position);
        m_vehicle.setHeading(heading);
        m_vehicle.setAccuracyRadius(meterAccuracy);

        // Notify camera manager that vehicle position changed
        m_cameraManager.setVehiclePosition(position);
    }

    public void setVehicleType(int event)
    {
        m_vehicle.setVehicleType(event);
    }

    public void setConfigurationData(JSONObject configuration)
    {
        // Map array index to JSON property name
        String[] configColorNames = new String[TnMapConf.FEATURE_TYPE_COUNT];
        configColorNames[TnMapConf.FEATURE_TYPE_GEOPOLITICAL] = "color.geopolitical";
        configColorNames[TnMapConf.FEATURE_TYPE_PARK] = "color.park";
        configColorNames[TnMapConf.FEATURE_TYPE_CAMPUS] = "color.campus";
        configColorNames[TnMapConf.FEATURE_TYPE_WATER] = "color.water";
        configColorNames[TnMapConf.FEATURE_TYPE_STATEBORDER] = "color.stateborder";
        configColorNames[TnMapConf.FEATURE_TYPE_NATIONBORDER] = "color.nationborder";
        configColorNames[TnMapConf.FEATURE_TYPE_WATER_LINE] = "color.waterline";
        configColorNames[TnMapConf.FEATURE_TYPE_FERRYBOAT] = "color.ferryboat";
        configColorNames[TnMapConf.FEATURE_TYPE_RAIL] = "color.border_rail";
        configColorNames[TnMapConf.FEATURE_TYPE_CANAL] = "color.canal";
        configColorNames[TnMapConf.FEATURE_TYPE_SMALLSTREET_BORDER] = "color.border_smallstreet";
        configColorNames[TnMapConf.FEATURE_TYPE_STREET_BORDER] = "color.border_street";
        configColorNames[TnMapConf.FEATURE_TYPE_INTERSECTION_BORDER] = "color.border_intersection";
        configColorNames[TnMapConf.FEATURE_TYPE_RAMP_BORDER] = "color.border_ramp";
        configColorNames[TnMapConf.FEATURE_TYPE_HIGHWAYRAMP_BORDER] = "color.border_highwayramp";
        configColorNames[TnMapConf.FEATURE_TYPE_ARTERIAL_BORDER] = "color.border_arterial";
        configColorNames[TnMapConf.FEATURE_TYPE_HIGHWAY_BORDER] = "color.border_highway";
        configColorNames[TnMapConf.FEATURE_TYPE_SMALLSTREET] = "color.fill_smallstreet";
        configColorNames[TnMapConf.FEATURE_TYPE_STREET] = "color.fill_street";
        configColorNames[TnMapConf.FEATURE_TYPE_INTERSECTION] = "color.fill_intersection";
        configColorNames[TnMapConf.FEATURE_TYPE_RAMP] = "color.fill_ramp";
        configColorNames[TnMapConf.FEATURE_TYPE_HIGHWAYRAMP] = "color.fill_highwayramp";
        configColorNames[TnMapConf.FEATURE_TYPE_ARTERIAL] = "color.fill_arterial";
        configColorNames[TnMapConf.FEATURE_TYPE_HIGHWAY] = "color.fill_highway";
        configColorNames[TnMapConf.FEATURE_TYPE_DATE_LINE] = "color.dateline";
        configColorNames[TnMapConf.FEATURE_TYPE_ROUTE] = "color.route";
        configColorNames[TnMapConf.FEATURE_TYPE_ARROW] = "color.arrow";

        // Fill our arrays based on values in property tree
        for(int i=0; i<TnMapConf.FEATURE_TYPE_COUNT; i++)
        {
            TnMapConf.setFeatureColor(i, new TnMapColor(getJsonValue(configuration, configColorNames[i])));
        }

        TnMapConf.setBackgroundColor(new TnMapColor(getJsonValue(configuration, "color.background")));

        TnMapConf.setRoadTextColor(new TnMapColor(getJsonValue(configuration, "color.road_text")));

        TnMapConf.setTrafficSpeedColor(TnMapConf.TRAFFIC_COLOR_SLOW,new TnMapColor(getJsonValue(configuration, "color.traffic_slow")));
        TnMapConf.setTrafficSpeedColor(TnMapConf.TRAFFIC_COLOR_MEDIUM,new TnMapColor(getJsonValue(configuration, "color.traffic_medium")));
        TnMapConf.setTrafficSpeedColor(TnMapConf.TRAFFIC_COLOR_FAST,new TnMapColor(getJsonValue(configuration, "color.traffic_fast")));


        float value = Float.parseFloat(getJsonValue(configuration, "roads.traffic_width_ratio"));
        TnMapConf.setFloat(TnMapConf.TRAFFIC_WIDTH_RATIO, value);
    }

    public static String getJsonValue(JSONObject configuration, String token)
    {
        int dotIndex = -1;
        JSONObject tmpJsonObject = configuration;
        try
        {
            while ((dotIndex = token.indexOf(".")) != -1)
            {

                tmpJsonObject = (JSONObject) configuration.getJSONObject(token.substring(0, dotIndex));
                token = token.substring(dotIndex + 1);

            }
            return tmpJsonObject.getString(token);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean reposition2DAnnotation(int annotationId,
            int x, 
            int y, 
            int pixelWidth, 
            int pixelHeight)
    {
        TnMapAnnotation2D annotation = idToAnnotation2D(annotationId);
        if (annotation != null)
        {
            annotation.reposition(x, y, pixelWidth, pixelHeight);
            return true;
        }
        return false;
    }


    public void beginSmoothZoomIn()
    {
        m_cameraManager.beginSmoothZoomIn();
    }

    public void beginSmoothZoomOut()
    {
        m_cameraManager.beginSmoothZoomOut();
    }


    public void endSmoothZoom()
    {
        m_cameraManager.endSmoothZoom();
    }


    public void setZoom(float zoom)
    {
        m_cameraManager.setZoom(zoom);
    }


    public void setZoom(float zoom, int x, int y)
    {
        m_cameraManager.setZoom(zoom, x, y);
    }


    public float getZoom()
    {
        return m_cameraManager.getZoom();
    }

    public boolean showRegion(double northLat, 
            double westLon, 
            double southLat, 
            double eastLon)
    {
        double[] p1 = new double[]{westLon, southLat, 0};
        double[] p2 = new double[]{eastLon, northLat, 0};
        TnMapMagic.latLonToGlobal(p1);
        TnMapMagic.latLonToGlobal(p2);
        m_cameraManager.setLookAtDiagonal(p1,
                p2);
        return true;
    }


    public boolean enableTurnArrow(int segment)
    {
        if (m_route != null) return m_route.enableTurnArrow(segment);
        return false;
    }


    public boolean enableAllTurnArrows()
    {
        if (m_route != null) return m_route.enableAllTurnArrows();
        return false;
    }


    public boolean disableTurnArrow(int segment)
    {
        if (m_route != null) return m_route.disableTurnArrow(segment);
        return false;
    }


    public boolean disableAllTurnArrows()
    {
        if (m_route != null) return m_route.disableAllTurnArrows();
        return false;
    }

    public void reload(TnMapTextureLoader loader)
    {
        m_skyDome.reload(loader);
    }

    public void clear()
    {
        // Tell the scene there's nothing in its view
        m_curScene.clear();
    }
}

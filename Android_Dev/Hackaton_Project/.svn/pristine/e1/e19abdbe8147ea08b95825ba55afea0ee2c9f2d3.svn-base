/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMapEngine.java
 *
 */
package com.telenav.map;

import com.telenav.datatypes.map.MapTile;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL;


/**
 *@author jyxu(jyxu@telenav.cn)
 *@date 2010-11-9
 */
public interface IMapEngine
{


    /************************************Constants Define*******************************************************/
//    public static int MAP_MIN_ZOOM_LEVEL= MapConfig.MAP_MIN_ZOOM_LEVEL;
//    public static int MAP_MAX_ZOOM_LEVEL = MapConfig.MAP_MAX_ZOOM_LEVEL;

    // Rendering API
    public static final int RENDERING_API_OGLES10 = 0;
    public static final int RENDERING_API_OGLES11 = 2;
    public static final int RENDERING_API_OGLES20 = 1;

    // Interaction Mode - Control how mouse movement interacts with the system                 
    public static final int INTERACTION_MODE_INVALID = -1;          //!< Indicates an error
    public static final int INTERACTION_MODE_NONE = 0;              //!< No interaction
    public static final int INTERACTION_MODE_PAN_AND_ZOOM = 1;        //!< The map pans to track the touched location
    public static final int INTERACTION_MODE_FOLLOW_VEHICLE = 2;     //!< The camera follows the vehicle.
    public static final int INTERACTION_MODE_ROTATE_AROUND_POINT = 3; //!< Rotate around the point in the center of the screen.
    public static final int INTERACTION_MODE_FREE_LOOK = 4;          //!< Place eye at location and look around.

    // Rendering _MODE - Indicate either 2D rendering (Top-Down) or 3D
    public static final int RENDERING_MODE_INVALID = -1;
    public static final int RENDERING_MODE_3D = 0;
    public static final int RENDERING_MODE_3D_NORTH_UP = 1;
    public static final int RENDERING_MODE_3D_HEADING_UP = 2;
    public static final int RENDERING_MODE_2D = 3;
    public static final int RENDERING_MODE_2D_NORTH_UP = 4;
    public static final int RENDERING_MODE_2D_HEADING_UP = 5;

    // Orientation that OpenGL should render to. "PortraitBottom"
    // means "portrait mode with home button on the
    // bottom". "LandscapeLeft" means "landscape mode with home
    // button on the left." Etc.
    public static final int ORIENTATION_INVALID = -1;
    public static final int ORIENTATION_PORTRAIT_BOTTOM = 0;
    public static final int ORIENTATION_PORTRAIT_TOP = 1;
    public static final int ORIENTATION_LANDSCAPE_LEFT = 2;
    public static final int ORIENTATION_LANDSCAPE_RIGHT = 3;

    // Touch Event
    public static final int TOUCH_EVENT_INVALID = -1;
    public static final int TOUCH_EVENT_BEGIN = 0;
    public static final int TOUCH_EVENT_MOVE = 1;
    public static final int TOUCH_EVENT_END = 2;
    public static final int TOUCH_EVENT_POINTER_DOWN = 3;

    // Annotation Layer - Predefined annotation layers plus a user
    // defined space for custom annotations.
    public static final int ANNOTATION_LAYER_NO_LAYER = -1;
    public static final int ANNOTATION_LAYER_POI_LAYER = 0;          //!< POI Icons
    public static final int ANNOTATION_LAYER_TRAFFIC_INCIDENT = 1;   //!< Traffic Icons
    public static final int ANNOTATION_LAYER_TRAFFIC_CAMERA = 2;     //!< Camera Icons
    public static final int ANNOTATION_LAYER_TRAFFIC_SPEED_TRAP = 3;  //!< Speed Trap Icons
    public static final int ANNOTATION_LAYER_ROUTE_WAYPOINT = 4;     //!< Begin/End/Waypoints of route
    public static final int ANNOTATION_LAYER_VEHICLE = 5;			//!< Vehicle Icons
    public static final int ANNOTATION_LAYER_USER_DEFINED = 32;      //!< First custom annotation layer
    public static final int ANNOTATION_LAYER_CONTROLLER = 80;      //!< First custom annotation layer
    public static final int ANNOTATION_LAYER_TRAFFIC_INCIDENT_CUSTOM = 90;      //!< First custom annotation layer
    public static final int ANNOTATION_LAYER_MAX_USER_DEFINED = 127;  //!< Last custom annotation layer

    // Events that the engine will care about
    public static final int EVENT_NEW_ROUTE = 0;
    public static final int EVENT_NEW_TRAFFIC = 1;
    public static final int EVENT_NEW_WEATHER = 2;
    public static final int EVENT_FLUSH_ENGINE_DATA = 3;
    public static final int EVENT_CLEAR_CACHES = 4;
    public static final int EVENT_SHOW_CAR_MODEL = 5;
    public static final int EVENT_SHOW_PEDESTRIAN_MODEL = 6;
    public static final int EVENT_SHOW_DOT_MODEL = 7;
    public static final int EVENT_NEW_TILE_ANNOTATIONS = 8;
    public static final int EVENT_CLEAR_BREADCRUMBS = 9;

    // General engine parameters. This will be extended as needed
    public static final int PARAMETER_BOOL_SHOW_CAR = 0;
    public static final int PARAMETER_BOOL_SHOW_ADI = 1;
    public static final int PARAMETER_BOOL_SHOW_ROUTE = 2;
    public static final int PARAMETER_BOOL_SHOW_MAP_SCALE = 3;
    public static final int PARAMETER_BOOL_ENGLISH_UNITS = 4;
    public static final int PARAMETER_BOOL_SHOW_REGIONS = 5;
    public static final int PARAMETER_BOOL_SHOW_ROADS = 6;
    public static final int PARAMETER_BOOL_SHOW_ANNOTATIONS_2D = 7;
    public static final int PARAMETER_BOOL_SHOW_ANNOTATIONS_3D = 8;
    public static final int PARAMETER_BOOL_SHOW_TEXT_2D = 9;
    public static final int PARAMETER_BOOL_SHOW_AERIAL = 10;
    public static final int PARAMETER_BOOL_SHOW_DEBUG_GRID = 11;
    public static final int PARAMETER_BOOL_SHOW_TRAFFIC_HIGHLIGHT = 12;
    public static final int PARAMETER_BOOL_SHOW_TRAFFIC_INCIDENTS = 13;
    public static final int PARAMETER_BOOL_SHOW_BREADCRUMBS = 14;
    public static final int PARAMETER_BOOL_SHOW_RASTERROAD = 15;
    public static final int PARAMETER_BOOL_SHOW_TEXT3D = 16;
    public static final int PARAMETER_BOOL_SHOW_LANDMARKS = 17;
    public static final int PARAMETER_BOOL_RECORD_BREADCRUMBS = 18;
    public static final int PARAMETER_BOOL_SIZE = 19;
    
    public static final int PARAMETER_INT_LEFT_HANDED_SPANNERS = 0;
    public static final int PARAMETER_INT_SIZE  = 1;

    public static final int PARAMETER_DOUBLE_CAR_SCALE = 0;
    public static final int PARAMETER_DOUBLE_ADI_LATITUDE = 1;
    public static final int PARAMETER_DOUBLE_ADI_LONGITUDE = 2;
    public static final int PARAMETER_DOUBLE_VERTICAL_OFFSET = 3;
    public static final int PARAMETER_DOUBLE_LOD_BIAS = 4;
    public static final int PARAMETER_DOUBLE_CAMERA_DECLINATION = 5;
    public static final int PARAMETER_DOUBLE_HORIZONTAL_OFFSET = 6;
    public static final int PARAMETER_DOUBLE_SIZE = 7;

    public static final int PARAMETER_STRING_NODRAW = 0;
    public static final int PARAMETER_STRING_SIZE = 1;

    public static final int ON_ROAD_STATE_INVALID_THRESHOLD = -1;
    public static final int ON_ROAD_STATE_NO = 0;
    public static final int ON_ROAD_STATE_YES = 1;
    public static final int ON_ROAD_STATE_DONT_KNOW = 2;
    
    public final static int eTerrainGridResolution_32  = 0;
    public final static int eTerrainGridResolution_64  = 1;
    public final static int eTerrainGridResolution_128 = 2;
    public final static int eTerrainGridResolution_256 = 3;
    
    /*
    eRasterEnable_Aerial,
    eRasterEnable_AerialWithLabels,
    eRasterEnable_Road,
    eRasterEnable_TerrainAerial,
    eRasterEnable_TerrainAerialWithLabels,
    eRasterEnable_TerrainRoad,
    eRasterEnable_HeightmapAsRaster,
    eRasterEnable_TelenavRasterRoad,
    eRasterEnable_TerrainTelenavRasterRoad,
    eRasterEnable_NONE
   */
   public static final int eRasterEnable_Aerial = 0;
   public static final int eRasterEnable_AerialWithLabels = 1;
   public static final int eRasterEnable_Road = 2;
   public static final int eRasterEnable_TerrainAerial = 3;
   public static final int eRasterEnable_TerrainAerialWithLabels = 4;
   public static final int eRasterEnable_TerrainRoad = 5; 
   public static final int eRasterEnable_HeightmapAsRaster = 6;
   public static final int eRasterEnable_TelenavRasterRoad = 7;
   public static final int eRasterEnable_TerrainTelenavRasterRoad = 8;
   public static final int eRasterEnable_NONE = 9;
   
   public final static int ANNOTATION_FIXED     = 0;
   public final static int ANNOTATION_BILLBOARD = 1;
   public final static int ANNOTATION_SPRITE    = 2;
   public final static int ANNOTATION_SPLATTED  = 3;
   public final static int ANNOTATION_SCREEN    = 4;
    
    /************************************End Constants Define*******************************************************/
    public long getViewId();
    
    public void setViewId(long viewId);
    
    public void init(int openGlVersion);
    
    public void initCache(long cacheSize, String cachePath); 
    
    public void startPreloader(); 
    
    public void stopPreloader(); 
    
    public void destroyMapEngine();

    public void setGl(TnGL gl);

    // Get a ConfId representing the engine's default configuration
    public long getDefaultConfig();
    
    // Add a resource (currently can be a JSON file or URL) to a configuration object
    public boolean addConfig(long confId, String source);
    
    // Add raw configuration data to a configuration object (currently must be JSON)
    public boolean addConfig(long confId, byte[] bytes);
    
    /************************************Parameter Setting**********************************************************/

    public boolean getBool(long viewId, int param);
    public boolean setBool(long viewId, int param, boolean value);
    
    public double getDouble(long viewId, int param);
    public boolean setDouble(long viewId, int param, double value);
    
    public boolean setStringList(long viewId, int param, String[] strList);

    // Set and get binary data
    public boolean setBinary(long viewId, int param, byte[] value);
    public boolean getBinary(long viewId, int param, byte[][] value);
    
    public void setRoute(RouteWrapper routeWrapper);
    
    public void setRouteColor(int routeId, int color);

    public void notify(int event);

    // All camera control calls trigger smooth camera transitions to
    // the specified settings and mode changes. Transition time is
    // specified via setTransitionTime

    public boolean setTransitionTime(long viewId, float seconds);

    public boolean  setInteractionMode(long viewId, int mode);
    public int  getInteractionMode(long viewId);
    
    /**
     * Any time InteractionMode changed, it will get noticed.
     * 
     * Don't forget setInteractionModeChangeListener(null) at proper time.
     * 
     * @param listener
     */
    public void setInteractionModeChangeListener(InteractionModeChangeListener listener);

    public boolean setRenderingMode(long viewId, int mode);
    public int getRenderingMode(long viewId);

    public void setOrientation(long viewId, int orientation);
    public int getOrientation(long viewId);

    /************************************Interaction**********************************************************/

    // Moves the camera to the specified location */
    public boolean lookAt(long viewId, double latitude, double longitude, float heading);


    // Pan and zoom (but not rotate) view to encompass given region */
    public boolean showRegion(long viewId, 
            double northLat, 
            double westLon, 
            double southLat, 
            double eastLon);

    // Pan and zoom (but not rotate) view to encompass given region within a given viewport rect */
    public boolean showRegion(long viewId, double northLat, double westLon, double southLat, double eastLon, int x, int y, int width, int height);
    
    public boolean handleTouchEvent(long viewId, TouchEvent event);

    /************************************End Interaction**********************************************************/

    /************************************Zoom in/Zoom out**********************************************************/

    public boolean setZoomLevel(long viewId, float zoomLevel);
    public boolean setZoomLevel(long viewId, float zoomLevel, int x, int y, double[] latitude, double[] longitude);

    public float getZoomLevel(long viewId);

    /*************************************************Navigation******************************************************/

    public  void setVehiclePosition(double degreesLatitude,
            double degreesLongitude, 
            float heading,
            int meterAccuracy);

    public  MapTile[] getSurroudingMapTiles(double degreesLatitude, 
            double degreesLongitude, int radius);

    /************************************************End Navigation******************************************************/

    /************************************************Opengl View Management*********************************************/

    public void updateAndRenderAndBuild(float deltaSeconds);
    public void updateAndRender(float deltaSeconds);
    public void build(float deltaSeconds);
    public void update(float deltaSeconds);
    public void render(float deltaSeconds);

    // Create a view at the specified canvas location that shares an
    // annotation set with a previously-created view

    public long createView(
            long confId,
            int x, 
            int y, 
            int width,
            int height, 
            float dpi,
            float view_scale_factor);
    
    public boolean deleteView(long viewId);

    public boolean resizeView(long viewId, 
            int x, int y, 
            int width, int height);

    //opengl 1.6 api
    public boolean resizeView(long viewID, int x, int y, int width, int height, float dpi, float view_scale_factor);
    
    // Get engine/view state
    public EngineState getViewState(long viewId);
    /************************************************End Opengl View Management******************************************/


    /*************************************************Annotation Management**********************************************/
    public long addAnnotationGraphic(String source);

    public long addAnnotationGraphics(AbstractTnImage img);

    public long addAnnotationGraphic(byte[] bytes, 
            int width, 
            int height);

    public boolean removeAnnotationGraphic(long id);


    // A sprite annotation always faces the camera. The Pivot Point is
    // point within the annotation's coordinate system that is to be
    // considered the 'origin' of the annotation for placement and
    // pivotiong. Its units are pixels, with the origin in lower
    // left */

    public long addSpriteAnnotation(long viewId, 
            int layer, 
            long graphicId,
            double degreesLatitude, 
            double degreesLongitude, 
            double height, 
            int pixelWidth, int pixelHeight,
            int pivotPointX, int pivotPointY);


    public long addScreenAnnotation(long viewId, 
            int layer, 
            long graphicID, 
            double degreesLatitude, 
            double degreesLongitude, 
            double metersHeight, 
            String style, 
            String text);
    
    // A billboard annotation spins on its vertical axis to face
    // camera, but does not tilt up toward it. That is, its vertical
    // axis is always perpendicular to the ground. The Pivot Point is
    // point within the annotation's coordinate system that is to be
    // considered the 'origin' of the annotation for placement and
    // pivotiong. Its units are pixels, with the origin in lower
    // left

    public long addBillboardAnnotation(long viewId, 
            int layer, 
            long graphicId,
            double degreesLatitude, 
            double degreesLongitude, 
            double height, 
            int pixelWidth, int pixelHeight, 
            int pivotPointX, int pivotPointY);


    // A fixed annotation does not reorient to face the camera

    public long addFixedAnnotation(long viewId, int layer, long graphicId, double degreesLatitude, double degreesLongitude, double height, int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY, float[] faceVector, float[] upVector);


    // A 2D annotation is placed in the view's screen coordinate
    // system

    public long add2DAnnotation(long viewId, 
            int layer, 
            long graphicId, 
            int x, 
            int y, 
            int pixelWidth, 
            int pixelHeight);

    public boolean removeAnnotation(long viewId, long annotationId);

    public boolean enableAnnotation(long annotationId);
    public boolean disableAnnotation(long annotationId);

    public void clearAnnotationLayer(long viewId, int layer);

    // Returns all annotations (2D and 3D) sorted based on their
    // distance on the screen from the specified screen
    // coordinate. Results are returned in a std::multimap, with the
    // key being the square of the distance from the pick point (in
    // screen pixels), and the value being the AnnotationId

    public AnnotationSearchResult[] getNearestAnnotations(long viewId, 
                          int x, 
                          int y, 
                          boolean onlyEnabledAnnotations);
    
    /******************************************End Annotation Management**********************************************/
    // Returns all tracked pickable data objects sorted based on their
    // distance on the screen from the specified screen
    // coordinate. Results are returned in a std::multimap, with the
    // key being the square of the distance from the pick point (in
    // screen pixels), and the value being a smart pointer to Pickable

    //public PickableMap getNearest(long viewId, int x, int y, boolean onlyEnabled=true);

    
    // Enable/disable the display of route turn arrows */
    public boolean enableTurnArrow(long viewId, int segment);
    public boolean enableAllTurnArrows(long viewId);
    public boolean disableTurnArrow(long viewId, int segment);
    public boolean disableAllTurnArrows(long viewId);
    
    
    // Returns an interface through which clients may configure and control various
    // non-engine support components such as the proxies
    public ITnMapClientSupport getClientSupport();
    
    public boolean disableAnnotationLayer(long viewId, int layer);

    public boolean enableAnnotationLayer(long viewId, int layer);
 
    //Add new model to the engine
    public long addModel(String source);
    public long addModel(byte[] data, int size);
    
    //Remove one previously added model
    public boolean removeModel(long modelId);

    // Enable the displaying of the routes of the specified name.
    public boolean enableRoute(long viewId, String name);
    
    // Disable the displaying of the routes of the specified name.
    public boolean disableRoute(long viewId, String name);

    //A 3D model annotation
    public long addModelAnnotation(long viewId, int layer, long graphicId, double degreesLatitude, double degreesLongitude, double height);
    
    // Pan and zoom (but not rotate) view to encompass given routes within given rect
    public boolean showRegionForRoutes(long viewId, String[] routeNames, int x, int y, int width, int height);
    
    public int reorderRoutes(long viewId, String[] routeNames);
    
    //Opengl 1.6 added
    public void calcRegion(long viewID, double northLat, double westLon, double southLat, double eastLon, int x, int y, int width, int height, float[] zoomArray, double[] latArray, double[] lonArray, boolean gridAligned); 
    
    public void calcRegion(long viewID, double northLat, double westLon, double southLat, double eastLon, int x, int y, int width, int height, float[] zoomArray, double[] latArray, double[] lonArray); 
    
    //Opengl 1.6 added
    public TnBitmap getBitmapSnapshot(int xOrigin, int yOrigin, int width, int height);
    
    //Opengl 1.6 added
    public long createAnnotation(long viewId, String style, TnMapAnnotationParams params, long graphicId);
    
    public long createAnnotation(long viewId, String style, long graphicId, int annotationType);
    
    //Opengl 1.8 added
    public long createAnnotation(long viewId, String style, TnMapAnnotationParams params, long graphicId, long pickableIdNumber);
    
    public TnMapAnnotationParams getDefaultAnnotationParams(long viewId, String style, int zoomLevel);
    
    public TnMapAnnotationParams getDefaultAnnotationParams(long viewId, String style);
    
    //Just the basics of the TnLogger for now...
    public final static int TnLogDisabled = 0;
    public final static int TnLogError    = 1;
    public final static int TnLogWarning  = 2;
    public final static int TnInfo        = 3;
    public final static int TnVerbose     = 4;
    
    public void setAllTopicLevels(int logLevel);
    
    public String[] getLogTopics();

    public void setTopicLevel(String topic, int logLevel);
    
    public void enableRasterType(long viewId, int type);
    
    public static class TouchEvent
    {
        public int event;
        public int x;
        public int y;
        public float pinch;
        public TouchEvent(int event, int x, int y, float pinch)
        {
            this.event = event;
            this.x = x;
            this.y = y;
            this.pinch = pinch;
        }
    }
    
public static class AnnotationSearchResult {
        
        // Inner structures to hold picked annotation details
        public static class IPickable {
        };
        
        public static class LatLonPickable extends IPickable {
            public LatLonPickable(double latitude, double longitude) {
                this.latitude = latitude;
                this.longitude = longitude;
            }
            
            public double latitude;
            public double longitude;
        }
        
        public static class TrafficPickable extends IPickable {
            
            public TrafficPickable(int severity, boolean laneClosed, int incidentType, String crossStreet, String message) {
                this.severity = severity;
                this.laneClosed = laneClosed;
                this.incidentType = incidentType;
                this.crossStreet = crossStreet;
                this.message = message;
            }
            
            // How severe is the incident?
            public static final int SEVERITY_SEVERE = 1;
            public static final int SEVERITY_MAJOR = 2;
            public static final int SEVERITY_MINOR = 3;
            
            public int severity;
            
            // Is the lane closed?
            boolean laneClosed;

            // The type of incident
            public static final int TYPE_ACCIDENT = 1;
            public static final int TYPE_CONGESTION = 2;
            public static final int TYPE_CONSTRUCTION = 3;
            public static final int TYPE_DISABLED_CAR = 4;
            public static final int TYPE_EVENT = 5;
            public static final int TYPE_MISC = 6;
            public static final int TYPE_TRAFFIC_CAMERA = 7;
            public static final int TYPE_SPEED_TRAP = 8;
            
            public int incidentType;
            
            // Cross street text
            public String crossStreet;
            
            // Other incident text
            public String message;
        };
        
        public AnnotationSearchResult(float distance, float eye_distance, float insideX, float insideY, float insideXtexture, float insideYtexture, int layer, long annotationId, IPickable pickable) {
            this.distance = distance;
            this.eye_distance = eye_distance;
            this.insideX = insideX;
            this.insideY = insideY;
            this.insideXtexture = insideXtexture;
            this.insideYtexture = insideYtexture;
            this.layer = layer;
            this.annotationId = annotationId;
            this.pickable = pickable;
        }
        
        // Distance from the touch point (if applicable)
        public float distance;

    // Distance along the pick ray (lower is in front of higher)
    public float eye_distance;
        
        // The point (in "point" size) within the annotation that was touched (if applicable)
        public float insideX;
        public float insideY;
        
        // The point (in texture space) within the annotation that was touched (if applicable)
        public float insideXtexture;
        public float insideYtexture;
        
        // The annotation layer that contains this annotation
        public int layer;
        
        // The touched annotation id (for general, engine-fed annotations)
        public long annotationId;
        
        // The touched Pickable (for proxy-fed annotations, if valid)
        public IPickable pickable;
    };
    
    // Structure to hold engine / view state
    public static class EngineState {
        
        public EngineState(double cameraLatitude, double cameraLongitude, float cameraHeading, float cameraHeight, float cameraFieldOfView, float cameraDeclination, double carLatitude, double carLongitude, float carHeading, float zoomLevel, int dataZoomLevel, int interactionMode, int renderMode, boolean updated, int tilesOnScreen, int tilesWithEdgesLoaded, int tilesWithPolygonsLoaded, int tilesWithTextLoaded, int tilesWithAnnotationsLoaded, int tilesWithLandmarksLoaded, int tilesWithTrafficLoaded, long totalGPUFootprint, long totalGPUTextureFootprint, long totalGPUVboFootprint/*,boolean optimalTileSetLoaded*/, boolean sceneComplete,boolean trafficComplete)
        {
            this.cameraLatitude = cameraLatitude;
            this.cameraLongitude = cameraLongitude;
            this.cameraHeading = cameraHeading;
            this.cameraHeight = cameraHeight;
            this.cameraFieldOfView = cameraFieldOfView;
            this.cameraDeclination = cameraDeclination;
            this.carLatitude = carLatitude;
            this.carLongitude = carLongitude;
            this.carHeading = carHeading;
            this.zoomLevel = zoomLevel;
            this.dataZoomLevel = dataZoomLevel;
            this.interactionMode = interactionMode;
            this.renderMode = renderMode;
//            this.isAnimating = isAnimating;
            this.tilesOnScreen = tilesOnScreen;
            this.tilesWithEdgesLoaded = tilesWithEdgesLoaded;
            this.tilesWithPolygonsLoaded = tilesWithPolygonsLoaded;
            this.tilesWithTextLoaded = tilesWithTextLoaded;
            this.tilesWithAnnotationsLoaded = tilesWithAnnotationsLoaded;
            this.tilesWithLandmarksLoaded = tilesWithLandmarksLoaded;
            this.tilesWithTrafficLoaded = tilesWithTrafficLoaded;
            this.totalGPUFootprint = totalGPUFootprint;
            this.totalGPUTextureFootprint = totalGPUTextureFootprint;
            this.totalGPUVboFootprint = totalGPUVboFootprint;
            this.updated = updated;
            this.sceneComplete = sceneComplete;
            this.trafficComplete = trafficComplete;
        }
        
        public EngineState()
        {
        	
        }
        
        public double cameraLatitude;
        public double cameraLongitude;
        public float cameraHeading;
        public float cameraHeight;
        public float cameraFieldOfView;
        public float cameraDeclination;
        public double carLatitude;
        public double carLongitude;
        public float carHeading;
        public float zoomLevel;
        public int dataZoomLevel; 
        public int interactionMode;
        public int renderMode;
        public boolean isAnimating;
        public boolean updated;
        public int tilesOnScreen;
        public int tilesWithEdgesLoaded;
        public int tilesWithPolygonsLoaded;
        public int tilesWithTextLoaded;
        public int tilesWithAnnotationsLoaded;
        public int tilesWithLandmarksLoaded;
        public int tilesWithTrafficLoaded;
        public long totalGPUFootprint;
        public long totalGPUTextureFootprint;
        public long totalGPUVboFootprint;
        public boolean sceneComplete;
        public boolean trafficComplete;
    };
    
    // Structure to hold TnBitmap
    public static class TnBitmap {
        
        public static final int ALL_THE_FORMATS = 1;
        
        public TnBitmap(byte[] bytes, int width, int height, int format) {
            this.bytes = bytes;
            this.width = width;
            this.height = height;
            this.format = format;
        }
        
        public byte[]     bytes;
        public final int  width;
        public final int  height;
        public final int  format;
        
    }
    
 // The paramaters that only have getters were just for internal usage by the map engine, therefore they've been disabled in the JNI version
    // Structure to hold TnMapAnnotationParams
    public static class TnMapAnnotationParams {
        
        public TnMapAnnotationParams() {}
        
        // cull immunity group name
        public String immunity_group;
        public String GetImmunityGroupName() { return this.immunity_group; }
        public TnMapAnnotationParams SetImmunityGroupName(String immunity_group) { this.immunity_group = immunity_group; return this; }
        
        //positional
        public double degreesLatitude, degreesLongitude, metersHeight;
        public double GetLatitudeDegrees()  { return this.degreesLatitude; }
        public double GetLongitudeDegrees() { return this.degreesLongitude; }
        public double GetHeightMeters()     { return this.metersHeight; }
        public TnMapAnnotationParams SetLatitudeLongitude(double latitude, double longitude) 
                                     { this.degreesLatitude = latitude; this.degreesLongitude = longitude; return this; }
        public TnMapAnnotationParams SetLatitudeLongitude(double latitude, double longitude, double heightMeters) 
                                     { this.degreesLatitude = latitude; this.degreesLongitude = longitude; this.metersHeight = heightMeters; return this; }
        
        //text to display
        public String text;
        public String GetText() { return this.text; }
        public TnMapAnnotationParams SetText(String text) { this.text = text; return this; }
        
        //layer info
        public int layer;
        public int GetAnnotationLayer() { return this.layer; }
        public TnMapAnnotationParams SetAnnotationLayer(int annotationLayer) { this.layer = annotationLayer; return this; }
        
        //priority bias
        public double priorityBias;
        public double GetPriorityBias() { return this.priorityBias; }
        public TnMapAnnotationParams SetPriorityBias(double priorityBias) { this.priorityBias = priorityBias; return this; }
        
        //optional rotation value, inited to 0.0
        public double rotation;
        public double GetRotation() { return this.rotation; }
        public TnMapAnnotationParams SetRotation(double rotation) { this.rotation = rotation; return this; }
        
        
        // config values, only those deemed worthy will be exposed!
        public String style;
        public String GetStyle() { return this.style; }
        public TnMapAnnotationParams SetStyle(String style) { this.style = style; return this; }
        
        // icon graphic name
        public String icon_name;
        public String GetIconName() { return this.icon_name; }
        public TnMapAnnotationParams SetIconName(String iconName) { this.icon_name = iconName; return this; }
        
        // icon text offset
        public  int    text_x, text_y;
        public  int GetTextX() { return this.text_x; }
        public  int GetTextY() { return this.text_y; }
        public  TnMapAnnotationParams SetTextX(int textX) { this.text_x = textX; return this; }
        public  TnMapAnnotationParams SetTextY(int textY) { this.text_y = textY; return this; }
        
        // icon offset
        public int    icon_x, icon_y;
        public int GetIconX() { return this.icon_x; }
        public int GetIconY() { return this.icon_y; }
        public TnMapAnnotationParams SetIconX(int iconX) { this.icon_x = iconX; return this; }
        public TnMapAnnotationParams SetIconY(int iconY) { this.icon_y = iconY; return this; }
        
        // icon dimensions
        public int    icon_w, icon_h;
        public int GetIconWidth()  { return this.icon_w; }
        public int GetIconHeight() { return this.icon_h; }
        public TnMapAnnotationParams SetIconWidth(int iconWidth)   { this.icon_w = iconWidth;  return this; }
        public TnMapAnnotationParams SetIconHeight(int iconHeight) { this.icon_h = iconHeight; return this; }
        
        // text color
        public byte textColorRed, textColorBlue, textColorGreen, textColorAlpha;
        public byte GetTextColorRed()      { return this.textColorRed; }
        public byte GetTextColorBlue()     { return this.textColorBlue; }
        public byte GetTextColorGreen()    { return this.textColorGreen; }
        public byte GetTextColorAlpha()    { return this.textColorAlpha; }
        public TnMapAnnotationParams SetTextColor(byte red, byte blue, byte green, byte alpha)
        {
            this.textColorRed = red; this.textColorBlue = blue; this.textColorGreen = green; this.textColorAlpha = alpha;
            return this;
        }
        
        // outline color
        public byte outlineColorRed, outlineColorBlue, outlineColorGreen, outlineColorAlpha;
        public byte GetOutlineColorRed()      { return this.outlineColorRed; }
        public byte GetOutlineColorBlue()     { return this.outlineColorBlue; }
        public byte GetOutlineColorGreen()    { return this.outlineColorGreen; }
        public byte GetOutlineColorAlpha()    { return this.outlineColorAlpha; }
        public TnMapAnnotationParams SetOutlineColor(byte red, byte blue, byte green, byte alpha)
        {
            this.outlineColorRed = red; this.outlineColorBlue = blue; this.outlineColorGreen = green; this.outlineColorAlpha = alpha;
            return this;
        }
        
        // texture coordinates for icon (default regular 0-1)
        public double texcoord_left, texcoord_bottom, texcoord_right, texcoord_top;
        public double GetTexCoordLeft()   { return this.texcoord_left; }
        public double GetTexCoordBottom() { return this.texcoord_bottom; }
        public double GetTexCoordRight()  { return this.texcoord_right; }
        public double GetTexCoordTop()    { return this.texcoord_top; }
        public TnMapAnnotationParams SetTexCoordLeft(double texcoord_left)     { this.texcoord_left = texcoord_left; return this; }
        public TnMapAnnotationParams SetTexCoordBottom(double texcoord_bottom) { this.texcoord_bottom = texcoord_bottom; return this; }
        public TnMapAnnotationParams SetTexCoordRight(double texcoord_right)   { this.texcoord_right = texcoord_right; return this; }
        public TnMapAnnotationParams SetTexCoordTop(double texcoord_top)       { this.texcoord_top = texcoord_top; return this; }
        
        public double animation_time;
        public double GetAnimationTime() { return this.animation_time; }
        public TnMapAnnotationParams SetAnimationTime(double animation_time) { this.animation_time = animation_time; return this; }
        
        public int annotationType;
        public int GetAnnotationType() { return this.annotationType; }
        public TnMapAnnotationParams SetAnnotationType(int annotationType) { this.annotationType = annotationType; return this; }
        
//        IMapEngine.AnnotationSearchResult.IPickable annotationPickable;
//        public IMapEngine.AnnotationSearchResult.IPickable GetAnnotationPickable(){return this.annotationPickable;}
//        public TnMapAnnotationParams SetAnnotationPickable(IMapEngine.AnnotationSearchResult.IPickable annotationPickable) { this.annotationPickable = annotationPickable; return this; }
        
        //DON'T MESS WITH THIS, should be private
        public String styleKey;
        
    }
}

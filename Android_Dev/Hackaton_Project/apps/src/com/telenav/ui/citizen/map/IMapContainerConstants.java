/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IMapContainerConstants.java
 *
 */
package com.telenav.ui.citizen.map;

/**
 *@author yren
 *@date Sep 28, 2012
 */
public interface IMapContainerConstants
{
    public static final int MAP_DAY_COLOR = 1;

    public static final int MAP_NIGHT_COLOR = 2;

    public static final int MAP_SAT_COLOR = 3;   
    
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
    public static final int RENDERING_MODE_2D_NORTH_UP = 1;
    public static final int RENDERING_MODE_2D_HEADING_UP = 2;
    public static final int RENDERING_MODE_3D_NORTH_UP = 3;
    public static final int RENDERING_MODE_3D_HEADING_UP = 4;

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
    public static final int TOUCH_EVENT_TAP = 4;
    public static final int TOUCH_LONG_TOUCH = 5;

    // Annotation Layer - Predefined annotation layers plus a user
    // defined space for custom annotations.
    public static final int ANNOTATION_LAYER_NO_LAYER = -1;
    public static final int ANNOTATION_LAYER_TILE_POI_LAYER = 0;          //!< Tile POI 
    public static final int ANNOTATION_LAYER_POI_LAYER = 1;          //!< POI Icons
    public static final int ANNOTATION_LAYER_TRAFFIC_INCIDENT = 2;   //!< Traffic Icons
    public static final int ANNOTATION_LAYER_TRAFFIC_CAMERA = 3;     //!< Camera Icons
    public static final int ANNOTATION_LAYER_TRAFFIC_SPEED_TRAP = 4;  //!< Speed Trap Icons
    public static final int ANNOTATION_LAYER_ROUTE_WAYPOINT = 5;     //!< Begin/End/Waypoints of route
    public static final int ANNOTATION_LAYER_VEHICLE = 6;           //!< Vehicle Icons
    public static final int ANNOTATION_LAYER_USER_DEFINED = 32;      //!< First custom annotation layer
    public static final int ANNOTATION_LAYER_CONTROLLER = 80;      //!< First custom annotation layer
    public static final int ANNOTATION_LAYER_TRAFFIC_INCIDENT_CUSTOM = 90;      //!< First custom annotation layer
    public static final int ANNOTATION_LAYER_MAX_USER_DEFINED = 127;  //!< Last custom annotation layer
    
    public static final int NAV_SDK_Z_ORDER_NO_LAYER = -1;
    public static final int NAV_SDK_Z_ORDER_TILE_POI_LAYER = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_TILE_POI_LAYER;          //!< Tile POI 
    public static final int NAV_SDK_Z_ORDER_POI_LAYER = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_POI_LAYER;          //!< POI Icons
    public static final int NAV_SDK_Z_ORDER_TRAFFIC_INCIDENT = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_TRAFFIC_INCIDENT;   //!< Traffic Icons
    public static final int NAV_SDK_Z_ORDER_TRAFFIC_CAMERA = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_TRAFFIC_CAMERA;     //!< Camera Icons
    public static final int NAV_SDK_Z_ORDER_TRAFFIC_SPEED_TRAP = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_TRAFFIC_SPEED_TRAP;  //!< Speed Trap Icons
    public static final int NAV_SDK_Z_ORDER_ROUTE_WAYPOINT = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_ROUTE_WAYPOINT;     //!< Begin/End/Waypoints of route
    public static final int NAV_SDK_Z_ORDER_VEHICLE = 65;           //!< Vehicle Icons
    public static final int NAV_SDK_Z_ORDER_USER_DEFINED = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_USER_DEFINED;      //!< First custom annotation layer
    public static final int NAV_SDK_Z_ORDER_CONTROLLER = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_CONTROLLER;      //!< First custom annotation layer
    public static final int NAV_SDK_Z_ORDER_TRAFFIC_INCIDENT_CUSTOM = ANNOTATION_LAYER_MAX_USER_DEFINED - ANNOTATION_LAYER_TRAFFIC_INCIDENT_CUSTOM;      //!< First custom annotation layer
    public static final int NAV_SDK_Z_ORDER_MAX_USER_DEFINED = 0;  //!< Last custom annotation layer

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
}

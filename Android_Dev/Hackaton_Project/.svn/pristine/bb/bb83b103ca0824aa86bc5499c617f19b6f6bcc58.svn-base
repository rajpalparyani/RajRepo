/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapConf.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.map.IMapEngine;
import com.telenav.map.opengl.java.TnMap.TnMapColor;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapConf
{
 // Default view parameters
    public static final float  DEFAULT_FOV = 55.0f;         // diagonal field-of-view
    public static final float  DEFAULT_HEADING = 0.0f;
    public static final float  DEFAULT_DECLINATION_2D = -89.0f;
    public static final float  DEFAULT_EYE_DISTANCE = 4.0f;    // Relative to tile size.
    public static final float  DEFAULT_ZOOM = 2.5f;                 // in log 2 space
    public static final float  DEFAULT_RANGE = 10.0f;
    public static final float  DEFAULT_BLEND_TIME = 0.5f;
    public static final float  DEFAULT_ZOOM_LOWER_BOUND = 0.25f;
    public static final float  DEFAULT_ZOOM_UPPER_BOUND = 15.0f;
    public static final float  DEFAULT_ZOOM_SPEED = 1.0f;
    
    public static final int DEFAULT_INTERACTION_MODE = IMapEngine.INTERACTION_MODE_PAN_AND_ZOOM;
    public static final int   DEFAULT_RENDERING_MODE = IMapEngine.RENDERING_MODE_3D_NORTH_UP;
    public static final int     DEFAULT_ORIENTATION = IMapEngine.ORIENTATION_PORTRAIT_BOTTOM;
    
   // Map cache
    public static final int MAP_CACHE_CAPACITY = 150;

   // Default camera declination
    public static final float DEFAULT_CAMERA_DECLINATION_3D = -45.0f;

   // Holding area for constants that we eventually will turn into JSON configuration
    public static final float DECLINATION_FOR_TEXT_2D3D_SWITCH = -70.0f;

   // Scale value for 2D stuff - This will go away when we actually calculate pixel-perfect given view
    public static final float SCALE_VALUE_FOR_2D_STUFF = 5.0f;

   // Touch interaction
    public static final float TOUCH_ZOOM_SENSITIVITY = 0.01f; // magic number
    public static final float TOUCH_HEADING_SENSITIVITY = 0.4f; // magic number that will definitely go away
    public static final float TOUCH_DECLINATION_SENSITIVITY = 0.25f; // magic number that will definitely go away
       
   // Pan/rotate friction as a multiple of momentum
    public static final double FRICTION_FACTOR = 2.0;

   // Vertical fraction of screen beyond which we don't draw text flags
    public static final float BILLBOARD_VISIBILITY_THRESHOLD = 0.6f;

   // Radius for text collisions
    public static final float TEXT_CONFLICT_RADIUS = 160;

   // Scale factor for 2D text
    public static final float TEXT_2D_SCALE_FACTOR = 0.4f;

   // If egde segments at sharper turn than this, start new edge.  @TODO: Put this in configuration.
    public static final double MAX_EDGE_SEGMENT_ANGLE = 45.0;
    
    public static  TnMapColor[] m_arrColors = new TnMapColor[]
    {
        new TnMapColor("#dededeff"),
        new TnMapColor("#a3cc99ff"),
        new TnMapColor("#c5c4c0ff"),
        new TnMapColor("#8fb2e0ff"),
        new TnMapColor("#a0a0a0ff"),
        new TnMapColor("#aaaaaaff"),
        new TnMapColor("#8fb2ddff"),
        new TnMapColor("#3a72e6ff"),
        new TnMapColor("#1e1e1eff"),
        new TnMapColor("#8fb2ddff"),
        new TnMapColor("#887e6dff"),
        new TnMapColor("#887e6dff"),
        new TnMapColor("#887e6dff"),
        new TnMapColor("#887e6dff"),
        new TnMapColor("#887e6dff"),
        new TnMapColor("#887e6dff"),
        new TnMapColor("#887e6dff"),
        new TnMapColor("#ffffffff"),
        new TnMapColor("#ffffffff"),
        new TnMapColor("#ffffffff"),
        new TnMapColor("#fff59fff"),
        new TnMapColor("#fff59fff"),
        new TnMapColor("#fff59fff"),
        new TnMapColor("#dfa800ff"),
        new TnMapColor("#862f2fff"),
        new TnMapColor("#0000ffff"),
        new TnMapColor("#ff4040ff")
    };
    
    public static double[][] m_arrWidth =
    {
        // 0,   1,   2,   3,   4,   5,   6,   7,   8,   9,   10,  11,  12,  13,  14,  15,  16},
        {1.2, 1.2, 1.2, 1.2, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.8, 0.8, 0.8, 0.1, 0.0},      // FEATURE_TYPE_STATEBORDER
        {2.8, 2.8, 2.8, 2.8, 2.4, 2.2, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 0.6, 0.05},     // FEATURE_TYPE_NATIONBORDER

        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0},      // FEATURE_TYPE_WATERLINE
        {1.3, 1.2, 1.1, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},      // FEATURE_TYPE_FERRYBOAT
        {3.0, 3.0, 3.0, 2.6, 2.6, 2.6, 2.2, 2.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},      // FEATURE_TYPE_RAIL
        {1.0, 1.0, 1.0, 1.0, 0.8, 0.6, 0.5, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 1.0, 0.4, 0.1, 0.05},     // FEATURE_TYPE_CANAL

        {8.0, 6.8, 5.0, 3.9, 3.5, 2.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},            // FEATURE_TYPE_SMALLSTREET_BORDER
        {9.2, 8.0, 6.8, 4.4, 3.6, 2.7, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},            // FEATURE_TYPE_STREET_BORDER
        {9.2, 8.0, 6.8, 4.4, 3.6, 2.7, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},            // FEATURE_TYPE_INTERSECTION_BORDER
        
        {7.2,  6.6, 5.5, 4.5, 4.5, 2.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},     // FEATURE_TYPE_RAMP_BORDER
        {7.8, 6.6, 6.1, 4.5, 4.5, 3.3, 2.5, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},     // FEATURE_TYPE_HIGHWAYRAMP_BORDER
        
        {9.3, 8.6, 8.0, 6.1, 4.4, 3.5, 3.3, 3.2, 2.7, 2.4, 2.4, 2.3, 2.3, 0.0, 0.0, 0.0, 0.0},           // FEATURE_TYPE_ARTERIAL_BORDER
        {11.5, 10.4, 9.2, 7.5, 6.7, 5.9, 5.3, 5.3, 4.8, 4.5, 4.3, 4.0, 3.9, 0.0, 0.0, 0.0, 0.0},           // FEATURE_TYPE_HIGHWAY_BORDER

        {6.0, 4.8, 3.0, 1.9, 1.5, 0.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},     // FEATURE_TYPE_SMALLSTREET
        {7.2, 6.0, 4.8, 2.4, 1.6, 0.7, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},            // FEATURE_TYPE_STREET
        {7.2, 6.0, 4.8, 2.4, 1.6, 0.7, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},            // FEATURE_TYPE_INTERSECTION
        
        {5.2,  4.6, 3.5, 2.5, 2.5, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},     // FEATURE_TYPE_RAMP
        {5.8,  5.6, 4.1, 2.5, 2.5, 1.3, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},     // FEATURE_TYPE_HIGHWAYRAMP
        
        {7.3, 6.6, 6.0, 4.1, 2.4, 1.5, 1.3, 1.2, 0.7, 0.4, 0.4, 0.3, 0.3, 0.0, 0.0, 0.0, 0.0},            // FEATURE_TYPE_ARTERIAL
        {8.7, 7.9, 6.7, 5.0, 4.2, 3.4, 2.8, 2.8, 2.3, 2.0, 1.8, 1.5, 1.4, 0.0, 0.0, 0.0, 0.0},            // FEATURE_TYPE_HIGHWAY

        {1.3, 1.2, 1.1, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},      // FEATURE_TYPE_DATE_LINE
        {9.0, 8.1, 7.3, 6.6, 5.9, 5.3, 4.8, 4.3, 3.9, 3.5, 3.1, 2.8, 2.5, 2.3, 2.1, 1.9, 1.7},      // FEATURE_TYPE_ROUTE
        {8.0, 7.2, 6.5, 5.8, 5.2, 4.7, 4.3, 3.8, 3.4, 3.1, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0},      // FEATURE_TYPE_ARROW
    };


    public static int[][] m_arrLabel =
    {
        //   0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16},
        {1, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},     // ROAD_TYPE_SMALLSTREET
        {1, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},         // ROAD_TYPE_STREET
        {1, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},         // ROAD_TYPE_INTERSECTION
        
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},     // ROAD_TYPE_RAMP
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},     // ROAD_TYPE_HIGHWAYRAMP
        
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1,  1,  0,  0,  0,  0},         // ROAD_TYPE_ARTERIAL
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1,  1,  0,  0,  0,  0},         // ROAD_TYPE_HIGHWAY
    };
    
//        {
            //  region type:
            public static int REGION_TYPE_MIN = 1;
            public static int REGION_TYPE_TURNINDICATOR = REGION_TYPE_MIN; 
            public static int REGION_TYPE_UNKNOWN = 2;
            public static int REGION_TYPE_GEOPOLITICAL = 3;
            public static int REGION_TYPE_PARK = 4;
            public static int REGION_TYPE_CAMPUS = 5;
            public static int REGION_TYPE_WATER = 6;
            public static int REGION_TYPE_INTERIOR = 7;
            public static int REGION_TYPE_BUILDING = 8;
            public static int REGION_TYPE_MAX = REGION_TYPE_BUILDING;
                    
            //  road type:
            public static int ROAD_TYPE_MIN = 11;
            public static int ROAD_TYPE_RAMP = ROAD_TYPE_MIN;
            public static int ROAD_TYPE_HIGHWAYRAMP = 12;
            public static int ROAD_TYPE_SMALLSTREET = 13; 
            public static int ROAD_TYPE_STREET = 14;
            public static int ROAD_TYPE_INTERSECTION = 15; 
            public static int ROAD_TYPE_ARTERIAL = 16;
            public static int ROAD_TYPE_HIGHWAY = 17;
            public static int ROAD_TYPE_MAX = ROAD_TYPE_HIGHWAY;
                
            public final static int LINE_TYPE_MIN  = 21;
            public final static int LINE_TYPE_RAIL = LINE_TYPE_MIN;
            public static int LINE_TYPE_CANAL  = 22;
            public static int LINE_TYPE_STATEBORDER  = 23;
            public static int LINE_TYPE_NATIONBORDER  = 24;
                    
            public static int LINE_TYPE_WATER_LINE  = 25;
            public final static int LINE_TYPE_FERRYBOAT  = 26;
            public static int LINE_TYPE_DATE_LINE  = 27;
            public static int LINE_TYPE_MAX = LINE_TYPE_DATE_LINE;
//        };
                    
//        enum
//        {
            public static int FEATURE_TYPE_GEOPOLITICAL               = 0;
                    
            public static int FEATURE_TYPE_PARK                       = 1;
            public static int FEATURE_TYPE_CAMPUS                     = 2;
            public static int FEATURE_TYPE_WATER                      = 3;
                    
            public static int FEATURE_TYPE_1D_BASE                    = 4;
                    
            public static int FEATURE_TYPE_STATEBORDER                = 4; 
            public static int FEATURE_TYPE_NATIONBORDER               = 5; 
                    
            public static int FEATURE_TYPE_WATER_LINE                 = 6;
            public static int FEATURE_TYPE_FERRYBOAT                  = 7; 
            public static int FEATURE_TYPE_RAIL                       = 8; 
            public static int FEATURE_TYPE_CANAL                      = 9; 
                    
            public static int FEATURE_TYPE_SMALLSTREET_BORDER         = 10;
            public static int FEATURE_TYPE_STREET_BORDER              = 11;
            public static int FEATURE_TYPE_INTERSECTION_BORDER        = 12;
                    
            public static int FEATURE_TYPE_RAMP_BORDER                = 13; 
            public static int FEATURE_TYPE_HIGHWAYRAMP_BORDER         = 14; 
                    
            public static int FEATURE_TYPE_ARTERIAL_BORDER            = 15;
            public static int FEATURE_TYPE_HIGHWAY_BORDER             = 16;
                    
            public static int FEATURE_TYPE_SMALLSTREET                = 17; 
            public static int FEATURE_TYPE_STREET                     = 18;
            public static int FEATURE_TYPE_INTERSECTION               = 19;
                    
            public static int FEATURE_TYPE_RAMP                       = 20; 
            public static int FEATURE_TYPE_HIGHWAYRAMP                = 21;
                    
            public static int FEATURE_TYPE_ARTERIAL                   = 22;
            public static int FEATURE_TYPE_HIGHWAY                    = 23; 
                    
            public static int FEATURE_TYPE_DATE_LINE                  = 24;
            public static int FEATURE_TYPE_ROUTE                      = 25;
            public static int FEATURE_TYPE_ARROW                      = 26;
//        };

//        enum
//        {
            public static int FEATURE_TYPE_COUNT                      = 27;
            public static int FEATURE_1D_COUNT                        = 23;
//        };

//        enum    //!< Type count for features in map data
//        {
            public static int REGION_TYPE_COUNT                       = 8;
            public static int ROAD_TYPE_COUNT                         = 7;
            public static int LINE_TYPE_COUNT                         = 7;
//        };
        
//        enum
//        {
            public static int FEATURE_LEVEL_COUNT                     = 17;
            public static int FEATURE_LAYER_COUNT                     = 10;
//        };
            
//        enum TRAFFIC_COLOR
//        {
            public final static int TRAFFIC_COLOR_SLOW = 0;
            public final static int TRAFFIC_COLOR_MEDIUM = 1;
            public final static int TRAFFIC_COLOR_FAST = 2;
//        };
        
//        enum
//        {
            public static int TRAFFIC_WIDTH_RATIO = 0;
//        };

//        struct FeatureColor
//        {
//            TnMapColor colorDay;
//            TnMapColor colorNight;
//        };
            
            public static int[] m_arrFeatureLayer= new int[]
       {
           0,  //FEATURE_TYPE_GEOPOLITICAL,

           1,  //FEATURE_TYPE_PARK,
           1,  //FEATURE_TYPE_CAMPUS,
           1,  //FEATURE_TYPE_WATER,

           2,  //FEATURE_TYPE_STATEBORDER,
           2,  //FEATURE_TYPE_NATIONBORDER,

           3,  //FEATURE_TYPE_WATER_LINE,
           3,  //FEATURE_TYPE_FERRYBOAT, 
           3,  //FEATURE_TYPE_RAIL, 
           3,  //FEATURE_TYPE_CANAL, 

           4,  //FEATURE_TYPE_SMALLSTREET_BORDER, 
           4,  //FEATURE_TYPE_STREET_BORDER,
           4,  //FEATURE_TYPE_INTERSECTION_BORDER,
           
           5,  //FEATURE_TYPE_RAMP_BORDER, 
           5,  //FEATURE_TYPE_HIGHWAYRAMP_BORDER, 

           6,  //FEATURE_TYPE_ARTERIAL_BORDER,
           7,  //FEATURE_TYPE_HIGHWAY_BORDER,

           8,  //FEATURE_TYPE_SMALLSTREET, 
           8,  //FEATURE_TYPE_STREET, 
           8,  //FEATURE_TYPE_INTERSECTION,

           9,  //FEATURE_TYPE_RAMP, 
           9,  //FEATURE_TYPE_HIGHWAYRAMP,
           
           10,  //FEATURE_TYPE_ARTERIAL, 
           11,  //FEATURE_TYPE_HIGHWAY, 

           12,  //FEATURE_TYPE_DATE_LINE,
           13, //FEATURE_TYPE_ROUTE,
           14, //FEATURE_TYPE_ARROW,
       };
    
    public static int[]    m_arrTypeMap = new int[]
    {
        REGION_TYPE_GEOPOLITICAL,
        REGION_TYPE_PARK,
        REGION_TYPE_CAMPUS,
        REGION_TYPE_WATER,

        LINE_TYPE_STATEBORDER,
        LINE_TYPE_NATIONBORDER,

        LINE_TYPE_WATER_LINE,
        LINE_TYPE_FERRYBOAT,
        LINE_TYPE_RAIL,
        LINE_TYPE_CANAL,

        ROAD_TYPE_SMALLSTREET,
        ROAD_TYPE_STREET,
        ROAD_TYPE_INTERSECTION,
        
        ROAD_TYPE_RAMP,
        ROAD_TYPE_HIGHWAYRAMP,
        
        ROAD_TYPE_ARTERIAL,
        ROAD_TYPE_HIGHWAY,

        ROAD_TYPE_SMALLSTREET,
        ROAD_TYPE_STREET,
        ROAD_TYPE_INTERSECTION,
        
        ROAD_TYPE_RAMP,
        ROAD_TYPE_HIGHWAYRAMP,
        
        ROAD_TYPE_ARTERIAL,
        ROAD_TYPE_HIGHWAY,

        LINE_TYPE_DATE_LINE,
        FEATURE_TYPE_ROUTE,
        FEATURE_TYPE_ARROW,
    };

    public static TnMapColor m_backgroundColor = new TnMapColor("#ebe6dcff");
    public static TnMapColor m_roadTextColor = new TnMapColor("#000000ff");
    public static TnMapColor m_trafficSlowColor = new TnMapColor("#ff00008f");
    public static TnMapColor m_trafficMediumColor = new TnMapColor("#ffff008f");
    public static TnMapColor m_trafficFastColor = new TnMapColor("#00ff008f");
    public static float      m_trafficWidthRatio = 0.4f;

    public static TnMapColor getBackgroundColor()
    {
        return m_backgroundColor;
    }

    public static void setBackgroundColor(TnMapColor color)
    {
        m_backgroundColor = color;
    }

    public static void setRoadTextColor(TnMapColor color)
    {
        m_roadTextColor = color;
    }

    public static TnMapColor getRoadTextColor()
    {
        return m_roadTextColor;
    }

    public static void setTrafficSpeedColor(int type, TnMapColor color)
    {
        switch(type)
        {
        case TRAFFIC_COLOR_SLOW:
            m_trafficSlowColor = color;
            break;
        case TRAFFIC_COLOR_MEDIUM:
            m_trafficMediumColor = color;
            break;
        case TRAFFIC_COLOR_FAST:
            m_trafficFastColor = color;
            break;
        }
    }

    public static TnMapColor getTrafficSpeedColor(int type)
    {
        switch(type)
        {
        case TRAFFIC_COLOR_SLOW:
            return m_trafficSlowColor;
        case TRAFFIC_COLOR_MEDIUM:
            return m_trafficMediumColor;
        case TRAFFIC_COLOR_FAST:
            return m_trafficFastColor;
        }

        return m_trafficFastColor;
    }

    public static void setFloat(int type, float value)
    {
        if(type == TRAFFIC_WIDTH_RATIO)
        {
            m_trafficWidthRatio = value;
        }
    }

    public static float getFloat(int type)
    {
        float value = 0.0f;
        
        if(type == TRAFFIC_WIDTH_RATIO)
        {
            value = m_trafficWidthRatio;
        }
        
        return value;
    }

    public static float getFeatureWidth(int type, int zoom)
    {
        if((type < FEATURE_TYPE_1D_BASE) || (zoom >= FEATURE_LEVEL_COUNT)) return 1.0f;
        
        return (float)m_arrWidth[type - FEATURE_TYPE_1D_BASE][zoom];
    }

    public static void setFeatureWidth(int type, int zoom, float width)
    {
        if((type < FEATURE_TYPE_1D_BASE) || (zoom >= FEATURE_LEVEL_COUNT)) return;
        
        m_arrWidth[type - FEATURE_TYPE_1D_BASE][zoom] = width;
    }

    public static boolean getRoadLabelFlag(int type, int zoom)
    {
        if((type < ROAD_TYPE_MIN || type > ROAD_TYPE_MAX) || (zoom >= FEATURE_LEVEL_COUNT)) return false;
        
        return m_arrLabel[type - ROAD_TYPE_MIN][zoom] == 1 ? true : false;
    }

    public static void setRoadLabelFlag(int type, int zoom, boolean bMark)
    {
        if((type < ROAD_TYPE_MIN || type > ROAD_TYPE_MAX) || (zoom >= FEATURE_LEVEL_COUNT)) return;
        
        m_arrLabel[type - ROAD_TYPE_MIN][zoom] = bMark ? 1 : 0;
    }

    public static TnMapColor getFeatureColor(int type)
    {
        TnMapColor color = null;
        
        if(type >= FEATURE_TYPE_COUNT) return color;

        color = m_arrColors[type];
        
        return color;
    }

    public static void setFeatureColor(int type, TnMapColor color)
    {
        if(type >= FEATURE_TYPE_COUNT) return;
        
        m_arrColors[type] = color;
    }

    public static boolean isPolygon(int type)
    {
        return ((type >= REGION_TYPE_MIN) && (type <= REGION_TYPE_MAX));
    }


    public static boolean isDashed(int type)
    {
        switch(type)
        {
        case LINE_TYPE_FERRYBOAT:
        case LINE_TYPE_RAIL:
            return true;
        }

        return false;
    }

    public static void getPolygonLayers(int[] layers)
    {
        layers[0] = 0;
        layers[1]   = 1;
    }

    public static int getFreewayLayer()
    {
        return 8;
    }

    public static float getFreewayWidth(int zoom)
    {
        return getFeatureWidth(FEATURE_TYPE_HIGHWAY, zoom);
    }

    public static void getRoadLayers(int[] layers)
    {
        layers[0] = 2;
        layers[1] = 12;
    }
}

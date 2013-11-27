package com.telenav.mapcontent;

/**
 * map related configurations, could be changed by application 
 *
 */
public class MapContentConfig
{
    public static long TRAFFIC_TILE_EXPIRE_TIME             = 5 * 60 * 1000L;
    
    public static long MAP_TILE_EXPIRE_TIME                 = 90 * 86400 * 1000L;
    
    public static int MAX_REQUEST_TILE_COUNT                = 24;
    
    public static int MAX_TILE_REQUEST_CACHE_SIZE           = 128;
    
    public static int PNG_MAP_TILE_DATA_CACHE_SIZE          = 128;
    
    public static int PNG_TRAFFIC_TILE_DATA_CACHE_SIZE      = 128;
    
    public static long COMM_TIMEOUT                         = 15 * 1000L; 
    
    public static int TILE_COUNT_ON_SCREEN                  = 12;
    
    public static void setParameters(int screenWidth, int screenHeight, 
            int tilePixelSize)
    {
        if (screenWidth == 0 || screenHeight == 0)
        {
            screenWidth = 480;
            screenHeight = 854;
        }
        int wCount = screenWidth / tilePixelSize + 1;
        if (screenWidth % tilePixelSize != 0)
            wCount ++;
        
        int hCount = screenWidth / tilePixelSize + 1;
        if (screenWidth % tilePixelSize != 0)
            hCount ++;
        
        TILE_COUNT_ON_SCREEN = MAX_REQUEST_TILE_COUNT = wCount * hCount;
        MAX_TILE_REQUEST_CACHE_SIZE = MAX_REQUEST_TILE_COUNT * 4;
        PNG_MAP_TILE_DATA_CACHE_SIZE = TILE_COUNT_ON_SCREEN * 5;
    }
}

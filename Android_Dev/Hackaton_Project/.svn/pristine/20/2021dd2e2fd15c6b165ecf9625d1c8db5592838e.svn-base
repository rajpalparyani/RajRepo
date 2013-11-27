package com.telenav.map;


/**
 *@author jyxu(jyxu@telenav.cn)
 *@date 2010-11-9
 */

public interface ITnMapClientSupport
{

        // CACHE

        // Create the disk-based cache. Must be called before calling any other cache APIs
        public boolean cacheCreate(String indexfile);
        
        // Remove the Cache object
        public void cacheRemove();
        
        // Cache conflict policy
        public static final int CONFLICT_POLICY_USE_OLD = 0;             // Favor old data
        public static final int CONFLICT_POLICY_USE_NEW_IF_EXPIRED = 1;    // Favor new data if old data is expired
        public static final int CONFLICT_POLICY_USE_NEW = 2;             // Favor new data
        
        // Set cache conflict policy, how do we treat duplicate data?
        // Default is eConflictPolicy_UseNew
        public void cacheSetConflictPolicy(int policy);
        
        // Cache storage policy
        public static final int STORAGE_POLICY_NOT_STORED = 0;           // Do not store data in cache
        public static final int STORAGE_POLICY_SESSION_DURATION = 1;     // Cache contents live within TnCache scope
        public static final int STORAGE_POLICY_PERMANENT = 2;           // Cache contents saved when TnCache goes out of scope
        
        // Set cache storage policy, how long do we store data?
        // Default is eStoragePolicy_SessionDuration
        public void cacheSetStoragePolicy(int policy);
        
        // Cache eviction policy
        public static final int EVICTION_POLICY_NO_EVICTION = 0;         // Do not evict data from cache (don't use this!)
        public static final int EVICTION_POLICY_EXPIRED = 1;            // Evict expired data only, ordered by expiration date
        public static final int EVICTION_POLICY_LRU = 2;                // Evict least recently used data first [DEFAULT]
        public static final int EVICTION_POLICY_EXPIRED_LRU = 3;         // Evict expired data first, then LRU unexpired data
    
        // Set cache eviction policy, what data gets deleted when cache is full?
        // Default is eEvictionPolicy_LRU
        public void cacheSetEvictionPolicy(int policy);
        
        // Cache data access policy
        //
        // NOTE: If the server does not have the capability of reporting whether it has an update,
        //       eAccessPolicy_AskServerIfModifiedWhenStale behaves as eAccessPolicy_LoadIfStale and
        //       eAccessPolicy_AskServerIfModified behaves as eAccessPolicy_Ignore
        //
        public static final int ACCESS_POLICY_ASK_SERVER_IF_MODIFIED_WHEN_STALE = 0;
        public static final int ACCESS_POLICY_ASK_SERVER_IF_MODIFIED = 1;
        public static final int ACCESS_POLICY_LOAD_IF_STALE = 2;
        public static final int ACCESS_POLICY_LOAD_IF_NOT_CACHED = 3;
        public static final int ACCESS_POLICY_DONT_LOAD = 4;
        public static final int ACCESS_POLICY_IGNORE = 5;
        
        public static final int REQUEST_RESULT_COMPLETE = 0;
        public static final int REQUEST_RESULT_FAIL     = 1;
        
        // Set cache access policy, under what conditions do we check the cache?
        // Default is eAccessPolicy_AskServerIfModifiedWhenStale
        public void cacheSetAccessPolicy(int policy);
        
        // Set the size of the cache beyond which eviction occurs, in bytes (default: unlimited)
        // NOTE: Size may increase beyond this value if the current
        // eviction policy does not allow any objects to be evicted.
        public void cacheSetCapacityBytes(long bytes);
        
        // Set the size of the cache beyond which eviction occurs, in objects (default: unlimited)
        // NOTE: Size may increase beyond this value if the current
        // eviction policy does not allow any objects to be evicted.
        public void cacheSetCapacityObjects(int objects);
        
        // PRELOADER
        
        // Create the cache preloader. Must be called before calling any other preloader APIs
        public void preloaderCreate();

        // Start preloader
        public void preloaderStart();
        
        // Stop preloader
        public void preloaderStop();
        
        
        // Notify preloader of new routes
        public void preloaderNotifyNewRoutes();
        
        // Set new engine states for preloader to consider
        public void preloaderSetEngineState(IMapEngine.EngineState state);

        // Set preloader strategy to "along all routes"
        public void preloaderSetStrategyAlongAllRoutes();

        // Set preloader strategy to "along route"
        public void preloaderSetStrategyAlongRoute(String routeName);
        
        // Set preloader strategy to "radius from car"
        public void preloaderSetStrategyRadiusFromCar(int tiles);
        
	    public static class ServerDesc {
	        public String hostname;
	        public String longPort;
	        public String shortPort;
	    }
	    
	    public static class Credentials {
	        public String phoneNumber;
	        public String pin;
	        public String userId;
	        public String carrier;
	        public String platform;
	        public String version;
	        public String device;
	        public String buildNumber;
	        public String gpsType;
	        public String productType;
	        public String audioLevel;
	        public String audioLanguage;
	        public String iconMode;
	        public String units;
	        public String prefLocale;
	        public String prefRegion;
	    }
	    
	    //Opengl 1.6 remove these 3 apis
	    // Set C-server host
//	    public void setCServerHost(ServerDesc server);
	    
	    // set C-server credentials
//	    public void setCServerCredentials(Credentials credentials);
	    
	    // set C-server host and credentials
//	    public void setCServerHostAndCredentials(ServerDesc server, Credentials credentials);
	    
        public void setTrafficCredentials(ITnMapClientSupport.Credentials credentials);
        
	    // ROUTE STUFFING
	    
	    // Clear any routes stored within the route proxy
	    public void setRouteClear();
	    
	    // Add a named route to the route proxy
	    public void setRouteNewRoute(String name);
	    
	    // Add a route segment to the current route in the route proxy
	    public void setRouteNewSegment();
	    
	    // Add an edge to the current segment in the route proxy
	    public void setRouteNewEdge();
	    
	    // Add a point to the current edge in the route proxy
	    public void setRouteAddPoint(double degreesLatitude, double degreesLongitude, double metersHeight);
	    
	    public void setRouteNewRoute(String name, String style);
	    
	    // Add a named route to the route proxy, using a given style and arrow style
	    public void setRouteNewRoute(String name, String style, String arrowStyle);

	    // RASTER TILES
	    
	    // Enable or disable the downloading of MS Bing Aerial tiles
	    public void setRasterTileAerialEnabled(boolean enable);
	    
	    // Enable or disable the downloading of MS Bing Aerial tiles with labels
	    public void setRasterTileAerialWithLabelsEnabled(boolean enable);
	    
	    // Enable or disable the downloading of MS Bing Road tiles
	    public void setRasterTileRoadEnabled(boolean enable);
	    
	    // RESOURCES
	    
	    // set the current working directory for resources
	    public void setResourcePath(String resourcePath);
	    
	    // Set the vector map server for http url generation
	    public void setVectorDataBaseURL(String baseurl);
	    
        public void setTrafficDataBaseURL(String baseurl);
        
        public void setLandmarkDataBaseURL(String baseURL); 
	    // GENERAL
	    // Generic "Pump" that allows the TnMapClientSupport object to perform periodic tasks
	    public void pump();
	    
	    public void setRequestResult(int requestResult);
	    
	    // ADI SERVICE
	    public static final int ONROAD_STATE_INVALID_THRESHOLD = -1;
	    public static final int ONROAD_STATE_NO = 0;
	    public static final int ONROAD_STATE_YES = 1;
	    public static final int ONROAD_STATE_DONT_KNOW = 2;
	    
	    // Returns OnRoadState_No, OnRoadState_Yes, or OnRoadState_DontKnow depending on whether point is on a road
	    public int isOnRoad(double latitude, double longitude, int[] threshold);
	    
//	    // The C++ object id
//	    private long nativeId;
//	    
//	    // Constructor (will only be called by C++)
//	    private TnMapClientSupport(long nativeId) {
//	        this.nativeId = nativeId;
//	    }

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LocationManager.java
 *
 */
package com.telenav.location;

import java.util.Hashtable;

import com.telenav.logger.Logger;

/**
 * This class provides access to the system location services.
 * <br />
 * Also you can add your special location provider by {@link #addProvider(String, TnLocationProvider)}.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public abstract class TnLocationManager
{
    /**
     * Name of the GPS location provider.
     */
    public final static String GPS_179_PROVIDER = "gps-179";

    /**
     * Name of the network location provider of native platform.
     */
    public final static String NETWORK_PROVIDER = "network";

    /**
     * Name of the network location provider of TN services. Such as Tn Cell Location, parlay-x request etc.
     * For tn cell location and parlay-x, you need implement your location provider.
     */
    public final static String TN_NETWORK_PROVIDER = "tn_network";

	 /**
     * A special location provider for receiving locations without actually initiating a location fix. This provider can be 
	 * used to passively receive location updates when other applications or services request them without actually requesting the locations yourself. 
     */
    public final static String PASSIVE_PROVIDER = "passive_p";

    /**
     * Name of the mviewer location provider.
     */
    public final static String MVIEWER_PROVIDER = "mviewer";
    
    /**
     * Name of the along-route location provider.
     */
    public final static String ALONGROUTE_PROVIDER = "along_route";
    
    /**
     * Name of GPS replay location provider.
     */
    public final static String REPLAY_PROVIDER = "replay";
    
    private static TnLocationManager locationManager;
    private static int initCount;
    
    private Hashtable providers;
    
    /**
     * Retrieve the instance of location manager.
     * 
     * @return {@link TnLocationManager}
     */
    public static TnLocationManager getInstance()
    {
        return locationManager;
    }

    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param locationMngr This manager is native manager of platforms. Such as {@link AndroidLocationManager} etc.
     */
    public synchronized static void init(TnLocationManager locationMngr)
    {
        if(initCount >= 1)
            return;
        
        locationManager = locationMngr;
        initCount++;
    }
    
    /**
     * Creates a mock location provider and adds it to the set of active providers.
     * 
     * @param provider the name of provider.
     * @param locationProvider The instance of provider.
     */
    public final void addProvider(String provider, TnLocationProvider locationProvider)
    {
        if(provider == null || provider.trim().length() == 0 || locationProvider == null)
        {
            throw new IllegalArgumentException("the provider's name or provider is null.");
        }
        
        if(providers == null)
        {
            providers = new Hashtable();
        }
        
        providers.put(provider, locationProvider);
    }
    
    /**
     * Returns the information associated with the location provider of the given name, or null if no provider exists by that name.
     * 
     * @param provider the name of provider.
     * @return {@link TnLocationProvider}
     */
    public final TnLocationProvider getProvider(String provider)
    {
        if(provider == null || provider.trim().length() == 0)
        {
            throw new IllegalArgumentException("the provider's name is empty.");
        }
        
        if(providers == null)
        {
            providers = new Hashtable();
        }
        else if(providers.containsKey(provider))
        {
            return (TnLocationProvider)providers.get(provider);
        }
        
        TnLocationProvider locationProvider = getProviderDelegate(provider);
        if(locationProvider != null)
        {
            providers.put(provider, locationProvider);
        }
        
        return locationProvider;
    }
 
    /**
     * Retrieve the {@link TnLocationProvider} according to the name.
     * <br />
     * Will be override by child managers.
     * 
     * @param provider the name of provider.
     * @return {@link TnLocationProvider}
     */
    protected abstract TnLocationProvider getProviderDelegate(String provider);
    
    /**
     * Returns the name of the provider that best meets the given criteria.
     * 
     * @param criteria the criteria of provider.
     * @return {@link TnLocationProvider}
     */
    protected abstract TnLocationProvider getProvider(TnCriteria criteria);
    
    /**
     * Returns the latest known location that the implementation has. This is the best estimate that the implementation has for the previously known location. 
     * <br />
     * Applications can use this method to obtain the last known location and check the timestamp and other fields to determine if this is recent enough and 
     * good enough for the application to use without needing to make a new request for the current location.
     * <br />
     * For the mock location provider, the timeout of last location is 30 minutes.
     * 
     * @param criteria the criteria of provider.
     * @return - {@link TnLocation}
     * 
     * @throws TnLocationException if the location couldn't be retrieved.
     */
    public final TnLocation getLastKnownLocation(TnCriteria criteria) throws TnLocationException
    {
        TnLocationProvider locationProvider = getProvider(criteria);

        return locationProvider == null ? null : locationProvider.getLastKnownLocation();
    }
    
    /**
     * Returns the latest known location that the implementation has. This is the best estimate that the implementation has for the previously known location. 
     * <br />
     * Applications can use this method to obtain the last known location and check the timestamp and other fields to determine if this is recent enough and 
     * good enough for the application to use without needing to make a new request for the current location.
     * <br />
     * For the mock location provider, the timeout of last location is 30 minutes.
     * 
     * @param provider the name of provider.
     * @return - {@link TnLocation}
     * 
     * @throws TnLocationException if the location couldn't be retrieved.
     */
    public final TnLocation getLastKnownLocation(String provider) throws TnLocationException
    {
        TnLocationProvider locationProvider = getProvider(provider);

        return locationProvider == null ? null : locationProvider.getLastKnownLocation();
    }
    
    /**
     * Retrieves a Location with the constraints given by the TnCriteria associated with this class. If no result could
     * be retrieved, a LocationException is thrown. If the location can't be determined within the timeout period
     * specified in the parameter, the method shall throw a LocationException.
     * 
     * @param criteria the criteria of provider.
     * @param timeout a timeout value in seconds.
     * @return TnLocation a TnLocation object
     * 
     * @throws TnLocationException if the location couldn't be retrieved or if the timeout period expired.
     */
    public final TnLocation getLocation(TnCriteria criteria, int timeout) throws TnLocationException
    {
        if (timeout <= 0)
        {
            throw new IllegalArgumentException("time out is <= 0");
        }

        TnLocationProvider locationProvider = getProvider(criteria);

        if (locationProvider == null)
        {
            throw new TnLocationException("can't retrieve location provider by the criteria: " + criteria);
        }

        return locationProvider.getLocation(timeout);
    }
    
    /**
     * Registers the current activity to be notified periodically by the named provider. Periodically, the supplied LocationListener will be called with the current Location or with status updates. 
     * 
     * @param criteria the criteria that the returned providers must match.
     * @param minTime <b>This is for android.</b>the minimum time interval for notifications, in milliseconds. This field is only used as a hint to conserve power, and actual time between location updates may be greater or lesser than this value.
     * @param minDistance <b>This is for android.</b>the minimum distance interval for notifications, in meters
     * @param timeout <b>This is for rim.</b>timeout value in seconds, must be greater than 0. if the value is -1, the default timeout for this provider is used. Also, if the interval is -1 to indicate the default, the value of this parameter has no effect and the default timeout for this provider is used. If the interval is 0, this parameter has no effect.
     * @param maxAge <b>This is for rim.</b>maximum age of the returned location in seconds, must be greater than 0 or equal to -1 to indicate that the default maximum age for this provider is used. Also, if the interval is -1 to indicate the default, the value of this parameter has no effect and the default maximum age for this provider is used. If the interval is 0, this parameter has no effect.
     * @param listener a {@link ITnLocationListener} whose {@link ITnLocationListener#onLocationChanged(TnLocationProvider, TnLocation)} method will be called for each location update
     * 
     * @return String the provider's name.
     */
    public final String requestLocationUpdates(TnCriteria criteria, long minTime, float minDistance, int timeout, int maxAge,
            ITnLocationListener listener)
    {
        TnLocationProvider locationProvider = getProvider(criteria);
        
        if(locationProvider != null)
        {
            locationProvider.requestLocationUpdates(minTime, minDistance, timeout, maxAge, listener);
            
            return locationProvider.getName();
        }
        
        return null;
    }
    
    /**
     * Registers the current activity to be notified periodically by the named provider. Periodically, the supplied LocationListener will be called with the current Location or with status updates. 
     * 
     * @param provider name of the location provider.
     * @param minTime <b>This is for android.</b>the minimum time interval for notifications, in milliseconds. This field is only used as a hint to conserve power, and actual time between location updates may be greater or lesser than this value.
     * @param minDistance <b>This is for android.</b>the minimum distance interval for notifications, in meters.
     * @param timeout <b>This is for rim.</b>timeout value in seconds, must be greater than 0. if the value is -1, the default timeout for this provider is used. Also, if the interval is -1 to indicate the default, the value of this parameter has no effect and the default timeout for this provider is used. If the interval is 0, this parameter has no effect.
     * @param maxAge <b>This is for rim.</b>maximum age of the returned location in seconds, must be greater than 0 or equal to -1 to indicate that the default maximum age for this provider is used. Also, if the interval is -1 to indicate the default, the value of this parameter has no effect and the default maximum age for this provider is used. If the interval is 0, this parameter has no effect.
     * @param listener a {@link ITnLocationListener} whose {@link ITnLocationListener#onLocationChanged(TnLocationProvider, TnLocation)} method will be called for each location update
     */
    public final void requestLocationUpdates(String provider, long minTime, float minDistance, int timeout, int maxAge,
            ITnLocationListener listener)
    {
        TnLocationProvider locationProvider = getProvider(provider);
        
        if(locationProvider != null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "requestLocationUpdates, provider="+provider
                    +", minTime="+minTime+", minDistance="+minDistance+", timeout="+timeout+", maxAge="+maxAge);
            
            locationProvider.requestLocationUpdates(minTime, minDistance, timeout, maxAge, listener);
        }
    }
    
    /**
     * Sends additional commands to a location provider. Can be used to support provider specific extensions to the Location Manager API
     * 
     * @param provider name of the location provider.
     * @param command name of the command to send to the provider.
     * @param extras optional arguments for the command (or null).
     * @return boolean
     */
    public final boolean sendExtraCommand(String provider, String command, Hashtable extras)
    {
        TnLocationProvider locationProvider = getProvider(provider);
        
        return locationProvider == null ? false : locationProvider.sendExtraCommand(command, extras);
    }
    
    /**
     * reset this provider, All pending synchronous location requests will be aborted.
     */
    public final void reset(String provider)
    {
        if(this.providers != null)
        {
            TnLocationProvider locationProvider = (TnLocationProvider)this.providers.remove(provider);
            if(locationProvider != null)
            {
                locationProvider.reset();
            }
        }
    }
    
    /**
     *  Determines whether a given GPS provider is available for use.
     *  
     * @param provider the name of provider.
     * @return true means available, otherwise false.
     */
    public abstract boolean isGpsProviderAvailable(String provider);
}

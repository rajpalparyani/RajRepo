/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnLocationProvider.java
 *
 */
package com.telenav.location;

import java.util.Hashtable;

/**
 * An abstract superclass for location providers.  A location provider
 * provides periodic reports on the geographical location of the
 * device.
 *
 * <p> Each provider has a set of criteria under which it may be used;
 * for example, some providers require GPS hardware and visibility to
 * a number of satellites; others require the use of the cellular
 * radio, or access to a specific carrier's network, or to the
 * internet.  They may also have different battery consumption
 * characteristics or monetary costs to the user.  The {@link
 * TnCriteria} class allows providers to be selected based on
 * user-specified criteria.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public abstract class TnLocationProvider
{
    /**
     * Availability status code: the location provider is permanently unavailable. 
     * Permanent unavailability means that the method is unavailable and the implementation is not able 
     * to expect that this situation would change in the near future. An example is when using a location
     *  method implemented in an external device and the external device is detached.
     */
    public static final int OUT_OF_SERVICE = 0;

    /**
     * Availability status code: the location provider is temporarily unavailable. 
     * Temporary unavailability means that the method is unavailable due to reasons that can be 
     * expected to possibly change in the future and the provider to become available. 
     * An example is not being able to receive the signal because the signal used by the location method 
     * is currently being obstructed, e.g. when deep inside a building for satellite based methods. 
     * However, a very short transient obstruction of the signal should not cause the provider to toggle 
     * quickly between TEMPORARILY_UNAVAILABLE and AVAILABLE.
     */
    public static final int TEMPORARILY_UNAVAILABLE = 1;

    /**
     * Availability status code: the location provider is available.
     */
    public static final int AVAILABLE = 2;

    protected String name;

    /**
     * Construct a provider with a special name, such as {@link TnLocationManager#GPS_179_PROVIDER},
     * {@link TnLocationManager#NETWORK_PROVIDER}, {@link TnLocationManager#TN_NETWORK_PROVIDER},
     * {@link TnLocationManager#MVIEWER_PROVIDER}, {@link TnLocationManager#PASSIVE_PROVIDER}.
     * 
     * @param name the name of provider.
     */
    public TnLocationProvider(String name)
    {
        this.name = name;
    }

    /**
     * Retrieve the name of this provider.
     * 
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns true if this provider meets the given criteria, false otherwise. 
     * 
     * @param criteria the criteria of provider.
     * @return boolean
     */
    protected boolean meetsCriteria(TnCriteria criteria)
    {
        return false;
    }

    /**
     * reset this provider, All pending synchronous location requests will be aborted.
     */
    protected abstract void reset();

    /**
     * Returns the latest known location that the implementation has. This is the best estimate that the implementation has for the previously known location. 
     * <br />
     * Applications can use this method to obtain the last known location and check the timestamp and other fields to determine if this is recent enough and 
     * good enough for the application to use without needing to make a new request for the current location.
     * 
     * @return - {@link TnLocation}
     * 
     * @throws TnLocationException if the location couldn't be retrieved.
     */
    protected abstract TnLocation getLastKnownLocation() throws TnLocationException;

    /**
     * Retrieves a Location associated with this class. If no result could be retrieved, a LocationException is thrown.
     * If the location can't be determined within the timeout period specified in the parameter, the method shall throw
     * a LocationException.
     * 
     * @param timeout a timeout value in seconds.
     * @return TnLocation a TnLocation object
     * 
     * @throws TnLocationException if the location couldn't be retrieved or if the timeout period expired.
     */
    protected abstract TnLocation getLocation(int timeout) throws TnLocationException;
    
    /**
     * Registers the current activity to be notified periodically by the named provider. Periodically, the supplied LocationListener will be called with the current Location or with status updates. 
     * 
     * @param minTime the minimum time interval for notifications, in milliseconds. This field is only used as a hint to conserve power, and actual time between location updates may be greater or lesser than this value.
     * @param minDistance the minimum distance interval for notifications, in meters
     * @param timeout timeout value in seconds, must be greater than 0. if the value is -1, the default timeout for this provider is used. Also, if the interval is -1 to indicate the default, the value of this parameter has no effect and the default timeout for this provider is used. If the interval is 0, this parameter has no effect.
     * @param maxAge maximum age of the returned location in seconds, must be greater than 0 or equal to -1 to indicate that the default maximum age for this provider is used. Also, if the interval is -1 to indicate the default, the value of this parameter has no effect and the default maximum age for this provider is used. If the interval is 0, this parameter has no effect.
     * @param listener
     */
    protected abstract void requestLocationUpdates(long minTime, float minDistance, int timeout, int maxAge, ITnLocationListener listener);
    
    /**
     * Sends additional commands to a location provider. Can be used to support provider specific extensions to the Location Manager API
     * 
     * @param command name of the command to send to the provider.
     * @param extras optional arguments for the command (or null).
     * @return boolean
     */
    protected boolean sendExtraCommand(String command, Hashtable extras)
    {
        return false;
    }
}

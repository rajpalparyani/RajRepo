/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnLocationListener.java
 *
 */
package com.telenav.location;

/**
 * Used for receiving notifications from the TnLocationManager when
 * the location has changed. These methods are called if the
 * LocationListener has been registered with the location manager service
 * using the {@link TnLocationManager#requestLocationUpdates(String, long, float, int, int, ITnLocationListener)}
 * method.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public interface ITnLocationListener
{

    /**
     * Called when the location has changed.
     *
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     *@param provider the location provider.
     * @param location The new location, as a Location object.
     */
    public void onLocationChanged(TnLocationProvider provider, TnLocation location);

    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     * update.
     * @param status {@link TnLocationProvider#OUT_OF_SERVICE} if the
     * provider is out of service, and this is not expected to change in the
     * near future; {@link TnLocationProvider#TEMPORARILY_UNAVAILABLE} if
     * the provider is temporarily unavailable but is expected to be available
     * shortly; and {@link TnLocationProvider#AVAILABLE} if the
     * provider is currently available.
     */
    public void onStatusChanged(TnLocationProvider provider, int status);
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LocationRequester.java
 *
 */
package com.telenav.carconnect.provider;

import com.telenav.app.TeleNavDelegate;
import com.telenav.carconnect.host.CarConnectHostManager;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavRunningStatusProvider;

/**
 * @author chihlh
 * @date Sep 18, 2012
 */

public class LocationRequester implements LocationListener
{

    private ILocationCallback lc;

    public LocationRequester(ILocationCallback lc)
    {
        this.lc = lc;
    }

    public void requestLocation()
    {
        LocationProvider lp = LocationProvider.getInstance();
        boolean strickFlag = false;
        if (!TeleNavDelegate.getInstance().isActivate() && !CarConnectHostManager.getInstance().isLocationAlwaysOn())
        {
            // location provider may be turned off, turn on it.
            Logger.log(Logger.INFO, this.getClass().getName(),
                "CarConnect: LocationRequester - Locatin provider was off, turned back on");
            lp.start();
            strickFlag = true;
        }
        t = System.currentTimeMillis();
        lp.getCurrentLocation(LocationProvider.GPS_VALID_TIME, LocationProvider.NETWORK_LOCATION_VALID_TIME, 30 * 1000, this,
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1, strickFlag);
    }

    long t;

    @Override
    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        TnLocation loc = null;
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            loc = locations[0];
            Logger.log(
                Logger.INFO,
                this.getClass().getName(),
                "CarConnect: LocationRequester - getCurrent location lat=" + loc.getLatitude() + ", lon=" + loc.getLongitude()
                        + ", accuracy=" + loc.getAccuracy() + ", loc delay = "
                        + (System.currentTimeMillis() - loc.getLocalTimeStamp()) + ", locationRequestTime = "
                        + (System.currentTimeMillis() - t));
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: LocationRequester - getCurrent location fail");
        }

        if (!TeleNavDelegate.getInstance().isActivate() && !CarConnectHostManager.getInstance().isLocationAlwaysOn())
        {
            // stop the Location service
            if (!NavRunningStatusProvider.getInstance().isNavRunning())
            {
                LocationProvider.getInstance().stop();
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "CarConnect: LocationRequester - app in background, turn location off");
            }
        }

        if (loc == null)
        {
            lc.onError();
        }
        else
        {
            lc.onSuccess(loc);
        }
    }
}

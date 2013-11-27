/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimLocationManager.java
 *
 */
package com.telenav.location.rim;

import javax.microedition.location.Criteria;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.gps.BlackBerryCriteria;
import net.rim.device.api.gps.GPSInfo;

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;
import com.telenav.logger.Logger;

/**
 * This class provides access to the system location services at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class RimLocationManager extends TnLocationManager
{
    /**
     * Construct the location manager at rim platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_LOCATION_API;#PERMISSION_LOCATION_DATA;
     * <br />
     * 
     */
    public RimLocationManager()
    {
    }
    
    protected TnLocationProvider getProvider(TnCriteria criteria)
    {
        Criteria nativeCriteria = RimLocationUtil.convert(criteria);

        TnLocationProvider locationProvider = null;

        if (criteria != null)
        {
            try
            {
                LocationProvider nativeProvider = LocationProvider.getInstance(nativeCriteria);

                locationProvider = new RimLocationProvider(GPS_179_PROVIDER, nativeProvider);
            }
            catch (LocationException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }

        return locationProvider;
    }

    protected TnLocationProvider getProviderDelegate(String provider)
    {
        TnLocationProvider locationProvider = null;

        Criteria criteria = null;

        if (GPS_179_PROVIDER.equals(provider))
        {
            criteria = new Criteria();
        }
        else if (NETWORK_PROVIDER.equals(provider))
        {
            criteria = new BlackBerryCriteria();
            ((BlackBerryCriteria)criteria).setMode(GPSInfo.GPS_MODE_CELLSITE);
        }
        else if (MVIEWER_PROVIDER.equals(provider))
        {
            locationProvider = new RimMviewerLocationProvider(MVIEWER_PROVIDER);
        }

        if (criteria != null)
        {
            try
            {
                LocationProvider nativeProvider = LocationProvider.getInstance(criteria);

                locationProvider = new RimLocationProvider(provider, nativeProvider);
            }
            catch (LocationException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }

        return locationProvider;
    }

    public boolean isGpsProviderAvailable(String provider)
    {
        if (GPS_179_PROVIDER.equals(provider))
        {
            return GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_ASSIST) || GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_AUTONOMOUS)
                    || GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CDMA_MS_BASED) || GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CDMA_MS_ASSIST)
                    || GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CDMA_SPEED_OPTIMAL) || GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CDMA_ACCURACY_OPTIMAL)
                    || GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CDMA_DATA_OPTIMAL) || GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_BT);
        }
        else if (NETWORK_PROVIDER.equals(provider))
        {
            return GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CELLSITE);
        }
        
        return true;
    }
}

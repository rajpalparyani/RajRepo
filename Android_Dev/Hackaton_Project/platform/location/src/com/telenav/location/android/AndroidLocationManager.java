/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidLocationManager.java
 *
 */
package com.telenav.location.android;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

/**
 * This class provides access to the system location services at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public class AndroidLocationManager extends TnLocationManager
{
    protected Context context;

    /**
     * Construct the location manager at android platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
     * 
     * @param context
     */
    public AndroidLocationManager(Context context)
    {
        this.context = context;
    }

    /**
     * Retrieve the {@link TnLocationProvider} according to the name.
     * 
     * @param provider
     * @return {@link TnLocationProvider}
     */
    protected TnLocationProvider getProviderDelegate(String provider)
    {
        String nativeProvider = AndroidLocationUtil.convertNativeProvider(provider);

        TnLocationProvider locationProvider = null;

        if (nativeProvider != null)
        {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationProvider nativeLocationProvider = lm.getProvider(nativeProvider);
            locationProvider = new AndroidLocationProvider(provider, lm, nativeLocationProvider);
        }

        if(locationProvider == null)
        {
            if(MVIEWER_PROVIDER.equals(provider))
            {
                locationProvider = new AndroidMviewerLocationProvider(MVIEWER_PROVIDER);
            }
        }
        
        return locationProvider;
    }

    /**
     * Returns the name of the provider that best meets the given criteria.
     * 
     * @param criteria
     * @return {@link TnLocationProvider}
     */
    protected TnLocationProvider getProvider(TnCriteria criteria)
    {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria nativeCriteria = AndroidLocationUtil.convert(criteria);
        String nativeProvider = lm.getBestProvider(nativeCriteria, true);

        return super.getProvider(AndroidLocationUtil.convertProvider(nativeProvider));
    }
    
    public boolean isGpsProviderAvailable(String provider)
    {
        String nativeProvider = AndroidLocationUtil.convertNativeProvider(provider);
        if (nativeProvider != null)
        {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationProvider nativeLocationProvider = lm.getProvider(nativeProvider);
            return lm.isProviderEnabled(nativeProvider) && nativeLocationProvider != null;
        }
        
        return false;
    }
}

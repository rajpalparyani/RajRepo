/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seLocationManager.java
 *
 */
package com.telenav.location.j2se;

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

/**
 * This class provides access to the system location services at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
public class J2seLocationManager extends TnLocationManager
{
    /**
     * Construct the location manager at j2se platform.
     */
    public J2seLocationManager()
    {
        
    }
    
    protected TnLocationProvider getProvider(TnCriteria criteria)
    {
        //TODO
        
        return null;
    }

    protected TnLocationProvider getProviderDelegate(String provider)
    {
        TnLocationProvider locationProvider = null;

        if (GPS_179_PROVIDER.equals(provider))
        {
            //TODO
        }
        else if (NETWORK_PROVIDER.equals(provider))
        {
            //TODO
        }
        else if (MVIEWER_PROVIDER.equals(provider))
        {
            locationProvider = new J2seMviewerLocationProvider(MVIEWER_PROVIDER);
        }

        return locationProvider;
    }

    public boolean isGpsProviderAvailable(String provider)
    {
        return true;
    }

}

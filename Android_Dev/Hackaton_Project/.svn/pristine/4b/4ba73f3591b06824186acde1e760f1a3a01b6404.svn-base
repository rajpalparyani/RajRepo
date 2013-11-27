/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MockLocationProvider.java
 *
 */
package com.telenav.module.location;

import com.telenav.gps.AlongRouteLocationProvider;
import com.telenav.gps.GpsService;
import com.telenav.gps.IGpsListener;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

/**
 *@author yren (yren@telenav.cn)
 *@date 2012-2-1
 */
public class MockLocationProvider implements ITnLocationProvider
{
    protected GpsService gpsService;
    
    TnLocation defaultLocation;
    
    public MockLocationProvider(TnLocation defaultLocation , GpsService gpsService)
    {
        this.defaultLocation = defaultLocation;
        this.gpsService = gpsService;
    }
    
    public void setGpsService(GpsService gpsService)
    {
        this.gpsService = gpsService;
    }
    
    public String getGpsServiceType()
    {
        return this.gpsService.getSourceType();
    }

    public int getGpsServiceStatus()
    {        
        return this.gpsService.getGpsStatus();
    }

    public TnLocation getLastKnownLocation(String provider)
    {        
        return null;
    }

    public int getGpsFixes(int numFixes, TnLocation[] locations)
    {
        return this.gpsService.getFixes(numFixes, locations);
    }

    public int getNetworkFixes(int numFixes, TnLocation[] locations)
    {
        return 0;
    }

    public void start()
    {
        if (gpsService != null)
        {
            gpsService.start();
            if (gpsService.getSourceType() == TnLocationManager.ALONGROUTE_PROVIDER)
            {
                TnLocationProvider provider = TnLocationManager.getInstance().getProvider(TnLocationManager.ALONGROUTE_PROVIDER);
                ((AlongRouteLocationProvider) provider).setOrigin(this.defaultLocation.getLatitude(), this.defaultLocation.getLongitude());
            }
        }
    }

    public void stop()
    {
        if (gpsService != null)
        {
            gpsService.stop();
        }
        
        storeLastKnownLocation();
    }
    
    protected void storeLastKnownLocation()
    { 
        LocationCacheManager.getInstance().save();
    }

    public void setListener(IGpsListener gpsListener)
    {
        
    }

    public void clearListener()
    {
        
    }

    public String convertToNativeProvider(int type)
    {
        return "";
    }

    public void setLocationParams(long interval, long fasterInterval, int priority, boolean immediately)
    {
        // TODO Auto-generated method stub
        
    }
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LocationModel.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.location;

import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;
import com.telenav.navsdk.events.LocationData;
import com.telenav.navsdk.events.LocationEvents;

/**
 *@author xiangli
 *@date 2012-3-12
 */
public class LocationUpdate extends Thread
{
//    private static TnLocation currentLocation = null;

    private static boolean isappExit = false;
    
    private boolean isLocationRun = false;
    
    private boolean stopUpdate   = false;
    
    private static LocationUpdate thread = new LocationUpdate();
    
    public static final double LAT_AND_LON_CONVERT_RATE=100000d;
    
    private LocationUpdate() {
        
    }
    
    public static LocationUpdate getLocationInstance()
    {
        return thread;
    }
    public void run()
    {
        isLocationRun = true;
        
        while (true)
        {
            if (isappExit)
            {
                MyLog.setLog("Telenav", "exit locationupdate thread");
                return;
            }
            
            synchronized (this)
            {
                if (stopUpdate)
                {
                    try
                    {
                        synchronized (LocationUpdate.class)
                        {
                            LocationUpdate.class.wait();
                        }
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        MyLog.setLog("LocationUpdateThread",e.getMessage());
                    }
                }
                
                try
                {
                    wait(1000);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (null == LocationProvider.getInstance())
                    continue;

                TnLocation loc = LocationProvider.getInstance().getCurrentLocation(
                    LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
                
                if (null == loc)
                {
                    MyLog.setLog("LocationUpdateThread", "current location null");
                    loc = LocationProvider.getInstance().getLastKnownLocation(
                        LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
                    if (null == loc)
                    {
                        MyLog.setLog("LocationUpdateThread", "last location is null");
                        continue;
                    }
                    
                }
                
                MyLog.setLog("LocationUpdateThread", "lat: " + loc.getLatitude() + " lon: " + loc.getLongitude());

                if (stopUpdate)
                    continue;
                
                if(CarConnectManager.isConnectedToCar())
                {
                    MyLog.setLog("LocationUpdateThread", "send location update to HU!");
                    LocationEvents.OnLocationUpdated response = constructLocationUpdate(loc.getLatitude(), loc.getLongitude(), loc.getHeading(), loc.getAltitude(), loc.getLocalTimeStamp());
                    CarConnectManager.getEventBus().broadcast("OnLocationUpdated", response);
                }
            }
        }
    }
    
    private LocationEvents.OnLocationUpdated constructLocationUpdate(int lat, int lon, int heading, float altitude, long timeStamp)
    {
        LocationEvents.OnLocationUpdated.Builder builder = LocationEvents.OnLocationUpdated.newBuilder();
        LocationData.Position.Builder positionBuilder = LocationData.Position.newBuilder();
        positionBuilder.setLatitude(((double)lat)/LAT_AND_LON_CONVERT_RATE);
        positionBuilder.setLongitude(((double)lon)/LAT_AND_LON_CONVERT_RATE);
        positionBuilder.setHeading(heading);
        positionBuilder.setAltitude(altitude);
        positionBuilder.setTimestamp(timeStamp);
        
        builder.setLocation(positionBuilder);
        return builder.build();
    }
    
    public boolean isLocationUpdatRun()
    {
        return isLocationRun;
    }
    
    public void stopUpdate ()
    {
        stopUpdate = true;
    }
    
    public void startUpdate ()
    {
        if(isAlive())
        {
            stopUpdate = false;
            synchronized (LocationUpdate.class)
            {
                LocationUpdate.class.notify();
            }
        }else{
            start();
        }
        
    }
}

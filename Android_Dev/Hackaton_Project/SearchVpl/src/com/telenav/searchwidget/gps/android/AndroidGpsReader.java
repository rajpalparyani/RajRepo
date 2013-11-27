/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidGpsReader.java
 *
 */
package com.telenav.searchwidget.gps.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.telenav.location.TnLocation;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Aug 22, 2011
 */

class AndroidGpsReader
{
    private final static int    GPS_BUFFER_SIZE     = 5;
    private final static int    MIN_GPS_INTERVAL    = 60000;
    
    private static AndroidGpsReader instance;    
    
    private LocationManager locationManager;
    
    private boolean isStarted;
    
    private AndroidGpsListener gpsListener;
    private AndroidGpsListener networkListener;
    
    private TnLocation[] positionData;
    private int latestFixIndex;
    
    public synchronized static AndroidGpsReader getInstance()
    {
        if (instance == null)
        {
            instance = new AndroidGpsReader();
        }
        
        return instance;
    }
    
    protected void init(Context context)
    {
        try
        {
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            
            positionData = new TnLocation[GPS_BUFFER_SIZE];
            
        }
        catch (Throwable ex)
        {
        }
    }

    public void start()
    {
        if (!isStarted)
        {
            if (locationManager == null) return;
            
            synchronized (this)
            {
                isStarted = true;
                
                // start GPS listener
                if (gpsListener == null)
                    gpsListener = new AndroidGpsListener();
                
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_GPS_INTERVAL, 0, 
                                                       gpsListener, Looper.getMainLooper());
              
                // start network listener
                if (networkListener == null) 
                    networkListener = new AndroidGpsListener();
                
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_GPS_INTERVAL, 0, 
                                                       networkListener, Looper.getMainLooper());
            }
        }                
    }
    
    public void stop()
    {
        if (isStarted)
        {
            if (locationManager == null) return;
            
            synchronized (this)
            {
                if (gpsListener != null)
                {
                    locationManager.removeUpdates(gpsListener);
                }
    
                if (networkListener != null)
                {
                    locationManager.removeUpdates(networkListener);
                }
                
                isStarted = false;
            }
        }

    }
    
    protected void addGpsData(TnLocation data)
    {
        synchronized(this)
        {
            positionData[latestFixIndex] = data;
            latestFixIndex ++;
            if (latestFixIndex >= positionData.length)
                latestFixIndex = 0;
        }            
        
        AndroidGpsProvider.getInstance().setGpsDataArrived(data);
    }

    
    class AndroidGpsListener implements LocationListener
    {
        AndroidGpsListener()
        {
        }
        
        public void onLocationChanged(Location location)
        {
            TnLocation tnLoc = AndroidLocationUtil.convert(location);
            addGpsData(tnLoc);
        }
        
        public void onProviderDisabled(String provider)
        {
            //Fix 53172: if device is on airplane mode, when come back to application, it will call
            //this function and if we call stop() here, application can not read gps anymore.
//            stop(); 
        }

        public void onProviderEnabled(String provider)
        {
//            if (provider.equals(LocationManager.GPS_PROVIDER))
//            {
//                start(); 
//            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }
        
    }    
}




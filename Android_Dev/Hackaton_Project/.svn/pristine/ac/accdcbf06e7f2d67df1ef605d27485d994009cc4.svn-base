/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidGpsProvider.java
 *
 */
package com.telenav.searchwidget.gps.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.telenav.datatypes.DataUtil;
import com.telenav.gps.IGpsListener;
import com.telenav.location.TnLocation;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Aug 22, 2011
 */

public class AndroidGpsProvider
{
	public static final int GPS_VALID_RANGE = 1000;
    private final static int GPS_TIMEOUT        = 300000;
    
    private static AndroidGpsProvider instance;
    
    private LocationManager locationManager;
    
    private boolean isRunning;
    private boolean isCancelled;
    
    private IGpsListener gpsListener;
    private boolean isGpsOutRange = false;
    
    public synchronized static AndroidGpsProvider getInstance()
    {
        if (instance == null)
        {
            instance = new AndroidGpsProvider();
        }
        
        return instance;
    }
    
    public void init(Context context)
    {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        AndroidGpsReader.getInstance().init(context);
    }
    
    public synchronized boolean getCurrentLocation(IGpsListener gpsListener)
    {
        if (isRunning) return false;
        
        this.gpsListener = gpsListener;
        isCancelled = false;
        isRunning = true;
        isGpsOutRange = false;
        AndroidGpsReader.getInstance().start();
        
        new GpsTask().start();
        
        return true;
    }
    
    public TnLocation getLastKnownLocation()
    {
        TnLocation loc = LocationDao.getInstance().getSearchWidgetLastKnownLocation();
        if (loc != null) return loc;
        
        loc = LocationDao.getInstance().getTnLastGpsLocation();
        if (loc != null) return loc;
        
        loc = LocationDao.getInstance().geTnLastCellLocation();
        if (loc != null) return loc;
        
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        loc = AndroidLocationUtil.convert(location);
        if (loc != null) return loc;
        
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        loc = AndroidLocationUtil.convert(location);
        if (loc != null) return loc;
        
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        loc = AndroidLocationUtil.convert(location);
        return loc;
    }
    
    
    public boolean isGpsOutRange() 
    {
		return isGpsOutRange;
	}

	//package privilege  
    synchronized void setGpsDataArrived(TnLocation location)
    {
    	TnLocation lastLocation = getLastKnownLocation();
    	if (null != lastLocation && null != location)
    	{
    		 long distMeter = DataUtil.gpsDistance(location.getLatitude() - lastLocation.getLatitude(), location.getLongitude() - lastLocation.getLongitude(),
    		            DataUtil.getCosLat(location.getLatitude()));
    		 isGpsOutRange = distMeter > GPS_VALID_RANGE;
    	}
        LocationDao.getInstance().setSearchWidgetLastKnownLocation(location);
        if (!isCancelled)
        {
            if (gpsListener != null)
            {
                gpsListener.gpsDataArrived(location);
            }
        }
        AndroidGpsReader.getInstance().stop();
        
        isCancelled = true;        
        this.notify();
    }
    
    
    class GpsTask extends Thread
    {
        public void run()
        {
            try
            {
                synchronized (AndroidGpsProvider.this)
                {
                    AndroidGpsProvider.this.wait(GPS_TIMEOUT);
                }
            }
            catch (Exception e)
            {}
            
            synchronized (AndroidGpsProvider.this)
            {
                if (!isCancelled)
                {
                    if (gpsListener != null)
                    {
                        gpsListener.gpsDataArrived(null);
                    }                    
                }
                AndroidGpsReader.getInstance().stop();
                isCancelled = true;
                isRunning = false;
            }
        }
    }

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidLocationProvider.java
 *
 */
package com.telenav.location.android;

import java.util.Enumeration;
import java.util.Hashtable;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;
import com.telenav.location.TnLocationProvider;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
class AndroidLocationProvider extends TnLocationProvider implements LocationListener
{
    protected LocationManager locationManager;

    protected LocationProvider locationProvider;

    protected ITnLocationListener listener;

    protected int satellites;
    
    protected Looper looper;
    
    public AndroidLocationProvider(String name, LocationManager locationManager, LocationProvider locationProvider)
    {
        super(name);

        this.locationManager = locationManager;
        this.locationProvider = locationProvider;
    }

    protected TnLocation getLastKnownLocation() throws TnLocationException
    {
        String nativeName = AndroidLocationUtil.convertNativeProvider(this.name);

        Location location = this.locationManager.getLastKnownLocation(nativeName);
        if (location != null)
        {
            TnLocation tnLocation = AndroidLocationUtil.convert(location);
            tnLocation.setSatellites(getSatellites(location));
        
            return tnLocation;
        }
        else
        {
            return null;
        }
    }
    
    protected TnLocation getLocation(int timeout) throws TnLocationException
    {
        return getLastKnownLocation();
    }

    protected boolean meetsCriteria(TnCriteria criteria)
    {
        Criteria nativeCriteria = AndroidLocationUtil.convert(criteria);

        return this.locationProvider.meetsCriteria(nativeCriteria);
    }

    protected void reset()
    {
        this.locationManager.removeUpdates(this);

        if (this.looper != null)
        {
            this.looper.quit();
            this.looper = null;
        }

        this.locationProvider = null;
        this.locationManager = null;
        this.listener = null;
    }

    protected boolean sendExtraCommand(String command, Hashtable extras)
    {
        Bundle extrasBundle = new Bundle();
        if(extras != null)
        {
            Enumeration keys = extras.keys();
            while(keys.hasMoreElements())
            {
                String key = keys.nextElement().toString();
                Object value = (Object)extras.get(key);
                if(value instanceof Integer)
                {
                    extrasBundle.putInt(key, ((Integer)value).intValue());
                }
                else if(value instanceof Boolean)
                {
                    extrasBundle.putBoolean(key, ((Boolean)value).booleanValue());
                }
                else
                {
                    extrasBundle.putString(key, extras.get(key).toString());
                }
            }
            
        }
        return this.locationManager.sendExtraCommand(this.name, command, extrasBundle);
    }
    
    protected void requestLocationUpdates(final long minTime, final float minDistance, final int timeout, final int maxAge, ITnLocationListener listener)
    {
        if (this.looper != null)
        {
            this.locationManager.removeUpdates(this);
            
            this.looper.quit();
            this.looper = null;
        }
        
        this.listener = listener;
        
//        Thread locationThread = new Thread("Location-Thread@PUSU")
//        {
//            public void run()
//            {
//                Looper.prepare();
//                
//                looper = Looper.myLooper();
                
//        Logger.log(Logger.INFO, AndroidLocationProvider.class.getName(), "Location Thread's Looper: " + looper);

        if (locationManager != null)
        {
            try
            {
                locationManager.requestLocationUpdates(AndroidLocationUtil.convertNativeProvider(name), minTime, minDistance,
                    this, Looper.getMainLooper());
            }
            catch (Exception e)
            {
                Logger.log(Logger.EXCEPTION, AndroidLocationProvider.class.getName(), "error on requestLocationUpdates!!!");
            }
        }
//                
//                Looper.loop();
//            }
//        };
//        locationThread.start();
    }

    public void onLocationChanged(Location location)
    {
        if (listener != null && location != null)
        {
            TnLocation tnLocation = AndroidLocationUtil.convert(location);
            
            tnLocation.setSatellites(getSatellites(location));
            
            listener.onLocationChanged(this, tnLocation);
            
            Logger.log(Logger.INFO, 
                this.getClass().getName(), 
                tnLocation.getLocalTimeStamp() + "," + tnLocation.getTime()      + "," +
                tnLocation.getLatitude()       + "," + tnLocation.getLongitude() + "," +
                tnLocation.getSpeed()          +  "," + tnLocation.getHeading()  + "," +
                tnLocation.getAccuracy()       + "," + tnLocation.getSatellites(), new Object[]{TnLocation.LOG_GPS_LOCATION, tnLocation});
            
//            Logger.log(Logger.INFO, 
//                       TnLocation.LOG_GPS_LOCATION, 
//                       tnLocation.getLocalTimeStamp() + "," + tnLocation.getTime()      + "," +
//                       tnLocation.getLatitude()       + "," + tnLocation.getLongitude() + "," +
//                       tnLocation.getSpeed()          +  "," + tnLocation.getHeading()  + "," +
//                       tnLocation.getAccuracy()       + "," + tnLocation.getSatellites());
        } 
    }

    public void onProviderDisabled(String provider)
    {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider)
    {
        // TODO Auto-generated method stub

    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        if(this.listener != null)
        {
            this.listener.onStatusChanged(this, AndroidLocationUtil.convertStatus(status));
        }
        
        if(extras != null)
        {
            satellites = extras.getInt("satellites");
        }
    }

    private int getSatellites(Location location)
    {
        if(this.satellites <= 0)
        {
            if (location.getExtras() != null)
            {
                this.satellites = location.getExtras().getInt("satellites");

                if (this.satellites <= 0)
                {
                    this.satellites = location.getExtras().getInt("NumSatellite");
                }
            }
        }
        
        if (this.satellites <= 0)
        {
            int err = (int) location.getAccuracy() * 7358 >> 13;
            if (err > 45)
            {
                this.satellites = 0;
            }
            else if (err > 35)
            {
                this.satellites = 1;
            }
            else if (err > 25)
            {
                this.satellites = 2;
            }
            else if (err > 15)
            {
                this.satellites = 3;
            }
            else if (err > 5)
            {
                this.satellites = 4;
            }
            else
            {
                this.satellites = 5;
            }
        }
        
        return this.satellites;
    }
}

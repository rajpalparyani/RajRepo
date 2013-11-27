/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * GoogleLocationProvider.java
 *
 */
package com.telenav.module.location.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.telenav.app.AbstractPlatformIniter;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.AndroidPlatformIniter;
import com.telenav.gps.IGpsListener;
import com.telenav.gps.TnGpsFilter;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.module.location.ITnLocationProvider;

/**
 * @author yren
 * @date 2013-7-15
 */
public class GoogleServiceLocationProvider implements com.google.android.gms.location.LocationListener, ITnLocationProvider,
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{

    protected IGpsListener gpsListener = null;
    
    protected TnLocation[] gpsPositionData;

    protected TnLocation[] networkPositionData;

    private final static byte DEFAULT_GPS_BUFFER_SIZE = 5;

    protected int latestGpsFixIndex;

    protected int latestNetworkFixIndex;

    private TnGpsFilter gpsFilter;
    
    private LocationRequest mLocationRequest;
    
    private int status = -1;
    
    public long gpsTimeOffset = 0;
    
    public long networkTimeOffset = 0;
    
    private Object dataMutex = new Object();
    
    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
    
    private boolean isRequestingGps;
    
    private long lastGpsFixTime;
    
    private static final int veryStrongGpsSignal = 20;
    
    private static final int strongGpsSignal = 50;
    
    private static final int nomalGpsSignal = 100;
    
    private static final int weakGpsSignal = 150;
    
    private static final int veryWeakGpsSignal = 200;
    
    private static Context context = AndroidPersistentContext.getInstance().getContext();
    
    private RequestMonitorThread requestRefreshMonitor = new RequestMonitorThread();
    
    Object monitorThreadMutex = new Object();
    LocationRequestParam param = new LocationRequestParam(-1, -1, -1);
    
    public GoogleServiceLocationProvider()
    {
        initLocationRequester();
        initBuffer();
        gpsFilter = new TnGpsFilter();
    }
    
    private void initLocationRequester()
    {
        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(1000);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
//        mLocationRequest.setFastestInterval(1000);

        mLocationClient = new LocationClient(context, this, this);
    }

    private void initBuffer()
    {
        int size = DEFAULT_GPS_BUFFER_SIZE;
        this.gpsPositionData = new TnLocation[size];
        this.networkPositionData = new TnLocation[size];
        for (int i = 0; i < size; i++)
        {
            this.gpsPositionData[i] = new TnLocation("");
            this.networkPositionData[i] = new TnLocation("");
        }
        this.latestGpsFixIndex = 0;
        this.latestNetworkFixIndex = 0;
    }

    public String getGpsServiceType()
    {
        return TnLocationManager.GPS_179_PROVIDER;
    }

    public int getGpsServiceStatus()
    {
        return this.status;
    }

    public TnLocation getLastKnownLocation(String provider)
    {
        TnLocation lastLocation = null;
        //for most device, should get the fix in this API.
        if (isAvailable())
        {
            if (mLocationClient.isConnected())
            {
                lastLocation = convert(mLocationClient.getLastLocation());
                if (lastLocation != null)
                {
                    long offset;
                    if (LocationManager.GPS_PROVIDER.equals(provider))
                    {
                        offset = gpsTimeOffset;
                    }
                    else
                    {
                        offset = networkTimeOffset;
                    }
                    // last known location was not received by chipset now, so the local time stamp should not be
                    // System.currentTimeMillis()
                    // set it to be same as GPS time stamp
                    lastLocation.setLocalTimeStamp(lastLocation.getTime() * 10 + offset);
                }
            }
            else
            {
                mLocationClient.connect();
                lastLocation = null;
            }
        }
        else
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.WARNING, this.getClass().getName(), "Google Service is not available, revert to raw gps!!!");
            }
            ((AndroidPlatformIniter) (AbstractPlatformIniter.getInstance())).resetLocationProviderByRawGps();
        }
        return lastLocation;
    }

    /**
     * Get specific number of gps fixes which are cached in pool
     * 
     * @param numFixes
     * @param locations
     * @return number
     */
    public int getGpsFixes(int numFixes, TnLocation [] locations)
    {
        return getFixes(numFixes, locations, dataMutex, this.latestGpsFixIndex, this.gpsPositionData);
    }
    
    /**
     * Get specific number of gps fixes which are cached in pool
     * 
     * @param numFixes
     * @param locations
     * @return number
     */
    public int getNetworkFixes(int numFixes, TnLocation [] locations)
    {
        return getFixes(numFixes, locations, dataMutex, this.latestNetworkFixIndex, this.networkPositionData);
    }
    
    private int getFixes(int numFixes, TnLocation [] locations, 
            Object dataMutex, int latestFixIndex, TnLocation[] positionData)
    {
        int fixCounter = 0;
        int buffSize = DEFAULT_GPS_BUFFER_SIZE;
        
        synchronized (dataMutex)
        {
            for (int i = latestFixIndex - 1; i >= 0; i--)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    locations[fixCounter].set(pos);
                    fixCounter++;
                    if (fixCounter >= numFixes || fixCounter >= locations.length)
                        return fixCounter;
                }
                else
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "pos is not valid. i = "+i);
                }
            }

            for (int i = buffSize - 1; i >= latestFixIndex; i--)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    locations[fixCounter].set(pos);
                    fixCounter++;
                    if (fixCounter >= numFixes || fixCounter >= locations.length)
                        return fixCounter;
                }
                else
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "pos is not valid, i = "+i);
                }
            }
        }
        
        return fixCounter; 
    }
    

    public void start()
    {
        this.lastGpsFixTime = System.currentTimeMillis();
        if (!isRequestingGps)
        {
            if (!mLocationClient.isConnected())
            {
                mLocationClient.connect();
            }
            else
            {
                mLocationClient.requestLocationUpdates(mLocationRequest, this);
            }
            this.isRequestingGps = true;
        }
    }
    
    public void stop()
    {
        if (isRequestingGps && mLocationClient.isConnected())
        {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient.disconnect();
            this.isRequestingGps = false;
        }
    }
    
    public void setLocationInterval(long interval)
    {
        this.stop();
        this.mLocationRequest.setInterval(interval);
        this.start();
    }

    public void setLocationFasterInterval(long interval)
    {
        this.stop();
        this.mLocationRequest.setFastestInterval(interval);
        this.start();
    }

    public void setLocationPriority(int priority)
    {
        this.stop();
        this.mLocationRequest.setPriority(priority);
        this.start();
    }
    
    public void setListener(IGpsListener gpsListener)
    {
        this.gpsListener = gpsListener;
    }
    
    public void clearListener()
    {
        this.gpsListener = null;
    }

    public String convertToNativeProvider(int type)
    {
        String provider = "";
        
        if((type & com.telenav.module.location.LocationProvider.TYPE_GPS) != 0)
        {
            provider = LocationManager.GPS_PROVIDER;
        }
        else if((type & com.telenav.module.location.LocationProvider.TYPE_NETWORK) != 0)
        {
            provider = LocationManager.NETWORK_PROVIDER;
        }
        else
        {
            provider = LocationManager.GPS_PROVIDER;
        }
        
        return provider;
    }

    public void onLocationChanged(Location location)
    {
        if (location != null)
        {
            TnLocation tnLocation = convert(location);
            tnLocation.setSatellites(getSatellites(location));
            synchronized (dataMutex)
            {
                if (TnLocationManager.GPS_179_PROVIDER.equals(tnLocation.getProvider()))
                {
                    if (gpsFilter.eliminateGpsNoise(tnLocation))
                    {
                        addGpsData(tnLocation);
                    }
                    else
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "eliminateGpsNoise ------------- lat: "
                                    + tnLocation.getLatitude() + ", lon: " + tnLocation.getLongitude() + ", heading: "
                                    + tnLocation.getHeading() + ", time: " + tnLocation.getTime() + ", localTime: "
                                    + tnLocation.getLocalTimeStamp());
                        }
                    }
                }
                if (TnLocationManager.NETWORK_PROVIDER.equals(location.getProvider()))
                {
                    addNetworkData(tnLocation);
                }
            }

            notifyGpsArrived(tnLocation);
        }
    }
    
    protected void notifyGpsArrived(final TnLocation tnLocation) 
    {
        if (gpsListener != null)
        {
            Thread t = new Thread(new Runnable() 
            {
                public void run() 
                {
                    try
                    {
                        gpsListener.gpsDataArrived(tnLocation);
                    }
                    catch (Throwable t)
                    {
                        t.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
    
    protected void addGpsData(TnLocation tnLocation)
    {
        synchronized (dataMutex)
        {
            lastGpsFixTime = System.currentTimeMillis();
            
            gpsPositionData[latestGpsFixIndex].set(tnLocation);
            latestGpsFixIndex++;
            if (latestGpsFixIndex >= gpsPositionData.length)
            {
                latestGpsFixIndex = 0;
            }
    
            gpsTimeOffset = tnLocation.getLocalTimeStamp() - tnLocation.getTime() * 10;
            
            if (Logger.DEBUG)
            {
                Logger.log(
                    Logger.INFO,
                    this.getClass().getName(),
                    "DB-Test GoogleServiceLocation addGpsData --- index: " + latestGpsFixIndex + " , lat: "
                            + tnLocation.getLatitude() + ", lon: "
                            + tnLocation.getLongitude() + ", heading: "
                            + tnLocation.getHeading() + ", time: " + tnLocation.getTime()
                            + ", localTime: " + tnLocation.getLocalTimeStamp()
                            + ", speed: " + tnLocation.getSpeed());
            }
        }
    }
    
    protected void addNetworkData(TnLocation tnLocation)
    {
        synchronized (dataMutex)
        {
            networkPositionData[latestNetworkFixIndex].set(tnLocation);
            latestNetworkFixIndex++;
            if (latestNetworkFixIndex >= networkPositionData.length)
            {
                latestNetworkFixIndex = 0;
            }
            
            networkTimeOffset = tnLocation.getLocalTimeStamp() - tnLocation.getTime() * 10;
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "DB-Test GoogleServiceLocation addNetworkData --- index : " + latestNetworkFixIndex
                            + " , lat : " + tnLocation.getLatitude() + " , lon : "
                            + tnLocation.getLongitude());
            }
        }
    }
    
    private int getSatellites(Location location)
    {
        int satellites = 0;
        if (location.getExtras() != null)
        {
            satellites = location.getExtras().getInt("satellites");

            if (satellites <= 0)
            {
                satellites = location.getExtras().getInt("NumSatellite");
            }
        }
        
        if (satellites <= 0)
        {
            int err = (int) location.getAccuracy() * 7358 >> 13;
            if (err > veryWeakGpsSignal)
            {
                satellites = 1;
            }
            else if (err > weakGpsSignal)
            {
                satellites = 2;
            }
            else if (err > nomalGpsSignal)
            {
                satellites = 3;
            }
            else if (err > strongGpsSignal)
            {
                satellites = 4;
            }
            else if (err > veryStrongGpsSignal)
            {
                satellites = 6;
            }
            else
            {
                satellites = 7;
            }
        }
        
        return satellites;
    }

    public void onConnectionFailed(ConnectionResult arg0)
    {
        if(Logger.DEBUG)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), "Google Service is not available, revert to raw gps!!!");
        }
        
        //Roll back to traditional native gps service
        if (requestRefreshMonitor.isAlive())
        {
            requestRefreshMonitor.interrupt();
        }
        
        ((AndroidPlatformIniter) (AbstractPlatformIniter.getInstance())).resetLocationProviderByRawGps();
    }

    public void onConnected(Bundle arg0)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, GoogleServiceLocationProvider.class.getName(), "Google Service connected");
        }
        if (mLocationClient != null && isRequestingGps)
        {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
        
        if(!requestRefreshMonitor.isAlive())
        {
            requestRefreshMonitor.start();
        }
    }

    public void onDisconnected()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, GoogleServiceLocationProvider.class.getName(), "Google Service disconnected");
        }
    }
    
    protected TnLocation convert(Location location)
    {
        if(location == null)
            return null;
        
        TnLocation tnLocation = new TnLocation(convertToTnProvider(location.getAccuracy()));
        tnLocation.setAccuracy((int)location.getAccuracy() * 7358 >> 13);
        tnLocation.setAltitude((int)location.getAltitude());
        tnLocation.setHeading((int)location.getBearing());
        tnLocation.setLatitude((int)(location.getLatitude() * 100000));
        tnLocation.setLongitude((int)(location.getLongitude() * 100000));
        tnLocation.setSpeed((int)location.getSpeed()* 9);
        tnLocation.setTime(location.getTime()/10);
        tnLocation.setValid(true);
        
        return tnLocation;
    }
    
    protected String convertToTnProvider(float accuracy)
    {
        if (accuracy < 0)
            return null;

        if (accuracy < strongGpsSignal)
        {
            return TnLocationManager.GPS_179_PROVIDER;
        }
        else
        {
            return TnLocationManager.NETWORK_PROVIDER;
        }
    }
    
    public static boolean isAvailable()
    {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, GoogleServiceLocationProvider.class.getName(), "Google Service connection results: "
                    + result);
        }
        return result == ConnectionResult.SUCCESS;
    }
    
    public void setLocationParams(long interval, long fasterInterval, int priority, boolean immediately)
    {
        synchronized (monitorThreadMutex)
        {
            param.interval = interval;
            param.fasterInterval = fasterInterval;
            param.priority = priority;
            if(immediately)
            {
                monitorThreadMutex.notify();
            }
        }
    }
    
    void refreshLocationRequest()
    {
        this.stop();
        
        if (param.interval > 0)
        {
            mLocationRequest.setInterval(param.interval);
        }
        if (param.fasterInterval > 0)
        {
            mLocationRequest.setFastestInterval(param.fasterInterval);
        }
        if (param.priority > 0)
        {
            mLocationRequest.setPriority(param.priority);
        }
        
        this.start();
    }
    
    class LocationRequestParam
    {
        private long interval;

        private long fasterInterval;

        private int priority;
        
        LocationRequestParam(long interval, long fasterInterval, int priority)
        {
            this.interval = interval;
            this.fasterInterval = fasterInterval;
            this.priority = priority;
        }
    }
    
    class RequestMonitorThread extends Thread
    {
        boolean isStopped = false;
        
        public void interrupt()
        {
            this.isStopped = true;
            super.interrupt();
        }
        
        public void run()
        {
            while (!isStopped)
            {
                synchronized (monitorThreadMutex)
                {
                    try
                    {
                        monitorThreadMutex.wait(8000);
                        if (param.interval > 0 || param.fasterInterval > 0 || param.priority > 0)
                        {
                            refreshLocationRequest();
                            
                            param.interval = -1;
                            param.fasterInterval = -1;
                            param.priority = -1;
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
        
    }

}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AndroidNativeLocationProvider.java
 *
 */
package com.telenav.module.location.android;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.telenav.app.NavServiceManager;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.gps.IGpsListener;
import com.telenav.gps.TnGpsFilter;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.module.location.ITnLocationProvider;
import com.telenav.navservice.NavServiceParameter;

/**
 *@author yren (yren@telenav.cn)
 *@date 2012-1-18
 */
public class AndroidNativeLocationProvider implements android.location.LocationListener, ITnLocationProvider
{    
    protected static final long SAFETY_TIMEOUT          = 45 * 1000;
    
    protected IGpsListener gpsListener = null;
    
    protected TnLocation[] gpsPositionData;
    
    protected TnLocation[] networkPositionData;
    
    protected int latestGpsFixIndex;
    
    protected int latestNetworkFixIndex;

    private final static byte DEFAULT_GPS_BUFFER_SIZE = 5;
    
    private LocationManager locationManager;    
    
    private Object dataMutex = new Object();
    
    private int status = -1;

    private TnGpsFilter gpsFilter;
    
    private boolean isGettingGps;
    
    private long lastGpsFixTime;
    
    private MonitorThread monitorThread;
    
    private static final int veryStrongGpsSignal = 20;
    
    private static final int strongGpsSignal = 50;
    
    private static final int nomalGpsSignal = 100;
    
    private static final int weakGpsSignal = 150;
    
    private static final int veryWeakGpsSignal = 200;
    
    public long gpsTimeOffset = 0;
    
    public long networkTimeOffset = 0;
    
    private class MonitorThread implements Runnable
    {
        private boolean isRunning;

        private Object waitMutex = new Object();

        public void start()
        {
            isRunning = true;
            new Thread(this).start();
        }

        public void stop()
        {
            isRunning = false;
            synchronized (waitMutex)
            {
                waitMutex.notify();
            }
        }

        public void run()
        {
            while (isRunning)
            {
                synchronized (waitMutex)
                {
                    try
                    {
                        waitMutex.wait(3000);
                    }
                    catch (Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }

                if (!isRunning)
                    break;

                try
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "Monitoring GPS in thread " + Thread.currentThread().getId());
                    if (isGettingGps && System.currentTimeMillis() - lastGpsFixTime > SAFETY_TIMEOUT)
                    {
                        // no gps for a long time, try to restart gps engine

                        // pause nav service for a while
                        if (NavServiceManager.getNavService() != null)
                        {
                            NavServiceParameter param = new NavServiceParameter();
                            param.setPauseGpsTime(15);
                            NavServiceManager.getNavService().setNavServiceParameters(param);

                            Thread.sleep(2000); // wait for navservice to pause GPS
                        }

                        if (!isRunning)
                            break;

                        stopImpl();
                        Thread.sleep(500);
                        if (!isRunning)
                            break;

                        synchronized (AndroidNativeLocationProvider.this)
                        {
                            if (isGettingGps) // in case stop() is called during the sleep
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "jump start GPS in thread "
                                        + Thread.currentThread().getId());
                                startImpl();
                            }
                        }

                        lastGpsFixTime = System.currentTimeMillis();
                    }
                }
                catch (Throwable t)
                {
                    Logger.log(this.getClass().getName(), t);
                }
            }
        }
    }

    public AndroidNativeLocationProvider()
    {
        initBuffer();
        
        gpsFilter = new TnGpsFilter();
        
        this.locationManager = (LocationManager) AndroidPersistentContext.getInstance()
                .getContext().getSystemService(Context.LOCATION_SERVICE);
        
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
        //for most device, should get the fix in this API.
        TnLocation lastLocation = convert(locationManager.getLastKnownLocation(provider));
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
            //last known location was not received by chipset now, so the local time stamp should not be System.currentTimeMillis()
            //set it to be same as GPS time stamp
            lastLocation.setLocalTimeStamp(lastLocation.getTime() * 10 + offset);
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
    
    public synchronized void start()
    {
        startImpl();
        this.lastGpsFixTime = System.currentTimeMillis();
        this.isGettingGps = true;
        
        if (monitorThread == null)
        {
            monitorThread = new MonitorThread();
            monitorThread.start();
        }
        
    }
    
    private void startImpl()
    {
        if (isProviderAvailable(LocationManager.GPS_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.getMainLooper());
        }
        
        try
        {
            if (isProviderAvailable(LocationManager.NETWORK_PROVIDER))
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this, Looper.getMainLooper());
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    private boolean isProviderAvailable(String provider)
    {
        if (provider == null)
        {
            return false;
        }
        
        List<String> providers = locationManager.getAllProviders();
        
        if (providers == null)
        {
            return true;
        }
        
        int size = providers.size();
        
        for (int i = 0; i < size; i++)
        {
            String curr = providers.get(i);
            if (provider.equals(curr))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public synchronized void stop()
    {
        this.isGettingGps = false;
        if (monitorThread != null)
        {
            monitorThread.stop();
            monitorThread = null;
        }
        stopImpl();
    }
    
    private void stopImpl()
    {
        locationManager.removeUpdates(this);
    }
    
    public void setListener(IGpsListener gpsListener)
    {
        this.gpsListener = gpsListener;
    }
    
    public void clearListener()
    {
        this.gpsListener = null;
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
                    "DB-Test addGpsData --- index: " + latestGpsFixIndex + " , lat: "
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
                    "DB-Test addNetworkData --- index : " + latestNetworkFixIndex
                            + " , lat : " + tnLocation.getLatitude() + " , lon : "
                            + tnLocation.getLongitude());
            }
        }
    }

    public void onLocationChanged(Location location)
    {
        if (location != null)
        {
            TnLocation tnLocation = convert(location);
            tnLocation.setSatellites(getSatellites(location));
            synchronized (dataMutex)
            {
                if (LocationManager.GPS_PROVIDER.equals(location.getProvider()))
                {
                    if (gpsFilter.eliminateGpsNoise(tnLocation))
                    {
                        addGpsData(tnLocation);
                    }
                    else
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(),
                                "eliminateGpsNoise ------------- lat: " + tnLocation.getLatitude() + ", lon: "
                                        + tnLocation.getLongitude() + ", heading: "+tnLocation.getHeading() 
                                        + ", time: "+tnLocation.getTime() + ", localTime: "+tnLocation.getLocalTimeStamp());
                        }
                    }
                }
                if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider()))
                {
                    addNetworkData(tnLocation);
                }
            }

            notifyGpsArrived(tnLocation);
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
    
	protected TnLocation convert(Location location)
    {
        if(location == null)
            return null;
        
        TnLocation tnLocation = new TnLocation(convertToTnProvider(location.getProvider()));
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
    
	protected String convertToTnProvider(String nativeProvider)
    {
        if(nativeProvider == null)
            return null;
        
        if(LocationManager.GPS_PROVIDER.equals(nativeProvider))
        {
            return TnLocationManager.GPS_179_PROVIDER;
        }
        else if(LocationManager.NETWORK_PROVIDER.equals(nativeProvider))
        {
            return TnLocationManager.NETWORK_PROVIDER;
        }

        return null;
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
    
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        this.status = status;
    }

    public void onProviderEnabled(String provider)
    {
        
    }

    public void onProviderDisabled(String provider)
    {
        
    }

    public void setLocationParams(long interval, long fasterInterval, int priority, boolean immediately)
    {
        
    }
}

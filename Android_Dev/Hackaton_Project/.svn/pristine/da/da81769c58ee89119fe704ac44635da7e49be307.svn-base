/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapVehiclePositionService.java
 *
 */
package com.telenav.ui.citizen.map;

import com.telenav.data.serverproxy.impl.navsdk.NavSdkLocationProviderProxy;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;

/**
 * @author yning
 * @date 2011-1-21
 */
public class MapVehiclePositionService implements IJob
{
    public static int NOTIFY_INTERVAL = 1 * 1000;

    private boolean isStoped = true;

    private boolean isPaused = true;;

    private boolean isGpsLocationAvailable = true;

    private boolean isNetworkLocationAvailable = false;

    private IVechiclePositionCallback vechiclePositionCallback;

    private long noGpsTimeout = NO_GPS_DEFAULT_VALUE;

    private static final long NO_GPS_DEFAULT_VALUE = 8000;

    private long lastGpsAvailableTime = -1;

    private int lastHeading = 0;

    private boolean needCheckTimeout = false;

    private static final int SHOWING_CAR_STATE_NOT_INITIALIZED = -1;
    private static final int SHOWING_CAR_STATE_TRUE = 1;
    private static final int SHOWING_CAR_STATE_FALSE = 0;

    private static MapVehiclePositionService service = new MapVehiclePositionService();

    private ThreadPool threadPool;

    private Object mutex = new Object();
    
    private boolean needHandleCar = false;

    private MapVehiclePositionService()
    {

    }

    public static MapVehiclePositionService getInstance()
    {
        return service;
    }

    public void setVehiclePositionCallback(IVechiclePositionCallback vechiclePositionCallback)
    {
        this.vechiclePositionCallback = vechiclePositionCallback;
    }

    public void start(boolean needHandleCar)
    {
        synchronized (mutex)
        {
            this.needHandleCar = needHandleCar;
            if (!isStoped)
            {
                return;
            }
            isStoped = false;
            if (threadPool == null)
            {
                threadPool = new ThreadPool(1, false, (byte) 1000);
                threadPool.start();

                threadPool.addJob(this);
            }
        }
    }

    public void stop()
    {
        synchronized (mutex)
        {
            isStoped = true;
            if (threadPool != null)
            {
                threadPool.cancelAll();
                threadPool.stop();
            }
            clear();
        }
    }

    public void resume(boolean needHandleCar)
    {
        synchronized (mutex)
        {
            this.needHandleCar = needHandleCar;
            isPaused = false;
            mutex.notifyAll();
        }
    }
    
    public void resume()
    {
        resume(needHandleCar);
    }

    public void pause()
    {
        isPaused = true;
    }

    public void clear()
    {
        isGpsLocationAvailable = true;
        isNetworkLocationAvailable = false;
        noGpsTimeout = NO_GPS_DEFAULT_VALUE;
        lastGpsAvailableTime = -1;
        needCheckTimeout = false;
    }

    public void restart(boolean needHandleCar)
    {
        synchronized (mutex)
        {
            this.needHandleCar = needHandleCar;
            clear();

            if (isStoped)
            {
                start(true);
            }
        }
    }
    
    public void execute(int handlerID)
    {
        while (!this.isStoped)
        {
            synchronized (mutex)
            {
                checkForPause();
                TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS, true);
                if (location != null)
                {
                    // update GPS location
                    updateGpsLocation(location, true);
                    isGpsLocationAvailable = true;
                    isNetworkLocationAvailable = false;
                    lastGpsAvailableTime = System.currentTimeMillis();
                }
                else
                {
                    location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_NETWORK, true);
                    if (lastGpsAvailableTime == -1)
                    {
                        lastGpsAvailableTime = System.currentTimeMillis();
                    }
                    
                    if (location != null)
                    {
                        // The navigation status only can use real GPS, The network GPS cannot be provided if in
                        // navigation
                        updateGpsLocation(location, !NavSdkNavEngine.getInstance().isRunning());

                        if (vechiclePositionCallback != null && !isNetworkLocationAvailable)
                        {
                            needCheckTimeout = true;
                        }
                        isGpsLocationAvailable = false;
                        isNetworkLocationAvailable = true;
                    }
                    else
                    {
                        if (isGpsLocationAvailable || isNetworkLocationAvailable)
                        {
                            isGpsLocationAvailable = false;
                            isNetworkLocationAvailable = false;
                            if (needHandleCar)
                            {
                                hideCar();
                            }
                            if (vechiclePositionCallback != null)
                            {
                                vechiclePositionCallback.updateVechiclePostion(null);
                                needCheckTimeout = true;
                            }
                        }
                    }
                    if (!isGpsLocationAvailable && !isNetworkLocationAvailable)
                    {
                        if (System.currentTimeMillis() - lastGpsAvailableTime > noGpsTimeout)
                        {
                            if (vechiclePositionCallback != null)
                            {
                                vechiclePositionCallback.gpsWeak();
                            }
                        }
                    }
                    if (needCheckTimeout)
                    {
                        if (System.currentTimeMillis() - lastGpsAvailableTime > noGpsTimeout)
                        {
                            needCheckTimeout = false;
                            if (vechiclePositionCallback != null)
                            {
                                vechiclePositionCallback.noGpsTimeout();
                            }
                        }
                    }
                }
            }
            try
            {
                synchronized (mutex)
                {
                    mutex.wait(1000);
                }
            }
            catch (InterruptedException e)
            {
            }
        }

        this.isStoped = true;
    }

    private void checkForPause()
    {
        if (isPaused)
        {
            try
            {
                synchronized (mutex)
                {
                    mutex.wait(0);
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }

    public void updateGpsLocation(final TnLocation location, boolean needUpdateLocationToSDK)
    {
        if (needHandleCar)
        {
            // enable layer
            MapContainer.getInstance().enableCar();
        }

        if (location.getSpeed() != 0)
        {
            lastHeading = location.getHeading();
        }

        if (location.getHeading() == 0)
        {
            location.setHeading(lastHeading);
        }

        int meterAccuracy;
        String provider = location.getProvider();
        if (provider.equalsIgnoreCase(TnLocationManager.NETWORK_PROVIDER)
                || provider.equalsIgnoreCase(TnLocationManager.TN_NETWORK_PROVIDER))
        {
            if (MapContainer.getInstance().isGPSCoarseEnabled())
            {
                MapContainer.getInstance().setLowFPSAllowed(false);
            }
            else
            {
                MapContainer.getInstance().setLowFPSAllowed(true);
            }
            meterAccuracy = location.getAccuracy();
        }
        else
        {
            MapContainer.getInstance().setLowFPSAllowed(true);
            meterAccuracy = 0;
        }
        location.setAccuracy(meterAccuracy);

        if (needUpdateLocationToSDK)
        {
            NavSdkLocationProviderProxy navSdkLocationProvider = new NavSdkLocationProviderProxy(null);
            navSdkLocationProvider.updateLocation(location);
        }

        if (vechiclePositionCallback != null)
        {
            vechiclePositionCallback.updateVechiclePostion(location);
        }
    }

    public boolean isLocationAvailable()
    {
        return isGpsLocationAvailable || isNetworkLocationAvailable;
    }

    public boolean isGpsLocationAvailable()
    {
        return isGpsLocationAvailable;
    }

    public boolean isNetworkLocationAvailable()
    {
        return isNetworkLocationAvailable;
    }

    public void hideCar()
    {
        TnLocation loc = LocationProvider.getInstance().getLastKnownLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if (loc == null)
        {
            loc = LocationProvider.getInstance().getDefaultLocation();
        }

        loc.setAccuracy(0);
        NavSdkLocationProviderProxy navSdkLocationProvider = new NavSdkLocationProviderProxy(null);
        navSdkLocationProvider.updateLocation(loc);
        MapContainer.getInstance().disableCar();
    }

    @Override
    public void cancel()
    {

    }

    @Override
    public boolean isCancelled()
    {
        return false;
    }

    @Override
    public boolean isRunning()
    {
        return !isStoped;
    }
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkGpsService.java
 *
 */
package com.telenav.module.location;

import java.util.Vector;

import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkNavigationProxyHelper.NavSdkNavigationUtil;
import com.telenav.gps.IGpsListener;
import com.telenav.gps.TnGpsFilter;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.eventmanager.EventHandler;
import com.telenav.navsdk.events.NavigationEvents;
import com.telenav.navsdk.events.NavigationEvents.VehiclePosition;

/**
 *@author chrbu
 *@date 2012-9-12
 */
public class NavSdkGpsService
{

    private static final int DEFAULT_GPS_BUFFER_SIZE = 5;

    private TnLocation[] positionData;

    private int oldestGpsBufferIndex;

    private String sourceTypeString;

    private Object mutex = new Object();

    private int gpsStatus = -1;

//    private int accuracy;

//    private TnLocation lastKnownPosition;

    protected Vector gpsListeners;

//    private long lastFixTime;

    protected TnGpsFilter gpsFilter = new TnGpsFilter();

    private EventHandler handler;

    public NavSdkGpsService(int gpsType, String provider)
    {
        this.sourceTypeString = provider;


        positionData = new TnLocation[DEFAULT_GPS_BUFFER_SIZE];
        for (int i = 0; i < DEFAULT_GPS_BUFFER_SIZE; ++i)
        {
            positionData[i] = new TnLocation(provider);
        }
        oldestGpsBufferIndex = 0;

//        lastKnownPosition = new TnLocation(provider);
        
        handler = new EventHandler()
        {
            
            @Override
            public void handle(String eventType, Object eventData)
            {
                if (eventType.equalsIgnoreCase("VehiclePosition")) {
                    VehiclePosition event = (NavigationEvents.VehiclePosition)eventData;
//                    Log.d("qhzhou", "lat:" + event.getMatchedLocation().getLatitude() + "\tlon:" + event.getMatchedLocation().getLongitude());
                    onLocationUpdated(event);
                }
            }
        };
        
    }

    public void start()
    {
        EventBus.getMain().subscribe("VehiclePosition", handler);

    }

    public void stop()
    {
        EventBus.getMain().unsubscribe("VehiclePosition", handler);

    }

    public void onLocationUpdated(VehiclePosition position)
    {
        synchronized (mutex)
        {
            parseVehiclePositionToTnLocation(position, positionData[oldestGpsBufferIndex]);
            notifyListener(positionData[oldestGpsBufferIndex]);
            oldestGpsBufferIndex++;
            if (oldestGpsBufferIndex >= DEFAULT_GPS_BUFFER_SIZE)
            {
                oldestGpsBufferIndex = 0;
            }
        }

    }

    private void notifyListener(TnLocation data)
    {
//        this.lastFixTime = System.currentTimeMillis();

        if (gpsFilter.eliminateGpsNoise(data))
        {
            if (gpsListeners != null)
            {
                for (int i = 0; i < gpsListeners.size(); i++)
                {
                    try
                    {
                        IGpsListener gpsListener = (IGpsListener) gpsListeners
                                .elementAt(i);
                        gpsListener.gpsDataArrived(data);
                    }
                    catch (Throwable t)
                    {
                        t.printStackTrace();
                    }
                }
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "GPS data is eliminated");
        }
    }

    private void parseVehiclePositionToTnLocation(VehiclePosition position, TnLocation location)
    {
        if (position == null || location == null)
        {
            return;
        }
        location.setLatitude(NavSdkNavigationUtil.convertCoordinate(position.getMatchedLocation().getLatitude()));
        location.setLongitude(NavSdkNavigationUtil.convertCoordinate(position.getMatchedLocation().getLongitude()));
        location.setAltitude((float) position.getMatchedLocation().getAltitude());
        location.setValid(true);
        location.setLocalTimeStamp(System.currentTimeMillis());
        //FIXME hardcode here
        location.setSatellites(3);
        location.setHeading((int) position.getHeading());
        location.setTime(position.getTime());
        location.setSpeed((int) position.getSpeed());
        location.setAccuracy((int) position.getPositionErrorRadiusInMeters());
    }


    protected int getBufferSize()
    {
        int size = 0;
        if (this.positionData != null)
            size = this.positionData.length;
        return size;
    }

    protected void setBufferSize(int size)
    {
        if (getBufferSize() != size)
        {
            synchronized (mutex)
            {
                // recreate the buffers
                this.positionData = new TnLocation[size];
                for (int i = 0; i < size; i++)
                    this.positionData[i] = new TnLocation(sourceTypeString);
                this.oldestGpsBufferIndex = 0;
            }
        }
    }

    public String getSourceType()
    {
        return sourceTypeString;
    }

    public int getGpsStatus()
    {
        return gpsStatus;
    }

    public void addListener(IGpsListener gpsListener)
    {
        if (this.gpsListeners == null)
        {
            this.gpsListeners = new Vector();
        }

        this.gpsListeners.addElement(gpsListener);
    }

    public void removeListener(IGpsListener gpsListener)
    {
        if (this.gpsListeners == null)
            return;

        this.gpsListeners.removeElement(gpsListener);
    }

    public void removeAllListeners()
    {
        if (this.gpsListeners == null)
            return;

        this.gpsListeners.removeAllElements();
    }

    public int getFixes(int numFixes, TnLocation[] locations)
    {
        int fixCounter = 0;
        synchronized (mutex)
        {
            int startIndex = oldestGpsBufferIndex - 1;
            for (int i = startIndex; i >= 0; --i)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    locations[fixCounter++].set(pos);
                    if (fixCounter >= numFixes || fixCounter >= locations.length)
                    {
                        return fixCounter;
                    }
                }
            }
            for (int i = positionData.length - 1; i >= oldestGpsBufferIndex; --i)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    locations[fixCounter++].set(pos);
                    if (fixCounter >= numFixes || fixCounter >= locations.length)
                    {
                        return fixCounter;
                    }
                }
            }
        }

        return fixCounter;
    }

}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NativeNetworkService.java
 *
 */
package com.telenav.module.location;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

/**
 *@author gbwang
 *@date 16 Sep 2011
 */
public class NativeNetworkService implements ITnLocationListener
{
    final static int DEFAULT_TIMEOUT = 32000; // 32s

    final static byte DEFAULT_GPS_BUFFER_SIZE = 5;

    protected boolean isStarted = false;

    protected TnLocation[] positionData;

    protected int latestFixIndex;

    protected long lastFixTime = 0;

    protected Object dataMutex = new Object();

    protected Object serviceMutex = new Object();

    NativeNetworkService()
    {
        setBufferSize(DEFAULT_GPS_BUFFER_SIZE);
    }

    public void start()
    {
        synchronized (serviceMutex)
        {
            if (isStarted)
            {
                return;
            }

            isStarted = true;
            TnLocationManager.getInstance().requestLocationUpdates(
                TnLocationManager.NETWORK_PROVIDER, 0, 0.0f, DEFAULT_TIMEOUT, 0, this);

        }
    }

    public void stop()
    {
        synchronized (serviceMutex)
        {
            if (!isStarted)
            {
                return;
            }

            isStarted = false;
            TnLocationManager.getInstance().reset(TnLocationManager.NETWORK_PROVIDER);
        }
    }

    public void onLocationChanged(TnLocationProvider provider, TnLocation location)
    {
        this.addNativeNetworkData(location);
    }

    public void onStatusChanged(TnLocationProvider provider, int status)
    {

    }

    protected void addNativeNetworkData(TnLocation data)
    {
        //todo, do we need cache the data?
        if (data.getSatellites() < 1)
        {
            data.setSatellites(1);
        }
        synchronized (dataMutex)
        {
            positionData[latestFixIndex].set(data);
            latestFixIndex++;
            if (latestFixIndex >= positionData.length)
                latestFixIndex = 0;
        }
    }

    public int getFixes(int numFixes, TnLocation[] data)
    {
        int fixCounter = 0;
        synchronized (dataMutex)
        {
            for (int i = latestFixIndex - 1; i >= 0; i--)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    data[fixCounter].set(pos);
                    fixCounter++;
                    if (fixCounter >= numFixes || fixCounter >= data.length)
                    {
                        return fixCounter;
                    }
                }
            }

            for (int i = getBufferSize() - 1; i >= latestFixIndex; i--)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    data[fixCounter].set(pos);
                    fixCounter++;
                    if (fixCounter >= numFixes || fixCounter >= data.length)
                    {
                        return fixCounter;
                    }
                }
            }
        }

        return fixCounter;
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
            synchronized (dataMutex)
            {
                // recreate the buffers
                this.positionData = new TnLocation[size];
                for (int i = 0; i < size; i++)
                    this.positionData[i] = new TnLocation(TnLocationManager.NETWORK_PROVIDER);
                this.latestFixIndex = 0;
            }
        }
    }
}

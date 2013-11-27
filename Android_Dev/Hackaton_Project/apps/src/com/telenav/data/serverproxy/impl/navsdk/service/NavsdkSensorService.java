/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavsdkSensorService.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.service;

import java.util.HashMap;
import java.util.Map;

import com.telenav.module.nav.SensorAccelerometerImpl;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.SensorData.Acceleration;
import com.telenav.navsdk.events.SensorEvents;
import com.telenav.navsdk.events.SensorEvents.AccelerometerUpdateRequest;
import com.telenav.navsdk.events.SensorEvents.StartAccelerometerUpdate;
import com.telenav.navsdk.events.SensorEvents.StopAccelerometerUpdate;
import com.telenav.navsdk.services.SensorListener;
import com.telenav.navsdk.services.SensorServiceProxy;
import com.telenav.telephony.ITnSensorListener;
import com.telenav.telephony.TnAccelerometerInfo;

/**
 * adapt from navsdk foundation :com.telenav.modules.sensor.SensorService
 * 
 * @author pwang
 * @date 2012-12-5
 */
public class NavsdkSensorService implements SensorListener
{
    private SensorAccelerometerImpl sensorAccelerometer;

    private static final int DEFAULT_INTERVAL_OF_LISTENER = 1000;// in millisecond;

    private Map<Integer, ITnSensorListener> mListenerMap;

    private static NavsdkSensorService instance;

    private SensorServiceProxy serverProxy;

    private NavsdkSensorService()
    {
        sensorAccelerometer = SensorAccelerometerImpl.getInstance();
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavsdkSensorService();
            instance.setEventBus(bus);
        }
    }

    public static NavsdkSensorService getInstance()
    {
        return instance;
    }

    private void setEventBus(EventBus bus)
    {
        serverProxy = new SensorServiceProxy(bus);
        serverProxy.addListener(this);
    }

    public void release()
    {
        if (sensorAccelerometer != null)
        {
            sensorAccelerometer.release();
            sensorAccelerometer = null;
        }
        if (mListenerMap != null)
        {
            mListenerMap.clear();
            mListenerMap = null;
        }
    }

    class SeveiceAccelerometerListener implements ITnSensorListener
    {
        public void onAccelerometerUpdated(TnAccelerometerInfo accelerometerInfo)
        {
            Acceleration.Builder builder = Acceleration.newBuilder();
            builder.setX(accelerometerInfo.x);
            builder.setY(accelerometerInfo.y);
            builder.setZ(accelerometerInfo.z);
            builder.setTimestamp(accelerometerInfo.timestamp);
            Acceleration data = builder.build();
            AccelerometerUpdateRequest.Builder respbuilder = AccelerometerUpdateRequest.newBuilder();
            respbuilder.setData(data);
            serverProxy.accelerometerUpdate(respbuilder.build());
        }
    }

    public void onStartAccelerometerUpdate(SensorEvents.StartAccelerometerUpdate event)
    {
        handle(event);
    }

    public void onStopAccelerometerUpdate(SensorEvents.StopAccelerometerUpdate event)
    {
        handle(event);
    }

    public void handle(StartAccelerometerUpdate req)
    {
        if (sensorAccelerometer == null)
            return;
        if (!req.hasListenerId())
            return;
        if (mListenerMap == null)
            mListenerMap = new HashMap<Integer, ITnSensorListener>();

        int listenerId = req.getListenerId();

        int interval = DEFAULT_INTERVAL_OF_LISTENER;
        if (req.hasFrequency())
            interval = req.getFrequency();
        ITnSensorListener listener = mListenerMap.get(listenerId);
        if (listener == null)
        {
            listener = new SeveiceAccelerometerListener();
            mListenerMap.put(listenerId, listener);
        }
        sensorAccelerometer.addAccelerometerListener(listener, interval);
        sensorAccelerometer.start();
    }

    public void handle(StopAccelerometerUpdate req)
    {
        if (sensorAccelerometer == null)
            return;
        if (!req.hasListenerId())
        {
            sensorAccelerometer.stop();
        }
        else
        {
            if (mListenerMap == null)
                return;
            int id = req.getListenerId();
            ITnSensorListener listener = mListenerMap.get(id);
            sensorAccelerometer.removeAccelerometerListener(listener);
            mListenerMap.remove(id);
        }
    }

}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AndroidSensorManager.java
 *
 */
package com.telenav.telephony.android;

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.telephony.ServiceState;

import com.telenav.logger.Logger;
import com.telenav.radio.ITnCoverageListener;
import com.telenav.telephony.ITnBatteryListener;
import com.telenav.telephony.ITnSensorListener;
import com.telenav.telephony.TnAccelerometerInfo;
import com.telenav.telephony.TnSensorManager;

/**
 *Provides access to the battery status at android platform.
 *@author pwang
 *@date 2012-12-7
 */
public class AndroidSensorManager extends TnSensorManager implements SensorEventListener
{
    protected Context context;

    private SensorManager sensorManager;
    
    private Sensor accelerometer;

    protected Vector listeners;
    
    /**
     * Construct the sensor manager at android platform. <br />
     * <br />
     * @param context
     */
    public AndroidSensorManager(Context context)
    {
        this.context = context;

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        listeners = new Vector();
        
        registerReceiver();
    }
    
    public void addListener(ITnSensorListener sensorListener)
    {
        listeners.addElement(sensorListener);
    }

    public void removeListener(ITnSensorListener sensorListener)
    {
        listeners.removeElement(sensorListener);
    }

    
    private void registerReceiver()
    {
        if(sensorManager != null && accelerometer != null)
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    private void unregisterReceiver()
    {
        if(sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public void finish()
    {
        unregisterReceiver();
    }

    protected void finalize() throws Throwable
    {
        super.finalize();
        
        unregisterReceiver();
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //TODO implement when there is requirement.
    }

    public void onSensorChanged(SensorEvent event)
    {
        TnAccelerometerInfo accelerometerInfo = new TnAccelerometerInfo();
        accelerometerInfo.x = event.values[0];
        accelerometerInfo.y = event.values[1];
        accelerometerInfo.z = event.values[2];
        accelerometerInfo.timestamp = (double) System.currentTimeMillis() / 1000;

        for (int i = 0; i < listeners.size(); i++)
        {
            ITnSensorListener listener = (ITnSensorListener) listeners.elementAt(i);
            listener.onAccelerometerUpdated(accelerometerInfo);
        }
    }

}

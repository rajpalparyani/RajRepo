/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * SensorAccelerometerImpl.java
 *
 */
package com.telenav.module.nav;

import java.util.Timer;
import java.util.Vector;

import com.telenav.telephony.ITnSensorListener;
import com.telenav.telephony.TnAccelerometerInfo;
import com.telenav.telephony.TnSensorManager;

/**
 * adapt from navsdk foundation :com.telenav.modules.sensor.Accelerometer
 *@author pwang
 *@date 2012-12-7
 */
public class SensorAccelerometerImpl implements ITnSensorListener
{
    private static SensorAccelerometerImpl instance = new SensorAccelerometerImpl();

    private TnSensorManager sensorManager;

    private TnAccelerometerInfo accelerometerInfo;

    private Vector<ListenerInfo> mListenerList;

    private Timer mTimer;

    private boolean bRunning;

    private boolean bRegistered;

    public SensorAccelerometerImpl()
    {
        sensorManager = TnSensorManager.getInstance();
        accelerometerInfo = new TnAccelerometerInfo();
        bRunning = false;
        bRegistered = false;
    }

    public static SensorAccelerometerImpl getInstance()
    {
        return instance;
    }

    public void release()
    {
        bRunning = false;
        if (bRegistered && sensorManager != null)
            sensorManager.removeListener(this);
        bRegistered = false;
        if (mTimer != null)
            mTimer.cancel();
        mTimer = null;

        if (mListenerList != null)
            mListenerList.clear();
        mListenerList = null;

        sensorManager = null;
    }

    static class ListenerInfo
    {
        private int interval;

        private NotifyTask task;

        private ListenerInfo(int interval, NotifyTask task)
        {
            this.interval = interval;
            this.task = task;
        }
    }

    public synchronized void addAccelerometerListener(ITnSensorListener listener, int interval)
    {
        if (mListenerList == null)
            mListenerList = new Vector<ListenerInfo>();
        if (listener == null)
            return;
        int size = mListenerList.size();
        for (int i = 0; i < size; i++)
        {
            ListenerInfo temp = mListenerList.get(i);
            if (temp.task.mListener == listener)
            {
                if (temp.interval == interval)
                    return;
                else
                {
                    temp.task.cancel();
                    temp.interval = interval;
                    temp.task = new NotifyTask(temp.task.mListener);
                    if (bRunning)
                    {
                        if (mTimer != null)
                            mTimer.purge();
                        else
                            mTimer = new Timer();
                        mTimer.schedule(temp.task, interval, interval);
                    }
                    return;
                }
            }
        }

        NotifyTask task = new NotifyTask(listener);
        ListenerInfo info = new ListenerInfo(interval, task);
        mListenerList.add(info);
        if (bRunning)
        {
            if (mTimer != null)
                mTimer.purge();
            else
                mTimer = new Timer();
            mTimer.schedule(task, interval, interval);
        }
    }

    public synchronized void removeAccelerometerListener(ITnSensorListener listener)
    {
        if (mListenerList == null)
            return;
        if (listener == null)
            return;
        int size = mListenerList.size();
        if (size == 0)
            return;
        for (int i = 0; i < size; i++)
        {
            ListenerInfo temp = mListenerList.get(i);
            if (temp.task.mListener == listener)
            {
                if (bRunning)
                    temp.task.cancel();
                mListenerList.remove(i);
                break;
            }
        }
        if (mTimer != null)
            mTimer.purge();
        if (mListenerList.size() == 0)
        {
            if (sensorManager != null)
                sensorManager.removeListener(this);
            bRegistered = false;
            bRunning = false;
            if (mTimer != null)
                mTimer.cancel();
            mTimer = null;
        }
    }

    public synchronized void start()
    {
        if (bRunning)
            return;

        if (sensorManager != null)
            sensorManager.addListener(this);
        bRegistered = true;
        bRunning = true;
        if (mTimer == null)
            mTimer = new Timer();
        int size = 0;
        if (mListenerList != null)
            size = mListenerList.size();

        for (int i = 0; i < size; i++)
        {
            ListenerInfo info = mListenerList.get(i);
            info.task = new NotifyTask(info.task.mListener);
            mTimer.schedule(info.task, info.interval, info.interval);
        }
    }

    public synchronized void stop()
    {
        if (bRunning)
        {
            int size = mListenerList.size();
            for (int i = 0; i < size; i++)
            {
                mListenerList.get(i).task.cancel();
            }
            if (mTimer != null)
                mTimer.cancel();
            if (sensorManager != null)
                sensorManager.removeListener(this);
            bRegistered = false;
            mTimer = null;
            bRunning = false;
        }
    }

    public void onAccelerometerUpdated(TnAccelerometerInfo accelerometerInfo)
    {
        this.accelerometerInfo.x = accelerometerInfo.x;
        this.accelerometerInfo.y = accelerometerInfo.y;
        this.accelerometerInfo.z = accelerometerInfo.z;
        this.accelerometerInfo.timestamp = (double) System.currentTimeMillis() / 1000;
    }

    class NotifyTask extends java.util.TimerTask
    {
        private ITnSensorListener mListener;

        private NotifyTask(ITnSensorListener listener)
        {
            mListener = listener;
        }

        public void run()
        {
            if (mListener != null && bRunning)
                mListener.onAccelerometerUpdated(accelerometerInfo);
        }
    }

}

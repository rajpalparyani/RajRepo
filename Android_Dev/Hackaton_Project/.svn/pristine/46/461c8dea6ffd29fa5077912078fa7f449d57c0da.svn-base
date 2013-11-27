/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TnSensorManager.java
 *
 */
package com.telenav.telephony;

/**
 *@author pwang
 *@date 2012-12-7
 */
public abstract class TnSensorManager
{
    private static TnSensorManager tnSensorManager;

    private static int initCount;

    /**
     * Retrieve the instance of sensor manager.
     * 
     * @return {@link TnBatteryManager}
     */
    public static TnSensorManager getInstance()
    {
        return tnSensorManager;
    }

    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param sensorMngr This manager is native manager of platforms. Such as {@link AndroidSensorManager} etc.
     */
    public synchronized static void init(TnSensorManager sensorManager)
    {
        if (initCount >= 1)
            return;

        tnSensorManager = sensorManager;
        initCount++;
    }

    /**
     * register a sensor listener object to receive notification of sensor state.
     * 
     * @param sensorListener The {@link ITnSensorListener} object to register.
     */
    public abstract void addListener(ITnSensorListener sensorListener);

    /**
     * UnRegisters a listener object of sensor state.
     * 
     * @param sensorListener The {@link ITnSensorListener} object to register.
     */
    public abstract void removeListener(ITnSensorListener sensorListener);
    
    /**
     * Finish the manager
     */
    public void finish()
    {
        
    }
}

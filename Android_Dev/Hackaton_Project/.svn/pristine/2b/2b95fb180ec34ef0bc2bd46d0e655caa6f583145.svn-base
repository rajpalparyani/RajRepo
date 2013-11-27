/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnBatteryManager.java
 *
 */
package com.telenav.telephony;

/**
 * Provides access to the device's battery status.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-20
 */
public abstract class TnBatteryManager
{
    private static TnBatteryManager tnbatteryManager;

    private static int initCount;

    /**
     * Retrieve the instance of battery manager.
     * 
     * @return {@link TnBatteryManager}
     */
    public static TnBatteryManager getInstance()
    {
        return tnbatteryManager;
    }

    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param batteryMngr This manager is native manager of platforms. Such as {@link AndroidBatteryManager} etc.
     */
    public synchronized static void init(TnBatteryManager batteryMngr)
    {
        if (initCount >= 1)
            return;

        tnbatteryManager = batteryMngr;
        initCount++;
    }

    /**
     * Retrieves the current battery level. 
     * 
     * @return  battery level.
     */
    public abstract int getBatteryLevel();

    /**
     * register a battery listener object to receive notification of battery state.
     * 
     * @param batteryListener The {@link ITnBatteryListener} object to register.
     */
    public abstract void addListener(ITnBatteryListener batteryListener);

    /**
     * UnRegisters a listener object of battery state.
     * 
     * @param batteryListener The {@link ITnBatteryListener} object to register.
     */
    public abstract void removeListener(ITnBatteryListener batteryListener);
    
    /**
     * Finish the manager
     */
    public void finish()
    {
        
    }
}

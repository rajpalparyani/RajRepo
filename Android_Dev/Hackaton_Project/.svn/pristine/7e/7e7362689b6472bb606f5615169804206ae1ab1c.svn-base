/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnRadioManager.java
 *
 */
package com.telenav.radio;

/**
 * Provides access to information about the radio services on the device. Applications can use the methods in this class
 * to determine radio states, as well as to access some types of radio information. Applications can also register a
 * listener to receive notification of radio coverage's state changes.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 11, 2010
 */
public abstract class TnRadioManager
{
    private static TnRadioManager radioManager;
    private static int initCount;
    
    /**
     * Retrieve the instance of radio manager.
     * 
     * @return {@link TnRadioManager}
     */
    public static TnRadioManager getInstance()
    {
        return radioManager;
    }

    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param radioMngr This manager is native manager of platforms. Such as {@link AndroidRadioManager} etc.
     */
    public synchronized static void init(TnRadioManager radioMngr)
    {
        if(initCount >= 1)
            return;
        
        radioManager = radioMngr;
        initCount++;
    }

    /**
     * Retrieve the cell information.
     * 
     * @return the cell information both support GSM/CDMA device.
     */
    public abstract TnCellInfo getCellInfo();

    /**
     * Check that if use the wifi currently to visit network.
     * 
     * @return true or false.
     */
    public abstract boolean isWifiConnected();
    
    /**
     * Indicates whether network connectivity exists and it is possible to establish connections and pass data.
     * 
     * @return true or false.
     */
    public abstract boolean isNetworkConnected();

    /**
     * Returns true if the device is considered roaming on the current network, for GSM purposes.
     * <p>
     * Availability: Only when user registered to a network.
     */
    public abstract boolean isRoaming();

    /**
     * Returns true if the device is airport mode.
     * 
     * @return true if it's airport mode.
     */
    public abstract boolean isAirportMode();
    
    /**
     * Retrieve the wifi information when wifi is connected.
     * 
     * @return the wifi information.
     */
    public abstract TnWifiInfo getWifiInfo();

    /**
     * Registers a listener object to receive notification of changes in specified coverage states.
     * 
     * @param coverageListener The {@link ITnCoverageListener} object to register.
     */
    public abstract void addListener(ITnCoverageListener coverageListener);

    /**
     * UnRegisters a listener object of coverage state service.
     * 
     * @param coverageListener The {@link ITnCoverageListener} object to register.
     */
    public abstract void removeListener(ITnCoverageListener coverageListener);
}

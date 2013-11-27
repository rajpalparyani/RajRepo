/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ITnBatteryListener.java
 *
 */
package com.telenav.telephony;

/**
 * Implement this interface if you intend to listen for battery status.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-20
 */
public interface ITnBatteryListener
{
    public final static int BATTERY_STATUS_UNKNOWN = 0;

    public final static int BATTERY_STATUS_CHARGING = 1;

    /**
     * Invoked when the internal battery voltage has returned to normal.
     */
    public void batteryGood();

    /**
     * Invoked when the internal battery voltage falls below a critical level.
     */
    public void batteryLow();

    /**
     *  Invoked when the internal battery state has changed.
     *  
     * @param status A BATTERY_STATUS_xxx masks
     * @param level battery level
     */
    public void batteryStatusChange(int status, int level);
}

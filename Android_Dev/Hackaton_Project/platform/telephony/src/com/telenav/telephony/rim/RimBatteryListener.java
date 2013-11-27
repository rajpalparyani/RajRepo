/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RimBatteryListener.java
 *
 */
package com.telenav.telephony.rim;

import com.telenav.telephony.ITnBatteryListener;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.SystemListener;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-20
 */
class RimBatteryListener implements SystemListener
{
    private ITnBatteryListener batteryListener;
    
    public RimBatteryListener(ITnBatteryListener batteryListener)
    {
        this.batteryListener = batteryListener;
    }
    
    public void batteryGood()
    {
        this.batteryListener.batteryGood();
    }

    public void batteryLow()
    {
        this.batteryListener.batteryLow();
    }

    public void batteryStatusChange(int status)
    {
        int tnStatus = ITnBatteryListener.BATTERY_STATUS_UNKNOWN;
        switch(status)
        {
            case DeviceInfo.BSTAT_CHARGING:
            {
                tnStatus = ITnBatteryListener.BATTERY_STATUS_CHARGING;
                break;
            }
        }
        this.batteryListener.batteryStatusChange(tnStatus, DeviceInfo.getBatteryLevel());
    }

    public void powerOff()
    {
        
    }

    public void powerUp()
    {
        
    }

}

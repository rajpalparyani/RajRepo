/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RimBatteryManager.java
 *
 */
package com.telenav.telephony.rim;

import java.util.Hashtable;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.DeviceInfo;

import com.telenav.telephony.ITnBatteryListener;
import com.telenav.telephony.TnBatteryManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-20
 */
public class RimBatteryManager extends TnBatteryManager
{
    protected Hashtable listeners;
    protected Application application;
    
    public RimBatteryManager(Application application)
    {
        this.application = application;
    }
    
    public void addListener(ITnBatteryListener batteryListener)
    {
        if(batteryListener == null)
            return;
        
        if(listeners == null)
        {
            listeners = new Hashtable();
        }
        
        RimBatteryListener rimBatteryListener = new RimBatteryListener(batteryListener);
        listeners.put(batteryListener, rimBatteryListener);
        
        application.addSystemListener(rimBatteryListener);
    }

    public int getBatteryLevel()
    {
        return DeviceInfo.getBatteryLevel();
    }

    public void removeListener(ITnBatteryListener batteryListener)
    {
        if(batteryListener == null || listeners == null)
            return;
        
        RimBatteryListener rimBatteryListener = (RimBatteryListener)listeners.get(batteryListener);
        if(rimBatteryListener == null)
            return;
        
        listeners.remove(batteryListener);
        
        application.removeSystemListener(rimBatteryListener);
    }

}

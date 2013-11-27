/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidBatteryManager.java
 *
 */
package com.telenav.telephony.android;

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.telenav.logger.Logger;
import com.telenav.telephony.ITnBatteryListener;
import com.telenav.telephony.TnBatteryManager;

/**
 * Provides access to the battery status at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-20
 */
public class AndroidBatteryManager extends TnBatteryManager
{
    protected Context context;

    protected int batteryLevel = -1;

    protected Vector listeners;

    static BroadcastReceiver listenerReceiver;
    
    /**
     * Construct the battery manager at android platform. <br />
     * <br />
     * Please make sure that grant below class's permissions. <br />
     * android.permission.BATTERY_STATS
     * 
     * @param context
     */
    public AndroidBatteryManager(Context context)
    {
        this.context = context;

        listeners = new Vector();
        
        if (listenerReceiver == null)
        {
            registerReceiver();
        }
    }

    public void addListener(ITnBatteryListener batteryListener)
    {
        listeners.addElement(batteryListener);
    }

    public int getBatteryLevel()
    {
        synchronized (this)
        {
            try
            {
                long time = System.currentTimeMillis();
                while (batteryLevel == -1 && (System.currentTimeMillis() - time) < 2000)
                {
                    try
                    {
                        this.wait(50);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }

            return batteryLevel;
        }
    }

    public void removeListener(ITnBatteryListener batteryListener)
    {
        listeners.removeElement(batteryListener);
    }

    private void registerReceiver()
    {
        listenerReceiver = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (Intent.ACTION_BATTERY_CHANGED.equals(action))
                {
                    int intStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
                    int intLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    int intScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                    batteryLevel = intLevel * 100 / intScale;
                    
                    for (int i = 0; i < listeners.size(); i++)
                    {
                        ITnBatteryListener listener = (ITnBatteryListener) listeners.elementAt(i);

                        int tnStatus = ITnBatteryListener.BATTERY_STATUS_UNKNOWN;
                        switch (intStatus)
                        {
                            case BatteryManager.BATTERY_STATUS_CHARGING:
                                tnStatus = ITnBatteryListener.BATTERY_STATUS_CHARGING;
                                break;
                        }
                        
                        try
                        {
                            listener.batteryStatusChange(tnStatus, batteryLevel);
                        }
                        catch(Throwable e)
                        {
                            Logger.log(this.getClass().getName(), e);
                        }
                    }
                }
                else if (Intent.ACTION_BATTERY_LOW.equals(action))
                {
                    for (int i = 0; i < listeners.size(); i++)
                    {
                        ITnBatteryListener listener = (ITnBatteryListener) listeners.elementAt(i);
                        listener.batteryLow();
                    }
                }
                else if (Intent.ACTION_BATTERY_OKAY.equals(action))
                {
                    for (int i = 0; i < listeners.size(); i++)
                    {
                        ITnBatteryListener listener = (ITnBatteryListener) listeners.elementAt(i);
                        listener.batteryGood();
                    }
                }
            }
        };
        
        this.context.registerReceiver(listenerReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.context.registerReceiver(listenerReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        this.context.registerReceiver(listenerReceiver, new IntentFilter(Intent.ACTION_BATTERY_OKAY));
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
    
    private void unregisterReceiver()
    {
        if(listenerReceiver != null)
        {
            this.context.unregisterReceiver(listenerReceiver);
            listenerReceiver = null;
        }
    }
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AndroidCarHookReceiver.java
 *
 */
package com.telenav.carconnect.host;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.telenav.logger.Logger;

/**
 * Broadcast receiver to monitor the bluetooth connection of Ford applink device
 * 
 * @author chihlh
 * @date Feb 22, 2012
 */
public class AndroidCarHookReceiver extends BroadcastReceiver
{

    private final static String TAG = "CarConnect";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        String action = intent.getAction();
        if (bluetoothDevice == null || action == null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Return here because bluetoothDevice or action is null");
            return;
        }

        String deviceName = bluetoothDevice.getName();
        if (deviceName == null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Return here because bluetoothDevice Name is null");
            return;
        }

        Log.i(TAG, "CarConnect: BlueTooth Event: " + action + ", " + deviceName + ", " + bluetoothDevice.getAddress());

        if (action.compareTo(BluetoothDevice.ACTION_ACL_CONNECTED) == 0)
        {

            if (deviceName.contains("SYNC"))
            {
                Intent serviceIntent = new Intent(context, AndroidCarConnectionService.class);
                serviceIntent.putExtra(AndroidCarConnectionService.EXTRA_NAME, AndroidCarConnectionService.START_CARCONNECT);
                context.startService(serviceIntent);
            }
        }
        else if (action.compareTo(BluetoothDevice.ACTION_ACL_DISCONNECTED) == 0)
        {
            if (deviceName.contains("SYNC"))
            {
                Intent serviceIntent = new Intent(context, AndroidCarConnectionService.class);
                serviceIntent.putExtra(AndroidCarConnectionService.EXTRA_NAME, AndroidCarConnectionService.STOP_CARCONNECT);
                context.startService(serviceIntent);
            }

        }

    }
}

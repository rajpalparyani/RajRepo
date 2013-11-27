/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AndroidCanConnectionService.java
 *
 */
package com.telenav.carconnect.host;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * @author chihlh
 * @date Feb 22, 2012
 */
public class AndroidCarConnectionService extends IntentService
{

    public AndroidCarConnectionService()
    {
        super("CarConnectionService");
    }

    private final static String Tag = "CarConnect";

    public final static String EXTRA_NAME = "comt.telenav.app.android.ACTION";

    public final static int START_CARCONNECT = 0;

    public final static int STOP_CARCONNECT = 1;

    @Override
    protected void onHandleIntent(Intent intent)
    {
        int action = intent.getIntExtra(EXTRA_NAME, -1);
        // Log.i(Tag, "Service Intent received action = " + action);
        switch (action)
        {
            case START_CARCONNECT:
                startCarConnectHost();
                break;
            case STOP_CARCONNECT:
                stopCarConnectHost();
                this.stopSelf(); // stop the service if bluetooth connection is disconnected
                break;
        }
    }

    private void startCarConnectHost()
    {
        Log.i(Tag, "CarConnect: Starting CarConnect monitor");
        startForeground(0, null);
        CarConnectHostManager.getInstance().startMonitor(this);
    }

    private void stopCarConnectHost()
    {
        // Log.i(Tag, "Stopping CarConnect monitor");
        // CarConnectHostManager.getInstance().stopMonitor(); due to there might be several start/stop event when phone
        // is near HU, we just disable the clean-up
        CarConnectHostManager.getInstance().setBluetoothState(false);
        stopForeground(false);
    }

}

/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * CarConnectRestartService.java
 *
 */
package com.telenav.carconnect.host;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.IntentService;
import android.content.Intent;

/**
 * @author jpwang
 * @date 2013-5-13
 */
public class CarConnectRestartService extends IntentService
{
    public final static String EXTRA_NAME = "com.telenav.app.android.ACTION";

    public final static int RESTART = 0;

    public CarConnectRestartService()
    {
        super("CarConnectRestartService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        int action = intent.getIntExtra(EXTRA_NAME, -1);
        switch (action)
        {
            case RESTART:
                restartCarConnect();
                break;
        }
        this.stopSelf();
    }

    private void restartCarConnect()
    {
        boolean isRestarted = false;
        try
        {
            do
            {
                Thread.sleep(2000);
                if (isCanRestarted())
                {
                    Intent serviceIntent = new Intent(this.getBaseContext(), AndroidCarConnectionService.class);
                    serviceIntent
                            .putExtra(AndroidCarConnectionService.EXTRA_NAME, AndroidCarConnectionService.START_CARCONNECT);
                    this.getBaseContext().startService(serviceIntent);
                    isRestarted = true;
                }
            } while (!isRestarted);
        }
        catch (InterruptedException e)
        {
        }
    }

    private boolean isCanRestarted()
    {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (RunningAppProcessInfo runningAppProcessInfo : infos)
        {
            if (runningAppProcessInfo.processName.equalsIgnoreCase(this.getPackageName()))
            {
                return false;
            }
        }
        return true;
    }
}
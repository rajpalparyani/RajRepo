/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfService.java
 *
 */
package com.telenav.app.android.scout_us;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.telenav.dwf.aidl.DwfAidlBinder;

/**
 *@author fangquanm
 *@date Jul 9, 2013
 */
public class DwfService extends Service
{
    private DwfAidlBinder mbinder;
    
    private ServiceHandler mServiceHandler;

    private final static class ServiceHandler extends Handler
    {
        public ServiceHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        
        mbinder = new DwfAidlBinder(this);
        
        HandlerThread thread = new HandlerThread("DwfService starting thread", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceHandler = new ServiceHandler(thread.getLooper());
        
        Message msg = mServiceHandler.obtainMessage();
        msg.obj = this.getApplicationContext();
        
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mbinder;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}

package com.telenav.navservice.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NavServiceTN extends Service 
{
    private NavServiceImpl impl;
    
    public void onCreate()
    {
        super.onCreate();
        impl = new NavServiceImpl(this);
    }
    
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return impl.onStartCommand(intent, flags, startId);
    }
    
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
}

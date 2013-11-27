/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfServiceConnection.java
 *
 */
package com.telenav.dwf.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.telenav.app.android.scout_us.DwfService;

/**
 *@author fangquanm
 *@date Jul 9, 2013
 */
public class DwfServiceConnection implements ServiceConnection
{
    private static DwfServiceConnection connection = new DwfServiceConnection();
    
    private DwfServiceConnection()
    {
        
    }
    
    public static DwfServiceConnection getInstance()
    {
        return connection;
    }
    
    private DwfAidl dwfAidl;
    
    public void startDwfService(Context context)
    {
        Intent intent = new Intent();

        intent.setClass(context, DwfService.class);

        context.bindService(intent, DwfServiceConnection.getInstance(), Context.BIND_AUTO_CREATE);
    }
    
    public DwfAidl getConnection()
    {
        return dwfAidl;
    }
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        dwfAidl = DwfAidl.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        dwfAidl = null;
    }
}

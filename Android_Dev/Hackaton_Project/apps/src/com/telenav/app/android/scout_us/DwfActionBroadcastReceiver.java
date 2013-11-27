/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfActionBroadcastReceiver.java
 *
 */
package com.telenav.app.android.scout_us;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;

/**
 *@author jpwang
 *@date 2013-8-6
 */
public class DwfActionBroadcastReceiver extends BroadcastReceiver
{

    private final static String DWF_UPDATE_ACTION = "com.telenav.intent.action.DWF_UPDATE_ACTION";
    
    private final static String DWF_ACTION_IS_STOPPED = "isDwfStopped";
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (!DWF_UPDATE_ACTION.equals(action))
        {
            return;
        }
        boolean isStopped = intent.getBooleanExtra(DWF_ACTION_IS_STOPPED, false);
        updateDwfAction(isStopped);
    }
    
    private void updateDwfAction(boolean isStopped)
    {
        DwfAidl aidl = DwfServiceConnection.getInstance().getConnection();
        try
        {
            if (aidl != null && aidl.getSharingIntent() != null)
            {
                if (isStopped)
                {
                    aidl.pauseShareLocation();
                }
                else
                {
                    aidl.resumeShareLocation();
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}

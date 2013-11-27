/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ExitAppReceiver.java
 *
 */
package com.telenav.app.android.scout_us;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-27
 */
public class ExitAppReceiver extends BroadcastReceiver 
{

    public void onReceive(Context context, Intent intent) 
    {
        if (!intent.getAction().equals("com.telenav.intent.action.EXIT_APP"))
        {
            return;
        }
        
        if (TeleNavDelegate.getInstance().isStarted())
        {
            DaoManager.saveInternalRMS();
            Bundle bundle = intent.getExtras();
            if (bundle == null || bundle.getBoolean("forceExitQuickly", true))
            {
                /**
                 * FIXME We use killProcess instead of exitApp flow here,
                 * because MaiTaiAdapter send EXIT_APP intent will cause
                 * System ActivityManager destroy timeout issue and launch
                 * timeout issue.
                 * Fix Bug: http://jira.telenav.com:8080/browse/TN-3459
                 */
//                android.os.Process.killProcess(android.os.Process.myPid());
                TeleNavDelegate.getInstance().exitApp(true, null);
            }
            else
            {
                TeleNavDelegate.getInstance().exitApp(false, null);
            }
        }
    }

}

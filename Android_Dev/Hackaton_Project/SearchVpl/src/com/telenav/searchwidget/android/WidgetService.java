/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WidgetService.java
 *
 */
package com.telenav.searchwidget.android;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.telenav.searchwidget.app.android.SearchApp;
import com.telenav.searchwidget.flow.android.SearchController;
import com.telenav.searchwidget.framework.android.WidgetParameter;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 19, 2011
 */

public class WidgetService extends Service
{
    private static boolean isFirstRun = true;
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
        SearchApp.getInstance().init(getApplicationContext());
       
        if (intent == null)
        {
            return START_NOT_STICKY;
        }
        else
        {
            Bundle bundle = intent.getExtras();
            if (bundle == null)
            {
                return START_NOT_STICKY;
            }
            
            WidgetParameter wp = WidgetParameter.fromBundle(bundle);
            
            new WidgetActionTask(wp).start();
            
            if (isFirstRun)
            {
                isFirstRun = false;
                return START_STICKY;
            }
            
            return START_NOT_STICKY;
        }
    }

}

class WidgetActionTask extends Thread
{
    private WidgetParameter wp;
    
    public WidgetActionTask(WidgetParameter wp)
    {
        this.wp = wp;
    }
    
    public void run()
    {
        SearchController.getInstance().handleWidgetAction(wp);
    }
}

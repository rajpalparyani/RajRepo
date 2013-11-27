/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WidgetBroadcastReceiver.java
 *
 */
package com.telenav.searchwidget.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.telenav.searchwidget.app.android.SearchApp;
import com.telenav.searchwidget.flow.android.IWidgetConstants;
import com.telenav.searchwidget.flow.android.SearchController;
import com.telenav.searchwidget.framework.android.WidgetManager;
import com.telenav.searchwidget.framework.android.WidgetParameter;

/**
 *@author hchai
 *@date 2011-8-4
 */
public class WidgetBroadcastReceiver extends BroadcastReceiver
{
	private static final String ACTION_REFRESH_WIDGET = "com.telenav.searchwidget.action.refreshwidget";
	
    public WidgetBroadcastReceiver()
    {
    }
    
    public void onReceive(Context context, Intent intent)
    {
        SearchApp.getInstance().init(context.getApplicationContext());
        
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())
        		|| ACTION_REFRESH_WIDGET.equals(intent.getAction()))
        {
            int[] ids = WidgetManager.getInstance().getAppWidgets();
            if(ids != null)
            {
	            for (int i = 0; i < ids.length; i ++)
	            {
	                WidgetParameter wp = new WidgetParameter(ids[i], WidgetManager.getInstance().getLayoutId(ids[i]), IWidgetConstants.ACTION_REFRESH_WIDGET);
	                SearchController.getInstance().handleWidgetAction(wp);
	            }
            }
        }
    }

}

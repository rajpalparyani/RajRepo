/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SearchWidgetProviderMini.java
 *
 */
package com.telenav.searchwidget.android;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import com.telenav.searchwidget.flow.android.IWidgetConstants;
import com.telenav.searchwidget.flow.android.SearchController;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 24, 2011
 */

public class SearchWidgetProviderMini extends SearchWidgetProvider
{
    public void onUpdateDelegate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        final int count = appWidgetIds.length;        
        for (int i = 0; i < count; i++)
        {
            SearchController.getInstance().init(
                    IWidgetConstants.CONTROLLER_EVENT_LAUNCH_MINI_SEARCH,
                    appWidgetIds[i]);
        }        
    }
    
    protected void onEnabledDelegate(Context context) 
    {
    }
    
    protected void onDisabledDelegate(Context context) 
    {
    }
    
    protected void onDeletedDelegate(Context context, int[] appWidgetIds)
    {
    }
    
}

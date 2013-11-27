/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SearchWidgetProviderFull.java
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

public class SearchWidgetProviderFull extends SearchWidgetProvider
{
    public void onUpdateDelegate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        final int count = appWidgetIds.length;
        
        for (int i = 0; i < count; i++)
        {
            SearchController.getInstance().init(
                    IWidgetConstants.CONTROLLER_EVENT_LAUNCH_FULL_VIEW, 
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

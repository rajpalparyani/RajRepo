/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SearchWidgetProvider.java
 *
 */
package com.telenav.searchwidget.android;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.telenav.searchwidget.app.android.SearchApp;
import com.telenav.searchwidget.framework.android.WidgetManager;
import com.telenav.searchwidget.res.android.AndroidImageManager;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 26, 2011
 */

public abstract class SearchWidgetProvider extends AppWidgetProvider
{
    public final void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        SearchApp.getInstance().init(context.getApplicationContext());
        
        onUpdateDelegate(context.getApplicationContext(), appWidgetManager, appWidgetIds);
    }
    
    public final void onEnabled(Context context) 
    {
        Context appContext = context.getApplicationContext();
        SearchApp.getInstance().init(appContext);
        onEnabledDelegate(appContext);        
    }
    
    public final void onDisabled(Context context) 
    {
        Context appContext = context.getApplicationContext();
        
        onDisabledDelegate(appContext);
        SearchApp.getInstance().stopApp();
        
    }
    
    public final void onDeleted(Context context, int[] appWidgetIds)
    {
        if (appWidgetIds != null && appWidgetIds.length > 0)
        {
            for (int i = 0; i < appWidgetIds.length; i ++)
            {
                WidgetManager.getInstance().unregisterWidget(appWidgetIds[i]);
                AndroidImageManager.getInstance().removeBitmap(appWidgetIds[i]);
            }
        }
        
        onDeletedDelegate(context.getApplicationContext(), appWidgetIds);
    }

    protected abstract void onUpdateDelegate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds);
    
    protected abstract void onEnabledDelegate(Context context); 
    
    protected abstract void onDisabledDelegate(Context context); 
    
    protected abstract void onDeletedDelegate(Context context, int[] appWidgetIds);
}

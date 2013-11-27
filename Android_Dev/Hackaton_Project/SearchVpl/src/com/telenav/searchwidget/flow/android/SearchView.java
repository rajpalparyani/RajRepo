/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SearchView.java
 *
 */
package com.telenav.searchwidget.flow.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.widget.RemoteViews;

import com.telenav.app.android.cingular.R;
import com.telenav.i18n.ResourceBundle;
import com.telenav.searchwidget.android.OneBoxActivity;
import com.telenav.searchwidget.data.datatypes.address.Stop;
import com.telenav.searchwidget.framework.android.WidgetManager;
import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.framework.android.WidgetViewStub;
import com.telenav.searchwidget.res.IStringSearchWidget;
import com.telenav.searchwidget.res.ResUtil;
import com.telenav.searchwidget.res.ResourceManager;
import com.telenav.searchwidget.res.android.BitmapUri;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 25, 2011
 */

class SearchView
{
    private static final String ACTION_SERVICE          = "com.telenav.searchwidget.action.service";
    private static final String ACTION_SET_ADDRESS      = "com.telenav.searchwidget.action.setaddress";
    
    private static final String DATA_SCHEME             = "widget";
    private static final String DATA_HOST               = "widget.telenav.com";
    private static final String DATA_PATH_PREFIX        = "service";
    
    private Context context;
    private ResourceBundle bundle;
    
    public SearchView(Context context)
    {
        this.context = context;
        
        bundle = ResourceManager.getInstance().getCurrentBundle();
    }
    
    public WidgetViewStub createMiniSearchView(int widgetId)
    {
        int layoutId = getMiniSearchLayoutId();
        RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
        
        WidgetViewStub stub = new WidgetViewStub();        
        stub.widgetId = widgetId;
        stub.remoteViews = views;
        stub.layoutId = layoutId;        

        int viewId = R.id.cat;        
        views.setTextViewText(R.id.input, ResUtil.getOneboxHint());

//        int color = context.getResources().getColor(R.color.oneBoxSearch);
//        views.setTextColor(R.id.input, color);      
        WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_SWITCH_TO_MINI_CAT);
        Intent intent = getPendingIntent(wp, viewId, ACTION_SERVICE);
        setOnClickService(views, viewId, intent);
        
        intent = new Intent(context, OneBoxActivity.class);
        intent.setPackage(context.getPackageName());
        intent.putExtra(IWidgetConstants.KEY_WIDGET_ID, widgetId);
        intent.putExtra(IWidgetConstants.KEY_LAYOUT_ID, layoutId);
        setOnClickActivity(views, R.id.onebox, intent);
        
        return stub;
    }
    
    public WidgetViewStub createMiniCatView(int widgetId)
    {
        int layoutId = getMiniCatLayoutId();
        RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
        
        WidgetViewStub stub = new WidgetViewStub();        
        stub.widgetId = widgetId;
        stub.remoteViews = views;
        stub.layoutId = layoutId;
                
        int viewId = R.id.search;        
        
        WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_SWITCH_TO_MINI_SEARCH);
        Intent intent = getPendingIntent(wp, viewId, ACTION_SERVICE);
        setOnClickService(views, viewId, intent);
        
        setOnClickCategoryService(stub);
        
        return stub;        
    }
    
    public WidgetViewStub createHalfView(int widgetId, Stop home, Stop work)
    {
        int layoutId = getHalfLayoutId();        
        RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
        
        WidgetViewStub stub = new WidgetViewStub();        
        stub.widgetId = widgetId;
        stub.remoteViews = views;
        stub.layoutId = layoutId;
        
        String driveFrom = bundle.getString(IStringSearchWidget.RES_DRIVE_FROM, 
                IStringSearchWidget.FAMILY_SEARCHWIDGET);
        views.setTextViewText(R.id.drivefrom, driveFrom);
        
        createAddrLayout(views, widgetId, R.id.home_container, home, true);        
        createAddrLayout(views, widgetId, R.id.work_container, work, false);
        
        Intent intent = new Intent(context, OneBoxActivity.class);
        intent.setPackage(context.getPackageName());
        intent.putExtra(IWidgetConstants.KEY_WIDGET_ID, widgetId);
        intent.putExtra(IWidgetConstants.KEY_LAYOUT_ID, layoutId);
        setOnClickActivity(views, R.id.onebox, intent);
        
        int viewId = R.id.refresh;
        WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_REFRESH_WIDGET);
        intent = getPendingIntent(wp, viewId, ACTION_SERVICE);
        setOnClickService(views, viewId, intent);
        
        context.startService(intent);
        
        return stub;
    }
    
    public WidgetViewStub createFullView(int widgetId, Stop home, Stop work)
    {        
        int layoutId = getFullLayoutId();        
        RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
        
        WidgetViewStub stub = new WidgetViewStub();
        stub.widgetId = widgetId;
        stub.remoteViews = views;
        stub.layoutId = layoutId;
        
        String around = bundle.getString(IStringSearchWidget.RES_AROUND, 
                IStringSearchWidget.FAMILY_SEARCHWIDGET);
        views.setTextViewText(R.id.around, around);
  
        views.setTextViewText(R.id.input, ResUtil.getOneboxHint());
        
//        int color = context.getResources().getColor(R.color.oneBoxSearch);
//        views.setTextColor(R.id.input, color);
        createAddrLayout(views, widgetId, R.id.home_container, home, true);        
        createAddrLayout(views, widgetId, R.id.work_container, work, false);
        
        Intent intent = new Intent(context, OneBoxActivity.class);
        intent.setPackage(context.getPackageName());
        intent.putExtra(IWidgetConstants.KEY_WIDGET_ID, widgetId);
        intent.putExtra(IWidgetConstants.KEY_LAYOUT_ID, layoutId);
        setOnClickActivity(views, R.id.onebox, intent);

        setOnClickCategoryService(stub);
        
        
        int viewId = R.id.refresh;
        WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_REFRESH_WIDGET);
        intent = getPendingIntent(wp, viewId, ACTION_SERVICE);
        setOnClickService(views, viewId, intent);
        
        context.startService(intent);
        
        return stub;
    }
    
    public void updateAddress(WidgetViewStub stub, String addr)
    {
        stub.remoteViews.setTextViewText(R.id.Address, addr);
        
        showView(stub);
    }    

    public void updateProgress(WidgetViewStub stub)
    {
        stub.remoteViews.removeAllViews(R.id.mapContainer);
        RemoteViews mapView = new RemoteViews(context.getPackageName(), R.layout.searchwidget_progress);
        stub.remoteViews.addView(R.id.mapContainer, mapView);
        
        showView(stub);
    }
    
    public void updateMap(WidgetViewStub stub, BitmapUri bitmapUri)
    {
        stub.remoteViews.removeAllViews(R.id.mapContainer);
        RemoteViews mapView = new RemoteViews(context.getPackageName(), R.layout.searchwidget_map);
        stub.remoteViews.addView(R.id.mapContainer, mapView);

        boolean hasBitmap = false;
        if (bitmapUri != null && bitmapUri.uri != null)
        {
            mapView.setImageViewUri(R.id.map, bitmapUri.uri);
            hasBitmap = true;
        }
        else if (bitmapUri != null && bitmapUri.image != null)
        {
            mapView.setImageViewBitmap(R.id.map, bitmapUri.image);
            hasBitmap = true;
        }
                
        if (hasBitmap)
        {
            int viewId = R.id.map;
            WidgetParameter wp1 = new WidgetParameter(stub.widgetId, stub.layoutId, IWidgetConstants.ACTION_CLICK_MAP);
            Intent intent = getPendingIntent(wp1, viewId, ACTION_SERVICE);
            setOnClickService(stub.remoteViews, R.id.map, intent);
        }
        
        showView(stub);
    }
    
    public void updateEta(WidgetViewStub stub, boolean isHome, int hours, int minutes, int delay, int color)
    {
        String hourStr = transformTime(hours);
        String minuteStr = transformTime(minutes);
        if (isHome)
        {
            stub.remoteViews.setTextColor(R.id.etaHomeHour, color);
            stub.remoteViews.setTextColor(R.id.homeHourStr, color);
            stub.remoteViews.setTextColor(R.id.etaHomeMin, color);
            
            stub.remoteViews.setTextViewText(R.id.etaHomeHour, hourStr);
            stub.remoteViews.setTextViewText(R.id.etaHomeMin, minuteStr);
        }
        else
        {
            stub.remoteViews.setTextColor(R.id.etaWorkHour, color);
            stub.remoteViews.setTextColor(R.id.workHourStr, color);
            stub.remoteViews.setTextColor(R.id.etaWorkMin, color);
                
            stub.remoteViews.setTextViewText(R.id.etaWorkHour, hourStr);
            stub.remoteViews.setTextViewText(R.id.etaWorkMin, minuteStr);     
        }
        
        showView(stub);
    }

	private String transformTime(int hours) {
		String str = "";
		if (hours <= 0)
        {
            str = "00";
        }
        else if (hours < 10)
        {
        	str = "0" + hours;
        }
        else
        {
        	str += hours; 
        }
		return str;
	}
    
    public void showSetAddress(WidgetParameter wp)
    {
        Intent intent = getPendingIntent(wp, -1, ACTION_SET_ADDRESS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
    
    public void showView(WidgetViewStub stub)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(stub.widgetId, stub.remoteViews);
        
        WidgetManager.getInstance().registerWidget(stub.widgetId, stub);
    }
    
    private void createAddrLayout(RemoteViews views, int widgetId, int containerId, Stop addr, boolean isHome)
    {
        views.removeAllViews(containerId);
        
        if (addr == null)
        {
            int layoutId = R.layout.searchwidget_setup_home;
            if (!isHome)
            {
                layoutId = R.layout.searchwidget_setup_work;
            }
            RemoteViews childView = new RemoteViews(context.getPackageName(), layoutId);
            views.addView(containerId, childView);
            
            int viewId = R.id.set_up;
            String text = bundle.getString(IStringSearchWidget.RES_SETUP_HOME, 
                    IStringSearchWidget.FAMILY_SEARCHWIDGET);
            childView.setTextViewText(viewId, text);
            String addrNameStr = bundle.getString(IStringSearchWidget.RES_HOME_STR, 
            		IStringSearchWidget.FAMILY_SEARCHWIDGET);
            viewId = R.id.home;
            if (!isHome)
            {
            	addrNameStr = bundle.getString(IStringSearchWidget.RES_WORK_STR, 
            			IStringSearchWidget.FAMILY_SEARCHWIDGET);
            	viewId = R.id.work;
            }
            childView.setTextViewText(viewId, addrNameStr);

            WidgetParameter wp = new WidgetParameter(widgetId, views.getLayoutId(), IWidgetConstants.ACTION_NAVIGATE);
            wp.putBoolean(IWidgetConstants.KEY_IS_HOME, isHome);
            wp.putBoolean(IWidgetConstants.KEY_IS_STOP_SET, false);
            Intent intent = getPendingIntent(wp, viewId, ACTION_SERVICE);

            setOnClickService(views, viewId, intent);            
        }
        else
        {
            int layoutId = R.layout.searchwidget_eta_home;
            if (!isHome)
            {
                layoutId = R.layout.searchwidget_eta_work;
            }
            RemoteViews childView = new RemoteViews(context.getPackageName(), layoutId);
            views.addView(containerId, childView);
            
            String toStr = bundle.getString(IStringSearchWidget.RES_TIME_TO_HOME, 
                    IStringSearchWidget.FAMILY_SEARCHWIDGET);
            if (!isHome)
            {
            	toStr = bundle.getString(IStringSearchWidget.RES_TIME_TO_WORK, 
                    IStringSearchWidget.FAMILY_SEARCHWIDGET);
            }
            String hourStr = ":";
            
            childView.setTextViewText(R.id.toStr, toStr);
            if (isHome)
            {
                childView.setTextViewText(R.id.homeHourStr, hourStr);
            }
            else
            {
                childView.setTextViewText(R.id.workHourStr, hourStr);
            }
            
            int viewId = R.id.etaHome;
            if (!isHome)
            {
                viewId = R.id.etaWork;
            }
            
            WidgetParameter wp = new WidgetParameter(widgetId, views.getLayoutId(), IWidgetConstants.ACTION_NAVIGATE);
            wp.putBoolean(IWidgetConstants.KEY_IS_STOP_SET, true);
            wp.putBoolean(IWidgetConstants.KEY_IS_HOME, isHome);
            Intent intent = getPendingIntent(wp, viewId, ACTION_SERVICE);
            setOnClickService(views, viewId, intent);
        }
    }
    
    private void setOnClickService(RemoteViews views, int viewId, Intent intent)
    {
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(viewId, pendingIntent);
    }

    private void setOnClickActivity(RemoteViews views, int viewId, Intent intent)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(viewId, pendingIntent);
    }
    
    private void setOnClickCategoryService(WidgetViewStub stub)
    {
        int[] viewIds = new int[] {
                R.id.coffee,
                R.id.food,
                R.id.gas,
                R.id.parking,
                R.id.atm
        };
        
        for (int i = 0; i < viewIds.length; i ++)
        {
            WidgetParameter wp = new WidgetParameter(stub.widgetId, stub.layoutId, IWidgetConstants.ACTION_CAT_SEARCH);
            Intent intent = getPendingIntent(wp, viewIds[i], ACTION_SERVICE);
            setOnClickService(stub.remoteViews, viewIds[i], intent);
        }
    }
    
    //If we provide the same action and category to get the pending intent,
    //Android platform will consider it the same intent and will replace the
    //previous one. That's why we need provide different data into PendingIntent
    //and get different PendingIntent instance.
    private Intent getPendingIntent(WidgetParameter wp, int viewId, String action)
    {
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(action);
        
        StringBuffer sb = new StringBuffer();
        sb.append(DATA_SCHEME).append("://")
            .append(DATA_HOST).append("/")
            .append(DATA_PATH_PREFIX).append("/")
            .append("widgetId=").append(wp.getWidgetId()).append("&")
            .append("layoutId=").append(wp.getLayoutId()).append("&")
            .append("viewId=").append(viewId).append("&")
            .append("action=").append(wp.getAction());
        intent.setData(Uri.parse(sb.toString()));
        
        wp.putInt(IWidgetConstants.KEY_VIEW_ID, viewId);
        intent.putExtras(WidgetParameter.toBundle(wp));
        
        return intent;
    }
    
    private int getMiniSearchLayoutId()
    {
    	return getOrientation() == Configuration.ORIENTATION_LANDSCAPE 
    				? R.layout.searchwidget_mini_search_land
    				: R.layout.searchwidget_mini_search;
    }

    private int getMiniCatLayoutId()
    {
    	return getOrientation() == Configuration.ORIENTATION_LANDSCAPE 
    				? R.layout.searchwidget_mini_cat_land
    				: R.layout.searchwidget_mini_cat;
    }

    private int getHalfLayoutId()
    {
    	return getOrientation() == Configuration.ORIENTATION_LANDSCAPE 
    				? R.layout.searchwidget_half_land
    				: R.layout.searchwidget_half;
    }
    
    private int getFullLayoutId()
    {
    	return getOrientation() == Configuration.ORIENTATION_LANDSCAPE 
    				? R.layout.searchwidget_full_land
    				: R.layout.searchwidget_full;
    }
    
    private int getOrientation()
    {
    	return this.context.getResources().getConfiguration().orientation;
    }
}

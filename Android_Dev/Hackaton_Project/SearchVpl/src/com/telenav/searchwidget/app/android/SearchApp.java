/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SearchApp.java
 *
 */
package com.telenav.searchwidget.app.android;

import java.util.Hashtable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import com.telenav.searchwidget.android.WidgetBroadcastReceiver;
import com.telenav.searchwidget.app.AbstractPlatformIniter;
import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.searchwidget.data.AddressDao;
import com.telenav.searchwidget.flow.android.IWidgetConstants;
import com.telenav.searchwidget.flow.android.SearchController;
import com.telenav.searchwidget.framework.android.WidgetManager;
import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.gps.android.AndroidGpsProvider;
import com.telenav.searchwidget.gps.android.LocationDao;
import com.telenav.searchwidget.res.ResourceManager;
import com.telenav.searchwidget.res.android.AndroidImageManager;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 20, 2011
 */

public class SearchApp
{
    private static SearchApp instance = new SearchApp();
    
    private BroadcastReceiver configReceiver;
    
    private Hashtable screenOnReceivers = new Hashtable(); 
    
    private SearchApp()
    {
    }
    
    public static SearchApp getInstance()
    {
        return instance;
    }
    
    public void init(Context context)
    {
        initRes(context);
        registerBacklightReceiver(context);
        registerConfigReceiver(context);
    }
    
    public void startApp(Context context)
    {
    }
    
    public void stopApp()
    {
    }
    
    private synchronized void initRes(Context context)
    {
        AndroidPersistentContext.getInstance().init(context);
        
        if (AndroidPersistentContext.getInstance().getApplicationSQLiteDatabase() == null)
        {
            AndroidPersistentContext.getInstance().openSQLiteDatabase();
        }
        
        if(AbstractPlatformIniter.getInstance() == null)
        {
            AbstractPlatformIniter.init(new AndroidPlatformIniter());
        }
        
        AbstractPlatformIniter.getInstance().initUi();
        AbstractPlatformIniter.getInstance().initIo();
        AbstractPlatformIniter.getInstance().initNio();
        AbstractPlatformIniter.getInstance().initNetwork();
        AbstractPlatformIniter.getInstance().initPersistent();
        AbstractPlatformIniter.getInstance().initLocation();
        
        AppConfigHelper.load();
        
        AddressDao.getInstance().setRootPath(context.getFilesDir().getAbsolutePath());
        LocationDao.getInstance().setRootPath(context.getFilesDir().getAbsolutePath());
        
        WidgetManager.getInstance().setRootPath(context.getFilesDir().getAbsolutePath());
        
        AndroidImageManager.getInstance().setRootPath(Environment.getDownloadCacheDirectory().getAbsolutePath());
        
        AndroidGpsProvider.getInstance().init(context);
        
        String tnLocale = LocationDao.getInstance().getTnLocale();
        if (null == tnLocale)
        {
        	setDefaultLocale(context);
        }
        else
        {
        	ResourceManager.getInstance().setLocale(tnLocale);
        }
    }
    
    private void registerBacklightReceiver(Context context)
    {
        if (!screenOnReceivers.containsKey(context))
        {
            WidgetBroadcastReceiver broadcastReceiver = new WidgetBroadcastReceiver();
            screenOnReceivers.put(context, broadcastReceiver);
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            context.registerReceiver(broadcastReceiver, filter);
        }

    }
    
    private void registerConfigReceiver(Context context)
    {
    	if (configReceiver != null) return;
    	
    	configReceiver = new BroadcastReceiver() 
    	{
    		public void onReceive(Context context, Intent intent)
    		{
    			SearchApp.getInstance().init(context.getApplicationContext());
    	        if (Intent.ACTION_CONFIGURATION_CHANGED.equals(intent.getAction()))
    	        {
    	            int[] ids = WidgetManager.getInstance().getAppWidgets();
    	            if(ids != null)
    	            {
	    	            for (int i = 0; i < ids.length; i ++)
	    	            {
	    	                WidgetParameter wp = new WidgetParameter(ids[i], WidgetManager.getInstance().getLayoutId(ids[i]), IWidgetConstants.ACTION_ORIENTATION_CHANGED);
	    	                SearchController.getInstance().handleWidgetAction(wp);
	    	            }
    	            }
    	        }
    		};
    	};
    	
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);
		context.registerReceiver(configReceiver, intentFilter);

    }
    
    protected void setDefaultLocale(Context context)
    {
        String deviceLocale = getDeviceLocale(context);
        if (deviceLocale == null || deviceLocale.length() == 0)
        {
            deviceLocale = AppConfigHelper.defaultLocale;
        }
        
        ResourceManager.getInstance().setLocale(deviceLocale);
    }
    
    protected String getDeviceLocale(Context context)
    {
        String localeLanguage = context.getResources().getConfiguration().locale.getLanguage();
        return getDeviceLocale(localeLanguage);
    }
    
    protected String getDeviceLocale(String localeLanguage)
    {
        if (localeLanguage != null && localeLanguage.length() > 0)
        {
            String localesList = AppConfigHelper.localesList;
            
            int pos = -1;
            while (true)
            {
                pos = localesList.indexOf(',');
                if (pos == -1)
                {
                    pos = localesList.length();
                }
                
                String tmpLocale = localesList.substring(0, pos);
                if (tmpLocale.indexOf(localeLanguage) != -1)
                {
                    return tmpLocale;
                }
                
                if (pos+1 < localesList.length() - 1)
                {
                    localesList = localesList.substring(pos+1);
                }
                else
                {
                    break;
                }
            }
        }
        
        return "";
    }
}

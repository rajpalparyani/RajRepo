/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MaiTaiHandler.java
 *
 */
package com.telenav.searchwidget.flow.android;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.telenav.app.android.cingular.R;
import com.telenav.location.TnLocation;
import com.telenav.sdk.maitai.IMaiTaiAdapter;
import com.telenav.sdk.maitai.IMaiTaiAdapterCallback;
import com.telenav.sdk.maitai.MaiTaiAdapterFactory;
import com.telenav.sdk.maitai.android.AndroidMaiTaiContext;
import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.searchwidget.app.android.AndroidPersistentContext;
import com.telenav.searchwidget.data.datatypes.address.Stop;
import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.gps.android.AndroidGpsProvider;
import com.telenav.searchwidget.res.ResUtil;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 29, 2011
 */

public class MaiTaiHandler extends AbstractHandler implements IMaiTaiAdapterCallback
{
    private final static String MAITAI_CALLBACK     = "telenav/searchwidget";
    
    private final static String MAITAI_ACTION       = "com.telenav.intent.action.maitai";
    private final static String MAITAI_CATEGORY     = "com.telenav.intent.category.MaiTai";

    private final static String VPL_ACTION          = "com.telenav.searchwidget.action.launchvpl";
    private final static String VPL_CATEGORY        = "com.telenav.searchwidget.category.vpl";
    
    private static final String KEY_VERSION         = "v";
    private static final String KEY_CONTEXT         = "c";
    private static final String KEY_CALLBACK        = "cb";
    private static final String KEY_DEVELOPER_KEY   = "k";
    private static final String KEY_NAMEDADDR       = "namedAddr";
    private static final String KEY_ADDR2           = "addr2";
    private static final String KEY_NAV_TYPE        = "type";
    private static final String KEY_CAT_NAME        = "cat";
    private static final String KEY_TERM            = "term";
    private static final String KEY_LAT             = "lat";
    private static final String KEY_LON             = "lon";
    private static final String KEY_WIDGET_TYPE   = "widgettype";
    //maitai 2.0
    private static final String KEY_NAMEDADDR2      = "namedAddr2";
    private static final String KEYWORD_HOME        = "HOME";
    private static final String KEYWORD_OFFICE      = "OFFICE";
    private static final String KEYWORD_ADDRESS_TYPE = "addressType";
    
    private static final String KEYWORD_CURRENT     = "CURRENT";
    private static final String KEYWORD_NAV         = "NAV";
    
    private static final String MAITAI_MAP          = "map";
    private static final String MAITAI_SEARCH       = "search";
    private static final String MAITAI_DRIVETO      = "driveTo";
    private static final String MAITAI_SET_ADDRESS  = "setAddress";
    
    private static final String CAT_ATM             = "ATM";
    private static final String CAT_FOOD            = "FOOD";
    private static final String CAT_COFFEE          = "COFFEE";
    private static final String CAT_GAS             = "GAS";
    private static final String CAT_MOVIE           = "MOVIES";
    private static final String CAT_SHOPPING        = "SHOPPING";
    private static final String CAT_PARKING         = "PARKING";
    
    private static WidgetParameter widgetParameter; 
    private static final String VALUE_BY_NAVIGATION_WIDGET         = "19";
    private static final String VALUE_BY_SEARCH_WIDGET             = "20";    
    private static final String VALUE_BY_TRAFFIC_WIDGET            = "21";
	
	private static final long MIN_INVOKE_INTERVAL = 2000;
    private static long lastInvokeTime = -1;
    
    public void executeDelegate(WidgetParameter wp)
    {
        if (!hasTnInstalled())
        {
            launchVpl();
            return;
        }
		
		synchronized(MaiTaiHandler.class)
        {
            if(System.currentTimeMillis() - lastInvokeTime < MIN_INVOKE_INTERVAL)
            {
                Log.d("MaiTaiHandler", "MaiTaiHandler ---> ignore action within 2s");
                return;
            }
            
            lastInvokeTime = System.currentTimeMillis();
        }
		
        widgetParameter = wp;
        switch (wp.getAction())
        {
        case IWidgetConstants.ACTION_CAT_SEARCH:
            int viewId = wp.getInt(IWidgetConstants.KEY_VIEW_ID);
            doCatSearch(viewId);
            break;
        case IWidgetConstants.ACTION_NAVIGATE:
        	boolean isStopSet = wp.getBoolean(IWidgetConstants.KEY_IS_STOP_SET);
        	if (isStopSet)
        	{
        		doDriveTo(wp.getBoolean(IWidgetConstants.KEY_IS_HOME));        		
        	}
        	else
        	{
        		doSetAddress(wp.getBoolean(IWidgetConstants.KEY_IS_HOME));
        	}
            break;
        case IWidgetConstants.ACTION_ONEBOX_SEARCH:
            String term = wp.getString(IWidgetConstants.KEY_TERM);
            doTermSearch(term);
            break;
        case IWidgetConstants.ACTION_CLICK_MAP:
            doMap();
            break;
        }
    }
    
    private void doSetAddress(boolean isHome) 
    {
    	HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEYWORD_ADDRESS_TYPE, isHome ? KEYWORD_HOME : KEYWORD_OFFICE);
        launchMaitai(map, MAITAI_SET_ADDRESS);        
	}

	public void maitaiResponse(String respUri)
    {
    }
    
    private void doMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_NAMEDADDR, KEYWORD_CURRENT);
        TnLocation location = AndroidGpsProvider.getInstance().getLastKnownLocation();
        
        if (location != null)
        {
            float lat = (float)location.getLatitude() / 100000;
            float lon = (float)location.getLongitude() / 100000;
            map.put(KEY_LAT, "" + lat);
            map.put(KEY_LON, "" + lon);
        }
        
        launchMaitai(map, MAITAI_MAP);        
    }
    
    private void doCatSearch(int viewId)
    {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(KEY_NAMEDADDR, KEYWORD_CURRENT);
        map.put(KEY_CAT_NAME, getCatName(viewId));
        
        launchMaitai(map, MAITAI_SEARCH);
    }
    
    private void doTermSearch(String term)
    {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(KEY_NAMEDADDR, KEYWORD_CURRENT);
        map.put(KEY_TERM, term);
        
        launchMaitai(map, MAITAI_SEARCH);        
    }
    
    private void doDriveTo(boolean isHome)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_NAMEDADDR, KEYWORD_CURRENT);
        map.put(KEY_NAMEDADDR2, isHome ? KEYWORD_HOME : KEYWORD_OFFICE);
        map.put(KEY_NAV_TYPE, KEYWORD_NAV);
        launchMaitai(map, MAITAI_DRIVETO);        
    }
    
    private void launchMaitai(HashMap<String, String> map, String maitaiAction)
    {
        map.put(KEY_VERSION, "2.0");
        map.put(KEY_CONTEXT, "cn");
        map.put(KEY_CALLBACK, MAITAI_CALLBACK);
        map.put(KEY_DEVELOPER_KEY, AppConfigHelper.apiKey);
        map.put(KEY_WIDGET_TYPE, getWidgetType());
        
        StringBuffer request = new StringBuffer();
        request.append("telenav://" + maitaiAction + "?");

        Set set = map.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext())
        {
            Map.Entry<String, String> me = (Map.Entry<String, String>) i.next();
            try
            {
                String key = me.getKey();
                key = URLEncoder.encode(key, "UTF-8");
                String val = me.getValue();
                val = URLEncoder.encode(val, "UTF-8");
                String pair = key + "=" + val;
                request.append(pair);
                if (i.hasNext())
                    request.append("&");
            }
            catch (Exception e)
            {
            }
        }
        
        IMaiTaiAdapter adapter = MaiTaiAdapterFactory.getMaiTaiAdapter();
        
        Context context = AndroidPersistentContext.getInstance().getContext();
        AndroidMaiTaiContext maitaiContext = new AndroidMaiTaiContext();
        maitaiContext.context = context;
        maitaiContext.intentAction = context.getPackageName() + ".searchwidget.maitaiadapter";
        adapter.init(maitaiContext);
        adapter.setCallback(this);
        adapter.request(request.toString());

    }

    private String getWidgetType()
    {
        String widgetType=null;
        if (widgetParameter != null)
        {
            int layoutId=widgetParameter.getLayoutId();
            if(R.layout.searchwidget_full==layoutId||R.layout.searchwidget_full_land==layoutId)
            {
                widgetType=VALUE_BY_NAVIGATION_WIDGET;               
            }
            if(R.layout.searchwidget_mini_search==layoutId||R.layout.searchwidget_mini_search_land==layoutId)
            {
                widgetType=VALUE_BY_SEARCH_WIDGET;               
            }
            if(R.layout.searchwidget_half==layoutId||R.layout.searchwidget_half_land==layoutId)
            {
                widgetType=VALUE_BY_TRAFFIC_WIDGET;               
            }             
        }
        return widgetType;
    }
    
    private boolean hasTnInstalled()
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        PackageManager pm = context.getPackageManager();
        
        Intent mainIntent = new Intent(MAITAI_ACTION, null);
        mainIntent.addCategory(MAITAI_CATEGORY);
        
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        
        if(list != null && list.size() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    
    private void launchVpl()
    {
        try
        {
            Intent intent = new Intent();
            intent.setAction(VPL_ACTION);
            intent.addCategory(VPL_CATEGORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            Context context = AndroidPersistentContext.getInstance().getContext();
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            
        }
    }
    
    private String getCatName(int viewId)
    {
        switch (viewId)
        {
        case R.id.atm:
            return CAT_ATM;
        case R.id.food:
            return CAT_FOOD;
        case R.id.gas:
            return CAT_GAS;
        case R.id.coffee:
            return CAT_COFFEE;
        case R.id.parking:
        default:
            return CAT_PARKING;
            
        }        
    }

}

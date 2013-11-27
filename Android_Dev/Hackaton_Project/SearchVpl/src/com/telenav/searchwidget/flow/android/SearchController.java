/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SearchController.java
 *
 */
package com.telenav.searchwidget.flow.android;

import android.content.Context;

import com.telenav.app.android.cingular.R;
import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.searchwidget.app.android.AndroidPersistentContext;
import com.telenav.searchwidget.data.AddressDao;
import com.telenav.searchwidget.data.datatypes.address.Stop;
import com.telenav.searchwidget.framework.android.WidgetManager;
import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.framework.android.WidgetViewStub;
import com.telenav.searchwidget.res.android.BitmapUri;
import com.telenav.searchwidget.serverproxy.data.EtaBean;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 25, 2011
 */

public class SearchController implements IWidgetActionHandler
{
    private static SearchController instance;
    
    private SearchView view;
    
    private SearchController()
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        view = new SearchView(context);
    }
    
    public synchronized static SearchController getInstance()
    {
        if (instance == null)
        {
            instance = new SearchController();
        }
        return instance;
    }
    
    public void init(int controllerEvent, int widgetId)
    {
        switch (controllerEvent)
        {
        case IWidgetConstants.CONTROLLER_EVENT_LAUNCH_MINI_SEARCH:
            launchMiniSearchView(widgetId);
            break;
        case IWidgetConstants.CONTROLLER_EVENT_LAUNCH_MINI_CAT:
            launchMiniCatView(widgetId);
            break;
        case IWidgetConstants.CONTROLLER_EVENT_LAUNCH_HALF_VIEW:
            launchHalfView(widgetId);
            break;
        case IWidgetConstants.CONTROLLER_EVENT_LAUNCH_FULL_VIEW:
            launchFullView(widgetId);
            break;
        }
    }
    
    private void launchMiniSearchView(int widgetId)
    {
    	new LauncherDelegate(this, view).launchMiniSearchView(widgetId);
    }
    
    private void launchMiniCatView(int widgetId)
    {
    	new LauncherDelegate(this, view).launchMiniCatView(widgetId);
    }
    
    private void launchHalfView(int widgetId)
    {
    	new LauncherDelegate(this, view).launchHalfView(widgetId);
    }
    
    private void launchFullView(int widgetId)
    {
    	new LauncherDelegate(this, view).launchFullView(widgetId);
    }
    
    public void handleWidgetAction(WidgetParameter wp)
    {
        WidgetViewStub stub = WidgetManager.getInstance().getViewStub(wp.getWidgetId());
        
        if (wp.getAction() == IWidgetConstants.ACTION_ORIENTATION_CHANGED)
        {
        	recoverWidget(wp.getWidgetId(), wp.getLayoutId());
        	return;
        }
		
        if (wp.getAction() == IWidgetConstants.ACTION_REFRESH_WIDGET)
        {
        	if (stub == null || wp.getBoolean(IWidgetConstants.KEY_IS_FORCED_REFRESH) || !GeocloudHandler.areHandlersRunning(wp.getWidgetId()))
        	{
        		recoverWidget(wp.getWidgetId(), wp.getLayoutId());
        	}
        	
            return;
        }
        
        boolean isRecreated = false;
        if (stub == null)
        {
            recoverWidget(wp.getWidgetId(), wp.getLayoutId());
            isRecreated = true;
        }
        
        switch (wp.getAction())
        {
        case IWidgetConstants.ACTION_SWITCH_TO_MINI_CAT:
            if (!isRecreated)
            {
                launchMiniCatView(wp.getWidgetId());
            }
            break;
        case IWidgetConstants.ACTION_SWITCH_TO_MINI_SEARCH:
            if (!isRecreated)
            {
                launchMiniSearchView(wp.getWidgetId());
            }
            break;
        case IWidgetConstants.ACTION_SET_ADDRESS_STRING:
            if (!isRecreated)
            {
                String addr = wp.getString(IWidgetConstants.KEY_ADDRESS_STRING);
                view.updateAddress(stub, addr);
            }
            break;
        case IWidgetConstants.ACTION_SET_MAP:
            if (!isRecreated)
            {
                BitmapUri bitmapUri = (BitmapUri)wp.get(IWidgetConstants.KEY_MAP_IMAGE);
                view.updateMap(stub, bitmapUri);
            }
            break;
        case IWidgetConstants.ACTION_CLICK_MAP:
            handleClickOnMap(wp);
            break;
        case IWidgetConstants.ACTION_CAT_SEARCH:
            handleCatSearch(wp);
            break;
        case IWidgetConstants.ACTION_REFRESH_ADDRESS:
            if (!isRecreated)
            {
                handleRefreshAddress(wp);
            }
            break;
        case IWidgetConstants.ACTION_UPDATE_ETA:
            if (!isRecreated)
            {
                handleUpdateEta(wp);
            }
            break;
        case IWidgetConstants.ACTION_NAVIGATE:
            handleNavigate(wp);
            break;
        }
    }    
    
    private void handleRefreshAddress(WidgetParameter wp)
    {
        if (wp.getLayoutId() == R.layout.searchwidget_full
        		|| wp.getLayoutId() == R.layout.searchwidget_full_land)
        {
            launchFullView(wp.getWidgetId());
        }
        else
        {
            launchHalfView(wp.getWidgetId());
        }
    }
    
    private void handleUpdateEta(WidgetParameter wp)
    {
        boolean isHome = wp.getBoolean(IWidgetConstants.KEY_IS_HOME);
        
        EtaBean bean = (EtaBean)wp.get(IWidgetConstants.KEY_ETA_BEAN);
        
        int hours = -1;
        int minutes = -1;
        int delay = -1;
        if (bean != null)
        {
            hours = (int)(bean.trafficTime / 3600);
            minutes = (int)(bean.trafficTime % 3600) / 60;
            
            delay = (int)((bean.trafficTime - bean.travelTime) / 60);
        }
        
        int rate = 0;
        int totalMinutes = hours * 60 + minutes;
        if (totalMinutes > 0)
        {
            rate = delay * 100 / totalMinutes;
        }
        
        Context context = AndroidPersistentContext.getInstance().getContext();
        int color = context.getResources().getColor(R.color.etaGreenColor);
        if (AppConfigHelper.BRAND_ATT.equals(AppConfigHelper.brandName))
        {
        	if (rate >= 5 && rate <= 15)
        	{
        		color = context.getResources().getColor(R.color.etaOrangeColor);
        	}
        	else if (rate > 15)
        	{
        		color = context.getResources().getColor(R.color.etaRedColor);
        	}
        }
        else
        {
        	color = context.getResources().getColor(R.color.normalColor);
        }

        WidgetViewStub stub = WidgetManager.getInstance().getViewStub(wp.getWidgetId());
        view.updateEta(stub, isHome, hours, minutes, delay, color);
    }
    
    private void handleClickOnMap(WidgetParameter wp)
    {
        new ActionHandler().execute(wp);
    }
    
    private void handleCatSearch(WidgetParameter wp)
    {
        new ActionHandler().execute(wp);
    }
    
    private void handleNavigate(WidgetParameter wp)
    {
        new ActionHandler().execute(wp);
    }
    
    private void recoverWidget(int widgetId, int layoutId)
    {
        switch (layoutId)
        {
        case R.layout.searchwidget_mini_search:
        case R.layout.searchwidget_mini_search_land:
            this.launchMiniSearchView(widgetId);
            break;
        case R.layout.searchwidget_mini_cat:
        case R.layout.searchwidget_mini_cat_land:
            this.launchMiniCatView(widgetId);
            break;
        case R.layout.searchwidget_half:
        case R.layout.searchwidget_half_land:
            this.launchHalfView(widgetId);
            break;
        case R.layout.searchwidget_full:
        case R.layout.searchwidget_full_land:
            this.launchFullView(widgetId);
            break;
        }
    }
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LauncherDelegate.java
 *
 */

package com.telenav.searchwidget.flow.android;

import com.telenav.gps.IGpsListener;
import com.telenav.location.TnLocation;
import com.telenav.searchwidget.data.AddressDao;
import com.telenav.searchwidget.data.datatypes.address.Stop;
import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.framework.android.WidgetViewStub;
import com.telenav.searchwidget.gps.android.AndroidGpsProvider;

/**
 * @author xinrongl
 * @date Jan 24, 2012
 */

class LauncherDelegate implements IGpsListener
{
    private final static int VALID_GPS_TIME     = 60000;
    
    
	private IWidgetActionHandler actionHandler;
	private SearchView view;
	
	private WidgetViewStub stub;
	
	public LauncherDelegate(IWidgetActionHandler handler, SearchView view)
	{
		this.actionHandler = handler;
		this.view = view;
	}
	
    public void launchMiniSearchView(int widgetId)
    {
        stub = view.createMiniSearchView(widgetId);
        view.showView(stub);

        TnLocation location = acquireLocation();
        
        handleRequestRgc(stub, location);
    }
    
    public void launchMiniCatView(int widgetId)
    {
        stub = view.createMiniCatView(widgetId);
        view.showView(stub);
        
        TnLocation location = acquireLocation();
        
        handleRequestRgc(stub, location);
    }
    
    public void launchHalfView(int widgetId)
    {
        Stop home = AddressDao.getInstance().getHomeStop();
        Stop work = AddressDao.getInstance().getOfficeStop();
        
        stub = view.createHalfView(widgetId, home, work);
        view.showView(stub);
        
        TnLocation location = acquireLocation();

        handleRequestRgc(stub, location);
        handleRequestMap(stub, location);
        if (home != null)
        {
            handleRequestEta(stub, home, true, location);
        }
        if (work != null)
        {
            handleRequestEta(stub, work, false, location);
        }
    }
    
    public void launchFullView(int widgetId)
    {
        Stop home = AddressDao.getInstance().getHomeStop();
        Stop work = AddressDao.getInstance().getOfficeStop();
        
        stub = view.createFullView(widgetId, home, work);
        view.showView(stub);
        
        TnLocation location = acquireLocation();

        handleRequestRgc(stub, location);
        handleRequestMap(stub, location);
        if (home != null)
        {
            handleRequestEta(stub, home, true, location);
        }
        if (work != null)
        {
            handleRequestEta(stub, work, false, location);
        }
    }
    
    private void handleRequestRgc(WidgetViewStub stub, TnLocation location)
    {
        WidgetParameter wp = new WidgetParameter(stub.widgetId, stub.layoutId, IWidgetConstants.ACTION_REQUEST_RGC);
        new GeocloudHandler(actionHandler, location).execute(wp);
    }
    
    private void handleRequestMap(WidgetViewStub stub, TnLocation location)
    {
        view.updateProgress(stub);
        WidgetParameter wp = new WidgetParameter(stub.widgetId, stub.layoutId, IWidgetConstants.ACTION_REQUEST_MAP);
        new GeocloudHandler(actionHandler, location).execute(wp);
    }
    
    private void handleRequestEta(WidgetViewStub stub, Stop stop, boolean isHome, TnLocation location)
    {
        WidgetParameter wp = new WidgetParameter(stub.widgetId, stub.layoutId, IWidgetConstants.ACTION_REQUEST_ETA);
        wp.put(IWidgetConstants.KEY_STOP, stop);
        wp.putBoolean(IWidgetConstants.KEY_IS_HOME, isHome);
        new GeocloudHandler(actionHandler, location).execute(wp);
    }
    
    private TnLocation acquireLocation()
    {
        TnLocation loc = AndroidGpsProvider.getInstance().getLastKnownLocation();
        long t = System.currentTimeMillis();
        if (loc == null || t - loc.getTime() * 10 > VALID_GPS_TIME)
        {
            AndroidGpsProvider.getInstance().getCurrentLocation(this);            
        }
        
    	return loc;
    }

	public void gpsDataArrived(TnLocation data)
	{
		if (data != null)
		{
	        WidgetParameter wp = new WidgetParameter(stub.widgetId, stub.layoutId, IWidgetConstants.ACTION_REFRESH_WIDGET);
	        wp.putBoolean(IWidgetConstants.KEY_IS_FORCED_REFRESH, AndroidGpsProvider.getInstance().isGpsOutRange());
	        this.actionHandler.handleWidgetAction(wp);
		}
	}

}


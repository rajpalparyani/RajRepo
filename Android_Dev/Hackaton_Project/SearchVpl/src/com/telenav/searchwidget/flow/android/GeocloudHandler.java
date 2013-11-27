/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GeocloudHandler.java
 *
 */
package com.telenav.searchwidget.flow.android;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.searchwidget.data.datatypes.address.Stop;
import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.res.IStringSearchWidget;
import com.telenav.searchwidget.res.ResUtil;
import com.telenav.searchwidget.res.ResourceManager;
import com.telenav.searchwidget.res.android.AndroidImageManager;
import com.telenav.searchwidget.res.android.BitmapUri;
import com.telenav.searchwidget.serverproxy.AbstractServerProxy;
import com.telenav.searchwidget.serverproxy.GeocodeProvider;
import com.telenav.searchwidget.serverproxy.GeocodeProxy;
import com.telenav.searchwidget.serverproxy.IServerProxyConstants;
import com.telenav.searchwidget.serverproxy.IServerProxyListener;
import com.telenav.searchwidget.serverproxy.data.EtaBean;
import com.telenav.searchwidget.serverproxy.data.GeocodeBean;
import com.telenav.searchwidget.serverproxy.data.MapBean;
import com.telenav.searchwidget.serverproxy.data.ReverseGeocodeBean;
import com.telenav.searchwidget.util.android.WidgetUtil;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 27, 2011
 */

public class GeocloudHandler extends AbstractHandler implements IServerProxyListener
{
    private int widgetId;
    private int layoutId;
    private IWidgetActionHandler actionHandler;
    
    private ResourceBundle bundle;
    
    private Bitmap mapImage;
    private boolean requestTrafficMap;
    
    private boolean isHome;
    
    private static Vector handlers = new Vector();
    
    private TnLocation location;
    
    public GeocloudHandler(IWidgetActionHandler handler, TnLocation location)
    {
        this.actionHandler = handler;
        
        this.location = location;
        
        this.bundle = ResourceManager.getInstance().getCurrentBundle();
    }
    
    protected void executeDelegate(WidgetParameter wp)
    {
    	registerHandler(this);
        
        widgetId = wp.getWidgetId();
        layoutId = wp.getLayoutId();
        
        switch (wp.getAction())
        {
        case IWidgetConstants.ACTION_REQUEST_RGC:
            doRgc();
            break;
        case IWidgetConstants.ACTION_REQUEST_MAP:
            doMap();
            break;
        case IWidgetConstants.ACTION_REQUEST_GEOCODE:
            String firstline = wp.getString(IWidgetConstants.KEY_FIRSTLINE);
            String lastline = wp.getString(IWidgetConstants.KEY_LASTLINE);
            doGeocode(firstline, lastline);
            break;
        case IWidgetConstants.ACTION_REQUEST_ETA:
            Stop stop = (Stop)wp.get(IWidgetConstants.KEY_STOP);
            isHome = wp.getBoolean(IWidgetConstants.KEY_IS_HOME);
            doEta(stop);
            break;
        }
    }
    
    private void doGeocode(String firstline, String lastline)
    {
        GeocodeProvider.getInstance().requestGeocode(firstline, lastline, this);
    }
    
    private void doRgc()
    {
        if (location == null)
        {
            updateUnknownLocation();
            unregisterHandler(this);
        }
        else
        {
            float rgcLat = (float)location.getLatitude() / 100000;
            float rgcLon = (float)location.getLongitude() / 100000;
            GeocodeProvider.getInstance().requestReverseGeocode(rgcLat, rgcLon, this);
        }
    }
    
    private void doMap()
    {
        requestTrafficMap = false;
        
        if (location == null)
        {
            updateMap(null);
            unregisterHandler(this);
        }
        else
        {
            float lat = (float)location.getLatitude() / 100000;
            float lon = (float)location.getLongitude() / 100000;
            GeocodeProvider.getInstance().requestTrafficMap(
                    lat, 
                    lon, 
                    IWidgetConstants.CONST_DEFAULT_ZOOM_LEVEL, 
                    getMapSize(),
                    false,
                    this);            
        }
    }
    
    private void doTraffic()
    {
        requestTrafficMap = true;
        
        if (location != null)
        {
            float lat = (float)location.getLatitude() / 100000;
            float lon = (float)location.getLongitude() / 100000;
            GeocodeProvider.getInstance().requestTrafficMap(
                    lat, 
                    lon, 
                    IWidgetConstants.CONST_DEFAULT_ZOOM_LEVEL, 
                    getMapSize(),
                    true,
                    this);
        }
    }
    
    private void doEta(Stop stop)
    {
        if (location == null)
        {
            updateEta(null);
            unregisterHandler(this);
        }
        else
        {
            float origLat = (float)location.getLatitude() / 100000;
            float origLon = (float)location.getLongitude() / 100000;
            float destLat = (float)stop.getLat() / 100000;
            float destLon = (float)stop.getLon() / 100000;
            
            String startEnd = "" + origLat + "," + origLon + "," + destLat + "," + destLon;
            GeocodeProvider.getInstance().requestEta(startEnd, this);
        }
    }
    
    public void transactionFinishedDelegate(AbstractServerProxy proxy)
    {
        boolean needUnregister = true;
        
        if (proxy instanceof GeocodeProxy)
        {
            GeocodeProxy geocodeProxy = (GeocodeProxy) proxy;
            if (IServerProxyConstants.ACTION_GEOCODE.equalsIgnoreCase(proxy.getRequestAction()))
            {
                GeocodeBean bean = geocodeProxy.getGeocodeBean();
                
                WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_MATCHED_ADDRESSES);
                if (bean.addresses != null)
                {
                    wp.put(IWidgetConstants.KEY_MATCHED_ADDRESSES, bean.addresses);
                }
                this.actionHandler.handleWidgetAction(wp);
            }
            else if (IServerProxyConstants.ACTION_REVERSE_GEOCODE.equalsIgnoreCase(proxy.getRequestAction()))
            {
                ReverseGeocodeBean bean = geocodeProxy.getReverseGeocodeBean();
                
                WidgetParameter wp = new WidgetParameter(widgetId, layoutId,
                        IWidgetConstants.ACTION_SET_ADDRESS_STRING);

                String addr = ResUtil.getapproximateAddress(bean);
                wp.putString(IWidgetConstants.KEY_ADDRESS_STRING, addr);
                actionHandler.handleWidgetAction(wp);
            }
            else if (IServerProxyConstants.ACTION_GET_MAP.equalsIgnoreCase(proxy.getRequestAction()))
            {
                MapBean bean = geocodeProxy.getMapBean();
                try
                {
                    byte[] image = Base64.decode(bean.imageString.toString(), Base64.DEFAULT);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length, options);
                    if (!requestTrafficMap)
                    {
                        mapImage = bm;
                        doTraffic();
                        needUnregister = false;
                    }
                    else
                    {
                        bm = WidgetUtil.combineBitmap(mapImage, bm);
                    }
                    BitmapUri uri = AndroidImageManager.getInstance().saveBitmap(widgetId, bm);
                    updateMap(uri);
                }
                catch (Exception e)
                {
                    
                }
            }
            else if (IServerProxyConstants.ACTION_GET_ETA.equalsIgnoreCase(proxy.getRequestAction()))
            {
                EtaBean bean = geocodeProxy.getEtaBean();
                
                updateEta(bean);
            }
        }
        if (needUnregister)
        {
            unregisterHandler(this);
        }
    }

    public void networkErrorDelegate(AbstractServerProxy proxy, byte statusCode)
    {
        transactionError(proxy);
    }

    public void transactionErrorDelegate(AbstractServerProxy proxy)
    {
        if (proxy instanceof GeocodeProxy)
        {
            if (IServerProxyConstants.ACTION_GEOCODE.equalsIgnoreCase(proxy.getRequestAction()))
            {
                WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_MATCHED_ADDRESSES);
                this.actionHandler.handleWidgetAction(wp);
            }
            else if (IServerProxyConstants.ACTION_REVERSE_GEOCODE.equalsIgnoreCase(proxy.getRequestAction()))
            {
                updateUnknownLocation();
            }
            else if (IServerProxyConstants.ACTION_GET_MAP.equalsIgnoreCase(proxy.getRequestAction()))
            {
                if (!requestTrafficMap)
                {
                    updateMap(null);
                }
            }
            else if (IServerProxyConstants.ACTION_GET_ETA.equalsIgnoreCase(proxy.getRequestAction()))
            {
                updateEta(null);
            }
            
            unregisterHandler(this);
        }
    }

    private void updateUnknownLocation()
    {
        WidgetParameter wp = new WidgetParameter(widgetId, layoutId,
                IWidgetConstants.ACTION_SET_ADDRESS_STRING);
        
        String addr = bundle.getString(IStringSearchWidget.RES_UNKNOWN_LOCATION, 
                IStringSearchWidget.FAMILY_SEARCHWIDGET);
        wp.putString(IWidgetConstants.KEY_ADDRESS_STRING, addr);
        actionHandler.handleWidgetAction(wp);        
    }
    
    private void updateMap(BitmapUri bitmapUri)
    {
        WidgetParameter wp = new WidgetParameter(widgetId, layoutId,
                IWidgetConstants.ACTION_SET_MAP);
        if (bitmapUri != null)
        {
            wp.put(IWidgetConstants.KEY_MAP_IMAGE, bitmapUri);
        }
        actionHandler.handleWidgetAction(wp);        
    }
    
    private void updateEta(EtaBean bean)
    {
        WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_UPDATE_ETA);
        if (bean != null)
        {
            wp.put(IWidgetConstants.KEY_ETA_BEAN, bean);
        }
        wp.putBoolean(IWidgetConstants.KEY_IS_HOME, isHome);
        
        this.actionHandler.handleWidgetAction(wp);        
    }
        
    private int getMapSize()
    {
//        Context context = AndroidPersistentContext.getInstance().getContext();
//        Activity activity = (Activity)context;
//        
//        DisplayMetrics metrics = new DisplayMetrics(); 
//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        if (metrics.densityDpi == DisplayMetrics.DENSITY_LOW)
//        {
//            return IWidgetConstants.CONST_MAP_SIZE_128;
//        }
        return IWidgetConstants.CONST_MAP_SIZE_256;
    }
    
    protected static synchronized void registerHandler(GeocloudHandler handler)
    {
        handlers.addElement(handler);
    }
    
    protected static synchronized void unregisterHandler(GeocloudHandler handler)
    {
        handlers.removeElement(handler);
    }
    
    public static synchronized boolean areHandlersRunning(int widgetId)
    {
        int size = handlers.size();
        for (int i = 0; i < size; i ++)
        {
            GeocloudHandler handler = (GeocloudHandler)handlers.elementAt(i);
            if (handler.widgetId == widgetId)
            {
                return true;
            }
        }
        return false;
    }
}

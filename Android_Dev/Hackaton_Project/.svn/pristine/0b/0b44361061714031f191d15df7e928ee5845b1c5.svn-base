/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GeocodeProvider.java
 *
 */
package com.telenav.searchwidget.serverproxy;

import com.telenav.comm.Host;
import com.telenav.comm.HostProvider;
import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.searchwidget.app.CommManager;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.INotifierListener;

/**
 * The TeleNav GeoCloud API provides a restful interface to the TeleNav Cloud 
 * for Location Based Services including geocoding, maps, local search and 
 * TeleNav Client integration. 
 * 
 *@author hchai
 *@date 2011-7-21
 */
public class GeocodeProvider implements IJob, INotifierListener
{
    public final static int TYPE_GEOCODE = 1;
    public final static int TYPE_REVERSE_GEOCODE = 2;
    public final static int TYPE_GET_MAP = 3;
    public final static int TYPE_GET_ETA = 4;
    
    public final static String FILE_GEOCODE = "/tnapi/services/geoutil/geocode";
    public final static String FILE_REVERSE_GEOCODE = "/tnapi/services/geoutil/reverseGeocode";
    public final static String FILE_GET_MAP = "/tnapi/services/map/getMap";
    public final static String FILE_GET_ETA = "/tnapi/services/routing/getETA";
    
//    private static volatile String apiKey = "AQAAASS3tFggf/////////8AAAABAAAAAQEAAAAQ/aqfYhfCV6No68w9IIeAyQEAAAAOAwAAABIAAAAaAAAAAQA=";
        
    private final static String hostName = "api.telenav.com";
    
    private final static String XML_HEAD_DESC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    private static GeocodeProvider inst = new GeocodeProvider();
    
    private static HostProvider hostProvider = new HostProvider();
    
    private int timeout = 30*1000;
    
    private byte retrytime = 2;
    
    public static GeocodeProvider getInstance()
    {
        return inst;
    }

    /**
     * Geocode address
     * This is a request for searches matching address and returns the latitude and longitude.
     * 
     * @param firstline
     * @param lastline
     * @param proxyListener The network operation callback object
     */
    public void requestGeocode(String firstline, String lastline, IServerProxyListener proxyListener)
    {
        Host host = hostProvider.createHost(Host.HTTP, hostName, 80, FILE_GEOCODE);
        
        StringBuffer request = new StringBuffer();
        request.append(XML_HEAD_DESC);
        request.append("<GeocodeRequest>");
        request.append("<apiKey>").append(AppConfigHelper.apiKey).append("</apiKey>");
        request.append("<firstLine>").append(firstline).append("</firstLine>");
        request.append("<lastLine>").append(lastline).append("</lastLine>");
        request.append("</GeocodeRequest>");
        
        GeocodeProxy proxy = new GeocodeProxy(host, CommManager.getInstance().getComm(), proxyListener);
        proxy.sendData(request. toString().getBytes(), IServerProxyConstants.ACTION_GEOCODE, retrytime, timeout);
    }
    
    /**
     * This is a request for find address for given latitude and longitude
     * 
     * @param lat ex. 37.37
     * @param lon ex. -121.3
     * @param proxyListener The network operation callback object
     */
    public void requestReverseGeocode(float lat, float lon, IServerProxyListener proxyListener)
    {
        Host host = hostProvider.createHost(Host.HTTP, hostName, 80, FILE_REVERSE_GEOCODE);
        
        StringBuffer request = new StringBuffer();
        request.append(XML_HEAD_DESC);
        request.append("<ReverseGeocodeRequest><apiKey>");
        request.append(AppConfigHelper.apiKey);
        request.append("</apiKey><lat>");
        request.append(lat);
        request.append("</lat><lon>");
        request.append(lon);
        request.append("</lon></ReverseGeocodeRequest>");
        
        GeocodeProxy proxy = new GeocodeProxy(host, CommManager.getInstance().getComm(), proxyListener);
        proxy.sendData(request. toString().getBytes(), IServerProxyConstants.ACTION_REVERSE_GEOCODE, retrytime, timeout);
    }
    
    /**
     * Get Map
     * This is request for returns map tile images. 
     * A map tile is one small square area of the entire map. 
     * You can create maps of any size using multiple tiles.
     *  
     * @param lat ex. 37.37
     * @param lon ex. -121.3
     * @param zoom - Requested zoom level.
     *          Allowed values:
     *              - size="256" - 0 to 17
     *              - size="128" - 0 to 18
     *              - size="64" - 0 to 19
     * 
     * @param size - Size of the tile to be returned. Default value="256"
     *          Allowed values:
     *              - "256" - return 256x256 bitmap
     *              - "128" - return 128x128 bitmap
     *              - "64" - return 64x64 bitmap
     *              
     * @param proxyListener The network operation callback object
     */
    public void requestTrafficMap(float lat, float lon, int zoom, int size, boolean requestTrafficLayer, IServerProxyListener proxyListener)
    {
        Host host = hostProvider.createHost(Host.HTTP, hostName, 80, FILE_GET_MAP);
        
        StringBuffer request = new StringBuffer();
        request.append("<ns3:getMapRequest xmlns:ns3=\"http://telenav.com/tnapi/datatypes/map/v10/\"><apiKey>");
        request.append(AppConfigHelper.apiKey);
        request.append("</apiKey>");
        request.append("<lat>").append(lat).append("</lat>");
        request.append("<lon>").append(lon).append("</lon>");
        request.append("<zoom>").append(zoom).append("</zoom>");
        request.append("<size>").append(size).append("</size>");
        if (requestTrafficLayer)
        {
            request.append("<layer>").append("Traffic").append("</layer>");
        }
        request.append("</ns3:getMapRequest>");
        
        GeocodeProxy proxy = new GeocodeProxy(host, CommManager.getInstance().getComm(), proxyListener);
        proxy.sendData(request. toString().getBytes(), IServerProxyConstants.ACTION_GET_MAP, retrytime, timeout);
    }
    
    /**
     * Get ETA
     * Estimate Time of Arrival
     * This operation estimates travel time (by car) and distance for the given trip's starting point and ending point.
     *  
     * @param startEnd
     * @param proxyListener
     */
    public void requestEta(String startEnd, IServerProxyListener proxyListener)
    {
        Host host = hostProvider.createHost(Host.HTTP, hostName, 80, FILE_GET_ETA);
        
        StringBuffer request = new StringBuffer();
        request.append(XML_HEAD_DESC);
        request.append("<getETA><apiKey>");
        request.append(AppConfigHelper.apiKey);
        request.append("</apiKey><startEnd>");
        request.append(startEnd);
        request.append("</startEnd></getETA>");
        
        GeocodeProxy proxy = new GeocodeProxy(host, CommManager.getInstance().getComm(), proxyListener);
        proxy.sendData(request. toString().getBytes(), IServerProxyConstants.ACTION_GET_ETA, retrytime, timeout);
    }
    
    public void cancel()
    {
        // TODO Auto-generated method stub
        
    }

    public void execute(int handlerID)
    {
        // TODO Auto-generated method stub
        
    }

    public boolean isCancelled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isRunning()
    {
        // TODO Auto-generated method stub
        return false;
    }

    
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public long getLastNotifyTimestamp()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getNotifyInterval()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void notify(long timestamp)
    {
        // TODO Auto-generated method stub
        
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        // TODO Auto-generated method stub
        
    }
}

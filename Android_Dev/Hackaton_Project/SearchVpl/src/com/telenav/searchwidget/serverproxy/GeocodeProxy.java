/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GeocodeProxy.java
 *
 */
package com.telenav.searchwidget.serverproxy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.comm.ICommStreamHandler;
import com.telenav.comm.ICommCallback.CommResponse;
import com.telenav.searchwidget.serverproxy.data.EtaBean;
import com.telenav.searchwidget.serverproxy.data.GeocodeBean;
import com.telenav.searchwidget.serverproxy.data.MapBean;
import com.telenav.searchwidget.serverproxy.data.ReverseGeocodeBean;

/**
 * A server proxy class used to handle geocoding request.
 * The useful function is get geocoding, maps, local search by following four functions
 * (getEtaBean, getGeocodeBean, getReverseGeocodeBean, getMapBean)
 * 
 *@author hchai
 *@date 2011-7-21
 */
public class GeocodeProxy extends AbstractServerProxy implements ICommStreamHandler
{
    private GeocodeHandler handler; 
        
    public GeocodeProxy(Host host, Comm comm, IServerProxyListener serverProxyListener)
    {
        super(host, comm, serverProxyListener);
        
    }

    protected void handleResponse(byte[] response)
    {
    }

    protected String sendData(byte[] request, String action, byte retryTimes, int timeout)
    {
        this.actionId = action;
        this.errorMessage = null;
        this.status = -1;
        
        String jobId = this.comm.sendData(this.actionId, host, request,  this, null, retryTimes, timeout, serverProxyCommCallback);

        addJobToList(jobId);

        return jobId;
    }

    public boolean receive(InputStream is, long length, CommResponse response,
            ICommCallback callback) throws IOException
    {
        response.status = ICommCallback.RESPONSE_ERROR;
        
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        
        String result = in.readLine();
        
        if (result != null)
        {
            int handlerType = -1;
            if (IServerProxyConstants.ACTION_GEOCODE.equalsIgnoreCase(this.getRequestAction()))
            {
                handlerType = GeocodeProvider.TYPE_GEOCODE;
            }
            else if (IServerProxyConstants.ACTION_REVERSE_GEOCODE.equalsIgnoreCase(this.getRequestAction()))
            {
                handlerType = GeocodeProvider.TYPE_REVERSE_GEOCODE;
            }
            else if (IServerProxyConstants.ACTION_GET_MAP.equalsIgnoreCase(this.getRequestAction()))
            {
                handlerType = GeocodeProvider.TYPE_GET_MAP;
            }
            else if (IServerProxyConstants.ACTION_GET_ETA.equalsIgnoreCase(this.getRequestAction()))
            {
                handlerType = GeocodeProvider.TYPE_GET_ETA;
            }
            
            if (handlerType != -1)
            {
                response.status = ICommCallback.SUCCESS;
                
                boolean ret =  parseData(handlerType, result);
                if (handler.isRespnseError())
                {
                    response.status = ICommCallback.RESPONSE_ERROR;
                    ret = false;
                    
                }
                return ret;
            }
        }
        
        return false;
    }
    
    public boolean parseData(int handlerType, String dataString)
    {
        InputStream bain = new ByteArrayInputStream(dataString.getBytes());
        handler = new GeocodeHandler(handlerType);
        
        SAXParserFactory fac = SAXParserFactory.newInstance();
        try
        {
            SAXParser parser = fac.newSAXParser();
            parser.parse(bain, handler);
            
            return true;
        }
        catch (Exception e)
        {
        }
        
        return false;
    }

    public boolean send(OutputStream os, CommResponse response, ICommCallback callback)
            throws IOException
    {
        return true;
    }
    
    
    /**
     * @return EtaBean object
     */
    public EtaBean getEtaBean()
    {
        if (handler != null)
        {
            return handler.getEtaBean();
        }
        
        return null;
    }

    
    /**
     * @return GeocodeBean object
     */
    public GeocodeBean getGeocodeBean()
    {
        if (handler != null)
        {
            return handler.getGeocodeBean();
        }
        
        return null;
    }

    
    /**
     * @return ReverseGeocodeBean object
     */
    public ReverseGeocodeBean getReverseGeocodeBean()
    {
        if (handler != null)
        {
            return handler.getReverseGeocodeBean();
        }
        
        return null;
    }

    /**
     * @return MapBean object
     */
    public MapBean getMapBean()
    {
        if (handler != null)
        {
            return handler.getMapBean();
        }
        
        return null;
    }
}

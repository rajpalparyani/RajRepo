/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * S4AProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.json;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IS4AProxy;
import com.telenav.logger.Logger;

/**
 *@author wchshao
 *@date Mar 12, 2013
 */
public class JsonS4AProxy extends AbstractServerProxy implements IS4AProxy
{
    private static final String PARAMETER_ON_MY_WAY = "onmyway";
    private static final String PARAMETER_DRIVE_TO = "driveto";
    private static final String PARAMETER_ERROR = "PARAMETER_ERROR";
    private static final String SYS_ERROR = "SYS_ERROR";
    private static final String TEST_TOKEN = "-LwId3YkwZanTfHX_6w4Fb49BlnHW48uqppwjw-2pHztAagOw76Coca7U0CV33wfFpmCDRRfUKZ70JJNhHL1zb2vejOlcJJmjBsHduGxap3aAUfDKFhQDia7IPQ9epRo2azI0LWwoTl050uPEIjPSflxHRdWOLzSsgIGPLD87lM";
    public static final String PRODUCTION_TOKEN = "lY7LOrV3A0temx1oXgKWcYvA0HtY1_1Q-WDKQx7flpnj2vgeyHZMT0WCfDh4gI3QL0RuHB5u4EXcsG1M3Qq-BW0nrZKAj7ObxBr6OJZLrIuidbePTF25Z51IBiQm_6hP77hhiNWuDbwGNZtAbTrVZGWSuNXFZuLDDViLblc9zMI";
    
    protected String tinyUrl;
    
    public JsonS4AProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    @Override
    protected void handleResponse(byte[] response)
    {
        handleData(response);
    }

    private void handleData(byte[] binData)
    {
        try
        {
            if (binData == null || binData.length == 0)
            {
                this.status = ICommCallback.NO_DATA;
            }
            else
            {
                String response = new String(binData);
                // dt and token are required, otherwise PARAMETER_ERROR returned
                if (PARAMETER_ERROR.equalsIgnoreCase(response))
                {
                    this.status = ICommCallback.RESPONSE_ERROR;
                    this.errorMessage = response;
                }
                // if any system error, SYS_ERROR returned, you may retry it later
                else if (SYS_ERROR.equalsIgnoreCase(response))
                {
                    this.status = ICommCallback.RESPONSE_ERROR;
                    this.errorMessage = response;
                }
                else
                {
                    tinyUrl = response.trim();
                    this.status = ICommCallback.SUCCESS;
                }
            }
        }
        catch (Exception t)
        {
            this.status = ICommCallback.EXCEPTION_PARSE;
            Logger.log(this.getClass().getName(), t, "handleNode() -server proxy handle Node exception.");
        }
    }

    @Override
    public String getTinyUrl()
    {
        return tinyUrl;
    }

    @Override
    public void requestTinyUrl(String address, String lat, String lon, String name, boolean isDriving)
    {
        byte[] request = generateRequestData(address, lat, lon, name, isDriving);
        if (request != null && request.length > 0)
        {
            tinyUrl = null;
            this.sendData(request, IServerProxyConstants.ACT_GET_TINY_URL, (byte)1, 30000);
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "S4A: request tiny URL: "
                            + "address: " + address
                            + ";lat: " + lat
                            + ";lon: " + lon
                            + ";name: " + name);
            }
        }
    }
    
    @Override
    public void updateETA(String session, String status, String userName, String eta, String lat, String lon, String speed, String accuracy, String routeId)
    {
        String uId = "";        
        byte[] request = generateRequestData(getSessionKey(session), userName, uId, status, eta, lat, lon, speed, accuracy, routeId);
        if (request != null && request.length > 0)
        {
            this.sendData(request, IServerProxyConstants.ACT_UPDATE_ETA, (byte)1, 30000);
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "S4A: update ETA: " 
                           + "session: " + session
                           + ";status: " + status
                           + ";userName: " + userName
                           + ";lat: " + lat
                           + ";lon: " + lon
                           + ";speed: " + speed
                           + ";accuracy: " + accuracy
                           + ";route Id: " + routeId);
            }
        }
    }
    
    private String getSessionKey(String tinyUrl)
    {
        String sessionKey = "";
        if(tinyUrl != null)
        {
            try
            {
                String[] urlitem = tinyUrl.split("\\/");
                sessionKey = urlitem[(urlitem.length - 1)];      
            }
            catch(Exception e)
            {
                
            }
        }
        
        return sessionKey; 
    }
    
    @Override
    protected Host getHostByAction(String action)
    {
        Host requestHost = null;
        if (action != null && action.length() > 0)
        {
            if (IServerProxyConstants.ACT_GET_TINY_URL.equalsIgnoreCase(action))
            {
                // hardcoded since we can only use production server of S4A
                requestHost = this.comm.getHostProvider().createHost("http", "apps.scout.me", 0, "/v1/tinyurl");
            }
            else if (IServerProxyConstants.ACT_UPDATE_ETA.equalsIgnoreCase(action))
            {
                requestHost = this.comm.getHostProvider().createHost("http", "apps.scout.me", 0, "/v1/shareETA");
            }
            if (requestHost != null)
            {
                host = requestHost;
            }
        }

        if (requestHost == null)
            requestHost = this.host;

        return requestHost;
    }
    
    private byte[] generateRequestData(String address, String lat, String lng, String name, boolean isDriving)
    {
        StringBuilder dt = new StringBuilder();
        dt.append(address);
        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng))
        {
            dt.append("@");
            dt.append(lat);
            dt.append(",");
            dt.append(lng);
        }
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("token", PRODUCTION_TOKEN));
        parameters.add(new BasicNameValuePair("dt", dt.toString()));
        
        if(isDriving)
        {
            parameters.add(new BasicNameValuePair("context", PARAMETER_ON_MY_WAY));
        }
        else
        {
            parameters.add(new BasicNameValuePair("context", PARAMETER_DRIVE_TO));
        }
        
        if (!TextUtils.isEmpty(name))
        {
            parameters.add(new BasicNameValuePair("name", name));
        }

        StringBuilder request = new StringBuilder();
        for (NameValuePair nvp : parameters)
        {
            if (request.toString().length() > 0)
            {
                request.append("&");
            }
            request.append(nvp.getName() + "=" + nvp.getValue());
        }
        
        try
        {
            return request.toString().getBytes(HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.log(this.getClass().getName(), e, "generateRequestData unsupported encoding.");
            return null;
        }
    }
    
    private byte[] generateRequestData(String session, String name, String uid, String status, String eta,
            String lat, String lon, String speed, String accuracy, String routeId)
    {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("source", "Scout4Android"));
        if (!TextUtils.isEmpty(session))
        {
            parameters.add(new BasicNameValuePair("session", session));
        }
        if (!TextUtils.isEmpty(name))
        {
            parameters.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(status))
        {
            parameters.add(new BasicNameValuePair("status", status));
        }
        if (!TextUtils.isEmpty(eta))
        {
            parameters.add(new BasicNameValuePair("eta", eta));
        }
        if (!TextUtils.isEmpty(lat))
        {
            parameters.add(new BasicNameValuePair("lat", lat));
        }
        if (!TextUtils.isEmpty(lon))
        {
            parameters.add(new BasicNameValuePair("lon", lon));
        }
        if (!TextUtils.isEmpty(speed))
        {
            parameters.add(new BasicNameValuePair("speed", speed));
        }
        if (!TextUtils.isEmpty(accuracy))
        {
            parameters.add(new BasicNameValuePair("horizontalaccuracy", accuracy));
        }
        if (!TextUtils.isEmpty(routeId))
        {
            parameters.add(new BasicNameValuePair("routeid", routeId));
        }
        
        StringBuilder request = new StringBuilder();
        for (NameValuePair nvp : parameters)
        {
            if (request.toString().length() > 0)
            {
                request.append("&");
            }
            request.append(nvp.getName() + "=" + nvp.getValue());
        }
        
        try
        {
            return request.toString().getBytes(HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.log(this.getClass().getName(), e, "generateRequestData unsupported encoding.");
            return null;
        }
    
    }

}

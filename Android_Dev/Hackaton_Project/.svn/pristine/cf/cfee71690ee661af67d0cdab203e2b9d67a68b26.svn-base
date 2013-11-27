/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPimJobListener.java
 *
 */
package com.telenav.data.serverproxy.impl.json;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;
import org.json.tnme.JSONTokener;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IDimProxy;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnSimCardInfo;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-11-23
 */
public class JsonDimProxy extends AbstractServerProxy implements IDimProxy
{
    public static final String KEY_FALLBACK = "FALLBACK";
    public static final String KEY_PTN_ENCRYPTION = "PTN_ENCRYPTION_ENABLED";
    public static final String KEY_INTERVAL = "RETRY_INTERVALS";
    public static final String KEY_TIMEOUT = "TIMEOUT";
    
    public static final String ENABLE = "1";
    public static final String DISABLE = "0";

    protected String dimStatus;
    protected String token;
    protected String port;;
    protected String destination;
    protected String carrier;
    protected String ptn;
    protected String serverDrivenInfo;
    
    protected boolean isAllowFallback = true;
    protected boolean isPtnEncrypted = true;
    
    protected int[] retryIntervals = null;
    protected int timeout = 0;
    
    public JsonDimProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    public String requestGetToken()
    {
        String tokenRequestStr = null;
        try
        {
            JSONObject root = new JSONObject();

            JSONArray array = new JSONArray();

            root.put("multiData", array);

            JSONObject infomation = new JSONObject();

            infomation.put("actionType", IServerProxyConstants.ACT_GET_DIM_TOKEN);
            
            TnSimCardInfo simCardInfo = TnTelephonyManager.getInstance().getSimCardInfo();
            String contentStr = simCardInfo.simCardId;
            if(contentStr == null || contentStr.length() == 0)
            {
                contentStr = "";
            }
            String dataStr = "{\"content\":\"" + contentStr + "\",\"contentType\":\"0\",\"httpHeaders\":{}}";

            infomation.put("data", dataStr);

            infomation.put("userProfile", getProfile());
            
            array.put(infomation);

            tokenRequestStr = root.toString();
            
            Logger.log(Logger.INFO, this.getClass().getName(), "DimRequest - action: GetToken tokenRequestStr: " + tokenRequestStr);
            
            return this.sendData(tokenRequestStr, IServerProxyConstants.ACT_GET_DIM_TOKEN, (byte) 1, 45000);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            return "";
        }
    }
    
    public String requestGetPtn(String token, boolean isEncrypted, byte retryTimes, int timeout)
    {
        String ptnRequestStr = null;
        try
        {
            JSONObject root = new JSONObject();

            JSONArray array = new JSONArray();

            root.put("multiData", array);

            JSONObject infomation = new JSONObject();

            if (isEncrypted)
            {
                infomation.put("actionType", IServerProxyConstants.ACT_GET_DIM_ENCRYPTED_PTN);
            }
            else
            {
                infomation.put("actionType", IServerProxyConstants.ACT_GET_DIM_PTN);
            }

            String dataStr = "{\"token\":\"" + token + "\",\"httpHeaders\":{}}";

            infomation.put("data", dataStr);

            infomation.put("userProfile", getProfile());
            
            array.put(infomation);

            ptnRequestStr = root.toString();

            Logger.log(Logger.INFO, this.getClass().getName(), "DimRequest - action: GetPtn ptnRequestStr: " + ptnRequestStr);
            
            return this.sendData(ptnRequestStr, IServerProxyConstants.ACT_GET_DIM_PTN, retryTimes, timeout);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            return "";
        }
    }
	
    public String getToken()
    {
        return this.token;
    }
    
    public String getDestination()
    {
        return this.destination;
    }
    
    public String getPort()
    {
        return this.port;
    }
    
    public String getPTN()
    {
        return this.ptn;
    }
    
    public String getCarrier()
    {
        return this.carrier;
    }
    
    public String getDimStatus()
    {
        return this.dimStatus;
    }
    
    public boolean isAllowFallback()
    {
        return this.isAllowFallback;
    }

    public boolean isPtnEncrypted()
    {
        return this.isPtnEncrypted;
    }
    

    public int[] getRetryIntervals()
    {
        return retryIntervals;
    }

    public int getTimeout()
    {
        return timeout;
    }
    
    protected JSONObject getProfile()
    {
        TnDeviceInfo deviceInfo = TnTelephonyManager.getInstance().getDeviceInfo();
        if(deviceInfo.platform == TnDeviceInfo.PLATFORM_ANDROID)
        {
            AppConfigHelper.platform = AppConfigHelper.PLATFORM_ANDROID;
        }
        else if(deviceInfo.platform == TnDeviceInfo.PLATFORM_RIM)
        {
            AppConfigHelper.platform = AppConfigHelper.PLATFORM_RIM;
        }
        
        JSONObject userProfile = new JSONObject();

        try
        {
            userProfile.put("carrier", AppConfigHelper.mandatoryProgramCode);

            userProfile.put("platform", AppConfigHelper.platform);

            userProfile.put("device", deviceInfo.deviceName);

            userProfile.put("version", AppConfigHelper.mandatoryClientVersion);
        }
        catch (Exception e)
        {

        }

        return userProfile;
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
                JSONTokener jsonTokener = new JSONTokener(response);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                JSONArray multiData = jsonObject.getJSONArray("multiData");
                jsonObject = (JSONObject) multiData.get(0);
                if(jsonObject != null)
                {
                    String action = jsonObject.getString("actionType");
                    String data = jsonObject.getString("data");
                    jsonTokener = new JSONTokener(data);
                    jsonObject = (JSONObject) jsonTokener.nextValue();
                    parseRequestSpecificData(action, jsonObject);
                }
            }
        }
        catch (Exception t)
        {
            this.status = ICommCallback.EXCEPTION_PARSE;
            Logger.log(this.getClass().getName(), t, "handleNode() -server proxy handle Node exception.");
        }
    }

    private void parseRequestSpecificData(String action, JSONObject jsonObject) throws JSONException
    {
        if(IServerProxyConstants.ACT_GET_DIM_TOKEN.equalsIgnoreCase(action))
        {
            port = jsonObject.getString("port");
            token = jsonObject.getString("token");
            destination = jsonObject.getString("destination");
            dimStatus = jsonObject.getString("status");
            errorMessage = jsonObject.getString("errorMsg");
            serverDrivenInfo = jsonObject.getString("serverDrivenInfo");
            parseServerDrivenInfo(serverDrivenInfo);
            this.status = ICommCallback.SUCCESS;
            Logger.log(Logger.INFO, this.getClass().getName(), "DimResponse - action: " + action + " -- port: " + port + " token: " + token+ " destination: " + destination+ " dimStatus: " + dimStatus + " errorMessage: " + errorMessage);
        }
        else if(action.equalsIgnoreCase(IServerProxyConstants.ACT_GET_DIM_PTN) || action.equalsIgnoreCase(IServerProxyConstants.ACT_GET_DIM_ENCRYPTED_PTN))
        {
            carrier = jsonObject.getString("carrier");
            ptn = jsonObject.getString("ptn");
            dimStatus = jsonObject.getString("status");
            errorMessage = jsonObject.getString("errorMsg");
            this.status = ICommCallback.SUCCESS;
            Logger.log(Logger.INFO, this.getClass().getName(), "DimResponse - action: " + action + " -- carrier: " + carrier + " ptn: " + ptn+ " dimStatus: " + dimStatus + " errorMessage: " + errorMessage);
        }
    }
    
    private void parseServerDrivenInfo(String serverDrivenInfo)
    {
        if(serverDrivenInfo == null || serverDrivenInfo.length() == 0)
            return;
        
        StringMap stringMap = parseDrvienKeyMap(getDrivenParams(serverDrivenInfo));
        if(stringMap != null)
        {
            if(ENABLE.equals(stringMap.get(KEY_FALLBACK)))
            {
                isAllowFallback = true;
            }
            else
            {
                isAllowFallback = false;
            }
            
            if(ENABLE.equals(stringMap.get(KEY_PTN_ENCRYPTION)))
            {
                isPtnEncrypted = true;
            }
            else
            {
                isPtnEncrypted = false;
            }
            handleInterval(stringMap.get(KEY_INTERVAL));
            handleTimeout(stringMap.get(KEY_TIMEOUT));
            
            Logger.log(Logger.INFO, this.getClass().getName(), "DimResponse - action: GetToken -- DrivenParams size: " + stringMap.size());
            Logger.log(Logger.INFO, this.getClass().getName(), "DimResponse - action: GetToken -- isAllowFallback: " + isAllowFallback + " isPtnEncrypted: " + isPtnEncrypted);
        }
    }

    private StringMap parseDrvienKeyMap(Vector<String> drivenParamsVec)
    {
        if(drivenParamsVec == null || drivenParamsVec.size() == 0)
            return null;
        
        StringMap stringMap = new StringMap();
            
        for(int n = 0; n < drivenParamsVec.size(); n++)
        {
            String mappingStr = drivenParamsVec.elementAt(n);
            int equalToken = mappingStr.indexOf('=');
            if(equalToken > 0)
            {
                String key = mappingStr.substring(0, equalToken);
                String value = mappingStr.substring(equalToken + 1);
                
                stringMap.put(key, value);
            }
        }
        
        return stringMap;
    }
    
    private Vector<String> getDrivenParams(String serverDrivenInfo)
    {
        if(serverDrivenInfo == null || serverDrivenInfo.length() == 0)
            return null;
        
        Vector<String> paramsVec = new Vector<String>();
        
        int index = serverDrivenInfo.indexOf(":");
        while (index >= 0)
        {
            String previous = serverDrivenInfo.substring(0, index);
            if(previous.length() > 0)
                paramsVec.addElement(previous);
            
            serverDrivenInfo = serverDrivenInfo.substring(index + 1, serverDrivenInfo.length());
            
            index = serverDrivenInfo.indexOf(":");
        }
        
        if(serverDrivenInfo.length() > 0)
            paramsVec.addElement(serverDrivenInfo);
        
        return paramsVec;
    }
    
    protected Host getHostByAction(String action)
    {
        Host requestHost = null;
        if (action != null && action.length() > 0)
        {
            requestHost = this.comm.getHostProvider().createHost(action);
            this.host = requestHost;
        }

        if(requestHost == null)
            requestHost = this.host;
        
        if (IServerProxyConstants.ACT_GET_DIM_PTN.equals(action)
                || IServerProxyConstants.ACT_GET_DIM_TOKEN.equals(action)
                /*|| IServerProxyConstants.ACT_GET_DIM_CARRIER.equals(action)
                || IServerProxyConstants.ACT_SEARCH_DIM_CARRIER.equals(action)*/)
        {
            requestHost.file += "?nodeType=json;encoding=UTF-8";
        }
        
        return requestHost;
    }

    protected String sendData(String data, String action, byte retryTimes, int timeout)
    {
        try
        {
            return this.sendData(data.getBytes("utf-8"), action, retryTimes, 45000);
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.log(this.getClass().getName(), e);
            return "";
        }
    }
    
    protected void handleResponse(byte[] response)
    {
        handleData(response);        
    }
    
    protected void handleInterval(String str)
    {
        if ( str != null && str.trim().length() > 0 )
        {
            String[] intervals = str.split(",");
            retryIntervals = new int[intervals.length];
            for (int i = 0; i < retryIntervals.length; i++)
            {
                retryIntervals[i] = Integer.parseInt(intervals[i]);
            }
        }
    }
    
    protected void handleTimeout(String str)
    {
        if ( str != null && str.trim().length() > 0 )
        {
            timeout = Integer.parseInt(str);
        }
    }
}

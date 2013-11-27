/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IKontagentLogger.java
 *
 */
package com.telenav.sdk.kontagent;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.provider.Settings.Secure;

import com.kontagent.Kontagent;
import com.kontagent.session.ISession;
import com.telenav.app.AbstractPlatformIniter;
/**
 *@author jyxu
 *@date 2013-5-8
 */
public class KontagentLogger implements IKontagentConstants
{
    public static final String QA_API_KEY = "e3481af11f564157a4e42fd75358c414";
    
    public static final String PRODUCTION_API_KEY = "c080fa59d7e54dff8ba6526ccf2c2063";
    
    public static final String TEST_MODE = Kontagent.TEST_MODE;
    
    public static final String PRODUCTION_MODE = Kontagent.PRODUCTION_MODE;
    
    private static KontagentLogger instance = new KontagentLogger();
    
    private String apiKey;
    private String mode;
    private boolean enableLogInFile = false;
    KontagentLocalLogger localLogger;
    boolean isRunning = false;
    
    public static KontagentLogger getInstance()
    {
        return instance;
    }
    
    public void start(Context context, String apiKey, String mode, boolean enableLogInFile)
    {
        if(!isRunning)
        {
            isRunning = true;
            this.apiKey = apiKey;
            this.mode = mode;
            this.enableLogInFile = enableLogInFile;
            Kontagent.getInstance().startSession(apiKey, context, mode);
            AbstractPlatformIniter.getInstance().initTelephony();
            String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            Kontagent.getInstance().getSession().setCustomId(android_id);
            Kontagent.applicationAdded();
            disableDebug();
            if(enableLogInFile)
            {
                if(localLogger == null)
                {
                    localLogger = new KontagentLocalLogger();
                }
                localLogger.printToFile(localLogger.getKtEventLogTitle());
            }
        }
    }

    public void stop()
    {
        if(isRunning)
        {
            Kontagent.getInstance().stopSession();
            if(enableLogInFile)
            {
                localLogger.close();
            }
            isRunning = false;
        }
    }
    
    public void enableDebug()
    {
        Kontagent.getInstance().enableDebug();
    }
    
    public void disableDebug()
    {
        Kontagent.getInstance().disableDebug();
    }
    
    public void setEnableLogInFile(boolean enableLogInFile)
    {
        this.enableLogInFile = enableLogInFile;
    }
    
    public String getSenderId()
    {
        return Kontagent.getInstance().getSenderId();
    }
    
    public void sendDeviceInformation(String version)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("v_maj", version);
        Kontagent.getInstance().sendDeviceInformation(params);
    }
    
    public void applicationAdded()
    {
        Kontagent.getInstance().applicationAdded();
    }
    
    public void revenueTracking(int cent, String type)
    {
        Map params = new HashMap();
        params.put("v", cent);
        params.put("tu", type);
        Kontagent.getInstance().revenueTracking(cent,params);
    }
    
    public void createSession(Context context, HashMap<String, Object> map)
    {
        ISession session = Kontagent.getInstance().createSession(context, map);
        session.start();
    }
    
    public void createSession(Context context, String apiKey, String senderId, String sdkMode)
    {
        ISession session = Kontagent.getInstance().createSession(context, apiKey, senderId, sdkMode);
        session.start();
    }
    
    public void createSession(Context context, String apiKey, String senderId, String sdkMode, boolean sendApplicationAdded)
    {
        ISession session = Kontagent.createSession(context, apiKey, senderId, sdkMode, sendApplicationAdded);
        session.start();
    }
    
    public void addCustomEvent(String category, String eventName)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("st1", category);
        Kontagent.getInstance().customEvent(eventName, params);
        if(enableLogInFile)
        {
            localLogger.printToFile(localLogger.getKtEventLog(category,  null,  null,  eventName,  -1,  -1));
        }
    }
    
    public void addCustomEvent(String category, String eventName, int level)
    {
        Map params = new HashMap();
        params.put("st1", category);
        params.put("l", ""+level);
        Kontagent.getInstance().customEvent(eventName, params);
        if(this.enableLogInFile)
        {
            localLogger.printToFile(localLogger.getKtEventLog(category,  null,  null,  eventName,  level,  -1));
        }
    }
    
    public void addCustomEvent(String category, String event, String eventName)
    {
        Map params = new HashMap();
        params.put("st1", category);
        params.put("st2", event);
        Kontagent.getInstance().customEvent(eventName, params);
        if(this.enableLogInFile)
        {
            localLogger.printToFile(localLogger.getKtEventLog(category,  event,  null,  eventName,  -1,  -1));
        }
    }
    
    public void addCustomEvent(String category, String event, String eventName, int level)
    {
        Map params = new HashMap();
        params.put("st1", category);
        params.put("st2", event);
        params.put("l", ""+level);
        Kontagent.getInstance().customEvent(eventName, params);
        if(this.enableLogInFile)
        {
            localLogger.printToFile(localLogger.getKtEventLog(category,  event,  null,  eventName,  level,  -1));
        }
    }
    
    public void addCustomEvent(String category, String event, String subEvent, String eventName)
    {
        Map params = new HashMap();
        params.put("st1", category);
        params.put("st2", event);
        params.put("st3", subEvent);
        Kontagent.getInstance().customEvent(eventName, params);
        if(this.enableLogInFile)
        {
            localLogger.printToFile(localLogger.getKtEventLog(category,  event,  subEvent,  eventName,  -1,  -1));
        }
    }
    
    public void addCustomEvent(String category, String event, String subEvent, String eventName, int level)
    {
        Map params = new HashMap();
        params.put("st1", category);
        params.put("st2", event);
        params.put("st3", subEvent);
        params.put("l", ""+level);
        Kontagent.getInstance().customEvent(eventName, params);
        if(this.enableLogInFile)
        {
            localLogger.printToFile(localLogger.getKtEventLog(category,  event,  subEvent,  eventName,  level,  -1));
        }
    }
    
    public void addCustomEvent(String category, String event, String subEvent, String eventName, int level, int v)
    {
        Map params = new HashMap();
        params.put("st1", category);
        params.put("st2", event);
        params.put("st3", subEvent);
        params.put("l", ""+level);
        params.put("v", ""+v);
        if(this.enableLogInFile)
        {
            localLogger.printToFile(localLogger.getKtEventLog(category,  event,  subEvent,  eventName,  level,  v));
        }
        Kontagent.getInstance().customEvent(eventName, params);
    }
  
    public String getApiKey()
    {
        return apiKey;
    }
    
    public String getMode()
    {
        return mode;
    }
}

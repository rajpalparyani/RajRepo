package com.telenav.navservice.android;

import android.content.Context;
import android.content.Intent;

import com.telenav.logger.Logger;
import com.telenav.navservice.NavServiceApi;
import com.telenav.navservice.NavServiceParameter;

public class AndroidNavServiceApi extends NavServiceApi
{
    private Context context;
    
    private Class navServiceClass;
    
    public AndroidNavServiceApi(Object context, Class navServiceClass)
    {
        if (context == null || !(context instanceof Context))
            throw new IllegalArgumentException("The context is null or not an instance of Context");
        
        this.context = (Context)context;
        this.navServiceClass = navServiceClass;
    }

    protected void sendCase(NavServiceParameter param)
    {
        Intent intent = new Intent(context, navServiceClass);
        
        if (param != null)
            convertNavServiceParametersToIntent(param, intent);
        
        context.startService(intent);
    }
    
    public static void convertNavServiceParametersToIntent(NavServiceParameter param, Intent intent)
    {
        if (param == null)
            return;
        
        if (param.hasSetServerUrl())
        {
            intent.putExtra(KEY_SERVER_URL, ""+param.getServerUrl());
        }
        if (param.hasSetUserId())
        {
            intent.putExtra(KEY_USER_ID, ""+param.getUserId());
        }
        if (param.hasSetPid())
        {
            intent.putExtra(KEY_PID, ""+param.getPid());
        }
        if (param.hasSetCarrierName())
        {
            intent.putExtra(KEY_CARRIER_NAME, ""+param.getCarrierName());
        }
        if (param.hasSetRouteId())
        {
            intent.putExtra(KEY_ROUTE_ID, ""+param.getRouteId());
        }
        if (param.hasSetForeground())
        {
            intent.putExtra(KEY_FOREGROUND, ""+param.isForeground());
        }
        if (param.hasSetLogEnabled())
        {
            intent.putExtra(KEY_LOG_ENABLED, ""+param.isLogEnabled());
        }
        if (param.hasSetAppVersion())
        {
            intent.putExtra(KEY_APP_VERSION, ""+param.getAppVersion());
        }
        if (param.hasSetAppName())
        {
            intent.putExtra(KEY_APP_NAME, ""+param.getAppName());
        }
        if (param.hasSetForceStop())
        {
            intent.putExtra(KEY_FORCE_STOP, ""+param.isForceStop());
        }
        if (param.hasSetPauseGpsTime())
        {
            intent.putExtra(KEY_PAUSE_GPS_TIME, ""+param.getPauseGpsTime());
        }
    }
    
    public static NavServiceParameter convertIntentToNavServiceParameters(Intent intent)
    {
        NavServiceParameter param = new NavServiceParameter();
        
        if (intent == null)
            return param;
        
        String s = intent.getStringExtra(KEY_SERVER_URL);
        if (s != null && !s.equals("null"))
        {
            param.setServerUrl(s);
        }
        
        s = intent.getStringExtra(KEY_USER_ID);
        if (s != null && !s.equals("null"))
        {
            param.setUserId(s);
        }
        
        s = intent.getStringExtra(KEY_CARRIER_NAME);
        if (s != null && !s.equals("null"))
        {
            param.setCarrierName(s);
        }
        
        s = intent.getStringExtra(KEY_ROUTE_ID);
        if (s != null)
        {
            if (s.equals("null"))
                param.setRouteId(null);
            else
                param.setRouteId(s);
        }
        
        s = intent.getStringExtra(KEY_PID);
        if (s != null && !s.equals("null"))
        {
            try{
                param.setPid(Integer.parseInt(s));
            }catch(Exception e)
            {
                Logger.log("AndroidNavServiceApi", e);
            }
        }
        
        s = intent.getStringExtra(KEY_FOREGROUND);
        if (s != null && !s.equals("null"))
        {
            param.setForeground(s.equals("true") ? true: false);
        }
        
        s = intent.getStringExtra(KEY_LOG_ENABLED);
        if (s != null && !s.equals("null"))
        {
            param.setLogEnabled(s.equals("true") ? true : false);
        }
        
        s = intent.getStringExtra(KEY_APP_VERSION);
        if (s != null && !s.equals("null"))
        {
            param.setAppVersion(s);
        }
        
        s = intent.getStringExtra(KEY_APP_NAME);
        if (s != null && !s.equals("null"))
        {
            param.setAppName(s);
        }
        
        s = intent.getStringExtra(KEY_FORCE_STOP);
        if (s != null && !s.equals("null"))
        {
            param.setForceStop(s.equals("true") ? true : false);
        }
        
        s = intent.getStringExtra(KEY_PAUSE_GPS_TIME);
        if (s != null && !s.equals("null"))
        {
            try{
                param.setPauseGpsTime(Integer.parseInt(s));
            }catch(Exception e)
            {
                Logger.log("AndroidNavServiceApi", e);
            }
        }
        
        return param;
    }
}

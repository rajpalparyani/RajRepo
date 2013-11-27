package com.telenav.navservice;


public abstract class NavServiceApi
{
    protected static String KEY_FOREGROUND         = "com.telenav.navservice.foreground";
    protected static String KEY_SERVER_URL         = "com.telenav.navservice.serverurl";
    protected static String KEY_USER_ID            = "com.telenav.navservice.userid";
    protected static String KEY_CARRIER_NAME       = "com.telenav.navservice.carriername";
    protected static String KEY_ROUTE_ID           = "com.telenav.navservice.routeid";
    protected static String KEY_PID                = "com.telenav.navservice.pid";
    protected static String KEY_LOG_ENABLED        = "com.telenav.navservice.logenabled";
    protected static String KEY_APP_VERSION        = "com.telenav.navservice.appversion";
    protected static String KEY_APP_NAME           = "com.telenav.navservice.appname";
    protected static String KEY_FORCE_STOP         = "com.telenav.navservice.forcestop";
    protected static String KEY_PAUSE_GPS_TIME     = "com.telenav.navservice.pausegpstime";
    
    public static final String NAV_SERVICE_CALLBACK_ACTION = "com.telenav.navservice.NAVSERVICECALLBACK";
    
    protected boolean isStarted;
    
    public void startNavService(NavServiceParameter param)
    {
        isStarted = true;
        sendCase(param);
    }
    
    public void setNavServiceParameters(NavServiceParameter param)
    {
        if (!isStarted)
            return;
        
        sendCase(param);
    }
    
    public void stopNavService()
    {
        isStarted = false;
        NavServiceParameter param = new NavServiceParameter();
        param.setForceStop(true);
        sendCase(param);
    }
    
    public void setForeground(boolean isForeground)
    {
        if (!isStarted)
            return;
        
        NavServiceParameter param = new NavServiceParameter();
        param.setForeground(isForeground);
        sendCase(param);
    }
    
    public void setRouteId(String routeId)
    {
        if (!isStarted)
            return;
        
        NavServiceParameter param = new NavServiceParameter();
        if (routeId == null || routeId.length() == 0)
            param.setRouteId("-1");
        else
            param.setRouteId(routeId);
        sendCase(param);
    }
    
    public void setLogEnabled(boolean isLogEnabled)
    {
        if (!isStarted)
            return;
        
        NavServiceParameter param = new NavServiceParameter();
        param.setLogEnabled(isLogEnabled);
        sendCase(param);
    }

    protected abstract void sendCase(NavServiceParameter param);
}

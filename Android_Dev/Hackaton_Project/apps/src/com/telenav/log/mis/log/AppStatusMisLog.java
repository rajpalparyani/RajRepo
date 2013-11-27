/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AppStatusMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-31
 */
public class AppStatusMisLog extends AbstractMisLog
{
    private boolean isFirstTimeLogin = false;
    
    private int appStartBattery;

    private int appEndBattery;

    private int totalSearchCount = 0;
    
    private int gpsLossCount = 0;

    private int networkLossCount = 0;
    
    private long gpsLossLastTime = -1;

    private long gpsLossTotalTime = 0;

    private long networkLossLastTime = -1;

    private long networkLossTotalTime = 0;

    private long appStartLat = -1;

    private long appStartLon = -1;

    private long appEndLat = -1;

    private long appEndLon = -1;

    private long appStartTime = 0;

    private long timeToShowHome = 0;
    
    private long loginTime = 0;
    
    private long syncResTime = 0;
    
    private String routeRequestBy = IMisLogConstants.VALUE_ADDRESS_INPUT_TYPE_SEARCH;

    public AppStatusMisLog(String id, int priority)
    {
        super(id, TYPE_INNER_APP_STATUS, priority);
    }

    public void gpsSingalLose()
    {
        if (this.gpsLossLastTime == -1)
        {
            this.gpsLossCount++;
            this.gpsLossLastTime = System.currentTimeMillis();
        }
    }

    public void gpsSingalResume()
    {
        if (this.gpsLossLastTime != -1)
        {
            this.gpsLossTotalTime += (System.currentTimeMillis() - this.gpsLossLastTime);
            this.gpsLossLastTime = -1;
        }
    }

    public int getGpsLossCount()
    {
        return this.gpsLossCount;
    }

    public long getGpsLossTime()
    {
        if (gpsLossLastTime == -1)
        {
            return gpsLossTotalTime;
        }
        else
        {
            return (gpsLossTotalTime + (System.currentTimeMillis() - gpsLossLastTime));
        }
    }

    public void networkLose()
    {
        if (this.networkLossLastTime == -1)
        {
            this.networkLossCount++;
            this.networkLossLastTime = System.currentTimeMillis();
        }
    }

    public void networkResume()
    {
        if (this.networkLossLastTime != -1)
        {
            this.networkLossTotalTime += (System.currentTimeMillis() - this.networkLossLastTime);
            this.networkLossLastTime = -1;
        }
    }

    public int getNetworkLossCount()
    {
        return this.gpsLossCount;
    }

    public long getNetworkLossTime()
    {
        if (networkLossLastTime == -1)
        {
            return networkLossTotalTime;
        }
        else
        {
            return (networkLossTotalTime + (System.currentTimeMillis() - networkLossLastTime));
        }
    }

    public void setAppStartLoc(TnLocation loc)
    {
        if(loc != null)
        {
            this.appStartLat = loc.getLatitude();
            this.appStartLon = loc.getLongitude();
        }
    }

    public long getAppStartLat()
    {
        return this.appStartLat;
    }

    public long getAppStartLon()
    {
        return this.appStartLon;
    }

    public long getAppEndLat()
    {
        return this.appEndLat;
    }

    public long getAppEndLon()
    {
        return this.appEndLon;
    }

    public void setAppEndLoc(TnLocation loc)
    {
        if(loc != null)
        {
            this.appEndLat = loc.getLatitude();
            this.appEndLon = loc.getLongitude();
        }
    }

    public void setAppStartBattery(int level)
    {
        this.appStartBattery = level;
    }

    public int getAppStartBattery()
    {
        return this.appStartBattery;
    }

    public void setAppEndBattery(int level)
    {
        this.appEndBattery = level;
    }

    public int getAppEndBattery()
    {
        return this.appEndBattery;
    }

    public void setAppStartTime(long time)
    {
        this.appStartTime = time;
    }
    
    public long getAppStartTime()
    {
        return this.appStartTime;
    }

    public void increaseSearchCount()
    {
        this.totalSearchCount++;
    }

    public int getSearchCount()
    {
        return this.totalSearchCount;
    }
    
    public void setHomeShowTime(long time)
    {
        this.timeToShowHome = time -  this.appStartTime;
    }
    
    public long getTimeToShowHome()
    {
        return this.timeToShowHome;
    }
    
    public void setIsFirstTimeLogin(boolean isFirstTimeLogin)
    {
        this.isFirstTimeLogin = isFirstTimeLogin;
    }
    
    public boolean isFirstTimeLogin()
    {
        return this.isFirstTimeLogin;
    }
    
    public void startLogin()
    {
        this.loginTime = System.currentTimeMillis();
    }
    
    public void finishLogin()
    {
        if(this.loginTime != 0)
        {
            this.loginTime = (System.currentTimeMillis() - loginTime);
        }
    }
    
    public long getLoginTime()
    {
        return this.loginTime;
    }
    
    public void startSyncRes()
    {
        this.syncResTime = System.currentTimeMillis();
    }
    
    public void finishSyncRes()
    {
        if(this.syncResTime != 0)
        {
            this.syncResTime = (System.currentTimeMillis() - syncResTime);
        }
    }
    
    public long getSyncResTime()
    {
        return this.syncResTime;
    }
    
    public String getRouteRequestBy()
    {
        return routeRequestBy;
    }

    public void setRouteRequestBy(String routeRequestBy)
    {
        this.routeRequestBy = routeRequestBy;
    }
    
}

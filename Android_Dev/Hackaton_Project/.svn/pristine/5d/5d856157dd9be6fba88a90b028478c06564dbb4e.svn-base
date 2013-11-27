/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AppSessionMislog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class AppSessionMisLog extends AbstractMisLog
{
    public AppSessionMisLog(String id, int priority)
    {
        super(id, TYPE_APP_SESSION_SUMMARY, priority);
    }
    
    public void processLog()
    {
        super.processLog();
        AppStatusMisLog statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);

        setTimestamp(statuslog.getAppStartTime());
        setAppSessionTime(System.currentTimeMillis() - statuslog.getAppStartTime());
        setzGpsLossCount(statuslog.getGpsLossCount());
        setzGpsLossTime(statuslog.getGpsLossTime());
        setzNetworkLossCount(statuslog.getNetworkLossCount());
        setzNetworkLossTime(statuslog.getNetworkLossTime());
        setzLat(statuslog.getAppStartLat());
        setzLon(statuslog.getAppStartLon());
        setzEndLat(statuslog.getAppEndLat());
        setzEndLon(statuslog.getAppEndLon());
        setzBatteryStart(statuslog.getAppStartBattery());
        setzBatteryEnd(statuslog.getAppEndBattery());
        setSearchCount(statuslog.getSearchCount());
        setTimeToShowHome(statuslog.getTimeToShowHome());
    }

    public void setAppSessionTime(long time)
    {
        this.setzDurationLength(time);
    }

    public void setSearchCount(int count)
    {
        this.setAttribute(IMisLogConstants.ATTR_APP_SESSION_SEARCH_COUNT, String.valueOf(count));
    }

    public void setTimeToShowHome(long time)
    {
        this.setAttribute(IMisLogConstants.ATTR_APP_SESSION_TIME_TO_SHOW_HOME, String.valueOf(time));
    }
    
    public void setTimeToShowHome(String exitPage)
    {
        this.setAttribute(IMisLogConstants.ATTR_APP_SESSION_EXIT_PAGE, exitPage);
    }
}

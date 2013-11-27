/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * HomeScreenTimeMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class HomeScreenTimeMisLog extends AbstractMisLog
{
    
    public HomeScreenTimeMisLog(String id, int priority)
    {
        super(id, TYPE_HOME_SCREEN_TIME, priority);
    }

    public void processLog()
    {
        super.processLog();
        AppStatusMisLog statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
        setzLat(statuslog.getAppStartLat());
        setzLon(statuslog.getAppStartLon());
        setzDurationLength(statuslog.getTimeToShowHome());
        setIsFirstTimeLogin(statuslog.isFirstTimeLogin() ? "1" : "0");
    }
	
    public void setIsFirstTimeLogin(String flag)
    {
        this.setAttribute(ATTR_IS_FIRST_TIME_LOGIN, flag);
    }
    
    public void setTime1(long time)
    {
        this.setAttribute(ATTR_TIME_1, String.valueOf(time));
    }

    public void setTime2(long time)
    {
        this.setAttribute(ATTR_TIME_2, String.valueOf(time));
    }

}

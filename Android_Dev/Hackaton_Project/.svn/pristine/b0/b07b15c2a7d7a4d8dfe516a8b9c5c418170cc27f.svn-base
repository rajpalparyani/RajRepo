/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FirstTimeLoginMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class FirstTimeLoginMisLog extends AbstractMisLog
{
    public FirstTimeLoginMisLog(String id, int priority)
    {
        super(id, TYPE_FIRST_TIME_LOGIN, priority);
    }

    public void processLog()
    {
        super.processLog();
        AppStatusMisLog statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
        setzLat(statuslog.getAppStartLat());
        setzLon(statuslog.getAppStartLon());
        setTime1(statuslog.getLoginTime());
        setTime2(statuslog.getSyncResTime());
        setzDurationLength(statuslog.getLoginTime() + statuslog.getSyncResTime());
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

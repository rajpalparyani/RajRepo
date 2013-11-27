/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NavsdkDefaultMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.INavsdkMisLog;
import com.telenav.logger.Logger;
import com.telenav.navsdk.events.MISLogData.MISLogItem;
import com.telenav.navsdk.events.MISLogEvents.SendMISLog;

/**
 *@author pwang
 *@date 2013-1-11
 */
public class NavsdkDefaultMisLog extends DefaultMisLog implements INavsdkMisLog
{

    public NavsdkDefaultMisLog(String id, int type, int priority)
    {
        super(id, type, priority);
    }

    public void navsdkPreProcess(SendMISLog navsdkMisLog)
    {
        for (int i = 0; i < navsdkMisLog.getLogitemsCount(); i++)
        {
            MISLogItem logItem = navsdkMisLog.getLogitems(i);
            this.setAttribute(logItem.getAttr(), logItem.getValue());
        }
        Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
        { this });
    }

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RTTMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.app.CommManager;
import com.telenav.comm.Host;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.INavsdkMisLog;
import com.telenav.log.mis.helper.AbstractMisLogHelper;
import com.telenav.logger.Logger;
import com.telenav.navsdk.events.MISLogData.MISLogItem;
import com.telenav.navsdk.events.MISLogEvents.SendMISLog;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class RttMisLog extends AbstractMisLog implements INavsdkMisLog
{
    String requestid;

    String requestfile;

    public RttMisLog(String id, int priority, AbstractMisLogHelper helper)
    {
        super(id, TYPE_RTT, priority, helper);
    }

    public void setRequestID(String id)
    {
        this.requestid = id;
    }

    public String getRequestID()
    {
        return this.requestid;
    }

    public void setRequestFile(String file)
    {
        this.requestfile = file;
    }

    public String getRequestFile()
    {
        return this.requestfile;
    }

    public void setRequestSize(int size)
    {
        this.setAttribute(ATTR_REQUEST_SIZE, String.valueOf(size));
    }

    public void setResponseSize(int size)
    {
        this.setAttribute(ATTR_RESPONSE_SIZE, String.valueOf(size));
    }

    public void setRoundTripTime(long time)
    {
        this.setAttribute(ATTR_ROUNDTRIP_TIME, String.valueOf(time));
    }

    public void setMisActionID(String misActionID)
    {
        this.setAttribute(ATTR_RTT_ACTION_ID, misActionID);
    }

    public void setMisActionName(String misActionName)
    {
        this.setAttribute(ATTR_RTT_ACTION_NAME, misActionName);
    }

    public void navsdkPreProcess(SendMISLog navsdkMisLog)
    {
        for(int i = 0; i < navsdkMisLog.getLogitemsCount(); i++)
        {
            MISLogItem logItem = navsdkMisLog.getLogitems(i);
            if(logItem.getAttr() == IMisLogConstants.ATTR_RTT_ACTION_NAME)
            {
                String action = logItem.getValue();
                this.setRequestID(action);
                Host host = CommManager.getInstance().getComm().getHostProvider().createHost(action);
                this.setRequestFile(host.file);
            }
            else
            {
                this.setAttribute(logItem.getAttr(), logItem.getValue());
            }
        }
        Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                { this });
    }

}

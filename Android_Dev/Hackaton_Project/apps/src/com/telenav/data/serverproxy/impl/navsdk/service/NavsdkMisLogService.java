/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavsdkAudioService.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.service;

import com.telenav.log.mis.INavsdkMisLog;
import com.telenav.log.mis.MisLogManager;
import com.telenav.logger.Logger;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.MISLogData.MISLogItem;
import com.telenav.navsdk.events.MISLogEvents.SendMISLog;
import com.telenav.navsdk.events.MISLogEvents.SendMISLogArray;
import com.telenav.navsdk.services.MISLogListener;
import com.telenav.navsdk.services.MISLogServiceProxy;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2012-12-26
 */
public class NavsdkMisLogService implements MISLogListener
{
    /** Client will send the log of this type as while as received */
    public static final int NAVSDK_MISLOG_INSTANT_PROCESS = 1;

    /** Client will create and store the log of this type temporarily, until client fill up all other necessary fields */
    public static final int NAVSDK_MISLOG_PRE_PROCESS = 2;

    /** Client will get the specific mislog and complete the log with received attributes and send at once */
    public static final int NAVSDK_MISLOG_POST_PROCESS = 3;

    /**
     * Client will get the specific mislog and fill the log of this type with received attributes and reserve until it
     * is complete
     */
    public static final int NAVSDK_MISLOG_PARTIAL_PROCESS = 4;

    private static NavsdkMisLogService instance;

    private MISLogServiceProxy serverProxy;

    private NavsdkMisLogService()
    {
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavsdkMisLogService();
            instance.setEventBus(bus);
        }
    }

    public static NavsdkMisLogService getInstance()
    {
        return instance;
    }

    private void setEventBus(EventBus bus)
    {
        serverProxy = new MISLogServiceProxy(bus);
        serverProxy.addListener(this);
    }

    public void onSendMISLog(SendMISLog event)
    {
        handle(event);
    }

    public void onSendMISLogArray(SendMISLogArray event)
    {
        handle(event);
    }

    private void handle(SendMISLog event)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "Casper:___received mislog with type :" + event.getType());
        StringBuffer values = new StringBuffer();
        for (int i = 0; i < event.getLogitemsCount(); i++)
        {
            MISLogItem logItem = event.getLogitems(i);
            values.append(logItem.getAttr() + ":" + logItem.getValue() + ",");
            values.append(":");
            values.append(logItem.getValue());
            values.append(",");
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "Casper: with values: " + values);
        INavsdkMisLog misLog = MisLogManager.getInstance().getFactory().createNavsdkMisLog(event.getType());
        misLog.navsdkPreProcess(event);
    }

    private void handle(SendMISLogArray event)
    {
        for (int i = 0; i < event.getLogarrayCount(); i++)
        {
            handle(event.getLogarray(i));
        }
    }
}

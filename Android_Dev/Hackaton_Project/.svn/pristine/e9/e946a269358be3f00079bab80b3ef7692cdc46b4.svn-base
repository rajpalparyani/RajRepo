/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FeedbackMislog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.telephony.TnBatteryManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class FeedbackMisLog extends AbstractMisLog
{

    public FeedbackMisLog(String id, int priority)
    {
        super(id, TYPE_FEEDBACK, priority);
    }
    
    public void processLog()
    {
        super.processLog();
        AppStatusMisLog statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);

        setzGpsLossCount(statuslog.getGpsLossCount());
        setzGpsLossTime(statuslog.getGpsLossTime());
        setzNetworkLossCount(statuslog.getNetworkLossCount());
        setzNetworkLossTime(statuslog.getNetworkLossTime());
        setzBatteryStart(statuslog.getAppStartBattery());
        setzBatteryEnd(TnBatteryManager.getInstance().getBatteryLevel());
    }

    public void setAppContext(String context)
    {
        this.setAttribute(ATTR_FEEDBACK_APP_CONTEXT, context);
    }

    public void setUseCase(String useCase)
    {
        this.setAttribute(ATTR_FEEDBACK_USE_CASE, useCase);
    }

    public void setInvokedBy(String invokedBy)
    {
        this.setAttribute(ATTR_FEEDBACK_INVOKED, invokedBy);
    }

    public void setType(String type)
    {
        this.setAttribute(ATTR_FEEDBACK_TYPE, type);
    }

    public void setSecondaryContext(String context)
    {
        this.setAttribute(ATTR_FEEDBACK_SECONDARY_CONTEXT, context);
    }
}

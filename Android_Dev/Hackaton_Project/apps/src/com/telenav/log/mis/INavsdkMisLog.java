/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * INavsdkMisLog.java
 *
 */
package com.telenav.log.mis;

import com.telenav.navsdk.events.MISLogEvents.SendMISLog;


/**
 *@author pwang
 *@date 2013-1-11
 */
public interface INavsdkMisLog
{
    public void navsdkPreProcess(SendMISLog navsdkMisLog);
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ClickStreamMisLog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class ClickStreamMisLog extends AbstractMisLog
{

    public ClickStreamMisLog(String id, int priority)
    {
        super(id, TYPE_CLICK_STREAM, priority);
    }
    
    public void addAction(long timeStamp, String clickId)
    {
        this.setAttribute(timeStamp, clickId);
    }

}

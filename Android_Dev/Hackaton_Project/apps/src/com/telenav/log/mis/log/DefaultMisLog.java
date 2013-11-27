/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * DefaultMisLog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-1-19
 */
public class DefaultMisLog extends AbstractMisLog
{
    public DefaultMisLog(String id, int type, int priority)
    {
        super(id, type, priority);
    }
    
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}

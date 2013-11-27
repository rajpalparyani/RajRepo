/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DsrGenericMisLog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class DsrGenericMisLog extends AbstractMisLog
{
    int attemptCount = 0;

    long startRecordTime = 0;

    public DsrGenericMisLog(String id, int priority)
    {
        super(id, TYPE_DSR_GENERIC, priority);
    }
    
    public void startRecord()
    {
        this.startRecordTime = System.currentTimeMillis();
    }

    public void stopRecord()
    {
        if (this.startRecordTime != 0)
        {
            this.setRecordDurationTime(System.currentTimeMillis() - startRecordTime);
        }
    }
    
    public void increaseAttemptCount()
    {
        attemptCount++;
    }
    
    public int getAttemptCount()
    {
        return attemptCount;
    }

    public void setUseCase(String useCase)
    {
        this.setAttribute(ATTR_DSR_USE_CASE, useCase);
    }
    
    public void setAttemptCount(int count)
    {
        this.setAttribute(ATTR_DSR_ATTEMPT_COUNT, String.valueOf(count));
    }

    public void setRecordDurationTime(long time)
    {
        this.setzDurationLength(time);
    }

}

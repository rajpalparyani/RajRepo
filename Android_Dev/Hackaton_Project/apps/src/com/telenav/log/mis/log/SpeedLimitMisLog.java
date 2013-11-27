/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SpeedLimitMisLog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class SpeedLimitMisLog extends AbstractMisLog
{
    private int speedLimitCount = 0;

    public SpeedLimitMisLog(String id, int priority)
    {
        super(id, TYPE_SPEED_LIMIT_IMPRESSION, priority);
    }
    
    public void processLog()
    {
        super.processLog();
        setSpeedLimitCount(speedLimitCount);
    }

    public void increaseSpeedLimitCount()
    {
        this.speedLimitCount++;
    }

    public void setSpeedLimitCount(int count)
    {
        this.setAttribute(ATTR_SPEED_LIMIT_CNT, String.valueOf(count));
    }

    public void setLimitSegmentId(int id)
    {
        this.setAttribute(ATTR_SPEED_LIMIT_SEGMENT_ID, String.valueOf(id));
    }

    public void setAverageSpeed(int avgSpeed)
    {
        this.setAttribute(ATTR_SPEED_AVG, String.valueOf(avgSpeed));
    }

    public void setMaxSpeed(int maxSpeed)
    {
        this.setAttribute(ATTR_SPEED_MAX, String.valueOf(maxSpeed));
    }

    public void setLimitSpeed(int limitSpeed)
    {
        this.setAttribute(ATTR_SPEED_LIMIT, String.valueOf(limitSpeed));
    }

    public void setAlertAverageTime(long time)
    {
        this.setAttribute(ATTR_ALERT_AVG_TIME, String.valueOf(time));
    }

    public void setAlertMaxTime(long time)
    {
        this.setAttribute(ATTR_ALERT_AVG_MAX, String.valueOf(time));
    }

    public void setSpeedLimitStartTime(long time)
    {
        this.setAttribute(ATTR_SPEED_LIMIT_START_TIME, String.valueOf(time));
    }

    public void setSpeedLimitEndTime(long time)
    {
        this.setAttribute(ATTR_SPEED_LIMIT_END_TIME, String.valueOf(time));
    }

}

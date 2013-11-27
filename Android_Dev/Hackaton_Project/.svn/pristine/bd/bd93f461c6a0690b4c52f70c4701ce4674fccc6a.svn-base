/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SpeedCameraImpressionMisLog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class SpeedCameraMisLog extends AbstractMisLog
{

    public SpeedCameraMisLog(String id, int priority)
    {
        super(id, TYPE_SPEED_CAMERA_IMPRESSION, priority);
    }
    
    public void setSpeedId(String trapId)
    {
        this.setAttribute(ATTR_SPEED_TRAP_ID, trapId);
    }
    
    public void setSpeedTrapAppScreen(String appScreen)
    {
        this.setAttribute(ATTR_SPEED_TRAP_APP_SCREEN, appScreen);
    }
    
    public void setSpeedTrapAlert(boolean alert)
    {
        this.setAttribute(ATTR_SPEED_TRAP_ALERT, alert ? "Yes" : "No");
    }
    

}

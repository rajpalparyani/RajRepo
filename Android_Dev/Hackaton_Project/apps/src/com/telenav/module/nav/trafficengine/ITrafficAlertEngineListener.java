/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITrafficAlertEngineListener.java
 *
 */
package com.telenav.module.nav.trafficengine;

/**
 * 
 * The traffic alert engine listener will help handling traffic alert events.
 * 
 *@author zhdong@telenav.cn
 *@date 2010-12-15
 */
public interface ITrafficAlertEngineListener
{
    /**
     * Notify that an audio event is generated.
     * 
     * @param audioEvent
     */
    public void handleTrafficAudio(TrafficAudioEvent audioEvent);

    /**
     * Notify that an traffic alert event is generated.
     * 
     * @param alertEvent
     */
    public void handleTrafficAlert(TrafficAlertEvent alertEvent);
}

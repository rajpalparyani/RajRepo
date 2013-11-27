/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficAudioEvent.java
 *
 */
package com.telenav.module.nav.trafficengine;

import com.telenav.datatypes.traffic.TrafficIncident;

/**
 *@author zhdong@telenav.cn
 *@date 2010-12-16
 */
public class TrafficAudioEvent
{

    /**
     * Specific incident audio
     */
    public static final byte TRAFFIC_AUDIO_TYPE_INCIDENT = 0;

    /**
     * Audio to notify user that the app is retrieving alerts.
     */
    public static final byte TRAFFIC_AUDIO_TYPE_FETCH = 1;

    private int distance;

    private byte audioType;

    private TrafficIncident incident;

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public byte getAudioType()
    {
        return audioType;
    }

    public void setAudioType(byte type)
    {
        this.audioType = type;
    }

    public TrafficIncident getTrafficIncident()
    {
        return incident;
    }

    public void setTrafficIncident(TrafficIncident incident)
    {
        this.incident = incident;
    }

}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficAlertEvent.java
 *
 */
package com.telenav.module.nav.trafficengine;

import com.telenav.location.TnLocation;

/**
 *@author zhdong@telenav.cn
 *@date 2010-12-16
 */
public class TrafficAlertEvent
{
    private int severity;

    private int distance;

    private int delay;

    private byte incidentType;

    private boolean isAvoidIncidentResultInFasterRoute = true;
    
    private String message;
    
    private TnLocation incidentLocation;
    
    private long incidentId;
    
    public long getIncidentId()
    {
        return incidentId;
    }

    public void setIncidentId(long incidentId)
    {
        this.incidentId = incidentId;
    }

    public TnLocation getIncidentLocation()
    {
        return incidentLocation;
    }

    public void setIncidentLocation(TnLocation incidentLocation)
    {
        this.incidentLocation = incidentLocation;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getSeverity()
    {
        return severity;
    }

    public void setSeverity(int severity)
    {
        this.severity = severity;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public int getTrafficDelay()
    {
        return delay;
    }

    public void setTrafficDelay(int trafficDelay)
    {
        this.delay = trafficDelay;
    }

    public byte getIncidentType()
    {
        return incidentType;
    }

    public void setIncidentType(byte incidentType)
    {
        this.incidentType = incidentType;
    }

    /**
     * this flag indicate client should disable or enable avoiding traffic incident
     * 
     * [Heads up] should be only used for traffic alert icon on navigation map screen
     */
    public boolean isAvoidIncidentResultInFasterRoute()
    {
        return isAvoidIncidentResultInFasterRoute;
    }

    public void setIsAvoidIncidentResultInFasterRoute(boolean flag)
    {
        isAvoidIncidentResultInFasterRoute = flag;
    }

}

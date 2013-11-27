/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ICommuteAlertProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.traffic.TrafficSegment;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface ICommuteAlertProxy
{
    public String requestCommuteAlertDetails(long alertId);

    public String requestTrafficSummary(long alertId);
    
    public Stop getCommuteAlertDest();
    
    public Stop getCommuteAlertOrig();
    
    public long getReportTime();
    
    public long getLastUpdateDurationTime();
    
    public TrafficSegment[] getSegments();
    
    public int getSeverity();
    
    public int getTotalDelay();
    
    public int getTotalIncident();
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITrafficProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface ITrafficProxy
{
    public static final int TYPE_TRAFFIC_SEGMENT = 55;

    public static final int TYPE_TRAFFIC_INCIDENT = 54;
    
    public Vector getIncidents();

    public long getReportTime();

    public long getLastUpdateDurationTime();

    public TrafficSegment[] getSegments();

    public int getSeverity();

    public int getTotalDelay();

    public int getTotalIncident();

    public boolean isAvoidIncidentsResultInFasterRoute();
    
    public String requestAlertsMovingMap(int routeID, int segmentIndexID, int edgeID, String[] alertIDs, TnLocation[] fixes);
    
    public String requestAlertsMovingMap(int routeID, int segmentIndexID, int edgeID, TnLocation[] fixes);
    
    public String requestTrafficSummary(int routeID, int routeSegmentIndex, int edgeIndex, String[] alertIDs);
}

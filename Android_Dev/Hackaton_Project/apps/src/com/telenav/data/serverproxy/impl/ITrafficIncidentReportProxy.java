/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITrafficIncidentReportProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface ITrafficIncidentReportProxy
{
    public static final int INCIDENT_TYPE_SPEED_TRAP = 1;
    public static final int INCIDENT_TYPE_TRAFFIC_CAMERA = 2;
    
    public String sendIncidentMapReport(int incidentType, TnLocation gpsFix);
    
    public String sendIncidentNavReport(int incidentType, TnLocation gpsFix);
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavParameter.java
 *
 */
package com.telenav.module.nav;

import com.telenav.location.TnLocation;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.navsdk.events.NavigationData;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-29
 */
public class NavParameter
{
    public String nextStreetName;
    public String nextStreetAlias;    
    public String currStreetName;
    public int distanceToTurn;
    public long tripDist;
    public String sector;
    public int headingAngle;
    public int numSats;
//    public int nextTurnImageId;
    public int turnType;
    public int nextNextTurnType;
    public String nextNextSteetName;
    public String nextNextExitName;
    public byte nextNextExitNumber;
//    public int speed;
    public int speedLimit;
    public boolean isSpeedLimitExceeded;
//    public int speedLimitKph;
//    public int speedLimitMph;
    public int[] laneInfos;
    public NavigationData.RouteTurnType[] laneTypes;
    //public boolean isArriveDest;
    public boolean isAdi;
    /**
     * Adi lat, only take effect when isAdi==true
     */
    public int adiLat;
    /**
     * Adi lon, only take effect when isAdi==true
     */
    public int adiLon;
//    public ITrafficAlertEvent trafficAlertEvent;
    public long totalToDest;
    public long eta;
    public boolean isPanning;
    
    /**
     * If it's the first step of TURN MAP, it should be true;
     * We use this to decide which background image should be used for turn Icon.
     */
    public boolean isTurnMapStart;
    
    public String exitName; 
    
    public byte exitNumber;
    
    /**
     * Traffic alert will be shown at the right bottom.
     */
    public TrafficAlertEvent alertEvent;
    
    public boolean isOutOfCoverage;
    
    public boolean isNoSatellite;
    
    public TnLocation vehicalLocation;
    
    public int nextSegmentIndex;
    
    public boolean hideVBB;
    
    public boolean isNextNextTurnValid()
    {
        return nextNextTurnType >= 0;
    }
    
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavsdkNavInfoEvent.java
 *
 */
package com.telenav.navsdk.nav.event;

import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.NavigationData.RouteTurnType;
/**
 *@author yren
 *@date 2012-12-6
 */
public class NavSdkNavEvent
{
    private String currentStreetName;
    private long distanceToDest;
    private int distanceToTurn;
    private int estimatedTime;
    private String nextStreetAlias;
    private String nextStreetName;
    private int nextTurnType;
    private int turnType;
    private int speedLimit;
    private String exitName;
    private int exitNum;
    private TnNavLocation adiLocation;
    private int[] laneInfos;
    private NavigationData.RouteTurnType[] laneTypes;
    private int nextTurnIndex;
    private int eventType;
    private boolean hasDistantToDest;
    
    public final static int TYPE_NAVSDK_NO_DESTINATION = 0;
    public final static int TYPE_NAVSDK_ROUTING = 1;
    public final static int TYPE_NAVSDK_ON_ROUTE = 2;
    public final static int TYPE_NAVSDK_OFF_ROUTE = 3;
    public final static int TYPE_NAVSDK_SHOULD_REROUTE = 4;
    // mock nav sdk event type for speed limit events
    public final static int TYPE_NAVSDK_SPEED_LIMIT_EXCEEDED = 100;
    public final static int TYPE_NAVSDK_CANCEL_LIMIT_EXCEEDED = 101;
    
    
    public NavSdkNavEvent(int eventType)
    {
        this.eventType = eventType;
    }
    
    public int getEventType()
    {
        return this.eventType;
    }
    
    public String getCurrentStreetName()
    {
        return currentStreetName;
    }
    public void setCurrentStreetName(String currentStreetName)
    {
        this.currentStreetName = currentStreetName;
    }
    public long getDistanceToDest()
    {
        return distanceToDest;
    }
    public void setDistanceToDest(double distanceToDest)
    {
        this.distanceToDest = (long) distanceToDest;
    }
    public int getDistanceToTurn()
    {
        return distanceToTurn;
    }
    public void setDistanceToTurn(int distanceToTurn)
    {
        this.distanceToTurn = distanceToTurn;
    }
    public int getEstimatedTime()
    {
        return estimatedTime;
    }
    public void setEstimatedTime(int estimatedTime)
    {
        this.estimatedTime = estimatedTime;
    }
    public String getNextStreetAlias()
    {
        return nextStreetAlias;
    }
    public void setNextStreetAlias(String nextStreetAlias)
    {
        this.nextStreetAlias = nextStreetAlias;
    }
    public String getNextStreetName()
    {
        return nextStreetName;
    }
    public void setNextStreetName(String nextStreetName)
    {
        this.nextStreetName = nextStreetName;
    }
    public int getNextTurnType()
    {
        return nextTurnType;
    }
    public void setNextTurnType(int nextTurnType)
    {
        this.nextTurnType = nextTurnType;
    }
    public int getTurnType()
    {
        return turnType;
    }
    public void setTurnType(int turnType)
    {
        this.turnType = turnType;
    }
    public int getSpeedLimit()
    {
        return speedLimit;
    }
    public void setSpeedLimit(int speedLimitMph)
    {
        this.speedLimit = speedLimitMph;
    }

    public String getExitName()
    {
        return exitName;
    }
    public void setExitName(String exitName)
    {
        this.exitName = exitName;
    }
    public int getExitNum()
    {
        return exitNum;
    }
    public void setExitNum(int exitNum)
    {
        this.exitNum = exitNum;
    }
    public TnNavLocation getAdiLocation()
    {
        return adiLocation;
    }
    public void setAdiLocation(TnNavLocation navLocation)
    {
        this.adiLocation = navLocation;
    }
    public RouteTurnType[] getLaneTypes()
    {
        return laneTypes;
    }
    public void setLaneTypes(RouteTurnType[] laneTypes)
    {
        this.laneTypes = laneTypes;
    }
    public int[] getLaneInfos()
    {
        return laneInfos;
    }

    public void setLaneInfos(int[] laneInfos)
    {
        this.laneInfos = laneInfos;
    }

    public int getNextTurnIndex()
    {
        return nextTurnIndex;
    }
    
    public void setNextTurnIndex(int nextTurnIndex)
    {
        this.nextTurnIndex = nextTurnIndex;
    }
    
    public boolean isHasDistantToDest()
    {
        return hasDistantToDest;
    }

    public void setHasDistantToDest(boolean hasDistantToDest)
    {
        this.hasDistantToDest = hasDistantToDest;
    }
}

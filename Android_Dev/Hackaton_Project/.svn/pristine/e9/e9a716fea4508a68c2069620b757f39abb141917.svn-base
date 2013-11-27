/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavInfoEvent.java
 *
 */
package com.telenav.nav.event;

import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.TnNavLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class NavInfoEvent extends NavEvent
{
    protected TnNavLocation snapPosition;
    
    protected TnNavLocation adiLocation;

    protected NavState routePosition;

    protected String nextStreetName;

    protected String nextStreetAlias;

    protected String currStreetName;

    protected int turnType;

    protected int nextTurnType;

    protected int distanceToTurn;

    protected long distanceToDest;

    protected int headingAngle;

    protected int estimatedTime;

    protected int numSats;
    
    protected int speedLimit;

    protected boolean isArriveDest;
    
    protected String exitName;
    
    protected int exitNum;
    
    protected int[] laneTypes = null;

    public NavInfoEvent(int eventType)
    {
        super(eventType);
    }
    
    public NavInfoEvent(TnNavLocation onRoutePos, int snappedHeading, int eta, String tempNextStreetName1, String tempNextStreetName2,
            int turnToken, int nextTurnToken, int distanceToTurn, long distanceToDest, String tempCurrStreetName, NavState onRoute,
            int newHeading, boolean isArriveDest)
    {
        super(TYPE_INFO);

        this.snapPosition = new TnNavLocation("");
        this.snapPosition.setTime(onRoutePos.getTime());
        this.snapPosition.setLatitude(onRoutePos.getLatitude());
        this.snapPosition.setLongitude(onRoutePos.getLongitude());
        this.snapPosition.setHeading(snappedHeading);

        this.estimatedTime = eta;

        this.nextStreetName = tempNextStreetName1;
        this.nextStreetAlias = tempNextStreetName2;
        this.turnType = turnToken;
        this.nextTurnType = nextTurnToken;
        this.distanceToTurn = distanceToTurn;
        this.distanceToDest = distanceToDest;
        this.currStreetName = tempCurrStreetName;
        this.routePosition = onRoute;
        this.headingAngle = newHeading;
        this.isArriveDest = isArriveDest;
        this.numSats = onRoutePos.getSatellites();
    }

    /**
     * @return Returns the currStreetName.
     */
    public String getCurrStreetName()
    {
        return currStreetName;
    }

    /**
     * @return Returns the distanceToDest.
     */
    public long getDistanceToDest()
    {
        return distanceToDest;
    }

    /**
     * @return Returns the distanceToTurn.
     */
    public int getDistanceToTurn()
    {
        return distanceToTurn;
    }

    /**
     * @return Returns the estimatedTime.
     */
    public int getEstimatedTime()
    {
        return estimatedTime;
    }

    /**
     * @return Returns the headingAngle.
     */
    public int getHeadingAngle()
    {
        return headingAngle;
    }

    /**
     * @return Returns the nextStreetAlias.
     */
    public String getNextStreetAlias()
    {
        return nextStreetAlias;
    }

    /**
     * @return Returns the nextStreetName.
     */
    public String getNextStreetName()
    {
        return nextStreetName;
    }

    /**
     * @return Returns the nextTurnType.
     */
    public int getNextTurnType()
    {
        return nextTurnType;
    }

    /**
     * @return Returns the numSats.
     */
    public int getSatelliteCount()
    {
        return numSats;
    }

    /**
     * @return Returns the routePosition.
     */
    public NavState getRoutePosition()
    {
        return routePosition;
    }

    /**
     * @return Returns the snapPosition.
     */
    public TnNavLocation getSnapPosition()
    {
        return snapPosition;
    }

    /**
     * @return Returns the turnType.
     */
    public int getTurnType()
    {
        return turnType;
    }

    public boolean isArriveDest()
    {
        return isArriveDest;
    }

    public int getNumSats()
    {
        return numSats;
    }

    public void setNumSats(int numSats)
    {
        this.numSats = numSats;
    }

    public void setSnapPosition(TnNavLocation snapPosition)
    {
        this.snapPosition = snapPosition;
    }

    public void setRoutePosition(NavState routePosition)
    {
        this.routePosition = routePosition;
    }

    public void setNextStreetName(String nextStreetName)
    {
        this.nextStreetName = nextStreetName;
    }

    public void setNextStreetAlias(String nextStreetAlias)
    {
        this.nextStreetAlias = nextStreetAlias;
    }

    public void setCurrStreetName(String currStreetName)
    {
        this.currStreetName = currStreetName;
    }

    public void setTurnType(int turnType)
    {
        this.turnType = turnType;
    }

    public void setNextTurnType(int nextTurnType)
    {
        this.nextTurnType = nextTurnType;
    }

    public void setDistanceToTurn(int distanceToTurn)
    {
        this.distanceToTurn = distanceToTurn;
    }

    public void setDistanceToDest(long distanceToDest)
    {
        this.distanceToDest = distanceToDest;
    }

    public void setHeadingAngle(int headingAngle)
    {
        this.headingAngle = headingAngle;
    }

    public void setEstimatedTime(int estimatedTime)
    {
        this.estimatedTime = estimatedTime;
    }

    public void setArriveDest(boolean isArriveDest)
    {
        this.isArriveDest = isArriveDest;
    }

    public int getSpeedLimitKph()
    {
        return speedLimit;
    }
    
    public int getSpeedLimitMph()
    {
        return speedLimit;
    }
    
    public int getSpeedLimit()
    {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit)
    {
        this.speedLimit = speedLimit;
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

    public void setAdiLocation(TnNavLocation adiLocation)
    {
        this.adiLocation = adiLocation;
    }

    public int[] getLaneTypes()
    {
        return laneTypes;
    }

    public void setLaneTypes(int[] laneTypes)
    {
        this.laneTypes = laneTypes;
    }
}

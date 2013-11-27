/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavAdiEvent.java
 *
 */
package com.telenav.nav.event;

import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class NavAdiEvent extends NavInfoEvent
{
    protected TnNavLocation userLocation;

    protected NavState entryPoint;

    protected boolean isFirstShowAdi;

    public NavAdiEvent(TnNavLocation userLocation, NavState entryPoint)
    {
        super(TYPE_ADI);

        this.entryPoint = entryPoint;
        this.userLocation = userLocation;
        this.snapPosition = this.userLocation;
    }

    public NavAdiEvent(TnNavLocation userLocation, NavState entryPoint, String tempNextStreetName1, String tempNextStreetName2, int turnToken,
            int nextTurnToken, int distanceToTurn, long distanceToDest, NavState onRoute, int newHeading, int eta, boolean isFirstShowAdi)
    {
        this(userLocation, entryPoint);

        this.nextStreetName = tempNextStreetName1;
        this.nextStreetAlias = tempNextStreetName2;
        this.turnType = turnToken;
        this.nextTurnType = nextTurnToken;
        this.distanceToTurn = distanceToTurn;
        this.distanceToDest = distanceToDest;
        this.routePosition = onRoute;
        this.headingAngle = newHeading;
        this.estimatedTime = eta;
        this.isFirstShowAdi = isFirstShowAdi;
    }

    public TnLocation getUserLocation()
    {
        return this.userLocation;
    }

    public NavState getRouteEntryPoint()
    {
        return this.entryPoint;
    }

    public boolean isFirstShowAdi()
    {
        return isFirstShowAdi;
    }
}

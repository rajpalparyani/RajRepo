package com.telenav.nav.spt.event;

import com.telenav.datatypes.nav.NavState;

public class SptNavInfoEvent extends SptNavEvent
{
    protected NavState routePosition;

    protected long distanceToDest;

    public SptNavInfoEvent(NavState onRoute,long distanceToDest)
    {
        super(TYPE_INFO);

        this.distanceToDest = distanceToDest;
        this.routePosition = onRoute;
    }
 
    /**
     * @return Returns the distanceToDest.
     */
    public long getDistanceToDest()
    {
        return distanceToDest;
    }

    /**
     * @return Returns the routePosition.
     */
    public NavState getRoutePosition()
    {
        return routePosition;
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavAudioEvent.java
 *
 */
package com.telenav.nav.event;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class NavAudioEvent extends NavEvent
{
    public static final int HEADING_NORTH = 1;

    public static final int HEADING_EAST = 2;

    public static final int HEADING_SOUTH = 3;

    public static final int HEADING_WEST = 4;

    private int currSegIndex;

    private int distToTurn;

    private byte audioType;

    private int turnType;

    private int heading;

    private boolean isPlayImmediately;

    private int currEdgeIndex;
    
    private int routeId;

    public NavAudioEvent(int routeId, int currSegIndex, int distToTurn, byte audioType, int turnType, int heading, boolean isPlayImmediately)
    {
        super(TYPE_AUDIO);

        this.routeId = routeId;
        this.currSegIndex = currSegIndex;
        this.distToTurn = distToTurn;
        this.audioType = audioType;
        this.turnType = turnType;
        this.heading = heading;
        this.isPlayImmediately = isPlayImmediately;
    }

    public int getCurrentSegmentIndex()
    {
        return this.currSegIndex;
    }

    public int getDistanceToTurn()
    {
        return this.distToTurn;
    }

    public byte getAudioType()
    {
        return this.audioType;
    }

    public int getRoute()
    {
        return this.routeId;
    }

    public boolean isPlayImmediately()
    {
        return isPlayImmediately;
    }

    public int getTurnType()
    {
        return turnType;
    }

    public int getHeadingType()
    {
        return getHeadingType(heading);
    }

    private int getHeadingType(int heading)
    {
        int headingType = -1;
        if (heading > 45 && heading <= 135)
            headingType = HEADING_EAST;
        else if (heading > 135 && heading <= 225)
            headingType = HEADING_SOUTH;
        else if (heading > 225 && heading <= 315)
            headingType = HEADING_WEST;
        else if (heading > 315 && heading <= 360 || heading >= 0 && heading <= 45)
            headingType = HEADING_NORTH;

        return headingType;
    }

    public int getCurrentEdgeIndex()
    {
        return currEdgeIndex;
    }
}

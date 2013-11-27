/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TrafficSegmentTest.java
 *
 */
package com.telenav.datatypes.traffic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-11
 */
public class TrafficSegmentTest
{
    TrafficSegment seg;

    @Before
    public void setup()
    {
        seg = new TrafficSegment();
    }

    @Test
    public void testAvgSpeed()
    {
        int avgSpeed = 400;
        seg.setAvgSpeed(avgSpeed);
        assertEquals(avgSpeed, seg.getAvgSpeed());
    }

    @Test
    public void testDelay()
    {
        int delay = 300;
        seg.setDelay(delay);
        assertEquals(delay, seg.getDelay());
    }

    @Test
    public void testIncidents()
    {
        TrafficIncident[] incidents = new TrafficIncident[3];
        for (int i = 0; i < incidents.length; i++)
        {
            TrafficIncident t = new TrafficIncident();
            incidents[i] = t;
        }
        seg.setIncidents(incidents);
        assertArrayEquals(incidents, seg.getIncidents());
    }

    @Test
    public void testJamTrend()
    {
        int jamTrend = -1;
        seg.setJamTrend(jamTrend);
        assertEquals(jamTrend, seg.getJamTrend());
    }

    @Test
    public void testLength()
    {
        int length = 400;
        seg.setLength(length);
        assertEquals(length, seg.getLength());
    }

    @Test
    public void testName()
    {
        String name = "name";
        seg.setName(name);
        assertEquals(name, seg.getName());
    }

    @Test
    public void testPostedSpeed()
    {
        int postedSpeed = 120;
        seg.setPostedSpeed(postedSpeed);
        assertEquals(postedSpeed, seg.getPostedSpeed());
    }

    @Test
    public void testSlowestSpeed()
    {
        int slowestSpeed = 20;
        seg.setSlowestSpeed(slowestSpeed);
        assertEquals(slowestSpeed, seg.getSlowestSpeed());
    }

    @Test
    public void testTime()
    {
        long time = 25;
        seg.setTime(time);
        assertEquals(time, seg.getTime());
    }

    @Test
    public void testTmcIDs()
    {
        String[] tmcIds = new String[]
        { "105+04400::78400005548584%78400005548585%78400005521263", "105P04399::78400005549250%78400005548580",
                "105P04399::78400005542781", "105P04399::78400005548577%78400005548578" };
        seg.setTmcIDs(tmcIds);
        assertArrayEquals(tmcIds, seg.getTmcIDs());
    }
    
    @Test
    public void testTravelTime()
    {
        int travelTime = 61;
        seg.setTravelTime(travelTime);
        assertEquals(travelTime, seg.getTravelTime());
    }
}

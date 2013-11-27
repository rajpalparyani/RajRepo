/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TrafficIncidentTest.java
 *
 */
package com.telenav.datatypes.traffic;

import org.junit.Before;
import org.junit.Test;

import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-11
 */
public class TrafficIncidentTest
{
    TrafficIncident incident;

    @Before
    public void setup()
    {
        incident = new TrafficIncident();
    }

    @Test
    public void testCrossSt()
    {
        String crossSt = "wolf street";
        incident.setCrossSt(crossSt);
        assertEquals(crossSt, incident.getCrossSt());
    }

    @Test
    public void testDelay()
    {
        int delay = 1000;
        incident.setDelay(delay);
        assertEquals(delay, incident.getDelay());
    }

    @Test
    public void testDistJudgeThreshold()
    {
        int distJudgeThreshold = 50;
        incident.setDistJudgeThreshold(distJudgeThreshold);
        assertEquals(distJudgeThreshold, incident.getDistJudgeThreshold());
    }

    @Test
    public void testEarchLocation()
    {
        int[] earchLocation = new int[]
        { 3737392, -12201074 };
        incident.setEarthLocation(earchLocation);
        assertArrayEquals(earchLocation, incident.getEarchLocation());
    }

    @Test
    public void testEdgeID()
    {
        int edgeID = 9999;
        incident.setEdgeID(edgeID);
        assertEquals(edgeID, incident.getEdgeID());
    }

    @Test
    public void testId()
    {
        long id = 56;
        String idStr = String.valueOf(id);
        incident.setId(id);
        assertEquals(idStr, incident.getId());
    }

    @Test
    public void testIncidentType()
    {
        byte incidentType = TrafficIncident.TYPE_CONGESTION;
        incident.setIncidentType(incidentType);
        assertEquals(incidentType, incident.getIncidentType());
    }

    @Test
    public void testLaneClosed()
    {
        byte laneClosed = 3;
        incident.setLaneClosed(laneClosed);
        assertEquals(laneClosed, incident.getLaneClosed());
    }

    @Test
    public void testLatLon()
    {
        int lat = 3737392;
        int lon = -12201074;
        incident.setLat(lat);
        incident.setLon(lon);
        assertEquals(lat, incident.getLat());
        assertEquals(lon, incident.getLon());
    }

    @Test
    public void testLocationAudio()
    {
        byte[] fakeData = new byte[]
        { 1, 2, 3, 4, 5, 6, 7 };

        AudioData audioData = AudioDataFactory.getInstance().createAudioData(fakeData);
        AudioDataNode node = AudioDataFactory.getInstance().createAudioDataNode(audioData);

        incident.setLocationAudio(node);
        assertEquals(node, incident.getLocationAudio());
    }

    @Test
    public void testMsg()
    {
        String msg = "traffic msg";
        incident.setMsg(msg);
        assertEquals(msg, incident.getMsg());
    }

    @Test
    public void testOptionIndex()
    {
        int optionIndex = 3;
        incident.setOptionIndex(optionIndex);
        assertEquals(optionIndex, incident.getOptionIndex());
    }

    @Test
    public void testScreenPos()
    {
        int[] pos = new int[]
        { 100, 100 };
        incident.setScreenPos(pos);
        assertArrayEquals(pos, incident.getScreenPos());
    }

    @Test
    public void testSeverity()
    {
        byte severity = TrafficIncident.SEVERITY_MINOR;
        incident.setSeverity(severity);
        assertEquals(severity, incident.getSeverity());
    }

    @Test
    public void testStreetAudio()
    {
        byte[] fakeData = new byte[]
        { 1, 2, 3, 4, 5, 6, 7 };

        AudioData audioData = AudioDataFactory.getInstance().createAudioData(fakeData);
        AudioDataNode node = AudioDataFactory.getInstance().createAudioDataNode(audioData);
        
        incident.setStreetAudio(node);
        assertEquals(node, incident.getStreetAudio());
    }
    
    @Test
    public void testIsAvoidIncidentResultInFasterRoute()
    {
        incident.setIsAvoidIncidentResultInFasterRoute(true);
        assertTrue(incident.isAvoidIncidentResultInFasterRoute());
        incident.setIsAvoidIncidentResultInFasterRoute(false);
        assertFalse(incident.isAvoidIncidentResultInFasterRoute());
    }
    
    @Test
    public void testFocused()
    {
        incident.setFocused(false);
        assertFalse(incident.isFocused());
        incident.setFocused(true);
        assertTrue(incident.isFocused());
    }
}

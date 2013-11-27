/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestTnGpsFilter.java
 *
 */
package com.telenav.gps;

import com.telenav.location.TnLocation;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-15
 */
public class TnGpsFilterTest extends TestCase
{
    public void testCheckPrecise()
    {
        //construct a valid location.
        int latitude = 3737392;
        int longitude = -12201074;
        
        TnLocation lastFix = new TnLocation("");
        long localTime = lastFix.getLocalTimeStamp();
        long time = System.currentTimeMillis() / 10;
        lastFix.setTime(time);
        lastFix.setValid(true);
        
        TnGpsFilter tnGpsFilter = new TnGpsFilter();
        
        //First fix is always valid.
        TnLocation locationToTest = new TnLocation("");
        locationToTest.setTime(time);
        locationToTest.setValid(true);
        assertTrue(tnGpsFilter.checkPrecise(locationToTest));
        
        //case 1. if current fix is newer than the last fix up to more than 50000ms, 
        //reset the fix buffer and set current fix. return true;
        locationToTest.setTime(time + 5100);
        assertTrue(tnGpsFilter.checkPrecise(locationToTest));
        
        //case 2. if location time or local time of current fix is older than last fix,
        //reset the fix buffer and return false.
        locationToTest.setTime(time - 1000);
        assertFalse(tnGpsFilter.checkPrecise(locationToTest));
        locationToTest.setTime(time);
        //First fix is alway valid.
        assertTrue(tnGpsFilter.checkPrecise(lastFix));
        locationToTest.setLocalTimeStamp(localTime - 1000);
        assertFalse(tnGpsFilter.checkPrecise(locationToTest));
        
        
        //case 3. Max distance is the distance calculated by the delta time times the max speed of the last speed and the current fix.
        //if the real distance of the two fixes are further than max distance, reset buffer and return false.
        //else, return false if buffer is not full; return true if buffer is full.
        
        //First fix is always true.
        lastFix.setLatitude(latitude);
        lastFix.setLongitude(longitude);
        lastFix.setSpeed(5);
        assertTrue(tnGpsFilter.checkPrecise(lastFix));
        //it will pass time check for sure. Valid time delta: 0s ~ 50000s
        locationToTest.setTime(time + 500);
        locationToTest.setLocalTimeStamp(localTime + 5000);
        
        //MaxDistance is 275000.
        locationToTest.setSpeed(10);
        //distance is 11520000.
        locationToTest.setLatitude(latitude + 1000);
        locationToTest.setLongitude(longitude + 1000);
        //reset old fixes. Return false.
        assertFalse(tnGpsFilter.checkPrecise(locationToTest));
        
        //First fix is always true.
        lastFix.setLatitude(latitude);
        lastFix.setLongitude(longitude);
        assertTrue(tnGpsFilter.checkPrecise(lastFix));
        //distance is 9000.
        locationToTest.setLatitude(latitude + 1);
        locationToTest.setLongitude(longitude + 1);
        //valid, but buffer is not full, return false. current fixes: 2.
        assertFalse(tnGpsFilter.checkPrecise(locationToTest));
        
        tnGpsFilter = new TnGpsFilter();
        lastFix.setSpeed(5);
        assertTrue(tnGpsFilter.checkPrecise(lastFix));
        lastFix.setTime(time + 100);
        assertFalse(tnGpsFilter.checkPrecise(lastFix));
        lastFix.setTime(time + 200);
        assertFalse(tnGpsFilter.checkPrecise(lastFix));
        lastFix.setTime(time + 300);
        assertFalse(tnGpsFilter.checkPrecise(lastFix));
        //valid, buffer is full, return true.
        assertTrue(tnGpsFilter.checkPrecise(locationToTest));
    }
}

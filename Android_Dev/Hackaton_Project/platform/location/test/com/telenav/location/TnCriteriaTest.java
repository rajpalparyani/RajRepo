/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnCriteriaTest.java
 *
 */
package com.telenav.location;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-7
 */
public class TnCriteriaTest
{
    TnCriteria criteria;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        criteria = new TnCriteria();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#TnCriteria()}.
     */
    @Test
    public void testTnCriteria()
    {
        assertNotNull(criteria);
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setAccuracy(int)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetAccuracyException()
    {
        criteria.setAccuracy(100);
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#getAccuracy()}.
     */
    @Test
    public void testGetAccuracy()
    {
        criteria.setAccuracy(TnCriteria.ACCURACY_COARSE);
        assertEquals(TnCriteria.ACCURACY_COARSE, criteria.getAccuracy());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setHorizontalAccuracy(int)}.
     */
    @Test
    public void testSetHorizontalAccuracy()
    {
        criteria.setHorizontalAccuracy(1000);
        assertEquals(1000, criteria.getHorizontalAccuracy());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#getHorizontalAccuracy()}.
     */
    @Test
    public void testGetHorizontalAccuracy()
    {
        criteria.setHorizontalAccuracy(1000);
        assertEquals(1000, criteria.getHorizontalAccuracy());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setVerticalAccuracy(int)}.
     */
    @Test
    public void testSetVerticalAccuracy()
    {
        criteria.setVerticalAccuracy(999);
        assertEquals(999, criteria.getVerticalAccuracy());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#getVerticalAccuracy()}.
     */
    @Test
    public void testGetVerticalAccuracy()
    {
        criteria.setVerticalAccuracy(999);
        assertEquals(999, criteria.getVerticalAccuracy());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setPowerRequirement(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetPowerRequirementException()
    {
        criteria.setPowerRequirement(-1);
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#getPowerRequirement()}.
     */
    @Test
    public void testGetPowerRequirement()
    {
        criteria.setPowerRequirement(TnCriteria.POWER_HIGH);
        assertEquals(TnCriteria.POWER_HIGH, criteria.getPowerRequirement());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setCostAllowed(boolean)}.
     */
    @Test
    public void testSetCostAllowed()
    {
        criteria.setCostAllowed(true);
        assertTrue(criteria.isCostAllowed());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#isCostAllowed()}.
     */
    @Test
    public void testIsCostAllowed()
    {
        criteria.setCostAllowed(true);
        assertTrue(criteria.isCostAllowed());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setAltitudeRequired(boolean)}.
     */
    @Test
    public void testSetAltitudeRequired()
    {
        criteria.setAltitudeRequired(true);
        assertTrue(criteria.isAltitudeRequired());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#isAltitudeRequired()}.
     */
    @Test
    public void testIsAltitudeRequired()
    {
        criteria.setAltitudeRequired(true);
        assertTrue(criteria.isAltitudeRequired());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setSpeedRequired(boolean)}.
     */
    @Test
    public void testSetSpeedRequired()
    {
        criteria.setSpeedRequired(true);
        assertTrue(criteria.isSpeedRequired());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#isSpeedRequired()}.
     */
    @Test
    public void testIsSpeedRequired()
    {
        criteria.setSpeedRequired(true);
        assertTrue(criteria.isSpeedRequired());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#setBearingRequired(boolean)}.
     */
    @Test
    public void testSetBearingRequired()
    {
        criteria.setBearingRequired(true);
        assertTrue(criteria.isBearingRequired());
    }

    /**
     * Test method for {@link com.telenav.location.TnCriteria#isBearingRequired()}.
     */
    @Test
    public void testIsBearingRequired()
    {
        criteria.setBearingRequired(true);
        assertTrue(criteria.isBearingRequired());
    }

}

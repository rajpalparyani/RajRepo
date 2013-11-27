/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavDataFactoryTest.java
 *
 */
package com.telenav.datatypes.nav;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-6
 */
public class NavDataFactoryTest
{
    @BeforeClass
    public static void init()
    {
        NavDataFactory.init(new NavDataFactory());
    }
    
    @Test
    public void testCreateNavState()
    {
        NavState navState = NavDataFactory.getInstance().createNavState(1001);
        assertNotNull(navState);
        assertEquals(1001, navState.getRoute());
    }
    
    @Test
    public void testCreateNavStateMultiParam()
    {
        int routeId = 1001;
        int segIndex = 3;
        int edgeIndex = 3;
        int shapePointIndex = 2;
        int range = 500;
        NavState navState = NavDataFactory.getInstance().createNavState(routeId, segIndex, edgeIndex, shapePointIndex, range);
        assertNotNull(navState);
        assertEquals(routeId, navState.getRoute());
        assertEquals(segIndex, navState.getSegmentIndex());
        assertEquals(edgeIndex, navState.getEdgeIndex());
        assertEquals(shapePointIndex, navState.getPointIndex());
        assertEquals(range, navState.getRange());
    }
    
    @Test
    public void testGetNavUtil()
    {
        NavUtil util1 = NavDataFactory.getInstance().getNavUtil();
        assertNotNull(util1);
        NavUtil util2 = NavDataFactory.getInstance().getNavUtil();
        assertNotNull(util2);
        assertEquals(util1, util2);
    }
}

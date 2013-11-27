/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * VectorMapEdgeAssert.java
 *
 */
package com.telenav.data.datatypes.map;

import junit.framework.TestCase;

import com.telenav.datatypes.map.VectorMapEdge;

/**
 *@author Leo (lchen@telenavsoftware.com)
 *@date 2011-10-9
 */
public class VectorMapEdgeAssert
{
    public static void assertVectorMapEdge(VectorMapEdge expected, VectorMapEdge actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.isPolygon(), actual.isPolygon());
        TestCase.assertEquals(expected.isIsland(), actual.isIsland());
        TestCase.assertEquals(expected.getStreetName(), actual.getStreetName());
        TestCase.assertEquals(expected.getIconType(), actual.getIconType());
        TestCase.assertEquals(expected.getSpeedLimit(), actual.getSpeedLimit());
    }
}

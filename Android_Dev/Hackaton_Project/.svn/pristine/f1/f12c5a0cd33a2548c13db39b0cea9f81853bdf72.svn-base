/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TileMarkAssert.java
 *
 */
package com.telenav.data.datatypes.map;

import junit.framework.TestCase;

import com.telenav.datatypes.map.TileMark;

/**
 *@author Leo (lchen@telenavsoftware.com)
 *@date 2011-10-9
 */
public class TileMarkAssert
{
    public static void assertTileMark(TileMark expected, TileMark actual)
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
        TestCase.assertEquals(expected.getMarkType(), actual.getMarkType());
        TestCase.assertEquals(expected.getLabelType(), actual.getLabelType());
        TestCase.assertEquals(expected.getName(), actual.getName());
        TestCase.assertEquals(expected.getRelatedPointX(), actual.getRelatedPointX());
        TestCase.assertEquals(expected.getRelatedPointY(), actual.getRelatedPointY());
        
        TestCase.assertEquals(expected.getHeading(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.getStyle(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.getSize(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.getScreenHeading(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.isAttachedToMapTile(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.getPositionLat(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.getPositionLon(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.getScreenX(), actual.getRelatedPointY());
        TestCase.assertEquals(expected.getScreenY(), actual.getRelatedPointY());
    }
}

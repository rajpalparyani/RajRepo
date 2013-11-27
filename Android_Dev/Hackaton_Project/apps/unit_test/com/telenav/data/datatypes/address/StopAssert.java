/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * StopAssert.java
 *
 */
package com.telenav.data.datatypes.address;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-20
 */
public class StopAssert
{
    public static void assertStop(Stop expected, Stop actual)
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
        TestCase.assertEquals(expected.getCity(), actual.getCity());
        TestCase.assertEquals(expected.getCountry(), actual.getCountry());
        TestCase.assertEquals(expected.getCrossStreetName(), actual.getCrossStreetName());
        TestCase.assertEquals(expected.getId(), actual.getId());
        TestCase.assertEquals(expected.isGeocoded(), actual.isGeocoded());
        TestCase.assertEquals(expected.isSharedAddress(), actual.isSharedAddress());
        TestCase.assertEquals(expected.getLabel(), actual.getLabel());
        TestCase.assertEquals(expected.getLat(), actual.getLat());
        TestCase.assertEquals(expected.getLon(), actual.getLon());
        TestCase.assertEquals(expected.getPostalCode(), actual.getPostalCode());
        TestCase.assertEquals(expected.getProvince(), actual.getProvince());
        TestCase.assertEquals(expected.getStatus(), actual.getStatus());
        TestCase.assertEquals(expected.getType(), actual.getType());
        TestCase.assertEquals(expected.getFirstLine(), actual.getFirstLine());
    }
}

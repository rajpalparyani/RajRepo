/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AddressAssert.java
 *
 */
package com.telenav.data.datatypes.address;

import com.telenav.data.datatypes.poi.PoiAssert;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-20
 */
public class AddressAssert
{
    public static void assertAddress(Address expected, Address actual)
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
        TestCase.assertEquals(expected.getId(), actual.getId());
        TestCase.assertEquals(expected.getLabel(), actual.getLabel());
        TestCase.assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        TestCase.assertEquals(expected.getSharedFromPTN(), actual.getSharedFromPTN());
        TestCase.assertEquals(expected.getSelectedIndex(), actual.getSelectedIndex());
        TestCase.assertEquals(expected.getSharedFromUser(), actual.getSharedFromUser());
        TestCase.assertEquals(expected.getType(), actual.getType());
        TestCase.assertEquals(expected.getUpdateTime(), actual.getUpdateTime());
        TestCase.assertEquals(expected.getCatagories(), actual.getCatagories());
        TestCase.assertEquals(expected.isExistedInFavorite(), actual.isExistedInFavorite());
        TestCase.assertEquals(expected.isCursorAddress(), actual.isCursorAddress());
        TestCase.assertEquals(expected.isValid(), actual.isValid());
        StopAssert.assertStop(expected.getStop(), actual.getStop());
        PoiAssert.assertPoi(expected.getPoi(), actual.getPoi());
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SentAddressAssert.java
 *
 */
package com.telenav.data.datatypes.address;

import junit.framework.TestCase;

/**
 *@author Alberta
 *@date 2011-10-8
 */
public class SentAddressAssert
{

    public static void assertSendAddress(SentAddress expected, SentAddress actual)
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
        TestCase.assertEquals(expected.getCreatedTime(), actual.getCreatedTime());
        TestCase.assertEquals(expected.getLabel(), actual.getLabel());
        TestCase.assertEquals(expected.getState(), actual.getState());
        TestCase.assertEquals(expected.getStreet(), actual.getStreet());
        TestCase.assertEquals(expected.getZip(), actual.getZip());
        AddressRecevierAssert.assertAddressRecevier(expected.getReceiver(), actual.getReceiver());
    }
}

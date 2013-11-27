/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AddressRecevierAssert.java
 *
 */
package com.telenav.data.datatypes.address;

import junit.framework.TestCase;

/**
 *@author Alberta (dzhao@telenavsoftware.com)
 *@date 2011-10-8
 */
public class AddressRecevierAssert
{
    public static void assertAddressRecevier(AddressRecevier expected, AddressRecevier actual)
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
        TestCase.assertEquals(expected.getReceivers(), actual.getReceivers());
    }
}

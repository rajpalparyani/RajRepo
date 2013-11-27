/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * StringMapAsserter.java
 *
 */
package com.telenav.data.datatypes.primitive;

import java.util.Enumeration;

import junit.framework.TestCase;

/**
 * @author bmyang
 * @date 2011-6-29
 */
public class StringMapAssert
{

	public static void assertStringMap(StringMap expected, StringMap actual)
	{
		if (null == expected)
		{
			TestCase.assertNull(actual);
			return ;
		}
		TestCase.assertNotNull(actual);
		StringMapAssert.assertChildMap(expected, actual);
		StringMapAssert.assertChildMap(actual, expected);
	}

	private static void assertChildMap(StringMap expected, StringMap actual) 
	{
		Enumeration keys = expected.keys();
		while (keys.hasMoreElements())
		{
			String key = (String) keys.nextElement();
			TestCase.assertEquals(expected.get(key), actual.get(key));
		}
	}

}

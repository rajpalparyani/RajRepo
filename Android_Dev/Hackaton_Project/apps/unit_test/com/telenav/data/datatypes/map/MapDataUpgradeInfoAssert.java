/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapDataUpgradeInfoAssert.java
 *
 */
package com.telenav.data.datatypes.map;

import junit.framework.TestCase;

/**
 * @author bmyang
 * @date 2011-7-5
 */
public class MapDataUpgradeInfoAssert
{

	public static void assertMapDataUpgradeInfo(MapDataUpgradeInfo expected, MapDataUpgradeInfo actual)
	{
	    if( expected == null )
	    {
	    	TestCase.assertNull(actual);
	        return;
	    }
	    TestCase.assertNotNull(expected);
	    TestCase.assertNotNull(actual);
	    TestCase.assertEquals(expected.getUpgradeMode(), actual.getUpgradeMode());
	    TestCase.assertEquals(expected.getName(), actual.getName());
	    TestCase.assertEquals(expected.getVersion(), actual.getVersion());
	    TestCase.assertEquals(expected.getBuildNumber(), actual.getBuildNumber());
	    TestCase.assertEquals(expected.getRegion(), actual.getRegion());
	    TestCase.assertEquals(expected.getState(), actual.getState());
	    TestCase.assertEquals(expected.getUrl(), actual.getUrl());
	    TestCase.assertEquals(expected.getSummary(), actual.getSummary());
	    TestCase.assertEquals(expected.getMapDataSize(), actual.getMapDataSize());
	}

}

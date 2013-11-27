/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TxNodePreferenceSerializableTest.java
 *
 */
package com.telenav.data.serializable.txnode;

import org.junit.Assert;

import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.preference.PreferenceGroup;

import junit.framework.TestCase;

/**
 *@author bmyang
 *@date 2011-6-22
 */
public class TxNodePreferenceSerializableTest extends TestCase 
{
	TxNodePreferenceSerializable preferenceSerializable;
	byte[] profileGroupData = {48, -99, -101, 58, 5, 12, 2, 50, 52, 48, -31, 1, 2, 14, 80, 114, 111, 102, 105, 108, 101, 0, 0};
	byte[] routeSettingData = {48, -99, -101, 58, 4, 10, -99, 1, 0, 0, 2, 8, 22, 76, 97, 110, 101, 32, 65, 115, 115, 105, 115, 116, 8, 83, 104, 111, 119, 8, 83, 104, 111, 119, 8, 72, 105, 100, 101, 0, 0};
	PreferenceGroup profileGroup;
	Preference routeSetting;
	protected void setUp() throws Exception 
	{
		preferenceSerializable = new TxNodePreferenceSerializable();
		//PreferenceGroup
		profileGroup = new PreferenceGroup(PreferenceGroup.ID_PREFERENCE_GROUP_GENERAL, "Profile");
		int[] preferIds = {Preference.ID_PREFERENCE_TYPE_FIRSTNAME, Preference.ID_PREFERENCE_TYPE_LASTNAME, Preference.ID_PREFERENCE_TYPE_EMAIL, Preference.ID_PREFERENCE_TYPE_USERNAME};
		profileGroup.setPreferenceIds(preferIds);
		//Preference
		routeSetting = new Preference(Preference.ID_PREFERENCE_LANE_ASSIST, "Lane Assist");
		routeSetting.setIntValue(0);
		routeSetting.setStrValue("Show");
		int[] optionValues = {Preference.LANE_ASSIST_ON, Preference.LANE_ASSIST_OFF};
        String[] optionNames = {"Show", "Hide"};
        routeSetting.setOptionValues(optionValues);
        routeSetting.setOptionNames(optionNames);
		super.setUp();
	}

	public void testCreatePreference() 
	{
		assertPreference(routeSetting, preferenceSerializable.createPreference(routeSettingData));
	}
	
	public void testCreatePreferenceNull() 
	{
		assertNull(preferenceSerializable.createPreference(null));
	}

	public void testCreatePreferenceGroup() 
	{
		assertPreferenceGroup(profileGroup, preferenceSerializable.createPreferenceGroup(profileGroupData));
	}
	
	public void testCreatePreferenceGroupNull() 
	{
		assertNull(preferenceSerializable.createPreferenceGroup(null));
	}

	public void testToBytesPreferenceGroup() 
	{
		Assert.assertArrayEquals(profileGroupData, preferenceSerializable.toBytes(profileGroup));
	}
	
	public void testToBytesPreferenceGroupNull() 
	{
		assertNull(preferenceSerializable.toBytes((PreferenceGroup)null));
	}

	public void testToBytesPreference() 
	{
		Assert.assertArrayEquals(routeSettingData, preferenceSerializable.toBytes(routeSetting));
	}
	
	public void testToBytesPreferenceNull() 
	{
		
		assertNull(preferenceSerializable.toBytes((Preference)null));
	}

	private void assertPreference(Preference expected, Preference actual)
	{
		if (null == expected)
		{
			assertNull(actual);
			return ;
		}
		assertNotNull(actual);
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getIntValue(), actual.getIntValue());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getStrValue(), actual.getStrValue());
		Assert.assertArrayEquals(expected.getOptionNames(), actual.getOptionNames());
		Assert.assertArrayEquals(expected.getOptionValues(), actual.getOptionValues());
	}
	
	private void assertPreferenceGroup(PreferenceGroup expected, PreferenceGroup actual)
	{
		if (null == expected)
		{
			assertNull(actual);
			return ;
		}
		assertNotNull(actual);
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
		Assert.assertArrayEquals(expected.getPreferenceIds(), actual.getPreferenceIds());
	}
}

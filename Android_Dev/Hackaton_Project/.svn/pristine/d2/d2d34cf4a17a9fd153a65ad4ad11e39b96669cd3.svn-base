/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TxNodeMandatorySerializableTest.java
 *
 */
package com.telenav.data.serializable.txnode;


import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.module.AppConfigHelper;

/**
 * @author htzheng
 * @date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class TxNodeMandatorySerializableTest
{

	private TxNodeMandatorySerializable txNodemandatorySerializable;
	byte[] data = {48, -99, -101, 58, 0, 0, 0, 6, 69, 2, 1, 4, 89, 1, 20, 84, 37, 56, 50, 99, 54, 37, 57, 68, 37, 53, 66, 37, 65, 57, 37, 56, 54, 89, 37, 50, 50, 37, 65, 70, 37, 57, 48, 37, 68, 67, 37, 50, 49, 37, 69, 66, 37, 57, 70, 37, 50, 49, 8, 49, 49, 49, 49, 44, 37, 55, 66, 37, 50, 51, 37, 51, 67, 37, 55, 68, 37, 49, 50, 37, 51, 69, 70, 37, 55, 68, 8, 55, 56, 56, 53, 10, 101, 110, 95, 85, 83, 4, 78, 65, 88, 65, 65, 65, 65, 65, 65, 67, 80, 121, 107, 73, 65, 65, 65, 69, 120, 72, 70, 116, 120, 48, 97, 52, 69, 118, 122, 107, 70, 74, 56, 99, 76, 84, 78, 49, 70, 43, 43, 104, 103, 69, 89, 48, 61, 6, 50, 48, 51, 2, 48, 2, -1, 0, 0, 124, 1, 4, 73, 1, 16, 20, 65, 84, 84, 78, 65, 86, 80, 82, 79, 71, 14, 65, 78, 68, 82, 79, 73, 68, 12, 55, 46, 48, 46, 48, 49, 18, 83, 71, 72, 45, 84, 57, 53, 57, 86, 8, 57, 57, 57, 57, 8, 65, 71, 80, 83, 14, 65, 84, 84, 95, 78, 65, 86, 0, 0, 0, 38, 1, 4, 77, 1, 6, 10, 109, 112, 51, 104, 105, 6, 80, 78, 71, 2, 51, 0, 0, 0};
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	    AppConfigHelper.isLoggerEnable = false;
		txNodemandatorySerializable = new TxNodeMandatorySerializable();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		txNodemandatorySerializable = null;
	}

	@Test
	public void testCreateMandatoryProfile_NullData()
	{
		assertNull(txNodemandatorySerializable.createMandatoryProfile(null));
	}
	
	@Test
	public void testCreateMandatoryProfile_HasData()
	{
//		MandatoryProfile profile = txNodemandatorySerializable.createMandatoryProfile(data);
//		Assert.assertArrayEquals(data, txNodemandatorySerializable.toBytes(profile));
	}
	
	@Test
	public void testToBytes_NullData()
	{
		assertNull(txNodemandatorySerializable.toBytes(null));
	}
	
	@Test
	public void testToBytes_HasData()
	{
		//create one profile
		MandatoryProfile expectProfile = initMandatoryProfile();
		byte[] bytes = txNodemandatorySerializable.toBytes(expectProfile);
		
		MandatoryProfile actualProfile = txNodemandatorySerializable.createMandatoryProfile(bytes);
		Assert.assertTrue(equalWithMandatoryProfile(expectProfile, actualProfile));
		
	}
	
	private MandatoryProfile initMandatoryProfile()
	{
		MandatoryProfile profile = new MandatoryProfile();
		profile.getUserInfo().phoneNumber = "5555218135";
		profile.getUserInfo().pin = "8135";
		profile.getUserInfo().userId = "tn7.0.1";
		profile.getUserInfo().eqpin = "12345";
		profile.getUserInfo().locale = "en_US";
		profile.getUserInfo().region = "NA";
		profile.getUserInfo().ssoToken = "12345";
		profile.getUserInfo().guideTone = "123";
		profile.getUserInfo().ptnType = "0";
		
		profile.getClientInfo().programCode = "ATTNAVPROG";
		profile.getClientInfo().platform = "Android";
		profile.getClientInfo().version = "7.0.1";
		profile.getClientInfo().device = "";
		profile.getClientInfo().buildNumber = "7011123";
		profile.getClientInfo().gpsTpye = "";
		profile.getClientInfo().productType = "ATT_NAV";
		profile.getClientInfo().deviceCarrier = "cingular";
		
		profile.getUserPrefers().audioFormat = "mp3hi";
		profile.getUserPrefers().imageType = "png";
		profile.getUserPrefers().audioLevel = "3";
        
		return profile;
	}
	
	private boolean equalWithMandatoryProfile(MandatoryProfile expected, MandatoryProfile actuals)
	{
		if (expected == null && actuals == null)
		{
			return true;
		}
		else if (expected == null && actuals != null)
		{
			return false;
		}
		else if (expected != null && actuals == null)
		{
			return false;
		}
		
		if (!actuals.getUserInfo().phoneNumber.equalsIgnoreCase(expected.getUserInfo().phoneNumber))
		{
			return false;
		}
		
		if (!actuals.getUserInfo().pin.equalsIgnoreCase(expected.getUserInfo().pin))
		{
			return false;
		}
		if (!actuals.getUserInfo().userId.equalsIgnoreCase(expected.getUserInfo().userId))
		{
			return false;
		}
		
		if (!actuals.getUserInfo().eqpin.equalsIgnoreCase(expected.getUserInfo().eqpin))
		{
			return false;
		}
		if (!actuals.getUserInfo().locale.equalsIgnoreCase(expected.getUserInfo().locale))
		{
			return false;
		}
		if (!actuals.getUserInfo().region.equalsIgnoreCase(expected.getUserInfo().region))
		{
			return false;
		}
		if (!actuals.getUserInfo().ssoToken.equalsIgnoreCase(expected.getUserInfo().ssoToken))
		{
			return false;
		}
		if (!actuals.getUserInfo().guideTone.equalsIgnoreCase(expected.getUserInfo().guideTone))
		{
			return false;
		}
		if (!actuals.getUserInfo().ptnType.equalsIgnoreCase(expected.getUserInfo().ptnType))
		{
			return false;
		}
		
		if (!actuals.getClientInfo().programCode.equalsIgnoreCase(expected.getClientInfo().programCode))
		{
			return false;
		}
		if (!actuals.getClientInfo().platform.equalsIgnoreCase(expected.getClientInfo().platform))
		{
			return false;
		}
		if (!actuals.getClientInfo().version.equalsIgnoreCase(expected.getClientInfo().version))
		{
			return false;
		}
		if (!actuals.getClientInfo().device.equalsIgnoreCase(expected.getClientInfo().device))
		{
			return false;
		}
		if (!actuals.getClientInfo().buildNumber.equalsIgnoreCase(expected.getClientInfo().buildNumber))
		{
			return false;
		}
		if (!actuals.getClientInfo().gpsTpye.equalsIgnoreCase(expected.getClientInfo().gpsTpye))
		{
			return false;
		}
		if (!actuals.getClientInfo().productType.equalsIgnoreCase(expected.getClientInfo().productType))
		{
			return false;
		}
		if (!actuals.getClientInfo().deviceCarrier.equalsIgnoreCase(expected.getClientInfo().deviceCarrier))
		{
			return false;
		}
		
		if (!actuals.getUserPrefers().audioFormat.equalsIgnoreCase(expected.getUserPrefers().audioFormat))
		{
			return false;
		}
		if (!actuals.getUserPrefers().imageType.equalsIgnoreCase(expected.getUserPrefers().imageType))
		{
			return false;
		}
		if (!actuals.getUserPrefers().audioLevel.equalsIgnoreCase(expected.getUserPrefers().audioLevel))
		{
			return false;
		}

		return true;
	}
}

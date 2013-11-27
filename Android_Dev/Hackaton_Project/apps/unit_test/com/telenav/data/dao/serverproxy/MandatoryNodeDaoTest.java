/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MandatoryNodeDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.module.AppConfigHelper;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 * @author htzheng
 * @date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnPersistentManager.class})
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class MandatoryNodeDaoTest
{
	private TnStore store;
	private MandatoryNodeDao manadatoryNodeDao;
	private byte[] mandatoryNodeData = {48, -99, -101, 58, 0, 0, 0, 6, 61, 2, 1, 4, 89, 1, 18, 84, 37, 56, 50, 99, 54, 37, 57, 68, 37, 53, 66, 37, 65, 57, 37, 56, 54, 89, 37, 50, 50, 37, 65, 70, 37, 57, 48, 37, 68, 67, 37, 50, 49, 37, 69, 66, 37, 57, 70, 37, 50, 49, 8, 49, 49, 49, 49, 44, 37, 55, 66, 37, 50, 51, 37, 51, 67, 37, 55, 68, 37, 49, 50, 37, 51, 69, 70, 37, 55, 68, 8, 55, 56, 56, 53, 10, 101, 110, 95, 85, 83, 4, 78, 65, 88, 65, 65, 65, 65, 65, 65, 67, 80, 121, 107, 73, 65, 65, 65, 69, 120, 65, 113, 49, 89, 50, 73, 81, 86, 113, 77, 118, 86, 97, 49, 109, 49, 104, 102, 87, 115, 51, 49, 110, 73, 115, 118, 65, 61, 6, 50, 48, 51, 2, 48, 0, 0, 124, 1, 4, 73, 1, 16, 20, 65, 84, 84, 78, 65, 86, 80, 82, 79, 71, 14, 65, 78, 68, 82, 79, 73, 68, 12, 55, 46, 48, 46, 48, 49, 18, 83, 71, 72, 45, 84, 57, 53, 57, 86, 8, 57, 57, 57, 57, 8, 65, 71, 80, 83, 14, 65, 84, 84, 95, 78, 65, 86, 0, 0, 0, 38, 1, 4, 77, 1, 6, 10, 109, 112, 51, 104, 105, 6, 80, 78, 71, 2, 51, 0, 0, 0};
	private MandatoryProfile mandatoryProfile;
	
	@Before
	public void setUp()
	{
		TnPersistentManager manager = PowerMock.createMock(TnPersistentManager.class);
    	PowerMock.mockStatic(TnPersistentManager.class);
    	EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager);
    	store = new TnStoreMock();
    	store.load();
    	EasyMock.expect(manager.createStore(TnPersistentManager.DATABASE, "mandatory_node")).andReturn(store);
    	
    	PowerMock.replayAll();
    	
    	manadatoryNodeDao = MandatoryNodeDao.getInstance();
    	manadatoryNodeDao.setStore(store);
	    AppConfigHelper.isLoggerEnable = false;
		
	}
	
	@After
	public void tearDown()
	{
		store.clear();
		manadatoryNodeDao.clear();
	}
	
	@Test
	public void testLoad_EmptyStore()
	{
		Assert.assertNotNull(manadatoryNodeDao.getMandatoryNode());
		
		//expect an empty MandatoryProfile
		assertMandatoryProfileEmpty(manadatoryNodeDao.getMandatoryNode());
	}
	
	@Test
	public void testLoad_SerializableNode()
	{
		SerializableManager.init(new TxNodeSerializableManager());
		mandatoryProfile =  SerializableManager.getInstance().getMandatorySerializable().createMandatoryProfile(mandatoryNodeData);
		store.put(1000, mandatoryNodeData);
		
		Assert.assertTrue(manadatoryNodeDao.getMandatoryNode() != null
				&& manadatoryNodeDao.getMandatoryNode().getUserInfo() != null
				&& manadatoryNodeDao.getMandatoryNode().getClientInfo() != null
				&& manadatoryNodeDao.getMandatoryNode().getUserPrefers() != null);
		
		assertEquals(mandatoryProfile, manadatoryNodeDao.getMandatoryNode());
	}
	
	@Test
	public void testStore_EmptyData()
	{
		manadatoryNodeDao.store(); //store one empty MandatoryProfile
		
		assertMandatoryProfileEmpty(manadatoryNodeDao.getMandatoryNode());//it should be empty.
	}
	
	@Test
	public void testStore_MandatoryNodeStore()
	{
		SerializableManager.init(new TxNodeSerializableManager());
		mandatoryProfile =  SerializableManager.getInstance().getMandatorySerializable().createMandatoryProfile(mandatoryNodeData);

		copyProfile(mandatoryProfile, manadatoryNodeDao.getMandatoryNode());
		
		manadatoryNodeDao.store(); //store some data
		assertEquals(mandatoryProfile, manadatoryNodeDao.getMandatoryNode());//it should be equal with the origin data.
	}
	
	@Test
	public void testClear()
	{
		SerializableManager.init(new TxNodeSerializableManager());
		mandatoryProfile =  SerializableManager.getInstance().getMandatorySerializable().createMandatoryProfile(mandatoryNodeData);

//		copyProfile(mandatoryProfile, manadatoryNodeDao.getMandatoryNode());
		manadatoryNodeDao.store(); //store some data
		Assert.assertTrue(store.get(1000) != null);//it should be null
		
		manadatoryNodeDao.clear();//clear the data.
		
		Assert.assertTrue(store.get(1000) == null);//it should be null after clear.
		
	}

	private void assertMandatoryProfileEmpty(MandatoryProfile profile)
	{
		Assert.assertNotNull(profile.getUserInfo());
		Assert.assertNotNull(profile.getClientInfo());
		Assert.assertNotNull(profile.getUserPrefers());
		
		MandatoryProfile.UserInfo userInfo = profile.getUserInfo();
		MandatoryProfile.ClientInfo clientInfo = profile.getClientInfo();
		MandatoryProfile.UserPrefers userPrefers = profile.getUserPrefers();
		
		Assert.assertTrue(
				"".equals(userInfo.phoneNumber)
				&& "".equals(userInfo.pin)
				&& "".equals(userInfo.userId)
				&& "".equals(userInfo.eqpin)
				&& "".equals(userInfo.locale)
				&& "".equals(userInfo.region)
				&& "".equals(userInfo.ssoToken)
				&& "".equals(userInfo.guideTone)
				&& "".equals(userInfo.ptnType));
		
		Assert.assertTrue("".equals(clientInfo.buildNumber)
				&& "".equals(clientInfo.device)
				&& "".equals(clientInfo.deviceCarrier)
				&& "".equals(clientInfo.gpsTpye)
				&& "".equals(clientInfo.platform)
				&& "".equals(clientInfo.productType)
				&& "".equals(clientInfo.programCode)
				&& "".equals(clientInfo.version));
		
		Assert.assertTrue("".equals(userPrefers.audioFormat)
				&& "3".equals(userPrefers.audioLevel)
				&& "".equals(userPrefers.imageType));
        		
	}
	
	private void assertEquals(MandatoryProfile expected, MandatoryProfile actuals)
	{
		MandatoryProfile.UserInfo actualsUserInfo = actuals.getUserInfo();
		MandatoryProfile.ClientInfo actualsClientInfo = actuals.getClientInfo();
		MandatoryProfile.UserPrefers actualsUserPrefers = actuals.getUserPrefers();
		
		MandatoryProfile.UserInfo expectedUserInfo = actuals.getUserInfo();
		MandatoryProfile.ClientInfo expectedClientInfo = actuals.getClientInfo();
		MandatoryProfile.UserPrefers expectedUserPrefers = actuals.getUserPrefers();
		
		Assert.assertTrue(actualsUserInfo.eqpin.equals(expectedUserInfo.eqpin)
				&& actualsUserInfo.guideTone.equals(expectedUserInfo.guideTone)
				&& actualsUserInfo.phoneNumber.equals(expectedUserInfo.phoneNumber)
				&& actualsUserInfo.locale.equals(expectedUserInfo.locale)
				&& actualsUserInfo.pin.equals(expectedUserInfo.pin)
				&& actualsUserInfo.ptnType.equals(expectedUserInfo.ptnType)
				&& actualsUserInfo.region.equals(expectedUserInfo.region)
				&& actualsUserInfo.ssoToken.equals(expectedUserInfo.ssoToken)
				&& actualsUserInfo.userId.equals(expectedUserInfo.userId));
		
		Assert.assertTrue(actualsClientInfo.buildNumber.equals(expectedClientInfo.buildNumber)
				&& actualsClientInfo.device.equals(expectedClientInfo.device)
				&& actualsClientInfo.deviceCarrier.equals(expectedClientInfo.deviceCarrier)
				&& actualsClientInfo.gpsTpye.equals(expectedClientInfo.gpsTpye)
				&& actualsClientInfo.platform.equals(expectedClientInfo.platform)
				&& actualsClientInfo.productType.equals(expectedClientInfo.productType)
				&& actualsClientInfo.programCode.equals(expectedClientInfo.programCode)
				&& actualsClientInfo.version.equals(expectedClientInfo.version));
		
		Assert.assertTrue(actualsUserPrefers.audioFormat.equals(expectedUserPrefers.audioFormat)
				&& actualsUserPrefers.audioLevel.equals(expectedUserPrefers.audioLevel)
				&& actualsUserPrefers.imageType.equals(expectedUserPrefers.imageType));
	}
	
	private void copyProfile(MandatoryProfile src, MandatoryProfile dest)
	{
		dest.getClientInfo().buildNumber = src.getClientInfo().buildNumber;
		dest.getClientInfo().device = src.getClientInfo().device;
		dest.getClientInfo().deviceCarrier = src.getClientInfo().deviceCarrier;
		dest.getClientInfo().gpsTpye = src.getClientInfo().gpsTpye;
		dest.getClientInfo().platform = src.getClientInfo().platform;
		dest.getClientInfo().productType = src.getClientInfo().productType;
		dest.getClientInfo().programCode = src.getClientInfo().programCode;
		dest.getClientInfo().version = src.getClientInfo().version;
		
		dest.getUserInfo().eqpin = src.getUserInfo().eqpin;
		dest.getUserInfo().guideTone = src.getUserInfo().guideTone;
		dest.getUserInfo().locale = src.getUserInfo().locale;
		dest.getUserInfo().phoneNumber = src.getUserInfo().phoneNumber;
		dest.getUserInfo().pin = src.getUserInfo().pin;
		dest.getUserInfo().ptnType = src.getUserInfo().ptnType;
		dest.getUserInfo().region = src.getUserInfo().region;
		dest.getUserInfo().ssoToken = src.getUserInfo().ssoToken;
		dest.getUserInfo().userId = src.getUserInfo().userId;
		
		dest.getUserPrefers().audioFormat = src.getUserPrefers().audioFormat;
		dest.getUserPrefers().audioLevel = src.getUserPrefers().audioLevel;
		dest.getUserPrefers().imageType = src.getUserPrefers().imageType;
	}
}

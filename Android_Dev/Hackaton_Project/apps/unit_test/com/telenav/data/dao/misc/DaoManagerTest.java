package com.telenav.data.dao.misc;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cache.AbstractCache;
import com.telenav.io.TnIoManager;
import com.telenav.module.AppConfigHelper;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStoreMock;

/**
 *@author gbwang
 *@date 2011-6-23
 */

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
@PrepareForTest({TnPersistentManager.class, AbstractCache.class, DaoManager.class, TnIoManager.class, FileStoreManager.class})

public class DaoManagerTest extends TestCase
{
	TnStoreMock mockStore; 
	DaoManager daoManager;
	TnPersistentManager tnPersistentManager;
	
	protected void setUp() throws Exception
    {
	    AppConfigHelper.isLoggerEnable = false;
    	mockStore = new TnStoreMock(); 
    	mockStore.load();
    	daoManager = new DaoManager();    	
    	PowerMock.mockStatic(TnPersistentManager.class);    
    	tnPersistentManager = PowerMock.createMock(TnPersistentManager.class);
    	EasyMock.expect(TnPersistentManager.getInstance()).andReturn(tnPersistentManager).anyTimes();
        super.setUp();
    }
	
	public void testGetMandatoryNodeDao()
	{
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.DATABASE, "mandatory_node")).andReturn(mockStore).anyTimes();
		PowerMock.replayAll();
		
		daoManager.getMandatoryNodeDao();	
		PowerMock.verifyAll();
	}
	
	public void testGetAddressDao()
	{
		daoManager.addressDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "address")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getAddressDao();		
		Assert.assertNotNull(daoManager.addressDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetExpressAddressDao()
	{
		daoManager.expressAddressDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CHUNK, "express_address")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getExpressAddressDao();		
		Assert.assertNotNull(daoManager.expressAddressDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetBillingAccountDao()
	{				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.DATABASE, "billing_account")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getBillingAccountDao();		
		
		PowerMock.verifyAll();
	}
	
	public void testGetMisLogDao()
	{
		daoManager.misLogDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "mislog")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getMisLogDao();		
		Assert.assertNotNull(daoManager.misLogDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetNearCitiesDao()
	{
		daoManager.nearCitiesDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "near_cities")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getNearCitiesDao();		
		Assert.assertNotNull(daoManager.nearCitiesDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetLocationCacheDao()
	{
		daoManager.locationCacheDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "location_cache")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getLocationCacheDao();		
		Assert.assertNotNull(daoManager.locationCacheDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetResourceBarDao() throws Exception
	{
		daoManager.resourceBarDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CHUNK, "persistent_audio")).andReturn(mockStore).anyTimes();
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "bar_version")).andReturn(mockStore).anyTimes();
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "cserver_node")).andReturn(mockStore).anyTimes();
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "backup_preference_static_data")).andReturn(mockStore).anyTimes();
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.DATABASE, "audio_rule")).andReturn(mockStore).anyTimes();
		
		PowerMock.mockStatic(AbstractCache.class);
		AbstractCache abstractCache = PowerMock.createMock(AbstractCache.class);
		PowerMock.expectNew(AbstractCache.class).andReturn(abstractCache).anyTimes();
		
		PowerMock.mockStatic(TnIoManager.class);
		TnIoManager tnIoManager = PowerMock.createMock(TnIoManager.class);
		EasyMock.expect(TnIoManager.getInstance()).andReturn(tnIoManager).anyTimes();
		
		PowerMock.replayAll();
		
		daoManager.getResourceBarDao();		
		Assert.assertNotNull(daoManager.resourceBarDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetServerDrivenParamsDao()
	{
		daoManager.serverDrivenParamsDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "server_driven")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getServerDrivenParamsDao();		
		Assert.assertNotNull(daoManager.serverDrivenParamsDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetDsrServerDrivenParamsDao()
	{
		daoManager.dsrServerDrivenParamsDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "dsr_dsp")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getDsrServerDrivenParamsDao();		
		Assert.assertNotNull(daoManager.dsrServerDrivenParamsDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetServiceLocatorDao()
	{
		daoManager.serverMappingDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "server_mapping")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getServiceLocatorDao();		
		Assert.assertNotNull(daoManager.serverMappingDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetStartupDao()
	{				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.DATABASE, "startup_data")).andReturn(mockStore);
		PowerMock.replayAll();
		
		daoManager.getStartupDao();	
		PowerMock.verifyAll();
	}
	
	public void testGetSimpleConfigDao()
	{
		daoManager.simpleConfigDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "simple_config")).andReturn(mockStore);
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "carrier_mapping")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getSimpleConfigDao();		
		Assert.assertNotNull(daoManager.simpleConfigDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetSecretSettingDao()
	{
		daoManager.secretSettingDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "secret_setting")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getSecretSettingDao();		
		Assert.assertNotNull(daoManager.secretSettingDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetCommHostDao()
	{
		daoManager.commHostDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "comm_host")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getCommHostDao();		
		Assert.assertNotNull(daoManager.commHostDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetPreferenceDao()
	{
		daoManager.prefDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.DATABASE, "preference_data")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getPreferenceDao();		
		Assert.assertNotNull(daoManager.prefDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetAppStoreDao()
	{
		daoManager.appStoreDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CHUNK, "appstore_resource")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getAppStoreDao();		
		Assert.assertNotNull(daoManager.appStoreDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetRuleManager() throws Exception
	{
		daoManager.ruleManager = null;		
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.DATABASE, "audio_rule")).andReturn(mockStore).anyTimes();
		PowerMock.mockStatic(AbstractCache.class);
		AbstractCache abstractCache = PowerMock.createMock(AbstractCache.class);
		PowerMock.expectNew(AbstractCache.class).andReturn(abstractCache).anyTimes();		
		PowerMock.replayAll();
		
		daoManager.getRuleManager();		
		Assert.assertNotNull(daoManager.ruleManager);
		
		PowerMock.verifyAll();
	}

	public void testGetBrowserSdkServiceDao()
	{
		daoManager.browserSdkServiceDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "browserSdkService_data")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getBrowserSdkServiceDao();		
		Assert.assertNotNull(daoManager.browserSdkServiceDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetTripsDao()
	{
		daoManager.tripsDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "trips")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getTripsDao();		
		Assert.assertNotNull(daoManager.tripsDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetUpsellDao()
	{
		daoManager.upsellDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.DATABASE, "upsell")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getUpsellDao();		
		Assert.assertNotNull(daoManager.upsellDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetResourceBackupDao()
	{
		daoManager.resourceBackupDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CHUNK, "persistent_audio_backup")).andReturn(mockStore);
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "audio_rule_backup")).andReturn(mockStore);
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "bar_version_backup")).andReturn(mockStore);
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "cserver_node_backup")).andReturn(mockStore);
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "backup_preference_static_data_backup")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getResourceBackupDao();		
		Assert.assertNotNull(daoManager.resourceBackupDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetApacheResouceBackupDao()
	{
		daoManager.apacheResouceDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CHUNK, "apache_I18n_resource")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getApacheResouceBackupDao();		
		Assert.assertNotNull(daoManager.apacheResouceDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetApacheServerIndexDao()
	{
		daoManager.apacheIndexDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "apache_I18n_index")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getApacheServerIndexDao();		
		Assert.assertNotNull(daoManager.apacheIndexDao);
		
		PowerMock.verifyAll();
	}
	
	public void testGetApacheIndexBackupDao()
	{
		daoManager.apacheIndexBackupDao = null;				
		EasyMock.expect(tnPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, "apache_I18n_index_backup")).andReturn(mockStore);
		
		PowerMock.replayAll();
		
		daoManager.getApacheIndexBackupDao();		
		Assert.assertNotNull(daoManager.apacheIndexBackupDao);
		
		PowerMock.verifyAll();
	}
	
	public void testClearUserPersonalData()
	{
	    daoManager.clearUserPersonalData();
		
		Assert.assertNull(daoManager.addressDao);
		Assert.assertNull(daoManager.expressAddressDao);
		Assert.assertNull(daoManager.appStoreDao);
		Assert.assertNull(daoManager.resourceBarDao);
		Assert.assertNull(daoManager.prefDao);
		Assert.assertNull(daoManager.upsellDao);
		Assert.assertNull(daoManager.simpleConfigDao);
	}

	public void testClearInternalRMS() throws Exception
	{
		PowerMock.mockStatic(FileStoreManager.class);
		FileStoreManager fileStoreManager = PowerMock.createMock(FileStoreManager.class);
		PowerMock.expectNew(FileStoreManager.class).andReturn(fileStoreManager).anyTimes();	
		fileStoreManager.clearFileSystem(TnPersistentManager.FILE_SYSTEM_INTERNAL);
		
		PowerMock.replayAll();
		
		daoManager.clearInternalRMS();
		
		Assert.assertNull(daoManager.addressDao);
		Assert.assertNull(daoManager.expressAddressDao);
		Assert.assertNull(daoManager.appStoreDao);
		Assert.assertNull(daoManager.browserSdkServiceDao);
		Assert.assertNull(daoManager.commHostDao);
		Assert.assertNull(daoManager.locationCacheDao);	
		Assert.assertNull(daoManager.misLogDao);
		Assert.assertNull(daoManager.nearCitiesDao);
		Assert.assertNull(daoManager.prefDao);		
		Assert.assertNull(daoManager.resourceBarDao);
		Assert.assertNull(daoManager.serverDrivenParamsDao);
		Assert.assertNull(daoManager.dsrServerDrivenParamsDao);
		Assert.assertNull(daoManager.simpleConfigDao);
		Assert.assertNull(daoManager.secretSettingDao);
		Assert.assertNull(daoManager.tripsDao);		
		Assert.assertNull(daoManager.upsellDao);
		
		PowerMock.verifyAll();
	}
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TeleNavDelegateTest.java
 *
 */
package com.telenav.app;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.StartupDao;
import com.telenav.module.AppConfigHelper;


/**
 *@author hchai
 *@date 2011-6-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TeleNavDelegate.class, DaoManager.class, FileStoreManager.class})
@SuppressStaticInitializationFor({"com.telenav.module.AppConfigHelper", "com.telenav.data.dao.misc.DaoManager"})
public class TeleNavDelegateTest extends TestCase
{
    private TeleNavDelegate teleNavDelegate;
    DaoManager daoManager;
    
    @Override
    protected void setUp() throws Exception
    {
        teleNavDelegate = TeleNavDelegate.getInstance();
        
        PowerMock.mockStatic(AbstractDaoManager.class);
        daoManager = PowerMock.createMock(DaoManager.class);
        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(daoManager).anyTimes();
        
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
 
    public void testGetDeviceLocaleForEn()
    {
        verifyGetDeviceLocale("en_US,es_MX,pt_PT", "en", "en_US");
    }
    
    public void testGetDeviceLocaleForEs()
    {
        verifyGetDeviceLocale("en_US,es_MX,pt_PT", "es", "es_MX");
    }
    
    public void testGetDeviceLocaleForPt()
    {
        verifyGetDeviceLocale("en_US,es_MX,pt_PT", "pt", "pt_PT");
    }
    
    public void testGetDeviceLocaleForPt2()
    {
        verifyGetDeviceLocale("en_US,es_MX,pt_PT,", "pt", "pt_PT");
    }
    
    public void testGetDeviceLocaleForNoMatch1()
    {
        verifyGetDeviceLocale("en_US,es_MX,pt_PT", "cn", "");
    }
    
    public void testGetDeviceLocaleForNoMatch2()
    {
        verifyGetDeviceLocale("", "en", "");
    }
    
    public void testGetDeviceLocaleForNoMatch3()
    {
        verifyGetDeviceLocale("", "", "");
    }
    
    public void testGetDeviceLocaleForNoMatch4()
    {
        verifyGetDeviceLocale("en_US,es_MX,pt_PT,", "", "");
    }
    
    public void testGetDeviceLocaleForNoMatch5()
    {
        verifyGetDeviceLocale("pt_PT,es_MX,en_US,en_GB", "", "");
    }
    
    public void verifyGetDeviceLocale(String localesList, String locale, String expect)
    {
        AppConfigHelper.localesList = localesList;
        assertEquals(teleNavDelegate.getDeviceLocale(locale), expect);
    }
    
    /*public void testCheckVersionChange_MainVersionChanged() throws Exception
    {
        StartupDao startupDao = PowerMock.createNiceMock(StartupDao.class);
        EasyMock.expect(daoManager.getStartupDao()).andReturn(startupDao).anyTimes();
        
        AppConfigHelper.buildNumber = "9001";
        
        EasyMock.expect(startupDao.getCurrentAppVersion()).andReturn(null).anyTimes();
        EasyMock.expect(startupDao.getBuildNumber()).andReturn(AppConfigHelper.buildNumber).anyTimes();
        
        PowerMock.mockStatic(DaoManager.class);
        DaoManager.getInstance().clearInternalRMS(true);
        
        PowerMock.mockStatic(FileStoreManager.class);
        FileStoreManager.clearMapCache();
        FileStoreManager.clearOpenglResource();
        
        startupDao.setCurrentAppVersion(AppConfigHelper.mandatoryClientVersion);
        startupDao.setBuildNumber(AppConfigHelper.buildNumber);
        startupDao.store();
        
        PowerMock.replayAll();
        
        teleNavDelegate.checkVersionChange();
        
        PowerMock.verifyAll();
        
    }
    
    public void testCheckVersionChange_BuildNumberChanged()
    {
        StartupDao startupDao = PowerMock.createNiceMock(StartupDao.class);
        EasyMock.expect(daoManager.getStartupDao()).andReturn(startupDao).anyTimes();
        
        AppConfigHelper.mandatoryClientVersion = "7.2";
        
        EasyMock.expect(startupDao.getCurrentAppVersion()).andReturn(AppConfigHelper.mandatoryClientVersion).anyTimes();
        EasyMock.expect(startupDao.getBuildNumber()).andReturn(null).anyTimes();
        
        PowerMock.mockStatic(DaoManager.class);
        DaoManager.getInstance().clearInternalRMS(false);
        
        PowerMock.mockStatic(FileStoreManager.class);
        FileStoreManager.clearMapCache();
        FileStoreManager.clearOpenglResource();
        
        startupDao.setCurrentAppVersion(AppConfigHelper.mandatoryClientVersion);
        startupDao.setBuildNumber(AppConfigHelper.buildNumber);
        startupDao.store();
        
        PowerMock.replayAll();
        
        teleNavDelegate.checkVersionChange();
        
        PowerMock.verifyAll();
        
    }*/
}

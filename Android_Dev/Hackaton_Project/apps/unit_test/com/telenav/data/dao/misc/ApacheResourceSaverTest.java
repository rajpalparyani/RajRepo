/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ApacheResourceSaverTest.java
 *
 */
package com.telenav.data.dao.misc;

import java.io.InputStream;
import java.util.Enumeration;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.i18n.PersistentResourceBundle;
import com.telenav.i18n.ResourceBundle;
import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;
import com.telenav.persistent.ITnPersistentContext;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStoreMock;
import com.telenav.res.ResourceManager;


import junit.framework.TestCase;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceManager.class, Logger.class, TeleNavDelegate.class})
public class ApacheResourceSaverTest extends TestCase
{
    DaoManager mockDaoManager;
    ApacheResouceDao indexBackupDao;
    ApacheResouceDao indexDao;
    ApacheResouceDao resourceBackupDao;
    SimpleConfigDao simpleConfigDao;
    ResourceBundle mockResourceBundle;
    PersistentResourceBundle mockPersistentResourceBundle;
    ResourceManager mockResManager;
    TeleNavDelegate telenavDelegate;

    protected void setUp() throws Exception
    {
        mockDaoManager = PowerMock.createMock(DaoManager.class);
        AbstractDaoManager.init(mockDaoManager);
        
        TnStoreMock backupStore = new TnStoreMock();
        backupStore.load();
        indexBackupDao = new ApacheResouceDao(backupStore);
        
        TnStoreMock indexStore = new TnStoreMock();
        indexStore.load();
        indexDao = new ApacheResouceDao(indexStore);
        
        TnStoreMock resourceBackupStore = new TnStoreMock();
        resourceBackupStore.load();
        resourceBackupDao = new ApacheResouceDao(resourceBackupStore);
        
        TnStoreMock configStore = new TnStoreMock();
        configStore.load();
        simpleConfigDao = new SimpleConfigDao(configStore, null);
        

        PowerMock.mockStatic(TnPersistentManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        ITnPersistentContext mockPersistentContext = PowerMock.createMock(ITnPersistentContext.class);
        TnPersistentManager.init(mockPersistentManager, mockPersistentContext);
        mockResourceBundle = PowerMock.createMock(ResourceBundle.class);
        mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        PowerMock.suppress(PowerMock.constructor(ResourceManager.class)); 
        PowerMock.mockStatic(ResourceManager.class);
        mockResManager = PowerMock.createMock(ResourceManager.class);
        
        telenavDelegate = PowerMock.createMock(TeleNavDelegate.class);
        PowerMock.mockStatic(TeleNavDelegate.class);
        EasyMock.expect(TeleNavDelegate.getInstance()).andReturn(telenavDelegate).anyTimes();
        
        super.setUp();
    }
    
    public void testSaveFile_NoDataNeedSave()
    {
        
        EasyMock.expect(DaoManager.getInstance().getApacheIndexBackupDao()).andReturn(indexBackupDao);
        EasyMock.expect(DaoManager.getInstance().getApacheServerIndexDao()).andReturn(indexDao);
        EasyMock.expect(DaoManager.getInstance().getApacheResouceBackupDao()).andReturn(resourceBackupDao);
        EasyMock.expect(DaoManager.getInstance().getSimpleConfigDao()).andReturn(simpleConfigDao);


        EasyMock.expect(ResourceManager.getInstance()).andReturn(mockResManager);
        EasyMock.expect(mockResManager.getCurrentBundle()).andReturn(mockResourceBundle);
        EasyMock.expect(mockResourceBundle.getPersistentBundle()).andReturn(mockPersistentResourceBundle);
        telenavDelegate.retrieveCarrier();
        mockPersistentResourceBundle.store();
        PowerMock.replayAll();
        ApacheResourceSaver.getInstance().saveFiles();
        PowerMock.verifyAll();
    }
    
    public void testSaveFile_Generic_ImageAndBinary()
    {
        byte[] image = new byte[0];
        byte[] binary = new byte[0];
        indexBackupDao.put("testIndex", new byte[0]);
        resourceBackupDao.put(ApacheResourceSaver.GENERIC + "/" + ApacheResourceSaver.IMAGE+"/testImage", image);
        resourceBackupDao.put(ApacheResourceSaver.GENERIC + "/" + ApacheResourceSaver.BINARY+"/testBinary", binary);
        
        EasyMock.expect(DaoManager.getInstance().getApacheIndexBackupDao()).andReturn(indexBackupDao);
        EasyMock.expect(DaoManager.getInstance().getApacheServerIndexDao()).andReturn(indexDao);
        EasyMock.expect(DaoManager.getInstance().getApacheResouceBackupDao()).andReturn(resourceBackupDao);
        EasyMock.expect(DaoManager.getInstance().getSimpleConfigDao()).andReturn(simpleConfigDao).anyTimes();

        EasyMock.expect(ResourceManager.getInstance()).andReturn(mockResManager).anyTimes();
        EasyMock.expect(mockResManager.getCurrentBundle()).andReturn(mockResourceBundle).anyTimes();
        EasyMock.expect(mockResourceBundle.getPersistentBundle()).andReturn(mockPersistentResourceBundle).anyTimes();
        mockPersistentResourceBundle.putGenericImage("testImage", ApacheResourceSaver.IMAGE, image);
        mockPersistentResourceBundle.putGenericBinary("testBinary", "", binary);
        mockPersistentResourceBundle.store();
        telenavDelegate.retrieveCarrier();
        PowerMock.replayAll();
        ApacheResourceSaver.getInstance().saveFiles();
        PowerMock.verifyAll();
    }
    
    public void testSaveFile_NotGeneric_ImageAndBinary()
    {
        byte[] image = new byte[0];
        byte[] binary = new byte[0];
        indexBackupDao.put("testIndex", new byte[0]);
        resourceBackupDao.put(ApacheResourceSaver.IMAGE+"/testImage", image);
        resourceBackupDao.put(ApacheResourceSaver.BINARY+"/testBinary", binary);
        
        EasyMock.expect(DaoManager.getInstance().getApacheIndexBackupDao()).andReturn(indexBackupDao);
        EasyMock.expect(DaoManager.getInstance().getApacheServerIndexDao()).andReturn(indexDao);
        EasyMock.expect(DaoManager.getInstance().getApacheResouceBackupDao()).andReturn(resourceBackupDao);
        EasyMock.expect(DaoManager.getInstance().getSimpleConfigDao()).andReturn(simpleConfigDao);


        EasyMock.expect(ResourceManager.getInstance()).andReturn(mockResManager).anyTimes();
        EasyMock.expect(mockResManager.getCurrentBundle()).andReturn(mockResourceBundle).anyTimes();
        EasyMock.expect(mockResourceBundle.getPersistentBundle()).andReturn(mockPersistentResourceBundle).anyTimes();
        mockPersistentResourceBundle.putImage("testImage", ApacheResourceSaver.IMAGE, image);
        mockPersistentResourceBundle.putBinary("testBinary", "", binary);
        mockPersistentResourceBundle.store();
        telenavDelegate.retrieveCarrier();
        PowerMock.replayAll();
        ApacheResourceSaver.getInstance().saveFiles();
        PowerMock.verifyAll();
    }
    
    public void testSaveFile_NotGeneric_TextString()
    {
        String txt = "111   testValue";
        byte[] text = txt.getBytes();
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnIoManager.init(mockIoManager);
        TnProperties mockTnProperties = PowerMock.createMock(TnProperties.class);
        Enumeration mockKeys = PowerMock.createMock(Enumeration.class);
        indexBackupDao.put("testIndex", new byte[0]);
        resourceBackupDao.put(ApacheResourceSaver.TEXT+"/testString.test", text);
        
        
        EasyMock.expect(DaoManager.getInstance().getApacheIndexBackupDao()).andReturn(indexBackupDao);
        EasyMock.expect(DaoManager.getInstance().getApacheServerIndexDao()).andReturn(indexDao);
        EasyMock.expect(DaoManager.getInstance().getApacheResouceBackupDao()).andReturn(resourceBackupDao);
        EasyMock.expect(DaoManager.getInstance().getSimpleConfigDao()).andReturn(simpleConfigDao);

        
        EasyMock.expect(TnIoManager.getInstance().createProperties()).andReturn(mockTnProperties).anyTimes();
        try
        {
            mockTnProperties.load(EasyMock.anyObject(InputStream.class));
        }
        catch (Exception e)
        {
        }
        PowerMock.mockStatic(Logger.class);
        Logger.log(EasyMock.anyInt(), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(mockTnProperties.size()).andReturn(1);
        EasyMock.expect(mockTnProperties.propertyNames()).andReturn(mockKeys);
        EasyMock.expect(mockKeys.hasMoreElements()).andReturn(true);
        EasyMock.expect(mockKeys.nextElement()).andReturn("111");
        EasyMock.expect(mockTnProperties.getProperty("111")).andReturn("testValue");
        EasyMock.expect(mockKeys.hasMoreElements()).andReturn(false);
        EasyMock.expect(ResourceManager.getInstance()).andReturn(mockResManager).anyTimes();
        EasyMock.expect(mockResManager.getCurrentBundle()).andReturn(mockResourceBundle).anyTimes();
        EasyMock.expect(mockResourceBundle.getPersistentBundle()).andReturn(mockPersistentResourceBundle).anyTimes();
        mockPersistentResourceBundle.putString(EasyMock.anyInt(), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
        mockPersistentResourceBundle.store();
        telenavDelegate.retrieveCarrier();
        //Logger.log(EasyMock.anyInt(), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
        PowerMock.replayAll();
        ApacheResourceSaver.getInstance().saveFiles();
        PowerMock.verifyAll();
    }
    
    public void testClear()
    {
        indexBackupDao.put("testIndex", new byte[0]);
        resourceBackupDao.put("testResource", new byte[0]);
        EasyMock.expect(DaoManager.getInstance().getApacheIndexBackupDao()).andReturn(indexBackupDao);
        EasyMock.expect(DaoManager.getInstance().getApacheResouceBackupDao()).andReturn(resourceBackupDao);
        PowerMock.replayAll();
        ApacheResourceSaver.getInstance().clear();
        PowerMock.verifyAll();
        assertEquals(0, indexBackupDao.size());
        assertEquals(0, resourceBackupDao.size());
    }
    
}

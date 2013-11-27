/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * BillingAccountDaoTest.java
 *
 */
package com.telenav.data.dao.serverproxy;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStoreMock;

/**
 *@author jxue
 *@date 2011-6-28
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SerializableManager.class,Address.class, TnPersistentManager.class})

public class BillingAccountDaoTest extends TestCase
{
    BillingAccountDao billingAccountDao;
    TnStoreMock tnStore;
    TnPersistentManager tnPersistentManager;
    
    public void setUp()throws Exception
    {
    	TnPersistentManager manager = PowerMock.createMock(TnPersistentManager.class);
    	PowerMock.mockStatic(TnPersistentManager.class);
    	EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager);
    	tnStore = new TnStoreMock();
    	tnStore.load();
    	EasyMock.expect(manager.createStore(TnPersistentManager.DATABASE, "billing_account")).andReturn(tnStore);
    	PowerMock.replayAll();
    	billingAccountDao = BillingAccountDao.getInstance();
    	billingAccountDao.setStore(tnStore);
        super.setUp();
    }
    
    public void testGetSocType()
    {
        String socTypeData = "I am ok";
        tnStore.put(20003, socTypeData.getBytes());
        Assert.assertEquals(socTypeData,  billingAccountDao.getSocType());
    }
    
    public void testSetSocType()
    {
        billingAccountDao.setSocType("I am ok");
        Assert.assertEquals("I am ok", new String(tnStore.get(20003)));
    }
    
    public void testGetAccountType()
    {
        String socTypeData = "I am ok";
        tnStore.put(20001, socTypeData.getBytes());
        Assert.assertEquals(socTypeData,  billingAccountDao.getAccountType());
    }
    
    public void testSetAccountType()
    {
    	billingAccountDao.setAccountType("I am good");
        billingAccountDao.setAccountType("I am ok");
        Assert.assertEquals("I am ok", new String(tnStore.get(20001)));
    }
    
    public void testGetPtn()
    {
        String ptn = "4083687541";
        tnStore.put(20004, ptn.getBytes());
        Assert.assertEquals("4083687541", billingAccountDao.getPtn());
    }
    
    public void testSetPtn()
    {
        billingAccountDao.setPtn("4083687541");
        Assert.assertEquals("4083687541", new String(tnStore.get(20004)));
    } 
        
    public void testGetSimCardId()
    {
        String ptn = "simCard";
        tnStore.put(20005, ptn.getBytes());
        Assert.assertEquals("simCard", billingAccountDao.getSimCardId());
    }
    
    public void testSetSimCardId()
    {
        billingAccountDao.setSimCardId("simCard");
        Assert.assertEquals("simCard", new String(tnStore.get(20005)));
    } 
    
    public void testGetAccountStatus()
    {
        tnStore.put(20002, "555".getBytes());
        Assert.assertEquals(555, billingAccountDao.getAccountStatus());
    }
   
    public void testSetAccountStatus()
    {
    	billingAccountDao.setAccountStatus(111);
        billingAccountDao.setAccountStatus(555);
        Assert.assertEquals(555, Integer.parseInt(new String(tnStore.get(20002))));
    }
    
    public void testGetUpsellDisplayTimes()
    {
    	tnStore.put(20008, "1".getBytes());
    	Assert.assertEquals(1, billingAccountDao.getUpsellDisplayTimes());
    }
    
    public void testSetUpsellDisplayTimes()
    {
    	billingAccountDao.setUpsellDisplayTimes(1);
    	Assert.assertEquals(1, Integer.parseInt(new String(tnStore.get(20008))));
    }
    
    public void testIsLoginNormal()
    {
        billingAccountDao.setAccountStatus(10);
        Assert.assertTrue(billingAccountDao.isLogin());
    }
    
    public void testIsLoginNew()
    {
    	billingAccountDao.setAccountStatus(14);
        Assert.assertTrue(billingAccountDao.isLogin());
    }
    
    public void testIsLoginOther()
    {
    	billingAccountDao.setAccountStatus(19);
        Assert.assertFalse(billingAccountDao.isLogin());
    }
    

}

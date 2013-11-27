/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufLoginProxyTest.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoAccountInfo;
import com.telenav.j2me.framework.protocol.ProtoForgetPinResp;
import com.telenav.j2me.framework.protocol.ProtoLoginResp;
import com.telenav.j2me.framework.protocol.ProtoSendPTNVerificationCodeReq;
import com.telenav.j2me.framework.protocol.ProtoSendPTNVerificationCodeResp;
import com.telenav.module.AppConfigHelper;

/**
 *@author yhzhou
 *@date 2011-9-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractDaoManager.class})
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class ProtoBufLoginProxyTest extends TestCase
{
    ProtoBufLoginProxy proxy = null;
 
    protected void setUp() throws Exception
    {
        proxy = new ProtoBufLoginProxy(null, null, null, null);
        Whitebox.setInternalState(AppConfigHelper.class, "isLoggerEnable", false);
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /*public void testAddRequestArgs_login_noParams() throws IOException
    {
        Vector requestVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_LOGIN, proxy);
        ProtoLoginReq.Builder builder = ProtoLoginReq.newBuilder();
        builder.setExecutorType("Login");
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(IServerProxyConstants.ACT_LOGIN, pb.getObjType());
        Assert.assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
    }*/
    
    /*public void testAddRequestArgs_encryptLogin_noParams() throws IOException
    {
        Vector requestVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_ENCRYPT_LOGIN, proxy);
        ProtoLoginReq.Builder builder = ProtoLoginReq.newBuilder();
        builder.setExecutorType("Login");
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(IServerProxyConstants.ACT_LOGIN, pb.getObjType());
        Assert.assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
    }*/

    /*public void testAddRequestArgs_login_hasParams() throws IOException
    {
        Vector requestVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_LOGIN, proxy);
        requestItem.params = new Vector();
        requestItem.params.add("620114");
        ProtoLoginReq.Builder builder = ProtoLoginReq.newBuilder();
        builder.setExecutorType("Login");
        builder.setVerificationCode("620114");
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(IServerProxyConstants.ACT_LOGIN, pb.getObjType());
        Assert.assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
    }*/
    
    /*public void testAddRequestArgs_encryptLogin_hasParams() throws IOException
    {
        Vector requestVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_ENCRYPT_LOGIN, proxy);
        requestItem.params = new Vector();
        requestItem.params.add("620114");
        ProtoLoginReq.Builder builder = ProtoLoginReq.newBuilder();
        builder.setExecutorType("Login");
        builder.setVerificationCode("620114");
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(IServerProxyConstants.ACT_LOGIN, pb.getObjType());
        Assert.assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
    }*/
    
    public void testAddRequestArgs_requestVerificationCode() throws IOException
    {
        Vector requestVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE, proxy);
        ProtoSendPTNVerificationCodeReq.Builder builder = ProtoSendPTNVerificationCodeReq.newBuilder();
        builder.setInValidField("");
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE, pb.getObjType());
        Assert.assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
    }
    
    public void testAddRequestArgs_forgetPIN() throws IOException
    {
        Vector requestVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_FORGET_PIN, proxy);
        ProtoSendPTNVerificationCodeReq.Builder builder = ProtoSendPTNVerificationCodeReq.newBuilder();
        builder.setInValidField("");
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(IServerProxyConstants.ACT_FORGET_PIN, pb.getObjType());
        Assert.assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
    }
    
    public void testAddRequestArgs_purchase_noParams()
    {
        Vector requestVector = new Vector();
        IllegalArgumentException illegalArgumentException = null;
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_PURCHASE, proxy);
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch(IllegalArgumentException e)
        {
            illegalArgumentException = e;
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        finally
        {
            assertNotNull(illegalArgumentException);
        }
    }
    
    public void testAddRequestArgs_purchase_lessParams()
    {
        Vector requestVector = new Vector();
        IllegalArgumentException illegalArgumentException = null;
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_PURCHASE, proxy);
        requestItem.params = new Vector();
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch(IllegalArgumentException e)
        {
            illegalArgumentException = e;
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        finally
        {
            assertNotNull(illegalArgumentException);
        }
    }
    
    public void testParseRequestSpecificData_login() throws IOException
    {
        ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        ProtoLoginResp.Builder builder = ProtoLoginResp.newBuilder();
        builder.setEqPin("2000");
        builder.setPin("1111");
        builder.setPtn("1111111111");
        String socType = "GPSNav_MRC2";
		builder.setSocCodeOfCurrentProduct(socType);
        builder.setUserId("11");
        builder.setStatus(0);
        builder.setErrorMessage("");
        protocolBuffer.setBufferData(builder.build().toByteArray());
        protocolBuffer.setObjType(IServerProxyConstants.ACT_LOGIN);
        ProtoBufLoginProxy mockProxy = PowerMock.createPartialMock(ProtoBufLoginProxy.class, "parsePurchaseData");

        AbstractDaoManager daoManager = PowerMock.createMock(AbstractDaoManager.class);
        PowerMock.mockStatic(AbstractDaoManager.class);
        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(daoManager).anyTimes();
        MandatoryNodeDao mandatoryNodeDao = PowerMock.createMock(MandatoryNodeDao.class);
        EasyMock.expect(daoManager.getMandatoryNodeDao()).andReturn(mandatoryNodeDao).anyTimes();
        MandatoryProfile mandatoryProfile = PowerMock.createMock(MandatoryProfile.class);
        EasyMock.expect(mandatoryNodeDao.getMandatoryNode()).andReturn(mandatoryProfile).anyTimes();
        UserInfo userInfo = PowerMock.createMock(UserInfo.class);
        EasyMock.expect(mandatoryProfile.getUserInfo()).andReturn(userInfo).anyTimes();
        
        mockProxy.parsePurchaseData((ProtoLoginResp)EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        BillingAccountDao billingAccoutDao = PowerMock.createMock(BillingAccountDao.class);
        EasyMock.expect(daoManager.getBillingAccountDao()).andReturn(billingAccoutDao).anyTimes();
        billingAccoutDao.setSocType(socType);
        billingAccoutDao.setPtn("1111111111");
        EasyMock.expectLastCall().once();
        PowerMock.replayAll();
        try
        {
            mockProxy.parseRequestSpecificData(protocolBuffer);
        }
        catch (IOException e)
        {
            fail(e.toString());
        }
        assertEquals("11", mockProxy.getUserId());
        assertEquals("1111", mockProxy.getPin());
        assertEquals("2000", mockProxy.getEqPin());
        assertEquals(socType, mockProxy.getSoc_code());
        assertEquals("1111111111", DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().phoneNumber);
        assertEquals("0", DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().ptnType);
        PowerMock.verifyAll();
    }
    
    public void testParseRequestSpecificData_login_failure() throws IOException
    {
    	ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        ProtoLoginResp.Builder builder = ProtoLoginResp.newBuilder();
        ProtoAccountInfo.Builder accountInfoBuilder = ProtoAccountInfo.newBuilder();
        accountInfoBuilder.setAccountStatus("1");
        accountInfoBuilder.setAccountType("premier");
        builder.setAccountInfo(accountInfoBuilder.build());
        builder.setStatus(1);
        protocolBuffer.setBufferData(builder.build().toByteArray());
        protocolBuffer.setObjType(IServerProxyConstants.ACT_LOGIN);
        ProtoBufLoginProxy mockProxy = PowerMock.createPartialMock(ProtoBufLoginProxy.class, "parsePurchaseData");
        mockProxy.parsePurchaseData((ProtoLoginResp)EasyMock.anyObject());
        
        AbstractDaoManager daoManager = PowerMock.createMock(AbstractDaoManager.class);
        PowerMock.mockStatic(AbstractDaoManager.class);
        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(daoManager).anyTimes();
        MandatoryNodeDao mandatoryNodeDao = PowerMock.createMock(MandatoryNodeDao.class);
        EasyMock.expect(daoManager.getMandatoryNodeDao()).andReturn(mandatoryNodeDao).anyTimes();
        MandatoryProfile mandatoryProfile = PowerMock.createMock(MandatoryProfile.class);
        EasyMock.expect(mandatoryNodeDao.getMandatoryNode()).andReturn(mandatoryProfile).anyTimes();
        UserInfo userInfo = PowerMock.createMock(UserInfo.class);
        EasyMock.expect(mandatoryProfile.getUserInfo()).andReturn(userInfo).anyTimes();
        
        mockProxy.parsePurchaseData((ProtoLoginResp)EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        BillingAccountDao billingAccoutDao = PowerMock.createMock(BillingAccountDao.class);
        EasyMock.expect(daoManager.getBillingAccountDao()).andReturn(billingAccoutDao).anyTimes();
        billingAccoutDao.setAccountStatus(1);
        billingAccoutDao.store();
        
        EasyMock.expectLastCall().once();
        PowerMock.replayAll();
        try
        {
        	mockProxy.parseRequestSpecificData(protocolBuffer);
        }
        catch (IOException e)
        {
            fail(e.toString());
        }
        assertEquals(1, mockProxy.getAccountStatus());
        assertEquals("premier", mockProxy.getAccountType());
    }
    
    public void testParseRequestSpecificData_forgetPin() throws IOException
    {
    	ProtoForgetPinResp.Builder builder = ProtoForgetPinResp.newBuilder();
    	builder.setErrorMessage("");
    	builder.setStatus(0);
    	ProtocolBuffer protoBuffer = new ProtocolBuffer();
    	protoBuffer.setObjType(IServerProxyConstants.ACT_FORGET_PIN);
    	protoBuffer.setBufferData(builder.build().toByteArray());
    	try 
    	{
    		proxy.parseRequestSpecificData(protoBuffer);
    	} 
    	catch (IOException e) 
    	{
    		e.printStackTrace();
    	}
    	assertEquals(proxy.getStatus(), 0);
    	assertEquals(proxy.getErrorMsg(), "");
    }
    
    public void testParseRequestSpecificData_requestVerificationCode() throws IOException
    {
    	ProtoSendPTNVerificationCodeResp.Builder builder = ProtoSendPTNVerificationCodeResp.newBuilder();
    	builder.setErrorMessage("");
    	builder.setStatus(0);
    	ProtocolBuffer protoBuffer = new ProtocolBuffer();
    	protoBuffer.setObjType(IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE);
    	protoBuffer.setBufferData(builder.build().toByteArray());
    	try 
    	{
    		proxy.parseRequestSpecificData(protoBuffer);
    	} 
    	catch (IOException e) 
    	{
    		e.printStackTrace();
    	}
    	assertEquals(proxy.getStatus(), 0);
    	assertEquals(proxy.getErrorMsg(), "");
    }
}

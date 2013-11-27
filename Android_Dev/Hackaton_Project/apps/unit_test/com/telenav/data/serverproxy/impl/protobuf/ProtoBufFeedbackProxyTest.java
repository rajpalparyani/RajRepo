/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufFeedbackProxyTest.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoFeedbackReq;
import com.telenav.j2me.framework.protocol.ProtoFeedbackResp;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.location.TnLocation;
import com.telenav.module.AppConfigHelper;

/**
 *@author yhzhou
 *@date 2011-9-29
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class ProtoBufFeedbackProxyTest extends TestCase
{
    ProtoBufFeedbackProxy proxy = null;
    
    protected void setUp() throws Exception
    {
        proxy = new ProtoBufFeedbackProxy(null, null, null, null);
        Whitebox.setInternalState(AppConfigHelper.class, "isLoggerEnable", false);
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testAddRequestArgs() throws IOException
    {
        Vector requestVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SEND_FEEDBACK, proxy);
        requestItem.params = new Vector();
        requestItem.params.add(0, 0);
        requestItem.params.add(1, "");
        requestItem.params.add(2, "good one");
        requestItem.params.add(3, 1317266335952L);
        TnLocation tnLocation = new TnLocation("along_route");
        tnLocation.setAccuracy(5);
        tnLocation.setAltitude(0.0f);
        tnLocation.setEncrypt(true);
        tnLocation.setHeading(0);
        tnLocation.setLocalTimeStamp(1317266948712L);
        tnLocation.setLongitude(-11824532);
        tnLocation.setSatellites(4);
        tnLocation.setSpeed(0);
        tnLocation.setTime(131726694871L);
        TnLocation[] gpsFixes = new TnLocation[1];
        gpsFixes[0] = tnLocation;
        requestItem.params.add(4, gpsFixes);
        requestItem.params.add(5, 1);
        ProtoFeedbackReq.Builder builder = ProtoFeedbackReq.newBuilder();
        builder.setFeedbackType("0");
        builder.setScreenTitle("");
        builder.setUserComments("good one");
        builder.setTimeStamp(1317266335952L);
        ProtoGpsList gpsList = ProtoBufServerProxyUtil.convertGpsList(gpsFixes, 1);
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        assertEquals(2, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        ProtocolBuffer pb1 = (ProtocolBuffer) requestVector.get(1);
        assertEquals(IServerProxyConstants.ACT_SEND_FEEDBACK, pb.getObjType());
        assertEquals("gps", pb1.getObjType());
        Assert.assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
        Assert.assertArrayEquals(gpsList.toByteArray(), pb1.getBufferData());
    }
        
    public void testParseRequestSpecificData() throws IOException
    {
        ProtoFeedbackResp.Builder builder = ProtoFeedbackResp.newBuilder();
        ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        builder.setErrorMessage("");
        builder.setStatus(0);
        protocolBuffer.setBufferData(builder.build().toByteArray());
        protocolBuffer.setObjType(IServerProxyConstants.ACT_SEND_FEEDBACK);
        ProtoFeedbackResp resp = null;
        try
        {
            proxy.parseRequestSpecificData(protocolBuffer);
            resp = ProtoFeedbackResp.parseFrom(protocolBuffer.getBufferData());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        assertEquals(0, resp.getStatus());
        assertEquals(0, resp.getErrorMessage().length());
    }
}

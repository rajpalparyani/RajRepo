/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufWeatherProxyTest.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.misc.AppStoreDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoFeedbackResp;
import com.telenav.j2me.framework.protocol.ProtoWeatherInfo;
import com.telenav.j2me.framework.protocol.ProtoWeatherResp;
import com.telenav.module.preference.carmodel.CarModelManager;

import junit.framework.TestCase;

/**
 * @author chrbu
 * @date 2011-12-31
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
@PrepareForTest(
{ ProtoBufWeatherProxyTest.class, AbstractProtobufServerProxy.class,
        AbstractServerProxy.class })
public class ProtoBufWeatherProxyTest extends TestCase
{

    ProtoBufWeatherProxy proxy = null;

    protected void setUp() throws Exception
    {
        proxy = new ProtoBufWeatherProxy(null, null, null, null);
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testRequestWeatherWithLocale() throws Exception
    {
//        String locale = "en";
//        Stop mStop = new Stop();
//        boolean isCanadianCarrier = true;
//        boolean useOriginalPicCode = true;
//        Vector mVector = new Vector();
//        mVector.add(locale);
//        ProtoBufWeatherProxy weatherProxy = PowerMock.createPartialMock(
//            ProtoBufWeatherProxy.class, "createProtoBufReq", "sendRequest");
//        PowerMock.expectPrivate(weatherProxy, "createProtoBufReq",
//            EasyMock.anyObject(RequestItem.class)).andReturn(mVector);
//        PowerMock.expectLastCall();
//
//        Object[] targetObject = new Object[]
//        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class), EasyMock.anyInt(), EasyMock.anyInt() };
//        PowerMock.expectPrivate(weatherProxy, "sendRequest", targetObject);
//        PowerMock.expectLastCall();
//
//        PowerMock.replayAll();
//        weatherProxy.requestWeather(mStop, isCanadianCarrier, locale, useOriginalPicCode, 3, 30000);
//        assertEquals(1, mVector.size());
//        PowerMock.verifyAll();

    }

    public void testRequestWeatherWithoutLocale() throws Exception
    {
//        Stop mStop = new Stop();
//        boolean isCanadianCarrier = true;
//        boolean useOriginalPicCode= true;
//        Vector mVector = new Vector();
//        ProtoBufWeatherProxy weatherProxy = PowerMock.createPartialMock(
//            ProtoBufWeatherProxy.class, "createProtoBufReq", "sendRequest");
//        PowerMock.expectPrivate(weatherProxy, "createProtoBufReq",
//            EasyMock.anyObject(RequestItem.class)).andReturn(mVector);
//        PowerMock.expectLastCall();
//
//        Object[] targetObject = new Object[]
//        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class), EasyMock.anyInt(), EasyMock.anyInt() };
//        PowerMock.expectPrivate(weatherProxy, "sendRequest", targetObject);
//        PowerMock.expectLastCall();
//
//        PowerMock.replayAll();
//        weatherProxy.requestWeather(mStop, isCanadianCarrier, useOriginalPicCode, 3, 30000);
//        assertEquals(0, mVector.size());
//        PowerMock.verifyAll();
    }

    public void testParseRequestSpecificData() throws IOException
    {
        ProtoWeatherResp.Builder builder = ProtoWeatherResp.newBuilder();
        ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        builder.setErrorMessage("");
        builder.setStatus(0);
        protocolBuffer.setBufferData(builder.build().toByteArray());
        protocolBuffer.setObjType(IServerProxyConstants.ACT_GET_WEATHER);
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

    public void testaddRequestArgs() throws Exception
    {
        Vector mVector = new Vector();
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_GET_WEATHER,
                proxy);
        requestItem.params = new Vector();
        requestItem.params.add(new Stop());
        requestItem.params.add(false);
        requestItem.params.add("1111");
        requestItem.params.add(true);
        proxy.addRequestArgs(mVector, requestItem);
        assertEquals(1, mVector.size());

    }

}

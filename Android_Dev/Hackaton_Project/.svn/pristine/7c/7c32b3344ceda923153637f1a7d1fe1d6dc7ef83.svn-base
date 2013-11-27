/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * JsonDimProxyTest.java
 *
 */
package com.telenav.data.serverproxy.impl.json;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.json.tnme.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.data.serverproxy.IServerProxyConstants;

/**
 *@author Barney (cchen@telenav.cn)
 *@date Sep 29, 2011
 */
public class JsonDimProxyTest
{
    private JsonDimProxy jsonDimProxy;
    private Host host;
    private Comm comm;
    
    @Before
    public void setUp() throws Exception
    {
        host = EasyMock.createMock(Host.class);
        comm = PowerMock.createPartialMockForAllMethodsExcept(Comm.class, "setHostProvider", "getHostProvider", "sendData");
        jsonDimProxy = new JsonDimProxy(host, comm, null, null);
    }

    @After
    public void tearDown() throws Exception
    {
    }
  
    @Test
    public void testGetHostByAction_nullAction()
    {
        String action = null;
        Host requestHost = jsonDimProxy.getHostByAction(action);
        
        Assert.assertEquals(host, requestHost);
    }

    @Test
    public void testHandleResponse_nullData()
    {
        byte[] binData = null;
        jsonDimProxy.handleResponse(binData);
        Assert.assertEquals(ICommCallback.NO_DATA, jsonDimProxy.getStatus());
    }
    
    @Test
    public void testHandleResponse_zeroLength()
    {
        byte[] binData = new byte[0];
        jsonDimProxy.handleResponse(binData);
        Assert.assertEquals(ICommCallback.NO_DATA, jsonDimProxy.getStatus());
    }
    
    @Test
    public void testHandleResponse()
    {
        byte[] binData = {123, 34, 109, 117, 108, 116, 105, 68, 97, 116, 97, 34, 58, 91, 123, 34, 100, 97, 116, 97, 34, 58, 34, 123, 92, 34, 99, 97, 114, 114, 105, 101, 114, 92, 34, 58, 110, 117, 108, 108, 44, 92, 34, 112, 116, 110, 92, 34, 58, 110, 117, 108, 108, 44, 92, 34, 115, 116, 97, 116, 117, 115, 92, 34, 58, 50, 44, 92, 34, 101, 114, 114, 111, 114, 77, 115, 103, 92, 34, 58, 92, 34, 65, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 32, 83, 116, 97, 114, 116, 32, 69, 114, 114, 111, 114, 58, 32, 80, 108, 101, 97, 115, 101, 32, 99, 108, 111, 115, 101, 32, 116, 104, 101, 32, 97, 112, 112, 108, 105, 99, 97, 116, 105, 111, 110, 32, 116, 111, 32, 116, 114, 121, 32, 97, 103, 97, 105, 110, 46, 92, 34, 125, 34, 44, 34, 97, 99, 116, 105, 111, 110, 84, 121, 112, 101, 34, 58, 34, 71, 101, 116, 80, 116, 110, 34, 44, 34, 117, 115, 101, 114, 80, 114, 111, 102, 105, 108, 101, 34, 58, 110, 117, 108, 108, 125, 93, 125};
        try
        {
            jsonDimProxy.handleResponse(binData);
        }
        catch (Exception e)
        {
            Assert.assertEquals(ICommCallback.EXCEPTION_PARSE, jsonDimProxy.getStatus());
        }
        Assert.assertEquals(ICommCallback.SUCCESS, jsonDimProxy.getStatus());
    }

    @Test
    public void testRequestGetToken()
    {
        JsonDimProxy mock = PowerMock.createPartialMock(JsonDimProxy.class, "getProfile");
        mock.requestGetToken();
    }
    
    @Test
    public void testRequestGetPtn()
    {
        String token = "42756367213575";
        JsonDimProxy mock = PowerMock.createPartialMock(JsonDimProxy.class, "getProfile");
        mock.requestGetPtn(token, false, (byte) 0, 3000);
    }
    
    @Test
    public void testRequestGetCarrier()
    {
        String ptn = "2064235241";
        JsonDimProxy mock = PowerMock.createPartialMock(JsonDimProxy.class, "getProfile");
//        mock.requestGetCarrier(ptn);
    }

}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MaiTaiAuthenticateTest.java
 *
 */
/**
 * 
 */
package com.telenav.sdk.maitai.impl;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.app.CommManager;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.HostProvider;

/**
 *@author Bu changrong
 *@date 2011-11-17
 */
/**
 * @author chrbu
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ MaiTaiAuthenticate.class, CommManager.class, HostProvider.class, Host.class })
public class MaiTaiAuthenticateTest extends TestCase
{
	/*
    public void test_parseResponse() throws Exception
    {
        MaiTaiAuthenticate mockMaiTaiAuthenticate = PowerMock.createPartialMock(MaiTaiAuthenticate.class, "getUrl");
        StringBuffer reponse = new StringBuffer();
        String code = "ok", message = "errorMessage";
        reponse.append("<code>");
        reponse.append(code);
        reponse.append("</code>");
        reponse.append("<message>");
        reponse.append(message);
        reponse.append("</message>");

        PowerMock.replayAll();
        boolean result = Whitebox.invokeMethod(mockMaiTaiAuthenticate, "parseResponse", reponse.toString());
        assertEquals(true, result);
        assertEquals(message, mockMaiTaiAuthenticate.getErrorMessage());
        PowerMock.replayAll();
    }

    public void test_parseResponse_false() throws Exception
    {
        MaiTaiAuthenticate mockMaiTaiAuthenticate = PowerMock.createPartialMock(MaiTaiAuthenticate.class, "getUrl");
        StringBuffer reponse = new StringBuffer();
        String code = "error", message = "errorMessage";
        reponse.append("<code>");
        reponse.append(code);
        reponse.append("</code>");
        reponse.append("<message>");
        reponse.append(message);
        reponse.append("</message>");

        PowerMock.replayAll();
        boolean result = Whitebox.invokeMethod(mockMaiTaiAuthenticate, "parseResponse", reponse.toString());
        assertEquals(false, result);
        assertEquals(message, mockMaiTaiAuthenticate.getErrorMessage());
        PowerMock.replayAll();
    }

    public void test_parseResponse_null() throws Exception
    {
        MaiTaiAuthenticate mockMaiTaiAuthenticate = PowerMock.createPartialMock(MaiTaiAuthenticate.class, "getUrl");
        StringBuffer reponse = new StringBuffer();
        PowerMock.replayAll();
        boolean result = Whitebox.invokeMethod(mockMaiTaiAuthenticate, "parseResponse",reponse.toString());
        assertEquals(false, result);
        PowerMock.replayAll();
    }

    public void test_getUrl() throws Exception
    {
        MaiTaiAuthenticate mockMaiTaiAuthenticate = PowerMock.createPartialMock(MaiTaiAuthenticate.class, "dump");
        PowerMock.mockStatic(CommManager.class);
        CommManager mockCommManager = PowerMock.createPartialMock(CommManager.class, "getComm");
        HostProvider mockHostProvider = PowerMock.createPartialMock(HostProvider.class, "createHost");
        Comm mockComm = PowerMock.createPartialMock(Comm.class, "getHostProvider");
        Host mockHost = PowerMock.createMock(Host.class);
        Whitebox.setInternalState(mockHost, "protocol", "http");
        Whitebox.setInternalState(mockHost, "host", "www.sina.com");
        Whitebox.setInternalState(mockHost, "port", 8080);
        Whitebox.setInternalState(mockHost, "file", "file");

        EasyMock.expect(CommManager.getInstance()).andReturn(mockCommManager);
        PowerMock.expectPrivate(mockCommManager, "getComm").andReturn(mockComm);
        PowerMock.expectPrivate(mockComm, "getHostProvider").andReturn(mockHostProvider);
        PowerMock.expectPrivate(mockHostProvider, "createHost", EasyMock.anyObject(String.class)).andReturn(mockHost);
        PowerMock.replayAll();
        String result = Whitebox.invokeMethod(mockMaiTaiAuthenticate, "getUrl");
        assertTrue((result.indexOf("file") > 0));
        PowerMock.verifyAll();
    }
    
    
    public void test_getUrl_host_null() throws Exception
    {
        MaiTaiAuthenticate mockMaiTaiAuthenticate = PowerMock.createPartialMock(MaiTaiAuthenticate.class, "dump");
        PowerMock.mockStatic(CommManager.class);
        CommManager mockCommManager = PowerMock.createPartialMock(CommManager.class, "getComm");
        HostProvider mockHostProvider = PowerMock.createPartialMock(HostProvider.class, "createHost");
        Comm mockComm = PowerMock.createPartialMock(Comm.class, "getHostProvider");

        EasyMock.expect(CommManager.getInstance()).andReturn(mockCommManager);
        PowerMock.expectPrivate(mockCommManager, "getComm").andReturn(mockComm);
        PowerMock.expectPrivate(mockComm, "getHostProvider").andReturn(mockHostProvider);
        PowerMock.expectPrivate(mockHostProvider, "createHost", EasyMock.anyObject(String.class)).andReturn(null);
        PowerMock.replayAll();
        String result = Whitebox.invokeMethod(mockMaiTaiAuthenticate, "getUrl");
        assertNotNull(result);
        PowerMock.verifyAll();
    }
	*/
}

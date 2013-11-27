/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidMviewerLocationProviderTest.java
 *
 */
/**
 * 
 */
package com.telenav.location.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import com.telenav.location.TnLocationException;
import com.telenav.location.TnLocationManager;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Socket.class, AndroidMviewerLocationProvider.class})
public class AndroidMviewerLocationProviderTest
{

    AndroidMviewerLocationProvider mviewerLocProvider;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        mviewerLocProvider = new AndroidMviewerLocationProvider(TnLocationManager.GPS_179_PROVIDER);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.location.android.AndroidMviewerLocationProvider#getMviewerData(int)}.
     * @throws Exception 
     */
    @Test
    public void testGetMviewerData() throws Exception
    {
        Socket mockSocket = PowerMock.createMock(Socket.class);
        InputStream is = PowerMock.createMock(InputStream.class);
        PowerMock.expectNew(Socket.class, "10.0.2.2", 11159).andReturn(mockSocket);
        mockSocket.setSoTimeout(5000);
        EasyMock.expect(mockSocket.getInputStream()).andReturn(is);
        EasyMock.expect(is.read(EasyMock.anyObject(byte[].class))).andReturn(Integer.valueOf(0));
        EasyMock.expect(is.read(EasyMock.anyObject(byte[].class))).andReturn(Integer.valueOf(-1));
        PowerMock.replayAll();
        String retStr = mviewerLocProvider.getMviewerData(5000);
        assertEquals(0, retStr.length());
        PowerMock.verifyAll();
    }

    @Test(expected=TnLocationException.class)
    public void testGetMviewerDataException() throws Exception
    {
        Socket mockSocket = PowerMock.createMock(Socket.class);
        InputStream is = PowerMock.createMock(InputStream.class);
        PowerMock.expectNew(Socket.class, "10.0.2.2", 11159).andReturn(mockSocket);
        mockSocket.setSoTimeout(5000);
        EasyMock.expect(mockSocket.getInputStream()).andReturn(is);
        EasyMock.expect(is.read(EasyMock.anyObject(byte[].class))).andThrow(new IOException());
        EasyMock.expect(is.read(EasyMock.anyObject(byte[].class))).andReturn(Integer.valueOf(-1));
        PowerMock.replayAll();
        String retStr = mviewerLocProvider.getMviewerData(5000);
        assertEquals(0, retStr.length());
        PowerMock.verifyAll();
    }
    /**
     * Test method for {@link com.telenav.location.android.AndroidMviewerLocationProvider#AndroidMviewerLocationProvider(java.lang.String)}.
     */
    @Test
    public void testAndroidMviewerLocationProvider()
    {
        assertNotNull(mviewerLocProvider);
        
    }

}

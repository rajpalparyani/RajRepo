/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnIoManagerTest.java
 *
 */
/**
 * 
 */
package com.telenav.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.logger.Logger;


/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-8
 */
@RunWith(PowerMockRunner.class) 
@PrepareForTest({TnIoManager.class, Logger.class})
public class TnIoManagerTest
{
    private TnIoManager mockIoManger;
    private InputStream mockIs;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        mockIs = PowerMock.createMock(InputStream.class);
        
        mockIoManger = new TnIoManager()
        {
            @Override
            public InputStream openFileFromAppBundle(String fileName) throws IOException
            {
                // TODO Auto-generated method stub
                return mockIs;
            }
            
            @Override
            public TnProperties createProperties()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TnGZIPOutputStream createGZIPOutputStream(OutputStream os, int size)
                    throws IOException
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TnGZIPOutputStream createGZIPOutputStream(OutputStream os) throws IOException
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TnGZIPInputStream createGZIPInputStream(InputStream is, int size)
                    throws IOException
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TnGZIPInputStream createGZIPInputStream(InputStream is) throws IOException
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TnBase64OutputStream createBase64OutputStream(OutputStream os)
                    throws IOException
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TnBase64InputStream createBase64InputStream(InputStream is) throws IOException
            {
                // TODO Auto-generated method stub
                return null;
            }

			@Override
            public String[] listFileFromAppBundle(String path) throws IOException
            {
                // TODO Auto-generated method stub
                return null;
            }
        };
        Whitebox.setInternalState(TnIoManager.class, "initCount", 0);
        TnIoManager.init(mockIoManger);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.io.TnIoManager#getInstance()}.
     */
    @Test
    public void testGetInstance()
    {
        assertEquals(mockIoManger, TnIoManager.getInstance());
    }

    @Test
    public void testInitTwice()
    {
        TnIoManager tnIoMgr = PowerMock.createMock(TnIoManager.class);
        TnIoManager.init(tnIoMgr);
        assertNotSame(tnIoMgr, TnIoManager.getInstance());
    }
    
    /**
     * Test method for {@link com.telenav.io.TnIoManager#readBytes(java.io.InputStream)}.
     * @throws IOException 
     */
    @Test
    public void testReadBytes() throws IOException
    {
        InputStream is = PowerMock.createMock(InputStream.class);
        byte[] data = TnIoManager.readBytes(is);
        assertEquals(0, data.length);
    }
    
    @Test
    public void testReadBytesRealInputStream() throws IOException
    {
        String str = new String("testReadBytesRealInputStream");
        InputStream is = new ByteArrayInputStream(str.getBytes());
        byte[] data = TnIoManager.readBytes(is);
        assertEquals(str.getBytes().length, data.length);
    }

   
    
    /**
     * Test method for {@link com.telenav.io.TnIoManager#openFileBytesFromAppBundle(java.lang.String)}.
     * @throws IOException 
     */
    @Test
    public void testOpenFileBytesFromAppBundleException() throws IOException
    {
        IOException ioException = PowerMock.createMock(IOException.class);
        EasyMock.expect(mockIs.read(EasyMock.anyObject(byte[].class))).andThrow(ioException);
        PowerMock.mockStatic(Logger.class);
        Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(Throwable.class));
        mockIs.close();
        PowerMock.replayAll();
        byte[] returnValue = TnIoManager.getInstance().openFileBytesFromAppBundle("mock");
        assertNull(returnValue);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testOpenFileBytesFromAppBundle() throws IOException
    {
        byte[] data = TnIoManager.getInstance().openFileBytesFromAppBundle("mock");
        assertEquals(0, data.length);
    }
    
    @Test(expected=IOException.class)
    public void testReadBytesThrowException() throws IOException
    {
        InputStream is = PowerMock.createMock(InputStream.class);
        IOException e = new IOException();
        EasyMock.expect(is.read(EasyMock.anyObject(byte[].class))).andThrow(e);
        PowerMock.replayAll();
        byte[] data = TnIoManager.readBytes(is);
        assertEquals(0, data.length);
        PowerMock.verifyAll();
    }
    
    /**
     * @throws IOException 
     */
    @Test(expected=IOException.class)
    public void testReadBytesThrowCloseException() throws IOException
    {
        InputStream is = PowerMock.createMock(InputStream.class);
        IOException e = new IOException();
        EasyMock.expect(is.read(EasyMock.anyObject(byte[].class))).andThrow(e);
        is.close();
        EasyMock.expectLastCall().andThrow(e);
        PowerMock.replayAll();
        byte[] data = TnIoManager.readBytes(is);
        assertEquals(0, data.length);
        PowerMock.verifyAll();
    }

}

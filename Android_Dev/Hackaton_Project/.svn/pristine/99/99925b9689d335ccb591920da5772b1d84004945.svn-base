/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidIoManagerTest.java
 *
 */
package com.telenav.io.android;

import static org.junit.Assert.*;

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

import com.telenav.io.TnGZIPInputStream;
import com.telenav.nio.android.AndroidNioManager;

import android.content.Context;
import android.content.res.AssetManager;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-7
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidIoManager.class, AssetManager.class})
public class AndroidIoManagerTest
{
    AndroidIoManager androidIoManager;
    Context mockContext;
    @Before
    public void setUp() throws Exception
    {
        mockContext = PowerMock.createMock(Context.class);
        Whitebox.setInternalState(AndroidIoManager.class, "initCount", 0);
        AndroidIoManager.init(new AndroidIoManager(mockContext));
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testOpenFileFromAppBundle() throws IOException
    {
        PowerMock.mockStatic(AssetManager.class);
        AssetManager assetManager = PowerMock.createMock(AssetManager.class);
        InputStream mockInputStream = PowerMock.createMock(InputStream.class);
        EasyMock.expect(mockContext.getAssets()).andReturn(assetManager);
        EasyMock.expect(assetManager.open("test")).andReturn(mockInputStream);
        PowerMock.replayAll();
        InputStream is = AndroidIoManager.getInstance().openFileFromAppBundle("test");
        assertEquals(mockInputStream, is);
    }

    @Test
    public void testCreateProperties()
    {
        assertNotNull(AndroidIoManager.getInstance().createProperties());
    }

    @Test(expected=IOException.class)
    public void testCreateGZIPInputStreamInputStream() throws IOException
    {
        InputStream is  = new InputStream()
        {
            
            @Override
            public int read() throws IOException
            {
                // TODO Auto-generated method stub
                return 0;
            }
        };

        assertNotNull(AndroidIoManager.getInstance().createGZIPInputStream(is));

    }

    @Test(expected=IOException.class)
    public void testCreateGZIPInputStreamInputStreamInt() throws IOException
    {
        InputStream is  = new InputStream()
        {
            
            @Override
            public int read() throws IOException
            {
                // TODO Auto-generated method stub
                return 0;
            }
        };

        assertNotNull(AndroidIoManager.getInstance().createGZIPInputStream(is, 512));
    }

    @Test
    public void testCreateGZIPOutputStreamOutputStream() throws IOException
    {
        OutputStream os = new OutputStream()
        {

            @Override
            public void write(int b) throws IOException
            {
                // TODO Auto-generated method stub
                
            }
            
        };
        assertNotNull(AndroidIoManager.getInstance().createGZIPOutputStream(os));
    }

    @Test
    public void testCreateGZIPOutputStreamOutputStreamInt() throws IOException
    {
        OutputStream os = new OutputStream()
        {

            @Override
            public void write(int b) throws IOException
            {
                // TODO Auto-generated method stub
                
            }
            
        };
        assertNotNull(AndroidIoManager.getInstance().createGZIPOutputStream(os, 512));
    }

    @Test
    public void testCreateBase64InputStream() throws IOException
    {
        InputStream is  = new InputStream()
        {
            
            @Override
            public int read() throws IOException
            {
                // TODO Auto-generated method stub
                return 0;
            }
        };
        assertNotNull(AndroidIoManager.getInstance().createBase64InputStream(is));
    }

    @Test
    public void testCreateBase64OutputStream() throws IOException
    {
        OutputStream os = new OutputStream()
        {

            @Override
            public void write(int b) throws IOException
            {
                // TODO Auto-generated method stub
                
            }
            
        };
        assertNotNull(AndroidIoManager.getInstance().createBase64OutputStream(os));
    }

    @Test
    public void testAndroidIoManager()
    {
        assertNotNull(AndroidIoManager.getInstance());
    }

    @Test(expected=IllegalStateException.class)
    public void testCreateGZIPInputStream() throws IOException
    {
        InputStream mockIs = PowerMock.createMock(InputStream.class);
        TnGZIPInputStream tnGzipInputStream = AndroidIoManager.getInstance().createGZIPInputStream(mockIs);
        assertNotNull(tnGzipInputStream);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testCreateGZIPInputStreamInt() throws IOException
    {
        InputStream mockIs = PowerMock.createMock(InputStream.class);
        TnGZIPInputStream tnGzipInputStream = AndroidIoManager.getInstance().createGZIPInputStream(mockIs, 100);
        assertNotNull(tnGzipInputStream);
    }
    
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidGZIPOutputStreamTest.java
 *
 */
/**
 * 
 */
package com.telenav.io.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-8
 */

public class AndroidGZIPOutputStreamTest
{

    AndroidGZIPOutputStream gzipOutputStream;
    OutputStream os;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        os = PowerMock.createMock(OutputStream.class);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.io.android.AndroidGZIPOutputStream#AndroidGZIPOutputStream(java.io.OutputStream)}.
     * @throws IOException 
     */
    @Test
    public void testAndroidGZIPOutputStreamOutputStream() throws IOException
    {
        gzipOutputStream = new AndroidGZIPOutputStream(os);
        assertNotNull(gzipOutputStream);
    }

    /**
     * Test method for {@link com.telenav.io.android.AndroidGZIPOutputStream#AndroidGZIPOutputStream(java.io.OutputStream, int)}.
     * @throws IOException 
     */
    @Test
    public void testAndroidGZIPOutputStreamOutputStreamInt() throws IOException
    {
        gzipOutputStream = new AndroidGZIPOutputStream(os, 512);
        assertNotNull(gzipOutputStream);
    }

}

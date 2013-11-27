/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidGZIPInputStreamTest.java
 *
 */
/**
 * 
 */
package com.telenav.io.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-8
 */
/**
 * @author byma
 *
 */
public class AndroidGZIPInputStreamTest
{

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.io.android.AndroidGZIPInputStream#AndroidGZIPInputStream(java.io.InputStream)}.
     * @throws IOException 
     */
    @Test(expected=IllegalStateException.class)
    public void testAndroidGZIPInputStreamInputStream() throws IOException
    {
        InputStream is = EasyMock.createMock(InputStream.class);
        AndroidGZIPInputStream androidGZIPInputStream = new AndroidGZIPInputStream(is);
        assertNotNull(androidGZIPInputStream);
        
    }

    /**
     * Test method for {@link com.telenav.io.android.AndroidGZIPInputStream#AndroidGZIPInputStream(java.io.InputStream, int)}.
     * @throws IOException 
     */
    @Test(expected=IllegalStateException.class)
    public void testAndroidGZIPInputStreamInputStreamInt() throws IOException
    {
        InputStream is = EasyMock.createMock(InputStream.class);
        AndroidGZIPInputStream androidGZIPInputStream = new AndroidGZIPInputStream(is, 100);
        assertNotNull(androidGZIPInputStream);
    }

}

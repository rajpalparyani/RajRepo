/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * Base64InputStreamTest.java
 *
 */
/**
 * 
 */
package com.telenav.io.base64;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-13
 */
public class Base64InputStreamTest
{
    Base64InputStream base64InputStream;
    InputStream inputStream;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        inputStream = new StringBufferInputStream("string buffer input stream");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.io.base64.Base64InputStream#markSupported()}.
     */
    @Test
    public void testMarkSupported()
    {
        base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test method for {@link com.telenav.io.base64.Base64InputStream#read()}.
     */
    @Test(expected=NullPointerException.class)
    public void testReadNull()
    {
        InputStream currentInputStream = new StringBufferInputStream(null);
        base64InputStream = new Base64InputStream(currentInputStream);
        int i = 0;
        try
        {
            i = base64InputStream.read();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }

    @Test
    public void testRead()
    {
        base64InputStream = new Base64InputStream(inputStream);
        int i = 0;
        try
        {
            i = base64InputStream.read();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(178, i);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayIntInt()
    {
        base64InputStream = new Base64InputStream(inputStream);
        String abc = "Hello world.";
        try
        {
            base64InputStream.read(abc.getBytes(), -1, -1);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayIntIntException()
    {
        base64InputStream = new Base64InputStream(inputStream);
        String abc = "Hello world.";
        try
        {
            base64InputStream.read(abc.getBytes(), 0, 100);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadByteArrayIntIntExceptionA()
    {
        base64InputStream = new Base64InputStream(inputStream);
        String abc = "Hello world.";
        int readInt = -1;
        try
        {
            readInt = base64InputStream.read(abc.getBytes(), 0, 0);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(0, readInt);
    }
    
    /**
     * Test method for {@link com.telenav.io.base64.Base64InputStream#Base64InputStream(java.io.InputStream)}.
     */
    @Test
    public void testBase64InputStreamInputStream()
    {
        base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
    }

    /**
     * Test method for {@link com.telenav.io.base64.Base64InputStream#Base64InputStream(java.io.InputStream, boolean, int, byte[])}.
     */
    @Test
    public void testBase64InputStreamInputStreamBooleanIntByteArray()
    {
        base64InputStream = new Base64InputStream(inputStream, true, 3, (new byte[]{' '}));
        assertNotNull(base64InputStream);
    }

   
}

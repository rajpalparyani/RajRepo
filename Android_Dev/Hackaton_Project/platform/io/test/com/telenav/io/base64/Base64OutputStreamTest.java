/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * Base64OutputStreamTest.java
 *
 */
/**
 * 
 */
package com.telenav.io.base64;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-13
 */
public class Base64OutputStreamTest
{
    Base64OutputStream base64OutputStream;
    OutputStream  outputStream;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        outputStream = new ByteArrayOutputStream();
        base64OutputStream = new Base64OutputStream(outputStream);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.telenav.io.base64.Base64OutputStream#close()}.
     */
    @Test
    public void testClose()
    {
        try
        {
            base64OutputStream.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /**
     * Test method for {@link com.telenav.io.base64.Base64OutputStream#write(int)}.
     */
    @Test
    public void testWriteInt()
    {
        try
        {
            base64OutputStream.write(100);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] singleBytes = Whitebox.getInternalState(base64OutputStream, "singleByte");
        assertNotNull(singleBytes[0]);
    }

    /**
     * Test method for {@link com.telenav.io.base64.Base64OutputStream#Base64OutputStream(java.io.OutputStream)}.
     */
    @Test
    public void testBase64OutputStreamOutputStream()
    {
        assertNotNull(base64OutputStream);
    }

    /**
     * Test method for {@link com.telenav.io.base64.Base64OutputStream#Base64OutputStream(java.io.OutputStream, boolean, int, byte[])}.
     */
    @Test
    public void testBase64OutputStreamOutputStreamBooleanIntByteArray()
    {
        byte[] lineSeperator = new byte[]{ '\r', '\n' };
        base64OutputStream = new Base64OutputStream(outputStream, false, 0, lineSeperator);
        assertNotNull(base64OutputStream);
    }

}

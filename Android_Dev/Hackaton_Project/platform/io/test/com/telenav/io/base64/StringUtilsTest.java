/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * StringUtilsTest.java
 *
 */
/**
 * 
 */
package com.telenav.io.base64;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-13
 */
public class StringUtilsTest
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
     * Test method for {@link com.telenav.io.base64.StringUtils#getBytesIso8859_1(java.lang.String)}.
     */
    @Test
    public void testGetBytesIso8859_1()
    {
        byte[] encodesBytes = StringUtils.getBytesIso8859_1("ABC");
        assertNotNull(encodesBytes);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#getBytesUsAscii(java.lang.String)}.
     */
    @Test
    public void testGetBytesUsAscii()
    {
        byte[] encodesBytes = StringUtils.getBytesUsAscii("ABC");
        assertNotNull(encodesBytes);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#getBytesUtf16(java.lang.String)}.
     */
    @Test
    public void testGetBytesUtf16()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf16("ABC");
        assertNotNull(encodesBytes);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#getBytesUtf16Be(java.lang.String)}.
     */
    @Test
    public void testGetBytesUtf16Be()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf16Be("ABC");
        assertNotNull(encodesBytes);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#getBytesUtf16Le(java.lang.String)}.
     */
    @Test
    public void testGetBytesUtf16Le()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf16Le("ABC");
        assertNotNull(encodesBytes);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#getBytesUtf8(java.lang.String)}.
     */
    @Test
    public void testGetBytesUtf8()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf8("ABC");
        assertNotNull(encodesBytes);
    }

   

   
    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#newStringIso8859_1(byte[])}.
     */
    @Test
    public void testNewStringIso8859_1()
    {
        byte[] encodesBytes = StringUtils.getBytesIso8859_1("ABC");
        String decodeString = StringUtils.newStringIso8859_1(encodesBytes);
        assertEquals("ABC", decodeString);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#newStringUsAscii(byte[])}.
     */
    @Test
    public void testNewStringUsAscii()
    {
        byte[] encodesBytes = StringUtils.getBytesUsAscii("ABC");
        String decodeString = StringUtils.newStringUsAscii(encodesBytes);
        assertEquals("ABC", decodeString);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#newStringUtf16(byte[])}.
     */
    @Test
    public void testNewStringUtf16()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf16("ABC");
        String decodeString = StringUtils.newStringUtf16(encodesBytes);
        assertEquals("ABC", decodeString);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#newStringUtf16Be(byte[])}.
     */
    @Test
    public void testNewStringUtf16Be()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf16Be("ABC");
        String decodeString = StringUtils.newStringUtf16Be(encodesBytes);
        assertEquals("ABC", decodeString);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#newStringUtf16Le(byte[])}.
     */
    @Test
    public void testNewStringUtf16Le()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf16Le("ABC");
        String decodeString = StringUtils.newStringUtf16Le(encodesBytes);
        assertEquals("ABC", decodeString);
    }

    /**
     * Test method for {@link com.telenav.io.base64.StringUtils#newStringUtf8(byte[])}.
     */
    @Test
    public void testNewStringUtf8()
    {
        byte[] encodesBytes = StringUtils.getBytesUtf8("ABC");
        String decodeString = StringUtils.newStringUtf8(encodesBytes);
        assertEquals("ABC", decodeString);
    }

}

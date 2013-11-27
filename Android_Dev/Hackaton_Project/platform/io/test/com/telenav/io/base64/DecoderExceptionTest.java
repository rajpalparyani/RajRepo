/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * DecoderExceptionTest.java
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
public class DecoderExceptionTest
{
    DecoderException decoderException;
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
     * Test method for {@link com.telenav.io.base64.DecoderException#DecoderException()}.
     */
    @Test
    public void testDecoderException()
    {
        decoderException = new DecoderException();
        assertNotNull(decoderException);
    }

    /**
     * Test method for {@link com.telenav.io.base64.DecoderException#DecoderException(java.lang.String)}.
     */
    @Test
    public void testDecoderExceptionString()
    {
        decoderException = new DecoderException("decodeException");
        assertEquals("decodeException", decoderException.getMessage());
    }

}

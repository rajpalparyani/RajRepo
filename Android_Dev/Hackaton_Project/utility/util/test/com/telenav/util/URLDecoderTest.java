/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * URLDecoder.java
 *
 */
package com.telenav.util;

import junit.framework.TestCase;

public class URLDecoderTest extends TestCase
{
    public void testDecode() throws Exception
    {
        String originalStr = "Most parts of a software system do not work in isolation, but collaborate with other parts to get their job done.";
        String encoding = "UTF-8";
        String encodedStr = URLEncoder.encode(originalStr, encoding);
        String decodedStr = URLDecoder.decode(encodedStr, encoding);
        assertEquals(decodedStr, originalStr);
    }
}

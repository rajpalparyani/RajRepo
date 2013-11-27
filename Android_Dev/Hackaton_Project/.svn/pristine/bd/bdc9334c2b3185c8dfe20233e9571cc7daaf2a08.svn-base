package com.telenav.util;

import junit.framework.TestCase;

public class URLEncoderTest extends TestCase
{
    public void testEncode() throws Exception
    {
        String originalStr = "Most parts of a software system do not work in isolation, but collaborate with other parts to get their job done.";
        String encoding = "UTF-8";
        URLEncoder.encode(originalStr, encoding);
    }

    public void testDontNeedEncoding()
    {
        assertEquals(true, URLEncoder.dontNeedEncoding('d'));
        assertEquals(false, URLEncoder.dontNeedEncoding('&'));
        assertEquals(false, URLEncoder.dontNeedEncoding('\n'));
    }
}

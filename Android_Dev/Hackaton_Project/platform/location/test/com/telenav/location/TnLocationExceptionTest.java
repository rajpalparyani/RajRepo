/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnLocationExceptionTest.java
 *
 */
package com.telenav.location;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-7-7
 */
public class TnLocationExceptionTest
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
     * Test method for {@link com.telenav.location.TnLocationException#TnLocationException(java.lang.String)}.
     */
    @Test
    public void testTnLocationException()
    {
        TnLocationException locationException = new TnLocationException("fff");
        assertEquals("fff", locationException.getMessage());
    }

}

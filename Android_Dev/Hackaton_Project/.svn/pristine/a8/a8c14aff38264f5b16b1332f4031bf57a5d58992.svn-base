/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestPrimitiveTypeCache.java
 *
 */
package com.telenav.util;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-3
 */
public class PrimitiveTypeCacheTest extends TestCase
{
    public void testValueOfBoolean()
    {
        assertEquals(Boolean.TRUE, PrimitiveTypeCache.valueOf(true));
        assertEquals(Boolean.FALSE, PrimitiveTypeCache.valueOf(false));
    }
    
    public void testValueOfByte()
    {
        for(int i = -128; i < 128; i++)
        {
            byte j = (byte)i;
            assertEquals(new Byte(j), PrimitiveTypeCache.valueOf(j));
        }
    }
    
    public void testValueOfInt()
    {
        for(int i = -128; i < 128; i++)
        {
            assertEquals(new Integer(i), PrimitiveTypeCache.valueOf(i));
        }
        
        assertEquals(new Integer(-65535), PrimitiveTypeCache.valueOf(-65535));
        assertEquals(new Integer(65535), PrimitiveTypeCache.valueOf(65535));
        assertEquals(new Integer(-2147483648), PrimitiveTypeCache.valueOf(-2147483648));
        assertEquals(new Integer(2147483647), PrimitiveTypeCache.valueOf(2147483647));
    }
    
    public void testValueOfLong()
    {
        for(long i = -128; i < 128; i++)
        {
            assertEquals(new Long(i), PrimitiveTypeCache.valueOf(i));
        }
        assertEquals(new Long(-65535), PrimitiveTypeCache.valueOf((long)-65535));
        assertEquals(new Long(65535), PrimitiveTypeCache.valueOf((long)65535));
        assertEquals(new Long(-2147483648), PrimitiveTypeCache.valueOf((long)-2147483648));
        assertEquals(new Long(2147483647), PrimitiveTypeCache.valueOf((long)2147483647));
    }
}

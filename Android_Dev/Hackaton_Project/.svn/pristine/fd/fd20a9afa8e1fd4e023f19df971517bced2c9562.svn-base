/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PrimitiveTypeCache.java
 *
 */
package com.telenav.util;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-26
 */
public class PrimitiveTypeCache
{
    static final Integer integerCache[] = new Integer[-(-128) + 127 + 1];

    static
    {
        for (int i = 0; i < integerCache.length; i++)
            integerCache[i] = new Integer(i - 128);
    }
    
    static final Long longCache[] = new Long[-(-128) + 127 + 1];

    static
    {
        for (int i = 0; i < longCache.length; i++)
            longCache[i] = new Long(i - 128);
    }
    
    static final Byte byteCache[] = new Byte[-(-128) + 127 + 1];

    static
    {
        for (int i = 0; i < byteCache.length; i++)
            byteCache[i] = new Byte((byte) (i - 128));
    }
    
    public static Integer valueOf(int i)
    {
        final int offset = 128;
        if (i >= -128 && i <= 127)
        { // must cache
            return integerCache[i + offset];
        }
        return new Integer(i);
    }

    public static Long valueOf(long l)
    {
        final int offset = 128;
        if (l >= -128 && l <= 127)
        { // will cache
            return longCache[(int) l + offset];
        }
        return new Long(l);
    }

    public static Byte valueOf(byte b)
    {
        final int offset = 128;
        return byteCache[(int) b + offset];
    }
    
    public static Boolean valueOf(boolean b)
    {
        return (b ? Boolean.TRUE : Boolean.FALSE);
    }
}

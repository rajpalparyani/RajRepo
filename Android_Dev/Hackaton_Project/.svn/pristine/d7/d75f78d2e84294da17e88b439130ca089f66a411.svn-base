/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LongPointTest.java
 *
 */
package com.telenav.datatypes.map;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

/**
 *@author yning
 *@date 2011-7-4
 */
public class LongPointTest
{
    @Test
    public void testAdd()
    {
        long shift = 140737488355328l;//2 ^ 47
        long[] points = new long[4];
        for(int i = 0; i < points.length; i++)
        {
            points[i] = 140737488355325l + i;
        }
        
        LongPoint.add(points, shift);
        
        assertEquals(281474976710653l, points[0]);
        assertEquals(281474976710654l, points[1]);
        assertEquals(281474976710655l, points[2]);
        assertEquals(281474976710656l, points[3]);
    }
    
    @Test
    public void testDevide()
    {
        long point = LongPoint.fromInts(100, 75);
        long expected = LongPoint.fromInts(4, 3);
        int denominator = 25;
        long actual = LongPoint.divide(point, denominator);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDevideInArray()
    {
        int denominator = 3;
        long[] points = new long[3];
        long[] expected = new long[3];
        for(int i = 0; i < points.length; i++)
        {
            points[i] = LongPoint.fromInts(denominator * i, denominator * (i + 1));
            expected[i] = LongPoint.fromInts(i, i + 1);
        }
        
        LongPoint.divide(points, denominator);
        
        for(int i = 0; i < points.length; i++)
        {
            assertEquals(expected[i], points[i]);
        }
    }
    
    @Test
    public void testFirst()
    {
        long point = LongPoint.fromInts(100, 75);
        assertEquals(100, LongPoint.first(point));
    }
    
    @Test
    public void testSecond()
    {
        long point = LongPoint.fromInts(100, 75);
        assertEquals(75, LongPoint.second(point));
    }
    
    @Test
    public void testFromIntsXY()
    {
        int x = 1;
        int y = 1;
        long expected = 4294967297l; //1 << 32 | 1
        long result = LongPoint.fromInts(x, y);
        assertEquals(expected, result);
        
        x = 2147483647;
        y = 2147483647; // 2147483647 << 32 | 2147483647
        expected = 9223372034707292159l;
        result = LongPoint.fromInts(x, y);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFromIntsArray()
    {
        int[] array = new int[]{1, 1};
        long expected = 4294967297l;
        long result = LongPoint.fromInts(array);
        assertEquals(expected, result);
        
        array = new int[]{2147483647, 2147483647};
        expected = 9223372034707292159l;
        result = LongPoint.fromInts(array);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFromIntsArrays()
    {
        int[][] arrays = new int[2][2];
        int[] array1 = new int[]{1, 1};
        int[] array2 = new int[]{2147483647, 2147483647};
        
        arrays[0] = array1;
        arrays[1] = array2;
        
        long[] expected = new long[]{4294967297l, 9223372034707292159l};
        
        long[] actual = LongPoint.fromInts(arrays);
        assertArrayEquals(expected, actual);
        
        assertNull(LongPoint.fromInts((int[][])null));
    }
    
    @Test
    public void testFromPolar()
    {
        int length = 10000;
        int heading = 45;
        
        long expected = 30369713757087l; //10000 * cos(45) << 32 | 10000 * sin(45)
        long actual = LongPoint.fromPolar(length, heading);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLength()
    {
        long vector = 12884901892l; //3 << 32 | 4
        int expected = 5; // 3 * 3 + 4 * 4 = 5 * 5
        int actual = LongPoint.length(vector);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMax()
    {
        //p1 = (5, 10);
        //p2 = (2, 17);
        //expected = (5, 17)
        long p1 = 21474836490l;
        long p2 = 8589934609l;
        long expected = 21474836497l;
        
        long actual = LongPoint.max(p1, p2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMin()
    {
        //p1 = (5, 10);
        //p2 = (2, 17);
        //expected = (2, 10)
        long p1 = 21474836490l;
        long p2 = 8589934609l;
        long expected = 8589934602l;
        
        long actual = LongPoint.min(p1, p2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMultBy()
    {
        //p1 = (5, 10);
        //c = 5;
        //expected = (25, 50)
        long p1 = 21474836490l;
        int c = 5;
        long expected = 107374182450l;
        long actual = LongPoint.multBy(p1, c);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMultByOutOfBount()
    {
        //p1 = (1073741826, 1073741826);
        //c = 2;
        //expected = (Integer.MAX_VALUE, Integer.MAX_VALUE)
        long p1 = 4611686028091064322l;
        int c = 2;
        long expected = 9223372034707292159l;
        long actual = LongPoint.multBy(p1, c);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRescale()
    {
        //p1 = (60, 30);
        //numerator = 2;
        //denominator = 3;
        //expected = (40, 20)
        long p1 = 257698037790l;
        int numerator = 2;
        int denominator = 3;
        long expected = 171798691860l;
        long actual = LongPoint.rescale(p1, numerator, denominator);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRescaleWithArray()
    {
        //p1 = (60, 30);
        //p2 = (15, 45);
        //p3 = (24, 36);
        //numerator = 2;
        //denominator = 3;
        //expected = {(40, 20), (10, 30), (16, 24)}
        long p1 = 257698037790l;
        long p2 = 64424509485l;
        long p3 = 103079215140l;
        long[] points = {p1, p2, p3};
        int numerator = 2;
        int denominator = 3;
        long expected1 = 171798691860l;
        long expected2 = 42949672990l;
        long expected3 = 68719476760l;
        long[] expected = {expected1, expected2, expected3};
        
        LongPoint.rescale(points, numerator, denominator);
        assertArrayEquals(expected, points);
    }
    
    @Test
    public void testScalarProduct()
    {
        //p1 = (5, 10);
        //p2 = (2, 17);
        //expected = (5 * 2) + (10 * 17) = 180
        long p1 = 21474836490l;
        long p2 = 8589934609l;
        int expected = 180;
        
        int actual = LongPoint.scalarProduct(p1, p2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSubtract()
    {
        //base = (50, 50);
        //shift = (30, 20);
        //expected = {(20, 30),(20, 31),(20, 32)};
        
        long[] points = new long[3];
        for(int i = 0; i < points.length; i++)
        {
            points[i] = 214748364850l + i;
        }
        
        long shift = 128849018900l;
        
        LongPoint.subtract(points, shift);
        
        long[] expected = new long[3];
        expected[0] = 85899345950l;
        expected[1] = 85899345951l;
        expected[2] = 85899345952l;
        
        assertArrayEquals(expected, points);
    }
    
    @Test
    public void testSubVectors()
    {
        //p1 = (50, 50);
        //shift = (30, 20);
        //expected = (20, 30);
        
        long p1 = 214748364850l;
        
        long shift = 128849018900l;
        
        long actual = LongPoint.subVectors(p1, shift);
        
        long expected = 85899345950l;
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToInt()
    {
        long value1 = 80000l;
        int intValue1 = LongPoint.toInt(value1);
        assertEquals(80000, intValue1);
        
        long value2 = (long)Integer.MAX_VALUE + 3;
        int intValue2 = LongPoint.toInt(value2);
        assertEquals(Integer.MAX_VALUE, intValue2);
        
        long value3 = (long)Integer.MIN_VALUE - 3;
        int intValue3 = LongPoint.toInt(value3);
        assertEquals(Integer.MIN_VALUE, intValue3);
    }
    
    @Test
    public void testToString()
    {
        //p1 = (50, 50);
        long p1 = 214748364850l;
        String expected = "(50,50)";
        String actual = LongPoint.toString(p1);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringForArray()
    {
        //{(50,50),(50,51),(50,52)}
        long[] points = new long[3];
        for(int i = 0; i < points.length; i++)
        {
            points[i] = 214748364850l + i;
        }
        
        String nullString = "null";
        assertEquals(nullString, LongPoint.toString(null));
        
        String expected = "[(50,50), (50,51), (50,52)]";
        String actual = LongPoint.toString(points);
        assertEquals(expected, actual);
    }
}

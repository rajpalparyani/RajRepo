/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestStringTokenizer.java
 *
 */
package com.telenav.util;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-7
 */
public class StringTokenizerTest extends TestCase
{
    public void testHasMoreTokens()
    {
        String testString = "Yes it's a test";
        String testString2 = "";
        StringTokenizer tokenizer = new StringTokenizer(testString, " ", true);
        StringTokenizer tokenizer2 = new StringTokenizer(testString2, " ", true);
        assertTrue(tokenizer.hasMoreTokens());
        assertFalse(tokenizer2.hasMoreTokens());
    }
    
    public void testNextToken()
    {
        String testString = "Yes it's a test";
        StringTokenizer tokenizer = new StringTokenizer(testString, " ", true);
        assertEquals("Yes", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("it's", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("a", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("test", tokenizer.nextToken());
        assertEquals(null, tokenizer.nextToken());
        
        testString = " Yes it's a test";
        tokenizer = new StringTokenizer(testString, " ", false);
        assertEquals("", tokenizer.nextToken());
        assertEquals("Yes", tokenizer.nextToken());
        assertEquals("it's", tokenizer.nextToken());
        assertEquals("a", tokenizer.nextToken());
        assertEquals("test", tokenizer.nextToken());
        assertEquals(null, tokenizer.nextToken());
        
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestDefaultLoggerFilter.java
 *
 */
package com.telenav.logger;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-3
 */
public class DefaultLoggerFilterTest extends TestCase
{
    public void testIsLoggable_A()
    {
        int level = 1;
        String enableClasses = "class1,class2";
        DefaultLoggerFilter loggerFilter = new DefaultLoggerFilter(level, enableClasses);
        assertTrue(loggerFilter.isLoggable(level, "class1", "", null, null));
        assertTrue(loggerFilter.isLoggable(level, "class2", "", null, null));
        assertFalse(loggerFilter.isLoggable(level, "class3", "", null, null));
        assertTrue(loggerFilter.isLoggable(0, "class1", "", null, null));
        assertTrue(loggerFilter.isLoggable(0, "class2", "", null, null));
        assertFalse(loggerFilter.isLoggable(0, "class3", "", null, null));
    }
    
    public void testIsLoggable_B()
    {
        int level = 1;
        String enableAll = "class1,class2, *";
        DefaultLoggerFilter loggerFilter = new DefaultLoggerFilter(level, enableAll);
        assertTrue(loggerFilter.isLoggable(level, "class1", "", null, null));
        assertTrue(loggerFilter.isLoggable(level, "class2", "", null, null));
        assertTrue(loggerFilter.isLoggable(level, "class3", "", null, null));
        assertTrue(loggerFilter.isLoggable(0, "class1", "", null, null));
        assertTrue(loggerFilter.isLoggable(0, "class2", "", null, null));
        assertTrue(loggerFilter.isLoggable(0, "class3", "", null, null));
    }
    
    public void testIsLoggable_C()
    {
        int level = 1;
        String enableClasses = "";
        DefaultLoggerFilter loggerFilter = new DefaultLoggerFilter(level, enableClasses);
        assertFalse(loggerFilter.isLoggable(level, "class1", "", null, null));
    }
    
    public void testFilter()
    {
        int level = 1;
        String enableClasses = "class1,class2";
        DefaultLoggerFilter loggerFilter = new DefaultLoggerFilter(level, enableClasses);
        assertEquals(false, loggerFilter.filter(0, "class1", "", null, null));
        assertEquals(true, loggerFilter.filter(1, "class1", "", null, null));
        assertEquals(true, loggerFilter.filter(2, "class1", "", null, null));
        assertEquals(false, loggerFilter.filter(2, "class3", "", null, null));
    }
    
    public void testGetLevel()
    {
        int level = 1;
        DefaultLoggerFilter loggerFilter = new DefaultLoggerFilter(level, "");
        assertEquals(level, loggerFilter.getLevel());
    }
}

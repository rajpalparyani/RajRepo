/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestLongIntLRUCache.java
 *
 */
package com.telenav.cache.intcache;

import java.util.Vector;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-7
 */
public class LongIntLRUCacheTest extends TestCase
{
    public void testClear()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        assertEquals(0, cache.size());
        
        cache.put(1001, "1001");
        cache.put(1002, "1002");
        assertEquals(2, cache.size());
        
        cache.clear();
        assertEquals(0, cache.size());
    }
    
    public void testContainsKey()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        assertFalse(cache.containsKey(1001));
        
        cache.put(1001, "1001");
        assertTrue(cache.containsKey(1001));
    }
    
    public void testContainerValue()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);

        assertFalse(cache.containsValue("1001"));

        cache.put(1001, "1001");
        assertTrue(cache.containsValue("1001"));
        
        cache.put(1002, null);
        assertTrue(cache.containsValue(null));
    }
    
    public void testElementsReturnsVector()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        
        Vector expected = new Vector();
        expected.addElement("1001");
        expected.addElement("1002");
        
        cache.put(1001, "1001");
        cache.put(1002, "1002");
        Vector actual = cache.elements();
        
        assertEquals(expected, actual);
    }
    
    public void testElements()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);

        Vector expected = new Vector();
        expected.addElement("1001");
        expected.addElement("1002");

        cache.put(1001, "1001");
        cache.put(1002, "1002");
        Vector actual = new Vector();
        cache.elements(actual);

        assertEquals(expected, actual);
    }
    
    public void testGet()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        cache.put(1001, "1001");
        cache.put(1002, "1002");
        cache.put(1003, "1003");
        assertEquals("1001", cache.get(1001));
        assertEquals("1002", cache.get(1002));
        assertEquals("1003", cache.get(1003));
    }
    
    public void testGetMaxSize()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        assertEquals(5, cache.getMaxSize());
        cache = new LongIntLRUCache(4);
        assertEquals(4, cache.getMaxSize());
        cache = new LongIntLRUCache(3);
        assertEquals(3, cache.getMaxSize());
    }
    
    public void testKeysReturnsLongIntVector()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        cache.put(1001, "1001");
        cache.put(1002, "1002");
        cache.put(1003, "1003");
        
        LongIntVector result = cache.keys();
        assertEquals(3, result.size());
        assertEquals(true, result.contains(1001));
        assertEquals(true, result.contains(1002));
        assertEquals(true, result.contains(1003));
    }
    
    public void testKeys()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        cache.put(1001, "1001");
        cache.put(1002, "1002");
        cache.put(1003, "1003");
        
        LongIntVector result = new LongIntVector(3);
        cache.keys(result);
        assertEquals(3, result.size());
        assertEquals(true, result.contains(1001));
        assertEquals(true, result.contains(1002));
        assertEquals(true, result.contains(1003));
    }
    
    public void testPutReturnsObject()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        assertEquals(null, cache.put(1001, "1001"));
        assertEquals(null, cache.put(1002, "1002"));
        assertEquals(null, cache.put(1003, "3"));
        assertEquals(null, cache.put(1004, "1004"));
        assertEquals(null, cache.put(1005, "1005"));
        
        //return the old value when put a new value with same key.
        assertEquals("3", cache.put(1003, "1003"));
        //remove&return the eldest one when exceeds the max count.
        assertEquals("1001", cache.put(1006, "1006"));
        assertEquals(null, cache.get(1001));
        assertEquals("1002", cache.put(1007, "1007"));
        assertEquals(null, cache.get(1002));
        assertEquals("1004", cache.put(1008, "1008"));
        assertEquals(null, cache.get(1004));
        assertEquals("1005", cache.put(1009, "1009"));
        assertEquals(null, cache.get(1005));
        assertEquals("1003", cache.put(1010, "1010"));//1003 has been modified recently.
        assertEquals(null, cache.get(1003));
    }
    
    public void testPutAll()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        cache.put(1001, "1001");
        cache.put(1002, "1002");
        cache.put(1003, "1003");
        cache.put(1004, "1004");
        cache.put(1005, "1005");
        
        LongIntLRUCache newCache = new LongIntLRUCache(5);
        newCache.putAll(cache);
        
        assertEquals(5, newCache.size());
        assertTrue(newCache.containsKey(1001));
        assertTrue(newCache.containsKey(1002));
        assertTrue(newCache.containsKey(1003));
        assertTrue(newCache.containsKey(1004));
        assertTrue(newCache.containsKey(1005));
        assertTrue(newCache.containsValue("1001"));
        assertTrue(newCache.containsValue("1002"));
        assertTrue(newCache.containsValue("1003"));
        assertTrue(newCache.containsValue("1004"));
        assertTrue(newCache.containsValue("1005"));
    }
    
    public void testRemove()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        assertEquals(null, cache.remove(1001));
        assertEquals(null, cache.remove(1002));
        assertEquals(null, cache.remove(1003));
        assertEquals(null, cache.remove(1004));
        assertEquals(null, cache.remove(1005));
        
        cache.put(1001, "1001");
        cache.put(1002, "1002");
        cache.put(1003, "1003");
        cache.put(1004, "1004");
        cache.put(1005, "1005");
        
        assertEquals(5, cache.size());
        assertTrue(cache.containsKey(1001));
        assertTrue(cache.containsKey(1002));
        assertTrue(cache.containsKey(1003));
        assertTrue(cache.containsKey(1004));
        assertTrue(cache.containsKey(1005));
        
        assertEquals("1001", cache.remove(1001));
        assertEquals("1002", cache.remove(1002));
        assertEquals("1003", cache.remove(1003));
        assertEquals("1004", cache.remove(1004));
        assertEquals("1005", cache.remove(1005));
        
        assertEquals(0, cache.size());
        assertFalse(cache.containsKey(1001));
        assertFalse(cache.containsKey(1002));
        assertFalse(cache.containsKey(1003));
        assertFalse(cache.containsKey(1004));
        assertFalse(cache.containsKey(1005));
    }
    
    public void testSize()
    {
        LongIntLRUCache cache = new LongIntLRUCache(5);
        cache.put(1001, "1001");
        assertEquals(1, cache.size());
        cache.put(1002, "1002");
        assertEquals(2, cache.size());
        cache.put(1003, "1003");
        assertEquals(3, cache.size());
        cache.put(1004, "1004");
        assertEquals(4, cache.size());
        cache.put(1005, "1005");
        assertEquals(5, cache.size());
        cache.put(1006, "1006");
        assertEquals(5, cache.size());
        cache.put(1007, "1007");
        assertEquals(5, cache.size());
        cache.remove(1007);
        assertEquals(4, cache.size());
        cache.remove(1006);
        assertEquals(3, cache.size());
        cache.remove(1005);
        assertEquals(2, cache.size());
        cache.remove(1004);
        assertEquals(1, cache.size());
        cache.remove(1003);
        assertEquals(0, cache.size());
    }
}

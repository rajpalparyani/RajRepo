/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestLRUCache.java
 *
 */
package com.telenav.cache;

import java.util.Vector;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-3
 */
public class LRUCacheTest extends TestCase
{
    public void testSize()
    {
        //test size increase;
        LRUCache lruCache = new LRUCache(5);
        lruCache.put("1", "1");
        assertEquals(1, lruCache.size());
        lruCache.put("2", "2");
        assertEquals(2, lruCache.size());
        lruCache.put("3", "3");
        assertEquals(3, lruCache.size());
        lruCache.put("4", "4");
        assertEquals(4, lruCache.size());
        lruCache.put("5", "5");
        assertEquals(5, lruCache.size());
        lruCache.put("6", "6");
        assertEquals(5, lruCache.size());
        
        //test size decrease;
        lruCache.remove("1");
        assertEquals(5, lruCache.size());
        lruCache.remove("2");
        assertEquals(4, lruCache.size());
        lruCache.remove("3");
        assertEquals(3, lruCache.size());
        lruCache.remove("4");
        assertEquals(2, lruCache.size());
        lruCache.remove("5");
        assertEquals(1, lruCache.size());
        lruCache.remove("6");
        assertEquals(0, lruCache.size());
    }
    
    public void testClear()
    {
        LRUCache lruCache = new LRUCache(5);
        lruCache.put("1", "1");
        assertEquals(1, lruCache.size());
        lruCache.put("2", "2");
        assertEquals(2, lruCache.size());
        lruCache.put("3", "3");
        
        lruCache.clear();
        assertEquals(0, lruCache.size());
    }
    
    public void testContainsValue()
    {
        LRUCache lruCache = new LRUCache(5);
        assertEquals(false, lruCache.containsValue("11"));
        lruCache.put("1", "11");
        assertEquals(true, lruCache.containsValue("11"));
        
        assertEquals(false, lruCache.containsValue(null));
        lruCache.put("2", null);
        assertEquals(true, lruCache.containsValue(null));
    }
    
    public void testContains()
    {
        LRUCache lruCache = new LRUCache(5);
        assertEquals(false, lruCache.contains("11"));
        lruCache.put("1", "11");
        assertEquals(true, lruCache.contains("11"));
        
        assertEquals(false, lruCache.contains(null));
        lruCache.put("2", null);
        assertEquals(true, lruCache.contains(null));
    }
    
    public void testContainsKey()
    {
        LRUCache lruCache = new LRUCache(5);
        assertEquals(false, lruCache.containsKey("1"));
        lruCache.put("1", "11");
        assertEquals(true, lruCache.containsKey("1"));
    }
    
    public void testElements()
    {
        LRUCache lruCache = new LRUCache(5);
        String str1 = "ni hao a";
        String str2 = "hello world";
        
        Vector expect = new Vector();
        expect.addElement(str1);
        expect.addElement(str2);
        
        Vector actual = new Vector();
        lruCache.put("1", str1);
        lruCache.put("2", str2);
        
        lruCache.elements(actual);
        
        assertEquals(expect, actual);
    }
    
    public void testElementsReturnVector()
    {
        LRUCache lruCache = new LRUCache(5);
        String str1 = "ni hao a";
        String str2 = "hello world";
        
        Vector expect = new Vector();
        expect.addElement(str1);
        expect.addElement(str2);
        
        lruCache.put("1", str1);
        lruCache.put("2", str2);
        
        Vector actual = lruCache.elements();
        
        assertEquals(expect, actual);
    }
    
    public void testGet()
    {
        LRUCache lruCache = new LRUCache(5);
        String key = "1";
        String str1 = "hello world";
        lruCache.put(key, str1);
        
        assertEquals(str1, lruCache.get(key));
    }
    
    public void testGetMaxSize()
    {
        LRUCache lruCache = new LRUCache(5);
        assertEquals(5, lruCache.getMaxSize());
        lruCache = new LRUCache(7);
        assertEquals(7, lruCache.getMaxSize());
        lruCache = new LRUCache(0);
        assertEquals(0, lruCache.getMaxSize());
    }
    
    public void testKeys()
    {
        LRUCache lruCache = new LRUCache(5);
        String key1 = "1";
        String key2 = "2";
        String str1 = "ni hao a";
        String str2 = "hello world";
        
        lruCache.put(key1, str1);
        lruCache.put(key2, str2);
        
        Vector expect = new Vector();
        expect.addElement(key1);
        expect.addElement(key2);
        
        Vector actual = new Vector();
        lruCache.keys(actual);
        assertEquals(expect, actual);
        
        actual = lruCache.keys();
        assertEquals(expect, actual);
    }
    
    public void testPut()
    {
        LRUCache lruCache = new LRUCache(3);
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        String key3 = "key3";
        String value3 = "value3";
        String key4 = "key4";
        String value4 = "value4";
        
        //if add value with new key, return null.
        assertNull(lruCache.put(key1, value1));
        //if input a new value with exists key, the old value will be returned.
        assertEquals(value1, lruCache.put(key1, "new value1"));
        assertEquals("new value1", lruCache.put(key1, value1));
        assertNull(lruCache.put(key2, value2));
        assertNull(lruCache.put(key3, value3));
        //if value count exceeds max value, the eldest value will be removed and returned.
        assertEquals(value1, lruCache.put(key4, value4));
    }
    
    public void testPutAll()
    {
        LRUCache lruCache1 = new LRUCache(3);
        LRUCache lruCache2 = new LRUCache(3);
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        String key3 = "key3";
        String value3 = "value3";
        lruCache2.put(key1, value1);
        lruCache2.put(key2, value2);
        lruCache2.put(key3, value3);
        lruCache1.putAll(lruCache2);
        
        assertEquals(value1, lruCache1.get(key1));
        assertEquals(value2, lruCache1.get(key2));
        assertEquals(value3, lruCache1.get(key3));
        
        lruCache1 = new LRUCache(3);
        lruCache2 = new LRUCache(4);
        String key4 = "key4";
        String value4 = "value4";
        lruCache2.put(key1, value1);
        lruCache2.put(key2, value2);
        lruCache2.put(key3, value3);
        lruCache2.put(key4, value4);
        
        lruCache1.putAll(lruCache2);
        assertEquals(null, lruCache1.get(key1));
        assertEquals(value2, lruCache1.get(key2));
        assertEquals(value3, lruCache1.get(key3));
        assertEquals(value4, lruCache1.get(key4));
    }
    
    public void testRemove()
    {
        LRUCache lruCache1 = new LRUCache(3);
        String key1 = "key1";
        String value1 = "value1";
        
        assertNull(lruCache1.remove(key1));
        lruCache1.put(key1, value1);
        assertEquals(value1, lruCache1.get(key1));
        assertEquals(value1, lruCache1.remove(key1));
        assertNull(lruCache1.get(key1));
        assertNull(lruCache1.remove(key1));
    }
}

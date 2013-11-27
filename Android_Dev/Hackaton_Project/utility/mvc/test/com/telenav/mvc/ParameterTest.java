/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestParameter.java
 *
 */
package com.telenav.mvc;

import java.util.Vector;

import junit.framework.TestCase;

/**
 *@author zhdong@telenav.cn
 *@date 2011-5-24
 */
public class ParameterTest extends TestCase
{
    Integer K1 = new Integer(1);

    Integer K2 = new Integer(2);

    Integer K3 = new Integer(3);

    Parameter parameter;
    protected void setUp() throws Exception
    {
        parameter = new Parameter();
        super.setUp();
    }
    
    public void testAdd()
    {
        Parameter toAdd = new Parameter();
        toAdd.put(K1, "1");
        toAdd.put(K2, "2");

        parameter.put(K3, "3");

        parameter.add(toAdd);

        assertEquals("1", parameter.get(K1));
        assertEquals("2", parameter.get(K2));
        assertEquals("3", parameter.get(K3));
        
        parameter.add(null);
        assertEquals("1", parameter.get(K1));
        assertEquals("2", parameter.get(K2));
        assertEquals("3", parameter.get(K3));
    }
    
    public void testFetch()
    {
        parameter.put(K1, "1");
        parameter.put(K2, "2");
        parameter.put(K3, "3");


        assertEquals("2", parameter.fetch(K2));
        assertEquals(null, parameter.fetch(K2));
        assertEquals(null, parameter.fetch(null));

    }
    
    public void testBool()
    {
        parameter.put(K1, true);

        assertEquals(true, parameter.getBool(K1));
        assertEquals(false, parameter.getBool(K2));
    }

    public void testOpt()
    {
        assertEquals("test", parameter.optString(K1, "test"));
        assertEquals("", parameter.optString(K1));
    }
    
    public void testGet()
    {
        parameter.put(K1, "1");
        parameter.put(K2, "2");
        
        assertEquals("1", parameter.get(K1));
        assertNull(parameter.get(null));
    }
    
    public void testOptBool()
    {
        parameter.put(K1, true);
        
        assertTrue(parameter.optBool(K1, false));
        assertFalse(parameter.getBool(K2));
        assertTrue(parameter.optBool(K2, true));
    }
    
    public void testFetchBool()
    {
        parameter.put(K1, true);
        
        assertTrue(parameter.fetchBool(K1));
        assertFalse(parameter.fetchBool(K1));
        assertFalse(parameter.fetchBool(K2));
    }
    
    public void testGetString()
    {
        parameter.put(K1, "1");
        
        assertEquals("1", parameter.getString(K1));
        assertEquals(null, parameter.getString(K2));
    }
    
    public void testOptString()
    {
        parameter.put(K1, "1");
        parameter.put(K2, null);
        
        assertEquals("1", parameter.optString(K1, "default value"));
        assertEquals("default value", parameter.optString(K2, "default value"));
    }
    
    public void testFetchString()
    {
        parameter.put(K1, "1");
        parameter.put(K2, null);
        
        assertEquals("1", parameter.fetchString(K1));
        assertEquals(null, parameter.fetchString(K2));
    }
    
    public void testGetInt()
    {
        parameter.put(K1, 1);
        
        assertEquals(1, parameter.getInt(K1));
        assertEquals(-1, parameter.getInt(K2));
    }
    
    public void testOptInt()
    {
        parameter.put(K1, 1);
        
        assertEquals(1, parameter.optInt(K1, 2));
        assertEquals(2, parameter.optInt(K2, 2));
    }
    
    public void testFetchInt()
    {
        parameter.put(K1, 1);
        assertEquals(1, parameter.fetchInt(K1));
        assertEquals(-1, parameter.fetchInt(K1));
        assertEquals(-1, parameter.fetchInt(K2));
    }
    
    public void testGetVector()
    {
        Vector vector = new Vector();
        vector.addElement("1");
        parameter.put(K1, vector);
        
        assertEquals(vector, parameter.getVector(K1));
        assertEquals(new Vector(), parameter.getVector(K2));
    }
    
    public void testFetchVector()
    {
        Vector vector = new Vector();
        vector.addElement("1");
        parameter.put(K1, vector);
        
        assertEquals(vector, parameter.fetchVector(K1));
        assertEquals(new Vector(), parameter.fetchVector(K1));
        assertEquals(new Vector(), parameter.fetchVector(K2));
    }
    
    public void testPutObject()
    {
        parameter.put(null, "1");
        assertEquals(null, parameter.getString(null));
        parameter.put(K1, "1");
        assertEquals("1", parameter.getString(K1));
        parameter.put(K1, null);
        assertEquals(null, parameter.getString(K1));
    }
    
    public void testPutInt()
    {
        parameter.put(null, "1");
        assertEquals(-1, parameter.getInt(null));
        parameter.put(K1, 1);
        assertEquals(1, parameter.getInt(K1));
    }
    
    public void testPutBool()
    {
        parameter.put(K1, null);
        assertEquals(false, parameter.getBool(K1));
        parameter.put(null, true);
        assertEquals(false, parameter.getBool(null));
        parameter.put(K1, true);
        assertEquals(true, parameter.getBool(K1));
    }
    
    public void testRemove()
    {
        parameter.put(null, "1");
        assertEquals(null, parameter.getString(null));
        parameter.remove(null);
        assertEquals(null, parameter.getString(null));
        
        parameter.put(K1, "1");
        assertEquals("1", parameter.getString(K1));
        parameter.remove(K1);
        assertEquals(null, parameter.getString(K1));
    }
    
    public void testRemoveAll()
    {
        parameter.put(K1, "1");
        assertEquals("1", parameter.getString(K1));
        parameter.put(K2, "2");
        assertEquals("2", parameter.getString(K2));
        parameter.removeAll();
        assertEquals(null, parameter.getString(K1));
        assertEquals(null, parameter.getString(K2));
    }
    
    public void testToString()
    {
        parameter.put(K1, "1");
        parameter.put(K2, true);
        parameter.put(K3, 21);
        String expected = "(3,21)(2,true)(1,1)";
        String actual = parameter.toString();
        assertEquals(expected, actual);
    }
}

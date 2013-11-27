/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LongIntArrayListTest.java
 *
 */
package com.telenav.datatypes;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *@author yning
 *@date 2011-7-4
 */
public class LongIntArrayListTest
{
    LongIntArrayList list;
    @Before
    public void setUp()
    {
        list = new LongIntArrayList(3);
    }
    
    @Test
    public void testAddElement()
    {
        list.addElement(1001);
        assertEquals(1, list.size());
        list.addElement(1002);
        assertEquals(2, list.size());
        list.addElement(1003);
        assertEquals(3, list.size());
        list.addElement(1004);
        assertEquals(4, list.size());
    }
    
    @Test
    public void testContains()
    {
        list.addElement(1001);
        Assert.assertTrue(list.contains(1001));
        list.addElement(1002);
        Assert.assertTrue(list.contains(1002));
        list.addElement(1003);
        Assert.assertTrue(list.contains(1003));
        list.addElement(1004);
        Assert.assertTrue(list.contains(1004));
        
        Assert.assertFalse(list.contains(1005));
        Assert.assertFalse(list.contains(1006));
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testElementAt()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1003);
        
        assertEquals(1002, list.elementAt(1));
        list.elementAt(6);
    }
    
    @Test
    public void testEnsureCapacity()
    {
        assertEquals(3, list.elementData.length);
        list.ensureCapacity(4);
        assertEquals(6, list.elementData.length);
        list.ensureCapacity(20);
        assertEquals(20, list.elementData.length);
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testFirstElement()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1003);
        
        assertEquals(1001, list.firstElement());
        
        //here should throw ArrayIndexOutOfBoundsException
        list.removeAllElements();
        list.firstElement();
    }
    
    @Test
    public void testIndexOf()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1003);
        list.addElement(1004);
        list.addElement(1001);
        
        assertEquals(0, list.indexOf(1001));
        assertEquals(1, list.indexOf(1002));
        assertEquals(2, list.indexOf(1003));
        assertEquals(3, list.indexOf(1004));
        assertEquals(0, list.indexOf(1001, 0));
        assertEquals(4, list.indexOf(1001, 1));
        assertEquals(-1, list.indexOf(1030));
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testInsertElementAt()
    {
        list.addElement(1001);
        list.addElement(1002);
        
        assertEquals(1, list.indexOf(1002));
        list.insertElementAt(1010, 1);
        assertEquals(1, list.indexOf(1010));
        assertEquals(2, list.indexOf(1002));
        
        list.insertElementAt(1999, 10);
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testLastElement()
    {
        list.addElement(1001);
        assertEquals(1001, list.lastElement());
        list.addElement(1002);
        assertEquals(1002, list.lastElement());
        
        list.removeAllElements();
        list.lastElement();
    }
    
    @Test
    public void testLastIndexOf()
    {
        assertEquals(-1, list.lastIndexOf(1001));
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1001);
        assertEquals(2, list.lastIndexOf(1001));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testLastIndexOfFromIndex()
    {
        assertEquals(-1, list.lastIndexOf(1001));
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1001);
        assertEquals(2, list.lastIndexOf(1001));
        
        list.lastIndexOf(1002, 10);
    }
    
    @Test
    public void testRemoveAllElements()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1001);
        assertEquals(3, list.size());
        list.removeAllElements();
        assertEquals(0, list.size());
    }
    
    @Test
    public void testRemoveElement()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1001);
        
        assertTrue(list.removeElement(1001));
        assertEquals(0, list.indexOf(1002));
        assertEquals(1, list.indexOf(1001));
        
        assertFalse(list.removeElement(1003));
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexNegative()
    {
        list.removeElementAt(-1);
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBound()
    {
        list.removeElementAt(1);
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testRemoveElementAt()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1003);
        list.addElement(1004);
        list.addElement(1005);
        list.addElement(1006);
        
        list.removeElementAt(2);
        //all elements behind 2 will move one index ahead
        assertEquals(1004, list.elementAt(2));
        assertEquals(1005, list.elementAt(3));
        assertEquals(1006, list.elementAt(4));
        
        //because list only have 5 elements.
        //below call will throw ArrayIndexOutOfBoundsException
        list.elementAt(5);
    }
    
    @Test
    public void testSetElementAt()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1003);
        list.addElement(1004);
        list.addElement(1005);
        list.addElement(1006);
        
        assertEquals(1004, list.elementAt(3));
        assertEquals(1005, list.elementAt(4));
        assertEquals(1006, list.elementAt(5));
        
        list.setElementAt(1999, 3);
        list.setElementAt(2000, 4);
        list.setElementAt(2001, 5);
        
        assertEquals(1999, list.elementAt(3));
        assertEquals(2000, list.elementAt(4));
        assertEquals(2001, list.elementAt(5));
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testSetElementAtIndexOutOfBound()
    {
        list.addElement(1001);
        list.addElement(1002);
        list.addElement(1003);
        list.addElement(1004);
        list.addElement(1005);
        list.addElement(1006);
        
        list.setElementAt(1999, 6);
    }
    
    @Test
    public void testSize()
    {
        list.addElement(1001);
        assertEquals(1, list.size());
        list.addElement(1002);
        assertEquals(2, list.size());
        list.addElement(1003);
        assertEquals(3, list.size());
        list.addElement(1004);
        assertEquals(4, list.size());
    }
}

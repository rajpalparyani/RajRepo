/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestLongIntVector.java
 *
 */
package com.telenav.cache.intcache;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-7
 */
public class LongIntVectorTest extends TestCase
{
    public void testAddElement()
    {
        LongIntVector vector = new LongIntVector(2);
        vector.addElement(1001);
        assertEquals(1, vector.size());
        vector.addElement(1002);
        assertEquals(2, vector.size());
        //the initial size of vector is 2. but it can increase it by itself.
        vector.addElement(1003);
        assertEquals(3, vector.size());
    }
    
    public void testContains()
    {
        LongIntVector vector = new LongIntVector(2);
        assertFalse(vector.contains(1001));
        
        vector.addElement(1001);
        assertTrue(vector.contains(1001));
        
        vector.removeElement(1001);
        assertFalse(vector.contains(1001));
    }
    
    public void testElementAt()
    {
        LongIntVector vector = new LongIntVector(2);
        try
        {
            vector.elementAt(0);
        }
        catch(Exception e)
        {
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
        
        vector.addElement(1002);
        assertEquals(1002, vector.elementAt(0));
    }
    
    public void testEnsureCapacity()
    {
        LongIntVector vector = new LongIntVector(2);
        
        vector.addElement(1002);
        vector.addElement(1003);
        assertEquals(2, vector.elementData.length);
        vector.ensureCapacity(3);
        assertEquals(4, vector.elementData.length);
        vector.ensureCapacity(10);
        assertEquals(10, vector.elementData.length);
    }
    
    public void testFirstElement()
    {
        LongIntVector vector = new LongIntVector(2);

        try
        {
            vector.firstElement();
        }
        catch(Exception e)
        {
            assertTrue(e instanceof ArrayIndexOutOfBoundsException);
        }
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        assertEquals(1002, vector.firstElement());
    }
    
    public void testIndexOfFromZero()
    {
        LongIntVector vector = new LongIntVector(2);

        vector.addElement(1002);
        vector.addElement(1003);
        
        assertEquals(1, vector.indexOf(1003));
        assertEquals(-1, vector.indexOf(1004));
    }
    
    public void testIndexOfFromSpecialIndex()
    {
        LongIntVector vector = new LongIntVector(2);

        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        assertEquals(1, vector.indexOf(1003, 0));
        assertEquals(3, vector.indexOf(1003, 2));
        assertEquals(5, vector.indexOf(1003, 4));
    }
    
    public void testInsertElementAt()
    {
        LongIntVector vector = new LongIntVector(2);

        try
        {
            vector.insertElementAt(1001, 1);
        }
        catch(Exception e)
        {
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
        
        vector.addElement(1002);
        vector.addElement(1002);
        vector.addElement(1002);
        vector.addElement(1002);
        vector.addElement(1002);
        vector.addElement(1002);
        
        vector.insertElementAt(1003, 3);
        
        assertEquals(7, vector.size());
        assertEquals(1003, vector.elementAt(3));
    }
    
    public void testLastElement()
    {
        LongIntVector vector = new LongIntVector(2);

        try
        {
            vector.lastElement();
        }
        catch(Exception e)
        {
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
        
        vector.addElement(1002);
        assertEquals(1002, vector.lastElement());
        vector.addElement(1003);
        assertEquals(1003, vector.lastElement());
        vector.addElement(1004);
        assertEquals(1004, vector.lastElement());
        vector.addElement(1005);
        assertEquals(1005, vector.lastElement());
        vector.addElement(1006);
        assertEquals(1006, vector.lastElement());
        vector.addElement(1007);
        assertEquals(1007, vector.lastElement());
    }
    
    public void testLastIndexOfFromEnding()
    {
        LongIntVector vector = new LongIntVector(2);

        try
        {
            vector.lastIndexOf(0);
        }
        catch(Exception e)
        {
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        assertEquals(5, vector.lastIndexOf(1003));
        assertEquals(4, vector.lastIndexOf(1002));
    }
    
    public void testLastIndexOfFromSpecialIndex()
    {
        LongIntVector vector = new LongIntVector(2);

        try
        {
            vector.lastIndexOf(1001, 1);
        }
        catch(Exception e)
        {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        assertEquals(3, vector.lastIndexOf(1003, 3));
        assertEquals(2, vector.lastIndexOf(1002, 3));
        
        assertEquals(1, vector.lastIndexOf(1003, 1));
        assertEquals(0, vector.lastIndexOf(1002, 1));
    }
    
    public void testRemoveAllElements()
    {
        LongIntVector vector = new LongIntVector(2);

        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        vector.addElement(1002);
        vector.addElement(1003);
        
        assertEquals(6, vector.size());
        vector.removeAllElements();
        assertEquals(0, vector.size());
    }
    
    public void testRemoveElement()
    {
        LongIntVector vector = new LongIntVector(2);
        vector.addElement(1002);
        vector.addElement(1003);
        
        assertEquals(2, vector.size());
        assertFalse(vector.removeElement(1001));
        assertEquals(2, vector.size());
        assertTrue(vector.removeElement(1002));
        assertEquals(1, vector.size());
        assertTrue(vector.removeElement(1003));
        assertEquals(0, vector.size());
    }
    
    public void testRemoveElementAt()
    {
        LongIntVector vector = new LongIntVector(2);
        
        try
        {
            vector.removeElementAt(0);
        }
        catch(Exception e)
        {
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
        
        try
        {
            vector.removeElementAt(-1);
        }
        catch(Exception e)
        {
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
        
        vector.addElement(1002);
        vector.addElement(1003);
        vector.addElement(1004);
        vector.addElement(1005);
        
        assertEquals(4, vector.size());
        assertEquals(1005, vector.removeElementAt(0));
        assertEquals(3, vector.size());
        assertEquals(1005, vector.removeElementAt(2));
        assertEquals(2, vector.size());
        assertEquals(1004, vector.removeElementAt(1));
        assertEquals(1, vector.size());
        assertEquals(1003, vector.removeElementAt(0));
        assertEquals(0, vector.size());
    }
    
    public void testSetElementAt()
    {
        LongIntVector vector = new LongIntVector(2);

        try
        {
            vector.setElementAt(1002, 0);
        }
        catch (Exception e)
        {
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
        
        vector.addElement(1003);
        vector.setElementAt(5555, 0);
        
        assertEquals(5555, vector.elementAt(0));
    }
}

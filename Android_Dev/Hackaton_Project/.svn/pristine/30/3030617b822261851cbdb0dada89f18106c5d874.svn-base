/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestQueue.java
 *
 */
package com.telenav.util;

import java.util.Vector;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-7
 */
public class QueueTest extends TestCase
{
    Vector result = new Vector();
    public void testIsEmpty()
    {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.push("1");
        assertFalse(queue.isEmpty());
        queue.pop();
        assertTrue(queue.isEmpty());
    }
    
    public void testPop1()
    {
        result.removeAllElements();
        final Queue queue = new Queue();
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                Object obj = queue.pop();
                if(obj != null)
                {
                    result.add(obj);
                }
            }
        };
        
        new Thread(runnable).start();
        assertTrue(queue.isEmpty());
        
        //push an object into queue. 
        //This object will be removed immediately by previous waiting pop.
        queue.push("1");
        
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertTrue(queue.isEmpty());
        assertEquals(1, result.size());
        assertEquals("1", result.elementAt(0));
    }
    
    public void testPop2()
    {
        result.removeAllElements();
        final Queue queue = new Queue();
        
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                Object obj = queue.pop(500);
                if(obj != null)
                {
                    result.add(obj);
                }
            }
        };
        
        //*********************************************
        //first test: should return object successfully.
        new Thread(runnable).start();
        assertTrue(queue.isEmpty());
        
        queue.push("1");
        
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertTrue(queue.isEmpty());
        assertEquals(1, result.size());
        assertEquals("1", result.elementAt(0));
        
        //*********************************************
        //second test: should return object failed.
        result.removeAllElements();
        new Thread(runnable).start();
        assertTrue(queue.isEmpty());
        
        try
        {
            Thread.sleep(1500); //timeout for sure.
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        queue.push("1");
        
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals(0, result.size());
    }
    
    public void testPush()
    {
        Queue queue = new Queue();
        assertEquals(0, queue.size());
        
        queue.push("23");
        assertEquals(1, queue.size());
        assertEquals("23", queue.pop());
    }
    
    public void testReset()
    {
        Queue queue = new Queue();
        queue.push("1");
        queue.push("2");
        queue.push("3");
        queue.push("4");
        queue.push("5");
        
        assertEquals(5, queue.size());
        
        queue.reset();
        assertEquals(0, queue.size());
    }
    
    public void testSize()  
    {
        Queue queue = new Queue();
        queue.push("1");
        queue.push("2");
        queue.push("3");
        queue.push("4");
        queue.push("5");
        
        assertEquals(5, queue.size());
        
        queue.reset();
        assertEquals(0, queue.size());
    }
}

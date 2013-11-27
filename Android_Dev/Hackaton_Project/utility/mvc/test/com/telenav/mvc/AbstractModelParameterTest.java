/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractModelParameterTest.java
 *
 */
package com.telenav.mvc;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-27
 */
public class AbstractModelParameterTest extends TestCase
{
    AbstractModelParameter parameter;
    protected void setUp() throws Exception
    {
        parameter = new AbstractModelParameter()
        {
            
        };
        super.setUp();
    }
    
    public void testGetState()
    {
        parameter.setState(9);
        assertEquals(9, parameter.getState());
    }
    
    public void testSetState()
    {
        parameter.setState(9);
        assertEquals(9, parameter.getState());
    }
    
    public void testGetPreState()
    {
        parameter.setState(6);
        parameter.preState = 7;
        assertEquals(7, parameter.getPreState());
    }
    
    public void testActivate()
    {
        parameter.deactivate();
        assertFalse(parameter.isActivated());
        
        parameter.activate();
        assertTrue(parameter.isActivated());
    }
    
    public void testActivateWithState()
    {
        parameter.setState(5);
        
        parameter.deactivate();
        assertFalse(parameter.isActivated());
        
        parameter.activate(9);
        assertTrue(parameter.isActivated());
        assertEquals(9, parameter.getState());
    }
    
    public void testDeactivate()
    {
        assertTrue(parameter.isActivated());
        parameter.deactivate();
        assertFalse(parameter.isActivated());
    }
    
    public void testIsActivated()
    {
        assertTrue(parameter.isActivated());
        parameter.deactivate();
        assertFalse(parameter.isActivated());
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractModelTest.java
 *
 */
package com.telenav.mvc;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-27
 */
public class AbstractModelTest extends TestCase
{
    Integer K1 = new Integer(1);
    Integer K2 = new Integer(2);
    Integer K3 = new Integer(3);
    
    AbstractController controller;
    AbstractModel model;
    
    protected void setUp() throws Exception
    {
        MvcAssert.init();
        
        controller = MvcControllerHelper.getMockController();
        controller.init();
        model = controller.getModel();
        
        super.setUp();
    }
    
    public void testResume()
    {
        MvcAssert.startRecord();
        model.resume();
        MvcAssert.stopRecord();
        
        MvcAssert.assertModelResume(model);
        MvcAssert.assertModelActivate(model, true);
    }
    
    public void testAddListener()
    {
        model.listeners.removeAllElements();
        assertFalse(model.listeners.contains(controller));
        model.addListener(controller);
        assertTrue(model.listeners.contains(controller));
    }
    
    public void testChangeState() 
    {
        model.state = 5;
        
        model.changeState(8);
        assertEquals(5, model.preState);
        assertEquals(8, model.state);
    }
    
    public void testPostModelEvent()
    {
        MvcAssert.startRecord();
        model.postModelEvent(1);
        MvcAssert.stopRecord();
        
        MvcAssert.assertModelEvent(model, 1);
    }
    
    public void testRelease()
    {
        MvcAssert.startRecord();
        model.release();
        MvcAssert.stopRecord();
        
        MvcAssert.assertModelRelease(model);
        assertFalse(model.isActivated());
        assertEquals(0, model.listeners.size());
    }
    
    public void testActivate()
    {
        /*MvcAssert.startRecord();
        model.activate(true);
        MvcAssert.stopRecord();
        
        MvcAssert.assertModelActivate(model, true);
        assertTrue(model.isActivated());
        MvcAssert.assertModelEvent(model, ITriggerConstants.EVENT_MODEL_STATE_CHANGE);*/
    }
    
    public void testDeactivate()
    {
        MvcAssert.startRecord();
        model.deactivate();
        MvcAssert.stopRecord();
        
        MvcAssert.assertModelDeactive(model);
        assertFalse(model.isActivated());
    }
    
    public void testXdoAction()
    {
        MvcAssert.startRecord();
        model.xdoAction(1);
        MvcAssert.stopRecord();
        
        MvcAssert.assertModelDoAction(model, 1);
    }
    
    public void testFetch()
    {
        assertNull(model.fetch(null));
        assertNull(model.fetch(K1));
        model.put(K1, "1");
        assertEquals("1", model.fetch(K1));
        assertNull(model.fetch(K1));
    }
    
    public void testPut()
    {
        model.put(null, "12");
        assertNull(model.get(null));
        model.put(K1, "1");
        assertEquals("1", model.get(K1));
    }
    
    public void testGet()
    {
        model.put(K1, "1");
        model.put(K2, 2);
        model.put(K3, true);
        
        assertNull(model.get(null));
        assertEquals("1", model.get(K1));
        assertEquals(2, model.getInt(K2));
        assertEquals(true, model.getBool(K3));
    }
}

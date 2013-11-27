/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractMvcMonitorTest.java
 *
 */
package com.telenav.mvc;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-27
 */
public class AbstractMvcMonitorTest extends TestCase
{
    AbstractMvcMonitor monitor;
    protected void setUp() throws Exception
    {
        MvcAssert.init();
        monitor = new AbstractMvcMonitorMock();
        super.setUp();
    }
    
    public void testGetCurrentController()
    {
        AbstractController controller = MvcControllerHelper.getMockController();
        controller.init();
        
        assertEquals(controller, monitor.getCurrentController());
    }
    
    public void testGetCurrentModel()
    {
        AbstractController controller = MvcControllerHelper.getMockController();
        controller.init();
        AbstractModel model = controller.getModel();
        assertEquals(model, monitor.getCurrentModel());
    }
    
    public void testGetCurrentView()
    {
        AbstractController controller = MvcControllerHelper.getMockController();
        controller.init();
        AbstractView view = controller.view;
        assertEquals(view, monitor.getCurrentView());
    }
    
    public void testGetMvcUiThreadHelper()
    {
        MvcContext.getInstance().init(null);
        assertNull(monitor.getMvcUiThreadHelper());
        MvcAssert.init();
        assertNotNull(monitor.getMvcUiThreadHelper());
    }
    
    public void testHandleViewEvent()
    {
        AbstractController controller = MvcControllerHelper.getMockController();
        controller.init();
        AbstractView view = controller.view;
        
        MvcAssert.startRecord();
        monitor.handleViewEvent(view, 1);
        monitor.handleViewEvent(view, ITriggerConstants.CMD_NONE);
        MvcAssert.stopRecord();
        
        MvcAssert.assertViewHandleViewEvent(view, 1, true);
        MvcAssert.assertViewHandleViewEvent(view, ITriggerConstants.CMD_NONE, false);
    }
}

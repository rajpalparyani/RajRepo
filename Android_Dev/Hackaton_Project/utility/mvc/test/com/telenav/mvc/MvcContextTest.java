/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MvcContextTest.java
 *
 */
package com.telenav.mvc;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-27
 */
public class MvcContextTest extends TestCase
{
    MvcContext mvcContext;
    protected void setUp() throws Exception
    {
        mvcContext = MvcContext.getInstance();
        super.setUp();
    }
    
    public void testInit()
    {
        mvcContext.init(null);
        assertNull(mvcContext.getMvcUiThreadHelper());
        MvcUiThreadHelperMock uiThreadHelper = new MvcUiThreadHelperMock();
        mvcContext.init(uiThreadHelper);
        assertEquals(uiThreadHelper, mvcContext.getMvcUiThreadHelper());
        mvcContext.init(null);
        assertNull(mvcContext.getMvcUiThreadHelper());
    }
    
    public void testGetMvcUiThreadHelper()
    {
        mvcContext.init(null);
        assertNull(mvcContext.getMvcUiThreadHelper());
        MvcUiThreadHelperMock uiThreadHelper = new MvcUiThreadHelperMock();
        mvcContext.init(uiThreadHelper);
        assertEquals(uiThreadHelper, mvcContext.getMvcUiThreadHelper());
        mvcContext.init(null);
        assertNull(mvcContext.getMvcUiThreadHelper());
    }
    
    public void testAddMonitor()
    {
        mvcContext.clearMonitors();
        assertEquals(0, mvcContext.monitors.size());
        mvcContext.addMonitor(null);
        assertEquals(0, mvcContext.monitors.size());
        AbstractMvcMonitor monitor = new AbstractMvcMonitorMock();
        mvcContext.addMonitor(monitor);
        assertEquals(1, mvcContext.monitors.size());
    }
    
    public void testRemoveMonitor() 
    {
        mvcContext.clearMonitors();
        assertEquals(0, mvcContext.monitors.size());
        AbstractMvcMonitor monitor = new AbstractMvcMonitorMock();
        mvcContext.addMonitor(monitor);
        assertEquals(1, mvcContext.monitors.size());
        
        mvcContext.removeMonitor(null);
        assertEquals(1, mvcContext.monitors.size());
        
        mvcContext.removeMonitor(monitor);
        assertEquals(0, mvcContext.monitors.size());
    }
    
    public void testClearMonitors()
    {
        mvcContext.clearMonitors();
        assertEquals(0, mvcContext.monitors.size());
        
        AbstractMvcMonitor monitor = new AbstractMvcMonitorMock();
        mvcContext.addMonitor(monitor);
        assertEquals(1, mvcContext.monitors.size());
        
        monitor = new AbstractMvcMonitorMock();
        mvcContext.addMonitor(monitor);
        assertEquals(2, mvcContext.monitors.size());
        
        monitor = new AbstractMvcMonitorMock();
        mvcContext.addMonitor(monitor);
        assertEquals(3, mvcContext.monitors.size());
        
        mvcContext.clearMonitors();
        assertEquals(0, mvcContext.monitors.size());
    }
}

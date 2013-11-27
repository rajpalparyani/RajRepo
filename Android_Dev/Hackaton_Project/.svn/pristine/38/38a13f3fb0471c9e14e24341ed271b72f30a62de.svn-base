/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractViewTest.java
 *
 */
package com.telenav.mvc;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-27
 */
public class AbstractViewTest extends TestCase
{
    AbstractView view;
    public static final int STATE_TRANSIENT = (IStateConstants.STATE_USER_BASE + 1) | IStateConstants.MASK_STATE_TRANSIENT;
    public static final int STATE_STABLE = IStateConstants.STATE_USER_BASE + 2;
    public static final int COMMAND_NOT_NONE = ITriggerConstants.CMD_USER_BASE + 1;
    public static final int COMMAND_NONE = ITriggerConstants.CMD_NONE;
    public static final int EVENT_MODEL_1 = ITriggerConstants.EVENT_MODEL_USER_BASE + 1;
    
    protected void setUp() throws Exception
    {
        MvcAssert.init();
        AbstractController controller = MvcControllerHelper.getMockController();
        controller.init();
        view = controller.view;
        super.setUp();
    }
    
    public void testIsShownTransientView()
    {
        assertTrue("state is not a transient state", view.isShownTransientView(STATE_TRANSIENT));
        assertFalse("state should be a stable state", view.isShownTransientView(STATE_STABLE));
    }
    
    public void testIsTransientState()
    {
        assertTrue("state is not a transient state", AbstractView.isTransientState(STATE_TRANSIENT));
        assertFalse("state should be a stable state", AbstractView.isTransientState(STATE_STABLE));
    }
    
    public void testSetModel()
    {
        AbstractModel model = MvcModelHelper.getMockModel();
        view.setModel(model);
        assertEquals(model, view.model);
    }
    
    public void testListener()
    {
        AbstractController anotherController = MvcControllerHelper.getMockController();
        int size = view.listeners.size();
        view.addListener(anotherController);
        int sizeAfterAdd = view.listeners.size();
        assertEquals(size + 1, sizeAfterAdd);
        
        view.addListener(anotherController);
        sizeAfterAdd = view.listeners.size();
        assertEquals(size + 1, sizeAfterAdd);
    }
    
    public void testPrepareModelData()
    {
        assertTrue(view.prepareModelData(STATE_STABLE, COMMAND_NOT_NONE));
    }
    
    public void testHandleViewEvent_A()
    {
        MvcAssert.startRecord();
        assertTrue(view.handleViewEvent(COMMAND_NOT_NONE));
        assertFalse(view.handleViewEvent(COMMAND_NONE));
        MvcAssert.stopRecord();
        
        MvcAssert.assertViewHandleViewEvent(view, COMMAND_NOT_NONE, true);
        MvcAssert.assertViewHandleViewEvent(view, COMMAND_NONE, false);
    }
    
    public void testHandleViewEvent_B()
    {
        view = new AbstractViewMock()
        {
            protected boolean prepareModelData(int state, int commandId)
            {
                return false;
            }
        };
        
        AbstractModel model = MvcModelHelper.getMockModel();
        MvcControllerHelper.getMockController(view, model);

        MvcAssert.startRecord();
        assertTrue(view.handleViewEvent(COMMAND_NOT_NONE));
        MvcAssert.stopRecord();

        MvcAssert.assertViewHandleViewEvent(view, COMMAND_NOT_NONE, true);
    }
    
    public void testShowTransientView()
    {
        assertFalse(view.showTransientView(STATE_TRANSIENT));
    }
    
    public void testShowView()
    {
        assertFalse(view.showTransientView(STATE_STABLE));
    }
    
    public void testXPopAllViews()
    {
        MvcAssert.startRecord();
        view.xpopAllViews();
        MvcAssert.stopRecord();
        
        MvcAssert.assertViewPopAllViews(view);
    }
    
    public void testHandleModelEvent()
    {
        AbstractModelParameter model = view.model;
        
        assertFalse(view.handleModelEvent(EVENT_MODEL_1));
        
        model.setState(STATE_TRANSIENT);
        MvcAssert.startRecord();
        assertFalse(view.handleModelEvent(ITriggerConstants.EVENT_MODEL_STATE_CHANGE));
        MvcAssert.stopRecord();
        MvcAssert.assertViewShowTransientView(view, STATE_TRANSIENT, 0);
        
        model.setState(STATE_STABLE);
        MvcAssert.startRecord();
        assertFalse(view.handleModelEvent(ITriggerConstants.EVENT_MODEL_STATE_CHANGE));
        MvcAssert.stopRecord();
        MvcAssert.assertViewShowView(view, STATE_STABLE, 0);
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractControllerTest.java
 *
 */
package com.telenav.mvc;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-27
 */
public class AbstractControllerTest extends TestCase
{
    public static final int STATE_1_TRANSIENT = IStateConstants.STATE_USER_BASE + 1 | IStateConstants.MASK_STATE_TRANSIENT;

    public static final int STATE_2 = IStateConstants.STATE_USER_BASE + 2;

    public static final int STATE_3 = IStateConstants.STATE_USER_BASE + 3;

    public static final int STATE_4 = IStateConstants.STATE_USER_BASE + 4;

    public static final int STATE_5_TRANSIENT = IStateConstants.STATE_USER_BASE + 5 | IStateConstants.MASK_STATE_TRANSIENT;

    public static final int EVENT_CONTROLLER_E1 = ITriggerConstants.EVENT_CONTROLLER_USER_BASE + 1;

    // don't exist this event in state table. For unit test.
    public static final int EVENT_CONTROLLER_E2 = ITriggerConstants.EVENT_CONTROLLER_USER_BASE + 2;

    public static final int COMMAND_1 = ITriggerConstants.CMD_USER_BASE + 1;

    public static final int COMMAND_2 = ITriggerConstants.CMD_USER_BASE + 2;

    public static final int EVENT_MODEL_E1 = ITriggerConstants.EVENT_MODEL_USER_BASE + 1;

    public static final int EVENT_MODEL_E2 = ITriggerConstants.EVENT_MODEL_USER_BASE + 2;

    public static final int ACTION_1 = IActionConstants.ACTION_USER_BASE + 1;

    int[][] state_table =
    {
    { IStateConstants.STATE_VIRGIN, ITriggerConstants.EVENT_CONTROLLER_START, STATE_1_TRANSIENT, IActionConstants.ACTION_NONE },
    { IStateConstants.STATE_ANY, EVENT_CONTROLLER_E1, STATE_2, IActionConstants.ACTION_NONE },
    { IStateConstants.STATE_ANY, COMMAND_1, STATE_3, IActionConstants.ACTION_NONE },
    { IStateConstants.STATE_ANY, COMMAND_2, IStateConstants.STATE_VIRGIN, IActionConstants.ACTION_NONE },
    { IStateConstants.STATE_ANY, EVENT_MODEL_E1, STATE_3, ACTION_1 },
    { IStateConstants.STATE_ANY, EVENT_MODEL_E2, STATE_5_TRANSIENT, IActionConstants.ACTION_NONE }, };

    protected Integer KEY1 = new Integer(1);

    protected Integer KEY2 = new Integer(2);

    AbstractControllerMock mockController;
    protected void setUp() throws Exception
    {
        MvcAssert.init();
        String name = this.getName();
        if(!"testInit".equals(name) && !"testInitWithParam".equals(name))
        {
            mockController = MvcControllerHelper.getMockController();
            mockController.setStateMachine(state_table);
            mockController.init();
        }
        super.setUp();
    }

    public void testInit()
    {
        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.init();
        MvcAssert.startRecord();
        AbstractControllerMock mockController = MvcControllerHelper.getMockController();
        MvcControllerHelper.setSuperController(mockController, superController);
        mockController.setStateMachine(state_table);
        mockController.init();
        MvcAssert.stopRecord();

        AbstractModel model = mockController.model;
        AbstractView view = mockController.view;

        MvcAssert.assertControllerInit(mockController, ITriggerConstants.EVENT_CONTROLLER_START, null, new Boolean(true));
        MvcAssert.assertModelDeactive(superController.getModel());
        TestCase.assertNotNull(mockController.state_table);
        TestCase.assertNotNull(model);
        TestCase.assertNotNull(view);
        TestCase.assertTrue(view.listeners.contains(mockController));
        TestCase.assertEquals(view.model, model);
        TestCase.assertTrue(model.listeners.contains(mockController));
        TestCase.assertTrue(model.listeners.contains(view));
        TestCase.assertEquals(model.controller, mockController);
        MvcAssert.assertModelActivate(model, false);
        assertEquals(STATE_1_TRANSIENT, model.state);
        assertEquals(mockController, AbstractController.getCurrentController());
    }

    public void testActivate()
    {
        MvcAssert.startRecord();
        mockController.activate();
        MvcAssert.stopRecord();

        assertEquals(IStateConstants.STATE_VIRGIN, mockController.model.state);
        MvcAssert.assertModelActivate(mockController.model, true);
        MvcAssert.assertModelEvent(mockController.model, ITriggerConstants.EVENT_MODEL_STATE_CHANGE);
        assertTrue(mockController.getModel().isActivated());
        assertEquals(mockController, AbstractController.getCurrentController());
    }

    public void testActivateWithEvent()
    {
        AbstractModel model = mockController.model;
        AbstractView view = mockController.view;

        model.listeners.removeAllElements();
        MvcAssert.startRecord();
        mockController.activate(EVENT_CONTROLLER_E1, null);
        MvcAssert.stopRecord();

        assertTrue(model.listeners.contains(mockController));
        assertTrue(model.listeners.contains(view));
        MvcAssert.assertModelActivate(model, false);
        assertTrue(model.isActivated());
        assertEquals(mockController, AbstractController.getCurrentController());
        assertEquals(STATE_2, model.getState());
    }

    public void testInitWithParam()
    {
        Parameter param = new Parameter();
        param.put(KEY1, "1");
        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.init();
        MvcAssert.startRecord();
        AbstractControllerMock mockController = MvcControllerHelper.getMockController();
        MvcControllerHelper.setSuperController(mockController, superController);
        mockController.setStateMachine(state_table);
        mockController.init(param);
        MvcAssert.stopRecord();

        AbstractModel model = mockController.model;
        AbstractView view = mockController.view;
        MvcAssert.assertControllerInit(mockController, ITriggerConstants.EVENT_CONTROLLER_START, param, new Boolean(true));
        MvcAssert.assertModelDeactive(superController.getModel());
        TestCase.assertNotNull(mockController.state_table);
        TestCase.assertNotNull(model);
        TestCase.assertNotNull(view);
        TestCase.assertTrue(view.listeners.contains(mockController));
        TestCase.assertEquals(view.model, model);
        TestCase.assertTrue(model.listeners.contains(mockController));
        TestCase.assertTrue(model.listeners.contains(mockController.view));
        TestCase.assertEquals(model.controller, mockController);
        MvcAssert.assertModelActivate(model, false);
        assertEquals(STATE_1_TRANSIENT, model.state);
        assertEquals(mockController, AbstractController.getCurrentController());
        assertEquals("1", model.get(KEY1));
    }

    public void testHandleCommand_A()
    {
        AbstractModel model = mockController.model;

        model.deactivate();
        assertFalse(mockController.handleCommand(COMMAND_1));
        model.activate();
        assertTrue(mockController.handleCommand(COMMAND_1));
    }

    public void testHandleCommand_B()
    {
        AbstractView view = mockController.view;
        AbstractModel model = mockController.model;

        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.init();
        AbstractModel superModel = superController.getModel();
        MvcControllerHelper.setSuperController(mockController, superController);

        MvcAssert.startRecord();
        assertTrue(mockController.handleCommand(COMMAND_2));
        MvcAssert.stopRecord();

        MvcAssert.assertControllerPostStateChange(mockController, STATE_1_TRANSIENT, IStateConstants.STATE_VIRGIN);
        MvcAssert.assertModelDeactive(model);
        MvcAssert.assertViewPopAllViews(view);
        MvcAssert.assertModelActivate(superModel, true);
        assertEquals(superController, AbstractController.getCurrentController());
    }

    public void testHandleCommand_C()
    {
        AbstractView view = mockController.view;
        AbstractModel model = mockController.model;

        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.setStateMachine(state_table);
        superController.init();
        AbstractModel superModel = superController.getModel();
        MvcControllerHelper.setSuperController(mockController, superController);

        // enable controller back event
        mockController.postControllerEvent(EVENT_CONTROLLER_E1, null);
        MvcAssert.startRecord();
        assertTrue(mockController.handleCommand(COMMAND_1));
        MvcAssert.stopRecord();

        MvcAssert.assertModelDeactive(model);
        MvcAssert.assertViewPopAllViews(view);
        MvcAssert.assertControllerRelease(mockController);
        MvcAssert.assertModelRelease(model);
        MvcAssert.assertModelActivate(superModel, false);
        assertEquals(STATE_2, superModel.getState());
    }

    public void testHandleModelEvent_A()
    {
        AbstractModel model = mockController.model;

        model.deactivate();
        assertFalse(mockController.handleModelEvent(EVENT_MODEL_E1));
    }

    public void testHandleModelEvent_B()
    {
        AbstractView view = mockController.view;
        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.init();
        MvcControllerHelper.setSuperController(mockController, superController);
        AbstractModel superModel = superController.model;
        AbstractView superView = superController.view;

        assertTrue(mockController.handleModelEvent(ITriggerConstants.EVENT_MODEL_STATE_CHANGE));

        MvcAssert.startRecord();
        assertTrue(mockController.handleModelEvent(ITriggerConstants.EVENT_MODEL_RELEASE_ALL_PREVIOUS_MODULES));
        MvcAssert.stopRecord();

        MvcAssert.assertViewPopAllViews(view);
        MvcAssert.assertControllerRelease(superController);
        MvcAssert.assertViewPopAllViews(superView);
        MvcAssert.assertModelRelease(superModel);
    }

    public void testHandleModelEvent_C()
    {
        AbstractModel model = mockController.model;

        MvcAssert.startRecord();
        assertTrue(mockController.handleModelEvent(EVENT_MODEL_E1));
        MvcAssert.stopRecord();

        MvcAssert.assertModelDoAction(model, ACTION_1);
    }

    public void testGetCurrentController()
    {
        assertEquals(mockController, AbstractController.getCurrentController());
    }

    public void testHandleControllerEvent_A()
    {
        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.init();
        MvcControllerHelper.setSuperController(mockController, superController);

        MvcAssert.startRecord();
        assertFalse(mockController.handleControllerEvent(EVENT_CONTROLLER_E2, null));
        MvcAssert.stopRecord();

        MvcAssert.assertControllerRelease(mockController);
        MvcAssert.assertControllerRelease(superController);
    }

    public void testHandleControllerEvent_B()
    {
        AbstractModel model = mockController.model;

        MvcAssert.startRecord();
        assertTrue(mockController.handleControllerEvent(EVENT_CONTROLLER_E1, null));
        MvcAssert.stopRecord();

        MvcAssert.assertModelActivate(model, false);

        assertEquals(STATE_2, model.getState());
    }

    public void testXpostStateChange()
    {
        MvcAssert.startRecord();
        mockController.xpostStateChange(STATE_2, STATE_3);
        MvcAssert.stopRecord();

        MvcAssert.assertControllerPostStateChange(mockController, STATE_2, STATE_3);
    }

    public void testReleaseAll()
    {
        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.init();
        MvcControllerHelper.setSuperController(mockController, superController);

        MvcAssert.startRecord();
        mockController.releaseAll();
        MvcAssert.stopRecord();

        MvcAssert.assertControllerRelease(mockController);
        MvcAssert.assertControllerRelease(superController);
    }

    public void testGetModel()
    {
        AbstractModelMock model = MvcModelHelper.getMockModel();
        mockController.setModel(model);

        assertNotNull(mockController.getModel());
        assertEquals(model, mockController.getModel());
    }

    public void testBackToLastStableState()
    {
        AbstractModel model = mockController.model;

        mockController.handleControllerEvent(EVENT_CONTROLLER_E1, null);
        mockController.handleModelEvent(EVENT_MODEL_E2);

        assertEquals(STATE_5_TRANSIENT, model.getState());
        assertEquals(STATE_2, mockController.backToLastStableState());
        assertEquals(STATE_2, model.getState());
    }
    
    public void testControllerBackEventToString()
    {
        mockController.postControllerEvent(EVENT_CONTROLLER_E1, null);
        String actual = mockController.controllerBackEvent.toString();
        String expected = "controler event: 3002001null";
        assertEquals(expected, actual);
    }
    
    public void testGetSuperController()
    {
        AbstractControllerMock superController = MvcControllerHelper.getMockController();
        superController.init();
        MvcControllerHelper.setSuperController(mockController, superController);
        
        assertEquals(superController, mockController.getSuperController());
    }
}

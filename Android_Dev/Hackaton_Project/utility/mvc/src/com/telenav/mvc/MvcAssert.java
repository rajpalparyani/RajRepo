/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MvcTestHelper.java
 *
 */
package com.telenav.mvc;

import java.util.Vector;

import junit.framework.TestCase;

import com.telenav.mvc.AbstractMvcMonitorMock.ControllerRecord;
import com.telenav.mvc.AbstractMvcMonitorMock.ModelRecord;
import com.telenav.mvc.AbstractMvcMonitorMock.Record;
import com.telenav.mvc.AbstractMvcMonitorMock.ViewRecord;

/**
 *@author yning
 *@date 2011-6-20
 */
public class MvcAssert
{
    static AbstractMvcMonitorMock monitor;

    public static void init()
    {
        setMockMvcUiThreadHelper();
    }

    public static void startRecord()
    {
        reset();
        monitor = new AbstractMvcMonitorMock();
        MvcContext.getInstance().addMonitor(monitor);
    }

    public static void stopRecord()
    {
        if (monitor != null)
        {
            MvcContext.getInstance().removeMonitor(monitor);
        }
    }

    public static void reset()
    {
        if (monitor != null)
        {
            monitor.clear();
        }
    }

    public static void assertModelEvent(AbstractModel model, int modelEvent)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_POST_MODEL_EVENT.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_POST_MODEL_EVENT.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        int recordedEvent = modelRecord.event;
                        if (recordedModel == model && recordedEvent == modelEvent)
                        {
                            if (AbstractMvcMonitor.MODEL_START_POST_MODEL_EVENT.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.MODEL_EXIT_POST_MODEL_EVENT.equalsIgnoreCase(method))
                            {
                                isFoundEnd = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    public static void assertModelResume(AbstractModel model)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_RESUME.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_RESUME.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        if (recordedModel == model)
                        {
                            if (AbstractMvcMonitor.MODEL_START_RESUME.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.MODEL_EXIT_RESUME.equalsIgnoreCase(method))
                            {
                                isFoundEnd = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    public static void assertModelRelease(AbstractModel model)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_RELEASE.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_RELEASE.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        if (recordedModel == model)
                        {
                            if (AbstractMvcMonitor.MODEL_START_RELEASE.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.MODEL_EXIT_RELEASE.equalsIgnoreCase(method))
                            {
                                isFoundEnd = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    public static void assertModelActivate(AbstractModel model, boolean isUpdateView)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_ACTIVATE.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_ACTIVATE.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        int recordedEvent = modelRecord.event;
                        int expectedEvent = isUpdateView ? 1 : 0;
                        if (recordedModel == model && recordedEvent == expectedEvent)
                        {
                            if (AbstractMvcMonitor.MODEL_START_ACTIVATE.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.MODEL_EXIT_ACTIVATE.equalsIgnoreCase(method))
                            {
                                isFoundEnd = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue("isFoundStart = " + isFoundStart + ", isFoundEnd = " + isFoundEnd, isFoundStart && isFoundEnd);
    }

    public static void assertModelDeactive(AbstractModel model)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_DEACTIVATE.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_DEACTIVATE.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        if (recordedModel == model)
                        {
                            if (AbstractMvcMonitor.MODEL_START_DEACTIVATE.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.MODEL_EXIT_DEACTIVATE.equalsIgnoreCase(method))
                            {
                                isFoundEnd = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    public static void assertModelDoAction(AbstractModel model, int actionId)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_DO_ACTION.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_DO_ACTION.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        int recordedActionId = modelRecord.event;
                        if (recordedModel == model && recordedActionId == actionId)
                        {
                            if (AbstractMvcMonitor.MODEL_START_DO_ACTION.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.MODEL_EXIT_DO_ACTION.equalsIgnoreCase(method))
                            {
                                isFoundEnd = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    public static void assertModelFetch(AbstractModel model, int key, Object result)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_FETCH.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_FETCH.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        int recordedKey = modelRecord.event;
                        if (recordedModel == model && recordedKey == key)
                        {
                            if (AbstractMvcMonitor.MODEL_START_FETCH.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }

                            if (AbstractMvcMonitor.MODEL_EXIT_FETCH.equalsIgnoreCase(method))
                            {
                                Object recordedResult = modelRecord.returnResult;
                                if ((recordedResult == null && result == null) || (recordedResult != null && recordedResult.equals(result)))
                                {
                                    isFoundEnd = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    public static void assertModelPut(AbstractModel model, int key, Object value)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_PUT.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_PUT.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        int recordedKey = modelRecord.event;
                        Object recordedValue = modelRecord.arg;
                        if (recordedModel == model && recordedKey == key)
                        {
                            if((recordedValue == null && value == null) || (recordedValue != null && recordedValue.equals(value)))
                            {
                                if (AbstractMvcMonitor.MODEL_START_PUT.equalsIgnoreCase(method))
                                {
                                    isFoundStart = true;
                                }
                                else if (AbstractMvcMonitor.MODEL_EXIT_PUT.equalsIgnoreCase(method))
                                {
                                    isFoundEnd = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    public static void assertModelGet(AbstractModel model, int key, Object result)
    {
        boolean isFoundStart = false;
        boolean isFoundEnd = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ModelRecord)
                {
                    ModelRecord modelRecord = (ModelRecord) record;
                    String method = modelRecord.method;
                    if (AbstractMvcMonitor.MODEL_START_GET.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.MODEL_EXIT_GET.equalsIgnoreCase(method))
                    {
                        AbstractModel recordedModel = modelRecord.model;
                        int recordedKey = modelRecord.event;
                        if (recordedModel == model && recordedKey == key)
                        {
                            if (AbstractMvcMonitor.MODEL_START_GET.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.MODEL_EXIT_GET.equalsIgnoreCase(method))
                            {
                                Object recordedResult = modelRecord.returnResult;
                                if ((recordedResult == null && result == null) || (recordedResult != null && recordedResult.equals(result)))
                                {
                                    isFoundEnd = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        TestCase.assertTrue(isFoundStart && isFoundEnd);
    }

    /**
     * 
     * @param controller
     */
    public static void assertControllerInit(AbstractController controller, int controllerEvent, Parameter parameter, boolean result)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record) records.elementAt(i);
                if (record instanceof ControllerRecord)
                {
                    ControllerRecord controllerRecord = (ControllerRecord) record;
                    String method = controllerRecord.method;
                    if (AbstractMvcMonitor.CONTROLLER_START_INIT.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.CONTROLLER_EXIT_INIT.equalsIgnoreCase(method))
                    {
                        AbstractController recordedController = controllerRecord.controller;
                        int recordedControllerEvent = controllerRecord.event;
                        Object recordedParam = controllerRecord.param;
                        Object recordedResult = controllerRecord.returnResult;
                        if (recordedController == controller && recordedControllerEvent == controllerEvent)
                        {
                            if (isParametersEquals(parameter, recordedParam))
                            {
                                if (AbstractMvcMonitor.CONTROLLER_START_INIT.equalsIgnoreCase(method))
                                {
                                    isFoundStart = true;
                                }

                                if (AbstractMvcMonitor.CONTROLLER_EXIT_INIT.equalsIgnoreCase(method))
                                {
                                    if (recordedResult instanceof Boolean)
                                    {
                                        Boolean actual = (Boolean) recordedResult;
                                        if (result == actual.booleanValue())
                                        {
                                            isFoundExit = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }

    public static void assertControllerPostStateChange(AbstractController controller, int currentState, int nextState)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ControllerRecord)
                {
                    ControllerRecord controllerRecord = (ControllerRecord) record;
                    String method = controllerRecord.method;
                    if (AbstractMvcMonitor.CONTROLLER_START_POST_STATE_CHANGE.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.CONTROLLER_EXIT_POST_STATE_CHANGE.equalsIgnoreCase(method))
                    {
                        if (controllerRecord.controller == controller)
                        {
                            Object recordedParam = controllerRecord.param;
                            if (recordedParam instanceof Integer)
                            {
                                int recordedNextState = ((Integer) recordedParam).intValue();
                                if (nextState == recordedNextState)
                                {
                                    if (AbstractMvcMonitor.CONTROLLER_START_POST_STATE_CHANGE.equalsIgnoreCase(method))
                                    {
                                        isFoundStart = true;
                                    }
                                    else if (AbstractMvcMonitor.CONTROLLER_EXIT_POST_STATE_CHANGE.equalsIgnoreCase(method))
                                    {
                                        isFoundExit = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }

    public static void assertControllerRelease(AbstractController controller)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;
        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ControllerRecord)
                {
                    ControllerRecord controllerRecord = (ControllerRecord) record;
                    String method = controllerRecord.method;
                    if (AbstractMvcMonitor.CONTROLLER_START_RELEASE.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.CONTROLLER_EXIT_RELEASE.equalsIgnoreCase(method))
                    {
                        if (controllerRecord.controller == controller)
                        {
                            if (AbstractMvcMonitor.CONTROLLER_START_RELEASE.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.CONTROLLER_EXIT_RELEASE.equalsIgnoreCase(method))
                            {
                                isFoundExit = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }

    /**
     * view
     */
    public static void assertViewIsShowTransientView(AbstractView view, int state, boolean result)
    {
        boolean isFoundExit = false;

        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ViewRecord)
                {
                    ViewRecord viewRecord = (ViewRecord) record;
                    String method = viewRecord.method;

                    if (AbstractMvcMonitor.VIEW_EXIT_SHOW_TRANSIENT_VIEW.equalsIgnoreCase(method))
                    {
                        AbstractView recordedView = viewRecord.view;
                        int recordedState = viewRecord.state;
                        Object recordedResult = viewRecord.returnResult;

                        if(recordedView == view && recordedState == state)
                        {
                            if (recordedResult instanceof Boolean)
                            {
                                boolean recordedResultValue = ((Boolean) recordedResult).booleanValue();
                                if (recordedResultValue == result)
                                {
                                    isFoundExit = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundExit);
    }

    public static void assertViewPrepareModelData(AbstractView view, int state, int commandId, boolean result)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;

        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ViewRecord)
                {
                    ViewRecord viewRecord = (ViewRecord) record;
                    String method = viewRecord.method;

                    if (AbstractMvcMonitor.VIEW_START_PREPARE_MODEL_DATA.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.VIEW_EXIT_PREPARE_MODEL_DATA.equalsIgnoreCase(method))
                    {
                        AbstractView recordedView = viewRecord.view;
                        int recordedState = viewRecord.state;
                        int recordedCommandId = viewRecord.command;
                        Object recordedResult = viewRecord.returnResult;

                        if (recordedView == view && recordedState == state && recordedCommandId == commandId)
                        {
                            if (AbstractMvcMonitor.VIEW_START_PREPARE_MODEL_DATA.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if(AbstractMvcMonitor.VIEW_EXIT_PREPARE_MODEL_DATA.equalsIgnoreCase(method))
                            {
                                if (recordedResult instanceof Boolean)
                                {
                                    boolean recordedResultValue = ((Boolean) recordedResult).booleanValue();
                                    if (recordedResultValue == result)
                                    {
                                        isFoundExit = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }

    public static void assertViewHandleViewEvent(AbstractView view, int commandId, boolean result)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;

        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ViewRecord)
                {
                    ViewRecord viewRecord = (ViewRecord) record;
                    String method = viewRecord.method;

                    if (AbstractMvcMonitor.VIEW_START_HANDLE_VIEW_EVENT.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.VIEW_EXIT_HANDLE_VIEW_EVENT.equalsIgnoreCase(method))
                    {
                        AbstractView recordedView = viewRecord.view;
                        int recordedCommandId = viewRecord.command;
                        Object recordedResult = viewRecord.returnResult;

                        if (recordedView == view && recordedCommandId == commandId)
                        {
                            if (AbstractMvcMonitor.VIEW_START_HANDLE_VIEW_EVENT.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if(AbstractMvcMonitor.VIEW_EXIT_HANDLE_VIEW_EVENT.equalsIgnoreCase(method))
                            {
                                if (recordedResult instanceof Boolean)
                                {
                                    boolean recordedResultValue = ((Boolean) recordedResult).booleanValue();
                                    if (recordedResultValue == result)
                                    {
                                        isFoundExit = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }
    
    public static void assertViewShowTransientView(AbstractView view, int state, int command)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;

        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ViewRecord)
                {
                    ViewRecord viewRecord = (ViewRecord) record;
                    String method = viewRecord.method;

                    if (AbstractMvcMonitor.VIEW_START_SHOW_TRANSIENT_VIEW.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.VIEW_EXIT_SHOW_TRANSIENT_VIEW.equalsIgnoreCase(method))
                    {
                        AbstractView recordedView = viewRecord.view;
                        int recordedState = viewRecord.state;
                        int recordedCommandId = viewRecord.command;

                        if (recordedView == view && recordedState == state)
                        {
                            if (AbstractMvcMonitor.VIEW_START_SHOW_TRANSIENT_VIEW.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.VIEW_EXIT_SHOW_TRANSIENT_VIEW.equalsIgnoreCase(method))
                            {
                                if (recordedCommandId == command)
                                {
                                    isFoundExit = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }
    
    public static void assertViewShowView(AbstractView view, int state, int command)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;

        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ViewRecord)
                {
                    ViewRecord viewRecord = (ViewRecord) record;
                    String method = viewRecord.method;

                    if (AbstractMvcMonitor.VIEW_START_SHOW_VIEW.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.VIEW_EXIT_SHOW_VIEW.equalsIgnoreCase(method))
                    {
                        AbstractView recordedView = viewRecord.view;
                        int recordedState = viewRecord.state;
                        int recordedCommandId = viewRecord.command;

                        if (recordedView == view && recordedState == state)
                        {
                            if (AbstractMvcMonitor.VIEW_START_SHOW_VIEW.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.VIEW_EXIT_SHOW_VIEW.equalsIgnoreCase(method))
                            {
                                if (recordedCommandId == command)
                                {
                                    isFoundExit = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }
    
    public static void assertViewPopAllViews(AbstractView view)
    {
        boolean isFoundStart = false;
        boolean isFoundExit = false;

        if (monitor != null)
        {
            Vector records = monitor.getRecords();
            for (int i = 0; i < records.size(); i++)
            {
                Record record = (Record)records.elementAt(i);
                if (record instanceof ViewRecord)
                {
                    ViewRecord viewRecord = (ViewRecord) record;
                    String method = viewRecord.method;

                    if (AbstractMvcMonitor.VIEW_START_POP_ALL_VIEWS.equalsIgnoreCase(method)
                            || AbstractMvcMonitor.VIEW_EXIT_POP_ALL_VIEWS.equalsIgnoreCase(method))
                    {
                        AbstractView recordedView = viewRecord.view;

                        if(recordedView == view)
                        {
                            if (AbstractMvcMonitor.VIEW_START_POP_ALL_VIEWS.equalsIgnoreCase(method))
                            {
                                isFoundStart = true;
                            }
                            else if (AbstractMvcMonitor.VIEW_EXIT_POP_ALL_VIEWS.equalsIgnoreCase(method))
                            {
                                isFoundExit = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        TestCase.assertTrue(isFoundStart && isFoundExit);
    }
    
    public static void assertControllerEvent(AbstractController controller, int controllerEventId, Parameter param)
    {
        TestCase.assertEquals(controllerEventId, MvcControllerHelper.getControllerEventId(controller));
        TestCase.assertTrue(isParametersEquals(param, MvcControllerHelper.getControllerEventParam(controller)));
    }
    
    public static void assertControllerEventId(AbstractController controller, int controllerEventId)
    {
        TestCase.assertEquals(controllerEventId, MvcControllerHelper.getControllerEventId(controller));
    }
    
    static void setMockMvcUiThreadHelper()
    {
        MvcUiThreadHelperMock mockHelper = new MvcUiThreadHelperMock();
        MvcContext.getInstance().init(mockHelper);
    }
    
    public static boolean isParametersEquals(Parameter expected, Object actual)
    {
        if (expected == null && actual == null)
        {
            return true;
        }
        else if (actual == null || expected == null)
        {
            return false;
        }
        else // both actual and expected are not null
        {
            if (!(actual instanceof Parameter))
            {
                return false;
            }
            Parameter actualParam = (Parameter)actual;
            String paramString = expected.toString();
            int startIndex = paramString.indexOf("(");
            while (startIndex != -1)
            {
                int endIndex = paramString.indexOf(")", startIndex + 1);
                if (endIndex > startIndex)
                {
                    String subString = paramString.substring(startIndex + 1, endIndex);
                    int commaIndex = subString.indexOf(",");
                    if (commaIndex != -1)
                    {
                        String keyStr = subString.substring(0, commaIndex);
                        try
                        {
                            Integer key = Integer.valueOf(keyStr);
                            Object expectedValue = expected.get(key);
                            Object actualValue = actualParam.get(key);
                            if ((expectedValue == null && actualValue == null)
                                    || (expectedValue != null && expectedValue.equals(actualValue)))
                            {
                                // do nothing
                            }
                            else
                            {
                                return false; // when error happens, can't compare any more. return false.
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            return false;
                        }
                    }
                    startIndex = paramString.indexOf("(", endIndex + 1);
                }
                else
                {
                    return false;
                }
            }
            return true;
        }
    }
}

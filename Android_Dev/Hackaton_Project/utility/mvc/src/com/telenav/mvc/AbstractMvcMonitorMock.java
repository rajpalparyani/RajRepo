/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MockMvcMonitor.java
 *
 */
package com.telenav.mvc;

import java.util.Vector;

/**
 *@author yning
 *@date 2011-6-21
 */
public class AbstractMvcMonitorMock extends AbstractMvcMonitor
{
    private Vector records = new Vector();
    
    protected void watchController(String method, AbstractController controller, AbstractModelParameter model, int event, Object param,
            Object returnResult)
    {
        ControllerRecord record = new ControllerRecord();
        record.method = method;
        record.controller = controller;
        record.model = model;
        record.event = event;
        record.param = param;
        record.returnResult = returnResult;
        
        records.addElement(record);
        super.watchController(method, controller, model, event, param, returnResult);
    }
    
    protected void watchView(String method, AbstractView view, AbstractModelParameter model, int state, int command, Object returnResult)
    {
        ViewRecord record = new ViewRecord();
        record.method = method;
        record.view = view;
        record.model = model;
        record.state = state;
        record.command = command;
        record.returnResult = returnResult;
        
        records.addElement(record);
        super.watchView(method, view, model, state, command, returnResult);
    }
    
    protected void watchModel(String method, AbstractModel model, int event, Object arg, Object returnResult)
    {
        ModelRecord record = new ModelRecord();
        record.method = method;
        record.model = model;
        record.event = event;
        record.arg = arg;
        record.returnResult = returnResult;
        
        records.addElement(record);
        super.watchModel(method, model, event, arg, returnResult);
    }
    
    public void clear()
    {
        records.removeAllElements();
    }
    
    public Vector getRecords()
    {
        return records;
    }
    
    class Record
    {
        public String method;
    }
    
    class ViewRecord extends Record
    {
        public AbstractView view;
        public AbstractModelParameter model;
        public int state;
        public int command;
        public Object returnResult;
    }
    
    class ModelRecord extends Record
    {
        public AbstractModel model;
        public int event;
        public Object arg;
        public Object returnResult;
    }
    
    class ControllerRecord extends Record
    {
        public AbstractController controller;
        public AbstractModelParameter model;
        public int event;
        public Object param;
        public Object returnResult;
    }
}

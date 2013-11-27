/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnPrivateEvent.java
 *
 */
package com.telenav.tnui.core;

/**
 * Only send this ui event in ui framework.
 * <br />
 * This event is private for the outside of ui framework. The module layer can't retrieve this event.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-22
 */
public class TnPrivateEvent
{
    /**
     * The ui timer action event.
     */
    public final static int ACTION_TIMER = 1;
    
    /**
     * text change event for text field.
     */
    public final static int ACTION_TEXT_CHANGE = 2;
    
    /**
     * back key for text field.
     */
    public final static int ACTION_TEXTFIELD_BACK = 3;
    
    /**
     * The instance of timer action event.
     * <br />
     * This event will handle the ui text scrolling, progress bar etc...
     */
    public final static TnPrivateEvent EVENT_TIMER = new TnPrivateEvent(ACTION_TIMER);
    
    private int action;
    private Object data;
    
    /**
     * construct a private ui event.
     * 
     * @param action
     */
    public TnPrivateEvent(int action)
    {
        this.action = action;
    }
    
    /**
     * retrieve the action of this event.
     * 
     * @return
     */
    public int getAction()
    {
        return this.action;
    }
    
    /**
     * set the data
     * 
     * @param data
     */
    public void setData(Object data)
    {
        this.data = data;
    }
    
    /**
     * retrieve the data of this event.
     * 
     * @return
     */
    public Object getData()
    {
        return this.data;
    }
}

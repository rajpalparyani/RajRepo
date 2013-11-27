/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnUiEvent.java
 *
 */
package com.telenav.tnui.core;

/**
 * The ui event wrapper.
 * <br />
 * Retrieve different ui event according to the type.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-22
 */
public class TnUiEvent
{
    // used to recording user input (click, key in, drag ant etc)
    public static final String LOG_UI_EVENT               =   "marts_uiEvent";
    
    /**
     * {@link TnCommandEvent}
     */
    public final static int TYPE_COMMAND_EVENT = 1;

    /**
     * {@link TnKeyEvent}
     */
    public final static int TYPE_KEY_EVENT = 2;

    /**
     * {@link TnMotionEvent}
     */
    public final static int TYPE_TOUCH_EVENT = 3;

    /**
     * {@link TnMotionEvent}
     */
    public final static int TYPE_TRACKBALL_EVENT = 4;

    /**
     * {@link TnPrivateEvent}
     */
    public final static int TYPE_PRIVATE_EVENT = 5;
    
    public final static int TYPE_GESTURE_SCALE = 6;
    
    private int eventType;
    private AbstractTnComponent tnComponent;
    private TnKeyEvent keyEvent;
    private TnMotionEvent motionEvent;
    private TnCommandEvent commandEvent;
    private TnPrivateEvent privateEvent;
    
    /**
     * construct the ui event.
     * 
     * @param type
     * @param tnComponent
     */
    public TnUiEvent(int type, AbstractTnComponent tnComponent)
    {
        this.eventType = type;
        this.tnComponent = tnComponent;
    }
    
    /**
     * retrieve the ui event type.
     * 
     * @return int
     */
    public int getType()
    {
        return eventType;
    }
    
    /**
     * retrieve the ui component which trigger the ui event.
     * 
     * @return {@link AbstractTnComponent}
     */
    public AbstractTnComponent getComponent()
    {
        return tnComponent;
    }
    
    /**
     * set the ui component which trigger the ui event.
     * 
     * @param component {@link AbstractTnComponent}
     */
    public void setComponent(AbstractTnComponent component)
    {
        this.tnComponent = component;
    }
    
    /**
     * retrieve the key event.
     * 
     * @return {@link TnKeyEvent}
     */
    public TnKeyEvent getKeyEvent()
    {
        return keyEvent;
    }
    
    /**
     * set key event.
     * 
     * @param keyEvent
     */
    public void setKeyEvent(TnKeyEvent keyEvent)
    {
        this.keyEvent = keyEvent;
    }
    
    /**
     * retrieve the motion event.
     * 
     * @return {@link TnMotionEvent}
     */
    public TnMotionEvent getMotionEvent()
    {
        return motionEvent;
    }
    
    /**
     * set the motion event.
     * 
     * @param motionEvent
     */
    public void setMotionEvent(TnMotionEvent motionEvent)
    {
        this.motionEvent = motionEvent;
    }
    
    /**
     * retrieve the command event.
     * 
     * @return {@link TnCommandEvent}
     */
    public TnCommandEvent getCommandEvent()
    {
        return commandEvent;
    }
    
    /**
     * set the command event.
     * 
     * @param commandEvent
     */
    public void setCommandEvent(TnCommandEvent commandEvent)
    {
        this.commandEvent = commandEvent;
    }
    
    /**
     * retrieve the private event.
     * 
     * @return {@link TnPrivateEvent}
     */
    public TnPrivateEvent getPrivateEvent()
    {
        return privateEvent;
    }
    
    /**
     * set the private event.
     * 
     * @param privateEvent
     */
    public void setPrivateEvent(TnPrivateEvent privateEvent)
    {
        this.privateEvent = privateEvent;
    }
}

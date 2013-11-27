/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMotionEvent.java
 *
 */
package com.telenav.tnui.core;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-22
 */
public class TnMotionEvent
{
    /**
     * A pressed gesture has started, the motion contains the initial starting location.
     */
    public static final int ACTION_DOWN = 0;

    /**
     * A pressed gesture has finished, the motion contains the final release location as well as any intermediate points
     * since the last down or move event.
     */
    public static final int ACTION_UP = 1;

    /**
     * A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP). The motion contains the most
     * recent point, as well as any intermediate points since the last down or move event.
     */
    public static final int ACTION_MOVE = 2;

    /**
     * The current gesture has been aborted. You will not receive any more points in it. You should treat this as an up
     * event, but not perform any action that you normally would.
     */
    public static final int ACTION_CANCEL = 3;

    /**
     * A movement has happened outside of the normal bounds of the UI element. This does not provide a full gesture, but
     * only the initial location of the movement/touch.
     */
    public static final int ACTION_OUTSIDE = 4;
    
    /**
     * long clicking the component.
     */
    public static final int ACTION_LONG_TOUCH = 5;
    
    public static final int ACTION_BEGIN_SCALE = 6;
    
    public static final int ACTION_ON_SCALE = 7;
    
    public static final int ACTION_END_SCALE = 8;

    private int[] xPos;

    private int[] yPos;

    private long eventTime = 0;
    
    private int action;
    
    private int pointerCount;
    
    private int distance;
    
    private boolean isScaleGesture;
    
    /**
     * Constructor of {@link TnMotionEvent}
     */
    public TnMotionEvent(int action)
    {
        this.action = action;
        
        this.xPos = new int[2];
        this.yPos = new int[2];
    }

    /**
     * for the first pointer index (may be an arbitrary pointer identifier).
     * 
     * @return the first pointer index
     */
    public int getX()
    {
        return this.xPos[0];
    }
    
    /**
     * Retrieves the mapped x coordinate for the specified touch point. 
     * 
     * @param touch Touch point associated with the x coordinate; 0 for the first (finger) touch point, 1 for the second (finger) touch point. 
     * @return Mapped x coordinate for the specified touch point. 
     */
    public int getX(int touch)
    {
        return this.xPos[touch];
    }
    
    /**
     * Retrieves the mapped y coordinate for the specified touch point. 
     * 
     * @param touch Touch point associated with the y coordinate; 0 for the first (finger) touch point, 1 for the second (finger) touch point. 
     * @return Mapped y coordinate for the specified touch point. 
     */
    public int getY(int touch)
    {
        return this.yPos[touch];
    }

    /**
     * Set this event's location.
     * 
     * @param xPos New absolute X location.
     * @param yPos New absolute Y location.
     */
    public void setLocation(int xPos, int yPos)
    {
        this.xPos[0] = xPos;
        this.yPos[0] = yPos;
    }
    
    /**
     * Set this event's location for the special touch point.
     * 
     * @param xPos New absolute X location.
     * @param yPos New absolute Y location.
     * @param touch Touch point associated with the y coordinate; 0 for the first (finger) touch point, 1 for the second (finger) touch point. 
     */
    public void setLocation(int xPos, int yPos, int touch)
    {
        this.xPos[touch] = xPos;
        this.yPos[touch] = yPos;
    }
    
    /**
     * set the pointer count.
     * 
     * @param pointerCount pointer count
     */
    public void setPointerCount(int pointerCount)
    {
        this.pointerCount = pointerCount;
        this.xPos = new int[pointerCount];
        this.yPos = new int[pointerCount];
    }
    
    /**
     * Retrieve the pointer count.
     * 
     * @return pointer count
     */
    public int getPointerCount()
    {
        return pointerCount;
    }

    /**
     * set the time when this specific event was generated.
     * 
     * @param time
     */
    public void setEventTime(long time)
    {
        this.eventTime = time;
    }
    
    public void setIsScaleGesture(boolean isScaleGesture)
    {
        this.isScaleGesture = isScaleGesture;
    }
    
    public boolean isScalGesture()
    {
        return this.isScaleGesture;
    }
    
    /**
     * Retrieve the time (in ms) when this specific event was generated.
     * 
     * @return the time (in ms) when this specific event was generated.
     */
    public long getEventTime()
    {
        return this.eventTime;
    }
    
    /**
     * for the first pointer index (may be an arbitrary pointer identifier).
     * 
     * @return for the first pointer index
     */
    public int getY()
    {
        return this.yPos[0];
    }

    /**
     * Return the kind of action being performed -- one of either ACTION_DOWN, ACTION_MOVE, ACTION_UP, or ACTION_CANCEL.
     * 
     * @return the kind of action being performed.
     */
    public int getAction()
    {
        return this.action;
    }
    
    public void setDistance(int distance)
    {
        this.distance = distance;
    }
    
    public int getDistance()
    {
        return this.distance;
    }
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnUiAnimationContext.java
 *
 */
package com.telenav.tnui.core;

import java.util.Hashtable;

/**
 * The TnUiAnimationContext class contains all the necessary data to uniquely describe a transition animation between UI components.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-12
 */
public final class TnUiAnimationContext
{
    /**
     * Constant for a wipe transition.
     */
    public final static String TRANSITION_WIPE = "TRANSITION_WIPE";
    
    /**
     * Attribute that indicates the path of movement for an animation.
     */
    public final static int ATTR_DIRECTION = -1;

    /**
     * Attribute that controls the elapsed time of the transition animation in milliseconds.
     */
    public final static int ATTR_DURATION = -2;

    /**
     * Attribute that indicates whether the incoming or outgoing UI component is animated.
     */
    public final static int ATTR_KIND = -3;
    
    /**
     * Attribute that indicates the clip bound of UI component.
     * Value should be TnRect.
     */
    public final static int ATTR_BOUND = -4;

    /**
     * Value for the direction attribute indicating that movement should occur to downwards.
     */
    public final static int DIRECTION_DOWN = 0;

    /**
     * Value for the direction attribute indicating that movement should occur to the left.
     */
    public final static int DIRECTION_LEFT = 1;

    /**
     * Value for the direction attribute indicating that movement should occur to the right.
     */
    public final static int DIRECTION_RIGHT = 2;

    /**
     * Value for the direction attribute indicating that movement should occur to upwards.
     */
    public final static int DIRECTION_UP = 3;

    /**
     * Value for the kind attribute that indicates the UI component that is pushed or exposed is animated.
     */
    public final static int KIND_IN = 0;

    /**
     * Value for the kind attribute that indicates the UI component that is popped or hidden is animated.
     */
    public final static int KIND_OUT = 1;

    private INativeUiAnimation nativeAnimation;

    private String id;

    private Hashtable attributes;
    
    private ITnUiAnimationListener listener;

    /**
     * Creates a new TnUiAnimationContext object that represents the specified animation id.
     * 
     * @param id The id of the animation this context should represent.
     */
    public TnUiAnimationContext(String id)
    {
        this.id = id;

        this.attributes = new Hashtable();
    }

    /**
     * Returns the animation id represented by this context. 
     * 
     * @return The id of the animation this context represents.
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Sets a value for the specified attribute of the animation. No validation occurs for attributes.
     * 
     * @param attrId The unique id of the attribute to set.
     * @param attrValue The value to set for the attribute. 
     */
    public void setAttribute(int attrId, int attrValue)
    {
        this.attributes.put(new Integer(attrId), new Integer(attrValue));
    }
    
    /**
     * Sets a value for the specified attribute of the animation. No validation occurs for attributes.
     * 
     * @param attrId The unique id of the attribute to set.
     * @param attrValue The value to set for the attribute. 
     */
    public void setAttribute(int attrId, Object attrValue)
    {
        this.attributes.put(new Integer(attrId), attrValue);
    }

    /**
     * Retrieves the value previously set for the specified attribute of this animation. 
     * 
     * @param attrId The unique id of the attribute to retrieve. 
     * @return The value of the attribute. 
     */
    public Object getAttribute(int attrId)
    {
        return this.attributes.get(new Integer(attrId));
    }

    /**
     * Retrieves the native animation object.
     * 
     * @return the native animation object.
     */
    public Object getNativeAnimation()
    {
        return this.nativeAnimation;
    }
    
    /**
     * Binds an animation listener to this animation. The animation listener is notified of animation events such as the
     * end of the animation or the repetition of the animation.
     * 
     * @param listener  the animation listener to be notified
     */
    public void setAnimationListener(ITnUiAnimationListener listener)
    {
        this.listener = listener;
    }
    
    public ITnUiAnimationListener getAnimationListener()
    {
        return this.listener;
    }
    
    void setNativeAnimation(INativeUiAnimation nativeAnimation)
    {
        this.nativeAnimation = nativeAnimation;
    }
    
    /**
     * An animation listener receives notifications from an animation. Notifications indicate animation related events,
     * such as the end or the repetition of the animation.
     * 
     * @author fqming
     * 
     */
    public interface ITnUiAnimationListener
    {
        /**
         * Notifies the start of the animation.
         * 
         * @param animation The started animation. 
         */
        public void onAnimationStart(TnUiAnimationContext  animation);
        
        /**
         * Notifies the end of the animation.
         * 
         * @param animation The animation which reached its end. 
         */
        public void onAnimationEnd(TnUiAnimationContext  animation);
        
        /**
         * Notifies the repetition of the animation.
         * 
         * @param animation The animation which was repeated. 
         */
        public void onAnimationRepeat(TnUiAnimationContext  animation);
    }
}

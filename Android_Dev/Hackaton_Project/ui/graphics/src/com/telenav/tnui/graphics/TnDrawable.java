/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnDrawable.java
 *
 */
package com.telenav.tnui.graphics;

/**
 * A drawable is a general abstraction for "something that can be drawn." Most often you will deal with Drawable as the
 * type of resource retrieved for drawing things to the screen; the Drawable class provides a generic API for dealing
 * with an underlying visual resource that may take a variety of forms. Unlike a Component, a Drawable does not have any
 * facility to receive events or otherwise interact with the user.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 13, 2010
 */
public abstract class TnDrawable
{
    protected TnRect bounds;

    /**
     * Specify a bounding rectangle for the drawable. This is where the drawable will draw when its draw() method is
     * called.
     * 
     * @param bounds a bounding rectangle for the drawable
     */
    public void setBounds(TnRect bounds)
    {
        this.bounds = bounds;
    }
    
    /**
     * Retrieve the bounding rectangle of this drawable.
     * 
     * @return a bounding rectangle for the drawable
     */
    public TnRect getBounds()
    {
        return this.bounds;
    }

    /**
     * Draw in its bounds (set via setBounds) respecting optional effects.
     * 
     * @param g The Graphics to draw into 
     */
    public abstract void draw(AbstractTnGraphics g);
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnPoint.java
 *
 */
package com.telenav.tnui.graphics;

/**
 * Point holds two integer coordinates.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class TnPoint
{
    /**
     * x coordinate.
     */
    public int x;

    /**
     * y coordinate.
     */
    public int y;

    /**
     * construct a default point.
     */
    public TnPoint()
    {
    }

    /**
     * construct the point from two integer.
     * 
     * @param x
     * @param y
     */
    public TnPoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * construct the point from another point.
     * 
     * @param src
     */
    public TnPoint(TnPoint src)
    {
        this.x = src.x;
        this.y = src.y;
    }

    /**
     * Set the point's x and y coordinates.
     * 
     * @param x
     * @param y
     */
    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Copy the source point into this point.
     * 
     * @param src {@link TnPoint}
     */
    public void set(TnPoint src)
    {
        this.x = src.x;
        this.y = src.y;
    }

    /**
     * Negate the point's coordinates.
     * 
     */
    public final void negate()
    {
        x = -x;
        y = -y;
    }

    /**
     * Offset the point's coordinates by dx, dy.
     * 
     * @param dx
     * @param dy
     */
    public final void offset(int dx, int dy)
    {
        x += dx;
        y += dy;
    }

    /**
     * Returns true if the point's coordinates equal (x,y).
     * 
     * @param x
     * @param y
     * @return boolean
     */
    public final boolean equals(int x, int y)
    {
        return this.x == x && this.y == y;
    }

    /**
     * Compares this instance with the specified object and indicates if they are equal. In order to be equal, o must represent the same object as this instance using a class-specific comparison. The general contract is that this comparison should be both transitive and reflexive. 
     * 
     * @return boolean
     */
    public boolean equals(Object o)
    {
        if (o instanceof TnPoint)
        {
            TnPoint p = (TnPoint) o;
            return this.x == p.x && this.y == p.y;
        }
        return false;
    }

    /**
     * Returns an integer hash code for this object.
     *  
     * @return int
     */
    public int hashCode()
    {
        return x * 32713 + y;
    }

    /**
     * Returns a string containing a concise, human-readable description of this object.
     * 
     * @return String
     */
    public String toString()
    {
        return "Point(" + x + ", " + y + ")";
    }
}

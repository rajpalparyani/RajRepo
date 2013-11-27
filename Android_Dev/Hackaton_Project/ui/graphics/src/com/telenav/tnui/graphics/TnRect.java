/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnRect.java
 *
 */
package com.telenav.tnui.graphics;

/**
 * Rectangle holds four integer coordinates for a rectangle. The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed directly. Use width() and height() to retrieve the rectangle's width and height. Note: most methods do not check to see that the coordinates are sorted correctly (i.e. left <= right and top <= bottom). 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class TnRect
{
    /**
     * left coordinate.
     */
    public int left;

    /**
     * top coordinate.
     */
    public int top;

    /**
     * right coordinate.
     */
    public int right;

    /**
     * bottom coordinate.
     */
    public int bottom;

    /**
     * Create a new empty rectangle.
     */
    public TnRect()
    {
    }

    /**
     * Create a new rectangle with the specified coordinates.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public TnRect(int left, int top, int right, int bottom)
    {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * Create a new rectangle, initialized with the values in the specified rectangle (which is left unmodified).
     * 
     * @param r
     */
    public TnRect(TnRect r)
    {
        left = r.left;
        top = r.top;
        right = r.right;
        bottom = r.bottom;
    }

    /**
     * Compares this instance with the specified object and indicates if they are equal.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof TnRect)
        {
            TnRect r = (TnRect) obj;
            if (r != null)
            {
                return left == r.left && top == r.top && right == r.right && bottom == r.bottom;
            }
        }
        
        return false;
    }
    
    /**
     * Returns the hashcode for this <code>TnRect</code>.
     */
    public int hashCode()
    {
        long bits = Double.doubleToLongBits(left);
        bits += Double.doubleToLongBits(top) * 37;
        bits += Double.doubleToLongBits(width()) * 43;
        bits += Double.doubleToLongBits(height()) * 47;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    /**
     * Returns a string containing a concise, human-readable description of this object.
     * 
     */
    public String toString()
    {
        return "Rect(" + left + ", " + top + " - " + right + ", " + bottom + ")";
    }

    /**
     * Returns true if the rectangle is empty (left >= right or top >= bottom) 
     * 
     * @return boolean
     */
    public final boolean isEmpty()
    {
        return left >= right || top >= bottom;
    }

    /**
     * the rectangle's width. This does not check for a valid rectangle (i.e. left <= right) so the result may be negative. 
     * 
     * @return int
     */
    public final int width()
    {
        return right - left;
    }

    /**
     * the rectangle's height. This does not check for a valid rectangle (i.e. top <= bottom) so the result may be negative. 
     * 
     * @return int
     */
    public final int height()
    {
        return bottom - top;
    }

    /**
     * the horizontal center of the rectangle. If the computed value is fractional, this method returns the largest integer that is less than the computed value. 
     * 
     * @return int
     */
    public final int centerX()
    {
        return (left + right) >> 1;
    }

    /**
     * the vertical center of the rectangle. If the computed value is fractional, this method returns the largest integer that is less than the computed value. 
     * 
     * @return int
     */
    public final int centerY()
    {
        return (top + bottom) >> 1;
    }

    /**
     * Set the rectangle to (0,0,0,0).
     */
    public void setEmpty()
    {
        left = right = top = bottom = 0;
    }

    /**
     * Set the rectangle's coordinates to the specified values. Note: no range checking is performed, so it is up to the caller to ensure that left <= right and top <= bottom.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void set(int left, int top, int right, int bottom)
    {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * Copy the coordinates from src into this rectangle.
     * 
     * @param src {@link TnRect}
     */
    public void set(TnRect src)
    {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    /**
     * Offset the rectangle by adding dx to its left and right coordinates, and adding dy to its top and bottom coordinates.
     * 
     * @param dx
     * @param dy
     */
    public void offset(int dx, int dy)
    {
        left += dx;
        top += dy;
        right += dx;
        bottom += dy;
    }

    /**
     * Offset the rectangle to a specific (left, top) position, keeping its width and height the same.
     * 
     * @param newLeft
     * @param newTop
     */
    public void offsetTo(int newLeft, int newTop)
    {
        right += newLeft - left;
        bottom += newTop - top;
        left = newLeft;
        top = newTop;
    }

    /**
     * Inset the rectangle by (dx,dy).
     * 
     * @param dx
     * @param dy
     */
    public void inset(int dx, int dy)
    {
        left += dx;
        top += dy;
        right -= dx;
        bottom -= dy;
    }

    /**
     * 
    Returns true if (x,y) is inside the rectangle.
     * @param x
     * @param y
     * @return boolean
     */
    public boolean contains(int x, int y)
    {
        return left < right && top < bottom // check for empty first
                && x >= left && x < right && y >= top && y < bottom;
    }

    /**
     * Returns true iff the 4 specified sides of a rectangle are inside or equal to this rectangle.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return boolean
     */
    public boolean contains(int left, int top, int right, int bottom)
    {
        return this.left < this.right && this.top < this.bottom && this.left <= left && this.top <= top && this.right >= right
                && this.bottom >= bottom;
    }

    /**
     * Returns true iff the specified rectangle r is inside or equal to this rectangle.
     * 
     * @param r
     * @return {@link TnRect}
     */
    public boolean contains(TnRect r)
    {
        // check for empty first
        return this.left < this.right && this.top < this.bottom
        // now check for containment
                && left <= r.left && top <= r.top && right >= r.right && bottom >= r.bottom;
    }

    /**
     * If the rectangle specified by left,top,right,bottom intersects this rectangle, return true and set this rectangle to that intersection, otherwise return false and do not change this rectangle.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return int
     */
    public boolean intersect(int left, int top, int right, int bottom)
    {
        if (this.left < right && left < this.right && this.top < bottom && top < this.bottom)
        {
            if (this.left < left)
            {
                this.left = left;
            }
            if (this.top < top)
            {
                this.top = top;
            }
            if (this.right > right)
            {
                this.right = right;
            }
            if (this.bottom > bottom)
            {
                this.bottom = bottom;
            }
            return true;
        }
        return false;
    }

    /**
     * If the specified rectangle intersects this rectangle, return true and set this rectangle to that intersection, otherwise return false and do not change this rectangle.
     * 
     * @param r
     * @return {@link TnRect}
     */
    public boolean intersect(TnRect r)
    {
        return intersect(r.left, r.top, r.right, r.bottom);
    }

    /**
     * If rectangles a and b intersect, return true and set this rectangle to that intersection, otherwise return false and do not change this rectangle.
     * 
     * @param a
     * @param b
     * @return boolean
     */
    public boolean setIntersect(TnRect a, TnRect b)
    {
        if (a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom)
        {
            left = Math.max(a.left, b.left);
            top = Math.max(a.top, b.top);
            right = Math.min(a.right, b.right);
            bottom = Math.min(a.bottom, b.bottom);
            return true;
        }
        return false;
    }

    /**
     * Returns true if this rectangle intersects the specified rectangle.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return boolean
     */
    public boolean intersects(int left, int top, int right, int bottom)
    {
        return this.left < right && left < this.right && this.top < bottom && top < this.bottom;
    }

    /**
     * Returns true iff the two specified rectangles intersect.
     * 
     * @param a
     * @param b
     * @return boolean
     */
    public static boolean intersects(TnRect a, TnRect b)
    {
        return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
    }

    /**
     * Update this Rect to enclose itself and the specified rectangle.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void union(int left, int top, int right, int bottom)
    {
        if ((left < right) && (top < bottom))
        {
            if ((this.left < this.right) && (this.top < this.bottom))
            {
                if (this.left > left)
                    this.left = left;
                if (this.top > top)
                    this.top = top;
                if (this.right < right)
                    this.right = right;
                if (this.bottom < bottom)
                    this.bottom = bottom;
            }
            else
            {
                this.left = left;
                this.top = top;
                this.right = right;
                this.bottom = bottom;
            }
        }
    }

    /**
     * Update this Rect to enclose itself and the specified rectangle.
     * 
     * @param r {@link TnRect}
     */
    public void union(TnRect r)
    {
        union(r.left, r.top, r.right, r.bottom);
    }

    /**
     * Update this Rect to enclose itself and the [x,y] coordinate.
     * 
     * @param x
     * @param y
     */
    public void union(int x, int y)
    {
        if (x < left)
        {
            left = x;
        }
        else if (x > right)
        {
            right = x;
        }
        if (y < top)
        {
            top = y;
        }
        else if (y > bottom)
        {
            bottom = y;
        }
    }

    /**
     * Swap top/bottom or left/right if there are flipped.
     * 
     */
    public void sort()
    {
        if (left > right)
        {
            int temp = left;
            left = right;
            right = temp;
        }
        if (top > bottom)
        {
            int temp = top;
            top = bottom;
            bottom = temp;
        }
    }

}

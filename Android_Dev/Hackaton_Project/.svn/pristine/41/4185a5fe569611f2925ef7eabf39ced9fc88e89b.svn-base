/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * Polygon.java
 *
 */
package com.telenav.data.polygon;

import android.graphics.Point;
import android.graphics.Rect;

/**
 *@author yning
 *@date 2012-3-7
 */
public class Polygon
{
    public int npoints;

    public int xpoints[];

    public int ypoints[];

    protected Rect bounds;

    private static final int MIN_LENGTH = 4;

    public Polygon()
    {
        xpoints = new int[MIN_LENGTH];
        ypoints = new int[MIN_LENGTH];
    }

    public void addPoint(int x, int y)
    {
        if (npoints >= xpoints.length || npoints >= ypoints.length)
        {
            int newLength = npoints * 2;
            // Make sure that newLength will be greater than MIN_LENGTH and
            // aligned to the power of 2
            if (newLength < MIN_LENGTH)
            {
                newLength = MIN_LENGTH;
            }
            else if ((newLength & (newLength - 1)) != 0)
            {
                newLength = Integer.highestOneBit(newLength);
            }

            int[] oldX = xpoints;
            int[] oldY = ypoints;
            xpoints = new int[newLength];
            ypoints = new int[newLength];
            System.arraycopy(oldX, 0, xpoints, 0, oldX.length);
            System.arraycopy(oldY, 0, ypoints, 0, oldY.length);
        }
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        npoints++;
        if (bounds != null)
        {
            updateBounds(x, y);
        }
    }

    void updateBounds(int x, int y)
    {
        if (x < bounds.left)
        {
            bounds.left = x;
        }
        else if (x > bounds.right)
        {
            bounds.right = x;
        }

        if (y < bounds.top)
        {
            bounds.top = y;
        }
        else if (y > bounds.bottom)
        {
            bounds.bottom = y;
        }
    }

    public boolean intersects(int x, int y, int w, int h)
    {
        if (npoints <= 0 || !getBoundingBox().intersects(x, y, x + w, y + h))
        {
            return false;
        }

        Crossings cross = getCrossings(x, y, x + w, y + h);
        return (cross == null || !cross.isEmpty());
    }

    private Crossings getCrossings(double xlo, double ylo, double xhi, double yhi)
    {
        Crossings cross = new Crossings.EvenOdd(xlo, ylo, xhi, yhi);
        int lastx = xpoints[npoints - 1];
        int lasty = ypoints[npoints - 1];
        int curx, cury;

        // Walk the edges of the polygon
        for (int i = 0; i < npoints; i++)
        {
            curx = xpoints[i];
            cury = ypoints[i];
            if (cross.accumulateLine(lastx, lasty, curx, cury))
            {
                return null;
            }
            lastx = curx;
            lasty = cury;
        }

        return cross;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 1.2
     */
    public boolean intersects(Rect r)
    {
        return intersects(r.left, r.top, r.right - r.left, r.bottom - r.top);
    }

    public Rect getBoundingBox()
    {
        if (npoints == 0)
        {
            return new Rect();
        }
        if (bounds == null)
        {
            calculateBounds(xpoints, ypoints, npoints);
        }
        return bounds;
    }

    void calculateBounds(int xpoints[], int ypoints[], int npoints)
    {
        int boundsMinX = Integer.MAX_VALUE;
        int boundsMinY = Integer.MAX_VALUE;
        int boundsMaxX = Integer.MIN_VALUE;
        int boundsMaxY = Integer.MIN_VALUE;

        for (int i = 0; i < npoints; i++)
        {
            int x = xpoints[i];
            boundsMinX = Math.min(boundsMinX, x);
            boundsMaxX = Math.max(boundsMaxX, x);
            int y = ypoints[i];
            boundsMinY = Math.min(boundsMinY, y);
            boundsMaxY = Math.max(boundsMaxY, y);
        }
        bounds = new Rect(boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
    }
    
    public boolean contains(Point p)
    {
        return contains(p.x, p.y);
    }
    
    public boolean contains(int x, int y)
    {
        if (npoints <= 2 || !getBoundingBox().contains(x, y))
        {
            return false;
        }
        int hits = 0;

        int lastx = xpoints[npoints - 1];
        int lasty = ypoints[npoints - 1];
        int curx, cury;

        // Walk the edges of the polygon
        for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++)
        {
            curx = xpoints[i];
            cury = ypoints[i];

            if (cury == lasty)
            {
                continue;
            }

            int leftx;
            if (curx < lastx)
            {
                if (x >= lastx)
                {
                    continue;
                }
                leftx = curx;
            }
            else
            {
                if (x >= curx)
                {
                    continue;
                }
                leftx = lastx;
            }

            double test1, test2;
            if (cury < lasty)
            {
                if (y < cury || y >= lasty)
                {
                    continue;
                }
                if (x < leftx)
                {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            }
            else
            {
                if (y < lasty || y >= cury)
                {
                    continue;
                }
                if (x < leftx)
                {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }

            if (test1 < (test2 / (lasty - cury) * (lastx - curx)))
            {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Poly.java
 *
 */
package com.telenav.map.opengl.java.math;

import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-27
 */
public class Poly
{
    protected static float cross(float[] a, float[] b)
    {
        return a[0] * b[1] - b[0] * a[1];
    }
    
    public static boolean overlaps(float[] p0, float[] p1, float[] p2, float[] p3, float[] q0, float[] q1, float[] q2, float[] q3)
    {
        return surrounds(p0, q0, q1, q2, q3) || surrounds(p1, q0, q1, q2, q3) || surrounds(p2, q0, q1, q2, q3)
                || surrounds(p3, q0, q1, q2, q3) || surrounds(q0, p0, p1, p2, p3) || surrounds(q1, p0, p1, p2, p3)
                || surrounds(q2, p0, p1, p2, p3) || surrounds(q3, p0, p1, p2, p3);
    }
    
    
    public static boolean surrounds(float[] point, float[] p0, float[] p1, float[] p2, float[] p3)
    {
        int s = Sign.sign(cross(Pe.sub(p1, p0), Pe.sub(point, p0)));

        return (s == Sign.sign(cross(Pe.sub(p2, p1), Pe.sub(point, p1))))
                && (s == Sign.sign(cross(Pe.sub(p3, p2), Pe.sub(point, p2))))
                && (s == Sign.sign(cross(Pe.sub(p0, p3), Pe.sub(point, p3))));
    }
    
    public static double length(Vector points) {
        
        double length = 0.0;

        int i = 0;
        if (i < points.size()) {
            double[] last_p = (double[])points.elementAt(i);
            ++i;
            while (i != points.size()) {

                double[] p = (double[])points.elementAt(i);
                length += Pe.magnitude(Pe.sub(p, last_p));
                last_p = p;
                ++i;
            }
        }
        return length;
    }
    
    public static double[] lerp(Vector points, double length)
    {
        double to_go = length;

        int i = 0;
        if (i < points.size()) {

            double[] prev_p = (double[])points.elementAt(i++);

            // If to_go before first point, return first point;
            if (to_go < 0) return prev_p;

            if (i < points.size()) {
                
                double[] p = (double[])points.elementAt(i++);

                double seg_len = Pe.magnitude(Pe.sub(p, prev_p));
                while ((i < points.size()) && (to_go > seg_len)) {

                    to_go -= seg_len;
                    prev_p = p;
                    p = (double[])points.elementAt(i++);
                    seg_len = Pe.magnitude(Pe.sub(p, prev_p));
                }

                // If to_go beyond last point, return last point.
                if (to_go > seg_len) return p;      

                return Pe.add(prev_p , Pe.multiply(Pe.sub(p, prev_p), (to_go / seg_len)));

            } else {

                // Only one point. Return it.
                return prev_p;
            }
        }

        // No points. Undefined. Signal error?
        return new double[]{0, 0, 0};
    }
    
    public static Vector dash(Vector points, double on, double off)
    {
        if (points.size() < 2) return points;

        double total_length = length(points);
        int count = (int)Math.floor(total_length/(on+off));      // Total 'off' dashes.
        double fudge = (total_length - count * (on+off))/2;           // Amount extra on at each end.

        Vector result = new Vector();

        // Do the inefficient thing for now. That is, just lerp into the
        // edge every time instead of iterating. @TODO: In the future,
        // this should build the result points in a single pass through
        // the edge.

        result.addElement(points.firstElement());

        for (int i = 0; i<count; ++i)
        {
            double d0 = fudge + (on/2) + i * (on+off);
            double d1 = d0 + off;
            result.addElement(lerp(points, d0));
            result.addElement(lerp(points, d1));
        }

        result.addElement(points.lastElement());

        return result;
    }
}

package com.telenav.map.opengl.java.math;

import java.util.Vector;

import com.telenav.logger.Logger;

public class PolygonTriangulator
{
    private static final double BIG = 1.0e30;      /* A number bigger than we expect to find here */

    private static final int COUNTER_CLOCKWISE = 0;
    private static final int CLOCKWISE = 1;


    private static int[] vp = new int[200];       
    /*
     * orientation
     *
     * Return either clockwise or counter_clockwise for the orientation
     * of the polygon.
     */

    private static int orientation(int n, double[][] v)
    {
        /* Do the wrap-around first */
        double area = v[n-1][0] * v[0][1] - v[0][0] * v[n-1][1];

        /* Compute the area (times 2) of the polygon */
        for (int i = 0; i < n-1; i++)
        {
            area += v[i][0] * v[i+1][1] - v[i+1][0] * v[i][1];
        }

        if (area >= 0.0)
            return COUNTER_CLOCKWISE;
        else
            return CLOCKWISE;
    }



    /*
     * determinant
     *
     * Computes the determinant of the three points.
     * Returns whether the triangle is clockwise or counter-clockwise.
     */

    private static int determinant(int p1, int p2, int p3, double[][] v)
    {
        double x1, x2, x3, y1, y2, y3;
        double determ;

        x1 = v[p1][0];
        y1 = v[p1][1];
        x2 = v[p2][0];
        y2 = v[p2][1];
        x3 = v[p3][0];
        y3 = v[p3][1];

        determ = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
        if (determ >= 0.0)
            return COUNTER_CLOCKWISE;
        else
            return CLOCKWISE;
    }

    /*
     * distance2
     *
     * Returns the square of the distance between the two points
     */

    private static double distance2(double x1, double y1, double x2, double y2 )
    {
        double xd, yd;               /* The distances in X and Y */
        double dist2;                /* The square of the actual distance */

        xd = x1 - x2;
        yd = y1 - y2;
        dist2 = xd * xd + yd * yd;

        return dist2;
    }

    /*
     * no_interior
     *
     * Returns 1 if no other point in the vertex list is inside
     * the triangle specified by the three points.  Returns
     * 0 otherwise.
     */

    private static boolean no_interior(int p1, int p2, int p3, double[][] v, int[] vp, int n, int poly_or)
    {
        for (int i = 0; i < n; i++) {
            int p = vp[i];              /* The point to test */
            if ((p == p1) || (p == p2) || (p == p3))
                continue;           /* Don't bother checking against yourself */
            if (   (determinant( p2, p1, p, v ) == poly_or)
                    || (determinant( p1, p3, p, v ) == poly_or)
                    || (determinant( p3, p2, p, v ) == poly_or) ) {
                continue;           /* This point is outside */
            } else {
                return false;           /* The point is inside */
            }
        }
        return true;                   /* No points inside this triangle */
    }

    /*
     * draw_poly
     *
     * Call this procedure with a polygon, this divides it into triangles
     * and calls the triangle routine once for each triangle.
     *
     * Note that this does not work for polygons with holes or self
     * penetrations.
     */

    public static Vector convertToTriangles(int n, double[][] v)
    {
        Vector result = new Vector();
        int prev = 0;
        int cur = 0;
        int next = 0;        /* Three points currently being considered */
        int count = 0;                  /* How many vertices left */
        int min_vert = 0;               /* Vertex with minimum distance */
        int i = 0;                      /* Iterative counter */
        double dist = 0d;                 /* Distance across this one */
        double min_dist = 0d;             /* Minimum distance so far */
        int poly_orientation = 0;       /* Polygon orientation */

        if (n > vp.length) 
        {
            vp = new int[n];
        }

        poly_orientation = orientation( n, v );

        for (i = 0; i < n; i++)
            vp[i] = i;              /* Put vertices in order to begin */

        /* Slice off clean triangles until nothing remains */

        count = n;
        while (count > 3) {
            min_dist = BIG;         /* A real big number */
            min_vert = 0;           /* Just in case we don't find one... */
            for (cur = 0; cur < count; cur++) {
                prev = cur - 1;
                next = cur + 1;
                if (cur == 0)       /* Wrap around on the ends */
                    prev = count - 1;
                else if (cur == count - 1)
                    next = 0;
                /* Pick out shortest distance that forms a good triangle */
                if (   (determinant( vp[prev], vp[cur], vp[next], v ) == poly_orientation)
                        /* Same orientation as polygon */
                        && no_interior( vp[prev], vp[cur], vp[next], v, vp, count, poly_orientation )
                        /* No points inside */
                        && ((dist = distance2( v[vp[prev]][0], v[vp[prev]][1],
                                v[vp[next]][0], v[vp[next]][1] )) < min_dist) )
                    /* Better than any so far */
                {
                    min_dist = dist;
                    min_vert = cur;
                }
            } /* End of for each vertex (cur) */

            /* The following error should "never happen". */
            if (min_dist == BIG)
            {
                Logger.log(Logger.INFO, PolygonTriangulator.class.getName(), "Error: Didn't find a triangle");
//                System.out.println("Error: Didn't find a triangle" );
            }

            prev = min_vert - 1;
            next = min_vert + 1;
            if (min_vert == 0)      /* Wrap around on the ends */
                prev = count - 1;
            else if (min_vert == count - 1)
                next = 0;

            /* Output this triangle */

            result.addElement(new double[]{v[vp[prev]][0], v[vp[prev]][1], v[vp[prev]][2]});
            result.addElement(new double[]{v[vp[min_vert]][0], v[vp[min_vert]][1], v[vp[min_vert]][2]});
            result.addElement(new double[]{v[vp[next]][0], v[vp[next]][1], v[vp[next]][2]});

            /* Remove the triangle from the polygon */

            count -= 1;
            for (i = min_vert; i < count; i++)
                vp[i] = vp[i+1];
        }

        /* Output the final triangle */

        result.addElement(new double[]{v[vp[0]][0], v[vp[0]][1], v[vp[0]][2]});
        result.addElement(new double[]{v[vp[1]][0], v[vp[1]][1], v[vp[1]][2]});
        result.addElement(new double[]{v[vp[2]][0], v[vp[2]][1], v[vp[2]][2]});

        return result;
    } /* End of draw_poly */

    /* End of poly_tri.c */

}

package com.telenav.datatypes.map;

import com.telenav.datatypes.DataUtil;

/**
 * Handles 2d points as longs.
 * Use it to pass around data that otherwise either take an
 * array to store or two independent variables.
 *
 * @author vladp
 */
public abstract class LongPoint
{
    private final static long LOW_INT_MASK = (1L << 32) - 1;
    private final static long HI_INT_MASK = -1L ^ LOW_INT_MASK;

    /**
     * Creates a long representing an integer point.
     *
     * @param x first component
     * @param y second component
     * @return a long that contains the two ints
     */
    public static long fromInts(int x, int y)
    {
        return ((long) x) << 32 | (LOW_INT_MASK & y);
    }

    /**
     * Creates a long representing an integer point.
     *
     * @param p the coordinates (x, y)
     * @return a long that contains the two ints
     */
    public static long fromInts(int[] p)
    {
        return fromInts(p[0], p[1]);
    }

    /**
     * Returns the first int stored in a long
     * @param n the long that stores two int numbers
     * @return the first int
     */
    public static int first(long n)
    {
        return (int) (n >> 32);
    }

    /**
     * Returns the second int stored in a long
     * @param n the long that stores two int numbers
     * @return the second int
     */
    public static int second(long n)
    {
        return (int) (LOW_INT_MASK & n);
    }

    /**
     * Adds two int 2d vectors represented as longs
     * @param a the first planar vector
     * @param b the second planar vector
     * @return the sum of a and b, as int vectors
     */
    public static long addVectors(long a, long b)
    {
        return (a & HI_INT_MASK) + (b & HI_INT_MASK) + (LOW_INT_MASK & (a + b));
    }

    /**
     * Subtracts two int 2d vectors represented as longs
     * @param a the first planar vector
     * @param b the second planar vector
     * @return the difference of a and b, as int vectors
     */
    public static long subVectors(long a, long b)
    {
        return (a & HI_INT_MASK) - (b & HI_INT_MASK) + (LOW_INT_MASK & (a - b));
    }

    /**
     * Converts a long to int, limiting the values, so that
     * too big values become Integer.MAX_VALUE, and too low values
     * become Integer.MIN_VALUE
     *
     * @param x the long to turn into an int
     * @return the int value
     */
    public static int toInt(long x)
    {
        return (int) (Math.max(Math.min(x, Integer.MAX_VALUE), Integer.MIN_VALUE));
    }

    /**
     * Multiplies a planar int vector (represented a long) by an int
     * @param a the vector
     * @param c the int
     * @return vector multiplied by c (component-wise)
     */
    public static long multBy(long a, int c)
    {
        return fromInts(toInt(((long) c) * first(a)), toInt(((long) c) * second(a)));
    }

    /**
     * Calculates a scalar product of two planar int vectors (represented as long)
     * @param a first vector
     * @param b second vector
     * @return their scalar product as int
     */
    public static int scalarProduct(long a, long b)
    {
        long p = ((long) first(a)) * first(b) + ((long) second(a)) * second(b);
        return toInt(p);
    }

    /**
     * String representation of one point
     * 
     * @param p the point
     * @return (px,py)
     */
    public static String toString(long p)
    {
        return "(" + first(p) + "," + second(p) + ")";
    }
    
    /**
     * String representation of an array of points
     * 
     * @param ps the points
     * @return [(p1x,p1y), ...]
     */
    public static String toString(long[] ps)
    {
        if (ps == null)
        {
            return "null";
        }
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < ps.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(toString(ps[i]));   
        }
        return sb.append("]").toString();
    }
    
    /**
     * Calculates vector length, that is, its Eucledian metric.
     * @param vector (x, y)
     * @return sqrt(x^2+y^2)
     */
    public static int length(long vector)
    {
        return DataUtil.distance(first(vector), second(vector));
    }

    /**
     * Calculates minimum point for two points
     * @param p1 (p1x, p1y)
     * @param p2 (p2x, p2y)
     * @return (min(p1x, p2x), (min(p1y, p2y))
     */
    public static long min(long p1, long p2)
    {
        return fromInts(Math.min(first(p1), first(p2)), 
                        Math.min(second(p1), second(p2)));
    }

    /**
     * Calculates maximum point for two points
     * @param p1 (p1x, p1y)
     * @param p2 (p2x, p2y)
     * @return (max(p1x, p2x), (max(p1y, p2y))
     */
    public static long max(long p1, long p2)
    {
        return fromInts(Math.max(first(p1), first(p2)), 
                        Math.max(second(p1), second(p2)));
    }
    
    /**
     * Shifts an array of points to a given vector
     * @param points points to shift
     * @param shift the vector to add to each point
     */
    public static void add(long[] points, long shift)
    {
        for (int i = 0; i < points.length; i++)
        {
            points[i] = addVectors(points[i], shift);
        }
    }
    
    /**
     * Shifts an array of points in the direction opposite to a given vector
     * @param points points to shift
     * @param shift the vector to subtract from each point
     */
    public static void subtract(long[] points, long shift)
    {
        for (int i = 0; i < points.length; i++)
        {
            points[i] = LongPoint.subVectors(points[i], shift);
        }
    }
    
    /**
     * Converts an array of (int, int) arrays to an array of long points
     * @param source int[][2] - coordinates, in arrays
     * @return long[]
     */
    public static long[] fromInts(int[][] source)
    {
       if (source == null)
       {
           return null;
       }
       long[] result = new long[source.length];
       
       for (int i = 0; i < source.length; i++)
       {
           result[i] = fromInts(source[i][0], source[i][1]);    
       }
       return result;
    }

    /**
     * Divides long point's coordinates into given denominator
     * @param point long point to rescale
     * @param denominator
     * @return a new point with coordinates recalculated
     */
    public static long divide(long point, int denominator)
    {
        return LongPoint.fromInts(LongPoint.first(point)  / denominator, 
                                  LongPoint.second(point) / denominator);
    }
    
    /**
     * Rescales in place an array of long points according to scale = 1 / denominator
     * {@see #divide(long, int)}
     * 
     * @param points
     * @param denominator
     */
    public static void divide(long[] points, int denominator)
    {
        for (int i = 0; i < points.length; i++)
        {
            points[i] = divide(points[i], denominator);
        }
    }

    /**
     * Rescales long point coordinates according to scale = numerator / denominator
     * @param point long point to rescale
     * @param numerator
     * @param denominator
     * @return a new point with coordinates recalculated
     */
    public static long rescale(long point, int numerator, int denominator)
    {
        return LongPoint.fromInts((int) (((long) LongPoint.first(point) * numerator * 2 + denominator) / denominator / 2), 
                                  (int) (((long) LongPoint.second(point)* numerator * 2 + denominator) / denominator / 2));
    }
    
    /**
     * Rescales in place an array of long points according to scale = numerator / denominator
     * {@see #rescale(long, int, int)}
     * 
     * @param points
     * @param numerator
     * @param denominator
     */
    public static void rescale(long[] points, int numerator, int denominator)
    {
        for (int i = 0; i < points.length; i++)
        {
            points[i] = rescale(points[i], numerator, denominator);
        }
    }

    /**
     * Creates a point for lengths and heading 
     * @param length radius in polar coordinates
     * @param heading angle in polar coordinates
     * @return
     */
    public static long fromPolar(int length, int heading)
    {
        heading = ((heading % 360) + 360) % 360;
        return fromInts((int) DataUtil.xCosY(length, heading),
                        (int) DataUtil.xSinY(length, heading));
    }
}

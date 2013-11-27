/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Pe.java
 *
 */
package com.telenav.map.opengl.java.math;

import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex3f4b;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.math.Ray.Rayf;


/**
 *@author fqming (fqming@telenav.cn)
 *@date Sep 9, 2010
 */
public class Pe
{
    public static float[] add(float[] p1, float[] p2)
    {
        float[] result = new float[p1.length];
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] + p2[i];
        }
        
        return result;
    }
    
    public static void add(float[] p1, float[] p2, float[] result)
    {
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] + p2[i];
        }
    }
    
    public static double[] add(double[] p1, double[] p2)
    {
        double[] result = new double[p1.length];
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] + p2[i];
        }
        
        return result;
    }
    
    public static void add(double[] p1, double[] p2, double[] result)
    {
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] + p2[i];
        }
    }
    
    public static float[] sub(float[] p1, float[] p2)
    {
        float[] result = new float[p1.length];
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] - p2[i];
        }
        
        return result;
    }
    
    public static void sub(float[] p1, float[] p2, float[] result)
    {
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] - p2[i];
        }
    }
    
    public static double[] sub(double[] p1, double[] p2)
    {
        double[] result = new double[p1.length];
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] - p2[i];
        }
        
        return result;
    }
    
    public static void sub(double[] p1, double[] p2, double[] result)
    {
        for(int i = 0; i < p1.length; i++)
        {
            result[i] = p1[i] - p2[i];
        }
    }
    
    public static float[] multiply(float[] a, float s)
    {
        float[] result = new float[a.length];
        multiply(a, s, result);
        return result;
    }
    
    public static void multiply(float[] a, float s, float[] result)
    {
        scale(a, s, result);
    }
    
    public static void scale(float[] a, float s, float[] result)
    {
        for(int i = 0; i < a.length; i++)
        {
            result[i] = a[i] * s;
        }
    }
    
    public static double[] multiply(double[] a, double s)
    {
        double[] result = new double[a.length];
        scale(a, s, result);
        return result;
    }
    
    public static void multiply(double[] a, double s, double[] result)
    {
        scale(a, s, result);
    }
    
    public static void scale(double[] a, double s, double[] result)
    {
        for(int i = 0; i < a.length; i++)
        {
            result[i] = a[i] * s;
        }
    }
    
    public static float dot(float[] p1, float[] p2)
    {
        float dot = 0;
        for(int i = 0; i < p1.length; i++)
        {
            dot += p1[i] * p2[i];
        }
        
        return dot;
    }
    
    public static double invSqrt(float t)
    {
        return 1f / Math.sqrt(t);
    }
    
    public static double invSqrt(double t)
    {
        return 1d / Math.sqrt(t);
    }
    
    public static double dot(double[] p1, double[] p2)
    {
        double dot = 0;
        for(int i = 0; i < p1.length; i++)
        {
            dot += p1[i] * p2[i];
        }
        
        return dot;
    }
    
    public static float[] normalize(float[] v)
    {
        float[] result = new float[v.length];
        normalize(v, result);
        return v;
    }
    
    public static void normalize(float[] v, float[] result)
    {
        float inv_sqrt = (float)invSqrt(dot(v,v));
        multiply(v, inv_sqrt, result);
    }
    
    public static double[] normalize(double[] v)
    {
        double[] result = new double[v.length];
        normalize(v, result);
        return result;
    }
    
    public static void normalize(double[] v, double[] result)
    {
        double inv_sqrt = invSqrt(dot(v,v));
        multiply(v, inv_sqrt, result);
    }
    
    public static float magnitude(float[] v)
    {
        return (float)Math.sqrt(dot(v, v));
    }
    
    public static double magnitude(double[] v)
    {
        return Math.sqrt(dot(v, v));
    }
    
    public static float tnmin(float a, float b)
    {
        return (a<b) ? a : b;
    }
    
    public static double tnmin(double a, double b)
    {
        return (a<b) ? a : b;
    }
    
    public static double[] cross(double[] a, double[] b)
    {
        double[] result = new double[a.length];
        cross(a, b, result);
        return result;
    }
    
    public static void cross(double[] a, double[] b, double[] result)
    {
        result[0] = a[1] * b[2] - a[2] * b[1];
        result[1] = a[2] * b[0] - a[0] * b[2];
        result[2] = a[0] * b[1] - a[1] * b[0];
    }
    
    public static float[] cross(float[] a, float[] b)
    {
        float[] result = new float[3];
        cross(a, b, result);
        return result;
    }
    
    public static void cross(float[] a, float[] b, float[] result)
    {
        result[0] = a[1] * b[2] - a[2] * b[1];
        result[1] = a[2] * b[0] - a[0] * b[2];
        result[2] = a[0] * b[1] - a[1] * b[0];
    }
    
    public static double degToRad(double d)
    {
        return d * (3.14159265358979323846d / 180.0d);
    }

    public static double radToDeg(double r)
    {
        return r * (180.0d / 3.14159265358979323846d);
    }
    
    public static float radToDeg(float r)
    {
        return r * (180.0f / 3.14159265358979323846f);
    }
    
    public static double invert(double t)
    {
        return 1 / t;
    }
    
    public static float invert(float t)
    {
        return 1 / t;
    }
    
    public static float lerp(float a, float b, float s)
    {
        return a + (b - a) * s;
    }
    
    public static double lerp(double a, double b, double s)
    {
        return a + (b - a) * s;
    }
    
    public static float lerpDeg(float a, float b, float s) {

        a = wrap(a, -180f, 180f);
        b = wrap(b, -180f, 180f);

        return
            (a<b)
            ?
            (((b-a) < 180f) ? lerp(a, b, s) : wrap(lerp(a, b - 360f, s), -180f, 180f))
            :
            (((a-b) < 180f) ? lerp(a, b, s) : wrap(lerp(a, b + 360f, s), -180f, 180f));
    }
    
    public static double lerpDeg(double a, double b, double s) {

        a = wrap(a, -180d, 180d);
        b = wrap(b, -180d, 180d);

        return
            (a<b)
            ?
            (((b-a) < 180d) ? lerp(a, b, s) : wrap(lerp(a, b - 360d, s), -180d, 180d))
            :
            (((a-b) < 180d) ? lerp(a, b, s) : wrap(lerp(a, b + 360d, s), -180d, 180d));
    }
    
    public static float wrap(float a, float min, float max)
    {

//        assert (max > min);

        float d = max - min;
        float s = a - min;
        float q = s / d;
        float m = q - (float)Math.floor(q);
        return m * d + min;
    }
    
    public static double wrap(double a, double min, double max)
    {

//        assert (max > min);

        double d = max - min;
        double s = a - min;
        double q = s / d;
        double m = q - Math.floor(q);
        return m * d + min;
    }
    
    public static int clamp(int a, int min, int max) {

        return (a<min)?min:((a>max)?max:a);
    }
    
    public static double clamp(double a, double min, double max) {

        return (a<min)?min:((a>max)?max:a);
    }
    
    public static float ease(float t) {
        float t_2 = t * t;
        float t_3 = t_2 * t;
        return 3*t_2 - 2*t_3;
    }
    
    public static double ease(double t) {
        double t_2 = t * t;
        double t_3 = t_2 * t;
        return 3*t_2 - 2*t_3;
    }
    
    public static float tnabs(float a)
    {
        return (a>0) ? a : -a;
    }
    
    public static double tnabs(double a)
    {
        return (a>0) ? a : -a;
    }
    
    public static void zero(float[] a)
    {
        for (int i=0; i<a.length; i++)
            a[i] = 0.0f;
    }

    public static void zero(double[] a)
    {
        for (int i=0; i<a.length; i++)
            a[i] = 0.0;
    }
    
    public static double[] toDouble(float[] a)
    {
        double[] r = new double[a.length];
        for (int i=0; i<a.length; i++)
            r[i] = a[i];
        return r;
    }

    public static float[] toFloat(double[] a)
    {
        float[] r = new float[a.length];
        for (int i=0; i<a.length; i++)
            r[i] = (float)a[i];
        return r;
    }

    public static float[] calc2DPos(float[] p,
            Matrixf transformMatrix,
            int screen_width, int screen_height)
    {
        float[] sp = Transform.transform(transformMatrix, p);

        // check to see if clipped in z
        if ((sp[2] > 1.0f) || (sp[2] < -1.0f))
        {
            return null; 
        }

        float x = (sp[0] + 1.0f)/2.0f * screen_width;
        float y = (-sp[1] + 1.0f)/2.0f * screen_height;

        return new float[]{x, y};
    }
    
    public static boolean intersect(float planeA, float planeB, float planeC, float planeD, Rayf ray, float[] result)
    {
        //float[] pn = new float[]{plane.a, plane.b, plane.c};
        //float vd = Pe.dot(pn, ray.direction);
        float vd = planeA * ray.directionX + planeB * ray.directionY + planeC * ray.directionZ;
        if (vd == 0)
            return false;

        //float v0 = -(Pe.dot(pn, new float[]{ray.originX, ray.originY, ray.originZ}) + plane.d);
        float v0 = -(planeA * ray.originX + planeB * ray.originY + planeC * ray.originZ + planeD);
        float t = v0 / vd;

        if (t < 0)
            return false;
        
        //Pe.add(ray.origin, Pe.multiply(ray.direction, t));
        result[0] = ray.originX + ray.directionX * t;
        result[1] = ray.originY + ray.directionY * t;
        result[2] = ray.originZ + ray.directionZ * t;
        return true;
    }
    
    public static double[] cloneArray(double[] p)
    {
        if (p == null)
            return null;
        
        double[] result = new double[p.length];
        for (int i=0; i<p.length; i++)
            result[i] = p[i];
        
        return result;
    }

    public static float[] cloneArray(float[] p)
    {
        if (p == null)
            return null;
        
        float[] result = new float[p.length];
        for (int i=0; i<p.length; i++)
            result[i] = p[i];
        
        return result;
    }

    public static TnMapVertex5f4b buildVert(double[] p, double[] t, TnMapColor color)
    {
        TnMapVertex5f4b result = new TnMapVertex5f4b();
        result.x = (float) p[0];
        result.y = (float) p[1];
        result.z = (float) p[2];
        result.s = (float) t[0];
        result.t = (float) t[1];
        result.color = color;
        return result;
    }

    public static TnMapVertex5f4b buildVert(float[] p, float[] t, TnMapColor color)
    {
        TnMapVertex5f4b result = new TnMapVertex5f4b();
        result.x = p[0];
        result.y = p[1];
        result.z = p[2];
        result.s = t[0];
        result.t = t[1];
        result.color = color;
        return result;
    }

    public static TnMapVertex5f4b buildVert(double[] p, double[] t)
    {
        TnMapVertex5f4b result = new TnMapVertex5f4b();
        result.x = (float) p[0];
        result.y = (float) p[1];
        result.z = (float) p[2];
        result.s = (float) t[0];
        result.t = (float) t[1];
        result.color.r = (byte)255;
        result.color.g = (byte)255;
        result.color.b = (byte)255;
        result.color.a = (byte)255;
        return result;
    }

    public static TnMapVertex5f4b buildVert(float[] p, float[] t)
    {
        TnMapVertex5f4b result = new TnMapVertex5f4b();
        result.x = p[0];
        result.y = p[1];
        result.z = p[2];
        result.s = t[0];
        result.t = t[1];
        result.color.r = (byte)255;
        result.color.g = (byte)255;
        result.color.b = (byte)255;
        result.color.a = (byte)255;
        return result;
    }

    public static TnMapVertex3f4b buildVert(double[] p, TnMapColor color)
    {
        TnMapVertex3f4b result = new TnMapVertex3f4b();
        result.x = (float) p[0];
        result.y = (float) p[1];
        result.z = (float) p[2];
        result.color = color;
        return result;
    }

}

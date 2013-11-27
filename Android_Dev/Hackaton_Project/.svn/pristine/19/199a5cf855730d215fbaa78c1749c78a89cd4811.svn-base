/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Quat.java
 *
 */
package com.telenav.map.opengl.java.math;

import com.telenav.tnui.opengles.TnGLUtils;


/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-27
 */
public class Quat
{
    public static class Quatf extends Quat
    {
        public float r, i, j, k;

        public Quatf()
        {
            r = 1;
            i = 0;
            j = 0;
            k = 0;
        }
        
        public Quatf(float nr, float ni, float nj, float nk)
        {
            r = nr;
            i = ni;
            j = nj;
            k = nk;
        }
        
        public Quatf(Quatf that)
        {
            r = that.r;
            i = that.i;
            j = that.j;
            k = that.k;
        }
        
        public Quatf(float radians, float[] axis)
        {
            float dst_l = (float) Math.sin(radians / 2f); // should be
            float src_l = (float) Pe.magnitude(axis); // actually is
            if (src_l <= Float.MIN_VALUE)
            {
                i = 0;
                j = 0;
                k = 0;
                r = 1;
            }
            else
            {
                float s = dst_l / src_l;
                i = axis[0] * s;
                j = axis[1] * s;
                k = axis[2] * s;
                r = (float) Math.cos(radians / 2f);
            }
        }
        
        public Quatf(float[] a, float[] b) 
        {

            /*
              This is undefined when a and b are parallel. Should check for
              degeneracy before normalizing at least in the case of a and b
              pointing in the same direction. (This routine actually has
              behaved very well in practice.)
            */

            float[] x = Pe.normalize(a);
            float[] z = Pe.normalize(Pe.cross(a,b));
            float[] y = Pe.normalize(Pe.cross(z,x));

            float[] v = Pe.normalize(b);

            float x_c = Pe.dot(v,x);
            float y_c = Pe.dot(v,y);

            float theta = (float)TnGLUtils.getInstance().atan2(y_c,x_c);

            float l = (float)Math.sin(theta/2f);
            i = z[0] * l;
            j = z[1] * l;
            k = z[2] * l;
            r = (float)Math.cos(theta/2f);
        }
        
        public static Quatf multiply(Quatf a, Quatf b)
        {
            return new Quatf(a.r * b.r - a.i * b.i - a.j * b.j - a.k * b.k, a.j * b.k - b.j * a.k + a.r * b.i + b.r * a.i, a.k
                    * b.i - b.k * a.i + a.r * b.j + b.r * a.j, a.i * b.j - b.i * a.j + a.r * b.k + b.r * a.k);
        }
        
        public static Quatf divide(Quatf a, Quatf b)
        {
            return multiply(a, invert(b));
        }
        
        public static Quatf conjugate(Quatf q)
        {
            return new Quatf(q.r, -q.i, -q.j, -q.k);
        }
        
        public static Quatf normalize(Quatf q)
        {
            float lsq = q.r * q.r + q.i * q.i + q.j * q.j + q.k * q.k;
            if (lsq <= Float.MIN_VALUE)
            {
                return new Quatf(1, 0, 0, 0);
            }
            else
            {
                float l = (float) Math.sqrt(lsq);
                return new Quatf(q.r / l, q.i / l, q.j / l, q.k / l);
            }
        }
        
        public static Quatf slerp(Quatf a, Quatf b, float u)
        {
            float very_small = Float.MIN_VALUE;

            float costheta = a.r * b.r + a.i * b.i + a.j * b.j + a.k * b.k;

            float theta = (float) TnGLUtils.getInstance().acos(costheta);
            float sintheta = (float) Math.sin(theta);

            float s0, s1;
            if (sintheta > very_small)
            {
                s0 = (float) Math.sin((1 - u) * theta) / sintheta;
                s1 = (float) Math.sin(u * theta) / sintheta;
            }
            else
            {
                s0 = (1 - u);
                s1 = u;
            }

            return new Quatf(s0 * a.r + s1 * b.r, s0 * a.i + s1 * b.i, s0 * a.j + s1 * b.j, s0 * a.k + s1 * b.k);
        }
        
        public static boolean isIdentity(Quatf q)
        {

            return (q.r == 1f) && (q.i == 0f) && (q.j == 0f) && (q.k == 0f);
        }
        
        public static Quatf invert(Quatf q)
        {
            float norm = (float) Math.sqrt(q.r * q.r + q.i * q.i + q.j * q.j + q.k * q.k);

            return new Quatf(q.r / norm, -q.i / norm, -q.j / norm, -q.k / norm);
        }
    }
    
    public static class Quatd extends Quat
    {
        public double r, i, j, k;

        public Quatd()
        {
            r = 1;
            i = 0;
            j = 0;
            k = 0;
        }
        
        public Quatd(double nr, double ni, double nj, double nk)
        {
            r = nr;
            i = ni;
            j = nj;
            k = nk;
        }
        
        public Quatd(Quatd that)
        {
            r = that.r;
            i = that.i;
            j = that.j;
            k = that.k;
        }
        
        public Quatd(double radians, double[] axis)
        {
            double dst_l =  Math.sin(radians / 2f); // should be
            double src_l =  Pe.magnitude(axis); // actually is
            if (src_l <= Double.MIN_VALUE)
            {
                i = 0;
                j = 0;
                k = 0;
                r = 1;
            }
            else
            {
                double s = dst_l / src_l;
                i = axis[0] * s;
                j = axis[1] * s;
                k = axis[2] * s;
                r =  Math.cos(radians / 2f);
            }
        }
        
        public Quatd(double[] a, double[] b) 
        {

            /*
              This is undefined when a and b are parallel. Should check for
              degeneracy before normalizing at least in the case of a and b
              pointing in the same direction. (This routine actually has
              behaved very well in practice.)
            */

            double[] x = Pe.normalize(a);
            double[] z = Pe.normalize(Pe.cross(a,b));
            double[] y = Pe.normalize(Pe.cross(z,x));

            double[] v = Pe.normalize(b);

            double x_c = Pe.dot(v,x);
            double y_c = Pe.dot(v,y);

            double theta = TnGLUtils.getInstance().atan2(y_c,x_c);

            double l = Math.sin(theta/2f);
            i = z[0] * l;
            j = z[1] * l;
            k = z[2] * l;
            r = Math.cos(theta/2f);
        }
        
        public static Quatd multiply(Quatd a, Quatd b)
        {
            return new Quatd(a.r * b.r - a.i * b.i - a.j * b.j - a.k * b.k, a.j * b.k - b.j * a.k + a.r * b.i + b.r * a.i, a.k
                    * b.i - b.k * a.i + a.r * b.j + b.r * a.j, a.i * b.j - b.i * a.j + a.r * b.k + b.r * a.k);
        }
        
        public static Quatd divide(Quatd a, Quatd b)
        {
            return multiply(a, invert(b));
        }
        
        public static Quatd conjugate(Quatd q)
        {
            return new Quatd(q.r, -q.i, -q.j, -q.k);
        }
        
        public static Quatd normalize(Quatd q)
        {
            double lsq = q.r * q.r + q.i * q.i + q.j * q.j + q.k * q.k;
            if (lsq <= Double.MIN_VALUE)
            {
                return new Quatd(1, 0, 0, 0);
            }
            else
            {
                double l =  Math.sqrt(lsq);
                return new Quatd(q.r / l, q.i / l, q.j / l, q.k / l);
            }
        }
        
        public static Quatd slerp(Quatd a, Quatd b, double u)
        {
            double very_small = Double.MIN_VALUE;

            double costheta = a.r * b.r + a.i * b.i + a.j * b.j + a.k * b.k;

            double theta =  TnGLUtils.getInstance().acos(costheta);
            double sintheta =  Math.sin(theta);

            double s0, s1;
            if (sintheta > very_small)
            {
                s0 =  Math.sin((1 - u) * theta) / sintheta;
                s1 =  Math.sin(u * theta) / sintheta;
            }
            else
            {
                s0 = (1 - u);
                s1 = u;
            }

            return new Quatd(s0 * a.r + s1 * b.r, s0 * a.i + s1 * b.i, s0 * a.j + s1 * b.j, s0 * a.k + s1 * b.k);
        }
        
        public static boolean isIdentity(Quatd q)
        {

            return (q.r == 1f) && (q.i == 0f) && (q.j == 0f) && (q.k == 0f);
        }
        
        public static Quatd invert(Quatd q)
        {
            double norm =  Math.sqrt(q.r * q.r + q.i * q.i + q.j * q.j + q.k * q.k);

            return new Quatd(q.r / norm, -q.i / norm, -q.j / norm, -q.k / norm);
        }
    }
}

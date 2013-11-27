/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Transform.java
 *
 */
package com.telenav.map.opengl.java.math;

import com.telenav.map.opengl.java.math.Quat.Quatd;
import com.telenav.map.opengl.java.math.Quat.Quatf;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-27
 */
public class Transform
{
    public static float[] transform(Quatf q, float[] p)
    {
        Quatf qp = new Quatf(0, p[0], p[1], p[2]);
        Quatf rp = Quatf.multiply(Quatf.multiply(q, qp), Quatf.conjugate(q));
        return new float[]{rp.i, rp.j, rp.k};
    }

    public static double[] transform(Quatd q, double[] p)
    {
        Quatd qp = new Quatd(0, p[0], p[1], p[2]);
        Quatd rp = Quatd.multiply(Quatd.multiply(q, qp), Quatd.conjugate(q));
        return new double[]{rp.i, rp.j, rp.k};
    }

    public static void transform(Matrixf m, float[] p, float[] result)
    {
        float w = m.m[3] * p[0] + m.m[7] * p[1] + m.m[11] * p[2] + m.m[15];
        float inv_w = Pe.invert(w);

        result[0] = (m.m[0] * p[0] + m.m[4] * p[1] + m.m[8] * p[2] + m.m[12]) * inv_w;
        result[1] = (m.m[1] * p[0] + m.m[5] * p[1] + m.m[9] * p[2] + m.m[13]) * inv_w;
        result[2] = (m.m[2] * p[0] + m.m[6] * p[1] + m.m[10] * p[2] + m.m[14]) * inv_w;
    }
    
    public static float[] transform(Matrixf m, float[] p)
    {
        float[] result = new float[3];

        transform(m, p, result);
        
        return result;
    }
    
    public static float[] transformVec(Matrixf m, float[] v)
    {
        float[] result = new float[3];
        result[0] = m.m[0] * v[0] + m.m[4] * v[1] + m.m[8] * v[2];
        result[1] = m.m[1] * v[0] + m.m[5] * v[1] + m.m[9] * v[2];
        result[2] = m.m[2] * v[0] + m.m[6] * v[1] + m.m[10] * v[2];
        return result;
    }
    
    public static double[] transform(Matrixf m, double[] p)
    {
        double[] result = new double[3];

        double w = m.m[3] * p[0] + m.m[7] * p[1] + m.m[11] * p[2] + m.m[15];
        double inv_w = Pe.invert(w);

        result[0] = (m.m[0] * p[0] + m.m[4] * p[1] + m.m[8] * p[2] + m.m[12]) * inv_w;
        result[1] = (m.m[1] * p[0] + m.m[5] * p[1] + m.m[9] * p[2] + m.m[13]) * inv_w;
        result[2] = (m.m[2] * p[0] + m.m[6] * p[1] + m.m[10] * p[2] + m.m[14]) * inv_w;

        return result;
    }

    public static double[] transformVec(Matrixf m, double[] v)
    {
        double[] result = new double[3];
        result[0] = m.m[0] * v[0] + m.m[4] * v[1] + m.m[8] * v[2];
        result[1] = m.m[1] * v[0] + m.m[5] * v[1] + m.m[9] * v[2];
        result[2] = m.m[2] * v[0] + m.m[6] * v[1] + m.m[10] * v[2];
        return result;
    }
}

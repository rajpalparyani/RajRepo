/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Lookat.java
 *
 */
package com.telenav.map.opengl.java.math;


/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-26
 */
public class Lookat
{
    public static class Lookatf extends Lookat
    {
        public float[] eye;
        public float[] obj;
        public float[] up;

        public Lookatf(float[] e, float[] o, float[] u)
        {
            eye = e;
            obj = o;
            up = u;
        }
    }
    
    public static class Lookatd extends Lookat
    {
        public double[] eye;
        public double[] obj;
        public double[] up;

        public Lookatd(double[] e, double[] o, double[] u)
        {
            eye = e;
            obj = o;
            up = u;
        }
    }
}

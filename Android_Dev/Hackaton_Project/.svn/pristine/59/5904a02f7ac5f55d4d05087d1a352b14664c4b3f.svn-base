/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Edge.java
 *
 */
package com.telenav.map.opengl.java.math;

import java.util.Vector;

import com.telenav.map.opengl.java.Pair;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class Edge
{
    public Vector points;

    public Pair segment;

    public Edge()
    {
        points = new Vector();
    }

    public float distance(Pair segment, float[] point)
    {
        float[] v0 = Pe.sub(point, (float[])segment.first);
        float[] v1 = Pe.sub(point, (float[])segment.second);

        float[] v2 = Pe.sub((float[])segment.second, (float[])segment.first);

        float d = Pe.magnitude(Pe.cross(v0, v1))/Pe.magnitude(v2);

        return d;
    }
    
    public double distance(Pair segment, double[] point)
    {
        double[] v0 = Pe.sub(point, (double[])segment.first);
        double[] v1 = Pe.sub(point, (double[])segment.second);

        double[] v2 = Pe.sub((double[])segment.second, (double[])segment.first);

        double d = Pe.magnitude(Pe.cross(v0, v1))/Pe.magnitude(v2);

        return d;
    }
    
    public static class Graph
    {
        public Vector edges;

        public Pair segment;

        public void newEdge()
        {
            if(edges == null)
            {
                edges = new Vector();
            }
            edges.addElement(new Edge());
        }

        public void addPoint(float[] p)
        {
            ((Edge) edges.lastElement()).points.addElement(p);
        }
        
        public void addPoint(double[] p)
        {
            ((Edge) edges.lastElement()).points.addElement(p);
        }
    }
}

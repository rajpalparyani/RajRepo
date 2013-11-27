/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Triangulator.java
 *
 */
package com.telenav.map.opengl.java.math;

import java.util.Vector;

import com.telenav.datatypes.GlobalCoordinateUtil;
import com.telenav.datatypes.map.AbstractVectorEdge;
import com.telenav.datatypes.map.VectorMapEdge;
import com.telenav.map.opengl.java.Pair;
import com.telenav.tnui.opengles.TnGLUtils;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class Triangulator
{
//    public static Vector toSegments(Vector points, double break_angle)
//    {
//        Vector edges = new Vector();
//
//        if (points.isEmpty())
//            return edges;
//
//        edges.add(new Vector());
//
//        double[] prev_v = null;
//        
//        for (int i = 0; i < points.size()-1; i++)
//        {
//            double[] p1 = (double[]) points.elementAt(i);
//            double[] p2 = (double[]) points.elementAt(i+1);
//            double[] v1 = Pe.normalize(Pe.sub(p2, p1));
//            double theta = 0;
//            if (prev_v != null)
//            {
//                theta = Pe.radToDeg(Pe.dot(v1, prev_v));
//            }
//            
//            if (Math.abs(theta) > break_angle)
//            {
//                ((Vector) edges.lastElement()).addElement(new Pair(p1, new double[3]));
//                edges.addElement(new Vector());
//                ((Vector) edges.lastElement()).addElement(new Pair(p1, v1));
//            }
//            else
//            {
//                ((Vector) edges.lastElement()).addElement(new Pair(p1, v1));
//            }
//            prev_v = v1;
//        }
//        double[] p1 = (double[]) points.lastElement();
//        ((Vector) edges.lastElement()).addElement(new Pair(p1, new double[3]));
//        
//        return edges;
//    }

    private static void addFirstPoint(double[] p, double[] v, double width, Vector result)
    {
        double vw0 = v[0] * width;
        double vw1 = v[1] * width;
        double vw2 = v[2] * width;

        double vl0 = vw0;
        double vl1 = vw1;
        double vl2 = vw2;
        
        double[] p0 = new double[3];
        p0[0] = p[0] - vl0 - vw1;
        p0[1] = p[1] - vl1 + vw0;
        p0[2] = p[2] - vl2 - vw2;
        
        double[] p1 = new double[3];
        p1[0] = p[0] - vl0 + vw1;
        p1[1] = p[1] - vl1 - vw0;
        p1[2] = p[2] - vl2 + vw2;
        
        double[] p2 = new double[3];
        p2[0] = p[0] - vw1;
        p2[1] = p[1] + vw0;
        p2[2] = p[2] - vw2;
        
        double[] p3 = new double[3];
        p3[0] = p[0] + vw1;
        p3[1] = p[1] - vw0;
        p3[2] = p[2] + vw2;
        
        result.addElement(new Pair(p0, new double[]{0.0, 0.0})); // degenerate point to start strip
        result.addElement(new Pair(p0, new double[]{0.0, 0.0}));
        result.addElement(new Pair(p1, new double[]{1.0, 0.0}));
        result.addElement(new Pair(p2, new double[]{0.0, 0.5}));
        result.addElement(new Pair(p3, new double[]{1.0, 0.5}));
    }
    
    private static void addLastPoint(double[] p, double[] prev_v, double width, Vector result)
    {
        double vw0 = prev_v[0] * width;
        double vw1 = prev_v[1] * width;
        double vw2 = prev_v[2] * width;
        
        double vl0 = vw0;
        double vl1 = vw1;
        double vl2 = vw2;
        
        double[] p0 = new double[3];
        p0[0] = p[0] - vw1;
        p0[1] = p[1] + vw0;
        p0[2] = p[2] - vw2;
            
        double[] p1 = new double[3];
        p1[0] = p[0] + vw1;
        p1[1] = p[1] - vw0;
        p1[2] = p[2] + vw2;
            
        double[] p2 = new double[3];
        p2[0] = p[0] + vl0 - vw1;
        p2[1] = p[1] + vl1 + vw0;
        p2[2] = p[2] + vl2 - vw2;
            
        double[] p3 = new double[3];
        p3[0] = p[0] + vl0 + vw1;
        p3[1] = p[1] + vl1 - vw0;
        p3[2] = p[2] + vl2 + vw2;
            
        result.addElement(new Pair(p0, new double[]{0.0, 0.5}));
        result.addElement(new Pair(p1, new double[]{1.0, 0.5}));
        result.addElement(new Pair(p2, new double[]{0.0, 1.0}));
        result.addElement(new Pair(p3, new double[]{1.0, 1.0}));
        result.addElement(new Pair(p3, new double[]{1.0, 1.0})); // degenerate
    }
    
    private static void addMiddlePoint(double[] p, double[] v, double[] v_next, double width, Vector result)
    {
        // Get perpendicular.
        // This finds the cosine of half the angle between edges.
        double sn0 = v[1] + v_next[1];
        double sn1 = -v[0] - v_next[0];
        double sn2 = v[2] + v_next[2];
        double vv = Math.sqrt(sn0*sn0 + sn1*sn1 + sn2*sn2);
        sn0 /= vv;
        sn1 /= vv;
        sn2 /= vv;

        double cos_theta = v[1] * sn0 - v[0] * sn1 + v[2] * sn2;
        
        double js0 = sn0 * width / cos_theta;
        double js1 = sn1 * width / cos_theta;
        double js2 = sn2 * width / cos_theta;
        double[] pp0 = new double[3];
        pp0[0] = p[0] - js0;
        pp0[1] = p[1] - js1;
        pp0[2] = p[2] - js2;
        
        double[] pp1 = new double[3];
        pp1[0] = p[0] + js0;
        pp1[1] = p[1] + js1;
        pp1[2] = p[2] + js2;
        
        result.addElement(new Pair(pp0, new double[]{0.0, 0.5}));
        result.addElement(new Pair(pp1, new double[]{1.0, 0.5}));
    }

    public static Vector edgeToTriStrip(Vector points, double width, double break_angle)
    {

        Vector result = new Vector();

        Vector edges = new Vector();

        if (points.size() < 2)
            return edges;

        edges.addElement(new Vector());

        double[] prev_v = null;
        
        for (int i = 0; i < points.size()-1; i++)
        {
            double[] p1 = (double[]) points.elementAt(i);
            double[] p2 = (double[]) points.elementAt(i+1);
            double[] v1 = Pe.normalize(Pe.sub(p2, p1));
            double theta = 0;
            if (prev_v != null)
            {
                theta = Pe.radToDeg(Pe.dot(v1, prev_v));
            }
            
            if (Math.abs(theta) > break_angle)
            {
                addLastPoint(p1, prev_v, width, result);
                
                edges.addElement(new Vector());
                
                addFirstPoint(p1, v1, width, result);
            }
            else
            {
                if (i == 0)
                {
                    addFirstPoint(p1, v1, width, result);
                }
                else
                {
                    addMiddlePoint(p1, prev_v, v1, width, result);
                }
            }
            prev_v = v1;
        }
        double[] p1 = (double[]) points.lastElement();
        addLastPoint(p1, prev_v, width, result);

        return result;
    }

    public static Vector edgeToTriStrip(AbstractVectorEdge edge, double[] origin, double width, double break_angle)
    {
        Vector result = new Vector();

        Vector edges = new Vector();

        if (edge.getShapePointsSize() < 2)
            return edges;

        edges.addElement(new Vector());

        double[] prev_v = new double[3];
        double[] v1 = new double[3];
        
        double[] p1 = new double[3];
        double[] p2 = new double[3];
        
        for (int i = 0; i < edge.getShapePointsSize()-1; i++)
        {
            p1[0] = edge.lonAt(i) - origin[0];
            p1[1] = GlobalCoordinateUtil.GLOBAL_LENGTH - edge.latAt(i) - origin[1];
            p1[2] = edge.altitudeAt(i) - origin[2];
            p2[0] = edge.lonAt(i+1) - origin[0];
            p2[1] = GlobalCoordinateUtil.GLOBAL_LENGTH - edge.latAt(i+1) - origin[1];
            p2[2] = edge.altitudeAt(i+1) - origin[2];
            Pe.normalize(Pe.sub(p2, p1), v1);
            double theta = 0;
            if (i > 0)
            {
                theta = Pe.radToDeg(TnGLUtils.getInstance().acos(Pe.dot(v1, prev_v)));
            }
            
            if (Math.abs(theta) > break_angle)
            {
                addLastPoint(p1, prev_v, width, result);
                
                edges.addElement(new Vector());
                
                addFirstPoint(p1, v1, width, result);
            }
            else
            {
                if (i == 0)
                {
                    addFirstPoint(p1, v1, width, result);
                }
                else
                {
                    addMiddlePoint(p1, prev_v, v1, width, result);
                }
            }
            prev_v[0] = v1[0];
            prev_v[1] = v1[1];
            prev_v[2] = v1[2];
        }
        p1[0] = edge.lonAt(edge.getShapePointsSize()-1) - origin[0];
        p1[1] = GlobalCoordinateUtil.GLOBAL_LENGTH - edge.latAt(edge.getShapePointsSize()-1) - origin[1];
        p1[2] = edge.altitudeAt(edge.getShapePointsSize()-1) - origin[2];
        addLastPoint(p1, prev_v, width, result);

        return result;
    }

//    public static Vector edgeToDashed(Vector points, double width, double break_angle)
//    {
//
//        Vector result = new Vector();
//        // @TODO: These should be in configuration file.
//        double on_length = width * 3.0;
//        double off_length = width * 2.0;
//
//        Vector edges = toSegments(Poly.dash(points, on_length, off_length), break_angle);
//
//        for (int i = 0; i < edges.size(); i++)
//        {
//
//            Vector edge = (Vector) edges.elementAt(i);
//
//            if (edge.size() >= 2)
//            {
//
//                for (int j = 0; j < edge.size(); j++)
//                {
//
//                    Pair pair = (Pair) edge.elementAt(j);
//                    double[] p = (double[]) pair.first;
//                    double[] v = (double[]) pair.second;
//
//                    double[] vw = Pe.multiply(v, width);
//                    double[] s = new double[]{v[1], -v[0], v[2]}; // Get perpendicular (side).
//                    double[] sw = Pe.multiply(s, width);
//
//                    double[] p0 = Pe.sub(Pe.sub(p, vw), sw);
//                    double[] p1 = Pe.add(Pe.sub(p, vw), sw);
//
//                    double[] p2 = Pe.sub(p, sw);
//                    double[] p3 = Pe.add(p, sw);
//
//                    ++j;
//                    int count = 0;
//
//                    while ((j + 1) != edge.size())
//                    {
//                        pair = (Pair) edge.elementAt(j);
//                        // Get edge direction.
//                        double[] v_next = (double[]) pair.second;
//
//                        // Get perpendicular.
//                        double[] s_next = new double[]{v_next[1], -v_next[0], v_next[2]};
//
//                        // This finds the cosine of half the angle between edges.
//                        double cos_theta = Pe.dot(s, Pe.normalize(Pe.add(s, s_next)));
//
//                        double[] joint_s = Pe.multiply(Pe.multiply(Pe.normalize(Pe.add(s, s_next)), width), Pe
//                                .invert(cos_theta));
//
//                        double[] pp0 = Pe.sub(p, joint_s);
//                        double[] pp1 = Pe.add(p, joint_s);
//
//                        count += 1;
//                        if ((count % 2) == 1)
//                        {
//                            result.add(new Pair(pp0, new double[]{0.0, 0.5}));
//
//                        }
//
//                        result.add(new Pair(pp0, new double[]{0.0, 0.5}));
//                        result.add(new Pair(pp1, new double[]{1.0, 0.5}));
//
//                        if ((count % 2) == 0)
//                        {
//                            result.add(new Pair(pp1, new double[]{1.0, 0.5}));
//                        }
//
//                        v = v_next;
//                        s = s_next;
//                        ++j;
//                    }
//                    // last point
//                    pair = (Pair) edge.elementAt(j);
//                    p = (double[]) pair.first;
//                    vw = Pe.multiply(v, width);
//                    s = new double[]{v[1], -v[0], v[2]};
//                    sw = Pe.multiply(s, width);
//
//                    p0 = Pe.sub(p, sw);
//                    p1 = Pe.add(p, sw);
//
//                    p2 = Pe.sub(Pe.add(p, vw), sw);
//                    p3 = Pe.add(Pe.add(p, vw), sw);
//
//                }
//            }
//        }
//        return result;
//    }
    
    public static Vector polygonToTriangles(VectorMapEdge polygon, double[] origin)
    {
        Vector result = new Vector();
        if (polygon.getShapePointsSize() < 3)
            return result;
        
        double[][] points = new double[polygon.getShapePointsSize()][3];
        for (int i=0; i<polygon.getShapePointsSize(); i++)
        {
            points[i][0] = polygon.lonAt(i) - origin[0]; 
            points[i][1] = GlobalCoordinateUtil.GLOBAL_LENGTH - polygon.latAt(i) - origin[1]; 
            points[i][2] = polygon.altitudeAt(i) - origin[2]; 
        }
        Vector r = PolygonTriangulator.convertToTriangles(points.length, points);
        if (r == null)
            return result;
        
        for (int j=0; j<r.size(); j++)
            result.addElement(r.elementAt(j));
        return result;
    }
}

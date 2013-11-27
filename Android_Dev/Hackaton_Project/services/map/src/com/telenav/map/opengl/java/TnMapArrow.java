/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapArrow.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.math.Edge;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Triangulator;
import com.telenav.map.opengl.java.math.Edge.Graph;
import com.telenav.nio.TnNioManager;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapArrow
{
    public final static double maxEdgeSegmentAngle = 45.0f;

    boolean enabled;

    boolean u_turn;

    Vector m_edges = new Vector();

    double[] origin;

    int m_segment;

    Hashtable m_vboMap = new Hashtable();

    TnGL10 gl10;

    public TnMapArrow(TnGL10 gl10, Vector edges, double[] origin, int segment)
    {
        this.gl10 = gl10;
        enabled = false;
        u_turn = false;
        m_edges = edges;
        this.origin = origin;
        m_segment = segment;
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

    public static TnMapVertex5f4b buildVert(double[] p, float s, float t, TnMapColor color)
    {
        TnMapVertex5f4b result = new TnMapVertex5f4b();
        result.x = (float) p[0];
        result.y = (float) p[1];
        result.z = (float) p[2];
        result.s = (float) s;
        result.t = (float) t;
        result.color = color;
        return result;
    }

    //IdAndCount<int[], int>
    public Pair buildArrow(int zoom)
    {

        // @TODO: This code to set margin and lineWidth is just coppied
        // directly out of TnMapEdge::BuildPrimitives. This logic should
        // probably be centralized.

        float arrow_width = TnMapConf.getFeatureWidth(TnMapConf.FEATURE_TYPE_ARROW, zoom);
        float width = arrow_width * (float) (1 << zoom);

        Graph arrow_edges = handleUTurns(buildSegmentEdges(width * 3.0f, width * 4.0f), width);

        TnMapColor color = TnMapConf.getFeatureColor(TnMapConf.FEATURE_TYPE_ARROW);
        TnMapColor border_color = new TnMapColor();
        if (false /* m_u_turn */)
        {
            // for debugging
            border_color.r = 0x00;
            border_color.g = 0xff;
            border_color.b = 0x00;
            border_color.a = color.a;
        }
        else
        {
            border_color.r = color.r / 2;
            border_color.g = color.g / 2;
            border_color.b = color.b / 2;
            border_color.a = color.a;
        }

        Vector geom_ptr = new Vector();

        buildArrowGeom(geom_ptr, arrow_edges, width * 1.2f, border_color);
        buildArrowGeom(geom_ptr, arrow_edges, width, color);

        int count = geom_ptr.size();
        int size = count * TnMapVertex5f4b.size();
        
        ByteBuffer vbb = TnNioManager.getInstance().allocateDirect(size);
        FloatBuffer data = vbb.asFloatBuffer();
        for(int i = 0; i < count; i++)
        {
            TnMapVertex5f4b vertex = (TnMapVertex5f4b)geom_ptr.elementAt(i);
            data.put(vertex.x);
            data.put(vertex.y);
            data.put(vertex.z);
            data.put(vertex.color.r);
            data.put(vertex.color.g);
            data.put(vertex.color.b);
            data.put(vertex.color.a);
            data.put(vertex.y);
            data.put(vertex.z);
            data.put(vertex.s);
            data.put(vertex.t);
        }
        data.position(0);

        data.clear();
        
        return new Pair(vbb, new Integer(count));
    }

    public void buildArrowGeom(Vector geom, Graph arrow_edges, float width, TnMapColor color)
    {
        for (int i = 0; i < arrow_edges.edges.size(); i++)
        {
            Edge edge = (Edge) arrow_edges.edges.elementAt(i);

            Vector edge_geom = Triangulator.edgeToTriStrip(edge.points, width, maxEdgeSegmentAngle);

            for (int k = 0; k < edge_geom.size(); ++k)
            {
                Pair p = (Pair) edge_geom.elementAt(k);
                geom.addElement(buildVert((double[]) p.first, (double[]) p.second, color));
            }
        }

        // Add Arrow Head
        {
            double[] v = Pe.multiply(getEndDir(arrow_edges), width * 1.7);
            double[] side = new double[]{v[1], -v[0], v[2]};
            double[] p = getEndPoint(arrow_edges);

            double[] p0 = Pe.sub(Pe.sub(p, side), Pe.multiply(v, 0.3));
            double[] p1 = Pe.sub(Pe.sub(p, Pe.multiply(side, 0.3)), Pe.multiply(v, 0.2));
            double[] p2 = Pe.add(p, v);
            double[] p3 = Pe.add(p, Pe.multiply(v, 0.3));
            double[] p4 = Pe.sub(Pe.add(p, side), Pe.multiply(v, 0.3));
            double[] p5 = Pe.sub(Pe.add(p, Pe.multiply(side, 0.3)), Pe.multiply(v, 0.2));

            geom.addElement(buildVert(p0, 0.5f, 0.0f, color));
            geom.addElement(buildVert(p0, 0.5f, 0.0f, color));
            geom.addElement(buildVert(p1, 0.5f, 0.5f, color));
            geom.addElement(buildVert(p2, 0.5f, 0.0f, color));
            geom.addElement(buildVert(p3, 0.5f, 0.5f, color));
            geom.addElement(buildVert(p4, 0.5f, 0.0f, color));
            geom.addElement(buildVert(p5, 0.5f, 0.5f, color));
            geom.addElement(buildVert(p5, 0.5f, 0.5f, color));
        }
    }

    public Pair getVBOBufferAndCount(int zoom)
    {
        Pair i = (Pair) m_vboMap.get(new Integer(zoom));
        if (i != null)
            return (Pair) i.second;

        Pair bufferAndCount = buildArrow(zoom);
        m_vboMap.put(new Integer(zoom), bufferAndCount);

        return bufferAndCount;
    }

    public void destroy()
    {
//        System.out.println("*****TnMapArrow destroy-----");
    }

    public void enable()
    {
        enabled = true;
    }

    public void disable()
    {
        enabled = false;
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }

    /*
     * This takes a vector of Edges, an index into it, and a length. It builds a new vector of edges copied from the
     * original, but with its center at the specified index, and extending 'length' in either direction. It assumes the
     * edges are geometrically contiguous. (It is also responsible for transforming into origin-local space.)
     */

    public Graph buildSegmentEdges(double length_back, double length_forward)
    {
        Vector lines = new Vector();

        if ((m_segment > 0) && (m_segment < m_edges.size()))
        {
            // Backwards from index. Need a signed int for that.
            int i = m_segment - 1;
            double to_go = length_back;
            for (; (i >= 0) && (to_go > 0.0); --i)
            {
                Edge edge = (Edge) m_edges.elementAt(i);
                lines.addElement(new Vector());
                int j = edge.points.size() - 1;
                if ((j > 0) && (to_go > 0.0))
                {
                    double[] p0 = (double[]) edge.points.elementAt(j);
                    ((Vector) lines.firstElement()).insertElementAt(p0, 0);
                    --j;
                    for (; (j >= 0) && (to_go > 0.0); --j)
                    {
                        double[] p1 = (double[]) edge.points.elementAt(j);
                        double[] v = Pe.sub(p1, p0);
                        double m = Pe.magnitude(v);
                        if (m > to_go)
                        {
                            double[] new_p = Pe.add(p0, Pe.multiply(v, to_go / m));
                            ((Vector) lines.firstElement()).insertElementAt(new_p, 0);
                        }
                        else
                        {
                            ((Vector) lines.firstElement()).insertElementAt(p1, 0);
                        }
                        to_go -= m;
                        p0 = p1;
                    }
                }
            }
        }

        {
            // Forwards from index.
            int i = m_segment;
            int size = m_edges.size();
            double to_go = length_forward;

            for (; (i < size) && (to_go > 0.0); ++i)
            {
                Edge edge = (Edge) m_edges.elementAt(i);
                lines.addElement(new Vector());
                int j = 0;
                if ((edge.points.size() > 1) && (to_go > 0.0))
                {
                    double[] p0 = (double[]) edge.points.elementAt(j);
                    ((Vector) lines.lastElement()).addElement(p0);
                    ++j;
                    for (; (j < edge.points.size()) && (to_go > 0.0); ++j)
                    {
                        double[] p1 = (double[]) edge.points.elementAt(j);
                        double[] v = Pe.sub(p1, p0);
                        double m = Pe.magnitude(v);
                        if (m > to_go)
                        {
                            double[] new_p = Pe.add(p0, Pe.multiply(v, to_go / m));
                            ((Vector) lines.lastElement()).addElement(new_p);
                        }
                        else
                        {
                            ((Vector) lines.lastElement()).addElement(p1);
                        }
                        to_go -= m;
                        p0 = p1;
                    }
                }
            }
        }

        /*
         * Copy it to a proper vector-based Edge type and translate to origin-local.
         */

        Graph result = new Graph();
        for (int i = 0; i < lines.size(); i++)
        {
            result.newEdge();

            Vector line = (Vector) lines.elementAt(i);
            for (int j = 0; j < line.size(); j++)
            {
                result.addPoint(Pe.sub((double[]) line.elementAt(j), origin));
            }
        }

        return result;
    }

    public double[] getEndPoint(Graph graph)
    {
        Vector i = graph.edges;
        if (i != null && !i.isEmpty())
        {
            Edge edge = (Edge) i.firstElement();
            if (edge.points != null && !edge.points.isEmpty())
            {
                return (double[]) edge.points.firstElement();
            }
        }

        return new double[]{0.0, 0.0, 0.0};
    }

    public double[] getEndDir(Graph graph)
    {
        Vector i = graph.edges;
        if (i != null && !i.isEmpty())
        {
            Edge edge = (Edge) i.firstElement();
            Pair s = edge.segment;
            if (s != null)
            {
                return Pe.normalize(Pe.sub((double[]) s.first, (double[]) s.second));
            }
        }

        return new double[]{0.0, 0.0, 0.0};
    }

    /*
     * Massage turn arrow edges to take into account u-turns.
     */

    public Graph handleUTurns(Graph graph, double width)
    {
        u_turn = false;
        int turn_here = 0;
        double[] shift = new double[]{0.0, 0.0, 0.0};// FIXME
         //#if 1
         // {
         // // Find point at which the arrow turns back on itself (if it
         // // does).
         // Graphd::iterator it = graph->end();
         //
         // Graphd::iterator i = graph->begin();
         // if (i != graph->end())
         // {
         // Edged::Segment prev = *i;
         // ++i;
         // for (;
         // (i != graph->end()) && (it == graph->end());
         // ++i)
         // {
         // Edged::Segment next = *i;
         // Vec3d v_prev = prev.second - prev.first;
         // Vec3d v_next = next.second - next.first;
         //#if 0
         // /*
         // Tried deciding that an arrow had turned back on
         // itself based on whether the geometry would overlap,
         // but that had some false positives when there were
         // short edge segments. For now just use the angle
         // between segments. Needs exercising with real data,
         // but u-turns are rare.
         // */
         //
         // if (dot(v_next, v_prev) < 0.0) &&
         // (magnitude(v_next) > width) &&
         // (distance(prev, next.second) < width))
         //#else
         // double u_turn_angle = 160.0;
         //
         // if (radToDeg(angle(v_next, v_prev)) > u_turn_angle)
         //#endif
         // {
         // m_u_turn = true;
         // turn_here = i.edge_iter.i0;
         // Vec3d dir = normalize(v_next - v_prev);
         // shift = Vec3<double>(-dir[1], dir[0], 0.0) * 0.7 * width;
         // //TnMapLogError("Arrow turns back on itself.\n");
         // } else {
         // prev = next;
         // }
         //
         // }
         // }
         // }
         //#endif
        //
        Graph result = new Graph();
        boolean before_u_turn = true;
        for (int i = 0; i < result.edges.size(); i++)
        {
            result.newEdge();

            Edge edge = (Edge) result.edges.elementAt(i);
            for (int j = 0; j < edge.points.size(); j++)
            {
                double[] p = (double[]) edge.points.elementAt(j);
                if (u_turn)
                {
                    if (before_u_turn)
                    {
                        result.addPoint(Pe.add(p, shift));
                    }
                    else
                    {
                        result.addPoint(Pe.sub(p, shift));
                    }
                    if (j == turn_here)
                    {
                        before_u_turn = false;
                        result.addPoint(Pe.sub(p, shift));
                    }

                }
                else
                {
                    result.addPoint(p);
                }
            }
        }

        return result;
    }

}

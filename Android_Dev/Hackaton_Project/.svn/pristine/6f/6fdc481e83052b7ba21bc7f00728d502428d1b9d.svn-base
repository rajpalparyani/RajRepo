/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapRoute.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Triangulator;
import com.telenav.nio.TnNioManager;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-1
 */
public class TnMapRoute
{
    Vector m_edges = new Vector();
    double[] m_startPoint;
    double[] m_finishPoint;
    double[] m_origin;

    Vector m_arrows = new Vector();

    Hashtable m_vboMap = new Hashtable();
    
    TnGL10 gl10;

     // If egde segments at sharper turn than this, start new edge.  @TODO: Put this in configuration.
     double maxEdgeSegmentAngle = 45.0f;


     // @TODO: Replace TnMapVertex5f4b and TnMapVertex3f4b

    public static TnMapVertex5f4b buildVert(double[] p, double[] t, TnMapColor color)
     {
         TnMapVertex5f4b result = new TnMapVertex5f4b();
         result.x = (float)p[0];
         result.y = (float)p[1];
         result.z = (float)p[2];
         result.s = (float)t[0];
         result.t = (float)t[1];
         result.color = color;
         return result;
     }

    public static TnMapVertex5f buildVert(double[] p, double[] t)
     {
         TnMapVertex5f result = new TnMapVertex5f();
         result.x = (float)p[0];
         result.y = (float)p[1];
         result.z = (float)p[2];
         result.s = (float)t[0];
         result.t = (float)t[1];
         return result;
     }


     // Create a route object out of a list of edges and a begin and end point
     public TnMapRoute(TnGL10 gl10, Vector edges, 
             Vector segments,
             double[] start, 
             double[] finish)
     {
         this.gl10 = gl10;
         m_edges = edges;
         m_startPoint = start;
         m_finishPoint = finish;
         if (m_edges != null && 
             !m_edges.isEmpty() &&
             !((Vector)m_edges.firstElement()).isEmpty())
         {
             m_origin = (double[])((Vector)m_edges.firstElement()).firstElement();

         } else {
             // empty route
             Pe.zero(m_origin);
         }

         buildArrows(segments);
     }


     public void setOrigin(double[] origin)
     {
         m_origin = origin;
         m_vboMap.clear();
     }


     public double[] getOrigin()
     {
         return m_origin;
     }


     public double[] getStartPoint()
     {
         return m_startPoint;
     }


     public double[] getFinishPoint()
     {
         return m_finishPoint;
     }


     public void buildArrows(Vector segments)
     {

         for (int i = 0; i < segments.size(); i++)
         {
             int segment = ((Integer)segments.elementAt(i)).intValue();
             m_arrows.addElement(new TnMapArrow(gl10, m_edges, m_origin, segment));
         }
     }
           

     //BufferAndCount<Buffer, int>
     public Pair buildRoute(int zoom)
     {
         // Convert to origin-local

         Vector route_edges = new Vector();

         for (int i = 0; i < m_edges.size(); i++)
         {
             Vector edge = (Vector)m_edges.elementAt(i);
             route_edges.addElement(new Vector());
             for (int j = 0; j < edge.size(); j++)
             {
                 double[] p = Pe.sub((double[])edge.elementAt(j), m_origin);
                 ((Vector)route_edges.lastElement()).addElement(p);
             }
         }


         // @TODO: This code to set margin and lineWidth is just coppied
         // directly out of TnMapEdge::BuildPrimitives. This logic should
         // probably be centralized.

         float routeWidth = TnMapConf.getFeatureWidth(TnMapConf.FEATURE_TYPE_ROUTE, zoom);
         float width = routeWidth * (float)(1 << zoom);

         Vector geom = new Vector();

         for (int i = 0; i < route_edges.size(); i++)
         {
             Vector edge = (Vector)route_edges.elementAt(i);

             Vector edge_geom = Triangulator.edgeToTriStrip(edge, width, maxEdgeSegmentAngle);

             for (int k = 0; k < edge_geom.size(); ++k)
             {
                 Pair p = (Pair) edge_geom.elementAt(k);
                 geom.addElement(buildVert((double[]) p.first, (double[]) p.second));
             }
         }

         int count = geom.size();
         int size = count * TnMapVertex5f.size();
         
         ByteBuffer vbb = TnNioManager.getInstance().allocateDirect(size);
         FloatBuffer data = vbb.asFloatBuffer();

         for(int i = 0; i < count; i++)
         {
             TnMapVertex5f vertex = (TnMapVertex5f)geom.elementAt(i);
             data.put(vertex.x);
             data.put(vertex.y);
             data.put(vertex.z);
             data.put(vertex.s);
             data.put(vertex.t);
         }
         data.position(0);

         return new Pair(vbb, new Integer(count));
     }


     public Pair getVBOBufferAndCount(int zoom)
     {
         Pair i = (Pair) m_vboMap.get(new Integer(zoom));
         if (i != null)
             return i;

         Pair bufferAndCount = buildRoute(zoom);
         m_vboMap.put(new Integer(zoom), bufferAndCount);

         return bufferAndCount;
     }

     public void destroy()
     {
     }


     public boolean enableTurnArrow(int segment)
     {
         if (segment < m_arrows.size())
         {
             ((TnMapArrow)m_arrows.elementAt(segment)).enable();
             return true;
         }
         return false;
     }


     public boolean disableTurnArrow(int segment)
     {
         if (segment < m_arrows.size())
         {
             ((TnMapArrow)m_arrows.elementAt(segment)).disable();
             return true;
         }
         return false;
     }
             

     public boolean enableAllTurnArrows()
     {
         if (m_arrows.isEmpty()) return false;

         for (int i = 0; i < m_arrows.size(); i++)
         {
             ((TnMapArrow)m_arrows.elementAt(i)).enable();
         }
         return true;
     }


     public boolean disableAllTurnArrows()
     {
         if (m_arrows.isEmpty()) return false;

         for (int i = 0; i < m_arrows.size(); i++)
         {
             ((TnMapArrow)m_arrows.elementAt(i)).disable();
         }
         return true;
     }


     public int arrowsCount()
     {
         return m_arrows.size();
     }


     public TnMapArrow getArrow(int index)
     {
         if (index<m_arrows.size())
         {
             return ((TnMapArrow)m_arrows.elementAt(index));
         }
         return null;
     }
}

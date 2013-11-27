/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapTile.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.datatypes.traffic.VectorTrafficEdge;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Triangulator;
import com.telenav.map.opengl.java.proxy.TnMapPickableTrafficIncident;
import com.telenav.nio.TnNioManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnTrafficRenderingData
{
    Tile tile;
    
    double[] m_origin;

    Matrixf m_viewMatrix = new Matrixf();

    BufferElement trafficFlowBuffer;

    IncidentVBO m_incidentVBO = new IncidentVBO();

    // Pickable regions in this tile
    Vector m_pickableRegions = new Vector();// TnMapPickRegion3D

    public static class Incident
    {
        public Incident(double[] _point, int _severity, boolean _laneClosed, int _incidentType, String _crossStreet, String _message,
                String _icon)
        {
            point = _point;
            severity = _severity;
            laneClosed = _laneClosed;
            incidentType = _incidentType;
            crossStreet = _crossStreet;
            message = _message;
            icon = _icon;
        }

        public double[] point;

        public int severity;

        public boolean laneClosed;

        public int incidentType;

        public String crossStreet;

        public String message;

        public String icon;
    };
    
    public TnTrafficRenderingData(Tile tile)
    {
        this.tile = tile;
        m_origin = new double[3];
        TnMapMagic.tileIdToPoint(tile.getId(), m_origin);
    }

    public static TnMapColor getTrafficColor(double speed)
    {

        // @TODO: Current presentation.

        /* double green = 1.0; */
        double yellow = .616667;
        double red = 0.3;

        TnMapColor color = new TnMapColor();

        if ((speed > yellow)) // green
        {
            color = TnMapConf.getTrafficSpeedColor(TnMapConf.TRAFFIC_COLOR_FAST);
        }
        else if ((speed > red) && (speed <= yellow)) // yellow
        {
            color = TnMapConf.getTrafficSpeedColor(TnMapConf.TRAFFIC_COLOR_MEDIUM);
        }
        else if ((speed >= 0.0) && (speed <= red)) // red
        {
            color = TnMapConf.getTrafficSpeedColor(TnMapConf.TRAFFIC_COLOR_SLOW);
        }
        else
        {
            color.r = (byte)0x99;
            color.g = (byte)0x99;
            color.b = (byte)0x99;
            color.a = (byte)0x6F;
        }

        return color;
    }

    public void destroy()
    {
        // @TODO: replace this with a queue in the renderer so it is
        // guaranteed to happen in the render thread.
        trafficFlowBuffer = null;
    }

    public void setViewMatrix(Matrixf viewMatrix)
    {
        m_viewMatrix = new Matrixf(viewMatrix);
    }

    public Matrixf getViewMatrix()
    {
        return m_viewMatrix;
    }

    public void setTrafficEdgeData(VectorTrafficEdge[] edges)
    {
        Vector geom = new Vector();

        // @TODO: Fix this with real road type info somehow.
        // int32_t featureWidth = 10;
        float featureWidth = TnMapConf.getFreewayWidth(tile.getZoom()) * TnMapConf.getFloat(TnMapConf.TRAFFIC_WIDTH_RATIO);

        double width = featureWidth * ((double) (1 << tile.getZoom()));
        
        for (int j = 0; j < edges.length; j++)
        {
            VectorTrafficEdge edge = edges[j];

            if (edge.getShapePointsSize() < 2)
                continue;
            
            TnMapColor color = getTrafficColor(edge.getTrafficSpeed() / 65.0);

            
            // Throw away more data here? Short segments?

            Vector vertices = Triangulator.edgeToTriStrip(edge, m_origin, width, TnMapConf.MAX_EDGE_SEGMENT_ANGLE);

            for (int k = 0; k < vertices.size(); k++)
            {
                Pair tmpPair = (Pair) vertices.elementAt(k);
                geom.addElement(Pe.buildVert((double[]) tmpPair.first, (double[]) tmpPair.second, color));
            }
        }
        
        trafficFlowBuffer = new BufferElement();
        trafficFlowBuffer.layers = new int[1];
        trafficFlowBuffer.layers[0] = geom.size();
        
        if (!geom.isEmpty())
        {
            // New geometry. Build a vbo. This all belongs in
            // a vbo object.
            int geomCount = geom.size();
            
            ByteBuffer pointerbb = TnNioManager.getInstance().allocateDirect(geomCount * 3 * 4);
            FloatBuffer pointerfb = pointerbb.asFloatBuffer();
            
            ByteBuffer texCoodbb = TnNioManager.getInstance().allocateDirect(geomCount * 2 * 4);
            FloatBuffer texCoodfb = texCoodbb.asFloatBuffer();
            
            ByteBuffer colorbb = TnNioManager.getInstance().allocateDirect(geomCount * 4);
            
            for (int n = 0; n < geom.size(); n++)
            {
                TnMapVertex5f4b vertex = (TnMapVertex5f4b) geom.elementAt(n);
                pointerfb.put(vertex.x);
                pointerfb.put(vertex.y);
                pointerfb.put(vertex.z);
                
                colorbb.put((byte)vertex.color.r);
                colorbb.put((byte)vertex.color.g);
                colorbb.put((byte)vertex.color.b);
                colorbb.put((byte)vertex.color.a);
                
                texCoodfb.put(vertex.s);
                texCoodfb.put(vertex.t);
            }
            geom.removeAllElements();
            trafficFlowBuffer.pointerBuffer = pointerbb;
            trafficFlowBuffer.texCoordBuffer = texCoodbb;
            trafficFlowBuffer.colorBuffer = colorbb;
        }
    }

    public void setTrafficIncidentData(Vector incidents)
    {
        // Create geometry

        m_incidentVBO.clear();
        Vector incidentgeom = new Vector();

        for (int i = 0; i < incidents.size(); i++)
        {
            Incident incident = (Incident) incidents.elementAt(i);
            // Default = white text
            TnMapColor color = new TnMapColor();
            color.r = (byte)255;
            color.g = (byte)255;
            color.b = (byte)255;
            color.a = (byte)255;

            // Incident icon geometry
            String icon = incident.icon;

            if (icon != null && icon.trim().length() != 0)
            {
                int begin = incidentgeom.size();

                float width = 25f;
                float height = 22f;

                if ((icon.startsWith("traffic_cam")) || (icon.startsWith("traffic_police")))
                {
                    width = 32f;
                    height = 63f;
                }

                float hw = width * 0.5f;
                float hh = height * 0.5f;

                float[] p0 = new float[]{hw, 0.0f, -hh};
                float[] p1 = new float[]{hw, 0.0f, hh};
                float[] p2 = new float[]{-hw, 0.0f, -hh};
                float[] p3 = new float[]{-hw, 0.0f, hh};

                float[] st0 = new float[]{1.0f, 0.0f};
                float[] st1 = new float[]{1.0f, 1.0f};
                float[] st2 = new float[]{0.0f, 0.0f};
                float[] st3 = new float[]{0.0f, 1.0f};

                // independent triangles

                incidentgeom.addElement(Pe.buildVert(p0, st0));
                incidentgeom.addElement(Pe.buildVert(p2, st2));
                incidentgeom.addElement(Pe.buildVert(p1, st1));

                incidentgeom.addElement(Pe.buildVert(p2, st2));
                incidentgeom.addElement(Pe.buildVert(p3, st3));
                incidentgeom.addElement(Pe.buildVert(p1, st1));

                int end = incidentgeom.size();
                int count = end - begin;
                m_incidentVBO.segments.addElement(new IncidentVBOSegment(incident.point, begin, count, icon));

                // Create pickable data object
                TnMapPickableTrafficIncident data = new TnMapPickableTrafficIncident(incident.severity, incident.laneClosed,
                        incident.incidentType, incident.crossStreet, incident.message);

                // Create 3D pick region an dadd it to our list
                m_pickableRegions.addElement(new TnMapPickRegion3D(incident.point, (int) width, (int) height, (int) hw, (int) hh, data));
            }
        }
        
        for (int i=0; i<incidentgeom.size(); i++)
        m_incidentVBO.geom.addElement(incidentgeom.elementAt(i));
    }

    // Add nearest pickables in the tile to the given map
    // touchedItems:<float,Pickable::const_ptr>
    public void addNearest(Hashtable touchedItems, TnMapCamera camera, int x, int y, boolean onlyEnabled)
    {
        for (int i = 0; i < m_pickableRegions.size(); i++)
        {
            TnMapPickRegion3D pick = (TnMapPickRegion3D) m_pickableRegions.elementAt(i);
            // Get distance to pick region
            float distance = pick.hit(x, y, camera);

            if(touchedItems.containsKey(new Float(distance)))
            {
                Vector v = (Vector)touchedItems.get(new Float(distance));
                v.addElement(pick.getPickable());
            }
            else
            {
                Vector v = new Vector();
                v.addElement(pick.getPickable());
             // Insert into our collection
                touchedItems.put(new Float(distance), v);
            }
        }
    }

    public BufferElement getTrafficFlowBuffer()
    {
        return trafficFlowBuffer;
    }

    public IncidentVBO getIncidentVBO()
    {
        return m_incidentVBO;
    }

    public double[] getOrigin()
    {
        return m_origin;
    }
    
    public static class IncidentVBOSegment
    {
        public IncidentVBOSegment(double[] _pos, int _begin, int _count, String _icon)
        {
            pos = _pos;
            begin = _begin;
            count = _count;
            icon = _icon;
        }

        public double[] pos;

        public int begin;

        public int count;

        public String icon;
    };

    public static class IncidentVBO
    {
        // Independent triangles of TnMap5f4b.

        public IncidentVBO()
        {
            vbo_id = new int[]{0};
            geom = new Vector();
            segments = new Vector();
        }

        public void clear()
        {
            geom.removeAllElements();
            segments.removeAllElements();
        }

        public Vector geom;
        
        public Buffer buffer;

        public int[] vbo_id;

        public Vector segments; // IncidentVBOSegment
    };
}

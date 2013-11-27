/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapVehicle.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.map.IMapEngine;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Triangulator;
import com.telenav.nio.TnNioManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapVehicle
{
    double[] m_position;
    double[] m_smoothedPosition;
    float m_heading;
    float m_smoothedHeading;

    int m_vertexCount;
    ByteBuffer m_vertexID;

    int m_squareVertexCount;
    ByteBuffer m_squareVertexID;
    
    float m_smoothedAccuracyRadius;
    int m_accuracyRadius;

    // ADI line
    // not worth putting in a vbo
    boolean m_adiEnabled;
    double[] m_adiPos;     // global adi position
    Hashtable m_adiGeomMap = new Hashtable(); //uint32_t, ADIGeom

    int m_type;

    boolean m_firstUpdate;
    
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

    public TnMapVehicle() 
    {
        m_position = new double[]{0.0, 0.0, 0.0};
        m_smoothedPosition = new double[]{0.0,0.0,0.0};
        m_heading = 0;
        m_smoothedHeading = 0;
        m_vertexCount = 0;
        m_squareVertexCount = 0;
        m_smoothedAccuracyRadius = 0;
        m_accuracyRadius = 0;
        m_type = IMapEngine.EVENT_SHOW_CAR_MODEL;
        m_firstUpdate = true;
    }

    public void update(float dt)
    {
        if(m_firstUpdate)
        {
            m_smoothedAccuracyRadius = m_accuracyRadius;
            m_smoothedPosition = m_position;
            m_smoothedHeading = m_heading;
            m_firstUpdate = false;
        }
        else
        {
            //Smooth Accuracy Radius
            double RC = 0.35;
            double alpha = dt / (RC + dt);
            double diff = m_accuracyRadius - m_smoothedAccuracyRadius;
            m_smoothedAccuracyRadius = m_smoothedAccuracyRadius + (float)(diff * alpha);

            //Smooth Vehicle Position
            RC = 0.4;
            alpha = dt / (RC + dt);
            double[] vDiff = Pe.sub(m_position , m_smoothedPosition);

            //If new position is very large then snap to it
            if(Math.abs(vDiff[0]) > 1000 || Math.abs(vDiff[1]) > 1000)
                m_smoothedPosition = m_position;
            else
                m_smoothedPosition = Pe.add(m_smoothedPosition, Pe.multiply(vDiff , alpha));

            //Smooth Vehicle Heading
            RC = 0.35;
            alpha = dt / (RC + dt);
            if(m_heading - m_smoothedHeading > 180)
            {
                diff = m_heading - 360f - m_smoothedHeading;
                m_smoothedHeading = Pe.wrap(m_smoothedHeading + (float)(diff * alpha), 0f, 360f);
            }
            else if(m_smoothedHeading - m_heading > 180)
            {
                diff = m_heading + 360f - m_smoothedHeading;
                m_smoothedHeading = Pe.wrap(m_smoothedHeading + (float)(diff * alpha),0f,360f);
            }
            else
            {
                diff = m_heading - m_smoothedHeading;
                m_smoothedHeading = m_smoothedHeading + (float)(diff * alpha);
            }
        }

    }

    /*!
        Sets the vehicle's location
    */
    public void setPosition(double[] position)
    {
        m_position = position;
        m_adiGeomMap.clear();
    }

    /*!
        Gets the vehicle's location
    */
    public double[] getPosition()
    {
        return m_smoothedPosition;
    }

    /*!
        Sets the vehicle's heading
    */
    public void setHeading(float heading)
    {
        m_heading = heading;
    }

    public float getHeading()
    {
        return m_smoothedHeading;
    }

    public void setVertexID(ByteBuffer vbb1, int count, ByteBuffer vbb2, int count2)
    {
        m_vertexID = vbb1;
        m_vertexCount = count;

        m_squareVertexID = vbb2;
        m_squareVertexCount = count2;
    }

    public ByteBuffer getVertexID(boolean forceSquare)
    {
        if(m_type == IMapEngine.EVENT_SHOW_CAR_MODEL && !forceSquare)
            return m_vertexID;

        return m_squareVertexID;
    }

    public int getVertexCount(boolean forceSquare)
    {
        if(m_type == IMapEngine.EVENT_SHOW_CAR_MODEL && !forceSquare)
            return m_vertexCount;

        return m_squareVertexCount;
    }

    public void setADIEndpoint(double[] adiPos)
    {
        m_adiPos = adiPos;
        m_adiGeomMap.clear();
    }

    //TnMapVertex5f
    public BufferElement getADIGeometry(int zoom)
    {
        BufferElement buffer = (BufferElement)m_adiGeomMap.get(new Integer(zoom));

        if (buffer == null) 
        {
            buffer = buildADI(zoom);
            if (buffer != null)
                m_adiGeomMap.put(new Integer(zoom), buffer);
        }

        return buffer;
    }

    public BufferElement buildADI(int zoom)
    {
        Vector geom = new Vector();

        double[] pos = Pe.sub(m_adiPos , m_position);
        double[] origin = new double[]{0.0, 0.0, 0.0};

        Vector edge = new Vector();//std::vector<Point3d>
        edge.addElement(pos);
        edge.addElement(origin);

        // For now, just make this a portion of the width the route.

        float adiWidth = TnMapConf.getFeatureWidth(TnMapConf.FEATURE_TYPE_ROUTE, zoom) / 6.0f;
        float width = adiWidth * ((float)(1 << zoom));

        // Update ADI end point
        Vector adi_geom = Triangulator.edgeToTriStrip(edge, width, 45.0);

        for (int k = 0; k < adi_geom.size(); k++)
        {
            Pair pair = (Pair)adi_geom.elementAt(k);
            geom.addElement(buildVert((double[])pair.first, (double[])pair.second));
        }
        
        BufferElement buffer = new BufferElement();
        buffer.layers = new int[]{geom.size()};
        
        ByteBuffer pointerbb = TnNioManager.getInstance().allocateDirect(geom.size() * 3 * 4);
        FloatBuffer pointerfb = pointerbb.asFloatBuffer();
        ByteBuffer texCoordbb = TnNioManager.getInstance().allocateDirect(geom.size() * 2 * 4);
        FloatBuffer texCoordfb = texCoordbb.asFloatBuffer();
        
        for (int i=0; i<geom.size(); i++)
        {
            TnMapVertex5f vertex = (TnMapVertex5f)geom.elementAt(i);
            pointerfb.put(vertex.x);
            pointerfb.put(vertex.y);
            pointerfb.put(vertex.z);
            texCoordfb.put(vertex.s);
            texCoordfb.put(vertex.t);
        }
        
        buffer.pointerBuffer = pointerbb;
        buffer.texCoordBuffer = texCoordbb;
        
        return buffer;
    }

    public void setADIState(boolean on)
    {
        m_adiEnabled = on;
    }

    public boolean getADIState()
    {
        return m_adiEnabled;
    }

    public void setVehicleType(int event)
    {
        m_type = event;
    }

    public int getVehicleType()
    {
        return m_type;
    }


    public void setAccuracyRadius(int radius)
    {
        m_accuracyRadius = radius;
    }

    public float getAccuracyRadius()
    {
        return m_smoothedAccuracyRadius;
    }
}

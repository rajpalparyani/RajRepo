/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapScale.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Vector;

import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.nio.TnNioManager;
import com.telenav.tnui.opengles.TnGL10;
import com.telenav.tnui.opengles.TnGLUtils;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-5
 */
public class TnMapScale
{
    double m_screenSpan;
    String m_text;
    float m_barLength;

    public Vector m_textGeom = new Vector();
    public int[] m_textVBO;
    public int m_textVBOCount;

    public Buffer m_textBuffer;

    public Vector m_barGeom = new Vector();
    public int[] m_barVBO;
    public int m_barVBOCount;

    public Buffer m_barBuffer;

    boolean m_enabled;
    boolean m_english;
    TnGL10 gl10;

    public static TnMapVertex5f4b buildVert(float[] p, float[] t, TnMapColor color)
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


    public static TnMapVertex5f4b buildVert(float[] p, float s, float t, TnMapColor color)
    {
        TnMapVertex5f4b result = new TnMapVertex5f4b();
        result.x = (float)p[0];
        result.y = (float)p[1];
        result.z = (float)p[2];
        result.s = s;
        result.t = t;
        result.color = color;
        return result;
    }


    //std::pair<std::string, float>
    public static Pair calcBar(float spanMeters, float minBarLength, boolean englishUnits)
    {
        float val;
        String unit;

        if (englishUnits)
        {
            float feet = 3.28083989501312f * spanMeters;
            if (feet < 5280)
            {
                val = feet;
                unit = "ft";
            } else {
                val = feet / 5280.0f;
                unit = "mi";
            }
        } else {
            if (spanMeters < 1000.0f)
            {
                val = spanMeters;
                unit = "m";
            } else {
                val = spanMeters/1000.0f;
                unit = "km";
            }
        }

        float target = round(val);

        return new Pair(target + unit, new Float(minBarLength * target / val));
    }


    public static float round(float v)
    {
        int exp = (int)TnGLUtils.getInstance().log10(v);
        float pow_rounded = (float)TnGLUtils.getInstance().pow(10, exp);

        //        #if 0
        //            {
        //                std::ostringstream out;
        //                out << "v: " << v << std::endl;
        //                out << "exp: " << exp << std::endl;
        //                out << "pow: " << pow_rounded << std::endl;
        //                TnMapLogError(out.str().c_str());
        //            }
        //        #endif

        float v1 = pow_rounded;

        float v2 = pow_rounded * 2.0f;
        if (v <= v2) return v1;

        float v5 = pow_rounded * 5.0f;
        if (v <= v5) return v2;

        return v5;
    }


    public TnMapScale(TnGL10 gl10)
    {
        this.gl10 = gl10;
        m_screenSpan = 0;
        m_barLength = 0;
        m_textVBO = new int[]{0};
        m_textVBOCount = 0;
        m_barVBO = new int[]{0};
        m_barVBOCount = 0;
        m_enabled = false;
        m_english = false;
    }


    public void build(float x,
            float y,
            float width,
            float height,
            TnMapCamera camera)
    {
        double screenSpan = camera.GetMetersHorizontal();

        // Get real screenwidth.
        float screenWidth = camera.GetScreenSize()[0];

        if (m_screenSpan != screenSpan)
        {
            m_screenSpan = screenSpan;

            float barSpan = (float)(screenSpan * width / screenWidth);
            Pair bar = calcBar(barSpan, width, m_english);

            if (!bar.first.equals(m_text))
            {
                m_text = (String)bar.first;
                buildTextGeom(x, y, width, height * 3.0f, m_text);
            }

            float deltaChange = 2.0f; // change in pixel length to retrigger a build
            if (Pe.tnabs(((Float)bar.second).floatValue() - m_barLength) > deltaChange)
            {
                m_barLength = ((Float)bar.second).floatValue();
                buildBarGeom(x, y, m_barLength, height);
            }    
        }
    }



    public void buildTextGeom(float x,
            float y,
            float width,
            float height,
            String text)
    {
        m_textGeom.removeAllElements();

        TnMapColor textColor = new TnMapColor();
        textColor.r = 255;
        textColor.g = 255;
        textColor.b = 255;
        textColor.a = 255;

        float size = height;
        float total_width = 0;

        for (int i = 0; i < text.length(); i++)
        {
            total_width += TnMapText.getCharWidth(text.charAt(i));
        }

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            x += TnMapText.getCharWidth(c)/2.f; 

            float[] p0 = new float[]{x,           y,              0.0f};
            float[] p1 = new float[]{x,           y - size,       0.0f};
            float[] p2 = new float[]{x + size,    y,              0.0f};
            float[] p3 = new float[]{x + size,    y - size,       0.0f};

            float[] st0 = TnMapText.getCharTexCoord(c, 0);
            float[] st1 = TnMapText.getCharTexCoord(c, 3);
            float[] st2 = TnMapText.getCharTexCoord(c, 1);
            float[] st3 = TnMapText.getCharTexCoord(c, 2);

            // independent triangles

            m_textGeom.addElement(buildVert(p0, st0, textColor));
            m_textGeom.addElement(buildVert(p2, st2, textColor));
            m_textGeom.addElement(buildVert(p1, st1, textColor));

            m_textGeom.addElement(buildVert(p2, st2, textColor));
            m_textGeom.addElement(buildVert(p3, st3, textColor));
            m_textGeom.addElement(buildVert(p1, st1, textColor));
            x += TnMapText.getCharWidth(c)/2.f; 
        }

        m_textVBOCount = m_textGeom.size();
        int geomSize = m_textGeom.size() * TnMapVertex5f4b.size();

        ByteBuffer vbb = TnNioManager.getInstance().allocateDirect(geomSize);
        FloatBuffer data = vbb.asFloatBuffer();
        for (int n = 0; n < m_textGeom.size(); n++)
        {
            TnMapVertex5f4b vertex = (TnMapVertex5f4b) m_textGeom.elementAt(n);
            data.put(vertex.x);
            data.put(vertex.y);
            data.put(vertex.z);
            data.put(vertex.color.r);
            data.put(vertex.color.g);
            data.put(vertex.color.b);
            data.put(vertex.color.a);
            data.put(vertex.s);
            data.put(vertex.t);
        }
        data.position(0);
        m_textBuffer = vbb;
        m_textGeom.removeAllElements();
    }


    public void buildBarGeom(float x,
            float y,
            float w,
            float h)
    {
        m_barGeom.removeAllElements();

        TnMapColor barColor = new TnMapColor();
        barColor.r = 255;
        barColor.g = 255;
        barColor.b = 255;
        barColor.a = 255;

        float hh = h / 2.0f;

        float x0 = x - hh;
        float x1 = x + hh;
        float x2 = x + w - hh;
        float x3 = x + w + hh;

        float y0 = y - h - hh;
        float y1 = y + hh;

        float[] p0 = new float[]{x0, y1, 0.0f};
        float[] p1 = new float[]{x0, y0, 0.0f};

        float[] p2 = new float[]{x1, y1, 0.0f};
        float[] p3 = new float[]{x1, y0, 0.0f};

        float[] p4 = new float[]{x2, y1, 0.0f};
        float[] p5 = new float[]{x2, y0, 0.0f};

        float[] p6 = new float[]{x3, y1, 0.0f};
        float[] p7 = new float[]{x3, y0, 0.0f};

        // triangle strip

        m_barGeom.addElement(buildVert(p0, 0.0f, 0.0f, barColor));
        m_barGeom.addElement(buildVert(p1, 0.0f, 1.0f, barColor));

        m_barGeom.addElement(buildVert(p2, 0.5f, 0.0f, barColor));
        m_barGeom.addElement(buildVert(p3, 0.5f, 1.0f, barColor));

        m_barGeom.addElement(buildVert(p4, 0.5f, 0.0f, barColor));
        m_barGeom.addElement(buildVert(p5, 0.5f, 1.0f, barColor));

        m_barGeom.addElement(buildVert(p6, 0.0f, 0.0f, barColor));
        m_barGeom.addElement(buildVert(p7, 0.0f, 1.0f, barColor));

        m_barVBOCount = m_barGeom.size();
        int size = m_barGeom.size() * TnMapVertex5f4b.size();

        ByteBuffer vbb = TnNioManager.getInstance().allocateDirect(size);
        FloatBuffer data = vbb.asFloatBuffer();
        for (int n = 0; n < m_barGeom.size(); n++)
        {
            TnMapVertex5f4b vertex = (TnMapVertex5f4b) m_barGeom.elementAt(n);
            data.put(vertex.x);
            data.put(vertex.y);
            data.put(vertex.z);
            data.put(vertex.color.r);
            data.put(vertex.color.g);
            data.put(vertex.color.b);
            data.put(vertex.color.a);
            data.put(vertex.s);
            data.put(vertex.t);
        }
        data.position(0);
        m_barBuffer = vbb;
        m_barGeom.removeAllElements();
    }

    public void destroy()
    {
        //            System.out.println("*****TnMapScale destroy-----");
        m_textBuffer = null;
        m_barBuffer = null;
    }


    public void englishUnits(boolean english)
    {
        if (english != m_english)
        {
            m_english = english;

            // reset
            m_screenSpan = 0;
            m_text = "";
            m_barLength = 0;
        }
    }


    public void enable()
    {
        m_enabled = true;
    }

    public void disable()
    {
        m_enabled = false;
    }
}

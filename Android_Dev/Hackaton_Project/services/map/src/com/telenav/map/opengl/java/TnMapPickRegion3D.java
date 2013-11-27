/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapPickRegion3D.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.map.opengl.java.TnMapEngine.Pickable;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Poly;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapPickRegion3D
{
    public double[] m_globalPosition;

    public float m_left;

    public float m_right;

    public float m_bottom;

    public float m_top;

    public Pickable m_data;

    public TnMapPickRegion3D()
    {
    }

    public TnMapPickRegion3D(double[] globalPosition, int width, int height, int origin_x, int origin_y, Pickable data)
    {
        m_globalPosition = globalPosition;
        m_left = -(float) origin_x;
        m_right = (-(float) origin_x) + ((float) width);
        m_bottom = -(float) origin_y;
        m_top = (-(float) origin_y) + ((float) height);
        m_data = data;
    }

    public Pickable getPickable()
    {
        return m_data;
    }

    public Matrixf buildViewMatrix(TnMapCamera camera)
    {
        // Scale PickRegion based on viewport height

        float scale = camera.Get2DScaleFactor();

        Matrixf mat = Matrixf.multiply(camera.GetCameraMatrix(), camera.GetSpriteMatrix(m_globalPosition));
        Matrixf m1 = new Matrixf();
        m1.buildScale(scale);
        mat = Matrixf.multiply(mat, m1);
        
        Matrixf m2 = new Matrixf();
        m2.buildRotateXPos90();
        mat = Matrixf.multiply(mat, m2);
        return mat;
    }

    public float hit(int x, int y, TnMapCamera camera)
    {

        // Note these are in triangle mesh order to match
        // construction. Check order when using surrounds test.

        float[] p1 = new float[]{m_left, m_bottom, 0.0f};
        float[] p2 = new float[]{m_right, m_bottom, 0.0f};
        float[] p3 = new float[]{m_left, m_top, 0.0f};
        float[] p4 = new float[]{m_right, m_top, 0.0f};

        Matrixf mat = Matrixf.multiply(camera.GetProjectionMatrix(), buildViewMatrix(camera));

        int screen_width = (int) camera.GetScreenSize()[0];
        int screen_height = (int) camera.GetScreenSize()[1];

        // Calculate the corner positions
        float[] sp1 = Pe.calc2DPos(p1, mat, screen_width, screen_height);
        if (sp1 == null)
            return Float.MAX_VALUE;

        float[] sp2 = Pe.calc2DPos(p2, mat, screen_width, screen_height);
        if (sp2 == null)
            return Float.MAX_VALUE;

        float[] sp3 = Pe.calc2DPos(p3, mat, screen_width, screen_height);
        if (sp3 == null)
            return Float.MAX_VALUE;

        float[] sp4 = Pe.calc2DPos(p4, mat, screen_width, screen_height);
        if (sp4 == null)
            return Float.MAX_VALUE;

        float[] b = new float[]{(float)x, (float)y};

        if (Poly.surrounds(b, sp1, sp2, sp4, sp3))
        {
            return 0;
        }

        float x1 = sp1[0];
        ;
        float x2 = sp3[0];
        float y1 = sp1[1];
        float y2 = sp3[1];

        float tx = x;
        float ty = y;

        // @TODO: Distance test assumes rectangle - not accurate for perspective PickRegions (non sprite)
        float dx = (tx < x1) ? (x1 - tx) : ((tx > x2) ? (tx - x2) : 0.0f);
        float dy = (ty < y1) ? (y1 - ty) : ((ty > y2) ? (ty - y2) : 0.0f);

        float distance_squared = dx * dx + dy * dy;
        return distance_squared;
    }

}

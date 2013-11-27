/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapAnnotation2D.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Poly;
import com.telenav.nio.TnNioManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-26
 */
public class TnMapAnnotation
{
    public final static int TN_MAP_ANNOTATION_ACTION_ENABLE = 1;

    public final static int TN_MAP_ANNOTATION_ACTION_DISABLE = 0;
    
    protected TnMapTexture texture;
    
    protected float m_left;
    protected float m_right;
    protected float m_bottom;
    protected float m_top;
    
    protected boolean m_enabled;
    
    protected BufferElement buffer;
    
    public boolean action(int action)
    {
        switch(action)
        {

        case TN_MAP_ANNOTATION_ACTION_ENABLE:
            this.m_enabled = true;
            return true;

        case TN_MAP_ANNOTATION_ACTION_DISABLE:
            this.m_enabled = false;
            return true;
        }
        return false;
    }
    
    public TnMapTexture getTexture()
    {
        return texture;
    }
    
    public BufferElement getVertexData()
    {
        return buffer;
    }
    
    public boolean isEnabled()
    {
        return m_enabled;
    }
    
    public static class TnMapAnnotation2D extends TnMapAnnotation
    {
        public TnMapAnnotation2D(int x, int y, int width, int height, TnMapTexture texture)
        {
            this.texture = texture;
            this.m_left = x;
            this.m_top = y;
            this.m_right = x + width;
            this.m_bottom = y + height;
            this.m_enabled = true;
            
            build();
        }
        
        public void reposition(int x,
                int y,
                int width,
                int height)
        {
            this.m_left = x;
            this.m_top = y;
            this.m_right = x + width;
            this.m_bottom = y + height;

            build();
        }
        
        protected void build()
        {
            if (buffer == null)
            {
                buffer = new BufferElement();
                buffer.layers = new int[]{4};
                buffer.pointerBuffer = TnNioManager.getInstance().allocateDirect(3 * 4 * 4);
                buffer.texCoordBuffer = TnNioManager.getInstance().allocateDirect(2 * 4 * 4);
            }
            else
            {
                buffer.pointerBuffer.clear();
                buffer.texCoordBuffer.clear();
            }
            
            FloatBuffer pointerfb = ((ByteBuffer)buffer.pointerBuffer).asFloatBuffer();
            FloatBuffer texCoodfb = ((ByteBuffer)buffer.texCoordBuffer).asFloatBuffer();
            
            //the Y axis is pointing to bottom for 2D annotations
            //TopRight Vertex
            pointerfb.put(m_right);
            pointerfb.put(m_top);
            pointerfb.put(0.0f);
            texCoodfb.put(1.0f);
            texCoodfb.put(0.0f);

            //TopLeft Vertex
            pointerfb.put(m_left);
            pointerfb.put(m_top);
            pointerfb.put(0.0f);
            texCoodfb.put(0.0f);
            texCoodfb.put(0.0f);

            //BottomRight Vertex
            pointerfb.put(m_right);
            pointerfb.put(m_bottom);
            pointerfb.put(0.0f);
            texCoodfb.put(1.0f);
            texCoodfb.put(1.0f);

            //BottomLeft Vertex
            pointerfb.put(m_left);
            pointerfb.put(m_bottom);
            pointerfb.put(0.0f);
            texCoodfb.put(0.0f);
            texCoodfb.put(1.0f);
        }
        
        public float hit(int x, int y, TnMapCamera camera)
        {
            float[] p1 = new float[]{m_left,   m_top, 0.0f};
            float[] p2 = new float[]{m_right,  m_top, 0.0f};
            float[] p3 = new float[]{m_left,   m_bottom, 0.0f};
            float[] p4 = new float[]{m_right,  m_bottom, 0.0f};

            Matrixf mat = new Matrixf(camera.GetAnnotation2DMatrix());

            int screen_width = (int)camera.GetScreenSize()[0];
            int screen_height = (int)camera.GetScreenSize()[1];

            //Calculate the corner positions
            float[] sp1 = Pe.calc2DPos(p1, mat, screen_width, screen_height);
            if (sp1 == null) return Float.MAX_VALUE;

            float[] sp2 = Pe.calc2DPos(p2, mat, screen_width, screen_height);
            if (sp2 == null) return Float.MAX_VALUE;

            float[] sp3 = Pe.calc2DPos(p3, mat, screen_width, screen_height);
            if (sp3 == null) return Float.MAX_VALUE;

            float[] sp4 = Pe.calc2DPos(p4, mat, screen_width, screen_height);
            if (sp4 == null) return Float.MAX_VALUE;

            float[] b = new float[]{(float)x, (float)y};

            if(Poly.surrounds(b, sp1, sp2, sp4, sp3))
            {
                return 0;
            }

            float x1 = sp1[0];
            float x2 = sp3[0];
            float y1 = sp1[1];
            float y2 = sp3[1];
            
            float tx = x;
            float ty = y;
            
            //@TODO: Distance test assumes rectangle - not accurate for perspective annotations (non sprite)
            float dx = (tx < x1) ? (x1 - tx) : ((tx > x2) ? (tx - x2) : 0.0f);
            float dy = (ty < y1) ? (y1 - ty) : ((ty > y2) ? (ty - y2) : 0.0f);

            float distance_squared = dx*dx + dy*dy;
            return distance_squared;
        }
        
    }
    
    public static class TnMapAnnotation3D extends TnMapAnnotation
    {
        private double[]  m_globalPosition;
        private float[] faceVector;
        private float[] upVector;
        
        public TnMapAnnotation3D(double[] globalPosition,
                int width, int height,
                int origin_x, int origin_y,
                TnMapTexture texture)
        {
            this.texture = texture;
            m_globalPosition = globalPosition;
            m_left = -origin_x;
            m_right = -origin_x + width;
            m_bottom = origin_y;
            m_top = origin_y + height;
            m_enabled = true;
            
            build();
        }
        
        public TnMapAnnotation3D(double[] globalPosition,
                int width, int height,
                int origin_x, int origin_y,
                float[] faceVector, float[] upVector,
                TnMapTexture texture, int layer)
        {
            this.texture = texture;
            this.m_globalPosition = globalPosition;
            this.m_left = -origin_x;
            this.m_right = -origin_x + width;
            this.m_bottom = origin_y;
            this.m_top = origin_y + height;
            this.m_enabled = true;
            this.faceVector = faceVector;
            this.upVector = upVector;
            
            build();
        }
        
        private void build()
        {
            if (buffer == null)
            {
                buffer = new BufferElement();
                buffer.layers = new int[]{4};
                buffer.pointerBuffer = TnNioManager.getInstance().allocateDirect(3 * 4 * 4);
                buffer.texCoordBuffer = TnNioManager.getInstance().allocateDirect(2 * 4 * 4);
            }
            else
            {
                buffer.pointerBuffer.clear();
                buffer.texCoordBuffer.clear();
            }
            
            FloatBuffer pointerfb = ((ByteBuffer)buffer.pointerBuffer).asFloatBuffer();
            FloatBuffer texCoodfb = ((ByteBuffer)buffer.texCoordBuffer).asFloatBuffer();
            
            //TopLeft Vertex
            pointerfb.put(m_left);
            pointerfb.put(m_top);
            pointerfb.put(0.0f);
            texCoodfb.put(0.0f);
            texCoodfb.put(0.0f);

            //TopRight Vertex
            pointerfb.put(m_right);
            pointerfb.put(m_top);
            pointerfb.put(0.0f);
            texCoodfb.put(1.0f);
            texCoodfb.put(0.0f);

            //BottomLeft Vertex
            pointerfb.put(m_left);
            pointerfb.put(m_bottom);
            pointerfb.put(0.0f);
            texCoodfb.put(0.0f);
            texCoodfb.put(1.0f);

            //BottomRight Vertex
            pointerfb.put(m_right);
            pointerfb.put(m_bottom);
            pointerfb.put(0.0f);
            texCoodfb.put(1.0f);
            texCoodfb.put(1.0f);
        }
        
        public float hit(int x, int y, TnMapCamera camera)
        {
            // Note these are in triangle mesh order to match
            // construction. Check order when using surrounds test.

            float[] p1 = new float[]{m_left,  m_bottom, 0.0f};
            float[] p2 = new float[]{m_right, m_bottom, 0.0f};
            float[] p3 = new float[]{m_left,  m_top, 0.0f};
            float[] p4 = new float[]{m_right, m_top, 0.0f};

            Matrixf mat = Matrixf.multiply(camera.GetProjectionMatrix() , buildViewMatrix(camera));

            int screen_width = (int)camera.GetScreenSize()[0];
            int screen_height = (int)camera.GetScreenSize()[1];

            //Calculate the corner positions
            float[] sp1 = Pe.calc2DPos(p1, mat, screen_width, screen_height);
            if (sp1 == null) return Float.MAX_VALUE;

            float[] sp2 = Pe.calc2DPos(p2, mat, screen_width, screen_height);
            if (sp2 == null) return Float.MAX_VALUE;

            float[] sp3 = Pe.calc2DPos(p3, mat, screen_width, screen_height);
            if (sp3 == null) return Float.MAX_VALUE;

            float[] sp4 = Pe.calc2DPos(p4, mat, screen_width, screen_height);
            if (sp4 == null) return Float.MAX_VALUE;

            float[] b = new float[]{x, y};

            if(Poly.surrounds(b, sp1, sp2, sp4, sp3))
            {
                return 0;
            }

            float x1 = sp1[0];
            float x2 = sp3[0];
            float y1 = sp1[1];
            float y2 = sp3[1];
            
            float tx = x;
            float ty = y;
            
            //@TODO: Distance test assumes rectangle - not accurate for perspective annotations (non sprite)
            float dx = (tx < x1) ? (x1 - tx) : ((tx > x2) ? (tx - x2) : 0.0f);
            float dy = (ty < y1) ? (y1 - ty) : ((ty > y2) ? (ty - y2) : 0.0f);

            float distance_squared = dx*dx + dy*dy;
            return distance_squared;
        }
        
        public double[] getGlobalPosition() 
        {
            return m_globalPosition;
        }
        
        public Matrixf buildViewMatrix(TnMapCamera camera)
        {
            float scale = camera.Get2DScaleFactor();
            
            Matrixf m1 = new Matrixf();
            m1.buildScale(scale);
            Matrixf m2 = new Matrixf();
            m2.buildRotateXPos90();
            Matrixf mat = 
                Matrixf.multiply(Matrixf.multiply(camera.GetCameraMatrix() , 
                camera.GetSpriteMatrix(m_globalPosition)) , 
                Matrixf.multiply(m1, m2));
            return mat;
        }
    }
}

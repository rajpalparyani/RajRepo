/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Matrix.java
 *
 */
package com.telenav.map.opengl.java.math;

import com.telenav.map.opengl.java.math.Lookat.Lookatf;
import com.telenav.tnui.opengles.TnGLUtils;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-26
 */
public class Matrixf
{
    public float[] m = new float[4 * 4];
    public int type;
    
    public int getType()
    {
        return this.type;
    }
    
    public String getTypeName()
    {
        return MatrixType.names[this.type];
    }
    
    public Matrixf()
    {
        this.type = MatrixType.IDENTITY;
        m[0] = 1; 
        m[1] = 0; 
        m[2] = 0; 
        m[3] = 0;

        m[4] = 0; 
        m[5] = 1; 
        m[6] = 0; 
        m[7] = 0;

        m[8] = 0; 
        m[9] = 0; 
        m[10] = 1; 
        m[11] = 0;

        m[12] = 0; 
        m[13] = 0; 
        m[14] = 0; 
        m[15] = 1;
    }
    
    public float[] getArray()
    {
        return m;
    }
    
    public void set(float a00, float a01, float a02, float a03, float a10, float a11, float a12, float a13, float a20, float a21, float a22,
            float a23, float a30, float a31, float a32, float a33, boolean find_type)
    {
        type = MatrixType.ARBITRARY;
        m[0] = a00;
        m[1] = a01;
        m[2] = a02;
        m[3] = a03;
        m[4] = a10;
        m[5] = a11;
        m[6] = a12;
        m[7] = a13;
        m[8] = a20;
        m[9] = a21;
        m[10] = a22;
        m[11] = a23;
        m[12] = a30;
        m[13] = a31;
        m[14] = a32;
        m[15] = a33;

        if (find_type)
            type = MatrixType.findType(m);
    }
    
    public Matrixf(float a00, float a01, float a02, float a03, float a10, float a11, float a12, float a13, float a20, float a21, float a22,
            float a23, float a30, float a31, float a32, float a33, boolean find_type)
    {
        set(a00, a01, a02, a03, a10, a11, a12, a13, a20, a21, a22, a23, a30, a31, a32, a33, find_type);
    }
    
    public void set(float a00, float a01, float a02, float a03, float a10, float a11, float a12, float a13, float a20, float a21, float a22,
            float a23, float a30, float a31, float a32, float a33, int t)
    {
        type = t;
        m[0] = a00;
        m[1] = a01;
        m[2] = a02;
        m[3] = a03;
        m[4] = a10;
        m[5] = a11;
        m[6] = a12;
        m[7] = a13;
        m[8] = a20;
        m[9] = a21;
        m[10] = a22;
        m[11] = a23;
        m[12] = a30;
        m[13] = a31;
        m[14] = a32;
        m[15] = a33;
    }
    
    public Matrixf(float a00, float a01, float a02, float a03, float a10, float a11, float a12, float a13, float a20, float a21, float a22,
            float a23, float a30, float a31, float a32, float a33, int t)
    {
        set(a00, a01, a02, a03, a10, a11, a12, a13, a20, a21, a22, a23, a30, a31, a32, a33, t);
    }
    
    public void set(float a00, float a01, float a02, float a10, float a11, float a12, float a20, float a21, float a22, boolean find_type)
    {
        type = MatrixType.ROT_ARB;
        m[0] = a00;
        m[1] = a01;
        m[2] = a02;
        m[3] = 0;
        m[4] = a10;
        m[5] = a11;
        m[6] = a12;
        m[7] = 0;
        m[8] = a20;
        m[9] = a21;
        m[10] = a22;
        m[11] = 0;
        m[12] = 0;
        m[13] = 0;
        m[14] = 0;
        m[15] = 1;

        if (find_type)
            type = MatrixType.findType(m);
    }
    
    public Matrixf(float a00, float a01, float a02, float a10, float a11, float a12, float a20, float a21, float a22, boolean find_type)
    {
        set(a00, a01, a02, a10, a11, a12, a20, a21, a22, find_type);
    }
    
    public void set(float a00, float a01, float a02, float a10, float a11, float a12, float a20, float a21, float a22, float a30, float a31,
            float a32)
    {
        type = MatrixType.NO_W;
        m[0] = a00;
        m[1] = a01;
        m[2] = a02;
        m[3] = 0;
        m[4] = a10;
        m[5] = a11;
        m[6] = a12;
        m[7] = 0;
        m[8] = a20;
        m[9] = a21;
        m[10] = a22;
        m[11] = 0;
        m[12] = a30;
        m[13] = a31;
        m[14] = a32;
        m[15] = 1;
    }
    
    public Matrixf(float a00, float a01, float a02, float a10, float a11, float a12, float a20, float a21, float a22, float a30, float a31,
            float a32)
    {
        set(a00, a01, a02, a10, a11, a12, a20, a21, a22, a30, a31, a32);
    }
    
    public Matrixf(Matrixf that)
    {
        copyMatrix(that);
    }
    
    public void copyMatrix(Matrixf that) 
    {
        type = that.type;

        System.arraycopy(that.m, 0, m, 0, m.length);
    }

    public void buildLookat(Lookatf l) 
    {

        type = MatrixType.NO_W;
        
        //float[] z = Pe.normalize(Pe.sub(l.eye, l.obj));
        float z0 = l.eye[0] - l.obj[0];
        float z1 = l.eye[1] - l.obj[1];
        float z2 = l.eye[2] - l.obj[2];
        float sqrt = (float)Math.sqrt(z0*z0 + z1*z1 + z2*z2);
        z0 /= sqrt;
        z1 /= sqrt;
        z2 /= sqrt;
        
        //float[] x = Pe.normalize(Pe.cross(l.up, z));
        float x0 = l.up[1] * z2 - l.up[2] * z1;
        float x1 = l.up[2] * z0 - l.up[0] * z2;
        float x2 = l.up[0] * z1 - l.up[1] * z0;
        sqrt = (float)Math.sqrt(x0*x0 + x1*x1 + x2*x2);
        x0 /= sqrt;
        x1 /= sqrt;
        x2 /= sqrt;
        
        //float[] y = Pe.normalize(Pe.cross(z,x));
        float y0 = z1 * x2 - z2 * x1;
        float y1 = z2 * x0 - z0 * x2;
        float y2 = z0 * x1 - z1 * x0;
        sqrt = (float)Math.sqrt(y0*y0 + y1*y1 + y2*y2);
        y0 /= sqrt;
        y1 /= sqrt;
        y2 /= sqrt;
        
        m[0] = x0;
        m[1] = y0;
        m[2] = z0;
        m[3] = 0f;

        m[4] = x1;
        m[5] = y1;
        m[6] = z1;
        m[7] = 0f;

        m[8] = x2;
        m[9] = y2;
        m[10] = z2;
        m[11] = 0f;

        m[12] = -x0*l.eye[0] + -x1*l.eye[1] + -x2*l.eye[2];
        m[13] = -y0*l.eye[0] + -y1*l.eye[1] + -y2*l.eye[2];
        m[14] = -z0*l.eye[0] + -z1*l.eye[1] + -z2*l.eye[2];
        m[15] = 1f;

    }
    
    public void buildTranslate(float v0, float v1, float v2) 
    {

        type = MatrixType.XLATE;

        m[0] = 1f; 
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = 1f; 
        m[6] = 0f; 
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = 0f; 
        m[10] = 1f; 
        m[11] = 0f;

        m[12] = v0;
        m[13] = v1;
        m[14] = v2;
        m[15] = 1f;
    }


    public void buildScale(float x, float y, float z) 
    {
        type = MatrixType.SCALE;

        m[0] = x;
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = y;
        m[6] = 0f; 
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = 0f; 
        m[10] = z;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }

    public void buildScale(float s) 
    {
        type = MatrixType.SCALE;

        m[0] = s;
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = s;
        m[6] = 0f; 
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = 0f; 
        m[10] = s;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }

    public void buildOrtho2(float left, float right, float bottom, float top) 
    {
        type = MatrixType.SCALE_XLATE;

        float l = left;
        float r = right;
        float b = bottom;
        float t = top;

        float sx = 2f/(r-l);
        float sy = 2f/(t-b);

        float tx = -(r+l)/(r-l);
        float ty = -(t+b)/(t-b);

        m[0] = sx;
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = sy;
        m[6] = 0f; 
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = 0f; 
        m[10] = 1f;
        m[11] = 0f;

        m[12] = tx; 
        m[13] = ty;
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildOrtho(float left, float right, float bottom, float top, float near, float far) 
    {
        type = MatrixType.SCALE_XLATE;

        float l = left;
        float r = right;
        float b = bottom;
        float t = top;
        float n = near;
        float f = far;

        float sx = 2f/(r-l);
        float sy = 2f/(t-b);
        float sz = -2f/(f-n);

        float tx = -(r+l)/(r-l);
        float ty = -(t+b)/(t-b);
        float tz = -(f+n)/(t-b);

        m[0] = sx;
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = sy;
        m[6] = 0f; 
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = 0f; 
        m[10] = sz;
        m[11] = 0f;

        m[12] = tx; 
        m[13] = ty;
        m[14] = tz;
        m[15] = 1f;
    }
    
    public void buildFrustum(float left, float right, float bottom, float top, float near, float far) 
    {
        type = MatrixType.FRUSTUM;

        float l = left;
        float r = right;
        float b = bottom;
        float t = top;
        float n = near;
        float f = far;

        float m00 = n*2f/(r-l);
        float m11 = n*2f/(t-b);
        float m22 = -(f+n)/(f-n);

        float m20 = (r+l)/(r-l);
        float m21 = (t+b)/(t-b);

        float m32 = -(2f*f*n)/(f-n);
        float m23 = -1f;

        m[0] = m00;
        m[1] = 0f; 
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = m11;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = m20;
        m[9] = m21;
        m[10] = m22;
        m[11] = m23;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = m32;
        m[15] = 0f;
    }

    public void buildRotate(float r, float i, float j, float k) 
    {
        type = MatrixType.ROT_ARB;

        m[0] = 1f - 2f * (j*j + k*k);
        m[1] = 2f * ( i*j + r*k);
        m[2] = 2f * ( i*k - r*j);
        m[3] = 0f;

        m[4] = 2f * ( i*j - r*k);
        m[5] = 1f - 2f * (i*i + k*k);
        m[6] = 2f * ( j*k + r*i);
        m[7] = 0f;

        m[8] = 2f * ( i*k + r*j);
        m[9] = 2f * ( j*k - r*i);
        m[10] = 1f - 2f * (i*i + j*j);
        m[11] = 0f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = 0f;
        m[15] = 1f;
    }

    public void buildRotate(float radians, float[] axis)
    {
        float dst_l = (float) Math.sin(radians / 2f); // should be
        float src_l = (float) Pe.magnitude(axis); // actually is
        if (src_l <= Float.MIN_VALUE)
        {
            buildRotate(0, 0, 0, 0);
        }
        else
        {
            float s = dst_l / src_l;
            float i = axis[0] * s;
            float j = axis[1] * s;
            float k = axis[2] * s;
            float r = (float) Math.cos(radians / 2f);
            buildRotate(r, i, j, k);
        }
    }
    
    public void buildRotate(float[] a, float[] b)
    {
        float[] x = Pe.normalize(a);
        float[] z = Pe.normalize(Pe.cross(a,b));
        float[] y = Pe.normalize(Pe.cross(z,x));

        float[] v = Pe.normalize(b);

        float x_c = Pe.dot(v,x);
        float y_c = Pe.dot(v,y);

        float theta = (float)TnGLUtils.getInstance().atan2(y_c,x_c);

        float l = (float)Math.sin(theta/2f);
        float i = z[0] * l;
        float j = z[1] * l;
        float k = z[2] * l;
        float r = (float)Math.cos(theta/2f);
        buildRotate(r, i, j, k);
    }
    
    public void buildRotateXDeg(double degree) 
    {
        type = MatrixType.ROT_X;

        float s = (float)Math.sin(Pe.degToRad(degree));
        float c = (float)Math.cos(Pe.degToRad(degree));

        m[0] = 1f; 
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = c;
        m[6] = s;
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = -s;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateYDeg(double degree) 
    {
        type = MatrixType.ROT_Y;

        float s = (float)Math.sin(Pe.degToRad(degree));
        float c = (float)Math.cos(Pe.degToRad(degree));

        m[0] = c;
        m[1] = 0f; 
        m[2] = -s;
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = 1f;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = s;
        m[9] = 0f;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateZDeg(double degree) 
    {
        type = MatrixType.ROT_Z;

        float s = (float)Math.sin(Pe.degToRad(degree));
        float c = (float)Math.cos(Pe.degToRad(degree));

        m[0] = c;
        m[1] = s;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = -s;
        m[5] = c;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = 1f;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }

    public void buildRotateXRad(double radians) 
    {
        type = MatrixType.ROT_X;

        float s = (float)Math.sin(radians);
        float c = (float)Math.cos(radians);

        m[0] = 1f; 
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = c;
        m[6] = s;
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = -s;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateYRad(double radians) 
    {
        type = MatrixType.ROT_Y;

        float s = (float)Math.sin(radians);
        float c = (float)Math.cos(radians);

        m[0] = c;
        m[1] = 0f; 
        m[2] = -s;
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = 1f;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = s;
        m[9] = 0f;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateZRad(double radians) 
    {
        type = MatrixType.ROT_Z;

        float s = (float)Math.sin(radians);
        float c = (float)Math.cos(radians);


        m[0] = c;
        m[1] = s;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = -s;
        m[5] = c;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = 1f;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateXPos90() 
    {
        type = MatrixType.ROT_X_90;

        float s = 1f;
        float c = 0f;

        m[0] = 1f; 
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = c;
        m[6] = s;
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = -s;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateYPos90() 
    {
        type = MatrixType.ROT_Y_90;

        float s = 1f;
        float c = 0f;

        m[0] = c;
        m[1] = 0f; 
        m[2] = -s;
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = 1f;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = s;
        m[9] = 0f;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateZPos90() 
    {
        type = MatrixType.ROT_Z_90;

        float s = 1f;
        float c = 0f;

        m[0] = c;
        m[1] = s;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = -s;
        m[5] = c;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = 1f;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateXNeg90() 
    {
        type = MatrixType.ROT_X_90;

        float s = -1f;
        float c = 0f;

        m[0] = 1f; 
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = c;
        m[6] = s;
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = -s;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateYNeg90() 
    {
        type = MatrixType.ROT_Y_90;

        float s = -1f;
        float c = 0f;

        m[0] = c;
        m[1] = 0f; 
        m[2] = -s;
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = 1f;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = s;
        m[9] = 0f;
        m[10] = c;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateZNeg90() 
    {
        type = MatrixType.ROT_Z_90;

        float s = -1f;
        float c = 0f;

        m[0] = c;
        m[1] = s;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = -s;
        m[5] = c;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = 1f;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateX180() 
    {
        type = MatrixType.SCALE;

        m[0] = 1f; 
        m[1] = 0f; 
        m[2] = 0f; 
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = -1f;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f; 
        m[9] = 0f;
        m[10] = -1f;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateY180() 
    {
        type = MatrixType.SCALE;

        m[0] = -1f;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f; 
        m[5] = 1f;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -1f;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }


    public void buildRotateZ180() 
    {
        type = MatrixType.SCALE;

        m[0] = -1f;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = -1f;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = 1f;
        m[11] = 0f;

        m[12] = 0f; 
        m[13] = 0f; 
        m[14] = 0f; 
        m[15] = 1f;
    }
    
    public static Matrixf invert(Matrixf m) {

        switch(m.type) 
        {

        case MatrixType.IDENTITY:
             return new Matrixf();

        case MatrixType.SCALE:
             return new Matrixf(1/m.m[0], 0, 0, 0, 
                                    0, 1/m.m[5], 0, 0, 
                                    0, 0, 1/m.m[10], 0, 
                                    0, 0, 0, 1,
                                    MatrixType.SCALE);

        case MatrixType.XLATE:
             return new Matrixf(1, 0, 0, 0,
                                    0, 1, 0, 0,
                                    0, 0, 1, 0,
                                    -m.m[12], -m.m[13], -m.m[14], 1,
                                    MatrixType.XLATE);

        case MatrixType.SCALE_XLATE:
        {
             float inv_a00 = Pe.invert(m.m[0]);
             float inv_a11 = Pe.invert(m.m[5]);
             float inv_a22 = Pe.invert(m.m[10]);
             return new Matrixf(inv_a00, 0, 0, 0, 
                                    0, inv_a11, 0, 0, 
                                    0, 0, inv_a22, 0, 
                                    -m.m[12]*inv_a00, -m.m[13]*inv_a11, -m.m[14]*inv_a22, 1,
                                    MatrixType.SCALE_XLATE);
        }


        case MatrixType.FRUSTUM:
        {
             float d = m.m[0]*m.m[5]*(m.m[10] - m.m[11]*m.m[14]);
             float inv_d = Pe.invert(d);
             float intermediate = m.m[10] - m.m[11]*m.m[14];
             return new Matrixf(m.m[5]*intermediate*inv_d, 
                                    0, 
                                    0, 
                                    0, 

                                    0, 
                                    m.m[0]*intermediate*inv_d, 
                                    0, 
                                    0, 

                                    -m.m[5]*m.m[8]*inv_d,
                                    -m.m[0]*m.m[9]*inv_d,
                                    m.m[0]*m.m[5]*inv_d,
                                    -m.m[0]*m.m[5]*m.m[11]*inv_d, 

                                    m.m[5]*m.m[8]*m.m[14]*inv_d, 
                                    m.m[0]*m.m[9]*m.m[14]*inv_d,
                                    -m.m[0]*m.m[5]*m.m[14]*inv_d,
                                    0,
                                    MatrixType.ARBITRARY);
        }

        case MatrixType.ROT_X_90:
        {
             float a00 = Pe.invert(m.m[0]);
             float a12 = Pe.invert(m.m[6]);
             float a21 = Pe.invert(m.m[9]);

             return new Matrixf(a00,   0,   0,   0,
                                    0,  0,   a12,    0,
                                    0,  a21,    0,   0,
                                    0,  0,   0,   1,
                                    MatrixType.ROT_X_90);
        }

        case MatrixType.ROT_Y_90:
        {
             float a02 = Pe.invert(m.m[2]);
             float a11 = Pe.invert(m.m[5]);
             float a20 = Pe.invert(m.m[8]);

             return new Matrixf(0,  0,   a02,    0,
                                    0,  a11,    0,   0,
                                    a20,   0,   0,   0,
                                    0,  0,   0,   1,
                                    MatrixType.ROT_Y_90);
        }

        case MatrixType.ROT_Z_90:
        {
             float a01 = Pe.invert(m.m[1]);
             float a10 = Pe.invert(m.m[4]);
             float a22 = Pe.invert(m.m[10]);

             return new Matrixf(0,  a01,    0,   0,
                                    a10,   0,   0,   0,
                                    0,  0,   a22,    0,
                                    0,  0,   0,   1,
                                    MatrixType.ROT_Z_90);
        }

        case MatrixType.ROT_X:
        {
             float d = m.m[0]*m.m[5]*m.m[10] - m.m[0]*m.m[6]*m.m[9];
             float inv_d = Pe.invert(d);

             float a00 = (m.m[5]*m.m[10]-m.m[6]*m.m[9]) * inv_d;
             float a11 = (m.m[0]*m.m[10]) * inv_d;
             float a12 = -(m.m[0]*m.m[6]) * inv_d;
             float a21 = -(m.m[0]*m.m[9]) * inv_d;
             float a22 = (m.m[0]*m.m[5]) * inv_d;

             return new Matrixf(a00,   0,   0,   0,
                                    0,  a11,    a12,    0,
                                    0,  a21,    a22,    0,
                                    0,  0,   0,   1,
                                    MatrixType.ROT_X);
        }

        case MatrixType.ROT_Y:
        {
             float d = m.m[0]*m.m[5]*m.m[10] - m.m[0]*m.m[6]*m.m[9];
             float inv_d = Pe.invert(d);

             float a00 = (m.m[5]*m.m[10]) * inv_d;
             float a02 = -(m.m[2]*m.m[5]) * inv_d;
             float a11 = (m.m[0]*m.m[10]-m.m[2]*m.m[8]) * inv_d;
             float a20 = -(m.m[5]*m.m[8]) * inv_d;
             float a22 = (m.m[0]*m.m[5]) * inv_d;

             return new Matrixf(a00,   0,   a02,    0,
                                    0,  a11,    0,   0,
                                    a20,   0,   a22,    0,
                                    0,  0,   0,   1,
                                    MatrixType.ROT_Y);
        }

        case MatrixType.ROT_Z:
        {
             float d = m.m[0]*m.m[5]*m.m[10] - m.m[0]*m.m[6]*m.m[9];
             float inv_d = Pe.invert(d);

             float a00 = (m.m[5]*m.m[10]) * inv_d;
             float a01 = -(m.m[1]*m.m[10]) * inv_d;
             float a10 = -(m.m[4]*m.m[10]) * inv_d;
             float a11 = (m.m[0]*m.m[10]) * inv_d;
             float a22 = (m.m[0]*m.m[5]-m.m[1]*m.m[4]) * inv_d;

             return new Matrixf(a00,   a01,    0,   0,
                                    a10,   a11,    0,   0,
                                    0,  0,   a22,    0,
                                    0,  0,   0,   1,
                                    MatrixType.ROT_Z);
        }

        case MatrixType.ROT_ARB:
        {
             float d = 
                  m.m[0]*m.m[5]*m.m[10] +
                  m.m[1]*m.m[6]*m.m[8] +
                  m.m[2]*m.m[4]*m.m[9] +
                  -m.m[0]*m.m[6]*m.m[9] +
                  -m.m[1]*m.m[4]*m.m[10] +
                  -m.m[2]*m.m[5]*m.m[8];

             float inv_d = Pe.invert(d);

             float a00 = (m.m[5]*m.m[10]-m.m[6]*m.m[9]) * inv_d;
             float a01 = (m.m[2]*m.m[9]-m.m[1]*m.m[10]) * inv_d;
             float a02 = (m.m[1]*m.m[6]-m.m[2]*m.m[5]) * inv_d;

             float a10 = (m.m[6]*m.m[8]-m.m[4]*m.m[10]) * inv_d;
             float a11 = (m.m[0]*m.m[10]-m.m[2]*m.m[8]) * inv_d;
             float a12 = (m.m[2]*m.m[4]-m.m[0]*m.m[6]) * inv_d;

             float a20 = (m.m[4]*m.m[9]-m.m[5]*m.m[8]) * inv_d;
             float a21 = (m.m[1]*m.m[8]-m.m[0]*m.m[9]) * inv_d;
             float a22 = (m.m[0]*m.m[5]-m.m[1]*m.m[4]) * inv_d;

             return new Matrixf(a00, a01, a02, 0,
                                    a10, a11, a12, 0,
                                    a20, a21, a22, 0,
                                    0, 0, 0, 1,
                                    MatrixType.ROT_ARB);
        }


        case MatrixType.NO_W:
        {
             float d = 
                  m.m[0]*m.m[5]*m.m[10] +
                  m.m[1]*m.m[6]*m.m[8] +
                  m.m[2]*m.m[4]*m.m[9] +
                  -m.m[0]*m.m[6]*m.m[9] +
                  -m.m[1]*m.m[4]*m.m[10] +
                  -m.m[2]*m.m[5]*m.m[8];

             float inv_d = Pe.invert(d);

             float a00 = (m.m[5]*m.m[10]-m.m[6]*m.m[9]) * inv_d;
             float a01 = (m.m[2]*m.m[9]-m.m[1]*m.m[10]) * inv_d;
             float a02 = (m.m[1]*m.m[6]-m.m[2]*m.m[5]) * inv_d;

             float a10 = (m.m[6]*m.m[8]-m.m[4]*m.m[10]) * inv_d;
             float a11 = (m.m[0]*m.m[10]-m.m[2]*m.m[8]) * inv_d;
             float a12 = (m.m[2]*m.m[4]-m.m[0]*m.m[6]) * inv_d;

             float a20 = (m.m[4]*m.m[9]-m.m[5]*m.m[8]) * inv_d;
             float a21 = (m.m[1]*m.m[8]-m.m[0]*m.m[9]) * inv_d;
             float a22 = (m.m[0]*m.m[5]-m.m[1]*m.m[4]) * inv_d;

             float x = 
              (m.m[4]*m.m[10]*m.m[13] +
           m.m[5]*m.m[8]*m.m[14] + 
           m.m[6]*m.m[9]*m.m[12] +
           -m.m[4]*m.m[9]*m.m[14] +
           -m.m[5]*m.m[10]*m.m[12] +
           -m.m[6]*m.m[8]*m.m[13]) * inv_d;

             float y = 
              (m.m[0]*m.m[9]*m.m[14] +
           m.m[1]*m.m[10]*m.m[12] + 
           m.m[2]*m.m[8]*m.m[13] +
           -m.m[0]*m.m[10]*m.m[13] + 
           -m.m[1]*m.m[8]*m.m[14] +
           -m.m[2]*m.m[9]*m.m[12]) * inv_d; 

             float z =
              (m.m[0]*m.m[6]*m.m[13] +
           m.m[1]*m.m[4]*m.m[14] + 
           m.m[2]*m.m[5]*m.m[12] +
           -m.m[0]*m.m[5]*m.m[14] +
           -m.m[1]*m.m[6]*m.m[12] +
           -m.m[2]*m.m[4]*m.m[13]) * inv_d;
       
             return new Matrixf(a00,   a01,    a02,    0,
                                    a10,   a11,    a12,    0,
                                    a20,   a21,    a22,    0,
                                    x, y, z, 1,
                                    MatrixType.NO_W);
        }

        case MatrixType.ARBITRARY:
        {
             float d = 
                  m.m[0] * m.m[5] * m.m[10] * m.m[15] +
                  m.m[0] * m.m[6] * m.m[11] * m.m[13] +
                  m.m[0] * m.m[7] * m.m[9] * m.m[14] +
                  m.m[1] * m.m[4] * m.m[11] * m.m[14] +
                  m.m[1] * m.m[6] * m.m[8] * m.m[15] +
                  m.m[1] * m.m[7] * m.m[10] * m.m[12] +
                  m.m[2] * m.m[4] * m.m[9] * m.m[15] +
                  m.m[2] * m.m[5] * m.m[11] * m.m[12] +
                  m.m[2] * m.m[7] * m.m[8] * m.m[13] +
                  m.m[3] * m.m[4] * m.m[10] * m.m[13] +
                  m.m[3] * m.m[5] * m.m[8] * m.m[14] +
                  m.m[3] * m.m[6] * m.m[9] * m.m[12] +
                  -m.m[0] * m.m[5] * m.m[11] * m.m[14] +
                  -m.m[0] * m.m[6] * m.m[9] * m.m[15] +
                  -m.m[0] * m.m[7] * m.m[10] * m.m[13] +
                  -m.m[1] * m.m[4] * m.m[10] * m.m[15] + 
                  -m.m[1] * m.m[6] * m.m[11] * m.m[12] +
                  -m.m[1] * m.m[7] * m.m[8] * m.m[14] +
                  -m.m[2] * m.m[4] * m.m[11] * m.m[13] +
                  -m.m[2] * m.m[5] * m.m[8] * m.m[15] +
                  -m.m[2] * m.m[7] * m.m[9] * m.m[12] +
                  -m.m[3] * m.m[4] * m.m[9] * m.m[14] +
                  -m.m[3] * m.m[5] * m.m[10] * m.m[12] +
                  -m.m[3] * m.m[6] * m.m[8] * m.m[13];

             float inv_d = Pe.invert(d);

             float a00 =
                  m.m[5] * m.m[10] * m.m[15] +
                  m.m[6] * m.m[11] * m.m[13] + 
                  m.m[7] * m.m[9] * m.m[14] +
                  -m.m[5] * m.m[11] * m.m[14] +
                  -m.m[6] * m.m[9] * m.m[15] + 
                  -m.m[7] * m.m[10] * m.m[13]; 

             float a01 =
                  m.m[1] * m.m[11] * m.m[14] + 
                  m.m[2] * m.m[9] * m.m[15] +
                  m.m[3] * m.m[10] * m.m[13] +
                  -m.m[1] * m.m[10] * m.m[15] +
                  -m.m[2] * m.m[11] * m.m[13] +
                  -m.m[3] * m.m[9] * m.m[14];

             float a02 =
                  m.m[1] * m.m[6] * m.m[15] +
                  m.m[2] * m.m[7] * m.m[13] + 
                  m.m[3] * m.m[5] * m.m[14] +
                  -m.m[1] * m.m[7] * m.m[14] +
                  -m.m[2] * m.m[5] * m.m[15] + 
                  -m.m[3] * m.m[6] * m.m[13];

             float a03 =
                  m.m[1] * m.m[7] * m.m[10] + 
                  m.m[2] * m.m[5] * m.m[11] +
                  m.m[3] * m.m[6] * m.m[9] +
                  -m.m[1] * m.m[6] * m.m[11] +
                  -m.m[2] * m.m[7] * m.m[9] +
                  -m.m[3] * m.m[5] * m.m[10]; 

             float a10 = 
                  m.m[4] * m.m[11] * m.m[14] + 
                  m.m[6] * m.m[8] * m.m[15] +
                  m.m[7] * m.m[10] * m.m[12] +
                  -m.m[4] * m.m[10] * m.m[15] +
                  -m.m[6] * m.m[11] * m.m[12] +
                  -m.m[7] * m.m[8] * m.m[14];

             float a11 =
                  m.m[0] * m.m[10] * m.m[15] +
                  m.m[2] * m.m[11] * m.m[12] + 
                  m.m[3] * m.m[8] * m.m[14] +
                  -m.m[0] * m.m[11] * m.m[14] +
                  -m.m[2] * m.m[8] * m.m[15] + 
                  -m.m[3] * m.m[10] * m.m[12];

             float a12 =
                  m.m[0] * m.m[7] * m.m[14] + 
                  m.m[2] * m.m[4] * m.m[15] +
                  m.m[3] * m.m[6] * m.m[12] +
                  -m.m[0] * m.m[6] * m.m[15] +
                  -m.m[2] * m.m[7] * m.m[12] +
                  -m.m[3] * m.m[4] * m.m[14];

             float a13 =
                  m.m[0] * m.m[6] * m.m[11] +
                  m.m[2] * m.m[7] * m.m[8] + 
                  m.m[3] * m.m[4] * m.m[10] +
                  -m.m[0] * m.m[7] * m.m[10] +
                  -m.m[2] * m.m[4] * m.m[11] + 
                  -m.m[3] * m.m[6] * m.m[8];

             float a20 =
                  m.m[4] * m.m[9] * m.m[15] +
                  m.m[5] * m.m[11] * m.m[12] + 
                  m.m[7] * m.m[8] * m.m[13] +
                  -m.m[4] * m.m[11] * m.m[13] +
                  -m.m[5] * m.m[8] * m.m[15] + 
                  -m.m[7] * m.m[9] * m.m[12];

             float a21 = 
                  -m.m[0] * m.m[9] * m.m[15] +
                  -m.m[1] * m.m[11] * m.m[12] +
                  -m.m[3] * m.m[8] * m.m[13] + 
                  m.m[0] * m.m[11] * m.m[13] + 
                  m.m[1] * m.m[8] * m.m[15] +
                  m.m[3] * m.m[9] * m.m[12];

             float a22 = 
                  m.m[0] * m.m[5] * m.m[15] +
                  m.m[1] * m.m[7] * m.m[12] + 
                  m.m[3] * m.m[4] * m.m[13] +
                  -m.m[0] * m.m[7] * m.m[13] +
                  -m.m[1] * m.m[4] * m.m[15] + 
                  -m.m[3] * m.m[5] * m.m[12];

             float a23 = 
                  m.m[0] * m.m[7] * m.m[9] + 
                  m.m[1] * m.m[4] * m.m[11] +
                  m.m[3] * m.m[5] * m.m[8] +
                  -m.m[1] * m.m[7] * m.m[8] +
                  -m.m[3] * m.m[4] * m.m[9] + 
                  -m.m[0] * m.m[5] * m.m[11];

             float a30 = 
                  m.m[4] * m.m[10] * m.m[13] + 
                  m.m[5] * m.m[8] * m.m[14] +
                  m.m[6] * m.m[9] * m.m[12] +
                  -m.m[4] * m.m[9] * m.m[14] +
                  -m.m[5] * m.m[10] * m.m[12] +
                  -m.m[6] * m.m[8] * m.m[13]; 

             float a31 =
                  m.m[0] * m.m[9] * m.m[14] +
                  m.m[1] * m.m[10] * m.m[12] + 
                  m.m[2] * m.m[8] * m.m[13] +
                  -m.m[0] * m.m[10] * m.m[13] +
                  -m.m[1] * m.m[8] * m.m[14] + 
                  -m.m[2] * m.m[9] * m.m[12];

             float a32 =
                  m.m[0] * m.m[6] * m.m[13] + 
                  m.m[1] * m.m[4] * m.m[14] +
                  m.m[2] * m.m[5] * m.m[12] +
                  -m.m[0] * m.m[5] * m.m[14] +
                  -m.m[1] * m.m[6] * m.m[12] +
                  -m.m[2] * m.m[4] * m.m[13]; 

             float a33 =
                  m.m[0] * m.m[5] * m.m[10] +
                  m.m[1] * m.m[6] * m.m[8] + 
                  m.m[2] * m.m[4] * m.m[9] +
                  -m.m[0] * m.m[6] * m.m[9] +
                  -m.m[1] * m.m[4] * m.m[10] + 
                  -m.m[2] * m.m[5] * m.m[8];

             return new Matrixf(a00*inv_d, a01*inv_d, a02*inv_d, a03*inv_d,
                                    a10*inv_d, a11*inv_d, a12*inv_d, a13*inv_d,
                                    a20*inv_d, a21*inv_d, a22*inv_d, a23*inv_d,
                                    a30*inv_d, a31*inv_d, a32*inv_d, a33*inv_d,
                                    MatrixType.ARBITRARY);
        }
        }

        return new Matrixf();  // shut the compiler up
   }
    
    
    
    private static Matrixf handleFrustum(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // FRUSTUM * IDENTITY
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               a.m[8],
                               a.m[9],
                               a.m[10],
                               a.m[11],

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.FRUSTUM);

            case MatrixType.SCALE:        // FRUSTUM * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.FRUSTUM);

            case MatrixType.XLATE:        // FRUSTUM * XLATE
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               a.m[8],
                               a.m[9],
                               a.m[10],
                               a.m[11],

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[10]*b.m[14] + a.m[14],
                               a.m[11]*b.m[14] + 1,

                               MatrixType.ARBITRARY);

            case MatrixType.SCALE_XLATE:      // FRUSTUM * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[10]*b.m[14] + a.m[14],
                               a.m[11]*b.m[14] + 1,

                               MatrixType.ARBITRARY);

            case MatrixType.FRUSTUM:      // FRUSTUM * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               a.m[11]*b.m[10] + b.m[11],

                               a.m[8]*b.m[14],
                               a.m[9]*b.m[14],
                               a.m[10]*b.m[14] + a.m[14],
                               a.m[11]*b.m[14] + 1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // FRUSTUM * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               a.m[8]*b.m[6],
                               a.m[9]*b.m[6],
                               a.m[10]*b.m[6],
                               a.m[11]*b.m[6],

                               0,
                               a.m[5]*b.m[9],
                               0,
                               0,

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Y_90:         // FRUSTUM * ROT_Y_90
              return new Matrixf(a.m[8]*b.m[2],
                               a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               a.m[11]*b.m[2],

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Z_90:         // FRUSTUM * ROT_Z_90
              return new Matrixf(0,
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
                               0,
                               0,
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X:        // FRUSTUM * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               a.m[8]*b.m[6],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[10]*b.m[6],
                               a.m[11]*b.m[6],

                               a.m[8]*b.m[10],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Y:        // FRUSTUM * ROT_Y
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               a.m[11]*b.m[2],

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Z:        // FRUSTUM * ROT_Z
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_ARB:      // FRUSTUM * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               a.m[11]*b.m[2],

                               a.m[0]*b.m[4] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[10]*b.m[6],
                               a.m[11]*b.m[6],

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               0,
                               0,
                               a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.NO_W:         // FRUSTUM * NO_W
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               a.m[11]*b.m[2],

                               a.m[0]*b.m[4] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[10]*b.m[6],
                               a.m[11]*b.m[6],

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[10]*b.m[14] + a.m[14],
                               a.m[11]*b.m[14] + 1,

                               MatrixType.ARBITRARY);

            case MatrixType.ARBITRARY:        // FRUSTUM * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[10]*b.m[2] + a.m[14]*b.m[3],
                               a.m[11]*b.m[2] + b.m[3],

                               a.m[0]*b.m[4] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[10]*b.m[6] + a.m[14]*b.m[7],
                               a.m[11]*b.m[6] + b.m[7],

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               a.m[11]*b.m[10] + b.m[11],

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[10]*b.m[14] + a.m[14]*b.m[15],
                               a.m[11]*b.m[14] + b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    public static Matrixf multiply(Matrixf a, Matrixf b) 
    {

        switch(a.type) 
        {

        case MatrixType.IDENTITY:
            return MatrixfUtil.handleIdentity(a, b);

        case MatrixType.SCALE:
            return MatrixfUtil.handleScale(a, b);
        case MatrixType.XLATE:
            return MatrixfUtil.handleXlate(a, b);
        case MatrixType.SCALE_XLATE:
            return MatrixfUtil.handleScale_Xlate(a, b);
        case MatrixType.FRUSTUM:
            return handleFrustum(a, b);
        case MatrixType.ROT_X_90:
            return MatrixfUtil3.handleROT_X_90(a, b);
        case MatrixType.ROT_Y_90:
            return MatrixfUtil3.handleROT_Y_90(a, b);
        case MatrixType.ROT_Z_90:
            return MatrixfUtil3.handleROT_Z_90(a, b);
        case MatrixType.ROT_X:
            return MatrixfUtil2.handleROT_X(a, b);
        case MatrixType.ROT_Y:
            return MatrixfUtil2.handleROT_Y(a, b);
        case MatrixType.ROT_Z:
            return MatrixfUtil2.handleROT_Z(a, b);
        case MatrixType.ROT_ARB:
            return MatrixfUtil2.handleROT_ARB(a, b);
        case MatrixType.NO_W:
            return MatrixfUtil3.handleNO_W(a, b);
        case MatrixType.ARBITRARY:
            return MatrixfUtil4.handleARBITRARY(a, b);
        }

        // Should never reach this. Adding it to shut the compiler up.
        return new Matrixf();
    }
}

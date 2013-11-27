/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidMatrix4f.java
 *
 */
package com.telenav.tnui.widget.android;

import android.opengl.Matrix;

import com.telenav.tnui.opengles.TnMatrix4f;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Sep 8, 2010
 */
class AndroidMatrix4f extends TnMatrix4f
{
    protected AndroidMatrix4f()
    {
        super();
    }
    
    public void frustumM(float[] m, int offset, float left, float right, float bottom, float top, float near, float far)
    {
        Matrix.frustumM(m, offset, left, right, bottom, top, near, far);
    }

    public void invertM(float[] mInv, int mInvOffset, float[] m, int mOffset)
    {
        Matrix.invertM(mInv, mInvOffset, m, mOffset);
    }

    public void multiplyMM(float[] result, int resultOffset, float[] lhs, int lhsOffset, float[] rhs, int rhsOffset)
    {
        Matrix.multiplyMM(result, resultOffset, lhs, lhsOffset, rhs, rhsOffset);
    }

//    public void multiplyMV(float[] resultVec, int resultVecOffset, float[] lhsMat, int lhsMatOffset, float[] rhsVec, int rhsVecOffset)
//    {
//        Matrix.multiplyMV(resultVec, resultVecOffset, lhsMat, lhsMatOffset, rhsVec, rhsVecOffset);
//    }

    public void orthoM(float[] m, int mOffset, float left, float right, float bottom, float top, float near, float far)
    {
        Matrix.orthoM(m, mOffset, left, right, bottom, top, near, far);
    }

    public void rotateM(float[] rm, int rmOffset, float[] m, int mOffset, float a, float x, float y, float z)
    {
        Matrix.rotateM(rm, rmOffset, m, mOffset, a, x, y, z);
    }

    public void rotateM(float[] m, int mOffset, float a, float x, float y, float z)
    {
        Matrix.rotateM(m, mOffset, a, x, y, z);
    }

    public void scaleM(float[] m, int mOffset, float x, float y, float z)
    {
        Matrix.scaleM(m, mOffset, x, y, z);
    }

    public void scaleM(float[] sm, int smOffset, float[] m, int mOffset, float x, float y, float z)
    {
        Matrix.scaleM(sm, smOffset, m, mOffset, x, y, z);
    }

    public void setIdentityM(float[] sm, int smOffset)
    {
        Matrix.setIdentityM(sm, smOffset);
    }

    public void setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ,
            float upX, float upY, float upZ)
    {
        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;

        // Normalize f
        float rlf = 1.0f / Matrix.length(fx, fy, fz);
        fx *= rlf;
        fy *= rlf;
        fz *= rlf;

        // compute s = f x up (x means "cross product")
        float sx = fy * upZ - fz * upY;
        float sy = fz * upX - fx * upZ;
        float sz = fx * upY - fy * upX;

        // and normalize s
        float rls = 1.0f / Matrix.length(sx, sy, sz);
        sx *= rls;
        sy *= rls;
        sz *= rls;

        // compute u = s x f
        float ux = sy * fz - sz * fy;
        float uy = sz * fx - sx * fz;
        float uz = sx * fy - sy * fx;

        rm[rmOffset + 0] = sx;
        rm[rmOffset + 1] = ux;
        rm[rmOffset + 2] = -fx;
        rm[rmOffset + 3] = 0.0f;

        rm[rmOffset + 4] = sy;
        rm[rmOffset + 5] = uy;
        rm[rmOffset + 6] = -fy;
        rm[rmOffset + 7] = 0.0f;

        rm[rmOffset + 8] = sz;
        rm[rmOffset + 9] = uz;
        rm[rmOffset + 10] = -fz;
        rm[rmOffset + 11] = 0.0f;

        rm[rmOffset + 12] = 0.0f;
        rm[rmOffset + 13] = 0.0f;
        rm[rmOffset + 14] = 0.0f;
        rm[rmOffset + 15] = 1.0f;

        translateM(rm, rmOffset, -eyeX, -eyeY, -eyeZ);
    }

    public void setRotateEulerM(float[] rm, int rmOffset, float x, float y, float z)
    {
        Matrix.setRotateEulerM(rm, rmOffset, x, y, z);
    }

    public void setRotateM(float[] rm, int rmOffset, float a, float x, float y, float z)
    {
        Matrix.setRotateM(rm, rmOffset, a, x, y, z);
    }

    public void translateM(float[] tm, int tmOffset, float[] m, int mOffset, float x, float y, float z)
    {
        Matrix.translateM(tm, tmOffset, m, mOffset, x, y, z);
    }

    public void translateM(float[] m, int mOffset, float x, float y, float z)
    {
        Matrix.translateM(m, mOffset, x, y, z);
    }

    public void transposeM(float[] mTrans, int mTransOffset, float[] m, int mOffset)
    {
        Matrix.transposeM(mTrans, mTransOffset, m, mOffset);
    }

}

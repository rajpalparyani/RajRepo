/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMatrix4f.java
 *
 */
package com.telenav.tnui.opengles;

/**
 * Defines a 4 x 4 floating point matrix representing a 3D transformation. <br />
 * This matrix class is directly compatible with OpenGL since its elements are laid out in memory exactly as they are
 * expected by OpenGL. The matrix uses column-major format such that array indices increase down column first.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Sep 8, 2010
 */
public abstract class TnMatrix4f
{
    private static int initCount;
    
    private static TnMatrix4f instance;
    
    protected TnMatrix4f()
    {
        
    }
    
    /**
     * Retrieve the instance of TnMatrix4f.
     * 
     * @return the instance of TnMatrix4f.
     */
    public static TnMatrix4f getInstace()
    {
        return instance;
    }
    
    /**
     * init platform's implementation.
     * 
     * @param matrix
     */
    public static void init(TnMatrix4f matrix)
    {
        if(initCount >= 1)
            return;
        
        instance = matrix;
        initCount++;
    }
    
    /**
     * Define a projection matrix in terms of six clip planes
     * @param m the float array that holds the perspective matrix
     * @param offset the offset into float array m where the perspective matrix data is written
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public abstract void frustumM(float[] m, int offset, float left, float right, float bottom, float top, float near, float far);
    
    /**
     * Inverts a 4 x 4 matrix.
     * 
     * @param mInv the array that holds the output inverted matrix
     * @param mInvOffset an offset into mInv where the inverted matrix is stored.
     * @param m the input array
     * @param mOffset an offset into m where the matrix is stored.
     */
    public abstract void invertM(float[] mInv, int mInvOffset, float[] m, int mOffset);
    
    /**
     * Computes the length of a vector
     * 
     * @param x x coordinate of a vector
     * @param y y coordinate of a vector
     * @param z z coordinate of a vector
     * @return the length of a vector 
     */
    public float length(float x, float y, float z)
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * Multiply two 4x4 matrices together and store the result in a third 4x4 matrix. In matrix notation: result = lhs x
     * rhs. Due to the way matrix multiplication works, the result matrix will have the same effect as first multiplying
     * by the rhs matrix, then multiplying by the lhs matrix. This is the opposite of what you might expect. The same
     * float array may be passed for result, lhs, and/or rhs. However, the result element values are undefined if the
     * result elements overlap either the lhs or rhs elements.
     * 
     * @param result The float array that holds the result.
     * @param resultOffset The offset into the result array where the result is stored.
     * @param lhs The float array that holds the left-hand-side matrix.
     * @param lhsOffset The offset into the lhs array where the lhs is stored
     * @param rhs The float array that holds the right-hand-side matrix.
     * @param rhsOffset The offset into the rhs array where the rhs is stored.
     */
    public abstract void multiplyMM(float[] result, int resultOffset, float[] lhs, int lhsOffset, float[] rhs, int rhsOffset);
    
    /**
     * Multiply a 4 element vector by a 4x4 matrix and store the result in a 4 element column vector. In matrix
     * notation: result = lhs x rhs The same float array may be passed for resultVec, lhsMat, and/or rhsVec. However,
     * the resultVec element values are undefined if the resultVec elements overlap either the lhsMat or rhsVec
     * elements.
     * 
     * @param resultVec The float array that holds the result vector.
     * @param resultVecOffset The offset into the result array where the result vector is stored.
     * @param lhsMat    The float array that holds the left-hand-side matrix.
     * @param lhsMatOffset The offset into the lhs array where the lhs is stored
     * @param rhsVec The float array that holds the right-hand-side vector.
     * @param rhsVecOffset The offset into the rhs vector where the rhs vector is stored.
     */
//    public abstract void multiplyMV(float[] resultVec, int resultVecOffset, float[] lhsMat, int lhsMatOffset, float[] rhsVec,
//            int rhsVecOffset);
    
    /**
     * Computes an orthographic projection matrix.
     * 
     * @param m returns the result
     * @param mOffset
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public abstract void orthoM(float[] m, int mOffset, float left, float right, float bottom, float top, float near, float far);
    
    /**
     * Rotates matrix m by angle a (in degrees) around the axis (x, y, z)
     * 
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z 
     */
    public abstract void rotateM(float[] rm, int rmOffset, float[] m, int mOffset, float a, float x, float y, float z);
    
    /**
     * Rotates matrix m in place by angle a (in degrees) around the axis (x, y, z)
     * 
     * @param m source matrix
     * @param mOffset index into m where the matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    public abstract void rotateM(float[] m, int mOffset, float a, float x, float y, float z);
    
    /**
     * Scales matrix m in place by sx, sy, and sz
     * 
     * @param m matrix to scale
     * @param mOffset index into m where the matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    public abstract void scaleM(float[] m, int mOffset, float x, float y, float z);
    
    /**
     * Scales matrix m by x, y, and z, putting the result in sm
     * 
     * @param sm returns the result
     * @param smOffset index into sm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z 
     */
    public abstract void scaleM(float[] sm, int smOffset, float[] m, int mOffset, float x, float y, float z);
    
    /**
     * Sets matrix m to the identity matrix.
     * 
     * @param sm returns the result
     * @param smOffset index into sm where the result matrix starts 
     */
    public abstract void setIdentityM(float[] sm, int smOffset);
    
    /**
     * Define a viewing transformation in terms of an eye point, a center of view, and an up vector.
     * 
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param eyeX eye point X
     * @param eyeY eye point Y
     * @param eyeZ eye point Z
     * @param centerX center of view X
     * @param centerY center of view Y
     * @param centerZ center of view Z
     * @param upX up vector X
     * @param upY up vector Y
     * @param upZ up vector Z
     */
    public abstract void setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY,
            float centerZ, float upX, float upY, float upZ);
    
    /**
     * Converts Euler angles to a rotation matrix
     * 
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param x Angle of rotation, in degrees
     * @param y Angle of rotation, in degrees
     * @param z Angle of rotation, in degrees
     */
    public abstract void setRotateEulerM(float[] rm, int rmOffset, float x, float y, float z);
    
    /**
     * Rotates matrix m by angle a (in degrees) around the axis (x, y, z)
     * 
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor Y
     * @param z scale factor Z
     */
    public abstract void setRotateM(float[] rm, int rmOffset, float a, float x, float y, float z);
    
    /**
     * Translates matrix m by x, y, and z, putting the result in tm
     * 
     * @param tm returns the result
     * @param tmOffset index into sm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param x translation factor x
     * @param y translation factor y
     * @param z translation factor z
     */
    public abstract void translateM(float[] tm, int tmOffset, float[] m, int mOffset, float x, float y, float z);
    
    /**
     * Translates matrix m by x, y, and z in place.
     * 
     * @param m matrix
     * @param mOffset index into m where the matrix starts
     * @param x translation factor x
     * @param y translation factor y
     * @param z translation factor z
     */
    public abstract void translateM(float[] m, int mOffset, float x, float y, float z);
    
    /**
     * Transposes a 4 x 4 matrix.
     * 
     * @param mTrans the array that holds the output inverted matrix
     * @param mTransOffset an offset into mInv where the inverted matrix is stored.
     * @param m the input array
     * @param mOffset an offset into m where the matrix is stored. 
     */
    public abstract void transposeM(float[] mTrans, int mTransOffset, float[] m, int mOffset);
}

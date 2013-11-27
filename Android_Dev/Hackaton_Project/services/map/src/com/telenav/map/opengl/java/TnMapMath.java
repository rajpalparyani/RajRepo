/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapMath.java
 *
 */
package com.telenav.map.opengl.java;


/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-1
 */
public class TnMapMath
{
    public static final int TOP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    
    public static float[] identity = new float[]{
        1.0f, 0.0f, 0.0f, 0.0f,
             0.0f, 1.0f, 0.0f, 0.0f,
             0.0f, 0.0f, 1.0f, 0.0f,
             0.0f, 0.0f, 0.0f, 1.0f
             };

   public static void loadIdentity(float matrix[])
   {
       System.arraycopy(identity, 0, matrix, 0, identity.length);
   }


   public static void getGlobalXYIndex(int[] xIndex, int[] yIndex, long tileId)
   {
        xIndex[0] = (int)((tileId >> 24) & 0xFFFFFF);
        yIndex[0] = (int)(tileId & 0xFFFFFF);
   }

   public static long getGlobalId(int xIndex, int yIndex)
   {
        long tileId = ((long)xIndex << 24) | yIndex;
       
        return tileId;
   }

   public static int getGlobalXIndex(int globalX, int zoom, int nTileBits)
   {
        int xIndex = globalX >> (nTileBits + zoom);
       
        return xIndex;
   }

   public static int getGlobalYIndex(int globalY, int zoom, int nTileBits)
   {
       int yIndex = globalY >> (nTileBits + zoom);
       
        return yIndex;
   }

   public static void combinTileIdAndZoom(long[] arrIds, int zoom, int count)
   {
        for(int i = 0; i < count; i++)
        {
            arrIds[i] = (arrIds[i] & 0xFFFFFFFFFFFFL) | (((long)zoom) << 48);
        }
   }

   public static long combinTileIdAndZoom(long Id, int zoom)
   {
        return (Id & 0xFFFFFFFFFFFFL) | (((long)zoom) << 48);
   }

   public static void splitTileIdAndZoom(long[] nId, int[] zoom)
   {
        zoom[0] = (int)((nId[0] >> 48) & 0xFF);
       
        nId[0] &= 0xFFFFFFFFFFFFL;
   }
   
   public static long extractTileId(long id)
   {
       return id & 0xFFFFFFFFFFFFL;
   }
   
   public static int extractZoom(long id)
   {
       return (int)((id >> 48) & 0xFF);
   }

   public static void getTileNeighborIds(long nId, int zoom, long[] neighborIds)
   {
        //Unpack ID
        long[] tmpNId = new long[]{nId};
        int[] tmpZoom = new int[1];
        splitTileIdAndZoom(tmpNId,tmpZoom);
        int[] xIndex = new int[1];
        int[] yIndex = new int[1];
        getGlobalXYIndex(xIndex,yIndex,tmpNId[0]);
       
        // Calculate neighbor tiles, wrapping around date line
        int MAX_INDEX = (1 << (19-zoom))-1;
        int south = yIndex[0] > 0 ? yIndex[0]-1 : MAX_INDEX;
        int east = xIndex[0] < MAX_INDEX ? xIndex[0]+1 : 0;
        int west = xIndex[0] > 0 ? xIndex[0]-1 : MAX_INDEX;
        int north = yIndex[0] < MAX_INDEX ? yIndex[0]+1 : 0;
       
        //Assign neighbor tileIDs
        neighborIds[0] = combinTileIdAndZoom(getGlobalId(xIndex[0],south),zoom);
        neighborIds[1] = combinTileIdAndZoom(getGlobalId(east,yIndex[0]),zoom);
        neighborIds[2] = combinTileIdAndZoom(getGlobalId(west,yIndex[0]),zoom);
        neighborIds[3] = combinTileIdAndZoom(getGlobalId(xIndex[0],north),zoom);

   }

   public static int mat(int row, int col)
   {
       return (col<<2)+row;
   }
   
   public static void ortho(float[] m, float left, float right, float bottom, float top, float nearval, float farval)
   {   
        //LoadIdentity(m);
       
        m[mat(0,0)] = 2.0F / (right-left);
        m[mat(0,1)] = 0.0F;
        m[mat(0,2)] = 0.0F;
        m[mat(0,3)] = -(right+left) / (right-left);
       
        m[mat(1,0)] = 0.0F;
        m[mat(1,1)] = 2.0F / (top-bottom);
        m[mat(1,2)] = 0.0F;
        m[mat(1,3)] = -(top+bottom) / (top-bottom);
       
        m[mat(2,0)] = 0.0F;
        m[mat(2,1)] = 0.0F;
        m[mat(2,2)] = -2.0F / (farval-nearval);
        m[mat(2,3)] = -(farval+nearval) / (farval-nearval);
       
        m[mat(3,0)] = 0.0F;
        m[mat(3,1)] = 0.0F;
        m[mat(3,2)] = 0.0F;
        m[mat(3,3)] = 1.0F;
   }


   public static void frustum(float m[], float left, float right, float bottom, float top, float nearval, float farval )
   {
        float x, y, a, b, c, d;
       
        x = (2.0F*nearval) / (right-left);
        y = (2.0F*nearval) / (top-bottom);
        a = (right+left) / (right-left);
        b = (top+bottom) / (top-bottom);
        c = -(farval+nearval) / ( farval-nearval);
        d = -(2.0F*farval*nearval) / (farval-nearval);  /* error? */
       
        m[mat(0,0)] = x;     
        m[mat(0,1)] = 0.0F;  
        m[mat(0,2)] = a;      
        m[mat(0,3)] = 0.0F;
       
        m[mat(1,0)] = 0.0F;  
        m[mat(1,1)] = y;     
        m[mat(1,2)] = b;      
        m[mat(1,3)] = 0.0F;
       
        m[mat(2,0)] = 0.0F;  
        m[mat(2,1)] = 0.0F;  
        m[mat(2,2)] = c;      
        m[mat(2,3)] = d;
       
        m[mat(3,0)] = 0.0F;  
        m[mat(3,1)] = 0.0F;  
        m[mat(3,2)] = -1.0F;  
        m[mat(3,3)] = 0.0F;
   }

   public static void matrixMult(float[]p, float[]a, float[]b)
   {
        int i = 0;
       
        for (i = 0; i < 4; i++) {
             float ai0=a[mat(i,0)]; float ai1=a[mat(i,1)]; float  ai2=a[mat(i,2)]; float ai3=a[mat(i,3)];
             p[mat(i,0)] = ai0 * b[mat(0,0)] + ai1 * b[mat(1,0)] + ai2 * b[mat(2,0)] + ai3 * b[mat(3,0)];
             p[mat(i,1)] = ai0 * b[mat(0,1)] + ai1 * b[mat(1,1)] + ai2 * b[mat(2,1)] + ai3 * b[mat(3,1)];
             p[mat(i,2)] = ai0 * b[mat(0,2)] + ai1 * b[mat(1,2)] + ai2 * b[mat(2,2)] + ai3 * b[mat(3,2)];
             p[mat(i,3)] = ai0 * b[mat(0,3)] + ai1 * b[mat(1,3)] + ai2 * b[mat(2,3)] + ai3 * b[mat(3,3)];
        }
   }

   public static void matrixMultVec(double out[], double in[], float matrix[])
   {
        int i = 0;
       
        for (i=0; i<4; i++) {
             out[i] = 
                  in[0] * matrix[mat( i, 0)] +
                  in[1] * matrix[mat( i, 1)] +
                  in[2] * matrix[mat( i, 2)] +
                  in[3] * matrix[mat( i, 3)];
        }
   }

   public static boolean matrixInvert(float invOut[],  float m[])
   {
        double inv[] = new double[16];
        double det = 0;
        int i;
       
        inv[0] = (double)m[5]*m[10]*m[15] -    (double)m[5]*m[11]*m[14] - (double)m[9]*m[6]*m[15] + 
             (double)m[9]*m[7]*m[14]  +  (double)m[13]*m[6]*m[11] - (double)m[13]*m[7]*m[10];
       
        inv[4] = (double)-m[4]*m[10]*m[15] + (double)m[4]*m[11]*m[14] + (double)m[8]*m[6]*m[15] - 
             (double)m[8]*m[7]*m[14] - (double)m[12]*m[6]*m[11] + (double)m[12]*m[7]*m[10];
       
        inv[8] = (double)m[4]*m[9]*m[15] - (double)m[4]*m[11]*m[13] - (double)m[8]*m[5]*m[15] + 
             (double)m[8]*m[7]*m[13] + (double)m[12]*m[5]*m[11] - (double)m[12]*m[7]*m[9];
       
        inv[12] = (double)-m[4]*m[9]*m[14] +   (double)m[4]*m[10]*m[13] + (double)m[8]*m[5]*m[14] - 
             (double)m[8]*m[6]*m[13] - (double)m[12]*m[5]*m[10] + (double)m[12]*m[6]*m[9];
       
        inv[1] = (double)-m[1]*m[10]*m[15] + (double)m[1]*m[11]*m[14] + (double)m[9]*m[2]*m[15] - 
             (double)m[9]*m[3]*m[14] - (double)m[13]*m[2]*m[11] + (double)m[13]*m[3]*m[10];
       
        inv[5] = (double)m[0]*m[10]*m[15] -    (double)m[0]*m[11]*m[14] - (double)m[8]*m[2]*m[15] + 
             (double)m[8]*m[3]*m[14] + (double)m[12]*m[2]*m[11] - (double)m[12]*m[3]*m[10];
       
        inv[9] = (double)-m[0]*m[9]*m[15] +    (double)m[0]*m[11]*m[13] + (double)m[8]*m[1]*m[15] - 
             (double)m[8]*m[3]*m[13] - (double)m[12]*m[1]*m[11] + (double)m[12]*m[3]*m[9];
       
        inv[13] = (double)m[0]*m[9]*m[14] -    (double)m[0]*m[10]*m[13] - (double)m[8]*m[1]*m[14] + 
             (double)m[8]*m[2]*m[13] + (double)m[12]*m[1]*m[10] - (double)m[12]*m[2]*m[9];
       
        inv[2] = (double)m[1]*m[6]*m[15] - (double)m[1]*m[7]*m[14] - (double)m[5]*m[2]*m[15] + 
             (double)m[5]*m[3]*m[14] + (double)m[13]*m[2]*m[7] - (double)m[13]*m[3]*m[6];
       
        inv[6] = (double)-m[0]*m[6]*m[15] +  (double)m[0]*m[7]*m[14] + (double)m[4]*m[2]*m[15] - 
             (double)m[4]*m[3]*m[14] - (double)m[12]*m[2]*m[7] + (double)m[12]*m[3]*m[6];
       
        inv[10] = (double)m[0]*m[5]*m[15] -    (double)m[0]*m[7]*m[13] - (double)m[4]*m[1]*m[15] + 
             (double)m[4]*m[3]*m[13] + (double)m[12]*m[1]*m[7] - (double)m[12]*m[3]*m[5];
       
        inv[14] = (double)-m[0]*m[5]*m[14] +   (double)m[0]*m[6]*m[13] + (double)m[4]*m[1]*m[14] - 
             (double)m[4]*m[2]*m[13] - (double)m[12]*m[1]*m[6] + (double)m[12]*m[2]*m[5];
       
        inv[3] =  (double)-m[1]*m[6]*m[11] +   (double)m[1]*m[7]*m[10] + (double)m[5]*m[2]*m[11] - 
             (double)m[5]*m[3]*m[10] - (double)m[9]*m[2]*m[7] +  (double)m[9]*m[3]*m[6];
       
        inv[7] =  (double)m[0]*m[6]*m[11] -    (double)m[0]*m[7]*m[10] - (double)m[4]*m[2]*m[11] + 
             (double)m[4]*m[3]*m[10] + (double)m[8]*m[2]*m[7] -  (double)m[8]*m[3]*m[6];
       
        inv[11] = (double)-m[0]*m[5]*m[11] +   (double)m[0]*m[7]*m[9] +  (double)m[4]*m[1]*m[11] - 
             (double)m[4]*m[3]*m[9] -  (double)m[8]*m[1]*m[7] +  (double)m[8]*m[3]*m[5];
       
        inv[15] = (double)m[0]*m[5]*m[10] -    (double)m[0]*m[6]*m[9] -  (double)m[4]*m[1]*m[10] + 
             (double)m[4]*m[2]*m[9] +  (double)m[8]*m[1]*m[6] -  (double)m[8]*m[2]*m[5];
       
        det = m[0]*inv[0] + m[1]*inv[4] + m[2]*inv[8] + m[3]*inv[12];
       
        if (det == 0) return false;
       
        det = 1.0 / det;
       
        for (i = 0; i < 16; i++)
        {
             invOut[i] = (float)(inv[i] * det);
        }
       
        return true;
   }


   public static void scale(float m[],  float x,  float y, float z)
   {
       
        m[0] *= x;   m[4] *= y;   m[8]  *= z;
        m[1] *= x;   m[5] *= y;   m[9]  *= z;
        m[2] *= x;   m[6] *= y;   m[10] *= z;
        m[3] *= x;   m[7] *= y;   m[11] *= z;
   }

   public static void translate(float m[], float x, float y, float z)
   {
        m[12] = m[0] * x + m[4] * y + m[8]  * z + m[12];
        m[13] = m[1] * x + m[5] * y + m[9]  * z + m[13];
        m[14] = m[2] * x + m[6] * y + m[10] * z + m[14];
        m[15] = m[3] * x + m[7] * y + m[11] * z + m[15];
   }
   
   public static double DEG2RAD = Math.PI/180;

   public static int getM(int col, int row)
   {
       return col*4+row;
   }
   
   public static void rotate(float matrix[], float angle, float x, float y, float z)
   {
        float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c, s, c;
        float m[] = new float[16];
        boolean optimized = false;
       
        s = (float) Math.sin( angle * DEG2RAD );
        c = (float) Math.cos( angle * DEG2RAD );
       
        loadIdentity(m);
       
        optimized = false;
       
        if (x == 0.0F) {
             if (y == 0.0F) {
                  if (z != 0.0F) {
                       optimized = true;
                       /* rotate only around z-axis */
                       m[getM(0,0)] = c;
                       m[getM(1,1)] = c;
                       if (z < 0.0F) {
                            m[getM(0,1)] = s;
                            m[getM(1,0)] = -s;
                       }
                       else {
                            m[getM(0,1)] = -s;
                            m[getM(1,0)] = s;
                       }
                  }
             }
             else if (z == 0.0F) {
                  optimized = true;
                  /* rotate only around y-axis */
                  m[getM(0,0)] = c;
                  m[getM(2,2)] = c;
                  if (y < 0.0F) {
                       m[getM(0,2)] = -s;
                       m[getM(2,0)] = s;
                  }
                  else {
                       m[getM(0,2)] = s;
                       m[getM(2,0)] = -s;
                  }
             }
        }
        else if (y == 0.0F) {
             if (z == 0.0F) {
                  optimized = true;
                  /* rotate only around x-axis */
                  m[getM(1,1)] = c;
                  m[getM(2,2)] = c;
                  if (x < 0.0F) {
                       m[getM(1,2)] = s;
                       m[getM(2,1)] = -s;
                  }
                  else {
                       m[getM(1,2)] = -s;
                       m[getM(2,1)] = s;
                  }
             }
        }
       
        if (!optimized) {
             float mag = (float)Math.sqrt(x * x + y * y + z * z);
           
             if (mag <= 1.0e-4) {
                  /* no rotation, leave mat as-is */
                  return;
             }
           
             x /= mag;
             y /= mag;
             z /= mag;
           
           
             /*
              *     Arbitrary axis rotation matrix.
              *
              *  This is composed of 5 matrices, Rz, Ry, T, Ry', Rz', multiplied
              *  like so:  Rz * Ry * T * Ry' * Rz'.  T is the final rotation
              *  (which is about the X-axis), and the two composite transforms
              *  Ry' * Rz' and Rz * Ry are (respectively) the rotations necessary
              *  from the arbitrary axis to the X-axis then back.  They are
              *  all elementary rotations.
              *
              *  Rz' is a rotation about the Z-axis, to bring the axis vector
              *  into the x-z plane.  Then Ry' is applied, rotating about the
              *  Y-axis to bring the axis vector parallel with the X-axis.  The
              *  rotation about the X-axis is then performed.  Ry and Rz are
              *  simply the respective inverse transforms to bring the arbitrary
              *  axis back to it's original orientation.  The first transforms
              *  Rz' and Ry' are considered inverses, since the data from the
              *  arbitrary axis gives you info on how to get to it, not how
              *  to get away from it, and an inverse must be applied.
              *
              *  The basic calculation used is to recognize that the arbitrary
              *  axis vector (x, y, z), since it is of unit length, actually
              *  represents the sines and cosines of the angles to rotate the
              *  X-axis to the same orientation, with theta being the angle about
              *  Z and phi the angle about Y (in the order described above)
              *  as follows:
              *
              *  cos ( theta ) = x / sqrt ( 1 - z^2 )
              *  sin ( theta ) = y / sqrt ( 1 - z^2 )
              *
              *  cos ( phi ) = sqrt ( 1 - z^2 )
              *  sin ( phi ) = z
              *
              *  Note that cos ( phi ) can further be inserted to the above
              *  formulas:
              *
              *  cos ( theta ) = x / cos ( phi )
              *  sin ( theta ) = y / sin ( phi )
              *
              *  ...etc.  Because of those relations and the standard trigonometric
              *  relations, it is pssible to reduce the transforms down to what
              *  is used below.  It may be that any primary axis chosen will give the
              *  same results (modulo a sign convention) using thie method.
              *
              *  Particularly nice is to notice that all divisions that might
              *  have caused trouble when parallel to certain planes or
              *  axis go away with care paid to reducing the expressions.
              *  After checking, it does perform correctly under all cases, since
              *  in all the cases of division where the denominator would have
              *  been zero, the numerator would have been zero as well, giving
              *  the expected result.
              */
           
             xx = x * x;
             yy = y * y;
             zz = z * z;
             xy = x * y;
             yz = y * z;
             zx = z * x;
             xs = x * s;
             ys = y * s;
             zs = z * s;
             one_c = 1.0F - c;
           
             /* We already hold the identity-matrix so we can skip some statements */
             m[getM(0,0)] = (one_c * xx) + c;
             m[getM(0,1)] = (one_c * xy) - zs;
             m[getM(0,2)] = (one_c * zx) + ys;
             /*    M(0,3) = 0.0F; */
           
             m[getM(1,0)] = (one_c * xy) + zs;
             m[getM(1,1)] = (one_c * yy) + c;
             m[getM(1,2)] = (one_c * yz) - xs;
             /*    M(1,3) = 0.0F; */
           
             m[getM(2,0)] = (one_c * zx) - ys;
             m[getM(2,1)] = (one_c * yz) + xs;
             m[getM(2,2)] = (one_c * zz) + c;
        }
       
        matrixMult(matrix, matrix, m);
   }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MatrixfUtil4.java
 *
 */
package com.telenav.map.opengl.java.math;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-19
 */
public class MatrixfUtil4
{
    public static Matrixf handleARBITRARY(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ARBITRARY * IDENTITY
              return new Matrixf(a.m[0],
                               a.m[1],
                               a.m[2],
                               a.m[3],

                               a.m[4],
                               a.m[5],
                               a.m[6],
                               a.m[7],

                               a.m[8],
                               a.m[9],
                               a.m[10],
                               a.m[11],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.SCALE:        // ARBITRARY * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               a.m[3]*b.m[0],

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               a.m[7]*b.m[5],

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.XLATE:        // ARBITRARY * XLATE
              return new Matrixf(a.m[0],
                               a.m[1],
                               a.m[2],
                               a.m[3],

                               a.m[4],
                               a.m[5],
                               a.m[6],
                               a.m[7],

                               a.m[8],
                               a.m[9],
                               a.m[10],
                               a.m[11],

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14],
                               a.m[3]*b.m[12] + a.m[7]*b.m[13] + a.m[11]*b.m[14] + a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.SCALE_XLATE:      // ARBITRARY * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               a.m[3]*b.m[0],

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               a.m[7]*b.m[5],

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14],
                               a.m[3]*b.m[12] + a.m[7]*b.m[13] + a.m[11]*b.m[14] + a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.FRUSTUM:      // ARBITRARY * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               a.m[3]*b.m[0],

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               a.m[7]*b.m[5],

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10] + a.m[12]*b.m[11],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10] + a.m[13]*b.m[11],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               a.m[3]*b.m[8] + a.m[7]*b.m[9] + a.m[11]*b.m[10] + a.m[15]*b.m[11],

                               a.m[8]*b.m[14] + a.m[12],
                               a.m[9]*b.m[14] + a.m[13],
                               a.m[10]*b.m[14] + a.m[14],
                               a.m[11]*b.m[14] + a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ARBITRARY * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               a.m[3]*b.m[0],

                               a.m[8]*b.m[6],
                               a.m[9]*b.m[6],
                               a.m[10]*b.m[6],
                               a.m[11]*b.m[6],

                               a.m[4]*b.m[9],
                               a.m[5]*b.m[9],
                               a.m[6]*b.m[9],
                               a.m[7]*b.m[9],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Y_90:         // ARBITRARY * ROT_Y_90
              return new Matrixf(a.m[8]*b.m[2],
                               a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               a.m[11]*b.m[2],

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               a.m[7]*b.m[5],

                               a.m[0]*b.m[8],
                               a.m[1]*b.m[8],
                               a.m[2]*b.m[8],
                               a.m[3]*b.m[8],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Z_90:         // ARBITRARY * ROT_Z_90
              return new Matrixf(a.m[4]*b.m[1],
                               a.m[5]*b.m[1],
                               a.m[6]*b.m[1],
                               a.m[7]*b.m[1],

                               a.m[0]*b.m[4],
                               a.m[1]*b.m[4],
                               a.m[2]*b.m[4],
                               a.m[3]*b.m[4],

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X:        // ARBITRARY * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               a.m[3]*b.m[0],

                               a.m[4]*b.m[5] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               a.m[7]*b.m[5] + a.m[11]*b.m[6],

                               a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               a.m[7]*b.m[9] + a.m[11]*b.m[10],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Y:        // ARBITRARY * ROT_Y
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[1]*b.m[0] + a.m[9]*b.m[2],
                               a.m[2]*b.m[0] + a.m[10]*b.m[2],
                               a.m[3]*b.m[0] + a.m[11]*b.m[2],

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               a.m[7]*b.m[5],

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[10]*b.m[10],
                               a.m[3]*b.m[8] + a.m[11]*b.m[10],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_Z:        // ARBITRARY * ROT_Z
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1],
                               a.m[3]*b.m[0] + a.m[7]*b.m[1],

                               a.m[0]*b.m[4] + a.m[4]*b.m[5],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5],
                               a.m[3]*b.m[4] + a.m[7]*b.m[5],

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               a.m[11]*b.m[10],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_ARB:      // ARBITRARY * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1] + a.m[8]*b.m[2],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               a.m[3]*b.m[0] + a.m[7]*b.m[1] + a.m[11]*b.m[2],

                               a.m[0]*b.m[4] + a.m[4]*b.m[5] + a.m[8]*b.m[6],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               a.m[3]*b.m[4] + a.m[7]*b.m[5] + a.m[11]*b.m[6],

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               a.m[3]*b.m[8] + a.m[7]*b.m[9] + a.m[11]*b.m[10],

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.NO_W:         // ARBITRARY * NO_W
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1] + a.m[8]*b.m[2],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               a.m[3]*b.m[0] + a.m[7]*b.m[1] + a.m[11]*b.m[2],

                               a.m[0]*b.m[4] + a.m[4]*b.m[5] + a.m[8]*b.m[6],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               a.m[3]*b.m[4] + a.m[7]*b.m[5] + a.m[11]*b.m[6],

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               a.m[3]*b.m[8] + a.m[7]*b.m[9] + a.m[11]*b.m[10],

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14],
                               a.m[3]*b.m[12] + a.m[7]*b.m[13] + a.m[11]*b.m[14] + a.m[15],

                               MatrixType.ARBITRARY);

            case MatrixType.ARBITRARY:        // ARBITRARY * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1] + a.m[8]*b.m[2] + a.m[12]*b.m[3],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1] + a.m[9]*b.m[2] + a.m[13]*b.m[3],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1] + a.m[10]*b.m[2] + a.m[14]*b.m[3],
                               a.m[3]*b.m[0] + a.m[7]*b.m[1] + a.m[11]*b.m[2] + a.m[15]*b.m[3],

                               a.m[0]*b.m[4] + a.m[4]*b.m[5] + a.m[8]*b.m[6] + a.m[12]*b.m[7],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5] + a.m[9]*b.m[6] + a.m[13]*b.m[7],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5] + a.m[10]*b.m[6] + a.m[14]*b.m[7],
                               a.m[3]*b.m[4] + a.m[7]*b.m[5] + a.m[11]*b.m[6] + a.m[15]*b.m[7],

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10] + a.m[12]*b.m[11],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10] + a.m[13]*b.m[11],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               a.m[3]*b.m[8] + a.m[7]*b.m[9] + a.m[11]*b.m[10] + a.m[15]*b.m[11],

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12]*b.m[15],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13]*b.m[15],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14]*b.m[15],
                               a.m[3]*b.m[12] + a.m[7]*b.m[13] + a.m[11]*b.m[14] + a.m[15]*b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
}

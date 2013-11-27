/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MatrixfUtil2.java
 *
 */
package com.telenav.map.opengl.java.math;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-19
 */
public class MatrixfUtil2
{
    public static Matrixf handleROT_X(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ROT_X * IDENTITY
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5],
                               a.m[6],
                               0,

                               0,
                               a.m[9],
                               a.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X);

            case MatrixType.SCALE:        // ROT_X * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               0,
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X);

            case MatrixType.XLATE:        // ROT_X * XLATE
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5],
                               a.m[6],
                               0,

                               0,
                               a.m[9],
                               a.m[10],
                               0,

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // ROT_X * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               0,
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // ROT_X * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               b.m[11],

                               0,
                               a.m[9]*b.m[14],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ROT_X * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[9]*b.m[6],
                               a.m[10]*b.m[6],
                               0,

                               0,
                               a.m[5]*b.m[9],
                               a.m[6]*b.m[9],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X);

            case MatrixType.ROT_Y_90:         // ROT_X * ROT_Y_90
              return new Matrixf(0,
                               a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               0,
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z_90:         // ROT_X * ROT_Z_90
              return new Matrixf(0,
                               a.m[5]*b.m[1],
                               a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4],
                               0,
                               0,
                               0,

                               0,
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_X:        // ROT_X * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               0,

                               0,
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X);

            case MatrixType.ROT_Y:        // ROT_X * ROT_Y
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z:        // ROT_X * ROT_Z
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               0,
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_ARB:      // ROT_X * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // ROT_X * NO_W
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // ROT_X * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               b.m[3],

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               b.m[7],

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               b.m[11],

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    public static Matrixf handleROT_Y(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ROT_Y * IDENTITY
              return new Matrixf(a.m[0],
                               0,
                               a.m[2],
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               a.m[8],
                               0,
                               a.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y);

            case MatrixType.SCALE:        // ROT_Y * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y);

            case MatrixType.XLATE:        // ROT_Y * XLATE
              return new Matrixf(a.m[0],
                               0,
                               a.m[2],
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               a.m[8],
                               0,
                               a.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // ROT_Y * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // ROT_Y * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8] + a.m[10]*b.m[10],
                               b.m[11],

                               a.m[8]*b.m[14],
                               0,
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ROT_Y * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[2]*b.m[0],
                               0,

                               a.m[8]*b.m[6],
                               0,
                               a.m[10]*b.m[6],
                               0,

                               0,
                               a.m[5]*b.m[9],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y_90:         // ROT_Y * ROT_Y_90
              return new Matrixf(a.m[8]*b.m[2],
                               0,
                               a.m[10]*b.m[2],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               0,
                               a.m[2]*b.m[8],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y);

            case MatrixType.ROT_Z_90:         // ROT_Y * ROT_Z_90
              return new Matrixf(0,
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
                               0,
                               a.m[2]*b.m[4],
                               0,

                               a.m[8]*b.m[10],
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_X:        // ROT_Y * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[2]*b.m[0],
                               0,

                               a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y:        // ROT_Y * ROT_Y
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               0,
                               a.m[2]*b.m[0] + a.m[10]*b.m[2],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               0,
                               a.m[2]*b.m[8] + a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y);

            case MatrixType.ROT_Z:        // ROT_Y * ROT_Z
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4],
                               0,

                               a.m[8]*b.m[10],
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_ARB:      // ROT_Y * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0] + a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4] + a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8] + a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // ROT_Y * NO_W
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0] + a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4] + a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8] + a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // ROT_Y * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0] + a.m[10]*b.m[2],
                               b.m[3],

                               a.m[0]*b.m[4] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4] + a.m[10]*b.m[6],
                               b.m[7],

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8] + a.m[10]*b.m[10],
                               b.m[11],

                               a.m[0]*b.m[12] + a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12] + a.m[10]*b.m[14],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    public static Matrixf handleROT_Z(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ROT_Z * IDENTITY
              return new Matrixf(a.m[0],
                               a.m[1],
                               0,
                               0,

                               a.m[4],
                               a.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Z);

            case MatrixType.SCALE:        // ROT_Z * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Z);

            case MatrixType.XLATE:        // ROT_Z * XLATE
              return new Matrixf(a.m[0],
                               a.m[1],
                               0,
                               0,

                               a.m[4],
                               a.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[4]*b.m[13],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // ROT_Z * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[4]*b.m[13],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // ROT_Z * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8] + a.m[4]*b.m[9],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               b.m[11],

                               0,
                               0,
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ROT_Z * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9],
                               a.m[5]*b.m[9],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y_90:         // ROT_Z * ROT_Y_90
              return new Matrixf(0,
                               0,
                               a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               a.m[1]*b.m[8],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z_90:         // ROT_Z * ROT_Z_90
              return new Matrixf(a.m[4]*b.m[1],
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
                               a.m[1]*b.m[4],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Z);

            case MatrixType.ROT_X:        // ROT_Z * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y:        // ROT_Z * ROT_Y
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               a.m[1]*b.m[8],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z:        // ROT_Z * ROT_Z
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4] + a.m[4]*b.m[5],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Z);

            case MatrixType.ROT_ARB:      // ROT_Z * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4] + a.m[4]*b.m[5],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8] + a.m[4]*b.m[9],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // ROT_Z * NO_W
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4] + a.m[4]*b.m[5],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8] + a.m[4]*b.m[9],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[4]*b.m[13],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // ROT_Z * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               b.m[3],

                               a.m[0]*b.m[4] + a.m[4]*b.m[5],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               b.m[7],

                               a.m[0]*b.m[8] + a.m[4]*b.m[9],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               b.m[11],

                               a.m[0]*b.m[12] + a.m[4]*b.m[13],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }
        return new Matrixf();

    }
    
    public static Matrixf handleROT_ARB(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ROT_ARB * IDENTITY
              return new Matrixf(a.m[0],
                               a.m[1],
                               a.m[2],
                               0,

                               a.m[4],
                               a.m[5],
                               a.m[6],
                               0,

                               a.m[8],
                               a.m[9],
                               a.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.SCALE:        // ROT_ARB * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.XLATE:        // ROT_ARB * XLATE
              return new Matrixf(a.m[0],
                               a.m[1],
                               a.m[2],
                               0,

                               a.m[4],
                               a.m[5],
                               a.m[6],
                               0,

                               a.m[8],
                               a.m[9],
                               a.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // ROT_ARB * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // ROT_ARB * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               b.m[11],

                               a.m[8]*b.m[14],
                               a.m[9]*b.m[14],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ROT_ARB * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               0,

                               a.m[8]*b.m[6],
                               a.m[9]*b.m[6],
                               a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9],
                               a.m[5]*b.m[9],
                               a.m[6]*b.m[9],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y_90:         // ROT_ARB * ROT_Y_90
              return new Matrixf(a.m[8]*b.m[2],
                               a.m[9]*b.m[2],
                               a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               a.m[1]*b.m[8],
                               a.m[2]*b.m[8],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z_90:         // ROT_ARB * ROT_Z_90
              return new Matrixf(a.m[4]*b.m[1],
                               a.m[5]*b.m[1],
                               a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4],
                               a.m[1]*b.m[4],
                               a.m[2]*b.m[4],
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_X:        // ROT_ARB * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               0,

                               a.m[4]*b.m[5] + a.m[8]*b.m[6],
                               a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y:        // ROT_ARB * ROT_Y
              return new Matrixf(a.m[0]*b.m[0] + a.m[8]*b.m[2],
                               a.m[1]*b.m[0] + a.m[9]*b.m[2],
                               a.m[2]*b.m[0] + a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z:        // ROT_ARB * ROT_Z
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4] + a.m[4]*b.m[5],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5],
                               0,

                               a.m[8]*b.m[10],
                               a.m[9]*b.m[10],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_ARB:      // ROT_ARB * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1] + a.m[8]*b.m[2],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4] + a.m[4]*b.m[5] + a.m[8]*b.m[6],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // ROT_ARB * NO_W
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1] + a.m[8]*b.m[2],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4] + a.m[4]*b.m[5] + a.m[8]*b.m[6],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // ROT_ARB * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1] + a.m[8]*b.m[2],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1] + a.m[9]*b.m[2],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1] + a.m[10]*b.m[2],
                               b.m[3],

                               a.m[0]*b.m[4] + a.m[4]*b.m[5] + a.m[8]*b.m[6],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5] + a.m[9]*b.m[6],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5] + a.m[10]*b.m[6],
                               b.m[7],

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10],
                               b.m[11],

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
}

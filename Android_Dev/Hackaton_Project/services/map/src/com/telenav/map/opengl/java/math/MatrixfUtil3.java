/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MatrixfUtil3.java
 *
 */
package com.telenav.map.opengl.java.math;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-19
 */
public class MatrixfUtil3
{
    public static Matrixf handleROT_X_90(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ROT_X_90 * IDENTITY
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[6],
                               0,

                               0,
                               a.m[9],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X_90);

            case MatrixType.SCALE:        // ROT_X_90 * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[6]*b.m[5],
                               0,

                               0,
                               a.m[9]*b.m[10],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X_90);

            case MatrixType.XLATE:        // ROT_X_90 * XLATE
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[6],
                               0,

                               0,
                               a.m[9],
                               0,
                               0,

                               a.m[0]*b.m[12],
                               a.m[9]*b.m[14],
                               a.m[6]*b.m[13],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // ROT_X_90 * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[6]*b.m[5],
                               0,

                               0,
                               a.m[9]*b.m[10],
                               0,
                               0,

                               a.m[0]*b.m[12],
                               a.m[9]*b.m[14],
                               a.m[6]*b.m[13],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // ROT_X_90 * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               a.m[9]*b.m[10],
                               a.m[6]*b.m[9],
                               b.m[11],

                               0,
                               a.m[9]*b.m[14],
                               0,
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ROT_X_90 * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[9]*b.m[6],
                               0,
                               0,

                               0,
                               0,
                               a.m[6]*b.m[9],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.SCALE);

            case MatrixType.ROT_Y_90:         // ROT_X_90 * ROT_Y_90
              return new Matrixf(0,
                               a.m[9]*b.m[2],
                               0,
                               0,

                               0,
                               0,
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

            case MatrixType.ROT_Z_90:         // ROT_X_90 * ROT_Z_90
              return new Matrixf(0,
                               0,
                               a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4],
                               0,
                               0,
                               0,

                               0,
                               a.m[9]*b.m[10],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_X:        // ROT_X_90 * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[9]*b.m[6],
                               a.m[6]*b.m[5],
                               0,

                               0,
                               a.m[9]*b.m[10],
                               a.m[6]*b.m[9],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X);

            case MatrixType.ROT_Y:        // ROT_X_90 * ROT_Y
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[9]*b.m[2],
                               0,
                               0,

                               0,
                               0,
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               a.m[9]*b.m[10],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z:        // ROT_X_90 * ROT_Z
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4],
                               0,
                               a.m[6]*b.m[5],
                               0,

                               0,
                               a.m[9]*b.m[10],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_ARB:      // ROT_X_90 * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[9]*b.m[2],
                               a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4],
                               a.m[9]*b.m[6],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               a.m[9]*b.m[10],
                               a.m[6]*b.m[9],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // ROT_X_90 * NO_W
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[9]*b.m[2],
                               a.m[6]*b.m[1],
                               0,

                               a.m[0]*b.m[4],
                               a.m[9]*b.m[6],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8],
                               a.m[9]*b.m[10],
                               a.m[6]*b.m[9],
                               0,

                               a.m[0]*b.m[12],
                               a.m[9]*b.m[14],
                               a.m[6]*b.m[13],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // ROT_X_90 * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[9]*b.m[2],
                               a.m[6]*b.m[1],
                               b.m[3],

                               a.m[0]*b.m[4],
                               a.m[9]*b.m[6],
                               a.m[6]*b.m[5],
                               b.m[7],

                               a.m[0]*b.m[8],
                               a.m[9]*b.m[10],
                               a.m[6]*b.m[9],
                               b.m[11],

                               a.m[0]*b.m[12],
                               a.m[9]*b.m[14],
                               a.m[6]*b.m[13],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    public static Matrixf handleROT_Y_90(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ROT_Y_90 * IDENTITY
              return new Matrixf(0,
                               0,
                               a.m[2],
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               a.m[8],
                               0,
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y_90);

            case MatrixType.SCALE:        // ROT_Y_90 * SCALE
              return new Matrixf(0,
                               0,
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               0,
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y_90);

            case MatrixType.XLATE:        // ROT_Y_90 * XLATE
              return new Matrixf(0,
                               0,
                               a.m[2],
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               a.m[8],
                               0,
                               0,
                               0,

                               a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // ROT_Y_90 * SCALE_XLATE
              return new Matrixf(0,
                               0,
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               0,
                               0,
                               0,

                               a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // ROT_Y_90 * FRUSTUM
              return new Matrixf(0,
                               0,
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8],
                               b.m[11],

                               a.m[8]*b.m[14],
                               0,
                               0,
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ROT_Y_90 * ROT_X_90
              return new Matrixf(0,
                               0,
                               a.m[2]*b.m[0],
                               0,

                               a.m[8]*b.m[6],
                               0,
                               0,
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

            case MatrixType.ROT_Y_90:         // ROT_Y_90 * ROT_Y_90
              return new Matrixf(a.m[8]*b.m[2],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[2]*b.m[8],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.SCALE);

            case MatrixType.ROT_Z_90:         // ROT_Y_90 * ROT_Z_90
              return new Matrixf(0,
                               a.m[5]*b.m[1],
                               0,
                               0,

                               0,
                               0,
                               a.m[2]*b.m[4],
                               0,

                               a.m[8]*b.m[10],
                               0,
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_X:        // ROT_Y_90 * ROT_X
              return new Matrixf(0,
                               0,
                               a.m[2]*b.m[0],
                               0,

                               a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y:        // ROT_Y_90 * ROT_Y
              return new Matrixf(a.m[8]*b.m[2],
                               0,
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[8]*b.m[10],
                               0,
                               a.m[2]*b.m[8],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y);

            case MatrixType.ROT_Z:        // ROT_Y_90 * ROT_Z
              return new Matrixf(0,
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4],
                               0,

                               a.m[8]*b.m[10],
                               0,
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_ARB:      // ROT_Y_90 * ROT_ARB
              return new Matrixf(a.m[8]*b.m[2],
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0],
                               0,

                               a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4],
                               0,

                               a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // ROT_Y_90 * NO_W
              return new Matrixf(a.m[8]*b.m[2],
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0],
                               0,

                               a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4],
                               0,

                               a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8],
                               0,

                               a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // ROT_Y_90 * ARBITRARY
              return new Matrixf(a.m[8]*b.m[2],
                               a.m[5]*b.m[1],
                               a.m[2]*b.m[0],
                               b.m[3],

                               a.m[8]*b.m[6],
                               a.m[5]*b.m[5],
                               a.m[2]*b.m[4],
                               b.m[7],

                               a.m[8]*b.m[10],
                               a.m[5]*b.m[9],
                               a.m[2]*b.m[8],
                               b.m[11],

                               a.m[8]*b.m[14],
                               a.m[5]*b.m[13],
                               a.m[2]*b.m[12],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    public static Matrixf handleROT_Z_90(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // ROT_Z_90 * IDENTITY
              return new Matrixf(0,
                               a.m[1],
                               0,
                               0,

                               a.m[4],
                               0,
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

                               MatrixType.ROT_Z_90);

            case MatrixType.SCALE:        // ROT_Z_90 * SCALE
              return new Matrixf(0,
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               0,
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

                               MatrixType.ROT_Z_90);

            case MatrixType.XLATE:        // ROT_Z_90 * XLATE
              return new Matrixf(0,
                               a.m[1],
                               0,
                               0,

                               a.m[4],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[10],
                               0,

                               a.m[4]*b.m[13],
                               a.m[1]*b.m[12],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // ROT_Z_90 * SCALE_XLATE
              return new Matrixf(0,
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[4]*b.m[13],
                               a.m[1]*b.m[12],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // ROT_Z_90 * FRUSTUM
              return new Matrixf(0,
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               0,
                               0,
                               0,

                               a.m[4]*b.m[9],
                               a.m[1]*b.m[8],
                               a.m[10]*b.m[10],
                               b.m[11],

                               0,
                               0,
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // ROT_Z_90 * ROT_X_90
              return new Matrixf(0,
                               a.m[1]*b.m[0],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9],
                               0,
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y_90:         // ROT_Z_90 * ROT_Y_90
              return new Matrixf(0,
                               0,
                               a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               0,
                               0,
                               0,

                               0,
                               a.m[1]*b.m[8],
                               0,
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z_90:         // ROT_Z_90 * ROT_Z_90
              return new Matrixf(a.m[4]*b.m[1],
                               0,
                               0,
                               0,

                               0,
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

                               MatrixType.SCALE);

            case MatrixType.ROT_X:        // ROT_Z_90 * ROT_X
              return new Matrixf(0,
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
                               0,
                               a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9],
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Y:        // ROT_Z_90 * ROT_Y
              return new Matrixf(0,
                               a.m[1]*b.m[0],
                               a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               0,
                               0,
                               0,

                               0,
                               a.m[1]*b.m[8],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.ROT_Z:        // ROT_Z_90 * ROT_Z
              return new Matrixf(a.m[4]*b.m[1],
                               a.m[1]*b.m[0],
                               0,
                               0,

                               a.m[4]*b.m[5],
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

            case MatrixType.ROT_ARB:      // ROT_Z_90 * ROT_ARB
              return new Matrixf(a.m[4]*b.m[1],
                               a.m[1]*b.m[0],
                               a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               a.m[1]*b.m[4],
                               a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9],
                               a.m[1]*b.m[8],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // ROT_Z_90 * NO_W
              return new Matrixf(a.m[4]*b.m[1],
                               a.m[1]*b.m[0],
                               a.m[10]*b.m[2],
                               0,

                               a.m[4]*b.m[5],
                               a.m[1]*b.m[4],
                               a.m[10]*b.m[6],
                               0,

                               a.m[4]*b.m[9],
                               a.m[1]*b.m[8],
                               a.m[10]*b.m[10],
                               0,

                               a.m[4]*b.m[13],
                               a.m[1]*b.m[12],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // ROT_Z_90 * ARBITRARY
              return new Matrixf(a.m[4]*b.m[1],
                               a.m[1]*b.m[0],
                               a.m[10]*b.m[2],
                               b.m[3],

                               a.m[4]*b.m[5],
                               a.m[1]*b.m[4],
                               a.m[10]*b.m[6],
                               b.m[7],

                               a.m[4]*b.m[9],
                               a.m[1]*b.m[8],
                               a.m[10]*b.m[10],
                               b.m[11],

                               a.m[4]*b.m[13],
                               a.m[1]*b.m[12],
                               a.m[10]*b.m[14],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    
    
    public static Matrixf handleNO_W(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // NO_W * IDENTITY
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE:        // NO_W * SCALE
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.XLATE:        // NO_W * XLATE
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

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.SCALE_XLATE:      // NO_W * SCALE_XLATE
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

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.FRUSTUM:      // NO_W * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[1]*b.m[0],
                               a.m[2]*b.m[0],
                               0,

                               a.m[4]*b.m[5],
                               a.m[5]*b.m[5],
                               a.m[6]*b.m[5],
                               0,

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10] + a.m[12]*b.m[11],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10] + a.m[13]*b.m[11],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               b.m[11],

                               a.m[8]*b.m[14] + a.m[12],
                               a.m[9]*b.m[14] + a.m[13],
                               a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // NO_W * ROT_X_90
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Y_90:         // NO_W * ROT_Y_90
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Z_90:         // NO_W * ROT_Z_90
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_X:        // NO_W * ROT_X
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Y:        // NO_W * ROT_Y
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Z:        // NO_W * ROT_Z
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_ARB:      // NO_W * ROT_ARB
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

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.NO_W:         // NO_W * NO_W
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

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // NO_W * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0] + a.m[4]*b.m[1] + a.m[8]*b.m[2] + a.m[12]*b.m[3],
                               a.m[1]*b.m[0] + a.m[5]*b.m[1] + a.m[9]*b.m[2] + a.m[13]*b.m[3],
                               a.m[2]*b.m[0] + a.m[6]*b.m[1] + a.m[10]*b.m[2] + a.m[14]*b.m[3],
                               b.m[3],

                               a.m[0]*b.m[4] + a.m[4]*b.m[5] + a.m[8]*b.m[6] + a.m[12]*b.m[7],
                               a.m[1]*b.m[4] + a.m[5]*b.m[5] + a.m[9]*b.m[6] + a.m[13]*b.m[7],
                               a.m[2]*b.m[4] + a.m[6]*b.m[5] + a.m[10]*b.m[6] + a.m[14]*b.m[7],
                               b.m[7],

                               a.m[0]*b.m[8] + a.m[4]*b.m[9] + a.m[8]*b.m[10] + a.m[12]*b.m[11],
                               a.m[1]*b.m[8] + a.m[5]*b.m[9] + a.m[9]*b.m[10] + a.m[13]*b.m[11],
                               a.m[2]*b.m[8] + a.m[6]*b.m[9] + a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               b.m[11],

                               a.m[0]*b.m[12] + a.m[4]*b.m[13] + a.m[8]*b.m[14] + a.m[12]*b.m[15],
                               a.m[1]*b.m[12] + a.m[5]*b.m[13] + a.m[9]*b.m[14] + a.m[13]*b.m[15],
                               a.m[2]*b.m[12] + a.m[6]*b.m[13] + a.m[10]*b.m[14] + a.m[14]*b.m[15],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    
}

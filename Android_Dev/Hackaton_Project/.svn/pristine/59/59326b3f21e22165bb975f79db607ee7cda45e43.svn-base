/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MatrixfUtil.java
 *
 */
package com.telenav.map.opengl.java.math;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-19
 */
public class MatrixfUtil
{
    public static Matrixf handleIdentity(Matrixf a, Matrixf b)
    {
        switch(b.type) 
        {
        case MatrixType.IDENTITY:         // IDENTITY * IDENTITY
          return new Matrixf(1,
                           0,
                           0,
                           0,

                           0,
                           1,
                           0,
                           0,

                           0,
                           0,
                           1,
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.IDENTITY);

        case MatrixType.SCALE:        // IDENTITY * SCALE
          return new Matrixf(b.m[0],
                           0,
                           0,
                           0,

                           0,
                           b.m[5],
                           0,
                           0,

                           0,
                           0,
                           b.m[10],
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.SCALE);

        case MatrixType.XLATE:        // IDENTITY * XLATE
          return new Matrixf(1,
                           0,
                           0,
                           0,

                           0,
                           1,
                           0,
                           0,

                           0,
                           0,
                           1,
                           0,

                           b.m[12],
                           b.m[13],
                           b.m[14],
                           1,

                           MatrixType.XLATE);

        case MatrixType.SCALE_XLATE:      // IDENTITY * SCALE_XLATE
          return new Matrixf(b.m[0],
                           0,
                           0,
                           0,

                           0,
                           b.m[5],
                           0,
                           0,

                           0,
                           0,
                           b.m[10],
                           0,

                           b.m[12],
                           b.m[13],
                           b.m[14],
                           1,

                           MatrixType.SCALE_XLATE);

        case MatrixType.FRUSTUM:      // IDENTITY * FRUSTUM
          return new Matrixf(b.m[0],
                           0,
                           0,
                           0,

                           0,
                           b.m[5],
                           0,
                           0,

                           b.m[8],
                           b.m[9],
                           b.m[10],
                           b.m[11],

                           0,
                           0,
                           b.m[14],
                           1,

                           MatrixType.FRUSTUM);

        case MatrixType.ROT_X_90:         // IDENTITY * ROT_X_90
          return new Matrixf(b.m[0],
                           0,
                           0,
                           0,

                           0,
                           0,
                           b.m[6],
                           0,

                           0,
                           b.m[9],
                           0,
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.ROT_X_90);

        case MatrixType.ROT_Y_90:         // IDENTITY * ROT_Y_90
          return new Matrixf(0,
                           0,
                           b.m[2],
                           0,

                           0,
                           b.m[5],
                           0,
                           0,

                           b.m[8],
                           0,
                           0,
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.ROT_Y_90);

        case MatrixType.ROT_Z_90:         // IDENTITY * ROT_Z_90
          return new Matrixf(0,
                           b.m[1],
                           0,
                           0,

                           b.m[4],
                           0,
                           0,
                           0,

                           0,
                           0,
                           b.m[10],
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.ROT_Z_90);

        case MatrixType.ROT_X:        // IDENTITY * ROT_X
          return new Matrixf(b.m[0],
                           0,
                           0,
                           0,

                           0,
                           b.m[5],
                           b.m[6],
                           0,

                           0,
                           b.m[9],
                           b.m[10],
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.ROT_X);

        case MatrixType.ROT_Y:        // IDENTITY * ROT_Y
          return new Matrixf(b.m[0],
                           0,
                           b.m[2],
                           0,

                           0,
                           b.m[5],
                           0,
                           0,

                           b.m[8],
                           0,
                           b.m[10],
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.ROT_Y);

        case MatrixType.ROT_Z:        // IDENTITY * ROT_Z
          return new Matrixf(b.m[0],
                           b.m[1],
                           0,
                           0,

                           b.m[4],
                           b.m[5],
                           0,
                           0,

                           0,
                           0,
                           b.m[10],
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.ROT_Z);

        case MatrixType.ROT_ARB:      // IDENTITY * ROT_ARB
          return new Matrixf(b.m[0],
                           b.m[1],
                           b.m[2],
                           0,

                           b.m[4],
                           b.m[5],
                           b.m[6],
                           0,

                           b.m[8],
                           b.m[9],
                           b.m[10],
                           0,

                           0,
                           0,
                           0,
                           1,

                           MatrixType.ROT_ARB);

        case MatrixType.NO_W:         // IDENTITY * NO_W
          return new Matrixf(b.m[0],
                           b.m[1],
                           b.m[2],
                           0,

                           b.m[4],
                           b.m[5],
                           b.m[6],
                           0,

                           b.m[8],
                           b.m[9],
                           b.m[10],
                           0,

                           b.m[12],
                           b.m[13],
                           b.m[14],
                           1,

                           MatrixType.NO_W);

        case MatrixType.ARBITRARY:        // IDENTITY * ARBITRARY
          return new Matrixf(b.m[0],
                           b.m[1],
                           b.m[2],
                           b.m[3],

                           b.m[4],
                           b.m[5],
                           b.m[6],
                           b.m[7],

                           b.m[8],
                           b.m[9],
                           b.m[10],
                           b.m[11],

                           b.m[12],
                           b.m[13],
                           b.m[14],
                           b.m[15],

                           MatrixType.ARBITRARY);
        }
        return new Matrixf();
    }

    public static Matrixf handleScale(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // SCALE * IDENTITY
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
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

                               MatrixType.SCALE);

            case MatrixType.SCALE:        // SCALE * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
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

                               MatrixType.SCALE);

            case MatrixType.XLATE:        // SCALE * XLATE
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10],
                               0,

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.SCALE_XLATE:      // SCALE * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.FRUSTUM:      // SCALE * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               b.m[11],

                               0,
                               0,
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.FRUSTUM);

            case MatrixType.ROT_X_90:         // SCALE * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
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

                               MatrixType.ROT_X_90);

            case MatrixType.ROT_Y_90:         // SCALE * ROT_Y_90
              return new Matrixf(0,
                               0,
                               a.m[10]*b.m[2],
                               0,

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
                               0,
                               1,

                               MatrixType.ROT_Y_90);

            case MatrixType.ROT_Z_90:         // SCALE * ROT_Z_90
              return new Matrixf(0,
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
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

            case MatrixType.ROT_X:        // SCALE * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               0,
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_X);

            case MatrixType.ROT_Y:        // SCALE * ROT_Y
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[10]*b.m[2],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               0,
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_Y);

            case MatrixType.ROT_Z:        // SCALE * ROT_Z
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
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

            case MatrixType.ROT_ARB:      // SCALE * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               0,
                               0,
                               0,
                               1,

                               MatrixType.ROT_ARB);

            case MatrixType.NO_W:         // SCALE * NO_W
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // SCALE * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               b.m[3],

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               b.m[7],

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               b.m[11],

                               a.m[0]*b.m[12],
                               a.m[5]*b.m[13],
                               a.m[10]*b.m[14],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
    
    public static Matrixf handleXlate(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // XLATE * IDENTITY
              return new Matrixf(1,
                               0,
                               0,
                               0,

                               0,
                               1,
                               0,
                               0,

                               0,
                               0,
                               1,
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.XLATE);

            case MatrixType.SCALE:        // XLATE * SCALE
              return new Matrixf(b.m[0],
                               0,
                               0,
                               0,

                               0,
                               b.m[5],
                               0,
                               0,

                               0,
                               0,
                               b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.XLATE:        // XLATE * XLATE
              return new Matrixf(1,
                               0,
                               0,
                               0,

                               0,
                               1,
                               0,
                               0,

                               0,
                               0,
                               1,
                               0,

                               b.m[12] + a.m[12],
                               b.m[13] + a.m[13],
                               b.m[14] + a.m[14],
                               1,

                               MatrixType.XLATE);

            case MatrixType.SCALE_XLATE:      // XLATE * SCALE_XLATE
              return new Matrixf(b.m[0],
                               0,
                               0,
                               0,

                               0,
                               b.m[5],
                               0,
                               0,

                               0,
                               0,
                               b.m[10],
                               0,

                               b.m[12] + a.m[12],
                               b.m[13] + a.m[13],
                               b.m[14] + a.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.FRUSTUM:      // XLATE * FRUSTUM
              return new Matrixf(b.m[0],
                               0,
                               0,
                               0,

                               0,
                               b.m[5],
                               0,
                               0,

                               b.m[8] + a.m[12]*b.m[11],
                               b.m[9] + a.m[13]*b.m[11],
                               b.m[10] + a.m[14]*b.m[11],
                               b.m[11],

                               a.m[12],
                               a.m[13],
                               b.m[14] + a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // XLATE * ROT_X_90
              return new Matrixf(b.m[0],
                               0,
                               0,
                               0,

                               0,
                               0,
                               b.m[6],
                               0,

                               0,
                               b.m[9],
                               0,
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Y_90:         // XLATE * ROT_Y_90
              return new Matrixf(0,
                               0,
                               b.m[2],
                               0,

                               0,
                               b.m[5],
                               0,
                               0,

                               b.m[8],
                               0,
                               0,
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Z_90:         // XLATE * ROT_Z_90
              return new Matrixf(0,
                               b.m[1],
                               0,
                               0,

                               b.m[4],
                               0,
                               0,
                               0,

                               0,
                               0,
                               b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_X:        // XLATE * ROT_X
              return new Matrixf(b.m[0],
                               0,
                               0,
                               0,

                               0,
                               b.m[5],
                               b.m[6],
                               0,

                               0,
                               b.m[9],
                               b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Y:        // XLATE * ROT_Y
              return new Matrixf(b.m[0],
                               0,
                               b.m[2],
                               0,

                               0,
                               b.m[5],
                               0,
                               0,

                               b.m[8],
                               0,
                               b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Z:        // XLATE * ROT_Z
              return new Matrixf(b.m[0],
                               b.m[1],
                               0,
                               0,

                               b.m[4],
                               b.m[5],
                               0,
                               0,

                               0,
                               0,
                               b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_ARB:      // XLATE * ROT_ARB
              return new Matrixf(b.m[0],
                               b.m[1],
                               b.m[2],
                               0,

                               b.m[4],
                               b.m[5],
                               b.m[6],
                               0,

                               b.m[8],
                               b.m[9],
                               b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.NO_W:         // XLATE * NO_W
              return new Matrixf(b.m[0],
                               b.m[1],
                               b.m[2],
                               0,

                               b.m[4],
                               b.m[5],
                               b.m[6],
                               0,

                               b.m[8],
                               b.m[9],
                               b.m[10],
                               0,

                               b.m[12] + a.m[12],
                               b.m[13] + a.m[13],
                               b.m[14] + a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // XLATE * ARBITRARY
              return new Matrixf(b.m[0] + a.m[12]*b.m[3],
                               b.m[1] + a.m[13]*b.m[3],
                               b.m[2] + a.m[14]*b.m[3],
                               b.m[3],

                               b.m[4] + a.m[12]*b.m[7],
                               b.m[5] + a.m[13]*b.m[7],
                               b.m[6] + a.m[14]*b.m[7],
                               b.m[7],

                               b.m[8] + a.m[12]*b.m[11],
                               b.m[9] + a.m[13]*b.m[11],
                               b.m[10] + a.m[14]*b.m[11],
                               b.m[11],

                               b.m[12] + a.m[12]*b.m[15],
                               b.m[13] + a.m[13]*b.m[15],
                               b.m[14] + a.m[14]*b.m[15],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }
        return new Matrixf();

    }
    
    public static Matrixf handleScale_Xlate(Matrixf a, Matrixf b)
    {
        switch(b.type) {
            case MatrixType.IDENTITY:         // SCALE_XLATE * IDENTITY
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.SCALE:        // SCALE_XLATE * SCALE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.XLATE:        // SCALE_XLATE * XLATE
              return new Matrixf(a.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[12],
                               a.m[5]*b.m[13] + a.m[13],
                               a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.SCALE_XLATE:      // SCALE_XLATE * SCALE_XLATE
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[12],
                               a.m[5]*b.m[13] + a.m[13],
                               a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.SCALE_XLATE);

            case MatrixType.FRUSTUM:      // SCALE_XLATE * FRUSTUM
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8] + a.m[12]*b.m[11],
                               a.m[5]*b.m[9] + a.m[13]*b.m[11],
                               a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               b.m[11],

                               a.m[12],
                               a.m[13],
                               a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.ARBITRARY);

            case MatrixType.ROT_X_90:         // SCALE_XLATE * ROT_X_90
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[6],
                               0,

                               0,
                               a.m[5]*b.m[9],
                               0,
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Y_90:         // SCALE_XLATE * ROT_Y_90
              return new Matrixf(0,
                               0,
                               a.m[10]*b.m[2],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               0,
                               0,
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Z_90:         // SCALE_XLATE * ROT_Z_90
              return new Matrixf(0,
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
                               0,
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_X:        // SCALE_XLATE * ROT_X
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               0,
                               0,

                               0,
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               0,
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Y:        // SCALE_XLATE * ROT_Y
              return new Matrixf(a.m[0]*b.m[0],
                               0,
                               a.m[10]*b.m[2],
                               0,

                               0,
                               a.m[5]*b.m[5],
                               0,
                               0,

                               a.m[0]*b.m[8],
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_Z:        // SCALE_XLATE * ROT_Z
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               0,
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               0,
                               0,

                               0,
                               0,
                               a.m[10]*b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ROT_ARB:      // SCALE_XLATE * ROT_ARB
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               a.m[12],
                               a.m[13],
                               a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.NO_W:         // SCALE_XLATE * NO_W
              return new Matrixf(a.m[0]*b.m[0],
                               a.m[5]*b.m[1],
                               a.m[10]*b.m[2],
                               0,

                               a.m[0]*b.m[4],
                               a.m[5]*b.m[5],
                               a.m[10]*b.m[6],
                               0,

                               a.m[0]*b.m[8],
                               a.m[5]*b.m[9],
                               a.m[10]*b.m[10],
                               0,

                               a.m[0]*b.m[12] + a.m[12],
                               a.m[5]*b.m[13] + a.m[13],
                               a.m[10]*b.m[14] + a.m[14],
                               1,

                               MatrixType.NO_W);

            case MatrixType.ARBITRARY:        // SCALE_XLATE * ARBITRARY
              return new Matrixf(a.m[0]*b.m[0] + a.m[12]*b.m[3],
                               a.m[5]*b.m[1] + a.m[13]*b.m[3],
                               a.m[10]*b.m[2] + a.m[14]*b.m[3],
                               b.m[3],

                               a.m[0]*b.m[4] + a.m[12]*b.m[7],
                               a.m[5]*b.m[5] + a.m[13]*b.m[7],
                               a.m[10]*b.m[6] + a.m[14]*b.m[7],
                               b.m[7],

                               a.m[0]*b.m[8] + a.m[12]*b.m[11],
                               a.m[5]*b.m[9] + a.m[13]*b.m[11],
                               a.m[10]*b.m[10] + a.m[14]*b.m[11],
                               b.m[11],

                               a.m[0]*b.m[12] + a.m[12]*b.m[15],
                               a.m[5]*b.m[13] + a.m[13]*b.m[15],
                               a.m[10]*b.m[14] + a.m[14]*b.m[15],
                               b.m[15],

                               MatrixType.ARBITRARY);

            }

        return new Matrixf();
    }
}

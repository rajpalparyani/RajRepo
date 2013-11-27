/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MatrixType.java
 *
 */
package com.telenav.map.opengl.java.math;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-26
 */
public class MatrixType
{
    public final static int IDENTITY = 0;
    public final static int SCALE = 1;
    public final static int XLATE = 2;
    public final static int SCALE_XLATE = 3;
    public final static int FRUSTUM = 4;
    public final static int ROT_X_90 = 5;
    public final static int ROT_Y_90 = 6;
    public final static int ROT_Z_90 = 7;
    public final static int ROT_X = 8;
    public final static int ROT_Y = 9;
    public final static int ROT_Z = 10;
    public final static int ROT_ARB = 11;
    public final static int NO_W = 12;
    public final static int ARBITRARY = 13;
    
    //value
    public final static int ZERO = 0;
    public final static int POS_ONE = 1;
    public final static int NEG_ONE = -1;
    public final static int ARB = Integer.MAX_VALUE;
    
    public static int count = ARBITRARY+1;
    
    public static int findValue(float v)
    {
        if (0 == v)
            return ZERO;
        if (1 == v)
            return POS_ONE;
        if (-1 == v)
            return NEG_ONE;
        return ARB;
    }
    
    public static int countMisses(int t, float[] m) {

        int misses = 0;
        for (int j=0; j<4; ++j) {
            for (int i=0; i<4; ++i) {

                int allowed_to_be = forms[t][j][i];
                int is = findValue(m[(j<<2)+i]);

                if (is != allowed_to_be) {
                    if (allowed_to_be==ARB) {
                        ++misses;
                    } else {
                        return -1;
                    }
                }
            }
        }
        return misses;
    }
    
    public static int findType(float[] m)
    {

        int type = ARBITRARY;
        int least_misses = 16;

        for (int i = 0; i < count; ++i)
        {
            int misses = countMisses(i, m);
            if ((misses != -1) && (misses < least_misses))
            {
                least_misses = misses;
                type = i;
            }
        }

        return type;
    }
    
    public static String[] names = new String[]{

            "IDENTITY",
            "SCALE",
            "XLATE",
            "SCALE_XLATE",
            "FRUSTUM",
            "ROT_X_90",
            "ROT_Y_90",
            "ROT_Z_90",
            "ROT_X",
            "ROT_Y",
            "ROT_Z",
            "ROT_ARB",
            "NO_W",
            "ARBITRARY",
          };
    
    public static int[][][] forms = new int[][][]{

            { // IDENTITY
              {POS_ONE,    ZERO,    ZERO,    ZERO},
              {   ZERO, POS_ONE,    ZERO,    ZERO},
              {   ZERO,    ZERO, POS_ONE,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },

            { // SCALE
              {    ARB,    ZERO,    ZERO,    ZERO},
              {   ZERO,     ARB,    ZERO,    ZERO},
              {   ZERO,    ZERO,     ARB,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },

            { // XLATE
              {POS_ONE,    ZERO,    ZERO,    ZERO},
              {   ZERO, POS_ONE,    ZERO,    ZERO},
              {   ZERO,    ZERO, POS_ONE,    ZERO},
              {    ARB,     ARB,     ARB, POS_ONE},
            },

            { // SCALE_XLATE
              {    ARB,    ZERO,    ZERO,    ZERO},
              {   ZERO,     ARB,    ZERO,    ZERO},
              {   ZERO,    ZERO,     ARB,    ZERO},
              {    ARB,     ARB,     ARB, POS_ONE},
            },

            { // FRUSTUM
              {    ARB,    ZERO,    ZERO,    ZERO},
              {   ZERO,     ARB,    ZERO,    ZERO},
              {    ARB,     ARB,     ARB,     ARB},
              {   ZERO,    ZERO,     ARB, POS_ONE},
            },

            { // ROT_X_90
              {    ARB,    ZERO,    ZERO,    ZERO},
              {   ZERO,    ZERO,     ARB,    ZERO},
              {   ZERO,     ARB,    ZERO,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },
            { // ROT_Y_90
              {   ZERO,    ZERO,     ARB,    ZERO},
              {   ZERO,     ARB,    ZERO,    ZERO},
              {    ARB,    ZERO,    ZERO,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },
            { // ROT_Z_90
              {   ZERO,     ARB,    ZERO,    ZERO},
              {    ARB,    ZERO,    ZERO,    ZERO},
              {   ZERO,    ZERO,     ARB,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },

            { // ROT_X
              {    ARB,    ZERO,    ZERO,    ZERO},
              {   ZERO,     ARB,     ARB,    ZERO},
              {   ZERO,     ARB,     ARB,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },
            { // ROT_Y
              {    ARB,    ZERO,     ARB,    ZERO},
              {   ZERO,     ARB,    ZERO,    ZERO},
              {    ARB,    ZERO,     ARB,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },
            { // ROT_Z
              {    ARB,     ARB,    ZERO,    ZERO},
              {    ARB,     ARB,    ZERO,    ZERO},
              {   ZERO,    ZERO,     ARB,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },

            { // ROT_ARB
              {    ARB,     ARB,     ARB,    ZERO},
              {    ARB,     ARB,     ARB,    ZERO},
              {    ARB,     ARB,     ARB,    ZERO},
              {   ZERO,    ZERO,    ZERO, POS_ONE},
            },

            { // NO_W
              {    ARB,     ARB,     ARB,    ZERO},
              {    ARB,     ARB,     ARB,    ZERO},
              {    ARB,     ARB,     ARB,    ZERO},
              {    ARB,     ARB,     ARB, POS_ONE},
            },

            { // ARBITRARY
              {    ARB,     ARB,     ARB,     ARB},
              {    ARB,     ARB,     ARB,     ARB},
              {    ARB,     ARB,     ARB,     ARB},
              {    ARB,     ARB,     ARB,     ARB},
            },


          };
}

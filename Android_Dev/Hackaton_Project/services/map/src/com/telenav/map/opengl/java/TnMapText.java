/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapText.java
 *
 */
package com.telenav.map.opengl.java;


/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapText
{
  //Font Texture Mapping
    public static float s_TexCoord[] = new float[256 * 8];
    
    public static int s_CharKerning[] =
    {
         7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,
         7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,
         4, 3, 5, 7,  7,12, 9, 2,  4, 4, 5, 8,  4, 4, 4, 4,
         7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 4, 4,  8, 4, 8, 7,

        13, 9, 9, 9,  9, 9, 8,10,  9, 3, 6, 9,  7,11, 9,10,
         9,10, 9, 9,  7, 9, 9,13,  7, 9, 7, 4,  4, 4, 5, 7,
         4, 7, 7, 7,  7, 7, 3, 7,  7, 3, 3, 7,  3,11, 7, 7,
         7, 7, 4, 7,  4, 7, 5, 9,  7, 7, 7, 4,  3, 4, 8, 7,

         7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,
         7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,
         4, 3, 7, 7,  7, 7, 3, 7,  4,10, 4, 7,  8, 4,10, 7,
         5, 7, 4, 4,  4, 7, 7, 4,  4, 4, 5, 7, 11,11,11, 8,

         9, 9, 9, 9,  9, 9,13, 9,  9, 9, 9, 9,  3, 3, 3, 3,
         9, 9,10,10, 10,10,10, 8, 10, 9, 9, 9,  9, 9, 9, 9,
         7, 7, 7, 7,  7, 7,12, 7,  7, 7, 7, 7,  3, 3, 3, 3,
         7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7,  7, 7, 7, 7
    };

    static
    {
        for (int i=0; i<16; i++)
        {
            for (int j=0; j<16; j++)
            {
                int character = i * 16 + j;
                int charIndex = character * 8;
                
                s_TexCoord[charIndex] = j * 32.0f / 512.0f;
                s_TexCoord[charIndex + 1] = (i+1) * 32.0f / 512.0f;

                s_TexCoord[charIndex + 2] = (j+1) * 32.0f / 512.0f;
                s_TexCoord[charIndex + 3] = (i+1) * 32.0f / 512.0f;

                s_TexCoord[charIndex + 4] = (j+1) * 32.0f / 512.0f;
                s_TexCoord[charIndex + 5] = i * 32.0f / 512.0f;

                s_TexCoord[charIndex + 6] = j * 32.0f / 512.0f;
                s_TexCoord[charIndex + 7] = i * 32.0f / 512.0f;
            }
        }
    }

    public static float[] getCharTexCoord(int character, int corner)
    {
        int i = character * 8 + corner * 2;
        return new float[]{s_TexCoord[i], s_TexCoord[i+1]};
    }

    public static float getCharWidth(int character)
    {
        return s_CharKerning[character];
    }
    
}

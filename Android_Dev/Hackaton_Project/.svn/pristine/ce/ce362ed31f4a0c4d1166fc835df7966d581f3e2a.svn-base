/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapMagic.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.map.opengl.java.math.Pe;
import com.telenav.tnui.opengles.TnGLUtils;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapMagic
{
    public static final int enoughTileBits = 26;
    public static final int maxGlobalCoord = (1 << (enoughTileBits - 1));
    public static final int ignoredTileBits = 6;
    
    public static final int invert(int i, int zoom)
    {
        /* This effectively finds (1 - i) in "tile-id" space from what we
         * can tell. (thant) 
         */
        i = (1 << (enoughTileBits - 1 - ignoredTileBits - zoom)) - i - 1;
        return i;
    }
    
    public static float zoomSpaceToReal(float zoom, float d)
    {
        return (float)(TnGLUtils.getInstance().pow(2.0f, zoom) * getTileSize(0) * d);
    }
    
    public static float realToZoomSpace(float realDistance, float d)
    {
        float new_d = realDistance / (getTileSize(0) * d);
        return (float)(TnGLUtils.getInstance().log(new_d) / TnGLUtils.getInstance().log(2.0f));
    }
    
    public static int zoomSpaceToZoomLevel(float zoomSpace)
    {
        return Pe.clamp((int)Math.floor(zoomSpace+0.5f), 0, 15);
    }

    public static float getTileSize(int zoom)
    {
        return (float)(1 << (zoom + ignoredTileBits));
    }

    public static int tileIdToZoomLevel(long tileId)
    {
        return (int)((tileId >> 48) & 0xFF);
    }

    public static void tileIdToPoint(long tileId, double[] point)
    {
        int zoom = tileIdToZoomLevel(tileId);

        int xIndex = (int)((tileId >> 24) & 0xFFFFFF);
        int yIndex = (int)(tileId & 0xFFFFFF);

        yIndex = invert(yIndex, zoom);

        int globalX = xIndex << (zoom + ignoredTileBits);
        int globalY = yIndex << (zoom + ignoredTileBits);
     
        point[0] = globalX;
        point[1] = globalY;
        point[2] = 0;
    }

    public static long pointToTileID(double[] point, int zoom)
    {
        int xIndex = ((int)point[0]) >> (ignoredTileBits + zoom);
        int yIndex = ((int)point[1]) >> (ignoredTileBits + zoom);

        yIndex = invert(yIndex, zoom);

        long id = (((long)xIndex) << 24) | (long)yIndex | (((long)zoom) << 48);

        return id;
    }

    public static void latLonToGlobalNoWrap(double[] p)
    {
        for (int i=0; i<p.length; i+=3)
        {
            double x = (p[i] + 180.0) / 360.0;
            
            double x_g = x * maxGlobalCoord;
    
            double y = TnGLUtils.getInstance().log(Math.tan(Pe.degToRad(p[i+1]) / 2.0 + Math.PI / 4.0));
    
            /* I think the extra divide by PI is that y is in radians (post
             * Mercator projection). (thant) */
            
            /* Actually, the following calculation seems to exist to keep
             * our global coordinate space the right proportion. As a
             * consequence, y=0 corresponds to approx. latitude S85.051128,
             * and y = maxGlobalCoord corresponds to latitude N85.051128.
             * Of course, x=0 corresponds to longitude W180 and
             * x = maxGlobalCoord corresponds to longitude E180.
             */
    
            double y_g = (y / (Math.PI * 2.0) + 0.5) * maxGlobalCoord;
    
            p[i] = x_g;
            p[i+1] = y_g;
        }
    }

    public static void latLonToGlobal(double[] p)
    {
        p[0] = Pe.wrap(p[0], -180.0, 180.0);
        latLonToGlobalNoWrap(p);
    }

    public static double globalToLat(double gLat)//FIXME
    {
        double y = 2.0 * Math.PI * gLat / maxGlobalCoord - Math.PI;
        return (360.0 / Math.PI) * TnGLUtils.getInstance().atan(TnGLUtils.getInstance().exp(y)) - 90.0;
    }

    public static double globalToLon(double gLon)
    {
        return gLon * 360.0 / maxGlobalCoord - 180.0;
    }

    public static double globalToAlt(double gAlt)
    {
        return gAlt;
    }
        
    public static void globalWrapAndClamp(double[] point)
    {
        // Wrap X
        if(point[0] < 0.0)
        {
            point[0] += maxGlobalCoord;
        }
        else if(point[0] > maxGlobalCoord)
        {
            point[0] -= maxGlobalCoord;
        }

        // Clamp Y
        point[1] = Math.min(Math.max(0.0f, point[1]), (double)maxGlobalCoord);
    }
}

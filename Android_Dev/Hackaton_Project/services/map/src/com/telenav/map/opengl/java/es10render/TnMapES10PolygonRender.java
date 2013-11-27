/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1PolygonRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.map.opengl.java.TnMapConf;
import com.telenav.map.opengl.java.TnMapRenderingData;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapES10PolygonRender
{
    TnGL10 gl10;

    public TnMapES10PolygonRender(TnGL10 gl10)
    {
        this.gl10 = gl10;
    }

    public void render(Vector tiles)
    {
        // @TODO: All server-specific secret knowledge needs to be
        // removed from the engine. For now, use Config to know what
        // the road layers are.

        // TnMapCheckGL("TnMapES1PolygonRender::Render start");

        int[] layers = new int[2];

        TnMapConf.getPolygonLayers(layers);

        int firstPolygonLayer = layers[0];
        int lastPolygonLayer = layers[1];

        gl10.glDisable(TnGL10.GL_TEXTURE_2D);
        gl10.glDisable(TnGL10.GL_BLEND);

        for (int layer = firstPolygonLayer; layer <= lastPolygonLayer; ++layer)
        {
            for (int i = 0; i < tiles.size(); i++)
            {
                Tile tile = (Tile) tiles.elementAt(i);
                if (tile == null)
                    continue;
                
                TnMapRenderingData rd = (TnMapRenderingData)tile.getRenderingData();
                if (rd == null)
                    continue;
                
                BufferElement polygonBuffer = rd.getPolygonBuffer();
                if (polygonBuffer == null)
                    continue;
                
                int first = polygonBuffer.layers[layer<<1];
                int count = polygonBuffer.layers[(layer<<1)+1];

                if (count > 0)
                {
                    int pointerBegin = first * 3 * 4;
                    int colorBegin = first * 4;

                    Matrixf viewMatrix = rd.getViewMatrix();
                    gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                    if (polygonBuffer.pointerBuffer != null && polygonBuffer.colorBuffer != null)
                    {
                        polygonBuffer.pointerBuffer.position(pointerBegin);
                        gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, polygonBuffer.pointerBuffer);
                        polygonBuffer.colorBuffer.position(colorBegin);
                        gl10.glColorPointer(4, TnGL10.GL_UNSIGNED_BYTE, 0, polygonBuffer.colorBuffer);
                        gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, count);
                    }
                }
            }
        }

        gl10.glEnable(TnGL10.GL_TEXTURE_2D);
        gl10.glEnable(TnGL10.GL_BLEND);

        // TnMapCheckGL("TnMapES1PolygonRender::Render end");
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1TrafficRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnTrafficRenderingData;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-9
 */
public class TnMapES10TrafficRender
{
    private TnMapTexture m_texture;
    private TnGL10 gl10;

    public TnMapES10TrafficRender(TnGL10 gl10, TnMapTextureLoader loader)
    {
        this.gl10 = gl10;
        m_texture = loader.syncLoad("TrafficTexture");
    }


    public void render(Vector tiles)
    {
        //    TnMapCheckGL("TnMapES1TrafficRender::Render start");

        // Draw traffic glow
        if(m_texture != null && m_texture.isLoaded())
        {
            m_texture.startUsing();
            for (int i = 0; i < tiles.size(); i++)
            {
                Tile tile = (Tile)tiles.elementAt(i);
                if (tile == null)
                    continue;
                
                TnTrafficRenderingData rd = (TnTrafficRenderingData)tile.getRenderingData();
                if (rd == null)
                    continue;
                
                BufferElement trafficFlowBuffer = rd.getTrafficFlowBuffer();

                int count = trafficFlowBuffer.layers[0];
                int pointerBegin = 0, colorBegin = 0, texCoordBegin = 0;

                Matrixf viewMatrix = rd.getViewMatrix();
                gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                if (trafficFlowBuffer.pointerBuffer != null && trafficFlowBuffer.texCoordBuffer != null
                        && trafficFlowBuffer.colorBuffer != null)
                {
                    trafficFlowBuffer.pointerBuffer.position(pointerBegin);
                    gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, trafficFlowBuffer.pointerBuffer);
                    trafficFlowBuffer.texCoordBuffer.position(texCoordBegin);
                    gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, 0, trafficFlowBuffer.texCoordBuffer);
                    trafficFlowBuffer.colorBuffer.position(colorBegin);
                    gl10.glColorPointer(4, TnGL10.GL_UNSIGNED_BYTE, 0, trafficFlowBuffer.colorBuffer);
                    gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, count);
                }
            }

            m_texture.stopUsing();
        }

        //    TnMapCheckGL("TnMapES1TrafficRender::Render end");
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1OnewayArrowRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.map.opengl.java.TnMapRenderingData;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.TnMapRenderingData.ArrowVBO;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.nio.TnNioManager;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapES10OnewayArrowRender
{
    TnMapTexture m_texture;

    TnGL10 gl10;

    public TnMapES10OnewayArrowRender(TnGL10 gl10, TnMapTextureLoader loader)
    {
        this.gl10 = gl10;
        m_texture = loader.syncLoad("arrow");
    }

    // @TODO: tiles is passed in by ref instead of const ref because it is
    // currently the renderer's job to bake the geom into a vbo. This
    // should be fixed.

    public void render(Vector tiles)
    {
        // TnMapCheckGL("TnMapES1OnewayArrowRender::Render start");

        if (m_texture != null && m_texture.isLoaded())
        {
            m_texture.startUsing();
            for (int i = 0; i < tiles.size(); i++)
            {
                Tile tile = (Tile) tiles.elementAt(i);
                if (tile == null)
                    continue;
                
                TnMapRenderingData rd = (TnMapRenderingData)tile.getRenderingData();
                if (rd == null)
                    continue;
                
                ArrowVBO arrow_vbo = rd.getArrowVBO();
                if (!arrow_vbo.geom.isEmpty())
                {
                    // New geometry. Build a vbo.
                    arrow_vbo.count = arrow_vbo.geom.size();
                    int size = arrow_vbo.count * TnMapVertex5f4b.size();

                    ByteBuffer vbb = TnNioManager.getInstance().allocateDirect(size );
                    FloatBuffer data = vbb.asFloatBuffer();
                    for (int n = 0; n < arrow_vbo.geom.size(); n++)
                    {
                        TnMapVertex5f4b vertex = (TnMapVertex5f4b) arrow_vbo.geom.elementAt(n);
                        data.put(vertex.x);
                        data.put(vertex.y);
                        data.put(vertex.z);
                        data.put(vertex.color.r);
                        data.put(vertex.color.g);
                        data.put(vertex.color.b);
                        data.put(vertex.color.a);
                        data.put(vertex.s);
                        data.put(vertex.t);
                    }
                    data.position(0);
                    arrow_vbo.buffer = vbb;
                    arrow_vbo.geom.removeAllElements();
                }

                Matrixf viewMatrix = rd.getViewMatrix();
                gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                int begin = 0;

                if (arrow_vbo.buffer != null)
                {
                    arrow_vbo.buffer.position(begin);
                    gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), arrow_vbo.buffer);
                    arrow_vbo.buffer.position(begin + 3 * 4 + TnMapColor.size());
                    gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), arrow_vbo.buffer);
                    arrow_vbo.buffer.position(begin + 3 * 4);
                    gl10.glColorPointer(4, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), arrow_vbo.buffer);
                    gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, arrow_vbo.count);
                }
            }
            
            m_texture.stopUsing();
        }

        // TnMapCheckGL("TnMapES1OnewayArrowRender::Render end");
    }
}

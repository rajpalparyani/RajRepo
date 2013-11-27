/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1RouteRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.nio.Buffer;

import com.telenav.map.opengl.java.Pair;
import com.telenav.map.opengl.java.TnMapArrow;
import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapConf;
import com.telenav.map.opengl.java.TnMapMagic;
import com.telenav.map.opengl.java.TnMapRoute;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-9
 */
public class TnMapES10RouteRender
{
    private TnMapTexture m_texture;

    private TnGL10 gl10;

    public TnMapES10RouteRender(TnGL10 gl10, TnMapTextureLoader loader)

    {
        this.gl10 = gl10;
        m_texture = loader.syncLoad("roadtexture_route");
    }

    public void render(TnMapRoute route, TnMapCamera camera)
    {
        // TnMapCheckGL("TnMapES1RouteRender::Render3D start");

        if (route != null)
        {
            // using namespace tngm;

            if (m_texture != null && m_texture.isLoaded())
            {
                m_texture.startUsing();
                gl10.glDisableClientState(TnGL10.GL_COLOR_ARRAY);

                TnMapColor color = TnMapConf.getFeatureColor(TnMapConf.FEATURE_TYPE_ROUTE);
                gl10.glColor4f(color.r / 255.f, color.g / 255.f, color.b / 255.f, color.a / 255.f);

                int zoom = TnMapMagic.zoomSpaceToZoomLevel(camera.getZoom());

                Matrixf viewMatrix = camera.GetViewMatrix(route.getOrigin());

                gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                int begin = 0;

                {
                    // Render route.

                    Pair bufferAndCount = route.getVBOBufferAndCount(zoom);
                    Buffer buffer = (Buffer) bufferAndCount.first;
                    int count = ((Integer) bufferAndCount.second).intValue();

                    buffer.position(begin);
                    gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f.size(), buffer);
                    buffer.position(begin + 3 * 4);
                    gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f.size(), buffer);
                    gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, count);
                }

                gl10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                gl10.glEnableClientState(TnGL10.GL_COLOR_ARRAY);

                m_texture.stopUsing();
            }
        }

        // TnMapCheckGL("TnMapES1RouteRender::Render3D end");

    }

    public void renderArrows(TnMapRoute route, TnMapCamera camera)
    {
        // TnMapCheckGL("TnMapES1RouteRender::RenderArrows start");

        if (route != null)
        {
            // using namespace tngm;

            if (m_texture != null && m_texture.isLoaded())
            {
                m_texture.startUsing();
                int zoom = TnMapMagic.zoomSpaceToZoomLevel(camera.getZoom());

                Matrixf viewMatrix = camera.GetViewMatrix(route.getOrigin());

                gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                int begin = 0;

                int acount = route.arrowsCount();
                for (int i = 0; i < acount; ++i)
                {
                    TnMapArrow arrow = route.getArrow(i);

                    if (arrow != null && arrow.isEnabled())
                    {
                        Pair bufferAndCount = arrow.getVBOBufferAndCount(zoom);
                        Buffer buffer = (Buffer) bufferAndCount.first;
                        int count = ((Integer) bufferAndCount.second).intValue();

                        if (buffer != null)
                        {
                            buffer.position(begin);
                            gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), buffer);
                            buffer.position(begin + 3 * 4 + TnMapColor.size());
                            gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), buffer);
                            buffer.position(begin + 3 * 4);
                            gl10.glColorPointer(4, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), buffer);
                            gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, count);
                        }
                    }
                }

                m_texture.stopUsing();
            }
        }
        // TnMapCheckGL("TnMapES1RouteRender::RenderArrows end");

    }
}

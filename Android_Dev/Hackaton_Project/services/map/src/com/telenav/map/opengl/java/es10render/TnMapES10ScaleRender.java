/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1ScaleRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapScale;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-9
 */
public class TnMapES10ScaleRender
{
    TnMapTexture m_barTexture;

    TnMapTexture m_fontTexture;

    TnGL10 gl10;

    public TnMapES10ScaleRender(TnGL10 gl10, TnMapTextureLoader loader, TnMapTexture fontTexture)
    {
        this.gl10 = gl10;
        m_barTexture = loader.syncLoad("MapBar");
        m_fontTexture = fontTexture;
    }

    public void render(TnMapScale mapScale, TnMapCamera camera)
    {
        // TnMapCheckGL("TnMapES1ScaleRender::Render start");
        if (mapScale != null)
        {
            if (m_fontTexture != null && m_fontTexture.isLoaded())
            {
                // text
                if (mapScale.m_textBuffer != null)
                {
                    m_fontTexture.startUsing();
                    mapScale.m_textBuffer.position(0);
                    gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), mapScale.m_textBuffer);
                    mapScale.m_textBuffer.position(3 * 4 + TnMapColor.size());
                    gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f4b.size() , mapScale.m_textBuffer);
                    mapScale.m_textBuffer.position(3 * 4);
                    gl10.glColorPointer(4, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), mapScale.m_textBuffer);
                    gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, mapScale.m_textVBOCount);

                    m_fontTexture.stopUsing();
                }
            }
            // log_addString("TnMapES1Renderer----------mapScale--------444444444444444444");
            if (m_barTexture != null && m_barTexture.isLoaded())
            {
                // bar
                if (mapScale.m_barBuffer != null)
                {
                    m_barTexture.startUsing();
                    if (mapScale.m_barBuffer != null)
                    {
                        mapScale.m_barBuffer.position(0);
                        gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f4b.size() , mapScale.m_barBuffer);
                        mapScale.m_barBuffer.position(3 * 4 + TnMapColor.size());
                        gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f4b.size() , mapScale.m_barBuffer);
                        mapScale.m_barBuffer.position(3 * 4);
                        gl10.glColorPointer(4, TnGL10.GL_FLOAT, TnMapVertex5f4b.size() , mapScale.m_barBuffer);
                        gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, mapScale.m_barVBOCount);
                    }
                    m_barTexture.stopUsing();
                }
            }
        }

        // TnMapCheckGL("TnMapES1ScaleRender::Render end");
    }
}

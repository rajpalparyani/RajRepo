/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1SkyDomeRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.telenav.map.opengl.java.TnMapSkyDome;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.nio.TnNioManager;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-9
 */
public class TnMapES10SkyDomeRender
{
    TnGL10 gl10;
    ByteBuffer dataBuffer, texCoordBuffer;

    public TnMapES10SkyDomeRender(TnGL10 gl10)
    {
        this.gl10 = gl10;
    }

    public void render(TnMapSkyDome skyDome, Matrixf matrix)
    {
        // TnMapCheckGL("TnMapES1SkyDomeRender::Render start");

        if (skyDome != null)
        {
            if (skyDome.getTexture() != null && skyDome.getTexture().isLoaded())
            {
                // TnMapCheckGL("TnMapSkyDome::Draw begin");
                skyDome.getTexture().startUsing();
                gl10.glDisableClientState(TnGL10.GL_COLOR_ARRAY);

                // Draw
                float[] matrixArray = matrix.getArray();
                gl10.glLoadMatrixf(matrixArray, 0);

                if (skyDome.getVertices() != null)
                {
                    dataBuffer = TnNioManager.getInstance().allocateDirect(skyDome.getVertices().length * 3 * 4);
                    FloatBuffer data = dataBuffer.asFloatBuffer();

                    texCoordBuffer = TnNioManager.getInstance().allocateDirect(skyDome.getVertices().length * 2 * 4);
                    FloatBuffer data2 = texCoordBuffer.asFloatBuffer();
                    for (int n = 0; n < skyDome.getVertices().length; n++)
                    {
                        TnMapVertex5f vertex = skyDome.getVertices()[n];
                        data.put(vertex.x);
                        data.put(vertex.y);
                        data.put(vertex.z);
                        data2.put(vertex.s);
                        data2.put(vertex.t);
                    }
                    skyDome.destroy();
                }
                
                if (dataBuffer != null && texCoordBuffer != null)
                {
                    gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, dataBuffer);
                    gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, 0, texCoordBuffer);
    
                    gl10.glDrawArrays(TnGL10.GL_TRIANGLE_FAN, 0, skyDome.getFanVertexCount());
                    gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, skyDome.getFanVertexCount(), skyDome.getStripVertexCount());
                }
                // Shutdown
                gl10.glEnableClientState(TnGL10.GL_COLOR_ARRAY);
                // TnMapCheckGL("TnMapSkyDome::Draw end");
            
                skyDome.getTexture().stopUsing();
            }
        }

        // TnMapCheckGL("TnMapES1ScaleRender::Render end");
    }
}

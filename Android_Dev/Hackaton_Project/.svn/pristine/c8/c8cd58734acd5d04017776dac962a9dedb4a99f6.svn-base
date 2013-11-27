/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1MarkRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapRenderingData;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapES10MarkRender
{
    TnMapTexture texture;

    TnGL10 gl10;

    public TnMapES10MarkRender(TnGL10 gl10, TnMapTexture texture)

    {
        this.gl10 = gl10;
        this.texture = texture;
    }

    public void render(Vector tiles, TnMapCamera camera)
    {

        // TnMapCheckGL("TnMapES1MarkRender::Render3D start");

//        if (camera.GetDeclination() <= TnMapConf.DECLINATION_FOR_TEXT_2D3D_SWITCH)
        {
            if (texture != null && texture.isLoaded())
            {
                texture.startUsing();
                gl10.glTexEnvf(TnGL10.GL_TEXTURE_ENV, TnGL10.GL_TEXTURE_ENV_MODE, TnGL10.GL_BLEND);

                for (int i = 0; i < tiles.size(); i++)
                {
                    Tile tile = (Tile) tiles.elementAt(i);
                    if (tile == null)
                        continue;
                    
                    TnMapRenderingData rd = (TnMapRenderingData)tile.getRenderingData();
                    if (rd == null)
                        continue;
                    
                    BufferElement text2dBuffer = rd.getText2DBuffer();
                    if (text2dBuffer == null)
                        continue;
                    
                    Matrixf viewMatrix = rd.getViewMatrix();
                    gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                    int begin = 0;

                    if (text2dBuffer.pointerBuffer != null && text2dBuffer.colorBuffer != null
                            && text2dBuffer.texCoordBuffer != null)
                    {
                        text2dBuffer.pointerBuffer.position(begin);
                        gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, text2dBuffer.pointerBuffer);
                        text2dBuffer.texCoordBuffer.position(0);
                        gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, 0, text2dBuffer.texCoordBuffer);
                        text2dBuffer.colorBuffer.position(0);
                        gl10.glColorPointer(4, TnGL10.GL_UNSIGNED_BYTE, 0, text2dBuffer.colorBuffer);
                        gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, text2dBuffer.layers[0]);
                    }
                }

                gl10.glTexEnvf(TnGL10.GL_TEXTURE_ENV, TnGL10.GL_TEXTURE_ENV_MODE, TnGL10.GL_MODULATE);

                texture.stopUsing();
            }
        }

        // TnMapCheckGL("TnMapES1MarkRender::Render3D end");
    }
}

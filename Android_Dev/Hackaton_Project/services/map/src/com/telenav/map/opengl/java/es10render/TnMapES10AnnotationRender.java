/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1AnnotationRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.util.Vector;

import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapAnnotation.TnMapAnnotation2D;
import com.telenav.map.opengl.java.TnMapAnnotation.TnMapAnnotation3D;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapES10AnnotationRender
{
    TnGL10 gl10;

    public TnMapES10AnnotationRender(TnGL10 gl10)
    {
        this.gl10 = gl10;
    }

    public void render2D(Vector layers)// TnMapAnnotation2DLayers
    {
        // TnMapCheckGL("TnMapES1AnnotationRender::Render2D start");
        gl10.glDisableClientState(TnGL10.GL_COLOR_ARRAY);
        for (int i = 0; i < layers.size(); i++)
        {
            Vector tnMapAnnotation2DSet = (Vector) layers.elementAt(i);
            for (int j=0; j<tnMapAnnotation2DSet.size(); j++)
            {
                TnMapAnnotation2D annotation = (TnMapAnnotation2D) tnMapAnnotation2DSet.elementAt(j);
                if (annotation.isEnabled())
                {
                    TnMapTexture texture = annotation.getTexture();
                    if (texture != null && texture.isLoaded())
                    {
                        texture.startUsing();
                        BufferElement buffer = annotation.getVertexData();

                        buffer.pointerBuffer.position(0);
                        gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, buffer.pointerBuffer);
                        buffer.texCoordBuffer.position(0);
                        gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, 0, buffer.texCoordBuffer);
                        gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, buffer.layers[0]);

                        texture.stopUsing();
                    }
                }
            }
        }

        gl10.glEnableClientState(TnGL10.GL_COLOR_ARRAY);
        // TnMapCheckGL("TnMapES1AnnotationRender::Render2D end");
    }

    /*
     * ! Render 3D Annotations in a 3D projected space.
     */
    public void render3D(Vector layers, TnMapCamera camera, float scale)
    {
        // TnMapCheckGL("TnMapES1AnnotationRender::Render3D start");

        // Setup OpenGL
        gl10.glDisableClientState(TnGL10.GL_COLOR_ARRAY);

        // Draw
        for (int i = 0; i < layers.size(); i++)
        {
            Vector tnMapAnnotation3DSet = (Vector) layers.elementAt(i);
            for (int j=0; j<tnMapAnnotation3DSet.size(); j++)
            {
                TnMapAnnotation3D annotation = (TnMapAnnotation3D) tnMapAnnotation3DSet.elementAt(j);
                if (annotation.isEnabled())
                {
                    TnMapTexture texture = annotation.getTexture();
                    if (texture != null && texture.isLoaded())
                    {
                        Matrixf view_mat = annotation.buildViewMatrix(camera);

                        gl10.glMatrixMode(TnGL10.GL_MODELVIEW);
                        gl10.glLoadMatrixf(view_mat.getArray(), 0);

                        texture.startUsing();
                        BufferElement buffer = annotation.getVertexData();

                        buffer.pointerBuffer.position(0);
                        gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, buffer.pointerBuffer);
                        buffer.texCoordBuffer.position(0);
                        gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, 0, buffer.texCoordBuffer);
                        gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, buffer.layers[0]);

                        texture.stopUsing();
                    }
                }
            }
        }

        gl10.glEnableClientState(TnGL10.GL_COLOR_ARRAY);

        // TnMapCheckGL("TnMapES1AnnotationRender::Render3D end");
    }
}

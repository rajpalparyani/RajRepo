/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1RoadRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.map.opengl.java.TnMapConf;
import com.telenav.map.opengl.java.TnMapRenderingData;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapES10RoadRender
{
    private TnMapTexture m_textureHighway, textureArterial, textureLocal;

    TnGL10 gl10;

    public TnMapES10RoadRender(TnGL10 gl10, TnMapTextureLoader loader) 
    {
        this.gl10 = gl10;
        
        m_textureHighway = loader.syncLoad("roadtexture_highway");
        textureArterial = loader.syncLoad("roadtexture_arterial");
        textureLocal = loader.syncLoad("roadtexture_local");
    }

    public void render(Vector tiles)
    {
        int[] layers = new int[2];
        TnMapConf.getRoadLayers(layers);
        int firstRoadLayer = layers[0];
        int lastRoadLayer = layers[1];

        render(tiles, firstRoadLayer, lastRoadLayer);
    }

    public void renderNonFreeways(Vector tiles)
    {
        int[] layers = new int[2];
        TnMapConf.getRoadLayers(layers);
        int firstRoadLayer = layers[0];
//        int lastRoadLayer = layers[1];
        int freewayLayer = TnMapConf.getFreewayLayer();

        render(tiles, firstRoadLayer, freewayLayer - 1);
    }

    public void renderFreeways(Vector tiles)
    {
        int[] layers = new int[2];
        TnMapConf.getRoadLayers(layers);
//        int firstRoadLayer = layers[0];
        int lastRoadLayer = layers[1];
        int freewayLayer = TnMapConf.getFreewayLayer();

        render(tiles, freewayLayer, lastRoadLayer);
    }
    
    private TnMapTexture getTexture(int layer)
    {
        switch(layer)
        {
            case 7:
            case 11:
                return m_textureHighway;
            case 6:
            case 10:
                return textureArterial;
            default:
                return textureLocal;
        }
    }

    public void render(Vector tiles, int first_layer, int last_layer)
    {
        TnMapTexture currentTexture = null;
        
//        float[] matrixArray = new float[16];
        
        for (int layer = first_layer; layer <= last_layer; ++layer)
        {
            TnMapTexture texture = getTexture(layer);
            if (currentTexture != texture)
            {
                if (currentTexture != null)
                    currentTexture.stopUsing();
                if (texture != null && texture.isLoaded())
                {
                    currentTexture = texture;
                    currentTexture.startUsing();
                }
                else
                {
                    currentTexture = null;
                    continue;
                }
            }
            
            for (int i = 0; i < tiles.size(); i++)
            {
                Tile tile = (Tile) tiles.elementAt(i);
                if (tile == null)
                    continue;
                
                TnMapRenderingData rd = (TnMapRenderingData)tile.getRenderingData();
                if (rd == null)
                    continue;
                
                BufferElement buffer = rd.getEdgeBuffer();
                if (buffer != null)
                {
//                        VBOSegment it = (VBOSegment) buffer.layers.get(layer);

                    if (buffer.layers[layer << 1] != -1)
                    {
                        int first = buffer.layers[layer << 1];
                        int count = buffer.layers[(layer << 1) + 1];

                        int pointerBegin = first * 3 * 4;
                        int texCoordBegin = first * 2 * 4;
                        int colorBegin = first * 4;

                        Matrixf viewMatrix = rd.getViewMatrix();
                        float[] matrixArray = viewMatrix.getArray();
                        gl10.glLoadMatrixf(matrixArray, 0);
                        
                        if (buffer.pointerBuffer != null && buffer.texCoordBuffer != null && buffer.colorBuffer != null)
                        {
                            buffer.pointerBuffer.position(pointerBegin);
                            gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, buffer.pointerBuffer);
                            buffer.texCoordBuffer.position(texCoordBegin);
                            gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, 0, buffer.texCoordBuffer);
                            buffer.colorBuffer.position(colorBegin);
                            gl10.glColorPointer(4, TnGL10.GL_UNSIGNED_BYTE, 0, buffer.colorBuffer);
                            gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, count);
                        }
                    }
                }
            }
        }
        if (currentTexture != null)
            currentTexture.stopUsing();
        // TnMapCheckGL("TnMapES1RoadRender::Render end");

    }
}

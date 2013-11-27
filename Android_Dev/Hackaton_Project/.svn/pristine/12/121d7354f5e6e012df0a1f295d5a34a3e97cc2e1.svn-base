/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1IncidentRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnTrafficRenderingData;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.TnTrafficRenderingData.IncidentVBO;
import com.telenav.map.opengl.java.TnTrafficRenderingData.IncidentVBOSegment;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.nio.TnNioManager;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapES10IncidentRender
{
    TnGL10 gl10;

    TnMapTextureLoader m_loader;

    // Incident icon textures
    Hashtable m_incidentTextures = new Hashtable(); // std::map<std::string,TnMapTexture::ptr>

    public TnMapES10IncidentRender(TnGL10 gl10, TnMapTextureLoader loader)
    {
        this.gl10 = gl10;
        m_loader = loader;
    }

    public void render(Vector tiles, TnMapCamera camera)
    {

        // TnMapCheckGL("TnMapES1IncidentRender::Render3D start");

        // Draw traffic incidents
        for (int i = 0; i < tiles.size(); i++)
        {
            Tile tile = (Tile) tiles.elementAt(i);
            if (tile == null)
                continue;
            
            TnTrafficRenderingData rd = (TnTrafficRenderingData)tile.getRenderingData();
            if (rd == null)
                continue;
            
            IncidentVBO incident_vbo = rd.getIncidentVBO();

            if (!incident_vbo.geom.isEmpty())
            {
                // New geometry. Build a vbo. This all belongs in
                // a vbo object.
                int size = incident_vbo.geom.size() * TnMapVertex5f4b.size();
                ByteBuffer vbb = TnNioManager.getInstance().allocateDirect(size);
                FloatBuffer data = vbb.asFloatBuffer();
                for (int n = 0; n < incident_vbo.geom.size(); n++)
                {
                    TnMapVertex5f4b vertex = (TnMapVertex5f4b) incident_vbo.geom.elementAt(n);
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
                incident_vbo.buffer = vbb;
                incident_vbo.geom.removeAllElements();
            }

            Vector incident_segments = incident_vbo.segments;

            for (int j = 0; j < incident_segments.size(); j++)
            {
                IncidentVBOSegment segement = (IncidentVBOSegment) incident_segments.elementAt(j);
                double[] pos = segement.pos;
                int first = segement.begin;
                int count = segement.count;
                String icon = segement.icon;

                int begin = first * TnMapVertex5f4b.size();

                // Load view matrix for this segment
                float scale = camera.Get2DScaleFactor();

                Matrixf cameraMatrix = camera.GetCameraMatrix();
                Matrixf viewMatrix = camera.GetSpriteMatrix(pos);
                Matrixf scaleMatrix = new Matrixf();
                scaleMatrix.buildScale(scale);

                viewMatrix = Matrixf.multiply(Matrixf.multiply(cameraMatrix, viewMatrix), scaleMatrix);
                gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                // Look up incident texture
                TnMapTexture mapTexture = (TnMapTexture) m_incidentTextures.get(icon);
                if (mapTexture == null)
                {
                    // Texture not loaded yet. Load.
                    m_incidentTextures.put(icon, m_loader.syncLoad(icon));
                }
                else if (mapTexture.isLoaded())
                {
                    // Texture is loaded. Draw shield
                    mapTexture.startUsing();
                    if (incident_vbo.buffer != null)
                    {
                        incident_vbo.buffer.position(begin);
                        gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), incident_vbo.buffer);
                        incident_vbo.buffer.position(begin + 3 * 4 + TnMapColor.size());
                        gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), incident_vbo.buffer);
                        incident_vbo.buffer.position(begin + 3 * 4);
                        gl10.glColorPointer(4, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), incident_vbo.buffer);
                        gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, count);
                    }
                    mapTexture.stopUsing();
                }
            }
        }

        // TnMapCheckGL("TnMapES1IncidentRender::Render3D start");
    }
}

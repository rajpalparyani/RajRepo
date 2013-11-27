/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1TextFlagRender.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.nio.Buffer;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.datatypes.map.Tile;
import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapCameraManager;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-9
 */
public class TnMapES10TextFlagRender
{
    public static class TextObj
    {
        public Matrixf matrix;

        public float[] screenPos;

        public double[] worldPos;

        public Buffer shield_buffer;

        public int shield_begin;

        public int shield_end;

        public String shield_icon;

        public Buffer text_buffer;

        public int text_begin;

        public int text_end;

        public int text_checksum;

        public Tile tilePtr;

        public int zoomLevel;
    }

    // Reference to texture loader (for lazy-loading of highway shields)
    TnMapTextureLoader m_loader;

    // Font textures
    TnMapTexture m_texture;

    Vector m_renderList = new Vector();// TextObj

    float m_activeDeclination;

    // Highway shields
    Hashtable m_shieldTextures = new Hashtable(); // <std::string,TnMapTexture::ptr>

    TnGL10 gl10;

    public TnMapES10TextFlagRender(TnGL10 gl10, TnMapTextureLoader loader, TnMapTexture texture)
    {
        this.gl10 = gl10;
        m_loader = loader;
        m_activeDeclination = 0.0f;
        m_texture = texture;
    }

    // @TODO: tiles is passed in by ref instead of const ref because it is
    // currently the renderer's job to bake the geom into a vbo. This
    // should be fixed.

    public void render(Vector tiles, TnMapCameraManager cameraManager, TnMapCamera camera)
    {
        // TnMapCheckGL("TnMapES1TextFlagRender::Render start");

//        Matrixf cameraMatrix = camera.GetCameraMatrix();
//        Matrixf projMatrix = camera.GetProjectionMatrix();
//
//        float widthTest = 2.f * TnMapConf.TEXT_CONFLICT_RADIUS / camera.GetScreenSize()[0];
//        float heightTest = 2.f * TnMapConf.TEXT_CONFLICT_RADIUS / camera.GetScreenSize()[1];
//
//        // If we went from tilted to top-down
//        if ((m_activeDeclination > TnMapConf.DECLINATION_FOR_TEXT_2D3D_SWITCH)
//                && (camera.GetDeclination() <= TnMapConf.DECLINATION_FOR_TEXT_2D3D_SWITCH))
//        {
//            m_renderList.clear();
//        }
//        m_activeDeclination = camera.GetDeclination();
//
//        boolean tilted = camera.GetDeclination() > TnMapConf.DECLINATION_FOR_TEXT_2D3D_SWITCH;
//
//        // List for text culling
//        Hashtable cullList = new Hashtable();// <float,TextObj>
//
//        for (int i = 0; i < tiles.size(); i++)
//        {
//            TnMapTile tile = (TnMapTile) tiles.elementAt(i);
//            if (tile.GetTextState() == TnMapTile.eState_valid)
//            {
//                Text3DVBO text3d_vbo = tile.GetText3DVBO();
//                if (!text3d_vbo.geom.isEmpty())
//                {
//                    // New geometry. Build a vbo. This all belongs in
//                    // a vbo object.
//                    int size = text3d_vbo.geom.size() * TnMapVertex5f4b.size();
//                    ByteBuffer vbb = ByteBuffer.allocateDirect(size);
//                    vbb.order(ByteOrder.nativeOrder());
//                    FloatBuffer data = vbb.asFloatBuffer();
//                    for (int n = 0; n < text3d_vbo.geom.size(); n++)
//                    {
//                        TnMapVertex5f4b vertex = (TnMapVertex5f4b) text3d_vbo.geom.elementAt(n);
//                        data.put(vertex.x);
//                        data.put(vertex.y);
//                        data.put(vertex.z);
//                        data.put(vertex.color.r / 255.0f);
//                        data.put(vertex.color.g / 255.0f);
//                        data.put(vertex.color.b / 255.0f);
//                        data.put(vertex.color.a / 255.0f);
//                        data.put(vertex.s);
//                        data.put(vertex.t);
//                    }
//                    data.position(0);
//                    text3d_vbo.buffer = vbb;
//                    text3d_vbo.geom.clear();
//                }
//
//                ShieldVBO shield_vbo = tile.GetShieldVBO();
//                if (!shield_vbo.geom.isEmpty())
//                {
//                    // New geometry. Build a vbo. This all belongs in
//                    // a vbo object.
//                    int size = shield_vbo.geom.size() * TnMapVertex5f4b.size();
//                    ByteBuffer vbb = ByteBuffer.allocateDirect(size);
//                    vbb.order(ByteOrder.nativeOrder());
//                    FloatBuffer data = vbb.asFloatBuffer();
//                    for (int n = 0; n < shield_vbo.geom.size(); n++)
//                    {
//                        TnMapVertex5f4b vertex = (TnMapVertex5f4b) shield_vbo.geom.elementAt(n);
//                        data.put(vertex.x);
//                        data.put(vertex.y);
//                        data.put(vertex.z);
//                        data.put(vertex.color.r / 255.0f);
//                        data.put(vertex.color.g / 255.0f);
//                        data.put(vertex.color.b / 255.0f);
//                        data.put(vertex.color.a / 255.0f);
//                        data.put(vertex.s);
//                        data.put(vertex.t);
//                    }
//                    data.position(0);
//                    shield_vbo.buffer = vbb;
//                    shield_vbo.geom.clear();
//                }
//
//                Vector text3d_segments = tile.GetText3DVBO().segments; // Text3DVBOSegments
//                Vector shield_segments = tile.GetShieldVBO().segments; // ShieldVBOSegments
//
//                for (int j = 0; j < text3d_segments.size(); j++)
//                {
//                    Text3DVBOSegment textSegment = (Text3DVBOSegment) text3d_segments.elementAt(j);
//                    double[] pos = textSegment.pos;
//
//                    int checksum = textSegment.checksum;
//                    int first = textSegment.begin;
//                    int count = textSegment.count;
//                    int shieldIndex = textSegment.shieldIndex;
//
//                    int begin = first * TnMapVertex5f4b.size();
//
//                    float scale = camera.Get2DScaleFactor();
//
//                    Matrixf viewMatrix = camera.GetSpriteMatrix(pos);
//                    Matrixf scaleMatrix = new Matrixf(new Scalef(scale));
//                    viewMatrix = Matrixf.multiply(Matrixf.multiply(cameraMatrix, viewMatrix), scaleMatrix);
//
//                    // Add to cull list
//                    TextObj object = new TextObj();
//                    object.matrix = new Matrixf(viewMatrix);
//                    object.text_buffer = text3d_vbo.buffer;
//                    object.text_begin = begin;
//                    object.text_end = begin + count * TnMapVertex5f4b.size();
//                    object.text_checksum = checksum;
//                    object.worldPos = pos;
//                    object.tilePtr = tile;
//                    float[] point3D = new float[]{0, 0, 0};
//                    float[] point2D = Transform.transform(Matrixf.multiply(projMatrix, viewMatrix), point3D);
//                    object.screenPos = new float[]{point2D[0], point2D[1]};
//                    object.zoomLevel = tile.GetZoomLevel();
//
//                    if (shieldIndex == -1)
//                    {
//                        // No shield (this should no longer ever happen)
//                        // TnMapLogError("TnMapES1TextFlagRender::Render: Text object has no backing shield!");
//                        // assert(shieldIndex >= 0);
//                    }
//                    else
//                    {
//                        // With associated shield
//                        object.shield_buffer = shield_vbo.buffer;
//
//                        ShieldVBOSegment shieldVBOSegment = (ShieldVBOSegment) shield_segments.elementAt(shieldIndex);
//                        object.shield_begin = shieldVBOSegment.begin * TnMapVertex5f4b.size();
//                        object.shield_end = object.shield_begin + shieldVBOSegment.count * TnMapVertex5f4b.size();
//                        object.shield_icon = shieldVBOSegment.icon;
//
//                        // Only draw if it's on the screen, AND one of:
//                        // 1. our view is tilted
//                        // 2. the text has an associated shield not called TnMapTile::DEFAULT_SIGN
//                        boolean onScreen = Math.abs(point2D[0]) < 1.0
//                                && Math.abs(point2D[1]) < TnMapConf.BILLBOARD_VISIBILITY_THRESHOLD;
//                        boolean roadSign = !TnMapTile.DEFAULT_SIGN.equals(object.shield_icon);
//
//                        if (onScreen && tilted && !roadSign)
//                        {
//                            float distanceSquared = (float) (Math.pow(object.screenPos[0] * 100, 2.f) + 
//                                    Math.pow(object.screenPos[1] * 100, 2.f));
//
//                            cullList.put(new Float(distanceSquared), object);
//                        }
//                        else if (roadSign && (Math.abs(point2D[0]) < 1.0 && Math.abs(point2D[1]) < 1.0))
//                        {
//                            boolean same_name = false;
//                            for (int t = 0; t < m_renderList.size() && !same_name; t++)
//                            {
//                                TextObj textObj = (TextObj) m_renderList.elementAt(t);
//                                if (object.shield_begin == textObj.shield_begin && object.shield_buffer == textObj.shield_buffer)
//                                    same_name = true;
//                            }
//                            if (!same_name)
//                                m_renderList.add(object);
//
//                        }
//                    }
//                }
//            }
//        }
//
//        // Remove old Text from Render List and update visible items
//        // @TODO: Algorithm is confusing and not very efficient
//        for (int t = 0; t < m_renderList.size(); t++)
//        {
//            TextObj textObj = (TextObj) m_renderList.elementAt(t);
//            if (textObj.zoomLevel != cameraManager.GetZoomLevel())
//            {
//                m_renderList.clear();
//                break;
//            }
//
//            float[] pos = textObj.screenPos;
//            if (TnMapTile.DEFAULT_SIGN.equals(textObj.shield_icon)
//                    && (Math.abs(pos[1]) > TnMapConf.BILLBOARD_VISIBILITY_THRESHOLD || Math.abs(pos[0]) > 1.0))
//            {
//                m_renderList.remove(textObj);
//
//                if (m_renderList.size() == 0)
//                    break;
//
//                t = 0;
//            }
//            else if (!TnMapTile.DEFAULT_SIGN.equals(textObj.shield_icon) && 
//                    (Math.abs(pos[1]) > 1.0 || Math.abs(pos[0]) > 1.0))
//            {
//                m_renderList.remove(textObj);
//
//                if (m_renderList.size() == 0)
//                    break;
//
//                t = 0;
//            }
//            else
//            {
//                float scale = camera.Get2DScaleFactor();
//
//                Matrixf viewMatrix = camera.GetSpriteMatrix(textObj.worldPos);
//                Matrixf scaleMatrix = new Matrixf(new Scalef(scale));
//                viewMatrix = Matrixf.multiply(Matrixf.multiply(cameraMatrix, viewMatrix), scaleMatrix);
//
//                float[] point3D = new float[]{0, 0, 0};
//                float[] point2D = Transform.transform(Matrixf.multiply(projMatrix, viewMatrix), point3D);
//                textObj.screenPos = new float[]{point2D[0], point2D[1]};
//                textObj.matrix = viewMatrix;
//            }
//        }
//
//        // Cull Pass
//        Iterator entryIter = cullList.entrySet().iterator();
//        while (entryIter.hasNext())
//        {
//            Entry entry = (Entry) entryIter.next();
//            TextObj object = (TextObj) entry.getValue();
//            boolean too_close = false;
//            boolean same_name = false;
//
//            for (int i = 0; i < m_renderList.size() && !too_close && !same_name; i++)
//            {
//                TextObj textObj = (TextObj) m_renderList.elementAt(i);
//                if (textObj.text_checksum == object.text_checksum)
//                {
//                    same_name = true;
//                }
//                else
//                {
//
//                    float[] aPoint = Pe.sub(textObj.screenPos, object.screenPos);
//
//                    too_close = (Math.abs(aPoint[0]) < widthTest) && (Math.abs(aPoint[1]) < heightTest);
//                }
//            }
//
//            if (!too_close && !same_name)
//            {
//                m_renderList.add(object);
//            }
//        }
//
//        // Render
//
//        // Draw each, in arbitrary order
//        for (int i = 0; i < m_renderList.size(); i++)
//        {
//            TextObj textObj = (TextObj) m_renderList.elementAt(i);
//            // State common to both shield and text
//            gl10.glLoadMatrixf(textObj.matrix.getArray(), 0);
//
//            // Look up shield texture
//            if (textObj.shield_icon.charAt(0) == 'C' || textObj.shield_icon.charAt(0) == 'M' || textObj.shield_icon.charAt(0) == 'P')
//                textObj.shield_icon = "USC_1.bmp";
//
//            TnMapTexture it = (TnMapTexture) m_shieldTextures.get(textObj.shield_icon);
//            if (it == null)
//            {
//                // Texture not loaded yet. Load.
//                m_shieldTextures.put(textObj.shield_icon, m_loader.syncLoad(textObj.shield_icon));
//            } 
//            else if (it.IsLoaded())
//            {
//                // Texture is loaded. Draw shield
//                TnMapUse u = new TnMapUse(it);
//                if (textObj.shield_buffer != null)
//                {
//                    textObj.shield_buffer.position(textObj.shield_begin);
//                    gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), textObj.shield_buffer);
//                    textObj.shield_buffer.position(textObj.shield_begin + 3 * 4 + TnMapColor.size());
//                    gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), textObj.shield_buffer);
//                    textObj.shield_buffer.position(textObj.shield_begin + 3 * 4);
//                    gl10.glColorPointer(4, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), textObj.shield_buffer);
//                    gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, (textObj.shield_end - textObj.shield_begin) / TnMapVertex5f4b.size());
//                }                
//                u.destroy();
//            }
//
//            // Draw text
//            if (m_texture != null && m_texture.IsLoaded())
//            {
//                gl10.glTexEnvf(TnGL10.GL_TEXTURE_ENV, TnGL10.GL_TEXTURE_ENV_MODE, TnGL10.GL_BLEND);
//
//                TnMapUse u = new TnMapUse(m_texture);
//                if (textObj.text_buffer != null)
//                {
//                    textObj.text_buffer.position(textObj.shield_begin);
//                    gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f4b.size(), textObj.text_buffer);
//                    textObj.text_buffer.position(textObj.shield_begin + 3 * 4 + TnMapColor.size());
//                    gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f4b.size() , textObj.text_buffer);
//                    textObj.text_buffer.position(textObj.shield_begin + 3 * 4);
//                    gl10.glColorPointer(4, TnGL10.GL_FLOAT, TnMapVertex5f4b.size() , textObj.text_buffer);
//                    gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, (textObj.shield_end - textObj.shield_begin) / TnMapVertex5f4b.size());
//                }
//                gl10.glTexEnvf(TnGL10.GL_TEXTURE_ENV, TnGL10.GL_TEXTURE_ENV_MODE, TnGL10.GL_MODULATE);
//                
//                u.destroy();
//            }
//        }
//
//        // TnMapCheckGL("TnMapES1TextFlagRender::Render end");
    }
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapTile.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Vector;

import com.telenav.datatypes.GlobalCoordinateUtil;
import com.telenav.datatypes.map.MapUtil;
import com.telenav.datatypes.map.Tile;
import com.telenav.datatypes.map.TileMark;
import com.telenav.datatypes.map.VectorMapEdge;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapVertex3f4b;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f4b;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Transform;
import com.telenav.map.opengl.java.math.Triangulator;
import com.telenav.nio.TnNioManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapRenderingData
{
    Tile tile;
    
    double[] m_origin;

    Matrixf m_viewMatrix = new Matrixf();

    BufferElement edgeBuffer, polygonBuffer;

    BufferElement text2dBuffer;

//    Text3DVBO m_text3DVBO = new Text3DVBO();
//
//    ShieldVBO m_shieldVBO = new ShieldVBO();

    ArrowVBO m_arrowVBO = new ArrowVBO();

    public static class Arrow
    {

        public Arrow(double[] _start, double[] _end, int _arrowType)
        {
            start = _start;
            end = _end;
            arrowType = _arrowType;
        }

        public double[] start;

        public double[] end;

        public int arrowType;
    };
    
    public TnMapRenderingData(Tile tile)
    {
        this.tile = tile;
        m_origin = new double[3];
        TnMapMagic.tileIdToPoint(tile.getId(), m_origin);
    }

    public void destroy()
    {
        // @TODO: replace this with a queue in the renderer so it is
        // guaranteed to happen in the render thread.
        edgeBuffer = null;
    }

    public void setViewMatrix(Matrixf viewMatrix)
    {
        m_viewMatrix = new Matrixf(viewMatrix);
    }

    public Matrixf getViewMatrix()
    {
        return m_viewMatrix;
    }

    public void setRoadEdgeData(VectorMapEdge[] edges)
    {
        Vector geom = new Vector();
        int[] layers = new int[TnMapConf.FEATURE_TYPE_COUNT << 1];
        for (int i=0; i<layers.length; i++)
            layers[i] = -1;
        
        for (int i = 0; i < TnMapConf.FEATURE_TYPE_COUNT; ++i)
        {
            // correspond to region/road/line (i.e. what was called
            // nFeatureType when originally read in as an edge type
            // (argh!))
            int featureType = TnMapConf.m_arrTypeMap[i];
            if (MapUtil.isPolygon((byte)featureType))
                continue;
            
            TnMapColor color = TnMapConf.getFeatureColor(i);
            double width = TnMapConf.getFeatureWidth(i, tile.getZoom()) * ((double) (1 << tile.getZoom()));

            //width *= 0.6; // Just to see what it would look like with thinner roads.

            int begin = geom.size();

            for (int j = 0; j < edges.length; j++)
            {
                VectorMapEdge edge = edges[j];
                if ((edge.getRoadType() == featureType) && (edge.getShapePointsSize() >= 2))
                {
                    Vector vertices = Triangulator.edgeToTriStrip(edge, m_origin, width, TnMapConf.MAX_EDGE_SEGMENT_ANGLE);

                    for (int k = 0; k < vertices.size(); k++)
                    {
                        Pair tmpPair = (Pair) vertices.elementAt(k);
                        geom.addElement(Pe.buildVert((double[]) tmpPair.first, (double[]) tmpPair.second, color));
                    }

                    // For railways. This is an abomination, but until we
                    // fix the way layers work...

//                    if (TnMapConf.IsDashed(featureType))
//                    {
//                        TnMapColor tmpColor = new TnMapColor();
//                        tmpColor.r = 196;
//                        tmpColor.g = 196;
//                        tmpColor.b = 196;
//                        tmpColor.a = 255;
//                        vertices = Triangulator.edgeToDashed(edge.points, width * 0.8, TnMapConf.MAX_EDGE_SEGMENT_ANGLE);
//                        for (int k = 0; k < vertices.size(); k++)
//                        {
//                            Pair tmpPair = (Pair) vertices.elementAt(k);
//                            geom.add(buildVert((double[]) tmpPair.first, (double[]) tmpPair.second, tmpColor));
//                        }
//
//                    }

                }
            }
            int end = geom.size();
            int count = end - begin;
            int layer = TnMapConf.m_arrFeatureLayer[i];
            if (begin != end)
            {
                if (layers[layer << 1] == -1)
                {
                    layers[layer << 1] = begin;
                    layers[(layer << 1) + 1] = count;
                }
                else
                {
                    layers[(layer << 1) + 1] += count;
                }
            }
        }
        
        edgeBuffer = new BufferElement();
        edgeBuffer.layers = layers;
        
        int geomCount = geom.size();
        ByteBuffer pointerbb = TnNioManager.getInstance().allocateDirect(geomCount * 3 * 4);
        FloatBuffer pointerfb = pointerbb.asFloatBuffer();
        
        ByteBuffer texCoodbb = TnNioManager.getInstance().allocateDirect(geomCount * 2 * 4);
        FloatBuffer texCoodfb = texCoodbb.asFloatBuffer();
        
        ByteBuffer colorbb = TnNioManager.getInstance().allocateDirect(geomCount * 4);
        
        for (int n=0; n<geomCount; n++)
        {
            TnMapVertex5f4b vertex = (TnMapVertex5f4b)geom.elementAt(n);
            pointerfb.put(vertex.x);
            pointerfb.put(vertex.y);
            pointerfb.put(vertex.z);
            
            colorbb.put((byte)vertex.color.r);
            colorbb.put((byte)vertex.color.g);
            colorbb.put((byte)vertex.color.b);
            colorbb.put((byte)vertex.color.a);
            
            texCoodfb.put(vertex.s);
            texCoodfb.put(vertex.t);
        }
        geom.removeAllElements();
        edgeBuffer.pointerBuffer = pointerbb;
        edgeBuffer.texCoordBuffer = texCoodbb;
        edgeBuffer.colorBuffer = colorbb;
        edgeBuffer.layers = layers;
    }

    public void setPolygonData(VectorMapEdge[] edges)
    {
        Vector geom = new Vector();
        int[] layers = new int[TnMapConf.FEATURE_TYPE_COUNT << 1];
        for (int i=0; i<layers.length; i++)
            layers[i] = -1;

        // @TODO: All server-specific secret knowlege needs to be removed
        // from the engine.
        for (int i = 0; i < TnMapConf.FEATURE_TYPE_COUNT; ++i)
        {
            // correspond to region/road/line (i.e. what was called
            // nFeatureType when originally read in as an edge type
            // (argh!))
            int featureType = TnMapConf.m_arrTypeMap[i];
            if (!MapUtil.isPolygon((byte)featureType))
                continue;

            int begin = geom.size();

            for (int j = 0; j < edges.length; j++)
            {
                VectorMapEdge polygon = edges[j];

                if (polygon.getRoadType() == featureType)
                {
                    TnMapColor color = TnMapConf.getFeatureColor(i);

                    Vector triangles = Triangulator.polygonToTriangles(polygon, m_origin);
                    for (int k = 0; k < triangles.size(); k++)
                    {
                        double[] tmpPoint = (double[]) triangles.elementAt(k);
                        geom.addElement(Pe.buildVert(tmpPoint, color));
                    }
                }
            }

            int end = geom.size();
            int count = end - begin;
            int layer = TnMapConf.m_arrFeatureLayer[i];
            if (begin != end)
            {
                if (layers[layer << 1] == -1)
                {
                    layers[layer << 1] = begin;
                    layers[(layer << 1) + 1] = count;
                }
                else
                {
                    layers[(layer << 1) + 1] += count;
                }
            }
        }
        
        polygonBuffer = new BufferElement();
        polygonBuffer.layers = layers;
        
        int geomCount = geom.size();
        ByteBuffer pointerbb = TnNioManager.getInstance().allocateDirect(geomCount * 3 * 4);
        FloatBuffer pointerfb = pointerbb.asFloatBuffer();
        
        ByteBuffer colorbb = TnNioManager.getInstance().allocateDirect(geomCount * 4);
        
        for (int n=0; n<geomCount; n++)
        {
            TnMapVertex3f4b vertex = (TnMapVertex3f4b)geom.elementAt(n);
            pointerfb.put(vertex.x);
            pointerfb.put(vertex.y);
            pointerfb.put(vertex.z);
            
            colorbb.put((byte)vertex.color.r);
            colorbb.put((byte)vertex.color.g);
            colorbb.put((byte)vertex.color.b);
            colorbb.put((byte)vertex.color.a);
            
        }
        geom.removeAllElements();
        polygonBuffer.pointerBuffer = pointerbb;
        polygonBuffer.colorBuffer = colorbb;
        polygonBuffer.layers = layers;
    }

//    public void SetText3DData(Vector texts)
//    {
//        m_text3DVBO.clear();
//        m_shieldVBO.clear();
//        Vector textgeom = new Vector();
//        
//        Vector shieldgeom = new Vector();
//
//        for (int i = 0; i < texts.size(); i++)
//        {
//            Text3D text = (Text3D) texts.elementAt(i);
//            // Default = no shield
//            int shieldIndex = -1;
//
//            // Shield icon geometry
//            String icon = text.icon == null || text.icon.trim().length() == 0 ? DEFAULT_SIGN : text.icon;
//
//            // Calculate width of string
//            float size = text.size;
//            float total_width = 0;
//            for (int j = 0; j < text.text.length(); j++)
//            {
//                total_width += TnMapText.TnMapGetCharWidth(text.text.charAt(j));
//            }
//
//            // Halfwidth and halfheight of text
//            float hw = total_width * 0.5f;
//            float hh = size * 0.5f;
//
//            // Default = white text
//            TnMapColor textColor = new TnMapColor();
//            TnMapColor shieldColor = new TnMapColor();
//            textColor.r = shieldColor.r = 255;
//            textColor.g = shieldColor.g = 255;
//            textColor.b = shieldColor.b = 255;
//            textColor.a = shieldColor.a = 255;
//
//            // Default text z offset
//            float textZoffset = -1.5f;
//
//            // Default actual text draw size
//            float textHalfHeight = hh;
//
//            if (icon != null && icon.trim().length() != 0)
//            {
//                // This text will have a shield
//                shieldIndex = m_shieldVBO.segments.size();
//
//                int begin = shieldgeom.size();
//
//                if (DEFAULT_SIGN.equals(icon))
//                {
//                    float signHeight = hh * 5.0f;
//
//                    float[] p0 = new float[]{-hw - hh, 0.0f, 0.0f};
//                    float[] p1 = new float[]{-hw - hh, 0.0f, signHeight};
//                    float[] p2 = new float[]{-hw, 0.0f, 0.0f};
//                    float[] p3 = new float[]{-hw, 0.0f, signHeight};
//                    float[] p4 = new float[]{-hh, 0.0f, 0.0f};
//                    float[] p5 = new float[]{-hh, 0.0f, signHeight};
//                    float[] p6 = new float[]{hh, 0.0f, 0.0f};
//                    float[] p7 = new float[]{hh, 0.0f, signHeight};
//                    float[] p8 = new float[]{hw, 0.0f, 0.0f};
//                    float[] p9 = new float[]{hw, 0.0f, signHeight};
//                    float[] p10 = new float[]{hw + hh, 0.0f, 0.0f};
//                    float[] p11 = new float[]{hw + hh, 0.0f, signHeight};
//
//                    // Default billboard texture is 5 parts:
//                    //
//                    // Left cap x: (0 - 2/10)
//                    // Left horizontal x: (2/10 - 3/10)
//                    // Center pointer x: (3/10 - 7/10)
//                    // Right horizontal x: (7/10 - 8/10)
//                    // Right cap x: (8/10 - 1)
//
//                    float[] st0 = new float[]{0.0f, 1.0f};
//                    float[] st1 = new float[]{0.0f, 0.0f};
//                    float[] st2 = new float[]{0.2f, 1.0f};
//                    float[] st3 = new float[]{0.2f, 0.0f};
//                    float[] st4 = new float[]{0.3f, 1.0f};
//                    float[] st5 = new float[]{0.3f, 0.0f};
//                    float[] st6 = new float[]{0.7f, 1.0f};
//                    float[] st7 = new float[]{0.7f, 0.0f};
//                    float[] st8 = new float[]{0.8f, 1.0f};
//                    float[] st9 = new float[]{0.8f, 0.0f};
//                    float[] st10 = new float[]{1.0f, 1.0f};
//                    float[] st11 = new float[]{1.0f, 0.0f};
//
//                    // independent triangles
//
//                    shieldgeom.add(buildVert(p0, st0, shieldColor));
//                    shieldgeom.add(buildVert(p1, st1, shieldColor));
//                    shieldgeom.add(buildVert(p2, st2, shieldColor));
//
//                    shieldgeom.add(buildVert(p1, st1, shieldColor));
//                    shieldgeom.add(buildVert(p3, st3, shieldColor));
//                    shieldgeom.add(buildVert(p2, st2, shieldColor));
//
//                    shieldgeom.add(buildVert(p2, st2, shieldColor));
//                    shieldgeom.add(buildVert(p3, st3, shieldColor));
//                    shieldgeom.add(buildVert(p4, st4, shieldColor));
//
//                    shieldgeom.add(buildVert(p3, st3, shieldColor));
//                    shieldgeom.add(buildVert(p5, st5, shieldColor));
//                    shieldgeom.add(buildVert(p4, st4, shieldColor));
//
//                    shieldgeom.add(buildVert(p4, st4, shieldColor));
//                    shieldgeom.add(buildVert(p5, st5, shieldColor));
//                    shieldgeom.add(buildVert(p6, st6, shieldColor));
//
//                    shieldgeom.add(buildVert(p5, st5, shieldColor));
//                    shieldgeom.add(buildVert(p7, st7, shieldColor));
//                    shieldgeom.add(buildVert(p6, st6, shieldColor));
//
//                    shieldgeom.add(buildVert(p6, st6, shieldColor));
//                    shieldgeom.add(buildVert(p7, st7, shieldColor));
//                    shieldgeom.add(buildVert(p8, st8, shieldColor));
//
//                    shieldgeom.add(buildVert(p7, st7, shieldColor));
//                    shieldgeom.add(buildVert(p9, st9, shieldColor));
//                    shieldgeom.add(buildVert(p8, st8, shieldColor));
//
//                    shieldgeom.add(buildVert(p8, st8, shieldColor));
//                    shieldgeom.add(buildVert(p9, st9, shieldColor));
//                    shieldgeom.add(buildVert(p10, st10, shieldColor));
//
//                    shieldgeom.add(buildVert(p9, st9, shieldColor));
//                    shieldgeom.add(buildVert(p11, st11, shieldColor));
//                    shieldgeom.add(buildVert(p10, st10, shieldColor));
//
//                    // All text black
//                    textColor.r = 0;
//                    textColor.g = 0;
//                    textColor.b = 0;
//
//                    // Default signs get offset because their origin is at the bottom
//                    textZoffset = hh * 3.0f - 1.25f;
//                }
//                else
//                {
//                    // Highway signs - simple! two triangles, full-size texture
//                    float signSize = hh * 2.5f;
//
//                    float[] p0 = new float[]{signSize, 0.0f, -signSize};
//                    float[] p1 = new float[]{signSize, 0.0f, signSize};
//                    float[] p2 = new float[]{-signSize, 0.0f, -signSize};
//                    float[] p3 = new float[]{-signSize, 0.0f, signSize};
//
//                    float[] st0 = new float[]{TnGL.EDGE, 0.0f};
//                    float[] st1 = new float[]{TnGL.EDGE, TnGL.EDGE};
//                    float[] st2 = new float[]{0.0f, 0.0f};
//                    float[] st3 = new float[]{0.0f, TnGL.EDGE};
//
//                    // independent triangles
//
//                    shieldgeom.add(buildVert(p0, st0));
//                    shieldgeom.add(buildVert(p2, st2));
//                    shieldgeom.add(buildVert(p1, st1));
//
//                    shieldgeom.add(buildVert(p2, st2));
//                    shieldgeom.add(buildVert(p3, st3));
//                    shieldgeom.add(buildVert(p1, st1));
//
//                    // Change text color to black for all but interstate shield
//                    if (!"USA".equals(icon))
//                    {
//                        textColor.r = 0;
//                        textColor.g = 0;
//                        textColor.b = 0;
//
//                        // Bump up slightly
//                        textZoffset += 0.5f;
//                    }
//                    else
//                    {
//                        // Bump down slightly
//                        textZoffset -= 0.5f;
//                    }
//
//                }
//
//                int end = shieldgeom.size();
//                int count = end - begin;
////                System.out.println("---shieldVBO: " + "pos[0] = " + text.pos.points[0] + "pos[1] = " + text.pos.points[1] + "pos[2] = " + text.pos.points[2] + ", begin = " + begin + ", count = " + count);
//                m_shieldVBO.segments.add(new ShieldVBOSegment(text.pos, begin, count, icon));
//            }
//
//            text = (Text3D) texts.elementAt(i);
//            // Text geometry
//            if (text.text != null && text.text.trim().length() != 0)
//            {
//                int begin = textgeom.size();
//
//                float currentSpacing;
//
//                float x = -total_width / 2.0f - size / 2.f;
//                for (int j = 0; j < text.text.length(); j++)
//                {
//                    char c = text.text.charAt(j);
//                    currentSpacing = TnMapText.TnMapGetCharWidth(c);
//                    x += currentSpacing / 2.f;
//
//                    float[] p0 = new float[]{x + size, 0.0f, textZoffset - textHalfHeight};
//                    float[] p1 = new float[]{x + size, 0.0f, textZoffset + textHalfHeight};
//                    float[] p2 = new float[]{x, 0.0f, textZoffset - textHalfHeight};
//                    float[] p3 = new float[]{x, 0.0f, textZoffset + textHalfHeight};
//
//                    float[] st0 = TnMapText.TnMapGetCharTexCoord(c, 1);
//                    float[] st1 = TnMapText.TnMapGetCharTexCoord(c, 2);
//                    float[] st2 = TnMapText.TnMapGetCharTexCoord(c, 0);
//                    float[] st3 = TnMapText.TnMapGetCharTexCoord(c, 3);
//
//                    // independent triangles
//
//                    textgeom.add(buildVert(p0, st0, textColor));
//                    textgeom.add(buildVert(p2, st2, textColor));
//                    textgeom.add(buildVert(p1, st1, textColor));
//
//                    textgeom.add(buildVert(p2, st2, textColor));
//                    textgeom.add(buildVert(p3, st3, textColor));
//                    textgeom.add(buildVert(p1, st1, textColor));
//
//                    // Space characters properly
//                    x += currentSpacing / 2.f;
//
//                }
//                int end = textgeom.size();
//                int count = end - begin;
//                int checksum = (int) TnMapUtil.TnMapCheckSum(text.text);
////                System.out.println("---text3DVBO: " + "pos[0] = " + text.pos.points[0] + "pos[1] = " + text.pos.points[1] + "pos[2] = " + text.pos.points[2] + ", begin = " + begin + ", count = " + count + ", checksum = " + checksum);
//                m_text3DVBO.segments.add(new Text3DVBOSegment(text.pos, begin, count, checksum, shieldIndex));
//
//                // #if 0
//                // {
//                // std::ostringstream out;
//                // out << "Flag: " << text.text
//                // << ", checksum: " << checksum
//                // << std::endl;
//                // TnMapLogError(out.str().c_str());
//                // }
//                // #endif
//            }
//        }
//        
//        m_text3DVBO.geom.addAll(textgeom);
//        
//        m_shieldVBO.geom.addAll(shieldgeom);
//    }

    public void setText2DData(TileMark[] tileMarks)
    {
        if (tileMarks == null || tileMarks.length == 0)
            return;
        
        Vector geom = new Vector();
        
        TnMapColor color = TnMapConf.getRoadTextColor();
        for (int i=0; i<tileMarks.length; i++)
        {
            TileMark tm = tileMarks[i];
            int markType = tm.getMarkType();
            if(markType == TileMark.MARK_TYPE_MAP_LABEL)
            {
                int labelType = tm.getLabelType();
                String name = tm.getName();
                if ((labelType >= TileMark.LABEL_TYPE_CHARACTER_MIN) &&
                    (labelType <= TileMark.LABEL_TYPE_CHARACTER_MAX))
                {
                    float[] p = new float[3];
                    p[0] = (float)(tm.getPositionLon() - m_origin[0]);
                    p[1] = (float)(GlobalCoordinateUtil.GLOBAL_LENGTH - tm.getPositionLat() - m_origin[1]);
                    p[2] = 0;
                    if (name != null && name.trim().length() != 0)
                    {
                        char c = name.charAt(0);

                        float size = (float) (tm.getSize() << tile.getZoom()) * TnMapConf.TEXT_2D_SCALE_FACTOR;
                        float heading = tm.getHeading();

                        Matrixf m1 = new Matrixf();
                        m1.buildRotateZDeg(-heading);
                        float[] up = Transform.transform(m1, new float[]{0.0f, size, 0.0f});
                        float[] side = new float[]{up[1], -up[0], up[2]};

                        float[] p0 = Pe.sub(Pe.add(p, up), side);
                        float[] st0 = TnMapText.getCharTexCoord(c, 3);

                        float[] p1 = Pe.add(Pe.add(p, up), side);
                        float[] st1 = TnMapText.getCharTexCoord(c, 2);

                        float[] p2 = Pe.sub(Pe.sub(p, up), side);
                        float[] st2 = TnMapText.getCharTexCoord(c, 0);

                        float[] p3 = Pe.add(Pe.sub(p, up), side);
                        float[] st3 = TnMapText.getCharTexCoord(c, 1);

                        // independent triangles

                        geom.addElement(Pe.buildVert(p0, st0, color));
                        geom.addElement(Pe.buildVert(p2, st2, color));
                        geom.addElement(Pe.buildVert(p1, st1, color));

                        geom.addElement(Pe.buildVert(p2, st2, color));
                        geom.addElement(Pe.buildVert(p3, st3, color));
                        geom.addElement(Pe.buildVert(p1, st1, color));
                    }
                }
//                else if((labelType == TnMsMarkMagic.LABEL_TYPE_ROAD_ICON) &&
//                       name.length()>0)
//                {
//                    // Parse label and (if exists) icon name
//                    String icon = name;
//                    String label = name;
//                    if(name.indexOf('-') != -1)
//                    {
//                        String ss = name;
//                        label = name.substring(0, name.indexOf('-'));
//                        int count = Math.max(1, Math.min(3, label.length()));
//                        icon = icon + '_';
//                        icon = icon + '0' + count;
//                    }
//                    
//                    textData.NewShield(label, icon, lat, lon, 0.0, heading, fontSize);
//                }
//                else if((labelType == TnMsMarkMagic.LABEL_TYPE_ARROW_POSITIVE) ||
//                        (labelType == TnMsMarkMagic.LABEL_TYPE_ARROW_NEGATIVE) ||
//                        (labelType == TnMsMarkMagic.LABEL_TYPE_SEGMENT))
//                {
//                    // Parse start and end lat/lon from "name"
//                    String ss = name;
//                    char ch = ','; 
//                    double startx = 0, starty = 0, endx = 0, endy = 0;
//                    
//                    int index = ss.indexOf(ch);
//                    if(index != -1)
//                    {
//                        String strStartx = ss.substring(0, index);
//                        ss = ss.substring(index+1);
//                        startx = Double.parseDouble(strStartx);
//                    }
//                    
//                    index = ss.indexOf(ch);
//                    if(index != -1)
//                    {
//                        String strStarty = ss.substring(0, index);
//                        ss = ss.substring(index+1);
//                        starty = Double.parseDouble(strStarty);
//                    }
//                    
//                    index = ss.indexOf(ch);
//                    if(index != -1)
//                    {
//                        String strEndx = ss.substring(0, index);
//                        ss = ss.substring(index+1);
//                        starty = Double.parseDouble(strEndx);
//                    }
//                    
//                    index = ss.indexOf(ch);
//                    if(index != -1)
//                    {
//                        String strEndy = ss.substring(0, index);
//                        ss = ss.substring(index+1);
//                        starty = Double.parseDouble(strEndy);
//                    }
//        
//                    double startlat = lat + starty/1000000.0; // NOTE: Extra zero
//                    double startlon = lon + startx/1000000.0; // this is on purpose
//                    double endlat = startlat + endy/1000000.0;
//                    double endlon = startlon + endx/1000000.0;
//    //              System.out.println("startLat="+startlat+";"+"startlon="+startlon+"endlat="+endlat+"endlon="+endlon);
//                    textData.NewArrow(startlat, startlon, 0.0, endlat, endlon, 0.0, labelType);
//                }
//                else if(labelType < TnMsMarkMagic.LABEL_TYPE_CHARACTER_MIN)
//                {
//                    // We get some interesting stuff with this labelType.
//                    // @TODO: Investigate
//                }
            }
        }
        
        setText2DData(geom);
    }
    
    private void setText2DData(Vector geom)
    {
        text2dBuffer = new BufferElement();

        if (geom.size() > 0)
        {
            // New geometry. Build a vbo.
            text2dBuffer.layers = new int[1];
            text2dBuffer.layers[0] = geom.size();
            
            int pointerSize = geom.size() * 3 * 4;
            int texCoordSize = geom.size() * 2 * 4;
            int colorSize = geom.size() * 4;

            ByteBuffer pointerbb = TnNioManager.getInstance().allocateDirect(pointerSize);
            FloatBuffer pointerfb = pointerbb.asFloatBuffer();
            ByteBuffer colorbb = TnNioManager.getInstance().allocateDirect(colorSize);
            ByteBuffer texCoordbb = TnNioManager.getInstance().allocateDirect(texCoordSize);
            FloatBuffer texCoordfb = texCoordbb.asFloatBuffer();
            for (int n = 0; n < geom.size(); n++)
            {
                TnMapVertex5f4b vertex = (TnMapVertex5f4b) geom.elementAt(n);
                pointerfb.put(vertex.x);
                pointerfb.put(vertex.y);
                pointerfb.put(vertex.z);
                colorbb.put((byte)vertex.color.r);
                colorbb.put((byte)vertex.color.g);
                colorbb.put((byte)vertex.color.b);
                colorbb.put((byte)vertex.color.a);
                texCoordfb.put(vertex.s);
                texCoordfb.put(vertex.t);
            }
            text2dBuffer.pointerBuffer = pointerbb;
            text2dBuffer.colorBuffer = colorbb;
            text2dBuffer.texCoordBuffer = texCoordbb;
            geom.removeAllElements();
        }
        
    }

    public void setArrowData(Vector arrows)
    {
        // @TODO: Build geometry associated with one-way arrows

        m_arrowVBO.clear();
        Vector geom = new Vector();

        for (int i = 0; i < arrows.size(); i++)
        {
            Arrow arrow = (Arrow) arrows.elementAt(i);

            // Fudge factor to control the width of the arrows.
            double arrowWidthFactor = 0.45;

            // Compute the vector that represents half the width of the arrow in world coordinates
            double[] halfAlong = Pe.multiply(Pe.sub(arrow.end, arrow.start), arrowWidthFactor);
            double[] halfAcross = new double[]{halfAlong[1], -halfAlong[0], halfAlong[2]};

            double[] p0 = Pe.sub(arrow.end, halfAcross);
            double[] p1 = Pe.add(arrow.end, halfAcross);
            double[] p2 = Pe.sub(arrow.start, halfAcross);
            double[] p3 = Pe.add(arrow.start, halfAcross);

            // Arrow style
            TnMapColor color = new TnMapColor();
            double[] st0 = new double[0];
            double[] st1 = new double[0];
            double[] st2 = new double[0];
            double[] st3 = new double[0];

            if (arrow.arrowType == TileMark.LABEL_TYPE_ARROW_POSITIVE)
            {
                st0 = new double[]{0.0, 1.0};
                st1 = new double[]{1.0, 1.0};
                st2 = new double[]{0.0, 0.0};
                st3 = new double[]{1.0, 0.0};
                color.r = (byte)0x00;
                color.g = (byte)0x00;
                color.b = (byte)0xff;
                color.a = (byte)0xff;
            }
            else if (arrow.arrowType == TileMark.LABEL_TYPE_ARROW_NEGATIVE)
            {
                st0 = new double[]{0.0, 0.0};
                st1 = new double[]{1.0, 0.0};
                st2 = new double[]{0.0, 1.0};
                st3 = new double[]{1.0, 1.0};
                color.r = (byte)0x00;
                color.g = (byte)0x00;
                color.b = (byte)0xff;
                color.a = (byte)0xff;
            }
            else if (arrow.arrowType == TileMark.LABEL_TYPE_SEGMENT)
            {
                double[] skinny = Pe.multiply(halfAcross, 0.03);

                p0 = Pe.sub(arrow.end, skinny);
                p1 = Pe.add(arrow.end, skinny);
                p2 = Pe.sub(arrow.start, skinny);
                p3 = Pe.add(arrow.start, skinny);

                st0 = new double[]{0.0, 0.5};
                st1 = new double[]{1.0, 0.5};
                st2 = new double[]{0.0, 0.25};
                st3 = new double[]{1.0, 0.25};
                color.r = (byte)0xff;
                color.g = (byte)0x00;
                color.b = (byte)0x00;
                color.a = (byte)0xff;
            }

            // independent triangles

            geom.addElement(Pe.buildVert(p0, st0, color));
            geom.addElement(Pe.buildVert(p2, st2, color));
            geom.addElement(Pe.buildVert(p1, st1, color));

            geom.addElement(Pe.buildVert(p2, st2, color));
            geom.addElement(Pe.buildVert(p3, st3, color));
            geom.addElement(Pe.buildVert(p1, st1, color));
        }
        m_arrowVBO.count = geom.size();
        
        for (int i=0; i<geom.size(); i++)
            m_arrowVBO.geom.addElement(geom.elementAt(i));
    }

    // Add nearest pickables in the tile to the given map
    public BufferElement getEdgeBuffer()
    {
        return edgeBuffer;
    }

    public BufferElement getPolygonBuffer()
    {
        return polygonBuffer;
    }

    public BufferElement getText2DBuffer()
    {
        return text2dBuffer;
    }

//    public Text3DVBO GetText3DVBO()
//    {
//        return m_text3DVBO;
//    }
//
//    public ShieldVBO GetShieldVBO()
//    {
//        return m_shieldVBO;
//    }

    public ArrowVBO getArrowVBO()
    {
        return m_arrowVBO;
    }

    public double[] getOrigin()
    {
        return m_origin;
    }
    
    public static class BufferElement
    {
        public int[] layers = new int[TnMapConf.FEATURE_TYPE_COUNT << 1];
        public Buffer pointerBuffer, texCoordBuffer, colorBuffer;
    }

//    public static class Text3DVBOSegment
//    {
//        public Text3DVBOSegment(double[] _pos, int _begin, int _count, int _checksum, int _shieldIndex)
//        {
//            pos = _pos;
//            checksum = _checksum;
//            begin = _begin;
//            count = _count;
//            shieldIndex = _shieldIndex;
//        }
//
//        public double[] pos;
//
//        public int checksum;
//
//        public int begin;
//
//        public int count;
//
//        public int shieldIndex;
//    };
//
//    public static class Text3DVBO
//    {
//        // Independent triangles of TnMap5f4b.
//
//        public Text3DVBO()
//        {
//            vbo_id = new int[]{0};
//            geom = new Vector();
//            segments = new Vector();
//        }
//
//        public void clear()
//        {
//            geom.removeAllElements();
//            segments.removeAllElements();
//        }
//
//        public Vector geom;
//
//        public Buffer buffer;
//        public int[] vbo_id;
//
//        public Vector segments; // Text3DVBOSegment
//    };
//
//    public static class ShieldVBOSegment
//    {
//        public ShieldVBOSegment(double[] _pos, int _begin, int _count, String _icon)
//        {
//            pos = _pos;
//            begin = _begin;
//            count = _count;
//            icon = _icon;
//        }
//
//        public double[] pos;
//
//        public int begin;
//
//        public int count;
//
//        public String icon;
//    };
//
//    public static class ShieldVBO
//    {
//        // Independent triangles of TnMap5f4b.
//
//        public ShieldVBO()
//        {
//            vbo_id = new int[]{0};
//            geom = new Vector();
//            segments = new Vector();
//        }
//
//        public void clear()
//        {
//            geom.removeAllElements();
//            segments.removeAllElements();
//        }
//
//        public Vector geom;
//        
//        public Buffer buffer;
//
//        public int[] vbo_id;
//
//        public Vector segments; // ShieldVBOSegment
//    };

    public static class ArrowVBO
    {
        // Independent triangles of TnMap5f4b.

        public ArrowVBO()
        {
            vbo_id = new int[]{0};
            count = 0;
            geom = new Vector();
        }

        public void clear()
        {
            geom.removeAllElements();
        }

        public Vector geom;

        public Buffer buffer;
        
        public int[] vbo_id;

        public int count;
    };

}

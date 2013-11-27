/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapAnnotationBuilder.java
 *
 */
package com.telenav.map.opengl.java;

import java.util.Hashtable;

import com.telenav.map.opengl.java.TnMapAnnotation.TnMapAnnotation2D;
import com.telenav.map.opengl.java.TnMapAnnotation.TnMapAnnotation3D;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapAnnotationBuilder
{
    Hashtable graphicToTextureMap;

    // The texture loader
    TnMapTextureLoader textureLoader;

    TnGL10 gl10;

    public TnMapAnnotationBuilder(TnGL10 gl10, TnMapTextureLoader loader)
    {
        this.gl10 = gl10;
        textureLoader = loader;
        graphicToTextureMap = new Hashtable();
    }

    /*
     * ! Builds 2D Annotations for the 2D orthoganal view
     * 
     * Creates interleaved vertex data and stores in a render object. This is done for each annotation object passed to
     * it.
     */

    public TnMapAnnotation2D build2DAnnotation(int graphicId, double x, double y, int w, int h)
    {
        // For now, fail silently on bad GraphicId.
        TnMapTexture texture = (TnMapTexture) graphicToTextureMap.get(new Integer(graphicId));

        TnMapAnnotation2D annotation = new TnMapAnnotation2D((int) x, (int) y, w, h, texture);

        return annotation;
    }

    public TnMapAnnotation3D build3DAnnotation(int graphicId, double[] p, int w, int h, int px, int py)
    {
        TnMapTexture texture = (TnMapTexture) graphicToTextureMap.get(new Integer(graphicId));

        TnMapAnnotation3D annotation = new TnMapAnnotation3D(p, w, h, px, py, texture);

        return annotation;
    }

    public TnMapAnnotation3D build3DAnnotation(int graphicId, double[] p, int w, int h, int px, int py,
                                              float[] faceVector, float[] upVector, int layer)
    {
        TnMapTexture texture = (TnMapTexture) graphicToTextureMap.get(new Integer(graphicId));
        TnMapAnnotation3D annotation = new TnMapAnnotation3D(p, w, h, px, py, faceVector, upVector, texture, layer);
        
        return annotation;
    }
    
    public boolean addGraphic(int graphicId, TnMapTexture texture)
    {
        graphicToTextureMap.put(new Integer(graphicId), texture);
        return true;
    }

    public boolean buildGraphic(int graphicId, byte[] bytes, int width, int height)
    {
        // Create a new, blank texture object
        TnMapTexture texture = new TnMapTexture(this.gl10);

        // Enqueue an async texture load
//        m_textureLoader.EnqueueLoad(texture, bytes, width, height);

        return addGraphic(graphicId, texture);
    }

    public boolean removeGraphic(int graphicId)
    {
        // Enqueue an async texture unload
//        TnMapTexture texture = (TnMapTexture) graphicToTextureMap.get(new Integer(graphicId));
//        if (texture != null)
//        {
//            m_textureLoader.EnqueueUnload(texture);
//        }

        graphicToTextureMap.remove(new Integer(graphicId));

        return true;
    }
}

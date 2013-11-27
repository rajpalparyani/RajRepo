package com.telenav.map.opengl.java;

import java.util.Vector;

import com.telenav.map.IMapEngine;
import com.telenav.map.opengl.java.es10render.TnMapES10Render;
import com.telenav.tnui.opengles.TnGL10;

public class TnMapRenderManager
{
    private AbstractTnMapESRender m_render;

    public TnMapRenderManager(TnGL10 gl10, TnMapTextureLoader loader)
    {
        m_render = new TnMapES10Render(gl10, loader);
    }

    public void render(TnMapCanvas canvas, TnMapCameraManager cameraManager)
    {
        TnMapCamera camera = cameraManager.getCurrentCamera();

        float constantSizeFactor = TnMapMagic.zoomSpaceToReal(camera.getZoom(), 1.5f);

        Vector vecMapTiles = canvas.getCurrentScene().getMapTiles();
        
        Vector vecTrafficTiles = canvas.getCurrentScene().getTrafficTiles();

        boolean[] bTraffic = new boolean[]{false}; 
//        canvas.getBool(IMapEngine.PARAMETER_BOOL_SHOW_TRAFFIC, bTraffic);

//        boolean[] lameSkyFlag = new boolean[]{false}; 
//            canvas.getBool(IMapEngine.PARAMETER_BOOL_ALWAYS_SHOW_SKY, lameSkyFlag);
//
//        double[] lameSkyAmount = new double[1]; 
//            canvas.getDouble(IMapEngine.PARAMETER_DOUBLE_SKY_AMOUNT, lameSkyAmount);

        m_render.enableTraffic(bTraffic[0]);

//        m_render.render(vecMapTiles, vecTrafficTiles, cameraManager, canvas.getAnnotation2DLayers(), canvas.getAnnotation3DLayers(), canvas.getScreenRect(),
//            canvas.getVehicle(), canvas.getRoute(), canvas.getMapScale(), canvas.getSkyDome(), constantSizeFactor, lameSkyFlag[0],
//            (float)lameSkyAmount[0]);
    }
}
